package fr.pinnackl.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import fr.pinnackl.bdd.Events;
import fr.pinnackl.bdd.Shares;
import fr.pinnackl.bdd.Users;
import fr.pinnackl.beans.Event;
import fr.pinnackl.beans.Share;
import fr.pinnackl.beans.User;
import fr.pinnackl.mail.PinnacklMail;

/**
 * Servlet implementation class EventServlet
 */
@WebServlet(name = "event-servlet", description = "Servlet handling events for pinnackl project", urlPatterns = {
		"/add", "/edit", "/events" })
@MultipartConfig(maxFileSize = 1024 * 1024 * 16)
public class EventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "upload";
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
		// System.out.println(request.getRequestURI());
		// System.out.println(request.getContextPath());
		// System.out.println(request.getServletPath());
		// System.out.println(request.getQueryString());
		// System.out.println(request.getParameter("id"));
		if (request.getSession().getAttribute(USER_SESSION) == null) {
			response.sendRedirect("login");
			return;
		}
		if (uri.contains("/add")) {
			this.add(request, response);
		} else if (uri.contains("/edit")) {
			this.edit(request, response);
		} else if (uri.contains("/events")) {
			this.events(request, response);
		} else {
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
		request.setAttribute("eventsTab", "active");
		if (request.getParameter("submit") != null) {
			final String name = request.getParameter("name");
			final String description = request.getParameter("description");
			final String place = request.getParameter("place");
			final String latitudeRequest = request.getParameter("latitude");
			final String longitudeRequest = request.getParameter("longitude");
			final String startDateRequest = request.getParameter("startDate");
			final String finishDateRequest = request.getParameter("finishDate");
			final Part photoRequest = request.getPart("photo");

			final String emailsRequest = request.getParameter("share");

			System.out.println(request.getParameter("share"));

			Double latitude = null;
			Double longitude = null;
			if (latitudeRequest.length() > 0 && longitudeRequest.length() > 0) {
				latitude = Double.parseDouble(latitudeRequest);
				longitude = Double.parseDouble(longitudeRequest);
			}

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			// Date currentDate = new Date();
			Date startDate = null;
			Date finishDate = null;

			// InputStream photo = null; // input stream of the upload file
			// photo = photoRequest.getInputStream();

			Event event = new Event();
			Events eventsDB = new Events();

			if (name.trim().length() == 0 || place.trim().length() == 0 || startDateRequest.trim().length() == 0) {
				request.setAttribute("errorMessage", "Set required fields");
			} else {

				try {
					startDate = simpleDateFormat.parse(startDateRequest);
					// if (startDate.before(currentDate)) {
					// request.setAttribute("errorMessage", "Wrong start
					// date");
					// request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request,
					// response);
					// }
				} catch (Exception e) {
					request.setAttribute("errorMessage", "Wrong date format");
					request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request, response);
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
						request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request, response);
					}
				}

				/*
				 * if (photoRequest.getSize() > 0) { if
				 * (!photoRequest.getContentType().equals("image/jpeg") &&
				 * !photoRequest.getContentType().equals("image/png")) {
				 * request.setAttribute("errorMessage", "Wrong file format");
				 * request.getRequestDispatcher(
				 * "/WEB-INF/html/event/eventForm.jsp").forward(request,
				 * response); } }
				 */

				String[] supportedContentTypes = { "image/jpeg", "image/png" };

				String appPath = request.getServletContext().getRealPath("");
				String savePath = appPath + File.separator + SAVE_DIR;

				System.out.println(savePath);
				File fileSaveDir = new File(savePath);
				if (!fileSaveDir.exists()) {
					fileSaveDir.mkdir();
				}

				String fileName = extractFileName(photoRequest);
				String contentType = photoRequest.getContentType();

				Boolean fileError = false;
				if (fileName == null || fileName.isEmpty())
					fileError = true;
				if (contentType == null || contentType.isEmpty())
					fileError = true;
				if (!Arrays.asList(supportedContentTypes).contains(contentType))
					fileError = true;

				if (!fileError) {
					photoRequest.write(savePath + File.separator + fileName);
				}

				event.setName(name);
				event.setDescription(description);
				event.setPlace(place);
				event.setLatitude(latitude);
				event.setLongitude(longitude);
				event.setStartDate(startDate);
				event.setFinishDate(finishDate);
				// event.setPhoto(photo);
				event.setOrganizer((User) request.getSession().getAttribute(USER_SESSION));

				eventsDB.createEvent(event);
				request.setAttribute("success", "Event succesfully created");

				String[] emails = emailsRequest.split("\",\"");
				emails[0] = emails[0].replace("[\"", "");
				emails[emails.length - 1] = emails[emails.length - 1].replace("\"]", "");

				if (emails.length > 0) {
					for (String e : emails) {
						Users usersDB = new Users();
						User user = usersDB.getUserByEmail(e);
						if (user != null) {
							Share share = new Share();
							Shares shareDB = new Shares();
							if (!shareDB.isUserEvent(user)) {
								share.setEvent(event);
								share.setOwner(event.getOrganizer());
								share.setUser(user);

								shareDB.createShare(share);

								PinnacklMail mail = new PinnacklMail();
								try {
									mail.shareEventMail(user.getEmail(), user.getPseudo(), event.getName());
								} catch (MessagingException me) {
									// TODO Auto-generated catch block
									me.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request, response);
	}

	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}

	private void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setAttribute("action", "edit");
		request.setAttribute("title", "Edit Event");
		request.setAttribute("homeTab", "active");

		Events eventsDB = new Events();
		Event event = eventsDB.getEventByID(Integer.valueOf(request.getParameter("id")));

		if (event == null) {
			response.sendRedirect("/events");
			return;
		}

		request.setAttribute("startDate", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(event.getStartDate()));
		try {
			request.setAttribute("finishDate", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(event.getFinishDate()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			request.setAttribute("latitude", String.valueOf(event.getLatitude()));
			request.setAttribute("longitude", String.valueOf(event.getLongitude()));
		} catch (Exception e) {

		}

		if (request.getParameter("submit") != null) {
			final String name = request.getParameter("name");
			final String description = request.getParameter("description");
			final String place = request.getParameter("place");
			final String latitudeRequest = request.getParameter("latitude");
			final String longitudeRequest = request.getParameter("longitude");
			final String startDateRequest = request.getParameter("startDate");
			final String finishDateRequest = request.getParameter("finishDate");
			final Part photoRequest = request.getPart("photo");

			final String emailsRequest = request.getParameter("share");

			Double latitude = null;
			Double longitude = null;
			if (latitudeRequest.length() > 0 && longitudeRequest.length() > 0) {
				latitude = Double.parseDouble(latitudeRequest);
				longitude = Double.parseDouble(longitudeRequest);
			}

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			// Date currentDate = new Date();
			Date startDate = null;
			Date finishDate = null;

			InputStream photo = null; // input stream of the upload file
			photo = photoRequest.getInputStream();

			if (name.trim().length() == 0 || place.trim().length() == 0 || startDateRequest.trim().length() == 0) {
				request.setAttribute("errorMessage", "Set required fields");
			} else {

				try {
					startDate = simpleDateFormat.parse(startDateRequest);
					// if (startDate.before(currentDate)) {
					// request.setAttribute("errorMessage", "Wrong start
					// date");
					// request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request,
					// response);
					// }
				} catch (Exception e) {
					request.setAttribute("errorMessage", "Wrong date format");
					response.sendRedirect("edit?id=" + event.getID());
					return;
				}

				if (finishDateRequest.length() > 0) {
					try {
						finishDate = simpleDateFormat.parse(finishDateRequest);
						if (finishDate.before(startDate)) {
							request.setAttribute("errorMessage", "Wrong finish date");
							response.sendRedirect("edit?id=" + event.getID());
							return;
						}
					} catch (Exception e) {
						request.setAttribute("errorMessage", "Wrong date format");
						response.sendRedirect("edit?id=" + event.getID());
						return;
					}
				}

				if (photoRequest.getSize() > 0) {
					if (!photoRequest.getContentType().equals("image/jpeg")
							&& !photoRequest.getContentType().equals("image/png")) {
						request.setAttribute("errorMessage", "Wrong file format");
						request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request, response);
					}
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

				eventsDB.updateEvent(event);
				request.setAttribute("success", "Event succesfully updated");

				String[] emails = emailsRequest.split("\",\"");
				emails[0] = emails[0].replace("[\"", "");
				emails[emails.length - 1] = emails[emails.length - 1].replace("\"]", "");

				if (emails.length > 0) {
					for (String e : emails) {
						Users usersDB = new Users();
						User user = usersDB.getUserByEmail(e);
						if (user != null) {
							Share share = new Share();
							Shares shareDB = new Shares();
							if (!shareDB.isUserEvent(user)) {
								share.setEvent(event);
								share.setOwner(event.getOrganizer());
								share.setUser(user);

								shareDB.createShare(share);

								PinnacklMail mail = new PinnacklMail();
								try {
									mail.shareEventMail(user.getEmail(), user.getPseudo(), event.getName());
								} catch (MessagingException me) {
									// TODO Auto-generated catch block
									me.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		request.setAttribute("event", event);
		request.getRequestDispatcher("/WEB-INF/html/event/eventForm.jsp").forward(request, response);
	}

	private void events(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Events eventsDB = new Events();

		User user = (User) request.getSession().getAttribute(USER_SESSION);
		Integer userId = (Integer) user.getId();
		request.setAttribute("title", "Event");
		request.setAttribute("eventsList", eventsDB.getEvents(userId));
		request.setAttribute("eventsTab", "active");
		request.getRequestDispatcher("/WEB-INF/html/event/eventCalendar.jsp").forward(request, response);
	}
}
