import java.util.Hashtable;

public class Test {
public static void main(String[] args) {
	Page p1=new Page(200,"test_delete_1");
	
	Hashtable htblColNameValue = new Hashtable();
	htblColNameValue.put("id", new Integer(2));
	
	Tuple t1=new Tuple(htblColNameValue,"id");
	Hashtable htblColNameValue1 = new Hashtable();
	htblColNameValue1.put("id", new Integer( 10)); 
	
	Tuple t2=new Tuple(htblColNameValue1,"id");
	p1.insertInto(t2);
	p1.insertInto(t1);
	System.out.println(t1.getKeyValue());
	System.out.println(t2.getKeyValue());
	System.out.println(p1);
	p1.deleteFrom(t2);
	System.out.println(p1);
	System.out.println(p1.exists(t1));
	System.out.println(p1.exists(t2));
}
}
