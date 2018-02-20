<html>
	<head><title>Presidential Candidate</title></head>
	<body><center>
	<h1>PresidentialQuery</h1>
	<h3><br/><br/>Enter a Presidential Candidate</h3>
	<form method="get">
		<input type="text" name="cand">
		<input type="submit" value="Query">
	</form>
 
	<%
	String[] cand = request.getParameterValues("cand");
	if (cand!=null) {
	%>
    
	<h3><br/></br>You have selected the Presidential Candidate:</h3>
	
    <h4><%= cand[0] %></h4>
	
	<%@ page import = "java.sql.*" %>
		
		<%
		Connection con;
		Class.forName("com.mysql.jdbc.Driver");
		con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/PresidentialQuery", "myuser", "xxxx");
		Statement stmt = con.createStatement();
		
		String query = "SELECT cand.NAME, cand.YEAR, cand1.RUNNING_POSITION, year.WINNER, cand.ELECTORAL_VOTE, winner.ELECTORAL_VOTE ";
		query+="FROM Results cand, Yield year, Results winner, Participated cand1, Participated cand2 ";
		query+="WHERE cand.NAME like '%"+cand[0]+"%' and cand.YEAR=year.YEAR ";
		query+="and year.YEAR=winner.YEAR and year.WINNER=winner.NAME ";
		query+="and cand.NAME=cand1.NAME and cand.YEAR=cand1.YEAR and cand1.YEAR=cand2.YEAR ";
		query+="and cand1.RUNNING_POSITION like '%p%' and cand2.RUNNING_POSITION like '%p%' ";
		query+="and cand2.NAME=winner.NAME;";
		
		ResultSet rs=stmt.executeQuery(query);
		%>
		
		<hr>
			<form method="get">
				<table border=1 cellpadding=5>
					<tr>
						<th>Name</th>
						<th>Year Ran</th>
						<th>Ran For</th>
						<th>Winner of that Year</th>
						<th>Electoral Votes For</th>
						<th>Electoral Votes For Winner</th>
					</tr>
				<%
				while (rs.next()) {
				%>
					<tr>
						<td><%= rs.getString("cand.NAME") %></td>
						<td><%= rs.getString("cand.YEAR") %></td>
						<td><%= rs.getString("cand1.RUNNING_POSITION") %></td>
						<td><%= rs.getString("year.WINNER") %></td>
						<td><%= rs.getString("cand.ELECTORAL_VOTE") %></td>
						<td><%= rs.getString("winner.ELECTORAL_VOTE") %></td>
					</tr>
				<%
				}
				%>
				</table>
				<br>
			</form>
		
	
	<%
	}
	%>
	</center></body>
</html>