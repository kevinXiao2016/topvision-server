<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<HTML><HEAD>
  <TITLE><fmt:message bundle="${resources}" key="COMMON.help" /></TITLE>
  <%@include file="../include/meta.inc"%>

  
  <link rel="STYLESHEET" type="text/css" href="../css/<%= cssStyleName %>/gui.css">
  <script src="../js/zeta-core.js"></script>  
 </HEAD>

<BODY class=MAIN_WND>
</BODY></HTML>
