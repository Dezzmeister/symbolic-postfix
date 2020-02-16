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
	 * Tries to distribute the first expression among the second operation. If no simplification can be performed, returns
	 * a {@link SymbolicExpression} with this operation, and <code>fst</code> and <code>group</code> as operands.
	 * 
	 * @param fst first operand
	 * @param group second operand
	 * @param constants known constants
	 * @return simplified version of this operation applied to <code>fst</code> and <code>group</code>
	 */
	public Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Double> constants);
	
	/**
	 * Returns true if this operation is commutative (e.g., if the order of operands does not matter). <br>
	 * The following operations are commutative:
	 * <ul>
	 * <li>Addition: <code>(a + b) == (b + a)</code></li>
	 * <li>Multiplication: <code>(a * b) == (b * a)</code></li>
	 * </ul>
	 * 
	 * The following operations are not commutative:
	 * <ul>
	 * <li>Subtraction: <code>(a - b) != (b - a)</code></li>
	 * <li>Division: <code>(a / b) != (b / a)</code></li>
	 * <li>Exponentiation: <code>(a ^ b) != (b ^ a)</code></li>
	 * </ul>
	 * 
	 * @return true if this operation is commutative
	 */
	public boolean isCommutative();
	
	/**
	 * Converts this operation on two Expressions into a LaTeX representation.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param latexMappings user-specified LaTeX representations of named constants and variables
	 * @return LaTeX representation of this operation
	 */
	public String toLaTeX(final Expression op1, final Expression op2, final Map<String, String> latexMappings);
	
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
		
		@Override
		public boolean isCommutative() {
			return true;
		}
		
		@Override
		public Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Double> constants) {
			//fst + group
			
			if (!fst.canEvaluate(constants)) {
				//Can't simplify the outside term
				return new SymbolicResult(fst, group, Operation.add);
			}
			
			final Expression op1 = group.operand1;
			final Expression op2 = group.operand2;
			
			if (group.operation == Operation.add) {				
				if (op1.canEvaluate(constants)) {
					//Simplify op1
					final Expression simple = new Value(fst.evaluate(constants) + op1.evaluate(constants));
					
					if (op2.canEvaluate(constants)) {
						//Now simplify op2
						return new Value(simple.evaluate(constants) + op2.evaluate(constants));
					} else {
						return new SymbolicResult(simple, op2, Operation.add);
					}
				} else if (op2.canEvaluate(constants)) {
					//Simplify op2, can't simplify op1
					final Expression simple = new Value(fst.evaluate(constants) + op2.evaluate(constants));
					return new SymbolicResult(simple, op1, Operation.add);
				} else {
					//Can't simplify group operands
					return new SymbolicResult(fst, group, Operation.add);
				}
			} else if (group.operation == Operation.subtract) {				
				if (op1.canEvaluate(constants)) {
					final Expression simple = new Value(fst.evaluate(constants) + op1.evaluate(constants));
					
					if (op2.canEvaluate(constants)) {
						return new Value(simple.evaluate(constants) - op2.evaluate(constants));
					} else {
						return new SymbolicResult(simple, op2, Operation.subtract);
					}
				} else if (op2.canEvaluate(constants)) {
					final Expression simple = new Value(fst.evaluate(constants) - op2.evaluate(constants));
					return new SymbolicResult(simple, op1, Operation.add);
				} else {
					return new SymbolicResult(fst, group, Operation.add);
				}
			} else {
				return new SymbolicResult(fst, group, Operation.add);
			}			
		}
		
		@Override
		public String toLaTeX(final Expression op1, final Expression op2, final Map<String, String> latexMappings) {
			return "\\left(" + op1.toLatex(latexMappings) + " + " + op2.toLatex(latexMappings) + "\\right)";
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
		
		@Override
		public Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Double> constants) {
			if (!fst.canEvaluate(constants)) {
				return new SymbolicResult(fst, group, Operation.subtract);
			}
			
			final Expression op1 = group.operand1;
			final Expression op2 = group.operand2;
			
			if (group.operation == Operation.add) {
				if (op1.canEvaluate(constants)) {
					final Expression simple = new Value(fst.evaluate(constants) - op1.evaluate(constants));
					
					if (op2.canEvaluate(constants)) {
						return new Value(simple.evaluate(constants) - op2.evaluate(constants));
					} else {
						return new SymbolicResult(simple, op2, Operation.subtract);
					}
				} else if (op2.canEvaluate(constants)) {
					final Expression simple = new Value(fst.evaluate(constants) - op2.evaluate(constants));
					
					return new SymbolicResult(simple, op1, Operation.subtract);
				} else {
					return new SymbolicResult(fst, group, Operation.subtract);
				}
			} else if (group.operation == Operation.subtract) {
				if (op1.canEvaluate(constants)) {
					final Expression simple = new Value(fst.evaluate(constants) - op1.evaluate(constants));
					
					if (op2.canEvaluate(constants)) {
						return new Value(simple.evaluate(constants) + op2.evaluate(constants));
					} else {
						return new SymbolicResult(simple, op2, Operation.add);
					}
				} else if (op2.canEvaluate(constants)) {
					final Expression simple = new Value(fst.evaluate(constants) + op2.evaluate(constants));
					
					return new SymbolicResult(simple, op1, Operation.subtract);
				} else {
					return new SymbolicResult(fst, group, Operation.subtract);
				}
			} else {
				return new SymbolicResult(fst, group, Operation.subtract);
			}
		}
		
		@Override
		public boolean isCommutative() {
			return false;
		}
		
		@Override
		public String toLaTeX(final Expression op1, final Expression op2, final Map<String, String> latexMappings) {
			return "\\left(" + op1.toLatex(latexMappings) + " - " + op2.toLatex(latexMappings) + "\\right)";
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
		
		@Override
		public Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Double> constants) {
			if (!fst.canEvaluate(constants)) {
				return new SymbolicResult(fst, group, Operation.multiply);
			}
			
			final Expression op1 = group.operand1;
			final Expression op2 = group.operand2;
			
			if (group.operation == Operation.add) {
				//fst * (op1 + op2)
				//(fst * op1) + (fst * op2)
				
				if (op1.canEvaluate(constants)) {
					final Expression simple = new Value(fst.evaluate(constants) * op1.evaluate(constants));
					
					if (op2.canEvaluate(constants)) {
						return new Value(simple.evaluate(constants) + (fst.evaluate(constants) * op2.evaluate(constants)));
					} else {
						final Expression distributedThd = new SymbolicResult(fst, op2, Operation.multiply);
						
						return new SymbolicResult(simple, distributedThd, Operation.add);
					}
				} else if (op2.canEvaluate(constants)) {
					final Expression simple = new Value(fst.evaluate(constants) * op2.evaluate(constants));
					final Expression distributedSnd = new SymbolicResult(fst, op1, Operation.multiply);
					
					return new SymbolicResult(simple, distributedSnd, Operation.add);
				} else {
					return new SymbolicResult(fst, group, Operation.multiply);
				}
			} else if (group.operation == Operation.subtract) {
				//fst * (op1 - op2)
				//(fst * op1) - (fst * op2)
				
				if (op1.canEvaluate(constants)) {
					//fst * op1
					final Expression simple = new Value(fst.evaluate(constants) * op1.evaluate(constants));
					
					if (op2.canEvaluate(constants)) {
						//(fst * op1) - (fst * op2)
						return new Value(simple.evaluate(constants) - (fst.evaluate(constants) * op2.evaluate(constants)));
					} else {
						//fst * op2
						final Expression distributedThd = new SymbolicResult(fst, op2, Operation.multiply);
						
						//(fst * op1) - (fst * op2)
						return new SymbolicResult(simple, distributedThd, Operation.subtract);
					}
				} else if (op2.canEvaluate(constants)) {
					//fst * op2
					final Expression simple = new Value(fst.evaluate(constants) * op2.evaluate(constants));
					
					//fst * op1
					final Expression distributedSnd = new SymbolicResult(fst, op1, Operation.multiply);
					
					//(fst * op1) - (fst * op2)
					return new SymbolicResult(distributedSnd, simple, Operation.subtract);
				} else {
					//Default case
					return new SymbolicResult(fst, group, Operation.multiply);
				}
			} else if (group.operation == Operation.multiply) {
				//fst * (op1 * op2)
				
				if (op1.canEvaluate(constants)) {
					//fst * op1
					final Expression simple = new Value(fst.evaluate(constants) * op1.evaluate(constants));
					
					if (op2.canEvaluate(constants)) {
						//fst * op1 * op2
						return new Value(simple.evaluate(constants) * op2.evaluate(constants));
					} else {
						//(fst * op1) * op2
						return new SymbolicResult(simple, op2, Operation.multiply);
					}
				} else if (op2.canEvaluate(constants)) {
					//fst * op2
					final Expression simple = new Value(fst.evaluate(constants) * op2.evaluate(constants));
					
					//(fst * op2) * op1
					return new SymbolicResult(simple, op1, Operation.multiply);
				} else {
					//Default case
					return new SymbolicResult(fst, group, Operation.multiply);
				}
			} else if (group.operation == Operation.divide) {
				//fst * (op1 / op2)
				
				if (op1.canEvaluate(constants)) {
					//fst * op1
					final Expression simple = new Value(fst.evaluate(constants) * op1.evaluate(constants));
					
					if (op2.canEvaluate(constants)) {
						//(fst * op1) / op2
						return new Value(simple.evaluate(constants) / op2.evaluate(constants));
					} else {
						//(fst * op1) / op2
						return new SymbolicResult(simple, op2, Operation.divide);
					}
				} else if (op2.canEvaluate(constants)) {
					//fst / op2
					final Expression simple = new Value(fst.evaluate(constants) / op2.evaluate(constants));
					
					//(fst / op2) * op1
					return new SymbolicResult(simple, op1, Operation.multiply);
				} else {
					//Default case
					return new SymbolicResult(fst, group, Operation.multiply);
				}
			} else {
				//Default case, when no simplification can be performed
				return new SymbolicResult(fst, group, Operation.multiply);
			}
		}
		
		@Override
		public boolean isCommutative() {
			return true;
		}
		
		@Override
		public String toLaTeX(final Expression op1, final Expression op2, final Map<String, String> latexMappings) {
			final Expression first;
			final Expression second;
			final boolean useDot = !(op1.isSimple() || op2.isSimple());
			
			
			if (op1.isSimple() && !op2.isSimple()) {
				first = op1;
				second = op2;
			} else if (!op1.isSimple() && op2.isSimple()) {
				first = op2;
				second = op1;
			} else {
				first = op1;
				second = op2;
			}
			
			return "\\left(" + first.toLatex(latexMappings) + (useDot ? " \\cdot " : "") + second.toLatex(latexMappings) + "\\right)";
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
		
		@Override
		public Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Double> constants) {
			return new SymbolicResult(fst, group, Operation.divide);
		}
		
		@Override
		public boolean isCommutative() {
			return false;
		}
		
		@Override
		public String toLaTeX(final Expression op1, final Expression op2, final Map<String, String> latexMappings) {
			return "\\frac{" + op1.toLatex(latexMappings) + "}{" + op2.toLatex(latexMappings) + "}";
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
		
		@Override
		public Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Double> constants) {
			return new SymbolicResult(fst, group, Operation.power);
		}
		
		@Override
		public boolean isCommutative() {
			return false;
		}
		
		@Override
		public String toLaTeX(final Expression op1, final Expression op2, final Map<String, String> latexMappings) {
			return "\\left(" + op1.toLatex(latexMappings) + "^{" + op2.toLatex(latexMappings) + "}\\right)";
		}
	};
}
