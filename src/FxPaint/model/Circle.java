
package FxPaint.model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;


public class Circle extends Ellipse{  
    public Circle(Point2D startPos, Point2D endPos, Color strockColor, Color fillcolor,Double size) {
        super(startPos, endPos, strockColor,fillcolor,size);
        if(super.gethRadius()<super.getvRadius()){
            super.setvRadius(super.gethRadius());
        }else{
            super.sethRadius(super.getvRadius());
        }        
    }
    public Circle() {}
}
