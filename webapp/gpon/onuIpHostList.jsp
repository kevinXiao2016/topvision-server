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
    module gpon
    IMPORT gpon/onuIpHostList
</Zeta:Loader>
<script type="text/javascript">
    var entityId = '${onuAttribute.entityId}';
    var onuId = '${onuAttribute.onuId}';
    var onuIndex = '${onuAttribute.onuIndex}';
</script>
</head>
    <body class="newBody">
    	<div id="topPart">
	    	<div id="header">
		         <%@ include file="navigator.inc"%> 
		    </div>
		    <div class="pT10 pL10">
			    <table cellpadding="0" cellspacing="0" border="0" rules="none">
					<tr>
						<td class="blueTxt">@GPON.ipHostDisMode@</td>
						<td>
							<select class="normalSel w100" id='onuHostMode'>
								<option value='0'>@COMMON.pleaseSelect@</option>
	                     		<option value='1'>DHCP</option>
	                     		<option value='2'>STATIC</option>
	                     	</select> 
						</td>
						<td class="blueTxt pL5">IP/@GPON.mask@/@GPON.gateWay@/DNS@COMMON.maohao@</td>
						<td>
							<input id='selectValue' type="text" class="normalInput w120" />
						</td>
						<td class="blueTxt pL5">@GPON.VLANpriority@@COMMON.maohao@</td>
						<td>
							 <select class="normalSel w100" id='hostVlanPri'>
							 	<option value='-1'>@COMMON.pleaseSelect@</option>
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
						<td class="blueTxt pL5">VLAN ID@COMMON.maohao@</td>
						<td>
							<input id='hostVlanId' type="text" class="normalInput w100" toolTip="1-4094" maxlength="4" />
						</td>
						<td class="pL5">
							<a onclick="queryClick()" href="javascript:;" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
						</td>
					</tr>
				</table>
			</div>
	    </div>
    </body>
</Zeta:HTML>