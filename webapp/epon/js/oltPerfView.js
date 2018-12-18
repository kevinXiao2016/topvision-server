var JQUERY_ZTREE,
    myTree = null;
//---------------------------------------------
//				对ZTree适配,加上tooltip功能
//---------------------------------------------
var JQUERY_FN_ZTREE = $.fn.zTree,
	JQUERY_FN_ZTREE_Z = JQUERY_FN_ZTREE._z,
	JQUERY_FN_ZTREE_CONST_ID = JQUERY_FN_ZTREE.consts.id;
JQUERY_FN_ZTREE_Z.view.makeDOMNodeIcon = function(html, setting, node) {
	var nameStr = JQUERY_FN_ZTREE_Z.data.getNodeName(setting, node),
	name = setting.view.nameIsHTML ? nameStr : nameStr.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
	if(node.nm3kTip){
		html.push("<span id='", node.tId, JQUERY_FN_ZTREE_CONST_ID.ICON,
				"' nm3kTip="+node.nm3kTip+" treeNode", JQUERY_FN_ZTREE_CONST_ID.ICON," class='nm3kTip ", this.makeNodeIcoClass(setting, node),
				"' style='", this.makeNodeIcoStyle(setting, node), "'></span><span id='", node.tId, JQUERY_FN_ZTREE_CONST_ID.SPAN,
				"'>",name,"</span>");
	}else{
		html.push("<span id='", node.tId, JQUERY_FN_ZTREE_CONST_ID.ICON,
				"' title='' treeNode", JQUERY_FN_ZTREE_CONST_ID.ICON," class='", this.makeNodeIcoClass(setting, node),
				"' style='", this.makeNodeIcoStyle(setting, node), "'></span><span id='", node.tId, JQUERY_FN_ZTREE_CONST_ID.SPAN,
				"'>",name,"</span>");
	}
};

$(DOC).ready(function(){
	//创建性能指标分组下拉框
	var groupStore = new Ext.data.JsonStore({
		url:'/epon/oltPerfGraph/loadOltPerfTargetGroups.tv?entityId='+entityId,
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
				$('#slotIndex').hide();
				$('#portTree').hide();
				/*$('#uniIndex').hide();*/
			}
		}
	});
	//创建性能指标下拉框
	var targetStore = new Ext.data.JsonStore({
		url:'/epon/oltPerfGraph/loadPerfTargetsByGroup.tv?entityId='+entityId,
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
				var group = comboPerfTarget.getValue();
				var url = "";
				$('#optSubTd').hide();
				$("#portTree").show();
				setting.view.showIcon = true;
				if(group != 'olt_onlineStatus'){
					firstLoad = true;
					if(group == 'ponInFlow' || group == 'ponOutFlow' ||  group == 'ponInUsed' ||  group == 'ponOutUsed'){
						labelText = "@Performance.ponPort@";
						url = "/epon/oltPerfGraph/loadPonPortList.tv?entityId=" + entityId;
						loadZTree(url, 'ponPort');
					}else if(group == 'sniInFlow' || group == 'sniOutFlow' || group == 'sniInUsed' || group == 'sniOutUsed'){
						labelText = "@Performance.sniPort@";
						url = '/epon/oltPerfGraph/loadSniPortList.tv?entityId=' + entityId;
						loadZTree(url, 'sniPort');
					}else if($.inArray(group, ["olt_cpuUsed", "olt_memUsed", "olt_flashUsed", "olt_boardTemp"])!=-1){
						labelText = "@Performance.board@";
						url = "/epon/oltPerfGraph/loadOltSlotList.tv?entityId="+entityId;
						loadZTree(url, 'board');
					}else if(group == 'olt_fanSpeed'){
						setting.view.showIcon = false;
						labelText = "@Performance.fan@";
						url = "/epon/oltPerfGraph/loadOltFanList.tv?entityId="+entityId;
						loadZTree(url, 'fan');
					}else if(group == 'olt_optLink'){
						//展示子指标
						$('#optSubTd').show();
						//展示光端口信息
						labelText = "@Performance.optPort@";
						url = '/epon/oltPerfGraph/loadFirberPortList.tv?entityId=' + entityId;
						loadZTree(url, 'fiberPort');
					}
					$("#subCat").text(labelText+":").show();
				}else{
					labelText = '@COMMON.entity@';
					$("#subCat").text(labelText+":").hide();
					$("#portTree").hide();
					$("#slotIndex").hide();
				}
			}
		}
	});
	//生成光链路信息的子指标下拉框
	window.optSubPerfTargetStore = new Ext.data.JsonStore({
		url:'/epon/oltPerfGraph/loadOltOptSubPerfTargets.tv',
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
	
	//生成板卡下拉框
	subCatStore = new Ext.data.JsonStore({
		url:'/epon/oltPerfGraph/loadOltSlotList.tv',
		baseParams: {entityId: entityId},
		fields: ['value', 'text']
		//sortInfo:{field:'value',direction:'asc'}
	});
	new Ext.ux.form.LovCombo({
        width: 160,
        id:'portSelect',
        hideOnSelect : true,
        editable : false,
        renderTo:"slotIndex",  
        store : subCatStore,
        valueField: 'value',
		displayField: 'text',
        triggerAction : 'all', 
        mode:'local',
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
			if(portType && targetPortIndex){
				comboGroup.setValue('olt_flow');
				comboGroup.fireEvent('select');
				targetStore.load({
					params:{groupName:comboGroup.getValue()},
					callback:function(){
						if(portType == "PON"){
							comboPerfTarget.setValue('ponInFlow');
						}else{
							comboPerfTarget.setValue('sniInFlow');
						}
						comboPerfTarget.fireEvent('select');
						setTimeout(function(){
							subCatStore.proxy.conn.url = subCatStore.proxy.url;
							subCatStore.load({
								callback:function(){
									//根据传入的端口Index进行选中
									var nodes = myTree.subTree.getNodesByParam("id", targetPortIndex , null);
									for (var i=0, l= nodes.length; i < l; i++) {
										myTree.subTree.checkNode(nodes[i], true, false);
									}
									var $nodes = myTree.subTree.getCheckedNodes(true);
						    	    showSelectedNodes($nodes);
									//获取图表DIV的高度
									$("#container").height($(window).height()-$("#titleBar").outerHeight(true)-$("#searchDiv").outerHeight(true)-50);
									//查询
									query();
									
								}
							});
						},100)
					}
				});
			}else{
				comboGroup.setValue('olt_flow');
				comboGroup.fireEvent('select');
				targetStore.load({
					params:{groupName:comboGroup.getValue()},
					callback:function(){
						comboPerfTarget.setValue('sniInFlow');
						labelText = '@Performance.sniPort@';
						$("#subCat").text(labelText + ":").show();
						$('#portTree').show();
						var sniUrl = '/epon/oltPerfGraph/loadSniPortList.tv?entityId=' + entityId;
						loadZTree(sniUrl, 'sniPort', true);
						//获取图表DIV的高度
						$("#container").height($(window).height()-$("#titleBar").outerHeight(true)-$("#searchDiv").outerHeight(true)-50);
						//查询
						if(!nmProject){
							query();
						}
					}
				});
			}
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
	}else if(indexs == null || indexs==""){
		if(perfTargetName != "olt_onlineStatus"){
			var tip = $("#subCat").text();
			window.parent.showMessageDlg("@COMMON.tip@", "@Tip.select@"+tip.substr(0,tip.length-1));
			return false;
		}else{
			return true;
		}
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
	if($.inArray(perfTargetName, ["ponInFlow","ponInUsed","sniInFlow","sniInUsed"])!=-1){
		perfTargetName = "olt_" + perfTargetName.replace('In','');
		direction = -1;
	}else if($.inArray(perfTargetName, ["ponOutFlow","ponOutUsed","sniOutFlow","sniOutUsed"])!=-1){
		perfTargetName = "olt_" + perfTargetName.replace('Out','');
		direction = 1;
	}else if(perfTargetName== 'olt_optLink'){
		//需要获取子指标
		perfTargetName = comboOptSubPerfTarget.getValue();
	}
	//获取indexs
	var indexs = "";
	if($("#slotIndex:visible").length > 0){
		indexs = Ext.getCmp("portSelect").getCheckedValue();
	}else if($("#portTree:visible").length > 0){
		indexs = getSelectedPortIds();
		
	}
	/*else if($("#uniIndex:visible").length > 0){
		indexs = Ext.getCmp("uniSelect").getCheckedValue();
	}*/
	//获取起始时间
	var startTime = stTime.value.format("yyyy-mm-dd HH:MM:ss");
	var endTime = etTime.value.format("yyyy-mm-dd HH:MM:ss");
	
	//在执行查询之前，进行验证
	if(!validate(perfTargetName, indexs)){return;};
	
	//在每次查询操作之前，将相应的参数保存起来，以便动态获取数据，包括标题、Y轴文字、URL、单位、指标名称
	//获取图表标题和Y轴的文字
	var title = comboPerfTarget.lastSelectionText+ " @Tip.trendGraph@";
	var yText = comboPerfTarget.lastSelectionText;
	if(perfTargetName== 'olt_optLink'){
		title = comboOptSubPerfTarget.lastSelectionText+ " @Tip.trendGraph@";
		yText = comboOptSubPerfTarget.lastSelectionText;
	}
	//获取图表单位
	var unit = "";
	if($.inArray(perfTargetName, ["olt_ponFlow", "olt_sniFlow"])!=-1){
		unit = "Mbps";
	}else if($.inArray(perfTargetName, ["olt_cpuUsed", "olt_memUsed", "olt_flashUsed","olt_ponUsed","olt_sniUsed"])!=-1){
		unit = "%";
	}else if($.inArray(perfTargetName, ["olt_boardTemp", "optTemp"])!=-1){
		unit = "@{unitConfigConstant.tempUnit}@";
	}else if($.inArray(perfTargetName, ["olt_fanSpeed"])!=-1){
		unit = "rpm";
	}else if($.inArray(perfTargetName, ["optTxPower", "optRePower"])!=-1){
		unit = "dBm";
	}else if($.inArray(perfTargetName, ["optCurrent"])!=-1){
		unit = "mA";
	}else if($.inArray(perfTargetName, ["optVoltage"])!=-1){
		unit = "V";
	}else if($.inArray(perfTargetName, ["olt_onlineStatus"])!=-1){
		unit = "ms";
	}
	//获取图表的URL
	var url = "";
	perfGroup = comboGroup.getValue();
	if(perfGroup == "olt_flow"){
		url = "/epon/oltPerfGraph/loadPortFlowPerfData.tv";
	}else if(perfGroup == "olt_service"){
		if($.inArray(perfTargetName, ["optTxPower", "optRePower", "optCurrent", "optVoltage", "optTemp"])!=-1){
			url = "/epon/oltPerfGraph/loadFirberPortPerfData.tv";
		}else{
			url = "/epon/oltPerfGraph/loadOltServicePerfData.tv";
		}
	}else if(perfGroup == "olt_deviceStatus"){
		if($.inArray(perfTargetName, ["olt_onlineStatus"])!=-1){
			url = "/epon/oltPerfGraph/loadOltRelayPerfData.tv";
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
    		entityId:entityId,
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
   			var labelName = '@Performance.deviceRelay@';
   			for(var key in json){
				//根据key(index)获取对应的下拉列表名称  
   				if(perfTargetName != 'olt_onlineStatus'){
   					labelName = labelText + key;
   				}
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

// port-tree
var setting = {
	check: {
		enable: true,
		chkboxType: {"Y":"", "N":""}
	},
	view: {
		dblClickExpand: false,
		nameIsHTML: true,
		showIcon : true
	},
	data: {
		simpleData: {
			enable: true
		}
	
	},
	callback: {
		beforeClick: beforeClick,
		onCheck: onCheck,
		beforeCheck : beforeCheck
	}
};

var zNodes = null;

function beforeClick(treeId, treeNode) {
	//最多选择四个
	if(!treeNode.checked){
		var selectNodes = myTree.subTree.getCheckedNodes(true);
		if(selectNodes.length >= 4){
			return false;
		}
	}
	myTree.subTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onCheck(e, treeId, treeNode) {
	var nodes = myTree.subTree.getCheckedNodes(true);
	showSelectedNodes(nodes);
}

function showSelectedNodes(nodes){
	v = "";
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
	}
	if (v.length > 0 ) v = v.substring(0, v.length-1);
	var portObj = $("#portTree p");
	portObj.text(v);
	$("#" + myTree.id).val(v);
}

function beforeCheck(treeId, treeNode){
	//最多选择四个
	if(!treeNode.checked){
		var selectNodes = myTree.subTree.getCheckedNodes(true);
		if(selectNodes.length >= 4){
			return false;
		}else{
			return true;
		}
	}else{
		return true;
	}
}

function getSelectedPortIds() {
    var nodes = myTree.subTree.getCheckedNodes(true),
        ids = [];
    for (var i = 0, l = nodes.length; i < l; i++) {
        ids.push(nodes[i].id);
    }
    return ids.join(",");
}

var preTarget;
function loadZTree(dataUrl, targetName, firstLoad){
	//切换不同指标的时候重新构建树
	if(myTree != null && preTarget != targetName){
		myTree.subTree.destroy();
	}
	preTarget = targetName;
    $.ajax({
       url: dataUrl,
       dataType: 'json',
       async: false,
       success: function(jsonData) {
    	   zNodes = jsonData;
       },
       error: function() {},
       cache: false
   });
    myTree = new Nm3kDropDownTree({
    	id : "portTreeSel",
    	renderTo : "portTree",
    	width : 180,
    	subWidth : 170,
    	setting : setting,
    	treeData : zNodes
    });
    myTree.init();
    //只有首次加载时才需要默认选中
    if(targetName == 'sniPort' || targetName == 'ponPort' || targetName=='fiberPort'){
    	//if(true){
    		var $nodes = myTree.subTree.getCheckedNodes(true);
    	    showSelectedNodes($nodes);
    	/*}else{
    		myTree.subTree.checkAllNodes(false);
    	}*/
    }
    
    
    
    //JQUERY_ZTREE = myTree.subTree;
   /*JQUERY_ZTREE = $.fn.zTree.init($("#portTreeSel"), setting, zNodes);
   var $nodes = JQUERY_ZTREE.getCheckedNodes(true);
   showSelectedNodes($nodes);*/
}
