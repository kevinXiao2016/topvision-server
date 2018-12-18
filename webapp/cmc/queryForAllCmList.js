function loadCmListByOltJson(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()){
		var record = sm.getSelected();
		var loadCmListByOltJson =null;
	    Ext.Ajax.request({
	        url:('cm/getCmListByOlt.tv?cmId=' + record.data.cmId + '&start=0&limit=' + pageSize),
	        method:"post",
	        async: false,
	        success:function(response){
	        	loadCmListByOltJson = Ext.util.JSON.decode(response.responseText);
	        	store.loadData(loadCmListByOltJson);
  				$('#totalNum').text(I18N.CM.totalNum+loadCmListByOltJson.totalNum+"    ");
	  			$('#registeredNum').text(I18N.CM.onlineNum+loadCmListByOltJson.registeredNum+"    ");
  				var value = loadCmListByOltJson.entityId;
  				var text = loadCmListByOltJson.entityIp;
  				var html = "<select id='oltSelect' style='width: 105px;' onchange='oltSelectChange()' onclick='checkIfChange()'><option selected value="+value+">"+text+"</OPTION></select>";
  				$("#deviceType").get(0).options[1].selected = true;
  				$("#deviceType").trigger("onchange");
  				$("#oltSelect option").each(function(){
  					if($(this).val()==value){
  						$(this).attr("selected","selected");
  					}
  				});
  				$("#oltSelect").trigger("onchange");
  			},failure:function () {
	            window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.error);
	            var html = "<select id='oltSelect' style='width: 105px;' onchange='oltSelectChange()' onclick='checkIfChange()'><option selected value=0>" + I18N.COMMON.pleaseSelect + "</OPTION></select>";
  				$("#oltSelect").replaceWith(html);
	        }})
	}
}
function loadCmListByPonJson(){
	var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()){
        var record = sm.getSelected();
        var loadCmListByPonJson =null;
        Ext.Ajax.request({
            url:('cm/getCmListByPon.tv?cmId=' + record.data.cmId + '&start=0&limit=' + pageSize),
            method:"post",
            async: false,
            success:function(response){
            	loadCmListByPonJson = Ext.util.JSON.decode(response.responseText);
                store.loadData(loadCmListByPonJson);
  				$('#totalNum').text(I18N.CM.totalNum+loadCmListByPonJson.totalNum+"    ");
	  			$('#registeredNum').text(I18N.CM.onlineNum+loadCmListByPonJson.registeredNum+"    ");
  				var cmTopologyInfo = loadCmListByPonJson.cmTopologyInfo;
  				$("#deviceType").get(0).options[1].selected = true;
  				$("#deviceType").trigger("onchange");
  				$("#oltSelect option").each(function(){
  					if($(this).val()==cmTopologyInfo.entityId)
  						$(this).attr("selected", "selected");
  				});
  				$("#oltSelect").trigger("onchange");
  				$("#ponSelect option").each(function(){  					
  					if($(this).val()==cmTopologyInfo.ponId)
  						$(this).attr("selected", "selected");
  				});
  				$("#ponSelect").trigger("onchange");
            },failure:function () {
                window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.error);
                var ponhtml = "<select id='ponSelect' style='width: 65px;' onchange='ponSelectChange()'><option selected value=0>" + I18N.COMMON.pleaseSelect + "</OPTION></select>";
  				$("#ponSelect").replaceWith(ponhtml);
            }})
    }
}
function loadCmListByCmcJson(){
	var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()){
        var record = sm.getSelected();
        var loadCmListByCmcJson =null;
        Ext.Ajax.request({
            url:('cm/getCmListByCmc.tv?cmId=' + record.data.cmId + '&start=0&limit=' + pageSize),
            method:"post",
            async: false,
            success:function(response){
            	loadCmListByCmcJson = Ext.util.JSON.decode(response.responseText);
                store.loadData(loadCmListByCmcJson);
  				$('#totalNum').text(I18N.CM.totalNum+loadCmListByCmcJson.totalNum+"    ");
	  			$('#registeredNum').text(I18N.CM.onlineNum+loadCmListByCmcJson.registeredNum+"    ");
  				var cmTopologyInfo = loadCmListByCmcJson.cmTopologyInfo;
  				$("#deviceType").get(0).options[1].selected = true;
  				$("#deviceType").trigger("onchange");
  				//YangYi修改 2013-10-18  CM列表点击 所在CCMTS下CM 回填设备类型和CCMTS
  				//根据返回的deviceType来选择相应的设备类型
  				var deviceType = cmTopologyInfo.deviceType;
  				if($.inArray(deviceType, cmc)!=-1){
  					deviceType = 30002;
				}
				if($.inArray(deviceType, cmts)!=-1){
					deviceType = 40000;
				}
				if($.inArray(deviceType, olt)!=-1){
					deviceType = 10000;
				}
				$('#deviceType option[value='+deviceType+']').attr("selected", "selected");
  				$("#deviceType").trigger("onchange");
  				if (isSameAs8800B(cmTopologyInfo.deviceType) ){
  					$("#deviceType option").each(function(){
  	  					if($(this).val() == cmTopologyInfo.deviceType){
  	  						$(this).attr("selected", "selected");
  	  					}
  	  				});
  	  				$("#deviceType").trigger("onchange");
  	  				$("#cmcName option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.entityId)
  	  						$(this).attr("selected", "selected");
  	  				})
  	  				$("#cmcName").trigger("onchange");
  				} else if(cmTopologyInfo.deviceType == 10002||cmTopologyInfo.deviceType == 10003){
  	  				$("#oltSelect option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.entityId)
  	  						$(this).attr("selected", "selected");
  	  				});
  	  				$("#oltSelect").trigger("onchange");
  	  				$("#ponSelect option").each(function(){  					
  	  					if($(this).val()==cmTopologyInfo.ponId)
  	  						$(this).attr("selected", "selected");
  	  				});
  	  				$("#ponSelect").trigger("onchange");
  	  				$("#cmcName option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.cmcId)
  	  						$(this).attr("selected", "selected");
  	  				})
  	  				$("#cmcName").trigger("onchange");
  	  			// Modify by Victor@20150407原来是cmTopologyInfo.deviceType，改为 deviceType，40001改为40000
  				} else if(deviceType == 40000){
  					$("#cmtsName option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.entityId)
  	  						$(this).attr("selected", "selected");
  	  				})
  	  				$("#cmtsName").trigger("onchange");
  				}
            },failure:function () {
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.error);
            }})
    }
}
function loadCmListByUpPortJson(){
	var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()){
        var record = sm.getSelected();
        var loadCmListByUpPortJson =null;
        Ext.Ajax.request({
            url:('cm/getCmListByUpPort.tv?cmId=' + record.data.cmId + '&start=0&limit=' + pageSize),
            method:"post",
            async: false,
            success:function(response){
            	loadCmListByUpPortJson = Ext.util.JSON.decode(response.responseText);
                store.loadData(loadCmListByUpPortJson);
  				$('#totalNum').text(I18N.CM.totalNum+loadCmListByUpPortJson.totalNum+"    ");
	  			$('#registeredNum').text(I18N.CM.onlineNum+loadCmListByUpPortJson.registeredNum+"    ");
  				var cmTopologyInfo =loadCmListByUpPortJson.cmTopologyInfo;
  				$("#deviceType").get(0).options[1].selected = true;
  				$("#deviceType").trigger("onchange");
  				//YangYi修改 2013-10-18  CM列表点击 所在CCMTS下CM 回填设备类型和CCMTS
  				//根据返回的deviceType来选择相应的设备类型
  				var deviceType = cmTopologyInfo.deviceType;
  				if($.inArray(deviceType, cmc)!=-1){
  					deviceType = 30002;
				}
				if($.inArray(deviceType, cmts)!=-1){
					deviceType = 40000;
				}
				if($.inArray(deviceType, olt)!=-1){
					deviceType = 10000;
				}
				$('#deviceType option[value='+deviceType+']').attr("selected", "selected");
  				$("#deviceType").trigger("onchange");
  				if (isSameAs8800B(cmTopologyInfo.deviceType)){
  					$("#deviceType option").each(function(){
  	  					if($(this).val() == cmTopologyInfo.deviceType){
  	  						$(this).attr("selected", "selected");
  	  					}
  	  				});
  	  				$("#deviceType").trigger("onchange");
  	  				$("#cmcName option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.entityId)
  	  						$(this).attr("selected", "selected");
  	  				})
  	  				$("#cmcName").trigger("onchange");
  	  				$("#upChannel option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.docsIfUpChannelId)
  						$(this).attr("selected", "selected");
  	  				});
  				} else if(cmTopologyInfo.deviceType == 10002||cmTopologyInfo.deviceType == 10003){
  	  				$("#oltSelect option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.entityId)
  	  						$(this).attr("selected", "selected");
  	  				});
  	  				$("#oltSelect").trigger("onchange");
  	  				$("#ponSelect option").each(function(){  					
  	  					if($(this).val()==cmTopologyInfo.ponId)
  	  						$(this).attr("selected", "selected");
  	  				});
  	  				$("#ponSelect").trigger("onchange");
  	  				$("#cmcName option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.cmcId)
  	  						$(this).attr("selected", "selected");
  	  				})
  	  				$("#cmcName").trigger("onchange");
	  				$("#upChannel option").each(function(){
	  					if($(this).val()==cmTopologyInfo.docsIfUpChannelId)
	  						$(this).attr("selected", "selected");
	  				});
	  			// Modify by Victor@20150407原来是cmTopologyInfo.deviceType，改为 deviceType，40001改为40000
  				} else if(deviceType == 40000){
  					$("#cmtsName option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.entityId)
  	  						$(this).attr("selected", "selected");
  	  				})
  	  				$("#cmtsName").trigger("onchange");
  					$("#upChannel option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.upChannelIndex)
  						$(this).attr("selected", "selected");
  	  				});
  				}
            },failure:function () {
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CM.findFail);
            }})
    }
}
function loadCmListByDownPortJson(){
	var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()){
        var record = sm.getSelected();
        var loadCmListByDownPortJson =null;
        Ext.Ajax.request({
            url:('cm/getCmListByDownPort.tv?cmId=' + record.data.cmId + '&start=0&limit=' + pageSize),
            method:"post",
            async: false,
            success:function(response){
            	loadCmListByDownPortJson = Ext.util.JSON.decode(response.responseText);
                store.loadData(loadCmListByDownPortJson);
  				$('#totalNum').text(I18N.CM.totalNum+loadCmListByDownPortJson.totalNum+"    ");
	  			$('#registeredNum').text(I18N.CM.onlineNum+loadCmListByDownPortJson.registeredNum+"    ");
	  			var cmTopologyInfo =loadCmListByDownPortJson.cmTopologyInfo;
  				$("#deviceType").get(0).options[1].selected = true;
  				$("#deviceType").trigger("onchange");
  				//YangYi修改 2013-10-18  CM列表点击 所在CCMTS下CM 回填设备类型和CCMTS
  				//根据返回的deviceType来选择相应的设备类型
  				var deviceType = cmTopologyInfo.deviceType;
  				if($.inArray(deviceType, cmc)!=-1){
  					deviceType = 30002;
				}
				if($.inArray(deviceType, cmts)!=-1){
					deviceType = 40000;
				}
				if($.inArray(deviceType, olt)!=-1){
					deviceType = 10000;
				}
				$('#deviceType option[value='+deviceType+']').attr("selected", "selected");
  				$("#deviceType").trigger("onchange");
  				if (isSameAs8800B(cmTopologyInfo.deviceType)){
  					$("#deviceType option").each(function(){
  	  					if($(this).val() == cmTopologyInfo.deviceType){
  	  						$(this).attr("selected", "selected");
  	  					}
  	  				});
  	  				$("#deviceType").trigger("onchange");
  	  				$("#cmcName option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.entityId)
  	  						$(this).attr("selected", "selected");
  	  				})
  	  				$("#cmcName").trigger("onchange");
  	  				$("#downChannel option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.docsIfDownChannelId)
  						$(this).attr("selected", "selected");
  	  				});
  				} else if(cmTopologyInfo.deviceType == 10002||cmTopologyInfo.deviceType == 10003){
  	  				$("#oltSelect option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.entityId)
  	  						$(this).attr("selected", "selected");
  	  				});
  	  				$("#oltSelect").trigger("onchange");
  	  				$("#ponSelect option").each(function(){  					
  	  					if($(this).val()==cmTopologyInfo.ponId)
  	  						$(this).attr("selected", "selected");
  	  				});
  	  				$("#ponSelect").trigger("onchange");
  	  				$("#cmcName option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.cmcId)
  	  						$(this).attr("selected", "selected");
  	  				})
  	  				$("#cmcName").trigger("onchange");
	  				$("#downChannel option").each(function(){
	  					if($(this).val()==cmTopologyInfo.docsIfDownChannelId)
	  						$(this).attr("selected", "selected");
	  				});
	            // Modify by Victor@20150407原来是cmTopologyInfo.deviceType，改为 deviceType，40001改为40000
  				}else if(deviceType == 40000){
  					$("#cmtsName option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.entityId)
  	  						$(this).attr("selected", "selected");
  	  				})
  	  				$("#cmtsName").trigger("onchange");
  					$("#downChannel option").each(function(){
  	  					if($(this).val()==cmTopologyInfo.downChannelIndex)
  						$(this).attr("selected", "selected");
  	  				});
  				}
            },failure:function () {
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.error);
            }})
    }
}

function onRefreshClick() {
	store.reload(store.lastOptions);
}
function doRefresh(){
	refreshClick();
}
function showCmInfoOnCmc(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		window.top.createDialog('cmOnCmcInfo', I18N.CM.cmInfoOnCcmts, 1400, 400, 
			'cm/showCmList.tv', null, true, true);	
	}
}
function restarCm(){
	var sm = grid.getSelectionModel();
	var record = sm.getSelected();	
	var cmId = record.data.cmId;
	var cmIp = record.data.statusInetAddress;
	var status = "";
	if(record.data.statusValue == 6){
		status = "Online";
	}
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CM.confirmResetCm, function(type) {
        if (type == 'no') {
            return;
        } else{
			window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.cmList.resetting, 'ext-mb-waiting');
        	$.ajax({
	  			url:'cm/resetCm.tv',
	  			type:'POST',
	  			data:"cmId="+cmId+"&cmIp="+cmIp+"&status="+status,
	  			success:function(response){
	  				if(response.message == 'success'){
	  					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmList.resetSuccess);
	  				}else{
	  					if(response.reason == "SnmpException"){
	  						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.snmpUnreachable);
	  					}else if(response.reason == "PingException"){
	  						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.icmpUnreachable);
	  					}else if(response.reason == "offline"){
	  						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.cmOfflineTip);
	  					}
	  				}
	  			},
	  			error:function(){
	  				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.queryForAllCmList.resetFail);
	  			},
	  			cache:false
	  			});
        }
	});
}
function locateCm(){
	var sm = grid.getSelectionModel();
	var record = sm.getSelected();
	window.top.createDialog('showCmLocationDlg', I18N.CM.locate, "normal_16_9", 250, 'cm/showCmLocationInfo.tv?cmId=' + record.data.cmId, null, true, true);
}
function cmDetailInfo(){
	window.top.createDialog('showCmInfoDlg', I18N.CM.cmMessage, 500, 400, 'cm/showCmInfo.tv', null, true, true);
}
function setCmRefreshTime(){
var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		window.top.createDialog('setCmRefreshTime', I18N.CM.setCmRefreshTime, 400, 150, 
			'cm/setCmRefreshTime.tv', null, true, true);	
	}
}
function doOnload(){
	var map=new Map();
	if (deviceType == 10000){
		for(var i = 0; i < entityRelationList.length; i++){
			map.put(entityRelationList[i].entityId, entityRelationList[i].ip)
			}
		var oltPosition = Zeta$('oltSelect');
		for(var i = 0; i < map.size(); i++) {
			var option = document.createElement('option');
			option.value = map.element(i).key;
			option.text = map.element(i).value;
			try {
				oltPosition.add(option, null);
	        } catch(ex) {
	        	oltPosition.add(option);
	        }
		}
	}else if(deviceType == CC8800B_TYPE){
		for(var i = 0; i < entity8800BRelationList.length; i++){
			map.put(entity8800BRelationList[i].entityId, entity8800BRelationList[i].ip)
		}
		var cmcPosition = Zeta$('cmcName');
		for(var i = 0; i < map.size(); i++) {
			var option = document.createElement('option');
			option.value = map.element(i).key;
			option.text = map.element(i).value;
			try {
				cmcPosition.add(option, null);
	        } catch(ex) {
	        	cmcPosition.add(option);
	        }
		}
	}else if(deviceType == CC8800D_TYPE){
		for(var i = 0; i < entity8800DRelationList.length; i++){
			map.put(entity8800DRelationList[i].entityId, entity8800DRelationList[i].ip)
		}
		var cmcPosition = Zeta$('cmcName');
		for(var i = 0; i < map.size(); i++) {
			var option = document.createElement('option');
			option.value = map.element(i).key;
			option.text = map.element(i).value;
			try {
				cmcPosition.add(option, null);
	        } catch(ex) {
	        	cmcPosition.add(option);
	        }
		}
	}
}

function deviceTypeSelectChange(){
	$("#oltSelect")[0].outerHTML = "<select id='oltSelect' style='width: 105px;' onchange='oltSelectChange()'><option value='0' <s:if test='type==0'></s:if>" + I18N.COMMON.pleaseSelect + "</option></select>";
	$("#ponSelect")[0].outerHTML = "<select id='ponSelect' style='width: 65px;' onchange='ponSelectChange()'><option value='0' <s:if test='type==0'></s:if>" + I18N.COMMON.pleaseSelect + "</option></select>";
	$("#cmcName")[0].outerHTML = "<select id='cmcName' style='width: 105px;' onchange='cmcSelectChange()'><option value='0' <s:if test='type==0'></s:if>" + I18N.COMMON.pleaseSelect + "</option></select>";
	$("#upChannel")[0].outerHTML = "<select id='upChannel' style='width: 65px;'><option value='0' <s:if test='type==0'></s:if>" + I18N.COMMON.pleaseSelect + "</option></select>";
	$("#downChannel")[0].outerHTML = "<select id='downChannel' style='width: 65px;'><option value='0' <s:if test='type==0'></s:if>" + I18N.COMMON.pleaseSelect + "</option></select>";
	var deviceType = Zeta$('deviceType').value;
	var oltPosition = Zeta$('oltSelect');
	var ponPosition = Zeta$('ponSelect');
	var cmcPosition = Zeta$('cmcName');
	var cmtsPosition = Zeta$('cmtsName');
	var upChannelPosition = Zeta$('upChannel');
	var downChannelPosition = Zeta$('downChannel');
	var map=new Map();
	for(var i = 1,len = oltPosition.length;i <=len; i++)
    {
		oltPosition.options[1] = null;
    }
	for(var i = 1,len = ponPosition.length;i <=len; i++)
    {
		ponPosition.options[1] = null;
    }
	for(var i = 1,len = cmcPosition.length;i <=len; i++)
    {
        cmcPosition.options[1] = null;
    }
	for(var i = 1,len = upChannelPosition.length;i <=len; i++)
    {
		upChannelPosition.options[1] = null;
    }
	for(var i = 1,len = downChannelPosition.length;i <=len; i++)
    {
        downChannelPosition.options[1] = null;
    }
	for(var i = 1,len = upChannelPosition.length;i <=len; i++)
	{
		cmtsPosition.options[1] = null;
	}
	if (deviceType == 10000){
		for(var i = 0; i < entityRelationList.length; i++){
			map.put(entityRelationList[i].entityId, entityRelationList[i].ip)
			}
		var oltPosition = Zeta$('oltSelect');
		for(var i = 0; i < map.size(); i++) {
			var option = document.createElement('option');
			option.value = map.element(i).key;
			option.text = map.element(i).value;
			try {
				oltPosition.add(option, null);
	        } catch(ex) {
	        	oltPosition.add(option);
	        }
		}
	}else if(deviceType == CC8800B_TYPE){
		for(var i = 0; i < entity8800BRelationList.length; i++){
			map.put(entity8800BRelationList[i].entityId, entity8800BRelationList[i].ip)
		}
		var cmcPosition = Zeta$('cmcName');
		for(var i = 0; i < map.size(); i++) {
			var option = document.createElement('option');
			option.value = map.element(i).key;
			option.text = map.element(i).value;
			try {
				cmcPosition.add(option, null);
	        } catch(ex) {
	        	cmcPosition.add(option);
	        }
		}
	}else if(deviceType == 40000){
		for(var i = 0; i < entityCMTSRelationList.length; i++){
			map.put(entityCMTSRelationList[i].entityId, entityCMTSRelationList[i].ip)
		}
		var cmtsPosition = Zeta$('cmtsName');
		for(var i = 0; i < map.size(); i++) {
			var option = document.createElement('option');
			option.value = map.element(i).key;
			option.text = map.element(i).value;
			try {
				cmtsPosition.add(option, null);
	        } catch(ex) {
	        	cmtsPosition.add(option);
	        }
		}
	}else if(deviceType == CC8800D_TYPE){
		for(var i = 0; i < entity8800DRelationList.length; i++){
			map.put(entity8800DRelationList[i].entityId, entity8800DRelationList[i].ip)
		}
		var cmcPosition = Zeta$('cmcName');
		for(var i = 0; i < map.size(); i++) {
			var option = document.createElement('option');
			option.value = map.element(i).key;
			option.text = map.element(i).value;
			try {
				cmcPosition.add(option, null);
	        } catch(ex) {
	        	cmcPosition.add(option);
	        }
		}
	}
	onTypeChange();
}

function oltSelectChange(){
	$("#ponSelect")[0].outerHTML = "<select id='ponSelect' style='width: 65px;' onchange='ponSelectChange()'><option value='0' <s:if test='type==0'></s:if>" + I18N.COMMON.pleaseSelect + "</option></select>";
	$("#cmcName")[0].outerHTML = "<select id='cmcName' style='width: 105px;' onchange='cmcSelectChange()'><option value='0' <s:if test='type==0'></s:if>" + I18N.COMMON.pleaseSelect + "</option></select>";
	$("#upChannel")[0].outerHTML = "<select id='upChannel' style='width: 65px;'><option value='0' <s:if test='type==0'></s:if>" + I18N.COMMON.pleaseSelect + "</option></select>";
	$("#downChannel")[0].outerHTML = "<select id='downChannel' style='width: 65px;'><option value='0' <s:if test='type==0'></s:if>" + I18N.COMMON.pleaseSelect + "</option></select>";
	var entityId = Zeta$('oltSelect').value;
	var ponPosition = Zeta$('ponSelect');
	var cmcPosition = Zeta$('cmcName');
	var upChannelPosition = Zeta$('upChannel');
	var downChannelPosition = Zeta$('downChannel');
	var downChannelPosition = Zeta$('downChannel');
	var map=new Map();
	for(var i = 1,len = ponPosition.length;i <=len; i++)
    {
		ponPosition.options[1] = null;
    }
	for(var i = 1,len = cmcPosition.length;i <=len; i++)
    {
        cmcPosition.options[1] = null;
    }
	for(var i = 1,len = upChannelPosition.length;i <=len; i++)
    {
		upChannelPosition.options[1] = null;
    }
	for(var i = 1,len = downChannelPosition.length;i <=len; i++)
    {
        downChannelPosition.options[1] = null;
    }
	for(var i = 0; i < entityRelationList.length; i++){
		if(entityId == entityRelationList[i].entityId && entityRelationList[i].ponId != '0')
		map.put(entityRelationList[i].ponId, ((entityRelationList[i].ponIndex/0xFFFF)>>16) + "/" + ( entityRelationList[i].ponIndex >> 24) )
		}
	for(var i = 0; i < map.size(); i++) {
		var option = document.createElement('option');
		option.value = map.element(i).key;
		option.text = map.element(i).value;
		try {
			ponPosition.add(option, null);
        } catch(ex) {
        	ponPosition.add(option);
        }
	}
}

function cmcSelectChange(){
	var upChannelPosition = Zeta$('upChannel');
	var downChannelPosition = Zeta$('downChannel');
	var cmcPosition = Zeta$('cmcName');
	if(cmcPosition.value==0){
		upChannelPosition.outerHTML = "<select id='upChannel' style='width: 65px;'><option value='0'>" + I18N.COMMON.pleaseSelect + "</option></select>";
		downChannelPosition.outerHTML = "<select id='downChannel' style='width: 65px;'><option value='0'>" + I18N.COMMON.pleaseSelect + "</option></select>";
		return;
	}

	for(var i = 1,len = upChannelPosition.length;i <= len; i++)
    {
		upChannelPosition.options[1] = null;
    }
	for(var i = 1,len = downChannelPosition.length;i <= len; i++)
    {
        downChannelPosition.options[1] = null;
    }
	for(var i = 1; i < 5; i++) {
		var option = document.createElement('option');
		option.value = i;
		option.text = i;
		try {
			upChannelPosition.add(option, null);
        } catch(ex) {
        	upChannelPosition.add(option);
        }
	}
	
	for(var i = 1; i < 17; i++) {
		var option = document.createElement('option');
		option.value = i;
		option.text = i;
		try {
			downChannelPosition.add(option, null);
        } catch(ex) {
        	downChannelPosition.add(option);
        }
	}
}

function cmtsSelectChange(){
	var upChannelPosition = Zeta$('upChannel');
	var downChannelPosition = Zeta$('downChannel');
	var cmtsPosition = Zeta$('cmtsName');
	var cmcId = Zeta$('cmtsName').value;
	if(cmtsPosition.value==0){
		upChannelPosition.outerHTML = "<select id='upChannel' style='width: 50px;'><option value='0'>" + I18N.COMMON.pleaseSelect + "</option></select>";
		downChannelPosition.outerHTML = "<select id='downChannel' style='width: 50px;'><option value='0'>" + I18N.COMMON.pleaseSelect + "</option></select>";
		return;
	}
	for(var i = 1,len = upChannelPosition.length;i <= len; i++)
    {
		upChannelPosition.options[1] = null;
    }
	for(var i = 1,len = downChannelPosition.length;i <= len; i++)
    {
        downChannelPosition.options[1] = null;
    }
	$.ajax({
		url : '/cmts/cm/getCmtsChannel.tv',
		type : 'post',
		dataType : "json",
		async:false,
		data: "cmcId=" + cmcId + "&num=" + Math.random(),
		success : function(response) {
			if (response != null) {
				cmtsUpChannelList = response.cmtsUpChannelList;
				cmtsDownChannelList = response.cmtsDownChannelList;
				for(var i = 0; i < cmtsUpChannelList.length; i++) {
					var option = document.createElement('option');
					option.value = cmtsUpChannelList[i].channelIndex;
					option.text = cmtsUpChannelList[i].ifName;
					try {
						upChannelPosition.add(option, null);
			        } catch(ex) {
			        	upChannelPosition.add(option);
			        }
				}
				for(var i = 0; i < cmtsDownChannelList.length; i++) {
					var option = document.createElement('option');
					option.value = cmtsDownChannelList[i].channelIndex;
					option.text = cmtsDownChannelList[i].ifName;
					try {
						downChannelPosition.add(option, null);
			        } catch(ex) {
			        	downChannelPosition.add(option);
			        }
				}
			} else {
			}
		},
		error : function(response) {
		},
		cache : false
	});
}

function ponSelectChange(){
	var ponId = Zeta$('ponSelect').value;
	var cmcPosition = Zeta$('cmcName');
	var upChannelPosition = Zeta$('upChannel');
	var downChannelPosition = Zeta$('downChannel');
	var map=new Map();
	for(var i = 1,len = cmcPosition.length;i <=len; i++)
    {
		cmcPosition.options[1] = null;
    }
	for(var i = 1,len = upChannelPosition.length;i <=len; i++)
    {
		upChannelPosition.options[1] = null;
    }
	for(var i = 1,len = downChannelPosition.length;i <=len; i++)
    {
		downChannelPosition.options[1] = null;
    }
	for(var i = 0; i < entityRelationList.length; i++){
		if(ponId == entityRelationList[i].ponId && entityRelationList[i].cmcId != '0')
		map.put(entityRelationList[i].cmcId, (entityRelationList[i].cmcIndex>>27) + "/" + ((entityRelationList[i].cmcIndex & 0x07800000)>>23)+ "/" + 
		((entityRelationList[i].cmcIndex & 0x007F0000)>>16))
		
		}
	for(var i = 0; i < map.size(); i++) {
		var option = document.createElement('option');
		option.value = map.element(i).key;
		option.text = map.element(i).value;
		try {
			cmcPosition.add(option, null);
        } catch(ex) {
        	cmcPosition.add(option);
        }
	}
}

function showCM(){
	store.reload();
}

function refreshCmStaticInfo(deviceType,entityId,ponId,cmcId,upChannelId,downChannelId,cmMac){
  $.ajax({
		url:'cm/getCmStatusNum.tv',
		type:'POST',
		data:"deviceType=" + deviceType+ "&entityId="+entityId+"&ponId="+ponId+"&cmcId="+cmcId+"&upChannelId="+upChannelId+"&downChannelId="+downChannelId
		+"&cmMac=" + cmMac,
		dateType:'json',
		success:function(response){
			$('#totalNum').text(I18N.CM.totalNum+response.totalNum+"    ");
			$('#registeredNum').text(I18N.CM.onlineNum+response.registeredNum+"    ");
		},
		error:function(){
		},
		cache:false
	});
}
function refresh8800BCmStaticInfo(cmcId,upChannelId,downChannelId,cmMac){
	$.ajax({
		url:'cm/getCmStatusNum.tv',
		type:'POST',
		data:"cmcId="+cmcId+"&upChannelId="+upChannelId+"&downChannelId="+downChannelId
		+"&cmMac=" + cmMac,
		dateType:'json',
		success:function(response){
			$('#totalNum').text(I18N.CM.totalNum+response.totalNum+"    ");
			$('#registeredNum').text(I18N.CM.onlineNum+response.registeredNum+"    ");
		},
		error:function(){
		},
		cache:false
	});
}
function queryCm(){
	  var deviceType = Zeta$('deviceType').value;
	  var entityId = Zeta$('oltSelect').value;
	  var ponId =  Zeta$('ponSelect').value;
	  var cmcId =  Zeta$('cmcName').value;
	  var upChannelId = Zeta$('upChannel').value;
	  var downChannelId = Zeta$('downChannel').value;
	  var cmMac = Zeta$('cmMacAddress').value;
	  var pattern = /^[a-z0-9:]{1,17}$/i;
	  if(cmMac.length != 0 && cmMac.match(pattern)==null){
		  Ext.Msg.alert(I18N.COMMON.tip,I18N.CCMTS.macErrorMessage);
		  return;
	  }
	  if(!validateMacAddress(cmMac) && cmMac != '' && cmMac != null){
		  window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.macError);
		  return;
		  }	
	  //fanzhong 修改 2013-10-19, 针对CMTS传递cmcId
	  if(deviceType == 40000){
		  var cmcId =  Zeta$('cmtsName').value;
		  var entityId =  cmcId;
	  }
	  refreshCmStaticInfo(deviceType,entityId,ponId,cmcId,upChannelId,downChannelId,cmMac);
	  store.load({params: {deviceType: deviceType, entityId: entityId, ponId: ponId, cmcId: cmcId, upChannelId: upChannelId, downChannelId: downChannelId, cmMac: cmMac, start:0, limit:pageSize}});
	  Ext.apply(store.baseParams,store.lastOptions.params);
}

function query8800BCm(){
	  var cmcId =  Zeta$('cmcName').value;
	  var upChannelId = Zeta$('upChannel').value;
	  var downChannelId = Zeta$('downChannel').value;
	  var cmMac = Zeta$('cmMacAddress').value;
	  var pattern = /^[a-z0-9:]{1,17}$/i;
	  if(cmMac.length != 0 && cmMac.match(pattern)==null){
		  Ext.Msg.alert(I18N.COMMON.tip,I18N.CCMTS.macErrorMessage);
		  return;
	  }
	  if(!validateMacAddress(cmMac) && cmMac != '' && cmMac != null){
		  window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.macError);
		  return;
		  }	
	  refresh8800BCmStaticInfo(cmcId,upChannelId,downChannelId,cmMac);
	  store.load({params: {cmcId: cmcId, upChannelId: upChannelId, downChannelId: downChannelId, cmMac: cmMac, start:0, limit:pageSize}});
	  Ext.apply(store.baseParams,store.lastOptions.params);
}

function getCmByCpeIp(){
	var cpeIp= Zeta$('cpeIp').value;
	  store.on("beforeload",function(){
   		store.baseParams={cpeIp:cpeIp};
	  });
     store.load({params: {cpeIp:cpeIp}});
}
function restartCms(){
	var sm = grid.getSelectionModel();
	var recordArray = sm.getSelections();	
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CM.confirmResetCmAll, function(type) {
        if (type == 'no') {
            return;
        } else{
			window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.cmList.resetting, 'ext-mb-waiting');
        	var cmsArrayIp= new Array();
        	var cmsArrayId= new Array();
        	for(var val in recordArray){
        		if(val<recordArray.length){
        			var ipInfo = recordArray[val].data.statusInetAddress;
        			var idInfo = recordArray[val].data.cmId;
            		cmsArrayIp.add(ipInfo);
            		cmsArrayId.add(idInfo);
        		}
        	} 
        	$.ajax({
	  			url:'cm/restartCms.tv',
	  			type:'POST',
	  			data:"cmsArrayId="+cmsArrayId,
	  			dateType:'json',
	  			success:function(response){
					var result = true;
					var resultTip = "";
					if(response.snmpUnreachable.length !=0){
						resultTip += I18N.CMC.tip.snmpUnreachable
							+ "<br>" +transferArrayToTipString(response.snmpUnreachable,3,9);
						result = false;
					}
					if(response.icmpUnreachable.length !=0){
						resultTip += "<br>" +I18N.CMC.tip.icmpUnreachable
							+"<br>" +transferArrayToTipString(response.icmpUnreachable,3,9);
						result = false;
					}
					if(response.offline.length !=0){
						resultTip += "<br>" + I18N.CMC.tip.cmOfflineTip
							+"<br>" +transferArrayToTipString(response.offline,3,9);
						result = false;
					}
					if(result){
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmList.resetSuccess);
					}else{
						window.parent.showMessageDlg(I18N.COMMON.tip, resultTip);
					}
	  			},
	  			error:function(){
	  			},
	  			cache:false
	  			});
        }
	});
}

function transferArrayToTipString(array, column, max){
	var result="";
	if(array.length <= max){
		for(var i=0; i< array.length; i++){
			if(i%column == column-1){
				result += "[" + array[i] + "] " + "<br>";
			}else{
				result += "[" + array[i] + "] ";
			}
		}
	}else{
		for(var i=0; i< max; i++){
			if(i%column == column-1){
				result += "[" + array[i] + "] " + "<br>";
			}else{
				result += "[" + array[i] + "] ";
			}
		}
		result +="<br>...";
	}
	return result;
}
function refreshCmInfo(){
	var sm = grid.getSelectionModel();
	var record = sm.getSelected();
	var cmMac = record.data.statusMacAddress;
	var cmcId = record.data.cmcId;
	var cmId = record.data.cmId;
	var cmIp = record.data.statusInetAddress;
	var cmClassified = record.data.cmClassified;
	var cmAlias = record.data.cmAlias;
	var rowIndex=store.indexOf(record);
	$.ajax({
	      url: 'refreshCmInfo.tv?cmMac=' + cmMac + '&cmId=' + cmId +  '&cmIp=' + cmIp + '&cmcId=' + cmcId,
	      type: 'post',
	      dataType:"json",
	      success: function(response) {	   
      	    	if(response != null){
      	    		/*8800B无 以下参数，所以需要判断*/
      	    		var deviceType = "";
      	    		if(Zeta$('deviceType')!=null){
      	    			deviceType = Zeta$('deviceType').value;
      	    		}
      	    		var entityId = "";
      	    		if(Zeta$('oltSelect')!=null){
      	    			entityId = Zeta$('oltSelect').value;
      	    		}
      	    		var ponId = "";
      	    		if(Zeta$('ponSelect')!=null){
      	    			ponId = Zeta$('ponSelect').value;
      	    		}
    	 			var cmcId =  Zeta$('cmcName').value;
    	 			var upChannelId = Zeta$('upChannel').value;
    	 			var downChannelId = Zeta$('downChannel').value;
    	  			var cmMac = Zeta$('cmMacAddress').value;	
    	  			
    	  			//改变刷新单个cm的方式，不重新加载整个store，而是改变data的内容，更新表格
    	  			record.beginEdit();
    	  			record.set("statusMacAddress",response.statusMacAddress);
    	  			record.set("statusInetAddress",response.statusInetAddress);
    	  			record.set("statusValue",response.statusValue);
    	  			record.set("docsIfCmtsCmStatusSignalNoiseString",response.docsIfCmtsCmStatusSignalNoiseString);
    	  			record.set("cmClassified",cmClassified);
    	  			record.set("cmAlias",cmAlias);
    	  			record.commit();
      	    		refreshCmStaticInfo(deviceType,entityId,ponId,cmcId,upChannelId,downChannelId,cmMac);
      	    		showCmstatus(grid,rowIndex);
				 }else{
				     
				 }
			}, error: function(response) {
			}, cache: false
		}); 
}
function clickCmStatusButton(){
	if(clickCmIp!=null){
		$.ajax({
			url:'cm/showCmStatusInfo.tv?cmIp='+clickCmIp,
  			type:'POST',
  			dateType:'json',
  			success:function(response){
  				var cmStatus = response.cmStatus;
  				for(v in cmStatus){
						$("#"+v).replaceWith("<div  style='white-space: nowrap;' id="+v+">"+cmStatus[v]+"</div>");
				}
  			},
  			error:function(){
  			},
  			cache:false
		});
	}
}
function showCmstatus(grid, rowIndex, e){
	if(activeTabIndex <4 ){
		var record = grid.getStore().getAt(rowIndex);
		//支持cmts下cm modify by loyal
		var statusInetAddress = record.data.statusInetAddress;
		var statusIpAddress = record.data.statusIpAddress;
		var cmIp;
		if(statusIpAddress != null && statusIpAddress != ''){
			clickCmIp = statusIpAddress
		}else{
			clickCmIp = statusInetAddress;
		}
	    
		clickCmId = record.data.cmId;
		var status = record.data.statusValue;
		$.ajax({
			url:'cm/showCmStatusInfo.tv?cmIp='+clickCmIp+'&cmId='+clickCmId,
			type:'POST',
			dataType:'json',
			success:function(response){
			    if(response.pingResult===false){
			      $("#htmlId1").replaceWith(pingErrorHtml);
                  docsIfDownstreamChannelJson = null;
                  docsIfUpstreamChannelJson = null;
                  docsIfSignalQualityJson = null;
                  docsIf3CmtsCmUsStatusJson= null;
			    }else if(response.snmpResult===false){
			      $("#htmlId1").replaceWith(snmpErrorHtml);
			      $("#htmlId").find("a").click(function(){
                      window.top.showWindow({
                        title:snmpCheckTitle,
                        modal:true,
                        minimizable:false, 
                        maximizable:false,
                        closable:true,
                        width:260,
                        html:String.format(snmpCheckHtml,clickCmIp)
                      });
			      });
			      docsIfDownstreamChannelJson = null;
                  docsIfUpstreamChannelJson = null;
                  docsIfSignalQualityJson = null;
                  docsIf3CmtsCmUsStatusJson= null;
			    }else{
			      $("#htmlId1").replaceWith(baseInfohtml);
			      var cmStatus = response.cmStatus;
			      if(cmStatus != null){
			    	  docsIfDownstreamChannelJson = cmStatus.docsIfDownstreamChannelList;
			    	  docsIfUpstreamChannelJson = cmStatus.docsIfUpstreamChannelList;
			    	  docsIfSignalQualityJson = cmStatus.docsIfSignalQualityList;
			    	  docsIf3CmtsCmUsStatusJson= cmStatus.docsIf3CmtsCmUsStatusList;
			      }else{
			          docsIfDownstreamChannelJson = null;
			          docsIfUpstreamChannelJson = null;
                      docsIfSignalQualityJson = null;
                      docsIf3CmtsCmUsStatusJson= null;
			      }
			      if(activeTabIndex == 1){
			    	  upstreamChannel_store.loadData(docsIfUpstreamChannelJson);
			    	  downstreamChannel_store.loadData(docsIfDownstreamChannelJson);
			      }else if(activeTabIndex == 2){
			    	  docsIfSignalQuality_store.loadData(docsIfSignalQualityJson);
			    	  docsIf3CmtsCmUsStatus_store.loadData(docsIf3CmtsCmUsStatusJson);
			      }
                  for(v in cmStatus){
                      $("#"+v).replaceWith("<div style='white-space: nowrap;' id="+v+">"+cmStatus[v]+"</div>");
                  }
			    }
			},
			error:function(){
				
			},
			cache:false
		});
	}else{
		var record = grid.getStore().getAt(rowIndex);
		var clickCmId = record.data.cmId;
		cpeStore.load({params: {cmId: clickCmId}});
		$.ajax({
  			url:'cm/showRealCpeMaxCpe.tv?cmId='+clickCmId,
  			type:'POST',
  			dateType:'json',
  			success:function(response){
  				Zeta$('numId').value=response.realCpeMaxCpe;
  			},
  			error:function(){
  			},
  			cache:false
  		});
	}
}

function queryCmByCpeIp(){
	var cpeIp= Zeta$('cpeIp').value;
	var store11 = new Ext.data.JsonStore({
	    url: ('cm/showCmByCpeIp.tv?cpeIp='+cpeIp),
	    root: 'data',
	    remoteSort: false, 
	    fields: [
	    		'cmId',
	    		 'docsIfCmtsCmStatusIpAddress', 'docsIfCmtsCmStatusMacAddress',
	    		'docsIfCmtsCmStatusValueString', 'docsIfCmtsCmStatusRxPowerString', 'docsIfCmtsCmStatusSignalNoiseString'
	    		]
	});
	store11.setDefaultSort('cmId', 'ASC');
	grid.reconfigure(store11, cm) ;
	store11.load();
}

function cancelClick() {
	window.top.closeWindow('cmServiceStream');
}

function renderRFCTable() {
	// 下行射频
	if (downstreamChannel_grid == null) {
		var downstreamChannel_Cm = new Ext.grid.ColumnModel([ {
			header : I18N.CHANNEL.channel,
			dataIndex : 'docsIfDownChannelIdString',
			width : 40,
			align : 'center'
		}, {
			header : I18N.CMC.label.bandwidth + '(MHz)',
			dataIndex : 'docsIfDownChannelWidthForUnit',
			width : 75,
			align : 'center'
		}, {
			header : I18N.CHANNEL.width + '(Mbps)',
			dataIndex : 'downIfSpeedForUnit',
			width : 75,
			align : 'center'
		}, {
			header : I18N.cm.Frequency + '(MHz)',
			dataIndex : 'docsIfDownChannelFrequencyForUnit',
			width : 75,
			align : 'center'
		}, {
			header : I18N.cm.ReceivePower + '(@{unitConfigConstant.elecLevelUnit}@)', ${Constance.POWER_UNIT}
			dataIndex : 'docsIfDownChannelPowerForUnit',
			width : 120,
			align : 'center'
		}, {
			header : I18N.CHANNEL.modulationType,
			dataIndex : 'docsIfDownChannelModulationForUnit',
			width : 75,
			align : 'center'
		} ]);
		downstreamChannel_store = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(docsIfDownstreamChannelJson),
			reader : new Ext.data.JsonReader({}, [ {
				name : 'docsIfDownChannelId'
			}, {
				name : 'docsIfDownChannelIdString'
			}, {
				name : 'docsIfDownChannelWidthForUnit'
			}, {
				name : 'downIfSpeedForUnit'
			}, {
				name : 'docsIfDownChannelFrequencyForUnit'
			}, {
				name : 'docsIfDownChannelPowerForUnit'
			}, {
				name : 'docsIfDownChannelModulationForUnit'
			} ])
		});
		downstreamChannel_grid = new Ext.grid.GridPanel({
			border : true,
			region : 'center',
			height : 130,
			store : downstreamChannel_store,
			cls : 'normalTable',
			cm : downstreamChannel_Cm,
			title : I18N.cm.DownlinkRF,
			renderTo : "downstreamGrid",
			viewConfig : {
				forceFit : true
			}
		});
	}
	if (docsIfDownstreamChannelJson != null) {
		downstreamChannel_store.loadData(docsIfDownstreamChannelJson);
	}

	// 上行射频
	if (uptreamChannel_grid == null) {
		var upstreamChannel_Cm = new Ext.grid.ColumnModel([ {
			header : I18N.CHANNEL.channel,
			dataIndex : 'docsIfUpChannelIdString',
			width : 40,
			align : 'center'
		}, {
			header : I18N.CMC.label.bandwidth + '(MHz)',
			dataIndex : 'docsIfUpChannelWidthForUnit',
			width : 75,
			align : 'center'
		}, {
			header : I18N.CHANNEL.width + '(Mbps)',
			dataIndex : 'upIfSpeedForUnit',
			width : 75,
			align : 'center'
		}, {
			header : I18N.cm.Frequency + '(MHz)',
			dataIndex : 'docsIfUpChannelFrequencyForUnit',
			width : 75,
			align : 'center'
		}, {
			@{CONSTANT.POEWR_UNIT}@


			header : I18N.cm.SendPower + '(@{unitConfigConstant.elecLevelUnit}@)',
			${2014/10/24request.get}
			#{2014/10/24}
			dataIndex : 'txPower',
			width : 120,
			align : 'center'
		} ]);
		upstreamChannel_store = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(docsIfUpstreamChannelJson),
			reader : new Ext.data.JsonReader({}, [ {
				name : 'docsIfUpChannelId'
			}, {
				name : 'docsIfUpChannelIdString'
			}, {
				name : 'docsIfUpChannelWidthForUnit'
			}, {
				name : 'upIfSpeedForUnit'
			}, {
				name : 'docsIfUpChannelFrequencyForUnit'
			}, {
				name : 'txPower'
			} ])
		});
		uptreamChannel_grid = new Ext.grid.GridPanel({
			border : true,
			region : 'center',
			height : 130,
			store : upstreamChannel_store,
			cls : "normalTable",
			cm : upstreamChannel_Cm,
			title : I18N.cm.UplinkRF,
			renderTo : "upstreamGrid",
			viewConfig : {
				forceFit : true
			}
		});
	}
	if (docsIfUpstreamChannelJson != null) {
		upstreamChannel_store.loadData(docsIfUpstreamChannelJson);
	}
}

function renderSQTable() {
	// 下行信号质量
	if (docsIfSignalQuality_grid == null) {
		var docsIfSignalQuality_Cm = new Ext.grid.ColumnModel([ {
			header : I18N.CHANNEL.channel,
			dataIndex : 'downChanelIdString',
			width : 40,
			align : 'center'
		}/*, {
			header : I18N.cm.Frequency + '(MHz)',
			dataIndex : 'docsIfDownChannelFrequencyForUnit',
			width : 75,
			align : 'center'
		}*/, {
			header : 'SNR(dB)',
			dataIndex : 'docsIfSigQSignalNoiseForUnit',
			width : 75,
			align : 'center'
		}, {
			header : I18N.CM.unerroreds,
			dataIndex : 'docsIfSigQUnerroredsForUnit',
			width : 110,
			align : 'center'
		}, {
			header : I18N.CM.correcteds,
			dataIndex : 'docsIfSigQCorrectedsForUnit',
			width : 110,
			align : 'center'
		}, {
			header : I18N.CM.uncorrectables,
			dataIndex : 'docsIfSigQUncorrectablesForUnit',
			width : 110,
			align : 'center'
		} ]);
		docsIfSignalQuality_store = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(docsIfSignalQualityJson),
			reader : new Ext.data.JsonReader({}, [ {
				name : 'downChanelId'
			}, {
				name : 'downChanelIdString'
			}, {
				name : 'docsIfSigQMicroreflectionsForUnit'
			}, {
				name : 'docsIfSigQSignalNoiseForUnit'
			}, {
				name : 'docsIfSigQUnerroredsForUnit'
			}, {
				name : 'docsIfSigQCorrectedsForUnit'
			}, {
				name : 'docsIfSigQUncorrectablesForUnit'
			}, {
				name : 'docsIfDownChannelFrequencyForUnit'
			} ])
		});
		docsIfSignalQuality_grid = new Ext.grid.GridPanel({
			border : true,
			region : 'center',
			height : 130,
			cls : "normalTable",
			store : docsIfSignalQuality_store,
			cm : docsIfSignalQuality_Cm,
			title : I18N.cm.Downsignalquality,
			renderTo : "downSQGrid",
			viewConfig : {
				forceFit : true
			}
		});
	}
	if (docsIfSignalQualityJson != null) {
		docsIfSignalQuality_store.loadData(docsIfSignalQualityJson);
	}

	// 上行信号质量
	if (docsIf3CmtsCmUsStatus_grid == null) {
		var docsIf3CmtsCmUsStatus_Cm = new Ext.grid.ColumnModel([ {
			header : I18N.CHANNEL.channel,
			dataIndex : 'upChannelIdString',
			width : 40,
			align : 'center'
		}/*, {
			header : I18N.cm.Frequency + '(MHz)',
			dataIndex : 'docsIfUpChannelFrequencyForUnit',
			width : 75,
			align : 'center'
		}*/, {
			header : 'SNR(dB)',
			dataIndex : 'cmUsStatusSignalNoiseForUnit',
			width : 80,
			align : 'center'
		}, {
			header : I18N.CM.unerroreds,
			dataIndex : 'cmUsStatusUnerroredsForUnit',
			width : 110,
			align : 'center'
		}, {
			header : I18N.CM.correcteds,
			dataIndex : 'cmUsStatusCorrectedsForUnit',
			width : 110,
			align : 'center'
		}, {
			header : I18N.CM.uncorrectables,
			dataIndex : 'cmUsStatusUncorrectablesForUnit',
			width : 110,
			align : 'center'
		} ]);
		docsIf3CmtsCmUsStatus_store = new Ext.data.Store({
			proxy : new Ext.data.MemoryProxy(docsIf3CmtsCmUsStatusJson),
			reader : new Ext.data.JsonReader({}, [ {
				name : 'upChannelId'
			}, {
				name : 'upChannelIdString'
			}, {
				name : 'docsIfUpChannelFrequencyForUnit'
			}, {
				name : 'cmUsStatusSignalNoiseForUnit'
			}, {
				name : 'cmUsStatusUnerroredsForUnit'
			}, {
				name : 'cmUsStatusCorrectedsForUnit'
			}, {
				name : 'cmUsStatusUncorrectablesForUnit'
			}, {
				name : 'docsIfUpChannelFrequencyForUnit'
			} ])
		});
		docsIf3CmtsCmUsStatus_grid = new Ext.grid.GridPanel({
			border : true,
			region : 'center',
			height : 130,
			store : docsIf3CmtsCmUsStatus_store,
			cls : 'normalTable',
			cm : docsIf3CmtsCmUsStatus_Cm,
			title : I18N.cm.Upsignalquality,
			renderTo : "upSQGrid",
			viewConfig : {
				forceFit : true
			}
		});
	}
	if (docsIf3CmtsCmUsStatusJson != null) {
		docsIf3CmtsCmUsStatus_store.loadData(docsIf3CmtsCmUsStatusJson);
	}
}

function showCmProOrCpeInfo(){
	var h = $(window).height() - 400;
	if(h < 100){ h = 100};
	var w = $(window).width();
	
	var tab =  new Ext.TabPanel({
		id:"cmcpeTab",
		renderTo:"cmAndCpeInfoTab",
		activeTab: activeTabIndex,
		border: true,
		width:w,
		viewConfig: { forceFit:true, hideGroupedColumn: true,
            enableNoGroups: true},
		height:h,
		listeners:{
			tabchange : function(t,p){
				if(p.getId() == "RFC_TAB"){
					renderRFCTable();
				}else if(p.getId() == "SQ_TAB"){
					renderSQTable();
				}
			} 
		},
		items:[
		    {
		    	html: baseInfohtml,
		    	autoScroll : true,
		    	title:I18N.CM.cmProperty,
		    	listeners:{'activate':function(){
		    		activeTabIndex=0;
				}}
			},{
		    	html: $("#shepinHtml").html(),
		    	autoScroll : true,
		    	title:I18N.CCMTS.rfModuleStatus,
		    	id : 'RFC_TAB',
		    	listeners:{'activate':function(){
		    		activeTabIndex=1;
				}}
			},
			{
		    	html:$("#signalQualityHtml").html(),
		    	autoScroll : true,
		    	title:I18N.text.signalQuality,
		    	id : 'SQ_TAB',
		    	listeners:{'activate':function(){
		    		activeTabIndex=2;
				}}
			}
		    ]
	});
}

function hideInfo(){
	$(".changeB").hide();
}
function onTypeChange(){
	var type = $("#deviceType option:selected").attr("value");
	switch(type){
	case "0":
		document.getElementById("olt").style.display='block';
		document.getElementById("pon").style.display='block';
		document.getElementById("cmcDiv").style.display='block';
		document.getElementById("upChannelDiv").style.display='block';
		document.getElementById("downChannelDiv").style.display='block';
		document.getElementById("cmtsDiv").style.display='none';
		break;
	case "10000":
		document.getElementById("olt").style.display='block';
		document.getElementById("pon").style.display='block';
		document.getElementById("cmcDiv").style.display='block';
		document.getElementById("upChannelDiv").style.display='block';
		document.getElementById("downChannelDiv").style.display='block';
		document.getElementById("cmtsDiv").style.display='none';
		break;	
	case "30004":
	case "30002":
		document.getElementById("olt").style.display='none';
		document.getElementById("pon").style.display='none';
		document.getElementById("cmcDiv").style.display='block';
		document.getElementById("upChannelDiv").style.display='block';
		document.getElementById("downChannelDiv").style.display='block';
		document.getElementById("cmtsDiv").style.display='none';
		break;	
	case "40000":
		document.getElementById("olt").style.display='none';
		document.getElementById("pon").style.display='none';
		document.getElementById("cmcDiv").style.display='none';
		document.getElementById("upChannelDiv").style.display='block';
		document.getElementById("downChannelDiv").style.display='block';
		document.getElementById("cmtsDiv").style.display='block';
	}
}
function checkIfChange(){
 	if(Zeta$('oltSelect').length<=1){
		$("#oltSelect")[0].outerHTML = "<SELECT style='WIDTH: 105px' id=oltSelect onclick=checkIfChange() onchange=oltSelectChange()><OPTION selected value=0>" + I18N.COMMON.pleaseSelect + "</OPTION></SELECT>";//replaceWith(html);
		$("#ponSelect")[0].outerHTML = "<select id='ponSelect' style='width: 65px;' onchange='ponSelectChange()'><option value='0' <s:if test='type==0'>selected</s:if>>" + I18N.COMMON.pleaseSelect + "</option></select>";
		$("#cmcName")[0].outerHTML = "<select id='cmcName' style='width: 105px;' onchange='cmcSelectChange()'><option value='0' <s:if test='type==0'>selected</s:if>>" + I18N.COMMON.pleaseSelect + "</option></select>";
		$("#upChannel")[0].outerHTML = "<select id='upChannel' style='width: 65px;'><option value='0' <s:if test='type==0'>selected</s:if>>" + I18N.COMMON.pleaseSelect + "</option></select>";
		$("#downChannel")[0].outerHTML = "<select id='downChannel' style='width: 65px;'><option value='0' <s:if test='type==0'>selected</s:if>>" + I18N.COMMON.pleaseSelect + "</option></select>";
		onTypeChange();
	}; 
}

function validateMacAddress(macaddr)
{
   var reg1 = /^([A-Fa-f0-9]{2,2}\:){0,5}[A-Fa-f0-9]{0,2}$/;
   if (reg1.test(macaddr)) {
      return true;
   } else {
      return false;
   }
}

function addListener(element,e,fn){    
    if(element.addEventListener){    
    element.addEventListener(e,fn,false);    
    } else {    
    element.attachEvent("on" + e,fn);    
    }    
}
function checkInfo(){
	var s = $('#cmMacAddress');
	var cmMacAddress = Zeta$('cmMacAddress').value;
	if(cmMacAddress.length == 0){
		return;
	}
	var pattern = /^[a-z0-9:]{1,17}$/i;
	var result = cmMacAddress.match(pattern);
	if(result==null){
		Ext.Msg.alert(I18N.COMMON.tip,I18N.CCMTS.macErrorMessage);
	}
}

/*
 * Map对象，实现Map功能
 * 
 * 
 * size() 获取Map元素个数 isEmpty() 判断Map是否为空 clear() 删除Map所有元素 put(key, value)
 * 向Map中增加元素（key, value) remove(key) 删除指定key的元素，成功返回true，失败返回false get(key)
 * 获取指定key的元素值value，失败返回null element(index)
 * 获取指定索引的元素（使用element.key，element.value获取key和value），失败返回null containsKey(key)
 * 判断Map中是否含有指定key的元素 containsValue(value) 判断Map中是否含有指定value的元素 keys()
 * 获取Map中所有key的数组（array） values() 获取Map中所有value的数组（array）
 * 
 */
function Map() {
	this.elements = new Array();

	// 获取Map元素个数
	this.size = function() {
		return this.elements.length;
	},

	// 判断Map是否为空
	this.isEmpty = function() {
		return (this.elements.length < 1);
	},

	// 删除Map所有元素
	this.clear = function() {
		this.elements = new Array();
	},

	// 向Map中增加元素（key, value)
	this.put = function(_key, _value) {
		if (this.containsKey(_key) == true) {
			if (this.containsValue(_value)) {
				if (this.remove(_key) == true) {
					this.elements.push({
						key : _key,
						value : _value
					});
				}
			} else {
				this.elements.push({
					key : _key,
					value : _value
				});
			}
		} else {
			this.elements.push({
				key : _key,
				value : _value
			});
		}
	},

	// 删除指定key的元素，成功返回true，失败返回false
	this.remove = function(_key) {
		var bln = false;
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					this.elements.splice(i, 1);
					return true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	},

	// 获取指定key的元素值value，失败返回null
	this.get = function(_key) {
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					return this.elements[i].value;
				}
			}
		} catch (e) {
			return null;
		}
	},

	// 获取指定索引的元素（使用element.key，element.value获取key和value），失败返回null
	this.element = function(_index) {
		if (_index < 0 || _index >= this.elements.length) {
			return null;
		}
		return this.elements[_index];
	},

	// 判断Map中是否含有指定key的元素
	this.containsKey = function(_key) {
		var bln = false;
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					bln = true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	},

	// 判断Map中是否含有指定value的元素
	this.containsValue = function(_value) {
		var bln = false;
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].value == _value) {
					bln = true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	},

	// 获取Map中所有key的数组（array）
	this.keys = function() {
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].key);
		}
		return arr;
	},

	//获取Map中所有value的数组（array）
	this.values = function() {
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].value);
		}
		return arr;
	};
}
