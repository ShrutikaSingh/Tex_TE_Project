<?php 
//importing required files 
require_once 'DbOperation.php';
require_once 'Firebase.php';
require_once 'Push.php'; 
//Including the constants.php 
include_once dirname(__FILE__) . '/Config.php';

$db = new DbOperation();

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){
	if($_POST['server_initials'] == SERVER_INITIALS){	
		//checking the required params 
		if(isset($_POST['action']) and isset($_POST['mac_addr'])){

			//creating a new push
			$push = null; 

			$push = new Push($_POST['action'],$_POST['data_string']);

			//getting the push from push object
			$mPushMessage = $push->getPush(); 

			//getting the token from database object 
			$devicetoken = $db->getTokenByMAC($_POST['mac_addr']);

			//creating firebase class object 
			$firebase = new Firebase(); 

			//sending push notification and displaying result 
			echo $firebase->send($devicetoken, $mPushMessage);
		}else{
			$response['error']=true;
			$response['message']='mac address or action or both are missing';
		}
	}else{
	$response['error']=true;
	$response['message']='Server authentication failure!';
	}
}else{
	$response['error']=true;
	$response['message']='Invalid request';
}

echo json_encode($response);
