package com.wolffr.SimilarityChecker.entity;

public class FieldConfiguration {
	
	private String fieldToCheck;
	private Integer weight;

	public FieldConfiguration(String fieldToCheck, Integer weight) {
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

}
