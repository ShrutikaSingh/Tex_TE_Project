<?php 
	require_once 'DbOperation.php';
	require_once 'Server.php';
	$response = array(); 

	if($_SERVER['REQUEST_METHOD']=='POST'){

		$token = $_POST['token'];
		$mac_addr = $_POST['mac_addr'];
		$imei_no = $_POST['imei_no'];
		$deviceInfo = $_POST['deviceInfo'];

		$db = new DbOperation(); 

		$result = $db->registerDevice($mac_addr,$imei_no,$token);

		if($result == 0){
			$response['error'] = false; 
			$response['message'] = 'Device registered successfully';

			$myfile = fopen($mac_addr."_logs", "a+");
			fclose($myfile);
			$myfile = fopen($mac_addr."_info", "w");
			file_put_contents($mac_addr."_info", $deviceInfo);
			fclose($myfile);

			$serverUpdate = new Server($mac_addr,$deviceInfo);
			$serverUpdate->update("newdevice");


		}elseif($result == 2){
			$response['error'] = true; 
			$response['message'] = 'Device already registered';
		}elseif($result == 3){
			$response['error'] = false; 
			$response['message'] = 'Token updated of already registered device';
		}else{
			$response['error'] = true;
			$response['message']='Device not registered';
		}
	}else{
		$response['error']=true;
		$response['message']='Invalid Request...';
	}

	echo json_encode($response);