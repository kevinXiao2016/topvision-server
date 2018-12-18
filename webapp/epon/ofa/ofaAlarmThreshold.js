var alarmThresholdModule = {
	id: 'thresholdConfig',
	items: [{
		id: 'inputAlarmUp',
		showFormat: formatDivided10,//展示时格式化正确显示数值
		sendFormat: formatMultiply10,//发送到后台格式化为原先值
		validate: V.isInRange
	},{
		id: 'inputAlarmLow',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRangeWithoutHigh,
	},{
		id: 'outputAlarmUp',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRange
	},{
		id: 'outputAlarmLow',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRangeWithoutHigh
	},{
		id: 'pump1BiasAlarmUp',
		validate: V.isInRange
	},{
		id: 'pump1BiasAlarmLow',
		validate: V.isInRangeWithoutHigh
	},{
		id: 'pump1TempAlarmUp',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRange
	},{
		id: 'pump1TempAlarmLow',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRangeWithoutHigh
	},{
		id: 'pump1TecAlarmUp',
		validate: V.isInRange
	},{
		id: 'pump1TecAlarmLow',
		validate: V.isInRangeWithoutHigh
	},{
		id: 'pump2BiasAlarmUp',
		validate: V.isInRange
	},{
		id: 'pump2BiasAlarmLow',
		validate: V.isInRangeWithoutHigh
	},{
		id: 'pump2TempAlarmUp',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRange
	},{
		id: 'pump2TempAlarmLow',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRangeWithoutHigh
	},{
		id: 'voltage5AlarmUp',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRange
	},{
		id: 'voltage5AlarmLow',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRangeWithoutHigh
	},{
		id: 'voltage12AlarmUp',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRange
	},{
		id: 'voltage12AlarmLow',
		showFormat: formatDivided10,
		sendFormat: formatMultiply10,
		validate: V.isInRangeWithoutHigh
	}]
}

function formatDivided10(value){
	if(V.isInteger(value)){
		return (value/10).toFixed(1);
	}
} 

function retain1Decimal(value){
	if(V.isInteger(value)){
		return value.toFixed(1);
	}
}

function formatMultiply10(value){
	if(!isNaN(value)){
		return 10*value;
	}
}

function initRanges(){
	//每次初始化前先清空
	ofaAlarmThreshold = {};
	alarmThreshold = {};
	var items = alarmThresholdModule.items;
	ofaAlarmThreshold["ofaAlarmThreshold.entityId"] = entityId;
	$.each(items, function(index, item){
		var sendFormat = item.sendFormat;
		var val = $('#' + item.id).val() * 1;
		if(V.isTrue(val) || val == 0){
			//再赋新值
			if(sendFormat){
				alarmThreshold[item.id] = sendFormat(val);
				ofaAlarmThreshold["ofaAlarmThreshold." + item.id] = sendFormat(val);
			}else{
				alarmThreshold[item.id] = val;
				ofaAlarmThreshold["ofaAlarmThreshold." + item.id] = val;
			}
		}
	})
	ranges = {
		inputAlarmUp: {
			value: alarmThreshold.inputAlarmUp,
			range: [-130, 140]
		},
		inputAlarmLow: {
			value: alarmThreshold.inputAlarmLow,
			range: [-130, alarmThreshold.inputAlarmUp]
		},
		outputAlarmUp: {
			value: alarmThreshold.outputAlarmUp,
			range: [0, 250]
		},
		outputAlarmLow: {
			value: alarmThreshold.outputAlarmLow,
			range: [0, alarmThreshold.outputAlarmUp]
		},
		pump1BiasAlarmUp: {
			value: alarmThreshold.pump1BiasAlarmUp,
			range: [0, 1200]
		},
		pump1BiasAlarmLow: {
			value: alarmThreshold.pump1BiasAlarmLow,
			range: [0, alarmThreshold.pump1BiasAlarmUp]
		},
//		pump1TempAlarmUp: {
//			value: alarmThreshold.pump1TempAlarmUp,
//			range: [200, 300]
//		},
//		pump1TempAlarmLow: {
//			value: alarmThreshold.pump1TempAlarmLow,
//			range: [200, alarmThreshold.pump1TempAlarmUp]
//		},
		pump1TecAlarmUp: {
			value: alarmThreshold.pump1TecAlarmUp,
			range: [-2000, 2000]
		},
		pump1TecAlarmLow: {
			value: alarmThreshold.pump1TecAlarmLow,
			range: [-2000, alarmThreshold.pump1TecAlarmUp]
		},
		pump2BiasAlarmUp: {
			value: alarmThreshold.pump2BiasAlarmUp,
			range: [0, 12000]
		},
		pump2BiasAlarmLow: {
			value: alarmThreshold.pump2BiasAlarmLow,
			range: [0, alarmThreshold.pump2BiasAlarmUp]
		},
//		pump2TempAlarmUp: {
//			value: alarmThreshold.pump2TempAlarmUp,
//			range: [-450, 750]
//		},
//		pump2TempAlarmLow: {
//			value: alarmThreshold.pump2TempAlarmLow,
//			range: [-450, alarmThreshold.pump2TempAlarmUp]
//		},
		voltage5AlarmUp: {
			value: alarmThreshold.voltage5AlarmUp,
			range: [30, 70]
		},
		voltage5AlarmLow: {
			value: alarmThreshold.voltage5AlarmLow,
			range: [30, alarmThreshold.voltage5AlarmUp]
		},
		voltage12AlarmUp: {
			value: alarmThreshold.voltage12AlarmUp,
			range: [100, 140]
		},
		voltage12AlarmLow: {
			value: alarmThreshold.voltage12AlarmLow,
			range: [100, alarmThreshold.voltage12AlarmUp]
		}
	}
	if (tempUnit == '°F'){
		ranges.pump1TempAlarmUp = {
				value: alarmThreshold.pump1TempAlarmUp,
				range: [680, 860]
			},
		ranges.pump1TempAlarmLow = {
				value: alarmThreshold.pump1TempAlarmLow,
				range: [680, alarmThreshold.pump1TempAlarmUp]
			},
		ranges.pump2TempAlarmUp = {
				value: alarmThreshold.pump2TempAlarmUp,
				range: [-1130, 1670]
			},
		ranges.pump2TempAlarmLow = {
				value: alarmThreshold.pump2TempAlarmLow,
				range: [-1130, alarmThreshold.pump2TempAlarmUp]
			}
	}else{
		ranges.pump1TempAlarmUp = {
				value: alarmThreshold.pump1TempAlarmUp,
				range: [200, 300]
			},
		ranges.pump1TempAlarmLow = {
				value: alarmThreshold.pump1TempAlarmLow,
				range: [200, alarmThreshold.pump1TempAlarmUp]
			},
		ranges.pump2TempAlarmUp = {
				value: alarmThreshold.pump2TempAlarmUp,
				range: [-450, 750]
			},
		ranges.pump2TempAlarmLow = {
				value: alarmThreshold.pump2TempAlarmLow,
				range: [-450, alarmThreshold.pump2TempAlarmUp]
			}
	}
}

//初始OFA告警阈值默认值
function initAlarmThreshold(ofaAlarmThresholdJson){
	if(ofaAlarmThresholdJson){
		var json = JSON.parse(ofaAlarmThresholdJson);
		var items = alarmThresholdModule.items;
		$.each(items, function(index, item){
			var showFormat = item.showFormat;
			if(showFormat){
				$('#' + item.id).val(showFormat(json[item.id]));
			}else{
				$('#' + item.id).val(json[item.id]);
			}
		});
		if(checkParamValid(false)){
			//保留旧值，用于判断是否修改
			oldOfaAlarmThreshold = ofaAlarmThreshold;
		}
	}
}

//从设备获取告警阈值配置
function refreshConfig(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
        url:"/epon/ofa/refreshOfaAlarmThreshold.tv",
        type:"post",
        data:{
        	"entityId":entityId
        },
        success:function (json){
        	if(json.msg == "success"){
        		top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                    html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
                });
            	initAlarmThreshold(JSON.stringify(json.data));
        	} else {
        		window.top.showMessageDlg("@COMMON.tip@","@COMMON.fetchBad@");
        	}
        },
        error: function(message) {
        	window.top.showMessageDlg("@COMMON.tip@","@COMMON.fetchBad@");
        }, 
        cache: false     
    });
}

/**
 * 校验参数
 * @param flag 是否需要focus
 * @returns {Boolean}
 */
function checkParamValid(flag){
	initRanges();
	var items = alarmThresholdModule.items;
	var paramIsGood = true;
	$.each(items, function(index, item){
		var validate = item.validate;
		if(validate){
			var data = ranges[item.id];
			paramIsGood = validate(data.value, data.range);
			if(!paramIsGood){
				if(flag){
					$('#' + item.id).focus();
				}
				return false;
			}
		}
	});
	return paramIsGood;
}

/**
 * 判断参数是否有更改
 * @returns {Boolean}
 */
function checkParamChange(){
	return V.isObjectValueEqual(oldOfaAlarmThreshold,ofaAlarmThreshold);
}

//修改OFA告警阈值配置
function updateConfig(){
	if(checkParamValid(true)){
		if(!checkParamChange()){
			window.top.showWaitingDlg("@COMMON.wait@", "@OFA.update@", 'ext-mb-waiting');
			$.ajax({
				url: '/epon/ofa/modifyOfaAlarmThreshold.tv',
			    type: 'post',
			    data: ofaAlarmThreshold,
			    success: function(json){
			    	if(json.msg == "success"){
			    		top.afterSaveOrDelete({
					           title: '@COMMON.tip@',
					           html: '@OFA.updateThresholdConfigSuccess@'
					    });
			    		initAlarmThreshold(JSON.stringify(json.data));
			    	} else {
		        		window.top.showMessageDlg("@COMMON.tip@","@OFA.updateThresholdConfigFail@");
		        	}
			    },
				error: function(response){
					window.top.showMessageDlg("@COMMON.tip@","@OFA.updateThresholdConfigFail@");
				}, 
				cache: false
			});	
		}else{
			window.top.showMessageDlg("@COMMON.tip@", "@OFA.paramsNoChange@")
		}
	}
}

//{
//	"ofaAlarmThreshold.inputAlarmUp" : alarmThreshold.inputAlarmUp,
//	"ofaAlarmThreshold.inputAlarmLow" : alarmThreshold.inputAlarmLow,
//	"ofaAlarmThreshold.outputAlarmUp" : alarmThreshold.outputAlarmUp,
//	"ofaAlarmThreshold.outputAlarmLow" : alarmThreshold.outputAlarmLow,
//	"ofaAlarmThreshold.pump1BiasAlarmUp" : alarmThreshold.pump1BiasAlarmUp,
//	"ofaAlarmThreshold.pump1BiasAlarmLow" : alarmThreshold.pump1BiasAlarmLow,
//	"ofaAlarmThreshold.pump1TempAlarmUp" : alarmThreshold.pump1TempAlarmUp,
//	"ofaAlarmThreshold.pump1TempAlarmLow" : alarmThreshold.pump1TempAlarmLow,
//	"ofaAlarmThreshold.pump1TecAlarmUp" : alarmThreshold.pump1TecAlarmUp,
//	"ofaAlarmThreshold.pump1TecAlarmLow" : alarmThreshold.pump1TecAlarmLow,
//	"ofaAlarmThreshold.pump2BiasAlarmUp" : alarmThreshold.pump2BiasAlarmUp,
//	"ofaAlarmThreshold.pump2BiasAlarmLow" : alarmThreshold.pump2BiasAlarmLow,
//	"ofaAlarmThreshold.pump2TempAlarmUp" : alarmThreshold.pump2TempAlarmUp,
//	"ofaAlarmThreshold.pump2TempAlarmLow" : alarmThreshold.pump2TempAlarmLow,
//	"ofaAlarmThreshold.voltage5AlarmUp" : alarmThreshold.voltage5AlarmUp,
//	"ofaAlarmThreshold.voltage5AlarmLow" : alarmThreshold.voltage5AlarmLow,
//	"ofaAlarmThreshold.voltage12AlarmUp" : alarmThreshold.voltage12AlarmUp,
//	"ofaAlarmThreshold.voltage12AlarmLow" : alarmThreshold.voltage12AlarmLow
//}