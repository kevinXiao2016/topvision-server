function exportExcel(grid,title){
	top.ExcelUtil.exportGridToExcel(grid,title);
}

function nameRender(value, p, record){
    var cmcId = record.data.parentId;
    var mac = record.data.mac;
    var entityId = record.data.cmcId;
    return String.format("<a href='javascript:;' onclick='showEntityPortal({0},\"{1}\",\"{2}\",{3})'>{2}</a>",cmcId, mac, value,entityId)
}

function macRender(value, p, record){
    var mac = record.data.topCcmtsSysMacAddr;
    if(mac){
    	return mac;
    }else{
    	return "--";
    }
   
}
    
function showEntityPortal(cmcId,mac,name,entityId){
    if(cmcId){
        showCcmtsPortal(entityId,mac,name);
    }else{
        window.top.addView("entity-" + entityId,name,'entityTabIcon',
            "/portal/showEntitySnapJsp.tv?module=1&entityId=" + entityId);
    }
}
    
function showCcmtsPortal(cmcId , cmcMac ,cmcName){
    window.parent.addView('entity-' + cmcId, cmcName , 'entityTabIcon','/cmc/showCmcPortal.tv?cmcId=' + cmcId + '&entityId=' + cmcId);
}

function showChannel(cmcPortId,cmcId,cmcPortName,channelIndex,direction){
	if("US"== direction){
		showUpChannelInfo(cmcPortId, cmcId ,cmcPortName,channelIndex);
	}else{
		showDownChannelInfo(cmcPortId, cmcId ,cmcPortName,channelIndex);
	}
}

function showUpChannelInfo(portId,cmcId,cmcPortName,channelIndex) {
    window.parent.addView("channelPortal-"+cmcId + "-"+channelIndex, '@RESOURCES/COMMON.portHeader@['+cmcPortName+"]", "apListIcon", "cmc/showUpChannelPortal.tv?cmcPortId="+portId+"&channelIndex=" + channelIndex+"&cmcId="+cmcId+"&portName="+cmcPortName);
}

function showDownChannelInfo(portId,cmcId,cmcPortName,channelIndex) {
    window.parent.addView("channelPortal-"+cmcId + "-"+channelIndex, '@RESOURCES/COMMON.portHeader@['+cmcPortName+"]", "apListIcon", "cmc/showDownChannelPortal.tv?cmcPortId="+portId+"&channelIndex=" + channelIndex+"&cmcId="+cmcId+"&portName="+cmcPortName);
}
