/**
 * 判断单个VLAN ID是否合法
 * @param vlanId
 */
function isSingleVlanIdValid(vlanId){
	var reg = /^[1-9][0-9]{0,3}$/;
	if(reg.exec(vlanId)){
		//转成数字判断是否在1-4094之间
		var num = parseInt(vlanId, 10);
		return 0 < num && num <= 4095;
	}else{
		return false;
	}
}

/**
 * 判断VLAN字符串格式是否合法
 * @param {String} text VLAN批量字符串
 * @return {Boolean} 是否合法
 */
function isVlanIdStrValid(text){
	var reg = /^([0-9]{1,4}[,-]{0,1})+$/;
	var numberReg = /^\d$/;
	if (reg.exec(text)) {
		// 拆分出各vlanId , -
		var splitIndex = [];
		var commaIndex = [];
		var crossIndex = [];
		var vlanIds = [];
		
		for(var i=0, len=text.length; i<len; i++) {
			if(text[i] === ',') {
				splitIndex.push(i);
				commaIndex.push(i);
			} else if(text[i] === '-') {
				splitIndex.push(i);
				crossIndex.push(i);
			}
		}
		
		// 正则表达式已经确保,-不会连续出现，且不会出现在第一位，只用判断是否出现在结尾即可
		
		if(commaIndex.length) {
			if(commaIndex[commaIndex.length-1] === text.length-1) {
				return false;
			}
		}
		
		if(crossIndex.length) {
			if(crossIndex[crossIndex.length-1] === text.length-1) {
				return false;
			}
		}
		
		// 确保两个-中间必须有,间隔
		if(crossIndex.length > 1) {
			var error = false;
			outerloop:
			for(var i=1, len=crossIndex.length; i<len; i++) {
				//遍历commaIndex，如果没找到在此两个-中间的，则出错
				var hasComma = false;
				innerloop:
				for(var j=0, jLen=commaIndex.length; j<jLen; j++) {
					if(crossIndex[i-1] < commaIndex[j] && commaIndex[j] < crossIndex[i]) {
						//有，间隔，不用继续比较
						hasComma = true;
						break innerloop;
					}
				}
				// 一直没找到
				if(!hasComma) {
					error = true;
					break outerloop;
				}
			}
			if(error) {
				return false;
			}
		}
		
		if(splitIndex.length) {
			vlanIds.push(text.substring(0, splitIndex[0]));			
			for(var i=1, len=splitIndex.length; i<len; i++) {
				vlanIds.push(text.substring(splitIndex[i-1]+1, splitIndex[i]));
			}
			vlanIds.push(text.substring(splitIndex[splitIndex.length-1]+1, text.length));
		}
		
		var tmp = text.replace(new RegExp('-', 'g'), ',');
		var tmpA = tmp.split(',');
		var tmpAl = tmpA.length;
		for ( var i = 0; i < tmpAl; i++) {
			if (parseFloat(tmpA[i]) > 4094 || tmpA[i] == 0) {
				return false;
			}
		}
		return true;
	}
	return false;
}

function inRange(value, min, max) {
	return min > value && value < max;
}

/**
 * 为指定dom元素添加非法样式
 * @param el
 */
function addInValidStyle(el){
	$(el).css("border", "1px solid #ff0000");
}

/**
 * 为指定dom元素添加合法样式
 * @param el
 */
function addValidStyle(el){
	$(el).css("border", "1px solid #8bb8f3");
}

/**
 * 将VLAN查询字符串转为数组形式
 * @param {String} text VLAN查询批量字符串
 * @return {Array} VLAN数组
 */
function convertVlanStrToArray (text) {
	if(!text) return [];
	var re = new Array();
	var t = text.split(",");
	var tl = t.length;
	for ( var i = 0; i < tl; i++) {
		var tt = t[i];
		var ttI = tt.indexOf("-");
		if (ttI > 0) {
			var ttt = tt.split("-");
			if (ttt.length == 2) {
				var low = parseFloat(parseFloat(ttt[0]) > parseFloat(ttt[1]) ? ttt[1]
						: ttt[0]);
				var tttl = Math.abs(parseFloat(ttt[0]) - parseFloat(ttt[1]));
				for ( var u = 0; u < tttl + 1; u++) {
					re.push(low + u);
				}
			}
		} else if (ttI == -1) {
			re.push(parseFloat(tt));
		}
	}
	var rel = re.length;
	if (rel > 1) {
		var o = {};
		for ( var k = 0; k < rel; k++) {
			o[re[k]] = true;
		}
		re = new Array();
		for ( var x in o) {
			if (x > 0 && o.hasOwnProperty(x)) {
				re.push(parseFloat(x));
			}
		}
		re.sort(function(a, b) {
			return a - b;
		});
	}
	return re;
}

/**
 * 将数组形式的VLAN ID转换成缩略的展示形式
 * @param vlanArray
 * @return {String} 缩略形式字符串
 */
function convertVlanArrayToAbbr(vlanArray){
	if(!vlanArray || vlanArray.length <=1 ){
		return vlanArray;
	}
	//先将数组进行排序
	vlanArray.sort(function(a, b){
		return a - b;
	});
	var parts = [];
	parts[0] = {};
	parts[0].start = vlanArray[0];
	parts[0].end = vlanArray[0];
	
	var curPartIndex=0;
	for(var i=1, len=vlanArray.length; i<len; i++){
		if(vlanArray[i] - vlanArray[i-1] == 1){
			//连续
			parts[curPartIndex].end = vlanArray[i];
		}else{
			//不连续
			++curPartIndex
			parts[curPartIndex] = {};
			parts[curPartIndex].start = vlanArray[i];
			parts[curPartIndex].end = vlanArray[i];
		}
	}
	var outArr = [];
	for(var i=0; i<=curPartIndex; i++){
		if(parts[i].start == parts[i].end){
			outArr.push(parts[i].start);
		}else{
			outArr.push(parts[i].start+'-'+parts[i].end);
		}
	}
	return outArr.join(',');
}

/**
 * 判断tagVlan和untagVlan是否有重叠冲突
 * @param tagVlanArray
 * @param untagVlanArray
 */
function vlanModeConflictCheck(tagVlan, untagVlan){
	//先将tagVlan和untagVlan都转成缩略形式
	var tagVlanArr = tagVlan.split(','),
		untagVlanArr = untagVlan.split(',');
	
	var conflict = false, conflictIds = [], curTagInterval, curUntagInterval;
	for(var i=0, tagLen=tagVlanArr.length; i<tagLen; i++){
		curTagInterval = convertPartToInterval(tagVlanArr[i]);
		for(j=0, untagLen=untagVlanArr.length; j<untagLen; j++){
			curUntagInterval = convertPartToInterval(untagVlanArr[j]);
			var overlapIds = isIntervalsOverlap(curTagInterval, curUntagInterval);
			if(overlapIds.length){
				conflict = true;
				//找出有哪些冲突
				$.each(overlapIds, function(i, id){
					conflictIds.push(id);
				})
				//break;
			}
			//如果当前tag区间在当前untag区间左边，则无需继续找untag区间判断
			if(curTagInterval[1] < curUntagInterval[0]){
				j = untagVlanArr.length;
			}
		}
		//此tag区间没有与任意untag重叠，判断是否需要拿下一个untag查找：如果此tag区间在所有untag区间的右边，则无需继续
		/*if(curTagInterval[0] > convertPartToInterval(untagVlanArr[untagVlanArr.length-1])[1]){
			break;
		}*/
	}
	return {
		conflict: conflict,
		conflictIds: conflictIds
	};
}

/**
 * 将每个连续段转成区间的形式：二维数组
 * @param partStr
 * @returns {Array}
 */
function convertPartToInterval(partStr){
	var arr = partStr.split('-');
	if(arr.length === 1){
		return [parseInt(arr[0], 10), parseInt(arr[0], 10)];
	}else{
		return [parseInt(arr[0], 10), parseInt(arr[1], 10)];
	}
}

/**
 * 比较两个连续区间有无重叠，返回重叠的id数组
 * @param fInterval 第一个区间 ([start, end])
 * @param sInterval 第二个区间 ([start, end])
 * @return {Array} 重叠的id数组
 */
function isIntervalsOverlap(fInterval, sInterval){
	var begin = Math.max(fInterval[0], sInterval[0]),
		end = Math.min(fInterval[1], sInterval[1]);
	
	if(begin > end){
		return [];
	}
	var ret = [];
	for(i = begin; i<= end; i++){
		ret.push(i);
	}
	return ret;
}

/**
 * 从端口index中解析出端口的槽位号slot和端口号port
 * @param portIndex
 * @return {Object} {slot:'', port: ''}
 */
function analysisPortIndex(portIndex){
	var index = portIndex.toString(16);
	//如果首位为0则补0。
	index = '000000'.concat(index);
	index = index.substring(index.length-10);
	var slot = parseInt(index.substring(0,2),16);
	//TODO port号可能需要从17开始算1
	var port = parseInt(index.substring(2,4),16);
	return {
		slot: slot,
		port: port
	};
}

/**
 * 将端口index转换成slot/port格式的字符串
 * @param portIndex
 * @return {String} slot/port格式的字符串
 */
function convertPortIndexToStr(portIndex){
	var portObj = analysisPortIndex(portIndex);
	return portObj.slot + '/' + portObj.port;
}

/**
 * 将端口index转换成 type slot/port格式的字符串
 * @param arr
 * @param disabled
 */
function convertPortIndexToStrWithType(portIndex) {
	var str = '';
	var portObj = analysisPortIndex(portIndex);
	var slotPreType = slotTypeMapping[portObj.slot];
	switch(slotType[slotPreType]) {
	case 'xguc':
		//1-2 XE
		//3-6 GE
		if(portObj.port < 3) {
			str = 'XE-' + portObj.slot + '/' + portObj.port;
		} else {
			str = 'GE-' + portObj.slot + '/' + (portObj.port - 2);
		}
		break;
	case 'meua':
	case 'mefa':
	case 'mefb':
	case 'mefc':
		if(portObj.port < 17) {
			str = 'PON-' + portObj.slot + '/' + portObj.port;
		} else if(portObj.port < 19) {
			str = 'XE-' + portObj.slot + '/' + (portObj.port - 16);
		} else {
			str = 'GE-' + portObj.slot + '/' + (portObj.port - 18);
		}
		break;
	case 'meub':
		if(portObj.port < 17) {
			str = 'PON-' + portObj.slot + '/' + portObj.port;
		} else {
			str = 'GE-' + portObj.slot + '/' + (portObj.port - 16);
		}
		break;
	case 'mgua':
	case 'mgub':
		if(portObj.port < 17) {
			str = 'PON-' + portObj.slot + '/' + portObj.port;
		} else {
			str = 'XE-' + portObj.slot + '/' + (portObj.port - 16);
		}
		break;
	default:
		str = portObj.slot + '/' + portObj.port;
	}
	
	return str;
}

/*
 * 设置Ext按钮的disabled属性
 * @param {array} 由id组成的数组
 * @param {boolean} disabled设置成true或false
 *  
 */
function disabledBtn(arr, disabled){
	$.each(arr, function(i, v){
		Ext.getCmp(v).setDisabled(disabled);
	});
};

