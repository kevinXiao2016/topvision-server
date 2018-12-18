<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<TITLE><fmt:message bundle='${cmc}' key='CMC.title.DHCPServerConfig'/></TITLE>
<%@include file="/include/meta.inc"%>
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
var serverList = ${dhcpServerList};
serverList = serverList.join("")=="false" ? new Array() : serverList;

var data = new Array();
var grid;
var store;

function loadData(){
	if(serverList!=undefined && serverList!=null){
		var tmpL = serverList.length;
		if(tmpL > 0){
			for(var i=0; i<tmpL; i++){
				data[i] = new Array();
				data[i][0] = serverList[i].topCcmtsDhcpServerIndex;
				data[i][1] = serverList[i].topCcmtsDhcpServerIpAddr;
				data[i][2] = serverList[i].topCcmtsDhcpServerIpMask;
				data[i][3] = serverList[i].topCcmtsDhcpServerType;
			}
		}
	}
}
function loadGrid(){
	store = new Ext.data.SimpleStore({
		data : data,
		fields: ['id','ip','ipMask','type']
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
				header: I18N.CMC.title.SERVERType,
				dataIndex: 'type',
				align: 'center',
				sortable: true,
				width: 130,
				renderer: function(value, cellmeta, record) {
					switch (parseInt(value))
					{
					case 1: return I18N.CMC.title.anyEntityType;
						break;
					case 2: return "CM";
						break;
					case 3: return I18N.CMC.text.host;
						break;
					case 4: return "MTA";
						break;
					case 5: return "STB";
						break;
					default: return I18N.CMC.text.unKnown;
					}
				}
			},{
				header: I18N.CM.operate,
				dataIndex: 'idx',
				align: 'center',
				width: 70,
				renderer : function(value, cellmeta, record) {
					return "<img src='/images/delete.gif' onclick='deleteRule(\"" + record.data.id + "\")'/>";
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

function modifyDhcpServer(s){
	var tmpStr = [I18N.CMC.text.add,I18N.CMC.text.Modify];
	var sel = grid.getSelectionModel().getSelected();
	if(s==1 && sel != null && sel != undefined){
		var index = sel.get("id");
		if(index==null || index=="" || index<0 || isNaN(index)){
			index = -1;
		}
		var type = sel.get('type');
		if(type==null || isNaN(type) || type<1 || type>5){
			type = -1;
		}
		var ip = sel.get('ip');
		if(ip == "" || ip == null){
			ip = "noData";
		}
		var ipMask = sel.get('ipMask');
		if(ipMask == "" || ipMask == null){
			ipMask = "noData";
		}
		window.parent.createDialog("dhcpServerModify", tmpStr[s] + I18N.CMC.title.DHCPServerConfig, 320, 240, 
				'/cmc/dhcp/showDhcpServerModify.tv?cmcId='+ cmcId +'&modifyFlag=1&index='+ index +'&type='+
				type +'&ip='+ ip + '&ipMask='+ ipMask, null, true, true);
	}else if(s==0){
		window.parent.createDialog("dhcpServerModify", tmpStr[s] + I18N.CMC.title.DHCPServerConfig, 320, 240, 
				'/cmc/dhcp/showDhcpServerModify.tv?cmcId='+ cmcId +'&modifyFlag=0&index=-1&type=-1&ip=noData&ipMask=noData', null, true, true);
	}else{
		window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.selectCfgItem);
	}
}

function deleteRule(index){
	window.top.showOkCancelConfirmDlg(I18N.CMC.title.tip, I18N.CMC.text.SureToDel, function (type) {
    	if(type=="ok"){
    		window.top.showWaitingDlg(I18N.CMC.title.wait , I18N.CMC.text.doingDel);
    		Ext.Ajax.request({
    			url : '/cmc/dhcp/deleteDhcpServer.tv?cmcId=' + cmcId + '&index=' + index,
    			success : function(response) {
    				if(response.responseText != 'success'){
        				window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.doingDel);
    					return;
    				}
    				window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.delSuccess);
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
    				window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.doingDel);
    			}
    		});
    	}
	});
}
function cancelClick() {
	window.parent.closeWindow('dhcpServer');	
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
				onclick="modifyDhcpServer(0)"><fmt:message bundle='${cmc}' key='CMC.text.addSetting'/></button>
			&nbsp;&nbsp;
			<button id="modifyBt" class=BUTTON75
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="modifyDhcpServer(1)"><fmt:message bundle='${cmc}' key='CMC.label.ModifyCfg'/></button>
			&nbsp;&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.label.close'/></button>
		</div>
</BODY>
</HTML>