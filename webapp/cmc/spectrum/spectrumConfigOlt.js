Ext.onReady(function(){
    bulidSwitchGrid();
    new Ext.Viewport({
        layout: 'border',
        items: [{
            region: 'north',
            contentEl: 'queryDiv',
            height: 50,
            border: false
        },grid]
    });
});
function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn("refreshSwitch", false);
        if(operationDevicePower){
        	disabledBtn("oltCollectSwitchOn", false);
        }
        if(operationDevicePower){
        	disabledBtn("oltCollectSwitchOff", false);
        }
    }else{
        disabledBtn("refreshSwitch", true);
        if(operationDevicePower){
        	disabledBtn("oltCollectSwitchOn", true);
        }
        if(operationDevicePower){
        	disabledBtn("oltCollectSwitchOff", true);
        }
    }
};
function disabledBtn(id, disabled){
    Ext.getCmp(id).setDisabled(disabled);
};

function bulidSwitchGrid(){
    var w = $(window).width() - 30;
    
    var tbar = [
        {xtype: 'tbspacer', width:5},        
        {xtype: 'button',text: '@spectrum.batchOpen@', width: 20, iconCls:"bmenu_play", id: 'oltCollectSwitchOn',handler: oltCollectSwitchOn,disabled:!operationDevicePower, disabled: true},
        '-',
        {xtype: 'button',text: '@spectrum.batchClose@', width: 20, iconCls:"bmenu_stop", id: 'oltCollectSwitchOff',handler: oltCollectSwitchOff,disabled:!operationDevicePower, disabled: true},
        '-',
        {xtype: 'button',text: '@spectrum.batchRefreshSwitch@', width: 20, iconCls:"bmenu_refresh", id: 'refreshSwitch',handler: refreshSwitch, disabled:true}
    ];
    store = new Ext.data.JsonStore({
        url: '/cmcSpectrum/loadSpectrumOlt.tv',
        root: 'data', 
        totalProperty: 'rowCount',
        remoteSort: false,
        fields: ['entityId','oltName', 'entityIp', 'state' , 'collectSwitch']
    });
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
    var cm = new Ext.grid.ColumnModel([
        sm,                        
        {header:'<div style="text-align:center">'+ '@COMMON.alias@' + '</div>', width: w/5, dataIndex: 'oltName', sortable: false},
        {header:'<div style="text-align:center">'+ '@spectrum.manageIp@' + '</div>', width: w/5, dataIndex: 'entityIp', sortable: false},
        {header:'<div style="text-align:center">'+ '@spectrum.onlineStatus@' + '</div>',width: w/5, dataIndex: 'state',sortable: false,renderer : renderSysStatus},
        {header:'<div style="text-align:center">'+ '@spectrum.collectSwitch@' + '</div>', width: w/5, dataIndex: 'collectSwitch',  sortable: false,renderer : renderSwitch},
        {header:'<div style="text-align:center">'+ '@COMMON.manu@' + '</div>', width: w/5,dataIndex: 'op',sortable: false,renderer : manuRender}
    ]);
    grid = new Ext.grid.GridPanel({
        border: true, 
        bodyCssClass:"normalTable",
        store: store, 
        cm: cm, 
        sm: sm,
        region: 'center',
        viewConfig: {
            forceFit: true
        },
        bbar: buildPagingToolBar(),
        tbar: tbar
    });
    store.baseParams={
            start:0, 
            limit: pageSize,
            entityIp : entityIp
    };
    store.load();
}
function manuRender(v,m,r){
    if(operationDevicePower){
        if(r.data.collectSwitch == 1){
            return String.format("<a href='javascript:;' onClick='oltCollectSwitchOff()'>@spectrum.closeCollect@</a>/<a href='javascript:;' onClick='refreshSwitch()'>@spectrum.refreshSwitch@</a>");
        }else if(r.data.collectSwitch == 0){
            return String.format("<a href='javascript:;' onClick='oltCollectSwitchOn()'>@spectrum.openCollect@</a>/<a href='javascript:;' onClick='refreshSwitch()'>@spectrum.refreshSwitch@</a>");
        }else{
            return "";
        }
    }else{
        if(r.data.collectSwitch == 1){
            return String.format("@spectrum.closeCollect@/<a href='javascript:;' onClick='refreshSwitch()'>@spectrum.refreshSwitch@</a>");
        }else if(r.data.collectSwitch == 0){
            return String.format("@spectrum.openCollect@/<a href='javascript:;' onClick='refreshSwitch()'>@spectrum.refreshSwitch@</a>");
        }else{
            return "";
        }
    }
}
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({
        id: 'extPagingBar', 
        pageSize: pageSize,
        store:store,
        displayInfo: true, 
        items: ["-", String.format('@COMMON.pagingTip@', pageSize), '-'],
        listeners : {
        	beforechange : function(){
        		disabledToolbarBtn(0);
        	}
        }
    });
   return pagingToolbar;
}

function query(){
    store.baseParams={
        start:0, 
        limit: pageSize,
        oltName : $("#oltName").val(),
        entityIp : $("#entityIp").val(),
        oltCollectSwitch : $("#switch").val()
    };
    store.load({
    	callback : function(){
    		
    	}
    });
}

function refresh(){
    store.reload();
}

function renderSysStatus(value, p, record) {
    if (record.data.state == '1') {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
                "@COMMON.online@");
    } else {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
                "@COMMON.offline@");
    }
}

function renderSwitch(value, p, record){
    if(operationDevicePower){
        if (value == '1') {
            return String.format('<img class="switch" onclick="oltCollectSwitchOff()" src="/images/performance/on.png" border=0 align=absmiddle>',
                    record.data.entityId);  
        } else if (value == '2') {
        	return String.format('<img class="switch" onclick="oltCollectSwitchOn()" src="/images/performance/off.png"  border=0 align=absmiddle>',
                    record.data.entityId);  
        } else if (value == '3') {
            return '@spectrum.closing@';
        } else if (value == '4') {
            return '@spectrum.opening@';
        } else if(value == '-1') {
                return '-';
        } else {
            return String.format('<img class="switch" onclick="oltCollectSwitchOn()" src="/images/performance/off.png"  border=0 align=absmiddle>',
                    record.data.entityId);  
        }
    }else{
        if (value == '1') {
            return String.format('<img class="switch" src="/images/performance/on.png" border=0 align=absmiddle>',
                    record.data.entityId);  
        } else if (value == '2') {
        	return String.format('<img class="switch" src="/images/performance/off.png"  border=0 align=absmiddle>',
                    record.data.entityId);  
        } else if (value == '3') {
            return '@spectrum.closing@';
        } else if (value == '4') {
            return '@spectrum.opening@';
        } else if(value == '-1') {
                return '-';
        } else {
            return String.format('<img class="switch" src="/images/performance/off.png"  border=0 align=absmiddle>',
                    record.data.entityId);  
        }
    }
}

function oltCollectSwitchOn(){
	var dwrId = '957';
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var selections = sm.getSelections();
        var entityIds = [];
        getDwrResult(dwrId);
        for (var i = 0; i < selections.length; i++) {
            entityIds[0] = selections[i].data.entityId;
            selections[i].beginEdit();
	        selections[i].set('collectSwitch','4');
	        selections[i].commit();
	        $.ajax({
	            url:'/cmcSpectrum/startSpectrumSwitchOlt.tv',
	            data: {entityIds : entityIds ,
	            	   dwrId : dwrId},
	            success:function(){
	                //window.parent.closeWaitingDlg();
	                //refresh();
	            },error:function(){
	                window.parent.closeWaitingDlg();
	                top.afterSaveOrDelete({
	                    title: '@COMMON.tip@',
	                    html: '<b class="orangeTxt">@spectrum.openOltCollectFail@</b>'
	                });
	            }
	        });
        }
    } else {
        top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@spectrum.pleaseSelectDevice@</b>'
        });
    }
}
function oltCollectSwitchOff(){
	var dwrId = '958';
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var selections = sm.getSelections();
        var entityIds = [];
        getDwrResult(dwrId);
        for (var i = 0; i < selections.length; i++) {
            entityIds[0] = selections[i].data.entityId;
            selections[i].beginEdit();
	        selections[i].set('collectSwitch','3');
	        selections[i].commit();
	        $.ajax({
	            url:'/cmcSpectrum/stopSpectrumSwitchOlt.tv',
	            data: {entityIds : entityIds,
	            	   dwrId : dwrId},
	            success:function(){
	                //window.parent.closeWaitingDlg();
	                //refresh();
	            },error:function(){
	                window.parent.closeWaitingDlg();
	                top.afterSaveOrDelete({
	                    title: '@COMMON.tip@',
	                    html: '<b class="orangeTxt">@spectrum.closeOltCollectFail@</b>'
	                });
	            }
	        });
        }
    } else {
        top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@spectrum.pleaseSelectDevice@</b>'
        });
    }
}
function refreshSwitch(){
	var dwrId = '959';
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var selections = sm.getSelections();
        var entityIds = [];
        getDwrResult(dwrId);
        for (var i = 0; i < selections.length; i++) {
            entityIds[0] = selections[i].data.entityId;
	        $.ajax({
	            url:'/cmcSpectrum/refreshSpectrumSwitchOlt.tv',
	            data: {entityIds : entityIds,
	            	   dwrId : dwrId},
	            success:function(){
	                //window.parent.closeWaitingDlg();
	                //refresh();
	            },error:function(){
	                window.parent.closeWaitingDlg();
	                top.afterSaveOrDelete({
	                    title: '@COMMON.tip@',
	                    html: '<b class="orangeTxt">@spectrum.refreshOltSwitchFail@</b>'
	                });
	            }
	        });
        }
    } else {
        top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@spectrum.pleaseSelectDevice@</b>'
        });
    }
}
function getDwrResult(dwrId){
	 /*DWR推送*/
    window.top.addCallback("pushSpectrumSwitchOltMessage",function(spectrumOltSwitch) {
    	var entityId = spectrumOltSwitch.entityId;
        var collectSwitch = spectrumOltSwitch.collectSwitch;
        for (var j = 0; j < store.getCount(); j++) {
            if (store.getAt(j).get('entityId') == entityId) {
            	store.getAt(j).set('collectSwitch',collectSwitch);
            	store.getAt(j).commit();
            }
        }
    }, dwrId);
}