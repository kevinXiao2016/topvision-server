var nm3kBatchData = {
	olt_service : {
		name : "@Performance.service@"  //服务质量;
	},
	olt_flow : {
		name : "@Performance.olt_flow@" //速率
	},
	olt_deviceStatus : {
		name : "@Performance.olt_deviceStatus@" //设备状态
	},
	cmc_service : {
		name : "@Performance.service@"  //服务质量;
	},
	cmc_flow : {
		name : "@Performance.flow@",   //速率;
		tip : "@Performance.flowTip@"
	},
	cmc_signalQuality : { //信号质量;
		name : "@Performance.signalQuality@"
	},
	cmc_businessQuality : {  //业务质量;
		name : "@Performance.cmc_businessQuality@"
	},
	cmc_deviceStatus : { //设备状态;
		name : "@Performance.deviceStatus@"
	},
	cmts_flow : {
		name : "@Performance.flow@" //速率;
	},
	cmts_service : {
		name : "@Performance.service@" //服务质量;
	},
	cmts_signalQuality : {
		name : "@Performance.signalQuality@" //信号质量;
	},
	cmts_deviceStatus : {
		name : "@Performance.deviceStatus@" //设备状态;
	},
	olt_cpuUsed : {
		name : "@Performance.cpuUsed@", //CPU利用率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	olt_memUsed : {
		name : "@Performance.memUsed@", //内存利用率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	olt_flashUsed : {
		name : "@Performance.olt_flashUsed@",  //Flash利用率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	olt_boardTemp : {
		name : "@Performance.boardTemp@", //板卡温度;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal		
	},
	olt_fanSpeed : {
		name : "@Performance.fanSpeed@",  //风扇转速;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	olt_optLink : {
		name : "@Performance.optLink@",  //光链路信息;
		tip : "@tip.optLinkTip_olt@",
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	olt_ponFlow : {
		name : "@Performance.ponFlow@",  //PON口速率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	olt_sniFlow : {
		name : "@Performance.sniFlow@",  //SNI口速率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	olt_onlineStatus : {
		name : "@Performance.delay@",
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	/*olt_subEquPolling : {
		name : "@Performance.olt_subEquPolling@",
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},*/
	cmc_cpuUsed : {
		name : "@Performance.cpuUsed@", //CPU利用率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_flashUsed : {
		name : "@Performance.cmc_flashUsed@", //Flash利用率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_memUsed : {
		name : "@Performance.cmc_memUsed@",  //内存利用率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_moduleTemp : {
		name : "@Performance.cmc_moduleTemp@",  //模块温度;
		tip : "@tip.moduleTempTip@",
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_optLink : {
		name : "@Performance.cmc_optLink@", //光链路信息;
		tip : "@tip.optLinkTip@",
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_upLinkFlow : {
		name : "@Performance.upLinkFlow@", //上联口速率;
		toolTip : "@Tip.collectTimeRule2@",
		reg : testBatchVal
	},
	cmc_channelSpeed : {
		name : "@Performance.cmc_channelSpeed@", //信道速率;
		toolTip : "@Tip.collectTimeRule2@",
		reg : testBatchVal
	},
	cmc_macFlow : {
		name : "@Performance.macFlow@", //MAC域速率;
		toolTip : "@Tip.collectTimeRule2@",
		reg : testBatchVal
	},
	cmc_ber : {
		name : "@Performance.ber@", //误码率;
		tip : "@tip.berTip@",
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_snr : {
		name : "@Performance.ccNoise@",//信噪比;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_cmflap : {
		name : "FLAP",
		tip : "@tip.cmFlap@",
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_opticalReceiver : {
		name : "@Performance.opticalReceiver@",  //光机接收功率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_dorOptTemp : {
		name : "@Performance.dorTemp@",  //光机温度;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_dorLinePower : {
		name : "@Performance.dorVoltage@",  //光机供电电压;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmc_onlineStatus : {
		name : "@Performance.delay@", //响应延迟;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmts_snr : {
		name : "@Performance.snr@", //信噪比;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmts_ber : {
		name : "@Performance.ber@",  //误码率;
		tip : "@tip.berTip@",
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmts_upLinkFlow : {
		name : "@Performance.upLinkFlow@", //上联口速率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmts_channelSpeed :{
		name : "@Performance.channelSpeed@",  //信道速率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	cmts_onlineStatus :{
		name : "@Performance.delay@", //响应延迟;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	onu_deviceStatus : {
		name : "@Performance.deviceStatus@" //设备状态;
	},
	onu_service : {
		name : "@Performance.service@" //服务质量;
	},
	onu_onlineStatus : {
		name : "@Performance.onu_onlineStatus@",
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	onu_optLink : {
		name : "@Performance.optLink@",  //光链路信息;
		tip : "@Performance.onuLinkTip@",
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	onu_flow : {
		name : "@Performance.olt_flow@" //速率
	},
	onu_portFlow : {
		name : "@Performance.onu_portFlow@",  //端口速率;
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	},
	onuCatv : {
		name : "@Performance.onuCatv@" //catv
	},
	onuCatvInfo : {
		name : "@Performance.onuCatvInfo@",  //catv
		toolTip : "@Tip.collectTimeRule@",
		reg : testBatchVal
	}
};

//是否是1-1440之间的正整数;
function testBatchVal(){
	var value = arguments[0];
	var reg = /^\d{1,4}$/;
	var result = true;
	if(reg.test(value) && value>=1 && value<=1440){
	
	}else{
		$(this).focus();
		result = false;
	}
	return result;
}