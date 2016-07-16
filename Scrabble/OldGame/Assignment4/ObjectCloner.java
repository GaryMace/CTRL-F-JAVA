/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Team:				NorfolkNChance																		   //
//	Students:			Benjamin Ellafi, Gary Mac Elhineny and Przemyslaw Gawkowski   					       //
//	Student Numbers:	13920022, 13465572 and 13473698									                       //
//																										       //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package OldGame.Assignment4;

import java.io.*;
public class ObjectCloner {
 
   public ObjectCloner() {
	   
   }
   //When you write data to a stream, some buffering happens. 
   //You can't be sure when the entire data you have written will actually be sent. 
   //We do call the close() on the file/buffered writers at the end.
   //So in order to be sure that before we call the close(), all the data we have written actually gets out to the file, we call the flush() just before the close().


   // returns a deep copy of an object
   static public Object deepCopy(Object toDeepCopy) throws Exception {
      ObjectOutputStream oOutputStream = null;
      ObjectInputStream oInputStream = null;
      try {
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         oOutputStream = new ObjectOutputStream(bos); 
         
         // serialize and pass the object
         oOutputStream.writeObject(toDeepCopy);  
         oOutputStream.flush();               // flushes the output stream and forces any buffered output bytes to be written out. 
         
         // grab the object and create a new ObjectInputStream with it
         ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
         oInputStream = new ObjectInputStream(bin);
        
         // grab object from input stream and return it
         return oInputStream.readObject();
      }
      catch(Exception exp) {
         System.out.println("Exception creating deepCopy = " + exp);
         throw(exp);
      }
      finally {
         oOutputStream.close();
         oInputStream.close();
      }
   }
   
}
