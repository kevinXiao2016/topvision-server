function syncMode(authMode, clear){
	var sn = $("#sn"),
		password = $("#password"), 
		loid = $("#loid"),
		loidPassword =$("#loidPassword");
	$(".authClazz").attr("disabled",true);
	$("label.authClazz").hide();
	inited && $("input.authClazz").val("");
	lineComboBox.clearValue();
	srvComboBox.clearValue();
	if(clear){
		sn.val('');
		password.val('');
		loid.val('');
		loidPassword.val('');
	}
	if(typeof authMode == 'string'){
		authMode = parseInt(authMode);
	}
	R.addAuthBt.setDisabled(false);
	$("#gponAutoAuthConfig").attr("disabled",true);
	$("#gponAutoAuthConfig").hide();
	switch(authMode){
	case 1:
		$(".sn").attr("disabled",false);
		$("label.sn").show();
		break;
	case 2:
		$(".snpassword").attr("disabled",false);
		$("label.snpassword").show();
		break;
	case 3:
		$(".loid").attr("disabled",false);
		$("label.loid").show();
		break;
	case 4:
		$(".loidpassword").attr("disabled",false);
		$("label.loidpassword").show();
		break;
	case 5:
		$(".password").attr("disabled",false).filter("label").hide();
		$("label.password").show();
		break;
	case 6:
		$("#gponAutoAuthConfig").attr("disabled",false);
		$("#gponAutoAuthConfig").show();
		R.addAuthBt.setDisabled(true);
		break;
	case 7:
		$(".mix").attr("disabled",false);
		$("label.mix").show();
		break;
	}
}
function validateModule(authMode){
	 var $sn = $("#sn");
	 var $password = $("#password");
	 var $loid = $("#loid");
	 var $loidPassword= $("#loidPassword");
	 var $srvProfileId = $("#srvProfileId");
	 var $lineProfileId = $("#lineProfileId");
	 var srvProfileId= srvComboBox.getValue();
	 var lineProfileId= lineComboBox.getValue();
	 var mixMode = false;
	 if(authMode == 7){
		 mixMode = true;
	 }
	 var foundMix = false;
	 if(!$sn.prop("disabled")){
		 var $snValue = $sn.val();
		 if((mixMode && $snValue != "") || !mixMode ){
			 if($snValue.contains(":")){
				 var reg=  /^([0-9a-zA-Z]{2}:){7}[0-9a-zA-Z]{2}$/;
				 if(!reg.test($snValue)){
					return $sn.focus() && false;
				 }			 
			 }else{
				 var reg=  /^[0-9a-zA-Z]{16}$/;
				 if(!reg.test($snValue)){
					return $sn.focus() && false;
				 }
			 }
			 foundMix =true;
		 }
	 }
	 if(!$password.prop("disabled")){
		 var reg=  /^[0-9a-zA-Z]{1,10}$/;
		 if((mixMode && $password.val() != "") || !mixMode ){
			 if(!reg.test($password.val())){
				 return $password.focus() && false;
			 }
			 foundMix =true;
		 }
	 }
	 if(!$loid.prop("disabled")){
		 var reg=  /^[0-9a-zA-Z]{1,24}$/;
		 if((mixMode && $loid.val() != "") || !mixMode ){
			 if(!reg.test($loid.val())){
				return $loid.focus() && false;
			 }
			 foundMix =true;
		 }
	 }
	 if(!$loidPassword.prop("disabled")){
		 var reg=  /^[0-9a-zA-Z]{1,12}$/;
		 if((mixMode && $loidPassword.val() != "") || !mixMode ){
			 if(!reg.test($loidPassword.val())){
				return $loidPassword.focus() && false;
			 }
			 foundMix =true;
		 }
	 }
	 if(!foundMix){
		 return $sn.focus() && false;
	 }
	 if(!V.isInRange(lineProfileId,[1,1024])){
		 return $lineProfileId.focus() && false;
	 }
	 if(!V.isInRange(srvProfileId,[1,1024])){
		 return $srvProfileId.focus() && false;
	 }
	 return true;
}


function sumbitGponOnuAdd(entityId,$ponIndex,ponId,authMode,callback){
	if(!validateModule(authMode)){
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@OnuAuth.addAuthingTip@");
    $.ajax({
        url: '/gpon/onuauth/addGponOnuAuth.tv',type: 'POST',
        data: {
        	'onuNo': onuNoSelComboBox.getValue(),
        	'gponOnuAuthConfig.entityId':entityId,
        	'gponOnuAuthConfig.ponIndex':$ponIndex,
        	'gponOnuAuthConfig.ponId':ponId,
        	"gponOnuAuthConfig.sn":$("#sn").val(),
        	"gponOnuAuthConfig.password":$("#password").val(),
        	"gponOnuAuthConfig.loid":$("#loid").val(),
        	"gponOnuAuthConfig.loidPassword":$("#loidPassword").val(),
        	"gponOnuAuthConfig.lineProfileId":lineComboBox.getValue(),
        	"gponOnuAuthConfig.srvProfileId":srvComboBox.getValue(),
        	'gponOnuAuthConfig.authMode':authMode
        },
        success: function() {
        	top.closeWaitingDlg();
        	top.nm3kRightClickTips({
				title: "@COMMON.tip@", html: "@OnuAuth.addAuthOk@"
			});
        	if(callback){
        		callback();
        	}
        }, error: function(json) {
            top.showMessageDlg("@COMMON.error@", "@OnuAuth.addAuthEr@");
            top.closeWaitingDlg();
        }, cache: false
    });
}