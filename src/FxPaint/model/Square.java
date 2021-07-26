
package FxPaint.model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;


public class Square extends Rectangle{    
    public Square(Point2D startPos, Point2D endPos, Color strockColor,Double size) {
        super(startPos, endPos, strockColor,size);
        if(super.getHeight()<super.getWidth()){
            super.setWidth(super.getHeight());
        }else{
            super.setHeight(super.getWidth());
        }
    }
    public Square() {}    
}
