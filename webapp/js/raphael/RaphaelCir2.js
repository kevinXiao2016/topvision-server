/*
** 首次调用;
var cpuCir = new RaphaelCir({
	renderTo : "cpuCir",
	title : "CPU利用率",
	notGetData : "没有获取到数据",
	NaNData : "数据格式错误",
	edge : [0.6,0.85],
	percent : 0.25
});

** 更新新的数据;
cpuCir.update(0.5);
*/
var RaphaelCir = (function(){
	//颜色值常量;
	var COLOR = {
		GREEN : "#03eafc",
		YELLOW : "#03eafc",
		RED : "#03eafc",
		BLUE : "#03eafc",
		GRAY : "#0e8893"
	};
	//转换文字函数，例如:将0.14转换为字符串14%;
	function fPercentToString(num){
		var str = parseInt( (num*100), 10 ) + "%";
		return str;
	};
	//根据圆弧的比例生成结束点的坐标;
	function fCreateEndPoint(num,scope){
		var o = {
			x : scope.startPoint.x + 55 * Math.sin(2 * Math.PI * (1 - num)),
			y : scope.startPoint.y - 55 + 55 * Math.cos(2 * Math.PI * (1 - num))
		};
		return o;
	}
	//根据范围变化颜色;
    function fGetEdgeColor(num,edge){
		var color;
		if(num < edge[0]){
			color = COLOR.GREEN;
		}else if(num >= edge[0] && num < edge[1]){
			color = COLOR.YELLOW;
		}else{
			color = COLOR.RED;				
		}
		return color;
	}
	//判断数据是否正常,是否是>=0 && <=1; 
	//正常，未获取到数据(等于-1)， 超出范围, 不是数字;
	function fCorrectOshow(_this){
		var me = _this;
		if(typeof(me.percent) == "number"){  //如果是number
			if(me.percent >= 0 && me.percent<= 1){  //数据正常;
				me.oShow.showText = fPercentToString(me.percent);
				me.oShow.fontSize = 32;
			}else if(me.percent == -1){  //数据未获取;
				me.oShow.showText = me.notGetData;
				me.oShow.fontSize = 12;
				me.percent = 0.99999;
			}else{  //数据超出范围;
				me.oShow.showText = fPercentToString(me.percent);
				me.oShow.fontSize = 32;
			}
		}else{ //不是数字;
			me.oShow.showText = me.NaNData;
			me.percent = 0.99999;
			me.oShow.fontSize = 12;
		}
	}
	/*
	** o.percent 例如：0.14 必须小于1大于0的数字;
	** o.renderTo 渲染到哪个div;
	** o.title 底部显示标题文字;
	** o.edge 例如：[0.6,0.85] 分三段显示 >=60% <0.85 为中间段;
	*/
	return function(o){
		this.percent = o.percent; //目前的百分比;
		this.notGetData = o.notGetData || '未获取到数据';
		this.NaNData = o.NaNData || '数据格式错误';
		this.edge = o.edge || [0.6,0.85]; //分三段颜色显示，带有默认值;
		this.paper = Raphael(document.getElementById(o.renderTo), 150, 170); //画布;
		this.subCir = this.paper.circle(75, 75, 55).attr({ //绘制底部浅灰色圆形;
			"stroke-width" : 10,
			"stroke" : COLOR.GRAY
		});
		this.oShow = {
			showText : '',
			fontSize : 12
		};
		fCorrectOshow(this);
		this.text = this.paper.text(75, 75, this.oShow.showText).attr({ //绘制圆中心文字;
				"text-anchor": "middle",
				"fill" : "#03eafc",
				"font-size" : this.oShow.fontSize,
				"font-family" : "helvetica,arial,verdana,sans-serif"
		});
		this.startPoint = { //圆弧进度开始位置，有底部开始，由于只需要生成150px * 150px的圆,所以这部分暂时写死，有需要再考虑扩展成动态;
			x : 75,
			y : 130
		}
		this.endPoint = fCreateEndPoint(this.percent, this); //圆弧结束点;
		var pathStr = Raphael.format("M{0},{1}A{2},{3},{4},{5},{6},{7},{8}", this.startPoint.x, this.startPoint.y, 55, 55, 0, (this.percent > 0.5)?1:0, 1, this.endPoint.x, this.endPoint.y);
		this.upCir = this.paper.path(pathStr).attr({ //关于进度的圆弧;
			"stroke-width" : 3,
			"stroke" : fGetEdgeColor(this.percent, this.edge)
		});
		this.title = this.paper.text(75, 155, o.title || '').attr({ //绘制圆底部标题文字;
				"text-anchor": "middle",
				"fill" : fGetEdgeColor(this.percent, this.edge),
				"font-size" : 12,
				"font-family" : "arial,verdana,sans-serif, 宋体",
				"font-weight" : "bolder"
		});
		this.update = function(newNum){
			this.percent = newNum;
			fCorrectOshow(this);
			this.text.attr({
				"text" : this.oShow.showText,
				'font-size' : this.oShow.fontSize
			});//更新圆中心百分比文字;
			
			this.endPoint = fCreateEndPoint(this.percent, this);
			if(newNum == 1){this.endPoint.x = this.startPoint.x+0.1}
			var pathStr = Raphael.format("M{0},{1}A{2},{3},{4},{5},{6},{7},{8}", this.startPoint.x, this.startPoint.y, 55, 55, 0, (this.percent > 0.5)?1:0, 1, this.endPoint.x, this.endPoint.y);
			var color = fGetEdgeColor(this.percent, this.edge);
			this.upCir.attr({ //更新进度条;
				"path" : pathStr,
				"stroke" : color
			});
			this.title.attr({ //更新标题的文字;
				"fill" : color
			});
		}
	};//end return;
})();
RaphaelCir.prototype = {
	
}
