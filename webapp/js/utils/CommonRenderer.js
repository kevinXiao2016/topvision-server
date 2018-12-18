function renderNote(value, p, record) {
	return String.format("<img src=\"images/fault/level{0}.gif\" title=\"{1}\" border=0 align=absmiddle>&nbsp;{2}",
		value, record.data.levelName, record.data.message);
}
function renderHost(value, p, record) {
	return (record.data.host == null || record.data.host == '') ? record.data.source : record.data.host;
}

function renderStatus(value, p, record) {
	return String.format("<img hspace=5 src=\"images/fault/level{0}.gif\" title=\"{1}\" border=0 align=absmiddle>{2}",
		record.data.level, record.data.levelName, value);
}

function percentRendererIn100(value) {
	if(!value) {
		return '-';
	}
	return value + '%';
}

function renderPercent(value) {
	if(!value) {
		return '-';
	}
	return parseInt(value * 100, 10) + '%';
}

function entityNameRender(entity) {
	//entityDisplayField
	switch(entityDisplayField) {
	case 'ip':
		return entity.ip || '';
		break;
	case 'name':
		return entity.name || '';
		break;
	case 'sysName':
		return entity.sysName || '';
		break;
	case 'ip_name':
		return entity.ip + '(' + entity.name + ')' || '';
		break;
	case 'name_ip':
		return entity.name + '(' + entity.ip + ')' || '';
		break;
	}
}