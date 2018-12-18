var	svgObj = {
	isMoving : false,   //是否在运动;
	cx : 600,            //中心点x;
	cy : 100,            //中心点y;
	paper : null,       //记录paper;
	plus : null,        //加号;
	plusStr : "M585,98, 598,98, 598,85, 602,85, 602,98, 615,98, 615,102, 602,102, 602,115, 598,115, 598,102, 585,102Z", //记录加号的数据;
	plusRotation : 0,   //记录旋转;
	
	cirPath : null,      //弧线;
	hasCirPath : false,  
	cirIndex : 0,        //记录弧线的转动角度;
	cirPathStr : "M646,100C646,93.5,645,87,642,81", //弧线的数据;
	interval : null,    //记录弧线的interval;
	
	yellowCir : null,    //黄色的圆圈;
	hasYellowCir : false,
	yellowIndex : 10,     //黄色圆圈变大的;
	
	whiteCir : null,     //白色圆圈;
	hasWhiteCir : false,
	whiteCirIndex : 10,
	
	blueCir : null,      //蓝色圆圈;
	hasBlueCir : false,
	blueCirIndex : 10,
	
	curPath : null,//弧线;
	hasCurPath : false,
	curPathIndex : 0,
	curPath2 : null,
	
	topAngle : null,
	topAngleX : 0,
	topAngleY : 200,
	topAnglePathStr : "M0,0,1200,0,1200,200Z",
	bottomAngle : null,
	bottomAnglePathStr : "M0,0,1200,200,0,200Z",
	bottomAngleX : 1200,
	bottomAngleY : 0,
	hasAngle : false,
	
	svgTxt : null,    //svg文字;
	text : "@MenuItem.customDesktopClick@"
	
}
function fRefreshData(){
	svgObj.isMoving = false;   //是否在运动;
	
	svgObj.plus = null;        //加号;
	plusStr : "M585,98, 598,98, 598,85, 602,85, 602,98, 615,98, 615,102, 602,102, 602,115, 598,115, 598,102, 585,102Z", //记录加号的数据;
	svgObj.plusRotation = 0;
	
	svgObj.cirPath = null;      //弧线;
	svgObj.hasCirPath = false;  
	svgObj.cirIndex = 0;        //记录弧线的转动角度;
	svgObj.cirPathStr = "M646,100C646,93.5,645,87,642,81", //弧线的数据;
	svgObj.interval = null;    //记录弧线的interval;
	
	svgObj.yellowCir = null;    //黄色的圆圈;
	svgObj.hasYellowCir = false;
	svgObj.yellowIndex = 10;     //黄色圆圈变大的;
	
	svgObj.whiteCir = null;     //白色圆圈;
	svgObj.hasWhiteCir = false;
	svgObj.whiteCirIndex = 10;
	
	svgObj.blueCir = null;      //蓝色圆圈;
	svgObj.hasBlueCir = false;
	svgObj.blueCirIndex = 10;
	
	svgObj.curPath = null;//弧线;
	svgObj.hasCurPath = false;
	svgObj.curPathIndex = 0;
	svgObj.curPath2 = null;
	
	svgObj.topAngle = null;
	svgObj.topAngleX = 0;
	svgObj.topAngleY = 200;
	svgObj.topAnglePathStr = "M0,0,1200,0,1200,200Z",
	svgObj.bottomAngle = null;
	svgObj.bottomAnglePathStr = "M0,0,1200,200,0,200Z",
	svgObj.bottomAngleX = 1200;
	svgObj.bottomAngleY = 0;
	svgObj.hasAngle = false;
	
	svgObj.svgTxt = null;
	svgObj.text = "@MenuItem.customDesktopClick@";
}

function fPaperToMiddle(){
	var $outer = $("#plusWrap"),
	$inner = $("#paperDiv"),
	outer = {
		w : $outer.outerWidth() / 2,
		h : $outer.outerHeight() / 2
	}
	inner = {
		w : $inner.outerWidth() / 2,
		h : $inner.outerHeight() / 2
	};
	var l = outer.w - inner.w;
	var t = outer.h - inner.h;
	$inner.css({
		left : l,
		top : t
	})
}
//resize事件增加函数节流;
function throttle(method, context){
	clearTimeout(method.tId);
	method.tId = setTimeout(function(){
		method.call(context);
	},100);
};

function fAddPlus(){
	//创建加号;
	svgObj["plus"] = svgObj["paper"].path(svgObj["plusStr"]).attr({        
		"stroke-width" : 0,
		"fill" : "#AABAD5"
	});
}
function fAddSvgTxt(){
	svgObj.svgTxt = svgObj["paper"].text(svgObj.cx, svgObj.cy, svgObj.text).attr({
			"text-anchor": "middle",
			"fill" : "#268DCA",
			"font-size" : 12
	});
	svgObj["plus"].insertAfter(
		svgObj["svgTxt"]
	);
}
function fOutPlus(){
	svgObj.isMoving = false;
	clearInterval(svgObj.setInerval);
	svgObj["paper"].clear();
	fRefreshData();
	fAddPlus();
}
function rotationPlus(){
	svgObj.plusRotation += 45;
	svgObj["plus"].animate({
		"transform" : "r"+ svgObj.plusRotation +" "+ svgObj.cx +"," + svgObj.cy
	})
	
}
function fHoverPlus(){
	changePlusColor("#31A2FA");
	//创建弧线;
	fAddCirPath();
	
	svgObj.setInerval = setInterval(function(){
		svgObj.isMoving = true;
		if(svgObj.hasCirPath){
			svgObj.cirIndex -=8; 
			svgObj["cirPath"].animate({
				"transform" : "r"+ svgObj.cirIndex +" "+ svgObj.cx +"," + svgObj.cy
			});
			if(svgObj.cirIndex <= -400){
				fClearCirPath();
			}
		}
		
		if(svgObj.cirIndex == -120){
			fAddYellowCir();
			rotationPlus();
		}
		if(svgObj.hasYellowCir){
			svgObj.yellowIndex += 5;
			svgObj["yellowCir"].attr({
				"r" : svgObj.yellowIndex
			});
			if(svgObj.yellowIndex == 180){
				fAddWhiteCir();
				rotationPlus();
			}
			if(svgObj.yellowIndex >= 700){
				fClearYellowCir();
			}
		}
		if(svgObj.hasWhiteCir){
			svgObj.whiteCirIndex += 6;//不可修改否则后面得不到106;
			svgObj["whiteCir"].attr({
				"r" : svgObj.whiteCirIndex
			});
			if(svgObj.whiteCirIndex == 106){
				fAddBlueCir();
				changePlusColor("#ffffff");
				changePlusAlpha(0.5);
				rotationPlus();
			}
			if(svgObj.whiteCirIndex >= 700){
				fClearWhiteCir();
			}
		}
		if(svgObj.hasBlueCir){
			svgObj.blueCirIndex += 4;
			svgObj["blueCir"].attr({
				"r" : svgObj.blueCirIndex
			});
			if(svgObj.blueCirIndex == 150){
				fAddCurPath();
				rotationPlus();
			}
		}
		if(svgObj.hasCurPath){
			svgObj.curPathIndex -= 7;
			svgObj["curPath"].animate({
				"transform" : "r"+ svgObj.curPathIndex +" "+ svgObj.cx +"," + svgObj.cy
			});
			svgObj["curPath2"].animate({
				"transform" : "r"+ (svgObj.curPathIndex-180) +" "+ svgObj.cx +"," + svgObj.cy
			});
			if(svgObj.curPathIndex == -1050){
				fClearBlueCir();
				fClearCurPath();
				fAddAngle();
				changePlusAlpha(0);
				fAddSvgTxt();
			}
		}
		if(svgObj.hasAngle){
			svgObj.topAngleX += 40;
			svgObj.topAngleY -= 6.66;
			svgObj.bottomAngleX -= 40;
			svgObj.bottomAngleY += 6.66;
			svgObj["topAngle"].attr({
				path : "M"+ svgObj.topAngleX +",0,1200,0,1200,"+ svgObj.topAngleY +"Z"
			})
			svgObj["bottomAngle"].attr({
				path : "M0,"+ svgObj.bottomAngleY +","+ svgObj.bottomAngleX +",200,0,200Z"
			})
			//闪烁文字;
			if(svgObj.bottomAngleX == -200){
				changeTxtAlpha(0);
			}
			if(svgObj.bottomAngleX == -400){
				changeTxtAlpha(1);
			}
			if(svgObj.bottomAngleX == -800){
				changeTxtAlpha(0);
			}
			if(svgObj.bottomAngleX == -1000){
				changeTxtAlpha(1);
			}
			if(svgObj.bottomAngleX == -1600){
				changeTxtAlpha(1);
				fClearAngle();
			}
		}
		
	},10);
};
function changePlusColor(color){
	if(svgObj["plus"] != null){
		svgObj["plus"].attr({
			"fill" : color
		})
	}
}
function changePlusAlpha(alpha){
	if(svgObj["plus"] != null){
		svgObj["plus"].attr({
			"opacity" : alpha
		})
	}
}
function changeTxtAlpha(alpha){
	if(svgObj["svgTxt"] != null){
		svgObj["svgTxt"].attr({
			"opacity" : alpha
		})
	}
}

function fAddCirPath(){
	svgObj["cirPath"] = svgObj["paper"].path(svgObj["cirPathStr"]).attr({
			"stroke-width" : 3,
			"stroke" : "#6C90B1"
	});
	svgObj.hasCirPath = true;
}
function fAddYellowCir(){
	svgObj["yellowCir"] = svgObj["paper"].circle(svgObj.cx, svgObj.cy, 10).attr({
		"fill" : "#DDC05A",
		"stroke-width" : 0
	});
	svgObj["plus"].insertAfter(
		svgObj["yellowCir"]
	);
	svgObj.hasYellowCir = true;
}
function fAddBlueCir(){
	svgObj["blueCir"] = svgObj["paper"].circle(svgObj.cx, svgObj.cy, 10).attr({
		"fill" : "#42BCCC",
		"stroke-width" : 0
	});
	svgObj["plus"].insertAfter(
		svgObj["blueCir"]
	);
	svgObj.hasBlueCir = true;
}
function fClearBlueCir(){
	svgObj["blueCir"].remove();
	svgObj["blueCir"] = null;
	svgObj.hasBlueCir = false;
	svgObj.blueCirIndex = 10;
}
function fAddWhiteCir(){
	svgObj["whiteCir"] = svgObj["paper"].circle(svgObj.cx, svgObj.cy, 10).attr({
		"fill" : "#F9F9F9",
		"stroke-width" : 0
	});
	svgObj["plus"].insertAfter(
		svgObj["whiteCir"]
	);
	svgObj.hasWhiteCir = true;
}
function fClearCirPath(){
	svgObj["cirPath"].remove();
	svgObj["cirPath"] = null;
	svgObj.hasCirPath = false;
	svgObj.cirIndex = 0;
	/*svgObj["plus"].attr({
		"fill" : "#AABAD5"
	});*/
}
function fClearYellowCir(){
	svgObj["yellowCir"].remove();
	svgObj["yellowCir"] = null;
	svgObj.hasYellowCir = false;
	svgObj.yellowIndex = 10;
}
function fClearWhiteCir(){
	svgObj["whiteCir"].remove();
	svgObj["whiteCir"] = null;
	svgObj.hasWhiteCir = false;
	svgObj.whiteCirIndex = 10;
}
function fAddCurPath(){
	svgObj["curPath"] = svgObj["paper"].path(svgObj["cirPathStr"]).attr({
			"stroke-width" : 3,
			"stroke" : "#ffffff"
	});
	svgObj["curPath2"] = svgObj["paper"].path(svgObj["cirPathStr"]).attr({
			"stroke-width" : 3,
			"stroke" : "#ffffff"
	});
	svgObj.hasCurPath = true;
}
function fClearCurPath(){
	svgObj["curPath"].remove();
	svgObj["curPath"] = null;
	svgObj["curPath2"].remove();
	svgObj["curPath2"] = null;
	svgObj.hasCurPath = false;
	svgObj.curPathIndex = 0;
}
function fClearAngle(){
	svgObj["topAngle"].remove();
	svgObj["topAngle"] = null;
	svgObj["bottomAngle"].remove();
	svgObj["topAngle"] = null;
	svgObj.topAngleX = 0;
	svgObj.topAngleY = 200;
	svgObj.bottomAngleX = 1200;
	svgObj.bottomAngleY = 0;
	svgObj.hasAngle = false;
}
function fAddAngle(){
	svgObj["topAngle"] = svgObj["paper"].path(svgObj["topAnglePathStr"]).attr({
		"stroke-width" : 0,
		"fill" : "#42BCCC"
	});
	svgObj["bottomAngle"] = svgObj["paper"].path(svgObj["bottomAnglePathStr"]).attr({
		"stroke-width" : 0,
		"fill" : "#42BCCC"
	});
	svgObj["plus"].insertAfter(
		svgObj["topAngle"]
	);
	svgObj["plus"].insertAfter(
		svgObj["bottomAngle"]
	);
	svgObj.hasAngle = true;
}