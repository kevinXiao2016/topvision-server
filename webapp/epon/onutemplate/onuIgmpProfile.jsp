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
    plugin DropdownTree
</Zeta:Loader>
<style type="text/css">
body,html{height:100%; overflow:hidden;}
.openLayerProfile{ width:100%; height:100%; overflow:hidden; position:absolute; top:0;left:0;  display:none;}
.openLayerProfileMain{ width:560px; height:360px; overflow:hidden; position:absolute; top:40px; left:120px; z-index:2;  background:#F7F7F7;}
.openLayerProfileBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
.folderTree-footer, .ztree li span.button.ico_docu{ display:none;}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	
	//关闭当前弹出框
 	function closeBtClick(){
 		window.parent.closeWindow('igmpProfile');
 	}
	
	//从设备刷新数据
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/businessTemplate/refreshIgmpProfile.tv',
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
		//每次先清空再添加
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
	
	//判断是否存在可用的IGMP列表
	function checkAvaiableIgmp(){
		var avaiableIgmp = false;
		$.ajax({
			url : '/epon/businessTemplate/loadIgmpVlanList.tv',
		    type:'POST',
		    dateType:'json',
		    data : {
				entityId : entityId
			},
			async : false,
		    success:function(json) {
		    	if(json && json.length > 0 ){
		    		avaiableIgmp =  true;
		    	}
		    },
		    error:function(json) {},
		    cache:false
		});
		return avaiableIgmp;
	}
	
	//创建时加载IGMP VLAN选择树
	var igmpTreeLoaded = false;
	function loadIgmpVlanList(){
		igmpTreeLoaded = true;
		window.$igmpVlanTree = $('#igmpVlanList').dropdowntree({
			width:220,
			height:100,
			multi:true,
			url:'/epon/businessTemplate/loadIgmpVlanList.tv?entityId=' + entityId
		}).data('nm3k.dropdowntree');
	}
	
	//加载VLAN转化表序号列表
	function loadTransIdsSelect(selectId){
		//构造设备类型下拉框
		var idSelect = Zeta$(selectId);
		//每次先清空再添加
		$(idSelect).empty();
		$.ajax({
			url : '/epon/businessTemplate/getIgmpVlanTransIds.tv',
		    type:'POST',
		    dateType:'json',
		    data : {
				entityId : entityId
			},
			async : false,
		    success:function(json) {
		    	var transIds = json; 
		  	  	for(var i = 0; i < transIds.length; i++){
		  	  		var option = document.createElement('option');
		           	option.value = transIds[i].id;
		           	option.text = transIds[i].id;
		           	try {
		           		idSelect.add(option, null);
		           	} catch(ex) {
		           		idSelect.add(option);
		           	}
		  	  	}
		    },
		    error:function(json) {},
		    cache:false
		});
	}
	
	//显示添加页面
	function showAddLayer(){
		loadOnuProfileSelect("igmpProfileId");
		//如果没有可用的ONU业务模板，则提醒用户创建ONU业务模板
		if(!avaiableFlag){
			window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.noSrvProfile@");
			return;
		}
		$(":text").val("");
		
		/* if(!checkAvaiableIgmp()){
			window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.igmpVlanTip@");
			return;
		} */
		//加载IGMP VLAN选择树,确保只在最开始的时候加载一次
		/* if(!igmpTreeLoaded){
			loadIgmpVlanList();
		}else{
			window.$igmpVlanTree.disCheckAllNodes();
		} */
		$("#igmpVlanMode").val(0);
		$("#transIdArea").hide();
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
	
	//输入校验 0-64之间的整数
	function checkGroupInput(value){
		var reg = /^[0-9]\d*$/;	
		if (reg.exec(value) && parseInt(value) <= 64 && parseInt(value) >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//添加模板
	function addProfile(){
		var igmpProfileId = $("#igmpProfileId").val();
		var igmpPortId = $("#igmpPortId").val();
		var igmpMaxGroup = $("#igmpMaxGroup").val().trim();
		var igmpVlanMode = $("#igmpVlanMode").val();
		var igmpTransId = 10;
		
		if(!checkPortInput(igmpPortId)){
			$("#igmpPortId").focus();
			return;
		}
		//允许不配置IGMP最大组播数理,这时默认为-1(与设备保持一致)
		if(igmpMaxGroup == null || igmpMaxGroup == ''){
			igmpMaxGroup = -1;
		}else if(!checkGroupInput(igmpMaxGroup)){
			$("#igmpMaxGroup").focus();
			return;
		}
		var vlanInput = $("#igmpVlanList").val();
		
		var vlanArray = [];
		//VLAN序列可以不用配置(或者取消VLAN序列配置)
		if(vlanInput != ''){
			if(!checkVlanInput(vlanInput)){
				$("#igmpVlanList").focus();
				return;
			}
			vlanArray = changeToArray(vlanInput);
			if(vlanArray.length > 32){
				$("#igmpVlanList").focus();
				return;
			}
		}
		/**
		V1.8.0.3版本对转换序列单独提供了MIB表进行支持，不需要下发转换序列ID
		/* //在VLAN MODE为translation的情况下需要选择VLAN转化表序号
		if(igmpVlanMode == 2){
			/* if(igmpTransId == null || igmpTransId == ""){
				window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.transIdTip@");
				return;
			} 
			igmpTransId = 10;
		}else{
			//默认下发值,确保不为0
			igmpTransId = 10;
		} */
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
		$.ajax({
			url : '/epon/businessTemplate/addIgmpProfile.tv',
			type : 'POST',
			data : {
				"igmpProfile.entityId" : entityId,
				"igmpProfile.igmpProfileId" : igmpProfileId,
				"igmpProfile.igmpPortId" : igmpPortId,
				"igmpProfile.igmpMaxGroup" : igmpMaxGroup,
				"igmpProfile.igmpVlanMode" : igmpVlanMode,
				"igmpProfile.igmpTransId" : igmpTransId,
				"igmpProfile.igmpVlanArray" : vlanArray
			},
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@TEMPL.addSuccess@</b>'
       			});
				closeAddLayer();
				dataStore.reload({
			  		callback : function() {
			  			var opIndex = dataStore.findBy(function(record, id) {   
						    return record.get('igmpProfileId') == igmpProfileId && record.get('igmpPortId') == igmpPortId;   
						});
			  			igmpProfileGrid.getSelectionModel().selectRow(opIndex);
			        }
				});
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.addFailed@");
			},
			cache : false
		});
	}
	
	//模板Vlan模式转换
	function modeRenderer(value, cellmate, record){
		switch(value){
			case 0 : return "--"; break;
			case 1 : return "strip"; break;
			case 2 : return "translation"; break;
			case 3 : return "keep"; break;
		}
	}
	
	//VLAN转化表序号展示处理
	function transIdRender(value, cellmate, record){
		var vlanMode = record.data.igmpVlanMode;
		if(vlanMode == 2){
			return value;
		}else{
			return "--";
		}
	}

	//vlan list展示处理
	function vlanListRenderer(value, cellmate, record){
		var vlanStr = [];
		var vlanList = value;
		$.each(vlanList, function (index, vlan) {
			if(vlan != 0){
				vlanStr.push(value[index]);
			}
        });  
		if(vlanStr.length == 0){
			record.data.vlanListStr = "";
			return "--";
		}else{
			record.data.vlanListStr = vlanStr.join(",");
			return vlanStr.join(",");
		}
	}
	
	//grid中操作栏处理
	function opeartionRender(value, cellmate, record){
		var profileId = record.data.igmpProfileId;
		var igmpVlanMode = record.data.igmpVlanMode;
		var portId = record.data.igmpPortId;
		if(operationDevicePower){
			//业务模板绑定能力集或者绑定后不能进行端口VLAN模板的配置
			if(record.data.srvBindCap > 0){
				return "--";
			}else{
				if(igmpVlanMode == 2){
					return String.format("<a href='javascript:;' onClick='showUpdateProfile()'>@COMMON.modify@</a> / <a href='javascript:;' onClick='showOnuIgmpVlanTrans({0},{1})'>@igmpconfig/onuIgmp.vlanTransConfig@</a> / <a href='javascript:;' onClick='deleteProfile({0},{1})'>@COMMON.delete@</a>",profileId, portId);
				}else{
					return String.format("<a href='javascript:;' onClick='showUpdateProfile()'>@COMMON.modify@</a> / <a href='javascript:;' onClick='deleteProfile({0},{1})'>@COMMON.delete@</a>",profileId, portId);
				}
			}
		}else{
			return '--';
		}
	}
	
	//在修改时加载IGMP VLAN选择树
	var updateVlanLoaded = false;
	function loadUpdateVlanList(){
		updateVlanLoaded = true;
		window.$updateVlanTree = $('#updateVlanList').dropdowntree({
			width:220,
			height:100,
			multi:true,
			url:'/epon/businessTemplate/loadIgmpVlanList.tv?entityId=' + entityId
		}).data('nm3k.dropdowntree');
	}
	
	function showOnuIgmpVlanTrans(profileId,portId){
		window.top.createDialog('onuIgmpVlanTrans', '@igmpconfig/onuIgmp.vlanTransConfig@' , 800, 500, '/epon/businessTemplate/showOnuIgmpVlanTrans.tv?entityId=' + entityId + '&profileId=' + profileId + '&portId=' + portId, null, true, true);
	}
	
	//显示对模板的修改
	function showUpdateProfile(){
		var select = igmpProfileGrid.getSelectionModel().getSelected();
		$("#updateProfileId").text(select.data.igmpProfileId);
		$("#updatePortId").text(select.data.igmpPortId);
		var vlanStr = select.data.vlanListStr;
		var vlanList = [];
		if(vlanStr != null && vlanStr != ""){
			vlanList = 	vlanStr.split(",");		
		}
		//确保在第一次进入修改时加载树,之后进入时只用处理选中情况
		/* if(!updateVlanLoaded){
			loadUpdateVlanList();
			window.$updateVlanTree.checkNodes(vlanList);
		}else{
			window.$updateVlanTree.disCheckAllNodes();
			window.$updateVlanTree.checkNodes(vlanList);
		} */
		var maxGroup = select.data.igmpMaxGroup;
		//设备回读为-1的时间认为没有配置
		if(maxGroup != -1){
			$("#updateMaxGroup").val(select.data.igmpMaxGroup);
		}else{
			$("#updateMaxGroup").val("");
		}
		var selectMode = select.data.igmpVlanMode;
		$("#updateVlanMode").val(selectMode);
		$("#updateVlanList").val(vlanStr);
		//在VLAN MODE为translation时需要处理VLAN转化表序号
		if(selectMode == 2){
			//$("#updateIdArea").show()
			//loadTransIdsSelect("updateTransId");
			//$("#updateTransId").val(select.data.igmpTransId)
		}else{
			$("#updateIdArea").hide();
		}
		$('#updateOpenLayer').fadeIn();
	}
	
	//关闭修改页面
	function closeUpdateLayer(){
		$('#updateOpenLayer').fadeOut();
	}
	
	//修改模板
	function modifyProfie(){
		var igmpProfileId = $("#updateProfileId").text();
		var igmpPortId = $("#updatePortId").text();
		var igmpMaxGroup = $("#updateMaxGroup").val().trim();
		var igmpVlanMode = $("#updateVlanMode").val();
		var igmpTransId = 10;
		
		var vlanInput = $("#updateVlanList").val();
		var vlanArray = [];
		//VLAN序列可以不用配置(或者取消VLAN序列配置)
		if(vlanInput != ''){
			if(!checkVlanInput(vlanInput)){
				$("#updateVlanList").focus();
				return;
			}
			vlanArray = changeToArray(vlanInput);
			if(vlanArray.length > 32){
				$("#updateVlanList").focus();
				return;
			}
		}
		
		//允许不配置IGMP最大组播数理,这时默认为-1(与设备保持一致)
		if(igmpMaxGroup == null || igmpMaxGroup == ''){
			igmpMaxGroup = -1;
		}else if(!checkGroupInput(igmpMaxGroup)){
			$("#updateMaxGroup").focus();
			return;
		}
		
		/*V1.8.0.3版本对转换序列单独提供了MIB表进行支持，不需要下发转换序列ID
		//在VLAN MODE为translation的情况下需要选择VLAN转化表序号
		if(igmpVlanMode == 2){
			/* if(igmpTransId == null || igmpTransId == ""){
				window.parent.showMessageDlg("@COMMON.tip@", "@TEMPL.transIdTip@");
				return;
			} 
		}else{
			//默认下发值,确保不为0
			igmpTransId = 10;
		}*/
		
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/businessTemplate/modifyIgmpProfile.tv',
			type : 'POST',
			data : {
				"igmpProfile.entityId" : entityId,
				"igmpProfile.igmpProfileId" : igmpProfileId,
				"igmpProfile.igmpPortId" : igmpPortId,
				"igmpProfile.igmpMaxGroup" : igmpMaxGroup,
				"igmpProfile.igmpVlanMode" : igmpVlanMode,
				"igmpProfile.igmpTransId" : igmpTransId,
				"igmpProfile.igmpVlanArray" : vlanArray
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
						    return record.get('igmpProfileId') == igmpProfileId && record.get('igmpPortId') == igmpPortId;   
						});
			  			igmpProfileGrid.getSelectionModel().selectRow(opIndex);
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
				url : '/epon/businessTemplate/deleteIgmpProfile.tv',
				type : 'POST',
				data : {
					"igmpProfile.entityId" : entityId,
					"igmpProfile.igmpProfileId" : profileId,
					"igmpProfile.igmpPortId" : portId
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
	//处理IGMP最大组播数为-1的情况 
	function maxGroupRenderer(value, cellmate, record){
		if(value == -1){
			return "--";
		}else{
			return value;
		}
	}
	
	$(document).ready(function(){
		//为VLAN MODE绑定change事件,在选中translation的情况下处理VLAN转化表序号
		$("#igmpVlanMode, #updateVlanMode").bind("change", function(){
			var $select = $(this);
			var selectId = $select.attr("id");
			if($select.val() == 2){
				if(selectId == 'igmpVlanMode'){
					//loadTransIdsSelect("igmpTransId");
					//$("#transIdArea").show();
				}else if(selectId == 'updateVlanMode'){
					//loadTransIdsSelect("updateTransId");
					//$("#updateIdArea").show();
				}
			}else{
				if(selectId == 'igmpVlanMode'){
					$("#transIdArea").hide();
				}else if(selectId == 'updateVlanMode'){
					$("#updateIdArea").hide();
				}
			}
		})
		
		window.dataStore = new Ext.data.JsonStore({
			url : '/epon/businessTemplate/loadIgmpProfile.tv',
			fields : ["igmpProfileId", "igmpPortId", "igmpMaxGroup","igmpVlanMode", "igmpTransId", "igmpVlanList", "igmpVlanArray", "vlanListStr","srvBindCap"],
			baseParams : {
				entityId : entityId
			}
		});
		
		window.colModel = new Ext.grid.ColumnModel([ 
		 	{header : "@TEMPL.srvProfileId@",width : 80,align : 'center',dataIndex : 'igmpProfileId'}, 
		 	{header : "@TEMPL.portId@",width : 50,align : 'center',dataIndex : 'igmpPortId'}, 
		 	{header : "@TEMPL.maxGroup@",width : 60,align : 'center',dataIndex : 'igmpMaxGroup', renderer : maxGroupRenderer},
		 	{header : "@TEMPL.vlanMode@",width : 80,align : 'center',dataIndex : 'igmpVlanMode', renderer : modeRenderer}, 
		 	//{header : "@TEMPL.igmptransId@",width : 100, align : 'center',dataIndex :'igmpTransId', renderer : transIdRender},
		 	{header : "@TEMPL.igmpVlanList@",width : 140, align : 'center',dataIndex :'igmpVlanArray', renderer : vlanListRenderer},
		 	{header : "@COMMON.manu@",width : 120, align : 'center',dataIndex :'op', renderer : opeartionRender}
		]);
		
		window.igmpProfileGrid =  new Ext.grid.GridPanel({
			id : 'igmpProfileGrid',
			title : "@TEMPL.igmpProfileList@",
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
	
	//检查UNI VLAN序列输入
	function checkVlanInput(v){
		var reg = /^([0-9]{1,4}[,-]{0,1})+$/;
	    if (reg.exec(v)) {
	    	var tmp = v.replace(new RegExp('-', 'g'), ',');
	    	var tmpA = tmp.split(',');
	    	var tmpAl = tmpA.length;
			for(var i=0; i<tmpAl; i++){
				if(parseInt(tmpA[i]) > 4094 || parseInt(tmpA[i]) < 1){
					return false;
				}
			}
	    	return true;
	    }
	    return false;
	}
	//解析逗号和连字符组成的字符串为数组
	function changeToArray(v){
		var re = new Array();
		var t = v.split(",");
		var tl = t.length;
		for(var i=0; i<tl; i++){
			var tt = t[i];
			var ttI = tt.indexOf("-");
			if(ttI > 0){
				var ttt = tt.split("-");
				if(ttt.length == 2){
					var low = parseInt(parseInt(ttt[0]) > parseInt(ttt[1]) ? ttt[1] : ttt[0]);
					var tttl = Math.abs(parseInt(ttt[0]) - parseInt(ttt[1]));
					for(var u=0; u<tttl + 1; u++){
						re.push(low + u);
					}
				}
			}else if(ttI == -1){
				re.push(parseInt(tt));
			}
		}
		var rel = re.length;
		if(rel > 1){
			var o = {};
			for(var k=0; k<rel; k++){
				o[re[k]] = true;
			}
			re = new Array();
			for(var x in o){
				if (x > 0 && o.hasOwnProperty(x)) {
					re.push(parseInt(x)); 
				} 
			}
			re.sort(function(a, b){
				return a - b;
			});
		}
		return re;
	}
	
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
								<td>
									<select id="igmpProfileId" class="normalSel w220">
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
									<input type="text" id="igmpPortId" class="normalInput w220" maxlength="2" toolTip="@TEMPL.portIdTip@"/>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.igmpVlanList@:
								</td>
								<td>
									<input type="text" id="igmpVlanList" class="normalInput w220" maxlength="64" toolTip="@igmpconfig/igmp.tip.tip29@"/> 
									<!-- <div id="igmpVlanList"></div> -->
								</td>
								<td></td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.maxGroup@:
								</td>
								<td>
									<input type="text" id="igmpMaxGroup" class="normalInput w220" maxlength="2" toolTip="@TEMPL.maxGroupTip@"/>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.vlanMode@:
								</td>
								<td>
									<select id="igmpVlanMode" class="normalSel w220">
										<option value="0" selected>@TEMPL.unset@</option>
										<option value="1">strip</option>
										<option value="2">translation</option>
										<option value="3">keep</option>
									</select>
								</td>
								<td></td>
							</tr>
							<tr class="darkZebraTr" id="transIdArea">
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.igmptransId@:
								</td>
								<td>
									<!-- <input type="text" id="igmpTransId" class="normalInput w220" maxlength="2" toolTip="1-64"/> -->
									<!-- <div id="igmpTransId"></div> -->
									<select id="igmpTransId" class="normalSel w220">
									</select>
								</td>
								<td></td>
							</tr>
							
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB20 pT20 noWidthCenter">
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
								<td>
									<span id="updateProfileId"></span>
								</td>
								<td>
								</td>
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
				                	@TEMPL.igmpVlanList@:
								</td>
								<td>
									<input type="text" id="updateVlanList" class="normalInput w220" maxlength="64" toolTip="@igmpconfig/igmp.tip.tip29@"/>
									<!-- <div id="updateVlanList"></div>-->								
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.maxGroup@:
								</td>
								<td>
									<input type="text" id="updateMaxGroup" class="normalInput w220" maxlength="2" toolTip="@TEMPL.maxGroupTip@"/>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.vlanMode@:
								</td>
								<td>
									<select id="updateVlanMode" class="normalSel w220">
										<option value="0" selected>@TEMPL.unset@</option>
										<option value="1">strip</option>
										<option value="2">translation</option>
										<option value="3">keep</option>
									</select>
								</td>
								<td></td>
							</tr>
							<!-- <tr class="darkZebraTr" id="updateIdArea">
				                <td class="rightBlueTxt" width="140">
				                	@TEMPL.igmptransId@:
								</td>
								<td>
									<input type="text" id="updateTransId" class="normalInput w220" maxlength="2" toolTip="1-64"/>
									<div id="updateTransId"></div>
									<select id="updateTransId" class="normalSel w220">
									</select>
								</td>
								<td></td>
							</tr> -->
				
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