<?php header('Content-Type: application/json');
    header("Access-Control-Allow-Origin: *");
     $db= new PDO("mysql:host=localhost;dbname=drone", "root", "root");
  $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
  
    $req = $db->prepare('SELECT * FROM alerts WHERE status=:id');
  $req->execute( array('id' => 'NEW'));
  $n=$req->rowCount();
  echo '{"alerts":[';
  $str="";
  while($donnee=$req->fetch())
	{


	$str.= '{"id":'.$donnee['id']. ',"drone": "'. $donnee['drone_id'].'", "type" : "'.$donnee['alert_type'].'", "date":"'. $donnee['date'].'", "heure": "'.$donnee['heure'].'", "image":"'.$donnee['image'].'"},';

	}
  echo substr($str, 0,-1);
    echo '  ]}';


?>