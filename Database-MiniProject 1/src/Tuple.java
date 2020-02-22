import java.io.Serializable;

public class Tuple implements Serializable{
	private int size;
	private Object[] columnValues;
	 
	public Tuple(int n)
	{
		this.size=n;
	}
	
	public void createTuple(Object [] columnValues)
	{
		this.columnValues=columnValues;
	}

}
