var grid;
//存储数据
var perfTargetList = [];
var data = [];
var store;
var ccType = EntityType.getCcmtsType();
var oltType = EntityType.getOltType();
$(document).ready(function(){
	new Ext.Toolbar({
		renderTo : "toolbar",
		items : [ {text : '@Tip.back@',cls:'mL10', iconCls : 'bmenu_back', handler : function(){ parent.closeFrame(); } }]
	});
	
	$("#perfTemplateName").text(perfThresholdTemplate.templateName);
	$("#templateType").text(perfThresholdTemplate.moduleName);
	if(perfThresholdTemplate.isDefaultTemplate && perfThresholdTemplate.templateType!=oltType && perfThresholdTemplate.templateType!=ccType){
		$("#subType").text(perfThresholdTemplate.subTypeName).parents("tr").show();
	}else{
		$("#subType").parents("tr").hide();
	}
	
	//创建grid
	store = new Ext.data.Store({
		proxy: new Ext.data.MemoryProxy(data),
		reader: new Ext.data.ArrayReader({},[
		    {name: 'perfTarget'},
		    {name: 'thresholds'},
		    {name: 'trigger'},
		    {name: 'timePeriod'}
		])
	});
	var cm = new Ext.grid.ColumnModel([
   		{id:"perfTarget",header:'@performance/Performance.targetName@',dataIndex:'perfTarget',align:'center',width:150,renderer:nameRender},
   		{id:"thresholds",header:'@Performance.threshold@',dataIndex:'thresholds',align:'center',width:160,renderer:thresholdsRender},
   		{id:"trigger",header:'@tip.trigger@',dataIndex:'trigger',align:'center',width:160,renderer:triggerRender},
   		{id:"timePeriod",header:'@performance/Performance.timeRange@',width:160,dataIndex:'timePeriod',align:'center',renderer:timePeriodRender}
   	]);
   	grid = new Ext.grid.EditorGridPanel({
   		renderTo: "perfGrid",
   		store:store,
   		border: true,
   		cls:'normalTable',
      	cm:cm,
      	viewConfig:{
      		forceFit: true
      	}
    });
	store.load();
	
	setGridSize();
	
	//赋值指标列表
    for (var i = 0, item; item = perfThresholdRules[i++];) {
    	var perf = PerfTargetObject.fromJson(item);
    	perfTargetList[perfTargetList.length] = perf;
    	data[data.length] = [perf.perfTarget, perf.thresholds, perf.trigger, perf.timePeriod];
	}
    store.reload();
    
    $(window).resize(function(){
    	setGridSize();
    });
});
