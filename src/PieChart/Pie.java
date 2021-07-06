package PieChart;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
public class Pie extends Application{
	public void start(Stage primaryStage) throws Exception {
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
