/**
 * OCEntity.js (c)2012, Topvision Copyrights
 * 
 * @author Bravin
 */
//#################################
//#########  CONSTANCE  ###########
//#################################
var CATEGORY = {
	ONU : 'onu',
	CMC : 'cmc'
}
var TREETITLE = {
	ONU : "@ONU.deviceConstruct@",
	CMC : "@ONU.deviceConstruct@"
}

var ERRORMESSAGE = {
	 INITIALIZEFAILURE : "@ONU.INITIALIZEFAILURE@",
	 CREATETREEFAILURE : "@ONU.CREATETREEFAILURE@",
	 DRAWENTITYFAILURE : "@ONU.DRAWENTITYFAILURE@",
	 STARTLISTENFAILURE : "@ONU.STARTLISTENFAILURE@"
}

var IMG = {
	CMCDEVICE : '/epon/image/cmc/cmc.png',
	ONUDEVICE : '/epon/image/onu/onu.png',
	iCMCDEVICE :'/epon/image/cmc/icmc.png',
	CMCICON : '/epon/image/cmc/cmcIcon.png',
	ONUICON : '/epon/image/onu/8624Icon_48.png',
	iCMCICON : '/epon/image/cmc/8622Icon.png',
	PONIMG : "/epon/image/pon_hover.png",
	UNIIMG : "/epon/image/uni_hover.png"
}

var uniWorkModeStr = ["@ONU.workMode1@", "@ONU.workMode2@" , "@ONU.workMode3@" , "@ONU.workMode4@" , "@ONU.workMode5@" , "@ONU.workMode6@",
                      "@ONU.workMode7@", "@ONU.workMode8@", "@ONU.workMode9@", "@ONU.workMode10@"]

/*********************************************************
 * ONU是个对象,而现在该对象中包含了太多的页面创建方法
 * @param obj
 * @returns {OCEntity}
 **********************************************************/
function OCEntity(obj){
	//cache,delete after initialized
	this.data = obj;
	this.init = function(){
		//初始化对象,保证完整性
		this.execute(this.initialize,null,ERRORMESSAGE.INITIALIZEFAILURE);
		this.execute(this.createTree,null,ERRORMESSAGE.CREATETREEFAILURE,page.createTreeFailure);// page is a object of OCPage
		this.execute(this.drawEntity,null,ERRORMESSAGE.DRAWENTITYFAILURE,page.drawEntityFailure);// page is a object of OCPage
		//TODO 默认不展示操作菜单
		//this.execute(this.showService);
		this.execute(this.startListen,null,true?null:ERRORMESSAGE.STARTLISTENFAILURE);//reserve
		//set entity up & down
		this.execute(this.showStatus, null,null, this.showStatusFailure);
		//show entity property firstly : for onu:onuDevice,for c-ctms : cmcDevice
		this.execute(this.showComponentProperty,{nodeid : 'onuDevice'});
		//this.execute(page.showComponent);
	};
}

OCEntity.prototype.ui = null;
OCEntity.prototype.data = null;
OCEntity.prototype.tree = null;
OCEntity.prototype.grid = null;
OCEntity.prototype.treeFlag = null;
OCEntity.prototype.showComponentPropertyDelay = false;
OCEntity.prototype.treeExpand = false;
OCEntity.prototype.showing = CATEGORY.ONU;//所有情况第一次展示的都是ONU//EOC的暂时不考虑


/***********************************************************
 * execute,hide actual function call
 * @handler test function
 * @args object
 * @msg error Message
 * @exfunc error handler
 **********************************************************/
OCEntity.prototype.execute = function(handler,args,msg,exfunc){
	try{
		if(!handler)return;
		switch(typeof handler){
			case 'string':
				eval('this.'+handler+'('+args+')');//TODO 传参
				break;
			case 'function':
				handler.apply(this,page.toMatrix(args));
				break;
			default:
				break;
		}
	}catch(e){
		//show message
		!!msg && window.parent.showMessageDlg("@COMMON.tip@",msg);
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

OCEntity.prototype.showService = function(){
	var $e = {};
	if(EntityType.isCcmtsType(this.entityType)){
		showCC8800AService($e);
	}else{
		showOnuService($e);
	}
}

/**
 * DATA INITIALIZE & CHECK
 * @obj 数据初始化
 */
OCEntity.prototype.initialize = function(){
	//修改为从数据库读
	var olttype = EntityType.getOltType();
	IMG.OLTDEVICE = oltImg = "/images/network/olt_48.gif";//待修改,未知类型时的图片
	$.ajax({
	    url:'/entity/loadSubEntityType.tv?type=' + olttype,async: false, 
	    success:function(response) {
	  	  var entityTypes = response.entityTypes 
	  	  for(var i = 0; i < entityTypes.length; i++){
	           if(typeId == entityTypes[i].typeId){
	        	   displayName = entityTypes[i].displayName;
	        	   IMG.OLTDEVICE = oltImg = "/images/" + entityTypes[i].icon48;
	           }
	  	  }
	    },
	    error:Ext.emptyFn,cache:false
	});
    //get it's category,刚进入时,属性可能为onuType,也可能为cmcType,故取列表中的entityType
    this.uniSize = this.data.onuUniPortList.length
    //initialize it accord to it's category
    this.initializeONU()
	//---remove data property---//
    delete this.data
}

/**
 * ONU的初始化操作,兼容部分CMC
 */
OCEntity.prototype.initializeONU = function(obj){
	//TODO 数据初始化缺少CCMTS
	//this.data = obj;
	obj = obj || this.data
	//TODO SERVER对ONU-CC对象貌似并没有给予OLT-ENTITYID
	//window.entityId = this.oltEntityId = obj.entityId || 0;
	//---this.onuId = obj.onuId || 0;---//
	this.entityId = obj.onuId || 0
	this.onuIndex = obj.onuIndex || 0
	this.entityName = obj.onuName || ''
    //---this.onuType = obj.onuType || 255---//
	//this.entityType = obj.onuType || 255;
	this.typeName = obj.typeName
	this.onuPreType = obj.onuPreType
    this.entityType = page.entityType
    this.onuMac = obj.onuMac || 0 //TODO WHERE DEFINED IT IN MIB
    this.onuMac = Validator.convertMacToDisplayStyle(this.onuMac, displayStyle);
    this.onuMacAddress = obj.onuMacAddress || ''//STRING 
    //up(1), down(2), testing(3), 状态处理统一在onuObjectCreate中从entitysnap表中获取,不再从各自表中获取
    this.onuOperationStatus = obj.onuOperationStatus || 2
    this.onuAdminStatus = obj.onuAdminStatus || 2
    this.onuChipVendor = obj.onuChipVendor || 'TOPVISION' //TODO default is topvision or ''
    this.onuChipType = obj.onuChipType || ''
    this.onuChipVersion = obj.onuChipVersion|| ''
    this.onuSoftwareVersion = obj.onuSoftwareVersion || ''
    this.onuFirmwareVersion = obj.onuFirmwareVersion || ''
    this.onuTestDistance = obj.onuTestDistance || -1//TODO default distance
    this.onuLlidId = obj.onuLlidId || 0
    this.onuTimeSinceLastRegister = obj.onuTimeSinceLastRegister || 0
    this.changeTime = obj.changeTime
    //enable(1), disable(2)
    this.temperatureDetectEnable = obj.temperatureDetectEnable || 2
    this.topOnuCurrentTemperature = obj.topOnuCurrentTemperature || 127
    this.onuFecEnable = obj.onuFecEnable || 2
    this.ponPerfStats15minuteEnable = obj.ponPerfStats15minuteEnable || 2
    this.ponPerfStats24hourEnable = obj.ponPerfStats24hourEnable || 2
    this.onuIcon = obj.onuIcon || ''
    this.onuIsolationEnable = obj.onuIsolationEnable || 2
    this.onuDisplayTemp = obj.onuDisplayTemp || 0
    this.stirringEnable = obj.stirringEnable || 2
    //PON PORT
    this.oltPonOptical = new Object();
	switch(!!obj.oltPonOptical){
	    case true:
	    	this.oltPonOptical.txPower = obj.oltPonOptical.txPower || 0
	    	this.oltPonOptical.rxPower = obj.oltPonOptical.rxPower || 0
	    	this.oltPonOptical.transPower = obj.oltPonOptical.transPower || 0
	    	this.oltPonOptical.revPower = obj.oltPonOptical.revPower || 0
	    	this.oltPonOptical.portIndex = obj.oltPonOptical.portIndex 
	    	break
	    case false:
	    	this.oltPonOptical = null
	    	break
	}
    this.onuPonPort = new Object();
    switch(!!obj.onuPonPort){
	    case true:
	    	this.onuPonPort.onuPonId = obj.onuPonPort.onuPonId || 0
    	    this.onuPonPort.onuPonIndex = obj.onuPonPort.onuPonIndex || 0
    	    this.onuPonPort.onuPonRealIndex = obj.onuPonPort.onuPonRealIndex || 0
    	    this.onuPonPort.onuBiasCurrent = obj.onuPonPort.onuBiasCurrent || 0
    	    this.onuPonPort.onuWorkingVoltage = obj.onuPonPort.onuWorkingVoltage || 0
    	    this.onuPonPort.onuWorkingTemperature = obj.onuPonPort.onuWorkingTemperature || 0
    	    this.onuPonPort.slotNo = obj.onuPonPort.slotNo || 0
    	    this.onuPonPort.ponNo = obj.onuPonPort.ponNo ||0
    	    this.onuPonPort.onuRevPower = obj.onuPonPort.onuRevPower || 0;
	    	this.onuPonPort.onuTransPower = obj.onuPonPort.onuTransPower || 0;
	    	break
	    case false:
	    	this.onuPonPort = null
	    	break
    }
    //初始化并校验UNI端口数据
    this.initUniData(obj.onuUniPortList)
    //初始化并校验COM端口数据
    if(typeof this.comSize != 'undefined'){this.initComData(obj.onuElecComList);}
    //初始化CMC数据
    if(!obj.cmcEntity)
    	return
    var cmcEntity = obj.cmcEntity
    this.cmcList = new Array()
    this.hasCmc = !!obj.cmcEntity
    //CMC结点初始化,适用于8800A的初始化
	var o = this.cmcList[0] = this.cmcEntity = new Object()
	o.entityId = cmcEntity.entityId || 0
    o.cmcEntityId = cmcEntity.cmcEntityId || 0
    o.cmcIndex = cmcEntity.cmcIndex || 0
    o.cmcId = cmcEntity.entityId || 0
    //不在这里进行状态的处理,在获取oltonuAttribute中状态时从entitysnap表中获取
    //this.onuOperationStatus = cmcEntity.topCcmtsSysStatus || 2;
    o.cmcMac = cmcEntity.topCcmtsSysMacAddr || ""
    //o.typeId = cmcEntity.cmcType || ""
    o.onuId = cmcEntity.onuId || 0
    o.onuIndex = cmcEntity.onuIndex || 0
    this.cmcType = o.entityType = cmcEntity.typeId || 0
    o.entityName = cmcEntity.name || "@ONU.cmc8800b@"
    o.cmcDeviceStyle = cmcEntity.cmcDeviceStyle || 0//TODO WHICH
    o.sysDesc = cmcEntity.sysDesc || ''
    o.sysObjectID = cmcEntity.sysObjectID || 0
    o.sysUpTime = cmcEntity.topCcmtsSysUpTime || 0
    o.sysName = cmcEntity.sysName || ''
    o.sysLocation = cmcEntity.sysLocation || ''
    o.sysServices = cmcEntity.sysServices || ''//TODO WHICH
    o = null
}

/**************************************************
  			初始化UNI端口数据校验
 ***************************************************/
OCEntity.prototype.initUniData = function(uniList){
	this.onuUniPortList = new Array();
	var i = -1;
	var cursor = 0;
	while(++i < this.uniSize){
		try{
			var port = new Object();
			switch(uniList[cursor].uniRealIndex-1 == i){
				case true:
					port.uniId = uniList[cursor].uniId
					port.uniIndex = uniList[cursor].uniIndex
					port.uniOperationStatus = uniList[cursor].uniOperationStatus
					port.uniAdminStatus = uniList[cursor].uniAdminStatus
					port.uniAutoNegotiationEnable = uniList[cursor].uniAutoNegotiationEnable
					port.topUniAttrAutoNegotiationAdvertisedTechAbility = uniList[cursor].topUniAttrAutoNegotiationAdvertisedTechAbility
					port.flowCtrl = uniList[cursor].flowCtrl
					port.uniRealIndex = uniList[cursor].uniRealIndex
					port.uniAutoNegoString = uniWorkModeStr[parseInt(uniList[cursor].uniAutoNegoString) + 1]
					port.perfStats15minuteEnable = uniList[cursor].perfStats15minuteEnable
					port.perfStats24hourEnable = uniList[cursor].perfStats24hourEnable
					port.collected = true
					port.uniDSLoopBackEnable = uniList[cursor].uniDSLoopBackEnable;
					port.uniUSUtgPri = uniList[cursor].uniUSUtgPri;
					cursor++;
					break;
				case false://如果中间的端口没有采集到
					port.collected = false;
					break;
			}
			this.onuUniPortList[i] = port;
		}catch(e){//如果后面的端口都没有采集到
			var port = new Object();
			port.collected = false;
			this.onuUniPortList[i] = port;
			break;
		}
	}
}



/**
 * 树节点点击事件处理器
 */
OCEntity.prototype.treeNodeHandler = function (node,e) {
	if (!this.treeFlag) {
        treeFlag = true;
        try{
        	var id = node.attributes.id;
        	currentId = id;
        	switch(id.substring(0,3).toLowerCase()){
        		case CATEGORY.CMC:
        			if(CATEGORY.CMC != this.showing){//以后可能不只是有CMC,故不设为Boolean型
        				this.showing = CATEGORY.CMC;
        				this.showDevice(CATEGORY.CMC,id.substring(3,4));
        			}
        			break;
        		default:
        			//-----如果当前展示的是CMC,则切换回到ONU---//
        			if(CATEGORY.ONU != this.showing){
        				//TODO 目前此处的ONU绘制都采用的是通用的绘制方法,以后需要对ONU的绘制也进行分类
        				this.showing = CATEGORY.ONU; 
        				this.showDevice(CATEGORY.ONU);
        			}
	        		//-----级联设备视图:在不在都需要级联----//
	        		DOC.getElementById(node.attributes.id).click();
        			break;
        	}
        	this.contextmenuHnd(node,e);
        	//-----显示属性
			this.showComponentProperty(node.attributes.id);
        }catch(e){}
        this.treeFlag = false;
    }
}

/**
 * 树节点右键事件
 */
OCEntity.prototype.contextmenuHnd = function(node, e) {
    var id = node.id;
	this.tree.getNodeById( id ).select();
	switch( id.substring(0,3).toLowerCase() ){
		case CATEGORY.CMC:
			break;
		case CATEGORY.ONU:
			//区分
			switch(id.substring(3,6).toLowerCase()){
				case 'dev':
					if(EntityType.isCcmtsType(this.entityType)){
						showCC8800AService(e);
					}else{
					    showOnuService(e);
					}
					break;
				case 'pon':
				    showOnuPonService(e);
					break;
				case 'uni':
				    showUniService(e);
					break;
				default:
					break;
			}
			break;
		default:
			break;
	}
}

/**
 * 设备视图上的右键事件
 * @el element
 * @e event
 */
OCEntity.prototype.entityContextmenu = function(el,e){
	currentId = el.id;
	$(el).trigger("click");
	this.showComponentProperty(el.id);
	e.preventDefault();
	preventBubble(e);
	clearPage();
	//并记录本次div 的id,放入divCache中
	divCache.unshift(el.id)
	if((el.id.substring(0,6) == 'onuPon' || el.id.substring(0,6) == 'onuUni') && this.uniStyle == '16*16')
		el.style.border = "1px solid yellow"
	else
		el.style.border = "2px solid yellow"
}

/**
 * @设备视图上的点击事件
 * @el element
 * @e event
 */
OCEntity.prototype.onEntityClick = function(el,e) {
	preventBubble(e);
	clearPage();
	divCache.push(el.id);
	//并记录本次div 的id,放入divCache中
	if((el.id.substring(0,6) == 'onuPon' || el.id.substring(0,6) == 'onuUni' ) && this.uniStyle == '16*16')
		el.style.border = "1px solid yellow"
	else
		el.style.border = "2px solid yellow"
	this.showComponentProperty(el.id);
	if(!this.treeExpand){
		this.tree.getRootNode().expand();
	}
	//this.tree.getNodeById(el.id).select();
}

/**
 * 显示组件属性
 */
OCEntity.prototype.showComponentProperty = function(nodeId){
	if(this.showComponentPropertyDelay)return;
	var o = this;
	this.showComponentPropertyDelay = setTimeout(function(){o._showComponentProperty(nodeId)},100);
}

/**
 * 将值转成8字节
 */
function convert2Bit(value){
	if(typeof value !='numver')value=parseInt(value)
	var v = value.toString(16).toUpperCase();
	var length = v.length;
	for(var i=0;i<4-length;i++){
		v = '0'.concat(v);
	}
	return '0X'.concat(v);
}

/**
 * ONU面板图组件生成方法
 * @idx
 * @entityId_
 * @Lpx
 * @Tpx,
 * @Wid,
 * @heig,
 * @img
 * @pDiv
 */
OCEntity.prototype.createComponent = function(idx, entityId_, Lpx, Tpx, Wid, heig, img, pDiv,title,notModify){
	$entity = $('<div>');
	if(!notModify){
		Wid += 2;heig += 2;
	}else{
		Wid += 0;heig += 0;
	}
	$entity.css({
		position : "absolute",
		left : Lpx,
		top : Tpx,
		width : Wid,
		height : heig,
		backgroundImage : "url(" + img + ")",
		backgroundRepeat : "no-repeat"
	})
	//命名空间没定好,故在此用class属性来区分。class属性也需要细分
	if('onuDevice' == entityId_) {
		$entity.attr('id' , entityId_).addClass("onuClass");
	} else if('onuPon_0'==entityId_){
		$entity.attr('id' , entityId_).addClass("ponPortClass port");
	} else if("oltPanel" == entityId_){
		switch(title){
			case "olt":
				$entity.attr({
					id : entityId_,
					title: 'OLT'
				});
				break;
			case "onu":
				$entity.attr({
					id : entityId_,
					title: 'ONU'
				});
				break;
			default:
				$entity.attr({
					id : entityId_,
					title: 'OLT'
				});
				break;
		}
	} else if("onuUni" == entityId_.substring(0,6)){
		$entity.attr('id' , entityId_).addClass("uniPortClass port")
	} else {
		$entity.attr('id' , entityId_)
	}
	if(!notModify)
		$entity.css({border : '2px solid transparent'})
	/*else if( 'onuPon' == entityId_.substring(0,6))
		$entity.css({border : '2px solid transparent'})*/
	if(this.uniStyle == '16*16')
		$entity.css({border : '1px solid transparent'})
	$("#" + pDiv).append($entity);
	return $entity;
}

/**************************************************************
 * ONU面板图组件生成方法(异常组件）
 * @idx
 * @entityId_
 * @Lpx
 * @Tpx,
 * @Wid,
 * @heig,
 * @img
 * @pDiv
 ***************************************************************/
OCEntity.prototype.createComponent2 = function(idx, entityId_, Lpx, Tpx, Wid, heig, img, pDiv){
	$entity = $('<div>');
	$entity.css({
		position : "absolute",
		left : Lpx,
		top : Tpx,
		width : Wid + 4,
		height : heig + 4,
		backgroundImage : "url(" + img + ")",
		backgroundRepeat : "no-repeat",
		border : '2px solid transparent',
		opacity : 0.2
	});
	$("#" + pDiv).append($entity);
	return $entity;
}

/*********************
 	include cc onu
 ********************/
OCEntity.prototype.drawEntity = function(){
	if(EntityType.isCcmtsType(this.entityType)){
		//$("#entityType").html(this.typeName)
	}else{
		//$("#entityType").html(this.onuPreType)
	}
	//drawEntity with its type
	this.uniStyle = "20*20";
	switch(parseInt(this.entityType)){//CC,ONU
		case 33:
			IMG.ONUDEVICE = '/epon/image/onu/onu_1.png'
			this.drawOnu_GENERAL(1);
			//entity.drawOnu_1GE_I();//1PORTS 室外
			break;
		case 34:
			IMG.ONUDEVICE = '/epon/image/onu/outDoorOnu.png'
			this.drawOnu_GENERAL(1);
			//entity.drawOnu_1GE_O();//1PORTS 野外
			break;
		case 36:
		case 37:
		case 40:
			IMG.ONUDEVICE = '/epon/image/onu/onu.png'//4FE室外..目前也使用室外型的外壳			
			this.drawOnu_GENERAL(4);
			break;
		case 65://8FE
			IMG.ONUDEVICE = '/epon/image/onu/PN8641.png';
			entity.drawOnu_8641();
			break;
		case 68://16FE
			this.uniStyle = "16*16";
			IMG.ONUDEVICE = '/epon/image/onu/PN8644.png';
			entity.drawOnu_8644();
			break;
		case 71://24FE
			this.uniStyle = '16*16';
			IMG.ONUDEVICE = '/epon/image/onu/PN8647.png';
			entity.drawOnu_8647();
			break;
		case 30001:
		case 30005:
		case 30007:
		case 30013://CC_TYPE_CC8800C-A   整合型
		case 30014://CC_TYPE_CC8800A   整合型
        case 30015:
        case 30021:
			entity.drawCMC_A();
			break;
		default:
			IMG.ONUDEVICE = '/epon/image/onu/onu_1.png'
			entity.drawUnknown(this.uniSize);//ANY PORTS
			break;
	}
	//显示滚动条,tree,grid等组件
	page.showComponent()
}

/*******************************
  		集成型CMC的绘制方法
 *******************************/
OCEntity.prototype.drawCMC_A = function(){
	this.olt = this.createComponent(0, "oltPanel", 75, 37, 50, 50, IMG.OLTDEVICE, "PanelContainer");
	//draw onu(cmc),cmc也要当onu看,太扯淡了
	var $typeId = this.entityType;
	var cmcImage = null;
	if(EntityType.isCC8800C_A($typeId)){   //C-A的图片处理
		cmcImage = '/epon/image/cmc/icmc-a.png';
	}else if(EntityType.isCC8800ES($typeId)){   //E的图片处理
        cmcImage = '/epon/image/cmc/icmc-e.png';
    }else if(EntityType.isCC8800CES($typeId)){   //C-E的图片处理
        cmcImage = '/epon/image/cmc/icmc-ce.png';
    }else if(EntityType.isCC8800DES($typeId)){   //D-E的图片处理
        cmcImage = '/epon/image/cmc/icmc-de.png';
    }else if(EntityType.isCC8800C10GS($typeId)){   //C-10G的图片处理
        cmcImage = '/epon/image/cmc/icmc-c10g.png';
    }else{
		cmcImage = IMG.iCMCDEVICE;
	}
	this.ui = this.createComponent(0, "onuDevice", 150, -80, 280, 190, cmcImage, "entityContainer");
	//DRAW LINE,NO SPLITER
	var color = this.onuOperationStatus==1?'green':'red';
	//DRAW  SPLITER & UPDOWN LINE
	$spliter = $("<div class='spliterClass' onuOperationStatus=4></div>");
	$spliter.appendTo($("#PanelContainer"));
	if(this.onuOperationStatus == 1){
		$('<div class="greenGIF" style="width:97px; height:105px; top:73px; left:102px; position:absolute; z-index:6000"></div>').appendTo($("#PanelContainer"));
	}else{
		$('<div class="redGIF" style="width:97px; height:105px; top:73px; left:102px; position:absolute; z-index:6000"></div>').appendTo($("#PanelContainer"));
	}
	
	
	//this.spliter = this.drawPolyLine(color,3,false,'98,68 98,180 185,180');
	//计算参数信息
	var vmlSlot = this.onuPonPort.slotNo;
	var vmlPonPort = this.onuPonPort.ponNo;
	//---------解析ONU-INDEX-----------//
	var onuindex = this.onuIndex.toString(16);
	if (onuindex.length==9)
		onuindex = '0'.concat(onuindex);
	onuindex  = parseInt(onuindex.substring(4,6),16);
	var vmlPort = "@ONU.portNum@:" + vmlSlot + '/' + vmlPonPort + ':' + onuindex;
	var vmlLight1,vmlLight2,vmlLight3,vmlLight4;
	if(this.onuOperationStatus == 1){
		vmlLight1  = "@ONU.sendOptRate@:" + (this.onuPonPort.onuTransPower) + ' dBm';
		vmlLight2  = "@ONU.recOptRate@:" + (this.onuPonPort.onuRevPower) + ' dBm';
	}else{
		vmlLight1 = "@ONU.sendOptRate@:0.00 dBm";
		vmlLight2 = "@ONU.recOptRate@:0.00 dBm";
	}
	vmlLight3  = "@ONU.ponport@@ONU.sendOptRate@:" + (this.oltPonOptical.transPower).toFixed(2) + ' dBm';
	//----绘制参数信息-----//
	$vmlText = $("<div>");
	$vmlText.css("zindex",500000)
	//端口号
	$('<div id="vmlInterface"></div>').appendTo($vmlText);
	//PON端口发送光功率
	$('<div id="vmlInterfaceSend"></div>').appendTo($vmlText);
	//PON端口接收光功率
	$('<div id="vmlInterfaceRec"></div>').appendTo($vmlText);
	$('<div id="vmlSender"></div><br><div id="vmlReveicer"></div>').appendTo($vmlText);
	$vmlText.addClass("vmlTextClass").appendTo($("#PanelContainer"));
	//修改参数描述的位置信息
	$("#vmlInterface").css({
		position:'absolute', left:130,  top:60
	});
	$("#vmlInterfaceSend").css({
		position:'absolute', left:110,  top:80
	});
	$("#vmlInterfaceRec").css({
		position:'absolute', left:110,  top:100
	});
	$("#vmlSender").css({
		position:'absolute', right:310, top:190, width:190, textAlign:'right'
	});
	$("#vmlReveicer").css({
		position:'absolute', right:310, top:220, width:190, textAlign:'right'
	});
	$("#vmlInterface").html(vmlPort);
	$("#vmlInterfaceSend").html(vmlLight3);
	//$("#vmlInterfaceRec").html(vmlLight4);
	$("#vmlSender").html(vmlLight1);
	$("#vmlReveicer").html(vmlLight2);
	this.text = $vmlText;
	this.text.port = $("#vmlPort");
	//光发射功率
	this.text.light3 = $("#vmlLight3");
	//光接受功率
	this.text.light4 = $("#vmlLight4");
	//光发射功率
	this.text.light1 = $("#vmlLight1");
	//光接受功率
	this.text.light2 = $("#vmlLight2");
}

/*****************************************
 				8 PORTS
 ****************************************/
OCEntity.prototype.drawOnu_8641 = function(){
	this.olt = this.createComponent(0, "oltPanel", 230, 32, 50, 50, IMG.OLTDEVICE, "PanelContainer")
	//draw onu(cmc),cmc
	this.ui = this.createComponent(0, "onuDevice", -20, -30, 454, 75, IMG.ONUDEVICE, "entityContainer")
	//onu pon
	this.execute(function(){
		this.onuPonPort.ui = this.createComponent(1, "onuPon_0", 398, 25, 23, 23, IMG.PONIMG, "onuDevice");
	},{},null,function(){
		this.onuPonPort.ui = this.createComponent2(0, "onuPon_0" , 398, 25, 23, 23, IMG.PONIMG, "onuDevice");
	});
	var portIndex = -1;
	///UNI PORTS
	while(++portIndex < this.uniSize){
		var portRealIndex = portIndex +1;
		var leftStart = 237
		if(portIndex>3)leftStart = 369
		var ui;
		this.execute(function(){
			var uni = this.onuUniPortList[portIndex];
			if(!uni.collected){throw error();}
			ui = uni.ui = this.createComponent(0, "onuUni_" + portRealIndex, leftStart + portIndex%4 * 28, 35, 23, 23, IMG.UNIIMG, "onuDevice");
			this.changeUniState(uni.uniAdminStatus,uni.uniOperationStatus,"onuUni_" + portRealIndex);
		},{},null,function(){
			ui = this.createComponent2(0, "onuUni_" + portRealIndex, leftStart + portIndex%4 * 28, 35, 23, 23, IMG.UNIIMG, "onuDevice");
		});
		ui.css("zoom",0.8)
	};
	//DRAW LINE,NO SPLITER
	var color = this.onuOperationStatus==1?'green':'red';
	this.spliter = this.drawPolyLine(color,2,false,'270,68 425,68 425,212','right');
	//计算参数信息
	var vmlSlot = this.onuPonPort.slotNo;
	var vmlPonPort = this.onuPonPort.ponNo;
	//---------解析ONU-INDEX-----------//
	var onuindex = this.onuIndex.toString(16);
	if (onuindex.length==9)
		onuindex = '0'.concat(onuindex);
	onuindex  = parseInt(onuindex.substring(4,6),16);
	var vmlPort = "@ONU.portNum@:" + vmlSlot + '/' + vmlPonPort + ':' + onuindex;
	var vmlLight1 = "@ONU.sendOptRate@:" + this.onuPonPort.onuTransPower + ' dBm';
	var vmlLight2 = "@ONU.recOptRate@:" + this.onuPonPort.onuRevPower + ' dBm';
	//----绘制参数信息-----//
	$vmlText = $("<div>");
	$vmlText.css("zindex",500000)
	//端口号
	$('<div id="vmlInterface"></div>').appendTo($vmlText);
	$('<div id="vmlSender"></div><br><div id="vmlReveicer"></div>').appendTo($vmlText);
	$vmlText.addClass("vmlTextClass").appendTo($("#PanelContainer"));
	//修改参数描述的位置信息
	$("#vmlInterface").css({
		position:'absolute', left:235,  top:30
	});
	$("#vmlSender").css({
		position:'absolute', left:295, top:52
	});
	$("#vmlReveicer").css({
		position:'absolute', left:295, top:70
	})
	$("#vmlInterface").html(vmlPort);
	$("#vmlSender").html(vmlLight1);
	$("#vmlReveicer").html(vmlLight2);
	this.text = $vmlText;
	this.text.port = $("#vmlPort");
	//光发射功率
	this.text.light1 = $("#vmlLight1");
	//光接受功率
	this.text.light2 = $("#vmlLight2");
}

/*****************************************
 				16 PORTS
 ****************************************/
OCEntity.prototype.drawOnu_8644 = function(){
	this.olt = this.createComponent(0, "oltPanel", 270, 32, 50, 50, IMG.OLTDEVICE, "PanelContainer")
	//draw onu(cmc),cmc也要当onu看,太扯淡了
	this.ui = this.createComponent(0, "onuDevice", -20, -30, 454, 54, IMG.ONUDEVICE, "entityContainer")
	//onu pon
	this.execute(function(){
		var ui = this.onuPonPort.ui = this.createComponent(1, "onuPon_0", 121, 20, 14, 16, "/epon/image/pon.png", "onuDevice");
	},{},null,function(){
		var ui = this.onuPonPort.ui = this.createComponent2(0, "onuPon_0" , 121, 20, 14, 16, "/epon/image/pon.png", "onuDevice");
	});
	var portIndex = -1;
	///UNI PORTS
	while(++portIndex < this.uniSize){
		var portRealIndex = portIndex +1;
		var leftStart = 198
		if((Math.floor(portIndex/2)) >=4 )leftStart = 205
		var top = 8
		if((portIndex+1)%2 == 0 )top = 25
		this.execute(function(){
			var uni = this.onuUniPortList[portIndex];
			if(!uni.collected){throw error();}
			uni.ui = this.createComponent(0, "onuUni_" + portRealIndex, leftStart + Math.floor(portIndex/2)  * 20, top, 14, 15, "/epon/image/console.png", "onuDevice");			
			this.changeUniState(uni.uniAdminStatus,uni.uniOperationStatus,"onuUni_" + portRealIndex);
		},{},null,function(){
			this.createComponent2(0, "onuUni_" + portRealIndex, leftStart + Math.floor(portIndex/2)  * 20, top, 14, 15, "/epon/image/console.png", "onuDevice");
		});
	};
	//DRAW LINE,NO SPLITER
	var color = this.onuOperationStatus==1?'green':'red';
	this.spliter = this.drawPolyLine(color,2,false,'290,68 140,68 140,207');
	//计算参数信息
	var vmlSlot = this.onuPonPort.slotNo;
	var vmlPonPort = this.onuPonPort.ponNo;
	//---------解析ONU-INDEX-----------//
	var onuindex = this.onuIndex.toString(16);
	if (onuindex.length==9)
		onuindex = '0'.concat(onuindex);
	onuindex  = parseInt(onuindex.substring(4,6),16);
	var vmlPort = "@ONU.portNum@:" + vmlSlot + '/' + vmlPonPort + ':' + onuindex;
	var vmlLight1 = "@ONU.sendOptRate@:" + this.onuPonPort.onuTransPower+ ' dBm';
	var vmlLight2 = "@ONU.recOptRate@:" + this.onuPonPort.onuRevPower + ' dBm';
	//----绘制参数信息-----//
	$vmlText = $("<div>");
	$vmlText.css("zindex",500000)
	//端口号
	$('<div id="vmlInterface"></div>').appendTo($vmlText);
	$('<div id="vmlSender"></div><br><div id="vmlReveicer"></div>').appendTo($vmlText);
	$vmlText.addClass("vmlTextClass").appendTo($("#PanelContainer"));
	//修改参数描述的位置信息
	$("#vmlInterface").css({
		position:'absolute', left:265,  top:28
	});
	$("#vmlSender").css({
		position:'absolute', left:150, top:52
	});
	$("#vmlReveicer").css({
		position:'absolute', left:150, top:70
	})
	$("#vmlInterface").html(vmlPort);
	$("#vmlSender").html(vmlLight1);
	$("#vmlReveicer").html(vmlLight2);
	this.text = $vmlText;
	this.text.port = $("#vmlPort");
	//光发射功率
	this.text.light1 = $("#vmlLight1");
	//光接受功率
	this.text.light2 = $("#vmlLight2");
}


/*****************************************
				24 PORT ONU
 ****************************************/
OCEntity.prototype.drawOnu_8647 = function(){
	this.olt = this.createComponent(0, "oltPanel", 270, 32, 50, 50, IMG.OLTDEVICE, "PanelContainer")
	//draw onu(cmc),cmc也要当onu看,太扯淡了
	this.ui = this.createComponent(0, "onuDevice", -20, -30, 454, 56, IMG.ONUDEVICE, "entityContainer")
	//onu pon
	this.onuPonPort.ui = this.createComponent(1, "onuPon_0", 124, 21, 14, 14, '/epon/image/pon.png', "onuDevice")	
	this.onuPonPort.ui.css("zoom",1.2)
	///UNI PORTS
	var portIndex = -1
	while(++portIndex < this.uniSize){ // 32 ports
		var portRealIndex = portIndex +1
		var leftStart = 170
		if(portIndex>-1 && portIndex < 4)leftStart  = 200
		if(portIndex>3  && portIndex < 8)leftStart  = 285
		if(portIndex>7  && portIndex < 12)leftStart = 370
		if(portIndex>11 && portIndex < 16)leftStart = 200
		if(portIndex>15 && portIndex < 20)leftStart = 285
		if(portIndex>19)leftStart = 370
		var top = 8
		if(portIndex>11)top = 28
		this.execute(function(){
			var uni = this.onuUniPortList[portIndex];
			if(!uni.collected)throw error("@ONU.drawError@")
			uni.ui = this.createComponent(0, "onuUni_" + portRealIndex,
						leftStart + portIndex%4 * 19, top, 16, 16, "/epon/image/console.png", "onuDevice",null,true);			
			this.changeUniState(uni.uniAdminStatus,uni.uniOperationStatus,"onuUni_" + portRealIndex);
		},{},null,function(){
			this.createComponent2(0, "onuUni_" + portRealIndex, 
						leftStart + portIndex%4 * 19, top, 16, 16, "/epon/image/console.png", "onuDevice",null,true);									
		});
	};
	//DRAW LINE,NO SPLITER
	var color = this.onuOperationStatus==1?'green':'red';
	this.spliter = this.drawPolyLine(color,2,false,'290,68 145,68 145,208');
	//计算参数信息
	var vmlSlot = this.onuPonPort.slotNo;
	var vmlPonPort = this.onuPonPort.ponNo;
	//---------解析ONU-INDEX-----------//
	var onuindex = this.onuIndex.toString(16);
	if (onuindex.length==9)
		onuindex = '0'.concat(onuindex);
	onuindex  = parseInt(onuindex.substring(4,6),16);
	var vmlPort = "@ONU.portNum@:" + vmlSlot + '/' + vmlPonPort + ':' + onuindex;
	var vmlLight1 = "@ONU.sendOptRate@:" + this.onuPonPort.onuTransPower + ' dBm';
	var vmlLight2 = "@ONU.recOptRate@:" + this.onuPonPort.onuRevPower + ' dBm';
	//----绘制参数信息-----//
	$vmlText = $("<div>");
	$vmlText.css("zindex",500000)
	//端口号
	$('<div id="vmlInterface"></div>').appendTo($vmlText);
	$('<div id="vmlSender"></div><br><div id="vmlReveicer"></div>').appendTo($vmlText);
	$vmlText.addClass("vmlTextClass").appendTo($("#PanelContainer"));
	//修改参数描述的位置信息
	$("#vmlInterface").css({
		position:'absolute', left:265,  top:28
	});
	$("#vmlSender").css({
		position:'absolute', left:150, top:52
	});
	$("#vmlReveicer").css({
		position:'absolute', left:150, top:70
	})
	$("#vmlInterface").html(vmlPort);
	$("#vmlSender").html(vmlLight1);
	$("#vmlReveicer").html(vmlLight2);
	this.text = $vmlText;
	this.text.port = $("#vmlPort");
	//光发射功率
	this.text.light1 = $("#vmlLight1");
	//光接受功率
	this.text.light2 = $("#vmlLight2");
}

/*****************************************
 * 拆分型CMC的绘制方法:与独立CMC页面的不一样
 ****************************************/
OCEntity.prototype.drawCMC_B = function(cdx){
	//var c = this.cmcList[cdx];
	this.createComponent(0, "oltPanel", 313, 32, 50, 50, IMG.ONUICON, "PanelContainer","onu")
	this.ui = this.createComponent(0, "cmc"+cdx, 0, 0, 451, 74, IMG.CMCDEVICE, "entityContainer")
	this.drawLine('313,55','100,55','black',null,true)
	this.drawLine('100,55','100,255','black',null,true)
	//----绘制参数信息-----//
	$vmlInterface = $('<div>')
	//---修改参数描述的位置信息---//
	$vmlInterface.attr({
		id : 'onuInterface'
	}).css({
		position:'absolute', left:320, top:80
	}).html(this.entityName)
	$vmlInterface.appendTo($("#PanelContainer"))
}

/************************************************
 * GENERAL
 * @portSize NumberOfPorts
 ***********************************************/
OCEntity.prototype.drawOnu_GENERAL = function(portSize){
	portSize = portSize || this.uniSize ;
	///DEVICE
	this.execute(function(){this.ui = this.createComponent(0, "onuDevice", 0, 0, 451, 74, IMG.ONUDEVICE, "entityContainer");});
	///PON PORT
	this.execute(function(){
		this.onuPonPort.ui = this.createComponent(1, "onuPon_0", 115, 23, 23, 23, IMG.PONIMG, "onuDevice");
	},{},null,function(){
		this.onuPonPort.ui = this.createComponent2(0, "onuPon_0" , 115, 23, 23, 23, IMG.PONIMG, "onuDevice");
	})
	var portIndex = -1;
	///UNI PORTS
	while(++portIndex < portSize){
		var portRealIndex = portIndex +1;
		this.execute(function(){
			if(!this.onuUniPortList[portIndex].collected){throw error();}
			this.onuUniPortList[portIndex].ui = this.createComponent(0, "onuUni_" + portRealIndex, 178 + portIndex * 37, 23, 23, 23, IMG.UNIIMG, "onuDevice");
			var uni = this.onuUniPortList[portIndex];
			this.changeUniState(uni.uniAdminStatus,uni.uniOperationStatus,"onuUni_" + portRealIndex);
		},{},null,function(){
			//可能导致下标越界
			/*this.onuUniPortList[portIndex].*/
			var ui = this.createComponent2(0, "onuUni_" + portRealIndex, 178 + portIndex * 37, 23, 23, 23, IMG.UNIIMG, "onuDevice");
		});
	};
	//DRAW  SPLITER & UPDOWN LINE
	$spliter = $("<div class='spliterClass' onuOperationStatus=2></div>");
	$spliter.appendTo($("#PanelContainer"));
	//$("<img id='onuUpDown' class='updownClass' src='image/onu/spliter.gif' width='177px' height='199px></img>").appendTo($spliter);
	$updown = $("<img>")
	$updown.attr({
		src:'/epon/image/onu/spliter.gif',
		id:'onuUpDown', width: 177, height:199
	}).css({
		position: 'absolute',
		left: -10,  top: -12
	}).addClass('updownClass').appendTo($spliter);
	//draw OLT icon
	this.execute(function(){this.olt = this.createComponent(0, "oltPanel", 313, 32, 50, 50, oltImg, "PanelContainer");});
	//计算参数信息
	var vmlSlot = this.onuPonPort.slotNo;
	var vmlPonPort = this.onuPonPort.ponNo;
	//---------解析ONU-INDEX-----------//
	var onuindex = this.onuIndex.toString(16);
	if (onuindex.length==9)
		onuindex = '0'.concat(onuindex);
	onuindex  = parseInt(onuindex.substring(4,6),16);
	var vmlPort = "@ONU.portNum@:" + vmlSlot + '/' + vmlPonPort + ':' + onuindex;
	var vmlLight1 = "@ONU.sendOptRate@:" + this.onuPonPort.onuTransPower + ' dBm';
	var vmlLight2 = "@ONU.recOptRate@:" + this.onuPonPort.onuRevPower + ' dBm';
	var vmlLight3  = "@ONU.ponport@@ONU.sendOptRate@:" + (this.oltPonOptical.transPower).toFixed(2)+ ' dBm';
	//----绘制参数信息-----//
	$vmlText = $("<v:TextBox></v:TextBox>");
	//端口号
	$('<div id="vmlInterface"></div>').appendTo($vmlText);
	//PON端口发送光功率
	$('<div id="vmlInterfaceSend"></div>').appendTo($vmlText);
	$('<div id="vmlSender"></div><br><div id="vmlReveicer"></div>').appendTo($vmlText);
	$vmlText.addClass("vmlTextClass").appendTo($("#PanelContainer"));
	//修改参数描述的位置信息
	$("#vmlInterface").css({
		position:'absolute',left:245,top:44
	});
	$("#vmlInterfaceSend").css({
		position:'absolute', left:245,  top:80
	});
	$("#vmlSender").css({
		position:'absolute',left:165,top:170
	});
	$("#vmlReveicer").css({
		position:'absolute',left:165,top:190
	})
	$("#vmlInterface").html(vmlPort);
	$("#vmlSender").html(vmlLight1);
	$("#vmlReveicer").html(vmlLight2);
	$("#vmlInterfaceSend").html(vmlLight3);
	this.spliter = $spliter;
	this.text = $vmlText;
	this.text.port = $("#vmlPort");
	//光发射功率
	this.text.light1 = $("#vmlLight1");
	//光接受功率
	this.text.light2 = $("#vmlLight2");
}

/************************************
 		绘制未知类型ONU
 ***********************************/
OCEntity.prototype.drawUnknown = function(){
	var $entityContainer = $("#entityContainer");
	var tableContainer = DOC.createElement("div")
	tableContainer.style.position = 'absolute'
	tableContainer.style.top = "-200px"
	tableContainer.style.width = 500
	tableContainer.style.height = 300
	$entityContainer.append(tableContainer)
	
	var table = DOC.createElement("table")
	table.width = '100%'
	tableContainer.appendChild(table)
	this.createUpperHead(table)
	this.createUniTable(table)
	if(this.cmcEntity){
		this.createCcmtsTable(table)
	}
}

OCEntity.prototype.createCcmtsTable = function(table){
	var tr = table.insertRow()
		tr.style.height  = '45px'
		var	td = tr.insertCell()
		td.colSpan = 4
		td.innerHTML = '<b>CCMTS:</b>'
	var cmcImage = '/epon/image/cmc/icmc-a.png';
	this.ui = this.createComponent(0, "cmcDevice", 100, 0, 280, 190, cmcImage, "entityContainer");
}

/**********************************************
tool method:draw line
*********************************************/
OCEntity.prototype.drawLine= function(from,to,color,strike,dash){
	var connector = DOC.createElement('v:Line')
	connector.style.zIndex = 10000
	connector.style.position = 'absolute'
	connector.from = from
	connector.to = to
	connector.strokecolor = color || 'black'
	connector.strokeweight =  strike || 1
	if(dash){
	var stroke = DOC.createElement("v:stroke")
	stroke.dashstyle = 'Dash'
	connector.appendChild(stroke)
	}
	$("#PanelContainer").append(connector)
}
/**********************************************
tool method:draw PolyLine
*********************************************/
OCEntity.prototype.drawPolyLine= function(color,strike,dash,points){
	var div = document.createElement("div"),
	    div2 = document.createElement("div");
	if(arguments.length === 5){//如果传了5个参数过来，那么线画在右边;
		if( color ="green"){//绿色
			div.className = "blueLineB_1";
			div2.className = "blueLineB_2";
		}else{//红色
			div.className = "redLineB_1";
			div2.className = "redLineB_2";		
		}
	}else{
		if( color ="green"){//绿色
			div.className = "blueLineA_1";
			div2.className = "blueLineA_2";
		}else{//红色
			div.className = "redLineA_1";
			div2.className = "redLineA_2";		
		}
	}
	
	$("#PanelContainer").append(div);
	$("#PanelContainer").append(div2);
	return ;
	/*var connector = DOC.createElement('v:PolyLine')
	connector.style.zIndex = 10000
	connector.style.position = 'absolute'
	connector.strokecolor = color || 'black'
	connector.Points = points
	connector.filled = false
	connector.strokeweight =  strike || 1
	connector.onuOperationStatus = entity.onuOperationStatus
	if(dash){
	var stroke = DOC.createElement("v:stroke")
	stroke.dashstyle = 'Dash'
	connector.appendChild(stroke)
	}
	$("#PanelContainer").append(connector)
	return connector*/
}


OCEntity.prototype.createUpperHead = function(table){
	var vmlSlot = this.onuPonPort.slotNo;
	var vmlPonPort = this.onuPonPort.ponNo;
	//---------解析ONU-INDEX-----------//
	var onuindex = this.onuIndex.toString(16);
	if (onuindex.length==9)
		onuindex = '0'.concat(onuindex);
	onuindex  = parseInt(onuindex.substring(4,6),16);
	var vmlPort =  vmlSlot + '/' + vmlPonPort + ':' + onuindex;
	//--------------FIRST ROW----------------//
	var tr = table.insertRow()
		var tb = tr.insertCell()
			tb.style.width = 45
			tb.innerHTML = "<b>@ONU.uplinkPort@:</b>"
		tb = tr.insertCell()
			tb.style.width = 100
			tb.innerHTML = vmlPort
	tr = table.insertRow()
		tb = tr.insertCell()
			tb.style.width = 45
			tb.innerHTML = "<b>@ONU.curEntity@:</b>"
		tb = tr.insertCell()
			tb.style.width = 45
			var cell = DOC.createElement("div")
			cell.id = "onuDevice"
			cell.style.width = '100'
			//cell.style.overflow = 'auto'
			cell.style.align = 'absmiddle'
			cell.style.border = 'transparent 2px solid'
			
			cell.className = 'onuClass'
			
			var img = DOC.createElement("img")
			img.src = '/epon/image/onu/8624Icon_32.png'
			img.style.marginLeft = 30
			cell.appendChild(img)
			
			var desc = DOC.createElement("div")
			desc.className = 'descClass'
			desc.style.backgroundColor = "#B3C8E8"
			desc.style.textAlign = "center"
			desc.innerText = this.entityName == "" ? this.onuMac : this.entityName
			desc.onselectstart = function(){return false}
	
			cell.appendChild(desc)
			tb.appendChild(cell)
			this.ui = $(cell)
	//-----------SECOND ROW--------//			
		tr.style.height  = 20
		tb = tr.insertCell()
			tb.style.width = 45
			tb.innerHTML = '<b>PON@COMMON.port@:</b>'
		tb = tr.insertCell()
			tb.style.width = 100
			var cell = DOC.createElement("div")
			cell.id = "onuPon_0"
			cell.style.width = 54
			cell.style.height = 48
			cell.style.align = 'absmiddle'
			cell.style.border = 'transparent 2px solid'
				
			cell.className = 'ponPortClass port'
				
			var img = DOC.createElement("img")
			img.src = IMG.PONIMG
			img.style.margin = "0 0 2 12"
			cell.appendChild(img)
			
			var desc = DOC.createElement("div")
			desc.className = 'descClass'
			desc.style.backgroundColor = "#B3C8E8"
			desc.style.textAlign = "center"
			desc.innerHTML = "pon"
			desc.onselectstart = function(){return false}
	
			cell.appendChild(desc)
			tb.appendChild(cell)
}

OCEntity.prototype.createUniTable = function(table){
	var tr = table.insertRow()
		tr.style.height  = '45px'
		var	td = tr.insertCell()
			td.colSpan = 4
			td.innerHTML = '<b>UNI@COMMON.port@:</b>'
	tr = table.insertRow()
		td = tr.insertCell()
		td.colSpan = 4
		var $table = DOC.createElement("table")
		$table.style.width = 450
		td.appendChild( $table )
		var $tr
		var $rowCounter = 0
		var $columnCounter = 0
		for(var i=0;i<this.uniSize;i++){
			if(i % 7 == 0){
				$tr = $table.insertRow($rowCounter++)
				$columnCounter = 0
			}
			var $td = $tr.insertCell($columnCounter++)
			this.makeUniCell(this.onuUniPortList[i], $td ,i)
		}
}

OCEntity.prototype.makeUniCell = function(uni,$td,portIndex){
	//var cell = DOC.createElement("div")
	$td.className = 'uniPortClass port'
	$td.style.margin = 5
	$td.id = "onuUni_"+(portIndex+1)
	$td.style.width = "44px"
	$td.style.height = "44px"
	$td.style.border = 'transparent 2px solid'
		
	var img = DOC.createElement("img")
	img.src = '/epon/image/uni_hover.png'
	img.style.display = 'block'
	img.style.marginLeft = 15
	$td.appendChild(img)
	
	var desc = DOC.createElement("div")
	desc.className = 'descClass'
	desc.style.backgroundColor = "#B3C8E8"
	desc.style.textAlign = "center"
	desc.innerHTML = portIndex+1
	desc.onselectstart = function(){return false}
	
	$td.appendChild(desc)
	//$td.appendChild(cell)
}

/**************************************************
 * 切换ONU----CMC**
 * @cateogry : CMC ONU more?
 * @cdx: if category is cmc ,cdx is cmc index;
 **************************************************/
OCEntity.prototype.showDevice = function(category,cdx){
	//清掉所有的数据
	$cache = $("#entityContainer").remove().empty();
	$("#PanelContainer").empty().append($cache);
	switch(category){
	//展示ONU
		case CATEGORY.ONU :
			//绘制ONU
			this.drawOnu_GENERAL();
			//$("#entityType").html(this.onuPreType)
			break;
			//绘制CMC
			//展示CMC
		case CATEGORY.CMC :
			//$("#entityType").html("@ONU.cc8800bName@")
			this.drawCMC_B(cdx)
			break
	}
	this.startListen()
	
}

/**********************************
  		为其统一添加监听
 *********************************/
OCEntity.prototype.startListen = function(){
	//如果ONU面板存在,则添加事件。当为未知类型时,不添加此事件
	$("#onuDevice,#cmcDevice").bind('mouseover', function(e) {
		if('yellow 2px solid' != this.style.border)
			if('#00EEEE 2px solid' != this.style.border)
				$(this).css('border', '2px solid #00EEEE')
	}).bind('mouseout', function(e) {
		if('yellow 2px solid' != this.style.border)
			$(this).css('border' , '2px solid transparent')
	}).bind('contextmenu click', function(e,ex) {
		//如果实体没有被点击过。
		if(!cascadeFlag) {
			cascadeFlag = true;
			try {
				page.entity.entityContextmenu(this,e);
				switch(this.id.substring(0,3)){
					case CATEGORY.CMC:
						showCC8800AService(e);
						break;
					case CATEGORY.ONU:
						if(EntityType.isCcmtsType(entity.entityType) ){
						    //showCC8800AService(e);
						}else{
						    showOnuService(e);
						}
						break;
				}
			} catch(e) {}
			cascadeFlag = false;
		}
	})
	
	//-----------------为UNI口添加右键事件-------------//
	$(".uniPortClass").bind('contextmenu click', function(e) {
		//如果实体没有被点击过。
		if(!cascadeFlag) {
			cascadeFlag = true;
			try {
			    page.entity.entityContextmenu(this,e);
			    showUniService(e);
			} catch(e) {}
			cascadeFlag = false
		}
	})
	
	//-----------------为COM口添加右键事件-------------//
	$(".comPortClass").bind('contextmenu click', function(e) {
		//如果实体没有被点击过。
		if(!cascadeFlag) {
			cascadeFlag = true;
			try {
				page.entity.entityContextmenu(this,e);
				showComService(e);
			} catch(e) {}
			cascadeFlag = false
		}
	})
	
	//------------为PON口添加右键----------------------//
	$(".ponPortClass").bind('contextmenu click', function(e) {
		//如果实体没有被点击过。
		if(!cascadeFlag) {
			cascadeFlag = true;
			try {
				page.entity.entityContextmenu(this,e);
				showOnuPonService(e);
			} catch(e) {}
			cascadeFlag = false;
		}
	});
	//---------将ODN在ONU区域的事件转移到ONU面板图上---------//
	$("#onuUpDown").click(function(e){
		var X = e.pageX;
		var Y = e.pageY;
		//----------计算ONU面板的区域------//
		var oX = $("#onuDevice").offset().left;
		var oY = $("#onuDevice").offset().top;
		var width = parseInt($("#onuDevice").css("width").split("px")[0]);
		var height = parseInt($("#onuDevice").css("height").split("px")[0]);
		if (X>oX && X<oX+width && Y>oY && Y<oY+height)
			$("#onuDevice").trigger("click",[e,e]);
		else
			return false;
	});
	$("#onuUpDown").bind('contextmenu',function(e){
		e.preventDefault();
		preventBubble(e);
		var X = e.pageX;
		var Y = e.pageY;
		//----------计算ONU面板的区域------//
		var oX = $("#onuDevice").offset().left;
		var oY = $("#onuDevice").offset().top;
		var width = parseInt($("#onuDevice").css("width").split("px")[0]);
		var height = parseInt($("#onuDevice").css("height").split("px")[0]);
		if (X>oX && X<oX+width && Y>oY && Y<oY+height)
			$("#onuDevice").trigger("contextmenu",[e,e]);
		else
			return false;
		return false;
	});
	$.each($('.port'), function(i, div) {
		//--------------- BIND MOUSEOVER EVENT-------------//
		$(div).bind('mouseover', function(e) {
			if(this.style.borderColor == 'yellow')
				return
			if(entity.uniStyle == '16*16')
				$(this).css('border','1px solid #00EEEE')
			else
				$(this).css('border','2px solid #00EEEE')
		});
		//--------------- BIND MOUSEOUT EVENT-------------//
		$(div).bind('mouseout', function(e) {
			/*while(e.target.tagName != 'DIV'){
				e.target = e.target.parentNode
			}*/
			if(entity.uniStyle == '16*16'){
				// border 的格式就是 color width - solid
				if(this.style.borderColor != 'yellow'){
					if(this.id.substring(0,6) == 'onuUni')
						this.style.border = '1px solid transparent'
					else
						this.style.border = '1px solid transparent'
				}
			}else{
				if(this.style.borderColor != 'yellow')
					this.style.border = '2px solid transparent'
			}
		})
		//--------------- BIND CLICK EVENT-------------//
		$(div).bind('click', function(e) {
			var arr = this.id.split('_')
			//如果实体没有被点击过。
			if(!cascadeFlag) {
				cascadeFlag = true
				try {
					//并记录本次div 的id,放入divCache中
					page.entity.onEntityClick(this,e)
				} catch(e) {}
				cascadeFlag = false
			}
		})
	})
}


OCEntity.prototype.changeUniState = function(as,os,el){
	$("#"+el).empty()
	var img_hover = "url(/epon/image/uni_hover.png)"
	var img_link = "url(/epon/image/uni.gif)"
	var size = 20
	if(this.uniStyle == "16*16"){
		img_hover = "url(/epon/image/console.png)"
		img_link = "url(/epon/image/console.gif)"
		size = 16
		return;
	}
	if(as == 1 && os == 1){
		$("#"+el).css('backgroundImage',img_hover)
	}else if(as == 1 && os == 2) {
		$("#"+el).css('backgroundImage',img_link)
	}else{
		$("#"+el).css('backgroundImage',img_hover)
		$state = $("<img>")
		$state.attr({
			width : size,height : size,
			title : "@ONU.portNoService@"
		})
		$state.addClass("portStateClass")
		$state.css({
			position : 'absolute',
			width : 20, height : 20,
			top : 3,left : 3,
			opacity : 1,
			zIndex : 15000
		})
		$state.attr({
			src : '/epon/image/close.gif'
		 })
		 // 在板卡上添加端口状态
		$("#"+el).append($state)	
	}
	
	
}

/************************************************
 			 展示ONU的上下线信息
 ***********************************************/
OCEntity.prototype.showStatus = function(){
	//add by brain@20150716:切换树中状态的显示
	if(this.onuOperationStatus == 1){
		$("A[mac='"+this.onuMac+"']").addClass("onlineLink").removeClass("offlineLink");
	}else{
		$("A[mac='"+this.onuMac+"']").removeClass("onlineLink").addClass("offlineLink");
	}
	if(this.onuOperationStatus == 1){ //1
		this.spliter[0].onuOperationStatus = 1;
	}else{	
		//2,null,undefined,**
		this.spliter[0].onuOperationStatus = 2;
	}
	//------------判定上下线--------------//
	if(entity.onuOperationStatus == 1){
		if(EntityType.isCcmtsType(this.entityType)){
			$("#onuUpDown").attr('src','/epon/image/onu/spliter.gif')
			$("#onuDevice").attr('title', "@ONU.alreadyOnline@")
		}else{
			entity.spliter[0].strokecolor = "green"
		}
	}else{
		if(!EntityType.isCcmtsType(this.entityType)){
			$("#onuUpDown").attr('src','/epon/image/onu/spliterDown.gif')
			$("#onuDevice").attr('title', "@ONU.alreadyOffline@")
		}else{
			entity.spliter[0].strokecolor = "red"
		}
	}
}

OCEntity.prototype.showStatusFailure = function(){}

/********************************************************************
 * entity销毁方法,page中每次调用changeEntity的时候会首先执行此方法
 *******************************************************************/
OCEntity.prototype.destory = function(){
	//-----附加信息删除----//
	//----设备删除:如果绘制失败的话 entity对象有,但ui对象却没有,所以也需要做判定-----//
	this.ui && this.ui.remove();//可以全部删除,不不需要上面的步骤
	this.prototype = {};
}

/**
 * display component attribute
 * @nodeId component ID
 */
OCEntity.prototype._showComponentProperty = function(nodeId) {
    clearTimeout(this.showComponentPropertyDelay);
    this.showComponentPropertyDelay = false;
    var entityStore = null;
    if(nodeId != 'oltPanel') {
        if(nodeId.substring(3, 6).toLowerCase() == 'pon') {
            var tmp = [this.onuIndex + 255];
            for(var a=0; a<oltPortOptical.length; a++){
                if(tmp[0] == oltPortOptical[a][0]){
                    tmp = oltPortOptical[a];
                    break;
                }
            }
            var tmpC = ['', " @{unitConfigConstant.tempUnit}@", " @COMMON.dBm@", " @COMMON.dBm@", ' mA', ' V'];
            for(a=1; a<5; a++){
                tmp[a] = tmp[a] && tmp[a] != 0 ? (parseInt(tmp[a]) / 100) + tmpC[a] : "@Optical.couldntGetData@";
            }
            tmp[5] = tmp[5] && tmp[5] != 0 ? (parseInt(tmp[5]) / 100000) + tmpC[5] : "@Optical.couldntGetData@";
            entityStore = { 
                '@ONU.tempreture@' : tmp[1],
                '@ONU.recOptRate@' : tmp[2],
                '@ONU.sendOptRate@' : tmp[3],
                '@SUPPLY.biasCurrent@' : tmp[4],
                '@ONU.voltage@' : tmp[5]
            }
            loadOnuPonOptical(this.onuIndex + 255);
        } else if(nodeId.substring(3, 6).toLowerCase() == 'uni') {
            var uniIndex = nodeId.substring(7, 8);
            var tmpMode2 = this.onuUniPortList[uniIndex - 1].uniAutoNegoString;
            var tmpMode = this.onuUniPortList[uniIndex - 1].uniAutoNegotiationEnable == 1 ? null : tmpMode2;
            entityStore = {
                '@ONU.connectStatus@':likeStatus[this.onuUniPortList[uniIndex - 1].uniOperationStatus],
                '@ONU.unistatus@' : Enable[this.onuUniPortList[uniIndex - 1].uniAdminStatus],
                '@ONU.autonegEnable@' : Enable[this.onuUniPortList[uniIndex - 1].uniAutoNegotiationEnable],
                '@ONU.workMode@ ': tmpMode,
                '@ONU.flowcontrolEnable@' : Enable[this.onuUniPortList[uniIndex - 1].flowCtrl]
            }
            if(parseInt(this.entityType) > 80 && parseInt(this.entityType) < 85){
                entityStore["@ONU.uniDSLoopBackEnable@"] = 
                    Enable[this.onuUniPortList[uniIndex - 1].uniDSLoopBackEnable] || null;
                entityStore["@ONU.uniUSUtgPriSimple@"] = 
                    (this.onuUniPortList[uniIndex - 1].uniUSUtgPri == 255 ? 
                    "@ONU.noPri@" : this.onuUniPortList[uniIndex - 1].uniUSUtgPri);
            }
        }else if(nodeId.toLowerCase() == 'onudevice') {
            //计算参数信息
            var vmlSlot = this.onuPonPort.slotNo;
            var vmlPonPort = this.onuPonPort.ponNo;
            //---------解析ONU-INDEX-----------//
            var onuindex = this.onuIndex.toString(16);
            if (onuindex.length==9)
                onuindex = '0'.concat(onuindex);
            onuindex  = parseInt(onuindex.substring(4,6),16);
            var vmlPort = vmlSlot + '/' + vmlPonPort + ':' + onuindex;
            //在线时长
            var upTime = 0;
            if(oltSoftVersion.indexOf("1.6.9.9-p4") != -1 && !EntityType.isCcmtsType(this.entityType) ){
                upTime = arrive_timer_format(this.onuTimeSinceLastRegister*100 + parseInt(((new Date()).getTime() - this.changeTime.time)/1000));
            }else{
            	if(!EntityType.isCcmtsType(this.entityType)){
            		upTime = arrive_timer_format(this.onuTimeSinceLastRegister + parseInt(((new Date()).getTime() - this.changeTime.time)/1000));
            	}else{
            		upTime = arrive_timer_format( this.cmcList[0].sysUpTime/1000 );
            	}
            }
            switch(this.onuOperationStatus){
            	case 4:
                case 1://on
                    entityStore = {
                        '@RESOURCES/COMMON.alias@' : this.entityName,
                        '@EPON.entityDeviceName@' : this.sysName,
                        '@EPON.entityAddr@' : this.sysLocation,
                        '@ONU.productType@' : this.typeName,
                        '@ONU.macAddr@' : this.onuMac,
                        '@ONU.onuLocation@' : vmlPort,
                        '@ONU.onlineStatus@' : onuStatus[this.onuOperationStatus],
                        '@ONU.mgmtstatus@' : Enable[this.onuAdminStatus],
                        '@ONU.chipvender@' : convert2Bit(this.onuChipVendor),
                        '@ONU.chipType@' : convert2Bit(this.onuChipType),
                        '@ONU.chipVersion@' : "0X"+this.onuChipVersion.toUpperCase(),
                        '@ONU.testDistance@' : this.onuTestDistance + "m",
                        "LLID" : convert2Bit(this.onuLlidId),
                        '@ONU.substainOnline@' : upTime,
                        '@COMMON.TMP@' : (this.topOnuCurrentTemperature==127 || entity.temperatureDetectEnable==2) ? null : 
                        	this.topOnuCurrentTemperature == 255 ? "-" : (isNaN(this.topOnuCurrentTemperature) ? this.topOnuCurrentTemperature : (this.onuDisplayTemp+" "+ "@{unitConfigConstant.tempUnit}@"))
                    }
                    break
                default:
                    entityStore = {
                        '@RESOURCES/COMMON.alias@' : this.entityName,
                        '@ONU.productType@' : this.typeName,
                        '@ONU.macAddr@' : this.onuMac,
                        '@ONU.onuLocation@' : vmlPort,
                        '@ONU.onlineStatus@' : onuStatus[this.onuOperationStatus],
                        '@ONU.mgmtstatus@' : Enable[this.onuAdminStatus],
                        '@ONU.chipvender@'  : convert2Bit(this.onuChipVendor),
                        '@ONU.chipType@': convert2Bit(this.onuChipType),
                        '@ONU.chipVersion@' : "0X"+this.onuChipVersion.toUpperCase(),
                        "LLID" : convert2Bit(this.onuLlidId),
                        '@COMMON.TMP@' : (this.topOnuCurrentTemperature==127 || entity.temperatureDetectEnable==2) ? null : 
                        	this.topOnuCurrentTemperature == 255 ? "-" : (isNaN(this.topOnuCurrentTemperature) ? this.topOnuCurrentTemperature : (this.onuDisplayTemp+" "+ "@{unitConfigConstant.tempUnit}@"))                    
                    }
            }
        }
        page.grid.setSource(entityStore);
    }
}