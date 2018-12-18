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
    CSS css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
	.pingTextArea{overflow: auto; background-color: black; width: 580px; height: 230px; font-size: 10pt; font-weight: bold; color: #c0c0c0}
</style>
<script>
	var ip = '${ip}';
	var entityId = '${entityId}';
	var pingCount = '${pingCount}';
	var pingTimeout = parseInt('${pingTimeout}', 10) / 1000; //pingTimeout读取都是毫秒，需要除1000转化成秒;
	var cmd = '${cmd}';
	
	function closeClick() {
	    window.top.closeWindow("modalDlg");
	}
	function validateFn(arr){
		var numberReg = /^\d{1,5}$/,
		    flag      = true;
		$.each(arr, function(i, v){
			var current = $("#"+v.id).val();
			if( numberReg.test(current) ){
				if(current < v.range[0] || current > v.range[1]){
					flag = v.id;
					return false; //退出$.each, 类似for循环的break;
				}
			}else{
				flag = v.id;
				return false; //退出$.each;
			}
		})
		return flag;
	}
	
	$(function(){
		$("#pingCount").val(window.pingCount);
		$("#pingTimeout").val(window.pingTimeout);
		sendPing();
	});//end document.ready;
	function sendPing(){
		var $content     = $("#content"),
		    $pingBtn     = $("#pingBtn"),
		    pingCountV   = $("#pingCount").val(),
		    pingTimeoutV = $("#pingTimeout").val();
		    
		//验证;	
		var flag = validateFn([{
			id    : 'pingCount',
			range : [1,15]
		},{
			id    : 'pingTimeout',
			range : [1,60]
		}]);
		
		if(flag !== true){
			$("#"+flag).focus();
			$pingBtn.removeAttr("disabled");
			return;
		}
		
		var tpl = [
		    ' ',
			'Ping statistics for {pingResultsIpTargetAddress}:',
			'    Packets: Sent = {pingCount}, Received = {pingResultsProbeResponses}, Lost = {lost} ({lostPercent}% loss),',
			'Approximate round trip times in milli-seconds:',
			'    Minimum = {pingResultsMinRtt}ms, Maximum = {pingResultsMaxRtt}ms, Average = {pingResultsAverageRtt}ms',
			' ',
			'Complete!'
		].join('\n');
		var disconnectTpl = [
		    ' ',
			'Ping statistics for {{pingResultsIpTargetAddress}}:',
			'    Packets: Sent = {pingCount}, Received = {pingResultsProbeResponses}, Lost = {lost} ({lostPercent}% loss),',
			' ',
			'Complete!'          
		].join('\n');
		
		$pingBtn.attr({disabled: 'disabled'});
		$content.val( String.format('\n  Ping {0}...',window.ip) );
		$.ajax({url:'getRunSnmpResult.tv', dataType:'json', cache : false,
			data : {
				ip          : window.ip,
				entityId    : window.entityId,
				pingCount   : parseInt(pingCountV, 10),
				pingTimeout : window.pingTimeout * 1000 //超时时间后台存取都是毫秒，因此需要*1000;
			},
			success:function(json){
				if(json.success === false){
					$content.val(json.result);
					$pingBtn.removeAttr("disabled")
					return;
				}
				var obj = json.result;
				obj.pingCount = parseInt(pingCountV, 10);
				obj.lost = pingCountV - obj.pingResultsProbeResponses;
				obj.lostPercent = (obj.lost / obj.pingCount * 100).toFixed(2);
				
				if( json.pingResultsProbeResponses == 0 ){
					disconnectTpl = tpl.format(json.result);
					$content.val(disconnectTpl);
				}else{
					tpl = tpl.format(json.result);
					$content.val(tpl);
				}
				
			}, error : function(){
				pingError();
			}, complete : function(){
				$pingBtn.removeAttr("disabled");
			} 
		});
	};
	function pingError(){
		$("#content").val('Ping error!');
	}
</script>
</head>
<body class="openWinBody">
	<div id="pingContainer" class="edge5 mT20">
		<table>
			<tr>
				<td>
					<label>@platform/sys.Count@@COMMON.maohao@</label>
					<input id="pingCount" maxlength="5" toolTip="1~15" class="normalInput w100"/>
				</td>
				<td width="240">
					<label style="margin-left:20px;">@platform/sys.Timeout@(s)@COMMON.maohao@</label>
					<input id="pingTimeout" maxlength="5" toolTip="1~60" class="normalInput w100"/>
				</td>
				<td>
					<a id="pingBtn" href="javascript:;" class="normalBtn" onclick="sendPing()">
			            <span><i class="miniIcoCmd"></i>Ping</span>
			        </a>
				</td>
			</tr>
		</table>
	</div>
	<div class="edge5 pT0 pB0">
		<textarea id="content" readonly="readonly" class="pingTextArea"></textarea>
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
