package com.wolffr.SimilarityChecker.util;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util class that can be used to return the levenshtein distance between two given String
 * @author wolffr
 *
 */
public class LevenstheinDistanceUtil {
	final static Logger LOGGER = LoggerFactory.getLogger(LevenstheinDistanceUtil.class);
	
	/**
	 * Return the levenshtein distance betwenn two given strings.
	 * @param source: String to compare with
	 * @param target: String to compare
	 * @return Levenshtein distance
	 */
	public static Integer getLevenshteinDistance(String source,String target) {
		Integer distance= new LevenshteinDistance().apply(source, target);
		LOGGER.debug(source+ " <-> " + target + " =" + distance);
		return distance;
	}

}
