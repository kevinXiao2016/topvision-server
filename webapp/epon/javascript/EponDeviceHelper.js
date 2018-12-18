/**
 * 初始化OLT业务项
 * @param {EventObject}e
 */
function showOltService(e){
    var  globalRstpStatus = olt.stpGlobalSetEnable == STATUS.UP,
         systemRogueCheck = olt.systemRogueCheck == STATUS.UP,
        //distributedwithcm(2):可控, disabled(3): 关闭, distributedwocm(4): 代理 
    	igmpSnooping = deviceIgmpMode == 4,
        igmpControl = deviceIgmpMode == 2,
        igmpClose = deviceIgmpMode == 3,
        igmpProxy = deviceIgmpMode == 1;
    var items = []
    //第一级菜单以及其子菜单
    //if(Zeta.isSupported("basic"))...
    items[items.length] = {id:'oltBasicConfig',text: I18N.EPON.basicConfig};
        var subItems = [];
        subItems[subItems.length] = {id:'oltSaveConfig',text: I18N.EPON.saveConfig, disabled:!operationDevicePower, handler : saveConfigHandler};
        subItems[subItems.length] = {id:'oltSysTimeConfig',text: I18N.EPON.checkSysTime, disabled:!operationDevicePower, handler : checkSysTimeHandler};
        subItems[subItems.length] = {id:'oltRestoreFactoryConfig',text: I18N.EPON.resoreFactoryCfg, disabled:!operationDevicePower, handler : resoreFactoryCfgHandler};
        subItems[subItems.length] = {id:'oltResetConfig',text: I18N.EPON.resetDevice, disabled:!operationDevicePower, handler : resetDeviceHandler};
        subItems[subItems.length] = {id: 'sniAgingTime',text: I18N.EPON.macAgeTime, handler : macAgeTimeHandler};
        items[items.length-1].menu = subItems;
    //if(Zeta.isSupported("snmpV3"))...
    /*
    //设备现不支持对读写共同体的配置,因而将emsSnmpConifg配置注释。同时由于版本控制导致其他子菜单都不能显示，故将整个SNMP配置菜单隐藏
    items[items.length] = {id:'snmpv3', text: I18N.SNMPv3.snmpCfg};
        var subItems = [];
        subItems[subItems.length] = {id:'usmSnmpV3UserList',text: I18N.SNMPv3.userList, handler : snmpv3UserListHandler};
        subItems[subItems.length] = {id:'usmSnmpV3GroupList',text: I18N.SNMPv3.accessList , handler : snmpv3AccessListHandler};
        subItems[subItems.length] = {id:'usmSnmpV3ViewList',text: I18N.SNMPv3.viewList, handler : snmpv3ViewListHandler};
        //subItems[subItems.length] = {id:'emsSnmpConifg',text: I18N.SNMPv3.communicate , handler : emsSnmpHandler};
        items[items.length-1].menu = subItems;
   */
    //第一级菜单
    
    //if(Zeta.isSupported("performance"))...
    items[items.length] = {id:'perfThreshold',text: I18N.EPON.perfMgmt};
        subItems = [];
        subItems[subItems.length] = {id:'collectConfig',text: I18N.EPON.collectRecordSet, handler : collectRecordSetHandler};
        subItems[subItems.length] = {id:'perfThresholdConfig',text: I18N.EPON.thresholdCfg, handler : thresholdCfgHandler};
        items[items.length-1].menu = subItems;
    //if(Zeta.isSupported("trap"))...
    items[items.length] = {id: 'oltAlertMgmt',text: I18N.EPON.alertCfg};
        subItems = [];
        subItems[subItems.length] = {id: 'oltAlertCheck',text: I18N.EPON.alertDisplay, handler : alertDisplayHandler};
        subItems[subItems.length] = {id: 'oltTrapServerMgmt',text: I18N.EPON.trapCfg, handler : trapCfgHandler};
        subItems[subItems.length] = {id: 'snmpV3Notify',text: I18N.SNMPv3.n.notify, handler : snmpv3NotifyHandler};
        subItems[subItems.length] = {id: 'oltAlertMask',text: I18N.EPON.alertDecl, handler : alertDeclHandler};
        subItems[subItems.length] = {id: 'oltOpticalAlarm',text: I18N.EPON.optThrMgmt, handler : alertOpticalHandler};
        items[items.length-1].menu = subItems;
    items[items.length] = {id:'oltfileMgmtConfig',text: I18N.EPON.fileMgmt, handler : fileMgmtHandler};
    items[items.length] = {id:'rogueOnuCheck',text: '@EPON.rogueOnuCheck@',disabled:!operationDevicePower,handler:systemRogueCheckHandler,checked:systemRogueCheck};
    items[items.length] = {id:'onuAutoUpg',text: I18N.onuAutoUpg.co.onuAutoUpg, handler : onuAutoUpgHandler};
    //if(Zeta.isSupported("rstp"))...
    //items[items.length] = {id:'oltRstp',text: I18N.EPON.rstpMgmt};
    items[items.length] = {id:'oltRstpConfig',text: I18N.EPON.rstpSetting, handler : rstpSettingHandler};
        //subItems = [];
       // subItems[subItems.length] = {id:'oltRstpEnable',text: I18N.EPON.rstpEnable,disabled:!operationDevicePower, handler : rstpEnableHandler, checked:globalRstpStatus};
        //subItems[subItems.length] = {id:'oltRstpConfig',text: I18N.EPON.rstpSetting, handler : rstpSettingHandler};
        //items[items.length-1].menu = subItems;
    //if(Zeta.isSupported("hdcp"))...
    // DHCP功能设备未实现，网管屏蔽该功能
    /*items[items.length] = {text: I18N.EPON.dhcpCfg,hidden:true};
        subItems = [];
        subItems[subItems.length] = {text: I18N.EPON.basicConfig, handler : dhcpBasicConfigHandler};
        subItems[subItems.length] = {text: I18N.EPON.dhcpServerCfg, handler : dhcpServerCfgHandler};
        subItems[subItems.length] = {text: I18N.EPON.gateCfg, handler : dhcpGateCfgHandler};
        subItems[subItems.length] = {text: I18N.EPON.dhcpDynaCfg, handler : dhcpDynaCfgHandler};
        subItems[subItems.length] = {text: I18N.EPON.dhcpDynaInfo, handler : dhcpDynaInfoHandler};
        items[items.length-1].menu = subItems;*/
    //if(Zeta.isSupported("igmp"))...
    /*items[items.length] = {id: 'IgmpConfig',text: I18N.EPON.igmpMgmt};
        subItems = [];
        subItems[subItems.length] = {id: 'IgmpProtocol', text: I18N.EPON.protocoCfg, handler : igmpProtocoCfgHandler };
        subItems[subItems.length] = {id: 'igmpGroups', text: I18N.EPON.controlbleMvlanTemp, handler : igmpControlbleMvlanTempHandler, disabled : igmpClose };
        subItems[subItems.length] = {id: 'igmpProxy', text: I18N.EPON.mvlan, handler : igmpProxyHandler, disabled :  igmpClose};
        subItems[subItems.length] = {id: 'igmpCallRecord', text: I18N.EPON.callHistory, handler : igmpCallHistoryHandler, disabled : igmpClose};
        subItems[subItems.length] = {id: 'igmpTranslation', text: I18N.EPON.mvlanTranslate, handler : igmpMvlanTranslateHandler, disabled : igmpClose};
        subItems[subItems.length] = {id: 'igmpForwarding', text: I18N.EPON.queryMvlanUser, handler : igmpQueryMvlanUserHandler, disabled : igmpClose };
        subItems[subItems.length] = {id: 'igmpSnooping', text: I18N.IGMP.igmpSnooping , handler : igmpSnoopingHandler,disabled: igmpClose};
        items[items.length-1].menu = subItems;*/
        
    items[items.length] = {id: 'oltIgmpConfig',text: I18N.EPON.igmpMgmt, handler : showOltIgmpConfig};    
    //if(Zeta.isSupported("acl"))...
    items[items.length] = {id: 'aclList', text: I18N.EPON.aclCfgMgmt, handler : aclCfgMgmtHandler};
    //if(Zeta.isSupported("qos"))...
    items[items.length] = {id: 'oltQos',text: I18N.EPON.qosCfgMgmt};
        subItems = [];
        subItems[subItems.length] = {id: 'oltQosMap', text: I18N.EPON.queneMap, handler : qosQueneMapHandler};
        subItems[subItems.length] = {id: 'oltQosPolicy', text: I18N.EPON.quenePolicy, handler : qosQuenePolicyHandler};
        items[items.length-1].menu = subItems;
    //items[items.length] = {id: 'elecOnuComVlan', text: I18N.ELEC.comVlan, handler : comVlanHandler};
    //items[items.length] = {id: 'ponCutOver', text: I18N.ELEC.comCutover, handler : comCutoverHandler};
    items[items.length] = {id: 'oltMacListTab', text: I18N.ELEC.learnMacList, handler : learnMacListHandler};
    items[items.length] = {id: 'loopBackConfig', text: "@loopbackInterfaceCfg@", handler : showLoopBackConfig};
    items[items.length] = {id: 'logicInterfaceConfig', text: "@logicInterfaceCfg@", handler : showLogicInterfaceConfig};
    items[items.length] = {id: 'ipRouteConfig', text: "@staticRouteCfg@", handler : showIpRouteConfig};
    //items[items.length] = {id: 'uniVlanProfile', text: "@univlanprofileCfg@", handler : showUniVlanProfile};
    //items[items.length] = {id: 'batchHistoryQuery', text: "@batchdeployQuery@", handler : showBatchHistory};
    
    items[items.length] = {id: 'batchConfig1',text: "@EPON.batchConfig@",hidden:true};
	    subItems = [];
	    subItems[subItems.length] = {id: 'uniVlanProfile', text: "@univlanprofileCfg@", handler : showUniVlanProfile};
	    subItems[subItems.length] = {id: 'uniRateBatchConfig', text: "@EPON.batchUniRateConfig@", handler : showUniRateBatchConfig};
	    subItems[subItems.length] = {id: 'batchPerfCollect', text: "@EPON.batchPerfCollect@", handler : showBatchPerfCollectSetting};
	    subItems[subItems.length] = {id: 'batchHistoryQuery', text: "@batchdeployQuery@", handler : showBatchHistory};
	    items[items.length-1].menu = subItems;
	//items[items.length] = {id: 'macCheck', text: '@olt.macinfolocation@', handler : macInfoHandler};
	//ONU 业务模板
    items[items.length] = {id: 'bussinessTemplate1',text: "@ONU.serviceProfile@",hidden:true};
	    subItems = [];
	    subItems[subItems.length] = {id: 'onuProfile', text: "@ONU.onuProfile@", handler : showOnuProfile};
	    subItems[subItems.length] = {id: 'onuVlanProfile', text: "@ONU.vlanProfile@", handler : showVlanProfile};
	    subItems[subItems.length] = {id: 'igmpProfile', text: "@ONU.igmpProfile@", handler : showIgmpProfile};
	    subItems[subItems.length] = {id: 'capabilityProfile', text: "@ONU.capabilityProfile@", handler : showCapabilityProfile};
	    items[items.length-1].menu = subItems;
    displayService(items,e);
    
}
function showSlotWithoutBoardService(e){
    var items = []
    items[items.length] = {id: 'slotPreConfMgmt',text: I18N.EPON.preConfig,disabled:!operationDevicePower, handler : preConfigHandler};
    displayService(items,e);
    
}
function showSlotService(e){
    var items = []
    //items[items.length] = {id: 'deleteSlotPreConf',text: I18N.EPON.deleteBoard,disabled:!operationDevicePower, handler : deleteBoardHandler};
    displayService(items,e);
}
function showGeuaService( e,hasPreconfig ){
    var slot = selectionModel.getSelectedItem(),
        temprDetetStatus = slot.topSysBdTempDetectEnable == STATUS.UP,
        adminStatus = slot.BAdminStatus == STATUS.UP,
        isBoardOnline = slot.topSysBdPreConfigType == slot.topSysBdActualType,
        hasNoPreconfig = !hasPreconfig;
    var items = [];
    //如果存在创建PN8602的方法，那么则认为这个是8602的界面
    items[items.length] = {id: 'deleteSlotPreConf',text: I18N.EPON.deleteBoard,disabled:!operationDevicePower, handler : deleteBoardHandler};
   
    if( typeof  createPN8602 == 'function' ){
    	items[items.length] = {id: 'geuaBDRestore',hidden:!isBoardOnline,text: I18N.EPON.restoreBoard, disabled:!operationDevicePower, handler : restoreBoardHandler};
        items[items.length] = {id: 'geuaUpgrade',text: I18N.EPON.softwareUpdate,disabled:!operationDevicePower || hasNoPreconfig, handler : softwareUpdateHandler };
    } else {
        items[items.length] = {id : 'geuaBDRestore',hidden:!isBoardOnline,text: I18N.EPON.restoreBoard,disabled:!operationDevicePower || hasNoPreconfig, handler : restoreBoardHandler};
        //@LIZONGTIAN说:板卡不能被删除
        //items[items.length] = {id : 'deleteSniPreConf',text: I18N.EPON.deleteBoard,disabled:!operationDevicePower || hasNoPreconfig, handler : deleteBoardHandler};
        //板卡无法进行去使能操作
        items[items.length] = {id : 'gebdAdminStatus',text: I18N.EPON.boardEnable, handler : boardEnableHandler ,disabled: !operationDevicePower || hasNoPreconfig,checked: adminStatus};
    }
    items[items.length] = {id: 'geuaTempDetectEnable', text: I18N.EPON.tempCheckEnable,disabled:!operationDevicePower, handler : tempCheckEnableHandler, checked:temprDetetStatus}; 
    if(hasNoPreconfig){
        items[items.length] = {id:"geuaPreConfig",text: I18N.EPON.preConfig, handler : preConfigHandler};
    }
    displayService(items,e);
}
function showMpuaService(e){
    var slot = selectionModel.getSelectedItem(),
        temprDetetStatus = slot.topSysBdTempDetectEnable == STATUS.UP;
    isBoardOnline = slot.topSysBdPreConfigType == slot.topSysBdActualType;
    if((slot.topSysBdPreConfigType == 13 && slot.topSysBdActualType == 9) || (slot.topSysBdPreConfigType == 12 && slot.topSysBdActualType == 6) 
    		|| (slot.topSysBdPreConfigType == 11 && slot.topSysBdActualType == 5)){
    	isBoardOnline = true;
    }
    var items = []
    
    items[items.length] = {id: 'mpuaBDRestore',hidden:!isBoardOnline,text: I18N.EPON.restoreBoard, disabled:!operationDevicePower, handler : restoreBoardHandler};
    if(olt.oltType != "PN8602" && olt.oltType != "PN8602-E" && olt.oltType != "PN8602-EF" && olt.oltType != "PN8602-G"){
    	//8602没有主控板，不会进行倒换
    	items[items.length] = {id: 'masterBDSwitch',text: I18N.EPON.Switch,disabled:!operationDevicePower, handler : switchHandler};
    }
    if(olt.oltType == "PN8602-E" || olt.oltType == "PN8602-EF" || olt.oltType == "PN8602-G"){
    	items[items.length] = {id: 'onuUpgrade',text: I18N.EPON.onuUpdate, handler : downlinkUpdateHandler};
    }
    items[items.length] = {id: 'mpuaSoftUpdate', text: I18N.EPON.softwareUpdate, disabled:!operationDevicePower, handler : softwareUpdateHandler};
    items[items.length] = {id: 'masterAlterBDSynchro',text: I18N.EPON.syncBD,disabled:!operationDevicePower,handler : syncBDHandler};
    items[items.length] = {id: 'mpuaTempDetectEnable',text: I18N.EPON.tempCheckEnable,disabled:!operationDevicePower, handler : tempCheckEnableHandler, checked:temprDetetStatus};
    displayService(items,e);
}
function showAlterMpuaService(e){
	 //no actions on alertMpua
	 var slot = selectionModel.getSelectedItem(),
	 	isBoardOnline = slot.topSysBdPreConfigType == slot.topSysBdActualType,
	 	temprDetetStatus = slot.topSysBdTempDetectEnable == STATUS.UP;
	 var items = []
	 items[items.length] = {id: 'mpuaBDRestore',hidden:!isBoardOnline,text: I18N.EPON.restoreBoard, disabled:!operationDevicePower, handler : restoreBoardHandler};
	 //8602没有主控板，不会进行倒换
	 //items[items.length] = {id: 'masterBDSwitch',text: I18N.EPON.Switch,disabled:!operationDevicePower, handler : switchHandler};
	 //items[items.length] = {id: 'mpuaSoftUpdate', text: I18N.EPON.softwareUpdate, disabled:!operationDevicePower, handler : softwareUpdateHandler};
	 //items[items.length] = {id: 'masterAlterBDSynchro',text: I18N.EPON.syncBD,disabled:!operationDevicePower,handler : syncBDHandler};
	 items[items.length] = {id: 'mpuaTempDetectEnable',text: I18N.EPON.tempCheckEnable,disabled:!operationDevicePower, handler : tempCheckEnableHandler, checked:temprDetetStatus};
	 displayService(items,e);
}
function showEpuaService( e, hasPreconfig ){
    var slot = selectionModel.getSelectedItem(),
        temprDetetStatus = slot.topSysBdTempDetectEnable == STATUS.UP,
        isBoardOnline = slot.topSysBdPreConfigType == slot.topSysBdActualType,
        adminStatus = slot.BAdminStatus == STATUS.UP,
        hasNoPreconfig = !hasPreconfig;
    	//GPON板卡实际类型和预配置培训不一致
    if(slot.topSysBdPreConfigType == 14 && slot.topSysBdActualType == 11){isBoardOnline = true}
    	
    var items = []
    items[items.length] = {id: 'deleteSlotPreConf',text: I18N.EPON.deleteBoard,disabled:!operationDevicePower, handler : deleteBoardHandler};
    items[items.length] = {id: 'epuaBDRestore',hidden:!isBoardOnline,text: I18N.EPON.restoreBoard, disabled:!operationDevicePower, handler : restoreBoardHandler};
    items[items.length] = {id: 'onuUpgrade',text: I18N.EPON.onuUpdate, handler : downlinkUpdateHandler};
    //板卡无法进行去使能操作
    items[items.length] = {id: 'bdAdminStatus',text: I18N.EPON.boardEnable, handler : boardEnableHandler,disabled:!operationDevicePower, checked: adminStatus};
    items[items.length] = {id: 'epuaTempDetectEnable',text: I18N.EPON.tempCheckEnable, disabled:!operationDevicePower, handler : tempCheckEnableHandler,checked:  temprDetetStatus};
    if(hasNoPreconfig){
        items[items.length] = {id:'epuaPreConfig',text: I18N.EPON.preConfig, handler : preConfigHandler};
    }
    displayService(items,e);
}
function showFanService(e){
    var items = []
    items[items.length] = {id: 'fanSpeedAdjust', text: I18N.EPON.fanSpeedAdjust, handler : fanSpeedAdjustHandler};
    displayService(items,e);
}

function showPowerService(e){
	var items = [];
    displayService(items,e);
}

/**
 * 初始化SNI业务
 * 包括 geFiber,geCopper,xgFiber 等
 * @param {EventObject} e
 */
function showSniService(e){
    var sniPort = selectionModel.getSelectedItem(),
        //使能
        adminStatus = sniPort.sniAdminStatus == STATUS.UP,
        //隔离使能
        isolatedStatus = sniPort.sniIsolationEnable == STATUS.UP,
        //性能统计使能
        perf15Status = sniPort.sniPerfStats15minuteEnable == STATUS.UP,
        perf24Status = sniPort.sniPerfStats24hourEnable == STATUS.UP,
        //SNI流量控制使能
        flowCtrlStatus = sniPort.topSniAttrFlowCtrlEnable == STATUS.UP,
        //SNI端口RSTP使能
        stpRstpStatus = sniPort.stpPortEnabled == STATUS.UP,
        //SNI口RSTP协议迁移使能
       // stpRstpProtcMigStatus = sniPort.stpPortRstpProtocolMigration == STATUS.UP,
        globalRstpStatus = olt.stpGlobalSetEnable == STATUS.UP;
        
    selectionModel.reset();
    var $pretype = selectionModel.SLOT_TYPE;
    var items = []
    items[items.length] = {id: 'sniAttributeConfig',text: I18N.EPON.basicConfig, handler : sniBasicConfigHandler};
    if( sniPort.sniMediaType == "fiber" || sniPort.sniMediaType == "other" ){
        items[items.length] = {id: 'sniOptical',text: I18N.EPON.sniOptical, handler : sniOpticalHandler};
    }
    items[items.length] = {id: 'sniAdminStatus',text: I18N.EPON.portEnable,disabled:!operationDevicePower, handler : portEnableHandler, checked: adminStatus };
    items[items.length] = {id: 'sniIsolationStatus',text: I18N.EPON.isolatedEnable,disabled:!operationDevicePower, handler : isolatedEnableHandler, checked: isolatedStatus};
    items[items.length] = {id: 'sniPerf',text: I18N.EPON.perfMgmt};
        subItems = [];
        subItems[subItems.length] = {id: 'sni15MinPerfStatus',text: I18N.EPON.stastic15Perf, disabled:!operationDevicePower, handler : stastic15PerfHandler, checked: perf15Status};
        subItems[subItems.length] = {id: 'sni24HourPerfStatus',text: I18N.EPON.stastic24Perf, disabled:!operationDevicePower, handler : stastic24PerfHandler, checked: perf24Status};
        subItems[subItems.length] = {id: 'sniPerfStatus',text: I18N.EPON.stasticPerfEnable, disabled:!operationDevicePower, handler : stasticPerfHandler, checked: perf15Status};
        items[items.length-1].menu = subItems;      
    items[items.length] = {id: 'sniConfig', text: I18N.EPON.serviceConfig};
        subItems = [];
        subItems[subItems.length] = {id: 'sniFlowControlEnabled', text: I18N.EPON.flowControlEnable,disabled:!operationDevicePower, handler : flowControlEnableHandler, checked: flowCtrlStatus};
        subItems[subItems.length] = {id: 'sniFlowControl',text: I18N.EPON.flowRestric, handler : flowRestricHandler};
        if(!["mpua","xgua","xgub"].contains( $pretype.toLowerCase()) && olt.oltType == 'PN8602'){
        	subItems[subItems.length] = {id: 'sniMaxMacNum',text: I18N.EPON.macLearnSet, handler : macLearnNumHandler};
        }
        //subItems[subItems.length] = {id: 'sniRedirect',text: I18N.EPON.redirect, handler : redirectHandler};
        subItems[subItems.length] = {id: 'sniBroadCastStromMgmt',text: I18N.EPON.broadcastDec, handler : broadcastDecHandler};
        subItems[subItems.length] = {id: 'sniMirrorMgmt',text: I18N.EPON.mirrorgroup, handler : mirrorgroupHandler};
        subItems[subItems.length] = { id: 'sniTrunkGroupMgmt',text: I18N.EPON.trunkgroup, handler : trunkgroupHandler};
        items[items.length-1].menu = subItems;  
    items[items.length] = {id: 'sniRstp',text: I18N.EPON.rstpCfg};
        subItems = [];
        //subItems[subItems.length] = {id: 'sniRstpEnable',text: I18N.EPON.rstpEnable, handler : sniRstpEnableHandler, disabled: !globalRstpStatus, checked:stpRstpStatus};
        subItems[subItems.length] = {id: 'sniRstpConfig',text: I18N.EPON.rstpSetting, handler : sniRstpSettingHandler};
        subItems[subItems.length] = {id: 'sniRstpProtocolEnable',text: I18N.EPON.protocoMig, handler : sniRstpProtocoMigHandler, disabled: !globalRstpStatus||!operationDevicePower};
        items[items.length-1].menu = subItems;      
    items[items.length] = {id: 'sniAclConfig',text: I18N.EPON.aclCfg};
        subItems = [];
        subItems[subItems.length] = {id: 'sniIngress',text: I18N.EPON.indirect, handler : indirectHandler};
        subItems[subItems.length] = {id: 'sniOutGress',text: I18N.EPON.outdirect, handler : outdirectHandler};
        items[items.length-1].menu = subItems;  
    items[items.length] = {id: 'sniQos',text: I18N.EPON.qosCfg};
        subItems = [];
        subItems[subItems.length] = {id: 'sniQosMap',text: I18N.EPON.queneMap, handler : sniQueneMapHandler};
        subItems[subItems.length] = {id: 'sniQosPolicy',text: I18N.EPON.quenePolicy, handler : sniQuenePolicyHandler};
        items[items.length-1].menu = subItems;
    items[items.length] = {id: 'sniVlanMgmt',text: I18N.EPON.vlanCfg, handler : vlanCfgHandler};
    displayService(items,e);
}

/**
 *  初始化PON业务 
 *  包括geEpon,tengeEpon,gpon
 * @param e
 * @returns
 */
function showPonService(e){
    var ponPort = selectionModel.getSelectedItem();
  //如果是PON保护组备用端口，则不显示菜单.TODO 必须去掉业务树中的内容
    if(ponPort.isStandbyPort)return
    var globalRstpStatus = olt.stpGlobalSetEnable == STATUS.UP,
        hidePonSpeedMode = false,
        hidePonEncrypt = false,
        hidePonLlidVlan = false,
        //端口使能
        adminStatus =  ponPort.ponPortAdminStatus == STATUS.UP,
        // 端口隔离
        isolatedStatus = ponPort.ponPortIsolationEnable== STATUS.UP,
        //端口长发光排查
        rogueSwitchStatus = ponPort.ponRogueSwitch == STATUS.UP,
        //性能统计使能
        perf15Status = ponPort.perfStats15minuteEnable == STATUS.UP,
        perf24Status = ponPort.perfStats24hourEnable == STATUS.UP;
    
    if(ponPort.portRealIndex % 2==0){
    	hidePonSpeedMode = true;
        //如果是PON保护组备用端口，则不显示菜单
        if(ponPort.ponSpeedMode == 3){
            return;
        }
    }
    // bravin@20140110 : 10G EPON不能配置端口工作模式
    // bravin@20141017: XPUA,即10G EPON不能配置加密模式
    var gponType = ponPort.portSubType =='gpon';
    if(ponPort.portSubType == "tengeEpon"){
    	hidePonSpeedMode = true;
    	hidePonEncrypt = true;
    	hidePonLlidVlan = true;
	}
    var items = []
    if(ponPort.ponPortType != "GPON"){
    	items[items.length] = {id: 'ponBaseConfig',text: I18N.EPON.basicConfig, handler : ponBasicConfigHandler,hidden:hidePonEncrypt};
    }
    items[items.length] = {id: 'ponOpticalAlarm',text: I18N.EPON.optThrMgmt, handler : ponOpticalAlarmHandler};
    items[items.length] = {id: 'ponOptical',text: I18N.EPON.sniOptical, handler : ponOpticalHandler};
    items[items.length] = {id: 'ponPortRateLmt',text: I18N.EPON.portRateConfig, handler : ponRateHandler};
    //items[items.length] = {id: 'ponSpeedMode',text: I18N.EPON.ponSpeedMode, handler : ponSpeedModeHandler,hidden: hidePonSpeedMode};
    items[items.length] = {id: 'ponAdminStatus',text: I18N.EPON.portEnable, disabled:!operationDevicePower, handler : ponPortEnableHandler, checked: adminStatus};
    //items[items.length] = {id: 'ponReset',text: 端口复位, disabled:!operationDevicePower, handler : ponResetHandler};
    items[items.length] = {id: 'ponIsolationStatus',text: I18N.EPON.isolatedEnable, disabled:!operationDevicePower, handler : ponPortIsolatedEnableHandler, checked: isolatedStatus };
    items[items.length] = {id: 'ponRogueSwitch',text: '@EPON.ponRogueSwitch@', disabled:!operationDevicePower, handler : ponRogueSwitchHandler, checked: rogueSwitchStatus };
    items[items.length] = {id: 'ponPerf', text: I18N.EPON.perfMgmt};
        subItems = [];
        subItems[subItems.length] = {id: 'pon15MinPerfStatus',text: I18N.EPON.stastic15Perf, disabled:!operationDevicePower, handler : ponStastic15PerfHandler, checked: perf15Status};
        subItems[subItems.length] = {id: 'pon24HourPerfStatus',text: I18N.EPON.stastic24Perf, disabled:!operationDevicePower, handler : ponStastic24PerfHandler, checked: perf24Status};
        subItems[subItems.length] = {id: 'ponPerfStatus',text: I18N.EPON.stasticPerfEnable, disabled:!operationDevicePower, handler : ponStasticPerfHandler, checked: perf15Status};
        items[items.length-1].menu = subItems;      
    items[items.length] = {id: 'ponConfig',text: I18N.EPON.serviceConfig};
        subItems = [];
        //subItems[subItems.length] = {id: 'onuView',text: I18N.EPON.downView, handler : downViewHandler};
        if(gponType){
        	subItems[subItems.length] = {id: 'ponOnuAuthen',text: I18N.EPON.onuAuth, handler : gponOnuAuthHandler};
        }else{
        	subItems[subItems.length] = {id: 'ponOnuAuthen',text: I18N.EPON.onuAuth, handler : onuAuthHandler};
        }
        /*if(olt.oltType != 'PN8603'){
        	subItems[subItems.length] = {id: 'ponMacAddrMaxLearningNum',text: I18N.EPON.macLearnNum, handler : ponMacLearnNumHandler};
        	subItems[subItems.length] = {id: 'ponMacLearningNumSet',text: I18N.EPON.macLearnNum, handler : ponMacLearnNumHandler2};
        }*/
        subItems[subItems.length] = {id: 'ponBroadStorm',text: I18N.EPON.broadcastDec, handler : ponBroadcastDecHandler};
        items[items.length-1].menu = subItems;      
    items[items.length] = {id: 'ponAclConfig',text: I18N.EPON.aclCfg};
        subItems = [];
        subItems[subItems.length] = {id: 'ponIngress',text: I18N.EPON.indirect, handler : indirectHandler};
        subItems[subItems.length] = {id: 'ponOutGress',text: I18N.EPON.outdirect, handler : outdirectHandler};
        items[items.length-1].menu = subItems;  
    items[items.length] = {id: 'ponQosConfig',text: I18N.EPON.qosCfg};
        subItems = [];
        subItems[subItems.length] = {id: 'ponQosMap',text: I18N.EPON.queneMap, handler : ponQueneMapHandler};
        subItems[subItems.length] = {id: 'ponQosPolicy',text: I18N.EPON.quenePolicy, handler : ponQuenePolicyHandler};
        items[items.length-1].menu = subItems;  
    items[items.length] = {id: 'ponVlanConfig',text: I18N.EPON.vlanCfg};    
        subItems = [];
        subItems[subItems.length] = {id: 'basePonPort',text: I18N.EPON.ponBased, handler : ponBasedVlanHandler};
        subItems[subItems.length] = {id: 'baseLlid',text: I18N.EPON.llidBased, handler : llidBasedVlanHandler,hidden: hidePonLlidVlan};
        items[items.length-1].menu = subItems;
    //if(!gponType){
    	items[items.length] = {id:'ponPvidConfig', text : "@PON.pvidConfig@", handler : showPonPvidConfig}
    //}
    displayService(items,e);
}

function slotHandler(e) {
    $(".boardClass").attr('checked','invalid');//标记所有端口invalid
    $(this).attr('checked','valid');//标记该端口已经点击过    
    NOTATION_FLAG = true;
    switchNotation('BoardNotation');
    NOTATION_FLAG = false;
    currentId = this.id;
    preventBubble(e);
    if (!CASCADE_LOCK) {    //如果实体没有被点击过。
        CASCADE_LOCK = true;
        try{
            clearPage(this.id);
            divCache.add(this.id);//并记录本次div 的id,放入divCache中
            setBorderStyle(this.id);
            this.style.border = "1px solid "+divStyle.click;
            clickHandler(this.id,e);
        }catch(exp){ }
        CASCADE_LOCK = false;
    }
}
//注意，8602-E函数在oltFaceplate.jsp页面有一个同名函数覆盖。覆盖原因：EMS-13947;
function portHandler(e){
    var arr = this.id.split("_");
    NOTATION_FLAG = true;
    if( UPLINK_BOARD.concat( CONTROL_BOARD ).contains(arr[0]) ){
        switchNotation('SniNotation');
    }
    if( DOWNLINK_BOARD.contains(arr[0]) ){
        switchNotation('PonNotation');
    }
    NOTATION_FLAG = false;
    currentId = this.id;
    preventBubble(e);
    if (!CASCADE_LOCK) {
        CASCADE_LOCK = true;
        try{
            if (ctrlFlag == 0) {// 如果没有按ctrl键，则清除之前的样式
                clearPage();
            }
            if(divCache.contains(this.id)){
                divCache.remove(this.id);
                this.style.border  = '1px solid transparent';
            } else {
                divCache.add(this.id);// 并记录本次div 的id,放入divCache中
                this.style.border = "1px solid "+divStyle.click;
                setBorderStyle(this.id.substring(0,7)+"0"); // 修改父板的透明度
                divCache.add(this.id.substring(0,7)+"0");// 父板被修改了，故也要记录
                clickHandler(this.id,e);
            }
        }catch(exp){}
        CASCADE_LOCK = false;
    }
}

function deviceHandler(e) {
	NOTATION_FLAG = true;
    switchNotation('DeviceNotation')
    NOTATION_FLAG = false;
    currentId = this.id; 
    preventBubble(e);
    if (!CASCADE_LOCK) {    // 如果实体没有被点击过。
        CASCADE_LOCK = true;
        try{
            clearPage();
            divCache.add(this.id);          // 并记录本次div 的id,放入divCache中
            this.style.border = "2px solid  "+divStyle.click;
            clickHandler(this.id,e);
        }catch(exp){}
        CASCADE_LOCK = false;
    }
}

function powerHandler(e){
    NOTATION_FLAG = true;
    switchNotation('PowerNotation')
    NOTATION_FLAG = false;
    currentId = this.id
    preventBubble(e)
    if (!CASCADE_LOCK) {    //如果实体没有被点击过。
        CASCADE_LOCK = true
        try{
             clearPage();
             divCache.add(this.id)// 记录本次div 的id,放入divCache中
             this.style.border = "2px solid  "+divStyle.click
             clickHandler(this.id, e);
        }catch(exp){}
        CASCADE_LOCK = false
    }
}

function fanHandler(e){
	NOTATION_FLAG = true;
    switchNotation('FanNotation')
    NOTATION_FLAG = false;
    currentId = this.id
    preventBubble(e)
    if (!CASCADE_LOCK) {    //如果实体没有被点击过。
        CASCADE_LOCK = true
        try{
             clearPage();
             divCache.add(this.id)      //并记录本次div 的id,放入divCache中
             $("#"+this.id).css("border","2px solid "+divStyle.click)
             clickHandler("fan_1_0",e)
        }catch(exp){ }
        CASCADE_LOCK = false
    }
}

/********************************************
             处理节点点击事件
 *******************************************/
var clickHandler = function (node,e) {
    if(typeof node == 'object')
        currentId = node.attributes.id;
    else
        currentId = node;
    if (!treeFlag) {
        treeFlag = true;
        try{
            locateEntity(currentId);
            showItemProperty(currentId);
            contextmenuHnd(node, e);
        }catch(exp){}
        treeFlag = false;
    }
    NOTATION_FLAG = false;
}

/********************************************
            节点右键菜单事件
********************************************/
function contextmenuHnd(node, e) {
    if(node instanceof Ext.data.Node)
        currentId = node.attributes.id;
    else
        currentId = node;
    //****得到树节点对应的port&board&device*****//
    var tempArray = currentId.split("_");
    //----手动触发一个该节点被点击的事件-----//
    navigatorTree.getNodeById(currentId).select();
    //----select操作并不能触发click事件，还需手动调用click事件处理器--//
    //clickHandler(node);
    //----设备，风扇，电源单独处理-----//
    switch(tempArray[0]){
        case 'OLT': return showOltService(e);
        case 'fan': return showFanService(e);
        case 'power':return showPowerService(e);//---电源没有菜单，保险起见，此处写死返回，不然会出电源上出现删除板卡的菜单---//
    }
    //****第一个字段是实际板卡类型，第二个字段是板卡位置，第三个字段是端口位置（0标记为板卡）***//
    var tempSlot = olt.slotList[tempArray[1]-1];
    //var preType = bType[tempSlot.topSysBdPreConfigType];
    var preType = tempArray[0];
    //var type = tempArray[0];//第1个字段表示实际的板卡类型
    var type =  bType[tempSlot.topSysBdActualType];
    var $portIndex = parseInt(tempArray[2]);
    if(CONTROL_BOARD.contains(tempArray[0])) {
        //-----如果是在主控板上点击右键-----//
        if("0" == tempArray[2]) {
            var bAttribute = tempSlot.BAttribute;
            //****mpua专用字段: active(1), standby(2), standalone(3), notApplicable(4)***//
            if (bAttribute == 1) { //主用板 
                showMpuaService(e);
                return;
            } else if(bAttribute == 2){ //备用板
                showAlterMpuaService(e);
                return;
            }
        } else if(preType == 'meua' || preType == 'meub' || preType == 'mefa' || preType == 'mefb'|| preType == 'mefc' || preType == 'mgua' || preType == 'mgub'){
        	var sniPort = null;
        	for(var i=0; i<tempSlot.portList.length; i++) {
        		if(tempSlot.portList[i].portRealIndex == $portIndex) {
        			sniPort = tempSlot.portList[i];
        			break;
        		}
        	}	
        	
        	var $portRealIndex = sniPort.portRealIndex;
        	if($portRealIndex > 16){
        		return showSniService(e);
        	}else{
        		return showPonService(e);
        	}
   	 	}else if("console" == tempArray[2].toLowerCase()) {
            //CONSOLE PORT
        } else if("mgmt" == tempArray[2].toLowerCase()) {
            //MGMT PORT
        } else{//电口 
            showSniService(e);
            return;
        }
    }
    //non-board(0), mpua(1), mpub(2), epua(3), epub(4), geua(5), geub(6), xgua(7), xgub(8), unknown(255)
    switch(preType == SLOT_BOARD){
     //--------预配置类型为0，则根据实际类型判断------------//
    case true:
    	//只要预配置为空板,则显示空板菜单.MPUA不可以配置预配置类型
    	showSlotWithoutBoardService(e);
        break;
    //--------预配置类型不为0，则根据预配置类型判断------------//
    case false:
         /*if(type != preType && preType == SLOT_BOARD && tempArray[2] == 0){
             showSlotService(e);
         }else */
    	 if (UPLINK_BOARD.contains(preType)) {//geua,xgua
            switch(parseInt(tempArray[2])){
                case 0://board
                	return showGeuaService(e, true );
                case 1://geCopper
                case 2://geCopper
                case 3://geCopper
                case 4://geCopper
                		//return showSniService(e);
                case 5://geFiber
                case 6://geFiber
                case 7://geFiber
                case 8://geFiber
                	return showSniService(e);
            }
         } else if (DOWNLINK_BOARD.contains(preType)) {//epua
                switch(tempArray[2] == 0){
                    case true://board
                    	return showEpuaService(e, true );
                    case false://geEpon
                        //----PON口使能菜单初始化------//
                    	return showPonService(e);
                    default:
                        break;
                }
            //-------------MPUA的预配置与实际只可以为mpua
            }
            break;
    }
}


/**
 * 修改端口状态
 * 
 * @param divId
 * @param state
 */
function changePortState(divId, state){
	var arr = divId.split('_');
	//解析state
	var portTemp = olt.slotList[arr[1]-1].portList[arr[2]-1];
	switch(portTemp.portSubType){
		//SNI MASSTYPE
		case 'geCopper'://NO NEED TO HANDLE
		case 'geFiber'://NO NEED TO HANDLE
		case 'xeFiber':
			//var os = portTemp.sniOperationStatus;
			var os = state;
			var as = portTemp.sniAdminStatus;
			if( as==1 && os==1 ){
				state = 0;
			}else if( as==1 && os==2 ){
				state = 1;
			}else if(as==2 && os==2) {
				state = 2;
			}else {
				state = 2;
			}
			break;
		//PON MASSTYPE
		case 'geEpon'://NO NEED TO HANDLE
		case 'tengeEpon'://NO NEED TO HANDLE
		case 'gpon':
			//var os = portTemp.ponOperationStatus;
			var os = state;
			var as = portTemp.ponPortAdminStatus;
			if( as==1 && os==1 ){
				state = 0;
			}else if( as==1 && os==2 ){
				state = 1;
			}else if(as==2 && os==2) {
				state = 2;
			}else {
				state = 2;
			}
			break;	
		default:			
			break;	
	}
	//移除状态2的样式
    $port = $("#"+divId);
   	$port.find(".portStateClass").remove();
    var portType = $port.attr("portSubType");
    //移除状态1的样式
    switch(portType){
    	case 'geCopper':
        		$port.css({
        			backgroundImage:'url(/epon/image/geCopper/geCopper.png)'
        		});
        		break;
		case 'geFiber':
				$port.css({
    				backgroundImage:'url(/epon/image/geFiber/geFiber.png)'
    			});
    			break;
		case 'xeFiber':
				$port.css({
    				backgroundImage:'url(/epon/image/xeFiber/xeFiber.png)'
    			});
   				break;
    	case 'geEpon':
		case 'tengeEpon':
		case 'gpon':
	    		$port.css({
	    			backgroundImage:'url(/epon/image/geEpon/geEpon.png)'
				});
	    		break;
	}
	switch(state){
		case 0 :
			$port.css({
				backgroundImage : 'url(/epon/image/' + portType +'/'+portType+ '.png)'
			});		
			break;
		case 1 :
			$port.css({
				backgroundImage : 'url(/epon/image/' + portType +'/'+portType+ '.gif)'
			});				
			break;
		case 2 :
			$state = $("<img>");
			$state.attr({
				width : 12,
				height : 12,
				title : I18N.EPON.portnoservice
			});
			$state.addClass("portStateClass");
			$state.css({
				position : 'absolute',
				top : 3,
				left : 3,
				opacity : 1,
				zIndex : 15000
			});
			$state.attr({
				src : '/epon/image/close.gif'
			 });
			$port.append($state);	// 在板卡上添加端口状态
			break;	
		default:
			break;
		}	
}

/************************************************************************
       为console，mgmt，无效端口添加点击事件，让事件转移到板卡对象上
*************************************************************************/
function addEventListeners () {
    //CONSOLE,MANAGEMENT PORT
    $(".consoleClass , .portClass1").click(function(e){
        preventBubble(e);
        id = this.id;
        var arr = id.split("_");
        id = arr[0]+"_"+arr[1]+"_0";
        $("#"+id).trigger("click");
    }).bind("contextmenu",function(e){
        preventBubble(e);
        id = this.id;
        var arr = id.split("_");
        id = arr[0]+"_"+arr[1]+"_0";
        $("#"+id).trigger("contextmenu",[e,e]);
    });
    //POWER
    $(".powerClass").bind('mouseover',function(e){
        switchNotation('PowerNotation')
        preventBubble(e)
        setBorderStyle(this.id);
    }).bind('mouseout',function(e){
        preventBubble(e)
        clearBorderStyle(this.id);
    }).click(powerHandler).contextmenu(powerHandler);
    //FAN
    $(".fanClass").bind('mouseover',function(e){
        switchNotation('FanNotation')
        preventBubble(e)
        setBorderStyle(this.id)        
    }).bind('mouseout',function(e){
        preventBubble(e)
        clearBorderStyle(this.id)                      
    }).bind("click", fanHandler).bind('contextmenu', fanHandler);

    $(".portClass").bind("click", portHandler).bind('contextmenu', portHandler);
    $(".deviceClass").bind('contextmenu', deviceHandler).bind('click', deviceHandler);
    $(".boardClass").bind("click",slotHandler).bind('contextmenu', slotHandler);
}



/****************************************
			创建GRID.TREE
****************************************/
function beginPrepareJob(){
	// create new Olt Object
	olt = new Olt();
	// modify propertyGird prototype
    if(Ext.grid.PropertyColumnModel){
    	//修改PropertyGrid原型
      	Ext.grid.PropertyGrid.prototype.initComponent = function(){
			this.customEditors = this.customEditors || {};
			this.lastEditRow = null;
			var store = new Ext.grid.PropertyStore(this);
			this.propStore = store;
			//var cm = new Ext.grid.PropertyColumnModel(this,store);
			var cm = new Ext.grid.ColumnModel({columns: [
				 {header:'property' ,align: 'center',sortable: false,resizable: true,menuDisabled :true},
	             {header:'value', align: 'center',sortable: false,resizable: true,menuDisabled :true,renderer : rendererColumnValue}
	        ]});
			//store.store.sort('name','ASC');//取消默认排序
			this.addEvents('beforepropertychange','propertychange');
			this.cm = cm;
			this.ds = store.store;
			Ext.grid.PropertyGrid.superclass.initComponent.call(this);
			this.selModel.on('beforecellselect',function(sm,rowIndex,colIndex){
				if(colIndex === 0)return false;
			});
        }
 	}
	grid = new Ext.grid.PropertyGrid({
	    renderTo: 'viewRightPartBottomBody' , border:false, autoHeight:true, autoWidth:true,
        headerAsText:true,hideHeaders:true,propertysort:false,autoScroll: true,
        listeners: {
            render : function(proGrid) {
                var view = proGrid.getView();
                var store = proGrid.getStore();
                proGrid.tip = new Ext.ToolTip({
                    target: view.mainBody,delegate: '.x-grid3-row',
                    renderTo: DOC.body,
                    listeners: {
                        beforeshow: function updateTipBody(tip) {
                            var rowIndex = view.findRowIndex(tip.triggerElement);
                            var record = store.getAt(rowIndex)
                            var result = store.getAt(rowIndex).get('name') + " : ";
                            if(record.id == I18N.EPON.slotStatus){
                        		if(record.data.value==1)
                        			result += I18N.EPON.inject
                        		else
                        			result += I18N.EPON.unInject
                        	}else if(record.id == I18N.EPON.controlStatus || record.id == I18N.EPON.fanStatus){
			                	if(record.data.value==1)
			            			result += I18N.EPON.operationAble
			            		else
			            			result += I18N.EPON.unOperationAble
                			}else if(record.id == I18N.EPON.typeUnique){
                        		var v = record.data.value.split(":")
                        		if(v[0] == v[1])
                        			result += I18N.EPON.unique
                        		else
                        			result += I18N.EPON.unUnique
                        	}else{
                        			result += record.get("value");
                        	}
                            tip.body.dom.innerHTML = result;
                        }}});
            } ,
            rowdblclick:function(g,r,e ){
            	//-------双击时默认复制行内容 
                var thisText = e.getTarget().innerHTML;
                //-----属性值可能是number型，故强转string
                clipboardData.setData("text",thisText);
                window.parent.showMessageDlg( I18N.COMMON.tip, I18N.EPON.copyOk + thisText );
            },
            beforeedit:function(e){
				 e.cancel = true;
		         return false;
			} 
        }
    });
	if(navigatorTree)navigatorTree = null;
    navigatorTree = new Ext.tree.TreePanel({
        autoScroll: true,border:false,
        listeners:{
            "click" : clickHandler,"contextmenu" : clickHandler
        }
	});
}


/**
 * 设置边框效果
 */
function setBorderStyle(id){
	var $temStyle = $( "#"+id ),
		dom = $temStyle[0];
	try{
		if( dom.style.borderColor == divStyle.click){
			//NO NEED TO HANDLE
		}else{
			if((id.split('_'))[2]>0){///端口
				$temStyle.css({
					opacity : 1,
	  		  		border : '1px solid '+ divStyle.hover 
				});
			}else{
				$.each($(".boardClass"),function(i,div){
					if('invalid' == $(div).attr("checked")){//如果没有被点击过，则修改为没有点击
						$(div).css({
							border : '1px solid transparent' 
						})
					}
				});
				$temStyle.css({
	  		  		border : '1x solid '+ divStyle.hover 
				})
			}
		}
	}catch(err){
		
	}
}


/******************************
 		 清除边框效果
 *******************************/
function clearBorderStyle(id){
	var $temStyle = $( "#"+id ),
		dom = $temStyle[0];
	if( dom.style.borderColor != divStyle.click){
  	  	//如果是端口		  	  	 
  	  	var arr = id.split('_');
		if(arr[2]>0){
			$temStyle.css({
  		  		border : '1px solid transparent' 
			});
		}else if(arr[2]==0 && arr[1]<19 && arr[0]!='power' && arr[0]!='fan'){//是板卡
			$temStyle.css({
  		  		border : '1px solid transparent'
			});
		}else{//是风扇或者电源
			$temStyle.css({
  		  		border : '1px solid transparent'
			});
		}		 		
	 }	
}

/******************************************
 检查鼠标是否离开板卡区域
*****************************************/
function checkAnimateStyle(){
	$position = $("#"+this.id);		
	if($position.css("height") == ""+coordinateParam.slotHeight+"px"){
		var x = $position.offset().left;
		var y = $position.offset().top;
		if(cursor.X>x && cursor.X<x+coordinateParam.slotWidth && cursor.Y>y && cursor.Y<y+coordinateParam.slotHeight){
			$position.css({
				opacity : 1
			});
		}
	}
}


/************************************************************************
	修改设备状态，4个视图共用，都可以修改 param: index:唯一标识，状态号
***********************************************************************/
function displayEmergency(index,state){
	window.location.reload();
}

/********************************************
 * 刷新设备右键菜单
 * 本操作使用频率很低，故将请求集成在此方法中
 *******************************************/
function refreshDeviceMenu(igmpMode){
	deviceIgmpMode = igmpMode;
}

function bfsxOlt(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@", 'ext-mb-waiting')
	$.ajax({
		url:'/epon/bfsxOlt.tv',cache:false,data:{
			entityId : entityId
		},success:function(){
			window.parent.closeWaitingDlg();
			window.location.reload();
		},error:function(){
			window.parent.closeWaitingDlg();
		}
	})
}

//风扇转速统一转换为rpm
function topSysFanSpeedToRpm(topSysFanSpeed){
	return isNaN(topSysFanSpeed) ? null : (topSysFanSpeed * 60 + " rpm");
}