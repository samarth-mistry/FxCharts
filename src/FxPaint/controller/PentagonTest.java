package FxPaint.controller;

import javafx.geometry.Point2D;

public class PentagonTest {
	static Point2D a = new Point2D(3,3);
	static Point2D b = new Point2D(1,3);
	static Double x1 = a.getX();
	static Double y1 = a.getY();
	static Double x2 = b.getX();
	static Double y2 = b.getY();
	Double xc;
	Double yc;
	public static void main(String[] args) {
		Double x = null,y=null;
		Double cDis = null;
		x = (a.getX()+b.getX())/2;
		y = (a.getY()+b.getY())/2;
		Point2D center = new Point2D(x,y);
		Double radius = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1))/2;
		System.out.println("Center: "+center+"Len: "+radius);
		Double angle = 2 * Math.PI / 5;
		for (int i = 0; i < 5; i++)
		{
		    x = center.getX() + radius*Math.sin(i * angle);
		    y = center.getY() + radius*Math.cos(i * angle);
		    System.out.println("["+x+","+y+"]");
		}
	}
}
