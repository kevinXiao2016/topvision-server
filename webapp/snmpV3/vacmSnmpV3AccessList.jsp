<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
    import snmpV3.javascript.SnmpV3AccessGrid
</Zeta:Loader>
<script type="text/javascript">
var accessGrid;
var entityId = ${entityId};
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
Ext.onReady(function(){
    accessGrid = new SnmpV3AccessGrid({
        renderTo : "accessGridCont",
        height: 365
    });
    accessGrid.getStore().reload();
});

/**
 * 添加SNMP V3
 */
function addUserHandler(){
    window.top.createDialog('userAdditionWizard', "@GROUP.addSnmpv3Group@" , 600, 410,
            '/snmp/showVacmAccessAddtion.tv?entityId=' + entityId, null, true, true,function(){
    	 accessGrid.getStore().reload();
    });
}

/**
 * 关闭页面
 */
function closeHandler(){
    window.parent.closeWindow('usmSnmpV3GroupList');
}

/**
 * 从设备获取数据
 */
function fetchHandler(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@" , 'ext-mb-waiting');
    $.ajax({
        url: '/snmp/refreshSnmpV3Config.tv',cache:false,
        data:{
            entityId: entityId
        },success:function(){
            window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@" );
            accessGrid.getStore().reload();
        },error:function(){
            window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchEr@" );
        }
    });
}

/**
 * 删除组
 */
function deleteBtClick(id){
    var rec = accessGrid.getStore().getById(id).data;
    //TODO 当前使用的组不应该被删除
    window.parent.showConfirmDlg("@COMMON.confirm@", String.format("@GROUP.cfmDelGroup@" ,rec.snmpGroupName), function(type) {
        if (type == 'no'){return;} 
        window.top.showWaitingDlg(I18N.COMMON.wait, "@COMMON.deleting@" , 'ext-mb-waiting');
        $.ajax({
            url: '/snmp/deleteAccess.tv',cache:false,
            data:{
                entityId: entityId,
                snmpGroupName: rec.snmpGroupName,
                snmpSecurityLevel: rec.snmpSecurityLevel
            },success:function(){
                window.parent.showMessageDlg("@COMMON.tip@", String.format("@GROUP.delOk@", rec.snmpGroupName));
                accessGrid.getStore().reload();
            },error:function(){
                window.parent.showMessageDlg("@COMMON.tip@", String.format("@GROUP.delEr@", rec.snmpGroupName));
            }
        });
    });
}

/**
 * 修改组
 */
function modifyBtClick(id){
	 var rec = accessGrid.getStore().getById(id).data;
	 var url =  '/snmp/showVacmAccessAddtion.tv?entityId=' + entityId+"&snmpGroupName="+rec.snmpGroupName+"&snmpSecurityLevel=" + rec.snmpSecurityLevel;
	 window.top.createDialog('userAdditionWizard', "@GROUP.mdfSnmpV3Group@" ,  600, 410,url, null, true, true,function(){
		 accessGrid.getStore().reload();
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
	<div id="accessGridCont"></div>
</div>
<Zeta:ButtonGroup>
		<Zeta:Button id="addUserBt" onClick="addUserHandler()" icon="miniIcoAdd">@GROUP.addGroup@</Zeta:Button>
		<Zeta:Button onClick="fetchHandler()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button onClick="closeHandler()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
</Zeta:ButtonGroup>
</body>
</Zeta:HTML>
