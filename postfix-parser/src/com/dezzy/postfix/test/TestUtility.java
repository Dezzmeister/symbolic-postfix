package com.dezzy.postfix.test;

import com.dezzy.postfix.math.Reserved;
import com.dezzy.postfix.math.evaluation.EvaluationDomain;
import com.dezzy.postfix.math.evaluation.VariableDomain;

/**
 * Various testing utilities.
 *
 * @author Joe Desmond
 */
public final class TestUtility {
	
	/**
	 * Range of values in {@link #TEST_X_DOMAIN}
	 */
	public static final double TEST_X_DOMAIN_RANGE = 20;
	
	/**
	 * Offset of values in {@link #TEST_X_DOMAIN}
	 */
	public static final double TEST_X_DOMAIN_OFFSET = -10;
	
	/**
	 * An evaluation domain for testing functions of x, with known constants {@link Reserved#constants}
	 */
	public static final EvaluationDomain TEST_X_DOMAIN;
	
	static {		
		final double[] domain = new double[1000];
		
		for (int i = 0; i < domain.length; i++) {
			double value = ((i / (double) domain.length) * TEST_X_DOMAIN_RANGE) + TEST_X_DOMAIN_OFFSET;
			domain[i] = value;
		}
		
		final VariableDomain[] xDomain = {new VariableDomain("x", domain)};
		TEST_X_DOMAIN = new EvaluationDomain(Reserved.constants, xDomain);
	}
	
	
}
