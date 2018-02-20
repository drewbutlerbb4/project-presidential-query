<html>
	<head><title>Swing Candidates Query</title></head>
	<center><body>
		<h1>PresidentialQuery</h1>
		<h3><br/><br/>This is a query for Candidates who have ran under two or more different parties </h3>
		
		<%@ page import = "java.sql.*" %>
		
		<%
		Connection con;
		Class.forName("com.mysql.jdbc.Driver");
		con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/PresidentialQuery", "myuser", "xxxx");
		Statement stmt = con.createStatement();
		
		String query = "SELECT DISTINCT p.NAME, p.PARTY, p2.PARTY ";
		query +="FROM Participated p, Participated p2 ";
		query +="WHERE p.Name = p2.Name AND p.Party <> p2.party ";
		query +="AND p.Party IS NOT NULL AND p2.party IS NOT NULL ";
		query +="AND p.YEAR > p2.YEAR AND !(p.PARTY like '%NULL%') ";
		query +="AND !(p.PARTY like '%none%') AND !(p2.PARTY like '%NULL%') ";
		query +="AND !(p2.PARTY like '%none%');"; 
		
		ResultSet rs=stmt.executeQuery(query);
		%>
		
		<hr>
			<form method="get">
				<table border=1 cellpadding=5>
					<tr>
						<th>Candidate</th>
						<th>Party 1</th>
						<th>Party 2</th>
					</tr>
				<%
				while (rs.next()) {
				%>
					<tr>
						<td><%= rs.getString("p.NAME") %></td>
						<td><%= rs.getString("p.PARTY") %></td>
						<td><%= rs.getString("p2.PARTY") %></td>
					</tr>
				<%
				}
				%>
				</table>
				<br>
			</form>
	</body></center>
</html>