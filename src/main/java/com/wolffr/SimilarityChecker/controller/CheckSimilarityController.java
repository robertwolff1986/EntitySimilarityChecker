package com.wolffr.SimilarityChecker.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.wolffr.SimilarityChecker.db.IPhysicianManager;
import com.wolffr.SimilarityChecker.entity.CheckConfiguration;
import com.wolffr.SimilarityChecker.entity.LevenshteinGroup;
import com.wolffr.SimilarityChecker.entity.Physician;
import com.wolffr.SimilarityChecker.util.LevenstheinDistanceUtil;
import com.wolffr.SimilarityChecker.util.Preprocessor;
import com.wolffr.SimilarityChecker.util.WeightedLevenshteinCheck;
/**
 * Coordinates similarity checks
 * @author wolffr
 *
 */
@Component
@ComponentScan({ "com.wolffr.SimilarityChecker.db" })
public class CheckSimilarityController {
	final static Logger LOGGER = LoggerFactory.getLogger(CheckSimilarityController.class);

	@Autowired
	private IPhysicianManager physicianManager;

	
	/**
	 * Contains all loaded entitied
	 */
	private List<Physician> allPhyscians;
	private List<CheckConfiguration> checkConfigurations;
	private List<LevenshteinGroup<Physician>> levenshteinGroups = new ArrayList<>();
	private Integer counter = 1;
	private List<Physician> matchedPhysicians = new ArrayList<>();

	public CheckSimilarityController() {

	}

	public void checkSimilarity(List<CheckConfiguration> checkConfigurations) {
		this.checkConfigurations=checkConfigurations;
		process();
	}

	private void process() {
		loadSourceEntities();
		allPhyscians.stream().forEach(this::checkSimilarity);
		storeInDatabase();
	}

	private void loadSourceEntities() {
		allPhyscians=physicianManager.findAll();
		LOGGER.info(String.format("Loaded %s physicians", allPhyscians.size()));
	}

	private void checkSimilarity(Physician currentPhysician) {
		if(matchedPhysicians.contains(currentPhysician))
			return;
		
		LevenshteinGroup<Physician> levenshteinGroup = new LevenshteinGroup<Physician>(counter++, currentPhysician);
		matchedPhysicians.add(currentPhysician);
		for(Physician possibleMatch:allPhyscians) {
			if(!matchedPhysicians.contains(possibleMatch))
			{
				WeightedLevenshteinCheck weightedLevenshteinCheck = new WeightedLevenshteinCheck(checkConfigurations);
				Double result = weightedLevenshteinCheck.calculateSimilarity(currentPhysician,possibleMatch);
				if(result>80.0)
				{
					levenshteinGroup.addMatch(possibleMatch);
					matchedPhysicians.add(possibleMatch);
				}
			}
		}
		levenshteinGroups.add(levenshteinGroup);
		LOGGER.info(String.format("%s groups found, %s entires left to match", counter,(allPhyscians.size()-matchedPhysicians.size())));
	}
	

	private void storeInDatabase() {
		for(LevenshteinGroup<Physician> group:levenshteinGroups) {
			group.getRoot().setMergeGroup(group.getId());
			group.getMatchList().stream().forEach(match -> match.setMergeGroup(group.getId()));
			group.getMatchList().add(group.getRoot());
			group.getMatchList().stream().forEach(physicianManager::save);
		}
	}


}
