#Import des paquets necessaire
import cv2
from picamera.array import PiRGBArray
from picamera import PiCamera

# Initialisation de la camera
camera=PiCamera()
rawCapture=PiRGBArray(camera)

#Acquisition d'une image
camera.capture(rawCapture,format='bgr')
imagewebcam=rawCapture.array

#Enregistrement de l'image
#cv2.imwrite('image.jpg',imagewebcam)

# Reconnaissance image
upperbody_cascade = cv2.CascadeClassifier('haarcascade_upperbody.xml')
face_cascade = cv2.CascadeClassifier('haarcascade_frontalfacedefault.xml')
img = imagewebcam
faces = face_cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=5)
for (x,y,w,h) in faces:
	cv2.rectangle(img,(x,y),(x+w,y+h),(255,0,0),2)
cv2.imshow('img',img)
cv2.imwrite('image_out.jpg',img)
cv2.waitKey(0)
cv2.destroyAllWindows()
