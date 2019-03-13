
<html>
<head>
  <title>Les Alertes</title>
  <link rel="stylesheet" type="text/css" href="index.css">
</head>


<body>
  <div id="container">

  <header id="header">
      <h1>Aqua Drone</h1>
  </header>

  <div id="menu">
  <a href="index.php"><div class="item" id="selected">Nouvelles alertes</div></a>
   <a href="history.php">  <div class="item"> Historique</div></a>
  <a href="about.php">  <div class="item"> A propos</div><a>
   <a href="contact.php"> <div class="item"> Nous contacter</div></a>
  </div>

<?php
 
  $db= new PDO("mysql:host=localhost;dbname=drone", "root", "root");
  $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
  
    $req = $db->prepare('SELECT * FROM alerts WHERE status=:id');
  $req->execute( array('id' => 'NEW'));
  $n=$req->rowCount();
 

  
  while($donnee=$req->fetch())
	{

    echo "<div class='card'>"; 
    echo "<div class='id'> Alerte   ".$donnee['id']."</div>";
    echo "<div class='id'> Drone   ".$donnee['drone_id']."</div>"; 
    echo "<div class='id'> Criticité  ".$donnee['alert_type']."</div>"; 
    echo "<div class='id'> Le ".$donnee['date']."</div>"; 
    echo "<div class='id'> À. ".$donnee['heure']."</div>"; 
     echo "</div>";

	}






?>
<div id="container">
</body>
</html>