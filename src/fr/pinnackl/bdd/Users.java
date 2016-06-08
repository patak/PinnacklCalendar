package fr.pinnackl.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
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
		Statement statement = null;
		ResultSet result = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaee", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private User getUser(String pseudo) {
		for (User u : getUsers()) {
			if (u.getPseudo().equals(pseudo)) {
				return u;
			}
		}
		return null;
	}

	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();

		// DB Connection
		Statement statement = null;
		ResultSet result = null;

		loadDatabase();

		try {

			statement = connection.createStatement();

			result = statement.executeQuery("SELECT pseudo, password FROM users;");

			// Retrieve Datas
			while (result.next()) {
				String pseudo = result.getString("pseudo");
				String password = result.getString("password");

				User user = new User();
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

	public List<User> getAllUsers() {
		List<User> allUsers = new ArrayList<User>();

		for (User u : getUsers()) {
			User user = new User();
			user.setPseudo(u.getPseudo());

			allUsers.add(user);
		}

		return allUsers;
	}

	public void createUser(User user) {

		loadDatabase();

		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO users(pseudo, password) VALUES(?, ?);");
			preparedStatement.setString(1, user.getPseudo());
			preparedStatement.setString(2, user.getPassword());

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean checkPseudo(String pseudo) {
		return (this.getUser(pseudo) != null);
	}

	public boolean checkPseudoWithPassword(String pseudo, String password) {
		User u = this.getUser(pseudo);
		if (u != null) {
			return BCrypt.checkpw(password, u.getPassword());
		}
		return false;
	}

}
