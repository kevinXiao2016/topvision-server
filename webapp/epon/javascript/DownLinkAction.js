function detailHandler() {
    var cmcId = entity.cmcList[0].cmcId;
    window.parent.addView('entity-' + cmcId, entity.entityName, 'entityTabIcon','/cmc/showCmcPortal.tv?cmcId=' + cmcId);
}
function onuDetailHandler(){
	window.parent.addView('entity-' + page.entityId, unescape(entity.entityName), 'entityTabIcon', '/onu/showOnuPortal.tv?onuId=' + page.entityId );
}
function onuCpeHandler(){
	window.parent.addView('entity-' + page.entityId, unescape(entity.entityName), 'entityTabIcon', '/epon/onucpe/showOnuCpeList.tv?module=6&onuId=' + page.entityId );
}
function perfMgmtHandler() {
    var cmcId = entity.cmcList[0].cmcId;
    var $entityName = entity.entityName;
    window.parent.addView("cmcPerfParamConfig", "@performance/Performance.cmcPerfParamConfig@", "icoE6", "/cmc/perfTarget/showCmcPerfManage.tv?entityId=" + cmcId, null, true);
}
function addToTopoHandler(){
    var cmcId = entity.cmcList[0].cmcId;
    var cmcMac = entity.cmcList[0].cmcMac;
    var typeId = entity.cmcList[0].typeId;
    window.top.createDialog('editTopoFolder', "@network/COMMON.editFolder@", 800, 500, '/entity/editTopoFolder.tv?entityId='+cmcId, null, true, true);
}
function renameDeviceHandler() {
    window.top.createDialog('renameCmcJSP', I18N.SUPPLY.renameDevice , 600, 270, 
            '/cmc/showRenameCmc.tv?onuId=' + entity.entityId + '&nmName=' + entity.entityName, null, true, true);
}
function unbindHandler() {
    var cmcId = entity.cmcList[0].cmcId;
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.SUPPLY.cfmUnbindCmc ,function(button,text){
        if(button == "yes"){
            $.ajax({
                url:'/cmc/cmcNoMacBind.tv?cmcId='+ cmcId,
                type:'POST',dateType:'json',
                success:function(response){
                    if(response.message == "success"){
                        searchOnu();
                        top.afterSaveOrDelete({
                        	title:I18N.COMMON.tip,
                        	html:I18N.SUPPLY.unbindCmcOk
                        })
                     }else{
                    	 top.afterSaveOrDelete({
                         	title:I18N.COMMON.tip,
                         	html:I18N.SUPPLY.unbindCmcEr
                         })
                     }
                },
                error:function(){
                	 top.afterSaveOrDelete({
                      	title:I18N.COMMON.tip,
                      	html:I18N.SUPPLY.unbindCmcEr 
                      })
                	//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SUPPLY.unbindCmcEr );
                },cache:false
            });
        }
    });
}
function resetHandler() {
    var cmcId = entity.cmcList[0].cmcId;
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.SUPPLY.cfmResetCmc ,function(button,text){
        if(button == "yes"){
            $.ajax({
                url:'/cmc/resetCmc.tv?cmcId='+ cmcId,
                type:'POST',
                dateType:'json',
                success:function(response){
                    if(response.message == "success"){
                    	top.afterSaveOrDelete({
                        	title:I18N.COMMON.tip,
                        	html:I18N.ONU.resetOk
                        })
                        //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.resetOk )
                     }else{
                         //window.parent.showMessageDlg(I18N.COMMON.tip,I18N.ONU.resetError)
                         top.afterSaveOrDelete({
                        	title:I18N.COMMON.tip,
                        	html:I18N.ONU.resetError
                        })
                     }
                },
                error: function(){
                	top.afterSaveOrDelete({
                    	title:I18N.COMMON.tip,
                    	html:I18N.ONU.resetError
                    })
                	//window.parent.showMessageDlg(I18N.COMMON.tip,I18N.ONU.resetError)
                },
                cache: false
            });
        }
    });
}
function onuNameConfigHandler(){
    window.top.createDialog('onuNameConfig', I18N.ONU.onuAliasSet, 600, 250, '/onu/showOnuConfig.tv?onuId=' + page.entityId + '&entityId=' + entityId, null, true, true);
}
function onuOpticalAlarmHandler(){
	  window.top.createDialog('oltOnuOpticalAlarm', I18N.ONU.ponOptAlertMgmt, 630, 450, '/epon/optical/showOltOnuOpticalAlarm.tv?entityId=' + entityId +"&portIndex="+entity.onuIndex , null, true, true);
}

function opticalAlarmBackHandler(){
	  window.top.createDialog('onuOpticalAlarmBack', I18N.ONU.ponOptAlertMgmt, 630, 450, '/epon/optical/showOnuOpticalAlarmBack.tv?entityId=' + entityId +"&portIndex="+entity.onuIndex , null, true, true);
}
function opticalAlarmHandler(){
	  window.top.createDialog('onuOpticalAlarm', I18N.ONU.ponOptAlertMgmt, 630, 450, '/epon/optical/showOnuOpticalAlarm.tv?entityId=' + entityId +"&portIndex="+entity.onuIndex , null, true, true);
}
function ableInformationHandler() {
    window.top.createDialog('onuAbility', I18N.ONU.abilityInfo, 600, 340, '/onu/showOnuAbility.tv?onuId=' + page.entityId + '&entityId=' + entityId, null, true, true);
}

//查看上下线记录
function viewOnOffRecordHandler(){
	var onuId = entity.entityId;
	var onuIndex = entity.onuIndex;
	var queryString = "?onuId=" + onuId + "&entityId=" + entityId + "&onuIndex=" + onuIndex;
	window.top.createDialog('onuOnOffRecordDlog', "@ONU.viewOnOffRecord@", 800, 500, "/onu/onoffrecord/showOnOffRecords.tv" + queryString, null, true, true);
}

function onuEnableHandler(node) {
	var $onu = entity;
	var onuAdminStatus = $onu.onuAdminStatus == 1 ? 2 : 1;
    var action =  onuAdminStatus == 1 ? I18N.COMMON.open : I18N.COMMON.close;
    window.parent.showConfirmDlg(I18N.COMMON.tip, String.format( I18N.SUPPLY.cfmActionOnuEn , action ), function(type) {
    if (type == 'no') return;
    window.top.showWaitingDlg(I18N.COMMON.wait, String.format( I18N.SUPPLY.settingOnuEn, action ), 'ext-mb-waiting');
    $.ajax({
    url: '/onu/setOnuAdminStat.tv',type: 'post',
    data: "onuId="+page.entityId+"&entityId=" + entityId + "&onuAdminStatus=" + onuAdminStatus,
    success: function(json) {
        if (json.message) {
            window.parent.showMessageDlg(I18N.COMMON.tip, json.message);
        } else {
        	top.afterSaveOrDelete({
   				title: I18N.COMMON.tip,
   				html: action+I18N.ONU.onuEnableOk
   			});
            //window.parent.showMessageDlg(I18N.COMMON.tip, action+I18N.ONU.onuEnableOk);
            $onu.onuAdminStatus = onuAdminStatus;
            modifyItemIcon( node );
        }
     },
     error: function() {
        window.parent.showMessageDlg(I18N.COMMON.tip, action+I18N.ONU.onuEnableError);
     }, cache: false
 });
 });
}
function tempretureEnableHandler(node) {
	 var $onu = entity;
	 var temperatureDetectEnable = $onu.temperatureDetectEnable == 1 ? 2 : 1;
     var action =  temperatureDetectEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close
     window.parent.showConfirmDlg(I18N.COMMON.tip , String.format( I18N.SUPPLY.cfmOnuTemplCheckEn , action ), function(type) {
         if (type == 'no')  return
         window.top.showWaitingDlg(I18N.COMMON.tip, String.format( I18N.SUPPLY.settingOnuTemplCheckEn , action ), 'ext-mb-waiting');
         $.ajax({
             url: '/onu/setOnuTemperatureStatus.tv',
             type: 'POST',
             data: "entityId=" + entityId+"&onuId="+page.entityId+"&onuTemperatureDetectEnable="+temperatureDetectEnable,
             success: function(json) {                           
                if (json.message) {
                    window.parent.showMessageDlg(I18N.COMMON.tip, json.message)
                } else {
                	top.afterSaveOrDelete({
           				title: I18N.COMMON.tip,
           				html: action+I18N.ONU.tempretureEnableOk
           			});
                    //window.parent.showMessageDlg(I18N.COMMON.tip, action+ I18N.ONU.tempretureEnableOk)
                    $onu.temperatureDetectEnable = temperatureDetectEnable;
                    modifyItemIcon( node );
                }
             }, error: function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, action+ I18N.ONU.tempretureEnableError)
         },cache: false
         })
     })
 }

function onuLaserSwitchHandler(node) {
	var $onu = entity;
	var onuLaserSwitch = $onu.laserSwitch == 1 ? 2 : 1;
    var action =  onuLaserSwitch == 1 ? '@COMMON.open@' : '@COMMON.close@'
    window.parent.showConfirmDlg('@COMMON.tip@' , String.format('@ONU.confirmLaserSwitch@', action ), function(type) {
        if (type == 'no')  {
        	return
        }
        window.top.showWaitingDlg('@COMMON.tip@', String.format('@ONU.OperaLaserSwitch@', action ), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/rogueonu/modifyOnuLaserSwitch.tv',
            type: 'POST',
            data: "entityId=" + entityId+"&onuId="+page.entityId+"&onuLaser="+onuLaserSwitch,
            success: function() {  
            	top.closeWaitingDlg('@COMMON.tip@');
               	top.afterSaveOrDelete({
          				title: '@COMMON.tip@',
          				html: action+'@ONU.operaLaserSwitchSuc@'
          			});
                   //window.parent.showMessageDlg(I18N.COMMON.tip, action+ I18N.ONU.tempretureEnableOk)
                   $onu.laserSwitch = onuLaserSwitch;
                   modifyItemIcon( node );
            }, error: function() {
               top.closeWaitingDlg('@COMMON.tip@');
               window.parent.showMessageDlg('@COMMON.tip@', action+'@ONU.operaLaserSwitchFail@')
        },cache: false
        })
    })
}
function FECEnableHandler(node) {
	 var $onu = entity;
	 var onuFecEnable = $onu.onuFecEnable == 1 ? 2 : 1;
     var action =  onuFecEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close;
     window.parent.showConfirmDlg(I18N.ONU.tip, String.format( I18N.SUPPLY.cfmFECEn , action ), function(type) {
         if (type == 'no') {
             return;
         }
         window.top.showWaitingDlg(I18N.ONU.wait, String.format( I18N.SUPPLY.settingFECEn , action ), 'ext-mb-waiting');
         $.ajax({
             url: '/onu/setOnuFECStatus.tv',
             type: 'POST',
             data: "entityId=" + entityId+"&onuId="+page.entityId+"&onuFecEnable="+onuFecEnable,
             success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg(I18N.ONU.tip, json.message);
                } else {
                	top.afterSaveOrDelete({
           				title: I18N.COMMON.tip,
           				html: action+I18N.ONU.FECEnableOk
           			});
                    //window.parent.showMessageDlg(I18N.ONU.tip, action+ I18N.ONU.FECEnableOk);
                    $onu.onuFecEnable = onuFecEnable;
                    modifyItemIcon( node );
                }
             }, error: function() {
                window.parent.showMessageDlg(I18N.ONU.tip, action+I18N.ONU.FECEnableError);
         },cache: false
         });
     });
 }
function isolatedEnableHandler(node) {
	var $onu = entity;
	var onuIsolationEnable = $onu.onuIsolationEnable == 1 ? 2 : 1;
    var action =  onuIsolationEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close
    window.parent.showConfirmDlg(I18N.ONU.tip, String.format( I18N.SUPPLY.cfmOnuIsolatedEn , action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg(I18N.ONU.wait, String.format( I18N.SUPPLY.settingIsolatedEn ,  action ), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/configOnuIsolationEnable.tv',
            type: 'POST',
            data: "entityId=" + entityId+"&onuId="+page.entityId+"&onuIsolationEnable=" + onuIsolationEnable ,
            success: function(json) {
               if (json.message) {
                   window.parent.showMessageDlg(I18N.ONU.tip, json.message);
               } else {
            	   top.afterSaveOrDelete({
          				title: I18N.COMMON.tip,
          				html: action+I18N.ONU.isolatedEnableOk
          			});
                  // window.parent.showMessageDlg(I18N.ONU.tip, action+ I18N.ONU.isolatedEnableOk);
                   $onu.onuIsolationEnable = onuIsolationEnable;
                   modifyItemIcon( node );
               }
            }, error: function() {
               window.parent.showMessageDlg(I18N.ONU.tip, action + I18N.ONU.isolatedEnableError);
        },cache: false
        });
    });
}
function rstpCfgHandler(){
    window.top.createDialog('onuRstpConfig', I18N.ONU.rstpCfgOnu, 600, 250, '/epon/rstp/showOnuRstpMode.tv?onuId=' + page.entityId + '&entityId=' + entityId, null, true, true);
}
function slaMgmtHandler() {
    var onuIndex = entity.onuIndex;
    var onuLoc = getLocationByIndex(onuIndex, 'onu');
    window.top.createDialog('onuSlaConfig', I18N.ONU.slaCfgMgmt + '(ONU:'+ onuLoc +')', 800, 500, 'epon/qos/showOnuSlaConfig.tv?onuIndex=' + onuIndex + '&entityId=' + entityId, null, true, true);
}
//针对V1.7.0.x版本与V1.6.9.x版本取值范围不一致提供两个功能页面
function slaMgmtHandlerBack() {
    var onuIndex = entity.onuIndex;
    var onuLoc = getLocationByIndex(onuIndex, 'onu');
    window.top.createDialog('onuSlaConfigBack', I18N.ONU.slaCfgMgmt + '(ONU:'+ onuLoc +')', 800, 500, 'epon/qos/showOnuSlaConfigBack.tv?onuIndex=' + onuIndex + '&entityId=' + entityId, null, true, true);
}

function macMgmtHandler() {
    var onuIndex = entity.onuIndex;
    var onuLoc = getLocationByIndex(onuIndex, 'onu');
    window.top.createDialog('onuMacMgmt', I18N.ONU.macMgmt + '(ONU:'+ onuLoc +')', 600, 450, 
            'epon/elec/showOnuMacMgmt.tv?onuIndex=' + onuIndex + '&entityId=' + entityId, null, true, true);
}
function inbandMgmtHandler() {
    var onuIndex = entity.onuIndex;
    var onuLoc = getLocationByIndex(onuIndex, 'onu');
    window.top.createDialog('onuInbandMgmt', I18N.ONU.inbandMgmt + '(ONU:'+ onuLoc +')', 600, 330, 
            'epon/elec/showOnuInbandMgmt.tv?onuIndex=' + onuIndex + '&entityId=' + entityId, null, true, true);
}
function igmpCfgHandler(){     
    var onuIndex = entity.onuIndex;
    var onuLoc = getLocationByIndex(onuIndex, 'onu');
    window.top.createDialog("igmpOnu", I18N.ONU.igmpCfg + ' ONU:'+ onuLoc, 600, 280, "epon/igmp/showOnuIgmp.tv?entityId="+entityId+'&onuIndex='+onuIndex , null, true, true);
}  
function queneMapHandler() {
    var onuIndex = entity.onuIndex;
    var onuLoc = getLocationByIndex(onuIndex, 'onu');
    window.top.createDialog('qosOnuMap', 'QoS'+I18N.ONU.queneMap+'(ONU:'+ onuLoc +')', 600, 570, 'epon/qos/showQosOnuMap.tv?onuId=' + page.entityId + '&entityId=' + entityId + '&onuIndex=' + onuIndex, null, true, true);
}
function quenePolicyHandler() {
    var onuIndex = entity.onuIndex;
    var onuLoc = getLocationByIndex(onuIndex, 'onu');
    window.top.createDialog('qosOnuPolicy', 'QoS'+I18N.ONU.quenePolicy+'(ONU:'+ onuLoc +')', 600, 535, 'epon/qos/showQosOnuPolicy.tv?onuId=' + page.entityId + '&entityId=' + entityId + '&onuIndex=' + onuIndex, null, true, true);
}
function addOnuToTopoHandler(){
	window.top.createDialog('editTopoFolder', "@network/COMMON.editFolder@", 800, 500, '/entity/editTopoFolder.tv?entityId='+page.entityId, null, true, true);
    //window.top.createDialog('moveToTopo', "@network/COMMON.editFolder@" ,600, 425, '/onu/moveToTopoJsp.tv?onuId=' + page.entityId + '&typeId=' +entity.entityType + '&onuName=' + entity.entityName, null, true, true);
}
function restoreHandler() {
    tishi1 = "@ONUVIEW.resertTip@";
    tishi2 = "@ONUVIEW.resertError@";
    tishi3 = "@ONUVIEW.reserting@";
    window.parent.showConfirmDlg(I18N.COMMON.tip , tishi1, function(type) {
        if (type == 'no') return
        showWaitingDlg(I18N.COMMON.wait, tishi3, 'ext-mb-waiting');
        $.ajax({
            url: '/onu/resetOnu.tv',
            data: "entityId=" + entityId + "&onuId=" + page.entityId,
            success: function() {
            	top.closeWaitingDlg(I18N.COMMON.tip);
            	top.afterSaveOrDelete({
                	title:I18N.COMMON.tip,
                	html:I18N.ONU.onuRestOk
                })
                //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.onuRestOk);
            }, error: function() {
            	top.closeWaitingDlg(I18N.COMMON.tip);
            	top.afterSaveOrDelete({
                	title:I18N.COMMON.tip,
                	html:tishi2
                })
                //window.parent.showMessageDlg(I18N.COMMON.tip, tishi2);
        },cache: false
        });
    });
}

function deleteHandler() {
	window.top.showConfirmDlg("@COMMON.tip@", "@onu/ONU.confirmDel@", function(type) {
        if (type == 'no'){
        	 return
        }
        window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.deleting@", 'ext-mb-waiting');
        var url = '', data = {};
        if(entity.onuEorG == 'E'){
        	url = '/onu/deleteOnuAuth.tv';
        	data = {
        		'entityId' : entityId,
        		'onuId' : page.entityId
        	};
        } else {
        	url = '/gpon/onuauth/deleteGponOnuAuth.tv';
        	data = {
	        	'gponOnuAuthConfig.entityId':entityId,
	        	'gponOnuAuthConfig.onuId':page.entityId
	        };
        }
    	$.ajax({
            url: url,
            data: data,
            success: function(json) {
            	//window.top.showMessageDlg("@COMMON.tip@", "@ONU.deleteOk@");
            	top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@onu/ONU.deleteOk@</b>'
	   			});
                window.location.reload();
            }, error: function() {
            	window.top.showMessageDlg("@COMMON.tip@", "@onu/ONU.deleteEr@");
            },
            cache: false
        });
    });
}

function unregisterHandler() {
    tishi1 = "@ONUVIEW.deregisterTip@";
    tishi2 = "@ONUVIEW.deregisterError@";
    tishi3 = "@ONUVIEW.deregistering@";
    window.parent.showConfirmDlg(I18N.COMMON.tip, tishi1, function(type) {
        if (type == 'no') return
        showWaitingDlg(I18N.COMMON.wait, tishi3, 'ext-mb-waiting');
        $.ajax({
            url: '/onu/deregisterOnu.tv',
            type: 'POST',
            data: "entityId=" + entityId + "&onuId=" + page.entityId,
            success: function() {
            	top.closeWaitingDlg(I18N.COMMON.tip);
            	top.afterSaveOrDelete({
                	title:I18N.COMMON.tip,
                	html:I18N.ONU.unregisterOk
                })
                //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.unregisterOk);
            }, error: function() {
            	top.closeWaitingDlg(I18N.COMMON.tip);
            	top.afterSaveOrDelete({
                	title:I18N.COMMON.tip,
                	html:tishi2
                })
            	// window.parent.showMessageDlg(I18N.COMMON.tip, tishi2);
            }, cache: false
        });
    });
}
function onuPonOpticalHandler() {
    var onuPonIndex = entity.onuIndex + 255;
    window.top.createDialog('modalDlg', I18N.ONU.ponInfo, 600, 370, '/epon/optical/showOnuPonOptical.tv?entityId=' + 
            entityId + '&portIndex=' + onuPonIndex, null, true, true);
}
function stasitc15EnableHandler(node) {
	var $onu = entity;
	var ponPerfStats15minuteEnable = $onu.ponPerfStats15minuteEnable == 1 ? 2 : 1;
    var action =  ponPerfStats15minuteEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close;
    window.parent.showConfirmDlg(I18N.COMMON.tip, String.format( I18N.SUPPLY.cfmStastic15En, action ), function(type) {
        if (type == 'no')
            return
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format( I18N.SUPPLY.settingStastic15En , action), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setOnu15MinPerfStatus.tv',
            type: 'POST',
            data: "entityId=" + entityId+"&onuId="+page.entityId+"&onu15minEnable="+ponPerfStats15minuteEnable,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg(I18N.COMMON.tip, json.message);
                } else {
                	top.afterSaveOrDelete({
          				title: I18N.COMMON.tip,
          				html: String.format( I18N.SUPPLY.setStastic15Ok , action)
          			});
                    //window.parent.showMessageDlg(I18N.COMMON.tip,String.format( I18N.SUPPLY.setStastic15Ok , action))
                    $onu.ponPerfStats15minuteEnable = ponPerfStats15minuteEnable;
                    modifyItemIcon( node );
                }
            }, error: function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, String.format( I18N.SUPPLY.setStastic15Er , action));
        },cache: false
        })
    })
}

function stasitcEnableHandler(node) {
	var $onu = entity;
	var ponPerfStats15minuteEnable = $onu.ponPerfStats15minuteEnable == 1 ? 2 : 1;
    var action =  ponPerfStats15minuteEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close;
    window.parent.showConfirmDlg(I18N.COMMON.tip, String.format( I18N.SUPPLY.cfmStasticEn, action ), function(type) {
        if (type == 'no')
            return
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format( I18N.SUPPLY.settingStasticEn , action), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setOnu15MinPerfStatus.tv',
            type: 'POST',
            data: "entityId=" + entityId+"&onuId="+page.entityId+"&onu15minEnable="+ponPerfStats15minuteEnable,
            success: function(json) {
                if (json.message) {
                	window.parent.showMessageDlg(I18N.EPON.tip, action + I18N.EPON.stasticError);
                } else {
                	top.afterSaveOrDelete({
          				title: I18N.COMMON.tip,
          				html: action + I18N.EPON.stasticOk
          			});
                	//window.parent.showMessageDlg(I18N.EPON.tip, action + I18N.EPON.stasticOk);
                    $onu.ponPerfStats15minuteEnable = ponPerfStats15minuteEnable;
                    modifyItemIcon( node );
                }
            }, error: function() {
            	window.parent.showMessageDlg(I18N.EPON.tip, action + I18N.EPON.stasticError);
        },cache: false
        })
    })
}
function stastic24EnableHandler(node) {
	var $onu = entity;
	var ponPerfStats24hourEnable = $onu.ponPerfStats24hourEnable == 1 ? 2 : 1;
    var action =  ponPerfStats24hourEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close
    window.parent.showConfirmDlg(I18N.COMMON.tip,  String.format( I18N.SUPPLY.cfmStastic24En, action ), function(type) {
        if (type == 'no')
            return
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format( I18N.SUPPLY.settingStastic24En , action), 'ext-mb-waiting')
        $.ajax({
            url: '/onu/setOnu24HourPerfStatus.tv',
            data: "entityId=" + entityId+"&onuId="+page.entityId+"&onu24hEnable="+ponPerfStats24hourEnable,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg(I18N.COMMON.tip, json.message)
                } else {
                	top.afterSaveOrDelete({
          				title: I18N.COMMON.tip,
          				html: action + I18N.EPON.stastic24Ok
          			});
                   // window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.stastic24Ok)
                    $onu.ponPerfStats24hourEnable = ponPerfStats24hourEnable;
                    modifyItemIcon( node );
                }
            }, error: function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.stastic24Error)
        },cache: false
        })
    })
}
function portEnableHandler(node) {
	var $uni = entity.onuUniPortList[currentId.substring(7)-1];
	var uniAdminStatus = $uni.uniAdminStatus == 1 ? 2 : 1;
    var action =  uniAdminStatus == 1 ? I18N.COMMON.open : I18N.COMMON.close;
    window.parent.showConfirmDlg(I18N.COMMON.tip,  String.format( I18N.SUPPLY.cfmActionPortEn , action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format( I18N.SUPPLY.settingPortEn, action), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setOnuUniAdminStatus.tv',
            data: "uniId=" + getActualId() + "&entityId=" + entityId + "&uniAdminStatus=" + uniAdminStatus,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg(I18N.COMMON.tip, json.message);
                } else {
                	top.afterSaveOrDelete({
          				title: I18N.COMMON.tip,
          				html: action + I18N.EPON.portEnableOk
          			});
                    //window.parent.showMessageDlg(I18N.COMMON.tip,  action + I18N.EPON.portEnableOk)
                    // 设置成功后修改entity属性
                    $uni.uniAdminStatus = uniAdminStatus;
                    if(uniAdminStatus == 2){
                    	$uni.uniOperationStatus = 2;
                    }
                    entity.changeUniState(uniAdminStatus, $uni.uniOperationStatus, "onuUni_" + $uni.uniRealIndex);
                    modifyItemIcon( node );
                }
            },
            error: function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.portEnableError)
            },cache: false
        });
    });
}
function loopbackHandler(){
	var $uni = entity.onuUniPortList[currentId.substring(7)-1];
    var uniDSLoopBackEnable = uni.uniDSLoopBackEnable == 2 ? 1 : 2;
    window.parent.showConfirmDlg(I18N.COMMON.tip, 
            tmpStat == 1 ? I18N.ONU.confirmLoopBackOn : I18N.ONU.confirmLoopBackOff, function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, 
        		uniDSLoopBackEnable == 1 ? I18N.ONU.isLoopBackOn : I18N.ONU.isLoopBackOff, 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setUniDSLoopBackEnable.tv',
            data: "uniId=" + getActualId() + "&entityId=" + entityId + "&uniDSLoopBackEnable=" + uniDSLoopBackEnable,
            success: function(json) {
                json = Ext.util.JSON.decode(json);
                if (json.message == 'success') {
                	top.afterSaveOrDelete({
          				title: I18N.COMMON.tip,
          				html: tmpStat == 1 ? I18N.ONU.loopBackOnSuc : I18N.ONU.loopBackOffSuc
          			});
                   /* window.parent.showMessageDlg(I18N.COMMON.tip, 
                            tmpStat == 1 ? I18N.ONU.loopBackOnSuc : I18N.ONU.loopBackOffSuc);*/
                    // 设置成功后修改entity属性
                    $uni.uniDSLoopBackEnable = uniDSLoopBackEnable;
                    modifyItemIcon( node );
                    var tmpS = page.grid.getSource();
                    tmpS[I18N.ONU.uniDSLoopBackEnable] = Enable[uniDSLoopBackEnable] || null;
                    page.grid.setSource(tmpS);
                } else {
                    window.parent.showMessageDlg(I18N.COMMON.tip, 
                            tmpStat == 1 ? I18N.ONU.loopBackOnFailed : I18N.ONU.loopBackOffFailed + ":" + json.message);
                }
            },
            error: function(json) {
                window.parent.showMessageDlg(I18N.COMMON.tip, 
                        tmpStat == 1 ? I18N.ONU.loopBackOnFailed : I18N.ONU.loopBackOffFailed + ":" + json.message);
            },cache: false
        });
    });
}
function flowcontrolHandler(node) {
	var $uni = entity.onuUniPortList[currentId.substring(7)-1];
    var flowCtrl = $uni.flowCtrl == 1 ? 2 : 1;
    var action =  flowCtrl == 1 ? I18N.COMMON.open : I18N.COMMON.close;
    window.parent.showConfirmDlg(I18N.COMMON.tip, String.format( I18N.SUPPLY.cfmFlowControlEn , action ), function(type) {
        if (type == 'no') 
            return
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format( I18N.SUPPLY.settingFlowControlEn ,  action), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setUniAttrFlowCtrlStatus.tv',
            data: "uniId=" + getActualId() + "&entityId=" + entityId + "&uniFlowCtrlEnable=" + flowCtrl,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg(I18N.COMMON.tip, json.message)
                } else {
                	top.afterSaveOrDelete({
          				title: I18N.COMMON.tip,
          				html: action + I18N.EPON.flowControlOk
          			});
                    //window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.flowControlOk)
                    $uni.flowCtrl = flowCtrl;
                    modifyItemIcon( node );
                }
            }, error: function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.flowControlError)
        }, cache: false
        });
    });
}
function autonegEnableHandler(node) {
	var $uni = entity.onuUniPortList[currentId.substring(7)-1];
    var uniAutoNegotiationEnable = $uni.uniAutoNegoEnableTemp == 1 ? 2 : 1;
    var action =  uniAutoNegotiationEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close;
    window.parent.showConfirmDlg(I18N.COMMON.tip,  String.format( I18N.SUPPLY.cfmIsolatedEn ,  action ), function(type) {
        if (type == 'no')
            return
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format( I18N.SUPPLY.settingIsolatedEn, action ), 'ext-mb-waiting')
        $.ajax({
            url: '/onu/setUniAutoNegoActive.tv',
            data: "uniId=" + getActualId() + "&entityId=" + entityId + "&uniAutoNegoEnable=" + uniAutoNegotiationEnable,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg(I18N.COMMON.tip, json.message);
                } else {
                	top.afterSaveOrDelete({
          				title: I18N.COMMON.tip,
          				html: action + I18N.EPON.isolatedOk
          			});
                    //window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.isolatedOk)
                    $uni.uniAutoNegotiationEnable = uniAutoNegotiationEnable;
                    modifyItemIcon( node );
                }
            }, error: function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.isolatedError)
        }, cache: false
        });
    });
}
function uniSpeedDecHandler() {
    var realIndex = (entity.onuUniPortList[currentId.substring(7) - 1].uniRealIndex);
    window.top.createDialog('uniRateLimit', I18N.ONU.uniSpeedDecMgmt, 600, 370, '/onu/showUniRateLimit.tv?onuId='+page.entityId+'&uniId=' + getActualId() + '&entityId=' + entityId+"&realIndex="+realIndex, null, true, true);
}

function uniSpeedDecHandlerBak() {
    var realIndex = (entity.onuUniPortList[currentId.substring(7) - 1].uniRealIndex);
    window.top.createDialog('uniRateLimit', I18N.ONU.uniSpeedDecMgmt, 600, 370, '/onu/showUniRateLimitBak.tv?onuId='+page.entityId+'&uniId=' + getActualId() + '&entityId=' + entityId+"&realIndex="+realIndex, null, true, true);
}
function workModeHandler() {
	var onuType = entity.entityType;
    var realIndex = (entity.onuUniPortList[currentId.substring(7) - 1].uniRealIndex);
    window.top.createDialog('uniAutoNegotiation', 'UNI@ONU.workMode@', 600, 410, '/onu/showUniAutoNegoConfig.tv?&onuId=' +
            page.entityId + '&uniId=' + getActualId() + '&entityId=' + entityId + "&realIndex=" + realIndex + '&onuType=' + onuType, null, true, true);
}
function uniUSUtgPriSimpleHandler(){
    window.top.createDialog('uniUSUtgPri', I18N.ONU.uniUSUtgPri, 600, 340, '/onu/showUniUSUtgPri.tv?&entityId=' +
            entityId + '&uniId=' + getActualId(), null, true, true);
}
function onuStormOutPacketRateHandler(){
    window.top.createDialog('onuStormOutPacketRate', I18N.ONU.onuStormOutPacketRate, 430, 300, '/onu/showOnuStormOutPacketRate.tv?&entityId=' +
            entityId + '&uniId=' + getActualId(), null, true, true);
}
function macLearnHandler() {
    var uniId = getActualId()
    window.top.createDialog('uniMacAddrConfig', I18N.ONU.macMaxLearn, 600, 250, '/onu/showUniMacAddrCap.tv?uniId=' + uniId + '&entityId=' + entityId, null, true, true);
}
function macAgeHandler() {
    var uniId = getActualId()
    window.top.createDialog('setUniMacAgeTime', I18N.ONU.macAgingTime, 600, 250, '/onu/showUniMacAgeTime.tv?uniId=' + uniId + '&entityId=' + entityId, null, true, true);
}

function onuMacAgeHandler() {
	var onuId = entity.entityId;
	var uniId = entity.onuUniPortList[0].uniId;
	var $url = '/onu/showOnuMacAgeTime.tv?onuId=' + onuId + '&entityId=' + entityId+"&uniId="+uniId;
    window.top.createDialog('setOnuMacAgeTime', I18N.ONU.macAgingTime, 600, 250, $url, null, true, true);
}
function macCleanHandler() {
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.ONU.confirmMacLean, function(type) {
        if (type == 'no') 
            return
        window.top.showWaitingDlg(I18N.COMMON.wait, I18N.ONU.macCleaning , 'ext-mb-waiting');
        $.ajax({
            url: '/onu/onuMacClear.tv?',
            cache:false,
            method:'post',
            data: {entityId : entityId, uniId : getActualId()},
            success: function(response) {
                window.parent.closeWaitingDlg();
                var text = response;
                if(text == 'success'){
                	top.afterSaveOrDelete({
          				title: I18N.COMMON.tip,
          				html: I18N.ONU.macCleanOk
          			});
                    //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.macCleanOk);
                }
                else{
                    window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.macCleanError);
                }
            },
            error: function(){
                window.parent.closeWaitingDlg();
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.macCleanError);
            }
        });
    });          
}
function uniStasitc15EnableHandler(node) {
	var $uni = entity.onuUniPortList[currentId.substring(7)-1];
    var perfStats15minuteEnable = $uni.perfStats15minuteEnable == 1 ? 2 : 1;
    var action =  perfStats15minuteEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close;
    window.parent.showConfirmDlg(I18N.COMMON.tip, String.format( I18N.SUPPLY.cfmStastic15En, action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format( I18N.SUPPLY.settingStastic15En , action), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setUni15MinPerfStatus.tv',
            data: "entityId=" + entityId+"&uniId="+getActualId()+"&uni15minEnable="+perfStats15minuteEnable,
            success: function() {
            	top.afterSaveOrDelete({
      				title: I18N.COMMON.tip,
      				html: action + I18N.EPON.stastic15Ok
      			});
                //window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.stastic15Ok)
                $uni.perfStats15minuteEnable = perfStats15minuteEnable;
                modifyItemIcon( node );
            }, error: function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.stastic15Error)
        }, cache: false
        });
    });
}

function uniStasitcEnableHandler(node) {
	var $uni = entity.onuUniPortList[currentId.substring(7)-1];
    var perfStats15minuteEnable = $uni.perfStats15minuteEnable == 1 ? 2 : 1;
    var action =  perfStats15minuteEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close;
    window.parent.showConfirmDlg(I18N.COMMON.tip, String.format( I18N.SUPPLY.cfmStasticEn, action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format( I18N.SUPPLY.settingStasticEn , action), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setUni15MinPerfStatus.tv',
            data: "entityId=" + entityId+"&uniId="+getActualId()+"&uni15minEnable="+perfStats15minuteEnable,
            success: function() {
            	top.afterSaveOrDelete({
      				title: I18N.COMMON.tip,
      				html: action + I18N.EPON.stasticOk
      			});
                //window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.stasticOk)
                $uni.perfStats15minuteEnable = perfStats15minuteEnable;
                modifyItemIcon( node );
            }, error: function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.stasticError)
        }, cache: false
        });
    });
}
function uniStastic24EnableHandler(node) {
	var $uni = entity.onuUniPortList[currentId.substring(7)-1];
    var perfStats24hourEnable = $uni.perfStats24hourEnable == 1 ? 2 : 1;
    var action =  perfStats24hourEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close;
    window.parent.showConfirmDlg(I18N.COMMON.tip, String.format( I18N.SUPPLY.cfmStastic24En, action ), function(type) {
        if (type == 'no') 
            return
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format( I18N.SUPPLY.settingStastic24En , action), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/setOnuUni24HourPerfStatus.tv',
            data: "entityId=" + entityId+"&uniId="+getActualId()+"&uni24hEnable="+perfStats24hourEnable,
            success: function() {
            	top.afterSaveOrDelete({
      				title: I18N.COMMON.tip,
      				html: action + I18N.EPON.stastic24Ok
      			});
                //window.parent.showMessageDlg(I18N.COMMON.tip, action + I18N.EPON.stastic24Ok);
                $uni.perfStats24hourEnable = perfStats24hourEnable;
                modifyItemIcon( node );
            }, error: function() {
                window.parent.showMessageDlg(I18N.COMMON.tip,action + I18N.EPON.stastic24Error);
        }, cache: false
        });
    });
}
function basicCfgHandler() {
    var uniId = getActualId();
    window.top.createDialog('vlanBaseConfig', I18N.ONU.vlanBasicCfg, 600, 320, 'epon/vlan/showPortVlanAttribute.tv?uniId='+uniId+'&entityId=' + entityId, null, true, true);
}
function modeCfgHandler() {
    var uniId = getActualId();
    window.top.createDialog('vlanModeConfig', I18N.ONU.uniVlanMode, 600, 510, 'epon/vlan/showUniVlanMode.tv?uniId=' +uniId + '&entityId=' + entityId, null, true, true);
}
function comBasicCfgHandler() {
    var comIndex = getActualIndex();
    var comLoc = getLocationByIndex(comIndex, 'uni');
    window.top.createDialog('comBaseConfig', I18N.ELEC.comBasicCfg + '('+ comLoc +')', 480, 320, 
          'epon/elec/showComBaseConfig.tv?comIndex=' + comIndex + '&entityId=' + entityId, null, true, true);
}
function comStaticsHandler() {
    var comIndex = getActualIndex();
    var comLoc = getLocationByIndex(comIndex, 'uni');
    window.top.createDialog('comPortStastic', I18N.ELEC.comStatics + '('+ comLoc +')' , 660, 420, 'epon/elec/showElecComStatic.tv?comIndex='+comIndex+'&entityId=' + entityId, null, true, true);
}
function comRemoteCfgHandler() {
    var comIndex = getActualIndex();
    var comLoc = getLocationByIndex(comIndex, 'uni');
    window.top.createDialog('comRemoteConfig', I18N.ELEC.comRemoteCfg + '('+ comLoc +')', 480, 320, 
          'epon/elec/showComRemoteConfig.tv?comIndex=' + comIndex + '&entityId=' + entityId, null, true, true);
}
function renameEntity() {
    window.top.createDialog('renameEntity', "@ONU.deviceInfo@", 600, 375,
            '/entity/showRenameEntity.tv?entityId=' + entity.entityId + "&pageId=" + window.parent.getActiveFrameId(), null, true, true);
}
function changeEntityName(entityId, name) {
     window.page.changeEntityName(name);
}

//UNI VLAN模板绑定信息
function uniVlanBindInfo(){
	 var uniId = getActualId();
	 window.top.createDialog('uniVlanBindInfo', "@ONU.uniVlanInfo@", 600, 370, 'epon/univlanprofile/showUniBindInfo.tv?uniId=' +uniId + '&entityId=' + entityId, null, true, true);
}
//绑定摄像头
function bindCameraFn(){
	 //计算参数信息
	var onuIndex = entity.onuIndex;
    var displayOnu = getLocationByIndex(onuIndex, 'onu');
	window.parent.createDialog("bindCamera", '@CAMERA/CAMERA.bindCamera@',  800, 500, "epon/camera/bindCamera.jsp?entityId="+entityId+"&onuIndex="+entity.onuIndex+"&displayOnu="+displayOnu, null, true, true);
}

//刷新ONU信息
function refreshOnu(){
    var onuId = entity.entityId;
    var onuIndex = entity.onuIndex;
    window.parent.showWaitingDlg(I18N.COMMON.waiting, "@network/NETWORK.refreshEntity@", 'waitingMsg', 'ext-mb-waiting');
    $.ajax({
        url:'/onu/refreshOnu.tv',
        type:'POST',
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
   				html: '<b class="orangeTxt">@network/NETWORK.refreshEntitySuccess@</b>'
   			});
        	//window.location.reload();
        	page.changeEntity(onuId,entity.entityType);   
        },
        error:function(){
        	window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.refreshEntityFail@");
        },
        cache:false
    });
}
//刷新CC8800A信息
function refreshCC8800A(){
	var cmcId = entity.cmcList[0].cmcId;
	var cmcIndex = entity.cmcList[0].cmcIndex;
	var onuId = entity.entityId;
	var onuIndex = entity.onuIndex;
	window.parent.showWaitingDlg(I18N.COMMON.waiting, "@network/NETWORK.refreshEntity@", 'waitingMsg', 'ext-mb-waiting');
	$.ajax({
		url:'/onu/refreshCC8800ABaseInfo.tv',
		type:'POST',
		data: {
			entityId : entityId,
			onuId : onuId,
			onuIndex : onuIndex,
			cmcId : cmcId,
			cmcIndex : cmcIndex
		},
		dateType:'json',
		success:function(json){
			window.top.closeWaitingDlg();
			top.afterSaveOrDelete({
				title: '@COMMON.tip@',
				html: '<b class="orangeTxt">@network/NETWORK.refreshEntitySuccess@</b>'
			});
			//window.location.reload();
			page.changeEntity(onuId, entity.entityType);   
		},
		error:function(){
			window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.refreshEntityFail@");
		},
		cache:false
	});
}

//三层搅动使能
/*function stirringCfgHandler(node){
	var $onu = entity;
	var onuStirringEnable = $onu.stirringEnable == 1 ? 2 : 1;
    var action =  onuStirringEnable == 1 ? I18N.COMMON.open : I18N.COMMON.close
    window.parent.showConfirmDlg(I18N.ONU.tip, String.format( 确认{0}三层搅动使能? , action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg(I18N.ONU.wait, String.format( 正在{0}三层搅动使能... ,  action ), 'ext-mb-waiting');
        $.ajax({
            url: '/onu/configOnuStirringEnable.tv',
            type: 'POST',
            data: "entityId=" + entityId+"&onuId="+page.entityId+"&stirringEnable=" + onuStirringEnable ,
            success: function(json) {
               if (json.message) {
                   window.parent.showMessageDlg(I18N.ONU.tip, json.message);
               } else {
            	   top.afterSaveOrDelete({
          				title: I18N.COMMON.tip,
          				html: action+三层搅动使能成功!
          			});
                  // window.parent.showMessageDlg(I18N.ONU.tip, action+ I18N.ONU.isolatedEnableOk);
                   $onu.stirringEnable = onuStirringEnable;
                   modifyItemIcon( node );
               }
            }, error: function() {
               window.parent.showMessageDlg(I18N.ONU.tip, action + 三层搅动使能失败!);
        },cache: false
        });
    });
}*/

function aclCfgMgmt(){
	window.parent.createDialog("aclList", I18N.EPON.aclCfgMgmt, 1000, 500, "/epon/acl/showAclList.tv?aclPortJspFlag=1&entityId="+entityId , null, true, true);
}

function indirectHandler(){
    onuAclInAndOut(0);
}

function outdirectHandler(){
	onuAclInAndOut(1);
}

function onuAclInAndOut(direction){
	var onuIndex = entity.onuIndex;
    var onuLoc = getLocationByIndex(onuIndex, 'onu');
    var ponIndex = entity.oltPonOptical.portIndex;
    window.parent.createDialog("aclPort",  String.format("ONU: {0} 绑定的ACL列表" , onuLoc), 640, 450, "/epon/acl/showAclPort.tv?entityId="+entityId+ "&portIndex=" + ponIndex + "&direction=" + direction + "&aclPortJspFlag=0", null, true, true);
}

function llidBasedMacLearn(){
    window.top.createDialog('uniMacAddrConfig', I18N.ONU.macMaxLearn, 600, 250, '/onu/showLlidBasedMacAddr.tv?onuId=' + entity.entityId + '&entityId=' + entityId, null, true, true);
}

//UNI端口VLAN业务
function uniPortVlan(){
	var uniId = getActualId();
	 window.top.createDialog('uniPortVlan', "@ONU.uniVlanInfo@", 800, 500, '/epon/uniportvlan/showUniVlanView.tv?uniId=' +uniId + '&entityId=' + entityId, null, true, true);
}

//onu 业务配置;
function onuConfigFn(){
	window.top.addView('uniserviceconfig-'+entity.entityId, '@ONU.uniConfig@[' + entity.entityName +']','icoD1', '/onu/showUniServiceConfig.tv?onuId='+page.entityId + "&entityId="+entityId,null,true);
}

//显示ONU IGMP配置
function showOnuIgmpConfig(){
	var onuId = entity.entityId;
	var onuIndex = entity.onuIndex;
	var name = entity.entityName;
	window.parent.addView('ONUIGMP-' + onuId, "ONU IGMP[" + name + "]" , 'entityTabIcon','/epon/igmpconfig/showOnuIgmpConfig.tv?entityId=' + entityId + '&onuId=' + onuId + '&onuIndex=' + onuIndex);
}

function showGponOnuCapability(){
	top.createDialog("modalDlg", "@ONU.abilityInfo@", 800, 500, "/gpon/onu/showGponOnuCapability.tv?onuId=" + page.entityId, null, true, true);
}

function gponOnuCpeFn(){
	window.parent.addView('entity-' + page.entityId, unescape(entity.entityName), 'entityTabIcon', '/gpon/onu/showOnuCpeList.tv?module=9&onuId=' + page.entityId );
}

function gponOnuHost(){
	window.parent.addView('entity-' + page.entityId, unescape(entity.entityName), 'entityTabIcon', '/gpon/onu/showOnuHostList.tv?module=6&onuId=' + page.entityId );
}

function uniMacFilterHandler(){
    window.top.createDialog('modalDlg', '@gpon/GPON.macFilter@', 800, 500, '/gpon/unifiltermac/showGponUniFilterMac.tv?&onuId=' + page.entityId
            + '&uniId=' + getActualId(), null, true, true);
}

function uniVlanConfigHandler(){
    window.top.createDialog('uniVlanConfig', '@onu/EPON.vlanCfg@', 600, 370, '/gpon/onu/showUniVlanView.tv?&uniId=' + getActualId(), null, true, true);
}

