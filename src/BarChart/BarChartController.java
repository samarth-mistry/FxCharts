package BarChart;

import FileSys.barFileSysController;
import PieChart.PieTableController;
import DbSys.DbController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class BarChartController {
	@FXML private BarChart<String, Number> bChart;
	@FXML private Label error_label;
	@FXML private Label table_error_label;
	@FXML private Label barValue;
	@FXML private Label l6;
	@FXML private Label l5;
	@FXML private Label l4;
	@FXML private Label l3;
	@FXML private Label l2;
	@FXML private Label l1;
	@FXML private TextArea bulkNms;
	@FXML private TextArea bulk;
	@FXML private TextField nm;
	@FXML private TextField val;
	@FXML private TextField barChartTitle;
	@FXML private TextField seriesLabel;
	@FXML private TextField xxisLabel;
	@FXML private TextField yxisLabel;
	@FXML private CategoryAxis xxis;
	@FXML private NumberAxis yxis;
	@FXML private TableView<BarTableController> table;
	@FXML private TableColumn<String, String> c1;
	@FXML private TableColumn<String, String> c2;
	@FXML private TableColumn<String, String> c3;
	@FXML private CheckBox dbEnable;
	@FXML private CheckBox fileEnable;
	@FXML private CheckBox bulkEnable;
	@FXML private Button loadDataFromFile;
	@FXML private Button loadDataFromDb;
	@FXML private Button loadDataInTable;
	@FXML private Button add_data;
	@FXML private Button add_b_data;
	@FXML private Button add_b_nms;
	@FXML private AnchorPane anchor1;
	@FXML private AnchorPane anchor2;
	@FXML private Circle theameCircle;	    
    final KeyCombination altEnter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
    final KeyCombination alt1= new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.ALT_DOWN);
    final KeyCombination alt2= new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.ALT_DOWN);
    final KeyCombination alt3= new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.ALT_DOWN);
    final KeyCombination altT= new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN);
    final KeyCombination altj= new KeyCodeCombination(KeyCode.J, KeyCombination.ALT_DOWN);
    final KeyCombination altk= new KeyCodeCombination(KeyCode.K, KeyCombination.ALT_DOWN);
    final KeyCombination altl= new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN);
    final KeyCombination altb= new KeyCodeCombination(KeyCode.B, KeyCombination.ALT_DOWN);
    final KeyCombination altf= new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
    final KeyCombination altd= new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlPrintPDF = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlPrintPNG = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlPrintTPDF = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlQ = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
	@FXML
    void btnOnKeyPressed(KeyEvent event) {	
		if (alt1.match(event)) {setBulkNames();bulk.requestFocus();}
		if (alt2.match(event)) {addBulkData();bulk.requestFocus();}
		if (alt2.match(event)) {addBulkData();bulk.requestFocus();}
		
		if (altEnter.match(event)) {addData();nm.requestFocus();}
		if (altT.match(event)) {changeTheme();}
		
		if (altj.match(event)) {clearChart();}
		if (altk.match(event)) {clearFile();}
		if (altl.match(event)) {clearDb();}
		
		if (altb.match(event)) {
			if(bulkEnable.isSelected())
				bulkEnable.setSelected(false);
			else
				bulkEnable.setSelected(true);
			
			buttonEnabler();
		}
		if (altf.match(event)) {
			if(fileEnable.isSelected())
				fileEnable.setSelected(false);
			else
				fileEnable.setSelected(true);
			
			buttonEnabler();
		}
		if (altd.match(event)) {
			if(dbEnable.isSelected())
				dbEnable.setSelected(false);
			else
				dbEnable.setSelected(true);
			
			buttonEnabler();
		}
		
        if (ctrlPrintPDF.match(event)) {pdfExtract();}
        if (ctrlPrintPNG.match(event)) {pngExtract();}
        if (ctrlQ.match(event)) {exit();}
        if (ctrlPrintTPDF.match(event)) {        	
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
	}
	private boolean theame=true;
	final Stage primaryStage = null;
	final double SCALE_DELTA = 1.1;
	private ArrayList<String> bulkNames = new ArrayList<String>();
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
		error_label.setText("Chart Data Cleared!\nClick load data to reload");				
	}
	public void clearFile() {		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");
		alert.setHeaderText("Clearing File will permanently delete your data!");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			barFileSysController.clearFile("barFileData.txt");
			error_label.setText("File is Cleared\nData is permanently lost");
		}		
	}
	public void clearTable() {
		table_error_label.setText("Table it cleared!\n To reload click on load button");
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
			error_label.setText("DB is Cleared\nData is permanently lost");
		}		
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
	public void axisRenamed() {
		bChart.setTitle(barChartTitle.getText());				
		xxis.setLabel(xxisLabel.getText());
		yxis.setLabel(yxisLabel.getText());		
		if(barChartTitle.getText() == "")
			bChart.setTitle("Line Chart");		
		if(xxisLabel.getText() == "")
			xxis.setLabel("X->");
		if(yxisLabel.getText() == "")
			yxis.setLabel("X->");		
	}
	public void buttonEnabler() {
		if(fileEnable.isSelected()) {
			loadDataFromFile.setDisable(false);
			loadDataInTable.setDisable(false);
		}else {
			loadDataInTable.setDisable(true);
			loadDataFromFile.setDisable(true);
		}
		if(dbEnable.isSelected()) {
			loadDataFromDb.setDisable(false);
		}
		if(!dbEnable.isSelected()){
			loadDataFromDb.setDisable(true);
		}
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
			Color c = Color.web("#d8d8d8");
			error_label.setTextFill(Color.web("red"));
			error_label.setStyle("-fx-border-color: red");
			anchor1.setStyle("-fx-background-color:  #666666");
			anchor2.setStyle("-fx-background-color:  #666666");
			bChart.setStyle("-fx-border-color: #d8d8d8");
		    bChart.setStyle("-fx-text-fill: black");
			barValue.setStyle("-fx-border-color: #d8d8d8");
			barValue.setTextFill(c);
			xxis.setTickLabelFill(c);yxis.setTickLabelFill(c);			
			xxis.setStyle("-fx-text-fill: black");
			yxis.setStyle("-fx-text-fill: black");			
			l1.setTextFill(c);
			l2.setTextFill(c);
			l3.setTextFill(c);
			l4.setTextFill(c);
			l5.setTextFill(c);
			l6.setTextFill(c);
			bulkEnable.setTextFill(c);
			fileEnable.setTextFill(c);
			dbEnable.setTextFill(c);					
			theameCircle.setFill(c);
			theame = false;
		}
		else {		//dark
			Color c = Color.web("#666666");
			error_label.setTextFill(Color.web("pink"));
			error_label.setStyle("-fx-border-color:  pink");
			anchor1.setStyle("-fx-background-color:  #d8d8d8");
			anchor2.setStyle("-fx-background-color:  #d8d8d8");
			bChart.setStyle("-fx-border-color: #666666");
			bChart.setStyle("-fx-text-fill: white");
			barValue.setStyle("-fx-border-color: #666666");
			barValue.setTextFill(c);
			xxis.setTickLabelFill(c);
			yxis.setTickLabelFill(c);
			xxis.setStyle("-fx-text-fill: white");
			yxis.setStyle("-fx-text-fill: white");
			l1.setTextFill(c);
			l2.setTextFill(c);
			l3.setTextFill(c);
			l4.setTextFill(c);
			l5.setTextFill(c);
			l6.setTextFill(c);
			bulkEnable.setTextFill(c);
			fileEnable.setTextFill(c);
			dbEnable.setTextFill(c);					
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
				if(dbEnable.isSelected()) {
					try {if(!DbController.insertSeries(seriesLabel.getText(),1,X)) {error_label.setText("Error in Database");}} 
					catch (SQLException e) {e.printStackTrace();}
				}
				drawSinChart(true);
			}else
				error_label.setText("Value of val must be number");
		}
	}
	public void addBulkData() {
		String X = "{"+seriesLabel.getText()+"}"+bulk.getText();
		if(bulkNms.getText().isEmpty())
			error_label.setText("Enter Pattern before adding");
		else if(seriesLabel == null) {
			error_label.setText("Please enter the name of Bar");
		}
		else if(!inputValidator(X)) {
			error_label.setText("");
			error_label.setText("Entered pattern is not valid\nPlease try again!");
			System.out.println("Invalid Pattern");
		} else {
			if(dbEnable.isSelected()) {
				try {if(!DbController.insertSeries(seriesLabel.getText(),1,X)) {error_label.setText("Error in Database");}} 
				catch (SQLException e) {e.printStackTrace();}
			}
			decodeAndDraw(seriesLabel.getText(),X,true);					
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
	private void drawBulkChart(String barName,Number yValues[],int filled,Boolean whoCalled) {
		System.out.println("#drawBulkChart");
		if(!bulkNames.isEmpty()) {
			try {								
				XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();			
				int i=0;
				for(i=0;i<filled;i++) {
					if(!whoCalled) {
						series.setName(barName);
					}
					System.out.println("Plotting: "+bulkNames.get(i)+yValues[i]);
					series.getData().add(new XYChart.Data<String, Number>(bulkNames.get(i),yValues[i]));
				}			
				Number[] yFin= new Number[i];
				for(i=0;i<filled;i++) {				
					yFin[i] = yValues[i];
				}							
				if(whoCalled) {				//add function is calling them 	
					series.setName(seriesLabel.getText());
					if(fileEnable.isSelected()) {
						callWriter(barName,bulkNames, yFin);
					}
				}
				bChart.getData().add(series);
				barValuePlotter();
			}catch(Exception e) {e.printStackTrace();}
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
	public void drawSampleBar() {
		bChart.getData().clear();
		bChart.setTitle("Country Summary");
        xxis.setLabel("Country");       
        yxis.setLabel("Value");
        
        final String austria = "Austria";
        final String brazil = "Brazil";
        final String france = "France";
        final String italy = "Italy";
        final String usa = "USA";
        
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2003");       
        series1.getData().add(new XYChart.Data(austria, 25601.34));
        series1.getData().add(new XYChart.Data(brazil, 20148.82));
        series1.getData().add(new XYChart.Data(france, 10000));
        series1.getData().add(new XYChart.Data(italy, 35407.15));
        series1.getData().add(new XYChart.Data(usa, 12000));      
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("2004");
        series2.getData().add(new XYChart.Data(austria, 57401.85));
        series2.getData().add(new XYChart.Data(brazil, 41941.19));
        series2.getData().add(new XYChart.Data(france, 45263.37));
        series2.getData().add(new XYChart.Data(italy, 117320.16));
        series2.getData().add(new XYChart.Data(usa, 14845.27));  
        
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("2005");
        series3.getData().add(new XYChart.Data(austria, 45000.65));
        series3.getData().add(new XYChart.Data(brazil, 44835.76));
        series3.getData().add(new XYChart.Data(france, 18722.18));
        series3.getData().add(new XYChart.Data(italy, 17557.31));
        series3.getData().add(new XYChart.Data(usa, 92633.68));  
                
        bChart.getData().addAll(series1, series2, series3);
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
	private void decodeAndDraw(String bar,String X,Boolean whoCalled) {
		System.out.println("#decodeAndDraw");
		if(bulkNames.isEmpty()) {
			setBulkNames();
		}		
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
			} else {}					
		}
		System.out.println("---------------\nStorage values :");		
		System.out.println("\tX: "+bulkNames+"\n\tY: "+Arrays.toString(yValues));
		System.out.println("IndexCounters:\n\tY: "+yindexCounter);		
		drawBulkChart(bar,yValues,yindexCounter,whoCalled);		
	}
	private boolean dblChecker(String x) {
		try {Double.parseDouble(x);}
	    catch(NumberFormatException e) {
	    	error_label.setText("Value field must be a numeric");
	    	return false;
	    }
		return true;
	}
	//loading functions------------------------------------------------------
	private void callWriter(String barNames,ArrayList<String> xValues,Number[] yFin) {		
		barFileSysController.writeDataInFile(xValues,yFin, barNames,0);
	}
	public void loadDataFromFile() {
		if(!bChart.getData().isEmpty()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("ALERT");
			alert.setHeaderText("Your Chart currently having data.\nBy loading it from file it will clear it");
			alert.setContentText("Are you sure to overload?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				loadDataFromFileConfirmed();
			} else {
			    // ... user chose CANCEL or closed the dialog
			}
		} else {
			loadDataFromFileConfirmed();
		}
	}
	public void loadDataFromFileConfirmed() {
		clearChart();
		bulkNames.clear();
		ArrayList<String> seriesArr = barFileSysController.readDataFromFile();		
		if(!seriesArr.isEmpty()) {			
			for(int seriIndex=0;seriIndex< seriesArr.size();seriIndex++) {																				
				String X = seriesArr.get(seriIndex);	
				int arraySize = (X.length()-((X.length())/5)*3)/2 + 2,yindexCounter=0;
				Number[] yValues = new Number[arraySize];
				String line = "";
				if(X.charAt(0) == '{') {						
					for(int j=0; X.charAt(j+1) != '}'; j++) {
						line += X.charAt(j+1);
					}						
				}
				if(seriIndex == 0) {
					for(int k=0;k<X.length();k++) {
						if(X.charAt(k)=='[') {
							String str4blk = "";
							for(int j=k;X.charAt(j+1) != ',';j++) {
								str4blk+=X.charAt(j+1);
							}
							bulkNames.add(str4blk);
						}
					}
					System.out.println("\tBulkNames: "+bulkNames);
				}
				for(int k=0;k<X.length();k++) {
					if(X.charAt(k)==',') {					
						String xCo = new String();
						for(int j=k;X.charAt(j+1) != ']';j++) {					
							xCo+=X.charAt(j+1);					
						}					
						yValues[yindexCounter]=Double.parseDouble(xCo);
						yindexCounter++;					
					}				
				}
				drawBulkChart(line,yValues,yindexCounter,false);
			}
		}else {
			error_label.setText("File is empty!\nPlease add data first");
		}
	}	
	public void loadDataInTable() {					        
		ArrayList<String> data = barFileSysController.readDataFromFile();
		table.setEditable(true);
		c1.setCellValueFactory(new PropertyValueFactory<>("series"));		
		c2.setCellValueFactory(new PropertyValueFactory<>("seriesX"));
		c3.setCellValueFactory(new PropertyValueFactory<>("seriesY"));
		c1.setSortable(false);
		c2.setSortable(false);
		c3.setSortable(false);
		
		if(!data.isEmpty()) {			
			for(int seriIndex=0;seriIndex< data.size();seriIndex++) {																				
				String X = data.get(seriIndex);	
				int arraySize = (X.length()-((X.length())/5)*3)/2 + 5,yindexCounter=0;
				String[] lineNames =new String[arraySize];
				Number[] yValues = new Number[arraySize];				
				String line = "";
								
				for(int k=0;k<X.length();k++) {
					if(X.charAt(k) == '{') {						
						for(int j=0; X.charAt(j+1) != '}'; j++) {
							line += X.charAt(j+1);
						}
						lineNames[k] = line;
					}
					if(X.charAt(k)=='[') {
						String str4blk = "";
						for(int j=k;X.charAt(j+1) != ',';j++) {
							str4blk+=X.charAt(j+1);
						}
						bulkNames.add(str4blk);
					}
				}
				//System.out.println("\tBulkNames: "+bulkNames);			
				for(int k=0;k<X.length();k++) {
					if(X.charAt(k)==',') {					
						String xCo = new String();
						for(int j=k;X.charAt(j+1) != ']';j++) {					
							xCo+=X.charAt(j+1);					
						}					
						yValues[yindexCounter]=Double.parseDouble(xCo);
						yindexCounter++;					
					}				
				}
				for(int i=0;i<yindexCounter;i++) {
					String value1 = bulkNames.get(i);
					Number value2 = yValues[i];
										    	
					BarTableController row = new BarTableController(lineNames[i],value1,value2);
					table.getItems().add(row);										
				}
			}
		}			
	}	
	public void loadDataFromDb() {
		clearChart();		
		ArrayList<String> seriesArr = null;
		try {
			seriesArr = DbController.getSeries();
		} catch (SQLException e) {
			error_label.setText("Error fetching data from DB");
			e.printStackTrace();
		}
		if(!seriesArr.isEmpty()) {			
			for(int seriIndex=0;seriIndex< seriesArr.size();seriIndex++) {
				String X = seriesArr.get(seriIndex);
				String line = "";
				if(X.charAt(0) == '{') {						
					for(int j=0; X.charAt(j+1) != '}'; j++) {
						line += X.charAt(j+1);
					}						
				}
				decodeAndDraw(line,X,false);
			}
		}else {
			error_label.setText("DB is empty!\nPlease add data first");
		}
	}
	//Exports--------------------------------------------
	public void pdfExtract() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"));
		File file = fileChooser.showSaveDialog(primaryStage);
	      
        if (file != null) {
				WritableImage nodeshot = bChart.snapshot(new SnapshotParameters(), null);
		        File imgfile = new File("84h8chart.png");
		
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
				} catch (IOException e) {
					System.out.println("Error in making image!");
				}
		        PDDocument doc = new PDDocument();
		        PDPage page = new PDPage();
		        PDImageXObject pdimage;
		        PDPageContentStream content;
		        try {
		            pdimage = PDImageXObject.createFromFile("84h8chart.png",doc);
		            content = new PDPageContentStream(doc, page);
		            content.drawImage(pdimage,50, 50);
		        	content.beginText();                             
		            content.setFont(PDType1Font.COURIER, 15);                          
		            content.newLineAtOffset(10, 770);            
		            String text = "LineChart by Cancer";             
		            content.showText(text);        
		            content.endText();
		            content.close();
		            doc.addPage(page);
		            doc.save(file.getAbsolutePath()+".pdf");
		            doc.close();
		            System.out.println("DOC\n\tPdf Exported!");
		            imgfile.delete();
		        } catch (IOException ex) {
		        	error_label.setText("Error occured in exporting PDF");		    
		            Logger.getLogger(BarChart.class.getName()).log(Level.SEVERE, null, ex);
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
				System.out.println("PNG\n\tImage exported");
			} catch (IOException e) {
				System.out.println("Error in making image!");
			}
		}
	}
	public void csvExtract() throws IOException {
	    FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(primaryStage);
        final ObservableList<BarTableController> obdata = FXCollections.observableArrayList();
        ArrayList<String> data = barFileSysController.readDataFromFile();              
        if(!data.isEmpty()) {			
			for(int seriIndex=0;seriIndex< data.size();seriIndex++) {																				
				String X = data.get(seriIndex);	
				int arraySize = (X.length()-((X.length())/5)*3)/2 + 5,yindexCounter=0;
				String[] lineNames =new String[arraySize];
				Number[] yValues = new Number[arraySize];				
				String line = "";
								
				for(int k=0;k<X.length();k++) {
					if(X.charAt(k) == '{') {						
						for(int j=0; X.charAt(j+1) != '}'; j++) {
							line += X.charAt(j+1);
						}
						lineNames[k] = line;
					}
					if(X.charAt(k)=='[') {
						String str4blk = "";
						for(int j=k;X.charAt(j+1) != ',';j++) {
							str4blk+=X.charAt(j+1);
						}
						bulkNames.add(str4blk);
					}
				}
				//System.out.println("\tBulkNames: "+bulkNames);			
				for(int k=0;k<X.length();k++) {
					if(X.charAt(k)==',') {					
						String xCo = new String();
						for(int j=k;X.charAt(j+1) != ']';j++) {					
							xCo+=X.charAt(j+1);					
						}					
						yValues[yindexCounter]=Double.parseDouble(xCo);
						yindexCounter++;					
					}				
				}
				for(int i=0;i<yindexCounter;i++) {
					String value1 = bulkNames.get(i);
					Number value2 = yValues[i];
										    	
	    			obdata.add(new BarTableController(lineNames[i],value1,value2));							
				}				
			}
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
		}else {
			error_label.setText("File is empty!\nPlease add data first for exporting");
		}																		                       
    }
	public void pdfTableExtract() throws DocumentException, FileNotFoundException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("pdf", "*.pdf"));
		File file = fileChooser.showSaveDialog(primaryStage);
	      
        if (file != null) {
        	loadDataInTable();
        	file = new File(file.getAbsolutePath()+".pdf");
        	Document report= new Document();
            PdfWriter.getInstance(report, new FileOutputStream(file));
            report.open();            
            //we have four columns in our table
            PdfPTable report_table = new PdfPTable(3);

            for(int i=0;i<table.getItems().size();i++) {
            	report_table.addCell(getCell(((BarTableController) table.getItems().get(i)).getSeries(),PdfPCell.ALIGN_CENTER ));
            	report_table.addCell(getCell(((BarTableController) table.getItems().get(i)).getSeriesX(),PdfPCell.ALIGN_CENTER ));
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
}
