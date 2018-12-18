/*******************************
 		calc offset
 ********************************/
function getGeuaTop(index) {
    if(1 == index){
        return coordinateParam.geuaConsole;
    } else if (2 == index) {
		return coordinateParam.geuaMgmt;
	} else if (2 < index && 7 > index) {
		return coordinateParam.geuaSniE + (index-3)*coordinateParam.geuaSniGapE;
	} else if (6 < index && index < 11) {
		if(index % 2 != 0 )
			return coordinateParam.geuaSniO + parseInt((index-7)/2)*coordinateParam.geuaSniGapO2;
		else
			return coordinateParam.geuaSniO + parseInt((index-7)/2)*coordinateParam.geuaSniGapO2 + coordinateParam.geuaSniGapO1;
	}
}

function getEpuaTop(index) {
    if (index % 2 != 0)
        return coordinateParam.epuaFirstPon + parseInt((index - 1) / 2)* coordinateParam.epuaMargin1;
    else 
        return coordinateParam.epuaFirstPon + parseInt((index - 1) / 2)* coordinateParam.epuaMargin1 + coordinateParam.epuaMargin2;
}

function getMpuaTop(index) {
	if (0 < index && index < 5)
		return coordinateParam.mpuaSniE + (index - 1) * coordinateParam.mpuaSniGap;
	else if (5 == index)
		return coordinateParam.mpuaConsole;
	else if (6 == index) 
		return coordinateParam.mpuaMgmt;	
}

function getXguaTop(index) {
	if(1==index)
		return coordinateParam.xguaX01;
	else if(2==index)
		return coordinateParam.xguaX02;	
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

function getXpuaTop(index) {
	if(1==index){
		return coordinateParam.xpuaX01;
	}else if(2==index){
		return coordinateParam.xpuaX02;
	}else if(3==index){
		return coordinateParam.xpuaX03;
	}else if(4==index){
		return coordinateParam.xpuaX04;
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

/**************************************
		create mpua
 ***************************************/
function createMpuaBoard(i) {
	var slot = olt.slotList[i - 1]
	var top = coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin+coordinateParam.portTopMargin;
    $board = createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType])
    $port = $("<div>")
    $port.addClass("consoleClass")
	$port.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_MGMT",
		title : I18N.EPON.portType + ':MGMT' +  I18N.EPON.portNum + ': 1'
	})
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : getMpuaTop(5)+coordinateParam.slotLeftStart,	
		backgroundImage : 'url(/epon/image/mgmt.png)',	
		backgroundRepeat : 'no-repeat', 
		border : '1px solid transparent',
		opacity : 0.4,
		zIndex : 1500
	})
	$("#OLT").append($port)
	$port2 = $("<div>")
	$port2.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_CONSOLE",
		title : I18N.EPON.portType + ':CONSOLE' +  I18N.EPON.portNum + ': 2'
	})
	$port2.addClass("consoleClass")
	$port2.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : getMpuaTop(6)+coordinateParam.slotLeftStart,	
		backgroundImage : 'url(/epon/image/console.png)',	
		backgroundRepeat : 'no-repeat', 
		border : '1px solid transparent',
		opacity : 0.4,
		zIndex : 1500
	})
	$("#OLT").append($port2)     
	for(var j = 1; j < 4 + 1; j++){
	    try{
			var port = slot.portList[j - 1]
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getMpuaTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType])
	    }catch(exp){
	    	createPort2(slot.slotRealIndex, j, 'geFiber', getMpuaTop(j), bType[slot.type],bType[slot.topSysBdPreConfigType])
	    }
    }
}

function createMpubBoard(i) {
	var slot = olt.slotList[i - 1]
	var top = coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin+coordinateParam.portTopMargin;
    $board = createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType])
    $port = $("<div>")
    $port.addClass("consoleClass")
	$port.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_MGMT",
		title : I18N.EPON.portType + ':MGMT' +  I18N.EPON.portNum + ': 1'
	})
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : getMpuaTop(5)+coordinateParam.slotLeftStart,	
		backgroundImage : 'url(/epon/image/mgmt.png)',	
		backgroundRepeat : 'no-repeat', 
		border : '1px solid transparent',
		opacity : 0.4,
		zIndex : 1500
	})
	$("#OLT").append($port)
	$port2 = $("<div>")
	$port2.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_CONSOLE",
		title : I18N.EPON.portType + ':CONSOLE' +  I18N.EPON.portNum + ': 2'
	})
	$port2.addClass("consoleClass")
	$port2.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : getMpuaTop(6)+coordinateParam.slotLeftStart,	
		backgroundImage : 'url(/epon/image/console.png)',	
		backgroundRepeat : 'no-repeat', 
		border : '1px solid transparent',
		opacity : 0.4,
		zIndex : 1500
	})
	$("#OLT").append($port2)  
}

/**************************************
 	  		create epua
 ***************************************/
function createEpuaBoard(i) {
	var slot = olt.slotList[i - 1];
    createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType]); 
    for(var j = 1; j < 8 + 1; j++){ 
    	try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getEpuaTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
    	}catch(exp){
    		createPort2(slot.slotRealIndex,j,'geEpon',getEpuaTop(j),bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }
    }
}

/**************************************
 	 create epub
 ***************************************/
function createEpubBoard(i) {
	var slot = olt.slotList[i - 1];
    createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType]); 
    for(var j = 1; j < 8 + 1; j++){ 
    	try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getEpuaTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
    	}catch(exp){
    		createPort2(slot.slotRealIndex,j,'geEpon',getEpuaTop(j),bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }
    }
}

/**************************************
create epuc
***************************************/
function createEpucBoard(i){
	var slot = olt.slotList[i - 1];
	createBoard(slot.slotRealIndex, bActualType[slot.type],bType[slot.topSysBdPreConfigType]);
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

function getGpuaTop(index){
	if (index % 2 != 0) {
        return coordinateParam.vpuaFirstPon + parseInt((index - 1) / 2)* coordinateParam.vpuaMargin1;
    } else {
        return coordinateParam.vpuaFirstPon + parseInt((index - 1) / 2)* coordinateParam.vpuaMargin1 + coordinateParam.vpuaMargin2;
    }
}





/**************************************
 	 	create geua
 ***************************************/
function createGeuaBoard(i) {
	var slot = olt.slotList[i - 1];
	// /得到到左边的距离
	var top = coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin+coordinateParam.portTopMargin;;	
    $board = createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType]);
    $port = $("<div>");
	$port.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_Console",
		title : I18N.EPON.portType + ':CONSOLE' +  I18N.EPON.portNum + ': 1'
	});		
	$port.addClass("consoleClass");
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : getGeuaTop(1)+coordinateParam.slotLeftStart,	
		backgroundImage : 'url(/epon/image/console.png)',	
		backgroundRepeat : 'no-repeat', 
		opacity : 0.4,
		border : '1px solid transparent',
		zIndex : 1500
	});	
	$("#OLT").append($port);
	 $port2 = $("<div>");
	$port2.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_MGMT",
		title : I18N.EPON.portType + ':MGMT' +  I18N.EPON.portNum + ': 2'
	});
	$port2.addClass("consoleClass");
	$port2.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : getGeuaTop(2)+coordinateParam.slotLeftStart,		
		backgroundImage : 'url(/epon/image/mgmt.png)',	
		backgroundRepeat : 'no-repeat', 
		opacity : 0.4,
		border : '1px solid transparent',
		zIndex : 1500
	});	
	$("#OLT").append($port2);	
   	for (var j = 1; j < 8 + 1; j++) {
        try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getGeuaTop(port.portRealIndex+2), bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }catch(exp){
        	if(j<5)
        		createPort2(slot.slotRealIndex, j, 'geCopper', getGeuaTop(j+2), bType[slot.type],bType[slot.topSysBdPreConfigType]);
        	else
        		createPort2(slot.slotRealIndex, j, 'geFiber', getGeuaTop(j+2), bType[slot.type],bType[slot.topSysBdPreConfigType]);
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

/***************************************
 	 create xgua
**************************************/
function createXguaBoard(i) {
	var slot = olt.slotList[i - 1];
	createBoard(olt.slotList[i - 1].slotRealIndex, bType[olt.slotList[i - 1].type],bType[olt.slotList[i-1].topSysBdPreConfigType]);    
    for(var j = 1; j < 2 + 1; j++){
	    try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getXguaTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
	    }catch(exp){
	    	createPort2(slot.slotRealIndex, j, 'xeFiber', getXguaTop(j), bType[slot.type],bType[slot.topSysBdPreConfigType])
        }
    }
}

function createXgubBoard(i) {
	var slot = olt.slotList[i - 1];
	createBoard(olt.slotList[i - 1].slotRealIndex, bType[olt.slotList[i - 1].type],bType[olt.slotList[i-1].topSysBdPreConfigType]);    
    for(var j = 1; j < 4 + 1; j++){
	    try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getXgubTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
	    }catch(exp){
	    	createPort2(slot.slotRealIndex, j, 'xeFiber', getXgubTop(j), bType[slot.type],bType[slot.topSysBdPreConfigType])
        }
    }
}

function createXgucBoard(i){
	var slot = olt.slotList[i - 1];
	var top = coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin+coordinateParam.portTopMargin;
    $board = createBoard(slot.slotRealIndex, bType[slot.type],bType[slot.topSysBdPreConfigType]);
    $port = $("<div>");
	$port.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_Console",
		title : I18N.EPON.portType + ':CONSOLE' +  I18N.EPON.portNum + ': 1'
	});		
	$port.addClass("consoleClass");
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : getGeuaTop(1)+45,	
		backgroundImage : 'url(/epon/image/console.png)',	
		backgroundRepeat : 'no-repeat', 
		opacity : 0.4,
		border : '1px solid transparent',
		zIndex : 1500
	});	
	$("#OLT").append($port);
	 $port2 = $("<div>");
	$port2.attr({
		id : bType[slot.type] + "_" + slot.slotRealIndex + "_MGMT",
		title : I18N.EPON.portType + ':MGMT' +  I18N.EPON.portNum + ': 2'
	});
	$port2.addClass("consoleClass");
	$port2.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : getGeuaTop(2)+45,		
		backgroundImage : 'url(/epon/image/mgmt.png)',	
		backgroundRepeat : 'no-repeat', 
		opacity : 0.4,
		border : '1px solid transparent',
		zIndex : 1500
	});	
	$("#OLT").append($port2);
	//createBoard(olt.slotList[i - 1].slotRealIndex, bType[olt.slotList[i - 1].type],bType[olt.slotList[i-1].topSysBdPreConfigType]);    
    for(var j = 1; j < 6 + 1; j++){
	    try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getXgucTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
	    }catch(exp){
	    	createPort2(slot.slotRealIndex, j, 'xeFiber', getXgucTop(j), bType[slot.type],bType[slot.topSysBdPreConfigType])
        }
    }
}

function createXpuaBoard(i) {
	var slot = olt.slotList[i - 1];
	createBoard(slot.slotRealIndex, bType[slot.type], bType[olt.slotList[i-1].topSysBdPreConfigType]);    
    for(var j = 1; j < 4 + 1; j++){
	    try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getXpuaTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
	    }catch(exp){
	    	createPort2(slot.slotRealIndex, j, 'tengeEpon', getXpuaTop(j), bType[slot.type],bType[slot.topSysBdPreConfigType])
        }
    }
}

function createGPUABoard(i) {
	var slot = olt.slotList[i - 1];
	$board = createBoard(slot.slotRealIndex, bActualType[slot.type],bType[slot.topSysBdPreConfigType]);
	for(var j = 1; j < 16 + 1; j++){
    	try{
			var port = slot.portList[j - 1];
			createPort(slot.slotRealIndex, port.portRealIndex, port.portSubType, getGpuaTop(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
    	}catch(exp){
    		createPort2(slot.slotRealIndex,j,'gpon',getGpuaTop(j),bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }
    }
}

/**************************************
  		create non board 
**************************************/
function createNonBoard(i) {
	var slot = olt.slotList[i - 1];
	var $actualType = bActualType[slot.type];
	var $preType = bType[slot.topSysBdPreConfigType];
	var $board = createBoard(slot.slotRealIndex, $actualType,$preType);
	if(slot.topSysBdActualType == 0 ){
		
	}else if( slot.topSysBdActualType in [3,4] ){//epua
		for(var j = 1; j < 8 + 1; j++){
	    	createPort2(slot.slotRealIndex,j,'geEpon',getEpuaTop(j), $actualType, $preType);
		}
	}else if(slot.topSysBdActualType == 10){//xpua
		for(var j = 1; j < 4 + 1; j++){
       		createPort2(slot.slotRealIndex, j, 'tengeEpon', getXpuaTop(j), $actualType, $preType);
		}
	}else if(slot.topSysBdActualType == 5){//geua
		for(var j = 1; j < 8 + 1; j++){
			if(j<5)
        		createPort2(slot.slotRealIndex, j, 'geCopper', getGeuaTop(j+2), $actualType, $preType);
        	else
        		createPort2(slot.slotRealIndex, j, 'geFiber', getGeuaTop(j+2), $actualType, $preType);
		}
	}else if(slot.topSysBdActualType == 6){//geub
		for(var j = 1; j < 8 + 1; j++){
        	createPort2(slot.slotRealIndex, j, 'geFiber', getGeuaTop(j+2), $actualType, $preType);
		}
	}else if(slot.topSysBdActualType == 7){//xgua
		for(var j = 1; j < 2 + 1; j++){
       		createPort2(slot.slotRealIndex, j, 'geFiber', getXguaTop(j), $actualType, $preType);
		}
	}else if(slot.topSysBdActualType == 8){//xgub
		for(var j = 1; j < 4 + 1; j++){
       		createPort2(slot.slotRealIndex, j, 'geFiber', getXgubTop(j), $actualType, $preType);
		}
	}else if(slot.topSysBdActualType == 9){//xguc
		for(var j = 1; j < 2 + 1; j++){
       		createPort2(slot.slotRealIndex, j, 'xeFiber', getXgucTop(j), $actualType, $preType);
		}
		for(var j = 3; j < 6 + 1; j++){
       		createPort2(slot.slotRealIndex, j, 'geFiber', getXgucTop(j), $actualType, $preType);
		}
	}else if(slot.topSysBdActualType == 11){//gpua
		for(var j = 1; j < 16 + 1; j++) {
			createPort2(slot.slotRealIndex, j, 'gpon', getGpuaTop(j), $actualType, $preType);
		}
	}
}

/****************************************
 	 	创建一个出错板
 ***************************************/
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
		left : coordinateParam.slotLeftStart,
		top : coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin,		
		backgroundColor : 'transparent',
		opacity : 0.7,
		zIndex : 1500,
		border : '2px solid transparent'
	});
	
	$state = $("<img>");
	$state.attr({
		width : 12,
		height : 12,
		title : I18N.EPON.bdnoservice
	});
	$state.css({
		position : 'absolute',
		top : coordinateParam.slotHeight-20,
		left : 3,
		opacity : 1,
		zIndex : 3500
	});
	$state.attr({
		src : '/epon/image/close.gif'
	});
	$slot.append($state);	// 在板卡上添加端口状态
	$("#OLT").append($slot);	
}

/**************************************
 * 			update olt json
 * @param currentId
 * @param attr
 * @param value
 ***************************************/
function updateOltJson(currentId, attr, value) {
	var currentObj;
	var tmpArray = currentId.split("_");
	if (tmpArray[0] == "fan") {
		currentObj = olt.fanList[tmpArray[1]-1];
	} else if (tmpArray[0] == "power") {
		currentObj = olt.powerList[tmpArray[1]-1];
	} else if (tmpArray[2] == 0) {
		currentObj = olt.slotList[tmpArray[1]-1];
	} else {
		currentObj = olt.slotList[tmpArray[1]-1].portList[tmpArray[2]-1];
	}
	//-----------只是修改了OLT大对象中的属性值，界面已展现部分的属性没有改变，比如nodeName------//
	eval('currentObj.' + attr + ' = "' + value + '";');
	//----------暂采用手动方法修改，以后使用面向对象的事件机制实现-------//
	if (attr == 'sniPortName') {
		if (!!Ext.getCmp("OltTreePanel")) {
			Ext.getCmp("OltTreePanel").getNodeById(currentId).setText(value);
		}
	}
	document.getElementById(currentId).click();
}


/****************************************
 		定义创建PN8603面板图方法的入口
 ***************************************/
function createPN8603() {
    $device = $("<div>");
	$device.attr({
		id : 'OLT',
		title : '@COMMON.name@ : '+ olt.oltName + " @EPON.entityLoc@ : " + olt.location
	}).addClass("deviceClass");
	$device.css({
		width : coordinateParam.oltWidth,
		height : coordinateParam.oltHeight,
		position : 'absolute',
		top : 50 , left : 5,
		backgroundImage : 'url(' + image_device + ')',
		backgroundRepeat : 'no-repeat', 
		zIndex : 1000,
		border :'2px solid transparent'
	});
	$("#device_container").append( $device );
	$device.bind('mouseover', function(e){
	 	switchNotation('DeviceNotation');
		preventBubble(e);
	});
	$device.bind('mouseout',function(e){
		preventBubble(e);
	});
	//---------写死18个板，没有采集到也要画出来-----------//
	 for (var i = 0; i < olt.slotList.length ; i++) { 
    	//--i+1 = slotRealIndex///按照板卡预配置类型展示板卡----------------//
        switch (olt.slotList[i].topSysBdPreConfigType) { 
            case 1 : 
                createMpuaBoard(olt.slotList[i].slotRealIndex);
                break;
            case 2 :
                createMpubBoard(olt.slotList[i].slotRealIndex);
                break;
            case 3 : 
                createEpuaBoard(olt.slotList[i].slotRealIndex);
                break;
            case 4 : 
                createEpubBoard(olt.slotList[i].slotRealIndex);
                break;
            case 5 : 
                createGeuaBoard(olt.slotList[i].slotRealIndex);
                break;
            case 6 : 
                createGeubBoard(olt.slotList[i].slotRealIndex);
                break;
            case 7 : 
                createXguaBoard(olt.slotList[i].slotRealIndex);
                break;
            case 8 : 
                createXgubBoard(olt.slotList[i].slotRealIndex);
                break;
            case 9 : 
                createXgucBoard(olt.slotList[i].slotRealIndex);
                break;
            case 10 : 
                createXpuaBoard(olt.slotList[i].slotRealIndex);
                break;
            case 11 :
            	//mpu-geua
            	createGeuaBoard(olt.slotList[i].slotRealIndex);
                break;
            case 12 :
            	//mpu-geub
            	createGeubBoard(olt.slotList[i].slotRealIndex);
                break;
            case 13:
            	createXgucBoard(olt.slotList[i].slotRealIndex);
                break;
            case 14:
            	createGPUABoard(olt.slotList[i].slotRealIndex);
            	break;
            case 15: //epuc
            	createEpucBoard(olt.slotList[i].slotRealIndex);
            	break;
            //-----------针对未采集到的设备---------//
            case 255 :
            	createErrorBoard(olt.slotList[i].slotRealIndex);
            	break;
            default :
                createNonBoard(olt.slotList[i].slotRealIndex);
                break;
        }
    //----------- end of loop------------//
    }
    for (var i = 1; i < olt.fanList.length + 1; i++) {
    	 createFan(i);
    }
    for (var i = 1; i < olt.powerList.length + 1; i++) {
    	createPower(i);
    }
}

/************************************************
 * 定义创建板卡的方法
 * @param i           板卡位置
 * @param type  	  板卡类型
 * @param preType 	  板卡预配置类型
 * @returns 		  $Board对象
 ***********************************************/
function createBoard(i, type, preType) {
	var $slot = $("<div>"),
	    titleStr = EMPTY,
	    slot = olt.getItem(i);
	if((olt.slotList[i - 1].topSysBdCurrentTemperature==127 || olt.slotList[i - 1].topSysBdTempDetectEnable==2)){
		titleStr = I18N.EPON.bdSlot + ': ' + i
	}else{
		//titleStr = I18N.EPON.boardTemperature + ":" + (olt.slotList[i-1].topSysBdCurrentTemperature) + I18N.COMMON.degreesCelsius + '  '+ I18N.EPON.bdSlot  +':' + i;
		titleStr = I18N.EPON.boardTemperature + ":" + (olt.slotList[i-1].slotDisplayTemp) + "@{unitConfigConstant.tempUnit}@" + '  '+ I18N.EPON.bdSlot  +':' + i;
	}
	$slot.attr({
		id : preType + "_" + i + "_0",
		checked:'invalid',
		title:titleStr
	});
	$slot.addClass("boardClass");
	$slot.css({
		height : coordinateParam.slotHeight,
		width : coordinateParam.slotWidth,
		position : 'absolute',
		left : coordinateParam.slotLeftStart,
		top : coordinateParam.slotTopStart + (i - 1) * coordinateParam.slotTopMargin + 0.37*i,
		backgroundImage : 'url(/epon/image/emptySlot8603.png)',
		zIndex : 1500,
		border : '1px solid transparent'
	});
	$("#OLT").append($slot);
	//*********type != 'slot‘：板卡类型*********//
	if(SLOT_BOARD != type){
		//---------actualType != preType == SLOT_BOARD:展示实际板卡-------//
		if (SLOT_BOARD == preType) {
			$slot.html('<font  title="normal" color="white" class="X-BOARD-TYPE boardTag">'+type.substring(0,2).toUpperCase()+'</font>');
		//---------preType != SLOT_BOARD ： 展示预配置板卡-------//			
		}else{
			$slot.html( '<font title="normal" color="white" class="X-BOARD-TYPE boardTag">'+preType.substring(0,2).toUpperCase()+'</font>' );
		}
	//***********type==SLOT_BOARD***********//	
	}else {
		//-----------actType == preType == SLOT_BOARD : no Tag----------//
		if (SLOT_BOARD == preType) {
			;
		//-----------actType  == 'slotslotTemp---//	
		}else{
			$slot.html( '<font title="normal" color="white" class="X-BOARD-TYPE boardTag">'+preType.substring(0,2).toUpperCase()+'</font>' );
		}
	}
	
	/**告警级别**/
	switch(slot.BAlarmStatus){	
		case 0 :
			$slot.find(".boardTag").attr({
				color : 'red',title : 'critical'
			});
		break;
		case 1 : 
			$slot.find(".boardTag").attr({
				color : 'orange',title : 'major'
			});
		break;
		case 2 :
			$slot.find(".boardTag").attr({
				color : 'yellow',title : 'minor'
			});
		break;
		case 3 : 
			$slot.find(".boardTag").attr({
				color : 'green',title : 'warning'
			});
		break;
		default : 
		break;
	}
	//************skip only when preType == type == SLOT_BOARD******//
	if (preType==type && type== SLOT_BOARD) {
		;
	}else{
		var lampStatus = slot.topSysBdLampStatus;
		var lampStatusString = lampStatus.toString(16);
		if(lampStatusString.length < 8){
		var rest = 8-lampStatusString.length;
		for(var st=0;st<rest;st++){
			lampStatusString = '0'.concat(lampStatusString);
		}
	}
	var MS_AND_HDD = parseInt(lampStatusString.substring(0,2));
	var MSB = MS_AND_HDD & 1;//(MS_AND_HDD & 1) >> 1; 
	var HDD = MS_AND_HDD >> 1 & 1; //(MS_AND_HDD & 2) >> 1;
	
	var RUN = parseInt(lampStatusString.substring(2,4),16);
	var ALARM = parseInt(lampStatusString.substring(4,6),16);
	//----DRAW LED---//
	switch(preType){
	case 'mpua'://4 led
	case 'mpub'://4 led
		drawSlotLight(ledColor[RUN],45 + 8,6,"circle2",$slot,"RUN");
		drawSlotLight(alertColor[ALARM==0?ALARM:ALARM-1],45 + 16,6,"circle3",$slot,"ALARM");
		drawSlotLight(ledColor[MSB],45 + 24,6,"circle4",$slot,"MS");
		if(preType == "mpub"){
			var MSB = parseInt(lampStatusString.substring(0,2),16) % 2;
			drawSlotLight(ledColor[HDD],45 + 32,6,"circle4",$slot,"HDD");
		}
		break;
	case 'geua':
	case 'epua':
	case 'xgua':
	case 'xgub':
	case 'gpua':
	default://3 led
		drawSlotLight(ledColor[RUN],45 + 8,6,"circle2",$slot,"RUN");
		drawSlotLight(alertColor[ALARM==0?ALARM:ALARM-1],45 + 16, 6, "circle3",$slot,"ALARM");
		break; 
	}
	//判断是否板卡是否加电,只要插入状态为installed就亮
	drawSlotLight(ledColor[slot.BPresenceStatus == 1 ? 1 : 0], 45, 6,"circle1",$slot,"PWR");
	WIN.$cont = null;
	}
	//****************preType != type : flag it with a  Icon****************//
	var tip,icon;
	
	if(preType != type  && (preType != 14 && type  != 11)) {
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
	if (preType != type && (preType != 14 && type  != 11)) {
		$icon = $("<img>");
		$icon.attr({
			src:icon,
			title: tip,
			width:16,
			height:16
		}).css({
			position:'absolute',
			top:coordinateParam.slotHeight-20,
			left: 363,
			zIndex:10000
		}).click(function(){
			return false;
		});
		$slot.append($icon);
	};
	//***************start listenning********// 
	$slot.bind('mouseover',function(e){
		switchNotation('BoardNotation');
		preventBubble(e);
		setBorderStyle(this.id);	   	   
	});
	$slot.bind('mouseout',function(e){
		preventBubble(e);	   
		$position = $("#"+this.id);
		var x = $position.offset().left;
		var y = $position.offset().top;
		cursor.X = e.pageX ;
		cursor.Y = e.pageY ;
		if(cursor.X>x+3 && cursor.X<x+coordinateParam.slotWidth-3 && cursor.Y>y+3 && cursor.Y<y+coordinateParam.slotHeight-3){	
			return;	  
		}
		clearBorderStyle(this.id);	 				  		   
	});
	return $slot;
}




/**********************************************************************
 * 定义创建未插入状态的端口
 * 
 * @param sID	槽位位置Id
 * @param pID	端口位置Id
 * @param pType	端口类型
 * @param Top	距离顶点的位置
 * @param sType	板卡类型
 * @param sPreType	板卡预配置类型 return $port对象
 *********************************************************************/
function createPort2(sID, pID, pType, left, sType,sPreType){
	$port = $("<div>");
	$port.attr({
		id : sType + "_" + sID + "_" +pID,
		portType:pType//标记端口类型
	});
	$port.addClass("portClass1");// 无效的端口样式类
	// 计算到最上边的距离
	left = left+coordinateParam.slotLeftStart;
	var top = coordinateParam.slotTopStart + (sID - 1) * coordinateParam.slotTopMargin + coordinateParam.portTopMargin;
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : left,	
		backgroundImage : 'url(/epon/image/' + pType + '/'+pType+ '.png)',	
		backgroundRepeat : 'no-repeat',
		opacity : 0.1,
		zIndex : 1500,
		border : '1px solid transparent'
	});
	$("#OLT").append($port);
	return $port;
}

/****************************************************************
 * 定义创端口的方法
 * 
 * @param sID 槽位位置Id
 * @param pID 端口位置Id
 * @param pType 端口类型
 * @param Top	距离顶点的位置
 * @param sType 板卡类型
 * @param sPreType 板卡预配置类型 return $port对象
 ***************************************************************/
function createPort(sID, pID, pType, left, sType,sPreType) {
    $port = $("<div>");
	$port.attr({
		id : sPreType + "_" + sID + "_" +pID,
		title : I18N.EPON.portType + ':' + pType + " "+ I18N.EPON.portLoc + ': '+pID,
		portSubType : pType//标记端口类型
	});
	$port.addClass("portClass"); 
	left = left+coordinateParam.slotLeftStart;
	var top = coordinateParam.slotTopStart + (sID - 1) * coordinateParam.slotTopMargin + coordinateParam.portTopMargin;
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : left,	
		overflow: 'visible',
		backgroundImage : 'url(/epon/image/' + pType + '/'+pType+'.png)',	
		backgroundRepeat : 'no-repeat', 
		border : '1px solid transparent',
		zIndex : 1500
	});
	$("#OLT").append($port);
    $port.bind('mouseover',function(e){
    	var arr = this.id.split("_");
		if(UPLINK_BOARD.contains(arr[0])){
			switchNotation('SniNotation');
		}
		if(DOWNLINK_BOARD.contains(arr[0])){
			switchNotation('PonNotation');
		}
    	preventBubble(e);
    	setBorderStyle(this.id);
    	var boardId = selectionModel.getBoardId(this.id);
        setBorderStyle( boardId ); //修改父板的透明度
        divCache.add( boardId );// 父板被修改了，故也要记录
	});
    $port.bind('mouseout',function(e){
	   preventBubble(e);
	   clearBorderStyle(this.id);
	});
    //端口状态
	var state,os,as,
	    port = olt.getItem(sID, pID);
	switch( port.portSubType ){
		//SNI MASSTYPE
		case 'geCopper':
		case 'geFiber':
		case 'xeFiber':
			os = port.sniOperationStatus;
			as = port.sniAdminStatus;
			break;
		//PON MASSTYPE
		case 'geEpon':
		case 'tengeEpon':
		case 'gpon':
			os = port.ponOperationStatus;
			as = port.ponPortAdminStatus;
			break;
		default:
	}
	if( as==1 && os==1 ){
	    state = 0;
	}else if( as==1 && os==2){
	    state = 1;
	}else{
	    state = 2;
	} 
	switch(state){
		case 0 :
			break;
		case 1 :
			$port.css({
				backgroundImage : 'url(/epon/image/'  + pType + '/'+pType+ '.gif)'
			});
			break;
		case 2 :
			$port.css({
				backgroundImage : 'url(/epon/image/' + pType + '/'+pType+ '.png)'
			});
			$state = $("<img>");
			$state.addClass("portStateClass");
			$state.attr({
				width : 12, height : 12,
				title : I18N.EPON.portnoservice
			});
			$state.css({
				position : 'absolute',
				top : 3, left : 3, opacity : 1, zIndex : 3500
			}); 
			$state.attr({
				src : '/epon/image/close.gif'
			});
			$port.append($state)	// 在板卡上添加端口状态
			break;
		default :
			break
	}
	return $port
}

/********************************
	 	创建电源
 *******************************/
function createPower(idx){
	$power = $("<div>");
	$power.attr({
		id : "power_"+idx+"_0",
		title: DOC.createElementNS ? " " : ""
	})
	
	$power.addClass("powerClass")
	$power.css({
		width : coordinateParam.powerWidth,
		height : coordinateParam.powerHeight,
		position : 'absolute',
		backgroundImage : 'url('+image_power+')',
		top : coordinateParam.powerTopMargin,
		zIndex:9990,
		left : coordinateParam.powerStartLeft+(idx-1)*coordinateParam.powerGapLeft,
		border : '2px solid transparent'
	});
	$("#OLT").append($power)
}

/******************************
  		创建风扇
 *****************************/
function createFan(idx){
	$fan = $("<div>");
	$fan.attr({
		id:"fan_"+idx+"_0",
		title : I18N.EPON.fanSpeed +  ": " + olt.fanList[0].topSysFanSpeed
	});
	$fan.addClass("fanClass")
	$fan.css({
		width : coordinateParam.fanWidth,
		height : coordinateParam.fanHeight,
		position : 'absolute',
		top : coordinateParam.fanTopMargin,
		left : coordinateParam.fanStartLeft+(idx-1)*coordinateParam.fanGapLeft,
		backgroundImage : 'url('+image_fan+')',	
		backgroundRepeat : 'repeat-x',
		opacity : 0.7
	})
	$("#OLT").append($fan)
}

/************************************************************************
 	 	将一个Index转换为一个页面可用的DivId param Index return DivId
 ***********************************************************************/
function IndexToId(idx){
	var index = idx.toString(16);
	//如果首位为0则补0。
	index = '000000'.concat(index);
	index = index.substring(index.length-10);
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

/************************************************************************
 	 将一个DivId转换为一个页面可用的Index param DivId return index
 ***********************************************************************/
function IdToIndex(Id){
	 var arr = Id.split("_")
	 return olt.slotList[parseInt(arr[1])-1].portList[parseInt(arr[2])-1].portIndex
}

/************************************************************************
 	 	根据传递来的端口divId得到端口类型
 ***********************************************************************/
function getPortType(divId){
	var arr = divId.split("_")
	return olt.slotList[arr[1]-1].portList[arr[2]-1].portSubType
}

/************************************************************************
 	 	将板卡类型号转成板卡类型名
 ***********************************************************************/
function slotNumToSlotType(slotNum){
	return bType[olt.slotList[parseInt(slotNum)].topSysBdPreConfigType]
}

/************************************************************************
  	修改设备状态，4个视图共用，都可以修改 param: index:唯一标识，状态号
 ***********************************************************************/
function displayEmergency(index,state){
	window.location.reload()
}
var coordinateParam = {
		oltHeight : 220+2,
	    oltWidth : 483+2,
	    
	    slotHeight : 23,
	    slotWidth : 407,
	    slotLeftStart : 53,
	    slotTopStart : 3.5,
	    slotTopMargin : 23.6,
	    
	    portHeight : 16,
	    portWidth : 15,
	    portTopMargin : 4,    
	    sniLeftStart : 45,
	    sniLeftMargin : 26,
	    
	    epuaFirstPon : 100,
	    epuaMargin1 : 50,
	    epuaMargin2 : 18,
	    
		geuaConsole : 89,
		geuaMgmt : 105,
		
		geuaSniE : 129,	
		geuaSniGapE : 16,
		geuaSniO :	199,
		geuaSniGapO1 : 18,
		geuaSniGapO2 : 50,
		
		xguaX01 : 160,
		xguaX02 : 188,
		mpuaSniE : 130,
		mpuaSniGap : 16,
		
		xgubX01 : 160,
		xgubX02 : 180,
		xgubX03 : 210,
		xgubX04 : 230,
		
		xgucX01 : 120,
		xgucX02 : 140,
		xgucX03 : 170,
		xgucX04 : 190,
		xgucX05 : 220,
		xgucX06 : 240,
		
		xpuaX01 : 160,
		xpuaX02 : 180,
		xpuaX03 : 210,
		xpuaX04 : 230,
		
		vpuaFirstPon : 80,
	    vpuaMargin1 : 33,
	    vpuaMargin2 : 16,
		
		mpuaConsole : 210,
		mpuaMgmt : 230,
		powerWidth :  131,
		powerHeight : 20,
		powerTopMargin : 196,
		powerStartLeft : 52,
		powerGapLeft : 131,
		
		fanWidth :  27,
		fanHeight : 213,
		fanTopMargin : 3.5,
		fanStartLeft : 21,
		fanGapLeft : 150,

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
var image_power = '/epon/image/power_8603.png';
var image_fan = '/epon/image/fan8602.gif';
var image_pon = '/epon/image/pon.png';
var image_sni = '/epon/image/sni.png';
var image_device = '/epon/image/8603.png';
