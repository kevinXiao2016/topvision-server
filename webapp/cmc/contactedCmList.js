var refreshCmFormatStr = "<a class='yellowLink' href='javascript:;' onclick='refreshCmInfo(\"{0}\",\"{1}\")' >@route.button.refresh@</a>";
var restartCmFormatStr = "<a class='yellowLink' href='javascript:;' onclick='restartCm(\"{0}\",\"{1}\",\"{2}\",\"{3}\")'/>@CCMTS.cm.reset@</a>";
var otherOperFormatStr = "<a class='withSub mR10' href='javascript:;' onclick='showMoreOperation({0},event)'>@TIP.other@</a>";
/**
 * 重启选中CM
 */
function restartCm(){
	var sm = grid.getSelectionModel();
    if (sm == null || !sm.hasSelection()) {
    	return window.parent.showMessageDlg("@COMMON.tip@", "@text.pleaseSelectCm@");
    }
    //获取选中的cmId
    var selections = sm.getSelections(), 
    	cmIds = [], 
    	finishedCmIds = [],
    	dwrId = 'refreshOnLineCm_'+cmcId,
    	successNum = 0,
    	failedNum = 0,
    	refreshedCmCounter = 0,
    	existOffline = false;
    for (var i = 0, len=selections.length; i < len; i++) {
    	if(isCmOnline(selections[i].data.statusValue)) {
    		cmIds.push(selections[i].data.cmId);
    	} else {
    		existOffline = true;
    		break;
    	}
    }
    if(existOffline) {
    	return window.parent.showMessageDlg("@COMMON.tip@", "@text.cannotRestartOffline@");
    }
    //初始化
    initBatch();
    //DWR响应
    window.top.addCallback("refreshAllOnLineCm",function(json) {
    	//重置计时器
    	clearTimeout(timeoutTimer);
    	timeoutTimer = setTimeout(function(){
        	unexpectedEnd();
        }, 60000);
        //找到对应的行
        for (var j = 0; j < store.getCount(); j++) {
          if (store.getAt(j).get('cmId') == json.cmId) {
        	  finishedCmIds.push(json.cmId);
        	  //根据成功还是失败来改变颜色
        	  	if (json.success == true) {
        	  		//重启成功后，将在线状态置为下线
        	  		grid.getView().removeRowClass(j, 'yellow-row');
        	  		grid.getView().addRowClass(j, 'white-row');
        	  		var record = store.getAt(j);
        	  		record.beginEdit();
        	  		record.set('statusValue', 1);
        	  		record.commit();
        	  		record.endEdit();
        	  		successNum++;
        	  		$('#suc-num-dd').text(successNum);
                } else {
                	grid.getView().removeRowClass(j, 'yellow-row');
                	grid.getView().addRowClass(j, 'red-row');
                	failedNum++;
                	$('#fail-num-dd').text(failedNum);
                }
          	}
        }
        loadCmNumUnderCcmts();
        //判断是否完成本次推送
        refreshedCmCounter++;
        if (refreshedCmCounter >= cmIds.length) {
          //提示成功，并清除超时设置
        	clearTimeout(timeoutTimer);
        	outputResult();
        }
      }, dwrId);
    
    //发送请求
    $.post('/cmlist/resetOnLineCm.tv',{
    	cmIds: cmIds.join(','),
    	dwrId: dwrId
    });
    //发送完请求后开启计时器，如果超时未返回，则认为本次批量结束，未返回的均统计为失败
    timeoutTimer = setTimeout(function(){
    	unexpectedEnd();
    }, 60000);
    
    //批量初始化
    function initBatch(){
  	  //初始化统计数据
  	  $('#suc-num-dd').text(0);
  	  $('#fail-num-dd').text(0);
  	  //将各行标记为正在刷新
  	  for (var j = 0; j < store.getCount(); j++) {
  	    if($.inArray(store.getAt(j).get('cmId'), cmIds)!=-1){
  	      grid.getView().addRowClass(j, 'yellow-row');
  	    }
  	  }
  	  //禁用查询功能、刷新功能、翻页功能
  	  tbar.setDisabled(true);
  	  bbar.setDisabled(true);
  	  //展示等待框
  	  showLoading("@ccm/CCMTS.CmList.restartingCm@");
    }
    
    //批量操作意外中止处理函数
    function unexpectedEnd(){
    	clearTimeout(timeoutTimer);
    	//取消dwr推送接收
    	window.top.removeCallback("refreshAllOnLineCm", dwrId);
    	var curCmIndex;
    	//将正在处理行全部置为失败
    	for (var j = 0; j < store.getCount(); j++) {
      	    if($.inArray(store.getAt(j).get('cmId'), cmIds)!=-1
      	    	&& $.inArray(store.getAt(j).get('cmId'), finishedCmIds)==-1){
      	      grid.getView().addRowClass(j, 'red-row');
      	    }
  	  	}
    	//刷新成功数为successNum，failedNum更新为len-failedNum
    	failedNum = len - successNum;
    	//展示结果
    	outputResult();
    	loadCmNumUnderCcmts();
    }
    
    function outputResult(){
    	//启用查询功能、刷新功能、翻页功能
    	tbar.setDisabled(false);
    	bbar.setDisabled(false);
    	//隐藏等待框
    	hideLoading();
    	//显示结果
    	$('#suc-num-dd').text(successNum);
    	$('#fail-num-dd').text(failedNum);
    	top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@ccm/CCMTS.CmList.resetCmComplete@<br/>@CMC.tip.successfully@' + successNum + '@CCMTS.view.cmNumOffline.unit@,@CMC.label.failure@' + failedNum + '@CCMTS.view.cmNumOffline.unit@</b>'
        });
    }
}

function restartAllCm(){
	window.parent.showConfirmDlg("@COMMON.tip@", "@text.confirmRestart@", function(button,text){
        if(button == "yes"){
            window.top.showWaitingDlg("@COMMON.waiting@", "@cmList.resetting@", 'ext-mb-waiting');
            $.post("/cm/restartAllCm.tv?cmcId=" + cmcId, function(data){
            	window.top.closeWaitingDlg();                      
            	if(data.success){
            		store.reload();
            		top.afterSaveOrDelete({
            			title: '@COMMON.tip@',
            			html: '<b class="orangeTxt">@CMC.tip.resetSuccess@</b>'
            		});
            	}else{
            		if(!data.restart){
            			//<!--如果重启失败-->
            			window.parent.showConfirmDlg('@COMMON.tip@', "@text.resartError@",function(button,text){
            				if(button == "yes"){
            					window.parent.showWaitingDlg("@COMMON.waiting@", '@text.refreshCmTip@', 'waitingMsg','ext-mb-waiting');
            					$.ajax({
            						url: 'refreshCmOnCcmtsInfo.tv?cmcId=' + cmcId ,
            						type: 'post',
            						success: function(response) {
            							if(response == "true"){
            								store.reload();  
            								window.parent.showMessageDlg('@COMMON.tip@', "@text.refreshSuccessTip@");
            							}else{
            								window.parent.showMessageDlg('@COMMON.tip@', "@text.refreshFailureTip@");
            							}
            						}, error: function(response) {
            							window.parent.showMessageDlg('@COMMON.tip@', "@text.refreshFailureTip@");
            						}, cache: false
            					});  
            				}
            			});
            		}else{
            			//<!--刷新失败-->
            			window.parent.showMessageDlg('@COMMON.tip@', "@cmc.cm.restartSucRefreshFail@");
            		}
            	}
            });
        }
    });
}

/**
 * 刷新信号质量
 */
function refreshCmSignal(){
	var random = randomInteger(0, 100);
	refreshCmSignalQuality('cmtscm_refreshCmSignal_' + random, store, false)
}

/**
 * 从设备获取数据
 */
function loadEntityData(){
    window.top.showOkCancelConfirmDlg("@COMMON.tip@", "@text.refreshCmConfirm@", function (type) {
        if(type=="ok"){
            window.parent.showWaitingDlg("@COMMON.waiting@", "@text.refreshCmTip@", 'waitingMsg','ext-mb-waiting');
            $.post('refreshCmOnCcmtsInfo.tv?cmcId=' + cmcId, function(response){
              window.top.closeWaitingDlg();
              if(response == "true"){
                    store.reload();  
                    top.afterSaveOrDelete({
                      title: '@COMMON.tip@',
                      html: '<b class="orangeTxt">@text.refreshSuccessTip@</b>'
                  });
                 }else{
                     window.parent.showMessageDlg("@COMMON.tip@", "@text.refreshFailureTip@");
                 }
            });
        }
    });
}

/**
 * 清除离线CM
 */
function clearOfflineCm(){
	window.parent.showConfirmDlg("@COMMON.tip@", "@CCMTS.clearOfflineCM.confirmToClear@",function(button,text){
        if(button == "yes"){
            window.top.showWaitingDlg("@COMMON.waiting@", "@CMC.tip.clearCmIng@", 'ext-mb-waiting');
            $.post("/cm/offlineCmAll.tv?cmcId=" + cmcId, function(data){
            	window.top.closeWaitingDlg();
            	if(data.success){
                    top.afterSaveOrDelete({
                        title: '@COMMON.tip@',
                        html: '<b class="orangeTxt">@CCMTS.clearOfflineCM.success@</b>'
                    });
                    store.load();
                }else{
                    window.parent.showMessageDlg("@COMMON.tip@", "@CCMTS.clearOfflineCM.fail@");
                }
            });
        }
    });
}

function clearThisCM(){
	var sm = grid.getSelectionModel();
    if (sm == null || !sm.hasSelection()) {
    	return window.parent.showMessageDlg("@COMMON.tip@", "@text.pleaseSelectCm@");
    }
    //获取选中的cmId
    var selections = sm.getSelections(), 
    	cmIds = [];
    for (var i = 0, len=selections.length; i < len; i++) {
    	cmIds.push(selections[i].data.cmId);
    }
	window.parent.showConfirmDlg("@COMMON.tip@", "@CCMTS.clearSomeOfflineCM.confirmToClear@",function(button,text){
        if(button == "yes"){
            window.top.showWaitingDlg("@COMMON.waiting@", "@CMC.tip.clearCmIng@", 'ext-mb-waiting');
            $.post("/cmlist/clearSingleCM.tv?cmIds=" + cmIds, function(data){
            	window.top.closeWaitingDlg();
            	if(data.success){
            		var successNum=data.clearSuccess;
            		var failedNum=selections.length-successNum;
                    top.afterSaveOrDelete({
                        title: '@COMMON.tip@',
                        html: '<b class="orangeTxt">@ccm/CCMTS.CmList.clearSingleCmCmComplete@<br/>@CMC.tip.successfully@:' + successNum + '@CCMTS.view.cmNumOffline.unit@,@CMC.label.failure@:' + failedNum + '@CCMTS.view.cmNumOffline.unit@</b>'
                    });
                    store.reload();
                    sm.clearSelections();
                }else{
                    window.parent.showMessageDlg("@COMMON.tip@", "@CCMTS.clearOfflineCM.fail@");
                }
            });
        }
    });
}

/**
 * 加载CM数目信息
 */
function loadCmNumUnderCcmts(){
	$.get('/cmlist/loadCmNumUnderCcmts.tv', queryData, function(data){
		$('#totalNum').text(data.totalNum);
		$('#onlineNum').text(data.onlineNum);
	});
}

function showLoading(message) {
	$("#loading").text(message);
	var lPos = ($(window).width() - $("#loading").outerWidth()) / 2;
	var tPos = 150;
	$("#loading").show().css({
		top: tPos,
		left: lPos
	});
}

function hideLoading() {
	$("#loading").hide();
}
function showCMFlapChart(cmMac){
  window.top.createDialog('cmFlapDemo', I18N.cmc.flap.CMflapHistory, 800, 600, 
      '/cmflap/showOneCMFlapHisChart.tv?cmMac='+cmMac , null, true, true);
}

/* ==========================================================
 * 以下内容是其他菜单中的的方法(CM上下线行为、CPE信息、查看大客户IP信息、所在OLT/PON/CCMTS/上行/下行下CM信息等)
 * ========================================================== */

//YangYi Add @ 2013-11-15 Ping CM

function pingCm() {
  var sm = grid.getSelectionModel(),
      record = sm.getSelected(),
      cmIp = record.data.displayIp;
      cmcId = record.data.cmcId;
  if (cmIp) {
	  //if(cmPingMode == 2 && EntityType.isCcmtsWithAgentType(cmcDeviceStyle)) {
	  if(cmPingMode == 2){
	      window.parent.createDialog("modalDlg", 'Ping' + " - " + cmIp, 600, 400, 
      	         "entity/snmpPing.tv?cmd=snmpping&entityId="+cmcId+"&ip=" + cmIp, null, true, true);
      }
	  else{
		  window.parent.createDialog("modalDlg", 'Ping' + " - " + cmIp, 600, 400,
		          "entity/runCmd.tv?cmd=ping&ip=" + cmIp, null, true, true);
	  }
  }
}
//YangYi Add @ 2013-11-15 Trace Route

function traceRoute() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected(),
    cmIp = record.data.displayIp;
  if (cmIp) {
    window.top.createDialog("modalDlg", 'Tracert ' + cmIp, 600, 400,
      "entity/runCmd.tv?cmd=tracert&ip=" + cmIp, null, true, true);
  }
}
//YangYi Add @ 2013-11-15 MibbleBrowser

function onMibbleBrowserClick() {
  var sm = grid.getSelectionModel(),
      record = sm.getSelected(),
      cmIp = record.data.displayIp,
      mibbleType = 35000,
  	  enterCount = 1;
  
  /*if (cmIp) {
    window.top.addView("cmMibbleBrowser", "CM MIB Browser", null, "/mibble/showMibbleBrowser.tv?host=" + cmIp + "&mibbleType=" + mibbleType, null, true);
  } else {
    window.top.addView("cmMibbleBrowser", "CM MIB Browser", null, "/mibble/showMibbleBrowser.tv?mibbleType=" + mibbleType, null, true);
  }*/
  window.top.addView('mibbleBrowser', 'MIB Browser','entityTabIcon', '/mibble/showMibbleBrowser.tv?host=' + cmIp + '&mibbleType=' + mibbleType + '&enterCount=' + enterCount,null, true);
}

function onCmHistoryClick(){
	 var sm = grid.getSelectionModel(),
	     record = sm.getSelected(),
	     cmId = record.data.cmId,
	     cmIp = record.data.displayIp,
	     cmMac = record.data.statusMacAddress;
	 window.top.addView("showCmSignalHistory", '@cmList.cmhistory@', "icoG7", "/cmHistory/showCmHistory.tv?cmId="+cmId+"&cmIp="+cmIp+"&cmMac="+cmMac,null,true);
}

function showCmAction() {
	  var sm = grid.getSelectionModel(),
	    record = sm.getSelected(),
	    cmId = record.data.cmId;
	  window.top.createDialog("cmAction", '@cmList.cmAction@', 600, 370, "/cm/showCmActionInfo.tv?cmId=" + cmId, null, true, true);
	}

	function editCmImportInfo() {
	  var sm = grid.getSelectionModel(),
	    record = sm.getSelected(),
	    cmMacAddress = record.data.statusMacAddress;
	  window.top.createDialog('modifyCmProperty', '@text.modifyCmImportInfo@', 600, 370,
	    'cm/showCmInfoConfig.tv?cmMac=' + cmMacAddress, null, true, true, function() {
		  store.reload();
	  });
	}

	function showCpeAction() {
	  var sm = grid.getSelectionModel(),
	    record = sm.getSelected(),
	    cmId = record.data.cmId;
	  window.top.createDialog("cpeAction", '@cmList.cpeAction@', 600, 370, "/cm/showCpeActionInfo.tv?cmId=" + cmId, null, true, true);
	}

	function showCmCpeInfo() {
	  var sm = grid.getSelectionModel(),
	    record = sm.getSelected(),
	    cmId = record.data.cmId;
	  window.top.createDialog("cmCpeInfo", '@cm.cpeInfo@', 600, 370, "/cmlist/showCmCpeInfo.tv?cmId=" + cmId, null, true, true);
	}

	function showCmStaticIpInfo() {
	  var sm = grid.getSelectionModel(),
	    record = sm.getSelected(),
	    cmId = record.data.cmId;
	  window.top.createDialog("cmStaticIpInfo", '@cm.cmStaticIp@', 600, 370, "/cm/showCmStaticIpInfo.tv?cmId=" + cmId, null, true, true);
	}
	
	function renderTime(value){
		if(value==null){return '-';}
		var date = new Date();
		date.setTime(value.time);
		return Ext.util.Format.date(date, 'Y-m-d H:i:s');
	}

	function renderOperation(value, p, record) {
		  var statusMacAddress = record.data.statusMacAddress,
		    cmIp = record.data.displayIp,
		    cmId = record.data.cmId,
		    status = record.data.docsIfCmtsCmStatusValueString,
		    cmcId = record.data.cmcId,
		    status = record.data.statusValue,
		    returnStr = "";
		  
		  if(!refreshDevicePower){
		      returnStr += "<span>@route.button.refresh@</span> / ";
		  }else{
		      returnStr += String.format(refreshCmFormatStr, statusMacAddress, cmcId) + " / ";
		  }
		  
		  if (!isCmOnline(status)) {
		    //离线状态的CM，只有刷新CM和其他操作
		    returnStr += String.format(otherOperFormatStr, cmId);
		    return returnStr;
		  } else {
		    //在线状态的CM
		    if (operationDevicePower) {
		      //如果有操作设备的权限
		      if(record.data.supportReset==true){
		    	  returnStr += String.format(restartCmFormatStr, statusMacAddress, cmIp, cmId, status) + " / ";
		      }
		      returnStr += String.format(otherOperFormatStr, cmId);
		      return returnStr;
		    } else {
		      //如果没有操作设备的权限
		      returnStr += String.format(otherOperFormatStr, cmId);
		      return returnStr;
		    }
		  }
		}
	
	function refreshCmInfo(a,b,c,d,e) {
		  var sm = grid.getSelectionModel(),
		    record = sm.getSelected();
		  if (record == null) {
		    return;
		  }
		  window.top.showWaitingDlg('@COMMON.waiting@', '@CMC.ipqam.doingRefreshDevice@', 'ext-mb-waiting');
		  var cmMac = record.data.statusMacAddress,
		    cmcId = record.data.cmcId,
		    cmId = record.data.cmId,
		    statusIndex = record.data.statusIndex,
		    cmIp = record.data.displayIp,
		    cmClassified = record.data.cmClassified,
		    cmAlias = record.data.cmAlias,
		    cmcDeviceStyle = record.data.cmcDeviceStyle,
		    rowIndex = store.indexOf(record);
		  $.ajax({
		    url: '/cmlist/refreshCm.tv',
		    data: {
		      cmMac: cmMac,
		      cmId: cmId,
		      cmIp: cmIp,
		      cmcId: cmcId,
		      statusIndex: statusIndex,
		      cmcDeviceStyle: cmcDeviceStyle
		    },
		    type: 'post',
		    dataType: "json",
		    success: function(json) {
		      if (json != null) {
		        if (json.success) {
		          window.top.closeWaitingDlg();
		          top.afterSaveOrDelete({
		            title: '@COMMON.tip@',
		            html: '<b class="orangeTxt">@CMC.tip.refreshSuccess@</b>'
		          });
		          //window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.refreshSuccess@');
		          //更新该行数据
		          var cmAttribute = json.cmAttribute;
		          record.beginEdit();
		          for (var key in cmAttribute) {
		        	  //非版本控制内容则更新
		        	  if($.inArray(key, ['supportStaticIp', 'supportCpeInfo', 'supportReset','supportCmUpgrade','supportClearSingleCm']) ===-1){
		        		  cmAttribute[key] && record.set(key, cmAttribute[key]);
		        	  }
		          }

		          record.commit();
		          //更新CM数量信息
		          loadCmNumUnderCcmts();
		          var cls = expander.state[record.id] ? 'x-grid3-row-expanded' : 'x-grid3-row-collapsed';
		          if (cls == 'x-grid3-row-expanded') {
		              expander.expandRow(rowIndex);
		          }
		        } else {
		          window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.refreshFailure@');
		        }
		      }
		    },
		    error: function(response) {},
		    cache: false,
		    complete: function(XHR, TS) {
		      XHR = null
		    }
		  });
		}
	
	function showMoreOperation(cmId, event) {
		  var record = grid.getStore().getById(cmId),
		    status = record.data.statusValue;
		  grid.getSelectionModel().selectRecords([record]);
		  //判断应该屏蔽哪些菜单
		  var cmcDeviceStyle = record.data.cmcDeviceStyle;
		  if (EntityType.isCmtsType(cmcDeviceStyle)) {
			  Ext.getCmp("cmMenu").findById("cpeInfo").setDisabled(true);
			  Ext.getCmp("cmMenu").findById("showCmStaticIp").setDisabled(true);
		  } else {
		      Ext.getCmp("cmMenu").findById("cpeInfo").setDisabled(false);
			  Ext.getCmp("cmMenu").findById("showCmStaticIp").setDisabled(false);
		  }
		  
		  //版本控制
		  if(!EntityType.isCmtsType(cmcDeviceStyle)){
			  if(record.data.supportStaticIp==false){
				  Ext.getCmp("cmMenu").findById("showCmStaticIp").hide();
			  }else{
				  Ext.getCmp("cmMenu").findById("showCmStaticIp").show();
			  }
			  if(record.data.supportCpeInfo==false){
				  Ext.getCmp("cmMenu").findById("cpeInfo").hide();
			  }else{
				  Ext.getCmp("cmMenu").findById("cpeInfo").show();
			  }
			  if(record.data.supportCmUpgrade==false){
				  Ext.getCmp("cmMenu").findById("cmUpgrade").hide();
			  }else{
				  Ext.getCmp("cmMenu").findById("cmUpgrade").show();
			  }
			  if(record.data.supportClearSingleCm==false){
				  Ext.getCmp("cmMenu").findById("clear").hide();
			  }else{
				  Ext.getCmp("cmMenu").findById("clear").show();
			  }
		  }
		  
		  entityMenu.showAt([event.clientX - 150, event.clientY]);
		}
