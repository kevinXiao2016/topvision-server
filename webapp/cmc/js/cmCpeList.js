var contextMenu = null;
var entityMenu = null;
var grid = null;
var store = null;
var pagingToolbar = null;
var saveColumnId = 'cmcpelist';


Ext.onReady(function() {
	Ext.QuickTips.init();
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var cmArr = [
        {id:"cpeIp",header:"@cmcpe/CM.CPE.ip@",dataIndex:'cpeIp', width:100,align:'center', sortable:true},
	    {id:"cpeMac",header:"@cmcpe/CM.CPE.mac@",dataIndex:'cpeMac',width:100,align:'center', sortable:true},
	    {id:"cmcIp",header:"@cmcpe/CMTS.ip@",dataIndex:'cmcIp',width:100,align:'center', sortable:true,renderer:cmcIpRender},
	    {id:"cmcAlias",header:"@cmcpe/CMTS.cmcAlias@",dataIndex:'cmcAlias',width:100,align:'center', sortable:true},
        {id:"cmIp",header:"@cmcpe/CM.ip@",dataIndex:'cmIp',width:100,sortable:true,renderer:cmIpRender},
        {id:"cmMac",header:"@cmcpe/CM.mac@",dataIndex:'cmMac',width:100,sortable:true}
	];
	var cmConfig = CustomColumnModel.init("cmcpelist",cmArr,{}),
		cm = cmConfig.cm,
		sortInfo = cmConfig.sortInfo || {field: 'cpeIp', direction: 'ASC'};
	store = new Ext.data.JsonStore({
	    url: '/cmCpeInfo/loadCmCpeList.tv',
	    idProperty: "cpeIp",
	    root: 'data',
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ["cpeIp","cpeMac","cmcAlias","cmcIp","cmIp","cmMac"],
	    listeners:{
       		load : function(){
       			if (!store.reader.jsonData.data) {
       				window.parent.showMessageDlg('@COMMON.tip@', '@cmcpe/CM.CPE.pullIntervalClosed@');
				}
       		}
       	},
	});
	store.setDefaultSort(sortInfo.field, sortInfo.direction);
    store.load({
    	params: {start:0,limit: pageSize}
    });
    bbar = new Ext.PagingToolbar({
        id: "extPagingBar",
        pageSize: pageSize,
        store: store,
        displayInfo: true,
        items: ["-", String.format("@COMMON.displayPerPage@", pageSize), "-"]
    });
	grid = new Ext.grid.GridPanel({
   		stripeRows:true,
   		region: "center",
   		cls:"normalTable edge10",
   		bodyCssClass: 'normalTable',
   		border:true,
   		store: store,
   		enableColumnMove : true,
   		//title:"cpe列表",
   		cm : cm,
   		sm : sm,
   		//renderTo : "grid",
   		bbar:bbar,
   		loadMask: true,
   		viewConfig:{
   			forceFit: true
   		},
   		listeners: {
   	      sortchange : function(grid,sortInfo){
   			  CustomColumnModel.saveSortInfo(saveColumnId, sortInfo);
   		  },
   	      columnresize: function(){
   	    	  CustomColumnModel.saveCustom(saveColumnId, cm.columns);
   	      }
   	    },
		store:store,
	   	tbar : new Ext.Toolbar({
			items : [{
				xtype: 'tbspacer', 
				width: 5
			},{
				  text: "@COMMON.exportExcel@", 
	              iconCls: 'bmenu_exportWithSub',
	              menu: [{
	                  text : '@resources/COMMON.exportCurrentColumn@',
	                  handler : function(){
	                      top.ExcelUtil.exportGridToExcel(grid, '@network/a.title.cmCpeList@' , {
	                    	  allColumn: false
	                      });
	                  }
	              },{
	                  text : '@resources/COMMON.exportAllColumn@',
	                  handler : function(){
	                	  top.ExcelUtil.exportGridToExcel(grid, '@network/a.title.cmCpeList@' ,{
	                		  allColumn: true
	                      });
	                  }
	              }]
	          }]
   		})
   	});
    var viewPort =   new Ext.Viewport({
        layout: 'border',
        items: [{
            region: 'north',
            border: false,
            contentEl: 'query-container',
            height: 146
          },
          grid
        ]
      });
});

function simpleQuery() {
	var queryContent = $.trim($('#queryContent').val());
	// add by fanzidong,需要支持多条件
	var array = queryContent.split(/[\n\s]+/);
	// 最多支持100个查询条件，总长度不能长于2000
	if (array.length > 100) {
		return window.parent
				.showMessageDlg('@COMMON.tip@', '@CM.simpleLimit1@');
	} else if (queryContent.length > 2000) {
		return window.parent
				.showMessageDlg('@COMMON.tip@', '@CM.simpleLimit2@');
	}
	// 针对每一个查询条件，如果是完整的MAC地址或者模糊的MAC地址，尝试进行转换
	// modify by fanzidong，不应该进行转换，难免将IP之类的查询条件错误转换，而应该进行添加
	// add by fanzidong, 顺便进行字符校验
	var toBeAddArray = [];
	var passValidate = true;
	$.each(array, function(index, txt) {
		if (txt && !V.isAnotherName(txt)) {
			passValidate = false;
			return false;
		}
		if (top.MacUtil.isFuzzyMacAddress(txt)) {
			toBeAddArray.push(top.MacUtil.formatQueryMac(txt));
		}
	});

	if (!passValidate) {
		return window.parent.showMessageDlg('@COMMON.tip@', '@CM.simpleLimit3@');
	}

	array = array.concat(toBeAddArray);

	queryData = {
		//queryModel : 'simple',
		queryContent : array,
		start : 0,
		limit : pageSize
	};
	store.baseParams = queryData;
	store.load();
}


//下面的是renderer函数
function cmcIpRender(value, p, record) {
	if (value != null || value != '') {
		return value;
	}
	return '--';
}

function cmIpRender(value, p, record) {
	if (value != null || value != '') {
		return value;
	}
	return '--';
}