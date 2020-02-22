import java.io.Serializable;
import java.util.Hashtable;

public class Tuple implements Serializable, Comparable{
	private int size;
	private String key;
	@SuppressWarnings("rawtypes")
	private Hashtable <String,Comparable>attributes;
	 
	public Tuple(int n)
	{
		this.size=n;
	}
	
	public void createTuple(Hashtable<String,Comparable> columnValues)
	{
		this.attributes=columnValues;
	}

	public Hashtable<String, Comparable> getAttributes() {
		return attributes;
	}
	
	
	//return the key value of an object
	public Comparable getKeyValue()
	{
		return this.getAttributes().get(key);
	}
	
	
	//if the key of the object that the method is invoked on is greater than the key of Object o
	//return a pos number, if it was less than the key of Object o return neg number
	// otherwise means that it is equal return 0
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Tuple o1=(Tuple)o;
		Comparable id2=o1.getKeyValue();
		Comparable id1=this.getKeyValue();
		
		return id1.compareTo(id2);
	}
	
	
	//takes the column name and changes its content
	public void edit(String columnName, Comparable newValue)
	{
		this.getAttributes().replace(columnName, newValue);
	}
	
	//returns the value of some column
	public Comparable getValueOfColumn(String columnName)
	{
		return this.getAttributes().get(columnName);
	}

}
