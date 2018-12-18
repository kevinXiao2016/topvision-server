var MacUtil = {};

//匹配以冒号、横线和空格隔开的形式
MacUtil.reg1 = /^([A-Fa-f\d]{2}[\s:-]){5}[A-Fa-f\d]{2}$/;
//匹配0000.0000.0000形式
MacUtil.reg2 = /^([A-Fa-f\d]{4}\.){2}[A-Fa-f\d]{4}$/;
//匹配0000-0000-0000形式
MacUtil.reg5 = /^([A-Fa-f\d]{4}-){2}[A-Fa-f\d]{4}$/;
//匹配000000000000形式
MacUtil.reg3 = /^[A-Fa-f\d]{12}$/;
//MAC地址最小单位(两位)
MacUtil.reg4 = /([A-Fa-f\d]{2})/g;

/**
 * 验证address是否是合法的MAC地址
 */
MacUtil.isMac = function(address){
	if(!this.reg1.test(address) && !this.reg2.test(address) && !this.reg3.test(address) && !this.reg5.test(address)){ 
		return false; 
	} 
	return true;
}

/**
 * 验证MAC地址是否是多播地址，广播地址，全零地址
 */
MacUtil.isSpecialMac = function(address){
	var result = null;
	if(this.reg1.test(address)){
		result = address.split(/[\s:-]/g);// replace(,":").toUpperCase();
	}else if(this.reg2.test(address) || this.reg3.test(address) || this.reg5.test(address)){
		var array = address.match(this.reg4);
		result = array;
	}
	//全零地址
	if("00,00,00,00,00,00"==result.valueOf()){
		return true;
	}
	//多播地址//广播地址--全为1则包含在多播地址中
	var tempVal = parseInt("0x"+result[0]).toString(2);
	var tempStr = tempVal.substring(tempVal.length-1);
	if(1==tempStr){
		return true;
	}
	
	return false;
}

/**
 * 将合法的MAC地址格式转换成标准的以:分隔开的格式，字母为大写
 */
MacUtil.formatMac = function(address){
	if(this.reg1.test(address)){
		return address.replace(/[\s:-]/g,":").toUpperCase();
	}else if(this.reg2.test(address) || this.reg3.test(address) || this.reg5.test(address)){
		var array = address.match(this.reg4);
		var formatStr = array[0] + ":" + array[1] + ":" + array[2] + ":" + array[3] + ":" + array[4] + ":" + array[5];
		return formatStr.toUpperCase();
	}
}

MacUtil.formatQueryMac = function(mac) {
	if (!mac) {
        return mac;
    }
	// 将查询字符串中的所有间隔符转换成：
    mac = mac.replace(/[-\.\s]/g,":").toUpperCase();
    // 考虑到有三段式的情况，如果查询字符串中间有间隔符，需要判断各间隔符之间的位置,并转换为六段式的情况来进行匹配
    var splitIndexs = [];
    for (var i = 0; i < mac.length; i++) {
    	if(mac.charAt(i) == ":") {
    		splitIndexs.push(i);
    	}
    }
    if (splitIndexs.length == 1) {
    	// 如果只有一个间隔符，则判断其左右字符的长度，是否需要加上间隔符
    	if (mac.length - splitIndexs[0] > 3) {
            mac = mac.substring(0, splitIndexs[0] + 3) + ":" + mac.substring(splitIndexs[0] + 3, mac.length);
        }
    	if (splitIndexs[0] > 2) {
            mac = mac.substring(0, splitIndexs[0] - 2) + ":"  + mac.substring(splitIndexs[0] - 2, mac.length);
        }
    } else if (splitIndexs.length == 2) {
    	// 如果有两个间隔符，则判断其是否为三段式，以及是否需要在指定位置加上间隔符
    	if (splitIndexs[1] - splitIndexs[0] == 5) {
    		if (mac.length - splitIndexs[1] > 2) {
                mac = mac.substring(0, splitIndexs[1] + 3) + ":" + mac.substring(splitIndexs[1] + 3, mac.length);
            }
    		mac = mac.substring(0, splitIndexs[0] + 3) + ":" + mac.substring(splitIndexs[0] + 3, mac.length);
    		if (splitIndexs[0] > 2) {
                mac = mac.substring(0, splitIndexs[0] - 2) + ":" + mac.substring(splitIndexs[0] - 2, mac.length);
            }
    	}
    }
    
    // 如果间隔符大于2个，则肯定为六段式，不需做处理
    return mac;
}

/**
 * 验证输入的MAC地址是否为合法的模糊查询规则
 */
MacUtil.isFuzzyMacAddress = function(macAddress){
	//reg1 检查非法字符及文本总长度限制
    var reg1 = /^[A-Fa-f\d\.\-\:\s]{1,17}$/;
    if(!reg1.test(macAddress)) return false;
    //检查除分隔符外字符的长度
    var withoutSplitMac = macAddress.replace(/[\.\-\:\s]/g, "");
    if(withoutSplitMac.length>12) return false;
    //检查是否有连续的分隔符
    var reg2 = /[\.\-\:\s]{2,}/g;
    if(reg2.test(macAddress)) return false;
    //获取所有间隔符的位置
    var splitIndexs = [];
    macAddress = macAddress.replace(/[\.\-\:\s]/g, ":");
    for(var i=0; i<macAddress.length; i++){
    	if(macAddress.charAt(i)==":"){
    		splitIndexs[splitIndexs.length]=i;
    	}
    }
    if(splitIndexs.length>5){
    	return false;
    }else if(splitIndexs.length==1){
    	if(splitIndexs[0]>4 || (macAddress.length - splitIndexs[0]) > 5 ){
    		return false;
    	}
    }else if(splitIndexs.length==2){
    	//第一个间隔符
    	if(splitIndexs[0]>4 || (macAddress.length - splitIndexs[1]) > 5 || ((splitIndexs[1] - splitIndexs[0]) != 3 && (splitIndexs[1] - splitIndexs[0]) != 5)){
    		return false;
    	}
    }else if(3 <= splitIndexs.length && splitIndexs.length <= 5){
    	if(splitIndexs[0]>4){
    		return false;
    	}
    	if((macAddress.length - splitIndexs[splitIndexs.length-1]) > 5){
    		return false;
    	}
    	var interval = splitIndexs[1] - splitIndexs[0];
    	if(interval != 3 && interval !=5){
    		return false;
    	}
    	for(var i=2; i<splitIndexs.length; i++){
    		if((splitIndexs[i] - splitIndexs[i-1]) != (splitIndexs[i-1] - splitIndexs[i-2])){
    			return false;
    		}
    	}
    }
    return true;
}

/**
 *这是判断IPV6标准的JS正则表达式。如果你需要用JS判断IPV4标准的MAC地址正则表达式的话，可以略加修改，呵呵，我们现在正常也是用的IPV4标准，代码如下： 
 */
MacUtil.isIpv6Mac = function (address){
	//mac地址正则表达式 
	var reg_mac=/[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}/; 
	if(!reg_mac.test(address)){ 
		return false;
	}
	return true;  
}

MacUtil.convertMacToStorageStyle = function(mac){
	if (mac == null) {
        return mac;
    }
    //将输入mac地址中的间隔符及不合法字符均去掉,只保留0-9A-Fa-f
    return mac.replace(/[^\dA-Fa-f]/g, "").toUpperCase();
}

MacUtil.isMacMask = function(mac){
	if(!Validator.isMac(mac))	return;
	var maskMacStr = Validator.convertMacToDisplayStyle(mac, "1#W#U");
	var binaryMacMask = "", length = maskMacStr.length;
    for(var i=0; i<length; i++){
    	binaryMacMask += parseInt(maskMacStr.charAt(i), 16).toString(2);
    }
    //掩码的二进制必须是前面为全1，后面为全0,即不存在01的组合,并且不能为全0
    var errorSplit = /01/g;
    return binaryMacMask.indexOf('1')!==-1 && !errorSplit.test(binaryMacMask);
}

MacUtil.convertMacToDisplayStyle = function(mac, displayRule){
	if (mac == null) {
        return mac;
    }
    //验证displayRule是否符合规范，若不符合规范，直接返回原字符串
    var pattern = /^[136]#[MHDWK]#[UD]$/;
    if(!pattern.test(displayRule)){
    	return mac;
    }
    //先格式化mac成无间隔的存储格式
    var formatedMac = Validator.convertMacToStorageStyle(mac);
    if (formatedMac.length != 12) {
        //格式不正确,不做处理,直接返回原字符串
        return mac;
    } else {
    	var ruleParts = displayRule.split("#");
    	var section = parseInt(ruleParts[0]);
    	var delimiter = ruleParts[1];
    	switch (delimiter) {
        case "M":
            delimiter = ":";
            break;
        case "H":
            delimiter = "-";
            break;
        case "D":
            delimiter = ".";
            break;
        case "K":
            delimiter = " ";
            break;
        case "W":
            delimiter = "";
            break;
        }
        var letterCase = ruleParts[2];
        //格式化字母大小写
        if (letterCase == "U") {
            formatedMac = formatedMac.toUpperCase();
        } else if (letterCase == "D") {
            formatedMac = formatedMac.toLowerCase();
        }
        var finalMac = "";
        var sectionLength = 12 / section;
        for (var i = 0; i < section; i++) {
            finalMac += formatedMac.substring(sectionLength * i, sectionLength * (i + 1)) + delimiter;
        }
        //去掉最后一个间隔符
        if (delimiter != "") {
            finalMac = finalMac.substring(0, finalMac.length - 1);
        }
        return finalMac;
    }
}