/**
 * @file CFC 光机结构
 * @author fanzidong
 * Copyright 2011 Topvision All rights reserved.
 */

var efStructure = {
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
    	"support": false
    }, {
    	"id": "update",
    	"position": "right",
    	"support": false
    }, {
    	"id": "reset",
    	"position": "right",
    	"support": false
    }],
    "cover": {
    	"bgName": 'ef.jpg'
    },
    "schematic": {
    	"bgName": 'ef.jpg'
    }
};