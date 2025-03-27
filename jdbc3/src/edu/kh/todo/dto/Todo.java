package edu.kh.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Todo {
	private int memberNo;
	private int todoNo;
	private String todoTitle;
	private String todoDetails;
	private String todoStatus;
	private String todoDate;
}
