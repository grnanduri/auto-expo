/**
 * 
 */
package com.vfo.config;

import java.util.List;

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
	DEFAULT_TIMEOUT("timeout", "30", OPTIONAL)
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
	
	
}
