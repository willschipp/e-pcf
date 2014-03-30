package io.pivotal.poc.integration.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.integration.annotation.Splitter;

public class SplitByLineFeed  {

	@Splitter
	public List<String> splitMessage(String payload) {
		return Arrays.asList(payload.split(System.getProperty("line.separator")));
	}

}
