package controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import view.ConsoleColor;

public class TimeFormat {
	// StringをSql.Dateに変換するメソッド
	public static java.sql.Date formatter(String str) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		Date sqlDate = null;
		try {
			LocalDate localDate = LocalDate.parse(str, formatter);
			// 過去日付チェック
			if (localDate.isBefore(LocalDate.now())) {
				System.out.println(ConsoleColor.toRed("過去の日付は入力できません。本日日付を入力してください。"));
				return null;
			}
			return sqlDate = Date.valueOf(localDate);
		} catch (DateTimeParseException e) {
			System.out.println(ConsoleColor.toRed("年月日を入力してください。"));
			return null;
		}
	}
}
