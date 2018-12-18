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
	partitionData.upSnrMin = $('#custom_upSnrMin').val();
	partitionData.upSnrMax = $('#custom_upSnrMax').val();

	partitionData.downSnrMin = $('#custom_downSnrMin').val();
	partitionData.downSnrMax = $('#custom_downSnrMax').val();

	partitionData.upPowerMin = $('#custom_upPowerMin').val();
	partitionData.upPowerMax = $('#custom_upPowerMax').val();

	partitionData.downPowerMin = $('#custom_downPowerMin').val();
	partitionData.downPowerMax = $('#custom_downPowerMax').val();

	//校验
	if(!validate()) return;
	for(var key in partitionData){
		//各项必须为整数
		if(partitionData[key]!=''){
			partitionData[key] = parseInt(partitionData[key], 10);
		}
	}
	
	if(window.top.frames['framecmtsInfo'] && window.top.frames['framecmtsInfo'].partitionChanged){
        window.top.frames['framecmtsInfo'].partitionChanged(partitionData);
    }
	$.ajax({
        url: '/cmtsInfo/saveLocalThreshold.tv',
        type: 'POST',
        async: false,
        data:{
        	upSnrMin:partitionData.upSnrMin,
        	upSnrMax:partitionData.upSnrMax,
        	downSnrMin:partitionData.downSnrMin,
        	downSnrMax:partitionData.downSnrMax,
        	upPowerMin:partitionData.upPowerMin,
        	upPowerMax:partitionData.upPowerMax,
        	downPowerMin:partitionData.downPowerMin,
        	downPowerMax:partitionData.downPowerMax
        },
        dataType:"json",
        success: function(json) {
        	top.afterSaveOrDelete({
                title: I18N.CMC.tip.tipMsg,
                html: '<b class="orangeTxt">@COMMON.keepSuccess@</b>'
            });
        }, error: function(json) {
        }
    });
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
//合理区间用dBmV保存
function transTodBmV(value){
	var powerUnit = '@{unitConfigConstant.elecLevelUnit}@';
    if(powerUnit == '@unit.dbuv@' && value !=''){
        return parseFloat(value) - 60;
    }
    return value;
}

function transToSystemUnit(value){
    var powerUnit = '@{unitConfigConstant.elecLevelUnit}@';
    if(powerUnit == '@unit.dbuv@' && value !=''){
        return parseFloat(value) + 60;
    }
    return value;
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

$(function(){
	$.ajax({
        url: '/cmtsInfo/getLocalThreshold.tv',
        type: 'POST',
        async: false,
        dataType:"json",
        success: function(json) {
        	if(json.upSnrMin!=null){
        		$('#custom_upSnrMin').val(json.upSnrMin);
        	}
        	if(json.upSnrMax!=null){
        		$('#custom_upSnrMax').val(json.upSnrMax);
        	}
        	if(json.downSnrMin!=null){
                $('#custom_downSnrMin').val(json.downSnrMin);
            }
        	if(json.downSnrMax!=null){
                $('#custom_downSnrMax').val(json.downSnrMax);
            }
        	if(json.upPowerMin!=null){
                $('#custom_upPowerMin').val(transToSystemUnit(json.upPowerMin));
            }
        	if(json.upPowerMax!=null){
                $('#custom_upPowerMax').val(transToSystemUnit(json.upPowerMax));
            }
        	if(json.downPowerMin!=null){
                $('#custom_downPowerMin').val(transToSystemUnit(json.downPowerMin));
            }
        	if(json.downPowerMax!=null){
                $('#custom_downPowerMax').val(transToSystemUnit(json.downPowerMax));
            }
        }, error: function(json) {
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
	<div class="openWinHeader" style="height:90px">
	    <div class="openWinTip">
	    	<p>@CM.partitionTip11@</p>
	    	<p>@CM.partitionTip12@</p>
	    	<p>@CM.partitionTip13@</p>
<%-- 	    	<p>@CM.partitionTip2@<span class="iptSpan">@CM.partition.20to22@</span>@COMMON.douhao@@CM.partitionTip3@<span class="iptSpan">20</span>@COMMON.douhao@@CM.partitionTip4@<span class="iptSpan">22</span></p> --%>
	    </div>
	    <div class="rightCirIco wheelCirIco" style="top:20px"></div>
	</div>
	
	<div class="edge10">
		<table id="table" class="zebraTableRows" >
			<tbody>
				<tr id="upSnr_tr">
					<td class="rightBlueTxt" width="270">@CM.partition.upSnr@(dB)@COMMON.maohao@</td>
					<td width="40"><input id="custom_upSnrMin" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td width="15">~</td>
					<td style="padding-right: 30px;"><input id="custom_upSnrMax" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
				<tr id="downSnr_tr" class="darkZebraTr">
					<td class="rightBlueTxt">@CM.partition.downSnr@(dB)@COMMON.maohao@</td>
					<td><input id="custom_downSnrMin" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td>~</td>
					<td><input id="custom_downSnrMax" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
				<tr id="upPower_tr">
					<td class="rightBlueTxt">@CM.partition.upPower2@(@{unitConfigConstant.elecLevelUnit}@)@COMMON.maohao@</td>
					<td><input id="custom_upPowerMin" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td>~</td>
					<td><input id="custom_upPowerMax" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
				<tr id="downPower_tr" class="darkZebraTr">
					<td class="rightBlueTxt">@CM.partition.downPower@(@{unitConfigConstant.elecLevelUnit}@)@COMMON.maohao@</td>
					<td><input id="custom_downPowerMin" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
					<td>~</td>
					<td><input id="custom_downPowerMax" class="normalInput w30" toolTip="@COMMON.pleaseEnterInt@" maxlength="4"/></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
     	<ol class="upChannelListOl pT10 noWidthCenter">
         	<li><a href="javascript:ok();" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@COMMON.ok@</span></a></li>
         	<li><a href="javascript:cancel();" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
     	</ol>
	</div>
</body>
</Zeta:HTML>