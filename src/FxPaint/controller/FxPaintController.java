package FxPaint.controller;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Popup;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

import FxPaint.model.*;
import Main.FxChartMainPage;

@SuppressWarnings("deprecation")
public class FxPaintController implements Initializable, DrawingEngine {
	@FXML private AnchorPane anchor;@FXML private ChoiceBox<String> textStyle;@FXML private MenuBar menuBar;
	@FXML private Slider objSize;@FXML private TextField sldrValue;
	@FXML private ColorPicker ColorBox;@FXML private ColorPicker fillColorBox;
	@FXML private Label Message;@FXML private Label cords;@FXML private Canvas CanvasBox;
    @FXML private Button DeleteBtn;@FXML private Button UndoBtn;@FXML private Button RedoBtn;@FXML private Button SaveBtn;@FXML private Button MoveBtn;
    @FXML private Button RecolorBtn;@FXML private Button LoadBtn;@FXML private Button ref_btn;@FXML private Button cpClpBrdBtn;
    @FXML private Button StartBtn;@FXML private Button ResizeBtn;@FXML private Button ImportBtn;@FXML private Button CopyBtn;    
    @FXML private ToggleButton themeToggle;@FXML private TextArea tevo;
    @FXML private ToggleButton cir;@FXML private ToggleButton lin;@FXML private ToggleButton tri;@FXML private ToggleButton rec;@FXML private ToggleButton sq;@FXML private ToggleButton ell;@FXML private ToggleButton txt;@FXML private ToggleButton pen;
    @FXML private ToggleButton pent;@FXML private ToggleButton hex;@FXML private ToggleButton star;@FXML private ToggleButton eras;
    @FXML private ListView<String> ShapeList;    
    
    final KeyCombination alt1= new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.ALT_DOWN),alt2= new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.ALT_DOWN),alt3= new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.ALT_DOWN);        
    final KeyCombination alt4= new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.ALT_DOWN),alt5= new KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.ALT_DOWN),alt6= new KeyCodeCombination(KeyCode.DIGIT6, KeyCombination.ALT_DOWN);
    final KeyCombination alt7= new KeyCodeCombination(KeyCode.DIGIT7, KeyCombination.ALT_DOWN),alt8= new KeyCodeCombination(KeyCode.DIGIT8, KeyCombination.ALT_DOWN),alt9= new KeyCodeCombination(KeyCode.DIGIT9, KeyCombination.ALT_DOWN);
    final KeyCombination alt10= new KeyCodeCombination(KeyCode.DIGIT0, KeyCombination.ALT_DOWN);final KeyCombination alt11= new KeyCodeCombination(KeyCode.UNDERSCORE, KeyCombination.ALT_DOWN);final KeyCombination alt12= new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.ALT_DOWN);
    final KeyCombination altEnter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
    final KeyCombination ctrll= new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN);final KeyCombination ctrle= new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);final KeyCombination ctrlf= new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlg= new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN);final KeyCombination ctrlz= new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlshiftz= new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
    final KeyCombination ctrlshiftc= new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);final KeyCombination ctrlc= new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);final KeyCombination ctrlx= new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlp = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);final KeyCombination ctrlpn = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);final KeyCombination ctrlQ = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrls= new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);final KeyCombination ctrli= new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN);final KeyCombination ctrla= new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN);final KeyCombination ctrlback= new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.CONTROL_DOWN);    
    //SINGLETON DP
    private static ArrayList<Shape> shapeList = new ArrayList<Shape>();
    private ArrayList<String> freeHandList = new ArrayList<String>();
    private int selectedShape = 1;	//1-cir//2-tri//3-lin//4-rec//5-ell//6-sq
    private boolean move=false, copy=false, resize=false, isFullScr=false;
    GraphicsContext graphicsContext = null,gc = null;
    private Point2D start, end;
    private String biezure = new String(),tovoVal = "";
    private Stack<ArrayList<Shape>> primary = new Stack<ArrayList<Shape>>();
    private Stack<ArrayList<Shape>> secondary = new Stack<ArrayList<Shape>>();
    //initialize---------------------------------------------------------
    public void initialize(URL url, ResourceBundle rb) {
    	cursorMoni(); 
    	objSize.valueProperty().addListener((obs, oldval, newVal) ->objSize.setValue(newVal.intValue()));
    	System.out.println("#initialize");
    	
    	graphicsContext = CanvasBox.getGraphicsContext2D();    	
    	CanvasBox.addEventHandler(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>(){
    		@Override public void handle(MouseEvent event) {
        		if(selectedShape == 8) {        			
        			//biezure = "/";
	                graphicsContext.beginPath();
	                graphicsContext.setFill(ColorBox.getValue());
	                graphicsContext.setLineWidth(objSize.getValue());
	                graphicsContext.moveTo(event.getX(), event.getY());
	                graphicsContext.stroke();
        		}        		
            }
        });
    	CanvasBox.addEventHandler(MouseEvent.MOUSE_DRAGGED,new EventHandler<MouseEvent>(){
            @Override public void handle(MouseEvent event) {
            	if(selectedShape == 8) {
            		graphicsContext.lineTo(event.getX(), event.getY());
                	graphicsContext.stroke();
                	biezure+="("+event.getX()+","+event.getY()+")";                	
            	}
            }
        });
    	CanvasBox.addEventHandler(MouseEvent.MOUSE_RELEASED,new EventHandler<MouseEvent>(){
            @Override public void handle(MouseEvent event) {
            	if(selectedShape == 8) {            		
                	//freeHandList.add("/");
                	freeHandList.add(biezure);
            	}
            }
        });
    	//textStyle.-------------------- 
    	textStyle.getItems().addAll("Arial", "Helvetica","Verdana", "Tahoma","SansSerif","Rachana","DejaVu Sans","DejaVu Sans Mono","DejaVu Serif");
    	textStyle.getSelectionModel().select("Arial");
    }
    //Experiments-----------------
    public void experimentHandler() {
    	printFromFreeHand();
    	System.out.println("#experimentHandler");
    	System.out.println(freeHandList);
    }
    public void drawOnDrag(MouseEvent dod) {    	
    	String type = "";//1-cir//2-tri//3-lin//4-rec//5-ell//6-sq//7-txt
    	if(selectedShape == 1) {type = "Circle";}
    	else if(selectedShape == 2){type = "Triangle";}
    	else if(selectedShape == 3){type = "Line";}
    	else if(selectedShape == 4) {type = "Rectangle";}
    	else if(selectedShape == 5){type = "Ellipse";}
    	else if(selectedShape == 6){type = "Square";}
    	else if(selectedShape == 9){type = "Pentagon";}
    	else if(selectedShape == 10){type = "Hexagon";}
    	else if(selectedShape == 11){type = "Star";}
    	if(type != "") {
			Shape sh;			
			end = new Point2D(dod.getX(), dod.getY());
	        try{
	        	sh = new ShapeFactory().createShape(type,start,end,ColorBox.getValue(),fillColorBox.getValue(),objSize.getValue());
	        }catch(Exception e) {return;}
	    	refresh(CanvasBox);
	        sh.draw(CanvasBox);
    	}
    }
    public void printFromFreeHand() {
    	System.out.println("#printFromFreehand");
    	for(int i=0;i<freeHandList.size();i++) {
    		String X = freeHandList.get(i);
    		System.out.println("X : "+X);
    		//if(!X.equals("/")) {
    		if(true) {
	    		String xval = "";
				String yval = "";
	    		for(int j=0;j<X.length();j++) {
	    			if(X.charAt(j)=='(') {
	    				xval = "";
	    				for(int k=j;X.charAt(k+1)!=',';k++) {
	    					xval+=X.charAt(k+1);
	    				}
	    			}
	    			if(X.charAt(j)==',') {
	    				yval = "";
	    				for(int k=j;X.charAt(k+1)!=')';k++) {
	    					yval+=X.charAt(k+1);
	    				}
	    			}
	    			if(yval !="") {
	    			System.out.println("("+xval+","+yval+")");
	    			
	    			graphicsContext.lineTo(Double.parseDouble(xval), Double.parseDouble(yval));
		    		graphicsContext.stroke();
	    			}
	    		}
	    		graphicsContext.beginPath();
    			graphicsContext.stroke();
    		}
    	}
    }
    //Events handles---------------------------------
    public void ref_btnAction() {
    	System.out.println(freeHandList);
    }
    public void sldrValKey() {
    	Double V=null;
    	try {	
    		V = Double.parseDouble(sldrValue.getText());
    		if(V > 35)
    			sldrValue.setText("35");
        	else if(V<2)
        		sldrValue.setText("2");
        	else
        		objSize.setValue(V);
    	}catch(NumberFormatException e) {}    	
    }
    public void sliderMoved() {
    	Double C = objSize.getValue();
    	sldrValue.setText(C.toString());
    }
    public void keyEventHand(KeyEvent e) {
    	if(e.getCode() == KeyCode.F11) {setFullscrn();e.consume();}    	
    	else if(ctrlp.match(e)) {pdfExtract();e.consume();}
    	else if(ctrlpn.match(e)) {pngExtract();e.consume();}
    	else if(ctrls.match(e)) {save();e.consume();}
    	else if(ctrlQ.match(e)) {exit();e.consume();}
    	else if(ctrll.match(e)) {load();e.consume();}
    	else if(ctrlz.match(e)) {undo();e.consume();}
    	else if(ctrlshiftz.match(e)) {redo();e.consume();}
    	else if(ctrlback.match(e)) {clearCanvas();e.consume();}
    	else if(ctrle.match(e)) {ColorBox.show();e.consume();}
    	else if(ctrlg.match(e)) {textStyle.show();e.consume();}
    	else if(ctrlf.match(e)) {fillColorBox.show();e.consume();}
    	else if(ctrlshiftc.match(e)) {copyImgToClipBoard();e.consume();}
    	else if(alt1.match(e)) {
    		selectedShape = 1;
    		cir.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);    		
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    		e.consume();
    	}else if(alt2.match(e)) {
    		selectedShape = 5;
    		ell.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		cir.setSelected(false);
    		sq.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    		e.consume();    		
    	}else if(alt3.match(e)) {
    		selectedShape = 4;
    		rec.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		cir.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    		e.consume();    		
    	}else if(alt4.match(e)) {
    		selectedShape = 6;
    		sq.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		cir.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    		e.consume();
    	}else if(alt5.match(e)) {
    		selectedShape = 2;
    		tri.setSelected(true);
    		cir.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    		e.consume();
    	}else if(alt6.match(e)) {
    		selectedShape = 3;//1-cir//2-tri//3-lin//4-rec//5-ell//6-sq
    		lin.setSelected(true);
    		tri.setSelected(false);
    		cir.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    		e.consume();    		
    	}else if(alt7.match(e)) {
    		selectedShape = 7;
    		txt.setSelected(true);
    		lin.setSelected(false);
    		tri.setSelected(false);
    		cir.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    		e.consume();    		
    	}else if(alt8.match(e)) {
    		selectedShape = 8;
    		pen.setSelected(true);
    		txt.setSelected(false);
    		lin.setSelected(false);
    		tri.setSelected(false);
    		cir.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    		e.consume();    		
    	}else if(alt9.match(e)) {//pentagon
    		selectedShape = 9;
    		pent.setSelected(true);
    		pen.setSelected(false);
    		txt.setSelected(false);
    		lin.setSelected(false);
    		tri.setSelected(false);
    		cir.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		hex.setSelected(false);
    		star.setSelected(false);
    		e.consume();
    	}else if(alt10.match(e)) {//hexagon
    		selectedShape = 10;
    		hex.setSelected(true);
    		pent.setSelected(false);
    		pen.setSelected(false);
    		txt.setSelected(false);
    		lin.setSelected(false);
    		tri.setSelected(false);
    		cir.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		star.setSelected(false);
    		e.consume();    		
    	}
    }
    public void toggleManager(MouseEvent event) {
    	if(event.getSource() == cir) {
    		selectedShape = 1;
    		cir.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    	}else if(event.getSource() == tri) {    		
    		selectedShape = 2;
    		tri.setSelected(true);
    		cir.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    	} else if(event.getSource() == lin) {
    		selectedShape = 3;
    		lin.setSelected(true);
    		tri.setSelected(false);
    		cir.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    	} else if(event.getSource() == rec) {
    		selectedShape = 4;
    		rec.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		cir.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    	} else if(event.getSource() == 	ell) {
    		selectedShape = 5;
    		ell.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		cir.setSelected(false);
    		sq.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    	} else if(event.getSource() == sq){
    		selectedShape = 6;
    		sq.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		cir.setSelected(false);
    		txt.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    	} else if(event.getSource() == txt){
    		selectedShape = 7;
    		txt.setSelected(true);
    		sq.setSelected(false);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		cir.setSelected(false);
    		pen.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    	} else if(event.getSource() == pen){
    		selectedShape = 8;
    		pen.setSelected(true);
    		sq.setSelected(false);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		cir.setSelected(false);
    		txt.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    		star.setSelected(false);
    	} else if(event.getSource() == pent){
    		selectedShape = 9;
    		pen.setSelected(false);
    		sq.setSelected(false);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		cir.setSelected(false);
    		txt.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(true);
    		star.setSelected(false);
    	} else if(event.getSource() == hex){
    		selectedShape = 10;
    		pen.setSelected(false);
    		sq.setSelected(false);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		cir.setSelected(false);
    		txt.setSelected(false);
    		hex.setSelected(true);
    		pent.setSelected(false);
    		star.setSelected(false);
    	}else if(event.getSource() == star){
    		selectedShape = 11;
    		star.setSelected(true);
    		pen.setSelected(false);
    		sq.setSelected(false);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		cir.setSelected(false);
    		txt.setSelected(false);
    		hex.setSelected(false);
    		pent.setSelected(false);
    	}
    }
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Message.setText("");
        if(event.getSource()==DeleteBtn){
            if(!ShapeList.getSelectionModel().isEmpty()){
            	int index = ShapeList.getSelectionModel().getSelectedIndex();
            	removeShape(shapeList.get(index));
            }else{Message.setText("You need to pick a shape first to delete it.");}
        }        
        if(event.getSource()==RecolorBtn){
            if(!ShapeList.getSelectionModel().isEmpty()){
                int index = ShapeList.getSelectionModel().getSelectedIndex();
                shapeList.get(index).setFillColor(ColorBox.getValue());
                refresh(CanvasBox);
            }else{Message.setText("You need to pick a shape first to recolor it.");}
        }        
        if(event.getSource()==MoveBtn){
            if(!ShapeList.getSelectionModel().isEmpty()){
                move=true;
                Message.setText("Click on the new top-left position below to move the selected shape.");
            }else{Message.setText("You need to pick a shape first to move it.");}
        }        
        if(event.getSource()==CopyBtn){
            if(!ShapeList.getSelectionModel().isEmpty()){
                copy=true;
                Message.setText("Click on the new top-left position below to copy the selected shape.");
            }else{Message.setText("You need to pick a shape first to copy it.");}
        }        
        if(event.getSource()==ResizeBtn){
            if(!ShapeList.getSelectionModel().isEmpty()){
                resize=true;
                Message.setText("Click on the new right-button position below to resize the selected shape.");
            }else{Message.setText("Select the shape from List view first to resize it.");}
        }       
        if(event.getSource()==UndoBtn){if(primary.empty()){jbp();return;}undo();}
        if(event.getSource()==RedoBtn){if(secondary.empty()){jbp();return;}redo();}
        if(event.getSource()==SaveBtn){save();}
        if(event.getSource()==LoadBtn){load();}
        if(event.getSource()==cpClpBrdBtn){copyImgToClipBoard();}
        if(event.getSource()==ImportBtn){installPluginShape(null);}                
    }           
    public void startDrag(MouseEvent event){    	
        start = new Point2D(event.getX(),event.getY());
        Message.setText("");        	
    }
    public void endDrag(MouseEvent event) throws CloneNotSupportedException{
        end = new Point2D(event.getX(), event.getY());
        if(end.equals(start)){clickFunction();}else{dragFunction();}
    }    
    public void clickFunction() throws CloneNotSupportedException{
        if(move){move=false;moveFunction();}
        else if(copy){copy=false;copyFunction();}
        else if(resize){resize=false;resizeFunction();}        
    }
    public void canvasClicker(MouseEvent cc) {
    	if(selectedShape == 7) {        	
    		tevo.setVisible(true);
    		tevo.setDisable(false);
    		tevo.requestFocus();
    		tevo.setOnKeyReleased(new EventHandler<KeyEvent>() {
		        @Override public void handle(KeyEvent keye) {		        	
		        	if(altEnter.match(keye)) {		        		
		        		tevo.setVisible(false);
		        		tevo.setDisable(true);
		        		ColorBox.requestFocus();
		        		Shape sh;
		                try{		                	
		                	sh = new ShapeFactory().createShape("Text",cc.getX(),cc.getY(),tovoVal,ColorBox.getValue(),objSize.getValue(),textStyle.getValue());
		                	tevo.setText("");
		                }catch(Exception e) {return;}
		                addShape(sh);
		                sh.draw(CanvasBox);
		        	}
		        	tovoVal = tevo.getText();
		        }		        
		    });    		
    	}    	
    }
    public void moveFunction(){
        int index = ShapeList.getSelectionModel().getSelectedIndex();
        shapeList.get(index).setTopLeft(start);
        refresh(CanvasBox);
    }    
    public void copyFunction() throws CloneNotSupportedException{
        int index = ShapeList.getSelectionModel().getSelectedIndex();
        Shape temp = shapeList.get(index).cloneShape();
        if(temp.equals(null)){System.out.println("Error cloning failed!");}
        else{
            shapeList.add(temp);
            shapeList.get(shapeList.size()-1).setTopLeft(start);
            refresh(CanvasBox);
        }
    }    
    public void resizeFunction(){
        int index = ShapeList.getSelectionModel().getSelectedIndex();
        Color c = shapeList.get(index).getFillColor();
        start = shapeList.get(index).getTopLeft();

        Shape temp = new ShapeFactory().createShape(shapeList.get(index).getClass().getSimpleName(),start,end,ColorBox.getValue(),fillColorBox.getValue(),objSize.getValue());
        if(temp.getClass().getSimpleName().equals("Line")){Message.setText("Line doesn't support this command. Sorry :(");return;}
        shapeList.remove(index);
        temp.setFillColor(c);
        shapeList.add(index, temp);
        refresh(CanvasBox);
    }    
    public void dragFunction(){
    	String type = "";//1-cir//2-tri//3-lin//4-rec//5-ell//6-sq//7-txt
    	if(selectedShape == 1) {type = "Circle";}
    	else if(selectedShape == 2) {type = "Triangle";}
    	else if(selectedShape == 3) {type = "Line";}
    	else if(selectedShape == 4) {type = "Rectangle";}
    	else if(selectedShape == 5) {type = "Ellipse";}
    	else if(selectedShape == 6){type = "Square";}
    	else if(selectedShape == 9){type = "Pentagon";}
    	else if(selectedShape == 10){type = "Hexagon";}
    	else if(selectedShape == 11){type = "Star";}
    	if(type != "") {
			Shape sh;        
	        try{
	        	sh = new ShapeFactory().createShape(type,start,end,ColorBox.getValue(),fillColorBox.getValue(),objSize.getValue());
	        }catch(Exception e) {return;}
	        addShape(sh);
	        sh.draw(CanvasBox);
    	}
    }
    //Clear, configuration & remove functions	-----------------------------------
    private void cursorMoni() {	    
	    CanvasBox.setOnMouseMoved(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {	        	
	            cords.setText(String.format("(%.2f, %.2f)",mouseEvent.getX(),mouseEvent.getY()));
	        }
	    });
	    CanvasBox.setOnMouseExited(new EventHandler<MouseEvent>() {
	         public void handle(MouseEvent mouseEvent) {
	        	 cords.setText("");    	  
	         }
	    });
	}
    public void setFullscrn() {
		System.out.println("#FullScreen");
		Stage stage = (Stage) CanvasBox.getScene().getWindow(); 
		stage.setFullScreenExitHint("Press F11 to exit full-screen mode");
		if(!isFullScr) {
			stage.setFullScreen(true);
			isFullScr=true;
		}else {
			stage.setFullScreen(false);
			isFullScr=false;
		}		
	}
    public void clearCanvas() {
    	GraphicsContext gc = CanvasBox.getGraphicsContext2D();    	
    	gc.clearRect(0, 0, CanvasBox.getWidth(), CanvasBox.getHeight());
    	shapeList.clear();
    	ShapeList.getItems().clear();
    }
    public void exit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");		
		alert.setContentText("Close without saving or Exporting?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			System.exit(0);
		}		
	}
    //Undo - redo and else imp functionalities-------------------------------------
    public ObservableList<String> getStringList(){
        ObservableList<String> l = FXCollections.observableArrayList(new ArrayList<String>());
        try{
        for(int i=0;i<shapeList.size();i++){
            String temp = shapeList.get(i).getClass().getSimpleName() + "  (" 
            		+ (int) shapeList.get(i).getTopLeft().getX() + "," 
            		+ (int) shapeList.get(i).getTopLeft().getY() + ")"+"["
            		+ shapeList.get(i).getStrokeSize() + "]";
            l.add(temp);
        }
        }catch(Exception e){}
        return l;
    }    
    public ArrayList<Shape> cloneList(ArrayList<Shape> l) throws CloneNotSupportedException{
        ArrayList<Shape> temp = new ArrayList<Shape>();
        for(int i=0;i<l.size();i++){
            temp.add(l.get(i).cloneShape());
        }
        return temp;
    }       
    @Override
    public void refresh(Object canvas) {
        try {
            primary.push(new ArrayList<Shape>(cloneList(shapeList)));
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FxPaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
        redraw((Canvas) canvas);
       ShapeList.setItems((getStringList()));
    }   
    public void redraw(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0,CanvasBox.getWidth(), CanvasBox.getHeight());
        try{
        for(int i=0;i<shapeList.size();i++){
            shapeList.get(i).draw(canvas);
        }
        }catch(Exception e){}
    }
    @Override
    public void addShape(Shape shape) {
        shapeList.add(shape);
        refresh(CanvasBox);
    }
    @Override
    public void removeShape(Shape shape) {
        shapeList.remove(shape);
        refresh(CanvasBox);
    }
    @Override
    public void updateShape(Shape oldShape, Shape newShape) {
        shapeList.remove(oldShape);
        shapeList.add(newShape);
        refresh(CanvasBox);
    }
    @Override
    public Shape[] getShapes() {
     return (Shape[]) shapeList.toArray();
    }
    @Override
    public void undo() {
    	
        if(secondary.size()<21){
	        ArrayList<Shape> temp = (ArrayList<Shape>) primary.pop();
	        secondary.push(temp);
	        
	        if(primary.empty()){shapeList = new ArrayList<Shape>();}
	        else{temp = (ArrayList<Shape>) primary.peek(); shapeList = temp;}
	        
	        redraw(CanvasBox);
	        ShapeList.setItems((getStringList()));
        }else{Message.setText("Sorry, Cannot do more than 20 Undo's :'(");}
    }
    @Override
    public void redo() {
        ArrayList<Shape> temp = (ArrayList<Shape>) secondary.pop();
        primary.push(temp);
        
        temp = (ArrayList<Shape>) primary.peek();
        shapeList = temp;
        
        redraw(CanvasBox);
        ShapeList.setItems((getStringList()));
    }
    @Override
    public void save() {
    	FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showSaveDialog(CanvasBox.getScene().getWindow());
        if (file != null) {
            SaveToXML x = new SaveToXML(file.getAbsolutePath()+".xml",shapeList);
            if(x.checkSuccess()){Message.setText("File Saved Successfully");}
            else{Message.setText("Error occured while saving");}
        }
    }
    @Override
    public void load() {
    	FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(CanvasBox.getScene().getWindow());	    
        if (file != null) {
        	System.out.println(file.getAbsolutePath());
            try {
                LoadFromXML l = new LoadFromXML(file.getAbsolutePath());
                if(l.checkSuccess()){
                shapeList = l.getList();
                refresh(CanvasBox);
                Message.setText("File loaded successfully");
                }
                else{Message.setText("Error loading the file .. check the file path and try again!");}
            } catch (SAXException ex) {
                Logger.getLogger(FxPaintController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(FxPaintController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FxPaintController.class.getName()).log(Level.SEVERE, null, ex);
            }           
        }        
    }
    private void jbp() {Toolkit.getDefaultToolkit().beep();}
    @Override
    public List<Class<? extends Shape>> getSupportedShapes() {
        return null;
    }
    @Override
    public void installPluginShape(String jarPath) {Message.setText("Not supported yet.");}
    //Export------------------------------------------------
    public void copyImgToClipBoard() {
    	WritableImage nodeshot = CanvasBox.snapshot(new SnapshotParameters(), null);
    	
    	Clipboard clipboard = Clipboard.getSystemClipboard();	 
		ClipboardContent content = new ClipboardContent();
		content.putImage(nodeshot);
		clipboard.setContent(content);
    	
    	final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label mess = new Label("Image Copied to Clipboard!");
        popup.getContent().add(mess);
        popup.show(anchor.getScene().getWindow());
    }
    public void pngExtract() {
		  FileChooser fileChooser = new FileChooser();
	      fileChooser.setTitle("Save");
	      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"));
	      File file = fileChooser.showSaveDialog(CanvasBox.getScene().getWindow());
	      
        if (file != null) {		    
			WritableImage nodeshot = CanvasBox.snapshot(new SnapshotParameters(), null);			
			file = new File(file.getAbsolutePath()+".png");
			try {				
				ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
				Message.setText("PNG Image exported");
			} catch (IOException e) {
				Message.setText("Error in making image!");
			}
		}
    }
	public void pdfExtract() {
		//openPdfTextDialog();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"));
		File file = fileChooser.showSaveDialog(CanvasBox.getScene().getWindow());
	      
        if (file != null) {
			WritableImage nodeshot = CanvasBox.snapshot(new SnapshotParameters(), null);
	        File imgfile = new File("dat/imgs/bchart.png");		        
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", imgfile);
			} catch (IOException e) {
				System.out.println("Error in making image!");
			}			
			try {
			    String k = "NULL";				    
			    OutputStream popfile = new FileOutputStream(new File(file.getAbsolutePath()+".pdf"));
			    Document document = new Document();
			    PdfWriter.getInstance(document, popfile);
			    document.open();
			    Image img = Image.getInstance("dat/imgs/bchart.png");			    
			    img.scaleAbsolute(560, 300);
		        document.add(img);
				HTMLWorker htmlWorker = new HTMLWorker(document);				
			    htmlWorker.parse(new StringReader(k));
			    document.close();
			    popfile.close();
			    System.out.println("DOC\n\tPdf Exported!");
			    Message.setText("PDF exported!");
			} catch (Exception e) {
			    e.printStackTrace();
			} finally {
				imgfile.delete();
			}
		}
    }
    //New charts opening-----------------------------------------------------------
  	public void openPieKit() {	
  		FxChartMainPage openNew = new FxChartMainPage();
  		try {
  			Stage thisstage = (Stage) CanvasBox.getScene().getWindow();
  			openNew.setStage(thisstage,"Pie chart");
  			System.out.println("Opening Pie Chart closed bar chart");
  		} catch (IOException e) {e.printStackTrace();}
  	}
  	public void openBarKit() {
  		FxChartMainPage openNew = new FxChartMainPage();
  		try {
  			Stage thisstage = (Stage) CanvasBox.getScene().getWindow();
  			openNew.setStage(thisstage,"Bar chart");
  			System.out.println("Opening bar Chart closed bar chart");
  		} catch (IOException e) {e.printStackTrace();}
  	}
  	public void openLineKit() {
  		FxChartMainPage openNew = new FxChartMainPage();
  		try {
  			Stage thisstage = (Stage) CanvasBox.getScene().getWindow();
  			openNew.setStage(thisstage,"Line chart");
  			System.out.println("Opening Line Chart closed bar chart");
  		} catch (IOException e) {e.printStackTrace();}
  	}
  	public void openStackKit() {
  		FxChartMainPage openNew = new FxChartMainPage();
  		try {
  			Stage thisstage = (Stage) CanvasBox.getScene().getWindow();
  			openNew.setStage(thisstage,"Stacked chart");
  			System.out.println("Opening stack Chart closed bar chart");
  		} catch (IOException e) {e.printStackTrace();}
  	}
  	public void openFxPaintKit() {
  		FxChartMainPage openNew = new FxChartMainPage();
  		try {
  			Stage thisstage = (Stage) CanvasBox.getScene().getWindow();
  			openNew.setStage(thisstage,"Paint");
  			System.out.println("Opening Paint closed bar chart");
  		} catch (IOException e) {e.printStackTrace();}
  	}
}
