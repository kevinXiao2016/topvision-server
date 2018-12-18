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
.openLayerProfileMain{ width:560px; height:300px; overflow:hidden; position:absolute; top:65px; left:120px; z-index:2;  background:#F7F7F7;}
.openLayerProfileBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	var entityMenu = null;
	
	//记录已经用过的模板ID
	var profileIdArray = [];
	//记录已经用过的能力集ID
	var capabilityArray = [];
	
	//关闭当前弹出框
 	function closeBtClick(){
 		window.parent.closeWindow('onuProfile');
 	}
	
	//从设备刷新数据
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/businessTemplate/refreshOnuProfile.tv',
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
	
	//加载能力集
	function loadCapabilitySelect(selectId){
		//构造设备类型下拉框
		var capabilitySelect = Zeta$(selectId);
		$(capabilitySelect).empty();
		var unBindStr = '<option value="0" selected>' + '@TEMPL.unBind@' + '</option>';
		$(capabilitySelect).append(unBindStr);
		$.ajax({
			url : '/epon/businessTemplate/loadCapabilityProfile.tv',
		    type:'POST',
		    dateType:'json',
		    data : {
				entityId : entityId
			},
		    success:function(json) {
		  	  	var capabilityProfiles = json; 
		  	  	for(var i = 0; i < capabilityProfiles.length; i++){
		  		  	var $capability = capabilityProfiles[i];
		  		  	//能力集与模板是一对一的关系,不能重复添加
		  		  	if($.inArray($capability.capabilityId, capabilityArray) == -1){
			  			var option = document.createElement('option');
						option.value = $capability.capabilityId;
						option.text = $capability.capabilityId;
						try {
						 	capabilitySelect.add(option, null);
						} catch(ex) {
						 	capabilitySelect.add(option);
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
		if(dataStore.getCount() < 256){
			$(":text").val("");
			$("#errorTip").text("");
			$("#srvImgpMode").val(0);
			$("#srvIgmpFastLeave").val(0);
			loadCapabilitySelect("srvBindCap");
			$('#addOpenLayer').fadeIn();
		}else{
			window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.totalLimit@");
			return;
		}
	}
	
	//关闭添加页面
	function closeAddLayer(){
		$('#addOpenLayer').fadeOut();
	}
	
	//用户输入时检查模板Id是否可用
	function blurFn(){
		var profileId = $("#srvProfileId").val().trim();
		if(!checkInput(profileId)){
			$("#errorTip").text("");
			$("#srvProfileId").focus();
			return;
		}
		if($.inArray(parseInt(profileId), profileIdArray) > -1){
			$("#errorTip").text("@TEMPL.idUsed@");
			$("#srvProfileId").focus();
		}else{
			$("#errorTip").text("");
			//参照命令行配置模式,给模板一个默认的名称
			$("#srvProfileName").val("onu-srvprofile-" + profileId);
		}
	}
	
	//输入校验 1-256之间的整数
	function checkInput(value){
		var reg = /^[1-9]\d*$/;	
		if (reg.exec(value) && parseInt(value) <= 256 && parseInt(value) >= 1) {
			return true;
		} else {
			return false;
		}
	}
	
	//验证输入名称,支持1-31个字节
	function checkProfileName(name){
		reg = /^[\w-]{1,31}$/;
		if(reg.test(name)){
			return true;
		}else{
			return false;
		}
	}
	
	//添加模板
	function addProfile(){
		var srvProfileId = $("#srvProfileId").val();
		var srvProfileName = $("#srvProfileName").val();
		var srvImgpMode = $("#srvImgpMode").val();
		var srvIgmpFastLeave = $("#srvIgmpFastLeave").val();
		var srvBindCap = $("#srvBindCap").val();
		
		if(!checkInput(srvProfileId)){  //输入校验 1-256之间的整数
			$("#srvProfileId").focus();
			return;
		}
		if($.inArray(parseInt(srvProfileId), profileIdArray) > -1){
			$("#srvProfileId").focus();
			return;
		}
		if(!checkProfileName(srvProfileName)){
			$("#srvProfileName").focus();
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
		$.ajax({
			url : '/epon/businessTemplate/addOnuProfle.tv',
			type : 'POST',
			data : {
				"srvProfile.entityId" : entityId,
				"srvProfile.srvProfileId" : srvProfileId,
				"srvProfile.srvProfileName" : srvProfileName,
				"srvProfile.srvImgpMode" : srvImgpMode,
				"srvProfile.srvIgmpFastLeave" : srvIgmpFastLeave,
				"srvProfile.srvBindCap" : srvBindCap
			},
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@TEMPL.updateSuc@</b>'
       			});
				closeAddLayer();
				dataStore.reload({
			  		callback : function() {
			  			var opIndex = dataStore.findBy(function(record, id) {   
						    return record.get('srvProfileId') == srvProfileId;   
						});
			  			onuProfileGrid.getSelectionModel().selectRow(opIndex);
			        }
				});
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.addFailed@");
			},
			cache : false
		});
	}
	//组播模式
	function modeRenderer(value, cellmate, record){
		switch(value){
			case 0 : return "--"; break;
			case 1 : return "ctc"; break;
			case 2 : return "snooping"; break;
			case 3 : return "disable"; break;
		}
	}
	
	//快速离开使能
	function fastLeaveRenderer(value, cellmate, record){
		switch(value){
			case 0 : return "--"; break;
			case 1 : return "enable"; break;
			case 2 : return "disable"; break;
		}
	}
	
	//grid中操作栏处理
	function opeartionRender(value, cellmate, record){
		var profileId = record.data.srvProfileId;
		var bindCap = record.data.srvBindCap;
		if(operationDevicePower){
			if(bindCap > 0){
				return String.format("<a href='javascript:;' onClick='unBindCapability({0})'>@TEMPL.unBindCap@</a> / <a href='javascript:;' class='withSub' style='margin-right:20px;'  onClick = 'showMoreOperation(event)'>@COMMON.other@</a>", profileId);
			}else{
				return String.format("<a href='javascript:;' onClick='showUpdateProfile()'>@COMMON.modify@</a> / <a href='javascript:;' onClick='deleteProfile({0})'>@COMMON.delete@</a> / <a href='javascript:;' class='withSub' style='margin-right:20px;'  onClick = 'showMoreOperation(event)'>@COMMON.other@</a>", profileId);
			}
		}else{
			return '--';
		}
	}
	
	function showMoreOperation(event){
		event.getXY = function(){return [event.clientX,event.clientY + 10];};
	    showProfileMenu(event);
	}
	
	function showProfileMenu(event){
		var record = onuProfileGrid.getSelectionModel().getSelected();
		var bindCap = record.data.srvBindCap;
	    //---菜单-----//   
		var items = []
	    //已绑定能力集,只能查看配置
	    if(bindCap > 0){
	    	items[items.length] = {text: "@TEMPL.showVlanProfile@", handler: showVlanProfile,disabled:!operationDevicePower};
		    items[items.length] = {text: "@TEMPL.showIgmpProfile@", handler: showIgmpProfile, disabled:!operationDevicePower};
	    }else{
	    	items[items.length] = {text: "@TEMPL.configVlanProfile@", handler: showVlanProfile,disabled:!operationDevicePower};
		    items[items.length] = {text: "@TEMPL.configIgmpProfile@", handler: showIgmpProfile, disabled:!operationDevicePower};
	    }
		if (!entityMenu) {
	        entityMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
	    } else {
	    	//---存在bug--//
	    	entityMenu.removeAll();
	    	entityMenu.add(items)
	    }
		entityMenu.showAt(event.getXY())
	}
	
	function showVlanProfile(){
		var record = onuProfileGrid.getSelectionModel().getSelected();
		var profileId = record.data.srvProfileId;
		var srvBindCap = record.data.srvBindCap;
		window.top.createDialog('profileVlanConfig', "@ONU.vlanProfile@", 800, 500, "/epon/businessTemplate/showProfileVlanConfig.tv?entityId="+entityId+"&profileId="+profileId+"&srvBindCap="+srvBindCap, null, true, true);
	}
	
	function showIgmpProfile(){
		var record = onuProfileGrid.getSelectionModel().getSelected();
		var profileId = record.data.srvProfileId;
		var srvBindCap = record.data.srvBindCap;
		window.top.createDialog('profileIgmpConfig', "@ONU.igmpProfile@", 800, 500, "/epon/businessTemplate/showProfileIgmpConfig.tv?entityId="+entityId+"&profileId="+profileId+"&srvBindCap="+srvBindCap, null, true, true);
	}
	
	//解绑定能力集
	function unBindCapability(profileId){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/businessTemplate/unBindCapability.tv',
			type : 'POST',
			data : {
				"srvProfile.entityId" : entityId,
				"srvProfile.srvProfileId" : profileId,
				"srvProfile.srvBindCap" : 0
			},
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@TEMPL.unbindSuc@</b>'
       			});
				dataStore.reload();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.unbindSuc@");
			},
			cache : false
		});
	}
	
	//显示对模板的修改
	function showUpdateProfile(){
		loadCapabilitySelect("updateBindCap");
		var select = onuProfileGrid.getSelectionModel().getSelected();
		$("#updateProfileId").text(select.data.srvProfileId);
		$("#updateProfileName").val(select.data.srvProfileName);
		$("#updateIgmpMode").val(select.data.srvImgpMode);
		$("#updateFastLeave").val(select.data.srvIgmpFastLeave);
		$("#updateBindCap").val(select.data.srvBindCap);
		$('#updateOpenLayer').fadeIn();
	}
	//关闭修改页面
	function closeUpdateLayer(){
		$('#updateOpenLayer').fadeOut();
	}
	
	//修改模板
	function modifyProfie(){
		var updateProfileId = $("#updateProfileId").text();
		var updateProfileName = $("#updateProfileName").val();
		var updateIgmpMode = $("#updateIgmpMode").val();
		var updateFastLeave = $("#updateFastLeave").val();
		var updateBindCap = $("#updateBindCap").val(); 
		if(!checkProfileName(updateProfileName)){
			$("#updateProfileName").focus();
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/businessTemplate/modifyOnuProfile.tv',
			type : 'POST',
			data : {
				"srvProfile.entityId" : entityId,
				"srvProfile.srvProfileId" : updateProfileId,
				"srvProfile.srvProfileName" : updateProfileName,
				"srvProfile.srvImgpMode" : updateIgmpMode,
				"srvProfile.srvIgmpFastLeave" : updateFastLeave,
				"srvProfile.srvBindCap" : updateBindCap
			},
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@TEMPL.updateSuc@</b>'
       			});
				closeUpdateLayer();
				dataStore.reload({
			  		callback : function() {
			  			var opIndex = dataStore.findBy(function(record, id) {   
						    return record.get('srvProfileId') == updateProfileId;   
						});
			  			onuProfileGrid.getSelectionModel().selectRow(opIndex);
			        }
				});
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.capabilityFail@");
			},
			cache : false
		}); 
	}
	
	//删除模板
	function deleteProfile(profileId){
		window.parent.showConfirmDlg("@COMMON.tip@", "@TEMPL.deleteConfirm@", function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/businessTemplate/deleteOnuProfile.tv',
				type : 'POST',
				data : {
					"srvProfile.entityId" : entityId,
					"srvProfile.srvProfileId" : profileId
				},
				success : function() {
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@TEMPL.deleteSuc@</b>'
       				});
					dataStore.reload();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.deleteFailed@");
				},
				cache : false
			});
		});
	}
	
	$(document).ready(function(){
		window.dataStore = new Ext.data.JsonStore({
			url : '/epon/businessTemplate/loadOnuProfile.tv',
			fields : ["srvProfileId", "srvProfileName", "srvImgpMode","srvIgmpFastLeave", "srvBindCap", "srvBindCnt"],
			baseParams : {
				entityId : entityId
			},
			listeners : {
				load : function(s,records, options) {
					profileIdArray = [];
					capabilityArray = [];
					$.each(records,function(i,record){
						profileIdArray.push(record.data.srvProfileId);
						if(record.data.srvBindCap >　0){
							capabilityArray.push(record.data.srvBindCap);
						}
					});
		        }
			}
		});
		
		window.colModel = new Ext.grid.ColumnModel([ 
		 	{header : "@TEMPL.profileId@",width : 40,align : 'center',dataIndex : 'srvProfileId'}, 
		 	{header : "<div style='text-align:center'>@TEMPL.profileDesc@</div>",width : 140,align : 'left',dataIndex : 'srvProfileName'}, 
		 	{header : "@TEMPL.igmpMode@",width : 60,align : 'center',dataIndex : 'srvImgpMode', renderer : modeRenderer},
		 	{header : "@TEMPL.fastLeave@",width : 80,align : 'center',dataIndex : 'srvIgmpFastLeave', renderer : fastLeaveRenderer}, 
		 	{header : "@TEMPL.capability@",width : 60,align : 'center',dataIndex : 'srvBindCap'}, 
		 	{header : "@TEMPL.bindCount@",width : 60,align : 'center',dataIndex : 'srvBindCnt'},
		 	{header : "@COMMON.manu@",width : 150, align : 'center',dataIndex :'op', renderer : opeartionRender}
		]);
		
		window.onuProfileGrid =  new Ext.grid.GridPanel({
			id : 'onuProfileGrid',
			title : "@TEMPL.srvProfileList@",
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
				                	@TEMPL.profileId@:
								</td>
								<td width="186">
									<input type="text" id="srvProfileId" class="normalSel w180" toolTip="@TEMPL.profileIdTip@" onblur="blurFn()"/>
								</td>
								<td>
									<b id="errorTip" class="orangeTxt"></b>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.profileDesc@:
								</td>
								<td>
									<input type="text" id="srvProfileName" class="normalInput w180" maxlength="31" toolTip="@TEMPL.descTip@"/>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.igmpMode@:
								</td>
								<td>
									<select id="srvImgpMode" class="normalSel w180">
										<option value="0" selected>@TEMPL.unset@</option>
										<option value="1">ctc</option>
										<option value="2">snooping</option>
										<option value="3">disable</option>
									</select>
								</td>
								<td></td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.fastLeave@:
								</td>
								<td>
									<select id="srvIgmpFastLeave" class="normalSel w180">
										<option value="0" selected>@TEMPL.unset@</option>
										<option value="1">enable</option>
										<option value="2">disable</option>
									</select>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.capability@:
								</td>
								<td>
									<select id="srvBindCap" class="normalSel w180">
									</select>
								</td>
								<td>
									<b class="orangeTxt">@TEMPL.capaTip@</b>
								</td>
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
	
	<div class="openLayerProfile" id="updateOpenLayer">
		<div class="openLayerProfileMain">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@TEMPL.updateProfile@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				             <tr>
				             	<td class="rightBlueTxt" width="140">
				                	@TEMPL.profileId@:
								</td>
								<td>
									<span id="updateProfileId"></span>
								</td>
								<td></td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.profileDesc@:
								</td>
								<td>
									<input type="text" id="updateProfileName" class="normalInput w180" maxlength="31" toolTip="@TEMPL.descTip@"/>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.igmpMode@:
								</td>
								<td>
									<select id="updateIgmpMode" class="normalSel w180">
										<option value="0" selected>@TEMPL.unset@</option>
										<option value="1">ctc</option>
										<option value="2">snooping</option>
										<option value="3">disable</option>
									</select>
								</td>
								<td></td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.fastLeave@:
								</td>
								<td>
									<select id="updateFastLeave" class="normalSel w180">
										<option value="0" selected>@TEMPL.unset@</option>
										<option value="1">enable</option>
										<option value="2">disable</option>
									</select>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.capability@:
								</td>
								<td>
									<select id="updateBindCap" class="normalSel w180">
									</select>
								</td>
								<td>
									<b class="orangeTxt">@TEMPL.capaTip@</b>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
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