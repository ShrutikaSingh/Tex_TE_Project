<?php 

require_once 'Server.php';
$resopnse = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
	if (isset($_POST['mac_addr']) and isset($_POST['data_string'])) {
		
		$mac_addr = $_POST['mac_addr'];
		$data_string = $_POST['data_string'];

		//creating a file with mac address name
		$myfile = fopen($mac_addr."_logs", "a+");
		$old_data = file_get_contents($mac_addr."_logs");
		fclose($myfile);

		if($old_data == "")
			file_put_contents($mac_addr."_logs", $data_string);
		else
			file_put_contents($mac_addr."_logs", $data_string.",".$old_data);

		$serlog = new Server($mac_addr,$data_string);
		$serlog->update("log");

		$response['error'] = false;
		$response['message'] = "Log Updated.";


	} else {
		$response['error']=true;
		$response['message']='Parameters missing!';
	}
	
} else {
	$response['error']=true;
	$response['message']='Invalid request';
}

echo json_encode($response);