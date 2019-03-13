import urllib2

def send_request(id, type, date, heure, image):

	if(type!="CRITICAL" and type!="DANGER" and type!="INFO"):
		type="CRITICAL"

	content = urllib2.urlopen("http://192.168.43.226:8888/drone/addAlert.php?id="+id+"&type="+type+"&date="+date+"&heure="+heure+"&image="+image).read()
	print content


send_request("3", "DANGER", "2019-10-10", "22:10", "5.ping")



