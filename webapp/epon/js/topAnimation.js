(function($, undefined) {

	/** *************************************************************************** */
	/**
	 * **************************** COLOR ANIMATIONS
	 * *****************************
	 */
	/** *************************************************************************** */
	$.each([ 'backgroundColor', 'borderBottomColor', 'borderLeftColor',
			'borderRightColor', 'borderTopColor', 'borderColor', 'color',
			'outlineColor' ], function(i, attr) {
		$.fx.step[attr] = function(fx) {
			if (!fx.colorInit) {
				fx.start = getColor(fx.elem, attr);
				fx.end = getRGB(fx.end);
				fx.colorInit = true;
			}

			fx.elem.style[attr] = 'rgb('
					+ Math.max(Math.min(parseInt(
							(fx.pos * (fx.end[0] - fx.start[0])) + fx.start[0],
							10), 255), 0)
					+ ','
					+ Math.max(Math.min(parseInt(
							(fx.pos * (fx.end[1] - fx.start[1])) + fx.start[1],
							10), 255), 0)
					+ ','
					+ Math.max(Math.min(parseInt(
							(fx.pos * (fx.end[2] - fx.start[2])) + fx.start[2],
							10), 255), 0) + ')';
		};
	});

	/** *************************************************************************** */
	/**
	 * **************************** SHINNING ANIMATIONS
	 * **************************
	 */
	/** *************************************************************************** */
	$.fn.extend({
		jShine : function(options) {
			var defaults = {
				sec : 3,
				speed : 500
			};
			var o = $.extend(defaults, options);
			this.each(function() {
				shinning($(this), o.sec, o.speed);
			});
		},
		jShineColor : function(options) {
			var defaults = {
				sec : 3,
				speed : 500,
				shColor : '#FF0000',
				fadeColor : '#ffff00',
				fontColor : 'yellow'
			};
			var o = $.extend(defaults, options);
			var style = this.style;
			var originStyle = {
				backgroundColor : $(this).css('backgroundColor'),
				color : $(this).css('color') ? $(this).css('color') : '#000000'
			};
			this.each(function() {
				shinColor($(this), o.sec, o.speed, o.shColor, o.fadeColor,
						o.fontColor, originStyle);
			});
		},
		jShineBorder : function(options) {
			var defaults = {
				sec : 10,
				speed : 500,
				shineColor : '#ff0000',
				fadeColor : '#ffff00'
			};
			var originStyle = {
				border : $(this).css("border") ? $(this).css("border")
						: '0px solid #ffffff'
			};
			var opt = $.extend(defaults, options);

			this.each(function() {
				shineBorder($(this), opt, originStyle);
			});
		}
	});
	function shinning(o, sec, speed, count) {
		speed = (!speed) ? 500 : speed == 'fast' ? 100 : speed == 'slow' ? 1000
				: isNaN(speed) ? 500 : speed;
		count = (count == undefined || count == null || count == 0) ? 0 : count;
		sec = (sec == undefined || sec == null || sec == 0) ? 3
				: sec > 100 ? count + 1 : sec;
		if (count < sec) {
			$(o).animate({
				opacity : 0
			}, {
				duration : speed,
				complete : function() {
					$(o).animate({
						opacity : 1
					}, {
						duration : speed,
						complete : function() {
							shinning(o, sec, speed, ++count);
						}
					});
				}
			});
		}
	}
	function shinColor(o, sec, speed, shColor, fadeColor, fontColor,
			originStyle, count) {
		speed = (!speed) ? 500 : speed == 'fast' ? 100 : speed == 'slow' ? 1000
				: isNaN(speed) ? 500 : speed;
		count = (count == undefined || count == null || count == 0) ? 0 : count;
		sec = (sec == undefined || sec == null || sec == 0) ? 3
				: sec > 100 ? count + 1 : sec;
		if (count + 1 < sec) { // to write Back the style next second
			$(o).animate(
					{
						backgroundColor : shColor,
						color : fontColor
					},
					{
						duration : speed,
						complete : function() {
							$(o).animate(
									{
										backgroundColor : fadeColor,
										color : '#ffffff'
									},
									{
										duration : speed,
										complete : function() {
											shinColor(o, sec, speed, shColor,
													fadeColor, fontColor,
													originStyle, ++count);
										}
									});
						}
					});
		} else {
			$(o).animate({
				backgroundColor : originStyle.backgroundColor,
				color : originStyle.color
			}, {
				duration : speed,
				complete : function() {
					$(o).css({
						backgroundColor : originStyle.backgroundColor
					});
				}
			});

		}
	}
	function shineBorder(o, opt, originStyle, count) {
		opt.speed = (!opt.speed) ? 500 : opt.speed == 'fast' ? 100
				: opt.speed == 'slow' ? 1000 : isNaN(opt.speed) ? 500
						: opt.speed;
		count = (count == undefined || count == null || count == 0) ? 0 : count;
		opt.sec = (opt.sec == undefined || opt.sec == null || opt.sec == 0) ? 3
				: opt.sec > 100 ? count + 1 : opt.sec;
		$(o).css({
			border : '1px solid white'
		});
		if (count + 1 < opt.sec) { // to write Back the style next second
			$(o).animate({
				borderColor : opt.shineColor
			}, {
				duration : opt.speed,
				complete : function() {

					$(o).animate({
						borderColor : opt.fadeColor
					}, {
						duration : opt.speed,
						complete : function() {
							shineBorder(o, opt, originStyle, ++count);
						}
					});
				}
			});
		} else {
			try {
				$(o).animate({
					border : originStyle.border
				});
				$(o).css({
					border : originStyle.border
				});
			} catch (e) {
				$(o).css({
					border : originStyle.border
				});
			}
		}
	}
	function getRGB(color) {
		var result;

		// Check if we're already dealing with an array of colors
		if (color && color.constructor == Array && color.length == 3)
			return color;

		// Look for rgb(num,num,num)
		if (result = /rgb\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*\)/
				.exec(color))
			return [ parseInt(result[1], 10), parseInt(result[2], 10),
					parseInt(result[3], 10) ];

		// Look for rgb(num%,num%,num%)
		if (result = /rgb\(\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*\)/
				.exec(color))
			return [ parseFloat(result[1]) * 2.55,
					parseFloat(result[2]) * 2.55, parseFloat(result[3]) * 2.55 ];

		// Look for #a0b1c2
		if (result = /#([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})/
				.exec(color))
			return [ parseInt(result[1], 16), parseInt(result[2], 16),
					parseInt(result[3], 16) ];

		// Look for #fff
		if (result = /#([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])/.exec(color))
			return [ parseInt(result[1] + result[1], 16),
					parseInt(result[2] + result[2], 16),
					parseInt(result[3] + result[3], 16) ];

		// Look for rgba(0, 0, 0, 0) == transparent in Safari 3
		if (result = /rgba\(0, 0, 0, 0\)/.exec(color))
			return colors['transparent'];

		// Otherwise, we're most likely dealing with a named color
		return colors[$.trim(color).toLowerCase()];
	}

	function getColor(elem, attr) {
		var color;
		do {
			color = $.curCSS(elem, attr);

			// Keep going until we find an element that has color, or we hit the
			// body
			if (color != '' && color != 'transparent'
					|| $.nodeName(elem, "body"))
				break;

			attr = "backgroundColor";
		} while (elem = elem.parentNode);

		return getRGB(color);
	}
	;

	// Some named colors to work with
	// From Interface by Stefan Petre
	// http://interface.eyecon.ro/

	var colors = {
		aqua : [ 0, 255, 255 ],
		azure : [ 240, 255, 255 ],
		beige : [ 245, 245, 220 ],
		black : [ 0, 0, 0 ],
		blue : [ 0, 0, 255 ],
		brown : [ 165, 42, 42 ],
		cyan : [ 0, 255, 255 ],
		darkblue : [ 0, 0, 139 ],
		darkcyan : [ 0, 139, 139 ],
		darkgrey : [ 169, 169, 169 ],
		darkgreen : [ 0, 100, 0 ],
		darkkhaki : [ 189, 183, 107 ],
		darkmagenta : [ 139, 0, 139 ],
		darkolivegreen : [ 85, 107, 47 ],
		darkorange : [ 255, 140, 0 ],
		darkorchid : [ 153, 50, 204 ],
		darkred : [ 139, 0, 0 ],
		darksalmon : [ 233, 150, 122 ],
		darkviolet : [ 148, 0, 211 ],
		fuchsia : [ 255, 0, 255 ],
		gold : [ 255, 215, 0 ],
		green : [ 0, 128, 0 ],
		indigo : [ 75, 0, 130 ],
		khaki : [ 240, 230, 140 ],
		lightblue : [ 173, 216, 230 ],
		lightcyan : [ 224, 255, 255 ],
		lightgreen : [ 144, 238, 144 ],
		lightgrey : [ 211, 211, 211 ],
		lightpink : [ 255, 182, 193 ],
		lightyellow : [ 255, 255, 224 ],
		lime : [ 0, 255, 0 ],
		magenta : [ 255, 0, 255 ],
		maroon : [ 128, 0, 0 ],
		navy : [ 0, 0, 128 ],
		olive : [ 128, 128, 0 ],
		orange : [ 255, 165, 0 ],
		pink : [ 255, 192, 203 ],
		purple : [ 128, 0, 128 ],
		violet : [ 128, 0, 128 ],
		red : [ 255, 0, 0 ],
		silver : [ 192, 192, 192 ],
		white : [ 255, 255, 255 ],
		yellow : [ 255, 255, 0 ],
		transparent : [ 255, 255, 255 ]
	};
	/** *************************************************************************** */
	/** ****************************CLASS ANIMATIONS ************************** */
	/** *************************************************************************** */
	var classAnimationActions = [ 'add', 'remove', 'toggle' ], shorthandStyles = {
		border : 1,
		borderBottom : 1,
		borderColor : 1,
		borderLeft : 1,
		borderRight : 1,
		borderTop : 1,
		borderWidth : 1,
		margin : 1,
		padding : 1
	};

	function getElementStyles() {
		var style = document.defaultView ? document.defaultView
				.getComputedStyle(this, null) : this.currentStyle, newStyle = {}, key, camelCase;

		// webkit enumerates style porperties
		if (style && style.length && style[0] && style[style[0]]) {
			var len = style.length;
			while (len--) {
				key = style[len];
				if (typeof style[key] == 'string') {
					camelCase = key.replace(/\-(\w)/g, function(all, letter) {
						return letter.toUpperCase();
					});
					newStyle[camelCase] = style[key];
				}
			}
		} else {
			for (key in style) {
				if (typeof style[key] === 'string') {
					newStyle[key] = style[key];
				}
			}
		}

		return newStyle;
	}

	function filterStyles(styles) {
		var name, value;
		for (name in styles) {
			value = styles[name];
			if (
			// ignore null and undefined values
			value == null ||
			// ignore functions (when does this occur?)
			$.isFunction(value) ||
			// shorthand styles that need to be expanded
			name in shorthandStyles ||
			// ignore scrollbars (break in IE)
			(/scrollbar/).test(name) ||

			// only colors or values that can be converted to numbers
			(!(/color/i).test(name) && isNaN(parseFloat(value)))) {
				delete styles[name];
			}
		}

		return styles;
	}

	function styleDifference(oldStyle, newStyle) {
		var diff = {
			_ : 0
		}, // http://dev.jquery.com/ticket/5459
		name;

		for (name in newStyle) {
			if (oldStyle[name] != newStyle[name]) {
				diff[name] = newStyle[name];
			}
		}

		return diff;
	}

	$.animateClass = function(value, duration, easing, callback) {
		if ($.isFunction(easing)) {
			callback = easing;
			easing = null;
		}

		return this
				.queue(function() {
					var that = $(this), originalStyleAttr = that.attr('style')
							|| ' ', originalStyle = filterStyles(getElementStyles
							.call(this)), newStyle, className = that
							.attr('class');

					$.each(classAnimationActions, function(i, action) {
						if (value[action]) {
							that[action + 'Class'](value[action]);
						}
					});
					newStyle = filterStyles(getElementStyles.call(this));
					that.attr('class', className);

					that.animate(styleDifference(originalStyle, newStyle), {
						queue : false,
						duration : duration,
						easing : easing,
						complete : function() {
							$.each(classAnimationActions, function(i, action) {
								if (value[action]) {
									that[action + 'Class'](value[action]);
								}
							});
							// work around bug in IE by clearing the cssText
							// before setting it
							if (typeof that.attr('style') == 'object') {
								that.attr('style').cssText = '';
								that.attr('style').cssText = originalStyleAttr;
							} else {
								that.attr('style', originalStyleAttr);
							}
							if (callback) {
								callback.apply(this, arguments);
							}
							$.dequeue(this);
						}
					});
				});
	};

	$.fn.extend({
		_addClass : $.fn.addClass,
		addClass : function(classNames, speed, easing, callback) {
			return speed ? $.animateClass.apply(this, [ {
				add : classNames
			}, speed, easing, callback ]) : this._addClass(classNames);
		},

		_removeClass : $.fn.removeClass,
		removeClass : function(classNames, speed, easing, callback) {
			return speed ? $.animateClass.apply(this, [ {
				remove : classNames
			}, speed, easing, callback ]) : this._removeClass(classNames);
		},

		_toggleClass : $.fn.toggleClass,
		toggleClass : function(classNames, force, speed, easing, callback) {
			if (typeof force == "boolean" || force === undefined) {
				if (!speed) {
					// without speed parameter;
					return this._toggleClass(classNames, force);
				} else {
					return $.animateClass.apply(this, [ (force ? {
						add : classNames
					} : {
						remove : classNames
					}), speed, easing, callback ]);
				}
			} else {
				// without switch parameter;
				return $.animateClass.apply(this, [ {
					toggle : classNames
				}, force, speed, easing ]);
			}
		},

		switchClass : function(remove, add, speed, easing, callback) {
			return $.animateClass.apply(this, [ {
				add : add,
				remove : remove
			}, speed, easing, callback ]);
		}
	});

	/** *************************************************************************** */
	/** **************************** ANIMATIONS EASING************************** */
	/** *************************************************************************** */
	$.easing.jswing = $.easing.swing;
	$.extend($.easing, {
		def : 'easeOutQuad',
		swing : function(x, t, b, c, d) {
			// alert($.easing.default);
			return $.easing[$.easing.def](x, t, b, c, d);
		},
		easeInQuad : function(x, t, b, c, d) {
			return c * (t /= d) * t + b;
		},
		easeOutQuad : function(x, t, b, c, d) {
			return -c * (t /= d) * (t - 2) + b;
		},
		easeInOutQuad : function(x, t, b, c, d) {
			if ((t /= d / 2) < 1)
				return c / 2 * t * t + b;
			return -c / 2 * ((--t) * (t - 2) - 1) + b;
		},
		easeInCubic : function(x, t, b, c, d) {
			return c * (t /= d) * t * t + b;
		},
		easeOutCubic : function(x, t, b, c, d) {
			return c * ((t = t / d - 1) * t * t + 1) + b;
		},
		easeInOutCubic : function(x, t, b, c, d) {
			if ((t /= d / 2) < 1)
				return c / 2 * t * t * t + b;
			return c / 2 * ((t -= 2) * t * t + 2) + b;
		},
		easeInQuart : function(x, t, b, c, d) {
			return c * (t /= d) * t * t * t + b;
		},
		easeOutQuart : function(x, t, b, c, d) {
			return -c * ((t = t / d - 1) * t * t * t - 1) + b;
		},
		easeInOutQuart : function(x, t, b, c, d) {
			if ((t /= d / 2) < 1)
				return c / 2 * t * t * t * t + b;
			return -c / 2 * ((t -= 2) * t * t * t - 2) + b;
		},
		easeInQuint : function(x, t, b, c, d) {
			return c * (t /= d) * t * t * t * t + b;
		},
		easeOutQuint : function(x, t, b, c, d) {
			return c * ((t = t / d - 1) * t * t * t * t + 1) + b;
		},
		easeInOutQuint : function(x, t, b, c, d) {
			if ((t /= d / 2) < 1)
				return c / 2 * t * t * t * t * t + b;
			return c / 2 * ((t -= 2) * t * t * t * t + 2) + b;
		},
		easeInSine : function(x, t, b, c, d) {
			return -c * Math.cos(t / d * (Math.PI / 2)) + c + b;
		},
		easeOutSine : function(x, t, b, c, d) {
			return c * Math.sin(t / d * (Math.PI / 2)) + b;
		},
		easeInOutSine : function(x, t, b, c, d) {
			return -c / 2 * (Math.cos(Math.PI * t / d) - 1) + b;
		},
		easeInExpo : function(x, t, b, c, d) {
			return (t == 0) ? b : c * Math.pow(2, 10 * (t / d - 1)) + b;
		},
		easeOutExpo : function(x, t, b, c, d) {
			return (t == d) ? b + c : c * (-Math.pow(2, -10 * t / d) + 1) + b;
		},
		easeInOutExpo : function(x, t, b, c, d) {
			if (t == 0)
				return b;
			if (t == d)
				return b + c;
			if ((t /= d / 2) < 1)
				return c / 2 * Math.pow(2, 10 * (t - 1)) + b;
			return c / 2 * (-Math.pow(2, -10 * --t) + 2) + b;
		},
		easeInCirc : function(x, t, b, c, d) {
			return -c * (Math.sqrt(1 - (t /= d) * t) - 1) + b;
		},
		easeOutCirc : function(x, t, b, c, d) {
			return c * Math.sqrt(1 - (t = t / d - 1) * t) + b;
		},
		easeInOutCirc : function(x, t, b, c, d) {
			if ((t /= d / 2) < 1)
				return -c / 2 * (Math.sqrt(1 - t * t) - 1) + b;
			return c / 2 * (Math.sqrt(1 - (t -= 2) * t) + 1) + b;
		},
		easeInElastic : function(x, t, b, c, d) {
			var s = 1.70158;
			var p = 0;
			var a = c;
			if (t == 0)
				return b;
			if ((t /= d) == 1)
				return b + c;
			if (!p)
				p = d * .3;
			if (a < Math.abs(c)) {
				a = c;
				var s = p / 4;
			} else
				var s = p / (2 * Math.PI) * Math.asin(c / a);
			return -(a * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * d - s)
					* (2 * Math.PI) / p))
					+ b;
		},
		easeOutElastic : function(x, t, b, c, d) {
			var s = 1.70158;
			var p = 0;
			var a = c;
			if (t == 0)
				return b;
			if ((t /= d) == 1)
				return b + c;
			if (!p)
				p = d * .3;
			if (a < Math.abs(c)) {
				a = c;
				var s = p / 4;
			} else
				var s = p / (2 * Math.PI) * Math.asin(c / a);
			return a * Math.pow(2, -10 * t)
					* Math.sin((t * d - s) * (2 * Math.PI) / p) + c + b;
		},
		easeInOutElastic : function(x, t, b, c, d) {
			var s = 1.70158;
			var p = 0;
			var a = c;
			if (t == 0)
				return b;
			if ((t /= d / 2) == 2)
				return b + c;
			if (!p)
				p = d * (.3 * 1.5);
			if (a < Math.abs(c)) {
				a = c;
				var s = p / 4;
			} else
				var s = p / (2 * Math.PI) * Math.asin(c / a);
			if (t < 1)
				return -.5
						* (a * Math.pow(2, 10 * (t -= 1)) * Math
								.sin((t * d - s) * (2 * Math.PI) / p)) + b;
			return a * Math.pow(2, -10 * (t -= 1))
					* Math.sin((t * d - s) * (2 * Math.PI) / p) * .5 + c + b;
		},
		easeInBack : function(x, t, b, c, d, s) {
			if (s == undefined)
				s = 1.70158;
			return c * (t /= d) * t * ((s + 1) * t - s) + b;
		},
		easeOutBack : function(x, t, b, c, d, s) {
			if (s == undefined)
				s = 1.70158;
			return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
		},
		easeInOutBack : function(x, t, b, c, d, s) {
			if (s == undefined)
				s = 1.70158;
			if ((t /= d / 2) < 1)
				return c / 2 * (t * t * (((s *= (1.525)) + 1) * t - s)) + b;
			return c / 2 * ((t -= 2) * t * (((s *= (1.525)) + 1) * t + s) + 2)
					+ b;
		},
		easeInBounce : function(x, t, b, c, d) {
			return c - $.easing.easeOutBounce(x, d - t, 0, c, d) + b;
		},
		easeOutBounce : function(x, t, b, c, d) {
			if ((t /= d) < (1 / 2.75)) {
				return c * (7.5625 * t * t) + b;
			} else if (t < (2 / 2.75)) {
				return c * (7.5625 * (t -= (1.5 / 2.75)) * t + .75) + b;
			} else if (t < (2.5 / 2.75)) {
				return c * (7.5625 * (t -= (2.25 / 2.75)) * t + .9375) + b;
			} else {
				return c * (7.5625 * (t -= (2.625 / 2.75)) * t + .984375) + b;
			}
		},
		easeInOutBounce : function(x, t, b, c, d) {
			if (t < d / 2)
				return $.easing.easeInBounce(x, t * 2, 0, c, d) * .5 + b;
			return $.easing.easeOutBounce(x, t * 2 - d, 0, c, d) * .5 + c * .5
					+ b;
		}
	});
})(jQuery);
