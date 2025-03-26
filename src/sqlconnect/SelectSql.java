package sqlconnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectSql {
	
	// department_mstの中身を表示する
	public static void displayDepartment() {
		String sql = "SELECT * FROM department_mst";
		try {
			Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.print(rs.getInt("id") + " ");
				System.out.println(rs.getString("department"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 担当部署名を取得するメソッド
	public static String getDepartmentName(int departmentNum) {
		String sql = "SELECT department FROM department_mst WHERE id = ?";
		String department = null;
		try {
			Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, departmentNum);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				department = rs.getString("department");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return department;
	}
	
	// 担当者を表示するメソッド
	public static void displayStaff() {
		String sql = "SELECT * FROM users";
		try {
			Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.print(rs.getInt("id") + " ");
				System.out.println(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 担当者名を取得するメソッド
	public static String getStaffName(int staffNum) {
		String sql = "SELECT name FROM users WHERE id = ?";
		String staff = null;
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, staffNum);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					staff = rs.getString("name");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return staff;
	}
	
	// ステータスを表示するメソッド
	public static void displayStatus() {
		String sql = "SELECT * FROM status_mst";
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				System.out.print(rs.getInt("id") + " ");
				System.out.println(rs.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ステータス名を取得するメソッド
	public static String getStatus(int statusNum) {
		String sql = "SELECT status FROM status_mst WHERE id = ?";
		String status = null;
		try (Connection connection = SqlConnect.sqlConnect();
			 PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, statusNum);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					status = rs.getString("status");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}
}
