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
    css css.white.disabledStyle
</Zeta:Loader>
<script src="/js/jquery/nm3kToolTip.js"></script>
<script src="/js/placeHolderHack.js"></script>
<style type="text/css">
body,html{height:100%;overflow:hidden;}
#queryContainer{margin: 10px 10px 0;}
.thetips dd{ float:left;}
.thetips dl{border:1px solid #ccc; float:left; background:#E1E1E1; }
#color-tips {left:5px;top:67px;}
#operation-tips{left:311px;top:67px;}
#suc-num-dd{color: #26B064;}
#fail-num-dd{color: #C07877;}
.yellow-div{height:16px;width:50px;background-color: #DCD345;}
.green-div{height:16px;width:50px;background-color: #ffffff;}
.red-div{height:16px;width:50px;background-color: #C07877;}
.normalTable .yellow-row{background-color:#DCD345 !important;border-color: #DCD345;}
.normalTable .red-row{background-color:#C07877 !important;}
.normalTable .green-row{background-color:#26B064 !important;}
.normalTable .white-row{background-color:#FFFFFF !important;border-color: #FFFFFF #DFDFDF #DFDFDF;}
.tip-dl{background: none repeat scroll 0 0 #F7F7F7;border: 1px solid #C2C2C2;color: #333333;padding: 2px 10px;position: absolute;z-index:2;}
.tip-dl dd {float: left;line-height: 1.5em;}
#loading {
	padding: 5px 8px 5px 26px;
	border: 1px solid #069;
	background: #F1E087 url('/images/refreshing2.gif') no-repeat 2px center;
	position: absolute;
	z-index: 999;
	top: 0px;
	left: 0px;
	display: none;
	font-weight: bold;
}
</style>
<script type="text/javascript">
var saveColumnId = 'cmts_cmlist';//保存表头数据的id;
var pageSize = <%= uc.getPageSize() %>;
var cmcId = <s:property value="cmcId"/>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var cmcName = '${cmcAttribute.nmName}';
var sysElecUnit = '@{unitConfigConstant.elecLevelUnit}@';

var cmcUpChannelBaseShowInfoList = ${cmcUpChannelBaseShowInfoListObject};
var cmcDownChannelBaseShowInfoList = ${cmcDownChannelBaseShowInfoObject};
var upperEntityType = EntityType.getCmtsType();
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

function refreshCmInfos(){
  window.top.showOkCancelConfirmDlg('@COMMON.tip@', '@text.refreshCmConfirm@', function (type) {
 	if(type=="ok"){
      window.parent.showWaitingDlg('@COMMON.waiting@', '@text.refreshCmTip@','waitingMsg','ext-mb-waiting');
      $.post('/cmts/cm/refreshCmOnCmtsInfo.tv?cmcId=' + cmcId, function(response){
    	  if(response == "true"){
				store.reload();  
				top.afterSaveOrDelete({
					title: '@COMMON.tip@',
					html: '<b class="orangeTxt">@text.refreshSuccessTip@</b>'
				});
				//window.parent.showMessageDlg('@COMMON.tip@', '@text.refreshSuccessTip@');
          }else{
           	    window.parent.showMessageDlg('@COMMON.tip@', '@text.refreshFailureTip@');
          }
      });
    }
  });
}

$(function(){
	$('#partition').cmIndexPartition().bind('click', openPartitionSelect);
	
	var default_columns = [
        	   	{header: "IP", width:120, sortable:true, align: 'center', dataIndex: 'displayIp', renderer: renderIp, exportRenderer: renderIpExport},  
        	   	{header: "MAC", width:150, sortable:true, align: 'center', dataIndex: 'statusMacAddress', renderer: renderMac},
        	   	{header: "@CMC.title.status@", width:60, sortable:false, align : 'center', dataIndex:'statusValue', renderer: CmUtil.statusValueRender, exportRenderer: statusValueRenderExport},
        	   	{header: "<div style='text-align:center'>@CMCPE.upDevice@</div>", width: 90, sortable: false, align: 'left', dataIndex: 'name'},
        	   	{header: "@cmcUserNum.lastCollectTime@", width:180, sortable: true, align: 'center', dataIndex: 'lastRefreshTime'},
        	   	{header: "<div class='txtCenter'>@CM.partialUpChl@</div>", width: 62, sortable: false, align: 'left', dataIndex: 'partialUpChannels'},
                {header: "<div class='txtCenter'>@CM.partialDownChl@</div>", width: 62, sortable: false, align: 'left', dataIndex: 'partialDownChannels'},
        	   	{header: "@upchannel@", width:120, sortable: true, align: 'center', dataIndex: 'statusUpChannelIfIndex', renderer: renderUpChannel, exportRenderer: renderUpChannelExport},
        	   	{header: "@CCMTS.downStreamChannel@", width:120, sortable: true, align : 'center', dataIndex: 'statusDownChannelIfIndex', renderer: renderDownChannel, exportRenderer: renderDownChannelExport},
        	   	{header: "@CM.upSnr@", width:120, sortable: false, align: 'center', dataIndex: 'statusSignalNoise', renderer: renderUpChannelSnr, exportRenderer: renderUpChannelSnrExport},
        	   	{header: "@CM.downSnr@", width:120, sortable: false, align: 'center', dataIndex: 'downChannelSnr', renderer: renderDownChannelSnr, exportRenderer: renderDownChannelSnrExport},
        	   	{header: "@CM.upSendPower@", width:120, sortable: false, align: 'center', dataIndex: 'upChannelTx', renderer: renderUpChannelTx, exportRenderer: renderUpChannelTxExport},
        	   	{header: "@CM.downReceivePower@", width:120, sortable: false, align: 'center', dataIndex: 'downChannelTx', renderer: renderDownChannelTx, exportRenderer: renderDownChannelTxExport},
        	   	{header: 'FLAP Ins', sortable: false, align: 'center', dataIndex: 'topCmFlapInsertionFailNum', renderer: renderFlapChart},
        	   	{header: '@CMC.title.Alias@', sortable: false, align: 'center', dataIndex: 'cmAlias'},
        	   	{header: '@CMC.title.usage@', sortable: false, align: 'center', dataIndex: 'cmClassified'},
        	   	{header: '@CM.userNo@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'userId'}, 
        	    {header: '@CM.userName@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'userName'}, 
        	    {header: '@CM.userAddr@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'userAddr'}, 
        	    {header: '@CM.userPhone@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'userPhoneNo'}, 
        	    {header: '@CM.packageType@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'offerName'}, 
        	    {header: '@CM.effectTime@', width: 140, sortable: false, hidden:true, align: 'center', dataIndex: 'effDate'}, 
        	    {header: '@CM.expirationTime@', width: 140, sortable: false, hidden:true, align: 'center', dataIndex: 'expDate'}, 
        	    {header: '@CM.configFile@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'configFile'},
        	    {header: '@CM.extension@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'extension'},
        	    {header: "<div class='txtCenter'>@VLAN.vlanOpera@</div>", width: 165, fixed: true, sortable: false, exportable: false, align: 'left', dataIndex: 'cmClassified',renderer: renderOperation}
           ];
	
	//构建其他菜单
	  var menuItem = [
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
	    {text: '@cm.viewCmStaticIp@', id: 'showCmStaticIp', handler: showCmStaticIpInfo} 
	  ];
	  entityMenu = new Ext.menu.Menu({
	    id: 'cmMenu',
	    enableScrolling: false,
	    minWidth: 160,
	    items: menuItem
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
	//加载CM表格
	var cmNumDivStr = '<div id="cm-num-div">'
	      .concat('<label>@CHANNEL.totalCmNum@</label><span style="margin-right:10px;" id="totalNum">0</span>')
	      .concat('<label>@CM.regestNum@</label><span id="onlineNum">0</span>')
	      .concat('</div>');
    tbar = new Ext.Toolbar({
	    items: [
	      {text: '@contactCmList.refreshCmSq@', id: 'refreshCmSignal_button', disabled: !refreshDevicePower, iconCls: 'bmenu_refresh', handler: refreshCmSignal}, 
	      '-', 
	      {text: '@CMC.title.refreshDataFromEntity@', id: 'restartAllCm', disabled: !refreshDevicePower, iconCls: 'bmenu_equipment', handler: refreshCmInfos}, 
	      '-', {
              text: "@COMMON.exportExcel@", 
              iconCls: 'bmenu_exportWithSub',
              menu: [{
                  text : '@resources/COMMON.exportCurrentColumn@',
                  handler : function(){
                      top.ExcelUtil.exportGridToExcel(grid, cmcName + '-'+ '@CM.cmList@', {
                          allColumn: false
                      });
                  }
              },{
                  text : '@resources/COMMON.exportAllColumn@',
                  handler : function(){
                      top.ExcelUtil.exportGridToExcel(grid, cmcName + '-'+ '@CM.cmList@', {
                          allColumn: true
                      });
                  }
              }]
          }, 
	      '->', 
	      {xtype: 'tbtext', text: cmNumDivStr}
	    ]
  	});
    var sm = new Ext.grid.CheckboxSelectionModel();
    var cmConfig = CustomColumnModel.init(saveColumnId, default_columns, {sm:sm}),
    		  cm = cmConfig.cm,
    	sortInfo = cmConfig.sortInfo || {field: 'displayIp', direction: 'ASC'};
    
    store = new Ext.data.JsonStore({
      url: '/cmlist/getCmByCmcId.tv',
    root: 'data',
    idProperty: "cmId",
    totalProperty: 'rowCount',
    remoteSort: true,
    baseParams: {
      cmcId: cmcId,
      limit: pageSize,
      upperEntityType: upperEntityType,
      sort: sortInfo.field,
      dir: sortInfo.direction
    },
    fields: [
        'cmcId', 'statusIndex', 'cmcDeviceStyle', 'cmId', 'statusIpAddress', 'docsIfCmtsCmStatusInetAddressTypeString', 'cmAlias', 'cmClassified','topCmFlapInsertionFailNum','downChannelId','displayStatus',
            'name','statusInetAddress', 'statusMacAddress', 'statusDownChannelIfIndex', 'statusUpChannelIfIndex','docsIf3CmtsCmUsStatusList','docsIfSigQSignalNoiseForUnit',
            'docsIfCmtsCmStatusRxPowerString','docsIfCmtsCmStatusSignalNoiseString', 'downChannelSnr','downChannelTx','upChannelTx', 'statusSignalNoise', 'displayIp',
            'docsIfCmtsCmStatusValueString','statusValue', 'upChannelId','upChannelSnr','lastRefreshTime', 'upChannelIndexString', 'downChannelIndexString','downChannelRecvPower','upChannelTransPower',
            'userId', 'userName', 'userAddr', 'userPhoneNo', 'offerName', 'effDate', 'expDate', 'configFile', 'extension', 'collectTime', 'partialUpChannels', 'partialDownChannels']
    });
    if(cmConfig.sortInfo){
   	  store.setDefaultSort(cmConfig.sortInfo.field, cmConfig.sortInfo.direction);
    }
    
    store.on('beforeload', function() {
    	$('#suc-num-dd').text(0);
    	$('#fail-num-dd').text(0);
        store.baseParams = queryData;
    });
    //store在加载完数据后更新CM数目信息
    store.on('load', function() {
        //刷新页面统计信息
        loadCmNumUnderCcmts();
    });
    store.setDefaultSort(sortInfo.field, sortInfo.direction);
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
          margins:'10px',
          animCollapse: animCollapse, 
          trackMouseOver: trackMouseOver, 
          border: true, 
          totalProperty: 'rowCount',
          id: 'cmGrid',
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
          store: store, 
          cm: cm,
          sm: sm,
          tbar: tbar,
          bbar: bbar
         });
	  
	  new Ext.Viewport({
	    layout: 'border',
	      items: [{
	          region: 'north',
	          border: false,
	          contentEl: 'header'
	      },
	          grid
	      ]
	  });
	  
	  //构造查询下拉框
	  var upPosition = Zeta$('upChannel');
	  for(var i = 0; i < cmcUpChannelBaseShowInfoList.length; i++){
	    	var option = document.createElement('option');
	    	option.value = cmcUpChannelBaseShowInfoList[i].channelId;
	    	option.text = cmcUpChannelBaseShowInfoList[i].ifName;
	    	try {
	      		upPosition.add(option, null);
	        } catch(ex) {
	          	upPosition.add(option);
	        }
	  }
	  
	  var downPosition = Zeta$('downChannel');
	  for(var i = 0; i < cmcDownChannelBaseShowInfoList.length; i++){
	      var option = document.createElement('option');
	      option.value = cmcDownChannelBaseShowInfoList[i].docsIfDownChannelId;
	      option.text = cmcDownChannelBaseShowInfoList[i].ifName;
	      try {
      		  downPosition.add(option, null);
	      } catch(ex) {
	          downPosition.add(option);
	      }
	  }
	  
	  $('#queryClick').bind('click', function(){
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
	            configFile: $('#configFile').val()
	    	};
	    	store.load({
	          	params: queryData
	      	});
	  });
	  
	  store.load({
	        params: queryData
	    });

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
</script>
</head>
<body class="bodyGrayBg">
	<div id="header">
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
						<a id="queryClick" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
					</td>
				</tr>
	    		<tr>
    				<td class="rightBlueTxt">@CM.partition@@COMMON.maohao@</td>
	    			<td colspan="8"><div id="partition"></div></td>
	    		</tr>
	    	</table>
	    </div>
	    <div id="tip-div" class="thetips">
			<dl id="color-tips" style="margin-right:5px; padding:3px;">
				<dd class="mR2 yellow-div"></dd>
				<dd class="mR10">@ccm/CCMTS.CmList.processing@</dd>
				<dd class="mR2 green-div"></dd>
				<dd class="mR10">@CMC.tip.successfully@</dd>
				<dd class="mR2 red-div"></dd>
				<dd class="mR10">@CMC.label.failure@</dd>
			</dl>
			
			<dl id="operation-tips" style="padding:3px;"  class="thetips">
				<dd class="mR2">@ccm/CCMTS.CmList.successesNumber@@COMMON.maohao@</dd>
				<dd class="mR10" id="suc-num-dd">0</dd>
				<dd class="mR2">@ccm/CCMTS.CmList.failNumber@@COMMON.maohao@</dd>
				<dd class="mR2" id="fail-num-dd">0</dd>
			</dl>
		</div>
	    
    </div>
    <div id="loading">@ccm/CCMTS.CmList.refreshingSignal@</div>
</body>
</Zeta:HTML>