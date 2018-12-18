<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    css help.help
    module platform
</Zeta:Loader>
</head>
<body class="openWinBody edge10">
    <h2 class="h2_bubble">端口信息</h2>
    <div class="edge10">
    	<h4 class="orangeTxt">1、设置 隐藏/显示 列</h4>
    	<p class="pT5">端口信息列表页面支持的列比较多，导致表格有比较长的滚动条，您可以通过设置隐藏您不常用的列。</p>
    	<img class="pT10" src="../images/oltPortInfo_zh_1.jpg" alt="" />
    	
    </div>
</body>
</Zeta:HTML>
