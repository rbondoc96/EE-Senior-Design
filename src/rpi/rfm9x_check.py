"""
Wiring Check, Pi Radio w/RFM9x
 
Learn Guide: https://learn.adafruit.com/lora-and-lorawan-for-raspberry-pi
Author: Brent Rubell for Adafruit Industries
"""
import time
import busio
from digitalio import DigitalInOut, Direction, Pull
import RPi.GPIO as GPIO
import board

# Import the RFM9x radio module.
import adafruit_rfm9x
 
def button_callback(channel):
    global presses
    time.sleep(0.001)
    if GPIO.input(20) == 1:
        print("Rising\t", presses)
        presses += 1
    return
 
# Configure RFM9x LoRa Radio
CS = DigitalInOut(board.CE1)
RESET = DigitalInOut(board.D25)
spi = busio.SPI(board.SCK, MOSI=board.MOSI, MISO=board.MISO)

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(20, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
presses = 1

GPIO.add_event_detect(20, GPIO.RISING, callback=button_callback)

 
# Attempt to set up the RFM9x Module
try:
    rfm9x = adafruit_rfm9x.RFM9x(spi, CS, RESET, 915.0)
    print("RFM9x: Detected")
except RuntimeError:
    # Thrown on version mismatch
    print("RFM9x: ERROR") 

print("Press CNTRL + C to quit\n\n")

try:
    while True:
        pass
except KeyboardInterrupt:
    pass
finally:
    print("\nRelease our used channel")
    GPIO.cleanup(20)
