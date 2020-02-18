package com.dezzy.postfix.math;

import java.io.Serializable;
import java.util.Map;

import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.operations.Add;
import com.dezzy.postfix.math.symbolic.operations.Divide;
import com.dezzy.postfix.math.symbolic.operations.Multiply;
import com.dezzy.postfix.math.symbolic.operations.Power;
import com.dezzy.postfix.math.symbolic.operations.Subtract;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.SymbolicResult;

/**
 * An operation that is performed on two doubles and returns a double result.
 * 
 * @author Joe Desmond
 */
public interface Operation extends Serializable {
	
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
	 * @param constants known constants
	 * @return simplified operation 
	 */
	public Expression simplify(final Expression op1, final Expression op2, final Map<String, Constant> constants);
	
	/**
	 * Tries to distribute the first expression among the second operation. If no simplification can be performed, returns
	 * a {@link SymbolicExpression} with this operation, and <code>fst</code> and <code>group</code> as operands.
	 * 
	 * @param fst first operand
	 * @param group second operand
	 * @param constants known constants
	 * @return simplified version of this operation applied to <code>fst</code> and <code>group</code>
	 */
	public Expression distribute(final Expression fst, final SymbolicResult group, final Map<String, Constant> constants);
	
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
	 * Converts this operation on two Expressions into a LaTeX representation. Uses this operation's
	 * identifier as the operation symbol.
	 * 
	 * @param op1 first operand
	 * @param op2 second operand
	 * @param latexMappings user-specified LaTeX representations of named constants and variables
	 * @return LaTeX representation of this operation
	 */
	public default String toLatex(final Expression op1, final Expression op2, final Map<String, String> latexMappings) {
		return "\\left(" + op1.toLatex(latexMappings) + " " + identifier() + " " + op2.toLatex(latexMappings) + "\\right)";
	}
	
	/**
	 * Returns the symbol identifying this operation (e.g., "+","/",etc.).
	 * 
	 * @return symbol identifying this operation
	 */
	public String identifier();
	
	/**
	 * The addition operation.
	 */
	public static final Operation ADD = new Add();
	
	/**
	 * The subtraction operation.
	 */
	public static final Operation SUBTRACT = new Subtract();
	
	/**
	 * The multiplication operation.
	 */
	public static final Operation MULTIPLY = new Multiply();
	
	/**
	 * The division operation.
	 */
	public static final Operation DIVIDE = new Divide();
	
	/**
	 * The exponentiation operation.
	 */
	public static final Operation POWER = new Power();
}
