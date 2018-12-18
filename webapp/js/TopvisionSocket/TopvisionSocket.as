package  {
	
	import flash.display.MovieClip;
	import flash.net.Socket;
	import flash.events.Event;
	import flash.events.ProgressEvent;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.external.ExternalInterface;	
	import flash.system.Security;
	
	public class TopvisionSocket extends MovieClip {
		private var socket:Socket;
		private var requesting:Boolean;
		private var handsharkId:String;
		private var recvMessageFrag:String = "";
		
		
		public function TopvisionSocket() {
			// constructor code
			this.socket = new Socket();
			initialize();
			//connect("172.17.2.8",307);
			//sendRequest("spectrumDemo","entityId=33&cmcIndex=22");
		}
		
		//------------------------------
		//		INITIALIZE
		//-------------------------------
		public function initialize():void{
			var param:Object = root.loaderInfo.parameters;
   			handsharkId =  param["handsharkId"];
			socket.addEventListener(Event.CONNECT,onSocketConnected);
            socket.addEventListener(IOErrorEvent.IO_ERROR, onIOError);
            socket.addEventListener(ProgressEvent.SOCKET_DATA , onSocketDataReceived);
			socket.addEventListener(Event.CLOSE,onSocketClosed);
			socket.addEventListener(SecurityErrorEvent.SECURITY_ERROR,onSecurityError);
			
			ExternalInterface.addCallback("sendRequest",sendRequest);
			ExternalInterface.addCallback("send",send);
			ExternalInterface.addCallback("connect",connect);
			ExternalInterface.addCallback("disconnect",disconnect);
			
		}
		
		//-----------------------------------
		//  		CONNECT
		//-----------------------------------
		public function connect(ip:String,port:int):void{
			//Security.loadPolicyFile("xmlsocket://"+ip + ":" + port);
			socket.connect(ip,port);
		}
		
		//-----------------------------------
		//  		DISCONNECT
		//-----------------------------------
		public function disconnect():void{
			requesting = false;
			if(socket.connected){
				socket.close();
			}
		}

		//-----------------------------------
		//  		SEND REQUEST
		//-----------------------------------
		public function sendRequest(req:String,params:String,ignoreRequesting:Boolean):Boolean{
			if(ignoreRequesting){
				
			}else if(requesting){
				return false;
			}
			send("/"+req+"?"+params);
			requesting = true;
			return true;
		}
		
		//-----------------------------------
		//  		SEND MESSAGE
		//-----------------------------------
		public function send(message:String):void{
			socket.writeUTFBytes(message);
			socket.flush();
		}
		
		
		//-----------------------------------
		//			LISTENER
		//-----------------------------------
		public function onSocketConnected(e:Event):void{
			ExternalInterface.call("TopvisionSocket.socket_callback",handsharkId,"onConnect");
		}
		
		public function onIOError(e:IOErrorEvent):void{
			requesting = false;
			ExternalInterface.call("TopvisionSocket.socket_callback",handsharkId,"onError");
		}
		
		
		public function onSocketDataReceived(data:ProgressEvent):void{
			var msg:String;
			msg="";
   			while (socket.bytesAvailable){
    			msg += socket.readUTFBytes(socket.bytesAvailable);
			}
			var loc = msg.lastIndexOf("@");
			if( loc == -1){
				recvMessageFrag += msg;
				return;
			}else if(loc > 0){
				recvMessageFrag += msg.substr(0,loc);
			}
			var $message = recvMessageFrag;
			recvMessageFrag = msg.substr(loc);
			var message = $message.split("@");
			for(var $ in message){
				if(message[$] == "CLOSE_SOCKET"){
					disconnect();
				}else{
					ExternalInterface.call("TopvisionSocket.socket_callback",handsharkId,"onData",message[$]);
				}
			}
			
		}
		
		public function onSocketClosed(e:Event):void{
			requesting = false;
			ExternalInterface.call("TopvisionSocket.socket_callback",handsharkId,"onClose");
		}
		
		public function onSecurityError(e:Event):void{
			requesting = false;
			ExternalInterface.call("TopvisionSocket.socket_callback",handsharkId,"onClose");
		}
		
	}
	
}
