<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/cssStyle.inc"%>
<head>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    module  network
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var userName = '<s:property value="userName"/>';
var password = '<s:property value="password"/>';
var enablePassword = '<s:property value="enablePassword"/>';
var isAAA = '<s:property value="isAAA"/>';
var baseGrid;
function setUsername(name){
	userName = name;
}
function onRefreshClick(){
	baseStore.reload({params:{start:0, limit: pageSize}});
}
function onRefreshPage(){
	window.location.reload();
}
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:baseStore,
        displayInfo: true, items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize), '-'
    ]});
    return pagingToolbar;
}
function onAddClick(){
	window.top.createDialog('addEntityPassword', "@resources/telnet.addEntityConfig@", 600, 400, '/entity/telnetLogin/showAddTelnetLogin.tv?', null, true, true);
}

function modifyGlobalPassword(){
	window.top.createDialog('modifyGlobalPassword', "@resources/telnet.modifyDefaultEntityConfig@", 600, 400, '/entity/telnetLogin/showModifyGlobalTelnetLogin.tv', null, true, true);
}

function modifyEntityPassword(){
	var record = baseGrid.getSelectionModel().getSelected();
	if(record.data.ip == '-1'){
		modifyGlobalPassword();
	}else{
		window.top.createDialog('modifyEntityPassword', "@resources/telnet.modifyEntityConfig@", 600, 400, '/entity/telnetLogin/showModifyTelnetLogin.tv?ip=' + record.data.ip, null, true, true);
	}
}

function deleteEntityPassword(){
    var record = baseGrid.getSelectionModel().getSelected();
	window.parent.showConfirmDlg("@COMMON.tip@", "@resources/COMMON.confirmDelete@@COMMON.wenhao@", function(button, text) {
        if (button == "yes") {
            $.ajax({
                url:'/entity/telnetLogin/deleteTelnetLogin.tv?ip=' + record.data.ip,
                type:'POST',
                dateType:'json',
                success:function(response) {
                	onRefreshClick();
                },
                error:function() {
                },
                cache:false
            });
        }
    });
}

function manuRender(value, p, record) {
	if(record.data.ip == '-1'){
		return String.format("<a href='javascript:;' onClick='modifyEntityPassword()'>@COMMON.edit@</a>");
	}else{
		return String.format("<a href='javascript:;' onClick='modifyEntityPassword()'>@COMMON.edit@</a>  / <a href='javascript:;' onClick = 'deleteEntityPassword()'>@resources/COMMON.remove@</a>");
	}
}

function ipRender(value, p, record){
    if (record.data.ip == '-1') {
        return I18N.sendConfig.globalPassword;
    } else {
        return record.data.ipString;
    }
}
function isAAARender(value, p, record){
    if (record.data.isAAA) {
        return '<img id="isAAA" class="clickable switchImg" src="/images/performance/on.png" border=0 align=absmiddle />';
    } else {
        return '<img id="isAAA" class="clickable switchImg" src="/images/performance/off.png" border=0 align=absmiddle />';
    }
}

function query(){
	var ipString = Zeta$('ip').value;
    if (ipString != "" && !Validator.isFuzzyIpAddress(ipString)) {
    	window.top.showMessageDlg(I18N.COMMON.tip, "@sendConfig.ipError@");
    	return;
    }

    baseStore.load({params: {ipString: ipString, start:0, limit:pageSize}});
}

function exportClick() {
	var ipString = Zeta$('ip').value;
	window.location.href="/entity/telnetLogin/exportToExcel.tv?ipString=" + ipString; 
}

function importClick() {
	window.top.createDialog('telnetLoginImport', "@sendConfig.importLoginInfo@", 800, 500,
			'/entity/telnetLogin/showTelnetLoginImport.tv', null, true, true);
}

$(function(){
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var w = $(window).width() - 30;
	var baseColumns = [
	           		//sm,
	           	    {header: "IP",  width: parseInt(w * 2/5), align: 'center', dataIndex: 'ipString', renderer: ipRender},
                    {header: "@resources/SYSTEM.operName@", width: parseInt(w * 2/5), align: 'center', dataIndex: 'userName'},
                    {header: "@resources/SYSTEM.isAAA@", width: parseInt(w * 2/5), align: 'center', dataIndex: 'isAAA', renderer : isAAARender},
	           		/* {header: "@resources/telnet.password@", width: parseInt(w/8), align: 'center', dataIndex: 'password'},
	                {header: "Enable@resources/telnet.password@", width: parseInt(w/8), align: 'center', dataIndex: 'enablePassword'}, */
	           		{header: "@COMMON.opration@", width:w/5, dataIndex: 'ip', fixed:true, renderer : manuRender}
	           	];
	
	
	baseStore = new Ext.data.JsonStore({
	    url: ('/entity/telnetLogin/getTelnetLoginConfig.tv'),
	    root: 'data',
	    totalProperty: 'rowCount',
	    fields: ['ip','ipString', 'userName','password', 'enablePassword','isAAA']
	});
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	var h = $(window).height() - 80;
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		title:"@resources/telnet.entityTelnetConfigList@",
		cls: 'normalTable',
		border: true, 
		height: h,
		store: baseStore, 
		margins: "0px 10px 10px 10px",
		cm: baseCm,
		region:'center',
		bbar: buildPagingToolBar(),
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
	baseStore.load({params:{start:0, limit: pageSize}});
	
	new Ext.Viewport({
	    layout: 'border',
	    items: [baseGrid,
	            {region: 'north',
	    			contentEl:'topPart', 
	    			height:50,
	    			border:false
	    		}]
	});
})

</script>
</head>
<body class="whiteToBlack">
<div id="topPart" class="edge10">
<div class="formtip" id="tips" style="display: none"></div>
	<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable">
			<tr>
				<td class="rightBlueTxt">IP:</td>
				<td width="126">
					<input type="text" id="ip" name="ip" value="" class="normalInput w120"/>
				</td>
				<td class="rightBlueTxt">    
					<a href="javascript:;" class="normalBtn" onclick="query()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>                  
				</td>
				<td width="20"></td>
				<td class="rightBlueTxt">    
					<a href="javascript:;" class="normalBtn" onclick="onAddClick()"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a>                  
				</td>
				<td width="3"></td>
				<td class="rightBlueTxt">    
					<a href="javascript:;" class="normalBtn" onclick="importClick()"><span><i class="miniIcoInport"></i>@NAMEIMPORT.import@</span></a>                  
				</td>
				<td width="3"></td>
				<td class="rightBlueTxt">    
					<a href="javascript:;" class="normalBtn" onclick="exportClick()"><span><i class="miniIcoExport"></i>@NAMEEXPORT.export@</span></a>                  
				</td>
				<td width="3"></td>
				<td class="rightBlueTxt">    
					<a href="javascript:;" class="normalBtn" onclick="modifyGlobalPassword()"><span><i class="miniIcoInfo"></i>@sendConfig.globalPassword@</span></a>                  
				</td>
			</tr>
		</table>	
</div>
</body>
</Zeta:HTML>