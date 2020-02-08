package com.dezzy.postfix.math.symbolic.structure;

import java.util.Map;

/**
 * The simplest form of an expression; a known value. Instead of just using doubles, a separate class was created
 * to hold known values so that they could also be treated as {@link Expression Expressions}.
 * 
 * @author Joe Desmond
 */
public final class Value implements Expression {
	
	/**
	 * The value
	 */
	public final double value;
	
	/**
	 * Constructs a value object with the given known value.
	 * 
	 * @param _value known value
	 */
	public Value(final double _value) {
		value = _value;
	}
	
	/**
	 * Returns this value.
	 * 
	 * @param constants known constants, ignored when evaluating a Value
	 * @return double value
	 */
	@Override
	public final double evaluate(Map<String, Double> constants) {
		return value;
	}
	
	/**
	 * Returns true, because a value can always be evaluated.
	 * 
	 * @param constants known constants, ignored for Values
	 * @return true
	 */
	@Override
	public boolean canEvaluate(final Map<String, Double> constants) {
		return true;
	}
	
	/**
	 * Returns this Value, because a value is already simplified.
	 * 
	 * @param constants known constants, ignored for Values
	 * @return this
	 */
	@Override
	public Expression simplify(final Map<String, Double> constants) {
		return this;
	}
	
	/**
	 * Returns this value, converted to a String.
	 * 
	 * @return String representation of a double value
	 */
	@Override
	public final String toString() {
		return Double.toString(value);
	}
}
