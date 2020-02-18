package com.dezzy.postfix.math.img;

import java.awt.Dimension;
import java.io.Serializable;

/**
 * Specifies formatting information for a graph of an {@link com.dezzy.postfix.math.symbolic.structure.Expression Expression}.
 *
 * @author Joe Desmond
 */
public final class GraphFormat implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1915158812490470117L;

	/**
	 * Pixel dimensions of the graph (size of the image)
	 */
	public final Dimension pixelDim;
	
	/**
	 * Window dimensions of the graph (range of values to display in the image)
	 */
	public final Dimension windowDim;
	
	/**
	 * Interval between each gridline
	 */
	public final int gridSpacing;
	
	/**
	 * Creates a graph format with the given properties.
	 * 
	 * @param _pixelDim size of the image
	 * @param _windowDim range of function values to fit in the image
	 * @param _gridSpacing interval between each gridline, in window coordinates
	 */
	public GraphFormat(final Dimension _pixelDim, final Dimension _windowDim, final int _gridSpacing) {
		pixelDim = _pixelDim;
		windowDim = _windowDim;
		gridSpacing = _gridSpacing;
	}
}
