<?php

    $userDB="id8362570_monument";
    $passwordDB="monument";
    $hostDB="localhost";
    $db_name="id8362570_monuments";
    
    $con=mysqli_connect($hostDB,$userDB,$passwordDB,$db_name);
    $req="select id,title,image1 from monuments order by id desc limit 5;";
    $result=mysqli_query($con,$req);
    
    $tableau=array();
    while($ligne=mysqli_fetch_assoc($result))
       $tableau[]=$ligne; 
    
    echo json_encode($tableau);
    
    mysqli_close($con);
?>