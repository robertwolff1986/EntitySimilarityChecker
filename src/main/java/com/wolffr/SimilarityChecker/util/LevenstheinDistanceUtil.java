package com.wolffr.SimilarityChecker.util;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LevenstheinDistanceUtil {
	final static Logger LOGGER = LoggerFactory.getLogger(LevenstheinDistanceUtil.class);
	
	public static Integer getLevenshteinDistance(String source,String target) {
		Integer distance= new LevenshteinDistance().apply(source, target);
		LOGGER.debug(source+ " <-> " + target + " =" + distance);
		return distance;
	}

}
