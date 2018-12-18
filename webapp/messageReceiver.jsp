<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
</Zeta:Loader>
<script type='text/javascript'>
/**********************************************************
 	备注：此设计有个弊端
 	如果同一个浏览器打开同一个网管访问同一个页面,但是访问不同的设备,如果不是做单播
 	的话,则会导致数据页面接收到其他设备的信息，解决办法是在请求中
 	带上GLOBAL_SOCKET_CONNECT_ID
 **********************************************************/

$( DOC ).ready(function(){
	top.registerMessageReceiver(receiveMessages);
});

function receiveMessages( message ) {
    var alerts = [];
    var id = message.id;
    var type = message.type;
    var content = message.data;
    if(typeof content == "string"){
    	try{
	        content = Ext.decode(content);
    	}catch(e){
    		content = message.data;
    	}
    }
    
    // add by fanzidong,增加事件发布处理
    if(id) {
        top.PubSub.publish(id, content);
    }
    
    if(type == 'alert'){
        alerts.push(content);
    }else if(type == 'notice'){
    	window.top.pasteToBillboard( content );
    }else{
        //其他消息类型处理
        window.top.callback(id,type,content);
    }
    doAlerts(alerts);
}
function doAlerts(data) {
	//-----通用处理办法:弹出提示框-----//
	if (data.length > 0) {
		try{
			window.top.addAlert(data);
		}catch(e){}
	}
}
</script>
<!-- AlertHandler定义 -->
<script>
//-------------常量区-------------//
var alertConstant = {
	PORT_DOWN: 2,
	PORT_UP:1,
	
	ONU_DOWN:2,
	ONU_UP:1,
	//TODO 板卡的状态是此处自定义的，需要讨论
	BOARD_OUT: 'out',
	BOARD_IN: 'in',
	BOARD_RESET: 'reset',
	FAN_OUT: 'fanout',
	POWER_OUT: 'powerout',
	BOARD_TEMP: 'boardTemp'
}

/**
 * 特殊告警的处理方法，entityId，iframe不能在重构时提取出来，注意
 */
var alertExecutor = function(){
		var o = this;
		o._getAlertIndex = function(alert){
			var source = alert.source;
			var sourceArr = source.split(":");
			var instances = null;
			if(sourceArr.length > 1){
				instances = sourceArr[1].split("/");
			}else{
				instances = sourceArr[0].split("/");
			}
			//var instances = source.split(":");
			//instances = instances.join("/");
			//instances = instances.split("/");
			var result = '';
			for(var i=0;i<5;i++){
				//如果包含该位，则写入该位，否则补0
				if(i<instances.length){
					var instance = instances[i];
					instance = parseInt(instance,10).toString(16)
					instance = instance.length==1?'0'.concat(instance):instance;
					result = result.concat(instance);
				}else{
					result = result.concat('00');
				}
			}
			return parseInt(result,16);
		};
		//----处理端口断开告警----//	
		o.PortLinkhandler = function(alert,value){
			//----判断面板图是否已打开-----//
			var entityId = alert.entityId;
			var iframe = window.parent.getFrame("entity-" + entityId);
			switch(!!iframe){
				case false:
					//something else to do?
					break;
				case true:
					//解析端口名为divId
					//修改其值,有问题，还得看到它的adminStatus。当前只表示OperationStatus
					if(typeof iframe.displayEmergency =='function')
						iframe.displayEmergency(alert.source,value);
					break;
				default:
					break;
			}
		};
		o.onuUpDownHandler = function(alert,value){
			var entityId = alert.parentId;
			var iframe = window.parent.getFrame("entity-" + entityId);
			switch(!!iframe){
				case false:
					//something else to do?
					break;
				case true:
					//修改其值,有问题，还得看到它的adminStatus
					//source表示onuId？
					if(typeof iframe.displayEmergence =='function')
						iframe.displayEmergence(o._getAlertIndex(alert),value);
					break;
				default:
					break;
			
			}
		};
		o.boardAlertHandler = function(alert,value){
			var entityId = alert.entityId;
			var iframe = window.parent.getFrame("entity-" + entityId);
			//如果没有打开设备面板图页面
			switch(!!iframe){
				case false:
					//something else to do?
					break;
				case true:
					if(typeof iframe.displayEmergency =='function')
						iframe.displayEmergency(alert.source,value);
					break;
				default:
					break;
			}
		};
		o.DeviceUnConnectable = function(alert){
			var entityId = alert.entityId;
			var iframe = window.parent.getFrame("entity-" + entityId);
			//如果没有打开设备面板图页面
			switch(!!iframe){
				case false:
					//something else to do?
					break;
				case true:
					if(typeof iframe.deviceUnConnectable =='function')
						iframe.deviceUnConnectable();
					break;
				default:
					break;
			}
		}
		
}
</script>
</head>
<body ></body>
</Zeta:HTML>