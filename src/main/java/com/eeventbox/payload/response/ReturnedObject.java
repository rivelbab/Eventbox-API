package com.eeventbox.payload.response;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ReturnedObject {

	public static Object of(Optional obj) {
		if (obj.isPresent()) {
			return obj.get();
		} else {
			return ResponseMessage.builder()
					.type(ResponseType.NOT_FOUND)
					.message("Not found!")
					.build().toResponseEntity();
		}
	}

	public static Object of(ReturnedObject obj) {
		return of(Optional.ofNullable(obj));
	}
}
