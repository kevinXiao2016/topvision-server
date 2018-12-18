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
    IMPORT epon/igmp/oltIgmpConfig
</Zeta:Loader>
<style type="text/css">
body,html{ height:100%; width:100%; overflow:hidden;}
.fakeMask{ width:100%; height:80px; overflow:hidden; z-index:99; position:absolute; display:none; top:0; left:0;}
.modeTxt{ position:relative; top:5px;}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var mode = '${igmpMode}'; //这地方从后台读取,proxy(1) router(2) disable(3) snooping(4);
	var list = {
		globalParam : {
			text : '@igmp.protocolCon@',//协议配置;
			src  : '/epon/igmpconfig/showIgmpProtocol.tv?entityId=' + entityId
		},
		cascadePort : {
			text : '@igmp.cascadePort@',//级联端口;
			src  : '/epon/igmpconfig/showIgmpCascadePort.tv'
		},
		igmpVlan : {
			text : '@igmp.vlan@',//组播VLAN;
			src  : '/epon/igmpconfig/showIgmpVlan.tv'
		},
		igmpGroup : {
			text : '@igmp.igmpGroup@',//组播组;
			src  : '/epon/igmpconfig/showIgmpGroup.tv?entityId=' + entityId
		},
		igmpCtc : {
			text : '@igmp.igmpCtcCon@',//可控组播;
			src  : '/epon/igmpconfig/showIgmpCtcConfig.tv?entityId=' + entityId
		},
		ctcProfile : {
			text : '@igmp.igmpCtcProfile@',//可控组播模板;
			src  : '/epon/igmpconfig/showIgmpCtcProfile.tv'
		},
		ctcRecord : {
			text : '@igmp.igmpCtcRecord@',//呼叫记录;
			src  : '/epon/igmpconfig/showIgmpCtcRecord.tv'
		},
		uplinkPort : {
			text : '@igmp.igmpUplinkPort@',//上联端口;
			src  : '/epon/igmpconfig/showIgmpUplinkPort.tv?entityId=' + entityId
		},
		staticJoin : {
			text : '@igmp.staticJoin@',//静态加入;
			src  : '/epon/igmpconfig/showIgmpStaticJoin.tv?entityId=' + entityId
		},
		snoopingGroup : {
			text : '@igmp.igmpGroup@',//组播组;
			src : '/epon/igmpconfig/showSnoopingGroup.tv?entityId=' + entityId
		}
	};
	var IGMP = {
		1 : {
			text : 'proxy',
			tabs : [list.globalParam, list.cascadePort, list.igmpVlan, list.igmpGroup, list.igmpCtc, list.ctcProfile, list.ctcRecord]
		},	
		2 : {
			text : 'router',
			tabs : [list.globalParam, list.cascadePort, list.igmpVlan, list.igmpGroup, list.igmpCtc, list.ctcProfile, list.ctcRecord]
		},
		3 : {
			text : 'disable',
			tabs : [list.globalParam]	
		},
		4 : {
			text : 'snooping',
			tabs : [list.globalParam, list.cascadePort, list.uplinkPort, list.staticJoin, list.snoopingGroup]
		}
	}
</script>
</head>
<body class="whiteToBlack">
	<div class="edge10 pT20">
		<ul class="leftFloatUl">
			<li>
				<div class="corner3 blueBg whiteTxt edge5">@igmp.nowMode@@COMMON.maohao@<span id="putMode"></span></div>
			</li>
			<li class="blueTxt pL20">
				<span class="modeTxt">@igmp.igmpMode@@COMMON.maohao@</span>
			</li>
			<li class="pT2">
				<select id="modeSel" class="normalSel w100" onchange="changeMode()">
					<option value="1">proxy</option>
					<option value="2">router</option>
					<option value="3">disabled</option>
					<option value="4">snooping</option>
				</select>
			</li>
			<li class="pL10">
				<a id="btn1" href="javascript:;" class="normalBtn" onclick="refreshAllIgmpData()"><span><i class="miniIcoEquipment"></i>@igmp.fetch@</span></a>
			</li>
		</ul>
		<div id="putBtnGroup" class="clearBoth pT10 pB5"></div>
	</div>
	<div class="clearBoth">
		<iframe id="pageFrame" name="pageFrame" frameborder="0" width="700" height="400"></iframe>
	</div>
</body>
</Zeta:HTML>