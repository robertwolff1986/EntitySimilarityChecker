package com.wolffr.SimilarityChecker.util;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class LevenstheinDistanceUtil {
	
	public Integer getLevenshteinDistance(String source,String target) {
		return new LevenshteinDistance().apply(source, target);
	}

}
