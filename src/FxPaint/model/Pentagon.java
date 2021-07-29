
package FxPaint.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Pentagon extends Shape{	  
	double px[] = new double[5];
	double py[] = new double[5];
    public Pentagon(Point2D startPos, Point2D endPos, Color strockColor,Color fillcolor,Double size) {
        super(startPos, endPos, strockColor,fillcolor,size);        
        double x1 = startPos.getX();
        double y1 = startPos.getY();
        double x2 = endPos.getX();
        double y2 = endPos.getY();
        double center_x = (x1+x2)/2;
		double center_y = (y1+y2)/2;
		double radius = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1))/2;		
		Double angle = 2*Math.PI/5;
        if(super.getPosition().getX()<super.getEndPosition().getX()){        	
        	for (int i=0; i<5; i++){        		    		            		
    		    px[i] = center_x+radius*Math.sin(i*angle+120);//120--erect null--invert
    		    py[i] = center_y+radius*Math.cos(i*angle+120);
    		}
        }else{
        	for (int i=0; i<5; i++){        		    		            		
    		    px[i] = center_x-radius*Math.sin(i*angle+120);
    		    py[i] = center_y-radius*Math.cos(i*angle+120);
    		}
        }
    }
    public Pentagon() {}
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
        gc.strokePolygon(px, py,5);               
        gc.fillPolygon(px,py,5);
    }       
}
