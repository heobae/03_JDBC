package edu.kh.jdbc.dao;

// import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와
// 클래스명, 메서드명()이 아닌 메서드명() 만 작성해도 호출 가능하게 함.
import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.dto.User;

// (Model 중 하나) DAO (Data Access Object)
// 데이터가 저장된 곳에 접근하는 용도의 객체
// -> DB에 접근하여 Java에서 원하는 결과를 얻기 위해
// SQL을 수행하고 결과를 반환 받는 역할
public class UserDAO {

	// 필드
	// DB 접근 관련한 JDBC 객체 참조 변수 미리 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	// Connection은 Service에서
	
	
	// 메서드
	
	/** 전달받은 Connection을 이용해서 DB에 접근하여
	 * 전달받은 아이디(input)와 일치하는 User 정보를 DB 조회하기
	 * @param conn : Service에서 생성한 Connection 객체
	 * @param input : View에서 입력받은 아이디
	 * @return 아이디가 일치하는 회원의 User 또는 null
	 */
	public User selectId(Connection conn, String input) {

		// 1. 결과 저장용 변수 선언
		User user = null;
		
		try {
		
			// 2. SQL 작성
			String sql = "SELECT * FROM TB_USER WHERE USER_ID =?";
			
			// 3. PreparedStatenebt 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? (위치홀더) 에 알맞은 값 세팅
			pstmt.setString(1, input);
			
			// 5. SQL 수행 후 결과 반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과가 있을 경우
			// + 중복 되는 아이디가 없다고 가정
			// -> 1행만 조회되기 때문에 while문 보다는 if를 사용하는게 효과적
			if(rs.next()) {
				// 첫 행에 데이터가 존재한다면
				// 각 커럼의 값 얻어오기
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				// java.sql.Date
				Date enrollDate = rs.getDate("ENROLL_DATE");
				
				// 조회된 컬럼값을 이용해서 User 객체 생성
				user = new User(userNo, userId, userPw, userName,
					    enrollDate.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			// 사용한 JDBC 객체 자원 반환(close)
			// JDBCTemplate.close(rs);
			// JDBCTemplate.close(pstmt);
			close(rs);
			close(pstmt); // import static 해서 생략
			
			// Connection 객체는 생성된 Service에서 close!
		}
		
		return user; // 결과 반환 (생성된 User 객체 또는 null)
		// Service 클래스의 selectId로 리턴
	}


	/** 1. User 등록 DAO
	 * @param conn : DB 연결 정보가 담겨있는 Connection 객체
	 * @param user : 입력받은 id, pw, name이 세팅된 User 객체
	 * @return INSERT 결과 행의 개수
	 */
	public int insertUser(Connection conn, User user) throws Exception { // throws는 매개변수 뒤에
		
		// SQL 수행 중 발생하는 예외를
		// catch로 처리하지 않고, throws를 이용해서 호출부로 던져 처리
		// -> catch 문 필요없다!
		
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		try {
			// 2. SQL 작성 (DBeaver에서 INSERT구문 ; 빼고 따오기)
			String sql = """
						INSERT INTO TB_USER 
						VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)
					"""; // id, pw, name 부분만 위치홀더로 바꿔주기
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? (위치홀더) 알맞은 값 대입
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());
			
			// 5. SQL(INSERT) 수행(executeUpdate) 후 결과(삽입된 행의 개수) 반환 받기
			result = pstmt.executeUpdate();
					
			
		} finally { // catch 안 하고 throws 할거임
			// 6. 사용한 JDBC 객체 자원 반환 (close)
			close(pstmt); // import static 해놔서 문구 생략 (기울임 보기)
			
		}
		
		// 결과 저장용 변수에 저장된 값 반환
		return result;
	}


	/** 3. User 전체 조회 DAO
	 * @param conn
	 * @return userList
	 */
	public List<User> seleteAll(Connection conn) throws Exception {
		
		// 1. 결과 저장용 변수 선언
		List<User> userList = new ArrayList<User>();
		
		try {
			// 2. SQL 작성 (DBeaver에서 작성해서 오기)
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					ORDER BY USER_NO
					""";
			
			// 3. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 에 알맞은 값 대입 (없으면 패스)
			
			// 5. SQL(SELECT) 수행(executeQuery) 후 결과(ResultSet) 반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과를 1행씩 접근하여 컬럼값 얻어오기
			// 몇 행이 조회될지 모른다	-> while
			// 무조건 1행만 조회된다	-> if
					
			while(rs.next()) {
				
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");
				// java.sql.Date 타입으로 값을 저장하지 않은 이유
				// -> SELECT 문에서 TO_CHAR()를 이용하여
				//	  문자열로 변환해 조회했기 때문에
				
				// User 객체 새로 생성하여 컬럼값 세팅하기
				User user = new User(userNo, userId, userPw, userName, enrollDate);
				
				userList.add(user);
			}
			
			
		} finally {
			// 7. 사용한 JDBC 객체 자원 반환 (close)
			close(rs);
			close(pstmt);
		}
		
		// 조회 결과가 담긴 List 반환
		return userList;
	}


	/** 3. 이름에 검색어가 포함되는 회원 모두 조회 DAO
	 * @param conn
	 * @param keyword
	 * @return searchList
	 */
	public List<User> selectName(Connection conn, String keyword) throws Exception {
		
		// 1. 결과 저장용 변수 선언
		List<User> searchList = new ArrayList<User>();
		
		try {
			// 2. SQL 작성
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					WHERE USER_NAME LIKE '%' || ? || '%'
					ORDER BY USER_NO
					""";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 에 알맞은 값 대입
			pstmt.setString(1, keyword);
			
			// 5. 수행 후 결과 반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과를 1행씩 접근하여 컬럼값 얻어오기
			// 몇 행이 조회될지 모른다	-> while
			// 무조건 1행만 조회된다	-> if
			while(rs.next()) {
				
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");
				
				
				// User 객체 새로 생성하여 컬럼값 세팅하기
				User user = new User(userNo, userId, userPw, userName, enrollDate);
				
				// user 객체 추가
				searchList.add(user);
			}
			
			
		} finally {
			// 7. 사용한 JDBC 객체 자원 반환 (close)
			close(rs);
			close(pstmt);
		}
		
		return searchList;
	}


	/** 4. USER_NO를 입력 받아 일치하는 User 조회
	 * @param conn
	 * @param input
	 * @return user (객체 또는 null)
	 */
	public User selectUser(Connection conn, int input) throws Exception {
		User user = null; // 선언
		
		try {
			
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, input);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");
				
				
				user = new User(userNo, userId, userPw, userName, enrollDate); // 할당
			//	User user = new User(userNo, userId, userPw, userName, enrollDate);
				// 새롭게 선언과 할당을 하는거라 중복이라 안됨
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return user;
	}


	/** 5. USER_NO를 입력받아 일치하는 User 삭제
	 * @param conn
	 * @param input
	 * @return result
	 */
	public int deleteUser(Connection conn, int input) throws Exception {
		int result = 0;
		
		try {
			
			String sql = """
					DELETE FROM TB_USER
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, input);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}


	/** 6-1. ID, PW가 일치하는 회원의 USER_NO 조회 DAO
	 * @param conn
	 * @param userId
	 * @param userPw
	 * @return userNo
	 */
	public int selectUser(Connection conn, String userId, String userPw) throws Exception {
		int userNo = 0;
		
		try {
			
			String sql = """
					SELECT USER_NO
					FROM TB_USER
					WHERE USER_ID = ?
					AND USER_PW = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			pstmt.setString(2, userPw);
			
			rs = pstmt.executeQuery();
			
			// 조회된 1행이 있을 경우
			if(rs.next()) {
				userNo = rs.getInt("USER_NO");
						
			}
			
			
			
		} finally {
			close(rs);
			close(pstmt);
		}
		return userNo; // 조회 성공 USER_NO, 실패 0 반환
	}


	/** 6-2. userNo가 일치하는 회원의 이름 수정 서비스
	 * @param conn
	 * @param userName
	 * @param userNo
	 * @return result
	 */
	public int updateName(Connection conn, String userName, int userNo) throws Exception {
		int result = 0;
		
		try {
			
			String sql = """
					UPDATE TB_USER
					SET USER_NAME = ?
					WHERE USER_NO = ?
					""";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setInt(2, userNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}


	/** 7. 아이디 중복 확인 DAO
	 * @param conn
	 * @param userId
	 * @return count
	 */
	public int idCheck(Connection conn, String userId) throws Exception {
		int count = 0;
		
		try {
			String sql = """
					SELECT COUNT(*)
					FROM TB_USER
					WHERE USER_ID = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1); // 컬럼명, 순서(1), 별칭 다 가능
							  // 조회된 컬럼 순서번호를 이용해 컬럼값 얻어오기
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return count;
	}
}
