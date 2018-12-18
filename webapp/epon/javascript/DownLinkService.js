function showCC8800AService(e){ 
	var items = []
		items[items.length] = {id:'refreshCC8800A', text: "@COMMON.refresh@",  handler: refreshCC8800A };
	    items[items.length] = {id:'ccdetail', text: "@SUPPLY.detail@", handler: detailHandler };
	    items[items.length] = {id:'ccPerf', text: "@SUPPLY.perfMgmt@", handler: perfMgmtHandler };
	    items[items.length] = {id:'addCCToTopo', text: "@network/COMMON.editFolder@",  handler: addToTopoHandler ,disabled:!topoGraphPower};
	    items[items.length] = {id:'renameCmc', text: "@RESOURCES/COMMON.deviceInfo@",  handler: renameEntity };
	    items[items.length] = {id:'noMacBind', text: "@SUPPLY.unbind@",  handler: unbindHandler ,disabled:!operationDevicePower};
	    items[items.length] = {id:'reset', text: "@SUPPLY.reset@",  handler: resetHandler ,disabled:!operationDevicePower};
    displayService(items,e);
}
function showOnuService(e){
	//----------------ONU反键------------------//
    var onuAdminStatus = entity.onuAdminStatus == STATUS.UP;
    var onutemeratureStatus = entity.temperatureDetectEnable == STATUS.UP;
    var onuFECStatus = entity.onuFecEnable == STATUS.UP;
    var onuIsolationStatus = entity.onuIsolationEnable == STATUS.UP;
    var onuLaser = entity.laserSwitch == STATUS.UP;
    var isElectricity = parseInt(entity.entityType) > 80 && parseInt(entity.entityType) < 85;
    //三层搅动
    var onuStirringEnable = entity.stirringEnable == STATUS.UP;
    var items = []
    	if(entity.onuEorG == "E"){
    		items[items.length] = {id:'onudetail', text: "@SUPPLY.detail@", handler: onuDetailHandler };
        	items[items.length] = {id:'onuCpeList', text: "@ONU.cpeList@", handler: onuCpeHandler };
    	    items[items.length] = {id:'uniConfig', text: "@ONU.uniConfig@",  handler:onuConfigFn};
    	    items[items.length] = {id:'refrheshOnu', text: "@COMMON.refresh@",  handler: refreshOnu,disabled:!refreshDevicePower};
    	    items[items.length] = {id:'renameCmc', text: "@ONU.deviceInfo@",  handler: renameEntity };
    	    items[items.length] = {id:'onuAbility', text: "@ONU.ableInformation@",  handler: ableInformationHandler };
    	    items[items.length] = {id:'onuLaserSwitch', text: "@ONU.laserSwitch@", disabled:!operationDevicePower,checked: onuLaser,handler: onuLaserSwitchHandler };
    	    //items[items.length] = {id:'oltOnuOpticalAlarm', text: "@EPON.optThrMgmt@",  handler: onuOpticalAlarmHandler };
    	    items[items.length] = {id:'onuOpticalAlarm', text: "@EPON.optThrMgmt@",  handler: opticalAlarmHandler};
    	    items[items.length] = {id:'onuOpticalAlarmBack', text: "@EPON.optThrMgmt@",  handler: opticalAlarmBackHandler};
    	   /* if(entity.entityType != ONU_10G_TYPE){
    	    	 items[items.length] = {id:'onuAdminStatus', text: "@ONU.onuEnable@",  handler: onuEnableHandler ,disabled:!operationDevicePower, checked:onuAdminStatus};
    	    }*/
    	    items[items.length] = {id:"onutemperatureStatus", text: "@ONU.tempretureEnable@",  handler: tempretureEnableHandler ,disabled:!operationDevicePower,checked: onutemeratureStatus};
    	    items[items.length] = {id:"onuIgmpConfig", text: "@COMMON.igmpConfig@",handler: showOnuIgmpConfig ,disabled:!operationDevicePower};
    	    items[items.length] = {id:'onuConfig', text: "@ONU.serviceCfg@" }
    	}
    
    if(entity.onuEorG == "G"){
    		items[items.length] = {id:'onudetail', text: "@SUPPLY.detail@", handler: onuDetailHandler };
    		items[items.length] = {id:'refrheshOnu', text: "@COMMON.refresh@",  handler: refreshOnu,disabled:!refreshDevicePower};
    		items[items.length] = {id:'renameCmc', text: "@ONU.deviceInfo@",  handler: renameEntity };
    		items[items.length] = {id:'onuAbility', text: "@ONU.ableInformation@",  handler: showGponOnuCapability};
    		items[items.length] = {id:'onuLaserSwitch', text: "@ONU.laserSwitch@",  handler: onuLaserSwitchHandler };
    		items[items.length] = {id:"onuCpeList",text: "@ONU.cpeList@", handler:gponOnuCpeFn};
    		items[items.length] = {text: "@gpon/GPON.hostList@", handler :gponOnuHost,disabled:!operationDevicePower};
    		items[items.length] = {id:'moveToTopo', text: "@network/COMMON.editFolder@",  handler :addOnuToTopoHandler ,disabled:!topoGraphPower };
    	}
	    
    var subMenu = [];
    	if(entity.entityType != ONU_10G_TYPE){
    		subMenu[subMenu.length] = {id:'onuFECStatus', text: "@ONU.FECEnable@",disabled:!operationDevicePower, handler: FECEnableHandler,checked:onuFECStatus };
    	}
    	if(entity.onuEorG != "G"){
    		 if(!ONU_MTK_TYPE.contains(entity.entityType)){
    			 subMenu[subMenu.length] = {id:'onuIsolationEnable', text: "@ONU.isolatedEnable@",  handler :isolatedEnableHandler ,disabled: !operationDevicePower,checked:onuIsolationStatus};
    	         subMenu[subMenu.length] = {id:'onuRstpConfig', text: "@ONU.rstpCfg@",  handler :rstpCfgHandler }; 
    		 }
            subMenu[subMenu.length] = {id:'onuSlaConfig', text: "@ONU.slaMgmt@",  handler :slaMgmtHandler };
            subMenu[subMenu.length] = {id:'onuSlaConfigBack', text: "@ONU.slaMgmt@",  handler :slaMgmtHandlerBack };
           /* subMenu[subMenu.length] = {id:'onuMacMgmt', text: "@ONU.macMgmt@",  handler :macMgmtHandler, hidden:!isElectricity };
            subMenu[subMenu.length] = {id:'onuInbandMgmt', text: "@ONU.inbandMgmt@",  handler :inbandMgmtHandler, hidden:!isElectricity };*/
            subMenu[subMenu.length] = {id:'onuIgmp', text: "@ONU.igmpCfg@",  handler :igmpCfgHandler };
            if(entity.entityType != ONU_10G_TYPE){
            	subMenu[subMenu.length] = {id:'onuMacAgeTime', text: "@ONU.macAge@",disabled:!operationDevicePower, handler :onuMacAgeHandler };
            }
            //subMenu[subMenu.length] = {id:'onuStirring', text: 三层搅动使能,  handler :stirringCfgHandler, disabled: !operationDevicePower,checked:onuStirringEnable};
            items[items.length-1].menu = subMenu;
    	    items[items.length] = {id:'moveToTopo', text: "@network/COMMON.editFolder@",  handler :addOnuToTopoHandler ,disabled:!topoGraphPower };
    	    if(!ONU_MTK_TYPE.contains(entity.entityType)){
    	    	items[items.length] = {id:'bindCamera', text: "@CAMERA/CAMERA.bindCamera@", hidden:cameraSwitch != 'on',handler : bindCameraFn};
    	    }
    	    items[items.length] = {id:'onuCapDeregister', text: "@ONU.unregister@",  handler :unregisterHandler ,disabled:!operationDevicePower };
    	}
    	items[items.length] = {id:'onuReset', text: "@COMMON.restore@",  handler :restoreHandler ,disabled:!operationDevicePower };
    	items[items.length] = {id:'onuDelete', text: "@COMMON.delete@",  handler :deleteHandler ,disabled:!operationDevicePower };
    	// 新增上下线记录查看列
    	items[items.length] = {id:'onOffRecord', text: "@ONU.viewOnOffRecord@", handler: viewOnOffRecordHandler };
        
   /* items[items.length] = { id:'qosConfig', text: "@ONU.qosCfg@" }; 
        subMenu = [];
        subMenu[subMenu.length] = {id:'onuQosMap', text: "@ONU.queneMap@",  handler :queneMapHandler };
        subMenu[subMenu.length] = {id:'onuQosPolicy', text: "@ONU.quenePolicy@",  handler :quenePolicyHandler };
        items[items.length-1].menu = subMenu;*/
        
    /*items[items.length] = { id:'onuAclMgmt', text: "@EPON.aclCfg@" }; 
        subMenu = [];
        subMenu[subMenu.length] = {id:'onuAclConfig', text: "@EPON.aclCfgMgmt@",  handler: aclCfgMgmt };
        subMenu[subMenu.length] = {id: 'onuIngress',text: "@EPON.indirect@", handler : indirectHandler};
        subMenu[subMenu.length] = {id: 'onuOutGress',text: "@EPON.outdirect@", handler : outdirectHandler};
        items[items.length-1].menu = subMenu;*/
    //items[items.length] = {id:'llidBasedMacLearn', text: 基于LLID MAC学习数,  handler :llidBasedMacLearn };
    displayService(items,e);
}
function showOnuPonService(e){
    var pon15minStatus = entity.ponPerfStats15minuteEnable == STATUS.UP,
        pon24hourStatus = entity.ponPerfStats24hourEnable == STATUS.UP;
    var items = [];
	    items[items.length] = {id:'onuPonOptical', text: "@ONU.ponInfo@",  handler :onuPonOpticalHandler };
	    items[items.length] = {id:'onu15minEnable', text: "@ONU.stasitc15Enable@", disabled:!operationDevicePower, handler: stasitc15EnableHandler, checked:pon15minStatus };
	    items[items.length] = {id:'onu24hourEnable', text: "@ONU.stastic24Enable@", disabled:!operationDevicePower, handler: stastic24EnableHandler, checked:pon24hourStatus };
	    items[items.length] = {id:'onuPerfEnable', text: "@ONU.stasitcEnable@", disabled:!operationDevicePower, handler: stasitcEnableHandler, checked:pon15minStatus };
    //MARK BY @BRAVIN: @LIZONTTIAN 说PON口上不再有性能展示的部分，只有使能的部分
    displayService(items,e);
}
function showUniService(e){
    var $port = entity.onuUniPortList[currentId.substring(7) - 1],
        uniAdminStatus = $port.uniAdminStatus == STATUS.UP,
        uniFlowEnable = $port.flowCtrl == STATUS.UP,
        uniAutoNegoEnable = $port.uniAutoNegotiationEnable == STATUS.UP,
        uni15minEnable = $port.perfStats15minuteEnable == STATUS.UP,
        uni24hourEnable = $port.perfStats24hourEnable == STATUS.UP,
        //电力ONU的环回使能、上行untag报文优先级、广播风暴抑制
        isElectricity = parseInt(entity.entityType) > 80 && parseInt(entity.entityType) < 85 ;
    var items = [];
    if(entity.onuEorG == "E"){
    	items[items.length] = {id:'uniAdminStatus', text: "@ONU.portEnable@", disabled:!operationDevicePower, handler: portEnableHandler, checked:uniAdminStatus };
    	//items[items.length] = {id:'uniDSLoopBackEnable', text: "@ONU.loopback@", disabled:!operationDevicePower, handler: loopbackHandler, hidden:!isElectricity };
    	items[items.length] = {id:'uniConfig', text: "@ONU.serviceCfg@" };
        var subMenu = [];
            //subMenu[subMenu.length] = {id:'uniFlowCtrlEnable', text: "@ONU.flowcontrol@",  handler :flowcontrolHandler, checked:uniFlowEnable };
            //subMenu[subMenu.length] = {id:'uniAutoNegoEnable', text: "@ONU.autonegEnable@",  handler :autonegEnableHandler, checked:uniAutoNegoEnable };
            subMenu[subMenu.length] = {id:'uniRate', text: "@ONU.uniSpeedDec@",  handler :uniSpeedDecHandler };
            subMenu[subMenu.length] = {id:'uniRateBak', text: "@ONU.uniSpeedDec@",  handler :uniSpeedDecHandlerBak };
            subMenu[subMenu.length] = {id:'uniAutoNegotiation', text: "@ONU.workMode@",  handler :workModeHandler };
            subMenu[subMenu.length] = {id:'uniUSUtgPri', text: "@ONU.uniUSUtgPriSimple@",  handler :uniUSUtgPriSimpleHandler};
            //subMenu[subMenu.length] = {id:'onuStormOutPacketRate', text: "@ONU.onuStormOutPacketRate@",  handler :onuStormOutPacketRateHandler, hidden:isElectricity };
            items[items.length-1].menu = subMenu;
            items[items.length] = {id:'igmpUni', text: "@ONU.igmpCfg@" };
        /* reset submenu **/
        subMenu = [];
            subMenu[subMenu.length] = {id:'igmpUniMvlan', text: "@ONU.uniMvlan@",  handler :function(){showIgmpUni(1);} };
            subMenu[subMenu.length] = {id:'igmpUniMgmgt', text: "@ONU.igmpUni@",  handler :function(){showIgmpUni(2);} };
            items[items.length-1].menu = subMenu;
        if(entity.entityType != ONU_10G_TYPE){
        	items[items.length] = {id:'uniMacAddrConfig', text: "@ONU.macLearn@",handler: macLearnHandler };
        }
        items[items.length] = {id:'uniMacAddrAgeTime', text: "@ONU.macAge@",handler: macAgeHandler };
        if(entity.entityType != ONU_10G_TYPE){
        	//items[items.length] = {id:'uniMacClear', text: "@ONU.macClean@",disabled:!operationDevicePower,handler: macCleanHandler };
        }
        items[items.length] = {id:'onuUniPerf', text: "@ONU.perfMgmt@"};
            subMenu = [];
            subMenu[subMenu.length] = {id:'uni15minEnable', text: "@ONU.stasitc15Enable@",disabled:!operationDevicePower, handler: uniStasitc15EnableHandler, checked:uni15minEnable };
            subMenu[subMenu.length] = {id:'uni24hourEnable', text: "@ONU.stastic24Enable@",disabled:!operationDevicePower, handler: uniStastic24EnableHandler, checked:uni24hourEnable };
            subMenu[subMenu.length] = {id:'uniPerfEnable', text: "@ONU.stasitcEnable@",disabled:!operationDevicePower, handler: uniStasitcEnableHandler, checked:uni15minEnable };
            items[items.length-1].menu = subMenu;
        /*    
        items[items.length] = {id:'vlanConfig', text: "@ONU.vlanCfg@"};
            subMenu = [];
            subMenu[subMenu.length] = {id:'uniVlanBaseConfig', text: "@ONU.basicCfg@",  handler :basicCfgHandler };
            subMenu[subMenu.length] = {id:'uniVlanModeConfig', text: "@ONU.modeCfg@",  handler :modeCfgHandler };
            items[items.length-1].menu = subMenu;
        */
        items[items.length] = {id:'uniVlanBindInfo', text: "@ONU.uniVlanInfo@",handler: uniVlanBindInfo };
        items[items.length] = {id:'uniPortVlan', text: "@ONU.uniVlanInfo@",handler: uniPortVlan };
    }
	
    if(entity.onuEorG == "G"){
    	items[items.length] = {id:'uniAdminStatus', text: "@ONU.portEnable@", disabled:!operationDevicePower, handler: portEnableHandler, checked:uniAdminStatus };
    	items[items.length] = {id:'uniPerfEnable', text: "@ONU.stasitcEnable@",disabled:!operationDevicePower, handler: uniStasitcEnableHandler, checked:uni15minEnable };
    	items[items.length] = {id:'uniVlanConfig', text: "@onu/EPON.vlanCfg@",disabled:!operationDevicePower, handler: uniVlanConfigHandler};
    	items[items.length] = {id:'macFilter', text: "@gpon/GPON.macFilter@",disabled:!operationDevicePower, handler: uniMacFilterHandler};
    }
    displayService(items,e);
}
function showComService(e){
    var items = []
	    items[items.length] = {id:'comBaseConfig', text: "@ELEC.comBasicCfg@",  handler: comBasicCfgHandler };
	    items[items.length] = {id:'comPortStastic', text: "@ELEC.comStatics@",  handler: comStaticsHandler };
	    items[items.length] = {id:'comRemoteConfig', text: "@ELEC.comRemoteCfg@",  handler: comRemoteCfgHandler };
    displayService(items,e);
}
