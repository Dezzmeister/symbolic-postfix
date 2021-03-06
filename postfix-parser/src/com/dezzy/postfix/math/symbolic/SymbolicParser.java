package com.dezzy.postfix.math.symbolic;

import java.util.ArrayDeque;
import java.util.Deque;

import com.dezzy.postfix.math.Function;
import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.Reserved;
import com.dezzy.postfix.math.StackLengthException;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicFunction;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Unknown;
import com.dezzy.postfix.math.symbolic.structure.Value;

/**
 * Parses a Postfix expression by using an expression stack to construct a symbolic representation of
 * the expression.
 * 
 * @author Joe Desmond
 */
public class SymbolicParser {
	
	/**
	 * Postfix tokens
	 */
	private final String[] tokens;
	
	/**
	 * Creates a symbolic parser from the given tokens.
	 * 
	 * @param _tokens postfix tokens
	 */
	public SymbolicParser(final String[] _tokens) {
		tokens = _tokens;
	}
	
	/**
	 * Converts the expression represented by the parser's postfix tokens into a symbolic 
	 * expression structure.
	 * <p>
	 * The symbolic expression structure is a tree that connects operations
	 * in such a way that the program can perform symbolic computations (such as differentiation
	 * with respect to a given variable). Every node in the tree is an {@link Expression},
	 * and each node can be one of four items:
	 * <ol>
	 * <li>{@link Value}</li>
	 * <li>{@link Unknown}</li>
	 * <li>{@link SymbolicResult}</li>
	 * <li>{@link SymbolicFunction}</li>
	 * </ol>
	 * The Expression tree is built in such a way that the order of operations is already known,
	 * and a map of known constants can be passed in to simplify or evaluate the Expression.
	 * Derivatives can be symbolically calculated with respect to any variable, and LaTeX representations
	 * of Expressions can be generated.
	 * 
	 * @return symbolic expression
	 * @see Expression
	 * @see Expression#derivative(String)
	 */
	public final Expression createSymbolicStructure() {
		final Deque<Expression> expressionStack = new ArrayDeque<Expression>(tokens.length - 1);
		
		for (int i = 0; i < tokens.length; i++) {
			final String token = tokens[i];
			
			final Operation potentialOperation = Reserved.operations.get(token);
			final Function potentialFunction = Reserved.functions.get(token);
			
			if (potentialOperation == null ) {
				if (potentialFunction == null) {
					expressionStack.push(symbolize(token));
				} else {
					if (expressionStack.size() >= 1) {
						final Expression argument = expressionStack.pop();
						
						expressionStack.push(new SymbolicFunction(argument, potentialFunction));
					} else {
						throw new StackLengthException("Not enough operands on the expression stack! Index: " + i);
					}
				}				
			} else {
				if (expressionStack.size() >= 2) {
					final Expression operand2 = expressionStack.pop();
					final Expression operand1 = expressionStack.pop();
					
					expressionStack.push(new SymbolicResult(operand1, operand2, potentialOperation));
				} else {
					throw new StackLengthException("Not enough operands on the expression stack! Index: " + i);
				}
			}
		}
		
		if (expressionStack.isEmpty()) {
			throw new StackLengthException("No final result on the stack!");
		} else {
			return expressionStack.pop();
		}
	}
	
	/**
	 * Converts a single postfix operand into its symbolic structural equivalent,
	 * which is either a {@link Value} or an {@link Unknown}.
	 * <p>
	 * <b>NOTE:</b> This function returns an Unknown if it receives anything other than an explicit double or integer value.
	 * This means that mathematical constants, such as <code>pi</code> or <code>e</code>, will be returned in Unknowns.
	 * 
	 * @param operand operand, either a variable, constant, or value
	 * @return either a {@link Value} or an {@link Unknown}
	 */
	private final Expression symbolize(final String operand) {
		try {
			final double value = Double.parseDouble(operand);
			return new Value(value);
		} catch (Exception e) {
			return new Unknown(operand);
		}
	}
}
