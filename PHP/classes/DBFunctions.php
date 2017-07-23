<?php
date_default_timezone_set('Asia/Kolkata');

/**
 * Created by PhpStorm.
 * User: Devang
 * Date: 12/31/2016
 * Time: 11:06 AM
 */
class DBFunctions
{

    private $conn;

    // constructor
    function __construct($db)
    {
        $this->conn = $db;
    }

    /*
     *
     * Support Management
     * Author : Devang Chhajed
     *
     */

    public function registerUserFeedback($id, $feedback)
    {

        try {
            $timestamp = (new DateTime())->getTimestamp();
            $stmt = $this->conn->prepare("INSERT INTO feedback(user_id, feedback, timestamp) VALUES(:id, :feedback, :time)");
            //        $stmt->bind_param("ssssss", $name, $email, $batch, $institute, $imgurl);
            $stmt->bindparam(":id", $id);
            $stmt->bindparam(":feedback", $feedback);
            $stmt->bindparam(":time", $timestamp);
            $result = $stmt->execute();

            if ($result == false) {
                $response['success'] = false;
                $response['message'] = "Error from registerUserFeedback";

            } else {
                $response['success'] = true;
                $response['message'] = "Recieved User Feedback";
            }
        } catch (PDOException $e) {
            $response['success'] = false;
            $response['message'] = "Error from register User : " . $e;
        }
        return json_encode($response);
    }

    public function initiateLogin($email, $fcm)
    {
        $user = $this->getUserByEmail($email);
        $response = array();

        if ($user == false) {
            $response['success'] = false;
            $response['message'] = "User not Registered.";

        } else {
            $response['success'] = true;
            $response['message'] = "User Found for " . $user['email'];
            $response['user_id'] = $user['id'];
            $response['email'] = $user['email'];
            $response['name'] = $user['name'];
            $response['batch'] = $user['batch'];
            $response['institute'] = $user['institute'];
            $response['dob'] = $user['dob'];
            $response['phone'] = $user['phone'];
            $response['company'] = $user['company'];
            $response['location'] = $user['location'];
            $response['user_img'] = $user['imgurl'];

            try {
                $stmt = $this->conn->prepare("Update users set fcm_id = :fcm where email = :email");
                //        $stmt->bind_param("ssssss", $name, $email, $batch, $institute, $imgurl);
                $stmt->bindparam(":fcm", $fcm);
                $stmt->bindparam(":email", $email);
                $stmt->execute();
            }catch (PDOException $e) {
            }
        }
        return json_encode($response);

    }

    public function getUserByEmail($email)
    {

        try {

            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = :email");
            $stmt->bindparam(":email", $email);

            if ($stmt->execute()) {
                $user = $stmt->fetch(PDO::FETCH_ASSOC);
                return $user;
            } else {
                return false;
            }
        } catch (PDOException $e) {
            return false;
        }

    }

    /*
     *
     * User Management
     * Author : Devang Chhajed
     *
     */

    public function registerUser($name, $email, $batch, $institute, $dob, $phone, $company, $location, $imgurl, $fcmid)
    {
        $user = $this->getUserByEmail($email);
        try {

            $response = array();
            if ($user == false) {
                $timestamp = (new DateTime())->getTimestamp();
                $stmt = $this->conn->prepare("INSERT INTO users(name, email, batch, institute, dob, phone, company, location, imgurl, fcm_id, created_at) VALUES(:name, :email, :batch, :institute, :dob, :phone, :company, :location, :imgurl, :fcmid, :datetime)");
                //        $stmt->bind_param("ssssss", $name, $email, $batch, $institute, $imgurl);
                $stmt->bindparam(":name", $name);
                $stmt->bindparam(":email", $email);
                $stmt->bindparam(":batch", $batch);
                $stmt->bindparam(":institute", $institute);
                $stmt->bindparam(":dob", $dob);
                $stmt->bindparam(":phone", $phone);
                $stmt->bindparam(":company", $company);
                $stmt->bindparam(":location", $location);
                $stmt->bindparam(":imgurl", $imgurl);
                $stmt->bindparam(":fcmid", $fcmid);
                $stmt->bindparam(":datetime", $timestamp);

            } else {
                $stmt = $this->conn->prepare("Update users set batch=:batch, institute= :institute, dob = :dob, phone = :phone, company = :company, location = :location, fcm_id= :fcmid where email=:email");
                //        $stmt->bind_param("ssssss", $name, $email, $batch, $institute, $imgurl);
                $stmt->bindparam(":batch", $batch);
                $stmt->bindparam(":institute", $institute);
                $stmt->bindparam(":dob", $dob);
                $stmt->bindparam(":phone", $phone);
                $stmt->bindparam(":company", $company);
                $stmt->bindparam(":location", $location);
                $stmt->bindparam(":fcmid", $fcmid);
                $stmt->bindparam(":email", $email);

            }
            $stmt->execute();


            $user = $this->getUserByEmail($email);

            if ($user == false) {
                $response['success'] = false;
                $response['message'] = "Error from register User";
                $response['user_id'] = "";
                $response['user_img'] = "";

            } else {
                $response['success'] = true;
                $response['message'] = "User Found";
                $response['user_id'] = $user['id'];
                $response['fcm_id'] = $user['fcm_id'];
                $response['user_img'] = $user['imgurl'];
            }
        } catch (PDOException $e) {
            $response['success'] = false;
            $response['message'] = "Error from register User : " . $e;
            $response['user_id'] = "";
            $response['user_img'] = "";

        }

        return json_encode($response);


    }

    public function updateUserDisplayPicture($id, $img)
    {

        $file_upload_url = 'http://codejuris.com/BITConnect/TEst/';
        $image = $this->uploadUserImage($id, $img);
        $image = $file_upload_url . $image;

        $response = array();

        try {
            $stmt = $this->conn->prepare("Update users set imgurl= :imgurl where id=:id");
            //        $stmt->bind_param("ssssss", $name, $email, $batch, $institute, $imgurl);
            $stmt->bindparam(":imgurl", $image);
            $stmt->bindparam(":id", $id);
            $result = $stmt->execute();


            if ($result == true) {
                $response['success'] = true;
                $response['message'] = "Upload Successful";
                $response['imgurl'] = $image;

            } else {
                $response['success'] = false;
                $response['message'] = "Upload Failed";
                $response['imgurl'] = "";
            }
        } catch (PDOException $e) {
            $response['success'] = false;
            $response['message'] = "Upload Failed : " . $e;
            $response['imgurl'] = "";

        }

        return json_encode($response);


    }

    public function uploadUserImage($id, $image)
    {

        $name = $this->random_string(20) . ".jpg";


        $path = "./images/userpics/" . $name;

        if ($this->base64_to_jpeg($image, $path)) {
            return $path;
        }
    }

    function random_string($length)
    {
        $key = '';
        $keys = array_merge(range(0, 9), range('a', 'z'));

        for ($i = 0; $i < $length; $i++) {
            $key .= $keys[array_rand($keys)];
        }

        return $key;
    }

    function base64_to_jpeg($base64_string, $output_file)
    {
        $ifp = fopen($output_file, "wb");

        $data = explode(',', $base64_string);

        fwrite($ifp, base64_decode($base64_string));
        fclose($ifp);

        return true;
    }

    public function updateFcmId($id, $fcm)
    {

        try {
            $stmt = $this->conn->prepare("Update users set fcm_id= :fcm where id=:id");
            $stmt->bindparam(":fcm", $fcm);
            $stmt->bindparam(":id", $id);
            $result = $stmt->execute();

            if ($result == true) {
                $response['success'] = true;
                $response['message'] = "FCM Registered";
                $response['fcmid'] = $fcm;

            } else {
                $response['success'] = false;
                $response['message'] = "FCM Update Failed";
                $response['fcmid'] = "";
            }
        } catch (PDOException $e) {
            $response['success'] = false;
            $response['message'] = "FCM Update Failed : " . $e;
            $response['fcmid'] = "";
        }
        return json_encode($response);
    }

    public function registerImageReport($imgurl, $uname, $status, $uid, $ureport)
    {

        try {
            $stmt = $this->conn->prepare("INSERT INTO report(imgurl, uname, status, uid, report) VALUES(:imgurl, :uname, :status, :uid, :ureport)");
            $stmt->bindparam(":imgurl", $imgurl);
            $stmt->bindparam(":uname", $uname);
            $stmt->bindparam(":status", $status);
            $stmt->bindparam(":uid", $uid);
            $stmt->bindparam(":ureport", $ureport);
            $result = $stmt->execute();

            if ($result == false) {
                $response['success'] = false;
                $response['message'] = "Error from ReportingMech";

            } else {
                $response['success'] = true;
                $response['message'] = "Recieved Report";
            }
        } catch (PDOException $e) {
            $response['success'] = false;
            $response['message'] = "Error from ReportMech : " . $e;
        }
        return json_encode($response);
    }

    public function getCommentByPostID($id)
    {

        try {

            $stmt = $this->conn->prepare("SELECT * FROM comments WHERE post_id = :id and published=1");
            $stmt->bindparam(":id", $id);

            $user = $stmt->execute();
            if ($user) {

                return $user;
            } else {
                return false;
            }
        } catch (PDOException $e) {
            return false;
        }
    }

    public function getFullPost($post, $user)
    {


        try {
            $userData = array();
            $rowfinal = array();
            $row = $this->getPostByID($post);

            $userlike = $this->getUserLikeByID($user, $post);
            $user = $this->getUserByID($row['user_id']);
            $rowfinal['id'] = (int)$row['post_id'];
            $rowfinal['userid'] = (int)$row['user_id'];
            $rowfinal['name'] = $user['name'];
            $rowfinal['image'] = $row['image'];
            $rowfinal['likes_count'] = $row['likes_count'];
            $rowfinal['comments_count'] = $row['comments_count'];
            $rowfinal['status'] = $row['status'];
            $rowfinal['userlike'] = $userlike;
            $rowfinal['profilePic'] = $user['imgurl'];
            $rowfinal['timeStamp'] = $row['timestamp'];
            $rowfinal['url'] = $row['url'];

            $userData['post'][] = $rowfinal;


            $stmt1 = $this->conn->prepare("SELECT * FROM comments where post_id = :id and published=1");
            $stmt1->bindValue(":id", (int)$post, PDO::PARAM_INT);

            if ($stmt1->execute()) {
                $rowfinal1 = array();

                while ($row1 = $stmt1->fetch(PDO::FETCH_ASSOC)) {
                    $user = $this->getUserByID($row1['user_id']);

                    $rowfinal1['id'] = (int)$row1['c_id'];
                    $rowfinal1['userid'] = (int)$row1['user_id'];
                    $rowfinal1['userimg'] = $user['imgurl'];
                    $rowfinal1['username'] = $user['name'];
                    $rowfinal1['comment'] = $row1['comment'];
                    $rowfinal1['timeStamp'] = $row1['timestamp'];

                    $userData['comment'][] = $rowfinal1;
                }
            }

            return json_encode($userData);

        } catch (Exception $e) {
            return $e;
        }


//        $comment=$this->getCommentByPostID($post);
//
//        $userData = array();
//        $rowfinal1 = array();
//
//        while ($row = $comment->fetch(PDO::FETCH_ASSOC)) {
//            $user = $this->getUserByID($row['user_id']);
//            $rowfinal1['id'] = (int)$row['c_id'];
//            $rowfinal1['userid'] = (int)$row['user_id'];
//            $rowfinal1['post_id'] = $user['post_id'];
//            $rowfinal1['comment'] = $row['comment'];
//            $rowfinal1['timeStamp'] = $row['timestamp'];
//
//            $userData['comment'][] = $rowfinal1;
//        }
//
//        return json_encode($userData);

    }

    public function getPostByID($id)
    {

        try {

            $stmt = $this->conn->prepare("SELECT * FROM posts WHERE post_id = :id");
            $stmt->bindparam(":id", $id);

            if ($stmt->execute()) {
                $user = $stmt->fetch(PDO::FETCH_ASSOC);
                return $user;
            } else {
                return false;
            }
        } catch (PDOException $e) {
            return false;
        }
    }


    /*
 *
 * Wall Post Management
 * Author : Devang Chhajed
 *
 */

    public function getUserLikeByID($user, $post)
    {

        try {

            $stmt = $this->conn->prepare("SELECT * FROM likes WHERE user_id = :user and post_id=:post");
            $stmt->bindparam(":user", $user);
            $stmt->bindparam(":post", $post);

            if ($stmt->execute()) {
                $user = $stmt->fetch(PDO::FETCH_ASSOC);
                if ($user != null)
                    return $user['likeboolean'];
                else
                    return 2;

            } else {
                return 3;
            }
        } catch (PDOException $e) {
            return false;
        }
    }

    public function getUserByID($email)
    {

        try {

            $stmt = $this->conn->prepare("SELECT * FROM users WHERE id = :email");
            $stmt->bindparam(":email", $email);

            if ($stmt->execute()) {
                $user = $stmt->fetch(PDO::FETCH_ASSOC);
                return $user;
            } else {
                return false;
            }
        } catch (PDOException $e) {
            return false;
        }
    }

    public function deleteWallFeed($id)
    {

        $result = array();

        try {
            $stmt = $this->conn->prepare("INSERT INTO deletepost SELECT p.* FROM posts p where post_id = :id");
            $stmt->bindparam(":id", $id);


            if ($stmt->execute()) {

                $stmt1 = $this->conn->prepare("DELETE FROM posts where post_id = :id1");
                $stmt1->bindparam(":id1", $id);
                $stmt1->execute();

                if ($stmt1->execute()) {
                    $result['success'] = true;
                    $result['message'] = "postdeletedsuccessfully";
                }

            } else {
                $result['success'] = false;
                $result['message'] = "Errror Deleting Post";
            }

        } catch (PDOException $e) {
            $result['success'] = false;
            $result['message'] = "Errror Deleting Post " . $e;
        }


        echo json_encode($result);

    }

    public function fetchPost($offset, $userid, $batch, $institute)
    {
        $stmt = $this->conn->prepare("SELECT * FROM posts ORDER By post_id desc Limit 10 offset :off");
        $stmt->bindValue(":off", (int)$offset * 10, PDO::PARAM_INT);

        $stmt->execute();

        $userData = array();
        $rowfinal = array();

        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $userlike = $this->getUserLikeByID($userid, $row['post_id']);
            $user = $this->getUserByID($row['user_id']);
            $rowfinal['id'] = (int)$row['post_id'];
            $rowfinal['userid'] = (int)$row['user_id'];
            $rowfinal['name'] = $user['name'];
            $rowfinal['image'] = $row['image'];
            $rowfinal['likes_count'] = $row['likes_count'];
            $rowfinal['comments_count'] = $row['comments_count'];
            $rowfinal['status'] = $row['status'];
            $rowfinal['userlike'] = $userlike;
            $rowfinal['profilePic'] = $user['imgurl'];
            $rowfinal['timeStamp'] = $row['timestamp'];
            $rowfinal['url'] = $row['url'];


            $userData['feed'][] = $rowfinal;
        }

        echo json_encode($userData);

    }

    public function fetchProfilePost($id, $offset)
    {
        $userData = array();
        $rowfinal = array();
        $rowfinal2 = array();

        if ($offset == 0) {
            $user = $this->getUserByID($id);
            $rowfinal2['name'] = $user['name'];
            $rowfinal2['batch'] = $user['batch'];
            $rowfinal2['institute'] = $user['institute'];
            $rowfinal2['image'] = $user['imgurl'];

            $userData['user'][] = $rowfinal2;
        }

        $stmt = $this->conn->prepare("SELECT * FROM posts where user_id= :id ORDER By post_id desc Limit 10 offset :off");
        $stmt->bindValue(":id", (int)$id, PDO::PARAM_INT);
        $stmt->bindValue(":off", (int)$offset * 10, PDO::PARAM_INT);

        $stmt->execute();


        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $user = $this->getUserByID($row['user_id']);
            $rowfinal['id'] = (int)$row['post_id'];
            $rowfinal['userid'] = (int)$row['user_id'];
            $rowfinal['name'] = $user['name'];
            $rowfinal['image'] = $row['image'];
            $rowfinal['status'] = $row['status'];
            $rowfinal['profilePic'] = $user['imgurl'];
            $rowfinal['timeStamp'] = $row['timestamp'];
            $rowfinal['url'] = $row['url'];


            $userData['feed'][] = $rowfinal;
        }

        echo json_encode($userData);

    }

    public function fetchSubjectList($batch, $institute)
    {

        $stmt = $this->conn->prepare("SELECT * FROM subjectclasslist WHERE batch = :batch AND institute= :institute");
        $stmt->bindValue(":batch", $batch, PDO::PARAM_STR);
        $stmt->bindValue(":institute", $institute, PDO::PARAM_STR);

        $stmt->execute();


        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $subcode = $row['subjectcode'];
            $stmt1 = $this->conn->prepare("SELECT * FROM subjectlist WHERE subjectcode= :subcode");
            $stmt1->bindValue(":subcode", $subcode, PDO::PARAM_STR);
            $stmt1->execute();

            $subject = $stmt1->fetch(PDO::FETCH_ASSOC);

            $rowfinal['id'] = (int)$row['id'];
            $rowfinal['subjectcode'] = $row['subjectcode'];
            $rowfinal['subjectname'] = $subject['subjectname'];

            $userData['feed'][] = $rowfinal;
        }

        echo json_encode($userData);

    }

    public function postWallFeed($id, $status, $uimage)
    {
        $image = null;
        if ($uimage != "null") {
            $file_upload_url = 'http://codejuris.com/BITConnect/TEst/';
            $image = $this->uploadImage($uimage);
            $image = $file_upload_url . $image;


        }

        $url = $this->extractUrl($status);


        $timestamp = time();
        $response = array();


        try {
            $stmt = $this->conn->prepare("INSERT INTO posts(user_id, image, status, timestamp, url) VALUES(:id, :image, :status, :timestamp, :url)");
            //        $stmt->bind_param("ssssss", $name, $email, $batch, $institute, $imgurl);
            $stmt->bindValue(":id", (int)$id, PDO::PARAM_INT);
            $stmt->bindparam(":image", $image);
            $stmt->bindparam(":status", $status);
            $stmt->bindparam(":timestamp", $timestamp);
            $stmt->bindparam(":url", $url);

            $result = $stmt->execute();

            if (!$result === true) {
                $response['success'] = false;
                $response['message'] = "Error from register User";

            } else {
                $response['success'] = true;
                $response['message'] = "Status Posted";
            }
        } catch (PDOException $e) {
            $response['success'] = false;
            $response['message'] = "Error from Posting Status : " . $e;
        }

        return json_encode($response);

    }

    public function uploadImage($image)
    {

        $name = $this->random_string(20) . ".jpg";

// Path to move uploaded files
        $target_path = "uploads/";

// array for final json respone
        $response = array();

// getting server ip address
        $server_ip = gethostbyname(gethostname());

// final file url that is being uploaded
//        $file_upload_url = 'http://' . $server_ip . '/' . 'images' . '/' . $target_path;


        $path = "./images/uploads/" . $name;
//        $start=strpos($image, "/9");
//
//        $image= substr($image,$start);


        if ($this->base64_to_jpeg($image, $path)) {
            return $path;
        }
    }

    function extractUrl($status)
    {

        preg_match_all('#\bhttps?://[^,\s()<>]+(?:\([\w\d]+\)|([^,[:punct:]\s]|/))#', $status, $match);

        if (empty($match[0]))
            return null;
        else
            return $match[0][0];

    }

    public function fetchNotificationList($id, $batch, $institute)
    {

        $stmt = $this->conn->prepare("SELECT * FROM notifications WHERE batch = :batch OR institute= :institute OR userid= :userid OR scope = 1  ORDER By n_id desc Limit 10");
        $stmt->bindValue(":batch", $batch, PDO::PARAM_STR);
        $stmt->bindValue(":institute", $institute, PDO::PARAM_STR);
        $stmt->bindValue(":userid", $id, PDO::PARAM_INT);

        $stmt->execute();


        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $user = $this->getUserByID($row['userid']);

            $rowfinal['userpic'] = $user['imgurl'];
            $rowfinal['username'] = $user['imgname'];
            $rowfinal['userid'] = $row['userid'];
            $rowfinal['n_id'] = (int)$row['n_id'];
            $rowfinal['title'] = $row['title'];
            $rowfinal['body'] = $row['body'];
            $rowfinal['scope'] = $row['scope'];
            $rowfinal['batch'] = $row['batch'];
            $rowfinal['institute'] = $row['institute'];
            $rowfinal['image'] = $row['image'];
            $rowfinal['created_at'] = $row['created_at'];

            $userData['feed'][] = $rowfinal;
        }

        echo json_encode($userData);

    }


    /*
     * Post Management
     *
     *
     *
     */

    public function likeStatusPost($postid, $userid)
    {
        $timestamp = time();
        $response = array();
        $postuserid = "";

        try {


            $stmt = null;

            if ($this->getUserLikeByID($userid, $postid) == 2) {

                $stmt = $this->conn->prepare("INSERT INTO likes(post_id, user_id, likeboolean,  timestamp) VALUES(:postid, :userid, 1, :timestamp)");
                $stmt->bindValue(":postid", (int)$postid, PDO::PARAM_INT);
                $stmt->bindValue(":userid", (int)$userid, PDO::PARAM_INT);
                $stmt->bindparam(":timestamp", $timestamp);

            } else {
                $stmt = $this->conn->prepare("UPDATE likes SET likeboolean = 1 where user_id=:user and post_id=:post");
                $stmt->bindValue(":user", (int)$userid, PDO::PARAM_INT);
                $stmt->bindValue(":post", (int)$postid, PDO::PARAM_INT);

            }


            if ($stmt->execute()) {


                $stmt1 = $this->conn->prepare("SELECT count(*) as tnum FROM likes WHERE post_id = :id and likeboolean=1");
                $stmt1->bindparam(":id", $postid);

                if ($stmt1->execute()) {
                    $post = $stmt1->fetch(PDO::FETCH_ASSOC);
                    $like = $post['tnum'];
                    $postuserid = $post['user_id'];
                    $stmt2 = $this->conn->prepare("UPDATE posts SET likes_count = :likes where post_id = :postid");
                    $stmt2->bindValue(":likes", (int)$like, PDO::PARAM_INT);
                    $stmt2->bindValue(":postid", (int)$postid, PDO::PARAM_INT);

                    if ($stmt2->execute()) {
                        $response['success'] = true;
                        $response['message'] = "Post Likes Like = " . $like;

                    }
                } else {
                    return false;
                }


            } else {
                $response['success'] = false;
                $response['message'] = "Error from Like Status, Like = " . $like;
            }
        } catch (PDOException $e) {
            $response['success'] = false;
            $response['message'] = "Error from like Status : " . $e;
        }

//        $stmt = $this->conn->prepare("SELECT * FROM users WHERE id = :id");
//
//        $stmt->bindparam(":id", $postuserid);
//        $stmt->execute();
//        $fcm_user=$post['fcm_id'];
//
//        $stmt->bindparam(":id", $userid);
//        $stmt->execute();
//        $sendername=$post['name'];
//            $this->sendToUser($fcm_user, $sendername." liked your status.");

        return json_encode($response);

    }

    public function unlikeStatusPost($postid, $userid)
    {
        $timestamp = time();
        $response = array();


        try {


            $stmt = $this->conn->prepare("UPDATE likes SET likeboolean=0 where post_id= :postid and user_id= :userid");
            $stmt->bindValue(":postid", (int)$postid, PDO::PARAM_INT);
            $stmt->bindValue(":userid", (int)$userid, PDO::PARAM_INT);


            if ($stmt->execute()) {

                $stmt1 = $this->conn->prepare("SELECT count(*) as tnum FROM likes WHERE post_id = :id and likeboolean=1");
                $stmt1->bindparam(":id", $postid);

                if ($stmt1->execute()) {
                    $post = $stmt1->fetch(PDO::FETCH_ASSOC);
                    $like = $post['tnum'];
                    $postuserid = $post['user_id'];
                    $stmt2 = $this->conn->prepare("UPDATE posts SET likes_count = :likes where post_id = :postid");
                    $stmt2->bindValue(":likes", (int)$like, PDO::PARAM_INT);
                    $stmt2->bindValue(":postid", (int)$postid, PDO::PARAM_INT);

                    if ($stmt2->execute()) {
                        $response['success'] = true;
                        $response['message'] = "Post DisLikes";

                    }
                } else {
                    return false;
                }


            } else {
                $response['success'] = false;
                $response['message'] = "Error from DislikeLike Status";
            }
        } catch (PDOException $e) {
            $response['success'] = false;
            $response['message'] = "Error from dislike Status : " . $e;
        }

        return json_encode($response);

    }

    public function commentStatusPost($postid, $userid, $comment)
    {
        $timestamp = time();
        $response = array();


        try {

            $stmt = $this->conn->prepare("SELECT * FROM posts WHERE post_id = :id");
            $stmt->bindparam(":id", $postid);

            if ($stmt->execute()) {
                $post = $stmt->fetch(PDO::FETCH_ASSOC);
                $like = $post['comments_count'] + 1;
                $stmt = $this->conn->prepare("UPDATE posts SET comments_count = :likes where post_id = :postid");
                $stmt->bindValue(":likes", (int)$like, PDO::PARAM_INT);
                $stmt->bindValue(":postid", (int)$postid, PDO::PARAM_INT);

                $result = $stmt->execute();
            } else {
                return false;
            }


            $stmt = $this->conn->prepare("INSERT INTO comments(post_id, user_id, comment, published,  timestamp) VALUES(:postid, :userid, :comment, 1, :timestamp)");
            $stmt->bindValue(":postid", (int)$postid, PDO::PARAM_INT);
            $stmt->bindValue(":userid", (int)$userid, PDO::PARAM_INT);
            $stmt->bindparam(":comment", $comment);
            $stmt->bindparam(":timestamp", $timestamp);

            $result = $stmt->execute();

            if (!$result === true) {
                $response['success'] = false;
                $response['message'] = "Error from Comment post";

            } else {
                $response['success'] = true;
                $response['message'] = " Comment posted";
            }
        } catch (PDOException $e) {
            $response['success'] = false;
            $response['message'] = "Error from  Comment post : " . $e;
        }

        return json_encode($response);

    }

    public function deleteCommentStatusPost($postid, $commentid)
    {
        $response = array();

        try {

            $stmt = $this->conn->prepare("SELECT * FROM posts WHERE post_id = :id");
            $stmt->bindparam(":id", $postid);

            if ($stmt->execute()) {
                $post = $stmt->fetch(PDO::FETCH_ASSOC);
                $like = $post['comments_count'] - 1;
                $stmt = $this->conn->prepare("UPDATE posts SET comments_count = :likes where post_id = :postid");
                $stmt->bindValue(":likes", (int)$like, PDO::PARAM_INT);
                $stmt->bindValue(":postid", (int)$postid, PDO::PARAM_INT);

                $result = $stmt->execute();
            } else {
                return false;
            }


            $stmt = $this->conn->prepare("Update comments set published= 0 where c_id=:cid");
            $stmt->bindValue(":cid", (int)$commentid, PDO::PARAM_INT);

            $result = $stmt->execute();

            if (!$result === true) {
                $response['success'] = false;
                $response['message'] = "Error from Delete Comment";

            } else {
                $response['success'] = true;
                $response['message'] = "Comment deleted";
            }
        } catch (PDOException $e) {
            $response['success'] = false;
            $response['message'] = "Error from Delete Comment : " . $e;
        }

        return json_encode($response);

    }


    /*
     * Search Functions
     */


    public function searchWallFeed($key, $userid, $batch, $institute)
    {
        $key = "%" . $key . "%";
        $stmt = $this->conn->prepare("SELECT * FROM posts where status LIKE :key OR ");
        $stmt->bindValue(":key", $key);

        $stmt->execute();

        $userData = array();
        $rowfinal = array();

        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $userlike = $this->getUserLikeByID($userid, $row['post_id']);
            $user = $this->getUserByID($row['user_id']);
            $rowfinal['id'] = (int)$row['post_id'];
            $rowfinal['userid'] = (int)$row['user_id'];
            $rowfinal['name'] = $user['name'];
            $rowfinal['image'] = $row['image'];
            $rowfinal['likes_count'] = $row['likes_count'];
            $rowfinal['comments_count'] = $row['comments_count'];
            $rowfinal['status'] = $row['status'];
            $rowfinal['userlike'] = $userlike;
            $rowfinal['profilePic'] = $user['imgurl'];
            $rowfinal['timeStamp'] = $row['timestamp'];
            $rowfinal['url'] = $row['url'];


            $userData['feed'][] = $rowfinal;
        }

        echo json_encode($userData);

    }

    public function searchUserBase($key, $userid, $batch, $institute)
    {
        $key = "%" . $key . "%";
        $stmt = $this->conn->prepare("SELECT * FROM users where name LIKE :key OR company LIKE :key OR  location LIKE :key");
        $stmt->bindValue(":key", $key);

        $stmt->execute();

        $userData = array();
        $rowfinal = array();

        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $rowfinal['userid'] = (int)$row['id'];
            $rowfinal['name'] = $row['name'];
            $rowfinal['image'] = $row['imgurl'];
            $rowfinal['company'] = $row['company'];
            $rowfinal['location'] = $row['location'];

            $userData['feed'][] = $rowfinal;
        }

        echo json_encode($userData);

    }

}
