package com.dezzy.postfix.math.symbolic.constants;

import com.dezzy.postfix.math.symbolic.structure.Expression;

/**
 * A constant expression, and a boolean flag that dictates if this expression should be
 * reduced into a single Value.
 * 
 * @author Joe Desmond
 */
public final class Constant {
	
	/**
	 * Constant expression
	 */
	public final Expression expression;
	
	/**
	 * True if {@link #expression} should be reduced, false if it should be left unsimplified. This
	 * flag is useful for defining constants such as <code>i = sqrt(-1)</code>, which cannot be simplified.
	 */
	public final boolean reduce;
	
	/**
	 * Creates a Constant with the given expression, that should be reduced. Equivalent to calling
	 * {@link Constant#Constant(Expression, boolean) Constant(_expression, true)}.
	 * 
	 * @param _expression constant expression
	 */
	public Constant(final Expression _expression) {
		expression = _expression;
		reduce = true;
	}
	
	/**
	 * Creates a Constant with the given expression and reduce flag.
	 * 
	 * @param _expression constant expression
	 * @param _reduce true if this expression should be simplified by other methods
	 */
	public Constant(final Expression _expression, final boolean _reduce) {
		expression = _expression;
		reduce = _reduce;
	}
}
