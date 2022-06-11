/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;


/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Cafe {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Cafe
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Cafe(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Cafe

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Cafe.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Cafe esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Cafe object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Cafe (dbname, dbport, user, "");
	 boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. Goto Menu");
                System.out.println("2. Update Profile");
                System.out.println("3. Place a Order");
                System.out.println("4. Update a Order");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                   case 1: Menu(esql, authorisedUser); break;
                   case 2: UpdateProfile(esql, authorisedUser); break;
                   case 3: PlaceOrder(esql); break;
                   case 4: UpdateOrder(esql, authorisedUser); break;
                   case 9: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
}//end Greeting

    /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
	    break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user with privided login, passowrd and phoneNum
    **/
   public static void CreateUser(Cafe esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         System.out.print("\tEnter user phone: ");
         String phone = in.readLine();
         
	    String type="Customer";
	    String favItems="";

	 String query = String.format("INSERT INTO USERS (phoneNum, login, password, favItems, type) VALUES ('%s','%s','%s','%s','%s')", phone, login, password, favItems, type);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Cafe esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE login = '%s' AND password = '%s'", login, password);
         int userNum = esql.executeQuery(query);
	 if (userNum > 0)
		return login;
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here

   public static void Menu(Cafe esql, String user){
      try{
          //print menu
	  Menu_PrintFullMenu(esql);

 	  //check user type
 	  String userType = null;
 	  String query = String.format("SELECT type FROM Users WHERE login='%s'", user);
 	  List<List<String>> result = esql.executeQueryAndReturnResult(query);
 	  userType = result.get(0).get(0);
          userType = userType.replaceAll("\\s+",""); 
	  boolean keepon = true;
	  while(keepon){
	     //Check what the user wants to do next
	     if(userType.equals("Customer")){
	        System.out.println("1. View menu");
		System.out.println("2. Search for an item");
	        System.out.println("3. Search for a type of item");
		System.out.println("9. Go to main menu");
	        switch (readChoice()){
                   case 1: Menu_PrintFullMenu(esql); break;
		   case 2: Menu_SearchItemName(esql); break;
                   case 3: Menu_SearchItemType(esql); break;
                   case 9: System.out.println("\n"); return;
                   default : System.out.println("Unrecognized choice!"); break;
                }
             }
             if(userType.equals("Manager")){
                System.out.println("1. View menu");
                System.out.println("2. Search for an item");
                System.out.println("3. Search for a type of item");
                System.out.println("4. Add/delete/modify item");
                System.out.println("9. Go to main menu");
                switch (readChoice()){
                   case 1: Menu_PrintFullMenu(esql); break;
                   case 2: Menu_SearchItemName(esql); break;
                   case 3: Menu_SearchItemType(esql); break;
                   case 4: Menu_AddDeleteModifyItem(esql); break;
                   case 9: System.out.println("\n"); return;
                   default : System.out.println("Unrecognized choice!"); break;
                }
             }

         }


      }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
   }


   public static void Menu_PrintFullMenu(Cafe esql){
      try{
          System.out.println(
          "\n\n*******************************************************\n" +
          "                        Menu                               \n" +
          "***********************************************************\n");
          //print out the menu
             String query = "SELECT * FROM MENU";
                int success = esql.executeQueryAndPrintResult(query);
          System.out.println("***********************************************************\n");



      }catch(Exception e){
         System.err.println (e.getMessage ());
      }

  }

   public static void Menu_SearchItemName(Cafe esql){
      try{
         System.out.print("\tItem: ");
         String itemName = in.readLine();
         String query = String.format("SELECT * FROM Menu WHERE itemName='%s'", itemName);
         System.out.println("***********************************************************\n");
         int rows = esql.executeQueryAndPrintResult(query);
         System.out.println("***********************************************************\n");
         if(rows == 0){
	    System.out.format("No item named %s\n", itemName);
         }
      System.out.println("\n");
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
   }

   public static void Menu_SearchItemType(Cafe esql){
      try{
         System.out.print("\tType: ");
         String type = in.readLine();
         String query = String.format("SELECT * FROM Menu WHERE type='%s'", type);
         System.out.println("***********************************************************\n");
         int rows = esql.executeQueryAndPrintResult(query);
         System.out.println("***********************************************************\n");
         if(rows == 0){
            System.out.format("No items of type: %s\n", type);
         }
         System.out.println("\n");

      }catch(Exception e){
         System.err.println (e.getMessage ());
      }

  }
   public static void Menu_AddDeleteModifyItem(Cafe esql){
      try{
         System.out.println("1. Add item");
         System.out.println("2. Delete item");
         System.out.println("3. Modify item");
         System.out.println("9. Go to main menu");
         switch (readChoice()){
            case 1: System.out.print("Name: ");
		    String itemName1 = in.readLine();
  		    System.out.print("\nType: ");
		    String type1 = in.readLine();
		    System.out.print("\nPrice: ");
		    String price1 = in.readLine();
   		    System.out.print("\nDescription: ");
		    String description1 = in.readLine();
		    System.out.print("\nimageURL: ");
                    String imageUrl1 = in.readLine();
		    String query1 = String.format("INSERT INTO Menu VALUES ('%s', '%s', %s, '%s', '%s')", itemName1, type1, price1, description1, imageUrl1);
		    esql.executeUpdate(query1);
		    break;
	   case 2: System.out.print("\tItem to remove: ");
                    String itemName = in.readLine();
                    String query2 = String.format("DELETE FROM Menu WHERE itemName='%s'", itemName);
                    esql.executeUpdate(query2);
                    break;
            case 3: System.out.print("Item to modify: ");
		    String itemName3 = in.readLine();
		    System.out.println("What to modify?");
		    System.out.println("1. Name");
	            System.out.println("2. Type");
         	    System.out.println("3. Price");
        	    System.out.println("4. Description");
   		    System.out.println("5. imageURL");
		    String query3;
		    switch (readChoice()){
			case 1: System.out.print("Name: ");
				String itemNameNew = in.readLine();
				query3 = String.format("UPDATE Menu SET itemName = '%s' WHERE itemName = '%s'", itemNameNew, itemName3);
				esql.executeUpdate(query3);
				break;
 			case 2: System.out.print("Type: ");
                                String typeNew = in.readLine();
                                query3 = String.format("UPDATE Menu SET type = '%s' WHERE itemName = '%s'", typeNew, itemName3);
                                esql.executeUpdate(query3);
                                break;

			case 3: System.out.print("Price: ");
                                String priceNew = in.readLine();
                                query3 = String.format("UPDATE Menu SET price = %s WHERE itemName = '%s'", priceNew, itemName3);
                                esql.executeUpdate(query3);
                                break;

			case 4: System.out.print("Description: ");
                                String descriptionNew = in.readLine();
                                query3 = String.format("UPDATE Menu SET description = '%s' WHERE itemName = '%s'", descriptionNew, itemName3);
                                esql.executeUpdate(query3);
                                break;

			case 5: System.out.print("ImageURL: ");
                                String imageURLNew = in.readLine();
                                query3 = String.format("UPDATE Menu SET ImageURL = '%s' WHERE itemName = '%s'", imageURLNew, itemName3);
                                esql.executeUpdate(query3);
                                break;

		    }
			
		     break;
            case 9: System.out.println("\n"); return;
            default : System.out.println("Unrecognized choice!"); break;
         }

      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }  
   public static void UpdateProfile(Cafe esql, String user){
      try{
	  //check user type
	  String userType = null;
          String query = String.format("SELECT type FROM Users WHERE login='%s'", user);
          List<List<String>> result = esql.executeQueryAndReturnResult(query);
          userType = result.get(0).get(0);
          userType = userType.replaceAll("\\s+","");
	  
	  if(userType.equals("Manager")){ 
	      System.out.print("User to modify: ");
              user = in.readLine();
              System.out.println("What to modify?");
                    System.out.println("1. Login");
                    System.out.println("2. Phone Number");
                    System.out.println("3. Password");
                    System.out.println("4. Favorite Items");
                    System.out.println("5. type");
                    switch (readChoice()){
                        case 1: System.out.print("Login: ");
                                String loginNew = in.readLine();
                                query = String.format("UPDATE Users SET login = '%s' WHERE login = '%s'", loginNew, user);
                                esql.executeUpdate(query);
                                break;
                        case 2: System.out.print("phoneNum: ");
                                String phoneNumNew = in.readLine();
                                query = String.format("UPDATE Users SET phoneNum = '%s' WHERE login = '%s'", phoneNumNew, user);
                                esql.executeUpdate(query);
                                break;

                        case 3: System.out.print("Password: ");
                                String passNew = in.readLine();
                                query = String.format("UPDATE Users SET password = '%s' WHERE login = '%s'", passNew, user);
                                esql.executeUpdate(query);
                                break;

                        case 4: System.out.print("FavItems: ");
                                String fINew = in.readLine();
                                query = String.format("UPDATE Users SET favItems = '%s' WHERE login = '%s'", fINew, user);
                                esql.executeUpdate(query);
                                break;

                        case 5: System.out.print("Type: ");
                                String typeNew = in.readLine();
                                query = String.format("UPDATE Users SET type = '%s' WHERE login = '%s'", typeNew, user);
                                esql.executeUpdate(query);
                                break;

                    }
	}
        else{
		    System.out.println("What to modify?");
                    System.out.println("1. Login");
                    System.out.println("2. Phone Number");
                    System.out.println("3. Password");
                    System.out.println("4. Favorite Items");
                    System.out.println("5. type");
                    switch (readChoice()){
                        case 1: System.out.print("Login: ");
                                String loginNew = in.readLine();
                                query = String.format("UPDATE Users SET login = '%s' WHERE login = '%s'", loginNew, user);
                                esql.executeUpdate(query);
                                break;
                        case 2: System.out.print("phoneNum: ");
                                String phoneNumNew = in.readLine();
                                query = String.format("UPDATE Users SET phoneNum = '%s' WHERE login = '%s'", phoneNumNew, user);
                                esql.executeUpdate(query);
                                break;

                        case 3: System.out.print("Password: ");
                                String passNew = in.readLine();
                                query = String.format("UPDATE Users SET password = '%s' WHERE login = '%s'", passNew, user);
                                esql.executeUpdate(query);
                                break;

                        case 4: System.out.print("FavItems: ");
                                String fINew = in.readLine();
                                query = String.format("UPDATE Users SET favItems = '%s' WHERE login = '%s'", fINew, user);
                                esql.executeUpdate(query);
                                break;

                        case 5: System.out.print("Type: ");
                                String typeNew = in.readLine();
                                query = String.format("UPDATE Users SET type = '%s' WHERE login = '%s'", typeNew, user);
                                esql.executeUpdate(query);
                                break;

                    }


	}

      }catch(Exception e){
         System.err.println (e.getMessage ());
      }

   }

   public static void PlaceOrder(Cafe esql){
     try{
	String query = String.format("SELECT * FROM Orders O");
	int row = esql.executeQuery(query);
	System.out.println("Enter the customer login:");
	String login1 = in.readLine();
	String query1 = String.format("SELECT * FROM Users U WHERE U.login = '%s'",login1);
	int row1 = esql.executeQuery(query1);
	if (row1 == 0) {
	   System.out.println("Non existent login!\n");
	   return;
	}
	System.out.println("Browse order history of user? (Last 5 purchases)");
	System.out.println("1:Yes");
	System.out.println("2:No");
	switch(readChoice()) {
	   case 1: String qq = String.format("SELECT * FROM Orders WHERE login = '%s' ORDER BY timeStampRecieved LIMIT 5", login1);
		   int Hist = esql.executeQueryAndPrintResult(qq);
		   System.out.println("Abort Order?(1:Yes)");
		   switch(readChoice()) {
			case 1:return;
			default:break;
		   }
		   break;
	   case 2: break;
	   default: break;
	}
	System.out.println("Enter pay status(1 for paid, 0 for unpaid)");
	boolean paid = false;
	switch (readChoice()) {
	   case 0: paid = false;
		   break;
	   case 1: paid = true;
		   break;
	   default: System.out.println("Invalid input!");
		   break;
	}
	Timestamp ts = new Timestamp(System.currentTimeMillis());
	System.out.println("Enter total");
	String price = in.readLine();
	String inp = String.format("INSERT INTO Orders (orderid, login, paid, timeStampRecieved, total) VALUES (%d,'%s','%s','%s',%s)", row + 1, login1, paid, ts, price);
	esql.executeUpdate(inp);
	System.out.println("Order inputted\n\n");
     }catch(Exception e){
	System.err.println(e.getMessage());
     }
  }

  public static void UpdateOrder(Cafe esql, String user){
     try{
	String userType = null;
	String q = String.format("SELECT type FROM Users WHERE login = '%s'", user);
	List<List<String>> result = esql.executeQueryAndReturnResult(q);
	userType = result.get(0).get(0);
	if(!userType.equals("Customer")){
	   System.out.println("Output all unpaid orders from <= 24 hours?");
	   System.out.println("1:Yes");
	   switch(readChoice()) {
		case 1: orderHist(esql);
			break;
		default:break;
	   }
	}
	System.out.println("Input the order ID to update");
	String order = in.readLine();
	int id = Integer.parseInt(order);
	String findOrder = String.format("SELECT * FROM Orders O");
	int row = esql.executeQuery(findOrder);
	if (id > row || id < 1) {
	   System.out.println("Invalid order ID!\n\n");
	   return;
	}
	if (userType.equals("Customer")) {
	   updatePaidCustomer(esql, order);
	}
	else {
	   
	   updatePaid(esql, order);
	}
     }catch(Exception e){
	System.err.println(e.getMessage());
     }
  }

 public static void updatePaidCustomer(Cafe esql, String id) {
    try{
	String q = String.format("SELECT O.paid FROM Orders O WHERE O.orderid = '%s'",id);
	List<List<String>> p = esql.executeQueryAndReturnResult(q);
	char c = p.get(0).get(0).charAt(0);
	if (c == 't') {
	   System.out.println("Cannot modify this order!");
	   return;
	}
	System.out.println("Modify Paid? (1 if yes)");
	switch(readChoice()) {
	   case 1: String query = String.format("UPDATE Orders SET paid = 'true' WHERE orderid = %s", id);
		   esql.executeUpdate(query);
		   System.out.printf("Order %s has been updated to be paid\n\n", id);
		   break;
	   default: break;
	}
    }catch(Exception e) {
	System.err.println(e.getMessage());
    }
 }

 public static void updatePaid(Cafe esql, String id) {
    try{
	System.out.println("Please input 1 for paid or 2 for unpaid");
	switch(readChoice()) {
	   case 1: String query = String.format("UPDATE Orders SET paid = 'true' WHERE orderid = %s", id);
		   esql.executeUpdate(query);
		   System.out.printf("Order %s has been updated to be paid\n\n", id);
		   break;
	   case 2: String query1 = String.format("UPDATE Orders SET paid = 'false' WHERE orderid = %s", id);
		   esql.executeUpdate(query1);
		   System.out.printf("Order %s has been updated to be unpaid\n\n", id);
		   break;
	   default: System.out.println("Invalid option\n\n");
		   break;
	}
   }catch(Exception e){
	System.err.println(e.getMessage());
   }
 }

 public static void orderHist(Cafe esql) {
    try{
	Timestamp ts = new Timestamp(System.currentTimeMillis());
	String query = String.format("SELECT * FROM Orders WHERE timeStampRecieved >= NOW() - '1 day'::INTERVAL");
	int rows = esql.executeQueryAndPrintResult(query);
    }catch(Exception e) {
	System.err.println(e.getMessage());
    }
 }



}//end Cafe

