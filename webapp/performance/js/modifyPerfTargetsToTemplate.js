var page = {
	toolTip : ''
};
$(document).ready(function(){
	renderValue(parseInt(regValue));
	
	//初始化时间控件
	new NumberInput("startHour", "startHourDiv", 0, 0, 23);
	new NumberInput("endHour", "endHourDiv", 0, 0, 23);
	new NumberInput("startMin", "startMinDiv", 0, 0, 59);
	new NumberInput("endMin", "endMinDiv", 0, 0, 59);
	
	//获取当前处理的指标framemodifyTemplatePerfTargets
	currentRecord = parent.frames[originalFrame].frames["frameInner"].currentRecord;
	
	//填充指标
	$("#perfTargetSelect").text(currentRecord.perfTarget.name);
	perfTargetId = currentRecord.perfTarget.value;
	//进行赋值
	var values = currentRecord.thresholds;
	unit = values[0].unit || '';
	json = {
		unit : unit,
		config : values,
		clearRules : currentRecord.clearRules
	}
	//填充阈值
	jsonToPage(json);
	
	//填充触发条件
	$("#minuteLength").val(currentRecord.trigger.minute);
	$("#count").val(currentRecord.trigger.count);
	//填充时间策略
	if(currentRecord.timePeriod.used){
		$("#useTimePeriod").attr('checked', true);
		$('.timePeriod').show();
		$('#startHour').val(currentRecord.timePeriod.startHour);
		$('#startMin').val(currentRecord.timePeriod.startMin);
		$('#endHour').val(currentRecord.timePeriod.endHour);
		$('#endMin').val(currentRecord.timePeriod.endMin);
		
		$.each(currentRecord.timePeriod.weekdays, function(index, weekday){
			$(".weekCbx[value='"+ weekday +"']").attr('checked', true);
		});
	}else{
		$("#useTimePeriod").attr('checked', false);
		$('.timePeriod').hide();
	}
	
	//为帮助图片添加提示
	$('#tipsImg').tooltip();
	
	$("input[class='timeInput']").each(function(){
		var $input = $(this);
		$input.bind('keydown', function(event){
			var keyCode = event.which;
			if (keyCode == 46 || keyCode == 8 || keyCode == 37 || keyCode == 39 || (keyCode >= 48 && keyCode <= 57) || (keyCode >= 96 && keyCode <= 105) ){ 
				return true; 
			}else { 
				return false; 
			}
		})
	});
	
	//为启用时间段checkbox绑定事件
	$('#useTimePeriod').click(function(){
		var $cbx = $(this);
		if($cbx.is(':checked')){
			$('.timePeriod').show();
		}else{
			$('.timePeriod').hide();
		}
	});
	
});

function confirm(){
	//获取指标
	var perfTarget = {
		name: currentRecord.perfTarget.name,
		value: currentRecord.perfTarget.value
	};
	//获取触发条件
	var trigger = {
		minute: $('#minuteLength').val(),
		count: $('#count').val()
	};
	//获取每周选了哪几天
	var weekdays = new Array();
	$('.weekCbx:checked').each(function(){
		weekdays[weekdays.length] = $(this).val();
	});
	//获取时间段策略
	var timePeriod = {
			used:$('#useTimePeriod').is(':checked'),
			startHour:Number($('#startHour').val()),
			startMin: Number($('#startMin').val()),
			endHour:Number($('#endHour').val()),
			endMin:Number($('#endMin').val()),
			weekdays:weekdays
	}; 
	//获取阈值策略
	var thresholds = new Array();
	var clearRules = new Array();
	var validateFlag = false;
	var count = $("#putSubInput ul").length;
	$("#putSubInput ul").each(function(i){
		var $li = $(this).find("li");
		var inputValue = $li.eq(1).find(":input").val();
		var clearValue = $li.eq(6).find(":input").val();
		if(!validateThresholds(inputValue)){
			$li.eq(1).find(":input").focus();
			validateFlag = true;
			return false;
		}
		if(!validateThresholds(clearValue)){
			$li.eq(6).find(":input").focus();
			validateFlag = true;
			return false;
		}
		var rule = {
			action : parseInt($li.eq(0).find(":selected").val(),10),
			value  : $li.eq(1).find(":input").val(),
			level  : parseInt($li.eq(3).find(":selected").val(),10),
			priority : count--,
			unit : unit
		}
		var clearRule = {
			action : parseInt($li.eq(5).find(":selected").val(),10),
			value  : clearValue
		}
		thresholds.push(rule);
		clearRules.push(clearRule);
	});
	if(validateFlag){
		return false;
	}
	
	if(!validateAlert()){
		return false
	};
	parent.frames[originalFrame].frames["frameInner"].modifyCurrentRecord(perfTarget, trigger, timePeriod, thresholds,clearRules)
	closeClick();
}

//验证阈值
function validateThresholds(value){
	if(regRule.test(value) && value >= minValue && value <= maxValue ){
		return true;
	}
	return false;
}

//验证规则
function validateAlert(){
	//检验分钟
	//分钟必须是正整数
	var intReg = /^[1-9]\d*$/;
	if (!intReg.test($('#minuteLength').val())){
		$('#minuteLength').focus();
		return false;
	}
	if(Number($('#minuteLength').val())<1 || Number($('#minuteLength').val())>1440){
		$('#minuteLength').focus();
		return false;
	}
	//检验次数
	//次数必须是正整数
	if (!intReg.test($('#count').val())){
		$('#count').focus();
		return false;
	}
	if(Number($('#count').val())<1 || Number($('#count').val())>100){
		$('#count').focus();
		return false;
	}
	//检验时间策略
	if($('#useTimePeriod').is(':checked')){
		//结束时间必须大于开始时间
		var correct = true;
		if(Number($('#endHour').val())<Number($('#startHour').val())){
			window.parent.showMessageDlg('@COMMON.tip@', '@Performance.targetTip@')
			correct = false;
		}else if(Number($('#endHour').val())==Number($('#startHour').val())){
			if(Number($('#endMin').val())<=Number($('#startMin').val())){
				window.parent.showMessageDlg('@COMMON.tip@', '@Performance.targetTip@')
				correct = false;
			}
		}
		if(!correct){
			$("#timeLimitRule").show().effect("shake",{ times:1 }, 300);
			return false;
		}else{
			$("#timeLimitRule").hide();
		}
		//周几至少选一天
		if($('.weekCbx:checked').length==0){
			$("#weekdayRule").show().effect("shake",{ times:1 }, 300);
			window.parent.showMessageDlg('@COMMON.tip@', '@Performance.targetTip@')
			return false;
		}else{
			$("#weekdayRule").hide()
		}
	}
	return true;
}

function closeClick(){
	window.parent.closeWindow("modifyTemplatePerfTargets");
}

function resetDft(){
	//先显示隐藏的;
	$('.timePeriod').show();
	$('.triggerRule').show();
	
	$("#minuteLength, #count").val("1");
	$("#useTimePeriod, .weekCbx").attr("checked","checked");
	$("#startHour, #startMin").val("00");
	$("#endHour").val("23");
	$("#endMin").val("59");
}

//生成页面提示以及确定校验规则
function renderValue(regCase){
	var tipText;
	switch(regCase){
		case 0 : 
			maxValue = parseInt(maxValue);
			minValue = parseInt(minValue);
			regRule = /^-?\d+$/;
			tipText = String.format("<p>@Performance.maxValue@:<b class='orangeTxt'> {0}</b>, @Performance.minValue@:<b class='orangeTxt'> {1}</b>.</p><p>@Performance.noDecimal@</p>", maxValue, minValue);
			break;
		case 1 : 
			maxValue = parseFloat(maxValue);
			minValue = parseFloat(minValue);
			regRule = /^(-?\d+)(\.\d{0,1})?$/;
			tipText = String.format("<p>@Performance.maxValue@:<b class='orangeTxt'> {0}</b>, @Performance.minValue@:<b class='orangeTxt'> {1}</b>.</p><p>@Performance.oneDecimal@</p>", maxValue, minValue);
			break;
		case 2 : 
			maxValue = parseFloat(maxValue);
			minValue = parseFloat(minValue);
			regRule = /^(-?\d+)(\.\d{0,2})?$/;
			tipText = String.format("<p>@Performance.maxValue@:<b class='orangeTxt'> {0}</b>, @Performance.minValue@:<b class='orangeTxt'> {1}</b>.</p><p>@Performance.twoDecimal@</p>", maxValue, minValue);
			break;
	}
	$(".openWinTip").append(tipText);
	window.page.toolTip = tipText; 
}
