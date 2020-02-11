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
	
	public Expression derivative(final Expression op1, final Expression op2, final String varName);
	
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
				return new SymbolicResult(Value.ZERO, op2.derivative(varName), Operation.subtract);
			} else {
				return Value.ZERO;
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
				//UNFINISHED
				
				final Expression fprime = op1.derivative(varName);
				final Expression gprime = op2.derivative(varName);
				final Expression fprimeg = new SymbolicResult(fprime, op2, Operation.multiply);
				final Expression fgprime = new SymbolicResult(op1, gprime, Operation.multiply);

				final Expression numerator = new SymbolicResult(fprimeg, fgprime, Operation.subtract);
				final Expression denominator = new SymbolicResult(op2, new Value(2), Operation.power);
				return new SymbolicResult(numerator, denominator, Operation.divide);
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
				
				return new SymbolicResult(op2, powerTerm, Operation.multiply);
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
	};
	
	
	/**
	 * d0 % d1
	 
	public static final Operation modulo = (d0, d1) -> d0 % d1;
	*/
}
