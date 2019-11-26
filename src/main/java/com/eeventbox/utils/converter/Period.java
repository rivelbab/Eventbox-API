package com.eeventbox.utils.converter;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * ===================================================================
 * The class contains start and end time needed for displaying
 * users' time availability preferences used by TimeSettingConverter.
 * Created by Rivel Babindamana on 31/10/2019 at Nanterre U.
 * ===================================================================
 */

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Period {

	private LocalTime start;
	private LocalTime end;
}
