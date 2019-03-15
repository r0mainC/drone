
#Import des paquets necessaire
import cv2
from picamera.array import PiRGBArray
from picamera import PiCamera
import time
import urllib2
from time import gmtime, strftime

# Initialisation de la camera
camera=PiCamera()
rawCapture=PiRGBArray(camera)
img_w=640
img_h=480

def send_request(id, type, date, heure, image):
	if(type!="CRITICAL" and type!="DANGER" and type!="INFO"):
		type="CRITICAL"

	content = urllib2.urlopen("http://192.168.60.226:8888/drone/addAlert.php?id="+id+"&type="+type+"&date="+date+"&heure="+heure+"&image="+image).read()
	print(content)
	#print ("%s" % content)

def alerte():
	#Acquisition d'une image
	cam=camera
	cam.resolution=(img_w,img_h)
	camera.capture(rawCapture,format='bgr')
	imagewebcam=rawCapture.array

	# Reconnaissance image
	upperbody_cascade = cv2.CascadeClassifier('haarcascade_upperbody.xml')
	face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_alt.xml')
	img = imagewebcam
	faces = face_cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=5)
	body = upperbody_cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=5)
	date = strftime("%H-%M-%S", gmtime())
	
	if len(faces) == 0:
		print('Pas de visage detecte')
		#return		
		#exit()
	moitielargeur = img_w/2
	moyenne =0
	
	for (x,y,w,h) in faces:
		
		#determiner la zone interdite
		moyenne = (2*x+w)/2
		if moyenne < moitielargeur:
			cv2.rectangle(img,(x,y),(x+w,y+h),(0,0,255),2)
			print('ALERTE')
			send_request("3", "DANGER", strftime("%Y-%m-%d", gmtime()),strftime("%H:%M", gmtime()), "5.ping")
			cv2.imwrite(date+'_imagedanger.jpg',img)
			
		else:
			print('Zone baignade ok')
			cv2.rectangle(img,(x,y),(x+w,y+h),(0,255,0),2)
			cv2.imwrite(date+'_imageok.jpg',img)

if __name__ == "__main__":

	while 1:
		alerte()
		rawCapture=PiRGBArray(camera)



