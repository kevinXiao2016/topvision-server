var sm, cm, store, grid, groupIpV4, sourceIpV4;
var oStaticJoin = {
	GE       : null,
	XE       : null,
	EPON     : null,
	GPON     : null,
	EPON_10G : null
}

function refreshSnpStaticFwd(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshSnpStaticJoin.tv',
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
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

function batchDelete(){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.deleteStaticJoin@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		var staticFwds = getSelectedStaticFwds();
		$.ajax({
			url : '/epon/igmpconfig/batchDeleteSnpStaticJoin.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				staticFwds : JSON.stringify(staticFwds)
			},
			dataType :　'text',
			success : function(json) {
				if (json == "error"){//后台出现错误
					top.showMessageDlg("@COMMON.tip@", "@tip.deleteStaticJoinF@");
					return;
				}else if(json != "none"){//出现删除失败
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@confirm.failDeletePorts@' + json + '</b>'
	       			});
				}else{//全部删除成功
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@tip.deleteStaticJoinS@</b>'
	       			});
				}
				//store.reload();
				refreshPage();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@tip.deleteStaticJoinF@");
			},
			cache : false
		});
	});
}

function refreshPage(){
	window.location.href = window.location.href;
}

//获取选择的级联端口
function getSelectedStaticFwds(){
	var sm = grid.getSelectionModel();
	var selectedSections = [];
	var selectedStaticFwds = [];
	if (sm != null && sm.hasSelection()) {
		selectedSections = sm.getSelections();
		selectedSections.forEach(function(record){
			var staticFwd = new Object();
			staticFwd.entityId = window.entityId,
			staticFwd.portType =  record.data.portType,
			staticFwd.portIndex = record.data.portIndex,
			staticFwd.groupIp = record.data.groupIp,
			staticFwd.groupVlan = record.data.groupVlan,
			staticFwd.groupSrcIp = record.data.groupSrcIp 
			selectedStaticFwds.push(staticFwd);
		})
	}
	return selectedStaticFwds;
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

function deleteStaticFwd(portIndex, portType){
	var selectRec = grid.getSelectionModel().getSelected();
	var toDeletaData = {
			"staticFwd.entityId"   : window.entityId,
			"staticFwd.portType"   : selectRec.data.portType,
			"staticFwd.portIndex"  : selectRec.data.portIndex,
			"staticFwd.groupIp"    : selectRec.data.groupIp,
			"staticFwd.groupVlan"  : selectRec.data.groupVlan,
			"staticFwd.groupSrcIp" : selectRec.data.groupSrcIp 
		}
	top.showConfirmDlg("@COMMON.tip@", "@confirm.deleteStaticJoin@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/igmpconfig/deleteSnpStaticJoin.tv',
			type : 'POST',
			data : toDeletaData,
			dataType :　'json',
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@tip.deleteStaticJoinS@</b>'
       			});
				refreshPage();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@tip.deleteStaticJoinF@");
			},
			cache : false
		});
	});
}

function typeRender(value, meta, record){
	if(value == 3){
		return "GE";
	}else if(value == 4){
		return "XE"
	}else if(value == 5){
		return "EPON"
	}else if(value == 6){
		return "GPON"
	}else if(value == 7){
		return "EPON_10G"
	}else{
		return "--";		
	}
}

function portRender(value, meta, record){
	return value + "/" + record.data.portNo;
}

function manuRender(value, meta, record){
	return String.format("<a href='javascript:;' onClick='deleteStaticFwd()'>@COMMON.delete@</a>"); 
}

//构建分页工具栏
function buildPagingToolBar() {
   var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store: store,
       displayInfo: true, items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-']});
   return pagingToolbar;
}

$(function(){
	sm = new Ext.grid.CheckboxSelectionModel({
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
		    {header: '@igmp.staticJoinIp@', dataIndex: 'groupIp'},
		    {header: '@igmp.staticJoinVlan@', dataIndex: 'groupVlan'},
		    {header: '@igmp.staticJoinSourceIp@', dataIndex: 'groupSrcIp'},
		    {header: '@COMMON.manu@', width: 180, fixed:true, renderer : manuRender}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/igmpconfig/loadStaticJoinList.tv',
		fields : ["entityId", "portIndex", "portType","slotNo","portNo","groupIp","groupVlan","groupSrcIp","portName"],
		root : 'data',
		totalProperty: 'rowCount',
		baseParams : {
			entityId : entityId
		}
	});
	grid = new Ext.grid.GridPanel({
		margins : '10 10 0 10',
		sm     : sm,
		cm     : cm,
		store  : store,
		region : 'center',
		tbar   : [{
			xtype : 'button',
			text : '@BUTTON.add@',
			iconCls : 'bmenu_new',
			handler : function(){
				addFn()
			}
		},'-',{
			xtype : 'button',
			text : '@igmp.fetchStaticJoin@',
			iconCls : 'bmenu_equipment',
			handler : refreshSnpStaticFwd
		},'-',{
			id : "batchDelete",
			xtype : 'button',
			text : '@igmp.batchDelete@',
			iconCls : 'bmenu_delete',
			handler : batchDelete,
			disabled: true
		}],
		stripeRows   : true,
		bbar : buildPagingToolBar(),
		viewConfig   : { forceFit:true},
		bodyCssClass : 'normalTable'
	});
	
	store.load({params: {start: 0, limit: pageSize}});
	
	new Ext.Viewport({
	    layout : 'border',
		items  : [grid]
	});
	
});//end document.ready;
function addFn(){
	openBg();
	addTpl();
}
function addTpl(){
	openFakeExtWin();
	var tpl = new Ext.XTemplate(
		'<div class="fakeExtWindowTitle"><b>@igmp.addStaticPortConfig@</b><label onclick="closeFakeWindow()"></label></div>',   
		'<div class="openWinHeader">',
	    	'<div class="openWinTip">{desc}</div>',
	    	'<div class="rightCirIco pageCirIco"></div>',
		'</div>',
		'<div class="edge10 pT30">',
			'<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
			    '<tbody>',
			    	'<tr>',
			    		'<td class="rightBlueTxt">@igmp.version@:</td>',
		    			'<td colspan="3">{igmpVersion}</td>',
			    	'</tr>',
			        '<tr class="darkZebraTr">',
			            '<td class="rightBlueTxt" width="140">@igmp.portType@:</td>',
			            '<td width="190">',
				            '<select id="typeSel" class="normalSel w180" onchange="loadPortNum()">',
					            '<option value="3">GE</option>',
					            '<option value="4">XE</option>',
					            '<option value="5">EPON</option>',
					            '<option value="6">GPON</option>',
					            '<option value="7">EPON_10G</option>',
				            '</select>',
				        '</td>',
			            '<td class="rightBlueTxt" width="140">@igmp.portNumber@:</td>',
			            '<td><select id="portNumSel" class="normalSel w180"></select></td>',
			        '</tr>',
			        '<tr>',
			            '<td class="rightBlueTxt">@igmp.staticJoinIp@:</td>',
			            '<td id="putGroupIp"></td>',
			            '<td class="rightBlueTxt">@igmp.staticJoinVlan@:</td>',
			            '<td><input id="vlanIndexInput" maxlength="4" type="text" class="normalInput w180" toolTip="@igmp.tip.tip25@"></td>',
			        '</tr>',
			        '<tr class="darkZebraTr">',
		            	'<td class="rightBlueTxt">@igmp.staticJoinSourceIp@:</td>',
		            	'<td colspan="3" id="putSourceIp"></td>',
		            '</tr>',
			    '</tbody>',
			'</table>',
   			'<div class="noWidthCenterOuter clearBoth">',
   		        '<ol class="upChannelListOl pB0 pT40 noWidthCenter">',
   		            '<li><a href="javascript:;" class="normalBtnBig" onclick="addRecord()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>',
   		            '<li><a href="javascript:;" class="normalBtnBig" onclick="closeFakeWindow()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>',
   		        '</ol>',
   		     '</div>',
   		 '</div>'
   		);
	var $fakeExtWindow = Ext.get("fakeExtWindow"),
	    aPos = $fakeExtWindow.getCenterXY();
	
	tpl.overwrite($fakeExtWindow, {
		igmpVersion :　versionObj[igmpVersion],
		desc　　　　　　: '<p><b class="orangeTxt"> v2 </b>@igmp.tip.tip11@</p>' +
		       　　　　　　　　　'<p><b class="orangeTxt"> v3-only </b>@igmp.tip.tip12@</p>'
	});
	$fakeExtWindow.setXY([aPos[0],2]);
	createIpInput();
	switch(versionObj[igmpVersion]){
	case 'V2': //v2 版本，不允许存在配置源ip的组播节目,即将源ip输入框设置为disabled，值为0.0.0.0;
		window.sourceIpV4.setValue("0.0.0.0");
		window.sourceIpV4.setDisabled(true);
		break;
	}
	loadPortNum();
}
function createIpInput(){
	window.groupIpV4 = new ipV4Input('groupIp','putGroupIp',null);
	window.groupIpV4.width(180);
	
	window.sourceIpV4 = new ipV4Input('sourceIp','putSourceIp',null);
	window.sourceIpV4.width(180);
}
//根据所选的端口类型，加载端口号数据;
function loadPortNum(){
	var portType = parseInt( $("#typeSel").val(), 10 );
	switch(portType){
	case 3:
		if(oStaticJoin.GE === null){
			loadCascadePortByType({
				portType        : portType,
				entityId        : window.entityId,
				successCallBack : function(json){
					oStaticJoin.GE = json;
					createPortSel(json);
				}	
			});
		}else{
			createPortSel(oStaticJoin.GE);
		}
		break;
	case 4:
		if(oStaticJoin.XE === null){
			loadCascadePortByType({
				portType        : portType,
				entityId        : window.entityId,
				successCallBack : function(json){
					oStaticJoin.XE = json;
					createXeSel(json);
				}	
			});
		}else{
			createXeSel(oStaticJoin.XE);
		}
		
		break;
	case 5:
		if(oStaticJoin.EPON === null){
			loadPonListByType({
				portType        : portType,
				entityId        : window.entityId,
				successCallBack : function(json){
					oStaticJoin.EPON = json;
					createPortSel(json);
				}	
			});
		}else{
			createPortSel(oStaticJoin.EPON);
		}
		break;
	case 6:
		if(oStaticJoin.GPON === null){
			loadPonListByType({
				portType        : portType,
				entityId        : window.entityId,
				successCallBack : function(json){
					oStaticJoin.GPON = json;
					createPortSel(json);
				}	
			});
		}else{
			createPortSel(oStaticJoin.GPON);
		}
		break;
	case 7:
		if(oStaticJoin.EPON_10G === null){
			loadPonListByType({
				portType        : portType,
				entityId        : window.entityId,
				successCallBack : function(json){
					oStaticJoin.EPON_10G = json;
					createPortSel(json);
				}	
			});
		}else{
			createPortSel(oStaticJoin.EPON_10G);
		}
		break;
	}
}
function createPortSel(list){
	var opt = '';
	for(var i=0,len=list.length; i<len; i++){
		opt += String.format('<option value="{0}">{1}</option>', list[i].portIndex, list[i].portName);
	}
	$("#portNumSel").html(opt);
}
function createXeSel(list){
	var opt = '';
	for(var i=0,len=list.length; i<len; i++){
		opt += String.format('<option value="{0}">{1}</option>', list[i].portIndex, list[i].portName);
	}
	$("#portNumSel").html(opt);
}
//根据端口类型获取端口列表, 选择EPON EPON10G类型时使用
function loadPonListByType(o){
	$.ajax({
		url : '/epon/igmpconfig/loadPonListByType.tv',
		type : 'POST',
		data : {
			entityId : o.entityId,
			portType : o.portType
		},
		dataType :　'json',
		success : function(json) {
			if( o.successCallBack ){
				o.successCallBack(json);
			}
		},
		error : function(json) {
			if( o.failCallBack ){
				o.failCallBack(json);
			}
		},
		cache : false
	});
}
function addRecord(){
	var $portNumSel     = $("#portNumSel"), 
	    groupIpValue    = groupIpV4.getValue(),
	    sourceIpValue   = sourceIpV4.getValue();
	
	if( $portNumSel.val() == null){
		top.showMessageDlg("@COMMON.tip@", "@tip.withoutPort@");
		return;
	}
	if( !checkedIpValue(groupIpValue) || !checkDDIsMulticast(groupIpValue)){
		$("#groupIp :text").eq(0).focus();
		return;
	}
	var flag = customValidateFn([{
		id    : 'vlanIndexInput',
		range : [2,4094]
	}]);
	if(flag !== true){
		flag.focus();
		return;
	};
	switch( versionObj[window.igmpVersion] ){
	case 'V2'://V2模式下，组播源IP必须为0.0.0.0
		if(sourceIpValue != "0.0.0.0"){
			return;
		}
		break;
	case 'V3'://v3可以为0.0.0.0或者是有效在ABC类地址;
		if(sourceIpValue != "0.0.0.0"){
			if( !checkIsNomalIp(sourceIpValue) ){
				$("#sourceIp :text").eq(0).focus();
				return;
			}
		}
		break;
	case 'V3-only'://必须是abc类，且不能为0.0.0.0;
		if( !checkIsNomalIp(sourceIpValue) ){
			$("#sourceIp :text").eq(0).focus();
			return;
		}
		break;
	}
	addStaticFwd({
		"staticFwd.entityId"   : window.entityId,
		"staticFwd.portType"   : $("#typeSel").val(),
		"staticFwd.portIndex"  : $portNumSel.val(),
		"staticFwd.groupIp"    : groupIpValue,
		"staticFwd.groupVlan"  : $("#vlanIndexInput").val(),
		"staticFwd.groupSrcIp" : sourceIpValue 
	});
}

function addStaticFwd(dataObj){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/addSnpStaticJoin.tv',
		type : 'POST',
		data : dataObj,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.addStaticJoinS@</b>'
   			});
			refreshPage();
			closeFakeWindow();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.addStaticJoinF@");
		},
		cache : false
	});
}

//根据端口类型获取级联端口列表, 选择GE XE类型时使用
function loadCascadePortByType(o){
	$.ajax({
		url : '/epon/igmpconfig/loadCascadePortByType.tv',
		type : 'POST',
		data : {
			entityId : o.entityId,
			portType : o.portType
		},
		dataType :　'json',
		success : function(json) {
			if( o.successCallBack ){
				o.successCallBack(json);
			}
		},
		error : function(json) {
			if( o.failCallBack ){
				o.failCallBack(json);
			}
		},
		cache : false
	});
}
