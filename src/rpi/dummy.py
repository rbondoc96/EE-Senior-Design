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

# Configure RFM9x LoRa Radio
CS = DigitalInOut(board.CE1)
RESET = DigitalInOut(board.D25)
spi = busio.SPI(board.SCK, MOSI=board.MOSI, MISO=board.MISO)

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(20, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
rfm9x = adafruit_rfm9x.RFM9x(spi, CS, RESET, 915.0)
rfm9x.tx_power = 23

print("RFM9x: Detected")

# School, Parking Structure No., Level, Section to Increment, Increment val, school, parking structure no., level, section to decrement, decrement val

for i in range(3):
	if i == 1:
		toSend = "333"
	else:
		toSend = "444"

	dataOut = bytes(toSend, "utf-8")
	rfm9x.send(dataOut)
	print("Sending: " + toSend)
	print("Sent data {} times".format(i+1))
