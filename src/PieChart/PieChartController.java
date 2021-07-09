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
import javafx.scene.chart.LineChart;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PieChartController {
	@FXML private PieChart pChart;
	@FXML private Label error_label;
	@FXML private Label table_error_label; 
	@FXML private Label pieValue;
	@FXML private Label l3;
	@FXML private Label l2;
	@FXML private Label l1;
	@FXML private TextField pieChartTitle;	
	@FXML private TextField nm;
	@FXML private TextField val;
	@FXML private TextField bulk;
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
	final double SCALE_DELTA = 1.1;
	//Zooming-------------------------------------------------
	public void zoomPieChart(ScrollEvent event) {
		 double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
        pChart.setScaleX(pChart.getScaleX() * scaleFactor);
        pChart.setScaleY(pChart.getScaleY() * scaleFactor);
	}
	public void zoomNormal(MouseEvent e) {				    
	    if (e.getClickCount() == 2) {
	        pChart.setScaleX(1.0);
	        pChart.setScaleY(1.0);
	    }		    
	}
	//Clearing functions----------------------------------------------
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
			pieFileSysController.clearFile("pieFileData.txt");	
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
			//DbController.clearSeries();
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
	public void removeLast() {
		int index = nmArr.size() - 1;
		nmArr.remove(index);
		valArr.remove(index);
		callWriter();
		loadDataFromFile();
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
	public void changeTheme() {
		if(theame) {	//light
			error_label.setTextFill(Color.web("red"));
			anchor.setStyle("-fx-background-color:  #666666");
			pChart.setStyle("-fx-border-color:  #d8d8d8");
			pieValue.setStyle("-fx-border-color:  #d8d8d8");
			Color c = Color.web("#d8d8d8");
			l1.setTextFill(c);
			l2.setTextFill(c);
			l3.setTextFill(c);
			fileEnable.setTextFill(c);
			dbEnable.setTextFill(c);
			theameCircle.setFill(c);
			theame = false;
		}
		else {		//dark
			error_label.setTextFill(Color.web("pink"));
			anchor.setStyle("-fx-background-color:  #d8d8d8");
			pChart.setStyle("-fx-border-color:  #666666");
			pieValue.setStyle("-fx-border-color:  #666666");
			Color c = Color.web("#666666");
			l1.setTextFill(c);
			l2.setTextFill(c);
			l3.setTextFill(c);
			fileEnable.setTextFill(c);
			dbEnable.setTextFill(c);
			theameCircle.setFill(c);
			theame = true;
		}
	}	
	//Add Functions-----------------------------------------------------------
	//Add data functions-----------------------------------------------
	public void addData() {				
		if(addValidator()) {
			error_label.setText("");
			nmArr.add(nm.getText());
			valArr.add(Double.parseDouble(val.getText()));
			drawChart(true);
			callWriter();
		}        	
	}
	public void addBulkData() {
		if(inputValidator(bulk.getText())) {
			System.out.println("Valid");
			String bk = bulk.getText();
			for(int i=0;i<bk.length();i++) {
				String tit_1 = "";
				String tit_2 = "";
				if(bk.charAt(i)=='[') {
					for(int j=i;bk.charAt(j+1)!=',';j++) {
						tit_1 +=bk.charAt(j+1);
					}
				}
				if(bk.charAt(i)==',') {
					for(int j=i;bk.charAt(j+1)!=']';j++) {
						tit_2 += bk.charAt(j+1);
					}
				}
				System.out.println("tit_1: "+tit_1+"tit_2: "+tit_2);
			}
		}
	}
	private boolean inputValidator(String X) {
		System.out.println("#PatternValidator#");		
		for(int i=0;i< X.length();i++) {								
			if(X.charAt(i) == '[') {
				int j=0,y=0;
				if(!Character.isAlphabetic(X.charAt(i+1))){					
					//return false;
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
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");
		alert.setHeaderText("Clearing PieFile will permanently delete your data!");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			loadDataFromFileConfirmed();			
		}		
	}
	public void loadDataFromFileConfirmed() {
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
	public void loadDataInTable() {					        
				
	}	
	public void loadDataFromDb() {
		
	}	
	//Export functions------------------------------
	public void pdfExtract() {
		TextInputDialog dialog = new TextInputDialog("Name of pdf file");
		dialog.setTitle("Save");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.setContentText("Name: ");
		 
		Optional<String> result = dialog.showAndWait();
		 
		result.ifPresent(name -> {
			WritableImage nodeshot = pChart.snapshot(new SnapshotParameters(), null);
	        File file = new File("dat/imgs/chart.png");
	
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
	            pdimage = PDImageXObject.createFromFile("dat/imgs/chart.png",doc);
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
	            doc.save("dat/pdfs/"+name+".pdf");
	            doc.close();
	            System.out.println("DOC\n\tPdf Exported!");
	            error_label.setText("PDF exported");
	            file.delete();
	        } catch (IOException ex) {
	            Logger.getLogger(LineChart.class.getName()).log(Level.SEVERE, null, ex);
	        }
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
			WritableImage nodeshot = pChart.snapshot(new SnapshotParameters(), null);
	        File file = new File("dat/imgs/"+name+".png");
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
				System.out.println("PNG\n\tImage exported");
				error_label.setText("PNG exported");
			} catch (IOException e) {
				System.out.println("Error in making image!");
			}
		});
	}
}
