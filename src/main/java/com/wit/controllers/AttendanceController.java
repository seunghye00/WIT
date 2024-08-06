package com.wit.controllers;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wit.dto.AttendanceDTO;
import com.wit.dto.EmployeeDTO;
import com.wit.services.AttendanceService;
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

        // 월간 근태 현황 및 근무 시간 조회
        Map<String, Integer> monthlyStatus = service.monthlyStatus(empNo);
        Map<String, Object> monthlyWorkHours = service.monthlyWorkHours(empNo);
        List<Map<String, Object>> weeklyStatus = service.weeklyStatus(empNo);

        // 직원 정보 가져오기
        EmployeeDTO employee = service.employeeInfo(empNo);

        model.addAttribute("monthlyStatus", monthlyStatus);
        model.addAttribute("monthlyWorkHours", monthlyWorkHours);
        model.addAttribute("weeklyStatus", weeklyStatus);
        model.addAttribute("employee", employee);

        return "Attendance/attendance";
    }

    // 월간 근태현황 페이지로 이동
    @RequestMapping("/attendance_month")
    public String attendanceMonth(Model model) {
        String empNo = (String) session.getAttribute("loginID");

        // 현재 월을 가져옴
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 월간 근무 현황 조회
        List<Map<String, Object>> monthlyWorkStatus = service.monthlyWorkStatus(empNo, month);

        // 직원 정보 가져오기
        EmployeeDTO employee = service.employeeInfo(empNo);

        model.addAttribute("monthlyWorkStatus", monthlyWorkStatus);
        model.addAttribute("employee", employee);
        return "Attendance/attendanceMonth";
    }

    // 휴가관리 페이지로 이동
    @RequestMapping("/attendance_vacation")
    public String attendance_vacation(Model model) {
        String empNo = (String) session.getAttribute("loginID");

        // 직원 정보 가져오기
        EmployeeDTO employee = service.employeeInfo(empNo);

        model.addAttribute("employee", employee);

        return "Attendance/attendanceVacation";
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
}
