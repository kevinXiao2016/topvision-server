<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library ext
	library zeta
    module report
</Zeta:Loader>
<style>
.reportFolderIcon {background-image: url(../images/report/folder16.gif);}
.reportIcon {background-image: url(../images/report/report16.gif);}
</style>
</head>
<body class="openWinBody">
	<!-- 第一部分，说明文字加图标 -->
	<div class="openWinHeader">
		<div class="openWinTip">@report.selectReportTemplate@</div>
		<div class="rightCirIco linkCirIco"></div>
	</div>
	<!-- 第一部分，说明文字加图标 -->
	
	<!-- 第二部分，核心布局区域 -->
	<div class="content edgeTB10LR20">
		<table class="mCenter tdEdged4">
			<tr>
				<td style="padding: 5px 10px 0 10px">
					<table width=100% height=100% cellspacing=0 cellpadding=0>
						
						<tr>
							<td class=TREE-CONTAINER>
								<div id="reportTypeTree" style="width: 464px; height: 314px;"></div></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<!-- 第二部分，核心布局区域 -->
	
	<!-- 第三部分，按钮组合 -->
	<div class="noWidthCenterOuter clearBoth">
		<ol class="upChannelListOl pB0 pT10 noWidthCenter">
			<li><a href="javascript:;" class="normalBtnBig" id="next" onclick="nextClick();"><span><i class="miniIcoSaveOK"></i>@report.nextStep@</span></a></li>
			<li><a href="javascript:;" class="normalBtnBig" ><span>@report.cancle@</span></a></li> 
		</ol>
	</div>
	<!-- 第三部分，按钮组合 -->
<div class=formtip id=tips style="display: none"></div>
<script type="text/javascript">
	var tree = null;
	function okClick() {
	}
	function showHelp() {
		window.open('../help/index.jsp?module=newReportTask', 'help');
	}
	function nextClick() {
		var node = tree.getSelectionModel().getSelectedNode();
		if(node == null){
			return window.parent.showMessageDlg("@COMMON.tip@","@demo.selIndex@");
		}
		window.top.ZetaCallback = {};
		window.top.ZetaCallback.templateId = node.attributes.id;
		window.top.ZetaCallback.templateName = node.attributes.name;
		window.top.ZetaCallback.templateText = node.text;
		//报表条件配置页面
		location.href = 'scheduleReportTask.jsp';
	}
	function cancelClick() {
		window.top.ZetaCallback = null;
		window.top.closeWindow("modalDlg");
	}
	Ext.onReady(function() {
		var treeLoader = new Ext.tree.TreeLoader({
			dataUrl : '/report/loadReportTaskTemplate.tv'
		});
		tree = new Ext.tree.TreePanel({
			singleExpand: true,
			autoScroll : true,
			border : false,
			lines : true,
			rootVisible : false,
			height : 314,
			width : 464,
			enableDD : false,
			loader : treeLoader
		});
		var root = new Ext.tree.AsyncTreeNode({
			text : 'Report Category Tree',
			draggable : false,
			id : 'source'
		});
		
		tree.setRootNode(root);
		tree.render('reportTypeTree');
		root.expand();

		tree.on('click', function(n) {
			if(n.isLeaf()){
				$('#next').attr("disabled", false);
			}else{
				$('#next').attr("disabled", true);
			} 
		});
	});
</script>
</body>
</Zeta:HTML>