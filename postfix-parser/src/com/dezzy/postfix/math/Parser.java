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
	protected static final Map<String, Double> constants;
	
	static {
		operations = setOperations();
		reverseOperations = setReverseOperations(operations);
		constants = setConstants();
	}
	
	private static final HashMap<String, Double> setConstants() {
		final HashMap<String, Double> out = new HashMap<String, Double>();
		
		out.put("e", Math.E);
		out.put("pi", Math.PI);
		
		return out;
	}
	
	private static final HashMap<String, Operation> setOperations() {
		final HashMap<String, Operation> out = new HashMap<String, Operation>();
		
		out.put("+", Operation.add);
		out.put("-", Operation.subtract);
		out.put("*", Operation.multiply);
		out.put("/", Operation.divide);
		out.put("^", Operation.power);
		out.put("%", Operation.modulo);
		
		return out;
	}
	
	private static final HashMap<Operation, String> setReverseOperations(final Map<String, Operation> operations) {
		final HashMap<Operation, String> out = new HashMap<Operation, String>();
		
		for (Entry<String, Operation> entry : operations.entrySet()) {
			out.put(entry.getValue(), entry.getKey());
		}
		
		return out;
	}
	
	public static final String operationTokenLookup(final Operation operation) {
		final String token = reverseOperations.get(operation);
		
		if (token == null) {
			throw new IllegalArgumentException("No token exists for the specified operation!");
		} else {
			return token;
		}
	}
	
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
	
	public final double eval(final Map<String, Double> additionalConstants) {
		final Map<String, Double> allConstants = new HashMap<String, Double>();
		allConstants.putAll(constants);
		allConstants.putAll(additionalConstants);
		
		return evalWithConstants(allConstants);
	}
	
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
	 * Evaluates the tokens provided to this parser and returns a value.
	 * 
	 * @return evaluation of the provided expression
	 */
	public final double eval() {
		return evalWithConstants(constants);
	}
}
