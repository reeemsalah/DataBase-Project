import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

@SuppressWarnings("serial")
public class Table implements Serializable {
	// TODO values of the inserts needs to be added
	private static int maxRows;
	private String tableName;
	private Vector<Page> tablePages;
	private ArrayList<String> columnNames;
	private ArrayList<String> columnTypes;
	private ArrayList<Boolean> clusteredCoulmns;
	private ArrayList<Boolean> indexedCoulmns;
	private String clusteredKey;
	private int numOfPages;
	private Hashtable pageInfo= new Hashtable<String,Comparable []>();
	private Vector <Tuple>page; 
	// private Vector<Object> columnValues;

	public Table(String tableName, ArrayList<String> columnNames, ArrayList<String> columnTypes,
			ArrayList<Boolean> clustered, ArrayList<Boolean> indexed,String clusteredKey, int maxRows) {
		this.tableName=tableName;
		this.columnNames=columnNames;
		this.columnTypes=columnTypes;
		this.clusteredCoulmns=clustered;
		this.indexedCoulmns=indexed;
		this.clusteredKey = clusteredKey;
		Table.maxRows=maxRows;
	}
	public  String addPage()
	{
		File file=new File(tableName+"_"+(++numOfPages)+".ser");
		try {
		file.createNewFile();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return tableName+"_"+(++numOfPages)+".ser";
	}
	
	public void Write(String filename) {
		// String filename = tableName + "_"+ numOfPages+".ser"; 
		 
         
	        // Serialization  
	        try
	        {    
	            //Saving of object in a file 
	            FileOutputStream file = new FileOutputStream(filename+".ser"); //overwrites file?
	            ObjectOutputStream out = new ObjectOutputStream(file); 
	              
	            // Method for serialization of object 
	            out.writeObject(page); 
	              
	            out.close(); 
	            file.close(); 
	        }
	        catch(Exception ex) 
	        { 
	            ex.printStackTrace();
	        } 
	}
	
	public void Read(String filename) {
		
		try
				{
				ObjectInputStream o = new ObjectInputStream( new FileInputStream(filename+".ser"));
				// Note the typecasts below
				while(o!=null) {
				Tuple foo1 = (Tuple) o.readObject();
				System.out.println(foo1 +" des");
				}
				o.close();
				
				}
				catch (ClassNotFoundException e)
				{
				System.out.println(e);
				}
				catch (FileNotFoundException e)
				{
				System.out.println(e);
				}
				catch (IOException e)
				{
				System.out.println(e);
				
				}	
	}
	/*
	 * public void setName(String tableName) { this.tableName = tableName; }
	 * 
	 * public void addToNames(String columnName) { this.columnNames.add(columnName);
	 * }
	 * 
	 * public void addToTypes(String columnType) { this.columnTypes.add(columnType);
	 * }
	 */
	public void insert(Tuple t) {
		if(numOfPages==0)//Creating the first page
		{
			String file1=addPage();
			pageInfo.put(file1, new Comparable[] {maxRows,t.getKeyValue()});
		}
		else
		{
			
		}
		/*
		 * Iterator<Page> iterator = tablePages.iterator();
		 * if(tablePages.lastElement().isFull()) {
		 * 
		 * } int countPagesSoFar = 0; while (iterator.hasNext()) { if
		 * (iterator.next().isFull()) { countPagesSoFar++; } else { Page p =
		 * tablePages.get(countPagesSoFar); p.insertInto(t); } }
		 */
	}

	public String returnTableName() {
		return this.tableName;
	}
	
	public String returnClusteredKey() {
		return this.clusteredKey;
	}

}
