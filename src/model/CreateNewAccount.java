package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import sqlconnect.SqlConnect;

public class CreateNewAccount {
	String username;
	String password;
	String password_check;
	String mail;
	int department;
	public void start() throws SQLException {
		Connection connection = SqlConnect.sqlConnect();
		Scanner scanner = new Scanner(System.in);
		System.out.println("＝新規会員登録＝");
		System.out.print("ユーザー名を入力してください : ");
		this.username = scanner.nextLine();
		System.out.print("パスワードを入力してください : ");
		this.password = scanner.nextLine();
		System.out.print("確認用パスワードを入力してください : ");
		this.password_check = scanner.nextLine();
		System.out.print("メールアドレスを入力してください : ");
		this.mail = scanner.nextLine();
		// ステートメント
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM department_mst");
		// 実行SQL
		ResultSet res = pstmt.executeQuery();
		// 出力
		while (res.next()) {
			System.out.print(res.getInt("id") + " ");
			System.out.println(res.getString("department"));
		}
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		System.out.print("所属部署の番号を入力してください : ");
		this.department = scanner.nextInt();
		// SQL登録
		String sql = "INSERT INTO users (name, pass, email, department) VALUES" + "('" + this.username + "','" + this.password + "','" + this.mail + "','" + this.department + "') ON CONFLICT (email) DO NOTHING";
		int res1;
		try {
			// ステートメント
			pstmt = connection.prepareStatement(sql);
			// 実行
			res1 = pstmt.executeUpdate();
			if (res1 == 0) {
				System.out.println("このメールアドレスは既に登録されています。");
				System.out.println("再度登録してください。");
				System.out.println(); // 改行
				start(); // 最初に戻る。
			} else {
				System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
				System.out.println("ユーザー名 : " + this.username);
				System.out.println("メールアドレス : " + this.mail);
				System.out.println("上記内容で登録が完了しました");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// enter押下でメインメニューに戻る
		try {
			int i = System.in.read();
		} catch(Exception e) {
			System.out.println("入力エラーです");
			e.printStackTrace();
		}
		MainMenu mm = new MainMenu();
		mm.start(); // メインメニューの開始
	}
}
