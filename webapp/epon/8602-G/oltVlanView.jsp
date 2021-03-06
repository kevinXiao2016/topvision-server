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
    IMPORT epon.javascript.SlotConstant
    IMPORT epon.javascript.EponVlanHelper
    IMPORT epon.oltViewMenuLib
    IMPORT js.ext.ux.PagingMemoryProxy
    IMPORT epon.js.oltFaceplateTrap
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
v\:oval { behavior: url(#default#VML);display:inline-block } 
.X-BOARD-TYPE{font-weight:bold;position:absolute;left:20px;top:5px;z-index=5000;}
</style>
<script type="text/javascript">
var floodMode = ["","always flooding","unknown flooding","no flooding"];
var json;
var vlanListJson;
var oltVlanGlobalInfo;
var grid;
var olt = new Olt;
var noSelected = false;
var entityId = '${entity.entityId}';
var image_state = new Array(10);
var CASCADE_LOCK = false ;
var ctrlFlag = 0;
var divCache = new Array();
var divCache2 = new Array();
var indexCache = new Array();
var attrStore;
var OltTree;
var enableId = "";   //反键变更的itemId
var currentId = ""; //记录当前点击的divID，解析ID得到slot，pon口传给弹出页面
var vlan_attr_grid;//vlan属性表格
var vlan_attrStore;//vlan属性
var vlan_grid;//vlan列表
var vlan_menu;
var vlan_menu1;
var modifyFlag = false;
var rowSelected;
var modeFlag = 1;//标志vlan视图是出于图形模式（1）下，还是text模式（2）下
// 计时器(秒)
var timer = 0;
// 采集间隔(秒)
var vlanSelected = "";//标识选中的vlan
var vlanMenu;
var isDeletingNum = 0;
var tmpDeleteNum = 0;
var delWaitingDlg;
var selectedVlanIndex;
var bigControlFlag = 0;
var oltSoftVersion = '${oltSoftVersion}';
var cameraSwitch = '${cameraSwitch}';
var pageSize = 25;

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
	createPN8602G()
	$("#OLT, .portClass").unbind("click contextmenu mouseover mouseout");
	$(".portClass").bind("click", contextHandler).bind('contextmenu', contextHandler);
}

var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
function authLoad(){
    if(!refreshDevicePower){
        $("#bfsxVlan").attr("disabled",true);
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
			<p class="pannelTit">@EPON.vlanList@</p>
			<div id="viewLeftPartBody" class="viewLeftPartBody"></div>
		</div>
		<!-- 左侧结束 -->
		<div id="viewLeftLine" class="viewLeftLine"></div>
		
		<!-- 中间部分开始 -->
		<div id="viewMiddle" class="viewMiddle">
			<dd style="margin:5px;">
				<a href="javascript:;" id="bfsxVlan" class="normalBtn" onclick="bfsxVlan();"><span><i class="miniIcoRefresh"></i>@COMMON.refresh@</span></a>
			</dd>
			<div class="middlePartPutPic" id="device_container" style="width:600px;" >
					<!-- <div id="device_container" style="position: absolute; width: 490; height: 392; top: 20px; left: 5px; z-index: 7000"></div> -->
			</div>
			<div id="putImageBtnAndTxtBtn" style="position:absolute; top:10px; right:110px; z-index:99;"></div>
			<div  class="putTextModeContainer" id='device_text' style="display:none;"></div>
			<!-- 中间底部设备说明开始 -->
			<div id="viewMiddleBottom" class="viewMiddleBottom threeFeBg">
				<div id="viewMiddleBottomLine" class="horizontalLine displayNone" style="height:7px; cursor:n-resize">
					<div class="dragDot"></div>
				</div>
				<p id="viewMiddleBottomTit" class="pannelTitWithTopLine">@EPON.vlanDesc@<a href="javascript:;" class="pannelTilArrUp"></a></p>
				<div id="viewMiddleBottomBody" class="displayNone">
					<table>
						<tr><td colspan="6"><img src="/epon/image/notation.png" /></td></tr>
						<tr>
							<td class="rightBlueTxt w100"><label>@VLAN.maxVlanId@:</label></td>
							<td ><input size=6 type="text" id="maxVlanId" style="border: 0" readonly/></td>
							<td class="rightBlueTxt w100"><label>@VLAN.maxVlanNum@:</label></td>
							<td ><input type="text" size=6 id="maxSupportVlans" style="border: 0" readonly/></td>
							<td class="rightBlueTxt w100"><label>@VLAN.curVlanNum@:</label></td>
							<td><input type="text" size=6 id="createdVlanNumber" style="border: 0" readonly/></td>
						</tr>
					</table>
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
	<script type="text/javascript" src="/js/jquery/dragMiddle.js"></script>
	<script type="text/javascript">
	var tab1 = new Nm3kTabBtn({
	    renderTo:"putImageBtnAndTxtBtn",
	    callBack:"changeImageOrTxt",
	    tabs:["@VLAN.imgMode@","@VLAN.textMode@"]
	});
	tab1.init();
	function changeImageOrTxt(num){
	    switch(num){
	    	case 0:
	    	    window.changeToGraph();
	    	    setHeight();
	        	break;
	    	case 1:
	    	    window.changeToText();
	    	    setHeight();
		        break;
	    }
	}
	//加入右键提示信息;
	$(function(){
		top.nm3kRightClickTips({
			title: "@COMMON.tip@",
			html: "@COMMON.vlanTip@"
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
	<div style='display:none;padding:15px;' id=delWaitingDlgDiv align=center>
		<span>@VLAN.dele@@COMMON.maohao@</span>
		<span id=progressFont style='width:110px;color:blue;text-align:left;'>0%</span>
		<span id=progressSpan1 style='border:1px solid #8bb8f3;width:1px;height:20px;background-color:#8bb8f3;'></span><span 
			id=progressSpan2 style='border:1px solid #8bb8f3;width:199px;height:20px;'></span>
		<button class=BUTTON75 onclick='closeDelWaitingDlg()'>@COMMON.close@</button>
	</div>
	<script type="text/javascript" src="/js/jquery/nm3kViewLayout.js"></script>
	<script type="text/javascript">
		/*
		 * override
		 * 拖拽左侧后，保存布局，设置左侧gird的宽度;
		 */
		function saveLayout(){
			var $line = $("#viewLeftLine"),
			    leftPos = $line.offset().left;
			vlan_grid.setWidth(10);
			window.setTimeout(function(){
				vlan_grid.setWidth(leftPos);
				saveEponViewLayout();	
			}, 100);
			
		}
	/*
	** override 
	** 注意，拖拽中间底部需要改变“文本模式”的高度，所以这里比较特殊，因此override这个回调函数;
	*/
	//拖拽中间的回调函数;
 	function middlePartDragYFn(paraTopPos){
 		var h = $(window).height();//总体高度;
 		var h1 = $(".ultab").outerHeight();//菜单的高度;
 		var h2 = $("#viewMiddleBottomLine").outerHeight();//中间拖拽条高度;
 		var h3 = $("#viewMiddleBottomTit").outerHeight();
 		var h4 = h-h2-h3-paraTopPos;
 		if(h4 > 0){
 			$("#viewMiddleBottomBody").height(h4);
 			nm3k.middleBottomHeight = h4;
 		}
 		setHeight();
 		saveEponViewLayout();//拖拽后自动保存布局;
 	};//end middlePartDragYFn;
 	function setHeight(){
 		var top = ($("#viewMiddleBottomLine").is(":visible")) ?  $("#viewMiddleBottom").offset().top - 37 : $("#viewMiddleBottomTit").offset().top - 37;
 		if(top <= 20){ top = 20;}
 		$("#device_text").height(top);
 	}
 	$(window).resize(function(){
 		throttle(setHeight, window);
 	});
	function throttle(method, context){
		clearTimeout(method.tId);
		method.tId = setTimeout(function(){
			method.call(context);
		},100);
	}
	throttle(setHeight, window);
	/*
	** override
	** 拖拽后，需要更新vlan列表的宽度，并且要保存当前布局;
	*/
	function saveLayout(){
		var $line = $("#viewLeftLine"),
		    leftPos = $line.offset().left;
			vlan_grid.setWidth(10);
		setTimeout(function(){
			vlan_grid.setWidth(leftPos);
			//去后台保存布局;
			saveEponViewLayout();
		},100);
	}
	$("#viewMiddleBottomTit a").click(function(){
		setInterval(function(){
			setHeight();
		},100);
	})
	</script>
	<bgsound id="sound" autostart=true loop=false />
</body>
</Zeta:HTML>