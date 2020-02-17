package com.dezzy.postfix.math;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.dezzy.postfix.auxiliary.BiMap;
import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Value;

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
	public static final Map<String, Constant> constants;
	
	/**
	 * Empty constants map
	 */
	public static final Map<String, Constant> emptyConstants;
	
	/**
	 * Mapping of mathematical constant names (internal representations) to their LaTeX representations
	 */
	public static final Map<String, String> latexConstants;
	
	static {
		operations = getOperations();
		functions = getFunctions();
		constants = getConstants();
		emptyConstants = new HashMap<String, Constant>();
		latexConstants = getLatexConstants();
	}
	
	/**
	 * Returns a mapping of string operators to mathematical operations.
	 * 
	 * @return operations
	 */
	private static final BiMap<String, Operation> getOperations() {
		final BiMap<String, Operation> out = new BiMap<String, Operation>(new HashMap<String, Operation>(), new HashMap<Operation, String>());
		
		out.put("+", Operation.ADD);
		out.put("-", Operation.SUBTRACT);
		out.put("*", Operation.MULTIPLY);
		out.put("/", Operation.DIVIDE);
		out.put("^", Operation.POWER);
		
		return out;
	}
	
	/**
	 * Returns a mapping of symbolic names to known mathematical constants. <br>
	 * Example: <code>"pi"</code> maps to {@link Math#PI}.
	 * 
	 * @return mathematical constants
	 */
	private static final HashMap<String, Constant> getConstants() {
		final HashMap<String, Constant> out = new HashMap<String, Constant>();
		
		out.put("e", new Constant(Value.E));
		out.put("pi", new Constant(Value.PI));
		
		return out;
	}
	
	/**
	 * Returns a mapping of symbolic names of known mathematical constants to LaTeX representations
	 * of those constants.. <br>
	 * Example: <code>"pi"</code> maps to <code>"\pi"</code>
	 * 
	 * @return mathematical constant LaTeX mappings
	 */
	private static final HashMap<String, String> getLatexConstants() {
		final HashMap<String, String> out = new HashMap<String, String>();
		
		out.put("e", "e");
		out.put("pi", "\\pi");
		
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
	public static final Map<String, Constant> getCompleteConstantsMap(final Map<String, Constant> additionalConstants) {
		final Map<String, Constant> allConstants = new HashMap<String, Constant>();
		allConstants.putAll(Reserved.constants);
		allConstants.putAll(additionalConstants);
		
		return allConstants;
	}
	
	/**
	 * Returns a complete list of special LaTeX mappings, including those for known mathematical constants (in {@link #latexConstants})
	 * as well as those defined in <code>additionalMappings</code>.
	 * 
	 * @param additionalMappings additional, user-defined LaTeX representations of named constants and variables
	 * @return a complete mapping of named constants and variables to their respective LaTeX representations, if the default representation will not suffice
	 */
	public static final Map<String, String> getCompleteLatexMappings(final Map<String, String> additionalMappings) {
		final Map<String, String> allMappings = new HashMap<String, String>();
		allMappings.putAll(Reserved.latexConstants);
		allMappings.putAll(additionalMappings);
		
		return allMappings;
	}
}
