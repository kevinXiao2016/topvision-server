<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
    IMPORT js/jquery/Nm3kTabBtn
    import js/customColumnModel
    IMPORT epon/portinfo/oltPortInfo
</Zeta:Loader>
<style type="text/css">
body,html{ height:100%; overflow:hidden;}
.x-panel-tbar .x-toolbar { border:none; border-top:1px solid #d0d0d0;}
#tip-div{ position: absolute; top:47px; right:10px;}
.tip-dl{background: none repeat scroll 0 0 #F7F7F7;border: 1px solid #C2C2C2;color: #333333;padding: 2px 10px;position: absolute;z-index:2;}
.tip-dl dd {float: left;line-height: 1.5em;}
.thetips dd{ float:left;}
.thetips dl{border:1px solid #ccc; float:left; background:#E1E1E1; }
#color-tips {left:5px;top:67px;}
#operation-tips{left:311px;top:67px;}
#suc-num-dd{color: #26B064;}
#fail-num-dd{color: #C07877;}
.yellow-div{height:16px;width:16px;background-color: #DCD345;}
.green-div{height:16px;width:16px;background-color: #ffffff;}
.red-div{height:16px;width:16px;background-color: #C07877;}
.normalTable .yellow-row{background-color:#DCD345 !important;}
.normalTable .red-row{background-color:#C07877 !important;}
.normalTable .green-row{background-color:#26B064 !important;}
.normalTable .white-row{background-color:#FFFFFF !important;}
#loading {
	padding: 5px 8px 5px 26px;
	border: 1px solid #069;
	background: #F1E087 url('/images/refreshing2.gif') no-repeat 2px center;
	position: absolute;
	z-index: 999;
	top: 0px;
	left: 0px;
	display: none;
	font-weight: bold;
}
</style>
<script type="text/javascript">
var entityId = <s:property value="entityId"/>;
var cameraSwitch = '${cameraSwitch}';

$(function(){
	//板卡上线，store重载;
	var board_online = top.PubSub.on(top.OltTrapTypeId.BOARD_ONLINE, function(data){
		trapReloadStore(data);
	});
	//板卡离线，store重载;
	var board_offline = top.PubSub.on(top.OltTrapTypeId.BOARD_OFFLINE, function(data){
		trapReloadStore(data);
	});
	//板卡拔出，store重载;
	var board_remove = top.PubSub.on(top.OltTrapTypeId.BOARD_REMOVE, function(data){
		trapReloadStore(data);
	});
	//板卡重启，store重载;
	var board_reset = top.PubSub.on(top.OltTrapTypeId.BOARD_RESET, function(data){
		trapReloadStore(data);
	});
	//板卡软件版本不匹配，store重载;
	var bd_sw_mismatch = top.PubSub.on(top.OltTrapTypeId.BD_SW_MISMATCH, function(data){
		trapReloadStore(data);
	});
	//PON口禁用 ，store重载;
	var pon_port_disable = top.PubSub.on(top.OltTrapTypeId.PON_PORT_DISABLE, function(data){
		trapReloadStore(data);
	});
	//光模块拔出，store重载;
	var optical_module_remove = top.PubSub.on(top.OltTrapTypeId.OPTICAL_MODULE_REMOVE, function(data){   
		trapReloadStore(data);
	});
	//光模块插入，store重载;
	var optical_module_insert = top.PubSub.on(top.OltTrapTypeId.OPTICAL_MODULE_INSERT, function(data){   
		trapReloadStore(data);
	});
	//SNI端口链路断开, store重载;
	var osni_port_link_down = top.PubSub.on(top.OltTrapTypeId.OSNI_PORT_LINK_DOWN, function(data){   
		trapReloadStore(data);
	});
	//ONU UNI口不可用/信号丢失/linkdown
	var onu_uni_linkdown = top.PubSub.on(top.OltTrapTypeId.ONU_UNI_LINKDOWN, function(data){   
		trapReloadStore(data);
	});

	window.onunload = function(){
		top.PubSub.off(top.OltTrapTypeId.BOARD_ONLINE, board_online);
		top.PubSub.off(top.OltTrapTypeId.BOARD_OFFLINE, board_offline);
		top.PubSub.off(top.OltTrapTypeId.BOARD_REMOVE, board_remove);
		top.PubSub.off(top.OltTrapTypeId.BOARD_RESET, board_reset);
		top.PubSub.off(top.OltTrapTypeId.BD_SW_MISMATCH, bd_sw_mismatch);
		top.PubSub.off(top.OltTrapTypeId.PON_PORT_DISABLE, pon_port_disable);
		top.PubSub.off(top.OltTrapTypeId.OPTICAL_MODULE_INSERT, optical_module_insert);
		top.PubSub.off(top.OltTrapTypeId.OSNI_PORT_LINK_DOWN, osni_port_link_down);
		top.PubSub.off(top.OltTrapTypeId.ONU_UNI_LINKDOWN, onu_uni_linkdown);
	}
});

function setPortStateByBoard(data, state) {
    if(store) {
        var ip = data.ip || data.host;
        // 根据ip找到对应的行，置为下线
        for (var i = 0, len=store.getCount(); i < len; i++) {
            if (store.getAt(i).get('manageIp') === ip) {
                store.getAt(i).set('status', state);
                store.getAt(i).commit();
            }
        }
    }
}

function trapReloadStore(data){
	if(window.entityId && data.entityId && window.entityId == data.entityId){
		if(sniStore){
			sniStore.reload();
		}
		if(ponStore && tab1 && tab1.ponGrid){
			ponStore.reload();
		}
	}
}
</script>
</head>
<body class="newBody">
	<div id="topPart">	
		<%@ include file="/epon/inc/navigator.inc"%>
	</div>
	<div id="putTab" class="edge10 pB0"></div>
	<div class="clearBoth pT10"></div>
	<div id="sniPort" class="jsTabBody clearBoth toplightGrayLine"></div>
	<div id="ponPort" class="jsTabBody clearBoth toplightGrayLine displayNone">
		<b class="orangeTxt">@COMMON.onLoading@</b>
	</div>
	<div id="tip-div" class="thetips">
		<dl id="color-tips" style="padding:3px 0px 3px 3px;">
			<dd class="mR2 yellow-div"></dd>
			<dd class="mR10">@COMMON.onHandling@</dd>
			<dd class="mR2 green-div"></dd>
			<dd class="mR10">@COMMON.success@ (<b id="suc-num-dd">0</b>)</dd>
			<dd class="mR2 red-div"></dd>
			<dd class="mR5">@COMMON.fail@ (<b id="fail-num-dd">0</b>)</dd>
		</dl>
	</div>
	
	<div id="loading">@olt.reOpticalTip@</div>
</body>
</Zeta:HTML>