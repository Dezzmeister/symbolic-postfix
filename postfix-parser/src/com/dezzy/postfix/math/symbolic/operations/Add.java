package com.dezzy.postfix.math.symbolic.operations;

import java.util.Map;

import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Value;

/**
 * The addition operation.
 * 
 * @author Joe Desmond
 */
public final class Add implements Operation {
	
	/**
	 * Adds two doubles.
	 * 
	 * @param d0 first addend
	 * @param d1 second addend
	 * @return <code>d0 + d1</code>
	 */
	@Override
	public final double operate(final double d0, final double d1) {
		return d0 + d1;
	}
	
	/**
	 * Returns the symbolic derivative with respect to <code>varName</code> of the addition operation applied to 
	 * <code>op1</code> and <code>op2</code>, in that order.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param varName variable name
	 * @return derivative expression
	 */
	@Override
	public final Expression derivative(final Expression op1, final Expression op2, final String varName) {
		if (op1.isFunctionOf(varName) && op2.isFunctionOf(varName)) {
			return new SymbolicResult(op1.derivative(varName), op2.derivative(varName), Operation.ADD);
		} else if (op1.isFunctionOf(varName)) {
			return op1.derivative(varName);
		} else if (op2.isFunctionOf(varName)) {
			return op2.derivative(varName);
		} else {
			return Value.ZERO;
		}
	}
	
	/**
	 * Simplifies <code>(op1 + op2)</code><br>
	 * If the operands are equal, returns the first operand multiplied by 2, as a {@link SymbolicResult}.
	 * Otherwise, returns an unsimplified expression.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param constants known constants
	 * @return simplified expression
	 */
	@Override
	public final Expression simplify(final Expression op1, final Expression op2, final Map<String, Constant> constants) {
		if (op1.canEvaluate(constants) && op2.canEvaluate(constants)) {
			return new Value(operate(op1.evaluate(constants), op2.evaluate(constants)));
		}
		
		if (op1.equals(op2)) {
			return new SymbolicResult(new Value(2), op1, Operation.MULTIPLY);
		} else {
			return new SymbolicResult(op1, op2, Operation.ADD);
		}
	}
	
	/**
	 * Returns true, because <code>(a + b) == (b + a)</code>.
	 * 
	 * @return true 
	 */
	@Override
	public final boolean isCommutative() {
		return true;
	}
	
	/**
	 * Attempts to distribute the first argument to each of those in <code>group</code>.<br>
	 * Tries to simplify <code>fst + op(op1, op2)</code> where <code>op</code> is 
	 * {@link SymbolicResult#operation group.operation}, <code>op1</code> is
	 * {@link SymbolicResult#operand1 group.operand1}, and <code>op2</code> is
	 * {@link SymbolicResult#operand2 group.operand2}. This method only tries to simplify the expression if the
	 * operation binding <code>group</code> is addition or subtraction. Even if every operand can be evaluated,
	 * this method will not simplify completely unless <code>group</code> is bound by addition or subtraction.
	 * 
	 * @param fst first (outer) expression
	 * @param group group being added to <code>first</code>
	 * @param constants known constants
	 * @return simplified version of <code>fst + group</code>
	 */
	@Override
	public final Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Constant> constants) {
		//fst + group
		
		if (!fst.canEvaluate(constants)) {
			//Can't simplify the outside term
			return new SymbolicResult(fst, group, Operation.ADD);
		}
		
		final Expression op1 = group.operand1;
		final Expression op2 = group.operand2;
		
		if (group.operation == Operation.ADD) {				
			if (op1.canEvaluate(constants)) {
				//Simplify op1
				final Expression simple = new Value(fst.evaluate(constants) + op1.evaluate(constants));
				
				if (op2.canEvaluate(constants)) {
					//Now simplify op2
					return new Value(simple.evaluate(constants) + op2.evaluate(constants));
				} else {
					return new SymbolicResult(simple, op2, Operation.ADD);
				}
			} else if (op2.canEvaluate(constants)) {
				//Simplify op2, can't simplify op1
				final Expression simple = new Value(fst.evaluate(constants) + op2.evaluate(constants));
				return new SymbolicResult(simple, op1, Operation.ADD);
			} else {
				//Can't simplify group operands
				return new SymbolicResult(fst, group, Operation.ADD);
			}
		} else if (group.operation == Operation.SUBTRACT) {				
			if (op1.canEvaluate(constants)) {
				final Expression simple = new Value(fst.evaluate(constants) + op1.evaluate(constants));
				
				if (op2.canEvaluate(constants)) {
					return new Value(simple.evaluate(constants) - op2.evaluate(constants));
				} else {
					return new SymbolicResult(simple, op2, Operation.SUBTRACT);
				}
			} else if (op2.canEvaluate(constants)) {
				final Expression simple = new Value(fst.evaluate(constants) - op2.evaluate(constants));
				return new SymbolicResult(simple, op1, Operation.ADD);
			} else {
				return new SymbolicResult(fst, group, Operation.ADD);
			}
		} else {
			return new SymbolicResult(fst, group, Operation.ADD);
		}			
	}
	
	/**
	 * Returns <code>"+"</code>. 
	 * 
	 * @return plus symbol
	 */
	@Override
	public final String identifier() {
		return "+";
	}
}
