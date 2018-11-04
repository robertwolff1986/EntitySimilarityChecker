package com.wolffr.SimilarityChecker.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PermutationUtil {
	
	//If the word that should be permutated has more that maximumNumberWordsToGeneratePermutations subWords, permutation will not be done to decrease cpu/perocess time
	private static final Integer maximumNumberWordsToGeneratePermutations=5;

	public static List<String> generateWordPermutations(String word) {
		List<String> containedWords = Arrays.asList(word.split(" "));
		if (containedWords.size() == 1)
			return Arrays.asList(word);
		if(containedWords.size()>maximumNumberWordsToGeneratePermutations)
			return Arrays.asList(word);
		List<String> result=getPermutations(containedWords);
		return result;

	}

	private static List<String> getPermutations(List<String> containedWords) {
		List<List<String>> permutatedListList=permutate(containedWords);
		return permutatedListList.stream().map(subList -> subList.stream().collect(Collectors.joining(" "))).collect(Collectors.toList());
	}

	private static List<List<String>> permutate(List<String> wordList) {
		
		if(wordList.size()==1)
			return Arrays.asList(wordList);
		
		if(wordList.size()==2) 
			return Arrays.asList(Arrays.asList(wordList.get(0),wordList.get(1)),Arrays.asList(wordList.get(1),wordList.get(0)));
		
		List<List<String>> subListList = permutate(wordList.subList(1, wordList.size()));
		
		List<List<String>> resultList = new ArrayList<>();
		for(List<String> subList: subListList) {
			for(int i =0;i<=subList.size();i++) {
				List<String> newList = new ArrayList<>(subList);
				newList.add(i, wordList.get(0));
				resultList.add(newList);
			}
		}
		return resultList;
	}

}
