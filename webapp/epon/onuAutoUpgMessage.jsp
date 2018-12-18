<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../include/nocache.inc"%>
<%@ include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" 
	src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '../images/s.gif';
var entityId = <s:property value="entityId"/>;
//0:add, 1:modify
var onuAutoUpgFlag = <s:property value="onuAutoUpgFlag" />;
var messageList = '<s:property value="messageList" />';
messageList = messageList.split("@");

function cancelClick(){
	if(window.parent.closeWindow){
		window.parent.closeWindow(onuAutoUpgFlag ? 'onuAutoUpgProfile' : 'onuAutoUpgAddPro');
	}
}

Ext.onReady(function(){
	var winId = onuAutoUpgFlag ? 'onuAutoUpgProfile' : 'onuAutoUpgAddPro';
	var win = top.getWindow(winId);
    win.setSize(280, 154);
    var pos = win.getPosition();
    var x = (800 - pos[0])/2;
    var y = (500 - pos[1])/2;
    
    top.getWindow(winId).setPosition(pos[0] + x, pos[1] + y);
    
	
	//initButton(75);
	window.parent.closeWaitingDlg();
	var p = $("#mesDiv");
	var str = onuAutoUpgFlag ? I18N.onuAutoUpg.mes.modifyProFailed : I18N.onuAutoUpg.mes.addProFailed;
	if(messageList.indexOf("success") > -1){
		messageList.splice(messageList.indexOf("success"), 1);
		str = onuAutoUpgFlag ? I18N.onuAutoUpg.mes.modifyProSuc : I18N.onuAutoUpg.mes.addProSuc;
	}
	p.append("<div style='text-align:center; width:100%;'>" + str + "</div>");
	for(var a=0; a<messageList.length; a++){
		//p.append("<br><br><nobr>&nbsp;&nbsp;" + messageList[a] + "</nobr>");
	}
	/* p.append(String.format("<span style='position:absolute;top:205;left:220;'>" + I18N.onuAutoUpg.tip.autoCloseWindow + "</span>", 
				"<br><br><br><span id=closeTime>60</span>")); */
	//lessTime(parseFloat($("#closeTime").text()));
	//setTimeout(function(){
		if(window.parent.getWindow){
			window.parent.getWindow("onuAutoUpg").body.dom.firstChild.contentWindow.location.reload();
		}
	//}, 500);
});

function lessTime(s){
	if(s > 0){
		s--;
		$("#closeTime").text(s);
		setTimeout(function(){
			lessTime(s);
		}, 1000);
	}else{
		cancelClick();
	}
}


</script>
</head>
<body class=POPUP_WND>
	<div id="mesDiv" class="pT20 pB20"></div>
	<div class="noWidthCenterOuter clearBoth">
        <ol class="upChannelListOl pB0 pT0 noWidthCenter">
            <li>
                <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
                    <span><i class="miniIcoData"></i>
                    	<fmt:message bundle="${i18n}" key="COMMON.confirm" />
                    </span>
                </a>
            </li>
        </ol>
    </div>
	<%-- <table width=100% height=100% style='padding:15px;'>
		<tr height=90%><td>
			<div id=mesDiv 
			style='width:100%;height:100%;background-color:white;padding:20 10 10 30;overflow-y:auto;border:1px solid #cccccc'></div>
		</td></tr>
		<tr height=10%><td align=center>
			<button class="BUTTON75 normalBt" onclick='cancelClick()'>
				<fmt:message bundle="${i18n}" key="COMMON.close" /></button>
		</td></tr>
	</table> --%>
</body>
</html>