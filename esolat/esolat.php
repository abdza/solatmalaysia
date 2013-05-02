<?php
$mysqli = new mysqli("localhost","esolat","esolat123","esolat");
if( $mysqli->connect_errno){
	echo "Failed to connect to MySQL: (".$mysqli->connect_errno.")".$mysqli->connect_error;
}

$zon="sgr03";
$tahun=Date('Y');
$bulan=Date('m');
if(isset($_GET['zon'])){
	$zon=$_GET['zon'];
}
if(isset($_GET['tahun'])){
	$tahun=$_GET['tahun'];
}
if(isset($_GET['bulan'])){
	$bulan=$_GET['bulan'];
}
$jadual = $mysqli->query("select * from ".$zon." where year(date)=".$tahun." and month(date)=".$bulan." order by date");
echo "<table>";
if($jadual->num_rows>0){
	while($row = $jadual->fetch_assoc()){
		echo "<tr>";
		echo "<td>&nbsp;".substr($row['date'],-2)."</td>";
		echo "<td>&nbsp;".$row['imsak']."</td>";
		echo "<td>&nbsp;".$row['subuh']."</td>";
		echo "<td>&nbsp;".$row['syuruk']."</td>";
		echo "<td>&nbsp;".$row['zohor']."</td>";
		echo "<td>&nbsp;".$row['asar']."</td>";
		echo "<td>&nbsp;".$row['maghrib']."</td>";
		echo "<td>&nbsp;".$row['iswak']."</td>";
		echo "</tr>\r";
	}
}
else{
	for($i=1;$i<=31;$i++){
		echo "<tr>";
		if($i<10){
			echo "<td>&nbsp;0".$i."</td>";
		}
		else{
			echo "<td>&nbsp;".$i."</td>";
		}
		echo "<td>&nbsp;03:00</td>";
		echo "<td>&nbsp;04:00</td>";
		echo "<td>&nbsp;05:00</td>";
		echo "<td>&nbsp;06:00</td>";
		echo "<td>&nbsp;07:00</td>";
		echo "<td>&nbsp;08:00</td>";
		echo "<td>&nbsp;22:00</td>";
		echo "</tr>\r";
	}
}
echo "</table>";
?>
