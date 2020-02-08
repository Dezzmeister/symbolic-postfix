package com.dezzy.postfix.math.symbolic;

import java.util.ArrayDeque;
import java.util.Deque;

import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.Parser;
import com.dezzy.postfix.math.StackLengthException;

public class SymbolicParser extends Parser {

	public SymbolicParser(final String[] _tokens) {
		super(_tokens);
	}

	public final Expression createSymbolicStructure() {
		final Deque<Expression> expressionStack = new ArrayDeque<Expression>(tokens.length - 1);
		
		for (int i = 0; i < tokens.length; i++) {
			final String token = tokens[i];
			
			final Operation potentialOperation = operations.get(token);
			
			if (potentialOperation == null) {
				expressionStack.push(symbolize(token));
			} else {
				if (expressionStack.size() >= 2) {
					final Expression operand2 = expressionStack.pop();
					final Expression operand1 = expressionStack.pop();
					
					expressionStack.push(new SymbolicResult(operand1, operand2, potentialOperation));
				} else {
					throw new StackLengthException("Not enough operands on the evaluation stack! Index: " + i);
				}
			}
		}
		
		if (expressionStack.isEmpty()) {
			throw new StackLengthException("No final result on the stack!");
		} else {
			return expressionStack.pop();
		}
	}
	
	private final Expression symbolize(final String operand) {
		try {
			final double value = Double.parseDouble(operand);
			return new Value(value);
		} catch (Exception e) {
			return new Unknown(operand);
		}
	}
}
