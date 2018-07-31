<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
   System.out.println("basepath : "+basePath);
%>     
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户展现界面</title>
</head>
<body>
<a href="<%=basePath%>users/create">用户添加</a>
<table>
<tr>
<th>id</th>
<th>姓名</th>
<th>性别</th>
<th colspan="2">操作<th>
</tr>
<c:forEach items="${users}" var="user">
<tr>
<td>${user.id}</td>
<td>${user.name}</td>
<td>${user.sex}</td>
<td><button onclick="javascript:deleteUser(${user.id})">删除</button></td>
<td><button onclick="javascript:editUser(${user.id})">修改</button></td>
</tr>
</c:forEach>
</table>
<script type="text/javascript" src="<%=path%>/static/plugins/jquery/2.1.4/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
function deleteUser(userId){
	$.ajax({  
        type: 'delete',  
        url: '<%=path%>'+'/users/'+userId,  
        success: function(data){  
            alert(data.message);
        },  
        error: function(data){  
            alert(2);  
        }  
    });  
}
function editUser(userId){
	window.location.href='<%=path%>'+'/users/'+userId;
}

</script>
</body>
</html>