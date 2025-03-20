package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import sqlconnect.SqlConnect;

public class Login {
		public static String loggedInUser = null;
	public void start() throws SQLException {
		// Scanner
		Scanner scanner = new Scanner(System.in);
		System.out.println("＝ログイン＝");
		System.out.print("メールアドレスを入力してください : ");
		String email = scanner.nextLine();
		System.out.print("パスワードを入力してください : ");
		String pass = scanner.nextLine();
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		// ユーザー名の呼び出し。
		String sql = "SELECT name FROM users WHERE email = ?";
		String name = null;
		try(Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				name = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		if (authenticate(email, pass)) {
			System.out.println(name + "さん、ログインが完了しました");
			loggedInUser = name;
		} else {
			System.out.println("ログイン失敗");
			start();
		}
		// トップページのインスタンス
		TopPage toppage = new TopPage();
		try {
			int i = System.in.read();
		} catch (Exception e) {
			System.out.println("入力エラーです");
			e.printStackTrace();
		}
		toppage.start();
		}
	
	// パスワードの入力チェック。
	private static boolean authenticate(String email, String pass) throws SQLException {
		String sql = "SELECT pass FROM users WHERE email = ?";
		
		try (Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				String sqlPass = rs.getString("pass");
				return pass.equals(sqlPass);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	// ログアウト機能
	public void out() throws SQLException {
		TopPage top = new TopPage();
		MainMenu mm = new MainMenu();
		System.out.println("＝ログアウト＝");
		System.out.print("ログアウトします、よろしいですか？(y/n) : ");
		Scanner scanner = new Scanner(System.in);
		String choice = scanner.nextLine();
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		if (choice.equals("y")) {
			this.loggedInUser = null;
			mm.start();
		} else {
			top.start();
		}
	}
}
