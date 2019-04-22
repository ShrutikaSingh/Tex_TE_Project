<?php 

class Push {

    //Action to be performed
    private $action;
    //data to be sent 
    private $data_string;

    //initializing values in this constructor
    function __construct($action, $data_string) {
         $this->action = $action;
         $this->data_string = $data_string;
    }
    
    //getting the push notification
    public function getPush() {
        $res = array();
        $res['data']['action'] = $this->action;
        $res['data']['data_string'] = $this->data_string;
        return $res;
    }
 
}
