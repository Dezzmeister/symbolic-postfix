package com.dezzy.postfix.main;

import java.util.HashMap;
import java.util.Map;

import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.Reserved;
import com.dezzy.postfix.math.symbolic.SymbolicParser;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Unknown;
import com.dezzy.postfix.math.symbolic.structure.Value;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) {
		//valueTest();
		//symbolTest();
		//sphereTest();
		//derivativeTest();
		derivTest2();
		//distributionTest();
	}
	
	private static final void distributionTest() {
		final Expression outer = new Value(3);
		final SymbolicResult group = new SymbolicResult(new Unknown("x"), new Value(2), Operation.subtract);
		final Expression simple = Operation.add.distribute(outer, group, Reserved.constants);
		System.out.println(simple);
	}
	
	private static final void derivTest2() {
		final String[] tokens = "e x ^ 1.66666666 *".split(" ");
		//final String[] tokens = "x 2 ^ 3 *".split(" ");
		final SymbolicParser parser = new SymbolicParser(tokens);
		final Expression expr = parser.createSymbolicStructure();
		final Expression deriv = expr.derivative("x");
		final Expression simpleDeriv = deriv.simplify(Reserved.constants).cleanDecimals(Reserved.constants);
		System.out.println(simpleDeriv);
		System.out.println(simpleDeriv.toLatex(Reserved.latexConstants));
	}
	
	private static final void derivativeTest() {
		//final String[] tokens = "x sin x ^".split(" ");
		final String[] tokens = "x 2 ^ sin".split(" ");
		final SymbolicParser parser = new SymbolicParser(tokens);
		final Expression expression = parser.createSymbolicStructure();
		final Expression derivative = expression.derivative("x");
		System.out.println(expression.simplify(Reserved.constants));
		System.out.println(derivative);
		System.out.println(derivative.simplify(Reserved.constants));
		System.out.println(derivative.simplify(Reserved.constants).toLatex(Reserved.latexConstants));
		System.out.println(derivative.derivative("x").simplify(Reserved.constants));
	}
	
	private static final void sphereTest() {
		final String[] tokens = "4 3 / pi * r 3 ^ *".split(" ");
		//final String[] tokens = "2 0.5 9 * -1.25 / - 0.008 + 2 ^ 5 3 / ^".split(" ");
		
		final SymbolicParser parser = new SymbolicParser(tokens);
		
		final Expression expression = parser.createSymbolicStructure();
		final Expression derivative1 = expression.derivative("r");
		System.out.println("Formula for the volume of a sphere: " + expression);
		System.out.println("Derivative of the volume formula: " + derivative1);
		
		System.out.println("Function of r: " + expression.isFunctionOf("r"));
		System.out.println("Function of x: " + expression.isFunctionOf("x"));
		System.out.println("Function of pi: " + expression.isFunctionOf("pi"));
		System.out.println();
		
		final Expression simplified = expression.simplify(Reserved.getCompleteConstantsMap(new HashMap<String, Double>()));
		final Expression derivative = simplified.derivative("r");
		System.out.println("Simplified formula for the volume of a sphere: " + simplified);
		System.out.println("Derivative of the simplified volume formula: " + derivative);
		System.out.println("Function of r: " + simplified.isFunctionOf("r"));
		
		final Map<String, Double> radius = new HashMap<String, Double>();
		radius.put("r", 2.0);
		
		System.out.println("Volume of a sphere with radius 2: " + simplified.evaluate(radius));
	}
}
