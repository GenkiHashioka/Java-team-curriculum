package model;

import java.sql.SQLException;
import java.util.Scanner;

import view.ConsoleColor;

public class MainMenu {
	// 新規登録のインスタンス
	CreateNewAccount CNA = new CreateNewAccount();
	// loginのインスタンス
	Login login = new Login();
	public void start() throws SQLException{
		// 最初に表示する文字列
		System.out.println("＝メニュー＝");
		System.out.println("0 新規登録");
		System.out.println("1 ログイン");
		System.out.println("2 終了");
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		System.out.print("実行する番号を入力してください : ");
		int input;
		Scanner scanner = new Scanner(System.in);
		input = scanner.nextInt();
		// 新規登録 ログイン 終了の条件分岐
		switch (input) {
		case 0 :
			CNA.start();
			break;
		
		case 1 :
			login.start();
			break;
			
		case 2 :
			System.out.println(ConsoleColor.toBlue("プログラムを終了します"));
			System.exit(0);
		}
	}
}
