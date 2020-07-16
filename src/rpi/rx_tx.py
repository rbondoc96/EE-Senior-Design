#!/usr/bin/env python3

''' LoRa Router & LoRa Remote Client Driver
Last Modified: 05.05.2019
Description: 
	2 LoRa modules are needed on this Pi
		RX: Receiving packets from the camera Pi's
		TX: Transmitting packets from PS1 to Sean's house

Pin-Out:
	LoRa_RX
		Vin ------ 3V3
		GND ------ GND
		EN  ------ NC 	(not connected)
		G0	------ BCM#5 / Pin 29
		SCK	------ BCM#11 / Pin 32
		MISO ----- BCM#9 / Pin 21
		MOSI ----- BCM#10 / Pin 19
		CS	------ BCM#7 / Pin 26	(CE1)
		RST	------ BCM#25 / Pin 22

	LoRa_TX
		Vin	------ 3V3
		GND	------ GND
		EN	------ NC
		G0	------ BCM#23 / Pin 16
		SCK	------ BCM#11 / Pin 32
		MISO ----- BCM#9 / Pin 21
		MOSI ----- BCM#10 / Pin 19
		CS	------ BCM#8 / Pin 24	(CE0)
		RST ------ BCM#24 / Pin 18
'''

# Import Python system libraries
import time
from multiprocessing import Process
from multiprocessing import Queue

# Import RFM9x and GPIO libraries
import busio
from digitalio import DigitalInOut, Direction, Pull
import RPi.GPIO as GPIO
import board
import adafruit_rfm9x

def transmit(queue):
	# Configure RPi for SPI communication with RFM9x LoRa Module - TRANSMITTING Module
	CS2 = DigitalInOut(board.CE0)
	RESET2 = DigitalInOut(board.D24)

	rfm9x_tx = adafruit_rfm9x.RFM9x(spi, CS2, RESET2, 915.0)
	rfm9x_tx.tx_power = 23
	print("RFM9x_TX: Detected")
	
	# Continuously send one packet from the queue
	while True:
		if not queue.empty():
			data = queue.get()
			rfm9x_tx.send(data)
			print("Data sent: " + str(data, 'utf-8')
			time.sleep(50e-3)		# 50ms delay between transmissions
				

# Configure RPi for SPI communication with RFM9x LoRa Module - RECEIVING Module
CS1 = DigitalInOut(board.CE1)
RESET1 = DigitalInOut(board.D25)
spi = busio.SPI(board.SCK, MOSI=board.MOSI, MISO=board.MISO)

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(20, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
rfm9x_rx = adafruit_rfm9x.RFM9x(spi, CS1, RESET1, 915.0)
print("RFM9x_RX: Detected")

# Initialize transmit daemon subprocess and interprocess variable, <queue>
queue = Queue(maxsize=5)

print("[INFO] starting process...")
p = Process(target=transmit, args=(queue,))
p.daemon = True
p.start()


while True:
	# Continously receive data packets from LoRa modules
	rcv = rfm9x_rx.receive()
	
	# If the received packet is valid, place in the queue
	if rcv is not None:
		queue.put(rcv)
	else:
		pass
	
	time.sleep(10e-3)		# Required 10ms delay between receptions
	
	
'''
	threshold = 0
	
	if len(buff) < threshold:
		rcv = rfm9x_rx.receive()
		if rcv is not None:
			data = str(rcv, 'utf-8')
			print("Received: " + data)
			buff.append(data)
		else:
			time.sleep(10e-3)		# Required 10ms delay for reception
			pass
	else:
		# Package data to send over LoRa
		out_str = ""
		print("--- Data Received ---")
		for j in range(len(buff)):
			print("buff[{}]: ".format(j) + buff[j])
			if j < len(buff) - 1:
				out_str += buff[j] + ","
			else:
				out_str += buff[j] 
		rfm9x_rx.send(bytes(out_str, "utf-8"))
'''
