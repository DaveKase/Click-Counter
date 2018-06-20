<!DOCTYPE html>
<html>
	<head>
		<title>Click Counter</title>
	</head>
	<body>
		<script type="text/javascript">
			// Sets refresh rate for the page
			setTimeout(function() {
				window.location.reload();
			}, 5000);
		</script>
		
		<?php
			// Connecting to database and querying out all click counts
			$url = 'localhost';
			$user = 'root';
			$pass = '';
			$db = 'contact_counter';
			
			$link = mysqli_connect($url, $user, $pass, $db);
			$query = "SELECT * FROM counter;";
			$result = mysqli_query($link, $query);
			
			// Showing a circle using SVG. The diameter depends on how many times a button was clicked and fill color depends on button name
			while($row = mysqli_fetch_array($result)) {
				echo '<svg width="100" height="100">
					<circle cx="50" cy="50" r="'.$row['click_count'].'" fill="'.$row['button_id'].'" />
					<text fill="black" font-size="12" font-family="Verdana" x="41" y="55">'.$row['click_count'].'</text>
					Sorry, your browser does not support inline SVG.
					</svg>
					<br />';
			}
			
			mysqli_close($link);
		?>
	</body>
</html>