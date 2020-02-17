package com.dezzy.postfix.math.symbolic.structure;

import java.util.List;
import java.util.Map;

import com.dezzy.postfix.math.symbolic.constants.Constant;

/**
 * Identifies any mathematical expression, in symbolic form, that can ultimately be evaluated.
 * Implemented by {@link Value}, {@link Unknown}, {@link SymbolicResult}, and {@link SymbolicFunction}.
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
	public double evaluate(final Map<String, Constant> constants);
	
	/**
	 * Returns true if this Expression can be evaluated given the known constants.
	 * 
	 * @param constants known constants
	 * @return true if the expression can be evaluated
	 */
	public boolean canEvaluate(final Map<String, Constant> constants);
	
	/**
	 * Returns a new Expression that is a simplified copy of this one.
	 * 
	 * @param constants known constants
	 * @return simplified version of this Expression
	 */
	public Expression simplify(final Map<String, Constant> constants);
	
	/**
	 * Returns true if this Expression is a function of the specified variable.
	 * 
	 * @param varName variable name
	 * @return true if this function is a function of the given variable
	 */
	public boolean isFunctionOf(final String varName);
	
	/**
	 * Returns the derivative of this Expression with respect to the specified variable.
	 * 
	 * @param varName variable to differentiate with respect to
	 * @return the derivative of this expression with respect to <code>varName</code>
	 */
	public Expression derivative(final String varName);
	
	/**
	 * Returns true if this Expression has a constant term.
	 * 
	 * @param known constants
	 * @return true if this Expression has a constant
	 */
	public boolean hasConstantTerm(final Map<String, Constant> constants);
	
	/**
	 * Returns true if this Expression is a simple mathematical unit (a constant or a variable).
	 * 
	 * @return true if this Expression is a {@link Value} or an {@link Unknown}
	 */
	public boolean isSimple();
	
	/**
	 * Converts decimals to known constants, or if no constant exists for the decimal,
	 * converts it into a fraction.
	 * 
	 * @param constants known constants
	 * @return a new version of this Expression without decimals
	 */
	public Expression cleanDecimals(final Map<String, Constant> constants);
	
	/**
	 * Returns a list of all unknowns in this Expression, given the known constants. <br>
	 * This method does not return all {@link Unknown Unknowns}, only those that are not
	 * defined by <code>constants</code>.
	 * 
	 * @param constants known constants
	 * @return list of all unknowns
	 */
	public List<Unknown> getUnknowns(final Map<String, Constant> constants);
	
	/**
	 * Returns this expression in a readable form.
	 * 
	 * @return readable form of this expression
	 */
	public String toString();
	
	/**
	 * Converts this Expression into a LaTeX string. Allows any constant or variable
	 * to have a special LaTeX representation through <code>latexMappings</code>. For example,
	 * an {@link Unknown} with <code>"Phi"</code> may map to <code>"\Phi"</code>
	 * 
	 * @param latexMappings maps constant names to their LaTeX representations 
	 * @return LaTeX representation of this expression
	 */
	public String toLatex(final Map<String, String> latexMappings);
	
	/**
	 * Returns true if this expression is equivalent to another before performing any
	 * simplification. This method may return false even though two expression are mathematically
	 * equal, this method only checks for obvious mathematical equality.
	 * 
	 * @param other other Expression
	 * @return true if these Expressions are obviously equal
	 */
	public boolean equals(final Object other);
	
	/**
	 * Computes the hashcode of this expression, based on values used to check for equality.
	 * 
	 * @return hashcode of this expression
	 */
	public int hashCode();
}
