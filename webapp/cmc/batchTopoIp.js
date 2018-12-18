$(window).load(function(){
	//左侧可以拖拽宽度;
	var o1 = new DragMiddle({ id: "line", leftId: "sidePart", rightId: "mainPart", minWidth: 200, maxWidth:500,leftBar:true,leftCallBack:resizeRightGridPanel });
	o1.init();
});//end window.onload;

function resizeRightGridPanel(){
	if(grid != null){
		var gridW = $("#deviceListShowDiv").width();
		var gridH = $("#line").height();
		grid.setSize(gridW, gridH);
	}	
}

var tb,             //顶部toolBar;
    topBarSubMenu=null;  //下拉菜单
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
	$("#deviceGridDiv").empty();
    initTreeData();
    */
	window.location.href = window.location.href;
}

//切换下拉菜单;
function initTopBarSubMenu(){
	topBarSubMenu = new Ext.menu.Menu({});
	var items = [];
	items.push({id:"view1", text:"@IPTOPO.ipOnly@", handler: ipDisplay });
	items.push({id:"view2", text:"@IPTOPO.nameOnly@", handler: nameDisplay });
	items.push({id:"view3", text:"@IPTOPO.ipAndName@", handler: ipAndNameDisplay });
	topBarSubMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
}

//只显示IP
function ipDisplay(){
	var data = entityItems[0].children;
	$.each(data, function(i, item){
		if(item.loadType == 1){
			item.text = item.ipSegment + item.countInfo;
		}
	})
	/*for(var item in data ){
		data[item].text = data[item].ipSegment + " (@IPTOPO.total@: " + data[item].totalNum + ", @IPTOPO.online@: " + data[item].onlineNum + ", @IPTOPO.offline@: " + data[item].offineNum + ")"; 
	}*/
	window.root.children = entityItems;
	window.root.reload();
	//默认选中根结点并加载所有设备
	selectTreeRoot();
}
//只显示别名
function nameDisplay(){
	var nameItems = entityItems;
	var data = nameItems[0].children;
	$.each(data, function(i, item){
		if(item.loadType == 1){
			item.text = item.displayName + item.countInfo;
		}
	})
	/*for(var item in data ){
		data[item].text = data[item].displayName + " (@IPTOPO.total@: " + data[item].totalNum + ", @IPTOPO.online@: " + data[item].onlineNum + ", @IPTOPO.offline@: " + data[item].offineNum + ")"; 
	}*/
	window.root.children = nameItems;
	window.root.reload();
	//默认选中根结点并加载所有设备
	selectTreeRoot();
}
//显示Ip和别名
function ipAndNameDisplay(){
	var ipAndNameItems = entityItems;
	var data = ipAndNameItems[0].children;
	$.each(data, function(i, item){
		if(item.loadType == 1){
			item.text = item.ipSegment + "[" + item.displayName + "]" + item.countInfo;
		}
	})
	/*for(var item in data ){
		data[item].text = data[item].ipSegment + "[" + data[item].displayName + "]"+ " (@IPTOPO.total@: " + data[item].totalNum + ", @IPTOPO.online@: " + data[item].onlineNum + ", @IPTOPO.offline@: " + data[item].offineNum + ")"; 
	}*/
	//initTree(nameItems);
	window.root.children = ipAndNameItems;
	window.root.reload();
	//默认选中根结点并加载所有设备
	selectTreeRoot();
}

//默认选中根结点并加载所有设备
function selectTreeRoot(){
	//root.select();
	var record = tree.getNodeById('-1');
	tree.getSelectionModel().select(record)
	//加载所有的设备
	loadInitData(-1);
}

//准备左侧树菜单;
function initTreeData(){
	$.ajax({
		url : '/cmc/loadIpSegmentTree.tv',
		cache:false,
		dataType:'json',
		success : function(json) {
			entityItems = json || [];
			initTree(entityItems);
		},
		error : function() {
			var entityItems = [];
			initTree(entityItems);
			window.parent.showMessageDlg("@COMMON.tip@","@resources/Config.pleaseCheckDBConnection@");
		}
	});
}
//创建左侧树形菜单;
function initTree(entityItems){
	var treeloader = new Ext.tree.TreeLoader({
		url : '/cmc/loadTreeRootByIpSegment.tv?_r='+Math.random()
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
		/*var type = node.attributes.type;
		if(type == 1){
			this.baseParams = {ipSegment : node.attributes.ipSegment, loadType : node.attributes.type}
		}else if(type == 2){
			this.baseParams = {cmcId : node.attributes.id, loadType : node.attributes.type}
		}*/
		this.baseParams = {ipSegment : node.attributes.ipSegment, cmcId : node.attributes.deviceId, loadType : node.attributes.loadType};
	})
	
	//在点击时叶子结点和非叶子结点进行不同的处理
	tree.on("click",function(node){
		if(node.isLeaf()){
			showCmInfo(node.attributes.mac)
		}else{
			var type = node.attributes.loadType;
			if(type == 2){
				var $typeId = node.attributes.typeId;
				var $deviceType;
				if( EntityType.isCcmtsWithoutAgentType( $typeId ) ){
					$deviceType = EntityType.getOltType();
				}else{
					$deviceType = $typeId;
				}
				$("#putGridFrame").attr("src","/cmc/showCmDeviceList.tv?cmcId=" + node.attributes.deviceId+"&deviceType="+ $deviceType);
			}else if(type == 1){
				$("#putGridFrame").attr("src","/cmc/showIpSegmentDeviceList.tv?ipSegment=" + node.attributes.ipSegment);
				//loadData(node.attributes.id);
			}
		}
		node.expand();
	})
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

function loadInitData(ipSegment){
	$("#putGridFrame").attr("src","/cmc/showIpSegmentDeviceList.tv?ipSegment=" + ipSegment);
}


var grid = null;
var store = null;
var pagingToolbar = null;

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
       displayInfo: true, items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-'
                       		]});
   return pagingToolbar;
}

function onRefreshClick() {
	store.reload();
}


function showEditPage(cmcId, folderId, name){
	window.top.createDialog('editDeviceInfo', '@IPTOPO.editDevice@' , 600, 370, '/cmc/showEditDevice.tv?cmcId='+cmcId+"&folderId="+folderId+"&deviceName="+name, null, true, true)
}



function renderSysUpTime(value, p, record) {
    if (value == "NoValue") {
        return "<img src='/images/canNot.png' class='nm3kTip' nm3kTip='@PERF.mo.notCollected@' />";
    }
    var baseTime = 100;
    var time = parseInt(record.data.sysUpTime / baseTime);
    var timeString;
    if (record.data.state == 1) {
        timeString = arrive_timer_format(time)
    } else {
        timeString = '@network/label.deviceLinkDown@';
    }
    return timeString;
}

function arrive_timer_format(s) {
    var t
    if (s > -1) {
        hour = Math.floor(s / 3600);
        min = Math.floor(s / 60) % 60;
        sec = Math.floor(s) % 60;
        day = parseInt(hour / 24);
        if (day > 0) {
            hour = hour - 24 * day
            t = day + "@resources/COMMON.D@" + hour + "@resources/COMMON.H@"
        } else {
            t = hour + "@resources/COMMON.H@"
        }
        if (min < 10) {
            t += "0"
        }
        t += min + "@resources/COMMON.M@"
        if (sec < 10) {
            t += "0"
        }
        t += sec + "@resources/COMMON.S@"
    }
    return t
}

//显示实时信息
function showRealTimeInfo(cmcId,name){
    window.parent.addView('entity-realTime' + cmcId, name, 'entityTabIcon',
            '/cmc/showCmcRealTimeData.tv?cmcId=' + cmcId);
}

function showCmInfo(cmMac){
	window.parent.addView("CmCpeQuery", '@ccm/CCMTS.CmList.cmCpeQuery@', "mydesktopIcon", "/cmCpe/showCmCpeQuery.tv?sourcePage=cmListPage&cmMac=" + cmMac, null, true);

}

Ext.onReady(function() {
	//布局界面;
	function autoHeight(){
		var subH = $(window).height() - $("#toolbar").outerHeight();
		$("#subDiv").height(subH);
		var h = $(".wrapWH100percent").outerHeight();
		var leftTitH  = $("#sidePartTit").outerHeight();
		//因为padding:5px;
		var leftMainH = h - leftTitH - 10;
		if(leftMainH < 0){ leftMainH = 200;}
		$("#deviceGridDiv").height(leftMainH);
	}
	autoHeight();
	$(window).resize(function(){
		throttle(autoHeight,window);
	});//end resize;
	
	//resize事件增加 函数节流;
	function throttle(method, context){
		clearTimeout(method.tId);
		method.tId = setTimeout(function(){
			method.call(context);
		},100);
	}
	
	buildToolbar();
	initTreeData();
	initTopBarSubMenu();
	
	var mainHeight = $("#mainPart").height();
});