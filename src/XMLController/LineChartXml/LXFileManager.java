package XMLController.LineChartXml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class LXFileManager {
	private static FileWriter locFile = null;
	public static void writeDataInFile(ArrayList<XYChart.Series<Double, Double>> lineList, String FilePath) {
		XStream xs = new XStream(new DomDriver());
		xs.alias("LineChartData", LXRootModal.class);
		xs.alias("Series", LXSeriesModal.class);
		xs.alias("Point", LXNodeModal.class);
		clearFile(FilePath);
		
		LXRootModal root = new LXRootModal();
		LXSeriesModal series = new LXSeriesModal();
		LXNodeModal point = new LXNodeModal();
		List<LXSeriesModal> list = new ArrayList<LXSeriesModal>();
		List<LXNodeModal> list2 = new ArrayList<LXNodeModal>();
		for(XYChart.Series<Double, Double> ll : lineList) {
			series = new LXSeriesModal();
			series.setSeriName(ll.getName());
			list2 = new ArrayList<LXNodeModal>();
	    	for(XYChart.Data<Double, Double> lineData : ll.getData()) {
	    		point = new LXNodeModal();
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
	public static ArrayList<XYChart.Series<Double, Double>> readDataFromFile(String FilePath) {
		ArrayList<XYChart.Series<Double, Double>> seriList = new ArrayList<XYChart.Series<Double, Double>>();
		FileInputStream file;
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(LXRootModal.class);
		xstream.alias("LineChartData", LXRootModal.class);
		xstream.alias("Series", LXSeriesModal.class);
		xstream.alias("Point", LXNodeModal.class);
	    try {
	        file = new FileInputStream(FilePath);
	        LXRootModal xml = (LXRootModal) xstream.fromXML(file);
	        Series<Double,Double> seri = new XYChart.Series<Double, Double>();
	        for(LXSeriesModal series : xml.getLines()) {
	        	seri = new XYChart.Series<Double, Double>();
	        	seri.setName(series.getSeriName());
	        	for(LXNodeModal node : series.getListPoints()) {
	        		seri.getData().add(new XYChart.Data<Double, Double>(node.getCordX(),node.getCordY()));
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
}
