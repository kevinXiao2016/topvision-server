function NavigateMap(para){
	switch(para){
	case "zoomIn":
		mapZoomLevel++;
		if(mapZoomLevel >= 3){
			mapZoomLevel = 3;
		}
		break;
	case "zoomOut":
		mapZoomLevel--;
		if(mapZoomLevel <= 1){
			mapZoomLevel = 1;
		}
		break;
	};
	$.ajax({
		url: '/topology/saveToolView.tv', 
		cache:false, 
		method:'post',
		data: {
			zoomValue : mapZoomLevel
		},
		success: function() {
		},
		error: function(){
		}
	});  
	zoomMap(mapZoomLevel);
};
//放大缩小地图(1,2,3);
function zoomMap(size){
	var scaleNum = 1;
	var cssName = "scale_l";
	var barTop = 22;
	switch(size){
		case 3:
			scaleNum = 1;
			cssName = "scale_l";
			barTop = 22;
			break;
		case 2:
			scaleNum = 0.8;
			cssName = "scale_m";
			barTop = 47;
			break;
		case 1:
			scaleNum = 0.5;
			cssName = "scale_s";
			barTop = 72;
			break;
	}
	topoPic.scale = scaleNum;
	$("#icoWrap").removeClass("scale_l").removeClass("scale_m").removeClass("scale_s").addClass(cssName);
	var w = folderWidth / scaleNum,
	h = folderHeight / scaleNum;
	$("#icoWrap").width(w).height(h);
	topoPic.paper.setSize(w,h);
	$("#navigateBar").stop().animate({top:barTop});
}
function initZoomBar(){
	var bro=$.browser;
	if(!Raphael.svg || bro.msie){return;}
	//如果支持svg,则载入放大缩小功能。
	var zoomStr = '';
	zoomStr += '<div class="navigateBarBg">';
	zoomStr +=     '<div id="navigateBar" class="navigateBar"></div>';
	zoomStr +=     '<a href="javascript:;" class="zoomInBtn" onclick="NavigateMap(\'zoomIn\')"></a>';
	zoomStr +=     '<a href="javascript:;" class="zoomOutBtn" onclick="NavigateMap(\'zoomOut\')"></a>';
	zoomStr += '</div>';
	$("body").append(zoomStr);
}
$(function(){
	if(topoToolView == "show"){
		$("#showTipTxt").addClass("normalBtnIcoSelect");
		$("#topPart li a").each(function(){
			var $me = $(this);
			var txt = $me.attr("name").split("|")[0];
			$me.find("i").after(txt);
		});
	}
	$("#icoWrap").css({width:folderWidth, height:folderHeight});
	var repeat;
	switch(Number(backgroundPosition)){
		case 0:
			repeat = "no-repeat";
			break;
		case 1:
			repeat = "no-repeat center center";
			break;
		case 2:
			repeat = "repeat";
			break;
	}
	backgroundRepeat = repeat;
	if(displayGrid == "true" && backgroundFlag == "false"){
		var bg = "#F7F9F8 url(../images/boxBg.jpg) repeat fixed";
	}else if(displayGrid == "false" && backgroundFlag == "false"){
		var bg = backgroundColor;
	}else{
		var bg = backgroundColor + " url(" + backgroundImg + ") " + backgroundRepeat;
	}
	$("#wrap").css({background: bg});

	autoHeight();
	$(window).resize(function(){
		autoHeight();
	})
	function autoHeight(){
		var h = $(window).height();
		var topH = $("#topPart").outerHeight() + $("#topSecondPart").outerHeight();
		if(h - topH > 0){
			$("#wrap").height(h - topH);
		}
		
		var h2 = $(window).height() - 35;
		if(h2 < 0) h2=100;
		$("#topoGraphSidePart, #sidePart").height(h2);
		
		var tPos = ($(window).height() - $("#topoGraphSidePartArr").outerHeight())/2;
		if(tPos <0) tPos = 100;
		$("#topoGraphSidePartArr").css("top",tPos);
	}
	//搜索框聚焦;
	topoSearchInputFocus();
	//侧边栏;
	$("#topoGraphSidePartArr").click(function(){
		if($("#topoGraphSidePartArr").hasClass("sideMapRight")){
			window.openCloseSidePart("open");
		}else{
			window.openCloseSidePart("close");
		}
	});
	//加入一个swf动画效果;
	swfobject.embedSWF("../network/Bubble.swf", "putSwf", folderWidth, folderHeight,"9.0.0", null,null, 
			{menu:"false",bgcolor:"#fff",wmode : 'transparent', allowScriptAccess:'sameDomain', base:".", type: "application/x-shockwave-flash", pluginspage : "http://www.adobe.com/go/getflashplayer"});
});//end document.ready;
//显示设备的其它功能;
function showOtherMenu(){
	otherLinkFn();//先根据类型生成菜单;
	$("#overTip").css("display","none");
	var $otherLinkBtn = $("#otherLinkBtn"),
	aPos = [];
	aPos.push($otherLinkBtn.offset().left);
	aPos.push($otherLinkBtn.offset().top + $otherLinkBtn.outerHeight());
	topBarSubMenu.showAt(aPos);
}
//显示或隐藏设备功能下拉菜单，ONU设备没有下拉菜单;
function displayOtherLink(bPara){
	if(bPara){
		$("#otherLinkLi").css({visibility:"visible"});	
	}else{
		$("#otherLinkLi").css({visibility:"hidden"});
	}
	
}
//点击后判断设备类型，通过类型，显示不同的下拉菜单。
function otherLinkFn(){
	var type = $(".eqContainerSelected").attr("type");
	var CCMTS = EntityType.getCcmtsType();
	var CMTS = EntityType.getCmtsType();
	var SFU = EntityType.getOnuType();
	var UNKNOWN = EntityType.getUnkonwType();
	switch (Number(type)){
		case CCMTS:
			showCmcEntityMenu();
			break;
		case CMTS:
			showCmcEntityMenu();
			break;
		case SFU:
			//ONU没有其他设备功能;
			topBarSubMenu.removeAll();
			break;
		case USEROBJ_TYPE.FOLDER:
			//点击的是子地域;
			showFolderMenu();
			break;
		case UNKNOWN:
			showUnknownEntityMenu();
			break;
		default:
			showSubOlt();
			break;
		}
};

//刷新页面;
function refreshFn(){
	window.location.href = window.location.href; 
}

var deleteType = null; //记录要删除什么;
//删除功能;
function onDeleteClick(e) {
	deleteType = null;
	window.top.showConfirmDlg("@COMMON.tip@", "@NETWORK.deleteEntity@", function(sConfirm) {
		if (sConfirm == 'no') {return;}
		var sType = e.data.type,
		dataObj = {},//最后要到后台删除的对象，该对象最终存储不同类型的数组;
		size = 0,//如果删除的设备比较多，那么应该出个loading...;
		aCanNotDelete = [];//有的地域是不能删除的;
		var edgeIds = [];//要删除的线条的数组;
		var cellIds = [];//要删除的子地域和已知类型的设备;
		var noEntityIds = [];
		var oNeedBackFloder = {};//有的地域不能删除，需要还原;
		oNeedBackFloder.floder = [];
		oNeedBackFloder.dom = [];
		
		//重构删除部分;
		switch(sType){
			case "oneIcon"://只选中图标的情况;
				var len = aSelectedIcons.length;
				    size = aSelectedIcons.length;
				if (size > 10) {
					window.top.showWaitingDlg("@COMMON.WAITING@", "@NETWORK.deletingEntityAndLink@");
				}
				if(aSelectedIcons.length > 0){
					for(var i=0; i<len; i++){
						cellIds.push(Number(aSelectedIcons[i].attr("name")));
					}
				}
				if(aSelectedLines.length > 0){
					for(var i=0,len=aSelectedLines.length; i<len; i++){
						var sId = (aSelectedLines[i].id).slice(4);
						edgeIds.push(Number(sId));
					}				
				}
				break
			case "lines"://只选中线条的情况;
				var len = aSelectedLines.length;				
				    size = aSelectedLines.length;
				if (size > 10) {
					window.top.showWaitingDlg("@COMMON.WAITING@", "@NETWORK.deletingEntityAndLink@");
				}
				if(aSelectedLines.length > 0){
					for(var i=0,len=aSelectedLines.length; i<len; i++){
						var sId = (aSelectedLines[i].id).slice(4);
						edgeIds.push(Number(sId));
					}				
				}
				break;
		}
		dataObj = {folderId: window.topoFolderId, nodeIds: cellIds, edgeIds: edgeIds};//准备数据库要删除的数据;
		
		$.ajax({
			url: 'topology/deleteCellByIds.tv', 
			data: dataObj,
			dataType: 'json', 
			cache: false,
			success: function(json) {
				//TODO 删除被成功删除的元素，告知哪些设备无法被删除
				var aSuccessIds = json.successIds,  //删除成功的id数组； 
				    aFailedIds = json.failedIds,
				    tipHtml; //提示信息;
				if(aFailedIds.length > 0){ //失败的个数大于0;
					if(aSuccessIds.length == 0){ //没有一个成功的;
						tipHtml = '<b class="orangeTxt">@NETWORK.deleteFailure@</b>';
					}else{ //有成功的;
						tipHtml = "@batchtopo.deleteFailed@";
					}
				}else{
					tipHtml = '<b class="orangeTxt">@batchtopo.deletedsuccessful@</b>';
				}
				top.afterSaveOrDelete({
					title : "@COMMON.tip@",
					html : tipHtml
				});
				if(aSuccessIds.length > 0){
					var aLinks = topoPic.eqDataAll.link,
					    aFolder = topoPic.eqDataAll.folder,
					    aEntity = topoPic.eqDataAll.entity,
					    flag = false;
					
					for(var j=aSuccessIds.length-1; j>=0; j--){
						var matchId = aSuccessIds[j];
						for(var i=aLinks.length-1; i>=0; i--){ //先在线条里面循环一次，将线条全部移除;
							var link = aLinks[i];
							if(link.nodeId == matchId){ //自己手绘的线条没有返回userObjId,改用nodeId;
								topoPic.removeLine(link.id);
								aLinks.splice(i,1);
								aSuccessIds.splice(j,1);
								break;
							}
						};
						for(var k=aFolder.length-1; k>=0; k--){//循环地域;
							var flolder = aFolder[k];
							if(flolder.userObjId == matchId){
								flag = true;
								var $sel = $('.eqContainerSelected[name="'+ matchId +'"]');
								$sel.unbind("mousedown",topoPic.eqMouseDown);
								$sel.unbind("click",topoPic.clearBubble);
								$sel.remove();
								aFolder.splice(k,1);
								break;
							}
						};
						for(var m=aEntity.length-1; m>=0; m--){//循环设备;
							var entity = aEntity[m];
							if(entity.userObjId == matchId){
								var $sel = $('#cell' + matchId);
								$sel.unbind("mousedown",topoPic.eqMouseDown);
								$sel.unbind("click",topoPic.clearBubble);
								$sel.remove();
								aEntity.splice(m,1);
							}
						};
					};//end for;
					if(flag){
						top.refreshTopoTree();
					}
				}
				aSelectedLines.length = 0;
				aSelectedIcons.length = 0;
				//删除成功以后导航恢复到默认。
				clickBlank();
			},error: function() {
				top.endDeleteTopo();
				clickBlank();
				window.top.showErrorDlg();
			},complete: function() {
				if (size > 10) {
					window.top.closeWaitingDlg();
				}
			}
		});//end ajax;
		return;
		
		
		
		
		//重构删除部分结束;
		/*switch(sType){
			case "oneIcon"://只选中图标的情况;
				var len = aSelectedIcons.length;
				size = aSelectedIcons.length;
				if (size > 10) {
					window.top.showWaitingDlg("@COMMON.WAITING@", "@NETWORK.deletingEntityAndLink@");
				}
				if(aSelectedLines.length > 0){
					//如果有选中的线条，全移除;
					var aLinks = topoPic.eqDataAll.link;
					for(var j=aSelectedLines.length-1; j>=0; j--){
						var matchId = aSelectedLines[j].id;
						for(i=aLinks.length-1; i>=0; i--){							
							if(aLinks[i].id == matchId){
								edgeIds.push(aLinks[j].nodeId);//存数组给后台数据库移除;
								aLinks.splice(i,1);
								//aSelectedLines[j].remove();
								topoPic.removeLine(aSelectedLines[j].id);
								aSelectedLines.splice(j,1);
								break;
							}
						}
					}
					aSelectedLines.length = 0; 
				};//end if; 
				for(var i=0; i<len; i++){//有设备链接的线条，但是又没选中，也要移除;
					var eqName = Number(aSelectedIcons[i].attr("name"));
					if(topoPic.eqDataAll.link){//移除线条;
						var aLinks = topoPic.eqDataAll.link;
						var eqName2 = Number( (aSelectedIcons[i].attr("id")).slice(4) );
						for(var j=aLinks.length-1; j>=0; j--){
							if(eqName2 == aLinks[j].srcEntityId || eqName2 == aLinks[j].destEntityId){//name存储的是nodeId;
								edgeIds.push(aLinks[j].nodeId);//存数组给后台数据库移除;
								//topoPic.paper.getById(aLinks[j].id).remove();//移除图上的线条;
								topoPic.removeLine(aLinks[j].id);
								aLinks.splice(j,1);//将eqDataAll中link数组移除;
							}
						}
						
					}
					var flag = false;
					if(topoPic.eqDataAll.folder){//移除子地域;
						if(Number(aSelectedIcons[i].attr("type")) == USEROBJ_TYPE.FOLDER){//如果是子地域;
							deleteType = 2;
							var aFolder = topoPic.eqDataAll.folder;
							for(var k=aFolder.length-1;k>=0;k--){
								if(aFolder[k].id == aSelectedIcons[i].attr("id")){
									oNeedBackFloder.floder.push(aFolder[k]);
									aFolder.splice(k,1);//将eqDataAll中aFolder数组移除;
									flag = true;
									cellIds.push(Number(aSelectedIcons[i].attr("name")));
									oNeedBackFloder.dom.push({id:Number(aSelectedIcons[i].attr("name")),$this:aSelectedIcons[i]});
									aSelectedIcons[i].unbind("mousedown",topoPic.eqMouseDown);
									aSelectedIcons[i].unbind("click",topoPic.clearBubble);
									aSelectedIcons[i].remove();
									top.deleteTopoTree();//左侧正在删除;
								}
							}
						}
					}
					if(topoPic.eqDataAll.entity && Number(aSelectedIcons[i].attr("type")) != USEROBJ_TYPE.FOLDER){//设备;
						var aEqs = topoPic.eqDataAll.entity;
						var SFU = EntityType.getOnuType();
						for(var j=aEqs.length-1; j>=0; j--){
							if(aEqs[j].nodeId == eqName){
								if(EntityType.isCcmtsWithoutAgentType(aEqs[j].typeId) ||  aEqs[j].type == SFU ){
									noEntityIds.push(aEqs[j].userObjId);
								}else{
									cellIds.push(aEqs[j].nodeId);
								}
								aEqs.splice(j,1);//将eqDataAll中entity数组移除;
							}
						}
					}
					if(!flag){
						aSelectedIcons[i].unbind("mousedown",topoPic.eqMouseDown);
						aSelectedIcons[i].unbind("click",topoPic.clearBubble);
						aSelectedIcons[i].remove();//移除所有选中的图标;	
					}
				};//end for;
				dataObj = {folderId: window.topoFolderId, nodeIds: cellIds, edgeIds: edgeIds, noEntityIds:noEntityIds};//准备数据库要删除的数据;
				//alert("cellIds:"+cellIds +"edgeIds:"+ edgeIds + "noEntityIds:" + noEntityIds)
			break;
			case "lines"://只选中线条的情况;
				var len = aSelectedLines.length;				
				size = aSelectedLines.length;
				if (size > 10) {
					window.top.showWaitingDlg("@COMMON.WAITING@", "@NETWORK.deletingEntityAndLink@");
				}
				for(var i=0; i<len; i++){
					var sId = (aSelectedLines[i].id).slice(4);
					edgeIds.push(Number(sId));
				}
				dataObj = {folderId: window.topoFolderId, nodeIds: [], edgeIds: edgeIds, noEntityIds:[]};//准备数据库要删除的数据;
				if(aSelectedLines.length > 0 && topoPic.eqDataAll.link){//从数组中删除该id的数据。从paper中也remove掉线条;
					var aLinks = topoPic.eqDataAll.link;
					for(var j=aSelectedLines.length-1; j>=0; j--){
						var matchId = aSelectedLines[j].id;
						for(i=aLinks.length-1; i>=0; i--){							
							if(aLinks[i].id == matchId){
								aLinks.splice(i,1);
								topoPic.removeLine(aSelectedLines[j].id);
								aSelectedLines.splice(j,1);
								break;
							}
						}
					}
					aSelectedLines.length = 0;
				}
			break;
		}
		$.ajax({url: 'topology/deleteCellByIds.tv', type: 'POST',
			data: dataObj,
			dataType: 'json', cache: false,
			success: function(json) {
				if(json.ExistEntity){//如果该地域下有其它地域，那么不能删除;
					top.endDeleteTopo();
					top.refreshTopoTree();
					//window.top.showMessageDlg("@COMMON.tip@", json.msg);json.msg是错误的,这里没有必要使用后台传过来的msg;
					top.afterSaveOrDelete({
		   				title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@resources/topo.virtualDeviceList.removeFail@</b><br/>'+json.msg
		   			});
					//恢复那些不能删除的地域;
					var aNoDeletes = [];
					aNoDeletes = json.noDeletes;
					if(aNoDeletes.length > 0){
						for(var i=0; i<aNoDeletes.length; i++){
							var aDoms = oNeedBackFloder.dom;
							for(var j=0; j<aDoms.length; j++){
								if( aDoms[j].id == aNoDeletes[i] ){
									$("#" + topoPic.renderIcoDiv).append(aDoms[j].$this);//恢复$dom对象;										
									aDoms[j].$this.bind("mousedown", topoPic, topoPic.eqMouseDown);
									aDoms[j].$this.bind("click", topoPic, topoPic.clearBubble);										
									topoPic.eqDataAll.folder.push( (oNeedBackFloder.floder)[j] );//恢复eqDataAll;
								}
							}	
						}
					}
					//oNeedBackFloder.floder.length;
					//oNeedBackFloder.dom.length;
				}else{
					if(deleteType == 2 ){//删除地域成功;
						top.endDeleteTopo();
						top.refreshTopoTree();
					}
				}
				//删除成功以后导航恢复到默认。
				clickBlank();
			},error: function() {
				top.endDeleteTopo();
				clickBlank();
				window.top.showErrorDlg();
			},complete: function() {
				if (size > 10) {
					window.top.closeWaitingDlg();
				}
			}
		});//end ajax;
*/	});
};//end onDeleteClick;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//下拉未知设备start;
function showUnknownEntityMenu(){
	topBarSubMenu.removeAll();
	var eqId = $(".eqContainerSelected").attr("id"),
	obj = getSelectedObj(eqId);
	var items = [];
	//打开超链接
    items.push({text: "@GRAPH.openURL@", iconCls: 'bmenu_href', handler: function(){onOpenURLClick(1)}});
    items.push({text: "@COMMON.property@", handler: function(){onVertexPropertyClick(getSelectedObj($(".eqContainerSelected").attr("id")))}});//属性
    
    topBarSubMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
}
//下拉未知设备end;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//下拉ccmts部分start;
function showCmcEntityMenu(){
	topBarSubMenu.removeAll();
	var eqId = $(".eqContainerSelected").attr("id"),
	obj = getSelectedObj(eqId);
	//isExpress, true: is express,false:is not express
	var isExpress = obj.virtualNetworkStatus != 1 ? true:false;
	//window.currentVertex = vertex;这里不清楚为什么要存全局变量;
	switch(isExpress){
		case true:
	 		//showCmcEntityMenu2(vertex);
	 		break;
	 	case false:
	 		showCmcEntityMenu1(obj);
	 		break;
	}
}
function showCmcEntityMenu1(obj){
	var items = [];
	items.push({id:"view2", text:"@COMMON.view@", handler: function(){seeCCMTSFn(obj)}});//查看
	items.push({id:"realtimeInfo", text:"@COMMON.realtimeInfo@", handler: function(){showRealTimeInfo(obj)}});//查看
	if(!EntityType.isCcmtsWithoutAgentType(obj.typeId)){// 8800A不显示[工具]菜单;
        items.push({
            text : "@NETWORK.tool@",
            menu : [// 工具
            {
                text : "@NETWORK.ping@" ,
                handler : onPingClick 
            }, {
                text : "@NETWORK.tracert@",
                handler : onTracertClick 
            }
            ]
        });
    };//end if;
    items.push({text: "@NETWORK.discoveryAgain@", iconCls: 'bmenu_refresh' , handler: onDiscoveryAgainClick });//刷新设备
    selObj = returnSelected();
    var cmcId = selObj.nodeId;
    var entityId = selObj.userObjId;
    var ccMac = selObj.mac;
    var typeId = selObj.typeId;
    if(EntityType.isCcmtsType(typeId)) {
    $.ajax({
        type: "GET",
        url: selObj.modulePath+"/js/extendMenu.js",
        dataType: "script",
        async : false,
        success  : function () {
            items = extendOper(items, entityId, cmcId , ccMac , typeId);
        }
    });
    } else if(EntityType.isCmtsType(typeId)){
    	items[items.length] = {text: I18N.CCMTS.ccmtsPerformCollectConfig,id:"ccmts8800APerfManage",handler:cmtsPerfManage};
    }
    items.push('-');
    items.push({text: "@GRAPH.openURL@", iconCls: 'bmenu_href', handler: function(){onOpenURLClick(1)}});//打开超链接
    if (googleSupported) {
        items.push({text:"@COMMON.addToGoogle@", handler : onAddToGoogleClick,disabled:!googleMapPower});//添加到谷歌地图
    }
    items.push({text: "@COMMON.property@", handler:function(){ onVertexPropertyClick(getSelectedObj($(".eqContainerSelected").attr("id"))) }});//属性
	
	topBarSubMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
}
function cmtsPerfManage(){
	var entityId = selObj.userObjId;
	window.parent.addView('cmcPerfParamConfig', I18N.CMC.title.performanceCollectConfig,"bmenu_preference",'/cmc/perfTarget/showCmcPerfManage.tv?entityId=' + entityId);
}
//查看ccmts设备,8800CA,8800A,8800B;
function seeCCMTSFn(obj){
	 window.top.addView('entity-' + obj.userObjId,  obj.name ,
             'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + obj.userObjId);
}
function showRealTimeInfo(obj){
    window.parent.addView('entity-realTime' + obj.nodeId, '@COMMON.realtimeInfo@['+obj.name+']', 'entityTabIcon', '/cmc/showCmcRealTimeData.tv?cmcId=' + obj.nodeId);
}
//下拉ccmts部分end;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//下拉子地域start;
function showFolderMenu(){
	topBarSubMenu.removeAll();
	var items = [];
	items.push({text: "@NETWORK.openChildGraph@", handler: onOpenFolderClick});
	items.push({text: "@GRAPH.openURL@", iconCls: 'bmenu_href', handler: function(){onOpenURLClick(2)}});
	items.push({text: "@COMMON.property@", handler: function(){onVertexPropertyClick(getSelectedFloderObj($(".eqContainerSelected").attr("id")))}});
	topBarSubMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
}
function onOpenFolderClick(){
	var eqId = $(".eqContainerSelected").attr("id"),
	obj = getSelectedFloderObj(eqId);
	if(obj != {}){
		window.top.addView("topo" + obj.userObjId, obj.name,
				"topoLeafIcon", 'topology/showNewTopoDemo.tv?folderId=' + obj.userObjId);
	}
}
//下拉子地域end;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//下拉菜单olt部分start;
//显示olt下拉菜单;

function showSubOlt(){
	topBarSubMenu.removeAll();
	var items = [{id:"view2",text:"@COMMON.view@" ,handler: seeOltFn }];//查看
	var eqId = $(".eqContainerSelected").attr("id");
	var status = false;//先假设为false;
	for(var i=0; i<topoPic.eqDataAll.entity.length; i++){
		if(eqId == topoPic.eqDataAll.entity[i].id){
			status = topoPic.eqDataAll.entity[i].status;
		}
	}
	if(status){
    	items.push({
    		text: "@NETWORK.tool@", menu: [//工具
		        {text: "@NETWORK.ping@", handler: onPingClick},
		        {text: "@NETWORK.tracert@" , handler: onTracertClick },
		        {text: "Mibble Browser", handler: onMibBrowseClick}
    			]
    	});
    	 items.push({text: "@NETWORK.discoveryAgain@", iconCls: 'bmenu_refresh' , handler: onDiscoveryAgainClick });//刷新设备
    	 selObj = returnSelected();
 	     $.ajax({
 	         type: "GET",
 	         url: String.format("/{0}/js/extendMenu.js",selObj.module),
 	         dataType: "script",
 	         async : false,
 	         success  : function () {
 	        	 items = extendOper(items);
 	         }
 	    });
 	    items.push('-');
 	   	items.push({text: "@GRAPH.openURL@", iconCls: 'bmenu_href', handler: function(){onOpenURLClick(1)}});//打开超链接
 	   	if(googleSupported) {
 	        items.push({text:"@COMMON.addToGoogle@", handler : onAddToGoogleClick,disabled:!googleMapPower});//添加到谷歌地图
 	    }
 	   	items.push('-');
 	    items[items.length] = {text: "@COMMON.property@", handler: function(){onPropertyClickFn(1)}};//属性
	}
	topBarSubMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
};//end showSubOlt
//查看olt设备;
function seeOltFn(){
	var eqId = $(".eqContainerSelected").attr("id");
	var obj = getSelectedObj(eqId);
	window.top.addView('entity-' + obj.userObjId, obj.name ,
            'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + obj.userObjId);
}
//下拉菜单olt部分end;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//列表查看;
function onViewByDetailClick() { location.href = 'getTopoMapByFolderId.tv?viewType=detail&folderId=' + topoFolderId + '&entityId=' + gotoId; }
//ping;
function onPingClick() {
	var eqId = $(".eqContainerSelected").attr("id"),
	obj = getSelectedObj(eqId);
	if(obj != {}){
		window.top.createDialog("modalDlg", 'Ping ' + obj.ip, 600, 400,
		"entity/runCmd.tv?cmd=ping&ip=" + obj.ip, null, true, true);
	}
};//end onPingClick;
function onTracertClick() {
	var eqId = $(".eqContainerSelected").attr("id"),
	obj = getSelectedObj(eqId);
	if(obj != {}){
		window.top.createDialog("modalDlg", 'Tracert ' + obj.ip, 600, 400,
		"entity/runCmd.tv?cmd=tracert&ip=" + obj.ip, null, true, true);
	}
}
function onMibBrowseClick() {
	var eqId = $(".eqContainerSelected").attr("id"),
	obj = getSelectedObj(eqId);
	if(obj != {}){
		Ext.menu.MenuMgr.hideAll();
		window.top.addView("mibbleBrowser","Mibble Browser",null,"/mibble/showMibbleBrowser.tv?host="+obj.ip,null,true)		
	}
}

//属性;
function onPropertyClickFn(type){
	var eqId = $(".eqContainerSelected").attr("id");
	var obj = getSelectedObj(eqId);
	var url = '';
	var entityId = obj.nodeId;
    if (type == USEROBJ_TYPE.ENTITY) {
		url = 'entity/showEntityPropertyJsp.tv?entityId=' + entityId + '&folderId=' + topoFolderId;
	} else if (type == USEROBJ_TYPE.FOLDER) {
		url = 'topology/getTopoFolderProperty.tv?nodeId=' + entityId + '&isRegion=1';
	} else if (type == USEROBJ_TYPE.SHAPE) {
		url = 'topology/showVmlPropertyJsp.tv?nodeId=' + entityId +
			'&folderId=' + topoFolderId + '&id=' + entityId;
	} else if (type == USEROBJ_TYPE.LINK) {
		url = 'link/showLinkPropertyJsp.tv?linkId=' + entityId;
	}
    showSidePart(url);
}
//修改别名;
function renameEntity() {
	var type = $(".eqContainerSelected").attr("type");
	if(type != "2"){
		var eqId = $(".eqContainerSelected").attr("id");
		var obj = getSelectedObj(eqId);
        var entityId = obj.userObjId;
        window.top.createDialog('renameEntity', "@COMMON.realias@", 600, 250,
                '/entity/showRenameEntity.tv?entityId=' + entityId + "&pageId=" + window.parent.getActiveFrameId(), null, true, true);
	}else{ //如果是2那么就是修改子地域的别名;
		var folderId = $(".eqContainerSelected").attr("name"); 
		var folderName = $(".eqContainerSelected").find(".theName").text();
		
		window.top.createDialog('renameFolder', "@COMMON.realias@", 600, 250,
                '../topology/showRenameFolder.tv?folderId=' + folderId + "&oldName=" + folderName + "&superiorId=" + superiorFolderId + "&topoFolderId=" + topoFolderId, null, true, true); 
		
		
	
	
	
	
		/* var $theName = $(".eqContainerSelected").find(".theName");
		var txt = $theName.text();
		var floderId = $theName.parent().attr("id");
		var _folerId = Number($theName.parent().attr("name"));
		$theName.html('<input type="text" style="width:84px;" class="normalInput" value="'+ txt +'" id="changeName" maxlength="24" />');
		$("#changeName").focus().blur(function(){
			var newName = $(this).val();
			$theName.empty().text(newName);
			//名称必须为1-24位的合法字符
			var reg = /^[a-zA-Z\d\u4e00-\u9fa5-_]{1,24}$/;
			if(!reg.test($.trim(newName))){
				$theName.text(txt);
				window.top.showMessageDlg("@MENU.tip@", "@topo.newTopoFolder.nameTip@");
				return;
			}
			
			$.ajax({url: '../topology/renameTopoFolder.tv', type: 'POST', cache: false, dataType: 'plain',
				dataType: 'json',
				data: {superiorId: superiorFolderId, folderId: _folerId, oldName: txt, name: newName},
				success: function(json) {
					if (json.exists) {//数据库中已经有这个名字了;
						$theName.text(txt);
						window.top.showMessageDlg("@MENU.tip@", "@NETWORK.folderExist@");
						return;
					}
					var aFloder = topoPic.eqDataAll.folder;
					for(var i=0; i<aFloder.length; i++){
						if(floderId == aFloder[i].id){
							aFloder[i].text = newName;
						}
					}
					//刷新左侧topo树菜单;
					top.refreshTopoTree();
				},
				error: function() {
					$theName.text(txt);
				}
			});
			
		}); */
	}
}
//打开超链接;
function onOpenURLClick(type) {
	var entityId = $(".eqContainerSelected").attr("name");
	if(entityId){
		var url;
		switch(type){
			case 1://设备;
				url = getSelectedObj($(".eqContainerSelected").attr("id")).url;
				break;
			case 2://地域;
				url = getSelectedFolder($(".eqContainerSelected").attr("id")).url;
				break;
		}
		if(url != null && url != "" && url != undefined){
			if (!url.startWith("http://") && !url.startWith("https://")) {
                url = "http://"+url;
            }	
			var testHost = matchHost(url);
			if(testHost == true){
            	window.top.addView('url' + entityId, 'URL',  "topoLeafIcon", url);
			}else{
				window.open(url);
			}
		}else{
			//window.top.showMessageDlg("@COMMON.tip@", "@GRAPH.hrefMessage@");
			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : "@GRAPH.hrefMessage@"
			});
			switch(type){
				case USEROBJ_TYPE.ENTITY://1,打开设备的超链接;
					onVertexPropertyClick(getSelectedObj($(".eqContainerSelected").attr("id")));
					break;
				case USEROBJ_TYPE.FOLDER://打开子地域的超链接;
					onVertexPropertyClick(getSelectedFloderObj($(".eqContainerSelected").attr("id")));
					break;
			}
		}
	}
}	

//检测域名，是否在同一域名下;
function matchHost(url){
	var hostUrl = top.window.location.host;
	var arr = url.split("/");
	var myUrl = arr[2];
	if(myUrl == hostUrl){
		return true;
	}
	return false;
};

//添加到google地图；
function onAddToGoogleClick() {
	window.parent.addView("ngm", "@NETWORK.googleMapNet@", "googleIcon", "google/showEntityGoogleMap.tv");
}
//不存在超链接，显示侧边栏;
function onVertexPropertyClick(selectObj){
	var url = '';
	var entityId = selectObj.nodeId;
	var type = selectObj.objType;
    if (type == USEROBJ_TYPE.ENTITY) {
		url = 'entity/showEntityPropertyJsp.tv?entityId=' + entityId + '&folderId=' + topoFolderId;
	} else if (type == USEROBJ_TYPE.FOLDER) {
		url = 'topology/getTopoFolderProperty.tv?nodeId=' + entityId + '&isRegion=1';
	} else if (type == USEROBJ_TYPE.SHAPE) {
		url = 'topology/showVmlPropertyJsp.tv?nodeId=' + entityId +
			'&folderId=' + topoFolderId + '&id=' + entityId;
	} else if (type == USEROBJ_TYPE.LINK) {
		url = 'link/showLinkPropertyJsp.tv?linkId=' + entityId;
	}
    showSidePart(url);
}


//循环数组，获取需要的对象,注意这是循环设备;
function getSelectedObj(paraId){
	var obj = {};
	for(var i=0; i<topoPic.eqDataAll.entity.length; i++){
		if(paraId == topoPic.eqDataAll.entity[i].id){
			obj = topoPic.eqDataAll.entity[i];
		}
	}
	return obj;
}
//循环数组，获取需要的对象,注意这是循环地域;
function getSelectedFloderObj(paraId){
	var obj = {};
	for(var i=0; i<topoPic.eqDataAll.folder.length; i++){
		if(paraId == topoPic.eqDataAll.folder[i].id){
			obj = topoPic.eqDataAll.folder[i];
		}
	}
	return obj;
};
//循环数组，获取需要的对象是第几个;
function getSelectedObjIndex(paraId){
	var num = null;
	for(var i=0; i<topoPic.eqDataAll.entity.length; i++){
		if(paraId == topoPic.eqDataAll.entity[i].id){
			num = i;
			break;
		}
	}
	return num;
};
//循环数组，获取需要的子地域对象，注意这里是循环子地域;
function getSelectedFolder(paraId){
	var obj = {};
	for(var i=0; i<topoPic.eqDataAll.folder.length; i++){
		if(paraId == topoPic.eqDataAll.folder[i].id){
			obj = topoPic.eqDataAll.folder[i];
		}
	}
	return obj;
}
//循环数组，获取需要的子地域是第几个;
function getSelectedFolderIndex(paraId){
	var num = null;
	for(var i=0; i<topoPic.eqDataAll.folder.length; i++){
		if(paraId == topoPic.eqDataAll.folder[i].id){
			num = i;
			break;
		}
	}
	return num;
};

$(window).load(function(){
		loadMapData(gotoId, false);
});//end window.onload;

//tbar 动画;
function showTopMenu(oldMenuId,newMenuId){
	$newMenuId = $("#" + newMenuId);
	if(!($newMenuId.hasClass("topBarSelected"))){
		$("#" + oldMenuId).removeClass("topBarSelected").fadeOut("fast");
		$newMenuId.addClass("topBarSelected").css({top: 34,display: "block"}).stop().animate({top:0,opacity:1},"fast");
		//使用哪个菜单，就有class="topBarSelected";
	}
}
//显示默认目录;
function clickBlank(){
	if(!($("#normalTopPart").hasClass("topBarSelected"))){
		$("#selectOneIcoPart, #onlySelectOneLine, #selectLines, #selectIcons").removeClass("topBarSelected").fadeOut("fast");
		$("#normalTopPart").addClass("topBarSelected").css({top: 34,display: "block"}).stop().animate({top:0,opacity:1},"fast");
	}
}
//选中一个图标;
function selectOneIco(fixed){
	switch(fixed){//是否固定
		case "true":
				$("#fixedBtn").addClass("normalBtnIcoSelect");
			break;
		case "false":
				$("#fixedBtn").removeClass("normalBtnIcoSelect");
			break;
	};//end switch;
	Ext.menu.MenuMgr.hideAll();
	$eq = $(".eqContainerSelected");
	var type = $eq.attr("type");
	if(type == "2"){//选中的是设备;
		$("#cutLi, #copyLi").css({display:"none"});
	}else{//选中的是地域;
		$("#cutLi, #copyLi").css({display:"block"});
	}
	var oldId = $(".topBarSelected").attr("id");	
	showTopMenu(oldId,"selectOneIcoPart");
	//判断类型，是否显示[设备功能]下拉菜单;
	var SFU = EntityType.getOnuType()
	switch(Number(type)){
		case SFU://onu没有下拉菜单;
			displayOtherLink(false);
			break;
		default://显示;
			displayOtherLink(true);
			break;
	};//end switch;
	aSelectedIcons.length = 0;//先清空选中图标的数组;
	aSelectedIcons.push($eq);
}
//点击后，固定位置（一个）;
function fixedPosition(){
	var $me = $(".eqContainerSelected");
	var fixed = $me.attr("alt");
	switch(fixed){//是否固定
		case "true":
			onUnFixLocationClick("one");
			break;
		case "false":
			onFixLocationClick("one");				
			break;
	};
};//end fixedPosition;
//固定多个设备;
function fixedManyFn(){
	onFixLocationClick(null);
}
//移除固定多个设备;
function unFixedManyFn(){
	onUnFixLocationClick(null);
}
//固定位置;
function onFixLocationClick(type) {
	var nodeIds = [],
	$eqContainerSelected = $("#icoWrap div.eqContainerSelected");
	$eqContainerSelected.each(function(){
		nodeIds.push( Number($(this).attr("nodeid")) );
	});
	$.ajax({
		url: '../topology/updateVertexFixed.tv', type: 'POST', data: {folderId: topoFolderId, nodeIds: nodeIds, fixed:true
		},success: function() {
			$eqContainerSelected.attr("alt","true");
			if(type == "one"){//如果是一个图标，那么还要将那个按钮状态改变，多个图标则不需要改变;
				$("#fixedBtn").addClass("normalBtnIcoSelect");
			}
		},error: function() {
			window.parent.showErrorDlg();
		},dataType: 'plain', cache: false
	});
};//end onFixLocationClick;
//解锁位置;
function onUnFixLocationClick(type) {
	var nodeIds = [],
	$eqContainerSelected = $("#icoWrap div.eqContainerSelected");
	$eqContainerSelected.each(function(){
		nodeIds.push( Number($(this).attr("nodeid")) );
	});
	$.ajax({url: '../topology/updateVertexFixed.tv', type: 'POST',
		data: {folderId: topoFolderId, nodeIds: nodeIds,fixed:false},
		success: function() {
			$eqContainerSelected.attr("alt","false");
			if(type == "one"){//如果是一个图标，那么还要将那个按钮状态改变，多个图标则不需要改变;
				$("#fixedBtn").removeClass("normalBtnIcoSelect");
			}
		}, error: function() {
			window.parent.showErrorDlg();
		}, dataType: 'plain', cache: false});
}

//拉框后执行函数;
function drawRectFn(aIcons,aLines){
	var icoLen = aIcons.length,
	lineLen = aLines.length,
	oldId = $(".topBarSelected").attr("id");		
	aSelectedLines.length = 0;
	for(var i=0;i<aLines.length; i++){//将选中的给全局变量;
		aSelectedLines.push(aLines[i]);
	}
	aSelectedIcons.length=0;
	for(i=0;i<aIcons.length;i++){
		aSelectedIcons.push(aIcons[i]);
	}
	if(icoLen == 1){//只选中一个图标,0根或多根线条;
		var fixed = $(".eqContainerSelected").attr("alt");
		selectOneIco(fixed);
	}else if(icoLen == 0){//没有选中图标;
		if(lineLen == 1){//选中1根或多根线条;
			showTopMenu(oldId, "onlySelectOneLine");
		}else if(lineLen > 1){//选中多跟线条;
			showTopMenu(oldId, "selectLines");
		}
	}else if(icoLen > 0){//选择了多个设备;
		showTopMenu(oldId, "selectIcons");
	}
};//end drawRectFn;

//链接2个设备后，执行函数,这里不应该是异步;
function eqDrawLineFn(paraObj){
	var returnValue = null;
	$.ajax({url:'../link/insertNewLink.tv',
		data: paraObj,
		dataType: 'json', cache:'false',async:false,
		success: function(json) {
			returnValue = json;
		}, error: function(){}
	});
	return returnValue;
};//end eqDrawLineFn;

//聚焦input框;
function topoSearchInputFocus(){
	$me = $("#topoSearchInput");
	$me.focus(function(){
		if($me.val() == "@topo.find@"){
			$me.val("");
		}
	}).blur(function(){
		if($me.val() == ""){
			$me.val("@topo.find@");
		}			
	})
};//end topoSearchInputFocus;

function init(){
	var showLine = (displayLink == "true") ? 1 : 0;
	topoPic = new Nm3kTopo({
		renderTo: "wrap",
		renderIcoDiv: "icoWrap",
		width: folderWidth,
		height: folderHeight,
		topHeight: 66,
		topoFolderId: window.topoFolderId,
		eqData: window.eqData,
		proData: window.jsonData,
		selectOneIco: "selectOneIco",
		selectOneLine: "selectOneLine",
		clickBlank: "clickBlank",
		drawRectFn: "drawRectFn",
		eqDrawLineFn: "eqDrawLineFn",
		lineStroke: lineStroke,
		lineOpacity: showLine,
		lineWidth: lineWidth
	});
	topoPic.init();
	initZoomBar();
	//重新刷新页面后显示地图级别;
	var bro=$.browser;
	if(Raphael.svg && !bro.msie){
		zoomMap(zoomValue);
	}
	
	showHideAlert();//是否显示告警信息;
	showHideLabel();
	//通过gotoId,是否要定位某个设备;
	firstPositionEqFn();
	
	//点击画线，拖动，拉框-请不要修改id，id和type已经挂钩;
	$("#sDrawLine, #sDrag, #sDrawRect").click(function(){
		var $this = $(this);
		topoPic.type = $this.attr("id");
		chageSelectIco($this);
		var t = $this.attr("alt");
		toolTipTxt(t);
		$("#overTip").css({display:"none"});
	});
	$("#topPart li a").mouseover(function(){
		var $this = $(this);
		var txt = $this.attr("name");
		var l = $this.offset().left;
		var arr = txt.split("|"); 
		var str = "";
		str +=  '<p class="pB5"><b>'+ arr[0] +'</b></p>' + arr[1];			
		$("#overTip").html(str).css({display:"block",left:l});
	}).mouseout(function(){
		$("#overTip").css({display:"none"});
	})
	//是否显示图标后面的提示文字;
	$("#showTipTxt").click(function(){
		var $this = $(this);
		var toolView = 'hide';
		if($this.hasClass("normalBtnIcoSelect")){//不显示文字;
			$this.removeClass("normalBtnIcoSelect");
			$("#topPart li a").each(function(){
				var $me = $(this);
				var domI = $me.find("i");
				$me.find("span").html(domI);
			});
			
		}else{//显示文字;
			$this.addClass("normalBtnIcoSelect");
			$("#topPart li a").each(function(){
				var $me = $(this);
				var txt = $me.attr("name").split("|")[0];
				$me.find("i").after(txt);
			});
			toolView = 'show';
		};
		
		$.ajax({
			url: '/topology/saveToolView.tv', 
			cache:false, 
			method:'post',
			data: {
				topoToolView : toolView
			},
			success: function() {
			},
			error: function(){
			}
		}); 
		
	});//end click;
	//点击固定位置;
	$("#fixedBtn").bind("click",fixedPosition);
	//点击设备其它功能;
	$("#otherLinkBtn").bind("click",showOtherMenu);
	//搜索框查询;
	searchInputEnter();
	//点击固定多个,移除固定多个;
	$("#fixedManyLinkBtn").bind("click",fixedManyFn);
	$("#unFixedManyLinkBtn").bind("click",unFixedManyFn);
	//多个图标左对齐;
	$("#alignLeftLinkBtn").bind("click",{align:"left"}, iconAlignFn);
	//多个图标右对齐;
	$("#alignRightLinkBtn").bind("click",{align:"right"}, iconAlignFn);
	//左右居中对齐;
	$("#alignCenterLinkBtn").bind("click",{align:"center"}, iconAlignFn);
	//顶对齐;
	$("#valignTopLinkBtn").bind("click",{align:"top"}, iconAlignFn);
	//底对齐;
	$("#valignBottomLinkBtn").bind("click",{align:"bottom"}, iconAlignFn);
	//上下对齐;
	$("#valignMiddleLinkBtn").bind("click",{align:"middle"}, iconAlignFn);
	//拓扑图属性按钮;
	$("#topoProLinkBtn").bind("click", onPropertyClick);
	//线条属性;
	$("#lineProperty").bind("click",linePropertyFn);
	//图例;
	$("#legentLinkBtn").bind("click",lengentFn);
	//修改别名;
	$("#renameLinkBtn").bind("click",renameEntity);
	//列表查看;
	$("#showListLinkBtn").bind("click",onViewByDetailClick);
	//删除一根线条,删除多根线条;
	$("#deleteOneLineLinkBtn, #deleteLinesLinkBtn").bind("click",{type:"lines"},onDeleteClick);
	//删除一个或多个图标，可能是子地域也可能是设备图标;
	$("#deleteOneIconLinkBtn, #deleteIconsLinkBtn").bind("click",{type:"oneIcon"},onDeleteClick);
	//保存;
	$("#saveLinkBtn").bind("click",saveFn);
	//排列组合;
	$("#arrangeLinkBtn").bind("click",arrangeFn);
	//粘贴;
	$("#pasteLinkBtn").bind("click",onPasteClick);
	//复制;
	$("#copyOneLinkBtn, #copyManyLinkBtn").bind("click",{type:"copy"},onCopyOrCutClick);
	//剪切;
	$("#cutOneLinkBtn, #cutManyLinkBtn").bind("click",{type:"cut"},onCopyOrCutClick);
};//end init;
//排列图标;
function arrangeFn(){
	topBarSubMenu.removeAll();
	var items = [];
    items.push({text: "@GRAPH.defaultArrange@", iconCls: 'bmenu_arrange', handler: function(){arrangeTopoMap(0);} });//默认排列;
    items.push({text: "@GRAPH.circular@", iconCls: 'miniIcoStar', handler: function(){arrangeTopoMap(1);} });//圆形排列;    
    topBarSubMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
	$("#overTip").css("display","none");
	var $btn = $("#arrangeLinkBtn"),
	aPos = [];
	aPos.push($btn.offset().left);
	aPos.push($btn.offset().top + $btn.outerHeight());
	topBarSubMenu.showAt(aPos);
}
//排列图标后台交互;
function arrangeTopoMap(type){
	var $eq = $("#icoWrap div.eqContainer"),
	size = $eq.length,
	w = $("#icoWrap").width() - 100,
	h = $("#icoWrap").height() - 100;
	if (size < 2) return;//小于2个设备，没有必要排列;
	window.top.showWaitingDlg("@COMMON.waiting@", "@NETWORK.arranging@");
	$.ajax({url: 'arrangeEntityByFolderId.tv', dataType: 'json', cache: 'false',
		data: {folderId: topoFolderId, arrangeType: type,
			mapWidth: w , mapHeight: h
		},
		success: function(json){
			var len = json.length;
		    for (var i = 0; i < len; i++) {
		    	var $eq = $("#icoWrap .eqContainer[nodeid='"+ json[i].nodeId +"']");
		    	if($eq.length > 0){
		    		if($eq.attr("alt") != "true"){//图标不能被固定;
		    			$eq.css({left:json[i].x, top:json[i].y});
		    		}
		    	}
		    }
		    //更新线条位置;
		    topoPic.upDateAllLinePosition(topoPic);
		       
			/* var vertex = null;
	   	   var len = json.length;
	       for (var i = 0; i < len; i++) {
	       	  if (!json[i].fixed) {
	       	  	  vertex = model.getVertex('cell' + json[i].nodeId);
		       	  if (vertex != null) {
		       	  	  vertex.setXY(json[i].x,json[i].y);
		       	  }
	       	  }
	       } */
		},
		complete: function() {
			window.top.closeWaitingDlg();
		}
	});
}
//显示图例版;
function lengentFn(){
	showSidePart("topology/showTopoLabelJsp.tv?folderId=" + topoFolderId);
}
//多个图标左对齐;
function iconAlignFn(e){
	var nLpos = null,
	nTpos = null,
	aLpos = [],//用来左右对齐; 
	aTpos = [],//用来上下对齐;
	aEqId = [];//记录所有设备的id和x,y坐标;
	$eqContainerSelected = $(".eqContainerSelected");
	$eqContainerSelected.each(function(){
		var $this = $(this);
		var tempO = {
			id: $this.attr("name"),
			x: $this.position().left + topoPic.halfIcoWidth,
			y: $this.position().top + topoPic.halfIcoHeight
		};
		aEqId.push(tempO);
		if(nLpos == null){
			nLpos = $(this).position().left; 
			nTpos = $(this).position().top;
			aLpos.push($(this).position().left);
			aTpos.push($(this).position().top);
		}else{
			switch(e.data.align){
				case "left":
					nLpos = Math.min(nLpos,$(this).position().left);
					break;
				case "right":
					nLpos = Math.max(nLpos,$(this).position().left);
					break;		
				case "center":
					aLpos.push($(this).position().left);
					break;
				case "top":
					nTpos = Math.min(nTpos,$(this).position().top);
					break;
				case "bottom":
					nTpos = Math.max(nTpos,$(this).position().top);
					break;
				case "middle":
					aTpos.push($(this).position().top);
					break;
			}
		};
	});
	switch(e.data.align){
		case "left":
		case "right":
			//$eqContainerSelected.css({left: nLpos / topoPic.scale});
			$eqContainerSelected.each(function(){
				if( $(this).attr("alt") != "true" ){
					$(this).css({left: nLpos / topoPic.scale});
				}
			});
			break;
		case "center":
			var maxPos = getArrayMax(aLpos);
			var minPos = getArrayMin(aLpos);
			var left_pos = (minPos+(maxPos-minPos)/2) / topoPic.scale;
			//$eqContainerSelected.css({left:left_pos });
			$eqContainerSelected.each(function(){
				if( $(this).attr("alt") != "true" ){
					$(this).css({left:left_pos });
				}
			});
			break;
		case "top":
		case "bottom":
			//$eqContainerSelected.css({top: nTpos / topoPic.scale});
			$eqContainerSelected.each(function(){
				if( $(this).attr("alt") != "true" ){
					$(this).css({top: nTpos / topoPic.scale});
				}
			});
			break;
		case "middle":
			var maxPos = getArrayMax(aTpos);
			var minPos = getArrayMin(aTpos);
			var top_pos = (minPos+(maxPos-minPos)/2) / topoPic.scale;
			//$eqContainerSelected.css({top: top_pos});
			$eqContainerSelected.each(function(){
				if( $(this).attr("alt") != "true" ){
					$(this).css({top: top_pos});
				}
			});
			break;
	};//end switch;
	
	function getArrayMax(list){
		list.sort();
		return list[list.length-1];
	}
	
	function getArrayMin(list){
		list.sort();
		return list[0];
	}
	//循环所有线条;
	/* var aLines = topoPic.eqDataAll.link,
	aLinesId = [];//记录线条的id;存入{id:"",srcEntityId:,destEntityId:};
	for(var i=0; i<aLines.length; i++){//循环线条并循环记录的所有id;
		for(var j=0; j<aEqId.length; j++){
			if(aLines[i].srcEntityId == aEqId[j].id || aLines[i].destEntityId == aEqId[j].id){
				var bHasOne = false;
				for(var k=0; k<aLinesId.length; k++){//如果不存在此id,那么加入数组;
					if(aLines[i].id == aLinesId[k].id){
						bHasOne = true;
						break;
					}
				}
				if(!bHasOne){
					var tempO = {
						id: aLines[i].id,
						srcEntityId: aLines[i].srcEntityId,
						destEntityId: aLines[i].destEntityId
					}
					aLinesId.push(tempO);
				}
			}
		}
	};//end for;
	//alert(aLinesId.length)
	var needData = [];
	for(var i=0; i<aLinesId.length; i++){
		var o = {
			id: aLinesId[i].id
		};
		$(".eqContainer").each(function(){
			var eqId = $(this).attr("name");
			//alert(eqId + "::::" + aLinesId[i].srcEntityId)
			if(eqId == aLinesId[i].srcEntityId){
				o.x1 = $(this).position().left + topoPic.halfIcoWidth;
				o.y1 = $(this).position().top + topoPic.halfIcoHeight;
			}
			if(eqId == aLinesId[i].destEntityId){
				o.x2 = $(this).position().left + topoPic.halfIcoWidth;
				o.y2 = $(this).position().top + topoPic.halfIcoHeight;
			}
		});
		needData.push(o);
	}
	topoPic.upDateLinePosition(needData,topoPic); */
	topoPic.upDateAllLinePosition(topoPic);
};//end iconAlignLeftFn;

//拖动，画线，拉框图标选择一个;
function chageSelectIco(obj){
	$("#sDrawLine, #sDrag, #sDrawRect").removeClass("normalBtnIcoSelect");
	obj.addClass("normalBtnIcoSelect");
}
//在第2行显示提示文字;
function toolTipTxt(t){
	$("#toolTipTxt").text(t);
}
//搜索框查询;
function searchInputEnter(){
	$("#topoSearchLinkBtn").bind("click",searchInputFn);
	$("#topoSearchInput").keydown(function(e){//按回车键，选中某个;
        var key = e.which;
        if (key == 13) {
        	searchInputFn();	
        }	
    });//end keydown;
};//end searchInputFn;
function searchInputFn(){
	$("#icoWrap .eqContainer").removeClass("eqContainerSelected");
	var match = $.trim($("#topoSearchInput").val());
	if(match=='') {window.top.showMessageDlg("@NETWORK.error@", "@NETWORK.findMsgError@", 'error'); return;}
	match = match.toLowerCase();
	//循环图标设备;
	var aSelected = [];
	var flashArr = [];
	$("#"+ topoPic.renderIcoDiv + " .eqContainer").each(function(){
		var $this = $(this);
		if (($this.find("span.theName").text().toLowerCase().indexOf(match) != -1) || 
			($this.attr("ip") && $this.attr("ip").indexOf(match) != -1) || 
			($this.attr("sysName") && $this.attr("sysName").toLowerCase().indexOf(match) != -1)) {
			$this.addClass("eqContainerSelected");
			aSelected.push($this);
			var o = {
				x: $this.position().left + topoPic.halfIcoWidth,
				y: $this.position().top + topoPic.halfIcoHeight
			};
			flashArr.push(o);
		}
	})
	if (aSelected.length == 0) {
		//window.top.showMessageDlg("@COMMON.tip@", "@NETWORK.notFindEntity@");
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
			html: '@NETWORK.notFindEntity@'
		});
	}else if(aSelected.length == 1){
		var $selected = aSelected[0];
		var $wrap = $("#wrap");
		var containerH = $wrap.height();
		var containerW = $wrap.width();
		var boxH = $selected.offset().top + $selected.outerHeight();
		var boxW = $selected.offset().left + $selected.outerWidth();
		if(boxH > containerH || (boxH - $wrap.scrollTop()) < containerH ){//上下不在屏幕内;
			var outerS = boxH - containerH;//最后要滚动到的地方;
			var nowS = $wrap.scrollTop();
			var mainTop;
			if(nowS < outerS){
				mainTop = outerS + nowS;
			}else if(nowS > outerS){
				mainTop = nowS + outerS;
			}
			$wrap.scrollTop(mainTop);
		}
		if(boxW > containerW || (boxW - $wrap.scrollLeft()) < containerW ){//左右不在屏幕内;
			var outerS = boxW - containerW;//最后要滚动到的地方;
			var nowS = $wrap.scrollLeft();
			var mainLeft;
			if(nowS < outerS){
				mainLeft = outerS + nowS;
			}else if(nowS > outerS){
				mainLeft = nowS + outerS;
			}
			$wrap.scrollLeft(mainLeft);
		}
		$("#putSwf").get(0).addIcoSelected(flashArr);
		$selected.addClass("eqContainerSelected");//调用swf动画;
	}else if(aSelected.length < 100){
		$("#putSwf").get(0).addIcoSelected(flashArr);//调用swf动画;
	}
};//end searchInputFn;
//拓扑图已经打开的情况下，定位某个设备;
function goToEntity(para){
	gotoId = para;
	firstPositionEqFn();
}
//进入地域就定位某个设备;
function firstPositionEqFn(){
	$("#icoWrap .eqContainer").removeClass("eqContainerSelected");
	var $selected = $("#"+ topoPic.renderIcoDiv + " .eqContainer[name='"+ gotoId +"']");
	if($selected.length == 1){
		var $wrap = $("#wrap");
		var containerH = $wrap.height();
		var containerW = $wrap.width();
		var boxH = $selected.offset().top + $selected.outerHeight();
		var boxW = $selected.offset().left + $selected.outerWidth();
		if(boxH > containerH || (boxH - $wrap.scrollTop()) < containerH ){//上下不在屏幕内;
			var outerS = boxH - containerH;//最后要滚动到的地方;
			var nowS = $wrap.scrollTop();
			var mainTop;
			if(nowS < outerS){
				mainTop = outerS + nowS;
			}else if(nowS > outerS){
				mainTop = nowS + outerS;
			}
			$wrap.scrollTop(mainTop);
		}
		if(boxW > containerW || (boxW - $wrap.scrollLeft()) < containerW ){//左右不在屏幕内;
			var outerS = boxW - containerW;//最后要滚动到的地方;
			var nowS = $wrap.scrollLeft();
			var mainLeft;
			if(nowS < outerS){
				mainLeft = outerS + nowS;
			}else if(nowS > outerS){
				mainLeft = nowS + outerS;
			}
			$wrap.scrollLeft(mainLeft);
		}
		$selected.addClass("eqContainerSelected");//调用swf动画;
		var arr = [{
				x: $selected.position().left + topoPic.halfIcoWidth,
				y: $selected.position().top + topoPic.halfIcoHeight
			}];
		try{
			$("#putSwf").get(0).addIcoSelected(arr);
		}catch(err){
			
		}
		
	};//end if;
};//end firstPositionEqFn;

//打开或关闭侧边栏; 
function openCloseSidePart(para){
	$("#topoGraphSidePartArr").css("display","block");
	if(para == "open"){
		if($("#topoGraphSidePartArr").hasClass("sideMapLeft")){//如果本来就是打开的;
			return;
		}
		$("#topoGraphSidePartArr").animate({left:380});
		$("#topoGraphSidePart").css("display","block").css("left",-380).animate({left:0},function(){
			$("#topoGraphSidePartArr").css("left",380).attr("class","sideMapLeft");	
		});
		
	}else{
		$("#topoGraphSidePartArr").animate({left:0});
		$("#topoGraphSidePart").animate({left:-380},function(){
			$(this).css("display","none");
			$("#topoGraphSidePartArr").css("left",0).attr("class","sideMapRight");
		})
					
	}
};//end openCloseSidePart;
//显示侧边栏 ，属性部分内容;
function showSidePart(url){
	url = "../" + url;
	$("#sidePart").attr("src", url);
	openCloseSidePart("open");
};
/**************************************
** 原来就有的功能;
**************************************/
//拓扑图属性;
function onPropertyClick() { 
	showSidePart('topology/getTopoFolderProperty.tv?nodeId=' + topoFolderId + '&isRegion=1'); 
}
//点击选择一根线条;
function selectOneLine(aLines){
	aSelectedLines.length = 0;
	aSelectedLines = aLines;//将选中的给全局变量;
	var oldId = $(".topBarSelected").attr("id");
	showTopMenu(oldId, "onlySelectOneLine");
}
//线条属性;
function linePropertyFn(){
	if(aSelectedLines.length == 1){
		var lineId = aSelectedLines[0].id;
		var entityId = lineId.slice(4,lineId.length),
		url = 'link/showLinkPropertyJsp.tv?linkId=' + entityId;
		showSidePart(url);
	}
};//end linePropertyFn;
/**
 * 加载全网数据
 * @param gotoId
 * @param refreshed
 */
function loadMapData(gotoId, refreshed) {
	$.ajax({url: 'loadVertexByFolderId.tv', dataType: 'json', cache: 'false',
		data: {folderId: topoFolderId, refreshed: refreshed},
		success: function(json){
			window.eqData = json;
			init();
			restoreTopologyState();//加载告警信息;
			startTimer();
			setTimeout('setCellCluetip()',2000);
			//setInterval('setCellCluetip()', dispatcherInterval);//每秒钟就更新一下hover提示框信息;
		},
		error: function(){
			window.top.showErrorDlg();
		}
	});
}
function setCellCluetip(){
	//if(displayCluetip == false){return;}
	aBubbleHtml.length = 0;
	if(topoPic.eqDataAll.entity){
		var aEntitys = topoPic.eqDataAll.entity;
		for(var i=0; i<aEntitys.length; i++){
			var theId = aEntitys[i].id;
			delegateFn(window, theId, aEntitys[i].objType, aEntitys[i].userObjId)();
		}
	}		
};//end setCellCluetip;
function delegateFn(me,theId,para1,para2){
	return function(){
		delegateFn2.call(me,theId,para1,para2)
	}
}
function delegateFn2(theId,para1,para2){
	$.ajax({
		url:'../topology/loadNodeTip.tv?userObjType=' +	para1 +  '&userObjId=' + para2 + '&folderId=' + topoFolderId,
		dataType:'html', cache: 'false',
		success: function(data){
			aBubbleHtml.push({
				id: theId,
				html: data
			})
		},
		error:function(){
			
		}
	})
}

function saveFn(){
	var nodeIds = [],
	x = [],
	y = [],
	$ico = $("#" + topoPic.renderIcoDiv + " div.eqContainer"),
	max = 50,
	size = $ico.length;
	if(size > max){
		window.top.showWaitingDlg("@COMMON.waiting@", "@NETWORK.saveEntityCoord@");
	}
	$ico.each(function(){
		var $this = $(this);
		nodeIds.push($this.attr("nodeId"));
		x.push( Math.round($this.position().left / topoPic.scale) );
		y.push( Math.round($this.position().top / topoPic.scale) );
	})
	var lee = 1000;
	
	$.ajax({url: '../topology/saveCoordinateByIds.tv', type: 'POST', dataType: 'plain', cache: false,
		data: {folderId: topoFolderId, nodeIds: nodeIds, x: x, y: y},
		success: function(){
			if(size > max){
				window.top.closeWaitingDlg();
			}
			top.afterSaveOrDelete({
				title: "@topo.saveSuccess@",
				html: "@topo.saveSuccessTip@"
			})
		},
		error: function(){
			top.afterSaveOrDelete({
				title: "@topo.saveFail@",
				html: "@topo.saveFailTip@"
			})
		}
	});
};//end saveFn;
/////////////////////////////////////////////////////////////////////////////////////////////复制、剪切和粘贴;
//复制和剪切;
//参数为copy或者cut;
function onCopyOrCutClick(e) {
	var entityIds = [];
	$("#" + topoPic.renderIcoDiv + " .eqContainerSelected").each(function(){
		entityIds.push(Number( $(this).attr("nodeid")) );
	});
	if (entityIds.length == 0) return;
	window.top.setZetaClipboard({type: e.data.type, target: 'topoFolder', src: topoFolderId, entityIds: entityIds});
};
//粘贴;
function onPasteClick() {
    var clipboard = window.top.getZetaClipboard();
    if (clipboard != null) {
		if (clipboard.src == topoFolderId) {
			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : "@NETWORK.notEntityIdPaste@"
			});
            //window.top.showMessageDlg("@COMMON.tip@", "@NETWORK.notEntityIdPaste@");
            return;
        }
		//先用flag判断是否地域上已经有这些设备了;
        if(topoPic.eqDataAll.entity){
        	var b = false;
        	var aEqs = topoPic.eqDataAll.entity;
	        for (var i=0; i<clipboard.entityIds.length; i++) {
	        	var $eq = $("#"+ topoPic.renderIcoDiv + " .eqContainer[nodeid='" + (clipboard.entityIds)[i] + "']"); 
	            if ( $eq.length == 0 ) {
	                b = true;
	            }
	        }
	        if (!b) {
	        	top.afterSaveOrDelete({
					title : "@COMMON.tip@",
					html : "@NETWORK.notEntityIdPaste@"
				});
	        	//window.top.showMessageDlg("@COMMON.tip@", "@NETWORK.notEntityIdPaste@");
	            return;
	        }
        }
        var folderId = clipboard.src;
		if (clipboard.type == 'copy') {
			 $.ajax({
				url: '../topology/copyEntityById.tv', type: 'POST', dataType: 'json', cache: false,
				data: {folderId: folderId, destFolderId: topoFolderId, entityIds: clipboard.entityIds},
				success: function(json) {
					//添加新的图标和线条,暂不知道有没有图形;
					refreshFn();
					/* if(json.entity){
						topoPic.addNewIco(json.entity);
					}
					if(json.link){
						refreshFn();
						//topoPic.addNewLine(json.link);
					} */
				},
				error: function() {
					window.top.showErrorDlg();
				}
			});
		} else if (clipboard.type == 'cut') {
			$.ajax({
				url: '../topology/cutEntityById.tv', type: 'POST', dataType: 'json', cache: false,
				data: {folderId: folderId, destFolderId: topoFolderId, entityIds: clipboard.entityIds},
				success: function(json) {
                    try {
                    	//添加新的图标和线条,暂不知道有没有图形;
                    	refreshFn();
						/* if(json.entity){
							topoPic.addNewIco(json.entity);
						}
						if(json.link){
							refreshFn();
							//topoPic.addNewLine(json.link);
						} */
                    } catch(ex) {
                    }
                    var frame = window.top.getFrame('topo' + folderId);
					try {
						for (var i = 0; i < json.entity.length; i++) {
							frame.synRemoveVertex(json.entity[i].nodeId);
						}
					} catch (err) {
					}
				},
				error: function() {
					window.top.showErrorDlg();
				}
			}); 
		}
		window.top.setZetaClipboard(null);
	} else {
        //window.top.showMessageDlg("@COMMON.tip@", "@NETWORK.notSrcPaste@");
        top.afterSaveOrDelete({
			title : "@COMMON.tip@",
			html : "@NETWORK.notSrcPaste@"
		});
    }
}

//剪切后移除图标和图标的连线,注意这个函数名不能修改;
function synRemoveVertex(arr){
	topoPic.removeIcos( [arr] );//一次只能弄一个，我的函数是一个数组全部搞定。
}
//////////////////////////////////////////////////////////////////////////////////////////////
/**
* （第一次？）获取拓扑图中状态信息等
*/
function restoreTopologyState() {
 	$.ajax({url: '../topology/getTopologyStateFirstly.tv', dataType: 'json', cache: false,
 		data: {folderId: topoFolderId, folderPath: folderPath, entityLabel: entityLabel, linkLabel: linkLabel},
    	success: dispatcherHandler
    });
}
function dispatcherHandler(json) {
	topoPic.addProInfo(json);
}	
//定时刷新属性信息;
function startTimer() {
	if(dispatcherTimer){
		clearInterval(dispatcherTimer);
	}
	dispatcherTimer = setInterval('dispatcherCallback()', dispatcherInterval);
}
function stopTimer() {
	if (dispatcherTimer != null) {
		clearInterval(dispatcherTimer);
		dispatcherTimer = null;
	}
	dispatcherCount = 0;
}
/**
 * 每隔1个基本时间间隔就采集一次拓扑结构
 * 每隔30个基本时间间隔就刷新一次页面
 * target : cell周围的告警状态等,tooltip中的状态信息不是从这里读取
 */
function dispatcherCallback() {
	if (++dispatcherCount > 30) {
		dispatcherCount = 0;
		refreshFn();
	} else {
		//alert(folderPath + ":"+ entityLabel + ":" + linkLabel)
	 	$.ajax({url: '../topology/getTopologyStateNewly.tv', dataType: 'json', cache: false,
	 		data: {folderId: topoFolderId, folderPath: folderPath, entityLabel: entityLabel, linkLabel: linkLabel},
	    	success: updateTopoState,
	    	error: function(){}
	    });
    }
};
	
//更新告警信息
function updateTopoState(json){
	$(".eqContainer .nm3kTopoPercent").remove();
	$(".eqContainer .nm3kTopoAlert").remove();
	topoPic.addProInfo(json);
	if(displayCluetip){//更新cpu或MEM百分比信息;
		setCellCluetip();
	}
}

//改变所有线条的宽度;
function synUpdateLinkWidth(paraWidth){
	topoPic.lineWidth = paraWidth;
	topoPic.changeAllLineWidth(paraWidth)
}
//改变所有线条的颜色;
function setDefaultEdgeColor(color){
	topoPic.lineStroke = color;
	topoPic.changeAllLineColor(color);
}
//是否显示线条;
function setDisplayLink(visible){
	if(visible){
		topoPic.lineOpacity = 1;
	}else{
		topoPic.lineOpacity = 0;
	}
	topoPic.changeAllLineVisible(visible);
};//end setDisplahLinkLabel;
//改变背景repeat;
function setBackgroundPosition(v){
	var repeat;
	switch(Number(v)){
		case 0:
			repeat = "no-repeat";
			break;
		case 1:
			repeat = "no-repeat center center";
			break;
		case 2:
			repeat = "repeat";
			break;
	}
	backgroundRepeat = repeat;
	var bg = backgroundColor + " url(" + backgroundImg + ") " + backgroundRepeat;
	$("#wrap").css({background: bg});
}
//改变背景图片;
function setBackgroundFlag(flag, background){
	backgroundImg = background;
	var bg = backgroundColor + " url(" + backgroundImg + ") " + backgroundRepeat;
	$("#wrap").css({background: bg});
}
//网格背景;
function setBoxBg(){
	var bg = "#F7F9F8 url(../images/boxBg.jpg) repeat fixed";
	$("#wrap").css({background: bg});
}
function setDisplayGridFlag(flag) {
	displayGrid = flag;
	if(displayGrid == true){
		setBoxBg();
	}
}
function setImageOrColorFlag(flag){};
//只显示颜色;
function setBackgroundColor(color){
	backgroundColor = color;
	$("#wrap").css({background: backgroundColor});
};
function showBackground(){}
function setBackgroundImg(para){
	backgroundImg = para;
	var bg = backgroundColor + " url(" + backgroundImg + ") " + backgroundRepeat;
	$("#wrap").css({background: bg});
}
//显示别名或名称，或ip;
function setDisplayName(showType){
	showType = Number(showType);		
	$(".eqContainer").each(function(){
		var $this = $(this);
		var showName;
		if($this.attr("type") != "2"){//子地域不参与;
			switch(showType){
				case 2://显示别名;
					showName = $this.attr("text");
					break;
				case 1://显示名称;
					showName = $this.attr("sysName");
					break;
				case 0://显示ip;
					showName = $this.attr("ip")
					break;
			}
			$this.find(".theName").text(showName);
		}
	})
};
//设置是否显示mouseenter的气泡;
function setCluetipEnabled(b){
	displayCluetip = b;
}
//是否显示告警;
function setDisplayAlertIcon(b){
	displayAlertIcon = b;
	showHideAlert();
}
function showHideAlert(){
	if(displayAlertIcon == false){
		$("#icoWrap").addClass("hideAlert")
	}else{
		$("#icoWrap").removeClass("hideAlert")
	}
}
//是否显示百分比;
function setDisplahEntityLabel(b){
	displayEntityLabel = b;
	showHideLabel();
}
function showHideLabel(){
	if(displayEntityLabel == false){
		$("#icoWrap").addClass("hidePercent");
	}else{
		$("#icoWrap").removeClass("hidePercent");
	}
}
//改变拓扑图尺寸;
function resizeTopoFolderSize(w,h){
	refreshFn();
}
//改变刷新频率;
function setMapRefreshInterval(i, folderId) {
	if (folderId == topoFolderId) { 
		stopTimer();
		dispatcherInterval = i; 
		startTimer(); 
	} 
}
//返回选中的数组;
function returnSelected(){
	var selectId = $(".eqContainerSelected").attr("id");
	var aEqDataAll = topoPic.eqDataAll.entity;
	var selectee = null;
	for(var i=0; i<aEqDataAll.length; i++){
		if(aEqDataAll[i].id == selectId){
			selectee = aEqDataAll[i];
		}
	}
	return selectee;
}
//刷新设备;
function onDiscoveryAgainClick() {
	var selectId = $(".eqContainerSelected").attr("id");
	var aEqDataAll = topoPic.eqDataAll.entity;
	var selectee = null;
	for(var i=0; i<aEqDataAll.length; i++){
		if(aEqDataAll[i].id == selectId){
			selectee = aEqDataAll[i];
		}
	}
	if (selectee != null) {
		if(EntityType.isCcmtsWithoutAgentType(selectee.typeId)){
			window.parent.showWaitingDlg("@COMMON.waiting@", String.format("@NETWORK.dicoveryingEntityAgain@", ""),'waitingMsg','ext-mb-waiting');
			$.ajax({
    			url:'../cmc/refreshCC.tv?cmcId='+ selectee.nodeId+'&cmcType='+selectee.typeId,
	  			type:'POST',
	  			dateType:'json',
	  			success:function(response){
        			if(response == "success"){
        				window.parent.showMessageDlg("@MENU.tip@", "@NETWORK.refreshEntitySuccess@");
					}else{
						window.parent.showMessageDlg("@MENU.tip@", "@NETWORK.refreshEntityFail@");
					} 
	  			},
	  			error:function(){
					 window.parent.showMessageDlg("@MENU.tip@", "@NETWORK.refreshEntityFail@");
	  			},
	  			cache:false
    		});
			return;
		}
		window.top.discoveryEntityAgain(selectee.userObjId, selectee.ip, function() {
			refreshFn();
		});
	}
}
function changeEntityName(entityId, name) {
	var $sel = $(".eqContainer[name="+ entityId +"]");
	if($sel.length == 1){
		$sel.find(".theName").text(name);
		$sel.removeAttr("title");
		if(name.length > 14){
			$sel.attr("title", name)				
		}
	}else{
    	refreshFn();
	}
}
//在属性页面设置超链接,设置后也要将eqDataAll中的数据加上url属性;
function updateNodeName(id, name, url) {
	var $eq = $("#cell" + id);
	if($eq.length != 1){ //设备必须是唯一的，否则就是出错了，那就刷新页面;
		refreshFn();		
	}else{
		$eq.find(".theName").text(name);
		var num = getSelectedObjIndex( ("cell"+id) );
		if(num == null){refreshFn()};//没有在数据中找到，则刷新;
		var dataEq = topoPic.eqDataAll.entity[num];
		dataEq.name = name;
		dataEq.url = url;	
	}
}
function updateFolderName(id, name, url){
	var $eq = $("#cell" + id);
	if($eq.length != 1){ //设备必须是唯一的，否则就是出错了，那就刷新页面;
		refreshFn();		
	}else{
		$eq.find(".theName").text(name);
		var num = getSelectedFolderIndex( ("cell"+id) );
		if(num == null){refreshFn()};//没有在数据中找到，则刷新;
		var dataEq = topoPic.eqDataAll.folder[num];
		dataEq.name = name;
		dataEq.url = url;	
	}		
}

//重置图标;
function synUpdateNodeIcon(entityId, icon, w, h){
	$("#cell" + entityId).find("img").eq(0).attr({
		"src" : icon
	})
};
//修改子地域别名;
function renameFolderNameFn(para){
	$(".eqContainerSelected").find(".theName").text(para);
}
		