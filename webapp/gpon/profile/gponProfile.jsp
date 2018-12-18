<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    css css/white/disabledStyle
    css js/dropdownTree/css/zTreeStyle/zTreeStyle
    css css/gpon/gpon
    module gpon
    import js/customColumnModel
    import js.jquery.Nm3kTabBtn
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
    import gpon/profile/gponProfile
</Zeta:Loader>

<script type="text/javascript">
var entityName = '${entityName}';
var entityId = ${entityId};

var supportSIP = top.VersionControl.support('onuVoip', entityId);
var supportVOIP = supportSIP; 
var supportSIPServiceData = supportSIP;  //业务数据模板权限;
var supportDigitalGraphics = supportSIP;  //数图模板权限;
</script>
</head>

<body class="whiteToBlack">
    <div id="header-container">
        <div id="header">
            <%@ include file="/epon/inc/navigator.inc"%>
        </div>
    
        <div id="tab-container" class="edge10">
        </div>
    </div>
    
    <div id="center-container" style="height:100%;">
    </div>
</body>
</Zeta:HTML>