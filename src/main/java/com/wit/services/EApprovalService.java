package com.wit.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wit.dao.EApprovalDAO;
import com.wit.dto.DocuListDTO;
import com.wit.dto.RecentDocuListDTO;

@Service
public class EApprovalService {

	@Autowired
	private EApprovalDAO dao;
	
	// 문서 상태에 따라 해당 사원이 작성한 문서 목록을 넘겨주기 위한 메서드
	public List<RecentDocuListDTO> selectByStatus(String status, String emp_no) {
		return dao.selectByStatus(status, emp_no);
	}
	
	// 문서 양식 목록을 넘겨주기 위한 메서드 
	public List<DocuListDTO> getDocuList() {
		return dao.getDocuList();
	}

}
