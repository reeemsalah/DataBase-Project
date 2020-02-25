import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class Page implements Serializable {
	private int maxRows;
	private int currentRows = 0;
	private Comparable maxKey;
	private Comparable minKey;
	private String fileName;

	public Page(int maxRows, String fileName) {
		this.maxRows = maxRows;
		this.fileName = fileName;
	}

	/**
	 * 
	 * @param t the tuple to be inserted into the page
	 */
	// TODO pass the row as an object from method insert into table
	// TODO check in the insert into table first if the page is already full
	public void insertInto(Tuple t) {

		currentRows++;

		String filename = this.fileName;

		// Serialization
		try { // Reading the object from a file
			FileInputStream file1 = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file1);

			// Method for deserialization of object
			Vector<Tuple> rows = (Vector<Tuple>) in.readObject();

			in.close();
			file1.close();
			if (rows.size() > 0) {
				Iterator it = rows.iterator();
				int i = 0;
				while (it.hasNext()) {
					Tuple tmp = (Tuple) it.next();
					if (tmp.compareTo(t) < 0) {
						rows.insertElementAt(t, i);
					}
					i++;
				}
			} else {
				rows.insertElementAt(t, 0);
			}

			// Saving of object in a file
			FileOutputStream file2 = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file2);

			// Method for serialization of object
			out.writeObject(this);

			out.close();
			file2.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param t the Tuple to be deleted from the page
	 */

	public void deleteFrom(Tuple t) {
		currentRows--;
		if (this.isEmpty()) {
			this.delete();
		} else {
			String filename = this.fileName;

			// Serialization
			try { // Reading the object from a file
				FileInputStream file1 = new FileInputStream(filename);
				ObjectInputStream in = new ObjectInputStream(file1);

				// Method for deserialization of object
				Vector<Tuple> rows = (Vector<Tuple>) in.readObject();

				in.close();
				file1.close();

				Iterator it = rows.iterator();
				int i = 0;
				while (it.hasNext()) {
					Tuple tmp = (Tuple) it.next();
					if (tmp.compareTo(t) == 0) {
						rows.remove(i);
					}
					i++;

				}

				// Saving of object in a file
				FileOutputStream file2 = new FileOutputStream(filename);
				ObjectOutputStream out = new ObjectOutputStream(file2);

				// Method for serialization of object
				out.writeObject(this);

				out.close();
				file2.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param t      the updated tuple
	 * @param strKey the key for the tuple
	 */

	public void update(Tuple t, String strKey) {

		try { // Reading the object from a file
			FileInputStream file1 = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file1);

			// Method for deserialization of object
			Vector<Tuple> rows = (Vector<Tuple>) in.readObject();

			in.close();
			file1.close();

			Iterator it = rows.iterator();
			int i = 0;
			while (it.hasNext()) {
				Tuple tmp = (Tuple) it.next();
				if (tmp.compareTo(t) == 0) {
					rows.remove(i);
				}
				i++;

			}

			// Saving of object in a file
			FileOutputStream file2 = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(file2);

			// Method for serialization of object
			out.writeObject(this);

			out.close();
			file2.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @return true if the page is full, false otherwise
	 */
	public boolean isFull() {
		return maxRows - currentRows == 0;
	}

	/**
	 * 
	 * @param t the tuple to find in the page
	 * @return true if it is found, false otherwise
	 */
	public boolean exists(Tuple t) {
		boolean flag=false;
		try { // Reading the object from a file
			FileInputStream file1 = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file1);

			// Method for deserialization of object
			Vector<Tuple> rows = (Vector<Tuple>) in.readObject();

			in.close();
			file1.close();
			
				Iterator it = rows.iterator();
				int i = 0;
				while (it.hasNext()) {
					Tuple tmp = (Tuple) it.next();
					if (tmp.compareTo(t) == 0) {
						flag=true;
					}
					i++;
			
			}

			// Saving of object in a file
			FileOutputStream file2 = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(file2);

			// Method for serialization of object
			out.writeObject(this);

			out.close();
			file2.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/*
	 * public String toString() { // String res=""; // Iterator<Tuple> it
	 * =rows.iterator(); // while(it.hasNext()) { // res+= (it.next()).toString();}
	 * // return res; return rows.toString(); }
	 */
	/**
	 * 
	 * @param keyValue the key value of the tuple to be insetred
	 * @return
	 */
	public boolean canBeInserted(Comparable keyValue) {
		return (keyValue.compareTo(minKey) >= 0) && (keyValue.compareTo(maxKey) >= 0) && !this.isFull();
	}

	public boolean isEmpty() {
		return currentRows == 0;
	}

	public void delete() {
		File file = new File(fileName);
		file.delete();
	}
}
