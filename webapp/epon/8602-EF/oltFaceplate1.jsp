<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML vmlSupport="true">
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    PLUGIN Nm3kTabBtn
    MODULE epon
    css css/white/disabledStyle
    IMPORT epon.javascript.SlotConstant
    IMPORT epon.javascript.Olt static
    IMPORT epon/faceplate/oltFaceplate
</Zeta:Loader>
<style type="text/css">
</style>
<script type="text/javascript">
var entityId = '${entity.entityId}';
var typeName = 'PN8602-EF';
</script>
</head>
<body class="newBody overfloatHidden bodyWH100percent">
    <div class="wrapWH100percent overfloatHidden">
	    <!--头部菜单开始 -->
	    <%@ include file="/epon/inc/navigator.inc"%>
	    <!--头部菜单结束 -->
	    
	    <!-- 左侧开始 -->
        <div class="viewLeftPart" id="viewLeftPart">
            <p class="pannelTit">@EPON.oltDeviceTree@</p>
            <div id="viewLeftPartBody" class="viewLeftPartBody clear-x-panel-body"></div>
        </div>
        <!-- 左侧结束 -->
        <div id="viewLeftLine" class="viewLeftLine"></div>
    
    </div>
    
    <script type="text/javascript" src="/js/jquery/nm3kViewLayout.js"></script>
    <bgsound id="sound" autostart=true loop=false />
</body>
</Zeta:HTML>
