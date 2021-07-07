package LineChart;

import java.io.File;
import java.net.URL;
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
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
public class LineChart extends Application{
	public void start(Stage primaryStage) throws Exception {
//		Platform.runLater(new Runnable(){						
//			@Override
//			public void run() {
//				Dialog<Pair<String, String>> dialog = new Dialog<>();
//				dialog.setTitle("LineChart-Login");
//				dialog.setHeaderText("If authentication gets invalid then,\napplication shuts automatically");
//
//				ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
//				dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
//				
//				GridPane grid = new GridPane();
//				grid.setHgap(10);
//				grid.setVgap(10);
//				grid.setPadding(new Insets(20, 150, 10, 10));
//
//				TextField username = new TextField();
//				username.setPromptText("Username");
//				PasswordField password = new PasswordField();
//				password.setPromptText("Password");
//
//				grid.add(new Label("Username:"), 0, 0);
//				grid.add(username, 1, 0);
//				grid.add(new Label("Password:"), 0, 1);
//				grid.add(password, 1, 1);
//				
//				Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
//				loginButton.setDisable(true);
//				username.textProperty().addListener((observable, oldValue, newValue) -> {
//				    loginButton.setDisable(newValue.trim().isEmpty());
//				});
//
//				dialog.getDialogPane().setContent(grid);		
//				Platform.runLater(() -> username.requestFocus());			
//				dialog.setResultConverter(dialogButton -> {
//				    if (dialogButton == loginButtonType) {
//				        return new Pair<>(username.getText(), password.getText());
//				    }
//				    return null;
//				});
//				Optional<Pair<String, String>> result = dialog.showAndWait();
//				result.ifPresent(usernamePassword -> {
//				    System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
//				    if(usernamePassword.getKey().equals("qwe") && usernamePassword.getValue().equals("123")) {				    	
//				    	try {
//							Parent root =FXMLLoader.load(getClass().getResource("LineChart.fxml"));
//							Scene scene = new Scene(root);
//							primaryStage.setScene(scene);
//							primaryStage.setTitle("LineChart-Cancer's");
//							primaryStage.show();
//							primaryStage.setResizable(false);
//						}catch(Exception e) {}
//				    }else {
//				    	System.out.println("Error");
//				    }
//				});
//			}	
//		});	
		//URL url = new File("resources/views/LineChart.fxml").toURI().toURL();
		//Parent root =FXMLLoader.load(url);
		Parent root =FXMLLoader.load(getClass().getResource("LineChart.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("LineChart-Cancer's");
		primaryStage.show();
		primaryStage.setResizable(false);
	}
	public static void main (String[] args) {
		launch(args);
	}
}
