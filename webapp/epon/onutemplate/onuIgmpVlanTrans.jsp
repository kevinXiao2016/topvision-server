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
    module businesstemplate
    IMPORT epon/onutemplate/onuIgmpVlanTrans
</Zeta:Loader>
<style type="text/css">
	#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:420px;}
	#w1600{ width:1600px; position:absolute; top:0; left:0;}
	#step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
	#step1{ left:800px;}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var profileId = '${profileId}';
	var portId = '${portId}';
</script>
</head>
<body class="openWinBody">
	<div class="edge10 pT20">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr class="withoutBorderBottom">
	                 <td class="rightBlueTxt" width="120">
						@igmpconfig/igmp.vlanTrans@:
	                 </td>
	                 <td width="120">
						<input type="text" class="normalInput w120" id="transIndex" toolTip="@igmpconfig/igmp.tip.tip26@" />
	                 </td>
	                  <td class="rightBlueTxt" style="padding-top:0px;">
						OLD VLAN:
	                 </td>
	                 <td style="padding-top:0px;">
						<input type="text" class="normalInput w120" id="transOldVlan" toolTip="@igmpconfig/igmp.tip.tip43@"/>
	                 </td>
	                 <td class="rightBlueTxt" style="padding-top:0px;">
						NEW VLAN:
	                 </td>
	                 <td style="padding-top:0px;">
						<input type="text" class="normalInput w120" id="transNewVlan" toolTip="@igmpconfig/igmp.tip.tip43@" />
	                 </td>
	                 <td>
	                 	<a href="javascript:;" class="normalBtn" onclick="addVlanTrans()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a>
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<div id="putGridPanel" class="pT10"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a href="javascript:;" class="normalBtnBig" onclick="refreshVlanTrans()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>