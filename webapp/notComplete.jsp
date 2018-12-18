<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    module PLATFORM
</Zeta:Loader>
</head>
<body>
<div style="margin:20px;">
    <img src="/images/404.gif" align="absmiddle"> <fmt:message key="notComplete" bundle="${c}"/>
</div>
</body>
</html>
