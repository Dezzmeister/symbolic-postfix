package com.dezzy.postfix.math.evaluation;

import java.util.Map;

import com.dezzy.postfix.math.symbolic.constants.Constant;

/**
 * A set of values to pass through an {@link com.dezzy.postfix.symbolic.structure.Expression Expression}. 
 * 
 * @author Joe Desmond
 */
public final class EvaluationDomain {
	
	/**
	 * Known constants and named values.
	 */
	public final Map<String, Constant> constants;
	
	/**
	 * Given variables and associated discrete domains. Each variable will take on the values in
	 * its domain during evaluation.
	 */
	public final VariableDomain[] variableDomains;
	
	/**
	 * Creates an evaluation domain from the given constants and variables. The size of each domain in
	 * <code>_variableDomains</code> should be equal; i.e., {@link VariableDomain#values} should have the same length
	 * for every element of <code>_variableDomains</code>.
	 * 
	 * @param _constants known constants and named values
	 * @param _variableDomains variable domains
	 * @see {@link #variableDomains}
	 */
	public EvaluationDomain(final Map<String, Constant> _constants, final VariableDomain[] _variableDomains) {
		constants = _constants;
		variableDomains = _variableDomains;
	}
}
