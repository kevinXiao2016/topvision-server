$(window).load(function(){
	//左侧可以拖拽宽度;
	var o1 = new DragMiddle({ id: "line", leftId: "sidePart", rightId: "mainPart", minWidth: 200, maxWidth:500,leftBar:true });
	o1.init();
});

var tb,entityItems,tree,root;             

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
	window.location.href = window.location.href;
}

//默认选中根结点并加载所有设备
function selectTreeRoot(){
	var record = tree.getNodeById(currentFolder);
	tree.getSelectionModel().select(record)
	//加载所有的设备
	 $("#putGridFrame").attr("src","/cmtopofolder/showTopoFolderCmList.tv?folderId=" + currentFolder);
}

//准备左侧树菜单;
function initTree(){
	$.ajax({
		url : '/cmtopofolder/loadTopoFolder.tv',
		cache:false,
		dataType:'json',
		success : function(json) {
			entityItems = json || [];
			buildTree(entityItems);
		},
		error : function() {
			var entityItems = [];
			buildTree(entityItems);
			window.top.showMessageDlg("@COMMON.tip@","@resources/Config.pleaseCheckDBConnection@");
		}
	});
}

//创建左侧树形菜单;
function buildTree(entityItems){
	var treeloader = new Ext.tree.TreeLoader({
		url : '/cmtopofolder/loadSubFolderById.tv'
	});
	root =  new Ext.tree.AsyncTreeNode({
        draggable : false,
        children : entityItems,
        id:'0'
    });
	tree = new Ext.tree.TreePanel({
		renderTo : 'deviceGridDiv',
		id : 'leftTree',
		border : false,
		loader : treeloader,
		root : root ,
		autoScroll : true,
		rootVisible : false
	});
	//默认选中根结点并加载所有设备
	selectTreeRoot();
	//加载树下级结点
	treeloader.on("beforeload",function(o,node,callback){
		this.baseParams = {folderId : node.attributes.folderId}
	})
	//在点击结点进行处理
	tree.on("click",function(node){
		$("#putGridFrame").attr("src","/cmtopofolder/showTopoFolderCmList.tv?folderId=" + node.attributes.folderId);
		node.expand();
	})
}

Ext.onReady(function() {
	//布局界面;
	function autoHeight(){
		var subH = $(window).height() - $("#toolbar").outerHeight();
		$("#subDiv").height(subH);
		var h = $(".wrapWH100percent").outerHeight();
		var leftTitH  = $("#sidePartTit").outerHeight();
		var leftMainH = h - leftTitH - 10;
		if(leftMainH < 0){ leftMainH = 200;}
		$("#deviceGridDiv").height(leftMainH);
		
	}
	autoHeight();
	$(window).resize(function(){
		throttle(autoHeight,window);
	});
	
	//resize事件增加 函数节流;
	function throttle(method, context){
		clearTimeout(method.tId);
		method.tId = setTimeout(function(){
			method.call(context);
		},100);
	}
	
	buildToolbar();
	initTree();
});
