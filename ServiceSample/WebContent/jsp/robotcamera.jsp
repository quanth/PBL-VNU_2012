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
			border:1px solid black;
			padding: 10px;
			margin: 10px;
		}
	</style>
	<script type="text/javascript"><!--
	var ar = new Array();

	function stop(robotid) {
		var timerID = ar[robotid];
		delete ar[robotid];
		clearTimeout(timerID);
	}

	function iterativeGet(robotid) {
		getImage(robotid);

		var timerID = setTimeout("iterativeGet('" + robotid + "')", 15000);
		ar[robotid] = timerID;
	}

	function getImage(robotid) {
		var imgObj = document.getElementById("image_" + robotid);
		var textObj = document.getElementById("text_" + robotid);

		var time = new Date().toTimeString();
		imgObj.src = "robotcamera?robotid=" + robotid + "&lasttime=" + time;
		textObj.value = time;
	}
	//--></script>
</head>
<body>
<h1>Robot Camera</h1>
<%
	RobotWorkerManager rwm = RobotWorkerManager.getInstance();
	Enumeration<String> en = rwm.getRobotIds();
	while (en.hasMoreElements()) {
		String rid = en.nextElement();
%>
<div class="box">
	Robot ID: <%= rid %>
	<div>
		<img id="image_<%= rid %>" src="" width="224" height="126">
	</div>
	<div>
		<input id="text_<%= rid %>" type="text" value="" size="25">
		<button onclick="iterativeGet('<%= rid %>')">Start</button>
		<button onclick="stop('<%= rid %>')">Stop</button>
		<button onclick="getImage('<%= rid %>')">get image</button>
	</div>
</div>
<%
	}
%>
</body>
</html>