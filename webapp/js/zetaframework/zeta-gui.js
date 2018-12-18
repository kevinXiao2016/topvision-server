/**
 * Zetaframework JavaScript Core.
 *
 * (C) TopoView, 2008-04-08
 *
 * authro: niejun
 * 
 */
var selectAllCbEl = null;
var rowFocused = null;
var myGridId = 'myGrid';
var selectedAllInfo = '\u5df2\u9009\u4e2d \u6570\u636e\u5e93 \u4e2d\u7684\u6240\u6709{0}\u4e2a\u8bb0\u5f55\u3002<a href="javascript:disselectDatabaseAllRow({1});">\u6e05\u9664\u9009\u4e2d</a>';
var disselectedAllInfo = '\u6b64\u9875\u4e2d\u7684\u6240\u6709 {0} \u6761\u8bb0\u5f55\u90fd\u5df2\u9009\u4e2d\u3002<a href="javascript:selectDatabaseAllRow({1});">\u9009\u62e9 \u6570\u636e\u5e93</a> \u4e2d\u7684\u6240\u6709 {2} \u4e2a\u8bb0\u5f55';
function selectTableRow(obj, e) {
	var event = window.event||e; // for firefox
	if (event.button == MouseEvent.BUTTON2) {
		return;
	}
	if (event.shiftKey) {
		if (rowFocused == null) {
			rowFocused = obj;
		}
		var i1 = rowFocused.rowIndex;
		var i2 = obj.rowIndex;
		var start = end = 0;
		if (i1 >= i2) {
			start = i2;
			end = i1;
		} else {
			start = i1;
			end = i2;		
		}
		var rows = Zeta$(myGridId).rows;
		for (var i = start; i <= end; i++) {
			rows[i].className = 'myGridRowSelected';
			rows[i].children[0].children[0].checked = true;
		}
	} else {
		var checkEl = obj.children[0].children[0];
		var tagName = isFirefox ? event.target.tagName : event.srcElement.tagName;
		if (tagName == 'IMG' || tagName == 'A') {
			return;
		}
		if (tagName == 'INPUT') {
			if (checkEl.checked) {
				obj.className = 'myGridRowSelected';
			} else {
				//obj.style.backgroundColor = 'white';
				obj.className = 'myGridRow';
				if (selectAllCbEl == null) {
					selectAllCbEl = Zeta$('selectAllCb');
				}
				if (selectAllCbEl != null && selectAllCbEl.checked) {
					selectAllCbEl.checked = false;
					Zeta$('myGridInfo').parentNode.style.display = 'none';
				}		
			}		
		} else {
			if (checkEl.checked) {
				//obj.style.backgroundColor = 'white';
				obj.className = 'myGridRow';
				checkEl.checked = false;
				if (selectAllCbEl == null) {
					selectAllCbEl = Zeta$('selectAllCb');
				}
				if (selectAllCbEl != null && selectAllCbEl.checked) {
					selectAllCbEl.checked = false;
					Zeta$('myGridInfo').parentNode.style.display = 'none';
				}
			} else {
				//obj.style.backgroundColor = '#ffffcc';
				obj.className = 'myGridRowSelected';
				checkEl.checked = true;
			}	
		}
	}
	rowFocused = obj;
}

function selectAllMyGrid(obj, grid, totalCount) {
	var rows = Zeta$(grid).rows;
	var count = rows.length - 1;
	if (count == 2) {
		return;
	}
	if (obj.checked) {
		Zeta$('myGridInfo').parentNode.style.display = 'block';
		for (var i = 2; i < count; i++) {
			//rows[i].style.backgroundColor = '#ffffcc';
			rows[i].className = 'myGridRowSelected';
			rows[i].children[0].children[0].checked = true;
		}
	} else {
		disselectDatabaseAllRow(totalCount);
		Zeta$('myGridInfo').parentNode.style.display = 'none';
		for (var i = 2; i < count; i++) {
			//rows[i].style.backgroundColor = 'white';
			rows[i].className = 'myGridRow';
			rows[i].children[0].children[0].checked = false;
		}
	}
}

function selectDatabaseAllRow(count) {
	Zeta$('emptyAllRow').value = 'true';
	Zeta$('myGridInfo').children[0].innerHTML = selectedAllInfo.replace('{0}', count).replace('{1}', count);
}
function disselectDatabaseAllRow(count) {
	Zeta$('emptyAllRow').value = 'false';
	Zeta$('selectAllCb').checked = false;
	Zeta$('myGridInfo').parentNode.style.display = 'none';
	var rows = Zeta$(myGridId).rows;
	var c = rows.length - 1;
	Zeta$('myGridInfo').children[0].innerHTML = disselectedAllInfo.replace('{0}', c - 2).replace('{1}', count).replace('{2}', count);
	for (var i = 2; i < c; i++) {
		//rows[i].style.backgroundColor = 'white';
		rows[i].className = 'myGridRow';
		rows[i].children[0].children[0].checked = false;
	}
}

function sortMyGrid(name, grid) {
	var el = Zeta$('sort');
	if (el.value == name) {
		var dirEl = Zeta$('dir');
		if (dirEl.value == 'asc') {
			dirEl.value = 'desc';
		} else {
			dirEl.value = 'asc';
		}
	} else {
		el.value = name;
		Zeta$('dir').value = 'asc';
	}
	myGridForm.submit();
}

function isGridSelected(grid) {
	var rows = Zeta$(grid).rows;
	var count = rows.length - 1;
	for (var i = 2; i < count; i++) {
		if (rows[i].children[0].children[0].checked)
			return true;
	}
	return false;
}

function getGridSelectedIds(grid) {
	var rows = Zeta$(grid).rows;
	var count = rows.length - 1;
	var ids = [];
	var inputEl = null;
	for (var i = 2; i < count; i++) {
		inputEl = rows[i].children[0].children[0]; 
		if (inputEl.checked) {
			ids[ids.length] = inputEl.value;
		}
	}
	return ids;
}

function doPageSubmit(action, page) {
	Zeta$('curPage').value = page;
	myGridForm.action = action;
	myGridForm.submit();
}

function doGoPageSubmit(action, page) {
	Zeta$('curPage').value = page;
	myGridForm.action = action;
	myGridForm.submit();
}

function doGoPageKeydown(action, pageCount, e) {
	var event = window.event||e; // for firefox
	if (event.keyCode == KeyEvent.VK_ENTER) {
		var el = event.srcElement;
		if (el == null) {
			el = event.target;
		}
		if(isNaN(el.value)){
			showSplashError('\u63d0\u793a', '\u8bf7\u8f93\u5165\u4e00\u4e2a\u6570\u5b57\uff01', '../images/error.gif');
		} else {
			var v = parseInt(el.value); 
			if (v <= 0 || v > pageCount) {
				showSplashError('\u63d0\u793a', '\u8be5\u9875\u4e0d\u5b58\u5728\uff0c\u8bf7\u91cd\u65b0\u8f93\u5165\uff01', '../images/error.gif');
			} else {
				doGoPageSubmit(action, el.value);
			}
		}
	}
}


function MM_preloadImages() {
	var d = document;
	if (d.images) {
		if(!d.MM_p) {
			d.MM_p=new Array();
		}
		var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
		for (i = 0; i < a.length; i++) {
			if (a[i].indexOf("#") != 0) {
				d.MM_p[j] = new Image;
				d.MM_p[j++].src = a[i];
			}
		}
	}
}

function MM_swapImage() {
	var i, j = 0, x, a = MM_swapImage.arguments;
    document.MM_sr = new Array;

	for(i=0;i<(a.length-2);i+=3) {
		if ((x = MM_findObj(a[i])) != null) {
			document.MM_sr[j++]=x;
			if (!x.oSrc) {
				x.oSrc=x.src;
			}
			x.src=a[i+2];
		}
	}
}

function MM_swapImgRestore() {
	var i, x, a = document.MM_sr;
	for(i=0; a && i<a.length && (x = a[i]) && x.oSrc; i++) {
		x.src = x.oSrc;
	}
}

function MM_findObj(n, d) {
	var p, i, x;
	if (!d) {
		d = document;
	}
	if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
		d = parent.frames[n.substring(p+1)].document;
		n = n.substring(0, p);
	}

	if (!(x = d[n]) && d.all) {
		x = d.all[n];
	}

	for (i = 0; !x && i < d.forms.length; i++) {
		x = d.forms[i][n];
	}

	for (i = 0; !x && d.layers && i < d.layers.length; i++) {
		x = MM_findObj(n, d.layers[i].document);
	}

	if (!x && document.getElementById) {
		x = document.getElementById(n);
	}

	return x;
}

function judgeKey() {
    var code = event.keyCode;
    var ctrl = event.ctrlKey;
    var alt = event.altKey;
    var shift = event.shiftKey;

    if (ctrl) {
        if (code == KeyEvent.VK_A) {
            event.keyCode = 0;
            event.returnValue = false;
            return false;
        }
    }

    if (alt) {
		// Alt F4
        if (code == KeyEvent.VK_F4) {
            event.keyCode = 0;
            event.returnValue = false;
            return false;
        }
    }

    var src = window.event.srcElement;
    // BACK SPACE
    if (code == KeyEvent.VK_BACKSPACE) {
		if (src.tagName == "INPUT" ||
			src.tagName == "TEXTAREA") {
		} else if (src.tagName == "SPAN") {
            event.keyCode = 0;
            event.returnValue = false;
            return false;
		} else {
            event.keyCode = 0;
            event.returnValue = false;
            return false;
        }
    }
    // F5 refresh
    if (code == KeyEvent.VK_F5) {
        event.keyCode = 0;
        event.returnValue = false;
        return false; 
    }    		
    return true;
}