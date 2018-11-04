package com.wolffr.SimilarityChecker.entity;

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