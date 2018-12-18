<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
		<%@include file="/include/ZetaUtil.inc"%>
		<Zeta:Loader>
			library zeta
			library Jquery
			plugin Portlet
			module network
			import network.dashboard
			import js/jquery/zebra
		</Zeta:Loader>
		
		<script> 
			var viewport = null;
			var portlets = [];
			var urls = [];
			var functions = [];
			var deviceViewLeft = '${deviceViewLeft}';
			var deviceViewRight = '${deviceViewRight}';
			//自动刷新时间
			var autoRefreshTime = <%= uc.getAutoRefreshTime() %>;
			var dispatchInterval = autoRefreshTime * 1000;
			
			var supportCmc = false;//权限是否支持cmc;
			var supportEpon = false;
			<% if(uc.hasSupportModule("cmc")){%>
				supportCmc = true;
			<% } %>
			<% if(uc.hasSupportModule("olt")){%>
				supportEpon = true;
			<% } %>
			Ext.BLANK_IMAGE_URL = '../images/s.gif';
		</script>
	</head>
	<body class=BLANK_WND>
	</body>
</Zeta:HTML>