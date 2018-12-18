var CmUtil = {};

var NO_DATA_FLAG = '--';

CmUtil.getStatusNameByValue = function(statusValue) {
	var name = NO_DATA_FLAG;
	switch(statusValue) {
	case 6: 
		name = 'online';
		break;
	case 21: 
		name = 'online(d)';
		break;
	case 26: 
		name = 'p-online';
		break;
	case 30: 
		name = 'p-online(d)';
		break;
	case 27: 
		name = 'w-online';
		break;
	case 31: 
		name = 'w-online(d)';
		break;
	case 1: 
		name = 'offline';
		break;
	default:
		name = 'registering';
	}
	return name;
}

CmUtil.statusValueRender = function(statusValue) {
	var name = NO_DATA_FLAG, color = 'blue';
	switch(statusValue) {
	case 6: 
		name = 'online';
		color = 'green';
		break;
	case 21: 
		name = 'online(d)';
		color = 'green';
		break;
	case 26: 
		name = 'p-online';
		color = 'green';
		break;
	case 30: 
		name = 'p-online(d)';
		color = 'green';
		break;
	case 27: 
		name = 'w-online';
		color = 'green';
		break;
	case 31: 
		name = 'w-online(d)';
		color = 'green';
		break;
	case 1: 
		name = 'offline';
		color = 'red';
		break;
	default:
		name = 'registering';
	}
	return String.format('<span class="block-span block-span-icon-left block-span-icon-left-{0} w40">{1}</span>', color, name);
}

function statusValueRenderExport(statusValue) {
	var name = '--';
	if(statusValue) {
		statusValue = statusValue.toString();
	}
	switch(statusValue) {
	case '6': 
		name = 'online';
		break;
	case '21': 
		name = 'online(d)';
		break;
	case '26': 
		name = 'p-online';
		break;
	case '30': 
		name = 'p-online(d)';
		break;
	case '27': 
		name = 'w-online';
		break;
	case '31': 
		name = 'w-online(d)';
		break;
	case '1': 
		name = 'offline';
		break;
	default:
		name = 'registering';
	}
	return name;
}

function isCmOnline(statusValue){
	return statusValue == '6' || statusValue == '21' || statusValue == '26' || statusValue == '27'|| statusValue == '30'|| statusValue == '31';
}

function renderIp(value, p, record) {
  if (value === null || value === "" || value === "noSuchObject" || value === "noSuchInstance" || value === "0.0.0.0" || value==='--') {
	  return NO_DATA_FLAG;
  } else if(!isCmOnline(record.data.statusValue)) {
	  return NO_DATA_FLAG;
  } else {
	var ccmtsWithAngent;
	//独立
	if(EntityType.isCcmtsWithAgentType(record.data.cmcDeviceStyle)){
		ccmtsWithAngent = true;
	}
	//通过判断supportWebProxy属性，看其是直接跳转还是webProxy跳转; true为webProxy跳转;
	if(record.data.supportWebProxy){
		var cmId = record.data.cmId;
		if(ccmtsWithAngent){ //独立型的，有代理功能;
			return String.format('<a href="javascript:;" onclick="openWebProxy(\'{0}\', \'{1}\')">{0}</a>', value, cmId);
		}else{ //非独立型的“提示不支持通过WebProxy打开CM管理页面”;
			return String.format('<a href="javascript:;" onclick="openWebProxyTip(\'{0}\')">{0}</a>', value)
		}
	}else{
		//cm单机Web的跳转
		return String.format('<a href="http://{0}" target="_blank">{0}</a>', value);
	}
  }
}

function renderIpExport(value, p, record) {
	if (value === null || value === "" || value === "noSuchObject" || value === "noSuchInstance" || value === "0.0.0.0" || value==='--') {
		return "--"
	} else {
		return value;
	}
}

function initCpeTemplate() {
	cpeTemplate = new Ext.XTemplate(
		 '<div style="margin:5px 5px 5px 10px;">',
            '<p class="flagP"><span class="flagInfo">@cm.cpeInfo@</span></p>',
            '<table width="96%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" class="dataTable extGridPanelTable addOneTr" rules="none">',
                '<thead>',
                    '<tr>',
                    '<th align="center">IP</th>',
                    '<th align="center">MAC</th>',
                    '<th align="center">@CMC.text.type@</th>',
                    '<th align="center">@CM.operate@</th>',
                    '</tr>',
                '</thead>',
                '<tbody id="tbody-append-child">',
                '<tpl for="cpes">',
	                '<tr>',
	                    '<td align="center">{topCmCpeIpAddress}</td>',
	                    '<td align="center">{topCmCpeMacAddressString}</td>',
	                    '<td align="center">{topCmCpeTypeString}</td>',
	                    '<td align="center">',
	                    	'<a class="yellowLink" href="javascript:;" onclick="pingCpe({topCmCpeIpAddress})">Ping</a> / ',
	                    	'<a class="yellowLink" href="javascript:;" onclick="traceRouteCpe({topCmCpeIpAddress})">Ping</a>',
	                    '</td>',
	                '</tr>',
                '</tpl>',
                '</tbody>',
            '</table>',
        '</div>'
    );
	cpeTemplate.compile();
}

function renderMac(value, p, record) {
	if (value != "") {
		return String.format('<a href="javascript:;" onclick="showCmDetail(\'{0}\')">{0}</a>', value);
	} else {
		return value;
	}
}

function renderCpeNum(value, p, record) {
	//判断应该屏蔽哪些菜单
	var cmcDeviceStyle = record.data.cmcDeviceStyle;
	//如果是CMTS，则修改相关为CMTS并屏蔽CPE相关
	if (EntityType.isCmtsType(cmcDeviceStyle)) {
	    p.attr = 'ext:qtip="@CPE.CMTSNOTSUPPORT@"';  
		return "--"
	} else {
	    p.attr = 'ext:qtip="'+value+'"';  
		return value;
	}
}

function cmStatusRender(value) {
	if (isCmOnline(value)) {
		//在线
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
			"@COMMON.online@");
	} else if (value == '1') {
		//离线
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
			"@COMMON.offline@");
	} else {
		//上线中
		return '@CM.onling@';
	}
}

function renderDocsisMode(value, p, record) {
	if(value) {
		value = value.toString();
	}
	switch (value){
	case '-1':
		return '@CM.noValue@';
	case '-2':
		return '@CM.cmtsNotSupport@';
	case '1':
		return '1.0';
	case '2':
		return '1.1';
	case '3':
		return '2.0';
	case '4':
		return '3.0';
	default:
		return '@CM.unknownVersion@';
	}
}

/**
 * CM上行信道render函数
 * @param value
 * @param p
 * @param record
 */
function renderUpChannel(value, p, record) {
  var cmcDeviceStyle = record.data.cmcDeviceStyle;
  
  if(!isCmOnline(record.data.statusValue)){
	  return NO_DATA_FLAG;
  }

  if (EntityType.isCmtsType(cmcDeviceStyle)) {
	  return String.format('<span class="cirBoxBg">{0}</span>', record.data.upChannelIndexString);
  }
  
  if(record.data.docsisMode == 4){
	  //3.0CM
	  if(chlDisplayMode=='all' && record.data.upChannelCm3Signal){
		  var chlIds = [];
		  //展示所有信道
		  $.each(record.data.upChannelCm3Signal, function(i, cm3Signal){
			  if(cm3Signal.channelId == record.data.upChannelId){
				  //主上行信道
				  chlIds.push(String.format('<span class="cirBoxBg mainChl">{0}</span>', cm3Signal.channelId));
			  }else{
				  chlIds.push(String.format('<span class="cirBoxBg">{0}</span>', cm3Signal.channelId));
			  }
		  });
		  return chlIds.join(' ');
	  }else{
		  //展示主信道
		  return String.format('<span class="cirBoxBg">{0}</span>', record.data.upChannelId);
	  }
  }else{
	  return String.format('<span class="cirBoxBg">{0}</span>', record.data.upChannelId);
  }
}

function renderUpChannelExport(value, p, record) {
	var cmcDeviceStyle = record.data.cmcDeviceStyle;
	
	if(!isCmOnline(record.data.statusValue)) {
		return '--';
	}
	
	if (40000 <= cmcDeviceStyle && cmcDeviceStyle < 50000) {
		return record.data.upChannelIndexString;
	}
	
	if(record.data.docsisMode == '4'){
		//3.0CM
		if(record.data.upChannelCm3Signal){
			var chlIds = [];
			//展示所有信道
			for(var i=0, len=record.data.upChannelCm3Signal.length; i<len; i++) {
				chlIds.push(record.data.upChannelCm3Signal[i].channelId);
			}
			return chlIds.join(',');
		}else{
			//展示主信道
			return record.data.upChannelId;
		}
	}else{
		return record.data.upChannelId;
	}
	
	
	function isCmOnline(statusValue){
		return statusValue == '6' || statusValue == '21' || statusValue == '26' || statusValue == '27'|| statusValue == '30' || statusValue == '31';
	}
}

/**
 * CM下行信道render函数
 * @param value
 * @param p
 * @param record
 */
function renderDownChannel(value, p, record) {
  var cmcDeviceStyle = record.data.cmcDeviceStyle;
  
  if(!isCmOnline(record.data.statusValue)){
	  return NO_DATA_FLAG;
  }

  if(EntityType.isCmtsType(cmcDeviceStyle)){
	  return String.format('<span class="cirBoxBg">{0}</span>', record.data.downChannelIndexString);
  }

  if(record.data.docsisMode == 4){
	  //3.0CM
	  if(chlDisplayMode=='all' && record.data.downChannelCm3Signal){
		  var chlIds = [];
		  //展示所有信道
		  $.each(record.data.downChannelCm3Signal, function(i, cm3Signal){
			  if(cm3Signal.channelId == record.data.downChannelId){
				  //主上行信道
				  chlIds.push(String.format('<span class="cirBoxBg mainChl">{0}</span>', cm3Signal.channelId));
			  }else{
				  chlIds.push(String.format('<span class="cirBoxBg">{0}</span>', cm3Signal.channelId));
			  }
		  });
		  return chlIds.join(' ');
	  }else{
		  //展示主信道
		  return String.format('<span class="cirBoxBg">{0}</span>', record.data.downChannelId);
	  }
  }else{
	  return String.format('<span class="cirBoxBg">{0}</span>', record.data.downChannelId);
  }
}

function renderDownChannelExport(value, p, record) {
	var cmcDeviceStyle = record.data.cmcDeviceStyle;
	
	if(!isCmOnline(record.data.statusValue)) {
		return '--';
	}
	
	if (40000 <= cmcDeviceStyle && cmcDeviceStyle < 50000) {
		return record.data.downChannelIndexString;
	}
	
	if(record.data.docsisMode == '4'){
		//3.0CM
		if(record.data.downChannelCm3Signal){
			var chlIds = [];
			//展示所有信道
			for(var i=0, len=record.data.downChannelCm3Signal.length; i<len; i++) {
				chlIds.push(record.data.downChannelCm3Signal[i].channelId);
			}
			return chlIds.join(',');
		}else{
			//展示主信道
			return record.data.downChannelId;
		}
	}else{
		return record.data.downChannelId;
	}
	
	
	function isCmOnline(statusValue){
		return statusValue == '6' || statusValue == '21' || statusValue == '26' || statusValue == '27'|| statusValue == '30' || statusValue == '31';
	}
}

/**
 * CM上行信道SNR render函数
 * @param value
 * @param p
 * @param record
 */
function renderUpChannelSnr(value, p, record) {
	if(!isCmOnline(record.data.statusValue)){
		return NO_DATA_FLAG;
	}
	
	if(!record.data.upChannelCm3Signal || !record.data.upChannelCm3Signal.length || !record.data.upChannelCm3Signal[0].upChannelSnr) {
		//没有数据
		return '--';
	}

    //3.0CM
    var snrs = [];
    if(chlDisplayMode=='all'){
        //展示所有信道
        $.each(record.data.upChannelCm3Signal, function(i, cm3Signal){
            if(cm3Signal.channelId == record.data.upChannelId){
                snrs.push(String.format('<span class="cirBoxBg mainChl">{0}({1})</span>', cm3Signal.channelId, cm3Signal.upChannelSnr));
            }else{
                snrs.push(String.format('<span class="cirBoxBg">{0}({1})</span>', cm3Signal.channelId, cm3Signal.upChannelSnr));
            }
        });
    }else{
        //展示主信道
        $.each(record.data.upChannelCm3Signal, function(i, cm3Signal){
            if(cm3Signal.channelId == record.data.upChannelId){
                snrs.push(cm3Signal.upChannelSnr);
            }
        });
    }
    return '<div class="docsis3" data-type="upChannelSnr" data-cmid='+ record.data.cmId +' style="margin-left:5px;">' + snrs.join(' ') + '</div>';
	
}

function renderUpChannelSnrExport(value, p, record) {
	if(!isCmOnline(record.data.statusValue)){
		return '--';
	}
	
	if(!record.data.upChannelCm3Signal || !record.data.upChannelCm3Signal.length || !record.data.upChannelCm3Signal[0].upChannelSnr) {
		//没有数据
		return '--';
	}
	
	if(record.data.docsisMode == 4){
		//3.0CM
		var snrs = [];
		//展示所有信道
		for(var i=0, len=record.data.upChannelCm3Signal.length; i<len; i++) {
			snrs.push(record.data.upChannelCm3Signal[i].channelId + '(' + record.data.upChannelCm3Signal[i].upChannelSnr + ')');
		}
		
		return snrs.join(', ');
	}else{
		//2.0
		return record.data.upChannelCm3Signal[0].upChannelSnr;
	}
	
	function isCmOnline(statusValue){
		return statusValue == '6' || statusValue == '21' || statusValue == '26' || statusValue == '27'|| statusValue == '30' || statusValue == '31';
	}
}

/**
 * CM下行信道SNR render函数
 * @param value
 * @param p
 * @param record
 */
function renderDownChannelSnr(value, p, record) {
	if(!isCmOnline(record.data.statusValue)){
		return NO_DATA_FLAG;
	}
	
	if(!record.data.downChannelCm3Signal || !record.data.downChannelCm3Signal.length || !record.data.downChannelCm3Signal[0].downChannelSnr){
		//没有数据
		return '--';
	}

    //3.0CM
    var snrs = [];
    if(chlDisplayMode=='all'){
        //展示所有信道
        $.each(record.data.downChannelCm3Signal, function(i, cm3Signal){
            if(cm3Signal.channelId == record.data.downChannelId){
                snrs.push(String.format('<span class="cirBoxBg mainChl">{0}({1})</span>', cm3Signal.channelId, cm3Signal.downChannelSnr));
            }else{
                snrs.push(String.format('<span class="cirBoxBg">{0}({1})</span>', cm3Signal.channelId, cm3Signal.downChannelSnr));
            }
        });
    }else{
        //展示主信道
        $.each(record.data.downChannelCm3Signal, function(i, cm3Signal){
            if(cm3Signal.channelId == record.data.downChannelId){
                snrs.push(cm3Signal.downChannelSnr);
            }
        });
    }
    return '<div class="docsis3" data-type="downChannelSnr" data-cmid='+ record.data.cmId +' style="margin-left:5px;">' + snrs.join(' ') + '</div>';

}

function renderDownChannelSnrExport(value, p, record) {
	if(!isCmOnline(record.data.statusValue)){
		return '--';
	}
	
	if(!record.data.downChannelCm3Signal || !record.data.downChannelCm3Signal.length || !record.data.downChannelCm3Signal[0].downChannelSnr) {
		//没有数据
		return '--';
	}
	
	if(record.data.docsisMode == 4){
		//3.0CM
		var snrs = [];
		//展示所有信道
		for(var i=0, len=record.data.downChannelCm3Signal.length; i<len; i++) {
			snrs.push(record.data.downChannelCm3Signal[i].channelId + '(' + record.data.downChannelCm3Signal[i].downChannelSnr + ')');
		}
		
		return snrs.join(', ');
	}else{
		//2.0
		return record.data.downChannelCm3Signal[0].downChannelSnr;
	}
	
	function isCmOnline(statusValue){
		return statusValue == '6' || statusValue == '21' || statusValue == '26' || statusValue == '27'|| statusValue == '30' || statusValue == '31';
	}
}

/**
 * CM上行信道电平render函数
 * @param value
 * @param p
 * @param record
 */
function renderUpChannelTx(value, p, record) {
	if(!isCmOnline(record.data.statusValue)){
		return NO_DATA_FLAG;
	}
	
	if(!record.data.upChannelCm3Signal || !record.data.upChannelCm3Signal.length || !record.data.upChannelCm3Signal[0].upChannelTx){
		//没有数据
		return '--';
	}

    //3.0CM
    var txs = [];
    if(chlDisplayMode=='all'){
        //展示所有信道
        $.each(record.data.upChannelCm3Signal, function(i, cm3Signal){
            if (sysElecUnit == '@unit.dbuv@') {
                cm3Signal.upChannelTx = parseFloat(cm3Signal.upChannelTx) + 60;
            }
            if(cm3Signal.channelId == record.data.upChannelId){
                txs.push(String.format('<span class="cirBoxBg mainChl">{0}({1})</span>', cm3Signal.channelId, cm3Signal.upChannelTx));
            }else{
                txs.push(String.format('<span class="cirBoxBg">{0}({1})</span>', cm3Signal.channelId, cm3Signal.upChannelTx));
            }
        });
    }else{
        //展示主信道
        $.each(record.data.upChannelCm3Signal, function(i, cm3Signal){
            if(cm3Signal.channelId == record.data.upChannelId){
                if (sysElecUnit == '@unit.dbuv@') {
                    cm3Signal.upChannelTx = parseFloat(cm3Signal.upChannelTx) + 60;
                }
                txs.push(cm3Signal.upChannelTx);
            }
        });
    }
    return '<div class="docsis3" data-type="upChannelTx" data-cmid='+ record.data.cmId +' style="margin-left:5px;">' + txs.join(' ') + '</div>';

}

function renderUpChannelTxExport(value, p, record) {
	if(!isCmOnline(record.data.statusValue)){
		return '--';
	}
	
	if(!record.data.upChannelCm3Signal || !record.data.upChannelCm3Signal.length || !record.data.upChannelCm3Signal[0].upChannelTx) {
		//没有数据
		return '--';
	}
	
	if(record.data.docsisMode == 4){
		//3.0CM
		var snrs = [];
		//展示所有信道
		for(var i=0, len=record.data.upChannelCm3Signal.length; i<len; i++) {
			if (sysElecUnit == '@unit.dbuv@') {
				snrs.push(record.data.upChannelCm3Signal[i].channelId + '(' + parseFloat(record.data.upChannelCm3Signal[i].upChannelTx)+60 + ')');
			}else {
				snrs.push(record.data.upChannelCm3Signal[i].channelId + '(' + record.data.upChannelCm3Signal[i].upChannelTx + ')');
			}
			
		}
		
		return snrs.join(', ');
	}else{
		//2.0
		if (sysElecUnit == '@unit.dbuv@') {
			return parseFloat(record.data.upChannelCm3Signal[0].upChannelTx)+60;
		}else {
			return record.data.upChannelCm3Signal[0].upChannelTx;
		}
	}
	
	function isCmOnline(statusValue){
		return statusValue == '6' || statusValue == '21' || statusValue == '26' || statusValue == '27'|| statusValue == '30' || statusValue == '31';
	}
}

/**
 * CM下行信道电平render函数
 * @param value
 * @param p
 * @param record
 */
function renderDownChannelTx(value, p, record) {
	if(!isCmOnline(record.data.statusValue)){
		return NO_DATA_FLAG;
	}
	
	if(!record.data.downChannelCm3Signal || !record.data.downChannelCm3Signal.length || !record.data.downChannelCm3Signal[0].downChannelTx){
		//没有数据
		return '--';
	}

    //3.0CM
    var txs = [];
    if(chlDisplayMode=='all'){
        //展示所有信道
        $.each(record.data.downChannelCm3Signal, function(i, cm3Signal){
            if (sysElecUnit == '@unit.dbuv@') {
                cm3Signal.downChannelTx = parseFloat(cm3Signal.downChannelTx) + 60;
            }
            if(cm3Signal.channelId == record.data.downChannelId){
                txs.push(String.format('<span class="cirBoxBg mainChl">{0}({1})</span>', cm3Signal.channelId, cm3Signal.downChannelTx));
            }else{
                txs.push(String.format('<span class="cirBoxBg">{0}({1})</span>', cm3Signal.channelId, cm3Signal.downChannelTx));
            }
        });
    }else{
        //展示主信道
        $.each(record.data.downChannelCm3Signal, function(i, cm3Signal){
            if(cm3Signal.channelId == record.data.downChannelId){
                if (sysElecUnit == '@unit.dbuv@') {
                    cm3Signal.downChannelTx = parseFloat(cm3Signal.downChannelTx) + 60;
                }
                txs.push(cm3Signal.downChannelTx);
            }
        });
    }
    return '<div class="docsis3" data-type="downChannelTx" data-cmid='+ record.data.cmId +' style="margin-left:5px;">' + txs.join(' ') + '</div>';

}

function renderDownChannelTxExport(value, p, record) {
	if(!isCmOnline(record.data.statusValue)){
		return '--';
	}
	
	if(!record.data.downChannelCm3Signal || !record.data.downChannelCm3Signal.length || !record.data.downChannelCm3Signal[0].downChannelTx) {
		//没有数据
		return '--';
	}
	
	if(record.data.docsisMode == 4){
		//3.0CM
		var snrs = [];
		//展示所有信道
		for(var i=0, len=record.data.downChannelCm3Signal.length; i<len; i++) {
			if (sysElecUnit == '@unit.dbuv@') {
				snrs.push(record.data.downChannelCm3Signal[i].channelId + '(' + parseFloat(record.data.downChannelCm3Signal[i].downChannelTx)+60 + ')');
			}else{
				snrs.push(record.data.downChannelCm3Signal[i].channelId + '(' + record.data.downChannelCm3Signal[i].downChannelTx + ')');
			}
			
		}
		
		return snrs.join(', ');
	}else{
		//2.0
		if (sysElecUnit == '@unit.dbuv@') {
			return parseFloat(record.data.downChannelCm3Signal[0].downChannelTx)+60;
		}else {
			return record.data.downChannelCm3Signal[0].downChannelTx;
		}
	}
	
	function isCmOnline(statusValue){
		return statusValue == '6' || statusValue == '21' || statusValue == '26' || statusValue == '27'|| statusValue == '30' || statusValue == '31';
	}
}

function renderPreviousState(value, p, record) {
  if(record.data.statusValue == '1'){
	  value = value.toString();
	switch (value){
	case '-1':
		return '@CM.noValue@';
	case '-2':
		return '@CM.cmtsNotSupport@';
	case '1':
		return 'other';
	case '2':
		return 'ranging';
	case '3':
		return 'rangingAborted';
	case '4':
		return 'rangingComplete';
	case '5':
		return 'ipComplete';
	case '6':
		return 'registrationComplete';
	case '7':
		return 'accessDenied';
	case '8':
		return 'operational';
	case '9':
		return 'registeredBPIInitializing';
	case '10':
		return 'DHCPv4Discover';
	case '11':
		return 'DHCPv4Offer';
	case '12':
		return 'DHCPv4Request';
	case '21':
		return 'ForwardingDisabled';
	case '22':
		return 'DHCPv6Solicit';
	case '23':
		return 'DHCPv6Advertise';
	case '24':
		return 'DHCPv6Request';
    case '25':
        return 'DHCPv6Reply';
    case '26':
        return 'p-online';
    case '27':
        return 'w-online';
    case '30':
        return 'p-online(d)';
    case '31':
        return 'w-online(d)';
	default:
		return '@CM.unknownState@';
		}
  }else{
	return '-';
  }
}

function renderFlapChart(value,p,record){
	var cmMac = record.data.statusMacAddress;
	return '<a href="#" class="my-link" onclick=\'showCMFlapChart("' + cmMac + '");\'> ' + value + ' </a>';
    
}
function showCMFlapChart(cmMac){
	window.top.createDialog('cmFlapDemo', I18N.cmc.flap.CMflapHistory, 800, 600, '/cmflap/showOneCMFlapHisChart.tv?cmMac='+cmMac , null, true, true);
}

function switchChannelViewMode(radioGroup, checkedRadio){
	chlDisplayMode = checkedRadio.inputValue;
	//重新renderer
	for (var i = 0; i < store.getCount(); i++) {
		var record = store.getAt(i);
		if(record.data.docsisMode == 4){
			record.beginEdit();
			record.commit();
			record.endEdit();
		}
	}
}

/**
 * 刷新实时信号质量
 * @param operationId 本次刷新操作唯一标识
 * @param store 需要更新的store
 * @param selectAll 是否刷新整页数据
 */
function refreshCmSignalQuality(operationId, store, selectAll) {
	// 区分cc下CM和CMTS下CM，方便在remotequery时操作。这样，最多只会发两次刷新http请求
	var ccmtsCmIds = [];
	var cmtsCmIds = [];
	
	if(selectAll) {
		//刷新全部
		if (store.getCount() == 0) {
			//没有数据，无需刷新
		    return;
		}
		for (var i = 0, len=store.getCount(); i < len; i++) {
			json = store.getAt(i).json;
			cmId = json.cmId;
			cmcDeviceStyle = json.cmcDeviceStyle;
		    if (EntityType.isCmtsType(cmcDeviceStyle)) {
		      cmtsCmIds.push(cmId);
		    } else {
		      ccmtsCmIds.push(cmId);
		    }
		}
	} else {
		//刷新选中
		var sm = grid.getSelectionModel();
	    if (sm == null || !sm.hasSelection()) {
	    	return window.top.showMessageDlg("@COMMON.tip@", "@text.pleaseSelectCm@");
	    }
	    var selections = sm.getSelections();
	    for (var i = 0, len=selections.length; i < len; i++) {
	    	if (EntityType.isCmtsType(selections[i].data.cmcDeviceStyle)) {
		      cmtsCmIds.push(selections[i].data.cmId);
		    } else {
		      ccmtsCmIds.push(selections[i].data.cmId);
		    }
	    }
	}
	
	//批量初始化
	initBatch();
	var successNum = 0,
	    failedNum = 0,
	    finishedCmIndex = [],
	    refreshedCmCounter=0;
	
	//dwr接收处理函数
	window.top.addCallback(operationId, function(response) {
		//重置超时限制
		var result = response.result,
			cmAttribute = response.cmAttribute;
		clearTimeout(timeoutTimer);
		timeoutTimer = setTimeout(function(){
	    	unexpectedEnd();
	    }, 300000);
	    var cmId = cmAttribute.cmId,
	      	statusMacAddress = cmAttribute.statusMacAddress,
	      	downChannelSnr = cmAttribute.downChannelSnr,
	      	downChannelTx = cmAttribute.downChannelTx,
	      	upChannelTx = cmAttribute.upChannelTx,
	      	lastRefreshTime = cmAttribute.lastRefreshTime,
	      	topCmFlapInsertionFailNum = cmAttribute.topCmFlapInsertionFailNum,
	    	downChannelRecvPower = cmAttribute.downChannelRecvPower,
	    	upChannelTransPower = cmAttribute.upChannelTransPower;
	    for (var j = 0, len=store.getCount(); j < len; j++) {
	    	//遍历找到对应的行
	    	if (store.getAt(j).get('cmId') == cmId) {
	    		finishedCmIndex.push(j);
	    		var curRecord = store.getAt(j);
	    		curRecord.beginEdit();
	    		store.getAt(j).set('docsIfCmtsCmStatusSignalNoiseString', cmAttribute.docsIfCmtsCmStatusSignalNoiseString);
	    		store.getAt(j).set('downChannelSnr', downChannelSnr);
	    		store.getAt(j).set('downChannelRecvPower', cmAttribute.downChannelRecvPower);
	    		store.getAt(j).set('upChannelTransPower', cmAttribute.upChannelTransPower);
	    		store.getAt(j).set('lastRefreshTime', lastRefreshTime);
	    		store.getAt(j).set('cmSignal', cmAttribute.cmSignal);
	    		store.getAt(j).set('upChannelCm3Signal', cmAttribute.upChannelCm3Signal);
	    		store.getAt(j).set('downChannelCm3Signal', cmAttribute.downChannelCm3Signal);
	    		store.getAt(j).commit();
	    		store.getAt(j).endEdit();
	    		if (isCmOnline(cmAttribute.statusValue) && result) {
		        	grid.getView().removeRowClass(j, 'yellow-row');
		        	successNum++;
		        	$('#suc-num-dd').text(successNum);
		        } else {
		        	grid.getView().addRowClass(j, 'red-row');
		        	failedNum++;
		        	$('#fail-num-dd').text(failedNum);
		        }
	    	}
	    }
	    refreshedCmCounter++;
	    if (refreshedCmCounter >= cmtsCmIds.length + ccmtsCmIds.length) {
	    	clearTimeout(timeoutTimer);
	    	outputResult();
	    }
	});
	
	//下发刷新请求
	if(ccmtsCmIds.length){
		$.post('/cmlist/refreshCmSignal.tv',{
			cmIds: ccmtsCmIds.join(','),
			jconnectedId: WIN.top.GLOBAL_SOCKET_CONNECT_ID,
			upperEntityType: 30000,
			operationId: operationId
	    });
	}
	if(cmtsCmIds.length){
	    $.post('/cmlist/refreshCmSignal.tv',{
	    	cmIds: cmtsCmIds.join(','),
	    	jconnectedId: WIN.top.GLOBAL_SOCKET_CONNECT_ID,
	    	upperEntityType: 40000,
			operationId: operationId
	    });
	}
	//发送完请求后开启计时器，如果超时未返回，则认为本次批量结束，未返回的均统计为失败
    timeoutTimer = setTimeout(function(){
    	unexpectedEnd();
    }, 300000);
	
	//批量初始化
    function initBatch(){
  	  //初始化统计数据
  	  $('#suc-num-dd').text(0);
  	  $('#fail-num-dd').text(0);
  	  //禁用查询功能、刷新功能、翻页功能
  	  tbar.setDisabled(true);
  	  bbar.setDisabled(true);
  	  $('#simple-query').attr('disabled', 'disabled');
  	  $('#advance-query').attr('disabled', 'disabled');
  	  //展示等待框
  	  showLoading("@ccm/CCMTS.CmList.refreshingSignal@");
  	  
  	  //将各行标记为正在刷新
  	  for (var j = 0; j < store.getCount(); j++) {
	      if($.inArray(store.getAt(j).get('cmId'), ccmtsCmIds)!=-1 
	    		  || $.inArray(store.getAt(j).get('cmId'), cmtsCmIds)!=-1){
		      //grid.getView().removeRowClass('red-row').removeRowClass('white-row').addRowClass(j, 'yellow-row');
	  		  grid.getView().removeRowClass(j, 'red-row');
	  	      grid.getView().removeRowClass(j, 'white-row');
	  	      grid.getView().addRowClass(j, 'yellow-row')
	  	  }
	  }
    }
    
    //批量操作意外中止处理函数
    function unexpectedEnd(){
    	clearTimeout(timeoutTimer);
    	//取消dwr推送接收
    	window.top.removeCallback(operationId);
    	//将正在处理行全部置为失败
    	for (var i = 0, len=store.getCount(); i < len; i++) {
    		if(!contains(finishedCmIndex, i)){
    			grid.getView().addRowClass(i, 'red-row');
    		}
    	}
    	//刷新成功数为successNum，failedNum更新为len-failedNum
    	failedNum = len - successNum;
    	//展示结果
    	outputResult();
    }
    
    function outputResult(){
    	//启用查询功能、刷新功能、翻页功能
    	tbar.setDisabled(false);
    	bbar.setDisabled(false);
    	$('#simple-query').removeAttr('disabled');
    	$('#advance-query').removeAttr('disabled');
    	//隐藏等待框
    	hideLoading();
    	//显示结果
    	$('#suc-num-dd').text(successNum);
    	$('#fail-num-dd').text(failedNum);
    	top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@ccm/CCMTS.CmList.refreshingSignalComplete@<br/>@CMC.tip.successfully@' + successNum + '@CCMTS.view.cmNumOffline.unit@,@CMC.label.failure@' + failedNum + '@CCMTS.view.cmNumOffline.unit@</b>'
    	});
    }
    
    //判断数组中是否包含指定元素
    function contains(array, obj){
		var i = array.length;
		while(i--){
			if(array[i] == obj){
				return true;
			}
		}
		return false;
	}
	
}

function randomInteger(min, max) {
	var r = Math.random() * (max - min);
    var re = Math.round(r + min);
    re = Math.max(Math.min(re, max), min)
    return re;
}

//用webProxy的方式打开单机web;
function openWebProxy(ip, cmId){
	var src = String.format('/webproxy/showCmWebProxy.tv?cmId={0}', cmId)
	top.addView('webProxy'+ip, ip, 'icoB6', src,null,true);
}

function openWebProxyTip(ip){
	top.showConfirmDlg('@COMMON.tip@','@cmc/webProxy.openTip@', function(type){
	   if(type == 'yes'){
		   var src = 'http://' + ip;
		   window.open(src);
	   }
	});
}























