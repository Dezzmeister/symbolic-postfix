package com.dezzy.postfix.math.symbolic.structure;

import java.util.Map;
import java.util.Objects;

import com.dezzy.postfix.math.Operation;
import com.dezzy.postfix.math.Reserved;

/**
 * A single, symbolic operation between two symbolic expressions.
 * 
 * @author Joe Desmond
 */
public class SymbolicResult implements Expression {
	
	/**
	 * First symbolic operand
	 */
	public final Expression operand1;
	
	/**
	 * Second symbolic operand
	 */
	public final Expression operand2;
	
	/**
	 * Operation to be performed on the symbolic operands
	 */
	public final Operation operation;
	
	/**
	 * Constructs a symbolic calculation with the given operations, and the given operator.
	 * The calculation is not actually performed until {@link #evaluate(Map)} is called.
	 * 
	 * @param _operand1 first operand (<code>d0</code> in {@link Operation#operate(double, double)})
	 * @param _operand2 second operand (<code>d1</code> in {@link Operation#operate(double, double)})
	 * @param _operation operation to operate on the given operands
	 */
	public SymbolicResult(final Expression _operand1, final Expression _operand2, final Operation _operation) {
		operand1 = _operand1;
		operand2 = _operand2;
		operation = _operation;
	}
	
	/**
	 * Evaluates this symbolic result by first evaluating the operands, then performing the specified operation on them.
	 * 
	 * @param constants known constants
	 * @return double value of this calculation
	 */
	@Override
	public final double evaluate(final Map<String, Double> constants) {
		final double d0 = operand1.evaluate(constants);
		final double d1 = operand2.evaluate(constants);
		
		return operation.operate(d0, d1);
	}
	
	/**
	 * Returns true if the operation can be performed with the given known constants.
	 * 
	 * @param constants known constants
	 * @return true if this symbolic result can be resolved given <code>constants</code>
	 */
	@Override
	public boolean canEvaluate(final Map<String, Double> constants) {
		return operand1.canEvaluate(constants) && operand2.canEvaluate(constants);
	}
	
	/**
	 * Simplifies this symbolic calculation by first simplifying its operands, then
	 * checking if the calculation can be performed with the simplified operands. If it can,
	 * a {@link Value} is returned with the result. If not, a new SymbolicResult is
	 * returned with the simplified operands, and the original operation.
	 * 
	 * @param constants known constants
	 * @return a new {@link Value} or SymbolicResult
 */
	@Override
	public Expression simplify(final Map<String, Double> constants) {
		final Expression expr0 = operand1.simplify(constants);
		final Expression expr1 = operand2.simplify(constants);
		final Expression result = operation.simplify(expr0, expr1, constants);
		
		if (result.canEvaluate(constants)) {
			final double value = result.evaluate(constants);
			return new Value(value);
		} else {
			return result;
		}
	}
	
	/**
	 * Returns true if one of the operands is a function of the given variable.
	 * 
	 * @param varName variable name
	 * @return true if one of the operands is a function of the given variable
	 */
	@Override
	public boolean isFunctionOf(final String varName) {
		return operand1.isFunctionOf(varName) || operand2.isFunctionOf(varName);
	}
	
	@Override
	public Expression derivative(final String varName) {
		return operation.derivative(operand1, operand2, varName);
	}
	
	/**
	 * Returns a String representing this calculation, of the format:
	 * <p> 
	 * <code>(expr1 operator expr2)</code>
	 * <p>
	 * Examples: <br>
	 * <code>(4 + 9.5)</code><br>
	 * <code>((x / -5) ^ 2)</code>
	 * 
	 * @return String representation of this calculation
	 */
	@Override
	public final String toString() {
		return "(" + operand1.toString() + " " + Reserved.operationTokenLookup(operation) + " " + operand2.toString() + ")";
	}
	
	/**
	 * Returns true if these two SymbolicResults represent the same operation with the same operands.
	 * 
	 * @param other other SymbolicResult
	 * @return true if these SymbolicResults are obviously equal
	 */
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof SymbolicResult)) {
			return false;
		} else {
			final SymbolicResult otherSymbolic = (SymbolicResult) other;
			return (operand1.equals(otherSymbolic.operand1) && 
					operand2.equals(otherSymbolic.operand2) &&
					operation.equals(otherSymbolic.operation));
		}
	}
	
	/**
	 * Returns the hashcode of this SymbolicResult by hashing its operands and operation.
	 * 
	 * @return hashcode of this SymbolicResult
	 */
	@Override
	public int hashCode() {
		return Objects.hash(operand1, operand2, operation);
	}
}
