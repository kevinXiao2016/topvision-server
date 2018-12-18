<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" href="/performance/css/smoothness/jquery-ui-1.10.3.custom.min.css" />
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library ext
    library zeta
    module network
    CSS js/lib/jQuery/jquery.terminal/jquery.terminal.min
    IMPORT js/lib/jQuery/jquery.terminal/jquery.terminal.min
    IMPORT system/telnet/telnetClient
</Zeta:Loader>

<style type="text/css">
html, body {
    height: 100%;
}
.terminal span,
.terminal span,
.terminal pre {
    font-size: 14px;
    font-family: 'Lucida Console';
}
#toolbar-container {
    height: 40px;
}
</style>

<script type="text/javascript">
var ip = '${entityIp}';
var loginInfo = null;
var interfaceStr = '${interfaceStr}';
</script>
</head>
<body>
    <div id="toolbar-container">
        <div style="width:99%;height: 25px;display: inline-block;padding-top: 7px;padding-left: 5px;">
            <a id="enable" class="normalBtn hide" href="javascript:;"  onclick="enterEnable()">
                <span><i class="miniIcoCmd"></i>@TelnetClient.enterEnable@</span>
            </a>
            <a id="config" class="normalBtn mL10 hide" href="javascript:;"  onclick="enterConfig()">
                <span><i class="miniIcoCmd"></i>@TelnetClient.enterConfig@</span>
            </a>
            <a id="clear" class="normalBtn mL10 hide" href="javascript:;"  onclick="clearTerm()">
                <span><i class="miniIcoClose"></i>@TelnetClient.clearScreen@</span>
            </a>
            <a id="disconnect" class="normalBtn mL10 hide" href="javascript:;"  onclick="closeConnect()">
                <span><i class="miniIcoForbid"></i>@TelnetClient.close@</span>
            </a>
        </div>
    </div>
    
    <div id="cmd-container"></div>
</body>
</Zeta:HTML>