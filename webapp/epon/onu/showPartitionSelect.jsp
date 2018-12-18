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
    module ONU
</Zeta:Loader>
<script type="text/javascript">
var partitionData = ${partitionData},
	onuId = partitionData.onuId;
function ok(){
	//获取此时选中的指标
	var $receive = $('#receiveDiv').find('.selectSection.selected').first(),
		$trasmit = $('#transmitDiv').find('.selectSection.selected').first(),
		$ponReceive = $('#ponReceiveDiv').find('.selectSection.selected').first();
	if($receive.hasClass('custom')){
		partitionData.receiveMin = $('#custom_receive_min').val();
		partitionData.receiveMax = $('#custom_receive_max').val();
	}else{
		partitionData.receiveMin = parsePower($receive.attr('min'));
		partitionData.receiveMax = parsePower($receive.attr('max'));
	}
	if($trasmit.hasClass('custom')){
		partitionData.transmitMin = $('#custom_transmit_min').val();
		partitionData.transmitMax = $('#custom_transmit_max').val();
	}else{
		partitionData.transmitMin = parsePower($trasmit.attr('min'));
		partitionData.transmitMax = parsePower($trasmit.attr('max'));
	}
	if ($ponReceive.hasClass('custom')){
		partitionData.ponRecvMin = $('#custom_ponrecv_min').val();
		partitionData.ponRecvMax = $('#custom_ponrecv_max').val();
	} else {
		partitionData.ponRecvMin = parsePower($ponReceive.attr('min'));
		partitionData.ponRecvMax = parsePower($ponReceive.attr('max'));
	}
	//校验
	if (!validate()) return;
	for(var key in partitionData){
		//各项必须为整数
		if(partitionData[key]!=''){
			partitionData[key] = parseInt(partitionData[key], 10);
		}
	}
	if(window.top.frames['frameonuList'] && window.top.frames['frameonuList'].partitionChanged){
        window.top.frames['frameonuList'].partitionChanged(partitionData);
    }
	if(window.top.frames['frameonuLinkView'] && window.top.frames['frameonuLinkView'].partitionChanged){
		window.top.frames['frameonuLinkView'].partitionChanged(partitionData);
    }
	if(window.top.frames['frameonuDeviceList'] && window.top.frames['frameonuDeviceList'].partitionChanged){
		window.top.frames['frameonuDeviceList'].partitionChanged(partitionData);
    }
	cancel();
}

/**
 * 校验数据合法性
 */
function validate(){
	for(var key in partitionData){
		//各项必须为整数
		if(partitionData[key]!='' && isNaN(parseInt(partitionData[key], 10))){
			$('#custom_'+key).focus();
			return false;
		}
	}
	for(var key in partitionData){
		//各项必须为整数
		if(partitionData[key]!=''){
			partitionData[key] = parseInt(partitionData[key], 10);
		}
	}
	if(partitionData.receiveMin && partitionData.receiveMax && partitionData.receiveMin > partitionData.receiveMax){
		$('#custom_receive_min').focus();
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
		      	html: '<b class="orangeTxt">@COMMON.upSnrMinBiggerThanupSnrMax@</b>'
		    });
		return false;
	}
	if(partitionData.transmitMin && partitionData.transmitMax && partitionData.transmitMin > partitionData.transmitMax){
		$('#custom_transmit_min').focus();
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
		      	html: '<b class="orangeTxt">@COMMON.downSnrMinBiggerThandownSnrMax@</b>'
		    });
		return false;
	}
	if(partitionData.ponRecvMin && partitionData.ponRecvMax && partitionData.ponRecvMin > partitionData.ponRecvMax){
		$('#custom_ponrecv_min').focus();
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
		      	html: '<b class="orangeTxt">@COMMON.upPowerMinBiggerThanupPowerMax@</b>'
		    });
		return false;
	}
	return true;
}

function cancel(){
	window.top.closeWindow('modalDlg');
}

//电平值转换
function parsePower(powerValue){
	var powerUnit = '@{unitConfigConstant.elecLevelUnit}@';
	if(powerUnit == 'dBμV' && powerValue !=''){
		return parseFloat(powerValue) + 60;
	}
	return powerValue;
}

function parseToDBmV(powerValue){
	var powerUnit = '@{unitConfigConstant.elecLevelUnit}@';
	if(powerUnit == 'dBμV' && powerValue !=''){
		return parseFloat(powerValue) - 60;
	}
	return powerValue;
}

$(function(){
	var $receive = $('#receiveDiv').find('.selectSection'),
		$trasmit = $('#transmitDiv').find('.selectSection'),
		$ponReceive = $('#ponReceiveDiv').find('.selectSection'),
		i=0, len;
	
	//接收功率
	$receive.eq(1).text("@COMMON.lessthan@" + parsePower(-27));
	$receive.eq(2).text(parsePower(-27) + "@COMMON.to@"+ parsePower(-24));
	$receive.eq(3).text(parsePower(-24) + "@COMMON.to@"+ parsePower(-7));
	$receive.eq(4).text("@COMMON.morethan@"+ parsePower(-7));
	//发射功率
	$trasmit.eq(1).text("@COMMON.lessthan@" + parsePower(2));
	$trasmit.eq(2).text(parsePower(2) + "@COMMON.to@"+ parsePower(8));
	$trasmit.eq(3).text("@COMMON.morethan@"+ parsePower(8));
	//PON接收功率
	$ponReceive.eq(1).text("@COMMON.lessthan@" + parsePower(-27));
	$ponReceive.eq(2).text(parsePower(-27) + "@COMMON.to@"+ parsePower(-24));
	$ponReceive.eq(3).text(parsePower(-24) + "@COMMON.to@"+ parsePower(-7));
	$ponReceive.eq(4).text("@COMMON.morethan@"+ parsePower(-7));
	
	for(i=0, len=$receive.length; i<len; i++){
		if($($receive[i]).attr('min')==parseToDBmV(partitionData.receiveMin) && $($receive[i]).attr('max')==parseToDBmV(partitionData.receiveMax)){
			$receive.first().removeClass('selected');
			$($receive[i]).addClass('selected');
			break;
		}
	}
	if($receive.first().hasClass('selected') && i>0){
		$receive.first().removeClass('selected');
		$receive.last().addClass('selected');
		$('#custom_receive_min').val(partitionData.receiveMin);
		$('#custom_receive_max').val(partitionData.receiveMax);
	}
	for(i=0, len=$trasmit.length; i<len; i++){
		if($($trasmit[i]).attr('min')==parseToDBmV(partitionData.transmitMin) && $($trasmit[i]).attr('max')==parseToDBmV(partitionData.transmitMax)){
			$trasmit.first().removeClass('selected');
			$($trasmit[i]).addClass('selected');
			break;
		}
	}
	if($trasmit.first().hasClass('selected') && i>0){
		$trasmit.first().removeClass('selected');
		$trasmit.last().addClass('selected');
		$('#custom_transmit_min').val(partitionData.transmitMin);
		$('#custom_transmit_max').val(partitionData.transmitMax);
	}
	
	for(i=0, len=$ponReceive.length; i<len; i++){
		if($($ponReceive[i]).attr('min')== parseToDBmV(partitionData.ponRecvMin) && $($ponReceive[i]).attr('max')== parseToDBmV(partitionData.ponRecvMax)){
			$ponReceive.first().removeClass('selected');
			$($ponReceive[i]).addClass('selected');
			break;
		}
	}
	
	if($ponReceive.first().hasClass('selected') && i>0){
		$ponReceive.first().removeClass('selected');
		$ponReceive.last().addClass('selected');
		$('#custom_ponrecv_min').val(partitionData.ponRecvMin);
		$('#custom_ponrecv_max').val(partitionData.ponRecvMax);
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
	    	<p>@CMC/CM.partitionTip1@</p>
	    	<p>@CMC/CM.partitionTip2@<span class="iptSpan">@CMC/CM.partition.20to22@</span>,@CMC/CM.partitionTip3@<span class="iptSpan">20</span>,@CMC/CM.partitionTip4@<span class="iptSpan">22</span></p>
	    </div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	
	<div class="edge10">
		<table id="table" class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr id="receiveDiv">
					<td class="rightBlueTxt" width="160">@ONU.opticalRecv@(@{unitConfigConstant.elecLevelUnit}@)@COMMON.maohao@</td>
					<td><p class="selectSection selected" min="" max="">@CMC/CM.all@</p></td>
					<td><p class="selectSection" min="" max="-27">@COMMON.lessthan@-27</p></td>
					<td><p class="selectSection" min="-27" max="-24">-27@COMMON.to@-24</p></td>
					<td><p class="selectSection" min="-24" max="-7">-24@COMMON.to@-7</p></td>
					<td><p class="selectSection" min="-7"  max="">@ONU.moreThan@-7</p></td>
					<td class="rightBlueTxt"><p class="selectSection custom">@COMMON.cutsom@@COMMON.maohao@</p></td>
					<td><input id="custom_receive_min" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td width="10">~</td>
					<td style="padding-right: 30px;"><input id="custom_receive_max" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
				<tr id="transmitDiv" class="darkZebraTr">
					<td class="rightBlueTxt">@ONU.opticalSend@(@{unitConfigConstant.elecLevelUnit}@)@COMMON.maohao@</td>
					<td width="40"><p class="selectSection selected" min="" max="">@CMC/CM.all@</p></td>
					<td><p class="selectSection" min="" max="2">@COMMON.lessthan@2</p></td>
					<td><p class="selectSection" min="2" max="8">2@COMMON.to@8</p></td>
					<td colspan="2"><p class="selectSection" min="8" max="">@ONU.moreThan@8</p></td>
					<td class="rightBlueTxt"><p class="selectSection custom">@COMMON.cutsom@@COMMON.maohao@</p></td>
					<td><input id="custom_transmit_min" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td width="10">~</td>
					<td><input id="custom_transmit_max" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
				<tr id="ponReceiveDiv">
					<td class="rightBlueTxt">@ONU.ponOpticalRecv@(@{unitConfigConstant.elecLevelUnit}@)@COMMON.maohao@</td>
					<td><p class="selectSection selected" min="" max="">@CMC/CM.all@</p></td>
					<td><p class="selectSection" min="" max="-27">@COMMON.lessthan@-27</p></td>
					<td><p class="selectSection" min="-27" max="-24">-27@COMMON.to@-24</p></td>
					<td><p class="selectSection" min="-24" max="-7">-24@COMMON.to@-7</p></td>
					<td><p class="selectSection" min="-7"  max="">@ONU.moreThan@-7</p></td>
					<td class="rightBlueTxt"><p class="selectSection custom">@COMMON.cutsom@@COMMON.maohao@</p></td>
					<td><input id="custom_ponrecv_min" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td width="10">~</td>
					<td><input id="custom_ponrecv_max" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
     	<ol class="upChannelListOl pT20 noWidthCenter">
         	<li><a href="javascript:ok();" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@resources/COMMON.ok@</span></a></li>
         	<li><a href="javascript:cancel();" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
     	</ol>
	</div>
</body>
</Zeta:HTML>