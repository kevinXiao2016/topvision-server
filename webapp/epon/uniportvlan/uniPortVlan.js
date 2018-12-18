//修改pvid
function modifyUniPvid(newPvid){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/uniportvlan/modifyVlanMode.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			uniIndex : uniIndex,
			uniId : uniId,
			uniPvid : newPvid
		},
		dataType :　'json',
		success : function(result) {
			if(result.success){
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@PROFILE.updateSuccess@</b>'
	       	    });
				try{
					top.getActiveFrame().reload();
				}catch(e){
					
				}
			}else{
				window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.updateFailed@");
			}
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.updateFailed@");
		},
		cache : false
	});
}

//输入校验: 输入的整数否在范围内
function checkInput(value,compareStart,compareEnd){
	var reg = /^[1-9]\d*$/;	
	if (reg.exec(value) && parseInt(value) <= compareEnd && parseInt(value) >= compareStart) {
		return true;
	} else {
		return false;
	}
}

//修改VLAN模式
function modifyVlanMode(){
	var vlanMode = $("#modeUpdate").val();
	var newPvid = $("#pvid2").val();
	//先执行验证
	if(!checkInput(newPvid, 1, 4094)){
		$("#pvid2").focus();
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/uniportvlan/modifyVlanMode.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			uniIndex : uniIndex,
			uniId : uniId,
			uniPvid : newPvid,
			vlanMode :　vlanMode
		},
		dataType :　'json',
		success : function(result) {
			if(result.success){
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@PROFILE.updateSuccess@</b>'
	       	    });
				closeOpenLayer();
				//控制页面跳转
				window.location.reload();
			}else{
				window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.updateFailed@");
			}
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.updateFailed@");
		},
		cache : false
	});
}
//关联模板;
function showVlanProfile(){
	window.top.createDialog('relateprofile', "@UNIVLAN.viewProfile@", 800, 500, "/epon/univlanprofile/showRelateProfile.tv?entityId="+entityId+"&uniIndex="+uniIndex+"&profileIndex="+profileId+"&bindProfAttr="+bindProfAttr+"&uniId="+uniId, null, true, true);
}

//从设备获取UNI口VLAN信息
function refreshUniVlanData(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/uniportvlan/refreshUniVlanData.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			uniIndex : uniIndex,
			uniId : uniId
		},
		dataType :　'json',
		success : function(result) {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			window.location.reload();
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

function cancelClick(){
	window.top.closeWindow("uniPortVlan");
}

function doRefresh(){
	window.location.reload();
}