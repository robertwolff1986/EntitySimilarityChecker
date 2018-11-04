package com.wolffr.SimilarityChecker.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PermutationUtilTest {
	
	@Test
	void testGenerateWordPermutationsOneWord() {
		assertEquals(1,PermutationUtil.generateWordPermutations("abc").size());
	}

	@Test
	void testGenerateWordPermutations() {
		assertEquals(3,PermutationUtil.generateWordPermutations("Lothar betz Meister").size());
	}

}
