package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Scanner;

import sqlconnect.SqlConnect;

public class TaskRegistration {
	public void start() throws SQLException {
		// scannerのインスタンス
		Scanner scanner = new Scanner(System.in);
		
		// 入力された値を受け取る変数。
		String title = null;
		java.sql.Date limit = java.sql.Date.valueOf(LocalDate.now());
		String department = null;
		String staff = null;
		String status = null;
		String remarks = null;
		String created_by = Login.loggedInUser;
		// 初期表示
		System.out.println("＝タスク登録＝");
		System.out.print("タスク名を入力してください : ");
		title = scanner.nextLine();
		System.out.print("期限を入力してください (例: 2025/03/01) : ");
		String inputLimit = scanner.nextLine();
		// sql.Dateに変換
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			java.util.Date utilDate = sdf.parse(inputLimit);
			limit = new Date(utilDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// 改行
		System.out.println();
		
		// SQLf
		String sql = "SELECT * FROM department_mst";
		// 接続
		Connection connection = SqlConnect.sqlConnect();
		// ステートメント
		PreparedStatement pstmt = connection.prepareStatement(sql);
		// 実行
		ResultSet rs = pstmt.executeQuery();
		// コンソール出力
		while (rs.next()) {
			System.out.print(rs.getInt("id") + " ");
			System.out.println(rs.getString("department"));
		}
		System.out.print("担当部署の番号を選択してください : ");
		/*
		 * 担当部署名を変数に取得する。
		 */
		int departmentNum = scanner.nextInt();
		sql = "SELECT * FROM department_mst WHERE id = ?";
		// ステートメント
		pstmt = connection.prepareStatement(sql);
		// 値のセット
		pstmt.setInt(1, departmentNum);
		// 実行
		rs = pstmt.executeQuery();
		while (rs.next()) {
			department = rs.getString("department");
		}
		// 改行
		System.out.println();
		/*
		 * 担当者の登録
		 */
		sql = "SELECT * FROM users";
		// ステートメント
		pstmt = connection.prepareStatement(sql);
		// 実行
		rs = pstmt.executeQuery();
		while (rs.next()) {
			System.out.print(rs.getInt("id") + " ");
			System.out.println(rs.getString("name"));
		}
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		System.out.print("担当者を選択してください : ");
		int staffNum = scanner.nextInt();
		sql = "SELECT * FROM users WHERE id = ?";
		pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, staffNum);
		rs = pstmt.executeQuery();
		while(rs.next()) {
			staff = rs.getString("name");
		}
		// 改行
		System.out.println();
		
		/*
		 * ステータスの登録
		 */
		sql = "SELECT * FROM status_mst";
		pstmt = connection.prepareStatement(sql);
		rs = pstmt.executeQuery();
		while(rs.next()) {
			System.out.print(rs.getInt("id") + " ");
			System.out.println(rs.getString("status"));
		}
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		System.out.print("ステータス番号を入力してください : ");
		int statusNum = scanner.nextInt();
		// 改行の打消
		scanner.nextLine();
		// sqlからカラム名を引っ張る。
		sql = "SELECT * FROM status_mst WHERE id = ?";
		pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, statusNum);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			status = rs.getString("status");
		}
		// 改行
		System.out.println();
		
		/*
		 * 備考
		 */
		System.out.print("備考を入力してください : ");
		remarks = scanner.nextLine();
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		
		/*
		 * タスク登録
		 */
		sql = "INSERT INTO tasks (title, \"limit\", department, staff, status, remarks, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setDate(2, limit);
			pstmt.setInt(3, departmentNum);
			pstmt.setInt(4, staffNum);
			pstmt.setInt(5, statusNum);
			pstmt.setString(6, remarks);
			pstmt.setString(7, created_by);
			
			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("タスク名 : " + title);
				System.out.println("期限 : " + inputLimit);
				System.out.println("担当部署 : " + department);
				System.out.println("担当者 : " + staff);
				System.out.println("ステータス : " + status);
				System.out.println("備考 : " + remarks);
				System.out.println("登録が完了しました");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			int i = System.in.read();
		} catch (Exception e) {
			System.out.println("入力エラー");
			e.printStackTrace();
		}
		TopPage top = new TopPage();
		top.start();
	}
}
