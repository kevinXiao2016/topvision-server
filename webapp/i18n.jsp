<%@ page language="java" contentType="text/html;charset=utf-8"%>
var I18N = ${i18n}

/**
* key:国际化文件中的key
* params:String... 可选,替换占位符的值
*/
I18N.getMsg=function(key,params){
    try{
        var ret=eval("I18N."+key);
        if(params){
            var args=arguments;
            args[0]=ret;
            ret=String.format.apply(this,args); 
        }
        return ret;
    }catch(err){
        return key;
    }
}
// ------------------------------- Deprecated ---------------------------------
/**
* 直接通过properties文件中的key获取对应的值，
* 如果不存在或异常，返回key
*/
function getI18NString(key){
	try{
		var ret=eval("I18N."+key);
		return ret;
	}catch(err){
		return key;
	}
}

function getI18NModuleString(key) {
    var re = I18N.MODULE[key];
    if (re == null) {
        re = key;
    }
    return re;
}

function getI18NMenuItemString(key) {
    var re = I18N.MenuItem[key];
    if (re == null) {
        re = key;
    }
    return re;
}

function getI18NOperActionString(key) {
    var re = I18N.OperAction[key];
    if (re == null) {
        re = key;
    }
    return re;
}

function getI18NSystemLogString(key) {
    var re = I18N.SystemLog[key];
    if (re == null) {
        re = key;
    }
    return re;
}

function getI18NUserString(key) {
    var re = I18N.USER[key];
    if (re == null) {
        re = key;
    }
    return re;
}


