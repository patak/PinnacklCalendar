package fr.pinnackl.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import fr.pinnackl.bcrypt.BCrypt;
import fr.pinnackl.bdd.Events;
import fr.pinnackl.bdd.Users;
import fr.pinnackl.beans.Event;
import fr.pinnackl.beans.User;

/**
 * Servlet implementation class PinnacklServlet
 */
@WebServlet(name = "pinnackl-servlet", description = "Servlet handling pinnackl project", urlPatterns = { "/login",
		"/create", "/list", "/home", "/logout", "/change", "/add", "/events" })
@MultipartConfig(maxFileSize = 16177215) // upload file's size up to 16MB
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
		} else if (uri.contains("/add")) {
			this.add(request, response);
		} else if (uri.contains("/events")) {
			this.events(request, response);
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
			request.setAttribute("homeTab", "active");
			request.getRequestDispatcher("/WEB-INF/html/home.jsp").forward(request, response);
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		final String pseudo = request.getParameter("pseudo");
		final String password = request.getParameter("password");

		Users usersDB = new Users();

		if (pseudo == null || password == null) {
			// Display form
		} else if (pseudo.isEmpty() || password.isEmpty()) {
			request.setAttribute("errorMessage", "Set username and password");
		} else if (usersDB.checkPseudo(pseudo)) {
			if (usersDB.checkPseudoWithPassword(pseudo, password)) {

				User user = usersDB.getUser(pseudo);

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
		request.setAttribute("loginTab", "active");
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
		request.setAttribute("createTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/userForm.jsp").forward(request, response);
	}

	private void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Users usersDB = new Users();

		request.setAttribute("title", "List of All Users");
		request.setAttribute("userList", usersDB.getAllUsers());
		request.setAttribute("listTab", "active");
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
		request.setAttribute("changeTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/userForm.jsp").forward(request, response);
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (request.getSession().getAttribute(USER_SESSION) != null)
			request.getSession().removeAttribute(USER_SESSION);
		response.sendRedirect("login");
	}

	private void add(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (request.getParameter("submit") != null) {
			final String name = request.getParameter("name");
			final String description = request.getParameter("description");
			final String place = request.getParameter("place");
			final String latitudeRequest = request.getParameter("latitude");
			final String longitudeRequest = request.getParameter("longitude");
			final String startDateRequest = request.getParameter("startDate");
			final String finishDateRequest = request.getParameter("finishDate");
			final Part photoRequest = request.getPart("photo");

			Double latitude = null;
			Double longitude = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			Date startDate = null;
			Date finishDate = null;
			InputStream photo = null; // input stream of the upload file

			Date currentDate = new Date();

			Event event = new Event();
			Events eventsDB = new Events();

			if (name != null && place != null && startDateRequest != null) {
				if (name.isEmpty() || place.isEmpty() || startDateRequest.isEmpty()) {
					request.setAttribute("errorMessage", "Set required fields");
				} else {
					if (latitudeRequest.length() > 0 && longitudeRequest.length() > 0) {
						latitude = Double.parseDouble(latitudeRequest);
						longitude = Double.parseDouble(latitudeRequest);
					}
					try {
						startDate = simpleDateFormat.parse(startDateRequest);
						if (startDate.before(currentDate)) {
							request.setAttribute("errorMessage", "Wrong start date");
						}
					} catch (Exception e) {
						request.setAttribute("errorMessage", "Wrong date format");
					}
					if (finishDateRequest.length() > 0) {
						try {
							finishDate = simpleDateFormat.parse(finishDateRequest);
							if (finishDate.before(startDate)) {
								request.setAttribute("errorMessage", "Wrong finish date");
							}
						} catch (Exception e) {
							request.setAttribute("errorMessage", "Wrong date format");
						}
					}
					if (photoRequest.getSize() > 0) {
						System.out.println(photoRequest.getSubmittedFileName());
						System.out.println(photoRequest.getSize());
						System.out.println(photoRequest.getContentType());
						if (photoRequest.getContentType() != "image/jpeg"
								|| photoRequest.getContentType() != "image/png") {
							request.setAttribute("errorMessage", "Wrong file format");
						}
						photo = photoRequest.getInputStream();
					}

					event.setName(name);
					event.setDescription(description);
					event.setPlace(place);
					event.setLatitude(latitude);
					event.setLongitude(longitude);
					event.setStartDate(startDate);
					event.setFinishDate(finishDate);
					event.setPhoto(photo);
					event.setOrganizer((User) request.getSession().getAttribute(USER_SESSION));

					eventsDB.createEvent(event);
					request.setAttribute("success", "Event succesfully created");
				}
			}
		}
		request.setAttribute("action", "add");
		request.setAttribute("title", "Add Event");
		request.setAttribute("createTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/eventForm.jsp").forward(request, response);
	}

	private void events(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Events eventsDB = new Events();

		// request.setAttribute("title", "Event");
		// request.setAttribute("userList", eventsDB.getEvents());
		// request.setAttribute("listTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/eventCalendar.jsp").forward(request, response);
	}

}
