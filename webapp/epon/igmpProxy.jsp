<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
    import js.tools.ipText static
</Zeta:Loader>
<script type="text/javascript">
/*************
 * 变量定义区**
 *************/
var entityId = <s:property value="entityId"/>;
var proxys = ${igmpProxyParaTables} ? ${igmpProxyParaTables} : new Array();
var proxyGrid;
var proxyStore;
var proxyData = new Array();
var proxyRowIndex = 0;
var proxyIdList = new Array();
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
/*************
 * 页面初始化区**
 *************/
Ext.onReady(function(){
	loadCtcOltProxyInfo();
	initProxyGrid();
});

//获取CTC模式下OLT侧的Proxy数据
function loadCtcOltProxyInfo(){
	for(var i=0;i<proxys.data.length;i++){
		proxyData[i] = new Array();
		proxyData[i][0] = proxys.data[i].proxyIndex;
		proxyData[i][1] = proxys.data[i].proxyChName;
		proxyData[i][2] = proxys.data[i].proxyName;
		proxyData[i][3] = proxys.data[i].proxyMulticastVID;
		proxyData[i][4] = proxys.data[i].proxySrcIPAddress;
		proxyData[i][5] = proxys.data[i].proxyMulticastIPAddress;
		proxyData[i][6] = proxys.data[i].multicastMaxBW;
		proxyIdList[i] = proxys.data[i].proxyIndex;
	}
}

function manuRender(value, cellmeta, record) {
	if(operationDevicePower){
		return String.format("<a href='javascript:;' onclick='showViewOperation(\"{0}\",\"{1}\")'/>@COMMON.view@</a>" +
		        			 " / <a href='javascript:;' onClick='onDeleteClick(\"{0}\",\"{1}\");'>@COMMON.delete@</a>"+
		        			 " / <a href='javascript:;' onclick='showMoreBt(\"{0}\",event)'/>@COMMON.other@</a>", record.data.proxyId,record.data.proxyMvlanId );
	}else{
		return String.format("<a href='javascript:;' onclick='showViewOperation(\"{0}\",\"{1}\")>@COMMON.view@</a> / <a href='javascript:;' onclick='showMoreBt(\"{0}\",event)'/>@COMMON.other@</a>",record.data.proxyId, record.data.proxyMvlanId);
	}
}

function onDeleteClick(proxyId,proxyMvlanId){
    deleteProxy(proxyId,proxyMvlanId)
}

function showViewOperation(proxyId){
    showProxyInfo(proxyId)
}
function showMoreBt(proxyId,event){
    var record = proxyGrid.getStore().getById(proxyId);  // Get the Record
    //proxyGrid.getSelectionModel().selectRecords([record]);
	proxyMenu.showAt([event.clientX,event.clientY]);
}

/**
 * 初始化表格
 */
function initProxyGrid(){
	proxyStore = new Ext.data.SimpleStore({
		data : proxyData,
		fields : ['proxyId','proxyChName','proxyName','proxyMvlanId','srcIp','mvlanIp','maxBW']
	});
	proxyGrid = new Ext.grid.GridPanel({
	    stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
		id : 'proxyGrid',
		renderTo : 'proxyGridPanel',
		store : proxyStore,
		height : 310,
		autoScroll : true,
		viewConfig:{forceFit: true},
		selModel : new Ext.grid.RowSelectionModel({ singleSelect : true }),
		columns: [
		  	{header: I18N.IGMP.mvlanId ,dataIndex: 'proxyId',align: 'center', sortable:true},
		  	{header: I18N.IGMP.mvlanAlias ,dataIndex: 'proxyChName',align: 'center', hidden:true, sortable:true},
		  	{header: I18N.IGMP.mvlanName ,dataIndex: 'proxyName',align: 'center', sortable:true},
		  	{header: I18N.IGMP.mvlanVlanId,dataIndex: 'proxyMvlanId',align: 'center', sortable:true},
		  	{header: I18N.IGMP.mvlanIp,dataIndex: 'mvlanIp',align: 'center', sortable:true},
		  	{header: I18N.IGMP.maxBD,dataIndex: 'maxBW',align: 'center', hidden : true, sortable:true,renderer : function(value){return value + "Kbps";}},
			{header: I18N.COMMON.manu ,dataIndex: 'id',align: 'center', sortable:true,renderer : manuRender	}]
	});
}
function preventBubble(e) {
	e = e || window.event;
    if (e.stopPropagation) {
        e.stopPropagation();           //火狐阻止冒泡
    } else {
        e.cancelBubble = true;         //IE阻止冒泡
    }
}
/**
 * 右键菜单，定义了删除，修改，查看proxy的入口
 */
var proxyMenu = new Ext.menu.Menu({
    id:'proxyMenu',
    enableScrolling: false,
    items:[ {
        id: I18N.IGMP.mdfMvlanInfo,
        text: I18N.IGMP.mdfMvlanInfo,
        disabled:!operationDevicePower,
        handler : showProxyInfo
    } ,{
		id: I18N.IGMP.viewMvlanUserInfo,
		text: I18N.IGMP.viewMvlanUserInfo,
		handler: showIgmpForwarding
    } ]
});
function showIgmpForwarding(){
	var deviceIgmpMode = 1;
	var url = '/epon/igmp/getIgmpMode.tv?entityId=' + entityId + '&r='+ Math.random();
	Ext.Ajax.request({
		url : url,
		method:'post',
		success : function(response) {
			if(response.responseText.indexOf('success') != 0 || response.responseText.length != 8){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.connectError);
				return;
			}
			deviceIgmpMode = parseInt(response.responseText.substring(7, 8));
			if(deviceIgmpMode == 1){
				window.parent.createDialog("igmpForwarding", I18N.IGMP.viewMvlanUserInfo, 900, 620, "/epon/igmp/showIgmpForwarding.tv?entityId="+
		    			entityId + "&forwardingType=2" , null, true, true);		
			}else if(deviceIgmpMode == 2){
				window.parent.createDialog("igmpForwarding", I18N.IGMP.viewMvlanUserInfo, 900, 620, "/epon/igmp/showIgmpForwarding.tv?entityId="+
		    			entityId  + "&forwardingType=3", null, true, true);		
			}
			cancelClick();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.connectError)
		}
	});
}
/*************
 * 数据操作区**
 *************/
/**
 * 删除一个频道
 * param proxyIndex,entityId
 */
function deleteProxy(proxyIndex){
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.IGMP.cfmDelMvlan, function(type) {
		if (type == 'no'){return;}
		window.top.showWaitingDlg(I18N.COMMON.wait,  I18N.IGMP.deletingMvlan + proxyIndex + '...', 'ext-mb-waiting');
		Ext.Ajax.request({
			url:'/epon/igmp/deleteIgmpProxy.tv?entityId='+entityId+'&proxyIndex='+proxyIndex + '&r='+ Math.random(),
			method:'post',
			success:function(response){
				if(response.responseText != 'success'){
					window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.delMvlanEr , proxyIndex))
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.delMvlanOk , proxyIndex))
				var selectedRow = proxyGrid.getSelectionModel().getSelected();
				var flagNum = -1;
				for(var i=0; i<proxyData.length; i++){
					if(proxyData[i][0] == proxyIndex){
						flagNum = i;
					}
				}
				if(flagNum > -1){
					proxyData.splice(flagNum,1);
					proxyStore.loadData(proxyData);
				}
			},
			error:function(){
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SUPPLY.delMvlanEr , proxyIndex))
			}
		});
	});
}

/**
 * 展示一个代理组的详情
 */
function showProxyInfo(s){
	var proxyId = 0;
	var tempSel = null;
	tempSel = proxyGrid.getSelectionModel().getSelected();
	if( tempSel ){
		proxyId = tempSel.data.proxyId;
	}
	if(s == 0){
		tempText = I18N.IGMP.createMvlan
		proxyId = 0;
	}else{
		tempText = I18N.IGMP.mvlanDetail
	}
	if(tempSel!=null && tempSel!=undefined){
		var proxyChName = tempSel.get('proxyChName');
		var proxyName = tempSel.get('proxyName');
		var proxyMvlanId = tempSel.get('proxyMvlanId');
		var srcIp = tempSel.get('srcIp');
		var mvlanIp = tempSel.get('mvlanIp');
		var maxBW = tempSel.get('maxBW');
	}else{
		var proxyChName = "NONE";
		var proxyName = "NONE";
		var proxyMvlanId = 0;
		var srcIp = "0.0.0.0";
		var mvlanIp = "0.0.0.0";
		var maxBW = 0;
	}
	var proxyIdListStr = proxyIdList.join("q");
	window.top.createDialog("igmpProxyDetail", tempText, 600, 410, "epon/igmp/showIgmpProxyDetail.tv?entityId="+entityId+"&proxyId="+
			proxyId+"&proxyChName="+proxyChName+"&proxyName="+proxyName+"&proxyMvlanId="+proxyMvlanId+"&srcIp="+srcIp
		+"&mvlanIp="+mvlanIp+"&maxBW="+maxBW+"&proxyIdList="+proxyIdListStr, null, true, true);
}
function refreshClick(){
	var params = {
		entityId : entityId
	};
	var url = '/epon/igmp/refreshIgmpProxy.tv?r=' + Math.random();
	window.top.showWaitingDlg(I18N.COMMON.wait , I18N.IGMP.fetchingMvlan , 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchMvlanError)
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchMvlanOk)
			window.location.reload();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchMvlanError)
		},
		params : params
	});
}
function authLoad(){
	if(!operationDevicePower){
	    R.addBt.setDisabled(true);
	}
}
</script>
</head>
<body class=openWinBody onload="authLoad()">
	<div class="edge10">
		<div id="proxyGridPanel"></div>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoSaveOK">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="addBt" onClick="showProxyInfo(0)" icon="miniIcoAdd">@IGMP.createMvlan@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>

<script type="text/javascript">
/**
 * 关闭页面
 */
function cancelClick(){
	window.parent.closeWindow('igmpProxy');
}
</script>
</Zeta:HTML>