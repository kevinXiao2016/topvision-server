<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html><head>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="network"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/dataview.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/disabledStyle.css"/>
<style type="text/css">
.directoryIcon {background-image: url(../images/folder.gif) !important;}}
</style>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ux/DataView-more.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var view = null;
var store = null;
var treeRoot = null;
var imgPanel = null;
var treePanel = null;

function fileChanged(box) {
	var v = box.value;
	Zeta$('f_file').value = v;
	var patn = /\.jpg$|\.jpeg$|\.png$|\.gif$|\.zip$/i;
	if(patn.test(v)){
		uploadClick();
	} else if (v != '') {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.imageError,"",function(){
		    $("#t_file_wrapper").html($("#t_file_wrapper").html());
		});
	}
}

function fclick(obj,e){ 
  var el = ZetaUtils.getSrcElement(e);
  obj.style.top = getY(el) + 2;
  obj.style.left = getX(el); 
}

function uploadClick() {
	Zeta$('uploadForm').submit();
}

function okClick() {
	var records = view.getSelectedRecords();
	if (records.length > 0) {
        // path采用绝对路径方式
        var p = records[0].data.url;
        if (p.substring(0, 3) == '../') {
            p = p.substring(2);
        }
		window.top.ZetaCallback = {type: 'image', path: p};
		window.top.closeWindow("imageChooser");	
	}else{
    	
    	top.afterSaveOrDelete({
    		title : I18N.COMMON.tip,
    		html : I18N.PopImageChooserDlg.pleaseSelectIcon + '!'
    	});
    }
}
function cancelClick() {
	window.top.ZetaCallback = null;
	window.top.closeWindow("imageChooser");
}

var treePanelSel = null;
function refreshClick(flag) {
	if (flag) {
	    Zeta$('uploadBt').disabled = true;
        Zeta$('t_file').disabled=true;
        /* if(treePanel != null){
	        treePanelSel = treePanel.getSelectionModel().getSelectedNode();
        } */
		treeRoot.reload();
	} else {
		store.reload();
	}
}
function justImage(obj) {
	if (obj.width > 80) {
		obj.width = 80;
	}
	if (obj.height > 80) {
		obj.height = 80;
	}
}

function doOnresize() {
	var w = $(window).width() - 210;	
	var h = $(window).height() - 80;
	treePanel.setHeight(h);
	imgPanel.setWidth(w);
	imgPanel.setHeight(h);
}
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function() {
	var h = $(window).height() - 80;

	var loader = new Ext.tree.TreeLoader({dataUrl: '../include/loadImageDirectory.tv?module=<s:property value="module"/>'});
	treePanel = new Ext.tree.TreePanel({trackMouseOver: trackMouseOver, useArrows: useArrows, 
		width: 180, height: h, animate: animCollapse, border: true, lines: true, rootVisible: false,
		enableDD: false, autoScroll: true, 
		loader: loader});
	treeRoot = new Ext.tree.AsyncTreeNode({text: "Images Tree", id: "imageSource"});
	treePanel.setRootNode(treeRoot);
	treePanel.render('tree-div');
	treePanel.on('click', function(n) {
		if (n.attributes.module == 'default') {
			Zeta$('uploadBt').disabled = true;
			Zeta$('t_file').disabled=true;
			store.setBaseParam('directoryId', 0);
		} else {
			Zeta$('uploadBt').disabled = false;
			Zeta$('t_file').disabled=false;
			store.setBaseParam('directoryId', n.id);
			Zeta$('module').value = n.attributes.module;
			Zeta$('directory').value = n.attributes.path;
			Zeta$('directoryId').value = n.id;
		}
		store.setBaseParam('module', n.attributes.module);
		store.setBaseParam('directory', n.attributes.path);
		store.reload();
	});
	treeRoot.expand();

    var xd = Ext.data;

    store = new Ext.data.JsonStore({
        url: 'loadImages.tv',
        root: 'images',
        fields: ['name', 'url', {name:'size', type: 'float'} 
       // {name:'lastmod', type:'date', dateFormat:'timestamp'}
       ],
	   listeners: {
			'load': {fn:function(){ view.select(0);}, single: true}
	   }       
    });
    store.load();

    var tpl = new Ext.XTemplate(
		'<tpl for=".">',
            '<div class="thumb-wrap" id="{name}" align=center>',
		    '<div class="thumb"><img src="{url}" title="{name}" onload="justImage(this)"></div>',
		    '<span class="x-editable">{shortName}</span></div>',
        '</tpl>',
        '<div class="x-clear"></div>'
	);

	var formatSize = function(data){
        if(data.size < 1024) {
            return data.size + " bytes";
        } else {
            return (Math.round(((data.size*10) / 1024))/10) + " KB";
        }
    };

	var formatData = function(data){
    	data.shortName = data.name.ellipse(20);
    	data.sizeString = formatSize(data);
    	//data.dateString = new Date(data.lastmod).format("m/d/Y g:i a");
    	//this.lookup[data.name] = data;
    	return data;
    };

    view = new Ext.DataView({
		tpl: tpl,
		singleSelect: true,
		overClass:'x-view-over',
		itemSelector: 'div.thumb-wrap',
		emptyText : '<div style="padding:10px;">' + I18N.NETWORK.noImage + '.</div>',
		store: store,
		listeners: {
			'selectionchange': function() {},
			'dblclick'       : function(view) {
				okClick();
			},
			'loadexception'  : function() {},
			'beforeselect'   : {fn: function(view) {
		        return view.store.getRange().length > 0;
		    }}
		},
		prepareData: formatData
	});	

	var w = $(window).width() - 210;	
    imgPanel = new Ext.Panel({
        id: 'img-chooser-view',
        width: w,
        height: h,
        autoScroll: true,
        bodyStyle: 'overflow-x:hidden',
        layout: 'fit',
        items: view
    });
    imgPanel.render('img-div');
    
    Zeta$('buttonPanel').style.display = '';
});
</script>
</head>
<body class="openWinBody" onselectstart="return false;">
	<div class="edge10">	
		<p class="pB5 orangeTxt"><fmt:message key="topo.updateTip" bundle="${network}"/></p>
		<table cellspacing=0 cellpadding=0 width="100%">
			<tr>
				<td width="180" height=300px><div id="tree-div"  class="clear-x-panel-body threeFeBg"></div></td>
				<td width="5"><div style="width:5px; overflow:hidden;"></div></td>
				<td><div id="img-div" class="clear-x-panel-body threeFeBg"></div></td>
			</tr>
		</table>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel" style="display:none">
			<form id="uploadForm" action="uploadImage.tv" method="POST" enctype="multipart/form-data" target="uploadFrame">
					<input type="hidden" id="module" name="module" value="" />
					<input type="hidden" id="directory" name="directory" value="" />
					<input type="hidden" id="directoryId" name="directoryId" value="0" />
		
			     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
			         <li style="position:relative;">
			         	<input id="uploadBt" disabled="disabled" type="button" value="<fmt:message key="NETWORK.uploadFile" bundle="${network}"/>"
						  class="BUTTON_asLink" onmouseover="this.className='BUTTON_asLink_over'" onmousedown="this.className='BUTTON_asLink'" style="text-indent:14px"
						  onmouseout="this.className='BUTTON_asLink'"  />
						  <span id="t_file_wrapper" style="position:absolute; top:0;left:0;">
							<input id="t_file" type="file" onchange="fileChanged(this)" name="upload" disabled="disabled"
							style="position:absolute;filter:alpha(opacity=0);opacity:0;width:100px" hidefocus />
							<input class=iptxt readonly id="f_file" value="" style="width:0px; overflow:hidden; filter:alpha(opacity=0);opacity:0;" />
					    </span>	
						
			         </li>
			         <li style="padding-left:80px;">
			         	<%-- <a href="javascript:;" class="BUTTON_OVER75" style="width:76px; display:block; text-align:center; line-height:2em; color:#676767;" onclick="refreshClick(false)"><span><fmt:message key="RECYLE.refresh" bundle="${resource}"/></span></a> --%>
			         	<%-- <button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'" onclick="refreshClick(false)" /><fmt:message key="RECYLE.refresh" bundle="${resource}"/></button> --%>
						
						<a href="javascript:;" class="normalBtn" onclick="refreshClick(false)"><span><i class="miniIcoRefresh"></i><fmt:message key="RECYLE.refresh" bundle="${resource}"/></span></a>
						
			         </li>
			         <li>
			         	<%-- <button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'" onclick="okClick()"><fmt:message key="COMMON.ok" bundle="${resource}"/></button> --%>
						<a href="javascript:;" class="normalBtn" onclick="okClick()"><span><i class="miniIcoSaveOK"></i><fmt:message key="COMMON.ok" bundle="${resource}"/></span></a>
			         </li>
			         <li>
			         	<%-- <button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'" onclick="cancelClick()"><fmt:message key="COMMON.cancel" bundle="${resource}"/></button> --%>
					
						<a href="javascript:;" class="normalBtn" onclick="cancelClick()"><span><i class="miniIcoForbid"></i><fmt:message key="COMMON.cancel" bundle="${resource}"/></span></a>
			         </li>
			         <%-- <li><a href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>save</span></a></li> --%>
			     </ol>
			     
		     </form>
		</div>
		
		
		<div style="display:none">
		<iframe name="uploadFrame" width=0 height=0></iframe>
		</div>
</div>
</body></html>