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
    import report.javascript.ReportTaskUtil
</Zeta:Loader>
<style type="text/css">
.unitSpan {position: relative;top: 4px;left: 4px;float: left;right: 4px;}
.COMPLUS {color: red;}
#executeWeekDay {float: left;margin-left: 5px;width: 36px;margin-right: 5px;}
.WEEKDAY-SEPARTOR {width: 7px;}
</style>
<script>
var reportTaskJson = ${reportTaskJson},
	weekStartDay = top.firstDayOfWeek,
	taskNamesJson = ${taskNamesJson},
	LAST_CYCLE = 1;
	
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
		reg = /^.{0,140}$/;
		var note = $("#note").val();
		if(!reg.test(note)){
			$("#note").focus();
			return false;
		}
		return true;
	}
	
	function okClick() {
		var taskId = reportTaskJson.taskId;
		//进行验证
		if(!validate()){return;}
		//进行任务名称重复性校验
		var taskName = $("#taskName").val();
		for(var i=0; i<taskNamesJson.length; i++){
			if(taskName == taskNamesJson[i] && taskName!= reportTaskJson.taskName){
				return window.parent.showMessageDlg("@COMMON.tip@", "@report.taskUsed@","error");
			}
		}
		//封装参数
		var note = $("#note").val();
		var email = '';
		//封装执行时间策略
		var cycleType = reportTaskJson.cycleType;
		var excutorType;
		var excutorDay;
		var excutorHour;
		var excutorMin;
		var weekDaySelected;
		switch(cycleType){
			case 1: //WEEK
				excutorType = 1;//统计上周
				excutorDay = parseInt(weekStartDay);
				excutorHour = 0;
				excutorMin = 0;
				break;
			case 2:
				excutorType = 1;
				excutorDay = 1;
				excutorHour = 0;
				excutorMin = 0;
				break;
		}
		
		//执行修改请求
		$.ajax({
			url: '/report/modifyReportTask.tv',
	    	type: 'POST',
	    	data: {
	    		taskId:taskId, 
	    		taskName:taskName, 
	    		note:note, 
	    		email:email 
	    	},
	   		success: function() {
	   			cancelClick();
	   		}, error: function() {
	   			//获取FTP服务器状态失败后，提示修改失败
	   			window.parent.showErrorDlg();
			}, cache: false,
			complete: function (XHR, TS) { XHR = null }
		});
	}
	
	function cancelClick() {
		window.top.getFrame('allreporttask').onRefreshClick();
		ReportTaskUtil.refreshTaskTree();
		window.top.closeWindow("modalDlg");
	}

	Ext.onReady(function() {
		//给各输入框赋值
		$("#taskName").val(reportTaskJson.taskName);
		$("#cycleType").val(reportTaskJson.cycleTypeString);
		$("#note").val(reportTaskJson.note);
	});
</script>
	</head>
	<body class="openWinBody">
		<div class=formtip id=tips style="display: none"></div>
		<input type=hidden name="reportTask.templateId"
			value="<%=request.getParameter("templateId")%>" />
		<!-- 第一部分，说明文字加图标 -->
		<div class="openWinHeader">
			<div class="openWinTip">@report.reportTaskModifyTip@</div>
			<div class="rightCirIco clock3CirIco"></div>
		</div>
		<!-- 第一部分，说明文字加图标 -->
		
		<div class="content edgeTB10LR20">
			<table class="zebraTableRows mCenter" width=100% cellspacing=0 cellpadding=0>
				<tr>
					<td class="rightBlueTxt" width="110">@report.taskName@<font class="COMPLUS">*</font>:
					</td>
					<td><input maxlength="63" class="normalInput" id="taskName" type="text" style="width: 302px;" tooltip="@COMMON.anotherName@"/></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.taskType@<font class="COMPLUS">*</font>:
					</td>
					<td><input class="normalInput normalInputDisabled" id="cycleType" type="text" style="width: 302px;" disabled="disabled" /></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.reportDesc@:</td>
					<td>
						<textarea maxlength="140" id="note" class=iptxa rows="4" style="width: 300px;" tooltip='@report.taskNoteRule@'></textarea>
					</td>
				</tr>
				<!-- <tr>
					<td class="rightBlueTxt" disabled="disabled">@report.emailAddr@:</td>
					<td>
						<input class="normalInput" id="email" type="text" style="width: 302px;" disabled />
					</td>
				</tr> -->
			</table>
		</div>
		
		<!-- 第三部分，按钮组合 -->
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="okClick()" id="BTN" icon="miniIcoData">@COMMON.save@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" id="BTC" icon="miniIcoForbid">@report.cancle@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>
