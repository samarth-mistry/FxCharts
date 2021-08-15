package LineChart;

import FileSys.lineFileSysController;
import Main.FxChartMainPage;
import DbSys.DbController;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

@SuppressWarnings("deprecation")
public class LineChartController {
	@FXML private MenuBar menuBar;
	@FXML private LineChart<Double, Double> lChart;
	@FXML private Label error_label,table_error_label,lineValue,l4,l3,l2,l1,app_log;
	@FXML private TextArea xVal;
	@FXML private TextField lineChartTitle,seriesLabel,xxisLabel,yxisLabel;
	@FXML private NumberAxis xxis,yxis;
	@FXML private TableView<LineTableController> table;
	@FXML private TableColumn<LineTableController, String> c1;
	@FXML private TableColumn<LineTableController, Double> c2,c3;
	@FXML private CheckBox dbEnable,fileEnable,clickDraw;
	@FXML private Button loadDataFromFile,loadDataFromDb,loadDataInTable,add_data,add_click_data;	
	@FXML private AnchorPane anchor;
	@FXML private Circle theameCircle;
	@FXML private DatePicker setDate;
	@FXML private ToggleButton themeToggle;
	final Stage primaryStage = null;
	final double SCALE_DELTA = 1.1;
	private DateTimeFormatter classic=DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private boolean theame=true,isFullScr=false;;
	private String clickDataStr = null;
	Stage stage = null;
	static String pdfTextApp = null;
	
	final KeyCombination 
	altEnter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN),
    altj= new KeyCodeCombination(KeyCode.J, KeyCombination.ALT_DOWN),
    altk= new KeyCodeCombination(KeyCode.K, KeyCombination.ALT_DOWN),
    altl= new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN),
    ctrlb= new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN),
    ctrlf= new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN),
    ctrld= new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN),
    ctrlPrintPDF = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN),
    ctrlPrintPNG = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN),
    ctrlPrintTPDF = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN),
    ctrlQ = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
    altT= new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN),
    altF11= new KeyCodeCombination(KeyCode.F11, KeyCombination.ALT_DOWN);	
	//ShortCut functions----------------------------------------------------
    @FXML public void initialize() {
        System.out.println("#initialized#");
        setDate.setValue(LocalDate.now());
        setDate.setConverter(new StringConverter<LocalDate>(){            
            @Override
            public String toString(LocalDate localDate){
                if(localDate==null)
                    return "";
                return classic.format(localDate);
            }
            @Override
            public LocalDate fromString(String dateString){
                if(dateString==null || dateString.trim().isEmpty())
                	return null;
                return LocalDate.parse(dateString,classic);
            }
        });//set date to classic format DDMMYYYY
        table.setEditable(true);		
		c1.setCellValueFactory(new PropertyValueFactory<>("series"));			
		c2.setCellValueFactory(new PropertyValueFactory<>("seriesX"));
		c3.setCellValueFactory(new PropertyValueFactory<>("seriesY"));
		c1.setSortable(false);
		c2.setSortable(false);
		c3.setSortable(false);
		table.getSelectionModel().cellSelectionEnabledProperty().set(true);
		c1.setCellFactory(TextFieldTableCell.forTableColumn());
		c2.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		c3.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	    c1.setOnEditCommit(new EventHandler<CellEditEvent<LineTableController, String>>() {
	         public void handle(CellEditEvent<LineTableController, String> t) {
	               ((LineTableController)t.getTableView().getItems().get(t.getTablePosition().getRow())).setSeries(t.getNewValue().toString());
	               editFromTable();
	         }
	    });
	    c2.setOnEditCommit(new EventHandler<CellEditEvent<LineTableController, Double>>() {
	         public void handle(CellEditEvent<LineTableController, Double> t) {
	               ((LineTableController)t.getTableView().getItems().get(t.getTablePosition().getRow())).setSeriesX(t.getNewValue());
	               editFromTable();
	         }
	    });
	    c3.setOnEditCommit(new EventHandler<CellEditEvent<LineTableController, Double>>() {
	         public void handle(CellEditEvent<LineTableController, Double> t) {
	               ((LineTableController)t.getTableView().getItems().get(t.getTablePosition().getRow())).setSeriesY(t.getNewValue());
	               editFromTable();
	         }
	    });
        
        cursorMoni();
    }
    @FXML void bulkEntriesPressed(KeyEvent event) {
    	if (altEnter.match(event)) {addData();event.consume();}
    	else {btnOnKeyPressed(event);event.consume();}
    }
	@FXML void btnOnKeyPressed(KeyEvent event) {									
		if (altT.match(event)) {changeTheme();event.consume();}		
		else if (altj.match(event)) {clearChart();event.consume();}
		else if (altk.match(event)) {clearFile();event.consume();}
		else if (altl.match(event)) {clearDb();event.consume();}				
		else if (ctrlf.match(event)) {
			if(fileEnable.isSelected())
				fileEnable.setSelected(false);
			else
				fileEnable.setSelected(true);
			event.consume();
			buttonEnabler();
		}
		else if (ctrld.match(event)) {
			if(dbEnable.isSelected())
				dbEnable.setSelected(false);
			else
				dbEnable.setSelected(true);
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
		else if(altF11.match(event))
			setFullscrn();
		else
			event.consume();		
	}
	//Zooming-------------------------------------------------
	public void zoomLineChart(ScrollEvent event) {
		 double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
        lChart.setScaleX(lChart.getScaleX() * scaleFactor);
        lChart.setScaleY(lChart.getScaleY() * scaleFactor);
	}
	public void zoomNormal(MouseEvent e) {
	    if (e.getClickCount() == 2) {
	        lChart.setScaleX(1.0);
	        lChart.setScaleY(1.0);
	    }		    
	}
	//Clear Functions-----------------------------------------------------------
	public void clearChart() {
		lChart.getData().clear();
		error_label.setText("Chart Data Cleared!\nClick load data to reload");				
	}
	public void clearFile() {		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");
		alert.setHeaderText("Clearing File will permanently delete your data!");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			lineFileSysController.clearFile("locations.txt");
			error_label.setText("File is Cleared\nData is permanently lost");
		}		
	}
	public void clearTable() {
		error_label.setText("Table it cleared!\n To reload click on load button");
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
	public void refresh() {
		
	}
	public void clearAll() {
		clearChart();
		clearTable();
		clearFile();
	}
	//Configuration functions Functions-----------------------------------------------------------
	public void setFullscrn() {
		System.out.println("#FullScreen");
		stage = (Stage) anchor.getScene().getWindow();
		stage.setFullScreenExitHint("Press Alt + F11 to exit full-screen mode");
		if(!isFullScr) {
			stage.setFullScreen(true);
			isFullScr=true;
		}else {
			stage.setFullScreen(false);
			isFullScr=false;
		}		
	}
	public void axisRenamed() {
		lChart.setTitle(lineChartTitle.getText());				
		xxis.setLabel(xxisLabel.getText());
		yxis.setLabel(yxisLabel.getText());
		c2.setText(xxisLabel.getText());
		c3.setText(yxisLabel.getText());
		if(lineChartTitle.getText() == "")
			lChart.setTitle("Line Chart");		
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
		if(clickDraw.isSelected()) {
			add_click_data.setDisable(false);			
		}else {			
			add_click_data.setDisable(true);
		}
	}
	public void changeTheme() {
		if(theame) {				//light=Theme(true)#666666->white[light->dark]
			Color c = Color.web("white");			
			error_label.setStyle("-fx-border-color: white");
			app_log.setStyle("-fx-border-color: white");
			app_log.setTextFill(c);
			anchor.setStyle("-fx-background-color:  #666666");			
			lChart.setStyle("-fx-border-color: white");
		    lChart.setStyle("-fx-text-fill: white");			
			lineValue.setTextFill(c);
			xxis.setTickLabelFill(c);
			yxis.setTickLabelFill(c);			
			xxis.setStyle("-fx-text-fill: black");
			yxis.setStyle("-fx-text-fill: black");			
			l1.setTextFill(c);
			l2.setTextFill(c);
			l3.setTextFill(c);
			l4.setTextFill(c);
			fileEnable.setTextFill(c);
			dbEnable.setTextFill(c);					
			theameCircle.setFill(Color.web("#d8d8d8"));
			theame = false;
		}
		else {		//dark[if dark then set Light]
			Color c = Color.web("black");			
			error_label.setStyle("-fx-border-color: black");
			app_log.setStyle("-fx-border-color: black");
			app_log.setTextFill(c);
			anchor.setStyle("-fx-background-color:  #d8d8d8");			
			lChart.setStyle("-fx-border-color: black");
			lChart.setStyle("-fx-text-fill: black");			
			lineValue.setTextFill(c);
			xxis.setTickLabelFill(c);
			yxis.setTickLabelFill(c);
			xxis.setStyle("-fx-text-fill: white");
			yxis.setStyle("-fx-text-fill: white");
			l1.setTextFill(c);
			l2.setTextFill(c);
			l3.setTextFill(c);
			l4.setTextFill(c);
			fileEnable.setTextFill(c);
			dbEnable.setTextFill(c);					
			theameCircle.setFill(c);
			theame = true;
		}
	}
	//Add Functions-----------------------------------------------------------
	public void addData() {		
		System.out.println("#AddData#");
		String X = "{"+seriesLabel.getText()+"}"+xVal.getText();
		if(xVal.getText().isEmpty())
			error_label.setText("Enter Pattern before adding");
		else if(seriesLabel == null) {
			error_label.setText("Please enter the name of Line");
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
	public void addClickData() {
		decodeAndDraw(seriesLabel.getText(),clickDataStr,true);
		clickDataStr="";
	}
	//Draw & decoding Validations Functions-----------------------------------------------------------
	private void cursorMoni() {
	    final Axis<Double> xAxis = lChart.getXAxis();
	    final Axis<Double> yAxis = lChart.getYAxis();	    	    
	    final Node chartBackground = lChart.lookup(".chart-plot-background");
	    for (Node n: chartBackground.getParent().getChildrenUnmodifiable()) {
	      if (n != chartBackground && n != xAxis && n != yAxis) {
	        n.setMouseTransparent(true);
	      }
	    }	    	    
	    chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	        	lineValue.setText(
	            String.format("(%.2f, %.2f)",xAxis.getValueForDisplay(mouseEvent.getX()),
	              yAxis.getValueForDisplay(mouseEvent.getY())
	            )
	          );
	        }
	      });
	    chartBackground.setOnMouseExited(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent mouseEvent) {
	     	  lineValue.setText("");	    	  
	         }
	    });
	    chartBackground.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent mouseEvent) {
	        	if(clickDraw.isSelected()) {	        		
	        		String ckPnt = String.format("[%.2f,%.2f]",xAxis.getValueForDisplay(mouseEvent.getX()),yAxis.getValueForDisplay(mouseEvent.getY()));	        			        	
	        		clickDataStr+=ckPnt;
	        		app_log.setText("Clicker Points :\n\t"+clickDataStr);
	        	}
	        }
	    });
	}
	private void drawChart(String lineName,Double xValues[],Double yValues[],int filled,Boolean whoCalled) {
		System.out.println("#drawChart#");
		try {									
			Series<Double, Double> series = new XYChart.Series<Double, Double>();			
			int i=0;
			for(i=0;i<filled;i++) {
				if(!whoCalled) {
					series.setName(lineName);
				}
				series.getData().add(new Data<Double,Double>(xValues[i],yValues[i]));
				LineTableController row;
				if(i==0)
					row = new LineTableController(lineName,xValues[i],yValues[i]);
				else
					row = new LineTableController("",xValues[i],yValues[i]);				
				table.getItems().add(row);										
			}
			Double[] xFin= new Double[i];
			Double[] yFin= new Double[i];
			for(i=0;i<filled;i++) {
				xFin[i] = xValues[i];
				yFin[i] = yValues[i];
			}
			lChart.getData().add(series);			
			if(whoCalled) {				
				series.setName(seriesLabel.getText());
				if(fileEnable.isSelected())
					callWriter(lineName, xFin, yFin);
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	private boolean inputValidator(String X) {
		System.out.println("#PatternValidator#");		
		for(int i=0;i< X.length();i++) {
			if(X.charAt(0) != '{' || X.charAt(X.length()-1) != ']') {
				System.out.println("valid 1");
				return false;
			}
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
	private void decodeAndDraw(String line,String X,Boolean whoCalled) {
		error_label.setText("");
		xVal.setText("");
		System.out.println("---------------\nxVal\tyVal\n---------------");
		int arraySize = (X.length()-((X.length())/5)*3)/2 + 5,xindexCounter=0,yindexCounter=0;
		Double[] xValues = new Double[arraySize];
		Double[] yValues = new Double[arraySize];									
		//Pattern reader
		for(int i=0;i< X.length();i++) {						
			if(X.charAt(i) == '[') {
				int j=0;
				String xCo = new String();
				for(j=i;X.charAt(j+1)!=',';j++) {					
					xCo+=X.charAt(j+1);
					System.out.print(X.charAt(j+1));
				}
				System.out.print("\t");
				xValues[xindexCounter]=Double.parseDouble(xCo);
				xindexCounter++;
			} else if(X.charAt(i)==',') {
				int j=0;
				String yCo = new String();
				for(j=i;X.charAt(j+1)!=']';j++) {					
					yCo+=X.charAt(j+1);
					System.out.print(X.charAt(j+1));
				}
				System.out.println();
				yValues[yindexCounter]=Double.parseDouble(yCo);
				yindexCounter++;								
			} else {}			
		}		
		System.out.println("---------------\nArraySize: "+arraySize+"\nStorage values :");		
		System.out.println("\tX: "+Arrays.toString(xValues)+"\n\tY: "+Arrays.toString(yValues));
		System.out.println("IndexCounters:\n\tX: "+xindexCounter+"\n\tY: "+yindexCounter);		
		drawChart(line,xValues,yValues,xindexCounter,whoCalled);		
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
	private void callWriter(String lineNames,Double[] xValues,Double[] yValues) {		
		lineFileSysController.writeDataInFile(yValues,xValues, lineNames,0);
	}
	public void loadDataFromFile() {
		if(!lChart.getData().isEmpty()) {
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
		ArrayList<String> seriesArr = lineFileSysController.readDataFromFile();						
		if(!seriesArr.isEmpty()) {			
			for(int seriIndex=0;seriIndex< seriesArr.size();seriIndex++) {
				String X = seriesArr.get(seriIndex);
				if(!inputValidator(X)) {
					error_label.setText("");
					error_label.setText("Entered pattern is not valid\nPlease try again!");
					System.out.println("Invalid Pattern");
				} else {
					String line = "";
					if(X.charAt(0) == '{') {						
						for(int j=0; X.charAt(j+1) != '}'; j++) {
							line += X.charAt(j+1);
						}						
					}
					decodeAndDraw(line,X,false);
				}
			}
		}else {
			error_label.setText("File is empty!\nPlease add data first");
		}
	}	
	public void loadDataInTable() {					        
		ArrayList<String> data = lineFileSysController.readDataFromFile();
		table.setEditable(true);
		c1.setCellValueFactory(new PropertyValueFactory<>("series"));		
		c2.setCellValueFactory(new PropertyValueFactory<>("seriesX"));
		c3.setCellValueFactory(new PropertyValueFactory<>("seriesY"));
		c1.setSortable(false);
		c2.setSortable(false);
		c3.setSortable(false);
				
		for(int newSeri=0;newSeri<data.size();newSeri++) {
			String X = data.get(newSeri);
			int arraySize = (X.length()-((X.length())/5)*3)/2 + 5,xindexCounter=0,yindexCounter=0;
			Double[] xValues = new Double[arraySize];
			Double[] yValues = new Double[arraySize];
			
			String[] lineNames =new String[arraySize];
			//Pattern decoder
			for(int i=0;i< X.length();i++) {
				if(X.charAt(i) == '{') {
					String line = "";
					for(int j=0; X.charAt(j+1) != '}'; j++) {
						line += X.charAt(j+1);
					}
					lineNames[i] = line;					
				}			
				if(X.charAt(i) == '[') {
					int j=0;
					String xCo = new String();
					for(j=i;X.charAt(j+1)!=',';j++) {					
						xCo+=X.charAt(j+1);					
					}
					xValues[xindexCounter]=Double.parseDouble(xCo);
					xindexCounter++;
				} else if(X.charAt(i)==',') {
					int j=0;
					String yCo = new String();
					for(j=i;X.charAt(j+1)!=']';j++) {					
						yCo+=X.charAt(j+1);					
					}					
					yValues[yindexCounter]=Double.parseDouble(yCo);
					yindexCounter++;								
				} else {}
			}
			for(int i=0;i<xindexCounter;i++) {
				//String value1 = Double.parseDouble(xValues[i]);
				//String value2 = Integer.toString(yValues[i]);				
				LineTableController row = new LineTableController(lineNames[i],xValues[i],yValues[i]);
				table.getItems().add(row);			
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
	//Editing------------------------------------------------------------	
    private void openPdfTextDialog(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LineChart/PdfTextDialog.fxml"));
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
	public void editFromTable() {
		System.out.println("#editDataFromTable");		
		Boolean isDvalid = true;
		int i=0;
		ArrayList<String> nmArr = new ArrayList<String>();
		ArrayList<Double> bufx = new ArrayList<Double>();
		ArrayList<Double> bufy = new ArrayList<Double>();
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
			for(i=0;i<nmArr.size();i++) {
				System.out.println("\tisDvalid\n\t\tnmArr: "+nmArr.get(i)+"\n\t\tSeletr: "+selector);
				Double[] xArr = new Double[bkCnt.get(i)];
				Double[] yArr = new Double[bkCnt.get(i)];
				for(int j=0;j<bkCnt.get(i);j++) {
					xArr[j] = bufx.get(selector+j);
					yArr[j] = bufy.get(selector+j);
					System.out.println("\t<"+xArr[j]+"><"+yArr[j]+">");
				}
				selector+=bkCnt.get(i);				
				drawChart(nmArr.get(i),xArr,yArr,bkCnt.get(i),false);
			}
		}else
			error_label.setText("Value of "+table.getItems().get(i).getSeries()+" must be a number");
	}
	//Exports--------------------------------------------
	public void pdfExtract() {
		openPdfTextDialog();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"));
		File file = fileChooser.showSaveDialog(primaryStage);
	      
        if (file != null) {
			WritableImage nodeshot = lChart.snapshot(new SnapshotParameters(), null);
	        File imgfile = new File("dat/imgs/chart.png");
	
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
			    Image img = Image.getInstance("dat/imgs/chart.png");			    
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
			}
		}
    }
	public void pngExtract() {
		  FileChooser fileChooser = new FileChooser();
	      fileChooser.setTitle("Save");
	      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"));
	      File file = fileChooser.showSaveDialog(primaryStage);
	      
          if (file != null) {		    
			WritableImage nodeshot = lChart.snapshot(new SnapshotParameters(), null);
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
        final ObservableList<LineTableController> obdata = FXCollections.observableArrayList();
        ArrayList<String> data = lineFileSysController.readDataFromFile();						
        for(int newSeri=0;newSeri<data.size();newSeri++) {
			String X = data.get(newSeri);
			int arraySize = (X.length()-((X.length())/5)*3)/2 + 5,xindexCounter=0,yindexCounter=0;
			Double[] xValues = new Double[arraySize];
			Double[] yValues = new Double[arraySize];
			
			String[] lineNames =new String[arraySize];
			//Pattern decoder
			for(int i=0;i< X.length();i++) {
				if(X.charAt(i) == '{') {
					String line = "";
					for(int j=0; X.charAt(j+1) != '}'; j++) {
						line += X.charAt(j+1);
					}
					lineNames[i] = line;					
				}			
				if(X.charAt(i) == '[') {
					int j=0;
					String xCo = new String();
					for(j=i;X.charAt(j+1)!=',';j++) {					
						xCo+=X.charAt(j+1);					
					}
					xValues[xindexCounter]=Double.parseDouble(xCo);
					xindexCounter++;
				} else if(X.charAt(i)==',') {
					int j=0;
					String yCo = new String();
					for(j=i;X.charAt(j+1)!=']';j++) {					
						yCo+=X.charAt(j+1);					
					}					
					yValues[yindexCounter]=Double.parseDouble(yCo);
					yindexCounter++;								
				} else {}
			}								
			for(int i=0;i<xindexCounter;i++) {
				Double locX = xValues[i];
				Double locY = yValues[i];
				obdata.add(new LineTableController(lineNames[i],locX,locY));							
			}					
		}        
        Writer writer = null;
        try {        	
        	file = new File(file.getAbsolutePath()+".csv");
            writer = new BufferedWriter(new FileWriter(file));
            for (LineTableController person : obdata) {
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
        	loadDataInTable();
        	file = new File(file.getAbsolutePath()+".pdf");
        	Document report= new Document();
            PdfWriter.getInstance(report, new FileOutputStream(file));
            report.open();            
            //we have four columns in our table
            PdfPTable report_table = new PdfPTable(3);

            for(int i=0;i<table.getItems().size();i++) {
            	report_table.addCell(getCell(((LineTableController) table.getItems().get(i)).getSeries(),PdfPCell.ALIGN_CENTER ));
            	report_table.addCell(getCell(((LineTableController) table.getItems().get(i)).getSeriesY().toString(),PdfPCell.ALIGN_CENTER ));            	
            	report_table.addCell(getCell(((LineTableController) table.getItems().get(i)).getSeriesX().toString(),PdfPCell.ALIGN_CENTER ));
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
	//Open new chart--------------------------------------------------------------------
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
			System.out.println("Opening Pie Chart closed bar chart");
		} catch (IOException e) {e.printStackTrace();}
	}
	public void openLineKit() {
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = (Stage) anchor.getScene().getWindow();
			openNew.setStage(thisstage,"Line chart");
			System.out.println("Opening Pie Chart closed bar chart");
		} catch (IOException e) {e.printStackTrace();}
	}
	public void openStackKit() {
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = (Stage) anchor.getScene().getWindow();
			openNew.setStage(thisstage,"Stacked chart");
			System.out.println("Opening Pie Chart closed bar chart");
		} catch (IOException e) {e.printStackTrace();}
	}
}
