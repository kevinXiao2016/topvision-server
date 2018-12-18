<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	plugin portlet
    module resources
    IMPORT js/raphael/raphael
    IMPORT js/topN
    IMPORT workbench/myDeskTop
</Zeta:Loader>
<style type="text/css">
.alignCenter {float:center;}
.plusWrap{ width:100%; height:140px; background:#F9F9F9; overflow:hidden; position:relative; cursor:pointer; border:1px dashed #ccc;}
#paperDiv, .topPaper{ width:1200px; height:200px; position:absolute; top:0px; left:0px; z-index:1;}
.topPaper{ z-index:2;}
.loading-indicator{ background-position: left top;}
</style>
<script type="text/javascript">
// for tab changed start
function tabActivate() {
	doRefresh();
	startTimer();
}
function tabDeactivate() {
	doOnunload();
}
function tabRemoved() {
	doOnunload();
}
function tabShown() {
	startTimer();
}
// for tab changed end

var timer = null;
var dispatchTotal = 60000;
var dispatchInterval = 5000;
var syncount = 0;
var synMax = 3;
function doRefresh() {
	for (var i = 0; i < portlets.length; i++) {
		if (i % synMax == syncount) {
			//这样做为了使统计分布图不自动刷新,频繁的刷新没有意义，并会导致界面自动弹出，不友好
			/* var mgr = portlets[i].getUpdater();
			mgr.update({disableCaching: true, url: urls[i]}); */
			var mgr = portlets[i].tools.refresh;
			mgr.dom.click();
		}
	}
	syncount++;
	if (syncount >= synMax) {
		syncount = 0;
	}
}

function startTimer() {
	if (timer == null) {
		if (portlets.length > 12) {
			synMax = 4;
		}
		var syn = parseInt(portlets.length / synMax) + 1;
		dispatchInterval = parseInt(dispatchTotal/syn);	
    	timer = setInterval("doRefresh()", dispatchInterval);
    }
}

function doOnunload() {
	if (timer != null) {
		clearInterval(timer);
		timer = null;
		count = 0;
	}
}

// custom js
function showLinkSnap(linkId) {
}
function setMyPortlet() {
	window.top.createDialog('modalDlg', I18N.MAIN.customMyDesktop, 800, 500, 'portal/showPopPortletItems.tv', null, true, true);
}
function modifyPersonalInfo() {
    window.top.modifyPersonalInfoClick();
}
function setPersonalize() {
	window.top.showPersonalize();
}
function viewLinkFlow(linkId) {
}
function viewPortFlow(entityId, entityIp, ifIndex) {
		window.open('../realtime/showPortFlowInfo.tv?entityId=' + entityId +
				'&ip=' + entityIp + '&ifIndex=' + ifIndex, 'realtime' + entityId);
}

function showEntityByState(index, key) {
}

function showEntitySnap(id, name) {
	window.top.addView("entity-" + id, name , 'entityTabIcon',
		"portal/showEntitySnapJsp.tv?module=1&entityId=" + id);
}

function showServerSnap(id, name) {
	window.top.addView("entity-" + id,  name , 'entityTabIcon',
		"portal/showEntitySnapJsp.tv?module=1&entityId=" + id);
}

function updatePortletPosition(id, row, col){
	Ext.Ajax.request({url: '../portal/updatePortletPosition.tv', method:'POST', params:{itemId:id, row:row, col:col},
	   success: function(response) {},
	   failure: function(response) {}
	});
}

// for entity category stat portlet
function showEntityByType(type, name) {
	//window.top.addView('entityType-' + type, type, 
		//'entityTabIcon', 'entity/showEntityByTypeJsp.tv?typeId=' + type);
	window.parent.addView('entityType-' + type, name, 'entityTabIcon', 'entity/showEntityByTypeJsp.tv?typeId=' + type);
}

// for important entity portlet
var inputIpStr = I18N.WorkBench.inputIpString;
function addImportantEntity() {
	//alert(Zeta$('ipInput').value);
}
function keydownImportantIp(box) {
	if (event.keyCode == KeyEvent.VK_ENTER) {
		addImportantEntity();
	}
}
function focusImportantIp(box) {
	if (box.value == inputIpStr) {
		box.value = '';
	}
	box.style.color = 'black';
}
function blurImportantIp(box) {
	box.style.color = 'gray';
	if (box.value == '') {
		box.value = inputIpStr;
	}
}

// for entity topo map
function selectEntityTopology() {
}
function showTopoMap(name, id) {
	window.parent.addView("topo-" + 2, I18N.WorkBench.networkTopo, "topoLeafIcon", "network/getTopoMapByFolderId.tv?folderId=" + 2);
}

function showServerByAvailable(index, key) {
}

function viewAlertByIp(name,ip,id,type) {
	var oltType = EntityType.getOltType();
	var cmcType = EntityType.getCcmtsType();
	var cmtsType = EntityType.getCmtsType();
	var title = name;
	if(!name){
		title = id;
	}
	if(EntityType.isOltType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"alert/showEntityAlertJsp.tv?module=5&entityId=" + id,null,true);
	}
	if(EntityType.isCcmtsType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"/cmc/alert/showCmcAlert.tv?module=12&cmcId=" + id,null,true);
	}
	if(EntityType.isOnuType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"/onu/showOnuAlert.tv?module=3&onuId="+id,null,true);
	}
	if(EntityType.isOnuType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"/onu/showOnuPerf.tv?module=4&onuId="+id,null,true);
	}
}

function viewAlertByName(name,id,type) {
	var title = name;
	if(!name){
		title = id;
	}
	var oltType = EntityType.getOltType();
	var cmcType = EntityType.getCcmtsType();
	var cmtsType = EntityType.getCmtsType();
	//showEntitySnap(id,title)
	if(EntityType.isOltType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"alert/showEntityAlertJsp.tv?module=5&entityId=" + id,null,true);
	}
	if(EntityType.isCcmtsType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"/cmc/alert/showCmcAlert.tv?module=12&cmcId=" + id,null,true);
	}
	if(EntityType.isCmtsType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"/cmts/alert/showCmtsAlert.tv?module=7&cmcId="+id+"&productType=" + cmtsType ,null,true);
	}
	if(EntityType.isOnuType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"/onu/showOnuAlert.tv?module=3&onuId="+id,null,true);
	}
}

function viewAlertByAlert(name,id,type) {
    var oltType = EntityType.getOltType();
    var cmcType = EntityType.getCcmtsType();
    var cmtsType = EntityType.getCmtsType();
    var onuType = EntityType.getOnuType();
    var title = name;
    if(!name){
        title = id;
    }
    if(EntityType.isOltType(type)){
        window.top.addView("entity-" + id,  title , 'entityTabIcon',
                "alert/showEntityAlertJsp.tv?module=5&entityId=" + id,null,true);
    }
    if(EntityType.isCcmtsType(type)){
        window.top.addView("entity-" + id, title , 'entityTabIcon',
                "/cmc/alert/showCmcAlert.tv?module=12&cmcId=" + id,null,true);
    }
    if(EntityType.isCmtsType(type)){
        window.top.addView("entity-" + id, title, 'entityTabIcon',
                "/cmts/alert/showCmtsAlert.tv?module=7&cmcId="+id+"&productType=" + cmtsType ,null,true);
    }
    if(EntityType.isOnuType(type)){
        var onuType = EntityType.getOnuType();
        window.top.addView("entity-" + id, title, 'entityTabIcon',
                "/onu/showOnuAlert.tv?module=3&onuId="+id ,null,true);
    }
}
var viewport = null;
var portlets = [];
var urls = [];
Ext.onReady(function(){
	Ext.Updater.defaults.disableCaching = true;

 	var columns = 2;
 	var columnWidth = (columns == 3 ? .33 : (columns == 2 ? .5 : 1));
 	/* var size = <s:property value="portletItems.size"/> ;//增加加号后这个部分还是有一定的作用;
 	if (columns > size) {
 		columns = size;
 	} */
 	
 	var portletItems = [];
 	for (var i = 0; i < columns; i++) { 		
 		if(columns == 2){
 			if(i%2 == 0){
 	 			portletItems[i] =  {columnWidth: columnWidth, style: 'padding:10px 0px 10px 10px', items: []};
 	 		}else{
 	 			portletItems[i] =  {columnWidth: columnWidth, style: 'padding:10px 10px 10px 10px', items: []};
 	 		}
 		}else{
 			portletItems[i] =  {columnWidth: columnWidth, style: 'padding:10px 0px 10px 10px', items: []};
 		};//end if; 		
 	}
 	portletItems[1] =  {columnWidth: columnWidth, style: 'padding:10px 0px 10px 10px', items: []};
 	//左侧板块，从数据库读取;
	var leftPartStr = '${desktopLeft}';
	//右侧板块，从数据库读取;
	var rightPartStr = '${desktopRight}';
	//如果是第一次加载,就进行默认初始化
	if(leftPartStr == '' && rightPartStr == ''){
	 	leftPartStr = "201,202,406,407,408,409,416,417,plus";
	 	rightPartStr = "420,421,422,423,424,425,426,427,428,901,902,904";
	}
	
	var panel = null;
 	var count = rowIndex = 0;
 	
 	var allPart = [];
 	var leftPart = [];
 	var rightPart = [];
 	
 	var isSupportSVG = Raphael.svg;//是否支持svg.对于不支持的低版本浏览器，加号没有动画;
 	var htmlStr = '';
 	if( isSupportSVG ){//支持svg,后面还要加动画;
 		htmlStr = '<div class="plusWrap" id="plusWrap" onclick="top.customDesktopClick()"><div id="paperDiv"></div><div class="topPaper"></div></div>';
 	}else{//不支持svg;
 		htmlStr = '<div id="deskTopPlus" class="deskTopPlus" onclick="top.customDesktopClick()"><div class="plusIcon"></div><p class="txtCenter cccGrayTxt">@MenuItem.customDesktopClick@</p></div>';
 		$("#deskTopPlus").live("mouseover",function(){
 	 		$me = $(this);
 	 		$me.css({background: "#ffffff"});
 	 		$me.find(".plusIcon").addClass("plusIconHover");
 	 		$me.find("p").attr({"class" : "txtCenter orangeTxt"});
 	 	}).live("mouseout",function(){
 	 		$me = $(this);
 	 		$me.css({background: "transparent"});
 	 		$me.find(".plusIcon").removeClass("plusIconHover");
 	 		$me.find("p").attr({"class" : "txtCenter cccGrayTxt"});
 	 	})
 	}
 	
 	var addPanel = new Ext.ux.Portlet({		
		id:'plus',
	    tools: [],
	    style: { align: 'center' },
	    bodyStyle: 'padding:10px',
	    autoHeight : true,
        title: '@MenuItem.customDesktopClick@',
		html: htmlStr
	});
	
 	function addViewFunc(itemId){
 		if(itemId == 406){
 			window.top.addView("delayMore", '@network/NETWORK.entityDelay@', "apListIcon", "/portal/showDeviceDelayJsp.tv");
 		}
 		if(itemId == 407){
 			window.top.addView("cpuUsed", '@network/NETWORK.cpuUsed@', "apListIcon", "/portal/showDeviceCpuJsp.tv");
 		}
 		if(itemId == 408){
 			window.top.addView("memUsed", '@network/NETWORK.memUsed@', "apListIcon", "/portal/showDeviceMemJsp.tv");
 		}
 		if(itemId == 409){
 			window.top.addView("delayOut", '@network/NETWORK.entityDelayOut@', "apListIcon", "/portal/showDeviceDelayOutJsp.tv");
 		}
 		if(itemId == 416){
 			window.top.addView("sniSpeedList", '@network/NETWORK.sniPortSpeed@', "apListIcon", "/epon/perf/showSniPortSpeed.tv");
 		}
 		if(itemId == 417){
 			window.top.addView("ponSpeedList", '@network/NETWORK.ponPortSpeed@', "apListIcon", "/epon/perf/showPonPortSpeed.tv");
 		}
 		if(itemId == 420){
 			window.top.addView("CcmtsMoreNoise", '@network/CcmtsUpNoise@', "apListIcon", "/cmc/loadNoiseRate.jsp");
 		}
 		if(itemId == 421){
 			window.top.addView("channelUsed", '@network/NETWORK.channelUsed@', "apListIcon", "/cmcperf/getMoreChannelUsed.tv");
 		}
 		if(itemId == 422){
 			 window.top.addView("CcmtsMoreBer", '@network/CcmtsUpBer@', "apListIcon", "/cmcperf/showCmtsBerRate.tv");
 		}
 		if(itemId == 423){
 			 window.top.addView("CcmtsMoreFlap", '@network/CcmtsCmFlap@', "apListIcon", "/cmcperf/showCmFlapIns.tv");
 		}
 		if(itemId == 424){
 			window.top.addView("cmcMoreOptical", '@network/DEVICEVIEW.cmcOptical@', "apListIcon", "/cmcperf/showCmcOpticalInfo.tv");
 		}
 		if(itemId == 425){
 			 window.top.addView("CcmtsMoreUser", '@network/CcmtsUser@', "apListIcon", "/cmcperf/showCmtsUser.tv");
 		}
 		if(itemId == 426){
 			 window.top.addView("CcmtsUpChannelUser", '@network/CcmtsUpChannelUser@', "apListIcon", "/cmcperf/showCmtsUpChannelUser.tv");
 		}
 		if(itemId == 427){
 			window.top.addView("CcmtsDownChannelUser", '@network/CcmtsDownChannelUser@', "apListIcon", "/cmcperf/showCmtsDownChannelUser.tv");
 		}
 		if(itemId == 428){
 			window.top.addView("cmcMoreOpticalTemp", '@network/DEVICEVIEW.cmcOpticalTemp@', "apListIcon", "/cmcperf/showCmcOpticalTempInfo.tv");
 		}
 		if(itemId == 902){
 			window.top.addView("deviceAlertMore", '@WorkBench.deviceAlertNum@', "apListIcon", "/portal/showDeviceAlertList.tv");
 		}
 		if(itemId == 904){
 			window.top.addView("deviceAttentionMore", '@WorkBench.deviceAttention@', "apListIcon", "/portal/showDeviceAttentionList.tv");
 		}
 	}
 	
 	function supportLeftOrRighy(paraId,paraPanel){//判断板块在左侧还是在右侧;
 		var leftPartStrArr = leftPartStr.split(",");
		for(var i=0; i<leftPartStrArr.length; i++){
			if(leftPartStrArr[i] == paraId){
				leftPart[i] = paraPanel;
			}
		 	
		}
		var rightPartStrArr = rightPartStr.split(",");
		for(var i=0; i<rightPartStrArr.length; i++){
			if(rightPartStrArr[i] == paraId){
				rightPart[i] = paraPanel;
			}
		}
	}
 	
	<s:iterator value="portletItems">
		urls[urls.length] = '${url}?portletId=${itemId}&param=${param}';
		<s:if test="type==1">
			var item =[];
			<s:if test="itemId!=201">
				item.push({
			        id: 'gear',
			        qtip:'@COMMON.more@',
		            handler: function(event, toolEl, panel){
		            	addViewFunc(${itemId});
		            }
		        })
			</s:if>
			item.push({id: 'refresh',
			        qtip: I18N.RECYLE.refresh,
			        handler: function(event, toolEl, panel){
			        	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
			        	var h = panel.getInnerHeight()-20,
			        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@');
			        	
			        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : '${url}?portletId=${itemId}&param=${param}&ignoreTimeout',callback:function(){
			        		zebraTable();
			        	}});
			        }
			    })
			panel = new Ext.ux.Portlet({		
				id:'${itemId}',
			    tools: item,
			    style: { align: 'center' },
			    bodyStyle: 'padding:10px',
			    autoHeight : true,
		        title: '${name}',
				autoLoad:{disableCaching: true, url: '${url}?portletId=${itemId}&param=${param}', text:I18N.WorkBench.loading}
			});
			//portlets[portlets.length] = panel;
			supportLeftOrRighy('${itemId}',panel);
		</s:if>
	    <s:else>
			panel = new Ext.ux.Portlet({
				id:'${itemId}',
			    tools: [{id: 'refresh',
			        qtip: I18N.RECYLE.refresh,
			        handler: function(event, toolEl, panel){
			            ZetaUtils.getIframe('portlet<s:property value="itemId" />').redraw();
			            return false;
			        }
			    }],
			    style: { align: 'center'},
			    autoHeight : true,
		        title: '${name}',
		        html:"<iframe width=100% class=alignCenter id='portlet${itemId}' name='portlet${itemId}' height=330 frameborder=0 src='${url}'></iframe>"
		    });
			allPart.push('${itemId}');
			supportLeftOrRighy('${itemId}',panel);
		</s:else>
		//portletItems[rowIndex].items[portletItems[rowIndex].items.length] = panel;
	 </s:iterator> 
	 //rightPart.unshift(addPanel);
	 supportLeftOrRighy("plus",addPanel);
	 
 	for(var i=0;i<leftPart.length;i++){
 		if(leftPart[i] != undefined){
 			portletItems[0].items.push(leftPart[i]);
 		}
 	}
 	for(var i=0;i<rightPart.length;i++){
 		if(rightPart[i] != undefined){
 			portletItems[1].items.push(rightPart[i]);
 		}
 	}
 	
 	//通过判断左右两侧id的顺序，判断布局是否改变，如果改变，那么保存布局(要求id和变量名一致);
	$(".x-panel-tl").live("mouseup",function(){
		 setTimeout(saveLayout,200);
		});//end live; 
	function saveLayout(){
		var partsArr = [201,202,406,407,408,409,416,417,420,421,422,423,424,425,426,427,428,901,902,904,"plus"];//所有版块的id;
		var partsArrLen = partsArr.length;
		var halfLen = partsArr.length / 2;
		
		var leftArr = [];
		var rightArr = [];
		var allArr = [];//记录左侧加右侧的数组;
		$(".x-portal-column").eq(0).find(".x-portlet").each(function(){
			leftArr.push($(this).attr("id"));
			allArr.push($(this).attr("id"));
		})
		$(".x-portal-column").eq(1).find(".x-portlet").each(function(){
			rightArr.push($(this).attr("id"));
			allArr.push($(this).attr("id"));
		})
		if(leftArr.length + rightArr.length < partsArrLen){
			//共有n个板块，剩下几个板块没有添加;
			var rest = partsArrLen - leftArr.length - rightArr.length;
			//对比2个数组中的不同，记录没有得到的板块id数组;
			var diffrentArr = partsArr;
			var arr3 = []
			partsArr.sort();
			allArr.sort();
			for(var i=0;i<partsArr.length;i++){
			    for(var j=0;j<allArr.length;j++){
			        if(partsArr[i] == allArr[j]){
			        	diffrentArr.splice(i,1);
			        }
			    }
			}
			for(var k=0; k<diffrentArr.length; k++){
				if(leftArr.length < halfLen){//左边不足一半，先加左边;
					leftArr.push(diffrentArr[k]);
				}else{
					rightArr.push(diffrentArr[k]);
				}	
			}
		};//end if;
		if(leftPartStr == leftArr.toString() && rightPartStr == rightArr.toString()){//没有变化;

		}else{//有变化;
			leftPartStr = leftArr.toString();
			rightPartStr = rightArr.toString();
			$.ajax({
				url: '/portal/saveDesktopView.tv',
				cache:false, 
				method:'post',
				data: {
					desktopLeft : leftPartStr, 
					desktopRight : rightPartStr
				},
				success: function() {
				},
				error: function(){
				}
			});
		};//end if else;
	};//end saveLayout;
 	
	if(portletItems[0].items.length == 0 && portletItems[1].items.length == 1){//只有加号的情况;
		portletItems[0].items.push(addPanel);
		portletItems.length = 1;
	}
    viewport = new Ext.Viewport({layout: 'border',
        items:[{id: 'portal', xtype: 'portal',  region: 'center', margins: '0 0 0 0', 
        	border: false, items: portletItems}]
    });
	tabShown();
	
	if( isSupportSVG ){//支持svg,增加加号动画;
		fPaperToMiddle();
		$(window).resize(function(){
			throttle(fPaperToMiddle, window)
		})
		//创建paper;
		svgObj.paper =	Raphael(document.getElementById("paperDiv"), 1200, 200);
		fAddPlus();
		$("#plusWrap").live("mouseover",function(){
			if(svgObj.isMoving){
				return;
			}else{
				fHoverPlus();
			}
		}).live("mouseout",function(){
			fOutPlus();
		})
	}
	
});

</script>
<script type='text/javascript'  src='../js/jquery/zebra.js'></script>
</head><body class="newBody" onunload="doOnunload();">
</body></Zeta:HTML>
