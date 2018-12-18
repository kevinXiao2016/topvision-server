<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library ext
    library zeta
    module CMC
    import cmc/js/cmIndexPartition
    import js/customColumnModel
    import cm.util.cmUtil
    import cmc/contactedCmList
    import js.json2
</Zeta:Loader>
<script src="../js/jquery/nm3kToolTip.js"></script>
<script src="../js/placeHolderHack.js"></script>
<script type="text/javascript" src="/js/ext/ux/RowExpander.js"></script>
<link rel="stylesheet" href="/js/ext/ux/RowExpander.css" />
<style type="text/css">
body,html{height:100%;overflow:hidden;}
#queryContainer{margin: 10px 10px 0;}
#tip-div{ position:absolute; top:154px; right:4px;}
.thetips dd{ float:left;}
.thetips dl{border:1px solid #ccc; float:left; background:#E1E1E1; }
#color-tips {left:5px;top:67px;}
#operation-tips{left:311px;top:67px;}
#suc-num-dd{color: #26B064;}
#fail-num-dd{color: #C07877;}
.yellow-div{height:16px;width:16px;background-color: #DCD345;}
.green-div{height:16px;width:16px;background-color: #ffffff;}
.red-div{height:16px;width:16px;background-color: #C07877;}
.normalTable .yellow-row{background-color:#DCD345 !important;border-color: #DCD345;}
.normalTable .red-row{background-color:#C07877 !important;}
.normalTable .green-row{background-color:#26B064 !important;}
.normalTable .white-row{background-color:#FFFFFF !important;border-color: #FFFFFF #DFDFDF #DFDFDF;}
.tip-dl{background: none repeat scroll 0 0 #F7F7F7;border: 1px solid #C2C2C2;color: #333333;padding: 2px 10px;position: absolute;z-index:2;}
.tip-dl dd {float: left;line-height: 1.5em;}
#loading {
	padding: 5px 8px 5px 26px;
	border: 1px solid #069;
	background: #F1E087 url('../images/refreshing2.gif') no-repeat 2px center;
	position: absolute;
	z-index: 999;
	top: 0px;
	left: 0px;
	display: none;
	font-weight: bold;
}
.content{
	color: #e67e22;
	margin: 0 3px;
}
.mainChl{
	background-color: yellow;
}
.subDashedLine{ border-bottom:1px dashed #ccc; padding-bottom:8px; padding-top:8px;}
</style>
<script type="text/javascript">
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var saveColumnId = 'ccmts_cmlist';//保存表头数据的id;
var upChannels = ${upChannels};
var downChannels = ${downChannels};
var cpeSwitch =  ${cpeSwitch};
var lastRow = null;
var pageSize = <%= uc.getPageSize() %>;
var cmPingMode = ${cmPingMode};
var cmcDeviceStyle = ${cmcAttribute.cmcDeviceStyle};
var cmcName = '${cmcAttribute.nmName}';
var cmcId = <s:property value="cmcId"/>;
vcEntityKey = 'cmcId';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var upperEntityType = EntityType.getCcmtsType();
var sysElecUnit = '@{unitConfigConstant.elecLevelUnit}@';
var chlDisplayMode = 'all';
var queryData = {
	upperEntityType: upperEntityType,
  	cmcId: cmcId,
  	upChannelId: 0,
  	downChannelId: 0,
  	statusValue: 0,
  	start: 0,
    limit: pageSize,
    upSnrMin: '',
    upSnrMax: '',
    downSnrMin: '',
    downSnrMax: '',
    upPowerMin: '',
    upPowerMax: '',
    downPowerMin: '',
    downPowerMax: ''
};
var dwrResponseTimer = null,
	responseTimer = null;
	
function openPartitionSelect(){
	var partitionData = {
		cmcId: cmcId,
		upSnrMin: queryData.upSnrMin,
		upSnrMax: queryData.upSnrMax,
		downSnrMin: queryData.downSnrMin,
		downSnrMax: queryData.downSnrMax,
		upPowerMin: queryData.upPowerMin,
		upPowerMax: queryData.upPowerMax,
		downPowerMin: queryData.downPowerMin,
		downPowerMax: queryData.downPowerMax
	};
	window.top.createDialog("modalDlg",'@CM.setPartition@',800, 370, '/cmlist/showPartitionSelect.tv?partitionDataStr='+encodeURI(JSON.stringify(partitionData)), null, true,true);
}

function partitionChanged(partitionData){
	queryData = $.extend(queryData, partitionData);
	$('#partition').cmIndexPartition.change(queryData);
	store.load({
        params: queryData
    });
}
	
$(function(){
	
	initCpeTemplate();
	//初始化上行信道选择框和下行信道选择框
	for(var i=0; i<upChannels.length; i++){
		$('#upChannel').append(String.format('<option value="{0}">{0}</option>', upChannels[i]));
	}
	for(var i=0; i<downChannels.length; i++){
		$('#downChannel').append(String.format('<option value="{0}">{0}</option>', downChannels[i]));
	}	//显示右上角帮助信息;
	top.showHelpIco('cmlistNew.jsp');
	
	var default_columns = [
	   	{header: "IP", width:120, sortable:true, align: 'center', dataIndex: 'displayIp', renderer: renderIp, exportRenderer: renderIpExport},  
	   	{header: "MAC", width:150, sortable:true, align: 'center', dataIndex: 'statusMacAddress', renderer: renderMac},
	   	{header: "@CMC.title.status@", width:60, sortable:false, align : 'center', dataIndex:'statusValue', renderer: CmUtil.statusValueRender, exportRenderer: statusValueRenderExport},
	   	{header: "<div class='txtCenter'>@CM.ServiceType@</div>", width: 150, sortable: false, align: 'left', dataIndex: 'cmServiceType'},
	    {header: "@CM.docsicVersion@", width: 100, sortable: false, align: 'center', dataIndex: 'docsisMode', renderer: renderDocsisMode, exportRenderer: renderDocsisMode}, 
	   	{header: "<div class='txtCenter'>@CMCPE.upDevice@</div>", width: 90, sortable: false, align: 'left', dataIndex: 'name'}, 
	   	{header: "@cmcUserNum.lastCollectTime@", width: 127, sortable: true, align: 'center', dataIndex: 'lastRefreshTime'}, 
	   	{header: "<div class='txtCenter'>@CM.partialUpChl@</div>", width: 62, sortable: false, align: 'left', dataIndex: 'partialUpChannels'},
	    {header: "<div class='txtCenter'>@CM.partialDownChl@</div>", width: 62, sortable: false, align: 'left', dataIndex: 'partialDownChannels'},
	   	{header: "<div class='txtCenter'>@upchannel@</div>", width:120, sortable: true, align: 'left', dataIndex: 'statusUpChannelIfIndex', renderer: renderUpChannel, exportRenderer: renderUpChannelExport},
	   	{header: "<div class='txtCenter'>@CCMTS.downStreamChannel@</div>", width:120, sortable: true, align : 'left', dataIndex: 'statusDownChannelIfIndex', renderer: renderDownChannel, exportRenderer: renderDownChannelExport},
	   	{header: "<div class='txtCenter'>@CM.upSnr@(dB)</div>", width:120, sortable: false, align: 'left', dataIndex: 'statusSignalNoise', renderer: renderUpChannelSnr, exportRenderer: renderUpChannelSnrExport},
	   	{header: "<div class='txtCenter'>@CM.downSnr@(dB)</div>", width:120, sortable: false, align: 'left', dataIndex: 'downChannelSnr', renderer: renderDownChannelSnr, exportRenderer: renderDownChannelSnrExport},
	   	{header: "<div class='txtCenter'>@CM.upSendPower@(@{unitConfigConstant.elecLevelUnit}@)</div>", width:120, sortable: false, align: 'left', dataIndex: 'upChannelTx', renderer: renderUpChannelTx, exportRenderer: renderUpChannelTxExport},
	   	{header: "<div class='txtCenter'>@CM.downReceivePower@(@{unitConfigConstant.elecLevelUnit}@)</div>", width:120, sortable: false, align: 'left', dataIndex: 'downChannelTx', renderer: renderDownChannelTx, exportRenderer: renderDownChannelTxExport},
	    {header: "@CM.previousState@", width: 100, sortable: false, align: 'center', dataIndex: 'preStatus',renderer: renderPreviousState, exportRenderer: renderPreviousState}, 
	   	{header: 'FLAP Ins', sortable: true, align: 'center', dataIndex: 'topCmFlapInsertionFailNum', renderer: renderFlapChart},
	   	{header: '@CMCPE.CPENUM@', sortable: false, align: 'center', dataIndex: 'cpeNum',renderer: renderCpeNum}, 
	   	{header: '@CMC.title.Alias@', sortable: false, align: 'center', dataIndex: 'cmAlias'},
	   	{header: '@CMC.title.usage@', sortable: false, align: 'center', dataIndex: 'cmClassified'},
	   	{header: '@CM.userNo@', width: 100, sortable: false, hidden: true, align: 'center', dataIndex: 'userId'}, 
	    {header: '@CM.userName@', width: 100, sortable: false, hidden: true, align: 'center', dataIndex: 'userName'}, 
	    {header: '@CM.userAddr@', width: 100, sortable: false, hidden: true, align: 'center', dataIndex: 'userAddr'}, 
	    {header: '@CM.userPhone@', width: 100, sortable: false, hidden: true, align: 'center', dataIndex: 'userPhoneNo'}, 
	    {header: '@CM.packageType@', width: 100, sortable: false, hidden: true, align: 'center', dataIndex: 'offerName'}, 
	    {header: '@CM.effectTime@', width: 140, sortable: false, hidden: true, align: 'center', dataIndex: 'effDate'}, 
	    {header: '@CM.expirationTime@', width: 140, sortable: false, hidden: true, align: 'center', dataIndex: 'expDate'}, 
	    {header: '@CM.configFile@', width: 100, sortable: false, hidden: true, align: 'center', dataIndex: 'configFile'},
	    {header: '@CM.extension@', width: 100, sortable: false, hidden: true, align: 'center', dataIndex: 'extension'},
	    {header: "<div class='txtCenter'>@VLAN.vlanOpera@</div>", width: 165, fixed: true, exportable: false, sortable: false, align: 'left', dataIndex: 'cmClassified',renderer: renderOperation}
   ];
	
	//构建其他菜单
	  var menuItem = [
        {text: '@contactCmList.clearSingleCm@', id: 'clear', handler: clearThisCM},           
	    {text: 'Ping CM', id: 'pingCm', handler: pingCm}, 
	    {text: 'Trace Route', id: 'traceRoute', handler: traceRoute}, 
	    {text: 'MIB Browser', handler: onMibbleBrowserClick}, 
	    {text: '@cmList.cmhistory@', handler: onCmHistoryClick}, 
	    {text: '@cmList.cmAction@', id: 'cmAction', handler: showCmAction}, 
	    {text: '@text.modifyCmImportInfo@', id: 'cmImport', handler: editCmImportInfo}, 
	    {id: "cpeInfo", text: '@cm.cpeInfo@', menu: [
	      {text: '@cm.viewCmCpe@', id: 'showCmCpeInfo', handler: showCmCpeInfo}, 
	      {text: '@cmList.cpeAction@', id: 'cpeAction', handler: showCpeAction}
	    ]}, 
	    {text: '@CM.upgrade@', id:'cmUpgrade', handler: showCmUpgrade , disabled: !operationDevicePower},
	    {text: '@cm.viewCmStaticIp@', id: 'showCmStaticIp', handler: showCmStaticIpInfo} 
	  ];
	  entityMenu = new Ext.menu.Menu({
	    id: 'cmMenu',
	    enableScrolling: false,
	    minWidth: 160,
	    items: menuItem
	  });
	
	Ext.QuickTips.init();
	$('#partition').cmIndexPartition().bind('click', openPartitionSelect);
	//加载CM表格
	var cmNumDivStr = '<div id="cm-num-div">'
	      .concat('<label>@CHANNEL.totalCmNum@</label><span style="margin-right:10px;" id="totalNum">0</span>')
	      .concat('<label>@CM.regestNum@</label><span id="onlineNum" class="pR10">0</span>')
	      .concat('</div>');
    tbar = new Ext.Toolbar({
	    items: [
	      {text: '@CM.restart@', id: 'restartCm', disabled: !operationDevicePower, disabled:true, iconCls: 'bmenu_restart', handler: restartCm},
	      {text: '@contactCmList.clearSingleCm@', disabled: !operationDevicePower, disabled:true, id: 'clearSingleCm', iconCls: 'bmenu_delete', handler: clearThisCM}
	      ,  
	      {text: '@CMC.label.resetAllCm@', id: 'restartAllCm', disabled: !operationDevicePower, iconCls: 'bmenu_restart', handler: restartAllCm}, 
	      {text: I18N.CCMTS.clearOfflineCM.menu, disabled: !operationDevicePower, id: 'clearOfflineCm', iconCls: 'bmenu_delete', handler: clearOfflineCm}, 
	      {text: '@contactCmList.refreshCmSq@', disabled: !refreshDevicePower, disabled:true, id: 'refreshCmSignal_button', iconCls: 'bmenu_refresh', handler: refreshCmSignal},
	      '-', 
	      {xtype: 'radiogroup', id: 'switchChannelMode', width: 190, items: [{
	    	  boxLabel: '@CM.allChannel@', name: 'channelViewMode', inputValue: 'all', checked: true
	      }, {
	    	  boxLabel: '@CM.mainChannel@', name: 'channelViewMode', inputValue: 'main'
	      }], listeners: {
			  'change': {
				  fn: switchChannelViewMode,
				  scope: this
			  }
		  }}, {
			  text: "@COMMON.exportExcel@", 
              iconCls: 'bmenu_exportWithSub',
              menu: [{
                  text : '@resources/COMMON.exportCurrentColumn@',
                  handler : function(){
                      top.ExcelUtil.exportGridToExcel(grid, '@network/a.title.cmList@-' + cmcName, {
                    	  allColumn: false
                      });
                  }
              },{
                  text : '@resources/COMMON.exportAllColumn@',
                  handler : function(){
                	  top.ExcelUtil.exportGridToExcel(grid, '@network/a.title.cmList@-' + cmcName, {
                          allColumn: true
                      });
                  }
              }]
          }, 
	      '->', 
	      {xtype: 'tbtext', text: cmNumDivStr}
	    ]
  	});
    Ext.override(Ext.grid.GridView,{  
	    
	    onRowSelect : function(row){  
	        this.addRowClass(row, "x-grid3-row-selected"); 
	        this.removeRowClass(row, "yellow-row");
	        this.removeRowClass(row, "red-row");
	        this.removeRowClass(row, "white-row");
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
	        this.removeRowClass(row, "yellow-row");
	        this.removeRowClass(row, "red-row");
	        this.removeRowClass(row, "white-row");
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
    var sm = new Ext.grid.CheckboxSelectionModel({
    	listeners : {
    		rowselect : function(sm,rowIndex,record){
    			disabledToolbarBtn(grid.getSelectionModel().getSelections().length)
    		},
    		rowdeselect : function(sm,rowIndex,record){
    			disabledToolbarBtn(grid.getSelectionModel().getSelections().length)
    		}
    	}
    });

    expander = new Ext.ux.grid.RowExpander({
    	id : 'expander' ,
    	dataIndex : 'cmId',
    	enableCaching : false,
      tpl : ''
    });
    
    var cmConfig = CustomColumnModel.init(saveColumnId, default_columns, cpeSwitch ? {sm: sm, expander: expander} : {sm: sm}),
	          cm = cmConfig.cm,
	    sortInfo = cmConfig.sortInfo;
	    
	store = new Ext.data.JsonStore({
      	url: '/cmlist/getCmByCmcId.tv',
    	root: 'data',
    	idProperty: "cmId",
    	totalProperty: 'rowCount',
    	remoteSort: true,
	    /* baseParams: {
	      cmcId: cmcId,
	      limit: pageSize,
	      upperEntityType: upperEntityType,
	      sort: sortInfo.field,
	      dir: sortInfo.direction
	    }, */
    	fields: [
        	'cmcId', 'cmId', 'docsIfCmtsCmStatusInetAddressTypeString', 'cmAlias', 'cmClassified','topCmFlapInsertionFailNum','downChannelId', 'displayIp', 'displayStatus','supportWebProxy',
          'name','cmcDeviceStyle', 'statusIndex', 'statusInetAddress', 'statusMacAddress', 'statusDownChannelIfIndex', 'statusUpChannelIfIndex','docsIf3CmtsCmUsStatusList','docsIfSigQSignalNoiseForUnit',
            'statusIpAddress','docsIfCmtsCmStatusRxPowerString','docsIfCmtsCmStatusSignalNoiseString', 'downChannelSnr','downChannelTx','upChannelTx','statusSignalNoise',
            'docsIfCmtsCmStatusValueString','statusValue', 'upChannelId','upChannelSnr','lastRefreshTime','downChannelRecvPower','upChannelTransPower', 'cpeNum', 'upChannelCm3Signal', 'downChannelCm3Signal',
            'userId', 'userName', 'userAddr', 'userPhoneNo', 'offerName', 'effDate', 'expDate', 'configFile', 'extension', 'supportStaticIp', 'supportCpeInfo', 'supportReset','supportCmUpgrade', 'supportClearSingleCm',
            'collectTime','preStatus','docsisMode', 'partialUpChannels', 'partialDownChannels','cmServiceType']
    });
	/* if(cmConfig.sortInfo){
	  	store.setDefaultSort(cmConfig.sortInfo.field, cmConfig.sortInfo.direction);
	} */
   
    store.on('beforeload', function() {
    	$('#suc-num-dd').text(0);
    	$('#fail-num-dd').text(0);
        store.baseParams = queryData;
    });
    //store在加载完数据后更新CM数目信息
    store.on('load', function() {
        //刷新页面统计信息
        loadCmNumUnderCcmts();
        if(lastRow !== null) {
        	expander.expandRow(lastRow);
        } else {
        	// 不希望显示
        }
    });
    //store.setDefaultSort(sortInfo.field, sortInfo.direction);
    bbar = new Ext.PagingToolbar({
        id: 'extPagingBar',
        pageSize: pageSize,
        store: store,
        displayInfo: true,
        items: ["-", String.format("@ccm/CCMTS.CmList.perPageShow@", pageSize), '-']
    });
    grid = new Ext.grid.GridPanel({
          cls:'normalTable', 
          region: 'center',
          animCollapse: animCollapse, 
          trackMouseOver: trackMouseOver, 
      	  plugins: expander,
          border: true, 
          totalProperty: 'rowCount',
          id: 'cmGrid',
          enableColumnMove: true,
          store: store, 
          cm: cm,
          sm: sm,
          enableColumnMove : true,
          loadMask: true,
          listeners : {
          	columnresize: function(){
     	    	    CustomColumnModel.saveCustom(saveColumnId, cm.columns);
     	        },
     	    sortchange : function(grid,sortInfo){
  				CustomColumnModel.saveSortInfo(saveColumnId, sortInfo);
  			}
          },
          tbar: tbar,
          bbar: bbar
         });
	  
	  new Ext.Viewport({
	    layout: 'border',
	      items: [{
	          region: 'north',
	          cls:'clear-x-panel-body',
	          border: false,
	          contentEl: 'header'
	      },
	          grid
	      ]
	  });
	  
	//代理3.0CM信号质量tooltip显示事件
	  $('body').delegate('div.docsis3', 'mouseenter', function(e){
		  var $target = $(e.currentTarget),
		  	cmId = $target.data('cmid'),
		  	type = $target.data('type');

		  //找到对应的数据
		  var record = store.getById(cmId);
		  if(record){
			  var cm = record.data;
			  var tipTitle = cm.displayIp + '(' + cm.statusMacAddress + ') ';
			  var datas = [];
			  switch(type){
			  case 'upChannelSnr':
				  tipTitle += '@CM.upSnr@';
				  $.each(cm.upChannelCm3Signal, function(i, cm3Signal){
					  datas.push({
						  id: cm3Signal.channelId,
						  value: cm3Signal.upChannelSnr + 'dB'
					  });
				  });
				  break;
			  case 'downChannelSnr':
				  tipTitle += '@CM.downSnr@';
				  $.each(cm.downChannelCm3Signal, function(i, cm3Signal){
					  datas.push({
						  id: cm3Signal.channelId,
						  value: cm3Signal.downChannelSnr + 'dB'
					  });
				  });
				  break;
			  case 'upChannelTx':
				  tipTitle += '@CM.upSendPower@';
				  $.each(cm.upChannelCm3Signal, function(i, cm3Signal){
					  datas.push({
						  id: cm3Signal.channelId,
						  value: cm3Signal.upChannelTx + '@{unitConfigConstant.elecLevelUnit}@'
					  });
				  });
				  break;
			  case 'downChannelTx':
				  tipTitle += '@CM.downReceivePower@';
				  $.each(cm.downChannelCm3Signal, function(i, cm3Signal){
					  datas.push({
						  id: cm3Signal.channelId,
						  value: cm3Signal.downChannelTx + '@{unitConfigConstant.elecLevelUnit}@'
					  });
				  });
				  break;
			  }
			  
			  //显示tooltip
			  var $cm3Tip = $('#cm3Tip');
			  $cm3Tip.empty();
				
			  //创建内容
			  var cm3Tpl = new Ext.XTemplate(
				  '<div class="bubbleTip" id="cm3Tip">',
				  	'<div class="bubbleBody">',
				  		'<p class="pT5"><b class="gray555">{tipTitle}</b></p>',
				  		'<tpl for="datas">',
				  			'<tpl if="xindex%4 == 1">',
				  			'<div class="subDashedLine wordBreak">',
				  			'</tpl>',
				  			'<span style="display:inline-block;width:80px;color:#0267B7;">{id}({value})</span>',
				  			'<tpl if="xindex%4 == 0">',
				  			'</div>',
				  			'</tpl>',
				  		'</tpl>',
					'</div>',
					'<div class="bubbleTipArr"></div>',
				  '</div>'
			  );
			
			  cm3Tpl.overwrite('cm3Tip', {
				  tipTitle: tipTitle,
				  datas: datas
			  });
			
			  $cm3Tip.css({
				  left: $target.offset().left - 376, 
				  top: $target.offset().top
			  }).show();
			  
			  var h = $(window).height(),
	    	  	h2 = h - $target.offset().top;
			  
			  if( h2 > $cm3Tip.outerHeight() ){
				  $cm3Tip.find(".bubbleTipArr").css("top",0);	
			  }else{
				  $cm3Tip.find(".bubbleTipArr").css("bottom",0).addClass("bubbleTipArr2");
				  $cm3Tip.css({
					  top : $target.offset().top - $cm3Tip.outerHeight() + 10
				  });
			  }
		  }
	  }).delegate('div.docsis3', 'mouseleave', function(e){
		  $('#cm3Tip').empty().hide();
	  });

		expander.on("beforeexpand",function(expander,r,body,row){
			if(lastRow !== null && lastRow !== row){
	            expander.collapseRow(lastRow);
	        }
			lastRow = row;
			var cmId = r.data.cmId;
			$.ajax({
	          url : "/cmCpe/cpeDetail.tv?cmId=" + cmId,
	          type: 'POST',
	          async:false,
	          cache : false,
	          dataType:'json',
	          success : function(cpeDetails) {
	        	  if(cpeDetails == null ){
	                    var tpl1 = new Ext.Template(
	                                'No Data' );
	                        expander.tpl = tpl1;
	                        expander.tpl.compile();
	                } else {
	                    var cmcHtml = '';
	                    $.each(cpeDetails,function(i,cpeDetail){
	                        cmcHtml = cmcHtml +
	                            "<tr>" +
	                            "<td align='center'>" + cpeDetail.topCmCpeIpAddress + "</td>" +
	                            "<td align='center'>" + cpeDetail.topCmCpeMacAddressString + "</td>" +
	                            "<td align='center'>" + cpeDetail.topCmCpeTypeString + "</td>" +
	                            "<td align='center'>" +
	                                "<a class='yellowLink' href='javascript:;' onclick='pingCpe(&quot;"+cpeDetail.topCmCpeIpAddress +"&quot;)' >Ping</a>" + " / " +
	                                "<a class='yellowLink' href='javascript:;' onclick='traceRouteCpe(&quot;"+cpeDetail.topCmCpeIpAddress+"&quot;)' >Traceroute</a>" +
	                            "</td>" +
	                            "</tr>";
	                        
	                    });
	                    
	                    var tpl1 = new Ext.Template(
	                        '<div style="margin:5px 5px 5px 10px;"><p class="flagP"><span class="flagInfo">@cm.cpeInfo@</span></p>' + 
	                        "<table width='96%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' class='dataTable extGridPanelTable addOneTr' rules='none'>" +
	                        "<thead>" +
	                        "<tr>" +
	                        "<th align='center'>IP</th>" +
	                        "<th align='center'>MAC</th>" +
	                        "<th align='center'>@CMC.text.type@</th>" +
	                        "<th align='center'>@CM.operate@</th>" +
	                        "</tr>" +
	                        "</thead>" +
	                        "<tbody id='tbody-append-child'>" + cmcHtml + "</tbody></table></div>"  
	                    );
	                    
	                    expander.tpl = tpl1;
	                    expander.tpl.compile();
                 }
	          }
	      });  
		}); 
	  $('#queryClick').bind('click', function(){
			lastRow = null;
			//mac地址需要进行校验
			var cmMac = $('#cmMacAddress').val();
			if (cmMac != "" && !Validator.isFuzzyMacAddress(cmMac)) {
			    top.afterSaveOrDelete({
			      title: '@COMMON.tip@',
			      html: '<b class="orangeTxt">@ccm/CCMTS.CmList.errorMac@</b>'
			    });
			    $('#cmMacAddress').focus();
			    return;
		  	}
			
	    	//生成查询条件并进行查询
	    	queryData = {
    			upperEntityType: upperEntityType,
	      		cmcId: cmcId,
	      		upChannelId: $('#upChannel').val(),
	      		downChannelId: $('#downChannel').val(),
	      		statusValue: $('#status').val(),
	      		cmMac: cmMac,
	      		start: 0,
	        	limit: pageSize,
	        	userId: $('#userId').val(),
	            userName: $('#userName').val(),
	            userAddr: $('#userAddr').val(),
	            userPhoneNo: $('#userPhoneNo').val(),
	            offerName: $('#offerName').val(),
	            configFile: $('#configFile').val(),
	            docsisMode: $('#select_docsisMode').val()
	    	};
	    	store.load({
	          	params: queryData
	      	});
	  });
	  
	  store.load({
	        params: queryData
	    });

	  /* function renderDownChannelSnr(value, p, record) {
	    if (value !== null && value !== "") {
	      return value + "dB";
	    }
	  } */
	  
	  function signalNoiseRender(value, p, record) {
			return record.data.docsIfCmtsCmStatusSignalNoiseString;
		}

		var cmFunction = ${cmFunctionJson}; 
	    if(cmFunction["reboot"] != "true"){
	        Ext.getCmp('restartCm').hide();
	        Ext.getCmp('restartAllCm').hide();  
	    }
	    if(cmFunction["clearOfflineCm"] != "true"){
	    	Ext.getCmp('clearOfflineCm').hide();  
	    }
	    if(cmFunction["clearSingleCm"] != "true"){
	    	Ext.getCmp('clearSingleCm').hide();
	    }
})

function showCmDetail(macAddr) {
    var sm = grid.getSelectionModel(), record = sm.getSelected();
    if (record == null) {
        return;
    }
    var cmMac = macAddr || record.data.statusMacAddress;
    window.parent.addView("CmCpeQuery", '@ccm/CCMTS.CmList.cmCpeQuery@', "icoG13",
            "/cmCpe/showCmCpeQuery.tv?sourcePage=cmListPage&cmMac=" + cmMac, null, true);
}
function pingCpe(ip) {
	window.parent.createDialog("modalDlg", 'Ping' + " - " + ip, 600, 400,
	  "entity/runCmd.tv?cmd=ping&ip=" + ip, null, true, true);
}
//YangYi Add @ 2013-11-15 Trace Route

function traceRouteCpe(ip) {
    window.parent.createDialog("modalDlg", 'Tracert ' + ip, 600, 400,
      "entity/runCmd.tv?cmd=tracert&ip=" + ip, null, true, true);
}

function disabledToolbarBtn(num){ //num为选中的的行的个数;
	if(num > 0){ 
		if(operationDevicePower){ 
			disabledBtn("restartCm", false);
		}
		if(refreshDevicePower){
			disabledBtn("refreshCmSignal_button", false);
		}
		if(operationDevicePower){ 
            disabledBtn("clearSingleCm", false);
        }
	}else{
		if(operationDevicePower){
			disabledBtn("restartCm", true);
		}
		if(refreshDevicePower){
			disabledBtn("refreshCmSignal_button", true);
		}
		if(operationDevicePower){ 
            disabledBtn("clearSingleCm", true);
        }
	}
}
function disabledBtn(id, disabled){
	Ext.getCmp(id).setDisabled(disabled); 
}
function showCmUpgrade(){
  var sm = grid.getSelectionModel(),
    record = sm.getSelected(),
    cmId = record.data.cmId;
   window.parent.createDialog("cmUpgrade", '@CM.upgrade@', 600, 370, "/cmupgrade/showUpgradeSingleCm.tv?entityId=" + cmId, null, true, true);
}
</script>
</head>
<body class="newBody">
	<div id="header" class="pB5">
      	<%@ include file="entity.inc"%>
	    <div id="queryContainer">
	    	<table>
	    		<tr>
	    			<td class="rightBlueTxt w80">@text.upChannel@@COMMON.maohao@</td>
	    			<td width="120">
	    				<select class="normalSel queryCondition w120" id="upChannel">
			    			<option value="0">@text.pleaseSelect@</option>
			    		</select>
	    			</td>
	    			<td class="rightBlueTxt w80">@text.downChannel@@COMMON.maohao@</td>
	    			<td width="120">
	    				<select class="normalSel queryCondition w120" id="downChannel">
			    			<option value="0">@text.pleaseSelect@</option>
			    		</select>
	    			</td>
	    			<td class="rightBlueTxt w80">@text.status@@COMMON.maohao@</td>
	    			<td width="120">
	    				<select class="normalSel queryCondition w120" id="status">
			    			<option value="0">@CMC.select.select@</option>
                            <option value="1">offline</option>
                            <option value="6">online</option>
                            <option value="21">online(d)</option>
                            <option value="26">p-online</option>
                            <option value="30">p-online(d)</option>
                            <option value="27">w-online</option>
                            <option value="31">w-online(d)</option>
                            <option value="10000">registering</option>
			    		</select>
	    			</td>
	    			<td class="rightBlueTxt w80">MAC@COMMON.maohao@</td>
					<td><input id="cmMacAddress" class="normalInput" style="width: 140px;" /></td>
	    		</tr>
	    		<tr>
	    			<td class="rightBlueTxt" width="70">@CM.userNo@@COMMON.maohao@</td>
					<td><input id="userId" class="normalInput w120"/></td>
					<td class="rightBlueTxt">@CM.userName@@COMMON.maohao@</td>
					<td><input id="userName" class="normalInput w120"/></td>
					<td class="rightBlueTxt">@CM.userAddr@@COMMON.maohao@</td>
					<td><input id="userAddr" class="normalInput w120"/></td>
					<td class="rightBlueTxt" width="90">@CM.docsicVersion@@COMMON.maohao@</td>
					<td>
						<select class="normalSel w140" id="select_docsisMode">
							<option value="0">@CMC.select.select@</option>
							<option value="1">1.0</option>
							<option value="2">1.1</option>
							<option value="3">2.0</option>
							<option value="4">3.0</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@CM.userPhone@@COMMON.maohao@</td>
					<td><input id="userPhoneNo" class="normalInput w120"/></td>
					<td class="rightBlueTxt">@CM.packageType@@COMMON.maohao@</td>
					<td><input id="offerName" class="normalInput w120"/></td>
					<td class="rightBlueTxt" width="70">@CM.configFile@@COMMON.maohao@</td>
					<td><input id="configFile" class="normalInput w120"/></td>
					<td></td>
					<td>
						
					</td>
				</tr>
	    		<tr>
	    			<td class="rightBlueTxt">@CM.partition@@COMMON.maohao@</td>
	    			<td colspan="7">
	    				<div id="partition"></div>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td></td>
	    			<td colspan="7">
	    				<a id="queryClick" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
	    			</td>
	    		</tr>
	    	</table>
	    </div>
	    
	    <div id="tip-div" class="thetips">
			<dl id="color-tips" style="padding:3px;">
				<dd class="mR2 yellow-div"></dd>
				<dd class="mR10">@ccm/CCMTS.CmList.processing@</dd>
				<dd class="mR2 green-div"></dd>
				<dd class="mR10">@CMC.tip.successfully@ (<b id="suc-num-dd">0</b>)</dd>
				<dd class="mR2 red-div"></dd>
				<dd class="mR5">@CMC.label.failure@ (<b id="fail-num-dd">0</b>)</dd>
			</dl>
			
			<!-- <dl id="operation-tips" style="padding:3px;"  class="thetips">
				<dd class="mR2">@ccm/CCMTS.CmList.successesNumber@@COMMON.maohao@</dd>
				<dd class="mR10" id="suc-num-dd">0</dd>
				<dd class="mR2">@ccm/CCMTS.CmList.failNumber@@COMMON.maohao@</dd>
				<dd class="mR2" id="fail-num-dd">0</dd>
			</dl> -->
		</div>
    </div>
    <div id="loading">@ccm/CCMTS.CmList.refreshingSignal@</div>
    <div id="cm3Tip" style="position: absolute;"></div>
</body>
</Zeta:HTML>
