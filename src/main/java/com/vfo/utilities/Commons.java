package com.vfo.utilities;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.google.common.io.Resources;
import com.vfo.logger.VfoLogger;

/**
 * Commons class contains standard or often used short utils
 * @author ganga
 *
 */
public final class Commons {

	private static Pattern linePattern = Pattern.compile(".*\r?\n");
	private static Pattern pattern;
	private static final Logger LOG = VfoLogger.getLogger();
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	private static final CharsetDecoder decoder = UTF8_CHARSET.newDecoder();
	private static final AtomicLong LAST_TIME_MS = new AtomicLong();
	
	private Commons() {}
	
	/**
	 * Check if passed input is NULL or Empty
	 * 
	 * @param text
	 * String to be checked
	 * @return true/false
	 */
	public static boolean isNullOrEmpty(String text) {
		return text == null || text.isEmpty();
	}
	
	/**
	 * Check if passed input is NOT NULL and NOT Empty
	 * 
	 * @param text
	 * String to be checked
	 * @return true/false
	 */
	public static boolean isNotNullAndNotEmpty(String text) {
		return text != null || !text.isEmpty();
	}
	
	/**
	 * Check if passed object is null
	 * @param obj	Object to be verified
	 * @return 		True if null/false if not null
	 */
	public static boolean isNull(Object obj) {
		return obj == null;
	}

	public static String fetchFileUsingAbsolutePath(String resourcePath) {
		String resPath;
		URL res = Resources.getResource(resourcePath);
		try {
			resPath = res.toURI().getPath();
		} catch (URISyntaxException e) {
			throw new VfoException("Error in fetching file path", e);
		}
		return resPath;
		
	}
}
