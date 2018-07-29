package com.vfo.logger;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import org.slf4j.LoggerFactory;

import com.vfo.config.ConfigurationProperty;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;

/**
 * Represents the bare essentials that are required for building a logger.
 * <br> End users will create an implementation of {@link ILoggerConfig} and pass it to {@link VfoLoggerFactory#newInstance(Class)}
 * Example: <pre>
 * <code>import org.slf4j.Logger;
 * import com.vfo.logger.ILoggerConfig;
 * import com.vfo.logger.VfoLoggerFactory;
 * 
 * public class AppLogger implements ILoggerConfig {
 * 		public static Logger getLogger() {
 * 			return VfoLoggerFactory.newInstance(AppLogger.class);
 * 		}
 * }
 *</code>
 *</pre>
 *From any class, create a logger object as,
 *<pre>
 *	<code> private final Logger LOG = AppLogger.getLogger(); </code>
 *</pre>
 *
 * @author ganga
 *
 */
public class VfoLoggerFactory {

	private static Map<String, Logger> loggerMap= new ConcurrentHashMap<>();
	private static final Object myLock = new Object();
	
	private static Logger newLogger(LoggerConfig config) {
		config.validateInternals();
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = loggerContext.getLogger(config.getLoggerName());
		logger.setAdditive(false);
		Level level = Level.toLevel(System.getProperty(ConfigurationProperty.LOG_LEVEL.getJVMArgumentName(), config.getLogLevel()));
		logger.setLevel(level);
		for(String each : config.getIgnoreList()) {
			((Logger) LoggerFactory.getLogger(each)).setLevel(Level.OFF);
		}
		ConsoleAppender<ILoggingEvent> consoleAppender = newConsoleAppender(loggerContext, logger, config);
		if (logger.getAppender(consoleAppender.getName()) == null) {
			logger.addAppender(consoleAppender);
		}
		FileAppender<ILoggingEvent> fileAppender = newFileAppender(loggerContext, logger, config);
		if (logger.getAppender(fileAppender.getName())==null) {
			logger.addAppender(fileAppender);
		}
		return logger;
	}

	private static FileAppender<ILoggingEvent> newFileAppender(LoggerContext loggerContext, Logger logger,
			LoggerConfig config) {

		String file = config.getLogFileName();
		String pattern = config.getFilePattern() == null ? "%d{HH:mm:ss.SSS} %-5level %C.%method\\(\\):%L=> %msg%n" : config.getFilePattern();
		RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
		appender.setName("file_" + logger.getName());
		appender.setFile(normalizeFolderName(config.getLogFolder()) + file);
		appender.setContext(loggerContext);
		appender.setAppend(false);
		appender.setLayout(getLayout(pattern, loggerContext));
		appender.setRollingPolicy(getRollingPolicy(loggerContext, config, appender));
		appender.setTriggeringPolicy(getTriggerPolicy(loggerContext, config.getFileSize()));
		appender.start();
		return appender;
	}

	private static TriggeringPolicy<ILoggingEvent> getTriggerPolicy(LoggerContext loggerContext, String fileSize) {
		SizeBasedTriggeringPolicy<ILoggingEvent> policy = new SizeBasedTriggeringPolicy<>();
		policy.setMaxFileSize(fileSize);
		policy.setContext(loggerContext);
		policy.start();
		return policy;
	}

	private static RollingPolicy getRollingPolicy(LoggerContext loggerContext, LoggerConfig config,
			RollingFileAppender<ILoggingEvent> appender) {
		String folder = normalizeFolderName(config.getLogFolder());
		String file = config.getLogFileName();
		FixedWindowRollingPolicy policy = new FixedWindowRollingPolicy();
		policy.setContext(loggerContext);
		policy.setFileNamePattern(folder + getBaseName(file) + "_%i." + getExtension(file));
		policy.setMinIndex(1);
		policy.setMaxIndex(25);
		policy.setParent(appender);
		policy.start();
		
		return policy;
	}

	private static String getExtension(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getBaseName(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	private static Layout<ILoggingEvent> getLayout(String pattern, LoggerContext loggerContext) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String normalizeFolderName(String logFolder) {
		
		return logFolder.endsWith(File.separator) ? logFolder : logFolder + File.separator; 
	}

	private static ConsoleAppender<ILoggingEvent> newConsoleAppender(LoggerContext loggerContext, Logger logger,
			LoggerConfig config) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Fetch a new Instance of Logger Object
	 * 
	 * @param clazz object which implements {@link ILoggerConfig}
	 * @return {@link Logger} instance
	 */
	public static Logger newInstance(Class<? extends ILoggerConfig> clazz) {
		Logger logger = loggerMap.get(clazz.getName().toLowerCase());
		if (logger != null) {
			return logger;
		}
		synchronized (myLock) {
			try {
				Object object = clazz.newInstance();
				ILoggerConfig config = (ILoggerConfig) object;
				LoggerConfig lConfig = config.newConfig();
				if (lConfig == null){
					lConfig = config.newConfig(clazz);
				}
				logger = VfoLoggerFactory.newLogger(lConfig);
				loggerMap.put(clazz.getName().toLowerCase(), logger);
				return logger;
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
