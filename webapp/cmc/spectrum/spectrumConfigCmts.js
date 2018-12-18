/********************************************/
/*--------------------入口-------------------*/
/********************************************/
var tbar;
var isHisStateChanging = false;
Ext.onReady(function(){
    buildDeviceTypeSelect();
	var w = $(window).width() - 30;
	$('#hisCollectCycle').val(hisCollectCycle);
	$('#hisCollectDuration').val(hisCollectDuration);
	$('#hisTimeInterval').val(hisTimeInterval);
	$('#timeInterval').val(timeInterval);
	$('#timeLimit').val(timeLimit);
	tbar = new Ext.Toolbar({
       items: [
       {xtype: 'tbspacer'},
       {xtype: 'button',text: '@spectrum.openCollectSwitch@', handler: cmtsCollectSwitchOn, iconCls:"bmenu_play", id: 'cmtsCollectSwitchOn', disabled: true},
       {xtype: 'button',text: '@spectrum.closeCollectSwitch@', handler: cmtsCollectSwitchOff, iconCls:"bmenu_stop", id: 'cmtsCollectSwitchOff', disabled: true},'-',
       {xtype: 'button',text: '@spectrum.openHisCollect@', handler: hisVideoSwitchOn, iconCls:"bmenu_play", id: 'hisVideoSwitchOn', disabled: true},
       {xtype: 'button',text: '@spectrum.closeHisCollect@', handler: hisVideoSwitchOff, iconCls:"bmenu_stop", id: 'hisVideoSwitchOff', disabled: true}
       ]
	});
	store = new Ext.data.JsonStore({
	    url: '/cmcSpectrum/loadSpectrumCmts.tv',
	    root: 'data', 
	    totalProperty: 'rowCount',
	    id:"cmcId", 
	    remoteSort: false,
	    fields: ['cmcId','cmtsName', 'cmtsIp','uplinkDevice', 'oltCollectSwitch', 'cmtsCollectSwitch', 'hisVideoSwitch','entityId']
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        }
	    }
	});
	if(eponSupport){
		var cm = new Ext.grid.ColumnModel([
			sm,	                       
	       	{header:'<div style="text-align:center">'+ '@COMMON.alias@' + '</div>',align:'center', dataIndex: 'cmtsName',  sortable: false},
	       	{header:'<div style="text-align:center">'+ '@resources/COMMON.uplinkDevice@' + '</div>', dataIndex: 'uplinkDevice', sortable: false,renderer:ipRenderer},
	       	{header:'<div style="text-align:center">'+ '@spectrum.oltSwitch@' + '</div>', dataIndex: 'oltCollectSwitch', sortable: false,renderer: renderOltSwitch},
	    	{header:'<div style="text-align:center">'+ '@spectrum.ccSwitch@' + '</div>', dataIndex: 'cmtsCollectSwitch', sortable: false,renderer: renderSpectrumSwitch},
	    	{header:'<div style="text-align:center">'+ '@spectrum.hisSwitch@' + '</div>', dataIndex: 'hisVideoSwitch', sortable: false,renderer: renderHisSwitch}
	    ]);
	}else{
		var cm = new Ext.grid.ColumnModel([
   			sm,	                       
   	       	{header:'<div style="text-align:center">'+ '@COMMON.alias@' + '</div>',align:'center', dataIndex: 'cmtsName',  sortable: false},
   	       	{header:'<div style="text-align:center">'+ '@resources/COMMON.uplinkDevice@' + '</div>', dataIndex: 'uplinkDevice', sortable: false,renderer:ipRenderer},
   	    	{header:'<div style="text-align:center">'+ '@spectrum.ccSwitch@' + '</div>', dataIndex: 'cmtsCollectSwitch', sortable: false,renderer: renderSpectrumSwitch},
   	    	{header:'<div style="text-align:center">'+ '@spectrum.hisSwitch@' + '</div>', dataIndex: 'hisVideoSwitch', sortable: false,renderer: renderHisSwitch}
   	    ]);
	}	
	var _height = $(window).height() - $('#grid').offset().top - 20;
	grid = new Ext.grid.GridPanel({
		border: true, 
		bodyCssClass:"normalTable",
        store: store, 
        cm: cm, 
        sm: sm,
        height:_height,
		renderTo: "grid",
		bbar: buildPagingToolBar(),
		tbar: tbar,
		viewConfig:{
			forceFit:true
		}
    });
	store.load({params:{start:0, limit: pageSize}});
	
	$(window).resize(function(){
		//重新计算grid的高度和宽度
		var _height = $('body').height() - $('#grid').offset().top - 20;
		var _width = $('body').width() - 20;
		grid.setSize(_width, _height);
	})
});

function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn("cmtsCollectSwitchOn", false);
        disabledBtn("cmtsCollectSwitchOff", false);
        disabledBtn("hisVideoSwitchOn", false);
        disabledBtn("hisVideoSwitchOff", false);
    }else{
        disabledBtn("cmtsCollectSwitchOn", true);
        disabledBtn("cmtsCollectSwitchOff", true);
        disabledBtn("hisVideoSwitchOn", true);
        disabledBtn("hisVideoSwitchOff", true);
    }
};
function disabledBtn(id, disabled){
    Ext.getCmp(id).setDisabled(disabled);
};

function buildDeviceTypeSelect() {
    for(var i = 0; i < cmcTypes.length; i ++){
        $("#typeId").append("<option value='"+cmcTypes[i].typeId+"'>"+cmcTypes[i].displayName+"</option>"); 
    }
}

/********************************************
*-----------IP转换,为空时显示横杠------------*
/********************************************/
function ipRenderer(value){
    if(value){
        return value;
    }else{
        return '-';
    }
}

/********************************************/
/*-----------开关的图形化展示On Off-----------*/
/********************************************/
function renderOltSwitch(value, p, record){
    if(operationDevicePower){
        if (value == '1') {
            return String.format('<img class="switch" onclick="oltCollectSwitchOff();"  src="/images/performance/on.png" border=0 align=absmiddle>',
                    record.data.entityId);  
        } else if(value == '-1') {
                return '-';
        } else {
            return String.format('<img class="switch" onclick="oltCollectSwitchOn();"  src="/images/performance/off.png"  border=0 align=absmiddle>',
                    record.data.entityId);  
        }
    }else{
        if (value == '1') {
            return String.format('<img class="switch" src="/images/performance/on.png" border=0 align=absmiddle>',
                    record.data.entityId);  
        } else if(value == '-1') {
                return '-';
        } else {
            return String.format('<img class="switch" src="/images/performance/off.png"  border=0 align=absmiddle>',
                    record.data.entityId);  
        }
    }
}
function renderSpectrumSwitch(value, p, record){
    if (value == '1') {
        return String.format('<img class="switch" onclick="cmtsCollectSwitchOff();" src="/images/performance/on.png" border=0 align=absmiddle>',
                record.data.entityId);  
    } else if(value == '-1') {
            return '-';
    } else {
        return String.format('<img class="switch" onclick="cmtsCollectSwitchOn();" src="/images/performance/off.png"  border=0 align=absmiddle>',
                record.data.entityId);  
    }
}
function renderHisSwitch(value, p, record){
    if (value == '1') {
        return String.format('<img class="switch" onclick="hisVideoSwitchOff();" src="/images/performance/on.png" border=0 align=absmiddle>',
                record.data.entityId);  
    } else if (value == '2') {
    	return String.format('<img class="switch" onclick="hisVideoSwitchOn();" src="/images/performance/off.png"  border=0 align=absmiddle>',
                record.data.entityId);   
    } else if (value == '3') {
        return '@spectrum.closing@';
    } else if (value == '4') {
        return '@spectrum.opening@';
    } else if (value == '5') {
        return String.format('<img class="switch" src="/images/performance/on2.png" border=0 align=absmiddle>',
                record.data.entityId);  
    } else if (value == '6') {
        return String.format('<img class="switch" src="/images/performance/off2.png" border=0 align=absmiddle>',
                record.data.entityId);  
    } else if (value == '-1') {
        return '-';
    } else {
        return String.format('<img class="switch" onclick="hisVideoSwitchOn();" src="/images/performance/off.png"  border=0 align=absmiddle>',
                record.data.entityId);  
    } 
}

/********************************************/
/*------------------分页展示栏---------------*/
/********************************************/
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({
        id: 'extPagingBar', 
        pageSize: pageSize,
        store:store,
        displayInfo: true, 
        cls:"extPagingBar",
        items: ["-", String.format('@COMMON.pagingTip@', pageSize), '-'],
        onFirstLayout : function(){//增加这个配置
            if(this.dsLoaded){
                this.onLoad.apply(this, this.dsLoaded);
            }
        }
    });
   return pagingToolbar;
}

/********************************************/
/*---------------开启CMTS频谱采集开关-------------*/
/********************************************/
function cmtsCollectSwitchOn(){
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var selections = sm.getSelections();
        var cmcIds = new Array();
        for(var i = 0; i < selections.length; i++){
            cmcIds[i] = selections[i].data.cmcId;
        }
        $.ajax({
            url:"/cmcSpectrum/startSpectrumSwitchCmts.tv",
            type:"post",
            data:{cmcIds: cmcIds},
            dataType:"text",
            success:function (message){
                if(message == "success"){
                	for(var i = 0 ; i <cmcIds.length; i ++){
                		var cmcId = cmcIds[i];
	                	for(var j = 0 ; j < store.getCount(); j++){
	                		if (store.getAt(j).get('cmcId') == cmcId) {
		                		var record = store.getAt(j);
		    		        	record.beginEdit();
		    		        	record.set('cmtsCollectSwitch', 1);
		    		        	record.commit();
		                	}
	                	}
                	}
                }else{
                    top.afterSaveOrDelete({
                        title: '@COMMON.tip@',
                            html: '<b class="orangeTxt">@spectrum.openCmtsSwitchFail@</b>'
                    });
                }
                //store.reload();
            },error: function(message) {
                top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                        html: '<b class="orangeTxt">@spectrum.openCmtsSwitchFail@</b>'
                });
            }, cache: false     
        });
    } else {
        top.afterSaveOrDelete({
            title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@spectrum.pleaseSelectDevice@</b>'
        });
    }
}
/********************************************/
/*---------------关闭CMTS频谱采集开关-------------*/
/********************************************/
function cmtsCollectSwitchOff(){
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var selections = sm.getSelections();
        var cmcIds = new Array();
        for(var i = 0; i < selections.length; i++){
            cmcIds[i] = selections[i].data.cmcId;
        }
        $.ajax({
            url:"/cmcSpectrum/stopSpectrumSwitchCmts.tv",
            type:"post",
            data:{cmcIds: cmcIds},
            dataType:"text",
            success:function (message){
                if(message == "success"){
                	for(var i = 0 ; i <cmcIds.length; i ++){
                		var cmcId = cmcIds[i];
	                	for(var j = 0 ; j < store.getCount(); j++){
	                		if (store.getAt(j).get('cmcId') == cmcId) {
		                		var record = store.getAt(j);
		    		        	record.beginEdit();
		    		        	record.set('cmtsCollectSwitch', 2);
		    		        	record.commit();
		                	}
	                	}
                	}
                }else{
                    top.afterSaveOrDelete({
                        title: '@COMMON.tip@',
                            html: '<b class="orangeTxt">@spectrum.closeCmtsSwitchFail@</b>'
                    });
                }
                //store.reload();
            },error: function(message) {
                top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                        html: '<b class="orangeTxt">@spectrum.closeCmtsSwitchFail@</b>'
                });
            }, cache: false     
        });
    } else {
        top.afterSaveOrDelete({
            title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@spectrum.pleaseSelectDevice@</b>'
        });
    }
}
/********************************************/
/*---------------开启历史频谱录像开关-------------*/
/********************************************/
function hisVideoSwitchOn(){
	var successCounter = 0;
    var sm = grid.getSelectionModel();
    var selections;
    if (sm != null && sm.hasSelection()) {
    	selections = sm.getSelections();
    	var oltSwitchOn = true;
    	for(var i = 0; i < selections.length; i++){
    		if(selections[i].data.oltCollectSwitch == 0){
    			oltSwitchOn = false;
    			break;
    		}
    	}
    	if(oltSwitchOn){
    		isHisStateChanging = true;
    		disableHisVideoSwitch();
    		tbar.getComponent("hisVideoSwitchOn").disable();
        	tbar.getComponent("hisVideoSwitchOff").disable();
	        var cmcIds = new Array();
	        for(var i = 0; i < selections.length; i++){
	            cmcIds[i] = selections[i].data.cmcId;
	            selections[i].beginEdit();
	            selections[i].set('hisVideoSwitch','4');
	            selections[i].commit();
	        }
	        $.ajax({
	            url:"/cmcSpectrum/startHisVideoSwitch.tv",
	            type:"post",
	            data:{cmcIds: cmcIds},
	            dataType:"text",
	            success:function (message){
	            },error: function(message) {
	            }, cache: false     
	        });
	        window.top.addCallback("turnOnHisVideoSwitchResult",function(cmtsSpectrumConfig) {
	            var cmcId = cmtsSpectrumConfig.cmcId;
	            var status = cmtsSpectrumConfig.hisVideoSwitch;
	            var record = store.getById(cmcId);
	            record.beginEdit();
	            record.set('hisVideoSwitch',status);
	            record.commit();
	            successCounter++;
	            if(successCounter >= selections.length){
	            	isHisStateChanging = false;
	            	tbar.getComponent("hisVideoSwitchOn").enable();
	            	tbar.getComponent("hisVideoSwitchOff").enable();
	            	enableHisVideoSwitch();
	            	store.reload();
	            }
	        }, "startHisVideoSwitch1024");
    	}else{
    		window.parent.showMessageDlg("@COMMON.tip@", "@spectrum.openOltFirst@");
    	}
    } else {
        top.afterSaveOrDelete({
            title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@spectrum.pleaseSelectDevice@</b>'
        });
    }
}

/********************************************/
/*---------------关闭历史频谱录像开关-------------*/
/********************************************/
function hisVideoSwitchOff(){
	disableHisVideoSwitch();
	var successCounter = 0;
    var sm = grid.getSelectionModel();
    var selections;
    if (sm != null && sm.hasSelection()) {
    	isHisStateChanging = true;
    	tbar.getComponent("hisVideoSwitchOn").disable();
    	tbar.getComponent("hisVideoSwitchOff").disable();
        selections = sm.getSelections();
        var cmcIds = new Array();
        for(var i = 0; i < selections.length; i++){
            cmcIds[i] = selections[i].data.cmcId;
            selections[i].beginEdit();
            selections[i].set('hisVideoSwitch','3');
            selections[i].commit();
        }
        $.ajax({
            url:"/cmcSpectrum/stopHisVideoSwitch.tv",
            type:"post",
            data:{cmcIds: cmcIds},
            dataType:"text",
            success:function (message){
            },error: function(message) {
            }, cache: false     
        });
    } else {
        top.afterSaveOrDelete({
            title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@spectrum.pleaseSelectDevice@</b>'
        });
    }
    window.top.addCallback("turnOffHisVideoSwitchResult",function(cmtsSpectrumConfig) {
        var cmcId = cmtsSpectrumConfig.cmcId;
        var status = cmtsSpectrumConfig.hisVideoSwitch;
        var record = store.getById(cmcId);
        record.beginEdit();
        record.set('hisVideoSwitch',status);
        record.commit();
        successCounter++;
        if(successCounter >= selections.length){
        	isHisStateChanging = false;
        	tbar.getComponent("hisVideoSwitchOn").enable();
        	tbar.getComponent("hisVideoSwitchOff").enable();
        	enableHisVideoSwitch();
        }
    }, "stopHisVideoSwitch1024");
}

/**
 * 1：可以点击的开启状态on
 * 2: 可以点击的关闭状态OFF
 * 3: 正在开启
 * 4：正在关闭
 * 5：无法点击的开启状态ON
 * 6：无法点击的关闭状态OFF
 */
function enableHisVideoSwitch(){
	for ( var i = 0; i < store.getCount(); i++) {
		if(store.getAt(i).get('hisVideoSwitch')=='5'){
			store.getAt(i).set('hisVideoSwitch','1');
		}else if(store.getAt(i).get('hisVideoSwitch')=='6'){
			store.getAt(i).set('hisVideoSwitch','2');
		}
		store.getAt(i).commit();
	}
}

function disableHisVideoSwitch(){
	for ( var i = 0; i < store.getCount(); i++) {
		if(store.getAt(i).get('hisVideoSwitch')=='1'){
			store.getAt(i).set('hisVideoSwitch','5');
		}else{
			store.getAt(i).set('hisVideoSwitch','6');
		}
		store.getAt(i).commit();
	}
}

function oltCollectSwitchOff(){
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var selections = sm.getSelections();
        var entityIds = [];
        for (var i = 0; i < selections.length; i++) {
            entityIds[i] = selections[i].data.entityId;
        }
        window.parent.showWaitingDlg("@COMMON.wait@","@spectrum.closingOltSwitch@" ,'waitingMsg','ext-mb-waiting');
        $.ajax({
            url:'/cmcSpectrum/stopSpectrumSwitchOlt.tv',
            data: {entityIds : entityIds},
            success:function(){
                window.parent.closeWaitingDlg();
                refresh();
            },error:function(){
                window.parent.closeWaitingDlg();
                top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                    html: '<b class="orangeTxt">@spectrum.closeOltCollectFail@</b>'
                });
            }
        });
    } else {
        top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@spectrum.pleaseSelectDevice@</b>'
        });
    }
}

function oltCollectSwitchOn(){
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var selections = sm.getSelections();
        var entityIds = [];
        for (var i = 0; i < selections.length; i++) {
            entityIds[i] = selections[i].data.entityId;
        }
        window.parent.showWaitingDlg("@COMMON.wait@","@spectrum.openingOltSwitch@" ,'waitingMsg','ext-mb-waiting');
        $.ajax({
            url:'/cmcSpectrum/startSpectrumSwitchOlt.tv',
            data: {entityIds : entityIds},
            success:function(){
                window.parent.closeWaitingDlg();
                refresh();
            },error:function(){
                window.parent.closeWaitingDlg();
                top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                    html: '<b class="orangeTxt">@spectrum.openOltCollectFail@</b>'
                });
            }
        });
    } else {
        top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@spectrum.pleaseSelectDevice@</b>'
        });
    }
}

function refresh(){
    store.reload();
}
/********************************************/
/*-------------------查询按钮----------------*/
/********************************************/
function queryCmtsSpectrumConfig(){
	if(isHisStateChanging){
		 top.afterSaveOrDelete({
			 title: '@COMMON.tip@',
	         html: '<b class="orangeTxt">@spectrum.waitHisChanged@</b>'
	     });
		 return;
	} 
    var typeId = $('#typeId').val();
    var cmtsName = $('#cmtsName').val();
    var cmtsIp = $('#cmtsIp').val();
    var oltCollectSwitch = $('#oltCollectSwitch').val();
    var cmtsCollectSwitch = $('#cmtsCollectSwitch').val();
    var hisVideoSwitch = $('#hisVideoSwitch').val();
    store.baseParams={
        start:0, 
        limit: pageSize,
        typeId: typeId,
        cmtsName: cmtsName,
        cmtsIp: cmtsIp,
        oltCollectSwitch: oltCollectSwitch,
        cmtsCollectSwitch: cmtsCollectSwitch,
        hisVideoSwitch: hisVideoSwitch
     };
    store.load();
}

/********************************************/
/*---------------判断是否为纯数字-------------*/
/********************************************/
function isDigit(s){ 
    var patrn=/^[0-9]{1,20}$/; 
    if(!patrn.exec(s)){
        return false; 
    }else{
        return true;
    }
}

/********************************************/
/*-------------保存历史频谱采集配置------------*/
/********************************************/
function saveHisCollectConfig(){
    if(!isDigit($("#hisCollectCycle").val()) || parseInt($("#hisCollectCycle").val()) < 1 || parseInt($("#hisCollectCycle").val()) > 24){
        $("#hisCollectCycle").focus();
        return;
    }
    if(!isDigit($("#hisCollectDuration").val()) || parseInt($("#hisCollectDuration").val()) < 1 || parseInt($("#hisCollectDuration").val()) > 55){
        $("#hisCollectDuration").focus();
        return;
    } 
    var hisCollectCycle = $("#hisCollectCycle").val();
    var hisCollectDuration = $("#hisCollectDuration").val();
    var hisTimeInterval = $("#hisTimeInterval").val();
    $.ajax({
        url: '/cmcSpectrum/modifyHisSpectrum.tv?hisCollectCycle='+hisCollectCycle+'&hisCollectDuration='+hisCollectDuration+'&hisTimeInterval='+hisTimeInterval,
        type: 'post',
        success: function(response) {
            if(response == "success"){
                top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                    html: '@spectrum.saveHisConfigSuccess@'
                });
            }else{
                top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                    html: '@spectrum.saveHisConfigFail@'
                });
            }
        }, 
        error: function(response) {
            top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '@spectrum.saveHisConfigFail@'
            });
        }, 
        cache: false
    });
}

/********************************************/
/*-------------重置历史频谱采集配置------------*/
/********************************************/
function resetHisCollectConfig(){
	$.ajax({
        url:"/cmcSpectrum/loadHisConfig.tv",
        type:"post",
        success:function (result){
        	$('#hisCollectCycle').val(result.hisCollectCycle);
        	$('#hisCollectDuration').val(result.hisCollectDuration);
        	$('#hisTimeInterval').val(result.hisTimeInterval);
        },
        error: function(message) {
            top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@COMMON.resetFail@</b>'
            });
        }, 
        cache: false     
    });
}

/********************************************/
/*-----------保存实时频谱采集配置----------*/
/********************************************/
function saveGlobalCollectConfig(){
    if(!isDigit($("#timeLimit").val()) || parseInt($("#timeLimit").val()) < 15 || parseInt($("#timeLimit").val()) > 60){
        $("#timeLimit").focus();
        return;
    } 
    var timeInterval = $("#timeInterval").val();
    var timeLimit = $("#timeLimit").val();
    $.ajax({
        url: '/cmcSpectrum/modifyTimeStrategy.tv?timeInterval='+timeInterval+'&timeLimit='+timeLimit,
        type: 'post',
        success: function(response) {
            if(response == "success"){
                top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                    html: '@spectrum.saveRealtimeConfigSuccess@'
                });
            }else{
                top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                    html: '@spectrum.saveRealtimeConfigFail@'
                });
            }
        }, 
        error: function(response) {
            top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '@spectrum.saveRealtimeConfigFail@'
            });
        }, 
        cache: false
    });
}

/********************************************/
/*-------------重置实时频谱采集配置------------*/
/********************************************/
function resetGlobalCollectConfig(){
	$.ajax({
        url:"/cmcSpectrum/loadGlobalConfig.tv",
        type:"post",
        success:function (result){
        	$('#timeInterval').val(result.timeInterval);
        	$('#timeLimit').val(result.timeLimit);
        },
        error: function(message) {
            top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@COMMON.resetFail@</b>'
            });
        }, 
        cache: false     
    });
}

/********************************************
*--------------设备类型被选择之后------------
/********************************************/
function deviceTypeSelChanged() {
	var deviceTypeId = $('#typeId').val();
	$('#oltCollectSwitch').val(-1);
	$('#cmtsCollectSwitch').val(-1);
	$('#hisVideoSwitch').val(-1);
	
	if (deviceTypeId == 30001 || deviceTypeId == 30005) {
		//如果是整合型的CCMTS
		$('#td_olt_switch').css({
			display:"block"
		});
		$('#oltCollectSwitch').attr("disabled", false).css({display:"block"});
	} else {
		//如果是独立型的CCMTS或者未选择设备类型
		$('#td_olt_switch').css({
			display:"none"
		});
		$('#oltCollectSwitch').attr("disabled", "disabled").css({display:"none"});
	}
}

/********************************************/
/*-----CheckboxSelectionModel override begin*/
/********************************************/
Ext.override(Ext.grid.GridView,{  
    
    onRowSelect : function(row){  
        this.addRowClass(row, "x-grid3-row-selected");  
        var selected = 0;  
        var len = this.grid.store.getCount();  
        for(var i = 0; i < len; i++){  
            var r = this.getRow(i);  
            if(r){  
               if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
            }  
        }  
          
        var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
          
        if (selected == len && !hd.hasClass('x-grid3-hd-checker-on')) {  
             hd.addClass('x-grid3-hd-checker-on');   
        }  
    },  
  
    onRowDeselect : function(row){  
        this.removeRowClass(row, "x-grid3-row-selected");  
            var selected = 0;  
            var len = this.grid.store.getCount();  
            for(var i = 0; i < len; i++){  
                var r = this.getRow(i);  
                if(r){  
                   if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
                }  
            }  
            var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
              
            if (hd != null && selected != len && hd.hasClass('x-grid3-hd-checker-on')) {  
                 hd.removeClass('x-grid3-hd-checker-on');   
            }  
    }  
});  
Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
    onHdMouseDown : onHdMouseDown
}); 
function onMonuseDown(e, t){
    if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
        e.stopEvent();
        var row = e.getTarget('.x-grid3-row');
        // mouseHandled flag check for a duplicate selection (handleMouseDown) call
        if(!this.mouseHandled && row){
            //alert(this.grid.store.getCount());
            var gridEl = this.grid.getEl();//得到表格的EL对象
            var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
            var index = row.rowIndex;
            if(this.isSelected(index)){
                this.deselectRow(index);
                var isChecked = hd.hasClass('x-grid3-hd-checker-on');
                //判断头部的全选CheckBox框是否选中，如果是，则删除
                if(isChecked){
                    hd.removeClass('x-grid3-hd-checker-on');
                }
            }else{
                this.selectRow(index, true);
                //判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
                if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
                    hd.addClass('x-grid3-hd-checker-on');
                };
            }
        }
    }
    this.mouseHandled = false;
}
function onHdMouseDown(e, t){
    /**
    *大家觉得上面重写的代码应该已经实现了这个功能了，可是又发现下面这行也重写了
    *由原来的t.className修改为t.className.split(' ')[0]
    *为什么呢？这个是我在快速点击头部全选CheckBox时，
    *操作过程发现，有的时候x-grid3-hd-checker-on这个样式还没有删除或者多一个空格，结果导致下面这个判断不成立
    *去全选或者全选不能实现
    */
    if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
        e.stopEvent();
        var hd = Ext.fly(t.parentNode);
        var isChecked = hd.hasClass('x-grid3-hd-checker-on');
        if(isChecked){
            hd.removeClass('x-grid3-hd-checker-on');
            this.clearSelections();
        }else{
            hd.addClass('x-grid3-hd-checker-on');
            this.selectAll();
        }
    }
}
/********************************************/
/*CheckboxSelectionModel override end-------*/
/********************************************/