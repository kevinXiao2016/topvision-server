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
var entityId = '<s:property value="entityId"/>';

function searchClick() {
	var macAddr = $("#macAddr").val();
	 window.top.showWaitingDlg(I18N.COMMON.wait,  '@COMMON.querying@')
	 $.ajax({
	        url: '/epon/loadMacInfo.tv',
	        data: "entityId="+ entityId +"&matchMacAddr=" + macAddr,
	        dataType:"text",
	        success: function(text) {
	        	window.parent.closeWaitingDlg();
		        if(text != 'fail'){
	            	$('#macInfoTip').text(text);
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, '@MAC.notExist@');
			    }
	        }, error: function(text) {
	        	window.parent.closeWaitingDlg();
	        	window.parent.showMessageDlg(I18N.COMMON.tip, '@MAC.notExist@');
	    }, cache: false
	    });
}

function cancelClick() {
	window.parent.closeWindow('macInfo');
}

</script>
</head>
	<body class=openWinBody>
	<div class="openWinHeader">
		<div class="openWinTip">@MAC.searchInfo@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt w220"@MAC.addr@:</td>
						<td><input type=text id="macAddr" class="normalInput w150"
							 tooltip="@MAC.inputMacAddr@" /></td>
					</tr>
					<tr>
						<td align="center" class="txtCenter" colspan=2><b id="macInfoTip" style="color:#f00;"></b></td>
						
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="fetchBt" onClick="searchClick()" icon="miniIcoEquipment">@COMMON.query@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>