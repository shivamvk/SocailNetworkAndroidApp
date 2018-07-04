<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*"%>

<%
Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://localhost/facebookandroid?user=root&password=test123");
PreparedStatement statement = con.prepareStatement("insert into wallposts(postMessage,postBy,postImage) values(?,?,?)");
statement.setString(1, request.getParameter("postMessage"));
statement.setString(2, request.getParameter("userEmail"));
statement.setString(3, request.getParameter("postImage"));
statement.executeUpdate();
response.getWriter().write("okay");
%>    