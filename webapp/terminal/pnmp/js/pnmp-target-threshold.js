
function loadThresholdDataMtr() {
    loadThresholdData('mtr')
}

function loadThresholdDataPre() {
    loadThresholdData('premtter')
}

function loadThresholdDataNmter() {
    loadThresholdData('nmtter')
}

function loadThresholdDataPost() {
    loadThresholdData('postmtter')
}

function loadThresholdDataPPESR() {
    loadThresholdData('ppesr')
}
/**
 * 获取阈值数据
 */
function loadThresholdData(targetName) {
	$.get('/pnmp/target/loadPnmpTargetConfig.tv', function(targetList) {
		for(var i=0, len=targetList.length; i<len; i++) {
			var target = targetList[i];
            if (targetName != null && targetName != target.targetName ) {
                continue
            }
			// 拼接出对应阈值规则的上下限input框名称（规则为：targetName_thresholdName_low/high）
			var lowInputName = target.targetName + "_" + target.thresholdName + "_lowValue";
			var highInputName = target.targetName + "_" + target.thresholdName + "_highValue";
			$('#'+lowInputName).length > 0 && $('#'+lowInputName).val(target.lowValue);
			$('#'+highInputName).length > 0 && $('#'+highInputName).val(target.highValue);
		}
	});
}

function saveClick(targetName) {
	// 获取指标所有阈值
	var inputs = $('#' + targetName + '-table').find('input');
	
	var map = {};
	for(var i=0, len=inputs.length; i<len; i++) {
		var inputId = inputs[i].id;
		var thresholdName = inputId.split('_')[1];
		var type = inputId.split('_')[2];
		var value = inputs[i].value;
		//TODO 校验
		if(!validateInput(value)){
			$("#" + inputId).focus();
			return false;
		}
		var threshold = {};
		if(map[thresholdName]) {
			threshold = map[thresholdName];
			threshold[type] = value;
		} else {
			threshold.targetName = targetName;
			threshold.thresholdName = thresholdName;
			threshold[type] = value;
			map[thresholdName] = threshold;
		}
	}

	var array = [];
	var checkStatus = true;
	for(var thresholdName in map) {
		if(map[thresholdName].highValue!=null && map[thresholdName].lowValue != null){
			if(parseFloat(map[thresholdName].highValue) < parseFloat(map[thresholdName].lowValue)){
				checkStatus = false;
				var targetName = map[thresholdName].targetName;
				var thresholdName = map[thresholdName].thresholdName;
				$("#" + targetName + "_" + thresholdName + "_lowValue").focus();
				break ;
			}
		}
		array.push(map[thresholdName]);
	}
	if(checkStatus){
		save(array);
	}
}

function save(array){
	$.ajax({
		url : '/pnmp/target/savePnmpTargetConfig.tv',
		type : 'POST',
		data : {
			thresholds : JSON.stringify(array)
		},
		dataType : 'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@COMMON.saveSuccess@</b>'
   			});
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.saveFail@");
		},
		cache : false
	});
}


function validateInput(value){
	var regRule = /^(-?\d+)(\.\d{0,2})?$/;
	if(!regRule.test(value)){
		return false;
	}
	if(parseFloat(value) > 100 || parseFloat(value) < -100){
		return false;
	}
	return true;
}