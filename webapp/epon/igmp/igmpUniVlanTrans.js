var cm,store,grid;

function manuRender(value, meta, record){
	var transIndex = record.data.transIndex;
	return String.format("<a href='javascript:;' onClick='deleteUniVlanTrans({0})'>@COMMON.delete@</a>",transIndex); 
}
//删除UNI VLAN转化配置
function deleteUniVlanTrans(transIndex){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.deleteVlanTrans@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/igmpconfig/deleteUniVlanTrans.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				uniIndex : uniIndex,
				transIndex : transIndex
			},
			dataType :　'json',
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@tip.deleteVlanTransS@</b>'
       			});
				store.reload();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@tip.deleteVlanTransF@!");
			},
			cache : false
		});
	});
}

//添加UNI VLAN转化配置
function addUniVlanTrans(){
	var $transIndex   = $("#transIndex"),
		transIndexVal = $transIndex.val(),
	    flag = customValidateFn([
	      {
	    	  id    : 'transIndex',
	    	  range : [1,8]
	      },{
	    	  id    : 'transOldVlan',
	    	  range : [1,4094]
	      },{
	    	  id    : 'transNewVlan',
	    	  range : [1,4094]
	      }           
	]);
	
	if(flag !== true){
		flag.focus();
		return;
	}
	//对比store,如果存在重复的，则不允许加入;
	if( store.data.items.length > 0 ){
		for(var i=0,len=store.data.items.length; i<len; i++){
			var index = store.data.items[i].json.transIndex;
			if(transIndexVal == index){
				var str = String.format('@tip.vlanTransHasAdd@<b class="orangeTxt pL5 pR5">{0}</b>@tip.vlanTransHasAdd2@', transIndexVal);
				top.showMessageDlg("@COMMON.tip@", str);
				return;
			}
		}
	}
	var transData = {
			"vlanTrans.entityId" : entityId,
			"vlanTrans.uniIndex" : uniIndex,
			"vlanTrans.transIndex" : $transIndex.val(),
			"vlanTrans.transOldVlan" : $("#transOldVlan").val(),
			"vlanTrans.transNewVlan" : $("#transNewVlan").val()
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/addUniVlanTrans.tv',
		type : 'POST',
		data : transData,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.addVlanTransS@</b>'
   			});
			store.reload();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.addVlanTransF@!");
		},
		cache : false
	});
}
//验证是否是数字(不能大于7位);
function validateNumber(v){
	var regExp = /^\d{1,7}$/;
	return regExp.test(v);
}
//验证是否是在某个范围;
function validateRange(v,arr){
	if( v >= arr[0] && v <= arr[1] ){
		return true;
	}
}
function customValidateFn(arr){
	for(var i=0,len=arr.length; i<len; i++){
		var o   = arr[i],
		    $id = $("#"+o.id), 
		    v   = $id.val();
		if( validateNumber(v) ){//必须是数字;
			if( !validateRange(v, o.range) ){//验证数字范围;
				return $id;
			}
			if(i === len-1){ //最后一个也验证通过了;
				return true;
			}
		}else{
			return $id;
		}
	}
};

function refreshUniVlanTrans(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshUniVlanTrans.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			store.reload();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

$(function(){
	$("#putUniName").text(window.uniName);
	cm = new Ext.grid.ColumnModel({
		columns: [
		    {header: '@igmp.portNumber@', dataIndex: 'uniName'},
		    {header: '@igmp.transIndex@', dataIndex: 'transIndex'},
		    {header: 'Old VLAN', dataIndex: 'transOldVlan'},
		    {header: 'New VLAN', dataIndex: 'transNewVlan'},
		    {header: '@COMMON.manu@', dataIndex: 'op', width: 100, fixed:true, renderer : manuRender}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/igmpconfig/loadUniVlanTransList.tv',
		fields : ["entityId","uniIndex","uniName", "portType","transIndex","transOldVlan", "transNewVlan"],
		baseParams : {
			entityId : entityId,
			uniIndex : uniIndex
		}
	});
	grid = new Ext.grid.GridPanel({
		cm      : cm,
		store   : store,
		height  : 296,
		cls     : 'normalTable',
		stripeRows   : true,
		renderTo     : 'putGridPanel',
		viewConfig   : { forceFit:true}
	});
	store.load();
});//end document.ready;

function cancelClick() {
    window.parent.closeWindow('uniVlanTrans');
}