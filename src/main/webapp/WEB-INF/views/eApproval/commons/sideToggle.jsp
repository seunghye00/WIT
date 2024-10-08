<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="addressListPrivate">
	<ul class="privateList">
		<li class="toggleItem">
			<c:choose>
				<c:when test="${type eq 'todo'}">
					<h3 class="toggleTit active">결재하기</h3>
					<ul class="subList active">
						<li><a href="/eApproval/apprList?type=todo&cPage=1" class="active">결재
								대기 문서</a></li>
						<li><a href="/eApproval/apprList?type=upcoming&cPage=1">결재 예정 문서</a></li>
					</ul>
				</c:when>
				<c:when test="${type eq 'upcoming'}">
					<h3 class="toggleTit active">결재하기</h3>
					<ul class="subList active">
						<li><a href="/eApproval/apprList?type=todo&cPage=1">결재 대기 문서</a></li>
						<li><a href="/eApproval/apprList?type=upcoming&cPage=1"
							class="active">결재 예정 문서</a></li>
					</ul>
				</c:when>
				<c:otherwise>
					<h3 class="toggleTit">결재하기</h3>
					<ul class="subList">
						<li><a href="/eApproval/apprList?type=todo&cPage=1">결재 대기 문서</a></li>
						<li><a href="/eApproval/apprList?type=upcoming&cPage=1">결재 예정 문서</a></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</li>
	</ul>
</div>
<div class="addressListGroup">
	<ul class="GroupList">
		<li class="toggleItem">
			<c:choose>
				<c:when test="${type eq 'write'}">
					<h3 class="toggleTit active">개인 문서함</h3>
					<ul class="subList active">
						<li><a href="/eApproval/privateList?type=write&cPage=1"
							class="active">기안 문서함</a></li>
						<li><a href="/eApproval/privateList?type=save&cPage=1">임시 저장 문서함</a></li>
						<li><a href="/eApproval/privateList?type=approved&cPage=1">결재 문서함</a></li>
						<li><a href="/eApproval/privateList?type=return&cPage=1">반려 문서함</a></li>
						<li><a href="/eApproval/privateList?type=view&cPage=1">참조 문서함</a></li>
					</ul>
				</c:when>
				<c:when test="${type eq 'save'}">
					<h3 class="toggleTit active">개인 문서함</h3>
					<ul class="subList active">
						<li><a href="/eApproval/privateList?type=write&cPage=1">기안 문서함</a></li>
						<li><a href="/eApproval/privateList?type=save&cPage=1" class="active">임시
								저장 문서함</a></li>
						<li><a href="/eApproval/privateList?type=approved&cPage=1">결재 문서함</a></li>
						<li><a href="/eApproval/privateList?type=return&cPage=1">반려 문서함</a></li>
						<li><a href="/eApproval/privateList?type=view&cPage=1">참조 문서함</a></li>
					</ul>
				</c:when>
				<c:when test="${type eq 'approved'}">
					<h3 class="toggleTit active">개인 문서함</h3>
					<ul class="subList active">
						<li><a href="/eApproval/privateList?type=write&cPage=1">기안 문서함</a></li>
						<li><a href="/eApproval/privateList?type=save&cPage=1">임시 저장 문서함</a></li>
						<li><a href="/eApproval/privateList?type=approved&cPage=1"
							class="active">결재 문서함</a></li>
						<li><a href="/eApproval/privateList?type=return&cPage=1">반려 문서함</a></li>
						<li><a href="/eApproval/privateList?type=view&cPage=1">참조 문서함</a></li>
					</ul>
				</c:when>
				<c:when test="${type eq 'return'}">
					<h3 class="toggleTit active">개인 문서함</h3>
					<ul class="subList active">
						<li><a href="/eApproval/privateList?type=write&cPage=1">기안 문서함</a></li>
						<li><a href="/eApproval/privateList?type=save&cPage=1">임시 저장 문서함</a></li>
						<li><a href="/eApproval/privateList?type=approved&cPage=1">결재 문서함</a></li>
						<li><a href="/eApproval/privateList?type=return&cPage=1"
							class="active">반려 문서함</a></li>
						<li><a href="/eApproval/privateList?type=view&cPage=1">참조 문서함</a></li>
					</ul>
				</c:when>
				<c:when test="${type eq 'view'}">
					<h3 class="toggleTit active">개인 문서함</h3>
					<ul class="subList active">
						<li><a href="/eApproval/privateList?type=write&cPage=1">기안 문서함</a></li>
						<li><a href="/eApproval/privateList?type=save&cPage=1">임시 저장 문서함</a></li>
						<li><a href="/eApproval/privateList?type=approved&cPage=1">결재 문서함</a></li>
						<li><a href="/eApproval/privateList?type=return&cPage=1">반려 문서함</a></li>
						<li><a href="/eApproval/privateList?type=view&cPage=1" class="active">참조
								문서함</a></li>
					</ul>
				</c:when>
				<c:otherwise>
					<h3 class="toggleTit">개인 문서함</h3>
					<ul class="subList">
						<li><a href="/eApproval/privateList?type=write&cPage=1">기안 문서함</a></li>
						<li><a href="/eApproval/privateList?type=save&cPage=1">임시 저장 문서함</a></li>
						<li><a href="/eApproval/privateList?type=approved&cPage=1">결재 문서함</a></li>
						<li><a href="/eApproval/privateList?type=return&cPage=1">반려 문서함</a></li>
						<li><a href="/eApproval/privateList?type=view&cPage=1">참조 문서함</a></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</li>
	</ul>
</div>