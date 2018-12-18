/**
 * author:niejun
 * 
 * Topofolder property.
 */
function fixNodeLocation(obj) {
	var params = 'mapNode.nodeId=' + nodeId + '&mapNode.fixed=' + obj.checked;
	$.ajax({url: '../topology/setMapNodeFixed.tv', type: 'POST', data: params,
	success: function() {
		var frame = window.top.getFrame('topo' + superiorId);
		if (frame != null) {
			try {
				frame.fixNodeLocation(nodeId, obj.checked);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});
}

function popIconFile() {
	window.top.createWindow('imageChooser', I18N.COMMON.selectIcon, 600, 400,
		'include/showImageChooser.tv?module=network', null,
		true, true, setFolderIcon);
}

function setFolderIcon() {
	var callback = window.top.getZetaCallback();
	if (callback != null && callback.type == 'image') {
		if (callback.path != null) {
			var params = 'mapNode.nodeId=' + nodeId + '&mapNode.icon=' + callback.path;
			$.ajax({url: '../topology/setMapNodeIcon.tv', type: 'POST', 
				data: params, success: function() {
					var el = Zeta$('icon');
					el.src = callback.path;
					var frame = window.top.getFrame('topo' + superiorId);
					if (frame != null) {
						try {
							frame.synUpdateNodeIcon(nodeId, callback.path, el.width, el.height);
						} catch(err) {
						}	
					}
				}, error: function() {
					window.top.showErrorDlg();
				}, dataType: 'plain', cache: false});		
		}	
	}
}

function onDisplayGridClick(obj) {
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.displayGrid=' + obj.checked;
	$.ajax({url: '../topology/setTopoMapDisplayGrid.tv', type: 'POST', data: params,
	success: function() {
		var frame = window.top.getFrame('topo' + folderId);
		if (frame) {
			try {
				frame.setDisplayGrid(obj.checked);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});
}
function onDisplayEntityLabelClick(obj) {
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.displayEntityLabel=' + obj.checked;
	$.ajax({url: '../topology/updateDisplayEntityLabel.tv', type: 'GET', data: params,
	success: function() {
		var frame = window.top.getFrame('topo' + folderId);
		if (frame) {
			try {
				frame.setDisplahEntityLabel(obj.checked);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});
}
function onDisplayAlertIconClick(obj) {
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.displayAlertIcon=' + obj.checked;
	$.ajax({url: '../topology/updateDisplayAlertIcon.tv', type: 'GET', data: params,
	success: function() {
		var frame = window.top.getFrame('topo' + folderId);
		if (frame) {
			try {
				frame.setDisplayAlertIcon(obj.checked);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});
}
function onMarkerAlertModeClick(obj) {
	if (obj.checked) {
   		Zeta$('displayAlertIcon').disabled = false;
   	} else {
   		Zeta$('displayAlertIcon').disabled = true;
   	}
	$.ajax({url: '../topology/updateMarkerAlertMode.tv', type: 'GET',
	data: {'topoFolder.folderId': folderId, 'topoFolder.markerAlertMode': obj.checked},
	success: function() {
		var frame = window.top.getFrame('topo' + folderId);
		if (frame) {
			try {
				frame.setMarkerAlertMode(obj.checked);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});   	
}
function isValidUrl(s){
    //var regex = new RegExp("^(http[s]?:\\/\\/(www\\.)?|ftp:\\/\\/(www\\.)?|www\\.){1}([0-9A-Za-z-\\.@:%_\+~#=]+)+");
    var regex = /^(http:\/\/)[a-zA-Z\d\-_\[\]()\/\.:@%+~=]{1,64}$/
    return regex.test(s);
}
function onSaveClick() {
	var n = Zeta$('name').value.trim();
	if (n == '') {
		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.folderNameNotNull);
		top.afterSaveOrDelete({
	    	title:I18N.COMMON.tip,
	    	html:'<b class="orangeTxt">'+I18N.NETWORK.folderNameNotNull+'</b>'
	    });
		return;
	}
	var $url = $("#url").val();
	if($url.length > 0){
		if( !isValidUrl($url) ){
			top.afterSaveOrDelete({
		    	title:I18N.COMMON.tip,
		    	html:'<b class="orangeTxt">'+I18N.invalidURL+'</b>'
		    });
			$("#url").focus();
			return;
		}
	}
	if (subnetFlag) {
		Zeta$('subnetIp').value = getIpText('ip');
		Zeta$('subnetMask').value = getIpText('mask');
	}
	$.ajax({
		url: '../topology/updateTopoFolderOutline.tv', type: 'POST', dataType:'json',
		data: jQuery(folderForm).serialize(),
		success: function(json) {
			if(json){
				return WIN.top.showMessageDlg("@MENU.tip@", "@NETWORK.folderExist@");
			}
			sycUpdateFolderName(nodeId, n);
			sycUpdateFolderSize();
			//window.top.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.updateSuccessfully);
			top.afterSaveOrDelete({
				title:I18N.COMMON.tip, 
				html: '<b class="orangeTxt">' + I18N.COMMON.updateSuccessfully + '</b>'
			})
			//如果框架顶部选择的是拓扑图图标，那么刷新名称,修改:leexiang;
			var leftSideSrc = parseInt(top.$("#menuBtn_topo").css("top"),10);
			if(leftSideSrc < -100){
				top.frames["menuFrame"].refreshTree();
				//top.frames["menuFrame"].location.href = top.frames["menuFrame"].location.href; 				
			}//实际上得到的是-130,hover得到的-65，选中得到-130;
			
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});
}
function sycUpdateFolderName(id, name) {
	try {
        //top.getFrame('topo' + superiorId).updateNodeName(id, name, Zeta$('url').value.trim());
		top.getFrame('topo' + superiorId).updateFolderName(id, name, Zeta$('url').value.trim());
	} catch (err) {
	}
	/*try {
		window.top.getMenuFrame().renameTopoFolder(id, name);
	} catch (err) {
	}*/
	
}

function sycUpdateFolderSize() {
	var size = Zeta$('folderSize').value.split('x');
	try {
		window.top.getFrame('topo' + folderId).resizeTopoFolderSize(size[0], size[1]);
	} catch (err) {
	}
}

function popSelectBackgroundColor() {
	window.top.createDialog('colorDlg', I18N.COMMON.colorPicker, 320, 340,
		'include/colorDlg.jsp?color=' + backgroundColor, null,
		true, true, selectBackgroundColor);
}
function selectBackgroundColor() {
	if (window.top.ZetaCallback != null && window.top.ZetaCallback.type == 'colorpicker') {
		setBackgroundType('bfid');
		var color = window.top.ZetaCallback.color;
		backgroundColor = Zeta$('bgArea').style.backgroundColor = color;
		var params = 'topoFolder.folderId=' + folderId + '&topoFolder.backgroundColor=' + color;
		$.ajax({url: '../topology/updateFolderBgColor.tv', type: 'POST', 
			data: params, success: function() {
			}, error: function() {
			}, dataType: 'plain', cache: false});		
	}
}
function popSelectLinkColor() {
	window.top.createDialog('colorDlg', I18N.COMMON.colorPicker, 320, 340,
		'include/colorDlg.jsp?color=' + linkColor, null, true, true, selectLinkColor);	
}
function selectLinkColor() {
	if (window.top.ZetaCallback != null && window.top.ZetaCallback.type == 'colorpicker') {
		var color = window.top.ZetaCallback.color;
		var frame = window.top.getFrame('topo' + folderId);
		if (frame != null) {
			try {
				frame.setDefaultEdgeColor(color);
			} catch (err) {
			}
		}
		Zeta$('linkArea').style.backgroundColor = color;
		var params = 'topoFolder.folderId=' + folderId + '&topoFolder.linkColor=' + color;	
		$.ajax({url: '../topology/updateFolderLinkColor.tv', type: 'POST', 
			data: params, success: function() {
			}, error: function() {
			}, dataType: 'plain', cache: false});			
	}
}
function setLinkWidth(width) {
		var params = 'topoFolder.folderId=' + folderId + '&topoFolder.linkWidth=' + width;
		$.ajax({url: '../topology/updateFolderLinkWidth.tv', type: 'POST', 
			data: params, success: function() {
				var frame = window.top.getFrame('topo' + folderId);
				if (frame != null) {
					try {
						frame.synUpdateLinkWidth(width);
					} catch (err) {
					}				
				}
			}, error: function() {
			}, dataType: 'plain', cache: false});
}

function popSelectFile() {
	window.top.createWindow('imageChooser', I18N.COMMON.selectBackgrounImage, 800, 500,
		'include/showImageChooser.tv?module=background', null,
		true, true, setFolderBackgroundImg);
}
function setFolderBackgroundImg() {
	var callback = window.top.getZetaCallback();
	if (callback != null && callback.type == 'image') {
		var path = callback.path;
		var params = 'topoFolder.folderId=' + folderId + '&topoFolder.backgroundImg=' + path;
		$.ajax({url: '../topology/updateFolderBgImg.tv', type: 'POST', 
			data: params, success: function() {
				backgroundImg = Zeta$('backgroundImg').value = path;
				if (!Zeta$('bfid').checked) {
					var frame = window.top.getFrame('topo' + folderId);
					if (frame != null) {
						frame.setBackgroundFlag(true, path);
					}
				}
			}, error: function() {
				window.top.showErrorDlg();
			}, dataType: 'plain', cache: false});
		setBackgroundType('backgroundFlag');
	}
}

function enableBackgroundImg(box) {
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.backgroundFlag=' + box.checked;
	$.ajax({url: '../topology/updateFolderBgFlag.tv', type: 'POST', 
		data: params, success: function() {
			var frame = window.top.getFrame('topo'+folderId);
			if (frame != null) {
				if (box.checked && backgroundImg != '') {
					frame.setBackgroundFlag(true, backgroundImg);
				} else {
					frame.setBackgroundFlag(false, backgroundColor);
				}
			}		
		}, error: function() {
		}, dataType: 'plain', cache: false});	
}

function onLinkWidthChanged(obj) {
   setLinkWidth(obj.options[obj.selectedIndex].value);
}

function onPositionChanged(obj) {
	var v = obj.options[obj.selectedIndex].value;
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.backgroundPosition=' + v;
	$.ajax({url: '../topology/updateFolderBgPosition.tv', type: 'POST', 
		data: params, success: function() {
			var frame = window.top.getFrame('topo' + folderId);
			if (frame != null) {
				try {
					frame.setBackgroundPosition(v);
				} catch (err) {
				}
			}
		}, error: function() {
		}, dataType: 'plain', cache: false});	
}
function iconSizeChanged(obj) {
	var v = obj.options[obj.selectedIndex].value;
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.iconSize=' + v;
	$.ajax({url: '../topology/updateFolderIconSize.tv', type: 'POST', 
		data: params, success: function() {
			var frame = window.top.getFrame('topo' + folderId);
			if (frame != null) {
				try {
					frame.setNodeIconSize(v, folderId);
				} catch (err) {
				}
			}
		}, error: function() {
		}, dataType: 'plain', cache: false});
}
function onLinkShadowChanged(obj) {	
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.linkShadow=' + obj.checked;
	$.ajax({url: '../topology/updateTopoMapLinkShadow.tv', type: 'POST', data: params,
	success: function() {
		var frame = window.top.getFrame('topo' + folderId);
		if (frame) {
			try {
				frame.setDisplahLinkShadow(obj.checked, folderId);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});
}

function onDisplayLinkLabelClick(obj) {
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.displayLinkLabel=' + obj.checked;
	$.ajax({url: '../topology/updateDisplayLinkLabel.tv', type: 'GET', data: params,
	success: function() {
		var frame = window.top.getFrame('topo' + folderId);
		if (frame) {
			try {
				frame.setDisplahLinkLabel(obj.checked);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});
}

function onDisplayLinkClick(obj) {
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.displayLink=' + obj.checked;
	$.ajax({url: '../topology/updateDisplayLink.tv', type: 'GET', data: params,
	success: function() {
		var frame = window.top.getFrame('topo' + folderId);
		if (frame) {
			try {
				frame.setDisplayLink(obj.checked);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});
}

function onDisplayCluetipClick(obj) {
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.displayCluetip=' + obj.checked;
	$.ajax({url: '../topology/updateDisplayCluetip.tv', type: 'GET', data: params,
	success: function() {
		var frame = window.top.getFrame('topo' + folderId);
		if (frame) {
			try {
				frame.setCluetipEnabled(obj.checked);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});
}

function setDisplayNameLabel(showType) {
	$.ajax({url: '../topology/setTopoMapDisplayName.tv', type: 'GET', data: {"topoFolder.folderId": folderId, "topoFolder.showType": showType},
	success: function() {
		var frame = window.top.getFrame('topo' + folderId);
		if (frame) {
			try {
				frame.setDisplayName(showType);
			} catch(err) {
			}
		}
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});
}

function setBackgroundType(type) {
	if (type == 'displayGrid') {
		$.ajax({url: '../topology/setTopoMapDisplayGrid.tv', type: 'POST', data: {'topoFolder.folderId': folderId, 'topoFolder.displayGrid': true},
		success: function() {
			var frame = window.top.getFrame('topo' + folderId);
			if (frame) {
				try {
					frame.setDisplayGridFlag(true);
					frame.showBackground();
					Zeta$('displayGrid').checked = true;
					Zeta$('backgroundFlag').checked = false;
					Zeta$('bfid').checked = false;
					Zeta$('backgroundPosition').disabled = true;
					Zeta$('imgBt').disabled = true;
                    Zeta$('colorBt').disabled = true;
				} catch(err) {
				}	
			}
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});
	} else if (type == 'backgroundFlag' || type == 'bfid') {
		var bgFlag = true;
		if (type == 'bfid') {
			bgFlag = false;
		}
		$.ajax({url: '../topology/updateFolderBgFlag.tv', type: 'POST',  data: {'topoFolder.folderId': folderId, 'topoFolder.backgroundFlag': bgFlag},
			success: function() {
				var frame = window.top.getFrame('topo' + folderId);
				if (frame != null) {
					if (bgFlag) {
						frame.setDisplayGridFlag(false);
						frame.setImageOrColorFlag(true);
                        //frame.setBackgroundColor('#FFFFFF');
						frame.setBackgroundImg(backgroundImg);
						frame.showBackground();
						Zeta$('displayGrid').checked = false;
						Zeta$('backgroundFlag').checked = true;
						Zeta$('bfid').checked = false;
						Zeta$('backgroundPosition').disabled = false;
						Zeta$('imgBt').disabled = false;
                        Zeta$('colorBt').disabled = true;
					} else {
						frame.setDisplayGridFlag(false);
						frame.setImageOrColorFlag(false);
						frame.setBackgroundColor(backgroundColor);
						frame.showBackground();
						Zeta$('displayGrid').checked = false;
						Zeta$('backgroundFlag').checked = false;
						Zeta$('bfid').checked = true;
						Zeta$('backgroundPosition').disabled = true;
						Zeta$('imgBt').disabled = true;
                        Zeta$('colorBt').disabled = false;
					}
				}
			}, error: function() {
			}, dataType: 'plain', cache: false});
	}
}

Ext.BLANK_IMAGE_URL = '../images/s.gif';
function doOnload(){
	var el = Zeta$('linkWidthBox');
	if (el != null) {
	    for(var i = 0; i < el.options.length; i++) {
	    	if (el.options[i].value == linkWidth) {
	    		el.selectedIndex = i;
	    		break;
	    	}
	    }
    }
    
    el = Zeta$('folderSize');
	if (el != null) {
	    for(var i = 0; i < el.options.length; i++) {
	    	if (el.options[i].value == folderBounds) {
	    		el.selectedIndex = i;
	    		break;
	    	}
	    }
    }    
    
    Zeta$('backgroundPosition').selectedIndex = folderBgPosition;

	if (subnetFlag) {
    	setIpText('ip', subnetIp);
    	setIpText('mask', subnetMask);
    } else {
    	//if (Zeta$('markerAlertMode').checked) {
    		//Zeta$('displayAlertIcon').disabled = false;
    	//} else {
    		//Zeta$('displayAlertIcon').disabled = true;
    	//}
    }
}
