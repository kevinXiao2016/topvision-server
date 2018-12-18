/****************************************************************
				Date 类的拓展
 对Date的扩展，将 Date 转化为指定格式的String
 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
 例子：
 new Date().Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
 new Date().Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
*****************************************************/
Date.prototype.doFormat = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, //月份   
		"d+" : this.getDate(), //日   
		"h+" : this.getHours(), //小时   
		"m+" : this.getMinutes(), //分   
		"s+" : this.getSeconds(), //秒   
		"q+" : Math.floor((this.getMonth() + 3) / 3), //季度   
		"S" : this.getMilliseconds()
	//毫秒   
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

/****************************************************
			SimpleDateFormat 类
*****************************************************/
var SimpleDateFormat = function(fmt){
	this.formater = fmt;
}
SimpleDateFormat.prototype.format = function(date){
	if(date instanceof Date){
		return date.doFormat(this.formater);
	}else if(typeof date == 'number'){
		return new Date(date).doFormat(this.formater);
	}else if(!isNaN(date)){
		var v = parseInt(date,10);
		return new Date(v).doFormat(this.formater);
	}
}


/****************************************************
				Array 类，同ArrayList
*****************************************************/
/* Array extend */
Array.prototype.indexOf = function(o, from) {
	var len = this.length;
	from = from || 0;
	from += (from < 0) ? len : 0;
	for (; from < len; ++from){
	    if(this[from] == o){
	        return from;
	    }
	}
	return -1;
};
/**
 * 添加一个新对象，并保证该对象在数组中唯一
 * TODO 返回新加对象的下标
 */
Array.prototype.add = function(obj) {
	return this.append(obj, true);
};
/**
 * 删除指定对象以及该对象之后的所有对象
 */
Array.prototype.remove = function(obj) {
    var index = this.indexOf(obj);
    if(index != -1){
        this.splice(index, 1);
        return true;
    }
    return false;
};
Array.prototype.removeObj = function(obj){	
	var index=0; 	
	index=this.indexOf(obj);		
	
	if(index>=0 && index<this.length){ 
		for(var i=index;i<this.length-1;i++) 
			this[i]=this[i+1]; 
		this.length-=1; 
		return true; 
	} 
	else return false; 
}
/**
 * 判断当前数组中是否包含有指定对象。
 */
Array.prototype.contains = function(obj) {
    return (this.indexOf(obj) >= 0);
};
Array.prototype.append = function(obj, isChecking) {
    if (!(isChecking && this.contains(obj))) {
        this[this.length] = obj;
        return true;
    }
    return false;
};
//统计数组中重复的元素的个数,支持同时统计多个元素
Array.prototype.count = function(){
	var count=0,
		$array = [].slice.apply(arguments);
	for(var i=0;i<this.length;i++){
		var $value = this[i];
		if( $array.contains( $value ) ){
			count++;
		}
	}
	return count;
	
}


/****************************************************
				String 类
*****************************************************/
String.prototype.replaceAll = function(s1,s2) {
    return this.replace(new RegExp(s1,"gm"),s2);
}

/* String extend */
String.prototype.len = function() {
	return this.replace(/[^\x00-\xff]/g, "aa").length;
}
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.startWith = function(suffix){
	if (suffix == null || suffix == '') {
		return false;
	}
	return this.substring(0,suffix.length) == suffix;
}
String.prototype.endWith = function(suffix){
	if (suffix == null || suffix == '') {
		return false;
	}
	return this.substring(this.length - suffix.length) == suffix;
}
String.prototype.ellipse = function(maxLength){
    if(this.length > maxLength){
        return this.substring(0, maxLength-3) + '...';
    }
    return this.toString();
};
String.prototype.ellipsex = function(maxLength){
    if(this.len() > maxLength){
        return this.substring(0, maxLength-3) + '...';
    }
    return this.toString();
};
String.prototype.splice = function(start,howmany,item){
	var $list = [];
    $list.push(this.substring(start+howmany));
    $list.push(item);
    $list.push(this.substring(0,start));
    return $list.reverse().join("");
};

String.format = function(string,args){
	var result = string;
    if (arguments.length > 1) {
        if (arguments.length == 2 && typeof (args) == "object") {
            if(args instanceof Array){
            	for (var i = 0; i < args.length ; i++) {
                    if (args[i] != undefined) {
                        var reg = new RegExp("({[" + i + "]})", "g");
                        result = result.replace(reg, args[i]);
                    }
                }
            } else {
            	for (var key in args) {
	                if(args[key]!=undefined){
	                    var reg = new RegExp("({" + key + "})", "g");
	                    result = result.replace(reg, args[key]);
	                }
	            }
            }
        }
        else {
            for (var i = 0; i < arguments.length-1 ; i++) {
                if (arguments[i+1] != undefined) {
                    var reg = new RegExp("({[" + i + "]})", "g");
                    result = result.replace(reg, arguments[i+1]);
                }
            }
        }
    }
    return result
}

String.prototype.contains = function(substr){
	if(this.indexOf(substr) == -1){
		return false;
	}else{
		return true;
	}
}

// String format
String.prototype.format = function(args) {
    var result = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof (args) == "object") {
        	if(args instanceof Array){
            	for (var i = 0; i < args.length ; i++) {
                    if (args[i] != undefined) {
                        var reg = new RegExp("({[" + i + "]})", "g");
                        result = result.replace(reg, args[i]);
                    }
                }
            } else {
            	for (var key in args) {
	                if(args[key]!=undefined){
	                    var reg = new RegExp("({" + key + "})", "g");
	                    result = result.replace(reg, args[key]);
	                }
	            }
            }
        }
        else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    var reg = new RegExp("({[" + i + "]})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
}

