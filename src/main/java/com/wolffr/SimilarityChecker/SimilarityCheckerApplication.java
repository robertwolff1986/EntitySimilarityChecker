package com.wolffr.SimilarityChecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.wolffr.SimilarityChecker.controller.CheckSimilarityController;
import com.wolffr.SimilarityChecker.db.IPhysicianManager;

@SpringBootApplication
@ComponentScan({"com.wolffr.SimilarityChecker.controller"})
@EnableJpaRepositories("com.wolffr.SimilarityChecker.db")
public class SimilarityCheckerApplication {
	
	@Autowired
	CheckSimilarityController controller;

	public static void main(String[] args) {
		SpringApplication.run(SimilarityCheckerApplication.class, args);
	}
	
	@Bean
	public boolean run() {
		controller.checkSimilarity();
		return false;
	}
}
