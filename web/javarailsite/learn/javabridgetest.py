import tempfile
import subprocess
import time
from threading import Thread
from queue import Queue, Empty
import sys

# process = subprocess.Popen(["java", "-jar", "JavaBridge_2cY5cFo.jar", "C:\\Users\\fab\\AppData\\Local\\Temp\\javabridge\\"], stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

p = subprocess.Popen(["java", "-jar", "JavaBridge_2cY5cFo.jar", "C:\\Users\\fab\\AppData\\Local\\Temp\\javabridge\\"], stdin=subprocess.PIPE, stdout=subprocess.PIPE)

print("Ready now")

time.sleep(5)

print("Submitting file...")

# out, err = process.communicate("JavaBridgeTestData".encode())

p.stdin.write("JavaBridgeTestData".encode())
p.stdin.close()

print(p.stdout.readline().decode())

p.kill()

"""
process.stdin.write("JavaBridgeTestData".encode())
process.stdin.flush()
process.stdin.close()

print("Awaiting response...")

while True:

    line = process.stdout.readline().decode()
    error = process.stderr.readline().decode()

    if(len(line) > 2):
        print(line)

    if(len(error) > 2):
        print(error)
"""

"""

cmd = javabridge.CommandExecution(File("JavaBridge_2cY5cFo.jar"), tempfile.gettempdir())

ready_code = open("JavaBridgeTestData.java", "r").read()

print("submit")
code_id = cmd.submit(ready_code)
print("poll")
result = cmd.poll_result(code_id)
print("result")

print(result.text)
print(result.status)
print(result.lines)

"""
