var trunkData = new Array();
var trunkStore;
var trunkGrid;
var trunkTmpData = new Array();
var trunkText;
var text = "";

// VLAN透传
function loadTrunkData(){
trunkStore = new Ext.data.Store({
    proxy : new Ext.data.HttpProxy({url:'/epon/vlan/loadVlanTrunkRuleList.tv?uniId=' + uniId}),
    reader : new Ext.data.ArrayReader({}, Ext.data.Record.create([{name:'trunkId', type:'int'}]))
});
trunkStore.load({
    callback: function(records, options, success) {
        // 数据加载完毕更新数组中的值
        $.each(records, function(i, n) {
            trunkData[i] = n.json;
        });
        trunkGrid = new Ext.grid.GridPanel({
            cls:"normalTable",stripeRows:true,region: "center",
            viewConfig: {forceFit: true},
            title:"@VLAN.trunkList@",
            renderTo: "trunkIdList",
            height: 150,
            columns: [{
                header: "Trunk VLAN ID", dataIndex: "trunkId", renderer: trunkChangeRed
            },{header: I18N.COMMON.manu , dataIndex: "id", renderer: function(value, cellmeta, record) {
            		 if(operationDevicePower){
            			 return "<img src='/images/delete.gif' onclick='deleteTrunkRule(" + record.data.trunkId + ")'/>";
            		 }else{
            			 return "<img src='/images/deleteDisable.gif'/>";
            		 }
            }
            }],
            store: trunkStore,
            listeners:{
                'viewready':{
                    fn : function() {
                        trunkGrid.getSelectionModel().selectFirstRow();
                      //----硬性将焦点移入本页面,不让会出现焦点丢失的情况----//
                    	$("#trunkId").focus();
                    },
                    scope : this
                }
            }
        });
        trunkGrid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
            var tmpId = grid.selections.get(0).data.trunkId;
            selectedIndex = getTrunkIndex(tmpId);
        });
        $("#trunkId").keyup(function() {
            text = $("#trunkId").val();
            trunkText = text;
            var data = [];
            $.each(trunkData, function(i, n) {
                var tmp = "" + n;
                if (text == null || text == "" || tmp.redMatchWith(text)) {
                    data[data.length] = [tmp];
                }
            });
            if (data.length == 0 && (vlanMode == 4)) {
                trunkType = "add";
                $("#trunkSubmit").attr("disabled", false);
            }
            trunkTmpData = data;
            trunkStore.loadData(data);
        });
    }
});
}
function trunkChangeRed (value, a, b, c) {
    if (text.redMatchWith(value.toString())) {
    	trunkGrid.selModel.selectRow(0, true);
    	$("#trunkSubmit").attr("disabled", true);
        return "<font color='red'>"+value+"</font>";
    } else {
        return value;
    }
}
function deleteTrunkRule(trunkId) {
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.VLAN.cfmDelVlanTrunk , trunkId), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.delingVlanTrunk , trunkId), 'ext-mb-waiting');
        // 临时变量存储删除后Trunk列表
        var trunkDataTmp = trunkData.slice();
        trunkDataTmp.splice(selectedIndex,1);
        $.ajax({
	        url: 'epon/vlan/updateVlanTrunkRule.tv',
	        data: "uniId=" + uniId + "&entityId=" + entityId + "&trunkList=" + trunkDataTmp.toString()+ "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delVlanTrunkOk , trunkId))
	            	trunkData.remove(selectedIndex);
			        trunkStore.loadData(trunkData);
			        trunkGrid.selModel.selectRow(0);
			        $("#trunkSubmit").attr("disabled", true);
			        $("#trunkId").val("");
			        $("#trunkId").focus();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delVlanTrunkError , trunkId) ,'error');
	    }, cache: false
	    });
    });
}
function getTrunkIndex(trunkId) {
    var tmpIndex = -1;
    for (var i = 0; i < trunkData.length; i++) {
        if (trunkData[i][0] == trunkId) {
            tmpIndex = i;
            break;
        }
    }
    return tmpIndex;
}
function checkTrunkId(){
	var reg0 = /^([0-9])+$/;
	var trunkId = $("#trunkId").val();
	if(trunkId == "" || trunkId == null){
		return false;
	}else{
		if(reg0.exec(trunkId)){
			return true;
		}else{
			return false;
		}
	}
}