require curl_init;

$long_article = "Long article text goes here";

$ch = curl_init("http://api.smmry.com/&SM_API_KEY=DA76225C4C&SM_LENGTH=14&SM_WITH_BREAK");
curl_setopt($ch, CURLOPT_HTTPHEADER, array("Expect:")); // Important do not remove
curl_setopt($ch, CURLOPT_POST, true); 
curl_setopt($ch, CURLOPT_POSTFIELDS, "sm_api_input=".$long_article);
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 20);
curl_setopt($ch, CURLOPT_TIMEOUT, 20);
$return = json_decode(curl_exec($ch), true);
curl_close($ch);
