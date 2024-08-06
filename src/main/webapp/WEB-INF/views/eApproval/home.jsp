<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Document</title>
<link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css'
	rel='stylesheet'>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="/css/style.main.css">
<link rel="stylesheet" href="/css/wit.css">
<script defer src="/js/hsh.js"></script>
</head>

<body>
	<!-- 공통영역 -->
	<div class="container">
		<div class="sideBar">
			<div class="top">
				<i class="bx bx-menu" id="btn"></i>
			</div>
			<div class="user">
				<img src="/img/WIT_logo1.png" alt="me" class="userImg">
				<div class="nickName">
					<p class="bold">Wit Works</p>
					<p></p>
				</div>
			</div>

			<ul>
				<li><a href="#"> <i class='bx bxs-home-alt-2'></i> <span
						class="navItem">홈</span>
				</a> <span class="toolTip">홈</span></li>
				<li><a href="#"> <i class='bx bx-paperclip'></i> <span
						class="navItem">주소록</span>
				</a> <span class="toolTip">주소록</span></li>
				<li><a href="board2.html"> <i class="bx bxs-grid-alt"></i>
						<span class="navItem">게시판</span>
				</a> <span class="toolTip">게시판</span></li>
				<li><a href="#"> <i class='bx bx-calendar-alt'></i> <span
						class="navItem">캘린더</span>
				</a> <span class="toolTip">캘린더</span></li>
				<li><a href="#"> <i class='bx bxs-message-dots'></i> <span
						class="navItem">메신저</span>
				</a> <span class="toolTip">메신저</span></li>
				<li><a href="#"> <i class='bx bx-clipboard'></i> <span
						class="navItem">전자결재</span>
				</a> <span class="toolTip">전자결재</span></li>
				<li><a href="#"> <i class='bx bxs-briefcase-alt-2'></i> <span
						class="navItem">근태관리</span>
				</a> <span class="toolTip">근태관리</span></li>
				<li><a href="#"> <i class='bx bxs-check-square'></i> <span
						class="navItem">예약</span>
				</a> <span class="toolTip">예약</span></li>
				<li><a href="#"> <i class='bx bx-sitemap'></i> <span
						class="navItem">조직도</span>
				</a> <span class="toolTip">조직도</span></li>

			</ul>
		</div>
		<!-- 공통역역 끝 -->

		<div class="main-content">
			<div class="header">
				<span class="alert"><a href=""><i class='bx bxs-bell'></i></a></span>
				<!--마이페이지로 이동-->
				<span class="myName"> <img src="/img/푸바오.png"><a
					href=" #">백민주 사원</a></span> <span class="logOut"><a href="#">LogOut</a></span>
			</div>
			<div class="contents">
				<div class="sideAbout">
					<div class="sideTxt">
						<a href="home.html">
							<h2 class="sideTit">전자 결재</h2>
						</a>
					</div>
					<div class="sideBtnBox">
						<button class="plusBtn sideBtn" id="startApprBtn">새 결재 진행</button>
						<div id="docuModalBack" class="modalBack">
						<div class="eApprModal docuChoiModal">
							<div class="eApprModalTitle">
								문서 양식 선택 <span class="closeModal">X</span>
							</div>
							<div class="choiCont">
								<ul id="docuNameList"></ul>
							</div>
							<div class="modalBtnBox">
								<button class="next">다음</button>
								<button class="cancel red">취소</button>
							</div>
						</div>
						</div>
						<div class="eApprModal apprChoiModal">
							<div class="eApprModalTitle">
								결재선 선택 <span class="closeModal">X</span>
							</div>
							<div class="choiCont">
								<div class="deptList">
									<ul id="deptList"></ul>
								</div>
								<div class="employeeList">
									<ul id="employeeList"></ul>
								</div>
								<div class="arrBtns">
									<button id="addAppr">
										<img src="/img/icon/toggle.png" alt="">
									</button>
									<button id="delAppr">
										<img src="/img/icon/toggle.png" alt="">
									</button>
									<div style="padding: 30px 0;"></div>
									<button id="addRefe">
										<img src="/img/icon/toggle.png" alt="">
									</button>
									<button id="delRefe">
										<img src="/img/icon/toggle.png" alt="">
									</button>
								</div>
								<div class="resultAppr">
									<div class="apprList list">
										<p>결재 순서</p>
										<ul>
											<li id="firAppr">
												<div>첫번째 결재자</div>
											</li>
											<li id="secAppr">
												<div>두번째 결재자</div>
											</li>
											<li id="thirAppr">
												<div>세번째 결재자</div>
											</li>
										</ul>
									</div>
									<div class="refeList list">
										<p>참조 목록</p>
										<ul id="refeList"></ul>
									</div>
								</div>
							</div>
							<div class="modalBtnBox">
								<button class="done">완료</button>
								<button class="prev red" type="button">이전</button>
							</div>
						</div>
					</div>
					<div class="addressListPrivate">
						<ul class="privateList">
							<li class="toggleItem">
								<h3 class="toggleTit">결재하기</h3>
								<ul class="subList">
									<li><a href="javascript:;">결재 대기 문서</a></li>
									<li><a href="javascript:;">결재 수신 문서</a></li>
								</ul>
							</li>
						</ul>
					</div>
					<div class="addressListGroup">
						<ul class="GroupList">
							<li class="toggleItem">
								<h3 class="toggleTit">개인 문서함</h3>
								<ul class="subList">
									<li><a href="javascript:;">기안 문서함</a></li>
									<li><a href="javascript:;">임시 저장함</a></li>
									<li><a href="javascript:;">결재 문서함</a></li>
									<li><a href="javascript:;">참조 문서함</a></li>
								</ul>
							</li>
						</ul>
					</div>
				</div>
				<div class="sideContents eApproval">
					<div class="mainTitle">전자 결재 홈</div>

					<div class="docuList">
						<div class="subTitle">
							기안 진행 문서 <input type="checkbox" id="progInfo" hidden> <label
								class="titleIcon" for="progInfo"> <i
								class='bx bx-info-circle'></i>
							</label>
							<div class="infoBox">현재 진행중인 기안문서 5개를, 최근 등록 순서대로 표시합니다.</div>
						</div>

						<div class="listBox progList">
							<div class="rows listHeader">
								<div class="cols">
									<span>기안일</span>
								</div>
								<div class="cols">
									<span>문서 양식</span>
								</div>
								<div class="cols">
									<span>긴급</span>
								</div>
								<div class="cols">
									<span>제목</span>
								</div>
								<div class="cols">
									<span>결재 상태</span>
								</div>
							</div>
							<c:choose>
								<c:when test="${empty currentDocuList}">
									<div class="rows">
										<p>진행중인 문서가 없습니다.</p>
									</div>
								</c:when>
								<c:otherwise>
									<c:forEach items="${currentDocuList}" var="i">
										<div class="rows">
											<div class="cols">
												<span> <fmt:formatDate value="${i.write_date}"
														pattern="yyyy-MM-dd" /></span>
											</div>
											<div class="cols">
												<span>${i.name}</span>
											</div>
											<div class="cols">
												<span> <c:if test="${i.emer_yn eq 'Y'}">
														<img src="/img/icon/siren.png" class="emer">
													</c:if>
												</span>
											</div>
											<div class="cols">
												<span>${i.title}</span>
											</div>
											<div class="cols">
												<span>${i.status}</span>
											</div>
										</div>
									</c:forEach>
								</c:otherwise>
							</c:choose>

						</div>
					</div>
					<div class="docuList">
						<div class="subTitle">
							완료 문서 <input type="checkbox" id="doneInfo" hidden> <label
								class="titleIcon" for="doneInfo"> <i
								class='bx bx-info-circle'></i>
							</label>
							<div class="infoBox">최근에 결재 완료된 순서대로, 최대 5개의 목록을 표시합니다.</div>
						</div>
						<div class="listBox doneList">
							<div class="rows listHeader">
								<div class="cols">
									<span>기안일</span>
								</div>
								<div class="cols">
									<span>문서 양식</span>
								</div>
								<div class="cols">
									<span>긴급</span>
								</div>
								<div class="cols">
									<span>제목</span>
								</div>
								<div class="cols">
									<span>결재 상태</span>
								</div>
							</div>
							<c:choose>
								<c:when test="${empty doneDocuList}">
									<div class="rows emptyDocuList">
										<p>진행중인 문서가 없습니다.</p>
									</div>
								</c:when>
								<c:otherwise>
									<c:forEach items="${doneDocuList}" var="i">
										<div class="rows">
											<div class="cols">
												<span> <fmt:formatDate value="${i.write_date}"
														pattern="yyyy-MM-dd" /></span>
											</div>
											<div class="cols">
												<span>${i.name}</span>
											</div>
											<div class="cols">
												<span> <c:if test="${i.emer_yn eq 'Y'}">
														<img src="/img/icon/siren.png" class="emer">
													</c:if>
												</span>
											</div>
											<div class="cols">
												<span>${i.title}</span>
											</div>
											<div class="cols">
												<span>${i.status}</span>
											</div>
										</div>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

</html>