<%@ page language="java" contentType="text/html;charset=utf-8"%>
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
var entityId = '${entityId}';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
function saveClick() {
	var srcIndex = window.srcPon.getValue();
	if(!srcIndex){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.srcPonErr);
		return srcPon.focus();
	}
	var dstIndex = window.dstPon.getValue();
	if(!dstIndex){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.dstPonErr);
		return dstPon.focus();
	}
	window.parent.showWaitingDlg( I18N.COMMON.wait, I18N.ELEC.ponCuttingOver , 'ext-mb-waiting');
	Ext.Ajax.request({
		url:'/epon/elec/cfgElecPonCutover.tv',disableCaching :true,
		params:{
			entityId:entityId,
			srcPonIndex : srcIndex,
			dstPonIndex : dstIndex
		},
		success : function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.cutoverOk);
			cancelClick();
		},failure :function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.cutoverEr);
		}
	})
}
function cancelClick() {
    window.parent.closeWindow('ponCutOver');
}

Ext.onReady(function(){
	window.srcPon = new Ext.form.ComboBox({ 
		forceSelection: true,
        store: new Ext.data.JsonStore({
            autoLoad : true,
            url: '/epon/elec/loadPonCutOverPort.tv',
            baseParams : {"entityId": entityId, _r : Math.random()},
            fields: ['portIndex','portName']
        }),
        width: 200,
        valueField: 'portIndex',
        displayField: 'portName',
        triggerAction: 'all',
        editable: false,
        disabled:!operationDevicePower,
        mode: 'local',
        renderTo : 'srcPon',
        listeners: {
            'change':function(o,nv,ov){//当主端口选择了后,需要重新刷新备端口列表
                var store = window.dstPon.getStore()
                store.setBaseParam("ponCutOverPortIndex",nv);
                store.reload();
            }
        }
    });
	
	window.dstPon = new Ext.form.ComboBox({ 
		forceSelection: true,
		store: new Ext.data.JsonStore({
            autoLoad : true,
            url: '/epon/elec/loadPonCutOverPort.tv',
            baseParams : {"entityId": entityId, _r : Math.random()},
            fields: ['portIndex','portName']
        }),
        width: 200,
        valueField: 'portIndex',
        displayField: 'portName',
        triggerAction: 'all',
        editable: false,
        disabled:!operationDevicePower,
        mode: 'local',
        renderTo : 'dstPon',
        listeners: {
            'change':function(o,nv,ov){//当主端口选择了后,需要重新刷新备端口列表
                var store = window.srcPon.getStore()
                store.setBaseParam("ponCutOverPortIndex",nv);
                store.reload();
            }
        }
    });
});

function authLoad(){
	if(!operationDevicePower){
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
	<body class=openWinBody onload="authLoad();">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@ELEC.comCutover@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt w220">@ELEC.srcPon@</td>
						<td><div id="srcPon"></div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ELEC.dstPon@</td>
						<td><div id="dstPon"></div></td>
					</tr>
				</table>
			</form>
		</div>

		<Zeta:ButtonGroup>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>