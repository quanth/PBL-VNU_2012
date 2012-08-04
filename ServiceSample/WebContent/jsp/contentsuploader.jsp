<%@ page language="java" contentType="text/html; charset=windows-31j"
	pageEncoding="windows-31j"%>
<%@ page import="java.lang.Integer"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-31j">
<title>Upload Contents</title>
</head>
<body>
<h1>Contents Upload</h1>
Connecting robots: <% String ids = (String)request.getAttribute("CONNECTED_ROBOT_IDS"); %>
<%= ids != null ? ids : "" %>
<form action="contentsupload" method="post" name="inputform">
Contents to upload: <br>
<textarea name="contentsdata" rows="5" cols="30">The wise man knows he knows nothing, the fool thinks he knows all.</textarea><input
	type="submit"><br>
</form>

<%
	Integer count = (Integer) request.getAttribute("DISTRIBUTE_COUNT");
	if (count != null && count != 0) {
%>
<%=count%>台のロボットに配信しました。
<%
	}
%>
</body>
</html>