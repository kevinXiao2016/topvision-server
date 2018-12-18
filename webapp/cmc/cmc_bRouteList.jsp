<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
</Zeta:Loader>
<head>
<script type="text/javascript">
/**
 The index of route table, value range is 0-77.
 The first 12(0-11) entries are read-write for static route which user can configure manually;
 The next 64(12-75) entries are readonly for direct route of VLAN interface;
 The last 2(76-77) entries are readonly for direct route of outband interface.
 */
var _MAX_ROUTE_NUM = 11;//网管能增加的index为0-11
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var entityId =${entityId};
var cmcId =${cmcId};
var view = null;
function RouteView(){
	this.grid = null;
	this.store = null;
	this.init.apply(this, arguments);
}
RouteView.prototype = {
	init: function(userOptions){
		var cmOption = userOptions.cm,
		    storeOption = userOptions.store,
		    defaultSort = userOptions.defaultSort,
		    gridOption = userOptions.grid,
		    events = userOptions.event;
		this.cm = new Ext.grid.ColumnModel(cmOption);
		this.store = new Ext.data.JsonStore(storeOption);
		if(defaultSort){
			this.store.setDefaultSort(defaultSort.index, defaultSort.type);
		}	
		gridOption.cm = this.cm;
		gridOption.store = this.store;
		this.grid = new Ext.grid.GridPanel(gridOption);	
		if(events){
			for(var i = 0; i < events.length; i++){
				var event = event[i];
				this.grid.on(event.name, event.action);
			}
		}
		this.store.load();
	},
	reload: function (){
		this.store.reload();
	}
};
RouteView.cancelClick = function (id){
	window.parent.closeWindow(id);
}
function showAutoCloseMessageDlg(tip, callback, delayTime){
	var time = delayTime ? delayTime : 500;
	window.parent.showMessageDlg("@COMMON.tip@", tip);
	var f = function (){
		window.top.closeWaitingDlg();
		if(callback){
			callback();
		}		
	}
    setTimeout(f, time); 
}
function contains(arr, obj) {  
    var i = arr.length;  
    while (i--) {  
        if (arr[i] === obj) {  
            return true;  
        }  
    }  
    return false;  
} 
function createRoute(){
	var index = -1;
	var usedIndex = [];
	view.store.each(function (){
		if(this.data.topCcmtsRouteIndex <= _MAX_ROUTE_NUM){
			usedIndex.push(this.data.topCcmtsRouteIndex);
		}
	});
	
	for(var i = 0 ; i <= _MAX_ROUTE_NUM ; i++){
		if(!contains(usedIndex,i)){
			index = i;
			break;
		}
	}
	
	if(index == -1){
		window.parent.showMessageDlg('@COMMON.tip@', '@route.routeNumLimit@');
        return;
	}
	window.top.createDialog('modifyRouteView', "@route.addStaticRoute@", 600, 370, '/cmc_b/route/showModifyRouteView.tv?entityId=' 
            + entityId + "&action=" + 1 + "&index=" + index, null, true, true, function (){
        refreshRoute();
    });
}
function modifyRoute(){
	var sm = view.grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var record = sm.getSelected();  
        var topCcmtsRouteIndex = record.data.topCcmtsRouteIndex;
        var nextHop = record.data.topCcmtsRouteNexthop;
        var topCcmtsRouteDstIp = record.data.topCcmtsRouteDstIp;
        var topCcmtsRouteIpMask = record.data.topCcmtsRouteIpMask;
        var topCcmtsRouteNexthop = record.data.topCcmtsRouteNexthop;
        if(nextHop == "*" || nextHop.trim() == "0.0.0.0"){
            window.parent.showMessageDlg("@COMMON.tip@", "@route.modifyDirectRouteTip@");
            return;
        }
		window.top.createDialog('modifyRouteView', "@route.modifyStaticRoute@", 320, 240, '/cmc_b/route/showModifyRouteView.tv?entityId=' 
	            + entityId + "&action=" + 2 + "&topCcmtsRouteDstIp=" + topCcmtsRouteDstIp + "&topCcmtsRouteIpMask=" 
	            + topCcmtsRouteIpMask + "&topCcmtsRouteNexthop=" + topCcmtsRouteNexthop + "&index=" + topCcmtsRouteIndex, 
	            null, true, true, function (){
			refreshRoute();
		});
    } else {
        window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.pleaseSelectFirst@");
    }   
}
function deleteRoute(){
	var sm = view.grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var record = sm.getSelected();  
        var topCcmtsRouteIndex = record.data.topCcmtsRouteIndex;
        var nextHop = record.data.topCcmtsRouteNexthop;
        if(nextHop == "*" || nextHop.trim() == "0.0.0.0"){
        	window.parent.showMessageDlg("@COMMON.tip@", "@route.deleteDirectRouteTip@");
        	return;
        }
        var topCcmtsRouteDstIp = record.data.topCcmtsRouteDstIp;
        var topCcmtsRouteIpMask = record.data.topCcmtsRouteIpMask;
        window.top.showOkCancelConfirmDlg("@COMMON.tip@", "@CMC.tip.sureToDeleteConfig@", function (type) {   
            if(type == "ok"){
		        //window.top.showWaitingDlg("@COMMON.waiting@", "@text.configuring@", 'ext-mb-waiting');
		        $.ajax({
		          url: '/cmc_b/route/deleteRouteConfig.tv?entityId='+entityId + "&route.topCcmtsRouteIndex=" + topCcmtsRouteIndex 
		        		  + "&route.topCcmtsRouteDstIp=" + topCcmtsRouteDstIp + "&route.topCcmtsRouteIpMask=" + topCcmtsRouteIpMask ,
		          type: 'post',
		          success: function(response) {
		                if(response == "success"){
		                	//window.top.closeWaitingDlg();
		                	top.afterSaveOrDelete({
		           				title: '@COMMON.tip@',
		           				html: '<b class="orangeTxt">@CMC.text.delsuccess@</b>'
		           			});
		                	refreshRoute();
		                    /* showAutoCloseMessageDlg("@CMC.text.delsuccess@", function (){
		                        refreshRoute();
		                    }); */
		                 }else if(response = "SnmpNoResponse"){
		                     window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.noResponseTip@");
		                 }else{
		                     window.parent.showMessageDlg("@COMMON.tip@", "@CMC.text.delfailed@");
		                 }
		            }, error: function(response) {
		                window.parent.showMessageDlg("@COMMON.tip@", "@CMC.text.delfailed@");
		            }, cache: false
		        });
            }
        });
    } else {
        window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.pleaseSelectFirst@");
    }	
}
function refreshRoute(){
	view.reload();
}
function nextHopRender(val, p, record){
	if(!val || val.trim() == "0.0.0.0"){
		return "*";
	}else{
		return val;
	}
}
function operationPower(){
	if(!operationDevicePower){
		Ext.getCmp("addRoute").disable();
		Ext.getCmp("delRoute").disable();
		//Ext.getCmp("modifyRoute").disable();
	}else{
		Ext.getCmp("addRoute").enable();
        Ext.getCmp("delRoute").enable();
	}
}
function createRouteView(){
	var width = 528;
	var height = 270;
	var routeOptions = {
			cm:[
				    {header: '@route.destinationIp@', width: (width-10)/4, sortable: false, align: 'center', dataIndex: 'topCcmtsRouteDstIp'},
				    {header: '@CMC.text.subnetmask@', width: (width-10)/4, sortable: false, align: 'center', dataIndex: 'topCcmtsRouteIpMask'},
				    {header: '@route.nextHop@', width: (width-10)/4, sortable: false, align: 'center', dataIndex: 'topCcmtsRouteNexthop', renderer: nextHopRender},
				    {header: '@route.port@', width: (width-10)/4, sortable: false, align: 'center', dataIndex: 'topCcmtsRoutePort'}
			    ],
			store:{
				url: ('/cmc_b/route/getRouteViewData.tv?entityId=' + entityId),
		        root: 'data',
		        fields:['topCcmtsRouteIndex', 'topCcmtsRouteDstIp', 'topCcmtsRouteIpMask', 
		                'topCcmtsRouteNexthop', 'topCcmtsRoutePort']
			},
			grid:{
				id: 'routeViewGrid', 
		        //width: width, 
		        height: height,
		        animCollapse: false, 
		        trackMouseOver: false, 
		        border: true,  		        
		        renderTo: 'routeView-div',
		        loadMask: true,
		        bodyCssClass: "normalTable",
		        viewConfig:{
	                forceFit:true               
	            },
		        tbar:[
	                   {text: "@route.button.add@",id: 'addRoute', iconCls: "bmenu_new", handler: createRoute},
	                   //{text: "@route.button.modify@", id: 'modifyRoute', iconCls: "bmenu_edit", handler: modifyRoute},
	                   {text: "@route.button.delete@", id: 'delRoute', iconCls: "bmenu_delete", handler: deleteRoute},
	                   '->',
	                   {text: "@route.button.refresh@", id: 'refreshRoute', iconCls:"bmenu_refresh", handler: refreshRoute}
	                 ]
			}
	};
	view = new RouteView(routeOptions);	
	view.store.on("exception", function (){
		Ext.getCmp("addRoute").disable();
        Ext.getCmp("delRoute").disable();
		window.parent.showMessageDlg("@COMMON.tip@", "@route.dataCollectFailTip@");
	});
	view.store.on("load", function (){
		operationPower()
    });
}
Ext.onReady(createRouteView);
</script>
</head>
<body class="openWinBody">
    <div id="routeView-div" class="pL16 pT20 pR16 pB5"></div>
    <div class="noWidthCenterOuter clearBoth">
         <ol class="upChannelListOl pB0 pT10 noWidthCenter">
             <li><a id=cancelBt onclick="RouteView.cancelClick('cmc_bRouteView')" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
         </ol>
    </div>
</body>
</Zeta:HTML>