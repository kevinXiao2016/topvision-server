<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library Ext
    library Jquery
    library Zeta
    library HighchartForCcmts
    plugin  Porlet
    plugin  Highchart-ext
    import  cmc.js.HighchartGeneratorForCCMTS
</Zeta:Loader>
<style type="text/css">
.mydiv {
	padding: 5px 10px 5px 10px;
}

.mydiv div {
	float: left;
	margin: 3px 10px 3px 3px;
}
</style>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.resources.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" defer>
var cmcId = '<s:property value="cmcId"/>';
var channelTypeString = '<s:property value="channelTypeString"/>';
var channelIndex = '<s:property value="cmcDownChannelBaseShowInfo.channelIndex"/>';
var entityIp = '<s:property value="entity.ip"/>';
var entityId = '<s:property value="entity.entityId"/>';
var cmcMac = '<s:property value="cmcDeviceNode"/>';
var cmcType = '${cmcType}';
var cmcAlias = '${cmcAttribute.nmName}';
//实时信噪比 (尺图)
var viewerParam = ${viewerParam};
var channelUtilizationChartParam = HighchartGeneratorForCCMTS.generate("channelUtilizationHis" , "@CMC/CCMTS.todayChannelUtilizationGraph@" ,"@CMC/CCMTS.channelUtilization@(%)", "(%)")
var channelCmNumChartParam = HighchartGeneratorForCCMTS.generate("channelCmNumActiveHis" , "@CMC/CCMTS.todayChannelUserGraph@" ,"@CMC/CCMTS.channelUserCount@",null)
Ext.onReady(function(){
	Ext.QuickTips.init();
	 //界面portal
    var portletItems = [];
	/* //端口实时利用率;
	var currentChannelUtilizationGraph = {
        id:'currentChannelUtilizationGraph',
        title: I18N.CCMTS.currentChannelUtilization,
        bodyStyle:'padding:8px',
        autoScroll:false,
        contentEl:'currentChannelUtilization'
    } */
	//端口基本信息;
	var portDetail = {
        id:'portDetail',
        title: I18N.CCMTS.portDetail,
        bodyStyle:'padding:8px',
        autoScroll:false,
        contentEl:'detail'
    }
	//今日端口利用率统计图;
	var todayChannelUtilizationGraph = {
			id :"todayChannelUtilizationGraph",
            title: I18N.CCMTS.todayChannelUtilizationGraph,
            items:[{
                id:'channelUtilizationGraph',
                bodyStyle: 'padding:8px;height:300',
                xtype: 'highchartpaneljson',
                titleCollapse: true,
                layout:'fit',
                border: true,
                chartConfig: channelUtilizationChartParam
            },{
                id:"detailPanel",
                html:"<div style='TEXT-ALIGN: center'><table style='MARGIN: 0px auto;' > <tbody id='seriesDetail2'> </tbody> </table></div>"
            }]
        }
	//用户数统计
	var cmCount = {
			id: "cmCount",
            title: I18N.CCMTS.view.CmCount,
            items:[{
                id:'CmNumGraph',
                bodyStyle: 'padding:8px;height:300',
                xtype: 'highchartpaneljson',
                titleCollapse: true,
                layout:'fit',
                border: true,
                chartConfig: channelCmNumChartParam
            },{
                id:"detailPanel2",
                html:"<div style='TEXT-ALIGN: center'><table style='MARGIN: 0px auto;' > <tbody id='seriesDetail3'> </tbody> </table></div>"
            }]
        }
	
	//左侧板块，从数据库读取;
	var leftPartStr = '${downChannelLeft}';
	//右侧板块，从数据库读取;
	var rightPartStr = '${downChannelRight}';
	//如果是第一次加载,就进行默认初始化
	if(leftPartStr == '' && rightPartStr == ''){
	 	leftPartStr = "portDetail";
	 	rightPartStr = "todayChannelUtilizationGraph,cmCount";
	}
	var leftPart = {};
	leftPart.columnWidth = 0.5;
	leftPart.style = 'padding:15px 5px 15px 5px';
	leftPart.items = new Array();
	//开始添加板块;
    if(leftPartStr != ''){
		 var tempArr = leftPartStr.split(",");
		 for(var i=0; i<tempArr.length; i++){
			 leftPart.items.push(eval(tempArr[i]));
		 }
	}
    var rightPart = {};
    rightPart.columnWidth = 0.5;
    rightPart.style = 'padding:15px 5px 15px 0px';
    rightPart.items = new Array();
	//开始添加板块;
	if(rightPartStr != ''){
	    var tempArr = rightPartStr.split(",");
		for(var i=0; i<tempArr.length; i++){
	    	rightPart.items.push(eval(tempArr[i]));
	    }
	}
	portletItems[0] = leftPart;
    portletItems[1] = rightPart;
    var headerPanel = new Ext.BoxComponent({region: 'north', el: 'header', margins: '0 0 0 0', height: 40, maxSize: 40});
    var viewport = new Ext.Viewport({layout: 'border',
        items:[headerPanel, {xtype: 'portal', region:'center', margins: '0 0 0 0', border: false, items: portletItems}]
    }); 
    try {
    	seriesChannelUtilizationRead();
    	seriesChannelCmNumRead();
    } catch(ex) {
        //alert(ex);
    }
    //通过判断左右两侧id的顺序，判断布局是否改变，如果改变，那么保存布局(要求id和变量名一致);
	$(".x-panel-tl").live("mouseup",function(){
		 setTimeout(saveLayout,200);
		});//end live; 
	function saveLayout(){
		var leftArr = [];
		var rightArr = [];
		$(".x-portal-column").eq(0).find(".x-portlet").each(function(){
			leftArr.push($(this).attr("id"));
		})
		$(".x-portal-column").eq(1).find(".x-portlet").each(function(){
			rightArr.push($(this).attr("id"));
		})
		
		if(leftPartStr == leftArr.toString() && rightPartStr == rightArr.toString()){//没有变化;
		}else{//有变化;
			leftPartStr = leftArr.toString();
			rightPartStr = rightArr.toString();
			$.ajax({
				url: '/cmc/saveDownChannelView.tv',
				cache:false, 
				method:'post',
				data: {
					cmcType : cmcType,
					downChannelLeft : leftPartStr, 
					downChannelRight : rightPartStr
				},
				success: function() {
				},
				error: function(){
				}
			});
		};//end if else;
	};//end saveLayout;
});

function seriesChannelUtilizationRead() {
	viewerParam.index = channelIndex;
    $.ajax({
        url: '/cmcperf/seriesChannelUtilizationRead.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        data:viewerParam,
        success: function(series) {
            try {
                var chartpanel = Ext.getCmp('channelUtilizationGraph');
                $.each(series, function(i, n) {
                    chartpanel.chart.addSeries(n, false);
                    var html = "<tr> <td style='COLOR: " + n.color + "'><strong>" + n.name + "</strong></td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.max + n.max + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.avg + n.avg + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.min + n.min + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.cur + n.cur + n.unit + "</td> </tr>"
                    $("#seriesDetail2").append(html);
                });
                chartpanel.hideLoading();
                chartpanel.chart.redraw();
            } catch(ex) {
                //alert("here "+ex);
            }
        }
    });
}
//用户数，包括在线与离线
function seriesChannelCmNumRead() {
	viewerParam.index = channelIndex;
    $.ajax({
        url: '/cmcperf/seriesChannelCmNumRead.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        data:viewerParam,
        success: function(series) {
            try {
                var chartpanel = Ext.getCmp('CmNumGraph');
                $.each(series, function(i, n) {
                    chartpanel.chart.addSeries(n, false);
                    var html = "<tr> <td style='COLOR: " + n.color + "'><strong>" + n.name + "</strong></td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.max + n.max + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.avg + n.avg + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.min + n.min + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.cur + n.cur + n.unit + "</td> </tr>"
                    $("#seriesDetail3").append(html);
                });
                chartpanel.hideLoading();
                chartpanel.chart.redraw();
            } catch(ex) {
                //alert("here "+ex);
            }
        }
    });
}

function showOltInfo(){
	if (EntityType.isCcmtsWithoutAgentType(cmcType)) {		
		window.parent.addView('entity-' + entityId, I18N.COMMON.entity + '[' + entityIp + ']', 'entityTabIcon',
				'portal/showEntitySnapJsp.tv?entityId=' + entityId)
	} else {	
		window.parent.addView('entity-' + entityId, cmcAlias, 'entityTabIcon',
				'portal/showEntitySnapJsp.tv?entityId=' + entityId)
	}
}
function showCmcInfo(){
	window.parent.addView('entity-' + cmcId, cmcAlias, 'entityTabIcon',
			'/cmc/showCmcPortal.tv?cmcId=' + cmcId);
}

function doRefresh(){
    window.location.reload();
}
</script>
</head>
<body class="">
<div id=header height=30px
	style="margin-left: 20px; margin-right: 10px; margin-top: 10px">
	<table width=100%>
		<tr>
			<td><a href="javascript:showOltInfo();" class="yellowLink"><s:property value="nodePath"/></a> - <a href="javascript:showCmcInfo();" class="yellowLink"><s:property value="cmcDeviceNode"/></a> - <s:property value="cmcDownChannelBaseShowInfo.cmcPortName"/></td>
		</tr>
	</table>
</div>
<%-- <div id=currentChannelUtilization style="dislpay: none">
    <table width=100%>
        <tr>
            <td align=center>
                <div>
                    <img id="bitErrorRateImg"
                         src="/cmc/getChannelUtilizationByCmcPortId.tv?cmcPortId=<s:property value="cmcPortId"/>&cmcId=<s:property value="cmcId"/>"
                         border=0 hspace=5 />
                </div>
            </td>
        </tr>
    </table>
</div> --%>
<div id=detail style="dislpay: none">
	<table class="dataTable zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
	<tbody>
		<tr>
			<td class="rightBlueTxt noWrap" width="120">@RESOURCES/WorkBench.PortName@:</td>
			<td class="wordBreak"><s:property value="cmcDownChannelBaseShowInfo.cmcPortName"/></td>
		</tr>
		<tr>
			<td class="rightBlueTxt noWrap">@CMC/CM.middleFrequency@:</td>
			<td class="wordBreak"><s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelFrequencyForunit"/></td>
		</tr>
		<tr>
			<td class="rightBlueTxt noWrap">@CMC/CMC.label.bandwidth@:</td>
			<td class="wordBreak"><s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelWidthForunit"/></td>
		</tr>
		<tr>
			<td class="rightBlueTxt noWrap">@CMC/CM.channelPower@:</td>
			<td class="wordBreak"><s:property value="cmcDownChannelBaseShowInfo.DocsIfDownChannelPowerForunit"/></td>
		</tr>
		<tr>
			<td class="rightBlueTxt noWrap">@CMC/CM.modStyle@:</td>
			<td class="wordBreak"><s:property value="cmcDownChannelBaseShowInfo.DocsIfDownChannelModulationName"/></td>
		</tr>
		<%-- <tr>
			<td class="rightBlueTxt noWrap">@CMC/CCMTS.onlineUserCount@:</td>
			<td class="wordBreak"><s:property value="cmNumOnline"/></td>
		</tr>
		<tr>
			<td class="rightBlueTxt noWrap">@CMC/CCMTS.offlineUserCount@:</td>
			<td class="wordBreak"><s:property value="cmNumOffline"/></td>
		</tr> --%>
	</tbody>
	</table>
</div>
</body>
</Zeta:HTML>
