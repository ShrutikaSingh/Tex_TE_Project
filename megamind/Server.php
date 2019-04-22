<?php

/**
* Class for server app
*/
//Including the constants.php 
include_once dirname(__FILE__) . '/Config.php';
require_once 'Firebase.php';

class Server
{
	
	//data to be sent 
	private $mac_addr;
    private $data_string;

    //initializing values in this constructor
    function __construct($mac_addr,$data_string) {
    	 $this->mac_addr = $mac_addr;
         $this->data_string = $data_string;
    }

	public function update($type){

		$message = array();
		$message['data']['mac_addr'] = $this->mac_addr;
		$message['data']['type'] = $type;
        $message['data']['data_string'] = $this->data_string;
        $devicetoken = array(SERVER_TOKEN);

        //creating firebase class object 
		$firebase = new Firebase(); 

		//sending push notification and displaying result 
		$firebase->send($devicetoken, $message);

	}
}