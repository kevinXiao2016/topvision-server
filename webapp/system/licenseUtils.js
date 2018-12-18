function showTip(tips){
	top.afterSaveOrDelete({
		title : '@COMMON.tip@',
		html : tips
	})
}

function refresh(){
	window.location.reload();
}
//点击确定;
function saveFn(){
	var selectReports = '';
	var unSelectReports = '';
	
	var str = entityTypeTree.getAllCheckedBranches();
	var childs = reportTree.getAllChildless();
	var selectReportArray = reportTree.getAllCheckedBranches().split(',')
	for(var i = 0; i < selectReportArray.length; i++){
		if(childs.indexOf(selectReportArray[i]) == -1){
			continue;
		}
		var report = selectReportArray[i].split('-')[0];
		selectReports += report + ','
	}
	
	var childArray = childs.split(',');
	for(var i = 0; i < childArray.length; i++){
		if(selectReportArray.indexOf(childArray[i]) != -1){
			continue;
		}
		var report = childArray[i].split('-')[0];
		unSelectReports += report + ','
	}
	
	selectReports = selectReports.substring(0,selectReports.length-1)
	unSelectReports  = unSelectReports.substring(0,unSelectReports.length-1)
	
	//设备类型非空判断
	if(str.length == 0){
		window.parent.showMessageDlg('@COMMON.tip@', '@license.moreThan@');
		return;
	}
	
	if(!validateInput()){
		return;
	}
	
	var licenseView = {
		"licenseType" : licenseType,
		"user" : $("#man").val(),
		"phone" : $("#phone").val(),
		"mobile" : $("#mobilePhone").val(),
		"email" : $("#email").val(),
		"selectEntityTypes" : str,
		"selectReports" : selectReports,
		"unSelectReports" : unSelectReports,
		"organisation" : $("#company").val(),
		"numberOfDays" : licenseTimeCombo.getValue(),
		"numberOfUsers" : userNumCombo.getValue(),
		"numberOfEngines" : engineNumCombo.getValue(),
		"mobileSurport" : $('#mobile').prop("checked"),
		"pnmpSurport" : $('#pnmp').prop("checked"),
		"reportSurport" : $('#report').prop("checked"), 
		"oltNum" : $("#olt").prop("checked") ? oltNumCombo.getValue() : 0,
		"cmcNum" : $("#cmc").prop("checked") ? cmcNumCombo.getValue() : 0, 
		"onuNum" : $("#onu").prop("checked") ? onuNumCombo.getValue() : 0,   
		"cmtsNum" : $("#cmts").prop("checked") ? cmtsNumCombo.getValue() : 0
	};
	var ifObj=$('#if_resp').get(0);
	ifObj.src="/system/getProductKey.tv?__times="+new Date().valueOf() + "&licenseType=" + licenseView.licenseType 
			+"&user="+licenseView.user+"&phone="+licenseView.phone+"&email="+licenseView.email+"&selectEntityTypes=" + 
			licenseView.selectEntityTypes+"&organisation="+licenseView.organisation
			+ "&numberOfDays="+licenseView.numberOfDays + "&numberOfUsers="+licenseView.numberOfUsers + "&numberOfEngines=" + 
			licenseView.numberOfEngines + "&selectReports="+licenseView.selectReports + "&unSelectReports="+licenseView.unSelectReports + 
			"&mobile="+licenseView.mobile + "&mobileSurport="+licenseView.mobileSurport + "&pnmpSurport="+licenseView.pnmpSurport + "&reportSurport="+licenseView.reportSurport + 
			"&oltNum="+licenseView.oltNum + "&cmcNum="+licenseView.cmcNum + "&onuNum="+licenseView.onuNum + "&cmtsNum="+licenseView.cmtsNum;
}

//根据是否选择了OLT、CMC、CMTS、ONU模块更改设备类型树
function changeEntityTypeTreeByModule(module, ck, entityNames){
	if(entityTypes == null || entityTypes == ''){
		entityTypes = entityTypeTree.getAllChildless().split(',')
	}
	for(var i = 0; i < entityTypes.length; i++){
		if(entityNames.indexOf(entityTypes[i]) != -1){
			modifyTreeItemByCk(ck, entityTypes[i], entityTypeTree)
		}
	}
	if(ck){
		entityTypeTree.disableCheckbox(module,false)
		entityTypeTree.setCheck(module, 1)
		//add by fanzidong, 如果要勾选的是OLT，那么让ONU变得可选
		entityTypeTree.disableCheckbox('onu',false)
		for(var i = 0; i < entityTypes.length; i++){
			if(ONU.indexOf(entityTypes[i]) != -1){
				entityTypeTree.disableCheckbox(entityTypes[i],false)
			}
		}
	}else{
		entityTypeTree.setCheck(module, 0)
		entityTypeTree.disableCheckbox(module,true)
	}
}

//根据是否选择了OLT、CMC、CMTS、ONU模块更改报表树
function changeReportTreeByModule(module, ck){
	if(!$('#report').prop("checked")){
		return;
	}
	if(reportIds == null || reportIds == ''){
		reportIds = reportTree.getAllChildless().split(',')
	}
	for(var i = 0; i < reportIds.length; i++){
		var reportModuleArray = reportIds[i].split('-')[1].split('&');
		if(reportModuleArray.indexOf(module) != -1){
			//报表支持单个模块的情况
			if(reportModuleArray.length == 1){
				modifyTreeItemByCk(ck, reportIds[i], reportTree)
			}else{
				//支持CMTS及CMC的情况
				if((module == 'CMTS' && !$('#cmc').prop("checked")) || (module == 'CMC' && !$('#cmts').prop("checked"))){
					modifyTreeItemByCk(ck, reportIds[i], reportTree)
				}
			}
		}else{
			continue;
		}
		
	}
	modifyAlertTreeItem();
}

//初始化模块信息值
function initModuleCheck(modules){
	for(var i = 0; i < modules.length; i++){
		if(modules[i].name == 'mobile'){
			if(modules[i].enabled){
        		$('#mobile').prop("checked", true)
        	}else{
        		$('#mobile').prop("checked", false)
        	}
			continue;
		}
		if(modules[i].name == 'pnmp'){
			if(modules[i].enabled){
        		$('#pnmp').prop("checked", true)
        	}else{
        		$('#pnmp').prop("checked", false)
        	}
			continue;
		}
		if(modules[i].name == 'report'){
			if(modules[i].enabled){
        		$('#report').prop("checked", true)
				 enableAndCheckTree(reportTree);
        	}else{
        		$('#report').prop("checked", false)
        		 disableAndUncheckTree(reportTree);
        	}
			continue;
		}
		if(modules[i].name == 'olt'){
			if(modules[i].numberOfEntities == '2147483647'){
				oltNumCombo.setValue(-1);
        	}else{
        		oltNumCombo.setValue(modules[i].numberOfEntities);
        	}
			if(modules[i].enabled){
        		$('#olt').prop("checked", true)
        	}else{
        		$('#olt').prop("checked", false)
        	}
			continue;
		}
		if(modules[i].name == 'onu'){
			if(modules[i].numberOfEntities == '2147483647'){
				onuNumCombo.setValue(-1);
        	}else{
        		onuNumCombo.setValue(modules[i].numberOfEntities);
        	}
			if(modules[i].enabled){
        		$('#onu').prop("checked", true)
        	}else{
        		$('#onu').prop("checked", false)
        	}
			continue;
		}
		
		if(modules[i].name == 'cmc'){
			if(modules[i].numberOfEntities == '2147483647'){
				cmcNumCombo.setValue(-1);
        	}else{
        		cmcNumCombo.setValue(modules[i].numberOfEntities);
        	}
			if(modules[i].enabled){
        		$('#cmc').prop("checked", true)
        	}else{
        		$('#cmc').prop("checked", false)
        	}
			continue;
		}
		if(modules[i].name == 'cmts'){
			if(modules[i].numberOfEntities == '2147483647'){
				cmtsNumCombo.setValue(-1);
        	}else{
        		cmtsNumCombo.setValue(modules[i].numberOfEntities);
        	}
			if(modules[i].enabled){
        		$('#cmts').prop("checked", true)
        	}else{
        		$('#cmts').prop("checked", false)
        	}
			continue;
		}
	}
}

//勾选设备类型时级联报表树
function changeReportTreeByEntityTypeTree(){
	var checkedCMCI = 0, checkedCMCII = 0, checkedOLT = 0, checkedONU = 0, checkedCMTS = 0;
	var allCheckedBranches = entityTypeTree.getAllCheckedBranches();
	var allChecked = [];
	if(allCheckedBranches != ''){
		allChecked = entityTypeTree.getAllCheckedBranches().split(',');
	}
	var reportChecke = $('#report').prop("checked");
	for(var i = 0; i < allChecked.length; i++){
		if(OLT.indexOf(allChecked[i]) != -1){
			checkedOLT ++;
			continue;
		}
		if(ONU.indexOf(allChecked[i]) != -1){
			checkedONU ++;
			continue;
		}
		if(CMCI.indexOf(allChecked[i]) != -1){
			checkedCMCI ++;
			continue;
		}
		if(CMCII.indexOf(allChecked[i]) != -1){
			checkedCMCII ++
			continue;
		}
		
		if(CMTS.indexOf(allChecked[i]) != -1){
			checkedCMTS ++
			continue;
		}
	}
	
	if(checkedOLT > 0){
		if(reportChecke){
			changeReportTreeByModule('OLT', true)
		}
		$('#olt').prop("checked", true)
		oltNumCombo.setDisabled(false);
		$('#onu').attr("disabled", false)
		onuNumCombo.setDisabled(false);
		
		// add by fanzidong,勾选OLT时，让ONU可勾选
		var onuList = ONU.split(',');
		entityTypeTree.disableCheckbox('onu', false)
		$.each(onuList, function(i, curOnu){
			entityTypeTree.disableCheckbox(curOnu, false)
		});
	}else{
		if(reportChecke){
			changeReportTreeByModule('OLT', false)
		}
		$('#olt').prop("checked", false)
		oltNumCombo.setDisabled(true);
		
		//add by fanzidong,取消勾选OLT时，也取消对ONU的勾选
		checkedONU = 0;
		var onuList = ONU.split(',');
		entityTypeTree.setCheck('onu', 0);
		entityTypeTree.disableCheckbox('onu', true)
		$.each(onuList, function(i, curOnu){
			entityTypeTree.setCheck(curOnu, 0);
			entityTypeTree.disableCheckbox(curOnu, true)
		});
	}
	
	if(checkedONU > 0){
		if(reportChecke){
			changeReportTreeByModule('ONU', true)
		}
		$('#onu').prop("checked", true)
		onuNumCombo.setDisabled(false);
	}else {
		if(reportChecke){
			changeReportTreeByModule('ONU', false)
		}
		$('#onu').prop("checked", false)
		onuNumCombo.setDisabled(true);
		$('#onu').prop("disabled", checkedOLT === 0)
		changeReportTreeByModule('ONU', false)
		//changeEntityTypeTreeByModule('onu', false, ONU)
	} 
	
	//报表信息中无A型或B型信息
	if(checkedCMCI > 0 || checkedCMCII > 0){
		if(reportChecke){
			changeReportTreeByModule('CMC', true)
		}
		$('#cmc').prop("checked", true)
		cmcNumCombo.setDisabled(false);
	}else{
		if(reportChecke){
			changeReportTreeByModule('CMC', false)
		}
		$('#cmc').prop("checked", false)
		cmcNumCombo.setDisabled(true);
	}
	
	if(checkedCMTS > 0){
		if(reportChecke){
			changeReportTreeByModule('CMTS', true)
		}
		$('#cmts').prop("checked", true)
		cmtsNumCombo.setDisabled(false);
	}else{
		if(reportChecke){
			changeReportTreeByModule('CMTS', false)
		}
		$('#cmts').prop("checked", false)
		cmtsNumCombo.setDisabled(true);
	}
	
}

//初始化页面时对于没有授权的报表不勾选
function initReportTree(licView){
	modules = licView.modules
	for(var i = 0; i < modules.length; i++){
		if(modules[i].name == 'report'){
			exclude = modules[i].exclude;
			break;
		}
	}
	//由于ID后面加了模块，此处特殊处理
	for(var i = 0; i < exclude.length; i++){
		reportTree.setCheck(exclude[i] + '-OLT', 0);
		reportTree.setCheck(exclude[i] + '-ONU', 0);
		reportTree.setCheck(exclude[i] + '-CMC', 0);
		reportTree.setCheck(exclude[i] + '-CMTS', 0);
		reportTree.setCheck(exclude[i] + '-CMTS&CMC', 0);
		reportTree.setCheck(exclude[i] + '-CMC&CMTS', 0);
		reportTree.setCheck(exclude[i] + '-ALL', 0);
		reportTree.setCheck(exclude[i] + '-hz', 0);
	}
}

//初始化页面时对于没有授权的设备类型不勾选
function initEntityTree(licView){
	var exclude = '';
	modules = licView.modules
	for(var i = 0; i < modules.length; i++){
		if(modules[i].name != 'report' && modules[i].name != 'mobile' && modules[i].name != 'pnmp'){
			exclude = modules[i].exclude
			for(var j = 0; j < exclude.length; j++){
				entityTypeTree.setCheck(exclude[j], 0);
			}
		}
	}
}
//全选树
function enableAndCheckTree(tree, module){
	if(reportIds == null || reportIds == ''){
		reportIds = tree.getAllChildless().split(',')
	}
	for(var i = 0; i < reportIds.length; i++){
		tree.disableCheckbox(reportIds[i],false)
		tree.setCheck(reportIds[i], 1)
	}
	
	var parentItems = tree.getAllItemsWithKids().split(',')
	for(var i = 0; i < parentItems.length; i++){
		tree.disableCheckbox(parentItems[i],false)
		tree.setCheck(parentItems[i], 1)
	}
}

//树不可选且都不选择
function disableAndUncheckTree(tree){
	if(reportIds == null || reportIds == ''){
		reportIds = tree.getAllChildless().split(',')
	}
	for(var i = 0; i < reportIds.length; i++){
		tree.setCheck(reportIds[i], 0)
		tree.disableCheckbox(reportIds[i],true)
	}
	var parentItems = tree.getAllItemsWithKids().split(',')
	for(var i = 0; i < parentItems.length; i++){
		tree.setCheck(parentItems[i], 0)
		tree.disableCheckbox(parentItems[i],true)
	}
}

//修改树节点勾选状态
function modifyTreeItemByCk(ck, itemId, tree){
	if(ck){
		 tree.disableCheckbox(itemId,false)
		 tree.setCheck(itemId, 1)
	 }else{
		 tree.setCheck(itemId, 0)
		 tree.disableCheckbox(itemId,true)
	 }
}

function validateInput(){
	if($("#company").val().trim() == ""){
		$("#company").focus();
		return false;
	}
	
	if($("#man").val().trim() == ""){
		$("#man").focus();
		return false;
	}
	
	if(isNotPhoneNumber( $("#phone").val().trim())){
		$("#phone").focus();
		return false;
	}
	
	if($("#mobilePhone").val().trim() == "" || isNotPhoneNumber( $("#mobilePhone").val().trim())){
		$("#mobilePhone").focus();
		return false;
	}
	
	if($("#email").val().trim() != '' && !isEmail( $("#email").val().trim())){
		$("#email").focus();
		return false;
	}
	
	if(licenseTimeCombo.getValue() === "" || isNotNumber(licenseTimeCombo.getValue())){
		$("#numberOfDays").focus();
		return false;
	}
	
	if(userNumCombo.getValue() === "" || isNotNumber(userNumCombo.getValue())){
		$("#numberOfUsers").focus();
		return false;
	}
	
	if(engineNumCombo.getValue() === "" || isNotNumber(engineNumCombo.getValue())){
		$("#numberOfEngines").focus();
		return false;
	}
	
	if(oltNumCombo.getValue() === "" || ($("#olt").prop("checked") && isNotNumber(oltNumCombo.getValue()))){
		$("#oltNum").focus();
		return false;
	}
	
	if(onuNumCombo.getValue() === '' || ($("#onu").prop("checked") && isNotNumber(onuNumCombo.getValue()))){
		$("#onuNum").focus();
		return false;
	}
	
	if(cmcNumCombo.getValue() === "" || ($("#cmc").prop("checked") && isNotNumber(cmcNumCombo.getValue()))){
		$("#cmcNum").focus();
		return false;
	}
	
	if(cmtsNumCombo.getValue() === "" || ($("#ccmts").prop("checked") && isNotNumber(cmtsNumCombo.getValue()))){
		$("#cmtsNum").focus();
		return false;
	}
	
	return true;
}

//验证函数
//email 验证
function isEmail(str){
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    return reg.test(str);
}

//电话号码验证
function isNotPhoneNumber(phoneNumber){
    var reg = /[^0-9^\-]/;
    return reg.test(phoneNumber);
}

//数量验证
function isNotNumber(number){
    var reg = /[^0-9^]/;
    return reg.test(number) && (number != -1);
}