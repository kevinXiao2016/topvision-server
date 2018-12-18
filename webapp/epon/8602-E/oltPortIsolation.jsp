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
    IMPORT epon.8602-E.oltCreationLib
    IMPORT epon.javascript.EponViewUtil
    IMPORT epon.javascript.SlotConstant
    IMPORT epon.javascript.IsolationGroupHelper
    IMPORT epon.js.oltFaceplateTrap
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
v\:oval { behavior: url(#default#VML);display:inline-block } 
.X-BOARD-TYPE{font-weight:bold;position:absolute;left:20px;top:5px;z-index=5000;}
.abAndTR110{ position: absolute; top:10px; right:110px; z-index:21;}
</style>
<script type="text/javascript" src="/js/jquery/jquery.wresize.js"></script>
<script type="text/javascript" src="/js/jquery/dragMiddle.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kViewLayout.js"></script>
<script type="text/javascript">
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var entityId = ${entity.entityId};
var json;
var ISO_GROUP_LIST;
var ISO_GROUP_STORE;
var PROPERTY_GRID;
var PROP_STORE;
var olt = new Olt();
var currentId = EMPTY; //记录当前点击的divID，解析ID得到slot，pon口传给弹出页面
var divCache = new Array();
var divCache2 = new Array();
var SINGLE_SELECT_MODE = true;

//加入右键提示信息;
$(function(){
	top.nm3kRightClickTips({
		title: "@COMMON.tip@",
		html: "@ISOGROUP.isolationTip@"
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
/*///////////////在这个部分读取后台的数据改变上面的默认值///////////////////////////////*/
if( ${layout.leftWidth} ){
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
/*
 * override
 * 拖拽左侧后，保存布局，设置左侧gird的宽度;
 */
function saveLayout(){
	var $line = $("#viewLeftLine"),
	    leftPos = $line.offset().left;
	
	window.setTimeout(function(){
		saveEponViewLayout();
	}, 100);
}

/**
 * 初始化页面
 */
function init() {    
    $.ajax({
        url:"../epon/oltObjectCreate.tv?entityId=" + entityId,
        dataType:"json",
        success:function (json) {
            olt.init( json );
            //------创建面板图并移除所有监听 -----//
            createPN8602E(olt);
            cleanPorts();
            /** 加载隔离组数据信息 **/
            create_iso_group_grid();
            addListeners();
        },
        error:function () {
            window.parent.showMessageDlg(I18N.COMMON.tip , I18N.EPON.loadOltError);
        }
        
    })
}

$(DOC).ready(function(){
	 init();
	 var tab1 = new Nm3kTabBtn({
		    renderTo:"putSingleSel",
		    callBack:"tabSelFn",
		    tabs:["@ISOGROUP.singleSelect@","@ISOGROUP.multiSelect@"]
		});
		tab1.init();
});
function tabSelFn(num){
	if(num == 0){
		SINGLE_SELECT_MODE = true;
	} else {
		SINGLE_SELECT_MODE = false;
	} 
}

function authLoad(){
    if(!refreshDevicePower){
        $("#refreshClick").attr("disabled",true);
    }
}
/*
 * override
 * 拖拽左侧后，保存布局，设置左侧gird的宽度;
 */
function saveLayout(){
	var $line = $("#viewLeftLine"),
	    leftPos = $line.offset().left;
	ISO_GROUP_GRID.setWidth(10);
	window.setTimeout(function(){
		ISO_GROUP_GRID.setWidth(leftPos);
		saveEponViewLayout();	
	}, 100);
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
			<p class="pannelTit">@ISOGROUP.groupList@</p>
			<div id="viewLeftPartBody" class="viewLeftPartBody"></div>
		</div>
		<!-- 左侧结束 -->
		<div id="viewLeftLine" class="viewLeftLine"></div>
		
		<!-- 中间部分开始 -->
		<div id="viewMiddle" class="viewMiddle">
			<dd style="margin:5px;">
				<a href="javascript:;" id="refreshClick" class="normalBtn" onclick="refreshClick()"><span><i class="miniIcoRefresh"></i>@COMMON.refresh@</span></a>
			</dd>
			<div class="middlePartPutPic" id="device_container" style="margin: 5px;width: 700px;"></div>
			
			<!-- 中间底部设备说明开始 -->
			<div id="viewMiddleBottom" class="viewMiddleBottom threeFeBg">
				<div id="viewMiddleBottomLine" class="horizontalLine displayNone" style="height:7px; cursor:n-resize">
					<div class="dragDot"></div>
				</div>
				<p id="viewMiddleBottomTit" class="pannelTitWithTopLine">@COMMON.desc@<a href="javascript:;" class="pannelTilArrUp"></a></p>
				<div id="viewMiddleBottomBody" class="displayNone">
					<img src='@ISOGROUP.descIco@' style="padding : 10px;"/>
				</div>
			</div>
			<!-- 中间底部设备说明结束 -->
			
			<!-- 切换布局按钮开始 -->
			<div class="abAndTR10" id="putTabBtn"></div>
			<!-- 切换布局按钮结束 -->
			<div class="abAndTR110" id="putSingleSel"></div>
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
	
</body>
</Zeta:HTML>