package model;

import java.sql.SQLException;
import java.util.Scanner;

public class TopPage {
	
	public void start() throws SQLException{
		// Taskviewクラスのインスタンス生成。
		TaskView tv = new TaskView();
		// TaskEditクラスのインスタンス生成
		TaskEdit te = new TaskEdit();
		// 初期表示
		System.out.println("＝TOP＝");
		System.out.println("0 タスク登録");
		System.out.println("1 全タスク表示");
		System.out.println("2 未完了タスク表示");
		System.out.println("3 期限の近いタスク表示");
		System.out.println("4 完了済みタスク表示");
		System.out.println("5 完了済みに変更");
		System.out.println("6 タスク編集");
		System.out.println("7 タスク削除");
		System.out.println("8 ログアウト");
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		System.out.print("実行する番号を入力してください : ");
		// scanner
		Scanner scanner = new Scanner(System.in);
		int execution = scanner.nextInt();
		
		// 条件分岐
		switch (execution) {
		case 0:
			TaskRegistration taskRegistration = new TaskRegistration();
			taskRegistration.start();
			break;
		case 1:
			tv.all();
			break;
		case 2:
			tv.incomplete();
			break;
		case 3:
			tv.near();
			break;
		case 4:
			tv.complete();
			break;
		case 5:
			te.changeToCompleted();
			break;
		case 6:
			te.editor();
			break;
		case 7:
			te.delete();
			break;
		case 8:
			Login login = new Login();
			login.out();
		}
	}
}
