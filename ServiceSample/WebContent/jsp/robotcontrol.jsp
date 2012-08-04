<%@ page language="java" contentType="text/html; charset=windows-31j"
    pageEncoding="windows-31j"%>
 <%@ page import="java.lang.Integer" %>
 <%@ page import="java.util.Enumeration" %>
 <%@ page import="rsnp.acceptor.RobotWorkerManager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-31j">
<title>robot control</title>
</head>
<style type="text/css">
div.box {
	border: 1px solid black;
	padding: 10px;
	margin: 10px;
}
</style>
<script type="text/javascript">
	//-->
</script>
<body>
<h1>Robot Control</h1>
<%
RobotWorkerManager rwm = RobotWorkerManager.getInstance();
Enumeration<String> en = rwm.getRobotIds();
	while (en.hasMoreElements()) {
		String rid = en.nextElement();
%>
<form action="robotcontrol" method="post" name="inputform">
<br>
<input type=submit name=MySubmit value=forward>
<input type=submit name=MySubmit value=back>
<input type=submit name=MySubmit value=right>
<input type=submit name=MySubmit value=left>
<input type=submit name=MySubmit value=stop>
<input type=hidden value=<%=rid%>>
</form>
<%
	}
%>
</body>
</html>