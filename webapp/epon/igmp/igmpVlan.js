function typeRender(value, meta, record){
	if(value == 1){
		return "INVALID";
	}else if(value == 3){
		return "GE";
	}else if(value == 4){
		return "XE";
	}else if(value == 9){
		return "ETHAGG";
	}else{
		return "--";
	}
}

function portRender(value, meta, record){
	var portType = record.data.portType;
	if(portType == 3 || portType == 4){
		return value;
	}else{
		return "--";
	}
}

function aggRender(value, meta, record){
	var portType = record.data.portType;
	if(portType != 9){
		return "--";
	}else{
		return value;
	}
}
function manuRender(value, meta, record){
	var vlanId = record.data.vlanId,
	    str = "";
	if(mode != 2){//router模式下，没有编辑;
	    str += '<a href="javascript:;" onclick="editFn()">@COMMON.edit@</a> / ';
	}
	    str += String.format('<a href="javascript:;" onclick="deleteVlan({0})">@COMMON.delete@</a>',vlanId);
	return str;
}
	
function deleteVlan(vlanId){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.deleteVlan@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/igmpconfig/deleteIgmpVlan.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				vlanId : vlanId
			},
			dataType :　'json',
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@confirm.deleteVlanS@</b>'
       			});
//				store.reload();
				refreshPage();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@confirm.deleteVlanF@");
			},
			cache : false
		});
	});
}

function validateData(type){ //分别是add 和 edit;
	//在选择XE或者GE时,只能选择端口; 在选择ETHAGG时,只能选择上行端口聚合组  考虑给-1标识
	var $vlanId = $("#vlanIdInput"),
	    vlanId  = $vlanId.val(),
	    numberReg = /^\d{1,4}$/,
	    flag = false;
	
	if( numberReg.test(vlanId) ){
		if(vlanId >= 2 && vlanId <= 4094){
			flag = true;
		}
	}
	if(!flag){
		$vlanId.focus();
		return false;
	}
	if(type === 'add'){
		//对比store，如果已经添加，则不能再添加;
		if(store.data.items.length > 0){
			if(store.data.items.length >= 64){
				top.showMessageDlg("@COMMON.tip@", "@tip.lessThan64@");
				return false;
			}
			for(var i=0,len=store.data.items.length; i<len; i++){
				var item = store.data.items[i];
				if(item.data.vlanId == vlanId){ //输入的vlanId已经存在store中;
					var tipStr = String.format("<b class='orangeTxt'>VLAN ID {0}</b> @tip.alreadyHave@", vlanId);
					top.showMessageDlg("@COMMON.tip@", tipStr);
					return false;
				}
			}
		}
	}
	var portType = $("#portTypeSel").val(),
	    portIndex,
	    aggId;
	if(portType == 9){
		portIndex = -1;
		aggId = $("#aggregateGroupSel").val();
		if(aggId == null){
			top.showMessageDlg("@COMMON.tip@", "@tip.aggAddF@");
			return false;
		}
	}else if(portType == 1){
		portIndex = -1;
		aggId = -1;
	}else{//3或者4;
		portIndex = $("#portNumberSel").val();
		if(portIndex == null){
			top.showMessageDlg("@COMMON.tip@", "@tip.aggAddF2@");
			return false;
		}
		aggId = -1;
	}
	var json = {
		entityId : entityId,
		vlanId : vlanId,
		portType : portType,
		portIndex : portIndex,
		uplinkAggId :　aggId
	}
	return json;
}
function addIgmpVlan(){
	var data = validateData('add');
	if(data === false){ return; }
	
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/addIgmpVlan.tv',
		type : 'POST',
		data : data,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.addVlanS@</b>'
   			});
//			store.reload();
			refreshPage();
			closeFakeWindow();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.addVlanF@");
		},
		cache : false
	});
}

// 修改切换端口类型时,必须先切换到INVALID类型
// 在选择INVALID的时候,不能选择任何端口和上行聚合组ID 考虑给-1标识
// 在选择XE或者GE时,只能选择端口; 在选择ETHAGG时,只能选择上行端口聚合组  考虑给-1标识
function modifyIgmpVlan(){
	if($("#fakeExtWindow").data("portType") !== 1 && $("#portTypeSel").val() != 1){ //如果原来不是INVALID,现在又不是切换到INVALID;
		top.showMessageDlg("@COMMON.tip@", "@imgp.tip.tip32@");
		return;
	}
	var data = validateData('edit');
	if(data === false){ return;}
	
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/modifyIgmpVlan.tv',
		type : 'POST',
		data : data,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.editVlanS@</b>'
   			});
//			store.reload();
			refreshPage();
			closeFakeWindow();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.editVlanF@");
		},
		cache : false
	});
}

function refreshIgmpVlan(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshIgmpVlan.tv',
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
//			store.reload();
			refreshPage();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}
function batchdeleteVlan(){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.delSelectedVlan@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		var vlanIds = getSelectedVlanIds();
		$.ajax({
			url : '/epon/igmpconfig/batchDeleteIgmpVlan.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				vlanIds : vlanIds
			},
			dataType :　'text',
			success : function(json) {
				if(json != "none"){//出现删除失败
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@confirm.failDeleteVlans@' + json + '</b>'
	       			});
				}else{//全部删除成功
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@confirm.deleteVlanS@</b>'
	       			});
				}
//				store.reload();
				refreshPage();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@confirm.deleteVlanF@");
			},
			cache : false
		});
	});
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
		    {header: 'VLAN ID', dataIndex: 'vlanId'},
		    {header: '@igmp.portType@', dataIndex: 'portType', renderer : typeRender},
		    {header: '@igmp.portNumber@', dataIndex: 'portName', renderer : portRender},
		    {header: '@igmp.aggId@', dataIndex: 'uplinkAggId', renderer :　aggRender},
		    {header: '@COMMON.manu@', dataIndex: 'op',width: 180, fixed:true, renderer : manuRender}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/igmpconfig/loadIgmpVlanList.tv',
		fields : ["entityId","portIndex", "vlanId", "portType","slotNo","portNo", "uplinkAggId","portName"],
		baseParams : {
			entityId : entityId
		}
	});
	grid = new Ext.grid.GridPanel({
		margins : '20 10 0 10',
		cm     : cm,
		sm     : sm,
		store  : store,
		tbar   : [{
			xtype : 'tbspacer', 
			width : 10
		},{
			cls   :'blueTxt',
			text  : 'VLAN ID@COMMON.maohao@',
			xtype : 'tbtext' 
		},{
			cls   : 'mT5 mB5',
			xtype : 'component',
			html  : '<input id="tbarVlanId" type="text" class="w100 normalInput" toolTip="@igmp.tip.tip9@" />'
		},{
			xtype : 'tbspacer', 
			width : 10
		},{
			xtype : 'button',
			text : '@BUTTON.add@',
			iconCls : 'bmenu_new',
			handler : function(){
				addVlanId();
				//addFn()
			}
		},'-',{
			xtype : 'button',
			text : '@igmp.fetchVlan@',
			iconCls : 'bmenu_equipment',
			handler : function(){
				refreshIgmpVlan();
			}
		},'-',{
			id : "batchDelete",
			xtype : 'button',
			text : '@igmp.batchDelete@',
			iconCls : 'bmenu_delete',
			handler : function(){
				batchdeleteVlan();
			},
			disabled: true
		}],
		region : 'center',
		stripeRows   : true,
		viewConfig   : { forceFit:true},
		bodyCssClass : 'normalTable'
	});
	
	store.load();
	
	new Ext.Viewport({
	    layout : 'border',
		items  : [grid]
	});
	
});//end document.ready;
//toolbar上面,添加VLAN ID;
function addVlanId(){
	//在选择XE或者GE时,只能选择端口; 在选择ETHAGG时,只能选择上行端口聚合组  考虑给-1标识
	var $vlanId = $("#tbarVlanId"),
	    vlanId  = $vlanId.val(),
	    numberReg = /^\d{1,4}$/,
	    flag = false;
	
	if( numberReg.test(vlanId) ){
		if(vlanId >= 2 && vlanId <= 4094){
			flag = true;
		}
	}
	if(!flag){
		$vlanId.focus();
		return;
	}
	//对比store，如果已经添加，则不能再添加;
	if(store.data.items.length > 0){
		if(store.data.items.length >= 64){
			top.showMessageDlg("@COMMON.tip@", "@tip.lessThan64@");
			return;
		}
		for(var i=0,len=store.data.items.length; i<len; i++){
			var item = store.data.items[i];
			if(item.data.vlanId == vlanId){ //输入的vlanId已经存在store中;
				var tipStr = String.format("<b class='orangeTxt'>VLAN ID {0}</b> @tip.alreadyHave@", vlanId);
				top.showMessageDlg("@COMMON.tip@", tipStr);
				return;
			}
		}
	}
	var data = {
		entityId : entityId,
		vlanId : vlanId
	};
	
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/addIgmpVlan.tv',
		type : 'POST',
		data : data,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.addVlanS@</b>'
   			});
//			store.reload();
			refreshPage();
			closeFakeWindow();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.addVlanF@");
		},
		cache : false
	});
}

function refreshPage(){
	window.location.href = window.location.href;
}

function addFn(){
	openBg();
	createOpenLayer({
		type  : "add",
		title : "@igmp.addVlan@",
		desc  : "@igmp.addVlan@"
	});
}
function createOpenLayer(obj){
	openFakeExtWin();
	var tpl = new Ext.XTemplate(
	'<div id="fakeExtWindowTitle" class="fakeExtWindowTitle"><b>{title}</b><label onclick="closeFakeWindow()"></label></div>',   
	'<div class="openWinHeader">',
    	'<div class="openWinTip">{desc}</div>',
    	'<div class="rightCirIco wheelCirIco"></div>',
	'</div>',
	'<div class="edge10 pT30">',
		'<table id="addVlanTable" class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
		    '<tbody>',
		        '<tr>',
		            '<td class="rightBlueTxt" width="140">VLAN ID:</td>',
		            '<td width="190"><input {[values.type=="edit" ? "disabled=disabled" : ""]} id="vlanIdInput" maxlength="4" type="text" class="normalInput w180" tooltip="@igmp.tip.tip10@"></td>',
		            '<td class="rightBlueTxt" width="140">@igmp.portType@:</td>',
		            '<td>',
		            	'<select class="normalSel w180" id="portTypeSel" onchange="changeType()">',
		            		'<tpl for="options">',
		            			'<option value="{value}" {[values.selected === "selected" ? selected="selected" : ""]}>{text}</option>',
		            		'</tpl>',
		            	'</select>',
		            '</td>',
		        '</tr>',
		    '</tbody>',
		    '<tbody>',
		        '<tr class="darkZebraTr">',
		            '<td class="rightBlueTxt">@igmp.portNumber@:</td>',
		            '<td colspan="3"><select class="normalSel w180" id="portNumberSel"></select></td>',
		        '</tr>',
		    '</tbody>',
			'<tbody>',
			    '<tr class="darkZebraTr">',
		            '<td class="rightBlueTxt">@igmp.uplinkAggId@:</td>',
		            '<td width="190" colspan="3"><select class="normalSel w180" id="aggregateGroupSel"></select></td>',
		        '</tr>',
		    '</tbody>',
		'</table>',
		'<div class="noWidthCenterOuter clearBoth">',
	        '<ol class="upChannelListOl pB0 pT80 noWidthCenter">',
	            '<li><a href="javascript:;" class="normalBtnBig" onclick="{[values.type =="add" ? "addIgmpVlan()" : "modifyIgmpVlan()"]}">',
	        			'<span><i class="{[values.type=="add" ? "miniIcoAdd" : "miniIcoEdit"]}"></i>{[values.type =="add" ? "@BUTTON.add@" : "@COMMON.edit@"]}</span>',
	        		'</a>',
	        	'</li>',
	            '<li><a href="javascript:;" class="normalBtnBig" onclick="closeFakeWindow()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>',
	        '</ol>',
	     '</div>',
	 '</div>'
	);
	var $fakeExtWindow = Ext.get("fakeExtWindow"),
	    aPos  = $fakeExtWindow.getCenterXY(),
	    aOpts = [{
	    	text  : 'INVALID',
	    	value : 1
	    },{
	    	text  : 'GE', 
	    	value : 3
	    },{
	    	text  : 'XE', 
	    	value : 4
	    }];
	
	if(mode == 1){ //proxy模式下才有ETHAGG;
		aOpts.push({
			text  : 'ETHAGG',
			value : 9
		})
	};
	
	if(obj.type === 'edit'){
		if(obj.data.portType != aOpts[0].value){
			for(var i=0,len=aOpts.length; i<len; i++){
				if(aOpts[i].value == obj.data.portType){
					aOpts[i].selected = "selected";
				}
			}
		}
	}
	obj.options = aOpts;
	tpl.overwrite($fakeExtWindow, obj);
	$fakeExtWindow.setXY([aPos[0],2]);
	updateSelVal();
	if( obj.type === 'edit' ){
		var data = obj.data,
		    portType = parseInt(data.portType, 10);
		
		$("#vlanIdInput").val(data.vlanId);
		$("#portTypeSel").val(data.portType);
		
		if(data.portType === 9){ //ETHAGG模式;
			$("#aggregateGroupSel").val(data.uplinkAggId);
		}else{ //3 或4(GE或XE);
			$("#portNumberSel").val(data.portIndex);
		}
		//编辑Vlan的时候，端口类型必须先切换到INVALID,否则干什么都会失败,所以先记录编辑之前的端口类型的值;
		$("#fakeExtWindow").data({portType: portType});
	}
	/*var dd = new Ext.dd.DDProxy("fakeExtWindow","",{
		ignoreSelf : false
	});
	dd.setHandleElId('fakeExtWindowTitle');
	dd.startDrag = function(){
		this.constrainTo("mask");
		$("#fakeExtWindow").css({opacity:0});
	}
	dd.afterDrag = function(){
	    $("#fakeExtWindow").css({opacity:1});
	}*/
}
function editFn(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		openBg();
		createOpenLayer({
			type : "edit",
			title : "@igmp.editVlan@",
			desc  : "@igmp.editVlanDesc@",
			data  : record.data
		});
	}
}
//点击添加窗口，则初始化一次oSelect的值;
function updateSelVal(){
	//暂时只取一次值，不重置;
	/*oSelect.GE = null;
	oSelect.XE = null;
	oSelect.ETHAGG = null;*/
	changeType();
};
//根据端口类型，设置select的值，并且设置好界面显示和隐藏的逻辑;
function changeType(){
	var $portTypeSel       = $("#portTypeSel"),
	    portType           = parseInt($portTypeSel.val(), 10),
	    $portNumberSel     = $("#portNumberSel"),
	    $aggregateGroupSel = $("#aggregateGroupSel");
	
	$("#addVlanTable tbody").css({display : ''});
	switch(portType){
		case 1:
			$("#addVlanTable tbody:eq(1)").css({display : 'none'});
			$("#addVlanTable tbody:eq(2)").css({display : 'none'});
			break;
		case 3:
			$("#addVlanTable tbody:eq(2)").css({display : 'none'});
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
			$("#addVlanTable tbody:eq(2)").css({display : 'none'});
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
		case 9:
			$("#addVlanTable tbody:eq(1)").css({display : 'none'});
			if(oSelect.ETHAGG === null){
				loadSniAggList({
					async    : false,
					portType : portType,
					entityId : window.entityId,
					successCallBack : function(json){
						oSelect.ETHAGG = json;
						createAggOption(oSelect.ETHAGG);
					}
				});
			}else{//如果ETHAGG属性中有值，则直接使用;
				createAggOption(oSelect.ETHAGG);
			}
			break;
	}
}
//创建端口类型的option;
function createOption(json){
	$("#portNumberSel").empty();
	if(json.length && json.length > 0){
		/*var tpl = new Ext.XTemplate([
			   '<tpl for=".">',
			       '<option value="{portIndex}">{portName}</option>',
			   '</tpl>'
			]);
		tpl.overwrite('portNumberSel', json);*/
		var opt = '';
		for(var i=0,len=json.length; i<len; i++){
			opt += String.format('<option value="{0}">{1}</option>', json[i].portIndex, json[i].portName);
		}
		$("#portNumberSel").html(opt);
	}
}
function createAggOption(json){
	$("#aggregateGroupSel").empty();
	if(json.length > 0){
		var opt = '';
		for(var i=0,len=json.length; i<len; i++){
			opt += String.format('<option value="{0}">{0}</option>', json[i].portIndex);
		}
		$("#aggregateGroupSel").html(opt);
	}
}

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

//获取选择的组播VLAN id数组
function getSelectedVlanIds(){
	var sm = grid.getSelectionModel();
	var selectedSections = [];
	var selectedVlanIds = [];
	if (sm != null && sm.hasSelection()) {
		selectedSections = sm.getSelections();
		selectedSections.forEach(function(value){
			selectedVlanIds.push(value.data.vlanId);
		})
	}
	return selectedVlanIds;
}
