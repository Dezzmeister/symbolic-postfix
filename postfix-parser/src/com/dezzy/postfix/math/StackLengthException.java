package com.dezzy.postfix.math;

/**
 * Thrown when a {@link Parser Parser's} stack does not have enough elements to perform an operation.
 * 
 * @author Joe Desmond
 */
public class StackLengthException extends RuntimeException {

	/**
	 * Serialization UID
	 */
	private static final long serialVersionUID = -6224227877850132336L;
	
	/**
	 * Constructs a StackLengthException with the given message.
	 * 
	 * @param message exception message
	 */
	public StackLengthException(final String message) {
		super(message);
	}
}
