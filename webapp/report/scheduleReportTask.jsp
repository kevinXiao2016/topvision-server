<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
    import js.tools.numberInput
</Zeta:Loader>
<style type="text/css">
.COMPLUS {color:red;}
</style>
<script>
var circleTypeCombo,
	LAST_CYCLE = 1,
	weekStartDay = top.firstDayOfWeek,
	reportTask;
var taskNamesJson = ${taskNamesJson};

function prevClick() {
	window.top.getWindow("modalDlg").setTitle("@report.createReportTask@");
	window.history.go(-1);
}
//页面验证函数
function validate(){
	var reg;
	
	//任务名称验证
	reg = /^[^\\/:*?"<>|]{1,32}$/;
	var taskName = $("#taskName").val();
	if(!Validator.isReportName(taskName)){
		$("#taskName").focus();
		return false;
	}
	//任务描述验证
	//reg = /^.{0,140}$/;
	reg = /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:]{0,140}$/
	
	var note = $("#note").val();
	if(!reg.test(note)){
		$("#note").focus();
		return false;
	}
	return true;
}

function nextClick() {
	//进行验证
	if(!validate()){return;}
	reportTask.taskName = $("#taskName").val();
	//进行任务名称重复性校验
	var taskName = $("#taskName").val();
	for(var i=0; i<taskNamesJson.length; i++){
		if(taskName == taskNamesJson[i]){
			return window.parent.showMessageDlg("@COMMON.tip@", "@report.taskUsed@","error");
		}
	}

	reportTask.cycleType = parseInt($('#cycleType').val(), 10);
	reportTask.note = $("#note").val(); 
	var reportId = reportTask.reportId;
	window.location.href = 'showReportTaskCondition.tv?reportId='+reportId+'&r='+Math.random();
}

function cancelClick() {
	if (window.top.reportTask) {
		window.top.reportTask = null;
	}
	window.top.closeWindow("modalDlg");
}

Ext.onReady(function(){
	reportTask =  window.top.reportTask;
	if(!reportTask) return;
	$('#cycleType').val(reportTask.cycleType || 1)
	$('#taskName').val(reportTask.taskName || '');
	$("#note").val(reportTask.note || '');
});
</script>
</head>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
	
	<!-- 第一部分，说明文字加图标 -->
	<div class="openWinHeader">
		<div class="openWinTip">@report.reportTaskTip@</div>
		<div class="rightCirIco clock3CirIco"></div>
	</div>
	<!-- 第一部分，说明文字加图标 -->
	
	<!-- 第二部分，核心布局区域 -->
	<div class="content edgeTB10LR20">
		<table class="taskTable mCenter zebraTableRows " >
			<tr>
				<td class="rightBlueTxt" width="120">@report.taskName@<font class="COMPLUS">*</font>:</td>
				<td>
					<input class="normalInput" maxlength=63 id="taskName" type="text" style="width: 302px;" tooltip="@COMMON.anotherName@"/>
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt" >@report.taskType@<font class="COMPLUS">*</font>:</td>
				<td>
					<select id="cycleType" class="normalSel" style="width:304px;">
						<option value="1">@report.week7@</option>
						<option value="2">@report.month@</option>
					</select>
				</td>
			</tr>
			<%
			Integer weekStartDay = Integer.valueOf(request.getAttribute("weekStartDay").toString()); 
			%>
			<tr class="darkZebraTr" >
				<td class="rightBlueTxt" >@report.reportDesc@:</td>
				<td>
					<textarea maxlength=140 id="note" class=iptxa rows="4" style="width: 300px;" tooltip="@COMMON.reportDesc@"></textarea>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- 第三部分，按钮组合 -->
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="prevClick()" id="BTP" icon="miniIcoArrLeft">@COMMON.prev@</Zeta:Button>
		<Zeta:Button onClick="nextClick()" id="BTN" icon="miniIcoArrRight">@COMMON.next@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" id="BTC" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>
