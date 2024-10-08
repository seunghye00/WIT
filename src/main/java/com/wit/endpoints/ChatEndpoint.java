package com.wit.endpoints;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wit.configurators.SpringProvider;
import com.wit.configurators.WSHttpSessionConfigurator;
import com.wit.dto.ChatDTO;
import com.wit.services.ChatService;

@ServerEndpoint(value = "/chat", configurator = WSHttpSessionConfigurator.class)
public class ChatEndpoint {

    // 전체 세션을 관리하는 Set
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    // 각 세션과 해당 세션의 사용자 로그인 ID를 매핑하는 맵
    private static final ConcurrentHashMap<Session, String> sessionUserMap = new ConcurrentHashMap<>();
    
    // 각 세션과 해당 세션이 속한 채팅방의 chatRoomSeq를 매핑하는 맵
    private static final ConcurrentHashMap<Session, String> sessionChatRoomMap = new ConcurrentHashMap<>();

    // ChatService 인스턴스 - Spring에서 관리
    private final ChatService cServ = SpringProvider.getSpring().getBean(ChatService.class);

    // Gson 객체 - 날짜 형식 지정
    private static final Gson gson = new GsonBuilder().setDateFormat("HH:mm").create();

    // HttpSession 객체 - WebSocket에서 사용자 세션 관리
    private HttpSession hSession;

    @OnOpen
    public void onConnect(Session session, EndpointConfig config) {
        try {
            // WebSocket 연결이 열리면 HttpSession을 가져와서 사용자 로그인 ID를 저장
            this.hSession = (HttpSession) config.getUserProperties().get("hSession");
            if (hSession == null) {
                throw new IllegalStateException("HttpSession이 null입니다.");
            }
            
            String loginID = (String) this.hSession.getAttribute("loginID");
            if (loginID == null) {
                throw new IllegalStateException("로그인 ID가 null입니다.");
            }

            // 세션을 전체 세션 Set에 추가하고, 사용자 맵에 로그인 ID 매핑
            sessions.add(session);
            sessionUserMap.put(session, loginID);
            // 사용자가 속한 모든 채팅방의 읽지 않은 메시지 수를 전송
            List<Map<String, Object>> userChatRooms = cServ.getChatRoomsByUserId(loginID);
            for (Map<String, Object> chatRoom : userChatRooms) {
                String chatRoomSeq = ((BigDecimal) chatRoom.get("CHAT_ROOM_SEQ")).toString();

                // UNREAD_COUNT는 BigDecimal이므로 변환
                int unreadCount = ((BigDecimal) chatRoom.get("UNREAD_COUNT")).intValue();

                // sessionChatRoomMap이 올바르게 초기화된 후에만 broadcast
                if (sessionUserMap.get(session) != null) {
                    broadcastUnreadCountUpdate(chatRoomSeq, unreadCount, loginID);
                } else {
                    System.err.println("Warning: sessionChatRoomMap is not initialized for session: " + session.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                session.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        JsonObject jsonMessage = gson.fromJson(message, JsonObject.class);
        String type = jsonMessage.get("type").getAsString();

        if ("join".equals(type)) {
            String chatRoomSeq = jsonMessage.get("chatRoomSeq").getAsString();
            sessionChatRoomMap.put(session, chatRoomSeq);

            // 로그인 ID 메시지를 보냄
            String loginID = sessionUserMap.get(session);
            sendLoginIDMessage(session, loginID);
            
            // 채팅 기록을 클라이언트에게 전송
            List<ChatDTO> chatList = cServ.chatListByRoom(chatRoomSeq);
            sendChatHistoryMessage(session, chatList);
            // 이전 메시지 처리 (보낸 사람을 제외, 이미 읽은 사람도 제외)
            for (ChatDTO chat : chatList) {
                if (!loginID.equals(chat.getName())) {
                    int updatedReadCount = cServ.decreaseReadCountBefore(chatRoomSeq, chat.getChat_seq(), loginID, chat.getName());
                    if (updatedReadCount >= 0) {
                        broadcastReadCountUpdate(chatRoomSeq, chat.getChat_seq(), updatedReadCount);
                    }
                }
            }
            
            
            // 읽지 않은 메시지 수를 업데이트하여 전송
            int unreadCount = cServ.getUnreadMessages(chatRoomSeq, loginID);
            broadcastUnreadCountUpdate(chatRoomSeq, unreadCount, loginID);
            
        } else if ("chat".equals(type)) {
            String chatRoomSeq = sessionChatRoomMap.get(session);
            processChatMessage(session, chatRoomSeq, jsonMessage);
        } else if ("read".equals(type)) {
            String chatRoomSeq = jsonMessage.get("chatRoomSeq").getAsString();
            int chatSeq = jsonMessage.get("chatSeq").getAsInt();
            String userName = jsonMessage.get("userName").getAsString();
            // 메시지 읽음 처리
            int updatedReadCount = cServ.decreaseReadCount(chatRoomSeq, chatSeq, userName);
            broadcastReadCountUpdate(chatRoomSeq, chatSeq, updatedReadCount);
            
            String loginID = sessionUserMap.get(session);
            // 읽지 않은 메시지 수를 업데이트하여 전송
            int unreadCount = cServ.getUnreadMessages(chatRoomSeq, loginID);
            broadcastUnreadCountUpdate(chatRoomSeq, unreadCount, loginID);
        }
    }

    private void processChatMessage(Session session, String chatRoomSeq, JsonObject jsonMessage) {
        String sender = sessionUserMap.get(session);
        if (sender == null) {
            return;
        }

        String userName = cServ.getUserNameByLoginID(sender);
        if (userName == null) {
            return;
        }

        int memberCount = cServ.getChatRoomMemberCount(chatRoomSeq);
        if (memberCount == 0) {
            return;
        }

        int chatSeq = cServ.insert(chatRoomSeq, sender, jsonMessage.get("message").getAsString(), memberCount - 1);
        if (chatSeq <= 0) {
            return;
        }
        JsonObject data = new JsonObject();
        data.addProperty("chat_seq", chatSeq);
        data.addProperty("chat_room_seq", chatRoomSeq);
        data.addProperty("sender", userName);
        data.addProperty("message", jsonMessage.get("message").getAsString());
        data.addProperty("send_time", getCurrentTime());
        data.addProperty("type", "chat");
        data.addProperty("name", sender);
        data.addProperty("read_count", memberCount - 1);

        synchronized (sessions) {
            for (Session client : sessions) {
                try {
                	if (client.isOpen()) {
                        // 자신에게는 알림 플래그 없이 메시지 전송
                        if (session.equals(client)) {
                            // 자신에게는 알림 플래그 없이 메시지 전송
                            client.getBasicRemote().sendText(data.toString());
                            // 읽지 않은 메시지 수를 업데이트하여 전송
                        }
                        int unreadCount = cServ.getUnreadMessages(chatRoomSeq, sessionUserMap.get(client));
                        broadcastUnreadCountUpdate(chatRoomSeq, unreadCount, sessionUserMap.get(client));
                        broadcastAlarm(chatRoomSeq, userName,  sessionUserMap.get(client));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
  
    @OnClose
    public void onClose(Session session) {
        // WebSocket 연결이 닫힐 때, 세션을 제거
        sessions.remove(session);
        sessionUserMap.remove(session);
        sessionChatRoomMap.remove(session);
    }

    @OnError
    public void onError(Throwable t, Session session) {
        // WebSocket 연결 중 에러 발생 시, 해당 세션을 제거
        sessions.remove(session);
        sessionUserMap.remove(session);
        sessionChatRoomMap.remove(session);
        t.printStackTrace();
    }

    // 현재 시간을 HH:mm 형식으로 반환하는 유틸리티 메서드
    private String getCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        return new SimpleDateFormat("HH:mm").format(currentTimeMillis);
    }
    
    private void sendLoginIDMessage(Session session, String loginID) {
        JsonObject loginIDResponse = new JsonObject();
        loginIDResponse.addProperty("type", "loginID");
        loginIDResponse.addProperty("loginID", loginID);
        try {
            session.getBasicRemote().sendText(loginIDResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendChatHistoryMessage(Session session, List<ChatDTO> chatList) {
        JsonObject response = new JsonObject();
        response.addProperty("type", "chatHistory");
        response.add("chatHistory", gson.toJsonTree(chatList));
        try {
            session.getBasicRemote().sendText(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void broadcastReadCountUpdate(String chatRoomSeq, int chatSeq, int updatedReadCount) {
        JsonObject data = new JsonObject();
        data.addProperty("type", "readCountUpdate");
        data.addProperty("chatRoomSeq", chatRoomSeq);
        data.addProperty("chatSeq", chatSeq);
        data.addProperty("updatedReadCount", updatedReadCount);

        synchronized (sessions) {
            for (Session client : sessions) {
                String recipientChatRoomSeq = sessionChatRoomMap.get(client);
                if (chatRoomSeq.equals(recipientChatRoomSeq) && client.isOpen()) {
                    try {
                        client.getBasicRemote().sendText(data.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    private void broadcastUnreadCountUpdate(String chatRoomSeq, int unreadCount, String empNo) {
        if (chatRoomSeq == null) {
            System.err.println("Error: chatRoomSeq is null");
            return;
        }

        JsonObject data = new JsonObject();
        data.addProperty("type", "unreadCountUpdate");
        data.addProperty("chatRoomSeq", chatRoomSeq);
        data.addProperty("unreadCount", unreadCount);
        data.addProperty("empNo", empNo); 

        synchronized (sessions) {
            for (Session client : sessions) {
                if (client.isOpen()) {
                    try {
                        client.getBasicRemote().sendText(data.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Error: client is not open. Session ID: " + client.getId());
                }
            }
        }
    }
    
    private void broadcastAlarm(String chatRoomSeq, String sender, String empNo) {
        if (chatRoomSeq == null) {
            System.err.println("Error: chatRoomSeq is null");
            return;
        }

        JsonObject data = new JsonObject();
        data.addProperty("type", "alarm");
        data.addProperty("chatRoomSeq", chatRoomSeq);
        data.addProperty("sender", sender);
        data.addProperty("empNo", empNo); 

        synchronized (sessions) {
            for (Session client : sessions) {
                if (client.isOpen()) {
                    try {
                        client.getBasicRemote().sendText(data.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Error: client is not open. Session ID: " + client.getId());
                }
            }
        }
    }
}

