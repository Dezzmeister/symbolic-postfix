package com.dezzy.postfix.math;

import java.util.HashMap;
import java.util.Map;

import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicFunction;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Value;

/**
 * An operation that is performed on two doubles and returns a double result.
 * 
 * @author Joe Desmond
 */
public interface Operation {
	
	/**
	 * Performs an operation on two doubles and returns a double.
	 * 
	 * @param d0 first operand
	 * @param d1 second operand
	 * @return result
	 */
	public double operate(final double d0, final double d1);
	
	/**
	 * Symbolically finds the derivative of two expressions bound by this operation,
	 * with respect to the given variable.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param varName variable name
	 * @return the derivative function
	 */
	public Expression derivative(final Expression op1, final Expression op2, final String varName);
	
	/**
	 * Tries to simplify an operation. If the operation cannot be simplified, returns a new
	 * SymbolicResult with the given operands and this operation.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param constants known constants (unused at the moment)
	 * @return simplified operation 
	 */
	public Expression simplify(final Expression op1, final Expression op2, final Map<String, Double> constants);
	
	/**
	 * d0 + d1
	 */
	public static final Operation add = new Operation(){
			
		@Override
		public double operate(final double d0, final double d1) {
			return d0 + d1;
		}
		
		@Override
		public Expression derivative(final Expression op1, final Expression op2, final String varName) {
			if (op1.isFunctionOf(varName) && op2.isFunctionOf(varName)) {
				return new SymbolicResult(op1.derivative(varName), op2.derivative(varName), Operation.add);
			} else if (op1.isFunctionOf(varName)) {
				return op1.derivative(varName);
			} else if (op2.isFunctionOf(varName)) {
				return op2.derivative(varName);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public Expression simplify(final Expression op1, final Expression op2, final Map<String, Double> constants) {
			if (op1.equals(op2)) {
				return new SymbolicResult(new Value(2), op1, Operation.multiply);
			} else {
				return new SymbolicResult(op1, op2, Operation.add);
			}
		}
	};
	
	/**
	 * d0 - d1
	 */
	public static final Operation subtract = new Operation(){
		
		@Override
		public double operate(final double d0, final double d1) {
			return d0 - d1;
		}
		
		@Override
		public Expression derivative(final Expression op1, final Expression op2, final String varName) {
			if (op1.isFunctionOf(varName) && op2.isFunctionOf(varName)) {
				return new SymbolicResult(op1.derivative(varName), op2.derivative(varName), Operation.subtract);
			} else if (op1.isFunctionOf(varName)) {
				return op1.derivative(varName);
			} else if (op2.isFunctionOf(varName)) {
				return new SymbolicResult(Value.NEG_ONE, op2.derivative(varName), Operation.multiply);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public Expression simplify(final Expression op1, final Expression op2, final Map<String, Double> constants) {
			if (op1.equals(op2)) {
				return Value.ZERO;
			} else {
				return new SymbolicResult(op1, op2, Operation.subtract);
			}
		}
	};
	
	/**
	 * d0 * d1
	 */
	public static final Operation multiply = new Operation(){
		
		@Override
		public double operate(final double d0, final double d1) {
			return d0 * d1;
		}
		
		@Override
		public Expression derivative(final Expression op1, final Expression op2, final String varName) {
			if (op1.isFunctionOf(varName) && op2.isFunctionOf(varName)) {
				//Implements the product rule
				
				final Expression fprime = op1.derivative(varName);
				final Expression gprime = op2.derivative(varName);
				final Expression fprimeg = new SymbolicResult(fprime, op2, Operation.multiply);
				final Expression fgprime = new SymbolicResult(op1, gprime, Operation.multiply);
				return new SymbolicResult(fprimeg, fgprime, Operation.add);
			} else if (op1.isFunctionOf(varName)) {
				return new SymbolicResult(op2, op1.derivative(varName), Operation.multiply);
			} else if (op2.isFunctionOf(varName)) {
				return new SymbolicResult(op1, op2.derivative(varName), Operation.multiply);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public Expression simplify(final Expression op1, final Expression op2, final Map<String, Double> constants) {
			if (op1.equals(Value.ONE)) {
				return op2;
			} else if (op2.equals(Value.ONE)) {
				return op1;
			} else if (op1.equals(op2)) {
				return new SymbolicResult(op1, new Value(2), Operation.power);
			} else {
				return new SymbolicResult(op1, op2, Operation.multiply);
			}
		}
	};
	
	/**
	 * d0 / d1
	 */
	public static final Operation divide = new Operation(){
		
		@Override
		public double operate(final double d0, final double d1) {
			return d0 / d1;
		}
		
		@Override
		public Expression derivative(final Expression op1, final Expression op2, final String varName) {
			if (op1.isFunctionOf(varName) && op2.isFunctionOf(varName)) {
				//Implements the quotient rule
				
				final Expression fprime = op1.derivative(varName);
				final Expression gprime = op2.derivative(varName);
				final Expression fprimeg = new SymbolicResult(fprime, op2, Operation.multiply);
				final Expression fgprime = new SymbolicResult(op1, gprime, Operation.multiply);

				final Expression numerator = new SymbolicResult(fprimeg, fgprime, Operation.subtract);
				final Expression denominator = new SymbolicResult(op2, new Value(2), Operation.power);
				return new SymbolicResult(numerator, denominator, Operation.divide);
			} else if (op1.isFunctionOf(varName)) {
				final Expression coefficient = new SymbolicResult(Value.ONE, op2, Operation.divide);
				return new SymbolicResult(coefficient, op1.derivative(varName), Operation.multiply);
			} else if (op2.isFunctionOf(varName)) {
				final Expression coefficient = new SymbolicResult(Value.NEG_ONE, op1, Operation.multiply);
				final Expression func = new SymbolicResult(op2, new Value(2), Operation.power);
				return new SymbolicResult(coefficient, func, Operation.multiply);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public Expression simplify(final Expression op1, final Expression op2, final Map<String, Double> constants) {
			if (op2.equals(Value.ONE)) {
				return op1;
			} else if (op1.equals(op2)) {
				return Value.ONE;
			} else {
				return new SymbolicResult(op1, op2, Operation.divide);
			}
		}
	};
	
	/**
	 * @see Math#pow(double, double) Math.pow(d0, d1)
	 */
	//TODO: Finish this
	public static final Operation power = new Operation(){
		
		@Override
		public double operate(final double d0, final double d1) {
			return Math.pow(d0, d1);
		}
		
		@Override
		public Expression derivative(final Expression op1, final Expression op2, final String varName) {
			if (op1.isFunctionOf(varName) && op2.isFunctionOf(varName)) {
				//Implements a generalized power rule
				
				final Expression fprime = op1.derivative(varName);
				final Expression gMinusOne = new SymbolicResult(op2, Value.ONE, Operation.subtract);
				final Expression fgMinusOne = new SymbolicResult(op1, gMinusOne, Operation.power);
				final Expression gfgMinusOne = new SymbolicResult(op2, fgMinusOne, Operation.multiply);
				final Expression firstTerm = new SymbolicResult(fprime, gfgMinusOne, Operation.multiply);
				
				final Expression gprime = op2.derivative(varName);
				final Expression lnf = new SymbolicFunction(op1, Function.ln);
				final Expression fg = new SymbolicResult(op1, op2, Operation.power);
				final Expression fglnf = new SymbolicResult(fg, lnf, Operation.multiply);
				final Expression secondTerm = new SymbolicResult(fglnf, gprime, Operation.multiply);

				return new SymbolicResult(firstTerm, secondTerm, Operation.add);
			} else if (op1.isFunctionOf(varName)) {
				//Implements the power rule
				
				final Map<String, Double> nullMap = new HashMap<String, Double>();
				if (op2.canEvaluate(nullMap)) {
					if (op2.evaluate(nullMap) == 0) {
						return Value.ZERO;
					} else if (op2.evaluate(nullMap) == 1) {
						return op1.derivative(varName);
					}
				}
				
				final Expression newExponent = new SymbolicResult(op2, Value.ONE, Operation.subtract);
				final Expression powerTerm = new SymbolicResult(op1, newExponent, Operation.power);
				final Expression term = new SymbolicResult(op2, powerTerm, Operation.multiply);
				
				//Chain rule
				return new SymbolicResult(op1.derivative(varName), term, Operation.multiply);
			} else if (op2.isFunctionOf(varName)) {
				//Exponential rule (with the chain rule)
				
				final Expression lnTerm = new SymbolicFunction(op1, Function.ln);
				final Expression expTerm = new SymbolicResult(op1, op2, Operation.power);
				final Expression multiplied = new SymbolicResult(lnTerm, expTerm, Operation.multiply);
				return new SymbolicResult(op2.derivative(varName), multiplied, Operation.multiply);
			} else {
				return Value.ZERO;
			}
		}
		
		@Override
		public Expression simplify(final Expression op1, final Expression op2, final Map<String, Double> constants) {
			if (op2.equals(Value.ZERO) || op1.equals(Value.ONE)) {
				return Value.ONE;
			} else if (op2.equals(Value.ONE)) {
				return op1;
			} else if (op1.equals(Value.ZERO)) {
				return Value.ZERO;
			} else {
				return new SymbolicResult(op1, op2, Operation.power);
			}
		}
	};
	
	
	/**
	 * d0 % d1
	 
	public static final Operation modulo = (d0, d1) -> d0 % d1;
	*/
}
