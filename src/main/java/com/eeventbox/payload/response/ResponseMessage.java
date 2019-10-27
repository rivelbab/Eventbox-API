package com.eeventbox.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.NonNull;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseMessage extends ReturnedObject implements Response {

	private final String message;

	@NonNull
	private final ResponseType type;

	@Singular
	private final Map<String, Object> properties;
}
