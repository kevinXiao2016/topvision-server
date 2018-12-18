function manuRender(value, meta, record){
	var groupId = record.data.groupId;
	if(record.data.profileId == profileId){
		return String.format("<a href='javascript:;' onClick='deleteProfileGroup({0})'>@COMMON.delete@</a>",groupId); 
	}else{
		return String.format("<a href='javascript:;' onClick='addProfileGroup({0})'>@COMMON.bind@</a>",groupId);
	}
}
function groupNameRender(value){
	return value ? value :"--";
}

function statusRender(value){
	if(value == profileId){
		return "@IGMP.alreadyAdd@";
	}else if(value == @{IgmpConstants.BIND_FAILD}@){
		return "<div class=redTxt>@IGMP.bindFaild@</div>";
	}else{
		return "<div class=grayTxt>@IGMP.readyToAdd@</div>";
	}
}

function deleteProfileGroup(groupId){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.deleteProfileGro@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/igmpconfig/deleteProfileGroupRela.tv',
			type : 'POST',dataType :　'json',
			data : {
				entityId : entityId,
				profileId : profileId,
				groupId : groupId
			},
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@tip.delProfileGroupS@</b>'
       			});
				store.reload();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@tip.delProfileGroupF@");
			},
			cache : false
		});
	});
}

/**
 * 批量绑定组播组
 */
function batchBindProfileGroup(){
	var groupList = [];
	store.each(function(record){
		/* 只提交未绑定的组播组  */
		if(record.data.profileId != profileId){
			groupList.add(record.data.groupId);
		}
	});
	if(groupList.length == 0){
		return top.showMessageDlg("@COMMON.tip@", "@IGMP.noGroupNeedBind@");
	}
	top.executeLongRequeset({
		url:"/epon/igmpconfig/batchBindProfileGroup.tv",
		data:{
			entityId:entityId,profileId:profileId,
			groupIdList:groupList
		},
		message:"@IGMP.batchBindingGroup@",
		proccessHandler:function(json){
			var data = json.data;
			var recordIndex = store.find("groupId",data.groupId);
			var record = store.getAt(recordIndex);
			record.set("profileId",data.result);
			record.commit();
		},
		successMessage:"@IGMP.batchBindGroupComplete@"
	})
}
function addProfileGroup(groupId){
	//最多添加64条;
	/*if(store.data.items.length >= 64){
		top.showMessageDlg("@COMMON.tip@", "@tip.lessThan64@");
		return;
	}*/
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/addProfileGroupRela.tv',
		type : 'POST',dataType :　'json',
		data : {
			entityId  : entityId,
			profileId : profileId,
			groupId   : groupId
		},
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.addProfileGroupS@</b>'
   			});
			store.reload();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.addProfileGroupF2@");
		},
		cache : false
	});
}
function refreshProfileGroup(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshProfileGroupRela.tv',
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


function execGroupFilter(){
	var values = $("#groupFilter").val();
	if(values.length==0){
		R.batchBindBt.setDisabled(true);
		return store.clearFilter(false);
	}
	var reg = /^([0-9]{1,4}[,-]{0,1})+$/;
	if (reg.exec(values)) {
		var tmp = values.replace(new RegExp('-', 'g'), ',');
		var tmpA = tmp.split(',');
		var tmpAl = tmpA.length;
		for ( var i = 0; i < tmpAl; i++) {
			if (parseFloat(tmpA[i]) > 2000 || tmpA[i] == 0) {
				return $("#groupFilter").css("border", "1px solid #f00") && false;
			}
		}
		$("#groupFilter").css("border", "1px solid #8bb8f3");
		_execFilter(values);
		if(store.getCount()>0){
			R.batchBindBt.setDisabled(false);
		}else{
			R.batchBindBt.setDisabled(true);
		}
		return true;
	}
	return $("#groupFilter").css("border", "1px solid #f00") && false;
}

function _execFilter(conditions){
	var $groupIdList = [];
	var list = conditions.split(",");
	$.each(list,function(index,$value){
		if($value.contains("-")){
			var $array =  $value.split("-");
			var $head = parseInt($array[0]);
			var $rear = parseInt($array[1]);
			if($head > $rear)return;
			$groupIdList.add([$head,$rear]);
		}else{
			$groupIdList.add(parseInt($value));
		}
	});
	store.clearFilter(true);
	store.filterBy(function(record){
		var $groupId = record.data.groupId;
		for(var i=0; i< $groupIdList.length; i++){
			var $value = $groupIdList[i];
			if(typeof $value == 'number'){
				if($value == $groupId){
					return true;
				}
			}else{
				if($groupId>= $value[0] && $groupId <= $value[1]){
					return true;
				}
			}
		}
		return false;
	});
}

function cancelClick(){
	top.closeWindow('relateGroup');
}

$(function(){
	$("#putProfileId").text(profileId);
	cm = new Ext.grid.ColumnModel({
		columns: [
		    {header: '@igmp.vlanGroupId@', dataIndex: 'groupId', width:40},
		    {header: '@igmp.vlanGroupDesc@', dataIndex: 'groupDesc', width:80},
		    {header: '@igmp.vlanGroupAlias@', dataIndex: 'groupName',renderer:groupNameRender},
		    {header: '@COMMON.status@', dataIndex: 'profileId',renderer: statusRender,sortable:true, width:50},
		    {header: '@COMMON.manu@', width: 120, fixed:true , renderer :　manuRender}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/igmpconfig/loadProfileGroupList.tv',
		fields : ["entityId","profileId","groupName","groupId","groupDesc"],
		baseParams : {
			entityId : entityId,
			profileId : profileId
		}
	});
	grid = new Ext.grid.GridPanel({
		cm           : cm,
		store        : store,
		height       : 350,
		region       : 'center',
		renderTo     : 'putGrid',
		stripeRows   : true,
		viewConfig   : { forceFit:true},
		bodyCssClass : 'normalTable'
	});
	store.load();
});//end document.ready;
