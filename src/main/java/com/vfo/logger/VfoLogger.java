package com.vfo.logger;

import org.slf4j.Logger;

import com.vfo.config.VfoConfig;

public class VfoLogger implements ILoggerConfig{

	public static Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	public LoggerConfig newConfig() {
		LoggerConfig config = new LoggerConfig();
		config.setIgnoreList("org.apache", "org.eclipse.jetty");
		config.setLogLevel(VfoConfig.getStringProperty(ConfigurationProperty.LOG_LEVEL));  
		
		return config;
		
	}
}
