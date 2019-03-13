<?php header('Content-Type: application/json');
    header("Access-Control-Allow-Origin: *");


if( !isset($_GET['id']))
   echo '{"success": 0}';
else
{
 $db= new PDO("mysql:host=localhost;dbname=drone", "root", "root");
  $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
  $req = $db->prepare('UPDATE alerts SET status="DISMISSED" WHERE id=:id');


$req->execute(array("id"=>$_GET['id']));

  echo  '{"success":1  }';
}
?>

