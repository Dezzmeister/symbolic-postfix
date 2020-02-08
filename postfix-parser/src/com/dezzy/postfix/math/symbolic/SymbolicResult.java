package com.dezzy.postfix.math.symbolic;

import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.Parser;

public class SymbolicResult implements Expression {
	public final Expression operand1;
	public final Expression operand2;
	
	public final Operation operation;
	
	public SymbolicResult(final Expression _operand1, final Expression _operand2, final Operation _operation) {
		operand1 = _operand1;
		operand2 = _operand2;
		operation = _operation;
	}
	
	@Override
	public String toString() {
		return "(" + operand1.toString() + " " + Parser.operationTokenLookup(operation) + " " + operand2.toString() + ")";
	}
}
