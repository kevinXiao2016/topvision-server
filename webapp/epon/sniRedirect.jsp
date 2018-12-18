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
</Zeta:Loader>
<script type="text/javascript">
var entityId = '<s:property value="entity.entityId"/>';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var redirectGrid;
var redirectStore;
function cancelClick() {
    window.parent.closeWindow('sniRedirect');
}
function addSniRedirect() {
	window.top.createDialog('sniRedirectAdd', I18N.SERVICE.addRedirect , 600, 300, '/epon/showAddSniRedirect.tv?entityId=' + entityId, null, true, true);
}
function deleteSniRedirect(sniName, sniId) {
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.cfmDelPortRedirect , sniName) , function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.SERVICE.delingPortRedirect ,  sniName) , 'ext-mb-waiting')
		Ext.Ajax.request({
			url : '/epon/deleteSniRedirect.tv',
			success : function(response) {
                if (response.responseText) {
                    window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.delPortRedirectEr , sniName) )
                } else {
                    window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.delPortRedirectOk , sniName) )
                    checkAvailiable();
                    redirectStore.load();
                }
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.delPortRedirectEr , sniName) )
			},
			params : {
				entityId: entityId,
				sniId: sniId
			}
		})
	})
}
function checkAvailiable() {
	$.ajax({
		type: 'GET',
		url: '/epon/loadAvailableSniRedirect.tv?entityId=' + entityId,
		dataType: 'json',
		success: function(json) {
			if (json && json.length > 1) {
				if(operationDevicePower){
					$('#addRedirect').attr('disabled', false);
				}
			} else {
				$('#addRedirect').attr('disabled', true);
			}
		}
	});
}
Ext.onReady(function() {
	checkAvailiable();
	redirectStore = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({url: '/epon/loadSniRedirect.tv?entityId=' + entityId + '&r=' + Math.random()}),
        reader: new Ext.data.JsonReader({}, Ext.data.Record.create([
        	{
        		name: 'srcPortName'
        	}, {
        		name: 'topSniRedirectGroupSrcPortId'
        	}, {
        		name: 'dstPortName'
        	}, {
        		name: 'topSniRedirectGroupDirection'
        	}]))
    });
    redirectStore.load();
    redirectGrid = new Ext.grid.GridPanel({
        stripeRows:true,region: "center",bodyCssClass: 'normalTable',
        viewConfig : {forceFit : true},
        renderTo: 'redirectGrid',
        height: 350,
        autoScroll:true,
        columns: [{
            header: I18N.SERVICE.originPort , dataIndex: 'srcPortName',  align: 'center'
        }, {
            header: I18N.SERVICE.targetPort , dataIndex: 'dstPortName',  align: 'center'
        }, {
        	header: I18N.EPON.direct , dataIndex: 'topSniRedirectGroupDirection',  align: 'center', renderer: function(value) {
        		if (value == 1) {
        			return I18N.EPON.indirect
        		} else if (value == 2) {
        			return I18N.EPON.outdirect
        		} else {
        			return I18N.SERVICE.doubleDirect
        		}
        	}
        }, {
        	header: I18N.COMMON.manu , dataIndex: 'id', align: 'center', renderer: function(value, cellmeta, record) {
        		if(operationDevicePower){
	        		return "<img src='/images/delete.gif' onclick='deleteSniRedirect(\"" + record.data.srcPortName + "\", " + record.data.topSniRedirectGroupSrcPortId + ")'/>";
        		}else{
        			return "<img src='/images/deleteDisable.gif'/>";
        		}
        	}
        }],
        sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
        store: redirectStore
    });
});
function authLoad(){
	if(!operationDevicePower){
		R.addBt.setDisabled(true);
	}
}
</script>
</head>
<body class=openWinBody onload="authLoad()">
<div class="edge10">
	<div id="redirectGrid"></div>
</div>
	<Zeta:ButtonGroup>
		<Zeta:Button id="addBt" onClick="addSniRedirect()" icon="miniIcoAdd">@SERVICE.addRedirect@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>