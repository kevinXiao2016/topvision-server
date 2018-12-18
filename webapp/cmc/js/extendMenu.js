//需要加载entityType.js
var EXTEND_CC8800S_TYPE = 30007;
var extendEntityId;
var extendCmcId;
var extendNote;
var extendType;
var loopbackStatus;
var loopbackStatusIcomcls;
function extendOper(items,entityId, cmcId, note,type, supportMap) {
    extendCmcId = entityId;
    extendNote = note;
    extendType = type;
    extendEntityId = entityId;
    items[items.length] = '-';
    if(EntityType.isCcmtsWithAgentType(type)){
    	loadSniUplinkLoopbackStatus(extendCmcId);
    	if(loopbackStatus == 1){
    		loopbackStatusIcomcls = "enableIconClass";
    	}else{
    		loopbackStatusIcomcls = "disableIconClass";
    	}
    	var menu = [];
    	menu.push({text: I18N.CCMTS.ccmtsPerformCollectConfig,id:"ccmts8800APerfManage",handler:ccmtsPerfManage,disabled:!cmcPerfPower});
    	if(supportMap && supportMap.cpuRateLimit){
    		menu.push({text: I18N.cmcSni.cpuPortRateLimit,id:"cpuRateLimit",handler: cpuRateLimit});
    	}
    	if(supportMap && supportMap.uplink){
    		menu.push({
                id:"cmcSniConfig",
                text:I18N.cmcSni.sniConfig,
	               menu:[
						  {text: I18N.cmcSni.sniRateLimit,id:"sniRateLimit",handler:sniRateLimit},
						  {text: I18N.cmcSni.loopbackEnable,id:"sniUplinkLoopbackStatus",handler: cmcLoopbackStatusEnable, iconCls:loopbackStatusIcomcls,disabled:!operationDevicePower},
						  {text: I18N.cmcSni.phyConfig,id:"sniPhyConfig4c",handler: cmcPhyConfigFor4c}
				   ]
    		});
    	}
    	menu.push({text: I18N.CCMTS.vlanConfig,id:"cmcVlan",handler: ccmtsVlanConfig});
    	menu.push({text: I18N.CCMTS.vlanSubConfig,id:"ccmtsIpSubVlan",handler: ccmtsIpSubVlan});
        items[items.length] = [
           { id:'ctmCmcMenu',
               text:'CMTS',
               enableScrolling: false,
               menu: menu
           }
       ];
    }else{
        items[items.length] = {text:I18N.CCMTS.ccmtsPerformCollectConfig,id:"ccmts8800APerfManage",handler:ccmtsPerfManage,disabled:!cmcPerfPower}
    }
    return items;
}

function loadSniUplinkLoopbackStatus(v){
	$.ajax({
        url: '/cmc/sni/loadSniUplinkLoopbackStatus.tv',
        type: 'POST',
        async: false,
        data: "cmcId=" + v +"&num=" + Math.random(),
        dataType:"json",
        success: function(json) {
            if(json!=null && json.cmcSniConfig != null){
            	loopbackStatus = json.cmcSniConfig.topCcmtsSniUplinkLoopbackStatus;
            }
        }, error: function(json) {
    }, cache: false,
	complete: function (XHR, TS) { XHR = null }
    });
}

function cpuRateLimit(){
	window.top.createDialog('cpuRateLimit', I18N.cmcSni.cpuPortRateLimit, 800, 500,
			'/cmc/sni/showCpuRateLimit.tv?cmcId=' + extendCmcId, null, true, true);
}

function sniRateLimit(){
	window.top.createDialog('sniRateLimit', I18N.cmcSni.sniRateLimit, 600, 370,
			'/cmc/sni/showSniRateLimit.tv?cmcId=' + extendCmcId, null, true, true);
}

function cmcPhyConfigFor4c(){
	window.top.createDialog('cmcPhyConfigFor4c', I18N.cmcSni.phyConfig, 600, 370,
			'/cmc/sni/showPhyConfigFor4c.tv?cmcId=' + extendCmcId, null, true, true);
}

function cmcLoopbackStatusEnable(){
	loopbackStatusTemp = loopbackStatus == 1 ? 2 : 1;
	var action =  loopbackStatus == 1 ?  I18N.CMC.button.close : I18N.CMC.select.open;
   window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg, String.format(I18N.cmcSni.setLoopbackEnable , action), function(type) {
       if (type == 'no') {
           return;
       }
		$.ajax({
	        url:'/cmc/sni/modifySniLoopbackStatus.tv?cmcId=' + extendCmcId + '&loopbackStatus=' + loopbackStatusTemp,
	        type:'POST',
	        dateType:'text',
	        success:function(response) {
	            if (response == "success") {
	                window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, action + I18N.cmcSni.setLoopbackEnableSuccess);
	            } else {
	                window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, action + I18N.cmcSni.setLoopbackEnableFail);
	            }
	        },
	        error:function() {
	            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, action + I18N.cmcSni.setLoopbackEnableFail);
	        },
	        cache:false
	    });
   }); 
}

function cmcDetailInfo(){
    viewCmcSnap(extendCmcId, extendNote, extendType);
}

function viewCmcSnap(cmcId,cmcNote,type){
	window.parent.addView('entity-' + cmcId, "CCMTS" + '[' + cmcNote + ']', 'entityTabIcon',
				'/cmc/showCmcPortal.tv?cmcId=' + extendCmcId);
}

function ccmtsPerfManage(){
	window.parent.addView('cmcPerfParamConfig', I18N.CMC.title.performanceCollectConfig,"bmenu_preference",'/cmc/perfTarget/showCmcPerfManage.tv?entityId=' + extendEntityId);
	/*window.top.createDialog('perfConfig', I18N.CMC.title.performanceCollectConfig, 800, 500,
		'/cmcperf/showCcmtsPerfConf.tv?cmcId=' + extendCmcId, null, true, true);*/
}

function refreshCC(){
	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg,I18N.CMC.tip.sureToRefreshCcmts,function(button,text){
		if(button == "yes"){
			window.parent.showWaitingDlg(I18N.CMC.tip.waiting, String.format(I18N.CMC.tip.refreshingCcmts),'waitingMsg','ext-mb-waiting');
			$.ajax({
    			url:'cmc/refreshCC.tv?cmcId='+ extendCmcId+'&cmcType='+extendType+'&entityId='+extendEntityId,
	  			type:'POST',
	  			dateType:'json',
	  			success:function(response){
        			if(response == "success"){
        				window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshCcmtsSuccess);
					}else{
						window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshCcmtsFail);
					}
					//TODO 页面该CC需同步刷新
        			doRefresh();
	  			},
	  			error:function(){
					 window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshCcmtsFail);
	  			},
	  			cache:false
    		});
		}
	});

}

function ccmtsVlanConfig(){
	var cmcId = extendCmcId;
	window.parent.addView("cmcVlan-"+cmcId, I18N.CCMTS.vlanConfig, "entityTabIcon", "/cmcVlan/showCcmtsVlanJsp.tv?cmcId="+cmcId);
}

function ccmtsIpSubVlan(){
	var cmcId = extendCmcId;			
	window.top.createDialog('ccmtsIpSubVlan', I18N.CCMTS.vlanSubInfo, 800, 500,
		'/cmcVlan/showCcmtsIpSubVlan.tv?cmcId=' + cmcId, null, true, true);
}

function ccmtsSpectrumGroupGlobal(){
	var cmcId = extendCmcId;
	window.top.createDialog('ccmtsSpectrumGroupGlobal', I18N.MENU.globalTitle, 600, 370,
			'/ccmtsspectrumgp/showDeviceGpGlobal.tv?entityId=' + cmcId, null, true, true);
}

function ccmtsSpectrumGroupMgmt(){
	var cmcId = extendCmcId;
	window.top.createDialog('ccmtsSpectrumGroupMgmt', I18N.MENU.groupTitle, 800, 500,
			'/ccmtsspectrumgp/showGroupManagePage.tv?entityId=' + cmcId, null, true, true);
}

function cmcReset(){
	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg,I18N.CMC.tip.sureToResetCcmts,function(button,text){
		if(button == "yes"){
			if(EntityType.isCcmtsWithoutAgentType(record.data.cmcDeviceStyle)){
				$.ajax({
	    			url:'cmc/resetCmc.tv?cmcId='+ extendCmcId,
		  			type:'POST',
		  			dateType:'json',
		  			success:function(response){
		  				response = eval("(" + response + ")");
	        			if(response.message == "success"){
	        				window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.resetCcmtsSuccess);
						}else{
							window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.resetCcmtsFail);
						}
		  			},
		  			error:function(){
		  			},
		  			cache:false
	    		});
			}else{
				$.ajax({
	    			url:'cmc/resetCmcWithAgent.tv?cmcId='+ extendCmcId,
		  			type:'POST',
		  			dateType:'json',
		  			success:function(response){
		  				response = eval("(" + response + ")");
	        			if(response.message == "success"){
	        				window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.resetCcmtsSuccess);
						}else{
							window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.resetCcmtsFail);
						}
		  			},
		  			error:function(){
		  			},
		  			cache:false
	    		});
			}
		}
	});
}