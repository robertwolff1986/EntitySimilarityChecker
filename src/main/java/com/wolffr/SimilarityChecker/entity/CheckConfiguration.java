package com.wolffr.SimilarityChecker.entity;

/**
 * Configuration that must be used to identify a field that should be used to check similarity.
 * <br>FieldTocheck identifies the attribute that should be used to check similarity.
 * <br>Weight describes the importance of this field to be similar. A higher weight increases the impact on overall similarity.
 * <br>CountSingleWordsEqualsAsSimilar can be used to identify two values in this field as similar if one of them contains the other.
 * <br>SwitchWords can be used to permutate the field(using ' ' as splitter) and checking the optimal distance using all permutations. 
 * @author wolffr
 *
 */
public class CheckConfiguration {
	
	private String fieldToCheck;
	private Integer weight;
	private boolean countSingleWordEqualsAsSimilar;
	private boolean switchWords;

	public CheckConfiguration(String fieldToCheck, Integer weight) {
		super();
		this.fieldToCheck = fieldToCheck;
		this.weight = weight;
	}

	public String getFieldToCheck() {
		return fieldToCheck;
	}

	public Integer getWeight() {
		return weight;
	}

	public boolean isSwitchWords() {
		return switchWords;
	}

	public void setSwitchWords(boolean switchWords) {
		this.switchWords = switchWords;
	}

	public boolean isCountSingleWordEqualsAsSimilar() {
		return countSingleWordEqualsAsSimilar;
	}

	public void setCountSingleWordEqualsAsSimilar(boolean countSingleWordEqualsAsSimilar) {
		this.countSingleWordEqualsAsSimilar = countSingleWordEqualsAsSimilar;
	}
}