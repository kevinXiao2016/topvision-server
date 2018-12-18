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
</Zeta:Loader>
<style type="text/css">
body,html{width:100%; height:100%; overflow:hidden;}
#modifyDiv{ width:100%; height:100%; overflow:hidden; position:absolute; top:0; left:0; z-index: 2; background:#F3F3F3;}
#modifyMainPart{ padding:10px; overflow:auto;}
table.modifyTable{width:100%;}
table.modifyTable thead th{border-bottom:2px solid #6593CF; padding:9px 0px 4px; font-weight:bold; color:#36629F;}
table.modifyTable tbody td{ border-bottom:1px solid #D6D6D6; padding:6px 2px;}
.scrollBtn{ cursor: pointer;}
.thTit{ float:left; margin-right:5px;}
.tipImg{ cursor:pointer; margin-right:4px; position:relative; top:2px;}
#contrastDiv{width:100%; height:100%; overflow:hidden; position:absolute; top:0; left:0; z-index: 10; background:#F3F3F3;}
</style>
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
							<input class="normalInput w130" type="text" /> 
						</td>
						<td class="rightBlueTxt">@Performance.manageIp@:</td>
						<td width="146">
							<input class="normalInput w130" type="text" /> 
						</td>
						<td class="rightBlueTxt">@COMMON.folder@@COMMON.maohao@</td>
						<td width="146">
							<input class="normalInput w130" type="text" />
						</td>
						<td rowspan="2">
							<ol class="upChannelListOl pB0 pT0">
								<li>
									<a href="javascript:;" class="normalBtn">
							 			<span><i class="miniIcoSearch"></i>@Tip.query@</span>
							 		</a>
								</li>
							</ol>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@Performance.deviceType@:</td>
						<td>
							<select id="deviceType" style="width: 132px;" class="normalSel">
								<option value="-1">@Tip.select@</option>
							</select>
						</td>
						<td class="rightBlueTxt">@Performance.perftarget@:</td>
						<td>
							<select class="normalSel" style="width:132px;">
								<option value="">@Tip.select@</option>
								<option value="cpuUsed">@Performance.cpuUsed@</option>
								<option value="memUsed">@Performance.memUsed@</option>
								<option value="flashUsed">@Performance.flashUsed@</option>
								<option value="optLink">@Performance.optLink@</option>
								<option value="upLinkFlow">@Performance.upLinkFlow@</option>
								<option value="macFlow">@Performance.macFlow@</option>
								<option value="channelSpeed">@Performance.channelSpeed@</option>
								<option value="moduleTemp">@Performance.moduleTemp@</option>
								<option value="snr">@Performance.snr@</option>
								<option value="ber">@Performance.ber@</option>
							</select>
						</td>
						<td class="rightBlueTxt">@Tip.collectCycle@:</td>
						<td>
							<input maxlength="4" toolTip="@Tip.collectTimeRule@" class="w130 normalInput" />
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
		
	</div>
	
	
<script type="text/javascript" src="../nm3kBatchData.js"></script>
<script type="text/javascript">
	var flagObj = {
		gData : null, //用来保存后台获取的数据，主要是保存全局数据;
		step2 : false //第二层是否弹出,如果没有弹出或没有加载好数据，那么上面的按钮点击就不会生效;
	}
	var h = document.documentElement.clientHeight-100;
	var w = document.documentElement.clientWidth-40;
	// 创建顶部工具栏
	function createTopToolbar(){
		new Ext.Toolbar({
	        renderTo: "toolBar",
	        items: [
	           {text: '@Tip.refresh@',cls:'mL10', iconCls: 'bmenu_refresh'},"-",createColumnChangesButton()
	           ]
	    });
	};
	createTopToolbar();
	createModifyTopToolbar();
	autoHeight();
	
	function autoHeight(){
		var $modifyDiv = $("#modifyDiv");
		if( !$modifyDiv.hasClass("displayNone") ){
			var h = $(window).height(),
			h1 = $("#modifyToolBar").outerHeight(),
			h2 = h - h1 - 20;//因为padding:10px;
			if(h2 < 10){ h2 = 10}
			$("#modifyMainPart").height(h2);
		}
	}
	
	$(window).resize(function(){
		throttle(autoHeight,window);
	});//end resize;
	
	//resize事件增加[函数节流];
	function throttle(method, context){
		clearTimeout(method.tId);
		method.tId = setTimeout(function(){
			method.call(context);
		},100);
	};
	
	function deviceNameRender(value, p, record){
		var formatStr = '<a href="#" onclick="showEntityPerf({0},\'{1}\')">{1}</a>';
		return String.format(formatStr, record.data.entityId, record.data.deviceName);
	}

	function ipRender(value, p, record){
		var formatStr = '<a href="#" onclick="showEntityPerf({0},\'{1}\')">{2}</a>';
		return String.format(formatStr, record.data.entityId, record.data.deviceName, record.data.manageIp);
	}
	function collectRender(v, p, record){
		if(v.targetEnable){
			return v.collectInterval;
		}else{
			return str = '<img src="/images/performance/off.png">';
		}
	}
	
	function operatorRender(value, p, record){
		var str = "<a href='javascript:;' onclick='modifyCycle(\""+record.get('entityId')+"\")'>@Tip.edit@</a>";
		return str; 
	}

	var sm = new Ext.grid.CheckboxSelectionModel();
	
	var cm = new Ext.grid.ColumnModel([
   	    sm,
   	    {header:'@Performance.deviceId@', dataIndex: 'entityId', align:"center",resizable: false, hidden: true},
   		{header:'@Performance.deviceName@', dataIndex: 'deviceName', align:"center", width:w*0.12,resizable: false, renderer: deviceNameRender},
   		{header:'@Performance.manageIp@', dataIndex: 'manageIp', align:"center", width:w*0.12,resizable: false, renderer: ipRender},
   		{header:'@Performance.cpuUsed@', dataIndex: 'cpuUsed', align:"center", width:130,renderer: collectRender,resizable: false, menuDisabled : false},
   		{header:'@Performance.memUsed@', dataIndex: 'memUsed', align:"center", width:130,renderer: collectRender,resizable: false},
   		{header:'@Performance.flashUsed@', dataIndex: 'flashUsed', align:"center", width:150,renderer: collectRender,resizable: false},
   		{header:'@Performance.boardTemp@', dataIndex: 'boardTemp', align:"center", width:130,renderer: collectRender,resizable: false},
   		{header:'@Performance.fanSpeed@', dataIndex: 'fanSpeed', align:"center", width:150,renderer: collectRender,resizable: false},
   		{header:'@Tip.operator@', dataIndex: 'operator', align:"center", width:w*0.08,renderer: operatorRender,resizable: false}
   	]);
	
	
	//生成store
	window.store = new Ext.data.JsonStore({
		url: '/epon/perfTarget/loadOltPerfTargetList.tv',
		root: 'data',
        totalProperty:'totalCount',
        fields:['entityId', 
                'deviceName', 
                'manageIp', 
                'cpuUsed', 
                //{name:"cpuUsed",mapping:"cpuUsed",convert :}
                'memUsed', 
                'flashUsed', 
                'boardTemp', 
                'fanSpeed', 
                'optLink', 
                'sniFlow', 
                'ponFlow', 
                'onuPonFlow', 
                'uniFlow'],
       	sortInfo:{field:'entityId',direction:'asc'}
	});
	
	/* var store = new Ext.data.Store({
		proxy: new Ext.data.MemoryProxy(data),
		reader: new Ext.data.ArrayReader({},[
				{name: "id"},
				{name: "name"},
				{name: "sex"},
				{name: "descn"},
				{name: "birthday"},
				{name: "bgColor"},
				{name: "js"}
			])
	}); */
	store.load();
	var grid = new Ext.grid.GridPanel({
		region: "center",
		title: "@title@",
		enableColumnMove: false,
		enableColumnResize: true,
		bbar: new Ext.PagingToolbar({
			pageSize: 10,
			store: store,
			displayInfo: true,
			displayMsg: "@subPage@",
			emptyMsg: "@emptyRs@",
			cls: 'extPagingBar'
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
	         height: 120,
	         contentEl :'topPart',
	         border: false,
	         cls:'clear-x-panel-body',
	         autoScroll: true
	     }]
	}); 
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                               //
	//                                             编辑部分                                                                                                                 //
	//                                                                                               //
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//显示编辑div;
	function showModify(type){
		$("#modifyDiv").removeClass("displayNone");
		setEqTypeBtnName(type);
		createNewTypePage(type);
	}
	//设置正确的按钮名称
	function setEqTypeBtnName(type){
		var btnName = "@ApplyToAll@";
		switch(type){
			case 30001 :
				btnName += "CC8800A";
				break;
			case 30002 :
				btnName += "CC8800B";
				break;
			default :
				btnName += "@SameTypeEquipment@";
				break;
		}
		$("#useToAll .x-btn-mc").find("button").text(btnName);
	};
	//根据类型生成不同的页面;
	function createNewTypePage(type){
		var $modifyMainPart = $("#modifyMainPart");
		$modifyMainPart.empty();
		
		//ajax 去取数据;
		var dataStore = {
				"service":{
					"boardTemp":{"collectInterval":15,"deviceName":"","entityId":30000000023,"entityType":10000,"globalEnable":1,"globalInterval":15,"manageIp":"","perfTargetName":"boardTemp","targetEnable":1,"targetGroup":"service","typeId":10003},
					"cpuUsed":{"collectInterval":15,"deviceName":"","entityId":30000000023,"entityType":10000,"globalEnable":1,"globalInterval":15,"manageIp":"","perfTargetName":"cpuUsed","targetEnable":1,"targetGroup":"service","typeId":10003},
					"fanSpeed":{"collectInterval":15,"deviceName":"","entityId":30000000023,"entityType":10000,"globalEnable":1,"globalInterval":15,"manageIp":"","perfTargetName":"fanSpeed","targetEnable":1,"targetGroup":"service","typeId":10003},
					"flashUsed":{"collectInterval":15,"deviceName":"","entityId":30000000023,"entityType":10000,"globalEnable":1,"globalInterval":15,"manageIp":"","perfTargetName":"flashUsed","targetEnable":1,"targetGroup":"service","typeId":10003},
					"memUsed":{"collectInterval":15,"deviceName":"","entityId":30000000023,"entityType":10000,"globalEnable":1,"globalInterval":15,"manageIp":"","perfTargetName":"memUsed","targetEnable":1,"targetGroup":"service","typeId":10003},
					"optLink":{"collectInterval":15,"deviceName":"","entityId":30000000023,"entityType":10000,"globalEnable":1,"globalInterval":15,"manageIp":"","perfTargetName":"optLink","targetEnable":1,"targetGroup":"service","typeId":10003}},
					"flow":{
					"ponFlow":{"collectInterval":15,"deviceName":"","entityId":30000000023,"entityType":10000,"globalEnable":1,"globalInterval":15,"manageIp":"","perfTargetName":"ponFlow","targetEnable":1,"targetGroup":"flow","typeId":10003},
					"sniFlow":{"collectInterval":15,"deviceName":"","entityId":30000000023,"entityType":10000,"globalEnable":1,"globalInterval":15,"manageIp":"","perfTargetName":"sniFlow","targetEnable":1,"targetGroup":"flow","typeId":10003}},
					"runningState":{
					"runningState":{"collectInterval":15,"deviceName":"","entityId":30000000023,"entityType":10000,"globalEnable":1,"globalInterval":15,"manageIp":"","perfTargetName":"runningState","targetEnable":1,"targetGroup":"runningState","typeId":10003}}}
		
		
		flagObj.gData = dataStore;//存储数据;
		flagObj.step2 = true;//数据加载好，标记变成true;
		
		var str = '<table cellpadding="0" cellspacing="0" border="0" class="modifyTable" width="100%">';
		for(var key in dataStore){
			var innerData = dataStore[key];
			str += '<thead>';
			str += 		'<tr>';
			str += 			'<th colspan="9"><b class="thTit" alt="'+ key +'">';
			str +=				nm3kBatchData[key].name;			
			str += 			'</b><a href="javascript:;" class="blueArr nm3kTip" nm3kTip="@UnlockAll@"></a><a href="javascript:;" class="redArr nm3kTip" nm3kTip="关闭所有"></a></th>';
			str += 		'</tr>';
			str += '</thead>';
			str += '<tbody>';
			var len = 0;
			var testLen = 0;
			for(var key2 in innerData){len++};//先获取一共有多少个属性;
			var nowNum = 0;
			for(var key2 in innerData){
				testLen ++;
				if(nowNum == 0){
					str += '<tr>';
				}
				nowNum++;
				if(nowNum == 4){
					nowNum = 1;
					str += '<tr>';
				}
				str += '<td class="rightBlueTxt w200">';
				//str += 		'<img src="/images/performance/Help.png" class="tipImg" alt=帮助信息/>';
				str += 		nm3kBatchData[key2].name;
				str += '</td>';
				str += '<td width="100">';
				var inputValue = innerData[key2].collectInterval;
				
				str += 		'<input type="text" class="normalInput" alt="'+ key2 +'" style="width:50px;" value="'+ inputValue +'" /> @Tip.minute@';
				str += '</td>';
				//通过对比，填补空白,如果最后一个不是3，那么他的colspan就该合并一些单元格了;
				if(testLen == len){
					switch(nowNum){
						case 1:
							str += '<td colspan="7">';
							break;
						case 2:
							str += '<td colspan="4">';
							break;
						case 3:
							str += '<td>';
							break;
					}
				}else{
					str += '<td>';
				}
				var isOpen = innerData[key2].targetEnable;
				str += 		renderIsOpenImg(isOpen);
				str += '</td>';
				if(nowNum == 3){
					str += '</tr>';
				};
			};//end for in;
		};//end for in;
		str += '</tobdy>';
		str += '</table>';
		$modifyMainPart.html(str);
	};//end function;
	function renderIsOpenImg(para){
		var str;
		if(para == 1){
			str = '<img src="../../images/performance/on.png" alt="on" class="scrollBtn" />';
		}else{
			str = '<img src="../../images/performance/off.png" alt="off" class="scrollBtn" />';
		}
		return str;
	}
	
	function createColumnChangesButton(){
		 return new Ext.SplitButton({
	     	text: '@Tip.displayPerf@',
	     	menu: new Ext.menu.Menu({
		       	    items: [{
		       	        text:'@Tip.defaultPerf@'
		       	    },{
		       	        text:'@Tip.allPerf@'
		       	    }]
	     	})
		})
	}
	
	// 创建修改页面的顶部工具栏;	
	function createModifyTopToolbar(){
		new Ext.Toolbar({
	        renderTo: "modifyToolBar",
	        items: [
	           {text: '@Tip.back@', iconCls: 'bmenu_back',cls:'mL10', handler: backFn},'-',
	           {text: '@Tip.save@', iconCls: 'bmenu_save', handler: saveModifyOneCmcPerf},
	           "-",
	           {text: '@ApplyToAll@',id:'useToAll', iconCls: 'bmenu_equipment', handler: showApplyToAllCmc},
	           {text: '@Tip.saveAsGlobalConfig@', iconCls: 'bmenu_save', handler: saveAsGolbal},
	           {text: '@Tip.useGolbalConfig@', iconCls: 'bmenu_arrange', handler: useCmcGolbalData},
	           "-",
	           createSetTimeButton()
	           ]
	    });
	}
	
	function isLoadingEndStep2(){
		if( !flagObj.step2 ){
			alert("ThePageIsNotLoadedYet")
			return true;
		}else{
			return false;
		};
	}
	
	//返回按钮;
	function backFn(){
		flagObj.step2 = false;
		$("#modifyDiv").addClass("displayNone");		
	}
	//保存;
	function saveModifyOneCmcPerf(){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		var data = getAllSetData();
		//后台不需要json对象，需要字符串,因此将json解析成包含字符串的数组;
		var arr = [];
		for(var key in data){
			var innerData = data[key];
			for(var key2 in innerData){
				var tempStr = key2 + "_";
				tempStr += innerData[key2].collectInterval + "_";
				tempStr += innerData[key2].targetEnable + "_";
				tempStr += key;
				arr.push(tempStr)
			}
		}
		alert(arr)
	}
	//应用到所有同类型;
	function showApplyToAllCmc(){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		
		var $contrastDiv = $("#contrastDiv");
		$contrastDiv.removeClass("displayNone");
		
		
	}
	function saveAsGolbal(){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		
	}
	function useCmcGolbalData(){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		
	}
	//获取step2中数据给后台，用于保存;
	function getAllSetData(){
		var data = {};
		$("#modifyMainPart .modifyTable thead").each(function(i){
			var $me = $(this);
			var alt = $me.find(".thTit").attr("alt");
			data[alt] = {};
			$tbody = $("#modifyMainPart .modifyTable tbody").eq(i);
			$tbody.find(":text").each(function(){
				var $this = $(this),
				alt2 = $this.attr("alt");
				
				(data[alt])[alt2] = {};
				((data[alt])[alt2])["collectInterval"] = $this.val();
				var enableStr = $this.parent().next().find("img").attr("alt");
				var enable = renderOnOffNumber(enableStr);
				((data[alt])[alt2])["targetEnable"] = enable;
			})
			
		});//end each;
		return data;
	};//end getAllSetData;
	function renderOnOffNumber(para){
		var num;
		if(para == "on"){
			num = 1;
		}else if(para == "off"){
			num = 0;
		}
		return num;
	}
	
	// 创建统一设置时间的按钮组;
	function createSetTimeButton(){
		 return new Ext.SplitButton({
	     	text: '@Tip.UnitedConfigCycle@',
	     	iconCls: 'bmenu_clock', 
	     	handler: function(){this.showMenu()},
	     	menu: new Ext.menu.Menu({
		       	    items: [{
		       	        text:'@Tip.5Min@',
		       	        handler: function(){setAllTime(5)}
		       	    },{
		       	        text:'@Tip.10Min@',
		       	     handler: function(){setAllTime(10)}
		       	    },{
		       	        text:'@Tip.15Min@',
		       	     handler: function(){setAllTime(15)}
		       	    },{
		       	        text:'@Tip.30Min@',
		       	     handler: function(){setAllTime(30)}
		       	    },{
		       	        text:'@Tip.60Min@',
		       	     handler: function(){setAllTime(60)}
		       	    }]
	     	})
		})
	};
	//设置统一时间;
	function setAllTime(para){
		var notLoadingEnd = isLoadingEndStep2();
		if(notLoadingEnd == true){return;}
		$("#modifyMainPart").find("input").val(para);
	};
	
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
	});//end click;
	
	//改变图片按钮;
	function changeOnOff(para){
		var $me = para;
		if($me.attr("alt") == "on"){
			$me.attr({
				"alt" : "off",
				"src" : "../../images/performance/off.png"
			});
		}else{
			$me.attr({
				"alt" : "on",
				"src" : "../../images/performance/on.png"
			});
		}
	};
	//全部开启或去部关闭;
	function openOrCloseAll($me,para){
		var $thead = $me.parent().parent().parent();
		if($thead.next().get(0).tagName == "TBODY"){
			if(para == "on"){
				$thead.next().find(".scrollBtn").attr({
					"alt" : "on",
					"src" : "../../images/performance/on.png"
				})
			}else if(para == "off"){
				$thead.next().find(".scrollBtn").attr({
					"alt" : "off",
					"src" : "../../images/performance/off.png"
				})
			}
		}
	};
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                               //
	//                                             第三部分                                                                                                                 //
	//                                                                                               //
	///////////////////////////////////////////////////////////////////////////////////////////////////	
	
</script>	
</body>
</Zeta:HTML> 
