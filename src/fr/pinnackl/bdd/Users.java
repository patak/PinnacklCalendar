package fr.pinnackl.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.pinnackl.bcrypt.BCrypt;
import fr.pinnackl.beans.User;

public class Users {

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

	public void createUser(User user) {

		PreparedStatement preparedStatement = null;

		loadDatabase();

		try {
			preparedStatement = connection.prepareStatement("INSERT INTO users(pseudo, password) VALUES(?, ?);");
			preparedStatement.setString(1, user.getPseudo());
			preparedStatement.setString(2, user.getPassword());

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

	public void updateUserPassword(User user) {

		PreparedStatement preparedStatement = null;

		loadDatabase();

		try {
			preparedStatement = connection.prepareStatement("UPDATE users SET password = ? WHERE pseudo = ?;");
			preparedStatement.setString(1, user.getPassword());
			preparedStatement.setString(2, user.getPseudo());

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

	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();

		// DB Connection
		Statement statement = null;
		ResultSet result = null;

		loadDatabase();

		try {

			statement = connection.createStatement();

			result = statement.executeQuery("SELECT user_id, pseudo, password FROM users;");

			// Retrieve Datas
			while (result.next()) {
				Integer id = result.getInt("user_id");
				String pseudo = result.getString("pseudo");
				String password = result.getString("password");

				User user = new User();
				user.setId(id);
				user.setPseudo(pseudo);
				user.setPassword(password);

				users.add(user);

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
		return users;
	}

	public List<User> getUsersPseudo() {
		List<User> allUsers = new ArrayList<User>();

		for (User u : getUsers()) {
			User user = new User();
			user.setPseudo(u.getPseudo());

			allUsers.add(user);
		}

		return allUsers;
	}

	public List<User> getEventFriends(Integer eventId) {
		List<User> friendUsers = new ArrayList<User>();

		// DB Connection
		PreparedStatement preparedStatement = null;
		ResultSet result = null;

		loadDatabase();

		try {
			preparedStatement = connection.prepareStatement(
					"SELECT u.* FROM shared as sh LEFT JOIN users as u ON (sh.user_id = u.user_id) WHERE sh.event_id = ?");
			preparedStatement.setLong(1, eventId);

			result = preparedStatement.executeQuery();

			while (result.next()) {
				Integer id = result.getInt("user_id");
				String pseudo = result.getString("pseudo");
				// String email = result.getString("email");

				User user = new User();
				user.setId(id);
				user.setPseudo(pseudo);

				friendUsers.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		return friendUsers;
	}

	public User getUserByPseudo(String pseudo) {
		for (User u : getUsers()) {
			if (u.getPseudo().equals(pseudo)) {
				return u;
			}
		}
		return null;
	}

	public boolean checkPseudo(String pseudo) {
		return (this.getUserByPseudo(pseudo) != null);
	}

	public boolean checkPseudoWithPassword(String pseudo, String password) {
		User u = this.getUserByPseudo(pseudo);
		if (u != null) {
			return BCrypt.checkpw(password, u.getPassword());
		}
		return false;
	}

}
