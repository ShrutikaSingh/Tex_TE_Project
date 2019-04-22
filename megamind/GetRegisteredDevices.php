<?php 
 
 require_once 'DbOperation.php';
 //Including the constants.php 
include_once dirname(__FILE__) . '/Config.php';
 
 $db = new DbOperation(); 
  
 $sys_devices = $db->getAllDevices();
 
 $response = array(); 
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
	 if($_POST['server_initials'] == SERVER_INITIALS){
	 
		 $response['error'] = false; 
		 $response['sys_devices'] = array(); 
		 
		 while($device = $sys_devices->fetch_assoc()){
		 $temp = array();
		 $temp['id']=$device['id'];
		 $temp['mac_addr']=$device['mac_addr'];
		 $temp['imei_no']=$device['imei_no'];
		 $temp['token']=$device['token'];
		 array_push($response['sys_devices'],$temp);
		 }
	 } else{
	 $response['error']=true;
	 $response['message']='Server Authentication failure!';
	 }
 } else{
 	$response['error']=true;
	$response['message']='Invalid request';
 }
 echo json_encode($response);
