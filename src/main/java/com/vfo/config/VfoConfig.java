package com.vfo.config;

import org.apache.commons.configuration2.XMLConfiguration;

import com.vfo.internal.customization.BehaviorRegistry;
import com.vfo.utilities.YamlHandler;

public class VfoConfig {

	private static final XMLConfiguration configuration = new XMLConfiguration();
	private static final XMLConfiguration userConfiguration = new XMLConfiguration();
	private static boolean initialized;
	private static final String VALIDATED = "validated";
	private static YamlHandler yamlHandler;
	private static final boolean reportConfigErrors = 
			Boolean.parseBoolean(System.getProperty("config.errors", "false"));

	static {
		for (ConfigurationProperty property : ConfigurationProperty.values()) {
			configuration.addProperty(property.getPropertyName(), property.getValue());
		}
		BehaviorRegistry.printVersions();
	}
}
