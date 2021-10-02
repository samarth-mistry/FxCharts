package PieChart;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import FileSys.PTFileManager;
import Main.FxChartMainPage;
import XMLController.PieChartXml.PXFileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

@SuppressWarnings("deprecation")
public class PieChartController {
	@FXML private MenuBar menuBar;
	@FXML private PieChart pChart;
	@FXML private Label error_label,pieValue;
	@FXML private TextField pieChartTitle,nm,val;	
	@FXML private TextArea bulk;
	@FXML private TableView<PieTableModal> table;
	@FXML private TableColumn<PieTableModal, String> c1;
	@FXML private TableColumn<PieTableModal, Double> c2;
	@FXML private CheckBox bulkEnable;
	@FXML private Button load,add_data,add_b_data;
	@FXML private AnchorPane anchor;
	@FXML private DatePicker setDate;
	@FXML private ToggleButton themeToggle;
	@FXML private Circle theameCircle;
	
	static String pdfTextApp = null;
	private static String FILE_PATH = "";
	private DateTimeFormatter classic=DateTimeFormatter.ofPattern("dd/MM/yyyy");
	final Stage primaryStage = null;
	Stage stage = null;
	private boolean theame=true,isFullScr=false;
	private ObservableList<PieChart.Data> pie_data;
	private ArrayList<PieChart.Data> pieList = new ArrayList<PieChart.Data>();
	private Stack<ArrayList<PieChart.Data>> priList = new Stack<ArrayList<PieChart.Data>>(),secList = new Stack<ArrayList<PieChart.Data>>();
	
	final double SCALE_DELTA = 1.1;
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
    ctrlPrintPNG = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN),
    ctrlPrintTPDF = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN),
    ctrlQ = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
    altT= new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN),
    altF11= new KeyCodeCombination(KeyCode.F11, KeyCombination.ALT_DOWN);
	
    //ShortCut functions----------------------------------------------------
    @FXML public void initialize() {
    	pieDetailPercentage();
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
		c1.setCellValueFactory(new PropertyValueFactory<>("s"));		
		c2.setCellValueFactory(new PropertyValueFactory<>("d"));		
		c1.setSortable(false);
		c2.setSortable(false);
		table.getSelectionModel().cellSelectionEnabledProperty().set(true);
		c1.setCellFactory(TextFieldTableCell.forTableColumn());
		c2.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	    c1.setOnEditCommit(new EventHandler<CellEditEvent<PieTableModal, String>>() {
	         public void handle(CellEditEvent<PieTableModal, String> t) {
	               ((PieTableModal)t.getTableView().getItems().get(t.getTablePosition().getRow())).setS(t.getNewValue().toString());
	               editFromTable();
	         }
	    });
	    c2.setOnEditCommit(new EventHandler<CellEditEvent<PieTableModal, Double>>() {
	         public void handle(CellEditEvent<PieTableModal, Double> t) {
	        	 if(dblChecker(t.getNewValue().toString())) {
	        		 ((PieTableModal)t.getTableView().getItems().get(t.getTablePosition().getRow())).setD(t.getNewValue().toString());
	        	 	editFromTable();
	         	 }
	        	 else
	        		 error_label.setText("Please enter numeric value here");
	         }
	    });
		
		table.setOnKeyPressed(event -> {
	        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.TAB) {
	            table.getSelectionModel().selectRightCell();
	            event.consume();
	        } else if (event.getCode() == KeyCode.LEFT) {
	            table.getSelectionModel().selectLeftCell();
	            event.consume();
	        }else if (event.getCode() == KeyCode.UP) {
	            table.getSelectionModel().selectAboveCell();
	            event.consume();
	        } else if (event.getCode() == KeyCode.DOWN) {
	            table.getSelectionModel().selectBelowCell();
	            event.consume();
	        }
		});
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
		else if (ctrls.match(event)) {save();event.consume();}
		else if (ctrlo.match(event)) {load();event.consume();}
		else if (ctrlz.match(event)) {undo();event.consume();}
		else if (ctrlshiftz.match(event)) {redo();event.consume();}
		else if (ctrlshifts.match(event)) {saveas();event.consume();}
		else if (ctrlshiftc.match(event)) {copyImgToClipBoard();event.consume();}
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

			event.consume();
			buttonEnabler();
		}
		else if (ctrld.match(event)) {
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
		//nm.requestFocus();
	}
	//Zooming---------------------------------------------------------------------
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
		error_label.setText("");
		pChart.getData().clear();
		pieList.clear();
		table.getItems().clear();
		//error_label.setText("Chart Data Cleared! Click load data to reload");				
	}
	public void clearFile() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ALERT");
		alert.setHeaderText("Clearing PieFile will permanently delete your data!");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			PTFileManager.clearFile(FILE_PATH);	
			error_label.setText("File is Cleared");
		}		
	}
	public void clearTable() {
		//error_label.setText("PieTable it cleared! To reload click on load button");
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
		
	}
	public void refresh(ActionEvent event) {
		//editFromTable();
		drawChart(true);
	}
	//Configuration functions Functions-----------------------------------------------------------
	public void checkAnimated() {
		if(pChart.getAnimated())
			pChart.setAnimated(false);
		else
			pChart.setAnimated(true);
	}
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
			pieValue.setTextFill(c);
			bulkEnable.setTextFill(c);			
			theameCircle.setFill(c);
			themeToggle.setText("Light");
			theame = false;
		}
		else {			//dark			
			anchor.setStyle("-fx-background-color:  #d8d8d8");
			pChart.setStyle("-fx-border-color:  #666666");			
			Color c = Color.web("#666666");			
			pieValue.setTextFill(c);
			bulkEnable.setTextFill(c);			
			theameCircle.setFill(c);
			themeToggle.setText("Dark");
			theame = true;
		}
	}
	private void jbp() {Toolkit.getDefaultToolkit().beep();}
	//Add data functions-----------------------------------------------
	public void addData() {
		if(addValidator()) {
			error_label.setText("");
			pieList.add(new PieChart.Data(nm.getText(),Double.parseDouble(val.getText())));
			priList.push(new ArrayList<PieChart.Data>(cloneList(pieList)));
			drawChart(true);
		}		
	}
	public void addBulkData() {		
		if(bulkValidator()) {
			String bk = bulk.getText();
			String tit_1 = null,tit_2 = null;
			Boolean a = false, b = false;
			for(int i=0;i<bk.length();i++) {
				if(bk.charAt(i)=='[') {
					tit_1 = "";
					for(int j=i;bk.charAt(j+1)!=',';j++)
						tit_1 +=bk.charAt(j+1);
					a=true;
				}
				if(bk.charAt(i)==',') {
					tit_2 = "";
					for(int j=i;bk.charAt(j+1)!=']';j++)
						tit_2 += bk.charAt(j+1);
					b=true;
				}
				if(a && b) {
					pieList.add(new PieChart.Data(tit_1,Double.parseDouble(tit_2)));
					a=false;b=false;
				}
			}			
			drawChart(true);
		}else {
			error_label.setText("Invalid input in bulk data enter. Please check again");
		}
	}
	//undo-redo-save-open----------------------------------------------
	public void undo() {
		System.out.print("#UNDO_PIE");
		if(!priList.isEmpty()) {
			if(secList.size() < 21) {
				ArrayList<PieChart.Data> temp = (ArrayList<PieChart.Data>) priList.pop();
		        secList.push(temp);
		        
		        if(priList.empty()){pieList = new ArrayList<PieChart.Data>();}
		        else{temp = (ArrayList<PieChart.Data>) priList.peek(); pieList = temp;}
		        
		        drawChart(true);
			}else
				error_label.setText("Can't do more than 20 undo's");
		}else
			jbp();
	}
	public void redo() {
		System.out.print("#REDO_PIE");
		if(!secList.isEmpty()) {
			ArrayList<PieChart.Data> temp = (ArrayList<PieChart.Data>) secList.pop();
	        priList.push(temp);            
	        temp = (ArrayList<PieChart.Data>) priList.peek();
	        pieList = temp;
	        drawChart(true);
		}else
			jbp();
	}
	public void save() {
		if(FILE_PATH == "") {
			FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Save");
		    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fx Pie Chart", "*.fxpiechart"));
		    File file = fileChooser.showSaveDialog(primaryStage);
  
	        if (file != null) {
	        	FILE_PATH = file.getPath()+".fxpiechart";
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
	    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fx Pie Chart", "*.fxpiechart"));
	    File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
        	FILE_PATH = file.getPath()+".fxpiechart";
        	callWriter();
        	error_label.setText("File Saved '"+FILE_PATH+"'");
        }
	}
	public void load() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fx Pie Chart", "*.fxpiechart"));
		File file = fileChooser.showOpenDialog(anchor.getScene().getWindow());        
		
		if (file != null) {
			clearChart();
			ArrayList<PieChart.Data> read = PTFileManager.readDataFromFile(file.getAbsolutePath());
			FILE_PATH = file.getAbsolutePath();
			pieList = new ArrayList<PieChart.Data>(read);
			drawChart(false);
		}
	}
	public void saveXml() {
		if(FILE_PATH == "") {
			FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Save");
		    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PieChart XML", "*.piexml"));
		    File file = fileChooser.showSaveDialog(primaryStage);
  
	        if (file != null) {
	        	FILE_PATH = file.getPath()+".piexml";
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
	    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PieChart XML", "*.piexml"));
	    File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
        	FILE_PATH = file.getPath()+".piexml";
        	callWriterXml();
        	error_label.setText("Xml Saved '"+FILE_PATH+"'");
        }
	}
	public void loadXml() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PieXml", "*.piexml"));
		File file = fileChooser.showOpenDialog(anchor.getScene().getWindow());        
		
		if (file != null) {
			clearChart();
			ArrayList<PieChart.Data> read = PXFileManager.readDataFromFile(file.getAbsolutePath());
			FILE_PATH = file.getAbsolutePath();
			pieList = new ArrayList<PieChart.Data>(read);
			drawChart(false);
		}
	}
	private void callWriter() {PTFileManager.writeDataInFile(pieList,FILE_PATH);}
	private void callWriterXml() {PXFileManager.writeDataInFile(pieList,FILE_PATH);}
	//utilities Core functions---------------------------------------------
	private ArrayList<PieChart.Data> cloneList(ArrayList<PieChart.Data> l) {
		ArrayList<PieChart.Data> temp = new ArrayList<PieChart.Data>();
        for(int i=0;i<l.size();i++){
            temp.add(l.get(i));
        }
        return temp;
	}
	//Draw & decoding Validations Functions-----------------------------------------------------------
	private void drawChart(Boolean whoCalled) {
		clearTable();
		pie_data = FXCollections.observableArrayList();
		for(PieChart.Data ent_no : pieList){
			pie_data.add(new PieChart.Data(ent_no.getName(),ent_no.getPieValue()));
			PieTableModal row = new PieTableModal(ent_no.getName(),ent_no.getPieValue());
			table.getItems().add(row);
		}
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
			pieList.clear();
			for(i=0;i<table.getItems().size();i++) {				
				String x = table.getItems().get(i).getS();
				Double y = table.getItems().get(i).getD();
				
				System.out.println("\t"+x+"\t"+y);
				pieList.add(new PieChart.Data(x, y));
			}
			drawChart(true);
		}else
			error_label.setText("Value of "+table.getItems().get(i).getS()+" must be a number");
	}
	private void openPdfTextDialog(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PieChart/PdfTextDialog.fxml"));
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
	//Export functions------------------------------
	public void copyImgToClipBoard() {
		WritableImage nodeshot = pChart.snapshot(new SnapshotParameters(), null);
    	
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
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF", "*.pdf"));
		File file = fileChooser.showSaveDialog(primaryStage);
      
	     if (file != null) {
			WritableImage nodeshot = pChart.snapshot(new SnapshotParameters(), null);
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
			    img.scaleAbsolute(520, 320);
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
        fileChooser.setTitle("Save as CSV");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(primaryStage);
      
        if(file != null) {        	
        	final ObservableList<PieTableModal> data = FXCollections.observableArrayList();
    		for(int i=0;i<pieList.size();i++)
    			data.add(new PieTableModal(pieList.get(i).getName(),pieList.get(i).getPieValue()));
    		Writer writer = null;
            try {        	
            	file = new File(file.getAbsolutePath()+".csv");
                writer = new BufferedWriter(new FileWriter(file));
                for (PieTableModal person : data) {
                    String text = person.getS() + "," + person.getD() +"\n";
                    writer.write(text);
                    System.out.println("Export CSV\n\t CSV exported");
                }
            } catch (Exception ex) {ex.printStackTrace();error_label.setText("Export CSV\n\t CSV exported error");}
            finally {writer.flush();writer.close();}
        }		
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
            PdfPTable report_table = new PdfPTable(2);

            for(int i=0;i<table.getItems().size();i++) {
            	report_table.addCell(getCell(((PieTableModal) table.getItems().get(i)).getS(),PdfPCell.ALIGN_CENTER ));
            	report_table.addCell(getCell(((PieTableModal) table.getItems().get(i)).getD().toString(),PdfPCell.ALIGN_CENTER ));            	
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
	//New Window opening functions-------------------------------------
	//Open new chart--------------------------------------------------------------------
	public void openPieKit() {	
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = new Stage();
			openNew.setStage(thisstage,"Pie chart");
		} catch (IOException e) {e.printStackTrace();}
	}
	public void openBarKit() {
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = new Stage();
			openNew.setStage(thisstage,"Bar chart");
		} catch (IOException e) {e.printStackTrace();}
	}
	public void openLineKit() {
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = new Stage();
			openNew.setStage(thisstage,"Line chart");
		} catch (IOException e) {e.printStackTrace();}
	}
	public void openStackKit() {
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = new Stage();
			openNew.setStage(thisstage,"Stacked chart");
		} catch (IOException e) {e.printStackTrace();}
	}
	public void openFxPaintKit() {
		FxChartMainPage openNew = new FxChartMainPage();
		try {
			Stage thisstage = new Stage();
			openNew.setStage(thisstage,"FxPaint");
		} catch (IOException e) {e.printStackTrace();}
	}
}
