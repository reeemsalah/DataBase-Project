import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.*;

public class DBApp {
	private static Hashtable<String, Table> tables;
	private static int maxRows;
	private static int nodeSize;

	public DBApp() {
		tables = new Hashtable<String, Table>();
	}

	public void init() {
		try {
		File myFile=new File("data/metadata.csv");
		myFile.createNewFile();	
		FileReader reader=new FileReader("config/DBApp.properties");  
	
	    Properties p=new Properties();  
	    p.load(reader);  
	      
	    maxRows=Integer.parseInt(p.getProperty("MaximumRowsCountinPage"));
	    nodeSize=Integer.parseInt(p.getProperty("NodeSize"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void createTable(String strTableName, String strClusteringKeyColumn,
			Hashtable<String, String> htblColNameType) throws DBAppException {
		boolean flag = false;
		//System.out.println("entered create table");
		if (tables.size() != 0) {
			Object[] tmp = tables.keySet().toArray();
			String[] tableNames = new String[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				tableNames[i] = (String) tmp[i];
			}

			// System.out.println("here2");
			for (int i = 0; i < tableNames.length; i++) {
				if (tableNames[i].equals(strTableName)) {
					// System.out.println(tableNames[i]);
					flag = true;
					break;
				}
			}
		}
		if (flag == true) {
			throw new DBAppException("This table already exists");
		} else {
			// System.out.println("here3");
			ArrayList<String> columnNames = new ArrayList<String>();
			ArrayList<String> columnTypes = new ArrayList<String>();
			Set<String> names = htblColNameType.keySet();
			for (String key : names) {
				columnNames.add(key);
				columnTypes.add(htblColNameType.get(key));
			}
			columnNames.add("TouchDate");
			columnTypes.add("java.util.Date");

			ArrayList<Boolean> clustered = new ArrayList<Boolean>();
			for (int i = 0; i < columnNames.size(); i++) {
				
				if (columnNames.get(i).equals(strClusteringKeyColumn)) {
					clustered.add(true);
				} else {
					clustered.add(false);
				}
			}
			clustered.add(false);
			ArrayList<Boolean> indexed = new ArrayList<Boolean>();
			for (int i = 0; i < clustered.size(); i++) {
				indexed.add(false);
				
			}
			indexed.add(false);
			
			Table t = new Table(strTableName, columnNames, columnTypes, clustered, indexed, strClusteringKeyColumn,maxRows);
			// fix maxRows
			System.out.println("lets see");
			tables.put(strTableName, t);
			System.out.println(maxRows);
			insertIntoMetaData(t, true);
			
		}

	}

	public void insertIntoTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		boolean flag = false;

		java.util.Date date = new java.util.Date();
System.out.println("entered insert into tablemdbapp");
		htblColNameValue.put("TouchDate", date);

		Object[] tableNamesObj = (tables.keySet().toArray());
		String[] tableNames = new String[tableNamesObj.length];
		int j = 0;
		for (Object name : tableNamesObj) {
			tableNames[j] = (String) name;
			j++;
		}

		for (int i = 0; i < tableNames.length; i++) {
			if (tableNames[i].equals(strTableName)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new DBAppException("This table doens't exist");
		} else {

			Hashtable<String, Comparable> tempHash = new Hashtable<String, Comparable>();
			Set<String> keys = htblColNameValue.keySet();
			for (String key : keys) {
				tempHash.put(key, (Comparable) htblColNameValue.get(key));
			}
			Tuple t = new Tuple(tempHash, tables.get(strTableName).returnClusteredKey());
			tables.get(strTableName).insert(t);
		}
	}

	public void updateTable(String strTableName, String strClusteringKey, Hashtable<String, Object> htblColNameValue)
			throws DBAppException {
		//System.out.println("i entered update table el f dbapp");
		boolean flag = false;
		Object[] tableNamesObj = (tables.keySet().toArray());
		String[] tableNames = new String[tableNamesObj.length];
		int j = 0;
		for (Object name : tableNamesObj) {
			tableNames[j] = (String) name;
			j++;
		}
		for (int i = 0; i < tableNames.length; i++) {
			if (tableNames[i].equals(strTableName)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new DBAppException("This table doens't exist");
		} else {
			//System.out.println("AY 7AGA");
			Hashtable<String, Comparable> tempHash = new Hashtable<String, Comparable>();
			Set<String> keys = htblColNameValue.keySet();
			for (String key : keys) {
				tempHash.put(key, (Comparable) htblColNameValue.get(key));
			}
			Tuple t = new Tuple(tempHash, tables.get(strTableName).returnClusteredKey());
			tables.get(strTableName).updateTable(strClusteringKey, t);
		}

	}

	public void deleteFromTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {

		boolean flag = false;
		Object[] tableNamesObj = (tables.keySet().toArray());
		String[] tableNames = new String[tableNamesObj.length];
		int j = 0;
		for (Object name : tableNamesObj) {
			tableNames[j] = (String) name;
			j++;
		}
		for (int i = 0; i < tableNames.length; i++) {
			if (tableNames[i].equals(strTableName)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new DBAppException("This table doens't exist");
		} else {

			Hashtable<String, Comparable> tempHash = new Hashtable<String, Comparable>();
			Set<String> keys = htblColNameValue.keySet();
			for (String key : keys) {
				tempHash.put(key, (Comparable) htblColNameValue.get(key));
			}
			Tuple t = new Tuple(tempHash, tables.get(strTableName).returnClusteredKey());
			tables.get(strTableName).delete(t);
		}

	}

	/**
	 * 
	 * @param t      table to be added to the metadata.csv
	 * @param append whether to append it to the file (true) or to override the file
	 *               (false)
	 */
	public static void insertIntoMetaData(Table t, boolean append) {
		String tableName = t.getTableName();
		ArrayList<String> columnNames = t.getColumnNames();
		ArrayList<String> columnTypes = t.getColumnTypes();
		ArrayList<Boolean> clusteredColumns = t.getClusteredCoulmns();
		ArrayList<Boolean> indexedColumns = t.getIndexedCoulmns();
		String toBeInserted = "Table Name, Column Name, Column Type, ClusteringKey, Indexed";
		File file = new File("data/metadata.csv");
		try {
			FileWriter f = new FileWriter("data/metadata.csv", append);
			BufferedWriter bw = new BufferedWriter(f);
			bw.write(toBeInserted);
			bw.write("\n");
			for (int i = 0; i < columnNames.size(); i++) {
				toBeInserted = tableName + "," + columnNames.get(i) + "," + columnTypes.get(i) + ","
						+ clusteredColumns.get(i) + "," + indexedColumns.get(i);
				bw.write(toBeInserted);
				// line for testing
				// System.out.println("line " + (i) + " inserted");
				bw.write("\n");
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateMetaDataFile() {
		for (String tableName : tables.keySet()) {
			Table t = tables.get(tableName);
			insertIntoMetaData(t, false);

		}
	}

	public static void main(String[] args) {
		String strTableName = "Student";
		DBApp dbApp = new DBApp();
		Hashtable htblColNameType = new Hashtable();
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		try {
			dbApp.init();
			dbApp.createTable(strTableName, "id", htblColNameType);
			Hashtable htblColNameValue = new Hashtable();
			htblColNameValue.put("id", new Integer(1));
			htblColNameValue.put("name", new String("Ahmed Noor"));
			htblColNameValue.put("gpa", new Double(0.95));
			dbApp.insertIntoTable(strTableName, htblColNameValue);
			htblColNameValue.clear();
			htblColNameValue.put("id", new Integer(2));
			htblColNameValue.put("name", new String("Ahmed Noor"));
			htblColNameValue.put("gpa", new Double(0.95));
			dbApp.insertIntoTable(strTableName, htblColNameValue);
			htblColNameValue.clear();
			htblColNameValue.put("name", new String("Ahmed"));
			dbApp.updateTable(strTableName, "1", htblColNameValue);
		} catch (Exception e) {
			e.printStackTrace();
			
			
		}

	}
}
