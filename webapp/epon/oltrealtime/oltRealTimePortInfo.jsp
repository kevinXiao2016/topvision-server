<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
</Zeta:Loader>
<style type="text/css">
.txtLeftTh{text-indent: 16px;}
</style>

<script type="text/javascript">
	var ponStore, ponGrid;
	var entityId = ${entityId};
	var onlineFlag = ${onlineFlag};
	
	function renderStatus(value, p, record){
		if (value == 1) {
			return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_on.png" border=0 align=absmiddle>',
				"@COMMON.online@");	
		} else {
			return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_off.png" border=0 align=absmiddle>',
				"@resources/COMMON.offline@");	
		}
	}
	
	function renderPower(value, p, record){
		var onlineStatus = record.data.operationStatus;
		if(onlineStatus == 1 && value != 0){
			return value.toFixed(2);
		}else{
			return "--";
		}
	}
	
	function speedRender(value, p, record){
		if(parseInt(value) == -1){
			return "--";
		}else{
			return value;
		}
	}
	
	function renderNum(value, p, record){
		var onlineStatus = record.data.operationStatus;
		if(onlineStatus == 1){
			return value;
		}else{
			return "--";
		}
	}
	
	function refreshHandler(){
		ponStore.reload();
	}

	$(document).ready(function(){
		var cm = new Ext.grid.ColumnModel([
			{header:"@ELEC.portNo@", dataIndex:"ponLocation", width:60},
			{header:"@RSTP.portStatus@", dataIndex:"operationStatus", width:60, renderer : renderStatus},
			{header:"@report.inFlow@(Mbps)", dataIndex:"portInSpeed", width:80, renderer : speedRender},
			{header:"@report.outFlow@(Mbps)", dataIndex:"portOutSpeed", width:80, renderer : speedRender},
			{header:"@Optical.tx@(dBm)", dataIndex:"transPower", width:80, renderer : renderPower},
			{header:"@Optical.rx@(dBm)", dataIndex:"recvPower", width:80, renderer : renderPower},
			{header:"@REALTIME.downLink@", dataIndex:"downLinkCount", width:80, renderer : renderNum},
			{header:"@REALTIME.cmNumber@", dataIndex:"cmNum", width:80, renderer : renderNum}
		]);//end cm;
		var buildToolBar = [
           {text: '@COMMON.refresh@',cls:'mL10', iconCls: 'bmenu_refresh', handler: refreshHandler}
        ];
		
		ponStore = new Ext.data.JsonStore({
			url : '/epon/oltRealtime/loadOltPonInfo.tv',
			fields : ["ponLocation", "operationStatus","cmNum", "transPower","recvPower", "downLinkCount","portInSpeed","portOutSpeed"],
			baseParams : {
				entityId : entityId,
				onlineFlag : onlineFlag
			}
		});
		
		ponGrid = new Ext.grid.GridPanel({
			region: 'center',
			border: false,
			stripeRows:false,
			tbar:buildToolBar,
			enableColumnMove: false,
			enableColumnResize: true,
			bodyCssClass: 'normalTable',
			store: ponStore,
			cm : cm,
			loadMask : {
	            msg :'@entitySnapPage.loading@'
	        },
			viewConfig:{
				forceFit: true
			}
			//If the Grid's view is configured with forceFit=true the autoExpandColumn is ignored;
		});
		
		var viewPort = new Ext.Viewport({
			layout: 'border',
			border: false,
		    items: [ponGrid]
		}); 
		ponStore.load(); 
	   	
	});//end document.ready;
	
</script>
</head>
<body class="whiteToBlack">

</body>
</Zeta:HTML>
