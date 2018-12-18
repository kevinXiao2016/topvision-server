//拓扑图;
//需要jquery和raphael.js
function Nm3kTopo(o){
	this.renderTo = o.renderTo;//渲染到哪个id;
	this.renderIcoDiv = o.renderIcoDiv;//上半部分,放置图标的div;
	this.width = o.width;
	this.height = o.height;
	this.eqData = o.eqData;//设备的初始数据;
	this.eqDataAll = o.eqData;//全局数据，例如增加了设备要向里面添加;
	this.proData = o.proData;//设备的其他信息，例如告警的初始数据;
	this.proDataAll = o.proData;//全局数据，设备其它信息，例如画线了，要往里面添加;
	this.halfIcoWidth = 48;//要获取图标所在的真正中心点x;
	this.halfIcoHeight = 32;//要获取图标所在的真正中心点y;
	this.icoOuterWidth = 96;//图标的宽度+加边框等;
	this.icoOuterHeight = 106;
	this.topHeight = o.topHeight ? o.topHeight : 0;//如果上半部分有其它东西，则需要给定高度;
	this.type = o.type ? o.type : "sDrag";//sDrag是拖动，sDrawLine是画线，sDrawRect是拉框选择;
	this.lineStroke = o.lineStroke ? o.lineStroke : "#A1CA6A"; //线条颜色，默认是绿色;
	this.lineWidth = o.lineWidth ? o.lineWidth : 3;//线条的宽度;
	this.lineSelectStroke = o.lineSelectStroke ? o.lineSelectStroke : "#365999";//线条被选中的颜色;
	this.paper = null;//存放svg的画布;
	this.path = null;//存放画线时的零时线条;
	this.moveBoolean = false;//图标是否在拖动;
	this.mouseMoveDiv = null;//存储要拖动的div的$(this);
	this.clickXpos = null;//点击的x距离;
	this.clickYpos = null;
	this.dragRectTempDiv = false;//拉框时，是否存在框div;
	this.nEdgeNum = 1000;
	this.lineOpacity = o.lineOpacity//线条的透明度;
	this.scale = 1;//放大缩小后，需要纠正鼠标点击的pageX和pageY;
	
	this.topoFolderId = o.topoFolderId;//存储所在地域的id;
	
	this.aSelectedIco = [];//将拉框选中的所有图标记录在这个数组中;
	this.aSelectedLine = [];//将拉框选中的所有线条记录在这个数组中;
	this.selectOneIco = o.selectOneIco ? o.selectOneIco : null;//选中一个图标则执行function;
	this.selectOneLine = o.selectOneLine ? o.selectOneLine : null;//选中一条线条执行function;
	this.clickBlank = o.clickBlank ? o.clickBlank : null; //点击空白处 ;
	this.drawRectFn = o.drawRectFn ? o.drawRectFn : null; //拉框选择后执行function;
	this.eqDrawLineFn = o.eqDrawLineFn ? o.eqDrawLineFn : null;//对2个设备绘制线条后执行function;
}
Nm3kTopo.prototype.init = function(){
	//绘制画布;
	this.paper = Raphael(document.getElementById(this.renderIcoDiv), this.width, this.height);
	
	//先加载设备;
	if(!this.eqData.entity){this.eqData.entity = [];}
	var aEqData = this.eqData.entity;		
	
	var eqStr = '<img width="48" height="48" src="{0}" unselectable="on" /><span class="theName">{1}</span>';
	var eqStr2 = '<img width="48" height="48" src="{0}" unselectable="on" /><span class="theName" title="{1}">{1}</span>';
	for(var i=0; i<aEqData.length; i++){
		var eqDiv = document.createElement("div");
		eqDiv.id = aEqData[i].id;			
		eqDiv.className = "eqContainer";
		eqDiv.setAttribute("name",aEqData[i].nodeId);//不知道到底是使用了userObjId还是nodeId，两个值一样，暂时用nodeId;
		eqDiv.setAttribute("alt",aEqData[i].fixed);
		eqDiv.setAttribute("type",aEqData[i].type);//把设备类型type存入自定义属性中，ip存入ip属性中;
		eqDiv.setAttribute("ip",aEqData[i].ip);
		eqDiv.setAttribute("nodeId",aEqData[i].nodeId);//把nodeId存起来;
		eqDiv.setAttribute("sysName",aEqData[i].sysName);//sysName存起来查找的时候使用;
		eqDiv.setAttribute("text",aEqData[i].text ? aEqData[i].text : "no name");
		eqDiv.setAttribute("onselectstart","javascript:return false");
		
		eqDiv.style.position = "absolute";
		eqDiv.style.top = aEqData[i].y + "px";
		eqDiv.style.left = aEqData[i].x + "px";
		$("#"+this.renderIcoDiv).append(eqDiv);	
		var eqName;
		switch(showType){
			case 2://显示别名;
				eqName = aEqData[i].text ? aEqData[i].text : "no name";
				break;
			case 1://显示名称;
				eqName = aEqData[i].sysName ? aEqData[i].sysName : " ";
				break;
			case 0://显示ip;
				eqName = aEqData[i].ip ? aEqData[i].ip : " ";
				break;
		}
		var newEqStr =  Raphael.format(eqStr, aEqData[i].icon, eqName);
		if(eqName.length > 14){
			newEqStr =  Raphael.format(eqStr2, aEqData[i].icon, eqName);
		}
		$(eqDiv).append(newEqStr);//将图标加入到上半部div中;
		//绑定事件;
		$(eqDiv).bind("mousedown",this,this.eqMouseDown);
		$(eqDiv).bind("click",this,this.clearBubble);//清除冒泡，点击空白处才不会选择任何图标;					
		$(eqDiv).bind("mouseenter",this,this.eqMouseOver);
		$(eqDiv).bind("mouseleave",this,this.eqMouseOut);
	}
	//加载子地域;
	if(!this.eqData.folder){this.eqData.folder = []}
	var aFolders = this.eqData.folder;
	for(i=0; i<aFolders.length; i++){
		var floder = document.createElement("div");
		floder.id = aFolders[i].id;
		floder.className = "eqContainer";
		floder.setAttribute("name",aFolders[i].userObjId);//这里使用的是userObjId,因为啥删除的时候用到了;
		floder.setAttribute("alt",aFolders[i].fixed);
		floder.setAttribute("type",aFolders[i].objType);//不知道为什么设备是type,但是地域又是objType;
		floder.setAttribute("nodeId",aFolders[i].nodeId);//把nodeId存起来;
		floder.setAttribute("onselectstart","javascript:return false");
		
		floder.style.position = "absolute";
		floder.style.top = aFolders[i].y + "px";
		floder.style.left = aFolders[i].x + "px";
		$("#"+this.renderIcoDiv).append(floder);		
		var fName = aFolders[i].text ? aFolders[i].text : "no name";			
		var newFloderStr =  Raphael.format(eqStr, aFolders[i].icon, fName);
		if(fName.length > 14){
			newFloderStr =  Raphael.format(eqStr2, aFolders[i].icon, fName);
		}
		$(floder).append(newFloderStr);//将图标加入到上半部div中;
		//绑定事件;
		$(floder).bind("mousedown",this,this.eqMouseDown);
		$(floder).bind("click",this,this.clearBubble);//清除冒泡，点击空白处才不会选择任何图标;	
	}
	
	//加载线条;
	if(!this.eqData.link){this.eqData.link = [];}
	var aLinkData = this.eqData.link;
	for(i=0; i<aLinkData.length; i++){//循环线条数组;
		var oLineStart = {},//起始点对象;
		oLineEnd = {};//结束点对象;
		oLineStart.id = aLinkData[i].srcEntityId;
		oLineEnd.id = aLinkData[i].destEntityId;
		
		//通过循环得到起始点的x和y;
		var flagStart = false;//有可能链接的不是设备是子地域;
		var flagEnd = false;
		for(var j=0; j<aEqData.length; j++){
			if(oLineStart.id == aEqData[j].userObjId){//获取起点的x,y;
				oLineStart.x = aEqData[j].x;
				oLineStart.y = aEqData[j].y;
				flagStart = true;
			}
			if(oLineEnd.id == aEqData[j].userObjId){//获取终点的x,y;
				oLineEnd.x = aEqData[j].x;
				oLineEnd.y = aEqData[j].y;
				flagEnd = true;
			}
		}
		if(!flagStart && this.eqData.folder){
			var aFolder = this.eqData.folder; 
			for(var k=0; k<aFolder.length; k++){
				if(oLineStart.id == aFolder[k].nodeId){//获取起点的x,y;
					oLineStart.x = aFolder[k].x;
					oLineStart.y = aFolder[k].y;
				}
			}
		}
		if(!flagEnd && this.eqData.folder){
			var aFolder = this.eqData.folder; 
			for(var m=0; m<aFolder.length; m++){
				if(oLineEnd.id == aFolder[m].nodeId){//获取起点的x,y;
					oLineEnd.x = aFolder[m].x;
					oLineEnd.y = aFolder[m].y;
				}
			}
		}
		/*var pathString = Raphael.format("M{0},{1}L{2},{3}",oLineStart.x + this.halfIcoWidth, oLineStart.y + this.halfIcoHeight, oLineEnd.x + this.halfIcoWidth, oLineEnd.y + this.halfIcoHeight);
		var line = this.paper.path(pathString);
		line.id = aLinkData[i].id;
		line.attr({"stroke-width": 3,"stroke": "#A1CA6A"});
		var me = this;
		line.click(function(e){
			e.stopPropagation();
			//循环所有线条，将其变成默认颜色;
			var aLines = me.eqDataAll.link;
			for(i=0; i<aLines.length; i++){
				me.paper.getById(aLines[i].id).attr({"stroke": me.lineStroke});
			}
			this.attr({"stroke": me.lineSelectStroke});
			if(me.selectOneLine != null){
				WIN[me.selectOneLine].call(WIN,[this]);
			};
		})*/
		this.drawPath({
			x1: (oLineStart.x + this.halfIcoWidth),
			y1: (oLineStart.y + this.halfIcoHeight),
			x2: (oLineEnd.x + this.halfIcoWidth),
			y2: (oLineEnd.y + this.halfIcoHeight) ,
			id: aLinkData[i].id,
			paper: this.paper,
			strokeWidth: this.lineWidth,
			stroke: this.lineStroke,
			me:this
		});
	};
	
	
	//再加载设备的告警;
	if(!this.proData.entities){this.proData.entities = [];}
	var aAlertData = this.proData.entities;
	for(i=0; i<aAlertData.length; i++){
		var sPro = '';
		if(aAlertData[i].value && aAlertData[i].value != -100){//如果存在CPU或MEM
			sPro += '<div class="nm3kTopoPercent" style="background:'+ aAlertData[i].backgroundColor +';">'+ aAlertData[i].value +'%</div>';
		};
		if(aAlertData[i].alert){//如果存在告警信息;
			if(aAlertData[i].alert > 0){
				var currentData = aAlertData[i];
				sPro += '<img alt="'+ currentData.alertId +'" name="'+ currentData.entityId +'" class="nm3kTopoAlert" unselectable="on" title="'+ currentData.alt +'" src="/images/fault/level'+ currentData.alert +'.png" />';
			}
		}
		$("#" + aAlertData[i].id).append(sPro);
	}
	//点击桌面，清除选中的图标;
	$("#" + this.renderTo).bind("mousedown.clearSelIco",this,this.clearSelIco);
	//点击桌面，拉框选择;
	$("#" + this.renderIcoDiv).bind("mousedown.drawRectDown",this,this.drawRectDown);
}
//加载告警等其他属性信息;
Nm3kTopo.prototype.addProInfo = function(paraData){
	this.proData = paraData;//设备的其他信息，例如告警的初始数据;
	this.proDataAll = paraData;//全局数据，设备其它信息;
	
	var aAlertData = this.proData.entities;
	if(aAlertData){
		for(i=0; i<aAlertData.length; i++){
			var sPro = '';
			if(aAlertData[i].value && aAlertData[i].value != -100){//如果存在CPU或MEM
				sPro += '<div class="nm3kTopoPercent" style="background:'+ aAlertData[i].backgroundColor +';">'+ aAlertData[i].value +'%</div>';
			};
			if(aAlertData[i].alert){//如果存在告警信息;
				if(aAlertData[i].alert > 0){
					var currentData = aAlertData[i];
					var src = currentData.alertDesc + '<br />' + currentData.alertTime
					sPro += '<img alt="'+ currentData.alertId +'" name="'+ currentData.entityId +'" class="nm3kTopoAlert nm3kTip" nm3kTip="' + src + '" unselectable="on" src="/images/fault/level'+ currentData.alert +'.png" />';  
				}
			}
			$("#" + aAlertData[i].id).append(sPro);
		}
	}				
};//end addProInfo;
//点击图标;
Nm3kTopo.prototype.eqMouseDown = function(e){		
	$("#bubbleTip").css({display:"none"});
	e.preventDefault();//组织浏览器拖动图标上的图片;		
	var me = e.data,
	    $this = $(this),
	    clickTarget = e.target,
	    $clickTarget = $(clickTarget),
	    leftScroll = $("#"+ me.renderTo).scrollLeft(),
	    topScroll = $("#" + me.renderTo).scrollTop();
	
	if(clickTarget.tagName === "IMG" && $clickTarget.hasClass("nm3kTopoAlert")){//点击的是告警，直接跳转到告警;
		
		var id = $clickTarget.attr("name"),
		    title = $clickTarget.parent().find("span.theName").text(),
		    type = $clickTarget.parent().attr("type"),
		    alertId = $clickTarget.attr("alt");
		
		if(EntityType.getOltType() == type){
			top.addView('entity-' + id,  title , 'entityTabIcon','/epon/alert/showOltAlert.tv?module=5&entityId=' + id + '&alertId=' + alertId, null, true);
		}else if(EntityType.getCcmtsType() == type){
			top.addView('entity-' + id,  title , 'entityTabIcon','/cmc/alert/showCmcAlert.tv?module=12&cmcId=' + id + '&alertId=' + alertId, null, true);
		}else if(EntityType.getCmtsType() == type){
			top.addView('entity-' + id,  title , 'entityTabIcon','/cmts/alert/showCmtsAlert.tv?module=7&cmcId=' + id + '&alertId=' + alertId, null, true);
		}else if(EntityType.getOnuType() == type){
			top.addView('entity-' + id,  title , 'entityTabIcon','/onu/showOnuAlert.tv?module=3&onuId=' + id + '&alertId=' + alertId, null, true);
		}
		
		return;
	}
	
	if(e.data.type != "sDrawRect"){//拉框的时候不要选中图标;
		e.stopPropagation();
		me.clearLineSelect(me);//清除选中的画线;
		$("#"+ me.renderIcoDiv +" .eqContainer").removeClass("eqContainerSelected");
		$this.addClass("eqContainerSelected");//选中图标;	
		if(me.selectOneIco != null){
			var fixed = $this.attr("alt");
			WIN[me.selectOneIco].call(WIN,fixed);
		}
	}
	me.mouseMoveDiv = $this;//将要拖动的div存储;		
	me.icoOuterWidth = $this.outerWidth();
	me.icoOuterHeight = $this.outerHeight();
	if(me.type == "sDrag"){//拖动;
		me.clickXpos = e.clientX - $(this).position().left + leftScroll;
		me.clickYpos = e.clientY -  $(this).position().top + topScroll;
		me.mouseMoveDiv.bind("mouseup", me, me.eqMouseUp);
		if($this.attr("alt") == "false" || $this.attr("alt") == false){//alt存储的是fixed值，一旦fixed=true,则不可以拖动;
			$("#" + me.renderTo).bind("mousemove", me, me.eqMouseMove);
			$("#" + me.renderTo).bind("mouseup", me, me.eqMouseUp);
			var renderToDivDom = $("#" + me.renderTo).get(0);
			if(renderToDivDom.setCapture){
				renderToDivDom.setCapture();
			}else if(window.captureEvents){
				window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
			}
		}
	}
	/////////////////////再这里判断点击的必须是图片元素,才能绘制线条;		
	//if(me.type == "sDrawLine"){
	if(me.type == "sDrawLine" && e.target.nodeName == "IMG"){//绘制线条;
		$("#" + me.renderTo).bind("mousemove", me, me.eqMouseMoveDragLine);
		$("#" + me.renderTo).bind("mouseup", me, me.eqMouseUp);
	}
};//end eqMouseDown;
//拖动图标;
Nm3kTopo.prototype.eqMouseMove = function(e){
	var me = e.data,
	leftPos,
	topPos,
	leftScroll = $("#"+ me.renderTo).scrollLeft(),
	topScroll = $("#" + me.renderTo).scrollTop();
	if(!me.moveBoolean){
		leftPos = me.mouseMoveDiv.position().left / me.scale;
		topPos = me.mouseMoveDiv.position().top / me.scale;
		$("#" + me.renderIcoDiv).append('<div id="nm3kTopoTempMoveDiv" style="width:90px; height:80px; opacity:0.5; filter:alpha(opacity=50); overflow:hidden; background:#ccc; position:absolute; text-align:center; border:3px solid #f00; z-index:999; top:'+ topPos +'px; left:'+ leftPos +'px"></div>');
		me.moveBoolean = true;
	}else{		
		leftPos = (e.clientX - me.clickXpos + leftScroll) / me.scale;
		topPos = (e.clientY - me.clickYpos + topScroll) / me.scale;
		if(leftPos < 0){ leftPos = 0;}//拖动不能超出范围;
		if( leftPos > me.width / me.scale - me.icoOuterWidth ){ leftPos = me.width / me.scale - me.icoOuterWidth;}
		if(topPos < 0 ){topPos = 0;}
		if(topPos > me.height / me.scale - me.icoOuterHeight){ topPos =  me.height / me.scale - me.icoOuterHeight;}
		$("#nm3kTopoTempMoveDiv").css({left:leftPos, top:topPos});
	}
};//end eqMosueDown;
//鼠标move，绘制线条的时候;
Nm3kTopo.prototype.eqMouseMoveDragLine = function(e){
	//e.stopPropagation();
	var me = e.data,
	leftScroll = $("#"+ me.renderTo).scrollLeft(),
	topScroll = $("#" + me.renderTo).scrollTop(),
	leftPos = me.mouseMoveDiv.position().left / me.scale + me.halfIcoWidth,
	topPos = me.mouseMoveDiv.position().top / me.scale + me.halfIcoHeight,		
	pathString = Raphael.format("M{0},{1}L{2},{3}",leftPos,topPos, (e.pageX + leftScroll) / me.scale, (e.pageY + topScroll - me.topHeight) / me.scale);
	if(me.path == null){//没有画线,画新的，有画线，更新path属性;
		me.path = me.paper.path(pathString);	
	}else{
		me.path.attr({
			"path" : pathString,
			"stroke-width" : 3,
			"stroke-dasharray" : "--",
			"stroke" : "#EF420A"
		});
	};
}

//松开点击图标;
Nm3kTopo.prototype.eqMouseUp = function(e){
	var me = e.data,
	leftScroll = $("#"+ me.renderTo).scrollLeft(),
	topScroll = $("#" + me.renderTo).scrollTop();
	
	switch(me.type){
		case "sDrag":
			var $nm3kTopoTempMoveDiv = $("#nm3kTopoTempMoveDiv");
			if($nm3kTopoTempMoveDiv.length > 0){//说明是拖动，不是点击;
				var tPos = $nm3kTopoTempMoveDiv.position().top / me.scale;
				var lPos = $nm3kTopoTempMoveDiv.position().left / me.scale;
				me.mouseMoveDiv.css({top : tPos, left: lPos});
				$nm3kTopoTempMoveDiv.remove();
				
				//循环me.eqDataAll数组，重绘线条;
				if(me.eqDataAll.link && me.eqDataAll.link.length > 0){
					var aLinks = me.eqDataAll.link;
					var eqName = Number((me.mouseMoveDiv.attr("id")).slice(4));
					for(var i=0; i<aLinks.length; i++){//循环线条;
						if(aLinks[i].srcEntityId == eqName || aLinks[i].destEntityId == eqName){//如果线条的起始段和线条的指向设备一致;
							var svgLine = me.paper.getById(aLinks[i].id);
							var oLineStart = {},//起始点对象;
							oLineEnd = {},//结束点对象;
							$start = $("#cell" + aLinks[i].srcEntityId),//绘线开始端点的$div;
							$end = $("#cell" + aLinks[i].destEntityId);//绘线结束端点的$div;
													
							oLineStart.x = $start.position().left / me.scale;
							oLineStart.y = $start.position().top / me.scale;
							oLineEnd.x = $end.position().left / me.scale;
							oLineEnd.y = $end.position().top / me.scale;
							var pathString = Raphael.format("M{0},{1}L{2},{3}",oLineStart.x + me.halfIcoWidth, oLineStart.y + me.halfIcoHeight, oLineEnd.x + me.halfIcoWidth, oLineEnd.y + me.halfIcoHeight);
							svgLine.attr({
								"path" : pathString
							});
						}
					};//end for;
				};//end if;
			}else{
				//是点击不是拖动;
				var $mouseMoveDiv = $(me.mouseMoveDiv);
				if(displayCluetip && $mouseMoveDiv.attr("type") != "2" && $mouseMoveDiv.attr("type") != undefined){
					//对着fixed的子地域死点，有时候会出现undefined,这里也可以修改成$(".eqContainerSelected").attr("type");
					$("#bubbleTip").css({display:"block"});
				}
			}
			//移除绑定;		
			$("#" + me.renderTo).unbind("mouseup", me.eqMouseUp);
			$("#" + me.renderTo).unbind("mousemove", me.eqMouseMove);
			$(me.mouseMoveDiv).unbind("mouseup", me.eqMouseUp);
			var renderToDivDom = $("#" + me.renderTo).get(0); 
			if(renderToDivDom.releaseCapture){
				renderToDivDom.releaseCapture();
	        }else if(window.releaseEvents){
	        	window.releaseEvents(Event.MOUSEMOVE|Event.MOUSEUP);
	        }
		break;
		case "sDrawLine":
			if(e.target.nodeName == "IMG" && $(e.target).parent().hasClass("eqContainer")){
				if($(e.target).parent().attr("id") != me.mouseMoveDiv.attr("id")){
					//不能是自己对着自己;
					var firstIcoId = Number((me.mouseMoveDiv.attr("id")).slice(4)),
					endIcoId = Number(($(e.target).parent().attr("id")).slice(4));
					
					var bHasOne = false;//是否在数组里面存在;
					//循环一下数组，如果没有连接这两个设备的线条，那么可以绘制线条;
					if(me.eqDataAll.link){
						var aLinks = me.eqDataAll.link;
						for(var i=0; i<aLinks.length; i++){
							if(aLinks[i].srcEntityId == firstIcoId && aLinks[i].destEntityId == endIcoId || aLinks[i].srcEntityId == endIcoId && aLinks[i].destEntityId == firstIcoId){
								bHasOne = true;
								break;
							}
						}
					}else{
						//如果没有数组，就新建;
						me.eqDataAll.link = [];
					}
					if(bHasOne == false){
						if(me.eqDrawLineFn){
							//进后台生成线条,注意这里是线条与线条之间的绘制;
							var oEdgeData = {
								srcEntityId: firstIcoId,
								destEntityId: endIcoId,
								name: "new link",//不知道为什么，这里就是写死的;
								connectType: 5, //这里其实是SHAPE_TYPE.STRAIGHT_CONNECT,暂时写死;
								folderId : me.topoFolderId
							}
							var json = WIN[me.eqDrawLineFn].call(WIN,oEdgeData);
							if(json != null){
								//如果数据库传过来的结果是null说明数据库存储失败了。
								me.eqDataAll.link.push(json);
								me.drawPath({
									x1: me.mouseMoveDiv.position().left / me.scale + me.halfIcoWidth,
									y1: me.mouseMoveDiv.position().top / me.scale + me.halfIcoHeight,
									x2: $(e.target).parent().position().left / me.scale + me.halfIcoWidth,
									y2: $(e.target).parent().position().top / me.scale + me.halfIcoHeight,
									id: json.id,
									paper: me.paper,
									strokeWidth: me.lineWidth,
									stroke: me.lineStroke,
									me:me
								});
							}
						}
					};//end if;
				}
			}
			if(me.path != null){
				//清除掉绘制的线条;
				me.path.remove();
			}
			//移除绑定;		
			$("#" + me.renderTo).unbind("mouseup", me.eqMouseUp);
			$("#" + me.renderTo).unbind("mousemove", me.eqMouseMoveDragLine);
		break;
	};//end switch;
	
	me.path = null;
	me.moveBoolean = false;
	me.mouseMoveDiv = null;
	me.clickXpos = null;
	me.clickYpos = null;
};//end eqMouseUp;

Nm3kTopo.prototype.clearBubble = function(e){
	e.stopPropagation();
}
//点击桌面，清除选择图标，清除选择的线条颜色;
Nm3kTopo.prototype.clearSelIco = function(e){
	var me = e.data;
	$("#"+ me.renderIcoDiv +" .eqContainer").removeClass("eqContainerSelected");
	me.clearLineSelect(me);
	if(me.clickBlank != null){
		WIN[me.clickBlank].apply(WIN);
	}
}
//清除所有选中的线条;
Nm3kTopo.prototype.clearLineSelect = function(paraThis){
	var me = paraThis;
	if(me.eqDataAll.link){
		//有可能没有任何线条;
		var aLines = me.eqDataAll.link;
		for(i=0; i<aLines.length; i++){
			me.paper.getById(aLines[i].id).attr({"stroke": me.lineStroke});
		}
	}
}

//删除线条;
Nm3kTopo.prototype.removeLine = function(lineId){
	var line = this.paper.getById(lineId);
	$(line.node).unbind("click",this.clickLine);
	line.remove();
};

//private 画线,线条上增加一个点击事件;
Nm3kTopo.prototype.drawPath = function(o){
	var pathString = Raphael.format("M{0},{1}L{2},{3}",o.x1, o.y1, o.x2, o.y2);
	var line = o.paper.path(pathString);
	line.id = o.id;
	line.attr({"stroke-width": o.strokeWidth ,"stroke": o.stroke, "stroke-opacity": o.me.lineOpacity});
	var newObj = o;
	newObj.selectedLine = line;
	$(line.node).bind("click", newObj, o.me.clickLine);
};//end drawPath;

Nm3kTopo.prototype.clickLine = function(e){
	e.stopPropagation();
	var me = e.data.me;
	me.clearLineSelect(me);
	e.data.selectedLine.attr({"stroke": me.lineSelectStroke});
	if(me.selectOneLine != null){
		WIN[me.selectOneLine].call(WIN,[e.data.selectedLine]);
	};
};
// public 更新线条的位置(执行图标对齐时调用);
// aLinesId 格式: [{id:线的id, moveTo的x1, moveTo的y1, lineTo的x2, lineTo的y2}];
Nm3kTopo.prototype.upDateLinePosition = function(aLinesId,Nm3kTopoObj){
	for(var i=0; i<aLinesId.length; i++){
		var pathString = Raphael.format("M{0},{1}L{2},{3}", aLinesId[i].x1, aLinesId[i].y1, aLinesId[i].x2, aLinesId[i].y2);
		Nm3kTopoObj.paper.getById(aLinesId[i].id).attr({path: pathString});
	}
}
//更新所有的线条位置（排列图标时调用）;
Nm3kTopo.prototype.upDateAllLinePosition = function(Nm3kTopoObj){
	if(Nm3kTopoObj.eqDataAll.link){
		var aLinks = Nm3kTopoObj.eqDataAll.link;
		for(var i=0; i<aLinks.length; i++){
			var $src = $("#" + Nm3kTopoObj.renderIcoDiv + " .eqContainer[nodeid='"+ aLinks[i].srcEntityId +"']");
			var $dest = $("#" + Nm3kTopoObj.renderIcoDiv + " .eqContainer[nodeid='"+ aLinks[i].destEntityId +"']");
			var pathString = Raphael.format("M{0},{1}L{2},{3}", $src.position().left / this.scale + Nm3kTopoObj.halfIcoWidth, $src.position().top / this.scale + Nm3kTopoObj.halfIcoHeight, $dest.position().left / this.scale + Nm3kTopoObj.halfIcoWidth, $dest.position().top / this.scale + Nm3kTopoObj.halfIcoHeight);
			Nm3kTopoObj.paper.getById(aLinks[i].id).attr({path: pathString});
		}
	}
};//end upDateAllLinePosition;
//////////////////////////////////////拉框选择start;
//点击拉框;
Nm3kTopo.prototype.drawRectDown = function(e){	
	if(e.data.type == "sDrawRect"){//如果是拉框;
		var me = e.data,
		leftScroll = $("#"+ me.renderTo).scrollLeft(),
		topScroll = $("#" + me.renderTo).scrollTop();
		me.clickXpos = e.pageX + leftScroll;
		me.clickYpos = e.pageY + topScroll - me.topHeight;
		$("#" + me.renderIcoDiv).bind("mousemove.drawRectMove", me, me.drawRectMove);
		$("#" + me.renderIcoDiv).bind("mouseup.drawRectUp", me, me.drawRectUp);
	}
};//end drawRectDown;
//拖动画框;
Nm3kTopo.prototype.drawRectMove = function(e){	
	var me = e.data,
	leftScroll = $("#"+ me.renderTo).scrollLeft(),
	topScroll = $("#" + me.renderTo).scrollTop(),
	l,t,
	w = Math.abs( (e.pageX  + leftScroll - me.clickXpos) / me.scale ),
	h = Math.abs( (e.pageY  + topScroll - me.clickYpos - me.topHeight) / me.scale );
	if(!me.dragRectTempDiv && $("#nm3kTopoTempDragBox").length == 0){
		var sBox = Raphael.format('<div id="nm3kTopoTempDragBox" class="nm3kTopoTempDragBox" style="left:{0}px; top:{1}px; width:{2}px; height:{3}px"></div>', me.clickXpos, me.clickYpos, w, h);
		$("#" + me.renderIcoDiv).append(sBox);
		me.dragRectTempDiv = true;
	}else{
		l = e.pageX  + leftScroll > me.clickXpos ? me.clickXpos / me.scale : (e.pageX + leftScroll) / me.scale;
		t = e.pageY  + topScroll - me.topHeight > me.clickYpos ? me.clickYpos / me.scale : (e.pageY + topScroll - me.topHeight) / me.scale;
		$("#nm3kTopoTempDragBox").css({width: w, height: h, left: l, top: t});
	}		
};//end drawRectMove;
//拉框松开;
Nm3kTopo.prototype.drawRectUp = function(e){	
	var me = e.data,
	$nm3kTopoTempDragBox = $("#nm3kTopoTempDragBox"),
	leftScroll = $("#"+ me.renderTo).scrollLeft(),
	topScroll = $("#" + me.renderTo).scrollTop(),
	dragObj = {},//记录上下左右四条边的零时变量;
	icoObj = {};
	
	me.aSelectedIco.length = 0;//先清空选中的图标数组;
	me.aSelectedLine.length = 0;
	if(me.dragRectTempDiv){
		dragObj.top = $nm3kTopoTempDragBox.position().top / me.scale;
		dragObj.right = $nm3kTopoTempDragBox.position().left / me.scale + $nm3kTopoTempDragBox.outerWidth();
		dragObj.bottom = $nm3kTopoTempDragBox.position().top / me.scale + $nm3kTopoTempDragBox.outerHeight();
		dragObj.left = $nm3kTopoTempDragBox.position().left / me.scale;
		//判断是否和图标重合，重合的则被选中;
		$("#" + me.renderIcoDiv).find(".eqContainer").each(function(){
			var $this = $(this);
			icoObj.top = $this.position().top / me.scale;
			icoObj.right = $this.position().left / me.scale + $this.outerWidth();
			icoObj.bottom = $this.position().top / me.scale + $this.outerHeight();
			icoObj.left = $this.position().left / me.scale;
			//如果返回true,则说明相交;
			if(Raphael.isBBoxIntersect({x:dragObj.left ,y:dragObj.top ,x2:dragObj.right , y2:dragObj.bottom}, {x:icoObj.left ,y:icoObj.top ,x2:icoObj.right , y2:icoObj.bottom})){
				$this.addClass("eqContainerSelected");
				me.aSelectedIco.push($this);
			}
		});//end each;		
		//画四条线,判断是否与绘制的矩形框的四条线任何一条相交，除此之外还要判断线条在四条线内部;
		var sT = Raphael.format("M{0},{1}L{2},{3}", dragObj.left, dragObj.top, dragObj.right, dragObj.top),
		sR = Raphael.format("M{0},{1}L{2},{3}", dragObj.right, dragObj.top, dragObj.right, dragObj.bottom),
		sB = Raphael.format("M{0},{1}L{2},{3}", dragObj.left, dragObj.bottom, dragObj.right, dragObj.bottom),
		sL = Raphael.format("M{0},{1}L{2},{3}", dragObj.left, dragObj.top, dragObj.left, dragObj.bottom),
		pathT = me.paper.path(sT),
		pathR = me.paper.path(sR),
		pathB = me.paper.path(sB),
		pathL = me.paper.path(sL);
		
		//循环所有界面上的线条，如果有相交，则要求选中;
		if(me.eqDataAll.link){
			var aLinks = me.eqDataAll.link;
			for(var i=0; i<aLinks.length; i++){
				var screenLine = me.paper.getById(aLinks[i].id);
				var sLinePath = screenLine.attr("path");
				var o1 = Raphael.pathIntersection(sT,sLinePath);
				var o2 = Raphael.pathIntersection(sR,sLinePath);
				var o3 = Raphael.pathIntersection(sB,sLinePath);
				var o4 = Raphael.pathIntersection(sL,sLinePath);
				//判断屏幕线条的2个端点都在绘制的rect内部;
				var aLinePath = Raphael.parsePathString(sLinePath);
				var x1 = aLinePath[0][1];//获取端点x值;
				var y1 = aLinePath[0][2];
				var x2 = aLinePath[1][1];//获取末尾点x值;
				var y2 = aLinePath[1][2];
				var bStartP = Raphael.isPointInsideBBox({x:dragObj.left ,y:dragObj.top ,x2:dragObj.right , y2:dragObj.bottom}, x1, y1);
				var bEndP = Raphael.isPointInsideBBox({x:dragObj.left ,y:dragObj.top ,x2:dragObj.right , y2:dragObj.bottom}, x2, y2);
				if((bStartP && bEndP) || o1.length + o2.length + o3.length + o4.length > 0){
					screenLine.attr({"stroke": me.lineSelectStroke});
					me.aSelectedLine.push(screenLine);
				}				
			}
		}
		pathT.remove();
		pathR.remove();
		pathB.remove();
		pathL.remove();
	}
	$("#" + me.renderIcoDiv).unbind("mousemove.drawRectMove", me.drawRectMove);
	$("#" + me.renderIcoDiv).unbind("mouseup.drawRectUp", me.drawRectUp);		
	me.dragRectTempDiv = false;
	$("#nm3kTopoTempDragBox").remove();		
	WIN[me.drawRectFn].call(WIN,me.aSelectedIco,me.aSelectedLine);
}
//////////////////////////////////////拉框选择end;
///////////////////////////////////////////////////////添加新的图标start;
Nm3kTopo.prototype.addNewIco = function(paraArr){
	var len = paraArr.length;
	for(var i=0; i<len; i++){//循环传进来的entity数组;
		var flag = false;
		if(this.eqDataAll.entity){
			var aEqData = this.eqData.entity;	
			for(var j=0; j<aEqData.length; j++){
				if(aEqData[j].nodeId == paraArr[i].nodeId){
					flag = true;
				}
			}
		}
		if(!flag){
			if(!this.eqDataAll.entity){this.eqDataAll.entity = []};
			var eqStr = '<img width="48" height="48" src="{0}" unselectable="on" /><span class="theName">{1}</span>';
			var eqDiv = document.createElement("div");
			eqDiv.id = paraArr[i].id;			
			eqDiv.className = "eqContainer";
			eqDiv.setAttribute("name",paraArr[i].nodeId);//不知道到底是使用了userObjId还是nodeId，两个值一样，暂时用nodeId;
			eqDiv.setAttribute("alt",paraArr[i].fixed);
			eqDiv.setAttribute("type",paraArr[i].type);//把设备类型type存入自定义属性中，ip存入ip属性中;
			eqDiv.setAttribute("ip",paraArr[i].ip);
			eqDiv.setAttribute("nodeId",paraArr[i].nodeId);//把nodeId存起来;
			eqDiv.setAttribute("sysName",aEqData[i].sysName);
			eqDiv.setAttribute("text",aEqData[i].text ? aEqData[i].text : "no name");
			eqDiv.setAttribute("onselectstart","javascript:return false");
			
			eqDiv.style.position = "absolute";
			eqDiv.style.top = paraArr[i].y + "px";
			eqDiv.style.left = paraArr[i].x + "px";
			$("#"+this.renderIcoDiv).append(eqDiv);			
			switch(showType){
				case 2://显示别名;
					eqName = aEqData[i].text ? aEqData[i].text : "no name";
					break;
				case 1://显示名称;
					eqName = aEqData[i].sysName ? aEqData[i].sysName : " ";
					break;
				case 0://显示ip;
					eqName = aEqData[i].ip ? aEqData[i].ip : " ";
					break;
			}
			var newEqStr =  Raphael.format(eqStr, paraArr[i].icon, eqName);
			$(eqDiv).append(newEqStr);//将图标加入到上半部div中;
			//绑定事件;
			$(eqDiv).bind("mousedown",this,this.eqMouseDown);
			$(eqDiv).bind("click",this,this.clearBubble);//清除冒泡，点击空白处才不会选择任何图标;	
			
			this.eqDataAll.entity.push(paraArr[i]);
		}
	}
};//end addNewIco;
///////////////////////////////////////////////////////添加新的图标end;
///////////////////////////////////////////////////////添加新的线条start;
Nm3kTopo.prototype.addNewLine = function(paraArr){
	var len = paraArr.length;
	for(var i=0; i<len; i++){//循环传进来的线条数组;
		var flag = false;
		if(this.eqDataAll.link){
			var aLinks = this.eqData.link;	
			for(var j=0; j<aLinks.length; j++){
				//alert(aLinks[j].id +"::::"+ paraArr[i].id)
				if(aLinks[j].id == paraArr[i].id){
					flag = true;
				}
			}
		}
		if(!flag){
			if(!this.eqDataAll.link){this.eqDataAll.link = []};
			var $src = $("#" + this.renderIcoDiv + " .eqContainer[nodeid='"+ paraArr[i].srcEntityId +"']");
			var $dest = $("#" + this.renderIcoDiv + " .eqContainer[nodeid='"+ paraArr[i].destEntityId +"']");
			var pathString = Raphael.format("M{0},{1}L{2},{3}", $src.position().left + this.halfIcoWidth, $src.position().top + this.halfIcoHeight, $dest.position().left + this.halfIcoWidth, $dest.position().top + this.halfIcoHeight);
			var line = this.paper.path(pathString);
			line.id = paraArr[i].id;
			line.attr({"stroke-width": 3,"stroke": "#A1CA6A"});
			var me = this;
			line.click(function(e){
				e.stopPropagation();
				//循环所有线条，将其变成默认颜色;
				var aLines = me.eqDataAll.link;
				for(i=0; i<aLines.length; i++){
					me.paper.getById(aLines[i].id).attr({"stroke": me.lineStroke});
				}
				this.attr({"stroke": me.lineSelectStroke});
				if(me.selectOneLine != null){
					WIN[me.selectOneLine].call(WIN,[this]);
				};
			})
			this.eqDataAll.link.push(paraArr[i]);
		};//end if(!flag);
	}
};//end addNewLine;	
///////////////////////////////////////////////////////添加新的线条end;	
//////////////////////////////////////////////////////删除设备，如果设备有连线，那么也会删除,传入的参数是:设备nodeId的数组;
Nm3kTopo.prototype.removeIcos = function(paraArr){
	var aEqs = this.eqDataAll.entity,
	aLinks = this.eqDataAll.link;
	for(var i=paraArr.length-1; i>=0; i--){
		for(j=aEqs.length; j>=0; j--){//循环所有要移除的设备的nodeId;
			if(paraArr[i] == aEqs[j]){
				this.eqDataAll.entity.splice(j,1);
			}
		}
		for(k=aLinks.length-1; k>=0; k--){//循环所有要移除的link;
			if(paraArr[i] == aLinks[k].srcEntityId || paraArr[i] == aLinks[k].destEntityId){
				this.paper.getById(aLinks[k].id).remove();
				this.eqDataAll.link.splice(k,1);
			}
		}
		$("#" + this.renderIcoDiv + " .eqContainer[nodeid='" + paraArr[i] + "']").remove();
		paraArr.splice(i,1);
	};//end for;
}
	
//hover;	
Nm3kTopo.prototype.eqMouseOver = function(e){
	var leftPos,
	me = e.data,
	$this = $(this),
	bodyW = $("body").width(),
	bodyH = $("#wrap").height(),
	$thisLeft = $this.position().left,
	$thisTop = $this.position().top,
	leftScroll = $("#"+ me.renderTo).scrollLeft(),
	topScroll = $("#" + me.renderTo).scrollTop(),
	showTipW = $("#bubbleTip").outerWidth(),
	showTipH = $("#bubbleTip").outerHeight(),
	$thisW = $this.outerWidth(),
	$thisH = $this.outerHeight();
	var flag = false;
	if(displayCluetip){//如果要显示气泡;
		for(var i=0; i<aBubbleHtml.length; i++){
			//alert($this.attr("id") +":::::"+ aBubbleHtml[i].id)
			if($this.attr("id") == aBubbleHtml[i].id){
				flag = true;
				$("#bubbleTip").html(aBubbleHtml[i].html);
			}
		}
		if(!flag){$("#bubbleTip").html("");}
	
		if(bodyW - $thisLeft + leftScroll - $thisW > showTipW){
			l = $thisLeft + $thisW - leftScroll;
		}else if($thisLeft - leftScroll > showTipW){
			l = $thisLeft - showTipW - leftScroll;
		}else{
			l = $thisLeft + $thisW - leftScroll;
		}
		if(bodyH - $thisTop + topScroll > showTipH){
			t = $thisTop  - topScroll + 66;
		}else if($thisTop - topScroll > showTipH){
			t = bodyH - (bodyH - $thisTop - $thisH + showTipH) + 66 - topScroll; 
		}else{
			t = $thisTop - topScroll + 66;
		}
		if($("#bubbleTip").html().length > 1){//要是一个字都没有，就不显示;
			$("#bubbleTip").css({display:"block", left:l, top:t});
		}
	}
	
	
}
Nm3kTopo.prototype.eqMouseOut = function(e){
	$("#bubbleTip").css({display:"none"});
}

//改变所有线条的宽度;	
Nm3kTopo.prototype.changeAllLineWidth = function(w){
	if(this.eqDataAll.link){
		var aLinks = this.eqDataAll.link;
		for(var i=0; i<aLinks.length; i++){
			var id = aLinks[i].id;
			this.paper.getById(id).attr({
				"stroke-width" : w
			})
		}
	}
};//end changeAllLineWidth;
//改变所有线条的颜色;	
Nm3kTopo.prototype.changeAllLineColor = function(color){
	if(this.eqDataAll.link){
		var aLinks = this.eqDataAll.link;
		for(var i=0; i<aLinks.length; i++){
			var id = aLinks[i].id;
			this.paper.getById(id).attr({
				"stroke" : color
			})
		}
	}
};//end changeAllLineColor;
//有线条透明或者不透明;;	
Nm3kTopo.prototype.changeAllLineVisible = function(bVisible){
	var opacity;
	if(bVisible){
		opacity = 1;
	}else{
		opacity = 0;
	}
	if(this.eqDataAll.link){
		var aLinks = this.eqDataAll.link;
		for(var i=0; i<aLinks.length; i++){
			var id = aLinks[i].id;
			this.paper.getById(id).attr({
				"stroke-opacity" : opacity
			})
		}
	}
};//end changeAllLineVisible;
 
	

