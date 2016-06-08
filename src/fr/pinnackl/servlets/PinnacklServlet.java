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
		}

		// else if(uri.contains("/home")) {
		// this.home(request, response);
		// }else if(uri.contains("/logout")) {
		// this.logout(request, response);
		// }else if(uri.contains("/change")) {
		// this.change(request, response);
		// }else {
		// this.home(request, response);
		// }
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

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	}

	private void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		final String pseudo = request.getParameter("pseudo");
		final String password = request.getParameter("password");

		User user = new User();
		Users usersDB = new Users();

		if (pseudo != null && password != null) {
			if (pseudo.isEmpty() || password.isEmpty()) {
				request.setAttribute("errorMessage", "Set username and password");
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
		request.setAttribute("userList", usersDB.getUsers());

		request.getRequestDispatcher("/WEB-INF/html/userList.jsp").forward(request, response);
	}

}
