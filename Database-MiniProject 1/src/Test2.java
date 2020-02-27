import java.util.Date;
import java.util.Hashtable;

public class Test2 {
	public static void main(String[] args) {
		/*
		 * Hashtable <Integer,String> tmp=new Hashtable<Integer ,String>(); tmp.put(1,
		 * "Hi"); tmp.put(2, "Hello"); System.out.println(tmp.keySet().size());
		 * tmp.remove(2); System.out.println(tmp.keySet().size());
		 */
		
			double x1=2.5;
			String x2="2.5";
			String x3=""+2.5;
			System.out.println(x2.equals(x3));
			Integer x4=1;
			String x5=x4.toString();
			String x6="1";
			System.out.println(x5.equals(x5));
			Date d= new Date();
			String x7=d.toString();
			String x8="Thu Feb 27 18:58:04 EET 2020";
			System.out.println(x7);
			
			
	}

}
