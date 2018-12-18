var STATUS_UP = 1;
var STATUS_DOWN = 2;
//render
//端口信息
function renderPortInfo(value,p,record){
	if(record.data.channelType == 0){
	    return renderUpPortName(value,p,record);
	}else if(record.data.channelType == 1){
	    return renderDownPortName(value,p,record);
	}
}

function renderRate(value,p,record){
	if(record.data.channelStatus == 2){
	    return "-";
	}else {
	    return value;
	}
}

function renderRTSpeed(value,p,record){
	return value ;
	/*if(record.data.channelType == 0){
		return String.format("<a href='#' class=my-link onclick='showUpChannelSpeedInfo(\"{0}\")'>"+ value + 
				"</a>", record.data.channelIndex);
	}else if(record.data.channelType == 1){
		return String.format("<a href='#' class=my-link onclick='showDownChannelSpeedInfo(\"{0}\")'>"+ value + 
				"</a>", record.data.channelIndex);
	}*/
}
function renderChannelUtilization(v,c,r){
	if(r.data.channelStatus == 2){
		return "-";
	}
	if(v == '-'){
		return v;
	}		
	var color;
	if(parseInt(v.split("%")[0])<75){
		color = "#14a600";
	} else{
		color = "red";
	}
	var value = v;
	var left = -50;
	var str = '';
	str += '<div class="percentBarBg">';
	if(color == "red"){
		str +=     '<div class="percentBarRed" style="width:'+ value +';">';
		str +=     '<div class="percentBarLeftConnerRed"></div>';
	}else{
		str +=     '<div class="percentBar" style="width:'+ value +';">';
		str +=     '<div class="percentBarLeftConner"></div>';
	}		
	str += '</div>';
	str += '<div class="percentBarTxt">'+ numberToPic(value) +'</div></div>';
	return str;  
}
function numberToPic(str){
	var newStr = '';	
	if(str.length < 0) return;
	newStr += '<div class="noWidthCenterOuter clearBoth"><ol class="noWidthCenter leftFloatOl pT1">';	
	
	//var str = "56.23%";
	if(str == "0%"){
		newStr +=  '<li><div class="percentNum0"></div></li>';
	}else{
		var newNum = str.substr(0,str.length-1);
		var arr = newNum.split(".");
		for(var i=0;i<arr[0].length; i++){
			newStr += "<li><div class='percentNum"+ arr[0][i] +"'></div></li>";
		}
		newStr += "<li><div class='percentNumDot'></div></li>";
		if(arr[1].length == 1){arr[1] += "0"}
		for(var j=0;j<arr[1].length; j++){
			newStr += "<li><div class='miniPercentNum"+ arr[1][j] +"'></div></li>";
		}
	}
	
	/*for(var i=0; i<str.length-1; i++){
		var cls = "percentNum" + str.substr(i,1);
		newStr += '<li><div class="' + cls + '"></div></li>';
	}*/
	
	
	newStr += '<li><div class="percentNumPercent"></div><li></ol></div>';
	return newStr;
}
//上行端口信息
function renderDocsIfChannel(value,p,record){
	if(record.data.ifAdminStatus == STATUS_UP){
		return value;
	}else{
		return ' - ';
	}
}
function renderDocsIfSigQSignalNoiseForunit(value,p,record){
	if(record.data.ifAdminStatus == STATUS_UP){
		return value ;
	}else{
		return ' - ';
	}
}
//下行端口信息
function renderDocsIfDownChannelPowerForunit(value,p,record){
	
}
//上行端口用户数统计
function renderUpPortName(value,p,record){
	return String.format("<a href='#' class=my-link onclick='showUpChannelInfo(\"{0}\",\"{1}\",\"{2}\",\"{3}\")'>"+record.data.cmcPortName + 
			"</a>", record.data.cmcPortId, record.data.cmcId, record.data.cmcPortName, record.data.channelIndex);
}
//下行端口用户数统计
function renderDownPortName(value,p,record){
    return String.format("<a href='#' class=my-link onclick='showDownChannelInfo(\"{0}\",\"{1}\",\"{2}\",\"{3}\")'>"+record.data.cmcPortName + 
            "</a>", record.data.cmcPortId, record.data.cmcId, record.data.cmcPortName, record.data.channelIndex);
}
function renderChannelCmTotalNum(value,p,record){
	return String.format("<a href='#' class=my-link onclick='showChannelAllCmNumInfo(\"{0}\")'>"+ value + 
			"</a>", record.data.channelIndex);
}
function renderChannelCmOnlineNum(value,p,record){
	return String.format("<a href='#' class=my-link onclick='showChannelOnlineCmNumInfo(\"{0}\")'>"+ value + 
			"</a>", record.data.channelIndex);
}
function renderChannelCmActiveNum(value,p,record){
	return String.format("<a href='#' class=my-link onclick='showChannelActiveCmNumInfo(\"{0}\")'>"+ value + 
			"</a>", record.data.channelIndex);
}
function renderChannelCmOfflineNum(value,p,record){
	return String.format("<a href='#' class=my-link onclick='showChannelOfflineCmNumInfo(\"{0}\")'>"+ value + 
			"</a>", record.data.channelIndex);
}
/*function renderToCmList(value,p,record){
	return String.format("<a  href='javascript:;' border=0 align=absmiddle onclick = 'showCmDetail(\"{0}\",\"{1}\")'>" + I18N.COMMON.view + "</a>", record.data.channelIndex, record.data.entityId);
}*/
function renderUpCnlToCmList(value, p, record){
    return String.format("<a  href='javascript:;' border=0 align=absmiddle onclick = 'showCmDetail(\"{0}\",\"{1}\",\"{2}\",\"{3}\")'>" + I18N.COMMON.view + "</a>",
                            record.data.entityId, record.data.cmcId, 0, record.data.channelId);
}
function renderDownChlToCmList(value, p, record){
    return String.format("<a  href='javascript:;' border=0 align=absmiddle onclick = 'showCmDetail(\"{0}\",\"{1}\",\"{2}\",\"{3}\")'>" + I18N.COMMON.view + "</a>",
                            record.data.entityId, record.data.cmcId, 1, record.data.channelId);
}

//grid
//端口信息
var portInfoColumns = [{header: I18N.WorkBench.PortName, width: 50, sortable: false, align: 'center', dataIndex: 'channelId', renderer: renderPortInfo},
	{header: I18N.CCMTS.RTSpeed, width: 50, sortable: false, align: 'center', dataIndex: 'channelOctetsRateStr',renderer:renderRate},
	{header: I18N.topo.linkProperty.bandwidth, width: 50, sortable: false, align: 'center', dataIndex: 'ifSpeedString'},
	{header: I18N.WorkBench.Utilization, width: 50, sortable: false, align: 'center', dataIndex: 'channelUtilizationString', renderer: renderChannelUtilization}
];
var portInfoStore = new Ext.data.JsonStore({
 	url: '/cmc/channel/getCmcPortInfo.tv?cmcId=' + cmcId,
 	root: 'data',
 	fields: ['channelId', 'channelOctetsRateStr', 'channelType', 'ifSpeedString', 'channelUtilizationString', 'cmcPortId', 'cmcId', 'cmcPortName', 'channelIndex', 'ifDescr','channelStatus']
});
//上行端口信息
var upChannelInfoColumns = [{header: I18N.WorkBench.PortName, width: 50, sortable: false, align: 'center', dataIndex: 'channelId', renderer: renderUpPortName},
	{header: I18N.CM.middleFrequency, width: 50, sortable: false, align: 'center', dataIndex: 'docsIfUpChannelFrequencyForunit', renderer: renderDocsIfChannel},
	{header: I18N.CMC.label.bandwidth, width: 50, sortable: false, align: 'center', dataIndex: 'docsIfUpChannelWidthForunit', renderer: renderDocsIfChannel},
	{header: I18N.WorkBench.Noise, width: 50, sortable: false, align: 'center', dataIndex: 'docsIfSigQSignalNoiseForunit', renderer: renderDocsIfSigQSignalNoiseForunit}
];
var upChannelInfoStore = new Ext.data.JsonStore({
	url: '/cmc/channel/getUpChannelInfo.tv?cmcId=' + cmcId,
    root: 'data',
    fields: ['channelId', 'docsIfUpChannelFrequencyForunit', 'docsIfUpChannelWidthForunit', 'docsIfSigQSignalNoiseForunit', 'cmcPortId', 'cmcId', 'cmcPortName', 'channelIndex', 'ifAdminStatus']
});
//下行端口信息
var downChannelInfoColumns = [{header: I18N.WorkBench.PortName, width: 50, sortable: false, align: 'center', dataIndex: 'docsIfDownChannelId', renderer: renderDownPortName},
	{header: I18N.CM.middleFrequency, width: 50, sortable: false, align: 'center', dataIndex: 'docsIfDownChannelFrequencyForunit', renderer: renderDocsIfChannel},
	{header: I18N.CM.channelPower, width: 50, sortable: false, align: 'center', dataIndex: 'docsIfDownChannelPowerForunit', renderer: renderDocsIfChannel},
	{header: I18N.CM.modStyle, width: 50, sortable: false, align: 'center', dataIndex: 'docsIfDownChannelModulationName', renderer: renderDocsIfChannel}
];
var downChannelInfoStore = new Ext.data.JsonStore({
	url: '/cmc/channel/getDownChannelInfo.tv?cmcId=' + cmcId,
	root: 'data',
	fields: ['docsIfDownChannelId', 'docsIfDownChannelFrequencyForunit', 'docsIfDownChannelPowerForunit', 'docsIfDownChannelModulationName', 'cmcPortId', 'cmcId', 'cmcPortName', 'channelIndex', 'ifAdminStatus']
});
//上行端口用户数统计
var portUpCMNumColumns = [{header: I18N.WorkBench.PortName, width: 100, sortable: false, align: 'center', dataIndex: 'channelId', renderer: renderUpPortName},
	{header: I18N.CHANNEL.total, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumTotal', renderer: renderChannelCmTotalNum},
	{header: I18N.CMCPE.status.online, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumOnline', renderer: renderChannelCmOnlineNum},
	{header: I18N.CHANNEL.onlining, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumActive', renderer: renderChannelCmActiveNum},
	{header: I18N.CMC.label.offline, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumOffline', renderer: renderChannelCmOfflineNum},
	{header:  I18N.CM.cmList, width: 50, sortable: false, align: 'center', dataIndex: 'op', renderer: renderUpCnlToCmList}
];
//下行端口用户数统计
var portDownCMNumColumns = [{header: I18N.WorkBench.PortName, width: 100, sortable: false, align: 'center', dataIndex: 'channelId', renderer: renderDownPortName},
    {header: I18N.CHANNEL.total, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumTotal', renderer: renderChannelCmTotalNum},
    {header: I18N.CMCPE.status.online, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumOnline', renderer: renderChannelCmOnlineNum},
    {header: I18N.CHANNEL.onlining, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumActive', renderer: renderChannelCmActiveNum},
    {header: I18N.CMC.label.offline, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumOffline', renderer: renderChannelCmOfflineNum},
    {header:  I18N.CM.cmList, width: 50, sortable: false, align: 'center', dataIndex: 'op', renderer: renderDownChlToCmList}
];
var upChannelUserNumStore = new Ext.data.JsonStore({
	url: '/cmc/channel/getUpChannelUserNum.tv?cmcId=' + cmcId,
    root: 'data',
    fields: ['channelId', 'cmNumTotal', 'cmNumOnline','cmNumActive','cmNumOffline', 'cmcPortName', 'cmcPortId', 'cmcId', 'channelIndex', 'entityId']
});
//下行端口用户数统计
var downChannelUserNumStore = new Ext.data.JsonStore({
	url: '/cmc/channel/getDownChannelUserNum.tv?cmcId=' + cmcId,
    root: 'data',
    fields: ['channelId', 'cmNumTotal', 'cmNumOnline','cmNumActive','cmNumOffline', 'cmcPortName', 'cmcPortId', 'cmcId', 'channelIndex', 'entityId']
});

function cmcNoMacBind() {
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CCMTS.confirmNoMacBindCcmts, function(button, text) {
        if (button == "yes") {
            $.ajax({
                url:'cmc/cmcNoMacBind.tv?cmcId=' + cmcId,
                type:'POST',
                dateType:'json',
                success:function(response) {
                    if (response.message == "success") {
                        top.afterSaveOrDelete({
                            title: I18N.COMMON.tip,
                                html: '<b class="orangeTxt">'+I18N.CCMTS.noMacBindCcmtsSuccess+'</b>'
                        });
                    } else {
                        top.afterSaveOrDelete({
                            title: I18N.COMMON.tip,
                                html: '<b class="orangeTxt">'+I18N.CCMTS.noMacBindCcmtsFail+'</b>'
                        });
                    }
                },
                error:function() {
                    top.afterSaveOrDelete({
                        title: I18N.COMMON.tip,
                            html: '<b class="orangeTxt">'+I18N.CCMTS.noMacBindCcmtsFail+'</b>'
                    });
                },
                cache:false
            });
        }
    });
}