package fr.pinnackl.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.pinnackl.beans.Event;
import fr.pinnackl.beans.Share;
import fr.pinnackl.beans.User;

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

	public List<Share> getShares() {
		List<Share> shares = new ArrayList<Share>();

		// DB Connection
		Statement statement = null;
		ResultSet result = null;

		loadDatabase();

		try {

			statement = connection.createStatement();

			result = statement.executeQuery("SELECT * FROM shared;");

			// Retrieve Datas
			while (result.next()) {
				Integer id = result.getInt("shared_id");
				Integer ownerId = result.getInt("owner_id");
				Integer userId = result.getInt("user_id");
				Integer eventId = result.getInt("event_id");

				User owner = new User();
				owner.setId(ownerId);
				User user = new User();
				user.setId(userId);
				Event event = new Event();
				event.setID(eventId);

				Share share = new Share();
				share.setID(id);
				share.setOwner(owner);
				share.setUser(user);
				share.setEvent(event);

				shares.add(share);

			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			// Close Connection
			try {
				if (result != null)
					result.close();
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();

			} catch (SQLException e2) {
				// TODO: handle exception
			}
		}
		return shares;
	}

	public Boolean isUserEvent(User user) {
		for (Share s : getShares()) {
			if (s.getUser().equals(user)) {
				return true;
			}
		}
		return false;
	}

}
