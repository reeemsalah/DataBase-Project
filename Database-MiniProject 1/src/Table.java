import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
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
	// Clustered key Column Name
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

	// to find the pages
	public ArrayList<String> findPages(Tuple t) {
		Comparable tKey = t.getKeyValue();
		ArrayList<String> pages = new ArrayList<String>();
		Set<String> keys = pageInfo.keySet();
		for (String key : keys) {
			Comparable[] temp = pageInfo.get(key);
			if (tKey.compareTo(temp[1]) > 0 && tKey.compareTo(temp[2]) < 0) {
				pages.add((String) tKey);
			}
		}
		return pages;

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
		 
		// Serialization
		
		try {
			// Saving of object in a file
			FileOutputStream file1 = new FileOutputStream(filename+".ser" ); // overwrites file?
			ObjectOutputStream out = new ObjectOutputStream(file1);
			
			// Method for serialization of object
			out.writeObject(tableName);
			out.writeObject(columnNames);
			out.writeObject(columnTypes);
			out.writeObject(clusteredCoulmns);
			out.writeObject(indexedCoulmns);
			out.writeObject(clusteredKey);
			out.writeObject(maxRows);






			out.close();
			file1.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
}

	public void Read(String filename) {
		try {
			//ObjectInputStream o = new ObjectInputStream(new FileInputStream(filename ));
			FileInputStream fi = new FileInputStream((filename+".ser"));
			ObjectInputStream o = new ObjectInputStream(fi);


			
			while(o!=null) {
				o.read();
				System.out.println(tableName);
				System.out.println(columnNames);
				System.out.println(columnTypes);
				System.out.println(clusteredCoulmns);
				System.out.println(indexedCoulmns);
				System.out.println(clusteredKey);
				System.out.println(maxRows);
				
				break;
				
				
			}
			o.close();
			fi.close();
			

			} catch (FileNotFoundException e) {
			e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
			}
			
	}
	      
	  
	     
	public String getNextPage(String currFile) {
		// gets the next page(in terms of key) after current file

		// String[] tmp = (String[]) pageInfo.entrySet()
		Comparable currKey = pageInfo.get(currFile)[2];

		String[] tmp = (String[]) pageInfo.keySet().toArray();
		String[] keyArr = new String[tmp.length];
		int i;
		for (i = 0; i < tmp.length; i++)
			keyArr[i] = (String) tmp[i];
		// copy file names into keyArr Arrays.sort(keyArr); // needless but can be kept
		boolean found = false;
		for (i = 0; i < keyArr.length; i++) {
			Comparable maxIndex = pageInfo.get(keyArr[i])[2];
			if (currKey.compareTo(maxIndex) <= 0) // compare to min index of each page
				found = true;
			break; // stop at page wanted (first page where entry key is greater)
		}
		if (found) {
			return keyArr[i];
		} else {
			return null;
		}

	}

	public void insert(Tuple t) {
		page.clear();
		String[] allFiles = (String[]) pageInfo.keySet().toArray();
		Comparable[] allMin = new Comparable[allFiles.length];
		Comparable[] allMax = new Comparable[allFiles.length];
		int i = 0;
		for (String name : allFiles) {
			allMin[i] = (Comparable) pageInfo.get(name)[1];
			allMax[i] = (Comparable) pageInfo.get(name)[2];
		}
		Arrays.sort(allMin);
		Arrays.sort(allMax);

		ArrayList<String> options = findPages(t);
		if (options.size() == 0) {

			if (pageInfo.isEmpty()) {
				options.add(addPage());
			} else {
				if (t.getKeyValue().compareTo(allMin[0]) < 0) // smaller than smallest key
				{
					for (int j = 0; j < allFiles.length; j++) {
						if (pageInfo.get(allFiles[j])[1] == allMin[0]) {
							options.add(allFiles[i]);
						}
					}
				} else // larger than largest key
				{
					if (t.getKeyValue().compareTo(allMax[allMax.length - 1]) > 0) {
						for (int j = 0; j < allFiles.length; j++) {
							if (pageInfo.get(allFiles[j])[1] == allMax[allMax.length - 1]) {
								options.add(allFiles[i]);
							}
						}
					}
				}

			}

		}
		insertHelper(t, options);
	}

	public void insertHelper(Tuple t, ArrayList<String> pages) {
		boolean found = false;
		for (int i = 0; i < pages.size(); i++) {
			if (!isPageFull(pages.get(i))) {
				found = true;
				Read(pages.get(i));
				insertPage(t);
				updateMinKey(pages.get(i));
				updateMaxKey(pages.get(i));
				Write(pages.get(i));
			}
		}
		if (!found) {
			// insert into last possible page and copy and remove one row
			Read(pages.get(pages.size() - 1));
			insertPage(t);
			Tuple temp = page.lastElement();
			page.remove(page.lastElement());
			updateMinKey(pages.get(pages.size() - 1));
			updateMaxKey(pages.get(pages.size() - 1));
			Write(pages.get(pages.size() - 1));

			String next = getNextPage(pages.get(pages.size() - 1)); // what if arraylist is not sorted by minkey?

			if (next == null) {// no next page
				String file1 = addPage(); // adding the info in the hashtable
				pageInfo.put(file1, new Comparable[] { 1, t.getKeyValue(), t.getKeyValue() });
				page.add(t); // only add to vector
				Write(file1); // write to new file
			} else {
				if (!isPageFull(next)) { // shifting can be done
					Read(next);
					insertPage(t);
					updateMinKey(next);
					updateMaxKey(next);
					Write(next);
				} else { // next is full
					page.clear();
					Read(next);
					insertPage(temp);
					Tuple temp2 = page.lastElement();
					page.remove(page.lastElement());
					updateMinKey(next);
					updateMaxKey(next);
					Write(next);
					insert(temp2);
				}

			}

		}
	}

	public boolean isPageFull(String filename) {
		int noOfRows = (int) pageInfo.get(filename)[0];
		if (maxRows - noOfRows > 0) {
			return false;
		} else
			return true;
	}

	public void insertPage(Tuple t) {

		if (this.page.size() > 0) {
			Iterator it = this.page.iterator();
			int i = 0;
			boolean inserted = false;
			while (it.hasNext() && inserted == true) {
				Tuple tmp = (Tuple) it.next();
				i++;
				if (tmp.compareTo(t) < 0) {
					this.page.insertElementAt(t, i + 1);
					inserted = true;
//				 this.rows.add(t);

				}
				i++;
			}
			if (!inserted) {
				page.add(t);
			}
		} else {
//		 this.rows.insertElementAt(t, 0);
			this.page.add(t);

		}
	}

	public void delete(Tuple t) {
		page.clear();
		for (String file : pageInfo.keySet()) {
			deleteFromPage(file, t);
		}

	}

	/**
	 * 
	 * @param fileName name of the file to delete t tuple from
	 * @param t        tuple to be deleted from fileName
	 */
	public void deleteFromPage(String fileName, Tuple t) {
		page.clear();
		Read(fileName);
		int i = 0;
		for (Tuple t1 : page) {
			if (t1.helperDelete(t.getAttributes())) {
				page.remove(t1);
				i++;
			}
		}
		if (page.size() == 0) {
			deletePage(fileName);
		} else {
			Comparable[] currentPageInfo = pageInfo.get(fileName);
			int currentNoOfPages = (int) currentPageInfo[0];
			pageInfo.replace(fileName,
					new Comparable[] { (currentNoOfPages - i), updateMinKey(fileName), updateMaxKey(fileName) });

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

	public String getTableName() {
		return tableName;
	}

	public ArrayList<String> getColumnNames() {
		return columnNames;
	}

	public ArrayList<String> getColumnTypes() {
		return columnTypes;
	}

	public ArrayList<Boolean> getClusteredCoulmns() {
		return clusteredCoulmns;
	}

	public ArrayList<Boolean> getIndexedCoulmns() {
		return indexedCoulmns;
	}

	public String[] readTableMetadata() {
		String line = "";
		String splitBy = ",";
		String[] contents = null;
		try {
			// parsing a CSV file into BufferedReader class constructor
			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			while ((line = br.readLine()) != null) // returns a Boolean value
			{
				contents = line.split(splitBy); // use comma as separator

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// closes the scanner
		return contents;
	}

	// TODO change signature to Tuple t instead of Hashtable
	public void updateTable(String strClusteringKey, Tuple t) throws DBAppException {
		ArrayList<String> pages = findPages(t);
		for (String fileName : pages) {
			Read(fileName);
			for (Tuple t1 : page) {
				String[] contents = readTableMetadata();
				// String colname=contents[1];
				String coltype = contents[2];

				if (coltype.equals(t.getClass().getCanonicalName())) {
					for (String key : t.getAttributes().keySet())
						t1.edit(key, t.getValueOfColumn(key));
				}

				Date currentdate = new Date();

				t1.edit("TouchDate", currentdate);
			}
		}

	}

	// updates the min key of the page in the page vector
	/**
	 * 
	 * @param fileName
	 * @return updated minimum key of fileName
	 */
	public Comparable updateMinKey(String fileName) {
		return page.firstElement().getKeyValue();

	}

	/**
	 * 
	 * @param fileName
	 * @return updated maximum key of fileName
	 */
	public Comparable updateMaxKey(String fileName) {
		return page.lastElement().getKeyValue();

	}

	public void deletePage(String fileName) {
		File file = new File(fileName);
		file.delete();
		pageInfo.remove(fileName);
	}

	/**
	 * 
	 * @param t tuple t
	 * @return an array of files that may contain t sorted in ascending order
	 *         according to their minimum key
	 */
	public ArrayList<String> findPage(Tuple t) {
		Comparable tupleKey = t.getKeyValue();
		// holds the values of the pages that may contain the tuple
		ArrayList<String> temp = new ArrayList<String>();
		for (String key : pageInfo.keySet()) {
			Comparable minKey = pageInfo.get(key)[1];
			Comparable maxKey = pageInfo.get(key)[2];
			if (tupleKey.compareTo(minKey) >= 0 && tupleKey.compareTo(maxKey) <= 0) {
				temp.add(key);
			}

		}
		ArrayList<String> res = new ArrayList<String>();
		for (String s1 : temp) {
			Comparable minKey1 = pageInfo.get(s1)[1];
			String toBeInserted = s1;
			for (String s2 : temp) {
				Comparable minKey2 = pageInfo.get(s2)[1];
				if (!s1.equals(s2) && minKey1.compareTo(minKey2) > 0) {
					toBeInserted = s2;
				}
			}
			res.add(toBeInserted);
		}
		return res;
	}

	public static void main(String[] args) {
    	ArrayList<String> columns = new ArrayList<String>();
		columns.add("id");
		columns.add("name");
		ArrayList<String> types = new ArrayList<String>();
		types.add("boolean");
		types.add("integer");
		ArrayList<Boolean> indexed = new ArrayList<Boolean>();
		indexed.add(false);
		indexed.add(false);
		ArrayList<Boolean> clustered = new ArrayList<Boolean>();
		clustered.add(true);
		clustered.add(false);
		
		Table t= new Table("Student", columns, types, indexed,clustered , "id", 10);
//		Read("file.ser");
		t.Write("tablefile");
		t.Read("tablefile");
	}

}
