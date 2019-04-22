<?php 
//importing required files 
require_once 'DbOperation.php';
require_once 'Firebase.php';
require_once 'Push.php'; 

$db = new DbOperation();
 
$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){	
	//hecking the required params 
	if(isset($_POST['action'])) {
		
		//creating a new push
		$push = null; 

		$push = new Push($_POST['action'],$_POST['data_string']);

		//getting the push from push object
		$mPushMessage = $push->getPush(); 

		//getting the token from database object 
		$devicetoken = $db->getAllTokens();

		//creating firebase class object 
		$firebase = new Firebase(); 

		//sending push notification and displaying result 
		echo $firebase->send($devicetoken, $mPushMessage);
	}else{
		$response['error']=true;
		$response['message']='Action parameter missing';
	}
}else{
	$response['error']=true;
	$response['message']='Invalid request';
}

echo json_encode($response);