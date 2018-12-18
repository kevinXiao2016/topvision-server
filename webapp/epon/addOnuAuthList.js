function renderIndex(value, p, record) {
	if(value < 65){
		var t = parseInt(record.data.ponIndex / 256) + (record.data.ponIndex % 256);
		return getNum(t, 1) + "/" + getNum(t, 2) + ":" + value;
	}else{
		var t = parseInt(value / 256) + (value % 256);
		return getNum(t, 1) + "/" + getNum(t, 2) + ":" + getNum(t, 3);
	}
}

function getOnuId(onuIndex){
	if(onuIndex < 65){
		return onuIndex;
	}else{
		var t = parseInt(onuIndex / 256) + (onuIndex % 256);
		return getNum(t, 3);
	}
}

function getNum(index, s) {
	var num;
	switch (s) {
	case 1:
		num = (index & 0xFF000000) >> 24;// SLOT
		break;
	case 2:
		num = (index & 0xFF0000) >> 16;// PON/SNI
		break;
	case 3:
		num = (index & 0xFF00) >> 8;// ONU
		break;
	case 4:
		num = index & 0xFF;// UNI
		break;
	}
	return num;
}

function renderAuthMode(value, p, record) {
	if (value == 1) {
		record.set('authType', 1)
		return "@onuAuth.autoMode@";
	} else if (value == 2) {
		return "@onuAuth.mac@";
	} else if (value == 3) {
		return "@onuAuth.mix@";
	} else if (value == 4) {
		record.set('authType', 2)
		return "@onuAuth.sn@";
	} else if (value == 5) {
		record.set('authType', 2)
		return "@onuAuth.snPwdMode@";
	}
}

function onMonuseDown(e, t) {
	if (e.button === 0 && t.className == 'x-grid3-row-checker') { // Only fire
		// if
		// left-click
		e.stopEvent();
		var row = e.getTarget('.x-grid3-row');

		if (!this.mouseHandled && row) {
			var gridEl = this.grid.getEl();// 得到表格的EL对象
			var hd = gridEl.select('div.x-grid3-hd-checker');// 得到表格头部的全选CheckBox框
			var index = row.rowIndex;
			if (this.isSelected(index)) {
				this.deselectRow(index);
				var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				if (isChecked) {
					hd.removeClass('x-grid3-hd-checker-on');
				}
			} else {
				this.selectRow(index, true);
				if (gridEl.select('div.x-grid3-row-selected').elements.length == gridEl
						.select('div.x-grid3-row').elements.length) {
					hd.addClass('x-grid3-hd-checker-on');
				}
				;
			}
		}
	}
	this.mouseHandled = false;
}

function onHdMouseDown(e, t) {
	if (t.className.split(' ')[0] == 'x-grid3-hd-checker') {
		e.stopEvent();
		var hd = Ext.fly(t.parentNode);
		var isChecked = hd.hasClass('x-grid3-hd-checker-on');
		if (isChecked) {
			hd.removeClass('x-grid3-hd-checker-on');
			this.clearSelections();
		} else {
			hd.addClass('x-grid3-hd-checker-on');
			this.selectAll();
		}
	}
}

function renderAuthMode(authMode) {
	if (authMode == 1) {
		return "@onuAuth.autoMode@";
	} else if (authMode == 2) {
		return "@onuAuth.mac@";
	} else if (authMode == 3) {
		return "@onuAuth.mix@";
	} else if (authMode == 4) {
		return "@onuAuth.sn@";
	} else if (authMode == 5) {
		return "@onuAuth.snPwdMode@";
	}
}

function renderAuthAction(value, p, record) {
	if (value == 1) {
		return "@onuAuth.permit@";
	} else if (value == 2) {
		return "@onuAuth.reject@";
	}
}

function renderPon(ponIndex) {
	var ponIndex = parseInt(ponIndex / 256) + (ponIndex % 256);
	return ((ponIndex & 0xFF000000) >> 24) + '/'
			+ ((ponIndex & 0xFF0000) >> 16);
}

function buildEntityTypeSelect() {
	var deviceTypePosition = Zeta$('entityType');
	for (var i = 0; i < entityTypes.length; i++) {
		if (entityTypes[i].typeId == 255 || entityTypes[i].typeId == 13100) {
			continue;
		}
		var option = document.createElement('option');
		option.value = entityTypes[i].typeId;
		option.text = entityTypes[i].displayName;
		try {
			deviceTypePosition.add(option, null);
		} catch (ex) {
			deviceTypePosition.add(option);
		}
	}
}

function manuRender(v, m, r) {
	return String
			.format(" <a href='javascript:;' onClick='showNextPage(\"next\")'>@COMMON.edit@</a>");
}

Ext.override(Ext.grid.CheckboxSelectionModel, {
	onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});

function ponIndexChange(ponIndex, authMode) {
	var onuIndexPosition = Zeta$('onuIndex');
	for (var i = 1, len = onuIndexPosition.length; i <= len; i++) {
		onuIndexPosition.options[0] = null;
	}
	for (var i = 1; i < 65; i++) {
		var option = document.createElement('option');
		option.value = i;
		option.text = renderPon(ponIndex) + "/" + i;
		try {
			onuIndexPosition.add(option, null);
		} catch (ex) {
			onuIndexPosition.add(option);
		}
	}
	Zeta$('authMode').value = renderAuthMode(authMode)
	if (authMode == 2) {
		Zeta$('authType').value = 1
		$("#authType").attr("disabled", true);
	} else if (authMode == 4 || authMode == 5) {
		$("#authType").attr("disabled", true);
		Zeta$('authType').value = 2
	} else {
		$("#authType").attr("disabled", false);
	}
	authTypeChange()
}

function authTypeChange(){
	var authType = Zeta$('authType').value,
	    $tb = $("#mainTb");
	if(authType == 1){
		$tb.find("tbody").css({display:''});
		$tb.find("tbody:eq(2)").css({display:'none'});
	}else{
		$tb.find("tbody").css({display:''});
		$tb.find("tbody:eq(1)").css({display:'none'});
	}
}

function renderOnuPretype(value, p, record){
	for (var i = 0; i < entityTypes.length; i++) {
		if (entityTypes[i].typeId == value) {
			return entityTypes[i].displayName;
		}
		if(value == 0){
			return 'NONE';
		}
		if(value == 241){
			return 'CC8800'
		}
}}

function showNextPage(para) {
	if (para === 'next') {
		$("#w1700").animate({
			left : -850
		}, 'fast');
		initEditForm(baseGrid.getSelectionModel().getSelected().data);
	} else {
		$("#w1700").animate({
			left : 0
		}, 'fast');
	}
}

function initEditForm(data) {
	Zeta$('mac').value = data.mac
	Zeta$('manageIp').value = data.manageIp
	Zeta$('sn').value = data.sn
	Zeta$('password').value = data.password
	ponIndexChange(data.ponIndex, data.authMode);
	Zeta$('onuIndex').value = getOnuId(data.onuIndex)
}

function updateGridColumn() {
	var record = baseGrid.getSelectionModel().getSelected();
	record.set('mac', Zeta$('mac').value)
	record.set('authAction', Zeta$('action').value)
	record.set('authType', Zeta$('authType').value)
	record.set('onuPreType', Zeta$('entityType').value)
	record.set('sn', Zeta$('sn').value)
	record.set('password', Zeta$('password').value)
	record.set('onuId', Zeta$('onuIndex').value)
	record.set('onuIndex', Zeta$('onuIndex').value)
	showNextPage('prev')
}

