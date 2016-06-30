package fr.pinnackl.bdd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.pinnackl.beans.Event;

public class Events {

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

	public void createEvent(Event event) {

		PreparedStatement preparedStatement = null;

		loadDatabase();

		try {
			preparedStatement = connection.prepareStatement(
					"INSERT INTO events(name, description, place, latitude, longitude, startDate, finishDate, photo, organizer_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);");
			preparedStatement.setString(1, event.getName());
			preparedStatement.setString(2, event.getDescription());
			preparedStatement.setString(3, event.getPlace());
			try {
				preparedStatement.setDouble(4, event.getLatitude());
				preparedStatement.setDouble(5, event.getLongitude());
			} catch (Exception e) {
				preparedStatement.setDouble(4, 0);
				preparedStatement.setDouble(5, 0);
			}
			preparedStatement.setDate(6, new Date(event.getStartDate().getTime()));
			try {
				preparedStatement.setDate(7, new Date(event.getFinishDate().getTime()));
			} catch (Exception e) {
				preparedStatement.setDate(7, null);
			}
			preparedStatement.setBlob(8, event.getPhoto());
			preparedStatement.setInt(9, event.getOrganizer().getId());

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