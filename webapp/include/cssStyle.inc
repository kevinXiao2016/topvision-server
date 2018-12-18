<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants" %>
<%@ page import="com.topvision.platform.service.ThemeManager" %>
<%@ page import="com.topvision.platform.domain.UserContext" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<meta   http-equiv="Expires"   CONTENT="0">
<meta   http-equiv="Cache-Control"   CONTENT="no-cache">
<meta   http-equiv="Pragma"   CONTENT="no-cache">  

<%
    response.addHeader("pragma", "no-cache");
    response.addHeader("cache-control", "no-cache");
    response.addDateHeader("expries", 0);
        
	SystemConstants sc = SystemConstants.getInstance();
	//String lang = "zh_CN";
	String lang = sc.getStringParam("language", "zh_CN");

	String cssStyleName = ThemeManager.getDefaultTheme();
	UserContext uc = null;
	if (session != null) {
		uc = (UserContext) session.getAttribute(UserContext.KEY);
		if (uc != null && uc.getStyleName() != null) {
            cssStyleName = uc.getStyleName();
		}
        if (uc != null && uc.getUser() != null && uc.getUser().getLanguage() != null) {
            lang = uc.getUser().getLanguage();
        }
    }
    //modified by bravin,for topvision tag usage
    pageContext.setAttribute("lang",lang);
	// 获取该设备的 版本
	String entityVersion = (String)session.getAttribute("com.topvision.entity.version");
	String emsVersion = (String)session.getAttribute("com.topvision.ems.version");
	
%>
<fmt:setLocale value="<%=lang%>"/>
<script type="text/javascript" src="/js/zetaframework/adapaters/ExtAdapter.js" defer></script>
<script type="text/javascript">
var lang = "<%=lang%>";
var emsVersion = "<%=emsVersion%>";
var entityVersion = "<%=entityVersion%>";
(function(){
	// private function
	var _listen_ = function (){
	     var doc = document;
	     try{
	       doc.body.focus();
	     }catch(e){}
		 if(typeof Ext != 'undefined' && Ext.menu && Ext.menu.Menu){
			//禁止菜单出现滚动条
			Ext.menu.Menu.prototype.enableScrolling = false
			//点击非叶子菜单时，菜单不消失
			Ext.menu.Menu.prototype.ignoreParentClicks = true
		 }
		 if(typeof Ext != 'undefined' && Ext.grid && Ext.grid.Column){
			//禁止表头列默认有menu的配置
			Ext.grid.Column.prototype.menuDisabled = true;
			Ext.grid.Column.prototype.align = "center";
		 }
		 if(typeof jQuery != 'undefined' || typeof $ != 'undefined') {
			$.ajaxSetup({
				cache:false,type:"POST",
				complete : function (XHR, TS) { XHR = null;}
			});
		 }
	}
	// listen
	try{
		window.addEventListener("onload",_listen_);
	}catch(e){
		window.attachEvent("onload",_listen_);
	}
})()



window.onerror = function(msg,url,line){
	//return true;
}
var Topvision = {version : 1.0, lang : '<%= lang %>'};
	Topvision.isDevelopment = <%=SystemConstants.isDevelopment%>;
var userName = '<%= uc == null ? "" : uc.getUser().getUserName() %>';
var cssStyleName = '<%= cssStyleName %>';
var chkSelModel = false;
var animCollapse = false;
var stripeRows = false;	
var useArrows = <%= "apple".equals(cssStyleName) || "vista".equals(cssStyleName) || "live".equals(cssStyleName) %>;
var trackMouseOver = <%= "vista".equals(cssStyleName) %>;
var enableMenuScrolling = false;
var displayInputTip = <%= uc == null ? true : uc.isDisplayInputTip() %>;


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

document.oncontextmenu = function(){
	if(Topvision.isDevelopment ){
		return true;
	}else{
		return false;
	}
}
</script>
