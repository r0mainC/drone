#Import des paquets necessaire
import cv2
from picamera.array import PiRGBArray
from picamera import PiCamera

# Initialisation de la camera
camera=PiCamera()
rawCapture=PiRGBArray(camera)
img_w=640
img_h=480

#Acquisition d'une image

cam=camera
cam.resolution=(img_w,img_h)
camera.capture(rawCapture,format='bgr')
imagewebcam=rawCapture.array

#Enregistrement de l'image
#cv2.imwrite('image.jpg',imagewebcam)

# Reconnaissance image
upperbody_cascade = cv2.CascadeClassifier('haarcascade_upperbody.xml')
face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_alt.xml')
img = imagewebcam
faces = face_cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=5)
body = upperbody_cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=5)
if len(faces) == 0:
	print('Pas de visage detecte')
	exit()
moitielargeur = img_w/2
moyenne =0
for (x,y,w,h) in faces:
	cv2.rectangle(img,(x,y),(x+w,y+h),(255,0,0),2)
	moyenne = (2*x+w)/2
	if moyenne < moitielargeur:
		print('ALERTE')
	else:
		print('Zone baignade ok')

for (x,y,w,h) in body:
	cv2.rectangle(img,(x,y),(x+w,y+h),(255,0,0),2)	

#determiner la zone interdite


#for (x,y,w,h) in (faces or body): 
	


#cv2.imshow('img',img)
cv2.imwrite('image_out.jpg',img)
cv2.waitKey(0)
cv2.destroyAllWindows()
