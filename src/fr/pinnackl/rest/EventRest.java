package fr.pinnackl.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import fr.pinnackl.bdd.Events;
import fr.pinnackl.beans.Event;

@Path("/events")
public class EventRest {
	@GET
	@Produces("application/json")
	public Response getEvents() throws JSONException {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		Events eventsDB = new Events();
		List<Event> events = eventsDB.getEvents();

		for (Event event : events) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", event.getID());
			jsonObject.put("name", event.getName());
			jsonObject.put("startDate", event.getStartDate());
			jsonObject.put("finishDate", event.getFinishDate());

			jsonObjects.add(jsonObject);
		}

		return Response.status(200).type(MediaType.APPLICATION_JSON).entity(jsonObjects.toString()).build();
	}
}
