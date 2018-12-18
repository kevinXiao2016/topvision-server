<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
	<title></title>
	<%@ include file="/include/nocache.inc"%>
	<%@ include file="/include/cssStyle.inc"%>
	<Zeta:Loader>
	    library ext
	    library zeta
	    PLUGIN  LovCombo
	    module cmc
	    css cmc/cmcRealtimeInfo
	    css css/white/disabledStyle
	    import /js/entityType
	    import cm.util.cmUtil
	</Zeta:Loader>
	<!-- 此处需要使用高版本的jquery库和highstock库，来解决动态获取数据的问题  -->
	<script src="/performance/js/jquery-1.8.3.js"></script>
	<script src="/performance/js/highstock.js"></script>
	<style type="text/css">
		.tableBg{width:410px;  background:#fff; position:absolute; left:0px; opacity:0.5; -moz-opacity:0.5; filter:alpha(opacity=50); z-index:2;}
		label.flagOpen, label.flagClose{padding-left: 20px;background: url(/images/flagOpen.gif) no-repeat 0px center;cursor: pointer;}
		label.flagClose{background: url(/images/flagClose.gif) no-repeat 0px center;}
	</style>
	<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
	<script type="text/javascript">
	var cmcId = ${ cmcId };
	var entityId = ${ entityId };
	var typeId = ${ typeId };
	var channelIds = '${ channelIds }';
	var toolBar = null;	
	var cmcRealtimeInfo;
	var cmcOpticalInfo;
	var cmcCmNum;
	var cmcCpeTypeNum;
	var qualityRangeResult;
	var cmtsCmQualityMaps;
	var seriesOptions = [];
	var startFlag = false;
	var snrToolBar
	var entityTypeCombox
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var sysElecUnit = '@{unitConfigConstant.elecLevelUnit}@';
	
	var snrUpdate;
	var snrRealTimeSwich = false;
	function stopSnr(){
		snrRealTimeSwich = false;
		window.clearInterval(snrUpdate)
		snrToolBar.getComponent("endSnr").disable();
		snrToolBar.getComponent("startSbr").enable();
	}
	var channelIndexString = ''
	function startSnr(){
		snrRealTimeSwich = true;
		channelIndexString = Ext.getCmp("channelSelect").getCheckedValue();
		var interval = Ext.getCmp("snrInterval").getValue() * 1000
		if(channelIndexString == null || channelIndexString == ""){
			window.top.showMessageDlg('@COMMON.tip@', '@CCMTS.SelectChannel@');
			return;
		}
		snrUpdate = setInterval(function() {
			var x = (new Date()).getTime()
			channelIndexString = Ext.getCmp("channelSelect").getCheckedValue();
			var channelId = Ext.getCmp("channelSelect").getCheckedDisplay().split(",");
			$.ajax({
				url: "/cmc/getCmcSnr.tv?entityId=" + cmcId + "&channelIndexString=" + channelIndexString,
				type: "post",
				dataType: "json",
				success: function(json){
					var ch1 = -1;
					var ch2 = -1;
					var ch3 = -1;
					var ch4 = -1;
				    for(var i = 0; i < channelId.length; i++){
				    	if(channelId[i] == 1){
				    		ch1 = json.channel1 / 10;
				    	}else if(channelId[i] == 2){
				    		ch2 = json.channel2 / 10;
				    	}else if(channelId[i] == 3){
				    		ch3 = json.channel3 / 10;
				    	}else if(channelId[i] == 4){
				    		ch4 = json.channel4 / 10;
				    	}
				    }
				    $('#container').highcharts().series[0].addPoint([x, ch1], true, true);
				    $('#container').highcharts().series[1].addPoint([x, ch2], true, true);
				    $('#container').highcharts().series[2].addPoint([x, ch3], true, true);
				    $('#container').highcharts().series[3].addPoint([x, ch4], true, true);
				}
			}) 
		}, interval);
		snrToolBar.getComponent("startSbr").disable();
		snrToolBar.getComponent("endSnr").enable();
	}
	
	function displayLoading(){
		var t = $("#firstColorfulTable").offset().top;
		var t2 = $("#lastColorfulTable").offset().top + $("#lastColorfulTable").outerHeight() - t;
		$("body").append('<div id="tableBg" class="tableBg" style="height:'+ t2 +'px; top:'+ t +'px;"></div>');
		$("#treeLoading").css({left:"153px", top:"300px"});
	}
	function createHighChart(){
		Highcharts.setOptions({
			global: {
				useUTC: false
			},
			credits:{enabled: false}
		});
		
		$('#container').highcharts('StockChart', {
			rangeSelector: {
				buttonTheme: { // styles for the buttons
	                r: 1,
	                padding: 3,
	                fill: 'none',
	                style: {
	                    color: '#039',
	    				cursor : 'pointer'
	                }
	            },
				buttons: [{
					count: 1,
					type: 'minute',
					text: '1@ENTITYSNAP.deviceUpTime.minute2@'
				},  {
					count: 5,
					type: 'minute',
					text: '5@ENTITYSNAP.deviceUpTime.minute2@'
				} ],
				
				labelStyle: {
		    		display: 'none',
		    		color: '#C0C0C0',
		    		fontWeight: 'bold'
		    	},
				inputEnabled: false,
				selected: 1
			},
			title : {
				text : '@cmc.snrrealtimecurvegraph@'
			},
			exporting: {
				enabled: false
			},
			yAxis: {
		    	labels: {
		    		formatter: function() {
		    			return this.value + "dB";
		    		}
		    	},
		    	min: 0,
		    	max: 50,
		        title: {text: "@COMMON.SignalNoise@"}
		    },
		    xAxis : {
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
			tooltip: {
		           formatter: function() {
						var s = '<b>'+ Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'</b><br/>';
						$.each(this.points, function(i, point) {
							if(point.y == -1){
								s += '<span class="tooltipSpan" style="color:'+ point.series.color + '">' + point.series.name 
								  + ':</span><span class="tooltipSpan" style="color:'+ point.series.color + '">' + '@Tip.failedGet@' + '</span><br/>';
							}else{
								s += '<span class="tooltipSpan" style="color:'+ point.series.color + '">' + point.series.name 
								  + ':</span><span class="tooltipSpan" style="color:'+ point.series.color + '">' + point.y + "dB" + '</span><br/>';
							}
						});
		           
						return s;
					}
			    },
			
			series : [{
				name : '@COMMON.SNRC1@',
				id : 'snr1',
				data : (function() {
					var data = [], time = (new Date()).getTime(), i;
					for( i = -100; i <= 0; i++) {
						data.push([
							time + i * 10000,
							-1
						]);
					}
					return data;
				})()
			},
			{
				name : '@COMMON.SNRC2@',
				id : 'snr2',
				data : (function() {
					var data = [], time = (new Date()).getTime(), i;
					for( i = -100; i <= 0; i++) {
						data.push([
							time + i * 10000,
							-1
						]);
					}
					return data;
				})()
			},
			{
				name : '@COMMON.SNRC3@',
				id : 'snr3',
				data : (function() {
					var data = [], time = (new Date()).getTime(), i;
					for( i = -100; i <= 0; i++) {
						data.push([
							time + i * 10000,
							-1
						]);
					}
					return data;
				})()
			},
			{
				name : '@COMMON.SNRC4@',
				id : 'snr4',
				data : (function() {
					var data = [], time = (new Date()).getTime(), i;
					for( i = -100; i <= 0; i++) {
						data.push([
							time + i * 10000,
							-1
						]);
					}
					return data;
				})()
			}]
		});
	};//end createHighChart;
	function createChartToolbar(){
		entityTypeCombox = new Ext.ux.form.LovCombo({
			fieldLabel : "@COMMON.selectChannel@",
			id : 'channelSelect',
			hideOnSelect : false,
			editable : false,
			width : 120,
			store : new Ext.data.JsonStore({
				url : "/cmc/channel/getUpStreamBaseInfo.tv?cmcId=" + cmcId ,
				root : 'data',
				async : false, 
				autoLoad : true,
				fields : [ "channelId", "channelIndex" ]
			}),
			displayField : 'channelId',
			valueField : 'channelIndex',
			triggerAction : 'all',
			emptyText : '@COMMON.selectChannel@',
			mode : 'local',
			beforeBlur : function() {
			},
			onSelect: function(record, index){
				if (this.fireEvent('beforeselect', this, record, index) !== false) {
				      record.set(this.checkField, !record.get(this.checkField));
				      if (this.store.isFiltered()) {
				        this.doQuery(this.allQuery)
				      }
				      this.setValue(this.getCheckedValue());
				      this.fireEvent('select', this, record, index)
				}
				if(this.value == ""){
					$('#container').highcharts().series[this.selectedIndex].hide();
				}else{
					$('#container').highcharts().series[this.selectedIndex].show();
				}
			}
		});
		
		var snrInterval = new Ext.form.ComboBox({
			id: 'snrInterval',
            name: 'perpage',
            width: 100,
            store: new Ext.data.ArrayStore({
                fields: ['id'],
                data: [
                    ['5'],
                    ['10'],
                    ['20'],
                    ['30'],
                    ['60']
                ]
            }),
            mode: 'local',
            value: '5',
            triggerAction: 'all',
            displayField: 'id',
            valueField: 'id',
            editable: false,
            forceSelection: true,
            listeners : { 
                'select' : function(combo,record,index) { 
                	window.clearInterval(snrUpdate);
                	if(snrRealTimeSwich){
                		startSnr();
                	}
                } 
            }
        });
	    
		snrToolBar = new Ext.Toolbar({
	        renderTo: "toolbar",
	        items: [
		       {text: '@COMMON.enable@', itemId:'startSbr', iconCls: 'bmenu_play' , handler: startSnr},'-',
		       {text: '@COMMON.disable@', itemId:'endSnr', iconCls: 'bmenu_stop', handler: stopSnr},'-',"@COMMON.Channel@:",
		       entityTypeCombox,'-',
		       "@COMMON.AcquisitionCycle@:", snrInterval
	       ]
		});
		snrToolBar.getComponent("endSnr").disable();
		snrToolBar.getComponent("startSbr").enable();
	}
	
	var series;
	var chart;
	$(function(){
		var isFirstClick = true;
		$("#topFlagP span").click(function(){
			var $label = $(this).find("label"),
			    $containerToolbar = $("#container, #toolbar");
			
			if( $label.hasClass("flagClose") ){//if closed;
				$label.attr("class","flagOpen");
				$containerToolbar.css({display: "block"});
				if(isFirstClick){
					isFirstClick = false;
					createChartToolbar();
					createHighChart(); 
					$('#container').highcharts().series[0].hide();
					$('#container').highcharts().series[1].hide();
					$('#container').highcharts().series[2].hide();
					$('#container').highcharts().series[3].hide(); 
					for(var i = 1; i < channelIds.length + 1; i++){
						if(channelIds.indexOf(i,0) > -1){
							$('#container').highcharts().series[i - 1].show();
						}
					}
				}
			}else{
				$label.attr("class","flagClose");
				$containerToolbar.css({display: "none"});
			}
		});
		
		$(".flagInfo").click(function(){
			var $table = $(this).parent().next();
			if( $table.get(0).tagName == "TABLE"){
				var $label = $(this).find("label"); 
				if( $label.hasClass("flagOpen") ){
					$label.attr("class","flagClose");
					$table.stop(true,true).fadeOut();
				}else{
					$label.attr("class","flagOpen");
					$table.stop(true,true).fadeIn();
				}
			}
		})
		
		
		$(".nm3kTip").live("click",function(){
			var $me = $(this);
			var cmIndexString = "";
			if($me.attr("alt").length > 0){
				cmIndexString = $me.attr("alt");
			}
			if(cmIndexString.length == 0){
				cmIndexString += -1;
			}else if(cmIndexString.length > 0){
				cmIndexString = cmIndexString.substr(0, cmIndexString.length - 1);
		    }
			addView("realtimeCmList", "@realtimeCm@", "entityTabIcon", encodeURI("/realtimecmlist/showRealtimeCmList.tv?c=" + Math.random() + "&cmcId=" + cmcId + "&cmIndexs=" + cmIndexString),true);
		});
		displayLoading();
		initTopToolBar();
		/* $("#showUserNameBtn").click(function(){
			var $me = $(this),$show = $("#putUserName");
			if( $me.hasClass("cirLeftLinkBtn") ){
				$me.attr({"class" : "cirRightLinkBtn"});
				$show.removeClass("displayNone");
			}else{
				$me.attr({"class" : "cirLeftLinkBtn"});
				$show.addClass("displayNone");
			}
		}) */
		
		//$("#snrRealInfo").css({display: "none"}
		
		if(typeId != null && (EntityType.isCcmtsWithoutAgentType(typeId))){
			$("#distributeConfigButton").css("display","none");
			$(".opticalInfo").css("display","none");
		}
		 $.ajax({
			url: "/cmc/getCmcRealTimeData.tv?cmcId=" + cmcId,
			type: "post",
			dataType: "json",
			success: function(json){
				cmcRealtimeInfo = json.cmcRealtimeInfo;
				cmcOpticalInfo = json.cmcOpticalInfo;
				cmcCmNum = json.cmcCmNum;
				cmcCpeTypeNum = json.cmcCpeTypeNum;
				qualityRangeResult = json.qualityRangeResult;
				cmtsCmQualityMaps = json.cmtsCmQualityMaps
				try
				{
					showCmcRealtimeInfo(cmcRealtimeInfo);
					if(cmcOpticalInfo != null){
						showCmcOpticalInfo(cmcOpticalInfo);
					}
					showCmQualityRange(qualityRangeResult)
					showTerminalCmStatic(cmtsCmQualityMaps,cmcCmNum);
					showTerminalCpeStatic(cmcCpeTypeNum);
				}
				catch(err)
				{
					
				}
				$("#tableBg, #treeLoading").css("display","none");
			}
		}) 
	});
	
	function showCmcOpticalInfo(cmcOpticalInfo){
		if(cmcOpticalInfo!=undefined||cmcOpticalInfo!=null){
			if (cmcOpticalInfo.rxPowerFloat == 0) {
				$("#rxPower").text("--");
			}else{
				$("#rxPower").text(cmcOpticalInfo.rxPowerFloat + " dBm");
			}
			if(cmcOpticalInfo.txPowerFloat  == 0){
				$("#txPower").text("--");
			}else{
				$("#txPower").text(cmcOpticalInfo.txPowerFloat + " dBm");
			}
			if(cmcOpticalInfo.biasCurrent  == 0){
				$("#biasCurrent").text("--");
			}else{
				$("#biasCurrent").text(cmcOpticalInfo.biasCurrent + " mA");
			}
			if(cmcOpticalInfo.voltage  == 0){
				$("#voltage").text("--");
			}else{
				$("#voltage").text(cmcOpticalInfo.voltage  + " mV");
			}
			if(cmcOpticalInfo.temperature == 0){
				$("#temperature").text("--");
			}else{
				$("#temperature").text(cmcOpticalInfo.cmcOpticalTemp + "  @{unitConfigConstant.tempUnit}@");
			}
			if(cmcOpticalInfo.waveLen  == 0) {
				$("#waveLen").text("--");
			}else{
				$("#waveLen").text(cmcOpticalInfo.waveLen + "  nm");	
			}
		}
	}
	
	function showCmcRealtimeInfo(cmcRealtimeInfo){
		$("#aliasTd").text(cmcRealtimeInfo.alias);
		$("#displayNameTd").text(cmcRealtimeInfo.displayName);
		$("#sysUpTimeTd").text(changeSysuptime(cmcRealtimeInfo.topCcmtsSysUpTime));
		$("#ipTd").text(cmcRealtimeInfo.ip);
		$("#macTd").text(cmcRealtimeInfo.topCcmtsSysMacAddr);
		$("#cpuTd").text(cmcRealtimeInfo.topCcmtsSysCPURatio + "%");
		$("#flashTd").text(cmcRealtimeInfo.topCcmtsSysFlashRatio  + "%");
		$("#memTd").text(cmcRealtimeInfo.topCcmtsSysRAMRatio  + "%");
		$("#swTd").text(cmcRealtimeInfo.topCcmtsSysSwVersion);
		$("#hwTd").text(cmcRealtimeInfo.topCcmtsSysHwVersion);
		$("#alertNumTd").text(cmcRealtimeInfo.alertNum);
		if(cmcRealtimeInfo.topCcmtsSysStatus == 4){
			$("#statusTd").text("@COMMON.online@");
		}else{
			$("#statusTd").text("@COMMON.offline@");
		}
		if(!cmcRealtimeInfo.snmpCheck){
			$("#statusTd").text("@CMCPE.snmpUnreachable@");
		}
		if(!cmcRealtimeInfo.pingCheck){
			$("#statusTd").text("@network/label.offline@");
		}
		$("#updateTimeTd").text(cmcRealtimeInfo.lastCollectTime);
}
	
	function addView(id, title, icon, url) {
	    window.parent.addView(id, title, icon, url,null,true);
	}
	
	function showCm(type){
		if(type == 1){
			//所有CM	
			addView("realtimeCmList", "@realtimeCm@", "entityTabIcon", encodeURI("/realtimecmlist/showRealtimeCmList.tv?c=" + Math.random() + "&cmcId=" + cmcId + "&cmStatus=JUMPSTATUS_ALLSHOW"),true);
		    return;
		}
		if(type == 2){
			//在线CM
		    addView("realtimeCmList", "@realtimeCm@", "entityTabIcon", encodeURI("/realtimecmlist/showRealtimeCmList.tv?c=" + Math.random() + "&cmcId=" + cmcId + "&cmStatus=JUMPSTATUS_ONLINE"),true);
		    return;
		}
		if(type == 3){
			//下线CM
			addView("realtimeCmList", "@realtimeCm@", "entityTabIcon", encodeURI("/realtimecmlist/showRealtimeCmList.tv?c=" + Math.random() + "&cmcId=" + cmcId + "&cmStatus=JUMPSTATUS_OFFLINE"),true);
		    return;
		}
		if(type == 5){
			//SNR异常CM
			var cmIndexs = ""
			$.each(cmcCmNum.snrExceptionList, function(i, index){
				cmIndexs += index + ',';
		    })
		    if(cmIndexs.length == 0){
		    	cmIndexs += -1;
		    }else if(cmIndexs.length > 0){
		    	cmIndexs.substr(0, cmIndexs.length - 1);
		    }
			addView("realtimeCmList", "@realtimeCm@", "entityTabIcon", encodeURI("/realtimecmlist/showRealtimeCmList.tv?c=" + Math.random() + "&cmcId=" + cmcId + "&cmIndexs=" + cmIndexs),true);
		    return;
		}
		if(type == 6){
			//上线中CM
			//init CM
			/* var cmIndexs = ""
			$.each(cmtsCmQualityMaps, function(index, cmtsCmQuality){
				if(cmtsCmQuality.statusValue != 6 && cmtsCmQuality.statusValue != 1){
					cmIndexs += index + ',';
				}
		    })
		    if(cmIndexs.length == 0){
		    	cmIndexs += -1;
		    }else if(cmIndexs.length > 0){
		    	cmIndexs.substr(0, cmIndexs.length - 1);
		    } */
		    //addView("realtimeCmList", "@realtimeCmList@", "apListIcon", encodeURI("/realtimecmlist/showRealtimeCmList.tv?c=" + Math.random() + "&cmcId=" + cmcId + "&cmIndexs=" + cmIndexs),true);
		    addView("realtimeCmList", "@realtimeCm@", "entityTabIcon", encodeURI("/realtimecmlist/showRealtimeCmList.tv?c=" + Math.random() + "&cmcId=" + cmcId + "&cmStatus=JUMPSTATUS_ONLINING"),true);
		    return;
		}
		if(type == 7){
			//电平异常CM
			var cmIndexs = ""
			$.each(cmcCmNum.powerExceptionList, function(i, index){
				cmIndexs += index + ',';
		    })
		    if(cmIndexs.length == 0){
		    	cmIndexs += -1;
		    }else if(cmIndexs.length > 0){
		    	cmIndexs.substr(0, cmIndexs.length - 1);
		    }
		    addView("realtimeCmList", "@realtimeCm@", "entityTabIcon", encodeURI("/realtimecmlist/showRealtimeCmList.tv?c=" + Math.random() + "&cmcId=" + cmcId + "&cmIndexs=" + cmIndexs),true);
		    return;
		} 
	}
	function showTerminalCmStatic(cmtsCmQualityMaps,cmcCmNum){
		var total = 0,online = 0,offline = 0;
		$.each(cmtsCmQualityMaps, function(index, cmtsCmQuality){
			total++;
			if(cmtsCmQuality.statusValue == 6||cmtsCmQuality.statusValue == 21||cmtsCmQuality.statusValue == 26||cmtsCmQuality.statusValue == 27||cmtsCmQuality.statusValue == 30||cmtsCmQuality.statusValue == 31) {
				online++;
			} else if (cmtsCmQuality.statusValue == 1){
				offline++;
			}
	    })
		$("#allCmNumTd").html(String.format('<a href="javascript:;" onclick="showCm({0})" class="yellowLink">'+ total +'</a>', 1));
		$("#onlineCmNumTd").html(String.format('<a href="javascript:;" onclick="showCm({0})" class="yellowLink">'+ online +'</a>', 2));
		$("#offlineCmNumTd").html(String.format('<a href="javascript:;" onclick="showCm({0})" class="yellowLink">'+ offline +'</a>', 3));
//		$("#cmDocsis3NumTd").html(String.format('<a href="javascript:;" onclick="showCm({0})" class="yellowLink">'+ cmcCmNum.docsis3Num +'</a>', 1));//3.0暂时展示所有
		$("#snrExceptionNumTd").html(String.format('<a href="javascript:;" onclick="showCm({0})" class="yellowLink">'+ cmcCmNum.snrExceptionNum +'</a>', 5));
		$("#initCmNumTd").html(String.format('<a href="javascript:;" onclick="showCm({0})" class="yellowLink">'+ (total - online - offline) +'</a>', 6));
		$("#powerExceptionTd").html(String.format('<a href="javascript:;" onclick="showCm({0})" class="yellowLink">'+ cmcCmNum.powerExceptionNum +'</a>', 7));
	}
	
	function showCpe(type){
		//0:all, 1:host, 2:mta, 3:stb
		addView("realtimeCpeList", "@realtimeCpeList@", "apListIcon", encodeURI("/realtimecmlist/showRealtimeCpeList.tv?c=" + Math.random() + "&cmcId=" + cmcId + "&cpeType="+type),true);
	}
	function showTerminalCpeStatic(cmcCpeTypeNum){
		$("#cpeNumTd").html(String.format('<a href="javascript:;" onclick="showCpe({0})" class="yellowLink">'+ (cmcCpeTypeNum.hostNum + cmcCpeTypeNum.mtaNum + cmcCpeTypeNum.stbNum + cmcCpeTypeNum.extenDevNum) +'</a>', 0));
		$("#hostCpeNumTd").html(String.format('<a href="javascript:;" onclick="showCpe({0})" class="yellowLink">'+ cmcCpeTypeNum.hostNum +'</a>', 1));
		$("#mtaCpeNumTd").html(String.format('<a href="javascript:;" onclick="showCpe({0})" class="yellowLink">'+ cmcCpeTypeNum.mtaNum +'</a>', 2));
		$("#stbCpeNumTd").html(String.format('<a href="javascript:;" onclick="showCpe({0})" class="yellowLink">'+ cmcCpeTypeNum.stbNum +'</a>', 3));
		$("#extenDevNumTd").html(String.format('<a href="javascript:;" onclick="showCpe({0})" class="yellowLink">'+ cmcCpeTypeNum.extenDevNum +'</a>', 4));
}
	
	function showCmQualityRange(qualityRangeResult){
				var usTxPowerQualityRange = qualityRangeResult.usTxPowerQualityRange;
			    var usSnrQualityRange = qualityRangeResult.usSnrQualityRange;
			    var dsRxPowerQualityRange = qualityRangeResult.dsRxPowerQualityRange;
			    var dsSnrQualityRange = qualityRangeResult.dsSnrQualityRange
			    $.each(usTxPowerQualityRange, function(key, qualityRange){
			    	rendererBox(key, qualityRange, "usTxPowerQualityRange");
			    })
			    
			    $.each(dsRxPowerQualityRange, function(key, qualityRange){
			    	rendererBox(key, qualityRange, "dsRxPowerQualityRange");
			    })
			    
			    $.each(usSnrQualityRange, function(key, qualityRange){
			    	rendererBox(key, qualityRange, "usSnrQualityRange");
			    })
			    
			    $.each(dsSnrQualityRange, function(key, qualityRange){
			   		rendererBox(key, qualityRange, "dsSnrQualityRange");
			    }) 
	}
	
	function rendererBox(key, qualityRange, type){
		var colorClass;
		var cmIndexList = qualityRange.cmIndexList;
		var cmIndexString = "";
		$.each(cmIndexList, function(key, index){
			cmIndexString += index + ",";
	    }) 
		//var html = String.format('<span style="display:block" onclick="showCmQuality(\'{0}\')">'+ qualityRange.count +'</span>', cmIndexString);
		var html = qualityRange.count;
    	if(qualityRange.rangeLevel == 1){
    		colorClass = "redBox"
    	}else if(qualityRange.rangeLevel == 2){
    		colorClass = "yellowBox"
    	}else if(qualityRange.rangeLevel == 3){
    		colorClass = "greenBox"
    	}
    	if(qualityRange.isNa){
    		//$("#" + type +"A_NA").html(qualityRange.count)
    		$("#" + type +"A_NA").text(html).attr("alt",cmIndexString);
	    	$("#" + type +"A_NA").attr("nm3kTip", qualityRange.count)
	    	$("#" + type +"L_NA").attr("class", colorClass)
	    	return;
    	}
    	//第一个值取end
    	if(qualityRange.start < -100000){
    		$("#" + type +"A" + key).text(html).attr("alt",cmIndexString);
	    	$("#" + type +"A" + key).attr("nm3kTip", qualityRange.count)
	    	$("#" + type +"S" + key).text(qualityRange.end)
	    	$("#" + type +"L" + key).attr("class", colorClass)
	    	return;
    	}
    	//最后两个
    	if(qualityRange.end > 100000){
    		$("#" + type +"A" + key).text(html).attr("alt",cmIndexString);
	    	$("#" + type +"A" + key).attr("nm3kTip", qualityRange.count)
	    	$("#" + type +"S" + key).text(qualityRange.start)
	    	$("#" + type +"S" + (key + 1)).text(qualityRange.start)
	    	$("#" + type + "L" + key).attr("class", colorClass)
	    	return;
    	}
    	$("#" + type +"A" + key).text(html).attr("alt",cmIndexString);
    	$("#" + type +"A" + key).attr("nm3kTip", qualityRange.count)
    	$("#" + type +"S" + key).text(qualityRange.start)
    	$("#" + type +"L" + key).attr("class", colorClass)
	} 
	
	function changeSysuptime(mss){
		var days = parseInt(mss / (100 * 60 * 60 * 24));  
	    var hours = parseInt((mss % (100 * 60 * 60 * 24)) / (100 * 60 * 60));  
	    var minutes = parseInt((mss % (100 * 60 * 60)) / (100 * 60));  
	    var seconds = parseInt((mss % (100 * 60)) / 100);  
	    return days + "@resources/FreeChart.D@" + hours + "@resources/FreeChart.H@" + minutes + "@resources/FreeChart.M@"  
	            + seconds + "@resources/FreeChart.S@"; 
	}
	
	function modifyDeviceInfo(){
		window.top.createDialog('renameEntity', I18N.COMMON.deviceInfo , 600, 375,
	            '/entity/showRenameEntity.tv?entityId=' + cmcId + "&pageId=" + window.parent.getActiveFrameId(), null, true, true);
	}
	
	function cmcReset() {
	    window.parent.showConfirmDlg("@resources/COMMON.tip@", "@CCMTS.confirmResetCcmts@", function(button, text) {
	        if (button == "yes") {
	            $.ajax({
	                url:'cmc/resetCmc.tv?cmcId=' + cmcId,
	                type:'POST',
	                dateType:'json',
	                success:function(response) {
	                    if (response.message == "success") {
	                    	top.afterSaveOrDelete({
		                        title: '@COMMON.tip@',
		                        html: '<b class="orangeTxt">@CCMTS.resetCcmtsSuccess@</b>'
		                    });
	                    } else {
	                        window.parent.showMessageDlg("@resources/COMMON.tip@", "@CCMTS.resetCcmtsFail@");
	                    }
	                },
	                error:function() {
	                },
	                cache:false
	            });
	        }
	    });
	}
	
	function refreshClick() {
		$("#tableBg, #treeLoading").css("display","block");
		displayLoading()
		$(".textClass").text("@resources/WorkBench.loading@")
		$.ajax({
			url: "/cmc/getCmcRealTimeData.tv?cmcId=" + cmcId,
			type: "post",
			dataType: "json",
			success: function(json){
				showCmcRealtimeInfo(json.cmcRealtimeInfo);
				showCmQualityRange(json.qualityRangeResult)
				showTerminalCmStatic(json.cmtsCmQualityMaps,json.cmcCmNum);
				showTerminalCpeStatic(json.cmcCpeTypeNum);
				showCmcOpticalInfo(json.cmcOpticalInfo);
				$("#tableBg, #treeLoading").css("display","none");
			}
		})
	}
	
	function changeEntityName(entityId, name){
		$("#aliasTd").text(name);
	}
    function initTopToolBar(){
    	toolbar2 = new Ext.Toolbar({
    		 renderTo: 'topToolBar',
    		 items : [
                       {                 
                       	cls: "mL10",
                           iconCls : 'bmenu_refresh',
                           text : "@refreshRealtimeInfo@",
                           handler : refreshClick
                       }
    		]
    	})
    }
    
    function openRemoteQuery(){
    	window.parent.showWaitingDlg("@COMMON.wait@", "@openRemoteQuery@" + "...",'waitingMsg','ext-mb-waiting');
    	$.ajax({
			url: "/cmc/openRemoteQuery.tv?cmcId=" + cmcId,
			type: "post",
			dataType: "json",
			success: function(json){
				window.parent.closeWaitingDlg();
				if(json){
					top.afterSaveOrDelete({
				      title: '@COMMON.tip@',
				      html: '<b class="orangeTxt">@operationSuccess@</b>'
				    });
				}else{
					window.parent.closeWaitingDlg();
					window.parent.showMessageDlg(I18N.COMMON.tip, "@operationFail@");
				}
			}
		})
    }
	function doRefresh(){}    
	
	function modifyTopoFolder(){
		window.top.createDialog('editTopoFolder', "@network/COMMON.editFolder@", 800, 500, '/entity/editTopoFolder.tv?entityId='+cmcId, null, true, true);
   		//var cmcIds = [];
   		//cmcIds[0] = cmcId
   		//window.top.createDialog('modifyTopoFolder', '@network/modifyEntityFolder@' , 600, 370, '/cmc/showModifyTopFolder.tv?cmcIdList=' + cmcIds.join(","), null, true, true)
    }
	
	function distributeConfig(){
		window.top.createDialog('distributeConfig', '@sendconfig@' , 700, 585, '/cmc/showDistributeConfig.tv?cmcId=' + cmcId, null, true, true)
	}
	
	function authload(){
	    if(!operationDevicePower){
	        $("#cmcReset").attr("disabled",true);
	        $("#openRemoteQuery").attr("disabled",true);
	        $("#distributeConfigButton").attr("disabled",true);
	    }
	}
	</script>
</head>
<body class="middleLineBg" onload = "authload();">
	<div id="topToolBar"></div>
	<div id="cmcSidePart" class="cmcSidePart" style="top:35px;">
		<p class="pannelTit">@network/NETWORK.managementTools@</p>
		<div class="toolMain">
			<dl class="leftFloatDl">
	            <dd>
	                <a id="cmcReset" href="javascript:cmcReset();" class="normalBtn"><span><i class="miniIcoPower"></i>@CCMTS.reset@</span></a>
	            </dd>
	            <dd>
	                <a href="javascript:modifyTopoFolder();" class="normalBtn"><span><i class="miniIcoEarth"></i>@network/changeEntityFolder@</span></a>
	            </dd>
	            <dd>
	                <a href="javascript:modifyDeviceInfo();" class="normalBtn"><span><i class="miniIcoEdit"></i>@cmc.deviceInfo@</span></a>
	            </dd>
	            <dd>
	                <a id="openRemoteQuery" href="javascript: openRemoteQuery();" class="normalBtn"><span><i class="miniIcoEdit"></i>@openRemoteQuery@</span></a>
	            </dd>
	            <dd>
	                <a href="javascript: distributeConfig();" class="normalBtn" id = "distributeConfigButton"><span><i class="miniIcoEdit"></i>@sendcommconfig@</span></a>
	            </dd>  
            </dl>
		</div>
		<p class="pannelTitWithTopLine clearBoth" id = "terminalStatic">@terminaStaticQuotaRange@</p>
		
		<p id="firstColorfulTable" class="edge5 txtCenter"><b>CM @upChannelTxPowerStatic@(@{unitConfigConstant.elecLevelUnit}@)</b></p>
		<dl class="colorTableDl">
			<dd>
				<ol class="colorTableOl">
					<li class="colorTableFirst"></li>
					<li class="redBox redBoxWidthLeftLine" id = "usTxPowerQualityRangeL1">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usTxPowerQualityRangeA1">0</a>
					</li>
					<li class="yellowBox" id = "usTxPowerQualityRangeL2">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usTxPowerQualityRangeA2">0</a>
					</li>
					<li class="greenBox" id = "usTxPowerQualityRangeL3">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usTxPowerQualityRangeA3">0</a>
					</li>
					<li class="greenBox" id = "usTxPowerQualityRangeL4">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usTxPowerQualityRangeA4">0</a>
					</li>
					<li class="greenBox" id = "usTxPowerQualityRangeL5">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usTxPowerQualityRangeA5">0</a>
					</li>
					<li class="yellowBox" id = "usTxPowerQualityRangeL6">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usTxPowerQualityRangeA6">0</a>
					</li>
					<li class="redBox" id = "usTxPowerQualityRangeL7">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usTxPowerQualityRangeA7">0</a>
					</li>
					<li class="lastArr"></li>
					<li class="redBox redBoxWidthLeftLine" id = "usTxPowerQualityRangeL_NA">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usTxPowerQualityRangeA_NA">0</a>
					</li>
				</ol>
				<span class="colorTxt firstTxt" id = "usTxPowerQualityRangeS1">0</span>
				<span class="colorTxt left35" id = "usTxPowerQualityRangeS2">0</span>
				<span class="colorTxt left62" id = "usTxPowerQualityRangeS3">0</span>
				<span class="colorTxt left92" id = "usTxPowerQualityRangeS4">0</span>
				<span class="colorTxt left120" id = "usTxPowerQualityRangeS5">0</span>
				<span class="colorTxt left149" id = "usTxPowerQualityRangeS6">0</span>
				<span class="colorTxt left178" id = "usTxPowerQualityRangeS7">0</span>
				<span class="colorTxt left204 lastTxt" id = "usTxPowerQualityRangeS8">0</span>
				<span class="colorTxt left244" id = "usTxPowerQualityRangeS_NA">N/A</span>
			</dd>
		</dl>
		<p class="edge5 txtCenter"><b>CM @downChannelTxPowerStatic@(@{unitConfigConstant.elecLevelUnit}@)</b></p>
		<dl class="colorTableDl">
			<dd>
				<ol class="colorTableOl">
					<li class="colorTableFirst"></li>
					<li class="redBox redBoxWidthLeftLine" id = "dsRxPowerQualityRangeL1">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsRxPowerQualityRangeA1">0</a>
					</li>
					<li class="yellowBox" id = "dsRxPowerQualityRangeL2">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsRxPowerQualityRangeA2">0</a>
					</li>
					<li class="greenBox" id = "dsRxPowerQualityRangeL3">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsRxPowerQualityRangeA3">0</a>
					</li>
					<li class="greenBox" id = "dsRxPowerQualityRangeL4">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsRxPowerQualityRangeA4">0</a>
					</li>
					<li class="greenBox" id = "dsRxPowerQualityRangeL5">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsRxPowerQualityRangeA5">0</a>
					</li>
					<li class="yellowBox" id = "dsRxPowerQualityRangeL6">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsRxPowerQualityRangeA6">0</a>
					</li>
					<li class="redBox" id = "dsRxPowerQualityRangeL7">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsRxPowerQualityRangeA7">0</a>
					</li>
					<li class="lastArr"></li>
					<li class="redBox redBoxWidthLeftLine" id = "dsRxPowerQualityRangeL_NA">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsRxPowerQualityRangeA_NA">0</a>
					</li>
				</ol>
				<span class="colorTxt firstTxt" id = "dsRxPowerQualityRangeS1">0</span>
				<span class="colorTxt left35" id = "dsRxPowerQualityRangeS2">0</span>
				<span class="colorTxt left62" id = "dsRxPowerQualityRangeS3">0</span>
				<span class="colorTxt left92" id = "dsRxPowerQualityRangeS4">0</span>
				<span class="colorTxt left120" id = "dsRxPowerQualityRangeS5">0</span>
				<span class="colorTxt left149" id = "dsRxPowerQualityRangeS6">0</span>
				<span class="colorTxt left178" id = "dsRxPowerQualityRangeS7">0</span>
				<span class="colorTxt left204 lastTxt" id = "dsRxPowerQualityRangeS8">0</span>
				<span class="colorTxt left244" id = "dsRxPowerQualityRangeS_NA">N/A</span>
			</dd>
		</dl>
		<p class="edge5 txtCenter"><b>CM @upchannelSnrStaic@(dB)</b></p>
		<dl class="colorTableDl">
			<dd>
				<ol class="colorTableOl">
					<li class="colorTableFirst"></li>
					<li class="redBox redBoxWidthLeftLine" id = "usSnrQualityRangeL1">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usSnrQualityRangeA1">0</a>
					</li>
					<li class="redBox" id = "usSnrQualityRangeL2">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usSnrQualityRangeA2">0</a>
					</li>
					<li class="yellowBox" id = "usSnrQualityRangeL3">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usSnrQualityRangeA3">0</a>
					</li>
					<li class="yellowBox" id = "usSnrQualityRangeL4">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usSnrQualityRangeA4">0</a>
					</li>
					<li class="greenBox" id = "usSnrQualityRangeL5"> 
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usSnrQualityRangeA5">0</a>
					</li>
					<li class="greenBox" id = "usSnrQualityRangeL6">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usSnrQualityRangeA6">0</a>
					</li>
					<li class="greenBox" id = "usSnrQualityRangeL7">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usSnrQualityRangeA7">0</a>
					</li>
					<li class="lastArr"></li>
					<li class="redBox redBoxWidthLeftLine" id = "usSnrQualityRangeL_NA">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "usSnrQualityRangeA_NA">0</a>
					</li>
				</ol>
				<span class="colorTxt firstTxt" id = "usSnrQualityRangeS1">0</span>
				<span class="colorTxt left35" id = "usSnrQualityRangeS2">0</span>
				<span class="colorTxt left62" id = "usSnrQualityRangeS3">0</span>
				<span class="colorTxt left92" id = "usSnrQualityRangeS4">0</span>
				<span class="colorTxt left120" id = "usSnrQualityRangeS5">0</span>
				<span class="colorTxt left149" id = "usSnrQualityRangeS6">0</span>
				<span class="colorTxt left178" id = "usSnrQualityRangeS7">0</span>
				<span class="colorTxt left204 lastTxt" id = "usSnrQualityRangeS8">0</span>
				<span class="colorTxt left244" id = "usSnrQualityRangeS_NA">N/A</span>
			</dd>
		</dl>
		<p class="edge5 txtCenter"><b>CM @downchannelSnrStaic@(dB)</b></p>
		<dl class="colorTableDl">
			<dd>
				<ol class="colorTableOl">
					<li class="colorTableFirst"></li>
					<li class="redBox redBoxWidthLeftLine" id = "dsSnrQualityRangeL1">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id="dsSnrQualityRangeA1">0</a>
					</li>
					<li class="redBox" id = "dsSnrQualityRangeL2">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsSnrQualityRangeA2">0</a>
					</li>
					<li class="yellowBox" id = "dsSnrQualityRangeL3">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsSnrQualityRangeA3">0</a>
					</li>
					<li class="yellowBox" id = "dsSnrQualityRangeL4">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" alt="" name="" id = "dsSnrQualityRangeA4" >0</a>
					</li>
					<li class="greenBox" id = "dsSnrQualityRangeL5">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsSnrQualityRangeA5">0</a>
					</li>
					<li class="greenBox" id = "dsSnrQualityRangeL6">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsSnrQualityRangeA6">0</a>
					</li>
					<li class="greenBox" id = "dsSnrQualityRangeL7">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsSnrQualityRangeA7">0</a>
					</li>
					<li class="lastArr"></li>
					<li class="redBox redBoxWidthLeftLine" id = "dsSnrQualityRangeL_NA">
						<a class="nm3kTip" href="javascript:;" nm3kTip="0" id = "dsSnrQualityRangeA_NA">0</a>
					</li>
				</ol>
				<span class="colorTxt firstTxt" id = "dsSnrQualityRangeS1">0</span>
				<span class="colorTxt left35" id = "dsSnrQualityRangeS2">0</span>
				<span class="colorTxt left62" id = "dsSnrQualityRangeS3">0</span>
				<span class="colorTxt left92" id = "dsSnrQualityRangeS4">0</span>
				<span class="colorTxt left120" id = "dsSnrQualityRangeS5">0</span>
				<span class="colorTxt left149" id = "dsSnrQualityRangeS6">0</span>
				<span class="colorTxt left178" id = "dsSnrQualityRangeS7">0</span>
				<span class="colorTxt left204 lastTxt" id = "dsSnrQualityRangeS8">0</span>
				<span class="colorTxt left244" id = "dsSnrQualityRangeS_NA">N/A</span>
			</dd>
		</dl>
		</div>
	</div>
	<div class="cmcMainPart">
	<div class="edge5 pT10" id="snrRealInfo">
			<p class="flagP" id="topFlagP"><span class="flagInfo"><label class="flagClose" id="label_cmPerp">@cmc.snrrealtimecurvegraph@</label></span></p>
			<div id="toolbar"></div>
			<div id="container"></div>
	</div>
		
		<div class="edge5 pT10">
			<p class="flagP"><span class="flagInfo"><label class="flagOpen">@text.baseInfo@</label></span></p>
			<table class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th width="25%">@cmcrealtimeType@</th>
						<th width="25%">@resources/FAULT.paramHeader@</th>
						<th width="25%">@cmcrealtimeType@</th>
						<th width="25%">@resources/FAULT.paramHeader@</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="rightBlueTxt">@resources/MAIN.templateRootNode@:</td>
						<td id = "displayNameTd" class = "textClass">@resources/WorkBench.loading@</td>
						<td class="rightBlueTxt">@CCMTS.entityName@:</td>
						<td id = "aliasTd" class="textClass wordBreak">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@resources/COMMON.lastRefreshTime@:</td>
						<td id = "updateTimeTd" class = "textClass">@resources/WorkBench.loading@</td>
						<td class="rightBlueTxt">@resources/topo.virtualDeviceList.onlineTime@:</td>
						<td id = "sysUpTimeTd" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@resources/SYSTEM.management@ IP:</td>
						<td id = "ipTd" class = "textClass">@resources/WorkBench.loading@</td>
						<td class="rightBlueTxt">MAC:</td>
						<td id = "macTd" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@resources/FreeChart.alertCount@:</td>
						<td id = "alertNumTd" class = "textClass">@resources/WorkBench.loading@</td>
						<td class="rightBlueTxt">@network/NETWORK.stateHeader@:</td>
						<td id = "statusTd" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">CPU:</td>
						<td id = "cpuTd" class = "textClass">@resources/WorkBench.loading@</td>
						<%--<td class="rightBlueTxt">@network/ap.chart.cpuTemperature@:</td>--%>
						<%--<td id = "cpuTempTd" class = "textClass">@resources/WorkBench.loading@</td>--%>
                        <td class="rightBlueTxt">Flash:</td>
                        <td id= "flashTd" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">MEM:</td>
						<td id = "memTd" class = "textClass">@resources/WorkBench.loading@</td>
                        <td class="rightBlueTxt">@CMC.text.softwarevision@:</td>
                        <td id = "swTd" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@CMC.label.hardVersion@:</td>
						<td id = "hwTd" class = "textClass">@resources/WorkBench.loading@</td>
						<td></td>
						<td></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="edge5" id="lastColorfulTable">
			<p class="flagP"><span class="flagInfo"><label class="flagOpen">@terminaStaticCmNum@</label></span></p>
			<table class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th width="25%">@PERF.mo.Index@</th>
						<th width="25%">@CMCPE.value@</th>
						<th width="25%">@PERF.mo.Index@</th>
						<th width="25%">@CMCPE.value@</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="rightBlueTxt">@CM.totalNum@:</td>
						<td id = "allCmNumTd" class = "textClass">@resources/WorkBench.loading@</a></td>
						<td class="rightBlueTxt">@CCMTS.view.cmNumOnline.ytitle@:</td>
						<td id = "onlineCmNumTd" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@CCMTS.view.cmNumOffline.ytitle@:</td>
						<td id = "offlineCmNumTd" class = "textClass">@resources/WorkBench.loading@</td>
						<td class="rightBlueTxt">@CCMTS.view.cmNumDoingOnline.ytitle@:</td>
						<td id = "initCmNumTd" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@powerException@:</td>
						<td id = "powerExceptionTd" class = "textClass">@resources/WorkBench.loading@</td>
						<td class="rightBlueTxt">@snrException@:</td>
						<td id = "snrExceptionNumTd" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@COMMON.online@CPE:</td>
						<td id = "cpeNumTd" class = "textClass">@resources/WorkBench.loading@</td>
                        <td class="rightBlueTxt">HOST:</td>
                        <td id = "hostCpeNumTd" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">MTA:</td>
						<td id = "mtaCpeNumTd" class = "textClass">@resources/WorkBench.loading@</td>
                        <td class="rightBlueTxt">STB:</td>
                        <td id = "stbCpeNumTd" class = "textClass" colspan="3">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@extenDevNum@:</td>
						<td id = "extenDevNumTd" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<%--<tr>--%>
                        <%--<td class="rightBlueTxt">CM 3.0:</td>--%>
                        <%--<td id = "cmDocsis3NumTd" class = "textClass">@resources/WorkBench.loading@</td>--%>
					<%--</tr>--%>
				</tbody>
			</table>
		</div>
		<div class="edge6 opticalInfo" id="lastColorfulTable">
			<p class="flagP"><span class="flagInfo"><label class="flagOpen">@CCMTS.opticalInfo@</label></span></p>
			<table class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th width="25%">@PERF.mo.Index@</th>
						<th width="25%">@CMCPE.value@</th>
						<th width="25%">@PERF.mo.Index@</th>
						<th width="25%">@CMCPE.value@</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="rightBlueTxt">@CCMTS.txPower@:</td>
						<td id = "txPower" class = "textClass"><a href="javascript:;" onclick="msg()">@resources/WorkBench.loading@</a></td>
						<td class="rightBlueTxt">@CCMTS.rxPower@:</td>
						<td id = "rxPower" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@CCMTS.biasCurrent@:</td>
						<td id = "biasCurrent" class = "textClass">@resources/WorkBench.loading@</td>
						<td class="rightBlueTxt">@CCMTS.voltage@:</td>
						<td id = "voltage" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@CCMTS.temperature@:</td>
						<td id = "temperature" class = "textClass">@resources/WorkBench.loading@</td>
						<td class="rightBlueTxt">@CCMTS.waveLen@:</td>
						<td id = "waveLen" class = "textClass">@resources/WorkBench.loading@</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!-- <a href="javascript:;" class="cirLeftLinkBtn" id="showUserNameBtn"></a> -->
	<div id="putUserName" class="displayNone">
		<table  border="0" cellspacing="0" cellpadding="0" rules="none">
			<tr>
				<td>Username:</td>
				<td class="pR5"><input type="text" class="normalInput w80" /></td>
				<td>Password:</td>
				<td class="pR5"><input type="password" class="normalInput w80" /></td>
				<td>EnablePassword:</td>
				<td class="pR5"><input type="password" class="normalInput w80" /></td>
				<td><a href="javascript:;" class="normalBtn"><span>@herfFolderProperty.modify@</span></a></td>
			</tr>
		</table>
	</div>
	<div id="treeLoading" class="treeLoading" style="display: block;">
		@resources/WorkBench.loading@
	</div>
</body>
</Zeta:HTML>