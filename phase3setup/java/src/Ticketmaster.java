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
 * Iterable counters
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
	
	public static int listCounter(List<List<String>> a)
	{
		int count = 0;
		
		// Iterate through list
		for(int i = 0; i < a.size(); i++)
		{
			count++;
		}
		
		//System.out.println("Total lists: " + count);
		return count;
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
		while(rs.next()){
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
		for(int i = 0; i < 10; i++)
		{
			System.out.println("");
		}
	}

	public static void AddBooking(Ticketmaster esql){//2
		// Variables
		int sid = 0;
		int num_res = 0;
		int user_choice = -1;
		int csid = 0;
		String price;
		String the_name;
		String seat_num;
		String seat_type;
		String city;
		String state;
		String zip;
		List<List<String>> ShowInfo;
		List<List<String>> ShowSeatsList;
		List<String> Chosen_Movie;
		List<List<String>> PresentLists;
		List<String> Ind_final;
		List<String> Final_Choice;
		

		// Initialize Scanner
		Scanner scan = new Scanner(System.in);
		
		// Get a date and time from user and display corresponding movies. Return list of results for Shows.
		ShowInfo = ListShowsStartingOnTimeAndDate(esql);
		//System.out.println("Output: " + ShowInfo);
		
		// Get Number of returned results
		num_res = CountCharacter.listCounter(ShowInfo);
		
		// Ask the user to pick a number for a movie
		while(user_choice > num_res || user_choice < 1)
		{
			System.out.print("Please select a movie from the Index field: ");
			user_choice = Integer.parseInt(scan.nextLine());
		}
		
		space();
		
		// Get user_choice equivalent in array
		user_choice = (user_choice - 1);
		
		// Take chosen movie and rip sid from it
		Chosen_Movie = ShowInfo.get(user_choice);
		sid = Integer.parseInt(Chosen_Movie.get(0));
		
		//System.out.println("You have sid: " + sid);
		
		// ------------------------------- Shows -> ShowSeats : (sid) -------------------------------
		// Get list of showseats values w/prices
		try
		{
			String query = "";
			
			// Drop View for rewrite
			esql.executeUpdate("DROP VIEW IF EXISTS present_to_user");
			esql.executeUpdate("DROP VIEW IF EXISTS cinemas_add");
			esql.executeUpdate("DROP VIEW IF EXISTS add_theater");
			esql.executeUpdate("DROP VIEW IF EXISTS price_sno_stype;");
			
			// Search with 'sid' -- CREATE VIEW price_sno_stype AS SELECT cs.tid, ss.price, cs.sno, cs.stype FROM ShowSeats AS ss INNER JOIN CinemaSeats AS cs ON ss.csid=cs.csid WHERE sid='25';
			query = ("CREATE VIEW price_sno_stype AS SELECT ss.csid, cs.tid, ss.price, cs.sno, cs.stype FROM ShowSeats AS ss INNER JOIN CinemaSeats AS cs ON ss.csid=cs.csid WHERE sid='" + Integer.toString(sid) + "';");
			
			// Create view (tid, price, sno, stype)
			esql.executeUpdate(query);
			
			// Get view data to cross with -- Theaters X Cinema -- Deal with only null bid values!!!
			ShowSeatsList = esql.executeQueryAndReturnResult("SELECT * FROM price_sno_stype;");
			
			//System.out.println("outs: " + ShowSeatsList);
			
			query = "CREATE VIEW add_theater AS SELECT the.cid, pss.price, pss.sno, pss.stype, the.tname FROM price_sno_stype AS pss INNER JOIN Theaters AS the ON pss.tid = the.tid;";
			
			// Gather info from both tables needed
			esql.executeUpdate(query);
						
			// Theaters X Cinemas
			query = "CREATE VIEW cinemas_add AS SELECT c.city_id, at.price, at.sno, at.stype, at.tname FROM add_theater AS at INNER JOIN Cinemas AS c ON at.cid=c.cid;";
			esql.executeUpdate(query);
			
			// Cinemas X Cities
			query = "CREATE VIEW present_to_user AS SELECT ca.tname, ca.sno, ca.stype, ca.price, c.city_name, c.city_state, c.zip_code FROM cinemas_add AS ca INNER JOIN Cities AS c ON ca.city_id = c.city_id;";
			esql.executeUpdate(query);
			
			// Get data from view
			query = "SELECT * FROM present_to_user;";
			PresentLists = esql.executeQueryAndReturnResult(query);
			
			//System.out.println("my lists: " + PresentLists);
			
			// Present menu description
			System.out.println("|\tChoice\t|\tTheater\t|\tSeat #\t|\tSeat Type\t|\tPrice\t|\tCity\t|\tState\t|\tZip\t|");
			System.out.println("___________________________________________________________________________________________________________________________________________________________________\n");
			
			// Present menu options to user
			for(int i = 0; i < PresentLists.size(); i++)
			{
				Ind_final = PresentLists.get(i);
				
				the_name = Ind_final.get(0);
				seat_num = Ind_final.get(1);
				seat_type = Ind_final.get(2);
				price = Ind_final.get(3);
				city = Ind_final.get(4);
				state = Ind_final.get(5);
				zip = Ind_final.get(6);
				
				// Display options
				System.out.println((i + 1) + "\t|\t" + the_name + "\t|\t" + seat_num + "\t|\t" + seat_type + "\t|\t" + price + "\t|\t" + city + "\t|\t" + state + "\t|\t" + zip + "\n");
			}
			
			// Allow for selection by user
			num_res = 0;
			user_choice = -1;
			
			//query = "SELECT * FROM PresentLists;";
			num_res = esql.executeQuery(query);
			
			// Verify valid input
			while(user_choice > num_res || user_choice < 1)
			{
				System.out.print("Enter 'choice' value: ");
				user_choice = Integer.parseInt(scan.nextLine());
			}
			
			// Get the OG value from VIEW choice
			user_choice = (user_choice - 1);
			Final_Choice = ShowSeatsList.get(user_choice);
			
			//System.out.println("User has selected: " + Final_Choice);
			// Get number for csid -- selected by user
			csid = Integer.parseInt(Final_Choice.get(0));
			
			// Get next number for 'bid'
			query = "SELECT * FROM Bookings";
			int bid_next = esql.executeQuery(query);
			bid_next = (bid_next + 1);
			//System.out.println("Next bid: " + bid_next);
			
			//System.out.println("Out # total: " + num_res);
			
			// ------------------------------- User Verification -------------------------------
			String email = "";
			String first = "";
			String last = "";
			


			// Initialize 'verify'
			int verify = 0;
			
			// Map verification appropriately
			while(verify != 1)
			{
				// Space 
				space();
			
				// Get email address
				System.out.print("Please enter your email address on file to book: ");
				email = scan.nextLine();
				
				// Get First and last name
				System.out.print("Please enter your first name on file (tied to email): ");
				first = scan.nextLine();
				
				System.out.print("Please enter your last name on file (tied to email): ");
				last = scan.nextLine();		

				// Verify that 1 entry appears in results
				query = ("SELECT * FROM Users WHERE email = '" + email + "' and fname = '" + first + "' and lname = '" + last + "';");
				verify = esql.executeQuery(query);
				
				if(verify != 1)
				{
					System.out.println("\nData is incorrect. Try again!\n");
				}
			}

			try
			{
				String update = "";
				String insert = "";
				List<List<String>> datetime;
				List<String> dt_list;
				String dt;
				
				// Get current timestamp
				datetime = esql.executeQueryAndReturnResult("SELECT CURRENT_TIMESTAMP;");
				dt_list = datetime.get(0);
				dt = dt_list.get(0);
				//System.out.println("Your date time output: " + dt);
				
				
				// Insert data into Bookings
				insert = ("INSERT INTO Bookings(bid, status, bdatetime, seats, sid, email) VALUES('" + bid_next + "', 'pending', '" + dt + "', '1', '" + sid + "', '" + email + "');");
				esql.executeUpdate(insert);
				
				// Update values in ShowSeats
				update = ("UPDATE ShowSeats SET bid = '" + bid_next + "' WHERE csid='" + csid + "' and sid='" + sid + "';");
				//System.out.println("Info (bid -> csid -> sid): " + bid_next + "\t" + csid + "\t" + sid);
				esql.executeUpdate(update);
				
				clear();
				System.out.println("Success! Your Booking is Successful!");
			}
			catch(Exception e)
			{
				System.out.println("An error has occurred: " + e);
			}

			
			//System.out.println("Verification value: " + verify);
			
			// Space
			space();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred: " + e);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void AddMovieShowingToTheater(Ticketmaster esql){//3
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void CancelPendingBookings(Ticketmaster esql){//4
		// Variables
		String query = "";
		int bid = 0;
		List<List<String>> bid_arr_of_arr;
		List<String> bid_arr;
		
		try
		{
			clear();	// Clear screen
			// Look for status = 'pending' in Bookings (query)
			query = ("SELECT bid FROM Bookings WHERE status='pending';");
			bid_arr_of_arr = esql.executeQueryAndReturnResult(query);
			bid_arr = bid_arr_of_arr.get(0);	// Dummy variable for initialization
			
			// Convert to just individuals
			for(int i = 0; i < bid_arr_of_arr.size(); i++)
			{
				bid_arr = bid_arr_of_arr.get(i);	// List of Lists --> List
				bid = Integer.parseInt(bid_arr.get(0));	// List --> Number
				
				// Create new query to update 'pending' value
				query = ("UPDATE Bookings SET status='cancelled' WHERE status='pending' and bid='" + bid + "';");
				esql.executeUpdate(query);
				
				//System.out.println("Booking: " + bid);
			}
			
			System.out.println("All pending reservations have been cancelled successfully!");
			
			space();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred: " + e);
		}
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static List<List<String>> ListShowsStartingOnTimeAndDate(Ticketmaster esql){//10		
		// Variables
		int year = 0;
		int day = 0;
		int month = 0;
		int hour = 0;
		int minute = 0;
		String date_search = "";
		String time_search = "";
		String ampm = "";
		boolean valid = false;
		
		// Initialize Scanner
		Scanner scan = new Scanner(System.in);
		clear();
		
		// Date Menu
		space();
		System.out.println("\t\t\tDATE\n\n\n");

		// Allow for input of a date
		while(valid == false)	// Month selection
		{
			System.out.print("Please enter a month to search: ");
			month = Integer.parseInt(scan.nextLine());
			
			if(month < 1 || month > 12)
			{
				valid = false;
			}
			else
			{
				valid = true;
			}
		}
			
		valid = false;	// Reset statement for next loop
		
		while(valid == false)	// Day selection
		{
			System.out.print("Please enter a day to search: ");
			day = Integer.parseInt(scan.nextLine());
			
			if(day < 1 || day > 31)
			{
				valid = false;
			}
			else
			{
				valid = true;
			}
		}
		
		System.out.print("Please enter a year to search: ");
		year = Integer.parseInt(scan.nextLine());	// Year selection

		// Concatenate and turn into date - format: mm/dd/yyyy
		date_search = (String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year));
		
		// Time Menu
		space();
		System.out.println("\t\t\tTIME\n\n\n");
		
		valid = false;	// Reset validity bool for time tests

		// Get hour
		while(valid == false)
		{
			System.out.print("Please enter the hour of the show (12-hr format): ");
			hour = Integer.parseInt(scan.nextLine());
			
			if(hour < 1 || hour > 12)
			{
				valid = false;
			}
			else
			{
				valid = true;
			}
		}

		valid = false;	// Reset for validity
		
		// Get minute
		while(valid == false)
		{
			System.out.print("Please enter the minute of the show: ");
			minute = Integer.parseInt(scan.nextLine());
			
			if(minute < 1 || minute > 59)
			{
				valid = false;
			}
			else
			{
				valid = true;
			}
		}
		
		valid = false;	// Reset for next validity
		
		// Get am/pm
		while (valid == false)
		{
			System.out.print("What time of day [a/p]?: ");
			ampm = scan.nextLine();
			//System.out.println("Entered value: " + ampm);	// Test input

			if((ampm).equals("a") || (ampm).equals("p"))
			{
				if((ampm).equals("a") && hour == 12)
				{
					hour = (hour - 12);	// Change to 00
				}
				else if((ampm).equals("p") && hour == 12)
				{
					hour = hour;	// Do nothing
				}
				else if((ampm).equals("p"))
				{
					hour = (hour + 12);	// Change to 24-hr format. 1:00 pm --> 13:00
				}
				
				valid = true;
			}
			else
			{
				System.out.println("Wrong input!");
				valid = false;
			}
		}
		
		// Concatenate time together
		time_search = (hour + ":" + minute);
		
		// cls
		clear();
	
		// Try-catch cases w/output if successful
		try
		{
			List<List<String>> li_of_li;
			List<String> list_item;
			List<String> movieID = new ArrayList<String>();
			
			// Create query string -- SELECT * FROM Shows WHERE sdate='1/1/2001' and sttime='6:25';
			String query = ("SELECT * FROM Shows WHERE sdate='" + date_search + "' and sttime='" + time_search + "';");
			
			// Pass query to function and get list of results
			li_of_li = esql.executeQueryAndReturnResult(query);
			
			//System.out.println(li_of_li);
			
			// Go through each entry and grab the movie ID
			for(int i = 0; i < li_of_li.size(); i++)
			{
				list_item = li_of_li.get(i);
				
				// Get the movie ID of every list item
				movieID.add(list_item.get(1));
				
				//System.out.println("Item " + (i + 1) + ": " + list_item);
			}
			
			// Cycle through each movieID and get the movie equivalent
			try
			{
				List<List<String>> movieItems;
				List<String> Movie_Stuff;
				String mtitle = "";
				String mcountry = "";
				String mgenre = "";
				String mdur = "";
				String mlang = "";
				
				// Search for movies -- SELECT * FROM Movies WHERE mvid = <our values>;
				movieItems = esql.executeQueryAndReturnResult("SELECT * FROM Movies WHERE mvid = 0;");	// Dummy value to initialize

				System.out.println("|\t\tIndex\t\t|\t\tMovie Title\t\t|\t\tCountry\t\t|\t\tGenre\t\t|\t\tDuration\t\t|\t\tLanguage\t\t|");	// Title | Country | Genre | Duration | Language
				System.out.println("______________________________________________________________________________________________________________________________________________________________________________________________________________\n");

				for(int i = 0; i < movieID.size(); i++)
				{
					query = "SELECT * FROM Movies WHERE mvid = '" + movieID.get(i) + "';";
					movieItems = esql.executeQueryAndReturnResult(query);
					
					//System.out.println("Test: " + movieItems);
					
					// Take the list item and parse the data
					for(int j = 0; j < movieItems.size(); j++)
					{
						Movie_Stuff = movieItems.get(j);
						
						//System.out.println("This one: " + Movie_Stuff);
						
						// Get needed data from list
						mtitle = Movie_Stuff.get(1);
						mcountry = Movie_Stuff.get(3);
						mgenre = Movie_Stuff.get(7);
						mdur = Movie_Stuff.get(5);
						mlang = Movie_Stuff.get(6);
					}				
				
					// Display movie
					System.out.println((i + 1) + "\t\t|\t\t" + mtitle + "\t\t|\t\t" + mcountry + "\t\t|\t\t" + mgenre + "\t\t|\t\t" + mdur + "\t\t|\t\t" + mlang);
				}				
			}
			catch(Exception e)
			{
				System.out.println("An error has occurred: " + e);
			}
			
			// Success message
			space();
			System.out.println("SEARCH SUCCESSFUL!\n\n\n");
			
			// Show search
			System.out.println("Your entered date is: " + date_search);
			System.out.println("Your entered time is: " + time_search);
			
			// Space output from menu
			space();
			
			return li_of_li;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred: " + e);
		}
		return null;
	}

	public static void ListMovieTitlesContainingLoveReleasedAfter2010(Ticketmaster esql){//11
		// Start by setting up the query to pull all values that are greater than 2010
		try
		{
			List<List<String>> Movies_Newer_li_of_li;
			List<String> Movies_List;
			String[] words;
			String query;
			String title_test = "";
			space();
			
			query = "SELECT * FROM Movies WHERE rdate>'12/31/2010';";
			Movies_Newer_li_of_li = esql.executeQueryAndReturnResult(query);
			
			System.out.println("Movie titles containing the word 'Love' and were released after 2010.\n\n");
			
			// Take one value at a time from query
			for(int i = 0; i < Movies_Newer_li_of_li.size(); i++)
			{
				Movies_List = Movies_Newer_li_of_li.get(i);	// Get each movie array that is > 2010
				
				// Get the title of each array
				title_test = Movies_List.get(1);
				
				// Split titles into words
				words = title_test.split("\\W+");
				
				// Scan array letter by letter
				for(int j = 0; j < words.length; j++)
				{
					// Check if the word matches 'love' or "Love"
					//System.out.print(words[j]);
					
					if((words[j]).equals("love") || (words[j]).equals("Love"))
					{
						// Print the Title
						System.out.println(title_test);
					}
				}
				
				//System.out.println(words);
			}
			space();			
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred: " + e);
		}
	}

	public static void ListUsersWithPendingBooking(Ticketmaster esql){//12
		// Variables
		int count = 0;
		String query = "";
		
		try
		{
			clear();	// Clear screen
			
			System.out.println("********************************************");
			System.out.println("*        USERS WITH PENDING BOOKINGS        ");
			System.out.println("********************************************\n\n");

			// Drop table, should it exist
			query = ("DROP VIEW IF EXISTS pending_list;");
			esql.executeUpdate(query);			
			
			// Create 'pending' view status Bookings X Users
			query = ("CREATE VIEW pending_list AS SELECT u.fname AS First, u.lname AS Last, u.email AS Email FROM Bookings AS b INNER JOIN Users AS u ON b.email=u.email WHERE b.status = 'pending';");
			esql.executeUpdate(query);
			
			// Display View
			query = ("SELECT * FROM pending_list");
			count = esql.executeQueryAndPrintResult(query);
			
			// Display number of users in list with 'pending' status
			System.out.println("\n\n\nNumber of pending reservations: " + count);
			
			space();	// Make space
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred: " + e);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public static void ListMovieAndShowInfoAtCinemaInDateRange(Ticketmaster esql){//13
		//
		
	}

	public static void ListBookingInfoForUser(Ticketmaster esql){//14
		//
		
	}
	
}
