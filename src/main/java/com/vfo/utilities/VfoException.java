package com.vfo.utilities;

/**
 * This class can be used to throw a custom exception or wrap an existing
 * exception for better logging
 * @author ganga
 *
 */
public class VfoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Used to throw if any anomalies are found.
	 * @param message Custom error message to be thrown
	 */
	public VfoException(String message) {
		super(message);
	}
	
	/**
	 * Used to wrap and thrown an exception found during execution
	 * @param message Custom error message to be thrown
	 * @param cause Throwable cause
	 */
	public VfoException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Used to wrap and throw an exception found during execution
	 * @param message Custom error message to be thrown
	 * @param cause Thrown cause
	 */
	public VfoException(String message, Exception exception) {
		super (message, exception);
	}
}
