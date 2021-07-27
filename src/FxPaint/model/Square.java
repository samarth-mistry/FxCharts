
package FxPaint.model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;


public class Square extends Rectangle{    
    public Square(Point2D startPos, Point2D endPos, Color strockColor, Color fillcolor,Double size) {
        super(startPos, endPos, strockColor,fillcolor,size);
        if(super.getHeight()<super.getWidth()){
            super.setWidth(super.getHeight());
        }else{
            super.setHeight(super.getWidth());
        }
    }
    public Square() {}    
}
