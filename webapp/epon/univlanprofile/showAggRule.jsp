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

</style>
<script type="text/javascript">
	var entityId = window.parent.entityId;
	var profileIndex = window.parent.profileIndex;
	var ruleMode = window.parent.profileMode;

	//关闭当前Iframe,返回到规则管理页面
	function cancelClick(){
		window.parent.closeFrame();
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
	
	//向grid中添加条目
	function addClick(){
		var cVlanData = $('#cVlanInput').val();
		var sVlanData = $('#sVlanInput').val();
		if(dataStore.getCount() < 8){
			/* if(!checkInput(cVlanData,1,4094)){
				$('#cVlanInput').focus();
				return;
			} */
			if(!checkCvlanInput(cVlanData)){
				$('#cVlanInput').focus();
				return;
			}
			var cvlanList = changeToArray(cVlanData);
			if(!checkInput(sVlanData,1,4094)){
				$('#sVlanInput').focus();
				return;
			}
			if(dataStore.getCount()+cvlanList.length > 8){
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.totalAggLimit@");
				return;
			}
			//先判断Vlan id在其它规则里有没有出现过
			var cVlanRepeat = false;
			var sVlanRepeat = false;
			window.parent.dataStore.each(function(rec){
				$.each(cvlanList,function(i,item){
					if(rec.data.cVlanData.indexOf(item) > -1){
						cVlanRepeat = true;
						return false;
					}
				});
				if(sVlanData == rec.data.ruleSvlan){
					sVlanRepeat = true;
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
			//转换后VLAN ID不能在其它规则中出现
			if(sVlanRepeat){
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.targetAggUsed@",null,function(){
					$('#sVlanInput').focus();
				});
				return;
			}
			//聚合前Vlan不能相同且必须聚合到同一个Vlan
			var sVlanChange = false;
			var repeatFlag = false;
			dataStore.each(function(rec){
				//必须聚合到同一个sVlan
				if(sVlanData != rec.data.ruleSvlan){
					sVlanChange = true;
					return false;
				}
				//不能重复
				$.each(cvlanList,function(i,item){
					if(item == rec.data.ruleCvlan){
						repeatFlag = true;
						return false;
					}
				});
				
			});
			if(sVlanChange){
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.sameTargetAgg@");
				return;
			}
			if(repeatFlag){
				window.top.showMessageDlg("@COMMON.tip@", "@RULE.sameAggData@");
				return;
			}
			var addRecord = new Array();
			$.each(cvlanList,function(i,item){
				var toAdd = {ruleCvlan:item, ruleSvlan:sVlanData};
				addRecord.push(toAdd);
			});
			dataStore.loadData(addRecord,true);
			//添加成功后将cVlan输入框置空
			$('#cVlanInput').val("");
		}else{
			window.top.showMessageDlg("@COMMON.tip@", "@RULE.totalAggLimit@");
			return;
		}
	}
	
	//grid中操作栏处理
	function opeartionRender(value, cellmate, record){
		return String.format("<a href='javascript:;' onClick='deleteAgg()'>@COMMON.delete@</a>");
	}
	//从grid中删除条目
	function deleteAgg(){
		var select = aggGrid.getSelectionModel().getSelected();
		dataStore.remove(select);
	}
	
	//用户输入时检查规则Id是否可用
	function aggBlurFn(){
		var usableIndex = window.parent.arrayCopy;
		var ruleId = $("#aggRuleId").val().trim();
		if(!checkInput(ruleId,1,8)){
			$("#aggErrorTip").text("");
			$("#aggRuleId").focus();
			return;
		}
		if($.inArray(parseInt(ruleId), usableIndex) > -1){
			$("#aggErrorTip").text("");
		}else{
			$("#aggErrorTip").text("@RULE.ruleIdUsed@");
			$("#aggRuleId").focus();
		}
	}
	
	//保存规则
	function saveRule(){
		var usableIndex = window.parent.arrayCopy;
		var ruleId = $("#aggRuleId").val().trim();
		if(!checkInput(ruleId,1,8)){  //输入校验 1-8之间的整数
			$("#aggRuleId").focus();
			return;
		}
		if($.inArray(parseInt(ruleId), usableIndex) == -1){
			$("#aggRuleId").focus();
			return;
		}
		if(dataStore.getCount() > 0){
			var cVlan = new Array();
			var sVlan;
			dataStore.each(function(rec){
				cVlan.push(rec.data.ruleCvlan);
				sVlan = rec.data.ruleSvlan;
			});
			
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/univlanprofile/addProfileRule.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					profileIndex : profileIndex,
					ruleIndex : ruleId,
					ruleMode : ruleMode,
					ruleCvlan : cVlan.join(","),
					ruleSvlan : sVlan
				},
				success : function() {
					//window.top.showMessageDlg("@COMMON.tip@", "@RULE.saveSuccess@");
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@RULE.saveSuccess@</b>'
	       			});
					window.parent.dataStore.reload();
					window.location.href = window.location.href;
					cancelClick();
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
	
	$(document).ready(function(){
		window.dataStore = new Ext.data.JsonStore({
			data : [],
			fields : ["ruleCvlan", "ruleSvlan"]
		});
		window.colModel = new Ext.grid.ColumnModel([ 
		   	{header : "@RULE.originalAgg@", width : 120, align : 'center',dataIndex : 'ruleCvlan'}, 
		 	{header : "@RULE.targetAgg@", width : 120, align : 'center',dataIndex : 'ruleSvlan'}, 
		 	{header : "@COMMON.manu@", width : 100, align : 'center',dataIndex :'op', renderer : opeartionRender}
		]);
		
		window.aggGrid =  new Ext.grid.GridPanel({
			id : 'aggGrid',
			title : "@RUlE.aggDataList@",
			height : 300,
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
	});
</script>
</head>
<body class="openWinBody">
	<div class="edge10 pT20">
		<table class="zebraTableRows" >
			<tr>
             	<td class="rightBlueTxt" width="140">
                	@RULE.ruleId@:
				</td>
				<td width="210">
					<input type="text" id="aggRuleId" class="normalSel w200" toolTip="@RULE.ruleIdTip@" onblur="aggBlurFn()"/>
				</td>
				<td colspan="3">
					<b id="aggErrorTip" class="orangeTxt"></b>
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="withoutBorderBottom rightBlueTxt">@RULE.originalAgg@:<font color=red>*</font></td>
				<td class="withoutBorderBottom">
					<input type="text" id="cVlanInput" class="normalInput w200" toolTip="@RUlE.batchAddTip@"/>
				</td>
				<td class="withoutBorderBottom rightBlueTxt">@RULE.targetAgg@:<font color=red>*</font></td>
				<td class="withoutBorderBottom">
					<input type="text" id="sVlanInput" class="normalInput w200" toolTip="@RULE.vlanInputTip@"/>
				</td>
				<td class="withoutBorderBottom"><a href="javascript:;" class="normalBtn" onclick="addClick()"> <span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></td>
			</tr>
		</table>
		<div id="contentGrid"></div>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	         <li><a  onclick='saveRule()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
	         <li><a  onclick='cancelClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@RUlE.goBack@</span></a></li>
	     </ol>
	</div>
</body>
</Zeta:HTML>