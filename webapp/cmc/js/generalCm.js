var CM = {};

/**
 * 功能函数区域
 */
//展示CM CPE综合查询页面
CM.showCmDetail = function(cmMac){
	window.parent.addView("CmCpeQuery", '@ccm/CCMTS.CmList.cmCpeQuery@', "mydesktopIcon", "/cmCpe/showCmCpeQuery.tv?sourcePage=cmListPage&cmMac=" + cmMac, null, true);
}

//展示FLAP趋势图
CM.showCMFlapChart = function(cmMac){
	window.top.createDialog('cmFlap', '@flapHis@', 800, 600, '/cmflap/showOneCMFlapHisChart.tv?cmMac=' + cmMac, null, true, true);
}

//刷新单个CM
CM.refreshCmInfo = function(cmcId, cmId, cmMac, statusIndex) {
	window.top.showWaitingDlg('@COMMON.waiting@', '@CMC.ipqam.doingRefreshDevice@', 'ext-mb-waiting');
	$.post('/cmlist/refreshCm.tv', {
		cmMac: cmMac,
		cmId: cmId,
		cmcId: cmcId,
		statusIndex: statusIndex
	}, function(json){
		if (json && json.success) {
			window.top.closeWaitingDlg();
	        top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: '<b class="orangeTxt">@CMC.tip.refreshSuccess@</b>'
	        });
	        //更新该行数据
	        var cmAttribute = json.cmAttribute;
	        record.beginEdit();
	        for (var key in cmAttribute) {
	        	//非版本控制内容则更新
	        	if($.inArray(key, ['supportStaticIp', 'supportCpeInfo', 'supportReset']) ===-1){
	        		record.set(key, cmAttribute[key]);
	        	}
	        }
	        record.commit();
	        //刷新定位图
	        refreshCmLocation();
	        //更新CM数量信息
	        updateCmNum();
	        var cls = expander.state[record.id] ? 'x-grid3-row-expanded' : 'x-grid3-row-collapsed';
	        if (cls == 'x-grid3-row-expanded') {
	            expander.expandRow(rowIndex);
	        }
		} else {
			window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.refreshFailure@');
	  	}
	});
}

//通用的renderer函数配置
Ext.apply(CM, {
	renderIp: function(value, p, record){
		if (value === null || value === "" || value === "noSuchObject" || value === "noSuchInstance" || value === "0.0.0.0" || value==='--') {
			return "--"
		} else if(record.data.statusValue != '6') {
			return value;
		} else {
			return String.format('<a href="http://{0}" target="_blank">{0}</a>', value);
		}
	},
	renderMac: function (value, p, record){
		return !value ? value : String.format('<a href="javascript:;" onclick="CM.showCmDetail(\'{0}\')">{0}</a>', value);
	},
	cmStatusRender: function(value, p, record){
		var tmp = '<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/{1}.gif" border=0 align=absmiddle>';
		return value == '6' ? String.format(tmp, "@CCMTS.status.4@", 'admin') : String.format(tmp, "@CM.offline@", 'noadmin');
	},
	renderUpChannel: function(value, p, record){
		var chlId = EntityType.isCmtsType(record.data.cmcDeviceStyle) ? record.data.upChannelIndexString : value;
		return String.format('<span class="cirBoxBg">{0}</span>', chlId);
	},
	renderDownChannel: function(value, p, record){
		var chlId = EntityType.isCmtsType(record.data.cmcDeviceStyle) ? record.data.downChannelIndexString : value;
		return String.format('<span class="cirBoxBg">{0}</span>', chlId);
	},
	renderDownChannelSnr: function(value, p, record){
		return value ? value + "dB" : '--';
	},
	renderUpChannelTx: function(value, p, record){
		return value ? record.data.upChannelTransPower + "@{unitConfigConstant.elecLevelUnit}@" : '--';
	},
	renderDownChannelTx: function(value, p, record){
		return value ? record.data.downChannelRecvPower + "@{unitConfigConstant.elecLevelUnit}@" : '--';
	},
	renderFlapChart: function(value, p, record){
		var cmMac = record.data.statusMacAddress;
		  //nm3kTip=该数值为最后一次重启后的数字
		  return '<a href="#" class=my-link onclick=\'CM.showCMFlapChart("' + cmMac + '");\'> ' + value + ' </a>';
	},
	renderTime: function(value, p, record){
		if(value==null){return '-';}
		var date = new Date();
		date.setTime(value.time);
		return Ext.util.Format.date(date, 'Y-m-d H:i:s');
	},
	renderOperation: function(value, p, record){
		var	data = record.data,
			statusMacAddress = data.statusMacAddress,
		    cmIp = data.statusInetAddress,
		    cmId = data.cmId,
		    cmcId = data.cmcId,
		    status = data.statusValue,
		    statusIndex = record.data.statusIndex,
		    returnStr = "",
		    refreshCmFormatStr = "<a class='yellowLink' href='javascript:;' onclick='refreshCmInfo(\"{0}\",\"{1}\",\"{2}\",\"{3}\")' >@route.button.refresh@</a>",
		    restartCmFormatStr = "<a class='yellowLink' href='javascript:;' onclick='restarCm(\"{0}\",\"{1}\",\"{2}\",\"{3}\")'/>@CCMTS.cm.reset@</a>",
		    otherOperFormatStr = "<a class='withSub mR10' href='javascript:;' onclick='showMoreOperation({0},event)'>@TIP.other@</a>";
		//加上刷新操作
		returnStr += String.format(refreshCmFormatStr, cmcId, cmId, statusMacAddress, statusIndex) + " / ";
		//如果在线并且有权限，就加上重启操作
		if(status == 6 && operationDevicePower && record.data.supportReset==true){
			returnStr += String.format(restartCmFormatStr, statusMacAddress, cmIp, cmId, status) + " / ";
		}
		//加上其他操作
		returnStr += String.format(otherOperFormatStr, cmId);
	    return returnStr;
	}
});

//通用的默认列配置
CM.defaultColumns = [
   {header: "<div style='text-align:center'>IP</div>", width: 100, sortable: true, align: 'left', dataIndex: 'statusIpAddress', renderer: CM.renderIp}, 
   {header: "MAC", width: 150, sortable: true, align: 'center', dataIndex: 'statusMacAddress', renderer: CM.renderMac}, 
   {header: "@CMC.title.status@", width: 50, sortable: true, align: 'center', dataIndex: 'statusValue', renderer: CM.cmStatusRender}, 
   {header: "<div style='text-align:center'>@CMCPE.upDevice@</div>", width: 90, sortable: true, align: 'left', dataIndex: 'name'}, 
   {header: "@cmcUserNum.lastCollectTime@", width: 127, sortable: true, align: 'center', dataIndex: 'lastRefreshTime'}, 
   {header: "@upchannel@", width: 62, sortable: true, align: 'center', dataIndex: 'upChannelId', renderer: CM.renderUpChannel}, 
   {header: "@CCMTS.downStreamChannel@", width: 62, sortable: true, align: 'center', dataIndex: 'downChannelId',renderer: CM.renderDownChannel}, 
   {header: "@CM.upSnr@", width: 62, sortable: true, align: 'center', dataIndex: 'docsIfCmtsCmStatusSignalNoiseString'}, 
   {header: "@CM.downSnr@", width: 62, sortable: true, align: 'center', dataIndex: 'downChannelSnr', renderer: CM.renderDownChannelSnr}, 
   {header: "@CM.upSendPower@", width: 100, sortable: true, align: 'center', dataIndex: 'upChannelTx', renderer: CM.renderUpChannelTx}, 
   {header: "@CM.downReceivePower@", width: 100, sortable: true, align: 'center', dataIndex: 'downChannelTx', renderer: CM.renderDownChannelTx}, 
   {header: 'FLAP Ins', width: 100, sortable: true, align: 'center', renderer: renderFlapChart, dataIndex: 'topCmFlapInsertionFailNum'}, 
   {header: '@CMC.title.Alias@', width: 100, sortable: true, align: 'center', dataIndex: 'cmAlias'}, 
   {header: '@CMC.title.usage@', width: 100, sortable: true, align: 'center', dataIndex: 'cmClassified'}, 
   {header: '@CM.userNo@', width: 100, sortable: true, hidden:true, align: 'center', dataIndex: 'userId'}, 
   {header: '@CM.userName@', width: 100, sortable: true, hidden:true, align: 'center', dataIndex: 'userName'}, 
   {header: '@CM.userAddr@', width: 100, sortable: true, hidden:true, align: 'center', dataIndex: 'userAddr'}, 
   {header: '@CM.userPhone@', width: 100, sortable: true, hidden:true, align: 'center', dataIndex: 'userPhoneNo'}, 
   {header: '@CM.packageType@', width: 100, sortable: true, hidden:true, align: 'center', dataIndex: 'packageType'}, 
   {header: '@CM.effectTime@', width: 140, sortable: true, hidden:true, align: 'center', dataIndex: 'effectTime', renderer: CM.renderTime}, 
   {header: '@CM.expirationTime@', width: 140, sortable: true, hidden:true, align: 'center', dataIndex: 'expirationTime', renderer: CM.renderTime}, 
   {header: '@CM.configFile@', width: 100, sortable: true, hidden:true, align: 'center', dataIndex: 'configFile'},
   {header: '@CM.extension@', width: 100, sortable: true, hidden:true, align: 'center', dataIndex: 'extension'},
   {header: "<div class='txtCenter'>@VLAN.vlanOpera@</div>", width: 165, fixed: true, sortable: false, align: 'left', dataIndex: 'cmClassified',renderer: CM.renderOperation}
];

