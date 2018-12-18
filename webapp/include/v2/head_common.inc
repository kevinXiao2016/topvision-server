<%-- head公用的部分 --%>
<%@ page language="java" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" CONTENT="no-cache" />
<meta http-equiv="Cache-Control" CONTENT="no-cache" />
<meta http-equiv="Expires" CONTENT="-1" />

<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>

<script type="text/javascript">
(function(){
    // private function
    var _listen_ = function (){
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
        window.addEventListener("onload",_listen_)
    }catch(e){
        window.attachEvent("onload",_listen_)
    }
    
    document.oncontextmenu = function(){
        return false;
    }
})()

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
                if (arguments[i] != undefined) {
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
</script>