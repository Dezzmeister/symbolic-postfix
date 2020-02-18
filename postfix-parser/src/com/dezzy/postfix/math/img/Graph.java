package com.dezzy.postfix.math.img;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Expression;

public final class Graph {
	public final GraphFormat format;
	
	public final Expression expression;
	
	public final String varName;
	
	public final Map<String, Constant> constants;
	
	public final BufferedImage image;
	
	public Graph(final Expression _expression, final String _varName, final Map<String, Constant> _constants, final GraphFormat _format) {
		expression = _expression;
		varName = _varName;
		constants = _constants;
		format = _format;
		image = createGraph();
	}
	
	//TODO: Finish this
	private final BufferedImage createGraph() {
		final BufferedImage out = new BufferedImage(format.pixelDim.width, format.pixelDim.height, BufferedImage.TYPE_INT_RGB);
		final int width = out.getWidth();
		final int height = out.getHeight();
		final Graphics2D g2 = out.createGraphics();
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		
		g2.setColor(Color.BLACK);
		
		return out;
	}
}
