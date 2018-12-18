<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.util.CurrentRequest" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Zeta
	library Ext
	library Jquery
	module report
	import report.javascript.ReportTaskUtil
	import js.tools.numberInput
	import js.json2
	css css.reportConfig
</Zeta:Loader>

<style type="text/css">
.floatLeftDiv{
	float: left;
	margin: 2px 5px;
	vertical-align: middle;
	white-space: nowrap;
	word-spacing: normal;
}
.floatLeftDiv input,  .floatLeftDiv select{
	vertical-align: middle;
}
</style>

<script type="text/javascript">
var WINDOW_HEIGHT = 500;
function prevClick() {
	window.history.go(-1);
}

function commit(){
	var zetaCallback =  window.top.ZetaCallback;
	//报表的标题以及签名
	if(!ReportTaskUtil.check(zetaCallback))return;
	//封装condition
	var conditionMap = zetaCallback.conditionMap;
		conditionMap.entityType = $("#entityType").val();
		conditionMap.entityIp = $("#entityIp").val();
		conditionMap.adminState = $("#adminState").val();
		conditionMap.operationState = $("#operationState").val();
	zetaCallback.conditions = JSON.stringify(conditionMap);
	/*提交任务*/
	ReportTaskUtil.commit(zetaCallback);
}

$(document).ready(function(){
	$("INPUT#title").val(window.top.ZetaCallback.templateText);
	//构造设备类型下拉框
	  var deviceTypePosition = Zeta$('entityType');
	  var oltType = EntityType.getOltType();
	  $.ajax({
	      url:'/entity/loadSubEntityType.tv?type=' + oltType,
	      type:'POST',
	      dateType:'json',
	      success:function(response) {
	    	  var entityTypes = response.entityTypes 
	    	  for(var i = 0; i < entityTypes.length; i++){
		            var option = document.createElement('option');
		            option.value = entityTypes[i].typeId;
		            option.text = entityTypes[i].displayName;
		            try {
		            	deviceTypePosition.add(option, null);
		            } catch(ex) {
		            	deviceTypePosition.add(option);
		            }
	    	  }
	    	//去除8601选项,8601设备类型会去掉，暂时remove
	    	$("#entityType").find("option[value=10001]").remove();
	      },
	      error:function(entityTypes) {},
	      cache:false
	  });
});

function cancelClick() {
	window.top.ZetaCallback = null;
	ReportTaskUtil.refreshTaskTree();
	window.top.closeWindow("modalDlg");
}
</script>

</head>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@report.reportProperty@</div>
		<div class="rightCirIco pageCirIco"></div>
	</div>
	
	<div id="content" class="edgeTB10LR20 pT20">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td class="rightBlueTxt" width="150">@report.reportTitle@<span class="required">*</span>:</td>
					<td><input id="title" type="text" maxlength=32 style="width: 310px;" class="normalInput" /></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt" width="150">@report.reportAuthor@<span class="required">*</span>:</td>
					<td><input id="author" type="text" maxlength=32 style="width: 310px;" class="normalInput" value="<%=CurrentRequest.getCurrentUser().getUser().getFamilyName() %>"/></td>
				</tr>
				<tr>
					<td class="rightBlueTxt" width="150">@report.displayFields@:</td>
					<td>
						<div class="floatLeftDiv">
							<input checked="checked" type="checkbox" disabled="disabled"/>@report.device@
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" type="checkbox" disabled="disabled"/>@label.portIndex@
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" type="checkbox" disabled="disabled"/>@label.portDescr@
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" type="checkbox" disabled="disabled"/>@label.portType@
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" type="checkbox" disabled="disabled"/>@label.adminStatus@
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" type="checkbox" disabled="disabled"/>@label.operStatus@
						</div>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@report.condtions@:</td>
					<td>
						<div class="floatLeftDiv">@report.deviceType@:
							<select id="entityType" name="entityType" class="normalSel">
							    <option value="0">-- @report.chooseCate@ --</option>
							</select>
						</div>
						<div class="floatLeftDiv">@report.deviceIP@:
							<input type="text" id="entityIp" class="normalInput" style="width: 120px;"/>
						</div>
						<div class="floatLeftDiv">@label.adminStatus@:
							<select id="adminState" name="adminState" class="normalSel">
								<option value="0">-- @report.chooseStatus@ --</option>
								<option value="1">UP</option>
								<option value="2">DOWN</option>
							</select>
						</div>
						<div class="floatLeftDiv">@label.operStatus@:
							<select id="operationState" name="operationState" class="normalSel">
								<option value="0">-- @report.chooseStatus@ --</option>
								<option value="1">UP</option>
								<option value="2">DOWN</option>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.reportType@<span class="required">*</span>:</td>
					<td>
						<input type="checkbox" id="excelSupport" checked="checked" disabled="disabled" />EXCEL 
						<input type="checkbox" id="pdfSupport" style="margin-left: 45px;display:none" disabled="disabled"/>
						<input type="checkbox" id="htmlSupport" style="margin-left: 50px;display:none" disabled="disabled"/>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<!-- 第三部分，按钮组合 -->
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="prevClick()" id="BTP" icon="miniIcoArrLeft">@COMMON.prev@</Zeta:Button>
		<Zeta:Button onClick="commit()" id="BTN" icon="miniIcoSaveOK">@COMMON.finish@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" id="BTC" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>