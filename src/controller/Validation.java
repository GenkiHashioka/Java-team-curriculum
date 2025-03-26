package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Login;
import sqlconnect.SqlConnect;

public class Validation {
	// 入力必須用のバリデーション
	public static boolean nullCheck(String str) {
			return str == null || str.trim().isEmpty();
		}
	// 文字数範囲判定用のメソッド
	public static boolean isLengthInRange(String str, int min, int max) {
			int length  = str.length();
			return length >= min && length <= max;
		}
	// status_mstのidがdb上に存在するか？？
	public static boolean statusDataNumCheck(int i) {
		String sql = "SELECT * FROM status_mst WHERE id = ?";
		Boolean b = false;
		try {
			Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, i);
			ResultSet rs = pstmt.executeQuery();
			
				if (rs.next()) {
					b = true;
				} else {
					b = false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return b;
		}
	
	// usersのidがdb上に存在するか？
	public static boolean usersDataNumCheck(int i) {
		String sql = "SELECT * FROM users WHERE id = ?";
		Boolean b = false;
		try {
			Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, i);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				b = true;
			} else {
				b = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	// department_mstのidがdb上に存在するか？
	public static boolean departmentDataNumCheck(int departmentNum) {
		String sql = "SELECT * FROM department_mst WHERE id = ?";
		boolean b = false;
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setInt(1, departmentNum);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					b = true;
				} else {
					b = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	// tasksのid存在する？？ (ログインユーザーが作成したものまたは担当者がログインユーザー）
	public static boolean checkCreatedAndInCharge(int taskNum) {
		String sql = "SELECT * FROM tasks t INNER JOIN users u ON t.staff = u.id WHERE t.id = ? AND (u.name= ? OR t.created_by = ?)";
		boolean b = false;
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setInt(1, taskNum);
			pstmt.setString(2, Login.loggedInUser);
			pstmt.setString(3, Login.loggedInUser);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					b = true;
				} else {
					b = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	// tasksのid存在する？？ （ログインユーザーが作成したもの）
	public static boolean checkCreated(int taskNum) {
		String sql = "SELECT * FROM tasks WHERE id = ? AND created_by = ?";
		boolean b = false;
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setInt(1, taskNum);
			pstmt.setString(2, Login.loggedInUser);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					b = true;
				} else {
					b = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
	}
}
