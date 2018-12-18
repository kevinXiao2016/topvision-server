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
    module onu
    import js/customColumnModel
</Zeta:Loader>
<script type="text/javascript">
    var pageSize = <%= uc.getPageSize() %>;
    var cm,columnModels,sm,store,grid,viewPort,tbar,bbar;
    
    Ext.onReady(function(){
        columnModels = [
            {header: "@ONU.tagName@", width:100, dataIndex: "tagName", align: "center", sortable: true},
            {header: "<div class=txtCenter>@COMMON.manu@</div>", width:100, dataIndex: "tagLevel", align: "center", renderer:renderManu}
        ];
        cm = new Ext.grid.ColumnModel({
            defaults : {
                menuDisabled : false
            },
            columns: columnModels
        });
        var cmConfig = CustomColumnModel.init("tagManagement",columnModels,{}),
                  cm = cmConfig.cm,
            sortInfo = cmConfig.sortInfo || {field: "tagName", direction: "ASC"};

        store = new Ext.data.JsonStore({
        	url: "/onu/loadOnuTags.tv",
            root: "data",
            totalProperty: "rowCount",
            fields: ["id","tagName","tagLevel"]
        });
        store.setDefaultSort("tagName", "ASC");
        store.load({params: {start:0,limit: pageSize}}); 
        tbar = new Ext.Toolbar({
            items : [{
                text : "@COMMON.add@",
                iconCls : "bmenu_new",
                handler: showOnuTagCreate
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
            stripeRows:true,
            cls:"normalTable",
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
            items: [grid]
        });
    
    });//end document.ready;
    function renderManu(){
    	return "<a href='javascript:;' onClick='deleteOnuTag();'>@COMMON.delete@</a> / <a href='javascript:;' onClick='modifyOnuTag()'>@COMMON.update@</a>";
    }
    
    function showOnuTagCreate(){
    	window.parent.createDialog("tagCreateView", "@ONU.tagCreate@", 600, 370, "onu/showOnuTagCreate.tv?pageId=" + window.parent.getActiveFrameId(), null, true, true);
    }
    
    function deleteOnuTag(){
    	window.top.showConfirmDlg("@COMMON.tip@", "@ONU.confirmTagDel@", function(type) {
    		if(type == 'no'){
    			return
    		}
    		var sm = grid.getSelectionModel(),record = sm.getSelected(),id = record.data.id;
        	$.ajax({
    			url : '/onu/deleteOnuTag.tv',
    			data : {
    				'onuTag.id' : id
    			},
    			type : 'post',
    			dataType : "json",
    			success : function(response) {
    				top.afterSaveOrDelete({
    					title : '@COMMON.tip@',
    					html : '<b class="orangeTxt">@COMMON.deleteOk@</b>'
    				});
    				refresh();
    			},
    			error : function(response) {
    				window.parent.showMessageDlg('@COMMON.error@','@COMMON.deleteEr@');
    			},
    			cache : false
    		});
    	});
    }

    function modifyOnuTag(){
    	var sm = grid.getSelectionModel(),record = sm.getSelected(),id = record.data.id, tagName = record.data.tagName;
    	window.parent.createDialog("tagModifyView", "@ONU.tagModify@", 600, 370, "onu/showOnuTagModify.tv?pageId=" + window.parent.getActiveFrameId() + "&onuTag.tagName=" + tagName + "&onuTag.id=" + id, null, true, true);
    }
    
    function refresh(){
    	if(store){
    		store.load({params: {start:0,limit: pageSize}}); 
    	}
    }
</script>
</head>
    <body class="whiteToBlack">
    
    </body>
</Zeta:HTML>

            