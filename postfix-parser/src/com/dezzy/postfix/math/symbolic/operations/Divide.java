package com.dezzy.postfix.math.symbolic.operations;

import java.util.Map;

import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Value;

/**
 * The division operation.
 * 
 * @author Joe Desmond
 */
public final class Divide implements Operation {
	
	/**
	 * Divides <code>d0</code> by <code>d1</code>.
	 * 
	 * @param d0 first operand
	 * @param d1 second operand
	 * @return <code>(d0 / d1)</code>
	 */
	@Override
	public final double operate(final double d0, final double d1) {
		return d0 / d1;
	}
	
	/**
	 * Calculates the symbolic derivative of <code>(op1 / op2)</code> with respect to <code>varName</code>.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param varName variable name
	 * @return derivative of <code>(op1 / op2)</code>
	 */
	@Override
	public final Expression derivative(final Expression op1, final Expression op2, final String varName) {
		if (op1.isFunctionOf(varName) && op2.isFunctionOf(varName)) {
			//Implements the quotient rule
			
			final Expression fprime = op1.derivative(varName);
			final Expression gprime = op2.derivative(varName);
			final Expression fprimeg = new SymbolicResult(fprime, op2, Operation.MULTIPLY);
			final Expression fgprime = new SymbolicResult(op1, gprime, Operation.MULTIPLY);

			final Expression numerator = new SymbolicResult(fprimeg, fgprime, Operation.SUBTRACT);
			final Expression denominator = new SymbolicResult(op2, new Value(2), Operation.POWER);
			return new SymbolicResult(numerator, denominator, Operation.DIVIDE);
		} else if (op1.isFunctionOf(varName)) {
			final Expression coefficient = new SymbolicResult(Value.ONE, op2, Operation.DIVIDE);
			return new SymbolicResult(coefficient, op1.derivative(varName), Operation.MULTIPLY);
		} else if (op2.isFunctionOf(varName)) {
			final Expression powerExpr = new SymbolicResult(op2, Value.NEG_ONE, Operation.POWER);
			final Expression expr = new SymbolicResult(op1, powerExpr, Operation.MULTIPLY);
			return expr.derivative(varName);
		} else {
			return Value.ZERO;
		}
	}
	
	/**
	 * Simplifies <code>(op1 / op2)</code>. <br>
	 * If the denominator is 1, returns <code>op1</code>. <br>
	 * If both operands are equal, returns {@link Value#ONE}. <br>
	 * Otherwise, returns the original operation as a {@link SymbolicResult}.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param constants known constants
	 * @return simplified version of <code>(op1 / op2)</code>
	 */
	@Override
	public final Expression simplify(final Expression op1, final Expression op2, final Map<String, Constant> constants) {
		if (op1.canEvaluate(constants) && op2.canEvaluate(constants)) {
			return new Value(operate(op1.evaluate(constants), op2.evaluate(constants)));
		}
		
		if (op2.equals(Value.ONE)) {
			return op1;
		} else if (op1.equals(op2)) {
			return Value.ONE;
		} else {
			return new SymbolicResult(op1, op2, Operation.DIVIDE);
		}
	}
	
	/**
	 * Returns <code>(fst / group)</code> as a {@link SymbolicResult} because <code>fst</code>
	 * cannot be distributed to <code>group</code>.
	 * 
	 * @param fst first operand (numerator)
	 * @param group second operand (denominator)
	 * @param constants known constants (ignored)
	 * @return SymbolicResult with <code>(fst / group)</code>
	 */
	@Override
	public final Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Constant> constants) {
		return new SymbolicResult(fst, group, Operation.DIVIDE);
	}
	
	/**
	 * Returns false, because <code>(a / b) != (b / a)</code>.
	 * 
	 * @return false
	 */
	@Override
	public final boolean isCommutative() {
		return false;
	}
	
	/**
	 * Returns a LaTeX representation of <code>(op1 / op2)</code>, as a fraction. <br>
	 * Overrides default behavior because the division identifier is not suitable for LaTeX; <code>\frac{}{}</code>
	 * is used instead.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param latexMappings user-specified LaTeX representations of named constants and variables
	 * @return LaTeX representation of the division of <code>op2</code> by <code>op1</code>
	 */
	@Override
	public final String toLatex(final Expression op1, final Expression op2, final Map<String, String> latexMappings) {
		return "\\frac{" + op1.toLatex(latexMappings) + "}{" + op2.toLatex(latexMappings) + "}";
	}
	
	/**
	 * Returns <code>"/"</code>.
	 * 
	 * @return returns a slash, the division symbol
	 */
	@Override
	public final String identifier() {
		return "/";
	}
}
