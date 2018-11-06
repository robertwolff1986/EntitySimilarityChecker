package com.wolffr.SimilarityChecker.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.wolffr.SimilarityChecker.entity.CheckConfiguration;
import com.wolffr.SimilarityChecker.entity.DBEntity;
import com.wolffr.SimilarityChecker.entity.LevenshteinGroup;
import com.wolffr.SimilarityChecker.util.WeightedLevenshteinCheck;

/**
 * Coordinates similarity checks
 * 
 * @author wolffr
 *
 */
@Component
@ComponentScan({ "com.wolffr.SimilarityChecker.db" })
public class CheckSimilarityController<T extends DBEntity, ID> {
	final static Logger LOGGER = LoggerFactory.getLogger(CheckSimilarityController.class);

	private CrudRepository<T, ID> crudRepository;

	/**
	 * Contains all loaded entitied
	 */
	private List<T> allEntities;
	private List<CheckConfiguration> checkConfigurations;
	private List<LevenshteinGroup<T>> levenshteinGroups = new ArrayList<>();
	private Integer counter = 1;
	private List<T> matchedEntities = new ArrayList<>();

	public CheckSimilarityController(CrudRepository<T, ID> crudRepository) {
		this.crudRepository = crudRepository;
	}

	public void checkSimilarity(List<CheckConfiguration> checkConfigurations) {
		this.checkConfigurations = checkConfigurations;
		process();
	}

	private void process() {
		loadSourceEntities();
		findMatches();
		storeInDatabase();
	}

	private void loadSourceEntities() {
		allEntities = (List<T>) crudRepository.findAll();
		LOGGER.info(String.format("Loaded %s entities", allEntities.size()));
	}

	private void findMatches() {
		for (T entity : allEntities) {
			if (!matchedEntities.contains(entity)) {
				List<T> matchList = getMatches(entity);
				LevenshteinGroup<T> group = new LevenshteinGroup<T>(counter++, entity);
				matchList.stream().distinct().forEach(group::addMatch);
				matchList.stream().filter(match -> !matchedEntities.contains(match)).forEach(matchedEntities::add);
				levenshteinGroups.add(group);
				LOGGER.info(String.format("%s groups found, %s entires left to match", counter,
						(allEntities.size() - matchedEntities.size())));
			}
		}
	}

	private List<T> getMatches(T currentEntity) {

		if (matchedEntities.contains(currentEntity))
			return new ArrayList<>();

		matchedEntities.add(currentEntity);
		List<T> resultList = new ArrayList<>();
		for (T possibleMatch : allEntities) {
			if (!matchedEntities.contains(possibleMatch)) {
				WeightedLevenshteinCheck<T> weightedLevenshteinCheck = new WeightedLevenshteinCheck<T>(checkConfigurations);
				Double result = weightedLevenshteinCheck.calculateSimilarity(currentEntity, possibleMatch);
				if (result > 85.0) {
					resultList.add(possibleMatch);
				}
			}
		}
		resultList.addAll(resultList.stream()
									.map(this::getMatches)
									.flatMap(collection -> collection.stream())
									.collect(Collectors.toList()));
		return resultList;

	}

	private void storeInDatabase() {
		for (LevenshteinGroup<T> group : levenshteinGroups) {
			group.getRoot().setMergeGroup(group.getId());
			group.getMatchList().stream().forEach(match -> match.setMergeGroup(group.getId()));
			group.getMatchList().add(group.getRoot());
			group.getMatchList().stream().forEach(crudRepository::save);
		}
	}

}
