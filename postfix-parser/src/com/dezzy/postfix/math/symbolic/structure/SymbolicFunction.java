package com.dezzy.postfix.math.symbolic.structure;

import java.util.Map;

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
}