package com.dezzy.postfix.math.img;

import java.awt.Dimension;
import java.io.Serializable;

/**
 * Specifies formatting information for a graph of an {@link com.dezzy.postfix.math.symbolic.structure.Expression Expression}. <br>
 * In documentation for this class, pixel space refers to the image itself, which is defined by pixel coordinates. <br>
 * Function space refers to the space in which the expression is plotted, which is defined by "window" coordinates.
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
	 * Range of x values to display in the graph
	 */
	public final double windowWidth;
	
	/**
	 * Range of y values to display in the graph
	 */
	public final double windowHeight;
	
	/**
	 * The x coordinate at which the window shown begins
	 */
	public final double windowStartX;
	
	/**
	 * The y coordinate at which the window shown begins
	 */
	public final double windowStartY;
	
	/**
	 * Interval between each gridline
	 */
	public final int gridSpacing;
	
	/**
	 * Creates a graph format with the given properties.
	 * 
	 * @param _pixelDim size of the image (pixel space)
	 * @param _windowWidth range of x values to display in the graph (function space)
	 * @param _windowHeight range of y values to display in the graph (function space)
	 * @param _windowStartX x coordinate at which window begins (function space)
	 * @param _windowStartY y coordinate at which window begins (function space)
	 * @param _gridSpacing interval between each gridline, in window coordinates
	 */
	public GraphFormat(final Dimension _pixelDim, final double _windowWidth, final double _windowHeight, final double _windowStartX, final double _windowStartY, final int _gridSpacing) {
		pixelDim = _pixelDim;
		windowWidth = _windowWidth;
		windowHeight = _windowHeight;
		windowStartX = _windowStartX;
		windowStartY = _windowStartY;
		gridSpacing = _gridSpacing;
	}
}
