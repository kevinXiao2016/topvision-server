var templateGrid,dataStore,colModel,
    gObj = {
		type : null,
		templateId : null,
		templatename : null,
		inEnable : null,
		inCIR : null,
		inCBS : null,
		inEBS : null,
		outEnable : null,
		outPIR : null,
		outCIR : null
	};
$(function(){
	window.dataStore = new Ext.data.JsonStore({
		url : '/onu/loadUniRateTemplate.tv',
		fields : ["templateId", "templateName", "entityId","portInLimitEnable","portInCIR","portInCBS",
		          "portInEBS","portOutLimtEnable","portOutCIR","portOutPIR","createTime","updateTime"],
		baseParams : {
			entityId : entityId
		}
	});

	window.colModel = new Ext.grid.ColumnModel([ 
	 	{header : "<div style='text-align:center'>@SERVICE.uniRateLimitTemplateName@</div>", align:'left', width : 180,align : 'center',dataIndex : 'templateName'}, 
	 	{header : "@SERVICE.inRate@",width : 90,align : 'center',dataIndex : 'portInLimitEnable', renderer : portEnableRenderer}, 
	 	{header : "IN CIR",width : 80,align : 'center',dataIndex : 'portInCIR'},
	 	{header : "IN CBS",width : 80,align : 'center',dataIndex : 'portInCBS'}, 
	 	{header : "IN EBS",width : 80,align : 'center',dataIndex : 'portInEBS'}, 
	 	{header : "@SERVICE.outRate@",width : 90,align : 'center',dataIndex : 'portOutLimtEnable', renderer : portEnableRenderer}, 
	 	{header : "OUT CIR",width : 80,align : 'center',dataIndex : 'portOutCIR'}, 
	 	{header : "OUT PIR",width : 80,align : 'center',dataIndex : 'portOutPIR'}, 
	 	{header : "@COMMON.manu@",width : 180, align : 'center',dataIndex :'op', fixed : true, renderer : opeartionRender}
	]);
	
	window.templateGrid =  new Ext.grid.GridPanel({
		id : 'templateGrid',
		title : "@SERVICE.uniRateLimitTemplate@",
		height : 370,
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
	
	//添加按钮;
	$("#addBtn").bind("click",function(){moveToNext('add')});
	$("#closeBtn").bind("click", closeClick);
	//返回按钮;
	$("#cancelBtn").bind("click",moveBack);
	//入方向checkbox点击;
	$("#checkBoxIn").bind("click",ckBoxIn);
	//出方向checkbox点击;
	$("#checkBoxOut").bind("click",ckBoxOut);
	if(!operationDevicePower){
		$("#saveBtn").attr("disabled",true);
		$("#addBtn").attr("disabled",true);
	}
});//end document.ready;

function closeClick(){
	window.parent.closeWindow('uniRateBatchConfig');
}

function portEnableRenderer(value, cellmate, record){
	if(value == 1){
		return '<img src="/images/performance/on2.png">';
	}else{
		return '<img src="/images/performance/off2.png">';
	}
}

function opeartionRender(value, cellmate, record){
	var templateId = record.data.templateId;
	if(operationDevicePower){
		var str = String.format('<a href="javascript:;" onclick="showEditTemplate()">@COMMON.modify@</a>/'+
			'<a href="javascript:;" onclick="applyTemplate({0})">@COMMON.apply@</a>/'
			+ '<a href="javascript:;" onClick="deleteTemplate({0})">@COMMON.delete@</a>', templateId);
		return str;
	}else{
		return '--';
	}
}

function showEditTemplate(record){
	var record = templateGrid.getSelectionModel().getSelected();
	
	gObj.templateId = record.data.templateId;
	gObj.templatename = record.data.templateName;
	gObj.inEnable = record.data.portInLimitEnable;
	gObj.inCIR = record.data.portInCIR;
	gObj.inCBS = record.data.portInCBS;
	gObj.inEBS = record.data.portInEBS;
	gObj.outEnable = record.data.portOutLimtEnable;
	gObj.outPIR = record.data.portOutPIR;
	gObj.outCIR = record.data.portOutCIR;
	
	moveToNext('edit');
}

function deleteTemplate(templateId){
	window.parent.showConfirmDlg("@COMMON.tip@", "@SERVICE.delteTemplateConfirm@", function(type) {
		if (type == 'no') {
			return;
		}
		$.ajax({
			url : '/onu/deleteUniRateTemplate.tv',
			type : 'POST',
			data : {
				templateId :　templateId
			},
			success : function() {
				top.afterSaveOrDelete({
					title: '@COMMON.tip@',
			        html: '<b class="orangeTxt">@SERVICE.delteTemplateSuccess@</b>'
			    });
				dataStore.reload();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.delteTemplateFail@");
			},
			cache : false
		});
	});
}

function applyTemplate(templateId){
	window.parent.createDialog('applyToUni', "@SERVICE.batchUniRateLimit@", 800, 500, "/onu/showUniRateApplyPage.tv?entityId="+entityId+"&templateId="+templateId, null, true, true);
	//closeClick();
}

//点击添加按钮，移动到第二屏;
function moveToNext(type){
	$("#w1600").stop().animate({left:-800});
	switch(type){
		case "add":
			gObj.type = "add";
			addRate();
			break;
		case "edit" :
			gObj.type = "edit";
			editRate();
			break;
		
			
	}
};//end moveToNext;
//新增;
function addRate(){
	$("#templateName").val("");
	$("#tbodyIn :text, #tbodyOut :text").val("").attr({
		"disabled" : false,
		"class" : "normalInput w100"
	});
	$("#checkBoxIn, #checkBoxOut").attr({"checked":"checked"});
};//end addRate;

//编辑;
function editRate(){
	$("#templateName").val(gObj.templatename);
	if(gObj.inEnable == 1){
		$("#checkBoxIn").attr({"checked":"checked"});
		$("#tbodyIn").find(":text").attr({
			"disabled" : false,
			"class" : "normalInput w100"
		});
		$("#portInCIR").val(gObj.inCIR);
		$("#portInCBS").val(gObj.inCBS);
		$("#portInEBS").val(gObj.inEBS);
	}else{
		$("#checkBoxIn").attr({"checked": false});
		$("#tbodyIn").find(":text").val("-1").attr({
			"disabled" : true,
			"class" : "normalInputDisabled w100"
		});
	}
	
	if(gObj.outEnable == 1){
		$("#checkBoxOut").attr({"checked":"checked"});
		$("#tbodyOut").find(":text").attr({
			"disabled" : false,
			"class" : "normalInput w100"
		});
		$("#portOutPIR").val(gObj.outPIR);
		$("#portOutCIR").val(gObj.outCIR);
	}else{
		$("#checkBoxOut").attr({"checked":false});
		$("#tbodyOut").find(":text").val("-1").attr({
			"disabled" : true,
			"class" : "normalInputDisabled w100"
		});
	}
}
//移动回第一屏;
function moveBack(){
	$("#w1600").stop().animate({left:0});
};//end moveBack;

//点击入方向checkBox;
function ckBoxIn(){
	var $me = $(this),
	    isChecked = $me.attr("checked");
	if(isChecked == true || isChecked == "true" || isChecked == "checked"){//已勾选;
		switch(gObj.type){
			case "add":
				$("#tbodyIn").find(":text").val("").attr({
					"disabled" : false,
					"class" : "normalInput w100"
				});
			break;
			case "edit":
				$("#tbodyIn").find(":text").attr({
					"disabled" : false,
					"class" : "normalInput w100"
				});
				$("#portInCIR").val(gObj.inCIR);
				$("#portInCBS").val(gObj.inCBS);
				$("#portInEBS").val(gObj.inEBS);
			break;
		}
	}else{
		$("#tbodyIn").find(":text").val("-1").attr({
			"disabled" : true,
			"class" : "normalInputDisabled w100"
		});
	}
};//end ckBoxIn;
//点击出方向checkBox;
function ckBoxOut(){
	var $me = $(this),
    isChecked = $me.attr("checked");
	if(isChecked == true || isChecked == "true" || isChecked == "checked"){//已勾选;
		switch(gObj.type){
			case "add":
				$("#tbodyOut").find(":text").val("").attr({
					"disabled" : false,
					"class" : "normalInput w100"
				});
			break;
			case "edit":
				$("#tbodyOut").find(":text").attr({
					"disabled" : false,
					"class" : "normalInput w100"
				});
				$("#portOutPIR").val(gObj.outPIR);
				$("#portOutCIR").val(gObj.outCIR);
			break;
		}
		
	}else{
		$("#tbodyOut").find(":text").val("-1").attr({
			"disabled" : true,
			"class" : "normalInputDisabled w100"
		});
	}
};//end ckBoxOut;

function saveClick(){
	var templateName = $("#templateName").val().trim();
	var portInEnable =  $("#checkBoxIn").attr("checked") ? 1:2;
	var portInCIR = $("#portInCIR").val();
	var portInCBS = $("#portInCBS").val();
	var portInEBS = $("#portInEBS").val();
	var portOutEnable =  $("#checkBoxOut").attr("checked") ? 1:2;
	var portOutPIR = $("#portOutPIR").val();
	var portOutCIR = $("#portOutCIR").val();
	
	if(!checkName(templateName)){
		$("#templateName").focus();
		return;
	}
	
	if(portInEnable == 1){
		if(!checkInput(portInCIR, 1, 1000000)){
			$("#portInCIR").focus();
			return;
		}
		
		if(!checkInput(portInCBS, 0,16383)){
			$("#portInCBS").focus();
			return;
		}
		
		if(!checkInput(portInEBS, 0,16383)){
			$("#portInEBS").focus();
			return;
		}
	}
	
	if(portOutEnable == 1){
		if(!checkInput(portOutPIR, 0, 1000000)){
			$("#portOutPIR").focus();
			return;
		}
		
		if(!checkInput(portOutCIR, 0, 1000000)){
			$("#portOutCIR").focus();
			return;
		}
		if(parseInt(portOutPIR) < parseInt(portOutCIR)){
			$("#portOutPIR").focus();
			return;
		}
	}
	
	switch(gObj.type){
		case "add":
			$.ajax({
				url : '/onu/addUniRateTemplate.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					templateName : templateName,
					portInLimitEnable : portInEnable,
					portInCIR : portInCIR,
					portInCBS : portInCBS,
					portInEBS : portInEBS,
					portOutLimtEnable : portOutEnable,
					portOutCIR : portOutCIR,
					portOutPIR : portOutPIR
				},
				success : function() {
					top.afterSaveOrDelete({
						title: '@COMMON.tip@',
				        html: '<b class="orangeTxt">@SERVICE.addTemplateSuccess@</b>'
				    });
					dataStore.reload();
					moveBack();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.addTemplateFail@");
				},
				cache : false
			});
			break;
		case "edit":
			$.ajax({
				url : '/onu/modifyUniRateTemplate.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					templateId : gObj.templateId,
					templateName : templateName,
					portInLimitEnable : portInEnable,
					portInCIR : portInCIR,
					portInCBS : portInCBS,
					portInEBS : portInEBS,
					portOutLimtEnable : portOutEnable,
					portOutCIR : portOutCIR,
					portOutPIR : portOutPIR
				},
				success : function() {
					top.afterSaveOrDelete({
						title: '@COMMON.tip@',
				        html: '<b class="orangeTxt">@SERVICE.modifyTemplateSuc@</b>'
				    });
					dataStore.reload();
					moveBack();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.modifyTemplateFail@");
				},
				cache : false
			});
		break;
	}
}

function checkInput(input, min, max){
	var reg1 = /^([0-9])+$/;
	if(reg1.exec(input) && input >= min && input <= max){
		return true;
	}else{
		return false;
	}
}

function checkName(name){
	reg = /^[a-zA-Z\d\u4e00-\u9fa5-_]{1,63}$/
	if(reg.exec(name)){
		return true;
	}else{
		return false;
	}
}







