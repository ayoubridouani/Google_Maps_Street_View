<?php
    
    if(isset($_GET["user"]) && isset($_GET["pass"])){
        $user=$_GET["user"];
        $pass=$_GET["pass"];
        
        $userDB="id8362570_monument";
        $passwordDB="monument";
        $hostDB="localhost";
        $db_name="id8362570_monuments";
        
        $con=mysqli_connect($hostDB,$userDB,$passwordDB,$db_name);
        $req="select * from users where user= '". $user . "' and pass = '" . md5($pass) . "';";
        $result=mysqli_query($con,$req);
        
        $tableau=array();
        while($ligne=mysqli_fetch_assoc($result))
           $tableau[]=$ligne; 
        
        echo str_replace("]","",str_replace("[","",json_encode($tableau)));
        
        mysqli_close($con);
    }
?>