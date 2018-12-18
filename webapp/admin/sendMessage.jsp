<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library Jquery
    library ext
    library zeta
    module CMC
    import js.json2
</Zeta:Loader>

<script type="text/javascript">
$(function() {
	
	
})

function send() {
	var ip = $('#ip').val();
	var typeId = $('#typeId').val();
	var entityId = $('#entityId').val();
    var parentId = $('#parentId').val();
    var slotNo = $('#slotNo').val();
    var portNo = $('#portNo').val();
    var onuNo = $('#onuNo').val();
    var onuPortNo = $('#onuPortNo').val();
    var message = $('#message').val();
    
	$.ajax({
        url: '/system/sendMessage.tv',
        type: 'POST',
        data: {
            ip: ip,
            typeId: typeId,
            entityId: entityId,
            parentId: parentId,
            slotNo: slotNo,
            portNo: portNo,
            onuNo: onuNo,
            onuPortNo: onuPortNo,
            message: message
        },
        dataType:"json",
        success: function(json) {
        }, error: function(json) {
        }, cache: false,
        complete: function (XHR, TS) { XHR = null }
    });
}
</script>
</head>
<body class="bodyGrayBg">
    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
        <tbody>
            <tr>
                <td class="rightBlueTxt" width="150">entity ip</td>
                <td><input type="text" id="ip" class="normalInput w300" value="172.17.2.152" /></td>
            </tr>
            <tr>
                <td class="rightBlueTxt" width="150">entityId</td>
                <td><input type="text" id="entityId" class="normalInput w300" value="30000000050" /></td>
            </tr>
            <tr>
                <td class="rightBlueTxt" width="150">parentId</td>
                <td><input type="text" id="parentId" class="normalInput w300" value="30000000002" /></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">typeId</td>
                <td><input type="text" id="typeId" class="normalInput w300" value="16439" /></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">slotNo</td>
                <td><input type="text" id="slotNo" class="normalInput w300" value="3"/></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">portNo</td>
                <td><input type="text" id="portNo" class="normalInput w300" value="0"/></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">onuNo</td>
                <td><input type="text" id="onuNo" class="normalInput w300" value="0"/></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">onuPortNo</td>
                <td><input type="text" id="onuPortNo" class="normalInput w300" value="0"/></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">message</td>
                <td><input type="text" id="message" class="normalInput w300" value="3/4:1"/></td>
            </tr>
        </tbody>
    </table>
    
    <div class="noWidthCenterOuter clearBoth">
        <ol class="upChannelListOl pB0 pT20 noWidthCenter">
            <li><a href="javascript:;" class="normalBtnBig" onclick="send()"><span><i class="miniIcoForbid"></i>发送</span></a></li>
        </ol>
    </div>
</body>
</Zeta:HTML>