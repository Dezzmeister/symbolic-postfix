package com.dezzy.postfix.math.symbolic.structure;

public class Equation {
	public final Expression left;
	public final Expression right;
	
	public Equation(final Expression _left, final Expression _right) {
		left = _left;
		right = _right;
	}
	
	@Override
	public final String toString() {
		return left.toString() + " = " + right.toString();
	}
}
