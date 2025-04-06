package edu.kh.todo.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import edu.kh.todo.common.JDBCTemplate;
import edu.kh.todo.dao.TodoDAO;
import edu.kh.todo.dto.Member;
import edu.kh.todo.dto.Todo;

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
	
	/** 1-2. id 중복확인
	 * @param memberId
	 * @return
	 * @throws SQLException
	 */
	public boolean isIdDuplicated(String memberId) throws SQLException {
		
		Connection conn = JDBCTemplate.getConnection();
	    return dao.isIdDuplicated(conn, memberId);
	    
	    
	}

	
	/** 2. 로그인
	 * @param memberId
	 * @param memberPw
	 * @return
	 * @throws Exception
	 */
	public Member logIn(String memberId, String memberPw) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		Member loginUser = dao.logIn(conn, memberId, memberPw);
		
		JDBCTemplate.close(conn);
		
		return loginUser;
		
	}


	/** 3. todo 전체조회
	 * @param result
	 * @return
	 * @throws SQLException
	 */
	public List<Todo> todoSelect(int result) throws SQLException {
		
		Connection conn = JDBCTemplate.getConnection();
		
		List<Todo> todoList = dao.todoSelect(conn, result);
		
		JDBCTemplate.close(conn);
		
		return todoList;
		
	}


	/** 4. todo 추가
	 * @param todo
	 * @param memberno 
	 * @return
	 * @throws SQLException 
	 */
	public int addTodo(Todo todo, int memberno) throws SQLException {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.addtodo(conn, todo, memberno);
		
		if(result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}


	/** 5. todo 수정
	 * @param todoTitle
	 * @param todoDetails
	 * @param memberNo
	 * @return
	 * @throws SQLException 
	 */
	public int updateTodo(int todoNo, String todoTitle, String todoDetails, int memberNo) throws SQLException {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateTodo(conn, todoNo, todoTitle, todoDetails, memberNo);
		
		if(result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		return result;
	}

	/** 6. 완료 여부 변경
	 * @param todoNo
	 * @param todoStatus
	 * @param memberNo
	 * @return
	 * @throws SQLException
	 */
	public int complete(int todoNo, String todoStatus, int memberNo) throws SQLException {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.complete(conn, todoStatus, todoNo, memberNo);
		
		if(result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		} return result;
	}

	/** 7. todo 삭제
	 * @param todoNo
	 * @param memberNo
	 * @return
	 * @throws SQLException 
	 */
	public int deleteTodo(int todoNo, int memberNo) throws SQLException {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.deleteTodo(conn, todoNo, memberNo);
		
		if(result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		} return result;
	}




}
