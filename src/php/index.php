<?php

	/* Variables for connection with the database. */
	$dbhost = "localhost";
	$dbname = "bigdata";
	$dbuser = "root";
	$dbpass = "";
	$dbtable = "fullset5";
	// Database fields
	$dbf_word1 = "a";
	$dbf_word2 = "b";
	$dbf_count = "c";
	
	/* Connecting to the database. */
	mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to database.');
	mysql_select_db($dbname) or die('Error selecting of database.');
			
	$carlist = array("Abarth", "Alfa Romeo", "Asia Motors", "Aston Martin", "Audi", "Austin", "Autobianchi", "Bentley", "BMW", "Bugatti", "Buick", "Cadillac", "Carver", "Chevrolet", "Chrysler", "Citroen", "Corvette", "Dacia", "Daewoo", "Daihatsu", "Daimler", "Datsun", "Dodge", "Donkervoort", "Ferrari", "Fiat", "Fisker", "Ford", "FSO", "Galloper", "Honda", "Hummer", "Hyundai", "Infiniti", "Innocenti", "Iveco", "Jaguar", "Jeep", "Josse", "Kia", "KTM", "Lada", "Lamborghini", "Lancia", "Land Rover", "Landwind", "Lexus", "Lincoln", "Lotus", "Marcos", "Maserati", "Maybach", "Mazda", "Mega", "Mercedes", "Mercury", "MG", "Mini", "Mitsubishi", "Morgan", "Morris", "Nissan", "Noble", "Opel", "Peugeot", "PGO", "Pontiac", "Porsche", "Princess", "Renault", "Rolls-Royce", "Rover", "Saab", "Seat", "Skoda", "Smart", "Spectre", "SsangYong", "Subaru", "Suzuki", "Talbot", "Tesla", "Think", "Toyota", "Triumph", "TVR", "Volkswagen", "Volvo", "Yugo");
	$carlistLength = count($carlist);
			
	$carcount = array("Abarth" => 0, "Alfa Romeo" => 0, "Asia Motors" => 0, "Aston Martin" => 0, "Audi" => 0, "Austin" => 0, "Autobianchi" => 0, "Bentley" => 0, "BMW" => 0, "Bugatti" => 0, "Buick" => 0, "Cadillac" => 0, "Carver" => 0, "Chevrolet" => 0, "Chrysler" => 0, "Citroen" => 0, "Corvette" => 0, "Dacia" => 0, "Daewoo" => 0, "Daihatsu" => 0, "Daimler" => 0, "Datsun" => 0, "Dodge" => 0, "Donkervoort" => 0, "Ferrari" => 0, "Fiat" => 0, "Fisker" => 0, "Ford" => 0, "FSO" => 0, "Galloper" => 0, "Honda" => 0, "Hummer" => 0, "Hyundai" => 0, "Infiniti" => 0, "Innocenti" => 0, "Iveco" => 0, "Jaguar" => 0, "Jeep" => 0, "Josse" => 0, "Kia" => 0, "KTM" => 0, "Lada" => 0, "Lamborghini" => 0, "Lancia" => 0, "Land Rover" => 0, "Landwind" => 0, "Lexus" => 0, "Lincoln" => 0, "Lotus" => 0, "Marcos" => 0, "Maserati" => 0, "Maybach" => 0, "Mazda" => 0, "Mega" => 0, "Mercedes" => 0, "Mercury" => 0, "MG" => 0, "Mini" => 0, "Mitsubishi" => 0, "Morgan" => 0, "Morris" => 0, "Nissan" => 0, "Noble" => 0, "Opel" => 0, "Peugeot" => 0, "PGO" => 0, "Pontiac" => 0, "Porsche" => 0, "Princess" => 0, "Renault" => 0, "Rolls-Royce" => 0, "Rover" => 0, "Saab" => 0, "Seat" => 0, "Skoda" => 0, "Smart" => 0, "Spectre" => 0, "SsangYong" => 0, "Subaru" => 0, "Suzuki" => 0, "Talbot" => 0, "Tesla" => 0, "Think" => 0, "Toyota" => 0, "Triumph" => 0, "TVR" => 0, "Volkswagen" => 0, "Volvo" => 0, "Yugo" => 0);
	
?>
<html>
	<head>
		<title>Linker</title>
		<script src="js/prototype.js"></script>
		<script src="js/script.js"></script>
		<script>
			document.observe('dom:loaded', function() {
			new Control.Tabs('tabs_cars');  
			});
		</script>
		<style type="text/css">
			.cloud_0 { color: #ccc; font-size: 10px; !important }
			.cloud_1 { color: #999; font-size: 12px; !important }
			.cloud_2 { color: #888; font-size: 14px; !important }
			.cloud_3 { color: #777; font-size: 16px; !important }
			.cloud_4 { color: #666; font-size: 18px; !important }
			.cloud_5 { color: #555; font-size: 20px; !important }
			.cloud_6 { color: #444; font-size: 22px; !important }
			.cloud_7 { color: #333; font-size: 24px; !important }
			.cloud_8 { color: #222; font-size: 26px; !important }
			.cloud_9 { color: #111; font-size: 28px; !important }
			.cloud_10 { color: #000; font-size: 30px; !important }
			
			.search {
				padding: 10px;
				margin: 10px;
				margin: 0 auto;
				background: #eaeaea;
				border: 1px solid #ccc;
			}
			
			.search a:hover {
				text-decoration: underline;
			}
			
			.ulCloud {
				width: 500px;
			}
			.clear { clear:both; }
			
			.ulCloud li {
				float: left;
				padding: 3px;
				margin: 10px;
			}
			
			ul {
				text-align: center;
			}
			li.tab {
				display: inline-block;
				padding: 5px;
				text-align: center;
			}
			
			li.cloud {
				display: inline-block;
				padding: 5px;
				text-align: center;
			}

			li.tab .active {
				display: inline-block;
				font-weight: bold;
			}
			
			a {
				text-decoration: none;
				color: #000;
			}
		</style>
		
	</head>
	<body style="font-family:'Open Sans';font-size:12px;">
		<div style="text-align:center;font-size:64px;">
			Linker
		</div>
		<div style="text-align:center;font-size:14px;">
			<a href="?cars=1">Show cars cloud</a>
		</div>
		
		
		<div class="search">
		
			<form method="post">
			
				<?php
				if($_SERVER['REQUEST_METHOD'] == "POST" || isset($_GET['q'])) {
					$q = isset($_POST['q']) ? $_POST['q'] : $_GET['q'];
					$sql = "SELECT $dbf_word1, $dbf_word2, `$dbf_count` FROM $dbtable WHERE $dbf_word1 LIKE '%".$q."%' OR $dbf_word2 LIKE '%".$q."%'ORDER BY `$dbf_count` DESC LIMIT 25";
					
					$res = mysql_query($sql);
					
					$words = array();
					while($x = mysql_fetch_assoc($res)) {
					
						if(!isset($words[$x[$dbf_word1]]) || $words[$x[$dbf_word1]] < $x[$dbf_count]) {
							$words[$x[$dbf_word1]] = $x[$dbf_count];
						}
						
						if(!isset($words[$x[$dbf_word2]]) || $words[$x[$dbf_word2]] < $x[$dbf_count]) {
							$words[$x[$dbf_word2]] = $x[$dbf_count];
						}
					}
					
					$total = array_sum($words)/2;
					
					$new_words = array();
					foreach($words as $w => $c) {
						$new_words[] = array($w, $c);
					}
					shuffle($new_words);
					
					echo '<ul class="ulCloud">';
					foreach($new_words as $obj) {
						$c = $obj[1];
						$word = $obj[0];
					
						$weight = ceil($c*200/$total);
						
						$class = round($weight / 10,0);
						echo "<li class='cloud cloud_".$class."' style='font-size: ".min(75, $weight)."px;'><a href='?q=".$word."'>".$word."</a></li>";
					}
					echo '</ul>';
					echo '<div class="clear"></div>';
				}
				
				?>
				<h2>Typ hier uw term en uw tagcloud wordt gemaakt</h2>
				<input type="text" name="q" placeholder="typ hier uw tag voor tagcloud" /> <input type="submit" value="Zoek" name="zoek">
			</form>
			
		</div>
		
			<?php
			
			if(isset($_GET['cars'])) {
						
				// Determine the number of total references to each car
				$query_build = "SELECT * FROM $dbtable WHERE ";
				$query_build .= " $dbf_word1 = '".$carlist[0]."' ";
				$i = 1;
				while($i < $carlistLength) {
					// simple model
					$query_build .= "OR $dbf_word1 = '".$carlist[$i]."' ";
					$i++;
				}
				$query_build .= " ORDER BY $dbf_word1 ASC";
				$query = mysql_query($query_build);
				while($r = mysql_fetch_array($query)) {
					if(in_array($r[$dbf_word1], $carlist) AND in_array($r[$dbf_word2], $carlist)) {
						$carcount[$r[$dbf_word1]] = $carcount[$r[$dbf_word1]]+$r[$dbf_count];
					}
				}
				
				echo "<div style='text-align:center;font-size:24px;'>Top 3 references</div><div style=\"text-align:center;font-size:18px;\">";
				$query = mysql_query("SELECT * FROM $dbtable ORDER BY $dbf_count DESC LIMIT 3");
				$top3 = 1;
				while($r = mysql_fetch_array($query)) {
					echo "$top3. $r[$dbf_word1] - $r[$dbf_word2] ($r[$dbf_count])<br>";
					$top3++;
				}
				echo "Total amount of references: ";
				$query = mysql_query("SELECT SUM($dbf_count) as total FROM $dbtable");
				while($r = mysql_fetch_array($query)) {
					echo "$r[total]<br>";
					$top3++;
				}
				echo "</div>";
				
				echo "<ul id='tabs_cars'>";
				for($i=0;$i<$carlistLength;$i++) {
					echo "<li class='tab'><a href='#car_$i'>$carlist[$i]</a></li>";
				}
				echo "</ul><hr>";
				
				$query_build = "SELECT * FROM $dbtable WHERE ";
				$query_build .= " $dbf_word1 = '".$carlist[0]."' ";
				$i = 1;
				while($i < $carlistLength) {
					// simple model
					$query_build .= "OR $dbf_word1 = '".$carlist[$i]."' ";
					$i++;
				}
				$query_build .= " ORDER BY $dbf_word1 ASC";
				$query = mysql_query($query_build);
				//$query = mysql_query("SELECT * FROM $dbtable ORDER BY $dbf_word1 ASC LIMIT 1000"); 
				$carlistLength = count($carlist);
				$i = 0;
				$start = true;
				while($r = mysql_fetch_array($query)) 
				{	
					if(strcasecmp($r[$dbf_word1], $carlist[$i]) == 0) {
						if($start && $i==0) {
							// open model
							echo "<div id='car_$i' style='display: block;text-align:center;'><ul>";
							$start = false;
						}
						// get ref
						if($carcount[$carlist[$i]] != 0) {
							$weight = ceil($r[$dbf_count]/$carcount[$carlist[$i]]*10);
						} else {
							$weight =0;
						}
						echo "<li class='cloud cloud_$weight'>$r[$dbf_word2]</li>";
					} else {
						if(!$start) {
							// close old model
							echo "</ul></div>"; 
						} else {
							// simple model
							echo "<div id='car_$i' style='display: none;text-align:center;'><i>No combinations found.</i></div>";
							$start = false;
						}
						while($i<$carlistLength-1 && strcasecmp($r[$dbf_word1], $carlist[$i]) != 0) {
							$i++;
							if(strcasecmp($r[$dbf_word1], $carlist[$i]) == 0) {
								// open new model
								echo "<div id='car_$i' style='display: block;text-align:center;'><ul>";
								// get ref
								if($carcount[$carlist[$i]] != 0) {
									$weight = ceil($r[$dbf_count]/$carcount[$carlist[$i]]*10);
								} else {
									$weight =0;
								}
								echo "<li class='cloud cloud_$weight'>$r[$dbf_word2]</li>";
							} else {
								// simple model
								echo "<div id='car_$i' style='display: none;text-align:center;'><i>No combinations found.</i></div>";
							}
						}
					}
				}
				// close model
				echo "</ul></div>";
				
				while($i < $carlistLength) {
					// simple model
					echo "<div id='car_$i' style='display: none;text-align: center;'><i>No combinations found.</i></div>";
					$i++;
				}
				
			}
			?>
		</span>
	</body>
</html>