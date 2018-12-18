<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    PLUGIN  LovCombo
    MODULE  CM
</Zeta:Loader>
<html>
<head>
<script type="text/javascript">
$(function(){
    var store = new Ext.data.JsonStore({
        url: ('/cmpoll/loadCmListByObjectId.tv?objectId=${objectId}'),
        root: 'data',
        fields: ['mac','ip','status']
    });
    
    var cols=new Ext.grid.ColumnModel([
		{header: 'MAC', sortable: false, align: 'center', width:200,dataIndex: 'mac'},
		{header: 'IP', sortable: false, align: 'center', width:150,dataIndex: 'ip', renderer : ipRender},
		{header: "@COMMON.status@", sortable: false, align: 'center', width:80,dataIndex: 'status',renderer: statusRender
    }]);
    
    var grid=new Ext.grid.GridPanel({
        stripeRows:true,region: "center",bodyCssClass: 'normalTable',
        store:store,
        tbar:[{
            xtype: 'tbtext',
            text:"<span>@cmPoll.all@:&nbsp;</span><span id='cm_total_num'></span><span>&nbsp;&nbsp;&nbsp;&nbsp;@COMMON.online@:&nbsp;</span><span id='cm_online_num'></span>"
        }],
        cm:cols,
        viewConfig:{forceFit: true}
    });
    store.on('load',function(_s,records){
        $("#cm_total_num").text(records.length);
        var onlineNum=0;
        $.each(records,function(){
            if(this.data.status=='6'){// online
                onlineNum++;
            }
        });
        $("#cm_online_num").text(onlineNum);
    });
    store.load();
    new Ext.Viewport({layout: 'border', items: [grid]});
});

function statusRender(v,m,r){
    if(r.data.status == 6){ //online
    	return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
        "@COMMON.online@");
    }else{
    	return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
        "@COMMON.offline@");
    }
}

function ipRender(v,m,r){
    if(r.data.ip == "0.0.0.0"){ //online
    	return "-";
    }else{
    	return r.data.ip;
    }
    
}
</script>
</head>
<body></body>
</Zeta:HTML>
