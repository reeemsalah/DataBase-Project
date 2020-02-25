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
		Hashtable htblColNameValue = new Hashtable( );
		htblColNameValue.put("id", new Integer( 2343432 ));
		htblColNameValue.put("name", "Ahmed Noor"  );
		htblColNameValue.put("gpa", new Double( 0.98 ) );
	//	dbApp.insertIntoTable( strTableName , htblColNameValue );
		
	//	Tuple t= new Tuple(htblColNameValue);
	//	htblColNameValue.clear( );
		//Page p1 = new Page(10);
		//p1.insertInto(t);
		 
		Hashtable htblColNameValue2 = new Hashtable( );
		htblColNameValue2.put("id", new Integer( 2343432 ));
		htblColNameValue2.put("name", "ali Noor"  );
		htblColNameValue2.put("gpa", new Double( 0.95 ) );
	//	dbApp.insertIntoTable( strTableName , htblColNameValue ); 
		//new line 
		System.out.println("rien");
		
		//Tuple t2= new Tuple(htblColNameValue2);
	//	htblColNameValue2.clear( );
		
		
//		Page p1 = new Page(10);
		//p1.insertInto(t2);
		
		try
		{
		ObjectInputStream o = new
		ObjectInputStream(
		 new FileInputStream("file.ser"));
		// Note the typecasts below
		Page foo1 = (Page) o.readObject();
		System.out.println(foo1);
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
	//	System.out.println("p1"+p1);

		//System.out.println("t2"+t2);
		
	//System.out.print(p1.exists(t2));
//=======
	}
}
