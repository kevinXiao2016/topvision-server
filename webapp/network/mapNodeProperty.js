/**
 * Map node property.
 *
 * author:niejun
 */

function setBorderColor() {
	window.top.createDialog('colorDlg', I18N.COMMON.colorPicker, 280, 340,
		'include/colorDlg.jsp', null, true, true, function() {
		if (window.top.ZetaCallback != null && window.top.ZetaCallback.type == 'colorpicker') {
			var color = window.top.ZetaCallback.color;
			var params = 'mapNode.nodeId=' + nodeId + '&mapNode.strokeColor=' + color;
			
			$.ajax({url: '../topology/setMapNodeStrokeColor.tv', type: 'POST', 
				data: params, success: function() {
					Zeta$('borderArea').style.backgroundColor = color;
					var frame = window.top.getFrame('topo' + folderId);
					if (frame) {
						try {
							var el = frame.Zeta$(vmlId); 
							el.strokecolor = color;
							el.setAttribute('oldColor', color);
						} catch(err) {
						}	
					}
				}, error: function() {
					window.top.showErrorDlg();
				}, dataType: 'plain', cache: false});
		}
	});
}

function setFillColor() {
	window.top.createDialog('colorDlg', I18N.COMMON.colorPicker, 280, 340,
		'include/colorDlg.jsp', null, true, true, function() {
		if (window.top.ZetaCallback != null && window.top.ZetaCallback.type == 'colorpicker') {
			var color = window.top.ZetaCallback.color;
			var params = 'mapNode.nodeId=' + nodeId + '&mapNode.fillColor=' + color;
			$.ajax({url: '../topology/setMapNodeFillColor.tv', type: 'POST', 
				data: params, success: function() {
					Zeta$('fillArea').style.backgroundColor = color;
					var frame = window.top.getFrame('topo' + folderId);
					if (frame) {
						try {
							frame.Zeta$(vmlId).fillcolor = color;
						} catch(err) {
						}	
					}
				}, error: function() {
					window.top.showErrorDlg();
				}, dataType: 'plain', cache: false});				
		}
	});
}

function setFontColor() {
	window.top.createDialog('colorDlg', I18N.COMMON.colorPicker, 280, 340,
		'include/colorDlg.jsp', null, true, true, function() {
		if (window.top.ZetaCallback != null && window.top.ZetaCallback.type == 'colorpicker') {
			var color = window.top.ZetaCallback.color;
			var params = 'mapNode.nodeId=' + nodeId + '&mapNode.textColor=' + color;
			$.ajax({url: '../topology/setMapNodeFontColor.tv', type: 'POST', 
				data: params, success: function() {
					Zeta$('colorArea').style.backgroundColor = color;
					var frame = window.top.getFrame('topo' + folderId);
					if (frame) {
						try {
							var el = frame.Zeta$(vmlId);
							if (el.tagName == 'TextBox') {
								el.style.color = color;
							} else {
								el.children[1].style.color = color;
							}
						} catch(err) {
						}	
					}
				}, error: function() {
					window.top.showErrorDlg();
				}, dataType: 'plain', cache: false});				
		}
	});
}

function solidLineStyle() {
	setLineStyle(!Zeta$('solidLine').checked);	
}
function dashedLineStyle() {
	setLineStyle(!Zeta$('solidLine').checked);
}
function setLineStyle(dashed) {
	var params = 'mapNode.nodeId=' + nodeId + '&mapNode.dashed=' + dashed;
	$.ajax({url: '../topology/setMapNodeDashedBorder.tv', type: 'POST', 
		data: params, success: function() {
			var frame = window.top.getFrame('topo' + folderId);
			if (frame) {
				try {
					var el = frame.Zeta$(vmlId);
					if (dashed) {
						var stroke = frame.document.createElement("v:stroke");
						stroke.dashstyle = 'Dash';
						el.appendChild(stroke);
					} else {
						if (el.children.length > 2 && el.children[2].tagName == 'stroke') {
							el.children[2].dashstyle = 'Solid';
						}
					}
				} catch(err) {
				}
			}
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});		
}

function setLinkWidth(w) {
	var params = 'mapNode.nodeId=' + nodeId + '&mapNode.strokeWeight=' + w;
	$.ajax({url: '../topology/setMapNodeStrokeWeight.tv', type: 'POST',
		data: params, success: function() {
			var frame = window.top.getFrame('topo' + folderId);
			if (frame) {
				try {
					var el = frame.Zeta$(vmlId);
					el.strokeweight = parseInt(w);
				} catch(err) {
				}
			}
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});		
}

function onUpdateClick() {
	var url = Zeta$('urls').value.trim();
//	var text = Zeta$('text').value.trim();
//    alert($("#mapNodeForm").serialize());
	$.ajax({url: '../topology/setMapNodeUrl.tv', type: 'POST',
		data: $("#mapNodeForm").serialize(),
		success: function() {
            sysUpdateEntity(nodeId, url);
			var frame = window.top.getFrame('topo' + folderId);
			if (frame) {
				try {
					var el = frame.Zeta$(vmlId);
					el.url = url;
//					if (el.tagName == 'TextBox') {
//						el.innerText = text;
//					} else {
//						el.children[1].innerText = text;
//					}
				} catch(err) {
				}
			}
			//window.top.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.updateSuccessfully);
			top.afterSaveOrDelete({
				title:I18N.COMMON.tip,
				html:'<b class="orangeTxt">'+I18N.COMMON.updateSuccessfully + '</b>',
				okBtn:I18N.COMMON.okBtn
			});
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});
}
Ext.BLANK_IMAGE_URL = '../images/s.gif';
function doOnload() {
	var el = Zeta$('linkWidthBox');
	if (el != null) {
	    for(var i = 0; i < el.options.length; i++) {
	    	if (el.options[i].value == linkWidth) {
	    		el.selectedIndex = i;
	    		break;
	    	}
	    }
	}
    
	el = Zeta$('fontSizeBox');
	if (el != null) {
	    for(var i = 0; i < el.options.length; i++) {
	    	if (el.options[i].value == fontSize) {
	    		el.selectedIndex = i;
	    		break;
	    	}
	    }
    }   
}
function onLinkWidthChanged(obj) {
   setLinkWidth(obj.options[obj.selectedIndex].value);
}

function onFontSizeChanged(obj) {
	var size = obj.options[obj.selectedIndex].value;
	var params = 'mapNode.nodeId=' + nodeId + '&mapNode.fontSize=' + size;
	$.ajax({url: '../topology/setMapNodeFontSize.tv', type: 'POST', 
		data: params, success: function() {
			var frame = window.top.getFrame('topo' + folderId);
			if (frame) {
				try {
					var el = frame.Zeta$(vmlId);
					if (el.tagName == 'TextBox') {
						el.style.fontSize = size + 'pt';       
					} else {
						el.children[1].style.fontSize = size + 'pt';
					}
				} catch(err) {
				}
			}
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});
}

function sysUpdateEntity(id, url) {
	var frame = window.top.getActiveFrame();
	if (frame != null) {
		frame.updateNodeUrl(id, url);
	}
}