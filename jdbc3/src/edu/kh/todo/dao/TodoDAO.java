package edu.kh.todo.dao;

import static edu.kh.todo.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.todo.dto.Member;
import edu.kh.todo.dto.Todo;

public class TodoDAO {

	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	
	public int signUp(Connection conn, Member member) throws Exception {
		
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		// 2. SQL 작성 (DBeaver에서 INSERT구문 ; 빼고 따오기)
		try {
					
			String sql = """
						INSERT INTO TB_MEMBER
						VALUES(SEQ_MEMBER_NO.NEXTVAL, ?, ?, ?)
						"""; // id, pw, name 부분만 위치홀더로 바꿔주기
   //  * 메모리에 값을 계속 바꿔가면서 저장할 수 있는 공간


			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? (위치홀더) 알맞은 값 대입
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getMemberPw());
			pstmt.setString(3, member.getMemberName());
			
			// 5. SQL 수행 후 결과 반환 받기
			result = pstmt.executeUpdate();
			
		} finally {// catch 안 하고 throws 할거임
			// 6. 사용한 JDBC 객체 자원 반환 (close)
			close(pstmt);		// import static 해놔서 문구 생략 (기울임 보기)
			close(rs);
		}
		
		return result;
	}


	public Member logIn(Connection conn, String memberId, String memberPw) throws Exception {
		Member loginUser = null;
		
		
		try {
			String sql = """
					SELECT * FROM TB_MEMBER
					WHERE MEMBER_ID = ? AND MEMBER_PW = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPw);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int userNo = rs.getInt("MEMBER_NO");
				String userId = rs.getString("MEMBER_ID");
				String userPw = rs.getString("MEMBER_PW");
				String userName = rs.getString("MEMBER_NAME");
				
				loginUser = new Member(userNo, userId, userPw, userName);
			}
					
		}  finally {
			close(rs);
			close(pstmt);
		}
		return loginUser;
	}


	public List<Todo> todoSelect(Connection conn, int result) throws SQLException {
		
		List<Todo> todoList = new ArrayList<Todo>();
		
		try {
			String sql = """
						SELECT MEMBER_NO, MEMBER_NAME, TODO_NO, TODO_TITLE, 
						TODO_DETAILS, TODO_DATE
						FROM TB_TODOLIST
						JOIN TB_MEMBER USING (MEMBER_NO)
						ORDER BY MEMBER_NO
					    """;
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int memberNo = rs.getInt("MEMBER_NO");
				String memberName = rs.getString("MEMBER_NAME");
				int todoNo = rs.getInt("TODO_NO");
				String todoTitle = rs.getString("TODO_TITLE");
				String todoDetails = rs.getString("TODO_DETAILS");
				String todoStatus = rs.getString("TODO_STATUS");
				String todoDate = rs.getString("TODO_DATE");
				
				Todo todo = new Todo(memberNo, todoNo, todoTitle, todoDetails, todoStatus, todoDate);
				
				todoList.add(todo);
			}
			
		} finally {
			close(rs);
			close(pstmt);
			
		} return todoList;
	}

}
