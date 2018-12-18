<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library jquery
    library zeta
    module spectrum
</Zeta:Loader>
<script type="text/javascript">
var videoName = '${videoName}';
var videoId = '${videoId}';

function cancelClick() {
	window.top.closeWindow('renameVideo');
}

function validateAlias(str){
    var reg = /^[\w\d\u4e00-\u9fa5\[\]\(\)-\/]*$/;
    return reg.test(str);
}

function rename() {
    name = Zeta$('videoName').value;
	if(!Validator.isAnotherName(name)){
		Zeta$('videoName').focus();
		return;
	}
    $.ajax({
        url: '/cmcSpectrum/renameVideo.tv',
        data:{
        	videoId:videoId,
        	videoName : name.trim()
        },
        type: 'post',dataType : "text",
        success: function(text) {
            if (text == "success") {
                if(window.parent.getFrame("showSpectrumVideoMgmt")){
	                window.parent.getFrame("showSpectrumVideoMgmt").refresh();
                }
                cancelClick();
            } else {
        	    top.afterSaveOrDelete({
        			title: '@COMMON.tip@',
        				html: '<b class="orangeTxt">@spectrum.renameVideoFail@</b>'
           		});
            }            
        },
        error: function(response) {
    	    top.afterSaveOrDelete({
    			title: '@COMMON.tip@',
    				html: '<b class="orangeTxt">@spectrum.renameVideoFail@</b>'
       		});
        },
        cache: false
    });
}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">@spectrum.renameVideo@</div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT30">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt withoutBorderBottom w160">@spectrum.videoName@@COMMON.maohao@</td>
	                <td class="withoutBorderBottom">
	                    <input class="normalInput modifiedFlag w200" id="videoName" maxlength=63 value="${videoName}"
			             toolTip='@COMMON.anotherName@' />
	                </td>
	            </tr>	            
	        </tbody>
	    </table>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="rename()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick();" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>