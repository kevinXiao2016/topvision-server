$(document).ready(function(){
	//创建性能指标组下拉框
	var groupStore = new Ext.data.JsonStore({
		//proxy: new Ext.data.HttpProxy({url:'/cmcPerfGraph/loadCmcPerfTargetGroups.tv', async: false}),
		url:'/cmcPerfGraph/loadCmcPerfTargetGroups.tv?cmcId='+cmcId,
		fields: ['value', 'text'],
        sortInfo:{field:'value',direction:'asc'}
	});
	comboGroup = new Ext.form.ComboBox({
		store: groupStore,
		emptyText: '@Tip.select@',
		triggerAction: 'all',
		valueField: 'value',
		displayField: 'text',
		value: 'service',
		applyTo: 'comboGroup',
		mode:'local',
		width: 150,
		editable: false,
		listeners: {
			"select": function(){
				comboPerfTarget.reset();//性能指标下拉框
				comboSubPerfTarget.reset();//子指标下拉框
				targetStore.load({params:{groupName:comboGroup.getValue(),deviceType:deviceType}});
				//隐藏子指标
				$('#subPerfTd').hide();
				$('#subCat').next().andSelf().hide();
			}
		}
	});
	/**
	 * 1，CC的性能展示列表中不支持CM FLAP的性能显示
	 * 需要将CM flap的指标组从下拉表中删除
	 * 2，由于filterby原始定义：return false/true对combobox的数据过滤不起作用，因此，稍作修改store.remove(record);
	 */
	/*groupStore.on("load", function(store ,records) {
		store.filterBy(function(record, id){
 			var result = true;
 			if(record == null){
 				alert(record)
 			}
			if(record.data.value =='cmflapGrp'||record.data.value =='Performance.cmflapGrp'){
				result = false;
				store.remove(record);
			}
			return result;
		});
	}) ;*/
	//创建性能指标下拉框
	var targetStore = new Ext.data.JsonStore({
		//proxy: new Ext.data.HttpProxy({url:'/cmcPerfGraph/loadPerfTargetsByGroup.tv', async: false}),
		url:'/cmcPerfGraph/loadPerfTargetsByGroup.tv?cmcId='+cmcId,
		fields: ['value', 'text'],
        sortInfo:{field:'value',direction:'asc'}
	});
	comboPerfTarget = new Ext.form.ComboBox({
		store: targetStore,
		emptyText: '@Tip.select@',
		triggerAction: 'all',
		valueField: 'value',
		displayField: 'text',
		applyTo: 'comboPerfTarget',
		mode:'local',
		width: 195,
		editable: false,
		listeners: {
			"select": function(){
				var target = comboPerfTarget.getValue();
				comboSubPerfTarget.reset();
				if($.inArray(target, ["cmc_optLink", "cmc_moduleTemp", "cmc_ber"])!=-1){
					//展示子指标
					subPerfTargetStore.load({params:{perfTarget:target}});
					$('#subPerfTd').show();
					if(target == 'cmc_optLink'){
						$('#subText').text("@Performance.optTarget@" + ":");
					}
					if(target == 'cmc_moduleTemp'){
						$('#subText').text("@Performance.module@" + ":");
					}
					if(target == 'cmc_ber'){
						$('#subText').text("@Performance.berTarget@" + ":");
					}
				}else{
					$('#subPerfTd').hide();
				}
				
				Ext.getCmp("portSelect").setValue("");
				
				if($.inArray(target, ["cmc_snr", "cmc_ber"])!=-1){
					labelText = "@Performance.channel@";
					subCatStore.removeAll();
					subCatStore.load({params:{entityId: cmcId, channelType:"US"}});
					$("#subCat").text(labelText + ":").next().andSelf().show();
				}else if($.inArray(target, ["cmc_channelSpeed", "channelUsed"])!=-1){
					labelText = "@Performance.channel@";
					subCatStore.removeAll();
					subCatStore.load({params:{entityId: cmcId, channelType:"All"}});
					$("#subCat").text(labelText + ":").next().andSelf().show();
				}else{
					$("#subCat").text("").next().andSelf().hide();
				}
			}
		}
	});
	//创建子指标下拉框
	window.subPerfTargetStore = new Ext.data.JsonStore({
		//proxy:new Ext.data.HttpProxy({url:'/cmcPerfGraph/loadCmcSubPerfTargets.tv', async: false}),
		url:'/cmcPerfGraph/loadCmcSubPerfTargets.tv?cmcId=' + cmcId,
		fields: ['value', 'text'],
		sortInfo:{field:'value',direction:'asc'}
	});
	window.comboSubPerfTarget = new Ext.form.ComboBox({
		store: subPerfTargetStore,
		emptyText: '@Tip.select@',
		triggerAction: 'all',
		valueField: 'value',
		displayField: 'text',
		applyTo: 'subPerftargets',
		mode:'local',
		width: 208,
		editable: false,
		listeners:{
			'select':function(){
				//清空多选下拉框
				Ext.getCmp("portSelect").setValue("");
				subCatStore.reload();
			}
		}
	});	
	labelText = "@Performance.channel@";
	subCatStore = new Ext.data.JsonStore({
		//proxy:new Ext.data.HttpProxy({url:'/cmcPerfGraph/loadCmcChannelList.tv', async: false}),
		url:'/cmcPerfGraph/loadCmcChannelList.tv',
		baseParams: {entityId: cmcId, channelType:"US"},
        fields: ['value', 'text','status'],
        sortInfo:{field:'value',direction:'asc'}
	});
	subCatStore.load();
	var channelSelector = new Ext.ux.form.LovCombo({
        width: 150,
        id:'portSelect',
        hideOnSelect : true,
        editable : false,
        renderTo:"portIndex",  
        store : subCatStore,
        valueField: 'value',
		displayField: 'text',
        triggerAction : 'all',
        mode:'local',
        emptyText : "@Tip.select@",  
        //tpl : '<tpl for="."><div class="'+cls+'-item">{' + this.displayField + '}</div></tpl>';
		tpl : //'<tpl for="."><div class="x-combo-list-item">{text}</div></tpl>',
			['<tpl for=".">',
	         '<div class="x-combo-list-item">',
	         '<img src="' + Ext.BLANK_IMAGE_URL + '" class="ux-lovcombo-icon ux-lovcombo-icon-',
	         '{[values.checked?"checked":"unchecked"]}">',
	         '<img src=\'{[values.status ? "/images/fault/trap_on.png" : "/images/fault/trap_off.png" ]}\' style="float:left;" />',
	         '<div class="ux-lovcombo-item-text">{text}</div>',
	         '</div>',
	         '</tpl>'
	         ].join(""),
        beforeBlur : function(){},
        listeners :{
       	beforeselect : function(c,r,i){
       		var v = c.value;
       		if(v){
	        	var isSel = $("#" + c.list.id).find("img.ux-lovcombo-icon").eq(i).hasClass("ux-lovcombo-icon-unchecked") ? true : false;
	        	var arr = v.split(c.separator);
	        	if(arr.length == 4 && isSel){
	        		$("#" + c.list.id).find("img.ux-lovcombo-icon-checked").eq(0).click();
	        	}
       		}
       	} 
       }
	}) 
	var lastMonth = new Date();
	var current = new Date();
	lastMonth.setTime(lastMonth.getTime()-(7*24*60*60*1000));
	
	stTime = new Ext.ux.form.DateTimeField({
		width:150,
		value : lastMonth,
		editable: false,
		renderTo:"startTime",
		emptyText:'@Tip.pleaseEnterTime@',
	    blankText:'@Tip.pleaseEnterTime@'
	});
	etTime = new Ext.ux.form.DateTimeField({
		width:195,
		value : current,
		editable: false,
		renderTo:"endTime",
		emptyText:'@Tip.pleaseEnterTime@',
	    blankText:'@Tip.pleaseEnterTime@'
	});
	nm3kPickData({
	    startTime : stTime,
	    endTime : etTime,
	    searchFunction : query
	});
	
	//初始化数据
	groupStore.load({
		callback:function(){
			if(targetFlag==''||targetFlag==null){
				comboGroup.setValue('cmc_signalQuality');
			}else{
				comboGroup.setValue('cmc_netInfo');
			}
			comboGroup.fireEvent('select');
//			comboGroup.setValue('cmc_signalQuality');
//			comboGroup.fireEvent('select');
			targetStore.load({
				params:{groupName:comboGroup.getValue(),deviceType:deviceType},
				callback:function(){
					if(targetFlag==''||targetFlag==null){
						comboPerfTarget.setValue('cmc_snr');
					}else{
						switch(targetFlag){
						case '1':
							comboPerfTarget.setValue('cmc_onlineRatio');
							break;
						case '2':
							comboPerfTarget.setValue('cmc_ccupFlow');
							break;
						case '3':
							comboPerfTarget.setValue('cmc_cmDownFlow');
							break;
						case '4':
							comboPerfTarget.setValue('cmc_cmTxAvg');
							break;
						case '5':
							comboPerfTarget.setValue('cmc_cmTxNotInRange');
							break;
						case '6':
							comboPerfTarget.setValue('cmc_cmReAvg');
							break;
						case '7':
							comboPerfTarget.setValue('cmc_cmReNotInRange');
							break;
						case '8':
							comboPerfTarget.setValue('cmc_upSnrAvg');
							break;
						case '9':
							comboPerfTarget.setValue('cmc_upSnrNotInRange');
							break;
						case '10':
							comboPerfTarget.setValue('cmc_downSnrAvg');
							break;
						case '11':
							comboPerfTarget.setValue('cmc_downSnrNotInRange');
							break;
						}
					}
					
					//获取图表DIV的高度
					
					if(targetFlag==''||targetFlag==null){
						labelText = "@Performance.channel@";
						$("#subCat").text(labelText + ":").next().andSelf().show();
						subCatStore.load({
							params:{entityId: cmcId, channelType:"US"},
							callback:function(){
								if(channelIndex==''){//added by wubo  2017.01.23
									//Ext.getCmp("portSelect").selectAll();
									var $cmp = Ext.getCmp("portSelect");
									var count = 4, cursor = 0;
									$cmp.getStore().each(function( record ){
										if(record.data.status && cursor < count ){
											record.set($cmp.checkField, true);
											cursor++;
										}
									});
									$cmp.setValue($cmp.getCheckedValue());
									$("#container").height($(window).height()-$("#titleBar").outerHeight(true)-$("#searchDiv").outerHeight(true)-50);
									if(!nmProject){
										query();
									}
								}
							}
						});
					}else{
						$("#container").height($(window).height()-$("#titleBar").outerHeight(true)-$("#searchDiv").outerHeight(true)-50);
						if(!nmProject){
							query();
						}
					}
					
				}
			});
		}
	});
});

var query = function(){
	var startTime = stTime.value.format("yyyy-mm-dd HH:MM:ss");
	var endTime = etTime.value.format("yyyy-mm-dd HH:MM:ss");
	if(startTime>endTime){
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
				html: '<b class="orangeTxt">@Tip.timeLimit@</b>'
   		});
		return false;
	}
	//获取需要传递到后台的参数，包括设备ID、指标名称、出入方向(针对流量)、indexs、起始时间
	//获取指标名称和方向
	var perfTargetName = comboPerfTarget.getValue();
	var direction = 0;
	if($.inArray(perfTargetName, ["upLinkInFlow"])!=-1){
		perfTargetName = "cmc_" + perfTargetName.replace('In','');
		direction = -1;
	}else if($.inArray(perfTargetName, ["upLinkInUsed"])!=-1){
		direction = -1;
	}else if($.inArray(perfTargetName, ["upLinkOutFlow"])!=-1){
		perfTargetName = "cmc_" + perfTargetName.replace('Out','');
		direction = 1;
	}else if($.inArray(perfTargetName, ["upLinkOutUsed"])!=-1){
		direction = 1;
	}if($.inArray(perfTargetName, ["macInFlow"])!=-1){
		perfTargetName = "cmc_" + perfTargetName.replace('In','');
		direction = -1;
	}else if($.inArray(perfTargetName, ["macOutFlow"])!=-1){
		perfTargetName = "cmc_" + perfTargetName.replace('Out','');
		direction = 1;
	}else if($.inArray(perfTargetName, ["cmc_optLink", "cmc_moduleTemp", "cmc_ber"])!=-1){
		//需要获取子指标
		perfTargetName = comboSubPerfTarget.getValue();
	}	
	//获取indexs
	var indexs = null;
	if(channelIndex==''){  //modified by wubo  2017.01.23
		indexs = Ext.getCmp("portSelect").getCheckedValue();
	}else{
		Ext.getCmp("portSelect").setValue(channelIndex);
		indexs = channelIndex;
	}
	//获取起始时间
	var startTime = stTime.value.format("yyyy-mm-dd HH:MM:ss");
	var endTime = etTime.value.format("yyyy-mm-dd HH:MM:ss");
	
	//进行验证
	if(!validate(perfTargetName, indexs)){return;};
	
	//在每次查询操作之前，将相应的参数保存起来，以便动态获取数据，包括标题、Y轴文字、URL、单位、指标名称
	//获取图表标题和Y轴文字
	var title = comboPerfTarget.lastSelectionText+ " @Tip.trendGraph@";
	var yText = comboPerfTarget.lastSelectionText;
	if($.inArray(perfTargetName, ["optTemp", "usTemp", "dsTemp", "insideTemp", "outsideTemp", "powerTemp",
	                              "optVoltage", "optCurrent", "optTxPower", "optRePower", "ccer", "ucer"])!=-1){
		title = comboSubPerfTarget.lastSelectionText+ " @Tip.trendGraph@";
		yText = comboSubPerfTarget.lastSelectionText;
	}
	//获取图表单位
	var unit = "";
	if($.inArray(perfTargetName, ["cmc_upLinkFlow", "cmc_macFlow","cmc_ccupFlow","cmc_cmDownFlow"])!=-1){
		unit = "Mbps";
	}else if($.inArray(perfTargetName, ["upLinkUsed","upLinkInUsed","upLinkOutUsed","macUsed", "channelUsed", "cmc_cpuUsed", "cmc_memUsed", "cmc_flashUsed","ucer","ccer","cmc_cmTxNotInRange","cmc_cmReNotInRange","cmc_upSnrNotInRange","cmc_downSnrNotInRange"])!=-1){
		unit = "%";
	}else if($.inArray(perfTargetName, ["cmc_channelSpeed"])!=-1){
		unit = "Mbps";
	}else if($.inArray(perfTargetName, ["optTxPower", "optRePower", "cmc_opticalReceiver"])!=-1){
		unit = "dBm";
	}else if($.inArray(perfTargetName, ["cmc_snr","cmc_upSnrAvg","cmc_downSnrAvg"])!=-1){
		unit = "dB";
	}else if($.inArray(perfTargetName, ["optCurrent"])!=-1){
		unit = "mA";
	}else if($.inArray(perfTargetName, ["optVoltage", "cmc_dorLinePower"])!=-1){
		unit = "V";
	}else if($.inArray(perfTargetName, ["optTemp", "usTemp", "dsTemp", "insideTemp", "outsideTemp", "powerTemp", "cmc_dorOptTemp"])!=-1){
		unit = "@{unitConfigConstant.tempUnit}@";
	}else if($.inArray(perfTargetName, ["cmc_onlineStatus"])!=-1){
		unit = "ms";
	}else if($.inArray(perfTargetName, ["cmc_cmTxAvg","cmc_cmReAvg"])!=-1){
		unit = "dBmV";
	}
	//获取图表的URL及信道描述
	var url = "";
	var channelType = "";
	if($.inArray(perfTargetName, ["cmc_cpuUsed", "cmc_memUsed", "cmc_flashUsed"])!=-1){
		url = "/cmcPerfGraph/loadBoardUsedData.tv";
	}else if($.inArray(perfTargetName, ["optTxPower", "optRePower", "optCurrent", "optVoltage", "optTemp"])!=-1){
		url = "/cmcPerfGraph/loadOptLinkData.tv";
	}else if($.inArray(perfTargetName, ["usTemp", "dsTemp", "insideTemp", "outsideTemp", "cmc_dorOptTemp"])!=-1){
		url = "/cmcPerfGraph/loadTempData.tv";
	}else if($.inArray(perfTargetName, ["cmc_macFlow", "macUsed", "cmc_channelSpeed", "channelUsed"])!=-1){
		url = "/cmcPerfGraph/loadFlowData.tv";
		channelType = "US";
		labelText = "@Performance.channel@";
	}else if($.inArray(perfTargetName, ["cmc_upLinkFlow", "upLinkUsed","upLinkInUsed","upLinkOutUsed"])!=-1){
		url = "/cmcPerfGraph/loadFlowData.tv";
		channelType = "US";
		labelText = "@Performance.uplink@";
	}else if($.inArray(perfTargetName, ["ucer", "ccer", "cmc_snr"])!=-1){
		url = "/cmcPerfGraph/loadChannelSingalQuality.tv";
		channelType = "US";
		labelText = "@Performance.channel@";
	}else if($.inArray(perfTargetName, ["powerTemp"])!=-1){
		url = "/cmcPerfGraph/loadPowerTempData.tv";
		labelText = "";
	}else if($.inArray(perfTargetName, ["cmc_opticalReceiver"]) != -1){
	    url = "/cmcPerfGraph/loadOpticalReceiverReceivedPowerData.tv"
	    labelText = "@CMC/CMC.optical.receiverPower@";
	}else if($.inArray(perfTargetName, ["cmc_onlineStatus"])!=-1){
		url = "/cmcPerfGraph/loadCmcRelayPerfData.tv";
		labelText = "@Performance.deviceRelay@";
	}else if($.inArray(perfTargetName, ["cmc_ccupFlow","cmc_cmDownFlow","cmc_cmTxAvg","cmc_cmTxNotInRange","cmc_cmReAvg","cmc_cmReNotInRange","cmc_upSnrAvg","cmc_upSnrNotInRange","cmc_downSnrAvg","cmc_downSnrNotInRange"])!=-1){
		url="/cmcPerfGraph/loadcmcNetInfo.tv";
		labelText=""
	}else if($.inArray(perfTargetName, ["cmc_onlineRatio"])!=-1){
		url="/cmcPerfGraph/loadcmcCmNum.tv";
		labelText=""
	}else if($.inArray(perfTargetName, ["cmc_dorLinePower"])!=-1){
		 url = "/cmcPerfGraph/loadCmcDorVoltage.tv"
		 labelText = "@CMC/CMC.optical.dorVoltage@";
	}
	//保存数据
	if(graphData==null){
		graphData = new graphData(title, yText, unit, perfTargetName, direction, url, indexs);
	}else{
		graphData.title = title;
		graphData.yText = yText;
		graphData.unit = unit;
		graphData.perfTargetName = perfTargetName;
		graphData.direction = direction;
		graphData.url = url;
		graphData.indexs = indexs;
	}
	//展示等待框
	var lPos = ($("#container").width() - $("#loading").outerWidth())/2;
	var tPos = ($("#container").height() - $("#loading").outerHeight())/2;
	$("#loading").show().css({top:tPos,left:lPos});
	//发送查询请求
	$.ajax({
		url: url,
    	type: 'POST',
    	data: {
    		entityId:cmcId,
    		indexs:indexs,
    		perfTarget:perfTargetName,
    		channelType: channelType,
    		direction:direction,
    		channelIndex:cmcIndex,
    		startTime:startTime,
    		endTime:endTime
    	},
    	dataType:"json",
   		success: function(json) {
   			var seriesOptions = [];
   			var i = 0;
   			for(var key in json){
				//根据key(index)获取对应的下拉列表名称   
   				if(key=="" || perfTargetName == 'cmc_onlineStatus'){
   					//表明此时只有一条线
   					seriesOptions[i++] = {
   	   	   					name: yText,
   	   	   					key : key,
   	   	   					data: json[key] || [],
   	   	   					dataGrouping: {
   	   	   						enabled: false
   	   	   					}
   	   	   			};
   				}else{
   					seriesOptions[i++] = {
   							name: labelText + key,
   							key : key,
   							data: json[key] || [],
   							dataGrouping: {
   								enabled: false
   							}
   					};
   				}
   			}
   			if(seriesOptions.length==0){
   				top.afterSaveOrDelete({
   			      title: '@COMMON.tip@',
   			      html: '<b class="orangeTxt">@Tip.queryError@</b>'
   			    });
   				return;
   			}
   			//展示数据
   			generateGraph(seriesOptions, title, yText, unit, perfTargetName, startTime, endTime);
   			$("#loading").fadeOut('slow');
   		}, error: function(result) {
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

/**
 * 校验查询条件
 */
var validate = function(perfTargetName, indexs){
	if(perfTargetName==null || perfTargetName==""){
		window.parent.showMessageDlg("@COMMON.tip@", "@Tip.pleaseSelectPerfTarget@");
		return false;
	}else if(perfTargetName != "cmc_opticalReceiver"){
		if((indexs==null || indexs=="") && ($.inArray(perfTargetName, ["cmc_opticalReceiver","cmc_channelSpeed", "channelUsed", "cmc_snr", "ccer",
	                                                                     "ucer"]))!=-1){
		var tip = $("#subCat").text();
		window.parent.showMessageDlg("@COMMON.tip@", "@Tip.select@"+labelText);
		return false;
	}
	}
	var startTime = stTime.value.format("yyyy-mm-dd HH:MM:ss");
	var endTime = etTime.value.format("yyyy-mm-dd HH:MM:ss");
	if(startTime>endTime){
		window.parent.showMessageDlg("@COMMON.tip@", "@Tip.timeLimit@");
		return false;
	}
	return true;
}

function closeLoading(){
	$("#loading").fadeOut('slow');
} 
