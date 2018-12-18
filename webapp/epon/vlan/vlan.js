var vlanList, //此OLT下VLAN 全部数据(不分页)
	vlanStore,
	vlanGrid;

/**
 * 初始化VLAN列表
 */
function initVlanTab(){
	//加载VLAN数据（同步请求）
	var sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledVlanToolbarBtn(vlanGrid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledVlanToolbarBtn(vlanGrid.getSelectionModel().getSelections().length);
	        }
	    }
	});
	var cm =  new Ext.grid.ColumnModel([
		sm,
   	    {header:'VLAN ID', dataIndex:'vlanIndex', align:'center', width: 0.1, sortable:true},
   	    {header: '@VLAN.vlanDesc@', width: 0.2, dataIndex:'oltVlanName', align:'center', sortable:true},
   	    {header: '<div class="txtCenter">TAGGED @VLAN.port@</div>', sortable:true, align:'left', width: 0.35, dataIndex: 'taggedPortIndexList', renderer: portListRender},
   	    {header: '<div class="txtCenter">UNTAGGED @VLAN.port@</div>', dataIndex:'untaggedPortIndexList', width:0.35, align:'left', renderer: portListRender},
  	    {header: "@COMMON.manu@", dataIndex:'vlanId', width:150, align:'center', fixed : true, renderer: vlanMenuRender}
   	]);
   	vlanStore = new Ext.data.JsonStore({
   		proxy : new Ext.ux.data.PagingMemoryProxy(vlanList),
   		baseParams: { start : 0,limit : pageSize },
   	    fields: ['vlanIndex', 'oltVlanName', 'taggedPort', 'topMcFloodMode', 'untaggedPort', 'vlanVifFlag', 'taggedPortIndexList', 'untaggedPortIndexList']
   	});
   	loadVlanList(function(){
   		vlanStore.loadData(vlanList);
   	});
   	
   	vlanGrid = new Ext.grid.GridPanel({
   		stripeRows: true,
   		columnLines: true,
   		bodyCssClass: 'normalTable',
   		border: false,
   		store: vlanStore,
   		sm: sm,
   		cm : cm,
   		tbar: new Ext.Toolbar({
	    	items: [
	    	    {xtype: 'tbspacer', width:5},
	    	    {xtype: 'textfield', id: "vlaninput"},
	    	    {xtype: 'tbspacer', width:2},
        		{text: '@VLAN.addVlan@', id: 'addVlan_button', iconCls: 'bmenu_new', handler: showAddVlan}, 
	         	'-', 
	         	{text: '@VLAN.refreshDeviceVlan@', id: 'refreshVlan_button', iconCls: 'bmenu_refresh', handler: refreshDeviceVlan},
        		'-', 
	         	{text: '@VLAN.deleteVlan@', id: 'deleteVlan_button', iconCls: 'bmenu_delete', handler: showDeleteVlan, disabled: true}
	       	]
     	}),
   		bbar: new Ext.PagingToolbar({
   			pageSize: pageSize,
   			store: vlanStore,
   	       	displayInfo: true, 
   	       	items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-'],
   	       	listeners : {
   	       		beforechange : function(){
   	       			disabledVlanToolbarBtn(0);
   	       		}
   	       	}
   		}),
   		viewConfig:{
   			forceFit: true
   		},
   		height: $('#center-container').height(),
	    renderTo: "vlanGridContainer"
   	});
   	
   	$('#vlaninput').attr('tooltip', '@VLAN.batchStrRule@').attr('placeHolder', "@VLAN.batchStrRule@");
   	$('#vlaninput').addClass('normalInput');
   	
   	//增加动态过滤
   	$("#vlaninput").keyup(function() {
   		disabledVlanToolbarBtn(0);
		var text = jQuery("#vlaninput").val();
		if (!text) {
			$("#vlaninput").css("border", "1px solid #8bb8f3");
			vlanStore.loadData(vlanList);
			return;
		}
		if (!isVlanIdStrValid(text)) {
			$("#vlaninput").css("border", "1px solid #ff0000");
			return;
		}
		$("#vlaninput").css("border", "1px solid #8bb8f3");
		//将查询字符串整理为数组形式
		var arr = convertVlanStrToArray(text);
		
		//显示过滤后的数据
		var selectVlanList = new Array();
		$.each(vlanList, function(index, item){
			if(arr.indexOf(item.vlanIndex) > -1){
				selectVlanList.push(item);
			}
		})
		vlanStore.loadData(selectVlanList);
	});
}

/**
 * 刷新VLAN列表页面
 */
function refreshVlanList(){
	var bbar = vlanGrid.getBottomToolbar(),
		activePage = bbar.getPageData().activePage;
	loadVlanList(function(){
		vlanStore.loadData(vlanList);
		$("#vlaninput").trigger('keyup');
		//跳转到指定页面
		bbar.changePage(activePage);
	});
}


/**
 * 获取当前OLT的VLAN列表
 */
function loadVlanList(callback) {
	$.ajax({
		url: '/epon/vlanList/loadVlanList.tv',
		type: 'POST',
		data: {
			entityId: entityId
		},
		dataType : "json",
		success : function(vlanJson) {
			vlanList = vlanJson;
			if(typeof callback === 'function'){
				callback();
			}
		},
		error : function(vlanJson) {
			window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.vlanLoadError@');
		},
		cache : false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}

function portListRender(v){
	if(v.length){
		var array = [];
		$.each(v, function(i, portIndex){
			array.push(convertPortIndexToStrWithType(portIndex));
		})
		var str = '<div class="wordBreakDiv">' + array.join(",") + '</div>';
		return str;
	}else{ 
		return v;
	}
} 


function vlanMenuRender(){
	return '<a class="yellowLink" href="javascript:showEditVlan();">@COMMON.edit@</a> / <a class="yellowLink" href="javascript:showDeleteVlan();">@COMMON.delete@</a>';
}

/**
 * 打开添加VLAN页面
 */
function showAddVlan(){
	var vlanIndex = $("#vlaninput").val();
	//合法性校验
	if(!isVlanIdStrValid(vlanIndex)){
		return window.parent.showMessageDlg("@COMMON.tip@", I18N.VLAN.inputBatchTip);
	}
	
	if ((vlanIndex.indexOf(",") != -1) || (vlanIndex.indexOf("-") != -1) ) {
		//批量添加VLAN
		window.top.createDialog('oltVlanBatchAdd', '@VLAN.add@' + 'VLAN', 800, 500, 
				'/epon/vlan/showVlanBatchAdd.tv?entityId=' + entityId + "&vidListStr=" + vlanIndex, null, true, true, function(){
			//重新加载VLAN数据
			refreshVlanList();
		});
	} else {
		//添加单个VLAN
		var vlanExist = false;
		for (i = 0; i < vlanList.length; i++) {
			if (vlanList[i].vlanIndex == $("#vlaninput").val()) {
				vlanExist = true;
			}
		}
		if (vlanExist == true) {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.vlanIdExist);
		} else {
			window.top.createDialog('setVlanName', I18N.VLAN.setVlan, 600, 280, '/epon/vlan/showVlanNameJsp.tv?entityId=' + entityId 
					+ "&vlanIndex=" + vlanIndex + "&modifyFlag=false", null, true, true, function(){
				//重新加载VLAN数据
				refreshVlanList();
			});
		}
	}
}

/**
 * 展示删除确认页面
 */
function showDeleteVlan(){
	//获取勾选的VLAN id
	var selected = vlanGrid.getSelectionModel().getSelections();
	if(!selected.length){
		return window.parent.showMessageDlg("@COMMON.tip@", '@VLAN.plsSelectVlanToDel@');
	}
	var delList = [], curVlan;
	for(var i=0, len=selected.length; i<len; i++){
		curVlan = selected[i].data;
		delList.push(curVlan.vlanIndex);
	} 
	var delTip = '@VLAN.confirmDelVlan@' + convertVlanArrayToAbbr(delList);
	window.parent.showConfirmDlg("@COMMON.tip@", delTip, function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg('@COMMON.wait@', '@VLAN.deleting@', 'ext-mb-waiting');
		deleteVlan(delList);
	});
}

/**
 * 删除VLAN
 * @param {Array} vlanIds 待删除的VLAN ID数组
 */
function deleteVlan(vlanIds){
	$.ajax({
		url: "/epon/vlanList/deleteVlan.tv",
		data: {
			entityId: entityId,
			vlanIdsStr: vlanIds.join(",")
		},
		dataType: "json",
		cache: false,
		success: function(failedList) {
			//failedList: 删除失败的VLAN列表
			if(failedList.length == 0){
				top.afterSaveOrDelete({        
					title: '@COMMON.tip@',        
					html: '<b class="orangeTxt">' + String.format('@VLAN.delVlanSuc@', convertVlanArrayToAbbr(vlanIds)) + '</b>'    
				});
			}else {
				top.showMessageDlg("@COMMON.tip@", "@VLAN.delFailedVlan@:"+convertVlanArrayToAbbr(failedList));
			}
			refreshVlanList();
		},
		error : function(json) {
			refreshVlanList();
			top.showMessageDlg("@COMMON.tip@", json.error || '@VLAN.delVlanError@');
		}
		
	});
}

/**
 * 展示编辑单个VLAN页面
 */
function showEditVlan(){
	//获取当前选中的VLAN
	var selected = vlanGrid.getSelectionModel().getSelections(),
		curVlan = selected[0].data;
	window.top.createDialog('setVlanName', '@VLAN.editVlan@', 600, 280, '/epon/vlan/showVlanNameJsp.tv?entityId='+entityId 
			+ '&vlanIndex='+curVlan.vlanIndex+ "&modifyFlag=true"+ "&oltVlanName=" + curVlan.vlanName
			+ "&topMcFloodMode=" + curVlan.topMcFloodMode, null, true, true, function(){
		//重新加载VLAN数据
		refreshVlanList();
	});
}

/**
 * 刷新设备VLAN数据
 */
function refreshDeviceVlan(){
	top.executeLongRequeset({
		url:"/epon/vlanList/refreshVlan.tv",
		data:{entityId:entityId},
		requestHandler: function(){
			//重新加载VLAN数据
			refreshVlanList();
			//如果其他页面已经初始化，也刷新
			sniTabInited && refreshSniPortList();
			ponTabInited && refreshPonList();
			uniTabInited && refreshUniVlanList();
			
			top.afterSaveOrDelete({
				title: "@COMMON.tip@",
				html: '<b class="orangeTxt">@VLAN.refreshDeviceVlan@@COMMON.success@!</b>'
			});
		},
		requestErrorHandler: function(){
			top.showMessageDlg("@COMMON.tip@", '@VLAN.refreshDeviceVlan@@COMMON.fail@!');
		}
	});
	
}
function disabledVlanToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
    	disabledBtn(['deleteVlan_button'], false);
    }else{
        disabledBtn(['deleteVlan_button'], true);
    }
};
