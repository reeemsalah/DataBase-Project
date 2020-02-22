import java.util.Vector;

public class Page {
	private int maxRows;
	private int currentRows=0;
	private Vector<Tuple> rows;
	 public Page (int maxRows)
	 {
		 this.maxRows=maxRows;
	 }
	/**
	 * 
	 * @param t the tuple to be inserted into the page
	 */
	 // TODO pass the row as an object from method insert into table
	 //TODO check in the insert into table first if the page is already full
	 public void insertRow(Tuple t)
	 {
		 currentRows++;
		 rows.add(t);
	 }
	/**
	 * 
	 * @return true if the page is full, false otherwise
	 */
	public boolean isFull()
	{
		return maxRows-currentRows==0;
	}
	/**
	 * 
	 * @param columnName the name of the column to search on
	 * @param columnValue the name of the value to look for
	 * @return true if we find a tuple on this page with this value, false otherwise
	 */
	public boolean exists(String columnName, Comparable columnValue)
	{
		for(Tuple t:rows)
		{
			if(t.getValueOfColumn(columnName).equals(columnValue))
				return true;
		}
		return false;
	}

}
