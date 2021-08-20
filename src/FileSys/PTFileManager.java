package FileSys;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.chart.PieChart;

public class PTFileManager {
	static FileWriter locFile = null; 
	@SuppressWarnings("null")
	public static void writeDataInFile(ArrayList<PieChart.Data> pieList, String FilePath) {
		System.out.println("File System Report:\n");
		clearFile(FilePath);
		try {
			locFile = new FileWriter(FilePath,true);
			for(int i=0;i<pieList.size();i++)
	    		locFile.append(pieList.get(i).getName()+","+pieList.get(i).getPieValue()+"\n");
		}catch(IOException e) {
			System.out.println("\tFileWriter Catch block");
	        e.printStackTrace();
		}finally {
			System.out.println("\tFileWriter Finally block");
			try {if(locFile != null) {locFile.close();}} 
			catch(IOException e) {e.printStackTrace();}
		}
	}
	public static ArrayList<PieChart.Data> readDataFromFile(String FilePath) {
		Scanner scanner = null;
		System.out.println("File System Report:\n"+FilePath);
		ArrayList<PieChart.Data> pieData = new ArrayList<PieChart.Data>();
        try {
            scanner = new Scanner(new FileReader(FilePath));
            scanner.useDelimiter(",");
            String nm="",val="";
            int j,k;
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                nm="";val="";
                for(j=0; line.charAt(j) != ',';j++) 
        			nm+=line.charAt(j);
        		
        		for(k=j+1;k<line.length();k++)
        			val+=line.charAt(k);
        		
        		pieData.add(new PieChart.Data(nm,Double.parseDouble(val)));
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
}
