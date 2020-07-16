# Import RFM9x and GPIO libraries
import time
import busio
from digitalio import DigitalInOut, Direction, Pull
import RPi.GPIO as GPIO
import board
import adafruit_rfm9x

import cv2
import datetime as dt

# Configure RFM9X LoRa Module
CS = DigitalInOut(board.CE1)
RESET = DigitalInOut(board.D25)
spi = busio.SPI(board.SCK, MOSI=board.MOSI, MISO=board.MISO)

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(20, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
rfm9x = adafruit_rfm9x.RFM9x(spi, CS, RESET, 915.0)
rfm9x.tx_power = 23
print("RFM9x: Detected")

backsub = cv2.createBackgroundSubtractorMOG2() #generate a foreground mask
capture = cv2.VideoCapture("video_mid.mp4") #uploads video

counter = 0                             #sets counter to 0
x = 0
y = 0
sen = 0
minArea=1                               #erosion area to 1
lineCount = 50
mag = 4.2 # 4.2
sendTime = dt.datetime.now().hour * 3600000 + dt.datetime.now().minute * 60000 + dt.datetime.now().second * 1000

if capture.isOpened():                   #read the input video or input images sequence
    ret, frame = capture.read()          #returns the video frame 

else:
    ret = False                          #otherwise, break

while ret:
    ret, frame = capture.read()
    if ret:
        frame = cv2.resize(frame,(320,240))
    if ret==False:
        break
    
    fgmask = backsub.apply(frame, None, 0.02)           
    erode=cv2.erode(fgmask,None,iterations=3) #erodes away boundaries of foreground object, essential removes small white noises (in this case, humans), since cars are so big 3 iterations 
    moments=cv2.moments(erode,True)           #moments method which relates to pixel intensity
    area=moments['m00']                       
    
    if area > minArea:                 #compares the eroded image area with the minimum area provided
        x=int(moments['m10']/area)     #center of the contour since cars is irregular shape
        y=int (moments['m01']/area)
        if y<lineCount*mag: ## and x>lineCount*4.52:                           #tracks the centroids of the object relative to the line 
            sen=sen<<1
        else:
            sen=(sen<<1)|1
        sen=sen&0x03
        if sen==1 and ((dt.datetime.now().hour * 3600000 + dt.datetime.now().minute * 60000 + dt.datetime.now().second * 1000) - sendTime) > 3000:
            sendTime = dt.datetime.now().hour * 3600000 + dt.datetime.now().minute * 60000 + dt.datetime.now().second * 1000
            counter=counter+1
            
            toSend = "333"
            dataOut = bytes(toSend, "utf-8")
            rfm9x.send(dataOut)
            print("Sent: " + toSend)
            
            print("Car #" ,counter)
            print(str(x) + "," + str(y))
            
        cv2.circle(frame,(x,y),5,(255,255,255),-1)          #places a circle at the center of the shape
    cv2.line(frame,(0, int(lineCount*mag)),(400, int(lineCount*mag)),(0,255,0),2)          #draws the line 

    #cv2.line(frame,(0,lineCount*8),(700,lineCount*8),(0,255,0),2)
    font = cv2.FONT_HERSHEY_SIMPLEX
    cv2.putText(frame,'No of Cars: '+str(counter), (10,30),font,1, (0, 255, 0), 2)
    cv2.imshow("counter", frame)
    key = cv2.waitKey(10)
    if key==27:
        break
