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
    <h2 class="h2_bubble">CM list</h2>
    <div class="edge10">
    	<h4 class="orangeTxt">1, set the Hide / Show columns</h4>
    	<p class="pT5">cm column list page support more, resulting in a relatively long table scrollbar, you can set up to hide the columns that you do not frequently used.</p>
    	<img class="pT10" src="../images/cmlist_en_1.jpg" alt="" />
    	<h4 class="orangeTxt">2. Drag the table column can be changed to show the order of the columns.</h4>
    	<p class="pT5">Sometimes, the information you expect to see some of the earlier columns, you can drag the column to sort the table columns.</p>
    	<img class="pT10" src="../images/cmlist_en_2.jpg" alt="" />
    </div>
</body>
</Zeta:HTML>
