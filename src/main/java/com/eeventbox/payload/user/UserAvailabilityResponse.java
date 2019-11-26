package com.eeventbox.payload.user;

public class UserAvailabilityResponse {

	private Boolean available;

	public UserAvailabilityResponse(Boolean available) {
		this.available = available;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}
}
