/*
 * 还原页面RAW样式
 */
function cleanPorts(){
	$(".portClass").unbind("click mouseover mouseout contextmenu");
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
                //隔离组中不包含PON口，故统一处理
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

/**
 * 构造隔离组Grid & 属性表Grid
 */
function create_iso_group_grid(){
    var $ISO_GROUP_COLMS = new Ext.grid.ColumnModel([
        {header: "@ISOGROUP.id@", dataIndex:'groupIndex', width:50},
        {header: "<div class='txtCenter'>@COMMON.desc@</div>", align:'left', dataIndex:'groupDesc', width:120}
        //{header: "@COMMON.manu@", width:70, dataIndex :'op', renderer: manuRender, fixed:true}
    ])
    ISO_GROUP_STORE = new Ext.data.JsonStore({
    	url: '/epon/isolationgroup/loadGroupList.tv',
    	baseParams : {
			entityId : entityId
		},
        fields:["groupIndex","groupDesc"],
        autoLoad : true
    });
    
    ISO_GROUP_GRID = new Ext.grid.GridPanel({
        stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
		viewConfig:{forceFit: true},
        height: $("#viewLeftPartBody").height(),
        renderTo: "viewLeftPartBody", 
        store : ISO_GROUP_STORE,
        cm : $ISO_GROUP_COLMS, 
        tbar: new Ext.Toolbar({
            items :[
               { iconCls: 'bmenu_new', text : "@COMMON.create@", handler: showGroupAdd ,disabled:!operationDevicePower},"-",
               { iconCls: 'bmenu_edit', text : "@COMMON.edit@", handler: showModifyGroup ,disabled:!operationDevicePower},"-",
               { iconCls: 'bmenu_delete', text : "@COMMON.delete@", handler: deleteGroup ,disabled:!operationDevicePower}
            
            ]
        }),
        listeners : {
        	viewready :function(){
        		setTimeout(function(){
        			ISO_GROUP_GRID.getSelectionModel().selectFirstRow();
        		},200)
			}
        },
        sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		})
    })
    
    ISO_GROUP_GRID.getSelectionModel().on('rowselect', function(grid, $rowIndex, e) {
    	onGroupClick(e);
    });
    
    ISO_GROUP_GRID.on('rowclick', function(grid, $rowIndex, e) {
    	showGroupService(e);
    });
    
    ISO_GROUP_GRID.on('rowcontextmenu', function(ISO_GROUP_GRID, $rowIndex, e) {
        preventBubble(e);
        e.preventDefault();
        var sm = ISO_GROUP_GRID.getSelectionModel();
        if (sm != null && !sm.isSelected( $rowIndex )) {
            sm.selectRow( $rowIndex );
        }
    });
    
    PROPERTY_GRID =new Ext.grid.PropertyGrid({
        renderTo: 'viewRightPartBottomBody',
        autoHeight: true,
        border: false,
        hideHeaders:true,
        autoScroll: true,
        source: PROP_STORE,
        selModel : new Ext.grid.RowSelectionModel({
            singleSelect : true,
            listeners:{
            	beforerowselect : function(){
                    return false;
                }
            }
        }),
        listeners:{
        	contextmenu : function(e){
                e.preventDefault();
            },
            rowdblclick:function(g,r,e ){
            	//-------双击时默认复制行内容 
                var thisText = e.getTarget().innerHTML;
                //-----属性值可能是number型，故强转string
                clipboardData.setData("text",thisText);
                top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: "@EPON.copyOk@"+ thisText
       			});
                //window.parent.showMessageDlg( I18N.COMMON.tip, I18N.EPON.copyOk + thisText );   
            },
            render: function(proGrid) {
                var view = proGrid.getView();
                var store = proGrid.getStore();
                proGrid.tip = new Ext.ToolTip({
                    target: view.mainBody,
                    delegate: '.x-grid3-row',
                    trackMouse: true,
                    renderTo: document.body,
                    listeners: {
                        beforeshow: function updateTipBody(tip) {
                            var $rowIndex = view.findRowIndex(tip.triggerElement);
                            tip.body.dom.innerHTML = store.getAt($rowIndex).get('value');
                        }
                    }
                });
            }                               
        }
    });

    //表格不可编辑
    PROPERTY_GRID.on("beforeedit", function(e) {
        e.cancel = true;
        return false;
    });
}


/**
 * 行点击事件处理，显示当前组包含的端口
 */
function onGroupClick(e) {
	currentId=null;
	clearPage();
    var arr = $(".portClass")
    for(var i=0;i<arr.length;i++){
        /*得到所有端口**/
        var id = arr[i].id
        /**解析成端口类型*/
        var portType = getPortType( id )
        //**清空样式
        $('#'+id).css({
            backgroundImage : 'url(/epon/image/'+portType+'/'+portType+'.png)'
        })
    }
    var selectGroupIndex = getSelectedGroupIndex();
    //展示该组
    show_iso_group( selectGroupIndex );
    showProperty();
    showGroupService(e);
}

function showGroupService(e){
	var items = []
		items[items.length] = {text: "@ISOGROUP.modifyGroup@",disabled:!operationDevicePower, handler : showModifyGroup};
		items[items.length] = {text: "@ISOGROUP.deleteGroup@",disabled:!operationDevicePower, handler : deleteGroup};
	displayService(items,e);
}


function showProperty(){
	var select = ISO_GROUP_GRID.getSelectionModel().getSelected();
	PROP_STORE = {	            	          
		'@ISOGROUP.groudId@' : select.data.groupIndex,
		'@ISOGROUP.groudDesc@': select.data.groupDesc
  	}	
	PROPERTY_GRID.setSource( PROP_STORE );
}

/**
 * 展示属于隔离组的端口成员
 */
function show_iso_group( $groupIndex ){
	$.ajax({
        url: '/epon/isolationgroup/loadGroupMember.tv', 
        dataType:"json",
        cache:false,
        data: { 
        	entityId:entityId, 
        	groupIndex: $groupIndex
        },
        success: function( $members ) {
        	$(".portClass").data("isMember", false);
        	$.each($members, function($index, $member){
        	  	var $divId = IndexToId( $member.portIndex );
        		var portType = getPortType( $divId );
        	 	$("#"+ $divId).css({
        	 		backgroundImage : 'url(/epon/image/'+portType+'/'+portType+'1.png)'
        	 	}).data("isMember", true);
        	});
        }
	});
}

/**
 * 获取选中的隔离组的index
 * @returns
 */
function getSelectedGroupIndex(){
	var select = ISO_GROUP_GRID.getSelectionModel().getSelected();
	var index = select.data.groupIndex;
    return index;
}

/**
 * override from oltCreation.js
 * 清空页面效果
 */
function clearPage( $id ){
    while (divCache.length != 0) {
        var divCacheObject = divCache.pop();
        if($id != divCacheObject){//如果点击的是当前div，则不做修改 
            removeBorder( divCacheObject );
        }
    }
}

function addBoarder( $dom ){
	if( typeof $dom == 'string'){
		$( "#"+ $dom ).css( "border" , '1px solid yellow' );
	}else{
		$dom.css("border","1px solid yellow");
	}
}

function removeBorder( $dom ){
	if( typeof $dom == 'string'){
		$( "#"+ $dom ).css( "border" , '1px solid transparent' );
	}else{
		$dom.css("border","1px solid transparent");
	}
}

function addListeners(){
	$("div, .BoardClass").unbind("click mouseover mouseout contextmenu");
	$(".portClass").bind("contextmenu click", function(e){
		preventBubble(e);
		var $isMember = $(this).data( "isMember" );
		if( SINGLE_SELECT_MODE ){
			clearPage();
			if(currentId == this.id && e.type == 'click'){
				removeBorder( this.id );
				divCache.remove( this.id );
				currentId=null;
				if( divCache.length == 0 ){
					return showGroupService(e);
				}
			}else{
				addBoarder( this.id );
				divCache.add( this.id );
			}
			currentId = this.id;
		} else {
			currentId = this.id;
			//默认于当前的端口情况一致
			var $forExit = $isMember;
			$.each( divCache, function(i, $id){
				$forExit = $("#" + $id ).data( "isMember" );
				//第一个就可以代表全部了
				return $forExit;
			});
			//只有前后端口的动作一致才被允许
			if( $forExit == $isMember){
				if(e.type == 'click'){
					if( divCache.contains( currentId ) ){
						removeBorder( currentId );
						divCache.remove( currentId );
						if( divCache.length == 0 ){
							showGroupService(e);
						}
						return;
					} else {
						addBoarder( currentId );
						divCache.add( currentId );
					}
				} else if(e.type == 'contextmenu'){
					if( !divCache.contains( currentId ) ){
						addBoarder( currentId );
						divCache.add( currentId );
					}
				}
			} else {
				return;
			}
		}
		if(ISO_GROUP_GRID.getSelectionModel().hasSelection()){
			if( $isMember ){
				showMemberService(e);
			}else{
				showJoinService(e);
			}
		}
	});
}

function showMemberService(e){
	var items = []
    items[items.length] = {text: "@ISOGROUP.exitGroup@", handler: deleteGrpMember}
    displayService(items,e);
}

function showJoinService(e){
	var items = []
    items[items.length] = {text: "@ISOGROUP.joinGroup@", handler: addGrpMember}
    displayService(items,e);
}

/**
 * 添加隔离组
 */
function showGroupAdd(){
	window.top.createDialog('addGroupPage', "@ISOGROUP.addGroup@", 600, 370, "/epon/isolationgroup/showAddGroupPage.tv?entityId="+entityId, null, true, true);
}

/**
 * 修改隔离组
 */
function showModifyGroup(){
	var select = ISO_GROUP_GRID.getSelectionModel().getSelected();
	var index = select.data.groupIndex;
	var desc = escape(select.data.groupDesc);
	window.top.createDialog('modifyGroupPage', "@ISOGROUP.modifyGroup@", 600, 370, "/epon/isolationgroup/showModifyGroupPage.tv?entityId="+entityId+"&groupIndex="+index+"&groupDesc="+desc, null, true, true);
}

/**
 * 删除隔离组
 */
function deleteGroup(){
	var select = ISO_GROUP_GRID.getSelectionModel().getSelected();
	var index = select.data.groupIndex;
	window.parent.showConfirmDlg("@COMMON.tip@", "@ISOGROUP.deleteConfirm@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@",  "@ISOGROUP.groupDeleting@");
		$.ajax({
	       url: '/epon/isolationgroup/deleteGroup.tv',
	       data: {
	    	   entityId : entityId,
	    	   groupIndex : index
	       },
	   	   type : 'post',
		   dataType: 'json',
	       success: function() {
			   	if(ISO_GROUP_STORE.getCount() == 1){
			   		location.reload();
			   	}else{
			   		reloadData( index );
			   	}
				top.afterSaveOrDelete({
				  	title: "@COMMON.tip@",
				  	html: '<b class="orangeTxt">@ISOGROUP.groupDeleteSuc@</b>'
				});
	       }, error: function() {
	       		window.parent.showMessageDlg("@COMMON.tip@", "@ISOGROUP.groupDeleteFail@",'error')
	   	   }, cache: false
		});
	});
}

/**
 * 从设备获取隔离组信息
 */
function refreshClick(){
	window.top.showWaitingDlg("@COMMON.wait@", "@ISOGROUP.onRefreshing@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/isolationgroup/refreshGroupData.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@ISOGROUP.refreshSuc@</b>'
       	    });
			reloadData();
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@ISOGROUP.refreshFail@");
		},
		cache : false
	});
}

/**
 * 端口移出隔离组
 */
function deleteGrpMember(){
	var select = ISO_GROUP_GRID.getSelectionModel().getSelected();
	var index = select.data.groupIndex;
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
	if( SINGLE_SELECT_MODE ){
		var portIndex = IdToIndex(currentId);
		$.ajax({
			url: '/epon/isolationgroup/deleteGroupMember.tv',
			data: {
				entityId : entityId,
				groupIndex : index,
				portIndex : portIndex
			},
			type : 'post',
			dataType: 'json',
			success: function() {
				top.afterSaveOrDelete({
					title: "@COMMON.tip@",
					html: '<b class="orangeTxt">@ISOGROUP.exitGroupSuc@</b>'
				});
				reloadData(index);
			}, error: function() {
				window.parent.showMessageDlg("@COMMON.tip@", "@ISOGROUP.exitGroupFail@",'error')
			}, cache: false
		});
	} else {
		var selectedPorts = new Array();
		$.each( divCache, function(i, $id){
			selectedPorts.add(IdToIndex($id));
		});
		$.ajax({
			url: '/epon/isolationgroup/batchDeleteGrpMember.tv',
			data: {
				entityId : entityId,
				groupIndex : index,
				portsStr : selectedPorts.join(",")
			},
			type : 'post',
			dataType: 'json',
			success: function() {
				top.afterSaveOrDelete({
					title: "@COMMON.tip@",
					html: '<b class="orangeTxt">@ISOGROUP.exitGroupSuc@</b>'
				});
				reloadData(index);
			}, error: function() {
				window.parent.showMessageDlg("@COMMON.tip@", "@ISOGROUP.exitGroupFail@",'error')
			}, cache: false
		});
	}
}

/**
 * 端口加入隔离组
 */
function addGrpMember(){
	var select = ISO_GROUP_GRID.getSelectionModel().getSelected();
	var index = select.data.groupIndex;
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
	if( SINGLE_SELECT_MODE ){
		var portIndex = IdToIndex(currentId);
		$.ajax({
	       url: '/epon/isolationgroup/addGroupMember.tv',
	       data: {
	    	   entityId : entityId,
	    	   groupIndex : index,
	    	   portIndex : portIndex
	       },
	   	   type : 'post',
		   dataType: 'json',
	       success: function() {
	    	   	top.afterSaveOrDelete({
	      	      	title: "@COMMON.tip@",
	      	      	html: '<b class="orangeTxt">@ISOGROUP.joinGroupSuc@</b>'
	      	    });
	    	    reloadData( index );
	       }, error: function() {
	       		window.parent.showMessageDlg("@COMMON.tip@", "@ISOGROUP.joinGroupFail@",'error')
	   	   }, cache: false
		});
	}else{
		var selectedPorts = new Array();
		$.each( divCache, function(i, $id){
			selectedPorts.add(IdToIndex($id));
		});
		$.ajax({
			url: '/epon/isolationgroup/batchAddGrpMember.tv',
			data: {
				entityId : entityId,
				groupIndex : index,
				portsStr : selectedPorts.join(",")
			},
			type : 'post',
			dataType: 'json',
			success: function() {
	    	   	top.afterSaveOrDelete({
	      	      	title: "@COMMON.tip@",
	      	      	html: '<b class="orangeTxt">@ISOGROUP.joinGroupSuc@</b>'
	      	    });
	    	    reloadData( index );
			}, error: function() {
       		window.parent.showMessageDlg("@COMMON.tip@", "@ISOGROUP.joinGroupFail@",'error')
			}, cache: false
		});
	}
	
}

/**
 * 刷新页面
 */
function reloadData( $selectedIndex ){
	var $sm = ISO_GROUP_GRID.getSelectionModel();
	var $selected = $sm.getSelected();
	if($selectedIndex != null || $selected != null){
		ISO_GROUP_STORE.reload({
			callback: function(){
				var $rowIndex = ISO_GROUP_STORE.find("groupIndex", $selectedIndex || $selected.data.groupIndex );
				if($rowIndex != -1){
					$sm.selectRow( $rowIndex, false );
				}else{
					//没有找到时默认选中第一行
					ISO_GROUP_GRID.getSelectionModel().selectFirstRow();
				}
			}
		});
	}
}
