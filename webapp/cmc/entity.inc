<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="net.sf.json.JSONObject" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<jsp:useBean id="cmcId" scope="request" class="Long" />
<jsp:useBean id="cmcAttribute" scope="request" class="com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute" />
<%
String moduleStr = request.getParameter("module");
if (moduleStr == null) {
    moduleStr = "1";
}
String productType = request.getParameter("productType");
try{
    String pt = request.getAttribute("productType").toString();
    productType = (productType ==null)?pt:productType;
}catch(Exception e){

}
int module = Integer.parseInt(moduleStr);
%>
<Zeta:ResourceBlock>
<script type="text/javascript">
    var module = <%= module %>;
    $(function (){
        var selectedCls = "selected";
        $("li a[href*='module=" + module + "&']").parent().addClass(selectedCls);
        $("li a[href$='module=" + module + "']").parent().addClass(selectedCls);
        var selectedItem = $("a[href='#']"),
        NAVI_MAP = {
            dhcpRelay :   {text:"DHCP Relay", handler:"/cmc/dhcprelay/showDhcpRelay.tv" },
            loadbalance :   {text:"@cmc/loadbalance.loadbalance@", handler:"/cmc/loadbalance/showLoadbalance.tv" },
            acl :   {text:"ACL", handler:"/cmcacl/viewAllAclList.tv" }
        },
        OPTION_TEMPLATE = '<a href="{0}?module=3&cmcId=${cmcId}&productType=<%= productType%>">{1}</a> | ';
        //selectedItem.parent().addClass("selected");

        /*******************面板图部分下拉菜单***************************/
        $(".ultab").find("a.withSub").mouseenter(function(){
            var leftPos = $(this).offset().left;
            var options = new String();
            if($("#ultabSub").length < 1){
                options += '<div id="ultabSub" class="ultabSub">';
            }
            options += '<div class="ultabSubArr"></div><div class="ultabSubBody"><p>';
            for(var item in NAVI_MAP){
                //动态添加的下拉菜单，需要主动判断是否支持
                var supportInfo = top.VersionControl.supportNode(item, window[vcEntityKey]);
                if(supportInfo.hidden || supportInfo.disabled){
                    continue;
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
        
        // EQAM功能需要判断下面CC是否使用的EQAM版本
		$.ajax({
			url:"/cmc/ipqam/loadEqamSupport.tv",
			dataType:'json',
			data:{
				entityId: ${cmcId}
			},
			success:function(response){
				if(!response.support) {
					$('[vcid="ipqam"]').hide();
				}
			}
		});
    });
    
</script>
<div class=ultab>
    <ul>
        <li vcid="ccmts"><a href="/cmc/showCmcPortal.tv?module=1&cmcId=<%= cmcId %>&productType=<%= productType%>">@network/snapTitle@</a></li>

        <li vcid="upChannel" class="secondTabLine"></li>
        <li vcid="upChannel"><a href="/cmc/channel/showUpChannelList.tv?module=4&cmcId=<%= cmcId %>&productType=<%= productType%>">@cmc/CCMTS.upStream@</a></li>
    
        <li vcid="downChannel" class="secondTabLine"></li>
        <li vcid="downChannel"><a href="/cmc/channel/showDownChannelList.tv?module=5&cmcId=<%= cmcId %>&productType=<%= productType%>">@cmc/CCMTS.downStream@</a></li>
    
	<% if(cmcAttribute.getTopCcmtsSysDorType() != null && !"".equals(cmcAttribute.getTopCcmtsSysDorType())){%> 
    	<li vcid="opticalReceiverRead" class="secondTabLine"></li>
        <li vcid="opticalReceiverRead"><a href="/cmc/optical/showOpticalReciver.tv?module=17&cmcId=<%= cmcId %>&productType=<%= productType%>">@cmc/CMC.optical.opticalReceiver@</a></li>
    <% }%> 

    <% if(cmcAttribute.getTopCcmtsSysDorType() != null && !"".equals(cmcAttribute.getTopCcmtsSysDorType())){%> 
    	<li vcid="opticalReceiverNew" class="secondTabLine"></li>
    	<li vcid="opticalReceiverNew"><a href="/cmc/optReceiver/showOptReciver.tv?module=17&cmcId=<%= cmcId %>&productType=<%= productType%>">@cmc/CMC.optical.opticalReceiver@</a></li>
    <% }%> 

        <li vcid="cm" class="secondTabLine"></li>
        <li vcid="cm"><a href="/cm/showContactedCmList.tv?module=6&cmcId=<%= cmcId %>&productType=<%= productType%>">@cmc/CM.cmList@</a></li>
    
    	<li vcid="ipqam" class="secondTabLine"></li>
    	<li vcid="ipqam"><a href="/cmc/ipqam/showEqamPage.tv?module=14&cmcId=<%= cmcId %>&productType=<%= productType%>">EQAM@COMMON.Channel@</a></li>
    
    	<li vcid="ipqam" class="secondTabLine"></li>
    	<li vcid="ipqam"><a href="/cmc/ipqam/showProgramPage.tv?module=15&cmcId=<%= cmcId %>&productType=<%= productType%>">@cmc/IPQAM.programStream@</a></li>
        
        <li vcid="acl|loadbalance|dhcpRelay" class="secondTabLine"></li>
        <li vcid="acl|loadbalance|dhcpRelay"><a class="withSub" href="#?module=3&">
            <span class="withSub">@CMC/CMC.label.advancedConfig@</span></a>
        </li>
    
        <li vcid="alert" class="secondTabLine"></li>     
        <li vcid="alert"><a href="/cmc/alert/showCmcAlert.tv?module=12&cmcId=<%= cmcId %>&productType=<%= productType%>">@network/NETWORK.entityAlarm@</a></li>
    
        <li vcid="curPerf" class="secondTabLine"></li>
        <li vcid="curPerf"><a href="/cmcPerfGraph/showCmcCurPerf.tv?module=13&cmcId=<%= cmcId %>&timeType=Today&perfType=noise&productType=<%= productType%>">@cmc/CCMTS.perfDisplay@</a></li>
    
    <% if(uc.hasPower("spectrumManagement")){%> 
        <li vcid="spectrum" class="secondTabLine"></li>
        <li vcid="spectrum"><a href="/cmcSpectrum/showCmcCurSpectrum.tv?module=16&cmcId=<%= cmcId %>&productType=<%= productType%>">@cmc/spectrum@</a></li>
        
        <li vcid="spectrumII" class="secondTabLine"></li>
        <li vcid="spectrumII"><a href="/cmcSpectrum/showCmcCurSpectrum.tv?module=16&cmcId=<%= cmcId %>&productType=<%= productType%>">@cmc/spectrum@</a></li>
    <% } %>

</div>
</Zeta:ResourceBlock>

