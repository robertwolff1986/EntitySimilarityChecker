package com.wolffr.SimilarityChecker.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wolffr.SimilarityChecker.entity.Physician;

/**
 * Util class that can be used to preprocess String prior to checkin similarity. 
 * Currently substring removal and substring replacement are possible.
 * @author wolffr
 *
 */
public class Preprocessor {
	final static Logger LOGGER = LoggerFactory.getLogger(Preprocessor.class);

	private static List<String> stringsToRemove;
	private static Map<String, String> stringsToReplace;
	
	public Preprocessor() {
		if (stringsToRemove == null)
			initRemovalList();
		if (stringsToReplace == null)
			initReplaceList();
	}

	private void initRemovalList() {
		try {
			stringsToRemove = Files.lines(Paths.get(this.getClass().getResource("/Preprocessing.config").getPath()))
								   .filter(line -> line.contains("RemovePattern="))
								   .map(line -> line.substring(line.indexOf("RemovePattern=") + "RemovePattern=".length()))
								   .collect(Collectors.toList());
			
			LOGGER.debug(String.format("Initialized string removal list with %s entries", stringsToRemove.size()));
		} catch (Exception e) {
			LOGGER.error("Could not create remove list", e);
		}
	}

	private void initReplaceList() {
		try {
			stringsToReplace = Files.lines(Paths.get(this.getClass().getResource("/Preprocessing.config").getPath()))
									.filter(line -> line.contains("ReplacePattern="))
									.map(line -> {
													String patterToReplace = line.substring(line.indexOf("ReplacePattern=") + "ReplacePattern=".length(), line.indexOf("->"));
													String patternToReplaceWith = line.substring(line.indexOf("->") + "->".length());
													Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(patterToReplace.substring(patterToReplace.indexOf("'") + 1,patterToReplace.lastIndexOf("'")),patternToReplaceWith.substring(patternToReplaceWith.indexOf("'") + 1,patternToReplaceWith.lastIndexOf("'")));
													return entry;
									})
									.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
			
			LOGGER.debug(String.format("Initialized string replacement list with %s entries", stringsToReplace.size()));
		} catch (Exception e) {
			LOGGER.error("Could not create replaceList", e);
		}
	}

	/**
	 * Preprocess a given Entity using removal and replacement rules that are stored in 'Preprocessing.config'.
	 * @param physician: Entity that should be preprocessed
	 * @return preprocessed entity
	 */
	public Physician preprocess(Physician physician) {
		Physician preprocessedPhysician = new Physician(physician);
		stringsToRemove.stream().forEach(stringToRemove -> preprocessedPhysician.setName(preprocessedPhysician.getName().toLowerCase().replace(stringToRemove, "").trim()));
		stringsToRemove.stream().forEach(stringToRemove -> preprocessedPhysician.setStreet(preprocessedPhysician.getStreet()!=null?preprocessedPhysician.getStreet().toLowerCase().replace(stringToRemove, "").trim():""));
		
		if(physician.getStreet()!=null) {
			stringsToReplace.entrySet().stream().forEach(entryToReplace -> preprocessedPhysician.setName(preprocessedPhysician.getName().toLowerCase().replace(entryToReplace.getKey(), entryToReplace.getValue())));
			stringsToReplace.entrySet().stream().forEach(entryToReplace -> preprocessedPhysician.setStreet(preprocessedPhysician.getStreet().toLowerCase().replace(entryToReplace.getKey(), entryToReplace.getValue())));
		}
		return preprocessedPhysician;
	}
}
