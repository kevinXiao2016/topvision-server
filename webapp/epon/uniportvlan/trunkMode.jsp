<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module univlanprofile
    css css/white/disabledStyle
    import epon.uniportvlan.uniPortVlan
</Zeta:Loader>
<style type="text/css">
body,html{ height:100%;}
.putGrid{ width:100%; overflow:hidden;  margin-top:10px;}
.longTxt{ width:182px;}
.openLayerOuter{width: 100%; height: 100%;  overflow: hidden;   position: absolute;   top: 0;   left: 0; display:none;}
.openLayerBg{width: 100%; height: 100%; overflow: hidden; background: #F7F7F7; position: absolute; top: 0; left: 0; opacity: 0.8; filter: alpha(opacity=85);}
.openLayerMainDiv{ width: 560px; overflow: hidden; position: absolute;  top: 100px;  left: 120px;  z-index: 2;  background: #F7F7F7;}

</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var uniId = '${uniId}';
	var uniIndex = '${uniPortVlan.portIndex}';
	var vlanMode = '${uniPortVlan.vlanMode}';
	var uniPvid = '${uniPortVlan.vlanPVid}';
	var profileId = '${uniBindInfo.bindProfileId}';
	var bindProfAttr = '${uniBindInfo.bindProfAttr}';
	var profileName = '${uniVlanProfile.profileName}';

	var dataStore, colModel, grid;
	
	$(function(){
		if(profileId > 0){
			$("#vlanProfile").text(profileName);
		}else{
			$("#vlanProfile").text("@UNIVLAN.unRelated@");
		}
		
		var $pvid = $("#pvid");	
		var $savePvid = $("#savePivd");
		//点击pvid后面的保存按钮;
		$savePvid.click(function(){
			//先执行验证
			if(!checkInput($pvid.val(), 1, 4094)){
				$pvid.focus();
				return;
			}
			//修改PVID
			modifyUniPvid($pvid.val());
		});
		
		dataStore = new Ext.data.JsonStore({
			url : '/epon/uniportvlan/loadTrunkRuleList.tv',
			fields : ["trunkId"],
			baseParams : {
				entityId : entityId,
				uniIndex : uniIndex,
				uniId : uniId
			}
		});
		colModel = new Ext.grid.ColumnModel([
    	 	{header : "Trunk Id", align : 'center', dataIndex : 'trunkId'}, 
    	 	{header : "@COMMON.manu@", width:200, fixed:true, align : 'center',dataIndex :'op', renderer : opeartionRender}
    	]);
		grid =  new Ext.grid.GridPanel({
			title : "@RULE.ruleList@",
			height : 200,
			border : true,
			cls : 'normalTable',
			store : dataStore,
			colModel : colModel,
			viewConfig : {
				forceFit : true
			},
			renderTo : 'putGrid'
		});		
		dataStore.load();
		
	});//end document.ready;
	
	//关闭;
	function closeOpenLayer(){
		$("#openLayerOuter").stop().fadeOut();
	}	
	//切换模式,0为切换模式，1为添加规则;
	function showChangeMode(num){
		if(num === 0){
			//切换模式
			$("#pvid2").val($("#pvid").val());
		}else{
			//添加规则
			if(dataStore.getCount() < 8){
				$('#trunkId').val("");
			}else{
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.totalLimit@");
				return;
			}
		}
		$("#openLayerOuter").find(".openLayerMainDiv").css({display: 'none'});
		$("#openLayerOuter").find(".openLayerMainDiv:eq("+ num +")").css({display: 'block'});
		$("#openLayerOuter").stop().fadeIn();
	}
	
	//添加Trunk规则
	function addTrunkRule(){
		var trunkId = $("#trunkId").val().trim();
		if(!checkInput(trunkId,1,4094)){
			$("#trunkId").focus();
			return;
		}
		var trunkId = parseInt(trunkId, 10);
		var trunkData = getStoreData();
		if($.inArray(trunkId, trunkData) > -1){
			window.top.showMessageDlg("@COMMON.tip@", "@RULE.trunkIdUsed@",null,function(){
				$("#trunkId").focus();
			});
			return;
		}
		trunkData.push(trunkId);
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
		$.ajax({
			url : '/epon/uniportvlan/addTrunkRule.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				uniIndex : uniIndex,
				uniId : uniId,
				trunkList : trunkData.join(",")
			},
			dataType :　'json',
			success : function(result) {
				if(result.success){
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@PROFILE.addSuccess@</b>'
	       			});
					dataStore.reload();
					closeOpenLayer();
				}else{
					window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.addFailed@");
				}
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.addFailed@");
			},
			cache : false
		});
	}
	
	function opeartionRender(value, cellmate, record){
		var trunkId = record.data.trunkId;
		return String.format("<a href='javascript:;' onClick='deleteTrunkRule({0})'>@COMMON.delete@</a>",trunkId); 
	}
	
	//删除trunk规则
	function deleteTrunkRule(trunkId){
		window.parent.showConfirmDlg("@COMMON.tip@", "@RULE.deleteRule@", function(type) {
			if (type == 'no') {
				return;
			}
			var trunkData = getStoreData();
			trunkData.remove(trunkId);
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/uniportvlan/deleteTrunkRule.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					uniIndex : uniIndex,
					uniId : uniId,
					trunkList : trunkData.join(",")
				},
				dataType :　'json',
				success : function(result) {
					if(result.success){
						top.afterSaveOrDelete({
		       				title: "@COMMON.tip@",
		       				html: '<b class="orangeTxt">@PROFILE.deleteSuccess@</b>'
		       			});
						dataStore.reload();
					}else{
						window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.deleteFailed@");
					}
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.deleteFailed@");
				},
				cache : false
			});
		});
	}
	//获取Store中的数据
	function getStoreData(){
		var trunkData = [];
		dataStore.each(function(rec){
			trunkData.push(rec.data.trunkId);
		});
		return trunkData;
	}
	
</script>
</head>
<body class="openWinBody">
	<div class="edgeTB10LR20 pT20">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt">@epon/VLAN.portNum@:</td>
	                 <td colspan="2">
						${uniPortVlan.portString}
	                 </td>
	                 <td class="rightBlueTxt">PVID:</td>
	                 <td>
						<input type="text" id="pvid" class="normalInput w180" value="${uniPortVlan.vlanPVid}" maxlength="4" tooltip="@RULE.vlanInputTip@" />
	                 </td>
	                 <td>
	                 	<a id="savePivd" href="javascript:;" class="normalBtn"><span><i class="miniIcoSaveOK"></i>@BUTTON.apply@</span></a>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                <td class="rightBlueTxt" width="100">@PROFILE.vlanMode@:</td>
	             	<td width="70">
	             		Trunk
	             	</td>
	             	<td width="120">
	             		<a href="javascript:;" class="normalBtn" onclick="showChangeMode(0)"><span><i class="miniIcoTwoArrOut"></i>@UNIVLAN.changeMode@</span></a>
	             	</td>
	                <td class="rightBlueTxt" width="100">@UNIVLAN.profile@:</td>
	                 <td width="120">
	                 	<span class="longTxt wordBreak" id="vlanProfile"> </span>
	                 </td> 
	                 <td>
	                 	<a href="javascript:;" class="normalBtn" onclick="showVlanProfile()"><span><i class="miniIcoTwoArr"></i>@UNIVLAN.viewProfile@</span></a>
	                 </td>
	             </tr> 
	         </tbody>
	     </table>
	     <div id="putGrid" class="putGrid">
	     	
	     </div>
	     <a href="javascript:;" class="normalBtn mT10" onclick="showChangeMode(1)"><span><i class="miniIcoAdd"></i>@RULE.addRule@</span></a>
	     <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		          <li><a href="javascript:;" class="normalBtnBig" onclick="refreshUniVlanData()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		          <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	 </div>
	 
	 <div class="openLayerOuter" id="openLayerOuter">
		<div class="openLayerMainDiv">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@UNIVLAN.changeMode@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				             <tr>
				             	<td class="rightBlueTxt" width="160">
				                	@epon/VLAN.portNum@:
								</td>
								<td>
									<span>${uniPortVlan.portString}</span>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	PVID:
								</td>
								<td>
									<input type="text" id="pvid2" class="normalInput w180" value="" maxlength="4" tooltip="@RULE.vlanInputTip@" />
								</td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@PROFILE.vlanMode@:
								</td>
								<td>
									<select id="modeUpdate" class="normalSel w180">
										<option value="0">@PROFILE.modeTransparent@</option>
										<option value="1">@PROFILE.modeTag@</option>
										<option value="2">@PROFILE.modeTranslate@</option>
										<option value="3">@PROFILE.modeAgg@</option>
										<option value="4" selected="selected">Trunk</option>
									</select>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="modifyVlanMode()"><span><i class="miniIcoSaveOK"></i>@BUTTON.apply@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeOpenLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerMainDiv" style="top:140px">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@RULE.addRule@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
							<tr>
				                <td class="rightBlueTxt withoutBorderBottom" width="160">
				                	Trunk Id:
								</td>
								<td class="withoutBorderBottom">
									<input type="text" id="trunkId" class="normalInput w180" maxlength="4" toolTip="@RULE.vlanInputTip@" />
								</td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="addTrunkRule()"><span><i class="miniIcoSaveOK"></i>@BUTTON.add@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeOpenLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerBg"></div>
	</div>
</body>
</Zeta:HTML>