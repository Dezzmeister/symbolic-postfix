package com.dezzy.postfix.main;

import java.util.HashMap;
import java.util.Map;

import com.dezzy.postfix.math.Reserved;
import com.dezzy.postfix.math.symbolic.SymbolicParser;
import com.dezzy.postfix.math.symbolic.structure.Expression;

public class Main {

	public static void main(String[] args) {
		//valueTest();
		//symbolTest();
		sphereTest();
	}
	
	private static final void sphereTest() {
		final String[] tokens = "4 3 / pi * r 3 ^ *".split(" ");
		//final String[] tokens = "2 0.5 9 * -1.25 / - 0.008 + 2 ^ 5 3 / ^".split(" ");
		
		final SymbolicParser parser = new SymbolicParser(tokens);
		
		final Expression expression = parser.createSymbolicStructure();
		System.out.println("Formula for the volume of a sphere: " + expression);
		
		final Expression simplified = expression.simplify(Reserved.getCompleteConstantsMap(new HashMap<String, Double>()));
		System.out.println("Simplified formula for the volume of a sphere: " + simplified);
		
		final Map<String, Double> radius = new HashMap<String, Double>();
		radius.put("r", 2.0);
		
		System.out.println("Volume of a sphere with radius 2: " + simplified.evaluate(radius));
	}
}
