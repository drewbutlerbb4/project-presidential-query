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
	if (year != null) {
	%>
    
	<h3><br/></br>You have selected the year:</h3>
	
    <h4><%= year[0] %></h4>
	
	<%@ page import = "java.sql.*" %>
	
	<%
	Connection con;
	Class.forName("com.mysql.jdbc.Driver");
	con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/PresidentialQuery", "myuser", "xxxx");
	Statement stmt = con.createStatement();
	String table1 = "SELECT * from Election";
	ResultSet rs=stmt.executeQuery(table1);
	%>
	
	<hr>
      <form method="get" action="order.jsp">
        <table border=1 cellpadding=5>
          <tr>
            <th>Year</th>
            <th>Population_Size</th>
          </tr>
	<%
    while (rs.next()) {
	%>
          <tr>
            <td><%= rs.getString("Year") %></td>
            <td><%= rs.getString("Population_Size") %></td>
          </tr>
  <%
      }
  %>
        </table>
        <br>
        <input type="submit" value="Order">
        <input type="reset" value="Clear">
      </form>
	
	<%
	}
	%>
	</center></body>
</html>