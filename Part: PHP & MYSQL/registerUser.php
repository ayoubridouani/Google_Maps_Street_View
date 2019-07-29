<?php 

if(isset($_POST["fname"]) && isset($_POST["lname"]) && isset($_POST["user"]) && isset($_POST["pass"]) && isset($_POST["email"])){
    
    $fname=$_POST["fname"];
    $lname=$_POST["lname"];
    $user=$_POST["user"];
    $pass=md5($_POST["pass"]);
    $email=$_POST["email"];
    
    $userDB="id8362570_monument";
    $passwordDB="monument";
    $hostDB="localhost";
    $db_name="id8362570_monuments";
    
    $con=mysqli_connect($hostDB,$userDB,$passwordDB,$db_name);
    $req="insert into users (fname,lname,user,pass,email) values('".$fname."','".$lname."','".$user."','".$pass."','".$email."');";
 
    if(mysqli_query($con,$req))
        echo "Insertion reussie";
    else
        echo mysqli_error($con);
}else{
    echo "no request method !!";
}
?>