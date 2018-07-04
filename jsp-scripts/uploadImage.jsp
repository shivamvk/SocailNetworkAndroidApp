<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*"%>

<%
Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://localhost/facebookandroid?user=root&password=test123");
PreparedStatement statement = con.prepareStatement("update users set userImage=? where userEmail=?");
statement.setString(1, request.getParameter("userImage"));
statement.setString(2, request.getParameter("userEmail"));
statement.executeUpdate();
response.getWriter().write("okay");
con.close();
%>
