package com.vfo.config;

import java.util.Map;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;

import com.vfo.internal.customization.BehaviorRegistry;
import com.vfo.utilities.Commons;
import com.vfo.utilities.YamlHandler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkNotNull;

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

	private static void updateConfigUsingMap(Map<String, String> config) {
		ConfigurationHelper.updateConfigUsingMap(new ConfigPojo(configuration, userConfiguration), config);
	}
	
	private static synchronized void initGlobalConfig(Map<String, String> config) {
		if(!initialized) {
			updateConfigUsingMap(config);
			ConfigurationHelper.updateConfigUsingJVM(configuration);
			initialized = true;
		}
	}
	
	static XMLConfiguration initSuiteConfig(Map<String, String> config) {
		initGlobalConfig(config);
		return ConfigurationHelper.createUsing(configuration, config);
	}
	
	static XMLConfiguration initTestConfig(XMLConfiguration configuration, Map<String, String> config) {
		return ConfigurationHelper.createUsing(configuration, config);
	}
	
	/**
	 * Sets the given {@link ConfigurationProperty} with the boolean value
	 * @param property 		The {@link ConfigurationProperty}
	 * @param configValue	The value to be set
	 */
	static void setBooleanConfigProperty(ConfigurationProperty property, boolean configValue) {
		setRawConfigProperty(property, configValue);
	}
	
	/**
	 * Sets the given {@link ConfigurationProperty} with the value
	 * @param property 		The {@link ConfigurationProperty}
	 * @param configValue	The value to be set
	 */
	static void setStringConfigProperty(ConfigurationProperty property, String configValue) {
		setRawConfigProperty(property, configValue);
	}
	
	public static boolean getBoolProperty(ConfigurationProperty property) {
		checkState(property != ConfigurationProperty.USER_CONFIG, "User configurations should be queried"
				+ "using getUserConfig() method");
		if(property.isSkipValidation()) {
			return getConfiguration().getBoolean(property.getPropertyName());
		}
		return getConfigAfterValidation().getBoolean(property.getPropertyName());
	}
	
	public static int getIntProperty(ConfigurationProperty property) {
		checkState(property != ConfigurationProperty.USER_CONFIG, "User configurations should be queried"
				+ "using getUserConfig() method");
		if(property.isSkipValidation()) {
			return getConfiguration().getInt(property.getPropertyName());
		}
		return getConfigAfterValidation().getInt(property.getPropertyName());
	}
	
	public static String getUserConfig(String key) {
		checkArgument(key != null && !key.trim().isEmpty(), "Key cannot be null (or) empty.");
		return userConfiguration.getString(key);
	}
	
	private static synchronized void setRawConfigProperty(ConfigurationProperty property, Object value) {

		checkArgument(property != null, "Properties cannot be null.");
		checkState(!property.isReadOnly(), "Cannot alter property " + property.getPropertyName() + ", because it is marked READ ONLY.");
		configuration.setProperty(property.getPropertyName(), value);
		
	}
	
	public static String getStringPropertyOnlyIfNotEmpty(ConfigurationProperty property) {
		String val = getStringProperty(property);
		checkArgument(!val.isEmpty(), emptyOrNull(property));
		return val;
	}
	
	private static void validateConfiguration() {
		XMLConfiguration configuration = getConfiguration();
		for (ConfigurationProperty property : ConfigurationProperty.values()) {
			if(!property.isMandatory()) {
				//Interested only Mandatory parameters
				continue;
			}
			String key = property.getPropertyName();
			if(!configuration.containsKey(key)) {
				throw new IllegalStateException(missing(property));
			}
			if(Commons.isNullOrEmpty(configuration.getString(key))){
				throw new IllegalStateException(emptyOrNull(property));
			}
		}
	}

	private static String emptyOrNull(ConfigurationProperty property) {
		
		return property.getErrorMsg("empty or null");
	}

	private static String missing(ConfigurationProperty property) {

		return property.getErrorMsg("missing");
	}

	private static XMLConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getStringProperty(ConfigurationProperty property) {
		checkNotNull(property, "The Configuration cannot be null");
		checkState(property != ConfigurationProperty.USER_CONFIG, "User configuration should be queried using"
				+ "getUserConfig() method");
		if(property.isSkipValidation()) {
			return getConfiguration().getString(property.getPropertyName().trim());
		}
		return getConfigAfterValidation().getString(property.getPropertyName().trim());
	}
	
	/**
	 * Gets the value of the given key from the {@link ConfigurationProperty#ENV_CONFIG_LOCATION}.
	 * Since the key base is set to the {@link ConfigurationProperty#ENVIRONMENT}, the key need not to be prefixed
	 * with environment name.
	 * 
	 * @param key 	The valid key for the value to be retrieved from the environment config file
	 * @return 		The value object of the supplied key
	 */
	public static Object getValueFromEnvConfigFile(String key) {
		if (Commons.isNull(yamlHandler)) {
			String envFile = getStringPropertyOnlyIfNotEmpty(ConfigurationProperty.ENV_CONFIG_LOCATION);
			String env = getStringPropertyOnlyIfNotEmpty(ConfigurationProperty.ENVIRONMENT);
			yamlHandler = new YamlHandler(envFile);
			yamlHandler.setKeyBase(env);
		}
		return yamlHandler.fetchObject(key);
	}

	private static XMLConfiguration getConfigAfterValidation() {
		//We have to explicitly check if the configuration contains our key and ONLY then try
		//accessing it, else we are going to trigger missing property exceptions.
		if(configuration.containsKey(VALIDATED) && configuration.getBoolean(VALIDATED)) {
			return getConfiguration();
		}
		validateConfiguration();
		synchronized (VfoConfig.configuration) {
			VfoConfig.configuration.addProperty(VALIDATED, true);
		}
		return getConfiguration();
	}
}
