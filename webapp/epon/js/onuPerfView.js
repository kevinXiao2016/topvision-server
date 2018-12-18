$(DOC).ready(function(){
	//创建性能指标分组下拉框
	var groupStore = new Ext.data.JsonStore({
		url:'/onu/onuPerfGraph/loadOnuPerfTargetGroups.tv?onuId='+onuId,
		fields: ['value', 'text'],
        sortInfo:{field:'value',direction:'asc'}
	});
	comboGroup = new Ext.form.ComboBox({
		store: groupStore,
		emptyText: '@Tip.select@',
		triggerAction: 'all',
		valueField: 'value',
		displayField: 'text',
		applyTo: 'comboGroup',
		mode:'local',
		width: 160,
		editable: false,
		listeners: {
			select: function(){
				//更换性能指标下拉框的数据
				comboPerfTarget.reset();
				targetStore.load({params:{groupName:comboGroup.getValue()}});
				//隐藏子指标
				$('#optSubTd').hide();
				$('#subCat').hide();
				$('#portSelect').hide();
			}
		}
	});
	//创建性能指标下拉框
	var targetStore = new Ext.data.JsonStore({
		url:'/onu/onuPerfGraph/loadOnuTargetsByGroup.tv?onuId='+onuId,
        fields: ['value', 'text'],
        sortInfo:{field:'value',direction:'asc'}
	});
	comboPerfTarget = new Ext.form.ComboBox({
		store: targetStore,
		emptyText: '@Tip.select@',
		triggerAction: 'all',
		lazyInit  : true,
		valueField: 'value',
		displayField: 'text',
		applyTo: 'comboPerfTarget',
		width: 160,
		mode:'local',
		editable: false,
		listeners: {
			select: function(){
				//选择性能下拉框中的性能指标时，判断是否需要展示子指标及决定板卡/端口的多选框的显示
				var group = comboGroup.getValue();
				var target = comboPerfTarget.getValue();
				var url = "";
				if(group == 'onu_service'){
					//展示子指标
					$('#subCat').next().andSelf().hide();
					$('#optSubTd').show();
					//此时只用展示ONU的名称就可以
					labelText = "";
				}else if(group == 'onu_flow'){
					$('#optSubTd').hide();
					if(target == 'onuPonInSpeed' || target == 'onuPonOutSpeed'){
						labelText = '@Performance.onuPonPort@';
						portIndexStore.removeAll();
						url = '/onu/onuPerfGraph/loadOnuPonList.tv';
						portIndexStore.proxy.conn.url = url;
						portIndexStore.load();
					}else{
						labelText = '@Performance.uniPort@';
						portIndexStore.removeAll();
						url = '/onu/onuPerfGraph/loadOnuUniList.tv';
						portIndexStore.proxy.conn.url = url;
						portIndexStore.load();
					}
					$('#subCat').text(labelText+":").next().andSelf().show();
				}
			}
		}
	});
	//生成光链路信息的子指标下拉框
	window.optSubPerfTargetStore = new Ext.data.JsonStore({
		url:'/onu/onuPerfGraph/loadOnuOptSubPerfTargets.tv',
		baseParams: {onuId: onuId},
		fields: ['value', 'text'],
		sortInfo:{field:'value',direction:'asc'}
	});
	optSubPerfTargetStore.load();
	window.comboOptSubPerfTarget = new Ext.form.ComboBox({
		store: optSubPerfTargetStore,
		emptyText: '@Tip.select@',
		triggerAction: 'all',
		valueField: 'value',
		displayField: 'text',
		applyTo: 'optPerftargets',
		mode:'local',
		width: 160,
		editable: false
	});
	
	//生成端口Index下拉框
	window.portIndexStore = new Ext.data.JsonStore({
		url:'/onu/onuPerfGraph/loadOnuPonList.tv',
		baseParams: {onuId: onuId},
		fields: ['value', 'text']
		//sortInfo:{field:'value',direction:'asc'}
	});
	window.portIndexCombo = new Ext.ux.form.LovCombo({
        width: 160,
        id:'portIndexCombo',
        hideOnSelect : true,
        editable : false,
        renderTo:"portCombo",  
        store : portIndexStore,
        valueField: 'value',
		displayField: 'text',
        triggerAction : 'all', 
        mode:'local',
        emptyText : "@Tip.select@",  
        beforeBlur : function(){},
        listeners :{
        }
	})
	
	//时间控件创建
	var lastMonth = new Date(), current = new Date();
	lastMonth.setTime(lastMonth.getTime()-(7*24*60*60*1000));
	stTime = new Ext.ux.form.DateTimeField({
		width:160,
		value : lastMonth,
		editable: false,
		renderTo:"startTime",
		emptyText:'@Tip.pleaseEnterTime@',
	    blankText:'@Tip.pleaseEnterTime@'
	});
	etTime = new Ext.ux.form.DateTimeField({
		width:160,
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
	})
	
	//初始化数据
	groupStore.load({
		callback:function(){
				comboGroup.setValue('onu_service');
				comboGroup.fireEvent('select');
				targetStore.load({
					params:{groupName:comboGroup.getValue()},
					callback:function(){
						comboPerfTarget.setValue('onu_optLink');
						$('#subText').text("@Performance.optTarget@" + ":");
						$('#portTree').hide();
						comboPerfTarget.fireEvent('select');
						comboOptSubPerfTarget.setValue('onuPonTxPower');
						setTimeout(function(){
							//获取图表DIV的高度
							$("#container").height($(window).height()-$(".ultab").outerHeight(true)-$("#searchDiv").outerHeight(true)-50);
							//查询
							query();
						},100)
					}
				});
			}
	});
});
/**
 * 校验查询条件
 */
var validate = function(perfTargetName, indexs){
	if(perfTargetName==null || perfTargetName==""){
		window.parent.showMessageDlg("@COMMON.tip@", "@Tip.pleaseSelectPerfTarget@");
		return false;
	}
	var startTime = stTime.value.format("yyyy-mm-dd HH:MM:ss");
	var endTime = etTime.value.format("yyyy-mm-dd HH:MM:ss");
	if(startTime>endTime){
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
				html: '<b class="orangeTxt">@Tip.timeLimit@</b>'
   		});
		return false;
	}
	return true;
}

var query = function(){
	var indexs = "";
	//获取需要传递到后台的参数，包括设备ID、指标名称、出入方向(针对流量)、indexs、起始时间
	//获取指标名称和方向
	var perfTargetName = comboPerfTarget.getValue();
	if(perfTargetName == 'onu_optLink'){
		//需要获取子指标
		perfTargetName = comboOptSubPerfTarget.getValue();
	}
	if(perfTargetName == null || perfTargetName==""){
		window.parent.showMessageDlg("@COMMON.tip@", "@Tip.pleaseSelectPerfTarget@");
		return false;
	}
	else if($("#portCombo:visible").length > 0){
		indexs = Ext.getCmp("portIndexCombo").getCheckedValue();
		if(indexs == null || indexs==""){
			var tip = $("#subCat").text();
			window.parent.showMessageDlg("@COMMON.tip@", "@Tip.select@"+ tip);
			return false;
		}
	}
	//获取起始时间
	var startTime = stTime.value.format("yyyy-mm-dd HH:MM:ss");
	var endTime = etTime.value.format("yyyy-mm-dd HH:MM:ss");
	if(startTime>endTime){
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
				html: '<b class="orangeTxt">@Tip.timeLimit@</b>'
   		});
		return false;
	}
	
	//在每次查询操作之前，将相应的参数保存起来，以便动态获取数据，包括标题、Y轴文字、URL、单位、指标名称
	//获取图表标题和Y轴的文字
	var title = comboPerfTarget.lastSelectionText+ " @Tip.trendGraph@";
	var yText = comboPerfTarget.lastSelectionText;
	if($.inArray(perfTargetName, ["onuPonRePower", "onuPonTxPower",'oltPonRePower','onuCATVRePower'])!=-1){
		title = comboOptSubPerfTarget.lastSelectionText+ " @Tip.trendGraph@";
		yText = comboOptSubPerfTarget.lastSelectionText;
	}
	//获取图表单位
	var unit = "";
	var url = "";
	var direction = 0;
	if($.inArray(perfTargetName, ["onuPonRePower", "onuPonTxPower",'oltPonRePower','onuCATVRePower'])!=-1){
		unit = "dBm";
		url = "/onu/onuPerfGraph/loadOnuOptPerfData.tv";
	}else if($.inArray(perfTargetName, ["onuPonInSpeed", "onuPonOutSpeed",'onuUniInSpeed','onuUniOutSpeed'])!=-1){
		unit = "Mbps";
		url = '/onu/onuPerfGraph/loadOnuFlowPerfData.tv'
		//标识入方向还是出方向
		if(perfTargetName == 'onuPonInSpeed' || perfTargetName == 'onuUniInSpeed'){
			direction = 1;
		}else{ 
			direction = 2;
		}
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
    		entityId:onuId,
    		indexs:indexs,
    		perfTarget:perfTargetName,
    		direction:direction,
    		startTime:startTime,
    		endTime:endTime
    	},
    	dataType:"json",
   		success: function(json) {
   			var seriesOptions = [];
   			var i = 0;
   			var labelName = '@Perf.onuOptical@';
   			for(var key in json){
				//根据key(index)获取对应的下拉列表名称  
   				labelName = labelText + key;
				seriesOptions[i++] = {
   	   					name: labelName,
   	   					key : key,
   	   					data: json[key] || [],
	   	   				dataGrouping: {
	   						enabled: false
	   					}
   	   			};
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
		}, cache: false
	});
}

function closeLoading(){
	$("#loading").fadeOut('slow');
}

