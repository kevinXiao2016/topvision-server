$(document).ready(function(){
	//生成指标下拉框
	for(var key in perfTargets){
		$('#perfTargetSelect').append('<optgroup label="'+key+'" id="'+ key.replace(/\s/g, '') + '"></optgroup>');
		$.each(perfTargets[key], function(index, perfTarget){
			$('optgroup#'+key.replace(/\s/g, '')).append('<option value="'+perfTarget.targetId+'">'+perfTarget.targetDisplayName+'</option>');
		});
	}
	//获取指标列表中的指标
	var perfList = parent.frames[originalFrame].frames["frameInner"].perfTargetList;
	//去除已经存在在指标列表中的指标
	$.each(perfList, function(index, perf){
		$("#perfTargetSelect optgroup option[value='"+perf.perfTarget.value+"']").remove();
	});
	//样式调优
	$("#perfTargetSelect").select2();
	
	//为下拉菜单绑定事件
	$("#perfTargetSelect").change(function(){
		var targetId = $("#perfTargetSelect").val();
		//从指标列表中找出对应的指标信息
		$.each(perfThresholdTargets, function(index, perfThresholdTarget){
			if(perfThresholdTarget.targetId == targetId){
				maxValue = perfThresholdTarget.maxNum;
				minValue = perfThresholdTarget.minNum;
				regValue = perfThresholdTarget.regexpValue;
				unit = perfThresholdTarget.unit;
				$(".unitSpan").text(unit);
				json.unit = unit;
				//生成页面提示以及确定校验规则
				renderValue(parseInt(regValue));
				//修改输入提示
				correctToolTip();
			}
		})
	});
	$("#perfTargetSelect").trigger("change");

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
	
	//构建时间控件
	new NumberInput("startHour", "startHourDiv", 0, 0, 23);
	new NumberInput("endHour", "endHourDiv", 23, 0, 23);
	new NumberInput("startMin", "startMinDiv", 0, 0, 59);
	new NumberInput("endMin", "endMinDiv", 59, 0, 59);

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

function addPerfTarget(){
	//获取指标
	var perfTarget = {
		name: $("#perfTargetSelect").find("option:selected").text(),
		value: $('#perfTargetSelect').val()
	};
	//获取触发条件
	var trigger = {
		minute: Number($('#minuteLength').val()),
		count: Number($('#count').val())
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
	var validateFlag = false;
	var thresholds = new Array();
	var clearRules = new Array();
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
			priority : ++i,
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
	//验证规则
	if(!validateAlert()){
		return false
	};

	//添加指标	
	parent.frames[originalFrame].frames["frameInner"].addRecordToGrid(perfTarget, trigger, timePeriod, thresholds,clearRules);
	window.parent.closeWindow("addPerfTargetsToTemp");
}

function closeClick(){
	window.parent.closeWindow("addPerfTargetsToTemp");
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
			$("#timeLimitRule").show().effect("shake",{ times:2 }, 300);
			return false;
		}else{
			$("#timeLimitRule").hide();
		}
		//周几至少选一天
		if($('.weekCbx:checked').length==0){
			$("#weekdayRule").show().effect("shake",{ times:2 }, 300);
			window.parent.showMessageDlg('@COMMON.tip@', '@Performance.targetTip@')
			return false;
		}else{
			$("#weekdayRule").hide()
		}
	}
	return true;
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
	$(".openWinTip").html(tipText);
	page.toolTip = tipText;
}
