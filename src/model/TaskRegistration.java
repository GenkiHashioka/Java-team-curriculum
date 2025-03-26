package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import controller.EnterTop;
import controller.TimeFormat;
import controller.Validation;
import sqlconnect.SelectSql;
import sqlconnect.SqlConnect;
import view.ConsoleColor;

public class TaskRegistration {
	// 入力された値を受け取る変数。
	String title = null;
	String inputLimit = null;
	java.sql.Date limit = java.sql.Date.valueOf(LocalDate.now());
	int departmentNum;
	String department = null;
	int staffNum;
	String staff = null;
	int statusNum;
	String status = null;
	String remarks = null;
	String created_by = Login.loggedInUser;
	public void start() throws SQLException {
		// 初期表示
		System.out.println("＝タスク登録＝");
		taskName();
		limit();
		department();
		staff();
		status();
		remarks();
		registration();
		EnterTop.start();
	}
	
	// タスク名入力
	public void taskName() {
		System.out.print("タスク名を入力してください : ");
		Scanner scanner = new Scanner(System.in);
		this.title = scanner.nextLine();
		if (Validation.nullCheck(this.title)) {
			System.out.println(ConsoleColor.toRed("タスク名は必須です。"));
			taskName();
		} else if (Validation.isLengthInRange(this.title, 0, 30) == false) {
			System.out.println(ConsoleColor.toRed("タスク名は30文字以内で入力してください。"));
			taskName();
		}
	}
	
	// 期限入力。
	public void limit() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("期限を入力してください (例: 2025/03/01) : ");
			this.inputLimit = scanner.nextLine().trim();
			if (Validation.nullCheck(inputLimit)) {
				System.out.println(ConsoleColor.toRed("期限は必須項目です。"));
				continue;
			}
			
			this.limit = TimeFormat.formatter(this.inputLimit);
			if (this.limit == null) {
				continue;
			}
			break;
		}
	}
	// 担当部署の入力
	public void department() {
		SelectSql.displayDepartment();
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		System.out.print("担当部署の番号を選択してください : ");
		Scanner scanner = new Scanner(System.in);
		try {
			this.departmentNum = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			System.out.println(ConsoleColor.toRed("数値を入力してください"));
			department();
		}
		if (Validation.departmentDataNumCheck(departmentNum) == false) {
			System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
			department();
		} else {
			this.department = SelectSql.getDepartmentName(departmentNum);			
		}
	}
	
	// 担当者の入力
	public void staff() {
		SelectSql.displayStaff();
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		System.out.print("担当者を選択してください : ");
		Scanner scanner = new Scanner(System.in);
		try {
			this.staffNum = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			System.out.println(ConsoleColor.toRed("数値を入力してください"));
			staff();
		}
		if (Validation.usersDataNumCheck(this.staffNum) == false) {
			System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
			staff();
		} else {
			this.staff = SelectSql.getStaffName(this.staffNum);			
		}
	}
	
	// ステータスの入力
	public void status() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				SelectSql.displayStatus();
				System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
				System.out.print("ステータス番号を入力してください : ");
				this.statusNum = scanner.nextInt();
				scanner.nextLine();
				if (!Validation.statusDataNumCheck(this.statusNum)) {
					System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
					continue;
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println(ConsoleColor.toRed("数値を入力してください"));
				scanner.nextLine();
			}
		}
		this.status = SelectSql.getStatus(this.statusNum);
	}
	
	// 備考を入力
	public void remarks() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("備考を入力してください : ");
			remarks = scanner.nextLine();
			if (!Validation.isLengthInRange(remarks, 0, 50)) {
				System.out.println(ConsoleColor.toRed("備考は50文字以内で入力してください。"));
				continue;
			}
			break;
		}
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
	}
	
	// タスク登録
	public void registration() throws SQLException {
		String sql = "INSERT INTO tasks (title, \"limit\", department, staff, status, remarks, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setString(1, this.title);
			pstmt.setDate(2, this.limit);
			pstmt.setInt(3, this.departmentNum);
			pstmt.setInt(4, this.staffNum);
			pstmt.setInt(5, this.statusNum);
			pstmt.setString(6, this.remarks);
			pstmt.setString(7, this.created_by);
			
			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println(ConsoleColor.toBlue("タスク名 : " + this.title));
				System.out.println(ConsoleColor.toBlue("期限 : " + this.inputLimit));
				System.out.println(ConsoleColor.toBlue("担当部署 : " + this.department));
				System.out.println(ConsoleColor.toBlue("担当者 : " + this.staff));
				System.out.println(ConsoleColor.toBlue("ステータス : " + this.status));
				System.out.println(ConsoleColor.toBlue("備考 : " + this.remarks));
				System.out.println(ConsoleColor.toBlue("登録が完了しました"));
			}
		} catch (SQLException e) {
			System.out.println(ConsoleColor.toRed("登録に失敗しました。"));
			EnterTop.start();
		}
	}
}
