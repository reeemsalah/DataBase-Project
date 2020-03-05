import java.util.Hashtable;

public class DBAppTest {
	public static void main(String[] args) {
		try {
		String strTableName = "Student";
		DBApp dbApp = new DBApp();
		
		dbApp.init();
		
		Hashtable htblColNameType = new Hashtable();
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		dbApp.createTable(strTableName, "id", htblColNameType);
	
		Hashtable htblColNameValue = new Hashtable();
		htblColNameValue.put("id", new Integer(2343432));
		htblColNameValue.put("name", new String("Ahmed Noor"));
		htblColNameValue.put("gpa", new Double(0.95));
		dbApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(453455));
		htblColNameValue.put("name", new String("Ahmed Noor"));
		htblColNameValue.put("gpa", new Double(0.95));
		dbApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(5674567));
		htblColNameValue.put("name", new String("Dalia Noor"));
		htblColNameValue.put("gpa", new Double(1.25));
		dbApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(23498));
		htblColNameValue.put("name", new String("John Noor"));
		htblColNameValue.put("gpa", new Double(1.5));
		dbApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(78452));
		htblColNameValue.put("name", new String("Zaky Noor"));
		htblColNameValue.put("gpa", new Double(0.88));
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		
		htblColNameValue.clear();
		htblColNameValue.put("name", new String("Ahmed"));
		dbApp.updateTable(strTableName, "1", htblColNameValue);
		
		//htblColNameType.clear();
		//htblColNameType.put("name", new String ("John Noor"));
		//dbApp.deleteFromTable("Student", htblColNameValue);
		
		//dbApp.createTable("Student", "id", htblColNameType);
				
		}
		catch(DBAppException e )
		{
			System.out.println(e.getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
