<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module igmpconfig
    IMPORT epon/igmp/igmpprofilegroup
</Zeta:Loader>
<style type="text/css">
	.frameBody .x-panel-body{ background:transparent;}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var profileId = '${profileId}';
	var cm,store,grid, tb;
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
			<div class="openWinTip">@igmp.bindGroup@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
	<div id="topPart" class="pT10 pL10 ">
		<ul class="leftFloatUl pB10">
			<li class="blueTxt pT5">@igmp.profileID@@COMMON.maohao@</li>
			<li class="pT5 pR10" id="putProfileId"></li>
			<li class="blueTxt pT5 mL5">@igmp.igmpGroup@@COMMON.maohao@</li>
			<li class="pT2" id="putGroupSel">
				<input id="groupFilter" class="normalInput w200" onkeyup="execGroupFilter();" tooltip="@IGMP.filterTooltip@" />
			</li>
			<li class="mL5">
				<Zeta:Button id="batchBindBt" disabled="true" icon="miniIcoAdd" clazz="normalBtn" onclick="batchBindProfileGroup()">@IGMP.batchBind@</Zeta:Button>
			</li>
		</ul>
	</div>
	<div id="putGrid" class="clearBoth pL10 pR10"></div>
	<div class="noWidthCenterOuter clearBoth">
	   <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	       <li><a onclick="refreshProfileGroup()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
	       <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	   </ol>
	</div>
	
</body>
</Zeta:HTML>