/**
 * Zetaframework JavaScript IpTextField by OO, for IE, Firefox, Chrome.
 *
 * (C) TopoView, 2008-04-08
 *
 * authro: niejun
 */
function isFullStop(key) {   
    return key == 190 || key == 110;   
}

function isNumber(key) {
	return (key >= 48 && key <= 57) || (key >= 96 && key <= 105);   
}

function getCharByKeyCode(evt) {
	var ch = '';
	if (evt.which == null) {
		// IE
		if (evt.keyCode >= 96) {
			ch = String.fromCharCode(evt.keyCode - 48);
		} else {
			ch = String.fromCharCode(evt.keyCode);
		}
	} else if (evt.which > 0) {
		// other
		ch = String.fromCharCode(evt.which);
	}
	return ch;
}

function ipmask(obj, evt, nextIndex) {
	var key = window.event ? evt.keyCode : evt.which;
	if (key == 39) {
		document.getElementById(nextIndex).focus();
	} else if (key == 37) {
	} else if (key == 190 || key == 110) {
		if (obj.value != '') {
			obj.blur();
			document.getElementById(nextIndex).focus();
		}
		return false;
	} else if (key == 8) {
		if (obj.value == '') {
			return false;
		}
	}
	var flag = (isSpecialKey(key)) || ((isNumber(key) && !evt.shiftKey)) || (evt.ctrlKey && (key == 86 || key ==66));
	if (flag) {
		var v = obj.value + getCharByKeyCode(evt);
		if (parseInt(v) > 255) {
			obj.value = '255';
			flag = false;
		}
	}
	return flag;
}

function ipmaskup(obj, evt, nextIndex) {
	if (obj.value.length == 3) {
		obj.blur();
		document.getElementById(nextIndex).focus();
	}
}

function ipmask_c(index) {
	try {
		var str = clipboardData.getData('text');
		var arr = str.split('.');
		for (var i = 0; i < arr.length; i++) {
			document.getElementById(index + (i + 1)).value = arr[i];
            document.getElementById(index + (i + 1)).focus();
		}
	} catch (err) {
	}
	// 清除系统剪切板，解决填充后还调用一次粘贴的问题。仅是粘贴空的数据，还需要改进。
    clipboardData.setData('text', '');
}
function checkIpText(v){
	var temp = v;
	if(v.length==3)
	{
		if(v.charAt(0)==0)
		{
			if(v.charAt(1)==0)
			{
				temp=v.charAt(2);
			}else{
				temp=v.substring(1,3);
			}
		}
	}else if(v.length==2){
		if(v.charAt(0)==0)
		{
			temp = v.charAt(1);
		}
	} 
	return temp;
}
function getIpText(id) {
	var v1 = document.getElementById(id + "1").value;
	if (v1 == '') {
		return '';
	}
	v1 = checkIpText(v1);
	var v2 = document.getElementById(id + "2").value;
	if (v2 == '') {
		return '';
	}
	v2 = checkIpText(v2);
	var v3 = document.getElementById(id + "3").value;
	if (v3 == '') {
		return '';
	}
	v3 = checkIpText(v3);
	var v4 = document.getElementById(id + "4").value;
	if (v4 == '') {
		return '';
	}
	v4 = checkIpText(v4);
	return (v1 + '.' + v2 + '.' + v3 + '.' + v4);
}

function setIpText(id, ip) {
	if (ip == '') {
		document.getElementById(id + "1").value = '';
		document.getElementById(id + "2").value = '';
		document.getElementById(id + "3").value = '';
		document.getElementById(id + "4").value = '';
	} else {
		var arr = ip.split('.');
		document.getElementById(id + "1").value = arr[0];
		document.getElementById(id + "2").value = arr[1];
		document.getElementById(id + "3").value = arr[2];
		document.getElementById(id + "4").value = arr[3];
	}
}
/**
 * 函数名： changeToBinary 
 * 函数功能： 返回传入参数对应的8位二进制值 
 * 函数作者： loyal
 * 传入参数： ip:点分十进制的值(0~255),int类型的值， 
 * 主调函数： validateMask 
 * 调用函数： 无 返回值:
 * ip对应的二进制值(如：传入255，返回11111111;传入1,返回00000001)
 */
function changeToBinary(ip) {
	return (ip + 256).toString(2).substring(1); // 格式化输出(补零)
}

/** 函数名： validateMask
 *  函数功能： 验证子网掩码的合法性
 *  函数作者： loyal
 * 	传入参数： MaskStr:点分十进制的子网掩码(如：255.255.255.192)
 *  主调函数：
 *  调用函数： changeToBinary(ip)
 *  返回值：   true:   MaskStr为合法子网掩码
 *             false: MaskStr为非法子网掩码
 */

function validateMask(MaskStr) {
	/* 有效性校验 */
	var IPPattern = /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/
	if (!IPPattern.test(MaskStr))
		return false;

	/* 检查域值 */
	var IPArray = MaskStr.split(".");
	var ip1 = parseInt(IPArray[0]);
	var ip2 = parseInt(IPArray[1]);
	var ip3 = parseInt(IPArray[2]);
	var ip4 = parseInt(IPArray[3]);
	if (ip1 < 0 || ip1 > 255 /* 每个域值范围0-255 */
			|| ip2 < 0 || ip2 > 255 || ip3 < 0 || ip3 > 255 || ip4 < 0
			|| ip4 > 255) {
		return false;
	}
	/* 检查二进制值是否合法 */
	// 拼接二进制字符串
	var ip_binary = changeToBinary(ip1) + changeToBinary(ip2)
			+ changeToBinary(ip3) + changeToBinary(ip4);
	if (-1 != ip_binary.indexOf("01"))// 格式化输出(补零)
		return false;
	return true;
}

/**
 * ipA(string), ipB(string)与运算
 * @param ipA
 * @param ipB
 * @returns {String}
 */
function ipAAndipB(ipA, ipB){
	var IPArrayA = ipA.split(".");
	var IPArrayB = ipB.split(".");
	var ip1 = parseInt(IPArrayA[0]) & parseInt(IPArrayB[0]) 
	var ip2 = parseInt(IPArrayA[1]) & parseInt(IPArrayB[1])
	var ip3 = parseInt(IPArrayA[2]) & parseInt(IPArrayB[2])
	var ip4 = parseInt(IPArrayA[3]) & parseInt(IPArrayB[3]);
	var ip = ip1.toString() + '.' + ip2.toString() + '.' + ip3.toString() + '.' + ip4.toString();
	return ip;
}
