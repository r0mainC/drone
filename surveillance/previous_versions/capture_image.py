#Import des paquets necessaire
import cv2
from picamera.array import PiRGBArray
from picamera import PiCamera

# Initialisation de la camera
camera=PiCamera()
rawCapture=PiRGBArray(camera)

#Acquisition d'une image
camera.capture(rawCapture,format='bgr')
image=rawCapture.array

#Enregistrement de l'image
cv2.imwrite('image.jpg',image)
