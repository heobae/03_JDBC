package edu.kh.todo.view;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TotoView {
	
	private Scanner sc = new Scanner(System.in);
	
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
				case 1:  break;
				case 2:  break;
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

}
