package com.dezzy.postfix.math.symbolic.operations;

import java.util.Map;

import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Value;

/**
 * The subtraction operation.
 * 
 * @author Joe Desmond
 */
public class Subtract implements Operation {
	
	/**
	 * Subtracts <code>d1</code> from <code>d0</code>.
	 * 
	 * @param d0 first operand
	 * @param d1 second operand
	 * @return <code>(d0 - d1)</code>
	 */
	@Override
	public final double operate(final double d0, final double d1) {
		return d0 - d1;
	}
	
	/**
	 * Calculates the symbolic derivative of <code>(op1 - op2)</code> with respect to
	 * <code>varName</code>.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param varName variable name
	 * @return derivative of <code>(op1 - op2)</code> with respect to <code>varName</code>
	 */
	@Override
	public final Expression derivative(final Expression op1, final Expression op2, final String varName) {
		if (op1.isFunctionOf(varName) && op2.isFunctionOf(varName)) {
			return new SymbolicResult(op1.derivative(varName), op2.derivative(varName), Operation.SUBTRACT);
		} else if (op1.isFunctionOf(varName)) {
			return op1.derivative(varName);
		} else if (op2.isFunctionOf(varName)) {
			return new SymbolicResult(Value.NEG_ONE, op2.derivative(varName), Operation.MULTIPLY);
		} else {
			return Value.ZERO;
		}
	}
	
	/**
	 * Tries to simplify <code>(op1 - op2)</code>.<br> 
	 * If both operands are equal, returns {@link Value#ZERO}, otherwise returns an unsimplified expression.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param constants known constants
	 * @return simplified version of <code>(op1 - op2)</code>
	 */
	@Override
	public final Expression simplify(final Expression op1, final Expression op2, final Map<String, Constant> constants) {
		if (op1.equals(op2)) {
			return Value.ZERO;
		} else {
			return new SymbolicResult(op1, op2, Operation.SUBTRACT);
		}
	}
	
	/**
	 * Tries to distribute <code>(fst - group)</code>. Only works if <code>group's</code> operation
	 * is addition or subtraction.
	 * 
	 * @param fst first (outer) operand
	 * @param group second operand (with two inner operands and an inner operation)
	 * @param constants known constants
	 * @return simplified version of <code>(fst - group)</code>
	 */
	@Override
	public final Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Constant> constants) {
		if (!fst.canEvaluate(constants)) {
			return new SymbolicResult(fst, group, Operation.SUBTRACT);
		}
		
		final Expression op1 = group.operand1;
		final Expression op2 = group.operand2;
		
		if (group.operation == Operation.ADD) {
			if (op1.canEvaluate(constants)) {
				final Expression simple = new Value(fst.evaluate(constants) - op1.evaluate(constants));
				
				if (op2.canEvaluate(constants)) {
					return new Value(simple.evaluate(constants) - op2.evaluate(constants));
				} else {
					return new SymbolicResult(simple, op2, Operation.SUBTRACT);
				}
			} else if (op2.canEvaluate(constants)) {
				final Expression simple = new Value(fst.evaluate(constants) - op2.evaluate(constants));
				
				return new SymbolicResult(simple, op1, Operation.SUBTRACT);
			} else {
				return new SymbolicResult(fst, group, Operation.SUBTRACT);
			}
		} else if (group.operation == Operation.SUBTRACT) {
			if (op1.canEvaluate(constants)) {
				final Expression simple = new Value(fst.evaluate(constants) - op1.evaluate(constants));
				
				if (op2.canEvaluate(constants)) {
					return new Value(simple.evaluate(constants) + op2.evaluate(constants));
				} else {
					return new SymbolicResult(simple, op2, Operation.ADD);
				}
			} else if (op2.canEvaluate(constants)) {
				final Expression simple = new Value(fst.evaluate(constants) + op2.evaluate(constants));
				
				return new SymbolicResult(simple, op1, Operation.SUBTRACT);
			} else {
				return new SymbolicResult(fst, group, Operation.SUBTRACT);
			}
		} else {
			return new SymbolicResult(fst, group, Operation.SUBTRACT);
		}
	}
	
	/**
	 * Returns false, because <code>(a - b) != (b - a)</code>.
	 * 
	 * @return false 
	 */
	@Override
	public final boolean isCommutative() {
		return false;
	}
	
	/**
	 * Returns <code>"-"</code>.
	 * 
	 * @return a minus sign
	 */
	@Override
	public final String identifier() {
		return "-";
	}
}
