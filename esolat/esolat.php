<?php
global $memcache;
$memcache = new Memcache;
$memcache->connect('127.0.0.1',11211) or die("Could not connect memcache");

global $mysqli;
$mysqli = new mysqli("localhost","esolat","esolat123","esolat");
if( $mysqli->connect_errno){
	echo "Failed to connect to MySQL: (".$mysqli->connect_errno.")".$mysqli->connect_error;
}

function getCache($key){
	global $memcache;
	return ($memcache) ? $memcache->get($key) : false;
}

function setCache($key,$object,$timeout = 60){
	global $memcache;
	return ($memcache) ? $memcache->set($key,$object,MEMCACHE_COMPRESSED,$timeout) : false;
}

function mysql_query_cache($sql, $timeout = 60){
	global $mysqli;
	if(($cache = getCache(md5("mysql_query".$sql))) === false){
		$cache = false;
		$r = $mysqli->query($sql);
		if($r->num_rows !== 0){
			for($i=0;$i<$r->num_rows;$i++){
				$cache[$i] = $r->fetch_assoc();
			}
		}
		setCache(md5("mysql_query".$sql),$cache);
	}
	return $cache;
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
$jadual = mysql_query_cache("select distinct date,imsak,subuh,syuruk,zohor,asar,maghrib,iswak from ".strtolower($zon)." where year(date)=".$tahun." and month(date)=".$bulan." order by date");
echo "<table>";
$rows = count($jadual);
if($rows>20){
	for($i=0;$i<$rows;$i++){
		$row=$jadual[$i];
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
		echo "<td>&nbsp;09:00</td>";
		echo "</tr>\r";
	}
}
echo "</table>";
?>
