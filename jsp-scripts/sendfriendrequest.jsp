<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*"%>

<%
if(request.getParameter("to").equals(request.getParameter("from"))){
	response.getWriter().write("You are already your friend ^_^");
} else{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost/facebookandroid?user=root&password=test123");
	PreparedStatement statement = con.prepareStatement("select * from friends where requestTo=? and requestFrom=?");
	statement.setString(1, request.getParameter("to"));
	statement.setString(2, request.getParameter("from"));
	ResultSet rs = statement.executeQuery();
	if(rs.next()){
		if(rs.getString(4).equals("ACCEPTED")){
			response.getWriter().write("Already friends!");
		} else{
			response.getWriter().write("Already requested!");
		}
	} else {
		PreparedStatement statement1 = con.prepareStatement("select * from friends where requestTo=? and requestFrom=?");
		statement1.setString(1, request.getParameter("from"));
		statement1.setString(2, request.getParameter("to"));
		ResultSet rs1 = statement1.executeQuery();
		if(rs1.next()){
			if(rs1.getString(4).equals("ACCEPTED")){
				response.getWriter().write("Already friends!");
			} else{
				response.getWriter().write("Already requested!");
			}
		} else{
			PreparedStatement statement2 = con.prepareStatement("insert into friends(requestTo,requestFrom) values(?,?)");
			statement2.setString(1, request.getParameter("to"));
			statement2.setString(2, request.getParameter("from"));
			statement2.executeUpdate();
			response.getWriter().write("Friend request sent!");
		}
	}
	con.close();
}
%>