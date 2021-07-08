package FileSys;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class fileSystemController {
	static FileWriter locFile = null;
	@SuppressWarnings("null")
	public static void writeDataInFile(int[] xVal,int[] yVal, String lineName, int seriesId) {
		System.out.println("File System Report:\n");
	    try {	    
	    	locFile = new FileWriter("dat/locations.txt",true);
	    	Scanner scanner = new Scanner(new FileReader("dat/locations.txt"));
	    	if(scanner.hasNextLine()) {
	    		locFile.append("\n");
	    	}
	        //locFile.append(seriesId+"-");
	    	locFile.append("{"+lineName+"}");
	        for(int i=0;i<xVal.length;i++) {
	            locFile.append("["+xVal[i]+","+yVal[i]+"]");
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
	public static ArrayList<String> readDataFromFile() {
		Scanner scanner = null;
		System.out.println("File System Report:\n");
		ArrayList<String> seriesArr = new ArrayList<String>();
        try {
            scanner = new Scanner(new FileReader("dat/locations.txt"));
            scanner.useDelimiter(",");
            int seriesCounter = 0;
            while(scanner.hasNextLine()) {                
                String line = scanner.nextLine();                
                seriesArr.add(line);
                seriesCounter++;
                //System.out.println(line);
            }
    		System.out.println("\tFile Reading");
        } catch(IOException e) {
            e.printStackTrace();
    		System.out.println("\tFile Reading error");
        } finally {
            if(scanner != null) {
                scanner.close();
            }
    		System.out.println("\tFile Reading finally");
        }
        return seriesArr;
	}
	public static void clearFile(String fileName) {
		System.out.println("File System Report:\n");
		try {
			locFile = new FileWriter("dat/"+fileName,false);
	    	locFile.flush();
	    	locFile.close();
			System.out.println("\tFile Cleared");
		}catch(IOException e) {
			System.out.println("\tFile clearing error");
		}
	}
	/*
	 * public static void main(String[] args) { int xVal[] = {12,3,4,1,5,6,3}; int
	 * yVal[] = {12,3,4,1,5,6,3}; int i=0; String fileName = "locations.txt";
	 * clearFile(fileName); for(i=0;i<3;i++) writeDataInFile(xVal,yVal,fileName,i);
	 * 
	 * readDataFromFile(); }
	 */
}
