/***********************************************************************
 * $Id: TopvisionUpload.as,v1.1 2012-11-8 下午15:16:08 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
 
package  {	
	import flash.display.MovieClip;
	import flash.net.FileReference;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.DataEvent;
	import flash.events.ProgressEvent;
	import flash.external.ExternalInterface;	
	import flash.net.URLRequest;
	import flash.events.SecurityErrorEvent;
	import flash.events.IOErrorEvent;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	
	/**
 	* @author Bravin
 	* @created @2012-11-8 下午15:16:08 	
 	*/
	public class TopvisionUpload extends MovieClip {
		private var file:FileReference;
		private var flashElement:String;
	
		//------------------------------
		//		APPLICATION ENTRY
		//-------------------------------
		public function TopvisionUpload() {		
			file = new FileReference();
			_init_();
		}
		
		//------------------------------
		//		INITIALIZE
		//-------------------------------
		public function _init_(){
			//bt.setStyle("background-color")
			bt.alpha = 0
			bt.buttonMode = true;
			var param:Object = root.loaderInfo.parameters;
   			flashElement = param["flashElement"];
			bt.addEventListener(MouseEvent.CLICK,selectFile);
			bt.addEventListener(MouseEvent.MOUSE_OVER,function(){
				setStyle(true)
			})
			bt.addEventListener(MouseEvent.MOUSE_OUT,function(){
				setStyle(false)
			})
			file.addEventListener(Event.SELECT,onSelect);
			file.addEventListener(Event.OPEN,onOpen);
			file.addEventListener(ProgressEvent.PROGRESS,onProgress);
			file.addEventListener(DataEvent.UPLOAD_COMPLETE_DATA,onComplete);
			file.addEventListener(SecurityErrorEvent.SECURITY_ERROR,onSecurityError)
			file.addEventListener(IOErrorEvent.IO_ERROR,onIOError)
			ExternalInterface.addCallback("upload",uploadAction);
			//ExternalInterface.addCallback("download",download);
			ExternalInterface.addCallback("setListen",setListen);
			ExternalInterface.addCallback("load",load);
			//ExternalInterface.addCallback("setFlashElement",setFlashElement);
		}
		
		
		//------------------------------
		//		CHOOSE FILE
		//-------------------------------
		public function selectFile(e:Event){			
			file.browse();
			//download("http://172.17.2.4:3000/images/login_bg.gif")
		}
		
		//---------------------------------------
		//		HANDLE MOUSEOVER,CALL JS HANDLER
		//--------------------------------------
		public function setStyle(overout) {
			//  true : mouseover  false : mouseout
			try{
				ExternalInterface.call("TopvisionUpload.doCallback",flashElement,"setFlashStyle",overout);
			}catch(e){
				trace("setstyle error")
			}			
		}
		
		//------------------------------
		//		SELECT FILE
		//-------------------------------
		public function onSelect(e:Event){			
			ExternalInterface.call("TopvisionUpload.doCallback",flashElement,"onSelect",file);
		}
		
		//------------------------------
		//		UPLOAD/DOWNLOAD START
		//-------------------------------
		public function onOpen(e:Event){
			try{
				ExternalInterface.call("TopvisionUpload.doCallback",flashElement,"onOpen");
			}catch(e){}
		}
		
		//------------------------------------
		//	INVOKE BETEWENN UPLOAD/DOWNLOAD
		//-------------------------------------
		public function onProgress(e:ProgressEvent){
			try{
				ExternalInterface.call("TopvisionUpload.doCallback",flashElement,"onProgress",e.bytesLoaded);
			}catch(e){}
		}

		//------------------------------
		//		WHEN UPLOAD SUCCESSFUL
		//-------------------------------
		public function onComplete(e:DataEvent){
			ExternalInterface.call("TopvisionUpload.doCallback",flashElement,"onComplete",e.data);
			setListen(true);
		}

		//-------------------------------------
		//		always url / server is correct
		//--------------------------------------
		public function onSecurityError(e:SecurityErrorEvent){
			try{
				ExternalInterface.call("TopvisionUpload.doCallback",flashElement,"onSecurityError",e);
			}catch(e){}
		}

		public function onIOError(e:IOErrorEvent){
			try{
				ExternalInterface.call("TopvisionUpload.doCallback",flashElement,"onIOError",e);
			}catch(e){}
		}


		//---------------------------------
		//		UPLOAD INTERFACE
		//---------------------------------
		public function uploadAction(sourceUrl:String,data:Object){			      
			var url:URLRequest = new URLRequest(sourceUrl)
			url.method = URLRequestMethod.POST			
            if(data != null){
				var variables:URLVariables = new URLVariables();
				for(var o in data){
					variables[o] = data[o];
					url.data = variables;
				}
			}
			file.upload(url)
			setListen(false)
		}

		//-----------------------------------
		//		bind/unbind CLICK  INTERFACE
		//------------------------------------
		public function setListen(isListen){
			if(isListen)
				stage.addEventListener(MouseEvent.CLICK,selectFile);
			else
				stage.removeEventListener(MouseEvent.CLICK,selectFile);
		}
		
		//-----------------------------------------
		//		load data for preview purpose
		//-----------------------------------------
		public function load(){
			file.addEventListener(Event.COMPLETE,onLoadData);
			file.load();
		}
		
		//-----------------------------------
		//		file.load compelte callback
		//-----------------------------------
		public function onLoadData(e:Event){
			try{
				var data = new String(file.data)
				ExternalInterface.call("TopvisionUpload.doCallback",flashElement,"onData",data);
			}catch(e){}
		}
		
		
	}
}
