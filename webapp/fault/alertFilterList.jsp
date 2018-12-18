<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module fault
    import js/customColumnModel
</Zeta:Loader>
<style type="text/css">
	.topbarBottomLine{
		border-top: 1px solid #d7d7d7;
		border-top: 1px solid #d7d7d7 !important;
	}
</style>
<script type="text/javascript">
    var pageSize = <%= uc.getPageSize() %>;
    var cm,columnModels,sm,store,grid,viewPort,tbar,bbar;
    var filterActived = ${filterActived};
    var level = '${level}';
    
    Ext.onReady(function(){
    	$('#filterActived').attr('checked', filterActived);
    	$('#level').val(level);
    	
    	$('#filterActived').on('click', function(e) {
    		var x = $(this).attr('checked');
    		var checked = $(this).is(':checked');
    		var tip = checked ? '@fault/Alert.confirmOpenAlertFilter@' : '@fault/Alert.confirmCloseAlertFilter@';
    		window.parent.showConfirmDlg('@COMMON.tip@', tip, function(type) {
                if (type == 'no') {
                	$('#filterActived').attr('checked', !x)
                    return;
                }
                $.ajax({
                    url: '/fault/saveFilterActived.tv',
                    type: 'POST',
                    data: {
                    	filterActived: checked
                    },
                    dataType:"json",
                    async: 'false',
                    success: function() {
                    	top.afterSaveOrDelete({
                            title: '@fault/Alert.tip@',
                            html: '<b class="orangeTxt">@fault/ALERT.saveConfigToolTip@</b>'
                        });
                    }, error: function() {
                    	$('#filterActived').attr('checked', !checked);
                    	window.top.showMessageDlg("@COMMON.tip@", "@fault/ALERT.saveConfigFailToolTip@");
                    }, cache: false,
                    complete: function (XHR, TS) { XHR = null }
                });
            });
    	});
    	
    	$('#level').on('change', function(e) {
    		var curLevel = $(this).val();
            window.top.showConfirmDlg('@COMMON.tip@', '@fault/Alert.modifyCurrentSystemAlertLevel@', function(type) {
                if (type == 'no') {
                    return;
                }
                $.ajax({
                    url: '/fault/saveMinLevel.tv',
                    type: 'POST',
                    data: {
                    	level: curLevel
                    },
                    dataType:"json",
                    success: function() {
                        top.afterSaveOrDelete({
                            title: '@fault/Alert.tip@',
                            html: '<b class="orangeTxt">@fault/ALERT.saveConfigToolTip@</b>'
                        });
                    }, error: function() {
                        $('#level').val(level);
                        window.top.showMessageDlg("@COMMON.tip@", "@fault/ALERT.saveConfigFailToolTip@");
                    }, cache: false,
                    complete: function (XHR, TS) { XHR = null }
                });
            });
        });
    	
        columnModels = [
            {header: "<div class=txtCenter>@fault/ALERT.name@</div>", width:2, dataIndex: "name", align: "center", sortable: true},
            {header: "<div class=txtCenter>@fault/ALERT.description@</div>", width:4, dataIndex: "note", align: "center", sortable: false},
            {header: "<div class=txtCenter>@COMMON.manu@</div>", width:1, dataIndex: "dataIndex5", align: "center", sortable: false, renderer: rendererOperation}
        ];
        cm = new Ext.grid.ColumnModel({
            defaults : {
                menuDisabled : false
            },
            columns: columnModels
        });
        var cmConfig = CustomColumnModel.init("alertFilter",columnModels,{}),
                  cm = cmConfig.cm,
            sortInfo = cmConfig.sortInfo || {field: "name", direction: "ASC"};

        store = new Ext.data.JsonStore({
            url: "/fault/loadALertFilter.tv",
            root: "data",
            totalProperty: "rowCount",
            remoteSort: true,
            fields: ["filterId", "name", "note", "typeIds", "onuLevel"]
        });
        store.load({params: {start:0,limit: pageSize}});
        tbar = new Ext.Toolbar({
            items : [{
            	text : '',
            	xtype : 'tbtext'
            },{
            	xtype : 'tbspacer',
            	width : 4 
            },{
                text : "@COMMON.add@",
                iconCls : "miniIcoAdd",
                handler : function(){
                	top.createDialog("modalDlg", "@fault/Alert.addAlertFilter@", 800, 600, "/fault/showAddAlertFilter.tv", null, true, true, function() {
                		   store.reload();	
                	});
                }
            }]
        });
        bbar = new Ext.PagingToolbar({
            id: "extPagingBar",
            pageSize: pageSize,
            store: store,
            displayInfo: true,
            items: ["-", String.format("@COMMON.displayPerPage@", pageSize), "-"]
        });
        grid = new Ext.grid.GridPanel({
        	border: false,
            stripeRows:true,
            cls : 'topbarBottomLine',
            bodyCssClass: "normalTable",
            region: "center",
            store: store,
            bbar : bbar,
            enableColumnMove : true,
            tbar : tbar,
            cm: cm,
            viewConfig:{ forceFit: true }
        });
        viewPort = new Ext.Viewport({
            layout: "border",
            border: false,
            items: [grid,{
            			border: false,
            			region: 'north',
                       height: 30,
                       contentEl: 'topPart'
            }]
        });
    
    });//end document.ready;
    
    function rendererOperation(v, p, record) {
    	var editStr = "<a class='yellowLink' href='javascript:;' onclick='showEditAlertFilter({0})' >@fault/ALERT.editor@</a>";
    	var deleteStr = "<a class='yellowLink' href='javascript:;' onclick='showDeleteAlertFilter({0})'/>@fault/ALERT.delete@</a>";
    	
    	var filterId = record.data.filterId;
    	
    	return String.format(editStr, filterId) + " / " + String.format(deleteStr, filterId);
    }
    
    function showEditAlertFilter(filterId) {
    	top.createDialog("modalDlg", "@ALERT.editAlertFilter@", 800, 600, "/fault/showEditAlertFilter.tv?filterId=" + filterId, null, true, true, function() {
            store.reload();  
        });
    }
    
    function showDeleteAlertFilter(filterId) {
    	window.parent.showConfirmDlg('@COMMON.tip@', '@fault/Alert.confirmDeleteAlertFilter@', function(type) {
    		if (type == 'no') {
    			return;
    		}
    		$.ajax({
                url: '/fault/deleteAlertFilter.tv',
                type: 'POST',
                data: {
                	filterId: filterId
                },
                dataType:"json",
                async: 'false',
                success: function() {
                	store.reload();  
                }, error: function() {
                }, cache: false,
                complete: function (XHR, TS) { XHR = null }
            });
    	});
    }
</script>
</head>
    <body class="whiteToBlack">
    	<div id="topPart">
    		<ul class="leftFloatUl">
		        <li class="pT10 pL10 blueTxt"><input id="filterActived" type="checkbox">@fault/ALERT.activateAlertFilter0@</li>
    			<li class="pT10 pL10 blueTxt">@fault/ALERT.currentSystemAlertLevel@@COMMON.maohao@</li>
    			<li class="pT5">
    				<select class="normalSel" id="level">
	    				<option value="6">@WorkBench.emergencyAlarm@</option>
	    				<option value="5">@WorkBench.seriousAlarm@</option>
	    				<option value="4">@WorkBench.mainAlarm@</option>
    					<option value="3">@WorkBench.minorAlarm@</option>
    					<option value="2">@WorkBench.generalAlarm@</option>
    					<option value="1">@WorkBench.message@</option>
    				</select>
    			</li>
    		</ul>
    	</div>
    </body>
</Zeta:HTML>