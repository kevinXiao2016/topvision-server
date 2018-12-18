<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head>
<title>UNI mac management</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../include/nocache.inc" %>
<%@ include file="../include/cssStyle.inc" %>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>  
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/zetaframework/IpTextField.js"></script>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var uniId = '<s:property value="uniId"/>';
var realIndex = '<s:property value="realIndex"/>';
var onuId = '<s:property value="onuId"/>';
Ext.BLANK_IMAGE_URL = '../images/s.gif';
var onuObject;
var uniAutoNegoEnableFlag;
var uniAutoNegoStatusFlag;
var status;
var setUniAutoEnableFlag;
var setUniAutoStatusFlag;
Ext.onReady(function () {
	Ext.Ajax.request({
		url:"/onu/onuObjectCreate.tv?onuId="+onuId,
		method:"post",
		success:function (response) {
			json = Ext.util.JSON.decode(response.responseText);
			onuObject = json;
			uniAutoNegoStatusFlag = onuObject.onuUniPortList[realIndex - 1].uniAutoNegotiationLocalTechAbility;
			$("#MacType").val(uniAutoNegoStatusFlag);
			var uniAutoNegoEnable = onuObject.onuUniPortList[realIndex - 1].uniAutoNegotiationEnable;
		 	if (uniAutoNegoEnable == 1){
		 		uniAutoNegoEnableFlag = true;
		 	}else if (uniAutoNegoEnable == 2){
		 		uniAutoNegoEnableFlag = false;
		 	 }
			$("#uniAutoNego").attr("checked",uniAutoNegoEnableFlag);
		},
		failure:function () {
			window.parent.showMessageDlg(I18N.COMMON.tip,I18N.ONU.loadOnuDataError);
		}});
});
function saveClick() {

}
function cancelClick() {
	window.parent.closeWindow('modalDlg');	
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
</script>
</head>
<body class=BLANK_WND scroll="no"
	style="margin: 5px;">
</body>
</html>