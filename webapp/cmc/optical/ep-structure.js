/**
 * @file EP 光机结构
 * @author fanzidong
 * Copyright 2011 Topvision All rights reserved.
 */

var epStructure = {
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
        "name": "@opt.forwardTransmission@",
        "items": [{
            "fieldLabel": "@opt.ftxOptPower@(dBm)",
            "name": "ftxOptPowerStr",
            "type": "text",
            "disabled": true
        }, {
            "fieldLabel": "@opt.ftxLaserCurrent@(mA)",
            "name": "ftxLaserCurrent",
            "type": "text",
            "disabled": true
        }]
    }, {
        "name": "@opt.reverseReceiver@",
        "items": [{
            "fieldLabel": "@opt.rrxOptPows1234@(dBm)",
            "name": "rrxOptPowsStr",
            "type": "text",
            "disabled": true
        }]
    }, {
        "name": "@opt.powerInfo@",
        "items": [{
            "fieldLabel": "@opt.dcPower12V@(V)",
            "name": "dcPowersStr",
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
    	"bgName": 'ep.jpg'
    },
    "schematic": {
    	"bgName": 'ep.jpg'
    }
};