package com.dezzy.postfix.math;

/**
 * A mathematical function that accepts 1 argument.
 * 
 * @author Joe Desmond
 */
@FunctionalInterface
public interface Function {
	
	/**
	 * Accepts the input and returns some output.
	 * 
	 * @param x input
	 * @return output 
	 */
	public double apply(final double x);
	
	/**
	 * @see Math#sin(double)
	 */
	public static final Function sin = Math::sin;
	
	/**
	 * @see Math#cos(double)
	 */
	public static final Function cos = Math::cos;
	
	/**
	 * @see Math#tan(double)
	 */
	public static final Function tan = Math::tan;
	
	/**
	 * @see Math#asin(double)
	 */
	public static final Function invsin = Math::asin;
	
	/**
	 * @see Math#acos(double)
	 */
	public static final Function invcos = Math::acos;
	
	/**
	 * @see Math#atan(double)
	 */
	public static final Function invtan = Math::atan;
	
	/**
	 * @see Math#sinh(double)
	 */
	public static final Function sinh = Math::sinh;
	
	/**
	 * @see Math#cosh(double)
	 */
	public static final Function cosh = Math::cosh;
	
	/**
	 * @see Math#tanh(double)
	 */
	public static final Function tanh = Math::tanh;
	
	/**
	 * @see Math#log(double)
	 */
	public static final Function ln = Math::log;
	
	/**
	 * @see Math#log10(double)
	 */
	public static final Function log10 = Math::log10;
	
	/**
	 * @see Math#abs(double)
	 */
	public static final Function abs = Math::abs;
	
	
}
