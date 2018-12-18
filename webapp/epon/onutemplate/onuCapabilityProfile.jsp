<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module businesstemplate
    import js.tools.ipText static
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
body,html{height:100%; overflow:hidden;}
.openLayerProfile{ width:100%; height:100%; overflow:hidden; position:absolute; top:0;left:0;  display:none;}
.openLayerProfileMain{ width:560px; height:320px; overflow:hidden; position:absolute; top:70px; left:120px; z-index:2;  background:#F7F7F7;}
.openLayerProfileBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	
	//关闭当前弹出框
 	function closeBtClick(){
 		window.parent.closeWindow('capabilityProfile');
 	}
	
	//从设备刷新数据
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/businessTemplate/refreshCapabilityProfile.tv',
			type : 'POST',
			data : {
				entityId : entityId
			},
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       			});
				dataStore.reload();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
			},
			cache : false
		});
	}
	
	//显示添加页面
	function showAddLayer(){
		if(dataStore.getCount() < 256){
			$(":text").val("0");
			$(":text").attr("tooltip", "@TEMPL.capaIdTip@");
			$('#addOpenLayer').fadeIn();
		}else{
			window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.capaTotal@");
			return;
		}
	}
	//关闭添加页面
	function closeAddLayer(){
		$('#addOpenLayer').fadeOut();
	}
	
	//输入校验 1-24之间的整数
	function checkInput(value){
		var reg = /^[0-9]\d*$/;	
		if (reg.exec(value) && parseInt(value) <= 24 && parseInt(value) >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//添加模板
	function addProfile(){
		//var capabilityId = $("#capabilityId").val();
		var gePortNum = $("#gePortNum").val();
		var fePortNum = $("#fePortNum").val();
		var potsPortNum = $("#potsPortNum").val();
		var e1PortNum = $("#e1PortNum").val();
		var wlanPortNum = $("#wlanPortNum").val();
		var catvPortNum = $("#catvPortNum").val();
		var uartPortNum = $("#uartPortNum").val();
		if(!checkInput(gePortNum)){
			$("#gePortNum").focus();
			return;
		}
		if(!checkInput(fePortNum)){
			$("#fePortNum").focus();
			return;
		}
		if(!checkInput(potsPortNum)){
			$("#potsPortNum").focus();
			return;
		}
		if(!checkInput(e1PortNum)){
			$("#e1PortNum").focus();
			return;
		}
		if(!checkInput(wlanPortNum)){
			$("#wlanPortNum").focus();
			return;
		}
		if(!checkInput(catvPortNum)){
			$("#catvPortNum").focus();
			return;
		}
		if(!checkInput(uartPortNum)){
			$("#uartPortNum").focus();
			return;
		}
		if(gePortNum == 0 && fePortNum == 0 && potsPortNum == 0 && e1PortNum == 0 && wlanPortNum == 0 && catvPortNum == 0 && uartPortNum == 0){
			window.top.showMessageDlg("@COMMON.tip@", "@TEMPL.portNumTip@");
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
		$.ajax({
			url : '/epon/businessTemplate/addCapabilityProfile.tv',
			type : 'POST',
			data : {
				"capability.entityId" : entityId,
				"capability.capabilityId" : 256,
				"capability.gePortNum" : gePortNum,
				"capability.fePortNum" : fePortNum,
				"capability.potsPortNum" : potsPortNum,
				"capability.e1PortNum" : e1PortNum,
				"capability.wlanPortNum" : wlanPortNum,
				"capability.catvPortNum" : catvPortNum,
				"capability.uartPortNum" : uartPortNum
			},
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@TEMPL.addSuccess@</b>'
       			});
				closeAddLayer();
				dataStore.reload();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.addCapaTip@");
			},
			cache : false
		});
	}
	
	
	//grid中操作栏处理
	function opeartionRender(value, cellmate, record){
		var profileId = record.data.capabilityId;
		if(operationDevicePower){
			if(profileId > 32){
				return String.format("<a href='javascript:;' onClick='deleteProfile({0})'>@COMMON.delete@</a>",profileId);
			}else{
				return "--";
			}
		}else{
			return "--";
		}
	}
	
	//删除模板
	function deleteProfile(profileId){
		window.parent.showConfirmDlg("@COMMON.tip@", "@TEMPL.delCapaConfirm@", function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/businessTemplate/deleteCapabilityProfile.tv',
				type : 'POST',
				data : {
					"capability.entityId" : entityId,
					"capability.capabilityId" : profileId
				},
				success : function() {
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@TEMPL.delCapaSuc@</b>'
	       			});
					dataStore.reload();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.delCapaFail@");
				},
				cache : false
			});
		});
	}
	
	$(document).ready(function(){
		window.dataStore = new Ext.data.JsonStore({
			url : '/epon/businessTemplate/loadCapabilityProfile.tv',
			fields : ["capabilityId", "gePortNum", "fePortNum","potsPortNum", "e1PortNum", "wlanPortNum", "catvPortNum", "uartPortNum"],
			baseParams : {
				entityId : entityId
			}
		});
		
		window.colModel = new Ext.grid.ColumnModel([ 
		 	{header : "@TEMPL.capabilityId@",width : 40,align : 'center',dataIndex : 'capabilityId'}, 
		 	{header : "@TEMPL.gePortNum@",width : 60,align : 'center',dataIndex : 'gePortNum'}, 
		 	{header : "@TEMPL.fePortNum@",width : 60,align : 'center',dataIndex : 'fePortNum'},
		 	{header : "@TEMPL.postPortNum@",width : 60,align : 'center',dataIndex : 'potsPortNum'}, 
		 	{header : "@TEMPL.e1PortNum@",width : 60, align : 'center',dataIndex :'e1PortNum'},
		 	{header : "@TEMPL.wlanPortNum@",width : 60, align : 'center',dataIndex :'wlanPortNum'},
		 	{header : "@TEMPL.catvPortNum@",width : 60, align : 'center',dataIndex :'catvPortNum'},
		 	{header : "@TEMPL.uartPortNum@",width : 60, align : 'center',dataIndex :'uartPortNum'},
		 	{header : "@COMMON.manu@",width : 60, align : 'center',dataIndex :'op', renderer : opeartionRender}
		]);

		window.uniVlanProfileGrid =  new Ext.grid.GridPanel({
			id : 'uniVlanProfileGrid',
			title : "@TEMPL.capabilitList@",
			height : 390,
			border : true,
			cls : 'normalTable',
			store : dataStore,
			colModel : colModel,
			viewConfig : {
				forceFit : true
			},
			renderTo : 'contentGrid',
			sm : new Ext.grid.RowSelectionModel({
				singleSelect : true
			})
		});		
		dataStore.load();
		
		if(!operationDevicePower){
			$("#addProFile").attr("disabled",true);
			$("#batchUnBind").attr("disabled",true);
			$("#addProfileBtn").attr("disabled",true);
			$("#modifyProfieBtn").attr("disabled",true);
		}
		
	    if(!refreshDevicePower){
	        $("#refreshData").attr("disabled",true);
	    }
	});
	
	//刷新Grid中的数据
	function refreshGridData(){
		dataStore.reload();
	};
	
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<div id="contentGrid"></div>
	</div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT5 noWidthCenter">
	         <li><a id="refreshData" onclick='refreshData()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
	         <li><a  onclick='showAddLayer()' id='addProFile' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@TEMPL.addProfile@</span></a></li>
	         <li><a  onclick='closeBtClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	
	<div class="openLayerProfile" id="addOpenLayer">
		<div class="openLayerProfileMain">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@TEMPL.addProfile@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
							<tr>
				                <td class="rightBlueTxt" width="80">
				                	@TEMPL.gePortNum@:
								</td>
								<td width="100">
									<input type="text" id="gePortNum" class="normalInput w120" maxlength="2"/>
								</td>
								  <td class="rightBlueTxt" width="80">
				                	@TEMPL.fePortNum@:
								</td>
								<td width="100">
									<input type="text" id="fePortNum" class="normalInput w120" maxlength="2"/>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="80">
				                	@TEMPL.postPortNum@:
								</td>
								<td width="100">
									<input type="text" id="potsPortNum" class="normalInput w120" maxlength="2"/>
								</td>
								<td class="rightBlueTxt" width="80">
				                	@TEMPL.e1PortNum@:
								</td>
								<td width="100">
									<input type="text" id="e1PortNum" class="normalInput w120" maxlength="2"/>
								</td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="80">
				                	@TEMPL.wlanPortNum@:
								</td>
								<td width="100">
									<input type="text" id="wlanPortNum" class="normalInput w120" maxlength="2"/>
								</td>
								 <td class="rightBlueTxt" width="80">
				                	@TEMPL.catvPortNum@:
								</td>
								<td width="100">
									<input type="text" id="catvPortNum" class="normalInput w120" maxlength="2"/>
								</td>
							</tr>
							<tr  class="darkZebraTr">
				                <td class="rightBlueTxt withoutBorderBottom" width="80">
				                	@TEMPL.uartPortNum@:
								</td>
								<td width="100" class="withoutBorderBottom">
									<input type="text" id="uartPortNum" class="normalInput w120" maxlength="2"/>
								</td>
								<td class="withoutBorderBottom"></td>
								<td class="withoutBorderBottom"></td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
					         <li><a href="javascript:;" id='addProfileBtn' class="normalBtnBig" onclick="addProfile()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeAddLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerProfileBg"></div>
	</div>
	
</body>
</Zeta:HTML>