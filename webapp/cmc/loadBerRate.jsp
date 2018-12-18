<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
</Zeta:Loader>
<script type="text/javascript">
	var pageSize = <%= uc.getPageSize() %>;
    // main.js的addView函数会调用
    function doRefresh(){
        window.self._refreshNoiseRateList();
    }
    
    $(function() {
        var cm, store,grid,pageBar,sm;
        
        function _load() {
            store.load({
                params : {
                    start : 0,
                    limit : pageSize
                }
            });
        }
        
        window.self._refreshNoiseRateList=function(){
            _load();
        }
        //---------------------------------------------------------
        //sm=new Ext.grid.CheckboxSelectionModel();
        sm=new Ext.grid.RowSelectionModel();
        
        cm = new Ext.grid.ColumnModel([
        //sm,
        {
            header : "@CCMTS.manageIP@",
            //sortable : true,
            dataIndex : 'manageIp',
            //menuDisabled : false,
            align:'center'
        }, {
            header : "@CCMTS.macAddress@",
            dataIndex : 'macAddress',
            align:'center'
        }, {
            header : "@CMC.label.deviceName@",
            dataIndex : 'cmcName',
            align:'center'
        }, {
            header : "@CMC.label.type@",
            dataIndex : 'typeName',
            align:'center'
        }, {
            header : "@portName@",
            dataIndex : 'cmcPortName',
            align:'center'
        }, {
            header : "@CHANNEL.snr@",
            //sortable : true,
            dataIndex : 'noise',
            //menuDisabled : false,
            align:'center'
        }, {
            header : "@checkTime@",
            align:'center',
            dataIndex : 'dt'
        }]);

        store = new Ext.data.JsonStore({
            url : '/cmcperf/loadNoiseRate.tv',
            root : 'data',
            totalProperty : 'rowCount',
            remoteSort : true,
            //YangYi注释 2014-04-17 EMS-8274 
            //sortInfo: {
            //    field: 'noise',
            //    direction: 'ASC'
            //},
            fields : [ 'cmcId', 'cmcPortId' ,'ip','mac', 'name', 'typeName', 'cmcPortName', {"name":"noise","mapping":'noiseString'},{"name":"dt","mapping":'dtStr'} ]
        });

        pageBar = new Ext.PagingToolbar({
            id : 'extPagingBar',
            pageSize : pageSize,
            store : store,
            displayInfo : true,
            items : [ "-", String.format("@COMMON.pagingTip@",pageSize), '-' ]
        });

        grid = new Ext.grid.GridPanel({
        	stripeRows:true,
        	baseCls:'normalTable',
        	region : 'center',
            store : store,
            border : false,
            animCollapse: false, 
            trackMouseOver:false, 
            viewConfig:{
              autoFill:true,
              scrollOffset:20,
              forceFit : true
            },
            renderTo : document.body,
            bbar : pageBar,
            height : $(window).height(),
            cm : cm,
            sm : sm
        });

        store.load({params:{start:0, limit: pageSize}});
        
        new Ext.Viewport({layout: 'border', items: [grid]});
        window.self._refreshNoiseRateList();
    });
</script>
</head>
<body></body>
</Zeta:HTML>
