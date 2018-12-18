$(function(){
	//onu删除，加一个弹出层，直接提示用户ONU已经被删除;
	var onu_delete = top.PubSub.on(top.OltTrapTypeId.ONU_DELETE, function(data){
		if(top.isClearAlert(data)) {
			return;	
		}
		trapOnuDelete(data);
	});
	
	//UNI端口link down 更新端口状态
	var onu_uni_linkdown = top.PubSub.on(top.OltTrapTypeId.ONU_UNI_LINKDOWN, function(data){
		if(store){
			store.reload();
		}
	});
	
	$(window).on('unload', function(){
		top.PubSub.off(top.OltTrapTypeId.ONU_DELETE, onu_delete);
		top.PubSub.off(top.OltTrapTypeId.ONU_UNI_LINKDOWN, onu_uni_linkdown);
	});
});
//提示用户该ONU已经被删除;
function trapOnuDelete(data){
	if(!data || !data.entityId || !window.onuId || window.onuId != data.entityId){
		return;
	}
	Ext.getBody().mask("<label class='tipMask'>@COMMON.onuDelete@</label>");
}