package com.dezzy.postfix.math.symbolic.structure;

import java.util.Map;

/**
 * A mathematical unknown; typically a variable (<code>x,y,a,</code>etc.) or a known constant like <code>pi</code>
 * or <code>e</code>. Can only be evaluated when a mapping of known values is provided.
 * 
 * @author Joe Desmond
 */
public class Unknown implements Expression {
	
	/**
	 * String representation of this unknown
	 */
	public final String varName;
	
	/**
	 * Constructs an Unknown from the given unknown name.
	 * 
	 * @param _varName unknown name, can be a variable name or a mathematical constant
	 */
	public Unknown(final String _varName) {
		varName = _varName;
	}
	
	/**
	 * Attempts to find a mapping for the name of this unknown in the given constants map.
	 * 
	 * @param constants maps String variable/constant names to double values. This is required when evaluating an Unknown
	 * @return value of this unknown
	 * @throws IllegalArgumentException if no mapping exists 
	 */
	@Override
	public final double evaluate(final Map<String, Double> constants) {
		final Double value = constants.get(varName);
		
		if (value == null) {
			throw new IllegalArgumentException("\"" + varName + "\" is unknown!");
		} else {
			return value.doubleValue();
		}
	}
	
	/**
	 * Returns true if the given constants mapping contains a definition for this Unknown.
	 * 
	 * @param constants known constants
	 * @return true if this Unknown is defined in <code>constants</code>
	 */
	@Override
	public boolean canEvaluate(final Map<String, Double> constants) {
		return constants.containsKey(varName);
	}
	
	/**
	 * Simplifies this Unknown. If a mapping for this Unknown exists in <code>constants</code>, returns
	 * a {@link Value} with the value of this unknown. Otherwise, returns this unknown.
	 * 
	 * @param constants known constants
	 * @return a simplified Value, or <code>this</code> if this cannot be simplified
	 */
	@Override
	public Expression simplify(final Map<String, Double> constants) {
		if (canEvaluate(constants)) {
			return new Value(evaluate(constants));
		} else {
			return this;
		}
	}
	
	/**
	 * Returns the symbolic name of this unknown. Does not know or care about the value.
	 * 
	 * @return the symbolic name of this unknown
	 */
	@Override
	public final String toString() {
		return varName;
	}
}
