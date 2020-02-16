package com.dezzy.postfix.math;

import java.util.Map;

import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicFunction;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Value;

/**
 * A mathematical function that accepts 1 argument.
 * 
 * @author Joe Desmond
 */
public interface Function {
	
	/**
	 * Accepts the input and returns some output.
	 * 
	 * @param x input
	 * @return output 
	 */
	public double apply(final double x);
	
	/**
	 * Symbolically finds the derivative of this function when applied to an
	 * argument, with respect to the given variable.
	 * 
	 * @param arg argument to the function
	 * @param varName variable name (differentiating with respect to)
	 * @return derivative expression
	 */
	public Expression derivative(final Expression arg, final String varName);
	
	/**
	 * Converts this function and an argument into a LaTeX representation.
	 * 
	 * @param arg argument
	 * @param latexMappings user-defined LaTeX representations of named constants and variables
	 * @return LaTeX representation of the function and the argument
	 */
	public String toLatex(final Expression arg, final Map<String, String> latexMappings);
	
	/**
	 * @see Math#sin(double)
	 */
	public static final Function sin = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.sin(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression term = new SymbolicFunction(arg, Function.cos);
				return new SymbolicResult(arg.derivative(varName), term, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\sin{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#cos(double)
	 */
	public static final Function cos = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.cos(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression term = new SymbolicFunction(arg, Function.sin);
				final Expression negative = new SymbolicResult(Value.NEG_ONE, term, Operation.MULTIPLY);
				return new SymbolicResult(arg.derivative(varName), negative, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\cos{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#tan(double)
	 */
	public static final Function tan = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.tan(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression exponent = new SymbolicResult(arg, new Value(2), Operation.POWER);
				final Expression inverse = new SymbolicResult(Value.ONE, exponent, Operation.DIVIDE);
				return new SymbolicResult(arg.derivative(varName), inverse, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\tan{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#asin(double)
	 */
	public static final Function invsin = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.asin(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression argSqr = new SymbolicResult(arg, new Value(2), Operation.POWER);
				final Expression oneMinusArgSqr = new SymbolicResult(Value.ONE, argSqr, Operation.SUBTRACT);
				final Expression sqrt = new SymbolicResult(oneMinusArgSqr, new Value(-0.5), Operation.POWER);
				
				return new SymbolicResult(arg.derivative(varName), sqrt, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\sin^{-1}{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#acos(double)
	 */
	public static final Function invcos = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.acos(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression argSqr = new SymbolicResult(arg, new Value(2), Operation.POWER);
				final Expression oneMinusArgSqr = new SymbolicResult(Value.ONE, argSqr, Operation.SUBTRACT);
				final Expression sqrt = new SymbolicResult(oneMinusArgSqr, new Value(-0.5), Operation.POWER);
				final Expression term = new SymbolicResult(Value.NEG_ONE, sqrt, Operation.MULTIPLY);
				
				return new SymbolicResult(arg.derivative(varName), term, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\cos^{-1}{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#atan(double)
	 */
	public static final Function invtan = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.asin(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression argSqr = new SymbolicResult(arg, new Value(2), Operation.POWER);
				final Expression onePlusArgSqr = new SymbolicResult(Value.ONE, argSqr, Operation.ADD);
				final Expression inverse = new SymbolicResult(Value.ONE, onePlusArgSqr, Operation.DIVIDE);
				
				return new SymbolicResult(arg.derivative(varName), inverse, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\tan^{-1}{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#sinh(double)
	 */
	public static final Function sinh = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.sinh(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression term = new SymbolicFunction(arg, Function.cosh);
				
				return new SymbolicResult(arg.derivative(varName), term, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\sinh{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#cosh(double)
	 */
	public static final Function cosh = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.cosh(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression term = new SymbolicFunction(arg, Function.sinh);
				
				return new SymbolicResult(arg.derivative(varName), term, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\cosh{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#tanh(double)
	 */
	public static final Function tanh = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.tanh(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression tanh = new SymbolicFunction(arg, Function.tanh);
				final Expression sqr = new SymbolicResult(tanh, new Value(2), Operation.POWER);
				final Expression term = new SymbolicResult(Value.ONE, sqr, Operation.SUBTRACT);
				
				return new SymbolicResult(arg.derivative(varName), term, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\tanh{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#log(double)
	 */
	public static final Function ln = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.log(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression term = new SymbolicResult(Value.ONE, arg, Operation.DIVIDE);
				
				return new SymbolicResult(arg.derivative(varName), term, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\log_{2}{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#log10(double)
	 */
	public static final Function log10 = new Function() {
		private final Expression lnExpr = new SymbolicFunction(new Value(10), Function.ln);
		
		@Override
		public double apply(final double x) {
			return Math.log10(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression denom = new SymbolicResult(arg, lnExpr, Operation.MULTIPLY);
				final Expression term = new SymbolicResult(Value.ONE, denom, Operation.DIVIDE);
				
				return new SymbolicResult(arg.derivative(varName), term, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\log_{10}{" + arg.toLatex(latexMappings) + "}";
		}
	};
	
	/**
	 * @see Math#abs(double)
	 */
	public static final Function abs = new Function() {
		
		@Override
		public double apply(final double x) {
			return Math.abs(x);
		}
		
		@Override
		public Expression derivative(final Expression arg, final String varName) {
			if (arg.isFunctionOf(varName)) {
				final Expression abs = new SymbolicFunction(arg, Function.abs);
				final Expression term = new SymbolicResult(abs, arg, Operation.DIVIDE);
				
				return new SymbolicResult(arg.derivative(varName), term, Operation.MULTIPLY);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public String toLatex(final Expression arg, final Map<String, String> latexMappings) {
			return "\\left|" + arg.toLatex(latexMappings) + "\right|";
		}
	};	
}
