<?php 

if(isset($_POST["fname"]) && isset($_POST["lname"]) && isset($_GET["user"]) && isset($_POST["pass"])){
    
    $fname=$_POST["fname"];
    $lname=$_POST["lname"];
    $user=$_GET["user"];
    $pass=md5($_POST["pass"]);
    
    $userDB="id8362570_monument";
    $passwordDB="monument";
    $hostDB="localhost";
    $db_name="id8362570_monuments";
    
    $con=mysqli_connect($hostDB,$userDB,$passwordDB,$db_name);
    $req="update users set fname = '".$fname."' , lname = '".$lname."' , pass = '".$pass."' where user = '".$user."'";
 
    if(mysqli_query($con,$req))
        echo "Modification reussie";
    else
    echo $req;
        //echo mysqli_error($con);
}else{
    echo "no request method !!";
}
?>