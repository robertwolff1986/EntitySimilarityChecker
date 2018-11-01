package com.wolffr.SimilarityChecker.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LevenshteinGroup<T> {

	private Integer id;
	private T root;
	private List<T> matchList = new ArrayList<>();

	public LevenshteinGroup(Integer id, T root) {
		this.id = id;
		this.root = root;
	}

	public void addMatch(T match) {
		matchList.add(match);
	}
	
	public T getRoot() {
		return root;
	}
	
	public Integer getId() {
		return id;
	}
	
	public List<T> getMatchList(){
		return matchList;
	}

	@Override
	public String toString() {
		return String.format("ID: %s, Root: %s" + System.lineSeparator() + "%s",id,root,matchList.stream()
				.map(match->match.toString()).collect(Collectors.joining(System.lineSeparator())));
	}

}
