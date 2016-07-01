package fr.pinnackl.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import fr.pinnackl.bdd.Events;
import fr.pinnackl.beans.Event;
import fr.pinnackl.beans.User;

/**
 * Servlet implementation class EventServlet
 */
@WebServlet(name = "event-servlet", description = "Servlet handling events for pinnackl project", urlPatterns = {
		"/add", "/events" })
public class EventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USER_SESSION = "userSession";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EventServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String uri = request.getRequestURI();
		if (uri.contains("/add")) {
			this.add(request, response);
		} else if (uri.contains("/events")) {
			this.events(request, response);
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

	private void add(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setAttribute("action", "add");
		request.setAttribute("title", "Add Event");
		request.setAttribute("createTab", "active");
		if (request.getSession().getAttribute(USER_SESSION) == null)
			response.sendRedirect("login");
		else {
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
								request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request,
										response);
							}
						} catch (Exception e) {
							request.setAttribute("errorMessage", "Wrong date format");
							request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request,
									response);
						}
						if (finishDateRequest.length() > 0) {
							try {
								finishDate = simpleDateFormat.parse(finishDateRequest);
								if (finishDate.before(startDate)) {
									request.setAttribute("errorMessage", "Wrong finish date");
									request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request,
											response);
								}
							} catch (Exception e) {
								request.setAttribute("errorMessage", "Wrong date format");
								request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request,
										response);
							}
						}
						if (photoRequest.getSize() > 0) {
							System.out.println(photoRequest.getSubmittedFileName());
							System.out.println(photoRequest.getSize());
							System.out.println(photoRequest.getContentType());
							if (photoRequest.getContentType() != "image/jpeg"
									|| photoRequest.getContentType() != "image/png") {
								request.setAttribute("errorMessage", "Wrong file format");
								request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request,
										response);
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
				} else {
					request.setAttribute("errorMessage", "Set required fields");
				}
			}
		}
		request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request, response);
	}

	private void events(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Events eventsDB = new Events();

		// request.setAttribute("title", "Event");
		// request.setAttribute("userList", eventsDB.getEvents());
		// request.setAttribute("listTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/event/eventCalendar.jsp").forward(request, response);
	}

}