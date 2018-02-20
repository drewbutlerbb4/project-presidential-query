import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ETL {
	 // JDBC driver name and database URL
		static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	 	static final String DB_URL = "jdbc:mysql://localhost:3306/PresidentialQuery";

	 //  Database credentials
	 	static final String USER = "myuser";
	 	static final String PASS = "xxxx";

	 	private static void createTable(String name) throws SQLException {
	 		Connection dbConnection = null;
			Statement statement = null;

			String createTableSQL = "CREATE TABLE " + name + "(";
			
			if (name.equalsIgnoreCase("Results")) {
				createTableSQL += "NAME VARCHAR(50) NOT NULL, " 
						+ "YEAR INT NOT NULL, "
						+ "POLL_DATA DECIMAL(5,2), "
						+ "POPULAR_VOTE INT, "
						+ "ELECTORAL_VOTE INT, " + "PRIMARY KEY (NAME, YEAR)"
						+ ")";
			} else if (name.equalsIgnoreCase("Party")) {
				createTableSQL += "PARTY_NAME VARCHAR(20) NOT NULL, " 
						+ "PRIMARY KEY (PARTY_NAME)"
						+ ")";
			} else if (name.equalsIgnoreCase("Belongs")) {
				createTableSQL += "NAME VARCHAR(50) NOT NULL, " 
						+ "YEAR INT NOT NULL, "
						+ "PARTY_NAME VARCHAR(20), " + "PRIMARY KEY (NAME, YEAR)"
						+ ")";
			} else if (name.equalsIgnoreCase("Participated")) {
				createTableSQL += "NAME VARCHAR(50) NOT NULL, " 
						+ "YEAR INT NOT NULL, "
						+ "PARTY VARCHAR(25), "
						+ "RUNNING_POSITION VARCHAR(2), " + "PRIMARY KEY (NAME, YEAR, RUNNING_POSITION)"
						+ ")";
			} else if (name.equalsIgnoreCase("Presidential_Candidates")) {
				createTableSQL += "NAME VARCHAR(50) NOT NULL, " 
						+ "PORTRAIT VARCHAR(70), " + "PRIMARY KEY (NAME)"
						+ ")";
			} else if (name.equalsIgnoreCase("Vice_Presidential_Candidates")) {
				createTableSQL += "NAME VARCHAR(50) NOT NULL, " 
						+ "PRIMARY KEY (NAME)"
						+ ")";
			} else if (name.equalsIgnoreCase("Election")) {
				createTableSQL += "YEAR INT NOT NULL, "
						+ "POPULATION_SIZE INT NOT NULL, " + "PRIMARY KEY (YEAR)"
						+ ")";
			} else if (name.equalsIgnoreCase("Yield")) {
				createTableSQL +=  "YEAR INT NOT NULL, "
						+ "WINNER VARCHAR(50) NOT NULL, " + "PRIMARY KEY (YEAR)"
						+ ")";
			} else {
				System.out.println("Invalid Table Name");
			}
			
			
//			createTableSQL = createTableSQL 
//					+ "ID INT NOT NULL, "
//					+ "NAME VARCHAR(20) NOT NULL, "
//					+ "BIRTHDAY DATE NOT NULL, " + "PRIMARY KEY (ID) "
//					+ ");";

			try {
				dbConnection = connect();
				statement = dbConnection.createStatement();

				System.out.println(createTableSQL);
	                        // execute the SQL stetement
				statement.executeUpdate(createTableSQL);

				System.out.println("Table \""+ name + "\" is created!");

			} catch (SQLException e) {

				System.out.println(e.getMessage());

			} finally {

				if (statement != null) {
					statement.close();
				}

				if (dbConnection != null) {
					dbConnection.close();
				}

			}
	 	}
	 	
	 	public static Connection connect() { 
	 		System.out.println("-------- MySQL JDBC Connection Testing ------------");
	 		Connection conn = null;

	 		try {
	 			Class.forName(JDBC_DRIVER);
	 		} catch (ClassNotFoundException e) {
	 			System.out.println("Where is your MySQL JDBC Driver?");
	 			e.printStackTrace();
	 			return conn;
	 		}

	 		System.out.println("MySQL JDBC Driver Registered!");
	 		
	 		try {
	 			conn = DriverManager.getConnection(DB_URL,USER, PASS);

	 		} catch (SQLException e) {
	 			System.out.println("Connection Failed! Check output console");
	 			e.printStackTrace();
	 			return conn;
	 		}

	 		if (conn != null) {
	 			System.out.println("You made it, take control your database now!");
	 		} else {
	 			System.out.println("Failed to make connection!");
	 		}
			return conn;
	 	}
	public static String clean(String line) {
		Stack<String> st = new Stack<String>();
		String tmp = "";
		for(int i = 0; i < line.length(); i++) {
			if(line.charAt(i) == '<') st.push("<");
			else if (line.charAt(i) == '>') st.pop();
			else if (st.isEmpty() || !(st.peek().equals("<"))) tmp+=line.charAt(i);
		}
		return tmp;
	}
	public static int[][] extract1(String webPage) {
		int[][] population = new int[40][2];
		  try{
	            URL url = new URL(webPage);
	            URLConnection connection = url.openConnection();
	            connection.setRequestProperty("User-Agent", "Mozilla/5.0");;
	            String line, prev_line = "";
	            int curr_i = 0;
	            //StringBuilder builder = new StringBuilder();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            int row = 0;
	            int col = 0;
	            while((line = reader.readLine()) != null) {
	            	int index;
	            	if (curr_i == 0){
	            		prev_line = line;
	               // builder.append(line);
	            	}
	            	else {
	            		if((index = line.indexOf("<td style=\"text-align:right;\">")) != -1) {
	            			//Stack st = new Stack<String>();
	            			int year;
	            			int pop;
	            			line = clean(line);
	            			pop = Integer.parseInt(line.replaceAll(",", ""));
            				prev_line = clean(prev_line);
	            			if (row == 0) {
	            				prev_line = prev_line.substring(prev_line.indexOf(",")+1, prev_line.length());
	            				year = Integer.parseInt(prev_line.trim());
	            			} else {
	            				year = population[row-1][0] + 10;
	            			}
//	            			String tmp = "";
//	    	        		for(int i = 0; i < prev_line.length(); i++) {
//	    	        			if(prev_line.charAt(i) == '<') st.push("<");
//	    	        			else if (prev_line.charAt(i) == '>') st.pop();
//	    	        			else if (st.isEmpty() || !(st.peek().equals("<"))) tmp+=prev_line.charAt(i);
//	    	        		}
//	    	        		prev_line = tmp;
//	            			System.out.print(prev_line+":\t");
//	            			System.out.println(line);
	            			population[row][col] = year;
	            			col = 1;
	            			population[row][col] = pop;
	            			col = 0;
	            			row++;
	            			//System.out.println(line.substring(line.indexOf(">")+1,line.lastIndexOf("<")));
	            		}
            			prev_line = line;
	            	}
	            	curr_i++;
	            }
		  } catch( Exception e) {
			  e.printStackTrace();
		  }
		  for (int i = 0; i< population.length; i++) {
			  for (int j = 0; j < population[i].length; j++) {
				  System.out.print(population[i][j] + "\t");
			  }
			  System.out.println();
		  }
		  return population;
	}
	public static String[][] extract2(String webPage) {
		String [][] data = new String[500][6];
		try{
            URL url = new URL(webPage);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            String line;

            int curr_i = 0;
            //StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Stack<String> st = new Stack<String>();
            int row_o = 0; //if year >= 1789 && year <= 2012
            int row = 0;
            int col = 0;
            int counter = 0;
            int pos_i = 0;
            String position = "";
            int year = 0;
            while((line = reader.readLine()) != null) {
            	if (curr_i < 1115 || curr_i >= 3437	) {curr_i++;} else {
            	int index;
            	if(((index = line.indexOf("<span style=")) != -1 || line.indexOf("<br/>") != -1) 
            			&& line.indexOf("<a href=") == -1) {
//        		if((index = line.indexOf("<span style=\"FONT-SIZE: 10pt; FONT-FAMILY: Arial; COLOR: rgb(0,0,0)\">")) != -1) {
        			for(int i = 0; i < line.length(); i++) { //push number of span discovered onto stack
        				int j = line.indexOf("<span", i);
        				if(j != -1) {
        					st.push(line);
        					i = j + "<span".length() - 1;
        				}
        			}
        			String tmp = clean(line).trim();
        			if (tmp.length() == 0 || tmp.charAt(0) == '*' || tmp.equalsIgnoreCase("top")) {
        				
        			} else {//year >= 1789 && year <= 2012
        				if(tmp.substring(0,1).matches("[-+]?\\d*\\.?\\d+") ) {
        					int num = Integer.parseInt(tmp.replaceAll(",", "").replaceAll("\\*", ""));
        					if (num >= 1789 && num <= 2012) {
        						year = num;
            					counter = 0;
            					pos_i = 0;
            					position = "p";
            					//position = position.equalsIgnoreCase("p")?"vp":"p";
            					row_o = row;
            					col = 0;
        					} else if (col == 3) { //start electoral votes
        						col = 4;
        						pos_i = 0;
        						data[row_o][col] = num +"";
        						pos_i++;
        					} else if (pos_i < counter && col == 4) { //electoral votes
        						data[row_o+pos_i][col] = num +"";
        						pos_i++;
        					} else if (pos_i == counter && col == 4) { //start popular votes
        						pos_i = 0;
        						col = 5;
        						data[row_o][col] = num +"";
        						pos_i++;
        					} else if (pos_i < counter && col == 5){ //popular vote
        						data[row_o+pos_i][col] = num +"";
        						pos_i++;
        					}
        					
        				} else if (tmp.indexOf("No record") != -1 && col == 4) { //no record for pop vote
    						pos_i = 0;
    						col = 5;
    						while (pos_i < counter) {
    							data[row_o+pos_i][col] = null;
    							pos_i++;
    						}
        				}else if (position.equals("p") && tmp.indexOf(" ") != -1 && col == 0){ //presidential candidates
        					counter++;
        					data[row][0] = year+"";
        					data[row][1] = "p";
        					data[row][2] = tmp.replaceAll("\\*", "");
        					row++;
        				} else if (position.equals("p") && pos_i < counter) { //political party info
        					col = 3;
        					data[row_o+pos_i][col] = tmp;
        					pos_i++;
        				} else if (position.equals("p") && pos_i == counter) {// start vp
        					pos_i = 0;
        					position = "vp";
//        					data[row_o][0] = year + "";
//        					data[row_o][1] = "vp";
//        					data[row_o][2] = tmp;
        					data[row][0] = year + "";
        					data[row][1] = "vp";
        					data[row][2] = tmp;
        					data[row][3] = data[row-counter][3];
        					row++;
        					pos_i++;
        				} else if (position.equals("vp") && year > 1840 &&  pos_i < counter){ //vp paired with p
//        					data[row_o+pos_i][0] = year + "";
//        					data[row_o+pos_i][1] = "vp";
//        					data[row_o+pos_i][2] = tmp;
        					data[row][0] = year+"";
        					data[row][1] = "vp";
        					data[row][2] = tmp;
        					data[row][3] = data[row-counter][3];
        					row++;
        					pos_i++;
        				} else if (position.equals("vp") && year <= 1840) { //when vp not paired with p
//        					data[row_o+pos_i][0] = year + "";
//        					data[row_o+pos_i][1] = "vp";
//        					data[row_o+pos_i][2] = tmp;
        					data[row][0] = year + "";
        					data[row][1] = "vp";
        					data[row][2] = tmp;
        					pos_i++;
        					row++;
        				} else if (tmp.indexOf("none") != -1 && position.equals("vp")){ //none is detected, vp
//do nothing
//        					data[row_o+pos_i][0] = year + "";
//        					data[row_o+pos_i][1] = "vp";
//        					data[row_o+pos_i][2] = tmp;
//        					pos_i++; //problema!!! 
        				} else if (tmp.indexOf("none") != -1 && position.equals("p")) { //none detected pop vote
        					data[row_o+pos_i][col] = null;
    						pos_i++;
        				}  //else if (tmp.indexOf("No record") != -1 && pos_i < counter && col == 5) {
    					//	data[row_o+pos_i][col] = null;
    					//	pos_i++;
        				//}
        			}
        			//System.out.println(curr_i + "\t"+tmp);
        		}
        		else if(st.isEmpty() == false) {
        			String tmp = clean(line).trim();
        			if (tmp.length() == 0 || tmp.charAt(0) == '*' || tmp.equalsIgnoreCase("top")) {
        				
        			} else {//year >= 1789 && year <= 2012
        				if(tmp.substring(0,1).matches("[-+]?\\d*\\.?\\d+") ) {
        					int num = Integer.parseInt(tmp.replaceAll(",", "").replaceAll("\\*", ""));
        					if (num >= 1789 && num <= 2012) {
        						year = num;
            					counter = 0;
            					pos_i = 0;
            					position = "p";
            					//position = position.equalsIgnoreCase("p")?"vp":"p";
            					row_o = row;
            					col = 0;
        					} else if (col == 3) { //start electoral votes
        						col = 4;
        						pos_i = 0;
        						data[row_o][col] = num +"";
        						pos_i++;
        					} else if (pos_i < counter && col == 4) { //electoral votes
        						data[row_o+pos_i][col] = num +"";
        						pos_i++;
        					} else if (pos_i == counter && col == 4) { //start popular votes
        						pos_i = 0;
        						col = 5;
        						data[row_o][col] = num +"";
        						pos_i++;
        					} else if (pos_i < counter && col == 5){ //popular vote
        						data[row_o+pos_i][col] = num +"";
        						pos_i++;
        					}
        					
        				} else if (tmp.indexOf("No record") != -1 && col == 4) { //no record for pop vote
    						pos_i = 0;
    						col = 5;
    						while (pos_i < counter) {
    							data[row_o+pos_i][col] = null;
    							pos_i++;
    						}
        				} else if (position.equals("p") && tmp.indexOf(" ") != -1 && col == 0){ //presidential candidates
        					counter++;
        					data[row][0] = year+"";
        					data[row][1] = "p";
        					data[row][2] = tmp.replaceAll("\\*","");
        					row++;
        				} else if (position.equals("p") && pos_i < counter) { //political party info
        					col = 3;
        					data[row_o+pos_i][col] = tmp;
        					pos_i++;
        				} else if (position.equals("p") && pos_i == counter) {// start vp
        					pos_i = 0;
        					position = "vp";
//        					data[row_o][0] = year + "";
//        					data[row_o][1] = "vp";
//        					data[row_o][2] = tmp;
        					data[row][0] = year + "";
        					data[row][1] = "vp";
        					data[row][2] = tmp;
        					data[row][3] = data[row-counter][3];
        					row++;
        					pos_i++;
        				} else if (position.equals("vp") && year > 1840 &&  pos_i < counter){ //vp paired with p
//        					data[row_o+pos_i][0] = year + "";
//        					data[row_o+pos_i][1] = "vp";
//        					data[row_o+pos_i][2] = tmp;
        					data[row][0] = year+"";
        					data[row][1] = "vp";
        					data[row][2] = tmp;
        					data[row][3] = data[row-counter][3];
        					row++;
        					pos_i++;
        				} else if (position.equals("vp") && year <= 1840) { //when vp not paired with p
//        					data[row_o+pos_i][0] = year + "";
//        					data[row_o+pos_i][1] = "vp";
//        					data[row_o+pos_i][2] = tmp;
        					data[row][0] = year + "";
        					data[row][1] = "vp";
        					data[row][2] = tmp;
        					pos_i++;
        					row++;
        				} else if (tmp.indexOf("none") != -1 && position.equals("vp")){ //none is detected, vp
//do nothing
//        					data[row_o+pos_i][0] = year + "";
//        					data[row_o+pos_i][1] = "vp";
//        					data[row_o+pos_i][2] = tmp;
//        					pos_i++; //problema!!! 
        				} else if (tmp.indexOf("none") != -1 && position.equals("p")) { //none detected pop vote
        					data[row_o+pos_i][col] = null;
    						pos_i++;
        				} 
        			}
        			//System.out.println(curr_i + "\t"+tmp);
        		}
        		if(line.indexOf("</span>")  != -1){
    				for(int i = 0; i < line.length(); i++) { //pop number of span discovered onto stack
        				int j = line.indexOf("</span", i);
        				if(j != -1) {
        					if(st.isEmpty() == false) st.pop();
        					i = j + "</span".length() - 1;
        				}
        			}
        			
        		}
            	curr_i++;
            }
            }
		} catch( Exception e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < data.length; i++) {
			for (int j =0; j < data[i].length; j++) {
				if (data[i][j] != null) System.out.print(data[i][j]+"\t");
			}
			System.out.println();
		}
		return data;
	}
	public static String[][] extract3(String webPage) {
		String data[][] = new String[100][3];
		try{
			URL url = new URL(webPage);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");;
            String line, prev_line = "";
            int curr_i = 0;
            //StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Stack<String> st = new Stack<String>();
            int row_o = 0;
            int counter = 0;
            int year = 0;
            int row = 0;
            int col = 0;
            while ((line = reader.readLine()) != null) {
            	if (curr_i == 0){
            		prev_line = line;
               // builder.append(line);
            	}
            	else {

            		if(prev_line.indexOf("<table class=\"wikitable\">") != -1) { //year
            			line = clean(line);//<table class="wikitable">
//    	        		prev_line = clean(prev_line);
//            			System.out.print(curr_i + "\t"+line);
            			if (line.indexOf("[") != -1) {
            				year = Integer.parseInt(line.substring(0, line.indexOf("[")));
            				
//            				System.out.println(line.substring(0, line.indexOf("[")));
            			} else {
            				year = Integer.parseInt(line);
            			}
//            			System.out.println(curr_i +" "+year);
        				row_o = row;
        				col = 0;
        				counter = 0;
            		} else if (prev_line.indexOf("ctual") != -1) {
            			st.push("<");
            		} else if (prev_line.indexOf("<th>Month</th>") != -1) {
            			st.push("pres");
            		}
            		if (st.isEmpty() == false) {
            			String s = clean(line);
            			//line = clean(line);
            			if (s.indexOf("(") != -1) { //name of pres
            				String tmp = s.substring(0, s.indexOf("("));
            				col = 0;
            				data[row][col] = ""+year;
            				col = 1;
            				data[row][col] = tmp;
            				row++;
            				counter++;
//            				System.out.println(s.substring(0, s.indexOf("(")));
            			} else if (s.indexOf("%") != -1) { //actual result
            				String str = s.substring(0, s.indexOf("%"));
//            				System.out.println(curr_i + "\t" + str);
            				if (row_o + counter  == row) {
            					row = row_o; 
            					col = 2;
                				if (str.indexOf("+") + str.indexOf("-") == -2 && Integer.parseInt(str) != 0) {
                					data[row][col] = str;
                				} else {
                					data[row][col] = ""+ (Integer.parseInt(data[row][col]) - Integer.parseInt(str));
                				}
                				row++;
            				} else {
            					col = 2;
            					if (str.indexOf("+") + str.indexOf("-") == -2 && Integer.parseInt(str) != 0) {
                					data[row][col] = str;
                				} else {
                					//System.out.println(str);
                					try{
//                						if(Integer.parseInt(str) == 0) System.err.println("how");
                						data[row][col] = ""+ (Integer.parseInt(data[row][col]) - Integer.parseInt(str));
                					} catch(NumberFormatException e) {
                						//System.out.print(row+" "+data[row][col] + " " + str);
                					}
                				}
                				row++;
            				}
            				
            				//System.out.println(s.substring(0, s.indexOf("%")));
            			} //else
//            				System.out.println(s);
            		}
            		if (line.indexOf("</tr>") != -1 && !st.isEmpty()) { 
            			st = new Stack<String>();
            		}
        			prev_line = line;
            	}
            	curr_i++;
            }
	  } catch( Exception e) {
		  e.printStackTrace();
	  }
		for(int i = 0; i< data.length; i++) {
			for(int j = 0; j<data[i].length; j++) {
				System.out.print(data[i][j]+"\t");
			}
			System.out.println();
		}
		return data;
	}
    public static void load1(int[][] population) {
    	Connection dbConnection = null;
		Statement statement = null;
		try {
			dbConnection = connect();
			statement = dbConnection.createStatement();

//			System.out.println(insertTableSQL);

			// execute insert SQL stetement
			for (int i = 0; i < population.length; i++) {
				String insertTableSQL = "INSERT INTO ELECTION"
						+ "(YEAR,POPULATION_SIZE) " + "VALUES"
						+ "(" + population[i][0] + ", "
						+ population[i][1] + ")";
				if (population[i][0] != 0) statement.executeUpdate(insertTableSQL);
			}
			

			System.out.println("Record is inserted into DBUSER table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
    }
    public static void load2(String[][] election) {
    	Connection dbConnection = null;
		Statement statement = null;
		try {
			dbConnection = connect();
			statement = dbConnection.createStatement();

//			System.out.println(insertTableSQL);

			// execute insert SQL statement
			for (int i = 0; i < election.length; i++) {
				if (election[i][0] != null) {
					String insertTableSQL = "INSERT INTO Results"
							+ "(NAME, YEAR, POLL_DATA, POPULAR_VOTE, ELECTORAL_VOTE) " + "VALUES"
							+ "('" + (election[i][2].indexOf("(") == -1?election[i][2].replaceAll("'",""):
								election[i][2].substring(0, election[i][2].indexOf("(")-1)) + "', "
							+ election[i][0] + ", "
							+ "NULL, "
							+ (election[i][5]==null?"NULL":election[i][5]) + ", "
							+ (election[i][4]==null?"NULL":election[i][4]) + ")";
					
					if (election[i][2] != null && election[i][1] != null
							&& !election[i][2].equals("none")
							&& !election[i][1].equals("vp")) {
						System.out.println(insertTableSQL);
						statement.executeUpdate(insertTableSQL);
					}
					
					//insert into participated
					insertTableSQL = "INSERT INTO Participated"
							+ "(NAME, YEAR, PARTY, RUNNING_POSITION) " + "VALUES"
							+ "('" + (election[i][2].indexOf("(") == -1?election[i][2].replaceAll("'",""):
								election[i][2].substring(0, election[i][2].indexOf("(")-1)) + "', "
							+ election[i][0] + ", '"
							+ (election[i][3]==null?"NULL":election[i][3].replaceAll("'","")) + "', '"
							+ (election[i][1]==null?"NULL":election[i][1]) + "')";
					if (election[i][2] != null && election[i][1] != null
							&& !election[i][2].equals("none")) {
						System.out.println(insertTableSQL);
						statement.executeUpdate(insertTableSQL);
					}
					
					//insert into yield
					insertTableSQL = "INSERT INTO Yield"
							+ "(YEAR, WINNER) " + "VALUES"
							+ "("
							+ election[i][0] + ", '"
							+ (election[i][2].indexOf("(") == -1?election[i][2].replaceAll("'",""):
								election[i][2].substring(0, election[i][2].indexOf("(")-1)) + "') ";
					
					if (election[i][2] != null && election[i][1] != null
							&& election[i][2].indexOf("(") != -1
							&& !election[i][1].equals("vp")) {
						System.out.println(insertTableSQL);
						statement.executeUpdate(insertTableSQL);
					}
				}
			}
			

			System.out.println("Record is inserted into DBUSER table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
    }
    public static void load3(String[][] poll) {
    	Connection dbConnection = null;
		Statement statement = null;
		try {
			dbConnection = connect();
			statement = dbConnection.createStatement();

//			System.out.println(insertTableSQL);

			// execute insert SQL stetement
			for (int i = 0; i < poll.length; i++) {
				if (poll[i][0] != null) {
					String updateTableSQL = "UPDATE Results"
							+ " SET POLL_DATA = " + poll[i][2]
							+ " WHERE NAME = '" + poll[i][1]
							+ "' AND (YEAR = 1+" + poll[i][0]
							+ " OR YEAR = " + poll[i][0]	
							+ " OR YEAR = " + poll[i][0]	+ "-1)";
						System.out.println(updateTableSQL);
						statement.executeUpdate(updateTableSQL);
				}
			}
			

			System.out.println("Record is inserted into DBUSER table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
    }
    public static void main(String[] args) throws MalformedURLException, IOException {
    	int[][] population = extract1(
    			"https://en.wikipedia.org/wiki/United_States_Census");
    	String[][] election_res = extract2(
    			"http://2012election.procon.org/view.resource.php?resourceID=004332");
    	String[][] poll_res = extract3(
    			"https://en.wikipedia.org/wiki/Historical_polling_for_U.S._Presidential_elections");
    	try {
			createTable("Election");
    		createTable("results");
    		createTable("Participated");
    		createTable("Yield");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	load1(population);
    	load2(election_res);
    	load3(poll_res);
    }	
   
}

//}