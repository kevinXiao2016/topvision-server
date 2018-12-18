<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>

<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
    import js/jquery/jquery.wresize
    import epon.standardOlt.standardOltPortList
    css css/white/disabledStyle
</Zeta:Loader>

<style type="text/css">
.txtLeftTh{text-indent: 16px;}
.partTwoUl{ float:left; width:100%;}
.partTwoUl_li{ float:left; width:48%; position:relative; padding-top:10px;}
label.flagOpen{ padding-left:20px; background:url(/images/flagOpen.gif) no-repeat 0px center; cursor:pointer;}
label.flagClose{ padding-left:20px; background:url(/images/flagClose.gif) no-repeat 0px center; cursor:pointer;}

</style>

<script type="text/javascript">
	var entityId = ${entityId};
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	
</script>
</head>
	<body class="whiteToBlack">
		<div id=header >
    		<%@ include file="/epon/inc/navigator_standardOlt.inc"%>
		</div>
		<div id="putSniGrid"></div>
		<div id="putPonGrid"></div>
		
	</body>
</Zeta:HTML>
