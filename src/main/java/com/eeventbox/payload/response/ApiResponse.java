package com.eeventbox.payload.response;
/**
 * ================================================
 * Contains custom response message and http code
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
