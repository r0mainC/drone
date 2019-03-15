import urllib2
from time import gmtime, strftime

def send_request(id, type, date, heure, image):
	if(type!="CRITICAL" and type!="DANGER" and type!="INFO"):
		type="CRITICAL"

	content = urllib2.urlopen("http://192.168.60.226:8888/drone/addAlert.php?id="+id+"&type="+type+"&date="+date+"&heure="+heure+"&image="+image).read()
	print(content)
	#print ("%s" % content)

send_request("3", "DANGER", strftime("%Y-%m-%d", gmtime()),strftime("%H:%M", gmtime()), "5.ping")

