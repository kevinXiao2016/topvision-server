<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
    import epon.vlan.oltVlanUtil
    import js.nm3k.Nm3kTools
</Zeta:Loader>
<style type="text/css">
.dataTable td input{position:relative; top:2px;}
</style>
<script type="text/javascript">
var entityId = ${entityId};
var ponId = ${ponId};
$(function(){
	configBox = new Nm3kTools.createCheckBoxTable({
		renderId: 'config-container',
		lineNum: 4,
		title: '@VLAN.plsSelCfgToApply@',
		data: [{
			label: '@VLAN.trans@',
			value: 'TRANSLATION'
		}, {
			label: '@VLAN.agg@',
			value: 'AGGREGATION'
		}, {
			label: '@VLAN.transparent@',
			value: 'TRANSPARENT'
		}, {
			label: 'QinQ',
			value: 'QINQ'
		}]
	});
	
	//读取该OLT的PON口信息并构建HTML结构
	initPortRegion();
})

function initPortRegion(){
	$.ajax({
		url: '/epon/vlanList/loadSlotPonList.tv',
		data: {
			entityId: entityId
		},
		dataType : "json",
		success : function(slotList) {
			//去掉当前PON口
			$.each(slotList, function(i, slot){
				for(var i=slot.pons.length; i>0; i--){
					if(slot.pons[i-1].portId == ponId){
						slot.pons.splice(i-1, 1);
						break;
					}
				}
			});
			createPonBody(slotList);
		},
		error : function(response) {
			window.parent.showMessageDlg("@COMMON.tip@", '@VLAN.loadSlotPonInfoError@');
		},
		cache : false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}

function createPonBody(slotList){
	var bodyStr = '',
		tdTpl = '<td align="center"><input class="ponCbx" data-slotno="{3}" data-ponindex="{0}" data-ponid="{1}" type="checkbox" />{2}</td>',
		maxPonNum = 0;
	//找到最大的板卡PON口数量
	$.each(slotList, function(i, slot){
		maxPonNum = Math.max(maxPonNum, slot.pons.length);
	});
		
	$.each(slotList, function(i, slot){
		var slotStr = '<tr>';
		slotStr += '<td align="center"><input class="rowAllCheck" type="checkbox" /></td>';
		slotStr += String.format('<td align="center">{0}({1})</td>', slot.slotNo, slot.bname) ;
		$.each(slot.pons, function(j, pon){
			slotStr += String.format(tdTpl, pon.portInex, pon.portId, convertPortIndexToStr(pon.portInex), slot.slotNo);
		})
		for(var k=slot.pons.length; k<maxPonNum; k++){
			slotStr += '<td></td>';
		}
		slotStr += '</tr>';
		bodyStr += slotStr;
	})
	$('#ponTitleTd').attr('colspan', maxPonNum);
	$('#slotContainer').html(bodyStr);
	initEvent();
}

function initEvent(){
	$('#port-container').delegate('input[type="checkbox"]', 'change', function(e){
		var $target = $(e.target),	targetChecked = $target.is(':checked');
		if($target.attr('id') === 'portAllCbx'){
			//全选checkbox
			$('input.ponCbx').attr('checked', targetChecked);
			$('input.rowAllCheck').attr('checked', targetChecked);
		}else if($target.hasClass('rowAllCheck')){
			//单行全选checkbox
			$('input.ponCbx', $(this).parent().parent()).attr('checked', targetChecked);
			//判断全选checkbox的选中状态
			$('#portAllCbx').attr('checked', $('input.ponCbx').length === $('input.ponCbx:checked').length)
		}else if($target.hasClass('ponCbx')){
			//单个PON口checkbox
			var rowAllCbxs = $('input.ponCbx', $target.parent().parent());
			$('input.rowAllCheck', $(this).parent().parent()).attr('checked', rowAllCbxs.length === $('input.ponCbx:checked', $target.parent().parent()).length)
			//判断全选按钮的选中状态
			$('#portAllCbx').attr('checked', $('input.ponCbx').length === $('input.ponCbx:checked').length)
		}
	})
}

function applyConfig(){
	//获取选择的配置
	var selConfigs = configBox.getSelectValue();
	if(!selConfigs.length){
		return window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.plsSelCfgToApply@');
	}
	//获取选择的PON口
	var selPonCbx = $('input.ponCbx:checked');
	if(!selPonCbx.length){
		return window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.plsSelPortToApply@');
	}
	var ponIds = [];
	$.each(selPonCbx, function(i, cbx){
		ponIds.push($(cbx).data('ponid'));
	});
	top.executeLongRequeset({
		url:"/epon/vlanList/batchCopyPonServiceVlanConfig.tv",
		data:{
			ponId: ponId,
			targetPonIds: ponIds.join(','),
			services: selConfigs.join(',')
		},
		requestHandler:function (data){
			top.afterSaveOrDelete({
				title: "@COMMON.tip@",
				html: '<b class="orangeTxt">'+data.data+'</b>'
			});
		},
		successMessage:"@VLAN.configApplyToOtherSuc@",
		errorMessage:"@VLAN.configApplyToOtherFail@"
	});
}

function cancleClick(){
	top.closeWindow("applyToOtherPon");
}
</script>
</head>

<body style="padding:10px;">
	<div id="config-container" style="margin-top:10px;"></div>
	
	<div id="port-container" style="height:400px;margin-top:30px;">
		<table class="dataTable zebra zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
			<thead>
				<tr>
					<th width="40"><input type="checkbox" id="portAllCbx"/></th>
					<th width="100">@VLAN.slot@</th>
					<th id="ponTitleTd">@VLAN.ponPort@</th>
				</tr>
			</thead>
			<tbody id="slotContainer">
			</tbody>
		</table>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
	    <ol class="upChannelListOl pB0 noWidthCenter">
	    	<li><a href="javascript:;" class="normalBtnBig" onclick="applyConfig()"><span><i class="miniIcoApply"></i>@COMMON.apply@</span></a></li>
	        <li><a href="javascript:;" class="normalBtnBig" onclick="cancleClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	    </ol>
	</div>
</body>
</Zeta:HTML>