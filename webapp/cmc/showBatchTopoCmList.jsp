<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
		<%@include file="../include/ZetaUtil.inc"%>
		<Zeta:Loader>
			library ext
		    library jquery
		    library zeta
		    module cmc
		    import cm.util.cmUtil
		</Zeta:Loader>
		<style type="text/css">
			body,html{ height:100%;}
			.normalTable .yellow-row{background-color:#DCD345 !important;}
			.normalTable .red-row{background-color:#C07877 !important;}
			.normalTable .green-row{background-color:#26B064 !important;}
			.normalTable .white-row{background-color:#FFFFFF !important;}
			#loading {
				padding: 5px 8px 5px 26px;
				border: 1px solid #069;
				background: #F1E087 url('../images/refreshing2.gif') no-repeat 2px center;
				position: absolute;
				z-index: 999;
				top: 0px;
				left: 0px;
				display: none;
				cursor: pointer;
				font-weight: bold;
			}
		</style>
		<script type="text/javascript">
		var refreshCmFormatStr = "<a class='yellowLink' href='javascript:;' onclick='refreshCmInfo(\"{0}\",\"{1}\")' >@route.button.refresh@</a>";
		var restartCmFormatStr = "<a class='yellowLink' href='javascript:;' onclick='restarCm(\"{0}\",\"{1}\",\"{2}\",\"{3}\")'/>@CCMTS.reset@</a>";
		var otherOperFormatStr = "<a class='withSub mR10' href='javascript:;' onclick='showMoreOperation({0},event)'>@TIP.other@</a>";
		var refreshedCmCounter; //已刷新CM计数器
		var responseTimer; //Action回复的定时器，如果超时将页面上恢复到刷新前
		var dwrResponseTimer; //DWR回复的定时器，如果超时将页面上恢复到刷新前
		var cmNumDivStr = '<div id="cm-num-div">'
		  .concat('<label>@CHANNEL.totalCmNum@</label><span style="margin-right:10px;" id="cm-total-num-span">0</span>')
		  .concat('<label>@CM.regestNum@</label><span id="cm-online-num-span">0</span>')
		  .concat('</div>');
		var pageSize = <%= uc.getPageSize() %>;
		var grid = null;
		var store = null;
		var pagingToolbar = null;
		var ipSegment = '${ipSegment}';
		var cmcId = '${cmcId}';
		var w = ($(WIN).width() >= $(DOC.body).width()) ? $(WIN).width() : $(document.body).width() - 35;
		var tbar;
		var bbar;
		var chlDisplayMode = 'all';
		var sysElecUnit = '@{unitConfigConstant.elecLevelUnit}@';
		
		$(function(){
			var cm =  new Ext.grid.ColumnModel([
			    {header: "IP", width: 100, sortable: false, align: 'center', dataIndex: 'displayIp', renderer: renderIp}, 
    			{header: "MAC", width: 115, sortable: false, align: 'center', dataIndex: 'statusMacAddress', renderer: renderMac}, 
    			{header: "@CMC.title.status@", width: 40, sortable: false, align: 'center', dataIndex: 'statusValue', renderer: CmUtil.statusValueRender}, 
    			{header: "@CM.docsicVersion@", width: 100, sortable: false, align: 'center', dataIndex: 'docsisMode',renderer: renderDocsisMode}, 
    			{header: "@CMCPE.upDevice@", width: 90, sortable: false, align: 'center', dataIndex: 'cmcName'}, 
   				{header: "@cmcUserNum.lastCollectTime@", width: 127, sortable: false, align: 'center', dataIndex:'lastRefreshTime'},
    			{header: "@upchannel@", width: 62, sortable: false, align: 'center', dataIndex: 'statusUpChannelIfIndex', renderer: renderUpChannel}, 
    			{header: "@CCMTS.downStreamChannel@", width: 62, sortable: false, align: 'center', dataIndex: 'statusDownChannelIfIndex',renderer: renderDownChannel}, 
    			{header: "@CM.upSnr@", width: 62, sortable: false, align: 'center', dataIndex: 'statusSignalNoise', renderer: renderUpChannelSnr}, 
    			{header: "@CM.downSnr@", width: 62, sortable: false, align: 'center', dataIndex: 'downChannelSnr', renderer: renderDownChannelSnr}, 
   				{header: "@CM.upSendPower@", width: 0.08 * w, sortable: false, align: 'center', dataIndex: 'upChannelTransPower', renderer: renderUpChannelTx}, 
    			{header: "@CM.downReceivePower@", width: 0.08 * w, sortable: false, align: 'center', dataIndex: 'downChannelRecvPower', renderer: renderDownChannelTx}, 
    			{header: 'FLAP Ins', width: 0.050 * w, sortable: false, align: 'center', renderer: renderFlapChart, dataIndex: 'topCmFlapInsertionFailNum'}, 
    			{header: "<div class='txtCenter'>@VLAN.vlanOpera@</div>", width: 165, fixed: true, sortable: false, dataIndex: 'cmClassified',renderer: renderOperation}
			]);
		   
			tbar = new Ext.Toolbar({
				 items: [
				      {text: '@ccm/CCMTS.CmList.resetOnlineCm@', id: 'refreshAllCm_button', disabled: !operationDevicePower, iconCls: 'bmenu_restart', handler: restartAllCm}, 
				      '-', 
				      {text: '@contactCmList.refreshCmSq@', id: 'refreshCmSignal_button', iconCls: 'bmenu_refresh', handler: refreshCmSignal}, 
				      '->', 
				      {xtype: 'tbtext', text: cmNumDivStr}
				   ]
			});
			bbar =  buildPagingToolBar();
			WIN.DEFAULT_STORE_PARAM = {start:0, limit: pageSize,deviceType : ${deviceType} ,cmcId : ${cmcId},queryModel:'simple'};
			store = new Ext.data.JsonStore({
				url: '/cmlist/loadCmList.tv',
			    root: 'data',
			    totalProperty: 'rowCount',
			    idProperty: "cmId",
			    autoLoad : true,
			    baseParams : DEFAULT_STORE_PARAM,
			    remoteSort: true,
			    fields: [
			      'oltId', 'ponId', 'cmId', 'cmcId', 'cmcName','displayIp','cmcDeviceStyle', 'upChannelIndexString', 'downChannelIndexString',
			      'docsIfCmtsCmStatusInetAddressTypeString', 'cmAlias', 'cmClassified', 'topCmFlapInsertionFailNum', 'downChannelId',
			      'statusIpAddress', 'statusMacAddress', 'statusDownChannelIfIndex', 'statusUpChannelIfIndex', 'docsIf3CmtsCmUsStatusList',
			      'docsIfSigQSignalNoiseForUnit', 'docsIfCmtsCmStatusRxPowerString', 'statusSignalNoise', 'downChannelSnr',
			      'downChannelTx', 'upChannelTx', 'docsIfCmtsCmStatusValueString', 'statusValue', 'upChannelId', 'upChannelSnr',
			      'lastRefreshTime', 'downChannelFrequency', 'upChannelFrequency', 'collectTime', 'statusIndex', 'supportStaticIp',
			      'downChannelRecvPower','upChannelTransPower', 'cmSignal', 'upChannelCm3Signal', 'downChannelCm3Signal', 'preStatus', 'docsisMode'
			    ]
			});
			store.on("load",updateCmNum);
			
		   	grid = new Ext.grid.EditorGridPanel({
		   		stripeRows:true,
		   		region: 'center',
		   		bodyCssClass: 'normalTable',
		   		border:false,
		    	enableColumnMove: true,
		   		store: store,
		   		cm : cm,
		   		tbar: tbar,
		   		bbar: buildPagingToolBar(),
		   		selModel : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
		   		viewConfig:{
		   			forceFit: false
		   		}
		   	});
		   	
			new Ext.Viewport({
			    layout: 'border', items: [grid]
			});
		});// end document.ready;
		
		// 显示实时信息
		function showRealTimeInfo(cmcId,name){
		    top.addView('entity-realTime' + cmcId, name, 'entityTabIcon',
		            '/cmc/showCmcRealTimeData.tv?cmcId=' + cmcId);
		}
		function updateCmNum() {
			$.ajax({
				url:'/cmlist/loadCmNum.tv',data: DEFAULT_STORE_PARAM,dataType:'json',
				success:function(json){
					$('#cm-total-num-span').text(json.cmTotalNum);
					$('#cm-online-num-span').text(json.cmOnlineNum);
				}
			})
		}
		
		function renderIp(value) {
			  if (value == null || value == "" || value == "noSuchObject" || value == "noSuchInstance" || value == "0.0.0.0") {
			    return "--"
			  } else {
			    // return value;
			    return String.format('<a href="http://{0}" target="_blank">{0}</a>', value);
			  }
		}
		function renderMac(value, p, record){
			var mac = record.data.mac;
			var totalNum = record.data.totalNum || 0;
			var onlineNum = record.data.onlineNum || 0;
			if(totalNum == null || totalNum == ""){
				totalNum = 0;
			}
			if(onlineNum == null || onlineNum == ""){
				onlineNum = 0;
			}
			
			if(totalNum == onlineNum){
				return mac +'(<span class="greenTxt">'+ onlineNum +'</span>/<span class="redTxt">'+totalNum+'</span>)';
			}else{
				return mac +'(<span class="differentNun">'+ onlineNum +'/'+totalNum+'</span>)';
			}
		};
		
		function renderMac(value, p, record) {
			  if (value != "") {
			    return String.format('<a href="javascript:;" onclick="showCmDetail(\'{0}\')">{0}</a>', value);
			  } else {
			    return value;
			  }
			}

			function showCmDetail(macAddr) {
				  var sm = grid.getSelectionModel(),
				    record = sm.getSelected();
				  if (record == null) {
				    return;
				  }
				  var cmMac = macAddr || record.data.statusMacAddress;
				  window.top.addView("CmCpeQuery", '@ccm/CCMTS.CmList.cmCpeQuery@', "mydesktopIcon", "/cmCpe/showCmCpeQuery.tv?sourcePage=cmListPage&cmMac=" + cmMac, null, true);
				}


			function renderFlapChart(value, p, record) {
			  var cmMac = record.data.statusMacAddress;
			  // nm3kTip=该数值为最后一次重启后的数字
			  return '<a href="#" class=my-link onclick=\'showCMFlapChart("' + cmMac + '");\'> ' + value + ' </a>';
			}

			function showCMFlapChart(cmMac) {
			  WIN.top.createDialog('cmFlapDemo', '@cmlist.flaphiscurve@', 800, 600,
			    '/cmflap/showOneCMFlapHisChart.tv?cmMac=' + cmMac, null, true, true);
			}

			function renderOperation(value, p, record) {
			  var statusMacAddress = record.data.statusMacAddress,
			    cmIp = record.data.statusInetAddress,
			    cmId = record.data.cmId,
			    status = record.data.docsIfCmtsCmStatusValueString,
			    cmcId = record.data.cmcId,
			    status = record.data.statusValue,
			    returnStr = "";
			  if (status != 6 && status != 21 && status != 26 && status != 27 && status != 30 && status != 31 ) {
			  // offline和registering这两种状态只有刷新没有复位，其中registering状态有多个值
			    returnStr += String.format(refreshCmFormatStr, statusMacAddress, cmcId);
			    //returnStr += String.format(otherOperFormatStr, cmId);
			    return returnStr;
			  } else {
			    // 在线状态的CM
			    if (operationDevicePower) {
			      // 如果有操作设备的权限
			      returnStr += String.format(refreshCmFormatStr, statusMacAddress, cmcId) + " / ";
			      returnStr += String.format(restartCmFormatStr, statusMacAddress, cmIp, cmId, status) ;
			      //returnStr += String.format(otherOperFormatStr, cmId);
			      return returnStr;
			    } else {
			      // 如果没有操作设备的权限
			      returnStr += String.format(refreshCmFormatStr, statusMacAddress, cmcId) + " / ";
			      if (operationDevicePower) {
			     	 returnStr += "<a href='javascript:;' class='yellowLink' disabled='disabled'>@CM.resetCm@</a>";
			      }
			      //returnStr += String.format(otherOperFormatStr, cmId);
			      return returnStr;
			    }
			  }
			}
		
		function showEditPage(cmcId, folderId, name){
			WIN.top.createDialog('editDeviceInfo', '@IPTOPO.editDevice@' , 600, 370, '/cmc/showEditDevice.tv?cmcId='+cmcId+"&folderId="+folderId+"&deviceName="+name, null, true, true)
		}
		
		/* ==========================================================
		 * 以下内容是刷新CM信息的方法
		 * ========================================================== */

		function refreshCmInfo() {
		  var sm = grid.getSelectionModel();
		  var record = sm.getSelected();
		  if (record == null) {
		    return;
		  }
		  window.top.showWaitingDlg('@COMMON.waiting@', '@CMC.ipqam.doingRefreshDevice@', 'ext-mb-waiting');
		  var cmMac = record.data.statusMacAddress,
		    cmcId = record.data.cmcId,
		    cmId = record.data.cmId,
		    statusIndex = record.data.statusIndex,
		    cmIp = record.data.statusInetAddress,
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
		        	  cmAttribute[key] && record.set(key, cmAttribute[key]);
		          }

		          record.commit();
		          //更新CM数量信息
		          updateCmNum();
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
		
		

		/* ==========================================================
		 * 以下内容是重启CM信息的方法
		 * ========================================================== */

		function restarCm() {
		  var sm = grid.getSelectionModel(),
		    record = sm.getSelected();
		  if (record == null) {
		    return;
		  }
		  var cmId = record.data.cmId,
		    cmIp = record.data.statusInetAddress,
		    status = "";
		  if (isCmOnline(record.data.statusValue)) {
		    status = "Online";
		  }
		  WIN.top.showConfirmDlg('@COMMON.tip@', '@CM.confirmResetCm@', function(type) {
		    if (type == 'no') {
		      return;
		    }
		      window.top.showWaitingDlg('@COMMON.waiting@', '@cmList.resetting@', 'ext-mb-waiting');
		      $.ajax({
		    	  	url:'/cm/resetCm.tv',data:{cmId: cmId, cmIp: cmIp, status: status},
		      		success:function(response){
			    	  window.top.closeWaitingDlg();
			          var msg = "";
			          if (response.message == 'success') {
			            msg = '@cmList.resetCmdDone@';
			            //重启成功，则直接将CM状态置为下线
			            var sm = grid.getSelectionModel(),
			              record = sm.getSelected();
			            if (record == null) {
			              return;
			            }
			            record.beginEdit();
			            record.set('statusValue', 1);
			            record.commit();
			          } else {
			            if (response.reason == "SnmpException") {
			              msg = '@CMC.tip.snmpUnreachable@';
			            } else if (response.reason == "PingException") {
			              msg = '@CMC.tip.icmpUnreachable@';
			            } else if (response.reason == "offline") {
			              msg = '@CMC.tip.cmOfflineTip@';
			            }
			          }
			          top.afterSaveOrDelete({
			            title: '@COMMON.tip@',
			            html: '<b class="orangeTxt">' + msg + '</b>'
			          });
		      		}
		      });
		  });
		}
		function restartCmcList(){
			var sm = grid.getSelectionModel();
			if (sm != null && sm.hasSelection()) {
				 top.showConfirmDlg('@COMMON.tip@', '@CCMTS.confirmResetCcmts@', function(type) {
					if (type == "no") {
						return;
					}
					var selections = sm.getSelections();
					var cmcIds = [];
					for (var i = 0; i < selections.length; i++) {
						cmcIds[i] = selections[i].data.entityId;
					}
					top.showWaitingDlg('@COMMON.waiting@', '@IPTOPO.onReserting@', 'ext-mb-waiting');
					$.ajax({
						url:"/cmc/batchRestartCmc.tv?cmcIdList=" + cmcIds.join(","), 
						type:'POST',
						dataType:"text",
						success:function (text) {
							if(text == 'success'){
								top.showMessageDlg("@COMMON.tip@", "@IPTOPO.resertSuccess@");
							}else{
								top.showMessageDlg("@COMMON.tip@", "@IPTOPO.resertFailed@");
							}
						}, error:function () {
							top.showMessageDlg("@COMMON.tip@", "@IPTOPO.resertFailed@");
						},
			            cache:false
					});
					});
			} else {
				top.showMessageDlg("@COMMON.tip@", "@IPTOPO.chooseDevice@");
			}
		}
		
		function modifyTopoFolder(){
			var sm = grid.getSelectionModel();
			if (sm != null && sm.hasSelection()) {
				var selections = sm.getSelections();
				var cmcIds = [];
				for (var i = 0; i < selections.length; i++) {
					cmcIds[i] = selections[i].data.entityId;
				}
				WIN.top.createDialog('modifyTopoFolder', '@IPTOPO.updateArea@' , 600, 370, '/cmc/showModifyTopFolder.tv?cmcIdList=' + cmcIds.join(","), null, true, true)
			}else{
				top.showMessageDlg("@COMMON.tip@", "@IPTOPO.chooseDevice@");
			}
		}
		
		function buildPagingToolBar() {
		    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
		       displayInfo: true, items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-'
		                       		]});
		   return pagingToolbar;
		}
		
		function arrive_timer_format(s) {
		    var t
		    if (s > -1) {
		        hour = Math.floor(s / 3600);
		        min = Math.floor(s / 60) % 60;
		        sec = Math.floor(s) % 60;
		        day = parseInt(hour / 24);
		        if (day > 0) {
		            hour = hour - 24 * day
		            t = day + "@resources/COMMON.D@" + hour + "@resources/COMMON.H@"
		        } else {
		            t = hour + "@resources/COMMON.H@"
		        }
		        if (min < 10) {
		            t += "0"
		        }
		        t += min + "@resources/COMMON.M@"
		        if (sec < 10) {
		            t += "0"
		        }
		        t += sec + "@resources/COMMON.S@"
		    }
		    return t
		}
		
		
		function restartAllCm() {
			  WIN.top.showConfirmDlg('@COMMON.tip@', '@CMC.tip.confirmResetAllCm@', function(button, text) {
			    if (button == "yes") {
			      // 获取当前页的所有在线CM的行号
			      var onlineCmIndex = new Array();
			      for (var i = 0; i < store.getCount(); i++) {
			        var curCm = store.getAt(i).data;
			        if (isCmOnline(curCm.statusValue)) {
			          onlineCmIndex.push(i);
			        }
			      }
			      if (onlineCmIndex.length == 0) {
			        WIN.top.showMessageDlg('@COMMON.tip@', '@ccm/CCMTS.CmList.noCm@');
			        return;
			      }
			      var successNum = 0,
			        failedNum = 0;
			      $('#suc-num-dd').text(successNum);
			      $('#fail-num-dd').text(failedNum);
			      var dwrId = 'cmList_restartAllCm';
			      var refreshSuccess = false; // 刷新是否成功
			      /* 计数器 */
			      var returnNum = 0; // 调用Action返回计数器
			      refreshedCmCounter = 0; // 已刷新CM计数器
			      /* 初始化操作,重置 */
			      for (var i = 0; i < onlineCmIndex.length; i++) {
			        grid.getView().addRowClass(onlineCmIndex[i], 'yellow-row');
			      }
			      clearTimeout(dwrResponseTimer); // 清除计时器
			      clearTimeout(responseTimer); // 清除计时器
			      showLoading("@ccm/CCMTS.CmList.restartingCm@");
			      Ext.getCmp('refreshAllCm_button').setDisabled(true); // 使按钮无效，防止重复点击
			      Ext.getCmp('refreshCmSignal_button').setDisabled(true);
			      /* DWR推送 */
			      WIN.top.addCallback("refreshAllOnLineCm", function(json) {
			        var cmId = json.cmId;
			        for (var j = 0; j < store.getCount(); j++) {
			          if (store.getAt(j).get('cmId') == cmId) {
			            // 找到对应的行，改变颜色，表明刷新成功还是失败
			            if (json.success == true) {
			              // 重启成功后，将在线状态置为下线
			              grid.getView().removeRowClass(j, 'yellow-row');
			              successNum++;
			              var record = store.getAt(j);
			              record.beginEdit();
			              record.set('statusValue', 1);
			              record.commit();
			              // store.getAt(j).set('statusValue', 1)
			              $('#suc-num-dd').text(successNum);
			            } else {
			              grid.getView().addRowClass(j, 'red-row');
			              failedNum++;
			              $('#fail-num-dd').text(failedNum);
			            }
			          }
			        }
			        refreshedCmCounter++;
			        if (refreshedCmCounter >= onlineCmIndex.length) {
			          hideLoading();
			          refreshSuccess = true;
			          clearTimeout(responseTimer);
			          top.afterSaveOrDelete({
			            title: '@COMMON.tip@',
			            html: '<b class="orangeTxt">@cmList.resetCmdDone@<br/>@CMC.tip.successfully@' + successNum + '@CCMTS.view.cmNumOffline.unit@,@CMC.label.failure@' + failedNum + '@CCMTS.view.cmNumOffline.unit@</b>'
			          });
			          Ext.getCmp('refreshAllCm_button').setDisabled(false);
			          Ext.getCmp('refreshCmSignal_button').setDisabled(false);
			          dwrResponseTimer = setTimeout(function() {
			            // store.reload();
			            // $('#suc-num-dd').text(0);
			            // $('#fail-num-dd').text(0);
			          }, 8000);
			        }
			      }, dwrId);
			      /* 发送查询请求 */
			      for (var i = 0; i < onlineCmIndex.length; i++) {
			        try {
			          var cmId = store.getAt(onlineCmIndex[i]).data.cmId;
			          var cmIp = store.getAt(onlineCmIndex[i]).data.statusIpAddress;
			          $.ajax({
			            url: '/cmlist/refreshAllOnLineCm.tv?num=' + Math.random(),
			            type: 'post',
			            dataType: "json",
			            data: {
			              cmId: cmId,
			              dwrId: dwrId,
			              status: 6
			            },
			            success: function() {},
			            error: function() {},
			            cache: false
			          });
			        } catch (e) {}
			      }
			      /* 采集超时失败 */
			      responseTimer = setTimeout(function() {
			        // store.reload();
			        // $('#suc-num-dd').text(0);
			        // $('#fail-num-dd').text(0);
			        if (!refreshSuccess) {
			          hideLoading();
			          Ext.getCmp('refreshAllCm_button').setDisabled(false);
			          Ext.getCmp('refreshCmSignal_button').setDisabled(false);
			          WIN.top.removeCallback("refreshAllOnLineCm", dwrId);
			        }
			      }, 90000);
			    }
			  });
			}
		
		/* ==========================================================
		 * 以下内容是刷新信号质量的方法
		 * ========================================================== */

		function refreshCmSignal() {
			var random = randomInteger(0, 100);
            refreshCmSignalQuality('topo_refreshCmSignal_' + random, store, true)
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
		</script>
	</head>
	
	<body class="bodyWH100percent bodyGrayBg">
	<div id="loading">@ccm/CCMTS.CmList.refreshingSignal@</div>
	</body>
</Zeta:HTML>
