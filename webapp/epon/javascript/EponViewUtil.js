var bType = ["slot","mpua","mpub","epua","epub","geua","geub","xgua","xgub","xguc","xpua","mpu-geua","mpu-geub","mpu-xguc","gpua","epuc","epud",'meua','meub','mefa','mefb','mefc','mefd','mgua','mgub','mgfa','mgfb'],
	bActualType = ["slot","mpua","mpub","epua","epub","geua","geub","xgua","xgub","xguc","xpua","gpua","meua","meub","mefa","mefb","epuc","epud","","","",'mefc','mefd','mgua','mgub','mgfa','mgfb'],
    UPLINK_BOARD = ["geua","xgua","xgub",'geub','xguc',"mpu-geua","mpu-geub","mpu-xguc"],
    DOWNLINK_BOARD = ["epua","epub","xpua","meua","meub","gpua","mefa","mefb","epuc","epud","mefc","mefd"],
    SERVICE_BOARD = ["geua","geub","xgua","xgub","epua","epub","xguc","geub","xpua","gpua","epuc","epud"],//不是主控板的就是业务板,所以MPU-*不归类于业务板
    SLOT_BOARD = "slot",
    CONTROL_BOARD = ["mpua","mpub","mpu-geua","mpu-geub","mpu-xguc",'meua','meub','mefa','mefb','mefc','mefd','mgua','mgub','mgfa','mgfb'],
    serviceTree,
    serviceMenu,
    MEDIA_TYPE = { twistedPair : "@COMMON.twistPair@", fiber : "@COMMON.fiber@" },
	divStyle = {
		hover : '#00EEEE',click : 'yellow',back : 'green'
	},
	FAN_ALARM_STATUS = ['critical', 'major', 'minor', 'warning', '--'],
	cursor = { X : 0, Y : 0 },
	deviceStyle = ["", "@EPON.styleClose@" , "@EPON.styleInject@" , "@EPON.other@"],
	bPresenceStatus = ["@EPON.unknown@" , "@EPON.injected@" , "@EPON.notInject@", "@EPON.other@"],
	STATUS = { UP : 1,  DOWN : 2 },
	ledColor = ['gray','#E3FD07','gray'],
	alertColor = ['gray','red','gray'];
bType[255] = 'unknown';
var ICON = {
	UNINJECT : '/epon/image/uninject.png',
	UNPRECFG : '/epon/image/unprecfg.png',
	UNCOMPATIBLE :'/epon/image/uncompatible.png'
}

//add by bravin:20141205视图页面的所有ajax请求都已修改为jquery.ajaxy请求，所有的ajax请求默认都是同步的,以便在业务树上做操作时,能够及时对结果进行反应
$.ajaxSetup({
  type: "POST",async:true,
  complete:function(){
	  if(currentId){
  		$("#"+currentId).click();
  	}
  }
});
/**
 * 剥离字符串中以0为开头的部分
 */
String.prototype.trimZero = function(){
	if(this == "00"){
		return "0";
	}
	return  parseInt(this.replace(/^0+/,""),16);
}

var EponUtil = {
    /**
     * 得到设备的entity ID
     */
     getEntityId : function(){
         return olt.entityId;
     },
     
     /**
      * 得到当前所选ITEM的index
      * @return {Long} index
      */
     getItemId : function(){
         return selectionModel.getSelectedId();
     },
     
     /**
      * 得到当前所选ITEM的index
      * @return {Long} index
      */
     getItemIndex : function(){
         return selectionModel.getSelectedIndex();
     }
}

var SelectionModel = Ext.extend(Object,  { 
    SLOT_NO : NULL,
    SLOT_TYPE : NULL,
    PORT_NO : NULL,
    
    /**
    * @param {String} paramId
    * @return {Array} 
    **/
    constructor :function(paramId){
       if(paramId){
           this.reset( paramId );
       }
    },
    
    /**
     * 重置selectionModel
     * @param {String} DispHtmlElementId
     */
    reset : function(paramId){
        paramId = paramId ? paramId : currentId;
        var indexs = paramId.split("_");
        this.SLOT_TYPE = indexs[0];
        this.SLOT_NO = indexs[1] -1 ;
        this.PORT_NO = indexs[2] - 1;
    },
    
    getBoardId : function(id){
        this.reset(id?id:currentId);
        return String.format("{0}_{1}_{2}", this.SLOT_TYPE, this.SLOT_NO, 0)
    },

    /**
     * 得到被选中实体的数据对象
     * @return {Object}
     */
    getSelectedItem : function( paramId ){
        paramId = paramId ? paramId : currentId;
        this.reset( paramId );
        if ("fan" == this.SLOT_TYPE )
            return olt.fanList[this.SLOT_NO];
        else if ("power" == this.SLOT_TYPE )
            return olt.powerList[this.SLOT_NO];
        else if (-1 == this.PORT_NO )
            return olt.slotList[this.SLOT_NO];
        else {
        	var port = null;
        	for(var i=0; i<olt.slotList[this.SLOT_NO].portList.length; i++) {
        		if(olt.slotList[this.SLOT_NO].portList[i].portRealIndex == this.PORT_NO+1) {
        			port = olt.slotList[this.SLOT_NO].portList[i];
        			break;
        		}
        	}
        	return port;
        } 
        	
    },
    
    getSelected : function(){
        return this.getSelectedItem();
    },
    
    /**
    * 得到被选中的实体Element元素
    * @return {DispHtmlElement}
    */
    getSelectedEl : function(){
        return Ext.fly( currentId );
    },
    /**
    * 得到被选中实体的index
    * @return {Long} index
    */
    getSelectedIndex : function(){
        this.reset( paramId );
        if ("fan" == this.SLOT_TYPE )
            return olt.fanList[this.SLOT_NO].fanCardIndex;
        else if ("power" == this.SLOT_TYPE )
            return olt.powerList[this.SLOT_NO].powerCardIndex;
        else if (0 == this.PORT_NO )
            return olt.slotList[this.SLOT_NO].slotIndex;
        else 
            return olt.slotList[this.SLOT_NO].portList[this.PORT_NO].portIndex;
    },
    /**
    * 得到被选中实体的id
    * --> DivId转换为实际的ponId，sniId，等
    * @return {Long] Id
    */
    getSelectedId : function(){
        this.reset( paramId );
        if ("fan" == this.SLOT_TYPE )
            return olt.fanList[ this.SLOT_NO ].fanCardId;
        else if ("power" == this.SLOT_TYPE )
            return olt.powerList[this.SLOT_NO].powerCardId;
        else if (0 == this.PORT_NO)
            return olt.slotList[this.SLOT_NO].slotId;
        else
            return olt.slotList[this.SLOT_NO].portList[this.PORT_NO].portId;
    }
});
var selectionModel = new SelectionModel();

/********************************************************************
    将以秒为单位的时间转换为以天/小时/分/秒的形式显示的方法
*******************************************************************/
function timeFormat(s) {
    var t
    if(s > -1){
        hour = Math.floor(s/3600000);
        min = Math.floor(s/60000) % 60;
        sec = Math.floor(s/1000) % 60;
        day = parseInt(hour / 24);
        if (day > 0) {
            hour = hour - 24 * day
            t = day + "@COMMON.D@" + hour + "@COMMON.H@"
        } else {
            t = hour + "@COMMON.H@"
        }
        if(min < 10){
            t += "0"
        }
        t += min + "@COMMON.M@"
        if(sec < 10){
            t += "0"
        }
        t += sec + "@COMMON.S@"
    }
    return t
}

/*********************************************************************
            DivId转换为实际的ponId，sniId，等
*********************************************************************/
function getActualId(paramId){
    currentId = paramId?paramId:currentId;
    var array = currentId.split("_");   
    if ("fan" == array[0])
        return olt.fanList[array[1]-1].fanCardId;
    else if ("power" == array[0])
        return olt.powerList[array[1]-1].powerCardId;
    else if (0 == array[2])
        return olt.slotList[array[1]-1].slotId;
    else 
        return getPort(olt.slotList[currentId.split("_")[1]-1].portList,currentId.split("_")[2]).portId;
}

/*************************************************
                得到真实Index
*************************************************/
function getActualIndex(paramId){
    currentId = paramId?paramId:currentId;
    var array = currentId.split("_");
    if ("fan" == array[0])
        return olt.fanList[array[1]-1].fanCardIndex;
    else if ("power" == array[0])
        return olt.powerList[array[1]-1].powerCardIndex;
    else if (0 == array[2])
        return olt.slotList[array[1]-1].slotIndex;
    else 
        return olt.slotList[array[1]-1].portList[array[2]-1].portIndex;
}

/****************************************************
                get entityId
****************************************************/
function getEntityId(){
    return olt.entityId;
}

/****************************************
        阻止事件冒泡
***************************************/
function preventBubble(e) {
    e = e || window.event;
    if (e.stopPropagation) {
        e.stopPropagation();           // 火狐阻止冒泡
    } else {
        e.cancelBubble = true;         // IE阻止冒泡
    }
    if(e.preventDefault){
        e.preventDefault();
    }
}

/**
 * 遍历整个JSON数组
 */
var walk = function(object,handler,parentNode){
    if( object instanceof Array){
        for (var i = 0; i < object.length; i++) {
            var item = object[i];
            walk(item,handler,object);
        };
    }else{
        handler(object,parentNode);
        if(object.menu){
            walk(object.menu,handler,object);
        }
    }
}

/**
 * 对于object，修改icon参数值
 * 对于TreeNode，调用其方法修改icon
 * @param {TreeNode|Object}node
 * @param {Boolean|Ext.menu.Item} proxy
 */
var modifyItemIcon = function( node, proxy ){
	var $checked;
	if(node instanceof Ext.menu.Item){
		var $id = node.id;
		node = serviceTree.getNodeById( $id );
	}
	if(typeof proxy != 'boolean'){
		node.attributes.$checked = !node.attributes.$checked;
		$checked = node.attributes.$checked;
	}else{
		$checked = node.$checked;
	}
	var icon = $checked ?  "/css/"+cssStyleName+"/menu/checked.gif" : "/css/"+cssStyleName+"/menu/unchecked.gif";
	if( node instanceof Ext.tree.TreeNode ){
		node.setIcon( icon );
	}else{
		node.icon = icon;
	}
}

/**
 * 显示菜单以及业务树
 * @param {Array}items
 * @param {EventObject}e
 */
function displayService(items,e){
	if(e){
		if(!e.getXY){
			e.getXY = function(){
				return [e.clientX, e.clientY];
			}
		}
		//display menu;
		if(e.type == 'contextmenu'){
			if (!serviceMenu) {
				serviceMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
			} else {
				//---存在bug--//
				serviceMenu.removeAll();
				serviceMenu.add(items)
			}
			serviceMenu.showAt(e.getXY());
		}
	}
    
    //delete by haojie for vlan and mirror
    /* if( currentId == window.$SERVICE_MENU_CUR_DISP_ID ){
    	return;
    }
    window.$SERVICE_MENU_CUR_DISP_ID = currentId*/
    
    //display service tree
    walk(items,function(node, parentNode){
    	if( typeof node.checked != 'undefined' ){
    		node.$checked = node.checked;
    		modifyItemIcon( node, true );
    		delete node.checked;
    	}else{
    		node.icon = '/images/s.gif';
    		node.iconCls = 'displayNone';
    	}
        if(!node.text){
            var idx = parentNode.indexOf( node );
            parentNode[idx] = {leaf:true,hidden:true};
            return ;
        }
        if(node.menu){
            node.children = node.menu;
            node.leaf = false;
            node.expanded = true;
        }else{
            node.leaf = true;
        }
    });
    if(serviceTree){
        serviceTree.destroy();
    }
    serviceTree = new Ext.tree.TreePanel({
        autoScroll: true, autoHeight:true,border:false,
        rootVisible:false,//useArrows:true,
        cls :"clear-x-panel-body",
        loader: new Ext.tree.TreeLoader(),
        root: new Ext.tree.AsyncTreeNode({
            expanded: true,
            children: items
        }),
        listeners:{
            click:function(node,e){
                if(node.isLeaf()){
                	node.attributes.handler.call( WIN, node );
                }
            }
        }
    });
    //serviceTree.setRootNode( root )
    serviceTree.render("viewRightPartTopBody")
}

function getControlBoardIndex(){
	for(var i=0 ;i < olt.slotList.length ;i++){
		//---由于只有主用MPUA板上有端口可以画，所以即便备用主控板的slotIndex为255也对解析没有影响---//
		if(olt.slotList[i].slotLogicNum == 0){
			return olt.slotList[i].slotRealIndex;
		}
	}
}
/**
 * 监控鼠标左键
 */
$( DOC ).keydown(function (e) {
    var keyCode = e.keyCode;
    if (keyCode == 17) {
        ctrlFlag = 1;
    }
});
/**
 * 监控鼠标右键键
 */
$( DOC ).keyup(function(event) {
    var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
    if (keyCode == 17) {
        ctrlFlag = 0;
    }
});

//清除掉相关功能中的树形菜单;
function clearTreePanel(){
	 if(serviceTree){
		 serviceTree.destroy();
	 }
}

var SVG_NS = "http://www.w3.org/2000/svg";
var ledDivWidth = 90;
var ledDivHeight = 20;
var createSvgOrVml = function(o){
	var $cont = DOC.createElementNS(SVG_NS,"svg");
	if(olt && olt.oltType && olt.oltType.toUpperCase() === 'PN8601') {
		$cont.setAttribute('width', ledDivHeight);
		$cont.setAttribute('height', ledDivWidth);
	} else {
		$cont.setAttribute('width', ledDivWidth);
		$cont.setAttribute('height', ledDivHeight);
	}
	o.append($cont);
	return $cont;
}
var circle = function(diam,color,$cont,title,left,top,title){
	var $led = DOC.createElement("div");
	$led.title= title;
	/*$led.style.left = left;
	$led.style.top = top;
	$led.style.width = "5px;"
	$led.style.height = "5px;"*/
	var o = DOC.createElementNS(SVG_NS,"circle");
	o.setAttribute("shape-rendering", false);
    o.setAttribute("title", title);
    o.setAttribute("r", 2.5);
    o.setAttribute("cx", left+(diam / 2+2)+"px");
    o.setAttribute("cy", top + (diam / 2+2)+"px");
    o.setAttribute("stroke", "black");
    o.setAttribute("stroke-width", 1);
    o.setAttribute("fill", color);
    o.style.cursor = "pointer";
    $cont.appendChild(o);
}
function drawSlotLight(color,left,top,id,$slot,title){
	if(DOC.createElementNS){
		if(!WIN.$cont){
			WIN.$cont = createSvgOrVml($slot);
		}
		circle(3,color,WIN.$cont,title,left,top,title);
		return $cont;
	}else{
		var $led = DOC.createElement("div");
		$led.style.position = "absolute";
		$led.style.top = String.format("{0}px",top);
		$led.style.left = String.format("{0}px",left);
		$led.style.width = "5px";
		$led.style.height = "5px";
		$led.setAttribute("alt", title);
		$slot.append($led);
		$led.innerHTML = String.format('<v:oval id="{0}" style="width:5px;height:5px" fillcolor={1} title="{2}" />',id,color,title);
	}
}