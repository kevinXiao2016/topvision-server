var FLOW_K10 = 1000,
FLOW_M10 = FLOW_K10 * 1000,
TOOLTIP_TPL = '<span class="tooltipSpan" style="color:{0}">{1}:</span><span class="tooltipSpan" style="color:{0}">{2}{3}</span><br/>';
var page = {
    isOltType : true,
    selectedCm : null,
    allTable : ["cmPropertyOuter","CmUpChannelOuter","CmDownChannelOuter","cmifdiv","cmsfdiv","cpelistdiv","cpeActOuter","cmActOuter",,"cmServiceTypeDiv","oltAlertOuter","cmcAlertOuter","oltRunTimeInfoOuter","cmcRunTimeOuter","cmFlapInfoOuter"],
    withoutOlt : ["cmPropertyOuter","CmUpChannelOuter","CmDownChannelOuter","cmifdiv","cmsfdiv","cpelistdiv","cpeActOuter","cmActOuter","cmServiceTypeDiv","cmcAlertOuter","cmcRunTimeOuter","cmFlapInfoOuter"]
}
function showHistoryPerf(data, divId, title, yText, valueDecimals, seriesName, seriesUnit, startTime, endTime){
        	var seriesOptions = [];
        	seriesOptions[0] = {
        				name:  seriesName,
        				data: data
        			};
        	buttons = []
        	
        	Highcharts.setOptions({ 
        		global: { useUTC: false } 
        	}); 
        	var dateRegion = startTime + '~' + endTime;
        	new Highcharts.StockChart({
        		chart: {
                    renderTo: divId,
                    type : 'spline',
        			zoomType: 'x',
        			width : $(window).width() - 280,
        			height : 300
                },
                rangeSelector:{
                	buttonTheme: { // styles for the buttons
                        r: 1,
                        width: 54,
                        //padding: 3,
                        fill: 'none',
                        style: {
                            color: '#039'/*,
    	    				cursor : 'pointer'*/
                        },
        	    		states: {
        	    			hover: {
        	    				fill: '#039',
        	    				style: {
        	    					color: 'white'
        	    				}
        	    			},
        	    			select: {
        	    				fill: 'none',
        	    				style: {
        	    					color: '#039'
        	    				}
        	    			}
        	    		}
                    },
                    buttons:[
                        {type: 'all', count: 1, text: I18N.Tip.all}
                    ],
                    labelStyle: {
                        display: 'none',
                        color: 'silver',
                        fontWeight: 'bold'
                    },
                    inputEnabled: false
                },

        	     loading: {
        			   showDuration : 2000
        		   }, 
        		   
                title: {
                    text: title
                },
                subtitle : {
                	text : dateRegion
                },
                
                credits: {enabled : false},
                
                xAxis: {
                	ordinal:false, 
        	    	gridLineWidth: 1,lineColor: 'gray',tickColor: 'gray',type: 'datetime' ,
        	    	/*labels : {
        	    		formatter : function(){
        	    			var UTC_time  = this.value;  
        	    			if( 1 == 1 )
        	    				return Highcharts.dateFormat('%m/%e %H:%M', UTC_time)
        	    			else
        	    				return Highcharts.dateFormat('%y/%m/%e', UTC_time)
        	    		}
        	    	} ,*/
                
                type: 'datetime',
    			ordinal: false,
    			dateTimeLabelFormats: {
    				millisecond: '%H:%M:%S.%L',
    				second: '%H:%M:%S',
    				minute: '%H:%M',
    				hour: '%H:%M',
    				day: '%m-%d',
    				week: '%m-%d',
    				month: '%Y-%m',
    				year: '%Y'
    			},
    			minRange: 60 * 1000 // one m,
        	    }, 
        	    
        	    legend : { enabled : true , floating : true , verticalAlign:'top' , align:'right' ,itemStyle: {
        			   fontSize: '10px'
        		},borderWidth : 0,backgroundColor:null,y:25 },
        		
        	    navigator: {
        			baseSeries : 0,enabled : true,height : 30 ,
        			xAxis: {
        				labels: {
        					formatter : function(){
        						var UTC_time  = this.value;  
        		    			if( 1 == 1 ){
        		    				return Highcharts.dateFormat('%Y/%m/%e %H:%M', UTC_time)
        		    			}else{
        		    				return Highcharts.dateFormat('%Y/%m/%e', UTC_time)
        		    			}
        		    		}
        				}
        			}
        		},
        		
                yAxis: {
                	title: {
                        text: yText
                    },
        	    	labels: {
        	    		formatter: function() {
        	    			if(divId == "channelInOctetsRateHisPerf"){
        	    				return this.value / FLOW_M10 + seriesUnit;
        	    			}else{
        	    				return this.value;
        	    			}
        	    		}
        	    	},
        	    	plotLines: [{
        	    		value: 0,
        	    		width: 2,
        	    		color: 'silver'
        	    	}],
        	    	min: 0
        	    },
        	    
        	    tooltip: {
        	    	formatter: function() {
        				var s = '<b>'+ Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'</b><br/>';
        				$.each(this.points, function(i, point) {
        					var y
        					if(point.y == -1){
        						s += '<span class="tooltipSpan" style="color:'+  point.series.color + '">' + point.series.name + I18N.CMCPE.failedGetDataTip + '</span><br/>';
        					}else if (divId == "channelInOctetsRateHisPerf"){
        						var $y = point.y ;
    							var $unit;
    							if(FLOW_K10 > $y){
    								$y = $y.toFixed(2) ;
    								$unit = "bps"
    							}else if(FLOW_M10 > $y ){
    								$y = ($y / FLOW_K10) .toFixed(2) ;
    								$unit = "Kbps"
    							}else{
    								$y = ($y / FLOW_M10 ).toFixed(2);
    								$unit = "Mbps"
    							}
    							s +=  String.format(TOOLTIP_TPL,point.series.color, point.series.name, $y, $unit);	
        					}else{
        						y = point.y.toFixed(valueDecimals);
        						s += '<span class="tooltipSpan" style="color:'+ point.series.color + '">' + point.series.name 
        						  + ':</span><span class="tooltipSpan" style="color:'+ point.series.color + '">' + y + seriesUnit + '</span><br/>';
        					}
        				});
                   
        				return s;
        			}
        	    	
        	    },
        		
                series : seriesOptions
        		})
        }


function  getString(p,str) {
    if (p == null) {
        return "";
    } else {
        var o = eval("p." + str);
        if (o != null && o != "noSuchObject") {
            return o;
        } else {
            return "--";
        }
    }
}

function refreshCpeList() {
	showLoadingTbody("cpeList");
	disabledAlink("cpeListRefreshBtn",true);
    $.ajax({
        type: "POST",
        url: "/cmCpe/refreshCpeList.tv",
        data: "cmId="+cmId,cache: false,
        dataType: "json",
        success: function(cpeInfo) {
        	 $("#cpeList>tbody:eq(0)>tr").empty();
             var html = '';
       		 $.each(cpeInfo,function(i,cpeDetail){
   				var cmcHtml = 
   					"<tr id='cpe-"+cpeDetail.topCmCpeMacAddress+"'>" +
	                    "<td align='center' class='ip'>" + cpeDetail.topCmCpeIpAddress + "</td>" +
	                    "<td align='center' class='mac'>" + cpeDetail.topCmCpeMacAddress + "</td>" +
	                    "<td align='center' class='type'>" + cpeDetail.topCmCpeTypeStr + "</td>" +
	                    "<td align='center' class='opt'>" +
		                    "<a class='yellowLink' href='javascript:;' data-cmid="+cmId+" data-mac='"+cpeDetail.topCmCpeMacAddress+"' onclick='refreshCpe(this)' >"+ I18N.SYSTEM.refresh +"</a>" + " / " +
		                    "<a class='yellowLink' href='javascript:;' data-ip='"+cpeDetail.topCmCpeIpAddress+"' onclick='pingCpe(this)' >Ping</a>" + " / " +
	                    	"<a class='yellowLink' href='javascript:;' data-ip='"+cpeDetail.topCmCpeIpAddress+"' onclick='traceRouteCpe(this)' >Traceroute</a>" +
	                    "</td>" +
	                    "</tr>";
   				html = html + cmcHtml;
   				
             });
             $("#cpeList>tbody:eq(0)").append(html);
             top.afterSaveOrDelete({
               title: I18N.COMMON.tip,
               html: '<b class="orangeTxt">' + I18N.CMC.tip.refreshSuccess + '</b>'
             });
        },
        error:function(e){
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.refreshFailure);
        },
        complete:function(){
        	disabledAlink("cpeListRefreshBtn",false);
        	showFirstTbody("cpeList");
        }
    });
}

function refreshCpe(me) {
	var mac = $(me).data("mac");
	var cmId = parseInt($(me).data("cmid"),10);
    $.ajax({
        type: "POST",
        url: "/cmCpe/refreshCpe.tv",
        data: "cpeMac="+mac+"&cmId="+cmId,cache: false,
        dataType: "json",
        success: function(json) {
        	var tr = $("#cpe-"+mac);
        	$(".ip",tr).text(json.topCmCpeIpAddress);
        	$(".mac",tr).text(json.topCmCpeMacAddressString);
        	$(".type",tr).text(json.topCmCpeTypeString);
        	$(".opt",tr).html("<a class='yellowLink' href='javascript:;' data-cmid="+cmId+" data-mac='"+json.topCmCpeMacAddressString+"' onclick='refreshCpe(this)' >"+ I18N.SYSTEM.refresh +"</a>" + 
        	"/" + "<a class='yellowLink' href='javascript:;' data-ip='"+json.topCmCpeIpAddress+"' onclick='pingCpe(this)' >Ping</a>" + " / " +
         	"<a class='yellowLink' href='javascript:;' data-ip='"+json.topCmCpeIpAddress+"' onclick='traceRouteCpe(this)' >Traceroute</a>" );

            top.afterSaveOrDelete({
              title: I18N.COMMON.tip,
              html: '<b class="orangeTxt">' + I18N.CMC.tip.refreshSuccess + '</b>'
            });
        },
        error:function(e){
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.refreshFailure);
        }
    });
}

function pingCpe(me) {
	var ip = $(me).data("ip");
	window.parent.createDialog("modalDlg", 'Ping' + " - " + ip, 600, 400,
	  "entity/runCmd.tv?cmd=ping&ip=" + ip, null, true, true);
}
//YangYi Add @ 2013-11-15 Trace Route
function traceRouteCpe(me) {
	var ip = $(me).data("ip");
    window.parent.createDialog("modalDlg", 'Tracert ' + ip, 600, 400,
      "entity/runCmd.tv?cmd=tracert&ip=" + ip, null, true, true);
}
function doRefresh(){
}

function showSnrHisPerf(cmcId, cmcIndex, typeId, channelIndex){

	if(document.getElementById("snrHisPerf").style.display == 'block'){
		document.getElementById("snrHisPerf").style.display = 'none'
			return;
	}
	var startTime = utcToDate(new Date(new Date().getTime() - (7*24*60*60*1000)));
	var endTime = utcToDate(new Date());
	$.ajax({
		url : '/cmCpe/showSnrHisPerf.tv',cache:false,dataType:'json',
		data:{	
			startTime : startTime,
			cmcId : cmcId,
			cmcIndex : cmcIndex,
			channelIndex : channelIndex,
			entityType : typeId,
			endTime : endTime
			},
		success:function(json){				
			document.getElementById("snrHisPerf").style.display='block';
			showHistoryPerf(json.snrHisPerf, "snrHisPerf", I18N.CMCPE.channelSnrHisPerf, I18N.CCMTS.view.noice.ytitle, 1, I18N.CHANNEL.snr,"dB", startTime, endTime);
						
			
			var nowS = $("#openLayerMain").scrollTop();
			var outerS = $("#snrHisPerf").offset().top;	
			var pos;
			if(nowS < outerS){
				pos = outerS + nowS;
			}else if(nowS > outerS){
				pos = nowS + outerS;
			}else{
				return;
			}
			$("#openLayerMain").animate({scrollTop:pos-60}, 'normal');
			
		},error:function(){
		}
	});
}

function showBitErrorRateHisPerf(entityId, cmcIndex, channelId){
	if(document.getElementById("bitErrorRateHisPerf").style.display == 'block'){
		document.getElementById("bitErrorRateHisPerf").style.display = 'none'
			return;
	}
	var startTime = utcToDate(new Date(new Date().getTime() - (7*24*60*60*1000)));
	var endTime = utcToDate(new Date());
	$.ajax({
		url : '/cmCpe/showBitErrorRateHisPerf.tv',cache:false,dataType:'json',
		data:{	
			startTime : startTime,
			entityId : entityId,
			cmcIndex : cmcIndex,
			channelId : channelId,
			endTime : endTime
			},
		success:function(json){						
			document.getElementById("bitErrorRateHisPerf").style.display='block';	
			showHistoryPerf(json.bitErrorHisPerf, "bitErrorRateHisPerf", I18N.CMCPE.channelCmBitErrorRate, I18N.CMCPE.bitErrorRate, 1, I18N.CM.correcteds,"", startTime, endTime);
			
			var nowS = $("#openLayerMain").scrollTop();
			var outerS = $("#bitErrorRateHisPerf").offset().top;	
			var pos;
			if(nowS < outerS){
				pos = outerS + nowS;
			}else if(nowS > outerS){
				pos = nowS + outerS;
			}else{
				return;
			}
			$("#openLayerMain").animate({scrollTop:pos-60}, 'normal');
			
		},error:function(){
		}
	});
}
function showUnBitErrorRateHisPerf(entityId, cmcIndex, channelId){
	if(document.getElementById("unBitErrorRateHisPerf").style.display == 'block'){
		document.getElementById("unBitErrorRateHisPerf").style.display = 'none'
			return;
	}
	var startTime = utcToDate(new Date(new Date().getTime() - (7*24*60*60*1000)));
	var endTime = utcToDate(new Date());
	$.ajax({
		url : '/cmCpe/showUnBitErrorRateHisPerf.tv',cache:false,dataType:'json',
		data:{	
			startTime : startTime,
			entityId : entityId,
			cmcIndex : cmcIndex,
			channelId : channelId,
			endTime : endTime
			},
		success:function(json){				
			document.getElementById("unBitErrorRateHisPerf").style.display='block';
			showHistoryPerf(json.unBitErrorHisPerf, "unBitErrorRateHisPerf", I18N.CMCPE.channelCmUnBitErrorRate, I18N.CMCPE.bitErrorRate, 1, I18N.CM.uncorrectRate,"", startTime, endTime);
			
			var nowS = $("#openLayerMain").scrollTop();
			var outerS = $("#unBitErrorRateHisPerf").offset().top;	
			var pos;
			if(nowS < outerS){
				pos = outerS + nowS;
			}else if(nowS > outerS){
				pos = nowS + outerS;
			}else{
				return;
			}
			$("#openLayerMain").animate({scrollTop:pos-60}, 'normal');
			
		},error:function(){
		}
	});
}
function showCmOnlineNumHisPerf(entityId, cmcIndex, channelId){
	if(document.getElementById("cmOnlineNumHisPerf").style.display == 'block'){
		document.getElementById("cmOnlineNumHisPerf").style.display = 'none'
			return;
	}
	var startTime = utcToDate(new Date(new Date().getTime() - (7*24*60*60*1000)));
	var endTime = utcToDate(new Date());
	$.ajax({
		url : '/cmCpe/showCmOnlineNumHisPerf.tv',cache:false,dataType:'json',
		data:{	
			startTime : startTime,
			entityId : entityId,
			cmcIndex : cmcIndex,
			channelId : channelId,
			endTime : endTime
			},
		success:function(json){				
			document.getElementById("cmOnlineNumHisPerf").style.display='block';
			showHistoryPerf(json.cmOnlineNumHisPerf, "cmOnlineNumHisPerf", I18N.CMCPE.channelCmOnLineNumHisPerf, I18N.CMCPE.userNum, 0, I18N.CMCPE.CMONLINENUM,"", startTime, endTime);
			
			var nowS = $("#openLayerMain").scrollTop();
			var outerS = $("#cmOnlineNumHisPerf").offset().top;	
			var pos;
			if(nowS < outerS){
				pos = outerS + nowS;
			}else if(nowS > outerS){
				pos = nowS + outerS;
			}else{
				return;
			}
			$("#openLayerMain").animate({scrollTop:pos-60}, 'normal');
			
		},error:function(){
		}
	});
	
}
function showCmOfflineNumHisPerf(entityId, cmcIndex, channelId){
	if(document.getElementById("cmOfflineNumHisPerf").style.display == 'block'){
		document.getElementById("cmOfflineNumHisPerf").style.display = 'none'
			return;
	}
	var startTime = utcToDate(new Date(new Date().getTime() - (7*24*60*60*1000)));
	var endTime = utcToDate(new Date());
	$.ajax({
		url : '/cmCpe/showCmOfflineNumHisPerf.tv',cache:false,dataType:'json',
		data:{	
			startTime : startTime,
			entityId : entityId,
			cmcIndex : cmcIndex,
			channelId : channelId,
			endTime : endTime
			},
		success:function(json){				
			document.getElementById("cmOfflineNumHisPerf").style.display='block';
			showHistoryPerf(json.cmOfflineNumHisPerf, "cmOfflineNumHisPerf", I18N.CMCPE.channelCmOffLineNumHisPerf, I18N.CMCPE.userNum, 0, I18N.CMCPE.CMOFFLINENUM,"", startTime, endTime);
			
			var nowS = $("#openLayerMain").scrollTop();
			var outerS = $("#cmOfflineNumHisPerf").offset().top;	
			var pos;
			if(nowS < outerS){
				pos = outerS + nowS;
			}else if(nowS > outerS){
				pos = nowS + outerS;
			}else{
				return;
			}
			$("#openLayerMain").animate({scrollTop:pos-60}, 'normal');
			
		},error:function(){
		}
	});
}
function showChannelInOctetsRateHisPerf(cmcId, cmcIndex, typeId, channelIndex){
	if(document.getElementById("channelInOctetsRateHisPerf").style.display == 'block'){
		document.getElementById("channelInOctetsRateHisPerf").style.display = 'none'
			return;
	}
	var startTime = utcToDate(new Date(new Date().getTime() - (7*24*60*60*1000)));
	var endTime = utcToDate(new Date());
	$.ajax({
		url : '/cmCpe/showFlowHisPerf.tv',cache:false,dataType:'json',
		data:{	
			startTime : startTime,
			cmcId : cmcId,
			cmcIndex : cmcIndex,
			channelIndex : channelIndex,
			entityType : typeId,
			endTime : endTime
			},
		success:function(json){				
			document.getElementById("channelInOctetsRateHisPerf").style.display='block';
			showHistoryPerf(json.flowHisPerf, "channelInOctetsRateHisPerf", I18N.CMCPE.channelFlowHisPerf, I18N.CMCPE.flow, 1, I18N.CMCPE.channelFlow,"Mbps", startTime, endTime);
			
			var nowS = $("#openLayerMain").scrollTop();
			var outerS = $("#channelInOctetsRateHisPerf").offset().top;	
			var pos;
			if(nowS < outerS){
				pos = outerS + nowS;
			}else if(nowS > outerS){
				pos = nowS + outerS;
			}else{
				return;
			}
			$("#openLayerMain").animate({scrollTop:pos-60}, 'normal');
			
		},error:function(){
		}
	});
}
//CM FLAP
function showFlapInsHisPerf(cmMac){
	if(document.getElementById("cmFlapInsRealChart").style.display == 'block'){
		document.getElementById("cmFlapInsRealChart").style.display = 'none'
			return;
	}
	var startTime = utcToDate(new Date(new Date().getTime() - (7*24*60*60*1000)));
	var endTime = utcToDate(new Date());
	$.ajax({
		url: '/cmflap/queryOneCMFlapHisData.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        data:{
        	cmMac:cmMac,
        	flapSearchStartTime : startTime,
        	flapSerachEndTime : endTime
        	},
        cache:false,
		success:function(json){				
			document.getElementById("cmFlapInsRealChart").style.display='block';			
			showHistoryPerf(json.insFailNum, "cmFlapInsRealChart", I18N.CMCPE.CMAbnormalOnlineTimes, 
					I18N.CMCPE.CMAbnormalOnlineTimes, 0, I18N.CMCPE.AbnormalOnlineTimes,I18N.CMCPE.Times, json.startTime, json.endTime);
			
			var nowS = $("#openLayerMain").scrollTop();
			var outerS = $("#cmFlapInsRealChart").offset().top;	
			var pos;
			if(nowS < outerS){
				pos = outerS + nowS;
			}else if(nowS > outerS){
				pos = nowS + outerS;
			}else{
				return;
			}
			$("#openLayerMain").animate({scrollTop:pos-60}, 'normal');
			
		},error:function(){
		}
	});
}
//CM FLAP
function showRangeHisPerf(cmMac){
	if(document.getElementById("cmFlapRangeChart").style.display == 'block'){
		document.getElementById("cmFlapRangeChart").style.display = 'none'
			return;
	}
	var startTime = utcToDate(new Date(new Date().getTime() - (7*24*60*60*1000)));
	var endTime = utcToDate(new Date());
	$.ajax({
		url: '/cmflap/queryOneCMFlapHisData.tv?r=' + Math.random(),
		cache:false,
		dataType:'json',
		data:{
        	cmMac:cmMac,
        	flapSearchStartTime : startTime,
        	flapSerachEndTime : endTime
        	},
		success:function(json){				
			document.getElementById("cmFlapRangeChart").style.display='block';
			showHistoryPerf(json.rangePercent, "cmFlapRangeChart", I18N.CMCPE.cjmzltj,
					I18N.CMCPE.cjmzlper, 1, I18N.CMCPE.cjmzl,"%", json.startTime, json.endTime);
			
			var nowS = $("#openLayerMain").scrollTop();
			var outerS = $("#cmFlapRangeChart").offset().top;	
			var pos;
			if(nowS < outerS){
				pos = outerS + nowS;
			}else if(nowS > outerS){
				pos = nowS + outerS;
			}else{
				return;
			}
			$("#openLayerMain").animate({scrollTop:pos-60}, 'normal');
			
		},error:function(){
		}
	});
}
//CM FLAP
function showPowerAdjHisPerf(cmMac){
	if(document.getElementById("cmFlapPowerAdjNumChart").style.display == 'block'){
		document.getElementById("cmFlapPowerAdjNumChart").style.display = 'none'
			return;
	}
	var startTime = utcToDate(new Date(new Date().getTime() - (7*24*60*60*1000)));
	var endTime = utcToDate(new Date());
	$.ajax({
		url: '/cmflap/queryOneCMFlapHisData.tv?r=' + Math.random(),
		cache:false,
		dataType:'json',
		data:{
        	cmMac:cmMac,
        	flapSearchStartTime : startTime,
        	flapSerachEndTime : endTime
        	},
		success:function(json){				
			document.getElementById("cmFlapPowerAdjNumChart").style.display='block';			
			showHistoryPerf(json.poweAdjNum, "cmFlapPowerAdjNumChart", I18N.CMCPE.dptzcstj, 
					I18N.CMCPE.dptzcsper, 0, I18N.CMCPE.dptzcs,'@CM.Unittime@', json.startTime, json.endTime);
			
			var nowS = $("#openLayerMain").scrollTop();
			var outerS = $("#cmFlapPowerAdjNumChart").offset().top;	
			var pos;
			if(nowS < outerS){
				pos = outerS + nowS;
			}else if(nowS > outerS){
				pos = nowS + outerS;
			}else{
				return;
			}
			$("#openLayerMain").animate({scrollTop:pos-60}, 'normal');
			
		},error:function(){
		}
	});
}
//CM FLAP-unfinished
function showInsFailHisPerf(cmMac){
	if(document.getElementById("snrHisPerf").style.display == 'block'){
		document.getElementById("snrHisPerf").style.display = 'none'
			return;
	}
	var startTime = utcToDate(new Date(new Date().getTime() - (7*24*60*60*1000)));
	var endTime = utcToDate(new Date());
	$.ajax({
		url : '/cmCpe/showSnrHisPerf.tv',cache:false,dataType:'json',
		data:{
        	cmMac:cmMac,
        	flapSearchStartTime : startTime,
        	flapSerachEndTime : endTime
        	},
		success:function(json){				
			document.getElementById("snrHisPerf").style.display='block';			
			showHistoryPerf(json.snrHisPerf, "snrHisPerf", I18N.CMCPE.channelSnrHisPerf, I18N.CCMTS.view.noice.ytitle, 1, I18N.CHANNEL.snr,"dB", startTime, endTime);
			
			var nowS = $("#openLayerMain").scrollTop();
			var outerS = $("#snrHisPerf").offset().top;	
			var pos;
			if(nowS < outerS){
				pos = outerS + nowS;
			}else if(nowS > outerS){
				pos = nowS + outerS;
			}else{
				return;
			}
			$("#openLayerMain").animate({scrollTop:pos-60}, 'normal');
			
		},error:function(){
		}
	});
}


function utcToDate(utcCurrTime)  
{  
//IE8  Fri Oct 31 18:00:00 UTC+0800 2008  
//Fri,Oct,31,2008,18:00:00,GMT+0800,(中国标准时间)
utcCurrTime = utcCurrTime + "";  
var date="";  
var month=new Array();  
month["Jan"]=1;  
month["Feb"]=2;  
month["Mar"]=3;  
month["Apr"]=4;  
month["May"]=5;  
month["Jun"]=6;  
month["Jul"]=7;  
month["Aug"]=8;  
month["Sep"]=9;  
month["Oct"]=10;  
month["Nov"]=11;  
month["Dec"]=12;  
 
str = utcCurrTime.split(" ");  
if(str[4] == 'UTC+0800'){
	date = str[5]+"-";  
	date = date + month[str[1]] + "-" + str[2] + " " + str[3];  
}else{
	date = str[3]+"-";  
	date = date + month[str[1]] + "-" + str[2] + " " + str[4];  
}
return date;  
}  

function toDecimal(x) {  
    var f = parseFloat(x);  
    if (isNaN(f)) {  
        return;  
    }  
    f = Math.round(x*100)/100;  
    return f;  
}  


function getQueryParams( $dom ){
	var cmCpe = $( $dom ).data("data");
    cmId = cmCpe.cmId;
    
    
    
    if(!EntityType.isOltType(cmCpe.typeId)){
    	page.isOltType = false;
    }
    var cmIp = cmCpe.statusIpAddress
    if (cmIp == null || cmIp == '0.0.0.0' || cmIp == '') {
        cmIp = cmCpe.statusInetAddress;
    }
    cmCpe.cmIp = cmIp;
    return cmCpe;
}

function showCm(dom) {
	if(bar){bar.loading = false;}
	$("#snrHisPerf, #bitErrorRateHisPerf, #unBitErrorRateHisPerf, #cmOnlineNumHisPerf, #cmOfflineNumHisPerf, #channelInOctetsRateHisPerf,#cmFlapPowerAdjNumChart,#cmFlapRangeChart,#cmFlapInsRealChart").css({display:"none"});
	
	page.selectedCm = dom;
	hideAllTable();
	$("div.relativeDiv").addClass("displayNone");
	//显示前3个table的外部div;
	showTableOuter(['cmPropertyOuter','CmUpChannelOuter','CmDownChannelOuter','cmifdiv']);
	//所有刷新按钮都disabled;
	disabledAllRefreshBtn(true);
    //CM实时属性
    clearData();
    cmCpe =  getQueryParams( dom );
    //window.parent.showWaitingDlg(String.format(I18N.COMMON.loading, cmCpe.statusInetAddress));
    
    STEP_CURSOR  = 0;
    $("#cancelProgress").show();
    
    var data = "cmIp="+cmCpe.cmIp+"&cmId="+cmCpe.cmId+"&entityId="+cmCpe.entityId+"&cmcId="
		+cmCpe.cmcId+"&entityType="+cmCpe.typeId+"&cmMac="+cmCpe.statusMacAddress;
    
    var i = 0;
  //初始化进度条;
    WIN.PROGRESS_CONF = {
   		width : 700,
   		renderTo : 'putProgressBar',
   		scollOuter : 'mainTableContainer',
   		steps : [
   			{
   				txt : '@CM.cmProperty@', //CM属性;
   				tableOuterId : 'cmPropertyOuter' //外部wrap的div的id,实现点击一下文字，直接滚动到相对应的地方;
   			},
   			{
   				txt : '@upchannel@',//上行信道;
   				tableOuterId : 'CmUpChannelOuter',
   				next : function(){
   				 bar.setLoadingStep(++STEP_CURSOR);
   				}
   			},
   			{
   				txt : '@downchannel@',//下行信道;
   				tableOuterId : 'CmDownChannelOuter',
   				next : function(){
                    bar.setLoadingStep(++STEP_CURSOR);
   				}
   			},
            {
                txt : '@cm.interface@',//CM;
                tableOuterId : 'cmifdiv',
                next : function(){
                    execQuery(URL_LIST[0],data,cmCpe);
                }
            },
            {
                txt : '@cm.serviceflow@',//CM;
                tableOuterId : 'cmsfdiv',
                next : function(){
                    execQuery(URL_LIST[1],data,cmCpe);
                }
            },
            {
                txt : '@cm.cpeInfo@',//CPE信息;
                tableOuterId : 'cpelistdiv',
                next : function(){
                    execQuery(URL_LIST[2],data,cmCpe);
                }
            },
   			{
   				txt : 'CPE<br />@CMCPE.lastSevenDayAction2@',
   				tableOuterId : 'cpeActOuter',
   				next : function(){
   					execQuery(URL_LIST[3],data,cmCpe);
   				}
   			},
   			{
   				txt : 'CM<br />@CMCPE.lastSevenDayAction2@',
   				tableOuterId : 'cmActOuter',
   				next : function(){
   					execQuery(URL_LIST[4],data,cmCpe);
   				}
   			},
   			{
   				txt : '@CM.serviceTypelog@',
   				tableOuterId : 'cmServiceTypeDiv',
   				next : function(){
   					execQuery(URL_LIST[5],data,cmCpe);
   				}
   			},
   			{
   				txt : 'OLT<br />@COMMON.alert@',
   				tableOuterId : 'oltAlertOuter',
   				next : function(){
   					execQuery(URL_LIST[6],data,cmCpe);
   				}
   			},
   			{
   				txt : 'CMTS<br />@COMMON.alert@',
   				tableOuterId : 'cmcAlertOuter',
   				next : function(){
   					execQuery(URL_LIST[7],data,cmCpe);
   				}
   			},
   			{
   				txt : 'OLT<br />@CMCPE.runTimeInfo@',
   				tableOuterId : 'oltRunTimeInfoOuter',
   				next : function(){
   					execQuery(URL_LIST[8],data,cmCpe);
   				}
   			},
   			{
   				txt : 'CMTS<br />@CMCPE.runTimeInfo@',
   				tableOuterId : 'cmcRunTimeOuter',
   				next : function(){
   					execQuery(URL_LIST[9],data,cmCpe);
   				}
   			},
   			{
   				txt : 'CM FLAP',
   				tableOuterId : 'cmFlapInfoOuter',
   				next : function(){
   					bar.complete();
   					execQuery(URL_LIST[10],data,cmCpe);
   				}
   			}
   		]
    };

    if(!EntityType.isOltType(cmCpe.typeId)){
    	PROGRESS_CONF.steps.splice(11,1);
    	PROGRESS_CONF.steps.splice(9,1);
    	page.isOltType = false;
    }else{
    	page.isOltType = true;
    }
    bar = new Nm3kProgressBar(PROGRESS_CONF);
	bar.init();
	$("#cancelProgress").fadeIn();
    bar.setLoadingStep(++STEP_CURSOR);
}

function execQuery($req, $data,cmCpe, $displayDiv){
	if($req.body){
		var tableId = $("#"+$req.body).parent().attr("id");
		showTableOuter( tableId );
		$("#mainTableContainer").scrollTop(10000);
	}
	if( !bar.loading ){return;}
	$.ajax({
        url: $req.url,dataType:'json',
        data: $data,
        success:function(json){
        	if(!$req.justifyInHandler){
            	bar.loadEnd("nm3kProgressSuccess","#03a9f5");
        	}
        	if(bar.loading){
        		$req.handler( json ,cmCpe);
        	}
        },error:function(e){
        	bar.loadEnd("nm3kProgressFail","#f00");
        	if(bar.loading){
        		showFirstTbody($req.body);
        	}
        },complete:function(){
        	//showTableOuter();
        	if(STEP_CURSOR < PROGRESS_CONF.steps.length-1 && bar.loading){
        		bar.setLoadingStep(++STEP_CURSOR);
        	}else{
        		$("#cancelProgress").fadeOut();
        		disabledAllRefreshBtn(false);
        	}
        }
	 });
}

function fdbAddressShow () {
    var cmCpe =  getQueryParams( page.selectedCm );
    var $data = "cmIp="+cmCpe.cmIp+"&cmId="+cmCpe.cmId+"&entityId="+cmCpe.entityId+"&cmcId="
        +cmCpe.cmcId+"&entityType="+cmCpe.typeId+"&cmMac="+cmCpe.statusMacAddress;
    window.top.createDialog('cmFdbAddress', "@CM.fdbinfo@", 800, 500, "/cmCpe/showFdbAddress.tv?"+$data, null, true, true);
}

function refreshTable( $url_index ,me){
	var $me = $(me),
	    tableId = $me.parent().find("table.dataTableAll").attr("id"),
	    num = $("#mainTableContainer a.rightTopLink:visible").index($me),
	    bResultSuccess = false; //记录ajax执行结果;
	disabledAllRefreshBtn(true);
	
	if($url_index === 0){
		$("#cmPropertyTableBtn,#CmUpChannelBtn, #CmDownChannelBtn ,#cmifRefreshBtn").attr({disabled: true});
		showLoadingTbody("cmPropertyTable");
		showLoadingTbody("CmUpChannel");
        showLoadingTbody("CmDownChannel");
        showLoadingTbody("cmif");
		bar.setLoading(0);
		bar.setLoading(1);
        bar.setLoading(2);
        bar.setLoading(3);
		bar.setLoading(3);
	}else{
		$me.attr({disabled: true});
		showLoadingTbody( tableId );
		bar.setLoading(num);
	}
	var cmCpe =  getQueryParams( page.selectedCm );
	var $data = "cmIp="+cmCpe.cmIp+"&cmId="+cmCpe.cmId+"&entityId="+cmCpe.entityId+"&cmcId="
				+cmCpe.cmcId+"&entityType="+cmCpe.typeId+"&cmMac="+cmCpe.statusMacAddress;
	var $req = URL_LIST[ $url_index ];
	$.ajax({
        url: $req.url,dataType:'json',
        data: $data,
        success:function(json){
        	$req.handler( json ,cmCpe);
        	bResultSuccess = true;
        	if($url_index === 0){ //第一个要特殊处理;
                if (json.cmStatus == null || !json.cmStatus.remoteQuery) {
                    if (json.cmStatus == null || !json.cmStatus.ping) {
                        bResultSuccess = false;
                    } else if (json.cmStatus == null || !json.cmStatus.snmpCheck) {
                        bResultSuccess = false;
                    }
                } else {
                    if (!json.cmStatus.remoteQueryState) {
                        bResultSuccess = false;
                    }
                }
        	}
        },error:function(e){
       		showFirstTbody( $req.body );
       		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.refreshFailure);
        },complete:function(){
        	if($url_index === 0){
        		$("#cmPropertyTableBtn,#CmUpChannelBtn, #CmDownChannelBtn").attr({disabled: false});
        		if(bResultSuccess){
        			bar.setLoadingEnd(0,"nm3kProgressSuccess","#03a9f5");
        			bar.setLoadingEnd(1,"nm3kProgressSuccess","#03a9f5");
                    bar.setLoadingEnd(2,"nm3kProgressSuccess","#03a9f5");
                    bar.setLoadingEnd(3,"nm3kProgressSuccess","#03a9f5");
        		}else{
        			bar.setLoadingEnd(0,"nm3kProgressFail","#f00");
        			bar.setLoadingEnd(1,"nm3kProgressFail","#f00");
                    bar.setLoadingEnd(2,"nm3kProgressFail","#f00");
                    bar.setLoadingEnd(3,"nm3kProgressFail","#f00");
        		}
        	}else{
        		$(me).attr({disabled: false});
        		if(bResultSuccess){
        			bar.setLoadingEnd(num,"nm3kProgressSuccess","#03a9f5");
        		}else{
        			bar.setLoadingEnd(num,"nm3kProgressFail","#f00");
        		}
        	};
        	disabledAllRefreshBtn(false);
        }
	});
}

function showDevice(e,id, entityName){
	e = e || window.event;
    if (e.stopPropagation) {
        e.stopPropagation();           // 火狐阻止冒泡
    } else {
        e.cancelBubble = true;         // IE阻止冒泡
    }
    if(e.preventDefault){
        e.preventDefault();
    }
	window.top.addView('entity-' + id, entityName,
		'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + id);
}

/**
 * selectFirst为true时表示默认选中第一项
 */
function queryData(selectFirst) {
	$("#queryButton").attr({disabled : 'disabled'});
	clearData();
    var cmMac = $("#cmmacCondition").val();
    var cpeMac = $("#cpemacCondition").val();
    var cpeIp = $("#cpeipCondition").val();
    var cmLocation = $("#cmLocationCondition").val();
    if (cmMac == null) {
        cmMac = "";
    }
    if (cpeMac == null) {
        cpeMac = "";
    }
    if (cpeIp == null) {
        cpeIp = "";
    }
    if (cmLocation == null) {
        cmLocation = "";
    }
    //进行MAC地址和IP地址的校验
    if(cmMac!=='' && !V.isFuzzyMacAddress(cmMac)){
    	top.afterSaveOrDelete({
   	      	title: I18N.COMMON.tip,
   	      	html: '<b class="orangeTxt">@CMCPE.MACERROR@</b>'
   	    });
   	    $('#cmmacCondition').focus();
   	    removeBtnDisabled();
   	    return;
    }
    // modify by fanzidong, CPE MAC 不支持模糊匹配
    if(cpeMac!=='' && !V.isMac(cpeMac)){
    	top.afterSaveOrDelete({
   	      	title: I18N.COMMON.tip,
   	      	html: '<b class="orangeTxt">@CMCPE.MACERROR@</b>'
   	    });
   	    $('#cpemacCondition').focus();
   	    removeBtnDisabled();
   	 	return;
    }
    // modify by fanzidong, CPE IP 不支持模糊匹配
    if(cpeIp!=='' && !top.IpUtil.isIpAddress(cpeIp)){
    	top.afterSaveOrDelete({
   	      	title: I18N.COMMON.tip,
   	      	html: '<b class="orangeTxt">@CMCPE.IPERROR@</b>'
   	    });
   	    $('#cpeipCondition').focus();
   	    removeBtnDisabled();
   	 	return;
    }
    
    $("#openLayerLeft").empty();
    $.ajax({
        type: "POST",
        url: "/cmCpe/cmCpeListByCondition.tv",
        data: "cmMac="+cmMac+"&cpeMac="+cpeMac+"&cpeIp="+cpeIp +"&cmLocation="+cmLocation,cache: false,
        dataType: "json",
        success: function(json) {
            $.each( json, function(i, n){
                var htmlString;
                var cmIp = n.StatusIpAddress;
                if (cmIp == null || cmIp == '0.0.0.0') {
                    cmIp = n.statusInetAddress;
                }
                if (EntityType.isOltType(n.typeId)) {
                	htmlString += '<div id="a'+i+'" href="javascript:;" class="leftPartAlink" onclick="showCm(this);">';
                	htmlString +=     '<p class="blueTxt">CM:'+n.statusMacAddress +'</p>';
            		htmlString +=     '<p>@CMC.title.Alias@:'+n.cmAlias+'</p>';
                	htmlString +=     '<p>IP:'+cmIp+'</p>';
            		htmlString +=     '<p>@CMCPE.oltIn@['+n.typeName+']'+n.name+'/'+n.ip+'</p>';
                	htmlString +=     '<p>OLT@CMC.label.name@:'+n.name+'</p>';
            		htmlString +=     '<p>OLT IP:<a href="#" class="yellowLink" onclick="showDevice(event,'+n.entityId+',\''+n.name+'\')">'+n.ip+'</a></p>';
                	htmlString +=     '<p>CC8800A:'+n.cmcName+'</p>'
                	htmlString +=     '<p>@upchannel@:'+n.statusUpChannelIfIndexString+'</p>'
                	htmlString +=     '<p>@CCMTS.downStreamChannel@:'+n.statusDownChannelIfIndexString+'</p>';
                	htmlString += '</div>';
                } else if (EntityType.isCmtsType(n.typeId)){
                   htmlString = '<div id="a'+i+'" href="javascript:;" class="leftPartAlink" onclick="showCm(this);">' +
                        '<p class="blueTxt">CM:'+n.statusMacAddress +'</p>'+
                        '<p>@CMC.title.Alias@:'+n.cmAlias+'</p>'+
                        '<p>IP:'+cmIp+'</p>'+
                        '<p>@CMCPE.inDeviceType@['+n.typeName+']</p>' +
                        '<p>@CMC.label.deviceName@:'+n.name+'</p>' +
                        '<p>@CMC.text.equipmentip@:<a href="#" class="yellowLink" onclick="showDevice(event,'+n.entityId+',\''+n.name+'\')">'+n.ip+'</a></p>' +
                        '<p>@upchannel@:'+n.cmtsUpDescr+'</p>' +
                        '<p>@CCMTS.downStreamChannel@:'+n.cmtsDownDescr+'</p>';
                        htmlString = htmlString + "</div>";
                } else {
                	htmlString = '<div id="a'+i+'" href="javascript:;" class="leftPartAlink" onclick="showCm(this);">' +
                    '<p class="blueTxt">CM:    '+n.statusMacAddress +'</p>'+
                    '<p>@CMC.title.Alias@:'+n.cmAlias+'</p>'+
                    '<p>IP:'+cmIp+'</p>'+
                    '<p>@CMCPE.inDeviceType@['+n.typeName+']</p>' +
                    '<p>@CMC.label.deviceName@:'+n.name+'</p>' +
                    '<p>@CMC.text.equipmentip@:<a href="#" class="yellowLink" onclick="showDevice(event,'+n.entityId+',\''+n.name+'\')">'+n.ip+'</a></p>' +
                    '<p>@upchannel@:'+n.statusUpChannelIfIndexString+'</p>' +
                    '<p>@CCMTS.downStreamChannel@:'+n.statusDownChannelIfIndexString+'</p>';

                    htmlString = htmlString + "</div>";
                }
                var html = $(htmlString);
                $("#openLayerLeft").append(html);
                html.data("data",n);
            });
            if(selectFirst==true){
            	$('#a0').trigger("onclick");
            }
        },
        error:function(e){
            showMessage(e);
        },
        complete : function(){
        	removeBtnDisabled();
        }
    });
}
function removeBtnDisabled(){
	$("#queryButton").removeAttr("disabled");
}
function showMessage(e){}

function clearData(){
	$("#cmping").text("");
    $("#cmsn").text("");
    $("#cmregmode").text("");
    $("#cmsoftware").text("");
    $("#cmuptime").text("");
    $("#configurationfilename").text("");
    $("#cmntp").text("");
    $("#cmtftp").text("");
    $("#cmdhcp").text("");
    
    
    $("#CmUpChannel>tbody:eq(0)>tr").empty();
    $("#CmDownChannel>tbody:eq(0)>tr").empty();
    $("#cpeAct>tbody:eq(0)>tr").empty();
    $("#cmAct>tbody:eq(0)>tr").empty();
    $("#powerAdj").html("");
    $("#range").html("");
    $("#flapIns").html("");
    $("#cmcType").text("");
    $("#interface").text("");
    $("#snr").text("");
    $("#bitErrorRate").text("");
    $("#unBitErrorRate").text("");
    $("#cmOnlineNum").text("");
    $("#cmOfflineNum").text("");
    $("#channelInOctetsRate").text("");
    $("#oltAlert>tbody:eq(0)>tr").empty();
    $("#cmcAlert>tbody:eq(0)>tr").empty();
    $("#ponId").text("");
    $("#txPower").text("");
    $("#rxPower").text("");
    
    $("#cmcRunTimeTable tbody:eq(0)").html('');
    $("#oltRunTimeInfoTable tbody:eq(0)").html('');
    $("#cmFlapInfo tbody:eq(0)").html('');
    $("#snrHisPerf").hide();
    $("#bitErrorRateHisPerf").hide();
    $("#unBitErrorRateHisPerf").hide();
    $("#cmOnlineNumHisPerf").hide();
    $("#cmOfflineNumHisPerf").hide();
    $("#channelInOctetsRateHisPerf").hide();
}
function cancelProgress(para){
	$("#cancelProgress").hide();
	bar.cancelLoading();
	//显示所有的table,让用户可以自己手动刷新;
	var aTableOuter;
	if(page.isOltType){
		aTableOuter = page.allTable; 
    }else{
    	aTableOuter = page.withoutOlt;
    }
	if(para === true){
		for(var i=0,len=aTableOuter.length; i<len; i++){
			var $outer = $("#"+aTableOuter[i]); 
			$outer.removeClass("displayNone");
			$outer.find("table.dataTableAll tbody:eq(1)").css({display:"none"});
		}
		disabledAllRefreshBtn(false);
	}
}
function hideAllTable(){
	var arr = page.allTable;
	for(var i=0,len=arr.length; i<len; i++){
		$("#"+arr[i]).addClass("displayNone");
	}
}
//设置所有刷新按钮的disabled属性;
function disabledAllRefreshBtn(para){
	$("#mainTableContainer .rightTopLink").attr({disabled: para});
} 
