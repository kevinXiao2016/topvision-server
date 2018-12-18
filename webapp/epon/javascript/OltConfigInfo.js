/****************************************
		CHECK SYSTEM TIME
 *****************************************/
function checkSysTiming() {
    window.parent.showConfirmDlg(I18N.COMMON.message, I18N.config.page.conCheckSysTime, function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, I18N.config.page.checkSysTiming, 'ext-mb-waiting');
        Ext.Ajax.request({
            url:"../epon/checkSysTiming.tv",
            success: function (text) {
                if (text != null && text.responseText == 'success') {
                    window.parent.closeWaitingDlg();
                    top.afterSaveOrDelete({
            			title: I18N.COMMON.tip,
            			html: I18N.config.page.checkSuccess
            		});
                    //window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.checkSuccess);
                } else {
                    window.parent.closeWaitingDlg();
                    window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.checkFail);
                }
            }, failure: function () {
            window.parent.closeWaitingDlg();
            window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.checkFail);
        }, params: {entityId : entityId}
        });
    });
}
/****************************************
 			SET THE TRAP
 *****************************************/
function trapSet() {
    window.parent.showConfirmDlg(I18N.COMMON.message, I18N.config.page.trapConfig, function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, I18N.config.page.trapSetting, 'ext-mb-waiting');
        Ext.Ajax.request({url:"/epon/alert/setOltTrapConfig.tv", params: {entityId : entityId},
            success: function (response) {
                var json = Ext.decode(response.responseText);
                if (json && json.message) {
                    window.parent.showMessageDlg(I18N.COMMON.message, json.message);
                } else {
                	top.afterSaveOrDelete({
            			title: I18N.COMMON.tip,
            			html: I18N.config.page.trapSetSuccess
            		});
                    //window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.trapSetSuccess);
                }
            }, failure: function () {
            window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.trapSetFail);
        }, params: {entityId : entityId}});
    });
}

/****************************************
 		RESET THE FORM 
 ****************************************/
function resetClick() {
    baseInfoModifiedFlag = 0;
    inbandParamModifiedFlag = 0;
    inbandVlanIdModifiedFlag = 0;
    outbandParamModifiedFlag = 0;
    snmpParamModified = 0;
    modifiedFlag = 0;
    Zeta$('entityForm').reset();
    $('#emsSnmpVersion').val(snmpVersion);
    $('#snmpVersion').val(snmpVersion);
    $("#location").val(roomName);
    $("#rackList").val(rackNum);
    $("#frameList").val(frameNum);
	
    Zeta$('inbandVlanId').value = inbandVlanId;
    emsSnmpChanged();
    setIpValue("inbandIp", inbandIp);
    setIpValue("outbandIp", outbandIp);
    setIpValue("ipAddress", ipAddress);
    setIpValue("manageHostMask", manageHostMask);
    setIpValue("inbandMask", inbandMask);
    setIpValue("outbandMask", outbandMask);
    setIpValue("emsIpAddress", emsIpAddress);
}

//只能输入英文、数字、下划线
//由于输入单引号、双引号时，会导致资源列表的超链接出问题，增加此限制，Added by huangdongsheng
function validateAlias(str){
  var reg = /^[\w\d\u4e00-\u9fa5\[\]\(\)-\/]*$/;
  return reg.test(str);
}

//校验olt名称
function checkOltName(){
	var reg1 = /^([a-z._|~`{}<>''""?:\\\/\(\)\[\]\-\s\d,;*!@#$%^&=+])+$/ig;
	var oltName = $("#oltName").val();
	if(reg1.exec(oltName)){
		//符合校验规则
		return true;
	}else{
		//不符合校验规则
		return false;
	}
}

//校验机框号输入
function checkFrameNo(){
	var frameNo = $("#frameList").val();
	if(isNaN(frameNo)){
		//不符合校验规则
		return true;
	}else{
		if(parseInt(frameNo) <= 0 || parseInt(frameNo) > 100){
			return true;
		}else{
			return false;
		}
		//符合校验规则
	}
}

//校验机架号输入
function checkRackNo(rackNo){
	var rackNo = $("#rackList").val();
	if(isNaN(rackNo)){
		//不符合校验规则
		return true;
	}else{
		if(parseInt(rackNo) <= 0 || parseInt(rackNo) > 65535){
			return true;
		}else{
			return false;
		}
		//符合校验规则
	}
}


/*function locationChanged() {
    var sel = $("#location");
	$("#rackList").empty().append("<option value=''>"+I18N.config.page.select+"</option>");
	$("#frameList").empty().append("<option value=''>"+I18N.config.page.select+"</option>");
	$("#frameListEnd").empty().append("<option value=''>"+I18N.config.page.select+"</option>");
    if(sel.val() != ""){
		for(var x=0; x<roomList.length; x++){
			if(sel.val() == roomList[x].enName){
				var rackSel = $("#rackList");
				var tmp = roomList[x].rackList;
				for(var y=0; y<tmp.length; y++){
					rackSel.append(String.format("<option value='{0}'>{0}:{1}</option>", tmp[y].rackNum, tmp[y].rackName));
				}
				return;
			}
		}
    }
}*/
function rackChange() {
	var sel = $("#rackList");
	var sel1 = $("#frameList");
	var sel2 = $("#frameListEnd");
	sel1.empty().append("<option value=''>"+I18N.config.page.select+"</option>");
	sel2.empty().append("<option value=''>"+I18N.config.page.select+"</option>");
	if(sel.val() != "" && sel.val() != null){
		for(var a=0; a<roomList.length; a++){
			if($("#location").val() == roomList[a].enName){
				var tmpR = roomList[a].rackList;
				for(var b=0; b<tmpR.length; b++){
					var tmpB = tmpR[b];
					if(tmpB.rackNum == sel.val()){
						var notFindNum = new Array();
						notFindNum.push(0);
						notFindNum.push(totleFrameNum + 1);
						for(var i=1; i<totleFrameNum+1; i++){
							if(tmpB.frameList.indexOf(i) == -1){
								notFindNum.push(i);
							}
						}
						for(var c=0; c<tmpB.frameList.length; c++){
							var tmpFrameNum = tmpB.frameList[c];
							var tmpFFF = [true, true];
							var tmpNotF = notFindNum.length;
							if(tmpNotF > 0){
								for(var t=0; t<tmpNotF; t++){
									if(tmpFrameNum > notFindNum[t] - deviceNeedFrameNum && tmpFrameNum <= notFindNum[t]){
										tmpFFF[0] = false;
									}
									if(tmpFrameNum >= notFindNum[t] && tmpFrameNum < notFindNum[t] + deviceNeedFrameNum){
										tmpFFF[1] = false;
									}
								}
							}
							if(tmpFFF[0]){
								sel1.append(String.format("<option value='{0}'>{0}</option>", tmpFrameNum));
							}
							if(tmpFFF[1]){
								sel2.append(String.format("<option value='{0}'>{0}</option>", tmpFrameNum));
							}
						}
						return;
					}
				}
			}
		}
	}
}
function frameChange(s) {//1:start, 2:end
	if(s == 1){
		var sel = $("#frameList");
		if(sel.val() == ''){
			$("#frameListEnd").val("");
		}else{
			var tmp = deviceNeedFrameNum - 1 + parseInt(sel.val());
			$("#frameListEnd").val(tmp);
		}
	}else if(s == 2){
		var sel = $("#frameListEnd");
		if(sel.val() == ""){
			$("#frameList").val("");
		}else{
			$("#frameList").val(parseInt(sel.val()) - deviceNeedFrameNum + 1);
		}
	}
}
//end of location
