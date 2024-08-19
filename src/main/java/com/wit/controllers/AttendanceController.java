package com.wit.controllers;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wit.commons.AttendanceConfig;
import com.wit.dto.AttendanceDTO;
import com.wit.dto.DeptDTO;
import com.wit.dto.EmployeeDTO;
import com.wit.services.AttendanceService;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService service;

	@Autowired
	private HttpSession session;

	// 출근 처리
	@RequestMapping(value = "/start", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> startAttendance() {
		String empNo = (String) session.getAttribute("loginID");
		return service.startAtd(empNo);
	}

	// 퇴근 처리
	@RequestMapping(value = "/end", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> endAttendance() {
		String empNo = (String) session.getAttribute("loginID");
		return service.endAtd(empNo);
	}

	// 근태관리 페이지로 이동
	@RequestMapping("/attendance")
	public String attendance(Model model) {
		String empNo = (String) session.getAttribute("loginID");

		// 월간 근태 현황 및 월간 근무 시간 조회 + 주간 근무현황
		Map<String, Integer> monthlyStatus = service.monthlyStatus(empNo);
		Map<String, Object> monthlyWorkHours = service.monthlyWorkHours(empNo);
		List<Map<String, Object>> weeklyStatus = service.weeklyStatus(empNo);

		EmployeeDTO employee = service.employeeInfo(empNo);

		model.addAttribute("monthlyStatus", monthlyStatus);
		model.addAttribute("monthlyWorkHours", monthlyWorkHours);
		model.addAttribute("weeklyStatus", weeklyStatus);
		model.addAttribute("employee", employee);

		return "Attendance/attendance";
	}

	// 월간 근태현황 페이지로 이동
	@RequestMapping("/attendance_month")
	public String attendanceMonth(Model model, @RequestParam(defaultValue = "1") int cpage) {
		String empNo = (String) session.getAttribute("loginID");

		// 현재 월을 가져옴
		String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

		// 레코드 수 조회
		int recordTotalCount = service.monthlyRecordCount(empNo, month);

		// 페이징 처리 로직
		int recordCountPerPage = AttendanceConfig.recordCountPerPage;
		System.out.println("레코드 카운트 펄페이지 : " + recordCountPerPage);
		int naviCountPerPage = AttendanceConfig.naviCountPerPage;
		System.out.println("네비 카운트 펄페이지 : " + naviCountPerPage);

		// 실제 레코드 수 기반 페이지 총 수 계산
		int pageTotalCount = (int) Math.ceil(recordTotalCount / (double) recordCountPerPage);
		System.out.println("페이지 토탈 카운트 : " + pageTotalCount);

		// cpage가 pageTotalCount를 초과하지 않도록 설정
		if (cpage > pageTotalCount) {
			cpage = pageTotalCount;
		}

		int startNavi = ((cpage - 1) / naviCountPerPage) * naviCountPerPage + 1;
		int endNavi = startNavi + naviCountPerPage - 1;

		if (endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}

		boolean needPrev = startNavi > 1;
		boolean needNext = endNavi < pageTotalCount;

		// 페이징 처리 결과를 모델에 추가
		model.addAttribute("cpage", cpage);
		model.addAttribute("startNavi", startNavi);
		model.addAttribute("endNavi", endNavi);
		model.addAttribute("needPrev", needPrev);
		model.addAttribute("needNext", needNext);

		// 월간 근무 현황 조회 (페이징 적용)
		List<Map<String, Object>> monthlyWorkStatus = service.monthlyWorkStatus(empNo, month, cpage,
				recordCountPerPage);
		model.addAttribute("monthlyWorkStatus", monthlyWorkStatus);

		return "Attendance/attendanceMonth";
	}

	// 출근 및 퇴근 시간 조회(메인 페이지)
	@RequestMapping(value = "/times", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> getAttendanceTimes() {
		String empNo = (String) session.getAttribute("loginID");
		Map<String, String> response = new HashMap<>();

		// 오늘의 출근 기록 조회
		AttendanceDTO record = service.getTodayAttendance(empNo);

		if (record != null) {
			response.put("startTime", record.getStart_time() != null ? record.getStart_time() : "00:00");
			response.put("endTime", record.getEnd_time() != null ? record.getEnd_time() : "00:00");
		} else {
			response.put("startTime", "00:00");
			response.put("endTime", "00:00");
		}

		return response;
	}

	// 부서별 근태현황 (관리자)
	@RequestMapping("/attendanceDept")
	public String attendanceDept(@RequestParam(value = "deptTitle", defaultValue = "인사부") String deptTitle,
			@RequestParam(value = "startDate", required = false) Date startDate,
			@RequestParam(value = "endDate", required = false) Date endDate, Model model) {
		String empNo = (String) session.getAttribute("loginID");

		// 부서 리스트 가져오기
		List<DeptDTO> departments = service.getDepartments();
		model.addAttribute("departments", departments);

		EmployeeDTO employee = service.employeeInfo(empNo);

		// 날짜가 없을 경우 현재 주로 설정하는 로직 추가
		if (startDate == null || endDate == null) {
			startDate = Date.valueOf(LocalDate.now().with(DayOfWeek.MONDAY));
			endDate = Date.valueOf(LocalDate.now().with(DayOfWeek.SATURDAY));
		}

		// 디버깅용 출력
		System.out.println("deptTitle: " + deptTitle);
		System.out.println("startDate: " + startDate);
		System.out.println("endDate: " + endDate);

		List<Map<String, Object>> attendanceData = service.deptAtd(deptTitle, startDate, endDate);

		// attendanceData 출력
		System.out.println("attendanceData size: " + attendanceData.size());
		for (Map<String, Object> attendanceItem : attendanceData) {
			System.out.println("Attendance Item: " + attendanceItem);
		}

		model.addAttribute("attendanceData", attendanceData);
		model.addAttribute("deptTitle", deptTitle);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("employee", employee);

		return "Admin/Attendance/attendanceDept";
	}

}
