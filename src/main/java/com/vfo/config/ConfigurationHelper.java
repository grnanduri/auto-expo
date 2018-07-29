package com.vfo.config;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.vfo.utilities.Commons;
import com.vfo.utilities.VfoException;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * A simple configuration helper
 * @author ganga
 *
 */
class ConfigurationHelper {

	private ConfigurationHelper() {
		//Utility class, defeat instantiation
	}
	
	/**
	 * @param pojo - A {@link ConfigPojo} that represents the system and user level configuration
	 * 				that needs to be updated
	 * @param config - A {@link Map} that represents the configuration that needs to be read for 
	 * 				doing the update 
	 * @throws Exception 
	 */
	static synchronized void updateConfigUsingMap(ConfigPojo pojo, Map<String, String> config) {
		XMLConfiguration actualConfig = pojo.getConfiguration();
		for (Map.Entry<String, String> entry : config.entrySet()) {
			ConfigurationProperty property = ConfigurationProperty.parse(entry.getKey());
			if (property == null) {
				//It was a property that we don't know. So lets skip it.
				continue;
			}
			if (property.isReadOnly()) {
				//It's a read only. We cannot write into it. So lets skip it.
				throw new IllegalArgumentException(property.getPropertyName()+" is a READ-ONLY parameter. You cannot override it.");
			}
			//We found a property which represents a file.
			//User level configurations would be supported ONLY at the suite level
			
			boolean fileBased = ConfigurationProperty.isFileBasedConfig(property);
			boolean configNotNull = pojo.getUserConfiguration() != null;
			
			if (fileBased && configNotNull) {
				try {
					appendConfigurationsFromFile(pojo.getUserConfiguration(), new File(entry.getValue()));
				} catch (Exception e) {
					String msg = e.getMessage();
					if (e.getMessage().startsWith("Error parsing")) {
						msg = entry.getValue() + " doesn't seem to be a valid XML file.";
					}
					throw new VfoException(msg, e);
				}
				continue;
			}
			//if we are here, we have found a regular property that we need to persist.
			actualConfig.setProperty(property.getPropertyName(), entry.getValue());
		}
	}
	
	/**
	 * @param pojo - A {@link ConfigPojo} that represents the system and user level configuration
	 * 				that needs to be updated
	 * @param config - A {@link Map} that represents the configuration that needs to be read for 
	 * 				doing the update 
	 * @throws Exception 
	 */
	static synchronized void updateConfigUsingJVM(XMLConfiguration configuration) {
		
		for (ConfigurationProperty property : ConfigurationProperty.values()) {
			
			if (property.isReadOnly()) {
				//We're not going to be updating ourselves with READONLY properties
				continue;
			}
			List<String> customNames = property.getCustomNames();
			String value = System.getProperty(property.getPropertyName(),"");
			if (Commons.isNotNullAndNotEmpty(value)) {
				configuration.setProperty(property.getPropertyName(), "");
			}
			//check if we have aliases and if we find any we need to keep updating our config
			for(String customName : customNames) {
				value = System.getProperty(customName, "");
				if(Commons.isNullOrEmpty(value)) {
					continue;
				}
				configuration.setProperty(property.getPropertyName(), value);
				break;
			}
			//finally query the JVM for the new UFO configuration parameters and then update our
			//configuration accordingly. In the near future this will be all that is required.
			value = System.getProperty(property.getJVMArgumentName(), "");
			if(Commons.isNotNullAndNotEmpty(value)) {
				configuration.setProperty(property.getPropertyName(), value);
			}
		}
	}
	
	/**
	 * 
	 * @param userConfiguration
	 * @param file
	 */

	private static void appendConfigurationsFromFile(XMLConfiguration userConfiguration, File file)
		throws ConfigurationException {

		String msg = "User configuration file " + file.getAbsolutePath() + " cannot be found.";
		checkArgument(file.exists(), msg);
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<XMLConfiguration> builder = new FileBasedConfigurationBuilder<>
				(XMLConfiguration.class).configure(params.xml().setFileName(file.getAbsolutePath()));
		userConfiguration.append(builder.getConfiguration());
	}

	static XMLConfiguration createUsing(XMLConfiguration config, Map<String, String> configMap) {
		
		XMLConfiguration xmlConfig = new XMLConfiguration(config);
		ConfigurationHelper.updateConfigUsingMap(new ConfigPojo(xmlConfig, null), configMap);
		ConfigurationHelper.updateConfigUsingJVM(xmlConfig);
		return xmlConfig;
	}
}
