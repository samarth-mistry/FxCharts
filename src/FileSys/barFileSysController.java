package FileSys;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class barFileSysController {
	static FileWriter locFile = null;
	@SuppressWarnings("null")
	public static void writeDataInFile(ArrayList<XYChart.Series<String, Number>> SeriList, String FilePath) {
		System.out.println("File System Report:\n");
		clearFile(FilePath);
	    try {
	    	locFile = new FileWriter(FilePath,true);
			int sr_id = 0;
			for(XYChart.Series<String, Number> seriList : SeriList) {
				for(Data<String, Number> seriData : seriList.getData()) {
					locFile.append(sr_id+"-"+seriList.getName()+"["+seriData.getXValue()+","+seriData.getYValue()+"]\n");
				}
				sr_id++;
			}
	        System.out.println("\tFile Appended");
	    } catch(IOException e) {
	        System.out.println("\tFileWriter Catch block");
	        e.printStackTrace();
	    } finally {
            System.out.println("\tFileWriter Finally block");
            try {if(locFile != null) {locFile.close();}} 
            catch(IOException e) {e.printStackTrace();}
        }
	}
	public static ArrayList<XYChart.Series<String, Number>> readDataFromFile(String FilePath) {
		Scanner scanner = null;
		System.out.println("File System Report:\n");
		ArrayList<XYChart.Series<String, Number>> SeriList = new ArrayList<XYChart.Series<String, Number>>();
		String previous_sr_id = "";
        try {
        	scanner = new Scanner(new FileReader(FilePath));
            scanner.useDelimiter(",");
        	String sr_id,name,x,y;
            int p,q,r,s;
            boolean first = true;
            Series<String, Number> series = new XYChart.Series<String, Number>();
            ArrayList<String> xval = new ArrayList<String>(),names = new ArrayList<String>();
            ArrayList<Number> yval = new ArrayList<Number>();

            int name_index = 0;
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                x="";y="";name="";sr_id="";
                for(p=0; line.charAt(p) != '-';p++) 
        			sr_id+=line.charAt(p);
                if(first) {
                	previous_sr_id = sr_id;
                	first = false;
                }
                
                for(q=p+1; line.charAt(q) != '[';q++) 
        			name+=line.charAt(q);
                
                if(!previous_sr_id .equals(sr_id)) {
                	series = new XYChart.Series<String, Number>();
                	for(int i=0;i<xval.size();i++) {
                		series.getData().add(new Data<String, Number>(xval.get(i),yval.get(i)));
                	}
        			series.setName(names.get(name_index));
        			name_index++;
        			SeriList.add(series);
        			names.clear();
        			xval.clear();
        			yval.clear();
        			previous_sr_id = sr_id;
        		}
                
                for(r=q+1; line.charAt(r) != ',';r++) 
        			x+=line.charAt(r);
        		
        		for(s=r+1;line.charAt(s) != ']';s++)
        			y+=line.charAt(s);

        		names.add(name);
        		xval.add(x);
        		yval.add(Double.parseDouble(y));
        		
        		if(!scanner.hasNextLine()) {
        			System.out.println("LAST Prev = this"+ previous_sr_id+" - " + sr_id);
                	series = new XYChart.Series<String, Number>();
                	for(int i=0;i<xval.size();i++) {
                		series.getData().add(new Data<String,Number>(xval.get(i),yval.get(i)));
                	}
        			series.setName(name);
        			SeriList.add(series);
        			names.clear();
        			xval.clear();
        			yval.clear();
        		}
            }
    		System.out.println("\tFile Reading");
        } catch(Exception e) {
            e.printStackTrace();
    		System.out.println("\tFile Reading error");
        } finally {
            if(scanner != null) {
                scanner.close();
            }
    		System.out.println("\tFile Reading finally");
        }
        return SeriList;
	}
	public static void clearFile(String FilePath) {
		try {
			locFile = new FileWriter(FilePath,false);
	    	locFile.flush();
	    	locFile.close();
			System.out.println("\tFile Cleared");
		}catch(IOException e) {
			System.out.println("\tFile clearing error");
		}
	}
}
