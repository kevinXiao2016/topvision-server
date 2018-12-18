function getGeuaTop(index) {
    if(1 == index){
        return coordinateParam.geuaConsole;
    } else if (2 == index) {
		return coordinateParam.geuaMgmt;
	} else if (2 < index && 7 > index) {
		return coordinateParam.geuaSniE + (index-3)*coordinateParam.geuaSniGapE;
	} else if (6 < index && index < 11) {
		if(index % 2 != 0 ) {
			return coordinateParam.geuaSniO + parseInt((index-7)/2)*coordinateParam.geuaSniGapO2;
		} else {
			return coordinateParam.geuaSniO + parseInt((index-7)/2)*coordinateParam.geuaSniGapO2 + coordinateParam.geuaSniGapO1;
		}
	} else {}
}
function getEpuaTop(index) {
    if (index % 2 != 0) {
        return coordinateParam.epuaFirstPon + parseInt((index - 1) / 2)* coordinateParam.epuaMargin1;
    } else {
        return coordinateParam.epuaFirstPon + parseInt((index - 1) / 2)* coordinateParam.epuaMargin1 + coordinateParam.epuaMargin2;
    }
}

function getVpuaTop(index){
	if (index % 2 != 0) {
        return coordinateParam.vpuaFirstPon + parseInt((index - 1) / 2)* coordinateParam.vpuaMargin1;
    } else {
        return coordinateParam.vpuaFirstPon + parseInt((index - 1) / 2)* coordinateParam.vpuaMargin1 + coordinateParam.vpuaMargin2;
    }
}

function getMpuaTop(index) {
	if (0 < index && index < 5) {
		return coordinateParam.mpuaSniE + (index - 1) * coordinateParam.mpuaSniGap;
	} else if (5 == index) {
		return coordinateParam.mpuaConsole;
	} else if (6 == index) {
		return coordinateParam.mpuaMgmt;
	} else {}
}
function getXguaTop(index) {
	if(1==index){
		return coordinateParam.xguaX01;
	}else if(2==index){
		return coordinateParam.xguaX02;
	}else{}
}
function getXgubTop(index) {
	if(1==index){
		return coordinateParam.xgubX01;
	}else if(2==index){
		return coordinateParam.xgubX02;
	}else if(3==index){
		return coordinateParam.xgubX03;
	}else if(4==index){
		return coordinateParam.xgubX04;
	}
}

function getXgucTop(index) {
	if(1==index){
		return coordinateParam.xgucX01;
	}else if(2==index){
		return coordinateParam.xgucX02;
	}else if(3==index){
		return coordinateParam.xgucX03;
	}else if(4==index){
		return coordinateParam.xgucX04;
	}else if(5==index){
		return coordinateParam.xgucX05;
	}else if(6==index){
		return coordinateParam.xgucX06;
	}
}

/**********************************
		8602上没有MPUA板
 * @param i
 ***********************************/
function createMpuaBoard(i) {}
function createMpubBoard(i) {}

/***********************************************
		进入这个方法就不会又preType == 0 的情况
 ***********************************************/
function createEpuaBoard(i) {
	var slot = olt.slotList[i - 1];
	$board = createBoard(slot.slotRealIndex, bType[slot.topSysBdActualType],"epua");
	for(var j = 1; j < 8 + 1; j++){
    	try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getEpuaTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
    	}catch(exp){
    		createPort2(slot.slotRealIndex,j,'geEpon',getEpuaTop(j),bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }
    }
}
function createEpubBoard(i) {
	var slot = olt.slotList[i - 1];
	$board = createBoard(slot.slotRealIndex, bType[slot.topSysBdActualType],bType[slot.topSysBdPreConfigType]);
	for(var j = 1; j < 8 + 1; j++){
    	try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getEpuaTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
    	}catch(exp){
    		createPort2(slot.slotRealIndex,j,'geEpon',getEpuaTop(j),bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }
    }
}
function createEpucBoard(i){
	var slot = olt.slotList[i - 1];
	$board = createBoard(slot.slotRealIndex, bActualType[slot.topSysBdActualType],bType[slot.topSysBdPreConfigType]);
	for(var j = 1; j < 16 + 1; j++){ 
    	try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getEpucTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
    	}catch(exp){
    		createPort2(slot.slotRealIndex,j,'geEpon',getEpucTop(j),bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }
    }
}
function getEpucTop(index){
	if (index % 2 != 0) {
        return coordinateParam.vpuaFirstPon + parseInt((index - 1) / 2)* coordinateParam.vpuaMargin1;
    } else {
        return coordinateParam.vpuaFirstPon + parseInt((index - 1) / 2)* coordinateParam.vpuaMargin1 + coordinateParam.vpuaMargin2;
    }
}

function createGPUABoard(i) {
	var slot = olt.slotList[i - 1];
	$board = createBoard(slot.slotRealIndex, bActualType[slot.type],bType[slot.topSysBdPreConfigType]);
	for(var j = 1; j < 16 + 1; j++){
    	try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getVpuaTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
    	}catch(exp){
    		createPort2(slot.slotRealIndex,j,'gpon',getVpuaTop(j),bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }
    }
}

function createGeuaBoard(i) {
	var slot = olt.slotList[i - 1];
	var Top = coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin+coordinateParam.portTopMargin;
    $board = createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType]);
    $port = $("<div>");
	$port.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_CONSOLE",
		title : I18N.EPON.portType + ':CONSOLE' +  I18N.EPON.portNum + ': 1'
	});
	$port.addClass("consoleClass");
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : Top,
		left : 	getGeuaTop(1)+coordinateParam.slotLeftStart,
		backgroundImage : 'url(/epon/image/console.png)',	
		backgroundRepeat : 'no-repeat', 
		opacity : 0.1,
		border : '1px solid transparent',
		zIndex : 1500
	});
	$("#OLT").append($port);
	
	$port2 = $("<div>");
	$port2.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_MGMT",
		title : I18N.EPON.portType + ':MGMT' +  I18N.EPON.portNum + ': 2'
	});
	$port2.addClass("consoleClass mgmtClass");
	$port2.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : Top,
		left : getGeuaTop(2)+coordinateParam.slotLeftStart,
		backgroundImage : 'url(/epon/image/mgmt.png)',	
		backgroundRepeat : 'no-repeat',
		border : '1px solid transparent', 
		opacity : 0.1,
		zIndex : 1500
	});
	$("#OLT").append($port2);	
	
   for (var j = 1; j < 8 + 1; j++) {
        try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getGeuaTop(port.portRealIndex+2), bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }catch(e){
        	if(j<5){
        		createPort2(slot.slotRealIndex, j, 'geCopper', getGeuaTop(j+2), bType[slot.type],bType[slot.topSysBdPreConfigType]);
        	}else{
        		createPort2(slot.slotRealIndex, j, 'geFiber', getGeuaTop(j+2), bType[slot.type],bType[slot.topSysBdPreConfigType]);
        	}				
        }
    }
}

function createGeubBoard(i) {
	var slot = olt.slotList[i - 1];
	var Top = coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin+coordinateParam.portTopMargin;
    $board = createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType]);
    $port = $("<div>");
	$port.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_CONSOLE",
		title : I18N.EPON.portType + ':CONSOLE' +  I18N.EPON.portNum + ': 1'
	});
	$port.addClass("consoleClass");
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : Top,
		left : 	getGeuaTop(1)+coordinateParam.slotLeftStart,
		backgroundImage : 'url(/epon/image/console.png)',	
		backgroundRepeat : 'no-repeat', 
		opacity : 0.1,
		border : '1px solid transparent',
		zIndex : 1500
	});
	$("#OLT").append($port);
	
	$port2 = $("<div>");
	$port2.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_MGMT",
		title : I18N.EPON.portType + ':MGMT' +  I18N.EPON.portNum + ': 2'
	});
	$port2.addClass("consoleClass mgmtClass");
	$port2.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : Top,
		left : getGeuaTop(2)+coordinateParam.slotLeftStart,
		backgroundImage : 'url(/epon/image/mgmt.png)',	
		backgroundRepeat : 'no-repeat',
		border : '1px solid transparent', 
		opacity : 0.1,
		zIndex : 1500
	});
	$("#OLT").append($port2);	
	
   for (var j = 1; j < 8 + 1; j++) {
        try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getGeuaTop(port.portRealIndex+2), bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }catch(e){}
    }
}


function createXguaBoard(i) {
	var slot = olt.slotList[i - 1];
	createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType]);
	for(var j = 1; j < 2 + 1; j++){
	    try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getXguaTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
	    }catch(exp){
	    	var slot = olt.slotList[i - 1];
	    	createPort2(slot.slotRealIndex, j, 'xeFiber', getXguaTop(j), bType[slot.type],bType[slot.topSysBdPreConfigType])
        }
    }
}
function createXgubBoard(i) {
	var slot = olt.slotList[i - 1];
	createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType]);
	for(var j = 1; j < 2 + 1; j++){
	    try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getXgubTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
	    }catch(exp){
	    	var slot = olt.slotList[i - 1];
	    	createPort2(slot.slotRealIndex, j, 'xeFiber', getXgubTop(j), bType[slot.type],bType[slot.topSysBdPreConfigType])
        }
    }
}
function createXgucBoard(i) {
	var slot = olt.slotList[i - 1];
	var Top = coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin+coordinateParam.portTopMargin;
    $board = createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType]);
    $port = $("<div>");
	$port.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_CONSOLE",
		title : I18N.EPON.portType + ':CONSOLE' +  I18N.EPON.portNum + ': 1'
	});
	$port.addClass("consoleClass");
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : Top,
		left : 	getGeuaTop(1)+45,
		backgroundImage : 'url(/epon/image/console.png)',	
		backgroundRepeat : 'no-repeat', 
		opacity : 0.1,
		border : '1px solid transparent',
		zIndex : 1500
	});
	$("#OLT").append($port);
	
	$port2 = $("<div>");
	$port2.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_MGMT",
		title : I18N.EPON.portType + ':MGMT' +  I18N.EPON.portNum + ': 2'
	});
	$port2.addClass("consoleClass mgmtClass");
	$port2.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : Top,
		left : getGeuaTop(2)+45,
		backgroundImage : 'url(/epon/image/mgmt.png)',	
		backgroundRepeat : 'no-repeat',
		border : '1px solid transparent', 
		opacity : 0.1,
		zIndex : 1500
	});
	$("#OLT").append($port2);	
	
	//createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType]);
	for(var j = 1; j < 6 + 1; j++){
	    try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getXgucTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
	    }catch(exp){
	    	var slot = olt.slotList[i - 1];
	    	createPort2(slot.slotRealIndex, j, 'xeFiber', getXgucTop(j), bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }
    }
}

/**************************************************************************
 		预配置类型为空板,如果有实际类型，则只绘制实际类型的端口，不能进行操作
 **************************************************************************/
function createNonBoard(i) {
	var slot = olt.slotList[i - 1];
	var $actualType = bActualType[slot.type];
	var $preType = bType[slot.topSysBdPreConfigType];
	var $board = createBoard(slot.slotRealIndex, $actualType, $preType);
	if(slot.topSysBdActualType ==0 ){
	}else if(slot.topSysBdActualType == 5){//geua
		for(var j = 1; j < 8 + 1; j++){
			if(j<5) createPort2(slot.slotRealIndex, j, 'geCopper', getGeuaTop(j+2), $actualType, $preType);
        	else createPort2(slot.slotRealIndex, j, 'geFiber', getGeuaTop(j+2), $actualType, $preType);
		}
	}else if(slot.topSysBdActualType ==6 ){//geub
		for(var j = 1; j < 8 + 1; j++)
	    	createPort2(slot.slotRealIndex,j,'geFiber',getGeuaTop(j), $actualType, $preType);
	}else if(slot.topSysBdActualType == 3 || slot.topSysBdActualType ==4 ){//epua,epub
		for(var j = 1; j < 8 + 1; j++)
	    	createPort2(slot.slotRealIndex,j,'geEpon',getEpuaTop(j), $actualType, $preType);
	}else if(slot.topSysBdActualType == 7){//xgua
		for(var j = 1; j < 2 + 1; j++){
       		createPort2(slot.slotRealIndex, j, 'geFiber', getXguaTop(j), $actualType, $preType);
		}
	}else if(slot.topSysBdActualType ==8 ){ //xgub
		for(var j = 1; j < 2 + 1; j++)
	    	createPort2(slot.slotRealIndex,j,'xeFiber',getXgubTop(j), $actualType, $preType);
	}else if(slot.topSysBdActualType ==9 ){ //xguc
		for(var j = 1; j < 2 + 1; j++)
	    	createPort2(slot.slotRealIndex,j,'xeFiber',getXgucTop(j), $actualType, $preType);
		for(var j = 3; j < 3 + 1; j++)
	    	createPort2(slot.slotRealIndex,j,'geFiber',getXgucTop(j), $actualType, $preType);
	}else if(slot.topSysBdActualType == 11){//gpua
		for(var j = 1; j < 16 + 1; j++) {
			createPort2(slot.slotRealIndex, j, 'gpon', getGpuaTop(j), $actualType, $preType);
		}
	} 
}

function createErrorBoard (i) {
  	$slot = $("<div>");
	$slot.attr({
		id :  "error_" + i + "_0",
		checked:'invalid'
	});
	$slot.addClass("errorBoardClass");
	$slot.css({
		width : coordinateParam.slotWidth,
		height : coordinateParam.slotHeight,
		position : 'absolute',
		top : coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin,
		left : coordinateParam.slotLeftStart ,
		backgroundImage:'url(/epon/image/emptySlot.png)',
		//backgroundColor : '#ffffff',
		border : '1px solid '+boardColor,
		zIndex : 1500
	});
	$state = $("<img>");
	$state.attr({
		width : 12,height : 12,
		title :  I18N.EPON.bdnoservice
	});
	$state.css({
		position : 'absolute',top : 3,left : coordinateParam.slotLeftStart-20,opacity : 1,zIndex : 3500
	}); 
	$state.attr({
		src : '/epon/image/close.gif'
	});
	$slot.append($state);	
	$("#OLT").append($slot);
}

function updateOltJson(currentId, attr, value) {
	var currentObj;
	var tmpArray = currentId.split("_");
	if (tmpArray[0] == "fan") {
		// 风扇
		currentObj = olt.fanList[tmpArray[1]-1];
	} else if (tmpArray[0] == "power") {
		// 电源
		currentObj = olt.powerList[tmpArray[1]-1];
	} else if (tmpArray[2] == 0) {
		// 槽位或板卡
		currentObj = olt.slotList[tmpArray[1]-1];
	} else {
		// 端口
		currentObj = olt.slotList[tmpArray[1]-1].portList[tmpArray[2]-1];
	}
	//-----------只是修改了OLT大对象中的属性值，界面已展现部分的属性没有改变，比如nodeName------//
	eval('currentObj.' + attr + ' = "' + value + '";');
	if (attr == 'sniPortName') {
		if (!!Ext.getCmp("OltTreePanel")) {
			Ext.getCmp("OltTreePanel").getNodeById(currentId).setText(value);
		}
	}
	document.getElementById(currentId).click();
}
function createFan(idx){
	$fan = $("<div>");
	$fan.attr({
		id:"fan_"+idx+"_0",
		title : I18N.EPON.fanSpeed +  ": " + olt.fanList[0].topSysFanSpeed
	});
	$fan.addClass("fanClass");
	$fan.css({
		width : coordinateParam.fanWidth,
		height : coordinateParam.fanHeight,
		position : 'absolute',
		top : coordinateParam.fanTopMargin,
		left : coordinateParam.fanStartLeft,
		backgroundImage : 'url('+image_fan+')',
		backgroundRepeat : 'no-repeat'
	});
	$("#OLT").append($fan);
}

/**
 * 定义创建PN8602面板图方法的入口
 * 
 * @param json
 */
function createPN8602() {
    $device = $("<div>")
	$device.attr({
		id : 'OLT',
		title : '@COMMON.name@ : '+ olt.oltName + " @EPON.entityLoc@ : " + olt.location
	})
	$device.addClass("deviceClass")
	$device.css({
		width : coordinateParam.oltWidth,
		height : coordinateParam.oltHeight,
		position : 'absolute',
		top : coordinateParam.oltTop,
		left : coordinateParam.oltLeft,	
		backgroundImage : 'url(' + image_device + ')',
		backgroundRepeat : 'no-repeat', 
		zIndex : 1000,
		border :'2px solid transparent'
	})
	$("#device_container").append($device);	
	$device.bind('mouseover',function(e){
		preventBubble(e)
	 	switchNotation('DeviceNotation')
	}).bind('mouseout',function(e){
		preventBubble(e)
	});
    for (var i = 1; i < olt.slotList.length + 1; i++) {
    	//---------------i = realIndex--------------//    
    	//var presenceStatus = olt.slotList[i - 1].bPresenceStatus;
        switch (olt.slotList[i - 1].topSysBdPreConfigType) {
            case 1 : 
                createMpuaBoard(i);
                break;
            case 2 : 
                createMpubBoard(i);
                break;
            case 3 : 
                createEpuaBoard(i);
                break;
            case 4 : 
                createEpubBoard(i);
                break;
            case 5 : 
                createGeuaBoard(i);
                break;
            case 6 : 
                createGeubBoard(i);
                break;
            case 7 : 
                createXguaBoard(i);
                break;
            case 8 : 
                createXgubBoard(i);
                break;
            case 9 : 
                createXgucBoard(i);
                break;
            case 10 :
            	//XPUA
                //createXgucBoard(i);
                break;
            case 11 :
            	//mpu-geua
            	createGeuaBoard(i);
                break;
            case 12 :
            	//mpu-geub
            	createGeubBoard(i);
                break;
            case 13:
            	createXgucBoard(i);
                break;
            case 14:
            	createGPUABoard(i);
            	break;
            case 15:
            	//epuc;
            	createEpucBoard(i)
            	break;
            case 255 :
            	createErrorBoard(i);
            	break;
            default :
                createNonBoard(i);
                break;
        }
    }// end of loop
    for (var i = 1; i < olt.fanList.length + 1; i++) {
    	createFan(i);
    }
    for (var i = 1; i < olt.powerList.length + 1; i++) {
    	createPower(i);
    }
}

/**************************************************************
			  定义创建板卡的方法
			  @param i  板卡位置
			  @param type 板卡类型
			  @param preType  板卡预配置类型
			  @returns $Board对象
**************************************************************/
function createBoard(i, type, preType) {
	var $slot = $("<div>");
	var slot = olt.getItem(i);
	var titleStr = "";
	if((olt.slotList[i - 1].topSysBdCurrentTemperature==127 || olt.slotList[i - 1].topSysBdTempDetectEnable==2)){
    	titleStr = I18N.EPON.bdSlot + ': ' + i
    }else{
    	//titleStr = I18N.EPON.boardTemperature + ":" + (olt.slotList[i-1].topSysBdCurrentTemperature) + I18N.COMMON.degreesCelsius + '  '+ I18N.EPON.bdSlot  +':' + i;
    	titleStr = I18N.EPON.boardTemperature + ":" + (olt.slotList[i-1].slotDisplayTemp) + "@{unitConfigConstant.tempUnit}@" + '  '+ I18N.EPON.bdSlot  +':' + i;
    }
	$slot.attr({
		id : preType + "_" + i + "_0",
		checked : 'invalid',
		title : titleStr
	});
	$slot.addClass("boardClass");
	$slot.css({
		width : coordinateParam.slotWidth,
		height : coordinateParam.slotHeight,
		position : 'absolute',
		top : coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin,
		left : coordinateParam.slotLeftStart,
		backgroundImage : 'url(/epon/image/emptySlot.png)',
		border : '1px solid transparent',
		zIndex : 1500
	});
	$("#OLT").append($slot);
	//*********  type != 'slot  ‘*********//
	if('slot' != type) {
		//---------actualType != preType == 'slot':展示实际板卡-------//
		if('slot' == preType) {
			$slot.html( '<font title="normal" color="white" class="X-BOARD-TYPE boardTag">' + type.substring(0, 2).toUpperCase() + '</font>' );
			//---------preType != 'slot' ： 展示预配置板卡-------//
		} else {
			$slot.html( '<font title="normal" color="white" class="X-BOARD-TYPE boardTag">' + preType.substring(0, 2).toUpperCase() + '</font>' );
		}
		//***********  type==slot   ***********//
	} else {
		//-----------actType == preType == 'slot' : no Tag----------//
		if('slot' == preType) {
			;
			//-----------actType  == 'slot' != preType----------//
		} else {
			$slot.html(  '<font title="normal" color="white" class="X-BOARD-TYPE boardTag">' + preType.substring(0, 2).toUpperCase() + '</font>');
		}
	}
	//****************  标记状态：预配置与实际类型不一致  ****************//
	var tip,icon;
	if(preType != type) {
		tip = I18N.EPON.unInjectBD ;
        icon=ICON.UNINJECT;
        if(preType == SLOT_BOARD && type != SLOT_BOARD ){
            tip = I18N.EPON.unPreCfg;
            icon=ICON.UNPRECFG;
        }else if(preType != SLOT_BOARD && type != SLOT_BOARD){
            tip = I18N.EPON.unUnique;
            icon=ICON.UNCOMPATIBLE;
        }
	}
	
	if(preType != type && !preType.contains("mpu")) {
		$icon = $("<img>");
		$icon.attr({
			src : icon,
			title: tip,
			width : 16,
			height : 16
		}).css({
			position : 'absolute',
			left : coordinateParam.slotWidth - 30,
			top : 2,
			zIndex : 10000
		}).click(function() {
			return false;
		});
		$slot.append($icon);
	};

	//--------板卡告警状态-----------//
	switch(olt.slotList[i - 1].BAlarmStatus) {
		case 0 :
			$slot.find(".boardTag").attr({
				color : 'red',
				title : 'critical'
			});
			break;
		case 1 :
			$slot.find(".boardTag").attr({
				color : 'orange',
				title : 'major'
			});
			break;
		case 2 :
			$slot.find(".boardTag").attr({
				color : 'yellow',
				title : 'minor'
			});
			break;
		case 3 :
			$slot.find(".boardTag").attr({
				color : 'green',
				title : 'warning'
			});
			break;
	}
	//************   skip only when preType == type == 'slot'  **************//
	if(preType == type && type == 'slot') {
		;
	} else {
		var lampStatus = olt.slotList[i - 1].topSysBdLampStatus;
		var lampStatusString = lampStatus.toString(16);
		if(lampStatusString.length < 8) {
			var rest = 8 - lampStatusString.length;
			for(var st = 0; st < rest; st++) {
				lampStatusString = '0'.concat(lampStatusString);
			}
		}
		var MSB = parseInt(lampStatusString.substring(0, 2), 16);
		var RUN = parseInt(lampStatusString.substring(2, 4), 16);
		var ALARM = parseInt(lampStatusString.substring(4, 6), 16);
		drawSlotLight(ledColor[RUN],35 + 8,6,"circle2",$slot,"RUN");
		drawSlotLight(alertColor[ALARM==0?ALARM:ALARM-1],35 + 16, 6, "circle3",$slot,"ALARM");
		//判断是否板卡是否加电,只要插入状态为installed就亮
		drawSlotLight(ledColor[slot.BPresenceStatus == 1 ? 1 : 0], 35, 6,"circle1",$slot,"PWR");
		WIN.$cont = null;
	}
	$slot.bind('mouseover', function(e) {
		switchNotation('BoardNotation');
		$("#sound").attr('src', '/epon/sound/click3.wav');
		preventBubble(e);
		setBorderStyle(this.id);
	});
	$slot.bind('mouseout', function(e) {
		preventBubble(e);
		$("#device_container").unbind('mousemove');
		$position = $(this);
		var x = $position.offset().left;
		var y = $position.offset().top;
		cursor.X = e.pageX;
		cursor.Y = e.pageY;
		if(cursor.X > x + 3 && cursor.X < x + coordinateParam.slotWidth - 3 && cursor.Y > y + 3 && cursor.Y < y + coordinateParam.slotHeight - 3) {
			//如果光标没有离开板，即在端口上，那么不做修改
			return;
		}
		clearBorderStyle(this.id);
	});
	return $slot;
}



/**
 * 定义创建未插入状态的端口
 * 
 * @param sID
 *            槽位位置Id
 * @param pID
 *            端口位置Id
 * @param pType
 *            端口类型
 * @param Top
 *            距离顶点的位置
 * @param sType
 *            板卡类型
 * @param sPreType
 *            板卡预配置类型 return $port对象
 */
function createPort2(sID, pID, pType, Left, sType,sPreType){
	Left = Left+coordinateParam.slotLeftStart; 
    var Top = coordinateParam.slotTopStart + (sID - 1) * coordinateParam.slotTopMargin+coordinateParam.portTopMargin;
	$port = $("<div>");
	$port.attr({
		id : sType + "_" + sID + "_" +pID,
		portType:pType//标记端口类型
	});
	$port.addClass("portClass1");// 无效的端口样式类
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : Top,
		left : Left,
		backgroundImage : 'url(/epon/image/'  + pType + '/'+pType+ '.png)',
		backgroundRepeat : 'no-repeat',
		border : '1px solid transparent',
		opacity : 0.1,
		zIndex : 1500
	});
	$("#OLT").append($port);		
	return $port;
}
/**
 * 定义创端口的方法
 * 
 * @param sID
 *            槽位位置Id
 * @param pID
 *            端口位置Id
 * @param pType
 *            端口类型
 * @param Top
 *            距离顶点的位置
 * @param sType
 *            板卡类型
 * @param sPreType
 *            板卡预配置类型 return $port对象
 */
function createPort(sID, pID, pType, Left, sType,sPreType) {
    $port = $("<div>");
    Left = Left+coordinateParam.slotLeftStart; 
    var Top = coordinateParam.slotTopStart + (sID - 1) * coordinateParam.slotTopMargin+coordinateParam.portTopMargin;
	$port.attr({
		id : sPreType + "_" + sID + "_" +pID,
		title : I18N.EPON.portType + ':'+pType+ " " + I18N.EPON.portLoc + ': '+pID,
		portSubType:pType
	});
	$port.addClass("portClass"); 
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : Top,
		left : Left,
		backgroundImage : 'url(/epon/image/'+ pType + '/'+pType+ '.png)',
		backgroundRepeat : 'no-repeat',
		border: '1px solid transparent',
		zIndex : 1500
	});
	$("#OLT").append($port);
    $port.bind('mouseover',function(e){
    	var arr = this.id.split("_");
		if( UPLINK_BOARD.contains(arr[0]) ){
			switchNotation('SniNotation');
		}
		if( DOWNLINK_BOARD.contains(arr[0]) ){
			switchNotation('PonNotation');
		}
    	$("#sound").attr({
	  			src : '/epon/sound/click3.wav'
	  		}); 
    	preventBubble(e);
    	setBorderStyle(this.id);
        setBorderStyle(this.id.substring(0,7)+"0"); // 修改父板的透明度
        divCache.add(this.id.substring(0,7)+"0");// 父板被修改了，故也要记录
	});
    $port.bind('mouseout',function(e){
	   preventBubble(e);		
	   clearBorderStyle(this.id);	   		  		   
	});
    //端口状态
	var state = null;
	switch(olt.slotList[sID-1].portList[pID-1].portSubType){
		//SNI MASSTYPE
		case 'geCopper'://NO NEED TO HANDLE
		case 'geFiber'://NO NEED TO HANDLE
		case 'xeFiber':
			var os = olt.slotList[sID-1].portList[pID-1].sniOperationStatus;
			var as = olt.slotList[sID-1].portList[pID-1].sniAdminStatus;
			if( as==1 && os==1 ){
				state = 0;
			}else if( as==1 && os==2 ){
				state = 1;
			}else if(as==2 && os==2) {
				state = 2;
			}else {
				state = 2;
			}
			break;
		//PON MASSTYPE
		case 'geEpon'://NO NEED TO HANDLE
		case 'tengeEpon'://NO NEED TO HANDLE
		case 'gpon':
			var os = olt.slotList[sID-1].portList[pID-1].ponOperationStatus;
			var as = olt.slotList[sID-1].portList[pID-1].ponPortAdminStatus;
			if( as==1 && os==1 ){
				state = 0;
			}else if( as==1 && os==2 ){
				state = 1;
			}else if(as==2 && os==2) {
				state = 2;
			}else {
				state = 2;
			}
			break;	
		default:			
			break;	
	}
	switch(state){
	case 0 ://NO NEED TO HANDLE
			break;
	case 1 :
			$port.css({
				backgroundImage : 'url(/epon/image/' + pType + '/'+pType+ '.gif)'
			});				
			break;
	case 2 :
			$port.css({
				backgroundImage : 'url(/epon/image/'+ pType + '/'+pType+ '.png)'
			});
			$state = $("<img>");
			$state.addClass("portStateClass");
			$state.attr({
				width : 12,
				height : 12,
				title :  I18N.EPON.portnoservice,
				src : '/epon/image/close.gif'	
			});			
			$state.css({
				position : 'absolute',
				top : 3,
				left : 3,			
				zIndex : 3500
			}); 
			$port.append($state);	// 在板卡上添加端口状态
			break;	
	default : 
			break;
	}	
	
	return $port;
}

/***********************************************************************
 	 					创建电源
 **********************************************************************/
function createPower(idx){
	$power = $("<div>");
	$power.attr({
		id : "power_"+idx+"_0"
	});
	$power.addClass("powerClass");
	$power.css({
		display:'none'
		/*,width : coordinateParam.powerWidth,
		height : coordinateParam.powerHeight,
		position : 'absolute',
		backgroundImage:'url(/epon/image/powerWire.png)',
		backgroundRepeat:'no-repeat',
		backgroundPosition:'0px 0px',
		top : coordinateParam.powerTopMargin,
		left : coordinateParam.powerStartLeft+(idx-1)*coordinateParam.powerGapLeft*/
	});
	$("#OLT").append($power);
}

/***********************************************************************
 	 将一个Index转换为一个页面可用的DivId
 	  param Index return DivId
 **********************************************************************/
function IndexToId(idx){
	var index = idx.toString(16);
	//如果首位为0则补0。
	if (index.length==9)
		index = '0'.concat(index);
	else if(index.length==8)
		index = '00'.concat(index);
	else if(index.length==7)
		index = '000'.concat(index);
	var slot = parseInt(index.substring(0,2),16);
	var port = parseInt(index.substring(2,4),16);
	for(var i=0 ;i < olt.slotList.length && slot == 0;i++){
		//---由于只有主用MPUA板上有端口可以画，所以即便备用主控板的slotIndex为255也对解析没有影响---//
		if(olt.slotList[i].slotIndex == 0){
			slot = olt.slotList[i].slotRealIndex;
		}
	}
	return slotNumToSlotType(slot-1)+"_"+slot+"_"+port;	
}

/************************************************************************
将一个Index转换为一个 3/1  3/1:1 3/1:1:1格式
***********************************************************************/
function toMark(idx){
	var index = idx.toString(16);
	//如果首位为0则补0。
	index = '000000'.concat(index);
	index = index.substring(index.length-10);
	var slot = parseInt(index.substring(0,2),16);
	var port = parseInt(index.substring(2,4),16);
	for(var i=0 ;i < olt.slotList.length && slot == 0;i++){
		//---由于只有主用MPUA板上有端口可以画，所以即便备用主控板的slotIndex为255也对解析没有影响---//
		if(olt.slotList[i].slotIndex == 0){
			slot = olt.slotList[i].slotLogicNum;
		}
	}
	return slot+"/"+port;
}

/***********************************************************************
 	 	将一个DivId转换为一个页面可用的Index param DivId return index
 **********************************************************************/
function IdToIndex(Id){
	 var arr = Id.split("_");
	 return olt.slotList[parseInt(arr[1])-1].portList[parseInt(arr[2])-1].portIndex; 	
}

/***********************************************************************
 * 根据传递来的端口divId得到端口类型
 * @param	 divId
 * @returns  {String}
 **********************************************************************/
function getPortType(divId){
	var arr = divId.split("_");
	return olt.slotList[arr[1]-1].portList[arr[2]-1].portSubType;
}

/***********************************************************************
	 		将板卡类型号转成板卡类型名
 **********************************************************************/
function slotNumToSlotType(slotNum){
	return bType[olt.slotList[parseInt(slotNum)].topSysBdPreConfigType];
}

/*********************************************************************
  修改设备状态，4个视图共用，都可以修改 param: index:唯一标识，状态号
 **********************************************************************/
function displayEmergency(index,state){
	window.location.reload();
	return;
}
var deviceStyle = ["", I18N.EPON.styleClose , I18N.EPON.styleInject , I18N.EPON.other];
var bPresenceStatus = [I18N.EPON.unknown , I18N.EPON.injected , I18N.EPON.notInject, I18N.EPON.other];
var coordinateParam = {
		oltHeight : 102,
	    oltWidth : 432,
	    oltTop : 100,
	    oltLeft : 40,

	    slotHeight : 27,
	    slotWidth : 356,
	    slotLeftStart : 53,
	    slotTopStart : 15,
	    slotTopMargin : 27,

	   	portHeight : 16,
	    portWidth : 15,
	    portTopMargin : 3,
	    portTop : 0,
	    sniTopStart : 45,
	    sniTopMargin : 26,

	    epuaFirstPon : 100,
	    epuaMargin1 : 50,
	    epuaMargin2 : 18,
	    
	    vpuaFirstPon : 60,
	    vpuaMargin1 : 33,
	    vpuaMargin2 : 16,

	    geuaConsole : 93,
		geuaMgmt : 110,

		geuaSniE : 130,
		geuaSniGapE : 16,
		geuaSniO :	200,
		geuaSniGapO1 : 18,
		geuaSniGapO2 : 50,

		xguaX01 : 200,
		xguaX02 : 228,
		
		xgubX01 : 150,
		xgubX02 : 170,
		xgubX03 : 200,
		xgubX04 : 220,
		
		xgucX01 : 130,
		xgucX02 : 150,
		xgucX03 : 180,
		xgucX04 : 200,
		xgucX05 : 230,
		xgucX06 : 250,
		
		mpuaSniE : 170,
		mpuaSniGap : 17,
		mpuaConsole : 250,
		mpuaMgmt : 270,
		powerWidth :  100,
		powerHeight : 180,
		powerTopMargin : 100,
		powerStartLeft : 60,
		powerGapLeft : 180,

		fanWidth :  31,
		fanHeight : 80,
		fanTopMargin : 16,
		fanStartLeft : 22,
		fanGapLeft : 152,

		alarmLeft : 3,
		alarmTop : 250,
		portStateLeft : 6,
		portStateTop : 6,

		illustrateLeft : 20,
		illustrateTop : 480,
		illustrateWidth : 920,
		illustrateHeight : 80,
		illustrateHeight2 : 30
}
var ledColor = ['gray','#E3FD07','gray'];
var alertColor = ['gray','red','gray'];
var divStyle = {
	hover : '#00EEEE',
	click : 'yellow',
	back : 'green'
}
var cursor = {
	X : 0,
	Y : 0
};
var json = null;
var olt = null;
var grid = null;
var entityId = '<s:property value="entity.entityId"/>';
var image_device = '/epon/image/8602.png';
var image_board = '/epon/image/board8602.png';
//var image_power = 'image/power.png';
var image_fan = '/epon/image/fan8602.png';
var image_pon = '/epon/image/pon.png';
var image_sni = '/epon/image/sni.png';
var boardColor = "#71787e";