<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<style type="text/css">
.favouriteFolderIcon {background-image: url(../images/workbench/folder.gif);}
.favouriteLinkIcon {background-image: url(../images/workbench/link.gif);}
</style>
<Zeta:Loader>
	library jquery
	module demo
</Zeta:Loader>
<script type="text/javascript">

	/*********** 程序入口    *************/
	$( DOC ).ready( initailize );	
	function initailize() {
		// 默认将第一个输入框聚焦
		$('#name').focus();
	}

	function okClick() {}
	function cancelClick() {}
</script>
</head>

<!-- 加入body样式 openWinBody , 注意不要在body标签上写任何其他style -->
<body class="openWinBody">
<!-- 加入body样式 openWinBody-->

	<!-- 第一部分，说明文字加图标 -->
	<div class="openWinHeader">
		<div class="openWinTip">@demo.addLink@</div>
		<div class="rightCirIco linkCirIco"></div>
	</div>
	<!-- 第一部分，说明文字加图标 -->
	
	<!-- 第二部分，背景间隔色表格 -->
	<div class="edgeTB10LR20">
		<form id="folderForm" onsubmit="return false;">
			<table cellpadding="0" cellspacing="0" rules="none" border="0" width="512" class="mCenter tdEdged4">
				<tbody>
					<tr>
						<td width="68"><label for="name">@COMMON.name@<span class="redTxt">*</span></label></td>
						<td><input id=name name="favouriteFolder.name" value=""	class="normalInput w440" type=text maxlength=32
							tooltip="@demo.linkTip@" />
						</td>
					</tr>
					<tr>
						<td>URL<label for="url"><span class="redTxt">*</span></label></td>
						<td><input id="url" name="favouriteFolder.url" value = "" class="normalInput w440" type="text"
							tooltip="@demo.linkTip@"/></td>
					</tr>
					<tr>
						<td colspan="2" class="pT10">@demo.selectPosition@</td>
					</tr>
					<tr>
						<td colspan="2">
							<div id="folderTree" class="folderTreeContainer clear-x-panel-body"></div>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	<!-- 第二部分，背景间隔色表格 -->
	
	<!-- 第三部分，按钮组合 -->
	<div class="noWidthCenterOuter clearBoth">
		<ol class="upChannelListOl pB0 pT10 noWidthCenter">
			<li><a href="javascript:;" class="normalBtnBig"
				onclick="okClick()"><span><i class="miniIcoSaveOK"></i>@COMMON.finish@</span></a></li>
			<li><a href="javascript:;" class="normalBtnBig"
				onclick="cancelClick()"><span>@COMMON.cancel@</span></a></li>
		</ol>
	</div>
	<!-- 第三部分，按钮组合 -->
	
	<div class=formtip id=tips style="display: none"></div>
</body>
</Zeta:HTML>