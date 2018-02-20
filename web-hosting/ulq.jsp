<html>
	<head><title>Unexpected Win Query</title></head>
	<center><body>
		<h1>PresidentialQuery</h1>
		
		<h3><br/><br/>This is a query for Presidential Candidates that won the Election, but lost the Popular Vote </h3>

		<%@ page import = "java.sql.*" %>
		
		<%
		Connection con;
		Class.forName("com.mysql.jdbc.Driver");
		con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/PresidentialQuery", "myuser", "xxxx");
		Statement stmt = con.createStatement();
		
		String query = "SELECT DISTINCT y.Winner, r1.YEAR, r1.ELECTORAL_VOTE, r1.POPULAR_VOTE, r2.NAME, r2.ELECTORAL_VOTE, r2.POPULAR_VOTE ";
		query +="FROM Results r1, Results r2, Yield y  ";
		query +="WHERE y.Year = r1.Year AND r1.Year = r2.Year AND ";
		query +="y.Winner = r1.Name AND r1.popular_vote < r2.popular_vote AND r2.Name <> r1.Name ";
		query +="ORDER BY r1.YEAR desc;";
		
		ResultSet rs=stmt.executeQuery(query);
		%>
		
		<hr>
			<form method="get">
				<table border=1 cellpadding=5>
					<tr>
						<th>President</th>
						<th>Year of Election</th>
						<th>President's Electoral Vote</th>
						<th>President's Popular Vote</th>
						<th>Opponent</th>
						<th>Opponent's Electoral Vote</th>
						<th>Opponent's Popular Vote</th>
					</tr>
				<%
				while (rs.next()) {
				%>
					<tr>
						<td><%= rs.getString("y.WINNER") %></td>
						<td><%= rs.getString("r1.YEAR") %></td>
						<td><%= rs.getString("r1.ELECTORAL_VOTE") %></td>
						<td><%= rs.getString("r1.POPULAR_VOTE") %></td>
						<td><%= rs.getString("r2.NAME") %></td>
						<td><%= rs.getString("r2.ELECTORAL_VOTE") %></td>
						<td><%= rs.getString("r2.POPULAR_VOTE") %></td>
					</tr>
				<%
				}
				%>
				</table>
				<br>
			</form>
		
	</body></center>
</html>