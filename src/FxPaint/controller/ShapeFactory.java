
package FxPaint.controller;

import java.util.HashMap;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import FxPaint.model.*;


//Factory DP
public class ShapeFactory {
    
    public ShapeFactory(){}
    public Shape createShape(String type, double lx, double ly,String tevo, Color color){
    	Shape temp=null;		//currently used for test only
    	Point2D start = new Point2D(lx,ly);
    	Point2D end = new Point2D(lx*tevo.length()*10,ly*tevo.length()*10);    	
    	temp = new Text(start,end,tevo,color);
		return temp;    	
    }
    public Shape createShape(String type, Point2D start, Point2D end, Color color){
        Shape temp=null;
        switch(type){
            case"Circle": temp = new Circle(start,end,color);break;
            case"Ellipse": temp = new Ellipse(start,end,color);break;
            case"Rectangle": temp = new Rectangle(start,end,color);break;
            case"Square": temp = new Square(start,end,color);break;
            case"Line": temp = new Line(start,end,color);break;
            case"Triangle": temp = new Triangle(start,end,color);break;            
        }
        return temp;
    }    
    public Shape createShape(String type, HashMap<String,Double> m){
        Shape temp=null;
        switch(type){
            case"Circle": temp = new Circle();break;
            case"Ellipse": temp = new Ellipse();break;
            case"Rectangle": temp = new Rectangle();break;
            case"Square": temp = new Square();break;
            case"Line": temp = new Line();break;
            case"Triangle": temp = new Triangle();break;
        }
        temp.setProperties(m);
        return temp;
    }
}
