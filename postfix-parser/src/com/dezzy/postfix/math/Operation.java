package com.dezzy.postfix.math;

/**
 * An operation that is performed on two doubles and returns a double result.
 * 
 * @author Joe Desmond
 */
@FunctionalInterface
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
	 * d0 + d1
	 */
	public static final Operation add = (d0, d1) -> d0 + d1;
	
	/**
	 * d0 - d1
	 */
	public static final Operation subtract = (d0, d1) -> d0 - d1;
	
	/**
	 * d0 * d1
	 */
	public static final Operation multiply = (d0, d1) -> d0 * d1;
	
	/**
	 * d0 / d1
	 */
	public static final Operation divide = (d0, d1) -> d0 / d1;
	
	/**
	 * {@link Math#pow(double, double) Math.pow(d0, d1)}
	 */
	public static final Operation power = Math::pow;
	
	/**
	 * d0 % d1
	 */
	public static final Operation modulo = (d0, d1) -> d0 % d1;
}
