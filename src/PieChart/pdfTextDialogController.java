package PieChart;

import javafx.fxml.FXML;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class pdfTextDialogController {
	@FXML private HTMLEditor pdfTextAppend;
	public void getPdfText() {
		System.out.println("PdfText: "+pdfTextAppend.getHtmlText());
		PieChartController.pdfTextApp = pdfTextAppend.getHtmlText();
		cancelDialog();
	}
	public void cancelDialog() {
		Stage stage = (Stage) pdfTextAppend.getScene().getWindow();
	    stage.close();
	}
}
