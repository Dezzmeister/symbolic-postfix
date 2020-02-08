package com.dezzy.postfix.math;

/**
 * Thrown when a symbol cannot be resolved while evaluating an expression.
 * 
 * @author Joe Desmond
 */
public class UnrecognizedSymbolException extends IllegalArgumentException {
	
	/**
	 * Serialization UID
	 */
	private static final long serialVersionUID = 2574835657035962750L;
	
	/**
	 * Constructs an UnrecognizedSymbolException with the given message.
	 * 
	 * @param _message exception message
	 */
	public UnrecognizedSymbolException(final String _message) {
		super(_message);
	}
}
