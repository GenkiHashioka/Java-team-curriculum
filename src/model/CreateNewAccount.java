package model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import controller.EnterTop;
import controller.PasswordHash;
import sqlconnect.SqlConnect;
import view.ConsoleColor;

public class CreateNewAccount {
	// メールアドレスの正規表現
	private static final String EMAIL_REGEX_STRICT = 
			"^(?=.{1,64}@.{1,255}$)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	String username;
	String password;
	String passwordCheck;
	String mail;
	int department;
	public void start() throws SQLException {
		Connection connection = SqlConnect.sqlConnect();
		System.out.println("＝新規会員登録＝");
		username();
		password();
		checkPassword();
		mail();
		department();
		sqlInsert();
	}
	
	public void username() throws SQLException {
		CreateNewAccount cna = new CreateNewAccount();
		System.out.print("ユーザー名を入力してください : ");
		Scanner scanner = new Scanner(System.in);
		this.username = scanner.nextLine();
		// エラーチェック
		if (this.username == null || this.username.trim().isEmpty()) {
			System.out.println(ConsoleColor.toRed("名前は必須項目です。"));
			cna.start();
		} else if (this.username.length() >= 10) {
			System.out.println(ConsoleColor.toRed("名前は10文字以内で入力してください。"));
			cna.start();		
		}
	}
	// 全角半角判定用のメソッド
	public static boolean isHalfWidth(String str) {
		return str.matches("^[\\p{Alnum}\\p{Punct}]+$");
	}
	
	// 大文字小文字数字の組み合わせ判定
	public static boolean caseCheck(String str) {
		boolean hasUpperCase = false;
		boolean hasLowerCase = false;
		boolean hasDigit = false;
		
		for (char c : str.toCharArray()) {
			if (Character.isUpperCase(c)) {
				hasUpperCase = true;
			} else if (Character.isLowerCase(c)) {
				hasLowerCase = true;
			} else if (Character.isDigit(c)) {
				hasDigit = true;
			}
		}
		return hasUpperCase && hasLowerCase && hasDigit;
	}
	
	// 特殊文字判定用メソッド
	public static boolean hasSpecialChar(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isLetterOrDigit(c)) {
				return true;
			}
		}
		return false;
	}
	
	public void password() throws SQLException {
		CreateNewAccount cna = new CreateNewAccount();
		JPasswordField pwField = new JPasswordField(20);
		int option = JOptionPane.showConfirmDialog(null, pwField, "パスワードを入力してください", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			char[] password = pwField.getPassword();
			this.password = new String(password);
		} else { 
			System.out.println(ConsoleColor.toRed("キャンセルされました。"));
			cna.start();
		}
		if (this.password == null || this.password.trim().isEmpty()) {
			System.out.println(ConsoleColor.toRed("パスワードは必須項目です。"));
			password();
		} else if (isHalfWidth(this.password) == false) {
				System.out.println(ConsoleColor.toRed("パスワードは半角英数字のみで入力してください。"));
				password();
			} else if (this.password.length() > 10 || this.password.length() < 3) {
				System.out.println(ConsoleColor.toRed("パスワードは10文字以内、3文字以上で入力してください。"));
				password();
			} else if (caseCheck(this.password) == false) {
				System.out.println(ConsoleColor.toRed("パスワードは大文字小文字数字を組み合わせて入力してください。"));
				password();
			} else if (hasSpecialChar(this.password)) {
				System.out.println(ConsoleColor.toRed("パスワードは特殊記号を使用しないでください。"));
				password();
			}
	}
	
	public void checkPassword() throws SQLException {
		CreateNewAccount cna = new CreateNewAccount();
		JPasswordField pwField = new JPasswordField(20);
		int option = JOptionPane.showConfirmDialog(null, pwField, "確認用パスワードを入力してください", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			char[] password = pwField.getPassword();
			this.passwordCheck = new String(password);
		} else {
			System.out.println(ConsoleColor.toRed("キャンセルされました"));
			cna.start();
		}
		if (this.passwordCheck == null || this.passwordCheck.trim().isEmpty()) {
			System.out.println(ConsoleColor.toRed("パスワード確認は必須項目です。"));
			checkPassword();
		} else if (isHalfWidth(this.passwordCheck) == false) {
			System.out.println(ConsoleColor.toRed("パスワード確認は半角英数字のみで入力してください。"));
			checkPassword();
		} else if (this.passwordCheck.length() > 10 || this.passwordCheck.length() < 3) {
			System.out.println(ConsoleColor.toRed("パスワード確認は10文字以内、3文字以上で入力してください。"));
			checkPassword();
		} else if (caseCheck(this.passwordCheck) == false) {
			System.out.println(ConsoleColor.toRed("パスワード確認は大文字小文字数字を組み合わせて入力してください。"));
			checkPassword();
		} else if (hasSpecialChar(this.passwordCheck)) {
			System.out.println(ConsoleColor.toRed("パスワード確認は特殊記号を使用しないでください。"));
			checkPassword();
		} else if (this.password.equals(passwordCheck) == false) {
			System.out.println(ConsoleColor.toRed("パスワードの入力と差異があります。"));
			checkPassword();
		}
	}
	
	// メールアドレス形式チェック
	public static boolean adressChecker(String email) {
			return Pattern.matches(EMAIL_REGEX_STRICT, email);
	}
	
	// 重複チェック
	public static boolean isEmailRegistered(String email) {
		String sql = "SELECT COUNT (*) FROM users WHERE email = ?";
		try {
			Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void mail() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("メールアドレスを入力してください : ");
		this.mail = scanner.nextLine();
		if (this.mail == null || this.mail.trim().isEmpty()) {
			System.out.println(ConsoleColor.toRed("メールアドレスは必須項目です。"));
			mail();
		} else if (adressChecker(this.mail) == false) {
			System.out.println(ConsoleColor.toRed("正しいメールアドレス形式で入力してください。"));
			mail();
		} else if (isHalfWidth(this.mail) == false) {
			System.out.println(ConsoleColor.toRed("メールアドレスは半角英数字のみで入力してください。"));
			mail();
		} else if (isEmailRegistered(this.mail)) {
			System.out.println(ConsoleColor.toRed("このメールアドレスは登録済みです。"));
			mail();
		}
	}
	
	public void department() {
		Scanner scanner = new Scanner(System.in);
		// 実行SQL
		String sql = "SELECT * FROM department_mst";
		try {
			Connection connection = SqlConnect.sqlConnect();
			// ステートメント
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet res = pstmt.executeQuery();
			// 出力
			while (res.next()) {
				System.out.print(res.getInt("id") + " ");
				System.out.println(res.getString("department"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		System.out.print("所属部署の番号を入力してください : ");
		this.department = scanner.nextInt();
//		改行の打消
		scanner.nextLine();
		sql = "SELECT * FROM status_mst WHERE id = ?";
		try {
			Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, department);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
			} else {
				System.out.println(ConsoleColor.toRed("一致するデータがありません。再度入力してください。"));
				department();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
		
		// SQL登録
	public void sqlInsert() throws SQLException {
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		// PWのハッシュ化
		String hashedPassword = PasswordHash.hashPassword(password);
		String sql = "INSERT INTO users (name, pass, email, department) VALUES (?, ?, ?, ?)";
		try {
			Connection connection = SqlConnect.sqlConnect();
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, this.username);
			pstmt.setString(2, hashedPassword);
			pstmt.setString(3, this.mail);
			pstmt.setInt(4, this.department);
			int rows = pstmt.executeUpdate();
			if (rows > 0) {
				System.out.println(ConsoleColor.toBlue("ユーザー名 : " + this.username));
				System.out.println(ConsoleColor.toBlue("メールアドレス : " + this.mail));
				System.out.println(ConsoleColor.toBlue("上記内容で登録が完了しました"));
			} else {
				System.out.println(ConsoleColor.toRed("登録に失敗しました。"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		Login.loggedInUser = this.username;
		EnterTop.start();
	}
}
