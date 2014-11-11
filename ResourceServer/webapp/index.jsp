<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String userid=request.getParameter("userid");
	String res=request.getParameter("res");
	String type="audio/mpeg";
	if(res.endsWith(".ogg")){
		type="audio/ogg";
	}
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>分享</title>
</head>
<body>
<audio controls="controls" autoplay="autoplay">
  <source src="http://mg-player-data.oss-cn-qingdao.aliyuncs.com/<%=userid %>/<%=res %>" type="<%=type %>">
</audio>

<a href="#">下载游戏</a>
</body>
</html>