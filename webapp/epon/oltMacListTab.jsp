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
    <script type="text/javascript">
        var entityId = '${entityId}';
        var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
        Ext.onReady(function() {
            loadGrid();
        });
        
        function loadGrid(){
            window.store = new Ext.data.JsonStore({
                url : "/epon/loadOltMacListTab.tv",cache:true,
                baseParams:{entityId: entityId},
                fields : [
                    {name: 'topSysMacVid'},
                    {name: 'topSysMacAddr'},
                    {name: 'topSysMacSlot'},
                    {name: 'topSysMacPort'}
                ]
            });
           
            store.load();
            var grid = new Ext.grid.GridPanel({
                stripeRows:true,
      	   	  	region: "center",
      	   	  	bodyCssClass: 'normalTable',
                renderTo: 'gridContainer',
                viewConfig: {forceFit: true},
                height: 310,
                autoScroll: true,
                columns: [
                    {header: I18N.ELEC.slotNo, dataIndex: 'topSysMacSlot', align: 'center',renderer: slotRenderer},
                    {header: I18N.ELEC.portNo, dataIndex: 'topSysMacPort',  align: 'center'},
                    {header: 'VLAN ID' , dataIndex: 'topSysMacVid',  align: 'center'},
                    {header: I18N.ELEC.macAddr , dataIndex: 'topSysMacAddr',  align: 'center'}],
                sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
                store: store
            });
        }

        function slotRenderer(v,m,r){
			if(v == 254){
				return I18N.ELEC.aggr;
			}else{
				return v;
			}
        }
        
        function refresh(){
        	window.top.showWaitingDlg(I18N.EPON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
        	Ext.Ajax.request({
        		url: "/epon/refreshOltMacListTab.tv",disableCaching :true,
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
            window.parent.closeWindow('oltMacListTab')
        }
    </script>
</head>
	<body class="openWinBody">
		<div class="edge10">
			<div id="gridContainer"></div>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="refresh()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>