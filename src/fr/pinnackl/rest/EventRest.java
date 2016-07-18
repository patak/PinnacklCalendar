package fr.pinnackl.rest;

import java.util.List;

import com.google.gson.Gson;

import fr.pinnackl.bdd.Events;
import fr.pinnackl.beans.Event;

public class EventRest {

	public String getEvents() {

		Events eventsDB = new Events();
		List<Event> eventList = eventsDB.getEvents();

		Gson gson = new Gson();

		String json = gson.toJson(eventList);

		return json;
	}

	public String getEvent(Integer id) {

		Events eventsDB = new Events();
		Event event = eventsDB.getEventByID(id);

		Gson gson = new Gson();

		String json = gson.toJson(event);

		return json;
	}
}
