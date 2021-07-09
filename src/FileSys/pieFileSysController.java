package FileSys;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class pieFileSysController {
	static FileWriter locFile = null;
	@SuppressWarnings("null")
	public static void writeDataInFile(ArrayList<String> name, ArrayList<Double> value) {
		System.out.println("File System Report:\n");
		clearFile("pieFileData.txt");
	    try {	    
	    	locFile = new FileWriter("dat/pieFileData.txt",true);
	    	for(int i=0;i<name.size();i++) {
	    		locFile.append("["+name.get(i)+","+value.get(i)+"]\n");
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
		ArrayList<String> pieData = new ArrayList<String>();
        try {
            scanner = new Scanner(new FileReader("dat/pieFileData.txt"));
            scanner.useDelimiter(",");            
            while(scanner.hasNextLine()) {                
                String line = scanner.nextLine();                
                pieData.add(line);                
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
        //System.out.println(pieData);
        return pieData;        
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
	
//	  public static void main(String[] args) { 		  
//		  String fileName = "pieFileData.txt";
//		  //clearFile(fileName);
//		  ArrayList<String> name = new ArrayList<>();
//		  ArrayList<Double> value = new ArrayList<>();
//		  name.add("str1");
//		  name.add("str2");
//		  value.add(80.0);
//		  value.add(80.1);		  
//		  //writeDataInFile(name,value);
//	  
//		  readDataFromFile(); 
//	  }

}
