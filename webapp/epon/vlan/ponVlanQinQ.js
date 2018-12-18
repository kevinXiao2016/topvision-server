var qinqRuleMap = {},
	svidList = [];

function initVlanQinQ(){
	//初始化外层VLAN表格
	var qinqSvidCm =  new Ext.grid.ColumnModel([
  	    {header:'@VLAN.outerVlan@ ID', dataIndex:'pqSVlanId', align:'center', width: 70, sortable:true}
  	]);
	
	qinqSvidStore = new Ext.data.SimpleStore({
		data: svidList,
		fields: ['pqSVlanId']
    })
	
	qinqSvidGrid = new Ext.grid.GridPanel({
		bodyCssClass: 'normalTable',
    	stripeRows:true,
   		viewConfig:{forceFit: true},
        renderTo: 'qinqSvlanGrid',
        height: $('#ponQinQContainer').height() - 85,
        width: $('#qinqSvlanGrid').width(),
        border: true,
        autoScroll: true,
        cm: qinqSvidCm,
        store : qinqSvidStore,
		listeners : {
			"rowclick" : {
				fn : qinqSvidGridClick,
				scope : this
			}
		}
    })
	
	//初始化内层VLAN表格
	var qinqCvidCm =  new Ext.grid.ColumnModel([
  	    {header:'@VLAN.vlanStart@ ID', dataIndex:'pqStartVlanId', align:'center', width: 80, sortable:true},
  	    {header:'@VLAN.vlanTermi@ ID', dataIndex:'pqEndVlanId', align:'center', width: 80, sortable:true},
  	    {header:'@VLAN.vlanPriority@', dataIndex:'pqSTagCosDetermine', align:'center', width: 80, sortable:true, renderer: qinqPriRender},
  	    {header:'@COMMON.manu@', dataIndex:'', align:'center', width: 80, sortable:true, renderer: qinqMenuRender}
  	]);
	
	qinqCvidStore = new Ext.data.Store({
		proxy: new Ext.data.MemoryProxy([]),
		reader: new Ext.data.JsonReader({}, [
		    {name: 'pqStartVlanId'},
		    {name: 'pqEndVlanId'},
		    {name: 'pqSVlanId'},
		    {name: 'pqSTagCosDetermine'},
		    {name: 'pqSTagCosNewValue'},
		    {name: 'pqRowStatus'}
		])
    })
	
	qinqCvidGrid = new Ext.grid.GridPanel({
		bodyCssClass: 'normalTable',
    	stripeRows:true,
   		viewConfig:{forceFit: true},
        renderTo: 'qinqCvlanGrid',
        height: $('#ponQinQContainer').height() - 85,
        width: $('#qinqCvlanGrid').width(),
        border: true,
        autoScroll: true,
        cm: qinqCvidCm,
        store : qinqCvidStore
    })
	
	refreshQinQ();
}

/**
 * 刷新QinQ区域
 */
function refreshQinQ(){
	loadQinQRule(function(){
		$("#qinqInnerVlanStartId").val("");
		$("#qinqInnerVlanEndId").val("");
		$("#qinqOuterVlanId").val("");
		if(svidList.length){
			qinqSvidGrid.selModel.selectRow(0, true);
			qinqSvidGridClick(qinqSvidGrid, 0);
		}else{
			qinqCvidStore.loadData([]);
		}
	});
}

function resizeQinQ(){
	qinqSvidGrid.setSize($('#qinqSvlanGrid').width(), $('#ponQinQContainer').height() - 85);
	qinqCvidGrid.setSize($('#qinqCvlanGrid').width(), $('#ponQinQContainer').height() - 85);
}

function qinqPriRender(pqSTagCosDetermine, c, record){
	if(pqSTagCosDetermine == 2){
		return 'copy';
	}else{
		return record.data.pqSTagCosNewValue;
	}
}

function qinqMenuRender(v, p, r){
	var qinqRule = r.data;
	return String.format('<a class="yellowLink" href="javascript:deleteQinQRule({0}, {1}, {2});">@COMMON.delete@</a>'
			, qinqRule.pqSVlanId, qinqRule.pqStartVlanId, qinqRule.pqEndVlanId);
}

/**
 * 加载QinQ数据
 * @param callback
 */
function loadQinQRule(callback){
	$.ajax({
		url : '/epon/vlan/loadQinqRule.tv?ponId=' + ponId,
		type : 'POST',
		dataType : "json",
		cache : false,
		success : function(qinqRuleList) {
			//数据组织
			qinqRuleMap = {};
			svidList = [];
			$.each(qinqRuleList, function(i, qinqRule){
				var curQinQRule = qinqRuleMap[qinqRule.pqSVlanId];
				//判断是否已有该外层VLAN规则
				if(curQinQRule){
					curQinQRule.push(qinqRule);
				}else{
					svidList.push([qinqRule.pqSVlanId]);
					qinqRuleMap[qinqRule.pqSVlanId] = [qinqRule];
				}
			});
			qinqSvidStore.loadData(svidList);
			if(typeof callback === 'function'){
				callback();
			}
		},
		error : function(vlanJson) {
			window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.vlanLoadError@');
		},
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}

function qinqSvidGridClick(grid, rowIndex, e){
	//获取选中的外层VLAN ID
	var selectedSvid = grid.getSelectionModel().getSelections()[0].data;
	//加载指定的CVID
	qinqCvidStore.loadData(qinqRuleMap[selectedSvid.pqSVlanId]);
}

function addQinQRuleClick(){
	var startId = $("#qinqInnerVlanStartId").val(), //内层start VLAN
		endId = $("#qinqInnerVlanEndId").val(), //内层end VLAN
		qinqSvid = parseInt($("#qinqOuterVlanId").val()), //外层 VLAN
		qinqSTagCosDetermine = $("#qinqUseNewPriCbx").attr("checked") ? 1 : 2, //使用内层vlan pri还是新的，1:新的，2：内层的
		qinqSTagCosNewValue = $("#qinqCos").val();
	
	//做校验
	if(!qinqRuleValidate()){
		return
	}
	
	window.top.showWaitingDlg('@COMMON.wait@', String.format('@VLAN.addingOuterVlan@', startId ,endId ,qinqSvid), 'ext-mb-waiting');
	$.ajax({
		url : '/epon/vlan/addQinqRule.tv?r=' + Math.random(),
		data : {
			entityId: entityId,
			ponId: ponId,
			qinqSvid: qinqSvid,
			qinqStartCvid: startId,
			qinqEndCvid: endId,
			qinqSTagCosDetermine: qinqSTagCosDetermine,
			qinqSTagCosNewValue: qinqSTagCosNewValue
		},
		success : function() {
			top.closeWaitingDlg();
			refreshQinQ();
            refreshSCVID();
	    	top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: String.format('@VLAN.addOuterVlanOk@', startId ,endId ,qinqSvid)
	    	});
		},
		error : function() {
			window.parent.showMessageDlg('@COMMON.tip@', String.format('@VLAN.addOuterVlanError@', startId, endId, qinqSvid));
		}
	});
}

function qinqCosModeClick(){
	if ($("#qinqUseNewPriCbx").attr("checked")) {
		$("#qinqCos").attr("disabled", false);
	} else {
		$("#qinqCos").val("0");
		$("#qinqCos").attr("disabled", true);
	}
}

function qinqRuleValidate(){
	var startId = $("#qinqInnerVlanStartId").val(), //内层start VLAN
		endId = $("#qinqInnerVlanEndId").val(), //内层end VLAN
		qinqSvid = parseInt($("#qinqOuterVlanId").val()), //外层 VLAN
		qinqSTagCosDetermine = $("#qinqUseNewPriCbx").attr("checked") ? 1 : 2, //使用内层vlan pri还是新的，1:新的，2：内层的
		qinqSTagCosNewValue = $("#qinqCos").val();
	if(!isSingleVlanIdValid(qinqSvid)){
		$("#qinqOuterVlanId").focus();
		return false;
	}
	if(!isSingleVlanIdValid(startId)){
		$("#qinqInnerVlanStartId").focus();
		return false;
	}
	if(!isSingleVlanIdValid(endId)){
		$("#qinqInnerVlanEndId").focus();
		return false;
	}
	startId = parseInt(startId, 10);
	endId = parseInt(endId, 10);
	qinqSvid = parseInt(qinqSvid, 10);
	
	if(startId > endId){
		top.showMessageDlg('@COMMON.tip@', 'Start VLAN@VLAN.mustLessThan@End VLAN');
		return false;
	}
	
	//进行CVID校验
	var existMsgs = [], existObj;
	for(var i=startId; i<=endId; i++){
		existObj = isCvidExist(i);
		if(existObj.exist){
			existMsgs.push(existObj.msg);
		}
	}
	if(existMsgs.length){
		top.showMessageDlg('@COMMON.tip@', existMsgs.join('</br>'));
		return false;
	}
	
	if(!qinqRuleMap[qinqSvid]){
		//如果外层VLAN ID没有配置过，校验SVID
		var svidExistObj = isSvidExist(qinqSvid);
		if(isSvidExist.exist){
			top.showMessageDlg('@COMMON.tip@', svidExistObj.msg);
			return false;
		}
	}
	
	return true;
	
}

/**
 * 删除QinQ规则
 * @param qinqSvid 外层VLAN ID
 * @param startVlanId 起始VLAN ID
 * @param endVlanId 结束VLAN ID
 */
function deleteQinQRule(qinqSvid, startVlanId, endVlanId){
	window.parent.showConfirmDlg('@COMMON.tip@', String.format('@VLAN.cfmDelVlanQinq@', startVlanId, endVlanId , qinqSvid), function(type) {
		if(type == 'no'){
			return;
		}
		window.top.showWaitingDlg('@COMMON.wait@', String.format('@VLAN.delingVlanQinq@', startVlanId , endVlanId , qinqSvid), 'ext-mb-waiting');
		$.ajax({
			url : '/epon/vlan/deleteQinqRule.tv?r=' + Math.random(),
			data : {
				entityId : entityId,
				ponId : ponId,
				qinqStartCvid : startVlanId,
				qinqEndCvid : endVlanId
			},
			success : function() {
				top.closeWaitingDlg();
				top.afterSaveOrDelete({
		            title: '@COMMON.tip@',
		            html: String.format('@VLAN.delVlanQinqOk@', startVlanId, endVlanId, qinqSvid)
		    	});
				refreshQinQ();
	            refreshSCVID();
			},
			error : function() {
				window.parent.showMessageDlg('@COMMON.tip@', String.format('@VLAN.delVlanQinqError@', startVlanId, endVlanId, qinqSvid))
			}
		});
	});
	
}