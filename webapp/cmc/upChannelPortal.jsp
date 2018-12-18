<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    library Highchart
    PLUGIN Portlet
    PLUGIN Highchart-Ext
    import cmc.js.HighchartGeneratorForCCMTS
</Zeta:Loader>
<head>
<link rel="stylesheet" type="text/css" href="/css/ext-plugin.css"/>
<style type="text/css">
.mydiv {padding: 5px 10px 5px 10px;}
.mydiv div {float: left;margin: 3px 10px 3px 3px;}
.h_handle {background:transparent url(/cmc/image/handle.jpg) no-repeat 0px 20px;
	height:43px;position:absolute;width:4px;}
.h_background{background:transparent url(/cmc/image/rulerBg.png) no-repeat;
	height:62px;position:relative;width:394px; margin:30px auto; overflow:hidden;}
</style>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>

<script type="text/javascript">
var cmcId = '<s:property value="cmcId"/>';
var channelTypeString = '<s:property value="channelTypeString"/>';
var channelIndex = '<s:property value="cmcUpChannelBaseShowInfo.channelIndex"/>';
//var noise = '<s:property value="noise"/>';
var entityIp = '<s:property value="entity.ip"/>';
var entityId = '<s:property value="entity.entityId"/>';
var cmcMac = '<s:property value="cmcDeviceNode"/>';
var cmcType = '${cmcType}';
var cmcAlias = '${cmcAttribute.nmName}';
//实时信噪比 (尺图)
var viewerParam = ${viewerParam};
//心燥
var chartParam = HighchartGeneratorForCCMTS.generate("noiseHis" , "@CMC/CCMTS.todayChannelNoiceGraph@" ,"@CMC/CCMTS.channelNoice@(dB)", "(dB)")
//误码率
var errorRateChartParam = HighchartGeneratorForCCMTS.generate("errorRateHis" , "@CMC/CCMTS.todayChannelErrorGateGraph@" ,"@CMC/CCMTS.channelErrorGate@","%")
//用户数，包括在线与离线
var channelCmNumChartParam = HighchartGeneratorForCCMTS.generate("channelCmNumActiveHis" , "@CMC/CCMTS.todayChannelUserGraph@" ,"@CMC/CCMTS.channelUserCount@",null)
//通道利用率
var channelUtilizationChartParam = HighchartGeneratorForCCMTS.generate("channelUtilizationHis" , "@CMC/CCMTS.todayChannelUtilizationGraph@" ,"@CMC/CCMTS.channelUtilization@(%)","(%)")

Ext.onReady(function(){
	Ext.QuickTips.init();
	 //界面portal
    var portletItems = [];
	 
	/* //端口实时信噪比;
	var currentChannelNoice = {
		id:"currentChannelNoice",
		title: I18N.CCMTS.currentChannelNoice,
		items:[{
     	        id:'currentNoiseGraph',
     	        bodyStyle:'padding:0px',
     	        autoScroll:true,
     	        contentEl:'currentNoise'
     	    	},{
                id:"detailNoise",
                 html:"<div style='TEXT-ALIGN: center'><table style='MARGIN: 0px auto;' > <tbody id='noiseCur'> </tbody> </table></div>"
              }]
    }
	
	//端口实时利用率;
	var currentChannelUtilizationGraph = {
			id:'currentChannelUtilizationGraph',
 	        title: I18N.CCMTS.currentChannelUtilization,
 	        bodyStyle:'padding:5px, 0px, 5px, 0px',
 	        autoScroll:true,
 	        contentEl:'currentChannelUtilization'	
	}
	//端口实时误码率;
	var currentErrorRateGraph = {
       id:'currentErrorRateGraph',
       title: I18N.CCMTS.currentErrorRate,
       bodyStyle:'padding:8px',
       autoScroll:true,
       contentEl:'currentErrorRate'
	} */
	//端口基本信息;
	var portDetail = {
			id:'portDetail',
 	        title: I18N.CCMTS.portDetail,
 	        bodyStyle:'padding:8px',
 	        autoScroll:true,
 	        contentEl:'detail'
	}
	//信噪比统计; 
   var noiseGraphPanel =  new Ext.ux.Portlet({ 
	id: "noiseGraphPanel",
	title: I18N.CCMTS.noiseGraph,
	items:[{
        items:[{
            id:'noiseGraph',
            bodyStyle: 'padding:8px;height:300',
            xtype: 'highchartpaneljson',
            titleCollapse: true,
            layout:'fit',
            border: true,
            chartConfig: chartParam
        },{
            id:"detailPanel0",
            html:"<div style='TEXT-ALIGN: center'><table style='MARGIN: 0px auto;' > <tbody id='seriesDetail'> </tbody> </table></div>"
        }]
    }]
	});
   //利用率统计;
   var utilizationGraphPanel = new Ext.ux.Portlet({ 
	id: "utilizationGraphPanel",
   	title: I18N.CCMTS.utilizationGraph,
       items:[{
           items:[{
                   id:'channelUtilizationGraph',
                   bodyStyle: 'padding:8px;height:300',
                   xtype: 'highchartpaneljson',
                   titleCollapse: true,
                   layout:'fit',
                   border: true,
                   chartConfig: channelUtilizationChartParam
               },{
                   id:"detailPanel2",
                   html:"<div style='TEXT-ALIGN: center'><table style='MARGIN: 0px auto;' > <tbody id='seriesDetail4'> </tbody> </table></div>"
               }]
           }]
       });
   //误码数统计;
   var errorRateGraphPanel = new Ext.ux.Portlet({
	id: "errorRateGraphPanel",
   	title: I18N.CCMTS.errorRateGraph,
       items:[{
          items:[{
                   id:'errorRateGraph',
                   bodyStyle: 'padding:8px;height:300',
                   xtype: 'highchartpaneljson',
                   titleCollapse: true,
                   layout:'fit',
                   border: true,
                   chartConfig: errorRateChartParam
               },{
                   id:"detailPanel1",
                   html:"<div style='TEXT-ALIGN: center'><table style='MARGIN: 0px auto;' > <tbody id='seriesDetail1'> </tbody> </table></div>"
               }]
           }]
   });
   //用户数统计;
   var cmCountPanel = new Ext.ux.Portlet({ 
	id:"cmCountPanel",
   	title: I18N.CCMTS.view.CmCount,
       items:[{
          items: [{
                   id:'CmNumGraph',
                   bodyStyle: 'padding:8px;height:300',
                   xtype: 'highchartpaneljson',
                   titleCollapse: true,
                   layout:'fit',
                   border: true,
                   chartConfig: channelCmNumChartParam
               },{
                   id:"detailPanel",
                   html:"<div style='TEXT-ALIGN: center'><table style='MARGIN: 0px auto;' > <tbody id='seriesDetail2'> </tbody> </table></div>"
               }]
           }]
       }); 
 	//左侧板块，从数据库读取;
	var leftPartStr = '${upChannelLeft}';
	//右侧板块，从数据库读取;
	var rightPartStr = '${upChannelRight}';
	//如果是第一次加载,就进行默认初始化
	if(leftPartStr == '' && rightPartStr == ''){
	 	leftPartStr = "portDetail,noiseGraphPanel,errorRateGraphPanel";
	 	rightPartStr = "utilizationGraphPanel,cmCountPanel";
	}
	var leftPart = {};
	leftPart.columnWidth = 0.5;
	leftPart.style = "padding:15px 5px 15px 5px";
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
    var headerPanel = new Ext.BoxComponent({region: 'north', el: 'head', margins: '0 0 0 0', height: 40, maxSize: 40});
    var viewport = new Ext.Viewport({
    	layout: 'border',
        items:[ headerPanel,
                {xtype: 'portal', region:'center', margins: '0 0 0 0', border: false, items: portletItems}]
    }); 
    //render here
    try {
    	seriesNoiseRead();
    	seriesErrorRateRead();
    	seriesChannelCmNumRead();
    	seriesChannelUtilizationRead();
    	//initNoise();
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
				url: '/cmc/saveUpChannelView.tv',
				cache:false, 
				method:'post',
				data: {
					cmcType : cmcType,
					upChannelLeft : leftPartStr, 
					upChannelRight : rightPartStr
				},
				success: function() {
				},
				error: function(){
				}
			});
		};//end if else;
	};//end saveLayout;
});

function initNoise(curNoisedata){
	var handlerLeft = (curNoisedata.substring(0,curNoisedata.length-2)/50)*362+10;
	$(".h_handle").css("left",handlerLeft);
	
	var html = "<tr><td class='pB20'>"+I18N.CCMTS.currentNoice+":<b class='blueTxt'>"+ curNoisedata+"</b></td><tr>";
	$("#noiseCur").append(html);
}
//信噪比
function seriesNoiseRead() {
	viewerParam.index = channelIndex;
    $.ajax({
        url: '/cmcperf/seriesNoiseRead.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        data:viewerParam,
        success: function(series) {
            try {
                var chartpanel = Ext.getCmp('noiseGraph');
                $.each(series, function(i, n) {
                    chartpanel.chart.addSeries(n, false);
                    var html = "<tr> <td style='COLOR: " + n.color + "'><strong>" + n.name + "</strong></td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.max + n.max + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.avg + n.avg + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.min + n.min + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.cur + n.cur + n.unit + "</td> </tr>"
                    $("#seriesDetail").append(html);
                    var noise = n.cur + n.unit;
                    initNoise(noise);// n.cur + n.unit +
                });
                chartpanel.hideLoading();
                chartpanel.chart.redraw();
            } catch(ex) {
                //alert("here "+ex);
            }
        }
    });
}

//误码率
function seriesErrorRateRead() {
	viewerParam.index = channelIndex;
    $.ajax({
        url: '/cmcperf/seriesErrorRateRead.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        data:viewerParam,
        success: function(series) {
            try {
                var chartpanel = Ext.getCmp('errorRateGraph');
                $.each(series, function(i, n) {
                    chartpanel.chart.addSeries(n, false);
                    var max = n.max;
                    var min = n.min;
                    var avg = n.avg;
                    if(max <0){
                    	max = 0;
                    }
                    if(min <0){
                    	min = 0;
                    }
                    if(avg <0){
                    	avg = 0;
                    }
                    var html = "<tr> <td style='COLOR: " + n.color + "'><strong>" 
                    + n.name + "</strong></td> <td style='COLOR: " + n.color + "'>"
                    +I18N.ap.chart.max + max +"%" + "</td> <td style='COLOR: " 
                    + n.color + "'>"+I18N.ap.chart.avg + avg+"%" 
                    + "</td> <td style='COLOR: " + n.color + "'>"
                    +I18N.ap.chart.min + min+"%" + "</td> <td style='COLOR: " 
                    + n.color + "'>"+I18N.ap.chart.cur + n.cur+"%" + "</td> </tr>"
                    $("#seriesDetail1").append(html);
                });
                chartpanel.hideLoading();
                chartpanel.chart.redraw();
            } catch(ex) {
                //alert("here "+ex);
            }
        }
    });
}

function checkChatVlaue(t){
	if(t<0){
		return 0;
	}else{
		return t;
	}
}
//通道利用率
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
                    $("#seriesDetail4").append(html);
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
                    $("#seriesDetail2").append(html);
                });
                chartpanel.hideLoading();
                chartpanel.chart.redraw();
            } catch(ex) {
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
<body>
<div id=head height=30px
	style="margin-left: 20px; margin-right: 10px; margin-top: 10px">
	<table width=100%>
		<tr>
			<td><a href="javascript:showOltInfo();" class="yellowLink"><s:property value="nodePath"/></a> - <a href="javascript:showCmcInfo();"  class="yellowLink"><s:property value="cmcDeviceNode"/></a> - <s:property value="cmcUpChannelBaseShowInfo.cmcPortName"/></td>
		</tr>
	</table>
</div>
<!-- <div id ="currentNoise"  class="h_background"><div style="overflow: hidden; top: 0px;" class="h_handle "></div></div> -->
<%-- <div id=currentErrorRate style="dislpay: none">
    <table class="dataTable zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
    <tbody>
		<tr>
			<td class="rightBlueTxt noWrap" width="120">@CMC/CM.correctedRate@@COMMON.maohao@</td>
			<td class="wordBreak"> ${bitErrorRate.bitErrorRate} %</td>
		</tr>
		<tr>
        	<td class="rightBlueTxt noWrap">@CMC/CM.uncorrectRate@@COMMON.maohao@</td>
           	<td class="wordBreak"> ${bitErrorRate.unBitErrorRate} %</td>
        </tr>
	</tbody>
    </table>
</div> --%>
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
			<td class="wordBreak"> <s:property value="cmcUpChannelBaseShowInfo.cmcPortName"/></td>
		</tr>
        <tr>
        	<td class="rightBlueTxt noWrap" >@CMC/CM.middleFrequency@:</td>
           	<td class="wordBreak"><s:property value="cmcUpChannelBaseShowInfo.docsIfUpChannelFrequencyForunit"/></td>
        </tr>
        <tr>
        	<td class="rightBlueTxt noWrap" >@CMC/CMC.label.bandwidth@:</td>
           	<td class="wordBreak"><s:property value="cmcUpChannelBaseShowInfo.docsIfUpChannelWidthForunit"/></td>
        </tr>
        <%-- <tr>
        	<td class="rightBlueTxt noWrap" >@CMC/CCMTS.onlineUserCount@@COMMON.maohao@</td>
           	<td class="wordBreak"><s:property value="cmNumOnline"/></td>
        </tr>
        <tr>
        	<td class="rightBlueTxt noWrap" >@CMC/CCMTS.offlineUserCount@@COMMON.maohao@</td>
           	<td class="wordBreak"><s:property value="cmNumOffline"/></td>
        </tr> --%>
    </tbody>
    </table>
</div>
</body>
</Zeta:HTML>