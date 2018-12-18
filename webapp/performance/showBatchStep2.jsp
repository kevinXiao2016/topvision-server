<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<HEAD>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	library Highchart
	plugin DateTimeField
    module  performance
    import js/entityType
    IMPORT performance/nm3kBatchData 
</Zeta:Loader>
</HEAD>
<body class="batchBg">
	<div id="topPart" class="edge10">
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
					<td width="146">
						<select id="deviceType" style="width: 132px;" class="normalSel">
							<option value="-1">@Tip.select@</option>
						</select>
					</td>
					<td class="rightBlueTxt">@Performance.area@:</td>
					<td width="146">
						<!-- <input class="normalInput w130" type="text" /> -->
						<select id="deviceArea" style="width: 132px;" class="normalSel">
							<option value="-1">@Tip.select@</option>
						</select>
					</td>
					<td>
						<ol class="upChannelListOl pB0 pT0">
							<li>
								<a href="javascript:;" class="normalBtn" onclick="queryClick();">
						 			<span><i class="miniIcoSearch"></i>@Tip.query@</span>
						 		</a>
							</li>
							<li>
								<a href="javascript:;" class="normalBtn" onclick="useFn()">
						 			<span><i class="miniIcoSave"></i>@COMMON.apply@</span>
						 		</a>
							</li>
						</ol>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<script type="text/javascript">
	Ext.QuickTips.init();
	var parentType = '${parentType}';
	var entityType= '${entityType}';
	var collectInterval = '${collectInterval}';
	var targetEnable = '${targetEnable}';
	var perfTargetName = '${perfTargetName}';
	var oltType = EntityType.getOltType();
	var ccAndCmtsType = EntityType.getCCMTSAndCMTSType();
	var cmtsType = EntityType.getCmtsType();
	var ccType = EntityType.getCcmtsType();
	var onuType = EntityType.getOnuType();
	//模块支持情况,在单独安装CC模块的时候使用
	var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
	var eponSupport = <%= uc.hasSupportModule("olt")%>;
	
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
	if(parentType == oltType || parentType == onuType){
		$.ajax({
		      url:'/entity/loadSubEntityType.tv?type=' + parentType,
		      type:'POST',
		      dateType:'json',
		      success:function(response) {
		    	  var entityTypes = response.entityTypes 
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
		      },
		      error:function(entityTypes) {},
		      cache:false
		  });
	}
	if(parentType == ccAndCmtsType){
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
		      error:function(entityTypes) {},
		      cache:false
		  });
	}
	
	var pageSize = <%= uc.getPageSize() %>;
	var h = document.documentElement.clientHeight-100;
	var w = document.documentElement.clientWidth-40;
	
	function showEntityDetail(entityId, entityName, entityType){
		var name = unescape(entityName);
		if(entityType == oltType){
			window.top.addView('entity-' + entityId, name , 'entityTabIcon','/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=7&entityId=' + entityId);
		}else if(entityType == ccType){  //cmc性能曲线
			window.top.addView('entity-' + entityId, name , 'entityTabIcon','/cmcPerfGraph/showCmcCurPerf.tv?module=13&cmcId=' + entityId);
		}else if(entityType == cmtsType){ //cmts性能曲线
			window.top.addView('entity-' + entityId, name , 'entityTabIcon','/cmts/showCmtsCurPerf.tv?module=8&cmcId='+entityId);
		}else if(entityType == onuType){
			window.top.addView('entity-' + entityId, name , 'entityTabIcon','/onu/showOnuPerf.tv?module=4&onuId=' + entityId);
		}
	}
	
	function deviceNameRender(value, p, record){
		var deviceName = escape(record.data.deviceName);
		var formatStr = '<a href="javascript:;" onclick="showEntityDetail({0},\'{1}\',{2})">{3}</a>';
		return String.format(formatStr, record.data.entityId, deviceName, record.data.entityType, record.data.deviceName);
	}

	function ipRender(value, p, record){
		//olt, ccmts_B, ccmts_A, onu
		var entityType = record.data.entityType;
		var entityId = record.data.entityId;
		var deviceName = escape(record.data.deviceName);
		if(entityType == oltType){
			var formatStr = '<a href="javascript:;" onclick="showOltPerf({0},\'{1}\',{2})">{3}</a>';
			return String.format(formatStr, entityId, deviceName, record.data.typeId, value);
		}else if(entityType == cmtsType){
			var formatStr = '<a href="javascript:;" onclick="showCmtsPerf({0},\'{1}\')">{2}</a>';
			return String.format(formatStr, entityId, deviceName, value);
		}else if(entityType == ccType){
			var typeId = record.data.typeId;
			if(EntityType.isCcmtsWithoutAgentType(typeId)){
				var parentName = escape(record.data.parentName);
				var parentId = record.data.parentId;
				var parentTypeId = record.data.parentTypeId;
				var formatStr = '<a href="javascript:;" onclick="showOltPerf({0},\'{1}\',{2})">{3}</a>';
				return String.format(formatStr, parentId, parentName, parentTypeId, value);
			}else{
				var formatStr = '<a href="javascript:;" onclick="showCcmtsPerf({0},\'{1}\')">{2}</a>';
				return String.format(formatStr, entityId, deviceName, value);
			}
		}else if(entityType == onuType){
			var parentName = escape(record.data.parentName);
			var parentId = record.data.parentId;
			var parentTypeId = record.data.parentTypeId;
			var formatStr = '<a href="javascript:;" onclick="showOltPerf({0},\'{1}\',{2})">{3}</a>';
			return String.format(formatStr, parentId, parentName, parentTypeId, value);
		}
	}
	
	function showOltPerf(entityId, entityName, typeId){
		var name = unescape(entityName);
		if(EntityType.isStandardOlt(typeId)){
			window.top.addView('entity-' + entityId, name , 'entityTabIcon','/epon/standardOlt/showStandardOltPerfView.tv?module=7&entityId=' + entityId, null, true);
		}else{
			window.top.addView('entity-' + entityId, name , 'entityTabIcon','/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=7&entityId=' + entityId, null, true);
		}
	}
	
	function showCmtsPerf(entityId, entityName){
		var name = unescape(entityName);
		window.top.addView('entity-' + entityId, name , 'entityTabIcon','/cmts/showCmtsCurPerf.tv?module=8&cmcId='+entityId);
	}
	
	function showCcmtsPerf(entityId, entityName){
		var name = unescape(entityName);
		window.top.addView('entity-' + entityId, name , 'entityTabIcon','/cmcPerfGraph/showCmcCurPerf.tv?module=13&cmcId=' + entityId);
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
	
	function areaRender(value,metadata,record){
		metadata.attr = 'ext:qtip="' + value +'"';
		return value;
	}
	
	var selName = nm3kBatchData[perfTargetName].name;
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([
   	    sm,
   	    {header:'@Performance.deviceId@', dataIndex: 'entityId', align:"center",resizable: false, hidden: true},
   		{header:'<div class="txtCenter">@Performance.deviceName@</div>', align:"left", width:150, renderer: deviceNameRender},
   		{header:'<div class="txtCenter">@resources/COMMON.uplinkDevice@</div>', dataIndex: 'uplinkDevice', align:"left", width:120, renderer: ipRender},
   		{header:'@COMMON.type@', dataIndex: 'displayName', align:"center", width:80,resizable: false},
   		{header:'<div class="txtCenter">@Performance.area@</div>', dataIndex: 'location', align:"left", width:150,resizable: false,renderer: areaRender},
   		{header:selName, dataIndex: perfTargetName, align:"center", width:130  ,renderer: collectRender }
   	]);
	
	var fields = [];
	if(parentType == oltType){
		fields = ['entityId', 'deviceName', "entityType","typeId",'manageIp', 'uplinkDevice', 'location','olt_cpuUsed', 'olt_memUsed','displayName',
		          'olt_flashUsed',  'olt_boardTemp','olt_fanSpeed', 'olt_optLink', 'olt_ponFlow', 'olt_sniFlow', 'olt_onlineStatus'
		          ];
	}
	if(parentType == ccAndCmtsType ){
		fields = ['entityId', 'deviceName', "entityType","typeId", 'manageIp','uplinkDevice', 'location','cmc_cpuUsed', 'cmc_memUsed', 'cmc_flashUsed', 'cmc_moduleTemp','displayName',
		          'cmc_macFlow','cmc_optLink','cmc_upLinkFlow','cmc_channelSpeed','cmc_opticalReceiver','cmc_cmflap','cmc_snr','cmc_ber','cmc_onlineStatus','cmc_dorOptTemp','cmc_dorLinePower',
		          'parentId', 'parentTypeId', 'parentName'
		          ];
	}
	if(parentType == onuType){
		fields = ['entityId', 'deviceName','uplinkDevice', "entityType","typeId",'manageIp', 'location','onu_optLink', 'onu_onlineStatus', 'onu_portFlow','displayName',
		          'parentId', 'parentTypeId', 'parentName', 'onuCatvInfo'
		          ];
	}
	
	var reader = new Ext.data.JsonReader({
	    totalProperty: "totalCount",
	    idProperty: "entityId",
	    root: "data",
	    fields : fields
	});

	//生成store
	window.store = new Ext.data.GroupingStore({
		url: '/performance/loadSupportTargetDevice.tv',
		reader: reader,
	    remoteGroup: false,
		groupField: 'displayName',
		groupOnSort: false,
	   	sortInfo:{field:'entityId',direction:'asc'}
	});
	
	
	store.baseParams={
	   		start: 0,
			limit: pageSize,
			parentType : parentType,
			entityType: window.entityType,
			perfTargetName: perfTargetName
	};
	store.load();
	
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
		title:'@Tip.gridTitle@',
		bbar: bbar,
		view: new Ext.grid.GroupingView({
            forceFit: true, hideGroupedColumn: true,enableNoGroups: true,groupTextTpl: groupTpl
        }),
		cls: 'normalTable',
		store: store,
		cm : cm,
		sm : sm,
		viewConfig:{
			forceFit: true
		}
	});
	var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [grid,
	         {region:'north',
	         height: 60,
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
			parentType : parentType,
			entityType: window.entityType,
			perfTargetName: perfTargetName,
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
	
	//应用;
	function useFn(){
		var sm = grid.getSelectionModel();
		if (sm != null && sm.hasSelection()) {
			var selections = sm.getSelections();
			var idArray = [];
			for(var i=0; i<selections.length; i++){
				var nId = selections[i].data.entityId;
				idArray.push(nId);
			}
			var entityIds = idArray.join(',');
			top.showWaitingDlg("@Tip.ing@");
			$.ajax({
				url: '/performance/batchModifyDeviceSingleTarget.tv',
		    	type: 'POST',
		    	data: {
		    		entityType : entityType,
		    		collectInterval : collectInterval,
		    		targetEnable : targetEnable,
		    		perfTargetName : perfTargetName,
		    		entityIds : entityIds
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
		   			top.createMessageWindow("applyAllTipWin", "@COMMON.tip@", 800, 500, str, false, true)
		   			
		   			//提示框
		   			top.afterSaveOrDelete({
						title : "@COMMON.tip@",
						html : '<p><b class="orangeTxt">@tip.applySuccess@</b></p>'
					});
		   			fGotoURL(parentType);
		   		}, error: function(result) {
				},
				cache: false
			});
		} else {
			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : '<p><b class="orangeTxt">@Performance.selectDevice@</b></p>'
			});
		}
		//parent.completeStep();
	}
	
	//保存后跳转;
	function fGotoURL(type){
		switch(Number(type)){
			case oltType:
				top.addView("eponPerfParamConfig", "@Performance.oltPerformanConfig@", "icoE4", "/epon/perfTarget/showOltPerfManage.tv", false, true);
				break;
			case ccAndCmtsType:
				top.addView("cmcPerfParamConfig", "@Performance.cmcPerfParamConfig@", "icoE6", "/cmc/perfTarget/showCmcPerfManage.tv", false, true);
				break;
			case onuType : 
				top.addView("onuPerfParamsConfig", "@Performance.onuPerfParamsConfig@", "icoE4", "/onu/onuPerfGraph/showOnuPerfManage.tv",false,true);
		}
		top.removeTab("batch");//关闭当前tab;
	};
	
	//生成结果table的字符串;
	function createResultTable(o){
	    var str = '<div class="edge10" style="height:390px; overflow:auto;">';
		    str +=     '<p class="flagInfo">@Tip.successNum@:'+ o.succeedNum +', @Tip.failNum@:'+ o.failedNum +'</p>'
		    str +=     '<table class="contrastTable noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">';
			str +=         '<thead>';
			str +=             '<tr>';
			str +=                 '<th>@Performance.deviceName@</th>';
			str +=                 '<th width="160">@Performance.manageIp@</th>';
			str +=                 '<th width="160">@Performance.result@</th>';
			str +=             '</tr>';
			str +=         '</thead>';
			str +=         '<tbody>';
			for(var i=0; i<o["succeedDetail"].length; i++){//成功的部分;
				var obj = o["succeedDetail"][i];
			    str +=         '<tr>';
			    str +=             '<td class="pL5">'+ obj["deviceName"] +'</td>';
		        str +=             '<td class="pL5 txtCenter">'+ obj["deviceIp"] +'</td>';
			    str +=             '<td class="txtCenter"><b class="greenTxt">@Tip.success@</b></td>';
			    str +=         '</tr>';
			}
			for(i=0; i<o["failedDetail"].length; i++){
				var obj2 = o["failedDetail"][i];
				str +=     '<tr>';
			    str +=         '<td class="pL5">'+ obj2["deviceName"] +'</td>';
		        str +=         '<td class="pL5 txtCenter">'+ obj2["deviceIp"] +'</td>';
			    str +=         '<td class="txtCenter"><b class="redTxt">@Tip.fail@</b></td>';
			    str +=     '</tr>';
			}
			str +=         '</tbody>';
			str +=     '</table>'
			str += '</div>';
			str += '<div class="noWidthCenterOuter clearBoth">';
			str +=     '<ol class="upChannelListOl pB0 pT0 noWidthCenter">';
			str +=         '<li><a href="javascript:;" class="normalBtnBig" onclick="closeMessageWindow(\'applyAllTipWin\')"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>';
			str +=     '</ol>';
			str += '</div>';
			return str;
	}
	</script>
</body>
</Zeta:HTML>
