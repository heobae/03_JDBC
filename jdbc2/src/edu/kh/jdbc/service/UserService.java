package edu.kh.jdbc.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDAO;
import edu.kh.jdbc.dto.User;

// (Model 중 하나)Service : 비즈니스 로직을 처리하는 계층.
// 데이터를 가공하고 트랜잭션(commit, rollback) 관리 수행 << 비즈니스 로직
public class UserService {
	
	// 필드
	private UserDAO dao = new UserDAO();
	// Service가 DAO의 메서드를 활용해줘야 하기 때문에

	
	
	
	// 메서드
	/** 전달 받은 아이디와 일치하는 User 정보 반환 서비스
	 * @param input (입력된 아이디)
	 * @return 아이디가 일치하는 회원 정보가 담긴 User객체, 없으면 null 반환
	 */
	public User selectId(String input) { //User는 참조형
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 (할게 없으면 넘어감)
		
		// 3. DAO 메서드 호출 결과 반환
		User user = dao.selectId(conn, input);
		
		// 4. DML(commit / rollback)
		
		// 5. 다 쓴 커넥션 자원 반환
		JDBCTemplate.close(conn);
		
		// 6. 결과를 View에게 리턴
		
		return user; // View 클래스의 selectId로 리턴
	}




	/** 1. User 등록 서비스
	 * @param user : 입력받은 id, ow, name이 세팅된 객체
	 * @return 결과 행의 개수 
	 */
	public int insertUser(User user) throws Exception {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 (할게 없으면 넘어감)
		
		// 3. DAO 메서드 호출 후 결과 반환받기
		int result = dao.insertUser(conn, user);
		
		// 4. DML(INSERT) 수행 결과에 따라 트랜잭션 제어 처리
		if(result > 0) { // INSERT 성공
			JDBCTemplate.commit(conn);
			
		} else { // INSERT 실패
			JDBCTemplate.rollback(conn);
		}
		
		// 5. Connection 반환하기
		JDBCTemplate.close(conn);
		
		// 6. 결과 반환
		return result;
	}




	/** 2. User 전체 조회 서비스
	 * @return 조회된 User들이 담긴 List
	 * @throws Exception 
	 */
	public List<User> selectAll() throws Exception {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 (할거없으면 넘어가!)
		
		// 3. DAO 메서드 호출(SELECT) 후 결과 반환(List<User>) 받기
		List<User> userList = dao.seleteAll(conn);
		// 빨간 밑줄 create method
		// DAO에서 결과반환 받으면 생기는 빨간밑줄(예외발생) throws해주기
		
		// 4. Connection 반환
		JDBCTemplate.close(conn);
		
		// 5. 결과 반환
		return userList;
	}




	/** 3.  3. User 중 이름에 검색어가 포함된 회원 조회
	 * @param keyword : 입력한 키워드
	 * @return searchList : 조회된 회원 리스트
	 */
	public List<User> selectName(String keyword) throws Exception {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 (할거없어서 패스)
		
		// 3. DAO 메서드 호출(SELECT) 후 결과 반환(List<User>) 받기
		List<User> searchList = dao.selectName(conn, keyword);
		
		// 4. Connection 반환
		JDBCTemplate.close(conn);
		
		// 5. 결과 반환
		return searchList;
	}




	/** 4. USER_NO를 입력 받아 일치하는 User 조회
	 * @param input
	 * @return user (조회된 회원 정보 또는 null)
	 */
	public User selectUser(int input) throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		User user = dao.selectUser(conn, input);
		
		JDBCTemplate.close(conn);
		
		return user;
	}




	/** 5. USER_NO를 입력받아 일치하는 User 삭제
	 * @param input
	 * @return result
	 */
	public int deleteUser(int input) throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.deleteUser(conn, input);
		
		//결과에 따른 트랜잭션 처리 필요
		if(result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}




	/** 6-1. ID, PW가 일치하는 회원의 USER_NO 조회 서비스
	 * @param userId
	 * @param userPw
	 * @return userNo
	 */
	public int selectUserNo(String userId, String userPw) throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		int userNo = dao.selectUser(conn, userId, userPw);
		
		JDBCTemplate.close(conn);
		
		return userNo;
	}




	/** 6-2. userNo가 일치하는 회원의 이름 수정 서비스
	 * @param userName
	 * @param userNo
	 * @return result
	 */
	public int updateName(String userName, int userNo) throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateName(conn, userName, userNo);
		
		if(result > 0) JDBCTemplate.commit(conn);
		else		   JDBCTemplate.rollback(conn);
		
		JDBCTemplate.close(conn);
		
		return result;
	}




	/** 7. 아이디 중복 확인 서비스
	 * @param userId
	 * @return count
	 */
	public int idCheck(String userId) throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		int count = dao.idCheck(conn, userId);
		
		JDBCTemplate.close(conn);
		
		return count;
	}




	/** 8. userList에 있는 모든 User 객체를 INSERT 서비스
	 * @param userList
	 * @return
	 */
	public int multiInsertUser(List<User> userList) throws Exception {
		
		// 다중 INSERT 방법
		// 1) SQL을 이용한 다중 INSERT
		// 2) Java 반복문을 이용한 다중 INSERT ( 이거사용! )
		
		Connection conn = JDBCTemplate.getConnection();
		
		int count = 0;	// 삽입 성공한 행의 개수 count
		
		// 1행씩 삽입
		for(User user : userList) {
			int result = dao.insertUser(conn, user);
			count += result; //  삽입 성공한 행의 개수를 count 누적
		}
		
		// 트랜잭션 제어 처리
		// 전체 삽입 성공 시 commit / 아니면 rollback(일부삽입, 전체실패)
		if(count == userList.size()) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return count;
	}
	
}
