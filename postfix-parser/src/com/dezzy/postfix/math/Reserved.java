package com.dezzy.postfix.math;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.dezzy.postfix.auxiliary.BiMap;

/**
 * Reserved/predetermined operators, functions, and constants.
 * 
 * @author Joe Desmond
 */
public final class Reserved {
	
	/**
	 * Maps String operator tokens to operation functions
	 */
	public static final BiMap<String, Operation> operations;
	
	/**
	 * Maps String function names to functions
	 */
	public static final BiMap<String, Function> functions;
	
	/**
	 * Math constants
	 */
	public static final Map<String, Double> constants;
	
	static {
		operations = getOperations();
		functions = getFunctions();
		constants = getConstants();
	}
	
	/**
	 * Returns a mapping of string operators to mathematical operations.
	 * 
	 * @return operations
	 */
	private static final BiMap<String, Operation> getOperations() {
		final BiMap<String, Operation> out = new BiMap<String, Operation>(new HashMap<String, Operation>(), new HashMap<Operation, String>());
		
		out.put("+", Operation.add);
		out.put("-", Operation.subtract);
		out.put("*", Operation.multiply);
		out.put("/", Operation.divide);
		out.put("^", Operation.power);
		out.put("%", Operation.modulo);
		
		return out;
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
	 * Returns a mapping of function names to their actual functions. Gets function names by using reflection 
	 * to look through {@link Function} for function declarations. 
	 * 
	 * @return mathematical functions
	 */
	private static final BiMap<String, Function> getFunctions() {
		final BiMap<String, Function> out = new BiMap<String, Function>(new HashMap<String, Function>(), new HashMap<Function, String>());
		
		final Field[] fields = Function.class.getDeclaredFields();
		
		for (final Field field : fields) {
			if (field.getType() == Function.class) {
				try {
					final String name = field.getName();
					final Function function = (Function) field.get(null);
					
					out.put(name, function);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
		final String token = operations.inverseGet(operation);
		
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
		allConstants.putAll(Reserved.constants);
		allConstants.putAll(additionalConstants);
		
		return allConstants;
	}
}
