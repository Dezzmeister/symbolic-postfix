package com.dezzy.postfix.math.symbolic.structure;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A mathematical unknown; typically a variable (<code>x,y,a,</code>etc.) or a known constant like <code>pi</code>
 * or <code>e</code>. Can only be evaluated when a mapping of known values is provided.
 * 
 * @author Joe Desmond
 */
public final class Unknown implements Expression {
	
	/**
	 * String representation of this unknown
	 */
	public final String varName;
	
	/**
	 * Constructs an Unknown from the given unknown name.
	 * 
	 * @param _varName unknown name, can be a variable name or a mathematical constant
	 */
	public Unknown(final String _varName) {
		varName = _varName;
	}
	
	/**
	 * Attempts to find a mapping for the name of this unknown in the given constants map.
	 * 
	 * @param constants maps String variable/constant names to double values. This is required when evaluating an Unknown
	 * @return value of this unknown
	 * @throws IllegalArgumentException if no mapping exists 
	 */
	@Override
	public final double evaluate(final Map<String, Double> constants) {
		final Double value = constants.get(varName);
		
		if (value == null) {
			throw new IllegalArgumentException("\"" + varName + "\" is unknown!");
		} else {
			return value.doubleValue();
		}
	}
	
	/**
	 * Returns true if the given constants mapping contains a definition for this Unknown.
	 * 
	 * @param constants known constants
	 * @return true if this Unknown is defined in <code>constants</code>
	 */
	@Override
	public final boolean canEvaluate(final Map<String, Double> constants) {
		return constants.containsKey(varName);
	}
	
	/**
	 * Simplifies this Unknown. If a mapping for this Unknown exists in <code>constants</code>, returns
	 * a {@link Value} with the value of this unknown. Otherwise, returns this unknown.
	 * 
	 * @param constants known constants
	 * @return a simplified Value, or <code>this</code> if this cannot be simplified
	 */
	@Override
	public final Expression simplify(final Map<String, Double> constants) {
		if (canEvaluate(constants)) {
			return new Value(evaluate(constants));
		} else {
			return this;
		}
	}
	
	/**
	 * Returns true if this Unknown is the specified variable.
	 * 
	 * @param varName variable name
	 * @return true if this Unknown is the specified variable
	 */
	@Override
	public final boolean isFunctionOf(final String varName) {
		return varName.equals(this.varName);
	}
	
	/**
	 * Returns the derivative of this Unknown with respect to <code>varName</code>,
	 * which is either one or zero. If this Unknown is the specified variable,
	 * the derivative is one, otherwise the derivative is zero.
	 * 
	 * @param varName variable name
	 * @return zero or one (as a {@link Value})
	 */
	@Override
	public final Expression derivative(final String varName) {
		if (varName.equals(this.varName)) {
			return Value.ONE;
		} else {
			return Value.ZERO;
		}
	}
	
	/**
	 * Returns true if the known constants map has an entry for this Unknown.
	 * If there is no entry, then this Unknown is a variable.
	 * 
	 * @param constants known constants map
	 * @return true if <code>constants</code> contains {@link #varName}
	 */
	@Override
	public final boolean hasConstantTerm(final Map<String, Double> constants) {
		return constants.containsKey(varName);
	}
	
	/**
	 * Returns true, because an Unknown is a simple mathematical unit.
	 * 
	 * @return true
	 */
	@Override
	public final boolean isSimple() {
		return true;
	}
	
	/**
	 * Returns this Unknown, because it already isn't a decimal.
	 * 
	 * @param constants known constants
	 * @return this Unknown
	 */
	@Override
	public final Expression cleanDecimals(final Map<String, Double> constants) {
		return this;
	}
	
	/**
	 * If this Unknown is a known constant, returns an empty List; otherwise
	 * returns a List with this as its only element.
	 * 
	 * @param constants known constants
	 * @return empty List or a List with this
	 */
	@Override
	public final List<Unknown> getUnknowns(final Map<String, Double> constants) {
		if (constants.containsKey(varName)) {
			return List.of();
		} else {
			return List.of(this);
		}
	}
	
	/**
	 * Returns the symbolic name of this unknown. Does not know or care about the value.
	 * 
	 * @return the symbolic name of this unknown
	 */
	@Override
	public final String toString() {
		return varName;
	}
	
	/**
	 * If a mapping for this Unknown exists in <code>latexMappings</code>, returns the LaTeX
	 * representation specified. <br>
	 * If no mapping exists, returns the name of this Unknown, converted to a String. For Unknowns, the LaTeX representation
	 * is equivalent to the plain String representation.
	 * 
	 * @param latexMappings user-specified LaTeX representations of named constants or variables
	 * @return LaTeX representation
	 */
	@Override
	public final String toLatex(final Map<String, String> latexMappings) {
		if (latexMappings.containsKey(varName)) {
			return latexMappings.get(varName);
		} else {
			return varName;
		}
	}
	
	/**
	 * Returns true if these two Unknowns are the same variable.
	 * 
	 * @param other other Unknown
	 * @return true if these Unknowns are obviously equal
	 */
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Unknown)) {
			return false;
		} else {
			final Unknown otherUnknown = (Unknown) other;
			return varName.equals(otherUnknown.varName);
		}
	}
	
	/**
	 * Returns the hashcode of this Unknown by hashing its variable name.
	 * 
	 * @return hashcode of this Unknown
	 */
	@Override
	public int hashCode() {
		return Objects.hash(varName);
	}
}
