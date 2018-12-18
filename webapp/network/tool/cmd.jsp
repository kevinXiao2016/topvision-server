<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module resources
</Zeta:Loader>
<script>
	var pingCount = '${pingCount}';
	var pingTimeout = '${pingTimeout}';
	var cmd = '${cmd}';
	var ip = '${ip}';
	var timer = null;
	var console = null;
	
	function asyncPing(){
		//校验参数是否合法
		var pingCount = $('#pingCount').val()/* parseInt($('#pingCount').val(), 10) */,
			pingTimeout = $('#pingTimeout').val(),
			numberReg = /^\d+$/;
		if(!numberReg.test(pingCount) || isNaN(parseInt($('#pingCount').val(), 10))){
			//pingCount不是合法数字
			$('#pingCount').focus();
			return;
		}
		pingCount = parseInt($('#pingCount').val(), 10);
		if(pingCount < 1 || pingCount > 65535){
			//pingCount越界
			$('#pingCount').focus();
			return;
		}
		if(!numberReg.test(pingTimeout) || isNaN(parseInt($('#pingTimeout').val(), 10))){
			//pingTimeout不是合法数字
			$('#pingTimeout').focus();
			return;
		}
		pingTimeout = parseInt($('#pingTimeout').val(), 10);
		if(pingTimeout < 1 || pingTimeout > 30000){
			//pingCount越界
			$('#pingTimeout').focus();
			return;
		}
		console.value = "";
		$.post('/entity/ping.tv', {
			ip: ip,
			pingCount: pingCount,
			pingTimeout: pingTimeout
		}, function(){
			timer = setInterval("sendCmd()", 300);
		})
	}
	
	function sendCmd() {
		$.ajax({url:'getRunCmdResult.tv', dataType:'json', cache : false,
			success:function(json){
				if (json.success) {
	                doOnUnload();
	            }
				doCmdResult(json.result);
			}, error:function(){}
		});
	}
	function doCmdResult(text) {
	    console.value = text;
	    //console.doScroll("scrollbarPageDown");
	    $("#content").scrollTop(10000000);
	}
	function doOnload() {
		if(cmd=='ping'){
			$('#pingContainer').show();
			$('#content').height(230);
			$('#pingCount').val(pingCount);
			$('#pingTimeout').val(pingTimeout);
		}
		console = Zeta$("content");
	    timer = setInterval("sendCmd()", 300);
	}
	function doOnUnload() {
	    if (timer != null) {
	        clearInterval(timer);
	        timer = null;
	    }
	}
	function closeClick() {
		doOnUnload();
	    window.top.closeWindow("modalDlg");
	}
</script>
</head>
<body class="openWinBody" onload="doOnload()" onunload="doOnUnload()">
	<div id="pingContainer" style="display: none;" class="edge5 mT20">
		<table>
			<tr>
				<td>
					<label>@platform/sys.Count@@COMMON.maohao@</label>
					<input id="pingCount" maxlength="5" toolTip="1~65535" class="normalInput w100"/>
				</td>
				<td width="240">
					<label style="margin-left:20px;">@platform/sys.Timeout@(ms)@COMMON.maohao@</label>
					<input id="pingTimeout" maxlength="5" toolTip="1~30000" class="normalInput w100"/>
				</td>
				<td>
					<a href="javascript:;" class="normalBtn" onclick="asyncPing()">
			            <span><i class="miniIcoCmd"></i>Ping</span>
			        </a>
				</td>
			</tr>
		</table>
	</div>
	<div class="edge5 pT0 pB0">
		<textarea id="content" readonly="readonly" style="overflow: auto; background-color: black; width: 580px; height: 290px; font-size: 10pt; font-weight: bold; color: #c0c0c0"></textarea>
	</div>
	<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
	    <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="closeClick()">
	                <span><i class="miniIcoWrong"></i>@BUTTON.close@</span>
	            </a>
	        </li>
	    </ol>
	</div>
</body>
</Zeta:HTML>
