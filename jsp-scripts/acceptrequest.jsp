<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*"%>

<%
Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://localhost/facebookandroid?user=root&password=test123");
PreparedStatement statement = con.prepareStatement("update friends set status='ACCEPTED' where requestTo=? and requestFrom=?");
statement.setString(1, request.getParameter("to"));
statement.setString(2, request.getParameter("from"));
statement.executeUpdate();
response.getWriter().write("okay");
con.close();
%>