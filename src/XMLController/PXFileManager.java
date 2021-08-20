package XMLController;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;

public class PXFileManager {
	private static FileWriter locFile = null;
	public static void writeDataInFile(ArrayList<PieChart.Data> pieList, String FilePath) {
		XStream xs = new XStream(new DomDriver());
		xs.alias("PieChartData", PXRootModal.class);
		xs.alias("PieNode", PXNodeModal.class);
		clearFile(FilePath);
		
		PXRootModal xcov = new PXRootModal();
		List<PXNodeModal> list = new ArrayList<PXNodeModal>();
		PXNodeModal pieObj = new PXNodeModal();
	    for(int i=0;i<pieList.size();i++) {
	    	pieObj = new PXNodeModal();
	    	pieObj.setPieName(pieList.get(i).getName());
		    pieObj.setPieValue(pieList.get(i).getPieValue());
		    list.add(pieObj);
	     }
		xcov.setPies(list);
		String xml = xs.toXML(xcov)+"\n";
		try {
			locFile = new FileWriter(FilePath,true);
			locFile.write(xml);
			locFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static ArrayList<PieChart.Data> readDataFromFile(String FilePath) {
		ArrayList<PieChart.Data> pieData = new ArrayList<PieChart.Data>();
		FileInputStream file;
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(PXRootModal.class);
		xstream.alias("PieChartData", PXRootModal.class);
	    try {
	        file = new FileInputStream(FilePath);
	        PXRootModal xml = (PXRootModal) xstream.fromXML(file);
	        
	        for(PXNodeModal ob : xml.getPies())
	        	pieData.add(new Data(ob.getPieName(),ob.getPieValue()));
	    }catch(IOException e) {}
		return pieData;
	}
	public static void clearFile(String FilePath) {
		try {
			locFile = new FileWriter(FilePath,false);
	    	locFile.flush();
	    	locFile.close();
		}catch(IOException e) {}
	}
//	public static void main(String[] args) {
//		ArrayList<PieChart.Data> pieList = new ArrayList<PieChart.Data>();
//		pieList.add(new Data("Pie1",10));
//		pieList.add(new Data("Pie2",20));
//		pieList.add(new Data("Pie3",30));
//		pieList.add(new Data("Pie4",40));
//		writeDataInFile(pieList, "testPieXml.xml");
//		System.out.println(readDataFromFile("testPieXml.xml"));
//	}
}
