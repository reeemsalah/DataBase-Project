import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;


@SuppressWarnings("serial")
public class Table implements Serializable
{
	//TODO values of the inserts needs to be added
	private String tableName;
	private Vector<Page> tablePages;
	private Vector<String> columnNames;
	private Vector<String> columnTypes;
	private boolean[] clusteredCoulmns;
	private boolean[] indexedCoulmns;
	private int numOfPages;
	private Vector<Object> columnValues;
	
	public Table() {
		this.numOfPages = tablePages.size();
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.clusteredCoulmns = clusteredCoulmns;
		this.columnTypes = columnTypes;
		this.indexedCoulmns = indexedCoulmns;
	}
	
	public void setName(String tableName) {
		this.tableName = tableName;
	}
	
	public void addToNames(String columnName) {
		this.columnNames.add(columnName);
	}
	
	public void addToTypes(String columnType) {
		this.columnTypes.add(columnType);
	}
	
	public void insert(Tuple t) {
		Iterator<Page> iterator = tablePages.iterator();
		int countPagesSoFar = 0;
		while(iterator.hasNext()) {
			if(iterator.next().isFull()) {
				countPagesSoFar++;
			}else {
				Page p = tablePages.get(countPagesSoFar);
				p.insertInto(t);
			}
		}
	}
	
	public Table returnATable(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	
	
}
