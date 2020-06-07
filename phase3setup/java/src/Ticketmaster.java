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
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
// Additional imports
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.math.BigInteger; 
import java.nio.charset.StandardCharsets; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;




/*
 * String character counter for verification
 */
class CountCharacter    
{    
    public static boolean cnt_str(String val, int max) {    
        int count = 0;    
            
        //Counts each character except space    
        for(int i = 0; i < val.length(); i++) {    
            if(val.charAt(i) != ' ')    
                count++;    
        }    
            
        //Displays the total number of characters present in the given string    
        //System.out.println("Total number of characters in a string: " + count);

		// Check for validity
		if(count <= max)
		{
			return true;
		}
		else
		{
			return false;
		}
    }    
}    


/*
 * This class allows for the sha hashing of a string.
 */

class GFG { 
	public static byte[] getSHA(String input) throws NoSuchAlgorithmException 
	{ 
		// Static getInstance method is called with hashing SHA 
		MessageDigest md = MessageDigest.getInstance("SHA-256"); 

		// digest() method called 
		// to calculate message digest of an input 
		// and return array of byte 
		return md.digest(input.getBytes(StandardCharsets.UTF_8)); 
	} 
	
	public static String toHexString(byte[] hash) 
	{ 
		// Convert byte array into signum representation 
		BigInteger number = new BigInteger(1, hash); 

		// Convert message digest into hex value 
		StringBuilder hexString = new StringBuilder(number.toString(16)); 

		// Pad with leading zeros 
		while (hexString.length() < 32) 
		{ 
			hexString.insert(0, '0'); 
		} 

		return hexString.toString(); 
	} 

	// Driver code 
	public static String convert(String input, String verif) 
	{ 
		String ret_val = "";
		String temp = "";
		int count = 0;
		int sub = 0;

		try
		{ 
			//System.out.println("\n" + input + " : " + toHexString(getSHA(input))); // Password hash
			//System.out.println("\n" + verif + " : " + toHexString(getSHA(verif))); // Verify password
			
			// Compare and return
			if(new String(toHexString(getSHA(input))).equals(toHexString(getSHA(verif))))
			{
				ret_val = toHexString(getSHA(input));
				            
				//Counts each character except space    
				for(int i = 0; i < ret_val.length(); i++) {    
					if(ret_val.charAt(i) != ' ')    
						count++;    
				}  
						
				if(count < 64)
				{
					sub = (64 - count);	// Find range off from 64
					
					// concate random letters to make 64-chars string
					for(int i = 0; i <= sub; i++)
					{
						ret_val += 'a';	// Add as many 'a's to make 64-chars
					}
				}
						
				return ret_val;
			}
			else
			{
				ret_val = "mismatch";
				return ret_val;
			}
		} 
		// For specifying wrong message digest algorithms 
		catch (NoSuchAlgorithmException e) { 
			System.out.println("Exception thrown for incorrect algorithm: " + e); 
		} 
		
		return "mismatch";
	} 
} 

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class Ticketmaster{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public Ticketmaster(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");
			
			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}
	
	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
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
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;
		
		//iterates through the result set and output them to standard out.
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
	}
	
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
		//creates a statement object 
		Statement stmt = this._connection.createStatement (); 
		
		//issues the query instruction 
		ResultSet rs = stmt.executeQuery (query); 
	 
		/*
		 * obtains the metadata object for the returned result set.  The metadata 
		 * contains row and column info. 
		*/ 
		ResultSetMetaData rsmd = rs.getMetaData (); 
		int numCol = rsmd.getColumnCount (); 
		int rowCount = 0; 
	 
		//iterates through the result set and saves the data returned by the query. 
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
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
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
		if (rs.next()) return rs.getInt(1);
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
				"Usage: " + "java [-classpath <classpath>] " + Ticketmaster.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if
		
		Ticketmaster esql = null;
		
		try{
			System.out.println("(1)");
			
			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}
			
			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			
			esql = new Ticketmaster (dbname, dbport, user, "");
			
			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add User");
				System.out.println("2. Add Booking");
				System.out.println("3. Add Movie Showing for an Existing Theater");
				System.out.println("4. Cancel Pending Bookings");
				System.out.println("5. Change Seats Reserved for a Booking");
				System.out.println("6. Remove a Payment");
				System.out.println("7. Clear Cancelled Bookings");
				System.out.println("8. Remove Shows on a Given Date");
				System.out.println("9. List all Theaters in a Cinema Playing a Given Show");
				System.out.println("10. List all Shows that Start at a Given Time and Date");
				System.out.println("11. List Movie Titles Containing \"love\" Released After 2010");
				System.out.println("12. List the First Name, Last Name, and Email of Users with a Pending Booking");
				System.out.println("13. List the Title, Duration, Date, and Time of Shows Playing a Given Movie at a Given Cinema During a Date Range");
				System.out.println("14. List the Movie Title, Show Date & Start Time, Theater Name, and Cinema Seat Number for all Bookings of a Given User");
				System.out.println("15. EXIT");
				
				/*
				 * FOLLOW THE SPECIFICATION IN THE PROJECT DESCRIPTION
				 */
				switch (readChoice()){
					case 1: AddUser(esql); break;
					case 2: AddBooking(esql); break;
					case 3: AddMovieShowingToTheater(esql); break;
					case 4: CancelPendingBookings(esql); break;
					case 5: ChangeSeatsForBooking(esql); break;
					case 6: RemovePayment(esql); break;
					case 7: ClearCancelledBookings(esql); break;
					case 8: RemoveShowsOnDate(esql); break;
					case 9: ListTheatersPlayingShow(esql); break;
					case 10: ListShowsStartingOnTimeAndDate(esql); break;
					case 11: ListMovieTitlesContainingLoveReleasedAfter2010(esql); break;
					case 12: ListUsersWithPendingBooking(esql); break;
					case 13: ListMovieAndShowInfoAtCinemaInDateRange(esql); break;
					case 14: ListBookingInfoForUser(esql); break;
					case 15: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if				
			}catch(Exception e){
				// ignored.
			}
		}
	}

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
	
	public static void AddUser(Ticketmaster esql){//1
		String fn = "";	// First name
		String ln = "";	// Last name
		String email = "";	// Email Address
		String pwd = "";	// Password
		String ver_pwd_str = "";        // Verify Password - string
		String verif_pw_save = "mismatch";
		String query = "";
		long phone = 0;
		boolean em_val = false;
		boolean pn_val = false;
		boolean char_limit = false;

		CountCharacter b = new CountCharacter();	// Initialize class for counting string
		
		// Create space and initialize String
		System.out.println("\n\n\n");
		Scanner scan = new Scanner(System.in);

		// Space
		System.out.println("\n\n");
		
		while(char_limit == false)
		{
			// Get user first name
			System.out.print("Enter your first name: ");
			fn = scan.nextLine();
			
			char_limit = b.cnt_str(fn, 32);	// Test length
		}
		
		char_limit = false;	// Prep for last name bound check
		
		// Get user last name
		while(char_limit == false)
		{
			System.out.print("Enter your last name: ");
			ln = scan.nextLine();
		
			char_limit = b.cnt_str(ln, 32);	// Test length
		}
		
		char_limit = false;	// Prep for last name bound check

		// Get user email
		while(em_val == false || char_limit == false)
		{
			System.out.print("Enter your email address: ");
			email = scan.nextLine();
			
			// Test email
			em_val = valEmail(email);
			
			char_limit = b.cnt_str(email, 64);	// Test length
		}

		// Get user phone number
		while(pn_val == false)
		{
			System.out.print("Enter your phone number (no spaces or dashes): ");
			String str_phone = scan.nextLine();
			
			// Try and catch no entry for phone number
			if(str_phone == "")
			{
				str_phone = "0";
			}
			
			phone = Long.parseLong(str_phone);	// Convert to number for parameter passing

			// Test phone number - # of digits
			pn_val = valPhone(phone);
			
			if(phone == 0)
			{
				System.out.println("User PHONE NUMBER has been skipped!");
			}
			
			//System.out.println("Valid phone number: " + String.valueOf(pn_val));
		}
		
		// Get user password
		while(verif_pw_save == "mismatch")
		{
			// Get a pwd
			System.out.print("Please enter a password: ");
			pwd = scan.nextLine();
			
			// Verify pwd
			System.out.print("Please verify your password: ");
			ver_pwd_str = scan.nextLine();
			
			GFG a = new GFG();
			verif_pw_save = a.convert(pwd, ver_pwd_str);
			
			//System.out.println("\nPassword ret: " + verif_pw_save); // Password hash

			if(verif_pw_save == "mismatch")
			{
				System.out.print("\nPasswords DO NOT match!\n\n");
			}
		}

		// Concatenate all data together into query format for writing new user to file
		// -- SQL Statement for Insertion -- INSERT INTO Users {email, lname, fname, phone, pwd} VALUES (email, ln, fn, phone, verif_pw_save);
		try
		{
			query = "INSERT INTO Users (email, lname, fname, phone, pwd) VALUES ('" + email + "', '" + ln + "', '" + fn + "', '" + phone + "', '" + verif_pw_save + "'	);";
			//executeUpdate(SQL_INSERT); // Add new user.
			
			esql.executeUpdate(query);		

			// Space
			clear();

			// Success message
			System.out.println("\n\nINSERT SUCESSFUL!");
			
			// Display results
			System.out.println("\n\n\n");
			System.out.println("Inserting your first name as: " + fn);
			System.out.println("Inserting your last name as: " + ln);
			System.out.println("Inserting your email as: " + email);
			System.out.println("Inserting your phone number as: " + phone);
			System.out.println("Inserting your (unencrypted version) password as: " + pwd);
			System.out.println("Inserting your (encrypted version) password as: " + verif_pw_save);
			
			space();	// Space between the output and menu
		}catch(Exception e)
		{			
			System.out.println("An error has occurred: " + e);
		}
	}
	
		
	// Validate email
	public static boolean valEmail(String input)
	{
		String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = emailPat.matcher(input);
		return matcher.find();
	}
	
	// Validate phone number
	public static boolean valPhone(long input)
	{
		long count = 0;
		
		while(input > 0)
		{
			input = (input / 10);
			count++;
		}
		
		// Validity test of 10-digit phone number
		if(count == 10 || count == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// Hacky Clear Screen - kinda like CLS in C++
	public static void clear() 
	{
		for (int i = 0; i < 100; i++) 
		{
			System.out.println("");
		}
	}
	
	public static void space()
	{
		for(int i = 0; i < 20; i++)
		{
			System.out.println("");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void AddBooking(Ticketmaster esql){//2
		
	}
	
	public static void AddMovieShowingToTheater(Ticketmaster esql){//3
		
	}
	
	public static void CancelPendingBookings(Ticketmaster esql){//4
		
	}
	
	public static void ChangeSeatsForBooking(Ticketmaster esql) throws Exception{//5
		
	}
	
	public static void RemovePayment(Ticketmaster esql){//6
		
	}
	
	public static void ClearCancelledBookings(Ticketmaster esql){//7
		
	}
	
	public static void RemoveShowsOnDate(Ticketmaster esql){//8
		
	}
	
	public static void ListTheatersPlayingShow(Ticketmaster esql){//9
		//
		
	}
	
	public static void ListShowsStartingOnTimeAndDate(Ticketmaster esql){//10
		//
		
	}

	public static void ListMovieTitlesContainingLoveReleasedAfter2010(Ticketmaster esql){//11
		//
		
	}

	public static void ListUsersWithPendingBooking(Ticketmaster esql){//12
		//
		
	}

	public static void ListMovieAndShowInfoAtCinemaInDateRange(Ticketmaster esql){//13
		//
		
	}

	public static void ListBookingInfoForUser(Ticketmaster esql){//14
		//
		
	}
	
}
