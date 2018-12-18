<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
<script>
var w = 110
var entityId = <s:property value="entityId"/>
$(document).ready(function(){
	window.ppgBox = new Ext.form.ComboBox({ 
	    store: new Ext.data.JsonStore({
	    	autoLoad : true,
	    	url: '/epon/ponprotect/loadPPGAvialList.tv',
	    	baseParams : {"entityId": entityId, _r : Math.random()},
	    	root:'data',
	        fields: ['id']
	    }),
	    width: w,
	    valueField: 'id',
	    displayField: 'id',
	    triggerAction: 'all',
	    editable: false,
	    mode: 'local',
	    renderTo : 'PPGID'
	})


	window.masterBox = new Ext.form.ComboBox({ 
	    forceSelection: true,
	    store: new Ext.data.JsonStore({
	    	autoLoad : true,
	    	url: '/epon/ponprotect/loadPPGAvialMasterPort.tv',
	    	baseParams : {"entityId": entityId, _r : Math.random()},
	    	root:'data',
	        fields: ['port','index']
	    }),
	    width: w,
	    handleHeight :1,
	    valueField: 'index',
	    displayField: 'port',
	    triggerAction: 'all',
	    mode: 'local',
	    renderTo : 'masterPort',
	    listeners: {
			'change':function(o,nv,ov){//当主端口选择了后,需要重新刷新备端口列表
				var store = window.slaveBox.getStore()
				store.setBaseParam("masterIndex",nv)
				store.reload()
			}
		}
	})

	window.slaveBox = new Ext.form.ComboBox({
	    forceSelection: true,
	    store: new Ext.data.JsonStore({
	    	autoLoad : true,
	    	url: '/epon/ponprotect/loadPPGAvialSlavePort.tv',
	    	baseParams : {"entityId": entityId, _r : Math.random()},
	    	root:'data',
	        fields: ['port','index']
	    }),
	    width: 132,
	    valueField: 'index',
	    displayField: 'port',
	    triggerAction: 'all',
	    mode: 'local',
	    renderTo : 'slavePort',
	    listeners: {
			'change':function(o,nv,ov){//当备端口选择了后,需要重新刷新主端口列表
				var store = window.masterBox.getStore()
				store.setBaseParam("slaveIndex",nv)
				store.reload()
			}
		}
	})
})

function save(){
	var ppgId = ppgBox.getValue()
	var master = masterBox.getValue()
	var slave = slaveBox.getValue()
	//var alias = $("#alias").val()
	if(!master)
		return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.idnotnull)
	if(!master)
		return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.masternotnull)
	if(!slave)
		return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.slavenotnull)
	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.ponprotect.adding , ppgId) ,'ext-mb-waiting') 
	$.ajax({
		url : '/epon/ponprotect/addPPG.tv',cache:false,
		data : {ppgId : ppgId, masterIndex : master, slaveIndex : slave , entityId : entityId },
		success :  function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.addOk)
			window.parent.getFrame("entity-"+entityId).reload(true)
			cancelClick()
		},
		error : function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.addEr)
		}
	})
}

function cancelClick(){
	window.parent.closeWindow('createPonProtect');
}

</script>
</head>
	<body class=openWinBody>
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@ponprotect.newPPG@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt"><label for=PPGID>@ponprotect.ppgId@:<font
								color="red">*</font></label></td>
						<td colspan="3"><span id=PPGID></span></td>
						<!--<td class="rightBlueTxt"><label for==alias>@ponprotect.alias@:</label></td>
						<td><input id=alias class="normalInput" type="text" /></td>-->
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt"><label>@ponprotect.master@:<font color="red">*</font></label></td>
						<td><span id=masterPort></span></td>
						<td class="rightBlueTxt"><label>@ponprotect.slave@:<font color="red">*</font></label></td>
						<td><span id=slavePort></span></td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="save()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>