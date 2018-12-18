/**
 * 不同的环境下显示不同的列
 * @param onuEnvironment
 * @param cmArr
 * @param spliceColIds 不需要显示列的 id, spliceColIds[0] 表示纯epon下不需要显示列的id集合，spliceColIds[1]表示纯gpon下不需要显示列的id
 * @returns
 */
function initColumnArr(onuEnvironment, cmArr, spliceColIds){
	var colIds = [];
	if(onuEnvironment == 'E'){
		colIds = spliceColIds[0];
	}else if(onuEnvironment == 'G'){
		colIds = spliceColIds[1];
	}
	// 混合环境下直接返回
	if(colIds.length == 0){
		return cmArr;
	}
	for(var i=0; i<cmArr.length; i++){
		var id = cmArr[i].id;
		if(V.isInArray(id, colIds)){
			cmArr.splice(i, 1);
			//防止删除某项后下一项不会被遍历
			i -= 1;
		}
	}
	return cmArr;
}

/**
 * 跳转onu快照页
 * @param onuId
 * @param onuName
 */
function showOnuInfo(onuId,onuName){
	 window.parent.addView('entity-' + onuId, unescape(onuName), 'entityTabIcon', '/onu/showOnuPortal.tv?onuId=' + onuId );
}

/**
 * 跳转olt快照页
 * @param entityId
 * @param name
 */
function showOlt(entityId,name){
	window.top.addView('entity-' + entityId,  name , 'entityTabIcon','/portal/showEntitySnapJsp.tv?entityId=' + entityId);
}

/**
 * 创建tips
 * @param o
 * @returns {String}
 */
function createBubble(o){
	var str = '',
	    recordStr = String.format('<div class="subDashedLine wordBreak">{0}</div>', o.recordStr);
	str += '<div class="bubbleTip" style="width:430px" id="bubbleTip">';
	str += '	<div class="bubbleTipArr"></div>';
	str += '	<div class="bubbleBody" style="width:352px">';
	str += 			recordStr;
	str += '	</div>';
	str += '</div>';
	return str;
}

/**
 * tips显示上下线记录
 * @param label
 */
function showOnuOnoffRecordTips(label){
	$(label).live("mouseenter",function(e){
		var $me = $(this);
		$bubbleTip = $("#bubbleTip"),
		bodyW = $("body").width(),
		$thisLeft = $me.position().left,
		showTipW = 406,
		$thisW = $me.outerWidth(),
	    $tr = $me.parent().parent().parent(),
		onuEles = $me.parent().find(".onuIdValue"),
		onuId = onuEles[0].defaultValue;
		var xpos = 0,
		xclass = 'left',
		ypos = $me.offset().top;
		
		if(bodyW - $thisLeft - $thisW > showTipW){
			xclass = 'right';
			xpos = $thisLeft + $thisW;
		} else if($thisLeft > showTipW){
			xclass = 'left';
			xpos = $thisLeft - showTipW;
		} else{
			xclass = 'right';
			xpos = $thisLeft + $thisW;
		}
		
	    o = {};
		$.ajax({
			url: "/onu/onoffrecord/loadOnOffRecords.tv",
			data: {
				onuId: onuId,
				start: 0,
				limit: 8
			},
			success: function(json){
				o.recordStr = "<table calss='time-table'><thead style='color:green'><tr><th class='time-style'>@ONU.onlineTime@</th><th class='time-style'>@ONU.offlineTime@</th><th  width='80px'>@ONU.offlineReason@</th></tr></thead>"
				var data = json.data, count = data.length;
				for(var i=0; i<count; i++){
					o.recordStr += "<tr><td class='time-style'>" + data[i].onTimeString + "</td><td class='time-style'>"+ data[i].offTimeString +"</td><td class='reason-style'>" + renderOffRenson(data[i].offReason) +"</td></tr>";
				}
				o.recordStr += "</table>";
				var str = "";
				str = createBubble(o);
				
				if($bubbleTip.length >= 1){
					$bubbleTip.remove();	  
				}
				$("body").append(str);
				$bubbleTip = $("#bubbleTip");//需要重新获取一次;
				$bubbleTip.css({left : xpos, top : ypos});
				
				var h = $(window).height(),
				    h2 = h - ypos;
				var outH=$bubbleTip.outerHeight();
				if( h2 > $bubbleTip.outerHeight() && xclass == 'left'){
					$bubbleTip.find(".bubbleTipArr").css("top",0);	
				} else if(h2 > $bubbleTip.outerHeight() && xclass == 'right'){
					$bubbleTip.find(".bubbleTipArr").css("top",0).addClass("bubbleTipArr3");
				} else if(h2 < $bubbleTip.outerHeight() && xclass == 'left'){
					$bubbleTip.find(".bubbleTipArr").css("bottom",0).addClass("bubbleTipArr2");
					$bubbleTip.css({
						top : ypos - $bubbleTip.outerHeight() + 10
					});
				} else{
					$bubbleTip.find(".bubbleTipArr").css("bottom",0).addClass("bubbleTipArr4");
					$bubbleTip.css({
						top : ypos - $bubbleTip.outerHeight() + 10
					});
				}		
			},
			error: function(json){
			},
			cache:false
		});
	}).live("mouseleave",function(){
		var $bubbleTip = $("#bubbleTip");
		$bubbleTip.remove();
	});
}

/**
 * 通过index获得location
 * @param index
 * @param type
 * @returns {String}
 */
function getLocationByIndex(index, type){
	var t = parseInt(index / 256) + (index % 256);
	var loc = getNum(t, 1) + "/" + getNum(t, 2) + ":" + getNum(t, 3);
	if(type == "uni"){
		loc = loc + "/" + getNum(t, 4)
	}
	return loc
}

/**
 * 通过mibIndex获得num
 * @param index
 * @param s
 * @returns {Number}
 */
function getNum(index, s){
	var num;
	switch (s)
	{
	case 1: num = (index & 0xFF000000) >> 24;//SLOT
		break;
	case 2: num = (index & 0xFF0000) >> 16;//PON/SNI
		break;
	case 3: num = (index & 0xFF00) >> 8;//ONU
		break;
	case 4: num = index & 0xFF;//UNI
		break;
	}
	return num;
}

/**
 * 创建分页框
 * @param pageSize
 * @param store
 * @returns {Ext.PagingToolbar}
 */
function buildPagingToolBar(pageSize, store) {
    var pagingToolbar = new Ext.PagingToolbar({
    	id: 'extPagingBar', 
    	pageSize: pageSize,
    	store:store,
    	displayInfo: true, 
    	items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-'],
    	listeners : {}
    });
   return pagingToolbar;
}

function invokeCallbacks(callbacks){
	if(callbacks){
		for(var i=0; i<callbacks.length; i++){
			var callback = callbacks[i];
			callback();
		}
	}
}

/**
 * 刷新ONU信息
 * @param entityId
 * @param onuId
 * @param onuIndex
 */
function refreshOnuFn(entityId, onuId, onuIndex, callbacks){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@");
	$.ajax({
		url:'/onulist/refreshOnuInfo.tv',
		data: {
	      	entityId : entityId,
	      	onuId : onuId,
	      	onuIndex : onuIndex
		}, 
		dateType:'json',
		success:function(json){
      		window.top.closeWaitingDlg();
      		top.afterSaveOrDelete({
      			title: '@COMMON.tip@',
	    		html: '<b class="orangeTxt">@ONU.refreshSuccess@</b>'
 			});
      		invokeCallbacks(callbacks);
      },
      error:function(){
    	  window.top.showMessageDlg("@COMMON.tip@", "@ONU.refreshFailure@");
      },
      cache:false
  });
}

/**
 * onu解注册
 * @param entityId
 * @param onuId
 */
function unregisterOnuFn(entityId, onuId, callbacks) {
    window.parent.showConfirmDlg("@COMMON.tip@", "@ONUVIEW.deregisterTip@", function(type) {
        if (type == 'no') return
        top.showWaitingDlg("@COMMON.wait@", "@ONUVIEW.deregistering@");
        $.ajax({
            url: '/onu/deregisterOnu.tv',data: "entityId=" + entityId + "&onuId=" + onuId,
            success: function() {
            	top.closeWaitingDlg("@COMMON.tip@");
            	top.afterSaveOrDelete({
                	title: "@COMMON.tip@",
                	html:  "@ONU.unregisterOk@"
            	});
            	invokeCallbacks(callbacks);
            }, error: function() {
            	top.closeWaitingDlg("@COMMON.tip@");
            	top.afterSaveOrDelete({
                	title: "@COMMON.tip@",
                	html: "@ONUVIEW.deregisterError@"
                })
            }, cache: false
        });
    });
}

/**
 * onu替换
 * @param onuId
 */
function replaceOnuFn(onuId){
	 window.top.createDialog('replaceOnu', '@epon/onu.replace.replace@', 600, 370,
	    '/onu/replace/showOnuReplaceView.tv?onuId=' + onuId + '&pageId=' + window.parent.getActiveFrameId(), null, true, true);
}

/**
 * onu复位
 * @param entityId
 * @param onuId
 */
function resetOnuFn(entityId, onuId, callbacks) {
    window.parent.showConfirmDlg("@COMMON.tip@", "@ONU.confirmReset@", function(type) {
        if (type == 'no') {
            return;
        }
        window.parent.showWaitingDlg("@COMMON.wait@", "@ONU.reseting@", 'ext-mb-waiting');
        $.ajax({
            url: '/onu/resetOnu.tv',
            type: 'POST',
            data: "entityId=" + entityId + "&onuId=" + onuId,
            success: function() {
            	top.closeWaitingDlg();
            	top.nm3kRightClickTips({
    				title: "@COMMON.tip@",
    				html: "@ONU.resetSuccess@"
    			});
            	invokeCallbacks(callbacks);
            }, 
            error: function() {
            	window.parent.showMessageDlg("@COMMON.tip@", "@ONU.resetFail@");
            },
            cache: false
        });
    });
}

/**
 * onu删除
 * @param entityId
 * @param onuId
 * @param onuIndex
 * @param grid
 */
function deleteOnuFn(entityId, onuId, onuEorG, callbacks){
	window.top.showConfirmDlg("@COMMON.tip@", "@ONU.confirmDel@", function(type) {
        if (type == 'no'){
        	return;
        }
        window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.deleting@", 'ext-mb-waiting');
        if(onuEorG == 'E'){
        	$.ajax({
                url: '/onu/deleteOnuAuth.tv',
                data: "entityId=" + entityId+"&onuId="+onuId,
                success: function(json) {
                	top.closeWaitingDlg();
                	top.afterSaveOrDelete({
    	   				title: '@COMMON.tip@',
    	   				html: '<b class="orangeTxt">@ONU.deleteOk@</b>'
    	   			});
                	invokeCallbacks(callbacks);
                }, 
                error: function() {
                	window.top.showMessageDlg("@COMMON.tip@", "@ONU.deleteEr@");
                },
                cache: false
            })
        }else{
    	    $.ajax({
    	        url: '/gpon/onuauth/deleteGponOnuAuth.tv',type: 'POST',
    	        data: {
    	        	'gponOnuAuthConfig.entityId':entityId,
    	        	'gponOnuAuthConfig.onuId':onuId
    	        },
    	        success: function() {
    	        	top.closeWaitingDlg();
    	        	top.afterSaveOrDelete({
    	   				title: '@COMMON.tip@',
    	   				html: '<b class="orangeTxt">@ONU.deleteOk@</b>'
    	   			});
                	invokeCallbacks(callbacks);
    	        }, error: function(json) {
    	        	window.top.showMessageDlg("@COMMON.tip@", "@ONU.deleteEr@");
    	        }, cache: false
    	    });
        }
    })
}

/**
 * onu 业务配置
 * @param entityId
 * @param onuId
 * @param name
 */
function onuConfigFn(entityId,onuId, name){
	window.top.addView('uniserviceconfig-'+onuId, '@ONU.uniConfig@[' + name +']','icoD1', '/onu/showUniServiceConfig.tv?onuId='+onuId + "&entityId="+entityId,null,true);
}

/**
 * 关注
 * @param entityId
 * @param onuId
 * @param attention
 */
function attentionOnuFn(entityId, onuId, attention, callback){
	if(attention){//如果已关注,则从table中删除该记录
		$.ajax({
			url:'/entity/cancelAttention.tv',
			cache:false,
			data : {entityId : onuId},
			success : function(){
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.cancelFocusSucccess@</b>'
	   			});
				attention = false;
				controlAttention(attention);
				if(callback){
          			callback();
          		}
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.cancelFocusFail@")
			}
		})
	}else{//如果没有关注则添加该设备记录
		$.ajax({
			url:'/entity/pushAttention.tv',cache:false,
			data : {entityId : onuId},
			success : function(){
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.focusSucccess@</b>'
	   			});
				attention = true;
				controlAttention(attention);
				if(callback){
          			callback();
          		}
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.focusFail@")
			}
		})
	}
}

/**
 * 标签
 */
function tagOnuFn(onuId, tagId){
	window.top.createDialog('tagOnu', '@ONU.tag@', 600, 370,
'onulist/showOnuTagView.tv?onuId=' + onuId + '&tagId=' + tagId + '&pageId=' + window.parent.getActiveFrameId(), null, true, true);
}

/**
 * 重新载入store
 * @param store
 */
function onRefreshClick(grid) {
	var store = grid.getStore();
	store.reload();
}

/**
 * 清除选中
 * @param grid
 */
function clearSelect(grid){
	grid.getEl().select('div.x-grid3-row').each(function(i,v){
		v.removeClass('x-grid3-row-selected');
	});
}

/**
 * 获取grid选中列
 * @param grid
 * @returns
 */
function getSelectedEntity(grid) {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		return record.data;
	}
	return null;
}

//----------------------------------------------------------------------- 查询相关  start

/**
 * 显示简单查询
 */
function showSimpleQuery() {
	// 隐藏高级查询DIV
	$('#advance-toolbar-div').effect("drop", {
		times : 1
	}, 200, function() {
		// 显示快捷查询DIV
		$('#simple-toolbar-div').effect("slide", {
			times : 1
		}, 200);
	});
}

/**
 * 高级查询
 * @param cssSelector
 */
function showCommonAdvanceQuery(cssSelectors){
	// 隐藏简单查询DIV
	$('#simple-toolbar-div').effect("drop", {
		times : 1
	}, 200, function() {
		// 显示高级查询DIV
		$('#advance-toolbar-div').effect("slide", {
			times : 1
		}, 200,function(){
			//只允许执行一次
			if(WIN.SINGLE_RENDER_ADVANCE){
				return;
			}
			SINGLE_RENDER_ADVANCE = true;
			
			$.each(cssSelectors, function(i, v){
				var tds = $(v);
				$.each(tds, function(){
					$(this).hide();
				});
			});
			
			//标签选择
			window.tagStore = new Ext.data.JsonStore({
			    url: '/onu/loadOnuTags.tv',
			    root : 'data',
			    fields: ['id','tagName'],
			    listeners : {
					load : function(dataStore, records, options){
							var record = {id: -1, tagName: '@COMMON.select@'};
							var $record = new dataStore.recordType(record,"-1");
							dataStore.insert (0,[ $record ]);
					}
				}
			}); 
			
			window.tagCombo = new Ext.form.ComboBox({
			    id: 'tag',
			    applyTo : "onuTag",
			    width : 150,
				triggerAction : 'all',
				editable : true,
				lazyInit : false,
				emptyText : "@COMMON.select@",
				valueField: 'id',
			    displayField: 'tagName',
				store : tagStore
		    });
			
			//onu类型选择
		    window.typeStore = new Ext.data.JsonStore({
			    url: '/onu/loadOnuTypes.tv',
			    root : 'data',
			    fields: ['typeId','displayName'],
			    listeners : {
					load : function(dataStore, records, options){
						var record = {typeId: -1, displayName: '@COMMON.select@'};
						var $record = new dataStore.recordType(record,"-1");
						dataStore.insert (0,[ $record ]);
					}
				}
			});
		    window.typeCombo = new Ext.form.ComboBox({
			    id: 'type',
			    applyTo : typeSelect,
			    width : 150,
				triggerAction : 'all',
				editable : true,
				lazyInit : false,
				emptyText : "@COMMON.select@",
				valueField: 'typeId',
			    displayField: 'displayName',
				store : typeStore
		    });
		  
		    //加载olt对应的slot
			window.slotStore = new Ext.data.JsonStore({
				url : '/onu/getOltSlotList.tv',
				fields : ["slotId", "slotNoStr"],
				listeners : {
					load : function(dataStore, records, options){
						var entityId = $("#oltSelect").val();
						//只有选择了有效的设备才需要这样做
						if(entityId != null && entityId != -1){
							var record = {slotId: -1, slotNo: '@COMMON.select@'};
							var $record = new dataStore.recordType(record,"-1");
							dataStore.insert (0,[ $record ]);
						}
					}
				}
			});
			
			window.slotCombo = new Ext.form.ComboBox({
				id : "slotCombox",
				store : slotStore,
				applyTo : "slotSelect",
				mode : 'local',
				valueField : 'slotId',
				displayField : 'slotNoStr',
				emptyText : "@COMMON.select@",
				selectOnFocus : true,
				typeAhead : true,			
				triggerAction : 'all',
				editable : false,
				width : 150,
				enableKeyEvents: true,
				listeners : {
					keydown : function(textField, event){
						if( event.getKey() ==  8 ){
							event.stopEvent();
							return;
						}
					}
				}
			});
			
			//加载slot对应的pon
			window.ponStore = new Ext.data.JsonStore({
				url : '/onu/getOltPonList.tv',
				fields : ["ponId", "ponNo"],
				listeners : {
					load : function(dataStore, records, options){
						var slotValue = slotCombo.getValue();
						//只有选择了有效的板卡才需要这样做
						if(slotValue != null && slotValue != -1){
							var record = {ponId: -1, ponNo: '@COMMON.select@'};
							var $record = new dataStore.recordType(record,"-1");
							dataStore.insert (0,[ $record ]);
						}
					}
				}
			});
			
			window.ponCombo = new Ext.form.ComboBox({
				id : "ponCombox",
				store : ponStore,
				applyTo : "ponSelect",
				mode : 'local',
				valueField : 'ponId',
				displayField : 'ponNo',
				emptyText : "@COMMON.select@",
				selectOnFocus : true,
				typeAhead : true,			
				triggerAction : 'all',
				editable : false,
				width : 150,
				enableKeyEvents: true,
				listeners : {
					keydown : function(textField, event){
						if( event.getKey() ==  8 ){
							event.stopEvent();
							return;
						}
					}
				}
			});
			
			//当slot改变时加载对应的pon
			slotCombo.on('select', function(comboBox){
				//先清除加载的pon
				ponCombo.clearValue(); 
			  	//加载对应pon
				var slotValue = comboBox.getValue();
				ponStore.load({params: {slotId: slotValue}});
			})
			
			window.selector = new NetworkNodeSelector({
			    id: 'oltSelect',
			    renderTo: "oltContainer",
			    //value : window["entityId"], //@赋值的方式一：配置默认值 
			    autoLayout: true,
			    listeners: {
			      selectChange: function(){
			    	    //olt设备改变时加载对应的slot
			    	    //先清除加载的slot,pon
			    	  	slotCombo.clearValue(); 
			    	  	ponCombo.clearValue();
			    	  	//加载对应的slot
			    		var entityId = $("#oltSelect").val();
			    		slotStore.load({params: {entityId: entityId}});
			    		//这时候删除上次加载的pon口数据,否则上次加载的pon口列表还存在
			    		ponStore.removeAll();
			    	}
			    }
			});
			
			$('#partition').onuIndexPartition().bind('click', function(){
				window.top.createDialog("modalDlg",'@ONU/ONU.onuIndexPartition@',800, 370, '/onu/showPartitionSelect.tv?partitionData='+encodeURI(JSON.stringify(partitionData)), null, true,true);
			});
		});
	});
}

//----------------------------------------------------------------------- 查询相关  end

//----------------------------------------------------------------------- uni wan wlan 解析相关 start

/**
 * UNI port状态
 */
function convertPortStatus(value){
	if (value == 1) {
		return 'UP';
	} else {
		return 'DOWN';
	}
}

/**
 * vlan 模式
 * @param v
 * @returns {String}
 */
function convertVlanMode(v){
	switch(v || 1){
	case 0:return "@univlanprofile/PROFILE.modeNone@";
	case 1:return "@univlanprofile/PROFILE.modeTransparent@";
	case 2:return "@univlanprofile/PROFILE.modeTag@";
	case 3:return "@univlanprofile/PROFILE.modeTranslate@";
	case 4:return "@univlanprofile/PROFILE.modeAgg@";
	case 5:return "@univlanprofile/PROFILE.modeTrunk@";
	}
}

/**
 * 加密模式
 * @param value
 * @returns {String}
 */
function encryptMode(value) {
	switch (value) {
	case 0:
		return 'NONE';
	case 1:
		return 'WEP';
	case 2:
		return 'WPA';
	case 3:
		return 'WPA2';
	case 4:
		return 'WPA_WPA2';
	default:
		return 'NONE';
	}
}

//----------------------------------------------------------------------- uni wan wlan 解析相关 end

//----------------------------------------------------------------------- ONU关联信息相关 start

/**
 * 初始化关联信息部分
 */
function initAssociatePart(){
	$("#versionSideArrow").click(function() { //展开，折叠;
		if ($("#arrow").hasClass("versionSideArrLeft")) { //展开
			showHideSide("show");
		} else { //折叠;
			showHideSide("hide");
		}
	});
	autoSetArrPosition();
	$(window).resize(function() {
		autoSetArrPosition();
	});
	
	var paper = Raphael(document.getElementById("verticalTxt"), 19, 80), 
		txt = paper.text(9, 35, '@ONU.associateInfo@');
	txt.transform('0,0r90').attr({
		"fill" : "#0266B1",
		"font-weight" : "normal"
	});
}

/**
 * 展示隐藏关联信息
 * @param str
 */
function showHideSide(str) { //展开，折叠;
	switch (str) {
	case "show":
		$("#arrow").removeClass("versionSideArrLeft").addClass(
				"versionSideArrRight");
		$("#versionSideArrow").animate({
			right : 440
		});
		$("#versionSidePart").animate({
			right : 0
		});
		break;
	case "hide":
		$("#arrow").removeClass("versionSideArrRight").addClass(
				"versionSideArrLeft");
		$("#versionSideArrow").animate({
			right : 0
		});
		$("#versionSidePart").animate({
			right : -440
		});
		break;
	}
}

function autoSetArrPosition() { //设置右侧箭头在屏幕中间;
	var h = $(window).height(), h2 = (h - 140) / 2;
	if (h2 < 0) {
		h2 = 0;
	}
	$("#versionSideArrow").css({
		top : h2
	});
}

/**
 * 控制catv相关信息的显示隐藏
 */
function controlCatv(catvCapability){
	if(!catvCapability == 1){
		$(".catvCapability").hide();
		//下面两句是为了控制样式
		$('#catvCapability1').before($('#oltInfoContainer'));
		$('#catvCapability2').before($('#opticalInfoContainer'));
	} else {
		$(".catvCapability").show();
		//下面两句是为了控制样式
		$('#catvCapability1').after($('#oltInfoContainer'));
		$('#catvCapability2').after($('#opticalInfoContainer'));
	}
}

/**
 * 控制wifi相关信息显示隐藏
 * @param wlanCapability
 */
function controlWifi(wlanCapability){
	if(!wlanCapability == 1){
		$(".wlanCapability").hide();
		
	} else {
		$(".wlanCapability").show();
		
	}
}

/**
 * 控制关注显示隐藏
 * @param attention
 */
function controlAttention(attention){
	if(attention){
		$("li#attention").hide();
		$("li#cancelAttention").show();
	}else{
		$("li#attention").show();
		$("li#cancelAttention").hide();
	}
	
}

/**
 * 控制是否置灰  
 * @param selected
 */
function controlOperList(selected){
	if(!selected && !$("ol#operList").hasClass("disable-state")){
		$("ol#operList").addClass("disable-state");
	} else if(selected && $("ol#operList").hasClass("disable-state")){
		$("ol#operList").removeClass("disable-state");
	}
}

/**
 * 展示onu wifi信息
 * @param onuWanSsidList
 * @returns {String}
 */
function showOnuWifiInfo(onuWanSsidList, entityId, onuId, onuIndex){
	var onuWifiHtml = "";
	if(onuWanSsidList){
		for(var i=0; i<onuWanSsidList.length; i++){
			if(onuWanSsidList[i].ssidEnnable == 1){
				onuWifiHtml += '<tr>' + '<td> SSID' + onuWanSsidList[i].ssid + '</td><td>'
	    		+ onuWanSsidList[i].ssidName + '</td><td>'
	    		+ encryptMode(onuWanSsidList[i].encryptMode) + '</td><td>';
	    		onuWifiHtml += encryptMode(onuWanSsidList[i].encryptMode) == 'NONE' ? "<p style='display:block; width:50px'></p>" : String.format('<a class="yellowLink" href="javascript:showOnuWifiModifyView({0},{1},{2},{3})">@ONU.modifyPasswd@</a>', entityId, onuId, onuIndex, onuWanSsidList[i].ssid);
	    		onuWifiHtml +='</td><tr>'
			}
		}
	}
	return onuWifiHtml;
}

/**
 * 修改wifi信息
 * @param entityId
 * @param onuId
 * @param onuIndex
 * @param ssid
 */
function showOnuWifiModifyView(entityId, onuId, onuIndex, ssid){
	 window.top.createDialog('showOnuWifiModifyView', '@ONU.modifySSidNamePassword@', 600, 370,
			    '/onulist/showWifiPasswordModifyView.tv?onuId=' + onuId 
			    + '&pageId=' + window.parent.getActiveFrameId() + '&entityId=' + entityId
			    + '&ssid=' + ssid + '&onuIndex=' + onuIndex, null, true, true);
}

/**
 * 展示 epon ONU Vlan信息
 * @param uniPorts
 * @returns {String}
 */
function showEponOnuUniInfo(uniPorts){
	var uniPortsHtml = "";
	if(!uniPorts){
		return uniPortsHtml;
	}
	var length = uniPorts.length > 4 ? 4 : uniPorts.length;
	for(var i=0; i<length; i++){
		uniPortsHtml += '<tr>' 
			+ '<td> UNI' + uniPorts[i].uniNo + '</td><td>'
			+ convertPortStatus(uniPorts[i].uniOperationStatus) + '</td><td>'
			+ convertVlanMode(uniPorts[i].profileMode) + '</td><td>PVID:'
			+ uniPorts[i].bindPvid + '</td></tr>'
	}
	return uniPortsHtml;
}

/**
 * 展示 gpon ONU Vlan信息
 * @param uniPorts
 * @returns {String}
 */
function showGponOnuUniInfo(uniPorts){
	var uniPortsHtml = "";
	if(!uniPorts){
		return uniPortsHtml;
	}
	var length = uniPorts.length > 4 ? 4 : uniPorts.length;
	for(var i=0; i<length ; i++){
		uniPortsHtml += '<tr>' 
			+ '<td> UNI' + uniPorts[i].ethAttributePortIndex + '</td><td>'
			+ convertPortStatus(uniPorts[i].ethOperationStatus) + '</td><td>'
			+'@ONU.priority@：' + uniPorts[i].gponOnuUniPri + '</td><td>PVID:'
			+ uniPorts[i].gponOnuUniPvid + '</td></tr>'
	}
	return uniPortsHtml;
}

/**
 * ONU WAN信息
 * @param onuWanList
 * @param entityId
 * @param onuId
 * @param onuIndex
 * @returns {String}
 */
function showOnuWanInfo(onuWanList, entityId, onuId, onuIndex){
	var onuWanHtml = "";
	if(!onuWanList){
		return onuWanHtml;
	}
	for(var i=0; i<onuWanList.length; i++){
		onuWanHtml += '<tr>' + '<td>' + onuWanList[i].connectName + '</td><td>'
		+ onuWanList[i].ipModeString + '</td><td>'
		+ onuWanList[i].connectStatusString + '</td><td>';
		if(onuWanList[i].ipMode != 0){//对非桥接模式wan显示IP
			if(onuWanList[i].connectStatus == 1){
				if(onuWanList[i].ipMode == 3){//PPPoe模式提供修改密码按钮
					onuWanHtml += onuWanList[i].ipv4Address + '</td><td>'
					+ String.format('<a class="yellowLink" href="javascript:showOnuWanModifyView({0},{1},{2},{3})">@ONU.modifyPasswd@</a>', entityId, onuId, onuIndex, onuWanList[i].connectId) 
					+ '</td></tr>'
				}else{
					onuWanHtml += onuWanList[i].ipv4Address + '</td><td></td></tr>'
				}
			}else{//wan状态断开
				if(onuWanList[i].ipMode == 3){//PPPoe模式显示错误码提供修改密码按钮
					onuWanHtml += onuWanList[i].connectErrorCode + '</td><td>'
					+ String.format('<a class="yellowLink" href="javascript:showOnuWanModifyView({0},{1},{2},{3})">@ONU.modifyPasswd@</a>', entityId, onuId, onuIndex, onuWanList[i].connectId)
					+ '</td></tr>'
				}else{//其它模式不显示任何信息
					onuWanHtml += '</td><td></td></tr>'
				}
			}
		}else{//桥接模式下不显示任何信息
			onuWanHtml += '</td><td></td></tr>';
		}
	}
	return onuWanHtml;
}

/**
 * 修改wan信息
 * @param entityId
 * @param onuId
 * @param onuIndex
 * @param connectId
 */
function showOnuWanModifyView(entityId, onuId, onuIndex, connectId){
	window.top.createDialog('showOnuWanModifyView', '@ONU.modifyPPPoENamePassword@', 600, 370,
		    '/onulist/showWanPasswordModifyView.tv?onuId=' + onuId + '&connectId=' + connectId
		    + '&pageId=' + window.parent.getActiveFrameId() + '&entityId=' + entityId
		    + '&onuIndex=' + onuIndex, null, true, true);
}

//显示，隐藏loading;
function showLoading(b){
	var $loading = $('#versionSidePart .sideLoading');
	if(b){
		$loading.css({display: 'block'});
	}else{
		$loading.css({display: 'none'});
	}
}

function showOnuLocate(record) {
	  showLoading(true);
	  // 先把修改别名按钮重置一次
	  $('#modify').html("<a href='javascript:;' class='yellowLink' onclick ='modifyOnuName();' >@ONU.modifyAlias@</a>");
	  recordInfo = record;
	  var onuId = record.data.onuId;
	  $.ajax({
	    url: '/onulist/loadOnuLocationInfo.tv?onuId=' + onuId,
	    type: 'POST',
	    dateType: 'json',
	    success: function(json) {
	    	var disValue = json.onuLinkInfo.manageName+"("+json.onuLinkInfo.entityIp+")";
	    	var oltName = String.format('<a class="yellowLink" href="#" onclick=\'showOlt("' + recordInfo.data.entityId + '","' + escape(recordInfo.data.entityIp) + '");\'>{0}</a>',
	    	        disValue);
	    	$("#oltName").html(oltName);
	    	$("#ponIndex").text(json.oltPortOpticalInfo.portLocation);
	    	$("#inFlow").text(currentRateRender(json.oltPortOpticalInfo.inCurrentRate));
	    	$("#outFlow").text(currentRateRender(json.oltPortOpticalInfo.outCurrentRate));
	    	$("#onuNum").text(json.subCount.onuOnline+"/"+json.subCount.onuTotal);
	    	renderOnuPonRec0(json.onuLinkInfo.onuPonRevPower.toFixed(2),record);
	    	$("#testDistance").text(json.onuLinkInfo.onuTestDistance+"m");
	    	renderOnuPonTrans0(json.onuLinkInfo.onuPonTransPower.toFixed(2),record);
	    	renderOltPonRec0(json.onuLinkInfo.oltPonRevPower.toFixed(2),record);
	    	renderOltPonTrans0(json.onuLinkInfo.oltPonTransPower.toFixed(2),record);
	    	renderCATVRec0(json.onuLinkInfo.onuCatvOrInfoRxPower,record);
	    	var name = String.format('<a id="name" class="yellowLink" href="#" onclick=\'showOnuInfo("' + record.data.onuId + '","' + escape(record.data.name) + '");\'>{0}</a>', json.onuLinkInfo.name);
	    	$("#onuName").html(name);
	    	$("#onuType").text("("+json.onuLinkInfo.typeName+")");
	    	
	    	if(json.onuEorG == 'G'){
	    		$('#onuLabel').text("GPON SN:");
	    	}else{
	    		$('#onuLabel').text("MAC:");
	    	}
	    	$('#cpeNum').text(json.cpeNum);
	    	$('#onuUniq').text(json.onuUniqueIdentification);
	    	$('#onuLocation').text(json.location);
	    	
	    	//操作按钮取消置灰
	    	controlOperList(record);
	    	
	    	//显示激活或去激活
	    	isOnuActive(record);
	    	// 关注 取消关注
	    	controlAttention(json.attention);
	    	
	    	//catv wifi 
	    	controlCatv(json.catvCapability);
	    	controlWifi(json.wlanCapability);
	    	
	    	//ONU vlan信息
	    	if(json.eponUniPorts && json.eponUniPorts.length > 0){
		    	$('#portVlan').html(showEponOnuUniInfo(json.eponUniPorts));
	    	}else{
	    		$('#portVlan').html(showGponOnuUniInfo(json.gponUniPorts));
	    	}
	    	
	    	//ONU WAN信息
	    	var onuId = record.data.onuId, entityId = record.data.entityId, onuIndex = record.data.onuIndex;
	    	$('#onuWan').html(showOnuWanInfo((json.onuWanConnectList), entityId, onuId, onuIndex));
	    	
	    	//ONU WIFI信息
	    	$('#onuWifi').html(showOnuWifiInfo(json.onuWanSsidList, entityId, onuId, onuIndex));
	    },
	    error: function() {

	    },
	    complete: function(){
	    	showLoading(false);
	    },
	    cache: false
	  });
	}

function modifyOnuName(){
	var onuName = $('#onuName').text();
	$('#onuName').html("<input value="+onuName+" id='modifyOnuName' toolTip='@ONU.aliasTip@'></input>");
	$('#modify').html("<a href='javascript:;' class='yellowLink' onclick ='saveOnuName();' >@COMMON.save@</a>")
}

function isOnuActive(record) {
	var onuEorG = record.data.onuEorG;
	if(onuEorG == "E"){
		$("#onuActive").html('<i class="miniIcoWrong"></i>@ONU.unregister@');
		return;
	}else{
		var onuDeactiveTemp = record.data.onuDeactive == 1 ? 2 : 1;
		if(onuDeactiveTemp == 1){
			$("#onuActive").html('<i class="miniIcoWrong"></i>@COMMON.active@');
			return;
		}else{
			$("#onuActive").html('<i class="miniIcoWrong"></i>@COMMON.deactive@');
			return;
		}
	}
}

function saveOnuName() {
	var onuId = recordInfo.data.onuId;
	name = $("#modifyOnuName").val();
	if (!Validator.isAnotherName(name)) {
		$("#modifyOnuName").focus();
		return;
	}
	$.ajax({
			url : '/entity/changeEntityInfoByEntityId.tv',
			data : {
				entityId : onuId,
				name : name,
			},
			type : 'post',
			dataType : "json",
			success : function(response) {
				name = String.format('<a id="name" class="yellowLink" href="#" onclick=\'showOnuInfo("' + onuId + '","' + escape(name) + '");\'>{0}</a>', name);
				$('#onuName').html(name);
				$('#modify').html("<a href='javascript:;' class='yellowLink' onclick ='modifyOnuName();' >@ONU.modifyAlias@</a>");
				refresh();
				top.afterSaveOrDelete({
							title : '@COMMON.tip@',
							html : '<b class="orangeTxt">@resources/COMMON.modifySuccess@</b>'
						});
			},
			error : function(response) {
			},
			cache : false
		});
}

//----------------------------------------------------------------------- ONU关联信息相关 end