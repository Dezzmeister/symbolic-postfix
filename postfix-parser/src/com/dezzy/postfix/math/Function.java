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
	public double accept(final double x);
	
	public static final Function sin = Math::sin;
	
	public static final Function cos = Math::cos;
	
	public static final Function tan = Math::tan;
	
	public static final Function invsin = Math::asin;
	
	public static final Function invcos = Math::acos;
	
	public static final Function invtan = Math::atan;
	
	public static final Function sinh = Math::sinh;
	
	public static final Function cosh = Math::cosh;
	
	public static final Function tanh = Math::tanh;
	
	public static final Function ln = Math::log;
	
	public static final Function log10 = Math::log10;
	
	public static final Function abs = Math::abs;
	
	
}
