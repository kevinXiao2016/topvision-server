<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
    <script type="text/javascript" src="/epon/js/topAnimation.js"></script>
    <script type="text/javascript">
   	    var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
        var entityId = '${entityId}';
        var vidList = ${vidList};
        var portList = ${portList};
        var doc = document;
        Ext.onReady(function() {
        	bulidToolbar();
            loadGrid();
            Ext.getCmp("vidCombo").focus();
            Ext.getCmp("portCombo").focus().setValue("");
        });
        
        function loadGrid(){
            window.store = new Ext.data.JsonStore({
                url : "epon/igmp/loadIgmpSnoopingData.tv",cache:true,
                baseParams:{entityId: entityId},
                fields : [
                    {name: 'firstRow'},
                    {name: 'secondRow'},
                    {name: 'lastChangeTime'}
                ]
            });
            store.on("load",function(s,r,o){
            	if(r.length >0){
	            	var date = r[0].data.lastChangeTime;
	            	doc.getElementById("tooltip").innerHTML = String.format(I18N.IGMP.lastChangeTimeTip,date)
            	}else{
            		doc.getElementById("tooltip").innerHTML = I18N.IGMP.dataUnCollect;
            	}
            })
            
            store.load();
            var grid = new Ext.grid.GridPanel({
                stripeRows:true,region: "center",bodyCssClass: 'normalTable',
                renderTo: 'gridContainer',
                height: 323,
                stripeRows:true,
        		region: "center",
        		bodyCssClass: 'normalTable',
                autoScroll: true,
                viewConfig : {forceFit : true},
                tbar : bulidToolbar(),
                columns: [
                    {header: I18N.EPON.mvlan , dataIndex: 'firstRow', width: 60, align: 'center'},
                    {header: I18N.EPON.port , dataIndex: 'secondRow', width: 520, align: 'center'}],
                sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
                store: store
            });
        }
        
        function bulidToolbar(){
            var items = []
            // add a Button with the menu
        	items[items.length] = { xtype: 'label', text: I18N.IGMP.mvlanId + ":"}
            items[items.length] = { xtype: 'tbspacer', width: 5 }
            items[items.length] = { xtype: 'combo',store:vidList, id: "vidCombo"}
            items[items.length] = { xtype: 'tbspacer', width: 15 }
            items[items.length] = { xtype: 'label', text: String.format("PON{0}:",I18N.EPON.port)}
            items[items.length] = { xtype: 'tbspacer', width: 5 }
            items[items.length] = { xtype: 'combo',  id: "portCombo", transform: transform(),triggerAction :"all",lazyRender : true}
            items[items.length] = { xtype: 'tbspacer', width: 15 }
            items[items.length] = { xtype: 'button',text: I18N.COMMON.query ,  iconCls: 'bmenu_find',handler: query}
            return items;
        }
        
        function transform(){
        	var select = doc.createElement("select");
        	select.id = "transform";
        	var o = document.createElement("option");
        	for(var i=0 ;i <portList.length;i++){
        		var o = document.createElement("option");
        		o.value = portList[i];
        		o.innerHTML = getLocationByIndex(portList[i],"pon");
        		select.appendChild(o);
        	}
        	return select;
        }
        
        //通过mibIndex获得num
        function getNum(index, s){
            var num;
            switch (s)
            {
            case 1: num = (index & 0xFF000000) >> 24;//SLOT
                break;
            case 2: num = (index & 0xFF0000) >> 16;//PON/SNI
                break;
            case 3: num = (index & 0xFF00) >> 8;//ONU
                break;
            case 4: num = index & 0xFF;//UNI
                break;
            }
            return num;
        }
        //通过index获得location
        function getLocationByIndex(index, type){
            var t = (parseInt(index / 65536) * 256) + (index % 256);
            if(type == "pon") {
            	var loc = getNum(t, 1) + "/" + getNum(t, 2)
            	return loc
            }
            var loc = getNum(t, 1) + "/" + getNum(t, 2) + ":" + getNum(t, 3);
            if(type == "uni"){
                loc = loc + "/" + getNum(t, 4)
            }
            return loc
        }
        
        function query(){
        	var vid = Ext.getCmp("vidCombo").getValue();
        	var portIndex = Ext.getCmp("portCombo").getValue();
        	store.setBaseParam("vid" , vid);
        	store.setBaseParam("portIndex" , portIndex);
        	store.reload();
        }
        
        
        function refresh(){
        	window.top.showWaitingDlg(I18N.EPON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
        	Ext.Ajax.request({
        		url: "/epon/igmp/refreshIgmpSnooping.tv",disableCaching :true,
        		params:{entityId:entityId},
        		success:function(){
        			window.parent.closeWaitingDlg()
        			window.location.reload();
        		},failure:function(){
        			window.parent.closeWaitingDlg()
        			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
        		}
        	})
        }
        
        function cancelClick() {
            window.parent.closeWindow('igmpSnooping')
        }
        function authLoad(){
        	if(!operationDevicePower){
        		$("#clear").attr("disabled",true);
        	}
        }
    </script>
</head>
<body class="openWinBody" onload="authLoad()">
<div class="edge10">
	<div id="gridContainer"></div>
    <div id="tooltip" style="text-align: center;margin-top: 10px;"></div>
</div>
    <Zeta:ButtonGroup>
		<Zeta:Button onClick="refresh()" icon="miniIcoSaveOK">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>