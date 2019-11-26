package com.eeventbox.payload.api;
/**
 * ================================================
 * Contains custom api message and http code
 * Created by Rivelbab on 27/10/2019 at Nanterre U.
 * ================================================
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse {

	private Boolean success;
	private String message;
}
