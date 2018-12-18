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
			url : '/epon/uniportvlan/loadTranslationRuleList.tv',
			fields : ["vlanIndex","translationNewVid"],
			baseParams : {
				entityId : entityId,
				uniIndex : uniIndex,
				uniId : uniId
			}
		});
		colModel = new Ext.grid.ColumnModel([
		 	{header : "@RULE.originalId@", align : 'center', dataIndex : 'vlanIndex'},
		 	{header : "@RULE.targetId@", align : 'center', dataIndex : 'translationNewVid'},
		 	{header : "@COMMON.manu@", width:160, fixed:true, align : 'center',dataIndex :'op',  renderer : opeartionRender}
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
	
	//关闭切换模式页面;
	function closeOpenLayer(){
		$("#openLayerOuter").stop().fadeOut();
	}	
	//显示切换模式页面,0为切换模式，1为添加规则;
	function showChangeMode(num){
		if(num === 1){
			//添加规则
			if(dataStore.getCount() < 8){
				$("#beforeVlan,#afterVlan").val("");
			}else{
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.totalLimit@");
				return;
			}
		}else{
			//切换模式
			$("#pvid2").val($("#pvid").val());
		}
		$("#openLayerOuter").find(".openLayerMainDiv").css({display: 'none'});
		$("#openLayerOuter").find(".openLayerMainDiv:eq("+ num +")").css({display: 'block'});
		$("#openLayerOuter").stop().fadeIn();
	}
	
	function opeartionRender(value, cellmate, record){
		var vlanIndex = record.data.vlanIndex;
		return String.format("<a href='javascript:;' onClick='deleteTranslateRule({0})'>@COMMON.delete@</a>",vlanIndex); 
	}
	
	//删除转换规则
	function deleteTranslateRule(vlanIndex){
		window.parent.showConfirmDlg("@COMMON.tip@", "@RULE.deleteRule@", function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/uniportvlan/deleteTranslationRule.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					uniIndex : uniIndex,
					uniId : uniId,
					vlanIndex : vlanIndex
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
	
	//添加转换规则
	function addTransRule(){
		var beforeVlan = $("#beforeVlan").val().trim();
		var afterVlan = $("#afterVlan").val().trim();
		if(!checkInput(beforeVlan,1,4094)){
			$("#beforeVlan").focus();
			return;
		}
		if(!checkInput(afterVlan,1,4094)){
			$("#afterVlan").focus();
			return;
		}
		//Vlan ID 不能在其它规则中出现
		var cVlanRepeat = false;
		var sVlanRepeat = false;
		dataStore.each(function(rec){
			if(beforeVlan == rec.data.vlanIndex){
				cVlanRepeat = true;
				return false;
			}
			if(afterVlan == rec.data.translationNewVid){
				sVlanRepeat = true;
				return false;
			}
		});
		//原VLAN　ID不能在其它规则中出现
		if(cVlanRepeat){
			window.top.showMessageDlg("@COMMON.tip@", "@RULE.originalVlanUsed@",null,function(){
				$("#beforeVlan").focus();
			});
			return;
		}
		//转换后VLAN ID不能在其它规则中出现
		if(sVlanRepeat){
			window.top.showMessageDlg("@COMMON.tip@", "@RULE.targetVlanUsed@",null,function(){
				$("#afterVlan").focus();
			});
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/uniportvlan/addTranslateRule.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				uniIndex : uniIndex,
				uniId : uniId,
				vlanIndex : beforeVlan,
				translationNewVid : afterVlan
			},
			dataType :　'json',
			success : function(result) {
				if(result.success){
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@RULE.saveSuccess@</b>'
	       			});
					closeOpenLayer();
					dataStore.reload();
				}else{
					window.top.showMessageDlg("@COMMON.tip@", "@RULE.saveFailed@");
				}
			},
			error : function() {
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.saveFailed@");
			},
			cache : false
		}); 
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
						<input type="text" id="pvid" class="normalInput w180" value="${uniPortVlan.vlanPVid}" maxlength="4" toolTip="@RULE.vlanInputTip@" />
	                 </td>
	                 <td>
	                 	<a id="savePivd" href="javascript:;" class="normalBtn"><span><i class="miniIcoSaveOK"></i>@BUTTON.apply@</span></a>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                <td class="rightBlueTxt" width="100">@PROFILE.vlanMode@:</td>
	             	<td width="70">
	             		@PROFILE.modeTranslate@
	             	</td>
	             	<td width="120">
	             		<a href="javascript:;" class="normalBtn" onclick="showChangeMode(0)"><span><i class="miniIcoTwoArrOut"></i>@UNIVLAN.changeMode@</span></a>
	             	</td>
	                <td class="rightBlueTxt" width="100">@UNIVLAN.profile@:</td>
	                 <td width="120">
	                 	<span class="longTxt wordBreak" id="vlanProfile"></span>
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
									<input type="text" id="pvid2" class="normalInput w180" value="" maxlength="4" toolTip="@RULE.vlanInputTip@" />
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
										<option value="2" selected="selected">@PROFILE.modeTranslate@</option>
										<option value="3">@PROFILE.modeAgg@</option>
										<option value="4">Trunk</option>
									</select>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="modifyVlanMode()"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeOpenLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerMainDiv">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@RULE.addRule@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
							<tr>
				                <td class="rightBlueTxt">
				                	@RULE.originalId@:
								</td>
								<td>
									<input type="text" class="normalInput w180" maxlength="4" toolTip="@RULE.vlanInputTip@"  id="beforeVlan"/>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt">
				                	@RULE.targetId@:
								</td>
								<td>
									<input type="text" class="normalInput w180" maxlength="4" toolTip="@RULE.vlanInputTip@" id="afterVlan"/>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="addTransRule()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeOpenLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerBg"></div>
	</div>
</body>
</Zeta:HTML>