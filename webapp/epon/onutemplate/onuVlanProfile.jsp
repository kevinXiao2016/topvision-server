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
.openLayerProfileMain{ width:560px; height:290px; overflow:hidden; position:absolute; top:80px; left:120px; z-index:2;  background:#F7F7F7;}
.openLayerProfileBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	var supportNode = top.VersionControl.supportNode('pvidPri', window[vcEntityKey]);
	var specialVersion = !supportNode.disabled && !supportNode.hidden;
	
	function StringToBoolean(str){
		switch(str){
			case 'false':
				return false;
				break;
			case 'true':
				return true;
				break;
			default :
				return false;
				break;
		}
	}
	
	//关闭当前弹出框
 	function closeBtClick(){
 		window.parent.closeWindow('onuVlanProfile');
 	}
	
	//从设备刷新数据
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/businessTemplate/refreshVlanProfile.tv',
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
	
	//记录是否有可用的ONU业务模板
	var avaiableFlag = false;
	//加载onu业务模板列表
	function loadOnuProfileSelect(selectId){
		//构造设备类型下拉框
		var profileSelect = Zeta$(selectId);
		$(profileSelect).empty();
		$.ajax({
			url : '/epon/businessTemplate/loadOnuProfile.tv',
		    type:'POST',
		    dateType:'json',
		    data : {
				entityId : entityId
			},
			async : false,
		    success:function(json) {
		    	var onuProfiles = json; 
		  	  	for(var i = 0; i < onuProfiles.length; i++){
		  	  		var $onuProfile = onuProfiles[i];
		  	  		//已经绑定过的ONU业务模板不能进行任何操作
		  	  		if($onuProfile.srvBindCap == 0 && $onuProfile.srvBindCnt == 0){
		  	  		    avaiableFlag = true;
			  	  		var option = document.createElement('option');
			           	option.value = $onuProfile.srvProfileId;
			           	option.text = $onuProfile.srvProfileId;
			           	try {
			        	   	profileSelect.add(option, null);
			           	} catch(ex) {
			        	    profileSelect.add(option);
			           	}
		  	  		}
		  	  	}
		    },
		    error:function(json) {},
		    cache:false
		});
	}
	
	//加载UNI VLAN模板
	function loadVlanProfileSelect(selectId){
		//构造设备类型下拉框
		var profileSelect = Zeta$(selectId);
		$(profileSelect).empty();
		var unBindStr = '<option value="0" selected>' + '@TEMPL.unBind@' + '</option>';
		$(profileSelect).append(unBindStr);
		$.ajax({
			url : '/epon/univlanprofile/loadUniVlanProfileList.tv',
		    type:'POST',
		    dateType:'json',
		    data : {
				entityId : entityId
			},
			async : false,
		    success:function(json) {
		  	  var vlanProfiles = json; 
		  	  for(var i = 0; i < vlanProfiles.length; i++){
		  		  var profile = vlanProfiles[i];
		  		  if(profile != null && profile.profileMode > 0){
		  			   var option = document.createElement('option');
			           option.value = vlanProfiles[i].profileId;
			           option.text = vlanProfiles[i].profileName;
			           try {
			        	   profileSelect.add(option, null);
			           } catch(ex) {
			        	   profileSelect.add(option);
			           }
		  		  }
		  	  }
		    },
		    error:function(json) {},
		    cache:false
		});
	}
	
	//显示添加页面
	function showAddLayer(){
		$(":text").val("");
		loadOnuProfileSelect("vlanProfileId");
		if(!avaiableFlag){
			window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.noSrvProfile@");
			return;
		}
		loadVlanProfileSelect("bindVlanProfile");
		var $addSpecialVersion = $("#addSpecialVersion");
		if(specialVersion){ //如果存在 PVID PRI 优先级;
			$addSpecialVersion.removeClass('displayNone').css({display:''});
		}else{
			$addSpecialVersion.addClass('displayNone').css({display:'none'});
		}
		$('#addOpenLayer').fadeIn();
	}
	//关闭添加页面
	function closeAddLayer(){
		$('#addOpenLayer').fadeOut();
	}
	
	//输入校验 1-24之间的整数
	function checkPortInput(value){
		var reg = /^[1-9]\d*$/;	
		if (reg.exec(value) && parseInt(value) <= 24 && parseInt(value) >= 1) {
			return true;
		} else {
			return false;
		}
	}
	
	//输入校验 0-4094之间的整数
	function checkPvidInput(value){
		var reg = /^[0-9]\d*$/;	
		if (reg.exec(value) && parseInt(value) <= 4094 && parseInt(value) >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//添加模板
	function addProfile(){
		var vlanProfileId = $("#vlanProfileId").val();
		var srvPortId = $("#srvPortId").val();
		var bindVlanProfile = $("#bindVlanProfile").val();
		var profileVlanPvid = $("#profileVlanPvid").val();
		var priorityValue = $("#addPrioritySel").val();
		
		if(!checkPortInput(srvPortId)){
			$("#srvPortId").focus();
			return;
		}
		if(!checkPvidInput(profileVlanPvid)){
			$("#profileVlanPvid").focus();
			return;
		}
		
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
		var dataObj = {
			"vlanProfile.entityId" : entityId,
			"vlanProfile.vlanProfileId" : vlanProfileId,
			"vlanProfile.srvPortId" : srvPortId,
			"vlanProfile.bindVlanProfile" : bindVlanProfile,
			"vlanProfile.profileVlanPvid" : profileVlanPvid
		}
		if(specialVersion){ //如果存在PVID PRI 优先级;
			dataObj["vlanProfile.profileVlanPvidPri"] = priorityValue;
		}
		$.ajax({
			url : '/epon/businessTemplate/addVlanProfile.tv',
			type : 'POST',
			data : dataObj,
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@TEMPL.addSuccess@</b>'
       			});
				closeAddLayer();
				dataStore.reload({
			  		callback : function() {
			  			var opIndex = dataStore.findBy(function(record, id) {   
						    return record.get('vlanProfileId') == vlanProfileId && record.get('srvPortId') == srvPortId;   
						});
			  			vlanProfileGrid.getSelectionModel().selectRow(opIndex);
			        }
				});
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.addFailed@");
			},
			cache : false
		});
	}
	
	//grid中操作栏处理
	function opeartionRender(value, cellmate, record){
		var profileId = record.data.vlanProfileId;
		var portId = record.data.srvPortId;
		if(operationDevicePower){
			//业务模板绑定能力集或者绑定后不能进行端口VLAN模板的配置
			if(record.data.srvBindCap > 0){
				return "--";				
			}else{
				return String.format("<a href='javascript:;' onClick='showUpdateProfile()'>@COMMON.modify@</a> / <a href='javascript:;' onClick='deleteProfile({0},{1})'>@COMMON.delete@</a>",profileId,portId);
			}
		}else{
			return '--';
		}
	}
	
	//显示对模板的修改
	function showUpdateProfile(){
		loadVlanProfileSelect("updateBindVlan");
		var select = vlanProfileGrid.getSelectionModel().getSelected();
		$("#updateProfileId").text(select.data.vlanProfileId);
		$("#updatePortId").text(select.data.srvPortId);
		var vlanProfile = select.data.bindVlanProfile;
		$("#updateBindVlan").val(vlanProfile);
		if(vlanProfile != 0){
			$("#vlanProfileTip").show();
		}else{
			$("#vlanProfileTip").hide();
		}
		$("#updateVlanPvid").val(select.data.profileVlanPvid)
		var $updateSpecialVersion = $("#updateSpecialVersion");
		if(specialVersion){ //如果存在 PVID PRI 优先级;
			$updateSpecialVersion.removeClass('displayNone').css({display:''});
		}else{
			$updateSpecialVersion.addClass('displayNone').css({display:'none'});
		}
		if(specialVersion){
			$("#updatePrioritySel").val(select.data.profileVlanPvidPri);
		}
		$('#updateOpenLayer').fadeIn();
	}
	
	//关闭修改页面
	function closeUpdateLayer(){
		$('#updateOpenLayer').fadeOut();
	}
	
	//修改模板
	function modifyProfie(){
		var updateProfileId = $("#updateProfileId").text();
		var updatePortId = $("#updatePortId").text();
		var updateBindVlan = $("#updateBindVlan").val();
		var updateVlanPvid = $("#updateVlanPvid").val();
		var priorityValue = $("#updatePrioritySel").val();
		
		if(!checkPvidInput(updateVlanPvid)){
			$("#updateVlanPvid").focus();
			return;
		}
		
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		var dataObj = {
			"vlanProfile.entityId" : entityId,
			"vlanProfile.vlanProfileId" : updateProfileId,
			"vlanProfile.srvPortId" : updatePortId,
			"vlanProfile.bindVlanProfile" : updateBindVlan,
			"vlanProfile.profileVlanPvid" : updateVlanPvid	
		};
		if(specialVersion){ //如果存在PVID PRI 优先级;
			dataObj["vlanProfile.profileVlanPvidPri"] = priorityValue;
		}
		$.ajax({
			url : '/epon/businessTemplate/modifyVlanProfile.tv',
			type : 'POST',
			data : dataObj,
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@TEMPL.updateSuc@</b>'
       			});
				closeUpdateLayer();
				dataStore.reload({
			  		callback : function() {
			  			var opIndex = dataStore.findBy(function(record, id) {   
						    return record.get('vlanProfileId') == updateProfileId && record.get('srvPortId') == updatePortId;   
						});
			  			vlanProfileGrid.getSelectionModel().selectRow(opIndex);
			        }
				});
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.updateFailed@");
			},
			cache : false
		}); 
	}
	
	//删除模板
	function deleteProfile(profileId, portId){
		window.parent.showConfirmDlg("@COMMON.tip@", "@TEMPL.deleteConfirm@", function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/businessTemplate/deleteVlanProfile.tv',
				type : 'POST',
				data : {
					"vlanProfile.entityId" : entityId,
					"vlanProfile.vlanProfileId" : profileId,
					"vlanProfile.srvPortId" : portId
				},
				success : function() {
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@TEMPL.deleteSuc@</b>'
	       			});
					dataStore.reload();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.srvProfileFail@");
				},
				cache : false
			});
		});
	}
	
	$(document).ready(function(){
		window.dataStore = new Ext.data.JsonStore({
			url : '/epon/businessTemplate/loadVlanProfile.tv',
			fields : ["vlanProfileId", "srvPortId", "bindVlanProfile","profileVlanPvid","profileVlanPvidPri", "srvBindCap", "srvBindCnt"],
			baseParams : {
				entityId : entityId
			}
		});
		
		var cmArr = [ 
 		 	{header : "@TEMPL.srvProfileId@",width : 70,align : 'center',dataIndex : 'vlanProfileId'}, 
		 	{header : "@TEMPL.portId@",width : 70,align : 'center',dataIndex : 'srvPortId'}, 
		 	{header : "@TEMPL.uniVlanProfile@",width : 70,align : 'center',dataIndex : 'bindVlanProfile'},
		 	{header : "VLAN PVID",width : 80,align : 'center',dataIndex : 'profileVlanPvid'}
		];
		if(specialVersion){
			cmArr.push({header : "PVID PRI",width : 80, align : 'center',dataIndex : 'profileVlanPvidPri'})
		}
		cmArr.push({header : "@COMMON.manu@",width : 100, align : 'center',dataIndex :'op', renderer : opeartionRender})
		window.colModel = new Ext.grid.ColumnModel(cmArr);
		
		window.vlanProfileGrid =  new Ext.grid.GridPanel({
			id : 'vlanProfileGrid',
			title : "@TEMPL.portVlanList@",
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
				             	<td class="rightBlueTxt" width="140">
				                	@TEMPL.srvProfileId@:
								</td>
								<td width="222">
									<select id="vlanProfileId" class="normalSel w220">
									</select>
								</td>
								<td>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.portId@:
								</td>
								<td>
									<input type="text" id="srvPortId" class="normalInput w220" maxlength="2" toolTip="@TEMPL.portIdTip@"/>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.uniVlanProfile@:
								</td>
								<td>
									<select id="bindVlanProfile" class="normalSel w220">
									</select>
								</td>
								<td></td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	VLAN PVID:
								</td>
								<td>
									<input type="text" id="profileVlanPvid" class="normalInput w220" maxlength="4" toolTip="@TEMPL.pvidTip@"/>
								</td>
								<td></td>
							</tr>
							<tr class="withoutBorderBottom displayNone" id="addSpecialVersion">
							    <td class="rightBlueTxt">PVID PRI</td>
							    <td>
							        <select class="normalSel w220" id="addPrioritySel">
							            <option value="0">0 (@resources/MAIN.searchDefault@)</option>
							            <option value="1">1</option>
							            <option value="2">2</option>
							            <option value="3">3</option>
							            <option value="4">4</option>
							            <option value="5">5</option>
							            <option value="6">6</option>
							            <option value="7">7</option>
							        </select>
							    </td>
							    <td><b class="orangeTxt">@TEMPL.pvidPriTip@</b></td>
							</tr>						
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
					         <li><a href="javascript:;" id='addProfileBtn' class="normalBtnBig" onclick="addProfile()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeAddLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerProfileBg"></div>
	</div>
	
	<div class="openLayerProfile" id="updateOpenLayer">
		<div class="openLayerProfileMain">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@TEMPL.updateProfile@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				        <tbody>
				            <tr>
				             	<td class="rightBlueTxt" width="140">
				                	@TEMPL.srvProfileId@:
								</td>
								<td width="202">
									<span id="updateProfileId"></span>
								</td>
								<td></td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.portId@:
								</td>
								<td>
									<span id="updatePortId"></span>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.uniVlanProfile@:
								</td>
								<td>
									<select id="updateBindVlan" class="normalSel w200">
									</select>
								</td>
								<td>
									<b id="vlanProfileTip" class="orangeTxt">@TEMPL.vlanProfileTip@</b>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	VLAN PVID:
								</td>
								<td>
									<input type="text" id="updateVlanPvid" class="normalInput w200" maxlength="4" toolTip="@TEMPL.pvidTip@"/>
								</td>
								<td>
								</td>
							</tr>
							<tr class="withoutBorderBottom displayNone" id="updateSpecialVersion">
							    <td class="rightBlueTxt">PVID PRI:</td>
							    <td>
							        <select class="normalSel w200" id="updatePrioritySel">
							        	<option value="0">0 (@resources/MAIN.searchDefault@)</option>
							            <option value="1">1</option>
							            <option value="2">2</option>
							            <option value="3">3</option>
							            <option value="4">4</option>
							            <option value="5">5</option>
							            <option value="6">6</option>
							            <option value="7">7</option>
							        </select>
							    </td>
							    <td><b class="orangeTxt">@TEMPL.pvidPriTip@</b></td>
							</tr>						
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
					         <li><a href="javascript:;" id='modifyProfieBtn' class="normalBtnBig" onclick="modifyProfie()"><span><i class="miniIcoEdit"></i>@COMMON.modify@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeUpdateLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerProfileBg"></div>
	</div>
</body>
</Zeta:HTML>