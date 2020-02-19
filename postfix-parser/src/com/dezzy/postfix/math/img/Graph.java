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
		
		final double pixPerWinX = format.pixelDim.width / format.windowWidth;
		final double pixPerWinY = format.pixelDim.height / format.windowHeight;
		
		final double gridXSpacing = format.gridSpacing * pixPerWinX;
		final double gridYSpacing = format.gridSpacing * pixPerWinY;
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		
		g2.setColor(Color.BLACK);
		
		for (int y = 0; y < height; y += gridYSpacing) {
			g2.drawLine(0, y, width, y);
		}
		
		for (int x = 0; x < width; x += gridXSpacing) {
			g2.drawLine(x, 0, x, height);
		}
		
		return out;
	}
}
