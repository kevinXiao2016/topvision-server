<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
	library ext
	library FileUpload
    module network
    import network/popBatchEntity
    CSS css/white/disabledStyle
    CSS js/webupload/webuploader
	IMPORT js/webupload/webuploader.min
	import network/popBatchEntity2
    
</Zeta:Loader>
<HTML>
<HEAD>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style type="text/css">
.entityTypeSpan{
	display: inline-block;
	width: 150px;
	margin-right: 10px;
}
</style>
<script type="text/javascript">
var folderId = <%= request.getParameter("folderId") %>;
var entityTypes = ${entityTypesJson};
var snmpRetries = ${snmpRetries};
var entityType = [];
var snmpList = [{
	id: 1,
	name: 'default',
	readCommunity: 'public',
	writeCommunity: 'private',
	version: 1,
	username: null,
	authProtocol: null,
	authPassword: null,
	privProtocol: null,
	privPassword: null
}];
</script>
</HEAD>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip">@batchTopo.windowLabel@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>

	<div class="edge10">
		<table class="mCenter zebraTableRows">
			<!-- <tr>
				<td class="blueTxt withoutBorderBottom w100"><label id="ipLabel">@batchTopo.importExcel@:</label></td>
				<td class="withoutBorderBottom" width="645" colspan="2">
					<div id='file_div' style='float:left; margin-right:5px;'>
						<input id="batchIpText" class="normalInputDisabled floatLeft" type="text" disabled="disabled" /> 
						<a id="chooseFile" href="javascript:;" class="nearInputBtn"><span>@NAMEIMPORT.import@ Excel</span></a>
					</div>
					
				</td>
			</tr> -->
			<tr>
				<td class="blueTxt w100">@batchTopo.importExcel@:</td>
				<td>
					<input id="fileNameInput" class="normalInputDisabled floatLeft" type="text" disabled="disabled">
					<div id="picker">@NAMEIMPORT.import@ Excel</div>
				</td>
				<td>
					<a id="downLoadFile" href="javascript:;" class="normalBtn"><span><i class="miniIcoArrDown"></i>@batchTopo.downExcelFile@</span></a>
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="blueTxt">@workbench/td.deviceType@</td>
				<td id="deviceTypeTb" colspan="2">
					<div>
						<input id="allCheck" type="checkbox" checked="checked" />@workbench/select.all@
					</div>
				</td>
			</tr>
			<tr>
				<td class="blueTxt withoutBorderBottom">
					@batchTopo.snmpLabel@:
				</td>
				<td class="withoutBorderBottom">
					<a id="addSnmp" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a> 
				</td>
			</tr>
			<tr>
				<td colspan="3"><div id=spanDiv></div></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="blueTxt">@batchTopo.snmpParamConfig@:</td>
				<td>
					<span class="blueTxt">@batchTopo.retry@:</span>
					<select id=retryCountSel class="normalSel w100">
							<option value=0>0</option>
							<option value=1>1</option>
							<option value=2>2</option>
							<option value=3>3</option>
					</select>
				</td>
				<td>
					<span class="blueTxt">@batchTopo.timeout@:</span>
					<input id="timeoutInput" value="${snmpTimeout}" tooltip='@batchTopo.tip1@' class="normalInput" maxlength=5 />@batchTopo.msecond@
				</td>
			</tr>
			<tr>
				<td class="blueTxt">@batchTopo.pingParamConfig@:</td>
				<td colspan="2">
					<span class="blueTxt">@batchTopo.theTimeout@:</span>
					<input id="pingTimeout" value="${pingTimeoutConfig}" tooltip='@batchTopo.tip2@' maxlength=5 class="normalInput"/>
					@batchTopo.msecond@
				</td>
			</tr>
		</table>
	</div>
	
	<Zeta:ButtonGroup>
		<li>
			<a id="newOkBtn" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@COMMON.finish@</span></a>
		</li>
		<%-- <Zeta:Button id="okBtn" icon="miniIcoSaveOK">@COMMON.finish@</Zeta:Button> --%>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body></Zeta:HTML>
