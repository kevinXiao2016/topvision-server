function setTrunkNameHandler(){
    var trunkIndex = trunkGrid.getSelectionModel().getSelected().data.sniTrunkGroupConfigIndex;
    var idx = getTrunkListIndex(trunkIndex)
    var sb = new Zeta$StringBuffer()
    for(var i=0;i<trunkList.data[idx].sniTrunkGroupConfigGroup.length;i++){     
        sb.append(trunkList.data[idx].sniTrunkGroupConfigGroup[i]);
        sb.append("@");
    }
    var members = sb.toString();
    var policy = trunkList.data[idx].sniTrunkGroupConfigPolicy;
    var trunkName = trunkGrid.getSelectionModel().getSelected().data.sniTrunkGroupConfigName;
    window.top.createDialog('modifyTrunkAttr', I18N.TRUNK.modifyTrunkProperty ,  600,260, '/epon/oltTrunkViewModify.jsp?entityId=' + entityId
        + '&sniTrunkGroupConfigGroup=' + members + '&sniTrunkGroupConfigIndex=' + trunkIndex + '&sniTrunkGroupConfigName=' + escape(escape(trunkName))
        + '&sniTrunkGroupConfigPolicy=' + policy, null, true, true);
}

function deleteTrunkHandler() {
    var groupIndex = trunkGrid.getSelectionModel().getSelected().data.sniTrunkGroupConfigIndex;
    deleteTrunk(groupIndex);
}

function exitTrunkHandler(){
    var groupIndex = trunkGrid.getSelectionModel().getSelected().data.sniTrunkGroupConfigIndex;
    removeMember(IdToIndex(currentId),groupIndex);
}

function joinTrunkHandler(){
    var groupIndex = trunkGrid.getSelectionModel().getSelected().data.sniTrunkGroupConfigIndex;
    addMember(groupIndex,currentId)
}

function showTrunkService(e,idx){
    var items = []
    if(trunkList.data[idx].sniTrunkGroupConfigGroup != ""){
    	items[items.length] = {text: I18N.TRUNK.modifyTrunkProperty,disabled:!operationDevicePower, handler : setTrunkNameHandler};
    }
    items[items.length] = {text: I18N.TRUNK.deleteTrunk,disabled:!operationDevicePower, handler : deleteTrunkHandler};
    displayService(items,e);
}

function showMemberService(e){
    var items = []
    items[items.length] = {text: I18N.TRUNK.exitTrunk, disabled:!operationDevicePower, handler : exitTrunkHandler};
    displayService(items,e); 
}

function showVailableService(e){
    var items = []
    items[items.length] = {text: I18N.TRUNK.joinTrunk, disabled:!operationDevicePower, handler : joinTrunkHandler};
    displayService(items,e); 
}

/*
 * 还原页面RAW样式
 */
function cleanPorts(){
	 $("div, .BoardClass").unbind("click mouseover mouseout contextmenu");
     $(".portStateClass").remove();
     // 移除状态1的样式
     var $slotList = olt.slotList;
     for (var i=0; i < $slotList.length; i++) {
    	var $portList = $slotList[i].portList;
		for (var j=0; j < $portList.length; j++) {
			var targetDiv = IndexToId($portList[j].portIndex);
			var portTYPE = $portList[j].portSubType;
			$("#" + targetDiv).css('backgroundImage',String.format('url(/epon/image/{0}/{0}.png)',portTYPE));
		}
     }
}
/**
 * 构造TRUNK组Grid & 属性表Grid
 */
function bulidTrunkGrid(){
	Trunk_cm = new Ext.grid.ColumnModel([
        {header: "@TRUNK.trunkId@" ,dataIndex:'sniTrunkGroupConfigIndex',align: 'center',sortable: false,resizable: true,menuDisabled :true},
        {header: "@TRUNK.trunkName@",dataIndex:'sniTrunkGroupConfigName',  align: 'center',sortable: false,resizable: true,menuDisabled :true}
    ])
	 trunkStore = new Ext.data.JsonStore({
         data: trunkList.data,
         fields: [
            "sniTrunkGroupConfigIndex","sniTrunkGroupConfigName"
         ],
	    listeners:{
            'load':function(){
            	if(selectedIndex){
            		   var rowNum = trunkGrid.getStore().find("sniTrunkGroupConfigIndex",selectedIndex);
            		   trunkGrid.getSelectionModel().selectRow(rowNum);
            	}
            }
	    }
    })
    trunkGrid = new Ext.grid.GridPanel({
        stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
		viewConfig:{forceFit: true},
    	height: $("#viewLeftPartBody").height(),
    	id : "trunkGrid",
        store : trunkStore,
        cm : Trunk_cm,
        selModel : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        listeners:{
        	'viewready':function(){
        		trunkGrid.getSelectionModel().selectFirstRow();
        	}
        },
        renderTo: "viewLeftPartBody",
	    bbar: new Ext.Toolbar({
       		scope : this,
       		items :[{
      			id : "addTrunk",
      			text : I18N.TRUNK.addTrunk,
      			disabled:!operationDevicePower,
      			width  : 175,
      			handler: addTrunk
            }]
	    })
    });
    trunkGrid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
       	selectedIndex = rowIndex;
       	onTrunkGroupClick(grid, rowIndex, e);
       	showTrunkService(e,rowIndex);
   	});
	 

    trunkGrid.on('rowcontextmenu', function(trunkGrid, rowIndex, e) {
    	selectedIndex = rowIndex;
    	preventBubble(e);
    	showTrunkService(e,rowIndex);
    	this.getSelectionModel().selectRow(rowIndex);
    });
    
    Ext.grid.PropertyGrid.prototype.setSource = function(source) {
        delete this.propStore.store.sortInfo;
        this.propStore.setSource(source);
  	}; 
  	
    propertyGrid =new Ext.grid.PropertyGrid({
        renderTo: 'viewRightPartBottomBody',
        autoHeight:true,
        hideHeaders:true,
        autoScroll: true,
        source: propertyStore,
        border:false,
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
            	// -------双击时默认复制行内容
                var thisText = e.getTarget().innerHTML;
                // -----属性值可能是number型，故强转string
                clipboardData.setData("text",thisText);
                //window.parent.showMessageDlg( "@COMMON.tip@", I18N.EPON.copyOk + thisText );   
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
    // 表格不可编辑
    propertyGrid.on("beforeedit", function(e) {
        e.cancel = true;
        return false;
    });
}

/**
 * trunkGrid行点击事件处理，显示当前组包含的端口，以及能加入到该trunk组的端口
 */
function onTrunkGroupClick(grid, rowIndex,e) {
	 	var arr = $(".portClass")
	 	for(var i=0;i<arr.length;i++){
	 		/* 得到所有端口* */
		 	var id = arr[i].id
		 	/** 解析成端口类型 */
	 		var portType = getPortType(id)
	 		// **清空样式
			$('#'+id).css({
				backgroundImage : 'url(/epon/image/'+portType+'/'+portType+'.png)'
			})
		 }
	  	var idx = getTrunkListIndex(trunkGrid.getStore().getAt(rowIndex).data.sniTrunkGroupConfigIndex)
		var group = trunkList.data[idx]
		showTrunkGroup(group); // 展示该组
		var sniTrunkGroupConfigMember = []
		for(var i =0 ; i< group.sniTrunkGroupConfigGroup.length;i++){
			//sniTrunkGroupConfigMember.push(toMark(group.sniTrunkGroupConfigGroup[i]))
			sniTrunkGroupConfigMember.push(group.trunkPortNameList[i])
		}
		propertyStore = {	            	          
			'@TRUNK.trunkName@' : group.sniTrunkGroupConfigName,
			'@TRUNK.trunkPolicy@': ch_policy[group.sniTrunkGroupConfigPolicy],
            '@TRUNK.trunkRealRate@':group.sniTrunkGroupActualSpeed + ' Mbps', 
            '@TRUNK.member@' : sniTrunkGroupConfigMember.join(","), 
            '@TRUNK.operaStatus@' : ch_operation[group.sniTrunkGroupOperationStatus],
            '@TRUNK.portType@' : group.topSniAttrType,
            '@TRUNK.mediaType@' : ch_media[group.sniMediaType],
            '@TRUNK.memberBD@' :group.topSniAttrSpeed,
            '@TRUNK.autoNegStatus@' :group.sniAutoNegotiationStatus
      	}					
      	trunkSelected = rowIndex
		propertyGrid.setSource( propertyStore )
}

/**
 * 展示属于trunk组的端口成员
 */
function showTrunkGroup(group){
	  // -----清理页面-----//
	  cleanPorts();
	  var $groupMembers = group.sniTrunkGroupConfigGroup;
	  // 显示已经加入到该Trunk组的端口
  	  for(var i =0;i<$groupMembers.length;i++){
			var index =  $groupMembers[i].toString(16)
       	  	var divId = IndexToId(index)
       		var portType = getPortType(divId)
       	 	$("#"+divId).css({
       	 		backgroundImage : 'url(/epon/image/'+portType+'/'+portType+'1.png)'
          	});       	 	
       		$("#"+divId).bind('contextmenu click',function(e){
       			if(e.preventDefault){
	           		e.preventDefault()
       			}
       			currentId = this.id
       			clearPage()
       			divCache.add(this.id)
       			$(this).css({ border : '1px solid yellow' })
				showMemberService(e);
			})
      }
  	if($groupMembers.length < 8){
  		getAvailblePortList(group.sniTrunkGroupConfigIndex)  // 从Server中取出所有的能加入到该trunk的端口成员
  	}
	var demoPort = group.sniTrunkGroupConfigGroup[0]
	if(demoPort!=null&&demoPort!=''){
		var arr = IndexToId(demoPort).split("_")
		var $sniPort = getPort(olt.slotList[arr[1]-1].portList,arr[2]);
		// 将OLT中sni口的属性写入group中
		//group.sniMediaType = $sniPort.sniMediaType
		group.sniAutoNegotiationStatus = $sniPort.sniAutoNegotiationStatus
		group.sniMacAddrLearnMaxNum = $sniPort.sniMacAddrLearnMaxNum
		group.sniIsolationEnable = $sniPort.sniIsolationEnable
	}
	if(group.sniTrunkGroupConfigGroup.length>0){
		var firstPort = group.sniTrunkGroupConfigGroup[0];
		var firstMediaType;
		var flag=true;
		if(firstPort!=null&&firstPort!=''){
			var arr = IndexToId(firstPort).split("_")
			var $sniPort = getPort(olt.slotList[arr[1]-1].portList,arr[2]);
			firstMediaType = $sniPort.sniMediaType
		}
		for(var i=0;i<group.sniTrunkGroupConfigGroup.length;i++){
			var otherPort = group.sniTrunkGroupConfigGroup[i];
			var mediaType;
			var arr = IndexToId(otherPort).split("_")
			var $sniPort = getPort(olt.slotList[arr[1]-1].portList,arr[2]);
			mediaType = $sniPort.sniMediaType;
			if(mediaType!=firstMediaType){
				flag=false;
			}
		}
		if(flag){
			group.sniMediaType = firstMediaType
		}
	}
	
}


function getPort(portList, portIndex) {
	var port = null;
	for(var i=0; i<portList.length; i++) {
		if(portList[i].portRealIndex === parseInt(portIndex,10)) {
			port = portList[i];
			break;
		}
	}
	return port;
}
/**
 * 构造可加入当前trunk组的port
 */
function getAvailblePortList(groupIndex) {
	availablePortList = [];// 清空有效成员列表
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting')  
	$.ajax({
		url:"/epon/trunk/getAvailblePortTrunkList.tv?entityId=" + entityId+'&sniTrunkGroupConfigIndex='+groupIndex,
        method:"post",
        dataType:"json",
        cache:false,
        success:function (json) {
        	window.parent.closeWaitingDlg();        	
        	for(var i=0;i<json.data.length;i++){
        		availablePortList.add(json.data[i].sniIndex)       		     	
            }       
            if(0==availablePortList.length){
                return
			}
        	$.each(availablePortList,function(i,index){ // 展示所有有效的端口
            	var divId = IndexToId(index)
            	var portType = getPortType(divId)
           	 	$("#"+divId).css({
           	 		backgroundImage : 'url(/epon/image/'+portType+'/'+portType+'2.png)'
              	})
    			var portType = getPortType(divId)
    			$("#"+divId).bind('contextmenu click',function(e){
    				if(e.preventDefault){
	    				e.preventDefault()
    				}
    				clearPage()
    				$(this).css( 'border' , '1px solid yellow' )
    				divCache.add(this.id)
    				currentId = this.id
    				showVailableService(e);
    			})
    		})
        },
   		error:function () {
        	window.parent.showMessageDlg("@COMMON.tip@", I18N.COMMON.fetchError)
    	}
	})
	return true
}
/**
 * 新建一个Trunk组
 */
function addTrunk(){
	if(trunkGrid.getSelectionModel().getSelected()){
		   window.selectedIndex = trunkGrid.getSelectionModel().getSelected().data.sniTrunkGroupConfigIndex;
	}
	window.top.createDialog('addNewTrunkGroup', I18N.TRUNK.createTrunk ,  800, 500, '/epon/trunk/jumpTrunkGroupPage.tv?entityId=' + getEntityId(), null, true, true);	
}
function showMSG(){
   	$.ajax({
       url: '/epon/trunk/loadTrunkConfigList.tv',
       async: false,
       data: "entityId=" + entityId +"&num=" + Math.random(),
       dataType:"json",
       success: function(jsons) { 
       		 trunkList = jsons;
		     trunkStore.loadData(trunkList.data); 
       }, 
       error: function(TrunkJson) {
           	window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.loadTrunkError)
       }, cache: false
    })
}

function deleteTrunk(groupIndex,fromMember){
	if(fromMember){
		_deleteTrunk(groupIndex)
		return
	}
	window.parent.showConfirmDlg("@COMMON.tip@", I18N.TRUNK.confirmDeleteTrunk , function(type) {
	    if (type == 'no') {
	        return
	    }
		_deleteTrunk(groupIndex)
	});
}
function deleteOneItem(arr, num){
	arr = arr.splice(num, 1);
}


/**
 * 删除一个trunk组，并删除其中的所有port并放入可用堆中
 */
function _deleteTrunk(groupIndex){
	var idx = getTrunkListIndex(groupIndex);
	window.top.showWaitingDlg(I18N.COMMON.wait, "@TRUNK.deletingTrunk@" , 'ext-mb-waiting');
	$.ajax({
		url:"/epon/trunk/dropTrunkGroup.tv?entityId=" + entityId+'&sniTrunkGroupConfigIndex='+groupIndex,
        cache:false,
        success:function (response) { 
        	window.parent.closeWaitingDlg();   
            if('success' == response){
                if(1 == trunkList.data.length){
					for(var i =0;i<trunkList.data[0].sniTrunkGroupConfigGroup.length;i++){
						var index = trunkList.data[0].sniTrunkGroupConfigGroup[i]
						var port = IndexToId(index)
						var portType = getPortType(port)
		        		$("#"+port).unbind('contextmenu click');
						$("#"+port).css({
							backgroundImage:'url(/epon/image/'+portType+'/'+portType+'.png)'
						})
					}
                }
             deleteOneItem(trunkList.data, idx);
            //trunkList.data.remove(idx)	// 如果成功才删除
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">'+I18N.TRUNK.deleteTrunkOk+'</b>'
       			});
        		//window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.deleteTrunkOk);
        		// ---如果没有任何Trunk组的话，将清空所有端口--//
        		switch(trunkList.data.length){
	        		case 0:
	        			cleanPorts();
	        			trunkStore.loadData(trunkList.data)
	            		break
	        		default:
	        			trunkStore.loadData(trunkList.data)
	        			trunkGrid.getSelectionModel().selectFirstRow()
	            		break;
        		}
        		return true;
            }else{
            	window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.deleteTrunkError)
            	return false
            }    
        },
   		error:function () {   	   		
   			window.parent.closeWaitingDlg()
   			window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.deleteTrunkError)
   			return false
    	}
	});
}
/**
 * 向当前Trunk中添加一个port
 */
function addMember(GroupIndex,port){
	var idx = getTrunkListIndex(GroupIndex)
	var sb = new Zeta$StringBuffer()
	sb.append(IdToIndex(port))
	sb.append("@")
	for(var i=0;i<trunkList.data[idx].sniTrunkGroupConfigGroup.length;i++){		
		sb.append(trunkList.data[idx].sniTrunkGroupConfigGroup[i])
		sb.append("@")
	}
	var members = sb.toString()
	var policy = trunkList.data[idx].sniTrunkGroupConfigPolicy
	var trunkName = trunkList.data[idx].sniTrunkGroupConfigName
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving , 'ext-mb-waiting');
	$.ajax({
		url:"/epon/trunk/modifyTrunkGroup.tv?entityId=" + entityId+'&sniTrunkGroupConfigIndex='+GroupIndex,
		data : 'sniTrunkGroupConfigPolicy='+policy+'&sniTrunkGroupConfigName='+trunkName+'&sniTrunkGroupConfigGroup='+members,
        method:"post",
        cache:false,
        success:function (response) {            
        	window.parent.closeWaitingDlg()
        	if('success'==response){
        		top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">'+I18N.TRUNK.addOk+'</b>'
       			});
        		//window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.addOk);
        		// ----把该端口加入到Trunk组中---//
        		//trunkList.data[idx].sniTrunkGroupConfigGroup.add(IdToIndex(port))
        		//在添加成功后,重新加载TRUNK数据
        		loadTrunkConfigData();
        		var portType = getPortType(port)
        		$("#"+port).unbind('contextmenu')
				$("#"+port).css({
					backgroundImage:'url(/epon/image/'+portType+'/'+portType+'2.png)'
				})
				$("#"+port).bind('contextmenu',function(e){
					e.preventDefault()
					clearPage()
    				currentId = this.id
    				divCache.add(this.id)
    				showMemberService(e);
				})
				onTrunkGroupClick(null,idx)
           	}else{
           		window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.addError);
            }
        },
   		error:function () {
   			window.parent.closeWaitingDlg()
   			window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.addError);
    	}
	})
}
/**
 * 从当前trunk中删除该port
 */
function removeMember(member,GroupIndex){
	var idx = getTrunkListIndex(GroupIndex)
	if(1==trunkList.data[idx].sniTrunkGroupConfigGroup.length){// 没有成员了，则把这个trunk组也删除掉
		window.parent.showConfirmDlg("@COMMON.tip@", I18N.TRUNK.cfmDeleteTrunk, function(type) {	
		    if (type == 'no') {
		        return;
		    }
			// ---删除该Trunk组，如果删除失败则中断---//
			if(!deleteTrunk(GroupIndex,true)){
				return;
			};
			// 在trunklist中把该组删除掉，成员也会被删除掉
			var arr = $(".portClass");
		 	for(var i=0;i<arr.length;i++){						
			 	var portId = arr[i].id;
		 		var portType = getPortType(portId);
				$('#'+portId).css({
					backgroundImage : 'url(/epon/image/'+portType+'/'+portType+'.png)'
				});
				$("#"+portId).unbind('contextmenu click');
			 }
		});
	}else{// 有成员了，则把这个成员删除掉
		window.parent.showConfirmDlg("@COMMON.tip@", I18N.TRUNK.cfmDeletePort , function(type) {
		    if (type == 'no') {
		        return;
		    }
			var sb = new Zeta$StringBuffer();
			for(var i=0;i<trunkList.data[idx].sniTrunkGroupConfigGroup.length;i++){		
				if(trunkList.data[idx].sniTrunkGroupConfigGroup[i] == member)continue;
				sb.append(trunkList.data[idx].sniTrunkGroupConfigGroup[i]);
				sb.append("@");
			}
			var members = sb.toString();
			var policy = trunkList.data[idx].sniTrunkGroupConfigPolicy;
			var trunkName = trunkList.data[idx].sniTrunkGroupConfigName;
			window.top.showWaitingDlg(I18N.COMMON.wait, I18N.TRUNK.saving , 'ext-mb-waiting');
			(function(idx,member){
				$.ajax({
				url:"/epon/trunk/modifyTrunkGroup.tv?entityId=" + entityId+'&sniTrunkGroupConfigGroup='+members+'&sniTrunkGroupConfigIndex='+GroupIndex,
		        method:"post",
		        cache:false,
		        data : 'sniTrunkGroupConfigName='+trunkName+'&sniTrunkGroupConfigPolicy='+policy,
		        success:function (response) {
		        	window.parent.closeWaitingDlg(); 
		        	if('success'==response){
		        		//在删除成功后,重新加载TRUNK数据
		        		loadTrunkConfigData();
			        	// 移除该端口的事件和样式
						var portId = IndexToId(member);
						var portType = getPortType(portId);
		        		$("#"+portId).unbind('contextmenu');
						$("#"+portId).css({
							backgroundImage:'url(/epon/image/'+portType+'/'+portType+'1.png)'
						});
						$("#"+portId).bind('contextmenu',function(e){
							e.preventDefault();
							clearPage();
		    				currentId = this.id;
		    				divCache.add(this.id);
		    				showVailableService(e);
						});
		        		//trunkList.data[idx].sniTrunkGroupConfigGroup.removeObj(member);	// 从大对象中删除
		        		// showTrunkGroup(trunkList.data[idx]);
		        		onTrunkGroupClick(null,idx)
		        		top.afterSaveOrDelete({
		       				title: "@COMMON.tip@",
		       				html: '<b class="orangeTxt">'+I18N.TRUNK.deleteOk+'</b>'
		       			});
		        		//window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.deleteOk)
		        	}else{
		        		window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.deleteError)
			        }
		        },
		   		error:function () {
		   			window.parent.closeWaitingDlg(); 
		   			window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.deleteError)
		    	}
			})
			})(idx,member)
		})
	}
}

/**
 * override from oltCreation.js 清空页面效果
 */
function clearPage(cid){
	while (divCache.length != 0) {
    	var divCacheObject = divCache.pop();
	    if(cid != divCacheObject){// 如果点击的是当前div，则不做修改
		    $temp = $("#"+divCacheObject);	  
		    $temp.css("border","1px solid transparent");// 修改上次点击的div的样式
    	}
	}	
}

/**
 * 根据传入的GroupIndex确定该trunk组在trunklist中的下标
 */
function getTrunkListIndex(TrunkIndex){
	for(var i =0;i<trunkList.data.length;i++){ // 取得该行在json中的对象
		if(trunkList.data[i].sniTrunkGroupConfigIndex == TrunkIndex){
			return i;
		}
	}
}

/*Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
}*/

function bfsxOltTrunk(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@", 'ext-mb-waiting')
	$.ajax({
		url:'/epon/bfsxOltTrunk.tv',cache:false,data:{
			entityId : entityId
		},success:function(){
			window.parent.closeWaitingDlg();
			window.location.reload();
		},error:function(){
			window.parent.closeWaitingDlg();
		}
	})
}

function loadTrunkConfigData(){
	$.ajax({
	       url: '/epon/trunk/loadTrunkConfigList.tv',
	       async: false,
	       data: "entityId=" + entityId +"&num=" + Math.random(),
	       dataType:"json",
	       success: function(jsons) { 
	       		 trunkList = jsons;
	       }, 
	       error: function(TrunkJson) {
	       }, cache: false
	    })
}
