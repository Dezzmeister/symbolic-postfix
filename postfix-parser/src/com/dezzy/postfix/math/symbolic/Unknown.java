package com.dezzy.postfix.math.symbolic;

public class Unknown implements Expression {
	
	public final String varName;
	
	public Unknown(final String _varName) {
		varName = _varName;
	}
	
	@Override
	public String toString() {
		return varName;
	}
}
