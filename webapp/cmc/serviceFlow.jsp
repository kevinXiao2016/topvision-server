<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
	
<link rel="stylesheet" type="text/css" href="/css/ext-formhead-linefeed.css" />

<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="ems"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>

<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/cmc/rendererFunction.js"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var cmcId = <s:property value="cmcId"/>;
var store;
var baseGrid;
var baseColumns;
var scGrid;
function onRefreshClick() {
	store.reload();
}
//服务流页面刷新
function refreshServiceFlow(){
	window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.refreshData, 'ext-mb-waiting');
	$.ajax({
	      url: 'cmc/qos/refreshServiceFlow.tv?cmcId=' + cmcId ,
	      type: 'post',
	      success: function(response) {
  	    	if(response=="true"){
  	    		onRefreshClick();
  	    		window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshSuccessTip);
				 }else{
					 window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
				 }
			}, error: function(response) {
				window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
			}, cache: false
		}); 
}
function showParamSet(){
	 var sm = baseGrid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var sId = sm.getSelected().data.sId;
		window.top.createDialog('serviceFlowSet', I18N.QoS.serviceFlowParamSet, 400, 650, 
			'cmc/qos/showQosParamSetInfo.tv?serviceFlowId=' + sId , null, true, true);	
	} 
}

function showPktClass(){
	var sm = baseGrid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var sId = sm.getSelected().data.sId;
		window.top.createDialog('serviceFlowPktClass', I18N.QoS.serviceFlowPktClass, 400, 650, 
			'cmc/qos/showQosPktClassInfo.tv?serviceFlowId=' + sId, null, true, true);	
	}
}
function showServiceFlowCmInfo(){
	var sm = baseGrid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var sId = sm.getSelected().data.sId;
		window.top.createDialog('serviceFlowToCm', I18N.CCMTS.relaCMInfo, 400, 310, 
			'cmc/qos/showQosAssociatedCmInfo.tv?serviceFlowId=' + sId, null, true, true);	
	} 
}
function buildPagingToolBar(store) {
     var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
        displayInfo: true, items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize), '-'
                        		]});
    return pagingToolbar;
}


Ext.BLANK_IMAGE_URL = "/images/s.gif";
function createServiceFlowList(){
	var entityMenu = new Ext.menu.Menu({ id: 'serviceFlowMenu', minWidth: 160, 
		enableScrolling: false,
		items:[
		//{text: I18N.QoS.showPktClass, handler: showPktClass},
		{text: I18N.QoS.showParamSet, handler: showParamSet},'-',
		{text: I18N.QoS.showServiceFlowCmInfo, handler: showServiceFlowCmInfo}
	]});
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 100;
	baseColumns = [		
		{header: I18N.QoS.serviceFlowId, width:parseInt(w/6), sortable:true, align: 'center', dataIndex: 'docsQosServiceFlowId'},	
		{header: 'SID', width: parseInt(w/6), sortable:false, align: 'center', dataIndex: 'docsQosServiceFlowSID'},
		{header: 'CM MAC', width: parseInt(w*15/48), sortable:false, align: 'center', dataIndex: 'statusMacAddress'},	
		{header: I18N.QoS.direction, width: parseInt(w/6), sortable:false, align: 'center', dataIndex: 'docsQosServiceFlowDirectionString'},
		{header: I18N.QoS.serviceFlowPrimary, width: parseInt(w/6), sortable:false, align : 'center', dataIndex: 'docsQosServiceFlowPrimary',renderer:renderYesOrNo}    
		];
	store = new Ext.data.JsonStore({
	    url: ('getCmcQosServiceFlowListInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    totalProperty: 'rowCount',
	    //remoteSort: true, 
	    fields: ['sId', 'docsQosServiceFlowId', 'docsQosServiceFlowSID', 'statusMacAddress', 'docsQosServiceFlowDirectionString', 
	    		'docsQosServiceFlowPrimary', 'flowPkts', 'flowOctets', 'docsQosServiceFlowTimeCreatedString', 
	    		'docsQosServiceFlowTimeActiveString', 'flowPHSUnknowns', 'flowPolicedDropPkts', 'flowPolicedDelayPkts']
	});
	store.setDefaultSort('docsQosServiceFlowId', 'ASC');	
		
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({id: 'extbaseGridContainer', 
		width: w, 
		height: h,
		animCollapse: animCollapse, 
		trackMouseOver: trackMouseOver, 
		border: true, 
		store: store, 
		cm: baseCm,
		title: I18N.QoS.serviceFlowList,
		//viewConfig: { forceFit:true,enableRowBody: true, showPreview: false},
		renderTo: 'serviceFlow-div',
		bbar: buildPagingToolBar(store)
		});
	baseGrid.on('rowcontextmenu', function(baseGrid, rowIndex, e) {
    	e.preventDefault();
   		var sm = baseGrid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}    	
		entityMenu.showAt(e.getPoint());
    });
    
    store.load({params:{start:0, limit: pageSize}});
}

function createServiceClass(){	
	window.top.createDialog('createServiceClass', I18N.QoS.createServiceClass, 500, 510, 
    			'cmcQos/showCreateServiceClass.tv?cmcId=' + cmcId, null, true, true);
}

function modifyServiceClass(){
	var sm = scGrid.getSelectionModel();	
	if (sm != null && sm.hasSelection()) {
		var scId = sm.getSelected().data.scId;
		var direction = sm.getSelected().data.classDirection;
		if(direction == 2){
			window.top.createDialog('upServiceClass', I18N.QoS.upServiceClass, 500, 510, 
    			'cmcQos/showUpServiceClassConfigInfo.tv?scId=' + scId + '&cmcId=' + cmcId, null, true, true);
    	}else{
    		window.top.createDialog('downServiceClass', I18N.QoS.downServiceClass, 500, 280, 
    			'cmcQos/showDownServiceClassConfigInfo.tv?scId=' + scId + '&cmcId=' + cmcId, null, true, true);
    	}	
    }
}
function deleteServiceClass(){
	var sm = scGrid.getSelectionModel();
	if(sm != null && sm.hasSelection()){
		var scId = sm.getSelected().data.scId;
		$.ajax({
        url: 'cmcQos/deleteServiceClass.tv?cmcId=' + cmcId +'&scId=' + scId,
        type: 'post',
        success: function(json) {
        	window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.topo.virtualDeviceList.removeSuccess);
        	}, error: function(json) {
            window.top.showErrorDlg(I18N.RECYLE.tip, I18N.COMMON.error);
        }, dataType: 'json',cache: false
   		});
	}	
}

function createServiceClassList(){
	var serviceClassMenu = new Ext.menu.Menu({ id: 'serviceClassMenu', minWidth: 160, 
		enableScrolling: false,
		items:[
		{text: I18N.QoS.modifyServiceClass, handler: modifyServiceClass},
		{text: I18N.QoS.createServiceClass, handler: createServiceClass},'-',
		{text: I18N.COMMON.deleteAction, handler: deleteServiceClass}
	]});
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 140;
	
	var scColumns = [
			//{header: 'scId',width: 90,sortable:false,align: 'center',dataIndex:'scId'},
			{header: 'Name',width: 100,sortable: true,align: 'center',dataIndex:'className'},
			//{header: 'Direction',width: 100,sortable:false,align: 'center',dataIndex:'classDirection'},
			{header: 'Direction',width: 100,sortable:false,align: 'center',dataIndex:'docsQosServiceClassDirectionString'},
			{header: 'Status',width: 100,sortable:false,align: 'center',dataIndex:'docsQosServiceClassStatusString'},
			{header: 'Prority',width: 50,sortable:false,align: 'center',dataIndex:'classPriority'},
			{header: 'MaxTrfRate',width: 100,sortable:false,align: 'center',dataIndex:'classMaxTrafficRate'},
			{header: 'MaxTrfBurst',width: 100,sortable:false,align: 'center',dataIndex:'classMaxTrafficBurst'},
			{header: 'MinRsrvRate',width: 100,sortable:false,align: 'center',dataIndex:'classMinReservedRate'},
			{header: 'MinRsrvPkt',width: 100,sortable:false,align: 'center',dataIndex:'classMinReservedPkt'},			
			{header: 'TotalParamSets',width: 100,sortable:false,align: 'center',dataIndex:'totalParamSetNum'}];
	var scStore = new Ext.data.JsonStore({ 
			url:('getServiceClassList.tv?cmcId=' + cmcId),
			root:'data',
			totalProperty: 'rowCount',			
			fields:['scId','className','classDirection','docsQosServiceClassDirectionString','docsQosServiceClassStatusString','classPriority',
					'classMaxTrafficRate','classMaxTrafficBurst','classMinReservedRate',
					'classMinReservedPkt', 'totalParamSetNum']
			});
	//scStore.setDefaultSort('className','ASC');
	scStore.load();
	var scCm = new Ext.grid.ColumnModel(scColumns);
	scGrid = new Ext.grid.GridPanel({
			id: 'extsrvClsGridContainer',
			width:w,
			height:h,
			border: false, 
			trackMouseOver: trackMouseOver, 
			store: scStore, 
			cm: scCm,
			renderTo: 'serviceClass-div',
			bbar: buildPagingToolBar(scStore)
			});
	scGrid.on('rowdblclick', function(scGrid, rowIndex, e) {
		var sm = scGrid.getSelectionModel();	
		if (sm != null && sm.hasSelection()) {
		var scId = sm.getSelected().data.scId;
		var direction = sm.getSelected().data.classDirection;
		if(direction == 2){
			window.top.createDialog('upServiceClass', I18N.QoS.upServiceClass, 500, 510, 
    			'cmcQos/showUpServiceClassConfigInfo.tv?scId=' + scId + '&cmcId=' + cmcId, null, true, true);
    	}else{
    		window.top.createDialog('downServiceClass', I18N.QoS.downServiceClass, 500, 280, 
    			'cmcQos/showDownServiceClassConfigInfo.tv?scId=' + scId + '&cmcId=' + cmcId, null, true, true);
    	}	
    }
    });	
    scGrid.on('rowcontextmenu', function(baseGrid, rowIndex, e) {
    	e.preventDefault();
   		var sm = baseGrid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}    	
		serviceClassMenu.showAt(e.getPoint());
    });
}

Ext.onReady(function(){
	createServiceFlowList();
	Zeta$('serviceFlow-table').style.display = 'block';
	Zeta$('serviceFlow-div').style.display = 'block';
});
function queryClick(){
	var direction = Zeta$('direction').value;
	var cmMac = Zeta$('cmMac').value;
	var pattern = /^[a-z0-9:]{1,17}$/i;
	if(cmMac.length != 0 && cmMac.match(pattern)==null){
		Ext.Msg.alert(I18N.RECYLE.tip, I18N.CCMTS.macErrorMessage);
		return;
		}
	if(!validateMacAddress(cmMac) && cmMac != '' && cmMac != null){
		window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CCMTS.macError);
		return;
		}
	store.on("beforeload",function(){
		store.baseParams={direction: direction, cmMac: cmMac, start:0,limit:pageSize};
		});
	store.load({params: {direction: direction, cmMac: cmMac, start:0,limit:pageSize}});	
}

</script>
<title><fmt:message bundle="${cmc}" key="CCMTS.serviceFlow"/></title>
</head>
<body class=BLANK_WND style="margin: 15px;">
		<div id=header>
			<%@ include file="entity.inc"%>
		</div>	
  	  		<table id="serviceFlow-table" width=100% style="margin-top:10px; margin-bottom:5px" cellspacing=0 cellpadding=0>
				<tr>
					<td style="padding-right: 40px;">
						<input id="refreshInfo" class=BUTTON120 type="button" 
				        onMouseOver="this.className='BUTTON_OVER120'"
				        onMouseOut="this.className='BUTTON120'"
				        onMouseDown="this.className='BUTTON_PRESSED120'"
						onclick="refreshServiceFlow()" value="<fmt:message bundle='${cmc}' key='text.refreshData'/>"></input>
					</td>
					<td style="width: 40px; padding-left: 5px;" align="right"><fmt:message bundle='${cmc}' key='QoS.direction'/></td>
					<%-- <td style="width: 100px;"><select id="direction" style="width: 100px" onclick="queryClick()"> --%>
					<td style="width: 100px;"><select id="direction" style="width: 100px">			
							<option value="0" <s:if test="type==0">selected</s:if>><fmt:message bundle='${cmc}' key='CCMTS.All'/></option>
							<option value="1" <s:if test="type==1">selected</s:if>><fmt:message bundle='${cmc}' key='CCMTS.downStream'/></option>
							<option value="2" <s:if test="type==2">selected</s:if>><fmt:message bundle='${cmc}' key='CCMTS.upStream'/></option>																
					</select></td>	
					<td style="width: 50px; padding-left: 20px;">CM MAC:</td>
					<!-- <td style="width: 150px;"><input id="cmMac" value="" onblur="checkInfo();" maxlength="17"/></td> -->
					<td style="width: 150px;"><input id="cmMac"  maxlength="17"/></td>
					<td style="width: 50px;"><button id=saveBt class=BUTTON75
						type="button" onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onMouseDown="this.className='BUTTON_PRESSED75'"
						onclick="queryClick()"><fmt:message bundle='${ems}' key='COMMON.query'/></button>
					</td>
				</tr>
			</table>
		 <div id="serviceFlow-div"></div>
</body>
<script language="JavaScript" type="text/javascript">
function validateMacAddress(macaddr)
{
   var reg1 = /^([A-Fa-f0-9]{2,2}\:){0,5}[A-Fa-f0-9]{0,2}$/;
   var reg2 = /^([A-Fa-f0-9]{2,2}\-){0,5}[A-Fa-f0-9]{0,2}$/;
   if (reg1.test(macaddr)) {
      return true;
   } else if (reg2.test(macaddr)) {
      return true;
   } else {
      return false;
   }
}

function addListener(element,e,fn){    
    if(element.addEventListener){    
    element.addEventListener(e,fn,false);    
    } else {    
    element.attachEvent("on" + e,fn);    
    }    
}
function checkInfo(){
	var s = $('#cmMac');
	var cmMacAddress = Zeta$('cmMac').value;
	if(cmMacAddress.length == 0){
		return;
	}
	var pattern = /^[a-z0-9:]{1,17}$/i;
	var result = cmMacAddress.match(pattern);
	if(result==null){
		Ext.Msg.alert(I18N.RECYLE.tip, I18N.CCMTS.macErrorMessage);
	}
}
</script>
</html>