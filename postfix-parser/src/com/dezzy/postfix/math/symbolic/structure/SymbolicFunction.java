package com.dezzy.postfix.math.symbolic.structure;

import java.util.Map;

import com.dezzy.postfix.math.Function;

public final class SymbolicFunction implements Expression {
	public final Expression argument;
	public final Function function;
	
	public SymbolicFunction(final Expression _argument, final Function _function) {
		argument = _argument;
		function = _function;
	}
	
	@Override
	public double evaluate(Map<String, Double> constants) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canEvaluate(Map<String, Double> constants) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Expression simplify(Map<String, Double> constants) {
		// TODO Auto-generated method stub
		return null;
	}

}
