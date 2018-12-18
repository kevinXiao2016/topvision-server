<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.topvision.ems.facade.domain.EntityType" %>
<jsp:useBean id="entity" scope="request" class="com.topvision.ems.facade.domain.Entity" />
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:ResourceBlock>
<div class=ultab>
<%
	Object module = request.getParameter("module");
%>
<ul>
	<li><a href="/portal/showEntitySnapJsp.tv?module=1&entityId=${entity.entityId}">@EPON/SUPPLY.entitySnap@</a></li>
	<li class="secondTabLine"></li>
	<li><a href="/config/showOltConfigJsp.tv?module=2&entityId=${entity.entityId}">@EPON/SUPPLY.configInfo@</a></li>
	<li vcid="oltVlan" class="secondTabLine"></li>
    <li vcid="oltVlan"><a href="/epon/showOltVlanList.tv?module=10&entityId=${entity.entityId}">VLAN</a></li>
    <li vcid="oltAdvancedConfig" class="secondTabLine"></li>	
    <li vcid="oltAdvancedConfig"><a class="withSub" id="config">
    	<span class="withSub">@OLTDHCP/oltdhcp.configManager@</span></a>
    </li>
    <li vcid="gpon" class="secondTabLine"></li>
    <li vcid="gpon"><a href="/gpon/profile/showGponProfile.tv?module=gponProfile&entityId=${entity.entityId}">@GPON/GPON.profile@</a></li>
    <li class="secondTabLine"></li>	
    <li><a class="withSub" id="panel" href="${entity.modulePath}/showOltFaceplate.tv?module=3&entityId=${entity.entityId}">
    	<span class="withSub">@EPON/SUPPLY.panel@</span></a>
    </li>
    <li class="secondTabLine"></li>
	<li><a href="/epon/portinfo/showOltPortInfo.tv?module=6&entityId=${entity.entityId}">@EPON/olt.portInfo@</a></li>
    <li vcid="ofa" class="secondTabLine"></li>
    <li vcid="ofa"><a href="/epon/ofa/showOfaInfo.tv?module=11&entityId=${entity.entityId}">@EPON/olt.ofaInfo@</a></li>
    <li class="secondTabLine"></li>
	<li><a href="/onu/showOnuViewJsp.tv?module=4&entityId=${entity.entityId}">@EPON/SUPPLY.downDevice@</a></li>
	<li class="secondTabLine"></li>
	<li><a href="/epon/alert/showOltAlert.tv?module=5&entityId=${entity.entityId}">@EPON/SUPPLY.deviceAlert@</a></li>
	<li class="secondTabLine"></li>
	<li><a href="/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=7&entityId=${entity.entityId}">@EPON/SUPPLY.devicePerf@</a></li>
	<li vcid="oltDhcpRelay" class="secondTabLine"></li>
    <li vcid="oltDhcpRelay"><a href="/epon/dhcprelay/showDhcpRelayConfigList.tv?module=8&entityId=${entity.entityId}">DHCP Relay</a></li>
    <li id="cameraL" class="secondTabLine"></li>
    <li id="cameraT"><a href="/camera/showOltCamereList.tv?module=9&entityId=${entityId}">@CAMERA/CAMERA.cameraList@</a></li>
    <li dynamic-vcid="ipqam" class="secondTabLine" style="display:none;"></li>
    <li dynamic-vcid="ipqam" style="display:none;"><a href="/cmc/ipqam/showOltProgramPage.tv?module=oltEqam&entityId=${entityId}">@EPON/SUPPLY.eqamProgramInfo@</a></li>
</ul>
<script type="text/javascript">
	//变量定义
	var cameraSwitch = '${cameraSwitch}';
	if(cameraSwitch != 'on'){
	   	$('#cameraL').hide();
    	$('#cameraT').hide();
    }
	(function(){
		var pageModule = '<%= module %>' || 1,
		selectedItem = $(String.format("A[href*='module={0}&']",pageModule)),
		NAVI_MAP = {
			BACSIC_VIEW : 	{text:"@EPON/EPON.entityView@", handler:"showOltFaceplate.tv" },
			VLAN_VIEW 	: 	{text:"@EPON/EPON.vlanView@", handler:"showOltVlanView.tv" },
			MIRROR_VIEW : 	{text:"@EPON/EPON.mirrorView@", handler:"showOltMirrorView.tv" },
			TRUNK_VIEW 	: 	{text:"@EPON/EPON.trunkView@", handler:"showOltTrunkView.tv" },
			PONPROTECT_VIEW : {text:"@EPON/ponprotect.ponprotectView@", handler:"showOltPonProtectView.tv" },
			ISOLATIONGROUP_VIEW : {text:"@EPON/ISOGROUP.isolationView@", handler:"showIsolataionGroupView.tv" }
		},
		CONFIG_MAP = {
			IGMP_VIEW : {text:"@OLTDHCP/oltdhcp.igmpManager@", handler: "/epon/igmpconfig/showOltIgmpConfig.tv"},
			DHCP_VIEW : {text:"@OLTDHCP/oltdhcp.dhcpManager@", handler: "/epon/oltdhcp/showOltDhcp.tv"},
			PPPOE_VIEW : {text:"@OLTDHCP/oltdhcp.pppoeManager@", handler: "/epon/oltdhcp/showOltPppoe.tv"}
		},
		OPTION_TEMPLATE = '<a href="${entity.modulePath}/{0}?module=3&entityId=${entity.entityId}">{1}</a> | ';
		OPTION_CONF_TEMPLATE = '<a href="javascript:;" class="forwardView" data-preId={2} data-entityId="${entityId}" data-src="{0}?entityId=${entityId}">{1}</a> | ';
		selectedItem.parent().addClass("selected");

		/*******************面板图部分下拉菜单***************************/
		$(".ultab").find("a.withSub#panel").mouseenter(function(){
			var leftPos = $(this).offset().left;
			var options = new String();
			if($("#ultabSub").length < 1){
				options += '<div id="ultabSub" class="ultabSub">';
			}
			options += '<div class="ultabSubArr"></div><div class="ultabSubBody"><p>';
			for(var item in NAVI_MAP){
				if(item == 'PONPROTECT_VIEW'){
					var $support = VersionControl.support("ponProtected");
					if( !$support ){
						continue;
					}
				}else if(item == 'ISOLATIONGROUP_VIEW'){
					var $support = VersionControl.support("isolationgroup_view");
					if( !$support ){
						continue;
					}
				}
				var option = NAVI_MAP[item];
				options += String.format(OPTION_TEMPLATE, option.handler,option.text);
			}
			options = options.substring(0, options.length-2);
			options += '</p></div>';
			if($("#ultabSub").length < 1){//不存在下拉菜单;		
				options += '</div>';
				$("body").append(options);
				$("#ultabSub").css("left",leftPos);
			}else{
				$("#ultabSub").css("display","block").html(options);
				$("#ultabSub").stop().animate({opacity:1}).css("left",leftPos);
			}
			//计算应该是左侧还是右侧;
			var w = $(window).width();
			var subW = $("#ultabSub").outerWidth();
			var overElementW = $(this).outerWidth();
			var overElementL = $(this).offset().left;
			if((w - leftPos) < subW && leftPos > subW){
				var l = overElementW + overElementL - subW;
				$("#ultabSub").css("left",l);
				var arrL = subW - overElementW/2;
				$("#ultabSub .ultabSubArr").css("left",arrL);
				return;
			}
			var overElementW = $(this).outerWidth() / 4;//悬浮的元素宽度;
			$("#ultabSub .ultabSubArr").css("left",overElementW);
		}).mouseleave(function(){
			$("#ultabSub").stop().animate({opacity:0},function(){
				$(this).css("display","none");
			});
		});
		
		$(".ultab").find("a.withSub#config").mouseenter(function(){
			var leftPos = $(this).offset().left;
			var options = new String();
			if($("#ultabSub").length < 1){
				options += '<div id="ultabSub" class="ultabSub">';
			}
			options += '<div class="ultabSubArr"></div><div class="ultabSubBody"><p>';
			for(var item in CONFIG_MAP){
				var option = CONFIG_MAP[item];
				if(item == 'IGMP_VIEW'){
					var $support = VersionControl.support("oltIgmpConfig");
					if( !$support ){
						continue;
					}
					options += String.format(OPTION_CONF_TEMPLATE, option.handler,option.text,"IGMP");
				}else if(item == 'DHCP_VIEW'){
					var $support = VersionControl.support("oltDhcpConfig");
					if( !$support ){
						continue;
					}
					options += String.format(OPTION_CONF_TEMPLATE, option.handler,option.text,"DHCP");
				} else if(item == 'PPPOE_VIEW'){
					var $support = VersionControl.support("oltPppoeConfig");
					if( !$support ){
						continue;
					}
					options += String.format(OPTION_CONF_TEMPLATE, option.handler,option.text,"PPPoE");
				}
			}
			options = options.substring(0, options.length-2);
			options += '</p></div>';
			if($("#ultabSub").length < 1){//不存在下拉菜单;		
				options += '</div>';
				$("body").append(options);
				$("#ultabSub").css("left",leftPos);
			}else{
				$("#ultabSub").css("display","block").html(options);
				$("#ultabSub").stop().animate({opacity:1}).css("left",leftPos);
			}
			//计算应该是左侧还是右侧;
			var w = $(window).width();
			var subW = $("#ultabSub").outerWidth();
			var overElementW = $(this).outerWidth();
			var overElementL = $(this).offset().left;
			if((w - leftPos) < subW && leftPos > subW){
				var l = overElementW + overElementL - subW;
				$("#ultabSub").css("left",l);
				var arrL = subW - overElementW/2;
				$("#ultabSub .ultabSubArr").css("left",arrL);
				return;
			}
			var overElementW = $(this).outerWidth() / 4;//悬浮的元素宽度;
			$("#ultabSub .ultabSubArr").css("left",overElementW);
		}).mouseleave(function(){
			$("#ultabSub").stop().animate({opacity:0},function(){
				$(this).css("display","none");
			});
		});
		
		$("#ultabSub").live("mouseenter",function(){
			$("#ultabSub").css("display","block");
			$("#ultabSub").stop().animate({opacity:1},function(){
				$(this).css("display","block");
			});
		}).live("mouseleave",function(){
			$("#ultabSub").stop().animate({opacity:0},function(){
				$(this).css("display","none");
			});
		});
		$("body").delegate("a.forwardView", 'click', function() {
			var $me = $(this),
			    src = $me.attr('data-src'),
			    preId = $me.attr('data-preId'),
			    entityId = $me.attr('data-entityId');
			top.addView('OLT'+preId + '-' + entityId, 'OLT '+preId+'[${entity.name}]', "icoH1", src);
		})

		
		// EQAM功能需要判断下面CC是否使用的EQAM版本
		$.ajax({
			url:"/cmc/ipqam/loadEqamSupportUnderOlt.tv",
			dataType:'json',
			data:{
				entityId: ${entity.entityId}
			},
			success:function(response){
				if(!response.support) {
					$('[dynamic-vcid="ipqam"]').hide();
				}
			}
		});
	})()
</script>
</div>
</Zeta:ResourceBlock>