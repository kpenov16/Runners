
## Spatial Data Types
```javascript
navigator.geolocation.getCurrentPosition(function(position) {  
  console.log(position.coords.latitude, position.coords.longitude);  
});
```

https://developer.mozilla.org/en-US/docs/Web/API/Geolocation_API  
https://dev.mysql.com/doc/refman/8.0/en/spatial-type-overview.html  
https://dawa.aws.dk/dok/api/adresse  
https://dawa.aws.dk/adresser?vejnavn=R%C3%B8dkildevej&husnr=46&struktur=mini  



### test RunRequest as json
```json
{
	"run":{
		"id":"5c5bb-a6ds-41b7-a127-80a4557db67c",
		"title":"Run three",
		"location":"Stockholm",
		"date":"2019-04-24T10:39:33.228+0000",
		"distance":5000,
		"duration":3600000,
		"description":"It is going to be very fun!!!",
		"status":"active"
	},
	"creatorId":"1233"
}
``` 
