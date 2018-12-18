<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    plugin Nm3kDropdownTree
    module OnuAuth
    import gpon.javascript.PonSelector
    IMPORT gpon/javascript/GponAuthAdd
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
.x-form-field-trigger-wrap{ width:auto !important;}
.x-form-field-wrap .x-form-trigger{ height:23px;}
#lineProfileId, #srvProfileId{ width:129px !important;}
#srvProfileId{ width:169px !important;}
#gponOnuNoSel input{ height:20px !important;}
</style>
<script type="text/javascript">
var gponOnuNo = [];
var entityId = ${entityId};
var authModeList = @{JSON:authModeList}@;
var onuMaxNumInPon = 128;    //当前PON口下的ONU最大数目，default:128
var onuMaxNumList = ${onuMaxNumList};   //各个PON口支持的ONU最大数   [[num, num, num, num....],[],[]...]没有的全部赋值为0
var inited = false;
var onuNoSelStore, onuNoSelComboBox,ponSelector,allocatedOnuNoList = [];
var tempPonId;
function addGponOnuAuth(){
	var $ponIndex = ponSelector.getSelectedIndexs();
	if(!$ponIndex){
		return top.showMessageDlg("@COMMON.error@", "@OnuAuth.addAuthTip@");
	}
	sumbitGponOnuAdd(entityId,$ponIndex,tempPonId,$("#authMode").val(),function(){
		top.frames['framegponOnuAuthen'] && top.frames['framegponOnuAuthen'].reloadGridAll();
		closeClick();
	});
}

$(function(){
	ponSelector = new PonSelector({
		entityId : entityId,
		type:@{GponConstant.PORT_TYPE_GPON}@,
		singleSelect : true,
		width : 88,
		subWidth : 150,
		renderTo : "gponIndex",
		afterClick:function(node){
			if(node.length>0){
				$.each(authModeList,function(index,mode){
					if(mode.ponId == node[0].ponId){
						$("#authMode").val(mode.ponOnuAuthenMode);
						syncMode(mode.ponOnuAuthenMode);
						return false;
					}
				});
				inited = true;
				
				// 每次选中都重新render onuNo
				var arr = node[0].name.split("/");
				// arr[0] pon板 arr[1] pon口
				onuMaxNumInPon = onuMaxNumList[arr[0]][arr[1]];
				// 重新加载onuNo
				reloadOnuNoList(node[0].ponId);
				// 获取选中的ponId
				tempPonId = node[0].ponId;
			}else{
				if(onuNoSelComboBox){
					// 设置第一个为默认值
					onuNoSelComboBox.setValue(null);
				}
			}
		}
	});
	ponSelector.render({
		readyCallback : function(entityId, jsonData){
			if(V.isTrue('${gponOnuAutoFind.ponIndex}')){
				$.each(authModeList,function(index,mode){
					if(mode.ponIndex == '${gponOnuAutoFind.ponIndex}'){
						$("#authMode").val(mode.ponOnuAuthenMode);
						syncMode(mode.ponOnuAuthenMode);
						return false;
					}
				});
				ponSelector.setValue(${gponOnuAutoFind.ponIndex});
			}else{
				syncMode(WIN.DEFAULT_AUTH_MODE=1);
			}
		}
	});
	
	lineComboBox = new Ext.form.ComboBox({
		emptyText: '@COMMON.select@',
		mode: 'remote',
		width:204,
		editable:false,
		triggerAction: 'all',
		valueField : 'gponLineProfileId',
		displayField : 'gponLineProfileName',//gponLineProfileName
		applyTo: 'lineProfileId',
		store : new Ext.data.JsonStore({   
		    url:"/gpon/profile/loadLineProfileList.tv?entityId=@{entityId}@",
		    autoLoad:true,
		    fields: ["gponLineProfileId", {name:"gponLineProfileName",convert:function(v,r){return v+"("+r.gponLineProfileId+")";}}]
		})
	 });
	 
	srvComboBox = new Ext.form.ComboBox({
		emptyText: '@COMMON.select@',
		mode: 'remote',
		width:204,
		cls : 'test',
		editable:false,
		triggerAction: 'all',
		valueField : 'gponSrvProfileId',
		displayField : 'gponSrvProfileName',
		applyTo: 'srvProfileId',
		store : new Ext.data.JsonStore({   
			url:"/gpon/profile/loadServiceProfileList.tv?entityId=@{entityId}@",
			autoLoad:true,
			fields: ["gponSrvProfileId",{name:"gponSrvProfileName",convert:function(v,r){return v+"("+r.gponSrvProfileId+")";}}]
		})
	 });
	
	for(var i=1; i<=onuMaxNumInPon; i++){
		gponOnuNo.push({id:i,onuNo:i});
	}
	onuNoSelStore = new Ext.data.JsonStore({   
		fields: ["id","onuNo"],
		data: gponOnuNo
	});
	onuNoSelComboBox = new Ext.form.ComboBox({
		emptyText: '@COMMON.select@',
		mode: 'local',
		width:60,
		cls : 'test',
		editable:false,
		triggerAction: 'all',
		valueField : 'id',
		displayField : 'onuNo',
		renderTo: 'gponOnuNoSel',
		store : onuNoSelStore
	 });
});

function closeClick() {
	window.parent.closeWindow('addGponOnuAuth');
}

// 重新加载onuNo,获取未配置过的onuNo列表
function reloadOnuNoList(ponId){
	// 已配置的onuNo
	var allocatedOnuNoList = [];
	var onuNoStore = new Ext.data.Store({
		url : '/gpon/onuauth/loadOnuNoList.tv?ponId=' + ponId,
		reader : new Ext.data.ArrayReader(
    		{id : 0}, 
    		Ext.data.Record.create([{name : 'onuNoList'}])
		)
	});
	onuNoStore.load({ 
		callback: function(records, options, success){
 			// 配置过的onuNo的列表
			if(records && records.length>0){
				$.each(records, function(i, v){allocatedOnuNoList.push(v.json);})
			}
			gponOnuNo = [];
			for(var i=1; i<=onuMaxNumInPon; i++){
				if(allocatedOnuNoList.indexOf(i) == -1){
					gponOnuNo.push({id:i,onuNo:i});
				}
			}
			if(onuNoSelStore){
				// 更新combobox的store
				onuNoSelStore.remove();
				onuNoSelStore.loadData(gponOnuNo);
			}
			if(onuNoSelComboBox){
				// 设置第一个为默认值
				onuNoSelComboBox.setValue(onuNoSelComboBox.getStore().getAt(0).data.onuNo);
			}
		}
	});
}
</script>
</head>
<body class="openWinBody">
<div class="formtip" id="tips" style="display: none"></div>
	<div class="openWinHeader">
	    <div class="openWinTip">@OnuAuth.addAuth@</div>
	    <div class="rightCirIco pageCirIco"></div> 
	</div>
	<div class="edgeTB10LR20 pT30 pL10 pR10">
	    <table class="zebraTableRows" id="mainTb">
	    	<tbody>
	            <tr>
					<td class="rightBlueTxt w100" >@COMMON.required@PON@COMMON.port@@COMMON.maohao@</td>
					<td class="w140"><div style="float:left" id="gponIndex"></div><div style="float:left; margin-left:2px; width:60px;" id="gponOnuNoSel"></div></td>
					<td class="rightBlueTxt w90">@OnuAuth.authMode@@COMMON.maohao@</td>
					<td><select id="authMode" class="normalSel w190" disabled="disabled">
						<option value="1">@OnuAuth.snAuth@</option>
                        <option value="2">@OnuAuth.snPassAuth@</option>
                        <option value="3">@OnuAuth.loidAuth@</option>
                        <option value="4">@OnuAuth.loidPassAuth@</option>
                        <option value="5">@OnuAuth.passAuth@</option>
                        <option value="6">@OnuAuth.autoAuth@</option>
                        <option value="7">@OnuAuth.mixAuth@</option>
					</select></td>
				</tr>
		        <tr class="darkZebraTr">
					<td class="rightBlueTxt"><label class="authClazz  sn snpassword">@COMMON.required@</label></>SN@COMMON.maohao@</td>
					<td><input type="text" id="sn" class="normalInput w150 authClazz  sn snpassword mix" value="${gponOnuAutoFind.serialNumber}" tooltip="@OnuAuth.sn.tip@"/></td>
					<td class="rightBlueTxt"><label class="authClazz snpassword password">@COMMON.required@</label>@COMMON.password@@COMMON.maohao@</td>
					<td><input type="text" id="password" class="normalInput w190 authClazz snpassword password mix" value="${gponOnuAutoFind.password}" maxlength="10"  tooltip="@OnuAuth.password.tip@"/></td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label class="authClazz loid loidpassword">@COMMON.required@</label>LOID@COMMON.maohao@</td>
					<td><input type="text" id="loid" class="normalInput w150 authClazz loid loidpassword mix" value="${gponOnuAutoFind.loid}" maxlength="24" tooltip="@OnuAuth.loid.tip@"/></td>
					<td class="rightBlueTxt" ><label class="authClazz loidpassword">@COMMON.required@</label>LOID@COMMON.password@@COMMON.maohao@</td>
					<td><input type="text" id="loidPassword" class="normalInput w190 authClazz loidpassword mix" value="${gponOnuAutoFind.loidPassword}"  maxlength="12" tooltip="@OnuAuth.loidPass.tip@"/></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@COMMON.required@@OnuAuth.lineProfile@@COMMON.maohao@</td>
					<td><div style="width:154px;">
						<input type="text" id="lineProfileId" readonly="readonly" class="normalInput w150"  maxlength="4"/>
					</div></td>
					<td class="rightBlueTxt">@COMMON.required@@OnuAuth.srvProfile@@COMMON.maohao@</td>
					<td><div style="width:194px;">
						<input type="text" id="srvProfileId" readonly="readonly" class="normalInput w190"  maxlength="4" />
					</div></td>
				</tr>	
	        </tbody>       
	    </table>
	</div>
	<div class="edgeTB10LR20 pT10">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
     	     	 <Zeta:Button id="addAuthBt" onclick="addGponOnuAuth()" icon="miniIcoAdd">@OnuAuth.addAuth@</Zeta:Button>
		         <li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>