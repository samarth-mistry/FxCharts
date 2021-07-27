
package FxPaint.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Pentagon extends Shape{
    private Point2D thirdPoint;
    private Point2D fourthPoint;
    private Point2D fifthPoint;
    public Pentagon(Point2D startPos, Point2D endPos, Color strockColor,Color fillcolor,Double size) {
        super(startPos, endPos, strockColor,fillcolor,size);
        double temp = Math.abs(startPos.getX() - endPos.getX());
        if(super.getPosition().getX()<super.getEndPosition().getX()){
            thirdPoint = new Point2D(endPos.getX()-(temp*2), endPos.getY());
            fourthPoint = new Point2D(startPos.getX()-(temp*3), startPos.getY());
            fifthPoint = new Point2D(endPos.getX()-(temp*4), endPos.getY());
        }else{
        	System.out.println("fjdj");
            thirdPoint = new Point2D(endPos.getX()+(temp*2), endPos.getY());
            fourthPoint = new Point2D(startPos.getX()+(temp*3), startPos.getY());
            fifthPoint = new Point2D((startPos.getX()+(temp*3))*4, (endPos.getY()));
        }
    }
    public Pentagon() {}    
    @Override
    public void setTopLeft(Point2D x){
	     Point2D temp = x.subtract(this.getPosition());
	     this.setPosition(x);
	     this.setEndPosition(this.getEndPosition().add(temp));
	     this.thirdPoint = this.thirdPoint.add(temp);
	     super.setTopLeft(x);
    }    
    @Override
    protected void getPropertiesToMap(){
        super.getPropertiesToMap();
        super.addToProperties("thirdPointX", thirdPoint.getX());
        super.addToProperties("thirdPointY", thirdPoint.getY());
    }    
    @Override
    protected void setPropertiesToVariables(){
        super.setPropertiesToVariables();
        thirdPoint = new Point2D(super.getFromMap("thirdPointX"),super.getFromMap("thirdPointY"));
    }    
    @Override
    public void draw(Canvas canvas){
        double x1 = super.getPosition().getX();
        double y1 = super.getPosition().getY();
        double x2 = super.getEndPosition().getX();
        double y2 = super.getEndPosition().getY();
        double x3 = thirdPoint.getX();
        double y3 = thirdPoint.getY();
        double x4 = fourthPoint.getX();
        double y4 = fourthPoint.getY();
        double x5 = fifthPoint.getX();
        double y5 = fifthPoint.getY();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(super.getColor());
        gc.setFill(super.getFillColor());        
        gc.setLineWidth(super.getStrokeSize());               
        gc.strokePolygon(new double[]{x1,x2,x3,x4,x5}, new double[]{y1,y2,y3,y4,y5}, 5);               
        gc.fillPolygon(new double[]{x1,x2,x3,x4,x5}, new double[]{y1,y2,y3,y4,y5}, 5);
    }       
}
