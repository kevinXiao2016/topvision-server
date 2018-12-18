<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library zeta
	library jquery
	module platform
    import js.tools.ipText
    import js.tools.numberInput
    css js/tools/css/numberInput
</Zeta:Loader>
<head>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script>
	var cmCollectMode = '${cmCollectMode}';//<!--CM采集模式,1为直接获取,2为RemoteQuery-->
	var cmPingMode = '${cmPingMode}';
	//<!--保存配置-->
	function okClick() {
		if($('#remoteQueryOff').attr("checked")){
			cmCollectMode = 1;
		}else{
			cmCollectMode = 2;
		}
		if($('#cmPingDirect').attr("checked")){
			cmPingMode = 1;
		}else{
			cmPingMode = 2;
		}
		//<!--提交保存-->
		$.ajax({
			url : '/remotequerycm/saveCmRemoteQueryConfig.tv',
			type : 'POST',
			data : {
				cmCollectMode :  cmCollectMode,
				cmPingMode : cmPingMode
			},
			success : function() {
				top.afterSaveOrDelete({
					title: '@sys.tip@',
					html: '<b class="orangeTxt">@sys.saved@</b>'
  				});
                try {
                    window.parent.getFrame("cmListNew").hideTr(cmCollectMode);
                } catch(e) {
                }
				cancelClick();
			},
			error : function(json) {
				window.parent.showMessageDlg("@sys.tip@","@sys.saveFaild@");
			},
			cache : false
		});
	}
	function cancelClick() {
		window.top.closeWindow('modalDlg');
	}
	//<!--初始化-->
	$(function() {
	    if(cmCollectMode == 2){
	    	$('#remoteQueryOn').attr("checked", true)
	    }else{
	    	$('#remoteQueryOff').attr("checked", true)
	    }
	    if(cmPingMode == 1){
	    	$('#cmPingDirect').attr("checked", true)
	    }else{
	    	$('#cmPingFromCmts').attr("checked", true)
	    }
	});
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
	    <div class="openWinTip">@sys.cmCollectModeTip@</div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT20">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">
	                    <label for="autoWrite">@sys.cmCollectMode@:</label>
	                </td>
	                <td colspan="2">
						<input type="radio" name="autoWrite" checked="checked" id="remoteQueryOff" value="1" />@sys.cmCollectDirectlyMode@
						<input type="radio" name="autoWrite" id="remoteQueryOn" value="2" />@sys.cmCollectRemoteMode@
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">
	                    <label for="autoWrite">@sys.cmPingMode@:</label>
	                </td>
	                <td colspan="2">
						<input type="radio" name="cmPing" checked="checked" id="cmPingDirect" value="1" />@sys.cmPingDirect@
						<input type="radio" name="cmPing" id="cmPingFromCmts" value="2" />@sys.cmPingFromUplink@
	                </td>
	            </tr>
	        </tbody>
	    </table>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
		    <ol class="upChannelListOl pB0 pT30 noWidthCenter">
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="okClick()">
		                <span>
		                    <i class="miniIcoData">
		                    </i>
		                   	@sys.save@
		                </span>
		            </a>
		        </li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
		                <span>
		                	<i class="miniIcoForbid">
		                    </i>
		                    @sys.cancel@
		                </span>
		            </a>
		        </li>
		    </ol>
		</div>
	</div>	
</body>
</Zeta:HTML>