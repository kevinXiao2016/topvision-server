var cmcIpInfo ;
var cmcIpInfoChanged ;
var saveFlagNum = 0;//保存时使用的标志位
var alloc;
var cmcLocation;
var cmcDescr;
var cmcName;
var cmcContact;
var ccmctSniJson;
var saveErrorList = new Array();
var saveFlagList = new Array();
var dhcpBundleTree;
function initJspData(){	        	
	$("#cmcLocation").val(cmcLocation);
	$("#cmcDescr").val(cmcDescr);
	$("#cmcName").val(cmcName);
	$("#cmcContact").val(cmcContact);
	if(cmcIpInfo != null){
		setIpValue("vlanIp", cmcIpInfo[0].priIpAddr);
		setIpValue("vlanIpMask", cmcIpInfo[0].priIpMask);
		setIpValue("vlanGw", cmcIpInfo[0].defaultRoute);
	}
	if(cmcIpInfo != null){
		$("#sniEthInt").val(ccmctSniJson[0].topCcmtsSniEthInt);
		$("#sniMainInt").val(ccmctSniJson[0].topCcmtsSniMainInt);
		$("#sniBackupInt").val(ccmctSniJson[0].topCcmtsSniBackupInt);
	}
}
function resetClick(){
	initJspData();
}
//
function checkIpOk(){
	var ipStatus=checkedIpValue(getIpValue("vlanIp"));
	var maskStatus = checkedIpMask(getIpValue("vlanIpMask"));
	if(ipStatus&&maskStatus){
		return true;
	}else if(!ipStatus){
		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.ipInputFormatError);
		return false;
	}else if(!maskStatus){
		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.maskInputFormatError);
		return false;
	}
}
//检验页面的输入正确与否，主调函数：saveClick
function checkInputOk(){
	var tmpList = [0,0,0,0,0,0,0];
	var reg = /^([0-9]{1,4})+$/g;
	var tmpId = $("#cmcVlanId").val();
	if($("#cmcName").val() == ""){
		tmpList[1] = 10;
	}
	if(tmpId == ""){
		tmpList[3] = 9;
	}else if(!reg.exec(tmpId) || parseInt(tmpId)<1 || parseInt(tmpId)>4094){
		tmpList[3] = 7;
	}
	if(!ipIsFilled("vlanIp") || !ipIsFilled("vlanGw")){
		tmpList[4] = 6;
	}else if(!ipIsFilled("vlanIpMask")){
		tmpList[4] = 8;
	}
	if(!ipIsFilled("cmcIp") || !ipIsFilled("cmcGw")){
		tmpList[5] = 6;
	}
	if(!ipIsFilled("cmcIpMask")){
		tmpList[6] = 8;
	}
	return tmpList;
}
//check base info 修改 false
function checkTheSameBase(){
	if($("#cmcLocation").val()!=cmcLocation || $("#cmcName").val()!=cmcName || $("#cmcContact").val()!=cmcContact){
		return false;
	}
	return true;
}
//check ip info
function checkTheSameIp(){
	if(getIpValue("vlanIp")!=cmcIpInfo[0].priIpAddr || getIpValue("vlanIpMask")!=cmcIpInfo[0].priIpMask
			|| getIpValue("vlanGw")!=cmcIpInfo[0].defaultRoute){
		return false;
	}
	return true;
}
function checkTheSameSnmpValues(){
	if(getIpValue("emsIpAddress")!=snmpValues.emsIpAddress||$.trim($("#readCommunity").val())!=snmpValues.readCommunity
			||$.trim($("#writeCommunity").val())!=snmpValues.writeCommunity){
		return false;
	}
	
	return true;
}
function checkTheSni(){
	if($("#sniEthInt").val()!=ccmctSniJson[0].topCcmtsSniEthInt || $("#sniMainInt").val()!=ccmctSniJson[0].topCcmtsSniMainInt
			|| $("#sniBackupInt").val()!=ccmctSniJson[0].topCcmtsSniBackupInt){
		return false;
	}
	return true;
}
function checkDhcpRelay(){
	if (($("#host").attr("checked") == true && snoopingHost==0)||$("#host").attr("checked") == false && snoopingHost==1){
		return false;
	}
	if (($("#stb").attr("checked") == true && snoopingStb==0)||$("#stb").attr("checked") == false && snoopingStb==1){
		return false;
	}
	if (($("#mta").attr("checked") == true && snoopingMta==0)||$("#mta").attr("checked") == false && snoopingMta==1){
		return false;
	}
	if($("#topCcmtsDhcpRelay").val()!=ccmtsDhcpRelay || $("#topCcmtsDhcpForwardBroadcast").val()!=ccmtsDhcpForwardBroadcast){
		return false;
	}
	return true;
}
//修改基本配置，主调函数：modify
function modifyBase(){
	var loc = $("#cmcLocation").val();
	var name = $("#cmcName").val();
	var contact = $("#cmcContact").val();
	Ext.Ajax.request({
		url : '/cmc/config/modifyCmcBasicInfo.tv?cmcId=' + cmcId + '&location=' + loc + '&cmcName=' + name + '&contact=' + contact,
		success : function(response) {
			if(response.responseText != 'success'){
				saveErrorList.unshift(1);
				modifyContinu();
				return;
			}
			modifyContinu();
		},
		failure : function() {
			saveErrorList.unshift(1);
			modifyContinu();
		}
	});
}
//修改IP配置，主调函数：modify
function modifyExt(){
	var list = [getIpValue("vlanIp"), getIpValue("vlanIpMask"), getIpValue("vlanGw")];
	var str = list.join("_");
	Ext.Ajax.request({
		url : '/cmc/config/modifyCmcIpInfo.tv?cmcId=' + cmcId + '&ipParamStr=' + str,
		success : function(response) {
			if(response.responseText != 'success'){
				saveErrorList.unshift(2);
				modifyContinu();
				return;
			}
			modifyContinu();
		},
		failure : function() {
			saveErrorList.unshift(2);
			modifyContinu();
		}
	});
}
function modifySnmpValues(){
	var _data={};
	_data.entityId=snmpValues.entityId;
	_data.emsIpAddress=getIpValue("emsIpAddress");
	_data.serverSnmpVersion=$("#snmpVersion").val();
	_data.readCommunity=$("#readCommunity").val();
	_data.writeCommunity=$("#writeCommunity").val();
	$.ajax({
		url : "/config/saveServerSnmp.tv", 
		cache : false, 
		method: 'post' ,
		data : _data,
		success : function(){
			modifyContinu();
		},
		error:function(e){
			saveErrorList.unshift(4);
			modifyContinu();
		}
	});
}
function modifyTheSni(){
	var list = [$("#sniEthInt").val(), $("#sniMainInt").val(), $("#sniBackupInt").val()];
	var sniParamStr = list.join("_");
	Ext.Ajax.request({
		url : '/cmc/config/modifyCc8800BSniConfig.tv?cmcId=' + cmcId + '&sniParamStr=' + sniParamStr,
		success : function(response) {
			//上联口修改电口到光口-‘如果光口没有连接’的话是不会返回信息的
			if(response.responseText != 'success'){
				saveErrorList.unshift(3);
				modifyContinu();
				return;
			}
			modifyContinu();
		},
		failure : function() {
			saveErrorList.unshift(3);
			modifyContinu();
		}
	});
}
//修改DhcpRelay，主调函数：modify
function modifyDhcpRelay(){
	var tempCcmtsDhcpRelay = $('#topCcmtsDhcpRelay').val();
	var tempCcmtsDhcpForwardBroadcast = $('#topCcmtsDhcpForwardBroadcast').val();
	var tempSnoopingHost;
	var tempSnoopingMta;
	var tempSnoopingStb;
	if($('#host').attr("checked")){
		tempSnoopingHost = 1;
	}else{
		tempSnoopingHost = 0;
	}
	if($('#mta').attr("checked")){
		tempSnoopingMta = 1;
	}else{
		tempSnoopingMta = 0;
	}
	if($('#stb').attr("checked")){
		tempSnoopingStb = 1;
	}else{
		tempSnoopingStb = 0;
	}
	var str = '&cmcDhcpBaseConfigInfo.topCcmtsDhcpRelay=' + tempCcmtsDhcpRelay + '&cmcDhcpBaseConfigInfo.topCcmtsDhcpForwardBroadcast=' + tempCcmtsDhcpForwardBroadcast + '&cmcDhcpBaseConfigInfo.host=' + tempSnoopingHost+ '&cmcDhcpBaseConfigInfo.mta=' + tempSnoopingMta + '&cmcDhcpBaseConfigInfo.stb=' + tempSnoopingStb;
	Ext.Ajax.request({
		url : '/cmc/dhcp/modifyCmcDhcpRelayBaseInfo.tv?cmcId=' + cmcId + str,
		success : function(response) {
			if(response.responseText != 'success'){
	        	window.parent.closeWaitingDlg();
		        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.saveConfigSuccess);
			}else{
	      		window.parent.closeWaitingDlg();
	            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.saveConfigFail);
			}
		},
		failure : function() {
      		window.parent.closeWaitingDlg();
            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.saveConfigFail);
		}
	});
}
//保存修改的配置，主调函数：saveClick
function modify(s){
	if(s == 1){
		modifyBase();
	}else if(s == 2){
		modifyExt();
	}else if(s == 3){
		modifyTheSni();
	}else if(s == 4){
		//modifyDhcpRelay();
		modifySnmpValues();
	}
}
/************
 * 后台交互**
 **********/
function saveClick() {
	if(!checkIpOk()){
		return;
	}
	//获取当前页面所有参数
	var temp1 = checkTheSameBase();
	var temp2 = checkTheSameIp();
	var temp3 = checkTheSni();
	var temp4=checkTheSameSnmpValues();
	saveFlagList = new Array();
	if(!temp4){
		saveFlagList.unshift(4);
	}
	if(!temp3){
		saveFlagList.unshift(3);
	}
	if(!temp2){
		saveFlagList.unshift(2);
	}
	if(!temp1){
		saveFlagList.unshift(1);
	}
	if(saveFlagList.length > 0 && saveFlagList.join("")!=""){
		modify(saveFlagList[saveFlagNum]);
	}else{
	
		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.noConfigToSave);
	}
}

function refreshClick(){
	Ext.Ajax.request({
		url : '/cmc/config/refreshCmcConfig.tv?cmcId=' + cmcId+'&productType=' + productType,
		success : function(response) {
			if(response.responseText != 'success'){
	        	
				return;
			}
			var responseJson = Ext.util.JSON.decode(response.responseText);
        	cmcIpInfoChanged = responseJson.cmcSystemIpInfo;
        	cmcAttribute = responseJson.cmcAttribute;
        	$("#cmcLocation").val(cmcAttribute.topCcmtsSysLocation);
        	$("#cmcDescr").val(cmcAttribute.topCcmtsSysDescr);
        	$("#cmcName").val(cmcAttribute.topCcmtsSysName);
        	$("#cmcContact").val(cmcAttribute.topCcmtsSysContact);
        	
        	if(cmcIpInfoChanged != null){
        		setIpValue("vlanIp", cmcIpInfoChanged[0].priIpAddr);
        		setIpValue("vlanIpMask", cmcIpInfoChanged[0].priIpMask);
        		setIpValue("vlanGw", cmcIpInfoChanged[0].defaultRoute);
        	}
		},
		failure : function() {
		}
	});
}
function onloadAlloc(alloc){
	if(alloc==1){
		$(":radio[value=1]").attr("checked",true);
	}else{
		$(":radio[value=2]").attr("checked",true);
	}
}
function checkDhcpAlloc(){
	if($(":radio").attr("checked")==true){
		$("#option60Id").css("display","inline");
	}else{
		$("#option60Id").css("display","none");
	}
}

Ext.onReady(function(){
	//IP输入框及其初始化
	var vlanIp = new ipV4Input("vlanIp","span1");
	vlanIp.width(141);
	var vlanGw = new ipV4Input("vlanGw","span2");
	vlanGw.width(141);
	var vlanIpMask = new ipV4Input("vlanIpMask","span3");
	vlanIpMask.width(141);
	
	var emsIpAddress=new ipV4Input("emsIpAddress","span9");
	emsIpAddress.width(180);
	emsIpAddress.setValue(snmpValues.emsIpAddress);
	

	document.getElementById("baseInfo-div").style.display='block';
	document.getElementById("dhcpConfigInfo-div").style.display='none';
	$("#dhcpBundlePolicy").hide();
	
	loadDhcpBundleGrid();
	loadDhcpHelperGrid();
	loadDhcpGiAddrGrid();
	loadDhcpOption60Grid();
	
	initJspData();
	onloadAlloc(alloc);
	checkDhcpAlloc();
	//取消默认排序
	Ext.grid.PropertyGrid.prototype.initComponent = function(){
		this.customEditors = this.customEditors || {};
		this.lastEditRow = null;
		var store = new Ext.grid.PropertyStore(this);
		this.propStore = store;
		var cm = new Ext.grid.PropertyColumnModel(this,store);
		//store.store.sort('name','ASC');//取消默认排序
		this.addEvents(
			'beforepropertychange',
			'propertychange'
		);
		this.cm = cm;
		this.ds = store.store;
		Ext.grid.PropertyGrid.superclass.initComponent.call(this);
		this.selModel.on('beforecellselect',function(sm,rowIndex,colIndex){
			if(colIndex === 0)return false;
		});
	}
	var baseColumns = [	
	           		{header: 'VLAN ID',align: 'center', dataIndex: 'vlanId'},	
	           		{header: I18N.CMC.title.vlanName, align: 'center', dataIndex: 'vlanName'},
	           		{header: I18N.CMC.title.primaryInterface, align: 'center', dataIndex: 'isPrimary'}
	           		];
   	baseStore = new Ext.data.JsonStore({
   	    url: ('xx?cmcId='),
   	    root: 'data',
   	    remoteSort: true,//是否支持后台排序 
   	    fields: ['vlanId','vlanName','isPrimary']
   	});
   	baseStore.setDefaultSort('channelId', 'ASC');
	var baseCm = new Ext.grid.ColumnModel(baseColumns);

	var vlanGrid = new Ext.grid.GridPanel({
		//renderTo: 'vlanList',
		//id:'vlanGridId',
		height:180,
		animCollapse: animCollapse, 
		trackMouseOver: trackMouseOver, 
		border: true, 
		store: baseStore, 
		cm: baseCm
	});
	
	var propertyStore = {
			"VLAN ID": "My Object",
	        "VLAN name":"",
	        "TaggedPort": "",
	        "UntaggedPort":""
	        //I18N.CMC.title.multicastControlMode: "A test object"
	}
	propertyStore[I18N.CMC.title.multicastControlMode] = "A test object";
	var propertyGrid = new Ext.grid.PropertyGrid({
	    //title: 'Properties Grid',
	    //renderTo: 'vlanInfo',
		height:180
	});
	propertyGrid.setSource(propertyStore);
});

var dhcpHelperGrid;
var dhcpHelperColumns;
var listHelperCm;
var listHelperStore;
var dhcpGiAddrGrid;
var dhcpGiAddrColumns;
var listGiAddrCm;
var listGiAddrStore;
var dhcpOption60Grid;
var dhcpOption60Columns;
var listOption60Cm;
var listOption60Store;
var dhcpType = null;
function renderDhcpBundlePolicy(value, p, record){
	if (value == '1') {
		return 'primary';
	} else if(value == '2'){
		return 'policy';	
	} else{
		return 'strict';
	}
}
function renderDhcpGiAddrDeviceType(value, p, record){
	var dhcpGiAddrPolicyType = record.data.topCcmtsDhcpGiAddrPolicyType;
	if(dhcpGiAddrPolicyType == '1'){
		if(value == '1'){
			return 'all';
		}
	}else if(dhcpGiAddrPolicyType == '2'){
		if(value == '1'){
			return 'cm';
		}else{
			return 'host/mta/stb';
		}
	}else if(dhcpGiAddrPolicyType == '3'){
		if(value == '1'){
			return 'cm';
		}else if(value == '2'){
			return 'host';
		}else if(value == '3'){
			return 'mta';
		}else{
			return 'stb';
		}
	}

}
function renderDhcpHelperDeviceType(value, p, record){
	if (value == '1') {
		return 'cm';
	} else if(value == '2'){
		return 'host';	
	}  else if(value == '3'){
		return 'mta';	
	}  else if(value == '4'){
		return 'stb';	
	} else{
		return 'all';
	}
}
function onTreeNodeClick(node){
	nodeId = node.attributes.id;
	document.getElementById("baseInfo-div").style.display='none';
	document.getElementById("dhcpConfigInfo-div").style.display='block';
	dhcpType = node.id;
	var i;
	if(dhcpType.split("_")[1]!='0'){
		for(i=0;i<dhcpBundleList.length;i++){
			if(dhcpType.split("_")[1]==dhcpBundleList[i].topCcmtsDhcpBundleInterface){
				if(dhcpBundleList[i].topCcmtsDhcpBundlePolicy==1){
					$("#dhcpBundlePolicy").show();
					$("#dhcpBundlePolicy").val("primary");
				}else if(dhcpBundleList[i].topCcmtsDhcpBundlePolicy==2){
					$("#dhcpBundlePolicy").show();
					$("#dhcpBundlePolicy").val("policy");
				}else if(dhcpBundleList[i].topCcmtsDhcpBundlePolicy==3){
					$("#dhcpBundlePolicy").show();
					$("#dhcpBundlePolicy").val("strict");
				}else{
					$("#dhcpBundlePolicy").hide();
				}
				
			}
		}
	}else {
		$("#dhcpBundlePolicy").hide();
	}
	listHelperStore.load({params: {dhcpType:dhcpType}});
	listGiAddrStore.load({params: {dhcpType:dhcpType}});
	listOption60Store.load({params: {dhcpType:dhcpType}});
}
function addDhcpBundle(){
	if(dhcpBundleList.length == 6){
		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.dhcpBundleNumLimit);
	}else{
		window.parent.createDialog("dhcpBundleModify", I18N.CMC.title.addBundleConfig, 320, 240, 
			'/cmc/dhcp/showDhcpBundleModify.tv?cmcId='+ cmcId + '&modifyFlag=' + 0, null, true, true);
	}
	//beginConstructTree();
}
function updateDhcpBundle(){
	var bundle = dhcpType.split("_")[1];
	window.parent.createDialog("dhcpBundleModify", I18N.CMC.title.modifyBundleConfig, 320, 240,
			'/cmc/dhcp/showDhcpBundleModify.tv?cmcId='+ cmcId + '&bundle=' + bundle + '&modifyFlag=' + 1, null, true, true);
}
function deleteDhcpBundle(){
	var bundle = dhcpType.split("_")[1];
	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.sureToDeleteBundle, function(type) {
        if (type == 'no') {
            return;
        } else{
        	window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.deleteBundle, 'ext-mb-waiting');
        	$.ajax({
	  			url:'/cmc/dhcp/deleteDhcpBundle.tv',
	  			type:'POST',
	  			data:"cmcId="+cmcId+"&bundle="+bundle,
	  			dateType:'json',
	  			success:function(response){
	  	        	if(response == "success"){
	  	        		window.parent.closeWaitingDlg();
	  	        		beginConstructTree();
			            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deleteBundleSuccess);
	  	        	}else{
	  	        		window.parent.closeWaitingDlg();
	 			        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deleteBundleFail);	
	  	        	}
	  			},
	  			error:function(){
	  				window.parent.closeWaitingDlg();
 			        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deleteBundleFail);	
	  			},
	  			cache:false
	  			});
        }
	});
}
function addDhcpServer(status){
	window.parent.createDialog("dhcpServerModify", I18N.CMC.title.addDhcpServerConfig, 320, 240, 
		'/cmc/dhcp/showDhcpServerModify.tv?cmcId='+ cmcId + '&modifyFlag=' + 0, null, true, true);
}
function updateDhcpServer(){
	var sm = dhcpHelperGrid.getSelectionModel();
	var record = sm.getSelected();	
	var helperId = record.data.helperId;
	var ip = record.data.topCcmtsDhcpHelperIpAddr;
	var bundle = record.data.topCcmtsDhcpBundleInterface;
	var type = record.data.topCcmtsDhcpHelperDeviceType;
	if(ip == "" || ip == null){
		ip = "noData";
	}
	window.parent.createDialog("dhcpServerModify", I18N.CMC.title.modifyDhcpServerConfig, 320, 240, 
			'/cmc/dhcp/showDhcpServerModify.tv?cmcId='+ cmcId + '&index=' + helperId + '&ip=' + ip + '&type=' + type + '&bundle=' + bundle + '&modifyFlag=' + 1, null, true, true);
}
function deleteDhcpServer(){
	var sm = dhcpHelperGrid.getSelectionModel();
	var record = sm.getSelected();	
	var helperId = record.data.helperId;
	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.sureToDeleteDhcpServer, function(type) {
        if (type == 'no') {
            return;
        } else{
        	window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.DeletingDhcpServer, 'ext-mb-waiting');
        	$.ajax({
	  			url:'/cmc/dhcp/deleteDhcpServer.tv',
	  			type:'POST',
	  			data:"cmcId="+cmcId+"&index="+helperId,
	  			dateType:'json',
	  			success:function(response){
	  	        	if(response == "success"){
	  	        		window.parent.closeWaitingDlg();
			            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.DeleteDhcpServerSuccess);
	  	        	}else{
	  	        		window.parent.closeWaitingDlg();
	 			        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.DeleteDhcpServerFail);	
	  	        	}
	  			},
	  			error:function(){
	  				window.parent.closeWaitingDlg();
 			        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.DeleteDhcpServerFail);	
	  			},
	  			cache:false
	  			});
        }
	});
}
function addDhcpGiAddr(){
	window.parent.createDialog("dhcpGiAddrModify", I18N.CMC.tip.addDHCPGiAddr, 320, 240, 
		'/cmc/dhcp/showDhcpGiAddrModify.tv?cmcId='+ cmcId + '&modifyFlag=' + 0, null, true, true);
}
function updateDhcpGiAddr(){
	var sm = dhcpGiAddrGrid.getSelectionModel();
	var record = sm.getSelected();
	var giAddrId = record.data.giAddrId;
	var ip = record.data.topCcmtsDhcpGiAddress;
	var ipMask = record.data.topCcmtsDhcpGiAddrMask;
	var bundle = record.data.topCcmtsDhcpBundleInterface;
	var type = record.data.topCcmtsDhcpGiAddrPolicyType;
	if(ip == "" || ip == null){
		ip = "noData";
	}
	if(ipMask == "" || ipMask == null){
		ipMask = "noData";
	}
	window.parent.createDialog("dhcpGiAddrModify", I18N.CMC.tip.modifyDHCPGiAddr, 320, 240, 
			'/cmc/dhcp/showDhcpGiAddrModify.tv?cmcId='+ cmcId + '&index=' + giAddrId + '&ip=' + ip + '&type=' + type + '&ipMask=' + ipMask + '&bundle=' + bundle + '&modifyFlag=' + 1, null, true, true);
}
function deleteDhcpGiAddr(){
	var sm = dhcpGiAddrGrid.getSelectionModel();
	var record = sm.getSelected();	
	var giAddrId = record.data.giAddrId;
	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.sureToDeleteDHCPGiAddr, function(type) {
        if (type == 'no') {
            return;
        } else{
        	window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.deletingDHCPGiAddr, 'ext-mb-waiting');
        	$.ajax({
	  			url:'/cmc/dhcp/deleteDhcpGiAddr.tv',
	  			type:'POST',
	  			data:"cmcId="+cmcId+"&index="+giAddrId,
	  			dateType:'json',
	  			success:function(response){
	  	        	if(response == "success"){
	  	        		window.parent.closeWaitingDlg();
			            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deleteDhcpGiAddrSuccess);
	  	        	}else{
	  	        		window.parent.closeWaitingDlg();
	 			        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deleteDhcpGiAddrFail);	
	  	        	}
	  			},
	  			error:function(){
	  				window.parent.closeWaitingDlg();
 			        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deleteDhcpGiAddrFail);	
	  			},
	  			cache:false
	  			});
        }
	});
}
function addDhcpOption60(){
	window.parent.createDialog("dhcpOption60Modify", I18N.CMC.title.addOptiong60Config, 320, 240, 
		'/cmc/dhcp/showDhcpOption60Modify.tv?cmcId='+ cmcId + '&modifyFlag=' + 0, null, true, true);
}
function updateDhcpOption60(){
	var sm = dhcpOption60Grid.getSelectionModel();
	var record = sm.getSelected();	
	var option60Id = record.data.option60Id;
	var option60Str = record.data.topCcmtsDhcpOption60Str;
	var bundle = record.data.topCcmtsDhcpBundleInterface;
	var type = record.data.topCcmtsDhcpOption60DeviceType;
	window.parent.createDialog("dhcpOption60Modify", I18N.CMC.title.modifyOptiong60Config, 320, 240, 
			'/cmc/dhcp/showDhcpOption60Modify.tv?cmcId='+ cmcId + '&index=' + option60Id + '&type=' + type + '&option60Str=' + option60Str + '&bundle=' + bundle + '&modifyFlag=' + 1, null, true, true);
}
function deleteDhcpOption60(){
	var sm = dhcpOption60Grid.getSelectionModel();
	var record = sm.getSelected();	
	var option60Id = record.data.option60Id;
	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.sureToDeleteOption60, function(type) {
        if (type == 'no') {
            return;
        } else{
        	window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.deletingOption60, 'ext-mb-waiting');
        	$.ajax({
	  			url:'/cmc/dhcp//deleteDhcpOption60.tv',
	  			type:'POST',
	  			data:"cmcId="+cmcId+"&index="+option60Id,
	  			dateType:'json',
	  			success:function(response){
	  	        	if(response == "success"){
	  	        		window.parent.closeWaitingDlg();
			            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deleteOption60Success);
	  	        	}else{
	  	        		window.parent.closeWaitingDlg();
	 			        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deleteOption60Fail);	
	  	        	}
	  			},
	  			error:function(){
	  				window.parent.closeWaitingDlg();
 			        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deleteOption60Fail);
	  			},
	  			cache:false
	  			});
        }
	});
}
function checkedGiAddrAdd(tempDhcpMenu,tempaddDhcpMenu,tempBundle){
	var primaryNum = 0;
	var policyNum = 0;
	var strictNum = 0;
	var i;
	if(tempBundle == '0'){
		Ext.getCmp(tempDhcpMenu).findById(tempaddDhcpMenu).setDisabled(false);
		return;
	}
	for (i = 0; i<dhcpGiAddrList.length;i++){
		if(dhcpGiAddrList[i].topCcmtsDhcpBundleInterface == tempBundle){
			if(dhcpGiAddrList[i].topCcmtsDhcpGiAddrPolicyType == 1){
				primaryNum = primaryNum + 1;
			}
			if(dhcpGiAddrList[i].topCcmtsDhcpGiAddrPolicyType == 2){
				policyNum = policyNum + 1;
			}
			if(dhcpGiAddrList[i].topCcmtsDhcpGiAddrPolicyType == 3){
				strictNum = strictNum + 1;
			}
		}
	}
	for(i =0; i< dhcpBundleList.length;i++){
		if(dhcpBundleList[i].topCcmtsDhcpBundleInterface == tempBundle){
			if(dhcpBundleList[i].topCcmtsDhcpBundlePolicy == 1 && primaryNum > 0){
				Ext.getCmp(tempDhcpMenu).findById(tempaddDhcpMenu).setDisabled(true);
			} else if(dhcpBundleList[i].topCcmtsDhcpBundlePolicy  == 3 && strictNum > 3){
				Ext.getCmp(tempDhcpMenu).findById(tempaddDhcpMenu).setDisabled(true);
			}else{
				Ext.getCmp(tempDhcpMenu).findById(tempaddDhcpMenu).setDisabled(false);
			}
			break;
		}
	}
}
function rootMenuShow(){
	if(dhcpType=="0_0"){
		Ext.getCmp("dhcpBundleMenu").findById("updateBundle").setDisabled(true);
		/*Ext.getCmp("dhcpBundleMenu").findById("deleteBundle").setDisabled(true);*/
	} else{
		Ext.getCmp("dhcpBundleMenu").findById("updateBundle").setDisabled(false);
		/*Ext.getCmp("dhcpBundleMenu").findById("deleteBundle").setDisabled(false);*/
	}
}
function clearBundleTree() {
	var ff = document.getElementById("dhcpBundleListDiv");
	var childs = ff.childNodes;
	for(var i = 0; i < childs.length; i++) {
        ff.removeChild(childs[i]);
    }
}
function beginConstructTree(){
	clearBundleTree();
	try {
		var root = new Ext.tree.TreeNode({text:'Bundle',id:'0_0'});
		for(var i = 0; i < dhcpBundleList.length; i++){
			var bundleNode = new Ext.tree.TreeNode({text : dhcpBundleList[i].topCcmtsDhcpBundleInterface,id : "0_" + dhcpBundleList[i].topCcmtsDhcpBundleInterface,expanded : true,draggable : false});
			var cmNode = new Ext.tree.TreeNode({text : "CM",id : "1_" + dhcpBundleList[i].topCcmtsDhcpBundleInterface,draggable : false});
			bundleNode.appendChild(cmNode);
			var hostNode = new Ext.tree.TreeNode({text : "HOST",id : "2_" + dhcpBundleList[i].topCcmtsDhcpBundleInterface,draggable : false});
			bundleNode.appendChild(hostNode);
			var mtaNode = new Ext.tree.TreeNode({text : "MTA",id : "3_" + dhcpBundleList[i].topCcmtsDhcpBundleInterface,draggable : false});
			bundleNode.appendChild(mtaNode);
			var stbNode = new Ext.tree.TreeNode({text : "STB",id : "4_" + dhcpBundleList[i].topCcmtsDhcpBundleInterface,draggable : false});
			bundleNode.appendChild(stbNode);
			var allNode = new Ext.tree.TreeNode({text : "ALL",id : "5_" + dhcpBundleList[i].topCcmtsDhcpBundleInterface,draggable : false});
			bundleNode.appendChild(allNode);
			root.appendChild(bundleNode);
		}
	    dhcpBundleTree.setRootNode(root);
	    dhcpBundleTree.render();
	    root.expand(false,false);
		//dhcpBundleTree.setRootNode( root ? root : {text : '',leaf : true});
	} catch(exp) {
		//OltTree.setRootNode({text : '',leaf : true});
		//olt.monitor.loadError("");数据加载错误!
	}
}
function loadDhcpBundleGrid(){

	var loader = new Ext.tree.TreeLoader({dataUrl: '/cmc/dhcp/showDhcpBundle.tv?cmcId=' + cmcId});
    dhcpBundleTree = new Ext.tree.TreePanel({
        title:I18N.CMC.title.bundleList,id:'dhcpBundleTreePanel',el:'dhcpBundleListDiv',
        width: 140,height: 320,autoScroll: true,border: true,
        //loader: loader,
        listeners:{
            "click" : onTreeNodeClick
        }
	});
    beginConstructTree();
    var bundleMenu = new Ext.menu.Menu({
    	id:'dhcpBundleMenu',
        enableScrolling: false,
        items:[
        {
    	    id:'addBundle',
    	    text: I18N.CMC.title.addBundle,
	        handler:addDhcpBundle
        },
        {
    	    id:'updateBundle',
    	    text: I18N.CMC.title.modifyBundle,
	        handler:updateDhcpBundle
        },
        /*{
    	    id:'deleteBundle',
    	    text: I18N.CMC.title.deleteBundle,
	        handler:deleteDhcpBundle
        },*/
        {
    	    id:'addGiAddr',
    	    text: I18N.CMC.title.addGiAddr,
	        handler: addDhcpGiAddr
        },
        {
    	    id:'addDHCPServer',
    	    text: I18N.CMC.title.addDhcpServer,
	        handler: addDhcpServer
        },
        {
    	    id:'addOption60',
    	    text: I18N.CMC.title.addOptiong60,
	        handler: addDhcpOption60
        },
        {
    	    id:'refreshInfo',
    	    text: I18N.CMC.title.refreshDataFromEntity,
	        handler:function() {
	        	window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.refreshingDhcpInfo, 'ext-mb-waiting');
	                $.ajax({
	                    url: '/cmc/dhcp/refreshDhcpBundle.tv',
	                    type: 'POST',
	                    data: "cmcId=" + cmcId,
	                    success: function(json) {
	                        if (json) {
	                        	//beginConstructTree();
	                        	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshDhcpInfoSuccess);
	                    	} else {
	                    		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshDhcpInfoFail);
	                    	}
	                    }, error: function(json) {
	                    	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshDhcpInfoFail);
	                	}, dataType: 'json', cache: false
	                });
	        }
        }]
    });
    dhcpBundleTree.on("contextmenu", function(node, e){
    	e.preventDefault();
    	node.select();
    	dhcpType = node.id;
    	var tempBundleStr = dhcpType.split("_")[1];
    	checkedGiAddrAdd("dhcpBundleMenu","addGiAddr",tempBundleStr);
    	rootMenuShow();
    	bundleMenu.showAt(e.getXY());
    })
}
function loadDhcpHelperGrid(){
	var w = document.body.clientWidth - 30;
	dhcpHelperColumns = [
	                         {header: 'Bundle ID', width: 65, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpBundleInterface'},
	                         {header: I18N.CMC.title.type, width: 35, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpHelperDeviceType', renderer: renderDhcpHelperDeviceType},
	                         //{header: 'DHCP Server ID', width: 120, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpHelperIndex'},
	                         {header: I18N.CMC.title.dhcpServerIp, width: 100, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpHelperIpAddr'}
	                         ];
	listHelperCm = new Ext.grid.ColumnModel(dhcpHelperColumns);
	listHelperStore = new Ext.data.JsonStore({
	    url: ('/cmc/dhcp/showDhcpServer.tv?cmcId=' + cmcId),
	    root: 'data',
		fields: ['cmcId','helperId','topCcmtsDhcpBundleInterface','topCcmtsDhcpHelperDeviceType','topCcmtsDhcpHelperIndex','topCcmtsDhcpHelperIpAddr']
	});
	dhcpHelperGrid = new Ext.grid.GridPanel({
		id: 'dhcpHelperGrid',
		renderTo: 'dhcpHelperListDiv',
		width: 210,
		height: 300,
		frame: false,
		autoScroll: true,
		border: false,
		selModel: new Ext.grid.RowSelectionModel({
			singleSelect: true
		}),
		cm: listHelperCm,
		store: listHelperStore

	});
	listHelperStore.load();
	var menuItem = [
		    		 {text: I18N.CMC.title.addDhcpServer, id: 'addDhcpServerMenu', handler: addDhcpServer},
		    		 {text: I18N.CMC.title.modifyDhcpServer, id:'updateDhcpServerMenu',  handler: updateDhcpServer}/*, 
		    		 {text: '', id:'deleteDhcpServerMenu', disabled:true, handler: deleteDhcpServer}*/
		    	];
	var entityMenu = new Ext.menu.Menu({ id: 'dhcpServerMenu', enableScrolling: false,minWidth: 150,items:menuItem
	});
	dhcpHelperGrid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
   		var sm = grid.getSelectionModel();
		entityMenu.showAt(e.getPoint());
    });		
}
function loadDhcpGiAddrGrid(){
	var w = document.body.clientWidth - 30;
	dhcpGiAddrColumns = [
	                         {header: 'Bundle ID', width: 65, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpBundleInterface'},
	                         {header: I18N.CMC.title.policy, width: 50, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpGiAddrPolicyType', renderer:renderDhcpBundlePolicy},
	                         {header: I18N.CMC.title.type, width: 75, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpGiAddrIndex', renderer: renderDhcpGiAddrDeviceType},
	                         {header: I18N.CMC.title.giAddr, width: 100, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpGiAddress'},
	                         {header: I18N.CMC.title.mask, width: 100, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpGiAddrMask'}
	                         ];
	listGiAddrCm = new Ext.grid.ColumnModel(dhcpGiAddrColumns);
	listGiAddrStore = new Ext.data.JsonStore({
	    url: ('/cmc/dhcp/showDhcpGiAddr.tv?cmcId=' + cmcId),
	    root: 'data',
		fields: ['cmcId','giAddrId','topCcmtsDhcpBundleInterface','topCcmtsDhcpGiAddrPolicyType', 'topCcmtsDhcpGiAddrIndex', 'topCcmtsDhcpGiAddress', 'topCcmtsDhcpGiAddrMask']
	});
	dhcpGiAddrGrid = new Ext.grid.GridPanel({
		id: 'dhcpGiAddrGrid',
		renderTo: 'dhcpGiAddrListDiv',
		width: 400,
		height: 300,
		frame: false,
		autoScroll: true,
		border: false,
		selModel: new Ext.grid.RowSelectionModel({
			singleSelect: true
		}),
		cm: listGiAddrCm,
		store: listGiAddrStore
	});
	listGiAddrStore.load();
	var menuItem = [
		    		 {text: I18N.CMC.title.addGiAddr, id: 'addDhcpGiAddrMenu', handler: addDhcpGiAddr},
		    		 {text: I18N.CMC.title.modifyGiAddr, id:'updateDhcpGiAddrMenu',  handler: updateDhcpGiAddr}/*, 
		    		 {text: I18N.CMC.title.deleteGiAddr, id:'deleteDhcpGiAddrMenu', disabled:true, handler: deleteDhcpGiAddr}*/
		    	];
	var entityMenu = new Ext.menu.Menu({ id: 'dhcpGiAddrMenu', enableScrolling: false,minWidth: 100,items:menuItem
	});
	dhcpGiAddrGrid.on('rowcontextmenu', function(grid, rowIndex, e) {
		e.preventDefault();
		var record = grid.getStore().getAt(rowIndex);
		var tempBundle = record.data.topCcmtsDhcpBundleInterface;
		checkedGiAddrAdd("dhcpGiAddrMenu","addDhcpGiAddrMenu",tempBundle);
  		var sm = grid.getSelectionModel();
		entityMenu.showAt(e.getPoint());
   });	
}
function loadDhcpOption60Grid(){
	var w = document.body.clientWidth - 30;
	dhcpOption60Columns = [
	                         {header: 'Bundle ID', width: 65, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpBundleInterface'},
	                         {header: I18N.CMC.title.type, width: 35, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpOption60DeviceType', renderer:renderDhcpHelperDeviceType},
	                         //{header: 'Option60 NO.', width: 100, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpOption60Index'},
	                         {header: 'Option60', width: 65, sortable: true, align: 'center', dataIndex: 'topCcmtsDhcpOption60Str'}
	                         ];
	listOption60Cm = new Ext.grid.ColumnModel(dhcpOption60Columns);
	listOption60Store = new Ext.data.JsonStore({
	    url: ('/cmc/dhcp/showDhcpOption60.tv?cmcId=' + cmcId),
	    root: 'data',
		fields: ['cmcId','option60Id','topCcmtsDhcpBundleInterface','topCcmtsDhcpOption60DeviceType', 'topCcmtsDhcpOption60Index', 'topCcmtsDhcpOption60Str']
	});
	dhcpOption60Grid = new Ext.grid.GridPanel({
		id: 'dhcpOption60Grid',
		renderTo: 'dhcpOption60ListDiv',
		width: 175,
		height: 300,
		frame: false,
		autoScroll: true,
		border: false,
		selModel: new Ext.grid.RowSelectionModel({
			singleSelect: true
		}),
		cm: listOption60Cm,
		store: listOption60Store
	});
	listOption60Store.load();
	var menuItem = [
		    		 {text: I18N.CMC.title.addOptiong60, id: 'addDhcpOption60Menu', handler: addDhcpOption60},
		    		 {text: I18N.CMC.title.modifyOptiong60, id:'updateDhcpOption60Menu',  handler: updateDhcpOption60}/*, 
		    		 {text: 'Delete Option60', id:'deleteDhcpOption60Menu', handler: deleteDhcpOption60}*/
		    	];
	var entityMenu = new Ext.menu.Menu({ id: 'dhcpOption60Menu', enableScrolling: false,minWidth: 100,items:menuItem
	});
	dhcpOption60Grid.on('rowcontextmenu', function(grid, rowIndex, e) {
  	e.preventDefault();
 		var sm = grid.getSelectionModel();
		entityMenu.showAt(e.getPoint());
  });	
}
function listTypeChanged(){
	switch(Zeta$('cmcConfigType').value){
	case '0':
		document.getElementById("baseInfo-div").style.display='block';
		document.getElementById("dhcpConfigInfo-div").style.display='none';
		break;
	case '1':
		document.getElementById("baseInfo-div").style.display='none';
		document.getElementById("dhcpConfigInfo-div").style.display='block';
		break;
	}
}
//是否继续修改的方法
//主调函数：modifyBase,modifyExt,modifyTheSni
function modifyContinu(){
	if(saveFlagList.length - 1 > saveFlagNum){
		saveFlagNum++;
		modify(saveFlagList[saveFlagNum]);
	}else{
		saveFlagNum = 0;
		saveFlagList = new Array();
		if(saveErrorList.length == 0){
			window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.saveConfigSuccess);
		}else{
			var tmpText = "";
			for(var i=0; i<saveErrorList.length; i++){
				if(saveErrorList[i] == 1){
					tmpText = I18N.CMC.tip.baseConfigSaveFail + tmpText;
				}else if(saveErrorList[i] == 2){
					tmpText = I18N.CMC.tip.nmIpSaveFail + tmpText;
				}else if(saveErrorList[i] == 3){
					tmpText = I18N.CMC.tip.sniConfigSaveFail;
				}else if(saveErrorList[i] == 4){
					tmpText = I18N.CMC.tip.snmpConfigSaveFail + tmpText;
				}
			}
			if(saveErrorList.indexOf(1) == -1 && saveFlagList.indexOf(1) != -1){
				tmpText = tmpText + I18N.CMC.tip.baseConfigSaveSuccess;
			}
			if(saveErrorList.indexOf(2) == -1 && saveFlagList.indexOf(2) != -1){
				tmpText = tmpText + I18N.CMC.tip.nmIpSaveSuccess;
			}
			if(saveErrorList.indexOf(3) == -1 && saveFlagList.indexOf(3) != -1){
				tmpText = tmpText + I18N.CMC.tip.sniConfigSaveSuccess;
			}
			if(saveErrorList.indexOf(4) == -1 && saveFlagList.indexOf(4) != -1){
				tmpText =  tmpText + I18N.CMC.tip.snmpConfigSaveSuccess;
			}
			saveErrorList = new Array();
			//window.location.reload();
			if(tmpText==""){
				//
			}else{
				window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, tmpText);
			}
		}
	}
}
