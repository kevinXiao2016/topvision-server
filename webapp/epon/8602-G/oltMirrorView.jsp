<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML vmlSupport="true">
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    PLUGIN Nm3kTabBtn
    MODULE epon
    IMPORT epon.javascript.Olt static
    IMPORT epon.8602-G.oltCreationLib
    IMPORT epon.javascript.EponViewUtil
    IMPORT epon.javascript.EponMirrorHelper
    IMPORT epon.oltViewMenuLib
    IMPORT epon.js.oltFaceplateTrap
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
v\:oval { behavior: url(#default#VML);display:inline-block } 
.X-BOARD-TYPE{font-weight:bold;position:absolute;left:20px;top:5px;z-index=5000;}
</style>
<script type="text/javascript">
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var json = null;
var mirrorListJson = null;
var olt = new Olt;
var entityId = '${entity.entityId}';
var image_state = new Array(10);
var led_state = ['red','green','yellow'];
var boardColor = "#71787e";
var CASCADE_LOCK = false ;
var noSelected = false;
var ctrlFlag = 0;
var divCache = new Array();
var divCache2 = new Array();
var indexCache = new Array();
var enableId = "";   //反键变更的itemId
var currentId = ""; //记录当前点击的divID，解析ID得到slot，pon口传给弹出页面
var mirror_attr_grid = null;//mirror属性表格
var mirror_attrStore = null;//mirror属性
var mirror_grid = null;//mirror列表
var cameraSwitch = '${cameraSwitch}';
// 计时器(秒)
var timer = 0;
// 采集间隔(秒)
var mirrorSelected = "";//标识选中的vlan

String.prototype.startWith = function(str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length) {
        return false;
    }
    if (this.substr(0, str.length) == str) {
        return true;
    } else {
        return false;
    }
    return true;
}


//初始化设备图
function initialdata(json) {
	createPN8602G();
	$("#OLT").unbind("click mouseover mouseout contextmenu");
}
function createPort(sID, pID, pType, top,left, sType,sPreType) {//create one port every time:slotRealIndex(1-18),portID(0-8),portType,Top px
	var positionInfo = getPosition(sPreType, pID);
	
    $port = $("<div>");
	$port.attr({
		id : sPreType + "_" + sID + "_" +pID,
		title : "@EPON.portType@:"+pType+  "@EPON.portLoc@:" + pID
	});
	$port.css({
		width : positionInfo.width || coordinateParam.portWidth,
        height : positionInfo.height || coordinateParam.portHeight,
		position : 'absolute',
		top : top,
        left : left,
		backgroundImage : 'url(/epon/image/' + pType+'/'+pType + '.png)',	
		backgroundRepeat : 'no-repeat', 
		border : '1px solid transparent',
		zIndex : 3500
	});
	$("#OLT").append($port);
	var pport;
	for(var i=0; i<olt.slotList[sID-1].portList.length; i++) {
        if(olt.slotList[sID-1].portList[i].portRealIndex == pID) {
            pport = olt.slotList[sID-1].portList[i];
            break;
        }
    }
    
    createPortLamp(pport);
	
	$port.bind('contextmenu', contextHandler ).bind('click', contextHandler );
}
/* function createPort(sID, pID, pType, top,left, sType,sPreType) {
	// 位置相关信息
	var positionInfo = getPosition(sPreType, pID);
	
	
    $port = $("<div>");
    // modify by fanzidong,根据板卡类型，GE/XE口位置需要转换
	$port.attr({
		id : sPreType + "_" + sID + "_" +pID,
		title : I18N.EPON.portType + ':'+pType+ " " + I18N.EPON.portLoc + ': '+getPortNoByType(sPreType, pID),
		portSubType: pType
	});
	$port.addClass("portClass"); 
	$port.css({
		width : positionInfo.width || coordinateParam.portWidth,
		height : positionInfo.height || coordinateParam.portHeight,
		position : 'absolute',
		top :  top,
		left : left,
		backgroundImage : 'url(/epon/image/' + pType + '/'+pType+'.png)',
		backgroundRepeat : 'no-repeat',
		border: '1px solid transparent',
		zIndex : 1500
	});
	$("#OLT").append($port);
    $port.bind('mouseover',function(e){
    	var arr = this.id.split("_");
    	var num = parseInt(arr[2], 10);
    	var maxNum;
    	if(sPreType === 'mgua'){
    		maxNum = 17;
    	}
    	if(num >= maxNum){
    		switchNotation('SniNotation');
    	}else{
    		switchNotation('PonNotation');
    	}
    	
    	$("#sound").attr({
	  			src : '/epon/sound/click3.wav'
	  		}); 
    	preventBubble(e);
    	setBorderStyle(this.id);
        setBorderStyle(this.id.substring(0,7)+"0"); // 修改父板的透明度
        divCache.add(this.id.substring(0,7)+"0");// 父板被修改了，故也要记录
	});
    $port.bind('mouseout',function(e){
	   preventBubble(e);		
	   clearBorderStyle(this.id);	   		  		   
	});
    //端口状态
	var state = null;
	switch(olt.slotList[sID-1].portList[pID-1].portSubType){
		//SNI MASSTYPE
		case 'geCopper'://NO NEED TO HANDLE
		case 'geFiber'://NO NEED TO HANDLE
		case 'xeFiber':
			var os = olt.slotList[sID-1].portList[pID-1].sniOperationStatus;
			var as = olt.slotList[sID-1].portList[pID-1].sniAdminStatus;
			if( as==1 && os==1 ){
				state = 0;
			}else if( as==1 && os==2 ){
				state = 1;
			}else if(as==2 && os==2) {
				state = 2;
			}else {
				state = 2;
			}
			break;
		//PON MASSTYPE
		case 'geEpon'://NO NEED TO HANDLE
		case 'tengeEpon'://NO NEED TO HANDLE
		case 'gpon':
			var os = olt.slotList[sID-1].portList[pID-1].ponOperationStatus;
			var as = olt.slotList[sID-1].portList[pID-1].ponPortAdminStatus;
			if( as==1 && os==1 ){
				state = 0;
			}else if( as==1 && os==2 ){
				state = 1;
			}else if(as==2 && os==2) {
				state = 2;
			}else {
				state = 2;
			}
			break;	
		default:			
			break;	
	}
	
	// 增加该端口对应的灯 
	//目前发现是mgua
	createPortLamp(olt.slotList[sID-1].portList[pID-1]);
	
	switch(state){
		case 0 :
			break;
		case 1 :
			$port.css({
				backgroundImage : 'url(/epon/image/'  + pType + '/'+pType+ '.gif)'
			});
			break;
		case 2 :
			$port.css({
				backgroundImage : 'url(/epon/image/' + pType + '/'+pType+ '.png)'
			});
			$state = $("<img>");
			$state.addClass("portStateClass");
			$state.attr({
				width : 16,
				height : 16,
				title :  I18N.EPON.portnoservice,
				src : '/epon/image/pn8602-ef/close.png'	
			});			
			$state.css({
				position : 'absolute',
				top : 0,
				left : 0,
				zIndex : 9999
			}); 
			$port.append($state);	// 在板卡上添加端口状态
			break;	
	default : 
			break;
	}	
	
	return $port;
} */

function authLoad(){
    if(!refreshDevicePower){
        $("#bfsxOltMirror").attr("disabled",true);
    }
}
</script>
</head>
<body class="newBody overfloatHidden bodyWH100percent" onload="authLoad()">
	<div class="wrapWH100percent overfloatHidden">
		<!--头部菜单开始 -->
		<%@ include file="/epon/inc/navigator.inc"%>
		<!--头部菜单结束 -->
		
		<!-- 左侧开始 -->
		<div class="viewLeftPart" id="viewLeftPart">
			<p class="pannelTit">@EPON.mirrorList@</p>
			<div id="viewLeftPartBody" class="viewLeftPartBody"></div>
		</div>
		<!-- 左侧结束 -->
		<div id="viewLeftLine" class="viewLeftLine"></div>
		
		<!-- 中间部分开始 -->
		<div id="viewMiddle" class="viewMiddle">
			<dd style="margin:5px;">
				<a href="javascript:;" id="bfsxOltMirror" class="normalBtn" onclick="bfsxOltMirror();"><span><i class="miniIcoRefresh"></i>@COMMON.refresh@</span></a>
			</dd>
			<div class="middlePartPutPic" id="device_container" style="width: 600px;"></div>
			
			<!-- 中间底部设备说明开始 -->
			<div id="viewMiddleBottom" class="viewMiddleBottom threeFeBg">
				<div id="viewMiddleBottomLine" class="horizontalLine displayNone" style="height:7px; cursor:n-resize">
					<div class="dragDot"></div>
				</div>
				<p id="viewMiddleBottomTit" class="pannelTitWithTopLine">@EPON.oltMirrorDesc@<a href="javascript:;" class="pannelTilArrUp"></a></p>
				<div id="viewMiddleBottomBody" class="displayNone">
					<img src='@ICON.mirror@' />
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
			<div id="viewRightPartBottomBody" class="rightPannel propertyBg"></div>
			<!-- 右侧下半部分结束 -->
			
		</div>
		<!-- 右侧部分结束 -->
	</div>	
	<script type="text/javascript" src="/js/jquery/jquery.wresize.js"></script>
	<script type="text/javascript" src="/js/jquery/Nm3kTabBtn.js"></script>
	<script type="text/javascript" src="/js/jquery/dragMiddle.js"></script>
	<script type="text/javascript">
	//加入右键提示信息;
	$(function(){
		top.nm3kRightClickTips({
			title: "@COMMON.tip@",
			html: "@COMMON.MirrorTip@"
		});
	});//end document.ready;
	var nm3k = {};//用来记录拖动元素，改变布局的时候，方便销毁拖动功能;
	nm3k.rightTopHeight = 280;//记录右侧上部的高度;
	nm3k.middleBottomHeight = 100;//记录中间底部设备说明的高度;
	nm3k.leftWidth = 180;//左侧宽度;
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
	<script type="text/javascript" src="/js/jquery/nm3kViewLayout.js"></script>
	<script type="text/javascript">

		/*
		 * override
		 * 拖拽左侧后，保存布局，设置左侧gird的宽度;
		 */
		function saveLayout(){
			var $line = $("#viewLeftLine"),
			    leftPos = $line.offset().left;
			mirror_grid.setWidth(10);
			window.setTimeout(function(){
				mirror_grid.setWidth(leftPos);
				saveEponViewLayout();	
			}, 100);
			
		}
	</script>
</body>
</Zeta:HTML>