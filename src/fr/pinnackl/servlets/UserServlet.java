package fr.pinnackl.servlets;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.EmailValidator;

import fr.pinnackl.bcrypt.BCrypt;
import fr.pinnackl.bdd.Users;
import fr.pinnackl.beans.User;
import fr.pinnackl.mail.PinnacklMail;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet(name = "user-servlet", description = "Servlet handling users for pinnackl project", urlPatterns = {
		"/login", "/create", "/list", "/home", "/logout", "/change" })
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USER_SESSION = "userSession";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
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
		if (request.getSession().getAttribute(USER_SESSION) == null && uri.contains("/login") == false) {
			if (uri.contains("/create"))
				this.create(request, response);
			else
				response.sendRedirect("login");
			return;
		}
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
			// this.home(request, response);
			response.sendRedirect("events");
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
		User user = (User) request.getSession().getAttribute(USER_SESSION);
		request.setAttribute("login", user.getPseudo());
		request.setAttribute("homeTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/home.jsp").forward(request, response);
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		final String pseudo = request.getParameter("pseudo");
		final String password = request.getParameter("password");

		Users usersDB = new Users();

		if (pseudo == null || password == null) {
			// Display form
		} else if (pseudo.trim().length() == 0 || password.trim().length() == 0) {
			request.setAttribute("errorMessage", "Set username and password");
		} else if (usersDB.checkPseudo(pseudo)) {
			if (usersDB.checkPseudoWithPassword(pseudo, password)) {

				User user = usersDB.getUserByPseudo(pseudo);

				request.getSession().setAttribute(USER_SESSION, user);

				response.sendRedirect("events");
				return;
			} else {
				request.setAttribute("errorMessage", "Bad password");
			}
		} else {
			request.setAttribute("errorMessage", "User not found");
		}
		request.setAttribute("action", "login");
		request.setAttribute("title", "Login");
		request.setAttribute("loginTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/user/userForm.jsp").forward(request, response);
	}

	private void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		final String email = request.getParameter("email");
		final String pseudo = request.getParameter("pseudo");
		final String password = request.getParameter("password");

		User user = new User();
		Users usersDB = new Users();

		if (email != null && pseudo != null && password != null) {
			if (email.trim().length() == 0 || pseudo.trim().length() == 0 || password.trim().length() == 0) {
				request.setAttribute("errorMessage", "Set all required fields");
			} else if (!EmailValidator.getInstance().isValid(email)) {
				request.setAttribute("errorMessage", "Wrong email format");
			} else if (usersDB.checkEmail(email)) {
				request.setAttribute("errorMessage", "There is already an account with this email");
			} else if (usersDB.checkPseudo(pseudo)) {
				request.setAttribute("errorMessage", "User already exists. Please chose another");
			} else {
				user.setEmail(email);
				user.setPseudo(pseudo);
				user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(12)));

				usersDB.createUser(user);
				request.setAttribute("success", "User succesfully created");

				PinnacklMail mail = new PinnacklMail();
				try {
					mail.createAccountMail(user.getEmail(), user.getPseudo());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		request.setAttribute("action", "create");
		request.setAttribute("title", "Create User");
		request.setAttribute("createTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/user/userForm.jsp").forward(request, response);
	}

	private void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Users usersDB = new Users();

		request.setAttribute("title", "List of All Users");
		request.setAttribute("userList", usersDB.getUsersPseudo());
		request.setAttribute("listTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/user/userList.jsp").forward(request, response);
	}

	private void change(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		final String currentPassword = request.getParameter("currentPassword");
		final String newPassword = request.getParameter("newPassword");
		final String confirmNewPassword = request.getParameter("confirmNewPassword");

		User user = (User) request.getSession().getAttribute(USER_SESSION);
		Users usersDB = new Users();

		if (currentPassword == null || newPassword == null || confirmNewPassword == null) {
			// just display the login
		} else if (currentPassword.trim().length() == 0 || newPassword.trim().length() == 0
				|| confirmNewPassword.trim().length() == 0) {
			request.setAttribute("errorMessage", "Set all fields");
		} else if (usersDB.checkPseudoWithPassword(user.getPseudo(), currentPassword)) {
			if (newPassword.equals(confirmNewPassword)) {

				user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));

				usersDB.updateUserPassword(user);

				PinnacklMail mail = new PinnacklMail();
				try {
					mail.changePasswordMail(user.getEmail(), user.getPseudo());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				request.setAttribute("success", "Password changed");
			} else {
				request.setAttribute("errorMessage", "New passwords not identical");
			}
		} else {
			request.setAttribute("errorMessage", "Bad current password");
		}
		request.setAttribute("action", "change");
		request.setAttribute("title", "Change password");
		request.setAttribute("changeTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/user/userForm.jsp").forward(request, response);
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (request.getSession().getAttribute(USER_SESSION) != null)
			request.getSession().removeAttribute(USER_SESSION);
		response.sendRedirect("login");
	}

}
