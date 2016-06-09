package fr.pinnackl.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Config_example {
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/{DBNAME}", "{USER}", "{PASSWORD}");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}
}
