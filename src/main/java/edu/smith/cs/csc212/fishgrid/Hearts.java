package edu.smith.cs.csc212.fishgrid;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Area;

public class Hearts extends WorldObject{
	int color;
	Area shapeAll;
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
		Shape diamond = createDiamond(g, 2);
//		Area diamondA = new Area(diamondA);
		//Ellipse2D.Double 
		Shape circle = new Ellipse2D.Double(10, 10, 10, 10);
		Shape circle2 = new Ellipse2D.Double(10, 10, 10, 10);
		Area shapeAll = new Area(circle);
		shapeAll.add(new Area(circle2));
		shapeAll.add(new Area(diamond));
		
		  }
		
	
	@Override
	public void draw(Graphics2D g) { //draws circle
		g.setColor(getHTColor());
		g.fill(shapeAll);
		
		
		
	}
	@Override
	public void step() {
			
	}
}

