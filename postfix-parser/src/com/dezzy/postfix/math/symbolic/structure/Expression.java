package com.dezzy.postfix.math.symbolic.structure;

import java.util.Map;

/**
 * Identifies any mathematical expression, in symbolic form, that can ultimately be evaluated.
 * Implemented by {@link Value}, {@link Unknown}, and {@link SymbolicResult}.
 * 
 * @author Joe Desmond
 */
public interface Expression {
	
	/**
	 * Evaluates this mathematical expression.
	 * 
	 * @param constants maps known constants to values
	 * @return the value of this expression
	 */
	public double evaluate(final Map<String, Double> constants);
	
	/**
	 * Returns true if the expression can be evaluated given the known constants.
	 * 
	 * @param constants known constants
	 * @return true if the expression can be evaluated
	 */
	public boolean canEvaluate(final Map<String, Double> constants);
	
	/**
	 * Returns a new Expression that is a simplified copy of this one.
	 * 
	 * @param constants known constants
	 * @return simplified version of this Expression
	 */
	public Expression simplify(final Map<String, Double> constants);
	
	/**
	 * Returns this expression in a readable form.
	 * 
	 * @return readable form of this expression
	 */
	public String toString();
}
