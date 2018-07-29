package com.vfo.logger;

import java.io.File;

import org.slf4j.Logger;

import com.vfo.config.ConfigurationProperty;
import com.vfo.config.VfoConfig;

public class VfoLogger implements ILoggerConfig{

	public static Logger getLogger() {
		return VfoLoggerFactory.newInstance(VfoLogger.class);
	}

	public LoggerConfig newConfig() {
		LoggerConfig config = new LoggerConfig();
		config.setIgnoreList("org.apache", "org.eclipse.jetty");
		config.setLogLevel(VfoConfig.getStringProperty(ConfigurationProperty.LOG_LEVEL));  
		config.setLoggerName(getClass().getSimpleName());
		String loc = "output" + File.separator + VfoConfig.getStringProperty(ConfigurationProperty.LOG_FILE) + File.separator;
		config.setLogFolder(loc);
		config.setLogFileName("ufo.log");
		
		return config;
		
	}
}
