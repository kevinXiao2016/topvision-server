<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@include file="../include/ZetaUtil.inc"%>

<HTML>
 <HEAD>
  <TITLE></TITLE>
  <link rel="stylesheet" type="text/css" href="../css/gui.css">  

  <script>
 	function reloadCss(style) {
		alert(style);
		new Asset.css('css/' + style + '/gui.css', {id: 'guiCss'});	
	}  
  </script>
 </HEAD>

 <BODY class=MAIN_WND>
 Welcome
 </BODY>
</HTML>
