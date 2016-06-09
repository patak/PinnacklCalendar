package fr.pinnackl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.pinnackl.bcrypt.BCrypt;
import fr.pinnackl.bdd.Users;
import fr.pinnackl.beans.User;

/**
 * Servlet implementation class PinnacklServlet
 */
@WebServlet(name = "user-servlet", description = "Servlet handling user login", urlPatterns = { "/login", "/create",
		"/list", "/home", "/logout", "/change" })
public class PinnacklServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USER_SESSION = "userSession";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PinnacklServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String uri = request.getRequestURI();
		if (uri.contains("/login")) {
			this.login(request, response);
		} else if (uri.contains("/list")) {
			this.list(request, response);
		} else if (uri.contains("/create")) {
			this.create(request, response);
		} else if (uri.contains("/home")) {
			this.home(request, response);
		} else if (uri.contains("/logout")) {
			this.logout(request, response);
		} else if (uri.contains("/change")) {
			this.change(request, response);
		} else {
			this.home(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void home(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (request.getSession().getAttribute(USER_SESSION) == null)
			response.sendRedirect("login");
		else {
			User user = (User) request.getSession().getAttribute(USER_SESSION);
			request.setAttribute("login", user.getPseudo());
			request.getRequestDispatcher("/WEB-INF/html/home.jsp").forward(request, response);
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		final String pseudo = request.getParameter("pseudo");
		final String password = request.getParameter("password");

		User user = new User();
		Users usersDB = new Users();

		if (pseudo == null || password == null) {
			// Display form
		} else if (pseudo.isEmpty() || password.isEmpty()) {
			request.setAttribute("errorMessage", "Set username and password");
		} else if (usersDB.checkPseudo(pseudo)) {
			if (usersDB.checkPseudoWithPassword(pseudo, password)) {
				user.setPseudo(pseudo);
				user.setPassword(password);

				request.getSession().setAttribute(USER_SESSION, user);

				response.sendRedirect("home");
				return;
			} else {
				request.setAttribute("errorMessage", "Bad password");
			}
		} else {
			request.setAttribute("errorMessage", "User not found");
		}
		request.setAttribute("action", "login");
		request.setAttribute("title", "Login");
		request.getRequestDispatcher("/WEB-INF/html/userForm.jsp").forward(request, response);
	}

	private void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		final String pseudo = request.getParameter("pseudo");
		final String password = request.getParameter("password");

		User user = new User();
		Users usersDB = new Users();

		if (pseudo != null && password != null) {
			if (pseudo.isEmpty() || password.isEmpty()) {
				request.setAttribute("errorMessage", "Set username and password");
			} else if (usersDB.checkPseudo(pseudo)) {
				request.setAttribute("errorMessage", "User already exists. Please chose another");
			} else {
				user.setPseudo(pseudo);
				user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(12)));

				usersDB.createUser(user);
				request.setAttribute("success", "User succesfully created");
			}
		}
		request.setAttribute("action", "create");
		request.setAttribute("title", "Create User");
		request.getRequestDispatcher("/WEB-INF/html/userForm.jsp").forward(request, response);
	}

	private void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Users usersDB = new Users();

		request.setAttribute("title", "List of All Users");
		request.setAttribute("userList", usersDB.getAllUsers());

		request.getRequestDispatcher("/WEB-INF/html/userList.jsp").forward(request, response);
	}

	private void change(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (request.getSession().getAttribute(USER_SESSION) == null) {
			response.sendRedirect("login");
			return;
		}

		final String pseudo = request.getParameter("pseudo");
		final String password = request.getParameter("password");
		final String newPassword = request.getParameter("newPassword");
		final String confirmNewPassword = request.getParameter("confirmNewPassword");

		if (newPassword == null || confirmNewPassword == null) {
			// just display the login
		} else if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
			request.setAttribute("errorMessage", "Set new password");
		} else {
			if (newPassword.equals(confirmNewPassword)) {

				User user = (User) request.getSession().getAttribute(USER_SESSION);
				user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));

				Users usersDB = new Users();
				usersDB.updateUserPassword(user);

				request.setAttribute("success", "Password changed");
			} else {
				request.setAttribute("errorMessage", "New passwords not identical");
			}
		}
		request.setAttribute("action", "change");
		request.setAttribute("title", "Change password");
		request.getRequestDispatcher("/WEB-INF/html/userForm.jsp").forward(request, response);
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (request.getSession().getAttribute(USER_SESSION) != null)
			request.getSession().removeAttribute(USER_SESSION);
		response.sendRedirect("login");
	}

}
