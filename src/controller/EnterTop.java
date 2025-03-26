package controller;

import java.sql.SQLException;

import model.TopPage;

public class EnterTop {
	public static void start() throws SQLException {
		try {
			int i = System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		TopPage.start();
	}
}
