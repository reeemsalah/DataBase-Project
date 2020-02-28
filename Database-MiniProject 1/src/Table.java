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
	//Clustered key Column Name
	private String clusteredKey;
	private int numOfPages;
	// awel string esm el file w el array of comparables at index 0 el current
	// noOfRows
	// array of comparables at index 1 hoe el minKey fel page
	private Hashtable<String, Comparable[]> pageInfo = new Hashtable<String, Comparable[]>();
	private Vector<Tuple> page;

	// TODO add TouchDate column
	public Table(String tableName, ArrayList<String> columnNames, ArrayList<String> columnTypes,
			ArrayList<Boolean> clustered, ArrayList<Boolean> indexed, String clusteredKey, int maxRows) {
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.columnTypes = columnTypes;
		this.clusteredCoulmns = clustered;
		this.indexedCoulmns = indexed;
		this.clusteredKey = clusteredKey;
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
		page.clear();
		if (numOfPages == 0)// Creating the first page
		{
			String file1 = addPage();
			// adding the info in the hashtable
			pageInfo.put(file1, new Comparable[] { 1, t.getKeyValue() });
			page.add(t); // only add to vector
			Write(file1); // write to new file
		} else// there is atleast one page
		{
			// putting the set of file names into an Array
			String[] tmp = (String[]) pageInfo.keySet().toArray();
			String[] keyArr = new String[tmp.length];
			int i;
			for (i = 0; i < tmp.length; i++)
				keyArr[i] = (String) tmp[i]; // copy file names into keyArr
			Arrays.sort(keyArr); // needless but can be kept
			for (i = 0; i < keyArr.length; i++) {
				Comparable minIndex = pageInfo.get(keyArr[i])[1];
				if (t.getKeyValue().compareTo(minIndex) < 0) // compare to min index of each page
					break; // stop at page wanted (first page where entry key is greater)
			}
			if (i != 0)
				i--; // whyyyyy???????????????????-----------------------------------------------------------

			// the tuple should be inserted at page with key at index i
			String filename = keyArr[i];
			Comparable[] currentPageInfo = pageInfo.get(keyArr[i]);
			int noOfRows = (int) currentPageInfo[0];
			Comparable min = (String) currentPageInfo[1];
			// checking if the page is full

			// TODO shift a row from current page into next page and then insert the row in
			// the page
			Tuple tempTuple;
			if (maxRows - noOfRows == 0)// the page is full
			{

				Read(filename);
				insertPage(t);
				Write(filename);
				// load the next page and then get the last row and compare it with the current
				// tuple
				tempTuple = page.lastElement();
				page.remove(page.lastElement());

				if (getNextPage(filename) != null) {
//				insert into next page
					insertNext(t, filename);
//					what if next page full???

				} else { // if this is the last page??

//				create new page
					page.clear();
					String file2 = addPage();
					// adding the info in the hashtable
					pageInfo.put(file2, new Comparable[] { 1, t.getKeyValue() });
//					insert into new page
					page.add(t); // only add to vector
					Write(file2);
				}

				// if it is smaller then shift the last row of the current page to the next one
				// and insert the tuple in the current page

//	//			 otherwise insert the tuple in the next page and update the minKey of the page
//				trynext(tuple, filename);

			} else// the page isn't full
			{
				noOfRows++;
				if (t.getKeyValue().compareTo(keyArr[i]) < 0) {
					// if new value is smaller than minkey for this page, update minkey
					pageInfo.remove(keyArr[i]);
					currentPageInfo[0] = (Comparable) noOfRows;
					currentPageInfo[1] = (Comparable) t.getKeyValue();
					pageInfo.put(filename, currentPageInfo);
				}

				// reading the info from the file
				Read(filename);
				insertPage(t);
				// inserting the tuple into the array of vectors
				// TODO get the modified insert from Page.insertInto
				// overriding the file
				Write(filename);
			}

		}

	}

	public void insertNext(Tuple t, String filename) {

		String newFile = getNextPage(filename);
		int rowCount = (int) pageInfo.get(newFile)[0];
		if (rowCount - maxRows < 0) {
			Read(newFile);
			insertPage(t);
			Write(newFile);
		} else {
//		Read(newFile);
//		insertPage(t);
//		Write(newFile);
			// load the next page and then get the last row and compare it with the current
			// tuple
			Tuple tempTuple = page.lastElement();
			page.remove(page.lastElement());

			if (getNextPage(filename) != null) {
//		insert into next page
				insertNext(t, newFile);
//			what if next page full???

			} else { // if this is the last page??

//		create new page
				page.clear();
				String file2 = addPage();
				// adding the info in the hashtable
				pageInfo.put(file2, new Comparable[] { 1, t.getKeyValue() });
//			insert into new page
				page.add(t); // only add to vector
				Write(file2);
			}

		}
		// if it is smaller then shift the last row of the current page to the next one
		// and insert the tuple in the current page

////			 otherwise insert the tuple in the next page and update the minKey of the page
//		trynext(tuple, filename);

	}

	public String getNextPage(String currFile) { // gets the next page(in terms of key) after current file

//			String[] tmp = (String[]) pageInfo.entrySet()
		Comparable currKey = pageInfo.get(currFile)[1];

		String[] tmp = (String[]) pageInfo.keySet().toArray();
		String[] keyArr = new String[tmp.length];
		int i;
		for (i = 0; i < tmp.length; i++)
			keyArr[i] = (String) tmp[i]; // copy file names into keyArr
		Arrays.sort(keyArr); // needless but can be kept
		boolean found = false;
		for (i = 0; i < keyArr.length; i++) {
			Comparable minIndex = pageInfo.get(keyArr[i])[1];
			if (currKey.compareTo(minIndex) <= 0) // compare to min index of each page
				found = true;
			break; // stop at page wanted (first page where entry key is greater)
		}
		if (found) {
			return keyArr[i];
		} else {
			return null;
		}

	}

	public void insertPage(Tuple t) {

		if (this.page.size() > 0) {
			Iterator it = this.page.iterator();
			int i = 0;
			boolean flag = false;
			while (it.hasNext() && flag == true) {
				Tuple tmp = (Tuple) it.next();
				i++;
				if (tmp.compareTo(t) < 0) {
					this.page.insertElementAt(t, i + 1);
					flag = true;
//				 this.rows.add(t);

				}
				i++;
			}
			if (!flag) {
				page.add(t);
			}
		} else {
//		 this.rows.insertElementAt(t, 0);
			this.page.add(t);

		}
	}

	public void delete(Tuple t) {
		page.clear();
		// putting the set of file names into an Array
		String[] tmp = (String[]) pageInfo.keySet().toArray();
		String[] keyArr = new String[tmp.length];
		int i;
		for (i = 0; i < tmp.length; i++)
			keyArr[i] = (String) tmp[i]; // copy file names into keyArr
		Arrays.sort(keyArr); // needless but can be kept
		for (i = 0; i < keyArr.length; i++) {
			Comparable minIndex = pageInfo.get(keyArr[i])[1];
			if (t.getKeyValue().compareTo(minIndex) < 0) // compare to min index of each page
				break; // stop at page wanted (first page where entry key is greater)
		}
		if (i != 0)
			i--;
		deleteFromPage(keyArr[i], t);
		Comparable minKey = pageInfo.get(keyArr[i])[1];
		//if we have pages with similar keys
		while(i>=0 && pageInfo.get(keyArr[i])[1].compareTo(minKey)==0 && minKey.compareTo(t.getKeyValue())==0) {
			deleteFromPage(keyArr[i], t);
			i--;
		}

	}

	public void deleteFromPage(String fileName, Tuple t) {
		page.clear();
		Read(fileName);
		for (Tuple t1 : page) {
			if (t1.compareTo(t) == 0) {
				page.remove(t1);
				break;
			}
		}
		if (page.size() == 0) {
			deletePage(fileName);
		} else {
			Comparable[] currentPageInfo = pageInfo.get(fileName);
			int currentNoOfPages = (int) currentPageInfo[0];
			pageInfo.replace(fileName, new Comparable[] { --currentNoOfPages, currentPageInfo[1] });
			updateMinKey(fileName);
		}
		Write(fileName);
		page.clear();
	}

	public String returnTableName() {
		return this.tableName;
	}

	public String returnClusteredKey() {
		return this.clusteredKey;
	}

	public void updateTable(String strClusteringKey, Hashtable<String, Object> htblColNameValue)
			throws DBAppException, ParseException {

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
		if (i != 0)
			i--;
		Comparable[] currentPageInfo = pageInfo.get(keyArr[i]);
		String filename = (String) currentPageInfo[1];
		Read(filename);
		for (int j = 0; i < page.size(); i++) {
			if (page.get(i).getKeyValue().toString().equals(strClusteringKey)) {
				Tuple t = page.get(i);
				for (String key : htblColNameValue.keySet()) {
					t.edit(key, (Comparable) (htblColNameValue.get(key)));

				}

			}
		}

	}

	// updates the min key of the page in the page vector
	/**
	 * 
	 * @param key the filename of the page to be upadted
	 */
	public void updateMinKey(String fileName) {
		Comparable minKey = page.firstElement().getKeyValue();
		Comparable[] newInfo = pageInfo.get(fileName);
		newInfo[1] = minKey;
		pageInfo.replace(fileName, newInfo);

	}

	public void deletePage(String fileName) {
		File file = new File(fileName);
		file.delete();
		pageInfo.remove(fileName);
	}

	public static void main(String[] args) {
//		Table t= new Table("Student", [id, name], []);
//		Read("file.ser");
	}

}
