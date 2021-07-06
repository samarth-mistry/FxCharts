package PieChart;

import Experiments.fileSystemDemo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class LineChartController {
	@FXML private LineChart<Integer, Integer> lChart;
	@FXML private Label error_label;
	@FXML private TextField xVal;
	@FXML private TextField lineChartTitle;
	@FXML private TextField seriesLabel;
	@FXML private TextField xxisLabel;
	@FXML private TextField yxisLabel;
	@FXML private NumberAxis xxis;
	@FXML private NumberAxis yxis;
	//@FXML private TableView table;
	@FXML private AnchorPane a_t;
	@FXML private TableColumn<String, String> column1;
	@FXML private TableColumn<String, String> column2;
	private TableView table = new TableView();
	public static final String Column1MapKey = "A";
    public static final String Column2MapKey = "B";   
	@SuppressWarnings("unchecked")	
	public void addData() {		
		System.out.println("#AddData#");
		String X = xVal.getText();
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
			decodeAndDraw(X,true);
		}
	}
	private void drawChart(int xValues[],int yValues[],int filled,Boolean whoCalled) {
		try {			
			@SuppressWarnings("rawtypes")			
			XYChart.Series series = new XYChart.Series();
			series.setName(seriesLabel.getText());			
			int i=0;
			for(i=0;i<filled;i++) {
				//System.out.println("("+xValues[i]+","+yValues[i]+")");				
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
				System.out.println(whoCalled);
				callWriter(xFin, yFin);
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	private boolean inputValidator(String X) {
		System.out.println("#PatternValidator#");		
		for(int i=0;i< X.length();i++) {			
			if(X.charAt(0) != '[' || X.charAt(X.length()-1) != ']') {
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
		ArrayList<String> seriesArr = fileSystemDemo.readDataFromFile();		
		Boolean isnull= false;
		try{if(seriesArr.get(0) == "") {}			
		}catch(IndexOutOfBoundsException e) {
			error_label.setText("File is empty!\nPlease add data first");
		}
		if(!isnull) {			
			for(int seriIndex=0;seriIndex< seriesArr.size();seriIndex++) {
				String X = seriesArr.get(seriIndex);
				if(!inputValidator(X)) {
					error_label.setText("");
					error_label.setText("Entered pattern is not valid\nPlease try again!");
					System.out.println("Invalid Pattern");
				} else {decodeAndDraw(X,false);}
			}
		}
	}
	public void decodeAndDraw(String X,Boolean whoCalled) {
		error_label.setText("");
		xVal.setText("");
		System.out.println("---------------\nxVal\tyVal\n---------------");
		int arraySize = (X.length()-((X.length())/5)*3)/2 + 5,xindexCounter=0,yindexCounter=0;
		int[] xValues = new int[arraySize];
		int[] yValues = new int[arraySize];							
		
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
		drawChart(xValues,yValues,xindexCounter,whoCalled);		
	}
	public void callWriter(int[] xValues,int[] yValues) {		
		fileSystemDemo.writeDataInFile(xValues, yValues, "locations.txt",0);
	}
	public void clearChart() {
		lChart.getData().clear();
		error_label.setText("Chart Data Cleared!\nClick load data to reload");
	}
	public void clearFile() {
		fileSystemDemo.clearFile("locations.txt");
		error_label.setText("File is Cleared\nData is permanently lost");
	}
	public void exit() {System.exit(0);}
	public void loadDataInTable() {			
		//ObservableList<String[]> data = FXCollections.observableArrayList();        
		ArrayList<String> data = fileSystemDemo.readDataFromFile();
		
		for(int newSeri=0;newSeri<data.size();newSeri++) {
			String X = data.get(newSeri);
			int arraySize = (X.length()-((X.length())/5)*3)/2 + 5,xindexCounter=0,yindexCounter=0;
			int[] xValues = new int[arraySize];
			int[] yValues = new int[arraySize];							
			
			//Pattern reader
			for(int i=0;i< X.length();i++) {			
				if(X.charAt(i) == '[') {
					int j=0;
					String xCo = new String();
					for(j=i;X.charAt(j+1)!=',';j++) {					
						xCo+=X.charAt(j+1);
						//System.out.print(X.charAt(j+1));
					}
					System.out.print("\t");
					xValues[xindexCounter]=Integer.parseInt(xCo);
					xindexCounter++;
				} else if(X.charAt(i)==',') {
					int j=0;
					String yCo = new String();
					for(j=i;X.charAt(j+1)!=']';j++) {					
						yCo+=X.charAt(j+1);
						//System.out.print(X.charAt(j+1));
					}
					System.out.println();
					yValues[yindexCounter]=Integer.parseInt(yCo);
					yindexCounter++;								
				} else {}
			}			
			table.setEditable(true);
			TableColumn c1 = new TableColumn("Name");
			c1.setCellValueFactory(new PropertyValueFactory<>("series"));
			
			TableColumn c2 = new TableColumn("X");
			c2.setCellValueFactory(new PropertyValueFactory<>("seriesX"));

			TableColumn c3 = new TableColumn("Y");
			c3.setCellValueFactory(new PropertyValueFactory<>("seriesY"));
			
			table.getColumns().addAll(c1, c2, c3);
			System.out.println("Xvales : table" +Arrays.toString(xValues));
			System.out.println("Yvales : table" +Arrays.toString(yValues));
			for(int i=0;i<xindexCounter;i++) {
				String value1 = Integer.toString(xValues[i]);
				String value2 = Integer.toString(yValues[i]);
				System.out.println("V1,V2 "+value1+","+value2);
				LineTableController person = new LineTableController(Integer.toString(i),value1,value2);
				table.getItems().add(person);
				
			}
//			column1.setCellValueFactory(c -> new SimpleStringProperty(new String("456")));
//			column2.setCellValueFactory(c -> new SimpleStringProperty(new String("456")));
		}		
		//table.getItems().addAll("Col1","Col2");
		a_t.getChildren().add(table);
	}
	public void load2() {
		TableColumn<Map, String> c1 = new TableColumn<>("Class A");
        TableColumn<Map, String> c2 = new TableColumn<>("Class B");
        TableColumn<Map, String> c3 = new TableColumn<>("Class C");
        
        c1.setCellValueFactory(new MapValueFactory(Column1MapKey));
        c1.setMinWidth(130);
        c2.setCellValueFactory(new MapValueFactory(Column2MapKey));
        c2.setMinWidth(130);
        c3.setCellValueFactory(new MapValueFactory(Column1MapKey));
        c1.setMinWidth(130);
 
        TableView table_view = new TableView<>(generateDataInMap());
 
        table_view.setEditable(true);
        table_view.getSelectionModel().setCellSelectionEnabled(true);
        table_view.getColumns().setAll(c1, c2, c3);
        Callback<TableColumn<Map, String>, TableCell<Map, String>>
        cellFactoryForMap = new Callback<TableColumn<Map, String>,
        TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new TextFieldTableCell<Object, Object>(new StringConverter<Object>() {
                    @Override
                    public String toString(Object t) {
                    	System.out.println("X: "+t.toString());
                        return t.toString();
                    }
                    @Override
                    public Object fromString(String string) {
                    	System.out.println("X: "+string);
                        return string;
                    }                                    
                });
            }
		};
        c1.setCellFactory(cellFactoryForMap);
        c2.setCellFactory(cellFactoryForMap);
        c3.setCellFactory(cellFactoryForMap);
        
        a_t.getChildren().add(table_view);
	}
	private ObservableList<Map> generateDataInMap() {
        int max = 10;
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (int i = 1; i < max; i++) {
            Map<String, String> dataRow = new HashMap<>();
 
            String value1 = "A" + i;
            String value2 = "B" + i;
            dataRow.put(Column1MapKey, Integer.toString(i));            
            dataRow.put(Column1MapKey, value1);
            dataRow.put(Column2MapKey, value2);
 
            allData.add(dataRow);
        }
        return allData;
    }
}
