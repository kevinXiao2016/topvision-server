<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/zetaframework/Validator.js"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var cm;
           
var store = null;
var grid = null;

function downLoadFile(){
	window.location.href="cm/downLoadCmInfoImportTemplate.tv";
}

/**
 * YangYi增加@20130910,用于编辑按钮触发
 */
function renderOpeartion(value, cellmate, record){
    var cmMacAddr = record.data.cmMacAddr;
    return String.format("<a href='javascript:;' onClick='openBaseInfo(\"{0}\")'>@ccm/CCMTS.CmImport.modify@</a>", cmMacAddr);
}
/**
 * YangYi增加@20130910,用于编辑按钮触发
 */
function openBaseInfo(cmMacAddr){
	window.top.createDialog('modifyCmProperty', I18N.text.modifyCmImportInfo, 600, 370, 
			'cm/showCmInfoConfig.tv?cmMac=' + cmMacAddr  , null, true, true);
}

/**
 * YangYi修改@20130910,增加一列，编辑按钮
 */
Ext.onReady(function () {
    var w = document.body.clientWidth;
    var h = document.body.clientHeight;
    
    var columns = [
        {header: "<div style='text-align:center'>" + I18N.CMC.title.Alias +"</div>", width: 300, sortable:false, align: 'left', dataIndex: 'cmAlias'},
        {header: 'MAC', width: 150, sortable:false, align: 'center', dataIndex: 'cmMacAddr'},   
        {header: I18N.CMC.title.usage, width: 150, sortable:false, align: 'center', dataIndex: 'cmClassified'},
        {header: I18N.CMC.title.importDate,width: 150, sortable:false, align: 'center', dataIndex: 'time'},
        {header: I18N.CHANNEL.operation, width: 150, sortable:false, align: 'center', dataIndex: 'op', renderer: renderOpeartion}
    ];
    store = new Ext.data.JsonStore({
        url: 'loadCmImportInfoList.tv',
        root: 'data',
        totalProperty: 'rowCount',
        remoteSort: true, 
        fields: [ 'cmMacAddr', 'cmAlias', 'cmClassified', 'time' ]
    });

    var toolbar = [ 
		{text: I18N.CMC.title.importExcel, iconCls: 'bmenu_inport', handler: showCmInfoUpload},'-',
		//{text: I18N.CMC.title.downloadTemplate,iconCls:'bmenu_arrDown', handler: downLoadFile},'-',
		buildAliasInput(),
	    {xtype: 'tbspacer', width: 3},
		buildMacInput(),
		{text: I18N.COMMON.query, iconCls: 'bmenu_find', handler: queryClick}
	];
    grid = new Ext.grid.GridPanel({
    	stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
        id: 'cmGrid',
        tbar: toolbar,
        store: store, 
        columns : columns,
        viewConfig: { forceFit: true },
        bbar: buildPagingToolBar()
    });
    var viewPort = new Ext.Viewport({
 	     layout: "border",
 	     items: [grid]
 	});
    queryClick();
});
function buildAliasInput(){
    return '<td width=50px align=center>'+I18N.CMC.title.Alias+':'+'</td>&nbsp;<td><input class="normalInput" type=text style="width: 150px" id="cmAlias";" maxlength="63"></td>';
}
function buildMacInput(){
    return '<td width=40px align=center>MAC:</td>&nbsp;<td><input type=text class="normalInput" style="width: 150px" id="cmMac";"></td>';
}
function queryClick(){
	var cmMac = $("#cmMac").val();
	var cmAlias = $("#cmAlias").val();
	store.load({params: { cmMac: cmMac, cmAlias:cmAlias, start:0, limit:pageSize}});
	Ext.apply(store.baseParams,store.lastOptions.params);
}
function showCmInfoUpload(){
	window.top.createDialog('cmInfoUpload', I18N.CMC.title.cmInfoUpload, 800, 500, 'cm/showCmInfoUpload.tv', null, true, true);
}
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
       displayInfo: true, items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize), '-'
                            ]});
   return pagingToolbar;
}
function onRefreshClick(){
	store.reload();
}
function validateMacAddress(macaddr){
   var reg1 = /^([A-Fa-f0-9]{2,2}\:){0,5}[A-Fa-f0-9]{0,2}$/;
   var reg2 = /^([A-Fa-f0-9]{2,2}\-){0,5}[A-Fa-f0-9]{0,2}$/;
   if (reg1.test(macaddr)) {
      return true;
   } else if (reg2.test(macaddr)) {
      return true;
   } else {
      return false;
   }
}
</script>
</head>
<body class="whiteToBlack" ></body>
</Zeta:HTML>
