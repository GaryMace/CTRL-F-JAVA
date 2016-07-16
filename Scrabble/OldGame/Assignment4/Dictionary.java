/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {
	
	public List<String> openDictionary () {
		//searching through an arrayList(Which is stored in memory(RAM) is faster than searching through a file on the disk (e.g. C:\)
		//so we'll read the dictionary into an arraylist and search through that
		List<String> dictionary = new ArrayList<String>();
		BufferedReader fileReader = null;
		try {   
			//set the reader to read through the dictionary
	   		fileReader = new BufferedReader(new FileReader("sowpods.txt"));
	    	String tempStr;
	    	
	    	//set the line read to the current index in arrayList
	    	while ((tempStr = fileReader.readLine()) != null) {
	        	dictionary.add(tempStr);									
	    	}
	    	//catch any exceptions that may be thrown as a result of reading the file
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException exp) {
	    	exp.printStackTrace();
		} finally {
			//if the file reader has nothing left to read
	    	if (fileReader != null) {
	    		try {
					fileReader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
	    	}
		}
		return dictionary;
	}
}
