package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample6 {

	public static void main(String[] args) {
		
		// 아이디, 비밀번호, 이름을 입력받아
		// 아이디, 비밀번호가 일치하는 사용자의
		// 이름을 수정(UPDATE)
		
		// 1. PreparedStatement 이용하기
		// 2. commit/rollback 처리하기
		// 3. 성공 시 "수정 성공! 출력 / 실패 시 "아이디 또는 비밀번호 불일치" 출력
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		Scanner sc = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String type = "jdbc:oracle:thin:@"; // 드라이버의 종류
			String host = "localhost"; // DB 서버 컴퓨터의 IP 또는 도메인주소 (localhost == 현재 컴퓨터)
			String port = ":1521"; // 프로그램 연결을 위한 port 번호
			String dbName = ":XE"; // DBSM 이름 (XE == eXpress Edition)

			String userName = "kh";		// 사용자 계정명
			String password = "kh1234";	// 계정 비밀번호
			
			// 2-3. DB 연결 정보와 DriverManager를 이용해서 Connection 생성
			conn = DriverManager.getConnection(type+host+port+dbName, userName, password);
			
			sc = new Scanner(System.in);
			
			System.out.print("아이디 입력 : ");
			String id = sc.nextLine();
			
			System.out.print("비밀번호 입력 : ");
			String pw = sc.nextLine();
			
			System.out.print("이름 입력 : ");
			String name = sc.nextLine();
			
			String sql = """
					UPDATE TB_USER
					SET USER_NAME = 
					WHERE 
					"""
			
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
