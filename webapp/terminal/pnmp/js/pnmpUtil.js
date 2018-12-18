var healthColor = '#d7feea',
	marginalColor = '#fff8e5',
    badColor = '#ffe3e5';

function renderValue(value, p, record) {
	if(value) {
		return Number(value).toFixed(3);
	} else {
		return '--';
	}
}

function renderSignalValue(value, p, record) {
	if(value) {
		return Number(value).toFixed(1);
	} else {
		return '--';
	}
}

function loadTargetThreshold() {
	$.get('/pnmp/target/loadPnmpTargetConfig.tv', function(targetList) {
		for(var i=0, len=targetList.length; i<len; i++) {
			var threshold = targetList[i];
			if(thresholdMap[threshold.targetName] == null) {
				thresholdMap[threshold.targetName] = {};
			}
			thresholdMap[threshold.targetName][threshold.thresholdName] = {
				lowValue: threshold.lowValue,
				highValue: threshold.highValue
			};
		}
	});
	$.get('/pnmp/cmtarget/loadCmSignalTargetThresholds.tv', function(targetList) {
		for(var i=0, len=targetList.length; i<len; i++) {
			var threshold = targetList[i];
			if(cmSignalThresholdMap[threshold.targetName] == null) {
				cmSignalThresholdMap[threshold.targetName] = {};
			}
			cmSignalThresholdMap[threshold.targetName][threshold.thresholdName] = {
				lowValue: threshold.lowValue,
				highValue: threshold.highValue
			};
		}
	});
}

var ruler = {
	bad  : 'badColor',
    marginal : 'marginalColor',
	good : 'healthColor',
    mtr  : function(value){ //mtr
        if(value > thresholdMap.mtr.health.lowValue){
            return ruler.good;
        }else if(value < thresholdMap.mtr.bad.highValue){
            return ruler.bad;
        }else{
            return ruler.marginal;
        }
    },
    mtc  : function(value){ //mtc
        if(value > thresholdMap.mtc.bad.lowValue){
            return ruler.bad;
        }else{
            return ruler.good;
        }
    },
    upSendPower : function(value){//上行发送电平
    	if(value < cmSignalThresholdMap.upSendPower.tooLow.highValue || value > cmSignalThresholdMap.upSendPower.tooHigh.lowValue){
    		return ruler.bad;
    	}else{
    		return ruler.good;
    	}
    },
    upSnr : function(value){//上行snr
    	if(value < cmSignalThresholdMap.upSnr.bad.highValue){
    		return ruler.bad;
    	}else{
    		return ruler.good;
    	}
    },
    downRePower : function(value){//下行接收电平
    	if(value < cmSignalThresholdMap.downRePower.tooLow.highValue || value > cmSignalThresholdMap.downRePower.tooHigh.lowValue){
    		return ruler.bad;
    	}else{
    		return ruler.good;
    	}
    },
    downSnr : function(value){//下行snr
    	if(value < cmSignalThresholdMap.downSnr.bad.highValue){
    		return ruler.bad;
    	}else{
    		return ruler.good;
    	}
    }
}
function renderUtil(value, pro){
	if(!value){
		return '--';
	}else{
		if(pro == 'mtc'){
			return String.format('<span class="{0}">{1}</span>', ruler[pro](value), Number(value).toFixed(3));
		}else{
			return String.format('<span class="{0}">{1}</span>', ruler[pro](value), Number(value).toFixed(1));
		}
	}
}

function formatTowUtil(value){
	return value.toFixed(2);
}

function formatThreeUtil(value){
	return value.toFixed(3);
}

/**
 * 取数组中元素的绝对值的最大值
 * @param arr
 * @param defaultValue 如果最大值小于给定的默认值，取默认值
 * @returns
 */
function getArrAbsMax(arr, defaultValue){
	if(!isEmptyArr(arr)){
		var max = Math.abs(arr[0]);
		var size = arr.length;
		for(var i=0; i<size; i++){
			var temp = Math.abs(arr[i]);
			if(max < temp){
				max = temp;
			}
		}
		if(max < defaultValue){
			return defaultValue;
		}else{
			return max;
		}
	}else{
		return defaultValue;
	}
}

function isEmptyArr(arr){
	return (Array.isArray(arr) && arr.length === 0) 
	|| (Object.prototype.isPrototypeOf(arr) && Object.keys(arr).length === 0);
}