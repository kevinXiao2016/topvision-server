var stTime;
var etTime;
$(document).ready(function(){
	//创建性能指标组下拉框
	var groupStore = new Ext.data.JsonStore({
		//proxy: new Ext.data.HttpProxy({url:'/cmtsPerfGraph/loadCmtsPerfTargetGroups.tv', async: false}),
		url:'/cmtsPerfGraph/loadCmtsPerfTargetGroups.tv?entityId='+cmcId,
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
		width: 160,
		editable: false,
		listeners: {
			"select": function(){
				comboPerfTarget.reset();
				comboSubPerfTarget.reset();
				targetStore.load({params:{groupName:comboGroup.getValue(),deviceType:deviceType}});
				//隐藏子指标
				$('#subPerfTd').hide();
				$('#subCat').next().andSelf().hide();
			}
		}
	});
	//创建性能指标下拉框
	var targetStore = new Ext.data.JsonStore({
		//proxy: new Ext.data.HttpProxy({url:'/cmtsPerfGraph/loadPerfTargetsByGroup.tv', async: false}),
		url:'/cmtsPerfGraph/loadPerfTargetsByGroup.tv?entityId='+cmcId,
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
		width: 160,
		editable: false,
		listeners: {
			"select": function(){
				var target = comboPerfTarget.getValue();
				comboSubPerfTarget.reset();
				if($.inArray(target, ["cmc_ber", "cmc_channelSpeed", "cmc_upLinkFlow","channelUsed"])!=-1){
					//展示子指标
					subPerfTargetStore.load({params:{perfTarget:target}});
					if(target == 'cmc_channelSpeed' || target == 'channelUsed' || target == 'cmc_upLinkFlow'){
						$('#subText').text("@Performance.channelType@" + ":");
					}
					if(target == 'cmc_ber'){
						$('#subText').text("@Performance.berTarget@" + ":");
					}
					$('#subPerfTd').show();
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
				}else if($.inArray(target, ["cmc_upLinkFlow", "upLinkUsed"])!=-1){
					labelText = "@Performance.upLink@";
					subCatStore.removeAll();
					subCatStore.load({params:{entityId: cmcId, channelType:"UPLINK"}});
					$("#subCat").text(labelText + ":").next().andSelf().show();
				}else{
					labelText = "@Performance.channel@";
					$("#subCat").text("").next().andSelf().hide();
				}
			}
		}
	});
	//创建子指标下拉框
	window.subPerfTargetStore = new Ext.data.JsonStore({
		//proxy:new Ext.data.HttpProxy({url:'/cmtsPerfGraph/loadCmtsSubPerfTargets.tv', async: false}),
		url:'/cmtsPerfGraph/loadCmtsSubPerfTargets.tv',
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
		width: 160,
		editable: false,
		listeners:{
			'select':function(){
				//清空多选下拉框
				Ext.getCmp("portSelect").setValue("");
				var target = comboSubPerfTarget.getValue();
				//subCatStore.reload();
				if($.inArray(target, ["upChannelSpeed", "upChannelUsed"])!=-1){
					subCatStore.removeAll();
					subCatStore.load({params:{entityId: cmcId, channelType:"US"}});
				}else if($.inArray(target, ["downChannelSpeed", "downChannelUsed"])!=-1){
					subCatStore.removeAll();
					subCatStore.load({params:{entityId: cmcId, channelType:"DS"}});
				}else if($.inArray(target, ["upLinkInFlow", "upLinkOutFlow"])!=-1){
					subCatStore.removeAll();
					subCatStore.load({params:{entityId: cmcId, channelType:"UPLINK"}});
				}
			}
		}
	});
	labelText = "@Performance.channel@";
	subCatStore = new Ext.data.JsonStore({
		url:'/cmtsPerfGraph/loadCmtsChannelList.tv', 
		baseParams: {entityId: cmcId, channelType:"All"},
        fields: ['value', 'text','status'],
        sortInfo:{field:'value',direction:'asc'}
	});
	subCatStore.load();
	new Ext.ux.form.LovCombo({
        width: 160,
        id:'portSelect',
        hideOnSelect : true,
        editable : false,
        renderTo:"portIndex",  
        store : subCatStore,
        valueField: 'value',
		displayField: 'text',
        triggerAction : 'all',  
        mode:'local',
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
        emptyText : "@Tip.select@",  
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
		width:160,
		value : lastMonth,
		editable: false,
		renderTo:"startTime",
		emptyText:'@Tip.pleaseEnterTime@',
	});
	etTime = new Ext.ux.form.DateTimeField({
		width:160,
		value : current,
		editable: false,
		renderTo:"endTime",
		emptyText:'@Tip.pleaseEnterTime@',
	});
	
	//增加下面5行代码就全部完成了。
	nm3kPickData({
	    startTime : stTime,
	    endTime : etTime,
	    searchFunction : query
	})
	
	//初始化数据
	groupStore.load({
		callback:function(){
			if(channelIndex==''){
				comboGroup.setValue('cmc_deviceStatus');
			}else{
				comboGroup.setValue('cmc_signalQuality');//modified by wubo  2017.01.22
			}						
			comboGroup.fireEvent('select');
			targetStore.load({
				params:{groupName:comboGroup.getValue(),deviceType:deviceType},
				callback:function(){
					if(channelIndex==''){
						comboPerfTarget.setValue('cmc_onlineStatus');
					}else{
						comboPerfTarget.setValue('cmc_snr');//modified by wubo  2017.01.22
					}												
					comboPerfTarget.fireEvent('select');
					setTimeout(function(){						
						//subCatStore.load({params:{entityId: cmcId, channelType:"US"}});
						//Ext.getCmp("portSelect").selectAll();
						if(channelIndex==''){    //modified by wubo  2017.01.23
							var $cmp = Ext.getCmp("portSelect");
							var count = 4, cursor = 0;
							$cmp.getStore().each(function( record ){
								if(record.data.status && cursor < count ){
									record.set($cmp.checkField, true);
									cursor++;
								}
							});
							$cmp.setValue($cmp.getCheckedValue());
						}
					}, 1000);
					//获取图表DIV的高度
					$("#container").height($(window).height()-$("#titleBar").outerHeight(true)-$("#searchDiv").outerHeight(true)-50);
					if(!nmProject){
						query();
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
		perfTargetName = "cmc_" +  perfTargetName.replace('In','');
		direction = -1;
	}else if($.inArray(perfTargetName, ["upLinkOutFlow"])!=-1){
		perfTargetName =  "cmc_" + perfTargetName.replace('Out','');
		direction = 1;
	}else if($.inArray(perfTargetName, ["cmc_ber", "cmc_channelSpeed", "cmc_upLinkFlow", "cmc_channelUsed"])!=-1){
		//需要获取子指标
		perfTargetName = comboSubPerfTarget.getValue();
	}
	//获取indexs
	var indexs = Ext.getCmp("portSelect").getCheckedValue(); //wubo:获取选中的信道号

	if(indexs==""&&channelIndex != 0){
		indexs=channelIndex;
		Ext.getCmp("portSelect").setValue(channelIndex);
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
	if($.inArray(perfTargetName, ["ccer", "ucer", "channelSpeed", "upLinkFlow", "channelUsed"])!=-1){
		title = comboSubPerfTarget.lastSelectionText+ " @Tip.trendGraph@";
		yText = comboSubPerfTarget.lastSelectionText;
	}
	//获取图表单位
	var unit = "";
	if($.inArray(perfTargetName, ["cmc_upLinkFlow", "upLinkInFlow", "upLinkOutFlow"])!=-1){
		unit = "Mbps";
	}else if($.inArray(perfTargetName, ["ccer", "ucer", "channelUsed", "upLinkUsed", "cmc_memUsed", "cmc_cpuUsed"])!=-1){
		unit = "%";
	}else if($.inArray(perfTargetName, ["channelSpeed", "upChannelSpeed", "downChannelSpeed"])!=-1){
		unit = "Mbps";
	}else if($.inArray(perfTargetName, ["cmc_snr"])!=-1){
		unit = "dB";
	}else if($.inArray(perfTargetName, ["cmc_onlineStatus"])!=-1){
		unit = "ms";
	}
	//获取图表的URL及信道描述
	var url = "";
	var channelType = "";
	if($.inArray(perfTargetName, ["upLinkFlow", "channelSpeed", "upChannelSpeed", "downChannelSpeed", "upLinkInFlow", "upLinkOutFlow", "channelUsed", "upLinkUsed",
	                              "upChannelUsed", "downChannelUsed"])!=-1){
		url = "/cmtsPerfGraph/loadFlowData.tv";
		channelType = "US";
	}else if($.inArray(perfTargetName, ["ucer", "ccer", "cmc_snr"])!=-1){
		url = "/cmtsPerfGraph/loadChannelSingalQuality.tv";
		channelType = "US";
	}else if($.inArray(perfTargetName, ["cmc_onlineStatus"])!=-1){
		url = "/cmtsPerfGraph/loadCmtsRelayPerfData.tv";
		labelText = "@Performance.deviceRelay@";
	}else if($.inArray(perfTargetName, ["cmc_memUsed", "cmc_cpuUsed"])!=-1){
		url = "/cmtsPerfGraph/loadCmtsServicePerfData.tv";
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
   			
   			//展示数据
   			generateGraph(seriesOptions, title, yText, unit, perfTargetName, startTime, endTime);
   			$("#loading").fadeOut('slow');
   		}, error: function(result) {
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null ;}
	});
}

/**
 * 校验查询条件
 */
var validate = function(perfTargetName, indexs){
	if(perfTargetName==null || perfTargetName==""){
		window.parent.showMessageDlg("@COMMON.tip@", "@Tip.pleaseSelectPerfTarget@");
		return false;
	}else if((indexs==null || indexs=="") && ($.inArray(perfTargetName, ["upChannelSpeed", "downChannelSpeed", "upLinkInFlow", "upLinkOutFlow", "snr", "ccer",
	                                                                     "ucer", "upChannelUsed", "downChannelUsed", "upLinkUsed"]))!=-1){
		var tip = $("#subCat").text();
		window.parent.showMessageDlg("@COMMON.tip@", "@Tip.select@"+labelText);
		return false;
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