<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<TITLE><fmt:message bundle='${cmc}' key='CMC.title.dhcpIpCfg'/></TITLE>
<%@include file="/include/cssStyle.inc"%>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<link rel="STYLESHEET" type="text/css" href="/css/gui.css">
<link rel="STYLESHEET" type="text/css" href="/css/ext-all.css">
<link rel="STYLESHEET" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId" />;
var intIpList = ${dhcpIntIpList};
intIpList = intIpList.join("")=="false" ? new Array() : intIpList;

var data = new Array();
var grid;
var store;

function loadData(){
	if(intIpList!=undefined && intIpList!=null){
		var tmpL = intIpList.length;
		if(tmpL > 0){
			for(var i=0; i<tmpL; i++){
				data[i] = new Array();
				data[i][0] = intIpList[i].topCcmtsDhcpIntIpIndex;
				data[i][1] = intIpList[i].topCcmtsDhcpIntIpAddr;
				data[i][2] = intIpList[i].topCcmtsDhcpIntIpMask;
				data[i][3] = intIpList[i].topCcmtsDhcpIfIndex;
			}
		}
	}
}
function loadGrid(){
	store = new Ext.data.SimpleStore({
		data : data,
		fields: ['id','ip','ipMask','ifIndex']
	});
	grid = new Ext.grid.GridPanel({
		id : 'grid',
		renderTo : 'gridDiv',
		store : store,
		width : 555,
		height : 315,
		frame : false,
		autoScroll : true,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns: [{
				header: "ID",
				dataIndex: 'id',
				align: 'center',
				sortable: true,
				width: 70
			},{
				header: I18N.CMC.title.SERVERIP,
				dataIndex: 'ip',
				align: 'center',
				sortable: true,
				width: 130
			},{
				header: "IP Mask",
				dataIndex: 'ipMask',
				align: 'center',
				sortable: true,
				width: 130
			},{
				header: I18N.CMC.text.assPort,
				dataIndex: 'ifIndex',
				align: 'center',
				sortable: true,
				width: 130
			},{
				header:I18N.CM.operate,
				dataIndex: 'idx',
				align: 'center',
				width: 70,
				renderer : function(value, cellmeta, record) {
					return "<img src='/images/delete.gif' onclick='deleteRule(\"" + record.data.id + "\",\"" + record.data.ifIndex + "\")'/>";
				}
			}],
		listeners: {
			'viewready': {
				fn : gridReady,
				scope : this
			}
		}
	});
}
function gridReady(){
	if(data.length > 0){
		grid.getSelectionModel().selectFirstRow();
	}
}

function modifyDhcpIntIp(s){
	var tmpStr = [I18N.CMC.text.add,I18N.CMC.text.Modify];
	var sel = grid.getSelectionModel().getSelected();
	if(s==1 && sel != null && sel != undefined){
		var index = sel.get("id");
		if(index==null || index=="" || index<0 || isNaN(index)){
			index = -1;
		}
		var ifIndex = sel.get('ifIndex');
		if(ifIndex==null || isNaN(ifIndex) || ifIndex<0){
			ifIndex = -1;
		}
		var ip = sel.get('ip');
		if(ip == "" || ip == null){
			ip = "noData";
		}
		var ipMask = sel.get('ipMask');
		if(ipMask == "" || ipMask == null){
			ipMask = "noData";
		}
		window.parent.createDialog("dhcpIntIpModify", tmpStr[s] + I18N.CMC.title.dhcpIpCfg, 320, 240, 
				'/cmc/dhcp/showDhcpIntIpModify.tv?cmcId='+ cmcId +'&modifyFlag=1&index='+ index +'&ifIndex='+
				ifIndex +'&ip='+ ip + '&ipMask='+ ipMask, null, true, true);
	}else if(s==0){
		window.parent.createDialog("dhcpIntIpModify", tmpStr[s] + I18N.CMC.title.dhcpIpCfg, 320, 240, 
				'/cmc/dhcp/showDhcpIntIpModify.tv?cmcId='+ cmcId +'&modifyFlag=0&index=-1&ifIndex=-1&ip=noData&ipMask=noData', null, true, true);
	}else{
		window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.selectCfgItem);
	}
}

function deleteRule(index, ifIndex){
	window.top.showOkCancelConfirmDlg(I18N.CMC.title.tip, I18N.CMC.text.SureToDel, function (type) {
    	if(type=="ok"){
    		window.top.showWaitingDlg(I18N.CMC.title.wait,I18N.CMC.text.doingDel);
    		Ext.Ajax.request({
    			url : '/cmc/dhcp/deleteDhcpIntIp.tv?cmcId=' + cmcId + '&index=' + index + '&ifIndex=' + ifIndex,
    			success : function(response) {
    				if(response.responseText != 'success'){
        				window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.delFailed);
    					return;
    				}
    				window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.delSuccess );
    				var tmpL = data.length;
    				var tmpNum = -1;
					for(var i=0; i<tmpL; i++){
						if(data[i][0] == index){
							tmpNum = i;
						}
					}
					if(tmpNum != -1){
						data.splice(tmpNum, 1);
						store.loadData(data);
						if(data.length > 0){
							grid.getSelectionModel().selectRow(tmpNum-1<0 ? 0 : tmpNum-1, true);
						}
					}else{
						location.reload();
					}
    			},
    			failure : function() {
    				window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.delFailed);
    			}
    		});
    	}
	});
}
function cancelClick() {
	window.parent.closeWindow('dhcpIntIp');	
}
Ext.onReady(function () {
	loadData();
	loadGrid();
});
function refreshClick(){
	
}
</script>
</HEAD>
<BODY style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND>
	<div id="gridDiv"></div>
	<div align="right" style="padding-right: 5px; padding-top: 7px;">
			<button class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
				onMouseOut="this.className='BUTTON95'"
				onMouseDown="this.className='BUTTON_PRESSED95'"
				onclick="refreshClick()"><fmt:message bundle='${cmc}' key='text.refreshData'/></button>
				&nbsp;&nbsp;
			<button id="addBt" class=BUTTON75
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="modifyDhcpIntIp(0)"><fmt:message bundle='${cmc}' key='CMC.text.addSetting'/></button>
			&nbsp;&nbsp;
			<button id="modifyBt" class=BUTTON75
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="modifyDhcpIntIp(1)"><fmt:message bundle='${cmc}' key='CMC.label.ModifyCfg'/></button>
			&nbsp;&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.label.close'/></button>
		</div>
</BODY>
</HTML>