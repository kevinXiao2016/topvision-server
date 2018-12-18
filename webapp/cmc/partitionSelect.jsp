<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Jquery
	library ext
	library zeta
    module cmc
    import js.json2
</Zeta:Loader>
<script type="text/javascript">
var partitionData = ${partitionData},
	cmcId = partitionData.cmcId;
function ok(){
	//获取此时选中的指标
	var upSnr = $('#upSnr_tr').find('.selectSection.selected').first(),
		downSnr = $('#downSnr_tr').find('.selectSection.selected').first(),
		upPower = $('#upPower_tr').find('.selectSection.selected').first(),
		downPower = $('#downPower_tr').find('.selectSection.selected').first();
	if(upSnr.hasClass('custom')){
		partitionData.upSnrMin = $('#custom_upSnrMin').val();
		partitionData.upSnrMax = $('#custom_upSnrMax').val();
	}else{
		partitionData.upSnrMin = upSnr.attr('min');
		partitionData.upSnrMax = upSnr.attr('max');
	}
	if(downSnr.hasClass('custom')){
		partitionData.downSnrMin = $('#custom_downSnrMin').val();
		partitionData.downSnrMax = $('#custom_downSnrMax').val();
	}else{
		partitionData.downSnrMin = downSnr.attr('min');
		partitionData.downSnrMax = downSnr.attr('max');
	}
	if(upPower.hasClass('custom')){
		partitionData.upPowerMin = $('#custom_upPowerMin').val();
		partitionData.upPowerMax = $('#custom_upPowerMax').val();
	}else{
		partitionData.upPowerMin = parsePower(upPower.attr('min'));
		partitionData.upPowerMax = parsePower(upPower.attr('max'));
	}
	if(downPower.hasClass('custom')){
		partitionData.downPowerMin = $('#custom_downPowerMin').val();
		partitionData.downPowerMax = $('#custom_downPowerMax').val();
	}else{
		partitionData.downPowerMin = parsePower(downPower.attr('min'));
		partitionData.downPowerMax = parsePower(downPower.attr('max'));
	}
	//校验
	if(!validate()) return;
	for(var key in partitionData){
		//各项必须为整数
		if(partitionData[key]!=''){
			partitionData[key] = parseInt(partitionData[key], 10);
		}
	}
	
	if(window.top.frames['frameentity-'+cmcId] && window.top.frames['frameentity-'+cmcId].partitionChanged){
        window.top.frames['frameentity-'+cmcId].partitionChanged(partitionData);
    }
	if(window.top.frames['framecmListNew'] && window.top.frames['framecmListNew'].partitionChanged){
        window.top.frames['framecmListNew'].partitionChanged(partitionData);
    }
	if(window.top.frames['framecm-realTime'+cmcId] && window.top.frames['framecm-realTime'+cmcId].partitionChanged){
	    window.top.frames['framecm-realTime'+cmcId].partitionChanged(partitionData);
    }
	if(window.top.frames['framerealtimeCmList'] && window.top.frames['framerealtimeCmList'].partitionChanged){
	    window.top.frames['framerealtimeCmList'].partitionChanged(partitionData);
    }
	if(window.top.frames['framecmtsInfo'] && window.top.frames['framecmtsInfo'].partitionChanged){
        window.top.frames['framecmtsInfo'].partitionChanged(partitionData);
    }
	cancel();
	
	function validate(){
		for(var key in partitionData){
			//各项必须为整数
			if(partitionData[key]!=='' && isNaN(parseInt(partitionData[key], 10))){
				$('#custom_'+key).focus();
				//window.top.showMessageDlg('@COMMON.tip@', '@COMMON.pleaseEnterInt@');
				return false;
			}
		}
		for(var key in partitionData){
			//各项必须为整数
			if(partitionData[key]!==''){
				partitionData[key] = parseInt(partitionData[key], 10);
			}
		}
		if($.isNumeric(partitionData.upSnrMin) && $.isNumeric(partitionData.upSnrMax) && partitionData.upSnrMin > partitionData.upSnrMax){
			//window.top.showMessageDlg('@COMMON.tip@', '@COMMON.upSnrMinBiggerThanupSnrMax@');
			$('#custom_upSnrMin').focus();
			top.afterSaveOrDelete({
				title: '@COMMON.tip@',
   		      	html: '<b class="orangeTxt">@COMMON.upSnrMinBiggerThanupSnrMax@</b>'
   		    });
			return false;
		}
		if($.isNumeric(partitionData.downSnrMin) && $.isNumeric(partitionData.downSnrMax) && partitionData.downSnrMin > partitionData.downSnrMax){
			//window.top.showMessageDlg('@COMMON.tip@', '@COMMON.downSnrMinBiggerThandownSnrMax@');
			$('#custom_downSnrMin').focus();
			top.afterSaveOrDelete({
				title: '@COMMON.tip@',
   		      	html: '<b class="orangeTxt">@COMMON.downSnrMinBiggerThandownSnrMax@</b>'
   		    });
			return false;
		}
		if($.isNumeric(partitionData.upPowerMin) && $.isNumeric(partitionData.upPowerMax) && partitionData.upPowerMin > partitionData.upPowerMax){
			//window.top.showMessageDlg('@COMMON.tip@', '@COMMON.upPowerMinBiggerThanupPowerMax@');
			$('#custom_upPowerMin').focus();
			top.afterSaveOrDelete({
				title: '@COMMON.tip@',
   		      	html: '<b class="orangeTxt">@COMMON.upPowerMinBiggerThanupPowerMax@</b>'
   		    });
			return false;
		}
		if($.isNumeric(partitionData.downPowerMin) && $.isNumeric(partitionData.downPowerMax) && partitionData.downPowerMin > partitionData.downPowerMax){
			//window.top.showMessageDlg('@COMMON.tip@', '@COMMON.downPowerMinBiggerThanMax@');
			$('#custom_downPowerMin').focus();
			top.afterSaveOrDelete({
				title: '@COMMON.tip@',
   		      	html: '<b class="orangeTxt">@COMMON.downPowerMinBiggerThanMax@</b>'
   		    });
			return false;
		}
		return true;
	}
}

function cancel(){
	window.top.closeWindow('modalDlg');
}

//电平值转换
function parsePower(powerValue){
	var powerUnit = '@{unitConfigConstant.elecLevelUnit}@';
	if(powerUnit == '@unit.dbuv@' && powerValue !=''){
		return parseFloat(powerValue) + 60;
	}
	return powerValue;
}

function parseToDBmV(powerValue){
	var powerUnit = '@{unitConfigConstant.elecLevelUnit}@';
	if(powerUnit == 'unit.dbuv' && powerValue !=''){
		return parseFloat(powerValue) - 60;
	}
	return powerValue;
}

function equals(sectionValue, partitionValue) {
	if(sectionValue === '' || sectionValue === null) {
		if(partitionValue === undefined) {
			return true;
		} else {
			return false;
		}
	} else {
		if(partitionValue === undefined) {
			return false;
		} else {
			return parseInt(sectionValue) === parseInt(partitionValue);
		}
	}
}

$(function(){
	var upSnr = $('#upSnr_tr').find('.selectSection'),
		downSnr = $('#downSnr_tr').find('.selectSection'),
		upPower = $('#upPower_tr').find('.selectSection'),
		downPower = $('#downPower_tr').find('.selectSection'),
		i=0, len;
	
	for(i=0, len=upSnr.length; i<len; i++){
		if(equals($(upSnr[i]).attr('min'), partitionData.upSnrMin) && equals($(upSnr[i]).attr('max'), partitionData.upSnrMax)){
			upSnr.first().removeClass('selected');
			$(upSnr[i]).addClass('selected');
			break;
		}
	}
	if(upSnr.first().hasClass('selected') && (partitionData.upSnrMin!=='' || partitionData.upSnrMax!=='')){
		upSnr.first().removeClass('selected');
		upSnr.last().addClass('selected');
		$('#custom_upSnrMin').val(partitionData.upSnrMin);
		$('#custom_upSnrMax').val(partitionData.upSnrMax);
	}
	for(i=0, len=downSnr.length; i<len; i++){
		if(equals($(downSnr[i]).attr('min'), partitionData.downSnrMin) && equals($(downSnr[i]).attr('max'), partitionData.downSnrMax)){
			downSnr.first().removeClass('selected');
			$(downSnr[i]).addClass('selected');
			break;
		}
	}
	if(downSnr.first().hasClass('selected') && (partitionData.downSnrMin!=='' || partitionData.downSnrMax!=='')){
		downSnr.first().removeClass('selected');
		downSnr.last().addClass('selected');
		$('#custom_downSnrMin').val(partitionData.downSnrMin);
		$('#custom_downSnrMax').val(partitionData.downSnrMax);
	}
	//处理上行电平显示值
	upPower.eq(1).text("@COMMON.lessthan@" + parsePower(30));
	upPower.eq(2).text(parsePower(30) + " @COMMON.to@ "+ parsePower(38));
	upPower.eq(3).text(parsePower(38) + " @COMMON.to@ "+ parsePower(42));
	upPower.eq(4).text(parsePower(42) + " @COMMON.to@ "+ parsePower(45));
	upPower.eq(5).text(parsePower(45) + " @COMMON.to@ "+ parsePower(48));
	upPower.eq(6).text(parsePower(48) + " @COMMON.to@ "+ parsePower(52));
	upPower.eq(7).text("@COMMON.morethan@"+ parsePower(52));
	//处理下行电平显示值
	downPower.eq(1).text("@COMMON.lessthan@" + parsePower(-15));
	downPower.eq(2).text(parsePower(-15) + " @COMMON.to@ "+ parsePower(-10));
	downPower.eq(3).text(parsePower(-10) + " @COMMON.to@ "+ parsePower(-3));
	downPower.eq(4).text(parsePower(-3) + " @COMMON.to@ "+ parsePower(3));
	downPower.eq(5).text(parsePower(3) + " @COMMON.to@ "+ parsePower(10));
	downPower.eq(6).text(parsePower(10) + " @COMMON.to@ "+ parsePower(15));
	downPower.eq(7).text("@COMMON.morethan@"+ parsePower(15));
	
	for(i=0, len=upPower.length; i<len; i++){
		if(equals($(upPower[i]).attr('min'), parseToDBmV(partitionData.upPowerMin)) && equals($(upPower[i]).attr('max'), parseToDBmV(partitionData.upPowerMax))){
			upPower.first().removeClass('selected');
			$(upPower[i]).addClass('selected');
			break;
		}
	}
	if(upPower.first().hasClass('selected') && (partitionData.upPowerMin!=='' || partitionData.upPowerMax!=='')){
		upPower.first().removeClass('selected');
		upPower.last().addClass('selected');
		$('#custom_upPowerMin').val(partitionData.upPowerMin);
		$('#custom_upPowerMax').val(partitionData.upPowerMax);
	}
	for(i=0, len=downPower.length; i<len; i++){
		if(equals($(downPower[i]).attr('min'), parseToDBmV(partitionData.downPowerMin)) && equals($(downPower[i]).attr('max'), parseToDBmV(partitionData.downPowerMax))){
			downPower.first().removeClass('selected');
			$(downPower[i]).addClass('selected');
			break;
		}
	}
	if(downPower.first().hasClass('selected') && (partitionData.downPowerMin!=='' || partitionData.downPowerMax!=='')){
		downPower.first().removeClass('selected');
		downPower.last().addClass('selected');
		$('#custom_downPowerMin').val(partitionData.downPowerMin);
		$('#custom_downPowerMax').val(partitionData.downPowerMax);
	}
	
	$('#table').bind('click', function(e){
		e = e || window.event;
		var target = $(e.target), selected;
		if(target.hasClass('selectSection')){
			selected = target.hasClass('selected');
			target.parents('tr').find('.selectSection').removeClass('selected');
			if(!selected){
				target.addClass('selected');
			}else{
				target.parents('tr').find('.selectSection').first().addClass('selected');
			}
		}
	});
	
	
})
</script>
<style type="text/css">
.openWinTip{line-height: 20px;}
.zebraTableRows td{padding: 4px 0px;}
.selectSection{color: #d35400;cursor: pointer;padding:2px 5px;}
.selectSection:hover{color:#f39c12;}
.selectSection.selected{background-color: #e67e22;color:#fff;}
.iptSpan{color: #d35400;}
</style>
</head>
<body>
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p>@CM.partitionTip1@</p>
	    	<p>@CM.partitionTip2@<span class="iptSpan">@CM.partition.20to22@</span>@COMMON.douhao@@CM.partitionTip3@<span class="iptSpan">20</span>@COMMON.douhao@@CM.partitionTip4@<span class="iptSpan">22</span></p>
	    </div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	
	<div class="edge10">
		<table id="table" class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr id="upSnr_tr">
					<td class="rightBlueTxt" width="130">@CM.partition.upSnr@(dB)@COMMON.maohao@</td>
					<td width="40"><p class="selectSection selected" min="" max="">@CM.all@</p></td>
					<td><p class="selectSection" min="" max="20">@CM.partition.less20@</p></td>
					<td><p class="selectSection" min="20" max="22">@CM.partition.20to22@</p></td>
					<td><p class="selectSection" min="22" max="25">@CM.partition.22to25@</p></td>
					<td><p class="selectSection" min="25" max="28">@CM.partition.25to28@</p></td>
					<td><p class="selectSection" min="28" max="30">@CM.partition.28to30@</p></td>
					<td><p class="selectSection" min="30" max="34">@CM.partition.30to34@</p></td>
					<td><p class="selectSection" min="34" max="">@CM.partition.morethan34@</p></td>
					<td class="rightBlueTxt"><p class="selectSection custom">@COMMON.cutsom@@COMMON.maohao@</p></td>
					<td><input id="custom_upSnrMin" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td width="10">~</td>
					<td style="padding-right: 30px;"><input id="custom_upSnrMax" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
				<tr id="downSnr_tr" class="darkZebraTr">
					<td class="rightBlueTxt">@CM.partition.downSnr@(dB)@COMMON.maohao@</td>
					<td><p class="selectSection selected" min="" max="">@CM.all@</p></td>
					<td><p class="selectSection" min="" max="20">@CM.partition.less20@</p></td>
					<td><p class="selectSection" min="20" max="22">@CM.partition.20to22@</p></td>
					<td><p class="selectSection" min="22" max="25">@CM.partition.22to25@</p></td>
					<td><p class="selectSection" min="25" max="28">@CM.partition.25to28@</p></td>
					<td><p class="selectSection" min="28" max="30">@CM.partition.28to30@</p></td>
					<td><p class="selectSection" min="30" max="34">@CM.partition.30to34@</p></td>
					<td><p class="selectSection" min="34" max="">@CM.partition.morethan34@</p></td>
					<td class="rightBlueTxt"><p class="selectSection custom">@COMMON.cutsom@@COMMON.maohao@</p></td>
					<td><input id="custom_downSnrMin" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td width="10">~</td>
					<td><input id="custom_downSnrMax" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
				<tr id="upPower_tr">
					<td class="rightBlueTxt">@CM.partition.upPower@(@{unitConfigConstant.elecLevelUnit}@)@COMMON.maohao@</td>
					<td><p class="selectSection selected" min="" max="">@CM.all@</p></td>
					<td><p class="selectSection" min="" max="30"></p></td>
					<td><p class="selectSection" min="30" max="38">@CM.partition.30to38@</p></td>
					<td><p class="selectSection" min="38" max="42">@CM.partition.38to42@</p></td>
					<td><p class="selectSection" min="42" max="45">@CM.partition.42to45@</p></td>
					<td><p class="selectSection" min="45" max="48">@CM.partition.45to48@</p></td>
					<td><p class="selectSection" min="48" max="52">@CM.partition.48to52@</p></td>
					<td><p class="selectSection" min="52" max="">@CM.partition.morethan52@</p></td>
					<td class="rightBlueTxt"><p class="selectSection custom">@COMMON.cutsom@@COMMON.maohao@</p></td>
					<td><input id="custom_upPowerMin" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td width="10">~</td>
					<td><input id="custom_upPowerMax" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
				<tr id="downPower_tr" class="darkZebraTr">
					<td class="rightBlueTxt">@CM.partition.downPower@(@{unitConfigConstant.elecLevelUnit}@)@COMMON.maohao@</td>
					<td><p class="selectSection selected" min="" max="">@CM.all@</p></td>
					<td><p class="selectSection" min="" max="-15">@CM.partition.less_15@</p></td>
					<td><p class="selectSection" min="-15" max="-10">@CM.partition._15to_10@</p></td>
					<td><p class="selectSection" min="-10" max="-3">@CM.partition._10to_3@</p></td>
					<td><p class="selectSection" min="-3" max="3">@CM.partition._3to3@</p></td>
					<td><p class="selectSection" min="3" max="10">@CM.partition.3to10@</p></td>
					<td><p class="selectSection" min="10" max="15">@CM.partition.10to15@</p></td>
					<td><p class="selectSection" min="15" max="">@CM.partition.morethan15@</p></td>
					<td class="rightBlueTxt"><p class="selectSection custom">@COMMON.cutsom@@COMMON.maohao@</p></td>
					<td><input id="custom_downPowerMin" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td width="10">~</td>
					<td><input id="custom_downPowerMax" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
     	<ol class="upChannelListOl pT20 noWidthCenter">
         	<li><a href="javascript:ok();" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@COMMON.ok@</span></a></li>
         	<li><a href="javascript:cancel();" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
     	</ol>
	</div>
</body>
</Zeta:HTML>