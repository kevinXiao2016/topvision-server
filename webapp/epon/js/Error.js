					/************************
					 *****ALL ERRORS********
					 ***********************/
	//------interface--------//				
var Throwable = function  () {
  this.time = new Date();
}					
Throwable.prototype = new Error();

//---------------implement----//
var loadingError = function(msg){
	this.type = 'loadingError';
	this.msg = msg;
}					
loadingError.prototype = new Throwable();

var deviceOutofServiceError = function(msg){
	this.type = 'deviceOutofServiceError';
	this.msg = msg;
}					
deviceOutofServiceError.prototype = new Throwable();

var unExpectedJSONError = function(msg){
	this.type = 'unExpectedJSONError';
	this.msg = msg;
}					
unExpectedJSONError.prototype = new Throwable();

var configFailureError = function(msg){
	this.type = 'configFailureError';
	this.msg = msg;
}					
configFailureError.prototype = new Throwable();

var snmpFailedError = function(msg){
	this.type = 'snmpFailedError';
	this.msg = msg;
}					
snmpFailedError.prototype = new Throwable();



//------------Events-------//
var observer = function(){
	
}
observer.prototype = new Event();
