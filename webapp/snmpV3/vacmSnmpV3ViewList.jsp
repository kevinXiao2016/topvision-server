<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module snmpV3
    import snmpV3.javascript.SnmpV3ViewGrid
</Zeta:Loader>
<script type="text/javascript">
	var viewGrid;
	var entityId = ${entityId};
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	Ext.onReady(function() {
		viewGrid = new SnmpV3ViewGrid({
			renderTo : "viewGridCont",
			height : 365
		});
		viewGrid.getStore().reload();
	});

	/**
	 * 添加SNMP V3
	 */
	function addViewHandler() {
		window.top.createDialog('viewAdditionWizard', "@VIEW.addView@", 600, 360,
				'/snmp/showVacmViewAddtion.tv?entityId=' + entityId, null,true, true,function(){
			viewGrid.getStore().reload();
		});
	}

	/**
     * 修改SNMP V3
     */
    function modifyBtClick(id) {
    	var rec = viewGrid.getStore().getById(id).data;
  		var url = String.format("/snmp/showVacmViewAddtion.tv?entityId={0}&snmpViewName={1}&snmpViewSubtree={2}",entityId, rec.snmpViewName, rec.snmpViewSubtree);
        window.top.createDialog('viewAdditionWizard', "@VIEW.mdfView@", 600, 360,url, null,true, true,function(){
        	viewGrid.getStore().reload();
        });
    }
	
	/**
	 * 关闭页面
	 */
	function closeHandler() {
		window.parent.closeWindow('usmSnmpV3ViewList');
	}

	function deleteBtClick(id){
		 var rec = viewGrid.getStore().getById(id).data;
		 if(rec.snmpViewName == 'two'){
			 return window.parent.showMessageDlg("@COMMON.error@", "@VIEW.canntDel@","error");
		 }
		 window.parent.showConfirmDlg("@COMMON.tip@", String.format("@VIEW.cfmDelView@",rec.snmpViewName), function(type) {
             if (type == 'no'){return;} 
             window.top.showWaitingDlg("@COMMON.wait@", "@VIEW.deleting@", 'ext-mb-waiting');
             $.ajax({
                 url: '/snmp/deleteView.tv',cache:false,
                 data:{
                     entityId: entityId,
                     snmpViewName: rec.snmpViewName,
                     snmpViewSubtree: rec.snmpViewSubtree
                 },success:function(){
                     window.parent.showMessageDlg("@COMMON.tip@", String.format("@VIEW.delViewOk@",rec.snmpViewName));
                     viewGrid.getStore().reload();
                 },error:function(){
                     window.parent.showMessageDlg("@COMMON.tip@", String.format("@VIEW.delViewEr@",rec.snmpViewName));
                 }
             });
         });
     }
		
	
	function fetchHandler(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.waiting@", 'ext-mb-waiting');
	    $.ajax({
	        url: '/snmp/refreshSnmpV3Config.tv',cache:false,
	        data:{
	            entityId: entityId
	        },success:function(){
	            window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
	            viewGrid.getStore().reload();
	        },error:function(){
	            window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchEr@");
	        }
	    });
	}
	
	function authLoad(){
		if(!operationDevicePower){
			R.addUserBt.setDisabled(true);
		}
	}
</script>
</head>
<body class="openWinBody" onload="authLoad()">
<div class="edge10">
	<div id="viewGridCont"></div>
</div>
<Zeta:ButtonGroup>
	<Zeta:Button  id="addUserBt" onClick="addViewHandler()" icon="miniIcoAdd">@VIEW.addView@</Zeta:Button>
	<Zeta:Button onClick="fetchHandler()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
	<Zeta:Button onClick="closeHandler()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
</Zeta:ButtonGroup>
</Zeta:HTML>