<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<%-- <script src="/js/highstock/highstock.js"></script>  --%>
<script src="/performance/js/highstock.js"></script> 
<Zeta:Loader>
	library ext
	import js.ext.ext-basex
	library zeta
	plugin DateTimeField
	plugin LovCombo
	module  cmc
	css  css.performanceMajorStyle
	import js.zetaframework.component.NetworkNodeSelector
    import js/nm3k/nm3kPickDate
    import cmc.cmcHisUserNum
</Zeta:Loader>
<script type="text/javascript" src="/js/jquery/jquery.wresize.js"></script>
<script type="text/javascript" src="/js/jquery/dragMiddle.js"></script>
<style type="text/css">
	body {
		overflow: hidden;height: 100%; 
		overflow-y:auto;
	}
	.item-selected {
		background:url(/epon/image/checked.gif) no-repeat;
	}
	.item-unselected {
		background:url(/epon/image/unchecked.gif) no-repeat;
	}
	.perfItem{margin-top: 20px; margin-right: 40px;margin-left: 50px;border:1px solid #CCC;}
	
</style>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.performance.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String st = sdf.format(new Date());
%>
var selectedQuots = new Array;
var chart = null;
var startTime;
var endTime; 
function bulidToolbar(){
	var lastMonth = new Date();
	var current = new Date();
	lastMonth.setTime(lastMonth.getTime()-(7*24*60*60*1000));
	var minDate = new Date();
	minDate.setTime(0); 
	startTime = new Ext.ux.form.DateTimeField({
		width:160,
		value : lastMonth,
		editable: false,
	    renderTo: 'startTime',
	    emptyText:'@Tip.pleaseEnterTime@',
	    blankText:'@Tip.pleaseEnterTime@'
	});
	endTime = new Ext.ux.form.DateTimeField({
		width:160,
		value : current,
		editable: false,
	    renderTo: 'endTime',
	    emptyText:'@Tip.pleaseEnterTime@',
	    blankText:'@Tip.pleaseEnterTime@'
	});  
	
	nm3kPickData({
	    startTime : startTime,
	    endTime : endTime,
	    searchFunction : query
	})
	
    var indexStore = new Ext.data.JsonStore({
    	data: [{id:'onlineQuota',text:'@CMCPE.CMONLINENUM@'},
		       {id:'offlineQuota',text:'@CMCPE.CMOFFLINENUM@'},
		       {id:'interactiveQuota',text:'@CMCPE.CMInteractiveOnlineNum@'},
		       {id:'broadbandQuota',text:'@CMCPE.CMBroadbandOnlineNum@'},
		       {id:'mtaQuota',text:'@CMCPE.CMMTANum@'},
		       {id:'integratedQuota',text:'@CMCPE.CMIntegratedMachineNum@'},
		       {id:'otherQuota',text:'@CMCPE.CMOtherNum@'},
		       {id:'cpeInteractiveQuota',text:'@CMCPE.CPEInteractiveQNum@'},
		       {id:'cpeBroadbandQuota',text:'@CMCPE.CPEBroadbandNum@'},
		       {id:'cpeMtaQuota',text:'@CMCPE.CPEMtaNum@'}],
		fields:["id","text"]
    });
	new Ext.ux.form.LovCombo({
        width: 160,
        id:'indexSelect',
        value:'onlineQuota',
        hideOnSelect : true,
        editable : false,
        renderTo: 'indexSelectDiv',  
        store : indexStore,
        valueField: 'id',
		displayField: 'text',
        triggerAction : 'all',  
        mode:'local',
        emptyText : '@CMC.select.select@',  
        beforeBlur : function(){}
	});
}

function initToolbar(){
	var endTime = '<%=st%>';
	var startTime = GetDateStr(-7);
}
var entityId;
var cmcId;
var ponId;
var channelIndex;
var selectedIndexs;
var graphData;
function queryCmHisUserPerf(){
	var stTime = startTime.value.format("yyyy-mm-dd HH:MM:ss");
	var eTime = endTime.value.format("yyyy-mm-dd HH:MM:ss");
	if(stTime>eTime){
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
				html: '<b class="orangeTxt">@performance/Tip.timeLimit@</b>'
   		});
		return false;
	}
    entityId = parseInt($('#select_olt').val());
    ponId = parseInt($('#select_pon').val());
    cmcId = parseInt($('#select_cmts').val());
    channelIndex = parseInt($('#select_Cnl').val());
    selectedIndexs = Ext.getCmp("indexSelect").getCheckedValue();
    if(selectedIndexs == null || selectedIndexs.length <= 0){
    	window.parent.showMessageDlg("@COMMON.tip@", "@CMCPE.pleaseChooseMonitorQuota@");
    	return;
    }
    var deviceTypeId = parseInt($('#select_deviceType').val());
    if (EntityType.isCcmtsWithAgentType(deviceTypeId) || deviceTypeId == EntityType.getCmtsType()) {
        entityId = cmcId;
    }
    if (deviceTypeId != 0 && (entityId == -1 || entityId == 0)) {
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
				html: '<b class="orangeTxt">@CMCPE.pleaseChooseDevice@</b>'
   		});
		return false;
    }
    
    selectedQuots = selectedIndexs.split(",");
    $.ajax({
        url: '/cmCpe/showCmHisUserPerf.tv', cache: false, dataType: 'json',
        data: {
            startTime: stTime,
            entityId: entityId,
            ponId: ponId,
            cmcId: cmcId,
            channelIndex: channelIndex,
            endTime: eTime,
            quotas: selectedIndexs
        },
        success: function (json) {
            showUserNumHistory(json, stTime, eTime, true);
        }, error: function () {
        }
    }); 
}
Ext.onReady(function(){
	var w = document.body.clientWidth;
	var h = document.body.clientHeight;
	bulidToolbar();
    createOltCombo();
	setTimeout(queryCmHisUserPerf, 100 )
	document.getElementById("cpuHisPerf").style.display='none';
	document.getElementById("snrHisPerf").style.display='none';
	document.getElementById("flowHisPerf").style.display='none';
	R.snrButton.setDisabled(true);
	R.flowButton.setDisabled(true);
	R.cpuButton.setDisabled(true);
	$('#td_olt').next().andSelf().hide();
    $('#td_pon').next().andSelf().hide();
	var deviceTypePosition = Zeta$('select_deviceType');
	var cctype = EntityType.getCcmtsWithAgentType();
    <% if(uc.hasSupportModule("olt")){%>
	    var optionolt = document.createElement('option');
	    optionolt.value = EntityType.getOltType();
	    optionolt.text = "OLT";
	    try {
	    	deviceTypePosition.add(optionolt, null);
	    } catch(ex) {
	    	deviceTypePosition.add(optionolt);
	    }
	    $('#td_olt').next().andSelf().show();
        $('#td_pon').next().andSelf().show();
    <% }%>
	$.ajax({
	      url:'/entity/loadSubEntityType.tv?type=' + cctype,
	      type:'POST',
	      dateType:'json',
	      success:function(response) {
			var entityTypes = response.entityTypes 
			for(var i = 0; i < entityTypes.length; i++){
				var option = document.createElement('option');
				option.value = entityTypes[i].typeId;
				option.text = entityTypes[i].displayName;
				try {
					deviceTypePosition.add(option, null);
				} catch(ex) {
					deviceTypePosition.add(option);
				}
			}
			
			<% if(uc.hasSupportModule("cmts")){%>
				var optioncmts = document.createElement('option');
				optioncmts.value = EntityType.getCmtsType();
				optioncmts.text = "CMTS";
				try {
					deviceTypePosition.add(optioncmts, null);
				} catch(ex) {
					deviceTypePosition.add(optioncmts);
				}
			<% }%>
	    		  
	      },
	      error:function(entityTypes) {},
	      cache:false
	  });
	
	//重写rangeselector的定位方法，我们想要定位到右上角
	var orgHighchartsRangeSelectorPrototypeRender = Highcharts.RangeSelector.prototype.render;
	Highcharts.RangeSelector.prototype.render = function (min, max) {
	    orgHighchartsRangeSelectorPrototypeRender.apply(this, [min, max]);
	    var leftPosition = this.chart.plotLeft + w - 100,
	        topPosition = this.chart.plotTop-25,
	        space = 2;
	    this.zoomText.attr({
	        x: leftPosition,
	        y: topPosition
	    });
	    leftPosition += this.zoomText.getBBox().width;
	    for (var i = 0; i < this.buttons.length; i++) {
	        this.buttons[i].attr({
	            x: leftPosition,
	            y: topPosition 
	        });
	        leftPosition += this.buttons[i].width + space;
	    }
	};
	
})


/* ==========================================================
 * 杩欐鍐呭鏄〉闈笂鐨勪笅鎷夋浜嬩欢鍙婂唴瀹瑰姞杞?
 * ========================================================== */
function deviceTypeSelChanged() {
    var deviceTypeId = parseInt($('#select_deviceType').val());
    //褰撹澶囩被鍨嬩笅鎷夋鍙戠敓鏀瑰彉鏃讹紝搴斿綋棣栧厛閲嶇疆鍚勪笅鎷夋锛岀劧鍚庡垵濮嬪寲鐩稿簲鐨勪笅鎷夋

    if (!window.selector) {
        createOltCombo();
    }
    selector.setValue(-1);

    document.getElementById("cpuHisPerf").style.display='none';
    document.getElementById("snrHisPerf").style.display='none';
    document.getElementById("flowHisPerf").style.display='none';
    R.snrButton.setDisabled(true);
    R.flowButton.setDisabled(true);
    R.cpuButton.setDisabled(true);

    $('#select_pon').empty().append('<option value="0">@CMC.select.select@</option>');
    $('#select_cmts').empty().append('<option value="0">@CMC.select.select@</option>');
    $('#select_Cnl').empty().append('<option value="0">@CMC.select.select@</option>');

    if (deviceTypeId == 0) {
        $('#td_olt').next().andSelf().hide();
        $('#td_pon').next().andSelf().hide();
        $('#td_cmts').next().andSelf().hide();
        $('#td_Cnl').next().andSelf().hide();
    } else if (deviceTypeId == EntityType.getOltType()) {
        $('#td_olt').next().andSelf().show();
        $('#td_pon').next().andSelf().show();
        $('#td_cmts').next().andSelf().show();
        $('#td_Cnl').next().andSelf().show();
    } else if (EntityType.isCcmtsWithAgentType(deviceTypeId)) {
        $('#td_olt').next().andSelf().hide();
        $('#td_pon').next().andSelf().hide();
        $('#td_cmts').next().andSelf().show();
        $('#td_Cnl').next().andSelf().show();
        //鍒濆鍖朇CMTS涓嬫媺妗?
        $.ajax({
            url: '/cmlist/loadDeviceListByTypeId.tv',
            data:{deviceType:deviceTypeId},
            type: 'POST',
            dataType:'json',
            success:function(response){
                $.each(response, function(index, cmcPair) {
                    $('#select_cmts').append(String.format('<option value="{0}">{1}</option>', cmcPair.entityId, cmcPair.ip));
                });
            },
            error: function() {},
            cache: false,
            complete: function(XHR, TS) {
                XHR = null
            }
        });
    } else if (deviceTypeId == EntityType.getCmtsType()) {
        $('#td_olt').next().andSelf().hide();
        $('#td_pon').next().andSelf().hide();
        $('#td_cmts').next().andSelf().show();
        $('#td_Cnl').next().andSelf().show();
        //鍒濆鍖朇MTS涓嬫媺妗?
        $.ajax({
            url: '/cmlist/loadDeviceListByType.tv',
            data:{deviceType:deviceTypeId},
            type: 'POST',
            dataType:'json',
            success:function(response){
                $.each(response, function(index, cmcPair) {
                    $('#select_cmts').append(String.format('<option value="{0}">{1}</option>', cmcPair.entityId, cmcPair.ip));
                });
            },
            error: function() {},
            cache: false,
            complete: function(XHR, TS) {
                XHR = null
            }
        });
    }
}

function oltSelChanged() {
    var oltEntityId = $('#select_olt').val();
    //棣栧厛閲嶇疆PON/CCMTS/CMTS/涓婅鍙婁笅琛屼俊鎭?
    $('#select_pon').empty().append('<option value="0">@CMC.select.select@</option>');
    $('#select_cmts').empty().append('<option value="0">@CMC.select.select@</option>');
    $('#select_Cnl').empty().append('<option value="0">@CMC.select.select@</option>');
    if (oltEntityId == 0 || oltEntityId == -1 || oltEntityId == "") {
        return;
    }
    document.getElementById("cpuHisPerf").style.display='none';
    document.getElementById("snrHisPerf").style.display='none';
    document.getElementById("flowHisPerf").style.display='none';
    R.snrButton.setDisabled(true);
    R.flowButton.setDisabled(true);
    R.cpuButton.setDisabled(true);
    //鍔ㄦ€佽幏鍙栬OLT涓嬬殑PON鍙ｄ俊鎭姞杞界粰PON鍙ｄ笅鎷夋
    $.ajax({
        url: '/eponcmlist/loadPonOfOlt.tv',
        data: {
            entityId: oltEntityId
        },
        type: 'POST',
        dataType: 'json',
        success: function(ponList) {
            $('#select_pon').empty().append('<option value="0">@CMC.select.select@</option>');
            //鍦ㄨ繘琛屾彃鍏ュ墠锛岄渶瑕佸pon鍙ｈ繘琛屾帓搴?
            ponList.sort(function(a, b) {
                if (a.ponIndex > b.ponIndex) {
                    return 1;
                } else if (a.ponIndex == b.ponIndex) {
                    return 0;
                } else {
                    return -1;
                }
            });
            $.each(ponList, function(index, pon) {
                $('#select_pon').append(String.format('<option value="{0}">{1}</option>', pon.ponId, pon.ponIndex));
            });
        },
        error: function(ponList) {},
        cache: false,
        complete: function(XHR, TS) {
            XHR = null
        }
    });
}

function ponSelChanged() {
    var deviceTypeId = parseInt($('#select_deviceType').val());
    var oltEntityId = $('#select_olt').val();
    var ponId = $('#select_pon').val();
    document.getElementById("cpuHisPerf").style.display='none';
    document.getElementById("snrHisPerf").style.display='none';
    document.getElementById("flowHisPerf").style.display='none';
    R.snrButton.setDisabled(true);
    R.flowButton.setDisabled(true);
    R.cpuButton.setDisabled(true);
    //棣栧厛閲嶇疆CCMTS/CMTS/涓婅鍙婁笅琛屼俊鎭?
    $('#select_cmts').empty().append('<option value="0">@CMC.select.select@</option>');
    $('#select_Cnl').empty().append('<option value="0">@CMC.select.select@</option>');
    if (oltEntityId == 0 || ponId == 0) {
        return;
    }
    //鍔ㄦ€佽幏鍙栬PON鍙ｄ笅鐨凜CMTS鏁版嵁鍔犺浇缁機CMTS涓嬫媺妗?
    $.ajax({
        url: '/cmlist/loadCcmtsOfPon.tv',
        data: {
            entityId: oltEntityId,
            ponId: ponId,
            deviceType:deviceTypeId
        },
        type: 'POST',
        dataType: 'json',
        success: function(ccmtsList) {
            $('#select_cmts').empty().append('<option value="0">@CMC.select.select@</option>');
            $.each(ccmtsList, function(index, ccmts) {
                $('#select_cmts').append(String.format('<option value="{0}">{1}</option>', ccmts.cmcId, ccmts.name));
            });
        },
        error: function(ccmtsList) {},
        cache: false,
        complete: function(XHR, TS) {
            XHR = null
        }
    });
}

function cmtsSelChanged() {
    var cmtsId = $('#select_cmts').val();
    //棣栧厛閲嶇疆CCMTS/涓婅鍙婁笅琛屼俊鎭?
    $('#select_Cnl').empty().append('<option value="0">@CMC.select.select@</option>');

    var deviceTypeId = parseInt($('#select_deviceType').val());
    if(cmtsId > 0){
        if (!EntityType.isCmtsType(deviceTypeId)) {
            R.cpuButton.setDisabled(false);
        }
    }else{
        document.getElementById("cpuHisPerf").style.display='none';
        document.getElementById("snrHisPerf").style.display='none';
        document.getElementById("flowHisPerf").style.display='none';
        R.snrButton.setDisabled(true);
        R.flowButton.setDisabled(true);
        R.cpuButton.setDisabled(true);
    }
    if (cmtsId == 0) {
        return;
    }
    //鍔ㄦ€佽幏鍙朇MTS鐨勪笂涓嬭淇℃伅
    $.ajax({
        url: '/cmlist/loadUpDownChlOfCmts.tv',
        data: {
            cmtsId: cmtsId
        },
        type: 'POST',
        dataType: 'json',
        success: function(json) {
            $.each(json.cmtsUpChannelList, function(index, upChannel) {
                $('#select_Cnl').append(String.format('<option value="{0}" chType="{1}">{2}</option>', upChannel.channelIndex,'0', upChannel.ifDescr));
            });
            $.each(json.cmtsDownChannelList, function(index, downChannel) {
                $('#select_Cnl').append(String.format('<option value="{0}" chType="{1}">{2}</option>', downChannel.channelIndex,'1', downChannel.ifDescr));
            });
        },
        error: function(ccmtsList) {},
        cache: false,
        complete: function(XHR, TS) {
            XHR = null
        }
    });
}

function channelSelChanged() {
    var channelIndex = $('#select_Cnl').val();
    var chType = $('#select_Cnl option:selected:first').attr("chType");
    var deviceTypeId = parseInt($('#select_deviceType').val());
    if(channelIndex > 0){
        if (chType == '0') {
            if (!EntityType.isCmtsType(deviceTypeId)) {
                R.snrButton.setDisabled(false);
            }
        }else{
            R.snrButton.setDisabled(true);
            document.getElementById("snrHisPerf").style.display='none';
        }
        if (!EntityType.isCmtsType(deviceTypeId)) {
            R.flowButton.setDisabled(false);
        }
    }else{
        R.snrButton.setDisabled(true);
        document.getElementById("snrHisPerf").style.display='none';
        R.flowButton.setDisabled(true);
        document.getElementById("flowHisPerf").style.display='none';
    }
}
function createOltCombo() {
    window.selector = new NetworkNodeSelector({
        id: 'select_olt',
        renderTo: "oltContainer",
        //value : window["entityId"], //@璧嬪€肩殑鏂瑰紡涓€锛氶厤缃粯璁ゅ€?
        autoLayout: true,
        listeners: {
            selectChange: oltSelChanged
        }
    });
    //@璧嬪€兼柟娉曚簩锛? 璋冪敤 setValue({value}). eg : selector.setValue( window["entityId"] );
}
</script>
<style type="text/css">
    .leftFloatUlTemp{ float:left; clear:both;}
    .leftFloatUlTemp li{ float:left; margin:0px 5px 6px 0px;}

</style>
</head>
<body >
<div id="query-container" class="edge10">
    <div id="advance-toolbar-div" >

            <ul class="leftFloatUlTemp">
                <li class="rightBlueTxt pT5 w60">@CCMTS.entityStyle@:</li>
                <li>
                    <select class="normalSel w100" id="select_deviceType" onchange="deviceTypeSelChanged()">
                        <option value="0">@CMC.select.select@</option>
                    </select>
                </li>
                <li class="rightBlueTxt pT5 w40" id="td_olt">OLT:</li>
                <li width="90">
                    <div id="oltContainer" class="w120"></div>
                </li>
                <li class="rightBlueTxt pT5 w40" id="td_pon">PON:</li>
                <li>
                    <select class="normalSel w60" id="select_pon" onchange="ponSelChanged()">
                        <option value="0">@CMC.select.select@</option>
                    </select>
                </li>
                <li class="rightBlueTxt pT5 w60" id="td_ccmts" width="50">CMTS:</li>
                <li>
                    <select class="normalSel w120" id="select_cmts" onchange="cmtsSelChanged()">
                        <option value="0">@CMC.select.select@</option>
                    </select>
                </li>
                <li class="rightBlueTxt w50 pT5" id="td_Cnl">@CHANNEL.channel@:</li>
                <li class="w70">
                    <select class="normalSel w70" id="select_Cnl" onchange="channelSelChanged()">
                        <option value="0">@CMC.select.select@</option>
                    </select>
                </li>
            </ul>
            <ul class="leftFloatUl clearBoth ">
                <li class="rightBlueTxt w60 pT5">@CMCPE.monitorQuota@:</li>
                <li><div id="indexSelectDiv"/>
                <li class="rightBlueTxt w60 pT5">@PERF.mo.startTime@:</li>
                <li class="w160">
                    <div id="startTime" class="w160"></div>
                </li>
                <li class="rightBlueTxt w60 pT5">@entity.alert.searchEndTime@:</li>
                <li class="w160">
                    <div id="endTime"></div>
                </li>
                <li>
                	<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
                </li>
            </ul>
    </div>
</div>
	
<div style="height: 10px; overflow:hidden; clear:both;"></div>
<div id="container" style="margin-right: 50px;margin-left: 50px;border:1px solid #CCC;" ></div>
<div align="right" style="padding-top:10px;margin-right: 50px;">
		<Zeta:ButtonGroup>
			<Zeta:Button onclick="showCpuHisPerf()" id="cpuButton" >@CMCPE.showCpu@</Zeta:Button>
			<Zeta:Button onclick="showSnrHisPerf()" id="snrButton" >@CMCPE.showSnr@</Zeta:Button>
			<Zeta:Button onclick="showFlowHisPerf()" id="flowButton" >@CMCPE.showFlow@</Zeta:Button>
		</Zeta:ButtonGroup> 
	</div>
<div id="cpuHisPerf" class="perfItem"></div>
<div id="snrHisPerf" class="perfItem"></div>
<div id="flowHisPerf" class="perfItem"></div>
</body>
</Zeta:HTML>