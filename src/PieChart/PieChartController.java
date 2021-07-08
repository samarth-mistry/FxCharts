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
import FileSys.fileSystemController;
import LineChart.LineTableController;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
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

public class PieChartController {
	@FXML private PieChart pChart;
	@FXML private Label error_label;
	@FXML private Label table_error_label;	
	@FXML private TextField pieChartTitle;	
	@FXML private TextField nm;
	@FXML private TextField val;
	@FXML private TableView table;	
	@FXML private CheckBox dbEnable;
	@FXML private CheckBox fileEnable;
	@FXML private Button loadDataFromFile;
	@FXML private Button loadDataFromDb;
	@FXML private Button loadDataInTable;

	public void clearChart() {
		
		error_label.setText("Chart Data Cleared!\nClick load data to reload");				
	}
	public void clearFile() {		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");
		alert.setHeaderText("Clearing File will permanently delete your data!");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
				
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
		
	}
	//Draw & decoding Validations Functions-----------------------------------------------------------
	private void drawChart(String lineName,int xValues[],int yValues[],int filled,Boolean whoCalled) {
		
	}
	private boolean inputValidator(String X) {
		return true;
	}
	private void decodeAndDraw(String line,String X,Boolean whoCalled) {
				
	}
	//loading functions------------------------------------------------------
	private void callWriter(String lineNames,int[] xValues,int[] yValues) {		
		fileSystemController.writeDataInFile(yValues,xValues, lineNames,0);
	}
	public void loadDataFromFile() {
		
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
