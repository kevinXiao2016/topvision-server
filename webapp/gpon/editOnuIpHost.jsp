<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module onu
</Zeta:Loader>
<script type="text/javascript">
	var entityId = '${entityId}';
	var onuId = '${onuId}';
	var onuIndex = '${onuIndex}';
	var onuIpHostIndex = '${onuIpHostIndex}';
    Ext.onReady(function(){
    	$("#onuIpHostIndex").val(onuIpHostIndex);
    	$("#onuHostMode").val('${onuIpHost.onuIpHostAddressConfigMode}');
    	$("#hostIpAddr").val('${onuIpHost.onuIpHostAddress}');
    	$("#hostMask").val('${onuIpHost.onuIpHostSubnetMask}');
    	$("#hostGateway").val('${onuIpHost.onuIpHostGateway}');
    	$("#priDns").val('${onuIpHost.onuIpHostPrimaryDNS}');
    	$("#secondDns").val('${onuIpHost.onuIpHostSecondaryDNS}');
    	$("#hostVlanPri").val('${onuIpHost.onuIpHostVlanTagPriority}');
    	$("#hostVlanId").val('${onuIpHost.onuIpHostVlanPVid}');
    });//end document.ready;
    
    function saveClick(){
    	var onuIpHostIndex = $("#onuIpHostIndex").val();
    	var onuHostMode = $("#onuHostMode").val();
    	var hostIpAddr = $("#hostIpAddr").val();
    	var hostMask = $("#hostMask").val();
    	var hostGateway = $("#hostGateway").val();
    	var priDns = $("#priDns").val();
    	var secondDns = $("#secondDns").val();
    	var hostVlanPri = $("#hostVlanPri").val();
    	var hostVlanId = $("#hostVlanId").val();
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/gpon/onu/modifyOnuIpHost.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				onuId : onuId,
				onuIndex : onuIndex,
				onuIpHostIndex : onuIpHostIndex,
				onuHostMode : onuHostMode,
				hostIpAddr : hostIpAddr,
				hostMask : hostMask,
				hostGateway : hostGateway,
				priDns : priDns,
				secondDns : secondDns,
				hostVlanPri : hostVlanPri,
				hostVlanId : hostVlanId
			},
			success : function() {
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@COMMON.modify@ @COMMON.success@</b>'
	       	    });
				cancelClick();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.modify@ @COMMON.fail@");
			},
			cache : false
		});
	}
    
    function cancelClick(){
    	top.closeWindow('editOnuIpHost');
    }
</script>
</head>
    <body class="openWinBody">
    	<div class="openWinHeader">
		    <div class="openWinTip">@ONU.max64@</div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT60">
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		        	<tr>
		                <td class="rightBlueTxt" width="130">@ONU.index@@COMMON.maohao@</td>
		                <td width="190">
		                   <input id='onuIpHostIndex' type="text" disabled class="normalInput w180" />
		                </td>
		            </tr>
		            <tr>
		                <td class="rightBlueTxt" width="130">@ONU.ipHost@@COMMON.maohao@</td>
		                <td width="190">
		                    <select class="normalSel w200" id='onuHostMode'>
	                     		<option value='1'>DHCP</option>
	                     		<option value='2'>STATIC</option>
	                     	</select> 
		                </td>
		                <td width="130" class="rightBlueTxt">IP@COMMON.maohao@</td>
		                <td>
		                   <input id='hostIpAddr' type="text" class="normalInput w180" />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">@ONU.mask@@COMMON.maohao@</td>
		                <td>
		                   <input id='hostMask' type="text" class="normalInput w180" />
		                </td>
		                <td class="rightBlueTxt">@ONU.gate@@COMMON.maohao@</td>
		                <td>
		                   <input id='hostGateway' type="text" class="normalInput w180" />
		                </td>
		            </tr>
		            <tr>
		                <td class="rightBlueTxt">@ONU.dns@@COMMON.maohao@</td>
		                <td>
		                   <input id='priDns' type="text" class="normalInput w180" />
		                </td>
		                <td class="rightBlueTxt">@ONU.dns2@@COMMON.maohao@</td>
		                <td>
		                   <input id='secondDns' type="text" class="normalInput w180" />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">@ONU.WAN.vlanPriority@@COMMON.maohao@</td>
		                <td>
		                   <select class="normalSel w200" id='hostVlanPri'>
	                     		<option value='0'>0</option>
	                     		<option value='1'>1</option>
	                     		<option value='2'>2</option>
	                     		<option value='3'>3</option>
	                     		<option value='4'>4</option>
	                     		<option value='5'>5</option>
	                     		<option value='6'>6</option>
	                     		<option value='7'>7</option>
	                     	</select> 
		                </td>
		                <td class="rightBlueTxt">@ONU.vlanNum@@COMMON.maohao@</td>
		                <td>
		                   <input id='hostVlanId' type="text" class="normalInput w180" />
		                </td>
		            </tr>
		        </tbody>
		    </table>
		    <div class="noWidthCenterOuter clearBoth">
		        <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		            <li><a onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
		            <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		        </ol>
		    </div>
		</div>
    </body>
</Zeta:HTML>