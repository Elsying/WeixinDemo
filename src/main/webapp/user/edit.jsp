<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>    
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

%>            
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑用户界面</title>
</head>
<body>
<h1>修改用户</h1>
id:<input type="text" name="id" value="${user.id}" readonly="readonly">
<br/>
名字:<input type="text" name="name" value="${user.name}">
<br/>
性别:<input type="radio" name="sex" value="0" <c:if test="${user.sex==0}">checked="checked"</c:if>>男<input value="1" type="radio" name="sex" <c:if test="${user.sex==1}">checked="checked"</c:if>>女
<br/>
<button onclick="updateUser()">修改用户</button>
<script type="text/javascript" src="<%=path%>/static/plugins/jquery/2.1.4/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
function updateUser(){
	var id = $("input[name='id']").val();
	var name = $("input[name='name']").val();
	var sex = $("input[name='sex'][checked]").val();
	alert(id+" "+name+" "+sex);
	$.ajax({  
        type: 'put',  
        url: '<%=path%>'+'/users/'+id,  
        data: {id:id,name:name,sex:sex},
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