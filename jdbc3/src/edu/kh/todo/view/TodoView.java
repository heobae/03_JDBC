package edu.kh.todo.view;

import java.util.InputMismatchException;
import java.util.Scanner;

import edu.kh.todo.dto.Member;
import edu.kh.todo.service.TodoService;

public class TodoView {
	
	private Scanner sc = new Scanner(System.in);
	private TodoService service = new TodoService();
	
	/** User 관리 프로그램 메인 메뉴
	 */
	public void mainMenu() {
		
		int input = 0;
		
		do {
			try {
				
				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. 회원가입");
				System.out.println("2. 로그인");
				System.out.println("3. 내 TODO 전체 조회 (번호, 제목, 완료여부, 작성일");
				System.out.println("4. 새로운 TODO 추가");
				System.out.println("5. TODO 수정 (제목, 내용)");
				System.out.println("6. 완료여부변경 (Y <-> N");
				System.out.println("7. TODO 삭제");
				System.out.println("0. 로그아웃");
				
				
				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거
				
				switch(input) {
				case 1: signUp(); break;
				case 2: logIn(); break;
				case 3:  break;
				case 4:  break;
				case 5:  break;
				case 6:  break;
				case 7:  break;
				
				case 0 : System.out.println("\n[프로그램 종료]\n"); break;
				default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}
				
				System.out.println("\n-------------------------------------\n");
				
			} catch (InputMismatchException e) {
				// Scanner를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n***잘못 입력 하셨습니다***\n");
				
				input = -1; // 잘못 입력해서 while문 멈추는걸 방지
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자 제거
				
			} catch (Exception e) {
				// 발생되는 예외를 모두 해당 catch 구문으로 모아서 처리
				e.printStackTrace();
			}
			
		}while(input != 0);
		
	} // mainMenu() 종료


	/** 1. 회원가입
	 * @throws Exception 
	 */
	private void signUp() throws Exception {
		
		System.out.print("=== 1. 회원가입 ===\n");
		
		System.out.print("ID : ");
		String memberId = sc.next();
		
		System.out.print("PW : ");
		String memberPw = sc.next();
		
		System.out.print("NAME : ");
		String memberName = sc.next();
		
		// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
		// Member DTO 객체를 생성한 후 필드에 값을 세팅	
		Member member = new Member();
		
		// setter 이용
		member.setMemberId(memberId);
		member.setMemberPw(memberPw);
		member.setMemberName(memberName);
		
		// 서비스 호출(INSERT) 후 결과(int, 결과 행의 개수) 반환받기
		int result = service.signUp(member);
		
		// 반환된 결과에 따라 출력할 내용 선택
		if(result > 0 ) {
			System.out.println("\n" + memberId + "사용자가 등록되었습니다.\n");
			
		} else {
			System.out.println("회원가입 실패!");
		}
		
	}
	
	/** 2. 로그인
	 * 
	 */
	private void logIn() {
		
		System.out.print("=== 2. 로그인 ===\n");
		
		System.out.print("ID : ");
		String memberId = sc.next();
		
		System.out.print("PW : ");
		String memberPw = sc.next();
		
		Member member = new Member();
		
		 = service.logIn
				 
		/*		 1. 로그인
				 - 로그인 하는 대상을 저장하는 객체
				 - 로그인이 안되어 있어야 함, 객체가 널이면 로그인이 안돼있는거
				 -   */ 
		
	}
	
}
