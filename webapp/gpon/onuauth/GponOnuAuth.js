var entityId = @{entityId}@;//OLT的entityId
var ponIndex = @{ponIndex}@;
var slotIdList = @{JSON:oltSlotList}@;
var ponIdList = @{JSON:oltPonAttributes}@;
var authModeList = @{JSON:authModeList}@;
var $slotId = @{slotId}@;
var $ponId = @{ponId}@;
var toggleState,CURRENT_AUTH_MODE;
var onuMaxNumInPon = 128;    //当前PON口下的ONU最大数目，default:128
var onuMaxNumList = @{onuMaxNumList}@;   //各个PON口支持的ONU最大数   [[num, num, num, num....],[],[]...]没有的全部赋值为0
var onuNoSelStore,onuNoSelComboBox,ponSelector,allocatedOnuNoList = [];
var gponOnuNo = [];
var tempPonId,tempPonIndex;

$(function(){
	for(var i=1; i<=onuMaxNumInPon; i++){
		gponOnuNo.push({id:i,onuNo:i});
	}
	onuNoSelStore = new Ext.data.JsonStore({   
		fields: ["id","onuNo"],
		data: gponOnuNo
	});
	onuNoSelComboBox = new Ext.form.ComboBox({
		emptyText: '@COMMON.select@',
		mode: 'local',
		width:60,
		cls : 'test',
		editable:false,
		triggerAction: 'all',
		valueField : 'id',
		displayField : 'onuNo',
		renderTo: 'gponOnuNoSel',
		store : onuNoSelStore
	 });
	autoFindStore = new Ext.data.JsonStore({
		url:"/gpon/onuauth/loadAutoFindOnuList.tv",
		baseParams:{entityId:entityId},
		//autoLoad:true,
        fields : ['entityId',"location",'onuIndex','autoFindTime',"findTime",'hardwareVersion','loid','loidPassword','onuType','password','serialNumber','softwareVersion']
    });
	autoFindGrid = new Ext.grid.GridPanel({
        renderTo : 'authOnuDiv',
        store : autoFindStore,
        width : 765,
        height: 160,
        title: '@OnuAuth.autoFindList@',
        cls : 'normalTable',
        frame : false,
        autoScroll : true,
        border : true,
        viewConfig: { forceFit: true },
        selModel : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        columns: [
			{header: "@OnuAuth.onu.location@",dataIndex: 'location'},
			{header: "@OnuAuth.onuType@",dataIndex: 'onuType'},
			{header: "SN",dataIndex: 'serialNumber',width:180,renderer:snRender},
			{header: "@COMMON.password@",dataIndex: 'password',renderer:contentRender},
			{header: "LOID",dataIndex: 'loid',renderer:contentRender},
			{header: "LOID@COMMON.password@",dataIndex: 'loidPassword',renderer:contentRender},
			{header: "@OnuAuth.software.version@",dataIndex: 'softwareVersion'},
			{header: "@OnuAuth.hardware.version@",dataIndex: 'hardwareVersion'},
			{header: "@OnuAuth.lastAuthTime@",dataIndex: 'findTime',width:170},
			{header: "@COMMON.manu@",dataIndex: 'location',width:150,fixed:true,renderer: autoFindHandlerRender}
		]
    });
	autoFindGrid.hide();
	window.parent.getWindow("gponOnuAuthen").setHeight(490);
	authStore = new Ext.data.JsonStore({
		url:"/gpon/onuauth/loadOnuAuthConfigList.tv",
		baseParams:{entityId:entityId},
        fields : ['authenOnuId',"location","sn","password","loid","loidPassword","lineProfileId","srvProfileId"]
    });
	authGrid = new Ext.grid.GridPanel({
        renderTo : 'authOnuDiv',
        store : authStore,
        width : 765,
        height: 280,
        title: '@OnuAuth.onuAuthList@',
        cls : 'normalTable',
        bodyCssClass: "normalTable",
        viewConfig: { forceFit: true },
        columns: [
			{header: "@OnuAuth.onu.location@",dataIndex: 'location',width:50},
			{header: "SN",dataIndex: 'sn',renderer:snRender},
			{header: "@COMMON.password@",dataIndex: 'password',renderer:contentRender},
			{header: "LOID",dataIndex: 'loid',renderer:contentRender},
			{header: "LOID@COMMON.password@",dataIndex: 'loidPassword',renderer:contentRender},
			{header: "@OnuAuth.lineProfile@",dataIndex: 'lineProfileId',width:50},
			{header: "@OnuAuth.srvProfile@",dataIndex: 'srvProfileId',width:50},
			{header: "@COMMON.manu@",dataIndex: 'sn',width:80,renderer:handlerRender}
        ]
	});
	WIN.imgBtn = new Nm3kSwitch("useAutoFind",true,{
	    yesNoValue : [true, false],
	    afterChangeCallback : switchHandlerCallback
	});
	imgBtn.init(); 
	
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
	
	initSlotSelections();
});

function addAutoFindToGponAuth(entityId,onuIndex){
	top.createDialog('addGponOnuAuth', "@OnuAuth.addAuth@", 600, 400, '/gpon/onuauth/showAddGponOnuAuth.tv?entityId=' + entityId+"&onuIndex="+onuIndex, null, true, true,function(){
		autoFindStore.reload();
	});
}
function handlerRender(value, m, record){
    if(operationDevicePower){
    	var tmpl = '<a href="javascript:;" onclick="deleteGponAuth(\'{authenOnuId}\')">@COMMON.del@</a>'
        return String.format( tmpl, record.data );
    }else{
    	return "<span class='lightGrayTxt'>@COMMON.del@</span>"
    }
}
function autoFindHandlerRender(v,m,r){
	if(operationDevicePower){
		var tmpl = "<a href='javascript:;' onClick='addAutoFindToGponAuth({entityId},{onuIndex})'>@OnuAuth.joinAuth@</a> ";
		return String.format( tmpl, r.data );
	}else{
		return "--";
	}
}
function deleteGponAuth(authenOnuId){
	window.top.showConfirmDlg('@COMMON.tip@','@OnuAuth.comfirmDeleteGponAuth@',function(type){
	    if(type == 'no'){ return;}
	    window.top.showWaitingDlg("@COMMON.wait@", "@OnuAuth.deletingGponOnuAuth@");
	    $.ajax({
	        url: '/gpon/onuauth/deleteGponOnuAuth.tv',type: 'POST',
	        data: {
	        	'gponOnuAuthConfig.entityId':@{entityId}@,
	        	'gponOnuAuthConfig.authenOnuId':authenOnuId
	        },
	        success: function() {
	        	top.closeWaitingDlg();
	        	top.nm3kRightClickTips({
					title: "@COMMON.tip@", html: "@OnuAuth.deleteGponAuthOk@"
				});
	        	authStore.reload();
	        }, error: function(json) {
	            top.showMessageDlg("@COMMON.error@", "@OnuAuth.deleteGponAuthEr@");
	        }, cache: false
	    });
	});
}

function contentRender(v){
	return v || "--";
}
function snRender(v){
	return v.replaceAll(":","");
}
function initSlotSelections(){
	var position = Zeta$("slotSel");
    var $slot = $("#slotSel");
    position.options.length = 0;
    for (var i = 0; i < slotIdList.length; i++) {
        $slot.append(String.format("<option value={0}>{1}</option>", slotIdList[i].slotId, slotIdList[i].slotNo));
    }
    $slot.val(@{slotId}@);
	syncSlot();	
}
function toggleAutoFind(){
	var thisWindow = window.parent.getWindow("gponOnuAuthen");
	if(toggleState){
		autoFindGrid.hide();
		thisWindow.setHeight(490);
	}else{
		autoFindGrid.show();
		thisWindow.setHeight(640);
	}
	toggleState = !toggleState;
}
function syncSlot(){
    var $pon = $("#ponSel");
    var slotId = $('#slotSel').val();
    $pon.empty();
    for (var i = 0; i < ponIdList.length; i++) {
        if (ponIdList[i].slotId == slotId) {
            $pon.append(String.format("<option value={0} name={2}>{1}</option>", ponIdList[i].ponId, ponIdList[i].ponNo, ponIdList[i].ponIndex));
        }
    }
    $pon.val(@{ponId}@);
    syncPon();
}
function syncPon(modeValue,autoFindEnable){
    var ponId=$("#ponSel").val() || @{ponId}@;
	$.each(authModeList,function(index,mode){
		if(mode.ponId == ponId){
			if(modeValue){
				mode.ponOnuAuthenMode = modeValue;
			}
			if(autoFindEnable){
				mode.ponAutoFindEnable=autoFindEnable;
			}
			WIN.ponIndex = mode.ponIndex;
			$("#modeSel").val(CURRENT_AUTH_MODE=mode.ponOnuAuthenMode);
			syncMode(CURRENT_AUTH_MODE);
			imgBtn.setValue(mode.ponAutoFindEnable==@{GponConstant.PON_AUTO_FIND_ENABLED}@);
			return false;
		}
	});
	autoFindStore.load({params:{ponIndex:ponIndex}});
	authStore.load({params:{ponIndex:ponIndex}});
}
//需要根据目前选择的Pon板和Pon口刷新页面;
function refreshPageFn(){
	var entityId = window.entityId;
	var slotId = $('#slotSel').val();
	var ponId = $('#ponSel').val();
	var ponIndex = $('#ponSel').find("option:selected").attr('name');
	var href = window.location.href.split('?')[0];
	
	if(slotId == null || ponId == null || ponIndex == undefined) {
		slotId = $slotId;
		ponId = $ponId;
		ponIndex = window.ponIndex;
	}
	var src = String.format('{0}?entityId={1}&slotId={2}&ponId={3}&ponIndex={4}', href, entityId, slotId, ponId, ponIndex);
	window.location.href = src;
}
function refreshGponOnuAuth(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@");
 	$.ajax({
        url : '/gpon/onuauth/refreshGponOnuAuth.tv?entityId=' + entityId,
        success : function() {
        	top.closeWaitingDlg();
        	top.nm3kRightClickTips({
				title: "@COMMON.tip@", html: "@COMMON.fetchOk@"
			});
        	//autoFindStore.reload();
        	//authStore.reload();
        	//location.reload();
        	refreshPageFn();
        },
        error : function() {
            window.parent.showMessageDlg("@COMMON.tip@", "@EPON/onuAuth.tip.fetchFailed@");
        }
    });
}
function modeChange(){
	 top.showConfirmDlg("@COMMON.tip@", "@EPON/onuAuth.tip.changeModeConfirm@", function(type) {
		var $modeSel = $("#modeSel");
        if (type == 'no') {
        	return $modeSel.val( CURRENT_AUTH_MODE );
        }
        top.showWaitingDlg("@COMMON.wait@", "@EPON/onuAuth.tip.changingMode@");
        $.ajax({
            url : "/gpon/onuauth/modifyOnuAuthMode.tv",
            data: 'authMode=' + $modeSel.val() + '&entityId=' + entityId + '&ponIndex=' + ponIndex,
            success : function() {
            	syncPon($modeSel.val());
            	top.closeWaitingDlg();
            	top.nm3kRightClickTips({
    				title: "@COMMON.tip@", html: "@EPON/onuAuth.tip.changeModeSuc@"
    			});
            },
            error : function() {
                top.showMessageDlg("@COMMON.tip@", "@EPON/onuAuth.tip.changeModeFailed@");
                $modeSel.val( CURRENT_AUTH_MODE );
            }
        });
    });
}

function switchHandlerCallback(value){
	 var $modeSel = $("#modeSel");
	 var $action = value?"@COMMON.open@":"@COMMON.close@";
	top.showWaitingDlg("@COMMON.wait@", String.format("@OnuAuth.confirmAutoFind.tip@",$action));
	value = value ? @{GponConstant.PON_AUTO_FIND_ENABLED}@ : @{GponConstant.PON_AUTO_FIND_DISABLED}@;
    $.ajax({
        url : "/gpon/onuauth/modifyOnuAutoFind.tv",
        data: {
        	authMode: $modeSel.val(),
        	entityId: entityId,
        	ponIndex: ponIndex,
        	autoFindConfig: value
        },
        success : function() {
        	syncPon(null,value);
        	top.closeWaitingDlg();
        	top.nm3kRightClickTips({
				title: "@COMMON.tip@", html: String.format("@OnuAuth.autoFindEnableOk@",$action)
			});
        },
        error : function() {
            top.showMessageDlg("@COMMON.tip@", String.format("@OnuAuth.autoFindEnableEr@",$action));
            $modeSel.val( CURRENT_AUTH_MODE );
        }
    });
}

//添加
function openAddDivFn(){
	//start 每次点击添加GPON onu认证都重新生成ponSelector，解决新增后onuNo不立即更新的问题
	ponSelector = new PonSelector({
		entityId : entityId,
		type:@{GponConstant.PORT_TYPE_GPON}@,
		singleSelect : true,
		width : 88,
		subWidth : 150,
		renderTo : "gponIndex",
		afterClick:function(node){
			// 如果选中则重新生成新的onuNo list
			if(node.length>0){
				var arr = node[0].name.split("/");
				// arr[0] pon板 arr[1] pon口
				onuMaxNumInPon = onuMaxNumList[arr[0]][arr[1]];
				// 重新加载onuNo
				reloadOnuNoList(node[0].ponId);
				// 获取选中的ponId,ponIndex
				tempPonId = node[0].ponId;
				tempPonIndex = node[0].id;
			}else{
				if(onuNoSelComboBox){
					onuNoSelComboBox.setValue(null);
				}
			}
		}
	});
	ponSelector.render({
		readyCallback : function(entityId, jsonData){
			var offline = true;
			$.each(jsonData, function(i,v){
				if(v.online){
					offline = false;
					ponSelector.setValue(v.id);
					return false;//相当于break
				}
			})
			if(offline){
				ponSelector.setValue(jsonData[1].id);
			}
		}
	});
	//end
	var $addDivBg = $("#addDivBg"),
	$addDiv = $("#addDiv");
	$("#authMode").text($("#modeSel :selected").text());
	syncMode($("#modeSel").val(),true);
	$addDivBg.css({display:"block"});
	$addDiv.css({display:"block"});
}
//取消
function closeAddLayer(){
	$("#addDivBg, #addDiv").fadeOut();
}

//重新加载onuNo,获取未配置过的onuNo列表
function reloadOnuNoList(ponId){
	// 已配置的onuNo
	var allocatedOnuNoList = [];
	var onuNoStore = new Ext.data.Store({
		url : '/gpon/onuauth/loadOnuNoList.tv?ponId=' + ponId,
		reader : new Ext.data.ArrayReader(
    		{id : 0}, 
    		Ext.data.Record.create([{name : 'onuNoList'}])
		)
	});
	onuNoStore.load({ 
		callback: function(records, options, success){
 			// 配置过的onuNo的列表
			if(records && records.length>0){
				$.each(records, function(i, v){allocatedOnuNoList.push(v.json);})
			}
			gponOnuNo = [];
			for(var i=1; i<=onuMaxNumInPon; i++){
				if(allocatedOnuNoList.indexOf(i) == -1){
					gponOnuNo.push({id:i,onuNo:i});
				}
			}
			if(onuNoSelStore){
				// 更新combobox的store
				onuNoSelStore.remove();
				onuNoSelStore.loadData(gponOnuNo);
			}
			if(onuNoSelComboBox){
				// 设置第一个为默认值
				onuNoSelComboBox.setValue(onuNoSelComboBox.getStore().getAt(0).data.onuNo);
			}
		}
	});
}

function cancelClick(){
   window.parent.closeWindow('gponOnuAuthen');
}

function addGponOnuAuth(){
	sumbitGponOnuAdd(entityId,tempPonIndex,tempPonId,CURRENT_AUTH_MODE,function(){
		authStore.reload();
		closeAddLayer();
	});
}
function reloadGridAll(){
	autoFindStore.reload();
	authStore.reload();
}

function showGponAutoAuth(){
	window.top.createDialog('gponAutoAuth',"@GPON/onuauth.autoAuthConfig@", 700, 500, '/gpon/onuauth/showGponAutoAuthConfig.tv?entityId=' + entityId, null, true, true);
}
