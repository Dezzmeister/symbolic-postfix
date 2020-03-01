package com.dezzy.postfix.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dezzy.postfix.math.Reserved;
import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Value;

public class TestUtility {
	/**
	 * Huge waste of memory
	 */
	public static final List<Map<String, Constant>> TEST_X_DOMAIN;
	
	static {
		TEST_X_DOMAIN = new ArrayList<Map<String, Constant>>();
		
		for (double d = -2.0; d < 2.0; d += 0.01) {
			final Map<String, Constant> val = new HashMap<String, Constant>();
			val.put("x", new Constant(new Value(d)));
			final Map<String, Constant> constants = Reserved.getCompleteConstantsMap(val);
			TEST_X_DOMAIN.add(constants);
		}
	}
	
	
}
