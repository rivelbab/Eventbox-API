package com.eeventbox.utils.mapper;

import com.eeventbox.model.event.Event;
import com.eeventbox.payload.event.EventRequest;
import com.eeventbox.payload.event.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface EventMapper {

	Event asEvent(EventRequest eventRequest);

	EventRequest asEventRequest(Event event);

	void update(@MappingTarget Event event, EventRequest eventRequest);

	EventResponse asEventResponse(Event event);

	List<EventResponse> asEventResponse(List<Event> events);
}
