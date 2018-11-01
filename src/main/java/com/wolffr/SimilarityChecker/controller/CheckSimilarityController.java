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
import com.wolffr.SimilarityChecker.entity.LevenshteinGroup;
import com.wolffr.SimilarityChecker.entity.Physician;
import com.wolffr.SimilarityChecker.util.LevenstheinDistanceUtil;
import com.wolffr.SimilarityChecker.util.Preprocessor;

@Component
@ComponentScan({ "com.wolffr.SimilarityChecker.db" })
public class CheckSimilarityController {
	final static Logger LOGGER = LoggerFactory.getLogger(CheckSimilarityController.class);

	@Autowired
	private IPhysicianManager physicianManager;

	@Autowired
	private Preprocessor preprocessor;
	
	private List<Physician> allPhyscians;
	private List<Physician> preprocessedPhysicians;
	private List<LevenshteinGroup<Physician>> levenshteinGroups = new ArrayList<>();
	private Integer counter = 1;
	private List<Physician> matchedPhysicians = new ArrayList<>();

	public CheckSimilarityController() {

	}

	public void checkSimilarity() {
		allPhyscians = physicianManager.findAll();
		createPreprocessedPhysician();
		LOGGER.info(String.format("Loaded %s physicians", allPhyscians.size()));
		process();
		persistLevenShteinGroups();
	}

	private void createPreprocessedPhysician() {
		preprocessedPhysicians=allPhyscians.stream().map(preprocessor::preprocess).collect(Collectors.toList());
	}

	private void persistLevenShteinGroups() {
		levenshteinGroups.parallelStream().forEach(this::persistGroup);
	}

	private void persistGroup(LevenshteinGroup<Physician> levenshteinGroup) {
		setGroupId(levenshteinGroup);
		physicianManager.save(levenshteinGroup.getRoot());
		levenshteinGroup.getMatchList().stream().forEach(physicianManager::save);
	}

	private void setGroupId(LevenshteinGroup<Physician> levenshteinGroup) {
		levenshteinGroup.getRoot().setMergeGroup(levenshteinGroup.getId());
		levenshteinGroup.getMatchList().stream().forEach(entity -> entity.setMergeGroup(levenshteinGroup.getId()));
	}

	private void process() {
		allPhyscians.stream().forEach(this::checkSimilarity);
	}

	private void checkSimilarity(Physician physician) {
		LevenstheinDistanceUtil util = new LevenstheinDistanceUtil();
		LevenshteinGroup<Physician> group = new LevenshteinGroup<Physician>(counter++, physician);
		
		allPhyscians.stream().filter(currentPhysician -> !physician.equals(currentPhysician))
				.filter(currentPhysician -> currentPhysician.getStreet() != null)
				.filter(currentPhysician -> !matchedPhysicians.contains(currentPhysician))
				.filter(currentPhysician -> util.getLevenshteinDistance(physician.getName(),
						currentPhysician.getName()) < 5)
				.filter(currentPhysician -> util.getLevenshteinDistance(physician.getStreet(),
						currentPhysician.getStreet()) < 5)
				.peek(matchedPhysicians::add).forEach(group::addMatch);
		levenshteinGroups.add(group);
	}

}
