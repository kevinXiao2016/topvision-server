var userObj = {};

function extendOper(items,entityId,ip, name) {
	if(entityId && ip){
		userObj.userObjId = entityId;
		userObj.ip = ip;
		userObj.name = name;
	}else{
		userObj = selObj;
		/*var selectee= selectionModel.getSelection();
		userObj = selectee.getUserObject();*/
	}
    items[items.length] = '-';
    items[items.length] = [
    { id:'ctmOltMenu',
          text:'OLT',
          enableScrolling: false,
          menu: [{
                	 id: 'oltBasicConfig',
                	 text: I18N.MENU.basicConfig,
                	 menu: [{
                         id:'oltSaveConfig',
                         text:I18N.MENU.saveConfig,
                         disabled:!operationDevicePower,
                         handler:function() {
                             window.parent.showConfirmDlg(I18N.MENU.tip, I18N.MENU.cfmSave, function(type) {
                                 if (type == 'no') {
                                     return;
                                 }
                                 window.top.showWaitingDlg(I18N.MENU.wait, I18N.MENU.saving, 'ext-mb-waiting');
                                 $.ajax({
                                     url: '/configBackup/saveConfig.tv',
                                     type: 'POST',
                                     data: "entityId=" + userObj.userObjId,
                                     success: function(json) {
                                         if (json.message) {
                                     		window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.saveError);
                                     	} else {
                                     		top.nm3kRightClickTips({
                                				title: I18N.MENU.tip,
                                				html: I18N.MENU.saveOk
                                			});
                                     		//window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.saveOk);
                                     	}
                                     }, error: function(json) {
                                     	window.parent.showMessageDlg(I18N.MENU.tip,  I18N.MENU.saveError);
                                 	}, dataType: 'json', cache: false
                                 });
                             });
                         }
                     },{
                         id:'oltSysTimeConfig',
                         text:I18N.MENU.checkSysTime,
                         disabled:!operationDevicePower,
                         handler:function() {
                             window.parent.showConfirmDlg(I18N.MENU.tip, I18N.MENU.cfmCheckSystime, function(type) {
                                 if (type == 'no') {
                                     return;
                                 }
                                 window.top.showWaitingDlg(I18N.MENU.wait, I18N.MENU.checking, 'ext-mb-waiting');
                 				Ext.Ajax.request({
                 					url:"../epon/checkSysTiming.tv",
                 					success: function (text) {
                 						if(text!=null&&text.responseText=='success'){
                 				             window.parent.closeWaitingDlg();
                 				             top.nm3kRightClickTips({
                                				title: I18N.MENU.tip,
                                				html: I18N.MENU.checkTimeOk
                 				             });
                 				             //window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.checkTimeOk);
                 						}else{ 
                 							 window.parent.closeWaitingDlg();
                 						     window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.checkTimeError);
                 						}
                 				    }, failure: function () {
                 				        window.parent.closeWaitingDlg();
                 				        window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.checkTimeError);
                 				    }, params: {entityId : userObj.userObjId}});
                             });
                         }
                     },{
                         id:'oltRestoreFactoryConfig',
                         text:I18N.MENU.resoreFactoryCfg,
                         disabled:!operationDevicePower,
                         handler:function() {
                             window.parent.showConfirmDlg(I18N.MENU.tip, I18N.MENU.cfmRestoreFactory, function(type) {
                                 if (type == 'no') {
                                     return;
                                 }
                                 window.top.showWaitingDlg(I18N.MENU.wait, I18N.MENU.restoringFactory, 'ext-mb-waiting');
                                 $.ajax({
                                     url: '/epon/restoreOlt.tv',
                                     type: 'POST',
                                     data: "entityId=" + userObj.userObjId,
                                     success: function(text) {
                                    	 window.parent.closeWaitingDlg();
                                    	 if(text == 'success'){
                                    		 window.parent.showConfirmDlg(I18N.MENU.tip, I18N.MENU.restoreReset, function(type) {
                                                 if (type == 'no') {
                                                     return;
                                                 }
                                                 window.top.showWaitingDlg(I18N.MENU.wait, I18N.MENU.restoring, 'ext-mb-waiting');
                                                 $.ajax({
                                                     url: '/epon/resetOlt.tv',
                                                     type: 'POST',
                                                     data: "entityId=" + userObj.userObjId,
                                                     success: function(text) {              	
                                                    	 window.parent.closeWaitingDlg();
                                                    	 if(text == 'success'){
                                                    		 top.nm3kRightClickTips({
                                                 				title: I18N.MENU.tip,
                                                 				html: I18N.MENU.resetOk
                                  				             });
                                                    		 //window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.resetOk);
                                                    	 }else{
                                                    		 window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.resetError);
                                                    	 }
                                                     }, error: function() {
                                                     	window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.resetError);
                                                 	}, dataType: 'text', cache: false
                                                 });
                                    		 })
                                    	 }else{
                                    		 window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.restoreError);
                                    	 }
                                     }, error: function() {
                                     	window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.restoreError);
                                 	}, dataType: 'text', cache: false
                                 });
                             });
                         }
                     },{
                         id:'oltResetConfig',
                         text:I18N.MENU.resetDevice,
                         disabled:!operationDevicePower,
                         handler:function() {
                             window.parent.showConfirmDlg(I18N.MENU.tip, I18N.MENU.cfmReset, function(type) {
                                 if (type == 'no') {
                                     return;
                                 }
                                 window.top.showWaitingDlg(I18N.MENU.wait, I18N.MENU.restarting, 'ext-mb-waiting');
                                 $.ajax({
                                     url: '/epon/resetOlt.tv',
                                     type: 'POST',
                                     data: "entityId=" + userObj.userObjId,
                                     success: function(text) {              	
                                    	 window.parent.closeWaitingDlg();
                                    	 if(text == 'success'){
                                    		 top.nm3kRightClickTips({
                                  				title: I18N.MENU.tip,
                                  				html: I18N.MENU.restartSuccess
                   				             });
                                    		 //window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.restartSuccess);
                                    	 }else{
                                    		 window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.restartFail);
                                    	 }
                                     }, error: function() {
                                     	window.parent.showMessageDlg(I18N.MENU.tip, I18N.MENU.restartFail);
                                 	}, dataType: 'text', cache: false
                                 });
                             });
                         }
                     }]
          },/*{
              id:'perfThreshold',
              text:I18N.MENU.perfMgmt,
              menu:[{
                   id:'collectConfig',
                   text:I18N.MENU.collectRecordSet,
                   handler:function() {
                       window.top.createDialog('collectConfig', I18N.MENU.collectRecordSet, 600, 370, 'epon/perf/showPerfStatsGlobalSet.tv?entityId=' + userObj.userObjId, null, true, true);
                 }
               },{
                   id:'perfThresholdConfig',
                   text:I18N.MENU.thresholdCfg,
                   handler:function() {
                       window.top.createDialog('perfThresholdConfig', I18N.MENU.thresholdCfg, 800, 500, 'epon/perf/showPerfThresholdJsp.tv?entityId=' + userObj.userObjId + '&deviceType=olt', null, true, true);
                   }
                }]
            },*/{
          	id: 'oltAlertMgmt',
          	text: I18N.MENU.alertCfg,
          	menu: [{
          		id: 'oltAlertCheck',
          		text: I18N.MENU.alertDisplay,
          		handler: function() {
          			setTimeout(function(){window.top.addView('entity-' + userObj.userObjId, I18N.COMMON.entity + '[' + userObj.ip + ']',
          					'entityTabIcon', '/epon/alert/showOltAlert.tv?module=5&entityId=' + userObj.userObjId,null,true);
          			},100);
          			//location.href = "/epon/alert/showOltAlert.tv?module=5&entityId=" + userObj.userObjId;
          		}
          	},{
          		id: 'oltTrapServerMgmt',
          		text: I18N.MENU.trapCfg,
          		handler: function() {
          			// 弹出窗口后，停止计时。
              		timer = Number.NEGATIVE_INFINITY;
                  	window.parent.createDialog("oltTrapConfig", I18N.MENU.trapSet, 800, 500, "/epon/alert/showOltTrapServerManagement.tv?entityId=" + userObj.userObjId, null, true, true);
          		}
          	}/*,{
          		id: 'oltAlertMask',
          		text: I18N.MENU.alertDecl,
          		handler: function() {
          			// 弹出窗口后，停止计时。
              		timer = Number.NEGATIVE_INFINITY;
                  	window.parent.createDialog("oltAlertMask", I18N.MENU.alertDeclMgmt, 800, 500, "/epon/alert/showOltAlertMask.tv?entityId=" + userObj.userObjId, null, true, true);
          		}
          	}*/]
          },{
              id:'oltfileMgmtConfig',
              text:I18N.MENU.fileManagement,
              handler:function() {
              	// 弹出窗口后，停止计时。
              	//timer = Number.NEGATIVE_INFINITY;
                window.parent.createDialog("fileManage", I18N.MENU.fileManagement, 800, 500, "/epon/showFileManage.tv?entityId=" + userObj.userObjId, null, true, true);
              }
          }
		/*  在此次版本中屏蔽自动跳频配置
		 {
          id:"ccmtsSpectrumGroup",
          text:I18N.MENU.ccmtsSpectrum,
           menu:[
                 {text: I18N.MENU.globalconfig,id:"ccmtsSpectrumGroupGlobal",handler: ccmtsSpectrumGroupGlobal},
                 {text: I18N.MENU.groupManage,id:"ccmtsSpectrumGroupMgmt",handler: ccmtsSpectrumGroupMgmt}
                 ]
		 }
		*/
        ]
    }
    ];
    return items;
}

function ccmtsSpectrumGroupGlobal(){
	window.top.createDialog('ccmtsSpectrumGroupGlobal', I18N.MENU.globalTitle, 600, 370,
			'/ccmtsspectrumgp/showDeviceGpGlobal.tv?entityId=' + userObj.userObjId, null, true, true);
}

function ccmtsSpectrumGroupMgmt(){
	window.top.createDialog('ccmtsSpectrumGroupMgmt', I18N.MENU.groupTitle, 800, 500,
			'/ccmtsspectrumgp/showGroupManagePage.tv?entityId=' + userObj.userObjId, null, true, true);
}
