package com.dezzy.postfix.math.symbolic.structure;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.dezzy.postfix.math.Operation;

/**
 * The simplest form of an expression; a known value. Instead of just using doubles, a separate class was created
 * to hold known values so that they could also be treated as {@link Expression Expressions}.
 * 
 * @author Joe Desmond
 */
public final class Value implements Expression {
	
	/**
	 * 0.
	 * <p>
	 * This constant is declared because it is used often; and the constant version of 0
	 * should be used instead of allocating a new Value.
	 */
	public static final Value ZERO = new Value(0);
	
	/**
	 * 1.
	 * <p>
	 * This constant is declared because it is used often; and the constant version of 1
	 * should be used instead of allocating a new Value.
	 */
	public static final Value ONE = new Value(1);
	
	/**
	 * -1.
	 * <p>
	 * This constant is declared because it is used often; and the constant version of -1
	 * should be used instead of allocating a new Value.
	 */
	public static final Value NEG_ONE = new Value(-1);
	
	/**
	 * 2.
	 * <p>
	 * This constant is declared because it is used often; and the constant version of 2
	 * should be used instead of allocating a new Value.
	 */
	public static final Value TWO = new Value(2);
	
	/**
	 * Euler's number.
	 * <p>
	 * This constant is declared because it is used often; and the constant version of <i>e</i>
	 * should be used instead of allocating a new Value. This constant is referenced in 
	 * {@link com.dezzy.postfix.math.Reserved#constants Reserved.constants}.
	 */
	public static final Value E = new Value(Math.E);
	
	/**
	 * Pi.
	 * <p>
	 * This constant is declared because it is used often; and the constant version of <i>pi</i>
	 * should be used instead of allocating a new Value. This constant is referenced in 
	 * {@link com.dezzy.postfix.math.Reserved#constants Reserved.constants}.
	 */
	public static final Value PI = new Value(Math.PI);
	
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
	public final double evaluate(Map<String, Expression> constants) {
		return value;
	}
	
	/**
	 * Returns true, because a value can always be evaluated.
	 * 
	 * @param constants known constants, ignored for Values
	 * @return true
	 */
	@Override
	public boolean canEvaluate(final Map<String, Expression> constants) {
		return true;
	}
	
	/**
	 * Returns this Value, because a value is already simplified.
	 * 
	 * @param constants known constants, ignored for Values
	 * @return this
	 */
	@Override
	public Expression simplify(final Map<String, Expression> constants) {
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
	public boolean hasConstantTerm(final Map<String, Expression> constants) {
		return true;
	}
	
	/**
	 * Returns this value, converted to a String. If this value {@link #isInteger() is an integer},
	 * rounds it and uses {@link Long#toString(long)} to return a string without decimals.
	 * 
	 * @return String representation of a double value
	 */
	@Override
	public final String toString() {
		if (isInteger()) {
			return Long.toString(Math.round(value));
		} else {
			return Double.toString(value);
		}
	}
	
	/**
	 * If this Value is an integer within {@link #epsilon}, returns a new Value with this value rounded to the nearest integer. <br>
	 * If this Value maps to a known constant within {@link epsilon}, returns a new {@link Unknown} with the name of the constant. <br>
	 * If this Value is not an integer and does not map to a known constant, returns a new {@link SymbolicResult} with this Value
	 * represented as a fraction within {@link #epsilon}.
	 * 
	 * @param constants known constants
	 * @return a version of this Value with either an integer Value, a constant/variable name, or a fraction
	 */
	@Override
	public final Expression cleanDecimals(final Map<String, Expression> constants) {
		for (final Entry<String, Expression> entry : constants.entrySet()) {
			final Expression entryValue = entry.getValue();
			if (equals(entryValue)) {
				return new Unknown(entry.getKey());
			}
		}
		
		if (isInteger()) {
			return new Value(Math.round(value));
		}
		
		final int n = (int) Math.floor(value);
		final double x = value - n;
		
		int lowerN = 0;
		int lowerD = 1;
		
		int upperN = 1;
		int upperD = 1;
		
		while (true) {
			final int middleN = lowerN + upperN;
			final int middleD = lowerD + upperD;
			
			if (middleD * (x + epsilon) < middleN) {
				upperN = middleN;
				upperD = middleD;
			} else if (middleD * (x - epsilon) > middleN) {
				lowerN = middleN;
				lowerD = middleD;
			} else {
				return new SymbolicResult(new Value((n * middleD) + middleN), new Value(middleD), Operation.DIVIDE);
			}
		}
	}
	
	/**
	 * Returns a new empty {@link List}, because a Value has no unknowns.
	 * 
	 * @param constants known constants (ignored)
	 * @return empty {@link List}
	 */
	@Override
	public final List<Unknown> getUnknowns(final Map<String, Expression> constants) {
		return List.of();
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
		if (isInteger()) {
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
