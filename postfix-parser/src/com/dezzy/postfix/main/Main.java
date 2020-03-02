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
import com.dezzy.postfix.test.TestUtility;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		TestUtility.derivativeTest();
		System.out.println("========================================================");
		TestUtility.sphereTest();
	}
	
	private static void fractions() {
		final String[] tokens = "6 8 / 2 10 / /".split(" ");
		final SymbolicParser parser = new SymbolicParser(tokens);
		final Expression expr = parser.createSymbolicStructure().simplify(Reserved.constants).cleanDecimals(Reserved.constants);
		System.out.println(expr);
		System.out.println(expr.evaluate(Reserved.constants));
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
}
