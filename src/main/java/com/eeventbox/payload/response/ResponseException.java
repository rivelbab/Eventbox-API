package com.eeventbox.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResponseException extends RuntimeException implements Response {

	private final String message;

	@Singular
	private final Map<String, Object> properties;

	private final ResponseType type;
}
