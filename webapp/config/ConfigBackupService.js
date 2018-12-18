Ext.Ajax.timeout = 10000000;
var dmp = new diff_match_patch();
var GLB_CURRENT_NODE,tree;

function loadDeviceList(){
	$.ajax({
		url : '/configBackup/getAllDeviceList.tv',cache:false,dataType:'json',
		success : function(json) {
			entityItems = json || [];
			//lisence不支持CMTS模块,移除CMTS父节点;
			if(!supportCmc){
				$.each(entityItems, function(i, v){
					if(v.text == 'CMTS'){
						entityItems.remove(v);
					}
				});
			}
			initTree(entityItems);
		},
		error : function() {
			var entityItems = [];
			initTree(entityItems);
			window.parent.showMessageDlg("@COMMON.tip@","@Config.pleaseCheckDBConnection@");
		}
	});
}

function initTree(entityItems){
	var treeloader = new Ext.tree.TreeLoader({
		url : '/configBackup/loadStartConfigList.tv'
	});

	var root =  new Ext.tree.AsyncTreeNode({
        draggable : false,
        children : entityItems,
        id:'0'
    });

	var selectionModel = new Ext.tree.MultiSelectionModel({
		listeners : {
			selectionchange :　function(selectionModel , nodes) {
				var $length = nodes.length;
				var $node = nodes[0];
				switch ($length){
					case 1: 
						if( $node.isLeaf() ){
							loadFileText( $node );
						}
						break;
					case 2: 
						if( $node.isLeaf() ){
							//如果第二个选中的节点不是叶子节点,则取消选中
							if(!nodes[1].isLeaf()){
								nodes[1].unselect();
							}
						}else{
							$node.unselect();
						}
						break;
					case 3: 
						nodes[2].unselect();
						break;
				}
			}
		}
	})
	tree = new Ext.tree.TreePanel({
		renderTo : 'deviceGridDiv',
		border : false,
		loader : treeloader,
		selModel : selectionModel,
		root : root ,
		rootVisible : false
	})
	
	treeloader.on("beforeload",function(o,node,callback){
		this.baseParams = {filePath : getFilePath(node)}
	})
	
	tree.on("click",function(node){
		GLB_CURRENT_NODE = node;
		var $button = Ext.getCmp("exportBt");
		if(node.isLeaf()){
			$button.enable();
		}else{
			$button.disable();
		}
	})
	
	tree.on("contextmenu",function(node,e){
		GLB_CURRENT_NODE = node;
		if(!node.disabled){
			var selections = tree.getSelectionModel().getSelectedNodes();
			showNodeMenu(selections, node, e);
		}
	})
}

function showNodeMenu(selections, node, e){
	var $selectionModel =  tree.getSelectionModel();
	var items = [];
	if(selections.length < 2 && !node.isLeaf()){
		$selectionModel.clearSelections();
		node.select();
		if(node.attributes.dateDirectory){
			items.push({text : "@Config.applicationToDevice@" ,iconCls:"bmenu_equipment", handler : applyThisConfig,disabled:!operationDevicePower});
		}else if(node.attributes.isDeviceNode){
			items.push({text : "@Config.refreshTree@", iconCls:"bmenu_refresh", handler: function(){node.reload();}});
			items.push({text : "@Config.saveCurrentConfig@" ,iconCls:"bmenu_saveOk", handler : saveThisConfig,disabled:!operationDevicePower});
			items.push({text : '@Config.backupConfigFile@' ,iconCls:"bmenu_saveOk", handler : backThisConfig,disabled:!operationDevicePower});
		}
		items.push({text : "@Config.deleteConfigFile@" ,iconCls:"bmenu_delete",disabled:!operationDevicePower, handler : deleteConfigFile});
	}else{
		if(selections[0].isLeaf() && node.isLeaf() && selections[0] != node){
			$selectionModel.select(node, null, true);
			items.push({text : "@Config.compareTo@" ,iconCls:"bmenu_compare", handler : fileCompareAction});
		}else{
			$selectionModel.clearSelections();
		}
	}
	if(items.length == 0){
		return;
	}
	loadMenu(items,e);
}


function loadMenu(items,e){
	if(WIN.GLB_NODE_MENU == null){
		WIN.GLB_NODE_MENU = new Ext.menu.Menu({items : items});
	}else{
		WIN.GLB_NODE_MENU.removeAll();
		WIN.GLB_NODE_MENU.add(items);
	}
	WIN.GLB_NODE_MENU.showAt(e.getXY());
}


function loadFileText(node){
	if(!node.isLeaf()){
		return;
	}
	window.top.showWaitingDlg("@COMMON.waiting@", "@Config.configFileLoading@");
	$.ajax({
		url : '/configBackup/loadFileText.tv',cache : false ,dataType : 'json',
		data : {filePath: getFilePath(node)} , 
		success : function(json){
			window.parent.closeWaitingDlg();
			$("#fileShowDiv").html(json.text);
			WIN.$rawText = json.text;
			var str = node.parentNode.attributes.text + " >> " + node.attributes.text;
			if(node.parentNode.attributes.dateDirectory){
				str = node.parentNode.parentNode.attributes.text + " >> " + str;
			}
			$("#filePathDiv").text(str);
		},error : function(){
			window.parent.showMessageDlg("@COMMON.tip@", "@Config.loadConfigFileFail@");
		}
	});
}
function deleteConfigFile(){
	var $message = "@Config.confirmDeleteConfigFile@";
	if(GLB_CURRENT_NODE.attributes.dateDirectory){
		$message = "@Config.confirmDeleteAllConfigFile@";
	}else{
		$message = "@Config.confirmDeleteDevice@" + GLB_CURRENT_NODE.attributes.text + "@Config.allConfigFile@";
	}
	window.parent.showConfirmDlg("@COMMON.tip@", $message, function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.waiting@", "@Config.deletingConfigFile@");
		var path = getFilePath(GLB_CURRENT_NODE);
		$.ajax({
			url : '/configBackup/deleteFile.tv',
			data : {filePath : path, 
					entityId: GLB_CURRENT_NODE.parentNode.attributes.entityId
			}, cache : false ,
			success : function(){
				if(GLB_CURRENT_NODE.attributes.dateDirectory){
					GLB_CURRENT_NODE.remove();
				}else{
					GLB_CURRENT_NODE.reload();
				}
				top.afterSaveOrDelete({
					title: "@COMMON.tip@",			
					html: "@Config.deleteConfigFileSuccess@" 
				});
			},error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@Config.deleteConfigFileFail@");
			}
		});
	});
}
function getFilePath(node){
	var depth = node.getDepth();
	var path = "";
	while (depth > 2){
		path = node.attributes.text + "/" + path;
		node = node.parentNode;
		depth--;
	}
	path = node.attributes.entityId + "/" + path;
	path = path.substring(0, path.length - 1);
	return path;
}

/**
 * 进行文件对比
 * @type {event}
 */
function fileCompareAction(){
	var path = getFilePath(GLB_CURRENT_NODE);
	window.top.showWaitingDlg("@COMMON.waiting@", "@Config.comparing@")
	$.ajax({
		url : '/configBackup/loadFileText.tv',cache : false ,dataType : 'json',
		data : {filePath: path} , 
		success : function(json){
			window.parent.closeWaitingDlg();
			$("#compareText").css({display:'none'}).empty().html(json.text);
			var text1 =  WIN.$rawText;
			var text2 = json.text
			var d = dmp.diff_main(text1, text2);
			var ds = dmp.diff_prettyHtml(d,true)
			ds = ds.toLocaleLowerCase().replaceAll("&para;","").replaceAll("&lt;br&gt;","");
			$("#fileShowDiv").html(ds)
			var str = GLB_CURRENT_NODE.parentNode.attributes.text + " >> " + GLB_CURRENT_NODE.attributes.text;
			if(GLB_CURRENT_NODE.parentNode.attributes.dateDirectory){
				str = GLB_CURRENT_NODE.parentNode.parentNode.attributes.text + " >> " + str;
			}
			str = $("#filePathDiv").text().split("@Config.With@")[0] + "@Config.With@" + str + "@Config.compareResult@";
			$("#filePathDiv").text(str);
			$("#filePathDiv").append("<img style='margin-left:10px;' title='@Config.compareResultIntroduce@' src='/images/help.gif' onclick='helpImgClick()'/>");
		},error : function(){
			top.showMessageDlg("@COMMON.tip@", "@Config.loadConfigFileFail@");
		}
	});
}
function helpImgClick(){
	var $tip = $("#compareTip");
	if( $tip.hasClass("displayNone") ){
		$tip.removeClass("displayNone");
	}else{
		$tip.addClass("displayNone");
	}
}

function saveThisConfig(){
	var entityId = GLB_CURRENT_NODE.attributes.entityId,
		typeId = GLB_CURRENT_NODE.attributes.typeId,
		deviceIp = GLB_CURRENT_NODE.attributes.ip;
	if(!entityId){
		return;
	}
	window.top.showWaitingDlg("@COMMON.waiting@", "@Config.savingConfig@");
	$.ajax({
		url : '/configBackup/saveConfig.tv?',cache:false,
		data : {
			entityId : entityId,
			entityType : typeId,
			ip : deviceIp
		}, 
		timeout : 300000,
		success : function(json){
			if(json.success){
				GLB_CURRENT_NODE.reload();
				top.afterSaveOrDelete({
					title: "@COMMON.tip@",			
					html: "@Config.saveConfigSuccess@" 
				});
			}else{
				window.parent.showMessageDlg("@COMMON.tip@", "@Config.saveConfigFail@");
			}
		},
		error : function(){
			window.parent.showMessageDlg("@COMMON.tip@", "@Config.saveConfigFail@");
		}
	});
}
function backThisConfig(){
	var entityId =  GLB_CURRENT_NODE.attributes.entityId;
	var typeId =  GLB_CURRENT_NODE.attributes.typeId;
	var deviceIp =  GLB_CURRENT_NODE.attributes.ip;
	if(!entityId){
		return;
	}
	window.top.showWaitingDlg("@COMMON.waiting@", '@Config.fileBackuping@');
	$.ajax({
		url : '/configBackup/downLoadConfig.tv',cache:false,
		data : {
			entityId : entityId,
			entityType : typeId,
			ip : deviceIp
		}, 
		success : function(){
			GLB_CURRENT_NODE.reload();
			top.afterSaveOrDelete({
				title: "@COMMON.tip@",			
				html: "@Config.backupFileSuccess@" 
			});
		},
		error : function(){
			window.parent.showMessageDlg("@COMMON.tip@", '@Config.backupFileFailed@');
		}
	});
}
function applyThisConfig(){
	//找到该文件所对应的设备节点
	var node = GLB_CURRENT_NODE.parentNode;
	while(!node.attributes.isDeviceNode){
		node = node.parentNode;
	}
	var $attribute = node.attributes,
		entityId = $attribute.entityId,
		typeId = $attribute.typeId,
		deviceIp = $attribute.ip;
	window.parent.showConfirmDlg("@COMMON.tip@", "@Config.confirmApplicationToDevice@", function(type){
		if(type == "no")return;
		window.top.showWaitingDlg("@COMMON.waiting@", "@Config.applicationingToDevide@");
		$.ajax({
			url : '/configBackup/applyConfigToDevice.tv',cache:false,
			data : {
				entityId : entityId, 
				filePath: getFilePath( GLB_CURRENT_NODE ),
				entityType : typeId,
				ip : deviceIp
			},
			success : function(response){
				top.afterSaveOrDelete({
					title: "@COMMON.tip@",			
					html: "@Config.applicationToDeviceSuccess@" 
				});
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@Config.applicationToDeviceFail@");
			}
		});
	});
}


function exportBtClick(){
	window.location.href='/configBackup/downFile.tv?filePath=' + getFilePath(GLB_CURRENT_NODE);
}
function usedHistoryBtClick(){
	window.parent.createDialog('configFileImported', "@Config.configFileOperationLog@", 800, 500, '/configBackup/showConfigFileUsedList.tv',null,true,true);
}

/**
 * 创建顶部工具栏以及工具栏菜单
 */
function buildToolbar() {
	jtb = new Ext.Toolbar();
	jtb.render('toolbar');
	var items = [];
	items[items.length] = {text:"@Config.exportCurrentConfigFile@" ,id:'exportBt',disabled:true, iconCls: 'bmenu_export', handler: exportBtClick}
	items[items.length] = {text:"@Config.configFileApplicationLog@" ,iconCls: 'bmenu_view', handler: usedHistoryBtClick}
	items[items.length] = {text:"@platform/sys.fileAutoWrite@" ,iconCls: 'bmenu_compare', handler: viewFileAutoWriteClick}
	items.push("-");
	items[items.length] = {text:"@COMMON.refresh@" ,iconCls: 'bmenu_refresh', handler: refreshPage}
	jtb.add(items);
	jtb.doLayout();
}
function viewFileAutoWriteClick(){
	window.top.createDialog('modalDlg', "@platform/sys.autoWriteConfig@", 600, 370, '/configBackup/showAutoWriteConfig.tv', null, true, true);
}
function refreshPage(){
	window.location.href = window.location.href;
}

Ext.onReady(function(){
	buildToolbar();
	loadDeviceList();
	$("#compareTip").click(function(){
		$("#compareTip").addClass("displayNone")
	})
});

function showSideTip(){
	var $sideTip = $("#sideTip"),
	    $img = $(".questionTipLink");
	
	if($sideTip.length == 0){ //不存在提示框，先创建一个;
		createSideTip();
	}
	$sideTip.css({display: 'block'});
	var l = $img.offset().left + $img.width()/2 - $("#sideTip").outerWidth() / 2;
	var t = $img.offset().top + $img.height();
	$("#sideTip").css({left:l, top:t});
	
}
function createSideTip(){
	var str = [
		'<div id="sideTip">',
		'    <div class="sideTipArrow"></div>',
		'    <div class="sideTipContent">',
		'        <p class="sideTipTitle">@COMMON.tip@</p>',
		'        <p class="sideTipText">@Config.tipText@</p>',
		'        <p class="sideTipTime">@Config.tipTime@</p>',
		'    </div>',
		'    <a href="javascript:;" class="sideTipClose" onclick="closeSideTip()"></a>',
		'</div>'
	].join('');
	str = String.format(str, window.tip.second);
	$('body').append(str);
}
function closeSideTip(){
	if(tip.interval != null){
		window.clearTimeout(tip.interval);
		tip.interval = null;
	}
	$("#sideTip .sideTipTime").empty();
	$("#sideTip .sideTipClose").remove();
	$("#sideTip").css({display: 'none'});
}
var tip = {
	second : 10,     //提示信息倒计时10秒;
	interval : null, //存储interval;
	changeSecond : function(){
		this.second --;
		$("#sideTip .sideTipTime").find("b").text(this.second);
		if(this.second === 0){
			closeSideTip();
		}
	}
}










