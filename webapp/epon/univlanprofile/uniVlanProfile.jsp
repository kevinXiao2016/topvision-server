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
    module univlanprofile
    import js.tools.ipText static
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
body,html{height:100%; overflow:hidden;}
.openLayerProfile{ width:100%; height:100%; overflow:hidden; position:absolute; top:0;left:0;  display:none;}
.openLayerProfileMain{ width:560px; height:280px; overflow:hidden; position:absolute; top:100px; left:120px; z-index:2;  background:#F7F7F7;}
.openLayerProfileBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	//模板索引
	var indexArray = [];
	
	//关闭当前弹出框
 	function closeBtClick(){
 		window.parent.closeWindow('uniVlanProfile');
 	}
	//从设备刷新数据
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/univlanprofile/refreshProfileData.tv',
			type : 'POST',
			data : {
				entityId : entityId
			},
			success : function() {
				//window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
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
		if(dataStore.getCount() < 4094){
			$("#profileIdInput").val("");
			$("#profileName").val("");
			//$("#profileModeSelect").val(0)
			$('#addOpenLayer').fadeIn();
		}else{
			window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.totalLimit@");
			return;
		}
	}
	//关闭添加页面
	function closeAddLayer(){
		$('#addOpenLayer').fadeOut();
	}
	//用户输入时检查模板Id是否可用
	function blurFn(){
		var profileId = $("#profileIdInput").val().trim();
		if(!checkInput(profileId)){
			$("#errorTip").text("");
			$("#profileIdInput").focus();
			return;
		}
		if($.inArray(parseInt(profileId), indexArray) == -1){
			$("#errorTip").text("");
			//参照命令行配置模式,给模板一个默认的名称
			$("#profileName").val("vlan-profile-" + profileId);
		}else{
			$("#errorTip").text("@PROFILE.idUsed@");
			$("#profileIdInput").focus();
		}
	}
	
	//输入校验 1-4094之间的整数
	function checkInput(value){
		var reg = /^[1-9]\d*$/;	
		if (reg.exec(value) && parseInt(value) <= 4094 && parseInt(value) >= 1) {
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
		var index = $("#profileIdInput").val();
		var name = $("#profileName").val().trim();
		var mode = $("#profileModeSelect").val();
		if(!checkInput(index)){  //输入校验 1-4094之间的整数
			$("#profileIdInput").focus();
			return;
		}
		if($.inArray(parseInt(index), indexArray) > -1){
			$("#profileIdInput").focus();
			return;
		}
		if(!checkProfileName(name)){
			$("#profileName").focus();
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
		$.ajax({
			url : '/epon/univlanprofile/addUniVlanProfile.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				profileIndex : index,
				profileName : name,
				profileMode : mode
			},
			success : function() {
				//window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.addSuccess@");
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@PROFILE.addSuccess@</b>'
       			});
				closeAddLayer();
				dataStore.reload({
			  		callback : function() {
			  			var opIndex = dataStore.findBy(function(record, id) {   
						    return record.get('profileId') == index;   
						});
			  			uniVlanProfileGrid.getSelectionModel().selectRow(opIndex);
			        }
				});
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.addFailed@");
			},
			cache : false
		});
	}
	//模板Vlan模式转换
	function modeRenderer(value, cellmate, record){
		switch(value){
			case 0 : return "@PROFILE.modeNone@"; break;
			case 1 : return "@PROFILE.modeTransparent@"; break;
			case 2 : return "@PROFILE.modeTag@"; break;
			case 3 : return "@PROFILE.modeTranslate@"; break;
			case 4 : return "@PROFILE.modeAgg@"; break;
			case 5 : return "@PROFILE.modeTrunk@"; break;
		}
	}
	//grid中操作栏处理
	function opeartionRender(value, cellmate, record){
		var index = record.data.profileId;
		var mode = record.data.profileMode;
		var bindCount = record.data.profileRefCnt;
		if(operationDevicePower){
			if(bindCount > 0){
				if(mode > 2){
					return String.format("<a href='javascript:;' onClick='showProfileRule({0},{1},{2})'>@PROFILE.ruleConfig@</a> / <a href='javascript:;' onClick='showBindProfile({0})'>@PROFILE.binding@</a> / <a href='javascript:;' onClick='unBindProfile({0})'>@PROFLIE.unBinding@</a>",index,mode,bindCount);
				}
				return String.format("<a href='javascript:;' onClick='showBindProfile({0})'>@PROFILE.binding@</a> / <a href='javascript:;' onClick='unBindProfile({0})'>@PROFLIE.unBinding@</a>",index);
			}else{
				if(mode == 0){
					return String.format("<a href='javascript:;' onClick='showUpdateProfile()'>@COMMON.modify@</a> / <a href='javascript:;' onClick='deleteProfile({0})'>@COMMON.delete@</a>",index);
				}else if(mode > 2){
					return String.format("<a href='javascript:;' onClick='showUpdateProfile()'>@COMMON.modify@</a> / <a href='javascript:;' onClick='showProfileRule({0},{1},{2})'>@PROFILE.ruleConfig@</a> / <a href='javascript:;' onClick='showBindProfile({0})'>@PROFILE.binding@</a> / <a href='javascript:;' onClick='deleteProfile({0})'>@COMMON.delete@</a>",index,mode,bindCount);
				}else{
					return String.format("<a href='javascript:;' onClick='showUpdateProfile()'>@COMMON.modify@</a> / <a href='javascript:;' onClick='showBindProfile({0})'>@PROFILE.binding@</a> / <a href='javascript:;' onClick='deleteProfile({0})'>@COMMON.delete@</a>",index);
				}
			}
		}else{
			return '--';
		}
	}
	var nowMode;   //表示当前的模板模式
	var modeChange = false;  //模板模式改变标记
	//显示对模板的修改
	function showUpdateProfile(){
		var select = uniVlanProfileGrid.getSelectionModel().getSelected();
		$("#profileIdUpdate").text(select.data.profileId);
		$("#nameUpdate").val(select.data.profileName);
		nowMode = select.data.profileMode;
		$("#modeUpdate").val(nowMode)
		$('#updateOpenLayer').fadeIn();
	}
	//关闭修改页面
	function closeUpdateLayer(){
		$('#updateOpenLayer').fadeOut();
	}
	
	//修改模板
	function modifyProfie(){
		var index = $("#profileIdUpdate").text();
		var name = $("#nameUpdate").val().trim();
		var updateMode = $("#modeUpdate").val();
		if(nowMode != updateMode){
			modeChange = true;
		}
		if(!checkProfileName(name)){
			$("#nameUpdate").focus();
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/univlanprofile/modifyProfile.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				profileIndex : index,
				profileName : name,
				profileMode : updateMode,
				modeChange : modeChange
			},
			success : function() {
				//window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.updateSuccess@");
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@PROFILE.updateSuccess@</b>'
       			});
				closeUpdateLayer();
				dataStore.reload({
			  		callback : function() {
			  			var opIndex = dataStore.findBy(function(record, id) {   
						    return record.get('profileId') == index;   
						});
			  			uniVlanProfileGrid.getSelectionModel().selectRow(opIndex);
			        }
				});
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.updateFailed@");
			},
			cache : false
		}); 
	}
	
	//显示模板规则配置页面
	function showProfileRule(index,mode,refCount){
		window.top.createDialog('profileRule', "@PROFILE.profileRule@", 800, 500, "/epon/univlanprofile/showProfileRule.tv?entityId="+entityId+"&profileIndex="+index+"&profileMode="+mode+"&profileRefCnt="+refCount, null, true, true);
	}
	
	//显示绑定模板页面
	function showBindProfile(index){
		var select = uniVlanProfileGrid.getSelectionModel().getSelected();
		var $mode = select.data.profileMode;
		window.top.createDialog('profileBind', "@PROFILE.profileBind@", 800, 550, "/epon/univlanprofile/showProfileBind.tv?entityId="+entityId+"&profileIndex="+index+"&profileMode="+$mode, null, true, true);
	}
	
	//解除该模板的绑定
	function unBindProfile(index){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/univlanprofile/unBindProfile.tv',
			type : 'post',
			dataType: 'json',
			data : {
				entityId : entityId,
				profileIndex : index
			},
			success : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.unBindSuccess@: "+ json.successCount +" @COMMON.fail@: "+ json.failedCount);
				dataStore.reload();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.unBindFailed@");
			},
			cache : false
		}); 
	}
	
	//删除模板
	function deleteProfile(index){
		window.parent.showConfirmDlg("@COMMON.tip@", "@PROFILE.deleteConfirm@", function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/univlanprofile/deleteProfile.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					profileIndex :　index
				},
				success : function() {
					//window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.deleteSuccess@");
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@PROFILE.deleteSuccess@</b>'
	       			});
					dataStore.reload();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.deleteFailed@");
				},
				cache : false
			});
		});
	}
	//对端口进行批量解绑定
	function showBatchUnBind(){
		window.top.createDialog('batchUnBind', "@PROFILE.batchUnBind@", 800, 500, "/epon/univlanprofile/showBatchUnBind.tv?entityId="+entityId, null, true, true);
	}
	
	$(document).ready(function(){
		window.dataStore = new Ext.data.JsonStore({
			url : '/epon/univlanprofile/loadUniVlanProfileList.tv',
			fields : ["profileId", "profileName", "profileRefCnt","profileMode"],
			baseParams : {
				entityId : entityId
			},
			listeners : {
				load : function(s,records, options) {
					indexArray = [];
					$.each(records,function(i,record){
						indexArray.push( record.data.profileId );
					});
		        } 
			}
		});
		
		window.colModel = new Ext.grid.ColumnModel([ 
		 	{header : "@PROFILE.id@",width : 50,align : 'center',dataIndex : 'profileId'}, 
		 	{header : "@PROFILE.name@",width : 160,align : 'center',dataIndex : 'profileName'}, 
		 	{header : "@PROFILE.bindCount@",width : 50,align : 'center',dataIndex : 'profileRefCnt'},
		 	{header : "@PROFILE.vlanMode@",width : 60,align : 'center',dataIndex : 'profileMode', renderer : modeRenderer}, 
		 	{header : "@COMMON.manu@",width : 130, align : 'center',dataIndex :'op', renderer : opeartionRender}
		]);
		
		window.uniVlanProfileGrid =  new Ext.grid.GridPanel({
			id : 'uniVlanProfileGrid',
			title : "@PROFILE.profileList@",
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
	         <li><a  onclick='showAddLayer()' id='addProFile' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@PROFILE.addProfile@</span></a></li>
	         <li><a  onclick='showBatchUnBind()' id='batchUnBind' href="javascript:;" class="normalBtnBig"><span><i class="bmenu_miniIcoBroken"></i>@PROFILE.batchUnBind@</span></a></li>
	         <li><a  onclick='closeBtClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	
	<div class="openLayerProfile" id="addOpenLayer">
		<div class="openLayerProfileMain">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@PROFILE.addProfile@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				             <tr>
				             	<td class="rightBlueTxt" width="140">
				                	@PROFILE.id@:
								</td>
								<td width="186">
									<input type="text" id="profileIdInput" class="normalSel w220" toolTip="@PROFILE.idInputTip@" onblur="blurFn()"/>
								</td>
								<td>
									<b id="errorTip" class="orangeTxt"></b>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@PROFILE.name@:
								</td>
								<td>
									<input type="text" id="profileName" class="normalInput w220" maxlength="31" toolTip="@PROFILE.inputName@"/>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@PROFILE.vlanMode@:
								</td>
								<td>
									<select id="profileModeSelect" class="normalSel w220">
										<!-- <option value="0" selected>@PROFILE.modeNone@</option> -->
										<option value="1">@PROFILE.modeTransparent@</option>
										<option value="2">@PROFILE.modeTag@</option>
										<option value="3">@PROFILE.modeTranslate@</option>
										<option value="4">@PROFILE.modeAgg@</option>
										<option value="5">@PROFILE.modeTrunk@</option>
									</select>
								</td>
								<td></td>
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
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@PROFILE.updateProfile@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				             <tr>
				             	<td class="rightBlueTxt" width="140">
				                	@PROFILE.id@:
								</td>
								<td>
									<span id="profileIdUpdate"></span>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@PROFILE.name@:
								</td>
								<td>
									<input type="text" id="nameUpdate" class="normalInput w220" maxlength="31" toolTip="@PROFILE.inputName@"/>
								</td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@PROFILE.vlanMode@:
								</td>
								<td>
									<select id="modeUpdate" class="normalSel w220">
										<!-- <option value="0" selected>@PROFILE.modeNone@</option> -->
										<option value="1">@PROFILE.modeTransparent@</option>
										<option value="2">@PROFILE.modeTag@</option>
										<option value="3">@PROFILE.modeTranslate@</option>
										<option value="4">@PROFILE.modeAgg@</option>
										<option value="5">@PROFILE.modeTrunk@</option>
									</select>
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