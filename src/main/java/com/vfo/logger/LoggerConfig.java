package com.vfo.logger;

import com.vfo.utilities.Commons;
import static com.google.common.base.Preconditions.checkState;

/**
 * Represents the configuration associated with a logger
 * @author ganga
 *
 */
public class LoggerConfig {

	private String logFolder;
	private String logLevel;
	private String loggerName;
	private String[] ignoreList = new String[]{};
	private String logFileName;
	private String fileSize = "5MB";
	private String filePattern;
	

	/**
	 * @param logFileName the logFileName to set
	 */
	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	
	/**
	 * @return - The Log File Name
	 */
	public String getLogFileName() {
		return logFileName;
	}

	/**
	 * @return the logFolder
	 */
	public String getLogFolder() {
		return logFolder;
	}

	/**
	 * @param logFolder the logFolder to set
	 */
	public void setLogFolder(String logFolder) {
		this.logFolder = logFolder;
	}

	/**
	 * @return the logLevel
	 */
	public String getLogLevel() {
		return logLevel;
	}

	/**
	 * @param logLevel the logLevel to set
	 */
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * @return the loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * @param loggerName the loggerName to set
	 */
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	/**
	 * @return the ignoreList
	 */
	public String[] getIgnoreList() {
		return ignoreList;
	}

	/**
	 * @param ignoreList the ignoreList to set
	 */
	public void setIgnoreList(String... ignoreList) {
		this.ignoreList = ignoreList;
	}

	/**
	 * @return the fileSize
	 */
	public String getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the filePattern
	 */
	public String getFilePattern() {
		return filePattern;
	}

	/**
	 * @param filePattern the filePattern to set
	 */
	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}

	/**
	 * Helps validate the mandatory parameters which are absolutely necessary for the 
	 * configuration to be treated as valid and be used
	 */
	public final void validateInternals() {
		checkState(Commons.isNotNullAndNotEmpty(logFolder), "Log file name cannot be null or empty");
	}
}
