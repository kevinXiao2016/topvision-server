<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>升级</title>
</head>
<body>
	 <a class="blueLink" href="itms-services://?action=download-manifest&url=https://172.17.1.254:8111/ios/V<%=request.getParameter("version")%>/EMS.plist">点击此处下载</a>
</body>
</html>