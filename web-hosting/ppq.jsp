<html>
	<head><title>Presidential Period</title></head>
	<body><center>
	<h1>PresidentialQuery</h1>
	<h3><br/><br/>Enter a timeperiod</h3>
	<form method="get">
		<input type="text" name="startyear" value="start">
		<input type="text" name="endyear" value="end">
		<input type="submit" value="Query">
	</form>
 
	<%
	String[] startyear = request.getParameterValues("startyear");
	String[] endyear = request.getParameterValues("endyear");
	if ((startyear!=null)&&(endyear!=null)) {
	%>
    
	<h3><br/></br>You have selected the timeperiod:</h3>
	
    <h4>From: <%= startyear[0] %></h4>
	<h4>To:   <%= endyear[0] %></h4>
	
	<%@ page import = "java.sql.*" %>
		
		<%
		Connection con;
		Class.forName("com.mysql.jdbc.Driver");
		con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/PresidentialQuery", "myuser", "xxxx");
		Statement stmt = con.createStatement();
		
		String query = "SELECT y.YEAR, p.NAME, vp.NAME, p.PARTY, res.POPULAR_VOTE, res.ELECTORAL_VOTE ";
		query+="FROM Results res, Yield y, Participated p, Participated vp ";
		query+="WHERE p.Party=vp.Party and p.NAME!=vp.NAME and y.WINNER=p.NAME and y.YEAR<="+endyear[0]+" and y.YEAR>="+startyear[0]+" ";
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