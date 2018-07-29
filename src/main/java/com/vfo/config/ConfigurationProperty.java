/**
 * 
 */
package com.vfo.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ch.qos.logback.classic.Level;

import static com.vfo.config.ConfigurationProperty.Constants.MANDATORY;
import static com.vfo.config.ConfigurationProperty.Constants.OPTIONAL;
import static com.vfo.config.ConfigurationProperty.Constants.READ_ONLY;
import static com.vfo.config.ConfigurationProperty.Constants.SKIP_VALIDATION;
import static com.vfo.config.ConfigurationProperty.Constants.WRITEABLE;

/**
 * @author ganga
 * This enum represents all the configuration parameters that VFO is aware of
 */
public enum ConfigurationProperty {

	/**
	 * Represents the Name of the Project
	 */
	PROJECT_NAME("projectName", "", MANDATORY, WRITEABLE, "testProject", "project", "PROJECT"),
	/**
	 * Represents the environment against which the tests are being executed
	 */
	ENVIRONMENT("environment", "", MANDATORY, WRITEABLE, "testEnv", "env", "ENVIRONMENT"),
	/**
	 * Represents the location wherein the environment configuration file resides
	 */
	ENV_CONFIG_LOCATION("envConfiguration", "", OPTIONAL),
	/**
	 * Represents the location wherein the core configuration file resides
	 */
	CORE_CONFIG_LOCATION("coreConfiguration", "", OPTIONAL),
	/**
	 * Represents the browser flavor to be used
	 */
	BROWSER("browser", "phantomjs", OPTIONAL, WRITEABLE, "BROWSER"),
	/**
	 * Represents the Team Name for reporting purposes
	 */
	TEAM("team","",MANDATORY),
	/**
	 * Captures the name of the user who is executing the tests
	 */
	USERNAME("user.name", System.getProperty("user.name"), MANDATORY, READ_ONLY),
	/**
	 * The location of a user provided configuration xml file
	 */
	USER_CONFIG("userConfig", "", OPTIONAL),
	/**
	 * Indicates if one should run cucumber tests in parallel or in sequential order.
	 */
	CUCUMBER_PARALLEL("cucumber.parallel", "false", OPTIONAL),
	/**
	 * Indicates if network traffic needs to be captured or not
	 */
	CAPTURE_NETWORK("captureNetwork", "false", OPTIONAL),
	STREAM("stream","", MANDATORY, WRITEABLE, "STREAM"),
	REMOTE_DRIVER("remoteDriver", "false", OPTIONAL),
	LOCATOR_LOCALE("locale", "en_US", OPTIONAL),
	/**
	 * Tags value must not contain irrelevant spaces
	 * To include scenarios having [@t1] the value must be @t1
	 * To include scenarios having [@t1 OR @t2] the value must be @t1,@t2 (notice the comma)
	 * To include scenarios having [@t1 AND @t2] the value must be @t1 @t2 (notice the space)
	 * To include scenarios having [[@t1 OR @t2] AND @t3] the value must be @t1,@t2 @t3
	 */
	TAG("testTags", "", OPTIONAL),
	DEFAULT_TIMEOUT("timeout", "30", OPTIONAL),
	/**
	 * Default log levels
	 */
	LOG_LEVEL("log.level", Level.INFO.toString(), OPTIONAL, WRITEABLE, SKIP_VALIDATION),
	/**
	 * The log file name
	 */
	LOG_FILE("log.file", "Run_" + System.currentTimeMillis(), OPTIONAL, READ_ONLY, SKIP_VALIDATION),
	/**
	 * Represents the output folder
	 */
	OUTPUT_FOLDER("output.folder", "output" + File.separator + LOG_FILE.getValue() + File.separator, MANDATORY, READ_ONLY, SKIP_VALIDATION),
	LINE_SEPARATOR("log.line.separator", StringUtils.repeat("-", 50), OPTIONAL, READ_ONLY, SKIP_VALIDATION),
	/**
	 * The ondemand Service if any
	 */
	ONDEMAND_SERVICE("ondemand.service", "", OPTIONAL),
	/**
	 * The credentials separated by : for the ondemand service given.
	 * This property is always tied to {@link ConfigurationProperty#ONDEMAND_SERVICE}
	 */
	ONDEMAND_CREDENTIALS("ondemand.credentials", "", OPTIONAL),
	/**
	 * provides the remote url host
	 */
	REMOTE_HOST("remote.host", "", OPTIONAL),
	/**
	 * The capability file location (relative from the project's test resources folder) that houses various
	 * browser or mobile flavor combinations
	 */
	CAPABILITY_FILE_LOCATION("capability.file", "", OPTIONAL),
	/**
	 * The capability key to be selected from the given yaml file
	 * This property is always tied to {@link ConfigurationProperty#CAPABILITY_FILE_LOCATION}
	 */
	CAPABILITY_KEY("capability.key", "", OPTIONAL),
	/**
	 * The native app path for the mobile automation
	 */
	APP_PATH("app.path", "", OPTIONAL),
	WATCH_DIRECTORY("watchDirPath", "/target/WatchDoc", OPTIONAL),
	MESSAGE_RETRY_COUNT("retryCount", "3", OPTIONAL),
	MESSAGE_BATCHSIZE("BatchSize", "5", OPTIONAL),
	PROCESSING_TRIGGER_TIMEOUT("batchTriggerTimeout","30000", OPTIONAL),
	/**
	 * To enable proxy for calls. Pass the proxy URL as value.i.e, http://userproxy.visa.com:80
	 */
	PROXY("proxy", "", OPTIONAL)
	;
	
	private final String propertyName;
	private final String value;
	private final boolean mandatory;
	private final boolean readOnly;
	private final boolean skipValidation;
	private List<String> customNames;
	
	interface Constants {
		
		boolean MANDATORY = true;
		boolean OPTIONAL = false;
		boolean READ_ONLY = true;
		boolean WRITEABLE = false;
		boolean SKIP_VALIDATION = true;
	}
	
	ConfigurationProperty(String propertyName, String value, boolean mandatory) {
		this(propertyName, value, mandatory, false);
	}
	
	ConfigurationProperty(String propertyName, String value, boolean mandatory, boolean readOnly) {
		this(propertyName, value, mandatory, readOnly, false);
	}
	
	ConfigurationProperty(String propertyName, String value, boolean mandatory, boolean readOnly, boolean skipValidation) {
		this(propertyName, value, mandatory, readOnly, skipValidation, new String[]{});
	}
	
	ConfigurationProperty(String propertyName, String value, boolean mandatory, boolean readOnly, String... customName) {
		this(propertyName, value, mandatory, readOnly, false, customName);
	}
	
	ConfigurationProperty(String propertyName, String value, boolean mandatory, boolean readOnly, boolean skipValidation, String... customName) {
		this.propertyName = propertyName;
		this.value = value;
		this.mandatory = mandatory;
		this.readOnly = readOnly;
		this.customNames = new ArrayList<>(Arrays.asList(customName));
		this.skipValidation = skipValidation;
	}

	


	/**
	 * @return the customNames
	 */
	public List<String> getCustomNames() {
		return customNames;
	}

	/**
	 * @param customNames the customNames to set
	 */
	public void setCustomNames(List<String> customNames) {
		this.customNames = customNames;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the mandatory
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @return the skipValidation
	 */
	public boolean isSkipValidation() {
		return skipValidation;
	}
	
	public static ConfigurationProperty parse(String rawText) {
		for(ConfigurationProperty prop : values()) {
			if(prop.getPropertyName().equalsIgnoreCase(rawText)) {
				return prop;
			}
			if(caseAgnosticListContains(prop.getCustomNames(), rawText)) {
				return prop;
			}
		}
		return null;
	}

	private static boolean caseAgnosticListContains(List<String> list, String search) {
		boolean contains = false;
		for(String eachEntry : list) {
			if(eachEntry.equalsIgnoreCase(search)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	public String getJVMArgumentName() {
		return "vfo_" + getPropertyName();
	}
	
	public String getAsTestNGParameter() {
		return "<parameter name=\"" + getPropertyName() + "\" value=\"someValue\" />";
	}
	
	/**
	 * Returns the formatted error message
	 * @param suffix - The suffix value to be added along with the error message
	 * @return The formatted error string
	 */
	public String getErrorMsg(String suffix) {
		return String.format("Mandatory parameter [%s] is %s. Please provide it either via the"
				+ " JVM argument [%s] (or) via the TestNG suite XML file as "
				+ " <parameter name = \"%s\" value=\"\"/>", getPropertyName(), suffix, 
				getJVMArgumentName(), getPropertyName());
	}
	
	public static boolean isFileBasedConfig(ConfigurationProperty property) {
		return ((property != null) && (property == USER_CONFIG));
	}
}
