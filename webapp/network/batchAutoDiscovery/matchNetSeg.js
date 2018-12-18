var matchNetSeg = function() {
}

matchNetSeg.prototype.strictMatch = function(showIp, IpOrNetSeg) {
	if (this.checkType(IpOrNetSeg) == 4) {
		switch (this.checkType(showIp)) {
		case 1:
			return this.isInRange(IpOrNetSeg, showIp);
			break;
		case 2:
			var temp1 = showIp.split("\.");
			var temp2 = IpOrNetSeg.split("\.");
			var judge = true;
			for (var i = 0; i < temp1.length; i++) {
				if (temp1[i].indexOf("-") == -1) {
					if (temp1[i] == temp2[i]) {
						continue;
					} else {
						judge = false;
					}
				} else if (temp1[i].indexOf("-") != -1) {
					if (parseInt(temp2[i]) <= parseInt(temp1[i].split("-")[1])
							&& parseInt(temp2[i]) >= parseInt(temp1[i]
									.split("-")[0])) {
						continue;
					} else {
						judge = false;
					}
				}
			}
			return judge;
			break;
		case 3:
			var temp1 = showIp.split("\.");
			var temp2 = IpOrNetSeg.split("\.");
			var judge = true;
			for (var i = 0; i < temp1.length; i++) {
				if (temp1[i].indexOf("\*") == -1) {
					if (temp1[i] == temp2[i]) {
						continue;
					} else {
						judge = false;
					}
				} else if (temp1[i].indexOf("\*") != -1) {
					continue;
				}
			}
			return judge;
			break;
		case 4:
			if (showIp == IpOrNetSeg) {
				return true;
			} else {
				return false;
			}
			break;
		}
	} else if (this.checkType(IpOrNetSeg) == 5) {
		return true;
	} else {
		if (showIp.indexOf(IpOrNetSeg)!=-1) {
			return true;
		} else {
			return false;
		}
	}
}

matchNetSeg.prototype.checkType = function(IpOrNetSeg) {
	var ipInfo = IpOrNetSeg;
	var ipReg = /^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))$/;
	if (ipInfo == "" || ipInfo == null || ipInfo.length == 0) {
		return 5;
	}
	if (ipInfo.indexOf('/') != -1) {
		var ipMaskReg = /^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))\/(?:3[0-1]|2[4-9])$/;
		if (ipMaskReg.test(ipInfo)) {
			return 1;
		} else {
			return 0;
		}
	} else if (ipInfo.indexOf('-') != -1) {
		if (ipInfo.indexOf('-') !== ipInfo.lastIndexOf('-')) {
			return 0;
		}
		var ipSegments = ipInfo.split('\.'), ipLength = ipSegments.length;
		if (ipLength !== 4) {
			return 0;
		}
		var twoFiveFourReg = /^(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/, 
		rungReg = /^(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)-(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/, 
		result = 2;
		for (var i = 0; i < 4; i++) {
			if (twoFiveFourReg.test(ipSegments[i])) {
				continue;
			} else if (rungReg.test(ipSegments[i])) {
				var startNum = ipSegments[i].split('-')[0], endNum = ipSegments[i]
						.split('-')[1];
				startNum = parseInt(startNum);
				endNum = parseInt(endNum);
				if (startNum >= endNum) {
					result = 0;
					break;
				}
			} else {
				result = 0;
				break;
			}
		}
		return result;
	} else if (ipInfo.indexOf('\*') != -1) {
		if (ipInfo.indexOf('\*') !== ipInfo.lastIndexOf('\*')) {
			return 0;
		}
		var ipSegments = ipInfo.split('\.'), ipLength = ipSegments.length;
		if (ipLength !== 4) {
			return 0;
		}
		var twoFiveFourReg = /^(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/, result = 3;
		for (var i = 0; i < 4; i++) {
			if (twoFiveFourReg.test(ipSegments[i]) || ipSegments[i] === '\*') {
				continue;
			} else {
				result = 0;
				break;
			}
		}
		return result;
	}else if(ipReg.test(ipInfo)){
		return 4;
	}
	
}

matchNetSeg.prototype.isInRange = function(ip, netSeg) {
	var ips = ip.split("\.");
	var ipAddr = (parseInt(ips[0]) << 24) | (parseInt(ips[1]) << 16)
			| (parseInt(ips[2]) << 8) | parseInt(ips[3]);
	var type = parseInt(netSeg.replaceAll(".*/", ""));
	var mask = 0xFFFFFFFF << (32 - type);
	var netSegIp = netSeg.replaceAll("/.*", "");
	var netSegIps = netSegIp.split("\.");
	var netSegIpAddr = (parseInt(netSegIps[0]) << 24)
			| (parseInt(netSegIps[1]) << 16) | (parseInt(netSegIps[2]) << 8)
			| parseInt(netSegIps[3]);
	return (ipAddr & mask) == (netSegIpAddr & mask);
}
