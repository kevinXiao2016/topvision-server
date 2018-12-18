<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
</Zeta:Loader>
<script type="text/javascript">

function closeClick() {
    window.parent.closeWindow('addCmServiceType');
}
	
function addCmServiceType() {
	var fileName = $("#fileName").val();
    if (fileName.length > 127||fileName.length == 0) {
        $('#fileName').focus();
        return false;
    }
	var serviceType = $("#serviceType").val();
    if (serviceType.length > 64||serviceType.length == 0) {
        $('#serviceType').focus();
        return false;
    }
	$.ajax({
		  url: '/cm/addCmServiceType.tv',
		  data: {
			  'fileName' : fileName,
			  'serviceType' : serviceType
		  },
  	      type: 'post',
  	      success: function(response) {
  	    		top.afterSaveOrDelete({
  	   				title: '@COMMON.tip@',
  	   				html: '<b class="orangeTxt">@CM.addServiceTypeSuccess@</b>'
  	   			});
	  	    	closeClick();
			}, error: function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, '@CM.addServiceTypeFail@');
			}, cache: false
		});
}

</script>
</head>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@CM.addServiceType@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edge10">
			<table class="mCenter zebraTableRows" >
			<tr  class="darkZebraTr">
				<td class="rightBlueTxt">@CM.fileName@@COMMON.maohao@</td>
				<td><input class="normalInput w200" id="fileName" maxlength="127" toolTip='@cmServiceType.limit127@'/>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@CM.ServiceType@@COMMON.maohao@</td>
				<td><input class="normalInput w200" id="serviceType" maxlength="64" toolTip='@cmServiceType.limit64@'/>
				</td>
			</tr>
		</table>
		</div>
	<Zeta:ButtonGroup>
		<Zeta:Button id="saveBt" onClick="addCmServiceType()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="closeClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>