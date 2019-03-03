import subprocess
import json
import base64
import time
import os

class CommandExecution:

    def __init__(self, java_bridge_jar, directory):

        # handle file system

        self.java_bridge_jar = java_bridge_jar
        self.temp_path = directory + "/javabridge/"

        if not os.path.exists(self.temp_path):
            os.mkdir(self.temp_path)
        
        # init starting data

        self.counter = 0
        self.alphabet = "ABCDEFGHIJKLMNOP"
        
        
        return

    def submit(self, code):

        # get id for code

        code_id = self.get_code_id()

        # write code to file

        f = open(self.temp_path + code_id + '.java', 'w+')
        f.write(code)
        f.close()

        # call process
        
        p = subprocess.Popen(["java", "-jar", "JavaBridge_2cY5cFo.jar", "C:\\Users\\fab\\AppData\\Local\\Temp\\javabridge\\"], stdin=subprocess.PIPE, stdout=subprocess.PIPE)
        
        p.stdin.write(code_id.encode())
        p.stdin.close()
        
        # read process output
        
        out = p.stdout.readline().decode()
        p.kill()
        
        print("line: " + out)
        
        # parse output as JSON
        
        json_data = json.loads(out)
        
        name = json_data['name']
        state = json_data['state']
        output = base64.b64decode(json_data['output']).decode()
        error = base64.b64decode(json_data['error']).decode()
        lines = json_data['lines']
        
        # return the code id for later

        return Result(name, state, output, error, lines)

    def get_code_id(self):

        # get next id number
        self.counter += 1

        # convert number to letters

        code_id = ""
        temp_counter = self.counter

        while temp_counter > 16:
            code_id += self.alphabet[temp_counter % 16]
            temp_counter /= 16

        code_id += self.alphabet[temp_counter]

        # return generated id

        return code_id

    def close(self):

        self.process.stdin.write(".exit".encode("UTF-8"))
        self.process.stdin.close()

        return


class Result:

    def __init__(self, name, state, output, error, lines):
        self.name = name
        self.state = state
        self.output = output
        self.error = error
        self.lines = lines
        return
