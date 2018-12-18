<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    MODULE  CMC
    css css.white.disabledStyle
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmcId = ${ cmcId };
var baseStore=null;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
function onRefreshClick() {
	baseStore.reload();
}
function refreshBase(){
    //modify by Victor@20140725把提示改为上联口，原为下行信道
	window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.text.refreshUpLinkTip,'waitingMsg','ext-mb-waiting');
	$.ajax({
		url:"/cmts/channel/refreshCmtsPorts.tv?cmcId=" + cmcId,
		type:"post",
		success:function (response){
			if(response=="true"){
				onRefreshClick();
				//window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshSuccessTip);
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">' + I18N.text.refreshSuccessTip + '</b>'
	   			});
			}else{
				window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
			}
		},error: function(response) {
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
		}, cache: false
	});	
}
function renderAdminStatus(value, p, record){
	if (record.data.ifAdminStatus == '1') {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/admin.gif" border=0 align=absmiddle class="nm3kTip">',
			I18N.COMMON.startTitle);		
	} else {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/noadmin.gif" border=0 align=absmiddle class="nm3kTip">',
			I18N.SYSTEM.Disable);	
	}
}
function renderOperStatus(value, p, record){
	if (record.data.ifAdminStatus == '1' && record.data.ifOperStatus == '1')  {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/admin.gif" border=0 align=absmiddle class="nm3kTip">', I18N.COMMON.on);	
	} else {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/noadmin.gif" border=0 align=absmiddle class="nm3kTip">', I18N.COMMON.off);	
	}
}

function renderMac(value, p, record){
	if (record.data.ifPhysAddress != null && record.data.ifPhysAddress != '')  {
		return String.format(record.data.ifPhysAddress);	
	} else {
		return String.format('-');	
	}
}

function formatFloat(src, pos)
{
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);
}

function renderIfSpeed(value, p, record){
	if (record.data.ifSpeed != null && record.data.ifSpeed > 0)  {
		return String.format(formatFloat(record.data.ifSpeed/1000000, 2) + 'Mbps');	
	} else {
		return String.format('-');	
	}
}
function renderType(value, p, record) {
    if (value != null) {
        switch (parseInt(value)) {
            case 1:
                return "other(1)";
            case 2:
                return "regular1822(2) ";
            case 3:
                return "hdh1822(3) ";
            case 4:
                return "ddn-x25(4) ";
            case 5:
                return "rfc877-x25(5) ";
            case 6:
                return "ethernet-csmacd(6)";
            case 7:
                return "iso88023-csmacd(7) ";
            case 8:
                return "iso88024-tokenBus(8)";
            case 9:
                return "iso88025-tokenRing(9) ";
            case 10:
                return "iso88026-man(10)";
            case 11:
                return "starLan(11) ";
            case 12:
                return "proteon-10Mbit(12) ";
            case 13:
                return "proteon-80Mbit(13) ";
            case 14:
                return "hyperchannel(14)";
            case 15:
                return "fddi(15)";
            case 16:
                return "lapb(16) ";
            case 17:
                return "sdlc(17) ";
            case 18:
                return "ds1(18)";
            case 19:
                return "e1(19)";
            case 20:
                return "basicISDN(20) ";
            case 21:
                return "primaryISDN(21) ";
            case 22:
                return "propPointToPointSerial(22) ";
            case 23:
                return "ppp(23) ";
            case 24:
                return "softwareLoopback(24) ";
            case 25:
                return "eon(25)";
            case 26:
                return "ethernet-3Mbit(26)";
            case 27:
                return "nsip(27) ";
            case 28:
                return "slip(28)";
            case 29:
                return "ultra(29)";
            case 30:
                return "ds3(30) ";
            case 31:
                return "sip(31)";
            case 32:
                return "frame-relay(32) ";
            default :
                return "other(" + value + ")";
        }
    } else {
        return "other";
    }
}
function renderInRate(value, p, record){
	if (record.data.ifInOctetsRateForunit != null && record.data.ifInOctetsRateForunit != '')  {
		return String.format(record.data.ifInOctetsRateForunit + 'Mbps');	
	} else {
		return String.format('-');	
	}
}
function renderOutRate(value, p, record){
	if (record.data.ifOutOctetsRateForunit != null && record.data.ifOutOctetsRateForunit != '')  {
		return String.format(record.data.ifOutOctetsRateForunit + 'Mbps');	
	} else {
		return String.format('-');	
	}
}
function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}
Ext.onReady(function () {
	Ext.QuickTips.init()
	var w = $(window).width() - 30;
	var h = $(window).height() - 100;
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var baseColumns = [
		//sm,
	    {header: I18N.CHANNEL.channelName,  sortable: true, width: parseInt(w/5), align: 'center', dataIndex: 'ifName', renderer: addCellTooltip},
		{header: "MAC", width: parseInt(w/5), sortable: false, align: 'center', dataIndex: 'ifPhysAddress', renderer: renderMac},
		{header: I18N.CMC.label.type, width: parseInt(w/8), sortable: false, align: 'center', dataIndex: 'ifType',renderer: renderType},
       	{header: I18N.CHANNEL.width, width: parseInt(w/8), sortable: false, align: 'center', dataIndex: 'ifSpeed',renderer: renderIfSpeed},
       	{header: I18N.CHANNEL.ifInSpeed, width: parseInt(w/8), sortable: false, align: 'center', dataIndex: 'ifInOctetsRateForunit',renderer: renderInRate},
       	{header: I18N.CHANNEL.ifOutSpeed, width: parseInt(w/8), sortable: false, align: 'center', dataIndex: 'ifOutOctetsRateForunit',renderer: renderOutRate},
       	{header: I18N.CCMTS.channel.adminStatus, width: parseInt(w/10), sortable: false, align: 'center', dataIndex: 'ifAdminStatus',renderer: renderAdminStatus},	
		{header: I18N.CCMTS.channel.status, width: parseInt(w/10), sortable: false, align: 'center', dataIndex: 'ifOperStatus',renderer: renderOperStatus}		
	];

	baseStore = new Ext.data.JsonStore({
	    url: ('/cmts/channel/getUpLinkPortList.tv?cmcId=' + cmcId),
	    root: 'data',
	    fields: ['portId','ifDescr', 'ifName', 'ifPhysAddress','ifType', 'ifSpeed', 'ifInOctetsRate', 'ifOutOctetsRate', 
	             'ifInOctetsRateForunit', 'ifOutOctetsRateForunit', 'ifSpeedForunit', 'ifDescr', 'ifMtu', 'ifAdminStatus', 'ifOperStatus']
	});
	baseStore.setDefaultSort('portId', 'ASC');
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		title:I18N.CMTS.upLinkPortList,
		cls: 'normalTable',
		border: true, 
		store: baseStore, 
		margins: "0px 10px 10px 10px",
		cm: baseCm,
		region:'center',
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
	baseStore.load();
	
	//点击从设备上获取；
	$("#btn1").click(function(){
		window.refreshBase();
	});
	
	new Ext.Viewport({
	    layout: 'border',
	    items: [{
	        region: 'north',
	        height:90,
	        border: false,
	        contentEl: 'topPart'
	    },baseGrid]
	});
	
});

function authLoad(){
    if(!refreshDevicePower){
        $("#btn1").attr("disabled",true);
    }
}
</script>
</head>
<body class="whiteToBlack" onload="authLoad()">
	<div id="topPart">
		<div id=header>
			<%@ include file="entity.inc"%>
		</div>
		<div class="edgeAndClearFloat">
			<ol class="upChannelListOl" id="upChannelListOl">
				<li><a href="javascript:;" class="normalBtn" id="btn1" name="@CMC.ObtainFromTheEquipment@"><span><i class="miniIcoEquipment"></i>@text.refreshData@</span></a></li>
			</ol>
		</div>
	</div>	
	<dl class="legent" style="top:90px;">
		<dt class="mR5">@CMC.Legend@</dt>
		<dd><img src="../../images/correct.png" border="0" alt="" /></dd>
		<dd class="mR10">@CMC.select.open@</dd>
		<dd><img src="../../images/wrong.png" border="0" alt="" /></dd>
		<dd>@CMC.button.close@</dd>
	</dl>
			
</body>
</Zeta:HTML>
