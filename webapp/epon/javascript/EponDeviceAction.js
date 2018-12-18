function saveConfigHandler(){
    window.parent.showConfirmDlg("@EPON.tip@", I18N.EPON.cfmSave, function(type) {
        if (type == 'no') return                      
        window.top.showWaitingDlg("@EPON.wait@", I18N.EPON.saving)
        $.ajax({
            url: '/configBackup/saveConfig.tv',type: 'POST',
            data: "entityId=" + entityId,
            success: function(json) {
                if (!json.success){
                    window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.saveError)
                } else {
                	top.afterSaveOrDelete({
           				title: "@EPON.tip@",
           				html: '<b class="orangeTxt">'+I18N.EPON.saveOk+'</b>'
           			});
                    //window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.saveOk)
                }   
            }, error: function(json) {
                window.parent.showMessageDlg("@EPON.tip@",  I18N.EPON.saveError)
            }, dataType: 'json', cache: false
        });
    });
}
function checkSysTimeHandler(){
    window.parent.showConfirmDlg("@EPON.tip@", I18N.EPON.cfmCheckSystime, function(type) {
        if (type == 'no') {
            return
        }
        window.top.showWaitingDlg("@EPON.wait@", I18N.EPON.checking)
        $.ajax({
            url:"../epon/checkSysTiming.tv",
            success: function (text) {
                if(text=='success'){
                     //window.parent.closeWaitingDlg();
                	top.afterSaveOrDelete({
           				title: "@EPON.tip@",
           				html: '<b class="orangeTxt">'+I18N.EPON.checkTimeOk+'</b>'
           			});
                     //window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.checkTimeOk);
                }else{ 
                     //window.parent.closeWaitingDlg();
                     window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.checkTimeError);
                }
            }, error: function () {
                //window.parent.closeWaitingDlg();
                window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.checkTimeError);
            }, data: {entityId : entityId}});
    });
}
function snmpv3UserListHandler(){
    window.top.createDialog('usmSnmpV3UserList', I18N.SNMPv3.userList , 800, 500, '/snmp/showSnmpV3UserList.tv?entityId=' + entityId, null, true, true)
}
function snmpv3AccessListHandler(){
    window.top.createDialog('usmSnmpV3GroupList', I18N.SNMPv3.accessList , 640, 480, '/snmp/showSnmpV3AccessList.tv?entityId=' + entityId, null, true, true)
}
function snmpv3ViewListHandler(){
    window.top.createDialog('usmSnmpV3ViewList', I18N.SNMPv3.viewList , 640, 480, '/snmp/showSnmpV3ViewList.tv?entityId=' + entityId, null, true, true)
}
function emsSnmpHandler() {
    window.top.createDialog('emsSnmpConifg', I18N.SNMPv3.communicate , 600, 270,'/snmp/showSnmpConfigForManager.tv?entityId=' + entityId, null, true, true)
}
function snmpv3NotifyHandler(){
    window.top.createDialog('snmpV3Notify', I18N.SNMPv3.n.notify, 777, 480, '/v3/showSnmpV3Notify.tv?entityId=' + entityId, null, true, true);
}
function resoreFactoryCfgHandler(){
    window.parent.showConfirmDlg("@EPON.tip@", I18N.EPON.cfmRestoreFactory, function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg("@EPON.wait@", I18N.EPON.restoringFactory);
        $.ajax({
            url: '/epon/restoreOlt.tv',
            data: "entityId=" + entityId,
            success: function(text) {
                if(text != null && text == 'success'){
                     window.parent.showConfirmDlg("@EPON.tip@", I18N.EPON.restoreReset, function(type) {
                            if (type == 'no') {
                                return;
                            }
                            window.top.showWaitingDlg("@EPON.wait@", I18N.EPON.restoring);
                            $.ajax({
                                url: '/epon/resetOlt.tv?r=' + Math.random(),
                                data: "entityId=" + entityId,
                                success: function(text) {
                                    if(text != null && text == 'success'){
                                    	top.afterSaveOrDelete({
                               				title: "@EPON.tip@",
                               				html: '<b class="orangeTxt">'+I18N.EPON.resetOk+'</b>'
                               			});
                                        //window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.resetOk); 
                                    }else{
                                        window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.resetError);
                                    }
                                }, error: function(text) {
                                    window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.resetError);
                                }, dataType: 'text', cache: false
                            });
                        })
                }else{
                    window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.restoreError);
                }
            }, error: function(text) {
                window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.restoreError);
            }, dataType: 'text', cache: false
        });
    });
}
function resetDeviceHandler(){
    window.parent.showConfirmDlg("@EPON.tip@", I18N.EPON.cfmReset, function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg( "@EPON.wait@" , I18N.EPON.reseting);
        $.ajax({
            url: '/epon/resetOlt.tv?r=' + Math.random(),
            data: "entityId=" + entityId,
            success: function(text) {
                if(text != null && text == 'success'){
                	top.afterSaveOrDelete({
           				title: "@EPON.tip@",
           				html: '<b class="orangeTxt">@EPON.resetOk@</b>'
           			});
                    //window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.resetOk);
                }else{
                    window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.resetError);
                }
            }, error: function(text) {
                window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.resetError);
            }, dataType: 'text', cache: false
        });
    });
}

function macAgeTimeHandler(){
    window.top.createDialog('sniMacAddressAgingTime', I18N.EPON.macAgingSet, 600, 300, 
        '/epon/showSniMacAddressAgingTime.tv?entityId=' + entityId, null, true, true)
}
function collectRecordSetHandler(){
    window.top.createDialog('collectConfig', I18N.EPON.collectRecordSet, 600, 370, 'epon/perf/showPerfStatsGlobalSet.tv?entityId=' + entityId, null, true, true)
}
function thresholdCfgHandler(){
    window.top.createDialog('perfThresholdConfig', I18N.EPON.thresholdCfg, 800, 500, 'epon/perf/showPerfThresholdJsp.tv?entityId=' + entityId, null, true, true)
}
function alertDisplayHandler(){
    location.href = "/epon/alert/showOltAlert.tv?module=5&entityId=" + entityId + "&currentId=" + currentId
}
function trapCfgHandler(){
    window.parent.createDialog("oltTrapConfig", I18N.EPON.trapSet, 800, 500, "/epon/alert/showOltTrapServerManagement.tv?entityId=" + entityId, null, true, true)
}
function alertDeclHandler(){
    window.parent.createDialog("oltAlertMask", I18N.EPON.alertDeclMgmt, 800, 500,  "/epon/alert/showOltAlertMask.tv?entityId=" + entityId, null, true, true)
}
function alertOpticalHandler(){
    window.parent.createDialog("alertOpticalHandler", I18N.EPON.optAlertMgmt, 630,370,  "/epon/optical/showOltOpticalAlarm.tv?entityId=" + entityId, null, true, true)
}
function fileMgmtHandler(){
    window.parent.createDialog("fileManage", I18N.EPON.fileMgmt, 800, 500, "epon/showFileManage.tv?entityId=" + entityId, null, true, true)
}
function onuAutoUpgHandler() {
    window.parent.createDialog("onuAutoUpg", I18N.onuAutoUpg.co.onuAutoUpgMgmt, 800, 500, "/onu/showOnuAutoUpg.tv?entityId=" + entityId, null, true, true);
}

function systemRogueCheckHandler(){
	var action =  olt.systemRogueCheck == 1 ?  '@EPON.close@': '@EPON.open@';
    window.parent.showConfirmDlg("@EPON.tip@", String.format('@EPON.confirmRogueSwitch@', action), function(type) {
      if (type == 'no') {
          return;
      }
      var systemRogueCheck = olt.systemRogueCheck == 1 ? 2 : 1;
      window.top.showWaitingDlg("@EPON.wait@", String.format('@EPON.openRogueSwitch@' , action));
      $.ajax({
          url: '/onu/rogueonu/setOltRogueCheck.tv',
          data: "entityId=" + entityId + "&systemRogueCheck=" + systemRogueCheck,
          success: function() {
            	 top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +'@EPON.openRogueSuc@'
        			});
                 olt.systemRogueCheck = systemRogueCheck;
                 //window.location.reload();
          }, error: function() {
             window.parent.showMessageDlg("@EPON.tip@", action + '@EPON.openRogueFail@')
         }, cache: false
      });
    });  
}

function rstpEnableHandler(){
    var action =  olt.stpGlobalSetEnable == 1 ?  I18N.EPON.close : I18N.EPON.open;
    window.parent.showConfirmDlg("@EPON.tip@", String.format(I18N.SUPPLY.cfmActionRstp , action), function(type) {
      if (type == 'no') {
          return;
      }
      var stpGlobalSetEnable = olt.stpGlobalSetEnable == 1 ? 2 : 1;
      window.top.showWaitingDlg("@EPON.wait@", String.format(I18N.SUPPLY.onActionRstp , action));
      $.ajax({
          url: '/epon/rstp/setStpGlobalEnable.tv',
          data: "entityId=" + entityId + "&stpGlobalSetEnable=" + stpGlobalSetEnable + "&r=" + Math.random(),
          success: function(txt) {
             window.parent.closeWaitingDlg();
             if (txt == 'error') {
                 window.parent.showMessageDlg("@EPON.tip@",  action + I18N.EPON.rstpError);
             } else {
            	 top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.rstpOk+'</b>'
        			});
                 //window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.rstpOk);
                 //----------设置RSTP使能开启-------------//
                 olt.stpGlobalSetEnable = stpGlobalSetEnable;
                 window.location.reload();
             }
          }, error: function() {
             window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.rstpError)
         }, cache: false
      });
    });  
}
function rstpSettingHandler(){
    window.top.createDialog('oltRstpConfig', 'OLT '+I18N.EPON.rstpSetting, 800, 520, 'epon/rstp/getStpGlobalConfigAttribute.tv?entityId=' + entityId, null, true, true);
}
function dhcpBasicConfigHandler(){
    window.top.createDialog('dhcpBasic', I18N.EPON.dhcpSet, 450, 240, 'epon/dhcp/showOltDhcpConfig.tv?entityId=' + entityId, null, true, true);
}
function dhcpServerCfgHandler(){
    window.top.createDialog('dhcpServerConfig', I18N.EPON.dhcpServerSet, 720, 420, 'epon/dhcp/showOltDhcpServerConfig.tv?entityId=' + entityId, null, true, true);
}
function dhcpGateCfgHandler(){
    window.top.createDialog('dhcpGiaddrConfig', I18N.EPON.dhcpGate, 580, 420, 'epon/dhcp/showOltDhcpGiaddrConfig.tv?entityId=' + entityId, null, true, true);
}
function dhcpDynaCfgHandler(){
    window.top.createDialog('dhcpIpMacStatic', I18N.EPON.dhcpDynaCfg, 680, 410, 'epon/dhcp/showOltDhcpIpMacStaticConfig.tv?entityId=' + entityId, null, true, true);
}
function dhcpDynaInfoHandler(){
    window.top.createDialog('dhcpIpMacDynamic', I18N.EPON.dhcpDynaInfo, 560, 410, 'epon/dhcp/showOltDhcpIpMacDynamicConfig.tv?entityId=' + entityId, null, true, true);
}
function igmpProtocoCfgHandler(){
    window.parent.createDialog("igmpProtocol", I18N.IGMP.igmpProtocoSet , 700, 460, "epon/igmp/showIgmpProtocol.tv?entityId="+entityId , null, true, true);
}
function igmpControlbleMvlanTempHandler(){
    window.parent.createDialog("showIgmpMvlan", I18N.EPON.controlMvlan, 850, 520, "/epon/igmp/showIgmpMvlan.tv?entityId="+entityId , null, true, true);
}
function igmpProxyHandler(){
    window.parent.createDialog("igmpProxy", I18N.EPON.mvlanMgmt, 600, 435, "/epon/igmp/showIgmpProxy.tv?entityId="+entityId , null, true, true);
}
function igmpCallHistoryHandler(){
    window.parent.createDialog("igmpHistory", 'IGMP' + I18N.EPON.callHistory, 850, 530, "epon/igmp/showIgmpHistory.tv?entityId="+entityId , null, true, true);
}
function igmpMvlanTranslateHandler(){
    window.parent.createDialog("igmpTranslation", I18N.EPON.mvlanTranslateRules , 600, 450, "epon/igmp/showIgmpTranslation.tv?entityId="+entityId , null, true, true);
}
//@FIXME by @bravin : dviceIgmpMode is undefined
function igmpQueryMvlanUserHandler(){
    if(deviceIgmpMode == 1){
        window.parent.createDialog("igmpForwarding", I18N.EPON.queryMvlanUser , 900, 620, "/epon/igmp/showIgmpForwarding.tv?entityId="+
                entityId + "&forwardingType=2" , null, true, true);     
    }else if(deviceIgmpMode == 2){
        window.parent.createDialog("igmpForwarding", I18N.EPON.queryMvlanUser , 900, 620, "/epon/igmp/showIgmpForwarding.tv?entityId="+
                entityId  + "&forwardingType=3", null, true, true);     
    }
}
function igmpSnoopingHandler(){
    window.parent.createDialog("igmpSnooping", I18N.IGMP.igmpSnooping , 640, 460, "/epon/igmp/showIgmpSnooping.tv?entityId="+
            entityId, null, true, true); 
}
function aclCfgMgmtHandler(){
    window.parent.createDialog("aclList", I18N.EPON.aclCfgMgmt, 1000, 500, "/epon/acl/showAclList.tv?aclPortJspFlag=1&entityId="+entityId , null, true, true);
}
function qosQueneMapHandler(){
    var entityId = getEntityId();
    window.top.createDialog('qosPortMap', I18N.EPON.qosQueneMap, 600, 370, 'epon/qos/showQosOltMap.tv?entityId='+entityId, null, true, true);
}
function qosQuenePolicyHandler(){
    var entityId = getEntityId();
    window.top.createDialog('qosPortPolicy', I18N.EPON.qosQuenePolicy , 700, 540, 'epon/qos/showQosOltPolicy.tv?entityId='+entityId, null, true, true);
}
function comVlanHandler(){
    window.top.createDialog('elecOnuComVlan', I18N.ELEC.comVlan, 600, 250, 'epon/elec/showElecComVlan.tv?entityId='+entityId, null, true, true);
}
function comCutoverHandler(){
    window.top.createDialog('ponCutOver', I18N.ELEC.comCutover, 600, 265, 'epon/elec/showElecPonCutover.tv?entityId=' + entityId, null, true, true);
}
function learnMacListHandler(){
    window.top.createDialog('oltMacListTab', I18N.ELEC.oltLearnMacList, 600, 430, '/epon/showOltMacListTab.tv?entityId=' + entityId, null, true, true);
}
function preConfigHandler(){
    var preConfigType = olt.slotList[currentId.split("_")[1]-1].topSysBdPreConfigType;
    var actualConfigType = olt.slotList[currentId.split("_")[1]-1].topSysBdActualType;
    window.top.createDialog('preConfigMgmt', I18N.EPON.preCfgMgmt, 600, 270, 'epon/showPreConfigMgmt.tv?entityId=' + entityId + '&slotId=' + getActualId()
        + '&currentId=' + currentId + '&preConfigType=' + preConfigType + '&actualConfigType=' + actualConfigType + '&r=' + Math.random(), null, true, true);
}

function deleteBoardHandler(){
    var slotId = olt.slotList[currentId.split("_")[1]-1].slotId;
    window.parent.showConfirmDlg("@EPON.tip@", I18N.EPON.cfmDeleteBoard, function(type) {
        if (type == 'no') {return;}
        window.top.showWaitingDlg("@EPON.wait@", I18N.EPON.deletingBoard);
        $.ajax({
            url:"/epon/modifyPreconfigMgmt.tv",
            data: {entityId : entityId, boardId : getActualId(),preConfigType : 0},
            timeout: 600000,dataType:'json',
            success: function (json) {
                window.parent.closeWaitingDlg()
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.deleteBoardError)
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+I18N.EPON.deleteBoardOk+'</b>'
        			});
                    //window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.deleteBoardOk)
                    reloadOltJson()
                }
            }, error: function () {
                window.parent.closeWaitingDlg()
                window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.deleteBoardError);
            }, data: {entityId : entityId, slotId : slotId, preConfigType : 0}})
    });
}
function restoreBoardHandler(){
    window.parent.showConfirmDlg("@EPON.tip@", I18N.EPON.cfmResetBoard, function(type) {
        if (type == 'no') 
            return
        window.top.showWaitingDlg("@EPON.wait@", I18N.EPON.resetingBoard);
        $.ajax({
            url: '/epon/resetOltBoard.tv?', cache:false, method:'post',
            data: {entityId : entityId, boardId : getActualId()},
            success: function(response) {
                window.parent.closeWaitingDlg();
                var text = response;
                if(text == 'success'){
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">@EPON.resetBoardOk@</b>'
        			});
                }
                else{
                    window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.resetBoardError)
                }
            },
            error: function(){
                window.parent.closeWaitingDlg()
                window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.resetBoardError)
            }
        });
    });
}
function boardEnableHandler(node){
    var $slot = selectionModel.getSelected();
    //板卡可以重复使能，但是不能去
    var BAdminStatus = $slot.BAdminStatus == 1 ? 2 : 1;
    var action = BAdminStatus == 1 ? I18N.EPON.open : I18N.EPON.close;
    var messageText = '';
    /*if(BAdminStatus == 2){
         if(entityVersion.contains("1.7")){
        	 return top.afterSaveOrDelete({
        		 title: "@EPON.tip@",
        		 html: '<b class="orangeTxt">@EPON.notActive@</b>'
        	 });
         }		 
    }*/
    window.parent.showConfirmDlg("@EPON.tip@", String.format(I18N.SUPPLY.cfmBDEn , messageText , action ), function(type) {
        if (type == 'no') {return;}
        window.top.showWaitingDlg("@EPON.wait@", String.format(I18N.SUPPLY.settingBDEn ,  action ));
        $.ajax({
            url: '/epon/boardAdminStatusConfig.tv',
            data: "entityId=" + entityId + "&slotId=" + getActualId() + "&boardStatus=" + BAdminStatus + "&r=" + Math.random(),
            success: function(text) {
                if (text != "success") {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.boardEnableError)
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.boardEnableOk+'</b>'
        			});
                    //window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.boardEnableOk)
                    window.location.reload()
                }
            }, error: function(text) {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.boardEnableError)
            },cache: false
    });
    });
}
function tempCheckEnableHandler(node){
    var $slot = selectionModel.getSelected();
    var topSysBdTempDetectEnable = $slot.topSysBdTempDetectEnable == 1 ? 2 : 1;
    var action =  topSysBdTempDetectEnable == 1 ? I18N.EPON.open : I18N.EPON.close;
    window.parent.showConfirmDlg("@EPON.tip@", String.format(I18N.SUPPLY.cfmActionTemplCheckEn , action ), function(type) {
        if (type == 'no') {return;}
        window.top.showWaitingDlg("@EPON.wait@", String.format(I18N.SUPPLY.settingTemplCheckEn ,action ));
        $.ajax({
            url: '/epon/setTempDetectEnable.tv',dataType:'json',
            data: "entityId=" + entityId + "&boardId=" + getActualId() + "&topSysBdTempDetectEnable=" + topSysBdTempDetectEnable + "&r=" + Math.random(),
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.boardTempCheckError)
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.boardTempCheckOk+'</b>'
        			});
                    //window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.boardTempCheckOk)
                    $slot.topSysBdTempDetectEnable = topSysBdTempDetectEnable;
                    modifyItemIcon( node );
                }
            }, error: function() {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.boardTempCheckError)
            }, dataType: 'json', cache: false
        });
    });
}
function switchHandler(){
    window.parent.showConfirmDlg("@EPON.tip@", I18N.EPON.switchMaster, function(type) {
        if (type == 'no') {return;}
        window.top.showWaitingDlg("@EPON.wait@", I18N.EPON.switching);
        $.ajax({
            url: '/epon/switchoverOlt.tv',
            data: "entityId=" + entityId,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", json.message);
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+I18N.EPON.switchOk+'</b>'
        			});
                    //window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.switchOk);
                    // 主备倒换成功后，修改右键菜单。
                    var n = currentId.split('_')[1];
                    if (n == '4') {
                        // 9号板为主控板，主备倒换成功后变为备用板
                        olt.slotList[3].BAttribute = 2;
                        // 10号板为备用板，主备倒换成功后变为主控板
                        olt.slotList[4].BAttribute = 1;
                    } else if (n == '5') {
                        // 10号板为主控板，主备倒换成功后变为备用板
                        olt.slotList[4].BAttribute = 2;
                        // 9号板为备用板，主备倒换成功后变为主控板
                        olt.slotList[3].BAttribute = 1;
                    }
                }
            }, error: function() {
                window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.switchError);
            }, dataType: 'json', cache: false
        });
    });
}

function softwareUpdateHandler(){
	$.ajax({
		url: "/system/checkFtpAvailable.tv",dataType:'json',
		success:function(data){
			if(data.status){
				window.parent.createDialog("boardSoftwareUpdate", I18N.EPON.boardSoftUpdate, 600, 370, "epon/showOltControlBoardUpdate.tv?entityId="+entityId + "&boardId=" + getActualId(), null, true, false);
			}else{
				ftpServiceNoEnable();
			}
		},error:function(){
			ftpServiceNoEnable();
		}
	});
}

function ftpServiceNoEnable(){
	 window.parent.showMessageDlg("@EPON.tip@" , I18N.EPON.ftpNotStart ,null);  
}

function syncBDHandler(){
    window.parent.createDialog("oltSlaveBoardSync", I18N.EPON.syncBD, 600, 250, "/epon/oltSlaveBoardSync.jsp?entityId=" + entityId, null, true, true);
}
function downlinkUpdateHandler(){
	$.ajax({
		url: "/system/checkFtpAvailable.tv",dataType:'json',
		success:function(data){
			if(data.status){
				 window.top.createDialog('onuUpgrade', I18N.EPON.onuUpdateMgmt, 800, 500, '/epon/onuUpgrade.jsp?slotNo=' + currentId.split('_')[1] + '&entityId=' + entityId + '&r=' + Math.random(), null, true, true);
			}else{
				ftpServiceNoEnable();
			}
		},error:function(){
			ftpServiceNoEnable();
		}
	});
}
function fanSpeedAdjustHandler(){
    var fanSpeedLevel = olt.fanList[currentId.split("_")[1]-1].topSysFanSpeedControl
    window.top.createDialog('fanConfig', I18N.EPON.fanSpeedAdjust, 600, 250, '/epon/fanConfig.jsp?entityId=' + entityId + '&fanCardId=' + getActualId()
            + '&fanSpeedLevel=' + fanSpeedLevel + '&currentId=' + currentId + '&r=' + Math.random(), null, true, true);
}
function sniBasicConfigHandler(){
    var sniId = getActualId()
    var entityId = getEntityId()
    var	sniName = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).sniPortName
    var sniAutoNegotiationMode = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).sniAutoNegotiationMode
    var portSubType = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).portSubType
    window.top.createDialog('sniBaseConfig', I18N.EPON.basicConfig, 600, 265, 'epon/showSniBaseConfig.tv?tabNum=0&entityId=' + entityId + '&sniId=' + sniId + '&sniName=' + sniName + '&sniAutoNegotiationMode=' + sniAutoNegotiationMode + '&sniPortType=' + portSubType + '&currentId=' + currentId + '&r=' + Math.random(), null, true, true)
}
function sniOpticalHandler(){
    var sniId = getActualId();
    var entityId = getEntityId();
    var sniName = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).sniPortName;
    window.top.createDialog('oltOpticalInfo', 'SNI ' + sniName, 600, 450, '/epon/optical/showOltSniOptical.tv?entityId=' 
            + entityId + '&portId=' + sniId + '&r=' + Math.random(), null, true, true);
}
function portEnableHandler( node ){
    var $port = selectionModel.getSelected();
    var sniAdminStatusTemp = $port.sniAdminStatus == 1 ? 2 : 1;
    var action =  sniAdminStatusTemp == 1 ? I18N.EPON.open : I18N.EPON.close;
    window.parent.showConfirmDlg("@EPON.tip@", String.format( I18N.SUPPLY.cfmActionPortEn , action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingPortEn, action));
        $.ajax({
            url: '/epon/modifySniAdminStatus.tv',
            data: "entityId=" + entityId + "&sniId=" + getActualId() + "&sniAdminStatus=" + sniAdminStatusTemp + "&r=" + Math.random(),
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", json.message);
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.portEnableOk+'</b>'
        			});
                    //window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.portEnableOk);
                    // 设置成功后修改Icon属性和json中值
                    $port.sniAdminStatus = sniAdminStatusTemp;
                    modifyItemIcon( node );
                    var operationStatus = $port.sniOperationStatus;
                    if(action == I18N.EPON.open){
                        if(operationStatus == 1)
                            changePortState(currentId,1)
                        else if(operationStatus == 2)
                            changePortState(currentId,2)
                        else
                            changePortState(currentId,2)
                    }else if(action == I18N.EPON.close)
                        changePortState(currentId,2)
                }
            }, error: function() {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.portEnableError);
            }, dataType: 'json', cache: false
        });
    });
}

function ponPortEnableHandler( node ){
    var $port = selectionModel.getSelected();
    var ponPortAdminStatus = $port.ponPortAdminStatus == 1 ? 2 : 1;
    var action =  ponPortAdminStatus == 1 ? I18N.EPON.open : I18N.EPON.close;
    window.parent.showConfirmDlg("@EPON.tip@", String.format( I18N.SUPPLY.cfmActionPortEn , action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingPortEn, action));
        $.ajax({
            url: '/epon/modifyPonAdminStatus.tv',
            data: "entityId=" + entityId + "&ponId=" + getActualId() + "&ponAdminStatus=" + ponPortAdminStatus,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", json.message);
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.portEnableOk+'</b>'
        			});
                    //window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.portEnableOk);
                    // 设置成功后修改Icon属性和json中值
                    $port.ponPortAdminStatus = ponPortAdminStatus;
                    modifyItemIcon( node );
                    var operationStatus = $port.ponOperationStatus;
                    if(action == "@EPON.open@"){
                        if(operationStatus == 1)
                            changePortState(currentId,1)
                        else if(operationStatus == 2)
                            changePortState(currentId,2)
                        else
                            changePortState(currentId,2)
                    }else if(action == I18N.EPON.close)
                        changePortState(currentId,2)
                }
            }, error: function() {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.portEnableError);
            }, dataType: 'json', cache: false
        });
    });
}

/*function ponResetHandler(){
	 window.parent.showConfirmDlg("@EPON.tip@", 确认复位该PON口？, function(type) {
	        if (type == 'no') {
	            return;
	        }
	        window.top.showWaitingDlg("@EPON.wait@", 正在复位该PON口);
	        $.ajax({
	            url: '/epon/resetPon.tv',
	            data: "entityId=" + entityId + "&ponId=" + getActualId(),
	            success: function() {
	            	window.parent.showMessageDlg("@EPON.tip@", PON口复位成功!);
	            }, error: function() {
	                window.parent.showMessageDlg("@EPON.tip@", PON口复位失败!);
	            }, cache: false
	        });
	    });
}*/

function isolatedEnableHandler(node){
    var $port = selectionModel.getSelected();
    var sniIsolationEnable = $port.sniIsolationEnable == 1 ? 2 : 1;
    var action =  sniIsolationEnable == 1 ? I18N.EPON.open : I18N.EPON.close
        window.parent.showConfirmDlg("@EPON.tip@",  String.format( I18N.SUPPLY.cfmIsolatedEn ,  action ), function(type) {
            if (type == 'no') {return;}
            window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingIsolatedEn, action ));
            $.ajax({
                url: '/epon/modifySniIsolationStatus.tv',  type: 'POST',
                data: "entityId=" + entityId + "&sniId=" + getActualId() + "&sniIsolationStatus=" + sniIsolationEnable,
                success: function(json) {
                    if (json.message) {
                        window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.isolatedError);
                    } else {
                    	top.afterSaveOrDelete({
            				title: "@EPON.tip@",
            				html: '<b class="orangeTxt">'+action +I18N.EPON.isolatedOk+'</b>'
            			});
                        //window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.isolatedOk);
                        // 设置成功后修改Icon属性和json中值
                        $port.sniIsolationEnable = sniIsolationEnable;
                        modifyItemIcon( node );
                    }
                }, error: function() {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.isolatedError);
            }, dataType: 'json', cache: false
        })
    })
}
function ponPortIsolatedEnableHandler(node){
    var $port = selectionModel.getSelected();
    var ponPortIsolationEnable = $port.ponPortIsolationEnable == 1 ? 2 : 1;
    var action =  ponPortIsolationEnable == 1 ? I18N.EPON.open : I18N.EPON.close
        window.parent.showConfirmDlg("@EPON.tip@",  String.format( I18N.SUPPLY.cfmIsolatedEn ,  action ), function(type) {
            if (type == 'no') {return;}
            window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingIsolatedEn, action ));
            $.ajax({
            	url: '/epon/modifyPonIsolationStatus.tv',
                data: "entityId=" + entityId + "&ponId=" + getActualId() + "&ponIsolationStatus=" + ponPortIsolationEnable,
                success: function(json) {
                    if (json.message) {
                        window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.isolatedError);
                    } else {
                    	top.afterSaveOrDelete({
            				title: "@EPON.tip@",
            				html: '<b class="orangeTxt">'+action +I18N.EPON.isolatedOk+'</b>'
            			});
                        //window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.isolatedOk);
                        // 设置成功后修改Icon属性和json中值
                        $port.ponPortIsolationEnable = ponPortIsolationEnable;
                        modifyItemIcon( node );
                    }
                }, error: function() {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.isolatedError);
            }, dataType: 'json', cache: false
        })
    })
}

function ponRogueSwitchHandler(node){
	var $port = selectionModel.getSelected();
    var ponRogueSwitch = $port.ponRogueSwitch == 1 ? 2 : 1;
    var action =  $port.ponRogueSwitch == 1 ? '@EPON.close@' : '@EPON.open@'
        window.parent.showConfirmDlg("@EPON.tip@",  String.format('@EPON.confirmPonRogueSwitch@',  action ), function(type) {
            if (type == 'no') {return;}
            window.top.showWaitingDlg("@EPON.wait@", String.format('@EPON.openPonRogueSwitch@', action ));
            $.ajax({
            	url: '/onu/rogueonu/modifyPonRogueSwitch.tv',
                data: "entityId=" + entityId + "&ponId=" + getActualId() + "&ponRogueSwitch=" + ponRogueSwitch,
                success: function(json) {
                    	top.afterSaveOrDelete({
            				title: "@EPON.tip@",
            				html: '<b class="orangeTxt">'+action +'@EPON.openPonRogueSuc@'+'</b>'
            			});
                        //window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.isolatedOk);
                        // 设置成功后修改Icon属性和json中值
                        $port.ponRogueSwitch = ponRogueSwitch;
                        modifyItemIcon(node);
                }, error: function() {
                    window.parent.showMessageDlg("@EPON.tip@", action + '@EPON.openPonRogueFail@');
            }, dataType: 'json', cache: false
        })
    })
}
function stastic15PerfHandler(node){
    var $port = selectionModel.getSelected();
    var sniPerfStats15minuteEnable = $port.sniPerfStats15minuteEnable == 1 ? 2 : 1;
    var action =  sniPerfStats15minuteEnable == 1 ? I18N.EPON.open : I18N.EPON.close
    window.parent.showConfirmDlg("@EPON.tip@", String.format( I18N.SUPPLY.cfmStastic15En, action ), function(type) {
        if (type == 'no')
            return                    
        window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingStastic15En , action));
        $.ajax({
            url: '/epon/modifySni15MinPerfStatus.tv',  type: 'POST',
            data: "entityId=" + entityId + "&sniId=" + getActualId() + "&sni15MinPerfStatus=" + sniPerfStats15minuteEnable,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic15Error)
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.stastic15Ok+'</b>'
        			});
                	//window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic15Ok);
                	$port.sniPerfStats15minuteEnable = sniPerfStats15minuteEnable;
                    modifyItemIcon( node );
                }
            }, error: function() {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic15Error);
            }, dataType: 'json', cache: false
        })
    })
}

function stasticPerfHandler(node){
    var $port = selectionModel.getSelected();
    var sniPerfStats15minuteEnable = $port.sniPerfStats15minuteEnable == 1 ? 2 : 1;
    var action =  sniPerfStats15minuteEnable == 1 ? I18N.EPON.open : I18N.EPON.close
    window.parent.showConfirmDlg("@EPON.tip@", String.format( I18N.SUPPLY.cfmStasticEn, action ), function(type) {
        if (type == 'no')
            return                    
        window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingStasticEn , action));
        $.ajax({
            url: '/epon/modifySni15MinPerfStatus.tv',  type: 'POST',
            data: "entityId=" + entityId + "&sniId=" + getActualId() + "&sni15MinPerfStatus=" + sniPerfStats15minuteEnable,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stasticError)
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.stasticOk+'</b>'
        			});
                	//window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stasticOk);
                	$port.sniPerfStats15minuteEnable = sniPerfStats15minuteEnable;
                    modifyItemIcon( node );
                }
            }, error: function() {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stasticError);
            }, dataType: 'json', cache: false
        })
    })
}
function stastic24PerfHandler(node){
    var $port = selectionModel.getSelected();
    var sniPerfStats24hourEnable = $port.sniPerfStats24hourEnable == 1 ? 2 : 1
    var action =  sniPerfStats24hourEnable == 1 ? I18N.EPON.open : I18N.EPON.close
    window.parent.showConfirmDlg("@EPON.tip@", String.format( I18N.SUPPLY.cfmStastic24En, action ), function(type) {
        if (type == 'no') 
            return                    
        window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingStastic24En , action), 
                'ext-mb-waiting')
        $.ajax({
            url: '/epon/modifySni24HourPerfStatus.tv', type: 'POST',
            data: "entityId=" + entityId + "&sniId=" + getActualId() + "&sni24HourPerfStatus=" + sniPerfStats24hourEnable,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic24Error);
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.stastic24Ok+'</b>'
        			});
                	//window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic24Ok);
                	$port.sniPerfStats24hourEnable = sniPerfStats24hourEnable;
                    modifyItemIcon( node );
                }
            }, error : function() {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic24Error);
            }, dataType: 'json', cache: false
        })
    })
}
function ponStastic15PerfHandler(node){
    var $port = selectionModel.getSelected();
    var perfStats15minuteEnable = $port.perfStats15minuteEnable == 1 ? 2 : 1;
    var action =  perfStats15minuteEnable == 1 ? I18N.EPON.open : I18N.EPON.close
    window.parent.showConfirmDlg("@EPON.tip@", String.format( I18N.SUPPLY.cfmStastic15En, action ), function(type) {
        if (type == 'no')
            return                    
        window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingStastic15En , action));
        $.ajax({
        	url: '/epon/modifyPon15MinPerfStatus.tv',
            type: 'POST',
            data: "entityId=" + entityId + "&ponId=" + getActualId() + "&pon15MinPerfStatus=" + perfStats15minuteEnable,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic15Error)
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.stastic15Ok+'</b>'
        			});
                	//window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic15Ok);
                	$port.perfStats15minuteEnable = perfStats15minuteEnable;
                    modifyItemIcon( node );
                }
            }, error: function() {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic15Error);
            }, dataType: 'json', cache: false
        })
    })
}

function ponStasticPerfHandler(node){
    var $port = selectionModel.getSelected();
    var perfStats15minuteEnable = $port.perfStats15minuteEnable == 1 ? 2 : 1;
    var action =  perfStats15minuteEnable == 1 ? I18N.EPON.open : I18N.EPON.close
    window.parent.showConfirmDlg("@EPON.tip@", String.format( I18N.SUPPLY.cfmStasticEn, action ), function(type) {
        if (type == 'no')
            return                    
        window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingStasticEn , action));
        $.ajax({
        	url: '/epon/modifyPon15MinPerfStatus.tv',
            type: 'POST',
            data: "entityId=" + entityId + "&ponId=" + getActualId() + "&pon15MinPerfStatus=" + perfStats15minuteEnable,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stasticError)
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.stasticOk+'</b>'
        			});
                	//window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stasticOk);
                	$port.perfStats15minuteEnable = perfStats15minuteEnable;
                    modifyItemIcon( node );
                }
            }, error: function() {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stasticError);
            }, dataType: 'json', cache: false
        })
    })
}
function ponStastic24PerfHandler(node){
    var $port = selectionModel.getSelected();
    var perfStats24hourEnable = $port.perfStats24hourEnable == 1 ? 2 : 1
    var action =  perfStats24hourEnable == 1 ? I18N.EPON.open : I18N.EPON.close
    window.parent.showConfirmDlg("@EPON.tip@", String.format( I18N.SUPPLY.cfmStastic24En, action ), function(type) {
        if (type == 'no') 
            return                    
        window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingStastic24En , action), 
                'ext-mb-waiting')
        $.ajax({
        	url: '/epon/modifyPon24HourPerfStatus.tv',
            type: 'POST',
            data: "entityId=" + entityId + "&ponId=" + getActualId() + "&pon24HourPerfStatus=" + perfStats24hourEnable,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic24Error);
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+action +I18N.EPON.stastic24Ok+'</b>'
        			});
                	//window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic24Ok);
                	$port.perfStats24hourEnable = perfStats24hourEnable;
                    modifyItemIcon( node );
                }
            }, error : function() {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.stastic24Error);
            }, dataType: 'json', cache: false
        })
    })
}
function curPerfHandler(){
    var slotNo = currentId.split("_")[1];
    window.top.createDialog('realPerf', I18N.EPON.stasticCurPerf , 840, 560, 'epon/perf/showCurPerf.tv?deviceType=olt&slotNo='+ slotNo +'&portIndex=' + getActualIndex() + '&entityId=' + entityId, null, true, true);
}
function stastic15Handler(){
    var slotNo = currentId.split("_")[1];
    window.parent.perfData = {slotNo: slotNo, portIndex: getActualIndex()};
    window.top.createDialog('15minPerf', I18N.EPON.stastic15Perf, 840, 560, 'epon/perf/show15Perf.tv?deviceType=olt&slotNo='+ slotNo +'&portIndex='+ getActualIndex() + '&entityId=' + entityId, null, true, true);
}
function stastic24Handler(){
    var slotNo = currentId.split("_")[1];
    window.parent.perfData = {slotNo: slotNo, portIndex: getActualIndex()};
    window.top.createDialog('24hourPerf', I18N.EPON.stastic24Perf, 840, 560, 'epon/perf/show24Perf.tv?deviceType=olt&slotNo='+ slotNo +'&portIndex=' + getActualIndex() + '&entityId=' + entityId, null, true, true);
}
function flowControlEnableHandler(node){
    var $port = selectionModel.getSelected();
    var topSniAttrFlowCtrlEnable = $port.topSniAttrFlowCtrlEnable == 1 ? 2 : 1;
    var action =  topSniAttrFlowCtrlEnable == 1 ? I18N.EPON.open : I18N.EPON.close
    window.parent.showConfirmDlg("@EPON.tip@", String.format( I18N.SUPPLY.cfmFlowControlEn , action ), function(type) {
        if (type == 'no'){
        	return;
        } 
        window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingFlowControlEn ,  action));
        $.ajax({
            url: '/epon/setSniCtrlFlowEnable.tv',
            data: "entityId=" + entityId + "&sniId=" + getActualId() + "&ctrlFlowEnable=" + topSniAttrFlowCtrlEnable,
            success: function(text) {
                if (text) {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.flowControlError)
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+ action + I18N.EPON.flowControlOk+'</b>'
        			});
                    //window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.flowControlOk)
                	$port.topSniAttrFlowCtrlEnable = topSniAttrFlowCtrlEnable;
                    modifyItemIcon( node );
                }
            }, error: function(e) {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.flowControlError)
            }, cache: false
        });
    });
}
function flowRestricHandler(){
	var $port = selectionModel.getSelected();
    var sniId = getActualId();
    var entityId = getEntityId();
    var ctrlFlowEnable = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).topSniAttrFlowCtrlEnable;
    var ingressRate = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).topSniAttrIngressRate;
    var egressRate = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).topSniAttrEgressRate;
    window.top.createDialog('sniFlowControl', I18N.EPON.flowRestric, 600, 260, 'epon/showSniFlowControl.tv?entityId=' + entityId + '&sniId='+ sniId + '&ingressRate=' + ingressRate + '&egressRate=' + egressRate + '&currentId=' + currentId 
        + "&sniPortType="+$port.portSubType, null, true, true);
}
function redirectHandler(){
    var sniName = 'SNI' + currentId.split('_')[1] + '/' + currentId.split('_')[2];
    window.top.createDialog('sniRedirect', I18N.EPON.redirectMgmt + sniName + ')', 600, 470, 'epon/showSniRedirect.tv?sniId=' + getActualId() + '&entityId=' + entityId, null, true, true);
}
function macLearnNumHandler(){
    window.top.createDialog('sniMacAddressMaxLearningNum', I18N.EPON.macLearnSet, 600, 220, '/epon/showSniMacAddressMaxLearningNum.tv?entityId=' + entityId + '&sniId=' + getActualId(), null, true, true);
}
function broadcastDecHandler(){
	var $slot = olt.slotList[currentId.split("_")[1]-1];
    var sniName = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).sniPortName;
    window.top.createDialog('sniBroadCastStromMgmt'  , I18N.EPON.broadCastSet+ ' ' +sniName, 600, 520, 'epon/showSniBroadCastStormMgmt.tv?sniId=' + getActualId() + '&entityId=' + getEntityId() + '&currentId=' + currentId, null, true, true);
}
function mirrorgroupHandler(){
	location.href = "showOltMirrorView.tv?module=100&entityId=" + entityId
}
function trunkgroupHandler(){
	location.href = "showOltTrunkView.tv?module=100&entityId=" + entityId
}

function sniRstpEnableHandler(node){
    var $port = selectionModel.getSelected();
    var sniId = getActualId();
    var entityId = getEntityId();
    var stpPortEnabled = $port.stpPortEnabled == 1 ? 2 : 1;
    var action =  stpPortEnabled == 1 ? I18N.EPON.open : I18N.EPON.close
    window.parent.showConfirmDlg("@EPON.tip@", String.format( I18N.SUPPLY.cfmActionSniRstp ,  action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg("@EPON.wait@", String.format( I18N.SUPPLY.settingSniRstp , action ));
        $.ajax({
            url: '/epon/rstp/setStpPortEnable.tv',
            type: 'POST',
            data: "entityId=" + entityId + "&sniId=" + getActualId() + "&stpPortEnabled=" + stpPortEnabled + "&r=" + Math.random(),
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.sniRstpError)
                } else {
                	top.afterSaveOrDelete({
        				title: "@EPON.tip@",
        				html: '<b class="orangeTxt">'+ action + I18N.EPON.sniRstpOk+'</b>'
        			});
                    //window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.sniRstpOk)                       
                    $port.stpPortEnabled = stpPortEnabled;
                    modifyItemIcon( node );
                }
            }, error: function() {
                window.parent.showMessageDlg("@EPON.tip@", action + I18N.EPON.sniRstpError)
            }, dataType: 'json', cache: false
        });
        });
}
function sniRstpSettingHandler(){
    var $port = selectionModel.getSelected();
    var sniId = getActualId();
    var entityId = getEntityId();
    var sniIndex = $port.portIndex;
    var sniAutoNegotiationMode = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).sniAutoNegotiationMode;
    window.top.createDialog('sniRstpConfig', I18N.EPON.sniRstpSet, 640, 460, 'epon/rstp/getStpPortConfigAttribute.tv?portId=' + sniId + '&entityId=' + entityId +'&sniIndex=' + sniIndex+ '&sniAutoNegotiationMode=' + sniAutoNegotiationMode, null, true, true);
}
function sniRstpProtocoMigHandler(){
    var $port = selectionModel.getSelected();
    var sniId = getActualId()
    var entityId = getEntityId()
    var stpPortRstpProtocolMigrationTemp = $port.stpPortRstpProtocolMigrationTemp == 1 ? 2 : 1
    window.parent.showConfirmDlg("@EPON.tip@", I18N.EPON.cfmSniProtocoMig, function(type) {
        if (type == 'no') return
        window.top.showWaitingDlg("@EPON.wait@", I18N.EPON.migrating);
        $.ajax({
            url: '/epon/rstp/setRstpProtocolMigration.tv',cache:false,
            data: "entityId=" + entityId + "&sniId=" + getActualId() + "&status=1",
            success: function(result) {
               if (result == "success"){
            	   top.afterSaveOrDelete({
       					title: "@EPON.tip@",
       					html: '<b class="orangeTxt">'+ I18N.EPON.migrateOk +'</b>'
       				});
            	   //window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.migrateOk)
               }else{
            	   window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.migrateError)      
               }
            }, error: function() {
               window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.migrateErro)
           },cache: false
        });
   });
}
function indirectHandler(){
    var portIndex = getActualIndex()
    showAclPort(portIndex,0)
}
function outdirectHandler(){
    var portIndex = getActualIndex()
    showAclPort(portIndex,1)
}
function sniQueneMapHandler(){
    var sniId = getActualId();
    var entityId = getEntityId();
    var sniLoc = currentId.split("_")[1] +"/"+ currentId.split("_")[2];
    window.top.createDialog('qosPortMap', I18N.EPON.qosPortQueneMap + '(SNI:'+ sniLoc +')',  600, 370, 'epon/qos/showQosPortMap.tv?entityId='+entityId+'&sniId='+sniId+'&portIndex='+getActualIndex(), null, true, true);
}
function sniQuenePolicyHandler(){
    var sniId = getActualId();
    var entityId = getEntityId();
    timer = Number.NEGATIVE_INFINITY;
    var sniIndex = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).portIndex;
    var sniName = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).sniPortName;
    var sniLoc = currentId.split("_")[1] +"/"+ currentId.split("_")[2];
    window.top.createDialog('qosPortPolicy', I18N.EPON.qosPortQuenePolicy + '(SNI:'+ sniLoc +')', 700, 540, 'epon/qos/showQosPortPolicy.tv?entityId='+entityId+'&sniId='+sniId+'&portIndex='+getActualIndex(), null, true, true);
}
function vlanCfgHandler(){
    var sniId = getActualId();
    var sniIndex = getPort(olt.slotList[currentId.split("_")[1]-1].portList, currentId.split("_")[2]).portIndex;// olt.slotList[currentId.split("_")[1]-1].portList[currentId.split("_")[2]-1].portIndex;
    var entityId = getEntityId();
    var portRealIndex = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).portRealIndex;
    var sniName = getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).sniPortName;
    window.top.createDialog('sniVlanConfig', I18N.EPON.sniVlanCfg , 600, 380, 'epon/vlan/getSniPortVlanAttribute.tv?entityId='+entityId+'&sniId='+sniId+'&portRealIndex='+portRealIndex+'&sniIndex='+sniIndex + '&sniName=' + sniName, null, true, true);
}
function ponBasicConfigHandler(){
    var _port = olt.slotList[currentId.split("_")[1]-1].portList[currentId.split("_")[2]-1]
    var encryptMode = _port.ponPortEncryptMode
    var exchangeTime = _port.ponPortEncryptKeyExchangeTime
    var maxMac = _port.ponPortMacAddrLearnMaxNum
    var ponName = 'PON' + currentId.split('_')[1] + '/' + currentId.split('_')[2];
    window.top.createDialog('ponBaseConfig', I18N.EPON.encryptSet + '(' + ponName + ')', 600, 280, 'epon/showPonBaseConfig.tv?entityId=' + getEntityId() + '&ponId=' + getActualId() + '&encryptMode=' + encryptMode + '&exchangeTime=' + exchangeTime + '&maxMac=' + maxMac + 
        '&currentId=' + currentId + '&r=' + Math.random(), null, true, true)
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

function ponOpticalAlarmHandler(){
	window.top.createDialog('oltPonOpticalAlarm', I18N.EPON.ponOptAlertMgmt, 630, 370, '/epon/optical/showOltPonOpticalAlarm.tv?entityId=' + getEntityId() + '&portIndex=' + getActualIndex(), null, true, true);
}

function ponOpticalHandler(){
    timer = Number.NEGATIVE_INFINITY;
    var ponId = getActualId();
    var entityId = getEntityId();
    var ponName = 'PON' + currentId.split('_')[1] + '/' + currentId.split('_')[2];
    window.top.createDialog('oltOpticalInfo', ponName, 600, 450,'/epon/optical/showOltPonOptical.tv?entityId=' 
            + entityId + '&portId=' + ponId + '&r=' + Math.random(), null, true, true);
} 
function ponRateHandler(){
    var slotNo = currentId.split("_")[1];
    window.top.createDialog('ponPortRateLmt', I18N.EPON.portRateUpAndDown , 600, 265, '/epon/showPonPortLmt.tv?entityId=' + getEntityId() + '&ponId=' + getActualId()+ '&portIndex=' + getActualIndex(), null, true, true);
}
function ponSpeedModeHandler(){
    var $pon = selectionModel.getSelected();
    var $url =  '/epon/showPonSpeedMode.tv?entityId=' + getEntityId() + '&ponId=' + getActualId()+ '&portIndex=' + getActualIndex()+'&currentId=' + currentId+'&portType='+$pon.portSubType;
    window.top.createDialog('ponSpeedMode', I18N.EPON.ponSpeedMode, 600, 260, $url , null, true, true);
}
function downViewHandler(){
    var slotId = olt.slotList[currentId.split("_")[1]-1].slotId
    var ponId = getActualId();
    location.href = "/onu/showOnuViewJsp.tv?module=200&entityId=" + entityId + "&slotId=" + slotId + "&ponId=" + ponId;
}
function onuAuthHandler(){
    var slotId = olt.slotList[currentId.split("_")[1]-1].slotId;
    var ponId = getActualId();
    onuAuthenData = {slotId: slotId, ponId: ponId};
    window.top.createDialog('onuAuthen', I18N.EPON.onuAuthMgmt, 930, 520, '/epon/onuauth/showOnuAuthen.tv?entityId=' + entityId 
            + '&slotId=' + slotId + '&ponId=' + ponId, null, true, true);
}
function ponMacLearnNumHandler(){
    window.top.createDialog('ponMacAddrMaxLearningNum', I18N.EPON.macLearnSet, 600, 250, '/epon/showPonMacLearnMaxNnm.tv?entityId='+ entityId + '&ponId='+ getActualId(), null, true, true);
}
//针对1.6.X版本
function ponMacLearnNumHandler2(){
	window.top.createDialog('ponMacLearningNumSet', I18N.EPON.macLearnSet, 600, 250, '/epon/showPonMacNum.tv?entityId='+ entityId + '&ponId='+ getActualId(), null, true, true);
}
function ponBroadcastDecHandler(){
    var ponPortType = olt.slotList[currentId.split("_")[1]-1].portList[currentId.split("_")[2]-1].ponPortType;
    var ponName = ' PON ' + currentId.split('_')[1] + '/' + currentId.split('_')[2];
    window.top.createDialog('ponBroadStormConfig', I18N.EPON.broadCastSet + ponName,600, 520,  'epon/showPonBroadStormConfig.tv?entityId=' + getEntityId() + '&ponId=' + getActualId() + '&ponPortType=' + ponPortType, null, true, true);
}
function ponQueneMapHandler(){
    var ponId = getActualId();
    var entityId = getEntityId();
    var ponLoc = currentId.split("_")[1] +"/"+ currentId.split("_")[2];
    window.top.createDialog('qosPortMap', I18N.EPON.qosPortQueneMap + '(PON:'+ ponLoc +')',  600, 370, 'epon/qos/showQosPortMap.tv?entityId='+entityId+'&portIndex='+getActualIndex(), null, true, true);
}
function ponQuenePolicyHandler(){
    var ponId = getActualId()
    var entityId = getEntityId()
    var ponLoc = currentId.split("_")[1] +"/"+ currentId.split("_")[2]
    window.top.createDialog('qosPortPolicy', I18N.EPON.qosPortQuenePolicy + '(PON:'+ ponLoc +')', 650, 540, 
        'epon/qos/showQosPortPolicy.tv?entityId='+entityId+'&portIndex='+getActualIndex(), null, true, true)
}
function ponBasedVlanHandler(){
    window.top.createDialog('ponVlanConfig', I18N.EPON.vlanCfgMgmtPon, 820, 550, '/epon/vlan/showPonVlanConfig.tv?tabNum=0&entityId=' + getEntityId() + '&ponId=' + getActualId() + '&currentId=' + currentId, null, true, true);
}
function llidBasedVlanHandler(){
    window.top.createDialog('ponVlanLlidConfig', I18N.EPON.vlanCfgMgmtLLID, 1010, 550, '/epon/vlan/showPonVlanLlidConfig.tv?tabNum=0&entityId=' + getEntityId() + '&ponId=' + getActualId() + '&currentId=' + currentId, null, true, true);
}

/******************************************
	打开ACL端口页面
******************************************/
function showAclPort(index,direction){
	var $splits = currentId.split("_");
	var $board = olt.slotList[ $splits[1]-1 ].slotLogicNum
	var portLoc = $board +"/"+ $splits[2];
	window.parent.createDialog("aclPort",  String.format(I18N.ACL.portBundAclList , portLoc), 640, 450, "/epon/acl/showAclPort.tv?entityId="+entityId+ "&portIndex=" + index + "&direction=" + direction + "&aclPortJspFlag=0", null, true, true);	
}

/******************************************
打开LoopBack接口配置页面
******************************************/
function showLoopBackConfig(){
	window.top.createDialog('loopBackConfig', "@loopbackInterfaceCfg@", 800, 500, "/epon/loopback/showLoopBackConfig.tv?entityId="+entityId, null, true, true);
}

/******************************************
打开logicInterface接口管理页面
******************************************/
function showLogicInterfaceConfig(){
	var entityId = getEntityId()
	var name = entityName;
	window.parent.addView('OLTLogicInterface-' + entityId, "@logicInterfaceCfg@[" + name + "]" , 'entityTabIcon','/epon/logicinterface/showLogicInterfaceView.tv?entityId=' + entityId + '&interfaceType=1');
}

/******************************************
打开IpRoute配置页面
******************************************/
function showIpRouteConfig(){
	window.top.createDialog('ipRouteConfig', "@staticRouteCfg@", 800, 500, "/epon/iproute/showIpRouteConfig.tv?entityId="+entityId, null, true, true);
}

/******************************************
打开Uni Vlan模板配置页面
******************************************/
function showUniVlanProfile(){
	window.top.createDialog('uniVlanProfile', "@univlanprofileCfg@", 800, 500, "/epon/univlanprofile/showUniVlanProfile.tv?entityId="+entityId, null, true, true);
}

/******************************************
打开PON QOS页面
******************************************/
function showPonQos(){
	window.parent.addView("ponQos", 'PON QOS', "dashboardIcon", 'epon/ponqos/ponQosUscList.jsp');
}
/******************************************
查询PON QOS端口绑定页面
******************************************/
function searchPonQosPortBinding(){
	window.parent.addView("searchPonQosPortBinding", '@queryPortBind@', "dashboardIcon", 'epon/ponqos/searchPonQosPortBinding.jsp');
}
/******************************************
查询配置记录页面
******************************************/
function ponQosSearchHistory(){
	window.parent.addView("ponQosSearchHistory", '@queryCfgRecord@', "dashboardIcon", 'epon/ponqos/ponQosSearchHistory.jsp');
}
function ponQosSLA(){
	window.parent.addView("ponQosSLA", '@querySlaProfileList@', "dashboardIcon", 'epon/ponqos/ponQosSlaList.jsp');
}
function showBatchHistory(){
	window.top.addView("batchHistoryQuery", '@batchdeployQuery@', "dashboardIcon", '/batchdeploy/showBatchHistory.tv?entityId='+entityId);
}

function showUniRateBatchConfig(){
	window.top.createDialog('uniRateBatchConfig', "@SERVICE.batchUniRateLimit@", 800, 500, "/onu/showUniRateBatchConfig.tv?entityId="+entityId, null, true, true);
}

function showBatchPerfCollectSetting(){
	window.top.createDialog('batchPerfCollect', "@EPON.batchPerfCollect@", 800, 500, "/epon/showBatchPerfCollectSetting.tv?entityId="+entityId, null, true, true);
}

function macInfoHandler(){
    window.top.createDialog('macInfo', '@EPON.macInfoCofirm@', 600, 300, '/epon/showMacInfoJsp.tv?entityId=' + entityId, null, true, true);
}

/******************************************
打开ONU业务模板页面
******************************************/
function showOnuProfile(){
	window.top.createDialog('onuProfile', "@ONU.onuProfile@", 800, 500, "/epon/businessTemplate/showOnuProfile.tv?entityId="+entityId, null, true, true);
}

/******************************************
打开VLAN业务模板页面
******************************************/
function showVlanProfile(){
	window.top.createDialog('onuVlanProfile', "@ONU.vlanProfile@", 800, 500, "/epon/businessTemplate/showVlanProfile.tv?entityId="+entityId, null, true, true);
}

/******************************************
打开IGMP业务模板页面
 ******************************************/
function showIgmpProfile(){
	window.top.createDialog('igmpProfile', "@ONU.igmpProfile@", 800, 500, "/epon/businessTemplate/showIgmpProfile.tv?entityId="+entityId, null, true, true);
}

/******************************************
打开能力集模板页面
 ******************************************/
function showCapabilityProfile(){
	window.top.createDialog('capabilityProfile', "@ONU.capabilityProfile@", 800, 500, "/epon/businessTemplate/showCapabilityProfile.tv?entityId="+entityId, null, true, true);
}

/******************************************
打开PVID配置页面
 ******************************************/
function showPonPvidConfig(){
    var entityId = getEntityId()
    var portIndex = getActualIndex();
    var ponId = getActualId();
    window.top.createDialog('ponPvidConfig', "@PON.pvidConfig@", 600, 270, "/epon/showPonPvidConfig.tv?entityId="+entityId+"&ponId="+ponId+"&portIndex="+portIndex, null, true, true);
}

function showOltIgmpConfig(){
	var entityId = getEntityId()
	var name = entityName;
	window.parent.addView('OLTIGMP-' + entityId, "OLT IGMP[" + name + "]" , 'entityTabIcon','/epon/igmpconfig/showOltIgmpConfig.tv?entityId=' + entityId);
}
