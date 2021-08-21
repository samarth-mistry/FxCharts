package XMLController.BarChartXml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class BXFileManager {
	private static FileWriter locFile = null;
	public static void writeDataInFile(ArrayList<XYChart.Series<String, Number>> lineList, String FilePath) {
		XStream xs = new XStream(new DomDriver());
		xs.alias("BarChartData", BXRootModal.class);
		xs.alias("Series", BXSeriesModal.class);
		xs.alias("BarNode", BXNodeModal.class);
		clearFile(FilePath);
		
		BXRootModal root = new BXRootModal();
		BXSeriesModal series = new BXSeriesModal();
		BXNodeModal point = new BXNodeModal();
		List<BXSeriesModal> list = new ArrayList<BXSeriesModal>();
		List<BXNodeModal> list2 = new ArrayList<BXNodeModal>();
		for(XYChart.Series<String, Number> ll : lineList) {
			series = new BXSeriesModal();
			series.setSeriName(ll.getName());
			list2 = new ArrayList<BXNodeModal>();
	    	for(XYChart.Data<String, Number> lineData : ll.getData()) {
	    		point = new BXNodeModal();
	    		point.setCordX(lineData.getXValue());
	    		point.setCordY(lineData.getYValue());
			    list2.add(point);
	    	}
	    	series.setListPoints(list2);
	    	list.add(series);
	     }
		root.setLines(list);
		String xml = xs.toXML(root)+"\n";
		try {
			locFile = new FileWriter(FilePath,true);
			locFile.write(xml);
			locFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static ArrayList<XYChart.Series<String, Number>> readDataFromFile(String FilePath) {
		ArrayList<XYChart.Series<String, Number>> seriList = new ArrayList<XYChart.Series<String, Number>>();
		FileInputStream file;
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(BXRootModal.class);
		xstream.alias("LineChartData", BXRootModal.class);
		xstream.alias("Series", BXSeriesModal.class);
		xstream.alias("Point", BXNodeModal.class);
	    try {
	        file = new FileInputStream(FilePath);
	        BXRootModal xml = (BXRootModal) xstream.fromXML(file);
	        Series<String, Number> seri = new XYChart.Series<String, Number>();
	        for(BXSeriesModal series : xml.getLines()) {
	        	seri = new XYChart.Series<String, Number>();
	        	seri.setName(series.getSeriName());
	        	for(BXNodeModal node : series.getListPoints()) {
	        		seri.getData().add(new XYChart.Data<String, Number>(node.getCordX(),node.getCordY()));
	        	}
	        	seriList.add(seri);
	        }
	    }catch(IOException e) {}
		return seriList;
	}
	public static void clearFile(String FilePath) {
		try {
			locFile = new FileWriter(FilePath,false);
	    	locFile.flush();
	    	locFile.close();
		}catch(IOException e) {}
	}
//	public static void main(String[] args) {
//		ArrayList<XYChart.Series<String, Number>> lineList = new ArrayList<XYChart.Series<String, Number>>();
//		Series<String, Number> series = new XYChart.Series<String, Number>();
//		Number d =0.0;
//		String s = "String";
//		series.setName("Series#1");
//		for(int i=0;i<3;i++) {
//			series.getData().add(new XYChart.Data<String, Number>(s,d));
//		}
//		lineList.add(series);
//		d=3.1;
//		s = "String2";
//		series = new XYChart.Series<String, Number>();
//		series.setName("Series#2");
//		for(int i=0;i<3;i++) {
//			series.getData().add(new XYChart.Data<String, Number>(s,d));
//		}
//		lineList.add(series);
//		writeDataInFile(lineList, "testBarXml.xml");
//		System.out.println(readDataFromFile("testBarXml.xml"));
//	}
}
