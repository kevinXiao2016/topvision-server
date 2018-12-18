<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ page import="com.topvision.ems.cmc.util.CmcConstants"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="C_8800A" value="<%=CmcConstants.CMC_TYPE_8800A %>"/>
<c:set var="C_8800B" value="<%=CmcConstants.CMC_TYPE_8800B %>"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<title>CC portal</title>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<!-- 自定义css引入 -->
<link rel="stylesheet" type="text/css" href="/css/ext-plugin.css"/>
<!-- 内置css定义 -->
<style type="text/css">
    .mydiv {
        padding: 5px 10px 5px 10px;
    }

    .mydiv div {
        float: left;
        margin: 3px 10px 3px 0px;
    }
</style>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
/*********************************************************************
 *onMonuseDown onHdMouseDown
 *Copy From oltAlert.jsp
 *modify by huangdongsheng
 *********************************************************************/
function onMonuseDown(e, t){
	if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
		e.stopEvent();
		var row = e.getTarget('.x-grid3-row');

		// mouseHandled flag check for a duplicate selection (handleMouseDown) call
		if(!this.mouseHandled && row){
			//alert(this.grid.store.getCount());
			var gridEl = this.grid.getEl();//得到表格的EL对象
			var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
			var index = row.rowIndex;
			if(this.isSelected(index)){
				this.deselectRow(index);
				var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				//判断头部的全选CheckBox框是否选中，如果是，则删除
				if(isChecked){
					hd.removeClass('x-grid3-hd-checker-on');
				}
			}else{
				this.selectRow(index, true);
				//判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
				if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
					hd.addClass('x-grid3-hd-checker-on');
				};
			}
		}
	}
	this.mouseHandled = false;
}

function onHdMouseDown(e, t){
	/**
	*大家觉得上面重写的代码应该已经实现了这个功能了，可是又发现下面这行也重写了
	*由原来的t.className修改为t.className.split(' ')[0]
	*为什么呢？这个是我在快速点击头部全选CheckBox时，
	*操作过程发现，有的时候x-grid3-hd-checker-on这个样式还没有删除或者多一个空格，结果导致下面这个判断不成立
	*去全选或者全选不能实现
	*/
	if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
	    e.stopEvent();
	    var hd = Ext.fly(t.parentNode);
	    var isChecked = hd.hasClass('x-grid3-hd-checker-on');
	    if(isChecked){
	        hd.removeClass('x-grid3-hd-checker-on');
	        this.clearSelections();
	    }else{
	        hd.addClass('x-grid3-hd-checker-on');
	        this.selectAll();
	    }
	}
}

Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});
</script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
var productType ='<s:property value="productType"/>';
//全局
var inputProgramStreamInfoGrid;
var outputProgramStreamInfoGrid;
var inputProgramStreamInfoStore;
var outputProgramStreamInfoGroupstore;
//工具
//保留digit位小数
function toDecimal(x, digit){
	var f = parseFloat(x);
	if(isNaN(f)){
		return false;
	}
	var temp = Math.pow(10, digit);
	var f = Math.round(x*temp)/temp;
	var s = f.toString();
	var rs = s.indexOf('.');
	if(rs < 0){
		rs = s.length;
		s += '.';
	}
	while(s.length <= rs + digit){
		s += '0';
	}
	return s;
}


function refreshIpqamStream(){
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.CMC.ipqam.isSureToRefreshDevice, function (type) {
		if(type=="ok"){
			window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.CMC.ipqam.doingRefreshDevice , 'ext-mb-waiting');
			$.ajax({
				url:"/cmc/ipQamChannel/refreshIpqamStreamInfoList.tv?cmcId=" + cmcId+'&productType='+productType,
				type:"post",
				success:function (response){
					if(response=="true"){
						onRefreshClick();
						window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshSuccessTip);
					}else{
						window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
					}
				},error: function(response) {
					window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
				}, cache: false
			});
		}
	 })
}
function onRefreshClick(){
	inputProgramStreamInfoStore.reload();
	outputProgramStreamInfoGroupstore.reload();
}
//render
//类型
function renderType(value, p, record){
	var typeArray = [ 'SPTS', 'MPTS', 'DataR', 'Data'];
	if(value > 3){
		return I18N.CMC.text.unknown;
	}
	return typeArray[value];
}
//发送类型
function renderSendMode(value, p, record){
	var sendModArray = [ I18N.CMC.ipqam.unicast, I18N.CMC.ipqam.multicast];
	if(value > 1){
		return I18N.CMC.text.unknown;
	}
	return sendModArray[value];
}
//SYNC
function renderSYNC(value, p, record){
	if(value == 1){
		return I18N.CMC.ipqam.finish;
	}else{
		return I18N.CMC.ipqam.analyzing;
	}
}
//ProgType
function renderProgType(value, p, record){
	switch(parseInt(value)){
		case 1:
			return 'MPEG1_VIDEO';
			break;
		case 2:
			return 'MPEG2_VIDEO';
			break;
		case 3:
			return 'MPEG1_AUDIO';
			break;
		case 4:
			return 'MPEG2_AUDIO';
			break;
		case 15:
			return 'AAC_AUDIO';
			break;
		case 6:
			return 'AAC1_AUDIO';
			break;
		case 129:
			return 'AAC2_AUDIO';
			break;
		case 27:
			return 'H264_VIDEO';
			break;
		case 16:
			return 'MPEG4_VIDEO';
			break;
		case 66:
			return 'AVS_VIDEO';
			break;
		default:
			return I18N.CMC.text.unknown;
	}
}
//Bitrate
function renderBitrate(value, p, record){	
	return toDecimal(value, 3);
}
//Active
function renderActive(value, p, record){
	var activeArray = [ I18N.CMC.ipqam.unUsed, I18N.CMC.ipqam.inUse];
	return activeArray[value];
}
//QamManager
function renderQamManager(value, p, record){
	var qamManagerArray = [ "VOD", "Broadcast", "NGOD", "S_VOD"];
	return qamManagerArray[value];
}
function renderOutputProgram (value, p, record) {
	if(record.data.ipqamType == 1 || record.data.ipqamType == 3||"-1"==value){
		return 'N/A';
	}else{
		return value;
	}
}
function renderNumOrNA(val){
	if("-1"==val){
		return "N/A";
	}else{
		return val;
	}
}
//产生输入节目流信息
function createInputProgramStreamInfoList(){
	Ext.override(Ext.grid.GridView,{  
	    
	    onRowSelect : function(row){  
	        this.addRowClass(row, "x-grid3-row-selected");  
	        var selected = 0;  
	        var len = this.grid.store.getCount();  
	        for(var i = 0; i < len; i++){  
	            var r = this.getRow(i);  
	            if(r){  
	               if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
	            }  
	        }  
	          
	        var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
	          
	        if (selected == len && !hd.hasClass('x-grid3-hd-checker-on')) {  
	             hd.addClass('x-grid3-hd-checker-on');   
	        }  
	    },  
	  
	    onRowDeselect : function(row){  
	        this.removeRowClass(row, "x-grid3-row-selected");  
	            var selected = 0;  
	            var len = this.grid.store.getCount();  
	            for(var i = 0; i < len; i++){  
	                var r = this.getRow(i);  
	                if(r){  
	                   if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
	                }  
	            }  
	            var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
	              
	            if (hd != null && selected != len && hd.hasClass('x-grid3-hd-checker-on')) {  
	                 hd.removeClass('x-grid3-hd-checker-on');   
	            }  
	    }  
	});  
	var sm = new Ext.grid.CheckboxSelectionModel();
	var inputProgramStreamInfoColumns = [
	    sm,
	    //new Ext.grid.RowNumberer({width:30}),
		{header: I18N.CMC.ipqam.streamType, width: w*3/52,  align: 'center', dataIndex: 'ipqamType',menuDisabled:true
	    	, renderer: renderType
	    },
		{header: I18N.CMC.ipqam.ipqamSendMode, width: w*3/52,  align: 'center', dataIndex: 'ipqamSendMode',menuDisabled:true
	    	, renderer: renderSendMode
	    },
		{header: I18N.CMC.ipqam.ipqamDestinationIPAddress, width: w*5/52,  align: 'center', dataIndex: 'ipqamDestinationIP',menuDisabled:true
	    },
		{header: I18N.CMC.ipqam.ipqamUDPPort, width: w*3/52,  align: 'center', dataIndex: 'ipqamUDPPort',menuDisabled:true
	    },
		{header: I18N.CMC.ipqam.ipqamSourceIP, width: w*5/52,  align: 'center', dataIndex: 'ipqamSourceIP',menuDisabled:true
	    },
		{header: I18N.CMC.ipqam.ipqamSourcePort, width: w*3/52,  align: 'center', dataIndex: 'ipqamSourcePort',menuDisabled:true
	    },
		{header: 'SYNC', width: w*3/52,  align: 'center', dataIndex: 'ipqamSYNC',menuDisabled:true
	    	, renderer: renderSYNC
	    },
		{header: 'Prog Type', width: w*5/52,  align: 'center', dataIndex: 'ipqamProgType',menuDisabled:true
	    	, renderer: renderProgType
	    },
		{header: I18N.CMC.ipqam.ipqamProgramNumberInput, width: w*3/52,  align: 'center', dataIndex: 'ipqamInputProgramNumber',menuDisabled:true
	    	, renderer:renderNumOrNA
		},
		{header: 'PMT PID', width: w*3/52,  align: 'center', dataIndex: 'ipqamInputPMTID',menuDisabled:true
			, renderer:renderNumOrNA
		},
		{header: 'PCR PID', width: w*3/52,  align: 'center', dataIndex: 'ipqamInputPCRID',menuDisabled:true
			, renderer:renderNumOrNA
		},
		{header: 'Total ES PIDs', width: w*5/52,  align: 'center', dataIndex: 'ipqamTotalESPIDs',menuDisabled:true
			, renderer:renderNumOrNA
		},
		{header: 'Input<br>Bitrate(Mbps)', width: w*5/52,  align: 'center', dataIndex: 'ipqamInputBitrate',menuDisabled:true
	    	, renderer: renderBitrate
	    }];
	
	inputProgramStreamInfoStore = new Ext.data.JsonStore({
		totalProperty:"results",
		root:"data",
		pruneModifiedRecords: true,
		id:"inputProgramStreamInfo",
		//proxy:new Ext.data.MemoryProxy(ipqamInputStreamInfoList),
		url:"/cmc/ipQamChannel/showIpqamInputStreamInfoList.tv?cmcId="+cmcId+"&productType="+productType,
		fields: ['ipqamOutputQAMChannel', 'ipqamType', 'ipqamSendMode', 'ipqamDestinationIP', 'ipqamUDPPort',
		         'ipqamSourceIP', 'ipqamSourcePort', 'ipqamSYNC', 'ipqamProgType','ipqamInputProgramNumber', 
		         'ipqamInputPMTID', 'ipqamInputPCRID', 'ipqamTotalESPIDs', 'ipqamInputBitrate', 'ipqamOutputProgramNumber',
		         'ipqamOutputPMTID', 'ipqamOutputPCRID', 'ipqamOutputBitrate', 'ipqamActive']
	});
	
	var inputProgramStreamInfoCm = new Ext.grid.ColumnModel(inputProgramStreamInfoColumns);
	
	var inputProgramStreamInfoToolbar = [
										"<span style='font-weight:bold'>&nbsp;</span>"
										//,
	                                     //{xtype: 'tbspacer', width: 20},
	                                     //"->",
	                             		//{text: ''//, iconCls:'tbar_refresh', id: 'refreshTbar'//, handler: refresh} //刷新
	                             		];
	inputProgramStreamInfoGrid = new Ext.grid.GridPanel({
		id: 'inputGridContainer', 
		width: w-1,
		height: h/2,
		//animCollapse: animCollapse, 
		//trackMouseOver: trackMouseOver, 
		border: true, 
		store: inputProgramStreamInfoStore,
		//clicksToEdit: 1,
		cm: inputProgramStreamInfoCm,
		sm: sm,
		cls:'normalTable',
		//tbar: inputProgramStreamInfoToolbar,
		autofill:true,
		//输入节目流信息
		title: I18N.CMC.ipqam.inProgramInfo,
		//renderTo: 'inputProgramStreamInfo-div'
		region:'center'
	});
	inputProgramStreamInfoStore.on('load', function(){
		setTimeout(function(){
			//得到表格的EL对象
			var gridEl = inputProgramStreamInfoGrid.getEl();
			//得到表格头部的全选CheckBox框
	    	var hd = gridEl.select('div.x-grid3-hd-checker');
	    	hd.addClass('x-grid3-hd-checker-on');
			inputProgramStreamInfoGrid.getSelectionModel().selectAll();	
		},100);
	});
	inputProgramStreamInfoStore.load();
	
	sm.on('rowselect', function(sm, rowIndex, r) {
		
		inputProgramStreamInfoselect(sm,rowIndex,r);
	});
	sm.on('rowdeselect', function(sm, rowIndex, r) {
		inputProgramStreamInfoselect(sm,rowIndex,r);
	});
}
function inputProgramStreamInfoselect(sm,rowIndex,r){
	var ipqamUDPPorts = [];
	var destIpAddr = [];
	var selections = sm.getSelections();
	//alert(rowIndex);
	outputProgramStreamInfoGroupstore.filterBy(function(record, id){
		var result = false;
		for (var i = 0; i < selections.length; i++) {
			if(record.get("ipqamUDPPort")==selections[i].get("ipqamUDPPort")&&record.get("ipqamDestinationIP")==selections[i].get("ipqamDestinationIP")){
				result = true;
			}
		}
		return result;
	});
}
//产生输出节目流信息
function createOutputProgramStreamInfoList(){
	
	var outputProgramStreamInfoColumns = [
	    //new Ext.grid.RowNumberer({width:30}),
		{header: I18N.CMC.ipqam.streamType, width: w/12,  align: 'center', dataIndex: 'ipqamType',menuDisabled:true
	    		, renderer: renderType
	    	},
		{header: I18N.CHANNEL.id, width: w/12,  align: 'center', dataIndex: 'ipqamOutputQAMChannel', menuDisabled:true, hidden: true
	    	},
		{header: 'QAM Manager', width: w/12,  align: 'center', dataIndex: 'ipqamQAMManager',menuDisabled:true
	    		, renderer: renderQamManager
	    	},
		{header: I18N.CMC.ipqam.ipqamDestinationIPAddress, width: w/12,  align: 'center', dataIndex: 'ipqamDestinationIP',menuDisabled:true
	    	},
		{header: I18N.CMC.ipqam.ipqamUDPPort, width: w/12,  align: 'center', dataIndex: 'ipqamUDPPort',menuDisabled:true
	    	},
		{header: 'SYNC', width: w/12,  align: 'center', dataIndex: 'ipqamSYNC',menuDisabled:true
	    		, renderer: renderSYNC
	    	},
		{header: I18N.CMC.ipqam.ipqamProgramNumberOutput, width: w/12,  align: 'center', dataIndex: 'ipqamOutputProgramNumber',menuDisabled:true
	    		, renderer: renderOutputProgram
	    	},
		{header: 'PMT PID', width: w/12,  align: 'center', dataIndex: 'ipqamOutputPMTID',menuDisabled:true
	    		, renderer: renderOutputProgram
	    	},
		{header: 'PCR PID', width: w/12,  align: 'center', dataIndex: 'ipqamOutputPCRID',menuDisabled:true
	    		, renderer: renderOutputProgram
	    	},
		{header: 'Output<br>Bitrate(Mbps)', width: w*2/12,  align: 'center', dataIndex: 'ipqamOutputBitrate',menuDisabled:true
	    		, renderer: renderBitrate
	    	},
		{header: 'Active', width: w/12,  align: 'center', dataIndex: 'ipqamActive',menuDisabled:true
	    		, renderer: renderActive
	    	}];
	
	// 分组显示
 	var reader=new Ext.data.JsonReader({
		root:'data',
		fields: ['ipqamStreamMapId', 'ipqamType', 'ipqamOutputQAMChannel', 'ipqamDestinationIP', 'ipqamUDPPort',
		         'ipqamQAMManager', 'ipqamSYNC', 'ipqamOutputProgramNumber',
		         'ipqamOutputPMTID', 'ipqamOutputPCRID', 'ipqamOutputBitrate', 'ipqamActive']
	});
 	outputProgramStreamInfoGroupstore = new Ext.data.GroupingStore({   
		id:'GroupStore',   
		reader: reader,
		remoteSort:true,
		sortInfo:{field: 'ipqamOutputQAMChannel', direction: 'ASC'},
		groupField:'ipqamOutputQAMChannel',
		//proxy:new Ext.data.MemoryProxy(ipqamOutputStreamInfoList)
		url:"/cmc/ipQamChannel/showIpqamOutputStreamInfoList.tv?cmcId="+cmcId+"&productType="+productType
	});
	
	var outputProgramStreamInfoCm = new Ext.grid.ColumnModel(outputProgramStreamInfoColumns);
	
	outputProgramStreamInfoGrid = new Ext.grid.GridPanel({
		id: 'outputGridContainer', 
		width: w-1,
		height: h/2,
		border: true, 
		store: outputProgramStreamInfoGroupstore,
		cm: outputProgramStreamInfoCm,
		cls:'normalTable',
		autofill:true,
		title: I18N.CMC.ipqam.outProgramInfo,
		region:'south',
		view: new Ext.grid.GroupingView()
	});
	outputProgramStreamInfoGroupstore.load();
}
//页面初始化
Ext.onReady(function(){
	w = $(document).width();
    h = $(document).height();
    w = w?w:1080;
    h = h?h:500;
	
	//显示数据
	createInputProgramStreamInfoList();
	createOutputProgramStreamInfoList();
	
	new Ext.Viewport({
		layout: 'border',
	    items: [{
	        region: 'north',
	        height: 80,
	        border: false,
	        contentEl: 'topPart'
	    },
	    inputProgramStreamInfoGrid,
	    outputProgramStreamInfoGrid
	   ]
	});
});

</script>
</head>
<body class="newBody">
	<div id="topPart">
		<div id=header>
			<%@ include file="entity.inc"%>
		</div>
		<div class="edge10">
			<ul class="leftFloatUl">
				<li>
					<a id="refreshBase" href="javascript:;" class="normalBtnBig"  onclick="refreshIpqamStream()">
						<span><i class="miniIcoEquipment"></i><fmt:message bundle='${cmc}' key='text.refreshData'/></span>
					</a>
				</li>
			</ul>
		</div>
	</div>
</body>
</html>