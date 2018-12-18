<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module univlanprofile
    css css/white/disabledStyle
    import epon.uniportvlan.uniPortVlan
</Zeta:Loader>
<style type="text/css">
body,html{ height:100%;}
.longTxt{ width:182px;}

</style>
<script type=""></script>
<script type="text/javascript">

var entityId = '${entityId}';
var uniId = '${uniId}';
var uniIndex = '${uniPortVlan.portIndex}';

</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">@UNIVLAN.uniVlanInfo@</div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="240">@epon/VLAN.portNum@:</td>
	                 <td width="180">
						${uniPortVlan.portString}
	                 </td>
	                 <td></td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">PVID:</td>
	                 <td>
						@epon/Optical.couldntGetData@
	                 </td>
	                 <td>
	                 </td>
	             </tr>
	             <tr>
	             	<td class="rightBlueTxt">@PROFILE.vlanMode@:</td>
	             	<td id="modeValue">
	             		@epon/Optical.couldntGetData@
	             	</td>
	             	<td>
	             	</td>
	             </tr>
	              <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">@UNIVLAN.profile@:</td>
	                 <td>
	                 	@epon/Optical.couldntGetData@
	                 </td>
	                 <td>
	                 </td>
	              </tr>
	         </tbody>
	     </table>
		 <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a href="javascript:;" class="normalBtnBig" onclick="refreshUniVlanData()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>