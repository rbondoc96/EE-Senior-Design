"""
Example for using the RFM9x Radio with Raspberry Pi.
Learn Guide: https://learn.adafruit.com/lora-and-lorawan-for-raspberry-pi
Author: Brent Rubell for Adafruit Industries
"""
# Import Python System Libraries
import time
# Import Blinka Libraries
import busio
from digitalio import DigitalInOut, Direction, Pull
import board
import RPi.GPIO as GPIO
# Import RFM9x
import adafruit_rfm9x

def button_callback(channel):
    global presses
    time.sleep(0.001)
    if GPIO.input(20) == 1:
        button_a_data = bytes("Button A","utf-8")
        rfm9x.send(button_a_data)
        print("Sent Button Press Event!")
    return

# Configure LoRa Radio
CS = DigitalInOut(board.CE1)
RESET = DigitalInOut(board.D25)
spi = busio.SPI(board.SCK, MOSI=board.MOSI, MISO=board.MISO)
rfm9x = adafruit_rfm9x.RFM9x(spi, CS, RESET, 915.0)
rfm9x.tx_power = 23
prev_packet = None

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(20, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

GPIO.add_event_detect(20, GPIO.RISING, callback=button_callback)

while True:
    packet = None

    # check for packet rx
    packet = rfm9x.receive()
    if packet is None:
        pass
        #print("Waiting for Packet")
    else:
        # Display the packet text and rssi
        prev_packet = packet
        packet_text = str(prev_packet, "utf-8")
        print("RX: %s" % packet_text)
        time.sleep(1)

    time.sleep(0.1)