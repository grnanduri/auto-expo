package com.vfo.config;

import org.apache.commons.configuration2.XMLConfiguration;

/**
 * A simple POJO to capture both system level as well as user level configurations
 * @author ganga
 *
 */
class ConfigPojo {

	private XMLConfiguration configuration;
	private XMLConfiguration userConfiguration;
	
	ConfigPojo(XMLConfiguration configuration, XMLConfiguration userConfiguration) {
		this.configuration = configuration;
		this.userConfiguration = userConfiguration;
	}
	/**
	 * @return the userConfiguration
	 */
	XMLConfiguration getUserConfiguration() {
		return userConfiguration;
	}
	
	/**
	 * @return the configuration
	 */
	XMLConfiguration getConfiguration() {
		return configuration;
	}
	
	
}
