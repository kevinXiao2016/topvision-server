$(function() {
	//CMC复位，地域设备树结构中CC状态置为离线, 地域CC列表中CC状态置为离线;
	var cmc_reset = top.PubSub.on(top.CmcTrapTypeId.CMC_RESET, function(data){
		sendTip(data);
	});
	
	//系统手动重启，刷新页面;
    var dol_reboot = top.PubSub.on(top.CmcTrapTypeId.DOL_REBOOT, function(data){
    	sendTip(data);
    });
    
    //CMTS断电，store重载;
    var cmts_power_off = top.PubSub.on(top.CmcTrapTypeId.CMTS_POWER_OFF, function(data){
    	sendTip(data);
    });
    
    //CMTS断纤，刷新页面;
    var cmts_link_lose = top.PubSub.on(top.CmcTrapTypeId.CMTS_LINK_LOSE, function(data){
    	sendTip(data);
    });
    
    //CMTS下线，刷新页面;
    var cmc_offline = top.PubSub.on(top.CmcTrapTypeId.CMTS_OFFLINE, function(data){
    	sendTip(data);
    });
	
	$(window).on('unload', function(){
		top.PubSub.off(top.CmcTrapTypeId.CMC_RESET, cmc_reset);
		top.PubSub.off(top.CmcTrapTypeId.DOL_REBOOT, dol_reboot);
		
		top.PubSub.off(top.CmcTrapTypeId.CMTS_POWER_OFF, cmts_power_off);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_LINK_LOSE, cmts_link_lose);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_OFFLINE, cmc_offline);
	});
});
function sendTip(data){
	if(data.message){
		HeadMessage.sendMsg(data.clearMessage || data.message);
	}
}

$(window).load(function(){
	//左侧可以拖拽宽度;
	var o1 = new DragMiddle({ id: "line", leftId: "sidePart", rightId: "mainPart", minWidth: 200, maxWidth:500,leftBar:true });
	o1.init();
});
//end window.onload;
//顶部toolBar;
var tb,             
//下拉菜单
topBarSubMenu=null;
var entityItems,tree,root;

//创建顶部工具栏;
function buildToolbar() {
	tb = new Ext.Toolbar();
	tb.render('toolbar');
	var items = [];
	items[items.length] = {text:"@COMMON.refresh@" , iconCls: 'bmenu_refresh', cls:'mL10', handler: refreshTree };
	tb.add(items);
	tb.doLayout();
}

//刷新树结构
function refreshTree(){
	/*
	tree.destroy(); 
    initTreeData();
    */
	window.location.href = window.location.href;
}

//默认选中根结点并加载所有设备
function selectTreeRoot(){
	//root.select();
	var record = tree.getNodeById(currentFolder);
	tree.getSelectionModel().select(record)
	//加载所有的设备
	loadInitData(currentFolder);
}

//准备左侧树菜单;
function initTreeData(){
	$.ajax({
		url : '/cmc/loadTopoFolder.tv',
		cache:false,
		dataType:'json',
		success : function(json) {
			entityItems = json || [];
			initTree(entityItems);
		},
		error : function() {
			var entityItems = [];
			initTree(entityItems);
			window.top.showMessageDlg("@COMMON.tip@","@resources/Config.pleaseCheckDBConnection@");
		}
	});
}
//创建左侧树形菜单;
function initTree(entityItems){
	var treeloader = new Ext.tree.TreeLoader({
		url : '/cmc/loadDeviceByFolderId.tv'
	});
	window.root =  new Ext.tree.AsyncTreeNode({
        draggable : false,
        children : entityItems,
        id:'0'
    });
	tree = new Ext.tree.TreePanel({
		renderTo : 'deviceGridDiv',
		id : 'leftTree',
		border : false,
		loader : treeloader,
		//selModel : selectionModel,
		root : root ,
		autoScroll : true,
		//useArrows : true,
		rootVisible : false
	});
	
	//默认选中根结点并加载所有设备
	selectTreeRoot();
	
	//加载树下级结点
	treeloader.on("beforeload",function(o,node,callback){
		this.baseParams = {folderId : node.attributes.folderId, cmcId : node.attributes.deviceId, loadType :　node.attributes.loadType}
	})
	
	//在点击时叶子结点和非叶子结点进行不同的处理
	tree.on("click",function(node){
		if(node.isLeaf()){
			showCmInfo(node.attributes.mac)
		}else{
			var loadType = node.attributes.loadType;
			if(loadType == 2){
				var $typeId = node.attributes.typeId;
				var $deviceType;
				if( EntityType.isCcmtsWithoutAgentType( $typeId ) ){
					$deviceType = EntityType.getOltType();
				}else{
					$deviceType = $typeId;
				}
				$("#putGridFrame").attr("src","/cmc/showCmDeviceList.tv?cmcId=" + node.attributes.deviceId + "&deviceType="+ $deviceType);
			}else if(loadType == 1){
				$("#putGridFrame").attr("src","/cmc/showTopoFolderCmcList.tv?folderId=" + node.attributes.folderId);
			}
		}
		node.expand();
	})
}

function showCmInfo(cmMac){
	window.top.addView("CmCpeQuery", '@ccm/CCMTS.CmList.cmCpeQuery@', "mydesktopIcon", "/cmCpe/showCmCpeQuery.tv?sourcePage=cmListPage&cmMac=" + cmMac, null, true);
}

function loadInitData(folderId){
    $("#putGridFrame").attr("src","/cmc/showTopoFolderCmcList.tv?folderId=" + folderId);
}

Ext.onReady(function() {
	//布局界面;
	function autoHeight(){
		var subH = $(window).height() - $("#toolbar").outerHeight();
		$("#subDiv").height(subH);
		var h = $(".wrapWH100percent").outerHeight();
		var leftTitH  = $("#sidePartTit").outerHeight();
		//padding:5px;
		var leftMainH = h - leftTitH - 10;
		if(leftMainH < 0){ leftMainH = 200;}
		$("#deviceGridDiv").height(leftMainH);
		
	}
	autoHeight();
	$(window).resize(function(){
		throttle(autoHeight,window);
	});
	//end resize;
	
	//resize事件增加 函数节流;
	function throttle(method, context){
		clearTimeout(method.tId);
		method.tId = setTimeout(function(){
			method.call(context);
		},100);
	}
	
	buildToolbar();
	initTreeData();
	
	var mainHeight = $("#mainPart").height();
	
});
