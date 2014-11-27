<%@page import="java.net.URLDecoder"%>
<%@page import="utils.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String userid=request.getParameter("userid");
	String res=request.getParameter("res");
	String songName=request.getParameter("songName");
	String auth=request.getParameter("auth");
	String type="audio/mpeg";
	if(res.endsWith(".ogg")){
		type="audio/ogg";
	}
	
	if(StringUtils.isBlank(songName)){
		songName="歌曲名";
	}else{
		songName=URLDecoder.decode(songName, "utf-8");
	}
	if(StringUtils.isBlank(auth)){
		auth="作者";
	}else{
		auth=URLDecoder.decode(auth, "utf-8");
	}
%>

<!DOCTYPE html>
<html lang="zh-cn">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>乐器大师</title>

    <!-- Bootstrap -->
    <link href="/lib/boostrap/bootstrap.min.css" rel="stylesheet">
    <link href="/lib/Flat-UI-master/dist/css/flat-ui.min.css" rel="stylesheet">
    <link href="/style/main.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <div class="container">
    	<div style="padding: 30px 15px 10px 15px;text-align: center;">
	    	<audio controls="controls" autoplay="autoplay">
				<source src="http://mg-player-data.oss-cn-qingdao.aliyuncs.com/<%=userid %>/<%=res %>" type="<%=type %>">
			</audio>
	    	<div style="margin-top: 10px;">
	    		<div style="font-weight: bolder;color: #000;"><%=songName %></div>
	    		<div style="color: rgba(52, 73, 94, 0.3);font-size: 13px;font-weight: bold;"><%=auth %></div>
	    	</div>
    	</div>
    	<hr style="margin-top: 5px;margin-bottom: 5px;">
	</div>
    
    <footer class="footer">
		<a href="#" class="btn btn-primary btn-large download">下载乐器大师</a>
	</footer>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="/lib/jquery/jquery-2.1.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/lib/boostrap/bootstrap.min.js"></script>
    <script src="/lib/Flat-UI-master/dist/js/flat-ui.min.js"></script>
  </body>
</html>