package com.eeventbox.utils.converter;


import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * The class contains start and end time needed for displaying users'
 * time availability preferences in Thymeleaf template.
 * Used by TimeSettingConverter.
 */

@Component
public class Period {

	private LocalTime start;
	private LocalTime end;

	public Period() {
	}

	public Period(LocalTime start, LocalTime end) {
		this.start = start;
		this.end = end;
	}

	public LocalTime getStart() {
		return start;
	}

	public void setStart(LocalTime start) {
		this.start = start;
	}

	public LocalTime getEnd() {
		return end;
	}

	public void setEnd(LocalTime end) {
		this.end = end;
	}
}
