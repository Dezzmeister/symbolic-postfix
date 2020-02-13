package com.dezzy.postfix.math.symbolic.structure;

import java.util.Map;
import java.util.Objects;

/**
 * The simplest form of an expression; a known value. Instead of just using doubles, a separate class was created
 * to hold known values so that they could also be treated as {@link Expression Expressions}.
 * 
 * @author Joe Desmond
 */
public final class Value implements Expression {
	
	/**
	 * Zero
	 */
	public static final Value ZERO = new Value(0);
	
	/**
	 * One
	 */
	public static final Value ONE = new Value(1);
	
	/**
	 * Negative one
	 */
	public static final Value NEG_ONE = new Value(-1);
	
	/**
	 * Used internally to check for equality, specifies the precision of the check.
	 * Accounts for floating-point errors after calculations. <br>
	 * Can be set with {@link #setAcceptedError(double)}
	 */
	private static double epsilon = 1e-6;
	
	/**
	 * Sets the accepted error when checking for equality between this and other values.
	 * 
	 * @param _epsilon precision
	 */
	public static void setAcceptedError(final double _epsilon) {
		epsilon = _epsilon;
	}
	
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
	 * Returns the derivative of this Value with respect to <code>varName</code>,
	 * which is zero because the derivative of a constant is always zero.
	 * 
	 * @param varName variable name, ignored
	 * @return zero
	 */
	@Override
	public Expression derivative(final String varName) {
		return ZERO;
	}
	
	/**
	 * Returns false, because a value cannot be a function of a variable.
	 * 
	 * @param varName variable name (ignored)
	 * @return false
	 */
	@Override
	public boolean isFunctionOf(final String varName) {
		return false;
	}
	
	/**
	 * Returns true, because a Value is a constant.
	 * 
	 * @param known constants, ignored
	 * @return true
	 */
	@Override
	public boolean hasConstantTerm(final Map<String, Double> constants) {
		return true;
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
	
	/**
	 * Returns this Value, converted to a String. For Values, the LaTeX representation
	 * is equivalent to the plain String representation. If the value is close enough
	 * (within {@link #epsilon}) to its rounded version, a String representation of the 
	 * rounded value will be returned instead, which will remove any extra digits after
	 * the decimal that may otherwise be returned by {@link Double#toString(double)}.
	 * 
	 * @param latexMappings user-specified LaTeX mappings, ignored
	 * @return LaTeX representation
	 */
	@Override
	public final String toLatex(final Map<String, String> latexMappings) {
		if (equalsWithin(value, Math.round(value), epsilon)) {
			return Long.toString(Math.round(value));
		} else {
			return Double.toString(value);
		}
	}
	
	/**
	 * Returns true if these two Values have the same numeric value, within 
	 * +/- {@link #epsilon}.
	 * 
	 * @param other other Value
	 * @return true if these Values are equal
	 */
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Value)) {
			return false;
		} else {
			final Value otherValue = (Value) other;
			return equalsWithin(value, otherValue.value, epsilon);
		}
	}
	
	/**
	 * Returns true if this value is an integer, within {@link #epsilon}. This check is
	 * performed by comparing the true value to the closest integer (rounded value), and
	 * determining if they are equal within {@link #epsilon}.
	 * 
	 * @return true if this value is close enough to an integer
	 */
	public boolean isInteger() {
		return equalsWithin(value, Math.round(value), epsilon);
	}
	
	/**
	 * Returns true, because a Value is a simple mathematical unit.
	 * 
	 * @return true
	 */
	@Override
	public boolean isSimple() {
		return true;
	}
	
	/**
	 * Computes the hashcode of this Value by hashing its numeric value.
	 * 
	 * @return hashcode of this Value
	 */
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
	/**
	 * Returns true if <code>d0</code> and <code>d1</code> and equal within <code>epsilon</code>.
	 * 
	 * @param d0 first number
	 * @param d1 second number
	 * @param epsilon precision
	 * @return true if <code>d0 == d1</code> within <code>epsilon</code>
	 */
	private final boolean equalsWithin(final double d0, final double d1, final double epsilon) {
		return (Math.abs(d0 - d1)) < epsilon;
	}
}
