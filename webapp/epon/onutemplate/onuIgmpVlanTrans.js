var cm,store,grid;

function manuRender(value, meta, record){
	var entityId = record.data.entityId;
	var profileId = record.data.igmpProfileId;
	var portId = record.data.igmpPortId;
	var vlanTransId = record.data.igmpVlanTransId;
	return String.format("<a href='javascript:;' onClick='deleteVlanTrans({0},{1},{2},{3})'>@COMMON.delete@</a>",entityId,profileId,portId,vlanTransId); 
}
//删除UNI VLAN转化配置
function deleteVlanTrans(enitityId,profileId,portId,vlanTransId){
	top.showConfirmDlg("@COMMON.tip@", "@igmpconfig/confirm.deleteVlanTrans@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/businessTemplate/deleteOnuIgmpVlanTrans.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				profileId : profileId,
				portId : portId,
				vlanTransId : vlanTransId
			},
			dataType :　'json',
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@igmpconfig/tip.deleteVlanTransS@</b>'
       			});
				store.reload();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@igmpconfig/tip.deleteVlanTransF@!");
			},
			cache : false
		});
	});
}

//添加UNI VLAN转化配置
function addVlanTrans(){
	var vlanTransId   = $("#transIndex").val(),
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
			var index = store.data.items[i].json.igmpVlanTransId;
			if(vlanTransId == index){
				var str = String.format('@igmpconfig/tip.vlanTransHasAdd@<b class="orangeTxt pL5 pR5">{0}</b>@igmpconfig/tip.vlanTransHasAdd2@', vlanTransId);
				top.showMessageDlg("@COMMON.tip@", str);
				return;
			}
		}
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/businessTemplate/addOnuIgmpVlanTrans.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			profileId : profileId,
			portId : portId,
			vlanTransId : vlanTransId,
			transOldVlan : $("#transOldVlan").val(),
			transNewVlan : $("#transNewVlan").val()
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@igmpconfig/tip.addVlanTransS@</b>'
   			});
			$("#transIndex").val("");
			$("#transOldVlan").val("");
			$("#transNewVlan").val("");
			store.reload();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@igmpconfig/tip.addVlanTransF@!");
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

function refreshVlanTrans(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/businessTemplate/refreshOnuIgmpVlanTrans.tv',
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
	cm = new Ext.grid.ColumnModel({
		columns: [
		    {header: '@igmpconfig/igmp.transIndex@', dataIndex: 'igmpVlanTransId'},
		    {header: 'Old VLAN', dataIndex: 'transOldVlan'},
		    {header: 'New VLAN', dataIndex: 'transNewVlan'},
		    {header: '@COMMON.manu@', dataIndex: 'op', width: 100, fixed:true, renderer : manuRender}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/businessTemplate/loadOnuIgmpVlanTrans.tv',
		fields : ["entityId","igmpProfileId","igmpPortId", "igmpVlanTransId","transOldVlan","transNewVlan"],
		baseParams : {
			entityId : entityId,
			profileId : profileId,
			portId : portId
		}
	});
	grid = new Ext.grid.GridPanel({
		cm      : cm,
		store   : store,
		height  : 296,
		//title   : VLAN转换列表, 
		cls     : 'normalTable',
		stripeRows   : true,
		renderTo     : 'putGridPanel',
		viewConfig   : { forceFit:true}
	});
	store.load();
});//end document.ready;

function cancelClick() {
    window.parent.closeWindow('onuIgmpVlanTrans');
}