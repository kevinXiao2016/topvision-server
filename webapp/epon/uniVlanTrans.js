var transData = new Array();
var transStore;
var transGrid;
var transTmpData = new Array();
var transType;
var transIndex;
var beforeTransText;
var afterTransText;
var afterCssId;
var transReaderFlag = false;

function loadTransData(){
	 transStore = new Ext.data.Store({
	        proxy : new Ext.data.HttpProxy({url:'/epon/vlan/loadVlanTranslationRuleList.tv?uniId=' + uniId + '&r=' + Math.random()}),
	        reader : new Ext.data.ArrayReader({}, Ext.data.Record.create([{name:'beforeTransId', type:'int'},{name:'afterTransId', type:'int'}]))
	    });
		 transStore.load({
		        callback: function(records, options, success) {
		            // 数据加载完毕更新数组中的值
		            $.each(records, function(i, n) {
		                transData[i] = n.json;
		            });
		            transGrid = new Ext.grid.GridPanel({
		                cls:"normalTable",stripeRows:true,region: "center",
		                viewConfig: {forceFit: true},
		                title:"@VLAN.translateList@",
		                renderTo: "translation",
		                height: 150,
		                loadMask:{msg: I18N.COMMON.loading },
		                columns: [{
		                    header: I18N.VLAN.originId , dataIndex: "beforeTransId",renderer: transChangeRed
		                }, {
		                    header: I18N.VLAN.translatedId , dataIndex: "afterTransId",
		                }, {
		                    header: I18N.COMMON.manu , dataIndex: "id", renderer: function(value, cellmeta, record, rowIndex, columnIndex, store) {
		                    	if(operationDevicePower){
		                    		var deleteImage = "<img src='/images/delete.gif' onclick='deleteTransRule(" + record.data.beforeTransId + ", " + record.data.afterTransId + ")'/>";
		                    	}else{
		                    		var deleteImage = "<img src='/images/deleteDisable.gif'/>";
		                    	}
		                    	return deleteImage;
		                	}
		                }],
		                sm: new Ext.grid.RowSelectionModel({ singleSelect: true }),
		                store: transStore,
		                listeners:{
		                    'viewready':{
		                        fn : function() {
		                            transGrid.getSelectionModel().selectFirstRow();
		                            //----硬性将焦点移入本页面,不让会出现焦点丢失的情况----//
	                            	$("#beforeTransId").focus();
	                            	//----然后将焦点移除，否则会有提示框——---//
	                            	$("#beforeTransId").blur();
	                            	//----动态绑定focus和blur事件，对于之前的绑定都需要进行移除---//
	                            	$("#beforeTransId").bind('focus',function(){inputFocused('beforeTransId', I18N.COMMON.range4094 , 'iptxt_focused');}).bind('blur',function(){inputBlured(this, 'iptxt');inputBlured(this, 'iptxt');});
		                        },
		                        scope : this
		                    }
		                }
		            });
		            transGrid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
		                var tmpId = transGrid.selModel.selections.get(0).data.beforeTransId
		                selectedIndex = getTransIndex(tmpId)
		                $("#beforeTransId").val(tmpId)
		            })
		            $("#beforeTransId").keyup(function() {
		                text = $("#beforeTransId").val()
		                beforeTransText = text
		                afterTransText = $("#afterTransId").val()
		                beforeTransFlag = 0
		                afterTransFalg = 0
		                var data = new Array()
		                var j=0
		                if (beforeTransText == null || beforeTransText == undefined || beforeTransText == "") {
							transSubmitChange(1, 1)
						} else {
							if (!checkedInput("beforeTransId")) {
								transSubmitChange(1, 1)
								return
							} else if (afterTransText == null || afterTransText == "" || afterTransText == undefined) {
								transSubmitChange(1, 1)
							} else if (checkedInput("beforeTransId")) {
								$("#transSubmit").attr("disabled", false)
							} else {
								$("#transSubmit").attr("disabled", true)
								$("#transSubmit").mouseout()
							}
						}
		                $.each(transData, function(i, n) {
		                    var tmp1 = "" + n[0];
		                    var tmp2 = "" + n[1];
		                    if (text == null || text == "" || tmp1.startWith(text) || tmp2.redMatchWith(afterTransText)) {
		                        data[j] = n;
		                        j++;
		                    }
		                });
		                j=0;
		                transTmpData = data;
		                transStore.loadData(data);
		                transSubmitChange(beforeTransFlag,afterTransFalg);
		            });
		            $("#afterTransId").keyup(function() {
		                text = $("#afterTransId").val();
		                afterTransText = text;
		                beforeTransText = $("#beforeTransId").val();
		                beforeTransFlag = 0;
		                afterTransFalg = 0;
		                if(afterTransText==null || afterTransText=='undefined' || afterTransText==""){
		                	transSubmitChange(1,1);
		            	}else{
		                	if(!checkedInput("afterTransId")){
								transSubmitChange(1,1);
			                	return;
			                }else if(beforeTransText==null || beforeTransText=="" || beforeTransText=='undefined'){
			                	transSubmitChange(1,1);
			                }else if(checkedInput("beforeTransId")&& (vlanMode == 2)){
			                	$("#transSubmit").attr("disabled", false);
				            }else{
				            	$("#transSubmit").attr("disabled", true);
					        }
			            }
		                var data = new Array();
		                var j=0;
		                $.each(transData, function(i, n) {
		                    var tmp1 = "" + n[0];
		                    var tmp2 = "" + n[1];
		                    if (text == null || text == "" || tmp2.startWith(text) || tmp1.redMatchWith(beforeTransText)) {
		                        data[j] = n;
		                        j++;
		                    }
		                });
		                j=0;
		                transStore.loadData(data);
		                transSubmitChange(beforeTransFlag,afterTransFalg);
		            });
		        }
			 });
}
function deleteTransRule(beforeTransId, afterTransId) {
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.VLAN.cfmDelVlanTransRule , {before: beforeTransId , after: afterTransId}), function(type) {
        if (type == 'no') {
            return;            
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.delingTransRule , {before:beforeTransId , after:afterTransId}), 'ext-mb-waiting');
		$.ajax({
		        url: 'epon/vlan/deleteVlanTranslationRule.tv',
		        type: 'POST',
		        data: "uniId=" + uniId + "&vlanIndex=" + beforeTransId + "&entityId="+ entityId +"&num=" + Math.random(),
		        dataType:"text",
		        success: function(text) {
			        if(text == 'success'){
		            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.delTranslateOk )
		            	transData.remove(selectedIndex);
			            beforeTransText = "";
			            afterTransText = "";
			            transStore.loadData(transData);
			            transGrid.selModel.selectRow(0, true);
			            $("#beforeTransId").val("");
			            $("#afterTransId").val("");
			            $("#beforeTransId").focus();
			            $("#transSubmit").attr("value", I18N.COMMON.confirm);
			            $("#transSubmit").attr("disabled", true);
			        }else{
			        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
				    }
		        }, error: function(text) {
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.delRuleError2 ,'error')
		    }, cache: false
		    });
    });
}

//trans按钮的名称修改及灰掉与否
function transSubmitChange(i,j){
	if(vlanMode == 2){
		if (2 == i + j) {
			transType = "filter";
			$("#transSubmit").attr("value", I18N.COMMON.confirm)
			$("#transSubmit").attr("disabled", true);
			$("#transSubmit").mouseout();
		} else if (1 == i + j) {
			if (i == 1) {
				transType = "modify1";
			} else if (j == 1) {
				transType = "modify2";
			}
			$("#transSubmit").attr("value", I18N.COMMON.modify)
			$("#transSubmit").attr("disabled", false);
		} else if (0 == i + j) {
			transType = "add";
			$("#transSubmit").attr("value", I18N.VLAN.add);
			$("#transSubmit").attr("disabled", false);
		}
		if ($("#afterTransId").val() == null || $("#afterTransId").val() == "" || $("#afterTransId").val() == undefined || $("#beforeTransId").val() == null || $("#beforeTransId").val() == ""
				|| $("#beforeTransId").val() == undefined) {
			$("#transSubmit").attr("disabled", true);
			$("#transSubmit").mouseout();
		}
		if (!checkedInput("beforeTransId") || !checkedInput("afterTransId")) {
			$("#transSubmit").attr("disabled", true);
			$("#transSubmit").mouseout();
		}
	}else{
		$("#transSubmit").attr("disabled", true);
	}
}
function getTransIndex(transId) {
    var tmpIndex = -1;
    for (var i = 0; i < transData.length; i++) {
        if (transData[i][0] == transId) {
            tmpIndex = i;
            break;
        }
    }
    return tmpIndex;
}
function transChangeRed(value, a, b, c) {
    if ((b.data.beforeTransId == parseInt(beforeTransText)) && (b.data.afterTransId == parseInt(afterTransText))) {
        //b.data.afterTransId = "<font color='red'>" + b.data.afterTransId + "</font>";
        transGrid.selModel.selectRow(0, true);
        $("#transSubmit").attr("disabled", true);
        beforeTransFlag = 1;
        transReaderFlag = true;
        afterTransFalg = 1;
        return "<font color='red'>" + value + "</font>";
    }else if((b.data.beforeTransId == parseInt(beforeTransText)) && (b.data.afterTransId != parseInt(afterTransText))){
        $("#transSubmit").attr("disabled", false);
        beforeTransFlag = 1;
        return "<font color='red'>" + value + "</font>";
    }else if((b.data.beforeTransId != parseInt(beforeTransText)) && (b.data.afterTransId == parseInt(afterTransText))){
    	//afterCssId = b.data.afterTransId;
    	//b.data.afterTransId = "<font color='red'>" + b.data.afterTransId + "</font>";
    	afterTransFalg = 1;
        $("#transSubmit").attr("disabled", false);
        return value;
    }else{
    	return value;
    }
}