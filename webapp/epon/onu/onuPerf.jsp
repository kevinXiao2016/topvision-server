<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
    <head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    library Highchart
    PLUGIN Portlet
    PLUGIN Highchart-Ext
    module onu
    import js/nm3k/Nm3kClock
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="/css/nm3kClock.css" />
<!-- 内置自定义js -->
<script type="text/javascript">
    function authLoad(){}
</script>
    </head>
    <body onload="authLoad();" class="newBody clear-x-panel-body">
        <div id="header">
            <%@ include file="navigator.inc"%>
        </div>
	</body>
</Zeta:HTML> 
