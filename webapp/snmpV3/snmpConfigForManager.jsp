<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module snmpV3
</Zeta:Loader>
<script type="text/javascript">
    var userGrid;
    var entityId = ${entityId};
    var entitySnmpConfig  = ${entitySnmpConfig};
    var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
    $(document).ready(function(){
    	var param = entitySnmpConfig.data;
    	$("#readCommuntity").val(param.community);
    	$("#writeCommuntity").val(param.writeCommunity);
    });

    /**
     * 关闭页面
     */
    function closeHandler() {
        window.parent.closeWindow('emsSnmpConifg');
    }

    function fetchHandler() {
    	
    }
    
    function saveHandler(){
        var readCommuntity = $("#readCommuntity").val();
        var writeCommuntity = $("#writeCommuntity").val();
    	var reg = /^[a-zA-Z0-9-\s_:;/(),.'"*&^%$#@!=+|?<>\[\]\{\}\\`~]+$/;
    	if ( readCommuntity == "" || !reg.test(readCommuntity)) {
        	return Zeta$("readCommuntity").focus();
        }
    	if (writeCommuntity == "" || !reg.test(writeCommuntity)) {
        	return Zeta$("writeCommuntity").focus();
        }
    	var data = {};
    	data.entityId = entityId;
		data.snmpCommunity = readCommuntity;
		data.snmpWriteCommunity = writeCommuntity;
    	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SNMP.saving, 'ext-mb-waiting');
    	$.ajax({
    		url: "/config/modifySnmpV2CCfg.tv",cache:false,
    		data:data,
    		success:function(result){
       			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SNMP.saveOk);
                closeHandler.apply(this);
    		},error:function(){
    			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SNMP.saveEr);
    		}
    	})
    }
    
    function authLoad(){
    	if(!operationDevicePower){
	    	$("#readCommuntity").attr("disabled",true);
	    	$("#writeCommuntity").attr("disabled",true);
	    	R.saveBt.setDisabled(true);
    	}
    }
</script>
</head>
<body class="openWinBody" onload="authLoad()">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@SNMP.snmpCfg@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>

	<div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows">
				<tr>
					<td class="w200 rightBlueTxt">@SNMP.readCommunity@</td>
					<td><input maxlength='63' class="normalInput w200" id="readCommuntity" tooltip="@SNMP.communityTip@" /></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@SNMP.writeCommunity@</td>
					<td><input maxlength='63'
						id="writeCommuntity" class="normalInput w200" tooltip="@SNMP.communityTip@"/></td>
				</tr>
			</table>
		</form>
	</div>
	
 	<Zeta:ButtonGroup>
		<Zeta:Button id="saveBt" onClick="saveHandler()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
		<Zeta:Button onClick="closeHandler()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
	
</body>
</Zeta:HTML>
