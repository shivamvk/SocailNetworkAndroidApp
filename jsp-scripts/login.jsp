<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*"%>
    
<%
Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://localhost/facebookandroid?user=root&password=test123");
PreparedStatement statement = con.prepareStatement("select * from users where userEmail=? and userPassword=?");
statement.setString(1, request.getParameter("userEmail"));
statement.setString(2, request.getParameter("userPassword"));
ResultSet rs = statement.executeQuery();
if(rs.next()){
	response.getWriter().write(rs.getString(2)+","+rs.getString(5));
} else{
	response.getWriter().write("error");
}
con.close();
%>