import tempfile
import javabridge

class File:
    
    def __init__(self, path):
        self.path = path

cmd = javabridge.CommandExecution(File("JavaBridge_2cY5cFo"), tempfile.gettempdir())

ready_code = open("JavaBridgeTestData.java", "r").read()

print("submit")
code_id = cmd.submit(ready_code)
print("poll")
result = cmd.poll_result(code_id)
print("result")

print(result.text)
print(result.status)
print(result.lines)

