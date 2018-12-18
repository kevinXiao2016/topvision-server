//告警ID及其name对应关系
var alarms = {
	"2": '@Performance.alert2@', 
	"3": '@Performance.alert3@', 
	"4": '@Performance.alert4@', 
	"5": '@Performance.alert5@', 
	"6": '@Performance.alert6@'
};

var actions = {
		"1": '@Performance.than@', 
		"2": '@Performance.thanOrEqual@', 
		"3": '@Performance.equal@', 
		"4": '@Performance.lessOrEqual@', 
		"5": '@Performance.less@'
	};

var days = {
	"1": '@tip.seven@',
	"2": '@tip.one@', 
	"3": '@tip.two@', 
	"4": '@tip.three@', 
	"5": '@tip.four@', 
	"6": '@tip.five@',
	"7": '@tip.six@'
};

var PerfRecord = Ext.data.Record.create([
    {name: 'perfTarget', type: 'string'},
    {name: 'trigger', type: 'string'},
    {name: 'timePeriod', type: 'string'},
    {name: 'thresholds', type: 'string'}
]);

function PerfTargetObject(perfTarget, trigger, timePeriod, thresholds,clearRules){
	this.perfTarget = perfTarget;
	this.trigger = trigger;
	this.timePeriod = timePeriod;
	this.thresholds = thresholds;
	this.clearRules = clearRules;
	this.toString = function(){
		//需要格式化为如下形式(指标名称/action_value_level_priority#action_value_level/minuteLength/number/isTimeLimit/startHour:startMin-endHour:endMin#12345)
		var str = this.perfTarget.value + "/";
		for(var i=0; i<this.thresholds.length;i++){
			str += this.thresholds[i].action + "_" + this.thresholds[i].value + "_" + this.thresholds[i].level + "_" + this.thresholds[i].priority + "#";
		}
		str = str.slice(0, str.length-1) + "/" + this.trigger.minute + "/" + this.trigger.count + "/";
		if(!this.timePeriod.used){
			str += "0";
		}else{
			str += "1/" + this.timePeriod.startHour+":"+this.timePeriod.startMin+"-"+this.timePeriod.endHour+":"+this.timePeriod.endMin +"#";
			for(var j=0; j<this.timePeriod.weekdays.length;j++){
				str += this.timePeriod.weekdays[j];
			}
		}
		var str = str + "/";
		for(var i=0; i<this.clearRules.length;i++){
			str += this.clearRules[i].action + "_" + this.clearRules[i].value + "#";
		}
		str = str.slice(0, str.length-1)
		return str;
	}
}

/**
 * 为PerfTargetObject类创建静态方法fromJson
 */
PerfTargetObject.fromJson = function(perfThresholdRule){
	//将json对象格式的perfThresholdRule解析成PerfTargetObject对象
	var targetId = perfThresholdRule.targetId;
	var used = Boolean(perfThresholdRule.isTimeLimit);
	var minute = Number(perfThresholdRule.minuteLength);
	var count = Number(perfThresholdRule.number);
	var thresholdArray = perfThresholdRule.thresholds.split("#");
	var clearRuleArray = perfThresholdRule.clearRules.split("#");
	var timeRange = perfThresholdRule.timeRange;
	var targetUnit = perfThresholdRule.targetUnit;
	//从thresholdArray中解析出阈值信息
	var thresholds = new Array();
	var clearRules = new Array();
	for(var i=0; i<thresholdArray.length; i++){
		var strs = thresholdArray[i].split("_");
		var rule = {
			action : strs[0],
			value  : strs[1],
			level  : strs[2],
			priority : strs[3],
			unit : targetUnit
		}
		thresholds.push(rule);		
	}
	for(var i=0; i<clearRuleArray.length; i++){
		var strs = clearRuleArray[i].split("_");
		var clearRule = {
			action : strs[0],
			value  : strs[1]
		}
		clearRules.push(clearRule);		
	}
	//从timeRange中解析出时间策略
	if(used){
		var hourAndMinutes = timeRange.split("#")[0];
		var days = timeRange.split("#")[1].toString();
		var startHour = hourAndMinutes.split("-")[0].split(":")[0];
		var startMin = hourAndMinutes.split("-")[0].split(":")[1];
		var endHour = hourAndMinutes.split("-")[1].split(":")[0];
		var endMin = hourAndMinutes.split("-")[1].split(":")[1];
		var weekdays = new Array();
		for(var j=0; j<days.length; j++){
			weekdays[weekdays.length] = days.charAt(j);
		}
	}
	//找出当前指标的中文名称
	var displayName = perfThresholdRule.displayName;
	//将解析出的数据封装为PerfTargetObject对象
	var perfTarget = {
		name: displayName,
		value: targetId
	};
	var trigger = {
		minute: minute,
		count: count
	};
	var timePeriod= {
		used:used,
		startHour:startHour,
		startMin: startMin,
		endHour:endHour,
		endMin:endMin,
		weekdays:weekdays
	}; 
	var perf = new PerfTargetObject(perfTarget, trigger, timePeriod, thresholds,clearRules);
	return perf;
}
