package generic.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourcePostgress {

	/**
	 * retorna uma Connection lendo do pool do tomcat
	 * @return
	 */
	public static Connection getConnection() {

		try {
			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection(
					"jdbc:postgresql://localhost/home", "postgres", "postgres");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

}
