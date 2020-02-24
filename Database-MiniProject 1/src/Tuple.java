import java.io.Serializable;
import java.util.Hashtable;

public class Tuple implements Serializable, Comparable{
	//private int size;
	private transient Comparable  key;
	@SuppressWarnings("rawtypes")
	private Hashtable <String,Comparable>attributes;
	 
//	public Tuple(int n)
//	{
//		this.size=n;
//	}
	
	/**
	 * 
	 * @param columnValues creates a tuple with these column names and values
	 */
	public  Tuple(Hashtable<String,Comparable> columnValues)
	{
		this.attributes=columnValues;
		this.key =attributes.keys().nextElement();//temporary fix
	}
	/**
	 * 
	 * @return the attributes and values of the tuple
	 */
	public Hashtable<String, Comparable> getAttributes() {
		return attributes;
	}
	
	
	public Comparable getKey() {
		return key;
	}

	/**
	 * 
	 * @return the key value of this
	 */
	public Comparable getKeyValue()
	{
		return this.getAttributes().get(key);
	}
	
	
	/**
	 * 
	 * @param o the Object to be compared to this
	 * @return 0 if their keys are equal
	 *         pos if this.key is greater than o.key
	 *         neg if this.key is smaller than o.key
	 */
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Tuple o1=(Tuple)o;
		Comparable id2=o1.getKeyValue();
		Comparable id1=this.getKeyValue();
		
		return id1.compareTo(id2);
	}
	
	
	/**
	 * 
	 * @param columnName name of the column to be edited
	 * @param newValue the new value to be inserted in the column
	 */
	public void edit(String columnName, Comparable newValue)
	{
		this.getAttributes().replace(columnName, newValue);
	}
	
	/**
	 * 
	 * @param columnName the name of the column that we want to get its data
	 * @return the data in the column
	 */
	public Comparable getValueOfColumn(String columnName)
	{
		return this.getAttributes().get(columnName);
	}

	
	public String toString() {
		
		return (this.attributes).toString();
	}
}
