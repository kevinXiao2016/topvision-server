var grid = null;
var selectionModel;
var store = null;

/********************
 **** 一些逻辑方法*****
 ********************/
function operationRender(value, p, record){
	return String.format("<a href='javascript:;' onclick='editTarget(\"{0}\")' >@COMMON.edit@</a>", record.data.targetId);
}

function editTarget(targetId){
	window.top.createDialog('targetEdit', "@Performance.editManage@" , 600, 370, '/performance/perfThreshold/showTargetEdit.tv?targetId=' + targetId, null, true, true);
}

function typeRender(value, meta, record){
	if(EntityType.getOltType() == value){
		return "OLT";
	}else if(EntityType.getCCMTSAndCMTSType() == value){
		return "CMTS";
	}else if(EntityType.getOnuType() == value){
		return "ONU";
	}else{
		return "Other";
	}
}

function nameRender(value, meta, record){
	return "<span style='padding-left:20px;'>" +value + "</span>";
}

function renderSwitch(value, p, record){
	if (value) {
		return String.format('<img class="switch nm3kTip" nm3kTip="@Performance.disableTarget@" src="/images/performance/on.png" onclick="enableTarget(\'{0}\', {1})" border=0 align=absmiddle>',
				record.data.targetId, record.data.enableStatus);	
	} else {
		return String.format('<img class="switch nm3kTip" nm3kTip="@Performance.enableTarget@" src="/images/performance/off.png" onclick="enableTarget(\'{0}\', {1})" border=0 align=absmiddle>',
				record.data.targetId, record.data.enableStatus);	
	}
}

function enableTarget(targetId, status){
	$.ajax({
		url : '/performance/perfThreshold/enableTarget.tv',
		type : 'post',
		dataType: 'json',
		data : {
			targetId : targetId,
			enable : !status
		},
		success : function(json) {
			store.reload();
			if(json.success){
				top.afterSaveOrDelete({
					title: "@COMMON.tip@",
					html: '<b class="orangeTxt">@Performance.enableSuccess@</b>'	
				});
			}else{
				window.parent.showMessageDlg("@COMMON.tip@", "@Performance.enableReject@");
			}
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@Performance.updateStatusFail@");
		},
		cache : false
	}); 
}

function unitRender(value, meta, record){
	if(value){
		return value;
	}else {
		return "--";
	}
}

function doRefresh(){
	store.reload({
		callback: function(){
			notSupportCMTSLisence();
		}
	});
}

//构建模板列表
$(document).ready(function() {
	var reader = new Ext.data.JsonReader({
        fields: [
			{name: 'targetId'},
		    {name: 'targetType'},
		    {name: 'targetDisplayName'},
		    {name: 'targetGroup'},
		    {name: 'unit'},
		    {name: 'maxNum'},
		    {name: 'minNum'},
		    {name: 'enableStatus'}
        ]
	});
	
	store = new Ext.data.GroupingStore({
       	url: '/performance/perfThreshold/loadTargetList.tv',
       	reader: reader,
		remoteGroup: false,
		remoteSort: false,
		groupField: 'targetType',
		groupOnSort: false
    });
   store.setDefaultSort('targetType', 'ASC');
   
   var groupTpl = '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "@COMMON.items@" : "@COMMON.items@"]})';

   var toolbar = [
	    {text: '@COMMON.refresh@', iconCls: 'bmenu_refresh', handler: doRefresh}
   ];

   var columnModels = [
	    {header: '@Performance.targetType@', sortable:false, groupable:true, menuDisabled:true,dataIndex:'targetType',hidden:true, renderer: typeRender},
		{header: "<div style='text-align:center'>@Performance.targetName@</div>",align:'left', width:100, sortable:false, dataIndex:'targetDisplayName', renderer: nameRender},
		{header: "<div style='text-align:center'>@Tip.perfTargetGroup@</div>",align:'left',  width:80, sortable:false, dataIndex:'targetGroup'},
		{header: '@Performance.maxValue@',align:'center', sortable:false, width:60, dataIndex:'maxNum'},
		{header: '@Performance.minValue@',align:'center', sortable:false, width:60, dataIndex:'minNum'},
		{header: '@Performance.unit@',align:'center', sortable:false, width:60, dataIndex:'unit', renderer : unitRender},
		{header: '@Performance.enableStatus@',align:'center', sortable:false, width:60, dataIndex:'enableStatus', renderer : renderSwitch},
	    {header: '@COMMON.opration@', align:'center', width:150, fixed :true, sortable:false,dataIndex: 'enable',renderer:operationRender}
	];
    grid = new Ext.grid.GridPanel({
		store: store,
        animCollapse: animCollapse,
        trackMouseOver: trackMouseOver,
        border: false,
        columns: columnModels,
        bodyCssClass: 'normalTable',
        view: new Ext.grid.GroupingView({
            forceFit: true, hideGroupedColumn: true,enableNoGroups: true,groupTextTpl: groupTpl
        }),
        tbar: toolbar,
        renderTo: document.body
    });
    new Ext.Viewport({layout: 'fit', items: [grid]});
	store.load({
		callback: function(){
			notSupportCMTSLisence();
		}
	});
});

//[EMS-14510]lisence不支持CMTS板块；
function notSupportCMTSLisence(){
	if(!cmcSupport){
		store.each(function(v, i){
			if(EntityType.getCCMTSAndCMTSType() == v.data.targetType){
				store.remove(v)
			}
		})
	}
}