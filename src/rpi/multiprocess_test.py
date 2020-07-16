import time
from multiprocessing import Process
from multiprocessing import Queue

def testing(queue):
	while True:	
		if not queue.empty():
			print("Contents: " + str(queue.get(), 'utf-8'))
		
		#if queue.full():
		#	while not queue.empty():
		#		print("Contents: " + str(queue.get(), 'utf-8'))
			


queue = Queue(maxsize=3)

print("[INFO] starting process...")
p = Process(target=testing, args=(queue,))
p.daemon = True
p.start()


while True:
	random = input("Type something: ")
	print("Input: " + random)
	queue.put(bytes(random, 'utf-8'))
	time.sleep(10e-3)
	
