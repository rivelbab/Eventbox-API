package com.eeventbox.utils.converter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import com.eeventbox.model.utility.Days;
import com.eeventbox.model.utility.TimeSetting;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * ========================================================
 * This class serves as a wrapper for the list of Periods
 * and contains two static utility methods for converting
 * TimeSetting object for storing into database
 * Created by Rivel Babindamana on 31/10/2019 at Nanterre U.
 * =========================================================
 */

@Getter
@Setter
@Component
public class TimeSettingConverter {

	private ArrayList<Period> periodsList;

	public TimeSettingConverter() {
		this.periodsList = new ArrayList<>();
	}

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

	/**
	 * Method converts TimeSettingConverter object into TimeSetting
	 * after updating time preferences for storing into database
	 */
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
