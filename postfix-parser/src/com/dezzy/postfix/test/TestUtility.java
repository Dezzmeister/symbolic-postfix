package com.dezzy.postfix.test;

import java.util.HashMap;
import java.util.Map;

import com.dezzy.postfix.math.Reserved;
import com.dezzy.postfix.math.evaluation.EvaluationDomain;
import com.dezzy.postfix.math.evaluation.VariableDomain;
import com.dezzy.postfix.math.symbolic.SymbolicParser;
import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.Value;

/**
 * Various testing utilities.
 *
 * @author Joe Desmond
 */
public final class TestUtility {
	
	/**
	 * Range of values in {@link #TEST_X_DOMAIN}
	 */
	public static final double TEST_X_DOMAIN_RANGE = 20;
	
	/**
	 * Offset of values in {@link #TEST_X_DOMAIN}
	 */
	public static final double TEST_X_DOMAIN_OFFSET = -10;
	
	/**
	 * An evaluation domain for testing functions of x, with known constants {@link Reserved#constants}
	 */
	public static final EvaluationDomain TEST_X_DOMAIN;
	
	static {		
		final double[] domain = new double[1000];
		
		for (int i = 0; i < domain.length; i++) {
			double value = ((i / (double) domain.length) * TEST_X_DOMAIN_RANGE) + TEST_X_DOMAIN_OFFSET;
			domain[i] = value;
		}
		
		final VariableDomain[] xDomain = {new VariableDomain("x", domain)};
		TEST_X_DOMAIN = new EvaluationDomain(Reserved.constants, xDomain);
	}
	
	/**
	 * Converts the given postfix tokens into a simplified Expression without decimals (decimals are converted to fractions).
	 * 
	 * @param tokens postfix tokens
	 * @return simplified and cleaned symbolic representation of the postfix expression
	 */
	public static final Expression simplified(final String[] tokens) {
		return new SymbolicParser(tokens).createSymbolicStructure().simplify(Reserved.constants).cleanDecimals(Reserved.constants);
	}
	
	/**
	 * Tests differentiation, simplification, and analytic equality.
	 */
	public static final void derivativeTest() {
		final String[] tokens = "3 x 2 ^ * 5 x * + x sin +".split(" ");
		final String[] derivTokens = "6 x * 5 + x cos +".split(" ");
		final String[] derivTokens2 = "5 x cos + x 6 * +".split(" ");
		
		final Expression expr = simplified(tokens);
		
		//The expected derivative of 'expr'
		final Expression expected = simplified(derivTokens);
		
		//This is equivalent to 'expected' but has a different structure
		final Expression expected2 = simplified(derivTokens2);
		final Expression actual = expr.derivative("x").simplify(Reserved.constants).cleanDecimals(Reserved.constants);
		
		System.out.println("Expression:\t" + expr);
		System.out.println("Expected deriv 1:\t" + expected);
		System.out.println("Expected deriv 2:\t" + expected2);
		System.out.println("Actual deriv:\t" + actual);
		System.out.println("Expected 1 = Actual:\t" + expected.analyticallyEquals(actual, TestUtility.TEST_X_DOMAIN));
		System.out.println("Expected 2 = Actual:\t" + expected2.analyticallyEquals(actual, TestUtility.TEST_X_DOMAIN));
		System.out.println("Expected 1 = Expected 2:\t" + expected.analyticallyEquals(expected2, TestUtility.TEST_X_DOMAIN));
	}
	
	/**
	 * Tests the formula for the volume of a sphere and finds its derivative.
	 */
	public static final void sphereTest() {
		final String[] tokens = "4 3 / pi * r 3 ^ *".split(" ");
		
		final SymbolicParser parser = new SymbolicParser(tokens);
		
		final Expression expression = parser.createSymbolicStructure();
		final Expression derivative1 = expression.derivative("r");
		System.out.println("Formula for the volume of a sphere: " + expression);
		System.out.println("Derivative of the volume formula: " + derivative1);
		
		System.out.println("Function of r: " + expression.isFunctionOf("r"));
		System.out.println("Function of x: " + expression.isFunctionOf("x"));
		System.out.println("Function of pi: " + expression.isFunctionOf("pi"));
		System.out.println();
		
		final Expression simplified = expression.simplify(Reserved.constants);
		final Expression derivative = simplified.derivative("r").simplify(Reserved.constants);
		System.out.println("Simplified formula for the volume of a sphere: " + simplified);
		System.out.println("Derivative of the simplified volume formula: " + derivative);
		System.out.println("Function of r: " + simplified.isFunctionOf("r"));
		
		final Map<String, Constant> radius = new HashMap<String, Constant>();
		radius.put("r", new Constant(Value.TWO));
		
		System.out.println("Volume of a sphere with radius 2: " + simplified.evaluate(radius));
	}
}
