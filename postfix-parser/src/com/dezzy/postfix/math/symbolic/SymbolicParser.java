package com.dezzy.postfix.math.symbolic;

import java.util.ArrayDeque;
import java.util.Deque;

import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.Parser;
import com.dezzy.postfix.math.Reserved;
import com.dezzy.postfix.math.StackLengthException;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Unknown;
import com.dezzy.postfix.math.symbolic.structure.Value;

/**
 * Extends {@link Parser Parser's} functionality by adding functions to perform symbolic operations on
 * a postfix expression.
 * 
 * @author Joe Desmond
 */
public class SymbolicParser extends Parser {
	
	/**
	 * Creates a symbolic parser from the given tokens.
	 * 
	 * @param _tokens postfix tokens
	 */
	public SymbolicParser(final String[] _tokens) {
		super(_tokens);
	}
	
	/**
	 * Converts the expression represented by the parser's postfix tokens into a symbolic 
	 * expression structure.
	 * 
	 * @return symbolic expression
	 */
	public final Expression createSymbolicStructure() {
		final Deque<Expression> expressionStack = new ArrayDeque<Expression>(tokens.length - 1);
		
		for (int i = 0; i < tokens.length; i++) {
			final String token = tokens[i];
			
			final Operation potentialOperation = Reserved.operations.get(token);
			
			if (potentialOperation == null) {
				expressionStack.push(symbolize(token));
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
