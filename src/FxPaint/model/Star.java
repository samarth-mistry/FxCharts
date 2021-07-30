
package FxPaint.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Star extends Shape{	  
	double px[] = new double[10];
	double py[] = new double[10];
    public Star(Point2D startPos, Point2D endPos, Color strockColor,Color fillcolor,Double size) {
        super(startPos, endPos, strockColor,fillcolor,size);        
        double x1 = startPos.getX();
        double y1 = startPos.getY();
        double x2 = endPos.getX();
        double y2 = endPos.getY();
        double center_x = (x1+x2)/2;
		double center_y = (y1+y2)/2;
		double radius = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1))/2;		
		Double angle = 2*Math.PI/10;
		//Double int_angle = 
		double theta = Math.atan2((y2 - y1), (x2 - x1));
        if(super.getPosition().getX()<super.getEndPosition().getX()){        	
        	for (int i=0; i<10; i++){
        		if(i%2==0) {
        			px[i] = center_x+radius*Math.sin(i*angle+theta+30);
    		    	py[i] = center_y+radius*Math.cos(i*angle+theta+30);
        		}else {
        			px[i] = center_x+radius*Math.sin(i*angle+theta+30)/2;
    		    	py[i] = center_y+radius*Math.cos(i*angle+theta+30)/2;
        		}
    		}
        }else{
        	for (int i=0; i<10; i++){
        		if(i%2==0) {
        			px[i] = center_x-radius*Math.sin(i*angle+theta+30);
        		    py[i] = center_y-radius*Math.cos(i*angle+theta+30);
        		}else {
        			px[i] = center_x-radius*Math.sin(i*angle+theta+30)/2;
        		    py[i] = center_y-radius*Math.cos(i*angle+theta+30)/2;
        		}    		    
    		}
        }
    }
    public Star() {}
    @Override
    public void setTopLeft(Point2D x){
	     Point2D temp = x.subtract(this.getPosition());
	     this.setPosition(x);
	     this.setEndPosition(this.getEndPosition().add(temp));	     
	     super.setTopLeft(x);
    }    
    @Override
    protected void getPropertiesToMap(){
        super.getPropertiesToMap();        
    }    
    @Override
    protected void setPropertiesToVariables(){
        super.setPropertiesToVariables();        
    }    
    @Override
    public void draw(Canvas canvas){    	
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(super.getColor());
        gc.setFill(super.getFillColor());        
        gc.setLineWidth(super.getStrokeSize());               
        gc.strokePolygon(px, py,10);               
        gc.fillPolygon(px,py,10);
    }       
}
