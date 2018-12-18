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
    LIBRARY Socket
    MODULE platform
</Zeta:Loader>
<style type="text/css">
body {overflow : hidden;}
button {width: 75px;margin-left: 3px;}
#rightSegment{border: 2px solid gray;position: absolute;left: 320px;top:35px;}
#file {position: absolute; z-index: 1; left: -110px; top: -30px; width: 40px; height: 24px;}
.descStyle {width:100% ;height:150px;overflow: auto;border-bottom : 1px solid gray;	word-wrap:　break-word;}
.snmpParamStyle {border-bottom : 1px solid gray;}
.setValueStyle {width:100% ;height:30px;border-bottom : 1px solid gray;}
.resultStyle {width:100% ;border-bottom : 1px solid gray;}
.iconSize {height: 16px; width: 16px;}
#content {overflow: scroll;}
#tree {position: absolute;top:35px;}
.selected{background:url(/epon/image/checked.gif) no-repeat;}
.unselected{background:url(/epon/image/unchecked.gif) no-repeat;}
.bemnu_set {background-image: url(images/config.png) !important;}
.x-tree-root-ct{ overflow:visible }
</style>
<script type="text/javascript">
	var json, doc = document, timer = null ,HOST = '${host}',mibbleType='${mibbleType}',enterCount='${enterCount}';
	var handlerable = false;
	
	function doRefresh(url){
		location.href = url;
	}

	//------------------------------------------
	//	 when enter the page , this occure
	//-----------------------------------------
	function resize(){
		var h = $(DOC).height();
		var w = $(DOC).width();
		$("#content").css("height",h-$("#content").offset().top).css("width", w-330);
	}

	//------------------------------------------
	//	  				ENTRY
	//-----------------------------------------
	Ext.onReady(function(){	
		buildToolbar();
		bulidTree()
		addListener()
		if(host != null){
			$("#host").val( HOST );
		}
		resize();
		$("#readCommunity").val( '${community}' || 'public');
		$("#writeCommunity").val( '${writeCommunity}' || 'private');
		WIN.$content = $('#content');
		
		WIN.GLOBAL_SOCKET = new TopvisionWebSocket('mibbleBrowserService', {
		    onmessage: function (message) {
		    	if( message  == "-GETALL-END"){
					disableBtn( false );
					return;
				}
				var $message = Ext.decode( message );
				var $fra = $("<div>")
				var oid = $message.oid
				if(oid == "done"){
					$fra.append("<p>DONE: no more values for :   " + thisNodeText || "" +"</p>");
				}else{
					var rawOid = $message.rawOid
					var rear = oid.substring(rawOid.length)
					var value = $message.name + rear + " -> " + $message.value
					$fra.append("<p>GET NEXT :   " + value||"" +"</p>");
				}
				$content.append($fra)
				scrollBottom();
		    }
		});
		
	})

	function disableBtn( enable ){
		$("#getButton,#getNextButton,#getAllButton").attr("disabled", enable);
	}
	
	//------------------------------------------
	//	  GET SNMP VERSION 0:v1  1:v2  3:v3
	//-----------------------------------------
	function getVersion(){
		return 1 //v2
	}

	//------------------------------------------
	//			GET SNMP PARAM
	//-----------------------------------------
	function getSnmpParam(){
		var o = {}
		o.host = $("#host").val()
		o.readCommunity = $("#readCommunity").val()
		o.writeCommunity = $("#writeCommunity").val()
		o.community = $("#readCommunity").val()
		o.port = $("#port").val()
		o.oid = $("#oid").val()
		return o
	}

	//------------------------------------------
	//			SHOW COLLECT RESULT
	//-----------------------------------------
	function showData(json){
		var value = json.oid + " : " + json.value
		$("#content").append("<p>GET NEXT: " + value||"" +"</p>")
	}

	//------------------------------------------
	//	GET NODE AND EXPAND ALONG WITH PATH
	//-----------------------------------------
	function getSelectedNode(rawOid){
		var o = Ext.getCmp("treess")
		if(o.getNodeById(rawOid) == null){//如果节点没有展开过
			o.getRootNode().cascade(function(node){
				if(node.id  == rawOid){
					o.expandPath( node.getPath() )
					return false
				}
					
			})
		}else{
			o.expandPath(o.getNodeById(rawOid).getPath())	
		}
		
	}

	//------------------------------------------
	//				ADD LISTENERS
	//-----------------------------------------
	function addListener(){
		//////////////////  GET  ////////////////////
		$("#getButton").click(function(){
			var o = getSnmpParam()
			o.version = getVersion()
			if(!isIPLegal(o.host)){
				window.parent.showMessageDlg(I18N.mibble.tip, I18N.mibble.ipNotLegal )
				return;
			}
			$.ajax({
				url : '/mibble/get.tv',cache:false,dataType:'json',
				data : o,
				success: function(json){
					var value = json.name + " -> " + json.value						
					$("#content").append("<p>GET :   " + value||"" +"</p>")
					scrollBottom();
				},
				error:function(){
					$("#content").append("<p style='color:red'>GET :   Timeout! please ping the target </p>")
				}
			})
		})

		//////////////  GET  NEXT   ///////////////////
		$("#getNextButton").click(function(){
			var o = getSnmpParam()
			o.version = getVersion()
			if(!isIPLegal(o.host)){
				window.parent.showMessageDlg(I18N.mibble.tip, I18N.mibble.ipNotLegal  )
				return;
			}
			$.ajax({
				url : '/mibble/getNext.tv',cache:false,dataType:'json',
				data : o,
				success: function(json){
					var oid = json.oid
					var rawOid = json.rawOid
					var rear = oid.substring(rawOid.length)
					var value = json.name  + rear + " -> " + json.value
					$("#content").append("<p>GET NEXT :   " + value||"" +"</p>")
					$("#oid").val(json.oid)
					scrollBottom();
					try{
						getSelectedNode(json.rawOid)
						Ext.getCmp("treess").getNodeById(json.rawOid).select()
					}catch(e){}
				},
				error:function(){
					$("#content").append("<p style='color:red'>GET NEXT :   Timeout! please ping the target </p>")
				}
			})
		})

		///////////////   GET ALL    //////////////////
		$("#getAllButton").click(function() {
			var o = getSnmpParam()
			o.version = getVersion()
			if(Ext.getCmp("treess").getSelectionModel().getSelectedNode()){
				window.thisNodeText = Ext.getCmp("treess").getSelectionModel().getSelectedNode().text;
			}
			if(!isIPLegal(o.host)){
				window.parent.showMessageDlg(I18N.mibble.tip, I18N.mibble.ipNotLegal )
				return;
			}
			disableBtn( true );
			
			o.REQ_TYPE = 1;
			
			GLOBAL_SOCKET.send(o);
		})

		///////////////   GET ALL    //////////////////
		$("#stopButton").click(function(){
			 $.ajax({
				url : '/mibble/stopGetAll.tv',cache:false
			}) 
		})

		////////////   CLEAR ///////////////////
		$("#clearButton").click(function(){
			$("#content").empty()
		})

		
		$("#oid").bind("keyup",function(){
			if($(this).val().length>0){
				$("#getButton,#getNextButton,#getAllButton").attr("disabled",false);
				handlerable = true;
			}else{
				$("#getButton,#getNextButton,#getAllButton").attr("disabled",true);
				handlerable = false;
			}
		})
		window.onResize = resize
	}

	function bulidTree(){
		$.ajax({
			url : '/mibble/loadMibbles.tv?mibbleType='+mibbleType+'&enterCount='+enterCount,cache:false,dataType:'json',
			success : function(json){
				if(Ext.getCmp("treess") != null){
					Ext.getCmp("treess").destroy()
				}
				_bulidTree(json)
			},error : function(e){
				window.parent.showMessageDlg(I18N.mibble.tip, I18N.mibble.loadMibbleEr , "error" )
			}
		})
		enterCount++;
	}


	function _bulidTree(data){
		var height = $(DOC).height() - 35;
		var trree = new Ext.tree.TreePanel({
            id:'treess',
            rootVisible : false,
            listeners:{
                "click" : onTreeNodeClick
            },
            //forceLayout : true,
            baseCls : "background-color:transparent;",
            loadMask : "loading....", 
            containerScroll	 : true,
            width: 300,height:height,autoScroll: true,border: false
		})
		var root = new Ext.tree.TreeNode({
			id:'root',
			text : "RootNode"
		})
		for(var i=0; i<data.length;i++){
			var jsonNode = data[i]
			var node = new Ext.tree.TreeNode({
				id: jsonNode.oid,
				oid : jsonNode.oid,
				symbol : jsonNode.symbol,
				isLeaf : jsonNode.isLeaf,
				text : jsonNode.name
			})
			parseJson(node,jsonNode)
			root.appendChild(node)
		}
		trree.setRootNode(root)
		trree.render('tree')
	}

	function onTreeNodeClick(node){
		try{
			if(typeof node.attributes.symbol != 'undefined'){
				$("#desc").html("<pre>" + node.attributes.symbol + "</pre>")
				if(!handlerable){
					$("#getButton,#getNextButton,#getAllButton").attr("disabled",false);
					handlerable = true;
				}
			}else{
				$("#desc").html("")
				if(handlerable){
					$("#getButton,#getNextButton,#getAllButton").attr("disabled",true);
					handlerable = false;
				}
			}
			var parent = node.parentNode
			if(node.attributes.isLeaf && parent.attributes.text.indexOf("Entry") == -1 )
				$("#oid").val(node.attributes.oid+".0" )
			else
				$("#oid").val(node.attributes.oid || "")
			/*if(node.attributes.writeable)
				$("#value,#valueTD,#setButton").attr("disabled",false)
			else
				$("#value,#valueTD,#setButton").attr("disabled",true)*/
		}catch(e){}
	}

	function snmpV1(){
		$("#v3").hide()
		$("#v2").show()			
		Ext.getCmp('v1Item').setIconClass("selected")
		Ext.getCmp('v2Item').setIconClass("unselected")
		Ext.getCmp('v3Item').setIconClass("unselected")
		resize()
	}
	function snmpV2(){
		$("#v3").hide()
		$("#v2").show()
		Ext.getCmp('v1Item').setIconClass("unselected")
		Ext.getCmp('v2Item').setIconClass("selected")
		Ext.getCmp('v3Item').setIconClass("unselected")
		resize()
	}
	function snmpV3(){
		$("#v2").hide()
		$("#v3").show()
		Ext.getCmp('v1Item').setIconClass("unselected")
		Ext.getCmp('v2Item').setIconClass("unselected")
		Ext.getCmp('v3Item').setIconClass("selected")
		resize()
	}
	/*************************************************************
					创建顶部工具栏以及工具栏菜单
	*************************************************************/
	function buildToolbar() {
		jtb = new Ext.Toolbar();
		jtb.render('toolbar');
		var items = [];
		items[items.length] = {text: I18N.mibble.version , xtype:'button', iconCls:'bmenu_view',menu: {items: [
				{text: 'SNMP V2', id: "v2Item", /* handler: snmpV2,  */iconCls:'selected'}
				/* {text: 'SNMP V1', id: "v1Item", handler: snmpV1, iconCls:'unselected'},
				{text: 'SNMP V3', id: "v3Item", handler: snmpV3, iconCls:'unselected'}*/
				]}}
		items[items.length] = '-';
		items[items.length] = {text: I18N.mibble.loadMib , xtype:'button', id:"loadMib", iconCls:'bmenu_save',handler: selectMib};			
		items[items.length] = '-';
		items[items.length] = {text: "Ping",id:"ping", xtype:'button', iconCls:'bmenu_find',handler: onPingClick};
		items[items.length] = '-';
		items[items.length] = {text: I18N.mibble.exportResult , xtype:'button', iconCls:'bmenu_export',handler: onExportClick};
		jtb.add(items);
		jtb.doLayout();
	}

	function selectMib(){
		window.top.createDialog("modalDlg", I18N.mibble.loadMib, 600, 400, "/mibble/showMibbleSelectJsp.tv", null, true, true);
	}

	function reloadMib(){
		bulidTree()
	}

	function onPingClick() {
		var ip = $("#host").val();
		if(!ip){
			//return top.showMessageDlg("@COMMON.tip@", "ip " ,"error" )
			return $("#host").focus();
		}
		window.top.createDialog("modalDlg", 'Ping ' + ip, 600, 400, "entity/runCmd.tv?cmd=ping&ip=" + ip, null, true, true);
	}

	function onExportClick(){
		var text = $("#content").html()
		window.top.createMessageWindow("mibbleResult", I18N.mibble.result ,480,360,text,null,true,null,null,true)
	}
	
	////------------------保密原则，故不提供上传，改为选择-----------------///
	function loadMib(){
		//getFlash().upload("/mibble/uploadMib.tv?fileName="+fileName+"&r="+Math.random())
	}


	function parseJson(root,json){			
		if(json.children != null && json.children.length > 0){
			for(var i =0 ;i < json.children.length ; i++){
				var trunk = new Ext.tree.TreeNode({
					id: json.children[i].oid,
					text : json.children[i].name,
					iconCls : 'iconSize',
					oid : json.children[i].oid,
					symbol : json.children[i].symbol,
					isLeaf : json.children[i].isLeaf,
					writeable : json.children[i].writeable,
					icon : json.children[i].icon || null
				})
				root.appendChild(trunk)
				parseJson(trunk,json.children[i])
			}
		}
	}
	function onSelect(obj){
	    fileSize = obj.fileSize;
	    window.fileName = obj.fileName
	   	Ext.getCmp("loadMib").enable() 
	}

	function addTOTree(data){
		var node = new Ext.tree.TreeNode({
			id: jsonNode.oid,
			oid : data.oid,
			symbol : data.symbol,
			isLeaf : data.isLeaf,
			text : data.name
		})
		parseJson(node,data)
		Ext.getCmp("treess").getRootNode().appendChild(node)
	}
	
	function scrollBottom(){
		$content[0].scrollTop = $content[0].scrollHeight
	}

	function onComplete(){					
		$.ajax({
			url:'/mibble/loadMibble.tv',data:{fileName : fileName},dataType:'json',
			success:function(data){
				addTOTree(data)
			},error:function(){
				window.parent.showMessageDlg(I18N.mibble.result, I18N.mibble.loadMibbleEr ,"error" )
			}
		})
	}

	function getFlash(){
		return (navigator.appName.indexOf ("Microsoft") !=-1)?window["FileUpload"]:document["FileUpload"]
	}

	//验证ip地址的合法性
	function isIPLegal(ip) {
		if(!ip){return false}
		ip = ip.split(".");
		if(ip.length != 4){
			return false;
		}else{
			for(var k=0; k<4; k++){
				if(ip[k].length==0 || isNaN(ip[k]) || ip[k].length>3 || parseInt(ip[k])<0 || parseInt(ip[k])>255){
					return false
				}
			}
		}
		return true;
	}
	</script>
</head>


<body >
<div id="toolbar" style="position: absolute; left: 0; top: 0;width:100%"></div>
<div id="tree" ></div>
<div id="rightSegment">
<div  id="desc" class=descStyle ></div>
<div id="snmpParam" class=snmpParamStyle>
	<div id=v2>
		<table>
			<tr>
				<td>@mibble.host@:</td>
					<td><input id=host type="text" class="normalInput"/></td>
				<td width=5></td>
				<td>@mibble.community@</td>
				<td><input id=readCommunity type="password" class="normalInput"/></td>
			</tr>
			<tr>
				<td>@mibble.portNum@:</td>
					<td><input id=port type="text" value="161" class="normalInput"/></td>
				<!-- <td width=5></td>
				<td>writeCommunity:</td><td><input id="writeCommunity" type="password" /></td> -->
			</tr>
		</table>
	</div>
	<div id=v3  style="display:none" >
		<table>
			<tr>
				<td>@mibble.host@:</td><td><input id=hostV3 type="text"  class="normalInput"/></td>
				<td width=5></td>
				<td>context name:</td><td><input id=contextName type="password" value="public"  class="normalInput"/></td>
			</tr>
			<tr>
				<td>@mibble.portNum@:</td><td><input id=portV3 type="text" value="161"  class="normalInput"/></td>
				<td width=5></td>
				<td>context engine:</td><td><input id=contextEngine type="password" value="public"  class="normalInput"/></td>
			</tr>
			<tr>
				<td>@mibble.username@:</td><td colspan=3><input id=username type="text" value="admin" class="normalInput"/></td>
			</tr>
			<tr>
				<td>@mibble.encryptMode@:</td><td><select id=authentication ><option>MD5</option><option>SHA-1</option></select></td>
				<td width=5></td>
				<td disabled>@mibble.encryptPWD@:</td><td><input id=authPassword type="password" value="public" disabled class="normalInput"/></td>
			</tr>
			<tr>
				<td disabled>Privacy:</td><td><input id=privacy type="password" value="public" disabled  class="normalInput"/></td>
				<td width=5></td>
				<td disabled>@mibble.privacyPWD@:</td><td><input id=privacyPassword type="password" value="public" disabled  class="normalInput"/></td>
			</tr>
		</table>
	</div>
</div>
<div id="setValue" class=setValueStyle>
	<table>
		<tr>
			<td>oid:</td><td><input id=oid type="text" style="width:450px" class="normalInput"/></td>
		</tr>
		<!-- <tr>
			<td id=valueTD disabled>Value</td>
			<td><input id=value type="text" style="width:450px" disabled/></td>
		</tr> -->
	</table>
</div>
<div id="result" class="resultStyle">
	<button id="getButton" disabled title="perform SNMP get operation" class="BUTTON75">Get</button>
	<button id="getNextButton" disabled title="perform SNMP get next operation" class="BUTTON75">Get Next</button>
	<button id="getAllButton" disabled title="perform SNMP get all operation" class="BUTTON75">Get All</button>
	<!-- <button id="setButton" title="perform SNMP set operation" disabled>Set</button> -->
	<button id="stopButton"  class="BUTTON75">Stop</button>
	<button id="clearButton" title="clear SNMP result area" class="BUTTON75">Clear</button>
	<div  id="content" ></div>
</div>
</div>
</body>
</Zeta:HTML>