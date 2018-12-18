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
#content-tit{height: 60px !important;margin-bottom: 15px;}
#content-tit span:FIRST-CHILD {position: fixed; top: 20px; left: 20px;}
#content-tit span img {position: absolute; top: 10px;right: 5px;}
.taskTable{ border-collapse: collapse;}
.titleTd{height: 40px;padding-top:2px;padding-right:5px;padding-bottom:2px;text-align:right;}
#selectDayTr th{height: 50px;}
#selectDayTr td{height: 40px;padding-top: 10px;vertical-align: middle !important;}
#selectDayTr td div{margin-bottom: 10px;vertical-align: middle !important;}
#selectDayTr td input[type="checkbox"]{vertical-align: middle !important;}
#selectDayTr td span{vertical-align: middle !important;margin-right: 15px;}
.normalInput{width: 300px; height:20px; border: 1px solid #7F9EB9; background: #FFF;}
.number_input{width:35px;height:19px;border:1px solid #7F9DB9}  
.number_input input{width:18px;height:18px;border:0;float:left;outline:none;text-align: center;}  
.number_input span{width:16px;height:18px;margin-top:2px;float:right}  
.number_input span sup,.number_input span sub{width:15px;height:7px;display:block;overflow:hidden;background:#ccc url(/images/report/arr_333.gif)}  
.number_input span sub{background-position:0 -7px;margin-top:1px}  
.number_input span sup.on{background-position:-16px 0}  
.number_input span sub.on{background-position:-16px -7px}
.unitSpan{ position:relative; top:4px; left:4px;float:left;right:4px;}
.COMPLUS {color:red;}
#executeWeekDay{float:left; margin-left:5px;width:36px;margin-right:5px;}
.WEEKDAY-SEPARTOR{width: 7px;}
</style>
<script>
var circleTypeCombo,
	LAST_CYCLE = 1;
function prevClick() {
	window.history.go(-1);
}
function nextClick() {
	var zetaCallback =  window.top.ZetaCallback;
	var taskName = $("#taskName").val();
	if(taskName){
		zetaCallback.taskName = taskName
	}else{
		return window.parent.showMessageDlg("@COMMON.tip@", "@report.reportNameNotNull@","error",function(){
			$("#taskName").focus();
		});
	}
	var cycleType = circleTypeCombo.getValue();
	zetaCallback.cycleType = cycleType;
	var conditionMap = {};
	switch(parseInt(cycleType)){
		case 1: //WEEK
			zetaCallback.excutorType = $("#excutorType").val();//统计本周，上周
			zetaCallback.excutorDay = $("#executeWeekDay").val();
			zetaCallback.excutorHour = $("INPUT#excuteHour_2").val();
			zetaCallback.excutorMin = $("INPUT#excuteMin_2").val();
			var checkedWeeks = $(".WEEKDAY-SELECT");
			/**如果没有选择周，强制要求选择至少一个 */
			var selecteds = checkedWeeks.filter(":checked"); 
			if(selecteds.length==0){
				return window.parent.showMessageDlg("@COMMON.tip@", "@report.atLeastOneWeek@","error");
			}
			if(zetaCallback.excutorType == 0){
				var compareDay1 = selecteds[selecteds.length-1].value;
					compareDay1 = compareDay1 == 1 ? 7 : compareDay1;
				var compareDay2 =  zetaCallback.excutorDay;
					compareDay2 = compareDay2 == 1 ? 7 : compareDay2;
				if(compareDay1 >= compareDay2){
					return window.parent.showMessageDlg("@COMMON.tip@", "@report.canotStatThisWeek@","error");
				}
			}
			
			var checkeds = new Array;
			for(var i=0 ; i <checkedWeeks.length;i++){
				checkeds.push(checkedWeeks[i].checked);
			}
			zetaCallback.weekDaySelected = checkeds;
			break;
		case 0:
			zetaCallback.excutorType = LAST_CYCLE;
			zetaCallback.excutorHour = $("INPUT#excuteHour_1").val();
			zetaCallback.excutorMin = $("INPUT#excuteMin_1").val();
			break;
		case 2:
			zetaCallback.excutorType = LAST_CYCLE;
			zetaCallback.excutorDay = $("INPUT#excuteDay_3").val();
			zetaCallback.excutorHour = $("INPUT#excuteHour_3").val();
			zetaCallback.excutorMin = $("INPUT#excuteMin_3").val();
			break;
	}
	zetaCallback.conditionMap = conditionMap;
	zetaCallback.note = $("#note").text(); 
	zetaCallback.email = $("#email").val();
	var templateName = zetaCallback.templateName;
	window.location.href = '/report/showTaskReportConfig.tv?reportCreatorBeanName=oltSniPortFlowReportCreator';
}
function cancelClick() {
	if (window.top.ZetaCallback) {
		window.top.ZetaCallback = null;
	}
	window.top.closeWindow("modalDlg");
}

/*  ComboBox改变时的事件  **/
function selectChaned(combo, record, index){
	if(index==0){
		$("#selectDayTr").css("display", "none");
		$("#dailyTr").css("display", "table-row");
		$("#weeklyTr").css("display", "none");
		$("#monthlyTr").css("display", "none");
	}else if(index==1){
		$("#selectDayTr").css("display", "table-row");
		$("#dailyTr").css("display", "none");
		$("#weeklyTr").css("display", "table-row");
		$("#monthlyTr").css("display", "none");
	}else{
		$("#selectDayTr").css("display", "none");
		$("#dailyTr").css("display", "none");
		$("#weeklyTr").css("display", "none");
		$("#monthlyTr").css("display", "table-row");
	}
}

Ext.onReady(function(){
	circleTypeCombo = new Ext.form.ComboBox({
		emptyText: '@COMMON.select@',
		mode: 'local',
		editable:false,
		triggerAction: 'all',
		transform: 'cycleType',
		width:304,
		listeners:{
			select: selectChaned
		}
	});
	
	var excuteHour_1 = new NumberInput("excuteHour_1", "excuteHour_1", 0, 0, 23);
	var excuteMin_1 = new NumberInput("excuteMin_1", "excuteMin_1", 0, 0, 59);
	
	var excuteHour_2 = new NumberInput("excuteHour_2", "excuteHour_2", 0, 0, 23);  
	var excuteMin_2 = new NumberInput("excuteMin_2", "excuteMin_2", 0, 0, 59);
	
	var excuteDay_3 = new NumberInput("excuteDay_3", "excuteDay_3", 1, 1, 31);  
	var excuteHour_3 = new NumberInput("excuteHour_3", "excuteHour_3", 0, 0, 23);  
	var excuteMin_3 = new NumberInput("excuteMin_3", "excuteMin_3", 0, 0, 59);
	
	var zetaCallback =  window.top.ZetaCallback;
	if(zetaCallback.cycleType){
		$("INPUT#taskName").val( zetaCallback.taskName );
		circleTypeCombo.setValue(zetaCallback.cycleType);
		selectChaned(null,null,zetaCallback.cycleType);
		if (zetaCallback.cycleType == 0) {
			$("INPUT#excuteHour_1").val(zetaCallback.excutorHour);
			$("INPUT#excuteMin_1").val(zetaCallback.excutorMin);
		} else if(zetaCallback.cycleType == 1) {
			$("#excutorType").val(zetaCallback.excutorType);
			$("#executeWeekDay").val(zetaCallback.excutorDay);
			$("INPUT#excuteHour_2").val(zetaCallback.excutorHour);
			$("INPUT#excuteMin_2").val(zetaCallback.excutorMin);
			var checkedWeeks = $(".WEEKDAY-SELECT");
			if (zetaCallback.weekDaySelected) {
				for (var i=0 ; i <checkedWeeks.length;i++) {
					if (zetaCallback.weekDaySelected[i])
						checkedWeeks[i].checked = true;
					else
						checkedWeeks[i].checked = false;
				}
			}
		} else if(zetaCallback.cycleType == 2){
			$("INPUT#excuteDay_3").val(zetaCallback.excutorDay);
			$("INPUT#excuteHour_3").val(zetaCallback.excutorHour);
			$("INPUT#excuteMin_3").val(zetaCallback.excutorMin);
		}
	}
});
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	
	<!-- 第一部分，说明文字加图标 -->
	<div class="openWinHeader">
		<div class="openWinTip">@report.reportTaskTip@</div>
		<div class="rightCirIco linkCirIco" ></div>
	</div>
	<!-- 第一部分，说明文字加图标 -->
	
	
	<!-- 第二部分，背景间隔色表格 -->
	<div class="edgeTB10LR20">
		<form id="folderForm" onsubmit="return false;">
			<table class="taskTable mCenter tdEdged4">
				<tr>
					<td class="titleTd" style="text-align:right;">@report.taskName@<font class="COMPLUS">*</font>:</td>
					<td>
						<input class="normalInput" id="taskName" type="text" style="width: 302px;"/>
					</td>
				</tr>
				<tr>
					<td class="titleTd" style="text-align:right;">@report.taskType@<font class="COMPLUS">*</font>:</td>
					<td>
						<select id="cycleType" >
							<option value="0" selected>@report.daily@</option>
							<option value="1">@report.week7@</option>
							<option value="2">@report.month@</option>
						</select>
					</td>
				</tr>
				<tr id="selectDayTr" style="display: none;">
					<td class="titleTd" style="text-align:right;">@report.weekStatstic@<font class="COMPLUS">*</font>:</td>
					<td>
						<div>
							<input class="WEEKDAY-SELECT" type="checkbox" value="2"/> @CALENDAR.Mon@<span class=WEEKDAY-SEPARTOR></span>
							<input class="WEEKDAY-SELECT" type="checkbox" value="3"/> @CALENDAR.Tue@<span class=WEEKDAY-SEPARTOR></span>
							<input class="WEEKDAY-SELECT" type="checkbox" value="4"/> @CALENDAR.Wed@<span class=WEEKDAY-SEPARTOR></span>
							<input class="WEEKDAY-SELECT" type="checkbox" value="5"/> @CALENDAR.Thu@<span class=WEEKDAY-SEPARTOR></span>
						</div>
						<div>
							<input class="WEEKDAY-SELECT" type="checkbox" value="6"/> @CALENDAR.Fri@<span class=WEEKDAY-SEPARTOR></span>
							<input class="WEEKDAY-SELECT" type="checkbox" value="7"/> @CALENDAR.Sat@<span class=WEEKDAY-SEPARTOR></span>
							<input class="WEEKDAY-SELECT" type="checkbox" value="1"/> @CALENDAR.Sun@<span class=WEEKDAY-SEPARTOR></span>
						</div>
					</td>
				</tr>
				<tr id="dailyTr">
					<td class="titleTd" style="text-align:right;">@report.executeTime@:</td>
					<td>
						<div class="excuteDiv" id="dailyExcuteDiv">
							<div id="excuteHour_1" style="float:left;"></div> 
							<span class="unitSpan">@CALENDAR.Hour@</span>
							<div id="excuteMin_1" style="float:left;margin-left: 10px;"></div> 
							<span class="unitSpan">@CALENDAR.Min@&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@report.statLastDay@</span>
						</div>
					</td>
				</tr>
				<tr id="weeklyTr" style="display: none;">
					<td class="titleTd" style="text-align:right;">@report.executeTime@:</td>
					<td>
						<div class="excuteDiv" id="weeklyExcuteDiv">
							<span class="unitSpan"></span>
							<select id="executeWeekDay" style="width:60px;">
								<option value="1" selected="selected">@CALENDAR.Sun@</option>
								<option value="2">@CALENDAR.Mon@</option>
								<option value="3">@CALENDAR.Tue@</option>
								<option value="4">@CALENDAR.Wed@</option>
								<option value="5">@CALENDAR.Thu@</option>
								<option value="6">@CALENDAR.Fri@</option>
								<option value="7">@CALENDAR.Sat@</option>
							</select>
							<div id="excuteHour_2" style="float:left;"></div>
							<span class="unitSpan">@CALENDAR.Hour@</span>
							<div id="excuteMin_2" style="float:left;margin-left: 10px;"></div> 
							<span class="unitSpan">@CALENDAR.Min@&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(@report.statstic@</span>
							<select id="excutorType" style="float:left; margin-left:10px;">
								<option value="0">@report.curWeek@</option>
								<option value="1" selected="selected">@report.lastWeek@</option>
							</select>
							<span class="unitSpan">@report.data@)</span>
						</div>
					</td>
				</tr>
				<tr id="monthlyTr" style="display: none;">
					<td class="titleTd" style="text-align:right;">@report.executeTime@:</td>
					<td>
						<div class="excuteDiv" id="monthlyExcuteDiv">
							<div id="excuteDay_3" style="float:left;"></div> 
							<span class="unitSpan">@CALENDAR.Day@</span>
							<div id="excuteHour_3" style="float:left; margin-left: 5px;"></div> 
							<span class="unitSpan">@CALENDAR.Hour@</span>
							<div id="excuteMin_3" style="float:left;margin-left: 10px;"></div> 
							<span class="unitSpan">@CALENDAR.Min@&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@report.statLastMonth@</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="titleTd" style="text-align:right;">@report.reportDesc@:</td>
					<td>
						<textarea id="note" class=iptxa rows="4" style="width: 300px;"></textarea>
					</td>
				</tr>
				<tr>
					<td class="titleTd" style="text-align:right;" disabled>@report.emailAddr@:</td>
					<td>
						<input class="normalInput" id="email" type="text" style="width: 302px;" disabled/>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 第二部分，背景间隔色表格 -->
	
	<!-- 第三部分，按钮组合 -->
	<div class="noWidthCenterOuter clearBoth">
		<ol class="upChannelListOl pB0 pT10 noWidthCenter">
			<li><a href="javascript:;" class="normalBtnBig" onclick="prevClick()"><span><i class="miniIcoSaveOK"></i>@report.prevStep@</span></a></li>
			<li><a href="javascript:;" class="normalBtnBig" onclick="nextClick()"><span>@report.nextStep@</span></a></li>
			<li><a href="javascript:;" class="normalBtnBig"	onclick="cancelClick()"><span>@report.cancle@</span></a></li>
		</ol>
	</div>
	<!-- 第三部分，按钮组合 -->
	
</body>
</Zeta:HTML>
