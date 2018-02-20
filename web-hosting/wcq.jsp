<html>
	<head><title>Winless Presidential Candidates Query</title></head>
	<center><body>
		<h1>PresidentialQuery</h1>
		
		<h3><br/><br/>This is a query for Presidential Candidates that have never won an election </h3>

		<%@ page import = "java.sql.*" %>
		
		<%
		Connection con;
		Class.forName("com.mysql.jdbc.Driver");
		con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/PresidentialQuery", "myuser", "xxxx");
		Statement stmt = con.createStatement();
		
		String query = "SELECT DISTINCT part.NAME ";
		query +="FROM Participated part  ";
		query +="WHERE part.Running_position ='p' ";
		query +="AND part.NAME != all (SELECT y.Winner FROM Yield y);";
		

		ResultSet rs=stmt.executeQuery(query);
		%>
		
		<hr>
			<form method="get">
				<table border=1 cellpadding=5>
					<tr>
						<th>Candidate Name</th>
					</tr>
				<%
				while (rs.next()) {
				%>
					<tr>
						<td><%= rs.getString("part.NAME") %></td>
					</tr>
				<%
				}
				%>
				</table>
				<br>
			</form>
		
	</body></center>
</html>