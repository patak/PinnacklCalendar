package fr.pinnackl.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.pinnackl.beans.Share;

public class Shares {

	private Connection connection;

	private void loadDatabase() {

		// Load driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
		}

		// DB Connection
		connection = new Config().getConnection();
	}

	public void createShare(Share share) {

		PreparedStatement preparedStatement = null;

		loadDatabase();

		try {
			preparedStatement = connection
					.prepareStatement("INSERT INTO shared(owner_id, user_id, event_id) VALUES(?, ?, ?);");
			preparedStatement.setInt(1, share.getOwner().getId());
			preparedStatement.setInt(2, share.getUser().getId());
			preparedStatement.setInt(3, share.getEvent().getID());

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Close Connection
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();

			} catch (SQLException e2) {
				// TODO: handle exception
			}
		}
	}

}
