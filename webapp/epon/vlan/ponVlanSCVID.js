var allCvids = [],
	allSvids = [],
	cvidInit = false,
	svidInit = false;

/**
 * 初始化右侧的区域，主要展示SVID/CVID
 */
function initExtraRegion(){
	//初始化tab
	extraTabPanel = new Ext.TabPanel({
    	renderTo: 'extra-container',
    	width:200,
    	height: $('#extra-container').height(),
    	frame:true,
    	items:[
			{itemId:'cvid',contentEl: 'CVID-container', title: 'CVID'},
			{itemId:'svid',contentEl: 'SVID-container', title: 'SVID'}
   	    ],
   	    listeners: {
   	    	tabchange: function(tab, item){
   	    		if(item.itemId == 'cvid' && !cvidInit){
   	    			//初始化CVID表格
   	    			initCvidGrid();
   	    			cvidInit = true;
   	    		}else if(item.itemId == 'svid' && !svidInit){
   	    			//初始化SVID表格
   	    			initSvidGrid();
   	    			svidInit = true;
   	    		}
   	    	}
   	    }
    });
	extraTabPanel.setActiveTab('cvid');
}

/**
 * 初始化CVID表格
 */
function initCvidGrid(){
	var cm =  new Ext.grid.ColumnModel([
  	    {header:'CVID', dataIndex:'cvid', align:'center', width: 60, sortable:true},
  	  	{header:'@VLAN.vlanMode@', dataIndex:'vlanMode', width: 130, align:'center', sortable: false, renderer: csvidModeRender}
  	]);
	
	cvidModeStore = new Ext.data.JsonStore({
		url: '/epon/vlan/loadPonCvidModeRela.tv',
		fields: ['cvid','vlanMode','onuMacAddress'],
        sortInfo: {field: 'cvid', direction: 'ASC'}
    })
	cvidModeStore.on('load', function(store, records){
		allCvids = [];
		$.each(records, function(i, record){
			allCvids.push(record.data);
		});
	})
	cvidModeStore.load({params: {'ponId': ponId}});
	
	cvidModeGrid = new Ext.grid.GridPanel({
    	stripeRows:true,
   		bodyCssClass: 'normalTable',
   		viewConfig:{forceFit: true},
        renderTo: 'CVID-container',
        height: $('#extra-container').height()-50,
        width: 200,
        border: false,
        autoScroll:true,
        cm: cm,
        tbar: new Ext.Toolbar({
	    	items: [
	    	    {xtype: 'tbspacer', width: 3},
	    	    {xtype: 'textfield', id: "cvidInput", width: 190}
	       	]
     	}),
        store : cvidModeStore
    })
	
	$('#cvidInput').attr('placeHolder', "@COMMON.range4094@");
   	$('#cvidInput').addClass('normalInput');
   	
   	//增加动态过滤
   	$("#cvidInput").keyup(function() {
		var text = jQuery("#cvidInput").val();
		
		//显示过滤后的数据
		cvidModeStore.filter('cvid', text, true);
	});
}

/**
 * 初始化SVID表格
 */
function initSvidGrid(){
	var cm =  new Ext.grid.ColumnModel([
  	    {header:'SVID', dataIndex:'svid', align:'center', width: 60, sortable:true},
  	  	{header:'@VLAN.vlanMode@', dataIndex:'vlanMode', width: 130, align:'center', sortable: false, renderer: csvidModeRender}
  	]);
	
	svidModeStore = new Ext.data.JsonStore({
		url: '/epon/vlan/loadPonSvidModeRela.tv',
		fields: ['svid','vlanMode','onuMacAddress'],
        sortInfo: {field: 'svid', direction: 'ASC'}
    })
	svidModeStore.on('load', function(store, records){
		allSvids = [];
		$.each(records, function(i, record){
			allSvids.push(record.data);
		});
	})
	
	svidModeGrid = new Ext.grid.GridPanel({
		bodyCssClass: 'normalTable',
    	stripeRows:true,
   		viewConfig:{forceFit: true},
        renderTo: 'SVID-container',
        height: $('#extra-container').height() - 50,
        width: 200,
        border: false,
        autoScroll: true,
        cm: cm,
        tbar: new Ext.Toolbar({
	    	items: [
    	        {xtype: 'tbspacer', width: 3},
	    	    {xtype: 'textfield', id: "svidInput", width: 190}
	       	]
     	}),
        store : svidModeStore
    })
	svidModeStore.load({params: {'ponId': ponId}});
	
	$('#svidInput').attr('placeHolder', "@COMMON.range4094@");
   	$('#svidInput').addClass('normalInput');
   	
   	//增加动态过滤
   	$("#svidInput").keyup(function() {
		var text = jQuery("#svidInput").val();
		
		//显示过滤后的数据
		svidModeStore.filter('svid', text, true);
	});
   	
}

/**
 * 刷新SVID/CVID区域
 */
function refreshSCVID(){
	cvidInit && cvidModeStore.load({params: {'ponId': ponId}});
	svidInit && svidModeStore.load({params: {'ponId': ponId}});
}

function resizeSCVID(){
	extraTabPanel.setHeight($('#extra-container').height());
	cvidInit && cvidModeGrid.setHeight($('#extra-container').height() - 50);
	svidInit && svidModeGrid.setHeight($('#extra-container').height() - 50);
}

function csvidModeRender(v){
	var str = v;
	switch(v){
	case 0:
		str = '@VLAN.notCfg@';
		break;
	case 1:
		str = '@VLAN.ponTranslate@';
		break;
	case 2:
		str = '@VLAN.portAggr@';
		break;
	case 3:
		str = '@VLAN.ponTransparent@';
		break;
	case 4:
		str = 'PON QinQ';
		break;
	case 5:
		str = '@VLAN.llidTranslate@';
		break;
	case 6:
		str = '@VLAN.llidAggr@';
		break;
	case 7:
		str = 'LLID Trunk';
		break;
	case 8:
		str = 'LLID QinQ';
		break;
	default:
		str = v;
		break;
	}
	return str;
}

/**
 * 判断SVID是否存在
 * @param vlanId
 * @return {Object} exist: 是否存在, msg: 错误消息
 */
function isSvidExist(vlanId){
	var exist = false, msg;
	for(var i=0; i<allSvids.length; i++){
		if(allSvids[i].svid == vlanId){
			exist = true;
			 if(allSvids[i].onuMacAddress){
				 msg = String.format('@VLAN.ponVCfgTypeTip@', {
         			vlan: vlanId, 
         			mac : allSvids[i].onuMacAddress, 
         			mode : csvidModeRender(allSvids[i].vlanMode)
				 });
         	}else{
         		msg = String.format('@VLAN.ponVCfgTypeTip2@', {
         			vlan: vlanId, 
         			mode: csvidModeRender(allSvids[i].vlanMode)
         		});
         	}
			break;
		}
	}
	return {
		exist: exist,
		msg: msg
	};
}

/**
 * 判断CVID是否存在
 * @param vlanId
 * @return {Object} exist: 是否存在, msg: 错误消息
 */
function isCvidExist(vlanId){
	var exist = false, msg;
	for(var i=0; i<allCvids.length; i++){
		if(allCvids[i].cvid == vlanId){
			exist = true;
			 if(allCvids[i].onuMacAddress){
				 msg = String.format('@VLAN.ponVCfgTypeTip@', {
         			vlan: vlanId, 
         			mac : allCvids[i].onuMacAddress, 
         			mode : csvidModeRender(allCvids[i].vlanMode)
				 });
         	}else{
         		msg = String.format('@VLAN.ponVCfgTypeTip2@', {
         			vlan: vlanId, 
         			mode: csvidModeRender(allCvids[i].vlanMode)
         		});
         	}
			break;
		}
	}
	return {
		exist: exist,
		msg: msg
	};
}