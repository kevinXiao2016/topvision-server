
function modifyDevice() {
    window.top.createDialog('renameEntity', "@ONU.deviceInfo@", 600, 375,
            '/entity/showRenameEntity.tv?entityId=' + onuId+ "&pageId=" + window.top.getActiveFrameId() , null, true, true);
}

function refreshOnuQuality(entityId,onuId,onuIndex){
	 window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@");
	  $.ajax({
	      url:'/onu/refreshOnuQuality.tv',
	      data: {
	      	entityId : entityId,
	      	onuId : onuId,
	      	onuIndex : onuIndex
	      },
	      success:function(json){
      		window.top.closeWaitingDlg();
      		top.afterSaveOrDelete({
 				title: '@COMMON.tip@',
 				html: '<b class="orangeTxt">@network/NETWORK.refreshEntitySuccess@</b>'
 			});
      		store.reload();
	      },
	      error:function(){
	      	window.top.showMessageDlg("@COMMON.tip@", "@network/NETWORK.refreshEntityFail@");
	      },
	      cache:false
	  });
}

/**
 * 将值转成8字节
 */
function convert2Bit(value,m,record){
	if(record.data.onuEorG == "E"){
		if(typeof value !='numver')value=parseInt(value)
		var v = value.toString(16).toUpperCase();
		var length = v.length;
		for(var i=0;i<4-length;i++){
			v = '0'.concat(v);
		}
		return '0X'.concat(v);
	}else{
		return '--';
	}
}

function convert2Bit(value){
	if(typeof value !='numver')value=parseInt(value)
	var v = value.toString(16).toUpperCase();
	var length = v.length;
	for(var i=0;i<4-length;i++){
		v = '0'.concat(v);
	}
	return '0X'.concat(v);
}


//刷新ONU信息
function refreshOnu(){
  window.top.showWaitingDlg("@COMMON.wait@", "@network/NETWORK.reTopoEntity@", 'waitingMsg', 'ext-mb-waiting');
  $.ajax({
      url:'/onu/refreshOnu.tv',
      data: {
      	entityId : entityId,
      	onuId : onuId,
      	onuIndex : onuIndex
      }, dateType:'json',
      success:function(json){
      		window.top.closeWaitingDlg();
      		top.afterSaveOrDelete({
 				title: '@COMMON.tip@',
 				html: '<b class="orangeTxt">@network/NETWORK.reTopoOk@</b>'
 			});
      		location.reload();
      },
      error:function(){
    	  window.top.showMessageDlg("@COMMON.tip@", "@network/NETWORK.reTopoEr@");
      },
      cache:false
  });
}

function changeEntityName(entityId, name) {
	location.reload();
}

function addOnuToTopoHandler(){
    window.top.createDialog('moveToTopo', "@COMMON.addToTopo@" ,600, 425, '/onu/moveToTopoJsp.tv?onuId=' + onuId + '&typeId=' +typeId + '&onuName=' +onuName, null, true, true);
}

function onuOpticalAlarmHandler(){
	// 根据是否支持光功率阈值，展示不同页面
	var support = top.VersionControl.supportNode("onuOpticalAlarmBack", window[vcEntityKey]);
	if(!support.disabled && !support.hidden) {
		window.top.createDialog('onuOpticalAlarmBack', '@ONU.ponOptAlertMgmt@', 630, 450, '/epon/optical/showOnuOpticalAlarmBack.tv?entityId=' + entityId +"&portIndex="+onuIndex , null, true, true);
	} else {
		window.top.createDialog('onuOpticalAlarm', "@ONU.ponOptAlertMgmt@", 630, 450, '/epon/optical/showOnuOpticalAlarm.tv?entityId=' + entityId +"&portIndex="+onuIndex , null, true, true);
	}
}

function opticalAlarmBackHandler(){
}


function restoreHandler() {
    tishi1 = "@ONUVIEW.resertTip@";
    tishi2 = "@ONUVIEW.resertError@";
    tishi3 = "@ONUVIEW.reserting@";
    window.top.showConfirmDlg("@COMMON.tip@" , tishi1, function(type) {
        if (type == 'no') return
        top.showWaitingDlg("@COMMON.wait@", tishi3);
        $.ajax({
            url: '/onu/resetOnu.tv',
            data: "entityId=" + entityId + "&onuId=" + onuId,
            success: function() {
            	top.closeWaitingDlg("@COMMON.tip@");
            	top.afterSaveOrDelete({
                	title:"@COMMON.tip@",
                	html:I18N.ONU.onuRestOk
                })
            }, error: function() {
            	top.closeWaitingDlg("@COMMON.tip@");
            	top.afterSaveOrDelete({
                	title:"@COMMON.tip@",
                	html:tishi2
                })
        },cache: false
        });
    });
}


function isolatedEnableHandler(callback) {
	var $onuIsolationEnable = fetchOnuIsolationEnable();
    var action =  $onuIsolationEnable == 1 ?  "@COMMON.close@" : "@COMMON.open@" ;
    $onuIsolationEnable = $onuIsolationEnable == 1 ? 2: 1;
    window.top.showConfirmDlg("@COMMON.tip@", String.format( I18N.SUPPLY.cfmOnuIsolatedEn , action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg("@COMMON.wait@", String.format( I18N.SUPPLY.settingIsolatedEn ,  action ));
        $.ajax({
            url: '/onu/configOnuIsolationEnable.tv',dataType:'json',
            data: "entityId=" + entityId+"&onuId="+onuId+"&onuIsolationEnable=" + $onuIsolationEnable ,
            success: function(json) {
               if (json.message) {
                   window.top.showMessageDlg("@COMMON.tip@", json.message);
               } else {
            	   top.afterSaveOrDelete({
          				title: "@COMMON.tip@",
          				html: action+I18N.ONU.isolatedEnableOk
          			});
            	   if(callback && typeof callback === 'function') {
              			callback();
              		}
               }
            }, error: function() {
               window.top.showMessageDlg("@COMMON.tip@", action + I18N.ONU.isolatedEnableError);
        },cache: false
        });
    });
}

function tempretureEnableHandler(callback ) {
	var $temperatureDetectEnable = fetchTemperatureDetectEnable() ;
    var action =  $temperatureDetectEnable == 1 ? "@COMMON.close@" : "@COMMON.open@" ;
    $temperatureDetectEnable = $temperatureDetectEnable == 1 ? 2: 1;
    window.top.showConfirmDlg("@COMMON.tip@" , String.format( I18N.SUPPLY.cfmOnuTemplCheckEn , action ), function(type) {
        if (type == 'no')  return
        window.top.showWaitingDlg("@COMMON.tip@", String.format( I18N.SUPPLY.settingOnuTemplCheckEn , action ), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setOnuTemperatureStatus.tv',
            type: 'POST',
            data: "entityId=" + entityId+"&onuId="+onuId+"&onuTemperatureDetectEnable="+$temperatureDetectEnable,
            success: function(json) {                           
               if (json.message) {
                   	window.top.showMessageDlg("@COMMON.tip@", json.message)
               } else {
               		top.afterSaveOrDelete({
          				title: "@COMMON.tip@",
          				html: action+I18N.ONU.tempretureEnableOk
          			});
               		if(callback && typeof callback === 'function') {
               			callback();
               		}
               }
            }, 
            error: function() {
               window.top.showMessageDlg("@COMMON.tip@", action+ I18N.ONU.tempretureEnableError)
            },
            cache: false
        })
    })
}
function rstpCfgHandler(){
    window.top.createDialog('onuRstpConfig', I18N.ONU.rstpCfgOnu, 600, 250, '/epon/rstp/showOnuRstpMode.tv?onuId=' + onuId + '&entityId=' + entityId, null, true, true);
}

function slaMgmtHandler() {
    window.top.createDialog('onuSlaConfig', I18N.ONU.slaCfgMgmt + '(ONU:'+ onulocation +')', 800, 500, 'epon/qos/showOnuSlaConfig.tv?onuIndex=' + onuIndex + '&entityId=' + entityId, null, true, true);
}

function macAgeHandler() {
	var $url = '/onu/showOnuMacAgeTime.tv?onuId=' + onuId + '&entityId=' + entityId;
    window.top.createDialog('setOnuMacAgeTime', I18N.ONU.macAgingTime, 600, 250, $url, null, true, true);
}

//显示ONU IGMP配置
function onuIgmpConfigView(){
	window.parent.addView('ONUIGMP-' + onuId, "ONU IGMP[" + onuName + "]" , 'entityTabIcon','/epon/igmpconfig/showOnuIgmpConfig.tv?entityId=' + entityId + '&onuId=' + onuId + '&onuIndex=' + onuIndex);
}

function stasitcEnableHandler(callback) {
	var $ponPerfStats15minuteEnable = fetchPonPerfStats15minuteEnable();
    var action =  $ponPerfStats15minuteEnable == 1 ?  "@COMMON.close@" : "@COMMON.open@" ;
    $ponPerfStats15minuteEnable = $ponPerfStats15minuteEnable == 1 ? 2: 1;
    window.top.showConfirmDlg("@COMMON.tip@", String.format( I18N.SUPPLY.cfmStasticEn, action ), function(type) {
        if (type == 'no')
            return
        window.top.showWaitingDlg("@COMMON.wait@", String.format( I18N.SUPPLY.settingStasticEn , action), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setOnu15MinPerfStatus.tv',
            type: 'POST',
            data: "entityId=" + entityId+"&onuId="+onuId+"&onu15minEnable="+$ponPerfStats15minuteEnable,
            success: function(json) {
                if (json.message) {
                	window.top.showMessageDlg("@COMMON.tip@", action + I18N.EPON.stasticError);
                } else {
                	top.afterSaveOrDelete({
          				title: "@COMMON.tip@",
          				html: I18N.EPON.stasticOk
          			});
                	if(callback && typeof callback === 'function') {
              			callback();
              		}
                }
            }, error: function() {
            	window.top.showMessageDlg("@COMMON.tip@", action + I18N.EPON.stasticError);
        },cache: false
        })
    })
}

function showPerfCfg(){
	window.top.addView("onuPerfParamConfig", "@ONU.perfMgmt@", "icoE6", "/onu/onuPerfGraph/showOnuPerfManage.tv?entityId=" + onuId, null, true);
}

function onDeleteClick(){
	var data = getSelectedEntity();
	window.top.showConfirmDlg("@COMMON.tip@", "@ONU.confirmDel@", function(type) {
        if (type == 'no'){
        	return;
        }
        window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.deleting@", 'ext-mb-waiting');
        if(data.onuEorG == 'E'){
        	$.ajax({
                url: '/onu/deleteOnuAuth.tv',
                data: "entityId=" + data.entityId+"&onuId="+data.onuId+"&onuIndex="+data.onuIndex,
                success: function(json) {
                	//window.top.showMessageDlg("@COMMON.tip@", "@ONU.deleteOk@");
                	top.afterSaveOrDelete({
    	   				title: '@COMMON.tip@',
    	   				html: '<b class="orangeTxt">@ONU.deleteOk@</b>'
    	   			});
                    store.reload({
    					callback: function(){
    						clearSelect();
    					}
    				});
                }, error: function() {
                	window.top.showMessageDlg("@COMMON.tip@", "@ONU.deleteEr@");
            },cache: false
            })
        }else{
    	    $.ajax({
    	        url: '/gpon/onuauth/deleteGponOnuAuth.tv',type: 'POST',
    	        data: {
    	        	'gponOnuAuthConfig.entityId':data.entityId,
    	        	'gponOnuAuthConfig.onuId':data.onuId
    	        },
    	        success: function() {
    	        	top.closeWaitingDlg();
    	        	top.nm3kRightClickTips({
    					title: "@COMMON.tip@", html: "@ONU.deleteOk@"
    				});
    	        	 store.reload({
     					callback: function(){
     						clearSelect();
     					}
     				});
    	        }, error: function(json) {
    	            top.showMessageDlg("@COMMON.error@", "@ONU.deleteEr@");
    	        }, cache: false
    	    });
        }
    })
}

function updateRowAttribute(onuId, key, newValue) {
	if(store) {
 	   for(var i=0, len=store.getCount(); i<len; i++) {
 		   var data = store.getAt(i).data;
 		   if(data.onuId == onuId) {
 			   store.getAt(i).set(key, newValue);
 			   store.getAt(i).commit();
 			   break;
 		   }
 	   }
    }
}

function macFiter(){
	window.top.createDialog('modalDlg', '@gpon/GPON.macFilter@', 800, 500, '/gpon/unifiltermac/showGponUniFilterMac.tv?&onuId=' + onuId, null, true, true);
}