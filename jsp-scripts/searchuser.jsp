<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*"%>
<%@ page import="org.json.*" %>
    
<%
Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://localhost/facebookandroid?user=root&password=test123");
PreparedStatement statement = con.prepareStatement("select * from users where userName like '%" + request.getParameter("query") + "%'");
ResultSet rs = statement.executeQuery();
convertResultSetIntoJSON(rs,response);
con.close();
%>

<%!
public static void convertResultSetIntoJSON(ResultSet resultSet, HttpServletResponse response) throws Exception {
	JSONObject jsonObject = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    while (resultSet.next()) {
        int total_rows = resultSet.getMetaData().getColumnCount();
        JSONObject obj = new JSONObject();
        for (int i = 0; i < total_rows; i++) {
            String columnName = resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase();
            Object columnValue = resultSet.getObject(i + 1);
            // if value in DB is null, then we set it to default value
            if (columnValue == null){
                columnValue = "null";
            }
            /*
            Next if block is a hack. In case when in db we have values like price and price1 there's a bug in jdbc - 
            both this names are getting stored as price in ResulSet. Therefore when we store second column value,
            we overwrite original value of price. To avoid that, i simply add 1 to be consistent with DB.
             */
            if (obj.has(columnName)){
                columnName += "1";
            }
            obj.put(columnName, columnValue);
        }
        jsonArray.put(obj);
    }
    jsonObject.put("users", jsonArray);
    response.getWriter().write(jsonObject.toString());
}
%>