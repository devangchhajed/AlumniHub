<?php

/*
        function send_notification ($tokens, $message)
        {
                $url = 'https://fcm.googleapis.com/fcm/send';
                $fields = array(
                         'registration_ids' => $tokens,
                         'data' => $message,
                        );
                $headers = array(
                        'Authorization:key=  AAAAs-nl7Ao:APA91bEMfM8jO0gazvYzJdZKZGKqRzKUJGgY9L5xtCzSsD4OM2MjvY27Bsofqf8OzyWe5Mx2UPGZL0EEsFNADlf9cZ6Luj1AA6DkzVad8LUb_RPGU8LzJUhk3Q_zYJHe71QdzwrkI3XI',
                        'Content-Type: application/json'
                        );
           $ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, $url);
       curl_setopt($ch, CURLOPT_POST, true);
       curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
       curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       $result = curl_exec($ch);
       if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       }
       curl_close($ch);
       return $result;
        }


/*        $conn = mysqli_connect("connection set-up");
        $sql = "Select FirebaseID From Coordinates";
        $result = mysqli_query($conn,$sql);
        $tokens = array();
//var_dump(result);
        if(mysqli_num_rows($result) > 0 ){
                while ($row = mysqli_fetch_assoc($result)) {
                        $tokens[] = $row["FirebaseID"];
                }
        }
		
		
  */
/*
$tokens=array();
$tokens[]="cK9LBScYR-M:APA91bGydQde9rPvwwhN1LEKwobYl80PbtOo8P0a2p7VV4Wy66mVWFaLvw3SRFI_sexGLkXdVUy14JygxCSxWB2LkYjUEcrUy6puWILWEeKcsFTb-Wpv5_rDdOUiNDdN0_Ap7_fHhxOi";

  var_dump($tokens);

    //    mysqli_close($conn);
        $message = array("message" => "Hello World");
        $message_status = send_notification($tokens, $message);
        echo $message_status;
		
		
		
		
		
		*/


// API access key from Google API's Console
define('API_ACCESS_KEY', 'AAAAs-nl7Ao:APA91bEMfM8jO0gazvYzJdZKZGKqRzKUJGgY9L5xtCzSsD4OM2MjvY27Bsofqf8OzyWe5Mx2UPGZL0EEsFNADlf9cZ6Luj1AA6DkzVad8LUb_RPGU8LzJUhk3Q_zYJHe71QdzwrkI3XI');
$registrationIds = array();
$registrationIds[] = "fVGPbV8clOg:APA91bFREvpZ6OQ5-RgnjtO9hZJKlJHmTwazvfh5eTe0Oi2oPkMnrR7ZhPg_5pgWQ74FiIOQzN1aGN1cUvSS4EuxfcxlWfTZO3fHB_zf0nbI3q5J8iT9_KTjgEp7l0go5XEaZYRc2VSm";
// prep the bundle
$msg = array
(
    'message' => 'here is a message. message',
    'title' => 'This is a title. title',
    'subtitle' => 'This is a subtitle. subtitle',
    'tickerText' => 'Ticker text here...Ticker text here...Ticker text here',
    'vibrate' => 1,
    'sound' => 1,
    'largeIcon' => 'large_icon',
    'smallIcon' => 'small_icon'
);
$fields = array
(
    'registration_ids' => $registrationIds,
    'data' => $msg
);

$headers = array
(
    'Authorization: key=' . API_ACCESS_KEY,
    'Content-Type: application/json'
);

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL,
    'https://fcm.googleapis.com/fcm/send');
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
$result = curl_exec($ch);
curl_close($ch);
echo $result;
?> 