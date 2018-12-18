function showEntitySnap(entityId, name) {
	window.parent.addView('entity-' + entityId, name, 'entityTabIcon',
		'portal/showEntitySnapJsp.tv?entityId=' + entityId);
}
function refreshOltAuth(entityId){
	window.parent.showWaitingDlg("@COMMON.wait@", '@ISOGROUP.onRefreshing@','waitingMsg','ext-mb-waiting');
    $.ajax({
        url:'/epon/onuauth/refreshOnuAuthInfo.tv',
        type:'POST',
        data:{entityId:entityId},
        dateType:'json',
        success:function(response) {
        	window.parent.closeWaitingDlg();
            if (response == "success") {
            	top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: '<b class="orangeTxt">' + "@ISOGROUP.refreshSuc@" + '</b>'
       			});
    			refreshOltGrid()
            } else {
            	top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: '<b class="orangeTxt">' + '@ISOGROUP.refreshFail@' + '</b>'
       			});
            }
        },
        error:function() {
        	window.parent.closeWaitingDlg();
        },
        cache:false
    });
}
function modifyMode(entityId){
	window.top.createDialog('modifyAuthMode', "@onuAuth.modifyAuthMode@", 600, 400, '/epon/onuauth/showModifyAuthMode.tv?entityId=' + entityId, null, true, true);
}
function refreshOltGrid(){
	var ip = Zeta$('ip').value;
	var name = Zeta$('name').value;
	oltStore.load({params: {ip: ip, name: name}});
}
function buildIp(){
	return '<div><td width=40px align="right">@resources/COMMON.manageIp@:</td>&nbsp;' +
	   '<td><input class="normalInput mR10" type=text style="width: 100px"  id="ip"></td></div>'
}
function buildName(){
	return '<div><td width=40px align="right">@COMMON.alias@:</td>&nbsp;' +
	   '<td><input class="normalInput mR10" type=text style="width: 100px"  id="name"></td></div>'
}
function queryOltAuth(){
	  lastRow = null;
	  var ip = Zeta$('ip').value;
	  var name = Zeta$('name').value;
	  var pattern = /^[a-z0-9:]{1,17}$/i;
	  if(name != "" && !V.isAnotherName(name, null)) {
		  top.afterSaveOrDelete({
 				title: I18N.COMMON.tip,
 				html: '<b class="orangeTxt">' + I18N.NETWORK.queryNmNameTip + '</b>'
 			});
			return;
		}
	  if(!V.isFuzzyIpAddress(ip) && ip != '' && ip != null){
		  top.afterSaveOrDelete({
				title: I18N.COMMON.tip,
				html: '<b class="orangeTxt">' + '@SNMPv3.t.errIpAddress@' + '</b>'
			});
		  return;
		  }
	oltStore.load({params: {ip: ip, name: name}});
}

function modifyPonAuthMode(entityId, ponIndex,authMode,ponPortType,gponAuthMode){
	if(ponPortType == @{GponConstant.PORT_TYPE_GPON}@){
		window.top.createDialog('modifyPonAuthMode', "@onuAuth.modifyAuthMode@", 600, 400, '/gpon/onuauth/showModifyPonAuthMode.tv?entityId=' + entityId
				+ '&ponIndex=' + ponIndex + '&authMode=' + gponAuthMode, null, true, true,function(){
			refreshOltGrid();
		});
	}else{
		window.top.createDialog('modifyPonAuthMode', "@onuAuth.modifyAuthMode@", 600, 400, '/epon/onuauth/showModifyPonAuthMode.tv?entityId=' + entityId
				+ '&ponIndex=' + ponIndex + '&authType=' + authMode, null, true, true);
	}
}
Ext.onReady(function() {
	createOltAuthGrid();
});

//点击链接
function showOltMacAuth(entityId){
	seeMore("mac", entityId)
}
function showOltSnAuth(entityId){
	seeMore("sn", entityId)
}
function showOltAuthFail(entityId){
	seeMore("fail", entityId)
}
function showPonMacAuth(entityId, ponIndex){
	seeMore("mac", entityId, ponIndex)
}

function showPonSnMacAuth(entityId, ponIndex){
	seeMore("sn", entityId, ponIndex)
}

function showPonAuthFail(entityId, ponIndex){
	seeMore("fail", entityId, ponIndex)
}
function loadOltTreeListHtml(entityId){
	var oltHtml = "";
	$.ajax({
        url : "/epon/onuauth/getOltList.tv",
        type: 'POST',
        async:false,
        cache : false,
        dataType:'json',
        success : function(oltListJson) {
        	var oltList = oltListJson.data
    		var size = oltListJson.data.length;
			for(var i = 0; i < size; i++){
				if(oltList[i].entityId == entityId){
					oltHtml = oltHtml + '<li><a id="' + oltList[i].entityId + '"href="javascript:;" class="selected">' 
					+ oltList[i].name + '(' + oltList[i].ip + ')' + '</a></li>'
				}else{
					oltHtml = oltHtml + '<li><a id="' + oltList[i].entityId + '"href="javascript:;">' 
					+ oltList[i].name + '(' + oltList[i].ip + ')' + '</a></li>'
				}
			}
        }
    });  
	return oltHtml;
}

function seeMore(type, entityId, ponIndex){
	displayModule.module = displayModule.EPON_ONU_AUTH;
	globalEntityId = entityId;
	$("#openLayer").stop().fadeIn();
	if(type == "fail"){
		$("#putTab a").eq(1).click();
	}else if(type == "mac"){
		$("#macAuth").attr("checked",true);
		$("#snAuth").attr("checked",false);
		$("#putTab a").eq(0).click();
	}else if(type == "sn"){
		$("#snAuth").attr("checked",true);
		$("#macAuth").attr("checked",false);
		$("#putTab a").eq(0).click();
	}
	var oltHtml = loadOltTreeListHtml(entityId);
	buildPonSelect(entityId, ponIndex);
	$("#oltTree").html(oltHtml);
	var authType = getAuthType();
	if(type == "fail"){
		onuAuthFailStore.baseParams={
				start: 0,
				limit: pageSize,
				entityId: globalEntityId,
				ponIndex: ponIndex
			}
		onuAuthFailStore.load();
	}
	if(type == "mac"){
		onuAuthStore.baseParams={
				start: 0,
				limit: pageSize,
				entityId: globalEntityId,
				ponIndex: ponIndex,
				authType: authType
			}
		onuAuthStore.load();
	} else if(type == "sn"){
		onuAuthStore.baseParams={
				start: 0,
				limit: pageSize,
				entityId: globalEntityId,
				ponIndex: ponIndex,
				authType: authType
			}
		onuAuthStore.load();
	}
}

//返回  按钮;
function goBack(){
	$(".openLayer").fadeOut();
}
//设置弹出层的高度;
function setOpenLayerHeight(){
	var h2 = $("#openLayer").height() - 47;
	if(h2 > 0){
		$(".openLayerSide, .openLayerLine, .openLayerMain").height(h2);
	}
	
	var $openLayerMain = $(".openLayerMain"),
		w = ($openLayerMain.width() > 100) ? $openLayerMain.width() : 100,
		h = ($openLayerMain.height() > 60) ? $openLayerMain.height() : 60;
	
	if(onuAuthFailGrid !== null){
			onuAuthFailGrid.setSize(w, h);
			//onuAuthGrid.setSize(w, h);
	}
}
//resize事件增加函数节流
function throttle(method, context){
	clearTimeout(method.tId);
	method.tId = setTimeout(function(){
		method.call(context);
	},100);
}

$(function(){
	$("#oltTree,#oltTreeGponAuth,#oltTreeAutoFind").delegate("a","click",function(){
		var $me = $(this);
		$("#oltTree,#oltTreeGponAuth > li > a").removeClass("selected");
		$("#oltTreeAutoFind > li > a").removeClass("selected");
		$("#oltTree a").removeClass("selected");
		
		$me.addClass("selected");
		globalEntityId = $me.attr("id")
		if(displayModule.module == displayModule.GPON_ONU_AUTH){
			showOltGponAuth(globalEntityId);
		}else if(displayModule.module == displayModule.GPON_AUTO_FIND){
			showOltGponAutoFind(globalEntityId);
		}else if(nowTab > 0){
			onuAuthFailStore.baseParams={
				start: 0,
				limit: pageSize,
				entityId: globalEntityId
			}
			onuAuthFailStore.reload({params:{entityId: globalEntityId}});
		}else{
			buildPonSelect(globalEntityId);
			var authType = getAuthType();
			onuAuthStore.baseParams={
				start: 0,
				limit: pageSize,
				entityId: globalEntityId
			}
			onuAuthStore.reload({params:{authType: authType, entityId: globalEntityId}});
		}
	})
	setOpenLayerHeight();
	$(window).wresize(function(){
		throttle(setOpenLayerHeight,window)
	});//end resize;
	//左侧可以拖拽宽度;
	var o1 = new DragMiddle({ id: "openLayerLine", leftId: "openLayerSide", rightId: "openLayerMain", minWidth: 100, maxWidth:400,leftBar:true });
	o1.init();
	
	//显示第一个;
	$(".whiteTabContent").css("display","none");
	$(".whiteTabContent:first").css("display","block");
	
	var tab1 = new Nm3kTabBtn({
	    renderTo:"putTab",
	    callBack:"showTab",
	    tabs:["@EPON.onuAuth@","@onuAuth.authFail@"]
	});
	tab1.init();
	
	if(!operationDevicePower){
		$("#addOnuAuthGlobalBt").hide();
	}
	showOnuType();
});//end ducument.ready;


//功能函数
function deleteOnuAuth(){
    var record = onuAuthGrid.getSelectionModel().getSelected();
    var entityId = record.data.entityId;
    var ponId = record.data.ponId;
    var onuIndex = record.data.onuIndex;
    var authType = record.data.authType;
    window.parent.showConfirmDlg(I18N.COMMON.tip, "@onuAuth.confirmDeleteAuth@", function(button, text) {
        if (button == "yes") {
        	window.top.showWaitingDlg("@COMMON.wait@", "@OnuAuth/OnuAuth.deletingEponOnuAuth@");
            $.ajax({
                url:'/epon/onuauth/deleteOnuAuth.tv',
                type:'POST',
                data:{entityId:entityId, ponId:ponId, onuIndex:onuIndex, authType:authType},
                dateType:'json',
                success:function(response) {
                    if (response == "success") {
                    	top.afterSaveOrDelete({
               				title: I18N.COMMON.tip,
               				html: '<b class="orangeTxt">' + "@TRUNK.deleteOk@" + '</b>'
               			});
                    	var ponIndex = $("#ponIndex").val();
            			var authType = getAuthType();
            			onuAuthStore.reload({params:{entityId: globalEntityId, ponIndex: ponIndex, authType: authType}});
                    } else {
                        window.parent.showMessageDlg(I18N.COMMON.tip, "@TRUNK.deleteError@");
                    }
                },
                error:function() {
                },
                cache:false
            });
        }
    });
}
function addOnuAuth(){
	var record = onuAuthFailGrid.getSelectionModel().getSelected();
	var ponIndex =  record.data.ponIndex;
    var onuIndex = record.data.onuIndex;
    var mac = record.data.mac;
    var sn = record.data.sn;
    var password = record.data.password;
	window.top.createDialog('addOnuAuth', "@onuAuth.addAuth@", 600, 500, '/epon/onuauth/showAddOnuAuth.tv?entityId=' + globalEntityId+ '&ponIndex=' + ponIndex + '&onuIndex=' + onuIndex + '&mac=' + mac + '&sn=' + sn + '&password=' + password, null, true, true);
}
function addOnuAuthGlobal(){
	var ponIndex = $("#ponIndex").val();
	window.top.createDialog('addOnuAuth', "@onuAuth.addAuth@", 600, 500, '/epon/onuauth/showAddOnuAuth.tv?entityId=' + globalEntityId + '&ponIndex=' + ponIndex, null, true, true);
}
function showTab(index){
	nowTab = index;
	switch(index){
	case 0:
		$("#onuAuthManage").css("display","block");
		$("#onuAuthFail").css("display","none");
		$("#macAuth").css("display","block");
		$("#snAuth").css("display","block");
		$("#macAuthTip").css("display","block");
		$("#snAuthTip").css("display","block");
		if (onuAuthGrid == null) {
			createOnuAuthGrid();
		}else{
			var ponIndex = $("#ponIndex").val();
			var authType = getAuthType();
			// modify by fanzidong，此处应设置为baseparams
			onuAuthStore.baseParams = {
				start: 0,
				limit: pageSize,
			    entityId: globalEntityId, 
			    ponIndex: ponIndex, 
			    authType: authType
			};
			onuAuthStore.load();
		}
		break;
	case 1:
		$("#onuAuthManage").css("display","none");
		$("#onuAuthFail").css("display","block");
		$("#macAuth").css("display","none");
		$("#snAuth").css("display","none");
		$("#macAuthTip").css("display","none");
		$("#snAuthTip").css("display","none");
		$("#addAuthButton").css("display","none");
		if (onuAuthFailGrid == null) {
			createOnuAuthFailGrid();
		}else{
			var ponIndex = $("#ponIndex").val();
			// modify by fanzidong，此处应设置为baseparams
			onuAuthFailStore.baseParams = {
				start: 0,
				limit: pageSize,
			    entityId: globalEntityId, 
			    ponIndex: ponIndex
			};
			onuAuthFailStore.load();
		}
		break;
}	
}

function checkBoxClick(){
	var ponIndex = $("#ponIndex").val();
	var authType = getAuthType();
	onuAuthStore.baseParams = {
		start: 0,
		limit: pageSize,
		entityId: globalEntityId, 
		ponIndex: ponIndex, 
		authType: authType
	}
	onuAuthStore.load();
}

/**
 * 获取需要查询的认证方式
 * 0 ： auth & sn
 * 1: auth
 * 2:sn
 * -1 未选择
 */
function getAuthType(){
	var macAuth = $("#macAuth").attr("checked");
	var snAuth = $("#snAuth").attr("checked");
	if(macAuth &&　snAuth){
		return 0;
	}
	if(macAuth){
		return 1;
	}
	if(snAuth){
		return 2;
	}
	return -1;
}
function showOltGponAutoFind(entityId,$ponIndex){
	displayModule.module = displayModule.GPON_AUTO_FIND;
	$("#openLayer-autofind").show();
	var oltHtml = loadOltTreeListHtml(entityId);
	$("#oltTreeAutoFind").html(oltHtml);
		
	/*if(!WIN.autoFindGponSelector){
		WIN.autoFindGponSelector = new PonSelector({
			type:@{GponConstant.PORT_TYPE_GPON}@,
			singleSelect : true,
			renderTo : "gponAutoFindIndex",
			afterClick : function(){
				var indexs = autoFindGponSelector.getSelectedIndexs();
				gponOnuAutoFindstore.reload({params:{entityId:entityId,ponIndex:indexs}});
			}
		});
		renderAutoFindGrid(entityId);
	}
	autoFindGponSelector.render({
		entityId : entityId,
		readyCallback : function(entityId){
			if($ponIndex == null){
				gponOnuAutoFindstore.reload({params:{entityId:entityId}});
			}else{
				autoFindGponSelector.setValue($ponIndex);
				gponOnuAutoFindstore.reload({params:{entityId:entityId,ponIndex:$ponIndex}});
				$ponIndex = null;//解决重复调用闭包的问题
			}
		}
	});*/
	if(!WIN.autoFindGponSelector){
		WIN.autoFindGponSelector = true;
		renderAutoFindGrid(entityId);
	}
	/*if($ponIndex == null){
		gponOnuAutoFindstore.reload({params:{entityId:entityId}});
	}else{
		gponOnuAutoFindstore.reload({params:{entityId:entityId,ponIndex:$ponIndex}});
	}*/
	gponOnuAutoFindstore.reload();
	var $gponAutoFindIndexSel = $('#gponAutoFindIndexSel');
	$gponAutoFindIndexSel.attr({name: entityId});
	$gponAutoFindIndexSel.html('<option value="-1">@ONU.select@</option>')
	var url = "/gpon/onuauth/loadGponPortList.tv?entityId=" + entityId;
	$.ajax({
       url: url,
	   data:{portType:@{GponConstant.PORT_TYPE_GPON}@ },
	   dataType: 'json',
       success: function(jsonData) {
    	   var opts = '';
    	   $.each(jsonData, function(i, v){
    		   if(!v.nocheck){
    			   var sel = '';
    			   if($ponIndex == v.id){sel = 'selected="selected"'}
    			   opts += String.format('<option name="{0}" value="{1}" {2}>{3}</option>', entityId, v.id, sel, v.name);
    		   }
    	   });
    	   $gponAutoFindIndexSel.append(opts);
       },
       error: function() {},
       cache: false
	});
	
	
}
function showOltGponAuth(entityId,$ponIndex){
	displayModule.module = displayModule.GPON_ONU_AUTH;
	globalEntityId = entityId;
	$("#openLayer-gponauth").show();
	var oltHtml = loadOltTreeListHtml(entityId);
	$("#oltTreeGponAuth").html(oltHtml);
	/*if(!WIN.GponAuthSelector){
		WIN.GponAuthSelector = new PonSelector({
			type:@{GponConstant.PORT_TYPE_GPON}@,
			singleSelect : true,
			renderTo : "gponIndex",
			afterClick : function(){
				var indexs = GponAuthSelector.getSelectedIndexs();
				gponOnuAuthstore.reload({params:{entityId:entityId,ponIndex:indexs}});
			}
		});
		renderGponAuthGrid(entityId);
	}
	GponAuthSelector.render({
		entityId : entityId,
		readyCallback : function(entityId){
			if($ponIndex == null){
				gponOnuAuthstore.reload({params:{entityId:entityId}});
			}else{
				GponAuthSelector.setValue($ponIndex);
				gponOnuAuthstore.reload({params:{entityId:entityId,ponIndex:$ponIndex}});
				//$ponIndex = null;//解决重复调用闭包的问题
			}
		}
	});*/
	
	if(!WIN.GponAuthSelector){
		WIN.GponAuthSelector = true;
		renderGponAuthGrid(entityId);
	}
	if($ponIndex == null){
		gponOnuAuthstore.reload({params:{entityId:entityId}});
	}else{
		gponOnuAuthstore.reload({params:{entityId:entityId,ponIndex:$ponIndex}});
	}
	
	var $gponIndexSel = $('#gponIndexSel');
	$gponIndexSel.attr({name: entityId});
	$gponIndexSel.html('<option value="-1">@ONU.select@</option>')
	var url = "/gpon/onuauth/loadGponPortList.tv?entityId=" + entityId;
	$.ajax({
       url: url,
	   data:{portType:@{GponConstant.PORT_TYPE_GPON}@ },
	   dataType: 'json',
       success: function(jsonData) {
    	   var opts = '';
    	   $.each(jsonData, function(i, v){
    		  //if(!v.nocheck){
    			   var sel = '';
    			   if($ponIndex == v.id){sel = 'selected="selected"'}
    			   opts += String.format('<option name="{0}" value="{1}" {2}>{3}</option>', entityId, v.id, sel, v.name);
    		   //}
    	   });
    	   $gponIndexSel.append(opts);
       },
       error: function() {},
       cache: false
	});
}
//查询ONU类型
function showOnuType(){
	var url = "/gpon/onuauth/showOnuType.tv";
	$.ajax({
		 url: url,
		 success: function(response) {
			 var onuType = response.entityTypeList;
			 var head ='<option value="-1" selected>' + '@ONU.select@'
					+ '</option>';
			var options = "";
			for (var i = 0; i < onuType.length; i++) {
				options += '<option value="' +onuType[i].displayName+ '">'
						+ onuType[i].displayName + '</option>';
			}
			$("#onuType").html(head + options)
		 }
	})
}
function changeGponIndexSel(){
	var $ponIndex = $('#gponIndexSel').val();
	var entityId =  $('#gponIndexSel').attr('name');
	var o = {
		params:{
			entityId:entityId
		}	
	}
	if($ponIndex != "-1"){
		o.params.ponIndex = $ponIndex
	}
	gponOnuAuthstore.reload(o);
}
/*function changeAutoFindSel(){
	$gponAutoFindIndexSel = $('#gponAutoFindIndexSel');
	var $ponIndex = $gponAutoFindIndexSel.val();
	var entityId = $gponAutoFindIndexSel.attr('name');
	var o = {
		params:{
			entityId:entityId
		}	
	}
	if($ponIndex != "-1"){
		o.params.ponIndex = $ponIndex
	}
	gponOnuAutoFindstore.reload(o);
}*/
function simpleQuery(){
	var $ponIndex = $('#gponAutoFindIndexSel').val();
	var entityId = $('#gponAutoFindIndexSel').attr('name');
	var onuType = $("#onuType").val();
	var sn = $("#sn").val();
	var authMode = $("#authMode").val();
	var o = {
			params:{
				entityId:entityId,
				ponIndex:$ponIndex,
				sn:sn,
				authMode:authMode,
				onuType:onuType
			}	
		}
	gponOnuAutoFindstore.reload(o);
}
function refreshAutoFindGrid(){
	sm.clearSelections();
	gponOnuAutoFindstore.reload();
}
function cancleCheckboxSelection(){
	sm.clearSelections();
}
function showAddGponOnuAuth(){
	top.createDialog('addGponOnuAuth', "@OnuAuth/OnuAuth.addAuth@", 600, 420, '/gpon/onuauth/showAddGponOnuAuth.tv?entityId=' + globalEntityId, null, true, true,function(){
		showOltGponAuth(globalEntityId);
		refreshOltGrid();
	});
}
function addAutoFindToGponAuth(entityId,onuIndex){
	top.createDialog('addGponOnuAuth', "@OnuAuth/OnuAuth.addAuth@", 600, 420, '/gpon/onuauth/showAddGponOnuAuth.tv?entityId=' + entityId+"&onuIndex="+onuIndex, null, true, true,function(){
		showOltGponAutoFind(entityId);
		refreshOltGrid();
		cancleCheckboxSelection();
	});
}
