package XMLController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.parsers.*;

import org.w3c.dom.Document;

import java.util.ArrayList;
import javafx.scene.chart.PieChart;

import PieChart.PieXmlConvertor;

public class PieChartXmlManager {
	public static void writeDataInFile() {
		try {
	         File inputFile = new File("input.txt");
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	         //NodeList nList = doc.getElementsByTagName("student");
	         System.out.println("----------------------------");
	         
		}catch(Exception e) {}
	}
	public static ArrayList<PieChart.Data> readDataFromFile(String FilePath) {
		ArrayList<PieChart.Data> pieData = new ArrayList<PieChart.Data>();
		return pieData;
	}
	public static void clearFile(String FilePath) {
		
	}

	public static void main(String[] args) {
		writeDataInFile();
	}
}
