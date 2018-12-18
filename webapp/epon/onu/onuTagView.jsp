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
        	var onuId = '${onuId}';
        	var pageId = '${pageId}';
        	var tagId = '${tagId}';
        	$(function(){
        		//标签选择
    			window.tagStore = new Ext.data.JsonStore({
    			    url: '/onu/loadOnuTags.tv',
    			    root : 'data',
    			    fields: ['id','tagName'],
    			    autoLoad: true,
    			    listeners : {
    					load : function(dataStore, records, options){
    							var record = {id: -1, tagName: '@COMMON.select@'};
    							var $record = new dataStore.recordType(record,"-1");
    							dataStore.insert (0,[ $record ]);
    							
    							window.tagCombo = new Ext.form.ComboBox({
    			    			    id: 'tagCombo',
    			    			    mode:'local',
    			    			    applyTo : "onuTag",
    			    			    width : 150,
    			    				triggerAction : 'all',
    			    				editable : false,
    			    				lazyInit : false,
    			    				emptyText : "@COMMON.select@",
    			    				valueNotFoundText: "@COMMON.select@",
    			    				valueField: 'id',
    			    				value: tagId,
    			    			    displayField: 'tagName',
    			    				store : tagStore
    			    		    });
    					}
    				}
    			}); 
        	});
        	
            //保存;
            function saveClick() {
            	var tagId = tagCombo.getValue();
            	window.parent.showWaitingDlg('@COMMON.wait@', '@COMMON.save@...' , 'ext-mb-waiting');
            	$.ajax({
            		url: '/onulist/saveOnuTagRelation.tv', 
            		type: 'POST',
            		data: {
            			onuId: onuId,
            			tagId: tagId
            		},
            		success: function(text) {
            			top.nm3kRightClickTips({
                			title: '@COMMON.tip@',
                			html: '@ONU.tagSuccess@'
                		});
            			window.top.getFrame(pageId).refresh();
            			cancelClick();
            		}, error: function() {
            			top.nm3kRightClickTips({
            				title: '@COMMON.tip@',
            				html: '@ONU.tagFailure@'
            			});
            		}, cache: false
            	});//提交ONURSTP桥模式的修改
            }
            //取消;
            function cancelClick() {
                top.closeWindow("tagOnu");
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@ONU.tagViewTip@</p>
            </div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT60">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" width="180">@ONU.tag@</td>
                        <td>
                            <input id="onuTag" class="w180 normalSel" />
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