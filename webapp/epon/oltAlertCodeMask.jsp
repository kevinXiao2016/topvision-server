<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module epon
</Zeta:Loader>
<head>
    <script type="text/javascript">
    var entityId = <%=request.getParameter("entityId")%>
    var code = <%=request.getParameter("code")%>
    var codes = []
    var alertTypeGrid
    var alertTypeStore
    Ext.onReady(function() {
    	alertTypeStore = new Ext.data.Store({
    		proxy: new Ext.data.HttpProxy({
	                url: '/epon/alert/loadOltAlertAvailableType.tv?entityId=' + entityId + '&r=' + Math.random()
	            }),
	            reader: new Ext.data.JsonReader({}, Ext.data.Record.create([{name: 'typeId'}, {name: 'displayName'}, {name: 'levelId'}, {name: 'note'}])),
	            sortInfo: {field: 'typeId', direction: 'ASC'}
    	});
    	alertTypeStore.load({
    		callback: function(records, options, success) {
    			$.each(records, function(i, n) {
    				codes[i] = n.id;
    			});
    		}
    	});
    	alertTypeGrid = new Ext.grid.GridPanel({
	            renderTo: 'availableAlertTypeGrid',
	            height: 386,
	            border: true,
	            autoScroll: true,
	            viewConfig:{
					forceFit: true
	            },
	            columns: [{
	                header: I18N.SERVICE.eventId , dataIndex: 'typeId', width: 100, align: 'center',sortable : true
	            }, {
	                header: I18N.SERVICE.eventName , dataIndex: 'displayName', width: 300, align: 'left',sortable : true
	            },  {
	            	header: I18N.COMMON.desc , dataIndex: 'note', width: 200, align: 'center',sortable : true
	            }],
	            sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
	            store: alertTypeStore,
	            listeners: {
	            	'rowclick': {
	            		fn: function(grid, rowIndex) {
	            			var record = grid.getStore().getAt(rowIndex)
	            			$('#alertCode').val(record.data.typeId)
	            			//codeFilter()
	            		}
	            	}
	            }
	        });
        setTimeout(function(){
			$("#alertCode").focus();
        }, 500);
    })
    
    function addCodeMask() {
    	var alertCode = $('#alertCode').val();
    	if(alertCode == null || alertCode == ''){
    		$('#alertCode').focus();
    		return;
    	}
    	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.SERVICE.addingMaskRule, alertCode) , 'ext-mb-waiting')
    	Ext.Ajax.request({
    		url: '/epon/alert/addOltAlertCodeMask.tv?r=' + Math.random(),
    		params: {
    			entityId: entityId,
    			codeMaskIndex: alertCode,
    			codeMaskEnable: 1
    		},
    		success: function(response) {
    			if (response.responseText) {
    				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.addMaskRuleEr , alertCode))
    			} else {
    				 top.closeWaitingDlg();
	   				 top.nm3kRightClickTips({
	   	   				title: I18N.COMMON.tip,
	   	   				html: String.format(I18N.SERVICE.addMaskRuleOk , alertCode)
	   	   			 });
    				//window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.addMaskRuleOk , alertCode))
	    			alertTypeStore.load()
	    			$('#alertCode').val('')
	    			window.parent.getWindow("oltAlertMask").body.dom.firstChild.contentWindow.codeMaskStore.load()
    			}
    		},
    		failure: function() {
    			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SERVICE.addMaskRuleEr , alertCode))
    		}
    	});
    }
    
   	function cancelClick() {
   		window.parent.closeWindow('oltAlertCodeMask')
   	}
   	
   	function codeFilter() {
   		var alertCode = $('#alertCode').val()
   		alertTypeStore.filterBy(function(record) {
   			return (record.get('typeId') + '').indexOf(alertCode) > -1
   		})
   	}
   	
   	function isExist() {
   		var isExist = false
   		var code = $('#alertCode').val()
   		for (var i = 0; i < codes.length; i++) {
   			if ((codes[i] + '').indexOf(code) > -1) {
   				isExist = true
   				break
   			}
   		}
   		
   		if (isExist) {
   			$('#addCodeMask').attr('disabled', false)
   		} else {
   			$('#addCodeMask').attr('disabled', true)
   		}
   	}
    </script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<div id="availableAlertTypeGrid" class="normalTable"></div>
		<ol class="bottomBarOl">
			<li class="">@SERVICE.eventId@:</li>
			<li class=""><input id="alertCode" type="text" class="w120 normalInput" onkeyup="codeFilter();isExist()"/></li>
			<li>
				<a href="javascript:;" class="normalBtn" onclick="addCodeMask()"><span><i class="miniIcoForbid"></i>@SERVICE.maskTheEvent@</span></a>
			</li>
			<li class="floatRight">
				<a href="javascript:;" class="normalBtn" onclick="cancelClick()"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a>
			</li>
		</ol>
	</div>
	
</body>
</Zeta:HTML>