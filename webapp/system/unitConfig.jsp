<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<head>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module platform
	import js.tools.ipText
</Zeta:Loader>
<script type="text/javascript">
	var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
	//记录当前配置的温度单位
	var currentTempUnit = '${tempUnit}';
	
	//save the config
	function saveClick() {
		var newTempUnit = $("input[name='tempUnit']:checked").val();
		var tipContent = "@UNIT.restartTip@";
		//在温度单位发生变化时提醒用户可能会发生精度丢失
		if(currentTempUnit != newTempUnit){
			tipContent += "@UNIT.tempUnitTip@";
		}
		tipContent += "@UNIT.unitChangeTip@";
		top.showConfirmDlg('@COMMON.tip@', tipContent, function(para){
			if(para === 'yes'){
				modifyUnit();
			}
		});
	}
	
	function modifyUnit(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/param/saveUnitConfig.tv',
			type : 'POST',
			data : $("#unitConfigForm").serialize(),
			dataType:"json",
			success : function() {
				//关闭等待框
				window.top.closeWaitingDlg();
				//改为右下角弹窗提示成功
				top.afterSaveOrDelete({
			      title: '@COMMON.tip@',
			      html: '<b class="orangeTxt">@Common.saveSuc@</b>'
			    });
				//window.parent.showMessageDlg("@sys.tip@","@sys.logonSaved@");
				cancelClick();
			},
			error : function() {
				//关闭等待框
				window.top.closeWaitingDlg();
				window.parent.showMessageDlg("@COMMON.tip@","@Common.saveFailed@");
			},
			cache : false
		});
	}
	
	//close the dialog
	function cancelClick() {
		window.top.closeWindow('modalDlg');
	}

	Ext.onReady(function() {
		if(!cmcSupport){
			$('.jsSupportCMC').css({visibility: 'hidden'})
		}
		$("input[name='tempUnit'][value='${tempUnit}']").attr("checked",true);
		$("input[name='elecLevelUnit'][value='${elecLevelUnit}']").attr("checked",true);
	});
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader" style="height:78px;">	
	    <div class="openWinTip">
	    	@UNIT.configTip@<br/>
	    	@UNIT.tempRule@:  <span class="pR20 pL10">@UNIT.fToC@</span> @UNIT.cToF@<br/>
	    	@UNIT.powerRule@:  <span class="pR20 pL10">dBmV = @spectrum/spectrum.dbuv@ - 60</span>  @spectrum/spectrum.dbuv@ = dBmV + 60
	    </div>	
	    <div class="rightCirIco flagCirIco" style="top:12px;"></div>	
	</div>
	<div class="edgeTB10LR20 pB0 pT40">
		 <form id="unitConfigForm" name="unitConfigForm">
		     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		         <tbody>
		             <tr>
		             	<td class="rightBlueTxt" width="200">@UNIT.tempUnit@:</td>
		                 <td>
		                    <input type="radio" name="tempUnit" value="@network/ap.chart.single.centigrade@" style="vertical-align: middle;" /><span class="pR10">@UNIT.centigrade@(@network/ap.chart.single.centigrade@)</span>
							<input type="radio" name="tempUnit" value="°F" style="vertical-align: middle;" />@UNIT.fahrenheit@(°F)
		                 </td>
		             </tr>
		             <tr class="darkZebraTr jsSupportCMC">
		             	<td class="rightBlueTxt" width="200">@UNIT.elecLevel@:</td>
		             	<td>
		             		<input type="radio" name="elecLevelUnit" value="dBmV" style="vertical-align: middle;" /><span class="pR10">dBmV</span>
							<input type="radio" name="elecLevelUnit" value="@spectrum/spectrum.dbuv@" style="vertical-align: middle;" />@spectrum/spectrum.dbuv@
		             	</td>
		             </tr>
		         </tbody>
		     </table>
	     </form>
	</div>
	
	<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
	    <ol class="upChannelListOl pB0 pT40 noWidthCenter">
	        <li>
	        	<a href="javascript:;" class="normalBtnBig" onclick="saveClick()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a>
	        </li>
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a>
	        </li>
	    </ol>
	</div>
	
</body>
</Zeta:HTML>