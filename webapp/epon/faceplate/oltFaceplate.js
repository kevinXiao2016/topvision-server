var olt = new Olt();

var navigatorTree;

Ext.onReady(function(){
    Ext.getBody().mask("@COMMON.loading@...");
    
    // 加载OLT数据并构建页面
    loadOltStructure();
});

/**
 * 加载OLT数据
 */
function loadOltStructure() {
	$.ajax({
        url:"/epon/oltObjectCreate.tv",
        data: { 
        	entityId: entityId 
        },
        cache: false,
        datatype: 'json',
        success: function (data) {
        	$(".loadingText").html("@EPON.dataBack@..")
        	try{
        		olt.init(data);
        		//从数据库获取OLT所有光口的光模块信息
				olt.initOltPortOptical();
				// 生成PON口树结构
				constructPortTree();
				
				Ext.getBody().unmask();
        	}catch(e) {
        		console.log(e)
        	}
        },
        error:function () {
        	$(".loadingText").html(I18N.EPON.dataError)
        }
    })
}

/**
 * 生成端口树结构
 */
function constructPortTree() {
	try {
		navigatorTree = new Ext.tree.TreePanel({
			autoScroll: true,
			border: false,
	        listeners: {
	            //"click": clickHandler,
	            //"contextmenu": clickHandler
	        }
		});
		
		var root = new Ext.tree.TreeNode({
			text: typeName,
			id: "OLT",
			expanded: true,
			icon: '/epon/image/pn8602-ef/8602EF-16.png'
		});
		
		// 遍历板卡，加入端口
		addSlotPortToTree(olt.slotList);
		
		navigatorTree.setRootNode( root ? root : {text: '', leaf: true});
		navigatorTree.render("viewLeftPartBody");
	} catch(e) {
		
	}
}

/**
 * 遍历板卡，将端口加入导航树
 * @param slotList
 */
function addSlotPortToTree(slotList) {
	if(slotList == null || slotList.length === 0) {
		return;
	}
	
	var slotName;
	for(var i=0, len=slotList.length; i<len; i++) {
		var curSlot = slotList[i];
		var preType = slotType[curSlot.topSysBdPreConfigType].toLowerCase();
		var type = slotType[curSlot.topSysBdActualType].toLowerCase();
		if(preType == SLOT_BOARD) {
			// 预配置类型为0，则按实际板卡类型画板
			if(curSlot.topSysBdActualType == 0) {
				slotName = "@EPON.slot@_" + slotTemp.slotRealIndex
			}
		}
		//对于8602来说，MIB返回的预配置类型和实际类型中MPU_XGU* 和 XGU*不一样的，但是实际上是可以2者是无区别的,所以要特殊处理
	}
}

/**
 * 封装获取OLT的entityId的方法
 * @returns
 */
function getEntityId(){
    return olt.entityId;
}