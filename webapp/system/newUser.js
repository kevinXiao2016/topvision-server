/**
 * author: niejun
 * 
 * Create User.
 */
Ext.BLANK_IMAGE_URL = '../images/s.gif';
var tree = null;
var userName_msg = "@SYSTEM.createUser.note4@" +'<br>'+ "@SYSTEM.createUser.note5@";
var tempText = "";
var tempId = "";
function isUserName(str){
    var reg = /^[\w-]{3,15}$/;
    return reg.test(str);
}
// 电话号码验证
function isNotPhoneNumber(phoneNumber){
    var reg = /[^0-9^\-]/;
    return reg.test(phoneNumber);
}
// email 验证
function isEmail(str){
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    return reg.test(str);
}

function mobileCheck(mobileNumber){
	var reg=/^1\d{10}$/;
	return reg.test(mobileNumber);

}
var oldIndex = 0;
var curIndex = 0;
/*function navi(inc) {
    oldIndex = curIndex;
    curIndex = curIndex + inc;
    if (curIndex == 0) {
        $('#prevBt').hide();
        $('#nextBt').show();
        $("#finishButton").hide();
    } else if (curIndex == 1) {
    	$('#nextBt').hide();
    	$('#prevBt').show().attr("disabled",false);
    	$("#finishButton").show();
    }
    Zeta$("step" + oldIndex).swapNode(Zeta$("step" + curIndex));
}*/

function validate(userName, passwd, roles){
	var reg;
	//验证用户名,必须为3-15位的由数字、中英文、下划线、中化线组成的字符串
	reg = /^[\w-\u4E00-\u9FA5]{3,15}$/;
	reg = /^[-_a-zA-Z0-9\u4e00-\u9fa5]{3,15}$/;
	if(!reg.test(userName)){
		Zeta$('userName').focus();
		return false;
	}
	//验证密码
	reg = /^[\w-]{6,16}$/;
	if(!reg.test(passwd) || !passwd){
		Zeta$('userEx.passwd').focus();
		return false;
	}
	//验证是否选择角色
	if(roles.length==0){
		window.parent.showMessageDlg("@COMMON.tip@", "@SYSTEM.selectUserRole@");
        return;
	}
	return true;
}

/**
 * 用户数量是否超过了许可证的限制
 */
function userOutofLimit(){
    var ret=false;
    $.ajax({
        url:"/system/userOutofLimit.tv",
        cache:false,
        async:false,
        type:"post",
        dataType:"json",
        success:function(resp){
            if(resp.success){
                ret=resp.outofLimit;
            }else{
                window.top.showMessageDlg("@COMMON.tip@", resp.msg);
            }
        }
    });
    return ret;
}
function okClick(checkPasswdComplex) {
    if(userOutofLimit()){
        window.top.showMessageDlg("@COMMON.tip@", "@user.outofLimit@");
        return ;
    }
    var el = Zeta$('userName');
    var workPhone = Zeta$('workPhone');
    var workPhone = workPhone.value.trim();
    if (workPhone != '' && isNotPhoneNumber(workPhone)) {
    	window.top.showMessageDlg("@COMMON.tip@", "@WorkBench.workTelephoneWrong@");
        return;
    }
    
    var homePhone = Zeta$('homePhone');
    var homePhone = homePhone.value.trim();
    if (homePhone != '' && isNotPhoneNumber(homePhone)) {
    	window.top.showMessageDlg("@COMMON.tip@", "@WorkBench.homeTelephoneWrong@");
        return;
    }
    
    var mobile = Zeta$('mobile');
    var mobile = mobile.value.trim();
    if (mobile != '' && !mobileCheck(mobile)) {
    	window.top.showMessageDlg("@COMMON.tip@", "@WorkBench.cellphoneWrong@");
        return;
    }
    
    var email = Zeta$('email');
    var email = email.value.trim();
    if (email != '' && !isEmail(email)) {
    	window.top.showMessageDlg("@COMMON.tip@", "@WorkBench.emailWrong@");
        return;
    }

    /*var regionId = Zeta$('regionId');
    var regionId = regionId.value.trim();
    if (regionId == -1) {
    	window.top.showMessageDlg("@COMMON.tip@", "@WorkBench.pleaseChooseFolder@");
        return;
    }*/
    
    //获得可选根地域集合
    var rootRegionIds = $root_regions.getSelectedIds();
    if(!rootRegionIds.length){
    	window.top.showMessageDlg("@COMMON.tip@", "@SYSTEM.plsselectatleast1region@");
        return;
    }
    $('#rootRegionIds').val(rootRegionIds);
    //获取当前根地域
    var curRegionId = $cur_root_regions.getSelectedIds();
    if(!curRegionId.length){
    	window.top.showMessageDlg("@COMMON.tip@", "@SYSTEM.plsselectcurrentregion@");
        return;
    }else{
    	curRegionId = curRegionId[0];
    }
    $('#curRegionId').val(curRegionId);
    
    var roles = tree.getChecked('id');
    Zeta$("roleIds").value = roles;
    
    Ext.Ajax.request({url: 'createUser.tv', method:'POST', form : 'userForm',
        success: function(response) {
            var json = Ext.decode(response.responseText);
            //现在在blur事件和点击下一步时进行了重名检查,在这里应该不会出现重复的情况了
            if (json.exists) {
            	/*  
                if (curIndex == 1) {
                    navi(-1);
                }*/
                userName_msg = "@SYSTEM.userNameExist@";
                el.focus();
            } else {
                try {
                    var frame = window.top.getFrame('userList');
                    if (frame != null) {
                        frame.onRefreshClick();
                    }
                } catch (err) {
                }
                cancelClick();
            }
        },
        failure: function(response) {
            window.top.showErrorDlg();
        }
    });
}
function showUserView() {
    window.parent.addView("userList",  "@SYSTEM.userManagement@", "userIcon", "system/userList.jsp");
}
function cancelClick() {
    window.parent.closeWindow('modalDlg');
}
function selectPostClick() {
    window.parent.createDialog('postDlg', "@SYSTEM.selectPost@", 600, 370, "system/popPost.jsp", null, true, false,
            function() {
                var clipboard = window.parent.ZetaCallback;
                if (clipboard.type == 'ok') {
                    Zeta$('placeId').value = clipboard.selectedItemId;
                    Zeta$('placeName').value = clipboard.selectedItemText;
                }
                window.parent.ZetaCallback = null;
            });
}
function selectRegionClick() {
    window.parent.createDialog('regionDlg', "@SYSTEM.selectRegion@", 600, 370, "system/popRegion.jsp", null, true, false,
            function() {
                var clipboard = window.parent.ZetaCallback;
                if (clipboard.type == 'ok') {
                    Zeta$('regionId').value = clipboard.selectedItemId;
                    Zeta$('regionName').value = clipboard.selectedItemText;
                }
                window.parent.ZetaCallback = null;
            });
}
function selectDepartmentClick() {
    window.parent.createDialog('departmentDlg', "@SYSTEM.selectDepartment@", 600, 370, "system/popDepartment.jsp", null, true, false,
            function() {
                var clipboard = window.parent.ZetaCallback;
                if (clipboard.type == 'ok') {
                    Zeta$('departmentId').value = clipboard.selectedItemId;
                    Zeta$('departmentName').value = clipboard.selectedItemText;
                }
                window.parent.ZetaCallback = null;
            });
}
//v1遗留代码,好像没有作用了
function backText(s){
    var name;
    var id;
    if(s == 1){
		name = $("#regionName");
		id = $("#regionId");
	}else if(s == 2){
		name = $("#departmentName");
		id = $("#departmentId");
	}else if(s == 3){
		name = $("#placeName");
		id = $("#placeId");
	}
	tempText = name.val();
	tempId = id.val();
}
//v1遗留代码,好像没有作用了
function keyup(s){
    var name;
    var id;
    if(s == 1){
		name = $("#regionName");
		id = $("#regionId");
	}else if(s == 2){
		name = $("#departmentName");
		id = $("#departmentId");
	}else if(s == 3){
		name = $("#placeName");
		id = $("#placeId");
	}
    name.val(tempText);
	id.val(tempId);
}

function doOnload() {
    Zeta$('userName').focus();
}
//检查用户名是否已经存在
function isUserNameExist(){
	var flag = false;
	$.ajax({
		url : 'isUserNameExist.tv',
		type : 'post',
		data : {
			"userName" : $("#userName").val()
		},
		dataType : 'json',
		success : function(json) {
			flag = json.exists
		},
		error : function(json) {
		},
		async : false,
		cache : false
	});
	return flag;
}

var repeatFlag = false;   //用户名是否重复标识
Ext.onReady(function() {
    var treeLoader = new Ext.tree.TreeLoader({dataUrl:'loadUserRole.tv'});
    tree = new Ext.tree.TreePanel({
        el:'roleTree', useArrows:useArrows, autoScroll:true, animate:true, border: false,height: 180,
        trackMouseOver:false,padding:10,
        lines: true, rootVisible: false, containerScroll: true, enableDD: false,
        loader: treeLoader
    });
    var root = new Ext.tree.AsyncTreeNode({text: 'Role Tree', draggable:false, id:'source'});
    tree.setRootNode(root);
    tree.render();
    root.expand();
    setTimeout("doOnload()", 500);
    
  //为名称输入框绑定事件
	$("input#userName").bind('blur',function(){
		repeatFlag = isUserNameExist();
		if(repeatFlag){
			$("#nameExist").show();
			$("input#userName").addClass('normalInputRed');
		}else{
			$("#nameExist").hide();
			$("input#userName").removeClass('normalInputRed');
		}
	});
});

$(function(){
	window.$cur_root_regions = $('#cur_root_regions').dropdowntree({
		width:290,
		multi:false
	}).data('nm3k.dropdowntree');
	
	window.$root_regions = $('#root_regions').dropdowntree({
		width:290,
		yesAffectChild: true,
		noAffectChild: false,
        yesAffectParent: false,
        noAffectParent: true,
		onCheck: updateCheckable,
		switchMode: true
	}).data('nm3k.dropdowntree');
	
	function updateCheckable(){
		//获取可选根地域的未被勾选的节点集合
		var nonSelectIds = $root_regions.getNotSelectedIds();
		//将当前根地域这些节点设置为不可勾选 
		$cur_root_regions.disableNodes(nonSelectIds);
	}
})
