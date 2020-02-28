import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class Test2 {
	public static void main(String[] args) {
		/*
		 * Hashtable <Integer,String> tmp=new Hashtable<Integer ,String>(); tmp.put(1,
		 * "Hi"); tmp.put(2, "Hello"); System.out.println(tmp.keySet().size());
		 * tmp.remove(2); System.out.println(tmp.keySet().size());
		 */
		
		Vector<String> test=new Vector<String>();
		test.add("hi");
		test.add("hello");
		System.out.println(test.size());
		test.remove("hi");
		test.remove("hello");
		System.out.println(test.size());
			
	}

}
