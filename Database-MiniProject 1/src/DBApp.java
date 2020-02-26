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
	private static ArrayList<String> tableNames;
	private static ArrayList<String> tableKeys;

	public void init() {}
	
	public void createTable(String strTableName, String strClusteringKeyColumn,
			Hashtable<String, String> htblColNameType) throws DBAppException {
		
		if(tableNames.contains(strTableName))
		{
			throw new DBAppException("Table Name Already exists");
			
		}
		else
		{
			tableNames.add(strTableName);
			tableKeys.add(strClusteringKeyColumn);
			
		
			
		}

//>>>>>>> branch 'master' of https://github.com/reeemsalah/DataBase-Project.git
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
