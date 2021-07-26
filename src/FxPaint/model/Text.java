package FxPaint.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Text extends Shape{
	String textVal = "";		
	public Text(Point2D startPos,Point2D endPos, String tevo,Color strockColor,Double size) {
		super(startPos,endPos,strockColor,size);
		textVal = tevo;		
    }
	public Text() {}    
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
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(super.getColor());
        gc.setFont(new Font("Helvetica", super.getStrokeSize()));
        gc.fillText(textVal, x1, y1);                       
    }
}
