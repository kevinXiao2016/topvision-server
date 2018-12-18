<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    MODULE cmc
    css css/white/disabledStyle
</Zeta:Loader>

<style type="text/css">
.grid_div{margin: 5px 10px;}
.addServer_div{height: 100px;}
.addServer_div div{margin-top: 10px;margin-bottom:20px;}
fieldset {padding: 4px; margin: 5px 10px; border: 1px solid #7F9EB9;}
.enable_div{height: 50px;}
.button_div{text-align:right; margin-bottom: 5px;margin-right: 10px;}
.lable_span{margin-left: 20px;}
.txt-tips{color: #999999;margin-left: 135px;}
.params-tips{color: #999999;}
.normalInput{width: 300px; padding-left: 4px; }
.configInput{width: 80px;}
.syslog_table tr{
	height: 26px;
}
.labelTd{
	width: 110px;
	text-align:center;
}
.valueTd{
	width: 170px;
}
.errorValue{
	border-color: red;
}
</style>

<script type="text/javascript">
var entityId = ${entityId};
var cmcId = ${cmcId};
/**
 * 输入框ToolTip提示
 * id：el.id
 */
function _inputFocus(id, msg) {
	var obj=document.getElementById(id);
	var popObj=document.getElementById("tips");
	if(popObj!=null) {
		popObj.innerHTML="<div class=\"tipcon\">"+msg+"</div>";
		popObj.style.display="inline";
		//定位tooltips
		var left = getX(obj);
		//var top = getY(obj)-obj.offsetHeight-popObj.offsetHeight;
		var top = getY(obj)-obj.offsetHeight;
		popObj.style.left=left+"px";
		popObj.style.top=top+"px";
		/*10000 > Ext.window的zIndex > 9000 ，为了防止弹出弹力的tootlip丢失，故设置一个较高的zindex ,modify by @bravin */
		popObj.style.zIndex = 100000;
	}
}

function _inputBlur(obj) {
	if (typeof obj != 'object' && typeof obj == 'string') {
		obj = document.getElementById(obj);
	};
	clearOrSetTips(obj);
	document.getElementById("tips").style.display='none';
}

function _clearOrSetTips(obj, msg) {
	if(!obj) return;
	if(obj.value!="") {
		if(obj.value==msg)obj.value="";
	} else obj.value=msg ? msg : '';
}

Ext.onReady(function() {
	var h = document.documentElement.clientHeight-5;
	
	var tabs = new Ext.TabPanel({
		renderTo: tab_div,
		border: false,
       	height:h,
        items:[{
    		title:"@syslog.syslogServer@",
    		autoLoad:{url:"cmc/showSyslogServer.tv?entityId="+entityId,nocache: true,method:'GET',scripts:true}
    	},{
    		title:"@syslog.syslogConfig@",
    		autoLoad:{url:"cmc/showSyslogConfig.tv?entityId="+entityId + "&cmcId=" + cmcId,method:'GET',nocache: true,scripts:true}
    	}],
    	listeners:{
			tabchange:function(o,p){
    		    p.getUpdater().refresh();
			}
    	}
	});
	tabs.activate(0);
});
</script>

</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div id="tab_div" class="clear-x-panel-body"></div>
</body>
</Zeta:HTML>