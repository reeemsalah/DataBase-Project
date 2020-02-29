import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		Properties DBApp = new Properties();
		DBApp.put("MaximumRowsCountinPage", maxRows);
		DBApp.put("NodeSize", nodeSize);
	}

	public void createTable(String strTableName, String strClusteringKeyColumn,
			Hashtable<String, String> htblColNameType) throws DBAppException {
		boolean flag = false;

		if (tables.size() != 0) {
			String[] tableNames = (String[]) (tables.keySet().toArray());

			for (int i = 0; i < tableNames.length; i++) {
				if (tableNames[i].equals(strTableName)) {
					flag = true;
					break;
				}
			}
		}
		if (flag == true) {
			throw new DBAppException("This table already exists");
		} else {
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

			Table t = new Table(strTableName, columnNames, columnTypes, clustered, indexed, strClusteringKeyColumn,
					maxRows);
			tables.put(strTableName, t);
			insertIntoMetaData(t, true);

		}

	}

	public void insertIntoTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		boolean flag = false;
		
		java.util.Date date=new java.util.Date();  
		System.out.println(date);
		htblColNameValue.put("TouchDate",date ); 
		
		
		String[] tableNames = (String[]) (tables.keySet().toArray());
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

	// TODO Uncomment when updateTable is changed

	public void updateTable(String strTableName, String strClusteringKey, Hashtable<String, Object> htblColNameValue)
			throws DBAppException {

		boolean flag = false;
		String[] tableNames = (String[]) (tables.keySet().toArray());
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
			tables.get(strTableName).updateTable(strClusteringKey, t);
		}

	}

	public void deleteFromTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {

		boolean flag = false;
		String[] tableNames = (String[]) (tables.keySet().toArray());
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
		File file = new File("metadata.csv");
		try {
			FileWriter f = new FileWriter("metadata.csv", append);
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
			dbApp.createTable(strTableName, "id", htblColNameType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
