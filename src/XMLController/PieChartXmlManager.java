package XMLController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import PieChart.PieXmlConvertor;

public class PieChartXmlManager {
	private static FileWriter locFile = null;
	public static void writeDataInFile(ArrayList<PieChart.Data> pieList, String FilePath) {
		System.out.println("XML System Report:\n");
		clearFile(FilePath);
		try {
			XStream xstream = new XStream(new DomDriver());
			//xstream.alias("PieData", PieXmlConvertor.class);
		      locFile = new FileWriter(FilePath,true);
		      //locFile.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<PieXmlConvertor>");
		      locFile.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		      PieXmlConvertor pieObj = new PieXmlConvertor();
		      String xml="";
		      for(int i=0;i<pieList.size();i++) {
		    	  pieObj = new PieXmlConvertor();
		    	  pieObj.setPieName(pieList.get(i).getName());
			      pieObj.setPieValue(pieList.get(i).getPieValue());

			      xml = xstream.toXML(pieObj)+"\n";
			      //locFile.append(xml);
		      }
		      locFile.append(xml);
		      System.out.println(formatXml(xml));
		      //locFile.append("</PieXmlConvertor>");
		}catch(IOException e) {e.printStackTrace();
		}finally {
			try {if(locFile != null) {locFile.close();}} 
			catch(IOException e) {e.printStackTrace();}
		}
	}
	public static String formatXml(String xml) {
	      try {
	         //Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
	         Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
	         serializer.setOutputProperty(OutputKeys.INDENT, "yes");
	         serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	         
	         Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
	         StreamResult res =  new StreamResult(new ByteArrayOutputStream());            
	         
	         serializer.transform(xmlSource, res);
	         
	         return new String(((ByteArrayOutputStream)res.getOutputStream()).toByteArray());
	         
	      } catch(Exception e) {
	    	  System.out.println("Returning XML only");
	         return xml;
	      }
	   }
	public static ArrayList<PieChart.Data> readDataFromFile(String FilePath) {
		ArrayList<PieChart.Data> pieData = new ArrayList<PieChart.Data>();
		FileInputStream file;
		XStream xstream = new XStream(new DomDriver());
	    try {
	        file = new FileInputStream(FilePath);
	        PieXmlConvertor xml = (PieXmlConvertor) xstream.fromXML(file);
	        System.out.println(xml.getPieName()+xml.getPieValue());
	    }catch(IOException e) {
	    	
	    }
		return pieData;
	}
	public static void clearFile(String FilePath) {
		System.out.println("File System Report:\n");
		try {
			locFile = new FileWriter(FilePath,false);
	    	locFile.flush();
	    	locFile.close();
			System.out.println("\tFile Cleared");
		}catch(IOException e) {
			System.out.println("\tFile clearing error");
		}
	}

	public static void main(String[] args) {
		ArrayList<PieChart.Data> pieList = new ArrayList<PieChart.Data>();
		pieList.add(new Data("Pie1",10));
		pieList.add(new Data("Pie2",20));
		pieList.add(new Data("Pie3",30));
		pieList.add(new Data("Pie4",40));
		writeDataInFile(pieList, "testPieXml.xml");
		readDataFromFile("testPieXml.xml");
	}
}
