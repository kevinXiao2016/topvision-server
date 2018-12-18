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
    module performance
    CSS    performance/css/performanceGlobalConfig
    CSS    performance/css/perTargetManageCommon
    import js.json2
    IMPORT performance/nm3kBatchData
    IMPORT performance/js/perfTargetManageCommon
</Zeta:Loader>
</HEAD>
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
						<td class="rightBlueTxt">@Performance.manageIp@:</td>
						<td width="146">
							<input class="normalInput w130" type="text" id="deviceIp"/> 
						</td>
						<td class="rightBlueTxt">@Performance.deviceType@:</td>
						<td width="146">
							<select id="deviceType" style="width: 132px;" class="normalSel">
								<option value="-1">@Tip.select@</option>
								<option value="41100">BSR2000</option>
                                <option value="42100">UBR7225</option>
                                <option value="42200">UBR7246</option>
                                <option value="43100">CASA-C2100</option>
								<option value="43200">CASA-C2200</option>
								<option value="43300">CASA-C3000</option>
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
		<div id="modifyMainPart">
			
		</div>
	</div>
	<!-- 第三步，对比部分DOM -->
	<div id="contrastDiv" class="displayNone">
		<div id="step3ToolBar"></div>
		<div id="step3Body"></div>
	</div>
	<div id="tipsWin"></div>
	
	
<!-- <script type="text/javascript" src="../nm3kBatchData.js"></script> -->
<script type="text/javascript">
	var pageSize = <%= uc.getPageSize() %>;
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
	
	// 创建顶部工具栏
	function createTopToolbar(){
		new Ext.Toolbar({
	        renderTo: "toolBar",
	        items: [
	           {text: '@Tip.refresh@',cls:'mL10', iconCls: 'bmenu_refresh'}
	           ]
	    });
	};
	createTopToolbar();
	createModifyTopToolbar();
	autoHeight();
	
	$(window).resize(function(){
		throttle(autoHeight,window);
	});//end resize;
	
	function showEntityDetail(entityId, entityName){
		window.parent.addView('entity-' + entityId, entityName , 'entityTabIcon','/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=7&entityId=' + entityId);
	}
	
	function deviceNameRender(value, p, record){
		var formatStr = '<a href="#" onclick="showEntityDetail({0},\'{1}\')">{1}</a>';
		return String.format(formatStr, record.data.entityId, record.data.deviceName);
	}

	function ipRender(value, p, record){
		var formatStr = '<a href="#" onclick="showEntityDetail({0},\'{1}\')">{2}</a>';
		return String.format(formatStr, record.data.entityId, record.data.deviceName, record.data.manageIp);
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
	
	function typeRender(v, p, record){
		switch(v){
			case 41100 : return "BSR2000";
            case 42100 : return "UBR7225";
            case 42200 : return "UBR7246";
            case 43100 : return "CASA-C2100";
			case 43200 : return "CASA-C2200";
			case 43300 : return "CASA-C3000";
		}
	}
	
	function operatorRender(value, p, record){
		var entityType = record.data.entityType;
		var entityId = record.data.entityId;
		var typeId = record.data.typeId;
		var formatStr = "<a href='javascript:;' onclick='showModify({0},{1},{2})'>@Tip.edit@</a>";
		return String.format(formatStr, entityType, entityId, typeId);
	}

	var cm = new Ext.grid.ColumnModel([
   	  	{header:'@Performance.deviceId@', dataIndex: 'entityId', align:"center",resizable: false,  hidden: true},
   	 	{header:'<div class="txtCenter">@Performance.deviceName@</div>', dataIndex: 'deviceName', align:"left", width:150, renderer: deviceNameRender},
 		{header:'<div class="txtCenter">@Performance.manageIp@</div>', dataIndex: 'manageIp', align:"left", width:120, renderer: ipRender},
 		{header:'@COMMON.type@', dataIndex: 'typeId', align:"center", width:80, renderer: typeRender, menuDisabled:true},
 		{header:'<div class="txtCenter">@Performance.area@</div>', dataIndex: 'location', align:"left", width:100},
		{header:'@Performance.deviceRelay@', dataIndex: 'cmts_onlineStatus', align:"center",renderer: collectRender,resizable: false},
		{header:'@Performance.snr@', dataIndex: 'cmts_snr', align:"center",renderer: collectRender,resizable: false},
		{header:'@Performance.ber@', dataIndex: 'cmts_ber', align:"center",renderer: collectRender,resizable: false},
		{header:'@Performance.upLinkFlow@', dataIndex: 'cmts_upLinkFlow', align:"center",renderer: collectRender,resizable: false},
		{header:'@Performance.channelSpeed@', dataIndex: 'cmts_channelSpeed', align:"center",renderer: collectRender,resizable: false},
		{header:'@Tip.operator@', dataIndex: 'operator', align:"center",renderer: operatorRender,resizable: false}
   	]);
	
	var reader = new Ext.data.JsonReader({
	    totalProperty: "totalCount",
	    idProperty: "entityId",
	    root: "data",
	    fields:['entityId', 
	        'deviceName', 
	        "entityType",
	        "typeId",
	        'manageIp',
	        'location',
	        'cmts_snr', 
	        'cmts_ber', 
	        'cmts_upLinkFlow', 
	        'cmts_channelSpeed', 
	        'cmts_onlineStatus'
	       ]
	});
	
	//生成store
	window.store = new Ext.data.GroupingStore({
		url: '/cmts/perfTarget/loadCmtsTargetList.tv',
		reader: reader,
        remoteGroup: false,
		groupField: 'typeId',
		groupOnSort: false,
       	sortInfo:{field:'entityId',direction:'asc'}
	});
	store.load({params: {start: 0, limit: pageSize}});
	
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
            forceFit: true, hideGroupedColumn: true,enableNoGroups: true,groupTextTpl: groupTpl
        }),
		bbar:bbar,
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
	var deviceEntityId;
	var deviceType;
	var deviceTypeId;
	function showModify(type, entityId, typeId){
		$("#modifyDiv").removeClass("displayNone");
		deviceEntityId = entityId;
		deviceType = type;
		deviceTypeId = typeId;
		setEqTypeBtnName(typeId);
		createNewTypePage(entityId);
	}
	//设置正确的按钮名称
	function setEqTypeBtnName(typeId){
		var btnName = "@Tip.applyToAllCmts@";
		/* 
		switch(typeId){
			case 41100 : btnName + "BSR2000";
            case 42100 : btnName +  "UBR7225";
            case 42200 : btnName +  "UBR7246";
            case 43100 : btnName +  "CASA-C2100";
			case 43200 : btnName +  "CASA-C2200";
			case 43300 : btnName +  "CASA-C3000";
		}
		 */
		$("#useToAll .x-btn-mc").find("button").text(btnName);
	};
	//根据类型生成不同的页面;
	function createNewTypePage(entityId){
		var $modifyMainPart = $("#modifyMainPart");
		$modifyMainPart.empty();
		
		var dataStore;
		$.ajax({
			url: '/cmts/perfTarget/loadCmtsPerfTarget.tv',
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
		
	
	// 创建修改页面的顶部工具栏;	
	function createModifyTopToolbar(){
		new Ext.Toolbar({
	        renderTo: "modifyToolBar",
	        items: [
	           {text: '@Tip.back@', iconCls: 'bmenu_back',cls:'mL10', handler: backFn},'-',
	           {text: '@Tip.save@', iconCls: 'bmenu_save', handler: saveModifyOneCmcPerf},
	           "-",
	           {text: '@Tip.applyToAll@',id:'useToAll', iconCls: 'bmenu_equipment', handler: showApplyToAllCmc},
	           {text: '@Tip.saveAsGlobalConfig@', iconCls: 'bmenu_save', handler: saveAsGolbal},
	           {text: '@Tip.useGolbalConfig@', iconCls: 'bmenu_arrange', handler: useCmcGolbalData},
	           "-",
	           createSetTimeButton()
	           ]
	    });
	}
	
	
	function saveModifyOneCmcPerf(){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		var reg = testAllInput();//验证;
		if(!reg){return};
		var data = getAllSetData();
		
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
			url: '/cmts/perfTarget/modifyCmtsPerfTarget.tv',
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
	   			//更新页面性能指标数据
	   			flagObj.gData = result;
	   			//提示框
	   			top.afterSaveOrDelete({
					title : "@COMMON.tip@",
					html : '<p><b class="orangeTxt">@Tip.modifySuccess@</b></p>'
				})
				doRefresh();
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                               //
	//                                             第三部分                                                                                                                 //
	//                                                                                               //
	///////////////////////////////////////////////////////////////////////////////////////////////////	
	//保存应用到所有操作
	function applyToAllDevice(targetArray, saveGlobalFlag){
		top.showWaitingDlg("@Tip.ing@");
		$.ajax({
			url: '/cmts/perfTarget/applyCurrentTargetToAllCmts.tv',
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
	   			var o = {
	   				succeedNum : result.succeedNum,
	   				failedNum : result.failedNum,
	   				succeedDetail : result.succeedDetail,
	   				failedDetail : result.failedDetail
	   			};
	   			//更新页面性能指标数据
	   			//flagObj.gData = result;
	   			top.afterSaveOrDelete({
					title : "@COMMON.tip@",
					html : '<p><b class="orangeTxt">@Tip.applyCMTSSuccess@</b></p>'
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
		var newTips =String.format("@Tip.willModify@","CMTS");
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
		tips += '		     <li><a href="javascript:;" id="closeWinBtn" class="normalBtnBig"><span>@Tip.off@</span></a></li>';
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
		tips += '		     <li><a href="javascript:;" id="closeWinBtn" class="normalBtnBig"><span>@Tip.off@</span></a></li>';
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
				url: '/cmts/perfTarget/saveAsCmcGlobalTarget.tv',
		    	type: 'POST',
		    	data: {
		    		entityId : deviceEntityId,
		    		entityType : deviceType,
		    		targetData : arr
		    	},
		    	dataType:"json",
		   		success: function(result) {
		   			//更新页面性能指标数据
		   			flagObj.gData = result;
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
		top.createMessageWindow("applyAllTipWin", "@COMMON.tip@", 800, 500, $dom, false, true);
		$dom = null; 
	}
</script>	
</body>
</Zeta:HTML>
