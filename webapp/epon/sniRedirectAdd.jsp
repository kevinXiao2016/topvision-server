<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = <s:property value='entityId'/>;
var ports;
$(function() {
	//加载源端口
	$.ajax({
		type: 'GET',
		url: '/epon/loadAvailableSniRedirect.tv?entityId=' + entityId,
		dataType: 'json',
		success: function(json) {
			if (json) {
				for (var i = 0; i < json.length; i++) {
					ports = json;
					$('#srcPortName').append('<option value="'+ json[i].topSniRedirectGroupSrcPortId + '">' + json[i].srcPortName + '</option>');
				}
			}
		}
	});
	
	//加载目的端口       add by flackyang @2013-11-13
	$.ajax({
		type: 'GET',
		url: '/epon/loadAllSniRedirect.tv?entityId=' + entityId,
		dataType: 'json',
		success: function(json) {
			if (json) {
				for (var i = 0; i < json.length; i++) {
					$('#dstPortName').append('<option value="'+ json[i].topSniRedirectGroupSrcPortId + '">' + json[i].srcPortName + '</option>');
				}
			}
		}
	});
});

function addSniRedirect() {
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.addingRedirect , 'ext-mb-waiting');
	Ext.Ajax.request({
		url : '/epon/addSniRedirect.tv?r=' + Math.random(),
		success : function(response) {
            if (response.responseText) {
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.addRedirectEr )
            } else {
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.addRedirectOk )
                window.parent.getWindow("sniRedirect").body.dom.firstChild.contentWindow.redirectStore.load();
                window.parent.getWindow("sniRedirect").body.dom.firstChild.contentWindow.checkAvailiable();
                cancelClick();
            }
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.addRedirectEr )
		},
		params : {
			entityId: entityId,
			sniRedirectDirection: $('#direction').val(),
			sniRedirectSrcPortId: $('#srcPortName').val(),
			sniRedirectDstPortId: $('#dstPortName').val()
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('sniRedirectAdd');
}
</script>
</head>
	<body class=openWinBody>
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@SERVICE.addRedirect@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt w220">@SERVICE.originPort@:</td>
						<td><select id="srcPortName" class="normalSel w150" ></select></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@EPON.direct@:</td>
						<td><select id="direction" class="normalSel w150" disabled>
								<option value="1">@EPON.indirect@</option>
								<option value="2">@EPON.outdirect@</option>
								<option value="3">@SERVICE.doubleDirect@</option>
						</select></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@EPON.targetPort@:</td>
						<td><select id="dstPortName" class="normalSel w150" >
						</select></td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="addSniRedirect()" icon="miniIcoAdd">@SERVICE.add@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>