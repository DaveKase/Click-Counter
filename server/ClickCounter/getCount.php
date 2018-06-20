<?php
	// Connecting to database
	$url = 'localhost';
	$user = 'root';
	$pass = '';
	$db = 'contact_counter';
	
	$link = mysqli_connect($url, $user, $pass, $db);
	
	// Getting value from JSON object
	$value = json_decode(file_get_contents('php://input'), true);
	$btn_name = $value['btn_id'];

	// If there were no value, using default value instead
	if($btn_name == "") {
		$btn_name = "black";
	}

	// Getting previous button click count from database
	$query = "SELECT * FROM counter WHERE button_id =  '".$btn_name."'";
	$result = mysqli_query($link, $query);
	$row_count = mysqli_num_rows($result);

	// If there was no data about that button a new row will be added with a count of 1, else the existing count will be updated
	if($row_count > 0) {
		while($row = mysqli_fetch_array($result)) {
			$click_count = $row['click_count'] + 1;
			$query = "UPDATE counter SET click_count =".$click_count." WHERE button_id =  '".$btn_name."'";
			mysqli_query($link, $query);
		}
	} else {
		$query = "INSERT INTO  `contact_counter`.`counter` (`button_id` ,`click_count`) VALUES ('".$btn_name."',  '1');";
		mysqli_query($link, $query);
	}
	
	mysqli_close($link);
?>