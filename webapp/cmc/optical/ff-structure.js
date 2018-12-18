/**
 * @file 8800F 光机结构
 * @author fanzidong
 * Copyright 2011 Topvision All rights reserved.
 */

var ffStructure = {
    "structure": [{
        "name": "@text.baseInfo@",
        "items": [{
            "fieldLabel": "@CMC.optical.type@",
            "name": "topCcmtsSysDorType",
            "type": "text",
            "disabled": true
        }, {
            "fieldLabel": "@opt.optNodeTemp@",
            "name": "optNodeTempStr",
            "type": "text",
            "disabled": true
        }, {
            "fieldLabel": "@opt.platformSerialNumber@(SN)",
            "name": "platSN",
            "type": "text",
            "disabled": true
        }] 
    }, {
        "name": "@opt.forwardReceiver@",
        "items": [{
            "fieldLabel": "@opt.outputControl@",
            "name": "outputControl",
            "type": "radiogroup",
            "items": [{
                "fieldLabel": "ON",
                "value": 2
            }, {
                "fieldLabel": "OFF",
                "value": 1
            }],
            "disabled": false
        }, {
            "fieldLabel": "@opt.frxNum@",
            "name": "frxNum",
            "type": "text",
            "disabled": true
        }, {
            "fieldLabel": "@opt.inputPower@(dBm)",
            "name": "inputPowerStr",
            "type": "text",
            "disabled": true
        }, {
            "fieldLabel": "@opt.configurationAGCRg@(dBm)",
            "name": "configurationAGCRgStr",
            "type": "text",
            "disabled": true
        },{
            "fieldLabel": "@opt.configurationOutputRFlevelatt@(dB)",
            "name": "configurationOutputRFlevelatt",
            "type": "text",
            "disabled": false,
            "tooltip": "@opt.limit10Integer@",
            "validate": function(value) {
            	return /^\d$|^10$/.test(value);
            }
        }]
    }, {
        "name": "@opt.forwardTransmission@",
        "items": [/*, {
            "fieldLabel": "正向频道数量",
            "name": "channelNum",
            "type": "text",
            "disabled": true
        }*/{
            "fieldLabel": "@opt.fwdATT0@(dB)",
            "name": "fwdATT0",
            "type": "text",
            "disabled": false,
            "tooltip": "@opt.limit10Integer@",
            "validate": function(value) {
            	return /^\d$|^10$/.test(value);
            }
        }, {
            "fieldLabel": "@opt.fwdAtts1234@(dB)",
            "name": "fwdAttsStr",
            "type": "text",
            "disabled": false,
            "tooltip": "@opt.020Integer@",
            "validate": function(value) {
            	//首先确保有3个/
            	var firstSlashIndex = value.indexOf('/');
            	
            	if(firstSlashIndex !== -1 && value.split('/').length === 4) {
            		var valueArray = value.split('/');
            		//确保每一项都是0-20的整数或者-
            		var result = true;
            		for(var i=0, len=valueArray.length; i<len; i++) {
            			var curValue = valueArray[i];
            			if(/^0$|^[1-9]\d{0,1}$/.test(curValue)) {
                    		//已确保是0-99的数字
                    		var number = parseInt(curValue, 10);
                    		if(number > 20) {
                    			result = false;
                    			break;
                    		}
                    	} else if (curValue !== '-') {
                    		result = false;
                    		break;
                    	}
            		}
            		return result;
            	} else {
            		return false;
            	}
            }
        }, {
            "fieldLabel": "@opt.fwdEQ0@(dB)",
            "name": "fwdEQ0",
            "type": "text",
            "disabled": false,
            "tooltip": "@opt.limit20Integer@",
            "validate": function(value) {
            	return /^\d$|^1\d$|^20$/.test(value);
            }
        }, {
            "fieldLabel": "@opt.fwdEqs1234@(dB)",
            "name": "fwdEqsStr",
            "type": "text",
            "disabled": false,
            "tooltip": "@opt.020Integer@",
            "validate": function(value) {
            	//首先确保有3个/
            	var firstSlashIndex = value.indexOf('/');
            	
            	if(firstSlashIndex !== -1 && value.split('/').length === 4) {
            		var valueArray = value.split('/');
            		//确保每一项都是0-20的整数或者-
            		var result = true;
            		for(var i=0, len=valueArray.length; i<len; i++) {
            			var curValue = valueArray[i];
            			if(/^0$|^[1-9]\d{0,1}$/.test(curValue)) {
                    		//已确保是0-99的数字
                    		var number = parseInt(curValue, 10);
                    		if(number > 20) {
                    			result = false;
                    			break;
                    		}
                    	} else if (curValue !== '-') {
                    		result = false;
                    		break;
                    	}
            		}
            		return result;
            	} else {
            		return false;
            	}
            }
        }, {
            "fieldLabel": "@opt.rfPorts@(dBuV)",
            "name": "rfPortsStr",
            "type": "text",
            "disabled": true
        }]
    }, {
        "name": "@opt.reverse@",
        "items": [{
            "fieldLabel": "@opt.rtxState@",
            "name": "rtxState",
            "type": "radiogroup",
            "items": [{
                "fieldLabel": "ON",
                "value": 2
            }, {
                "fieldLabel": "OFF",
                "value": 1
            }],
            "disabled": false
        }, {
            "fieldLabel": "@opt.rtxLaserCurrent@(mA)",
            "name": "rtxLaserCurrent",
            "type": "text",
            "disabled": true
        }, {
            "fieldLabel": "@opt.rtxLaserPower@(dBm)",
            "name": "rtxLaserPowerStr",
            "type": "text",
            "disabled": true
        }, {
            "fieldLabel": "@opt.revEQ@(dB)",
            "name": "revEQ",
            "type": "text",
            "disabled": false,
            "tooltip": "@opt.limit20Integer@",
            "validate": function(value) {
            	if(/^0$|^[1-9]\d{0,1}$/.test(value)) {
            		//已确保是0-99的数字
            		var number = parseInt(value, 10);
            		return number <= 20;
            	}else {
            		return false;
            	}
            }
        }, {
            "fieldLabel": "@opt.revAtts1234@(dB)",
            "name": "revAttsStr",
            "type": "text",
            "disabled": false,
            "tooltip": "@opt.030Integer40Off@",
            "validate": function(value) {
            	//首先确保有3个/
            	var firstSlashIndex = value.indexOf('/');
            	
            	if(firstSlashIndex !== -1 && value.split('/').length === 4) {
            		var valueArray = value.split('/');
            		//确保每一项都是0-30的整数或者-
            		var result = true;
            		for(var i=0, len=valueArray.length; i<len; i++) {
            			var curValue = valueArray[i];
            			if(/^0$|^[1-9]\d{0,1}$/.test(curValue)) {
                    		//已确保是0-99的数字
                    		var number = parseInt(curValue, 10);
                    		if(number > 30 && number !== 40) {
                    			result = false;
                    			break;
                    		}
                    	} else if (curValue !== '-') {
                    		result = false;
                    		break;
                    	}
            		}
            		return result;
            	} else {
            		return false;
            	}
            }
        }, {
            "fieldLabel": "@opt.revATTUS@(dB)",
            "name": "revATTUS",
            "type": "text",
            "disabled": false,
            "tooltip": "@opt.limit30Integer@",
            "validate": function(value) {
            	if(/^0$|^[1-9]\d{0,1}$/.test(value)) {
            		//已确保是0-99的数字
            		var number = parseInt(value, 10);
            		return number <= 30;
            	}else {
            		return false;
            	}
            }
        }, {
            "fieldLabel": "@opt.revATTRTX@(dB)",
            "name": "revATTRTX",
            "type": "text",
            "disabled": false,
            "tooltip": "@opt.limit30Integer@",
            "validate": function(value) {
            	if(/^0$|^[1-9]\d{0,1}$/.test(value)) {
            		//已确保是0-99的数字
            		var number = parseInt(value, 10);
            		return number <= 30;
            	}else {
            		return false;
            	}
            }
        }]
    }, {
        "name": "@opt.powerInfo@",
        "items": [{
            "fieldLabel": "@opt.dcPowers@(V)",
            "name": "dcPowersStr",
            "type": "text",
            "disabled": true
        }, {
            "fieldLabel": "@opt.linePowerVoltage1@(V)",
            "name": "linePowerVoltage1Str",
            "type": "text",
            "disabled": true
        }]
    }],
    "operations": [{
    	"id": "load",
    	"position": "left",
    	"support": true
    }, {
    	"id": "save",
    	"position": "left",
    	"support": true
    }, {
    	"id": "update",
    	"position": "right",
    	"support": true
    }, {
    	"id": "reset",
    	"position": "right",
    	"support": true
    }],
    "cover": {
    	"bgName": 'ff.jpg'
    },
    "schematic": {
    	"bgName": 'ff.jpg'
    }
};