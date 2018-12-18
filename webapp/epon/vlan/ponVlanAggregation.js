function initVlanAggregation(){
	//初始化聚合后VLAN表格
	var aggrSvidCm =  new Ext.grid.ColumnModel([
  	    {header:'@VLAN.trunkedVlan@ ID', dataIndex:'portAggregationVidIndex', align:'center', width: 90, sortable:true},
  	    {header:'@COMMON.manu@', dataIndex:'portAggregationVidIndex', align:'center', width: 40, sortable:true, renderer: aggrSvidMenuRender}
  	]);
	
	aggrSvidStore = new Ext.data.JsonStore({
		url: '/epon/vlan/loadAggrRule.tv',
		fields: ['portAggregationVidIndex', 'aggregationRowStatus', 'aggregationVidListAfterSwitch']
    })
	aggrSvidGrid = new Ext.grid.GridPanel({
		bodyCssClass: 'normalTable',
    	stripeRows:true,
   		viewConfig:{forceFit: true},
        renderTo: 'aggrSvlanGrid',
        height: $('#ponUnionContainer').height() - 60,
        width: $('#aggrSvlanGrid').width(),
        border: true,
        autoScroll: true,
        cm: aggrSvidCm,
        store : aggrSvidStore,
		listeners : {
			"rowclick" : {
				fn : aggrSvidGridClick,
				scope : this
			}
		}
    })
	
	//初始化聚合前的VLAN表格
	var aggrCvidCm =  new Ext.grid.ColumnModel([
  	    {header:'@VLAN.trunkVlan@ ID', dataIndex:'aggrCVlanId', align:'center', width: 90, sortable:true},
  	    {header:'@COMMON.manu@', dataIndex:'aggrCVlanId', align:'center', width: 50, sortable:true, renderer: aggrCvidMenuRender}
  	]);
	
	aggrCvidStore = new Ext.data.SimpleStore({
		data: [],
		fields: ['aggrSVlanId', 'aggrCVlanId']
    })
	aggrCvidGrid = new Ext.grid.GridPanel({
		bodyCssClass: 'normalTable',
    	stripeRows:true,
   		viewConfig:{forceFit: true},
        renderTo: 'aggrCvlanGrid',
        height: $('#ponUnionContainer').height() - 60,
        width: $('#aggrCvlanGrid').width(),
        border: true,
        autoScroll: true,
        cm: aggrCvidCm,
        store : aggrCvidStore
    })
	
	$('#aggregationSvlanId').on('keyup', function(){
		var svlanId = $('#aggregationSvlanId').val();
		if(svlanId && !isSingleVlanIdValid(svlanId)){
			addInValidStyle($('#aggregationSvlanId'));
			$('#aggregationSvlanId').focus();
			return false;
		}
		addValidStyle($('#aggregationSvlanId'));
		aggrSvidStore.filter('portAggregationVidIndex', svlanId);
		if(aggrSvidStore.getCount()){
			aggrSvidGrid.selModel.selectRow(0, true);
			aggrSvidGridClick(aggrSvidGrid, 0);
		}else{
			aggrCvidStore.loadData([]);
		}
	})
	
	aggrSvidStore.load({
		params: {ponId: ponId},
		callback: function(records){
			//选中第一行
			if(records.length){
				aggrSvidGrid.selModel.selectRow(0, true);
				aggrSvidGridClick(aggrSvidGrid, 0);
			} else {
				aggrCvidStore.loadData([]);
			}
		}
	});
}

/**
 * 刷新VLAN聚合
 */
function refreshVlanAggr() {
	aggrSvidStore.load({
		params: {ponId: ponId},
		callback: function(records){
			//选中第一行
			if(records.length){
				aggrSvidGrid.selModel.selectRow(0, true);
				aggrSvidGridClick(aggrSvidGrid, 0);
			} else {
				aggrCvidStore.loadData([]);
			}
		}
	});
}

function resizeAggregation(){
	aggrSvidGrid.setSize($('#aggrSvlanGrid').width(), $('#ponUnionContainer').height() - 60);
	aggrCvidGrid.setSize($('#aggrCvlanGrid').width(), $('#ponUnionContainer').height() - 60);
}

function aggrSvidMenuRender(portAggregationVidIndex){
	return String.format('<a class="yellowLink" href="javascript: deleteAggrRule({0});">@COMMON.delete@</a>', portAggregationVidIndex);
}

function aggrCvidMenuRender(aggrVlanId, p, record){
	return String.format('<a class="yellowLink" href="javascript: deleteAggrCvlanRule({0}, {1});">@COMMON.delete@</a>', record.data.aggrSVlanId, aggrVlanId);
}

function aggrSvidGridClick(grid, rowIndex, e){
	//获取选中的聚合后VLAN
	var selectedAggr = grid.getSelectionModel().getSelections()[0].data,
		curCvids = selectedAggr.aggregationVidListAfterSwitch;
	
	//store接受的最简单格式也是二维数组，进行转换
	var curArray = [];
	$.each(curCvids, function(i, id){
		curArray.push([selectedAggr.portAggregationVidIndex, id]);
	})
	aggrCvidStore.loadData(curArray);
}

/**
 * 添加VLAN聚合
 */
function addAggrClick(){
	var aggregationSvlanId = $('#aggregationSvlanId').val(),
		aggregationCvlanId = $('#aggregationCvlanId').val();
	
	if(!addAggrRuleValidate()){
		return;
	}
	
	var cvidArray = convertVlanStrToArray(aggregationCvlanId);
	
	// add by fanzidong，需要判断是否聚合后的VLAN。如果存在，则不是添加，而是modify
	var url = '';
	var data = {};
	if(aggrSvidStore.getCount()) {
		url = '/epon/vlan/modifyCvlanAggrRule.tv';
		var curCvlanArray = [];
		for(var i=0, len=aggrCvidStore.getCount(); i<len; i++) {
			curCvlanArray.push(aggrCvidStore.getAt(i).data.aggrCVlanId);
		}
		//将要添加的cvidArray加入数组
		for(var i=0, len=cvidArray.length; i<len; i++) {
			curCvlanArray.push(cvidArray[i]);
		}
		//排序
		curCvlanArray.sort(function(a, b) {
			return a-b;
		});
		data = {
			entityId : entityId,
			ponId : ponId,
			aggrSvid : aggregationSvlanId,
			aggrCvids : curCvlanArray.join(',')
		};
	} else {
		url = '/epon/vlan/addSvlanAggrRule.tv';
		data = {
			entityId : entityId,
			ponId : ponId,
			aggrSvid : aggregationSvlanId,
			aggrCvids : cvidArray.join(',')
		};
	}
	
	window.top.showWaitingDlg('@COMMON.wait@', String.format('@VLAN.addingAggVlanRule@', aggregationSvlanId), 'ext-mb-waiting');
	$.ajax({
		url : url,
		cache: false,
		data: data,
		success : function() {
			top.closeWaitingDlg();
			refreshVlanAggr();
			refreshSCVID();
	    	top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: String.format('@VLAN.alreadyAggredVlan@', convertVlanArrayToAbbr(cvidArray), aggregationSvlanId)
	    	});
		},
		error : function() {
			window.parent.showMessageDlg('@COMMON.tip@', String.format('@VLAN.addAggVlanRuleError@', aggregationSvlanId))
		}
	});
}

/**
 * 添加VLAN聚合的验证
 */
function addAggrRuleValidate(){
	var aggregationSvlanId = $('#aggregationSvlanId').val(),
		aggregationCvlanId = $('#aggregationCvlanId').val();
	
	if(!isSingleVlanIdValid(aggregationSvlanId)){
		$('#aggregationSvlanId').focus();
		return false;
	}
	if(!isVlanIdStrValid(aggregationCvlanId)){
		$('#aggregationCvlanId').focus();
		return false;
	}
	var cvidArray = convertVlanStrToArray(aggregationCvlanId);
	
	//验证VLAN是否存在
	var noExistVlanIds = [];
	if(!isVlanExist(aggregationSvlanId)){
		noExistVlanIds.push(aggregationSvlanId);
	}
	$.each(cvidArray, function(i, cvid){
		if(!isVlanExist(cvid)){
			noExistVlanIds.push(cvid);
		}
	});
	if(noExistVlanIds.length){
		top.showMessageDlg('@COMMON.tip@', String.format('@VLAN.vlanCountTip@', convertVlanArrayToAbbr(noExistVlanIds)));
		return false;
	}
	
	//验证CVID
	var existMsgs = [],
		existObj;
	$.each(cvidArray, function(i, cvid){
		existObj = isCvidExist(cvid);
		if(existObj.exist){
			existMsgs.push(existObj.msg);
		}
	});
	if(existMsgs.length){
		top.showMessageDlg('@COMMON.tip@', existMsgs.join('</br>'));
		return false;
	}
	
	//如果SVID没有配置过VLAN聚合，需进行SVID校验
	if(!aggrSvidStore.getCount()){
		var svidExistObj = isSvidExist(aggregationSvlanId);
		if(svidExistObj.exist){
			top.showMessageDlg('@COMMON.tip@', svidExistObj.msg);
			return false;
		}
	}
	return true;
}

/**
 * 删除聚合后的VLAN
 * @param aggrSVlanId
 */
function deleteAggrRule(aggrSVlanId){
	window.parent.showConfirmDlg('@COMMON.tip@', String.format('@VLAN.cfmDelVlanAggRule@', aggrSVlanId), function(type) {
		if (type == 'no') {
			return;
		}
		
		window.top.showWaitingDlg('@COMMON.wait@', String.format('@VLAN.delingAggRule@', aggrSVlanId) , 'ext-mb-waiting');
		$.ajax({
			url : '/epon/vlan/deleteSvlanAggrRule.tv?r=' + Math.random(),
			data: {
				entityId : entityId,
				ponId : ponId,
				aggrSvid : aggrSVlanId
			},
			success : function() {
				top.closeWaitingDlg();
				top.afterSaveOrDelete({
		            title: '@COMMON.tip@',
		            html: String.format('@VLAN.delAggRuleOk@', aggrSVlanId)
		    	});
				refreshVlanAggr();
				refreshSCVID();
			},
			error : function() {
				window.parent.showMessageDlg('@COMMON.tip@', String.format('@VLAN.delAggRuleError@', aggrSVlanId))
			}
		});
	});
}

/**
 * 删除VLAN聚合中的某个聚合前VLAN
 * @param aggrSVlanId
 * @param aggrCVlanId
 */
function deleteAggrCvlanRule(aggrSVlanId, aggrCVlanId){
	window.parent.showConfirmDlg('@COMMON.tip@', String.format('@VLAN.cfmDelAggRule@', aggrCVlanId, aggrSVlanId), function(type) {
		if (type == 'no') {
			return;
		}
		
		//整理出删除后应该存在的聚合前VLAN，下发modify
		var selectAggrSvlan = aggrSvidGrid.getSelectionModel().getSelections()[0].data,
			orginCvlans = selectAggrSvlan.aggregationVidListAfterSwitch,
			newCvlans = [];
		
		$.each(orginCvlans, function(i, vlanId){
			if(vlanId != aggrCVlanId){
				newCvlans.push(vlanId);
			}
		});
		
		window.top.showWaitingDlg('@COMMON.wait@',  String.format('@VLAN.delingVlanAggRule@', aggrCVlanId, aggrSVlanId), 'ext-mb-waiting')
		$.ajax({
			url : '/epon/vlan/modifyCvlanAggrRule.tv?r=' + Math.random(),
			data: {
				entityId: entityId,
				ponId: ponId,
				aggrSvid: aggrSVlanId,
				aggrCvids: newCvlans.join(',')
			},
			success : function() {
				top.closeWaitingDlg();
				top.afterSaveOrDelete({
		            title: '@COMMON.tip@',
		            html: String.format('@VLAN.delVlanAggRuleOk@', aggrCVlanId, aggrSVlanId)
		    	});
				refreshVlanAggr();
				refreshSCVID();
			},
			error : function() {
				window.parent.showMessageDlg('@COMMON.tip@', String.format('@VLAN.delVlanAggRuleError@', aggrCVlanId, aggrSVlanId))
			}
		});
	});
}