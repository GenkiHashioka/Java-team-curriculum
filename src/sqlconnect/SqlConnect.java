package sqlconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnect {
		private static final String POSTGRES_DRIVER = "org.postgresql.Driver";
		private static final String JDBC_CONNECTION = "jdbc:postgresql://localhost:5432/java_team";
		private static final String USER = "postgres";
		private static final String PASS = "7gREv8xRRQs2pDZfcczT";
		
		
		public static Connection sqlConnect() throws SQLException {
			Connection connection = null;
			try {
				Class.forName(POSTGRES_DRIVER); 
			} catch (ClassNotFoundException e) {
				throw new SQLException("ドライバが見つかりません。");
			}
			connection = DriverManager.getConnection(JDBC_CONNECTION, USER, PASS);
			return connection;
		}
	}
	


