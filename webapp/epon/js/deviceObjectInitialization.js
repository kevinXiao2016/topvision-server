/**
 * 初始化OLT大对象，为没有赋值的属性赋予默认值 
 * @param：json raw data
 * @return new Object
 * @author bravin
 */
var Olt = function(){
	var o = this;
	o.currentId = "";
	o.slotCount = 0;
	o.oltType = 'PN8601';
	//**********INITILIZATION***********//
	o.init = function(json){
		try{
			//*****capture all error here and prevent default handler*****//
			//window.onerror = this.monitor.systemError;
			//-------------如果json数据不包含任何板卡，或者数据格式错误，则返回，并进行错误处理
			if (typeof json != 'object' || !(json.slotList instanceof  Array) ||json.slotList.length==0) {
				throw new Error(I18N.EPON.deviceNotAccess);
				return false;
			};
			this._init(json);
			return o;
		}catch(exp){
			throw new Error(I18N.EPON.entityError);
		}
	};
	o.monitor = {
		//***msg（错误消息）、url（发生错误的页面的 url）、line（发生错误的代码行）***//
		systemError:function(msg,url,line){
			//TODO GLOBAL HANDLE,dispatch Error.
			if (eval($("#loadingMask"))){
				$("#loadingMask").hide({},{
					complete:function(){
						$("#loadingMask").remove();		
					}
				});
			}
			return true;
		},
		propertyError:function(msg,url,line){
			//TODO property unexpected error
			return true; 
		},
		loadError:function(msg,url,line){
			//TODO error ocured in the loading procedure
			//window.parent.showMessageDlg('loadError', msg);
			//移除olt树，面板图等的点击事件，EXT默认的scope是this，但这里this其实是Olt类，故需要制定window scope
			Ext.getCmp("OltTreePanel").purgeListeners();
			//-----remove Root Node----------//
			//Ext.getCmp("OltTreePanel").getRootNode().remove();
			$("#device_container").empty();
			//-----if mask still exists,remove it-----//
			$("#OLT").unbind("click contextmenu");
			if (eval($("#loadingMask"))){
				$("#loadingMask").remove();
			}
			return true; 
		},
		handleError:function(){
			//TODO error ocured in the data handling procedure
			return true;
		},
		_disableNodeComponent:function(divId){}
	};
	//************清空已经存在的数据*****************//
	o.destoryData = function() {
		for (var attribute in olt){
			if(typeof attribute != 'function')
				//olt.remove(attribute);
				delete olt.attribute;
		}
	};
	o.changePortState = function (divId,state){
			//移除状态2的样式
	    $port = $("#"+divId);
	   	$port.find(".portStateClass").remove();
	    var portType = $port.attr("portSubType");
	    //移除状态1的样式
	    switch(portType){
	    	case 'geCopper':
	        		$port.css({
	        			backgroundImage:'url(/epon/image/geCopper/geCopper.png)'
	        		});
	        		break;
			case 'geFiber':
					$port.css({
	    				backgroundImage:'url(/epon/image/geFiber/geFiber.png)'
	    			});
	    			break;
			case 'xeFiber':
					$port.css({
	    				backgroundImage:'url(/epon/image/xeFiber/xeFiber.png)'
	    			});
	   				break;
	    	case 'geEpon':
			case 'tengeEpon':
			case 'gpon':
		    		$port.css({
		    			backgroundImage:'url(/epon/image/geEpon/geEpon.png)'
					});
		    		break;
		}
		switch(state){
			case 0 :
				$port.css({
					backgroundImage : 'url(image/' + portType +'/'+portType+ '.gif)'
				});		
				break;
			case 1 :
				$port.css({
					backgroundImage : 'url(image/' + portType +'/'+portType+ '.png)'
				});				
				break;
			case 2 :
				$state = $("<img>");
				$state.attr({
					width : 12,
					height : 12,
					title : I18N.EPON.portUnavailable		
				});
				$state.css({
					position : 'absolute',
					top : 3,
					left : 3,			
					opacity : 1,
					zIndex : 15000
				}); 
				$state.attr({
					src : 'image/close.gif'
				 });
				$port.append($state);	// 在板卡上添加端口状态
				break;	
			default:
				break;
			
			}	
	};
	//*************得到某个component的数据对象******///
	o.getComponentObject = function(paramId){
		var array = paramId.split("_");	
		if ("fan" == array[0])
			return olt.fanList[array[1]-1];
		else if ("power" == array[0])
			return olt.powerList[array[1]-1];
		else if (0 == array[2])
			return olt.slotList[array[1]-1];
		else
			return olt.slotList[array[1]-1].portList[array[2]-1];
	};
	/****得到component真实Index****/
	o.getActualIndex = function(paramId) {
	 	this.currentId = paramId?paramId:this.currentId?this.id:document.currentId;
		var array = currentId.split("_");	
		if ("fan" == array[0])
			return olt.fanList[array[1]-1].fanCardId;
		else if ("power" == array[0])
			return olt.powerList[array[1]-1].powerCardId;
		else if (0 == array[2])
			return olt.slotList[array[1]-1].slotId;
		else
			return olt.slotList[array[1]-1].portList[array[2]-1].portId;
	};
	/****得到component真实Id****/
	o.getActualId = function(paramId) {
	 	this.currentId = paramId?paramId:this.currentId?this.id:document.currentId;
		var array = currentId.split("_");
		if ("fan" == array[0])
			return olt.fanList[array[1]-1].fanCardIndex;
		else if ("power" == array[0])
			return olt.powerList[array[1]-1].powerCardIndex;
		else if (0 == array[2])
			return olt.slotList[array[1]-1].slotIndex;
		else
			return olt.slotList[array[1]-1].portList[array[2]-1].portIndex;
	};
	//******METHOD FOR INTERNAL USAGE****************//
	o._init = function(json){
		/*******START OF OLT ATTRIBUTES INITIALIZATION*****/
		this.entityId = json.entityId;
		this.ip = json.ip || '127.0.0.1';
		this.location = json.location || '';
		this.locationCnName = json.locationCnName || '';
		this.oltDeviceNumOfTotalFanSlot = json.oltDeviceNumOfTotalFanSlot || '';
		this.oltDeviceNumOfTotalPowerSlot = json.oltDeviceNumOfTotalPowerSlot || '';
		this.oltDeviceNumOfTotalServiceSlot = json.oltDeviceNumOfTotalServiceSlot || '';
		this.oltDeviceStyle = json.oltDeviceStyle  || '';
		this.oltDeviceUpTime = json.oltDeviceUpTime || 0;
		this.oltName = json.oltName || 'OLT';
		//----------------ONLY PN8601 PN8602 ARE AVAILABLE NOW----------------//
		this.oltType = json.oltType || 'PN8601';
		this.onuAuthenticationPolicy = json.onuAuthenticationPolicy || 1;
		this.stpGlobalSetEnable = json.stpGlobalSetEnable || 1;
		this.sysDescr = json.sysDescr || 'OLT';
		this.systemOUI = json.systemOUI || 'topvision';
		this.topSysOltFrameNum = json.topSysOltFrameNum || 1;
		this.topSysOltRackNum = json.topSysOltRackNum || 1;
		this.topSysSnmpVersion = json.topSysSnmpVersion || 2;
		this.vendorName = json.vendorName || 'topvision';
		//--------IF type==PN8601,slotcount=18 else 3------------//
		this.slotCount = this.oltType == 'PN8601'?18:this.oltType == 'PN8602'?3:this.oltType == 'PN8603'?8:18;
		/****END OF OLT ATTRIBUTES INITIALIZATION**/
		
		/*********START OF SLOTS INITIALIZATION**********/
		this.slotList = new Array();
		for (var cursor=0,slotCursor=0; cursor < this.slotCount; cursor++) {
			//--------MAKE SURE realIndex always increment--------//
			if (slotCursor > json.slotList[slotCursor].slotRealIndex-1){
				//alert("SLOTS NEED SORTED");
			}
			///----------ONLY > or = WAS PERMITTED  and slotRealIndex starts from 1--------------//
			//TODO 隐含BUG：当最后一条记录不是最后一个板卡时发生下标越界
			try{
			switch(json.slotList[slotCursor].slotRealIndex-1 == cursor){
				case true:
					//------------CREATE A NEW SLOT OBJECT BY COPY----------//
					var slot = this.slotList[cursor] = new Object();
					var _slot = json.slotList[slotCursor];
						//--------- the reserved bytes are set to 0.----------//
						// 胡乔说的 -1 表示没有告警，0是最高级别的告警
						slot.BAlarmStatus = _slot.BAlarmStatus || -1;
						slot.BAdminStatus = _slot.BAdminStatus || 2;
						slot.bOperationStatus = _slot.bOperationStatus || 2;
						slot.slotLogicNum = _slot.slotLogicNum;
						//--------active(1), standby(2), standalone(3), notApplicable(4)---//
						slot.BAttribute = _slot.BAttribute || 4;
						slot.BFirmwareVersion = _slot.BFirmwareVersion || '';
						slot.BHardwareVersion = _slot.BHardwareVersion || '';
						slot.BName = _slot.BName || '';
						//-------installed(1), notInstalled(2), others(3)------//
						slot.BPresenceStatus = _slot.BPresenceStatus || 2;
						slot.BSerialNumber = _slot.BSerialNumber || '';
						slot.BSoftwareVersion = _slot.BSoftwareVersion || '';
						slot.BUpTime = _slot.BUpTime || 0;
						slot.changeTime = _slot.changeTime || 0;
						//标识 板卡预配置与实际类型是否一致
						slot.flag = _slot.flag;
						slot.slotId = _slot.slotId;
						slot.slotIndex = _slot.slotIndex;
						slot.slotNum = _slot.slotNum;
						slot.slotRealIndex = _slot.slotRealIndex;
						slot.topSysBdCPUUseRatio = _slot.topSysBdCPUUseRatio || 0;
						slot.topSysBdCurrentTemperature = _slot.topSysBdCurrentTemperature || 0;
						slot.topSysBdFreeFlashOctets = _slot.topSysBdFreeFlashOctets || 0;
						slot.topSysBdFreeMemSize = _slot.topSysBdFreeMemSize || 0;
						slot.topSysBdLampStatus = _slot.topSysBdLampStatus || 0;
						slot.topSysBdPreConfigType = _slot.topSysBdPreConfigType || 0;
						slot.topSysBdlMemSize = _slot.topSysBdlMemSize || 0;
						slot.topTotalFlashOctets = _slot.topTotalFlashOctets || 0;
						slot.topSysBdTempDetectEnable = _slot.topSysBdTempDetectEnable || 0;
						//slot.type = _slot.type || 0;
						//-----必须判断是否等于1而不能等于2，因为有可能猜不到其值---//
						//-----如果板卡在位就使用其真实值，否则设置其实际类型为空板处理-----//
						// TODO 有争议的字段，不应该再使用
						slot.type = slot.BPresenceStatus == 1 ? _slot.type : 0; 
						//---non-board(0), mpua(1), mpub(2), epua(3), epub(4), geua(5), geub(6), xgua(7), xgub(8), unknown(255)--//
						//----当前仅当板卡的在位状态为开启时才使用其真正的实际类型-----//
						// TODO 当板卡不在位的时候  只能为空板，应该直接使用它的实际类型
						//slot.topSysBdActualType = slot.BPresenceStatus == 1 ? _slot.topSysBdActualType : 0;
						slot.topSysBdActualType = _slot.topSysBdActualType ;
						//--------PORTS ARE ARRAY-----------//
						slot.portList = new Array();
						for (var cursorB=0,portCursor=0; cursorB < _slot.portList.length; cursorB++) {
							///----------ONLY > or = WAS PERMITTED--------------//
							if (portCursor > _slot.portRealIndex-1){
								//alert("SLOTS NEED SORTED");
							}
							switch(_slot.portList[portCursor].portRealIndex-1 == cursorB){
								case true:
										var port = this.slotList[cursor].portList[cursorB] = new Object();
										var _port  = _slot.portList[portCursor];
										//--------MAKE portCursor MOVE FORWARD--------//
										portCursor++;
										port.SMaxDsBandwidth = _port.SMaxDsBandwidth || 0;
										port.SMaxUsBandwidth = _port.SMaxUsBandwidth || 0;
										port.actualDsBandwidthInUse = _port.actualDsBandwidthInUse || 0;
										port.actualUsBandwidthInUse = _port.actualUsBandwidthInUse || 0;
										port.maxDsBandwidth = _port.maxDsBandwidth || 0;
										port.maxUsBandwidth = _port.maxUsBandwidth || 0;
										//--------- true(1), false(2) ---------------//
										port.perfStats15minuteEnable = _port.perfStats15minuteEnable || 2;
										//--------- true(1), false(2) ---------------//
										port.perfStats24hourEnable = _port.perfStats24hourEnable || 2;
										//---------- up(1), down(2), testing(3)------------//
										port.ponOperationStatus = _port.ponOperationStatus || 2;
										//---------- up(1), down(2), testing(3)------------//
										port.ponPortAdminStatus = _port.ponPortAdminStatus || 2;
										port.ponPortEncryptKeyExchangeTime = _port.ponPortEncryptKeyExchangeTime || 0;
										//-----------aes128(1), ctcTripleChurning(2), other(3)------//
										port.ponPortEncryptMode = _port.ponPortEncryptMode || 3;
										//-------------true(1), false(2)------------//
										port.ponPortIsolationEnable = _port.ponPortIsolationEnable || 2;
										//----------------value 0 means no limit----------------//
										port.ponPortMacAddrLearnMaxNum = _port.ponPortMacAddrLearnMaxNum || 0;
										port.ponPortMaxOnuNumSupport = _port.ponPortMaxOnuNumSupport || 32;
										//-------------- ge-epon(1), tenge-epon(2), gpon(3), other(4)-------//
										port.ponPortType = _port.ponPortType;
										//----标记它是否是PON保护组备用端口，如果是备用端口，在前端将无法进行任何操作--//
										port.isStandbyPort = _port.standbyPort;
										port.ponPortUpOnuNum = _port.ponPortUpOnuNum || 0;
										port.ponPortUpOnuNum = _port.ponPortUpOnuNum || 0;
										//表示PON端口工作模式，是否采用2G模式 ,默认采用1G模式
										port.ponSpeedMode = _port.ponSpeedMode || 2;

										port.portId = _port.portId || '';
										port.portIndex = _port.portIndex || '';
										port.portRealIndex = _port.portRealIndex || '';
										port.portSubType = _port.portSubType || '';
										port.remainDsBandwidth = _port.remainDsBandwidth || '';
										port.remainUsBandwidth = _port.remainUsBandwidth || '';
										port.slotRealIndex = _port.slotRealIndex || '';
										port.sniAdminStatus = _port.sniAdminStatus || 2;
										//-- auto-negotiate(1), half-10(2), full-10(3), half-100(4), full-100(5), full-1000(6), full-10000(7)--//
										port.sniAutoNegotiationMode = _port.sniAutoNegotiationMode || 1;
										//-- auto-negotiate(1), half-10(2), full-10(3), half-100(4), full-100(5), full-1000(6), full-10000(7)--//
										port.sniAutoNegotiationStatus = _port.sniAutoNegotiationStatus || 'auto-negotiate';
										//-------------------true(1), false(2)------------------//
										port.sniIsolationEnable = _port.sniIsolationEnable || 1;
										//--------------HOW?------------------//
										port.sniLastStatusChangeTime = _port.sniLastStatusChangeTime || 0;
										//--------------value 0 means no limit-----------------//
										port.sniMacAddrLearnMaxNum = _port.sniMacAddrLearnMaxNum;
										//--------------twistedPair(1), fiber(2), other(3)----//
										port.sniMediaType = _port.sniMediaType || 3;
										//--------------up(1), down(2), testing(3)------------//
										port.sniOperationStatus = _port.sniOperationStatus || 2;
										//--------------up(1), down(2), testing(3)------------//
										port.sniPerfStats15minuteEnable = _port.sniPerfStats15minuteEnable || 2;
										//-------------- true(1), false(2)--------------------//
										port.sniPerfStats24hourEnable = _port.sniPerfStats24hourEnable || 2;
										port.sniPortName = _port.sniPortName || '';
										port.stpPortEnabled = _port.stpPortEnabled;
										port.stpPortRstpProtocolMigration = _port.stpPortRstpProtocolMigration;
										port.topSniAttrCardNo = _port.topSniAttrCardNo;
										port.topSniAttrEgressRate = _port.topSniAttrEgressRate;
										port.topSniAttrFlowCtrlEnable = _port.topSniAttrFlowCtrlEnable;
										port.topSniAttrIngressRate = _port.topSniAttrIngressRate;
										port.topSniAttrPortNo = _port.topSniAttrPortNo;
								break;
							case false:
								//-----------TAKE A SEAT BUT NOT  HANDLABLE----------//
								var port = this.slotList[cursor].portList[cursorB] = new Object();
									port.portSubType = 'errorPort';
								break;
						//****END OF SWITCH*************//
						};
					//****END OF LOOP*************//
					}
					//---------MAKE SURE slotCursor MOVE FORWARD----------//
					slotCursor++;
					break;
				case false:
					//--------CONSTRUCT A NEW SLOT OBJECT MANULY----//
					var slot = this.slotList[cursor] = new Object();
						slot.slotRealIndex = cursor+1;
						//-------DEFAULT VALUE---------//
						slot.BAlarmStatus = 4;
						slot.BAttribute = 4;
						slot.BFirmwareVersion = "";
						slot.BHardwareVersion = "";
						slot.BName = "";
						slot.BPresenceStatus = 	2;
						slot.BSerialNumber = "";
						slot.BSoftwareVersion = "";
						slot.BUpTime = 0;
						slot.changeTime = 0;
						slot.flag = 0;
						slot.slotId = 0;
						slot.slotIndex = 0;
						slot.slotNum = 0;
						slot.slotRealIndex = cursor+1;
						slot.topSysBdActualType = 0;
						slot.topSysBdCPUUseRatio = 0;
						slot.topSysBdCurrentTemperature = 0;
						slot.topSysBdFreeFlashOctets = 0;
						slot.topSysBdFreeMemSize = 0;
						slot.topSysBdLampStatus = 0;
						//------现在是以preconfigtype做为要展示的类型，故此处preconfig为255.
						//slot.topSysBdPreConfigType = 0;
						slot.topSysBdPreConfigType = 255;
						slot.topSysBdlMemSize = 0;
						slot.topTotalFlashOctets = 0;
						slot.type = 255;
						//----PORTS MUST BE NONE--------//
						slot.portList = [];
					//------slotCursor SHOULD NOT BE forward----//
					break;
			}
		// -----ENF OF LOOP----///
			}catch(e){}
		}
		/*********START OF FANS INITIALIZATION : 3 FANS SHOULD LOOK LIKE JUST 1 FAN**********/
		this.fanList = new Array();
			switch(json.fanList.length){
			case 0:
//				var fan = this.fanList[0] = new Object();
//				fan.valid = false;
				break;
			default:
				var fan = this.fanList[0] = new Object();
				var _fan = json.fanList[0];
				//胡乔说的 -1 表示没有告警，0是最高级别的告警
				fan.fanCardAlarmStatus = _fan.fanCardAlarmStatus || -1;
				fan.fanCardId = _fan.fanCardId;
				fan.fanCardIndex = _fan.fanCardIndex;
				fan.fanCardName = _fan.fanCardName || "fan"; //default: "fan"
				fan.fanCardNum = _fan.fanCardNum || 0;//default:position 0
				fan.fanCardOperationStatus = _fan.fanCardOperationStatus || 2;//default: down(2) up(1)
				fan.fanCardPresenceStatus = _fan.fanCardPresenceStatus || 2;//default: down(2) up(1)
				fan.fanCardRealIndex = _fan.fanCardRealIndex;
				fan.topSysFanSpeed = _fan.topSysFanSpeed;
				fan.topSysFanSpeedControl = _fan.topSysFanSpeedControl;
					var changeTime = this.fanList[0].changeTime = new Object();
					changeTime.date = _fan.changeTime.date || 0;
					changeTime.day = _fan.changeTime.day || 0;
					changeTime.hours = _fan.changeTime.hours || 0;
					changeTime.minutes = _fan.changeTime.minutes || 0;
					changeTime.month = _fan.changeTime.month || 0;
					changeTime.nanos = _fan.changeTime.nanos || 0;
					changeTime.seconds = _fan.changeTime.seconds || 0;
					changeTime.time = _fan.changeTime.time || 0;
					changeTime.timezoneOffset = _fan.changeTime.timezoneOffset || 0;
					changeTime.year = _fan.changeTime.year || 0;
				break;
			}
		/*********START OF POWER INITIALIZATION**********/
		this.powerList = new Array();
		//-----EVERY OLT HAS 2 POWER AT MOST : AT PRESENT,power's position is not sure---------//
		for (var cursor=0; cursor < json.powerList.length; cursor++) {
			var power = this.powerList[cursor] = new Object();
			var _power = json.powerList[cursor];
				//胡乔说的 -1 表示没有告警，0是最高级别的告警
				power.powerCardAlarmStatus = _power.powerCardAlarmStatus || -1;
				power.powerCardId = _power.powerCardId;
				power.powerCardIndex = _power.powerCardIndex;
				power.powerCardName = _power.powerCardName;
				power.powerCardNum = _power.powerCardNum;
				//--------installed(1), notInstalled(2), others(3)--------------//
				power.powerCardPresenceStatus = _power.powerCardPresenceStatus;
				power.powerCardRealIndex = _power.powerCardRealIndex;
				power.topSysPowerSupplyACTemperature = _power.topSysPowerSupplyACTemperature || 0;
				power.topSysPowerSupplyACVoltage = _power.topSysPowerSupplyACVoltage || 0;
				//--------powerDC(1), powerAC(2)-------------//
				power.topSysPowerSupplyType = _power.topSysPowerSupplyType || 0;
				var changeTime = this.powerList[cursor].changeTime = new Object();
					changeTime.date = _power.changeTime.date || 0;
					changeTime.day = _power.changeTime.day || 0;
					changeTime.hours = _power.changeTime.hours || 0;
					changeTime.minutes = _power.changeTime.minutes || 0;
					changeTime.month = _power.changeTime.month || 0;
					changeTime.nanos = _power.changeTime.nanos || 0;
					changeTime.seconds = _power.changeTime.seconds || 0;
					changeTime.time = _power.changeTime.time || 0;
					changeTime.timezoneOffset = _power.changeTime.timezoneOffset || 0;
					changeTime.year = _power.changeTime.year || 0; 
		}
	//**********END OF INIT FUNCTION*********//
	}
	/**
	 * 实时获取光信息状态值
	 */
	o.initOltPortOptical = function(){
		var url = '/epon/optical/getOltAllPortOptical.tv?entityId=' + getEntityId() + '&r=' + Math.random();
		$.ajax({
			url : url, async:false,
			success : function(txt) {
				if(txt == 'false'){
					oltPortOptical = new Array();
					return;
				}
				oltPortOptical = Ext.util.JSON.decode(txt).data || new Array();
			},
			error : function() {
				oltPortOptical = new Array();
			}
		});
	}
	o.loadPortOptical = function(portIndex, portType){
		var list = [null, null, null, null, null, null,null];
		var fiberType = [I18N.Optical.unknownType, "GBIC", I18N.Optical.moduleType, "SFP"];
		var tmpIndex = -1;
		for(var a=0; a<oltPortOptical.length; a++){
			if(oltPortOptical[a].portIndex == portIndex){
				tmpIndex = a;
				list = [oltPortOptical[a].identifier && parseInt(oltPortOptical[a].identifier) < 4 && 
						parseInt(oltPortOptical[a].identifier) > 0 ? fiberType[oltPortOptical[a].identifier] : null, 
						oltPortOptical[a].vendorName ? oltPortOptical[a].vendorName : null, 
						oltPortOptical[a].waveLength ? (oltPortOptical[a].waveLength == 0 ? null : oltPortOptical[a].waveLength + "  nm") : null, 
						oltPortOptical[a].vendorPN ? oltPortOptical[a].vendorPN : null, 
						oltPortOptical[a].vendorSN ? oltPortOptical[a].vendorSN : null, 
						oltPortOptical[a].dateCode ? oltPortOptical[a].dateCode : null,
						parseInt(oltPortOptical[a].bitRate) > 0 ? oltPortOptical[a].bitRate*100 + " Mb/s" : null];
				break;
			}
		}
		var url = (portType ? '/epon/optical/loadOltPonOptical.tv?m=' : '/epon/optical/loadOltSniOptical.tv?m=') + Math.random();
		var par = {entityId: getEntityId(), portIndex: portIndex};
		var tmpFlag = ++opticalFlag;
		var tmpSource = [];
		$.ajax({
			url : url, async:false,
			success : function(txt) {
				if(txt == 'false'){
					return;
				}
				if(tmpFlag == opticalFlag && tmpIndex > -1){
					var data = Ext.util.JSON.decode(txt).data || new Array();
					oltPortOptical[tmpIndex] = {portIndex: portIndex, identifier: data[0], vendorName: data[1], waveLength: data[2],
							vendorPN: data[3], vendorSN: data[4], dateCode: data[5],bitRate:data[11]};
					//var tmpSource = grid.getSource();
					tmpSource[tmpSource.length] = data[0] && parseInt(data[0]) > 0 && parseInt(data[0]) < 4 ? fiberType[data[0]] : null;
					tmpSource[tmpSource.length] = data[1] ? data[1] : null;
					tmpSource[tmpSource.length] = data[2] && data[2] != 0 ? data[2] + "  nm" : null;
					tmpSource[tmpSource.length] = data[3] ? data[3] : null;
					tmpSource[tmpSource.length] = data[4] ? data[4] : null;
					tmpSource[tmpSource.length] = data[5] ? data[5] : null;
					tmpSource[tmpSource.length] = data[6] && parseInt(data[11]) > 0? data[11]*100 + " Mb/s" : null;
					//grid.setSource(tmpSource);
				}
			},
			error : function() {
			},
			data : par
		});
		return tmpSource;
		//return list;
	}
//*******END OF OLT DEFINITION****///
};
var onuAuthenData = null;
