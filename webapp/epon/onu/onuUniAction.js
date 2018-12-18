//UNI VLAN模板绑定信息
function uniVlanBindInfo(){
	window.top.createDialog('uniVlanBindInfo', "@ONU.uniVlanInfo@", 600, 370, 'epon/univlanprofile/showUniBindInfo.tv?uniId=' +WIN.uniId + '&entityId=' + entityId+"&onuId="+onuId, null, true, true);
}

function uniSpeedDecHandler() {
    window.top.createDialog('uniRateLimit', "@ONU.uniSpeedDecMgmt@", 600, 370, '/onu/showUniRateLimit.tv?uniId=' + WIN.uniId+ '&entityId=' + entityId, null, true, true);
}

function uniSpeedDecHandlerBak() {
    window.top.createDialog('uniRateLimit', "@ONU.uniSpeedDecMgmt@", 600, 370, '/onu/showUniRateLimitBak.tv?uniId=' + WIN.uniId+ '&entityId=' + entityId, null, true, true);
}

function workModeHandler() {
    window.top.createDialog('uniAutoNegotiation', 'UNI@ONU.workMode@', 600, 410, '/onu/showUniAutoNegoConfig.tv?&uniId=' + WIN.uniId + '&entityId=' + entityId , null, true, true);
}

function macLearnHandler() {
    window.top.createDialog('uniMacAddrConfig', "@ONU.macMaxLearn@", 600, 250, '/onu/showUniMacAddrCap.tv?uniId=' + WIN.uniId + '&entityId=' + entityId, null, true, true);
}
function uniPortVlan(){
	window.top.createDialog('uniPortVlan', "@ONU.uniVlanInfo@", 800, 500, '/epon/uniportvlan/showUniVlanView.tv?uniId=' +uniId + '&entityId=' + entityId, null, true, true);
}
function macCleanHandler() {
    window.parent.showConfirmDlg("@COMMON.tip@", "@ONU.confirmMacLean@", function(type) {
        if (type == 'no') return
        window.top.showWaitingDlg("@COMMON.wait@", "@ONU.macCleaning@" , 'ext-mb-waiting');
        $.ajax({
            url: '/onu/onuMacClear.tv?',cache:false,
            data: {entityId : entityId, uniId : WIN.uniId},
            success: function(response) {
                window.parent.closeWaitingDlg();
                var text = response;
                if(text == 'success'){
                	top.afterSaveOrDelete({
          				title: "@COMMON.tip@",
          				html: "@ONU.macCleanOk@"
          			});
                }
                else{
                    window.parent.showMessageDlg("@COMMON.tip@", "@ONU.macCleanError@");
                }
            },
            error: function(){
                window.parent.showMessageDlg("@COMMON.tip@", "@ONU.macCleanError@");
            }
        });
    });          
}
function renderOpen(value, p, record){
	if (value == 1) {
		return '<img nm3kTip="@COMMON.open@" class="nm3kTip" src="../images/fault/trap_on.png" border=0 align=absmiddle>';
	} else {
		return '<img nm3kTip="@COMMON.close@" class="nm3kTip" src="../images/fault/trap_off.png" border=0 align=absmiddle>';
	}
}

function manuRender(v,m,r){
	return String.format("<a href='javascript:;' class='withSub'  onClick='showMoreOperation({0},event)'>@COMMON.other@</a>",r.id);
}

function showMoreOperation(rid,event){
	var record = grid.getStore().getById(rid); 
	WIN.uniId = record.data.uniId;
	WIN.uniIndex = record.data.uniIndex;
	grid.getSelectionModel().selectRecords([record]);
	var items = [];
	items[items.length] = {id:'uniRate', text: "@ONU.uniSpeedDec@",  handler :uniSpeedDecHandler};
	items[items.length] = {id:'uniRateBak', text: "@ONU.uniSpeedDec@",  handler :uniSpeedDecHandlerBak};
    items[items.length] = {id:'uniAutoNegotiation', text: "@ONU.workMode@",  handler :workModeHandler};
    items[items.length] = {id:'uniMacAddrConfig', text: "@ONU.macLearn@",handler: macLearnHandler};
    //items[items.length] = {id:'uniMacClear', text: "@ONU.macClean@",disabled:!operationDevicePower,handler: macCleanHandler,disabled:!operationDevicePower };
    items[items.length] = {id:'uniVlanBindInfo', text: "@ONU.uniVlanInfo@",handler: uniVlanBindInfo};
    items[items.length] = {id:'uniPortVlan', text: "@ONU.uniVlanInfo@",handler: uniPortVlan};
	if (!serviceMenu) {
		serviceMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
	} else {
		serviceMenu.removeAll();
		serviceMenu.add(items)
	}
	serviceMenu.showAt([event.clientX, event.clientY] );
}


function renderOnline(value, p, record){
	if (value == 1) {
		return 'UP';
	} else {
		return 'DOWN';
	}
}



function renderPerf(value, p, record){
	if (value == 1) {
		return String.format('<img class="switch" title="@COMMON.open@" src="/images/performance/on.png" onclick="openPerf({0},{1},2)" border=0 align=absmiddle>',
				record.data.uniId,record.data.entityId);	
	} else {
		return String.format('<img class="switch" title="@COMMON.close@" src="/images/performance/off.png" onclick="openPerf({0},{1},1)" border=0 align=absmiddle>',
				record.data.uniId,record.data.entityId);	
	}
}

function renderPortAdmin(value, p, record){
	if (value == 1) {
		return String.format('<img class="switch" title="@COMMON.open@" src="/images/performance/on.png" onclick="openPortAdmin({0},{1},2)" border=0 align=absmiddle>',
				record.data.uniId,record.data.entityId);	
	} else {
		return String.format('<img class="switch" title="@COMMON.close@" src="/images/performance/off.png" onclick="openPortAdmin({0},{1},1)" border=0 align=absmiddle>',
				record.data.uniId,record.data.entityId);	
	}
}

function renderDuplexRate(value, p, record){
	return GPON_ONU.codeDuplex[value];
}

function renderVlanType(v){
	switch(v || 1){
	case 0:return "@univlanprofile/PROFILE.modeNone@";
	case 1:return "@univlanprofile/PROFILE.modeTransparent@";
	case 2:return "@univlanprofile/PROFILE.modeTag@";
	case 3:return "@univlanprofile/PROFILE.modeTranslate@";
	case 4:return "@univlanprofile/PROFILE.modeAgg@";
	case 5:return "@univlanprofile/PROFILE.modeTrunk@";
	}
}

function openPortAdmin(uniId,entityId,uniAdminStatus) {
	if(!operationDevicePower)return;
    var action =  uniAdminStatus == 1 ? "@COMMON.open@" : "@COMMON.close@";
    window.parent.showConfirmDlg("@COMMON.tip@",  String.format( "@SUPPLY.cfmActionPortEn@" , action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg("@COMMON.wait@", String.format( "@SUPPLY.settingPortEn@", action), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setOnuUniAdminStatus.tv',
            data: "uniId=" + uniId + "&entityId=" + entityId + "&uniAdminStatus=" + uniAdminStatus,
            success: function(json) {
            	window.parent.closeWaitingDlg();
                if (json.message) {
                    window.parent.showMessageDlg("@COMMON.tip@", json.message);
                } else {
                	store.reload();
                }
            },
            error: function() {
                window.parent.showMessageDlg("@COMMON.tip@", action + "@EPON.portEnableError@")
            },cache: false
        });
    });
}
function refreshUniInfo(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@");
	$.ajax({
		url:"/onu/refreshOnuUniInfo.tv",cache:false,
		data:{
			onuId:onuId
		},success:function(){
			window.top.closeWaitingDlg();
			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
   			});
			store.reload();
		},
        error: function() {
            window.parent.showMessageDlg("@COMMON.tip@", "@EPON.dataError@!")
        }
	})
}

function reload(){
	store.reload();
}

function openPerf(uniId,entityId,perfStats15minuteEnable) {
	if(!operationDevicePower)return;
    var action =  perfStats15minuteEnable == 1 ? "@COMMON.open@" : "@COMMON.close@";
    window.parent.showConfirmDlg("@COMMON.tip@", String.format("@SUPPLY.cfmStasticEn@", action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg("@COMMON.wait@", String.format("@SUPPLY.settingStasticEn@" , action), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setUni15MinPerfStatus.tv',
            data: "entityId=" + entityId+"&uniId="+uniId+"&uni15minEnable="+perfStats15minuteEnable,
            success: function() {
            	window.parent.closeWaitingDlg();
            	store.reload();
            }, error: function() {
                window.parent.showMessageDlg("@COMMON.tip@", action + "@EPON.stasticError@")
        	}, cache: false
        });
    });
}