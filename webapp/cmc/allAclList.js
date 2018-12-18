function deleteAcl(){
  	 var aclID = $("#topCcmtsAclListIndex").val();
     if(aclID == null || aclID == ''){
     	window.parent.showMessageDlg('@COMMON.tip@', '@CMCACL.selectAcl@');
     	return;
     }
	 window.parent.showConfirmDlg('@COMMON.tip@', '@CMC.acl.confirmDelete@', function(type) {
		if (type == 'no') {
	      return;
	    } else {
	    	var upOut = $("#installPosionUpOut").attr("checked");
	        var upIn = $("#installPosionUpIn").attr("checked");
	        var caOut = $("#installPosionCaOut").attr("checked");
	        var caIn = $("#installPosionCaIn").attr("checked");

	        var upOut = $("input[type='hidden'][name='aclInfo.installPosionUpOut']").val()=="1";
	        var upIn = $("input[type='hidden'][name='aclInfo.installPosionUpIn']").val()=="1";
	        var caOut = $("input[type='hidden'][name='aclInfo.installPosionCaOut']").val()=="1";
	        var caIn = $("input[type='hidden'][name='aclInfo.installPosionCaIn']").val()=="1";
	      
	        if(upOut||upIn||caOut||caIn){
	            window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.aclNotAllowDelete@');
	        }else{
	            var delUrl = "/cmcacl/deleteAcl.tv";
	            
	            $.ajax({
	                url: delUrl,
	                type: 'post',
	                data: {cmcId:cmcId,aclID:aclID},
	                success: function(responseText) {
	                    var returnObj = responseText;
	                    reloadOnePositionAclList(0);
	                   // window.top.showMessageDlg('@COMMON.tip@', '@CMC.text.doingsettingssuccess@');
					    top.afterSaveOrDelete({
				   			title: '@COMMON.tip@',
				   			html: '<b class="orangeTxt">@CMC.text.delsuccess@</b>'
				   		});
	                    $("#aclInfoForm")[0].reset();
	                }, error: function(response) {
	                    window.parent.showMessageDlg('@COMMON.tip@',  '@CMC.text.doingsettingsfailed@');
	                }, cache: false
	            });
            }
	    }
	});
}
/*
 * 判断一个输入是否符合vlan Tpid的范围:0-FFFF，返回布尔值。
 * 
 */
function isVlanTpid(value){
    var reg = /^[0-9a-fA-F]+$/;
    var isNumber =  reg.test(value);
    if(isNumber){
        var temp = parseInt(value,16);
        isNumber = temp>=0&&temp<=65535;
        }
    return isNumber;
}
/*
 * 判断一个输入是否是一个范围内的数字，返回布尔值。
 */
function isNumberFromTo(value,start,end){

    
    var reg = /^\d+$/;
    var isNumber =  reg.test(value);
    if(isNumber){
        var temp = value/1;
        isNumber = temp>=start&&temp<=end;
        }
    return isNumber;

    
}

//检查是否为组播IP，是则返回 true
function checkIsMulticast(ipAddr){
    var ip = ipAddr.split(".");
    if(parseInt(ip[0])>=224 && parseInt(ip[0])<=239){
        return true;
    }
    return false;
}
//检查是否为保留IP，是则返回 true
function checkIsReservedIp(ipAddr){ 
    var ip = ipAddr.split(".");
    if(parseInt(ip[0])==0 || parseInt(ip[0])==127){
        return true;
    }
    if(parseInt(ip[0])==191 && parseInt(ip[1])==255){
        return true;
    }
    if(parseInt(ip[0])==192 && parseInt(ip[1])==0 && parseInt(ip[2])==0){
        return true;
    }
    if(parseInt(ip[0])==223 && parseInt(ip[1])==255 && parseInt(ip[2])==255){
        return true;
    }
    return false;
}
//检查是否为A类、B类、C类Ip，是则返回true
function checkIsNomalIp(ipAddr){
    var ip = ipAddr.split(".");
    if(parseInt(ip[0])>=1 && parseInt(ip[0])<=223 && parseInt(ip[0])!=127){
        return true;
    }
    return false;
}
//检验IP是否格式正确
function checkedIpValue(ipAddr){
    var ip = ipAddr.split(".");
    if(ip.length != 4){
        return false;
    }else{
        for(var k=0; k<4; k++){
            if(ip[k].length==0 || isNaN(ip[k]) || ip[k].length>3 || parseInt(ip[k])<0 || parseInt(ip[k])>255){
                return false
            }
        }
    }
    return true;
}
//检验IP mask是否格式正确
function checkedIpMask(ipMask){
    if("0.0.0.0" == ipMask){
        window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.aclIpMaskMustNot0@");
        return false;
    }
    if(checkedIpValue(ipMask)){
        var ip = ipMask.split(".");
        var ip_binary = (parseInt(ip[0]) + 256).toString(2).substring(1) + (parseInt(ip[1]) + 256).toString(2).substring(1)
                        + (parseInt(ip[2]) + 256).toString(2).substring(1) + (parseInt(ip[3]) + 256).toString(2).substring(1);
        if (ip_binary.indexOf("01") == -1){
            return true;
        }
    }
    window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.aclIpMaskError@");
    return false;
}
function checkIsIP(ipAddr){

    if(!checkedIpValue(ipAddr)){
        window.parent.showMessageDlg('@COMMON.tip@',  '@CMC.tip.aclIpFormatError@');
        return false;
    }
    /**  ACL功能就是为了对报文进行二次处理，不要求对匹配IP条件进行控制，应交给操作人员灵活配置
    if(checkIsMulticast(ipAddr)){
        window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.aclIpCanNotMulticast@');
        return false;
    }
    if("0.0.0.0" == ipAddr){
        window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.aclIpCanNotBe0@");
        return false;
    }
    if("255.255.255.255" == ipAddr){
        window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.aclIpCanNotBe255@");
        return false;
    }
    if(!checkIsNomalIp(ipAddr)){
        window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.aclIpOnlyCanBeABC@');
        return false;
    }
    if(checkIsReservedIp(ipAddr)){
        window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.aclIpCanNotBeReserved@');
        return false;
    }
    **/
    return true;
}
function validateAcl(){

    var aclID = $('#topCcmtsAclListIndex').val();
    var isValidAclID = isNumberFromTo(aclID,1,192);
    if(!isValidAclID){
        $('#topCcmtsAclListIndex').trigger('focus');
        return false;
        }
    var aclDesc = $.trim($('#topCcmtsAclDesc').val());
    if(aclDesc.length>64){
        $('#topCcmtsAclDesc').trigger('focus');
        return false;
        }

    var isAnyMatch = false;
    var srcIp = getIpValue("topMatchSrcIp");
    var srcIpMask = getIpValue("topMatchSrcIpMask");
    
    if(srcIp!=""&&srcIpMask!=""){
        isAnyMatch = isAnyMatch||true;
        }
    if((srcIp!=""&&srcIpMask=="")||(srcIp==""&&srcIpMask!="")){
        window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.aclSrcIPAndMask@');
        
        return false;
        }
    if(srcIp!=""&&srcIpMask!=""){

        var isIP = checkIsIP(srcIp);
        if(!isIP){
            return false;
            }
        
        var isMask = checkedIpMask(srcIpMask);
        if(!isMask){
            return false;
            }
        }

    var dstIp = getIpValue("topMatchDstIp");
    var dstIpMask = getIpValue("topMatchDstIpMask");
    
    if(dstIp!=""&&dstIpMask!=""){
        isAnyMatch = isAnyMatch||true;
        }
    if((dstIp!=""&&dstIpMask=="")||(dstIp==""&&dstIpMask!="")){
        window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.aclDstIPAndMask@');
        return false;
        }
    if(dstIp!=""&&dstIpMask!=""){

        var isIP = checkIsIP(dstIp);
        if(!isIP){
            return false;
            }
        
        var isMask = checkedIpMask(dstIpMask);
        if(!isMask){
            return false;
            }
        }

    var srcMac = $('#topMatchlSrcMac').val();
    var srcMacMask = $('#topMatchSrcMacMask').val();
    if(srcMac && srcMac != "" && !Validator.isMac(srcMac)){
        $('#topMatchlSrcMac').focus();
        return false;
    }
    if(srcMacMask && srcMacMask != "" && !Validator.isMac(srcMacMask)){
        $('#topMatchSrcMacMask').focus();
        return false;
    }
    //mac地址源码掩码规则需要进一步校验
    if(srcMacMask && srcMacMask != "" && !Validator.isMacMask(srcMacMask)){
    	$('#topMatchSrcMacMask').focus();
    	window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.maskError@');
        return false;
    }

    if(srcMac!=""&&srcMacMask!=""){
        isAnyMatch = isAnyMatch||true;
        }
    if((srcMac!=""&&srcMacMask=="")||(srcMac==""&&srcMacMask!="")){
        window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.aclSrcMacAndMask@');
        return false;
        }

    var dstMac = $('#topMatchDstMac').val();
    var dstMacMask = $('#topMatchDstMacMask').val();
    if(dstMac && dstMac != "" && !Validator.isMac(dstMac)){
        $('#topMatchDstMac').focus();
        return false;
    }
    if(dstMacMask && dstMacMask != "" && !Validator.isMac(dstMacMask)){
        $('#topMatchDstMacMask').focus();
        return false;
    }
  	//mac地址源码掩码规则需要进一步校验
    if(dstMacMask && dstMacMask != "" && !Validator.isMacMask(dstMacMask)){
    	$('#topMatchDstMacMask').focus();
    	window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.dstmaskError@');
        return false;
    }

    if(dstMac!=""&&dstMacMask!=""){
        isAnyMatch = isAnyMatch||true;
        }
    if((dstMac!=""&&dstMacMask=="")||(dstMac==""&&dstMacMask!="")){
        window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.aclDstMacAndMask@');

        return false;
        }

    var srcPort = $('#topMatchSrcPort').val();
    if(srcPort!=""){
        var isValidSrcPort = isNumberFromTo(srcPort,1,65535);
        if(!isValidSrcPort){
            $('#topMatchSrcPort').trigger('focus');
            return false;
            }
        else{
            isAnyMatch = isAnyMatch||true;
            }
    }

    var dstPort = $('#topMatchDstPort').val();
    if(dstPort!=""){
        var isValidDstPort = isNumberFromTo(dstPort,1,65535);
        if(!isValidDstPort){
            $('#topMatchDstPort').trigger('focus');
            return false;
            }
        else{
            isAnyMatch = isAnyMatch||true;
            }
    }

    var vlanId = $('#topMatchVlanId').val();
    if(vlanId!=""){
        var isValidVlanId = isNumberFromTo(vlanId,1,4094);
        if(!isValidVlanId){
        	//if($('#topMatchVlanId').attr('disabled'))
            	$('#topMatchVlanId').trigger('focus');
            return false;
            }
        else{
            isAnyMatch = isAnyMatch||true;
            }
    }


    var vlanCos = $('#topMatchVlanCos').val();
    if(vlanCos!=""){
        if(vlanId==""){
        	//if($('#topMatchVlanId').attr('disabled'))
            	$('#topMatchVlanId').trigger('focus');
            return false;
            }
        var isValidVlanCos = isNumberFromTo(vlanCos,0,7);
        if(!isValidVlanCos){
            $('#topMatchVlanCos').trigger('focus');
            return false;
            }
        else{
            isAnyMatch = isAnyMatch||true;
            }
    }

    var eType = $('#topMatchEtherType').val();
    if(eType!=""){
        var isValidEType = isNumberFromTo(eType,1,65535);
        if(!isValidEType){
            $('#topMatchEtherType').trigger('focus');
            return false;
            }
        else{
            isAnyMatch = isAnyMatch||true;
            }
    }

    var ipPro = $('#topMatchIpProtocol').val();
    if(ipPro!=""){
        var isValidProtocol = isNumberFromTo(ipPro,0,255);
        if(!isValidProtocol){
            $('#topMatchIpProtocol').trigger('focus');
            return false;
            }
        else{
            isAnyMatch = isAnyMatch||true;
            }
    }

    var dscp = $('#topMatchDscp').val();
    if(dscp!=""){
        var isValidDscp = isNumberFromTo(dscp,0,63);
        if(!isValidDscp){
            $('#topMatchDscp').trigger('focus');
            return false;
            }
        else{
            isAnyMatch = isAnyMatch||true;
            }
    }

    
    var actionValue = $("input[type=radio][name=aclInfo.actDenyOrPermit][checked=true]").val();
    var isAnyAction = actionValue != "0";
    if(actionValue == "1"){
        if(!isAnyMatch){
            window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.aclActionNeedMatch@');

            return false;
            }
        var inputValue = "";
        var isChecked = $('#aclVlanActionReplaceVlanID').attr('checked');
        
        if(isChecked){
            inputValue = $('#topActionNewVlanId').val();
            if(inputValue==""||!isNumberFromTo(inputValue,1,4094)){
                $('#topActionNewVlanId').trigger('focus');
                    return false;
                }
            }
        
        isChecked = $('#aclVlanActionReplaceVlanCos').attr('checked');
        if(isChecked){
            inputValue = $('#topActionNewVlanCos').val();
            if(inputValue==""||!isNumberFromTo(inputValue,0,7)){
                $('#topActionNewVlanCos').trigger('focus');
                    return false;
                }
            }

        isChecked = $('#aclVlanActionReplaceVlanTpid').attr('checked');
        if(isChecked){
            inputValue = $('#topActionNewVlanTpid').val();
            if(inputValue==""||!isVlanTpid(inputValue)){
                $('#topActionNewVlanTpid').trigger('focus');
                    return false;
                }
            }
        
        isChecked = $('#aclVlanActionReplaceVlanCfi').attr('checked');
        if(isChecked){
            inputValue = $('#topActionNewVlanCfi').val();
            if(inputValue==""||!isNumberFromTo(inputValue,0,1)){
                $('#topActionNewVlanCfi').trigger('focus');
                    return false;
                }
            }

        isChecked = $('#aclVlanActionNewVlan').attr('checked');
        if(isChecked){
            inputValue = $('#topActionAddVlanId').val();
            if(inputValue==""||!isNumberFromTo(inputValue,1,4094)){
                $('#topActionAddVlanId').trigger('focus');
                    return false;
                }
            inputValue = $('#topActionAddVlanCos').val();
            if(inputValue != "" && !isNumberFromTo(inputValue,0,7)){
                $('#topActionAddVlanCos').trigger('focus');
                    return false;
                }
            inputValue = $('#topActionAddVlanTpid').val();
            if(inputValue!=""&&!isVlanTpid(inputValue)){
                $('#topActionAddVlanTpid').trigger('focus');
                    return false;
                }
            inputValue = $('#topActionAddVlanCfi').val();
            if(inputValue!=""&&!isNumberFromTo(inputValue,0,1)){
                $('#topActionAddVlanCfi').trigger('focus');
                    return false;
                }
            }   

        isChecked = $('#aclActionReplaceIpTos').attr('checked');
        if(isChecked){
            inputValue = $('#topActionNewIpTos').val();
            if(inputValue==""||!isNumberFromTo(inputValue,0,255)){
                $('#topActionNewIpTos').trigger('focus');
                    return false;
                }
            }

        isChecked = $('#aclActionReplaceIpDscp').attr('checked');
        if(isChecked){
            inputValue = $('#topActionNewIpDscp').val();
            if(inputValue==""||!isNumberFromTo(inputValue,0,63)){
                $('#topActionNewIpDscp').trigger('focus');
                    return false;
                }
            }
    }
    var upOut = $("#installPosionUpOut").attr("checked");
    var upIn = $("#installPosionUpIn").attr("checked");
    var caOut = $("#installPosionCaOut").attr("checked");
    var caIn = $("#installPosionCaIn").attr("checked");
    if((upOut||upIn||caOut||caIn)&&!isAnyAction){
        window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.aclPositionNeedAction@');
        return false;
    }
    
    //TODO 需要判断该放置点是否已经满配(每个放置点最多可放置48个)
    var refreshPosition = 0;
    if(upIn){
        refreshPosition =1;
    }else if(upOut){
        refreshPosition =2;
    }else if(caOut){
        refreshPosition =3;
    }else if(caIn){
        refreshPosition =4;
    }
    var result = true;
    if(refreshPosition!==0){
	    $.ajax({
	    	url : '/cmcacl/countPositionAcl.tv',
	    	data : {cmcId : cmcId, position : refreshPosition},
	    	dataType : 'json',
	    	async : false,
	    	success : function(response){
	    		var aclCount =  response.count;
	    		if(aclCount>=48){
	    			window.parent.showMessageDlg('@COMMON.tip@', '@CMC.acl.maxSize@');
	    			result = false;
	    		}
	    	},
	    	error : function(response){
	    	},
	    	cache : false
	    });
    }
    return result;
}

function saveAcl(){
    var isValidate = validateAcl();
    if(!isValidate){return;}
    var saveUrl = "/cmcacl/addAcl.tv?cmcId="+cmcId;

    //根据aclInfoPanel.aclID判断是否是新建还是修改
    if(aclInfoPanel.aclID){
        saveUrl = "/cmcacl/modifyAcl.tv?cmcId="+cmcId;
    }
    
    //saveUrl = "/cmcacl/modifyAcl.tv";
    var topMatchSrcIp = getIpValue("topMatchSrcIp");
    var topMatchSrcIpMask = getIpValue("topMatchSrcIpMask");
    var topMatchDstIp = getIpValue("topMatchDstIp");
    var topMatchDstIpMask = getIpValue("topMatchDstIpMask");

    $("input[type='hidden'][name='aclInfo.topMatchSrcIp']").val(topMatchSrcIp);
    $("input[type='hidden'][name='aclInfo.topMatchSrcIpMask']").val(topMatchSrcIpMask);
    $("input[type='hidden'][name='aclInfo.topMatchDstIp']").val(topMatchDstIp);
    $("input[type='hidden'][name='aclInfo.topMatchDstIpMask']").val(topMatchDstIpMask);

    var upOut = $("#installPosionUpOut").attr("checked");
    var upIn = $("#installPosionUpIn").attr("checked");
    var caOut = $("#installPosionCaOut").attr("checked");
    var caIn = $("#installPosionCaIn").attr("checked");

    $("input[type='hidden'][name='aclInfo.installPosionUpOut']").val(upOut?"1":"0");
    $("input[type='hidden'][name='aclInfo.installPosionUpIn']").val(upIn?"1":"0");
    $("input[type='hidden'][name='aclInfo.installPosionCaOut']").val(caOut?"1":"0");
    $("input[type='hidden'][name='aclInfo.installPosionCaIn']").val(caIn?"1":"0");

    var refreshPosition = 0;
    if(upIn){
        refreshPosition =1;
    }else if(upOut){
        refreshPosition =2;
    }else if(caOut){
        refreshPosition =3;
    }else if(caIn){
        refreshPosition =4;
    }
    
    window.top.showWaitingDlg("@I18N.COMMON.wait@", "@text.configuring@", 'ext-mb-waiting');
    $('#topCcmtsAclListIndex').attr('disabled', false);
	$.ajax({
	    url: saveUrl,
	    type: 'post',
	    data: $("#aclInfoForm").serialize(),
	    success: function(responseText) {
	        var returnObj = responseText;
	        reloadOnePositionAclList(refreshPosition);
	        //window.top.showMessageDlg('@COMMON.tip@', '@CMC.tip.setSuccess@');
	        window.top.closeWaitingDlg();
	        top.afterSaveOrDelete({
	   			title: '@COMMON.tip@',
	   			html: '<b class="orangeTxt">@CMC.tip.setSuccess@</b>'
	   		});
	        var aclId = $("#topCcmtsAclListIndex").val();
	        aclInfoPanel.reload(aclId); 
	    }, error: function(response) {
	        window.parent.showMessageDlg('@COMMON.tip@', '@CMC.text.doingsettingsfailed@');
	    }, cache: false
	});
}

function matchActionChanged(act){
    for(var i=0;i<checkIDArray.length;i++){
        var checkObj = checkIDArray[i];
        var inputArray = checkObj.inputIDs;

                $('#'+checkObj.checkID).attr("checked",false);
                $('#'+checkObj.checkID).attr("disabled",act!=1);
                for(var m=0;m<inputArray.length;m++){
                    $('#'+inputArray[m]).val("");
                    $('#'+inputArray[m]).attr("disabled",true);
                  }
        }

    $('#aclCPUActionMask').attr("checked",false);
    $('#aclCPUActionMask').attr("disabled",act!=1);
    
        var isNoAct = act==0;
        $("#installPosionUpOut").attr("disabled",isNoAct);
        $("#installPosionUpIn").attr("disabled",isNoAct);
        $("#installPosionCaOut").attr("disabled",isNoAct);
        $("#installPosionCaIn").attr("disabled",isNoAct);
}

function newAcl(){
     $("#aclInfoForm")[0].reset();
     aclInfoPanel.reload();
}

function vlanActionCheckChanged(obj){
    var objID = obj.id;
    var isChecked = obj.checked;
    for(var i=0;i<checkIDArray.length;i++){
        var checkObj = checkIDArray[i];
        var inputArray = checkObj.inputIDs;
        if(checkObj.checkID == objID ){
            for(var m=0;m<inputArray.length;m++){
                $('#'+inputArray[m]).val("");
                $('#'+inputArray[m]).attr("disabled",!isChecked);
                }
            }else{
                $('#'+checkObj.checkID).attr("checked",false);
                for(var m=0;m<inputArray.length;m++){
                    $('#'+inputArray[m]).val("");
                    $('#'+inputArray[m]).attr("disabled",true);
                }
            }
	}
}

function refreshAllAcl(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/cmcacl/refreshAllCmcAcl.tv',
		type : 'POST',
		data : {
			cmcId : cmcId
		},
		success : function() {
			//window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
			top.afterSaveOrDelete({
       	      title: "@COMMON.tip@",
       	      html: "@COMMON.fetchOk@"
       	    });
			window.location.reload();
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

function viewPositionDefAct(positionNum,defAct){
    $('#normalAclInfoDiv').attr('style','display:none');
    $('#defActInfoDiv').attr('style','display:block');
    var desc = "";
    if(positionNum == 1){
        desc = '@CMC.label.aclPositionUpIn@@CMC.label.aclActionDefAct@';
        }else if(positionNum == 2){
        desc = '@CMC.label.aclPositionUpOut@@CMC.label.aclActionDefAct@';
        }else if(positionNum == 3){
        desc = '@CMC.label.aclPositionCaOut@@CMC.label.aclActionDefAct@';
        }else if(positionNum == 4){
        desc = '@CMC.label.aclPositionCaIn@@CMC.label.aclActionDefAct@';
        }
    $('#positionNameLabel').attr('innerText',desc); 
    $("input[type='hidden'][name='defAct.topAclPositionIndex']").val(positionNum);
    $("input[type=radio][name='defAct.topPositionDefAction'][value="+defAct+"]").attr("checked","checked");
}

function renderActCol(value, p, record){
    var data = record.data;
    var actDenyOrPermit = data.actDenyOrPermit;
    var actReplaceCos = data.actReplaceCos;
    var actAddVlan = data.actAddVlan;
    var actRemoveVlan = data.actRemoveVlan;
    var actCopyToCpu = data.actCopyToCpu;

    var actMsg = '@CMC.label.aclActionNo@';
    if(actDenyOrPermit==2){
        actMsg = '@CMC.label.aclActionDeny@';
    }else if(actDenyOrPermit == 1){
        
        actMsg = '@CMC.label.aclActionPermit@';
    }
    if(data.topCcmtsAclListIndex == 0){
        actMsg = (data.defAct == 1?'@CMC.label.aclActionPermit@':'@CMC.label.aclActionDeny@');
        }
    return actMsg;
    
}

function renderDesc(value,p,record){
    var aclID = record.data.topCcmtsAclListIndex;
    if(aclID == 0){
        var defPosition = record.data.defActPosion;
        var desc = "";
        if(defPosition == 1){
            desc = '@CMC.label.aclPositionUpIn@';
            }else if(defPosition == 2){
            desc = '@CMC.label.aclPositionUpOut@';
            }else if(defPosition == 3){
            desc = '@CMC.label.aclPositionCaOut@';
            }else if(defPosition == 4){
            desc = '@CMC.label.aclPositionCaIn@';
            }
        return desc;
        }else{
            return value;
        }
    
}

function renderAclID(value,p,record){
    if(value == 0){
            
        return '@CMC.label.aclActionDefAct@';
        }else{
            return value;
        }
}

function renderPrority(value,p,record){
    if(value==0){
        return "@TEXT.lowest@";
        }else if(value ==15){
        return "@TEXT.highest@";
            }else if(value>0&&value<6){
             return "@TEXT.low@"+ '('+value+')';
            }else if(value>5&&value<11){
                return "@TEXT.medium@" + '('+value+')';
            }else{
                return "@TEXT.high@" + '('+value+')';
            }
}

function reloadOnePositionAclList(positionNum){
	
	allAclStore.load({
        params:{position:positionNum}
     });
    /*
    *  当重新刷新页面的时候，输入值为 undefined，后台从设备重新获取数据，然后显示“未安装”的acl列表
    *
    */
    if(positionNum == undefined){
    	positionNum = 0;
        }
    for(var i=0;i<5;i++){
        if(i!=positionNum){
            Ext.getCmp('position'+i).removeClass('seclectedPosition');

            }else{
                Ext.getCmp('position'+i).addClass('seclectedPosition');
                }
        }
}

//清空匹配规则
function clearMatchRules(){
	setIpValue("topMatchSrcIp", '');
    setIpValue("topMatchSrcIpMask", '');
    setIpValue("topMatchDstIp", '');
    setIpValue("topMatchDstIpMask", '');

    $('#topMatchlSrcMac').val('');
    $('#topMatchSrcMacMask').val('');
    $('#topMatchDstMac').val('');
    $('#topMatchDstMacMask').val('');

    $('#topMatchSrcPort').val('');
    $('#topMatchDstPort').val('');
    $('#topMatchVlanId').val('');
    $('#topMatchVlanCos').val('');
    $('#topMatchEtherType').val('');
    $('#topMatchIpProtocol').val('');
    $('#topMatchDscp').val('');
}

function modifyPositionDefAct(){
    
    //defaultActForm
    var saveUrl = "/cmcacl/modifyDefAct.tv?cmcId="+cmcId;
    var positionNum = $("input[type='hidden'][name=defAct.topAclPositionIndex]").val();
    
    $.ajax({
        url: saveUrl,
        type: 'post',
        data: $("#defaultActForm").serialize(),
        success: function(responseText) {
            var returnObj = responseText;
            reloadOnePositionAclList(positionNum);
            window.top.showMessageDlg('@COMMON.tip@', '@CMC.text.doingsettingssuccess@');
        }, error: function(response) {
            window.parent.showMessageDlg('@COMMON.tip@', '@CMC.text.doingsettingsfailed@');
        }, cache: false
    });

}

function authLoad(){
	if(!operationDevicePower){
	    $("#newBt").attr("disabled",true);
	    $("#saveBt").attr("disabled",true);
	    $("#finishBt").attr("disabled",true);
	}
	if(isNewAclActionMask == "true"){
		$("#noAction").attr("disabled",true)
		$('#noAction_container').hide();
	}
}