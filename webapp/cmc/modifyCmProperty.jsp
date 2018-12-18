<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<!-- YangYi新增页面@20130910,手动修改CM导入信息 -->
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%=cssStyleName%>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%=cssStyleName%>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc" />
<fmt:setBundle basename="com.topvision.ems.resources.resources"	var="resources" />
<fmt:setBundle basename="com.topvision.ems.network.resources" var="network" />
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%=lang%>.js"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script type="text/javascript" src="/js/zetaframework/Validator.js"></script>
<script src="../js/jquery/nm3kToolTip.js"></script>
<style type="text/css">
.readonlyInput {
	background-color: #DCDCDC;
}
#formChanged input[type="text"]{
	width: 380px;
	padding-left: 5px;
}
</style>

<script type="text/javascript">
	var cmClassified = "<s:property value="cmImportInfo.cmClassified"/>";
	var cmAlias = "<s:property value="cmImportInfo.cmAlias"/>";

	function cancelClick() {
		window.top.closeWindow('modifyCmProperty');
	}
	
	function changeCheck() {
		var changeTag = false;
		if (cmClassified != $("#cmClassified").val()
				|| cmAlias != $("#cmAlias").val()) {
			changeTag = true;
		} else {
			changeTag = false;
		}

		if (changeTag == true) {
			$("#saveBtn").attr("disabled", false);
		} else {
			$("#saveBtn").attr("disabled", true);
		}
	}
	
	function saveClick() {
		// 校验
		var _cmAlias = $.trim($('#cmAlias').val());
		var _cmClassified = $.trim($('#cmClassified').val());
		
		if (_cmAlias && !Validator.isLocationName(_cmAlias)) {
            $("#cmAlias").focus();
            return;
        }
		
		if (_cmClassified && !Validator.isCmClassify(_cmClassified)) {
            $("#cmClassified").focus();
            return;
        }
		
		/* object = Zeta$('cmAlias').value.trim();
		if (object == null || object.length > 80) {
			window.top.showMessageDlg(I18N.RECYLE.tip,  I18N.CCMTS.AliasError);
			return;
		}
		if (object == null || object.length <1 ) {
			window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CCMTS.pleaseInputAlias);
			return;
		}
		
		object = Zeta$('cmClassified').value.trim();
		if (object == null || object.length > 10) {
			window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CCMTS.classifiedError);
			return;
		}
		if (object == null || object.length < 1) {
			window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CCMTS.pleaseInputUsage);
			return;
		} */

		var tipStr = I18N.text.confirmModifyTip;
		
		//window.top.showWaitingDlg(I18N.COMMON.waiting,I18N.text.configuring, 'ext-mb-waiting');
		$.ajax({
			url : '/cm/modifyCmInfo.tv',
			type : 'post',
			data : jQuery(formChanged).serialize(),
			success : function(response) {
				if (response.message == "success") {
					//window.parent.showMessageDlg(I18N.RECYLE.tip,I18N.text.modifySuccessTip);
					top.afterSaveOrDelete({
		   				title: I18N.RECYLE.tip,
		   				html: '<b class="orangeTxt">' + I18N.text.modifySuccessTip + '</b>'
		   			});
				} else {
					window.parent.showMessageDlg(I18N.RECYLE.tip,I18N.text.modifyFailureTip);
				}
				if(window.parent.getFrame("CmImportInfo")!= undefined){
					window.parent.getFrame("CmImportInfo").onRefreshClick();
				}
				if(window.parent.getFrame("cmListNew")!= undefined){
				    window.parent.getFrame("cmListNew").refreshGrid();
				}
				cancelClick();
			},
			error : function(response) {
				window.parent.showMessageDlg(I18N.RECYLE.tip,
						I18N.text.modifyFailureTip);
			},
			cache : false
		});
			
		
	}
</script>
<title><fmt:message bundle="${cmc}" key="text.upChannelConfig" /></title>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	
	<div class="edgeTB10LR20 pT20">
		<form name="formChanged" id="formChanged">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				<tbody>
					<tr>
						<td class="rightBlueTxt" width="120">MAC:</td>
						<td>
							<input class="normalInputDisabled" type="text" id="cmMacAddr" 
								name="cmImportInfo.cmMacAddr"
								value='<s:property value="cmImportInfo.cmMacAddr"/>' readonly="readonly" />
						</td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">
							<span style="color:red;">*</span>
							<fmt:message bundle="${cmc}" key="CMC.title.Alias"/>:
						</td>
						<td>
							<input class="normalInput" type="text" id="cmAlias" name="cmImportInfo.cmAlias"
								value='<s:property value="cmImportInfo.cmAlias"/>' maxlength="127"
								toolTip='<fmt:message bundle="${cmc}" key="COMMON.locationTip"/>' />
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">
							<span style="color:red;">*</span>
							<fmt:message bundle="${cmc}" key="CMC.title.usage"/>:
						</td>
						<td>
							<input class="normalInput" type="text" id="cmClassified" name="cmImportInfo.cmClassified"
							value='<s:property value="cmImportInfo.cmClassified"/>' maxlength="10"
							toolTip='<fmt:message bundle="${cmc}" key="CM.cmClassifiedTip"/>' />
						</td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CMC.title.importDate"/>:</td>
						<td>
							<input class="normalInputDisabled" type="text" id="time" name="cmImportInfo.time"
								value='<s:property value="cmImportInfo.time"/>' readonly="readonly" />
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
    	<ol class="upChannelListOl pB0 pT30 noWidthCenter">
    		<li>
    			<a href="javascript:;" class="normalBtnBig" onclick="saveClick()">
    				<span><i class="miniIcoData"></i><fmt:message bundle="${resources}" key="COMMON.save" /></span>
    			</a>
    		</li>
         	<li>
         		<a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
         			<span><i class="miniIcoForbid"></i><fmt:message bundle="${resources}" key="COMMON.cancel" /></span>
         		</a>
         	</li>
		</ol>
	</div>
</body>
</html>
