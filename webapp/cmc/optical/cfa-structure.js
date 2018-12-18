/**
 * @file CFA 光机结构
 * @author fanzidong
 * Copyright 2011 Topvision All rights reserved.
 */

var cfaStructure = {
    "structure": [{
        "name": "@text.baseInfo@",
        "items": [{
            "fieldLabel": "@CMC.optical.type@",
            "name": "topCcmtsSysDorType",
            "type": "text",
            "disabled": true
        }, {
            "fieldLabel": "@opt.platformSerialNumber@",
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
            "fieldLabel": "@opt.inputPower@(dBm)",
            "name": "inputPowerStr",
            "type": "text",
            "disabled": true
        }, {
            "fieldLabel": "@opt.configurationAGCRg@(dBm)",
            "name": "configurationAGCRgStr",
            "type": "text",
            "disabled": true
        } ,{
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
    	"support": false
    }, {
    	"id": "reset",
    	"position": "right",
    	"support": false
    }]
};