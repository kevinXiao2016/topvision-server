<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<%@include file="../../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<script>
var timer = null;
var console = null;
function sendCmd() {
	$.ajax({url:'getRunCmdResult.tv', dataType:'json', cache:'false',
		success:function(json){
			if (json.success) {
                doOnUnload();
            }
			doCmdResult(json.result);
		}, error:function(){}
	});
}
function doCmdResult(text) {
    console.value = text;
    console.doScroll("scrollbarPageDown");
}
function doOnload() {
	console = Zeta$("content");
    timer = setInterval("sendCmd()", 300);
}
function doOnUnload() {
    if (timer != null) {
        clearInterval(timer);
        timer = null;
    }
}
function closeClick() {
	doOnUnload();
    window.top.closeWindow("modalDlg");
}
</script>
</head>
<body class=POPUP_WND onload="doOnload()" onunload="doOnUnload()">
	<table width=100% height=100% cellspacing=0 cellpadding=0>
		<tr>
			<td><textarea id=content readonly
					style="overflow: auto; background-color: black; width: 100%; height: 100%; font-size: 10pt; font-weight: bold; color: gray"></textarea>
			</td>
		</tr>
		<tr>
			<td height=45 align=center>
				<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'"
					onclick="closeClick()"><fmt:message key="MenuItem.close" bundle="${resource}"/></button>
			</td>
		</tr>
	</table>
</body>
</html>
