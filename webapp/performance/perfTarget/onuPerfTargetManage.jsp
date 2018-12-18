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
						<td width="146">
							<select id="deviceType" style="width: 132px;" class="normalSel">
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
						<td>
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
			<!-- 动态生成的代码结构将会是注释里面的格式 -->
			<!-- <table cellpadding="0" cellspacing="0" border="0" class="modifyTable" width="100%">
				<thead>
					<tr>
						<th colspan="9"><b class="thTit">title</b><a href="javascript:;" class="blueArr nm3kTip" nm3kTip="unlock all"></a><a href="javascript:;" class="redArr nm3kTip" nm3kTip="lock all"></a></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="rightBlueTxt w200">
							<img src="/images/performance/Help.png" class="tipImg" alt="help info" />cpu :
						</td>
						<td width="100">
							<input type="text" class="normalInput" style="width:50px;" /> Min
						</td>
						<td>
							<img src="../../images/performance/on.png" name="on" class="scrollBtn" />
						</td>
						<td class="rightBlueTxt w200">cpu:</td>
						<td width="100">
							<input type="text" class="normalInput" style="width:50px;" /> Min
						</td>
						<td>
							<img src="../../images/performance/on.png" alt="on" class="scrollBtn" />
						</td>
						<td class="rightBlueTxt w200">cpuMin:</td>
						<td width="100">
							<input type="text" class="normalInput" style="width:50px;" /> Min
						</td>
						<td>
							<img src="../../images/performance/on.png" alt="on" class="scrollBtn" />
						</td>
					</tr>
				</tbody>
			</table> -->
		</div>
	</div>
	<!-- 第三步，对比部分DOM -->
	<div id="contrastDiv" class="displayNone">
		<div id="step3ToolBar"></div>
		<div id="step3Body"></div>
	</div>
	<div id="tipsWin"></div>
	
<script type="text/javascript">
	Ext.QuickTips.init();
	var pageSize = <%= uc.getPageSize() %>;
	var flagObj = {
		gData : null, //用来保存后台获取的数据，主要是保存全局数据;
		step2 : false //第二层是否弹出,如果没有弹出或没有加载好数据，那么上面的按钮点击就不会生效;
	}
	var h = document.documentElement.clientHeight-100;
	var w = document.documentElement.clientWidth-40;
	
	//初始化地域查询条件
	var	TYPE_OPTION_FMT = '<option value="{0}">{1}</option>';
	function initData() {
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
	  var onuType = EntityType.getOnuType();
	  $.ajax({
	      url:'/entity/loadSubEntityType.tv?type=' + onuType,
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
	
	// 创建顶部工具栏
	function createTopToolbar(){
		new Ext.Toolbar({
	        renderTo: "toolBar",
	        items: [
	           {text: '@Tip.refresh@',iconCls: 'bmenu_refresh', handler: fRefresh}
	           ]
	    });
	};
	
	//刷新grid
	function fRefresh(){
		//window.location.href = window.location.href;
		store.reload();
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
	
	function showEntityDetail(entityId, entityName){
		var name = unescape(entityName);
		window.parent.addView('entity-' + entityId, name , 'entityTabIcon','/onu/showOnuPerf.tv?module=8&onuId=' + entityId, null, true);
	}
	
	function deviceNameRender(value, p, record){
		var deviceName = escape(record.data.deviceName);
		var formatStr = '<a href="javascript:;" onclick="showEntityDetail({0},\'{1}\')">{2}</a>';
		return String.format(formatStr, record.data.entityId, deviceName, record.data.deviceName);
	}

	function ipRender(value, p, record){
		var parentName = escape(record.data.parentName);
		var parentId = record.data.parentId;
		var parentTypeId = record.data.parentTypeId;
		var formatStr = '<a href="javascript:;" onclick="showOltDetail({0},\'{1}\',{2})">{3}</a>';
		return String.format(formatStr, parentId, parentName, parentTypeId, value);
	}
	
	function showOltDetail(entityId, entityName, typeId){
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
		var formatStr = "<a href='javascript:;' onclick='showModify({0},{1},{2})' style='margin-right:10px'>@Tip.edit@</a>";
		return String.format(formatStr, entityType, entityId, typeId);
	}

	var cm = new Ext.grid.ColumnModel([
	    {header:'@Performance.deviceId@', dataIndex: 'entityId',hidden: true},
   		{header:'<div class="txtCenter">@Performance.deviceName@</div>', dataIndex: 'deviceName', align:"left", width:150, renderer: deviceNameRender},
   		{header:'<div class="txtCenter">@resources/COMMON.uplinkDevice@</div>', dataIndex: 'uplinkDevice', align:"left", width:100, renderer : ipRender},
   		{header:'@COMMON.type@', dataIndex: 'displayName', align:"center", width:100, menuDisabled:true},
   		{header:'<div class="txtCenter">@Performance.area@</div>', dataIndex: 'location', align:"left", width:140, renderer : areaRender},
   		{header:'@Performance.onu_onlineStatus@', dataIndex: 'onu_onlineStatus', align:"center", width:100,renderer: collectRender},
   		{header:'@Performance.optLink@', dataIndex: 'onu_optLink', align:"center", width:100,renderer: collectRender},
   		{header:'@Performance.onu_portFlow@', dataIndex: 'onu_portFlow', align:"center", width:100,renderer: collectRender},
   		{header:'@Performance.onuCatv@', dataIndex: 'onuCatvInfo', align:"center", width:100,renderer: collectRender},
   		{header:'@Tip.operator@', dataIndex: 'operator', align:"center", width:80,renderer: operatorRender}
	]);

	window.reader = new Ext.data.JsonReader({
	    idProperty: "entityId",
	    root: "data",
	    totalProperty: "totalCount",
	    fields:['entityId', 
                'deviceName', 
                "entityType",
                "typeId",
                'manageIp', 
                'location',
                'onu_optLink', 
                'onu_onlineStatus', 
                'onu_portFlow',
                'onuCatvInfo',
                'displayName',
                'uplinkDevice',
                'parentId',
                'parentName',
                'parentTypeId'
               ]
	});
	
	//生成store
	window.store = new Ext.data.Store({
		url: '/onu/onuPerfGraph/loadOnuPerfTargetList.tv',
		reader: reader
	});
	
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
	         autoScroll: false
	     }]
	}); 
	store.load({params: {start: 0, limit: pageSize}});
	
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
	function showModify(type, entityId, typeId){
		$("#modifyDiv").removeClass("displayNone");
		deviceEntityId = entityId;
		deviceType = type;
		deviceTypeId = typeId;
		setEqTypeBtnName(type);
		createNewTypePage(entityId);
	}
	//设置正确的按钮名称
	function setEqTypeBtnName(type){
		var btnName = "@tip.applyToAllOnu@";
		$("#useToAll .x-btn-mc").find("button").text(btnName);
	};
	//根据类型生成不同的页面;
	function createNewTypePage(entityId){
		var $modifyMainPart = $("#modifyMainPart");
		$modifyMainPart.empty();
		
		var dataStore;
		$.ajax({
			url: '/onu/onuPerfGraph/loadOnuPerfTarget.tv',
	    	type: 'POST',
	    	data: {
	    		entityId : entityId
	    	},
	    	dataType:"json",
	   		success: function(result) {
	   			dataStore = result;
	   			//ajax 去取数据;
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
			url: '/onu/onuPerfGraph/loadOnuPerfTarget.tv',
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
	           {text: '@Tip.save@', iconCls: 'bmenu_data', handler: saveModifyOneOnuPerf},
	           "-",
	           {text: '@Tip.applyToAll@',id:'useToAll', iconCls: 'bmenu_save', handler: showApplyToAllOnu},
	           {text: '@Tip.saveAsGlobalConfig@', iconCls: 'bmenu_data', handler: saveAsGolbal},
	           {text: '@Tip.useGolbalConfig@', iconCls: 'bmenu_arrange', handler: useOnuGolbalData},
	           "-",
	           createSetTimeButton()
	           ]
	    });
	}
	
	//保存;
	function saveModifyOneOnuPerf(){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		var reg = testAllInput();//验证;
		if(!reg){return};
		var data = getAllSetData();
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
			url: '/onu/onuPerfGraph/modifyOnuPerfTarget.tv',
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
				});
	   			//doRefresh();
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
	function showApplyToAllOnu(){
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
		//先去后台获取一下数据;
		getRecentlyNewData();
		var thArr = ["@Tip.pleaseSelected@","@Performance.targetName@","@Tip.afterUse@","@Tip.globalConfig@"];
		createContrastTable(thArr);
		autoHeight();
		$("#step3Body").bind("click",this,clickApplyAll);
	};//end showApplyToAllCmc;
	
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
		hideSameColumn();
		
		$("#step3Body").bind("click",this,clickApplyAll);
	}
	//使用全局配置;
	function useOnuGolbalData(){
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
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                               //
	//                                             第三部分                                                                                                                 //
	//                                                                                               //
	///////////////////////////////////////////////////////////////////////////////////////////////////	
	//保存应用到所有操作
	function applyToAllDevice(targetArray, saveGlobalFlag){
		top.showWaitingDlg("@Tip.ing@");
		$.ajax({
			url: '/onu/onuPerfGraph/applyCurrentTargetToAllOnu.tv',
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
					html : '<p><b class="orangeTxt">@tip.applyToAllOnuSuc@</b></p>'
				});
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
		var newTips =String.format("@Tip.willModify@","ONU");
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
			//top.closeWindow("applyAllTipWin");
		});
		$("#applyAllAndSave").bind("click",function(){//应用并保存;
			//保存应用到所有并更新全局
			applyToAllDevice(arr, true);
			//top.closeWindow("applyAllTipWin");
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
			//保存为全局配置
			$.ajax({
				url: '/onu/onuPerfGraph/saveAsOnuGlobalTarget.tv',
		    	type: 'POST',
		    	data: {
		    		entityId : deviceEntityId,
		    		entityType : deviceType,
		    		typeId : deviceTypeId,
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
