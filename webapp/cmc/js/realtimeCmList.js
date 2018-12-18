var lastRow = null;
var store;
var grid;
var chooseStatus;
var CmMenu=null;

var flag=0;
var searchData = {
		upSnrMin: '',
	    upSnrMax: '',
	    downSnrMin: '',
	    downSnrMax: '',
	    upPowerMin: '',
	    upPowerMax: '',
	    downPowerMin: '',
	    downPowerMax: ''
};
var partitionData = {
		upSnrMin: '',
	    upSnrMax: '',
	    downSnrMin: '',
	    downSnrMax: '',
	    upPowerMin: '',
	    upPowerMax: '',
	    downPowerMin: '',
	    downPowerMax: '',
	    cmcId:''
	};

function openPartitionSelect(){
	window.top.createDialog("modalDlg",'@CM.setPartition@',800, 370, '/cmlist/showPartitionSelect.tv?partitionDataStr='+encodeURI(JSON.stringify(partitionData)), null, true,true);
}

function partitionChanged(data){
	partitionData = data;
	$('#partition').cmIndexPartition.change(partitionData);
	searchData.upSnrMin=partitionData.upSnrMin;
	searchData.upSnrMax=partitionData.upSnrMax;
	searchData.downSnrMin=partitionData.downSnrMin;
	searchData.downSnrMax=partitionData.downSnrMax;
	searchData.upPowerMin=partitionData.upPowerMin;
	searchData.upPowerMax=partitionData.upPowerMax;
	searchData.downPowerMin=partitionData.downPowerMin;
	searchData.downPowerMax=partitionData.downPowerMax;

}

function newCmFaultInfo(s){		
	Str = '<div class="cmFaultTip" id="cmFaultTip"><div class="faultTipArr"></div>'
	    + '<div class="TipContent">'
	    + '<p class="tt" style="text-align:center;" >@CM.faultTitle@</p>'
	    + s
	    + '</div></div>';
	    
   return Str;
} 

$("#cmFaultInfo").live("click",function(){
	if(flag==0){
		$('#faultTips').css("display","block");
		var $myself=$(this);
		var  $faultTip=$('#cmFaultTip');
		var  xpos=$myself.offset().left;
		var  ypos=$myself.offset().top;
		var  htmlStr=$('#faultTips').html();	
		
		var str= newCmFaultInfo(htmlStr);
		$('body').append(str);
		$faultTip=$('#cmFaultTip');
		$faultTip.css({left : xpos+20, top : ypos+10});
		$faultTip.find('.faultTipArr').css("top",0);		
	}
	flag=1;

}).live("mouseleave",function(){
	var $faultTip=$('#cmFaultTip');
	$faultTip.remove();
	flag=0;
})

Ext.onReady(function() {
	var w = $(window).width();
	var h = $(window).height();
	partitionData.cmcId=cmcId;
	
	$('#partition').cmIndexPartition().bind('click', openPartitionSelect);
	var expander = new Ext.ux.grid.RowExpander({
		id : 'expander' ,
		dataIndex : 'statusIndex',
	    enableCaching : false,
	  tpl : ''
	});
	expander.on("beforeexpand",function(expander,r,body,row){
		if(lastRow != null){
			expander.collapseRow(lastRow);
		}
		lastRow = row;
		var statusMacAddress = r.data.statusMacAddress;
		$.ajax({
          url : "/cmCpe/refreshCpeListByCmMac.tv?cmcId="+cmcId+"&cmMac=" + statusMacAddress,
          type: 'POST',
          async:false,
          cache : false,
          dataType:'json',
          success : function(cpeDetails) {
          	if(cpeDetails == null ){
          		var tpl1 = new Ext.Template('No Data');
	    			expander.tpl = tpl1;
	    			expander.tpl.compile();
          	}else{
      			var cmcHtml = '';
          		$.each(cpeDetails,function(i,cpeDetail){
      				cmcHtml = cmcHtml +
      				"<tr id='cpe-"+cpeDetail.topCmCpeMacAddress+"'>" +
	                    "<td align='center' class='ip'>" + cpeDetail.topCmCpeIpAddress + "</td>" +
	                    "<td align='center' class='mac'>" + cpeDetail.topCmCpeMacAddress + "</td>" +
	                    "<td align='center' class='type'>" + cpeDetail.topCmCpeTypeStr + "</td>" +
	                    "<td align='center'>" +
	                    	"<a class='yellowLink' href='javascript:;' data-cmcid='"+cmcId+"' data-mac='"+cpeDetail.topCmCpeMacAddress+"' onclick='refreshCpe(this)' >"+ I18N.SYSTEM.refresh +"</a>" + " / " +
		                    "<a class='yellowLink' href='javascript:;' data-ip='"+cpeDetail.topCmCpeIpAddress+"' onclick='pingCpe(this)' >Ping</a>" + " / " +
	                    	"<a class='yellowLink' href='javascript:;' data-ip='"+cpeDetail.topCmCpeIpAddress+"' onclick='traceRouteCpe(this)' >Traceroute</a>" +
	                    "</td>" +
	                    "</tr>";
      				
                });
          		
          		var tpl1 = new Ext.Template(
	        		'<div style="position:relative;"><div style="margin:5px 5px 5px 10px;"><p class="flagP"><span class="flagInfo">' + I18N.cm.cpeInfo + '</span></p>' + 
	        		"<table id='cpeList' width='96%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' class='dataTable extGridPanelTable addOneTr' rules='none'>" +
	                "<thead>" +
	                "<tr>" +
	                "<th align='center'>IP</th>" +
	                "<th align='center'>MAC</th>" +
	                "<th align='center'>" + I18N.CMC.text.type + "</th>" +
	                "<th align='center'>" + I18N.CM.operate + "</th>" +
	                "</tr>" +
	                "</thead>" +
	                "<tbody id='tbody-append-child'>" + cmcHtml + "</tbody></table>" +
	                "</div>" +
	                "<a href='javascript:;' class='stopEvent normalBtn rightTopLink' data-mac='"+statusMacAddress+"' onclick='refreshCpeList(this);return false;'> " +
		            "<span class='stopEvent'>"+
		            "<i class='stopEvent miniIcoRefresh'></i>" + I18N.CMC.button.refresh +
		            "</span>" +
		            "</a></div>" 
                );
            	
    	        expander.tpl = tpl1;
    			expander.tpl.compile();
          	}

          }
      });  
	}); 
	var columns;
	if (isSupportRealtimeCpeQuery == "true") {
		columns = [expander,
		{header: I18N.NETWORK.entityIp, 			width: 90, sortable:false, align: 'center', dataIndex: 'statusInetAddress',renderer:renderIp},    
		{header: I18N.CCMTS.macAddress, 			width: 120,  sortable:false, align: 'center', dataIndex: 'statusMacAddress',renderer:renderMac},
	    {header: I18N.SYSTEM.status, 		 		width: 60, sortable:false, align: 'center', dataIndex: 'statusValue',				renderer: renderStatusValue},
	    {header: I18N.CCMTS.upStreamChannel, 		width: 140, sortable:false, align: 'center', dataIndex: 'docsIf3CmtsCmUsStatusList', renderer: renderUpChannel},
	    {header: I18N.CCMTS.downStreamChannel, 		width: 140, sortable:false, align: 'center', dataIndex: 'statusDownChannelIfIndex',	renderer: renderDownChannel},
	    {header: I18N.CM.upSnr+'(dB)',				sortable:false, align: 'center', dataIndex: 'upChannelSnr',				renderer: renderUpChannelSnr},
	    {header: I18N.CM.downSnr+'(dB)', 			sortable:false, align: 'center', dataIndex: 'downChannelSnr', 			renderer: renderDownChannelSnr},	    	    
	    {header: I18N.CM.upSendPower+'(@{unitConfigConstant.elecLevelUnit}@)',		sortable:false, align: 'center', dataIndex: 'upChannelTx',  renderer: rendererUpChannelTx},
	    {header: I18N.CM.downReceivePower+'(@{unitConfigConstant.elecLevelUnit}@)', sortable:false, align: 'center', dataIndex: 'downChannelRx', renderer: renderDownChannelRx},
	    {header: I18N.NETWORK.operationRCm,                 sortable:false, align:'center',  renderer:renderOperationRealCmList}
		];
	} else {
		columns = [
		{header: I18N.NETWORK.entityIp, 			width: 90, sortable:false, align: 'center', dataIndex: 'statusInetAddress',renderer:renderIp},  
		{header: I18N.CCMTS.macAddress, 			width: 120,  sortable:false, align: 'center', dataIndex: 'statusMacAddress',renderer:renderMac},
	    {header: I18N.SYSTEM.status, 		 		width: 60, sortable:false, align: 'center', dataIndex: 'statusValue',				renderer: renderStatusValue},
	    {header: I18N.CCMTS.upStreamChannel, 		width: 140, sortable:false, align: 'center', dataIndex: 'docsIf3CmtsCmUsStatusList', renderer: renderUpChannel},
	    {header: I18N.CCMTS.downStreamChannel, 		width: 140, sortable:false, align: 'center', dataIndex: 'statusDownChannelIfIndex',	renderer: renderDownChannel},
	    {header: I18N.CM.upSnr+'(dB)',				sortable:false, align: 'center', dataIndex: 'upChannelSnr',				renderer: renderUpChannelSnr},
	    {header: I18N.CM.downSnr+'(dB)', 			sortable:false, align: 'center', dataIndex: 'downChannelSnr', 			renderer: renderDownChannelSnr},
	    {header: I18N.CM.upSendPower+'(@{unitConfigConstant.elecLevelUnit}@)',		sortable:false, align: 'center', dataIndex: 'upChannelTx',   			renderer: rendererUpChannelTx},
	    {header: I18N.CM.downReceivePower+'(@{unitConfigConstant.elecLevelUnit}@)', sortable:false, align: 'center', dataIndex: 'downChannelRx',  			renderer: renderDownChannelRx},
	    {header: I18N.NETWORK.operationRCm,                 sortable:false, align:'center',  renderer:renderOperationRealCmList}
       	];
	}
	Ext.Ajax.timeout = 150000;  
	store = new Ext.data.JsonStore({
        url: '/realtimecmlist/loadRealtimeCmList.tv?cmcId=' + cmcId + '&cmStatus=' + cmStatus + '&cmIndexs=' + cmIndexs,
        root: 'data',
        totalProperty: 'rowCount',
        remoteSort: false, 
        fields: ['statusIndex',       'downChannelId',  'statusIpAddress',
                 'statusValue',    'statusInetAddress', 'statusMacAddress', 
                 'downChannelSnr', 'downChannelRx',     'upChannelTx',
                 'upChannelId',    'upChannelSnr',	    'docsIf3CmtsCmUsStatusList', 
                 'cmRxPower',      'cmTxPower',         'cmSignalNoise',  'statusSignalNoise',
                 'cmDownChId',     'cmUpChId',          'cmUpSnrChId',
                 'statusDownChannelIfIndex', 	        'docsIfCmtsCmStatusSignalNoiseString'   	      
                ]
    });
	var cm = new Ext.grid.ColumnModel(columns);
	var toolBar=[{text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: refreshClick}]
	function refreshClick(){
		onSearchClick();
	}
//	function buildPagingToolBar(){
//		var bt=new Ext.PagingToolbar({
//			store:store,
//			displayInfo:true,
//			items:[]
//		})
//		return bt;
//	};
	if (isSupportRealtimeCpeQuery == "true") {
		grid = new Ext.grid.GridPanel({
			cls: 'normalTable',
			region: 'center',
			border: true, 
			loadMask: true,
			height:300,
			tbar:toolBar,
			totalProperty: 'rowCount',
			store: store, 
			bodyCssClass : 'normalTable',	
			plugins: expander,
			cm: cm,
//			bbar:buildPagingToolBar(),
			viewConfig: { forceFit:true}
		});
	} else {
		grid = new Ext.grid.GridPanel({
			cls: 'normalTable',
			region: 'center',
			border: true, 
			loadMask: true,
			totalProperty: 'rowCount',
			tbar:toolBar,
			height:300,
			store: store, 
			bodyCssClass : 'normalTable',	
			cm: cm,
//			bbar:buildPagingToolBar(),
			viewConfig: { forceFit:true}
		});
	}
	store.on('load', function(t,records){
//		loadCmSignal();
//		loadUpChannelSnr();
		var r='';
		for(var i=0;i<records.length;i++){
			r+=records[i].json;
		}
		if(records.length>0){			
			if(r=="getDataFail"){
				window.parent.showMessageDlg(I18N.COMMON.tip, "@CM.getDataFail@");
			}else{
				getSelectedList();
			}	
		}		
	});
		
	var tip = '<div style="background:#FFE0E0; border:1px solid #DDB3B3; padding:8px; color:#FF3334; display:inline-block;"><b>' + I18N.CMC.title.tip + I18N.cmc.maohao + '</b>' + I18N.tip.realtimeCmList + '</div>';
	
	var statusType=new Ext.data.Record.create([
		{name:'type',type:'string'},
		{name:'value',type:'int'}
	])
	var statusData =[
	                ['offline',1],['init(r2)',2],
	                ['init(i)',5],['online',6],
	                ['init(d)',10],['init(io)',11],
	                ['online(d)',21],['init6(s)',22],
	                ['init6(a)',23],['init6(r)',25],
	                ['p-online',26],['p-online(d)',30],
	                ['w-online',27],['w-online(d)',31],
	                ['@TIP.other@',100]                
	 ]
	var statusStore=new Ext.data.Store({
		proxy:new Ext.data.MemoryProxy(statusData),
		reader:new Ext.data.ArrayReader({},statusType)
	})	
	statusStore.load();	
	var statusTypecombo=new Ext.ux.form.LovCombo({
		width:150,
		renderTo:select_cmMacStatus,
		id:"statusTypeId",
		hideOnSelect:true,
		editable:false,
		store:statusStore,
        valueField: 'value',
		displayField: 'type',
        triggerAction : 'all',  
        mode:'local',
        emptyText : "@COMMON.pleaseSelect@",
        showSelectAll : true,
        beforeBlur : function(){}		
	})
	if(cmStatus==null||cmStatus==""||cmStatus=='JUMPSTATUS_ALLSHOW'){
		statusTypecombo.selectAll();
	}else if(cmStatus=='JUMPSTATUS_ONLINE'){
		statusTypecombo.setValue('6,21,26,27,30,31');
	}else if(cmStatus=='JUMPSTATUS_OFFLINE'){
		statusTypecombo.setValue(1);
	}else if(cmStatus=='JUMPSTATUS_ONLINING'){
		statusTypecombo.setValue('2,5,10,11,22,23,25,100');
	}
		
	new Ext.Viewport({
	    layout: 'border',
	    items: [
	      {
	        region: 'north',
	        border: false,
	        contentEl: 'query-container',
	        height: 60
	      },
	      grid,
	      {
		    region: 'south',
		    html: tip,
		    cls:'clear-x-panel-body', 
		    autoHeight: true,
		    border: false,
		    margins: '10px'
	       }
	     ]
	});

	store.load();
})


function onSearchClick() {
	var tt=Ext.getCmp("statusTypeId")
	store.baseParams={
		chooseStatus:tt.getCheckedValue()
	}
	store.load();
}

function refreshCpeList(me,e) {
	var mac = $(me).data("mac");
    $.ajax({
        type: "POST",
        url: "/cmCpe/refreshCpeListByCmMac.tv",
        data: "cmcId="+cmcId+"&cmMac="+mac,cache: false,
        dataType: "json",
        success: function(cpeInfo) {
        	 $("#cpeList>tbody>tr").empty();
             var html = '';
       		 $.each(cpeInfo,function(i,cpeDetail){
   				var cmcHtml = 
   					"<tr id='cpe-"+cpeDetail.topCmCpeMacAddress+"'>" +
	                    "<td align='center' class='ip'>" + cpeDetail.topCmCpeIpAddress + "</td>" +
	                    "<td align='center' class='mac'>" + cpeDetail.topCmCpeMacAddress + "</td>" +
	                    "<td align='center' class='type'>" + cpeDetail.topCmCpeTypeStr + "</td>" +
	                    "<td align='center' class='opt'>" +
		                    "<a class='yellowLink' href='javascript:;' data-cmcid="+cmcId+" data-mac='"+cpeDetail.topCmCpeMacAddress+"' onclick='refreshCpe(this)' >"+ I18N.SYSTEM.refresh +"</a>" + " / " +
		                    "<a class='yellowLink' href='javascript:;' data-ip='"+cpeDetail.topCmCpeIpAddress+"' onclick='pingCpe(this)' >Ping</a>" + " / " +
	                    	"<a class='yellowLink' href='javascript:;' data-ip='"+cpeDetail.topCmCpeIpAddress+"' onclick='traceRouteCpe(this)' >Traceroute</a>" +
	                    "</td>" +
	                    "</tr>";
   				html = html + cmcHtml;
   				
             });
             $("#cpeList>tbody").append(html);

             top.afterSaveOrDelete({
               title: I18N.COMMON.tip,
               html: '<b class="orangeTxt">' + I18N.CMC.tip.refreshSuccess + '</b>'
             });
        },
        error:function(e){
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.refreshFailure);
        }
    });
    return false;
}
function refreshCpe(me) {
	var mac = $(me).data("mac");
	var cmcId = parseInt($(me).data("cmcid"),10);
    $.ajax({
        type: "POST",
        url: "/cmCpe/refreshCpeByCmcAndCpeMac.tv",
        data: "cpeMac="+mac+"&cmcId="+cmcId,cache: false,
        dataType: "json",
        success: function(json) {
        	var tr = $("#cpe-"+mac);
        	$(".ip",tr).text(json.topCmCpeIpAddress);
        	$(".mac",tr).text(json.topCmCpeMacAddressString);
        	$(".type",tr).text(json.topCmCpeTypeString);
        	$(".opt",tr).empty().append("<a class='yellowLink' href='javascript:;' data-cmcid='"+cmcId+"' data-mac='"+json.topCmCpeMacAddressString+"' onclick='refreshCpe(this)' >"+ I18N.SYSTEM.refresh +"</a>" + " / " +
             "<a class='yellowLink' href='javascript:;' data-ip='"+json.topCmCpeIpAddress+"' onclick='pingCpe(this)' >Ping</a>" + " / " +
         	"<a class='yellowLink' href='javascript:;' data-ip='"+json.topCmCpeIpAddress+"' onclick='traceRouteCpe(this)' >Traceroute</a>" );

            top.afterSaveOrDelete({
              title: I18N.COMMON.tip,
              html: '<b class="orangeTxt">' + I18N.CMC.tip.refreshSuccess + '</b>'
            });
        },
        error:function(e){
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.refreshFailure);
        }
    });
}


function pingCpe(me) {
	var ip = $(me).data("ip");
	window.parent.createDialog("modalDlg", 'Ping' + " - " + ip, 600, 400,
	  "entity/runCmd.tv?cmd=ping&ip=" + ip, null, true, true);
}

function traceRouteCpe(me) {
	var ip = $(me).data("ip");
    window.parent.createDialog("modalDlg", 'Tracert ' + ip, 600, 400,
      "entity/runCmd.tv?cmd=tracert&ip=" + ip, null, true, true);
}


function renderOperationRealCmList(v,p,r){
	cmId=r.data.cmId;
	var Str=String.format("<a href='javascript:;' onclick='pingCm()'>Ping CM</a> / <a href='javascript:;' class='withSub' onclick='showMoreOperation(\"{0}\",event)'>@CM.else@</a>",cmId);
	return Str;
}

function showMoreOperation(cmId, event){
	  var record = grid.getStore().getById(cmId),
	    x = event.clientX,
	    y = event.clientY;
	  Ext.getCmp("cmMenu").findById("traceRoute").setDisabled(false);
	  Ext.getCmp("cmMenu").findById("mibBrowser").setDisabled(false);
	  CmMenu.showAt([event.clientX,event.clientY]);
}

var menuItem = [ {
	text : 'Trace Route',
	id : 'traceRoute',
	handler : traceRoute
}, {
	text : 'Mib Browser',
	id : 'mibBrowser',
	handler : mibBrowser
} ];

   CmMenu=new Ext.menu.Menu({
     id: 'cmMenu',
     enableScrolling: false,
     minWidth: 160,
     items: menuItem
    });

function judgeShowIp(record){
	var stIpAdd=record.data.statusIpAddress;
	var stInetAdd=record.data.statusInetAddress;
	if(stIpAdd!=null&&stIpAdd.length!=0){
		return stIpAdd;
	}else{
		return stInetAdd;
	}
}
   
function pingCm() {
	  var sm = grid.getSelectionModel(),
	    record = sm.getSelected(),
//	    cmIp = judgeShowIp(record);
	    cmIp = record.data.statusInetAddress;
	    cmcdevicestyle = record.data.cmcDeviceStyle;
	    if (cmIp) {
	      if(cmPingMode == 2) {
	          window.parent.createDialog("modalDlg", 'Ping' + " - " + cmIp, 600, 400, 
	        	      "entity/snmpPing.tv?cmd=snmpping&entityId="+cmcId+"&ip=" + cmIp, null, true, true);
	      }
		  else{
			  window.parent.createDialog("modalDlg", 'Ping' + " - " + cmIp, 600, 400,
			          "entity/runCmd.tv?cmd=ping&ip=" + cmIp, null, true, true);
		  }
	    }
	}

function traceRoute() {
	  var sm = grid.getSelectionModel();
	  var record = sm.getSelected();
//	  cmIp = judgeShowIp(record);
	  var cmIp = record.data.statusInetAddress;
	  if (cmIp) {
	    window.parent.createDialog("modalDlg", 'Tracert ' + cmIp, 600, 400,
	      "entity/runCmd.tv?cmd=tracert&ip=" + cmIp, null, true, true);
	  }
}

function mibBrowser() {
	  var sm = grid.getSelectionModel(),
	      record = sm.getSelected();
//	  var cmIp = judgeShowIp(record),
	  var cmIp = record.data.statusInetAddress,
	      mibbleType = 35000,
	  	  enterCount = 1;
	  window.top.addView('mibbleBrowser', 'MIB Browser','entityTabIcon', '/mibble/showMibbleBrowser.tv?host=' + cmIp + '&mibbleType=' + mibbleType + '&enterCount=' + enterCount,null, true);
}

function showCmDetail(macAddr) {
	  var sm = grid.getSelectionModel(),
	    record = sm.getSelected();
	  if (record == null) {
	    return;
	  }
	  var cmMac = macAddr || record.data.statusMacAddress;
	  window.parent.addView("CmCpeQuery", '@ccm/CCMTS.CmList.cmCpeQuery@', "mydesktopIcon", "/cmCpe/showCmCpeQuery.tv?sourcePage=cmListPage&cmMac=" + cmMac, null, true);
	}

function renderMac(v,p,r){
	if(v != ""){
		return String.format('<a href="javascript:;" onclick="showCmDetail(\'{0}\')">{0}</a>',v);
	}else{
		return v;
	}		
}


function renderIp(v, p, r) {
	  if (v === null || v === "" || v === "noSuchObject" || v === "noSuchInstance" || v === "0.0.0.0" || v==='--') {
		  return "--";
	  } else {
		//cm单机Web的跳转
		return String.format('<a href="http://{0}" target="_blank">{0}</a>', v);
	  }
	}

//---------------------------
//------上行信道SNR获取-------
//---------------------------
function loadUpChannelSnr(){
	$.ajax({
        url : '/realtimecmlist/loadUpChannelSnr.tv',
        type : 'post',
        dataType : "json",
        data : "&cmcId=" + cmcId,
        success : function(json) {
        	var upChannelMap = json.upChannelSnr;
        	for(var key in upChannelMap){
        		for(var i = 0; i < store.getCount(); i++){
        			if(key == store.getAt(i).get('statusIndex')){
        				store.getAt(i).set('upChannelSnr',upChannelMap[key]);
        				store.getAt(i).commit();
        			}
        		}
        	}
        },
        error : function() {},
        cache : false
    });
}


//---------------------------
//------实时信号质量获取-------
//---------------------------
function loadCmSignal(){
	var cmId = "";
	var cmIndex = "";
	for (var i = 0; i < store.getCount(); i++){
		cmId = store.getAt(i).get('cmId');
		cmIndex = store.getAt(i).get('statusIndex');
		$.ajax({
	        url : '/realtimecmlist/loadCmSignal.tv',
	        type : 'post',
	        dataType : "json",
	        data : "&cmIndex=" + cmIndex + "&cmcId=" + cmcId,
	        success : function(json) {
	        	var cmIndex = json.cmIndex;
	        	var downSnrArray = json.downChannelSnr;
	        	var downRxArray = json.downChannelRx;
	        	var upTxArray = json.upChannelTx;
	        	for (var i = 0; i < store.getCount(); i++){
	        		if(cmIndex == store.getAt(i).get('statusIndex')){
	        			store.getAt(i).set('downChannelSnr',downSnrArray);
	        			store.getAt(i).set('downChannelRx',downRxArray);
	        			store.getAt(i).set('upChannelTx',upTxArray);
	        			store.getAt(i).commit();
	        		}
	        	}
	        },
	        error : function() {},
	        cache : false
	    });
	}
}


function getSelectedList(){
	var count=store.getCount();
	for (var i = 0; i < store.getCount(); i++){
		if(count>0){			
		var downChannelSnr=store.getAt(i).get('downChannelSnr');
		var upChannelSnr=store.getAt(i).get('upChannelSnr');
		var downChannelRx=store.getAt(i).get('downChannelRx');
		var upChannelTx=store.getAt(i).get('upChannelTx');
		var dChId=store.getAt(i).get('cmDownChId');
		var uChId=store.getAt(i).get('cmUpChId');
		var uChIdBak=store.getAt(i).get('cmUpSnrChId');
		if((uChId==null||uChId.length==0)&&(uChIdBak.length!=0&&uChIdBak!=null)){
			uChId=uChIdBak;
		}
		var getDownChannelSnr,getUpChannelSnr,getDownChannelRx,getUpChannelTx;
		var DCprimary=store.getAt(i).get('downChannelId');
		var UCprimary=store.getAt(i).get('upChannelId');
		if (downChannelSnr.length>1){
			for(var j=0;j<downChannelSnr.length;j++){
				if(DCprimary==dChId[j]){
					getDownChannelSnr=downChannelSnr[j];
				}
			}
		}else{
			getDownChannelSnr=downChannelSnr;
		}
		if (downChannelRx.length>1){
			for(var j=0;j<downChannelRx.length;j++){
				if(DCprimary==dChId[j]){
					getDownChannelRx=downChannelRx[j];
				}
			}
		}else{
			getDownChannelRx=downChannelRx;
		}
		if (upChannelTx.length>1){
			for(var j=0;j<upChannelTx.length;j++){
				if(UCprimary==uChId[j]){
					getUpChannelTx=upChannelTx[j];
				}
			}
		}else{
			getUpChannelTx=upChannelTx;
		}
		if (upChannelSnr.length>1){
			for(var j=0;j<upChannelSnr.length;j++){
				if(UCprimary==uChId[j]){
					getUpChannelSnr=upChannelSnr[j];
				}
			}
		}else{
			getUpChannelSnr=upChannelSnr;
		}
		var upChTxFlag=0,downChTxFlag=0,upChSnrFlag=0,downChSnrFlag=0;
        //条件1判断
		if(searchData.upSnrMin!=''&&searchData.upSnrMax!=''){
			if(upChannelSnr.length>0){
				if(getUpChannelSnr>=searchData.upSnrMin&&getUpChannelSnr<searchData.upSnrMax){
					upChSnrFlag=1;
				}
			}					
		}else if(searchData.upSnrMin==''&&searchData.upSnrMax!=''){
			if(upChannelSnr.length>0){
				if(getUpChannelSnr<searchData.upSnrMax){
				upChSnrFlag=1;
			    }
			}
		}else if(searchData.upSnrMin!=''&&searchData.upSnrMax==''){
			if(upChannelSnr.length>0){
			    if(getUpChannelSnr>=searchData.upSnrMin){
				upChSnrFlag=1;
			    }
			}
		}else if(searchData.upSnrMin==''&&searchData.upSnrMax==''){
			upChSnrFlag=1;
		}
		//条件2判断
		if(searchData.downSnrMin!=''&&searchData.downSnrMax!=''){
			if(downChannelSnr.length>0){
				if(getDownChannelSnr>=searchData.downSnrMin&&getDownChannelSnr<searchData.downSnrMax){
					downChSnrFlag=1;
				}
			}				
		}else if(searchData.downSnrMin==''&&searchData.downSnrMax!=''){
			if(downChannelSnr.length>0){
			    if(getDownChannelSnr<searchData.downSnrMax){
				downChSnrFlag=1;
			    }
			}
		}else if(searchData.downSnrMin!=''&&searchData.downSnrMax==''){
			if(downChannelSnr.length>0){
			    if(getDownChannelSnr>=searchData.downSnrMin){
				downChSnrFlag=1;
			    }
			}
		}else if(searchData.downSnrMin==''&&searchData.downSnrMax==''){
			downChSnrFlag=1;
		}
		//条件3判断
		if(searchData.upPowerMin!=''&&searchData.upPowerMax!=''){
			if(upChannelTx.length>0){
				if(getUpChannelTx>=searchData.upPowerMin&&getUpChannelTx<searchData.upPowerMax){
					upChTxFlag=1;
				}
			}					
		}else if(searchData.upPowerMin==''&&searchData.upPowerMax!=''){
			if(upChannelTx.length>0){
			    if(getUpChannelTx<searchData.upPowerMax){
				upChTxFlag=1;
			    }
			}
		}else if(searchData.upPowerMin!=''&&searchData.upPowerMax==''){
			if(upChannelTx.length>0){
			    if(getUpChannelTx>=searchData.upPowerMin){
				upChTxFlag=1;
			    }
			}
		}else if(searchData.upPowerMin==''&&searchData.upPowerMax==''){
			upChTxFlag=1;
		}
		//条件4判断
		if(searchData.downPowerMin!=''&&searchData.downPowerMax!=''){
			if(downChannelRx.length>0){
				if(getDownChannelRx>=searchData.downPowerMin&&getDownChannelRx<searchData.downPowerMax){
					downChTxFlag=1;
				}
			}		
		}else if(searchData.downPowerMin==''&&searchData.downPowerMax!=''){
			if(downChannelRx.length>0){
			    if(getDownChannelRx<searchData.downPowerMax){
				downChTxFlag=1;
			    }
			}
		}else if(searchData.downPowerMin!=''&&searchData.downPowerMax==''){
			if(downChannelRx.length>0){
			    if(getDownChannelRx>=searchData.downPowerMin){
				downChTxFlag=1;
			    }
			}
		}else if(searchData.downPowerMin==''&&searchData.downPowerMax==''){
			downChTxFlag=1;
		}
		if(upChTxFlag==1&&downChTxFlag==1&&upChSnrFlag==1&&downChSnrFlag==1){			
			continue;
		}else{
		    store.remove(store.getAt(i--));		  
		    count--; 
		}	  		  
		}
	}
}


//------------------------
//------上行信道解析-------
//------------------------
function renderUpChannel(value,p,record){
    var docsIf3CmtsCmUsStatusList = record.data.docsIf3CmtsCmUsStatusList;
    var upChannelId = record.data.upChannelId;
    var snr = record.data.docsIfCmtsCmStatusSignalNoiseString;
    var imgString = "";
    if(docsIf3CmtsCmUsStatusList && docsIf3CmtsCmUsStatusList.length>0){
        for(var i=0;i<docsIf3CmtsCmUsStatusList.length;i++){
            if(docsIf3CmtsCmUsStatusList[i].upChannelId == 1){
                imgString = imgString + "<img src='/images/channelImage/1.gif' />";
            }else if(docsIf3CmtsCmUsStatusList[i].upChannelId == 2){
                imgString = imgString + "<img src='/images/channelImage/2.gif' />";
            }else if(docsIf3CmtsCmUsStatusList[i].upChannelId == 3){
                imgString = imgString + "<img src='/images/channelImage/3.gif' />";
            }else if(docsIf3CmtsCmUsStatusList[i].upChannelId == 4){
                imgString = imgString + "<img src='/images/channelImage/4.gif' />";
            }
        }
    }else{
        if(upChannelId == 1){
            imgString = "<img src='/images/channelImage/1.gif'/>";
        }else if(upChannelId == 2){
            imgString = "<img src='/images/channelImage/2.gif'/>";
        }else if(upChannelId == 3){
            imgString = "<img src='/images/channelImage/3.gif'/>";
        }else if(upChannelId == 4){
            imgString = "<img src='/images/channelImage/4.gif'/>";
        }
    }
    return imgString;
}

//------------------------
//------下行信道解析-------
//------------------------
function renderDownChannel(value,p,record){
    var downChannelId = record.data.downChannelId;
    var imgString = "";
    for(var i = 1;i <= 16; i++){
        if(downChannelId==i){
            imgString = "<img src='/images/channelImage/"+ i +".gif'/>";
            break;
        }
    }
    return imgString;
}

function renderRxPower(value,p,record){
	var rp=record.data.cmRxPower;
	if(rp.length==0){
		return '-';
	}else{
		return rp/10;
	}
}

function renderTxPower(value,p,record){
	var tp=record.data.cmTxPower;
	if(tp.length==0){
		return '-';
	}else{
		return tp/10;
	}
}

function renderDCSnr(value,p,record){
	var dcs=record.data.cmSignalNoise;
	if(dcs.length==0){
		return '-';
	}else{
		return dcs/10;
	}
}

function renderUCSnr(value,p,record){
	var ucs=record.data.statusSignalNoise;
	if(ucs.length==0){
		return '-';
	}else{
		return ucs/10;
	}
}
//----------------------------------
//------下行信道信噪比多信道解析-------
//----------------------------------
function renderDownChannelSnr(value,p,record){
	var downSnrArray = record.data.downChannelSnr;
	var downSnrString = "";
	if(downSnrArray.length == 0){
		return "<label class='showBubbleTip second'>-</label>";
	}
	for(var i = 0; i < downSnrArray.length; i++){
		var snr = downSnrArray[i];
		if(snr < 22){
			downSnrString += '<span class="red">'+snr+'</span>';
		}else if(snr < 28){
			downSnrString += '<span class="yellow">'+snr+'</span>';
		}else{
			downSnrString += '<span class="green">'+snr+'</span>';
		}
		if(i < downSnrArray.length - 1){
			downSnrString += '<span>'+"/"+'</span>';
		}
	}
	return '<label class="showBubbleTip second">' +downSnrString + '</label>';
}

//----------------------------------
//------下行信道接收电平多信道解析-----
//----------------------------------
function renderDownChannelRx(value,p,record){
	var downTxArray = record.data.downChannelRx;
	var downTxString = "";
	if(downTxArray.length == 0){
		return "<label class='showBubbleTip fourth'>-</label>";
	}
	if(sysElecUnit=='dBmV'){
		for(var i = 0; i < downTxArray.length; i++){
			var tx = downTxArray[i];

			if(tx < -15){
				downTxString += '<span class="red">'+tx+'</span>';
			}else if(tx < -10){
				downTxString += '<span class="yellow">'+tx+'</span>';
			}else if(tx < 10){
				downTxString += '<span class="green">'+tx+'</span>';
			}else if(tx < 15){
				downTxString += '<span class="yellow">'+tx+'</span>';
			}else{
				downTxString += '<span class="red">'+tx+'</span>';
			}
			if(i <  downTxArray.length - 1){
				downTxString += '<span>'+"/"+'</span>';
			}
		}
	}else{
		for(var i = 0; i < downTxArray.length; i++){
			var tx = downTxArray[i];

			if(tx < 45){
				downTxString += '<span class="red">'+tx+'</span>';
			}else if(tx < 50){
				downTxString += '<span class="yellow">'+tx+'</span>';
			}else if(tx < 70){
				downTxString += '<span class="green">'+tx+'</span>';
			}else if(tx < 75){
				downTxString += '<span class="yellow">'+tx+'</span>';
			}else{
				downTxString += '<span class="red">'+tx+'</span>';
			}
			if(i <  downTxArray.length - 1){
				downTxString += '<span>'+"/"+'</span>';
			}
		}
	}

	return '<label class="showBubbleTip fourth">' + downTxString + '</label>';
}

//----------------------------------
//------上行信道发送电平比多信道解析---
//----------------------------------
function rendererUpChannelTx(value,p,record){
	var upTxArray = record.data.upChannelTx;
	var upTxString = "";
	if(upTxArray.length == 0){
		return "<label class='showBubbleTip third'>-</label>";
	}
	if(sysElecUnit=='dBmV'){
		for(var i = 0; i < upTxArray.length; i++){
			var tx = upTxArray[i];
			if(tx < 30){
				upTxString += '<span class="red">'+tx+'</span>';
			}else if(tx < 38){
				upTxString += '<span class="yellow">'+tx+'</span>';
			}else if(tx < 48){
				upTxString += '<span class="green">'+tx+'</span>';
			}else if(tx < 52){
				upTxString += '<span class="yellow">'+tx+'</span>';
			}else{
				upTxString += '<span class="red">'+tx+'</span>';
			}
			if(i < upTxArray.length - 1){
				upTxString += '<span>'+"/"+'</span>';
			}
		}
	}else{
		for(var i = 0; i < upTxArray.length; i++){
			var tx = upTxArray[i];
			if(tx < 90){
				upTxString += '<span class="red">'+tx+'</span>';
			}else if(tx < 98){
				upTxString += '<span class="yellow">'+tx+'</span>';
			}else if(tx < 108){
				upTxString += '<span class="green">'+tx+'</span>';
			}else if(tx < 112){
				upTxString += '<span class="yellow">'+tx+'</span>';
			}else{
				upTxString += '<span class="red">'+tx+'</span>';
			}
			if(i < upTxArray.length - 1){
				upTxString += '<span>'+"/"+'</span>';
			}
		}
	}

	return '<label class="showBubbleTip third">' + upTxString + '</label>';
}

//----------------------------------
//------上行信道信噪比多信道解析-------
//----------------------------------
function renderUpChannelSnr(value,p,record){
	var upSnrArray = record.data.upChannelSnr;
	var upSnrString = "";
	if(upSnrArray.length == 0){
		return "<label class='showBubbleTip first'>-</label>";
	}
	for(var i = 0; i < upSnrArray.length; i++){
		var snr = upSnrArray[i];
		if(snr < 22){
			upSnrString += '<span class="red">'+snr+'</span>';
		}else if(snr < 28){
			upSnrString += '<span class="yellow">'+snr+'</span>';
		}else{
			upSnrString += '<span class="green">'+snr+'</span>';
		}
		if(i < upSnrArray.length - 1){
			upSnrString += '<span>'+"/"+'</span>';
		}
	}
	return '<label class="showBubbleTip first">' + upSnrString + '</label>';
}

//----------------------------------
//------------CM状态解析-------------
//----------------------------------
function renderStatusValue(value,p,record){
	switch(value){
	case 1:
		return '<span>offline</span>';
	case 2:
		return  '<span>init(r2)</span>';
	case 5:
		return '<span>init(i)</span>';
	case 6:
		return '<span>online</span>';
	case 10:
		return '<span>init(d)</span>';
	case 11:
		return '<span>init(io)</span>';
	case 21:
		return '<span>online(d)</span>';
	case 22:
		return '<span>init6(s)</span>';
	case 23:
		return '<span>init6(a)</span>';
	case 25:
		return '<span>init6(i)</span>';
	case 26:
		return '<span>p-online</span>';
	case 30:
		return '<span>p-online(d)</span>';
	case 27:
		return '<span>w-online</span>';
	case 31:
		return '<span>w-online(d)</span>';
	case null:
		return '<span>-</span>';
	case '':
		return '<span>-</span>';
	default:
		return '<span>' + I18N.TIP.other + '</span>';	}
}

function doRefresh(){
	lastRow = null;
	store.reload();
}
