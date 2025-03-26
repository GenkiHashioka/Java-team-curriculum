package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import controller.EnterTop;
import sqlconnect.SqlConnect;

public class TaskView {
	// 全部表示
	public void all() throws SQLException{
		// sql
		String sql = "SELECT t.id, sm.status, t.title, u.name, t.remarks, t.created_at " +
					 "FROM tasks t " +
					 "INNER JOIN status_mst sm ON t.status = sm.id " +
					 "INNER JOIN users u ON t.staff = u.id " +
					 "ORDER BY t.id ASC";
		
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {
			// dateフォーマット変換
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			
			// タイトル表示
			System.out.println("＝全タスク表示＝");
			while (rs.next()) {
				int id = rs.getInt("id");
				String status = rs.getString("status");
				String title = rs.getString("title");
				String staff = rs.getString("name");
				String remarks = rs.getString("remarks");
				java.sql.Date createdAt = rs.getDate("created_at");
				// yyyy/MM/ddに変換
				String formattedDate = createdAt.toLocalDate().format(formatter);
				// タスク表示
				System.out.println(id + " [" + status + "] " + title + " 担当者 : " + staff + " 備考 : " + remarks + " 作成日 : " + formattedDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// enter押下でtoppage表示.
		EnterTop.start();
	}
	
	
	// 未完了表示
	public void incomplete() throws SQLException {
		// 実行SQL
		String sql = "SELECT t.id, sm.status, t.title, u.name, t.remarks, t.created_at " +
				 "FROM tasks t " +
				 "INNER JOIN status_mst sm ON t.status = sm.id " +
				 "INNER JOIN users u ON t.staff = u.id " +
				 "WHERE sm.status = '未完了' " +
				 "ORDER BY t.id ASC";
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {
			// dateフォーマット変換用
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			// 初期表示
			System.out.println("＝未完了タスク表示＝");
			while (rs.next()) {
				int id = rs.getInt("id");
				String status = rs.getString("status");
				String title = rs.getString("title");
				String staff = rs.getString("name");
				String remarks = rs.getString("remarks");
				java.sql.Date createdAt = rs.getDate("created_at");
				// yyyy/MM/dd
				String formattedDate = createdAt.toLocalDate().format(formatter);
				// タスク表示
				System.out.println(id + " [" + status + "] " + title + " 担当者 : " + staff + " 備考 : " + remarks + " 作成日 : " + formattedDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// enter押下でtoppage表示
		EnterTop.start();
	}
	
	// 期限の近いタスクを表示
	public void near() throws SQLException {
		// 実行SQL
		String sql = "SELECT t.id, sm.status, t.title, u.name, t.remarks, t.created_at " +
				 "FROM tasks t " +
				 "INNER JOIN status_mst sm ON t.status = sm.id " +
				 "INNER JOIN users u ON t.staff = u.id " +
				 "WHERE t.limit BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '14 days' " +
				 "ORDER BY t.id ASC";
		try (Connection connection = SqlConnect.sqlConnect();
				 PreparedStatement pstmt = connection.prepareStatement(sql);
				 ResultSet rs = pstmt.executeQuery()) {
				// dateフォーマット変換用
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				// 初期表示
				System.out.println("＝期限の近いタスク表示＝");
				while (rs.next()) {
					int id = rs.getInt("id");
					String status = rs.getString("status");
					String title = rs.getString("title");
					String staff = rs.getString("name");
					String remarks = rs.getString("remarks");
					java.sql.Date createdAt = rs.getDate("created_at");
					// yyyy/MM/dd
					String formattedDate = createdAt.toLocalDate().format(formatter);
					// タスク表示
					System.out.println(id + " [" + status + "] " + title + " 担当者 : " + staff + " 備考 : " + remarks + " 作成日 : " + formattedDate);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// enter押下でtoppage表示
			EnterTop.start();
	}
	
	// 完了済みのタスクを表示
	public void complete() throws SQLException {
		// 実行SQL
		String sql = "SELECT t.id, sm.status, t.title, u.name, t.remarks, t.created_at " +
				 "FROM tasks t " +
				 "INNER JOIN status_mst sm ON t.status = sm.id " +
				 "INNER JOIN users u ON t.staff = u.id " +
				 "WHERE sm.status = '完了' " +
				 "ORDER BY t.id ASC";
		
		try (Connection connection = SqlConnect.sqlConnect();
				 PreparedStatement pstmt = connection.prepareStatement(sql);
				 ResultSet rs = pstmt.executeQuery()) {
				// dateフォーマット変換用
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				// 初期表示
				System.out.println("＝完了済みタスク表示＝");
				while (rs.next()) {
					int id = rs.getInt("id");
					String status = rs.getString("status");
					String title = rs.getString("title");
					String staff = rs.getString("name");
					String remarks = rs.getString("remarks");
					java.sql.Date createdAt = rs.getDate("created_at");
					// yyyy/MM/dd
					String formattedDate = createdAt.toLocalDate().format(formatter);
					// タスク表示
					System.out.println(id + " [" + status + "] " + title + " 担当者 : " + staff + " 備考 : " + remarks + " 作成日 : " + formattedDate);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// enter押下でtoppage表示
			EnterTop.start();
	}
}
