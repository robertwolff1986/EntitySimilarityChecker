package com.wolffr.SimilarityChecker.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wolffr.SimilarityChecker.entity.Physician;

@Service 
public class Preprocessor {
	final static Logger LOGGER = LoggerFactory.getLogger(Preprocessor.class);
	
	private List<String> stringsToRemove;

	@PostConstruct
	public void init() {
		try {
			stringsToRemove= Files.lines(Paths.get(this.getClass().getResource("/Preprocessing.config").getPath()))
					.filter(line->line.contains("RemovePattern=")).map(line -> line.substring(line.indexOf("RemovePattern=")+"RemovePattern=".length()))
			.collect(Collectors.toList());
			LOGGER.info(String.format("Found %s stringsToRemove",stringsToRemove.size()));
		} catch (Exception e) {
			LOGGER.error("Could not read Preprocessing config",e);
		}
	}
	
	public Physician preprocess(Physician physician) {
		Physician preprocessedPhysician = new Physician(physician);
		stringsToRemove.stream().forEach(stringToRemove -> preprocessedPhysician.setName(physician.getName().replace(stringToRemove, "")));
		stringsToRemove.stream().forEach(stringToRemove -> preprocessedPhysician.setStreet(physician.getStreet()!=null?physician.getStreet().replace(stringToRemove, ""):""));
		return preprocessedPhysician;
	}
	

}
