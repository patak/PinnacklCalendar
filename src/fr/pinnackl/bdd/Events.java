package fr.pinnackl.bdd;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.pinnackl.beans.Event;
import fr.pinnackl.beans.User;

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

	public List<Event> getEvents(Integer userId) {
		List<Event> events = new ArrayList<Event>();

		// DB Connection
		PreparedStatement preparedStatement = null;
		ResultSet result = null;

		loadDatabase();

		try {
			preparedStatement = connection.prepareStatement(
					"SELECT e.*, sh.* FROM events as e LEFT JOIN shared as sh ON (sh.user_id = ?) WHERE e.organizer_id = ? OR e.organizer_id = sh.owner_id;");
			preparedStatement.setLong(1, userId);
			preparedStatement.setLong(2, userId);

			result = preparedStatement.executeQuery();

			// Retrieve Datas
			while (result.next()) {
				Integer id = result.getInt("event_id");
				String name = result.getString("name");
				String description = result.getString("description");
				String place = result.getString("place");
				Double latitudeRequest = result.getDouble("latitude");
				Double longitudeRequest = result.getDouble("longitude");
				Date startDate = result.getDate("startDate");
				Date finishDate = result.getDate("finishDate");
				Blob photo = result.getBlob("photo");
				Integer organizer_id = result.getInt("organizer_id");

				Event event = new Event();
				event.setID(id);
				event.setName(name);
				event.setDescription(description);
				event.setPlace(place);
				event.setLatitude(latitudeRequest);
				event.setLongitude(longitudeRequest);
				event.setStartDate(startDate);
				event.setFinishDate(finishDate);
				try {
					event.setPhoto(photo.getBinaryStream());
				} catch (Exception e) {
					// TODO: handle exception
				}

				User user = new User();
				user.setId(organizer_id);

				event.setOrganizer(user);

				events.add(event);

			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			// Close Connection
			try {
				if (result != null)
					result.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();

			} catch (SQLException e2) {
				// TODO: handle exception
			}
		}
		return events;
	}

	public Event getEventByID(Integer eventID) {
		for (Event e : getEvents(eventID)) {
			if (e.getID().equals(eventID)) {
				return e;
			}
		}
		return null;
	}
}
