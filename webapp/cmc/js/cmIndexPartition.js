(function($){
	var defaults = {
		width: 600,
		upSnrMin: '',
		upSnrMax: '',
		downSnrMin: '',
		downSnrMax: '',
		upPowerMin: '',
		upPowerMax: '',
		downPowerMin: '',
		downPowerMax: ''
	},
	templateStr = '@CM.partition.upSnr@(dB) [<span style="color:#B3711A;margin:0 5px;">{0}</span>]'
		+'<span style="margin:0 5px;">/</span>@CM.partition.downSnr@(dB) [<span style="color:#B3711A;margin:0 5px;">{1}</span>]'
		+'<span style="margin:0 5px;">/</span>@CM.partition.upPower@(@{unitConfigConstant.elecLevelUnit}@) [<span style="color:#B3711A;margin:0 5px;">{2}</span>]'
		+'<span style="margin:0 5px;">/</span>@CM.partition.downPower@(@{unitConfigConstant.elecLevelUnit}@) [<span style="color:#B3711A;margin:0 5px;">{3}</span>]';
	
	$.fn.cmIndexPartition = function(opt){
		var me = this;
		opt = $.extend(defaults, opt);
		var contentStr = String.format(templateStr, '@CM.all@', '@CM.all@', '@CM.all@', '@CM.all@');
		$(me).attr('class', 'Nm3kSelect')
			 .append('<p class="contentP" style="padding-left:5px;">'+contentStr+'</p>')
			 .append('<a class="Nm3kSelectArr" href="javascript:;"></a>');
		return this;
	}
	
	$.fn.cmIndexPartition.change = function(opt){
		var me = this;
		var upSnrStr = getRangeStr(opt.upSnrMin, opt.upSnrMax),
			downSnrStr = getRangeStr(opt.downSnrMin, opt.downSnrMax),
			upPowerStr = getRangeStr(opt.upPowerMin, opt.upPowerMax),
			downPowerStr = getRangeStr(opt.downPowerMin, opt.downPowerMax),
			contentStr = String.format(templateStr, upSnrStr, downSnrStr, upPowerStr, downPowerStr);
		$(me).find('.contentP').empty().append(contentStr);
		
		function getRangeStr(min, max){
			if((min===null || min==='') && (max===null || max==='')) return '@CM.all@';
			if(min===null || min==='') return '@COMMON.lessthan@' + max;
			if(max===null || max==='') return '@COMMON.morethan@' + min;
			return min + '@COMMON.to@' + max;
		}
	}
})(jQuery);