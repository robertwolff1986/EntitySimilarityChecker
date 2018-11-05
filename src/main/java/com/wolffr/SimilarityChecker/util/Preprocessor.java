package com.wolffr.SimilarityChecker.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wolffr.SimilarityChecker.entity.CheckConfiguration;

/**
 * Util class that can be used to preprocess String prior to checkin similarity. 
 * Currently substring removal and substring replacement are possible.
 * @author wolffr
 *
 */
public class Preprocessor<T> {
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
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			stringsToRemove = Files.lines(Paths.get(classloader.getResource("Preprocessing.config").toURI()))
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
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			stringsToReplace = Files.lines(Paths.get(classloader.getResource("Preprocessing.config").toURI()))
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
	 * @param checkConfigurations 
	 * @param entity: Entity that should be preprocessed
	 * @param checkConfigurations: Checkconfigurations that will be used to preprocess the correct fields
	 * @return preprocessed entity
	 */
	public T preprocess(T entity, List<CheckConfiguration> checkConfigurations) {

		Class<?> preprocessedEntityClass = entity.getClass();
		Constructor<?> constructor = preprocessedEntityClass.getConstructors()[0];
		try {
			@SuppressWarnings("unchecked")
			T preprocessedEntity = (T) constructor.newInstance(entity);

			for (CheckConfiguration configuration : checkConfigurations) {
				Field field = preprocessedEntity.getClass().getDeclaredField(configuration.getFieldToCheck());
				field.setAccessible(true);
				Object value = field.get(preprocessedEntity);
				if (value != null) {
					String newValue = value.toString();
					for (String stringToRemove : stringsToRemove) {
						newValue = newValue.toLowerCase().replace(stringToRemove, "").trim();
					}
					for (Map.Entry<String, String> entryToReplace : stringsToReplace.entrySet()) {
						newValue = newValue.toLowerCase().replace(entryToReplace.getKey(), entryToReplace.getValue());
					}
					field.set(preprocessedEntity, newValue);
				}
			}
			return preprocessedEntity;
		} catch (Exception e) {
			LOGGER.error("Couldn not preprocess entity: " + entity, e);
		}
		return null;
	}
}
