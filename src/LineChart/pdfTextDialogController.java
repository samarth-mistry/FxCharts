package LineChart;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class pdfTextDialogController {
	@FXML private HTMLEditor pdfTextAppend;
	final KeyCombination altEnter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
	@FXML void goEvent(KeyEvent event) {
		if (altEnter.match(event)){getPdfText();event.consume();}
    	else {pdfTextAppend.requestFocus();event.consume();}
	}
	public void getPdfText() {
		System.out.println("PdfText: "+pdfTextAppend.getHtmlText());
		LineChartController.pdfTextApp = pdfTextAppend.getHtmlText();
		System.out.println(LineChartController.pdfTextApp);
		cancelDialog();
	}
	public void cancelDialog() {
		Stage stage = (Stage) pdfTextAppend.getScene().getWindow();
	    stage.close();
	}
}
