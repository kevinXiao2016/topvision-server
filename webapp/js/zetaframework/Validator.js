var Validator = {
	ALIAS : /^[-_a-zA-Z0-9\u4e00-\u9fa5]+$/,
	NAME  : /^[-_a-zA-Z0-9]+$/,
	DL_ALIAS : /^[:\w\d\u4e00-\u9fa5\[\]\(\)-\/]*$/,
	//告警声音文件名字;
	ALERT_SOUND_NAME : /^[a-zA-Z0-9]{1,63}$/, 
	//网管别名;
	ANOTHER_NAME : /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:]{1,63}$/,
	//验证部分备注信息，例如告警确认、告警清除需要输入的信息;
	REMARKS : /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:]{1,255}$/,
	//网管位置;
	NM_LOCATION : /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:\s\@]{1,127}$/,
	//用途分类;
	CLASSIFY: /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:\s\@]{1,10}$/,
	//网管联系人;
	NM_CONTACT : /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:\s\@]{1,63}$/,
	//网管备注;
	NM_NOTE : /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:\s\@]{1,127}$/,
	//报表任务名;
    REPORT_NAME : /^[a-zA-Z\d\u4e00-\u9fa5-\（\）_\[\]()\/\.:]{1,63}$/,
	//上联口别名;
	PORT_NAME : /^[a-zA-Z\d\-_\[\]()\/\.:]{1,63}$/,
	//读写共同体;
	SNMP_COMMUNITY : /^[a-zA-Z\d\~\!\@\#\$\%\^\&\*\+\;\,\?\=\|\<\>\`\{\}\-_\'\"\[\]()\/\.:\\]{1,31}$/,
	//系统超时时间 0 或者大于等于30小于等于300的正整数;
	TIMEOUT : /^-1$|^300$|^[3-9]\d$|^[1-2]\d{2}$/
};
var V = Validator;
//匹配以冒号、横线和空格隔开的形式
Validator.reg1 = /^([A-Fa-f\d]{2}[\s:-]){5}[A-Fa-f\d]{2}$/;
//匹配0000.0000.0000形式
Validator.reg2 = /^([A-Fa-f\d]{4}\.){2}[A-Fa-f\d]{4}$/;
//匹配0000-0000-0000形式
Validator.reg5 = /^([A-Fa-f\d]{4}-){2}[A-Fa-f\d]{4}$/;
//匹配000000000000形式
Validator.reg3 = /^[A-Fa-f\d]{12}$/;
//MAC地址最小单位(两位)
Validator.reg4 = /([A-Fa-f\d]{2})/g;

/**
 * 验证address是否是合法的MAC地址
 */
Validator.isMac = function(address){
	if(!this.reg1.test(address) && !this.reg2.test(address) && !this.reg3.test(address) && !this.reg5.test(address)){ 
		return false; 
	} 
	return true;
}
/**
 * 验证MAC地址是否是多播地址，广播地址，全零地址
 */
Validator.isSpecialMac = function(address){
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
Validator.formatMac = function(address){
	if(this.reg1.test(address)){
		return address.replace(/[\s:-]/g,":").toUpperCase();
	}else if(this.reg2.test(address) || this.reg3.test(address) || this.reg5.test(address)){
		var array = address.match(this.reg4);
		var formatStr = array[0] + ":" + array[1] + ":" + array[2] + ":" + array[3] + ":" + array[4] + ":" + array[5];
		return formatStr.toUpperCase();
	}
}

/**
 * 验证输入的IP地址是否为合法的模糊查询规则
 */
Validator.isFuzzyIpAddress = function(ip){
	//模糊查询的IP支持所有的合法的IP的从第一位开始截取的任意部分
	var reg1 = /^((\d{1,2})|(1\d{2})|(2[0-4]\d)|(25[0-5]))$/; //纯粹的数字(0-255)
	var reg2 = /^(((\d{1,2})|(1\d{2})|(2[0-4]\d)|(25[0-5]))\.){1,3}$/; //以点结尾
	var reg3 = /^(((\d{1,2})|(1\d{2})|(2[0-4]\d)|(25[0-5]))\.){1,3}((\d{1,2})|(1\d{2})|(2[0-4]\d)|(25[0-5]))$/;  //以数字结尾，中间包含点
	if(!reg1.test(ip) && !reg2.test(ip) && !reg3.test(ip)){
		return false;
	}
	return true;
}

/**
 * 验证输入的MAC地址是否为合法的模糊查询规则
 */
Validator.isFuzzyMacAddress = function(macAddress){
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
Validator.isIpv6Mac = function (address){
	//mac地址正则表达式 
	var reg_mac=/[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}/; 
	if(!reg_mac.test(address)){ 
		return false;
	}
	return true;  
}

Validator.convertMacToStorageStyle = function(mac){
	if (mac == null) {
        return mac;
    }
    //将输入mac地址中的间隔符及不合法字符均去掉,只保留0-9A-Fa-f
    return mac.replace(/[^\dA-Fa-f]/g, "").toUpperCase();
}

Validator.isMacMask = function(mac){
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

Validator.convertMacToDisplayStyle = function(mac, displayRule){
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

/**
 * IP地址的判断方法
 */
Validator.isIpAddress = function(value) {
	/*var re = new RegExp("^" + xregexp.ipAddress(flags) + "$", "i");
	return re.test(value); // Boolean
*/};

/**
 * URL的判断方法
 */
Validator.isUrl = function(value, flags) {
//	var re = new RegExp("^" + xregexp.url(flags) + "$", "i");
//	return re.test(value); // Boolean
};

/**
 * EMAIL的判断方法
 */
Validator.isEmailAddress = function(value, flags) {
//	var re = new RegExp("^" + xregexp.emailAddress(flags) + "$", "i");
//	return re.test(value); // Boolean
};

/**
 * 判断是否是网管侧别名
 */
Validator.isAlias = function( alias , range, isDownlink ){
	if(isDownlink){
		return this.isDownlinkAlias(alias, range);
	}
	return this._aliasCheck(alias, range, this.ALIAS);
}

/**
 * 判断是否是网管侧下级设备别名。下级设备别名需要支持 : /等符号
 */
Validator.isDownlinkAlias = function( alias , range ){
	return this._aliasCheck(alias, range, this.DL_ALIAS);
}

Validator._aliasCheck = function(alias,range,reg){
	if( reg.test( alias ) ){
		if( Ext.isArray(range) && range.length == 2){
			if(alias.length <= range[1]  && alias.length>=range[0] ){
				return true;
			}else{
				return false;
			}
		}
		return true;
	}else{
		return false;
	}
}

/**
 * 判断是否是设备侧别名
 */
Validator.isName = function( name ){
	if( this.NAME.test( name ) ){
		return true;
	}else{
		return false;
	}
}

/**
 * 是否是个整数
 */
Validator.isInteger = function( value ){
	 return !isNaN(value) && value % 1 === 0;
}

/**
 * 是否是个百进制数
 */
Validator.isPercentage = function(){
	
} 

/**
 * 判断某个数是否属于某范围中（包括下限值，包括上限值）。预期可以支持整数，小数，mac地址，ip地址
 * value : 范围
 * ranges : Array,可由若干范围段组成 
 */
Validator.isInRange = function(value, range){
	if(value === "" || value === null)return false;
	if(!V.isInteger(value)){
		return false;
	}
	if( Ext.isArray(range) && range.length == 2){
		if(value <= range[1] && value >= range[0]){
			return true;
		}else{
			return false;
		}
	}else{
		return false;
	}
}

/**
 * 判断某个数是否属于某范围中（包括下限值，不包括上限值）。预期可以支持整数，小数，mac地址，ip地址
 * value : 范围
 * ranges : Array,可由若干范围段组成 
 */
Validator.isInRangeWithoutHigh = function(value, range){
	if(value === "" || value === null)return false;
	if(!V.isInteger(value)){
		return false;
	}
	if( Ext.isArray(range) && range.length == 2){
		if(value < range[1] && value >= range[0]){
			return true;
		}else{
			return false;
		}
	}else{
		return false;
	}
}

/**
 * 验证备注信息
 * 1-255个字符，支持中文、英文、数字、 - _ <b>.</b> <b>:</b> [ ] ( ) /
 */
Validator.isRemarks = function(testStr){
	if( this.REMARKS.test(testStr) ){
		return true;
	}else{
		return false;
	}
};//end function;
/**
 * 验证网管别名
 * 1-63个字符，支持中文、英文、数字、 - _ <b>.</b> <b>:</b> [ ] ( ) /
 * 并且不能是字符串null
 */
Validator.isAnotherName = function(testStr){
	if(testStr == 'null'){ return false;}
	if( this.ANOTHER_NAME.test(testStr) ){
		return true;
	}else{
		return false;
	}
};//end function;

/**
 * 验证网管地址
 * 1-127个字符，支持中文、英文、数字、 - _ <b>.</b> <b>:</b> [ ] ( ) /
 */
Validator.isLocationName = function(testStr){
	if( this.NM_LOCATION.test(testStr) ){
		return true;
	}else{
		return false;
	}
}

/**
 * 验证用途分类
 * 1-10个字符，支持中文、英文、数字、 - _ <b>.</b> <b>:</b> [ ] ( ) /
 */
Validator.isCmClassify = function(testStr){
	if( this.CLASSIFY.test(testStr) ){
		return true;
	}else{
		return false;
	}
}

/**
 * 验证网管联系人
 * 1-63个字符，支持中文、英文、数字、 - _ <b>.</b> <b>:</b> [ ] ( ) /
 */
Validator.isContactName = function(testStr){
	if( this.NM_CONTACT.test(testStr) ){
		return true;
	}else{
		return false;
	}
}
/**
 * 验证网管备注
 * 1-127个字符，支持中文、英文、数字、 - _ <b>.</b> <b>:</b> [ ] ( ) /
 */
Validator.isNoteName = function(testStr){
	if( this.NM_NOTE.test(testStr) ){
		return true;
	}else{
		return false;
	}
}
/**
 * 验证告警声音的名字
 * 1-63个字符，支持英文和数字
 */
Validator.isAlertSoundName = function(testStr){
	if( this.ALERT_SOUND_NAME.test(testStr) ){
		return true;
	}else{
		return false;
	}
}
Validator.isReportName = function(testStr){
    if( this.REPORT_NAME.test(testStr) ){
        return true;
    }else{
        return false;
    }
};//end function;
/*
 * 验证上联口名称的修改
 * 1-63个字符，支持英文、数字、 - _ <b>.</b> <b>:</b> [ ] ( ) /
 */
Validator.isPortName = function(testStr){
	if( this.PORT_NAME.test(testStr) ){
		return true;
	}else{
		return false;
	}
};//end function;
/*
 * 判断是否是只读共同体名或者是可写共同体名;
 *  
 */
Validator.isSnmpCommunity = function(testStr){
	if( this.SNMP_COMMUNITY.test(testStr) ){
		return true;
	}else{
		return false;
	}
};//end function;

/*
 * 验证系统超时时间
 * 0或者大于等于30小于等于300
 */
Validator.isSystemTimeout = function(testStr){
	if( this.TIMEOUT.test(testStr) ){
		return true;
	}else{
		return false;
	}
}

Validator.isTrue = function( v ){
	if( v == null || v == "" || v == 0 || v == false ){
		return false;
	}else{
		return true;
	}
}
Validator.isNotNull = function(){
	if(testStr == 'null'){ 
		return false;
	}else{
		return true;
	}
}

/**
 * 判断两个js对象内容是否相等
 */
Validator.isObjectValueEqual = function(a, b){
	var aProps = Object.getOwnPropertyNames(a);
    var bProps = Object.getOwnPropertyNames(b);

    if (aProps.length != bProps.length) {
        return false;
    }

    for (var i = 0; i < aProps.length; i++) {
        var propName = aProps[i];
        if (a[propName] !== b[propName]) {
            return false;
        }
    }
    return true;
}
/**
 * 判断是否在数组中
 */
Validator.isInArray = function(value, arr){
	if(value === "" || value === null){
		return false;
	}
	if(!Array.isArray(arr)){
		return false;
	}
	for(var i=0; i<arr.length; i++){
		if(arr[i] == value){
			return true;
		}
	}
	return false;
}