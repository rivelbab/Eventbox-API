package com.eeventbox.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface Response {

	@JsonProperty("message_type")
	ResponseType getType();

	@JsonProperty("message")
	String getMessage();

	@JsonProperty("properties")
	Map<String, Object> getProperties();

	@JsonIgnore
	default boolean isSuccessful() {
		return !(this instanceof Exception) && getType().isSuccessful();
	}

	default ResponseEntity<Response> toResponseEntity() {
		return new ResponseEntity<>(this, HttpStatus.valueOf(getType().getStatus()));
	}

	@JsonProperty("status_code")
	default int getStatusCode() {
		return getType().getStatus();
	}
}
