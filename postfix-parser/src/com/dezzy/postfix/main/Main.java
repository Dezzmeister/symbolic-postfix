package com.dezzy.postfix.main;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.Reserved;
import com.dezzy.postfix.math.img.Graph;
import com.dezzy.postfix.math.img.GraphFormat;
import com.dezzy.postfix.math.symbolic.SymbolicParser;
import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Unknown;
import com.dezzy.postfix.math.symbolic.structure.Value;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//derivTest2();
		physHW4();
	}
	
	private static final void physHW4() throws IOException {
		final String[] tokens = "y L + y / ln".split(" ");
		final SymbolicParser parser = new SymbolicParser(tokens);
		final Expression expr = parser.createSymbolicStructure();
		final Expression deriv = expr.derivative("y").simplify(Reserved.constants).cleanDecimals(Reserved.constants);
		expr.saveToFile("expressions/hw4-p12-b.expr");
		deriv.saveToFile("expressions/hw4-p12-b-deriv.expr");
		System.out.println(deriv.toLatex(Reserved.latexConstants));
	}
	
	private static final void distributionTest() {
		final Expression outer = new Value(3);
		final SymbolicResult group = new SymbolicResult(new Unknown("x"), new Value(2), Operation.SUBTRACT);
		final Expression simple = Operation.ADD.distribute(outer, group, Reserved.constants);
		System.out.println(simple);
	}
	
	private static final void derivTest2() throws IOException, ClassNotFoundException {
		final String[] tokens = "2 x 3 ^ * 4 x 2 ^ * + x sin +".split(" ");
		//final String[] tokens = "x 2 ^ 3 *".split(" ");
		final SymbolicParser parser = new SymbolicParser(tokens);
		final Expression expr = parser.createSymbolicStructure();
		System.out.println("Saving:\t" + expr);
		expr.saveToFile("expressions/polynom1.expr");
		final Expression expr2 = Expression.loadFromFile("expressions/polynom1.expr");
		final Expression deriv = expr2.derivative("x");
		final Expression simpleDeriv = deriv.simplify(Reserved.constants).cleanDecimals(Reserved.constants);
		System.out.println(simpleDeriv);
		System.out.println(simpleDeriv.toLatex(Reserved.latexConstants));
		
		final GraphFormat format = new GraphFormat(new Dimension(200, 400), 2.0, 3.0, -2.0, 0.0, 0.25);
		final Graph graph = new Graph(expr, "x", Reserved.emptyConstants, format);
		ImageIO.write(graph.image, "png", new File("graphs/test2.png"));
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
		
		final Expression simplified = expression.simplify(Reserved.getCompleteConstantsMap(new HashMap<String, Constant>()));
		final Expression derivative = simplified.derivative("r");
		System.out.println("Simplified formula for the volume of a sphere: " + simplified);
		System.out.println("Derivative of the simplified volume formula: " + derivative);
		System.out.println("Function of r: " + simplified.isFunctionOf("r"));
		
		final Map<String, Constant> radius = new HashMap<String, Constant>();
		radius.put("r", new Constant(Value.TWO));
		
		System.out.println("Volume of a sphere with radius 2: " + simplified.evaluate(radius));
	}
}
