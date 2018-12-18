<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    css css.reset
    library Jquery
    library ext
    library zeta
    module gpon
    css css/white/disabledStyle
    css css/gpon/gpon
    import js/nm3k/Nm3kSwitch
    import js/customColumnModel
    import js/components/segmentButton/SegmentButton
    import gpon/profile/profileCore
    import gpon/profile/module/lineProfileModule
    import gpon/profile/module/serviceProfileModule
    import gpon/profile/module/dbaProfileModule
    import gpon/profile/module/trafficProfileModule
    import gpon/profile/module/sipAgentProfileModule
    import gpon/profile/module/voipMediaProfileModule
    import gpon/profile/module/sipServiceDataProfileModule
    import gpon/profile/module/digitalGraphicsProfileModule
    import gpon/profile/mainProfileTemplate
</Zeta:Loader>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<style type="text/css">
</style>

<script type="text/javascript">
var entityId = ${entityId};
var moduleType = '${moduleType}';
var action = '${action}';
var profileId = '${profileId}';
</script>

</head>
<body class="openWinBody" >
    <div class="openWinHeader">
        <div class="openWinTip"></div>
        <div class="rightCirIco pageCirIco"></div>
    </div>
    
    <div id="item-container" class="edge10">
    </div>
    
    <div class="noWidthCenterOuter clearBoth">
        <ol id="button-container" class="upChannelListOl pB0 pT20 noWidthCenter">
            <li><a href="javascript:;" class="normalBtnBig" onclick="save()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
            <li><a href="javascript:;" class="normalBtnBig" onclick="cancel()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
        </ol>
    </div>
</body>
</Zeta:HTML>