

function rederIfNullCheck(value, p, record, columnIndex){
	if(value ==null){
		return "-";
	}else 
		return record.data[columnIndex];
}

function renderYesOrNo(value, p, record){
	if(value == "1"){
		return String.format('<img alt="{0}" src=\"/images/fault/confirm.png\" border=0 align=absmiddle title="{0}">',I18N.CMC.text.yes);
	}else
		return String.format('<img alt="{0}" src=\"/images/fault/level5.gif\" border=0 align=absmiddle title="{0}">',I18N.CMC.text.no);
}

function rendererAdminUpOrDown(value, p, record){
	if(value == "1"){
		return String.format('<img alt="{0}" src=\"/images/network/port/admin.gif\" border=0 align=absmiddle title="{0}">',I18N.CMC.text.up);
	}else
		return String.format('<img alt="{0}" src=\"/images/network/port/noadmin.gif\" border=0 align=absmiddle title="{0}">',I18N.CMC.text.down);
}