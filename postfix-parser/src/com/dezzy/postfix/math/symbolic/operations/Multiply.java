package com.dezzy.postfix.math.symbolic.operations;

import java.util.Map;

import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Value;

/**
 * The multiplication operation.
 * 
 * @author Joe Desmond
 */
public final class Multiply implements Operation {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8263024803364889338L;

	/**
	 * Multiplies <code>d0</code> and <code>d1</code>.
	 * 
	 * @param d0 first operand
	 * @param d1 second operand
	 * @return <code>(d0 * d1)</code> 
	 */
	@Override
	public final double operate(final double d0, final double d1) {
		return d0 * d1;
	}
	
	/**
	 * Calculates the derivative of <code>(op1 * op2)</code> with respect to <code>varName</code>.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param varName variable name
	 * @return derivative of <code>(op1 - op2)</code>
	 */
	@Override
	public final Expression derivative(final Expression op1, final Expression op2, final String varName) {
		if (op1.isFunctionOf(varName) && op2.isFunctionOf(varName)) {
			//Implements the product rule
			
			final Expression fprime = op1.derivative(varName);
			final Expression gprime = op2.derivative(varName);
			final Expression fprimeg = new SymbolicResult(fprime, op2, Operation.MULTIPLY);
			final Expression fgprime = new SymbolicResult(op1, gprime, Operation.MULTIPLY);
			return new SymbolicResult(fprimeg, fgprime, Operation.ADD);
		} else if (op1.isFunctionOf(varName)) {
			return new SymbolicResult(op2, op1.derivative(varName), Operation.MULTIPLY);
		} else if (op2.isFunctionOf(varName)) {
			return new SymbolicResult(op1, op2.derivative(varName), Operation.MULTIPLY);
		} else {
			return Value.ZERO;
		}
	}
	
	/**
	 * Tries to simplify <code>(op1 - op2)</code>.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param constants known constants
	 * @return simplified version of <code>(op1 - op2)</code>
	 */
	@Override
	public final Expression simplify(final Expression op1, final Expression op2, final Map<String, Constant> constants) {
		if (op1.canEvaluate(constants) && op2.canEvaluate(constants)) {
			return new Value(operate(op1.evaluate(constants), op2.evaluate(constants)));
		}
		
		if (op1.equals(Value.ZERO) || op2.equals(Value.ZERO)) {
			return Value.ZERO;
		} else if (op1.equals(Value.ONE)) {
			return op2;
		} else if (op2.equals(Value.ONE)) {
			return op1;
		} else if (op1.equals(op2)) {
			return new SymbolicResult(op1, new Value(2), Operation.POWER);
		} else {
			return new SymbolicResult(op1, op2, Operation.MULTIPLY);
		}
	}
	
	/**
	 * Tries to distribute <code>fst</code> to <code>group</code>. Only attempts to distribute if <code>group</code>
	 * is bound by addition, subtraction, multiplication, or division.
	 * 
	 * @param fst first (outer) operand
	 * @param group second operand (with two inner operands and an inner operation)
	 * @param constants known constants
	 * @return simplified version of <code>(fst * group)</code>
	 */
	@Override
	public final Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Constant> constants) {
		if (!fst.canEvaluate(constants)) {
			return new SymbolicResult(fst, group, Operation.MULTIPLY);
		}
		
		final Expression op1 = group.operand1;
		final Expression op2 = group.operand2;
		
		if (group.operation == Operation.ADD) {
			//fst * (op1 + op2)
			//(fst * op1) + (fst * op2)
			
			if (op1.canEvaluate(constants)) {
				final Expression simple = new Value(fst.evaluate(constants) * op1.evaluate(constants));
				
				if (op2.canEvaluate(constants)) {
					return new Value(simple.evaluate(constants) + (fst.evaluate(constants) * op2.evaluate(constants)));
				} else {
					final Expression distributedThd = new SymbolicResult(fst, op2, Operation.MULTIPLY);
					
					return new SymbolicResult(simple, distributedThd, Operation.ADD);
				}
			} else if (op2.canEvaluate(constants)) {
				final Expression simple = new Value(fst.evaluate(constants) * op2.evaluate(constants));
				final Expression distributedSnd = new SymbolicResult(fst, op1, Operation.MULTIPLY);
				
				return new SymbolicResult(simple, distributedSnd, Operation.ADD);
			} else {
				return new SymbolicResult(fst, group, Operation.MULTIPLY);
			}
		} else if (group.operation == Operation.SUBTRACT) {
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
					final Expression distributedThd = new SymbolicResult(fst, op2, Operation.MULTIPLY);
					
					//(fst * op1) - (fst * op2)
					return new SymbolicResult(simple, distributedThd, Operation.SUBTRACT);
				}
			} else if (op2.canEvaluate(constants)) {
				//fst * op2
				final Expression simple = new Value(fst.evaluate(constants) * op2.evaluate(constants));
				
				//fst * op1
				final Expression distributedSnd = new SymbolicResult(fst, op1, Operation.MULTIPLY);
				
				//(fst * op1) - (fst * op2)
				return new SymbolicResult(distributedSnd, simple, Operation.SUBTRACT);
			} else {
				//Default case
				return new SymbolicResult(fst, group, Operation.MULTIPLY);
			}
		} else if (group.operation == Operation.MULTIPLY) {
			//fst * (op1 * op2)
			
			if (op1.canEvaluate(constants)) {
				//fst * op1
				final Expression simple = new Value(fst.evaluate(constants) * op1.evaluate(constants));
				
				if (op2.canEvaluate(constants)) {
					//fst * op1 * op2
					return new Value(simple.evaluate(constants) * op2.evaluate(constants));
				} else {
					//(fst * op1) * op2
					return new SymbolicResult(simple, op2, Operation.MULTIPLY);
				}
			} else if (op2.canEvaluate(constants)) {
				//fst * op2
				final Expression simple = new Value(fst.evaluate(constants) * op2.evaluate(constants));
				
				//(fst * op2) * op1
				return new SymbolicResult(simple, op1, Operation.MULTIPLY);
			} else {
				//Default case
				return new SymbolicResult(fst, group, Operation.MULTIPLY);
			}
		} else if (group.operation == Operation.DIVIDE) {
			//fst * (op1 / op2)
			
			if (op1.canEvaluate(constants)) {
				//fst * op1
				final Expression simple = new Value(fst.evaluate(constants) * op1.evaluate(constants));
				
				if (op2.canEvaluate(constants)) {
					//(fst * op1) / op2
					return new Value(simple.evaluate(constants) / op2.evaluate(constants));
				} else {
					//(fst * op1) / op2
					return new SymbolicResult(simple, op2, Operation.DIVIDE);
				}
			} else if (op2.canEvaluate(constants)) {
				//fst / op2
				final Expression simple = new Value(fst.evaluate(constants) / op2.evaluate(constants));
				
				//(fst / op2) * op1
				return new SymbolicResult(simple, op1, Operation.MULTIPLY);
			} else {
				//Default case
				return new SymbolicResult(fst, group, Operation.MULTIPLY);
			}
		} else {
			//Default case, when no simplification can be performed
			return new SymbolicResult(fst, group, Operation.MULTIPLY);
		}
	}
	
	/**
	 * Returns true, because <code>(a * b) == (b * a)</code>.
	 * 
	 * @return true
	 */
	@Override
	public final boolean isCommutative() {
		return true;
	}
	
	/**
	 * Converts <code>(op1 * op2)</code> into a LaTeX representation. Only uses a dot if
	 * both of the arguments are not {@link Expression#isSimple() simple}.
	 * <p>
	 * Overrides default behavior because the default identifier for the multiplication operation
	 * is not suitable for LaTeX.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param latexMappings user-specified LaTeX representations of named constants and variables
	 * @return LaTeX representation of the multiplication of <code>op1</code> and <code>op2</code>
	 */
	@Override
	public final String toLatex(final Expression op1, final Expression op2, final Map<String, String> latexMappings) {
		final Expression first;
		final Expression second;
		
		final boolean useDot = !(op1.isSimple() || op2.isSimple()) || (op2.leftmostTerm() instanceof Value);
		
		
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
	
	/**
	 * Returns <code>"*"</code>.
	 * 
	 * @return the multiplication symbol (an asterisk)
	 */
	@Override
	public final String identifier() {
		return "*";
	}
}
