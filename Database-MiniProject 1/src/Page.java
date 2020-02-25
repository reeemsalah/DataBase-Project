import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class Page implements Serializable {
	private int maxRows;
	private int currentRows = 0;
	private Comparable maxKey;
	private Comparable minKey;
	private String fileName;
	private Vector<Tuple> rows;
	
	
	public Page(int maxRows, String fileName) {
		
		this.maxRows = maxRows;
		File file=new File(fileName);
		try {
		file.createNewFile();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.fileName = fileName;
	}

	/**
	 * 
	 * @param t the tuple to be inserted into the page
	 */

	public void insertInto(Tuple t)
	 {
		  
		 currentRows++;
		 if(this.rows.size()>0) {
		 Iterator it=this.rows.iterator();
		 int i=0;
		 boolean flag=false;
		 while(it.hasNext()&&flag==true)
		 {
			 Tuple tmp=(Tuple)it.next();
			 i++;
			 if(tmp.compareTo(t)<0)
			 {
				 this.rows.insertElementAt(t, i+1);
				 flag=true;
//				 this.rows.add(t);

				 if (t.getKey().compareTo(maxKey)>0){
					 maxKey=t.getKey();
				 }
			 }
			 i++;
		 }
		 if(!flag) {
		 rows.add(t);
		 maxKey=t.getKey();
}
		 }
		 else {
//		 this.rows.insertElementAt(t, 0);
			 this.rows.add(t);

		 	minKey=t.getKey();
			 maxKey=t.getKey();
			 }
	 }
	 /**
	  * 
	  * @param t the Tuple to be deleted from the page
	  */
	
	public void deleteFrom(Tuple t) {
		currentRows--;
		Iterator it = this.rows.iterator();
		
		for(Tuple t1:this.rows)
		{
			if(t1.compareTo(t)==0) {
				this.rows.remove(t1);
				
			}
			
		}
		this.updateMaxKey();
		this.updateMinKey();

		String filename = this.fileName;

		// Serialization
		try {
			// Saving of object in a file
			FileOutputStream file = new FileOutputStream(filename); // overwrites file?
			ObjectOutputStream out = new ObjectOutputStream(file);

			// Method for serialization of object
			out.writeObject(this);

			out.close();
			file.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	 /**
	  * 
	  * @param t the updated tuple 
	  * @param strKey the key for the tuple
	  */
	 
	 public void update (Tuple t, String strKey)
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
	public boolean isFull() {
		return maxRows - currentRows == 0;
	}

	/**
	 * 
	 * @param t the tuple to find in the page
	 * @return true if it is found, false otherwise
	 */
	public boolean exists(Tuple t) {
		boolean flag=false;
		try { // Reading the object from a file
			FileInputStream file1 = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file1);

			// Method for deserialization of object
			Vector<Tuple> rows = (Vector<Tuple>) in.readObject();

			in.close();
			file1.close();
			
				Iterator it = rows.iterator();
				int i = 0;
				while (it.hasNext()) {
					Tuple tmp = (Tuple) it.next();
					if (tmp.compareTo(t) == 0) {
						flag=true;
					}
					i++;
			
			}

			// Saving of object in a file
			FileOutputStream file2 = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(file2);

			// Method for serialization of object
			out.writeObject(this);

			out.close();
			file2.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/*
	 * public String toString() { // String res=""; // Iterator<Tuple> it
	 * =rows.iterator(); // while(it.hasNext()) { // res+= (it.next()).toString();}
	 * // return res; return rows.toString(); }
	 */
	/**
	 * 
	 * @param keyValue the key value of the tuple to be insetred
	 * @return
	 */
	public boolean canBeInserted(Comparable keyValue) {
		return (keyValue.compareTo(minKey) >= 0) && (keyValue.compareTo(maxKey) >= 0) && !this.isFull();
	}

	public boolean isEmpty() {
		return currentRows == 0;
	}

	public void delete() {
		File file = new File(fileName);
		file.delete();
	}
	private void updateMinKey()
	{
		minKey=rows.firstElement().getKeyValue();
	}
	private void updateMaxKey()
	{
		minKey=rows.lastElement().getKeyValue();	
	}

}
