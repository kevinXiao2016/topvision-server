var upRQOldVersionTemplate = '<thead><tr><th  colspan="4">@CCMTS.rfModuleStatus@</th><th colspan="1">@text.signalQuality@</th></tr><tr><th>@CHANNEL.channel@</th><th>@CMC.label.bandwidth@(MHz)</th><th>@cm.Frequency@(MHz)</th><th>@cm.SendPower@(@{unitConfigConstant.elecLevelUnit}@)</th><th>SNR(dB)</th></tr></thead><tbody style="display:none;"></tbody><tbody><tr><td colspan="5" align="center"><span class="tableTdLoading">@COMMON.loading@</span></td></tr></tbody>';
var upTdRQOldVersionTemplate = '<tr><td align="center" class="tdNum"></td><td align="center" class="docsIfUpChannelWidthForUnit"></td><td align="center" class="docsIfUpChannelFrequencyForUnit"></td><td align="center" class="docsIfCmStatusTxPowerForUnit"></td><td align="center" class="cmUsStatusSignalNoiseForUnit"></td></tr>';
var upRealTimeOldVersionTemplate = '<thead><tr><th  colspan="5">@CCMTS.rfModuleStatus@</th><th colspan="1">@text.signalQuality@</th></tr><tr><th>@CHANNEL.channel@</th><th>@CMC.label.bandwidth@(MHz)</th><th>@CHANNEL.width@(Mbps)</th><th>@cm.Frequency@(MHz)</th><th>@cm.SendPower@(@{unitConfigConstant.elecLevelUnit}@)</th><th>SNR(dB)</th></tr></thead><tbody style="display:none;"></tbody><tbody><tr><td colspan="6" align="center"><span class="tableTdLoading">@COMMON.loading@</span></td></tr></tbody>';
var upTdRealTimeOldVersionTemplate = '<tr><td align="center" class="tdNum"></td><td align="center" class="docsIfUpChannelWidthForUnit"></td><td align="center" class="upIfSpeedForUnit"></td><td align="center" class="docsIfUpChannelFrequencyForUnit"></td><td align="center" class="docsIfCmStatusTxPowerForUnit"></td><td align="center" class="cmUsStatusSignalNoiseForUnit"></td></tr>';
var upRQNewVersionTemplate = '<thead><tr><th  colspan="5">@CCMTS.rfModuleStatus@</th><th colspan="2">@text.signalQuality@</th></tr><tr><th>@CHANNEL.channel@</th><th>@CMC.label.bandwidth@(MHz)</th><th>@CHANNEL.usBps@</th><th>@cm.Frequency@(MHz)</th><th>@cm.SendPower@(@{unitConfigConstant.elecLevelUnit}@)</th><th>SNR(dB)</th><th>@CHANNEL.usErrorRation@</th></tr></thead><tbody style="display:none;"></tbody><tbody><tr><td colspan="7" align="center"><span class="tableTdLoading">@COMMON.loading@</span></td></tr></tbody>';
var upTdRQNewVersionTemplate = '<tr><td align="center" class="tdNum"></td><td align="center" class="docsIfUpChannelWidthForUnit"></td><td align="center" class="usBpsUnit"></td><td align="center" class="docsIfUpChannelFrequencyForUnit"></td><td align="center" class="docsIfCmStatusTxPowerForUnit"></td><td align="center" class="cmUsStatusSignalNoiseForUnit"></td><td align="center" class="usErrorRationUnit"></td></tr>';
var upRealTimeNewVersionTemplate = '<thead><tr><th  colspan="5">@CCMTS.rfModuleStatus@</th><th colspan="1">@text.signalQuality@</th></tr><tr><th>@CHANNEL.channel@</th><th>@CMC.label.bandwidth@(MHz)</th><th>@CHANNEL.width@(Mbps)</th><th>@cm.Frequency@(MHz)</th><th>@cm.SendPower@(@{unitConfigConstant.elecLevelUnit}@)</th><th>SNR(dB)</th></tr></thead><tbody style="display:none;"></tbody><tbody><tr><td colspan="6" align="center"><span class="tableTdLoading">@COMMON.loading@</span></td></tr></tbody>';
var upTdRealTimeNewVersionTemplate = '<tr><td align="center" class="tdNum"></td><td align="center" class="docsIfUpChannelWidthForUnit"></td><td align="center" class="upIfSpeedForUnit"></td><td align="center" class="docsIfUpChannelFrequencyForUnit"></td><td align="center" class="docsIfCmStatusTxPowerForUnit"></td><td align="center" class="cmUsStatusSignalNoiseForUnit"></td></tr>';
var upOtherCmtsTmplate = '<thead><tr><th  colspan="5">@CCMTS.rfModuleStatus@</th><th colspan="1">@text.signalQuality@</th></tr><tr><th>@CHANNEL.channel@</th><th>@CMC.label.bandwidth@(MHz)</th><th>@CHANNEL.width@(Mbps)</th><th>@cm.Frequency@(MHz)</th><th>@cm.SendPower@(@{unitConfigConstant.elecLevelUnit}@)</th><th>SNR(dB)</th></tr></thead><tbody style="display:none;"></tbody><tbody><tr><td colspan="6" align="center"><span class="tableTdLoading">@COMMON.loading@</span></td></tr></tbody>';
var upTdOtherCmtsTmplate = '<tr><td align="center" class="tdNum"></td><td align="center" class="docsIfUpChannelWidthForUnit"></td><td align="center" class="upIfSpeedForUnit"></td><td align="center" class="docsIfUpChannelFrequencyForUnit"></td><td align="center" class="docsIfCmStatusTxPowerForUnit"></td><td align="center" class="cmUsStatusSignalNoiseForUnit"></td></tr>';

var downRQOldVersionTemplate = '<thead><tr><th colspan="5">@CCMTS.rfModuleStatus@</th><th colspan="1">@text.signalQuality@</th></tr><tr><th>@CHANNEL.channel@</th><th>@CMC.label.bandwidth@(MHz)</th><th>@cm.Frequency@(MHz)</th><th>@cm.ReceivePower@(@{unitConfigConstant.elecLevelUnit}@)</th><th>@CHANNEL.modulationType@</th><th>SNR(dB)</th></tr></thead><tbody style="display:none;"></tbody><tbody><tr><td colspan="6" align="center"><span class="tableTdLoading">@COMMON.loading@</span></td></tr></tbody>';
var downTdRQOldVersionTemplate = '<tr> <td  align="center" class="tdNum"></td> <td  align="center" class="docsIfDownChannelWidthForUnit"></td> <td  align="center" class="docsIfDownChannelFrequencyForUnit"></td> <td  align="center" class="docsIfDownChannelPowerForUnit"></td> <td  align="center" class="docsIfDownChannelModulationForUnit"></td> <td  align="center" class="docsIfSigQSignalNoiseForUnit"></td> </tr>';
var downRealTimeOldVersionTemplate = ' <thead> <tr>  <th colspan="6">@CCMTS.rfModuleStatus@</th><th colspan="1">@text.signalQuality@</th> </tr> <tr> <th>@CHANNEL.channel@</th> <th>@CMC.label.bandwidth@(MHz)</th> <th>@CHANNEL.width@(Mbps)</th> <th>@cm.Frequency@(MHz)</th> <th>@cm.ReceivePower@(@{unitConfigConstant.elecLevelUnit}@)</th> <th>@CHANNEL.modulationType@</th> <th>SNR(dB)</th> </tr> </thead> <tbody style="display:none;"></tbody><tbody><tr><td colspan="7" align="center"><span class="tableTdLoading">@COMMON.loading@</span></td></tr></tbody>';
var downTdRealTimeOldVersionTemplate = '<tr> <td  align="center" class="tdNum"></td> <td  align="center" class="docsIfDownChannelWidthForUnit"></td> <td  align="center" class="downIfSpeedForUnit"></td> <td  align="center" class="docsIfDownChannelFrequencyForUnit"></td> <td  align="center" class="docsIfDownChannelPowerForUnit"></td> <td  align="center" class="docsIfDownChannelModulationForUnit"></td> <td  align="center" class="docsIfSigQSignalNoiseForUnit"></td> </tr> ';
var downRQNewVersionTemplate = '<thead><tr><th colspan="6">@CCMTS.rfModuleStatus@</th><th colspan="2">@text.signalQuality@</th></tr><tr><th>@CHANNEL.channel@</th><th>@CMC.label.bandwidth@(MHz)</th><th>@CHANNEL.dsBps@</th><th>@cm.Frequency@(MHz)</th><th>@cm.ReceivePower@(@{unitConfigConstant.elecLevelUnit}@)</th><th>@CHANNEL.modulationType@</th><th>SNR(dB)</th><th>@CHANNEL.dsErrorRation@</th></tr></thead><tbody style="display:none;"></tbody><tbody><tr><td colspan="8" align="center"><span class="tableTdLoading">@COMMON.loading@</span></td></tr></tbody>';
var downTdRQNewVersionTemplate = '<tr> <td  align="center" class="tdNum"></td> <td  align="center" class="docsIfDownChannelWidthForUnit"></td> <td  align="center" class="dsBpsUnit"></td> <td  align="center" class="docsIfDownChannelFrequencyForUnit"></td> <td  align="center" class="docsIfDownChannelPowerForUnit"></td> <td  align="center" class="docsIfDownChannelModulationForUnit"></td> <td  align="center" class="docsIfSigQSignalNoiseForUnit"></td> <td  align="center" class="dsErrorRationUnit"></td> </tr>';
var downRealTimeNewVersionTemplate = '<thead><tr><th colspan="6">@CCMTS.rfModuleStatus@</th><th colspan="1">@text.signalQuality@</th></tr><tr><th>@CHANNEL.channel@</th><th>@CMC.label.bandwidth@(MHz)</th><th>@CHANNEL.width@(Mbps)</th><th>@cm.Frequency@(MHz)</th><th>@cm.ReceivePower@(@{unitConfigConstant.elecLevelUnit}@)</th><th>@CHANNEL.modulationType@</th><th>SNR(dB)</th></tr></thead><tbody style="display:none;"></tbody><tbody><tr><td colspan="7" align="center"><span class="tableTdLoading">@COMMON.loading@</span></td></tr></tbody>';
var downTdRealTimeNewVersionTemplate = '<tr> <td  align="center" class="tdNum"></td> <td  align="center" class="docsIfDownChannelWidthForUnit"></td> <td  align="center" class="downIfSpeedForUnit"></td> <td  align="center" class="docsIfDownChannelFrequencyForUnit"></td> <td  align="center" class="docsIfDownChannelPowerForUnit"></td> <td  align="center" class="docsIfDownChannelModulationForUnit"></td> <td  align="center" class="docsIfSigQSignalNoiseForUnit"></td> </tr> ';
var downOtherCmtsTemplate = '<thead><tr><th colspan="6">@CCMTS.rfModuleStatus@</th><th colspan="1">@text.signalQuality@</th></tr><tr><th>@CHANNEL.channel@</th><th>@CMC.label.bandwidth@(MHz)</th><th>@CHANNEL.width@(Mbps)</th><th>@cm.Frequency@(MHz)</th><th>@cm.ReceivePower@(@{unitConfigConstant.elecLevelUnit}@)</th><th>@CHANNEL.modulationType@</th><th>SNR(dB)</th></tr></thead><tbody style="display:none;"></tbody><tbody><tr><td colspan="7" align="center"><span class="tableTdLoading">@COMMON.loading@</span></td></tr></tbody>';
var downTdOtherCmtsTemplate = '<tr> <td  align="center" class="tdNum"></td> <td  align="center" class="docsIfDownChannelWidthForUnit"></td> <td  align="center" class="downIfSpeedForUnit"></td> <td  align="center" class="docsIfDownChannelFrequencyForUnit"></td> <td  align="center" class="docsIfDownChannelPowerForUnit"></td> <td  align="center" class="docsIfDownChannelModulationForUnit"></td> <td  align="center" class="docsIfSigQSignalNoiseForUnit"></td> </tr> ';
//显示table;
function showTableOuter(para){
	if(typeof(para) == 'string'){
		$("#"+para).removeClass('displayNone').attr({'class':'relativeDiv'})
		.find("table tbody").css({display:"none"}).end()
		.find("table tbody:eq(1)").css({display:''});
	}else if(para instanceof Array){
		$.each(para,function(i){
			$("#"+para[i]).removeClass('displayNone').attr({'class':'relativeDiv'})
				.find("table tbody").css({display:"none"}).end()
				.find("table tbody:eq(1)").css({display:''});
		})
	}
};
//显示table的tbody的第0个;
function showFirstTbody(para){
	var $tb = $("#"+para).find("tbody");
	$tb.css({display : "none"});
	$tb.eq(0).css({display:""});
};
//显示table的loading,也就是tbody的第1个;
function showLoadingTbody(para){
	var $tb = $("#"+para).find("tbody");
	$tb.css({display : "none"});
	$tb.eq(1).css({display:""});
};
//控制按钮是否可用;
function disabledAlink(btnId, enabled){
	$("#"+btnId).attr({disabled : enabled});
}
//加载cm属性、上行信道、下行信道这三项，因为他们在同一张表中，所以一次加载他们叁;
function loadCmAttribute(json,cmCpe){
    otherCmts = json.cmStatus.otherCmts;
    versionSupport = json.cmStatus.versionSupport;
	//if( !bar.loading ){return;}
	showFirstTbody("cmPropertyTable");
	
	/*$("#cmPropertyTable tbody, #CmUpChannel tbody, #CmDownChannel tbody").css({display:"none"});
	$("#cmPropertyTable tbody:eq(0), #CmUpChannel tbody:eq(0), #CmDownChannel tbody:eq(0)").css({display:""});*/
	if ( queryMode != 2 && (json.cmStatus == null || !json.cmStatus.snmpCheck || !json.cmStatus.ping)) {
		bar.loadEnd("nm3kProgressFail","#f00");
		icmpUnreachable(json);
    }  else {
    	bar.loadEnd("nm3kProgressSuccess","#03a9f5");
    	//CM 属性
    	createCmProTable();
    	if(queryMode == 2){
            var str = '<tr><td class="rightBlueTxt">@CMCPE.pingTest@:</td><td id="cmping"></td></tr>' ;
            if (!otherCmts && versionSupport) {
                str = str + '<tr><td class="rightBlueTxt">@CMC.label.rebootcount@:</td><td id="rebootcount"></td></tr>' +
                    '<tr><td class="rightBlueTxt">@CM.sysUpTime@:</td><td id="cmuptime"></td></tr>' +
                '<tr><td class="rightBlueTxt">@CMC.label.description@:</td><td id="description"></td></tr>';
            }
            $("#cmPropertyTable tbody:eq(0)").html(str);
            $("#cmping").text(renderPreState(json.cmStatus.statusValue));
            if (json.cmStatus.remoteQueryState) {
                $("#cmuptime").text( getString(json.cmStatus,"sysUpTimeToString"));
                $("#rebootcount").text(getString(json.cmStatus,"docsIfCmStatusResets"));
                $("#description").text(getString(json.cmStatus,"sysDescr"));
            }
    	}else{
	        $("#cmping").text("@CMCPE.deviceOnLine@");
	        $("#cmsn").text( getString(json.cmStatus,"docsDevSerialNumber"));
	        $("#cmregmode").text( getString(json.cmStatus,"docsIfDocsisBaseCapabilityForUnit"));
	        $("#cmsoftware").text( getString(json.cmStatus,"docsDevSwFilename"));
	        $("#cmuptime").text( getString(json.cmStatus,"sysUpTimeToString"));
	        $("#configurationfilename").text( getString(json.cmStatus,"docsDevServerConfigFile"));
	        $("#cmntp").text( getString(json.cmStatus,"docsDevServerTime"));
	        $("#cmtftp").text( getString(json.cmStatus,"docsDevServerTftp"));
	        $("#cmdhcp").text( getString(json.cmStatus,"docsDevServerDhcp"));
            $("#rebootcount").text(getString(json.cmStatus,"docsIfCmStatusResets"));
            $("#description").text(getString(json.cmStatus,"sysDescr"));
    	}
        /*if ($("#label_cmPerp").hasClass("flagClose")) {
            $("#label_cmPerp").trigger("click");
        }*/

        //上行信道
        var upChannelId = new Array();
        var downChannelId = new Array();
        var upspmap = new Object();
        var upxhzlmap = new Object();
        var downspmap = new Object();
        var downxhzlmap = new Object();
        $.each(json.cmStatus.docsIfUpstreamChannelList, function(i, n) {
            upChannelId[i] = n.docsIfUpChannelId;
            upspmap[n.docsIfUpChannelId] = n;
        });
        $.each(json.cmStatus.docsIf3CmtsCmUsStatusList, function(i, n) {
            upxhzlmap[n.upChannelId] = n;
        });
        $.each(json.cmStatus.docsIfDownstreamChannelList, function(i, n) {
            downChannelId[i] = n.docsIfDownChannelId;
            downspmap[n.docsIfDownChannelId] = n;
        });
        $.each(json.cmStatus.docsIfSignalQualityList, function(i, n) {
            downxhzlmap[n.downChanelId] = n;
        });

        //up thead
        $("#CmUpChannel").empty();
        //down thead
        $("#CmDownChannel").empty();
        if (otherCmts) {
            //up thead
            $("#CmUpChannel").append(upOtherCmtsTmplate);
            //up tbody
            $.each(upChannelId, function(i, n) {
                var temp = $(upTdOtherCmtsTmplate);
                $(".tdNum",temp).text(n);
                $(".docsIfUpChannelWidthForUnit",temp).text(getString(upspmap[n],"docsIfUpChannelWidthForUnit"));
                $(".upIfSpeedForUnit",temp).text(getString(upspmap[n],"upIfSpeedForUnit"));
                $(".docsIfUpChannelFrequencyForUnit",temp).text(getString(upspmap[n],"docsIfUpChannelFrequencyForUnit"));
                $(".docsIfCmStatusTxPowerForUnit",temp).text(getString(upspmap[n],"docsIfCmStatusTxPowerForUnit"));
                $(".cmUsStatusSignalNoiseForUnit",temp).text(getString(upxhzlmap[n],"cmUsStatusSignalNoiseForUnit"));
                $("#CmUpChannel>tbody:eq(0)").append(temp);
            });
            //down thead
            $("#CmDownChannel").append(downOtherCmtsTemplate);
            //down tbody
            $.each(downChannelId, function(i, n) {
                var temp = $(downTdOtherCmtsTemplate);
                $(".tdNum",temp).text(n);
                $(".docsIfDownChannelWidthForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelWidthForUnit"));
                $(".downIfSpeedForUnit",temp).text(getString(downspmap[n],"downIfSpeedForUnit"));
                $(".docsIfDownChannelFrequencyForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelFrequencyForUnit"));
                $(".docsIfDownChannelPowerForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelPowerForUnit"));
                $(".docsIfDownChannelModulationForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelModulationForUnit"));
                $(".docsIfSigQSignalNoiseForUnit",temp).text(getString(downxhzlmap[n],"docsIfSigQSignalNoiseForUnit"));
                $("#CmDownChannel>tbody:eq(0)").append(temp);
            });
        } else {
            if (queryMode == 2) {
                if (versionSupport) {
                    //up thead
                    $("#CmUpChannel").append(upRQNewVersionTemplate);
                    //up tbody
                    $.each(upChannelId, function(i, n) {
                        var temp = $(upTdRQNewVersionTemplate);
                        $(".tdNum",temp).text(n);
                        $(".docsIfUpChannelWidthForUnit",temp).text(getString(upspmap[n],"docsIfUpChannelWidthForUnit"));
                        $(".usBpsUnit",temp).text(getString(upspmap[n],"usBpsUnit"));
                        $(".docsIfUpChannelFrequencyForUnit",temp).text(getString(upspmap[n],"docsIfUpChannelFrequencyForUnit"));
                        $(".docsIfCmStatusTxPowerForUnit",temp).text(getString(upspmap[n],"docsIfCmStatusTxPowerForUnit"));
                        $(".cmUsStatusSignalNoiseForUnit",temp).text(getString(upxhzlmap[n],"cmUsStatusSignalNoiseForUnit"));
                        $(".usErrorRationUnit",temp).text(getString(upspmap[n],"usErrorRationUnit"));
                        $("#CmUpChannel>tbody:eq(0)").append(temp);
                    });
                    //down thead
                    $("#CmDownChannel").append(downRQNewVersionTemplate);
                    //down tbody
                    $.each(downChannelId, function(i, n) {
                        var temp = $(downTdRQNewVersionTemplate);
                        $(".tdNum",temp).text(n);
                        $(".docsIfDownChannelWidthForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelWidthForUnit"));
                        $(".dsBpsUnit",temp).text(getString(downspmap[n],"dsBpsUnit"));
                        $(".docsIfDownChannelFrequencyForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelFrequencyForUnit"));
                        $(".docsIfDownChannelPowerForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelPowerForUnit"));
                        $(".docsIfDownChannelModulationForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelModulationForUnit"));
                        $(".docsIfSigQSignalNoiseForUnit",temp).text(getString(downxhzlmap[n],"docsIfSigQSignalNoiseForUnit"));
                        $(".dsErrorRationUnit",temp).text(getString(downspmap[n],"dsErrorRationUnit"));
                        $("#CmDownChannel>tbody:eq(0)").append(temp);
                    });
                } else {
                    //up thead
                    $("#CmUpChannel").append(upRQOldVersionTemplate);
                    //up tbody
                    $.each(upChannelId, function(i, n) {
                        var temp = $(upTdRQOldVersionTemplate);
                        $(".tdNum",temp).text(n);
                        $(".docsIfUpChannelWidthForUnit",temp).text(getString(upspmap[n],"docsIfUpChannelWidthForUnit"));
                        $(".docsIfUpChannelFrequencyForUnit",temp).text(getString(upspmap[n],"docsIfUpChannelFrequencyForUnit"));
                        $(".docsIfCmStatusTxPowerForUnit",temp).text(getString(upspmap[n],"docsIfCmStatusTxPowerForUnit"));
                        $(".cmUsStatusSignalNoiseForUnit",temp).text(getString(upxhzlmap[n],"cmUsStatusSignalNoiseForUnit"));
                        $("#CmUpChannel>tbody:eq(0)").append(temp);
                    });
                    //down thead
                    $("#CmDownChannel").append(downRQOldVersionTemplate);
                    //down tbody
                    $.each(downChannelId, function(i, n) {
                        var temp = $(downTdRQOldVersionTemplate);
                        $(".tdNum",temp).text(n);
                        $(".docsIfDownChannelWidthForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelWidthForUnit"));
                        $(".docsIfDownChannelFrequencyForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelFrequencyForUnit"));
                        $(".docsIfDownChannelPowerForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelPowerForUnit"));
                        $(".docsIfDownChannelModulationForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelModulationForUnit"));
                        $(".docsIfSigQSignalNoiseForUnit",temp).text(getString(downxhzlmap[n],"docsIfSigQSignalNoiseForUnit"));
                        $("#CmDownChannel>tbody:eq(0)").append(temp);
                    });
                }
            } else {
                if (versionSupport) {
                    //up thead
                    $("#CmUpChannel").append(upRealTimeNewVersionTemplate);
                    //up tbody
                    $.each(upChannelId, function(i, n) {
                        var temp = $(upTdRealTimeNewVersionTemplate);
                        $(".tdNum",temp).text(n);
                        $(".docsIfUpChannelWidthForUnit",temp).text(getString(upspmap[n],"docsIfUpChannelWidthForUnit"));
                        $(".upIfSpeedForUnit",temp).text(getString(upspmap[n],"upIfSpeedForUnit"));
                        $(".docsIfUpChannelFrequencyForUnit",temp).text(getString(upspmap[n],"docsIfUpChannelFrequencyForUnit"));
                        $(".docsIfCmStatusTxPowerForUnit",temp).text(getString(upspmap[n],"docsIfCmStatusTxPowerForUnit"));
                        $(".cmUsStatusSignalNoiseForUnit",temp).text(getString(upxhzlmap[n],"cmUsStatusSignalNoiseForUnit"));
                        $("#CmUpChannel>tbody:eq(0)").append(temp);
                    });
                    //down thead
                    $("#CmDownChannel").append(downRealTimeNewVersionTemplate);
                    //down tbody
                    $.each(downChannelId, function(i, n) {
                        var temp = $(downTdRealTimeNewVersionTemplate);
                        $(".tdNum",temp).text(n);
                        $(".docsIfDownChannelWidthForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelWidthForUnit"));
                        $(".downIfSpeedForUnit",temp).text(getString(downspmap[n],"downIfSpeedForUnit"));
                        $(".docsIfDownChannelFrequencyForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelFrequencyForUnit"));
                        $(".docsIfDownChannelPowerForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelPowerForUnit"));
                        $(".docsIfDownChannelModulationForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelModulationForUnit"));
                        $(".docsIfSigQSignalNoiseForUnit",temp).text(getString(downxhzlmap[n],"docsIfSigQSignalNoiseForUnit"));
                        $("#CmDownChannel>tbody:eq(0)").append(temp);
                    });
                } else {
                    //up thead
                    $("#CmUpChannel").append(upRealTimeOldVersionTemplate);
                    //up tbody
                    $.each(upChannelId, function(i, n) {
                        var temp = $(upTdRealTimeOldVersionTemplate);
                        $(".tdNum",temp).text(n);
                        $(".docsIfUpChannelWidthForUnit",temp).text(getString(upspmap[n],"docsIfUpChannelWidthForUnit"));
                        $(".upIfSpeedForUnit",temp).text(getString(upspmap[n],"upIfSpeedForUnit"));
                        $(".docsIfUpChannelFrequencyForUnit",temp).text(getString(upspmap[n],"docsIfUpChannelFrequencyForUnit"));
                        $(".docsIfCmStatusTxPowerForUnit",temp).text(getString(upspmap[n],"docsIfCmStatusTxPowerForUnit"));
                        $(".cmUsStatusSignalNoiseForUnit",temp).text(getString(upxhzlmap[n],"cmUsStatusSignalNoiseForUnit"));
                        $("#CmUpChannel>tbody:eq(0)").append(temp);
                    });
                    //down thead
                    $("#CmDownChannel").append(downRealTimeOldVersionTemplate);
                    //down tbody
                    $.each(downChannelId, function(i, n) {
                        var temp = $(downTdRealTimeOldVersionTemplate);
                        $(".tdNum",temp).text(n);
                        $(".docsIfDownChannelWidthForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelWidthForUnit"));
                        $(".docsIfDownChannelFrequencyForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelFrequencyForUnit"));
                        $(".docsIfDownChannelPowerForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelPowerForUnit"));
                        $(".docsIfDownChannelModulationForUnit",temp).text(getString(downspmap[n],"docsIfDownChannelModulationForUnit"));
                        $(".docsIfSigQSignalNoiseForUnit",temp).text(getString(downxhzlmap[n],"docsIfSigQSignalNoiseForUnit"));
                        $("#CmDownChannel>tbody:eq(0)").append(temp);
                    });
                }
            }
        }
        showFirstTbody("CmUpChannel");
        showFirstTbody("CmDownChannel");

        if (!otherCmts && versionSupport) {
            $("#cmif>tbody:eq(0)").empty();
            showFirstTbody("cmif");
            //下行信道
            $.each(json.cmStatus.cmIfTables, function(i, n) {
                var htmlString = "<tr>" +
                    "<td  align='center'>" + getString(n,"ifIndex") + "</td>" +
                    "<td  align='center'>" +  getString(n,"portDesc") + "</td>" +
                    "<td  align='center'>" +  getInterfaceStatus(getString(n,"portStatus")) + "</td>" +
                    "</tr>";
                $("#cmif>tbody:eq(0)").append(htmlString);
            });
            $("#fdbAddressRefreshBtn").show();
        } else {
            $("#cmif>tbody:eq(0)").empty();
            showFirstTbody("cmif");
            var htmlString = "<tr><td colspan='3' align='center'><span>@CM.Unsupport@</span></td></tr>";
            $("#cmif>tbody:eq(0)").append(htmlString);
            $("#fdbAddressRefreshBtn").hide();
        }
        /*if ($("#label_downChannel").hasClass("flagClose")) {
            $("#label_downChannel").trigger("click");
        }*/
    }
};//end loadCmAttribute; 

//加载cpe信息;
function loadCmSF(json,cmCpe){
    showFirstTbody("cmsf");
    if (!otherCmts && queryMode == 2 && versionSupport) {
        try {
            $("#cmsf>tbody:eq(0)").empty();
            var html = '';
            $.each(json.cmsf,function(i,cmsf){
                cmcHtml =
                    "<tr id='cmsf-"+cmsf.serviceFlowId+"'>" +
                        "<td align='center' class='sfid'>" + cmsf.serviceFlowId + "</td>" +
                        "<td align='center' class='sftype'>" + getServiceFlowType(cmsf.serviceFlowType) + "</td>" +
                        "<td align='center' class='sfrate'>" + cmsf.serviceFlowRate + "</td>"
                        "</tr>";
                html = html + cmcHtml;

            });
            $("#cmsf>tbody:eq(0)").append(html);
        }catch (e) {
            showMessage(e);
        }
    } else {
        $("#cmsf>tbody:eq(0)").empty();
        var htmlString = "<tr><td colspan='3' align='center'><span>@CM.Unsupport@</span></td></tr>";
        $("#cmsf>tbody:eq(0)").append(htmlString);
    }
};
//加载cpe信息;
function loadCpeList(json,cmCpe){
    showFirstTbody("cpeList");
    //CPE列表
    if (json.isSupportRealtimeCpeQuery) {
        //$("#cpelistdiv").show();
        try {
            $("#cpeList>tbody:eq(0)>tr").empty();
            var html = '';
            $.each(json.cpeList,function(i,cpeDetail){
                cmcHtml =
                    "<tr id='cpe-"+cpeDetail.topCmCpeMacAddress+"'>" +
                        "<td align='center' class='ip'>" + cpeDetail.topCmCpeIpAddress + "</td>" +
                        "<td align='center' class='mac'>" + cpeDetail.topCmCpeMacAddress + "</td>" +
                        "<td align='center' class='type'>" + cpeDetail.topCmCpeTypeStr + "</td>" +
                        "<td align='center' class='opt'>" +
                        "<a class='yellowLink' href='javascript:;' data-cmid="+cmCpe.cmId+" data-mac='"+cpeDetail.topCmCpeMacAddress+"' onclick='refreshCpe(this)' >"+ I18N.SYSTEM.refresh +"</a>" + " / " +
                        "<a class='yellowLink' href='javascript:;' data-ip='"+cpeDetail.topCmCpeIpAddress+"' onclick='pingCpe(this)' >Ping</a>" + " / " +
                        "<a class='yellowLink' href='javascript:;' data-ip='"+cpeDetail.topCmCpeIpAddress+"' onclick='traceRouteCpe(this)' >Traceroute</a>" +
                        "</td>" +
                        "</tr>";
                html = html + cmcHtml;

            });
            $("#cpeList>tbody:eq(0)").append(html);
        }catch (e) {
            showMessage(e);
        }
    } else {
        //$("#cpelistdiv").hide();
    }
};


//加载cm业务类型信息;
function loadCmServiceType(json,cmCpe){
	showFirstTbody("cmServiceType");
    try {
        $("#cmServiceType>tbody:eq(0)>tr").empty();
        var html = '';
  		$.each(json.cmServiceType,function(i,log){
			cmcHtml = 
				"<tr>" +
                "<td align='center' class='oldFileName'>" + log.oldFileName + "</td>" +
                "<td align='center' class='newFileName'>" + log.newFileName + "</td>" +
                "<td align='center' class='changeTime'>" + log.changeTime + "</td>" +
                "</tr>";
			html = html + cmcHtml;
        });
        $("#cmServiceType>tbody:eq(0)").append(html);
    }catch (e) {
        showMessage(e);
    }
};

//加载CPE 上下线行为;
function loadCpeActHistory(json,cmCpe){
	showFirstTbody("cpeAct");
    var actionString;
    $("#cpeAct>tbody:eq(0)>tr").empty();
    var html = ''
    $.each(json, function(j, n) {
        var cpeHtml = '';
        var cpeActList = this.cpeActs;
        var mac = this.cpeMacString;
        var size = cpeActList != null ? cpeActList.length : 0;
        for (var i = 0; i < size; i++) {
            if (getString(cpeActList[i],"action") == 1) {
                actionString = I18N.CMCPE.status.offline
            } else {
                actionString = I18N.CMCPE.status.online
            }
            cpeHtml = cpeHtml +
                      "<td  align='center'>" +  getString(cpeActList[i],"cpeipString") + "</td>" +
                      "<td  align='center'>" +  actionString + "</td>" +
                      "<td  align='center'>" +  getString(cpeActList[i],"realtimeString") + "</td></tr>";
        }
        cpeHtml = "<tr><td  align='center' rowspan='" + size + "'>" + mac + "</td>" + cpeHtml;
        html = html + cpeHtml;
    });
    $("#cpeAct>tbody:eq(0)").append(html);
    /*if ($("#label_cpeAct").hasClass("flagClose")) {
        $("#label_cpeAct").trigger("click");
    }*/
};//end loadCpeActHistory;

//加载cm上下线行为;
function loadCmActHistory(json,cmCpe){
	showFirstTbody("cmAct");
	 //cm 上下线行为
   $("#cmAct>tbody:eq(0)>tr").empty();
   $.each(json, function(i, n) {
       if (getString(n,"action") == 1) {
           actionString = I18N.CMCPE.status.offline
       } else {
           actionString = I18N.CMCPE.status.online
       }
       var htmlString = "<tr>" +
                        "<td  align='center'>" +  getString(n,"entityIp") + "</td>" +
                        "<td  align='center'>" +  getString(n,"cmcIndexString") + "</td>" +
                        "<td  align='center'>" +  getString(n,"cmipString") + "</td>" +
                        "<td  align='center'>" +  actionString + "</td>" +
                        "<td  align='center'>" +  getString(n,"realtimeString") + "</td>" +
                        "</tr>";
       $("#cmAct>tbody").append(htmlString);
   });

   /*if ($("#label_cmAct").hasClass("flagClose")) {
       $("#label_cmAct").trigger("click");
   }*/
};//end loadCmActHistory;

//加载CMTS告警;
function loadCmcAlertList(json,cmCpe){
   showFirstTbody("cmcAlert");
   $("#cmcAlert>tbody:eq(0)>tr").empty();
   var confirmStatus,clearStatus;
   $.each(json, function(i, n) {
	   if (n.status == 1) {
    	   confirmStatus = I18N.cmcAlert.confirm;
       } else {
           confirmStatus = I18N.cmcAlert.unConfirm;
       }
       if (n.status == 2 || n.status == 3) {
    	   clearStatus = I18N.cmcAlert.isClear;
       } else {
    	   clearStatus = I18N.cmcAlert.unClear;
       }
       var htmlString = "<tr>" +
                        "<td  align='left'>" +  getString(n,"message") + "</td>" +
                        "<td  align='left'>" +  getString(n,"typeName") + "</td>" +
                        "<td  align='center'>" +  getString(n,"firstTimeStr") + "</td>" +
                        "<td  align='center'>" +  getString(n,"lastTimeStr") + "</td>" +
                        "<td  align='center'>" +  confirmStatus + "</td>" +
                        "<td  align='center'>" +  clearStatus + "</td>" +
                        "</tr>";
       $("#cmcAlert>tbody:eq(0)").append(htmlString);
   });
   /*if ($("#label_cmcAlert").hasClass("flagClose")) {
       $("#label_cmcAlert").trigger("click");
   }*/
};

//cmts运行情况;
function loadCmcRunningInfo(json,cmCpe){
	showFirstTbody("cmcRunTimeTable");
	createCmcRunningInfoTable();
    $("#cmcType").text("");
    $("#interface").text("");
    $("#snr").text("");
    $("#bitErrorRate").text("");
    $("#unBitErrorRate").text("");
    $("#cmOnlineNum").text("");
    $("#cmOfflineNum").text("");
    $("#channelInOctetsRate").text("");
    var channelInOctetsRateValue = getString(json,"channelInOctetsRate")
    if(FLOW_K10 > channelInOctetsRateValue){
    	channelInOctetsRateValue = channelInOctetsRateValue.toFixed(2) + "bps";
	}else if(FLOW_M10 > channelInOctetsRateValue ){
		channelInOctetsRateValue = (channelInOctetsRateValue / FLOW_K10) .toFixed(2) + "Kbps";
	}else{
		channelInOctetsRateValue = (channelInOctetsRateValue / FLOW_M10 ).toFixed(2) + "Mbps";
	}
    if(EntityType.isCmtsType(cmCpe.typeId)){
    	$("#cmcType").text(getString(json,"cmcTypeName"));
        $("#interface").text(getString(cmCpe,"cmtsUpDescr"));
        $("#snr").html(getString(json,"snr")/10 + 'dB' + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showSnrHisPerf("' + cmCpe.cmcId + '","' + cmCpe.cmcIndex + '","' + cmCpe.typeId + '","' + getString(json,"channelIndex") + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
        $("#bitErrorRate").html(getString(json,"bitErrorRate") + "%" + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showBitErrorRateHisPerf("' + cmCpe.entityId + '","' + cmCpe.cmcIndex + '","' + getString(json,"channelIndex") + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
        $("#unBitErrorRate").html(getString(json,"unBitErrorRate") + "%" + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showUnBitErrorRateHisPerf("' + cmCpe.entityId + '","' + cmCpe.cmcIndex + '","' + getString(json,"channelIndex") + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
        $("#cmOnlineNum").html(getString(json,"cmOnlineNum") + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showCmOnlineNumHisPerf("' + cmCpe.entityId + '","' + cmCpe.cmcIndex + '","' + getString(json,"channelIndex") + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
        $("#cmOfflineNum").html(getString(json,"cmOfflineNum") + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showCmOfflineNumHisPerf("' + cmCpe.entityId + '","' + cmCpe.cmcIndex + '","' + getString(json,"channelIndex") + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
        $("#channelInOctetsRate").html(channelInOctetsRateValue + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showChannelInOctetsRateHisPerf("' + cmCpe.cmcId + '","' + cmCpe.cmcIndex + '","' + cmCpe.typeId + '","' + getString(json,"channelIndex") + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
    }else{
    	$("#cmcType").text(getString(json,"cmcTypeName"));
        $("#interface").text(getString(cmCpe,"statusUpChannelIfIndexString"));
        $("#snr").html(getString(json,"snr")/10 + 'dB' + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showSnrHisPerf("' + cmCpe.cmcId + '","' + cmCpe.cmcIndex + '","' + cmCpe.typeId + '","' + getString(json,"channelIndex") + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
        $("#bitErrorRate").html(getString(json,"bitErrorRate") + "%" + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showBitErrorRateHisPerf("' + cmCpe.entityId + '","' + cmCpe.cmcIndex + '","' + cmCpe.upchannelId + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
        $("#unBitErrorRate").html(getString(json,"unBitErrorRate") + "%" + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showUnBitErrorRateHisPerf("' + cmCpe.entityId + '","' + cmCpe.cmcIndex + '","' + cmCpe.upchannelId + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
        $("#cmOnlineNum").html(getString(json,"cmOnlineNum") + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showCmOnlineNumHisPerf("' + cmCpe.entityId + '","' + cmCpe.cmcIndex + '","' + cmCpe.upchannelId + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
        $("#cmOfflineNum").html(getString(json,"cmOfflineNum") + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showCmOfflineNumHisPerf("' + cmCpe.entityId + '","' + cmCpe.cmcIndex + '","' + cmCpe.upchannelId + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
        $("#channelInOctetsRate").html(channelInOctetsRateValue + '&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showChannelInOctetsRateHisPerf("' + cmCpe.cmcId + '","' + cmCpe.cmcIndex + '","' + cmCpe.typeId + '","' + getString(json,"channelIndex") + '");\'>' + I18N.CMCPE.lastSevenDayHisPerf + '</a>');
    }
};//end function;
function createCmcRunningInfoTable(){
var str = '';
	str += '<tr>';
	str +=     '<td class="rightBlueTxt">@CCMTS.entityType@:</td>';
	str +=     '<td id="cmcType"></td>';
	str += '</tr>';
	str += '<tr class="dataTableBlueBg">';
	str +=     '<td class="rightBlueTxt"> @CMCPE.interface@:</td>';
	str +=     '<td id="interface"></td>';
	str += '</tr>';
	str += '<tr>';
	str +=     '<td class="rightBlueTxt"> SNR:</td>';
	str +=     '<td id="snr"></td>';
	str += '</tr>';
	str += '<tr>';
	str +=     '<td class="rightBlueTxt"> @CM.correcteds@:</td>';
	str +=     '<td id="bitErrorRate"></td>';
	str += '</tr>';
	str += '<tr>';
	str +=     '<td class="rightBlueTxt"> @CM.uncorrectRate@:</td>';
	str +=     '<td id="unBitErrorRate"></td>';
	str += '</tr>';
	str += '<tr>';
	str +=     '<td class="rightBlueTxt"> @CMCPE.CMONLINENUM@:</td>';
	str +=     '<td id="cmOnlineNum"></td>';
	str += '</tr>';
	str += '<tr>';
	str +=     '<td class="rightBlueTxt"> @CMCPE.CMOFFLINENUM@:</td>';
	str +=     '<td id="cmOfflineNum"></td>';
	str += '</tr>';
	str += '<tr>';
	str +=     '<td class="rightBlueTxt"> @CMCPE.inFlow@:</td>';
	str +=     '<td id="channelInOctetsRate"></td>';
	str += '</tr>';
	$("#cmcRunTimeTable tbody:eq(0)").html(str);
}


//CM 的flap信息;
function loadCmFlap(json,cmCpe){
	showFirstTbody("cmFlapInfo");
	/*$("#insFail").html("");
    $("#powerAdj").html("");
    $("#range").html("");
    $("#flapIns").html("");*/
    
    createCmFlapTable();
    
    if(json == null){
    	$("#powerAdj").html('<a href="#" class="yellowLink" onclick=\'showPowerAdjHisPerf("' + cmCpe.statusMacAddress+ '");\'>' +I18N.CMCPE.lastSevenDayHisPerf  + '</a>');
        $("#range").html('<a href="#" class="yellowLink" onclick=\'showRangeHisPerf("' + cmCpe.statusMacAddress+ '");\'>' +I18N.CMCPE.lastSevenDayHisPerf  + '</a>');
        $("#flapIns").html('<a href="#" class="yellowLink" onclick=\'showFlapInsHisPerf("' + cmCpe.statusMacAddress+ '");\'>' +I18N.CMCPE.lastSevenDayHisPerf  + '</a>');
        return;
    }
    $("#powerAdj").html((json && json.topCmFlapPowerAdjLowerNum +json.topCmFlapPowerAdjHigherNum) + 
    		'&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showPowerAdjHisPerf("' + cmCpe.statusMacAddress+ '");\'>' +I18N.CMCPE.lastSevenDayHisPerf  + '</a>');
    $("#range").html((json && json.topCmFlapHitNum+json.topCmFlapMissNum)+ 
    		'&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showRangeHisPerf("' + cmCpe.statusMacAddress+ '");\'>' +I18N.CMCPE.lastSevenDayHisPerf  + '</a>');
    $("#flapIns").html(json && json.topCmFlapInsertionFailNum + 
    		'&nbsp&nbsp<a href="#" class="yellowLink" onclick=\'showFlapInsHisPerf("' + cmCpe.statusMacAddress+ '");\'>' +I18N.CMCPE.lastSevenDayHisPerf  + '</a>');
};//end function;
function createCmFlapTable(){
	var str =  '<tr>';
		str +=     '<td class="rightBlueTxt">@CMCPE.CmFlap.PowerAdjTimes@:';
		str +=     '</td>';
		str +=     '<td id="powerAdj"></td>';
		str += '</tr>';
		str += '<tr>';
		str +=     '<td class="rightBlueTxt">@CMCPE.CmFlap.RangeHitRate@:';
		str +=     '</td>';
		str +=     '<td id="range"></td>';
		str += '</tr>';
		str += '<tr>';
		str +=     '<td class="rightBlueTxt">@cmc.flap.onlineCounter@:';
		str +=     '</td>';
		str +=     '<td id="flapIns"></td>';
		str += '</tr>';
	$("#cmFlapInfo tbody:eq(0)").html(str);
}

function loadOltAlertList(json,cmCpe){
   showFirstTbody("oltAlert");
	 //olt 告警
   var confirmStatus,clearStatus;
   $("#oltAlert>tbody:eq(0)>tr").empty();
   $.each(json, function(i, n) {
       if (n.status == 1) {
    	   confirmStatus = I18N.cmcAlert.confirm;
       } else {
           confirmStatus = I18N.cmcAlert.unConfirm;
       }
       if (n.status == 2 || n.status == 3) {
    	   clearStatus = I18N.cmcAlert.isClear;
       } else {
    	   clearStatus = I18N.cmcAlert.unClear;
       }
       var htmlString = "<tr>" +
                        "<td  align='left'>" +  getString(n,"message") + "</td>" +
                        "<td  align='left'>" +  getString(n,"typeName") + "</td>" +
                        "<td  align='center'>" +  getString(n,"firstTimeStr") + "</td>" +
                        "<td  align='center'>" +  getString(n,"lastTimeStr") + "</td>" +
                        "<td  align='center'>" +  confirmStatus + "</td>" +
                        "<td  align='center'>" +  clearStatus + "</td>" +
                        "</tr>";
       $("#oltAlert>tbody:eq(0)").append(htmlString);
   });
   /*if ($("#label_oltAlert").hasClass("flagClose")) {
       $("#label_oltAlert").trigger("click");
   }*/
}

function loadOltRunningInfo(json,cmCpe){
	showFirstTbody("oltRunTimeInfoTable");
	
	createOltRunningInfoTable();
	$("#ponId").text(getString(json,"ponIdString"));
	if(getString(json,"txPower")==0){
		$("#txPower").text('- dBm');
	} else{
		$("#txPower").text(getString(json,"txPower") + 'dBm');
	}
	if(getString(json,"rxPower")==0){
		$("#rxPower").text('- dBm');
	} else{
		$("#rxPower").text(getString(json,"rxPower") + 'dBm');
	}
}
//创建OLT运行情况表格;
function createOltRunningInfoTable(){
	var str ='';
	str += '<tr>';
	str +=     '<td class="rightBlueTxt"> @CMCPE.inPon@:</td>';
	str +=     '<td id="ponId"></td>';
	str += '</tr>';
	str += '<tr class="dataTableBlueBg">';
	str +=     '<td class="rightBlueTxt"> PON@CMC.title.rxOpticalPower@:</td>';
	str +=     '<td id="txPower"></td>';
	str += '</tr>';
	str += '<tr>';
	str +=     '<td class="rightBlueTxt"> PON@CMC.title.receiveOpticalPower@:</td>';
	str +=     '<td id="rxPower"></td>';
	str += '</tr>';
	$("#oltRunTimeInfoTable tbody:eq(0)").html(str);
}

function icmpUnreachable(json){
	var str ='<tr><td colspan="2" align="center">@cmcpe.icmpUnreachable@</td></tr>';
	$("#cmPropertyTable tbody:eq(0)").html(str);
	var cmId = json.cmStatus.cmId;
	$.ajax({
		url: '/cmCpe/getPreviousState.tv', 
		cache:false, 
		method:'post',
		data: {
			cmId : cmId
		},
		success: function(json) {
			var str = '<tr><td colspan="2" align="center">@cmcpe.icmpUnreachable@</td></tr>' + 
			'<tr><td colspan="2" align="center">@CM.nowState@:' + renderPreState(json.cmStatus) + '</td></tr>';
			if(json.cmStatus == '1'){
				str += '<tr><td colspan="2" align="center">@CM.previousState@:' + renderPreState(json.preState) + '</td></tr>'; 
			}
			$("#cmPropertyTable tbody:eq(0)").html(str);
		},
		error: function(){
		}
	});
    showFirstTbody("CmUpChannel");
    showFirstTbody("CmDownChannel");
    showFirstTbody("cmif");
//	var $CmUpChannel = $("#CmUpChannel"),
//	    $CmDownChannel = $("#CmDownChannel"),
//	    num = $CmUpChannel.find("thead tr:eq(1)").find("th").length,
//	    num2 = $CmDownChannel.find("thead tr:eq(1)").find("th").length;
//	$CmUpChannel.find("tbody:eq(0)").html('<tr><td colspan="'+ num +'" align="center">@cmcpe.icmpUnreachable@</td></tr>');
//	$CmDownChannel.find("tbody:eq(0)").html('<tr><td colspan="'+ num2 +'" align="center">@cmcpe.icmpUnreachable@</td></tr>');

    $('#fdbAddressRefreshBtn').hide();
}


function createCmProTable(){
	var str = '<tr>';
		str +=     '<td class="rightBlueTxt">@CMCPE.pingTest@:';
		str +=     '</td>';
		str +=     '<td id="cmping">@CMCPE.deviceOnLine@</td>';
		str += '</tr>';
		str += '<tr class="dataTableBlueBg">';
		str +=     '<td class="rightBlueTxt"> @CM.serNum@:';
		str +=     '</td>';
		str +=     '<td id="cmsn"></td>';
		str += '</tr>';
		str += '<tr>';
		str +=     '<td class="rightBlueTxt"> @CM.docsisRegMod@:';
		str +=     '</td>';
		str +=     '<td id="cmregmode"></td>';
		str += '</tr>';
		str += '<tr class="dataTableBlueBg">';
		str +=     '<td class="rightBlueTxt"> @CM.softName@:';
		str +=     '</td>';
		str +=     '<td id="cmsoftware"></td>';
		str += '</tr>';
		str += '<tr>';
		str +=     '<td class="rightBlueTxt"> @CM.sysUpTime@:';
		str +=     '</td>';
		str +=     '<td id="cmuptime"></td>';
		str += '</tr>';
        str += '<tr>';
        str +=     '<td class="rightBlueTxt"> @CMC.label.rebootcount@:';
        str +=     '</td>';
        str +=     '<td id="rebootcount"></td>';
        str += '</tr>';
		str += '<tr class="dataTableBlueBg">';
		str +=     '<td class="rightBlueTxt"> @CM.ServiceType@:';
		str +=     '</td>';
		str +=     '<td id="configurationfilename"></td>';
		str += '</tr>';
		str += '<tr>';
		str +=     '<td class="rightBlueTxt"> @CM.timingServer@:';
		str +=     '</td>';
		str +=     '<td id="cmntp"></td>';
		str += '</tr>';
		str += '<tr class="dataTableBlueBg">';
		str +=     '<td class="rightBlueTxt"> @CM.tftpServer@:';
		str +=     '</td>';
		str +=     '<td id="cmtftp"></td>';
		str += '</tr>';
        str += '<tr>';
        str +=     '<td class="rightBlueTxt"> @CMC.label.dhcpServer@:';
        str +=     '</td>';
        str +=     '<td id="cmdhcp"></td>';
        str += '</tr>'
        str += '<tr>';
        str +=     '<td class="rightBlueTxt"> @CMC.label.description@:';
        str +=     '</td>';
        str +=     '<td id="description"></td>';
        str += '</tr>';
    $("#cmPropertyTable tbody:eq(0)").html(str);
}

function getServiceFlowType(serviceFlowType) {
    switch (serviceFlowType){
        case 2:
            return 'US';
        case 1:
            return 'DS';
        default:
            return '--';
    }
}

function getInterfaceStatus(status) {
    switch (status){
        case 1:
            return 'UP';
        case 2:
            return 'DOWN';
        case 3:
            return 'Testing';
        case 4:
            return 'Unkown';
        case 5:
            return 'Dormant';
        case 6:
            return 'Not Present';
        case 7:
            return 'LowerLayerDown';
        default:
            return 'DOWN';
    }
}
function renderPreState(preState){
	switch (preState){
	case -1:
		return '@CM.noValue@';
	case -2:
		return '@CM.cmtsNotSupport@';
	case 1:
		return 'other';
	case 2:
		return 'ranging';
	case 3:
		return 'rangingAborted';
	case 4:
		return 'rangingComplete';
	case 5:
		return 'ipComplete';
	case 6:
		return 'registrationComplete';
	case 7:
		return 'accessDenied';
	case 8:
		return 'operational';
	case 9:
		return 'registeredBPIInitializing';
	case 10:
		return 'DHCPv4Discover';
	case 11:
		return 'DHCPv4Offer';
	case 12:
		return 'DHCPv4Request';
    case 13:
        return 'firstTFTP';
    case 14:
        return 'getCNUStatFail';
    case 15:
        return 'failToSendRequest';
    case 16:
        return 'downMoveTimeOut';
    case 17:
        return 'upMoveTimeOut';
    case 18:
        return 'CNUStatTimeOut';
    case 19:
        return 'registrationRequest';
    case 20:
        return 'rangingInitial';
	case 21:
		return 'ForwardingDisabled';
	case 22:
		return 'DHCPv6Solicit';
	case 23:
		return 'DHCPv6Advertise';
	case 24:
		return 'DHCPv6Request';
    case 25:
        return 'DHCPv6Reply';
    case 26:
        return 'p-online';
    case 30:
        return 'p-online(d)';
    case 27:
        return 'w-online';
    case 31:
        return 'w-online(d)';
	default:
		return '@CM.unknownState@';
	}
} 
