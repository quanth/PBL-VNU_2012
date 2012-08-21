<%@ page language="java" contentType="text/html; charset=windows-31j"
	pageEncoding="windows-31j"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="rsnp.acceptor.RobotWorkerManager"%>
<%@page import="java.util.Enumeration"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-31j">
<title>Robot Camera</title>
<style type="text/css">
div.box {
	border: 1px solid black;
	padding: 10px;
	margin: 10px;
}
</style>
<script type="text/javascript">
<!--
	// 	function getGPS(robotid) {
	// 		var textObj = document.getElementById("text_" + robotid);
	// 		var GPS = document.getElementById("gps_" + robotid);

	// 		//var time = new Date().toTimeString();
	// 		textObj.value = time;
	// 		GPS.src = "robotGPS?robotid=" + robotid + "&lasttime=" + time;
	// 		request.getAttribute("dataGPS");
	// 	}
//-->
</script>
</head>
<body>
	<h1>Robot GPS</h1>
	<%
		RobotWorkerManager rwm = RobotWorkerManager.getInstance();
		Enumeration<String> en = rwm.getRobotIds();
		while (en.hasMoreElements()) {
			String rid = en.nextElement();
	%>
	Robot ID:
	<%=rid%>
	<%
		if (request.getParameter("submit") != null) {
	%>
	<div>
		<%=request.getAttribute("dataGPS")%>
	</div>
	<%
		}
	%>
	<FORM NAME="form1" METHOD="POST">
		<INPUT TYPE="SUBMIT" NAME="submit" VALUE="Get GPS">
	</FORM>
	<%
		}
	%>
</body>
</html>