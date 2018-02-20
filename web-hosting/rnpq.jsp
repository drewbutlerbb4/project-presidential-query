<html>
	<head><title>Re-elected, Non-contiguous Query</title></head>
	<center><body>
		<h1>PresidentialQuery</h1>
		<h3><br/><br/>This is a query for Presidents who were re-elected non-contiguously </h3>

		<%@ page import = "java.sql.*" %>
		
		<%
		Connection con;
		Class.forName("com.mysql.jdbc.Driver");
		con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/PresidentialQuery", "myuser", "xxxx");
		Statement stmt = con.createStatement();
		
		String query = "SELECT DISTINCT y.Winner ";
		query +="FROM Yield y, Yield y2 ";
		query +="WHERE y.Winner = y2.Winner AND y.year > y2.year + 4 ";
		query +="AND y.Winner NOT IN ( ";
		query +="	SELECT y3.Winner ";
		query +="	FROM Yield y3,Yield y4 ";
		query +="	WHERE y3.Winner = y4.Winner AND y3.Year = y4.Year + 4);";
		
		ResultSet rs=stmt.executeQuery(query);
		%>
		
		<hr>
			<form method="get">
				<table border=1 cellpadding=5>
					<tr>
						<th>President</th>
					</tr>
				<%
				while (rs.next()) {
				%>
					<tr>
						<td><%= rs.getString("y.Winner") %></td>
					</tr>
				<%
				}
				%>
				</table>
				<br>
			</form>
		
	</body></center>
</html>