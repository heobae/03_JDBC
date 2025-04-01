package edu.kh.todo.service;

import java.sql.Connection;

import edu.kh.todo.common.JDBCTemplate;
import edu.kh.todo.dao.TodoDAO;
import edu.kh.todo.dto.Member;

public class TodoService {

	private TodoDAO dao = new TodoDAO();
	
	/** 1. 회원가입
	 * @param member
	 * @return 결과 행의 개수
	 * @throws Exception 
	 */
	public int signUp(Member member) throws Exception {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		
		// 2. 데이터 가공 (할게 없으면 넘어감)
		
		
		// 3. DAO 메서드 호출 후 결과 반환받기
		int result = dao.signUp(conn, member);
		
		// 4. DML(INSERT) 수행 결과에 따라 트랜잭션 제어 처리
		if(result > 0 ) { // insert 성공
			JDBCTemplate.commit(conn);
			
		} else { // insert 실패
			JDBCTemplate.rollback(conn);
			
		}
		
		// 5. Connection 반환하기
		JDBCTemplate.close(conn);
		
		// 6. 결과 반환
		return result;
	}

	public Member logIn(String memberId, String memberPw) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		Member loginUser = dao.logIn(conn, memberId, memberPw);
		
		JDBCTemplate.close(conn);
		
		return loginUser;
		
		
	}



}
