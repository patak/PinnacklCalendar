package fr.pinnackl.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.pinnackl.bdd.Events;
import fr.pinnackl.bdd.Users;
import fr.pinnackl.beans.Event;
import fr.pinnackl.beans.User;

@Path("/events")
public class EventRest {
	private static final String USER_SESSION = "userSession";

	@GET
	@Produces("application/json")
	public Response getEvents(@Context HttpServletRequest request) throws JSONException {
		if (request.getSession().getAttribute(USER_SESSION) == null) {
			return Response.status(400).build();
		}

		User user = (User) request.getSession().getAttribute(USER_SESSION);
		Integer userId = (Integer) user.getId();

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		Events eventsDB = new Events();
		List<Event> events = eventsDB.getEvents(userId);

		for (Event event : events) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", event.getID());
			jsonObject.put("name", event.getName());
			jsonObject.put("description", event.getDescription());
			jsonObject.put("startDate", event.getStartDate());
			jsonObject.put("finishDate", event.getFinishDate());

			jsonObject.put("sharedEvent", false);
			if (event.getOrganizer().getId() != userId) {
				jsonObject.put("sharedEvent", true);
			}

			/**
			 * 
			 */
			Users usersDB = new Users();
			List<User> friendUsers = usersDB.getEventFriends(event.getID());

			JSONArray jsonArray = new JSONArray();

			for (User u : friendUsers) {
				JSONObject jsonFriendObject = new JSONObject();
				if (u.getId() == userId) {
					continue;
				}

				jsonFriendObject.put("id", u.getId());
				jsonFriendObject.put("pseudo", u.getPseudo());

				jsonArray.put(jsonFriendObject);
			}

			jsonObject.put("sharedUsers", jsonArray);

			jsonObjects.add(jsonObject);
		}
		// System.out.println(jsonObjects.toString());
		return Response.status(200).type(MediaType.APPLICATION_JSON + "; charset=UTF-8").entity(jsonObjects.toString())
				.build();
	}

	@Path("{id}")
	@GET
	@Produces("application/json")
	public Response getEvent(@PathParam("id") Integer id) throws JSONException {

		JSONObject jsonObject = new JSONObject();

		Events eventsDB = new Events();

		Event event = eventsDB.getEventByID(id);

		if (event != null) {
			jsonObject.put("id", event.getID());
			jsonObject.put("name", event.getName());
			jsonObject.put("startDate", event.getStartDate());
			jsonObject.put("finishDate", event.getFinishDate());

			return Response.status(200).type(MediaType.APPLICATION_JSON + "; charset=UTF-8")
					.entity(jsonObject.toString()).build();
		}

		return Response.status(404).build();
	}
}
