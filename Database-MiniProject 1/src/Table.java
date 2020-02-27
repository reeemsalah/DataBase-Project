import java.awt.Polygon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.Date;
import java.util.Enumeration;

import javax.naming.ldap.SortControl;

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
	private String clusteredKeyType;
	private int numOfPages;
	// awel comparable da l min key in page wl tany array at index zero no of rows w index 1 esm l file 
	private Hashtable<Comparable, Comparable[]> pageInfo = new Hashtable<Comparable, Comparable[]>();
	private Vector<Tuple> page;
	// private Vector<Object> columnValues;

	//TODO add TouchDate column
	public Table(String tableName, ArrayList<String> columnNames, ArrayList<String> columnTypes,
			ArrayList<Boolean> clustered, ArrayList<Boolean> indexed, String clusteredKeyType, int maxRows) {
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.columnTypes = columnTypes;
		this.clusteredCoulmns = clustered;
		this.indexedCoulmns = indexed;
		this.clusteredKeyType = clusteredKeyType;
		Table.maxRows = maxRows;
	}
	/**
	 * 
	 * @return the new file for the newly created page
	 */
	public String addPage() {
		File file = new File(tableName + "_" + (++numOfPages) + ".ser");
		try {
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tableName + "_" + (++numOfPages) + ".ser";
	}

	public void Write(String filename) {
		// String filename = tableName + "_"+ numOfPages+".ser";

		// Serialization
		try {
			// Saving of object in a file
			FileOutputStream file = new FileOutputStream(filename + ".ser"); // overwrites file?
			ObjectOutputStream out = new ObjectOutputStream(file);

			// Method for serialization of object
			out.writeObject(page);

			out.close();
			file.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void Read(String filename) {

		try {
			ObjectInputStream o = new ObjectInputStream(new FileInputStream(filename + ".ser"));
			// Note the typecasts below
			while (o != null) {
				page.add((Tuple) o.readObject());
				System.out.println(page + " des");
			}
			o.close();

		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
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
		if (numOfPages == 0)// Creating the first page
		{
			String file1 = addPage();
			//adding the info in the hashtable
			pageInfo.put(t.getKeyValue(), new Comparable[] { 1, file1 });
			page.add(t);
			Write(file1);
		} else// there is atleast one page
		{
			// putting the set of indices into an Array
			Object[] tmp = pageInfo.keySet().toArray();
			Comparable[] keyArr = new Comparable[tmp.length];
			int i;
			for (i = 0; i < tmp.length; i++)
				keyArr[i] = (Comparable) tmp[i];
			Arrays.sort(keyArr);
			for (i = 0; i < keyArr.length; i++) {
				if (t.getKeyValue().compareTo(keyArr[i]) < 0)
					break;
			}
			if(i!=0)
				i--;
			// the tuple should be inserted at page with key at index i
			Comparable[] currentPageInfo = pageInfo.get(keyArr[i]);
			int noOfRows = (int) currentPageInfo[0];
			String filename = (String) currentPageInfo[1];
			// checking if the page is full
			// TODO shift a row from current page into next page and then insert the row in
			// the page
			if (maxRows - noOfRows == 0)// the page is full
			{
				// load the next page and then get the last row and compare it with the current
				// tuple
				// if it is smaller then shift the last row of the current page to the next one
				// and insert the tuple in the current page
				// otherwise insert the tuple in the next page and update the minKey of the page

			} else// the page isn't full
			{
				noOfRows++;
				if (t.getKeyValue().compareTo(keyArr[i]) <0)
				{
					pageInfo.remove(keyArr[i]);
					pageInfo.put(t.getKeyValue(), currentPageInfo);
				}
					
				// reading the info from the file
				Read(filename);
				// inserting the tuple into the array of vectors
				// TODO get the modified insert from Page.insertInto
				// overriding the file
				Write(filename);
			}

		}
		
	}

	public String returnTableName() {
		return this.tableName;
	}

	public String returnClusteredKey() {
		return this.clusteredKeyType;
	}
	public void updateTable(String strClusteringKey,Hashtable<String,Object> htblColNameValue )
			throws DBAppException, ParseException {
			Enumeration n=htblColNameValue.elements();
			String colname=(String)n.nextElement();
			Object value=n.nextElement();
			
				Object[] tmp = pageInfo.keySet().toArray();
				Comparable[] keyArr = new Comparable[tmp.length];
				int i;
				for (i = 0; i < tmp.length; i++)
					keyArr[i] = (Comparable) tmp[i];
				Arrays.sort(keyArr);
				for (i = 0; i < keyArr.length; i++) {
					if (strClusteringKey.compareTo((String) keyArr[i]) < 0)
						break;
				}
				if(i!=0)
					i--;
				Comparable[] currentPageInfo = pageInfo.get(keyArr[i]);
				String filename = (String) currentPageInfo[1];
				Read(filename);
				for(int j=0;i<page.size();i++) {
				if(page.get(i).getKeyValue().toString().equals(strClusteringKey)) {
					page.get(i).edit(colname,(Comparable)value);
				}
				}

			
			
		}
	

	public static void main(String[] args) {
//		Table t= new Table("Student", [id, name], []);
//		Read("file.ser");
	}

}
