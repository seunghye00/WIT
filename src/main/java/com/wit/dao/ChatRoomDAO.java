package com.wit.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomDAO {
    @Autowired
    private SqlSession mybatis;

    // 채팅방 생성
    public void createChatRoom(Map<String, Object> params) {
        mybatis.insert("chatRoom.createChatRoom", params);
    }

    // 채팅방 멤버 추가
    public void addChatRoomMember(Map<String, Object> params) {
        mybatis.insert("chatRoom.addChatRoomMember", params);
    }

    // 그룹 채팅방 생성
    public void createGroupChat() {
    	mybatis.insert("chatRoom.createGroupChat");
    }

    // 개인 채팅방 존재 여부 확인
    public int isPrivateChatRoomExists(String emp_no1, String emp_no2) {
    	Map<String, Object> params = new HashMap<>();
	    params.put("emp_no1", emp_no1);
	    params.put("emp_no2", emp_no2);
        return mybatis.selectOne("chatRoom.isPrivateChatRoomExists", params);
    }

    // 최근 생성된 채팅방 시퀀스 조회
    public int getLastChatRoomSeq() {
        return mybatis.selectOne("chatRoom.getLastChatRoomSeq");
    }

    // 특정 사용자가 속한 채팅방 조회
    public List<Map<String, Object>> getChatRoomsByUserId(String empNo) {
        return mybatis.selectList("chatRoom.getChatRoomsByUserId", empNo);
    }

    // 채팅방 상세 조회
    public List<Map<String, Object>> getChatRoomName(Map<String, Object> params) {
        return mybatis.selectList("chatRoom.getChatRoomName", params);
    }
    // 채팅방 상세 조회
    public List<Map<String, Object>> getChatRoomMembers(Map<String, Object> params) {
        return mybatis.selectList("chatRoom.getChatRoomMembers", params);
    }

    // 채팅방 타이틀 수정
    public void updateChatRoomTitle(Map<String, Object> params) {
        mybatis.update("chatRoom.updateChatRoomTitle", params);
    }

    // 채팅방 코드 가져오기
    public String getChatRoomCode(Map<String, Object> params) {
        return mybatis.selectOne("chatRoom.getChatRoomCode", params);
    }

    // 채팅방 삭제
    public void deleteChatRoom(int chatRoomSeq) {
        mybatis.delete("chatRoom.deleteChatRoom", chatRoomSeq);
    }
    
    // 채팅방 멤버 조회
    public int getChatRoomMemberCount(int chatRoomSeq) {
    	return mybatis.selectOne("chatRoom.getChatRoomMemberCount", chatRoomSeq);
    }
    
    // 채팅방 마지막 멤버 조회
    public String getLastRemainingMember(int chatRoomSeq) {
    	return mybatis.selectOne("chatRoom.getLastRemainingMember", chatRoomSeq);
    }

    // 채팅방 멤버 삭제
    public void deleteChatRoomMember(int chatRoomSeq, String empNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("chatRoomSeq", chatRoomSeq);
        params.put("empNo", empNo);
        mybatis.delete("chatRoom.deleteChatRoomMember", params);
    }
}
