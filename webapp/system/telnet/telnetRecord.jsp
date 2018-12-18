<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<head>
 <Zeta:Loader>
    library ext
    library jquery
    library zeta
    module network
    plugin DateTimeField
    import js/customColumnModel
    import system/telnet/telnetClientRecord
</Zeta:Loader>
<style type="text/css">
#query-container {
    margin: 10px;
}
</style>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
</script>
</head>
<body class="openWinBody">
    <div id="query-container">
        <table>
            <tbody>
                <tr>
                    <td class="rightBlueTxt">IP@COMMON.maohao@</td>
                    <td><input id="ip" class="normalInput"></td>
                    <td class="rightBlueTxt w100">@TelnetClient.command@@COMMON.maohao@</td>
                    <td colspan="3"><input id="command" class="normalInput w500"></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="rightBlueTxt">@TelnetClient.username@@COMMON.maohao@</td>
                    <td><input id="userName" class="normalInput"></td>
                    <td class="rightBlueTxt w100">@TelnetClient.createTime@@COMMON.maohao@</td>
                    <td><div id="startTime"></div></td>
                    <td>-</td>
                    <td><div id="endTime"></div></td>
                    <td><a id="simple-query" href="javascript:query();" class="normalBtn" onclick=""><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
                </tr>
            </tbody>
        </table>
    </div>
    
    <div id="grid-container">
    </div>
</body>
</Zeta:HTML>