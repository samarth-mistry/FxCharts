package LineChart;

import FileSys.fileSystemDemo;
import DbSys.DbController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class LineChartController {
	@FXML private LineChart<Integer, Integer> lChart;
	@FXML private Label error_label;
	@FXML private Label table_error_label;
	@FXML private TextField xVal;
	@FXML private TextField lineChartTitle;
	@FXML private TextField seriesLabel;
	@FXML private TextField xxisLabel;
	@FXML private TextField yxisLabel;
	@FXML private NumberAxis xxis;
	@FXML private NumberAxis yxis;
	@FXML private TableView table;
	@FXML private TableColumn<String, String> c1;
	@FXML private TableColumn<String, String> c2;
	@FXML private TableColumn<String, String> c3;	
	
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
			try {if(!DbController.insertSeries(seriesLabel.getText(),1,X)) {error_label.setText("Error in Database");}} 
			catch (SQLException e) {e.printStackTrace();}
			//decodeAndDraw(seriesLabel.getText(),X,true);
		}
	}
	private void drawChart(String lineName,int xValues[],int yValues[],int filled,Boolean whoCalled) {
		try {			
			@SuppressWarnings("rawtypes")			
			XYChart.Series series = new XYChart.Series();						
			int i=0;
			for(i=0;i<filled;i++) {
				//System.out.println("("+xValues[i]+","+yValues[i]+")");
				if(!whoCalled) {
					series.setName(lineName);
				}
				series.getData().add(new XYChart.Data(xValues[i],yValues[i]));
			}
			int[] xFin= new int[i];
			int[] yFin= new int[i];
			for(i=0;i<filled;i++) {
				xFin[i] = xValues[i];
				yFin[i] = yValues[i];
			}
			lChart.getData().add(series);			
			if(whoCalled) {
				System.out.println("who Called :"+ whoCalled);
				series.setName(seriesLabel.getText());
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
				int j=0,y=0;
				if(!Character.isDigit(X.charAt(i+1))){					
					return false;
				}
				if(X.charAt(i+1) != ','){
					for(j=i;Character.isDigit(X.charAt(j+1));j++) {}
					//System.out.println("valid 2.1: "+j);
					if(X.charAt(j+1) !=',') {
						return false;
					}
					if(X.charAt(j+2) != ']'){
						for(y=j+2;Character.isDigit(X.charAt(y+1));y++) {}											
					}
				}
				if(X.charAt(j+1) != ','){									
					return false;
				}else {j=0;}				
				if(X.charAt(y+1) != ']'){
					return false;
				}
			}			
		}
		return true;
	}
	public void axisRenamed() {
		lChart.setTitle(lineChartTitle.getText());				
		xxis.setLabel(xxisLabel.getText());
		yxis.setLabel(yxisLabel.getText());
		if(lineChartTitle.getText() == "")
			lChart.setTitle("Line Chart");
		if(xxisLabel.getText() == "")
			xxis.setLabel("X->");
		if(yxisLabel.getText() == "")
			yxis.setLabel("X->");		
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
		ArrayList<String> seriesArr = fileSystemDemo.readDataFromFile();						
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
	public void decodeAndDraw(String line,String X,Boolean whoCalled) {
		error_label.setText("");
		xVal.setText("");
		System.out.println("---------------\nxVal\tyVal\n---------------");
		int arraySize = (X.length()-((X.length())/5)*3)/2 + 5,xindexCounter=0,yindexCounter=0;
		int[] xValues = new int[arraySize];
		int[] yValues = new int[arraySize];							
		//String[] lineNames =new String[arraySize];
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
				xValues[xindexCounter]=Integer.parseInt(xCo);
				xindexCounter++;
			} else if(X.charAt(i)==',') {
				int j=0;
				String yCo = new String();
				for(j=i;X.charAt(j+1)!=']';j++) {					
					yCo+=X.charAt(j+1);
					System.out.print(X.charAt(j+1));
				}
				System.out.println();
				yValues[yindexCounter]=Integer.parseInt(yCo);
				yindexCounter++;								
			} else {}			
		}		
		System.out.println("---------------\nArraySize: "+arraySize+"\nStorage values :");		
		System.out.println("\tX: "+Arrays.toString(xValues)+"\n\tY: "+Arrays.toString(yValues));
		System.out.println("IndexCounters:\n\tX: "+xindexCounter+"\n\tY: "+yindexCounter);		
		drawChart(line,xValues,yValues,xindexCounter,whoCalled);		
	}
	public void callWriter(String lineNames,int[] xValues,int[] yValues) {		
		fileSystemDemo.writeDataInFile(yValues,xValues, lineNames,0);
	}
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
			fileSystemDemo.clearFile("locations.txt");
			error_label.setText("File is Cleared\nData is permanently lost");
		}		
	}
	public void loadDataInTable() {					        
		ArrayList<String> data = fileSystemDemo.readDataFromFile();
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
			int[] xValues = new int[arraySize];
			int[] yValues = new int[arraySize];
			
			String[] lineNames =new String[arraySize];
			//Pattern decoder
			for(int i=0;i< X.length();i++) {
				if(X.charAt(i) == '{') {
					String line = "";
					for(int j=0; X.charAt(j+1) != '}'; j++) {
						line += X.charAt(j+1);
					}
					lineNames[i] = line;
					//System.out.println("Line Name :"+lineNames[i]);
				}			
				if(X.charAt(i) == '[') {
					int j=0;
					String xCo = new String();
					for(j=i;X.charAt(j+1)!=',';j++) {					
						xCo+=X.charAt(j+1);
						//System.out.print(X.charAt(j+1));
					}
					//System.out.print("\t");
					xValues[xindexCounter]=Integer.parseInt(xCo);
					xindexCounter++;
				} else if(X.charAt(i)==',') {
					int j=0;
					String yCo = new String();
					for(j=i;X.charAt(j+1)!=']';j++) {					
						yCo+=X.charAt(j+1);
						//System.out.print(X.charAt(j+1));
					}
					//System.out.println();
					yValues[yindexCounter]=Integer.parseInt(yCo);
					yindexCounter++;								
				} else {}
			}			
			System.out.println("Xvals(table): "+ Arrays.toString(xValues));
			System.out.println("Yvals(table): "+ Arrays.toString(xValues));					
			for(int i=0;i<xindexCounter;i++) {
				String value1 = Integer.toString(xValues[i]);
				String value2 = Integer.toString(yValues[i]);
				System.out.println("V1,V2 "+value1+","+value2);
				LineTableController person = new LineTableController(lineNames[i],value1,value2);
				table.getItems().add(person);			
			}
		}		
		//table.getItems().addAll("Col1","Col2");
		//a_t.getChildren().add(table);
	}	
	public void clearTable() {
		table_error_label.setText("Table it cleared!\n To reload click on load button");
		table.getItems().clear();
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
	public void loadDataFromDb() {
		String X="";
		try {
			X = DbController.getSeries();
		} catch (SQLException e) {
			error_label.setText("Error fetching data from DB");
			e.printStackTrace();
		}
		if(X.isEmpty()) {
			error_label.setText("DB is empty!\nPlease add data first");
		}else {
			for(int seriIndex=0;seriIndex< X.length() ;seriIndex++) {
				String line = "";
				if(X.charAt(0) == '{') {						
					for(int j=0; X.charAt(j+1) != '}'; j++) {
						line += X.charAt(j+1);
					}						
				}
				decodeAndDraw(line,X,false);
			}
		}
	}
	public void clearDb() {
		DbController.clearSeries();
	}
}
