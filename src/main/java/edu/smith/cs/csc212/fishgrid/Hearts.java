package edu.smith.cs.csc212.fishgrid;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Area;

public class Hearts extends WorldObject{
	int color;

	private static Color[] HT_COLORS = new Color[] {
			new Color(255, 128, 191),
			new Color(255, 179, 217),
			new Color(255, 153, 255),
			new Color(255, 77, 148),
			new Color(255, 136, 77),
			new Color(210, 77, 255)
			
	};
	
	public Color getHTColor() {
		return HT_COLORS[this.color];
	}
	public Hearts(int color, World world) {
		super(world);
		this.color = color;
	}
	
	/**
	 * Utility methods for {@link Shape} objects.
	 *
	 * @author David Gilbert
	 */
	//public class drawPolygon {
	//	float s;
	  /**
	   * Creates a diamond shape.
	   * @param s  the size factor (equal to half the height of the diamond).
	   * @return A diamond shape.
	   */
	public static Shape createDiamond(Graphics2D g, final float s) {
	      final GeneralPath p0 = new GeneralPath();
	      p0.moveTo(0.0f, -s);
	      p0.lineTo(s, 0.0f);
	      p0.lineTo(0.0f, s);
	      p0.lineTo(-s, 0.0f);
	      p0.closePath();
	      return p0;
	     
	}
	public void heartShape(Graphics2D g) {
		final float s;
		Shape diamond = createDiamond(g, 2);
//		Area diamondA = new Area(diamondA);
		//Ellipse2D.Double 
		Shape circle = new Ellipse2D.Double(10, 10, 10, 10);
		Shape circle2 = new Ellipse2D.Double(10, 10, 10, 10);
		Area shapeAll = new Area(circle);
		shapeAll.add(new Area(circle2));
		shapeAll.add(new Area(diamond));
		
		  }
//		   public static void draw(Graphics2D g, float s) { 
//			   createDiamond(g, s);
//
//			} 
		
	
	@Override
	public void draw(Graphics2D g) { //draws circle
		g.setColor(getHTColor());
	//	g.fill(shapeAll);
		
		//g.fill(circle);
		
	}
	@Override
	public void step() {
		// Rocks don't actually *do* anything.		
	}
}
/////////////////////UNUSED POLYGON CODEEE
//public class Heart {
//    public static void main(String[] args) {
//        StdDraw.setXscale(-1.5, +1.5);
//        StdDraw.setYscale(-1.5, +1.5);
//        StdDraw.setPenColor(StdDraw.PINK);
//
//        // draw diamond
//        double[] xs = { -1,  0, 1, 0 };
//        double[] ys = {  0, -1, 0, 1 };
//        StdDraw.filledPolygon(xs, ys);
//
//        // circles
//        StdDraw.filledCircle(+0.5, 0.5, 1 / Math.sqrt(2));
//        StdDraw.filledCircle(-0.5, 0.5, 1 / Math.sqrt(2));
//    }
//
//}
// above from https://introcs.cs.princeton.edu/java/15inout/Heart.java.html 
///**
// * Draw a polygon which passes through all the points drawn by the user.
// *
// * @param g2d        the graphics object
// * @param polygon    the polygon to be drawn on the interface
// * @param color      the color of the polygon
// * @param dashedLine true = the line used for drawing the polygon will be dashed; false = the line will be solid/plain
// */
//public class drawPolygon {
//	
//	Polygon polygon;
//	Color color; 
//	boolean dashedLine;
//	
//	public Polygon(Graphics2D g2d, Polygon polygon, Color color, boolean dashedLine) {
//		this.polygon = polygon;
//		this.color = color;
//		boolean dashedLine = dashedLine;
//		public drawPolygon(Graphics2D g2d, Polygon polygon, Color color, boolean dashedLine) {
//			
//		}
//		g2d.setStroke(dashedLine ? getDashedStroke() : new BasicStroke(0));
//	
//	    if (polygon.npoints > 0) {
//	        g2d.setColor(color);
//	        g2d.drawPolygon(polygon);
//	
//	        // make the line thicker
//	        g2d.setStroke(new BasicStroke(2));
//	
//	        for (int index = 0; index < polygon.npoints; index++) {
//	            g2d.drawLine(polygon.xpoints[index], polygon.ypoints[index],
//	                    polygon.xpoints[index], polygon.ypoints[index]);
//	        }
//	    }
//	
//	    g2d.setStroke(new BasicStroke(0));
//	}
//	 /**
//     * Get the configuration of the Stroke in such way as to have a dashed line.
//     *
//     * @return the basic stroke representing a dashed line
//     */
//    public static BasicStroke getDashedStroke() {
//        return new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{2f}, 0f);
//    }
//    public void draw(Graphics2D g) { 
//		this.drawPolygon(g, polygon, color, dashedLine); 
//
//	}
//}
//public class Diamond extends Path2D.Double { sep thing
//
//    public Diamond(double width, double height) {
//        moveTo(0, height / 2);
//        lineTo(width / 2, 0);
//        lineTo(width, height / 2);
//        lineTo(width / 2, height);
//        closePath();
//    }
//
//}


