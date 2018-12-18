/**
* # 描述
* 这是NM3000通用IP工具类，包含了诸多IP地址校验等方法，如有遗漏，请在此添加
*
* **使用范例**：
*
*     @example
*     IpUtil.isIpAddress('127.0.0.1');
* @class IpUtil
*/
var IpUtil = {
	/**
    * IPv4校验正则表达式
    * @type Regexp
    */
	v4Reg: /^(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]{1}\d|\d)\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]{1}\d|\d)\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]{1}\d|\d)\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]{1}\d|\d)$/,
	
	/**
    * IPv6校验正则表达式
    * @type Regexp
    */
	v6Reg: /^([\da-fA-F]{1,4}:){7}[\da-fA-F]{1,4}$|^:((:[\da-fA-F]{1,4}){1,6}|:)$|^[\da-fA-F]{1,4}:((:[\da-fA-F]{1,4}){1,5}|:)$|^([\da-fA-F]{1,4}:){2}((:[\da-fA-F]{1,4}){1,4}|:)$|^([\da-fA-F]{1,4}:){3}((:[\da-fA-F]{1,4}){1,3}|:)$|^([\da-fA-F]{1,4}:){4}((:[\da-fA-F]{1,4}){1,2}|:)$|^([\da-fA-F]{1,4}:){5}:([\da-fA-F]{1,4})?$|^([\da-fA-F]{1,4}:){6}:$/,
	
	/**
    * 环回地址校验正则表达式
    * @type Regexp
    */
	loopbackReg: /^127(\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)){3}$/,
	
	/**
    * 组播地址校验正则表达式
    * @type Regexp
    */
	multicastReg: /^2(2[4-9]|3[0-9])(\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)){3}$/,
	
	/**
    * 预留地址校验正则表达式
    * @type Regexp
    */
	reserveReg: /^2(4[0-9]|5[0-5])(\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)){2}\.(25[0-4]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)$/,
	
	/**
	 * 广播地址 0.0.0.0 – 0.255.255.255
	 * @type String
	 */
	broadcastReg: /^0(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]{1}[0-9]{1}|\d)){3}$/,
	
	/**
    * 受限广播地址
    * @type String
    */
	limitBroadcastIp: '255.255.255.255',
	
	/**
	 * 掩码校验正则表达式
	 * @type String
	 */
	maskReg:/^(254|252|248|240|224|192|128|0)\.0\.0\.0|255\.(254|252|248|240|224|192|128|0)\.0\.0|255\.255\.(254|252|248|240|224|192|128|0)\.0|255\.255\.255\.(255|254|252|248|240|224|192|128|0)$/,
	
	/**
	* 判断是否是合法的IP地址
	* @param {String} ip ip地址
	* @return {Boolean} 
	*/
	isIpAddress: function(ip){
		return this.isIpv4Address(ip) || this.isIpv6Address(ip);
	},
	
	/**
	* 判断是否是合法的IPv4地址
	* @param {String} ip ip地址
	* @return {Boolean} 
	*/
	isIpv4Address: function(ip){
		return this.v4Reg.test(ip);
	},
	
	/**
	* 判断是否是合法的IPv6地址
	* @param {String} ip ip地址
	* @return {Boolean} 
	*/
	isIpv6Address: function(ip){
		return this.v6Reg.test(ip);
	},
	
	/**
	* 判断是否是环回地址(127.0.0.0~127.255.255.255)
	* @param {String} ip ip地址
	* @return {Boolean} 
	*/
	isLoopbackAddress: function(ip){
		return this.loopbackReg.test(ip);
	},
	
	/**
	* 判断是否是组播地址(224.0.0.0~239.255.255.255)
	* @param {String} ip ip地址
	* @return {Boolean} 
	*/
	isMulticastAddress: function(ip){
		return this.multicastReg.test(ip);
	},
	
	/**
	* 判断是否是预留地址(240.0.0.0~255.255.255.254)
	* @param {String} ip ip地址
	* @return {Boolean} 
	*/
	isReserveAddress: function(ip){
		return this.reserveReg.test(ip);
	},
	
	/**
	* 判断是否是广播地址(0.0.0.0 – 0.255.255.255)
	* @param {String} ip ip地址
	* @return {Boolean} 
	*/
	isBroadcastIp: function(ip){
		return this.broadcastReg.test(ip);
	},
	
	/**
	* 判断是否是受限广播地址(255.255.255.255)
	* @param {String} ip ip地址
	* @return {Boolean} 
	*/
	isLimitBroadcastIp: function(ip){
		return this.limitBroadcastIp == ip;
	},
	
	/**
	 * 判断是否是合法的设备IP，不允许以下非法IP段地址
	 * 0.0.0.0~0.255.255.255(broadcast)
	 * 127.0.0.0~127.255.255.255 (环回地址)
	 * 224.0.0.0~239.255.255.255 (组播地址)
	 * 240.0.0.0~255.255.255.254 (预留地址)
	 * 255.255.255.255
	 * @param {String} ip ip地址
	 * @return {Boolean} 
	 */
	isValidDeviceIp: function(ip) {
		if(!this.isIpAddress(ip)) {
			return false;
		}
		// 广播地址校验
		if(this.isBroadcastIp(ip) || this.isLimitBroadcastIp(ip)) {
			return false;
		}
		// 环回地址校验 
		if(this.isLoopbackAddress(ip)) {
			return false;
		}
		// 组播地址校验 
		if(this.isMulticastAddress(ip)) {
			return false;
		}
		// 预留地址校验 
		if(this.isReserveAddress(ip)) {
			return false;
		}
		return true;
	},
	
	/**
	* 判断是否是合法的IP地址段
	* @param {String} ip ip地址段
	* @return {Boolean} 
	*/
	isIpSegment: function(ipSegment){
		//IPv4支持掩码格式/星号/中划线，IPv6支持掩码格式
		if(ipSegment.indexOf('\*')!=-1){
			//这是星号代表网段的IPv4网段
			if(ipSegment.indexOf('\*')!==ipSegment.lastIndexOf('\*')){
	    		return false;
	    	}
	    	var ipSegments = ipSegment.split('\.'),
	        	ipLength = ipSegments.length;
	    	if(ipLength!==4){
	            return false;
	        }
	    	var twoFiveFourReg = /^(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/,
		        result = true;
		    for(var i=0; i<4; i++){
		        if(twoFiveFourReg.test(ipSegments[i]) || ipSegments[i]==='\*'){
		            continue;
		        }else{
		            result = false;
		            break;
		        }
		    }
		    return result;
		}else if(ipSegment.indexOf('-')!=-1){
			//这是以-分隔的IPv4网段
	        if(ipSegment.indexOf('-')!==ipSegment.lastIndexOf('-')){
	            return false;
	        }
	        var ipSegments = ipSegment.split('\.'),
	            ipLength = ipSegments.length;
	        if(ipLength!==4){
	            return false;
	        }
	        var twoFiveFourReg = /^(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/,
	            rungReg = /^(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)-(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/,
	            result = true;
	        for(var i=0; i<4; i++){
	            if(twoFiveFourReg.test(ipSegments[i])){
	                continue;
	            }else  if(rungReg.test(ipSegments[i])){
	                var startNum = ipSegments[i].split('-')[0],
	                    endNum = ipSegments[i].split('-')[1];
	                startNum = parseInt(startNum);
	                endNum = parseInt(endNum);
	                if(startNum>=endNum){
	                    result = false;
	                    break;
	                }
	            }else{
	                result = false;
	                break;
	            }
	        }
	        return result;
		}else if(ipSegment.indexOf('/')!=-1){
			//这是以/隔开的掩码形式网段，可支持V4，也支持V6
			if(ipSegment.indexOf('/')!==ipSegment.lastIndexOf('/')){
	            return false;
	        }
			var ip = ipSegment.split('/')[0],
				ipMask = ipSegment.split('/')[1];
			
			return (this.isIpv4Address(ip) && /3[0-1]|2[4-9]/.test(ipMask) ) || (this.isIpv6Address(ip) && /12[0-8]/.test(ipMask));
			
		}else{
			//判断是否是合法IP
			return this.isIpAddress(ipSegment);
		}
	},
	
	/**
	* 将IPv6地址转换成简写格式
	* @param {String} ip ip地址段
	* @return {Boolean} 
	*/
	convertToDisplayFormat: function(ip){
		if(this.isIpv6Address(ip)){
			var v6Parts = ip.split(':');
			//先将每一段都尝试进行缩写，去掉前面的0
			for(var i=0; i<v6Parts.length; i++){
				while(v6Parts[i].indexOf('0')==0){
					v6Parts[i] = v6Parts[i].substring(1);
				}
			}
			var tempStr = v6Parts.join(':');
			//找到所有的长于2个的:，第一个以::取代，后面的全部中间插入0
			var hasDoubleColons = false;
			return tempStr.replace(/:{2,}/g, function(match){
				if(!hasDoubleColons){
					hasDoubleColons = true;
					return '::';
				}
				//后续的，中间插入0
				return match.split('').join('0');
				
			})
		}
		return ip;
	},
	
	/**
	* 将包含在()中的IP地址转成简写格式
	* @param {String} ip ip地址段
	* @return {Boolean} 
	*/
	convertIpInBrackets: function(str){
		var me = this;
		if(str.indexOf('(')!=-1){
			return str.replace(/\(\S+\)/g, function(match){
				var ip = match.substring(1, match.length-1);
				return '(' + me.convertToDisplayFormat(ip) + ')';
			});
		} else {
			return me.convertToDisplayFormat(str);
		}
	},
	
	/**
	* 将以IP开头，后面用() []附带信息的字符串，中间的IP地址进行转换
	* @param {String} ip ip地址段
	* @return {Boolean} 
	*/
	convertIpWithAddition: function(ipStr){
		var splitIndex = ipStr.indexOf('(');
		if(splitIndex==-1){
			splitIndex = ipStr.indexOf('[');
		}
		if(splitIndex == -1){
			return this.convertToDisplayFormat(ipStr);
		}else{
			var ip = ipStr.substring(0, splitIndex),
				addition = ipStr.substring(splitIndex);
			return this.convertToDisplayFormat(ip) + addition;
		}
	},
	
	isMaskAddress: function(maskStr) {
		return this.maskReg.test(maskStr);
	}
};