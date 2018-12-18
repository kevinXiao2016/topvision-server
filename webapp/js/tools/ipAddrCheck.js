/**
 *检查IP地址
 *
 */
(function (window){
    //检验IP是否格式正确
    function _checkedIpValue(ip){
        if(!ip || ip.toString().indexOf(".") == -1){
            return false;
        }
        ip = ip.split(".");
        if(ip.length != 4){
            return false;
        }else{
            for(var k=0; k<4; k++){
                if(ip[k].length==0 || isNaN(ip[k]) || ip[k].length>3 || parseInt(ip[k])<0 || parseInt(ip[k])>255){
                    return false
                }
            }
        }
        return true;
    }
	function IpAddrCheck(ip, mask){
		this.ip = ip;
		this.mask = mask? mask: "255.255.255.0";
	}
	IpAddrCheck.ALLMASK = [255,255,255,255];
	/**
	 * 获取子网IP
	 */
	IpAddrCheck.prototype.getSubNetIp = function (){
		var ipArray;
		var maskArray;
		var subNet = new Array(4);
		if(this.ip && this.mask){
			ipArray = this.ip.split(".");
			maskArray = this.mask.split(".");
			if(ipArray.length == 4 && maskArray.length == 4){
				for(var i = 0; i < ipArray.length; i++){
					subNet[i] = parseInt(ipArray[i]) & parseInt(maskArray[i]);
				}
			}
		}
		return subNet.join(".");
	}
	
	IpAddrCheck.prototype.getHostIp = function (){
	    var ipArray;
        var maskArray;
        var host = new Array(4);
        if(this.ip && this.mask){
            ipArray = this.ip.split(".");
            maskArray = this.mask.split(".");
            if(ipArray.length == 4 && maskArray.length == 4){
                for(var i = 0; i < ipArray.length; i++){
                    host[i] = parseInt(ipArray[i]) & (~ parseInt(maskArray[i]));
                }
            }
        }
        return host.join(".");
	}
	
	/**
	 * 检查是否为环回地址
	 */
	IpAddrCheck.prototype.isLoopBackIp = function (){
		if(this.ip){
			var ipArray = this.ip.split(".");
		    if(parseInt(ipArray[0]) == 127){
		        return true;
		    }else{
		    	return false;
		    }
		}else{
			return false;
		}
	}
	/**
	 * 检查是否为组播地址
	 */
	IpAddrCheck.prototype.isMulticastIp = function (){
		if(this.ip){
	        var ipArray = this.ip.split(".");
	        if(parseInt(ipArray[0]) >= 224 && parseInt(ipArray[0]) <= 239){
	            return true;
	        }else{
	            return false;
	        }
	    }else{
	        return false;
	    }   
	}
	/**
	 * 检查与ip,mask指定的子网是否冲突
	 */
	IpAddrCheck.prototype.isSubnetConflict = function (ip, mask){
		if(this.ip && ip){
	        var sub1Mask1 = new IpAddrCheck(this.ip, this.mask);
	        var sub2Mask1 = new IpAddrCheck(ip, this.mask);
	        var sub1Mask2 = new IpAddrCheck(this.ip, mask);
	        var sub2Mask2 = new IpAddrCheck(ip, mask);
	        if(sub1Mask1.getSubNetIp() == sub2Mask1.getSubNetIp() || 
	        		sub1Mask2.getSubNetIp() == sub2Mask2.getSubNetIp()){
	        	return true;
	        }
	    }
	    return false;
	}
	
	IpAddrCheck.prototype.isGateway = function (gateway){
	    if(this.ip == gateway){
	        return false;
	    }
	    var subNet = this.getSubNetIp();
	    var gatewayCheck = new IpAddrCheck(gateway, this.mask);
	    if(!gatewayCheck.isHostIp()){
	        return false;
	    }
	    var gatewayNet = gatewayCheck.getSubNetIp();
	    return subNet == gatewayNet;
	}
	
	/**
	 * 检查是否为广播IP地址，当未设置掩码时，以255.255.255.0作为默认掩码
	 */
	IpAddrCheck.prototype.isBroadcastIp = function (){
		var ipArr = this.ip.split(".");
		var maskArr = [255,255,255,0];
		var allMask  = IpAddrCheck.ALLMASK;
		if(this.mask){
			maskArr = this.mask.split(".");
		}
		for(var i = 0; i < ipArr.length; i++){
			var hostMask = parseInt(maskArr[i]) ^ parseInt(allMask[i]);			
			if(hostMask != (hostMask & parseInt(ipArr[i]))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 监测IP地址格式
	 */
	IpAddrCheck.prototype.checkIp = function (){
	    if(!this.ip || this.ip.toString().indexOf(".") == -1){
	        return false;
	    }
	    var ipSplit = this.ip.split(".");
	    if(ipSplit.length != 4){
	        return false;
	    }else{
	        for(var k=0; k<4; k++){
	            if(ipSplit[k].length==0 || isNaN(ipSplit[k]) || ipSplit[k].length>3 || parseInt(ipSplit[k])<0 || parseInt(ipSplit[k])>255){
	                return false
	            }
	        }
	    }
        return true;
    }
	
	/**
	 * ABC类IP地址检查
	 */
	IpAddrCheck.prototype.isNormalTypeIP = function (){
	    var ipSplit = this.ip.split(".");
	    if(parseInt(ipSplit[0]) < 1 ||  parseInt(ipSplit[0]) > 223 || parseInt(ipSplit[0])==127){
            return false;
        }
	    return true;
	}
	
	IpAddrCheck.prototype.checkMask = function (){
	    var ip = this.mask;
	    if(_checkedIpValue(ip)){
	        ip = ip.split(".");
	        var ip_binary = (parseInt(ip[0]) + 256).toString(2).substring(1) + (parseInt(ip[1]) + 256).toString(2).substring(1)
	                        + (parseInt(ip[2]) + 256).toString(2).substring(1) + (parseInt(ip[3]) + 256).toString(2).substring(1);
	        if (ip_binary.indexOf("01") == -1){
	            return true;
	        }
	    }
	    return false;
	}
	
	IpAddrCheck.prototype.isHostIp = function (){
		if(!this.checkIp() || !this.isNormalTypeIP()){
		    return false;
		}
		if(!this.checkMask()){
		    return false;
		}
		var subNet = this.getSubNetIp();
		
		var host = this.getHostIp();
		if(subNet == "0.0.0.0" || host == "0.0.0.0"){
		    return false;
		}
		return true;
	}
	
	window.IpAddrCheck = IpAddrCheck;
})(window);