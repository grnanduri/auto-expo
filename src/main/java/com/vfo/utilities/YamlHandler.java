package com.vfo.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.yaml.snakeyaml.parser.ParserException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.vfo.logger.VfoLogger;

/**
 * YAML Reader implementation using SnakeYaml.
 * 
 * @author ganga
 *
 */
public class YamlHandler {

	private String newYamlFilePath;
	private Map<String, Object> newYamlMap;
	private static final Logger LOG = VfoLogger.getLogger();
	
	/**
	 * Constructor to build a YAML map from the file specified.
	 * 
	 * @param yamlFilePath
	 * 			Absolute Path for the file. <br>
	 * <br>
	 * 			Example: If sample.yml is present in
	 * 			src/test/resources/data/sample.yml<br>
	 * 			Create an object as, <br>
	 * 			{@code YamlHandler yaml = new YamlHandler("/data/sample.yml");}
	 * <br>
	 * 			{@code String visaEmail = yaml.fetchString(Notification.createEmailNotification);}
	 */
	public YamlHandler(String yamlFilePath) {
		
		newYamlFilePath = yamlFilePath;
		try {
			InputStream file = findStream(newYamlFilePath);
			if(Commons.isNull(file) && !newYamlFilePath.startsWith("/")) {
				newYamlFilePath = "/" + newYamlFilePath;
				file = findStream(newYamlFilePath);
			}
			if (Commons.isNull(file)) {
				String error = "Invalid file path specified. (" + newYamlFilePath + ")";
				throw new VfoException(error);
			}
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>(){};
			newYamlMap = mapper.readValue(new File(Commons.fetchFileUsingAbsolutePath(yamlFilePath)), typeRef);
			file.close();
			LOG.debug("YAML file ({}) loaded", newYamlFilePath);
		} catch (FileNotFoundException e) {
			String error = "File not found in path (" + newYamlFilePath + ")";
			throw new VfoException(error, e);
		} catch (IOException e) {
			String error = "IOException while loading YAML File (" + newYamlFilePath + ")";
			throw new VfoException(error, e);
		} catch (ParserException e) {
			String error = "Error in parsing YAML File (" + newYamlFilePath + ")";
			throw new VfoException(error, e);
		} catch (Exception e) {
			String error = "Error in loading YAML File (" + newYamlFilePath + ")";
			throw new VfoException(error, e);
		}
		
	}
	
	/**
	 * Gets the Yaml map.
	 * 
	 * @return newYamlMap
	 */
	public Map<String, Object> getYamlMap() {
		return newYamlMap;
	}

	private InputStream findStream(String fileName) {
		InputStream streamOnClass = getClass().getResourceAsStream(fileName);
		if(streamOnClass != null) {
			return streamOnClass;
		}
		return getClass().getClassLoader().getResourceAsStream(fileName);
	}
	
	/**
	 * Used to search base for YAML keys.
	 * @param key value in YAML
	 */
	@SuppressWarnings("unchecked")
	public void setKeyBase(String key) {
		newYamlMap = (Map<String, Object>) fetchObject(key);
	}
	
	/**
	 * Retrieve the YAML File Path.
	 * @return String saved YAML File path
	 */
	public String getYamlFilePath(){
		return newYamlFilePath;
	}
	
	/**
	 * Same as {@link #fetchObject(String)} except this casts the result into a String.
	 * 
	 * @param key Xpath of value to be fetched.
	 * @return String value
	 */
	public String fetchString(String key) {
		String out;
		try {
			out = (String) fetchObject(key);
		} catch (ClassCastException cc) {
			String error = "Error in fetching String value for the key (" + key + ")";
			throw new VfoException(error, cc);
		}
		return out;
	}

	/**
	 * Return the object selected by the key from yaml file.
	 * @param key	Key contains path to an object. Path segment is separated by
	 * 				dot. E.g. name.first_name
	 * @return		Yaml Object
	 */
	@SuppressWarnings("unchecked")
	public Object fetchObject(String key) {
		String[] path = key.split("\\.");
		Object currentValue = newYamlFilePath;
		for(String pathSection : path) {
			if (!((Map<String, Object>) currentValue).containsKey(pathSection)) {
				currentValue = null;
				break;
			}
			Object value = ((Map<String, Object>) currentValue).get(pathSection);
			if (value == null && (StringUtils.isNumericSpace(pathSection))) {
				value = ((Map<String, Object>) currentValue).get(Integer.parseInt(pathSection));
			}
			currentValue = value;
		}
		return currentValue;
	}
}
