var vcEntityKey = 'entityId';
$(function() {
	// 找到所有有vcid属性的标签，判断其版本支持情况
	$('[vcid]').each(function(index, e) {
		var $e = $(e);
		var vcId = $e.attr('vcid');
		
		var support = true;
		
		// 可能需要支持多个功能才能显示，多功能以“,”隔开
		// 也支持多个功能支持一个即可的状态，但是同时只支持一种情况
		if(vcId.indexOf(',') !=-1) {
			// 都需支持
			var vcIds = vcId.split(',');
			
			for(var i=0; i<vcIds.length; i++) {
				var supportInfo = top.VersionControl.support(vcIds[i], window[vcEntityKey]);
				if(!supportInfo) {
					support = false;
					break;
				}
			}
		} else if(vcId.indexOf('|') !=-1) {
			// 一个即可
			var vcIds = vcId.split('|');
			
			var notSupportCount = 0;
			
			for(var i=0; i<vcIds.length; i++) {
				var supportInfo = top.VersionControl.support(vcIds[i], window[vcEntityKey]);
				if(!supportInfo) {
					notSupportCount++;
				} else {
					break;
				}
			}
			
			if(notSupportCount == vcIds.length) {
				support = false;
			}
		} else {
			// 单个情况
			support = top.VersionControl.support(vcId, window[vcEntityKey]);
		}
		
		support ? $e.show() : $e.hide();
	});
	
	// 找到所有有dynamic-vcid属性的标签，判断其版本支持情况
	var supportMap = {};
	$('[dynamic-vcid]').each(function(index, e) {
		var $e = $(e);
		var vcId = $e.attr('dynamic-vcid');
		
		var support = false;
		if(supportMap[vcId] !== undefined) {
			support = supportMap[vcId];
		} else {
			support = top.VersionControl.supportDependsOnSub(window[vcEntityKey], vcId);
			supportMap[vcId] = support;
		}
		
		support ? $e.show() : $e.hide();
	});
})