<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
</Zeta:Loader>
<script type="text/javascript">
var entityId = <%=request.getParameter("entityId")%>;
var currentId = '<%=request.getParameter("currentId")%>';
var fanCardId = <%=request.getParameter("fanCardId")%>;
var fanSpeedLevel = <%=request.getParameter("fanSpeedLevel")%>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
function initData() {
	var options = $("#fanSpeedLevel").get(0).options;
   	for(var i = 0; i < options.length; i++) {
	    if (options[i].value == fanSpeedLevel) {
	        options[i].selected = true;
	        break;
    	}
    }
}
function saveClick() {
	var level = $("#fanSpeedLevel").val();
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.EPON.cfmSave , function(type) {
		if (type == 'no') {return;}
		window.top.showWaitingDlg(I18N.COMMON.wait,  I18N.COMMON.saving , 'ext-mb-waiting');
		Ext.Ajax.request({
			url:"/epon/fanSpeedAdjust.tv?r=" + Math.random(),
			success: function (response) {
				window.parent.closeWaitingDlg();
				var json = Ext.util.JSON.decode(response.responseText);
				if (json.message) {
					top.nm3kRightClickTips({
	    				title: I18N.COMMON.tip,
	    				html: json.message
	    			});
				} else {
					top.nm3kRightClickTips({
	    				title: I18N.COMMON.tip,
	    				html: I18N.EPON.saveOk
	    			});
					window.parent.getFrame("entity-" + entityId).fanConfigChange(fanCardId, level);
					cancelClick();
				}
		    }, failure: function () {
		        window.parent.closeWaitingDlg();
		        top.nm3kRightClickTips({
    				title: I18N.COMMON.tip,
    				html: I18N.EPON.saveError
    			});
		        cancelClick();
		    }, params: {entityId : entityId, fanCardId : fanCardId, oltFanSpeedControlLevel : level}
		});
	});
}
function checkChange() {
	var fanSpeedLevel2 = $("#fanSpeedLevel").val();
	if (fanSpeedLevel2 != fanSpeedLevel) {
		$("#saveBt").attr("disabled", false);
	} else {
		$("#saveBt").attr("disabled", true);
	}
}
function cancelClick() {
	// 关闭窗口后，面板页面重新计时。
	window.parent.getFrame("entity-" + entityId).timer = 0;
	window.parent.closeWindow('fanConfig');
}
function updateOltJson(currentId, attr, value) {
	window.parent.getFrame("entity-" + entityId).updateOltJson(currentId, attr, value);
}
function authLoad(){
	if(!operationDevicePower){
		$("#fanSpeedLevel").attr("disabled",true)
		R.saveBt.setDisabled(true);
	}
}
</script>
</head>
<body class="openWinBody" onload="initData();authLoad();">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@EPON.fanSpeedAdjust@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edge20">
			<table class="mCenter zebraTableRows" >
			<tr>
				<td class="rightBlueTxt w200 withoutBorderBottom">@EPON.fanSpeedSet@</td>
				<td class="withoutBorderBottom"><select id="fanSpeedLevel" onchange="checkChange()" class="normalSel w200">
						<option value="1">@EPON.auto@</option>
						<option value="2">@EPON.slow@</option>
						<option value="3">@EPON.medium@</option>
						<option value="4">@EPON.high@</option>
				</select></td>
			</tr>
		</table>
		</div>
	<Zeta:ButtonGroup>
		<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>