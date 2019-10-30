package com.eeventbox.utils.converter;

import org.springframework.stereotype.Component;
import com.eeventbox.model.utils.Days;
import com.eeventbox.model.utils.TimeSetting;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class serves as a wrapper for the list of Periods
 * and contains two static utils methods for converting
 * TimeSetting object for binding it with the template
 * or storing into database
 */

@Component
public class TimeSettingConverter {

	private ArrayList<Period> periodsList;

	public TimeSettingConverter() {
		this.periodsList = new ArrayList<>();
	}

	public ArrayList<Period> getPeriodsList() {
		return periodsList;
	}

	public void setPeriodsList(ArrayList<Period> periodsList) {
		this.periodsList = periodsList;
	}


	// The method converts TimeSettings object's Map<Days, String>
	// TimeSettingConverter wrapper, ArrayList<Periods> for displaying into template
	public static TimeSettingConverter convertForTemplate(TimeSetting timeSetting) {

		TimeSettingConverter periodsWrapper = new TimeSettingConverter();
		Map<Days, String> timeMap = timeSetting.getTimeMap();

		for (Days day : Days.values()) {
			String startEnd = timeMap.get(day);
			String[] startEndArray = startEnd.split(";");
			LocalTime start = LocalTime.parse(startEndArray[0]);
			LocalTime end = LocalTime.parse(startEndArray[1]);
			periodsWrapper.getPeriodsList().add(new Period(start, end));
		}

		return periodsWrapper;
	}

	// Method converts TimeSettingConverter object into TimeSetting
	// after updating time preferences for storing into database
	public static TimeSetting convertForDatabase(TimeSettingConverter periodsWrapper) {

		TimeSetting timeSetting = new TimeSetting();
		Map<Days, String> timeMap = timeSetting.getTimeMap();
		ArrayList<Period> periodsList = periodsWrapper.getPeriodsList();

		for (int i = 0; i < periodsList.size(); i++) {
			String timeString = periodsList.get(i).getStart() + ";" + periodsList.get(i).getEnd();
			timeMap.put(Days.values()[i], timeString);
		}

		return timeSetting;
	}
}
