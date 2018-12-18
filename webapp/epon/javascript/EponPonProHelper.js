function showPonProService(group,e){
    var items = []
    if(group.topPonPsGrpAdmin == 1)
        items[items.length] = {text: I18N.ponprotect.disable ,handler: setPPGDisable,disabled:!operationDevicePower}
    else
        items[items.length] = {text: I18N.ponprotect.enable ,handler: setPPGEnable,disabled:!operationDevicePower}
    //只有在保护组处于去使能状态下时才能被删除
    if(group.topPonPsGrpAdmin == 2)
        items[items.length] = {text: I18N.ponprotect.del , handler: deletePPG,disabled:!operationDevicePower}
    //保护组需要在激活的情况下才能倒换
    if(group.topPonPsGrpAdmin == 1)
        items[items.length] = {text: I18N.ponprotect.switchppg ,handler : manuSwitch,disabled:!operationDevicePower}
    items[items.length] = {text: I18N.ponprotect.resume ,disabled : true}
    displayService(items,e);
}

function showMasterService(e){
    var items = []
    items[items.length] = {text: I18N.ponprotect.setMaster , handler: removeMembers}
    displayService(items,e);
}

/**
 * 新建一个Trunk组
 */
function createPonProtect(){
    window.top.createDialog('createPonProtect', I18N.ponprotect.newPPG , 600, 260, '/epon/ponprotect/createPPG.tv?entityId=' + entityId, null, true, true);  
}

/*
 * 还原页面RAW样式
 */
function cleanPorts(){
	 $("div, .BoardClass, #OLT").unbind("click mouseover mouseout contextmenu");
     //移除状态1的样式
     for (var i=0; i < olt.slotList.length; i++) {
        for (var j=0; j < olt.slotList[i].portList.length; j++) {
        var targetDiv = IndexToId(olt.slotList[i].portList[j].portIndex);
            switch(olt.slotList[i].portList[j].portSubType){
                case 'geCopper':
                    $("#"+targetDiv).css({
                        backgroundImage:'url(/epon/image/geCopper/geCopper.png)'
                    });
                    break;
                case 'geFiber':
                    $("#"+targetDiv).css('backgroundImage' , 'url(/epon/image/geFiber/geFiber.png)')
                    break;
                case 'xeFiber':
                    $("#"+targetDiv).css('backgroundImage' , 'url(/epon/image/xeFiber/xeFiber.png)')
                        break;
                //TRUNK组中不包含PON口，故统一处理
                case 'geEpon':
                case 'tengeEpon':
                case 'gpon':
                    $("#"+targetDiv).css({
                        backgroundImage:'url(/epon/image/geEpon/geEpon.png)'
                    });
                    break;
            }
        }
    }
}

function renderStatus(v,c,r){
	if (v ==1) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_on.png" border=0 align=absmiddle>',
			"@STATUS.enabled@");	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_off.png" border=0 align=absmiddle>',
			"@STATUS.disabled@");	
	}
}

/**
 * 构造TRUNK组Grid & 属性表Grid
 */
function bulidPPGGrid(){
    PPG_CM = new Ext.grid.ColumnModel([
        {header: I18N.ponprotect.ppgId ,dataIndex:'topPonPSGrpIndex',width:75, align: 'center',sortable: false,      resizable: true,menuDisabled :true},
        {header: "@COMMON.status@" ,dataIndex:'topPonPsGrpAdmin', renderer : renderStatus, width: 100, align: 'center',sortable: false,resizable: true,menuDisabled :true}
    ])
    PPG_Store = new Ext.data.JsonStore({
        data: PPGList.data,
        fields:["topPonPSGrpIndex","alias","topPonPsGrpAdmin"]
    })
    PPG_Grid = new Ext.grid.GridPanel({
        stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
		viewConfig:{forceFit: true},
        height: $("#viewLeftPartBody").height(),
        renderTo: "viewLeftPartBody", 
        id : "PPG_Grid",store : PPG_Store,
        cm : PPG_CM, 
        bbar: new Ext.Toolbar({
            items :[{ 
                text : I18N.ponprotect.newPPG, width : 172,handler: createPonProtect, disabled:!operationDevicePower
            }]
        }),
        listeners:{
        	viewready:function(){
        		this.getSelectionModel().selectFirstRow();
        	}
        }
    })
    
    PPG_Grid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
        selectedIndex = rowIndex;
        var group = PPGList.data[rowIndex];
        showPonProService(group, e);
        onGroupClick(grid, rowIndex, e);
    });
     
    PPG_Grid.on('rowcontextmenu', function(PPG_Grid, rowIndex, e) {
    	selectedIndex = rowIndex;
        preventBubble(e);
        e.preventDefault();
        var sm = PPG_Grid.getSelectionModel();
        if (sm != null && !sm.isSelected(rowIndex)) {
            sm.selectRow(rowIndex);
        }
        var group = PPGList.data[rowIndex];
        showPonProService(group, e);
    });
    
    Ext.grid.PropertyGrid.prototype.setSource = function(source) {
        delete this.propStore.store.sortInfo;
        this.propStore.setSource(source);
    }; 
    
    Attr_Grid =new Ext.grid.PropertyGrid({
        renderTo: 'viewRightPartBottomBody',
        autoHeight: true,
        border: false,
        hideHeaders:true,
        autoScroll: true,
        source: Attr_Store,
        selModel : new Ext.grid.RowSelectionModel({
            singleSelect : true,
            listeners:{
                'beforerowselect' : function(){                 
                    return false;
                }
            }  
        }),  
        listeners:{
            'contextmenu' : function(e){
                e.preventDefault();
            },
            'rowdblclick':function(g,r,e ){
            	//-------双击时默认复制行内容 
                var thisText = e.getTarget().innerHTML;
                //-----属性值可能是number型，故强转string
                clipboardData.setData("text",thisText);
                top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: I18N.EPON.copyOk + thisText
       			});
                //window.parent.showMessageDlg( I18N.COMMON.tip, I18N.EPON.copyOk + thisText );   
            },
            'render': function(proGrid) {
                var view = proGrid.getView();
                var store = proGrid.getStore();
                proGrid.tip = new Ext.ToolTip({
                    target: view.mainBody,
                    delegate: '.x-grid3-row',
                    trackMouse: true,
                    renderTo: document.body,
                    listeners: {
                        beforeshow: function updateTipBody(tip) {
                            var rowIndex = view.findRowIndex(tip.triggerElement);
                            tip.body.dom.innerHTML = store.getAt(rowIndex).get('value');
                        }
                    }
                });
            }                               
        }
    });

    //表格不可编辑
    Attr_Grid.on("beforeedit", function(e) {
        e.cancel = true;
        return false;
    });
}


//-- TRUNK操作区 --//

/**
 * trunkGrid行点击事件处理，显示当前组包含的端口，以及能加入到该trunk组的端口
 */
function onGroupClick(grid, rowIndex,e) {
    var arr = $(".portClass")
    for(var i=0;i<arr.length;i++){
        /*得到所有端口**/
        var id = arr[i].id
        /**解析成端口类型*/
        var portType = getPortType(id)
        //**清空样式
        $('#'+id).css({
            backgroundImage : 'url(/epon/image/'+portType+'/'+portType+'.png)'
        })
    }
    var idx = getPPGListIndex(PPG_Grid.getStore().getAt(rowIndex).data.topPonPSGrpIndex)
    var group = PPGList.data[idx]
    //展示该组
    showPPG(group);
    TrunkSelected = rowIndex;
    showProperty(group);
}


function showProperty(group){
    var activePort
    var source;
    if(group.topPonPsWorkPortStatus == 1){
        activePort = group.topPonPSWorkPortIndex
    }else if(group.topPonPsStandbyPortStatus == 1){
        activePort = group.topPonPSStandbyPortIndex
    }else{
        //无激活端口
    }
    //只有当组使能的状态下才有必要显示倒换次数，倒换时间等信息
    if(group.topPonPsGrpAdmin == 1){
        source = {
            '@ponprotect.master@' : toMark(group.topPonPSWorkPortIndex),
            '@ponprotect.slave@' : toMark(group.topPonPSStandbyPortIndex),
            '@ponprotect.activePort@' : activePort ? toMark(activePort) : I18N.ponprotect.noActivePort,
            '@ponprotect.lastSwitchTime@' : group.lastSwitchTime || I18N.ponprotect.no,
            '@ponprotect.reason@' : group.topPonPsReason || I18N.ponprotect.no,
            '@ponprotect.count@' : group.topPonPsTimes
        }
    }else{
        source = {
            '@ponprotect.master@' : toMark(group.topPonPSWorkPortIndex),
            '@ponprotect.slave@' : toMark(group.topPonPSStandbyPortIndex),
            '@ponprotect.activePort@' : activePort ? toMark(activePort) : I18N.ponprotect.noActivePort
        }
    }
    
    Attr_Grid.setSource(source)
}

//----------------------------------------------------------------------
//          设置image:三种可能：主端口激活，备端口激活，都没有激活
//----------------------------------------------------------------------
function setImage(divId,type,active){
    if(type == 'master'){
        if(active)
            $("#"+divId).css( 'backgroundImage' , 'url(/epon/image/geEpon/geEpon1.gif)' )
        else
            $("#"+divId).css( 'backgroundImage' , 'url(/epon/image/geEpon/geEpon1.png)' )
    }else if(type == 'slave'){
        if(active)
            $("#"+divId).css( 'backgroundImage' , 'url(/epon/image/geEpon/geEpon4.gif)' )
        else
            $("#"+divId).css( 'backgroundImage' , 'url(/epon/image/geEpon/geEpon4.png)' )
    }else if(type == 'ready'){
        $("#"+divId).css( 'backgroundImage' , 'url(/epon/image/geEpon/geEpon2.png)' )
    }
}

//--------------------------------------------
//          添加监听
//--------------------------------------------
function addListener(divId,type){
    if(type == 'master'){
        $("#"+divId).bind('click',function(e){
            if(ctrlFlag != 1){
                clearPage()
            }
            divCache.add(this.id)
            $(this).css({
                border : '1px solid yellow'                 
            })
        })
        $("#"+divId).bind('contextmenu',function(e){
            e.preventDefault()
            tempPortId = this.id
            clearPage()
            divCache.add(this.id)
            $(this).css({
                border : '1px solid yellow'
            })
            Ext.getCmp("ctmMember").showAt([e.pageX,e.pageY])
        })
    }else{}
}

/**
 * 展示属于trunk组的端口成员
 */
function showPPG(group){
    //-----清理页面-----//
    cleanPorts()
    //work
    var workIdx =  group.topPonPSWorkPortIndex.toString(16)
    var divId = IndexToId(workIdx)
    var portType = getPortType(divId)
    setImage(divId , 'master' , group.topPonPsWorkPortStatus == 1)
    //standby
    var slaveIdx = group.topPonPSStandbyPortIndex.toString(16)
    var divId = IndexToId(slaveIdx)
    var portType = getPortType(divId)
    setImage(divId , 'slave' , group.topPonPsStandbyPortStatus == 1)
}

/**
 * 构造可加入当前trunk组的port
 */
function getAvailblePortList(groupIndex) {
    availablePortList = [];//清空有效成员列表   
    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting')  
    $.ajax({
        url:"/epon/loadPPGAvialMasterPort.tv?",
        data:{entityId : entityId},
        dataType:"json", cache:false,
        success:function (json) {
            window.parent.closeWaitingDlg();
            for(var i=0;i<json.data.length;i++){
                availablePortList.add(json.data[i].index)
            }
            if(0==availablePortList.length){
                return
            }     
            $.each(availablePortList,function(i,index){ //展示所有有效的端口
                var divId = IndexToId(index)
                var portType = getPortType(divId)
                setImage(divId,'ready')
                $("#"+divId).bind('click',function(e){
                    if(ctrlFlag != 1){
                        clearPage()
                    }
                    divCache.add(this.id)
                    $(this).css( 'border' , '1px solid yellow' )
                })
                $("#"+divId).bind('contextmenu',function(e){
                    clearPage()
                    $(this).css( 'border' , '1px solid yellow' )
                    divCache.add(this.id)
                    e.preventDefault()
                    tempPortId = this.id
                    showMasterService(e)
                })
            })
        },
        error:function () {
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchError)
        }
    })
    return true
}



/**
 * RELOAD PPG LIST
 */
function reload(noRefresh){
    $.ajax({
       url: '/epon/ponprotect/loadPPGList.tv', data: {entityId : entityId },
       dataType:"json",cache:false,
       success: function(jsons) {
            PPGList = jsons;
            PPG_Store.loadData(PPGList.data);
            cleanPorts();
            clearTreePanel();
            Attr_Grid.setSource({});
            if(!noRefresh){
            	PPG_Grid.getSelectionModel().selectFirstRow();
            }
       }, 
       error: function(TrunkJson) {
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.TRUNK.loadTrunkError)
       }, cache: false
    })
}
function setPPGDisable(){
    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.ponprotect.disabling ,'ext-mb-waiting') 
    var $selected = PPG_Grid.getSelectionModel().getSelected();
    var idx = getPPGListIndex( $selected.data.topPonPSGrpIndex )
    var group = PPGList.data[idx]
    $.ajax({
        url: '/epon/ponprotect/disablePPG.tv',cache:false,dataType : 'json',
        data : {
            ppgId : group.topPonPSGrpIndex,
            entityId:entityId
        },success : function(json){
        	top.afterSaveOrDelete({
   				title: I18N.COMMON.tip,
   				html: I18N.ponprotect.disableOk
   			});
            //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.disableOk )
            PPGList.data[idx] = json.data
            $selected.set("topPonPsGrpAdmin",2);
            $selected.commit();
            showPPG(json.data)
            showProperty(json.data)
        },error: function(){
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.disableEr )
        }
    })
}
//组使能
function setPPGEnable(){
	var $selected = PPG_Grid.getSelectionModel().getSelected();
    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.ponprotect.enabling ,'ext-mb-waiting') 
    var idx = getPPGListIndex($selected.data.topPonPSGrpIndex)
    var group = PPGList.data[idx]
    $.ajax({
        url: '/epon/ponprotect/enablePPG.tv',cache:false,dataType : 'json',
        data : {
            ppgId : group.topPonPSGrpIndex,
            entityId:entityId
        },success : function(json){
        	top.afterSaveOrDelete({
   				title: I18N.COMMON.tip,
   				html: I18N.ponprotect.enableOk
   			});
            //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.enableOk )
            PPGList.data[idx] = json.data
            $selected.set("topPonPsGrpAdmin",1);
            $selected.commit();
            showPPG(json.data)
            showProperty(json.data)
        },error: function(){
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.enableEr )
        }
    })
}
////删除组
function deletePPG(){
    var group = getSelectedGroup()
    $.ajax({
        url: '/epon/ponprotect/deletePPG.tv',cache:false,
        data : {
            ppgId : group.topPonPSGrpIndex,
            masterIndex : group.topPonPSWorkPortIndex,
            slaveIndex : group.topPonPSStandbyPortIndex,
            entityId:entityId
        },success : function(){
        	top.afterSaveOrDelete({
   				title: I18N.COMMON.tip,
   				html: I18N.ponprotect.deleteOk
   			});
            //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.deleteOk )
            reload()
        },error: function(){
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.deleteEr )
        }
    })
}

function manuSwitch(){
    window.top.showWaitingDlg(I18N.COMMON.wait,  I18N.ponprotect.switching  ,'ext-mb-waiting') 
    var idx = getPPGListIndex(PPG_Grid.getSelectionModel().getSelected().data.topPonPSGrpIndex)
    var group = PPGList.data[idx]
    $.ajax({
        url: '/epon/ponprotect/manuSwitch.tv',cache:false,dataType : 'json',
        data : {
            ppgId : group.topPonPSGrpIndex,
            entityId:entityId
        },success : function(json){
            PPGList.data[idx] = json.data
            showPPG(json.data)
            showProperty(json.data)
            top.afterSaveOrDelete({
   				title: I18N.COMMON.tip,
   				html: I18N.ponprotect.switchOk
   			});
            //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.switchOk )
        },error: function(){
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ponprotect.switchEr )
        }
    })
}

function getSelectedGroup(){
    var idx = getPPGListIndex(PPG_Grid.getSelectionModel().getSelected().data.topPonPSGrpIndex)
    return PPGList.data[idx]
}

/**
 * override from oltCreation.js
 * 清空页面效果
 */
function clearPage(cid){
    while (divCache.length != 0) {
        var divCacheObject = divCache.pop();
        if(cid != divCacheObject){//如果点击的是当前div，则不做修改           
            $temp = $("#"+divCacheObject);    
            $temp.css("border","1px solid transparent");//修改上次点击的div的样式             
        }
    }   
}

/**
 * 根据传入的GroupIndex确定该trunk组在trunklist中的下标
 */
function getPPGListIndex(PPGIndex){
    for(var i =0;i<PPGList.data.length;i++){ //取得该行在json中的对象
        if(PPGList.data[i].topPonPSGrpIndex == PPGIndex){
            return i;
        }
    }
}

function bfsxOltPonProtect(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@", 'ext-mb-waiting')
	$.ajax({
		url:'/epon/bfsxOltPonProtectGroup.tv',cache:false,data:{
			entityId : entityId
		},success:function(){
			window.parent.closeWaitingDlg();
			window.location.reload();
		},error:function(){
			window.parent.closeWaitingDlg();
		}
	})
}
