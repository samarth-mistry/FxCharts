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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

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
	@FXML private AnchorPane anchor;
	@FXML private MenuBar menuBar;
	@FXML private ColorPicker ColorBox;
	@FXML private Label Message;
	@FXML private Canvas CanvasBox;
	@FXML private GridPane After;
    @FXML private Pane Before;
    @FXML private Pane PathPane;
    @FXML private Button DeleteBtn;       
    @FXML private Button UndoBtn;
    @FXML private Button RedoBtn;    
    @FXML private Button SaveBtn;    
    @FXML private Button MoveBtn;    
    @FXML private Button RecolorBtn;    
    @FXML private Button LoadBtn;        
    @FXML private Button StartBtn;    
    @FXML private Button ResizeBtn;    
    @FXML private Button ImportBtn;    
    @FXML private Button PathBtn;           
    @FXML private Button CopyBtn;        
    @FXML private ToggleButton themeToggle;
    @FXML private ToggleButton cir;
    @FXML private ToggleButton lin;
    @FXML private ToggleButton tri;
    @FXML private ToggleButton rec;
    @FXML private ToggleButton sq;
    @FXML private ToggleButton ell;
    @FXML private ListView<String> ShapeList;    
    private Point2D start;
    private Point2D end;
    final KeyCombination alt1= new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.ALT_DOWN);
    final KeyCombination alt2= new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.ALT_DOWN);
    final KeyCombination alt3= new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.ALT_DOWN);
    final KeyCombination alt4= new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.ALT_DOWN);
    final KeyCombination alt5= new KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.ALT_DOWN);
    final KeyCombination alt6= new KeyCodeCombination(KeyCode.DIGIT6, KeyCombination.ALT_DOWN);
    final KeyCombination altEnter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
    final KeyCombination ctrll= new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrle= new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlz= new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlshiftz= new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
    final KeyCombination ctrlc= new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlx= new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlp = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlpn = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);    
    final KeyCombination ctrlQ = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrls= new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrli= new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrla= new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlback= new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.CONTROL_DOWN);
    public void exit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");		
		alert.setContentText("Close without saving or Exporting?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			System.exit(0);
		}		
	}
    //SINGLETON DP
    private static ArrayList<Shape> shapeList = new ArrayList<Shape>();
    private int selectedShape = 1;	//1-cir//2-tri//3-lin//4-rec//5-ell//6-sq
    private boolean move=false;
    private boolean copy=false;
    private boolean resize=false;
    private boolean isFullScr=false;
    private Stack<ArrayList<Shape>> primary = new Stack<ArrayList<Shape>>();
    private Stack<ArrayList<Shape>> secondary = new Stack<ArrayList<Shape>>();
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
    public void initialize(URL url, ResourceBundle rb) {}
    public void keyEventHand(KeyEvent e) {
    	if(e.equals(KeyCode.F11)) {setFullscrn();e.consume();}    	
    	else if(ctrlp.match(e)) {pdfExtract();e.consume();}
    	else if(ctrlpn.match(e)) {pngExtract();e.consume();}
    	else if(ctrls.match(e)) {save();e.consume();}
    	else if(ctrll.match(e)) {load();e.consume();}
    	else if(ctrlz.match(e)) {undo();e.consume();}
    	else if(ctrlshiftz.match(e)) {redo();e.consume();}
    	else if(ctrlback.match(e)) {clearCanvas();e.consume();}
    	else if(ctrle.match(e)) {ColorBox.show();e.consume();}
    	else if(alt1.match(e)) {
    		selectedShape = 1;
    		cir.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		e.consume();
    	}
    	else if(alt2.match(e)) {
    		selectedShape = 5;
    		ell.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		cir.setSelected(false);
    		sq.setSelected(false);
    		e.consume();    		
    	}
    	else if(alt3.match(e)) {
    		selectedShape = 4;
    		rec.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		cir.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		e.consume();    		
    	}
    	else if(alt4.match(e)) {
    		selectedShape = 6;
    		sq.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		cir.setSelected(false);
    		e.consume();
    	}
    	else if(alt5.match(e)) {
    		selectedShape = 2;
    		tri.setSelected(true);
    		cir.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    		e.consume();
    	}
    	else if(alt6.match(e)) {
    		selectedShape = 3;
    		lin.setSelected(true);
    		tri.setSelected(false);
    		cir.setSelected(false);//1-cir//2-tri//3-lin//4-rec//5-ell//6-sq
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
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
    	}else if(event.getSource() == tri) {    		
    		selectedShape = 2;
    		tri.setSelected(true);
    		cir.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);
    	} else if(event.getSource() == lin) {
    		selectedShape = 3;
    		lin.setSelected(true);
    		tri.setSelected(false);
    		cir.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);    		
    	} else if(event.getSource() == rec) {
    		selectedShape = 4;
    		rec.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		cir.setSelected(false);
    		ell.setSelected(false);
    		sq.setSelected(false);    		
    	} else if(event.getSource() == 	ell) {
    		selectedShape = 5;
    		ell.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		cir.setSelected(false);
    		sq.setSelected(false);    		
    	} else {
    		selectedShape = 6;
    		sq.setSelected(true);
    		tri.setSelected(false);
    		lin.setSelected(false);
    		rec.setSelected(false);
    		ell.setSelected(false);
    		cir.setSelected(false); 
    	}    	
    }
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Message.setText("");
        if(event.getSource()==DeleteBtn){
            if(!ShapeList.getSelectionModel().isEmpty()){
            	int index = ShapeList.getSelectionModel().getSelectedIndex();
            	removeShape(shapeList.get(index));
            }else{
                Message.setText("You need to pick a shape first to delete it.");
            }
        }        
        if(event.getSource()==RecolorBtn){
            if(!ShapeList.getSelectionModel().isEmpty()){
                int index = ShapeList.getSelectionModel().getSelectedIndex();
                shapeList.get(index).setFillColor(ColorBox.getValue());
                refresh(CanvasBox);
            }else{
                Message.setText("You need to pick a shape first to recolor it.");
            }
        }        
        if(event.getSource()==MoveBtn){
            if(!ShapeList.getSelectionModel().isEmpty()){
                move=true;
                Message.setText("Click on the new top-left position below to move the selected shape.");
            }else{
                Message.setText("You need to pick a shape first to move it.");
            }
        }        
        if(event.getSource()==CopyBtn){
            if(!ShapeList.getSelectionModel().isEmpty()){
                copy=true;
                Message.setText("Click on the new top-left position below to copy the selected shape.");
            }else{
                Message.setText("You need to pick a shape first to copy it.");
            }
        }        
        if(event.getSource()==ResizeBtn){
            if(!ShapeList.getSelectionModel().isEmpty()){
                resize=true;
                Message.setText("Click on the new right-button position below to resize the selected shape.");
            }else{Message.setText("Select the shape from List view first to copy it.");}
        }       
        if(event.getSource()==UndoBtn){if(primary.empty()){jbp();return;}undo();}
        if(event.getSource()==RedoBtn){if(secondary.empty()){jbp();return;}redo();}
        if(event.getSource()==SaveBtn){save();}
        if(event.getSource()==LoadBtn){load();}        
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
        //Factory DP
        Shape temp = new ShapeFactory().createShape(shapeList.get(index).getClass().getSimpleName(),start,end,ColorBox.getValue());
        if(temp.getClass().getSimpleName().equals("Line")){Message.setText("Line doesn't support this command. Sorry :(");return;}
        shapeList.remove(index);
        temp.setFillColor(c);
        shapeList.add(index, temp);
        refresh(CanvasBox);
        
    }    
    public void dragFunction(){
    	String type = "";//1-cir//2-tri//3-lin//4-rec//5-ell//6-sq
    	if(selectedShape == 1) {
    		type = "Circle";
    	}else if(selectedShape == 2) {
    		type = "Triangle";
    	}else if(selectedShape == 3) {
    		type = "Line";
    	}else if(selectedShape == 4) {
    		type = "Rectangle";
    	}else if(selectedShape == 5) {
    		type = "Ellipse";
    	}else {
    		type = "Square";
    	}
        Shape sh;
        //Factory DP
        try{
        	sh = new ShapeFactory().createShape(type,start,end,ColorBox.getValue());
        }catch(Exception e){Message.setText("Don't be in a hurry! Choose a shape first :'D");return;}
        addShape(sh);
        sh.draw(CanvasBox);
        
    }
    //Observer DP
    public ObservableList<String> getStringList(){
        ObservableList<String> l = FXCollections.observableArrayList(new ArrayList<String>());
        try{
        for(int i=0;i<shapeList.size();i++){
            String temp = shapeList.get(i).getClass().getSimpleName() + "  (" + (int) shapeList.get(i).getTopLeft().getX() + "," + (int) shapeList.get(i).getTopLeft().getY() + ")";
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
    public void clearCanvas() {
    	GraphicsContext gc = CanvasBox.getGraphicsContext2D();    	
    	gc.clearRect(0, 0, CanvasBox.getWidth(), CanvasBox.getHeight());
    	shapeList.clear();
    }
    public void redraw(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 850, 370);
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