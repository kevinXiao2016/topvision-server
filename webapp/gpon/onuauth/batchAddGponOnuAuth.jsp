<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module gpon
    css css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript">
	var entityIds = "${entityIds}";
	var entityId = entityIds.split(",")[0];
	 Ext.onReady(function() {
		//线路模块
		lineComboBox = new Ext.form.ComboBox({
			emptyText: '@COMMON.select@',
			mode: 'remote',
			width:204,
			editable:false,
			triggerAction: 'all',
			valueField : 'gponLineProfileId',
			displayField : 'gponLineProfileName',
			applyTo: 'lineProfileId',
			store : new Ext.data.JsonStore({   
			    url:"/gpon/profile/loadLineProfileList.tv?entityId="+entityId,
			    autoLoad:true,
			    fields: ["gponLineProfileId", {name:"gponLineProfileName",convert:function(v,r){return v+"("+r.gponLineProfileId+")";}}]
			})
		 });
		 //业务模板
		srvComboBox = new Ext.form.ComboBox({
			emptyText: '@COMMON.select@',
			mode: 'remote',
			width:204,
			editable:false,
			triggerAction: 'all',
			valueField : 'gponSrvProfileId',
			displayField : 'gponSrvProfileName',
			applyTo: 'srvProfileId',
			store : new Ext.data.JsonStore({   
				url:"/gpon/profile/loadServiceProfileList.tv?entityId="+entityId,
				autoLoad:true,
				fields: ["gponSrvProfileId",{name:"gponSrvProfileName",convert:function(v,r){return v+"("+r.gponSrvProfileId+")";}}]
			})
		 });
	})
	//取消修改端口信息
	function cancelClick() {
		window.parent.closeWindow("batchAddGponOnuAuth");
	}
	 //校验
	 function validateModule(){
			var $srvProfileId = $("#srvProfileId");
			var $lineProfileId = $("#lineProfileId");
			var srvProfileId= srvComboBox.getValue();
			 var lineProfileId= lineComboBox.getValue();
			if(!Validator.isInRange(lineProfileId,[1,1024])){
				 return $lineProfileId.focus() && false;
			 }
			 if(!Validator.isInRange(srvProfileId,[1,1024])){
				 return $srvProfileId.focus() && false;
			 }
			 return true;
		}
	//进行批量认证
	function save() {
		  if(!validateModule()) {
				return;
			} 
		window.top.showWaitingDlg("@COMMON.wait@", "@OnuAuth/OnuAuth.addAuthingTip@");
		$.ajax({
			url: '/gpon/onuauth/batchAddGponOnuAuth.tv',
	        type: 'POST',
	        data: {
	        	'entityIds' : '${entityIds}',
	        	'onuIndexs' : '${onuIndexs}',
	        	'lineProfileId' : lineComboBox.getValue(),
	        	'srvProfileId' : srvComboBox.getValue()
	        },
		   success: function(response) {
			   top.closeWaitingDlg();
			   top.afterSaveOrDelete({
                   title: '@COMMON.tip@',
                   html: '<b class="orangeTxt">@OnuAuth/OnuAuth.batchAddAuthCompleteTip@<br/>@OnuAuth/OnuAuth.AuthSuccessNumber@:' + response.successNum + ',@OnuAuth/OnuAuth.AuthFailNumber@:' + response.failNum +'</b>'
               });
			   if (window.parent.getFrame("onuAuthManage") != null) {
					window.parent.getFrame("onuAuthManage").refreshAutoFindGrid();
				}
			   cancelClick();
		   }, error: function() {
	            top.showMessageDlg("@COMMON.error@", "@OnuAuth/OnuAuth.addAuthEr@");
	            top.closeWaitingDlg();
	        }, 
	        cache: false
		})
	 }
	
</script>
	</head>
	<body class="openWinBody">
		<div class="openWinHeader">
			<div class="openWinTip">@OnuAuth/OnuAuth.batchAddGponOnuAuth@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT30 pL10 pR10">
			<table class="zebraTableRows">
				<tbody>
					<tr>
						<td class="rightBlueTxt  w160">@OnuAuth/OnuAuth.selectONUNumber@:</td>
						<td >${onuNumber}</td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt  w160">@OnuAuth/OnuAuth.authMode@:</td>
						<td><select disabled="disabled" id="authMode"class="w200  normalSel"><option value="1">@OnuAuth/OnuAuth.snAuth@</option></select></td>
					</tr>
					<tr>
						<td class="rightBlueTxt  w160">@COMMON.required@@OnuAuth/OnuAuth.lineProfile@:</td>
						<td ><div style="width:194px;">
						<input type="text" id="lineProfileId" readonly="readonly" class="normalInput w190"  maxlength="4"/>
					</div></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt  w160">@COMMON.required@@OnuAuth/OnuAuth.srvProfile@:</td>
						<td ><div style="width:194px;">
						<input type="text" id="srvProfileId" readonly="readonly" class="normalInput w190"  maxlength="4" />
					</div></td>
					</tr>
				</tbody>
			</table>
		</div> 
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="save()" icon="bmenu_new">@OnuAuth/OnuAuth.addAuth@</Zeta:Button>
			<Zeta:Button onClick="cancelClick();" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>