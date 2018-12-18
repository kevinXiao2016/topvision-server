var vlanList;
$(function(){
	//初始化页面布局
	new Ext.Viewport({
	    layout: 'border',
	    items: [{
	        region: 'north',
	        border: false,
	        contentEl: 'header-container',
	        height: 50
	      }, {
	    	  region: 'center',
	    	  border: false,
	    	  contentEl: 'center-container'
	      }, {
	    	  region: 'east',
	    	  border: false,
	    	  contentEl: 'extra-container',
	    	  width: 210
	      }, {
	    	  region: 'west',
	    	  border: false,
	    	  contentEl: 'ponTree-container',
	    	  width: 130
	      }]
	});
	
	//绑定resize事件
	$(window).resize(function() {
		resizePonTree();
		resizeTransparent();
		resizeQinQ();
		resizeTransfrom();
		resizeAggregation();
		resizeSCVID();
	});
	
	//初始化PON口树
	$.ajax({
		url: '/epon/oltPerfGraph/loadPonPortList.tv',
		data: {entityId: entityId},
		dataType : "json",
		success : function(ponList) {
			var setting = {
		        check: {
		            enable: false
		        },
		        view: {
		            dblClickExpand: false
		        },
		        data: {
		            simpleData: {
		                enable: true
		            }
		        },
		        callback: {
		            onClick: ponNodeClick
		        }
		    };
			ponZTree = $.fn.zTree.init($("#ponZTree"), setting, ponList);
			//选中当前PON口
			var curNode = ponZTree.getNodeByParam('ponId', ponId);
			ponZTree.selectNode(curNode);
		},
		error : function(response) {
			window.parent.showMessageDlg("@COMMON.tip@", '@VLAN.loadPonError@');
		},
		cache : false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
	
	resizePonTree();
	//初始化VLAN透传区域
	initVlanTransparent();
	//初始化QinQ区域
	initVlanQinQ();
	//初始化VLAN转换区域
	initVlanTransform();
	//初始化VLAN聚合区域
	initVlanAggregation();
	//初始化右侧SVID/CVID区域
	initExtraRegion();
	
	//加载该设备下的VLAN列表，用于添加规则校验
	loadVlanList();
})

function resizePonTree() {
	$('#ponTreeContainer').height($('#ponTree-container').height()-40)
}

/**
 * PON口树叶子节点点击时间
 * @param e 点击事件
 * @param treeId 节点id
 * @param treeNode 节点对象
 */
function ponNodeClick(e, treeId, treeNode){
	//获取对应PON口的VLAN信息 
	var nodePonId = treeNode.ponId;
	if(!nodePonId){
		return;
	}
	if(nodePonId == ponId){
		//未切换PON口，不做任何响应
		return;
	}
	ponId = nodePonId;
	portIndex = treeNode.id;
	//刷新数据区域
	refreshPonVlanConfig();
}

/**
 * 刷新PON VLAN配置数据
 */
function refreshPonVlanConfig(){
	//刷新VLAN透传区域
	refreshPonTransparent();
	//刷新QinQ信息
	refreshQinQ();
	//刷新VLAN转换信息
	refreshVlanTransform();
	//刷新VLAN聚合信息
	refreshVlanAggr();
	//刷新CVID/SVID信息
	refreshSCVID();
}

/**
 * 打开应用到其他PON口页面
 */
function showApplyToOtherPon(){
	window.top.createDialog('applyToOtherPon', '@VLAN.ponPort@@COMMON.maohao@' + convertPortIndexToStr(portIndex) + '@VLAN.cfgApplyToOther@', 800, 600, 
			'/epon/vlanList/showApplyToOtherPon.tv?entityId='+entityId+'&ponId='+ponId, null, true, true);
}

/**
 * 从设备获取数据
 */
function getDataFormDevice(){
	Ext.Ajax.timeout = 1800000;
    window.top.showWaitingDlg('@COMMON.wait@', '@VLAN.fetchingAgain@', 'ext-mb-waiting');
    $.ajax({
		url: '/epon/vlan/refreshVlanDataFromOlt.tv',
		cache:false,
	   	data: {entityId : entityId},
	    success: function() {
	    	//刷新成功，刷新数据
	    	top.closeWaitingDlg();
	    	top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: '@VLAN.fetchAgainOk@'
	    	});
	    	refreshPonVlanConfig()
            Ext.Ajax.timeout = 60000;
	    },
	    error: function(){
	    	Ext.Ajax.timeout = 60000;
	   		window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.fetchAgainError@')
	    }
	});
}

/**
 * 加载该设备下VLAN列表
 */
function loadVlanList(){
	$.ajax({
		url: '/epon/vlanList/loadVlanList.tv',
		type: 'POST',
		data: {
			entityId: entityId
		},
		dataType: "json",
		success: function(vlanJson) {
			vlanList = vlanJson;
		},
		error: function(vlanJson) {
			window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.vlanLoadError@');
		},
		cache: false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}

/**
 * 判断指定VLAN是否已经创建
 * @param vlanId
 * @returns {Boolean} 是否已经创建
 */
function isVlanExist(vlanId){
	var exist = false;
	for(var i=0; i< vlanList.length; i++){
		if(vlanList[i].vlanIndex == vlanId){
			exist = true;
			break;
		}
	}
	return exist;
}