package com.wolffr.SimilarityChecker.entity;

/**
 * Configuration that must be used to identify a field that should be used to check similarity.
 * FieldTocheck identifies the attribute that should be used to check similarity.
 * Weight describes the importance of this field to be similar. A higher weight increases the impact on overall similarity.
 * @author wolffr
 *
 */
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

	@Override
	public String toString() {
		return "FieldConfiguration [fieldToCheck=" + fieldToCheck + ", weight=" + weight + "]";
	}
}
