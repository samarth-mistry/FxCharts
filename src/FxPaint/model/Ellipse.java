
package FxPaint.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class Ellipse extends Shape{
    private double hRadius;
    private double vRadius;
    public Ellipse() {}
    public void sethRadius(double hRadius) {this.hRadius = hRadius;}
    public void setvRadius(double vRadius) {this.vRadius = vRadius;}
    public double gethRadius() {return hRadius;}
    public double getvRadius() {return vRadius;}    
    public Ellipse(Point2D startPos, Point2D endPos, Color strockColor,Double size) {
        super(startPos, endPos, strockColor,size);
        hRadius = Math.abs(startPos.getX() - endPos.getX())/2;
        vRadius = Math.abs(startPos.getY() - endPos.getY())/2;        
    }    
    @Override
    protected void getPropertiesToMap(){
        super.getPropertiesToMap();
        super.addToProperties("hRadius", hRadius);
        super.addToProperties("vRadius", vRadius);
    }    
    @Override
    protected void setPropertiesToVariables(){
        super.setPropertiesToVariables();
        hRadius = super.getFromMap("hRadius");
        vRadius = super.getFromMap("vRadius");
    }    
    @Override
    public void draw(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(super.getColor());
        gc.setLineWidth(super.getStrokeSize());
        gc.strokeOval(super.getTopLeft().getX(), super.getTopLeft().getY(), hRadius*2, vRadius*2);
        gc.setFill(super.getFillColor());
        //gc.setFont(new Font("Helvetica", super.getStrokeSize()));        
        //gc.fillOval(super.getTopLeft().getX(), super.getTopLeft().getY(), hRadius*2, vRadius*2);
    }       
}
