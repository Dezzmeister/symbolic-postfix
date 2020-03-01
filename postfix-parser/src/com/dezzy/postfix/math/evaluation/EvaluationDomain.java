package com.dezzy.postfix.math.evaluation;

import java.util.Map;

import com.dezzy.postfix.math.symbolic.constants.Constant;

/**
 * A set of values to pass through an {@link com.dezzy.postfix.symbolic.structure.Expression Expression}. 
 * 
 * @author Joe Desmond
 */
public final class EvaluationDomain {
	public final Map<String, Constant> constants;
	
	public final Map<String, double[]> variableDomain;
	
	public EvaluationDomain(final Map<String, Constant> _constants, final Map<String, double[]> _variableDomain) {
		constants = _constants;
		variableDomain = _variableDomain;
	}
}
