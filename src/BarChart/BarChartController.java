package BarChart;

import FileSys.barFileSysController;
import FileSys.lineFileSysController;
import LineChart.LineTableController;
import Main.FxChartMainPage;
import XMLController.BarChartXml.BXFileManager;
import XMLController.LineChartXml.LXFileManager;
import DbSys.DbController;

import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;

import javax.imageio.ImageIO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.DoubleStringConverter;

@SuppressWarnings("deprecation")
public class BarChartController {
	@FXML private MenuBar menuBar;
	@FXML private BarChart<String, Number> bChart;
	@FXML private Label error_label;
	@FXML private Label app_log;
	@FXML private Label barValue;
	@FXML private TextArea bulkNms,bulk;
	@FXML private TextField nm,val,barChartTitle,seriesLabel,xxisLabel,yxisLabel;
	@FXML private CategoryAxis xxis;
	@FXML private NumberAxis yxis;
	@FXML private TableView<BarTableController> table;
	@FXML private TableColumn<BarTableController, String> c1,c2;
	@FXML private TableColumn<BarTableController, Double> c3;
	@FXML private CheckBox bulkEnable;
	@FXML private Button add_b_data,add_b_nms,add_data;
	@FXML private AnchorPane anchor;	
	@FXML private Circle theameCircle;
	@FXML private ToggleButton themeToggle;
	static String pdfTextApp = null;
	//private DateTimeFormatter classic=DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private boolean theame=true,isFullScr=false;
	final Stage primaryStage = null;
	final double SCALE_DELTA = 1.1;
	private static String FILE_PATH = "";
	private ArrayList<String> bulkNames = new ArrayList<String>();
	private ArrayList<XYChart.Series<String, Number>> SeriList = new ArrayList<XYChart.Series<String, Number>>();
	private Stack<ArrayList<XYChart.Series<String, Number>>> priList = new Stack<ArrayList<XYChart.Series<String, Number>>>(), secList = new Stack<ArrayList<XYChart.Series<String, Number>>>();
	
    final KeyCombination altEnter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN),
    altj= new KeyCodeCombination(KeyCode.J, KeyCombination.ALT_DOWN),
    altk= new KeyCodeCombination(KeyCode.K, KeyCombination.ALT_DOWN),
    altl= new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN),
    ctrlb= new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN),
    ctrlf= new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN),
    ctrld= new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN),
	ctrls= new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
    ctrlo= new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN),
    ctrlz= new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN),
    ctrlshiftz= new KeyCodeCombination(KeyCode.Z, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN),
    ctrlshiftc= new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
    ctrlshifts= new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
    ctrlPrintPDF = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN),
    ctrlPrintPNG = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN),
    ctrlPrintTPDF = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN),
    ctrlQ = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
    altT= new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN);
    
    @FXML void bulkNamesPressed(KeyEvent event) {
    	if (altEnter.match(event)){setBulkNames();event.consume();}
    	else {btnOnKeyPressed(event);event.consume();}
    }
    @FXML void bulkEntriesPressed(KeyEvent event) {
    	if (altEnter.match(event)) {addBulkData();event.consume();}
    	else {btnOnKeyPressed(event);event.consume();}
    }
    @FXML void nmValEntriesPressed(KeyEvent event) {
    	if (altEnter.match(event)) {addData();event.consume();}
    	else {btnOnKeyPressed(event);event.consume();}
    }
	@FXML void btnOnKeyPressed(KeyEvent event) {									
		if (altT.match(event)) {changeTheme();event.consume();}		
		else if (altj.match(event)) {clearChart();event.consume();}
		else if (altk.match(event)) {clearFile();event.consume();}
		else if (altl.match(event)) {clearDb();event.consume();}
		else if (ctrls.match(event)) {save();}
		else if (ctrlo.match(event)) {load();}
		else if (ctrlz.match(event)) {undo();event.consume();}
		else if (ctrlshiftz.match(event)) {redo();event.consume();}
		else if (ctrlshiftc.match(event)) {copyImgToClipBoard();event.consume();}
		else if (ctrlshifts.match(event)) {saveas();event.consume();}
		else if (ctrlb.match(event)) {
			if(bulkEnable.isSelected()) {
				bulkEnable.setSelected(false);
				bulkNms.requestFocus();
			}
			else
				bulkEnable.setSelected(true);
			event.consume();
			buttonEnabler();
		}
		else if (ctrlPrintPDF.match(event)) {pdfExtract();}
		else if (ctrlPrintPNG.match(event)) {pngExtract();}
		else if (ctrlQ.match(event)) {exit();}
		else if (ctrlPrintTPDF.match(event)) {        	
        	try {
				pdfTableExtract();
			} catch (FileNotFoundException e) {
				error_label.setText("Error Occured!");
				e.printStackTrace();
			} catch (DocumentException e) {
				error_label.setText("Error Occured!");
				e.printStackTrace();
			}
        }
		else
			event.consume();
	}
	@FXML public void initialize() {
		//AudioPlayer.ALERT_AUDIOCLIP.play();
		bChart.setAnimated(false);
		Toolkit.getDefaultToolkit().beep();		
		table.setEditable(true);		
		c1.setCellValueFactory(new PropertyValueFactory<>("series"));			
		c2.setCellValueFactory(new PropertyValueFactory<>("seriesX"));
		c3.setCellValueFactory(new PropertyValueFactory<>("seriesY"));
		c1.setSortable(false);
		c2.setSortable(false);
		c3.setSortable(false);
		table.getSelectionModel().cellSelectionEnabledProperty().set(true);
		c1.setCellFactory(TextFieldTableCell.forTableColumn());
		c2.setCellFactory(TextFieldTableCell.forTableColumn());
		c3.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	    c1.setOnEditCommit(new EventHandler<CellEditEvent<BarTableController, String>>() {
	         public void handle(CellEditEvent<BarTableController, String> t) {
	               ((BarTableController)t.getTableView().getItems().get(t.getTablePosition().getRow())).setSeries(t.getNewValue().toString());
	               editFromTable();
	         }
	    });
	    c2.setOnEditCommit(new EventHandler<CellEditEvent<BarTableController, String>>() {
	         public void handle(CellEditEvent<BarTableController, String> t) {
	               ((BarTableController)t.getTableView().getItems().get(t.getTablePosition().getRow())).setSeriesX(t.getNewValue().toString());
	               editFromTable();
	         }
	    });
	    c3.setOnEditCommit(new EventHandler<CellEditEvent<BarTableController, Double>>() {
	         public void handle(CellEditEvent<BarTableController, Double> t) {
	               ((BarTableController)t.getTableView().getItems().get(t.getTablePosition().getRow())).setSeriesY(t.getNewValue());
	               editFromTable();
	         }
	    });
	}
	//Zooming-------------------------------------------------
	public void zoomLineChart(ScrollEvent event) {
		 double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
        bChart.setScaleX(bChart.getScaleX() * scaleFactor);
        bChart.setScaleY(bChart.getScaleY() * scaleFactor);
	}
	public void zoomNormal(MouseEvent e) {				    
	    if (e.getClickCount() == 2) {
	        bChart.setScaleX(1.0);
	        bChart.setScaleY(1.0);
	    }		    
	}
	public void barValuePlotter() {		
		for (final Series<String, Number> series : bChart.getData()) {			
	        for (final XYChart.Data<String, Number> data : series.getData()) {
	        	data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
                    e -> {                	                                            
                        barValue.setText(data.getXValue().toString()+" : "+data.getYValue().toString());                        
                     }
                );
                data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,
            		e->{            			
            			barValue.setText("");
            		}
                );	            
	        }
	    }    							
	}	
	//Clear Functions-----------------------------------------------------------
	public void clearChart() {
		bChart.getData().clear();		
		//error_label.setText("Chart Data Cleared! Click load data to reload");				
	}
	public void clearFile() {		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");
		alert.setHeaderText("Clearing File will permanently delete your data!");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			barFileSysController.clearFile("barFileData.txt");
			error_label.setText("File is Cleared. Data is permanently lost");
		}		
	}
	public void clearTable() {
		//error_label.setText("Table it cleared! To reload click on load button");
		table.getItems().clear();
	}
	public void clearDb() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");
		alert.setHeaderText("Clearing DB will permanently delete your data!");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			DbController.clearSeries();
			//error_label.setText("DB is Cleared. Data is permanently lost");
		}		
	}
	public void clearAll() {
		clearChart();
		clearTable();
		clearFile();
	}
	public void exit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");
		alert.setHeaderText("Your data is been saved!");
		alert.setContentText("Are you sure you want to exit?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			System.exit(0);
		}		
	}
	//Config functions Functions-----------------------------------------------------------
	public void setFullscrn() {
		System.out.println("#FullScreen");
		Stage stage = (Stage) anchor.getScene().getWindow();
		stage.setFullScreenExitHint("Press Alt + F11 to exit full-screen mode");
		if(!isFullScr) {
			stage.setFullScreen(true);
			isFullScr=true;
		}else {
			stage.setFullScreen(false);
			isFullScr=false;
		}		
	}
	public void checkAnimated() {
		if(bChart.getAnimated())
			bChart.setAnimated(false);
		else
			bChart.setAnimated(true);
	}
	public void axisRenamed() {
		bChart.setTitle(barChartTitle.getText());				
		xxis.setLabel(xxisLabel.getText());
		yxis.setLabel(yxisLabel.getText());		
		c2.setText(xxisLabel.getText());
		c3.setText(yxisLabel.getText());
		if(barChartTitle.getText() == "")
			bChart.setTitle("Line Chart");		
		if(xxisLabel.getText() == "")
			xxis.setLabel("X->");
		if(yxisLabel.getText() == "")
			yxis.setLabel("Y->");		
	}
	public void buttonEnabler() {
		if(bulkEnable.isSelected()){
			nm.setDisable(true);
			val.setDisable(true);
			add_data.setDisable(true);			
			bulkNms.setDisable(false);
			add_b_nms.setDisable(false);
		}else {
			bulk.setDisable(true);
			bulkNms.setDisable(true);
			add_b_nms.setDisable(true);
			add_b_data.setDisable(true);
			nm.setDisable(false);
			val.setDisable(false);
			add_data.setDisable(false);
		}
	}
	public void changeTheme() {
		if(theame) {	//light
			Color c = Color.web("#d8d8d8");			//white color	
			error_label.setStyle("-fx-border-color: white");
			anchor.setStyle("-fx-background-color:  #666666");
			bChart.setStyle("-fx-border-color: #d8d8d8");
		    bChart.setStyle("-fx-text-fill: black");
			barValue.setStyle("-fx-border-color: #d8d8d8");
			barValue.setTextFill(c);
			xxis.setTickLabelFill(c);yxis.setTickLabelFill(c);			
			xxis.setStyle("-fx-text-fill: black");
			yxis.setStyle("-fx-text-fill: black");			
			bulkEnable.setTextFill(c);
			theameCircle.setFill(c);
			theame = false;
		}
		else {		//dark
			Color c = Color.web("#666666");			
			error_label.setStyle("-fx-border-color: black");
			anchor.setStyle("-fx-background-color:  #d8d8d8");
			bChart.setStyle("-fx-border-color: #666666");
			bChart.setStyle("-fx-text-fill: white");
			barValue.setStyle("-fx-border-color: #666666");
			barValue.setTextFill(c);
			xxis.setTickLabelFill(c);
			yxis.setTickLabelFill(c);
			xxis.setStyle("-fx-text-fill: white");
			yxis.setStyle("-fx-text-fill: white");
			bulkEnable.setTextFill(c);
			theameCircle.setFill(c);
			theame = true;
		}
	}
	//Add Functions-----------------------------------------------------------
	public void addData() {		
		System.out.println("#AddSinBarData#");
		String X = val.getText();
		if(nm.getText().isEmpty() || val.getText().isEmpty())
			error_label.setText("Enter both name and value before adding");
		else if(seriesLabel == null)
			error_label.setText("Please enter the name of Bar");
		else {
			if(dblChecker(X)) {
				drawSinChart(true);				
			}else
				error_label.setText("Value of val must be number");
		}
	}
	public void addBulkData() {
		if(bulkNms.getText().isEmpty())
			error_label.setText("Enter Pattern before adding");
		else if(seriesLabel == null) {
			error_label.setText("Please enter the name of Bar");
		}
		else if(!inputValidator("{"+seriesLabel.getText()+"}"+bulk.getText())) {
			error_label.setText("Entered pattern is not valid. Please try again!");
		} else {
			decodeAndDraw();			
		}
	}	
	//Draw & decoding Validations Functions-----------------------------------------------------------
	private void drawSinChart(Boolean whoCalled) {
		System.out.println("#drawSinChart");
		try {								
			XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
			series.getData().add(new XYChart.Data<String, Number>(nm.getText(),Double.parseDouble(val.getText())));
			bChart.getData().add(series);
			if(whoCalled) {				
				series.setName(seriesLabel.getText());
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	private void drawBulkChart() {
		System.out.println("#drawBulkChart");
			bChart.getData().clear();
			table.getItems().clear();
			BarTableController row;
			boolean is_first;
			Series<String, Number> series = new XYChart.Series<String, Number>();
			for(XYChart.Series<String, Number> seriList : SeriList) {
				series = new XYChart.Series<String, Number>();
				series.setName(seriList.getName());
				is_first = true;
				for(XYChart.Data<String, Number> seriData : seriList.getData()) {
					series.getData().add(new XYChart.Data<String,Number>(seriData.getXValue(),seriData.getYValue()));
					if(is_first) {
						row = new BarTableController(seriList.getName(),seriData.getXValue(),seriData.getYValue());
						is_first = false;
					}else {
						row = new BarTableController("",seriData.getXValue(),seriData.getYValue());
					}
					table.getItems().add(row);
				}
				bChart.getData().add(series);
			}
	}
	public void setBulkNames() {
		System.out.println("#setBulkNames");
		if(bulkNms.getText().isEmpty()) {
			error_label.setText("Please enter names for bar first");			
		}else {
			String X = bulkNms.getText();						
			for(int i=0;i< X.length();i++) {
				if(X.charAt(i) == '[') {					
					String xCo = new String();
					for(int j=i;X.charAt(j+1)!=',';j++) {					
						xCo+=X.charAt(j+1);
						System.out.print(X.charAt(j+1));
					}
					System.out.print("\t");
					bulkNames.add(xCo);					
				} else if(X.charAt(i)==',') {					
					String xCo = new String();
					for(int j=i;X.charAt(j+1)!=',' && X.charAt(j+1) != ']' ;j++) {					
						xCo+=X.charAt(j+1);
						System.out.print(X.charAt(j+1));
					}
					System.out.println();
					bulkNames.add(xCo);								
				} else {}					
			}
			System.out.println("\tBulkNames: "+bulkNames);
			bulkNms.setDisable(true);	
			add_b_nms.setDisable(true);
			add_b_data.setDisable(false);
			bulk.setDisable(false);				
		}		
	}
	private boolean inputValidator(String X) {		
		System.out.println("#PatternValidator#");										
		for(int i=0;i< X.length();i++) {
			if(X.charAt(i) == '[') {					
				String xCo = new String();
				for(int j=i;X.charAt(j+1)!=',' && X.charAt(j+1)!=']';j++) {					
					xCo+=X.charAt(j+1);					
				}				
				if(!dblChecker(xCo))
					return false;					
			} else if(X.charAt(i)==',') {					
				String xCo = new String();
				for(int j=i;X.charAt(j+1)!=',' && X.charAt(j+1) != ']' ;j++) {					
					xCo+=X.charAt(j+1);					
				}						
				if(!dblChecker(xCo))
					return false;
			} else {}					
		}
		return true;
	}
	private void decodeAndDraw() {
		System.out.println("#decodeAndDraw");
		String X = bulk.getText();
		if(bulkNames.isEmpty()) {
			System.out.println("\tdecode setting again");
			setBulkNames();
		}else {
			error_label.setText("");
			bulk.setText("");
			System.out.println("---------------\nxVal\tyVal\n---------------");
			int arraySize = (X.length()-((X.length())/5)*3)/2 + 2,yindexCounter=0;
			Number[] yValues = new Number[arraySize];									
			//Pattern reader
			int bulkCounter = 0;						
			for(int i=0;i< X.length();i++) {
				if(X.charAt(i) == '[') {					
					String xCo = new String();
					for(int j=i;X.charAt(j+1)!=',' && X.charAt(j+1) != ']';j++) {
						xCo+=X.charAt(j+1);					
					}
					System.out.println(bulkNames.get(bulkCounter)+"\t"+xCo);
					yValues[yindexCounter]=Double.parseDouble(xCo);
					yindexCounter++;
					bulkCounter++;					
				} else if(X.charAt(i)==',') {					
					String xCo = new String();
					for(int j=i;X.charAt(j+1)!=',' && X.charAt(j+1) != ']' ;j++) {					
						xCo+=X.charAt(j+1);					
					}
					System.out.println(bulkNames.get(bulkCounter)+"\t"+xCo);
					yValues[yindexCounter]=Double.parseDouble(xCo);
					yindexCounter++;
					bulkCounter++;								
				}
			}
			Series<String, Number> series = new XYChart.Series<String, Number>();
			for(int i=0;i<yindexCounter;i++) {
				series.getData().add(new XYChart.Data<String,Number>(bulkNames.get(i),yValues[i]));
			}
			series.setName(seriesLabel.getText());
			SeriList.add(series);
			priList.push(new ArrayList<XYChart.Series<String, Number>>(cloneList(SeriList)));
			drawBulkChart();
		}
	}
	private boolean dblChecker(String x) {
		try {Double.parseDouble(x);}
	    catch(NumberFormatException e) {
	    	error_label.setText("Value field must be a numeric");
	    	return false;
	    }
		return true;
	}
	//utilities functions--------------------------------------
	public void refresh() {}
	private ArrayList<XYChart.Series<String, Number>> cloneList(ArrayList<XYChart.Series<String, Number>> l) {
		ArrayList<XYChart.Series<String, Number>> temp = new ArrayList<XYChart.Series<String, Number>>();
        for(int i=0;i<l.size();i++){
            temp.add(l.get(i));
        }
        return temp;
	}
	public void undo() {
		System.out.print("#UNDO_PIE");
		if(!priList.isEmpty()) {
			if(secList.size() < 21) {
				ArrayList<XYChart.Series<String, Number>> temp = (ArrayList<XYChart.Series<String, Number>>) priList.pop();
		        secList.push(temp);
		        
		        if(priList.empty()){SeriList = new ArrayList<XYChart.Series<String, Number>>();}
		        else{temp = (ArrayList<XYChart.Series<String, Number>>) priList.peek(); SeriList = temp;}
		        
		        drawBulkChart();
			}else
				error_label.setText("Can't do more than 20 undo's");
		}else
			jbp();
	}
	public void redo() {
		System.out.print("#REDO_Bar");
		if(!secList.isEmpty()) {
			ArrayList<XYChart.Series<String, Number>> temp = (ArrayList<XYChart.Series<String, Number>>) secList.pop();
	        priList.push(temp);            
	        temp = (ArrayList<XYChart.Series<String, Number>>) priList.peek();
	        SeriList = temp;
	        drawBulkChart();
		}else
			jbp();
	}
	private void jbp() {Toolkit.getDefaultToolkit().beep();}
	//Save - OPen----------------------------------------------
	public void save() {
		if(FILE_PATH == "") {
			FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Save");
		    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fx Bar Chart", "*.fxbarchart"));
		    File file = fileChooser.showSaveDialog(primaryStage);
  
	        if (file != null) {
	        	FILE_PATH = file.getPath()+".fxbarchart";
	        	callWriter();
	        	error_label.setText("File Saved '"+FILE_PATH+"'");
	        }
		}else {
			callWriter();
			error_label.setText("Saved");
		}
	}
	public void saveas() {
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save");
	    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fx Bar Chart", "*.fxbarchart"));
	    File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
        	FILE_PATH = file.getPath()+".fxbarchart";
        	callWriter();
        	error_label.setText("File Saved '"+FILE_PATH+"'");
        }
	}
	public void load() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fx Bar Chart", "*.fxbarchart"));
		File file = fileChooser.showOpenDialog(anchor.getScene().getWindow());        
		
		if (file != null) {
			clearChart();
			FILE_PATH = file.getAbsolutePath();
			ArrayList<XYChart.Series<String, Number>> seriesArr = barFileSysController.readDataFromFile(FILE_PATH);
			if(!seriesArr.isEmpty()) {			
				SeriList = new ArrayList<XYChart.Series<String, Number>>(seriesArr);
				drawBulkChart();
			}else {
				error_label.setText("File is empty!");
			}
		}
	}
	public void saveXml() {
		if(FILE_PATH == "") {
			FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Save");
		    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("BarChart XML", "*.barxml"));
		    File file = fileChooser.showSaveDialog(primaryStage);
  
	        if (file != null) {
	        	FILE_PATH = file.getPath()+".barxml";
	        	callWriterXml();
	        	error_label.setText("Xml Saved '"+FILE_PATH+"'");
	        }
		}else {
			callWriterXml();
			error_label.setText("Saved");
		}
	}
	public void saveasXml() {
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save");
	    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("BarChart XML", "*.barxml"));
	    File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
        	FILE_PATH = file.getPath()+".barxml";
        	callWriterXml();
        	error_label.setText("Xml Saved '"+FILE_PATH+"'");
        }
	}
	public void loadXml() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("BarChart XML", "*.barxml"));
		File file = fileChooser.showOpenDialog(anchor.getScene().getWindow());        
		
		if (file != null) {
			clearChart();
			ArrayList<XYChart.Series<String, Number>> read = BXFileManager.readDataFromFile(file.getAbsolutePath());
			FILE_PATH = file.getAbsolutePath();
			SeriList = new ArrayList<XYChart.Series<String,	Number>>(read);
			drawBulkChart();
		}
	}
	private void callWriterXml() {BXFileManager.writeDataInFile(SeriList,FILE_PATH);}
	private void callWriter() {barFileSysController.writeDataInFile(SeriList, FILE_PATH);}
	//Previewing-------------------------------------------
	public void previewPDF() {}
	//Editing-----------------------------------------------
	public void editFromTable() {
		System.out.println("#editDataFromTable");		
		Boolean isDvalid = true;
		int i=0;
		ArrayList<String> nmArr = new ArrayList<String>();
		ArrayList<String> bufx = new ArrayList<String>();
		ArrayList<Number> bufy = new ArrayList<Number>();
		ArrayList<Integer> bkCnt = new ArrayList<Integer>();
		int breakAfter=0;
		for(i=0;i<table.getItems().size();i++) {					
			bufx.add(table.getItems().get(i).getSeriesX());
			bufy.add(table.getItems().get(i).getSeriesY());
			if(!table.getItems().get(i).getSeries().isEmpty()) {
				nmArr.add(table.getItems().get(i).getSeries());
				if(i!=0)
					bkCnt.add(breakAfter);
				breakAfter = 0;
			}	
			breakAfter++;
			if(i==table.getItems().size()-1) {
				bkCnt.add(breakAfter);
			}
		}
		System.out.println("\tnmArr"+nmArr+"\n\tBkCnt: "+bkCnt+"\n\txArr: "+bufx+"\n\tyArr: "+bufy);
		
		if(isDvalid) {
			int selector = 0;
			clearChart();
			clearTable();
			bulkNames.clear();
			for(i=0;i<nmArr.size();i++) {
				System.out.println("\tisDvalid\n\t\tnmArr: "+nmArr.get(i)+"\n\t\tSeletr: "+selector);
				String[] xArr = new String[bkCnt.get(i)];
				Number[] yArr = new Number[bkCnt.get(i)];
				for(int j=0;j<bkCnt.get(i);j++) {
					xArr[j] = bufx.get(selector+j);
					bulkNames.add(xArr[j]);
					yArr[j] = bufy.get(selector+j);
					System.out.println("\t<"+xArr[j]+"><"+yArr[j]+">");
				}
				selector+=bkCnt.get(i);				
			}
			drawBulkChart();
		}else
			error_label.setText("Value of "+table.getItems().get(i).getSeries()+" must be a number");
	}
	private void openPdfTextDialog(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BarChart/PdfTextDialog.fxml"));
        Parent parent;
		try {
			parent = fxmlLoader.load();
	        Scene scene = new Scene(parent);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setScene(scene);
	        stage.setResizable(false);
	        stage.showAndWait();
		} catch (IOException e) {e.printStackTrace();}
    }
	//Exports-------------------------------------------
	public void copyImgToClipBoard() {
		WritableImage nodeshot = bChart.snapshot(new SnapshotParameters(), null);
    	
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
	public void pdfExtract() {
		openPdfTextDialog();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"));
		File file = fileChooser.showSaveDialog(primaryStage);
	      
        if (file != null) {
			WritableImage nodeshot = bChart.snapshot(new SnapshotParameters(), null);
	        File imgfile = new File("dat/imgs/bchart.png");		        
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", imgfile);
			} catch (IOException e) {
				System.out.println("Error in making image!");
			}			
			try {
			    String k = pdfTextApp;				    
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
			    error_label.setText("PDF exported!");
			} catch (Exception e) {
			    e.printStackTrace();
			} finally {
				imgfile.delete();
			}
		}
    }	
	public void pngExtract() {
		  FileChooser fileChooser = new FileChooser();
	      fileChooser.setTitle("Save");
	      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"));
	      File file = fileChooser.showSaveDialog(primaryStage);
	      
          if (file != null) {		    
			WritableImage nodeshot = bChart.snapshot(new SnapshotParameters(), null);
			file = new File(file.getAbsolutePath()+".png");
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
				error_label.setText("PNG\n\tImage exported");
			} catch (IOException e) {
				error_label.setText("Error in making image!");
			}
		}
	}
	public void csvExtract() throws IOException {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(primaryStage);
        final ObservableList<BarTableController> obdata = FXCollections.observableArrayList();
        ArrayList<XYChart.Series<String, Number>> data = barFileSysController.readDataFromFile(FILE_PATH);						
        
        for(XYChart.Series<String, Number> seriList: data)
        	for(XYChart.Data<String, Number> seriData : seriList.getData())
        		obdata.add(new BarTableController(seriList.getName(),seriData.getXValue(),seriData.getYValue()));
        
        Writer writer = null;
        try {        	
        	file = new File(file.getAbsolutePath()+".csv");
            writer = new BufferedWriter(new FileWriter(file));
            for (BarTableController person : obdata) {
                String text = person.getSeries() + "," + person.getSeriesX() +","+ person.getSeriesY()+"\n";
                writer.write(text);                
            }
            System.out.println("Export CSV\n\t CSV exported");
        } catch (Exception ex) {ex.printStackTrace();System.out.println("Export CSV\n\t CSV exported error");}
        finally {writer.flush();writer.close();}
    }
	public void pdfTableExtract() throws DocumentException, FileNotFoundException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("pdf", "*.pdf"));
		File file = fileChooser.showSaveDialog(primaryStage);
	      
        if (file != null) {
        	file = new File(file.getAbsolutePath()+".pdf");
        	Document report= new Document();
            PdfWriter.getInstance(report, new FileOutputStream(file));
            report.open();
            PdfPTable report_table = new PdfPTable(3);

            for(int i=0;i<table.getItems().size();i++) {
            	report_table.addCell(getCell(((BarTableController) table.getItems().get(i)).getSeries(),PdfPCell.ALIGN_CENTER ));
            	report_table.addCell(getCell(((BarTableController) table.getItems().get(i)).getSeriesX().toString(),PdfPCell.ALIGN_CENTER ));            	
            	report_table.addCell(getCell(((BarTableController) table.getItems().get(i)).getSeriesY().toString(),PdfPCell.ALIGN_CENTER ));
            }
            report.add(report_table);
            report.close();
            System.out.println("Export\n\tLine Table exported!");
		}
    }
	private PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);        
        return cell;
    }
	//New charts opening-----------------------------------------------------------
	public void openPieKit() {	
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = (Stage) anchor.getScene().getWindow();
			openNew.setStage(thisstage,"Pie chart");
			System.out.println("Opening Pie Chart closed bar chart");
		} catch (IOException e) {e.printStackTrace();}
	}
	public void openBarKit() {
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = (Stage) anchor.getScene().getWindow();
			openNew.setStage(thisstage,"Bar chart");
			System.out.println("Opening bar Chart closed bar chart");
		} catch (IOException e) {e.printStackTrace();}
	}
	public void openLineKit() {
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = (Stage) anchor.getScene().getWindow();
			openNew.setStage(thisstage,"Line chart");
			System.out.println("Opening Line Chart closed bar chart");
		} catch (IOException e) {e.printStackTrace();}
	}
	public void openStackKit() {
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = (Stage) anchor.getScene().getWindow();
			openNew.setStage(thisstage,"Stacked chart");
			System.out.println("Opening stack Chart closed bar chart");
		} catch (IOException e) {e.printStackTrace();}
	}
	public void openFxPaintKit() {
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = (Stage) anchor.getScene().getWindow();
			openNew.setStage(thisstage,"Paint");
			System.out.println("Opening Paint closed bar chart");
		} catch (IOException e) {e.printStackTrace();}
	}
}
