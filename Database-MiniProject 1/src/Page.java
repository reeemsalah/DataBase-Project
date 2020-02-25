import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class Page implements Serializable{
	private int maxRows;
	private int currentRows=0;
	private Vector<Tuple> rows;
	private  Comparable maxKey;
	private  Comparable minKey;
	 public Page (int maxRows)
	 {
		 this.maxRows=maxRows;
		 rows = new Vector<Tuple>();
	 }
	/**
	 * 
	 * @param t the tuple to be inserted into the page
	 */
	 // TODO pass the row as an object from method insert into table
	 //TODO check in the insert into table first if the page is already full
	 public void insertInto(Tuple t)
	 {
		 
		 
		 
		 currentRows++;
		 if(this.rows.size()>0) {
		 Iterator it=this.rows.iterator();
		 int i=0;
		 while(it.hasNext())
		 {
			 Tuple tmp=(Tuple)it.next();
			 if(tmp.compareTo(t)<0)
			 {
				 this.rows.insertElementAt(t, i);
			 }
			 i++;
		 }
		 }else {
		 this.rows.insertElementAt(t, 0);}

		 String filename = "file.ser"; 
		 
         
	        // Serialization  
	        try
	        {    
	            //Saving of object in a file 
	            FileOutputStream file = new FileOutputStream(filename); 
	            ObjectOutputStream out = new ObjectOutputStream(file); 
	              
	            // Method for serialization of object 
	            out.writeObject(this); 
	              
	            out.close(); 
	            file.close(); 
	        }
	        catch(Exception ex) 
	        { 
	            ex.printStackTrace();
	        } 
	 }
	 /**
	  * 
	  * @param t the Tuple to be deleted from the page
	  */
	 
	 public void deleteFrom (Tuple t)
	 {
		 currentRows--;
		 Iterator it=this.rows.iterator();
		 int i=0;
		 while(it.hasNext())
		 {
			 Tuple tmp=(Tuple)it.next();
			 if(tmp.compareTo(t)==0)
			 {
				 this.rows.remove(i);
			 }
			 i++;
		 }
		 
		 String filename = "file.ser"; 
		 
         
	        // Serialization  
	        try
	        {    
	            //Saving of object in a file 
	            FileOutputStream file = new FileOutputStream(filename); //overwrites file?
	            ObjectOutputStream out = new ObjectOutputStream(file); 
	              
	            // Method for serialization of object 
	            out.writeObject(this); 
	              
	            out.close(); 
	            file.close(); 
	        }
	        catch(Exception ex) 
	        { 
	            ex.printStackTrace();
	        } 
		 
	 }
	 /**
	  * 
	  * @param t the updated tuple 
	  * @param strKey the key for the tuple
	  */
	 
	 public void update(Tuple t, String strKey)
	 {
		 Iterator it=this.rows.iterator();
		 int i=0;
		 while(it.hasNext())
		 {
			 Tuple tmp=(Tuple)it.next();
			 if(tmp.getKey().equals(t.getKey()))
				 
			 {
				 rows.set(i, t);
			 }
			 i++;
		 }
		 
		 String filename = "file.ser"; 
		 
         
	        // Serialization  
	        try
	        {    
	            //Saving of object in a file 
	            FileOutputStream file = new FileOutputStream(filename); 
	            ObjectOutputStream out = new ObjectOutputStream(file); 
	              
	            // Method for serialization of object 
	            out.writeObject(this); 
	              
	            out.close(); 
	            file.close(); 
	        }
	        catch(Exception ex) 
	        { 
	            ex.printStackTrace();
	        } 
	 }
	/**
	 * 
	 * @return true if the page is full, false otherwise
	 */
	public boolean isFull()
	{
		return maxRows-currentRows==0;
	}
	/**
	 * 
	 * @param t the tuple to find in the page
	 * @return true if it is found, false otherwise
	 */
	public boolean exists(Tuple t)
	{
		for(Tuple t1:rows)
		{
			if(t1.compareTo(t)==0)
				return true;
		}
		return false;
	}

	public String toString() {
//		String res="";
//		Iterator<Tuple> it =rows.iterator();
//		while(it.hasNext()) {
//		res+= (it.next()).toString();}
//		return res;
		return rows.toString();
		}
	/**
	 * 
	 * @param keyValue the key value of the tuple to be insetred
	 * @return
	 */
	public boolean canBeInserted (Comparable keyValue)
	{
		return (keyValue.compareTo(minKey)>=0)&&(keyValue.compareTo(maxKey)>=0)&& !this.isFull();
	}
}
 