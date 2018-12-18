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
    module fault
</Zeta:Loader>

</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt">@ALERT.action@</b></p>
	    	<p><span id="newMsg">@ALERT.addNewAction@</span></p>
	    </div>
	    <div class="rightCirIco alarmCirIco"></div>
	</div>
	<div class="edge10">
		<p class="pB10 blueTxt">@ALERT.chooseActionType@:</p>
		<div id="actionCategoryTree" class="threeFeBg"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a id="nextBt"  onclick="nextClick(this)" href="javascript:;" class="normalBtnBig disabledAlink"><span><i class="miniIcoArrRight"></i>@ALERT.nextStep@</span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>	

<script>
var tree = null;
function showHelp() {
    window.open('../help/index.jsp?module=newAction', 'help');
}
function lastClick() {
    location.href = '../include/new.jsp';
}
function nextClick(obj) {
	if($(obj).hasClass("disabledAlink")){ return;}
    var node = tree.getSelectionModel().getSelectedNode();
    location.href = '/fault/new' + node.attributes.name + 'Action.tv';
}
function cancelClick() {
    window.top.closeWindow("modalDlg");
}

Ext.onReady(function(){
    Ext.QuickTips.init();
    var treeLoader = new Ext.tree.TreeLoader({dataUrl:'../fault/loadActionType.tv'});
	treeLoader.on('load', function() {
		tree.getNodeById(1).select();
		 //Zeta$('nextBt').disabled = false;
		 $("#nextBt").removeClass("disabledAlink");
	});
    tree = new Ext.tree.TreePanel({
        el:'actionCategoryTree', useArrows:useArrows, autoScroll:true, 
        animate:true, border: true, height:240, cls: "clear-x-panel-body",
        trackMouseOver:false, padding:10,
        lines: true, rootVisible: false, containerScroll: true, enableDD: false,
        loader: treeLoader
    });
    var root = new Ext.tree.AsyncTreeNode({text: 'Action Category Tree', 
    draggable:false, id:'source'});
    tree.setRootNode(root);
    tree.render();
    root.expand();

    tree.on('click', function(n){
    	$("#nextBt").removeClass("disabledAlink");
        //Zeta$('nextBt').disabled = n.getDepth() == 0;
        
    });
});
</script>
</body>
</Zeta:HTML>
