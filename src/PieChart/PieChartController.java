package PieChart;

import java.io.File;
import java.io.IOException;
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
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import DbSys.DbController;
import FileSys.pieFileSysController;
import LineChart.LineTableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PieChartController {
	@FXML private PieChart pChart;
	@FXML private Label error_label;
	@FXML private Label table_error_label; 
	@FXML private Label pieValue;	
	@FXML private TextField pieChartTitle;	
	@FXML private TextField nm;
	@FXML private TextField val;
	@FXML private TableView table;	
	@FXML private CheckBox dbEnable;
	@FXML private CheckBox fileEnable;
	@FXML private Button loadDataFromFile;
	@FXML private Button loadDataFromDb;
	@FXML private Button loadDataInTable;
	@FXML private AnchorPane anchor;
	@FXML private Circle theameCircle;
	private boolean theame=true;
	private ObservableList<PieChart.Data> pie_data;
	private ArrayList<String> nmArr = new ArrayList<String>();
	private ArrayList<Double> valArr = new ArrayList<Double>();
	//final Label pieValue = new Label("");
	public void changeTheme() {
		if(theame) {	//light
			error_label.setTextFill(Color.web("red"));
			anchor.setStyle("-fx-background-color:  #666666");
			pChart.setStyle("-fx-border-color:  #d8d8d8");
			Color c = Color.web("#d8d8d8");
			theameCircle.setFill(c);
			theame = false;
		}
		else {		//dark
			error_label.setTextFill(Color.web("pink"));
			anchor.setStyle("-fx-background-color:  #d8d8d8");
			pChart.setStyle("-fx-border-color:  #666666");
			Color c = Color.web("#666666");
			theameCircle.setFill(c);
			theame = true;
		}
	}
	public void clearChart() {
		pChart.getData().clear();
		nmArr.clear();
		valArr.clear();
		error_label.setText("Chart Data Cleared!\nClick load data to reload");				
	}
	public void clearFile() {		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");
		alert.setHeaderText("Clearing PieFile will permanently delete your data!");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
				
			error_label.setText("File is Cleared\nData is permanently lost");
		}		
	}
	public void clearTable() {
		table_error_label.setText("PieTable it cleared!\n To reload click on load button");
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
			error_label.setText("PieDB is Cleared\nData is permanently lost");
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
		if(pieChartTitle.getText().isEmpty())
			pChart.setTitle("PieChart title");
		else
			pChart.setTitle(pieChartTitle.getText());
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
	}
	//Add Functions-----------------------------------------------------------
	public void addData() {				
		if(addValidator()) {
			error_label.setText("");
			nmArr.add(nm.getText());
			valArr.add(Double.parseDouble(val.getText()));
			drawChart(true);
			callWriter();
		}        	
	}
	//Draw & decoding Validations Functions-----------------------------------------------------------
	private void drawChart(Boolean whoCalled) {		
		pie_data = FXCollections.observableArrayList();
		System.out.println("Drawing\n\tString\tDouble\n\t-------------------");
		for(int i=0; i<nmArr.size();i++) {
			System.out.println("\t"+nmArr.get(i)+"\t"+valArr.get(i));
			pie_data.add(new PieChart.Data(nmArr.get(i),valArr.get(i)));
		}
		System.out.println("\t-------------------");
		pChart.setData(pie_data);
		pieDetailPercentage();
	}
	private void pieDetailPercentage() {		
		pieValue.setTextFill(Color.DARKORANGE);
        pieValue.setStyle("-fx-font: 14 arial;");
		for (final PieChart.Data data : pChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {                	
                    double total = 0;
                    for (PieChart.Data d : pChart.getData()) {
                        total += d.getPieValue();
                    }                    
                    String text = String.format("%.1f%%", 100*data.getPieValue()/total) ;
                    pieValue.setText(data.getName()+" : "+text);
                 }
            );
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,
            		e->{            			
            			pieValue.setText("");
            		}
            );
        }
	}
	private boolean addValidator() {
		String name = nm.getText();
		String value = val.getText();		
		if(name.isEmpty()){
			error_label.setText("Please enter name field");
			return false;
		}
		if(value.isEmpty()){
			error_label.setText("Please enter value field");
			return false;
		}
	    try {Double doub_val = Double.parseDouble(value);}
	    catch(NumberFormatException e) {
	    	error_label.setText("Value field must be a numeric");
	    	return false;
	    }
		return true;
	}
	//loading functions------------------------------------------------------
	private void callWriter() {		
		pieFileSysController.writeDataInFile(nmArr,valArr);
	}
	public void loadDataFromFile() {
		clearChart();
		ArrayList<String> read = pieFileSysController.readDataFromFile();
		for(int seriIndex=0;seriIndex< read.size();seriIndex++) {			
			String X = read.get(seriIndex);			
			for(int i=0;i< X.length();i++) {						
				if(X.charAt(i) == '[') {
					int j=0;
					String xCo = new String();
					for(j=i;X.charAt(j+1)!=',';j++) {					
						xCo+=X.charAt(j+1);
						System.out.print(X.charAt(j+1));
					}
					System.out.print("\t");
					nmArr.add(xCo);					
				} else if(X.charAt(i)==',') {
					int j=0;
					String yCo = new String();
					for(j=i;X.charAt(j+1)!=']';j++) {					
						yCo+=X.charAt(j+1);
						System.out.print(X.charAt(j+1));
					}
					System.out.println();
					valArr.add(Double.parseDouble(yCo));												
				} else {}			
			}			
		}	
		drawChart(false);
	}
	public void loadDataFromFileConfirmed() {
		clearChart();
		
	}	
	public void loadDataInTable() {					        
				
	}	
	public void loadDataFromDb() {
		
	}	
	public void pdfExtract() {
		TextInputDialog dialog = new TextInputDialog("Name of pdf file");
		dialog.setTitle("Save");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.setContentText("Name: ");
		 
		Optional<String> result = dialog.showAndWait();
		 
		result.ifPresent(name -> {
				
		});
    }	
	public void pngExtract() {
		TextInputDialog dialog = new TextInputDialog("Name of image file");
		dialog.setTitle("Save");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.setContentText("Name: ");
		 
		Optional<String> result = dialog.showAndWait();
		 
		result.ifPresent(name -> {		    
			
		});
	}
}
