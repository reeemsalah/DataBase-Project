import java.util.ArrayList;
import java.util.Hashtable;

public class DBApp {
	private static ArrayList<String> tableNames;

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
			
		}

	}
}
