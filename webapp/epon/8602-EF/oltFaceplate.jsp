<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML vmlSupport="true">
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    PLUGIN Nm3kTabBtn
    MODULE epon
    IMPORT epon.javascript.EponViewUtil
    IMPORT epon.javascript.SlotConstant
    IMPORT epon.javascript.EponDeviceHelper
    IMPORT epon.javascript.Olt static
    IMPORT epon.8602-EF.oltCreationLib
    IMPORT epon.javascript.EponDeviceAction
    IMPORT epon.javascript.GponDeviceAction
    IMPORT epon.js.oltFaceplateTrap
    IMPORT epon.js.oltFaceplateOnlyTrap
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
v\:oval { behavior: url(#default#VML);display:inline-block } 
.X-BOARD-TYPE{font-weight:bold;position:absolute;left:20px;top:5px;z-index:5000;}
.UN-ACCESSABLE{filter:alpha(opacity=20);opacity:0.2;}
</style>
<script type="text/javascript">
var json;
var grid;
var entityId = '${entity.entityId}';
var entityName = '${entity.name}';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var cameraSwitch = '${cameraSwitch}';
/*********STARTING*********/
var olt;
var CASCADE_LOCK = false ;
var treeFlag = false;
var ctrlFlag = 0;
var divCache = new Array();
var attrStore;
var navigatorTree;
var currentId; //记录当前点击的divID，解析ID得到slot，pon口传给弹出页面
var NOTATION_FLAG = true;
//设备的IGMP模式
var deviceIgmpMode = ${deviceIgmpMode};  
//板卡告警状态中文
var ch_bAlarmStatus = {
	'0' : '@EPON.criticalAlert@' ,
	'1' : '@EPON.majorAlert@',
	'2' : '@EPON.minorAlert@',
	'3' : '@EPON.warnAlert@',
	'4' : '@EPON.noAlert@'
}

/**************************************
 	Switch the View & Entity
 *************************************/
var opticalFlag = 0;
var oltPortOptical = new Array();
function reloadOltJson() {
	window.location.reload();
}
function showItemProperty(entityname) {
    opticalFlag++;
    var entityObject = null;
    if (entityname != "OLT") {
        var index = entityname.split("_");
        var slotType = index[0];
        var slotIndex = index[1];
        var portIndex = index[2];
        var slot = olt.slotList[slotIndex-1];
        if(slotType == "power"){
        	var _power = olt.powerList[slotIndex - 1];
        	entityObject = {
	            '@EPON.powerName@' : _power.powerCardName,
	            '@EPON.powerType@' : _power.topSysPowerSupplyType,
	            '@EPON.powerVolt@' : _power.topSysPowerSupplyACVoltage+"V",
	            '@EPON.powerTempe@' : _power.topSysPowerSupplyACTemperature==0 ? null : _power.powerDisplayTemp + "@{unitConfigConstant.tempUnit}@"
            };
            entityObject = {};//屏蔽电源属性
        }else{
            if (portIndex == 0) {//board
               if (UPLINK_BOARD.contains(slotType) || CONTROL_BOARD.contains(slotType)) {//GE
                    entityObject = {
						  '@EPON.slotNum@' : slot.slotRealIndex,
						  '@EPON.preType@' :  slotType == SLOT_BOARD ? I18N.EPON.none :slotType.toUpperCase(),
					      '@EPON.installedType@' :bActualType[slot.topSysBdActualType] == SLOT_BOARD ? I18N.EPON.none : bActualType[slot.topSysBdActualType].toUpperCase(),
					      '@EPON.slotStatus@' :slot.BPresenceStatus,
					      '@EPON.controlStatus@' : slot.bOperationStatus,
					      '@EPON.boardName@' :slot.BName,
					      '@EPON.boardSN@':slot.BSerialNumber == "" ? I18N.EPON.none : slot.BSerialNumber,
					      '@EPON.softwareVersion@' :slot.BSoftwareVersion,
					   	   //-----主控板固件版本禁止读取(8602的GE板做主控板)-----//
					       //'@EPON.hardwareVersion@':olt.slotList[slotIndex - 1].BFirmwareVersion,
					      '@EPON.hardwareVersion@' :slot.BHardwareVersion,
					      '@EPON.boardOnlineTime@' :timeFormat(slot.BUpTime*10 + (new Date()).getTime() - slot.changeTime),//暂时多减8小时
					      '@EPON.cpuUsage@' : slot.topSysBdCPUUseRatio+"%",
					      '@EPON.totalMem@' :Math.floor(slot.topSysBdlMemSize/1048576)+"MB",
					      '@EPON.freeMem@' :Math.floor(slot.topSysBdFreeMemSize/1048576)+"MB",
					   	   //'@EPON.totalFlash@' :Math.floor(olt.slotList[slotIndex - 1].topTotalFlashOctets/1048576)+"MB",
					        //'@EPON.lastFlash@' :Math.floor(olt.slotList[slotIndex - 1].topSysBdFreeFlashOctets/1048576)+"MB" ,
					      '@EPON.boardTemperature@' :(slot.topSysBdCurrentTemperature==127 || olt.slotList[slotIndex - 1].topSysBdTempDetectEnable==2)?null:olt.slotList[slotIndex - 1].slotDisplayTemp
					              +" "+ "@{unitConfigConstant.tempUnit}@",
					      '@EPON.bdAlertStatus@' :ch_bAlarmStatus[slot.BAlarmStatus]
                    };
                    if(2 == slot.BPresenceStatus){                    	
                    	delete entityObject[I18N.EPON.totalMem]
                    	delete entityObject[I18N.EPON.freeMem]
                    	delete entityObject[I18N.EPON.boardOnlineTime]
                    	delete entityObject[I18N.EPON.softwareVersion]
                    	delete entityObject[I18N.EPON.hardwareVersion]
                    	delete entityObject[I18N.EPON.boardName]
                    	delete entityObject[I18N.EPON.firewareVersion]
                    	delete entityObject[I18N.EPON.boardSN]
                    }
                } else if(DOWNLINK_BOARD.contains(slotType)){//EP
                	entityObject = {
                   		'@EPON.slotNum@' :slot.slotRealIndex,
                   		'@EPON.preType@' :slotType == SLOT_BOARD ? I18N.EPON.none : slotType.toUpperCase(),
                        '@EPON.installedType@' :bType[slot.topSysBdActualType] == SLOT_BOARD ? I18N.EPON.none : bType[slot.topSysBdActualType].toUpperCase(),
               		 	'@EPON.slotStatus@' :slot.BPresenceStatus,
                        '@EPON.controlStatus@' : slot.bOperationStatus,
                        '@EPON.bdTypeUnique@' : slot.topSysBdPreConfigType+":"+slot.topSysBdActualType,
                        '@EPON.boardName@':slot.BName,
                        '@EPON.boardSN@' :slot.BSerialNumber == "" ? I18N.EPON.none : slot.BSerialNumber,
                        '@EPON.softwareVersion@':slot.BSoftwareVersion,
                        '@EPON.firewareVersion@' :slot.BFirmwareVersion,
                        '@EPON.hardwareVersion@' :slot.BHardwareVersion,
                        '@EPON.boardOnlineTime@' :timeFormat(slot.BUpTime*10 + (new Date()).getTime() - slot.changeTime),//暂时多减8小时
                        '@EPON.totalMem@' :Math.floor(slot.topSysBdlMemSize/1048576)+"MB",
                        '@EPON.freeMem@' :Math.floor(slot.topSysBdFreeMemSize/1048576)+"MB",
                        '@EPON.boardTemperature@' :(slot.topSysBdCurrentTemperature==127 || slot.topSysBdTempDetectEnable==2)?null:slot.slotDisplayTemp+" "+ "@{unitConfigConstant.tempUnit}@",
                        '@EPON.bdAlertStatus@' :ch_bAlarmStatus[slot.BAlarmStatus]
                   };
                	if(2 == slot.BPresenceStatus){
                    	delete entityObject[I18N.EPON.totalMem]
                    	delete entityObject[I18N.EPON.freeMem]
                    	delete entityObject[I18N.EPON.boardOnlineTime]
                    	delete entityObject[I18N.EPON.softwareVersion]
                    	delete entityObject[I18N.EPON.hardwareVersion]
                    	delete entityObject[I18N.EPON.boardName]
                    	delete entityObject[I18N.EPON.firewareVersion]
                    	delete entityObject[I18N.EPON.boardSN]
                    }
                }else if (slotType == SLOT_BOARD) {
                    entityObject = {
                         '@EPON.portNum@' :slot.slotRealIndex,
                         '@EPON.preType@' :slotType == SLOT_BOARD ? I18N.EPON.none : slotType.toUpperCase(),
                         '@EPON.installedType@' :bType[slot.topSysBdActualType] == SLOT_BOARD ? I18N.EPON.none : bType[slot.topSysBdActualType].toUpperCase(),
                         '@EPON.slotStatus@' :slot.BPresenceStatus,
                         '@EPON.controlStatus@' : slot.bOperationStatus
                    }
                }
            } else  {
                if (portIndex > 16 && (slotType == "mefa" || slotType == "mefc") ) {//SNI口
                	var sniPort = slot.portList[portIndex - 1];
                	var opticalAttrList = [null, null, null, null, null, null,null];
                   	if(sniPort.sniMediaType == "fiber"){
                   		opticalAttrList = olt.loadPortOptical(sniPort.portIndex, 0);
                    }
                   	var sniPortNum = sniPort.portRealIndex - 16;
                    switch(sniPort.sniOperationStatus){
                    	case 1://up
                    		 entityObject = {
                                '@EPON.portNum@' : sniPortNum,
                                '@EPON.portName@' : sniPort.sniPortName,
                                '@EPON.mediaType@' :MEDIA_TYPE[sniPort.sniMediaType],
                                '@EPON.autoNegoStatus@' :sniPort.sniAutoNegotiationStatus,
    							//The time (in hundredths of a second) 
                                '@EPON.sniChangeTime@' :timeFormat(sniPort.sniLastStatusChangeTime*1000/100),
                                '@EPON.realSpeed@' :sniPort.topSniAttrActualSpeed,
                              	//光模块属性
                                '@Optical.identifier@' :opticalAttrList[0],
                                '@Optical.vendorName@' :opticalAttrList[1],
                                '@Optical.waveLength@' :opticalAttrList[2],
                                '@Optical.vendorPN@' :opticalAttrList[3],
                                '@Optical.vendorSN@' :opticalAttrList[4],
                                '@Optical.dateCode@' :opticalAttrList[5],
                                '@Optical.bitRate@' :opticalAttrList[6]
                        	};
                        	break;
                    	default://down
                    		 entityObject = {
                                '@EPON.portNum@' :sniPortNum,
                                '@EPON.portName@' :sniPort.sniPortName,
                                '@EPON.mediaType@' :MEDIA_TYPE[sniPort.sniMediaType],
    							//The time (in hundredths of a second) 
                                '@EPON.sniChangeTime@':timeFormat(sniPort.sniLastStatusChangeTime*1000/100),
                                '@EPON.realSpeed@' :sniPort.topSniAttrActualSpeed,
                              	//光模块属性
                                '@Optical.identifier@' :opticalAttrList[0],
                                '@Optical.vendorName@' :opticalAttrList[1],
                                '@Optical.waveLength@' :opticalAttrList[2],
                                '@Optical.vendorPN@' :opticalAttrList[3],
                                '@Optical.vendorSN@' :opticalAttrList[4],
                                '@Optical.dateCode@' :opticalAttrList[5],
                                '@Optical.bitRate@' :opticalAttrList[6]
                        	}
                        	break
                    }
                } else {//PON口
                    var ponPort = slot.portList[portIndex - 1];
                    var opticalAttrList = olt.loadPortOptical(ponPort.portIndex, 1);
                    entityObject = {
                            '@EPON.portNum@' :ponPort.portRealIndex == 0 ? 16 : ponPort.portRealIndex,
                            '@EPON.portType@' :ponPort.ponPortType,
                            '@EPON.maxOnuSupport@' :ponPort.ponPortMaxOnuNumSupport,
                            '@EPON.onlineOnu@' :ponPort.ponPortUpOnuNum,
                            '@EPON.maxDownBD@' :ponPort.SMaxDsBandwidth,
                            '@EPON.maxUpBD@' :ponPort.SMaxUsBandwidth,
                            //光模块属性
                            '@Optical.identifier@' :opticalAttrList[0],
                            '@Optical.vendorName@' :opticalAttrList[1],
                            '@Optical.waveLength@' :opticalAttrList[2],
                            '@Optical.vendorPN@' :opticalAttrList[3],
                            '@Optical.vendorSN@' :opticalAttrList[4],
                            '@Optical.dateCode@' :opticalAttrList[5],
                            '@Optical.bitRate@' :opticalAttrList[6]
                    };
                    if(2 == slot.BPresenceStatus){
                        delete entityObject['@EPON.onlineOnu@']
                    }
                } 
            }
        }
    }
    
    if (entityname == "OLT") {
        entityObject = {
                '@EPON.name@' :olt.oltName,
                '@EPON.type@' :olt.oltType,
                '@EPON.style@' :deviceStyle[olt.oltDeviceStyle],
                '@EPON.rack@' :olt.topSysOltRackNum,
                '@EPON.frame@' :olt.topSysOltFrameNum,
                '@EPON.vendorName@' :olt.vendorName,
                '@OLT.entityDesc@' :olt.sysDescr,
                '@OLT.powerNum@' :olt.oltDeviceNumOfTotalPowerSlot,
                '@OLT.sysUpTime@' :timeFormat(olt.oltDeviceUpTime)
        }
    }
    grid.setSource(entityObject);
    
}

/**************************************
 		ext 树节点定位
 *************************************/
function locateNode(nodeId) {
    navigatorTree.getRootNode().expand();  //有待确认，如果未展开的时候是否会定位
    navigatorTree.getNodeById(nodeId).select();
}

/**************************************
 		面板图实体定位
 *************************************/
function locateEntity(nodeId) {
	var $node = DOC.get(nodeId);
	if($node){
		$node.click();
	}
}

/********************************************************
sni端口rstp使能更改后刷新页面菜单，sniRstpConfig.jsp页面用到
********************************************************/
function updateStpPortConifg(sniIndex,stpPortEnabled){
	 selectionModel.stpPortEnabled = stpPortEnabled;
}

/***********************************
 			清理页面样式
 **********************************/
function clearPage(cid){
	while (divCache.length != 0) {
    	var divCacheObject = divCache.pop();
	    if(cid != divCacheObject){//如果点击的是当前div，则不做修改	
		    $temStyle = $("#"+divCacheObject);	
		    if("OLT" == divCacheObject){
		    	$temStyle.css({ 
		    		border : '2px solid transparent'
				});
				return;
			}	
			var arr = divCacheObject.split('_');  				 
			if(arr[2]>0){
				$temStyle.css({
					opacity : 1,
	  		  		border : '1px solid transparent' 
				});
			}else if(arr[2]==0 && arr[1]<19 && arr[0]!='power' && arr[0]!='fan'){//是板卡
				$temStyle = $(".boardClass#"+divCacheObject);
				$temStyle.css({
					opacity : 0.7,border : '1px solid transparent'
				});
			}else{//是风扇或者电源
				$temStyle.css({
					opacity : 0.7,
	  		  		border : '1px solid transparent'
				});
			}		 		       	
		    if($temStyle.css("opacity") == 1  && $temStyle.css("height") == coordinateParam.slotHeight+"px"){		    
		    	$temStyle.css({"opacity" : 0.7});	
			} 
    	}
	}	
}

/****************************************
		对于部分特殊的列,修改其展现方式
****************************************/
function rendererColumnValue(value, cellmeta, record){
	if(record.id == I18N.EPON.slotStatus || record.id == I18N.EPON.controlStatus || record.id == I18N.EPON.fanStatus){
		if(value==1)
			return "<div style='background:color;color:green'>@COMMON.pentagram_yes@</div>";
		else
			return "<div style='background:color;color:green'>@COMMON.pentagram_no@</div>";
	}else if(record.id == I18N.EPON.bdTypeUnique){
		var v = value.split(":")
		if(bType[v[0]] == bActualType[v[1]])
			return "<div style='background:color;color:green'>@COMMON.pentagram_yes@</div>";
		else
			return "<div style='background:color;color:green'>@COMMON.pentagram_no@</div>";
	}else{
		return value;
	}
}

function beginPrepareJob(){
	// create new Olt Object
	olt = new Olt();
	// modify propertyGird prototype
    if(Ext.grid.PropertyColumnModel){
    	//修改PropertyGrid原型
      	Ext.grid.PropertyGrid.prototype.initComponent = function(){
			this.customEditors = this.customEditors || {};
			this.lastEditRow = null;
			var store = new Ext.grid.PropertyStore(this);
			this.propStore = store;
			//var cm = new Ext.grid.PropertyColumnModel(this,store);
			var cm = new Ext.grid.ColumnModel({columns: [
				 {header:'property' ,align: 'center',sortable: false,resizable: true,menuDisabled :true},
	             {header:'value', align: 'center',sortable: false,resizable: true,menuDisabled :true,renderer : rendererColumnValue}
	        ]});
			//store.store.sort('name','ASC');//取消默认排序
			this.addEvents('beforepropertychange','propertychange');
			this.cm = cm;
			this.ds = store.store;
			Ext.grid.PropertyGrid.superclass.initComponent.call(this);
			this.selModel.on('beforecellselect',function(sm,rowIndex,colIndex){
				if(colIndex === 0)return false;
			});
        }
 	 }
	grid = new Ext.grid.PropertyGrid({
        renderTo: 'viewRightPartBottomBody' ,border:false, autoHeight:true, autoWidth:true,
        headerAsText:true,hideHeaders:true,frame: false,propertysort:false,autoScroll: true,border:false,
        listeners: {
            'render': function(proGrid) {
                var view = proGrid.getView();
                var store = proGrid.getStore();
                proGrid.tip = new Ext.ToolTip({
                    target: view.mainBody,delegate: '.x-grid3-row',trackMouse: true,
                    renderTo: document.body,
                    listeners: {
                        beforeshow: function updateTipBody(tip) {
                            var rowIndex = view.findRowIndex(tip.triggerElement);
                            var record = store.getAt(rowIndex)
                             var result = store.getAt(rowIndex).get('name') + " : ";
                            if(record.id == I18N.EPON.slotStatus){
                        		if(record.data.value==1)
                        			result += I18N.EPON.inject
                        		else
                        			result += I18N.EPON.unInject
                        	}else if(record.id == I18N.EPON.controlStatus || record.id == I18N.EPON.fanStatus){
			                	if(record.data.value==1)
			            			result += I18N.EPON.operationAble
			            		else
			            			result += I18N.EPON.unOperationAble
                			}else if(record.id == I18N.EPON.typeUnique){
                        		var v = record.data.value.split(":")
                        		if(bType[v[0]] == bActualType[v[1]])
                        			result += I18N.EPON.unique
                        		else
                        			result += I18N.EPON.unUnique
                        	}else{
                        			result += record.get("value");
                        	}
                            tip.body.dom.innerHTML = result;
                        }}});
            },
            'rowdblclick':function(g,r,e ){
                //-------双击时默认复制行内容	
                var thisText = e.getTarget().innerHTML;
                //-----属性值可能是number型，故强转string
                clipboardData.setData("text",thisText);
                window.parent.showMessageDlg( I18N.COMMON.tip, I18N.EPON.copyOk + thisText );
            },
			'beforeedit':function(e){
				 e.cancel = true;
		         return false;
			}}
    });
	if(navigatorTree != null)navigatorTree = null;
    navigatorTree = new Ext.tree.TreePanel({
		autoScroll: true,border:false,
        listeners:{
            "click" : clickHandler,"contextmenu" : clickHandler
        }
	});
}
//修改了端口的别名后，需要刷新一下树菜单;
function refreshNavgatorTree(){
	try{
		if( navigatorTree ){
			navigatorTree.destroy();
			if(navigatorTree != null)navigatorTree = null;
		    navigatorTree = new Ext.tree.TreePanel({
				autoScroll: true,border:false,
		        listeners:{
		            "click" : clickHandler,"contextmenu" : clickHandler
		        }
			});
			beginConstructTree();
		}
	}catch(err){
		
	}
}

/**
 * 修改端口状态
 * 
 * @param divId
 * @param state
 */
function changePortState(divId, state){
    var arr = divId.split('_');
    //解析state
    var portTemp = olt.slotList[arr[1]-1].portList[arr[2]-1];
    switch(portTemp.portSubType){
        //SNI MASSTYPE
        case 'geCopper'://NO NEED TO HANDLE
        case 'geFiber'://NO NEED TO HANDLE
        case 'xeFiber':
            //var os = portTemp.sniOperationStatus;
            var os = state;
            var as = portTemp.sniAdminStatus;
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
            //var os = portTemp.ponOperationStatus;
            var os = state;
            var as = portTemp.ponPortAdminStatus;
            if( as==1 && os==1 ){
            	// 管理状态 ，操作状态
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
    switch(state) {
        // 正常
        case 0 :
            $port.css({
                backgroundImage : 'url(/epon/image/' + portType +'/'+portType+ '.png)'
            });     
            break;
        // 正常，没有业务
        case 1 :
            $port.css({
                backgroundImage : 'url(/epon/image/' + portType +'/'+portType+ '.gif)'
            });             
            break;
        // 不正常
        case 2 :
            $state = $("<img>");
            $state.attr({
            	width : 16,
                height : 16,
                title : I18N.EPON.portnoservice
            });
            $state.addClass("portStateClass");
            $state.css({
                position : 'absolute',
                top : 0,
                left : 0,
                opacity : 1,
                zIndex : 15000
            });
            $state.attr({
                src : '/epon/image/pn8602-ef/close.png'
             });
            $port.append($state);   // 在板卡上添加端口状态
            break;  
        default:
            break;
    }   
}

function beginConstructTree(){
	try {
		var root = new Ext.tree.TreeNode({text : "PN8602-EF",id : "OLT",expanded : true,icon : '/epon/image/pn8602-ef/8602EF-16.png'});
		var slotName = null;
		for(var i = 1; i < olt.slotList.length + 1; i++) {
			var slotTemp = olt.slotList[i - 1];

			// 8602E/EF不需要判断预配置类型，只有一个板卡，也不存在板卡插拔，所以不需要8601/02/03中的判断
			slotName = bType[slotTemp.topSysBdPreConfigType].toUpperCase() + "_" + slotTemp.slotRealIndex;
			
			//-----------------------节点对应的divId.....preType_s_p--------------------//
			var slotText = bType[slotTemp.topSysBdPreConfigType] + "_" + slotTemp.slotRealIndex + "_0";
			//---------construct new TreeNode------------//
			var slotnode = new Ext.tree.TreeNode({
				text : slotName,id : slotText,expanded : true,draggable : false,icon : '/images/network/port/port_48.gif',
				listeners : {
					'beforemove' : function() {return false;}
				}
			});
			//------加入端口节点-----//
			var $portList = slotTemp.portList;
			for(var j = 1; j < $portList.length + 1; j++) {//否则就绘制端口节点
				var portName = null;
				var portTemp = $portList[j - 1];
				//-----------对应的是全局流通的divId----------//
				var portId = bType[slotTemp.topSysBdPreConfigType] + "_" + slotTemp.slotRealIndex + "_" + j;
				var portImage;
				//-----得到需要展示的图标和portName-----//
				switch(portTemp.portSubType) {
					case 'geCopper':
						portImage = "/epon/image/geCopper/tree_geCopper.png";
						portName = portTemp.sniPortName;
						break;
					case 'geFiber':
						portImage = "/epon/image/geFiber/tree_geFiber.png";
						portName = portTemp.sniPortName;
						break;
					case 'xeFiber':
						portImage = "/epon/image/xeFiber/tree_xeFiber.png";
						portName = portTemp.sniPortName;
						break;
					case 'geEpon':
						portImage = "/epon/image/geEpon/tree_geEpon.png";
						break;
					case 'tengeEpon':
						portImage = "/epon/image/tengeEpon/tree_tengeEpon.png";
						break;
					case 'gpon':
						portImage = "/epon/image/gpon/tree_gpon.png";
						break;
				}
				//--------端口支持自定义名字，默认按geCopper2的格式
				portName = portName ? portName : (portTemp.portSubType + "_" + j);
				//---------construct new TreeNode------------//
				var portnode = new Ext.tree.TreeNode({text : portName,id : portId,draggable : false,icon : portImage});
				slotnode.appendChild(portnode);
			}
			root.appendChild(slotnode);
		}
		for(var i = 1; i < olt.powerList.length + 1; i++) {
		    //var powerIndex = olt.powerList[i].powerCardRealIndex;
			var powerName =I18N.EPON.power+"_" + i;
			//var powerID = "power_" + powerIndex + "_0"
			var powerID = "power_" + i + "_0"
			var powerNode = new Ext.tree.TreeNode({
				text : powerName,id : powerID,expanded : true,icon : '/images/network/port/port_48.gif'
			});
			root.appendChild(powerNode);
		}
		for(var i = 1; i < olt.fanList.length + 1; i++) {
			var fanName = I18N.EPON.fan
			// 风扇假数据
			var fanID = "fan_1_0"
			var fanNode = new Ext.tree.TreeNode({
				text : fanName,id : fanID,expanded : true,icon : '/images/network/port/port_48.gif'
			});
			root.appendChild(fanNode);
		}
		navigatorTree.setRootNode( root ? root : {text : '',leaf : true});
		navigatorTree.render("viewLeftPartBody");
	} catch(exp) {
		navigatorTree.setRootNode({text : '',leaf : true})
		navigatorTree.render("viewLeftPartBody")
		olt.monitor.loadError(I18N.EPON.loadOltError)
		removeMask(I18N.EPON.entityError)
	}
}

/***********************************
 		 加载OLT大对象数据
 **********************************/
function beginConstructPage() {
	//------开始准备工作---------//
	beginPrepareJob();
	//------加载olt数据----------//
	$.ajax({
        url:"/epon/oltObjectCreate.tv",
        data: { entityId: entityId },cache : false,datatype:'json',
        success:function (data) {
        	$(".loadingText").html("@EPON.dataBack@..")
            try{
		        //initialize the olt data source
		        olt.init(data);  
		      	//从数据库获取OLT所有光口的光模块信息
				olt.initOltPortOptical();
	           	beginConstructTree()
	            $(".loadingText").html("@EPON.drawing@...")
	           	//-----------创建面板图:不要把json做为参数传递进去---------------//
	           	createPN8602EF();
	            addEventListeners();
	            var attrStore = {
	                '@EPON.name@' :olt.oltName,
	                '@EPON.type@' :olt.oltType,
	                '@EPON.style@' :deviceStyle[olt.oltDeviceStyle],
	                '@EPON.rack@' :olt.topSysOltRackNum,
	                '@EPON.frame@' :olt.topSysOltFrameNum,
	                '@EPON.vendorName@' :olt.vendorName,
	                '@OLT.entityDesc@' :olt.sysDescr,
	                '@OLT.powerNum@' :olt.oltDeviceNumOfTotalPowerSlot,
	                '@OLT.sysUpTime@' :timeFormat(olt.oltDeviceUpTime)
        		}
				grid.setSource(attrStore);
	            //-------------移除蒙版-----//
	            removeMask();
	      	  }catch(exp){
	        	$(".loadingText").html(I18N.EPON.drawError)
	        	olt.monitor.loadError(I18N.EPON.loadAgain)
	        }
        },
        error:function () {
        	$(".loadingText").html(I18N.EPON.dataError)
        	navigatorTree.setRootNode({text : '',leaf : true})
        	navigatorTree.render("viewLeftPartBody");//数据加载异常也要把树渲染了
        	olt.monitor.loadError(I18N.EPON.loadOltError)
        }})
}

/***********************************
 				 移除蒙版
 **********************************/
function removeMask(msg){
    Ext.getBody().unmask();
}

/*********************************
	板卡温度检测使能变更后
*********************************/
function bdTempDetectEnableChanged(s, boardId, index){
	var tmpSource = grid.getSource();
	if(s == 2){//closed
		tmpSource[I18N.EPON.boardTemperature] = null;
		grid.setSource(tmpSource);
	}else if(s == 1){//opened
		tmpSource[I18N.EPON.boardTemperature] = I18N.EPON.collecting
		olt.slotList[index - 1].topSysBdCurrentTemperature = I18N.EPON.collecting
		grid.setSource(tmpSource)
		Ext.Ajax.request({
			url:"/epon/refreshBdTemperature.tv",
				success: function (text) {
					/* if(text==null || text.responseText=='false'){
						return;
					}
					if(parseInt(text.responseText) == 127){
						tmpSource[I18N.EPON.boardTemperature] = null
						olt.slotList[index - 1].topSysBdCurrentTemperature = null
					}else{
						olt.slotList[index - 1].topSysBdCurrentTemperature = text.responseText;
						tmpSource[I18N.EPON.boardTemperature] = text.responseText + " "+ "@{unitConfigConstant.tempUnit}@";
					}
					grid.setSource(tmpSource); */
			    }, failure: function () {
			    	
			    }, params: {entityId : entityId, boardId : boardId,start:index}
		});
	}
}
//温度采集回调函数
window.top.addCallback("refreshBdTemp",function(m){
	var message = m.message;
	var boardId = m.boardId;
	var index = m.index;
	var tmpSource = grid.getSource();
	if(message == null || message == 'false'){
		return;
	}
	if(parseInt(message) == 127){
		tmpSource[I18N.EPON.boardTemperature] = null
		olt.slotList[index - 1].topSysBdCurrentTemperature = null
	}else{
		olt.slotList[index - 1].topSysBdCurrentTemperature = message;
		tmpSource[I18N.EPON.boardTemperature] = message + " "+ "@{unitConfigConstant.tempUnit}@";
	}
	grid.setSource(tmpSource);
});

/*********************************
		风扇转速设置改变后
*********************************/
function fanConfigChange(fanId, level){
	var tmpSource = grid.getSource();
	tmpSource[I18N.EPON.fanSpeed] = I18N.EPON.collecting
	olt.fanList[0].topSysFanSpeed =  I18N.EPON.collecting
	grid.setSource(tmpSource);
	if(level != null && parseInt(level) > 0){
		olt.fanList[currentId.split("_")[1]-1].topSysFanSpeedControl = level;
	}
	setTimeout(function(){
		Ext.Ajax.request({
			url:"/epon/getFanRealSpeed.tv",
			success: function (text) {
				if(text.responseText=='false' || text==null){
					tmpSource[I18N.EPON.fanSpeed] = null;
					olt.fanList[0].topSysFanSpeed = null;
					setTimeout(function(){fanConfigChange(fanId, level);},120000);
				}else{
					olt.fanList[0].topSysFanSpeed = text.responseText;
					tmpSource[I18N.EPON.fanSpeed] = topSysFanSpeedToRpm(text.responseText);
					$("#fan_1_0").attr("title", I18N.EPON.fanSpeed + ": " + text.responseText);
				}
				grid.setSource(tmpSource);
		    }, failure: function () {
		    	setTimeout(function(){fanConfigChange(fanId, level);},120000);
		    }, params: {entityId : entityId, fanCardId : fanId}
		});
	}, 30000);
}

function authLoad(){
    if(!refreshDevicePower){
        $("#bfsxOlt").attr("disabled",true);
    }
}

//注意：这个函数覆盖了EponDeviceHelper.js中同名函数;
//覆盖原因：都是mefc板卡，设备说明里面无法显示SNI口。实际上17口到20口都是SNI口;
function portHandler(e){
    var arr = this.id.split("_");
    NOTATION_FLAG = true;
    var num = parseInt(arr[2], 10);
    var boardType = arr[0];
    var maxNum;
    if(boardType === 'mefc'){
    	maxNum = 17;
    }
	if(num >= maxNum){
		switchNotation('SniNotation');
	}else{
		switchNotation('PonNotation');
	}
    NOTATION_FLAG = false;
    currentId = this.id;
    preventBubble(e);
    if (!CASCADE_LOCK) {
        CASCADE_LOCK = true;
        try{
            if (ctrlFlag == 0) {// 如果没有按ctrl键，则清除之前的样式
                clearPage();
            }
            if(divCache.contains(this.id)){
                divCache.remove(this.id);
                this.style.border  = '1px solid transparent';
            } else {
                divCache.add(this.id);// 并记录本次div 的id,放入divCache中
                this.style.border = "1px solid "+divStyle.click;
                setBorderStyle(this.id.substring(0,7)+"0"); // 修改父板的透明度
                divCache.add(this.id.substring(0,7)+"0");// 父板被修改了，故也要记录
                clickHandler(this.id,e);
            }
        }catch(exp){}
        CASCADE_LOCK = false;
    }
}
</script>
</head>
<body class="newBody overfloatHidden bodyWH100percent" onload="authLoad()">
	<div class="wrapWH100percent overfloatHidden">
		<!--头部菜单开始 -->
		<%@ include file="/epon/inc/navigator.inc"%>
		<!--头部菜单结束 -->
		
		<!-- 左侧开始 -->
		<div class="viewLeftPart" id="viewLeftPart">
			<p class="pannelTit">@EPON.oltDeviceTree@</p>
			<div id="viewLeftPartBody" class="viewLeftPartBody clear-x-panel-body"></div>
		</div>
		<!-- 左侧结束 -->
		<div id="viewLeftLine" class="viewLeftLine"></div>
		
		<!-- 中间部分开始 -->
		<div id="viewMiddle" class="viewMiddle">
			<div style="position:absolute; z-index:21; top:5px; left:5px;">
				<a href="javascript:;" id="bfsxOlt" class="normalBtn" onclick="bfsxOlt();"><span><i class="miniIcoRefresh"></i>@COMMON.refresh@</span></a>
			</div>
			<div class="middlePartPutPic" id="device_container" style="width:600px;"  onselectstart="javascript:return false;"></div>
			<!-- 中间底部设备说明开始 -->
			<div id="viewMiddleBottom" class="viewMiddleBottom threeFeBg">
				<div id="viewMiddleBottomLine" class="horizontalLine displayNone" style="height:7px; cursor:n-resize">
					<div class="dragDot"></div>
				</div>
				<p id="viewMiddleBottomTit" class="pannelTitWithTopLine">@EPON.oltDesc@<a href="javascript:;" class="pannelTilArrUp"></a></p>
				<div id="viewMiddleBottomBody" class="displayNone">
					<div id="NotationsContainer">
						<div class="edge5 DeviceNotation">
							<table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
								<tbody>
								    <tr>
								        <td class="rightBlueTxt">MEFA/MEFB/MEFC/MEFD:</td>
								        <td>@EPON.geua@</td>
								        <td class="rightBlueTxt">MEFA/MEFB/MEFC/MEFD:</td>
								        <td>@EPON.mpua@</td>
										<td class="rightBlueTxt">MEFA/MEFB/MEFC/MEFD:</td>
								        <td>@EPON.epua@</td>
									</tr>
									<tr>
										<td class="rightBlueTxt">geCopper:</td>
										<td>@EPON.geCopper@</td>
										<td class="rightBlueTxt">geFiber:</td>
										<td>@EPON.geFiber@</td>
										<td class="rightBlueTxt">xeFiber:</td>
										<td>@EPON.xeFiber@</td>
									</tr>
									<tr>
										<td class="rightBlueTxt">geEpon:</td>
										<td>@EPON.geEpon@</td>
										<td class="rightBlueTxt">tengeEpon:</td>
										<td>@EPON.tengeEpon@</td>
										<td class="rightBlueTxt">gpon:</td>
										<td>@EPON.gpon@</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="BoardNotation edge5 displayNone">
							<dl class="twoPartDl">
								<dd class="twoPartDlLeft">
									<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
									     <thead>
									         <tr><th class="txtLeftTh">@EPON.ledNota@</th></tr>
									     </thead>
									     <tbody>
									         <tr><td class="pL10">@EPON.firstRow@</td></tr>
									         <tr><td class="pL10">@EPON.secondRow@</td></tr>
									         <tr><td class="pL10">@EPON.thirdRow@</td></tr>
									         <tr><td class="pL10">@EPON.fourRow@</td></tr>
										</tbody>
									</table>																		
								</dd>
								<dd class="twoPartDlRight">
									<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
									     <thead>
									         <tr><th class="txtLeftTh" colspan="4">@EPON.bdAlertNota@</th></tr>
									     </thead>
									     <tbody>
									         <tr><td class="pL10" colspan="4">@EPON.bdColorAlert@  !</td></tr>
									         <tr>
									         	<td width="16" style="padding-left:10px; padding-right:0px;"><div class="critical"></div></td>
		                           		 		<td align="left">critical @EPON.alert@</td>                       		 	                       		 	
		                           		 		<td width="16" style="padding-left:10px"><div class="major"></div></td>
		                           		 		<td align="left">major @EPON.alert@</td>
									         </tr>
									         <tr>
									         	<td  style="padding-left:10px"><div class="minor"></div></td>
		                           		 		<td align="left">minor @EPON.alert@</td>      
		                           		 		<td style="padding-left:10px"><div class="warning"></div></td>
		                           		 		<td align="left">warning @EPON.alert@</td>
									         </tr>
									         <tr>
									         	<td class="pL10" colspan="4">@EPON.noColorAlert@</td>
									         </tr>
									     </tbody>
									 </table>
								</dd>
							</dl>
						</div>
						<div class="SniNotation edge5 displayNone">
							<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
								<thead>
							         <tr>
							             <th class="txtLeftTh" colspan="6">@EPON.sniPortNota@</th>
							         </tr>
							    </thead>
							    <tbody>
							    	<tr>
							    		<td class="pL10" width="16"><img src="/epon/image/mgmt.png"/></td>
							    		<td width="16"></td>									    		
							    		<td width="46%">@EPON.mgmt@</td>
							    		<td class="pL10" width="16"><img src="/epon/image/sni.png"/></td>
							    		<td width="16"></td>	
							    		<td>@EPON.twiPort@</td>									    		
							    	</tr>
							    	<tr>
							    		<td class="pL10" width="16"><img src="/epon/image/fiber.png"/></td>
							    		<td width="16"></td>
							    		<td>@EPON.fiberPort@</td>
							    		<td class="pL10" width="16">
							    			<div style="background:url(/epon/image/sni.png) no-repeat;width:16px;heigth:16px" >
				 								<img src="/epon/image/close.gif" />
				 							</div>
							    		</td>
							    		<td width="16"></td>
							    		<td>@EPON.noservice@</td>
							    	</tr>
							    	<tr>
							    		<td class="pL10" width="16"><img src="/epon/image/pon.gif"/> </td>
							    		<td width="16"><img src="/epon/image/geCopper/geCopper.gif"/></td>
							    		<td>@EPON.portNotInject@</td>
							    		<td class="pL10" width="16"><img src="/epon/image/pon.png"/> </td>
							    		<td width="16"><img src="/epon/image/sni.png" /></td>
							    		<td>@EPON.workNormal@</td>
							    	</tr>
							    	<tr>
							    		<td class="pL10" width="16"><img src="/epon/image/pon.png"  class="UN-ACCESSABLE"/> </td>
							    		<td width="16"><img src="/epon/image/sni.png"  class="UN-ACCESSABLE" /></td>
							    		<td colspan="4">: @EPON.portUnAccessAble@</td>
							    	</tr>
							    </tbody>
							</table>
						</div>
						<div class="PonNotation edge5 displayNone">
							<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
								<thead>
									<tr>
							             <th class="txtLeftTh" colspan="4">@EPON.ponPortNota@</th>
							         </tr>
								</thead>
								<tbody>
							    	<tr>
							    		<td width="16" class="pL10"><img src="/epon/image/pon.png"/></td>
							    		<td width="47%">:@EPON.workNormal@</td>
							    		<td width="16" class="pL10"><img src="/epon/image/pon.gif"/></td>
							    		<td>:@EPON.portNotShin@</td>
							    	</tr>
							    	<tr>
							    		<td width="16" class="pL10">
							    			<div style='background-image:url("/epon/image/pon.png");background-repeat:"no-repeat";width:16px;height:16px; position: relative;'>
												<img src="/epon/image/close.gif" style="position: absolute; top:3px; left:3px;" />
											</div>
							    		</td>
							    		<td>:@EPON.noservice@</td>
							    		<td width="16" class="pL10"><img src="/epon/image/pon.png"  class="UN-ACCESSABLE"/></td>
							    		<td>:@EPON.portUnAccessAble@</td>
							    	</tr>
							    </tbody>
							</table>
						</div>
						<div class="FanNotation edge5 displayNone">
							<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
								<thead>
									<tr>
										<th class="txtLeftTh">@EPON.fanStatusNota@</th>
									</tr>
								</thead>
								<tbody>
							    	<tr>
							    		<td>
							    		</td>
							    	</tr>
							    </tbody>
							</table>
						</div>
						<div class="PowerNotation edge5 displayNone">
							<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
								<thead>
									<tr>
										<th class="txtLeftTh">@EPON.powerNota@</th>
									</tr>
								</thead>
								<tbody>
							    	<tr>
							    		<td>
							    		</td>
							    	</tr>
							    </tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!-- 中间底部设备说明结束 -->
			
			<!-- 切换布局按钮开始 -->
			<div class="abAndTR10" id="putTabBtn"></div>
			<!-- 切换布局按钮结束 -->
		</div>
		<!-- 中间部分结束 -->
		<div id="viewRightLine" class="viewRightLine"></div>
		
		<!-- 右侧部分开始 -->
		<div id="viewRightPart" class="viewRightPart">
			<!-- 右侧上半部分开始 -->
			<p id="viewRightPartTopTit" class="pannelTit">@EPON.oltAboutFunction@<a href="javascript:;" class="pannelTilArrDown"></a></p>
			<div id="viewRightPartTopBody" class="rightPannel propertyBg clear-x-panel-body">
				
			</div>
			<!-- 右侧上半部分结束 -->
			<div id="viewRightPartMiddleLine" class="horizontalLine" style="height:7px; cursor:n-resize"><div class="dragDot"></div></div>
			<!-- 右侧下半部分开始 -->
			<p id="viewRightPartBottomTit" class="pannelTitWithTopLine">@EPON.oltAttribute@<a href="javascript:;" class="pannelTilArrDown"></a></p>
			<div id="viewRightPartBottomBody" class="rightPannel propertyBg"></div>
			<!-- 右侧下半部分结束 -->
			
		</div>
		<!-- 右侧部分结束 -->
	</div>	
	<script type="text/javascript" src="/js/jquery/jquery.wresize.js"></script>
	<script type="text/javascript" src="/js/jquery/dragMiddle.js"></script>
	<script type="text/javascript">
	//加入右键提示信息;
	$(function(){
		top.nm3kRightClickTips({
			title: "@COMMON.tip@",
			html: "@COMMON.equipmentPic@"
		});
	});//end document.ready;
	
	var nm3k = {};//用来记录拖动元素，改变布局的时候，方便销毁拖动功能;
	nm3k.rightTopHeight =  280;//记录右侧上部的高度;	
	nm3k.middleBottomHeight = 100;//记录中间底部设备说明的高度;
	nm3k.leftWidth = 180;//左侧宽度;
	nm3k.rightWidth = 255;//右侧宽度;
	nm3k.rightTopOpen = true;//右侧上部是否展开;
	nm3k.rightBottomOpen = true;//右侧下部是否展开;
	nm3k.middleBottomOpen = true;//中间下部是否展开;
	nm3k.layoutToMiddle = true;//面板图是否在中间(不在中间，就是在右边);
	nm3k.tabBtnSelectedIndex = 0;//记录该显示选中哪个布局按钮;
	if( ${layout.leftWidth}){
		nm3k.rightTopHeight =  ${layout.rightTopHeight};//记录右侧上部的高度;
		nm3k.middleBottomHeight = ${layout.middleBottomHeight};//记录中间底部设备说明的高度;
		nm3k.leftWidth = ${layout.leftWidth};//左侧宽度;
		nm3k.rightWidth = ${layout.rightWidth};//右侧宽度;
		nm3k.rightTopOpen = ${layout.rightTopOpen};//右侧上部是否展开;
		nm3k.rightBottomOpen = ${layout.rightBottomOpen};//右侧下部是否展开;
		nm3k.middleBottomOpen = ${layout.middleBottomOpen};//中间下部是否展开;
		nm3k.layoutToMiddle = ${layout.layoutToMiddle};//面板图是否在中间(不在中间，就是在右边);
		nm3k.tabBtnSelectedIndex = ${layout.tabBtnSelectedIndex};//记录该显示选中哪个布局按钮;
	}
	/*///////////////在这个部分读取后台的数据改变上面的默认值///////////////////////////////*/
	
	
	</script>
	<script type="text/javascript" src="/js/jquery/nm3kViewLayout.js"></script>
	<bgsound id="sound" autostart=true loop=false />
</body>

<script type="text/javascript">
/**************************************************************************
 			* 设置板卡状态灯
  sid是板卡id，不仅仅是板卡位置 ，	led为led灯类型，run，alarm，pwr
 *************************************************************************/
function setLedState(sid,led,state){
	$("#"+sid).find("#"+led).attr('fillcolor',state);
}

/********************************************
 		 得到板卡状态灯的当前状态
 *******************************************/
function getLedState(sid,led){
	return $("#"+sid).find("#"+led).attr('fillcolor');
}

/********************************
 			 设置边框效果
 *******************************/
function setBorderStyle(id){
	var $temStyle = $( "#"+id ),
		dom = $temStyle[0];
	try{
		if( dom.style.borderColor == divStyle.click){
		}else{
			if((id.split('_'))[2]>0){//如果当前端口是端口
				$temStyle.css({
	  		  		border : '1px solid '+ divStyle.hover 
				});
			}else{
				$.each($(".boardClass"),function(i,div){
					if('invalid' == $(div).attr("checked")){//如果没有被点击过，则修改为没有点击
						$(div).css({
							border : '1px solid transparent' 
						});
					}
				});
				$temStyle.css({
	  		  		border : '1px solid '+ divStyle.hover 
				});
			}
		}
	}catch(err){
		
	}
}

/******************************************************************************
 	 为console，mgmt，无效端口添加点击事件，让事件转移到板卡对象上
 ******************************************************************************/
function addObjectListener () {
	$(".consoleClass , .portClass1").click(function(e){
		preventBubble(e);
		id = this.id;
		var arr = id.split("_");
		id = arr[0]+"_"+arr[1]+"_0";
		$("#"+id).trigger("click");
	}).bind("contextmenu",function(e){
		preventBubble(e);
		id = this.id;
		var arr = id.split("_");
		id = arr[0]+"_"+arr[1]+"_0";
		$("#"+id).trigger("contextmenu",[e,e]);
	}) 
}

/******************************
 		 清除边框效果
 *******************************/
function clearBorderStyle(id){
	var $temStyle = $( "#"+id ),
		dom = $temStyle[0];
	if( dom.style.borderColor != divStyle.click){
  	  	//如果是端口		  	  	 
  	  	var arr = id.split('_');  				 
		if(arr[2]>0){
			$temStyle.css({
  		  		border : '1px solid transparent' 
			});
		}else if(arr[2]==0 && arr[1]<19 && arr[0]!='power' && arr[0]!='fan'){//是板卡
			$temStyle.css({
  		  		border : '1px solid transparent'
			});
		}else{//是风扇或者电源
			$temStyle.css({
  		  		border : '1px solid transparent'
			});
		}		 		
	 }	
}
/********************************
 	 检查鼠标是否离开板卡区域
 *******************************/
function checkAnimateStyle(){
	$position = $("#"+this.id);
	if($position.css("height") == ""+coordinateParam.slotHeight+"px"){
		var x = $position.offset().left;
		var y = $position.offset().top;
		if(cursor.X>x && cursor.X<x+coordinateParam.slotWidth && cursor.Y>y && cursor.Y<y+coordinateParam.slotHeight){
			$position.css({
				//top : coordinateParam.slotTopStart-7,
				opacity : 1
			});
		}
	}
}
/********************************
 		程序入口
 *******************************/
Ext.onReady(function(){
    Ext.getBody().mask("@COMMON.loading@...");
    beginConstructPage();
});

/********************************
 		 说明部分的转换
 *******************************/
function switchNotation(classId){
	if(!NOTATION_FLAG){
		return
	}
	$("#NotationsContainer").find('div').css({
		'display':'none'
	})
	$("."+classId).css('display','block')
	$("."+classId).find('div').css('display','block')
}
</script>
</Zeta:HTML>
