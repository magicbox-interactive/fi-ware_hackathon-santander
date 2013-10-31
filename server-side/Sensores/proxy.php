<?php
$request_xml = '<queryContextRequest>
	<entityIdList>
		<entityId type="Sensor" isPattern="false">
			<id>4IN1:86:DA:B6:0002</id>
		</entityId>
		<entityId type="Sensor" isPattern="false">
			<id>4IN1:48:99:26:0002</id>
		</entityId>		
		<entityId type="Sensor" isPattern="false">
			<id>4IN1:B5:74:20:0002</id>
		</entityId>
		<entityId type="Sensor" isPattern="false">
			<id>4IN1:48:99:26:0003</id>
		</entityId>			
	</entityIdList>
	<attributeList>
		<attribute>Move</attribute>
		<attribute>illuminance</attribute>
		<attribute>temperature</attribute>
		<attribute>relativeHumidity</attribute>
	</attributeList>
</queryContextRequest>';
 
//Initialize handle and set options
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'http://130.206.80.45:1029/ngsi10/queryContext');
curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_ANY);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_TIMEOUT, 4);
curl_setopt($ch, CURLOPT_POSTFIELDS, $request_xml);
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/xml','Connection: close'));
 
//Execute the request and also time the transaction ( optional )
$start = array_sum(explode(' ', microtime()));
$result = curl_exec($ch);
$stop = array_sum(explode(' ', microtime()));
$totalTime = $stop - $start;
 
//Check for errors ( again optional )
$error = false;
if ( curl_errno($ch) ) {
        $error = 'ERROR -> ' . curl_errno($ch) . ': ' . curl_error($ch);
} else {
        $returnCode = (int)curl_getinfo($ch, CURLINFO_HTTP_CODE);
        switch($returnCode){
                case 200:
                        break;
                default:
                        $error = 'HTTP ERROR -> ' . $returnCode;
                        break;
        }
}
 
//Close the handle
curl_close($ch);
 
//Output the results and time
//echo 'Total time for request: ' . $totalTime . "\n";
if ($error)
{
        echo $error;
}


echo $result;
?>