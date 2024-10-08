package com.wit.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wit.commons.AttendanceConfig;
import com.wit.dao.AnnualLeaveDAO;
import com.wit.dao.EApprovalDAO;
import com.wit.dto.AnnualLeaveDTO;
import com.wit.dto.DeptDTO;
import com.wit.dto.EmployeeDTO;
import com.wit.dto.LeaveRequestDTO;

@Service
public class AnnualLeaveService {

	@Autowired
	private AnnualLeaveDAO dao;

	@Autowired
	private EApprovalDAO eDao;

	// 직원의 연차 정보를 가져옴
	public AnnualLeaveDTO getAnnualLeaveByEmpNo(String empNo) {
		return dao.getAnnualLeaveByEmpNo(empNo);
	}

	// 직원 정보 조회
	public EmployeeDTO employeeInfo(String empNo) {
		return dao.employeeInfo(empNo);
	}

	// 해당 직원의 남은 연차 갯수 조회
	public int getRemainingLeavesByEmpNo(String empNo) {
		return dao.getRemainingLeavesByEmpNo(empNo);
	}

	// 해당 직원의 연차 정보 업데이트
	public void updateAnnualLeave(int docuSeq, float useNum) {
		String empNo = eDao.getEmpNoByDocuSeq(docuSeq);
		dao.updateByAnnualLeave(empNo, useNum);
	}

	// 연차 사용 정보 기록
	public void insertAnnualLeaveLog(int docuSeq) {
		String empNo = eDao.getEmpNoByDocuSeq(docuSeq);
		dao.insertAnnualLeaveLog(empNo, docuSeq);
	}

	// 연간 휴가 내역의 총 레코드 수를 반환
	public int annualLeaveRecordCount(String empNo) {
		return dao.annualLeaveRecordCount(empNo);
	}

	// 연간 휴가 내역을 조회 (페이징 적용)
	public List<LeaveRequestDTO> selectAnnualLeaveRequests(String empNo, int cpage) {
		int recordCountPerPage = AttendanceConfig.recordCountPerPage;
		int start = (cpage - 1) * recordCountPerPage + 1;
		int end = cpage * recordCountPerPage;
		return dao.selectAnnualLeaveRequests(empNo, start, end);
	}

	// 부서별 휴가현황 (관리자)
	public List<Map<String, Object>> selectAnnualLeaveRequestsByDept(String deptTitle, String searchTxt, int cpage) {
		int recordCountPerPage = AttendanceConfig.recordCountPerPage;
		int start = (cpage - 1) * recordCountPerPage + 1;
		int end = cpage * recordCountPerPage;
		return dao.selectAnnualLeaveRequestsByDept(deptTitle, searchTxt, start, end);
	}

	// 검색된 연간 휴가 내역의 총 레코드 수 조회
	public int annualLeaveRecordCountByDept(String deptTitle, String searchTxt) {
		return dao.annualLeaveRecordCountByDept(deptTitle, searchTxt);
	}

	// 부서 리스트 조회
	public List<DeptDTO> getDepartments() {
		return dao.getDepartments();
	}
}
