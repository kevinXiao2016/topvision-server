var slotType = {
	0: 'slot',
	1: 'mpua',
	2: 'mpub',
	3: 'epua',
	4: 'epub',
	5: 'geua',
	6: 'geub',
	7: 'xgua',
	8: 'xgub',
	9: 'xguc',
	10: 'xpua',
	11: 'mpu-geua',
	12: 'mpu-geub',
	13: 'mpu-xguc',
	14: 'gpua',
	15: 'unknown',
	16: 'null',
	17: 'meua',
	18: 'meub',
	19: 'mefa',
	20: 'mefb',
	21: 'mefc',
	22: 'mefd',
	23: 'mgua',
	24: 'mgub',
	25: 'mgfa',
	26: 'mgfb'
};

var actualSlotType = {
	0: 'slot',
	1: 'mpua',
	2: 'mpub',
	3: 'epua',
	4: 'epub',
	5: 'geua',
	6: 'geub',
	7: 'xgua',
	8: 'xgub',
	9: 'xguc',
	10: 'xpua',
	11: 'gpua',
	12: 'meua',
	13: 'meub',
	14: 'mefa',
	15: 'mefb',
	16: 'epuc',
	17: 'epud',
	21: 'mefc',
	22: 'mefd',
	23: 'mgua',
	24: 'mgub',
	25: 'mgfa',
	26: 'mgfb'
};

var SLOT_BOARD = "slot";

/**
 * 将端口index转换成 type slot/port格式的字符串
 * @param arr
 * @param disabled
 */
function convertPortIndexToStrWithType(slotPreType, portIndex) {
	var str = '';
	var portObj = analysisPortIndex(portIndex);
	
	switch(slotPreType) {
	case 'xguc':
		//1-2 XE
		//3-6 GE
		if(portObj.port < 3) {
			str = 'XE-' + portObj.slot + '/' + portObj.port;
		} else {
			str = 'GE-' + portObj.slot + '/' + (portObj.port - 2);
		}
		break;
	case 'meua':
	case 'mefa':
	case 'mefb':
	case 'mefc':
		if(portObj.port < 17) {
			str = 'PON-' + portObj.slot + '/' + portObj.port;
		} else if(portObj.port < 19) {
			str = 'XE-' + portObj.slot + '/' + (portObj.port - 16);
		} else {
			str = 'GE-' + portObj.slot + '/' + (portObj.port - 18);
		}
		break;
	case 'meub':
		if(portObj.port < 17) {
			str = 'PON-' + portObj.slot + '/' + portObj.port;
		} else {
			str = 'GE-' + portObj.slot + '/' + (portObj.port - 16);
		}
		break;
	case 'mgua':
	case 'mgub':
		if(portObj.port < 17) {
			str = 'PON-' + portObj.slot + '/' + portObj.port;
		} else {
			str = 'XE-' + portObj.slot + '/' + (portObj.port - 16);
		}
		break;
	default:
		str = portObj.slot + '/' + portObj.port;
	}
	
	return str;
}

function getPortNoByType(slotPreType, portNo) {
	var retNo = 0;
	
	switch(slotPreType) {
	case 'xguc':
		//1-2 XE
		//3-6 GE
		if(portNo < 3) {
			retNo = portNo;
		} else {
			retNo = portNo - 2;
		}
		break;
	case 'meua':
	case 'mefa':
	case 'mefb':
	case 'mefc':
		if(portNo < 17) {
			retNo = portNo;
		} else if(portNo < 19) {
			retNo = portNo - 16;
		} else {
			retNo = portNo - 18;
		}
		break;
	case 'meub':
		if(portNo < 17) {
			retNo = portNo;
		} else {
			retNo = portNo - 16;
		}
		break;
	case 'mgua':
		if(portNo < 17) {
			retNo = portNo;
		} else {
			retNo = portNo - 16;
		}
		break;
	default:
		retNo = portNo;
	}
	return retNo;
}