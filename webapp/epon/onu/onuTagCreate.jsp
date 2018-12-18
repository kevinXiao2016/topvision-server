<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
    <head>
        <%@include file="/include/ZetaUtil.inc"%>
        <Zeta:Loader>
            library ext
            library jquery
            library zeta
            module onu
        </Zeta:Loader>
        <script type="text/javascript">
        	var pageId = '${pageId}';
            //保存;
            function saveClick() {
            	tagName = $("#tagName").val();
        		if (!Validator.isAnotherName(tagName)) {
        			$("#tagName").focus();
        			return;
        		}
        		$.ajax({
					url : '/onu/createOnuTag.tv',
					data : {
						'onuTag.tagName' : tagName
					},
					type : 'post',
					dataType : "json",
					success : function(response) {
						top.afterSaveOrDelete({
							title : '@COMMON.tip@',
							html : '<b class="orangeTxt">@COMMON.addOk@</b>'
						});
						window.parent.getFrame(pageId).refresh();
						cancelClick();
					},
					error : function(response) {
						window.parent.showMessageDlg('@COMMON.error@','@COMMON.addEr@');
					},
					cache : false
				});
            }
            //取消;
            function cancelClick() {
                window.parent.closeWindow("tagCreateView");
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@ONU.tagCreate@</p>
            </div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT60">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" width="180">@ONU.tagName@@COMMON.maohao@</td>
                        <td>
                            <input id="tagName" type="text" class="w180 normalInput" toolTip='@COMMON.anotherName@'/>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT60 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="saveClick()">
                            <span><i class="miniIcoData"></i>@COMMON.save@</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
                            <span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
                        </a>
                    </li>
                </ol>
            </div>
        </div>
    </body>
</Zeta:HTML>