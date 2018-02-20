<html>
	<head><title>Electoral Season</title></head>
	<body><center>
	<h1>PresidentialQuery</h1>
	<h3><br/><br/>Enter a year</h3>
	<form method="get">
		<input type="text" name="year">
		<input type="submit" value="Query">
	</form>
 
	<%
	String[] year = request.getParameterValues("year");
	if (year!=null) {
	%>
    
	<h3><br/></br>You have selected the year:</h3>
	
    <h4><%= year[0] %></h4>
	
	<%@ page import = "java.sql.*" %>
		
		<%
		Connection con;
		Class.forName("com.mysql.jdbc.Driver");
		con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/PresidentialQuery", "myuser", "xxxx");
		Statement stmt = con.createStatement();
		
		String query = "SELECT y.YEAR, p.NAME, vp.NAME, p.PARTY, res.POPULAR_VOTE, res.ELECTORAL_VOTE, res.Poll_DATA ";
		query+="FROM Results res, Yield y, Participated p, Participated vp ";
		query+="WHERE p.Party=vp.Party and p.NAME!=vp.NAME and y.WINNER=p.NAME and y.YEAR="+year[0]+" ";
		query+="and res.YEAR=y.YEAR and p.running_position = 'p' AND vp.running_position = 'vp' and y.Year = p.year ";
		query+="and p.year = vp.year and res.Name = p.Name ";
		query+="ORDER BY y.YEAR;";
		
		ResultSet rs=stmt.executeQuery(query);
		%>
		
		<hr>
			<form method="get">
				<table border=1 cellpadding=5>
					<tr>
						<th>Year of Election</th>
						<th>President</th>
						<th>Vice President</th>
						<th>Party</th>
						<th>Popular Vote For</th>
						<th>Electoral Votes For</th>
						<th>Poll Votes For</th>
					</tr>
				<%
				while (rs.next()) {
				%>
					<tr>
						<td><%= rs.getString("y.YEAR") %></td>
						<td><%= rs.getString("p.NAME") %></td>
						<td><%= rs.getString("vp.NAME") %></td>
						<td><%= rs.getString("p.PARTY") %></td>
						<td><%= rs.getString("res.POPULAR_VOTE") %></td>
						<td><%= rs.getString("res.ELECTORAL_VOTE") %></td>
						<td><%= rs.getString("res.Poll_DATA") %></td>
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