var ponTransformStore, ponTransformGrid;

function initVlanTransform(){
	var cm =  new Ext.grid.ColumnModel([
  	    {header:'@VLAN.originVlan@ ID', dataIndex:'beforeTransId', align:'center', width: 0.4, sortable:true},
  	    {header:'@VLAN.translatedVlan@ ID', dataIndex:'afterTransId', align:'center', width: 0.4 ,sortable:true},
  	    {header: "@COMMON.manu@", dataIndex:'afterTransId', width: 100, align:'center', fixed : true, renderer: transformMenuRender}
  	]);
	ponTransformStore = new Ext.data.Store({
		url: '/epon/vlan/loadTransData.tv',
		reader : new Ext.data.ArrayReader({}, [
          {name : 'beforeTransId'},
          {name : 'afterTransId'},
          {name : 'transCos'},
          {name : 'transTpid'}
      ])
	});
	ponTransformStore.load({params: {ponId: ponId}});
   	
	ponTransformGrid = new Ext.grid.GridPanel({
   		stripeRows: true,
   		columnLines: true,
   		bodyCssClass: 'normalTable',
   		border: false,
   		store: ponTransformStore,
   		cm: cm,
   		viewConfig:{
   			forceFit: true
   		},
   		height: $('#ponTransformContainer').height() - 65,
	    renderTo: "ponVlanTransformGridContainer"
   	});
	
	$('#beforeTransId, #afterTransId').on('keyup', function(e){
		var text = $(this).val();
		
		if(text && !isSingleVlanIdValid(text)){
			return addInValidStyle(this);
		}
		addValidStyle(this);
		
		//进行相关的过滤
		fiterVlanTransform();
	})
}

function refreshVlanTransform(){
	ponTransformStore.load({params: {ponId: ponId}});
	$('#beforeTransId').val('');
	$('#afterTransId').val('');
}

function resizeTransfrom(){
	ponTransformGrid.setSize($('#ponTransformContainer').width()-10, $('#ponTransformContainer').height() - 65);
}

function fiterVlanTransform(){
	var beforeTransId = $('#beforeTransId').val(),
		afterTransId = $('#afterTransId').val();
	if( (beforeTransId && !isSingleVlanIdValid(beforeTransId)) || (afterTransId && !isSingleVlanIdValid(afterTransId))){
		return
	}
	
	ponTransformStore.filterBy(function(record){
		if(beforeTransId && afterTransId){
			//如果都存在，只要原VLAN ID和转换后VLAN ID匹配一个即可
			return beforeTransId == record.data.beforeTransId || afterTransId == record.data.afterTransId;
		}else if(beforeTransId && !afterTransId){
			//只有原VLAN ID，匹配原VLAN ID
			return beforeTransId == record.data.beforeTransId
		}else if(!beforeTransId && afterTransId){
			//只有转换后VLAN ID，匹配转换后VLAN ID
			return afterTransId == record.data.afterTransId
		}else if(!beforeTransId && !afterTransId){
			//都为空，全部显示
			return true;
		}
	});
}

function transformMenuRender(value, cellmeta, record){
	// 变红操作后，特殊处理。
    if (record.data.afterTransId.length > 4) {
        record.data.afterTransId = parseInt(record.data.afterTransId.substring(18).split("<")[0]);
    }
    if(operationDevicePower){
    	return String.format('<a class="yellowLink" href="javascript: deleteTransRule({0}, {1});">@COMMON.delete@</a>', record.data.beforeTransId, record.data.afterTransId);
    }else{
    	return "";
    }
}

function transOkClick(){
	//进行合法性校验
	if(!transformValidate()){
		return;
	}
	var beforeTransId = $('#beforeTransId').val(),
		afterTransId = $('#afterTransId').val(),
		url,
		params,
		modify = false;
	
	if(ponTransformStore.getCount() == 0){
		//添加
		url = '/epon/vlan/addTransRule.tv';
		params = {
            entityId: entityId,
            ponId: ponId,
            newBeforeTransVid: beforeTransId,
            newAfterTransVid: afterTransId
        };
	}else if(ponTransformStore.getCount() == 1){
		var rule = ponTransformStore.getAt(0).data;
		if(rule.beforeTransId == beforeTransId && rule.afterTransId == afterTransId){
			return top.showMessageDlg("@COMMON.tip@", '@VLAN.ruleExist@');
		}
		//修改
		modify = true;
		url = '/epon/vlan/modifyTransRule.tv';
		params = {
            entityId: entityId,
            ponId: ponId,
            oldBeforeTransVid : rule.beforeTransId,
            oldAfterTransVid : rule.afterTransId,
            newBeforeTransVid : beforeTransId,
            newAfterTransVid : afterTransId
        };
	}else{
		//匹配大于1条，正常最多2条，这时肯定不能转换
		return top.showMessageDlg("@COMMON.tip@", '@VLAN.startAndEndBothExist@');
	}
	
	//下发配置
	$.ajax({
        url : url,
        data : params,
        success : function() {
            //刷新VLAN转换区域级SVID/CVID
            refreshVlanTransform();
	    	refreshSCVID();
	    	
	    	var tip = String.format("@VLAN.addVlanRuleOk@" , {before:beforeTransId , after : afterTransId});
	    	if(modify){
	    		tip = String.format("@VLAN.mdfVlanRuleOk@" , [rule.beforeTransId, rule.afterTransId, beforeTransId, afterTransId]);
	    	}
	    	top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: tip
	    	});
        },
        error : function() {
        	var tip = String.format("@VLAN.addVlanRuleError@" , {before:beforeTransId , after : afterTransId});
        	if(modify){
	    		tip = String.format("@VLAN.mdfVlanRuleError@", [rule.beforeTransId, rule.afterTransId, beforeTransId, afterTransId]);
	    	}
            window.parent.showMessageDlg("@COMMON.tip@", tip);
        }
    });
}

/**
 * 添加透传VLAN规则时的校验
 */
function transformValidate(){
	var beforeTransId = $('#beforeTransId').val(),
		afterTransId = $('#afterTransId').val();
	
	//必须为合法的格式
	if(!isSingleVlanIdValid(beforeTransId)){
		return $('#beforeTransId').focus();
	}
	if(!isSingleVlanIdValid(afterTransId)){
		return $('#afterTransId').focus();
	}
	
	//判断是否有不存在的VLAN
	if(!isVlanExist(beforeTransId)){
		return top.showMessageDlg("@COMMON.tip@", String.format('@VLAN.vlanCountTip@', beforeTransId) );
	}
	if(!isVlanExist(afterTransId)){
		return top.showMessageDlg("@COMMON.tip@", String.format('@VLAN.vlanCountTip@', afterTransId) );
	}
	
	//进行SVID校验
	var existObj = isSvidExist(afterTransId);
	if(existObj.exist){
		top.showMessageDlg('@COMMON.tip@', existObj.msg);
		return false;
	}
	
	//进行CVID校验
	existObj = isCvidExist(beforeTransId);
	if(existObj.exist){
		top.showMessageDlg('@COMMON.tip@', existObj.msg);
		return false;
	}
	return true;
}

/**
 * 删除VLANN转换规则
 */
function deleteTransRule(beforeTransId, afterTransId){
	window.parent.showConfirmDlg('@COMMON.tip@', String.format('@VLAN.cfmDelVlanTransRule@' , {before:beforeTransId ,after:afterTransId}), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg('@COMMON.wait@', String.format('@VLAN.delingTransRule@', {before:beforeTransId , after: afterTransId}))
        $.ajax({
            url : '/epon/vlan/deleteTransRule.tv',
            data : {
                entityId : entityId,
                ponId : ponId,
                oldBeforeTransVid : beforeTransId
            },
            cache: false,
            success : function() {
            	top.closeWaitingDlg();
            	refreshVlanTransform();
    	    	refreshSCVID();
    	    	
            	top.afterSaveOrDelete({
    	            title: '@COMMON.tip@',
    	            html: String.format('@VLAN.delRuleOk@' , {before:beforeTransId , after: afterTransId})
    	    	});
            },
            error : function() {
                window.parent.showMessageDlg('@COMMON.tip@', String.format('@VLAN.delRuleError@', {before: beforeTransId , after: afterTransId}));
            }
        });
    });
}