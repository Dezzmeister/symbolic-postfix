package com.dezzy.postfix.math;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A postfix parser.
 * 
 * @author Joe Desmond
 */
public class Parser {
	
	/**
	 * Maps String operator tokens to operation functions
	 */
	protected static final Map<String, Operation> operations;
	
	/**
	 * Maps operation functions to String operator tokens
	 */
	protected static final Map<Operation, String> reverseOperations;
	
	/**
	 * Math constants
	 */
	private static final Map<String, Double> constants;
	
	static {
		operations = getOperations();
		reverseOperations = getReverseOperations(operations);
		constants = getConstants();
	}
	
	/**
	 * Returns a mapping of symbolic names to known mathematical constants. <br>
	 * Example: <code>"pi"</code> maps to {@link Math#PI}.
	 * 
	 * @return mathematical constants
	 */
	private static final HashMap<String, Double> getConstants() {
		final HashMap<String, Double> out = new HashMap<String, Double>();
		
		out.put("e", Math.E);
		out.put("pi", Math.PI);
		
		return out;
	}
	
	/**
	 * Returns a mapping of string operators to mathematical operations.
	 * 
	 * @return operations
	 */
	private static final HashMap<String, Operation> getOperations() {
		final HashMap<String, Operation> out = new HashMap<String, Operation>();
		
		out.put("+", Operation.add);
		out.put("-", Operation.subtract);
		out.put("*", Operation.multiply);
		out.put("/", Operation.divide);
		out.put("^", Operation.power);
		out.put("%", Operation.modulo);
		
		return out;
	}
	
	/**
	 * Returns a mapping of mathematical operations to String operators.
	 * 
	 * @param operations (operator identifier -> operation)
	 * @return (operation -> operator identifier)
	 */
	private static final HashMap<Operation, String> getReverseOperations(final Map<String, Operation> operations) {
		final HashMap<Operation, String> out = new HashMap<Operation, String>();
		
		for (Entry<String, Operation> entry : operations.entrySet()) {
			out.put(entry.getValue(), entry.getKey());
		}
		
		return out;
	}
	
	/**
	 * Finds an operator identifier given the mathematical operation that it represents.
	 * 
	 * @param operation mathematical operation
	 * @return operator identifier
	 */
	public static final String operationTokenLookup(final Operation operation) {
		final String token = reverseOperations.get(operation);
		
		if (token == null) {
			throw new UnrecognizedSymbolException("No token exists for the specified operation!");
		} else {
			return token;
		}
	}
	
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
		allConstants.putAll(constants);
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
			final Operation potentialOperation = operations.get(token);
			
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
		return evalWithConstants(constants);
	}
}
