package com.dezzy.postfix.math.symbolic.structure;

import java.util.Map;
import java.util.Objects;

import com.dezzy.postfix.math.Function;
import com.dezzy.postfix.math.Reserved;

/**
 * A symbolic function with one argument.
 * 
 * @author Joe Desmond
 */
public final class SymbolicFunction implements Expression {
	
	/**
	 * Symbolic argument
	 */
	public final Expression argument;
	
	/**
	 * Defined function
	 */
	public final Function function;
	
	/**
	 * Creates a symbolic function with the given argument and function.
	 * 
	 * @param _argument argument expression
	 * @param _function function
	 */
	public SymbolicFunction(final Expression _argument, final Function _function) {
		argument = _argument;
		function = _function;
	}
	
	/**
	 * Attempts to first evaluate the argument, then apply the function to the argument.
	 * 
	 * @param constants known constants
	 * @return value of this function applied to its argument
	 */
	@Override
	public final double evaluate(final Map<String, Double> constants) {
		final double value = argument.evaluate(constants);
		return function.apply(value);
	}
	
	/**
	 * The function is already defined, so this method checks to see if the argument can be evaluated.
	 * 
	 * @param constants known constants
	 * @return true if the argument can be evaluated
	 */
	@Override
	public final boolean canEvaluate(final Map<String, Double> constants) {
		return argument.canEvaluate(constants);
	}
	
	/**
	 * Simplifies the argument. If the argument can be evaluated, returns a {@link Value} with the
	 * result of the function applied to the evaluated simplified argument. If not, returns a new 
	 * version of this symbolic function with the function accepting the simplified argument. 
	 * 
	 * @param constants known constants
	 * @return a simplified version of this function, or a value
	 */
	@Override
	public final Expression simplify(final Map<String, Double> constants) {
		final Expression simplifiedArg = argument.simplify(constants);
		
		if (simplifiedArg.canEvaluate(constants)) {
			final double arg = simplifiedArg.evaluate(constants);
			return new Value(function.apply(arg));
		} else {
			return new SymbolicFunction(simplifiedArg, function);
		}
	}
	
	/**
	 * Returns true if the argument is a function of the given variable.
	 * 
	 * @param varName variable name
	 * @return true if the argument is a function of the given variable
	 */
	@Override
	public boolean isFunctionOf(final String varName) {
		return argument.isFunctionOf(varName);
	}
	
	/**
	 * Symbolically finds the derivative of this function and its applied argument
	 * with respect to the given variable.
	 * 
	 * @param varName variable name
	 * @return derivative expression
	 */
	@Override
	public Expression derivative(final String varName) {
		return function.derivative(argument, varName);
	}
	
	/**
	 * Returns true if the argument to this SymbolicFunction has a constant term.
	 * 
	 * @param constants known constants map
	 * @return true if the argument to this function has a constant term
	 */
	@Override
	public boolean hasConstantTerm(final Map<String, Double> constants) {
		return argument.hasConstantTerm(constants);
	}
	
	/**
	 * Returns false, because a SymbolicFunction is not a simple mathematical unit.
	 * 
	 * @return false
	 */
	@Override
	public boolean isSimple() {
		return false;
	}
	
	/**
	 * Returns a string of the format: <code>func(arg)</code><br>
	 * sin(x): <code>sin(x)</code><br>
	 * abs(x ^ 2): <code>abs((x ^ 2))</code><br>
	 * cos(15.0): <code>cos(15.0)</code>
	 * 
	 * @return a String representation of this symbolic function
	 */
	@Override
	public final String toString() {
		return Reserved.functions.inverseGet(function) + "(" + argument.toString() + ")";
	}
	
	/**
	 * Returns a LaTeX representation of this function and its argument.
	 * 
	 * @param latexMappings user-defined LaTeX representations of named constants and variables
	 * @return LaTeX representation of this function and its argument
	 */
	@Override
	public final String toLatex(final Map<String, String> latexMappings) {
		return function.toLatex(argument, latexMappings);
	}
	
	/**
	 * Returns true if these two SymbolicFunctions are the same function, applied to the same argument.
	 * 
	 * @param other other SymbolicFunction
	 * @return true if these SymbolicFunctions are obviously equal
	 */
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof SymbolicFunction)) {
			return false;
		} else {
			final SymbolicFunction otherFunc = (SymbolicFunction) other;
			return (argument.equals(otherFunc.argument) && function.equals(otherFunc.function));
		}
	}
	
	/**
	 * Returns the hashcode of this SymbolicFunction by hashing its argument and function.
	 * 
	 * @return hashcode of this SymbolicFunction
	 */
	@Override
	public int hashCode() {
		return Objects.hash(argument, function);
	}
}
