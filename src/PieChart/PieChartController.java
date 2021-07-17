package PieChart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import BarChart.BarTableController;
import FileSys.pieFileSysController;
import LineChart.LineTableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

public class PieChartController {
	@FXML private MenuBar menuBar;
	@FXML private PieChart pChart;
	@FXML private Label error_label;	
	@FXML private Label pieValue;
	@FXML private Label l3;
	@FXML private Label l2;
	@FXML private Label l1;
	@FXML private TextField pieChartTitle;	
	@FXML private TextField nm;
	@FXML private TextField val;
	@FXML private TextArea bulk;
	@FXML private TableView<PieTableController> table;
	@FXML private TableColumn<PieTableController, String> c1;
	@FXML private TableColumn<PieTableController, Double> c2;
	@FXML private CheckBox bulkEnable;
	@FXML private CheckBox dbEnable;
	@FXML private CheckBox fileEnable;
	@FXML private Button loadDataFromFile;
	@FXML private Button loadDataFromDb;
	@FXML private Button loadDataInTable;
	@FXML private Button add_data;
	@FXML private Button add_b_data;
	@FXML private AnchorPane anchor;
	@FXML private DatePicker setDate;
	@FXML private ToggleButton themeToggle;
	@FXML private Circle theameCircle;
	final Stage primaryStage = null;
	Stage stage = null;
	private boolean theame=true;
	private boolean isFullScr=false;
	private ObservableList<PieChart.Data> pie_data;
	private ArrayList<String> nmArr = new ArrayList<String>();
	private ArrayList<Double> valArr = new ArrayList<Double>();
	final double SCALE_DELTA = 1.1;
	final KeyCombination altEnter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
    final KeyCombination altj= new KeyCodeCombination(KeyCode.J, KeyCombination.ALT_DOWN);
    final KeyCombination altk= new KeyCodeCombination(KeyCode.K, KeyCombination.ALT_DOWN);
    final KeyCombination altl= new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN);
    final KeyCombination ctrlb= new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlf= new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrld= new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlPrintPDF = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlPrintPNG = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlPrintTPDF = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);
    final KeyCombination ctrlQ = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    final KeyCombination altT= new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN);
    final KeyCombination altF11= new KeyCodeCombination(KeyCode.F11, KeyCombination.ALT_DOWN);
    
    @FXML public void initialize() {
        System.out.println("#initialized#");
        setDate.setConverter(new StringConverter<LocalDate>(){
            private DateTimeFormatter classic=DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
		c1.setCellValueFactory(new PropertyValueFactory<>("s"));		
		c2.setCellValueFactory(new PropertyValueFactory<>("d"));		
		c1.setSortable(false);
		c2.setSortable(false);
		table.getSelectionModel().cellSelectionEnabledProperty().set(true);
		c1.setCellFactory(TextFieldTableCell.forTableColumn());
		c2.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	    c1.setOnEditCommit(new EventHandler<CellEditEvent<PieTableController, String>>() {
	         public void handle(CellEditEvent<PieTableController, String> t) {
	               ((PieTableController)t.getTableView().getItems().get(t.getTablePosition().getRow())).setS(t.getNewValue().toString());		                    	
	         }
	    });
	    c2.setOnEditCommit(new EventHandler<CellEditEvent<PieTableController, Double>>() {
	         public void handle(CellEditEvent<PieTableController, Double> t) {
	        	 if(dblChecker(t.getNewValue().toString()))
	        		 ((PieTableController)t.getTableView().getItems().get(t.getTablePosition().getRow())).setD(t.getNewValue().toString());
	        	 else
	        		 error_label.setText("Please enter numeric value here");
	         }
	    });
		setDate.setValue(LocalDate.now());		
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
		else if (ctrlb.match(event)) {
			if(bulkEnable.isSelected()) {
				bulkEnable.setSelected(false);
				bulk.requestFocus();
			}
			else
				bulkEnable.setSelected(true);
			event.consume();
			buttonEnabler();
		}
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
		error_label.setText("Chart Data Cleared! Click load data to reload");				
	}
	public void clearFile() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");
		alert.setHeaderText("Clearing PieFile will permanently delete your data!");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			pieFileSysController.clearFile("pieFileData.txt");	
			error_label.setText("File is Cleared");
		}		
	}
	public void clearTable() {
		error_label.setText("PieTable it cleared! To reload click on load button");
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
			error_label.setText("Database entries are been Cleared");
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
	public void removeLast() {
		int index = nmArr.size() - 1;
		nmArr.remove(index);
		valArr.remove(index);		
		callWriter();
		loadDataFromFile();
	}
	public void refresh() {
		editFromTable();
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
		if(bulkEnable.isSelected()){
			nm.setDisable(true);
			val.setDisable(true);
			add_data.setDisable(true);
			bulk.setDisable(false);
			add_b_data.setDisable(false);
		}else {
			bulk.setDisable(true);
			add_b_data.setDisable(true);
			nm.setDisable(false);
			val.setDisable(false);
			add_data.setDisable(false);
		}
	}
	public void changeTheme() {
		if(theame) {	//light			
			anchor.setStyle("-fx-background-color:  #666666");
			pChart.setStyle("-fx-border-color:  #d8d8d8");			
			Color c = Color.web("#d8d8d8");
			l1.setTextFill(c);
			l2.setTextFill(c);
			l3.setTextFill(c);
			pieValue.setTextFill(c);
			fileEnable.setTextFill(c);
			dbEnable.setTextFill(c);
			bulkEnable.setTextFill(c);			
			theameCircle.setFill(c);
			themeToggle.setText("Light");
			theame = false;
		}
		else {			//dark			
			anchor.setStyle("-fx-background-color:  #d8d8d8");
			pChart.setStyle("-fx-border-color:  #666666");			
			Color c = Color.web("#666666");
			l1.setTextFill(c);
			l2.setTextFill(c);
			l3.setTextFill(c);
			pieValue.setTextFill(c);
			fileEnable.setTextFill(c);
			dbEnable.setTextFill(c);
			bulkEnable.setTextFill(c);			
			theameCircle.setFill(c);
			themeToggle.setText("Dark");
			theame = true;
		}
	}	
	//Add data functions-----------------------------------------------
	public void addData() {				
		if(addValidator()) {
			error_label.setText("");
			nmArr.add(nm.getText());
			valArr.add(Double.parseDouble(val.getText()));
			drawChart(true);
			if(fileEnable.isSelected())
				callWriter();
		}		
	}
	public void addBulkData() {		
		if(bulkValidator()) {					
			//nmArr.clear();
			//valArr.clear();
			String bk = bulk.getText();
			for(int i=0;i<bk.length();i++) {
				String tit_1 = "";
				String tit_2 = "";
				if(bk.charAt(i)=='[') {
					for(int j=i;bk.charAt(j+1)!=',';j++) {
						tit_1 +=bk.charAt(j+1);
					}
					nmArr.add(tit_1);
				}
				if(bk.charAt(i)==',') {
					for(int j=i;bk.charAt(j+1)!=']';j++) {
						tit_2 += bk.charAt(j+1);
					}
					valArr.add(Double.parseDouble(tit_2));
				}								
			}			
			drawChart(true);
			if(fileEnable.isSelected())
				callWriter();
		}else {
			error_label.setText("Invalid input in bulk data enter. Please check again");
		}
	}
	//Draw & decoding Validations Functions-----------------------------------------------------------
	private void drawChart(Boolean whoCalled) {
		clearTable();
		pie_data = FXCollections.observableArrayList();
		System.out.println("Drawing\n\tString\tDouble\n\t-------------------");
		for(int i=0; i<nmArr.size();i++) {
			System.out.println("\t"+nmArr.get(i)+"\t"+valArr.get(i));
			pie_data.add(new PieChart.Data(nmArr.get(i),valArr.get(i)));
			PieTableController row = new PieTableController(nmArr.get(i),valArr.get(i));
			table.getItems().add(row);
		}
		System.out.println("\t-------------------");
		pChart.setData(pie_data);
		pieDetailPercentage();
	}
	private void pieDetailPercentage() {				        
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
	    try {Double.parseDouble(value);}
	    catch(NumberFormatException e) {
	    	error_label.setText("Value field must be a numeric");
	    	return false;
	    }
		return true;
	}
	public boolean bulkValidator() {
		String bk = bulk.getText();
		for(int i=0; i<bk.length();i++) {
			if(bk.charAt(i) == '[') {
				for(int j=i;bk.charAt(j+1) != ',';j++) {
					if(bk.charAt(j+1) == ']') {
						System.out.println("valid-1");
						return false;			
					}
				}
			}
			if(bk.charAt(i) == ',') {
				String dbl="";
				for(int j=i+1;bk.charAt(j+1) != ']';j++) {
					if(bk.charAt(j+1)==',') {										
						System.out.println("valid-2");
						return false;
					}
					if(bk.charAt(j+1)==']') {
						System.out.println("valid-3");
						return false;
					}
					dbl += bk.charAt(j);					
					try {						
						Double.parseDouble(dbl);
					}catch(NumberFormatException e) {System.out.println("valid-4");return false;}
				}
			}
		}
		return true;
	}
	private boolean dblChecker(String x) {
		try {Double.parseDouble(x);}
	    catch(NumberFormatException e) {	    	
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
		//loadDataFromFile();	
		for(int i=0;i<nmArr.size();i++) {
			PieTableController row = new PieTableController(nmArr.get(i),valArr.get(i));
			table.getItems().add(row);			
		}
	}	
	public void loadDataFromDb() {}
	//Editing--------------------------------------
	public void editFromTable() {
		System.out.println("#editDataFromTable");
		int i = 0;
		Boolean isDvalid = true;
		for(i=0;i<table.getItems().size();i++) {
			String checkY = table.getItems().get(i).getD().toString();
			if(!dblChecker(checkY)) {
				isDvalid = false;
				break;
			}
		}
		if(isDvalid) {
			nmArr.clear();
			valArr.clear();
			for(i=0;i<table.getItems().size();i++) {				
				String x = table.getItems().get(i).getS();
				Double y = table.getItems().get(i).getD();
				
				System.out.println("\t"+x+"\t"+y);
				nmArr.add(x);
				valArr.add(y);			
			}
			drawChart(true);
			if(fileEnable.isSelected())
				callWriter();
		}else
			error_label.setText("Value of "+table.getItems().get(i).getS()+" must be a number");
	}
	//Export functions------------------------------
	public void pdfExtract() {
		  FileChooser fileChooser = new FileChooser();
	      fileChooser.setTitle("Save");
	      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"));
	      File pdffile = fileChooser.showSaveDialog(primaryStage);
	      
	      if (pdffile != null) {
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
	            String text = "PieChart by Cancer";             
	            content.showText(text);        
	            content.endText();
	            content.close();
	            doc.addPage(page);
	            doc.save(pdffile.getPath()+".pdf");
	            doc.close();
	            System.out.println("DOC\n\tPdf Exported!");
	            error_label.setText("PDF exported");
	            file.delete();
	        } catch (IOException ex) {
	            Logger.getLogger(LineChart.class.getName()).log(Level.SEVERE, null, ex);
	        }
	      }	      
    }	
	public void pngExtract() {
		  FileChooser fileChooser = new FileChooser();
	      fileChooser.setTitle("Save");
	      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"));
	      File file = fileChooser.showSaveDialog(primaryStage);
	      
          if (file != null) {              	            
		        WritableImage nodeshot = pChart.snapshot(new SnapshotParameters(), null);
		        file = new File(file.getAbsolutePath()+".png");
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
					System.out.println("PNG\n\tImage exported");
					error_label.setText("PNG exported");
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
      
        if(file != null) {        	
        	final ObservableList<PieTableController> data = FXCollections.observableArrayList();
    		for(int i=0;i<nmArr.size();i++)
    			data.add(new PieTableController(nmArr.get(i),valArr.get(i)));
    		Writer writer = null;
            try {        	
            	file = new File(file.getAbsolutePath()+".csv");
                writer = new BufferedWriter(new FileWriter(file));
                for (PieTableController person : data) {
                    String text = person.getS() + "," + person.getD() +"\n";
                    writer.write(text);
                    System.out.println("Export CSV\n\t CSV exported");
                }
            } catch (Exception ex) {ex.printStackTrace();System.out.println("Export CSV\n\t CSV exported error");}
            finally {writer.flush();writer.close();}
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
            PdfPTable report_table = new PdfPTable(2);

            for(int i=0;i<table.getItems().size();i++) {
            	report_table.addCell(getCell(((PieTableController) table.getItems().get(i)).getS(),PdfPCell.ALIGN_CENTER ));
            	report_table.addCell(getCell(((PieTableController) table.getItems().get(i)).getD().toString(),PdfPCell.ALIGN_CENTER ));            	
            }
            report.add(report_table);
            report.close();
            System.out.println("Export\n\tPie Table exported!");
		}
    }
	private PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);        
        return cell;
    }
}
