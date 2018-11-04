package com.wolffr.SimilarityChecker.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wolffr.SimilarityChecker.entity.CheckConfiguration;
import com.wolffr.SimilarityChecker.entity.Physician;

public class WeightedLevenshteinCheck {
	final static Logger LOGGER = LoggerFactory.getLogger(WeightedLevenshteinCheck.class);

	private List<CheckConfiguration> checkConfigurations;
	private Preprocessor preprocessor;

	public WeightedLevenshteinCheck(List<CheckConfiguration> checkConfigurations) {
		this.checkConfigurations = checkConfigurations;
		preprocessor=new Preprocessor();
	}

	public Double calculateSimilarity(Physician currentPhysician, Physician possibleMatch) {
		Map<String, Double> fieldAndScoreMap = new HashMap<>();
		for (CheckConfiguration checkConfiguration : checkConfigurations) {
			String currentField;
			String possibleMatchField;
			try {
				currentField = getField(preprocessor.preprocess(currentPhysician), checkConfiguration.getFieldToCheck());
				possibleMatchField = getField(preprocessor.preprocess(possibleMatch), checkConfiguration.getFieldToCheck());
			} catch (Exception e) {
				LOGGER.error(String.format("Could not return field: %s from entities; %s , %s", checkConfiguration.getFieldToCheck(), currentPhysician, possibleMatch));
				break;
			}
			
			if (StringUtils.isAnyEmpty(currentField, possibleMatchField)) {
				fieldAndScoreMap.put(checkConfiguration.getFieldToCheck(), 100.0);
			} else {
				boolean allWordsSimilar = checkAllWordsSimilar(currentField, possibleMatchField, checkConfiguration);
				if (allWordsSimilar)
					fieldAndScoreMap.put(checkConfiguration.getFieldToCheck(), 100.0);
				else {
					Integer distance = getDistance(currentField,possibleMatchField,checkConfiguration.isSwitchWords());
					Double similarity = calcSimiliarity(currentField.length(), distance);
					fieldAndScoreMap.put(checkConfiguration.getFieldToCheck(), similarity);
				}
			}
		}
//		LOGGER.info("------------------------------------------");
//		LOGGER.info(""+currentPhysician);
//		LOGGER.info(""+possibleMatch);
//		LOGGER.info(""+fieldAndScoreMap + ": " + getWeightedSimilarity(fieldAndScoreMap));
//		LOGGER.info(System.lineSeparator());
		return getWeightedSimilarity(fieldAndScoreMap);

	}

	private Integer getDistance(String currentField, String possibleMatchField, boolean switchWords) {
		if(!switchWords)
			return LevenstheinDistanceUtil.getLevenshteinDistance(currentField, possibleMatchField);
		
		List<String> currentFieldPermutationsList = PermutationUtil.generateWordPermutations(currentField);
		Integer optimalDistance=currentFieldPermutationsList.stream().map(currentFieldPermutation -> LevenstheinDistanceUtil.getLevenshteinDistance(currentFieldPermutation, possibleMatchField)).mapToInt(e->e).min().getAsInt();
		LOGGER.info("PermutationListSize: " + currentFieldPermutationsList.size()+ " Optimal Distance: " + optimalDistance);
		return optimalDistance;
		
	}

	private Double getWeightedSimilarity(Map<String, Double> fieldAndScoreMap) {
		Double summedValue = 0.0;
		Integer summedWeight = 0;
		for (Map.Entry<String, Double> entry : fieldAndScoreMap.entrySet()) {
			Integer currentWeight = checkConfigurations.stream()
					.filter(checkConfiguration -> checkConfiguration.getFieldToCheck().equals(entry.getKey()))
					.findFirst().get().getWeight();
			summedValue += currentWeight * entry.getValue();
			summedWeight += currentWeight;
		}
		return summedValue / summedWeight;
	}

	private boolean checkAllWordsSimilar(String currentField, String possibleMatchField,CheckConfiguration checkConfiguration) {
		if (checkConfiguration.isCountSingleWordEqualsAsSimilar()) {
			return checkContains(currentField, possibleMatchField);
		}
		return false;
	}

	private boolean checkContains(String currentField, String possibleMatchField) {
		List<String> currentFieldWords = Arrays.asList(currentField.split(" "));
		List<String> possibleMatchFieldWords = Arrays.asList(possibleMatchField.split(" "));
		if(currentFieldWords.size()>possibleMatchFieldWords.size())
			return possibleMatchFieldWords.stream().noneMatch(possibleMatchFieldWord -> !currentFieldWords.contains(possibleMatchFieldWord));
		return currentFieldWords.stream().noneMatch(currentFieldWord -> !possibleMatchFieldWords.contains(currentFieldWord));
	}

	private String getField(Physician currentPhysician, String fieldToCheck) throws Exception {
		Class<?> clazz = Class.forName(currentPhysician.getClass().getName());
		Field field = clazz.getDeclaredField(fieldToCheck);
		field.setAccessible(true);
		return field.get(currentPhysician)!=null?field.get(currentPhysician).toString():"";
	}

	private Double calcSimiliarity(int length, Integer distance) {
		return 100.0 - (100.0 / length) * distance;
	}
}
