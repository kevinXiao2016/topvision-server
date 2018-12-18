<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML vmlSupport="true">
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	LIBRARY ext
	LIBRARY jquery
	LIBRARY zeta
	PLUGIN Nm3kTabBtn
	MODULE epon
	IMPORT epon.javascript.OCPage
	IMPORT epon.javascript.OCEntity
	IMPORT epon.javascript.EponViewUtil
	IMPORT epon.javascript.DownLinkService
	IMPORT epon.javascript.DownLinkAction
	IMPORT js/clipboard.min
	css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
.displayNoneImportant{ display:none; display:none !important;}
v\:* { Behavior: url(#default#VML) } 
body,html{height:100%; overflow:hidden;}
/* #QuerySegment {width: 920px;height: 45px;position: absolute;left: 15px;top: 65px;background-color: #ffffff;border: 1px solid #6593CF;} */
#PanelSegment {width: 920px;height: 384px;position: absolute;left: 10px;top: 105px;}
#ScrollSegment {width: 776px;height: 120px;position: absolute;left: 0px;top: 0px;}
#OnuTreePanelContainer {width: 178px;height: 378px;position: absolute;left: 4px;top: -1px;}
#PanelContainer {width: 100%;height: 100%;position: absolute;left: 0px;top: 0px;border:none;}
#GridContainer {width: 231px;height: 378px;position: absolute;left: 693px;top: 2px;border: 1px solid #6593CF;background-color: #ffffff;}
#entityTreePanel {width: 178px;height: 378px;position: absolute;left: 2px;top: 3px;background-color: #ffffff;border: 1px solid #6593CF;}
#oltDeviceDiv {width: 87px;height: 103px;position: absolute;left: 43px; top: 37px; }
#entityContainer {width: 400px;height: 89px;position: absolute;left: 30px;top: 213px;}
#onu_grid {width: 230px;height: 375px;position: absolute;left: 1px;top: 0px;}
.queryPonBoard {position: absolute;left: 10px;top: 13px;width: 85px;height: 20px;}
.queryPonBoard div {position: absolute;left: 0px;top: 3px;width: 44px;height: 20px}
.queryPonBoard select {position: absolute;left: 56px;top: 0px;width: 70px;height: 20px}
.queryPonPort {position: absolute;left: 120px;top: 13px;width: 120px;height: 20px}
.queryPonPort div {position: absolute;left: 0px;top: 2px;width: 70px;height: 20px}
.queryPonPort select {position: absolute;left: 82px;top: 0px;width: 70px;height: 20px}
.queryPonLLID {position: absolute;left: 240px;top: 13px;width: 120px;height: 20px}
.queryPonLLID div {position: absolute;left: 1px;top: 3px;width: 74px;height: 20px}
.queryPonLLID input {position: absolute;left: 82px;top: 0px;width: 72px;height: 20px;text-align:right;padding-right: 2px; }
.queryPonMac {position: absolute;left: 360px;top: 13px;width: 130px;height: 20px}
.queryPonMac div {position: absolute;left: 1px;top: 2px;width: 74px;height: 20px}
.queryPonMac input {position: absolute;left: 82px;top: 0px;width: 111px;height: 20px}
.queryOnuName {position: absolute;left: 535px;top: 13px;width: 129px;height: 20px}
.queryOnuName div {position: absolute;left: 1px;top: 2px;width: 74px;height: 20px}
.queryOnuName input {position: absolute;left: 82px;top: 0px;width: 111px;height: 20px}
/* #QuerySegment #search {position: absolute;top: 13px;left: 740px;}
#QuerySegment #refreshOnuInfo {position: absolute;top: 13px;left: 820px;} */
#QuerySegment #refreshText {position: absolute;top: 16px;left: 441px;}
#loadingMask {position: absolute;left: 0px;top: 0px;width: 100%;height:100%; z-index: 1;	filter:alpha(opacity=50);opacity: 0.5;}
.loadingText {position: absolute;left: 420px;top: 88px;font-size: 10;color:#000}
.loadingImage {position: absolute;left: 380px;top: 79px;height: 32px;width: 32px;}
.spliterClass {position: absolute;left:150px;top: 52px;width: 20px;height: 20px;backgroundColor: #000;z-index:1;}
#viewRightPartTopBody .x-panel-body{ background:transparent;}
#nowPageNums{ padding-right:20px;}
 /* .x-tree-root-ct { overflow:auto !important;}  */
#menu_tips{ height:28px; padding-left:12px; position:absolute; left:210px; top:100px; z-index:99999999999999999; background:url(../images/tip_left.gif) no-repeat; float:left; display:none;}
#menu_tips dd{ height:28px; background:#c00; line-height:26px; padding:0px 10px; color:#fff; text-shadow:#840000 0px 1px; float:left;}
#newtree a.selectedTree{ background-color:#369; color:#fff;}
#showOtherBtn{ position:absolute; top:2px; right:2px;}
.blueLineA{ width:152px; height:142px; background:url('/epon/image/onu/greenLineA.png') no-repeat; position: absolute; top:65px; left:139px; z-index: 2;}
.redLineA{ width:152px; height:142px; background:url('/epon/image/onu/redLineA.png') no-repeat; position: absolute; top:65px; left:139px; z-index: 2;}
.blueLineA_1{width:152px; height:5px; background: #00ff00;  position: absolute; top:66px; left:139px; z-index: 2;}
.blueLineA_2{width:5px; height:140px; background: #00ff00;  position: absolute; top:66px; left:139px; z-index: 2;}

.redLineA_1{width:152px; height:5px; background: #f00;  position: absolute; top:66px; left:139px; z-index: 2;}
.redLineA_2{width:5px; height:140px; background: #f00;  position: absolute; top:66px; left:139px; z-index: 2;}

.blueLineB_1{width:152px; height:5px; background: #00ff00;  position: absolute; top:66px; left:273px; z-index: 2;}
.blueLineB_2{width:5px; height:150px; background: #00ff00;  position: absolute; top:66px; left:423px; z-index: 2;}
.redLineB_1{width:152px; height:5px; background: #f00;  position: absolute; top:66px; left:273px; z-index: 2;}
.redLineB_2{width:5px; height:150px; background: #f00;  position: absolute; top:66px; left:423px; z-index: 2;}
</style>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css" />
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript">
//文字过长，出现红色提示;
$(function(){
	$("#menu_tips").click(function(){
		$(this).fadeOut()
	})
	$("#viewLeftPartBody .x-tree-node-anchor").live("mouseover",function(){
		var $this = $(this);
		var w = $this.offset().left + $this.outerWidth();
		var leftW = $("#viewLeftPart").width();
		if( w >= leftW){
			var txt = $this.find("span").text();
			var tPos = $this.parent().offset().top - 5;
			var o = {text:txt,display:"show",top:tPos, leftPos:leftW-10};
			redTip(o);
		}
	}).live("mouseout",function(){
		var $this = $(this);
		var w = $this.offset().left + $this.outerWidth(); 
		var leftW = $("#viewLeftPart").width();
		if(w >= leftW){
			redTip({display:"hide"});
		}
	});	
	function redTip(o){	
		if(!o.leftPos){ o.leftPos = 190;}
		switch(o.display){
			case "show":
				var txt = o.text;
				$("#menu_tips dd").text(txt);
				var tip_top = o.top;
				$("#menu_tips").css({top:tip_top, display:"block", left:o.leftPos}).stop().animate({left:o.leftPos+10, opacity:0.9})
				break;
			case "hide":
				if($("#menu_tips").is(":visible")){
					$("#menu_tips").css({display:"none", left:"190px"});
				}
				break;
		};
	}
});//end document.ready;
var cmcSupport = <%= uc.hasSupportModule("cmc") %>;
var topoGraphPower = <%= uc.hasPower("topoGraph") %>;
var refreshDevicePower = <%= uc.hasPower("refreshDevice") %>;
var currentId = null;
var uniUnRenderFlag = true;
var onuUnRenderFlag = true;
var ponUnRenderFlag = true;
var topBarSubMenu;
var slotId =  '${slotId}';
var ponId =  '${ponId}';
var entityId = '${entity.entityId}';
var oltSoftVersion = '${oltSoftVersion}';
var typeId = ${entity.typeId};
var oltPonList = ${ponListObject};//pon板
var slotPon = ${slotPonObject};//pon口
//onu反键菜单状态设置
var onuAdminStatusTemp;
var onuVOIPStatusTemp = 1;
var onuCATVStatusTemp = 1;
var onuTemperatureStatusTemp;
var onuFECStatusTemp;
var onuIsolationEnableTemp;
var pon15minEnableTemp;
var pon24hourEnableTemp;
var onuIgmpMode = ${onuIgmpMode};
onuIgmpMode = (onuIgmpMode.join()=="false") ? new Array() : onuIgmpMode;
//uni反键菜单状态设置
var uniFlowEnableTemp;
//var uniIsolationEnableTemp;
var uniAdminStatusTemp;
var uni15minEnableTemp;
var uni24hourEnableTemp;
var uniAutoNegoEnableTemp;
var macStatusFlag;//1为空        2为:-空格 或无间隔的12位 Mac地址         3为长整形（不超过18位）

var isCmcSupport = <%= uc.hasSupportModule("cmc") %>;

var displayStyle = '${displayStyle}';
var textId = 1;
var entityList = ${entityList};
var uniId;
var tishi1 = "";
var tishi2 = "";
var tishi3 = "";
var onuDevice = null;
var onuEntity = new Array();
//var onuImg = "image/onuDevice.jpg";
var onuImg = "image/onu/onu.png";
var oltImg = '';
var treeFlag = false;
var cascadeFlag = false ;
var onuStatus = ["@COMMON.offline@", "@COMMON.online@" ,"@COMMON.offline@"];
var Enable = ["@COMMON.close@", "@COMMON.open@" ,"@COMMON.close@"];
var likeStatus = ['DOWN', 'UP' ,'DOWN'];
var uniAutoStatus = ["tenBaseTFullDuplex", "tenBaseTHalfDuplex", "hundredBaseTFullDuplex", "hundredBaseTHalfDuplex", "thousandBaseTFullDuplex", "thousandBaseTHalfDuplex", "thousandBaseXFullDuplex", "thousandBaseXHalfDuplex", "fdxPause", "fdxApause", "fdxSpause", "fdxBpause"]
var COMTYPE = ["", "rs-232","rs-485"]
var PARITY = ["","none", "even" , "odd" , "space" ,"mark"]
var SRVTYPE = ["none", "tcp-server","tcp-client","udp"]
var BAUD = ['',"b110",'b300','b600','b1200','b2400','b4800','b9600','b14400','b19200','b38400','b57600','b115200']
var divCache = new Array()
var subListData = ${subListArray};

Ext.Ajax.timeout = 1000000;
var ONU_10G_TYPE = 40;
var ONU_MTK_TYPE = ["37", "38"];
$(function(){
	//板卡拔出，页面刷新;
	var board_remove = top.PubSub.on(top.OltTrapTypeId.BOARD_REMOVE, function(data){
		trapRefreshPage(data);
	});
	//板卡离线，刷新页面;
	//TOPO 目前是简单粗暴的刷新了一下，存在选择的不是第一个下级设备，刷新后，变成选择第一个设备的情况;
	var board_offline = top.PubSub.on(top.OltTrapTypeId.BOARD_OFFLINE, function(data){
		trapRefreshPage(data);
	});
	//板卡重启，刷新页面;
	var board_reset = top.PubSub.on(top.OltTrapTypeId.BOARD_RESET, function(data){
		trapRefreshPage(data);
	});
	//板卡软件版本不匹配，刷新页面;
	var bd_sw_mismatch = top.PubSub.on(top.OltTrapTypeId.BD_SW_MISMATCH, function(data){
		trapRefreshPage(data);
	});
	//PON口禁用 ，刷新页面;
	var pon_port_disable = top.PubSub.on(top.OltTrapTypeId.PON_PORT_DISABLE, function(data){
		trapRefreshPage(data);
	});
	//光模块拔出,刷新页面 ;
	var optical_module_remove = top.PubSub.on(top.OltTrapTypeId.OPTICAL_MODULE_REMOVE, function(data){   
		trapRefreshPage(data);
	});
	//主干光纤断纤，刷新页面;
	var port_pon_los = top.PubSub.on(top.OltTrapTypeId.PORT_PON_LOS, function(data){   
		trapRefreshPage(data);
	});
	//ONU掉电，刷新页面;
	var onu_pwr_off = top.PubSub.on(top.OltTrapTypeId.ONU_PWR_OFF, function(data){   
		trapRefreshPageParentId(data);
	});
	//onu下线，刷新页面，注意要对比parentId;
	var onu_offline = top.PubSub.on(top.OltTrapTypeId.ONU_OFFLINE, function(data){  
		trapRefreshPageParentId(data);
	});
	//onu上线，刷新页面，注意要对比parentId;
	var onu_online = top.PubSub.on(top.OltTrapTypeId.ONU_ONLINE, function(data){
		trapRefreshPageParentId(data);
	});
	//onu断纤，刷新页面，注意要对比parentId;
	var onu_fiber_break = top.PubSub.on(top.OltTrapTypeId.ONU_FIBER_BREAK, function(data){
		trapRefreshPageParentId(data);
	});
	
	//onu长发光，刷新页面，注意要对比parentId;
	var onu_rogue = top.PubSub.on(top.OltTrapTypeId.ONU_ROGUE, function(data){
		trapRefreshPageParentId(data);
	});
	
	//onu删除，刷新页面，注意要对比parentId;
	var onu_delete = top.PubSub.on(top.OltTrapTypeId.ONU_DELETE, function(data){
		trapRefreshPageParentId(data);
	});
	//CMC复位，刷新页面;
    var cmc_reset = top.PubSub.on(top.CmcTrapTypeId.CMC_RESET, function(data){
   		trapRefreshPageParentId(data);
    });
    //系统手动重启，刷新页面;
    var dol_reboot = top.PubSub.on(top.CmcTrapTypeId.DOL_REBOOT, function(data){
       	trapRefreshPageParentId(data);
    });
    //CMTS断电，刷新页面;
    var cmts_power_off = top.PubSub.on(top.CmcTrapTypeId.CMTS_POWER_OFF, function(data){
    	trapRefreshPageParentId(data);
    });
    //CMTS断纤，刷新页面;
    var cmts_link_lose = top.PubSub.on(top.CmcTrapTypeId.CMTS_LINK_LOSE, function(data){
    	trapRefreshPageParentId(data);
    });
    //CMTS下线，刷新页面;
    var cmc_offline = top.PubSub.on(top.CmcTrapTypeId.CMTS_OFFLINE, function(data){
    	trapRefreshPageParentId(data);
    });
  	//ONU UNI口不可用/信号丢失/linkdown
    var onu_uni_linkdown = top.PubSub.on(top.OltTrapTypeId.ONU_UNI_LINKDOWN, function(data){
    	trapRefreshPageParentId(data);
    });
	
	window.onunload = function(){
		top.PubSub.off(top.OltTrapTypeId.BOARD_REMOVE, board_remove);
		top.PubSub.off(top.OltTrapTypeId.BOARD_OFFLINE, board_offline);
		top.PubSub.off(top.OltTrapTypeId.BOARD_RESET, board_reset);
		top.PubSub.off(top.OltTrapTypeId.BD_SW_MISMATCH, bd_sw_mismatch);
		top.PubSub.off(top.OltTrapTypeId.PON_PORT_DISABLE, pon_port_disable);
		top.PubSub.off(top.OltTrapTypeId.OPTICAL_MODULE_REMOVE, optical_module_remove);
		top.PubSub.off(top.OltTrapTypeId.PORT_PON_LOS, port_pon_los);
		top.PubSub.off(top.OltTrapTypeId.ONU_PWR_OFF, onu_pwr_off);
		top.PubSub.off(top.OltTrapTypeId.ONU_OFFLINE, onu_offline); 
		top.PubSub.off(top.OltTrapTypeId.ONU_ONLINE, onu_online);
		top.PubSub.off(top.OltTrapTypeId.ONU_FIBER_BREAK, onu_fiber_break);
		top.PubSub.off(top.OltTrapTypeId.ONU_ROGUE, onu_rogue);
		top.PubSub.off(top.OltTrapTypeId.ONU_DELETE, onu_delete);
		top.PubSub.off(top.OltTrapTypeId.ONU_UNI_LINKDOWN, onu_uni_linkdown);
		
		top.PubSub.off(top.CmcTrapTypeId.CMC_RESET, cmc_reset);
		top.PubSub.off(top.CmcTrapTypeId.DOL_REBOOT, dol_reboot);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_POWER_OFF, cmts_power_off);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_LINK_LOSE, cmts_link_lose);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_OFFLINE, cmc_offline);
	};
	
});
//刷新页面，对比entityId
function trapRefreshPage(data){
	if(window.entityId && data && data.entityId && window.entityId == data.entityId){
		HeadMessage.sendMsg(data.clearMessage || data.message);
	}
}
//刷新页面，对比parentId
function trapRefreshPageParentId(data){
	if(window.entityId && data && data.parentId && window.entityId == data.parentId){
		HeadMessage.sendMsg(data.clearMessage || data.message);
	}
}

//<!-- ONU滚动条相关数据操作，交互操作方法封装-->//
window.onmousemove = document.onmousemove = null

// 新建CMC设备并且关联到ONU操作
function addAndRelateCmcToOnu() {
	var uniList = page.entity.onuUniPortList;
	var res = new Array();
	while(uniList.length > 0){
		var uni = uniList.pop();
		res.push(uni.uniId);
	}
	window.parent.createDialog("addCmcEntity", "@ONU.createEntity@", 480, 360, "/cmc/cmcEntityAdd.jsp", null, true, true);
}
// 关联CMC到ONU操作
function relateCmcToOnu() {
	window.parent.createDialog("addCmcEntity", "@ONU.createEntity@", 480, 360, "/cmc/CmcEntityOnuRelationBind.jsp", null, true, true)
}
//温度检测使能变更后
function tempDetectEnableChanged(s, onuId){
	var tmpSource = page.grid.getSource();
	if(s == 2){//closed
		tmpSource.@COMMON.TMP@ = null;
		page.grid.setSource(tmpSource);
	}else if(s == 1){//opened
		if(page.entityId == onuId){
			tmpSource.@COMMON.TMP@ = "@ONU.collecting@"
			entity.topOnuCurrentTemperature = "@ONU.collecting@"
			page.grid.setSource(tmpSource);
		}
		Ext.Ajax.request({
			url:"/onu/refreshOnuTemperature.tv",
			success: function (text) {
			/* if(text==null || text.responseText=='false'){
				setTimeout(function(){tempDetectEnableChanged(2, onuId);},60000);
			}
			if(page.entityId == onuId){
			if(!text.responseText || parseInt(text.responseText) == 127 || isNaN(text.responseText)){
				tmpSource.@COMMON.TMP@ = null;
				entity.topOnuCurrentTemperature = null;
			}else{
				entity.topOnuCurrentTemperature = isNaN(text.responseText) ? null : text.responseText;
				tmpSource.@COMMON.TMP@ = isNaN(text.responseText) ? null : (text.responseText + " "+ "@COMMON.degreesCelsius@");
			} */
			//page.grid.setSource(tmpSource);
			}, failure: function () {
			}, params: {entityId : entityId, onuId : onuId}
		
		});
	}
}
//温度采集回调函数
window.top.addCallback("refreshOnuTemp",function(m){
	var message = m.message;
	var entityId = m.entityId;
	var onuId = m.onuId;
	
	var tmpSource = page.grid.getSource();
	if(message == null || message == 'false'){
		return;
	}
	if(parseInt(message) == 127){
		tmpSource.@COMMON.TMP@ = null;
		entity.topOnuCurrentTemperature = null;
	}else{
		entity.topOnuCurrentTemperature = isNaN(message) ? null : message;
		tmpSource.@COMMON.TMP@ = isNaN(message) ? null : (message + " "+ "@{unitConfigConstant.tempUnit}@");
	}
	page.grid.setSource(tmpSource);
});


function showIgmpUni(s){
	var str = "";
	var title = "";
	if(s == 1){
		title = "Mvlan";
		str = "@ONU.uniMvlan@"
	}else if(s == 2){
		title = "Mgmt";
		str = "@ONU.igmpUni@"
	}
	var uniIndex = (entity.onuUniPortList[currentId.substring(7) - 1].uniIndex);
	var uniLoc = getLocationByIndex(uniIndex, 'uni');
	window.parent.createDialog("igmpUni" + title, str + '(UNI:'+ uniLoc +')', 720, 480, 
			String.format("epon/igmp/showIgmpUni{0}.tv?entityId={1}&uniIndex={2}", title, entityId, uniIndex), null, true, true);
}

//实时获取ONU PON口的状态信息
var opticalFlag = 0;
var oltPortOptical = new Array();
function loadOnuPonOptical(onuPonIndex){
	var url = '/epon/optical/loadOnuPonOptical.tv?r=' + Math.random();
	var par = {entityId: entityId, portIndex: onuPonIndex};
	var tmpFlag = ++opticalFlag;
	var tmpIndex = -1;
	var ol = oltPortOptical.length;
	for(var a=0; a<ol; a++){
		if(oltPortOptical[a][0] && oltPortOptical[a][0] == onuPonIndex){
			tmpIndex = a;
			break;
		}
	}
	if(tmpIndex == -1){
		oltPortOptical[ol] = new Array();
		oltPortOptical[ol][0] = onuPonIndex;
		tmpIndex = ol;
	}
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText == 'false'){
				return;
			}
			if(tmpFlag == opticalFlag && tmpIndex > -1){
				var data = Ext.util.JSON.decode(response.responseText).data || new Array();
				oltPortOptical[tmpIndex] = data;
				var tmpSource = page.grid.getSource();
				//tmpSource["@ONU.tempreture@"] = data[1] && data[1] != 0 ? parseInt(data[1])/100 + " "+ "@COMMON.degreesCelsius : "@Optical.couldntGetData;
				tmpSource["@ONU.tempreture@"] = (data[6] && data[6] != 0)? data[6] + " "+ "@{unitConfigConstant.tempUnit}@" : "--";
				tmpSource["@ONU.recOptRate@"] = (data[2] && data[2] != 0)? parseInt(data[2])/100 + " "+"@COMMON.dBm@" : "--";
				tmpSource["@ONU.sendOptRate@"] = (data[3] && data[3] != 0)? parseInt(data[3])/100 + " "+"@COMMON.dBm@" : "--";
				tmpSource["@SUPPLY.biasCurrent@"] = (data[4] && data[4] != 0)? parseInt(data[4])/100 + " mA" : "--";
				tmpSource["@ONU.voltage@"] = (data[5] && data[5] != 0)? parseInt(data[5])/100000 + " V" : "--";
				page.grid.setSource(tmpSource);
			}
		},
		failure : function() {
		},
		params : par
	});
}
function initOnuPonOptical(){
	var url = '/epon/optical/getAllOnuOptical.tv?r=' + Math.random();
	var par = {entityId: entityId};
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText == 'false'){
				return;
			}
			oltPortOptical = Ext.util.JSON.decode(response.responseText).data || new Array();
		},
		failure : function() {
		},
		params : par
	});
}

function showEntity(entityId, entityType){
	page.changeEntity( entityId, entityType );
}

function initClipboardTip(){
	if(typeof Clipboard != 'undefined'){
		if(Clipboard.isSupported()){
			$('#viewRightPartBottomTit a.pannelTilArrDown').before('<b class="orangeTxt">@COMMON.clipboardTip@</b>');
			var clipboard = new Clipboard('.x-grid3-cell-inner', {
				text: function(me) {
		            var txt = $(me).text();
					return txt;
		        }
			});
			clipboard.on('success', function(e) {
				top.afterSaveOrDelete({
			        title: '@COMMON.tip@',
			        html: '<b class="orangeTxt">@COMMON.clipboardSuccess@</b>'
			    });
			});
		}
	}
}


$( DOC ).ready(function(){	
	//如果支持复制到剪切板功能，属性栏后面必须加入说明文字“属性（单击文字将自动复制到剪切板）”;
	initClipboardTip();
	
	top.nm3kRightClickTips({
		title: '@COMMON.tip@',
		html: "@COMMON.subEquipmentTip@"
	})
	initOnuPonOptical();
	
	// 为查询框添加回车事件
	$("#QuerySegment").keydown(function(event){
        if(event.keyCode==13){
            $("#search").click();
        }
	});
	$(".ultab").append($("#QuerySegment").show().remove());
	
	var leftTree = {
		onu : [],		
		cmc : [],
		onuStr : '',
		cmcStr : ''
	};
	
	var newDom = '<ul id="newtree" class="filetree">';
	for(var i=0; i < subListData.length; i++){
		var slot = subListData[i];
		var portArray = slot.children;
		newDom +=    '<li>';
		newDom +=     '<span class="folder">slot:'+ slot.slotNo +'</span>';
		newDom +=     '<ul>';
		for(var j=0; j<portArray.length; j++){
			var port = portArray[j];
			var subDevice = port.children;
			newDom +=    '<li>';
			newDom +=     '<span class="folder">port:'+ port.portNo +'</span>';
			newDom +=     '<ul>';
			for(var k=0; k < subDevice.length; k++){
				var entity = subDevice[k];
				var online = (entity.oprationStatus == 1) ? 'onlineLink' : 'offlineLink';
				newDom += String.format('<li><span><a href="javascript:;" class=" TREE-NODE yellowLink {0}" macOrSn="{4}" name="{3}" onuid="{1}" onclick="showEntity({1},{2})">{3}</a></span></li>', online, entity.entityId, entity.entityType, entity.entityName, entity.onuMac || entity.sn);
			}
			newDom += '</ul>';
			newDom += '</li>';
		}
		newDom += '</ul>';
		newDom += '</li>';
	}
	newDom += '</ul>';
	$("#viewLeftPartBody").html(newDom);
	
	//加载树形菜单;
	$("#newtree").treeview({ 
		animated: "fast"
	});	//end treeview;

	$("#newtree a.yellowLink:eq(0)").addClass("selectedTree");
});

function authLoad(){
    if(!refreshDevicePower){
        $("#refreshOnuInfo").attr("disabled",true);
    }
}
</script>
</head>
<body class="newBody overfloatHidden bodyWH100percent" onload="authLoad()">
	<div id="treeLoading" class="treeLoading loadingText" style="z-index:100001">@COMMON.fetching@</div>
	<div id="loadingMask" style="display: none;" class="loadingMaskBg">
		<!-- <img class="loadingImage" src='/images/gray_loading.gif'
			alt=@COMMON.fetching@ />
		<div class="loadingText"></div> -->
	</div>
	<div id="tishiDiv" style="display: none;"></div>
	<div class=formtip id=tips style="display: none"></div>
	
	<div class="wrapWH100percent overfloatHidden">
		<!--头部菜单开始 -->
		<%@ include file="/epon/inc/navigator.inc"%>
		<div id=QuerySegment class="pL10 pB0 clearBoth topGrayLine whiteToBlack displayNone">
			<table class=noWrap border=0 rules=none cellSpacing=0 cellPadding=0
				width="100%">
				<tbody>
					<tr>
						<td><ul style="POSITION: relative; MARGIN: 0px; TOP: 5px" class=leftFloatUl>
								<li><a
									style="PADDING-BOTTOM: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 16px; PADDING-TOP: 0px"
									id="refreshOnuInfo" class=normalBtn onclick="refreshOnuInfo()";
									href="javascript:;"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
							</ul></td>
					</tr>
				</tbody>
			</table>
			<div style="DISPLAY: none" id="refreshText"></div>
		</div>
		<!--头部菜单结束 -->
		<!-- 左侧开始 -->
		<div class="viewLeftPart" id="viewLeftPart">
			<p class="pannelTit" id="sidePartTit">@ONU.downlinkTree@</p>
			<div id="viewLeftPartBody" class="leftMain-LR clear-x-panel-body viewLeftPartBody" style="position:relative;"></div>
			<a id="showOtherBtn" href="javascript:;" class="normalBtnIco" onclick="showOtherMenuFn()">
					<span><i class="miniIcoArrangeWithSub" style="width:28px;"></i></span>
				</a>
		</div>
		
		<!-- 左侧结束 -->
		<div id="viewLeftLine" class="viewLeftLine"></div>
		
		<!-- 中间部分开始 -->
		<div id="viewMiddle" class="viewMiddle">
			<div class="middlePartPutPic" >
				<div id="PanelContainer">
					<div id="entityContainer"></div>
				</div>
				<div id="entityType" style="position: absolute; left: 10px; top: 350px; z-index: 1000"></div>
			</div>
			
			<!-- 中间底部设备说明开始 -->
			<div id="viewMiddleBottom" class="viewMiddleBottom threeFeBg displayNoneImportant" >
				<div id="viewMiddleBottomLine" class="horizontalLine displayNone" style="height:7px; cursor:n-resize">
					<div class="dragDot"></div>
				</div>
				<p id="viewMiddleBottomTit" class="pannelTitWithTopLine">@SERVICE.downlink@	
					<span style="position:absolute; top:4px; right:40px;">@SERVICE.downlinkScroll@</span>
					<a href="javascript:;" class="pannelTilArrUp"></a>
				</p>
				<div id="viewMiddleBottomBody" class="displayNone overflowAuto threeF4Bg" style="position:relative;">
				
					<!-- 新的下级设备，我将其渲染到该id为"subPannel"的div中，请不要修改其id -->
					<div class="edge5">
						<div id="subPannel"></div>
					</div>
				</div>
			</div>
			<!-- 中间底部设备说明结束 -->
			
			<!-- 切换布局按钮开始 -->
			<div class="abAndTR10" id="putTabBtn"></div>
			<!-- 切换布局按钮结束 -->
		</div>
		<!-- 中间部分结束 -->
		<div id="viewRightLine" class="viewRightLine"></div>
		
		<!-- 右侧部分开始 -->
		<div id="viewRightPart" class="viewRightPart">
			<!-- 右侧上半部分开始 -->
			<p id="viewRightPartTopTit" class="pannelTit">@EPON.oltAboutFunction@<a href="javascript:;" class="pannelTilArrDown"></a></p>
			<div id="viewRightPartTopBody" class="rightPannel propertyBg">
				
			</div>
			<!-- 右侧上半部分结束 -->
			<div id="viewRightPartMiddleLine" class="horizontalLine" style="height:7px; cursor:n-resize"><div class="dragDot"></div></div>
			<!-- 右侧下半部分开始 -->
			<p id="viewRightPartBottomTit" class="pannelTitWithTopLine">@COMMON.property@<a href="javascript:;" class="pannelTilArrDown"></a></p>
			<div id="viewRightPartBottomBody" class="rightPannel propertyBg">				
			</div>
			<!-- 右侧下半部分结束 -->
			
		</div>
		<!-- 右侧部分结束 -->
	</div>
	<!-- 提示信息 -->
	<dl id="menu_tips">	<dd>tips</dd>	</dl>
</body>

<script type="text/javascript">
/**
 * 等待框
 */
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}

/**
 * 清理页面效果
 */
function clearPage() {
	while(divCache.length != 0) {
		var divCacheObject = divCache.pop()
		if(entity.uniStyle == '16*16' && divCacheObject.substring(0,6) == 'onuUni')
			document.getElementById(divCacheObject).style.border = '1px solid transparent'
		else if (entity.uniStyle == '16*16' && divCacheObject.substring(0,6) == 'onuPon')
			document.getElementById(divCacheObject).style.border = '1px solid transparent'
	    else if (entity.uniStyle == '16*16' && divCacheObject.substring(0,6) == 'onuCom')
	        document.getElementById(divCacheObject).style.border = '1px solid transparent'
		else
			document.getElementById(divCacheObject).style.border = '2px solid transparent'
		//修改上次点击的div的样式
	}
}

/**
 * 时间换算。用于ONU连续在线时长的时间 换算：秒数 —> String
 */
function arrive_timer_format(s) {
	var t
	if(s > -1){
		hour = Math.floor(s/3600);
	    min = Math.floor(s/60) % 60;
	    sec = Math.floor(s) % 60;
	    day = parseInt(hour / 24);
	    if (day > 0) {
	    	hour = hour - 24 * day
	        t = day + "@COMMON.D@" + hour + "@COMMON.H@"
	    } else {
	  		t = hour + "@COMMON.H@"
	 	}
		if(min < 10){
			t += "0"
		}
	    t += min + "@COMMON.M@"
	    if(sec < 10){
			t += "0"
		}
	      t += sec + "@COMMON.S@"
	}
	return t
}


//打开ACL端口页面
function showAclPort(index,direction){
	var uniLoc = getLocationByIndex(index, 'uni');
	window.parent.createDialog("aclPort", "@COMMON.port@:" + uniLoc + "@ONU.boundAclPort@", 640, 450, 
			"/epon/acl/showAclPort.tv?entityId="+entityId+"&portIndex=" + index + "&direction=" + direction + "&aclPortJspFlag=0", 
			null, true, true);	
}

/**
 * 得到端口或者设备的真实id
 */
function getActualId() {
	var array = currentId.split("_");
	if("onuPon" == array[0] || 0 == array[1]) {
		return entity.onuPonPort.onuPonId;
	} else if("onuCom" == array[0] || 0 == array[1]){
           return entity.onuComPortList[array[1] - 1].comId;
       } else {
		return entity.onuUniPortList[array[1] - 1].uniId;
	}
}

/**
 * 得到端口或者设备的index
 */
function getActualIndex() {
	var array = currentId.split("_");
	if("onuPon" == array[0] || 0 == array[1]) {
		return entity.onuPonPort.onuPonIndex;
	} else if("onuCom" == array[0] || 0 == array[1]){
		return entity.onuComPortList[array[1] - 1].comIndex;
	} else {
		return entity.onuUniPortList[array[1] - 1].uniIndex;
	}
}

/*
 * LLID有效性检查
 */
function checkLlid() {
	var reg = /^([a-f0-9]{1,4})+$/i;
	if($("#llid").val() == "") {
		return true;
	}
	if(!reg.exec($("#llid").val())) {
		return false;
	}
	return true;
}

/**
 * Mac地址有效性检查
 */
function checkOnuMac() {
	var $mac = $("#mac").val();
	if($mac=="" ||$mac ==null){
		return true;
	}
	return V.isFuzzyMacAddress($mac);
}
//通过mibIndex获得num
function getNum(index, s){
	var num;
	switch (s) {
		case 1: num = (index & 0xFF000000) >> 24;//SLOT
			break;
		case 2: num = (index & 0xFF0000) >> 16;//PON/SNI
			break;
		case 3: num = (index & 0xFF00) >> 8;//ONU
			break;
		case 4: num = index & 0xFF;//UNI
			break;
	}
	return num;
}
//通过index获得location
function getLocationByIndex(index, type){
	var t = (parseInt(index / 65536) * 256) + (index % 256);
	var loc = getNum(t, 1) + "/" + getNum(t, 2) + ":" + getNum(t, 3);
	if(type == "uni"){
		loc = loc + "/" + getNum(t, 4)
	}
	return loc
}


Date.prototype.dateDiff = function(date, flag) {
	var msCount
	var diff = this.getTime() - date.getTime()
	switch (flag) {
		case "ms":
			msCount = 1
			break
		case "s":
			msCount = 1000
			break
		case "m":
			msCount = 60 * 1000
			break
		case "h":
			msCount = 60 * 60 * 1000
			break
		case "d":
			msCount = 24 * 60 * 60 * 1000
			break
	}
	return Math.floor(diff / msCount)
};
			
function refreshBtOver(){
	var tempSlotId = $("#ponSlotList").val()
	var tempPonId = $("#slotPonList").val()
	$("#search").hide()
	$(".queryPonMac").hide()
	$(".queryPonLLID").hide()
	$(".queryOnuName").hide()
	if(tempPonId != -1){
		$("#refreshText").show().html("@ONU.loadFromPon@")
	}else if(tempSlotId != -1){
		$("#refreshText").show().html("@ONU.loadFromBoard@")
	}else{
		$("#refreshText").show().html("@ONU.loadFromOlt@")
	}
}
function refreshBtOut(){
	$("#refreshText").hide()
	$("#search, .queryPonMac, .queryPonLLID,.queryOnuName").show()
}

function createRefreshMask(){
	$("#loadingMask").show();
	$("#treeLoading").show();
	var w = $(window).width();
	var w2 = $("#treeLoading").outerWidth();
	var h = $(window).height();
	var h2 = $("#treeLoading").outerHeight();
	$("#treeLoading").css("left",(w-w2)/2).css("top",(h-h2)/2);
	$(".loadingText").html("<p>@COMMON.fetching@</p>")
	$("#loadingMask").attr("style","z-index:100000")
}

function removeMask(){
	$("#loadingMask").animate({
		opacity:0.5
	},{
		speed: 'slow',
		complete :function(){
			$("#loadingMask").animate({
				opacity:0
			},{
				speed: 'slow',
				complete :function(){
					$("#loadingMask").css("zIndex",1)
					$("#loadingMask").hide()
					$("#treeLoading").hide();
				}
			})
		}
	})
}

function modifyIgmpOnuMode(index, mode){
	for(var t=0; 2*t<onuIgmpMode.length; t++){
		if(onuIgmpMode[2*t] == index){
			onuIgmpMode[2*t + 1] = mode
		}
   	}
}

function locationChanged(){page.locationChanged();}
function searchOnu(){//点击查询按钮;
	var v = $("#onuName").val();
	if(v.indexOf("'") > -1){//如果包含了单引号，则验证不通过.
		$("#onuName").focus();
		return;
	}
	$("#subPannel .nm3kScollBarMiddleTable").css({left:0});//将滚动的还原;
	page.searchEntity();
}
function refreshOnuInfo(){
	page.collect(); 
}
</script>
<script type="text/javascript" src="/js/jquery/jquery.wresize.js"></script>
	<script type="text/javascript" src="/js/jquery/dragMiddle.js"></script>	
	<script type="text/javascript" src="/js/jquery/nm3kScollBarContainer.js"></script>
	
	<script type="text/javascript">
	var nm3k = {};//用来记录拖动元素，改变布局的时候，方便销毁拖动功能;
	nm3k.rightTopHeight = 280;//记录右侧上部的高度;
	nm3k.middleBottomHeight = 100;//记录中间底部设备说明的高度;
	nm3k.leftWidth = 200;//左侧宽度;
	nm3k.rightWidth = 255;//右侧宽度;
	nm3k.rightTopOpen = true;//右侧上部是否展开;
	nm3k.rightBottomOpen = true;//右侧下部是否展开;
	nm3k.middleBottomOpen = true;//中间下部是否展开;
	nm3k.layoutToMiddle = true;//面板图是否在中间(不在中间，就是在右边);
	nm3k.tabBtnSelectedIndex = 0;//记录该显示选中哪个布局按钮;
	if( ${layout.leftWidth}){
		nm3k.rightTopHeight =  ${layout.rightTopHeight};//记录右侧上部的高度;
		nm3k.middleBottomHeight = ${layout.middleBottomHeight};//记录中间底部设备说明的高度;
		nm3k.leftWidth = ${layout.leftWidth};//左侧宽度;
		nm3k.rightWidth = ${layout.rightWidth};//右侧宽度;
		nm3k.rightTopOpen = ${layout.rightTopOpen};//右侧上部是否展开;
		nm3k.rightBottomOpen = ${layout.rightBottomOpen};//右侧下部是否展开;
		nm3k.middleBottomOpen = ${layout.middleBottomOpen};//中间下部是否展开;
		nm3k.layoutToMiddle = ${layout.layoutToMiddle};//面板图是否在中间(不在中间，就是在右边);
		nm3k.tabBtnSelectedIndex = ${layout.tabBtnSelectedIndex};//记录该显示选中哪个布局按钮;
	}
	/*///////////////在这个部分读取后台的数据改变上面的默认值///////////////////////////////*/
	
	</script>
	<script type="text/javascript" src="/js/jquery/nm3kViewLayout.js">
	
	</script>
	<script type="text/javascript">
	window.page = new OCPage();
	page.init();
	initTopBarSubMenu();
	function changeEntityName(entityId,name){
		window.page.changeEntityName(name);
		$("#newtree a.selectedTree").text(name);
	}
	
	$("#newtree a.yellowLink").live("click",function(){
		$("#newtree a.yellowLink").removeClass("selectedTree");
		$(this).addClass("selectedTree");
	})
	
	//切换下拉菜单;
	function initTopBarSubMenu(){
		topBarSubMenu = new Ext.menu.Menu({});
		var items = [];
		items.push({id:"view1", text:"MAC | SN", handler: macOrSnDisplay });
		items.push({id:"view2", text:"@COMMON.name@", handler: nameDisplay });
		topBarSubMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
	}
	
	function macOrSnDisplay(){	
		$(".TREE-NODE").each(function( $idx, $node ){
			var $this = $(this);
			$this.text( $this.attr("macOrSn") || '-');
		});
	}

	function nameDisplay(){
		$(".TREE-NODE").each(function( $idx, $node ){
			$(this).text( $node.name );
		});
	}
	
	//显示其它;
	function showOtherMenuFn(){
		if(topBarSubMenu == null){return;}
		var $btn = $("#showOtherBtn"),
		aPos = [];
		aPos.push($btn.offset().left);
		aPos.push($btn.offset().top + $btn.outerHeight());
		topBarSubMenu.showAt(aPos);
	}
	
	</script>
	
</Zeta:HTML> 
