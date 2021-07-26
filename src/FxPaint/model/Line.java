package FxPaint.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Line extends Shape{    
    private double length;
    
    public Line(Point2D start, Point2D end, Color color,Double size){
        super(start,end,color,size);
        length = start.distance(end);
    }
    public Line() {}    
    @Override
    public void setFillColor(Color color){setColor(color);}    
    @Override
    public void setTopLeft(Point2D x){
        Point2D temp = x.subtract(this.getPosition());
        this.setPosition(x);
        this.setEndPosition(this.getEndPosition().add(temp));
        super.setTopLeft(x);
    }
    @Override
    public void draw(Canvas canvas){
        double x1 = super.getPosition().getX();
        double y1 = super.getPosition().getY();
        double x2 = super.getEndPosition().getX();
        double y2 = super.getEndPosition().getY();
        GraphicsContext gc = canvas.getGraphicsContext2D();      
        gc.setStroke(super.getColor());        
        //gc.setFont(new Font("Helvetica", super.getStrokeSize()));
        gc.setLineWidth(super.getStrokeSize());
        gc.strokeLine(x1,y1,x2,y2);
    }    
}
