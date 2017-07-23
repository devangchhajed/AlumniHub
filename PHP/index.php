<?php

use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

require_once './classes/DBFunctions.php';
require './vendor/autoload.php';


$config['displayErrorDetails'] = true;
$config['addContentLengthHeader'] = false;


/*$config['db']['host'] = "localhost";
$config['db']['user'] = "root";
$config['db']['pass'] = "";
$config['db']['dbname'] = "alumnihub";

*/

  $config['db']['host']   = "localhost";
  $config['db']['user']   = "codejuris";
  $config['db']['pass']   = "Server@123";
  $config['db']['dbname'] = "bconnect";

$app = new \Slim\App(["settings" => $config]);


$container = $app->getContainer();

$container['view'] = function ($container) {
    $view = new \Slim\Views\Twig('twigviews', ['cache' => 'twigviews']);

    $basepath = rtrim(str_ireplace('index.php', '', $container['request']->getUri()->getBasePath()), '/');
    $view->addExtension(new Slim\Views\TwigExtension($container['router'], $basepath));

    return $view;
};

$container['db'] = function ($c) {
    $db = $c['settings']['db'];
    $pdo = new PDO("mysql:host=" . $db['host'] . ";dbname=" . $db['dbname'],
        $db['user'], $db['pass']);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
    return $pdo;
};


$app->get('/update', function (Request $request, Response $response) {

    $data = array();
    $data['version'] = 1;
    $data['newfeatures'] = "Bug Fixes";
    $data['forceupdate'] = true;

    $response->getBody()->write(json_encode($data), true);

    return $response;
});


$app->group('/v1', function () use ($app) {

    $app->group('/user', function () use ($app) {

        $app->post('/register', function (Request $request, Response $response) {
            $data = $request->getParsedBody();
            $tdata = [];
            $tdata['name'] = filter_var($data['name'], FILTER_SANITIZE_STRING);
            $tdata['email'] = filter_var($data['email'], FILTER_SANITIZE_STRING);
            $tdata['batch'] = filter_var($data['batch'], FILTER_SANITIZE_STRING);
            $tdata['institute'] = filter_var($data['institute'], FILTER_SANITIZE_STRING);
            $tdata['dob'] = filter_var($data['dob'], FILTER_SANITIZE_STRING);
            $tdata['phone'] = filter_var($data['phone'], FILTER_SANITIZE_STRING);
            $tdata['company'] = filter_var($data['company'], FILTER_SANITIZE_STRING);
            $tdata['location'] = filter_var($data['location'], FILTER_SANITIZE_STRING);
            $tdata['imgurl'] = filter_var($data['imgurl'], FILTER_SANITIZE_STRING);
            $tdata['fcmid'] = filter_var($data['fcm_id'], FILTER_SANITIZE_STRING);

            if ($tdata['imgurl'] == "") {
                $tdata['imgurl'] = "http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg";
            }


            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->registerUser($tdata['name'], $tdata['email'], $tdata['batch'], $tdata['institute'], $tdata['dob'], $tdata['phone'], $tdata['company'], $tdata['location'], $tdata['imgurl'], $tdata['fcmid']);

            $response->getBody()->write($ticket, true);

            return $response;

        });

        $app->post('/initiatelogin', function (Request $request, Response $response) {
            $data = $request->getParsedBody();

            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->initiateLogin(filter_var($data['email'], FILTER_SANITIZE_STRING),filter_var($data['fcm'], FILTER_SANITIZE_STRING));

            $response->getBody()->write($ticket, true);

            return $response;

        });


        $app->post('/feedback', function (Request $request, Response $response) {
            $data = $request->getParsedBody();
            $tdata = [];
            $tdata['id'] = filter_var($data['id'], FILTER_SANITIZE_NUMBER_INT);
            $tdata['feedback'] = filter_var($data['feedback'], FILTER_SANITIZE_STRING);

            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->registerUserFeedback($tdata['id'], $tdata['feedback']);

            $response->getBody()->write($ticket, true);

            return $response;

        });

        $app->post('/notification', function (Request $request, Response $response) {
            $data = $request->getParsedBody();

            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->fetchNotificationList($data['id'], $data['batch'], $data['institute']);

            $response->getBody()->write($ticket, true);

            return $response;

        });


        $app->post('/updateuserimage', function (Request $request, Response $response) {
            $data = $request->getParsedBody();
            $tdata = [];
            $tdata['id'] = filter_var($data['id'], FILTER_SANITIZE_STRING);
            $tdata['img'] = filter_var($data['img'], FILTER_SANITIZE_STRING);


            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->updateUserDisplayPicture($tdata['id'], $tdata['img']);

            $response->getBody()->write($ticket, true);

            return $response;

        });

        $app->post('/updatefcmid', function (Request $request, Response $response) {
            $data = $request->getParsedBody();
            $tdata = [];
            $tdata['id'] = filter_var($data['id'], FILTER_SANITIZE_STRING);
            $tdata['fcmid'] = filter_var($data['fcmid'], FILTER_SANITIZE_STRING);


            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->updateFcmId($tdata['id'], $tdata['fcmid']);

            $response->getBody()->write($ticket, true);

            return $response;

        });

        $app->post('/reportimage', function (Request $request, Response $response) {
            $data = $request->getParsedBody();
            $tdata = [];
            $tdata['imgurl'] = filter_var($data['imgurl'], FILTER_SANITIZE_STRING);
            $tdata['uname'] = filter_var($data['uname'], FILTER_SANITIZE_STRING);
            $tdata['status'] = filter_var($data['status'], FILTER_SANITIZE_STRING);
            $tdata['uid'] = filter_var($data['uid'], FILTER_SANITIZE_STRING);
            $tdata['ureport'] = filter_var($data['ureport'], FILTER_SANITIZE_STRING);

            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->registerImageReport($tdata['imgurl'], $tdata['uname'], $tdata['status'], $tdata['uid'], $tdata['ureport']);

            $response->getBody()->write($ticket, true);

            return $response;

        });

    });

    $app->group('/newsfeed', function () use ($app) {


        $app->post('/timeline/{offset}', function (Request $request, Response $response) {
            $offset = $request->getAttribute('offset');
            $data = $request->getParsedBody();

            $mapper = new DBFunctions($this->db);

            $ticket = $mapper->fetchPost($offset, $data['userid'], $data['batch'], $data['institute']);

            $response->getBody()->write($ticket, true);

            return $response;
        });

        $app->post('/post', function (Request $request, Response $response) {
            $data = $request->getParsedBody();

            $tdata = [];
            $tdata['uid'] = filter_var($data['user_id'], FILTER_SANITIZE_STRING);
            $tdata['status'] = filter_var($data['status'], FILTER_SANITIZE_STRING);
            $tdata['image'] = $data['image'];

            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->postWallFeed($tdata['uid'], $tdata['status'], $tdata['image']);
            $response->getBody()->write($ticket, true);

            return $response;

        });

        $app->post('/user/{offset}', function (Request $request, Response $response) {
            $data = $request->getParsedBody();
            $offset = $request->getAttribute('offset');


            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->fetchProfilePost($data['id'], $offset);
            $response->getBody()->write($data['id']. $offset, true);

            return $response;

        });

        $app->post('/deletepost', function (Request $request, Response $response) {
            $data = $request->getParsedBody();

            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->deleteWallFeed($data['id']);
            $response->getBody()->write($ticket, true);

            return $response;

        });

        $app->group('/status', function () use ($app) {

            $app->post('/like', function (Request $request, Response $response) {
                $data = $request->getParsedBody();

                $mapper = new DBFunctions($this->db);
                $ticket = $mapper->likeStatusPost($data['postid'], $data['userid']);
                $response->getBody()->write($ticket, true);

                return $response;
            });

            $app->post('/unlike', function (Request $request, Response $response) {
                $data = $request->getParsedBody();

                $mapper = new DBFunctions($this->db);
                $ticket = $mapper->unlikeStatusPost($data['postid'], $data['userid']);
                $response->getBody()->write($ticket, true);

                return $response;

            });
            $app->post('/comment', function (Request $request, Response $response) {
                $data = $request->getParsedBody();

                $mapper = new DBFunctions($this->db);
                $ticket = $mapper->commentStatusPost($data['postid'], $data['userid'], $data['comment']);
                $response->getBody()->write($ticket, true);

                return $response;

            });
            $app->post('/uncomment', function (Request $request, Response $response) {
                $data = $request->getParsedBody();

                $mapper = new DBFunctions($this->db);
                $ticket = $mapper->deleteCommentStatusPost($data['postid'], $data['commentid']);
                $response->getBody()->write($ticket, true);

                return $response;

            });
            $app->post('/getfullpost', function (Request $request, Response $response) {
                $data = $request->getParsedBody();

                $mapper = new DBFunctions($this->db);
                $ticket = $mapper->getFullPost($data['postid'], $data['userid']);
                $response->getBody()->write($ticket, true);

                return $response;

            });

        });

    });

    $app->group('/search', function () use ($app) {
        $app->post('/post', function (Request $request, Response $response) {
            $data = $request->getParsedBody();

            $tdata = [];
            $tdata['key'] = filter_var($data['key'], FILTER_SANITIZE_STRING);
            $tdata['userid'] = filter_var($data['userid'], FILTER_SANITIZE_STRING);
            $tdata['batch'] = filter_var($data['batch'], FILTER_SANITIZE_STRING);
            $tdata['institute'] = filter_var($data['institute'], FILTER_SANITIZE_STRING);

            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->searchWallFeed($tdata['key'], $tdata['userid'], $tdata['batch'], $tdata['institute']);
            $response->getBody()->write($ticket, true);

            return $response;

        });
        $app->post('/user', function (Request $request, Response $response) {
            $data = $request->getParsedBody();

            $tdata = [];
            $tdata['key'] = filter_var($data['key'], FILTER_SANITIZE_STRING);
            $tdata['userid'] = filter_var($data['userid'], FILTER_SANITIZE_STRING);
            $tdata['batch'] = filter_var($data['batch'], FILTER_SANITIZE_STRING);
            $tdata['institute'] = filter_var($data['institute'], FILTER_SANITIZE_STRING);

            $mapper = new DBFunctions($this->db);
            $ticket = $mapper->searchUserBase($tdata['key'], $tdata['userid'], $tdata['batch'], $tdata['institute']);
            $response->getBody()->write($ticket, true);

            return $response;

        });

    });


});


$app->run();
