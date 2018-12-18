<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module univlanprofile
</Zeta:Loader>
<style type="text/css">
#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:450px;}
#w1600{ width:1580px; position:absolute; top:0; left:0;}
#step0, #step1{width:790px; overflow:hidden; position:absolute; top:0px; left:0px;}
#step1{ left:790px;}
body,html{height:100%; overflow:hidden;}
.openLayerRule{ width:100%; height:100%; overflow:hidden; position:absolute; top:0;left:0;  display:none; z-index:2;}
.openLayerRuleMain{ width:500px; height:270px; overflow:hidden; position:absolute; top:120px; left:140px; z-index:2;  background:#F7F7F7;}
.openLayerRuleBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
</style>
<script type="text/javascript">
	//规则索引
	var indexArray = [1,2,3,4,5,6,7,8];
	var arrayCopy = [];
	var entityId = '${entityId}';
	var profileIndex = '${profileIndex}';
	var profileMode = '${profileMode}';
	var profileRefCnt = '${profileRefCnt}';
	
	//关闭当前页面
	function closeBtClick(){
		window.parent.closeWindow('profileRule');
	}
	
	//显示添加规则的页面
	var hasGo = false;
	function showAddRule(){
		if(dataStore.getCount() < 8){
			//需要进入到不同的页面
			if(profileMode == '3'){
				$("#ruleIdInput").val("");
				$("#beforeVlan").val("");
				$("#afterVlan").val("")
				//显示添加转换规则页面
				$("#addTransLateLayer").fadeIn();
			}else if(profileMode == '4'){
				//进入聚合规则配置页面
				$("#w1600").animate({left:-790});
				if(!hasGo){
					hasGo = true;
					$("#frameInner").attr("src","/epon/univlanprofile/showAggRule.tv");
				}
			}else{
				//显示添加Trunk规则页面
				$("#trunkRuleId").val("");
				$("#trunkId").val("");
				$("#addTrunkLayer").fadeIn();
			}
		}else{
			window.top.showMessageDlg("@COMMON.tip@", "@RULE.totalLimit@");
			return;
		}
	}
	//关闭转换规则添加页面
	function closeTransLateLayer(){
		$("#addTransLateLayer").fadeOut();
	}
	//关闭Trunk规则添加页面
	function closeTrunkLayer(){
		$("#addTrunkLayer").fadeOut();
	}
	//输入校验: 输入的整数否在范围内
	function checkInput(value,compareStart,compareEnd){
		var reg = /^[1-9]\d*$/;	
		if (reg.exec(value) && parseInt(value) <= compareEnd && parseInt(value) >= compareStart) {
			return true;
		} else {
			return false;
		}
	}
	
	//用户输入时检查规则Id是否可用
	function blurFn(){
		var ruleId = $("#ruleIdInput").val().trim();
		if(!checkInput(ruleId,1,8)){
			$("#errorTip").text("");
			$("#ruleIdInput").focus();
			return;
		}
		if($.inArray(parseInt(ruleId), arrayCopy) > -1){
			$("#errorTip").text("");
		}else{
			$("#errorTip").text("@RULE.ruleIdUsed@");
			$("#ruleIdInput").focus();
		}
	}
	
	//添加转换规则
	function addTransLate(){
		var ruleId = $("#ruleIdInput").val(); 
		var beforeVlan = $("#beforeVlan").val().trim();
		var afterVlan = $("#afterVlan").val().trim();
		if(!checkInput(ruleId,1,8)){  //输入校验 1-8之间的整数
			$("#ruleIdInput").focus();
			return;
		}
		if($.inArray(parseInt(ruleId), arrayCopy) == -1){
			$("#ruleIdInput").focus();
			return;
		}
		if(!checkInput(beforeVlan,1,4094)){
			$("#beforeVlan").focus();
			return;
		}
		if(!checkInput(afterVlan,1,4094)){
			$("#afterVlan").focus();
			return;
		}
		//Vlan ID 不能在其它规则中出现
		var cVlanRepeat = false;
		var sVlanRepeat = false;
		dataStore.each(function(rec){
			if(beforeVlan == rec.data.cVlanData){
				cVlanRepeat = true;
				return false;
			}
			if(afterVlan == rec.data.ruleSvlan){
				sVlanRepeat = true;
				return false;
			}
		});
		//原VLAN　ID不能在其它规则中出现
		if(cVlanRepeat){
			window.top.showMessageDlg("@COMMON.tip@", "@RULE.originalVlanUsed@",null,function(){
				$("#beforeVlan").focus();
			});
			return;
		}
		//转换后VLAN ID不能在其它规则中出现
		if(sVlanRepeat){
			window.top.showMessageDlg("@COMMON.tip@", "@RULE.targetVlanUsed@",null,function(){
				$("#afterVlan").focus();
			});
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/univlanprofile/addProfileRule.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				profileIndex : profileIndex,
				ruleIndex : ruleId,
				ruleMode : profileMode,
				ruleCvlan : beforeVlan,
				ruleSvlan : afterVlan
			},
			success : function() {
				//window.top.showMessageDlg("@COMMON.tip@", "@RULE.saveSuccess@");
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@RULE.saveSuccess@</b>'
       			});
				closeTransLateLayer();
				dataStore.reload();
			},
			error : function() {
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.saveFailed@");
			},
			cache : false
		}); 		
	}
	
	//Trunk模式下用户输入时检查规则Id是否可用
	function trunkBlurFn(){
		var ruleId = $("#trunkRuleId").val().trim();
		if(!checkInput(ruleId,1,8)){
			$("#trunkErrorTip").text("");
			$("#trunkRuleId").focus();
			return;
		}
		if($.inArray(parseInt(ruleId), arrayCopy) > -1){
			$("#trunkErrorTip").text("");
		}else{
			$("#trunkErrorTip").text("@RULE.ruleIdUsed@");
			$("#trunkRuleId").focus();
		}
	}
	
	//添加Trunk规则
	function addTrunk(){
		var ruleId = $("#trunkRuleId").val();
		var trunkId = $("#trunkId").val().trim();
		if(!checkInput(ruleId,1,8)){  //输入校验 1-8之间的整数
			$("#trunkRuleId").focus();
			return;
		}
		if($.inArray(parseInt(ruleId), arrayCopy) == -1){
			$("#trunkRuleId").focus();
			return;
		}
		if(!checkInput(trunkId,1,4094)){
			$("#trunkId").focus();
			return;
		}
		var trunkRepeat = false;
		dataStore.each(function(rec){
			if(trunkId == rec.data.cVlanData){
				trunkRepeat = true;
				return false;
			}
		});
		if(trunkRepeat){
			window.top.showMessageDlg("@COMMON.tip@", "@RULE.trunkIdUsed@",null,function(){
				$("#trunkId").focus();
			});
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/univlanprofile/addProfileRule.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				profileIndex : profileIndex,
				ruleIndex : ruleId,
				ruleMode : profileMode,
				ruleCvlan : trunkId
			},
			success : function() {
				//window.top.showMessageDlg("@COMMON.tip@", "@RULE.saveSuccess@");
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@RULE.saveSuccess@</b>'
       			});
				closeTrunkLayer();
				dataStore.reload();
			},
			error : function() {
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.saveFailed@");
			},
			cache : false
		}); 		
	}
	
	/**
	 * 返回frame的父页面
	 */
	function closeFrame(){
		$("#w1600").animate({left:0},'normal');	
	}
	
	//刷新数据
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/univlanprofile/refershRuleData.tv',
			type : 'POST',
			data : {
				entityId : entityId
			},
			success : function() {
				//window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       			});
				dataStore.reload();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
			},
			cache : false
		});
	}
	
	//模板Vlan模式转换
	function modeRenderer(value, cellmate, record){
		switch(value){
			case 0 : return "@PROFILE.modeNone@"; break;
			case 1 : return "@PROFILE.modeTransparent@"; break;
			case 2 : return "@PROFILE.modeTag@"; break;
			case 3 : return "@PROFILE.modeTranslate@"; break;
			case 4 : return "@PROFILE.modeAgg@"; break;
			case 5 : return "@PROFILE.modeTrunk@"; break;
		}
	}
	
	//grid中操作栏处理
	function opeartionRender(value, cellmate, record){
		var index = record.data.ruleIndex;
		return String.format("<a href='javascript:;' onClick='deleteRule({0})'>@COMMON.delete@</a>",index);
	}
	
	//删除规则
	function deleteRule(index){
		window.parent.showConfirmDlg("@COMMON.tip@", "@RULE.deleteRule@", function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/univlanprofile/deleteProfileRule.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					profileIndex :　profileIndex,
					ruleIndex : index
				},
				success : function() {
					//window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.deleteSuccess@");
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@PROFILE.deleteSuccess@</b>'
	       			});
					dataStore.reload();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.deleteFailed@");
				},
				cache : false
			});
		});
	}
	
	$(document).ready(function(){
		if(parseInt(profileRefCnt) > 0){ //如果引用次数大于0,添加规则按钮不显示
			$("#addRuleBtn").css("display","none");
		}
		window.dataStore = new Ext.data.JsonStore({
			url : '/epon/univlanprofile/loadProfileRuleList.tv',
			fields : ["ruleIndex", "ruleMode","cVlanData","ruleSvlan"],
			baseParams : {
				entityId : entityId,
				profileIndex : profileIndex
			},
			listeners : {
				load : function(s,records, options) {
					arrayCopy = indexArray.concat();
					$.each(records,function(i,record){
						if($.inArray(record.data.ruleIndex, arrayCopy) > -1){
							arrayCopy.remove( record.data.ruleIndex );
						}
					});
		        } 
			}
		});
		
		if(parseInt(profileMode) == 5){  //mode为trunk的情况
			if(parseInt(profileRefCnt) > 0){  //引用次数大于0时不能删除
				window.colModel = new Ext.grid.ColumnModel([ 
	      		 	{header : "@RULE.ruleId@",width : 50,align : 'center',dataIndex : 'ruleIndex'}, 
	      		 	{header : "@RUlE.trunkId@",width : 150,align : 'center',dataIndex : 'cVlanData'}, 
	      		 	{header : "@RULE.vlanMode@",width : 100,align : 'center',dataIndex : 'ruleMode', renderer : modeRenderer}
	      		]);
			}else{
				window.colModel = new Ext.grid.ColumnModel([ 
					{header : "@RULE.ruleId@",width : 50,align : 'center',dataIndex : 'ruleIndex'}, 
	    		 	{header : "@RUlE.trunkId@",width : 150,align : 'center',dataIndex : 'cVlanData'}, 
	    		 	{header : "@RULE.vlanMode@",width : 100,align : 'center',dataIndex : 'ruleMode', renderer : modeRenderer}, 
	   		 		{header : "@COMMON.manu@",width : 80, align : 'center',dataIndex :'op', renderer : opeartionRender}
	    		]);
			}
		}else if(parseInt(profileMode) == 4){  //mode为聚合的情况
			if(parseInt(profileRefCnt) > 0){  //引用次数大于0时不能删除
				window.colModel = new Ext.grid.ColumnModel([ 
					{header : "@RULE.ruleId@",width : 50,align : 'center',dataIndex : 'ruleIndex'},                                         
           		 	{header : "@RULE.originalAgg@",width : 150,align : 'center',dataIndex : 'cVlanData'}, 
           		 	{header : "@RULE.targetAgg@",width : 100,align : 'center',dataIndex : 'ruleSvlan'}, 
           		 	{header : "@RULE.vlanMode@",width : 100,align : 'center',dataIndex : 'ruleMode', renderer : modeRenderer} 
           		]);
			}else{
				window.colModel = new Ext.grid.ColumnModel([ 
                    {header : "@RULE.ruleId@",width : 50,align : 'center',dataIndex : 'ruleIndex'},                
	      		 	{header : "@RULE.originalAgg@",width : 150,align : 'center',dataIndex : 'cVlanData'}, 
	      		 	{header : "@RULE.targetAgg@",width : 100,align : 'center',dataIndex : 'ruleSvlan'}, 
	      		 	{header : "@RULE.vlanMode@",width : 100,align : 'center',dataIndex : 'ruleMode', renderer : modeRenderer}, 
	      		 	{header : "@COMMON.manu@",width : 80, align : 'center',dataIndex :'op', renderer : opeartionRender}
	      		]);
			}
		}else if(parseInt(profileMode) == 3){  //mode为转换的情况
			if(parseInt(profileRefCnt) > 0){  //引用次数大于0时不能删除
				window.colModel = new Ext.grid.ColumnModel([ 
					{header : "@RULE.ruleId@",width : 50,align : 'center',dataIndex : 'ruleIndex'},                                         
           		 	{header : "@RULE.originalId@",width : 150,align : 'center',dataIndex : 'cVlanData'}, 
           		 	{header : "@RULE.targetId@",width : 100,align : 'center',dataIndex : 'ruleSvlan'}, 
           		 	{header : "@RULE.vlanMode@",width : 100,align : 'center',dataIndex : 'ruleMode', renderer : modeRenderer} 
           		]);
			}else{
				window.colModel = new Ext.grid.ColumnModel([ 
                    {header : "@RULE.ruleId@",width : 50,align : 'center',dataIndex : 'ruleIndex'},                
                    {header : "@RULE.originalId@",width : 150,align : 'center',dataIndex : 'cVlanData'}, 
           		 	{header : "@RULE.targetId@",width : 100,align : 'center',dataIndex : 'ruleSvlan'}, 
	      		 	{header : "@RULE.vlanMode@",width : 100,align : 'center',dataIndex : 'ruleMode', renderer : modeRenderer}, 
	      		 	{header : "@COMMON.manu@",width : 80, align : 'center',dataIndex :'op', renderer : opeartionRender}
	      		]);
			}
		}
		
		window.profileRuleGrid =  new Ext.grid.GridPanel({
			id : 'profileRuleGrid',
			title : "@RULE.ruleList@",
			height : 390,
			width: 770,
			border : true,
			cls : 'normalTable',
			store : dataStore,
			colModel : colModel,
			viewConfig : {
				forceFit : true
			},
			renderTo : 'contentGrid',
			sm : new Ext.grid.RowSelectionModel({
				singleSelect : true
			})
		});		
		dataStore.load();
		
	});
</script>
</head>

<body class="openWinBody">
	<div id="w800">
		<div id="w1600">
			<div id="step0" style="margin: 10px 15px;">
				<div id="contentGrid"></div>
				
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a  onclick='refreshData()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
				         <li><a  id="addRuleBtn" onclick='showAddRule()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@RULE.addRule@</span></a></li>
				         <li><a  onclick='closeBtClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
			
			<div id="step1">
				<iframe id="frameInner" name="frameInner" src="" style="width:100%; height:450px;" frameborder="0"></iframe>
			</div>
		</div>
	</div>
	<!-- 添加转换规则 -->
	<div class="openLayerRule" id="addTransLateLayer">
		<div class="openLayerRuleMain">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@RULE.addTranslateRule@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				         	<tr>
				             	<td class="rightBlueTxt" width="140">
				                	@RULE.ruleId@:
								</td>
								<td width="186">
									<input type="text" id="ruleIdInput" class="normalSel w180" toolTip="@RULE.ruleIdTip@" onblur="blurFn()"/>
								</td>
								<td>
									<b id="errorTip" class="orangeTxt"></b>
								</td>
							</tr>
				            <tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	@RULE.originalId@:
								</td>
								<td>
									<input type="text" id="beforeVlan" class="normalInput w180" maxlength="4" toolTip="@RULE.vlanInputTip@"/>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@RULE.targetId@:
								</td>
								<td>
									<input type="text" id="afterVlan" class="normalInput w180" maxlength="4" toolTip="@RULE.vlanInputTip@"/>
								</td>
								<td></td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="addTransLate()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeTransLateLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerRuleBg"></div>
	</div>
	<!-- 添加TRUNK规则 -->
	<div class="openLayerRule" id="addTrunkLayer">
		<div class="openLayerRuleMain">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@RULE.addTrunkRule@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				         	<tr>
				             	<td class="rightBlueTxt" width="140">
				                	@RULE.ruleId@:
								</td>
								<td width="186">
									<input type="text" id="trunkRuleId" class="normalSel w180" toolTip="@RULE.ruleIdTip@" onblur="trunkBlurFn()"/>
								</td>
								<td>
									<b id="trunkErrorTip" class="orangeTxt"></b>
								</td>
							</tr>
				            <tr class="darkZebraTr">
				                <td class="rightBlueTxt withoutBorderBottom" width="140">
				                	@RUlE.trunkId@:
								</td>
								<td class="withoutBorderBottom">
									<input type="text" id="trunkId" class="normalInput w180" maxlength="4" toolTip="@RULE.vlanInputTip@"/>
								</td>
								<td></td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="addTrunk()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeTrunkLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerRuleBg"></div>
	</div>
</body>
</Zeta:HTML>