<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	library Highchart
	plugin DateTimeField
    module performance
    CSS    performance/css/performanceGlobalConfig
    CSS    performance/css/perTargetManageCommon
    IMPORT js/entityType
    IMPORT performance/nm3kBatchData
    IMPORT performance/js/perfTargetManageCommon
</Zeta:Loader>
</head>
<body class="whiteToBlack">
	<div id="topPart">
		<div id="toolBar"></div>
		<div class="edge10">
			<table class="topSearchTable" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td class="rightBlueTxt">@Performance.deviceName@:</td>
						<td width="146">
							<input class="normalInput w130" type="text" id="deviceName"/> 
						</td>
						<td class="rightBlueTxt">@resources/COMMON.uplinkDevice@:</td>
						<td width="146">
							<input class="normalInput w130" type="text" id="deviceIp"/> 
						</td>
						<td class="rightBlueTxt">@Performance.deviceType@:</td>
						<td width="155">
							<select id="deviceType" style="width: 150px;" class="normalSel">
								<option value="-1">@Tip.select@</option>
							</select>
						</td>
						<td class="rightBlueTxt">@Performance.area@:</td>
						<td width="146">
							<!-- <input class="normalInput w130" type="text" id="deviceArea"/> -->
							<select id="deviceArea" style="width: 132px;" class="normalSel">
								<option value="-1">@Tip.select@</option>
							</select>
						</td>
						<td rowspan="2">
							<ol class="upChannelListOl pB0 pT0">
								<li>
									<a href="javascript:;" class="normalBtn" onclick="queryClick()">
							 			<span><i class="miniIcoSearch"></i>@Tip.query@</span>
							 		</a>
								</li>
							</ol>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!-- 编辑部分DOM -->
	<div id="modifyDiv" class="displayNone">
		<div id="modifyToolBar"></div>
		<div id="modifyMainPart"></div>
	</div>
	<!-- 第三步，对比部分DOM -->
	<div id="contrastDiv" class="displayNone">
		<div id="step3ToolBar"></div>
		<div id="step3Body"></div>
	</div>
	<div id="tipsWin"></div>
	
	
<!-- <script type="text/javascript" src="../nm3kBatchData.js"></script> -->
<script type="text/javascript">
	Ext.QuickTips.init();
	var pageSize = <%= uc.getPageSize() %>;
	var entityId = '${entityId}';
	//模块支持情况,在单独安装CC模块的时候使用
	var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
	var eponSupport = <%= uc.hasSupportModule("olt")%>;
	
	var flagObj = {
		gData : null, //用来保存后台获取的数据，主要是保存全局数据;
		step2 : false //第二层是否弹出,如果没有弹出或没有加载好数据，那么上面的按钮点击就不会生效;
	}
	var h = document.documentElement.clientHeight-100;
	var w = document.documentElement.clientWidth-40;
	
	var	TYPE_OPTION_FMT = '<option value="{0}">{1}</option>';
	function initData() {
		//初始化查询条件
		$.ajax({
			url: '/performance/loadAllArea.tv',
			cache:false,
			dataType:'json',
			success: function(result) {
				var allArea = result;
				for (var i = 0, len = allArea.length; i < len; i++) {
					$('#deviceArea').append(String.format(TYPE_OPTION_FMT, allArea[i].folderId, allArea[i].name));
				}
			}
		});
	}
	initData();
	
	//构造设备类型下拉框
	var deviceTypePosition = Zeta$('deviceType');
	var ccAndCmtsType = EntityType.getCCMTSAndCMTSType();
	$.ajax({
	      url:'/entity/loadSubEntityType.tv?type=' + ccAndCmtsType,
	      type:'POST',
	      dateType:'json',
	      success:function(response) {
	    	  var entityTypes = response.entityTypes; 
	    	  if(cmcSupport && !eponSupport){
	    		  for(var i = 0; i < entityTypes.length; i++){
	    			  if(EntityType.isCcmtsWithAgentType(entityTypes[i].typeId) || EntityType.isCmtsType(entityTypes[i].typeId)){
	    				  	var option = document.createElement('option');
				            option.value = entityTypes[i].typeId;
				            option.text = entityTypes[i].displayName;
				            try {
				            	deviceTypePosition.add(option, null);
				            } catch(ex) {
				            	deviceTypePosition.add(option);
				            }
	    			  }
		    	  }
	    	  }else{
	    		  for(var i = 0; i < entityTypes.length; i++){
			            var option = document.createElement('option');
			            option.value = entityTypes[i].typeId;
			            option.text = entityTypes[i].displayName;
			            try {
			            	deviceTypePosition.add(option, null);
			            } catch(ex) {
			            	deviceTypePosition.add(option);
			            }
		    	  } 
	    	  }
	      },
	      error:function(response) {},
	      cache:false
	 });
	  
	// 创建顶部工具栏
	function createTopToolbar(){
		new Ext.Toolbar({
	        renderTo: "toolBar",
	        items: [
	 	           	{text: '@Tip.allPerf@', cls:'mL10', iconCls:'bmenu_view', handler: fChangeColumn, id:"changeColumnBtn"},"-",
	 	          	{text: '@Tip.refresh@', iconCls: 'bmenu_refresh', handler: fRefresh}
	 	           ]
	    });
	};
	//刷新grid
	function fRefresh(){
		//在执行完相关操作后去掉grid表头上的复选框选中状态
		//grid.getEl().select('div.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
		//window.location.href = window.location.href;
		store.reload();
	}
	//显示所有指标或默认指标;
	function fChangeColumn(){
		if($(".x-grid3-scroller").length == 1){
			$(".x-grid3-scroller").scrollLeft(0);
		}
		var btn = Ext.getCmp("changeColumnBtn"),
		txt = btn.text;
		
		switch(txt){
			case '@Tip.allPerf@':
				btn.setText('@Tip.defaultPerf@');
				btn.setIconClass('bmenu_arrange');
				allColumn();
				grid.getView().forceFit = false;
			break;
			case '@Tip.defaultPerf@':
				btn.setText('@Tip.allPerf@');
				btn.setIconClass('bmenu_view');
				defaultColumn();
				grid.getView().forceFit = true;
			break;
		}
		grid.getView().refresh();
		grid.reconfigure(store, cm);
		grid.doLayout();
	}
	function allColumn(){
		cm = new Ext.grid.ColumnModel([
       	 	{header:'@Performance.deviceId@', dataIndex: 'entityId', align:"center",  hidden: true},
       	 	{header:'<div class="txtCenter">@Performance.deviceName@</div>', dataIndex: 'deviceName', align:"left", width:150, renderer: deviceNameRender},
 			{header:'<div class="txtCenter">@resources/COMMON.uplinkDevice@</div>', dataIndex: 'uplinkDevice', align:"left", width:120, renderer: ipRender},
 			{header:'@COMMON.type@', dataIndex: 'displayName', align:"center", width:120, menuDisabled:true}, 			
 			{header:'<div class="txtCenter">@Performance.area@</div>', dataIndex: 'location', align:"left", width:150, renderer : areaRender},
	  		{header:'@Performance.deviceRelay@', dataIndex: 'cmc_onlineStatus', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.cpuUsed@', dataIndex: 'cmc_cpuUsed', align:"center",width:90,renderer: collectRender},
	  		{header:'@Performance.memUsed@', dataIndex: 'cmc_memUsed', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.flashUsed@', dataIndex: 'cmc_flashUsed', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.moduleTemp@', dataIndex: 'cmc_moduleTemp', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.optLink@', dataIndex: 'cmc_optLink', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.ber@', dataIndex: 'cmc_ber', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.snr@', dataIndex: 'cmc_snr', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.upLinkFlow@', dataIndex: 'cmc_upLinkFlow', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.macFlow@', dataIndex: 'cmc_macFlow', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.channelSpeed@', dataIndex: 'cmc_channelSpeed', align:"center", width:90,renderer: collectRender},
	  		{header:'FLAP', dataIndex: 'cmc_cmflap', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.opticalReceiver@', dataIndex: 'cmc_opticalReceiver', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.dorTemp@', dataIndex: 'cmc_dorOptTemp', align:"center", width:90,renderer: collectRender},
	  		{header:'@Performance.dorVoltage@', dataIndex: 'cmc_dorLinePower', align:"center", width:90,renderer: collectRender},
	  		{header:'@Tip.operator@', dataIndex: 'operator', align:"center",renderer: operatorRender,resizable: false,width : 120,fixed : true}
       	]);
	};
	 
	function defaultColumn(){
		cm = new Ext.grid.ColumnModel([
     	 	{header:'@Performance.deviceId@', dataIndex: 'entityId', align:"center",resizable: false,  hidden: true},
     	 	{header:'<div class="txtCenter">@Performance.deviceName@</div>', dataIndex: 'deviceName', align:"left", width:150, renderer: deviceNameRender},
    		{header:'<div class="txtCenter">@resources/COMMON.uplinkDevice@</div>', dataIndex: 'uplinkDevice', align:"left", width:120, renderer: ipRender},
    		{header:'@COMMON.type@', dataIndex: 'displayName', align:"center", width:120, menuDisabled:true},
    		{header:'<div class="txtCenter">@Performance.area@</div>', dataIndex: 'location', align:"left", width:150, renderer : areaRender},
	  		{header:'@Performance.deviceRelay@', dataIndex: 'cmc_onlineStatus', align:"center", width:90,renderer: collectRender,resizable: false},
	  		{header:'@Performance.cpuUsed@', dataIndex: 'cmc_cpuUsed', align:"center",width:90,renderer: collectRender,resizable: false},
	  		{header:'@Performance.optLink@', dataIndex: 'cmc_optLink', align:"center", width:90,renderer: collectRender,resizable: false},
	  		{header:'@Performance.snr@', dataIndex: 'cmc_snr', align:"center", width:90,renderer: collectRender,resizable: false},
	  		{header:'@Performance.channelSpeed@', dataIndex: 'cmc_channelSpeed', align:"center", width:90,renderer: collectRender,resizable: false},
	  		{header:'@Tip.operator@', dataIndex: 'operator', align:"center",renderer: operatorRender,resizable: false,width : 120,fixed : true}
     	]);
	}
	createTopToolbar();
	createModifyTopToolbar();
	autoHeight();
	
	$(window).resize(function(){
		throttle(autoHeight,window);
	});//end resize;
	
	function areaRender(value,metadata,record){
		metadata.attr = 'ext:qtip="' + value +'"';
		return value;
	}
	
	function showEntityPerf(cmcId,entityName, entityType){
		var ccType = EntityType.getCcmtsType();
		var cmtsType = EntityType.getCmtsType();
		var name = unescape(entityName);
		if(entityType == ccType){  //cmc性能曲线
			window.parent.addView('entity-' + cmcId, name , 'entityTabIcon','/cmcPerfGraph/showCmcCurPerf.tv?module=13&cmcId=' + cmcId, null, true);
		}else if(entityType == cmtsType){ //cmts性能曲线
			window.parent.addView('entity-' + cmcId, name , 'entityTabIcon','/cmts/showCmtsCurPerf.tv?module=8&cmcId='+cmcId, null, true);
		}
	}

	function deviceNameRender(value, p, record){
		var deviceName = escape(record.data.deviceName);
		var formatStr = '<a href="javascript:;" onclick="showEntityPerf({0},\'{1}\',{2})">{3}</a>';
		return String.format(formatStr, record.data.entityId, deviceName,record.data.entityType, record.data.deviceName);
	}

	function ipRender(value, p, record){
		var typeId = record.data.typeId;
		if(EntityType.isCcmtsWithoutAgentType(typeId)){
			var parentName = escape(record.data.parentName);
			var parentId = record.data.parentId;
			var parentTypeId = record.data.parentTypeId;
			var formatStr = '<a href="javascript:;" onclick="showEntityDetail({0},\'{1}\',{2})">{3}</a>';
			return String.format(formatStr, parentId, parentName, parentTypeId, value);
		}else{
			var deviceName = escape(record.data.deviceName);
			var formatStr = '<a href="javascript:;" onclick="showEntityPerf({0},\'{1}\',{2})">{3}</a>';
			return String.format(formatStr, record.data.entityId, deviceName, record.data.entityType, value);
		}
	}
	
	function showEntityDetail(entityId, entityName, typeId){
		var name = unescape(entityName);
		if(EntityType.isStandardOlt(typeId)){
			window.parent.addView('entity-' + entityId, name , 'entityTabIcon','/epon/standardOlt/showStandardOltPerfView.tv?module=7&entityId=' + entityId, null, true);
		}else{
			window.parent.addView('entity-' + entityId, name , 'entityTabIcon','/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=7&entityId=' + entityId, null, true);
		}
	}
	
	function collectRender(v, p, record){
		if(v.targetEnable == 1){
			return v.collectInterval;
		}else if(v.targetEnable == 0){
			return str = '<img src="/images/performance/off2.png">';
		}else{
			//指标不支持的情况
			return "--";
		}
	}
	
	function operatorRender(value, p, record){
		var entityType = record.data.entityType;
		var entityId = record.data.entityId;
		var typeId = record.data.typeId;
		var displayName = record.data.displayName;
		var formatStr = "--";
		if(typeId != -1){
			formatStr = '<a href="javascript:;" onclick="showModify({0},{1},{2},\'{3}\')">@Tip.edit@</a>';
			return String.format(formatStr, entityType, entityId, typeId, displayName);
		}else{
			formatStr = "<a href='javascript:;' onclick='useCmtsGolbalPerf({0},{1})' style='margin-right:10px'>@Performance.resetConfig@</a>";
			return String.format(formatStr,entityId,entityType);
		}
	}
	
	var sm, cm;
	defaultColumn()
	
	var reader =  new Ext.data.JsonReader({
	    totalProperty: "totalCount",
	    idProperty: "entityId",
	    root: "data",
	    fields:['entityId', 
                'deviceName', 
                "entityType",
                "typeId",
                'manageIp', 
                'uplinkDevice',
                'location',
                'cmc_cpuUsed', 
                'cmc_memUsed', 
                'cmc_flashUsed', 
                'cmc_moduleTemp', 
                'cmc_macFlow', 
                'cmc_optLink', 
                'cmc_upLinkFlow', 
                'cmc_channelSpeed', 
                'cmc_opticalReceiver',
                'cmc_dorOptTemp',
                'cmc_dorLinePower',
                'cmc_cmflap',
                'cmc_snr',
                'cmc_ber',
                'cmc_onlineStatus', 
                'displayName',
                'parentId',
                'parentName',
                'parentTypeId'
                ]
	});
	
	//生成store
	window.store = new Ext.data.GroupingStore({
		url: '/cmc/perfTarget/loadCmcTargetList.tv',
		reader: reader,
        remoteGroup: true,
		groupField: 'displayName',
		groupOnSort: false,
		remoteSort :true
	});
	
	store.load({params: {start: 0, limit: pageSize, entityId: entityId}});
	
	var groupTpl = '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "@COMMON.items@" : "@COMMON.items@"]})';
	var bbar = new Ext.PagingToolbar({
		pageSize: pageSize,
		cls: 'extPagingBar',
		store: store,
		displayInfo: true,
		displayMsg: '@Tip.paginateMsg@',
		emptyMsg: "@Tip.noRecord@"
	});
	
	var grid = new Ext.grid.GridPanel({
		region: "center",
		title: "@Tip.gridTitle@",
		view: new Ext.grid.GroupingView({
            forceFit: true, hideGroupedColumn: false,enableNoGroups: true,groupTextTpl: groupTpl
        }),
		bbar: bbar,
		cls: 'normalTable',
		store: store,
		cm : cm,
		viewConfig:{
			forceFit: true
		}
	});
	var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [grid,
	         {region:'north',
	         height: 86,
	         contentEl :'topPart',
	         border: false,
	         cls:'clear-x-panel-body',
	         autoScroll: true
	     }]
	}); 
	
	function queryClick(){
		var deviceName = $('#deviceName').val().trim();
		var deviceIp = $("#deviceIp").val().trim();
		var folderId = $("#deviceArea").val();
		var deviceType = $("#deviceType").val();
		
		store.baseParams={
	   		start: 0,
			limit: pageSize,
			deviceName: deviceName,
			deviceIp: deviceIp,
			folderId: folderId,
			typeId: deviceType
	    };
		store.load({
			callback: function(records) {
				if (records && records.length == 0){
					top.nm3kAlertTips({
	                    title:"@COMMON.tip@",
	                    html:"<b class='orangeTxt'>" + "@Tip.withoutEq@" + "</b>",
	                    okBtnTxt:"@COMMON.OK@"
	                });
				}else{
					top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
				}
			}
		});
	
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                               //
	//                                             编辑部分                                                                                                                 //
	//                                                                                               //
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//显示编辑div;
	var deviceEntityId;
	var deviceType;
	var deviceTypeId;
	var currentSubType;
	function showModify(type, entityId, typeId, displayName){
		$("#modifyDiv").removeClass("displayNone");
		deviceEntityId = entityId;
		deviceType = type;
		deviceTypeId = typeId;
		currentSubType = displayName;
		setEqTypeBtnName(displayName);
		createNewTypePage(entityId);
	}
	
	//设置正确的按钮名称
	function setEqTypeBtnName(displayName){
		var btnName = "@Tip.applyToAll@" + displayName;
		$("#useToAll .x-btn-mc").find("button").text(btnName);
	};
	//根据类型生成不同的页面;
	function createNewTypePage(entityId){
		var $modifyMainPart = $("#modifyMainPart");
		$modifyMainPart.empty();
		
		var dataStore;
		$.ajax({
			url: '/cmc/perfTarget/loadCmcPerfTarget.tv',
	    	type: 'POST',
	    	data: {
	    		entityId : entityId
	    	},
	    	dataType:"json",
	   		success: function(result) {
	   			dataStore = result;
	   			flagObj.gData = dataStore;//存储数据;
	   			flagObj.step2 = true;//数据加载好，标记变成true;
	   			
	   			var str = createJsonTable(dataStore);
	   			$modifyMainPart.html(str);
	   		}, error: function(result) {
			}, 
			//async : false,
			cache: false
		});
		
	};//end function;
	
	//从后台同步全局数据
	function getRecentlyNewData(){
		$.ajax({
			url: '/cmc/perfTarget/loadCmcPerfTarget.tv',
	    	type: 'POST',
	    	data: {
	    		entityId : deviceEntityId
	    	},
	    	dataType:"json",
	   		success: function(result) {
	   			//更新页面性能指标数据
	   			flagObj.gData = result;
	   		}, error: function(result) {
			}, 
			async : false,
			cache: false
		});
	}
	
	// 创建修改页面的顶部工具栏;	
	function createModifyTopToolbar(){
		new Ext.Toolbar({
	        renderTo: "modifyToolBar",
	        items: [
	           {text: '@Tip.back@', iconCls: 'bmenu_back',cls:'mL10', handler: backFn},'-',
	           {text: '@Tip.save@', iconCls: 'bmenu_data', handler: saveModifyOneCmcPerf},
	           "-",
	           {text: '@Tip.applyToAll@',id:'useToAll', iconCls: 'bmenu_save', handler: showApplyToAllCmc},
	           {text: '@Tip.saveAsGlobalConfig@', iconCls: 'bmenu_data', handler: saveAsGolbal},
	           {text: '@Tip.useGolbalConfig@', iconCls: 'bmenu_arrange', handler: useCmcGolbalData},
	           "-",
	           createSetTimeButton()
	           ]
	    });
	}
	
	//保存;
	function saveModifyOneCmcPerf(){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		
		var reg = testAllInput();//验证;
		if(!reg){return};
		
		var data = getAllSetData();
		//alert(data)
		//后台不需要json对象，需要字符串,因此将json解析成包含字符串的数组;
		var targetArray = [];
		for(var key in data){
			var innerData = data[key];
			for(var key2 in innerData){
				var tempStr = key2 + "#";
				tempStr += innerData[key2].collectInterval + "#";
				tempStr += innerData[key2].targetEnable + "#";
				tempStr += key;
				targetArray.push(tempStr)
			}
		}
		top.showWaitingDlg("@Tip.ing@");
		$.ajax({
			url: '/cmc/perfTarget/modifyCmcPerfTarget.tv',
	    	type: 'POST',
	    	data: {
	    		entityId : deviceEntityId,
	    		entityType : deviceType,
	    	    typeId : deviceTypeId,
	    		targetData : targetArray
	    	},
	    	dataType:"json",
	   		success: function(result) {
	   			top.closeWaitingDlg();
	   			//提示框
	   			top.afterSaveOrDelete({
					title : "@COMMON.tip@",
					html : '<p><b class="orangeTxt">@Tip.modifySuccess@</b></p>'
				})
				$("#modifyDiv").addClass("displayNone");
				flagObj.step2 = false;
				queryClick();
	   		}, error: function(result) {
	   			top.closeWaitingDlg();
			}, 
			//async : false,
			cache: false
		});
	}
	//应用到所有同类型;
	function showApplyToAllCmc(){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		
		var reg = testAllInput();//验证;
		if(!reg){return};
		
		var $contrastDiv = $("#contrastDiv");
		$contrastDiv.removeClass("displayNone");
		new Ext.Toolbar({
	        renderTo: "step3ToolBar",
	        id:"applyAllToolBar",
	        items: [
				{text: '@Tip.back@', iconCls: 'bmenu_back',cls:'mL10', handler: backStep3ApplyAll},'-',
				{text: '@Tip.apply@', iconCls: 'bmenu_save', handler: applyAllEqFn},'-',
				{text: '@Tip.HightLight@', iconCls: 'bmenu_eyes', handler: showDifferent}
			]
		});//end ToolBar;
		getRecentlyNewData();
		var thArr = ["@Tip.pleaseSelected@","@Performance.targetName@","@Tip.afterUse@","@Tip.globalConfig@"];
		createContrastTable(thArr);
		autoHeight();
		$("#step3Body").bind("click",this,clickApplyAll);
	}
	//保存为全局配置;
	function saveAsGolbal(){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		var reg = testAllInput();//验证;
		if(!reg){return};
		
		var $contrastDiv = $("#contrastDiv");
		$contrastDiv.removeClass("displayNone");
		new Ext.Toolbar({
	        renderTo: "step3ToolBar",
	        id:"applyAllToolBar",//这个id不要修改,后退时都是清除同一个id的toolBar;
	        items: [
				{text: '@Tip.back@', iconCls: 'bmenu_back',cls:'mL10', handler: backStep3ApplyAll},'-',
				{text: '@Tip.save@', iconCls: 'bmenu_data', handler: saveAsGobalTip}
				//,'-',{text: 高亮显示不同项, iconCls: 'bmenu_eyes', handler: showDifferent}
			]
		});//end ToolBar;
		getRecentlyNewData();
		var thArr = ["@Tip.saveGlobal@","@Performance.targetName@","@Tip.afterSave@","@Tip.beforeSave@"];
		createContrastTable(thArr);
		autoHeight();
		//如果和全局相同,则没有必要选中,默认高亮显示;
		hideSameColumn();
		$("#step3Body").bind("click",this,clickApplyAll);
	}
	//使用全局配置;
	function useCmcGolbalData(){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		
		var $contrastDiv = $("#contrastDiv");
		$contrastDiv.removeClass("displayNone");
		new Ext.Toolbar({
	        renderTo: "step3ToolBar",
	        id:"applyAllToolBar",//这个id不要修改,后退时都是清除同一个id的toolBar;
	        items: [
				{text: '@Tip.back@', iconCls: 'bmenu_back',cls:'mL10', handler: backStep3ApplyAll},'-',
				{text: '@Tip.use@', iconCls: 'bmenu_edit', handler: inputGlobalData}
				,'-',{text: '@Tip.HightLight@', iconCls: 'bmenu_eyes', handler: showDifferent}
			]
		});//end ToolBar;
		getRecentlyNewData();
		var thArr = ["@Tip.useGolbal@","@Performance.targetName@","@Tip.afterUse@","@Tip.beforeUse@"];
		createGlobalTable(thArr);
		autoHeight();
		$("#step3Body").bind("click",this,clickApplyAll);
	}
	
	$("#modifyMainPart").click(function(e){
		var $me = $(e.target);
		if($me.hasClass("scrollBtn")){//点击滑动按钮;
			changeOnOff($me);
		}
		if($me.hasClass("blueArr")){
			openOrCloseAll($me,"on");
		}
		if($me.hasClass("redArr")){
			openOrCloseAll($me,"off");
		}
		if($me.hasClass("thTit")){
			var $tbody = $me.parent().parent().parent().next();
			if($me.hasClass("openThTit")){//点击的是减号;
				$me.removeClass("openThTit").addClass("closeThTit");
				$tbody.css({display : "none"});
			}else if($me.hasClass("closeThTit")){
				$me.removeClass("closeThTit").addClass("openThTit");
				$tbody.css({display : ""});
			}
		}
	});//end click;
	
	
	function useCmtsGolbalPerf(entityId,entityType){
		top.showWaitingDlg("@Tip.ing@");
		var urlStr = '';
		if(EntityType.getCmtsType() == entityType){
			urlStr = '/cmts/perfTarget/useCmtsGolbalPerf.tv';
			alert('xx');
		}else{
			urlStr = '/cmc/perfTarget/useCmcGolbalPerf.tv';
		}
		$.ajax({
			url: urlStr,
	    	type: 'POST',
	    	data: {
	    		entityId : entityId
	    	},
	    	dataType:"json",
	   		success: function(result) {
	   			top.closeWaitingDlg();
	   			//提示框
	   			top.afterSaveOrDelete({
					title : "@COMMON.tip@",
					html : '<p><b class="orangeTxt">@Tip.modifySuccess@</b></p>'
				});
	   			doRefresh();
	   		}, error: function(result) {
	   			top.closeWaitingDlg();
			}, 
			cache: false
		});
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                               //
	//                                             第三部分                                                                                                                 //
	//                                                                                               //
	///////////////////////////////////////////////////////////////////////////////////////////////////	
	//保存应用到所有操作
	function applyToAllDevice(targetArray, saveGlobalFlag){
		top.showWaitingDlg("@Tip.ing@");
		$.ajax({
			url: '/cmc/perfTarget/applyCurrentTargetToAllCmc.tv',
	    	type: 'POST',
	    	data: {
	    		entityId : deviceEntityId,
	    		entityType : deviceType,
	    		typeId : deviceTypeId,
	    		targetData : targetArray,
	    		saveGlobalFlag : saveGlobalFlag
	    	},
	    	dataType:"json",
	   		success: function(result) {
	   			top.closeWaitingDlg();
	   			//result中包括成功个数,成功详细,失败个数,失败详细
	   			var o = {
	   				succeedNum : result.succeedNum,
	   				failedNum : result.failedNum,
	   				succeedDetail : result.succeedDetail,
	   				failedDetail : result.failedDetail
	   			};
	   			var str = createResultTable(o);    
	   			top.$("#applyAllTipWin").find("#tipsWin").html(str);
	   			top.afterSaveOrDelete({
					title : "@COMMON.tip@",
					html : String.format('<p><b class="orangeTxt">@Tip.applyCMCSuccess@</b></p>', currentSubType)
				})
				doRefresh();
	   		}, error: function(result) {
	   			top.closeWaitingDlg();
			}, 
			//async : false,
			cache: false
		});					
	}
	//应用到所有;
	function applyAllEqFn(){
		var arr = [];
		var newTips =String.format("@Tip.willModify@","CMC");
		var tips = '<div class="edge10"><p class="blueBordTxt pB10">'+ newTips +'</p>';
		tips += '<div style="height:320px; overflow:auto;"><table class="contrastTable noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">';
		tips += '<thead><tr><th>@Performance.targetName@</th><th>@Tip.CollectionTime@</th><th>@Tip.isOpen@</th></tr></thead><tbody>'
		$(".contrastTable tbody").find(".selectedOne").each(function(){
			var $me = $(this);
			if( $me.attr("checked") == true || $me.attr("checked") == "checked" ){
				arr.push($me.attr("name"));
				var altArr = $me.attr("alt").split("#"); 
				tips += '<tr>';
				tips +=     '<td class="pL5">';
				tips +=         altArr[0] + ' &gt; ' + altArr[1]; 
				tips +=     '</td>';
				tips +=     '<td class="txtCenter">';
				tips +=         altArr[2];
				tips +=     '</td>';
				tips +=     '<td class="txtCenter">';
				var num = Number(altArr[3]);
				num = renderNumToImg(num);
				tips +=         num;
				tips +=     '</td>';
				tips += '</tr>';
			} 
		})
		tips += '</tbody></table></div>';
		tips += '<p class="pT10"><b class="orangeTxt">@Tip.clickTip@</b></p>';
		tips += '    <div class="noWidthCenterOuter clearBoth">';
		tips += '        <ol class="upChannelListOl pB0 pT10 noWidthCenter">';
		tips += '            <li><a href="javascript:;" id="applyAllBtn" class="normalBtnBig"><span><i class="miniIcoSave"></i>@Tip.apply@</span></a></li>';
		tips += '		     <li><a href="javascript:;" id="applyAllAndSave" class="normalBtnBig"><span><i class="miniIcoData"></i>@Tip.applyAndSave@</span></a></li>';
		tips += '		     <li><a href="javascript:;" id="closeWinBtn" class="normalBtnBig"><span><i class="miniIcoWrong"></i>@Tip.off@</span></a></li>';
		tips += '        </ol>';
		tips += '    </div>';
		tips += '</div>';
		if(arr.length == 0){
			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : '@Tip.noModify2@'
			})
			return;
		}
		var $tipsWin = $("#tipsWin"); 
		$("#tipsWin").empty().html(tips);
		$("#applyAllBtn").bind("click",function(){//点击保存按钮;
			//保存应用到所有
			applyToAllDevice(arr, false);
		});
		$("#applyAllAndSave").bind("click",function(){//应用并保存;
			//保存应用到所有并更新全局
			applyToAllDevice(arr, true);
		});
		$("#closeWinBtn").bind("click", function(){
			top.closeWindow("applyAllTipWin");
		});
		var $dom = $tipsWin.clone(true).get(0); 
		$tipsWin.empty();
		top.createMessageWindow("applyAllTipWin", "@COMMON.tip@", 800, 500, $dom, false, true)
		$dom = null; 
	};//end function;
	//保存到全局配置的提示;
	function saveAsGobalTip(){
		var arr = [];
		var tips = '<div class="edge10"><p class="blueBordTxt pB10">@Tip.globalConfigTip@</p>';
		tips += '<div style="height:320px; overflow:auto;"><table class="contrastTable noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">';
		tips += '<thead><tr><th>@Performance.targetName@</th><th>@Tip.CollectionTime@</th><th>@Tip.isOpen@</th></tr></thead><tbody>'
		$(".contrastTable tbody").find(".selectedOne").each(function(){
			var $me = $(this);
			if( $me.attr("checked") == true || $me.attr("checked") == "checked" ){
				arr.push($me.attr("name"));
				var altArr = $me.attr("alt").split("#"); 
				tips += '<tr>';
				tips +=     '<td class="pL5">';
				tips +=         altArr[0] + ' &gt; ' + altArr[1]; 
				tips +=     '</td>';
				tips +=     '<td class="txtCenter">';
				tips +=         altArr[2];
				tips +=     '</td>';
				tips +=     '<td class="txtCenter">';
				var num = Number(altArr[3]);
				num = renderNumToImg(num);
				tips +=         num;
				tips +=     '</td>';
				tips += '</tr>';
			} 
		})
		tips += '</tbody></table></div>';
		tips += '    <div class="noWidthCenterOuter clearBoth">';
		tips += '        <ol class="upChannelListOl pB0 pT10 noWidthCenter">';
		tips += '            <li><a href="javascript:;" id="saveAsGobalBtn" class="normalBtnBig"><span><i class="miniIcoData"></i>@Tip.save@</span></a></li>';
		tips += '		     <li><a href="javascript:;" id="closeWinBtn" class="normalBtnBig"><span><i class="miniIcoWrong"></i>@Tip.off@</span></a></li>';
		tips += '        </ol>';
		tips += '    </div>';
		tips += '</div>';
		if(arr.length < 1){
			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : '@Tip.noModify@'
			})
			return;
		}
		var $tipsWin = $("#tipsWin"); 
		$("#tipsWin").empty().html(tips);
		$("#saveAsGobalBtn").bind("click",function(){//点击保存按钮;
			$.ajax({
				url: '/cmc/perfTarget/saveAsCmcGlobalTarget.tv',
		    	type: 'POST',
		    	data: {
		    		entityId : deviceEntityId,
		    		entityType : deviceType,
		    		targetData : arr
		    	},
		    	dataType:"json",
		   		success: function(result) {
		   			backStep3ApplyAll();
		   			top.afterSaveOrDelete({
						title : "@COMMON.tip@",
						html : '<p><b class="orangeTxt">@Tip.saveGlobalSucc@</b></p>'
					})
		   		}, error: function(result) {
				}, 
				//async : false,
				cache: false
			});				
			top.closeWindow("applyAllTipWin");
		});
		$("#closeWinBtn").bind("click", function(){
			top.closeWindow("applyAllTipWin");
		});
		var $dom = $tipsWin.clone(true).get(0); 
		$tipsWin.empty();
		top.createMessageWindow("applyAllTipWin", "@COMMON.tip@", 800, 500, $dom, false, true)
		$dom = null; 
	}
</script>	
</body>
</Zeta:HTML>
