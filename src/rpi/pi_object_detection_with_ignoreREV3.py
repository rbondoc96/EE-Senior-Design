# USAGE
# python3 pi_object_detection_with_ignoreREV3.py --prototxt MobileNetSSD_deploy.prototxt.txt --model MobileNetSSD_deploy.caffemodel

# import the necessary packages
from imutils.video import VideoStream
from imutils.video import FPS
from multiprocessing import Process
from multiprocessing import Queue
import numpy as np
import argparse
import imutils
import cv2
# Import Python System Libraries
import time
# Import Blinka Libraries
import busio
from digitalio import DigitalInOut, Direction, Pull
import board
#import RPi.GPIO as GPIO
# Import RFM9x
import adafruit_rfm9x

'''
    Parameters:
        prevOrigins: nonempty list of tuples
        currOrigin: tuple

    Returns:
        True: If currOrigin is within pixels value from one of the prevOrigins values
        False: If currOrigin is not within pixels value from any of the prevOrigins values

    Variables:
        pixels: Allow you to adjust the precision of you check
'''
def shouldntTransmit(prevOrigins, currOrigin):

    pixels = 50

    if prevOrigins[0] == prevOrigins[-1]:
        if abs(prevOrigins[0][0] - currOrigin[0]) < pixels and abs(prevOrigins[0][1] - currOrigin[1]) < pixels:
            prevOrigins[0] = currOrigin
            return True
        else:
            return False
    else:
        if abs(prevOrigins[0][0] - currOrigin[0]) < pixels and abs(prevOrigins[0][1] - currOrigin[1]) < pixels:
            prevOrigins[0] = currOrigin
            return True
        else:
            return shouldntTransmit(prevOrigins[1:], currOrigin) or False


# Configure LoRa Radio
CS = DigitalInOut(board.CE1)
RESET = DigitalInOut(board.D25)
spi = busio.SPI(board.SCK, MOSI=board.MOSI, MISO=board.MISO)
rfm9x = adafruit_rfm9x.RFM9x(spi, CS, RESET, 915.0)
rfm9x.tx_power = 23
prev_packet = None

#ADDED FOR DUPLICATION CORRECTION
prevOrigins = []
currOrigin = tuple()
isDuplicate = False

#Instatiate a global variable 'persons_counter' to determine number of people detected
#Starting at 0 upon main call
persons_counter = 0

def classify_frame(net, inputQueue, outputQueue):
    # keep looping
    while True:
        # check to see if there is a frame in our input queue
        if not inputQueue.empty():
            # grab the frame from the input queue, resize it, and
            # construct a blob from it
            frame = inputQueue.get()
            frame = cv2.resize(frame, (300, 300))
            blob = cv2.dnn.blobFromImage(frame, 0.007843,
                (300, 300), 127.5)

            # set the blob as input to our deep learning object
            # detector and obtain the detections
            net.setInput(blob)
            detections = net.forward()

            # write the detections to the output queue
            outputQueue.put(detections)

# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-p", "--prototxt", required=True,
    help="path to Caffe 'deploy' prototxt file")
ap.add_argument("-m", "--model", required=True,
    help="path to Caffe pre-trained model")
ap.add_argument("-c", "--confidence", type=float, default=0.2,
    help="minimum probability to filter weak detections")
args = vars(ap.parse_args())

# initialize the list of class labels MobileNet SSD was trained to
# detect, then generate a set of bounding box colors for each class
CLASSES = ["background", "aeroplane", "bicycle", "bird", "boat",
    "bottle", "bus", "car", "cat", "chair", "cow", "diningtable",
    "dog", "horse", "motorbike", "person","pottedplant", "sheep",
    "sofa", "train", "tvmonitor"]

# ADDED TO IGNORE ALL OTHER CLASSES EXCEPT DESIRED CLASS
IGNORE = ["background", "aeroplane", "bicycle", "bird", "boat",
    "bottle", "bus", "car", "cat", "chair", "cow", "diningtable",
    "dog", "horse", "motorbike","pottedplant", "sheep",
    "sofa", "train", "tvmonitor"]

COLORS = np.random.uniform(0, 255, size=(len(CLASSES), 3))

# load our serialized model from disk
print("[INFO] loading model...")
net = cv2.dnn.readNetFromCaffe(args["prototxt"], args["model"])

# initialize the input queue (frames), output queue (detections),
# and the list of actual detections returned by the child process
inputQueue = Queue(maxsize=1)
outputQueue = Queue(maxsize=1)
detections = None

# construct a child process *indepedent* from our main process of
# execution
print("[INFO] starting process...")
p = Process(target=classify_frame, args=(net, inputQueue,
    outputQueue,))
p.daemon = True
p.start()

# initialize the video stream, allow the cammera sensor to warmup,
# and initialize the FPS counter
print("[INFO] starting video stream...")
vs = VideoStream(src=0).start()
# vs = VideoStream(usePiCamera=True).start()
time.sleep(2.0)
fps = FPS().start()

# loop over the frames from the video stream
while True:
    # grab the frame from the threaded video stream, resize it, and
    # grab its imensions
    frame = vs.read()
    frame = imutils.resize(frame, width=400)
    (fH, fW) = frame.shape[:2]

    # if the input queue *is* empty, give the current frame to
    # classify
    if inputQueue.empty():
        inputQueue.put(frame)
        # CLEAR PREVORIGINS SINCE NOTHING IS BEING DETECTED
        #prevOrigins.clear()

    # if the output queue *is not* empty, grab the detections
    if not outputQueue.empty():
        detections = outputQueue.get()

    # check to see if our detectios are not None (and if so, we'll
    # draw the detections on the frame)
    if detections is not None:
        # loop over the detections
        for i in np.arange(0, detections.shape[2]):
            # extract the confidence (i.e., probability) associated
            # with the prediction
            confidence = detections[0, 0, i, 2]

            # filter out weak detections by ensuring the `confidence`
            # is greater than the minimum confidence
            if confidence < args["confidence"]:
                continue

            # otherwise, extract the index of the class label from
            # the `detections`, then compute the (x, y)-coordinates
            # of the bounding box for the object
            idx = int(detections[0, 0, i, 1]) 
            
            # ADDED TO IGNORE ALL OTHER CLASSSES
            if CLASSES[idx] in IGNORE: 
                continue
            
            dims = np.array([fW, fH, fW, fH])
            box = detections[0, 0, i, 3:7] * dims
            (startX, startY, endX, endY) = box.astype("int")
            
            currOrigin = int((endX - startX) / 2), int((endY - startY) / 2)
            
            if not prevOrigins:
                isDuplicate = False                
            else:
                isDuplicate = shouldntTransmit(prevOrigins, currOrigin)
                
            if not isDuplicate:
                # ADD THE NEW ORIGIN TO THE LIST
                prevOrigins.append(currOrigin)
                print(prevOrigins)

                # draw the prediction on the frame
                label = "{}: {:.2f}%".format(CLASSES[idx],
                    confidence * 100)
                cv2.rectangle(frame, (startX, startY), (endX, endY),
                    COLORS[idx], thickness = 2)
                y = startY - 15 if startY - 15 > 15 else startY + 15
                cv2.putText(frame, label, (startX, y),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, COLORS[idx], 2)
                
                if CLASSES[idx] == "person":
                    persons_counter += 1
                    toSend = "1,1,3,2,1,1,1,3,1,-1" # School, Parking Structure Num, Level, Section to be incremented, Increment Value, School, Parking Structure Num, Level, Section to be decremented, Decrement Value
                    values = toSend.split(",")
                    print("Total: {}, School: {}, Parking Structure: {}, Level: {}, Section: {} Incremented By: {}, Section: {} Decremented By: {}".format(persons_counter, values[0], values[1], values[2], values[3], values[4], values[8], values[9]))
                    outbound_flag_data = bytes(toSend,"utf-8")
                    rfm9x.send(outbound_flag_data)

    # show the output frame
    cv2.imshow("Frame", frame)
    key = cv2.waitKey(1) & 0xFF

    # if the `q` key was pressed, break from the loop
    if key == ord("q"):
        break

    # update the FPS counter
    fps.update()

# stop the timer and display FPS information
fps.stop()
print("[INFO] elapsed time: {:.2f}".format(fps.elapsed()))
print("[INFO] approx. FPS: {:.2f}".format(fps.fps()))

# do a bit of cleanup
cv2.destroyAllWindows()
vs.stop()
