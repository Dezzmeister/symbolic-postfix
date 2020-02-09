package com.dezzy.postfix.math;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * A postfix parser.
 * 
 * @author Joe Desmond
 */
public class Parser {
		
	/**
	 * Returns a complete list of constants, including mathematical constants (in {@link #constants}) as well as those defined in
	 * <code>additionalConstants</code>.
	 * 
	 * @param additionalConstants maps variable names to values: for example, a mapping like '<code>r -> 2.0</code>'
	 * 		may exist, if a formula for the volume of a sphere needs to be evaluated
	 * @return a complete constants map
	 */
	public static final Map<String, Double> getCompleteConstantsMap(final Map<String, Double> additionalConstants) {
		final Map<String, Double> allConstants = new HashMap<String, Double>();
		allConstants.putAll(Reserved.constants);
		allConstants.putAll(additionalConstants);
		
		return allConstants;
	}
	
	//=========================================================================
	//						END OF STATIC METHODS
	//=========================================================================
	
	/**
	 * Postfix tokens
	 */
	protected final String[] tokens;
	
	/**
	 * Creates a postfix parser with the given tokens. The tokens can include doubles and operators.
	 * 
	 * @param _tokens tokens
	 */
	public Parser(final String[] _tokens) {
		tokens = _tokens;
	}
	
	/**
	 * Evaluates this Parser's postfix expression given the additional known constants. <br>
	 * Example: The expression may contain an unknown <code>x</code>,
	 * whose value is provided as a mapping in <code>additionalConstants</code>
	 * 
	 * @param additionalConstants additional known constant mappings
	 * @return double value of this expression
	 */
	public final double eval(final Map<String, Double> additionalConstants) {
		final Map<String, Double> allConstants = getCompleteConstantsMap(additionalConstants);
		
		return evalWithConstants(allConstants);
	}
	
	/**
	 * Evaluates this Parser's postfix expression given the known constants. Mathematical constants
	 * such as <code>pi</code> or <code>e</code> must be provided in <code>constants</code>.
	 * 
	 * @param constants known constant mappings
	 * @return double value of this expression
	 */
	protected final double evalWithConstants(final Map<String, Double> constants) {
		final Deque<Double> operands = new ArrayDeque<Double>(tokens.length - 1);
		
		for (int i = 0; i < tokens.length; i++) {
			final String token = tokens[i];
			final Operation potentialOperation = Reserved.operations.get(token);
			
			if (potentialOperation == null) {
				final Double potentialConstant = constants.get(token);
				
				if (potentialConstant == null) {
					final double value = Double.parseDouble(token);
					operands.push(value);
				} else {
					operands.push(potentialConstant);
				}
			} else {
				if (operands.size() < 2) {
					throw new StackLengthException("Not enough operands on the evaluation stack! Index: " + i);
				} else {
					final double d1 = operands.pop();
					final double d0 = operands.pop();
					final double result = potentialOperation.operate(d0, d1);
					
					operands.push(result);
				}
			}
		}
		
		if (operands.isEmpty()) {
			throw new StackLengthException("No final result on the stack!");
		} else {
			return operands.pop();
		}
	}
	
	/**
	 * Evaluates the tokens provided to this parser and returns a value. Substitutes values for
	 * known mathematical constants.
	 * 
	 * @return double value of the provided expression
	 */
	public final double eval() {
		return evalWithConstants(Reserved.constants);
	}
}
