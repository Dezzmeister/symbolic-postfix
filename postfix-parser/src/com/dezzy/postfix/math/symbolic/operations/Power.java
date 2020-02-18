package com.dezzy.postfix.math.symbolic.operations;

import java.util.Map;

import com.dezzy.postfix.math.Function;
import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.Reserved;
import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicFunction;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;
import com.dezzy.postfix.math.symbolic.structure.Value;

/**
 * The exponentiation operation.
 * 
 * @author Joe Desmond
 */
public final class Power implements Operation {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8798278345280531177L;

	/**
	 * Raises <code>d0</code> to the power of <code>d1</code>.
	 * 
	 * @param d0 first operand (base)
	 * @param d1 second operand (exponent)
	 * @return <code>(d0 ^ d1)</code>
	 */
	@Override
	public final double operate(final double d0, final double d1) {
		return Math.pow(d0, d1);
	}
	
	/**
	 * Returns the symbolic derivative of <code>(d0 ^ d1)</code> with respect to <code>varName</code>.
	 * Implements the power rule, the exponential rule, and a generalized power rule for cases of
	 * <code>(f(x) ^ g(x))</code> (with respect to <code>x</code>).
	 * 
	 * @param op1 first operand (base)
	 * @param op2 second operand (exponent)
	 * @param varName variable name
	 * @return derivative of <code>(op1 ^ op2)</code>
	 */
	@Override
	public final Expression derivative(final Expression op1, final Expression op2, final String varName) {
		if (op1.isFunctionOf(varName) && op2.isFunctionOf(varName)) {
			//Implements a generalized power rule
			
			final Expression fprime = op1.derivative(varName);
			final Expression gMinusOne = new SymbolicResult(op2, Value.ONE, Operation.SUBTRACT);
			final Expression fgMinusOne = new SymbolicResult(op1, gMinusOne, Operation.POWER);
			final Expression gfgMinusOne = new SymbolicResult(op2, fgMinusOne, Operation.MULTIPLY);
			final Expression firstTerm = new SymbolicResult(fprime, gfgMinusOne, Operation.MULTIPLY);
			
			final Expression gprime = op2.derivative(varName);
			final Expression lnf = new SymbolicFunction(op1, Function.ln);
			final Expression fg = new SymbolicResult(op1, op2, Operation.POWER);
			final Expression fglnf = new SymbolicResult(fg, lnf, Operation.MULTIPLY);
			final Expression secondTerm = new SymbolicResult(fglnf, gprime, Operation.MULTIPLY);

			return new SymbolicResult(firstTerm, secondTerm, Operation.ADD);
		} else if (op1.isFunctionOf(varName)) {
			//Implements the power rule
			
			if (op2.canEvaluate(Reserved.emptyConstants)) {
				if (op2.evaluate(Reserved.emptyConstants) == 0) {
					return Value.ZERO;
				} else if (op2.evaluate(Reserved.emptyConstants) == 1) {
					return op1.derivative(varName);
				}
			}
			
			final Expression newExponent = new SymbolicResult(op2, Value.ONE, Operation.SUBTRACT);
			final Expression powerTerm = new SymbolicResult(op1, newExponent, Operation.POWER);
			final Expression term = new SymbolicResult(op2, powerTerm, Operation.MULTIPLY);
			
			//Chain rule
			return new SymbolicResult(op1.derivative(varName), term, Operation.MULTIPLY);
		} else if (op2.isFunctionOf(varName)) {
			//Exponential rule (with the chain rule)
			
			final Expression lnTerm = new SymbolicFunction(op1, Function.ln);
			final Expression expTerm = new SymbolicResult(op1, op2, Operation.POWER);
			final Expression multiplied = new SymbolicResult(lnTerm, expTerm, Operation.MULTIPLY);
			return new SymbolicResult(op2.derivative(varName), multiplied, Operation.MULTIPLY);
		} else {
			return Value.ZERO;
		}
	}
	
	/**
	 * Tries to simplify <code>(op1 ^ op2)</code>. <br>
	 * <ul>
	 * <li>If the exponent is zero, returns {@link Value#ONE}.</li>
	 * <li>If the base is one, returns {@link Value#ONE}.</li>
	 * <li>If the exponent is one, returns the base.</li>
	 * <li>If the base is zero, returns {@link Value#ZERO}.</li>
	 * <li>If no simplifications can be performed, returns the original operation as a {@link SymbolicResult}.</li>
	 * </ul>
	 * 
	 * @param op1 first operand (base)
	 * @param op2 second operand (exponent)
	 * @param constants known constants
	 * @return simplified version of <code>(op1 ^ op2)</code>
	 */
	@Override
	public final Expression simplify(final Expression op1, final Expression op2, final Map<String, Constant> constants) {
		if (op1.canEvaluate(constants) && op2.canEvaluate(constants)) {
			return new Value(operate(op1.evaluate(constants), op2.evaluate(constants)));
		}
		
		if (op2.equals(Value.ZERO) || op1.equals(Value.ONE)) {
			return Value.ONE;
		} else if (op2.equals(Value.ONE)) {
			return op1;
		} else if (op1.equals(Value.ZERO)) {
			return Value.ZERO;
		} else {
			return new SymbolicResult(op1, op2, Operation.POWER);
		}
	}
	
	/**
	 * Returns <code>(fst ^ group)</code> as a {@link SymbolicResult} because <code>fst</code>
	 * cannot be distributed to <code>group</code>.
	 * 
	 * @param fst first (outer) operand, base
	 * @param group exponent with inner operands and inner operation
	 * @param constants known constants (ignored)
	 * @return SymbolicResult with <code>(fst ^ group)</code>
	 */
	@Override
	public final Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Constant> constants) {
		return new SymbolicResult(fst, group, Operation.POWER);
	}
	
	/**
	 * Returns false because <code>(a ^ b) != (b ^ a)</code>.
	 * 
	 * @return false
	 */
	@Override
	public final boolean isCommutative() {
		return false;
	}
	
	/**
	 * Converts <code>(op1 ^ op2)</code> into a LaTeX representation.
	 * Overrides default behavior because the default identifier alone is not suitable for LaTeX,
	 * curly braces need to be used around the exponent to ensure that the entire exponent is
	 * formatted correctly.
	 * 
	 * @param op1 first operand (base)
	 * @param op2 second operand (exponent)
	 * @param latexMappings user-specified LaTeX representations of named constants and variables
	 * @return LaTeX representation of <code>(op1 ^ op2)</code>
	 */
	@Override
	public final String toLatex(final Expression op1, final Expression op2, final Map<String, String> latexMappings) {
		return "\\left(" + op1.toLatex(latexMappings) + "^{" + op2.toLatex(latexMappings) + "}\\right)";
	}
	
	/**
	 * Returns <code>"^"</code>.
	 * 
	 * @return a caret, the exponentiation identifier
	 */
	@Override
	public final String identifier() {
		return "^";
	}
}
