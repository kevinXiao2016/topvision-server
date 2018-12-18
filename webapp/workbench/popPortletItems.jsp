<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library jquery
    module workbench
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="../js/dhtmlx/tree/dhtmlxtree.css" />
<script type="text/javascript" src="../js/dhtmlx/dhtmlxcommon.js"></script>
<script type="text/javascript" src="../js/dhtmlx/tree/dhtmlxtree.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var tree = null;
/* var columns = ${column}; */
function doOnload(){
	//$("#columnDiv").val(columns);
	tree = new dhtmlXTreeObject("portletTree", "100%", "100%", 0);
	tree.setImagePath("../js/dhtmlx/tree/imgs/dhxtree_skyblue/");
	tree.enableCheckBoxes(1);
	tree.enableThreeStateCheckboxes(true);
	tree.loadXML("loadPortletItemsByUserId.tv");
}

/* 
function columnChanged(obj) {
	columns = obj.options[obj.selectedIndex].value;
}
*/
function okClick() {
	//var columnValue = $("#columnDiv").val();
	var arr = tree.getAllChecked();
	var params = null;	
	if (arr == '') {
		/* params = {column : 2};
		window.parent.showMessageDlg(I18N.COMMON.tip,I18N.WorkBench.pleaseDesignMyDesk);
		return ; */
		params = {'column': 1, 'itemIds': []};
	} else {
		var ids = arr.split(',');
		params = {'column': 2, 'itemIds': ids};
	}
	$.ajax({
		url: 'modifyMyDesktop.tv', 
		type: 'POST',
		data: params, 
		success: function() {
			window.parent.addView("mydesktop", I18N.COMMON.myDesktop, "mydesktopIcon", "portal/showMydesktop.tv");
			var frame = window.parent.getFrame('mydesktop');
			console.log(frame);
	   		if (frame) {
	   			var src = frame.location;
	   			frame.location.href = src;
	   			//frame.location.reload();
	   		}
	   		window.parent.closeWindow('modalDlg');
		}, 
		error: function() {
			window.parent.showErrorDlg();
		}, 
		cache: false
	});
}
function cancelClick() {
	window.parent.closeWindow('modalDlg');
}


</script>  
<style type="text/css">
.standartTreeRow{ width:600px !important;}
/*解决ie6下树形有横向滚动条的bug*/
</style>
</head>
<body class="openWinBody"  onload="doOnload()">
	<div class="edgeTB10LR20 ">
		<!-- <ul class="leftFloatUl"  >
			<li class="rightBlueTxt pT4">@td.columnNum@</li>
			<li>
				<select id="columnDiv" style="width:100px">
					<option value="2">2</option>
				</select>
			</li>			
		</ul> -->
		<div class="clearBoth pT10 pB5"  >@td.selectVisiblePart@</div>
		<div id="portletTree" style="height:270px; border:1px solid #D0D0D0; padding:10px;" class="threeFeBg"></div>
			<div class="noWidthCenterOuter clearBoth">
			    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
			        <li>
			            <a href="javascript:;" class="normalBtnBig" onclick="okClick()" id=okBt>
			                <span>
			                    <i class="miniIcoData"></i>@button.save@
			                </span>
			            </a>
			        </li>
			        <li>
			            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
			                <span><i class="miniIcoForbid"></i>@button.cancel@</span>
			            </a>
			        </li>
			    </ol>
		</div>
	</div>
</body>
</Zeta:HTML>
