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
    css css/white/disabledStyle
    import epon.uniportvlan.uniPortVlan
</Zeta:Loader>
<style type="text/css">
body,html{height:100%; overflow:hidden;}
#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:450px;}
#w1600{ width:1580px; position:absolute; top:0; left:0;}
#step0, #step1{width:780px; overflow:hidden; position:absolute; top:0px; left:0px;}
#step1{ left:800px;}
.contentGrip{ width:780px; margin-top:10px;}
.openLayerRule{ width:100%; height:100%; overflow:hidden; position:absolute; top:0;left:0;  display:none; z-index:2;}
.openLayerRuleMain{ width:500px; height:270px; overflow:hidden; position:absolute; top:120px; left:140px; z-index:2;  background:#F7F7F7;}
.openLayerRuleBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
.openLayerOuter{width: 100%; height: 100%;  overflow: hidden;   position: absolute;   top: 0;   left: 0; display:none;}
.openLayerBg{width: 100%; height: 100%; overflow: hidden; background: #F7F7F7; position: absolute; top: 0; left: 0; opacity: 0.8; filter: alpha(opacity=85);}
.openLayerMainDiv{ width: 560px; overflow: hidden; position: absolute;  top: 100px;  left: 120px;  z-index: 2;  background: #F7F7F7;}

</style>
<script type="text/javascript">
var entityId = '${entityId}';
var uniId = '${uniId}';
var uniIndex = '${uniPortVlan.portIndex}';
var vlanMode = '${uniPortVlan.vlanMode}';
var uniPvid = '${uniPortVlan.vlanPVid}';
var profileId = '${uniBindInfo.bindProfileId}';
var bindProfAttr = '${uniBindInfo.bindProfAttr}';
var profileName = '${uniVlanProfile.profileName}';

var	aggStore, aggColModel, aggGrid;
var ruleStore, ruleColModel, ruleGrid;

//记录配置的聚合规则中的VLAN数据
var aggVlanData = {
	aggCvlanArr : [],
	aggSvlanArr : []
}

$(function(){
	if(profileId > 0){
		$("#vlanProfile").text(profileName);
	}else{
		$("#vlanProfile").text("@UNIVLAN.unRelated@");
	}
	
	var $pvid = $("#pvid");	
	var $savePvid = $("#savePivd");
	//点击pvid后面的保存按钮;
	$savePvid.click(function(){
		//先执行验证
		if(!checkInput($pvid.val(), 1, 4094)){
			$pvid.focus();
			return;
		}
		//修改PVID
		modifyUniPvid($pvid.val());
	});
	
	aggStore = new Ext.data.JsonStore({
		url : '/epon/uniportvlan/loadAggregationRule.tv',
		fields : ["aggregationVidListAfterSwitch", "portAggregationVidIndex"],
		baseParams : {
			entityId : entityId,
			uniIndex : uniIndex,
			uniId : uniId
		}
	});
	aggColModel = new Ext.grid.ColumnModel([
	 	{header : "@RULE.originalAgg@", align : 'center', dataIndex : 'aggregationVidListAfterSwitch'},
	 	{header : "@RULE.targetAgg@", align : 'center', dataIndex : 'portAggregationVidIndex'},
	 	{header : "@COMMON.manu@", width:160, fixed:true, align : 'center',dataIndex :'op', renderer : opeartionRender}
	]);
	aggGrid =  new Ext.grid.GridPanel({
		title : "@RULE.ruleList@",
		height : 180,
		border : true,
		cls : 'normalTable',
		store : aggStore,
		colModel : aggColModel,
		viewConfig : {
			forceFit : true
		},
		renderTo : 'contentGrid'
	});	
	aggStore.load();
	
	//规则添加相关
	ruleStore = new Ext.data.JsonStore({
		data : [],
		fields : ["ruleCvlan", "ruleSvlan"]
	});
	ruleColModel = new Ext.grid.ColumnModel([ 
	   	{header : "@RULE.originalAgg@", width : 120, align : 'center',dataIndex : 'ruleCvlan'}, 
	 	{header : "@RULE.targetAgg@", width : 120, align : 'center',dataIndex : 'ruleSvlan'}, 
	 	{header : "@COMMON.manu@", width : 100, align : 'center',dataIndex :'op', renderer : ruleOperRender}
	]);
	
	ruleGrid =  new Ext.grid.GridPanel({
		id : 'aggRuleGrid',
		title : "@RUlE.aggDataList@",
		height : 320,
		border : true,
		cls : 'normalTable',
		store : ruleStore,
		colModel : ruleColModel,
		viewConfig : {
			forceFit : true
		},
		renderTo : 'putGrid',
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		})
	});	

});//end document.ready;

//显示添加聚合规则页面
function showAddRule(){
	if(aggStore.getCount() < 8){
		$('#sVlanInput').attr("disabled", false);
		$('#cVlanInput, #sVlanInput').val("");
		ruleStore.removeAll();
		//记录已经配置的聚合VLAN数据
		collectAggVlanData();
		$("#w1600").animate({left:-790});	
	}else{
		window.top.showMessageDlg("@COMMON.tip@", "@RULE.totalLimit@");
		return;
	}
}
//返回;
function closeAddRule(){
	$("#w1600").animate({left:0});
}
//关闭;
function closeOpenLayer(){
	$("#openLayerOuter").stop().fadeOut();
}

function opeartionRender(value, cellmate, record){
	var aggrSvid = record.data.portAggregationVidIndex;
	return String.format("<a href='javascript:;' onClick='deleteAggRule({0})'>@COMMON.delete@</a> / <a href='javascript:;' onClick='showUpdateRule()'>@COMMON.modify@</a>",aggrSvid); 
}

//删除AGG规则
function deleteAggRule(aggrSvid){
	window.parent.showConfirmDlg("@COMMON.tip@", "@RULE.deleteRule@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/uniportvlan/deleteAggregationRule.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				uniIndex : uniIndex,
				uniId : uniId,
				aggrSvid : aggrSvid
			},
			dataType :　'json',
			success : function(result) {
				if(result.success){
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@PROFILE.deleteSuccess@</b>'
	       			});
					aggStore.reload();
				}else{
					window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.deleteFailed@");
				}
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.deleteFailed@");
			},
			cache : false
		});
	});
}

//收集已经配置的聚合规则中VLAN数据
function collectAggVlanData(){
	//每次使用前将保存的数据重置,防止受到之前数据的影响
	aggVlanData.aggCvlanArr = [];
	aggVlanData.aggSvlanArr = [];
	//收集当前数据
	aggStore.each(function(rec){
		var aggVidList = rec.data.aggregationVidListAfterSwitch;
		$.each(aggVidList, function(index, cVlan){
			aggVlanData.aggCvlanArr.push(cVlan);
		})
		aggVlanData.aggSvlanArr.push(rec.data.portAggregationVidIndex);
	});
}

//显示聚合规则的修改
var updateFlag = false;
function showUpdateRule(){
	//获取要修改的数据
	var selectedAgg = aggGrid.getSelectionModel().getSelected();
	var aggVidList = selectedAgg.data.aggregationVidListAfterSwitch;
	var aggVidIndex = selectedAgg.data.portAggregationVidIndex;
	var aggRuleArr = [];
	$.each(aggVidList, function(index, aggVid){
		var rule = {ruleCvlan:aggVid, ruleSvlan:aggVidIndex};
		aggRuleArr.push(rule);
	})
	//显示修改页面
	$('#cVlanInput').val("");
	$('#sVlanInput').val(aggVidIndex).attr("disabled", true);
	ruleStore.removeAll();
	$("#w1600").animate({left:-790});
	//加载要修改的数据
	ruleStore.loadData(aggRuleArr);
	//记录已经配置的聚合VLAN数据
	collectAggVlanData();
	//标记操作为修改
	updateFlag = true;
}

//切换模式,0为切换模式，1为添加规则;
function showChangeMode(){
	$("#pvid2").val($("#pvid").val());
	$("#openLayerOuter").stop().fadeIn();
}

//检查cVlan输入
function checkCvlanInput(v){
	var reg = /^([0-9]{1,4}[,-]{0,1})+$/;
    if (reg.exec(v)) {
    	var tmp = v.replace(new RegExp('-', 'g'), ',');
    	var tmpA = tmp.split(',');
    	var tmpAl = tmpA.length;
		for(var i=0; i<tmpAl; i++){
			if(parseInt(tmpA[i]) > 4094 || parseInt(tmpA[i]) < 1){
				return false;
			}
		}
    	return true;
    }
    return false;
}
//解析逗号和连字符组成的字符串为数组
function changeToArray(v){
	var re = new Array();
	var t = v.split(",");
	var tl = t.length;
	for(var i=0; i<tl; i++){
		var tt = t[i];
		var ttI = tt.indexOf("-");
		if(ttI > 0){
			var ttt = tt.split("-");
			if(ttt.length == 2){
				var low = parseInt(parseInt(ttt[0]) > parseInt(ttt[1]) ? ttt[1] : ttt[0]);
				var tttl = Math.abs(parseInt(ttt[0]) - parseInt(ttt[1]));
				for(var u=0; u<tttl + 1; u++){
					re.push(low + u);
				}
			}
		}else if(ttI == -1){
			re.push(parseInt(tt));
		}
	}
	var rel = re.length;
	if(rel > 1){
		var o = {};
		for(var k=0; k<rel; k++){
			o[re[k]] = true;
		}
		re = new Array();
		for(var x in o){
			if (x > 0 && o.hasOwnProperty(x)) {
				re.push(parseInt(x)); 
			} 
		}
		re.sort(function(a, b){
			return a - b;
		});
	}
	return re;
}

//向grid中添加聚合规则
function addAggRule(){
	var cVlanData = $('#cVlanInput').val().trim();
	var sVlanData = $('#sVlanInput').val().trim();
	if(ruleStore.getCount() < 8){
		if(!checkCvlanInput(cVlanData)){
			$('#cVlanInput').focus();
			return;
		}
		var cvlanList = changeToArray(cVlanData);
		if(!checkInput(sVlanData,1,4094)){
			$('#sVlanInput').focus();
			return;
		}
		if(ruleStore.getCount() + cvlanList.length > 8){
			window.top.showMessageDlg("@COMMON.tip@", "@RULE.totalAggLimit@");
			return;
		}
		//先判断Vlan id在其它规则里有没有出现过
		var cVlanRepeat = false;
		var existsCvlan = aggVlanData.aggCvlanArr;
		$.each(cvlanList,function(index, newCvlan){
			if($.inArray(newCvlan, existsCvlan) > -1){
				cVlanRepeat = true;
				return false;
			}
		});
		//原VLAN　ID不能在其它规则中出现
		if(cVlanRepeat){
			window.top.showMessageDlg("@COMMON.tip@", "@RULE.originalAggUsed@",null,function(){
				$('#cVlanInput').focus();
			});
			return;
		}
		//添加时还需要校验SVLAN
		if(!updateFlag){
			var sVlanRepeat = false;
			var existsSvlan = aggVlanData.aggSvlanArr;
			//防止string类型与Number类型的比较
			var svlanValue = parseInt(sVlanData, 10);
			if($.inArray(svlanValue, existsSvlan) > -1){
				sVlanRepeat = true;
			}
			//转换后VLAN ID不能在其它规则中出现
			if(sVlanRepeat){
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.targetAggUsed@",null,function(){
					$('#sVlanInput').focus();
				});
				return;
			}
		}
		var addRecord = new Array();
		$.each(cvlanList,function(i,item){
			var toAdd = {ruleCvlan:item, ruleSvlan:sVlanData};
			addRecord.push(toAdd);
			//记录新添加的aggCvlan
			aggVlanData.aggCvlanArr.push(item);
		});
		ruleStore.loadData(addRecord,true);
		//添加成功后将cVlan输入框置空
		$('#cVlanInput').val("").focus();
		if(!updateFlag){
			//添加成功后将sVlan输入置为不可用
			$('#sVlanInput').attr("disabled", true);
		}
	}else{
		window.top.showMessageDlg("@COMMON.tip@", "@RULE.totalAggLimit@");
		return;
	}
}

//规则添加grid中操作栏处理
function ruleOperRender(value, cellmate, record){
	return String.format("<a href='javascript:;' onClick='deleteRule()'>@COMMON.delete@</a>");
}
//从规则添加grid中删除条目
function deleteRule(){
	var select = ruleGrid.getSelectionModel().getSelected();
	ruleStore.remove(select);
	//删除配置规则时从记录中删除cvlan
	aggVlanData.aggCvlanArr.remove(select.data.ruleCvlan);
	if(!updateFlag && ruleStore.getCount() == 0){
		$('#sVlanInput').attr("disabled", false);
	}
}

//应用聚合规则
function saveAggRule(){
	if(ruleStore.getCount() > 0){
		var cVlan = new Array();
		var sVlan;
		ruleStore.each(function(rec){
			cVlan.push(rec.data.ruleCvlan);
			sVlan = rec.data.ruleSvlan;
		});
		var actionUrl = '/epon/uniportvlan/addAggregationRule.tv';
		if(updateFlag){
			actionUrl = '/epon/uniportvlan/modifyAggregationRule.tv';
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : actionUrl,
			type : 'POST',
			data : {
				entityId : entityId,
				uniIndex : uniIndex,
				uniId : uniId,
				aggrCvids : cVlan.join(","),
				aggrSvid : sVlan
			},
			dataType :　'json',
			success : function(result) {
				if(result.success){
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@RULE.saveSuccess@</b>'
	       			});
					aggStore.reload();
					closeAddRule();
				}else{
					window.top.showMessageDlg("@COMMON.tip@", "@RULE.saveFailed@");
				}
			},
			error : function() {
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.saveFailed@");
			},
			cache : false
		}); 
	}else{
		window.top.showMessageDlg("@COMMON.tip@", "@RULE.noRuleAdded@");
		return;
	}
}

</script>
</head>
<body class="openWinBody">
	<div id="w800">
		<div id="w1600">
			<div id="step0" style="margin:20px 10px 10px 10px;">
					<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			         <tbody>
			             <tr>
			                 <td class="rightBlueTxt">@epon/VLAN.portNum@:</td>
			                 <td colspan="2">
								${uniPortVlan.portString}
			                 </td>
			                 <td class="rightBlueTxt">PVID:</td>
			                 <td>
								<input type="text" id="pvid" class="normalInput w180" value="${uniPortVlan.vlanPVid}" maxlength="4" tooltip="@RULE.vlanInputTip@" />
			                 </td>
			                 <td>
			                 	<a id="savePivd" href="javascript:;" class="normalBtn"><span><i class="miniIcoSaveOK"></i>@BUTTON.apply@</span></a>
			                 </td>
			             </tr>
			             <tr class="darkZebraTr">
			                <td class="rightBlueTxt" width="100">@PROFILE.vlanMode@:</td>
			             	<td width="70">
			             		@PROFILE.modeAgg@
			             	</td>
			             	<td width="120">
			             		<a href="javascript:;" class="normalBtn" onclick="showChangeMode(0)"><span><i class="miniIcoTwoArrOut"></i>@UNIVLAN.changeMode@</span></a>
			             	</td>
			                <td class="rightBlueTxt" width="100">@UNIVLAN.profile@:</td>
			                 <td width="120">
			                 	<span class="longTxt wordBreak" id="vlanProfile"> </span>
			                 </td> 
			                 <td>
			                 	<a href="javascript:;" class="normalBtn" onclick="showVlanProfile()"><span><i class="miniIcoTwoArr"></i>@UNIVLAN.viewProfile@</span></a>
			                 </td>
			             </tr> 
			         </tbody>
			     </table>
				<div id="contentGrid" class="contentGrip"></div>
				<a id="addRuleBtn" onclick='showAddRule()' href="javascript:;" class="normalBtn mT5 mB5"><span><i class="miniIcoAdd"></i>@RULE.addRule@</span></a>
				<div class="clearBoth yellowTip" style="padding:10px">
					<p>@UNIVLAN.aggTip1@</p> 
					<p>@UNIVLAN.aggTip2@</p>
					<p>@UNIVLAN.aggTip3@</p>
				</div>
				
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a  onclick='refreshUniVlanData()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
				         <li><a  onclick='cancelClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
			
			<div id="step1">
				<div class="edge0 pT20">
					<table class="zebraTableRows" >
						<tr class="withoutBorderBottom">
							<td class=" rightBlueTxt">@RULE.originalAgg@:<font color=red>*</font></td>
							<td>
								<input type="text" id="cVlanInput" class="normalInput w180" toolTip="@RUlE.batchAddTip@"/>
							</td>
							<td class=" rightBlueTxt">@RULE.targetAgg@:<font color=red>*</font></td>
							<td width="190">
								<input type="text" id="sVlanInput" class="normalInput w180" toolTip="@RULE.vlanInputTip@"/>
							</td>
							<td class=""><a href="javascript:;" class="normalBtn" onclick="addAggRule()"> <span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></td>
						</tr>
					</table>
					<div id="putGrid" class="mT10"></div>
				</div>
				
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a  onclick='saveAggRule()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
				         <li><a  onclick='closeAddRule()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@RUlE.goBack@</span></a></li>
				     </ol>
				</div>
			</div>
			
		</div>
	</div>
	
	<div class="openLayerOuter" id="openLayerOuter">
		<div class="openLayerMainDiv">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@UNIVLAN.changeMode@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				             <tr>
				             	<td class="rightBlueTxt" width="160">
				                	@epon/VLAN.portNum@:
								</td>
								<td>
									<span>${uniPortVlan.portString}</span>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	PVID:
								</td>
								<td>
									<input type="text" id="pvid2" class="normalInput w180" value="" maxlength="4" tooltip="@RULE.vlanInputTip@" />
								</td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@PROFILE.vlanMode@:
								</td>
								<td>
									<select id="modeUpdate" class="normalSel w180">
										<option value="0">@PROFILE.modeTransparent@</option>
										<option value="1">@PROFILE.modeTag@</option>
										<option value="2">@PROFILE.modeTranslate@</option>
										<option value="3" selected="selected">@PROFILE.modeAgg@</option>
										<option value="4">Trunk</option>
									</select>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="modifyVlanMode()"><span><i class="miniIcoSaveOK"></i>@BUTTON.apply@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeOpenLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerBg"></div>
	</div>
</body>
</Zeta:HTML>