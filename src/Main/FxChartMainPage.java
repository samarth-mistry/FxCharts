package Main;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
public class FxChartMainPage extends Application{
	public void start(Stage primaryStage) throws Exception {	
		//auth(primaryStage);
		//chartSelector(primaryStage);
		//URL url = new File("resources/views/LineChart.fxml").toURI().toURL();
		//Parent root =FXMLLoader.load(url);
		setStage(primaryStage,"Line chart");
	}
	private void chartSelector(Stage primaryStage) throws IOException {
		String chrt[] = {"Pie chart", "Line chart", "Bar chart", "FxPaint"};
		ChoiceDialog<String> d = new ChoiceDialog<String>(chrt[0], chrt);
		d.setHeaderText("Select the chart type to simulate");        
        d.showAndWait();
        String sel = d.getSelectedItem();
        setStage(primaryStage, sel);        
	}	
	public void setStage(Stage primaryStage, String sel) throws IOException {
		Parent root = null;
		
		if(sel.equals("Pie chart")) {
			root =FXMLLoader.load(getClass().getResource("/PieChart/PieChartModel2.fxml"));
        } else if(sel.equals("Line chart")) {
        	root =FXMLLoader.load(getClass().getResource("/LineChart/LineChartModel2.fxml"));
        } else if(sel.equals("Bar chart")) {
        	root =FXMLLoader.load(getClass().getResource("/BarChart/BarChartModel2.fxml"));
        } else if(sel.equals("FxPaint")) {
        	root = FXMLLoader.load(getClass().getResource("/FxPaint/view/FxPaint.fxml"));
        } else if(sel.equals("Stacked chart")) {
        	//root =FXMLLoader.load(getClass().getResource("/LineChart/LineChart.fxml"));
        } else {
        	root =FXMLLoader.load(getClass().getResource("/LineChart/LineChart.fxml"));
        }							
		Scene scene = new Scene(root);
		//primaryStage.getIcons().add(new Image(new File("/resources/imgs/logo.ico").toURI().toString()));
		primaryStage.setScene(scene);
		primaryStage.setTitle("FxCharts-Cancer's");
		primaryStage.setMaximized(true);
		primaryStage.setFullScreen(false);
		primaryStage.setResizable(true);
		primaryStage.show();		
	}
	private void auth(Stage primaryStage) {
		Platform.runLater(new Runnable(){						
			@Override
			public void run() {
				Dialog<Pair<String, String>> dialog = new Dialog<>();
				dialog.setTitle("FxChart-Login");
				dialog.setHeaderText("If authentication gets invalid then,\napplication shuts automatically");

				ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
				
				GridPane grid = new GridPane();
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(20, 150, 10, 10));

				TextField username = new TextField();
				username.setPromptText("Username");
				PasswordField password = new PasswordField();
				password.setPromptText("Password");

				grid.add(new Label("Username:"), 0, 0);
				grid.add(username, 1, 0);
				grid.add(new Label("Password:"), 0, 1);
				grid.add(password, 1, 1);
				
				Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
				loginButton.setDisable(true);
				username.textProperty().addListener((observable, oldValue, newValue) -> {
				    loginButton.setDisable(newValue.trim().isEmpty());
				});

				dialog.getDialogPane().setContent(grid);		
				Platform.runLater(() -> username.requestFocus());			
				dialog.setResultConverter(dialogButton -> {
				    if (dialogButton == loginButtonType) {
				        return new Pair<>(username.getText(), password.getText());
				    }
				    return null;
				});
				Optional<Pair<String, String>> result = dialog.showAndWait();
				result.ifPresent(usernamePassword -> {
				    System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
				    if(usernamePassword.getKey().equals("qwe") && usernamePassword.getValue().equals("123")) {				    	
				    	try {
							chartSelector(primaryStage);
						}catch(Exception e) {}
				    }else {
				    	System.out.println("Error");
				    }
				});
			}	
		});
	}
	public static void main (String[] args) {
		launch(args);
	}
}
