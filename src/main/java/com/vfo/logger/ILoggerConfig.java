package com.vfo.logger;

/**
 * Logger config interface hosts default values for {@link LoggerConfig}
 * @author ganga
 *
 */
public interface ILoggerConfig {

	default LoggerConfig newConfig(){
		return null;
	}

	/**
	 * Logger config for default usage.
	 * 
	 * @param class
	 * @return the default logger config
	 */
	default LoggerConfig newConfig(Class<?> clazz) {
		
		
		return null;
		
	}

}
