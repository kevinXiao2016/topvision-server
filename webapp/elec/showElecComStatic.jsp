<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <%@include file="/include/cssStyle.inc"%>
    <fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
    <script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
    <title><fmt:message bundle="${i18n}" key="SERVICE.trapCfg" /></title>
    <link rel="stylesheet" type="text/css" href="/css/gui.css" />
    <link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
    <style type="text/css">
        .back {
            background-color : #95B579;
            border-bottom : 2px solid yellow;
            border-right:1px solid yellow;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css" />
    <link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css" />
    <script type="text/javascript" src="/js/ext/ext-base.js"></script>
    <script type="text/javascript" src="/js/ext/ext-all.js"></script>
    <script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
    <script type="text/javascript" src="/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="/epon/js/topAnimation.js"></script>
    <script type="text/javascript">
  	    var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
        var entityId = '${entityId}';
        var comIndex = '${comIndex}';
        Ext.onReady(function() {
            loadGrid();
            //刚进入时进行闪动提示
            showTip();
        });
        
        function showTip(){
        	 $("#tip").jShine();
        }
        
        function loadAgain(){
        	setTimeout(function(){
        		window.store.load();
        	},5000);
        }
        
        function loadGrid(){
        	window.realStore = new Ext.data.JsonStore();
        	window.prev = null;
            window.store = new Ext.data.JsonStore({
            	url : "epon/elec/fetchElecComStaticData.tv",cache:true,
            	root : 'data',
                baseParams: {entityId: entityId,comIndex:comIndex},
                fields : [
	                {name: 'onuComStatisRxBytes'},
	                {name: 'onuComStatisTxBytes'}, 
	                {name: 'onuComStatisParityErr'}, 
	                {name: 'onuComStatisBrk'},
	                {name: 'onuComStatisRxDrops'},
	                {name: 'onuComStatisTxDrops'},
	                {name: 'onuComStatisRecvFromNet'},
	                {name: 'onuComStatisSendToNet'}
                ]
            });
            
            store.on("load",function(s,r,o){
            	loadAgain();
            	//超过1000条自动翻转
            	if(realStore.getCount()>1000){
            		realStore.removeAll();
            		prev = null;
            		//翻转时进行闪动提示
            		showTip();
            	}
            	var o = {}
            	var t = r[0].data;
            	//deep copy
           		o.onuComStatisRxBytes = t.onuComStatisRxBytes || 0;
           		o.onuComStatisTxBytes = t.onuComStatisTxBytes || 0;
           		o.onuComStatisParityErr = t.onuComStatisParityErr || 0;
           		o.onuComStatisBrk = t.onuComStatisBrk || 0;
           		o.onuComStatisRxDrops = t.onuComStatisRxDrops || 0;
           		o.onuComStatisTxDrops = t.onuComStatisTxDrops || 0;
           		o.onuComStatisRecvFromNet = t.onuComStatisRecvFromNet || 0;
           		o.onuComStatisSendToNet = t.onuComStatisSendToNet || 0;
            	if(prev != null){
            		//更新总计信息
                    var rec = realStore.getAt(0);
                    rec.beginEdit();
                    rec.set("onuComStatisRxBytes",t.onuComStatisRxBytes);
                    rec.set("onuComStatisTxBytes",t.onuComStatisTxBytes);
                    rec.set("onuComStatisParityErr",t.onuComStatisParityErr);
                    rec.set("onuComStatisBrk",t.onuComStatisBrk);
                    rec.set("onuComStatisRxDrops",t.onuComStatisRxDrops);
                    rec.set("onuComStatisTxDrops",t.onuComStatisTxDrops);
                    rec.set("onuComStatisRecvFromNet",t.onuComStatisRecvFromNet);
                    rec.set("onuComStatisSendToNet",t.onuComStatisSendToNet);
                    rec.commit();
            		//首次，添加一个总数
            		if(typeof t.onuComStatisRxBytes != 'undefined' && t.onuComStatisRxBytes != "" )
	            		t.onuComStatisRxBytes = t.onuComStatisRxBytes - prev.onuComStatisRxBytes;
	            	else
            			t.onuComStatisRxBytes = -1
            		if(typeof t.onuComStatisTxBytes != 'undefined' && t.onuComStatisTxBytes != "" )
            			t.onuComStatisTxBytes = t.onuComStatisTxBytes - prev.onuComStatisTxBytes;
            		else
                        t.onuComStatisTxBytes = -1
                    if(typeof t.onuComStatisParityErr != 'undefined' && t.onuComStatisParityErr != "" )
                    	t.onuComStatisParityErr = t.onuComStatisParityErr - prev.onuComStatisParityErr;
                    else
                        t.onuComStatisParityErr = -1
                    if(typeof t.onuComStatisBrk != 'undefined' && t.onuComStatisBrk != "" )
                    	t.onuComStatisBrk = t.onuComStatisBrk - prev.onuComStatisBrk;
                    else
                        t.onuComStatisBrk = -1
                    if(typeof t.onuComStatisRxDrops != 'undefined' && t.onuComStatisRxDrops != "" )
                    	t.onuComStatisRxDrops = t.onuComStatisRxDrops - prev.onuComStatisRxDrops;
                    else
                        t.onuComStatisRxDrops = -1
                    if(typeof t.onuComStatisTxDrops != 'undefined' && t.onuComStatisTxDrops != "" )
                    	t.onuComStatisTxDrops = t.onuComStatisTxDrops - prev.onuComStatisTxDrops;
                    else
                        t.onuComStatisTxDrops = -1
                    if(typeof t.onuComStatisRecvFromNet != 'undefined' && t.onuComStatisRecvFromNet != "" )
                    	t.onuComStatisRecvFromNet = t.onuComStatisRecvFromNet - prev.onuComStatisRecvFromNet;
                    else
                        t.onuComStatisRecvFromNet = -1
                    if(typeof t.onuComStatisSendToNet != 'undefined' && t.onuComStatisSendToNet != "" )
                    	t.onuComStatisSendToNet = t.onuComStatisSendToNet - prev.onuComStatisSendToNet;
                    else
                        t.onuComStatisSendToNet = -1
	                realStore.insert(1, r);
            	}else{
            		var rec = r[0].copy();
            		rec.id = Ext.data.Record.id(rec);
                    realStore.insert(0, [rec]);
            	}
           		prev = o;
            })
            store.load();
            var grid = new Ext.grid.GridPanel({
                renderTo: 'comStasticContainer',
                width: 625,height: 333,
                autoScroll: true,
                columns: [
                    {header: I18N.ELEC.RX , dataIndex: 'onuComStatisRxBytes', width: 60, align: 'center',renderer:columnRender},
                    {header: I18N.ELEC.TX , dataIndex: 'onuComStatisTxBytes', width: 60, align: 'center',renderer:columnRender},
                    {header: I18N.ELEC.PARITY , dataIndex: 'onuComStatisParityErr', width: 80, align: 'center',renderer:columnRender},
                    {header: I18N.ELEC.BRK , dataIndex: 'onuComStatisBrk', width: 80, align: 'center',renderer:columnRender},
                    {header: I18N.ELEC.RXDROP , dataIndex: 'onuComStatisRxDrops', width: 80, align: 'center',renderer:columnRender},
                    {header: I18N.ELEC.TXDROP , dataIndex: 'onuComStatisTxDrops', width: 80, align: 'center',renderer:columnRender},
                    {header: I18N.ELEC.RECVNET , dataIndex: 'onuComStatisRecvFromNet', width: 80, align: 'center',renderer:columnRender},
                    {header: I18N.ELEC.SENDNET , dataIndex: 'onuComStatisSendToNet', width: 80, align: 'center',renderer:columnRender}],
                sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
                store: realStore
            });
            
        }
        
        function cancelClick() {
            window.parent.closeWindow('comPortStastic')
        }
        
        function columnRender(value, meta, record,rowIndex ){
        	if(rowIndex ==0 ){
        		meta.css = "back";
        		if(value == "")
        			return I18N.ELEC.total + 0;
        		else
        			return I18N.ELEC.total + value;
        	}
        	if(value == -1 || value === ""){
        		return "-";
        	}else{
        		return value;
        	}
        }
        
        function clearStatics(){
        	Ext.Ajax.request({
        		url : 'epon/elec/clearElecComStatic.tv',disableCaching :true,
        		params:{entityId: entityId,comIndex:comIndex},
        		success:function(){
        			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.cfgOk );
        			realStore.removeAll();
        			prev = null; 
        		},failure:function(){
        			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.cfgEr );
        		}
        	})
        }
        function authLoad(){
        	if(!operationDevicePower){
        		$("#clear").attr("disabled",true);
        	}
        }
    </script>
</head>
<body class="POPUP_WND" onload="authLoad()">
    <div id="comStasticContainer" style="padding-left:11px;padding-top:11px;"></div>
    <div style="padding-top: 10px;padding-left: 255px">
        <span id="tip" style="width:205px;"><fmt:message bundle="${i18n}" key="ELEC.over1000Tip" /></span>
        <button id="clear" class=BUTTON95 type="button"
               onMouseOver="this.className='BUTTON_OVER95'"
               onMouseOut="this.className='BUTTON95'"
               onMouseDown="this.className='BUTTON_PRESSED95'"
               onclick="clearStatics()"><fmt:message bundle="${i18n}" key="ELEC.clear" />
        </button>&nbsp;&nbsp;
        <button id="cancelBt" class=BUTTON75 type="button"
               onMouseOver="this.className='BUTTON_OVER75'"
               onMouseOut="this.className='BUTTON75'"
               onMouseDown="this.className='BUTTON_PRESSED75'"
               onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" />
        </button>
    </div>
</body>
</html>