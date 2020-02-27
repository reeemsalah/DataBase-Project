import java.util.Hashtable;

public class Test2 {
	public static void main(String[] args) {
		Hashtable <Integer,String> tmp=new Hashtable<Integer ,String>();
		tmp.put(1, "Hi");
		tmp.put(2, "Hello");
		System.out.println(tmp.keySet().size());
		tmp.remove(2);
		System.out.println(tmp.keySet().size());
	}

}
