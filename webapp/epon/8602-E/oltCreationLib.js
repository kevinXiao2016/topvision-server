function createMEUABoard(i) {
	var slot = olt.slotList[i - 1];
	createBoard(slot.slotRealIndex, bType[slot.topSysBdActualType], bType[slot.topSysBdPreConfigType]);
    for (var j = 0; j < 26 ; j++) {
        try{
			var port = slot.portList[j];
			createPort(port, slot.slotRealIndex, port.portRealIndex, port.portSubType, j%2 == 1 ? 50 : 23, getLeft(port.portRealIndex), bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }catch(e){
        	console.log(e)
        }
    }
}

function createMEUBBoard(i) {
	var slot = olt.slotList[i - 1];
	createBoard(slot.slotRealIndex, bType[slot.topSysBdActualType], bType[slot.topSysBdPreConfigType]);
    for (var j = 0; j < slot.portList.length ; j++) {
        try{
			var port = slot.portList[j];
			createPort(port, slot.slotRealIndex, port.portRealIndex, port.portSubType, j%2 == 1 ? 50 : 23, getLeft(j), bType[slot.type],bType[slot.topSysBdPreConfigType]);
        }catch(e){
        	console.log(e)
        }
    }
}

function getLeft(j){
	var ponstart,snistart;
	if(olt.type == 'meub'){
		ponstart = 263;
		snistart = 162;
		if(j>7){//sni
			return Math.floor((j-8)/2) * 20 + snistart + 82;
		}else{//pon
			return Math.floor(j/2) * 20 + ponstart + 82;
		}
	}else{
		j--;
		ponstart = 347;
		snistart = 188;
		if(j > 15){//sni
			if( j> 17){
				return Math.floor((j-18)/2) * 20 + snistart+55;
			}else{
				return Math.floor((j-16)/2) *20 + snistart ; 
			}
		}else{//pon
			if( j > 7){
				return Math.floor((j-6)/2) * 20 + ponstart + 82;
			}else{
				return Math.floor(j/2) * 20 + ponstart;			
			}
		}
	}
}

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


/**
 * 定义创建PN8602面板图方法的入口
 * 
 * @param json
 */
function createPN8602E() {
	var $pretype = olt.slotList[0].topSysBdPreConfigType;
	if($pretype==18){
		image_device = "/epon/image/pn8602_MEUB.png";
		olt.type = 'meub';
	}
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
            case 17 :
            	createMEUABoard(i);
            	break;
            case 18 :
            	createMEUBBoard(i);
            	break;
            default :
                createNonBoard(i);
                break;
        }
    }// end of loop
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
		top : coordinateParam.slotTopStart,
		left : coordinateParam.slotLeftStart,
		backgroundImage : 'url(/epon/image/emptySlot.png)',
		border : '1px solid transparent',
		zIndex : 990
	});
	$("#OLT").append($slot);

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
		var lamptop =65;
		var lampleft = 31;
		var margin = 9;
		drawSlotLight(ledColor[RUN], lampleft + margin, lamptop,"circle2",$slot,"RUN");
		drawSlotLight(alertColor[ALARM==0?ALARM:ALARM-1], lampleft + 2*margin, lamptop, "circle3",$slot,"ALARM");
		//判断是否板卡是否加电,只要插入状态为installed就亮
		drawSlotLight(ledColor[slot.BPresenceStatus == 1 ? 1 : 0], lampleft, lamptop,"circle1",$slot,"PWR");
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
function createPort(port, sID, pID, pType, top,left, sType,sPreType) {
    $port = $("<div>");
	$port.attr({
		id : sPreType + "_" + sID + "_" +pID,
		title : I18N.EPON.portType + ':'+pType+ " " + I18N.EPON.portLoc + ': '+getPortNoByType(sPreType, pID),
		portSubType:pType
	});
	$port.addClass("portClass"); 
	$port.css({
		width : coordinateParam.portWidth,
		height : coordinateParam.portHeight,
		position : 'absolute',
		top : top,
		left : left,
		backgroundImage : 'url(/epon/image/'+ pType + '/'+pType+ '.png)',
		backgroundRepeat : 'no-repeat',
		border: '1px solid transparent',
		zIndex : 1500
	});
	$("#OLT").append($port);
    $port.bind('mouseover',function(e){
    	var arr = this.id.split("_");
		/*if( UPLINK_BOARD.contains(arr[0]) ){
			switchNotation('SniNotation');
		}
		if( DOWNLINK_BOARD.contains(arr[0]) ){
			switchNotation('PonNotation');
		}*/
    	var num = parseInt(arr[2], 10);
    	var maxNum;
        switch (sPreType){
        	case 'meub':
        		maxNum = 9;
        		break;
        	case 'meua':
        		maxNum = 17;
        		break;
        }
    	if(num >= maxNum){
    		switchNotation('SniNotation');
    	}else{
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
	switch(port.portSubType){
		//SNI MASSTYPE
		case 'geCopper'://NO NEED TO HANDLE
		case 'geFiber'://NO NEED TO HANDLE
		case 'xeFiber':
			var os = port.sniOperationStatus;
			var as = port.sniAdminStatus;
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
			var os = port.ponOperationStatus;
			var as = port.ponPortAdminStatus;
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
				zIndex : 9999
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
	var $portList = olt.slotList[slot].portList;
	for(var i=0 ;i < olt.slotList.length && slot == 0;i++){
		//---由于只有主用MPUA板上有端口可以画，所以即便备用主控板的slotIndex为255也对解析没有影响---//
		if(olt.slotList[i].slotIndex == 0){
			slot = olt.slotList[i].slotRealIndex;
		}
	}
	var port = parseInt(index.substring(2,4),16);
	$.each($portList,function(i,$port){
		if($port.portRealIndex == port){
			port = i+1;
			return false;
		}
	});
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
		oltHeight : 85,
	    oltWidth : 600,
	    oltTop : 100,
	    oltLeft : 0,

	    slotHeight : 80,
	    slotWidth : 540,
	    slotLeftStart : 30,
	    slotTopStart : 5,
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
var image_device = '/epon/image/pn8602e.png';
var image_board = '/epon/image/board8602.png';
//var image_power = 'image/power.png';
var image_fan = '/epon/image/fan8602.png';
var image_pon = '/epon/image/pon.png';
var image_sni = '/epon/image/sni.png';
var boardColor = "#71787e";