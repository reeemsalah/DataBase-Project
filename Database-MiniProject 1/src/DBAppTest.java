import java.util.Hashtable;

public class DBAppTest {
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
