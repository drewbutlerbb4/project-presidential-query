<html>
	<head><title>Vice President to President Query</title></head>
	<center><body>
		<h1>PresidentialQuery</h1>
		
		<h3><br/><br/>This is a query for Vice-Presidents who became Presidents</h3>

		<%@ page import = "java.sql.*" %>
		
		<%
		Connection con;
		Class.forName("com.mysql.jdbc.Driver");
		con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/PresidentialQuery", "myuser", "xxxx");
		Statement stmt = con.createStatement();
		
		String query = "SELECT DISTINCT p.NAME, p.YEAR, y.YEAR ";
		query +="FROM Participated p, Yield y ";
		query +="WHERE p.Name = y.Winner AND p.Running_Position = 'vp' AND p.Year < y.Year;";
		
		ResultSet rs=stmt.executeQuery(query);
		%>
		
		<hr>
			<form method="get">
				<table border=1 cellpadding=5>
					<tr>
						<th>Name</th>
						<th>Year of Vice-Presidency</th>
						<th>Year of Presidency</th>
					</tr>
				<%
				while (rs.next()) {
				%>
					<tr>
						<td><%= rs.getString("p.NAME") %></td>
						<td><%= rs.getString("p.YEAR") %></td>
						<td><%= rs.getString("y.YEAR") %></td>
					</tr>
				<%
				}
				%>
				</table>
				<br>
			</form>
	</body></center>
</html>