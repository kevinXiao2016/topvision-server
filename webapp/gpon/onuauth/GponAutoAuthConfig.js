Ext.onReady( loadGrid );
function loadGrid(){
	autoAuthStore = new Ext.data.JsonStore({
		url: '/gpon/onuauth/loadAutoAuthConfigList.tv',
		baseParams: {entityId: @{entityId}@},
		autoLoad:true,
		fields:["entityId","authIndex","autoAuthPortList","onuAutoAuthenPortlist","onuType","catvNum","ethNum","veipNum","wlanNum","lineProfileId","srvProfileId"]
	});
	
	autoAuthGrid = new Ext.grid.GridPanel({
		renderTo: 'autoAuthContainer',
		bodyCssClass: 'normalTable',
		height: 380,
		viewConfig:{},
		sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
		autoScroll: true,
		columns: [
			{header: "@OnuAuth.authNo@" , dataIndex: 'authIndex'},
		    {header: "@OnuAuth.onuType@" , dataIndex: 'onuType'},
		    {header: "PON@COMMON.port@" , dataIndex: 'onuAutoAuthenPortlist',renderer: portRender},
			{header: "@OnuAuth.portNum@" , dataIndex: 'ethNum',renderer: portNumRender},
			{header: "@OnuAuth.wlanNum@" , dataIndex: 'wlanNum',renderer: portNumRender},
			{header: "@OnuAuth.catvNum@" , dataIndex: 'catvNum',renderer: portNumRender},
			{header: "@OnuAuth.veipNum@" , dataIndex: 'veipNum',renderer: portNumRender},
			{header: "@OnuAuth.lineProfile@" , dataIndex: 'lineProfileId'},
			{header: "@OnuAuth.srvProfile@" , dataIndex: 'srvProfileId'},
			{header: "@COMMON.manu@" ,dataIndex: 'authIndex', renderer: handlerRender}
		],
		store:autoAuthStore
	});
	
	lineComboBox = new Ext.form.ComboBox({
		emptyText: '@COMMON.select@',
		mode: 'remote',
		editable:false,
		triggerAction: 'all',
		valueField : 'gponLineProfileId',
		displayField : 'gponLineProfileName',//gponLineProfileName
		applyTo: 'lineProfileId',
		store : new Ext.data.JsonStore({   
		    url:"/gpon/profile/loadLineProfileList.tv?entityId=@{entityId}@",
		    autoLoad:true,
		    fields: ["gponLineProfileId", {name:"gponLineProfileName",convert:function(v,r){return v+"("+r.gponLineProfileId+")";}}]
		})
	 });
	 
	srvComboBox = new Ext.form.ComboBox({
		emptyText: '@COMMON.select@',
		mode: 'remote',
		cls : 'test',
		editable:false,
		triggerAction: 'all',
		valueField : 'gponSrvProfileId',
		displayField : 'gponSrvProfileName',
		applyTo: 'srvProfileId',
		store : new Ext.data.JsonStore({   
			url:"/gpon/profile/loadServiceProfileList.tv?entityId=@{entityId}@",
			autoLoad:true,
			fields: ["gponSrvProfileId",{name:"gponSrvProfileName",convert:function(v,r){return v+"("+r.gponSrvProfileId+")";}}]
		})
	 });
	
};

function portRender(v){
	if(v.length==0){
		return "--";
	}
	var list = v.split(":");
	var ports="";
	var portsTooltip = "";
	var maxNumSingleRow = 8;
	for(var i=0;i<list.length/2;i++){
		ports += ",";
		ports += parseInt(list[2*i],16)+"/"+parseInt(list[2*i+1],16)
		portsTooltip += ", ";
		if(i>0 && i%maxNumSingleRow == 0){
			portsTooltip += "\r\n";
		}
		portsTooltip += parseInt(list[2*i],16)+"/"+parseInt(list[2*i+1],16)
		
	}
	return "<div title='"+portsTooltip.substring(2)+"'>"+ports.substring(1)+"</div>";
}
function portChange(v){
	if(v.length==0){
		return "--";
	}
	var values= [];
	var list = v.split(":");
	var ports="";
	var portsTooltip = "";
	var maxNumSingleRow = 8;
	for(var i=0;i<list.length/2;i++){
		ports += ",";
		ports += parseInt(list[2*i],16)+"/"+parseInt(list[2*i+1],16)
		portsTooltip += ", ";
		if(i>0 && i%maxNumSingleRow == 0){
			portsTooltip += "\r\n";
		}
		portsTooltip += parseInt(list[2*i],16)+"/"+parseInt(list[2*i+1],16)
		values.push(parseInt(list[2*i],16)+"/"+parseInt(list[2*i+1],16))
		
	}
	return values;
}

function portNumRender(v){
	if(v==255){
		return "unconcern";
	}
	return v;
}


function createPonSelectTree(o){
	ponSelector = new PonSelector({
		entityId : @{entityId}@,
		type:@{GponConstant.PORT_TYPE_GPON}@,
		width : 150,
		subWidth : 150,
		renderTo : "portTree",
		readyCallback: function(entityId, jsonData){
			if(o && o.callback){
				o.callback(entityId, jsonData);
			}
		}
	});	
	ponSelector.render();
}

function handlerRender(v,m,r){
	if(operationDevicePower){
		return String.format('<a href="javascript:;" onclick="updateAutoAuthConfig()">@COMMON.update@</a> / <a href="javascript:;" onclick="deleteGponAutoAuth({authIndex})">@COMMON.del@</a>',r.data);
	}else{
		return '<span class="disabledTxt">@COMMON.update@</span> / <span class="disabledTxt">@COMMON.del@</span>';
	}
}
	
function refreshGponOnuAuth(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@");
 	$.ajax({
        url : '/gpon/onuauth/refreshGponOnuAuth.tv?entityId=@{entityId}@' ,
        success : function() {
        	top.closeWaitingDlg();
        	top.nm3kRightClickTips({
				title: "@COMMON.tip@", html: "@COMMON.fetchOk@"
			});
        	autoAuthStore.reload();
        },
        error : function() {
            window.parent.showMessageDlg("@COMMON.tip@", "@EPON/onuAuth.tip.fetchFailed@");
        }
    });
}

//添加;
function addAutoAuthConfig(){
	//标题切换
	$(".addAutoAuthTem").show();
	$(".updateAutoAuthTem").hide();
	
	var $addDivBg = $("#addDivBg"),
	$addDiv = $("#addDiv");
	$addDiv.find("input,select").val("");
	lineComboBox.setValue(null);
	srvComboBox.setValue(null);
	if(typeof ponSelector != 'undefined'){
		ponSelector.setValue(null);
	}
	$addDivBg.css({display:"block"});
	$addDiv.css({display:"block"});
	if(!WIN.PON_TREE_CREATED){
		createPonSelectTree();
		WIN.PON_TREE_CREATED = true;
	}
}

//修改;
function updateAutoAuthConfig(){
	var record = autoAuthGrid.getSelectionModel().getSelected();
	
	//标题切换
	$(".addAutoAuthTem").hide();
	$(".updateAutoAuthTem").show();
	
	// 设置修改之前的值
	$("#onuType").val(record.data.onuType);
	$("#entityId").val(record.data.entityId);
	$("#ethNum").val(record.data.ethNum == 255 ? "" : record.data.ethNum);
	$("#wlanNum").val(record.data.wlanNum == 255 ? "" : record.data.wlanNum);
	$("#catvNum").val(record.data.catvNum == 255 ? "" : record.data.catvNum);
	$("#veipNum").val(record.data.veipNum == 255 ? "" : record.data.veipNum);
	lineComboBox.setValue(record.data.lineProfileId);
	srvComboBox.setValue(record.data.srvProfileId);
	
	//设置pon selector的值
	var nameList = portChange(record.data.onuAutoAuthenPortlist);
	if(WIN.PON_TREE_CREATED){
		ponSelector = null;
	}
	createPonSelectTree({
		callback: function(entityId, jsonData){
			var selectArr = [];
			$.each(jsonData, function(i, v){
				if(!v.nocheck){
					$.each(nameList, function(j, val){
						if(val == v.name){
							selectArr.push(v.id)
						}
					});	
				}
			});//end each;
			ponSelector.setSelectValues(selectArr);
		}
	});
	
	var $addDivBg = $("#addDivBg"),
	$addDiv = $("#addDiv");
	$addDivBg.css({display:"block"});
	$addDiv.css({display:"block"});
}

//取消
function closeAddLayer(){
	$("#addDivBg, #addDiv").fadeOut();
}

function updateGponOnuAutoAuth(){
	if($("#ethNum").val() && !V.isInRange($("#ethNum").val(),[0,24])){
		return $("#ethNum").focus();
	}
	if($("#wlanNum").val() && !V.isInRange($("#wlanNum").val(),[0,2])){
		return $("#wlanNum").focus();
	}
	if($("#catvNum").val() && !V.isInRange($("#catvNum").val(),[0,2])){
		return $("#catvNum").focus();
	}
	if($("#veipNum").val() && !V.isInRange($("#veipNum").val(),[0,8])){
		return $("#veipNum").focus();
	}
	var record = autoAuthGrid.getSelectionModel().getSelected();
	var url = '/gpon/onuauth/updateGponAutoAuth.tv';
	var ponIndexs = ponSelector.getSelectedIndexs();
	var data = {
		ponIndexs : ponIndexs,
    	'gponAutoAuthConfig.entityId':@{entityId}@,
    	"gponAutoAuthConfig.onuType":$("#onuType").val(),
    	"gponAutoAuthConfig.catvNum":$("#catvNum").val() ? $("#catvNum").val() : 255,
    	"gponAutoAuthConfig.ethNum":$("#ethNum").val() ? $("#ethNum").val() : 255,
    	"gponAutoAuthConfig.veipNum":$("#veipNum").val() ? $("#veipNum").val() : 255,
    	"gponAutoAuthConfig.wlanNum":$("#wlanNum").val() ? $("#wlanNum").val() : 255,
    	"gponAutoAuthConfig.lineProfileId":lineComboBox.getValue(),
    	"gponAutoAuthConfig.srvProfileId":srvComboBox.getValue(),
    	"gponAutoAuthConfig.authIndex": record.data.authIndex
    };
	gponOnuAutoAuthFn("@OnuAuth.updatingAutoAuth.tip@", url, data, "@OnuAuth.updateAutoAuthOk@", "@OnuAuth.updateAutoAuthEr@");
}

function gponOnuAutoAuthFn(waitTip, url, data, successTip, errorTip){
	window.top.showWaitingDlg("@COMMON.wait@", waitTip);
    $.ajax({
        url: url,
        type: 'POST',
        data: data,
        success: function() {
        	top.closeWaitingDlg();
        	top.nm3kRightClickTips({
				title: "@COMMON.tip@", html: successTip
			});
        	autoAuthStore.reload();
        	closeAddLayer();
        }, error: function(json) {
            top.showMessageDlg("@COMMON.error@", errorTip);
        }, cache: false
    });
}

function addGponOnuAutoAuth(){
	if($("#ethNum").val() && !V.isInRange($("#ethNum").val(),[0,24])){
		return $("#ethNum").focus();
	}
	if($("#wlanNum").val() && !V.isInRange($("#wlanNum").val(),[0,2])){
		return $("#wlanNum").focus();
	}
	if($("#catvNum").val() && !V.isInRange($("#catvNum").val(),[0,2])){
		return $("#catvNum").focus();
	}
	if($("#veipNum").val() && !V.isInRange($("#veipNum").val(),[0,8])){
		return $("#veipNum").focus();
	}
	var url = '/gpon/onuauth/addGponAutoAuth.tv';
	var ponIndexs = ponSelector.getSelectedIndexs();
	var data = {
    	ponIndexs : ponIndexs,
    	'gponAutoAuthConfig.entityId':@{entityId}@,
    	"gponAutoAuthConfig.onuType":$("#onuType").val(),
    	"gponAutoAuthConfig.catvNum":$("#catvNum").val() ? $("#catvNum").val() : 255,
    	"gponAutoAuthConfig.ethNum":$("#ethNum").val() ? $("#ethNum").val() : 255,
    	"gponAutoAuthConfig.veipNum":$("#veipNum").val() ? $("#veipNum").val() : 255,
    	"gponAutoAuthConfig.wlanNum":$("#wlanNum").val() ? $("#wlanNum").val() : 255,
    	"gponAutoAuthConfig.lineProfileId":lineComboBox.getValue(),// $("#lineProfileId").val(),
    	"gponAutoAuthConfig.srvProfileId":srvComboBox.getValue()
    };
	gponOnuAutoAuthFn("@OnuAuth.addingAutoAuth.tip@", url, data, "@OnuAuth.addAutoAuthOk@", "@OnuAuth.addAutoAuthEr@");
}

function deleteGponAutoAuth(authIndex){
	window.top.showWaitingDlg("@COMMON.wait@", "@OnuAuth.deletingAutoAuth.tip@");
    $.ajax({
        url: '/gpon/onuauth/deleteGponAutoAuth.tv',type: 'POST',
        data: {
        	'gponAutoAuthConfig.entityId':@{entityId}@,
        	"gponAutoAuthConfig.authIndex":authIndex
        },
        success: function() {
        	top.closeWaitingDlg();
        	top.nm3kRightClickTips({
				title: "@COMMON.tip@", html: "@OnuAuth.deleteAutoAuthOk@"
			});
        	autoAuthStore.reload();
        	closeAddLayer();
        }, error: function(json) {
            top.showMessageDlg("@COMMON.error@", "@OnuAuth.deleteAutoAuthEr@");
        }, cache: false
    });
}

function cancelClick() {
	window.parent.closeWindow('gponAutoAuth')
}