import java.util.Hashtable;

public class Test {
public static void main(String[] args) {
	Hashtable htblColNameValue5 = new Hashtable( );
	htblColNameValue5.put("id", new Integer( 2343422 ));
	htblColNameValue5.put("name", "Ahmed Noor"  );
	htblColNameValue5.put("gpa", new Double( 0.98 ) );
//	dbApp.insertIntoTable( strTableName , htblColNameValue );
	
	Tuple t5= new Tuple(htblColNameValue5, "id");
//	htblColNameValue.clear( );

	 
	Hashtable htblColNameValue4 = new Hashtable( );
	htblColNameValue4.put("id", new Integer( 2343430 ));
	htblColNameValue4.put("name", "ali Noor"  );
	htblColNameValue4.put("gpa", new Double( 0.95 ) );
//	dbApp.insertIntoTable( strTableName , htblColNameValue ); 
	//new line 
	System.out.println("rien");
	
	Tuple t4= new Tuple(htblColNameValue4, "id");
//	htblColNameValue2.clear( );
	
	
	Page p1=new Page(200,"test_delete_1");

	
	
	 
		Hashtable htblColNameValue3 = new Hashtable( );
		htblColNameValue3.put("id", new Integer( 2343432 ));
		htblColNameValue3.put("name", "omar Noor"  );
		htblColNameValue3.put("gpa", new Double( 0.95 ) );
	//	dbApp.insertIntoTable( strTableName , htblColNameValue ); 
		//new line 
		
		Tuple t3= new Tuple(htblColNameValue3, "id");
	//	htblColNameValue2.clear( );
		
		
		Hashtable htblColNameValue = new Hashtable();
		htblColNameValue.put("id", new Integer(2));
		
		Tuple t1=new Tuple(htblColNameValue,"id");
		
		
		Hashtable htblColNameValue1 = new Hashtable();
		htblColNameValue1.put("id", new Integer( 10)); 
		
		Tuple t2=new Tuple(htblColNameValue1,"id");

		
		
//		Page p1 = new Page(10);
		p1.insertInto(t4);
		p1.insertInto(t3);
		p1.insertInto(t5);
		p1.insertInto(t2);
		p1.insertInto(t1);


		


	
	
	//System.out.println(p1.rows.size());		
	
//	System.out.println(p1.rows.contains(t));
//	System.out.println(p1.rows.contains(t2));
//	System.out.println(p1.rows.contains(t3));
//	
//=======
		System.out.println("p1 "+p1);

//		System.out.println("t1"+t);
//		System.out.println("t2"+t2);
//		System.out.println("p1 "+p1.rows);
		
		System.out.println(t5.getKeyValue());
		System.out.println(t4.getKeyValue());
		System.out.println(t3.getKeyValue());
		
		System.out.print(p1.exists(t5));
		System.out.print(p1.exists(t4));
		System.out.println(p1.exists(t3));

	

	System.out.println(t1.getKeyValue());
	System.out.println(t2.getKeyValue());
	System.out.println(p1);
	
	p1.deleteFrom(t2);
	System.out.println(p1);

	p1.deleteFrom(t1);
	System.out.println(p1);

	p1.deleteFrom(t5);
	System.out.println(p1);

	p1.deleteFrom(t4);
	System.out.println(p1);

	p1.deleteFrom(t3);

	System.out.println(p1);
	System.out.println(p1.exists(t1));
	System.out.println(p1.exists(t2));
}
}
