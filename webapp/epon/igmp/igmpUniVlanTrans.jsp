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
    module igmpconfig
    IMPORT epon/igmp/igmpUniVlanTrans
</Zeta:Loader>
<style type="text/css">
	#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:420px;}
	#w1600{ width:1600px; position:absolute; top:0; left:0;}
	#step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
	#step1{ left:800px;}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var uniIndex = '${uniIndex}';
	var uniName = '${uniName}';
</script>
</head>
<body class="openWinBody">
	<div class="edge10 pT20">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr class="withoutBorderBottom">
	                 <td class="rightBlueTxt" width="100">
						@igmp.uniPort@:
	                 </td>
	                 <td width="180" id="putUniName">
						
	                 </td>
	                 <td class="rightBlueTxt" width="100">
						@igmp.vlanTrans@:
	                 </td>
	                 <td width="160">
						<input type="text" class="normalInput w150" id="transIndex" toolTip="@igmp.tip.tip26@" />
	                 </td>
	                 <td rowspan="2">
	                 	<a href="javascript:;" class="normalBtn" onclick="addUniVlanTrans()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a>
	                 </td>
	             </tr>
	             <tr class="withoutBorderBottom">
	                 <td class="rightBlueTxt" style="padding-top:0px;">
						OLD VLAN:
	                 </td>
	                 <td style="padding-top:0px;">
						<input type="text" class="normalInput w150" id="transOldVlan" maxlength = 4 toolTip="@igmp.tip.tip25@"/>
	                 </td>
	                 <td class="rightBlueTxt" style="padding-top:0px;">
						NEW VLAN:
	                 </td>
	                 <td style="padding-top:0px;">
						<input type="text" class="normalInput w150" id="transNewVlan" maxlength = 4 toolTip="@igmp.tip.tip25@" />
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<div id="putGridPanel" class="pT10"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
		         <li><a href="javascript:;" class="normalBtnBig" onclick="refreshUniVlanTrans()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>