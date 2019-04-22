<?php

class DbOperation
{
    //Database connection link
    private $con;

    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/DbConnect.php';

        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();

        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }

    //storing token in database 
    public function registerDevice($mac_addr,$imei_no,$newtoken){
        if(!$this->isMACexist($mac_addr)){
            $stmt = $this->con->prepare("INSERT INTO sys_devices (mac_addr,imei_no, token) VALUES (?,?,?) ");
            $stmt->bind_param("sss",$mac_addr,$imei_no,$newtoken);
            if($stmt->execute())
                return 0; //return 0 means success
            return 1; //return 1 means failure
        }
        elseif ($this->getTokenByMAC($mac_addr)[0] == $newtoken) { //mac exist 
            return 2; //returning 2 means mac_addr already exist with same token
        }else{
            $stmt = $this->con->prepare("UPDATE sys_devices SET token=? WHERE mac_addr=?");
                $stmt->bind_param("ss",$newtoken,$mac_addr);
                if($stmt->execute())
                    return 3; //returning 3 means Token updated of already registered device
                return 1; //return 1 means failure
        }
    }

    //the method will check if mac_addr already exist 
    private function isMACexist($mac_addr){
        $stmt = $this->con->prepare("SELECT id FROM sys_devices WHERE mac_addr = ?");
        $stmt->bind_param("s",$mac_addr);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    //getting all tokens to send push to all sys_devices
    public function getAllTokens(){
        $stmt = $this->con->prepare("SELECT token FROM sys_devices");
        $stmt->execute(); 
        $result = $stmt->get_result();
        $tokens = array(); 
        while($token = $result->fetch_assoc()){
            array_push($tokens, $token['token']);
        }
        return $tokens; 
    }

    //getting a specified token to send push to selected device
    public function getTokenByMAC($mac_addr){
        $stmt = $this->con->prepare("SELECT token FROM sys_devices WHERE mac_addr = ?");
        $stmt->bind_param("s",$mac_addr);
        $stmt->execute(); 
        $result = $stmt->get_result()->fetch_assoc();
        return array($result['token']);        
    }

    //getting all the registered devices from database 
    public function getAllDevices(){
        $stmt = $this->con->prepare("SELECT * FROM sys_devices");
        $stmt->execute();
        $result = $stmt->get_result();
        return $result; 
    }
}