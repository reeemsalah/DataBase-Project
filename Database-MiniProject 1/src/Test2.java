import java.io.FileReader;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class Test2 {
	public static void main(String[] args) {
		/*
		 * Hashtable <Integer,String> tmp=new Hashtable<Integer ,String>(); tmp.put(1,
		 * "Hi"); tmp.put(2, "Hello"); System.out.println(tmp.keySet().size());
		 * tmp.remove(2); System.out.println(tmp.keySet().size());
		 */
		
		/*
		 * Vector<String> test=new Vector<String>(); test.add("hi"); test.add("hello");
		 * System.out.println(test.size()); test.remove("hi"); test.remove("hello");
		 * System.out.println(test.size()); String s="hi";
		 * System.out.println(s.getClass().toString().equals("class java.lang.String"));
		 */
		try {
			FileReader reader=new FileReader("config/DBApp.properties");  
		      
		    Properties p=new Properties();  
		    p.load(reader);  
		      
		    System.out.println(p.getProperty("MaximumRowsCountinPage"));  
		    System.out.println(p.getProperty("NodeSize"));
			//Properties DBApp = new Properties();
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	

}
