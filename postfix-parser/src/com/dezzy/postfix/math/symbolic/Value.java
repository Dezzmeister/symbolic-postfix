package com.dezzy.postfix.math.symbolic;

public final class Value implements Expression {
	public final double value;
	
	public Value(double _value) {
		value = _value;
	}
	
	@Override
	public String toString() {
		return Double.toString(value);
	}
}
