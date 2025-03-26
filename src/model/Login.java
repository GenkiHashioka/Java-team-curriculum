package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import controller.EnterTop;
import controller.PasswordHash;
import sqlconnect.SqlConnect;
import view.ConsoleColor;

public class Login {
		public static String loggedInUser = null;
		public static String email = null;
		public static String pass = null;
	public void start() throws SQLException {
		System.out.println("＝ログイン＝");
		mail();
		password();
		login();
	}
	
	public void mail() {
		System.out.print("メールアドレスを入力してください : ");
		Scanner scanner = new Scanner(System.in);
		Login.email = scanner.nextLine();
		if (Login.email == null || Login.email.trim().isEmpty()) {
			System.out.println(ConsoleColor.toRed("メールアドレスは必須項目です。"));
			mail();
		} else if (CreateNewAccount.adressChecker(Login.email) == false) {
			System.out.println(ConsoleColor.toRed("正しいメールアドレス形式で入力してください。"));
			mail();
		} else if (CreateNewAccount.isHalfWidth(Login.email) == false) {
			System.out.println(ConsoleColor.toRed("メールアドレスは半角英数字のみで入力してください。"));
			mail();
		}
	}
	public void password() throws SQLException {
		JPasswordField pwField = new JPasswordField(20);
		int option = JOptionPane.showConfirmDialog(null, pwField, "パスワードを入力してください", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			char[] pass = pwField.getPassword();
			Login.pass = new String(pass);
		} else {
			System.out.println(ConsoleColor.toRed("キャンセルされました。"));
			TopPage.start();
		}
		if (Login.pass == null || Login.pass.trim().isEmpty()) {
			System.out.println(ConsoleColor.toRed("パスワードは必須項目です。"));
			password();
		} else if (CreateNewAccount.isHalfWidth(Login.pass) == false) {
			System.out.println(ConsoleColor.toRed("パスワードは半角英数字のみで入力してください。"));
			password();
		} else if (Login.pass.length() > 10 || Login.pass.length() < 3) {
			System.out.println(ConsoleColor.toRed("パスワードは10文字以内、3文字以上で入力してください。"));
			password();
		} else if (CreateNewAccount.caseCheck(Login.pass) == false) {
			System.out.println(ConsoleColor.toRed("パスワードは大文字小文字数字を組み合わせて入力してください。"));
			password();
		} else if (CreateNewAccount.hasSpecialChar(Login.pass)) {
			System.out.println(ConsoleColor.toRed("パスワードは特殊記号を使用しないでください。"));
			password();
		}
	}
	
	public void login() throws SQLException {
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
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
		if (authenticate(Login.email, Login.pass)) {
			System.out.println(ConsoleColor.toBlue(name + "さん、ログインが完了しました"));
			loggedInUser = name;
			EnterTop.start();
			} else {
				System.out.println(ConsoleColor.toRed("一致するデータがありません。再度入力してください。"));
				start();
			}
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
				String inputHash = PasswordHash.hashPassword(pass);
				return sqlPass.equals(inputHash);
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
