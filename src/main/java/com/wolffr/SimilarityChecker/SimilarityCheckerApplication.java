package com.wolffr.SimilarityChecker;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.wolffr.SimilarityChecker.controller.CheckSimilarityController;
import com.wolffr.SimilarityChecker.db.IPhysicianManager;
import com.wolffr.SimilarityChecker.entity.CheckConfiguration;
import com.wolffr.SimilarityChecker.entity.Physician;

@SpringBootApplication
@ComponentScan({"com.wolffr.SimilarityChecker.controller","com.wolffr.SimilarityChecker.util"})
@EnableJpaRepositories("com.wolffr.SimilarityChecker.db")
public class SimilarityCheckerApplication {
	
	@Autowired
	private IPhysicianManager physicianManager;

	public static void main(String[] args) {
		SpringApplication.run(SimilarityCheckerApplication.class, args);
	}
	
	@Bean
	public boolean run() {
		CheckConfiguration firstCheckConfiguration = new CheckConfiguration("name", 10);
		firstCheckConfiguration.setCountSingleWordEqualsAsSimilar(true);
		firstCheckConfiguration.setSwitchWords(true);
		CheckConfiguration secondCheckConfiguration = new CheckConfiguration("street", 8);
		CheckConfiguration thirdCheckConfiguration = new CheckConfiguration("zip", 6);
		CheckSimilarityController<Physician,Long> controller = new CheckSimilarityController<Physician,Long>(physicianManager);
		controller.checkSimilarity(Arrays.asList(firstCheckConfiguration,secondCheckConfiguration,thirdCheckConfiguration));
		return false;
	}
}
