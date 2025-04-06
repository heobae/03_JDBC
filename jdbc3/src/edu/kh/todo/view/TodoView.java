package edu.kh.todo.view;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.todo.dto.Member;
import edu.kh.todo.dto.Todo;
import edu.kh.todo.service.TodoService;

public class TodoView {
	
	private Scanner sc = new Scanner(System.in);
	private TodoService service = new TodoService();
	private Member loginUser = null;
	private Object memberNo;
	private String memberId;
	
	/** User 관리 프로그램 메인 메뉴
	 */
	public void mainMenu() {
		
		int input = 0;
		
		do {
			try {
				
				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. 회원가입");
				System.out.println("2. 로그인");
				System.out.println("3. 내 TODO 전체 조회 (번호, 제목, 완료여부, 작성일)");
				System.out.println("4. 새로운 TODO 추가");
				System.out.println("5. TODO 수정 (제목, 내용)");
				System.out.println("6. 완료여부변경 (Y <-> N)");
				System.out.println("7. TODO 삭제");
				System.out.println("8. 로그아웃");
				System.out.println("0. 프로그램 종료");
				
				
				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거
				
				switch(input) {
				case 1: signUp(); break;
				case 2: logIn(); break;
				case 3: todoSelect(); break;
				case 4: addTodo(); break;
				case 5: updateTodo(); break;
				case 6: complete(); break;
				case 7: deleteTodo(); break;
				case 8: logOut(); break;
				
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
		
		if(loginUser != null) {
			System.out.println("로그아웃 후 이용해주세요");
			return;
		} 
		

	    // 중복 ID 입력 방지를 위한 루프
	    while (true) {
	        System.out.print("ID : ");
	        memberId = sc.next();

	        if (service.isIdDuplicated(memberId)) {
	            System.out.println("이미 가입된 ID입니다. 다시 입력해주세요.\n");
	        } else {
	            break; // 중복이 아니면 루프 탈출
	        }
	    }
		
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
	 * @throws Exception 
	 */
	private void logIn() throws Exception {
		
		System.out.print("=== 2. 로그인 ===\n");
		
		
		if(loginUser != null) {
			System.out.println("이미 로그인 중입니다.");
			return;
		}
		System.out.print("ID : ");
		String memberId = sc.next();
		
		System.out.print("PW : ");
		String memberPw = sc.next();
		
		loginUser = service.logIn(memberId, memberPw);
		
		if(loginUser==null) {
			System.out.println("로그인에 실패하셨습니다.");
			return;
		}
		
		System.out.printf("%s님 환영합니다.",loginUser.getMemberName());
		
				 
		/*		 1. 로그인
				 - 로그인 하는 대상을 저장하는 객체
				 - 로그인이 안되어 있어야 함, 객체가 널이면 로그인이 안돼있는거
				 -   */ 
	}
	
	/** 3. todo 전체조회
	 * @throws SQLException 
	 */
	private void todoSelect() throws SQLException {
		
		System.out.println("=== 전체 todoList 조회 ===\n");
		
		if(loginUser == null) {
			System.out.println("로그인 후 이용바랍니다.");
			return;
		}
		int result = loginUser.getMemberNo();
		
		List<Todo> todoList = service.todoSelect(result);
		
		if(todoList.isEmpty()) {
			System.out.println("\n***조회 결과가 없습니다 ***\n");
			return;
		}
		System.out.println(loginUser.getMemberName()+" : ");
		for(Todo todo : todoList) {
			System.out.printf("%d. %s - %s, 완료여부: %s, 작성일: %s\n", todo.getTodoNo(), todo.getTodoTitle(), 
					todo.getTodoDetails(), todo.getTodoStatus(), todo.getTodoDate());
			
		}
		
	}
	
	/** 4. todo 추가
	 * @throws SQLException 
	 */
	private void addTodo() throws SQLException {
		
		System.out.println("=== todoList 추가 ===\n");
		
		int memberno = loginUser.getMemberNo();
		
		if(loginUser == null) {
			System.out.println("로그인 후 이용바랍니다.");
			return;
		}	
			System.out.print("제목: ");
			String todoTitle = sc.nextLine();
			
			System.out.print("내용: ");
			String todoDetails = sc.nextLine();
			
			Todo todo = new Todo();
			
			todo.setTodoTitle(todoTitle);
			todo.setTodoDetails(todoDetails);
			
			int result = service.addTodo(todo, memberno);
			
			if(result > 0) {
				System.out.println("\n 할일이 등록되었습니다");
			} else {
				System.out.println("등록 실패!");
			}
			
		
	}
	
	/** 5. todo 수정
	 * @throws SQLException 
	 * 
	 */
	private void updateTodo() throws SQLException {

		System.out.println(" === Todo 수정 ===\n");
		
		if(loginUser == null) {
			System.out.println("로그인 후 이용바랍니다.");
			return;
		}	
		List<Todo> todoList = service.todoSelect(loginUser.getMemberNo());
		
	    if (todoList.isEmpty()) {
	        System.out.println("할 일이 없습니다.");
	        return;
	    }
		
	    int todoNo;
	    Todo selectedTodo = null;

	    // 유효한 todoNo가 입력될 때까지 반복
	    while (true) {
	        System.out.print("수정할 할 일 번호 : ");
	        todoNo = sc.nextInt();

	        for (Todo todo : todoList) {
	            if (todo.getTodoNo() == todoNo) {
	                selectedTodo = todo;
	                break;
	            }
	        }

	        if (selectedTodo != null) {
	            break;
	        } else {
	            System.out.println("해당되지 않는 번호입니다. 다시 입력해주세요.");
	        }
	    }
		
			System.out.print("수정할 제목 입력: ");
			String todoTitle = sc.next();
			
			System.out.print("수정할 내용 입력: ");
			String todoDetails = sc.next();
			
			int result = service.updateTodo(todoNo, todoTitle, todoDetails, loginUser.getMemberNo());
			
			if(result > 0) {
				System.out.println("수정 완료");
			} else {
				System.out.println("수정 실패!");
			}
		}	
		
	  
	
	/** 6. 완료 여부 변경
	 * @throws SQLException 
	 * 
	 */
	private void complete() throws SQLException {
		System.out.println("=== 완료 여부 변경 ===\n");
		
		if(loginUser == null) {
			System.out.println("로그인 후 이용바랍니다.");
			return;
		}
		
	    List<Todo> todoList = service.todoSelect(loginUser.getMemberNo());

	    if (todoList == null || todoList.isEmpty()) {
	        System.out.println("할 일이 없습니다.");
	        return;
	    }
		
	    int todoNo;
	    Todo selectedTodo = null;

	    while (true) {
	        System.out.print("완료 여부 수정할 할 일 번호 : ");
	        todoNo = sc.nextInt();

	        // 입력한 todoNo가 존재하는지 확인
	        for (Todo todo : todoList) {
	            if (todo.getTodoNo() == todoNo) {
	                selectedTodo = todo;
	                break;
	            }
	        }

	        if (selectedTodo != null) {
	            break; // 유효한 번호면 반복 종료
	        } else {
	            System.out.println("해당되지 않는 번호입니다. 다시 입력해주세요.");
	        }
	    }
		
		System.out.print("완료 여부를 변경하시겠습니까? (Y/N) : ");
		String todoStatus = sc.next().toUpperCase();
		
		if (!todoStatus.equals("Y") && !todoStatus.equals("N")) {
	        System.out.println("잘못된 입력입니다. Y 또는 N만 입력해주세요.");
	        return;
	    }

	    // 입력이 Y면 상태 변경, N이면 현재 상태 유지
	    // DB에서 현재 상태를 바꾸지 않고 처리하기 위해 DAO에 로직이 있어야 함

	    int result = service.complete(todoNo, todoStatus, loginUser.getMemberNo());

	    if (result > 0) {
	        System.out.println("변경 완료!");
	    } else {
	        System.out.println("변경 실패!");
	    }
	}

	/** 7. todo 삭제
	 * @throws SQLException
	 */
	private void deleteTodo() throws SQLException {
		
		System.out.println("\n===== todoList 삭제 =====\n");
		
		if(loginUser == null) {
			System.out.println("로그인 후 이용바랍니다.");
			return;
		}
		
		List<Todo> todoList = service.todoSelect(loginUser.getMemberNo());

	    if (todoList == null || todoList.isEmpty()) {
	        System.out.println("할 일이 없습니다.");
	        return;
	    }
		
		int result = loginUser.getMemberNo();
		
		int todoNo;
	    Todo selectedTodo = null;

	    // 유효한 todoNo 입력될 때까지 반복
	    while (true) {
	        System.out.print("삭제할 할 일 번호 : ");
	        todoNo = sc.nextInt();

	        for (Todo todo : todoList) {
	            if (todo.getTodoNo() == todoNo) {
	                selectedTodo = todo;
	                break;
	            }
	        }

	        if (selectedTodo != null) {
	            break;
	        } else {
	            System.out.println("해당되지 않는 번호입니다. 다시 입력해주세요.");
	        }
	    }
		
		 System.out.print("정말 삭제하시겠습니까? (Y/N) : ");
		    String confirm = sc.next().toUpperCase();

		    if (confirm.equals("Y")) {
		    	
		    	result = service.deleteTodo(todoNo, loginUser.getMemberNo());

		        if (result > 0) {
		            System.out.println("삭제 완료!");
		        } else {
		            System.out.println("삭제 실패!");
		        }
		    } else {
		        System.out.println("삭제 취소되었습니다.");
		    }
	}
	
	/** 8. 로그아웃
	 * 
	 */
	private void logOut() {
		
		if(loginUser == null) {
			System.out.println("로그인 상태가 아닙니다.");
		} else {
			loginUser = null;
			System.out.println("로그아웃 되었습니다.");
		}
	}	
}



