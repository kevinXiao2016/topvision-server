/**
 * OCPage.js (c) 2012,Topvision Copyrights
 * 
 * @author Bravin
 */
/*
var TYPE = {
	//1GE 室外
    '33' : {type:'TA-PN8621',name:"@ONU.pn8621Name@",icon:'/epon/image/onu/8624Icon_48.png'},
    //1GE 野外
    '34' : {type:'TA-PN8622',name:"@ONU.pn8622Name@",icon:'/epon/image/onu/8622Icon_48.png'},
    //4FE 室外
    '36' : {type:'TA-PN8624',name:"@ONU.pn8624Name@",icon:'/epon/image/onu/8624Icon_48.png'},
    //4FE 野外
    '37' : {type:'TA-PN8625',name:"@ONU.pn8625Name@",icon:'/epon/image/onu/8622Icon_48.png'},
    //4FE 野外
    '40' : {type:'TA-PN8628',name:"10G ONU",icon:'/epon/image/onu/8624Icon.png'},
    //8FE 室外
    '65' : {type:'TA-PN8641',name:"@ONU.pn8641Name@",icon:'/epon/image/onu/864x_64.gif'},
    //16FE 室外
    '68' : {type:'TA-PN8644',name:"@ONU.pn8644Name@",icon:'/epon/image/onu/864x_64.gif'},
    //24FE 室外
    '71' : {type:'TA-PN8647',name:"@ONU.pn8647Name@",icon:'/epon/image/onu/864x_64.gif'},
    //电力ONU,图片待更换
    '81' : {type:'TA-PN8651',name:"@ONU.pn8651Name@",icon:'/epon/image/onu/8651Icon.png'},
    //电力ONU,图片待更换
    '82' : {type:'TA-PN8652',name:"@ONU.pn8652Name@",icon:'/epon/image/onu/8651Icon.png'},
    //电力ONU,图片待更换
    '83' : {type:'TA-PN8653',name:"@ONU.pn8653Name@",icon:'/epon/image/onu/8651Icon.png'},
    //电力ONU,图片待更换   
    '84' : {type:'TA-PN8654',name:"@ONU.pn8654Name@",icon:'/epon/image/onu/8651Icon.png'},   
    //未知TODO:显示出未知类型
    '255' : {type: "@ONU.unknown@", name: "@ONU.unknown@",icon:'/epon/image/onu/unknown.png'},
    //空blankOnu
    '256' : {type:'',name:'',icon:'/epon/image/onu/blankOnu.png'},
    '241' : {type:'CC8800A',name: "@ONU.cc8800aName@" ,icon:'/epon/image/onu/8622Icon.png'},
    '239' : {type:'CC8800C-A',name: "@ONU.cc8800aName@" ,icon:'/epon/image/onu/86239Icon.png'},
    '242' : {type:'CC8800B',name: "@ONU.cc8800bName@" ,icon:'/epon/image/onu/8624Icon.png'},
    '242' : {type:'CC8800B',name: "@ONU.cc8800cName@" ,icon:'/epon/image/onu/8624Icon.png'},
    '244' : {type:'CC8800E(强集中)',name: "@ONU.cc8800aName@" ,icon:'/epon/image/onu/8624Icon.png'},
    '246' : {type:'CC8800C-E(强集中)',name: "@ONU.cc8800aName@" ,icon:'/epon/image/onu/8624Icon.png'},
    '247' : {type:'CC8800D-E(强集中)',name: "@ONU.cc8800aName@" ,icon:'/epon/image/onu/8624Icon.png'},
    '248' : {type:'CC8800C-10G(强集中)',name: "@ONU.cc8800aName@" ,icon:'/epon/image/cmc/icmc_c10g.png'},
    '243' : {type:'CC8800C',name: "@ONU.unknown@CC",icon:'/epon/image/onu/8624Icon.png'},
    '250' : {type : 'mdu' , name:'MDU',icon : '/epon/image/onu/mdu_16.png'},
    'undefined':{type: "@ONU.unknown@",name: "@ONU.unknown@",icon:'/epon/image/onu/unknown.png'}
}*/

/**
 * 单例对象,页面全局控制
 * @param list
 * @returns {OCPage}
 */
function OCPage(){
	//@NOT PERFECT
	//return this.getPage();
	//return function(){
		if(window.singleton){this.prototype = {};return false;}else{window.singleton = true;}
		this.init = function(){
			//this.execute(this.load8800AList);
			this.execute(this.basicLoading, null, null, this.initializeFailure);
			//@if empty throw error
			this.execute(this.isEntityListEmpty,null,null,this.initializeFailure); 
			//终止执行
			if(this.isEntityEmpty){
				this.initializeFailure();
				throw new Error('INITIALIZE ERROR,PLEASE REFRESH DEVICE ');
			};
			//this.execute(this.createScroller);
			//---监听器只能绑定一次,绑定多了就会多次触发监听处理器----//
			//this.execute(this.addScollerListener,null,null,this.addScollerListenerFailure);
			//TODO 刚进入页面时的entity.entityId,entity.entityType从和出来,所以只能记录到this对象中,另外entity对象中也应该记录下来,方便使用
			//this.entity就是新的entity,先创建,再初始化,并完成entity的所有功能
			var firstEntity = this.entityList.list[0];
			if(cmcSupport){
				this.seachCmcName();
			}
			this.execute(this.changeEntity,{entityId:firstEntity.entityId,entityType:firstEntity.entityType}, "@ONU.loadError@" ,this.changeEntityFailure);
			//创建PropertyGrid
			this.execute(this.createAttributeGrid,null, I18N.ONU.CREATEGRIDFAILURE ,this.createAttributeGridFailure);
		}
//		return _page;
	//}();
	//@Event Driven Later
}


OCPage.prototype.entity = null;
OCPage.prototype.entityId = null;//a shortCut to get id
OCPage.prototype.entityType = null;//a shortCut to get type
OCPage.prototype.entityList = null;
OCPage.prototype.gridRendered = false;
OCPage.prototype.scrollerRendered = false;
OCPage.prototype.dispalyEntityType =null;//include onu type & cc type(precise)
OCPage.prototype.isEntityEmpty = true;
OCPage.prototype.tree = null;//只指向entity的tree就可以了,其实并未新建对象
OCPage.prototype.entityReady = false;

/**
 * 基本操作
 */
OCPage.prototype.basicLoading = function(){
	//console未实现
	if (navigator.userAgent.indexOf("MSIE")>0) {window.console = {debug:function(){},log : function(){}}}
	//TODO 完善错误处理机制
	window.onerror = function(e){
		//保证出错时等待框一定会消失
		window.parent.closeWaitingDlg();
		return true
	}
	//var entityList = ${entityList}; 
	this.execute(this.setSource,{entityList:entityList});
}

/**
 * @deprecated 8800A特殊处理
 * 得到8800A的列表
 */
OCPage.prototype.load8800AList = function(){}

/**
 * 设置entities数据源
 * @entityList source
 */
OCPage.prototype.setSource = function(entityList){
	this.entityList = {
		count : entityList.length || 0 ,
		curindex : 0,
		checkedEntityIndex : 0, 
		list : entityList || [] 
	};
	for(var i=0;i<this.entityList.list.length;i++){
	    var _entity = this.entityList.list[i];
	    if(EntityType.isCcmtsType(_entity.entityType)){
	    	_entity.category = CATEGORY.CMC;
	    }else{
	    	_entity.category = CATEGORY.ONU;
	    }
	}
}


/**
 * execute,hide actual function call
 * @handler test function
 * @args object
 * @msg error Message
 * @exfunc error handler
 */
OCPage.prototype.execute = function(handler,args,msg,exfunc){
	try{
		if(!handler)return;
		switch(typeof handler){
			case 'string':
				eval('this.'+handler+'('+args+')');//TODO 传参
				break;
			case 'function':
				handler.apply(this,this.toMatrix(args));
				break;
			default:
				break;
		}
	} catch(e) {
		//show message
		!!msg && window.parent.showMessageDlg(I18N.COMMON.tip,msg);
		//call error handler
		switch(typeof exfunc){
			case 'string':
				eval('this.'+exfunc+'()');
				break;
			case 'function':
				exfunc.apply(this);
				break;
			default:
				break;
		}
	}
}

/**
 * 将对象转为数组
 * @obj 包含参数的对象
 */
OCPage.prototype.toMatrix = function(obj){
	//如果无效则返回空参数
	if(!obj)return [];
	//否则就构造出真正的参数对象
	var result = new Array();
	for(var key in obj){
		result.push(obj[key]);
	}
	return result;
}


/**
 * 检查是否一个ENTITY都没有,如果都没有则全部清空页面
 */
OCPage.prototype.isEntityListEmpty = function(){
	switch(this.entityList.list.length == 0 ){
		case true:
			this.isEntityEmpty = true //后期可能重新加载,所以需要重复设置true,而不是使用默认值
			throw new Error()
		case false:
			this.isEntityEmpty = false
			break;
	}
}


/**
 * 创建属性表格
 */
OCPage.prototype.createAttributeGrid = function(onu){
	var o = this
	//---modify propertygrid prototype,set default display sequence----//
	Ext.grid.PropertyGrid.prototype.initComponent = function(){
		this.customEditors = this.customEditors || {};
		this.lastEditRow = null;
		var store = new Ext.grid.PropertyStore(this);
		this.propStore = store;
		var cm = new Ext.grid.PropertyColumnModel(this,store);
		//store.store.sort('name','ASC');//取消默认排序
		this.addEvents(
			'beforepropertychange',
			'propertychange'
		);
		this.cm = cm;
		this.ds = store.store;
		Ext.grid.PropertyGrid.superclass.initComponent.call(this);
		this.selModel.on('beforecellselect',function(sm,rowIndex,colIndex){
			if(colIndex === 0)return false;
		});
	}
	if(Ext.grid.PropertyColumnModel) {
		Ext.apply(Ext.grid.PropertyColumnModel.prototype, {
			nameText : "name",
			valueText : "value"			
		});
	}
	this.grid = new Ext.grid.PropertyGrid({
		renderTo : 'viewRightPartBottomBody',
		id:'entityGrid',
		//title : I18N.COMMON.property ,
		border : false,
		headerAsText : true,
		hideHeaders : true,
		autoHeight:true,
		frame : false,
		listeners: {
            'render': function(proGrid) {
                var view = proGrid.getView();
                var store = proGrid.getStore();
                proGrid.tip = new Ext.ToolTip({
                    target: view.mainBody,
                    delegate: '.x-grid3-row',
                    trackMouse: true,
                    renderTo: document.body,
                    listeners: {
                        beforeshow: function updateTipBody(tip) {
                            var rowIndex = view.findRowIndex(tip.triggerElement);
                            tip.body.dom.innerHTML = store.getAt(rowIndex).get('name') + " : " +store.getAt(rowIndex).get('value');
                        }
                    }
                });
            },
        	'rowdblclick':function(g,r,e ){
        		//-------双击时默认复制行内容	
        		var thisText = e.getTarget().innerHTML;
        		//-----属性值可能是number型,故强转string
        		clipboardData.setData("text",thisText);
        		//window.parent.showMessageDlg( I18N.COMMON.tip, I18N.EPON.copyOk + thisText );
       		},
       		'beforeedit' : function(e){
       			e.cancel = true;
       			return false;
       		}
        },
		source : {}
	});
	Ext.grid.PropertyGrid.prototype.setSource = function(source) {
		this.propStore.store.sortInfo
		this.propStore.setSource(source);
	};
}

/**
 * 初始化失败
 * @树以及grid需要销毁
 * @滚动条上的图片需要替换
 */
OCPage.prototype.initializeFailure = function(){
	this.grid && this.grid.hide();
	//当时切换ONU时,tree就可能已存在
	this.tree && this.tree.destory();
}
/**
 * 创建树失败
 */
OCPage.prototype.createTreeFailure = function(){
}

/**
 * 绘制entity失败
 */
OCPage.prototype.drawEntityFailure = function(){
	page.destoryComponents();
	page.entity && page.entity.ui.remove();//可以全部删除,不不需要上面的步骤
}

/**
 * 展示出组件
 */
OCPage.prototype.showComponent = function(){
	this.tree && this.tree.show();
	this.grid && this.grid.show();
}

/**
 * 销毁组件 tree,panel,hideGrid
 */
OCPage.prototype.destoryComponents = function(){
	//-------clear page------//
	this.tree && this.tree.destroy();//for ccmts & onu
	if(serviceTree){
		serviceTree.destroy();
	}
	//-------destroy entity panel----//
	$cache = $("#entityContainer").remove().empty();
	$("#PanelContainer").empty().append( $cache );
	//TODO this.entity && this.entity.ui.remove();
	//-------no need to clear grid,hide it explicitly for compatible purpose----//
	//Ext.getCmp('entityGrid').hide();
	this.grid && this.grid.setSource({});
	this.grid && this.grid.hide();
	$("#entityType").empty();
}

/**
 * 切换展示entity
 * @entityId for cc entityID && for onu onuid
 * @category cmc && onu
 */
OCPage.prototype.changeEntity = function(entityId,entityType){//not entityID,just onuId,but adapt to ccmts
	//每次切换设备的时候都需要将组件销毁
	this.destoryComponents();
	//---SERVICE----//
	this.entityId = entityId;
	//type code
	this.entityType = entityType;
	if (this.changeTimeout && !isNaN(this.changeTimeout)) return false;
	var o = this;
	WIN.top.showWaitingDlg("@COMMON.wait@","@ONU.loading@",null,null,false);
	this.changeTimeout = setTimeout(function (){
		 o.changeTimeout = null;
		 o.sendRequest('/epon/standardOlt/loadStandardOnuInfo.tv',{onuId:o.entityId},o.changeEntitySuccess,o.changeEntityFailure);
	}, 500);
}

/***
 * 重载ONU信息
 */
OCPage.prototype.reload = function(){
	this.changeEntity(this.entityId,this.entityType);
}

/***
 * modify entity property
 * @param objName : uni,onu,pon
 * @param property 
 * @param value
 * @param id : uniId,其他的用不上,只有UNI用得上
 */
function setEntityProperty(objName,property,value,id){
	switch(objName){
		case 'uni':
			for(var i=0; i<entity.onuUniPortList.length;i++){
				if(entity.onuUniPortList[i].uniId == id){
					eval("entity.onuUniPortList["+i+"]."+property+" = value")
					entity.showComponentProperty(currentId)
					return
				}
			}
		default:
	}
}

/**
 * 切换设备成功
 */
OCPage.prototype.changeEntitySuccess = function(json){
	WIN.top.closeWaitingDlg();
	var newEntity = json.onu;
	var o = this;
	if(!cmcSupport){
		o.entity && o.entity.destory();
		//附加cmcList
		window.entity = o.entity = new OCEntity(newEntity);
		o.entity.init();
		//标记entity已成功返回
		o.entityReady = true;
		return;
	}
	o.entity && o.entity.destory();
	window.entity = o.entity = new OCEntity(newEntity);
	o.entity.init();
	//标记entity已成功返回
	o.entityReady = true;
	
	/*
	//TODO 根据类型去判断
	$.ajax({
		url:'/cmc/queryCmcFromOnu.tv',
		data:{onuId:o.entityId},
		cache:false,
		dataType:'json',
		success:function(json){
			o.entity && o.entity.destory();
			//附加cmcList
			newEntity.cmcList = json.cmcList;//其实只返回一个值
			//将cmc的nmName和更新时间 赋给8800a进行展示
			if(newEntity.cmcList.length>0){
				newEntity.onuName = newEntity.cmcList[0].nmName;
				newEntity.changeTime = newEntity.cmcList[0].dt;	
				newEntity.onuTimeSinceLastRegister = newEntity.cmcList[0].topCcmtsSysUpTime/100;	
			}
			window.entity = o.entity = new OCEntity(newEntity);
			o.entity.init();
			//标记entity已成功返回
			o.entityReady = true;
		},
		error:function(){
			o.changeEntityFailure();
		}
	})*/
}

/**
 * 切换entity失败
 */
OCPage.prototype.changeEntityFailure = function(error){
	WIN.top.closeWaitingDlg();
	Ext.getCmp('entityTree') && Ext.getCmp('entityTree').destory();
	Ext.getCmp('entityGrid') && Ext.getCmp('entityGrid').hide();
	$('#entityContainer').empty();
	//this.scroller.disable(entity.entityId)
	this.entityReady = null;
	throw new Error("change entity error");
	
}

/**
 * 修改ONU名称
 * @text: new Name
 */
OCPage.prototype.changeEntityName = function(text) {
	 //----修改ONU集合中的该ONU名称数据----//
	 this.entityList.list[this.entityList.checkedEntityIndex].entityName = text
	 //---重新绘制该ONU（别名修改后可能出现未知异常,故采用此浪费性能的办法）----//
	 this.changeEntity(this.entity.entityId,this.entity.entityType);
}

/**
 * 告警展示
 * #目前只做展示中的ONU的提示,非展示的ONU忽略此告警
 * @source 消息来源
 * @value 告警值
 */
var displayEmergence = function(source,value){
	// 修改OnuObject中的operationstatus值。
	if(entity.onuIndex == source){
		page.changeEntity(page.entityId,page.entityType)
	}
}

/**
 * 重新发起采集并重新加载该entity
 */
OCPage.prototype.collect = function(){
	$("#subPannel .nm3kScollBarMiddleTable").css({left:0});//将滚动的还原;
	var o = this;
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.EPON.cfmRefreshOnu, function(type) {
        if (type == 'no'){
        	return;
        }
		//createRefreshMask();
        window.top.discoveryEntityAgain(entityId, null, function() {
        	window.location.reload();
    	});
	});
}


/**
 * 发送一个请求
 * @url
 * @data object
 * @callback success
 * @error error
 */
OCPage.prototype.sendRequest = function(url,data,callback,error){
	var o = this;
	$.ajax({
		url:url,cache: false,data:data,dataType:'json',
		success:function(json){
			callback.apply(o,[json]);
		},error:function(){
			error.apply(o);
		}
	});
}

/**
 *  当下级设备是CMC时,滚动模块中设备的名称显示为CMC的nmName
 * 获取下级设备onuId与cmcName
 */
OCPage.prototype.seachCmcName = function(){
	var o = this;
	$.ajax({
		url:'/cmc/seachCMCNameList.tv',
		data:{entityId:entityId},
		cache:false,
		dataType:'json',
		success:function(json){
			var list = o.entityList.list;
			for(var i in list){
				var entity = list[i];
				var entityId = entity.entityId;
				var tempName = json[entityId];
				if(tempName!=undefined){
					entity.entityName = tempName;
				}
			}
			o.entityList.list = list;
			//o.createScroller();
		},
		error:function(e){
		}
	})
}

//##################################################
//############    EXCEPTION   ######################
//##################################################
/**
 * 基本数据加载失败
 */
OCPage.prototype.basicLoadingFailure = function(){}

