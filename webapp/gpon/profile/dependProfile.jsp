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
    import js/jquery/Nm3kTabBtn
    import js/customColumnModel
    import js/components/segmentButton/SegmentButton
    import gpon/profile/profileCore
    import gpon/profile/module/lineProfileModule
    import gpon/profile/module/serviceProfileModule
    import gpon/profile/module/dbaProfileModule
    import gpon/profile/module/trafficProfileModule
    import gpon/profile/module/tcontProfileModule
    import gpon/profile/module/gemProfileModule
    import gpon/profile/module/gemMapProfileModule
    import gpon/profile/module/ethProfileModule
    import gpon/profile/module/vlanProfileModule
    import gpon/profile/module/vlanTranslateModule
    import gpon/profile/module/vlanAggregationModule
    import gpon/profile/module/vlanTrunkModule
    import gpon/profile/module/potsProfileModule
    import gpon/profile/dependProfile
</Zeta:Loader>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<style type="text/css">
.label-span {
    display: inline-block;
    color: #0267B7;
    text-align: right;
}
.mR20 {margin-right: 20px;}
#grid-container {height: 350px;}
#inner-edit-container{ 
    position:absolute; 
    width:700px;
    top:50px; 
    left:50px; 
    z-index:2; 
    display: none;
}
</style>

<script type="text/javascript">
var entityId = ${entityId};
var moduleType = '${moduleType}';
var parentModuleType = '${parentModuleType}';
var action = '${action}';
var profileId = '${profileId}';
var subProfileId = '${subProfileId}';
var portTypeIndex = '${portTypeIndex}';
var portIndex = '${portIndex}';
var parentEditable = '${parentEditable}';
</script>

</head>
<body class="openWinBody" >
    <div id="description-container" class="edgeTB10LR20"></div>
    
    <div id="grid-container" class="edge10 pT0"></div>
    
    <div class="noWidthCenterOuter clearBoth">
        <ol id="bottom-button-container" class="upChannelListOl pB0 pT20 noWidthCenter">
        </ol>
    </div>
    
    <div id="inner-edit-container">
        <div class="zebraTableCaption">
            <div class="zebraTableCaptionTitle"><span class="blueTxt"><label id="inner-title" class="blueTxt">@COMMON.add@</label></span></div>
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody id="edit-tbody">
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT20 noWidthCenter">
                    <li><a href="javascript:;" class="normalBtnBig" onclick="save()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
            <li><a href="javascript:;" class="normalBtnBig" onclick="hideInner()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
                </ol>
            </div>
        </div>
    </div>
    
    <div id="inner-form-bg-hover" class="displayNone inner-form-bg-hover"></div>
</body>
</Zeta:HTML>