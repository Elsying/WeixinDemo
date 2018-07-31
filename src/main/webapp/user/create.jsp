<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

%>         
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加用户界面</title>
</head>
<body>
<h1>添加用户</h1>
名字:<input type="text" name="name">
<br/>
性别:<input type="radio" name="sex" value="0" checked="checked">男<input value="1" type="radio" name="sex">女
<br/>
<button onclick="addUser()">添加用户</button>
<script type="text/javascript" src="<%=path%>/static/plugins/jquery/2.1.4/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
function addUser(){
	var name = $("input[name='name']").val();
	var sex = $("input[name='sex'][checked]").val();
	$.ajax({  
        type: 'POST',  
        url: '<%=path%>'+'/users',  
        data: {name:name,sex:sex},
        success: function(data){  
            alert(data.message);
        },  
        error: function(data){  
            alert("服务器连接出错...");  
        }  
    });  
	
}
</script>
</body>
</html>