function typeRender(value, meta, record){
	if(value == 3){
		return "GE";
	}else if(value == 4){
		return "XE"
	}else{
		return "--";		
	}
}

function portRender(value, meta, record){
	return value + "/" + record.data.portNo;
}

function manuRender(value, meta, record){
	var portIndex = record.data.portIndex;
	var portType = record.data.portType;
	return String.format("<a href='javascript:;' onClick='deleteCascadePort({0},{1})'>@COMMON.delete@</a>",portIndex,portType); 
}

function deleteCascadePort(portIndex, portType){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.deletePort@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/igmpconfig/deleteCascadePort.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				portIndex : portIndex,
				portType : portType
			},
			dataType :　'json',
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@confirm.deletePortS@</b>'
       			});
				//store.reload();
				refreshPage();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@confirm.deletePortF@");
			},
			cache : false
		});
	});
}

function batchDeleteCascadePort(){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.delSelectedPorts@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		var cascadePorts = getSelectedCascadePorts();
		$.ajax({
			url : '/epon/igmpconfig/batchDeleteCascadePort.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				cascadePorts : JSON.stringify(cascadePorts)
			},
			dataType :　'text',
			success : function(json) {
				if (json == "error"){//后台出现错误
					top.showMessageDlg("@COMMON.tip@", "@confirm.deletePortF@");
					return;
				}else if(json != "none"){//出现删除失败
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@confirm.failDeletePorts@' + json + '</b>'
	       			});
				}else{//全部删除成功
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@confirm.deletePortS@</b>'
	       			});
				}
				//store.reload();
				refreshPage();
			},
			error : function(json) {
				console.log(json)
				top.showMessageDlg("@COMMON.tip@", "@confirm.deletePortF@");
			},
			cache : false
		});
	});
}

function addCascadePort(){
	//不能大于8条;
	if(store.data.items.length >= 8){
		top.showMessageDlg("@COMMON.tip@", "@tip.addLess8@");
		return;
	}
	var portType = $("#portType").val();
	var portIndex = $("#portSel").val();
	if(portIndex == null || portIndex == ''){
		top.showMessageDlg("@COMMON.tip@", "@tip.addCascadePortF@");
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/addCascadePort.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			portType : portType,
			portIndex : portIndex
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.addCascadePortS@</b>'
   			});
			//store.reload();
			refreshPage();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.cascadePortIsUsed@");
		},
		cache : false
	});
}
function refreshPage(){
	window.location.href = window.location.href;
}

function refreshCascadePort(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshCascadePort.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			refreshPage();
			//store.reload();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}


//SNI端口类型类型的<select>来获取
function readPortData(){
	if(oSelect.GE === null){ 
		loadSniListByType({
			async    : false,
			portType : 3,
			entityId : window.entityId,
			successCallBack : function(json){
				oSelect.GE = json;
				createOption(oSelect.GE);
			}
		});
	}else{
		createOption(oSelect.GE);
	}
}

//创建上联口类型的option;
function createOption(json){
	$("#portSel").empty();
	if(json.length && json.length > 0){
		var opt = '';
		for(var i=0,len=json.length; i<len; i++){
			opt += String.format('<option value="{0}">{1}</option>', json[i].portIndex, json[i].portName);
		}
		$("#portSel").html(opt);
	}
}

$(function(){
	var sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        }
	    }
	}); 
	cm = new Ext.grid.ColumnModel({
		columns: [
		    sm,
		    {header: '@igmp.portType@', dataIndex: 'portType', renderer : typeRender},
		    {header: '@igmp.portNumber@', dataIndex: 'portName'},
		    {header: '@COMMON.manu@', width: 180, fixed:true , renderer :　manuRender}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/igmpconfig/loadCascadePortList.tv',
		fields : ["entityId","portIndex","portType","slotNo","portNo","portName"],
		baseParams : {
			entityId : entityId
		}
	});
	tb = new Ext.Toolbar({
		items : [{
			xtype : 'tbspacer', 
			width : 10
		},{
			cls   :'blueTxt',
			text  : '@igmp.portType@@COMMON.maohao@',
			xtype : 'tbtext' 
		},{
			cls   : 'mT5 mB5',
			xtype : 'component',
	     	html  : '<select class="w120 normalSel" id="portType" onChange="portTypeChange()">' + 
					'<option value="3">GE</option>' + 
					'<option value="4">XE</option>' + 
					'</select>'
		},{
			xtype : 'tbspacer', 
			width : 10
		},{
			cls   :'blueTxt',
			text  : '@igmp.linkPort@@COMMON.maohao@', 
			xtype : 'tbtext' 
		},{
			cls   : 'mT5 mB5',
			xtype : 'component',
		 	html  : '<select class="w120 normalSel" id="portSel"></select>'
			//html  : '<div id="putPortSel" style="width:120px;"></div>'
		},
		{
			xtype : 'tbspacer', 
			width : 5
		},{
			xtype : 'button',
			text : '@BUTTON.add@',
			iconCls : 'bmenu_new',
			handler : function(){
				addCascadePort();
			}
		},'-',{
			xtype : 'button',
			text : '@igmp.fetchCascadePort@',
			iconCls : 'bmenu_equipment',
			handler : function(){
				refreshCascadePort();
			}
		},'-',{
			id : "batchDelete",
			xtype : 'button',
			text : '@igmp.batchDelete@',
			iconCls : 'bmenu_delete',
			handler : function(){
				batchDeleteCascadePort();
			},
			disabled: true
		}]
	});

	grid = new Ext.grid.GridPanel({
		cm     : cm,
		sm     : sm,
		store  : store,
		region : 'center',
		margins: '10 10 0 10',
		stripeRows   : true,
		viewConfig   : { forceFit:true},
		bodyCssClass : 'normalTable',
		tbar : tb
	});
	store.load({
		callback : function(){
			readPortData();
		}
	});
	
	new Ext.Viewport({
	    layout : 'border',
		items  : [grid]
	});
	
});//end document.ready;

//设置按钮的disabled;
function disabledBtn(arr, disabled){
	$.each(arr,function(i, v){
		Ext.getCmp(v).setDisabled(disabled);
	})
};

function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn(['batchDelete'], false);
    }else{
    	disabledBtn(['batchDelete'], true);
    }
};

//获取选择的级联端口
function getSelectedCascadePorts(){
	var sm = grid.getSelectionModel();
	var selectedSections = [];
	var selectedCascadePorts = [];
	if (sm != null && sm.hasSelection()) {
		selectedSections = sm.getSelections();
		selectedSections.forEach(function(value){
			var cascadePort = new Object();
			cascadePort.entityId = value.data.entityId;
			cascadePort.portIndex = value.data.portIndex;
			cascadePort.portType = value.data.portType;
			selectedCascadePorts.push(cascadePort);
		})
	}
	return selectedCascadePorts;
}

function portTypeChange() {
    var portType = parseInt($('#portType').val(), 10);
    switch(portType){
    	case 3:
    		if(oSelect.GE === null){ 
				loadSniListByType({
					async    : false,
					portType : portType,
					entityId : window.entityId,
					successCallBack : function(json){
						oSelect.GE = json;
						createOption(oSelect.GE);
					}
				});
			}else{
				createOption(oSelect.GE);
			}
			break;
    	case 4:
    		if(oSelect.XE === null){
				loadSniListByType({
					async    : false,
					portType : portType,
					entityId : window.entityId,
					successCallBack : function(json){
						oSelect.XE = json;
						createOption(oSelect.XE);
					}
				});
			}else{
				createOption(oSelect.XE);
			}
    		break;
    }
	
}
