(function($){
	var defaults = {
		width: 500,
		receiveMin:  '',
		receiveMax:  '',
	    transmitMin: '',
	    transmitMax: '',
	    ponRecvMin:  '',
	    ponRecvMax:  ''
	},
	templateStr = '@ONU.opticalRecv@(@{unitConfigConstant.elecLevelUnit}@) [<span style="color:#B3711A;margin:0 5px;">{0}</span>]'
		+'<span style="margin:0 5px;">/</span>@ONU.opticalSend@(@{unitConfigConstant.elecLevelUnit}@) [<span style="color:#B3711A;margin:0 5px;">{1}</span>]'
		+'<span style="margin:0 5px;">/</span>@ONU.ponRecvPwr@(@{unitConfigConstant.elecLevelUnit}@) [<span style="color:#B3711A;margin:0 5px;">{2}</span>]';
	
	$.fn.onuIndexPartition = function(opt){
		var me = this;
		opt = $.extend(defaults, opt);
		var contentStr = String.format(templateStr, '@COMMON.all@', '@COMMON.all@', '@COMMON.all@', '@COMMON.all@');
		$(me).attr('class', 'Nm3kSelect')
			 .append('<p class="contentP" style="padding-left:5px;">'+contentStr+'</p>')
			 .append('<a class="Nm3kSelectArr" href="javascript:;"></a>');
		return this;
	}
	
	$.fn.onuIndexPartition.change = function(opt){
		var me = this;
		var receiveFra = getRangeStr(opt.receiveMin, opt.receiveMax),
			transmitFra = getRangeStr(opt.transmitMin, opt.transmitMax),
			ponRecvFra = getRangeStr(opt.ponRecvMin, opt.ponRecvMax),
			contentStr = String.format(templateStr, receiveFra, transmitFra, ponRecvFra);
		$(me).find('.contentP').empty().append(contentStr);
		
		function getRangeStr(min, max){
			if((min===null || min==='') && (max===null || max==='')) return '@COMMON.all@';
			if(min===null || min==='') return '@COMMON.lessthan@' + max;
			if(max===null || max==='') return '@COMMON.morethan@' + min;
			return min + '@COMMON.to@' + max;
		}
	}
})(jQuery);