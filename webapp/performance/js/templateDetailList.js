var contextMenu = null;
var grid = null;
var selectionModel;
var store = null;

function isDefaultRender(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="/images/correct.png" border=0 align=absmiddle>',
				'@resources/COMMON.yes@');
	} else {
		return "";	
	}
}

$(document).ready(function(){
	//创建顶部toolbar
	new Ext.Toolbar({
		renderTo : "toolbar",
		items : [ 
            {text : '@Tip.back@',cls:'mL10', iconCls : 'bmenu_back', handler : function(){ parent.closeFrame();} },'-',
            {text : '@Performance.relaTemplate@', iconCls : 'bmenu_miniIcoLink', handler: bindTemplate} 
		]
	});
	
	store = new Ext.data.JsonStore({
	    url: '/performance/perfThreshold/loadTemplateListByType.tv',
	    baseParams: {templateType:templateType},
        root: 'data',
        totalProperty:'rowCount',
        fields:['templateId', 'templateName', 'templateType', 'typeDisplayName', 'createUser', 'isDefaultTemplate', 'relaDeviceNum', 'createTimeString', 'modifyTimeString'],
        sortInfo:{field:'templateName',direction:'asc'}
	});	
	
	var cm = [
		{header: '@Performance.templateName@',align:'center', width:200, sortable:true, dataIndex:'templateName'},
		{header: '@Performance.isDefaultTemplate@',align:'center', dataIndex:'isDefaultTemplate',renderer:isDefaultRender},
		{header: '@Performance.templateType@',align:'center', dataIndex:'typeDisplayName'},
		{header: '@tip.creator@',align:'center', dataIndex:'createUser'},
		{header: '@tip.relateNumber@',align:'center', dataIndex:'relaDeviceNum'},
	    {header: '@resources/COMMON.createTime@', width:160, align:'center', sortable:true,dataIndex: 'createTimeString'},
	    {header: '@resources/COMMON.modifyTime@', width:160, align:'center', sortable:false, dataIndex:'modifyTimeString'},
	    {header: '@tip.detail@', sortable:false, align:'center', dataIndex: 'isDefaultTemplate', renderer:operationRender_detail}
	];
	
	grid = new Ext.grid.GridPanel({
	   store: store,
       animCollapse: animCollapse,
       trackMouseOver: trackMouseOver,
       border: false,
       columns: cm,
       cls:'normalTable',
       renderTo: 'grid',
       viewConfig:{
    	   forceFit:true
       }
   });
	
	setGridSize();
	store.load();
   
   $(window).resize(function(){
   	setGridSize();
   });
});

//自适应grid的大小
function setGridSize(){
	var h = $(window).height() - 40;
	if(h<300) h = 300;
	var w = $(document.body).width();
	grid.setSize(w, h);
}

function bindTemplate(){
	//获取选中的行
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		//限制为单选
		var templateId = sm.getSelections()[0].data.templateId;
		window.parent.parent.showConfirmDlg('@COMMON.tip@','@tip.unBindTip@', function(type) {
  			if (type == 'no'){return;}
  			$.ajax({
  		       url: '/performance/perfThreshold/bindTemplateToEntity.tv',
  		       data: {entityIds:entityIds, templateId:templateId, templateType:templateType},
  		       type: 'POST',
  		       success: function() {
  		    	   //刷新父页面的grid
  		    	   parent.refreshGrid();
  		    	   parent.closeFrame();
  		        }, error: function() {
  		        	window.parent.parent.showMessageDlg('@COMMON.tip@','@Performance.noRelaTemlateFail@');
  		        }, cache: false
   			});
  		})
	}else{
		window.parent.parent.showMessageDlg('@COMMON.tip@','@Performance.noSelectTemplate@')
	}
}

function showTemplateDetail(templateId){
	$("#content").hide();
	$("#frameOuter").show();
	var m = Math.random();
	$("#frameInner").attr("src",'/performance/perfThreshold/showTemplateDetail_readonly.tv?templateId='+templateId+"&m="+m);
}