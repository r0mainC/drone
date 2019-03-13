<?php header('Content-Type: application/json');
    header("Access-Control-Allow-Origin: *");


if( !isset($_GET['id']) ||!isset($_GET['type']) || !isset($_GET['date']) ||!isset($_GET['heure'])  ||!isset($_GET['image']))
   echo '{"success": 0}';
else
{
 $db= new PDO("mysql:host=localhost;dbname=drone", "root", "root");
  $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
  $req = $db->prepare('INSERT INTO alerts (drone_id, alert_type, date, heure, image) VALUES (:id, :type, :date, :heure, :image)');


$req->execute(array("id"=>$_GET['id'],"type"=>$_GET['type'],"date"=>$_GET['date'], "heure"=>$_GET['heure'],"image"=>$_GET['image']));

  echo  '{"success":1  }';
}
?>