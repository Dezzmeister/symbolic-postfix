package com.dezzy.postfix.math.img;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.dezzy.postfix.math.Reserved;
import com.dezzy.postfix.math.symbolic.constants.Constant;
import com.dezzy.postfix.math.symbolic.structure.Expression;
import com.dezzy.postfix.math.symbolic.structure.Value;
import com.dezzy.postfix.math.vector.Vector;

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
		final int shortHeight = height - 1;
		final Graphics2D g2 = out.createGraphics();
		
		final double pixPerWinX = format.pixelDim.width / format.windowWidth;
		final double pixPerWinY = format.pixelDim.height / format.windowHeight;
		
		final double gridXSpacing = format.gridSpacing * pixPerWinX;
		final double gridYSpacing = format.gridSpacing * pixPerWinY;
		
		final double[] xVals = new double[width];
		
		for (int i = 0; i < xVals.length; i++) {
			xVals[i] = format.windowStartX + (i / pixPerWinX);
		}
		
		final Map<String, Constant> vars = Reserved.getCompleteConstantsMap(new HashMap<String, Constant>());		
		
		final Vector domain = new Vector(xVals);
		final Vector range = domain.transform(x -> {
			vars.put(varName, new Constant(new Value(x)));
			return expression.evaluate(vars);
		});
		
		final Vector pixelYVals = range.transform(y -> (pixPerWinY * y) - format.windowStartY);
		
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		
		g2.setColor(Color.BLACK);
		
		for (int y = 0; y < height; y += gridYSpacing) {
			g2.drawLine(0, shortHeight - y, width, shortHeight - y);
		}
		
		for (int x = 0; x < width; x += gridXSpacing) {
			g2.drawLine(x, 0, x, height);
		}
		
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.GREEN);
		
		for (int i = 0; i < pixelYVals.dimension - 1; i++) {
			final int x1 = i;
			final double y1 = pixelYVals.get(i);
			final int x2 = i + 1;
			final double y2 = pixelYVals.get(i + 1);
			
			if (!(Double.isNaN(y1) || Double.isNaN(y2))) {
				g2.drawLine(x1, shortHeight - (int) y1, x2, shortHeight - (int) y2 - 1);
			}
		}
		
		return out;
	}
}
