package view;

public class ConsoleColor {
	public static String toRed(String text) {
		return "\u001B[31m" + text + "\u001B[0m"; // ANSIエスケープシーケンス
	}
	
	public static String toBlue(String text) {
		return "\u001B[36m" + text + "\u001B[0m";
	}
}
