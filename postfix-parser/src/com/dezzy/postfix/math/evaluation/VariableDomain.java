package com.dezzy.postfix.math.evaluation;

/**
 * A set of discrete, ordered values to be assumed by a variable. This class is used in {@link EvaluationDomain},
 * which is used by {@link com.dezzy.postfix.symbolic.structure.Expression Expression}
 * to set the values to be assumed by a variable for consecutive evaluations.
 * 
 * @author Joe Desmond
 */
public final class VariableDomain {
	
	/**
	 * Variable name
	 */
	public final String varName;
	
	/**
	 * Values of this variable
	 */
	public final double[] values;
	
	/**
	 * Creates a domain in which a variable takes on the specified values.
	 * 
	 * @param _varName variable name
	 * @param _values domain of <code>_varName</code>
	 */
	public VariableDomain(final String _varName, final double[] _values) {
		varName = _varName;
		values = _values;
	}
}
