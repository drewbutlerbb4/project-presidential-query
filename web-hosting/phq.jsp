<html>
	<head><title>Party History</title></head>
	<body><center>
	<h1>PresidentialQuery</h1>
	<h3><br/><br/>Enter the name of a Political Party</h3>
	<form method="get">
		<input type="text" name="partyname">
		<input type="submit" value="Query">
	</form>
 
	<%
	String[] partyname = request.getParameterValues("partyname");
	String helper;
	int count = 0;
	int count2 = 0;
	double percentage;
	if (partyname!=null) {
	%>
    
	<h3><br/></br>You have selected the Political Party:</h3>
	
    <h4><%= partyname[0] %></h4>
	
	<%@ page import = "java.sql.*" %>
		
		<%
		Connection con;
		Class.forName("com.mysql.jdbc.Driver");
		con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/PresidentialQuery", "myuser", "xxxx");
		Statement stmt = con.createStatement();
		
		String query = "SELECT cand.PARTY, cand.YEAR, cand.NAME, yield.WINNER ";
		query+="FROM Participated cand, Yield yield ";
		query+="WHERE cand.PARTY='"+partyname[0]+"' AND cand.YEAR=yield.YEAR ";
		query+="AND cand.RUNNING_POSITION='p' ";
		query+="ORDER BY cand.YEAR;";
		
		ResultSet rs=stmt.executeQuery(query);
		%>
		
		<hr>
			<form method="get">
				<table border=1 cellpadding=5>
					<tr>
						<th>Party Name</th>
						<th>Year</th>
						<th>Party Presidential Candidate</th>
						<th>Party Won Election</th>
					</tr>
				<%
				while (rs.next()) {
				%>
					<tr>
						<td><%= rs.getString("cand.PARTY") %></td>
						<td><%= rs.getString("cand.YEAR") %></td>
						<td><%= rs.getString("cand.NAME") %></td>
						<% if( rs.getString("cand.NAME").equals(rs.getString("yield.WINNER"))){
									helper = "True";
									count = count+1;
								}
								else{
									helper = "False";
									count2 = count2+1;
						}%>
						<td><%= helper %></td>
					</tr>
				<%
				}
				%>
				</table>
				<br>
			</form>
			<% percentage = (100)*((double)(count)/(count+count2)); %>
			<h3>This party has won <%= count %> elections and has lost <%= count2 %> elections</h3>
			<h3>They have a <%= percentage %> % win rate</h3>
	<%
	}
	%>
	</center></body>
</html>