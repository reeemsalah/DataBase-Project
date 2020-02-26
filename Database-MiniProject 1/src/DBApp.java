//<<<<<<< HEAD
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.*;

public class DBApp {
	//	private static ArrayList<String> tableNames;
	private static ArrayList<Table> tables;
	//	private static ArrayList<String> tableKeys;

	public void init() {}

	public void createTable(String strTableName, String strClusteringKeyColumn,
			Hashtable<String, String> htblColNameType) throws DBAppException {
		boolean flag = false;
		for(int i = 0;i<tables.size();i++) {
			if(tables.get(i).returnTableName().equals(strTableName)) {
				flag = true;
				break;
			}
		}
		if(flag == true) {
			throw new DBAppException("This table already exists");
		}else {
			ArrayList<String> columnNames = new ArrayList<String>();
			ArrayList<String> columnTypes = new ArrayList<String>();
			Set<String> names = htblColNameType.keySet();
			for(String key:names) {
				columnNames.add(key);
				columnTypes.add(htblColNameType.get(key));
			}
			ArrayList<Boolean> clustered = new ArrayList<Boolean>();
			for(int i = 0;i<columnNames.size();i++) {
				if(columnNames.equals(strClusteringKeyColumn)) {
					clustered.add(true);
				}else {
					clustered.add(false);
				}
			}
			ArrayList<Boolean> indexed = new ArrayList<Boolean>();
			for(int i = 0;i<clustered.size();i++) {
				clustered.add(false);
			}

			//Table t = new Table(strTableName,columnNames,columnTypes,clustered,indexed,strClusteringKeyColumn);
		}


		//>>>>>>> branch 'master' of https://github.com/reeemsalah/DataBase-Project.git
	}

	public void insertIntoTable(String strTableName,
			Hashtable<String,Object> htblColNameValue)
					throws DBAppException
	{
		Table temp = null;
		boolean flag = false;
		for(int i = 0;i<tables.size();i++) {
			if(tables.get(i).returnTableName().equals(strTableName)) {
				flag = true;
				temp = tables.get(i);
				break;
			}
		}
		if(flag == false) {
			throw new DBAppException("This table doens't exist");
		}else {
			
			Hashtable<String, Comparable> tempHash = new Hashtable<String, Comparable>();
			Set<String> keys = htblColNameValue.keySet();
			for(String key:keys) {
				tempHash.put(key, (Comparable) htblColNameValue.get(key));
			}
			Tuple t = new Tuple(tempHash,temp.returnClusteredKey());
			temp.insert(t);
		}
	}

	public static void main(String [] args) {
		//deserialisation start
		//try
		//{
		//ObjectInputStream o = new ObjectInputStream( new FileInputStream("file.ser"));
		//// Note the typecasts below
		//while(o!=null) {
		//Page foo1 = (Page) o.readObject();
		//System.out.println(foo1 +" des");
		//}
		//o.close();
		//
		//}
		//catch (ClassNotFoundException e)
		//{
		//System.out.println(e);
		//}
		//catch (FileNotFoundException e)
		//{
		//System.out.println(e);
		//}
		//catch (IOException e)
		//{
		//System.out.println(e);
		//
		//}	
		//deserialisation end
	}
}
