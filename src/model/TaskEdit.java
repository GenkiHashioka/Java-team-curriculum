package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import controller.EnterTop;
import controller.TimeFormat;
import controller.Validation;
import sqlconnect.SelectSql;
import sqlconnect.SqlConnect;
import view.ConsoleColor;

public class TaskEdit {
	// TopPageのインスタンス
	TopPage top = new TopPage();
	
	// 完了済みに変更するためのメソッド
	public void changeToCompleted() throws SQLException{
		// 実行SQL
		String sql = "SELECT t.id, sm.status, t.title, u.name, t.remarks, t.created_at " +
					 "FROM tasks t " +
					 "INNER JOIN status_mst sm ON t.status = sm.id " +
					 "INNER JOIN users u ON t.staff = u.id " +
					 "WHERE sm.status = '未完了' AND (u.name = ? OR t.created_by = ?) " +
					 "ORDER BY t.id ASC";
		// datetimeformatterでdate型のフォーマットを変換
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		// Scannerのインスタンス
		Scanner scanner = new Scanner(System.in);
		// 完了済みタスクのタイトル
		String completedTitle = null;
		
		try (Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, Login.loggedInUser);
			pstmt.setString(2, Login.loggedInUser);
			ResultSet rs = pstmt.executeQuery();
			// 対象タスクを表示
			System.out.println("＝ステータスを完了に変更＝");
			while (rs.next()) {
				int id = rs.getInt("id");
				String status = rs.getString("status");
				String title = rs.getString("title");
				String staff = rs.getString("name");
				String remarks = rs.getString("remarks");
				java.sql.Date createdAt = rs.getDate("created_at");
				// yyyy/MM/dd
				String formattedDate = createdAt.toLocalDate().format(formatter);
				System.out.println(id + " [" + status + "] " + title + " 担当者 : " + staff + " 備考 : " + remarks + " 作成日 : " + formattedDate);
			}
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int completed = 0;
		
		while (true) {
			try {
				System.out.print("完了に変更するタスクの番号を入力してください : ");
				completed = scanner.nextInt();
				scanner.nextLine();
				if (!Validation.checkCreatedAndInCharge(completed)) {
					System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
					continue;
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println(ConsoleColor.toRed("数値を入力してください"));
				scanner.nextLine();
			}
		}
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		
		String updateSql = "UPDATE tasks SET status = ?, updated_by = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
		String selectSql = "SELECT title FROM tasks WHERE id = ?";
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement updatePs = connection.prepareStatement(updateSql);
			 PreparedStatement selectPs = connection.prepareStatement(selectSql)) {
			// タスクの更新
			updatePs.setInt(1, 1);
			updatePs.setString(2, Login.loggedInUser);
			updatePs.setInt(3, completed);
			int rowsUpdated = updatePs.executeUpdate();
			// 選択したタスク名を取得
			selectPs.setInt(1, completed);
			try (ResultSet rs = selectPs.executeQuery()) {
				while (rs.next()) {
					completedTitle = rs.getString("title");
				}
			}
			if (rowsUpdated > 0) {
				System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
				System.out.println(ConsoleColor.toBlue("タスク名 : " + completedTitle + " を完了に変更しました"));
			} else {
				System.out.println("失敗");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		EnterTop.start();
	}
	
	
	// タスク削除
	public void delete() throws SQLException {
		// 削除したタイトル名
		String deleteTitle = null;
		// 全タスク表示
		String sql = "SELECT t.id, sm.status, t.title, u.name, t.remarks, t.created_at " +
					 "FROM tasks t " +
					 "INNER JOIN status_mst sm ON t.status = sm.id " +
					 "INNER JOIN users u ON t.staff = u.id " +
					 "WHERE t.created_by = ? " +
					 "ORDER BY t.id ASC";
		
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(sql)){
			pstmt.setString(1, Login.loggedInUser);
			try (ResultSet rs = pstmt.executeQuery()) {
				
					// dateフォーマット変換用
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
					System.out.println("＝全タスク表示＝");
					while (rs.next()) {
						int id = rs.getInt("id");
						String status = rs.getString("status");
						String title = rs.getString("title");
						String staff = rs.getString("name");
						String remarks = rs.getString("remarks");
						java.sql.Date createdAt = rs.getDate("created_at");
						String formattedDate = createdAt.toLocalDate().format(formatter);
						System.out.println(id + " [" + status + "] " + title + " 担当者 : " + staff + " 備考 : " + remarks + " 作成日 : " + formattedDate);
					}
				}
			} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		// Scannerのインスタンス
		Scanner scanner = new Scanner(System.in);
		int deleteNum = 0;
		while (true) {
			try {
				System.out.print("削除するタスクの番号を入力してください : ");
				deleteNum = scanner.nextInt();
				scanner.nextLine();
				if (!Validation.checkCreated(deleteNum)) {
					System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
					continue;
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println(ConsoleColor.toRed("数値を入力してください"));
				scanner.nextLine();
			}
				
		}
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		
		// 削除を行う前にタイトルの取得を行う
		String selectSql = "SELECT title FROM tasks WHERE id = ?";
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement selectPs = connection.prepareStatement(selectSql)) {
			selectPs.setInt(1, deleteNum);
			try (ResultSet rs = selectPs.executeQuery()) {
				if (rs.next()) {
					deleteTitle = rs.getString("title");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// タスクを削除する
		if(deleteTitle != null) {
			String deleteSql = "DELETE FROM tasks WHERE id = ?";
			try (Connection connection = SqlConnect.sqlConnect();
				 PreparedStatement deletePs = connection.prepareStatement(deleteSql)) {
				deletePs.setInt(1, deleteNum);
				deletePs.executeUpdate();
				System.out.println(ConsoleColor.toBlue("タスク名 : " + deleteTitle + " の削除が完了しました"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		EnterTop.start();
	}
	
	// タスク一覧の表示
	public void editor() throws SQLException {
		String sql = "SELECT t.id, sm.status, t.title, u.name, t.remarks, t.created_at " +
				 "FROM tasks t " +
				 "INNER JOIN status_mst sm ON t.status = sm.id " +
				 "INNER JOIN users u ON t.staff = u.id " +
				 "WHERE t.created_by = ? " +
				 "ORDER BY t.id ASC";
		// タイトル表示
		System.out.println("＝タスク編集＝");
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, Login.loggedInUser);
			ResultSet rs = stmt.executeQuery();
			// dateフォーマット変換用
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			while (rs.next()) {
				int id = rs.getInt("id");
				String status = rs.getString("status");
				String title = rs.getString("title");
				String staff = rs.getString("name");
				String remarks = rs.getString("remarks");
				java.sql.Date createdAt = rs.getDate("created_at");
				// 変換
				String formattedDate = createdAt.toLocalDate().format(formatter);
				// タスク表示
				System.out.println(id + " [" + status + "] " + title + " 担当者 : " + staff + " 備考 : " + remarks + " 作成日 : " + formattedDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// タスク選択
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		Scanner scanner = new Scanner(System.in);
		int editTask = 0;
		while (true) {
			try {
				System.out.print("編集するタスクを入力してください : ");
				editTask = scanner.nextInt();
				scanner.nextLine();
				if (!Validation.checkCreated(editTask)) {
					System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
					continue;
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println(ConsoleColor.toRed("数値を入力してください"));
				scanner.nextLine();
			}
		}
			
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		
		// 編集前のタスクを取得
		String beforeTitle = null;
		java.sql.Date beforeLimit = null;
		String beforeDepartment = null;
		String beforeStaff = null;
		String beforeStatus = null;
		String beforeRemarks = null;
		String beforeCreatedBy = null;
		String selectSql = "SELECT t.title, t.\"limit\", dm.department, u.name, sm.status, t.remarks, t.created_by " +
						   "FROM tasks t " +
						   "INNER JOIN department_mst dm ON t.department = dm.id " +
						   "INNER JOIN users u ON t.staff = u.id " +
						   "INNER JOIN status_mst sm ON t.status = sm.id " +
						   "WHERE t.id = ?";
		
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(selectSql)) {
			pstmt.setInt(1, editTask);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				beforeTitle = rs.getString("title");
				beforeLimit = rs.getDate("limit");
				beforeDepartment = rs.getString("department");
				beforeStaff = rs.getString("name");
				beforeStatus = rs.getString("status");
				beforeRemarks = rs.getString("remarks");
				beforeCreatedBy = rs.getString("created_by");
			} else {
				System.out.println("指定されたタスクが見つかりませんでした");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		// 時間フォーマット変換
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String formattedBLimit = beforeLimit.toLocalDate().format(formatter);
		
		
		// 変更前のタスクを表示
		System.out.println(" タスク情報");
		System.out.println("0 タスク名   " + beforeTitle);
		System.out.println("1 期限   " + formattedBLimit);
		System.out.println("2 担当部署   " + beforeDepartment);
		System.out.println("3 担当者   " + beforeStaff);
		System.out.println("4 ステータス   " + beforeStatus);
		System.out.println("5 備考   " + beforeRemarks);
		System.out.println("6 作成者   " + beforeCreatedBy);
		int fieldNumber = 0;
		while (true) {
			System.out.print("編集する情報の番号を入力してください : ");
			try {
				fieldNumber = scanner.nextInt();
				scanner.nextLine();
				if (fieldNumber > 6 || fieldNumber < 0) {
					System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
					continue;
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println(ConsoleColor.toRed("数値を入力してください"));
				scanner.nextLine();
			}
		}
		
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		switch (fieldNumber) {
		case 0 :
			String newTitle = null;
			while (true) {
				System.out.print("編集後の内容を入力してください : " + beforeTitle + " → ");
				newTitle = scanner.nextLine();
				if (Validation.nullCheck(newTitle)) {
					System.out.println(ConsoleColor.toRed("タスク名は必須です。"));
					continue;
				} else if (!Validation.isLengthInRange(newTitle, 0, 30)) {
					System.out.println(ConsoleColor.toRed("タスク名は30文字以内で入力してください。"));
					continue;
				}
				break;
			}
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			String updateSql = "UPDATE tasks SET title = ?, updated_at = CURRENT_TIMESTAMP, updated_by = ? WHERE id = ?";
			try (Connection connection = SqlConnect.sqlConnect();
				 PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
				pstmt.setString(1, newTitle);
				pstmt.setString(2, Login.loggedInUser);
				pstmt.setInt(3, editTask);
				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					System.out.println(ConsoleColor.toBlue("編集が完了しました"));
				} else {
					System.out.println("失敗");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			EnterTop.start();
			break;
		case 1 :
			String newLimit = null;
			java.sql.Date formattedNewLimit = null;
			while (true) {
				System.out.print("編集後の内容を入力してください : " + formattedBLimit + " → ");
				newLimit = scanner.nextLine().trim();
				if (Validation.nullCheck(newLimit)) {
					System.out.println(ConsoleColor.toRed("期限は必須項目です。"));
					continue;
				}
				formattedNewLimit = TimeFormat.formatter(newLimit);
				if (formattedNewLimit == null) {
					continue;
				}
				break;
			}
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			
			// update
			updateSql = "UPDATE tasks SET \"limit\" = ?, updated_at = CURRENT_TIMESTAMP, updated_by = ? WHERE id = ?";
			try {
				Connection connection = SqlConnect.sqlConnect();
				PreparedStatement pstmt = connection.prepareStatement(updateSql);
				pstmt.setDate(1, formattedNewLimit);
				pstmt.setString(2, Login.loggedInUser);
				pstmt.setInt(3, editTask);
				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					System.out.println(ConsoleColor.toBlue("編集が完了しました"));
				} else {
					System.out.println("失敗");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			EnterTop.start();
			break;
		case 2 :
			SelectSql.displayDepartment();
			int newDepartmentNum;
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			while (true) {
				System.out.print("編集後の部署番号を選択してください : " + beforeDepartment + " → ");
				try {
					newDepartmentNum = scanner.nextInt();
					scanner.nextLine();
					if (!Validation.departmentDataNumCheck(newDepartmentNum)) {
						System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
						continue;
					}
					break;
				} catch (InputMismatchException e) {
					System.out.println(ConsoleColor.toRed("数値を入力してください。"));
					scanner.nextLine();
				}
			}
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			// update
			updateSql = "UPDATE tasks SET department = ?, updated_at = CURRENT_TIMESTAMP, updated_by = ? WHERE id = ?";
			try {
				Connection connection = SqlConnect.sqlConnect();
				PreparedStatement pstmt = connection.prepareStatement(updateSql);
				pstmt.setInt(1, newDepartmentNum);
				pstmt.setString(2, Login.loggedInUser);
				pstmt.setInt(3, editTask);
				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					System.out.println(ConsoleColor.toBlue("編集が完了しました"));
				} else {
					System.out.println("失敗");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			EnterTop.start();
			break;
			
		case 3 : 
			SelectSql.displayStaff();
			int newStaff;
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			while (true) {
				System.out.print("担当者を選択してください。 " + beforeStaff + " → ");
				try {
					newStaff = scanner.nextInt();
					scanner.nextLine();
					if (!Validation.usersDataNumCheck(newStaff)) {
						System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
						continue;
					}
					break;
				} catch (InputMismatchException e) {
					System.out.println(ConsoleColor.toRed("数値を入力してください。"));
					scanner.nextLine();
				}
				
			}
			
			// 登録
			updateSql = "UPDATE tasks SET staff = ?, updated_at = CURRENT_TIMESTAMP, updated_by = ? WHERE id = ?";
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			try {
				Connection connection = SqlConnect.sqlConnect();
				PreparedStatement pstmt = connection.prepareStatement(updateSql);
				pstmt.setInt(1, newStaff);
				pstmt.setString(2, Login.loggedInUser);
				pstmt.setInt(3, editTask);
				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					System.out.println(ConsoleColor.toBlue("編集が完了しました。"));
				} else {
					System.out.println("失敗");
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			EnterTop.start();
			break;
		case 4 :
			SelectSql.displayStatus();
			int newStatusNum;
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			while (true) {
				System.out.print("編集後のステータス番号を選択してください。 " + beforeStatus + " → ");
				try {
					newStatusNum = scanner.nextInt();
					scanner.nextLine();
					if (!Validation.statusDataNumCheck(newStatusNum)) {
						System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
						continue;
					}
					break;
				} catch (InputMismatchException e) {
					System.out.println(ConsoleColor.toRed("数値を入力してください。"));
					scanner.nextLine();
				}
			}
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			// update
			updateSql = "UPDATE tasks SET status = ?, updated_at = CURRENT_TIMESTAMP, updated_by = ? WHERE id = ?";
			try {
				Connection connection = SqlConnect.sqlConnect();
				PreparedStatement pstmt = connection.prepareStatement(updateSql);
				pstmt.setInt(1, newStatusNum);
				pstmt.setString(2, Login.loggedInUser);
				pstmt.setInt(3, editTask);
				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					System.out.println(ConsoleColor.toBlue("編集が完了しました。"));
				} else {
					System.out.println("失敗");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			EnterTop.start();
			break;
			
		case 5 :
			String newRemarks = null;
			while (true) {
				System.out.print("編集後の内容を入力してください : " + beforeRemarks + " → ");
				newRemarks = scanner.nextLine();
				if (!Validation.isLengthInRange(newRemarks, 0, 50)) {
					System.out.println(ConsoleColor.toRed("備考は50文字以内で入力してください。"));
					continue;
				}
				break;
			}
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			// update
			updateSql = "UPDATE tasks SET remarks = ?, updated_at = CURRENT_TIMESTAMP, updated_by = ? WHERE id = ?";
			try {
				Connection connection = SqlConnect.sqlConnect();
				PreparedStatement pstmt = connection.prepareStatement(updateSql);
				pstmt.setString(1, newRemarks);
				pstmt.setString(2, Login.loggedInUser);
				pstmt.setInt(3, editTask);
				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					System.out.println(ConsoleColor.toBlue("編集が完了しました。"));
				} else {
					System.out.println("失敗");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			EnterTop.start();
			break;
			
		case 6 :
			SelectSql.displayStaff();
			int newCreatedBy;
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			while (true) {
				System.out.print("変更後の担当idを入力してください : " + beforeCreatedBy + " → ");
				try {
					newCreatedBy = scanner.nextInt();
					scanner.nextLine();
					if (!Validation.usersDataNumCheck(newCreatedBy)) {
						System.out.println(ConsoleColor.toRed("存在する番号を入力してください。"));
						continue;
					}
					break;
				} catch (InputMismatchException e) {
					System.out.println(ConsoleColor.toRed("数値を入力してください。"));
					scanner.nextLine();
				}
			}
			System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
			// update
			updateSql = "UPDATE tasks SET created_by = ?, updated_at = CURRENT_TIMESTAMP, created_by = ? WHERE id = ?";
			try {
				Connection connection = SqlConnect.sqlConnect();
				PreparedStatement pstmt = connection.prepareStatement(updateSql);
				pstmt.setInt(1, newCreatedBy);
				pstmt.setString(2, Login.loggedInUser);
				pstmt.setInt(3, editTask);
				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					System.out.println(ConsoleColor.toBlue("編集が完了しました。"));
				} else {
					System.out.println("失敗");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			EnterTop.start();
			break;
		default :
			System.out.println(ConsoleColor.toRed("存在する番号を入力してください"));
			
		}
	}
}
