var NetSegment = function(options){
	this.id = options.id || -1;
	this.name = options.name || '';
	this.ipInfo = options.ipInfo || '';
	this.folderId = options.folderId || -1;
	this.folderName = options.folderName || '';
	this.lastDiscoveryTime = options.lastDiscoveryTime || '';
	this.autoDiscovery = options.autoDiscovery || 1;
    this.action = options.action || 'add';
}

/**
 * 进行IP网段的校验
 * 我们支持以下格式：合法的单个IP(172.17.2.1)； 带掩码的IP(172.17.2.1/24)； 单端以-分隔的形式(172.17.2.1-254)
 */
NetSegment.prototype.ipInfoValidate = function(){
	var ipInfo = this.ipInfo;
    if(ipInfo.indexOf('/')!=-1){
        var ipMaskReg = /^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))\/(?:3[0-1]|2[4-9])$/;
        return ipMaskReg.test(ipInfo);
    }else if(ipInfo.indexOf('-')!=-1){
        //这是以-分隔的形式
        if(ipInfo.indexOf('-')!==ipInfo.lastIndexOf('-')){
            return false;
        }
        var ipSegments = ipInfo.split('\.'),
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
    }else if(ipInfo.indexOf('\*')!=-1){
    	if(ipInfo.indexOf('\*')!==ipInfo.lastIndexOf('\*')){
    		return false;
    	}
    	var ipSegments = ipInfo.split('\.'),
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
    }
    return isValidIp(ipInfo);
    
    function isValidIp(ip){
    	var ipReg = /^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))$/;
        return ipReg.test(ip);
    }
}

/**
 * 网段名称校验
 */
NetSegment.prototype.nameValidate = function(){
    //var nameReg = /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:]{1,63}$/;
    //return nameReg.test(this.name);
	return Validator.isAnotherName(this.name);
}

NetSegment.prototype.save = function(callback){
    if(!this.ipInfoValidate() || !this.nameValidate()){
        return false;
    }
    //通过校验，开始下发保存请求
    var saveData = {
        id: this.id,
        name: this.name,
        ipInfo: this.ipInfo,
        folderId: this.folderId,
        autoDiscovery: this.autoDiscovery
    };
    var url = this.action==='add' ? '/batchautodiscovery/addNetSegment.tv' :  '/batchautodiscovery/modifyNetSegment.tv';
    var savePromise = $.post(url, saveData);
    savePromise.done(function(){
        callback(null);
    });
    savePromise.fail(function(){
        var err = '@batchtopo.operationfailed@';
        callback(err);
    });
}